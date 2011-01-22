package org.jallinone.scheduler.callouts.server;

import org.openswing.swing.server.*;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.system.progressives.server.CompanyProgressiveUtils;
import org.jallinone.system.server.*;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.scheduler.callouts.java.*;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage call-out type.</p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 *
 * <p> This file is part of JAllInOne ERP/CRM application.
 * This application is free software; you can redistribute it and/or
 * modify it under the terms of the (LGPL) Lesser General Public
 * License as published by the Free Software Foundation;
 *
 *                GNU LESSER GENERAL PUBLIC LICENSE
 *                 Version 2.1, February 1999
 *
 * This application is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free
 * Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 *       The author may be contacted at:
 *           maurocarniel@tin.it</p>
 *
 * @author Mauro Carniel
 * @version 1.0
 */
public class CallOutTypesBean  implements CallOutTypes {


  private DataSource dataSource; 

  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  /** external connection */
  private Connection conn = null;
  
  /**
   * Set external connection. 
   */
  public void setConn(Connection conn) {
    this.conn = conn;
  }

  /**
   * Create local connection
   */
  public Connection getConn() throws Exception {
    
    Connection c = dataSource.getConnection(); c.setAutoCommit(false); return c;
  }




  public CallOutTypesBean() {
  }




  /**
   * Business logic to execute.
   */
  public VOResponse insertCallOutType(CallOutTypeVO vo,String serverLanguageId,String username,ArrayList companiesList) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      vo.setEnabledSCH11("Y");

      String companyCode = companiesList.get(0).toString();
      if (vo.getCompanyCodeSys01SCH11()==null)
        vo.setCompanyCodeSys01SCH11(companyCode);

      // generate PROGRESSIVE_HIE02 value...
      stmt = conn.createStatement();
      BigDecimal progressiveHIE02 = CompanyProgressiveUtils.getInternalProgressive(vo.getCompanyCodeSys01SCH11(),"HIE02_HIERARCHIES","PROGRESSIVE",conn);
      BigDecimal progressiveHIE01 = TranslationUtils.insertTranslations(vo.getDescriptionSYS10(),vo.getCompanyCodeSys01SCH11(),conn);
      stmt.execute("INSERT INTO HIE02_HIERARCHIES(PROGRESSIVE,COMPANY_CODE_SYS01,ENABLED) VALUES("+progressiveHIE02+",'"+vo.getCompanyCodeSys01SCH11()+"','Y')");
      stmt.execute("INSERT INTO HIE01_LEVELS(PROGRESSIVE,PROGRESSIVE_HIE02,LEV,ENABLED) VALUES("+progressiveHIE01+","+progressiveHIE02+",0,'Y')");
      stmt.execute("UPDATE HIE02_HIERARCHIES SET PROGRESSIVE_HIE01="+progressiveHIE01+" WHERE PROGRESSIVE="+progressiveHIE02);
      vo.setProgressiveHie02SCH11(progressiveHIE02);
      vo.setProgressiveHie01HIE02(progressiveHIE01);

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SCH11","COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveHie02SCH11","PROGRESSIVE_HIE02");
      attribute2dbField.put("enabledSCH11","ENABLED");

      // insert into SCH11...
      Response res = QueryUtil.insertTable(
          conn,
          new UserSessionParameters(username),
          vo,
          "SCH11_CALL_OUT_TYPES",
          attribute2dbField,
          "Y",
          "N",
          null,
          true
      );
      if (res.isError())
    	  throw new Exception(res.getErrorMessage());


