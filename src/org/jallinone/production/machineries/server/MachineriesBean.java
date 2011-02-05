package org.jallinone.production.machineries.server;

import org.openswing.swing.server.*;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.message.send.java.LookupValidationParams;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.production.machineries.java.*;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage machineries.</p>
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
public class MachineriesBean  implements Machineries {


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




  public MachineriesBean() {
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public MachineryVO getMachinery() {
	  throw new UnsupportedOperationException();
  }


  /**
   * Business logic to execute.
   */
  public VOListResponse loadMachineries(GridParams gridParams,String serverLanguageId,String username,ArrayList companiesList,ArrayList customizedFields) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      // retrieve companies list...
      String companies = "";
      if (gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01)!=null) {
        companies = "'"+gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01)+"'";
      }
      else {
        for(int i=0;i<companiesList.size();i++)
          companies += "'"+companiesList.get(i).toString()+"',";
        companies = companies.substring(0,companies.length()-1);
      }

      String sql =
          "select PRO03_MACHINERIES.COMPANY_CODE_SYS01,PRO03_MACHINERIES.MACHINERY_CODE,PRO03_MACHINERIES.PROGRESSIVE_SYS10,SYS10_TRANSLATIONS.DESCRIPTION,PRO03_MACHINERIES.ENABLED, "+
          "PRO03_MACHINERIES.CURRENCY_CODE_REG03,PRO03_MACHINERIES.VALUE,PRO03_MACHINERIES.DURATION,PRO03_MACHINERIES.FINITE_CAPACITY "+
          "from PRO03_MACHINERIES,SYS10_TRANSLATIONS where "+
          "PRO03_MACHINERIES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "PRO03_MACHINERIES.ENABLED='Y' and "+
          "PRO03_MACHINERIES.COMPANY_CODE_SYS01 in ("+companies+")";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("machineryCodePRO03","PRO03_MACHINERIES.MACHINERY_CODE");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("progressiveSys10PRO03","PRO03_MACHINERIES.PROGRESSIVE_SYS10");
      attribute2dbField.put("enabledPRO03","PRO03_MACHINERIES.ENABLED");
      attribute2dbField.put("companyCodeSys01PRO03","PRO03_MACHINERIES.COMPANY_CODE_SYS01");
      attribute2dbField.put("finiteCapacityPRO03","PRO03_MACHINERIES.FINITE_CAPACITY");
      attribute2dbField.put("valuePRO03","PRO03_MACHINERIES.VALUE");
      attribute2dbField.put("durationPRO03","PRO03_MACHINERIES.DURATION");
      attribute2dbField.put("currencyCodeReg03PRO03","PRO03_MACHINERIES.CURRENCY_CODE_REG03");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);

      // read from PRO03 table...
      Response answer = CustomizeQueryUtil.getQuery(
    		  conn,
    		  new UserSessionParameters(username),
    		  sql,
    		  values,
    		  attribute2dbField,
    		  MachineryVO.class,
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
    	Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching machineries list",ex);
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
  public VOListResponse updateMachineries(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      MachineryVO oldVO = null;
      MachineryVO newVO = null;
      Response res = null;

      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (MachineryVO)oldVOs.get(i);
        newVO = (MachineryVO)newVOs.get(i);

        // update SYS10 table...
        TranslationUtils.updateTranslation(oldVO.getDescriptionSYS10(),newVO.getDescriptionSYS10(),newVO.getProgressiveSys10PRO03(),serverLanguageId,conn);

        HashSet pkAttrs = new HashSet();
        pkAttrs.add("companyCodeSys01PRO03");
        pkAttrs.add("machineryCodePRO03");

        HashMap attribute2dbField = new HashMap();
        attribute2dbField.put("machineryCodePRO03","MACHINERY_CODE");
        attribute2dbField.put("progressiveSys10PRO03","PROGRESSIVE_SYS10");
        attribute2dbField.put("enabledPRO03","ENABLED");
        attribute2dbField.put("companyCodeSys01PRO03","COMPANY_CODE_SYS01");
        attribute2dbField.put("finiteCapacityPRO03","FINITE_CAPACITY");
        attribute2dbField.put("valuePRO03","VALUE");
        attribute2dbField.put("durationPRO03","DURATION");
        attribute2dbField.put("currencyCodeReg03PRO03","CURRENCY_CODE_REG03");

        res = new CustomizeQueryUtil().updateTable(
            conn,
            new UserSessionParameters(username),
            pkAttrs,
            oldVO,
            newVO,
            "PRO03_MACHINERIES",
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
    	Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing machineries",ex);
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
  public VOListResponse validateMachineryCode(LookupValidationParams validationPars,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      String sql =
          "select PRO03_MACHINERIES.COMPANY_CODE_SYS01,PRO03_MACHINERIES.MACHINERY_CODE,PRO03_MACHINERIES.PROGRESSIVE_SYS10,SYS10_TRANSLATIONS.DESCRIPTION,PRO03_MACHINERIES.ENABLED, "+
          "PRO03_MACHINERIES.CURRENCY_CODE_REG03,PRO03_MACHINERIES.VALUE,PRO03_MACHINERIES.DURATION,PRO03_MACHINERIES.FINITE_CAPACITY "+
          "from PRO03_MACHINERIES,SYS10_TRANSLATIONS where "+
          "PRO03_MACHINERIES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "PRO03_MACHINERIES.ENABLED='Y' and "+
          "PRO03_MACHINERIES.MACHINERY_CODE='"+validationPars.getCode()+"' and "+
          "PRO03_MACHINERIES.COMPANY_CODE_SYS01='"+validationPars.getLookupValidationParameters().get(ApplicationConsts.COMPANY_CODE_SYS01)+"'";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("machineryCodePRO03","PRO03_MACHINERIES.MACHINERY_CODE");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("progressiveSys10PRO03","PRO03_MACHINERIES.PROGRESSIVE_SYS10");
      attribute2dbField.put("enabledPRO03","PRO03_MACHINERIES.ENABLED");
      attribute2dbField.put("companyCodeSys01PRO03","PRO03_MACHINERIES.COMPANY_CODE_SYS01");
      attribute2dbField.put("finiteCapacityPRO03","PRO03_MACHINERIES.FINITE_CAPACITY");
      attribute2dbField.put("valuePRO03","PRO03_MACHINERIES.VALUE");
      attribute2dbField.put("durationPRO03","PRO03_MACHINERIES.DURATION");
      attribute2dbField.put("currencyCodeReg03PRO03","PRO03_MACHINERIES.CURRENCY_CODE_REG03");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);

      GridParams gridParams = new GridParams();

      // read from PRO03 table...
      Response answer = CustomizeQueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          MachineryVO.class,
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
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while validating machinery code",ex);
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
  public VOListResponse insertMachineries(ArrayList list,String serverLanguageId,String username,ArrayList companiesList,ArrayList customizedFields) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      MachineryVO vo = null;
      String companyCode = companiesList.get(0).toString();
      BigDecimal progressiveSYS10 = null;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("machineryCodePRO03","MACHINERY_CODE");
      attribute2dbField.put("progressiveSys10PRO03","PROGRESSIVE_SYS10");
      attribute2dbField.put("enabledPRO03","ENABLED");
      attribute2dbField.put("companyCodeSys01PRO03","COMPANY_CODE_SYS01");
      attribute2dbField.put("finiteCapacityPRO03","FINITE_CAPACITY");
      attribute2dbField.put("valuePRO03","VALUE");
      attribute2dbField.put("durationPRO03","DURATION");
      attribute2dbField.put("currencyCodeReg03PRO03","CURRENCY_CODE_REG03");
      Response res = null;

      for(int i=0;i<list.size();i++) {
        vo = (MachineryVO)list.get(i);
        vo.setEnabledPRO03("Y");
        if (vo.getFiniteCapacityPRO03() == null)
          vo.setFiniteCapacityPRO03("N");
        if (vo.getCompanyCodeSys01PRO03()==null)
          vo.setCompanyCodeSys01PRO03(companyCode);

        // insert record in SYS10...
        progressiveSYS10 = TranslationUtils.insertTranslations(vo.getDescriptionSYS10(),vo.getCompanyCodeSys01PRO03(),conn);
        vo.setProgressiveSys10PRO03(progressiveSYS10);

        // insert into PRO03...
        res = CustomizeQueryUtil.insertTable(
            conn,
            new UserSessionParameters(username),
            vo,
            "PRO03_MACHINERIES",
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

      return new VOListResponse(list,false,list.size());
    }
    catch (Throwable ex) {
    	Logger.error(username, this.getClass().getName(),
    			"executeCommand", "Error while inserting new machineries", ex);
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
  public VOResponse deleteMachineries(ArrayList list,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      stmt = conn.createStatement();

      MachineryVO vo = null;
      for(int i=0;i<list.size();i++) {
        // logically delete the record in PRO03...
        vo = (MachineryVO)list.get(i);
        stmt.execute("update PRO03_MACHINERIES set ENABLED='N' where "+
                     "COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01PRO03()+"' and "+
                     "MACHINERY_CODE='"+vo.getMachineryCodePRO03()+"'"
        );
      }

      return  new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
    	Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing machineries",ex);
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

