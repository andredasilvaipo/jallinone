package org.jallinone.production.manufactures.server;

import org.openswing.swing.server.*;

import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.message.send.java.LookupValidationParams;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.production.manufactures.java.*;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage operations used in manufacture cycles.</p>
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
public class OperationsBean  implements Operations {


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




  public OperationsBean() {
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public OperationVO getOperation() {
	  throw new UnsupportedOperationException();
  }


  /**
   * Business logic to execute.
   */
  public VOListResponse loadOperations(GridParams gridParams,String serverLanguageId,String username,ArrayList companiesList) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      // retrieve companies list...
      String companies = "";
      for(int i=0;i<companiesList.size();i++)
        companies += "'"+companiesList.get(i).toString()+"',";
      companies = companies.substring(0,companies.length()-1);

      String sql =
          "select PRO04_OPERATIONS.COMPANY_CODE_SYS01,PRO04_OPERATIONS.OPERATION_CODE,SYS10_TRANSLATIONS.DESCRIPTION,"+
          "PRO04_OPERATIONS.PROGRESSIVE_SYS10,PRO04_OPERATIONS.QTY,PRO04_OPERATIONS.TASK_CODE_REG07,"+
          "PRO04_OPERATIONS.MACHINERY_CODE_PRO03,PRO04_OPERATIONS.DURATION,PRO04_OPERATIONS.VALUE,"+
          "PRO04_OPERATIONS.NOTE,TASKS.DESCRIPTION,PRO03.DESCRIPTION "+
          "from SYS10_TRANSLATIONS,PRO04_OPERATIONS "+
          "LEFT OUTER JOIN "+
          "(select SYS10_TRANSLATIONS.DESCRIPTION,REG07_TASKS.COMPANY_CODE_SYS01,REG07_TASKS.TASK_CODE "+
          "from REG07_TASKS,SYS10_TRANSLATIONS where "+
          "REG07_TASKS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=?) TASKS ON "+
          "TASKS.COMPANY_CODE_SYS01=PRO04_OPERATIONS.COMPANY_CODE_SYS01 and "+
          "TASKS.TASK_CODE=PRO04_OPERATIONS.TASK_CODE_REG07 "+
          "LEFT OUTER JOIN "+
          "(select SYS10_TRANSLATIONS.DESCRIPTION,PRO03_MACHINERIES.COMPANY_CODE_SYS01,PRO03_MACHINERIES.MACHINERY_CODE "+
          "from PRO03_MACHINERIES,SYS10_TRANSLATIONS where "+
          "PRO03_MACHINERIES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=?) PRO03 ON "+
          "PRO03.COMPANY_CODE_SYS01=PRO04_OPERATIONS.COMPANY_CODE_SYS01 and "+
          "PRO03.MACHINERY_CODE=PRO04_OPERATIONS.MACHINERY_CODE_PRO03 "+
          "where "+
          "PRO04_OPERATIONS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "PRO04_OPERATIONS.COMPANY_CODE_SYS01 in ("+companies+") and "+
          "PRO04_OPERATIONS.ENABLED='Y'";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01PRO04","PRO04_OPERATIONS.COMPANY_CODE_SYS01");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("operationCodePRO04","PRO04_OPERATIONS.OPERATION_CODE");
      attribute2dbField.put("progressiveSys10PRO04","PRO04_OPERATIONS.PROGRESSIVE_SYS10");
      attribute2dbField.put("qtyPRO04","PRO04_OPERATIONS.QTY");
      attribute2dbField.put("taskCodeReg07PRO04","PRO04_OPERATIONS.TASK_CODE_REG07");
      attribute2dbField.put("machineryCodePro03PRO04","PRO04_OPERATIONS.MACHINERY_CODE_PRO03");
      attribute2dbField.put("durationPRO04","PRO04_OPERATIONS.DURATION");
      attribute2dbField.put("valuePRO04","PRO04_OPERATIONS.VALUE");
      attribute2dbField.put("notePRO04","PRO04_OPERATIONS.NOTE");
      attribute2dbField.put("taskDescriptionSYS10","TASKS.DESCRIPTION");
      attribute2dbField.put("machineryDescriptionSYS10","PRO03.DESCRIPTION");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(serverLanguageId);
      values.add(serverLanguageId);

      // read from PRO04 table...
      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          OperationVO.class,
          "Y",
          "N",
          null,
          gridParams,
          true
      );


      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching operations",ex);
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
  public VOListResponse updateOperations(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      OperationVO oldVO = null;
      OperationVO newVO = null;
      Response res = null;

      HashSet pkAttrs = new HashSet();
      pkAttrs.add("companyCodeSys01PRO04");
      pkAttrs.add("operationCodePRO04");

      HashMap attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01PRO04","COMPANY_CODE_SYS01");
      attribute2dbField.put("operationCodePRO04","OPERATION_CODE");
      attribute2dbField.put("progressiveSys10PRO04","PROGRESSIVE_SYS10");
      attribute2dbField.put("qtyPRO04","QTY");
      attribute2dbField.put("taskCodeReg07PRO04","TASK_CODE_REG07");
      attribute2dbField.put("machineryCodePro03PRO04","MACHINERY_CODE_PRO03");
      attribute2dbField.put("durationPRO04","DURATION");
      attribute2dbField.put("valuePRO04","VALUE");
      attribute2dbField.put("notePRO04","NOTE");


      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (OperationVO)oldVOs.get(i);
        newVO = (OperationVO)newVOs.get(i);

        TranslationUtils.updateTranslation(oldVO.getDescriptionSYS10(),newVO.getDescriptionSYS10(),newVO.getProgressiveSys10PRO04(),serverLanguageId,conn);

        res = new QueryUtil().updateTable(
            conn,
            new UserSessionParameters(username),
            pkAttrs,
            oldVO,
            newVO,
            "PRO04_OPERATIONS",
            attribute2dbField,
            "Y",
            "N",
            null,
            true
        );
        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }
      }

      return new VOListResponse(newVOs,false,newVOs.size());
    }
    catch (Throwable ex) {
    	Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing operations",ex);
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
  public VOListResponse validateOperationCode(LookupValidationParams validationPars,String serverLanguageId,String username,ArrayList companiesList) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      // retrieve companies list...
      String companies = "";
      if (validationPars.getLookupValidationParameters().get(ApplicationConsts.COMPANY_CODE_SYS01)!=null)
        companies = "'"+(String)validationPars.getLookupValidationParameters().get(ApplicationConsts.COMPANY_CODE_SYS01)+"'";
      else {
        for (int i = 0; i < companiesList.size(); i++)
          companies += "'" + companiesList.get(i).toString() + "',";
        companies = companies.substring(0, companies.length() - 1);
      }

      String sql =
          "select PRO04_OPERATIONS.COMPANY_CODE_SYS01,PRO04_OPERATIONS.OPERATION_CODE,SYS10_TRANSLATIONS.DESCRIPTION,"+
          "PRO04_OPERATIONS.PROGRESSIVE_SYS10,PRO04_OPERATIONS.QTY,PRO04_OPERATIONS.TASK_CODE_REG07,"+
          "PRO04_OPERATIONS.MACHINERY_CODE_PRO03,PRO04_OPERATIONS.DURATION,PRO04_OPERATIONS.VALUE,"+
          "PRO04_OPERATIONS.NOTE,TASKS.DESCRIPTION,PRO03.DESCRIPTION "+
          "from SYS10_TRANSLATIONS,PRO04_OPERATIONS "+
          "LEFT OUTER JOIN "+
          "(select SYS10_TRANSLATIONS.DESCRIPTION,REG07_TASKS.COMPANY_CODE_SYS01,REG07_TASKS.TASK_CODE "+
          "from REG07_TASKS,SYS10_TRANSLATIONS where "+
          "REG07_TASKS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=?) TASKS ON "+
          "TASKS.COMPANY_CODE_SYS01=PRO04_OPERATIONS.COMPANY_CODE_SYS01 and "+
          "TASKS.TASK_CODE=PRO04_OPERATIONS.TASK_CODE_REG07 "+
          "LEFT OUTER JOIN "+
          "(select SYS10_TRANSLATIONS.DESCRIPTION,PRO03_MACHINERIES.COMPANY_CODE_SYS01,PRO03_MACHINERIES.MACHINERY_CODE "+
          "from PRO03_MACHINERIES,SYS10_TRANSLATIONS where "+
          "PRO03_MACHINERIES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=?) PRO03 ON "+
          "PRO03.COMPANY_CODE_SYS01=PRO04_OPERATIONS.COMPANY_CODE_SYS01 and "+
          "PRO03.MACHINERY_CODE=PRO04_OPERATIONS.MACHINERY_CODE_PRO03 "+
          "where "+
          "PRO04_OPERATIONS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "PRO04_OPERATIONS.COMPANY_CODE_SYS01 in ("+companies+") and "+
          "PRO04_OPERATIONS.ENABLED='Y' and "+
          "PRO04_OPERATIONS.OPERATION_CODE='"+validationPars.getCode()+"'";


      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01PRO04","PRO04_OPERATIONS.COMPANY_CODE_SYS01");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("operationCodePRO04","PRO04_OPERATIONS.OPERATION_CODE");
      attribute2dbField.put("progressiveSys10PRO04","PRO04_OPERATIONS.PROGRESSIVE_SYS10");
      attribute2dbField.put("qtyPRO04","PRO04_OPERATIONS.QTY");
      attribute2dbField.put("taskCodeReg07PRO04","PRO04_OPERATIONS.TASK_CODE_REG07");
      attribute2dbField.put("machineryCodePro03PRO04","PRO04_OPERATIONS.MACHINERY_CODE_PRO03");
      attribute2dbField.put("durationPRO04","PRO04_OPERATIONS.DURATION");
      attribute2dbField.put("valuePRO04","PRO04_OPERATIONS.VALUE");
      attribute2dbField.put("notePRO04","PRO04_OPERATIONS.NOTE");
      attribute2dbField.put("taskDescriptionSYS10","TASKS.DESCRIPTION");
      attribute2dbField.put("machineryDescriptionSYS10","PRO03.DESCRIPTION");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(serverLanguageId);
      values.add(serverLanguageId);

      // read from PRO04 table...
      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          OperationVO.class,
          "Y",
          "N",
          null,
          new GridParams(),
          true
      );

      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while validating operation code",ex);
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
  public VOListResponse insertOperations(ArrayList vos,String serverLanguageId,String username,ArrayList companiesList) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      OperationVO vo = null;
      Response res = null;

      String companyCode = companiesList.get(0).toString();

      for(int i=0;i<vos.size();i++) {
        vo = (OperationVO)vos.get(i);
        if (vo.getCompanyCodeSys01PRO04()==null)
          vo.setCompanyCodeSys01PRO04(companyCode);
        vo.setEnabledPRO04("Y");
        vo.setProgressiveSys10PRO04(TranslationUtils.insertTranslations(vo.getDescriptionSYS10(),vo.getCompanyCodeSys01PRO04(),conn));

        Map attribute2dbField = new HashMap();
        attribute2dbField.put("companyCodeSys01PRO04","COMPANY_CODE_SYS01");
        attribute2dbField.put("operationCodePRO04","OPERATION_CODE");
        attribute2dbField.put("progressiveSys10PRO04","PROGRESSIVE_SYS10");
        attribute2dbField.put("qtyPRO04","QTY");
        attribute2dbField.put("taskCodeReg07PRO04","TASK_CODE_REG07");
        attribute2dbField.put("machineryCodePro03PRO04","MACHINERY_CODE_PRO03");
        attribute2dbField.put("durationPRO04","DURATION");
        attribute2dbField.put("valuePRO04","VALUE");
        attribute2dbField.put("notePRO04","NOTE");
        attribute2dbField.put("enabledPRO04","ENABLED");

        // insert into PRO04...
        res = QueryUtil.insertTable(
            conn,
            new UserSessionParameters(username),
            vo,
            "PRO04_OPERATIONS",
            attribute2dbField,
            "Y",
            "N",
            null,
            true
        );
        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }

      }


      return new VOListResponse(vos,false,vos.size());
    }
    catch (Throwable ex) {
    	Logger.error(username, this.getClass().getName(),
    			"executeCommand", "Error while inserting new operations", ex);
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
  public VOResponse deleteOperations(ArrayList list,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      stmt = conn.createStatement();

      OperationVO vo = null;
      for(int i=0;i<list.size();i++) {
        // logically delete the record in PRO04...
        vo = (OperationVO)list.get(i);
        stmt.execute(
          "update PRO04_OPERATIONS set ENABLED='N' where "+
          "COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01PRO04()+"' and "+
          "OPERATION_CODE='"+vo.getOperationCodePRO04()+"'");
      }

      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
    	Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing operations",ex);
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