      return new VOResponse(vo);
    }
    catch (Throwable ex) {
      Logger.error(username, this.getClass().getName(),
                   "executeCommand", "Error while inserting a new call-out", ex);
      try {
    		if (this.conn==null && conn!=null)
    			// rollback only local connection
    			conn.rollback();
    	}
    	catch (Exception ex3) {
    	}

      throw new Exception(ex.getMessage());
    }
    finally {
        try {
            stmt.close();
        }
        catch (Exception exx) {}
        try {
            if (this.conn==null && conn!=null) {
                // close only local connection
                conn.commit();
                conn.close();
            }

        }
        catch (Exception exx) {}
    }


  }




  /**
   * Business logic to execute.
   */
  public VOListResponse loadCallOutTypes(GridParams gridParams,String serverLanguageId,String username,ArrayList companiesList,ArrayList customizedFields ) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      // retrieve companies list...
      String companies = "";
      for(int i=0;i<companiesList.size();i++)
        companies += "'"+companiesList.get(i).toString()+"',";
      companies = companies.substring(0,companies.length()-1);

      String sql =
          "select SCH11_CALL_OUT_TYPES.COMPANY_CODE_SYS01,SCH11_CALL_OUT_TYPES.PROGRESSIVE_HIE02,SCH11_CALL_OUT_TYPES.ENABLED,SYS10_TRANSLATIONS.DESCRIPTION,HIE02_HIERARCHIES.PROGRESSIVE_HIE01 from SCH11_CALL_OUT_TYPES,SYS10_TRANSLATIONS,HIE02_HIERARCHIES where "+
          "SCH11_CALL_OUT_TYPES.PROGRESSIVE_HIE02=HIE02_HIERARCHIES.PROGRESSIVE and "+
          "HIE02_HIERARCHIES.PROGRESSIVE_HIE01=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "SCH11_CALL_OUT_TYPES.COMPANY_CODE_SYS01 in ("+companies+") and "+
          "SCH11_CALL_OUT_TYPES.ENABLED='Y'";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SCH11","SCH11_CALL_OUT_TYPES.COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveHie02SCH11","SCH11_CALL_OUT_TYPES.PROGRESSIVE_HIE02");
      attribute2dbField.put("enabledSCH11","SCH11_CALL_OUT_TYPES.ENABLED");
      attribute2dbField.put("progressiveHie01HIE02","HIE02_HIERARCHIES.PROGRESSIVE_HIE01");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);


      // read from SCH11 table...
      Response answer = CustomizeQueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          CallOutTypeVO.class,
          "Y",
          "N",
          null,
          gridParams,
          true,
          customizedFields 
      );
      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching call-outs types list",ex);
      throw new Exception(ex.getMessage());
    }
    finally {
        try {
            if (this.conn==null && conn!=null) {
                // close only local connection
                conn.commit();
                conn.close();
            }

        }
        catch (Exception exx) {}
    }


  }



  /**
   * Business logic to execute.
   */
  public VOListResponse updateCallOutTypes(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      CallOutTypeVO oldVO = null;
      CallOutTypeVO newVO = null;

      HashSet pk = new HashSet();
      pk.add("companyCodeSys01SCH11");
      pk.add("progressiveHie02SCH11");

      //CustomizedWindows cust = ((JAIOUserSessionParameters)userSessionPars).getCustomizedWindows();

      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (CallOutTypeVO)oldVOs.get(i);
        newVO = (CallOutTypeVO)newVOs.get(i);

        // update root description...
        TranslationUtils.updateTranslation(
            oldVO.getDescriptionSYS10(),
            newVO.getDescriptionSYS10(),
            newVO.getProgressiveHie01HIE02(),
            serverLanguageId,
            conn
        );

      }

      return  new VOListResponse(newVOs,false,newVOs.size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing call-out types",ex);
      try {
    		if (this.conn==null && conn!=null)
    			// rollback only local connection
    			conn.rollback();
    	}
    	catch (Exception ex3) {
    	}

      throw new Exception(ex.getMessage());
    }
    finally {
        try {
            if (this.conn==null && conn!=null) {
                // close only local connection
                conn.commit();
                conn.close();
            }

        }
        catch (Exception exx) {}
    }


  }



  /**
   * Business logic to execute.
   */
  public VOResponse deleteCallOutType(ArrayList list,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      stmt = conn.createStatement();

      CallOutTypeVO vo = null;
      for(int i=0;i<list.size();i++) {
        // logically delete the record in SCH11...
        vo = (CallOutTypeVO)list.get(i);
        stmt.execute("update SCH11_CALL_OUT_TYPES set ENABLED='N' where COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01SCH11()+"' and PROGRESSIVE_HIE02="+vo.getProgressiveHie02SCH11());
      }

      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing call-out types",ex);
      try {
    		if (this.conn==null && conn!=null)
    			// rollback only local connection
    			conn.rollback();
    	}
    	catch (Exception ex3) {
    	}

      throw new Exception(ex.getMessage());
    }
    finally {
        try {
            stmt.close();
        }
        catch (Exception exx) {}
        try {
            if (this.conn==null && conn!=null) {
                // close only local connection
                conn.commit();
                conn.close();
            }

        }
        catch (Exception exx) {}
    }


  }



}

