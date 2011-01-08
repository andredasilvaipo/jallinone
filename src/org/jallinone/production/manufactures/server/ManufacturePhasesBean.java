package org.jallinone.production.manufactures.server;

import org.openswing.swing.server.*;

import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.system.progressives.server.CompanyProgressiveUtils;
import org.jallinone.system.server.*;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.production.manufactures.java.*;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage manufactures phases.</p>
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
public class ManufacturePhasesBean  implements ManufacturePhases {


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




  public ManufacturePhasesBean() {
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public ManufacturePhaseVO getManufacturePhase() {
	  throw new UnsupportedOperationException();
  }

  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public ManufactureVO getManufacture() {
	  throw new UnsupportedOperationException();
  }
  

  /**
   * Business logic to execute.
   */
  public VOListResponse loadManufacturePhases(GridParams gridParams,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      ManufactureVO vo = (ManufactureVO)gridParams.getOtherGridParams().get(ApplicationConsts.MANUFACTURE_VO);

      String sql =
          "select PRO02_MANUFACTURE_PHASES.COMPANY_CODE_SYS01,PRO02_MANUFACTURE_PHASES.PROGRESSIVE,SYS10_TRANSLATIONS.DESCRIPTION,PRO02_MANUFACTURE_PHASES.MANUFACTURE_CODE_PRO01,"+
          "PRO02_MANUFACTURE_PHASES.PROGRESSIVE_SYS10,PRO02_MANUFACTURE_PHASES.QTY,PRO02_MANUFACTURE_PHASES.TASK_CODE_REG07,"+
          "PRO02_MANUFACTURE_PHASES.MACHINERY_CODE_PRO03,PRO02_MANUFACTURE_PHASES.PHASE_NUMBER,PRO02_MANUFACTURE_PHASES.MANUFACTURE_TYPE,"+
          "PRO02_MANUFACTURE_PHASES.DURATION,PRO02_MANUFACTURE_PHASES.VALUE,PRO02_MANUFACTURE_PHASES.COMPLETION_PERC,PRO02_MANUFACTURE_PHASES.OPERATION_CODE_PRO04,"+
          "PRO02_MANUFACTURE_PHASES.NOTE,PRO02_MANUFACTURE_PHASES.OPERATION_CODE_PRO04,PRO04_ALIAS.DESCRIPTION,TASKS.DESCRIPTION,PRO03.DESCRIPTION "+
          "from SYS10_TRANSLATIONS,PRO02_MANUFACTURE_PHASES "+
          "LEFT OUTER JOIN "+
          "(select SYS10_TRANSLATIONS.DESCRIPTION,PRO04_OPERATIONS.OPERATION_CODE,PRO04_OPERATIONS.COMPANY_CODE_SYS01 "+
          "from PRO04_OPERATIONS,SYS10_TRANSLATIONS where "+
          "PRO04_OPERATIONS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=?) PRO04_ALIAS ON "+
          "PRO04_ALIAS.COMPANY_CODE_SYS01=PRO02_MANUFACTURE_PHASES.COMPANY_CODE_SYS01 and "+
          "PRO04_ALIAS.OPERATION_CODE=PRO02_MANUFACTURE_PHASES.SUBST_OPERATION_CODE_PRO04 "+
          "LEFT OUTER JOIN "+
          "(select SYS10_TRANSLATIONS.DESCRIPTION,REG07_TASKS.COMPANY_CODE_SYS01,REG07_TASKS.TASK_CODE "+
          "from REG07_TASKS,SYS10_TRANSLATIONS where "+
          "REG07_TASKS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=?) TASKS ON "+
          "TASKS.COMPANY_CODE_SYS01=PRO02_MANUFACTURE_PHASES.COMPANY_CODE_SYS01 and "+
          "TASKS.TASK_CODE=PRO02_MANUFACTURE_PHASES.TASK_CODE_REG07 "+
          "LEFT OUTER JOIN "+
          "(select SYS10_TRANSLATIONS.DESCRIPTION,PRO03_MACHINERIES.COMPANY_CODE_SYS01,PRO03_MACHINERIES.MACHINERY_CODE "+
          "from PRO03_MACHINERIES,SYS10_TRANSLATIONS where "+
          "PRO03_MACHINERIES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=?) PRO03 ON "+
          "PRO03.COMPANY_CODE_SYS01=PRO02_MANUFACTURE_PHASES.COMPANY_CODE_SYS01 and "+
          "PRO03.MACHINERY_CODE=PRO02_MANUFACTURE_PHASES.MACHINERY_CODE_PRO03 "+
          "where "+
          "PRO02_MANUFACTURE_PHASES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "PRO02_MANUFACTURE_PHASES.COMPANY_CODE_SYS01=? and "+
          "PRO02_MANUFACTURE_PHASES.MANUFACTURE_CODE_PRO01=? ";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01PRO02","PRO02_MANUFACTURE_PHASES.COMPANY_CODE_SYS01");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("manufactureCodePro01PRO02","PRO02_MANUFACTURE_PHASES.MANUFACTURE_CODE_PRO01");
      attribute2dbField.put("progressivePRO02","PRO02_MANUFACTURE_PHASES.PROGRESSIVE");
      attribute2dbField.put("progressiveSys10PRO02","PRO02_MANUFACTURE_PHASES.PROGRESSIVE_SYS10");
      attribute2dbField.put("qtyPRO02","PRO02_MANUFACTURE_PHASES.QTY");
      attribute2dbField.put("taskCodeReg07PRO02","PRO02_MANUFACTURE_PHASES.TASK_CODE_REG07");
      attribute2dbField.put("machineryCodePro03PRO02","PRO02_MANUFACTURE_PHASES.MACHINERY_CODE_PRO03");
      attribute2dbField.put("phaseNumberPRO02","PRO02_MANUFACTURE_PHASES.PHASE_NUMBER");
      attribute2dbField.put("manufactureTypePRO02","PRO02_MANUFACTURE_PHASES.MANUFACTURE_TYPE");
      attribute2dbField.put("durationPRO02","PRO02_MANUFACTURE_PHASES.DURATION");
      attribute2dbField.put("valuePRO02","PRO02_MANUFACTURE_PHASES.VALUE");
      attribute2dbField.put("completionPercPRO02","PRO02_MANUFACTURE_PHASES.COMPLETION_PERC");
      attribute2dbField.put("substOperationCodePro04PRO02","PRO02_MANUFACTURE_PHASES.SUBST_OPERATION_CODE_PRO04");
      attribute2dbField.put("notePRO02","PRO02_MANUFACTURE_PHASES.NOTE");
      attribute2dbField.put("description2","PRO04_ALIAS.DESCRIPTION");
      attribute2dbField.put("taskDescriptionSYS10","TASKS.DESCRIPTION");
      attribute2dbField.put("machineryDescriptionSYS10","PRO03.DESCRIPTION");
      attribute2dbField.put("operationCodePro04PRO02","PRO02_MANUFACTURE_PHASES.OPERATION_CODE_PRO04");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(serverLanguageId);
      values.add(serverLanguageId);
      values.add(serverLanguageId);
      values.add(vo.getCompanyCodeSys01PRO01());
      values.add(vo.getManufactureCodePRO01());

      // read from PRO02 table...
      Response answer = CustomizeQueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          ManufacturePhaseVO.class,
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
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching manufacture phases",ex);
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
  public VOListResponse updateManufacturePhases(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      ManufacturePhaseVO oldVO = null;
      ManufacturePhaseVO newVO = null;
      Response res = null;

      HashSet pkAttrs = new HashSet();
      pkAttrs.add("companyCodeSys01PRO02");
      pkAttrs.add("progressivePRO02");

      HashMap attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01PRO02","COMPANY_CODE_SYS01");
      attribute2dbField.put("manufactureCodePro01PRO02","MANUFACTURE_CODE_PRO01");
      attribute2dbField.put("progressivePRO02","PROGRESSIVE");
      attribute2dbField.put("progressiveSys10PRO02","PROGRESSIVE_SYS10");
      attribute2dbField.put("qtyPRO02","QTY");
      attribute2dbField.put("taskCodeReg07PRO02","TASK_CODE_REG07");
      attribute2dbField.put("machineryCodePro03PRO02","MACHINERY_CODE_PRO03");
      attribute2dbField.put("phaseNumberPRO02","PHASE_NUMBER");
      attribute2dbField.put("manufactureTypePRO02","MANUFACTURE_TYPE");
      attribute2dbField.put("durationPRO02","DURATION");
      attribute2dbField.put("valuePRO02","VALUE");
      attribute2dbField.put("completionPercPRO02","COMPLETION_PERC");
      attribute2dbField.put("notePRO02","NOTE");
      attribute2dbField.put("operationCodePro04PRO02","OPERATION_CODE_PRO04");
      attribute2dbField.put("substOperationCodePro04PRO02","SUBST_OPERATION_CODE_PRO04");

      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (ManufacturePhaseVO)oldVOs.get(i);
        newVO = (ManufacturePhaseVO)newVOs.get(i);

        res = new CustomizeQueryUtil().updateTable(
            conn,
            new UserSessionParameters(username),
            pkAttrs,
            oldVO,
            newVO,
            "PRO02_MANUFACTURE_PHASES",
            attribute2dbField,
            "Y",
            "N",
            null,
            true,
            customizedFields
        );
        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }
      }

      return new VOListResponse(newVOs,false,newVOs.size());
    }
    catch (Throwable ex) {
    	Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing manufactures phases",ex);
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
  public VOListResponse insertManufacturePhases(ArrayList vos,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      ManufacturePhaseVO vo = null;
      Response res = null;

      for(int i=0;i<vos.size();i++) {
        vo = (ManufacturePhaseVO)vos.get(i);
        vo.setProgressiveSys10PRO02(TranslationUtils.insertTranslations(vo.getDescriptionSYS10(),vo.getCompanyCodeSys01PRO02(),conn));
        vo.setProgressivePRO02(CompanyProgressiveUtils.getInternalProgressive(vo.getCompanyCodeSys01PRO02(),"PRO02_MANUFACTURE_PHASES","PROGRESSIVE",conn));

        Map attribute2dbField = new HashMap();
        attribute2dbField.put("companyCodeSys01PRO02","COMPANY_CODE_SYS01");
        attribute2dbField.put("manufactureCodePro01PRO02","MANUFACTURE_CODE_PRO01");
        attribute2dbField.put("progressivePRO02","PROGRESSIVE");
        attribute2dbField.put("progressiveSys10PRO02","PROGRESSIVE_SYS10");
        attribute2dbField.put("qtyPRO02","QTY");
        attribute2dbField.put("taskCodeReg07PRO02","TASK_CODE_REG07");
        attribute2dbField.put("machineryCodePro03PRO02","MACHINERY_CODE_PRO03");
        attribute2dbField.put("phaseNumberPRO02","PHASE_NUMBER");
        attribute2dbField.put("manufactureTypePRO02","MANUFACTURE_TYPE");
        attribute2dbField.put("durationPRO02","DURATION");
        attribute2dbField.put("valuePRO02","VALUE");
        attribute2dbField.put("completionPercPRO02","COMPLETION_PERC");
        attribute2dbField.put("substOperationCodePro04PRO02","SUBST_OPERATION_CODE_PRO04");
        attribute2dbField.put("notePRO02","NOTE");
        attribute2dbField.put("operationCodePro04PRO02","OPERATION_CODE_PRO04");

        // insert into PRO02...
        res = CustomizeQueryUtil.insertTable(
            conn,
            new UserSessionParameters(username),
            vo,
            "PRO02_MANUFACTURE_PHASES",
            attribute2dbField,
            "Y",
            "N",
            null,
            true,
            customizedFields
        );
        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }

      }


      return new VOListResponse(vos,false,vos.size());
    }
    catch (Throwable ex) {
    	Logger.error(username, this.getClass().getName(),
    			"executeCommand", "Error while inserting new manufacture phases", ex);
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
  public VOResponse deleteManufacturePhases(ArrayList list,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      stmt = conn.createStatement();

      ManufacturePhaseVO vo = null;
      for(int i=0;i<list.size();i++) {
        // phisically delete the record in PRO02...
        vo = (ManufacturePhaseVO)list.get(i);
        stmt.execute(
          "delete from PRO02_MANUFACTURE_PHASES where "+
          "COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01PRO02()+"' and "+
          "PROGRESSIVE="+vo.getProgressivePRO02());
      }

      return  new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
    	Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing manufacture phases",ex);
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

