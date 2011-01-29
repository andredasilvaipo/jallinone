package org.jallinone.registers.task.server;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.sql.DataSource;

import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.registers.task.java.TaskVO;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.system.translations.server.TranslationUtils;
import org.openswing.swing.logger.server.Logger;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.message.receive.java.VOResponse;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.message.send.java.LookupValidationParams;
import org.openswing.swing.server.UserSessionParameters;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage tasks.</p>
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
public class TasksBean  implements Tasks {


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




  public TasksBean() {
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public TaskVO getTask() {
	  throw new UnsupportedOperationException();	  
  }


  /**
   * Business logic to execute.
   */
  public VOListResponse validateTaskCode(LookupValidationParams validationPars,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      String sql =
          "select REG07_TASKS.COMPANY_CODE_SYS01,REG07_TASKS.TASK_CODE,REG07_TASKS.PROGRESSIVE_SYS10,SYS10_TRANSLATIONS.DESCRIPTION,"+
          "REG07_TASKS.ACTIVITY_CODE_SAL09,REG07_TASKS.ENABLED,REG07_TASKS.FINITE_CAPACITY, "+
          "SAL09_ACTIVITIES.DESCRIPTION from SYS10_TRANSLATIONS,REG07_TASKS "+
          "LEFT OUTER JOIN "+
          "(select SYS10_TRANSLATIONS.DESCRIPTION,SAL09_ACTIVITIES.COMPANY_CODE_SYS01,SAL09_ACTIVITIES.ACTIVITY_CODE from SAL09_ACTIVITIES,SYS10_TRANSLATIONS where "+
          "SAL09_ACTIVITIES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=?) SAL09_ACTIVITIES ON "+
          "SAL09_ACTIVITIES.COMPANY_CODE_SYS01=REG07_TASKS.COMPANY_CODE_SYS01 and "+
          "SAL09_ACTIVITIES.ACTIVITY_CODE=REG07_TASKS.ACTIVITY_CODE_SAL09 "+
          "where "+
          "REG07_TASKS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "REG07_TASKS.ENABLED='Y' and "+
          "REG07_TASKS.TASK_CODE='"+validationPars.getCode()+"' and "+
          "REG07_TASKS.COMPANY_CODE_SYS01=?";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01REG07","REG07_TASKS.COMPANY_CODE_SYS01");
      attribute2dbField.put("taskCodeREG07","REG07_TASKS.TASK_CODE");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("progressiveSys10REG07","REG07_TASKS.PROGRESSIVE_SYS10");
      attribute2dbField.put("enabledREG07","REG07_TASKS.ENABLED");
      attribute2dbField.put("activityCodeSal09REG07","REG07_TASKS.ACTIVITY_CODE_SAL09");
      attribute2dbField.put("activityDescriptionREG07","SAL09_ACTIVITIES.DESCRIPTION");
      attribute2dbField.put("finiteCapacityREG07","REG07_TASKS.FINITE_CAPACITY");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(serverLanguageId);
      values.add(validationPars.getLookupValidationParameters().get(ApplicationConsts.COMPANY_CODE_SYS01));

      GridParams gridParams = new GridParams();

      // read from REG07 table...
      Response answer = CustomizeQueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          TaskVO.class,
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
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while validating task code",ex);
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
  public VOListResponse loadTasks(GridParams gridParams,String serverLanguageId,String username,ArrayList companiesList,ArrayList customizedFields) throws Throwable {
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
          "select REG07_TASKS.COMPANY_CODE_SYS01,REG07_TASKS.TASK_CODE,REG07_TASKS.PROGRESSIVE_SYS10,SYS10_TRANSLATIONS.DESCRIPTION,"+
          "REG07_TASKS.ACTIVITY_CODE_SAL09,REG07_TASKS.ENABLED,REG07_TASKS.FINITE_CAPACITY, "+
          "SAL09_ACTIVITIES.DESCRIPTION from SYS10_TRANSLATIONS,REG07_TASKS "+
          "LEFT OUTER JOIN "+
          "(select SYS10_TRANSLATIONS.DESCRIPTION,SAL09_ACTIVITIES.COMPANY_CODE_SYS01,SAL09_ACTIVITIES.ACTIVITY_CODE from SAL09_ACTIVITIES,SYS10_TRANSLATIONS where "+
          "SAL09_ACTIVITIES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=?) SAL09_ACTIVITIES ON "+
          "SAL09_ACTIVITIES.COMPANY_CODE_SYS01=REG07_TASKS.COMPANY_CODE_SYS01 and "+
          "SAL09_ACTIVITIES.ACTIVITY_CODE=REG07_TASKS.ACTIVITY_CODE_SAL09 "+
          "where "+
          "REG07_TASKS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "REG07_TASKS.ENABLED='Y' and "+
          "REG07_TASKS.COMPANY_CODE_SYS01 in ("+companies+")";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01REG07","REG07_TASKS.COMPANY_CODE_SYS01");
      attribute2dbField.put("taskCodeREG07","REG07_TASKS.TASK_CODE");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("progressiveSys10REG07","REG07_TASKS.PROGRESSIVE_SYS10");
      attribute2dbField.put("enabledREG07","REG07_TASKS.ENABLED");
      attribute2dbField.put("activityCodeSal09REG07","REG07_TASKS.ACTIVITY_CODE_SAL09");
      attribute2dbField.put("activityDescriptionREG07","SAL09_ACTIVITIES.DESCRIPTION");
      attribute2dbField.put("durationREG07","REG07_TASKS.DURATION");
      attribute2dbField.put("finiteCapacityREG07","REG07_TASKS.FINITE_CAPACITY");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(serverLanguageId);

      // read from REG07 table...
      Response answer = CustomizeQueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          TaskVO.class,
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
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching tasks list",ex);
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
  public VOListResponse insertTasks(ArrayList list,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      TaskVO vo = null;

      BigDecimal progressiveSYS10 = null;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01REG07","COMPANY_CODE_SYS01");
      attribute2dbField.put("taskCodeREG07","TASK_CODE");
      attribute2dbField.put("progressiveSys10REG07","PROGRESSIVE_SYS10");
      attribute2dbField.put("enabledREG07","ENABLED");
      attribute2dbField.put("finiteCapacityREG07","FINITE_CAPACITY");
      attribute2dbField.put("activityCodeSal09REG07","ACTIVITY_CODE_SAL09");

      Response res = null;
      for(int i=0;i<list.size();i++) {
        vo = (TaskVO)list.get(i);
        vo.setEnabledREG07("Y");

        // insert record in SYS10...
        progressiveSYS10 = TranslationUtils.insertTranslations(vo.getDescriptionSYS10(),vo.getCompanyCodeSys01REG07(),conn);
        vo.setProgressiveSys10REG07(progressiveSYS10);

        // insert into REG07...
        res = CustomizeQueryUtil.insertTable(
            conn,
            new UserSessionParameters(username),
            vo,
            "REG07_TASKS",
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
                   "executeCommand", "Error while inserting new tasks", ex);
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
  public VOListResponse updateTasks(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      TaskVO oldVO = null;
      TaskVO newVO = null;
      Response res = null;

      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (TaskVO)oldVOs.get(i);
        newVO = (TaskVO)newVOs.get(i);

        // update SYS10 table...
        TranslationUtils.updateTranslation(oldVO.getDescriptionSYS10(),newVO.getDescriptionSYS10(),newVO.getProgressiveSys10REG07(),serverLanguageId,conn);

        HashSet pkAttrs = new HashSet();
        pkAttrs.add("companyCodeSys01REG07");
        pkAttrs.add("taskCodeREG07");

        HashMap attr2dbFields = new HashMap();
        attr2dbFields.put("companyCodeSys01REG07","COMPANY_CODE_SYS01");
        attr2dbFields.put("taskCodeREG07","TASK_CODE");
        attr2dbFields.put("progressiveSys10REG07","PROGRESSIVE_SYS10");
        attr2dbFields.put("enabledREG07","ENABLED");
        attr2dbFields.put("finiteCapacityREG07","FINITE_CAPACITY");
        attr2dbFields.put("activityCodeSal09REG07","ACTIVITY_CODE_SAL09");

        res = new CustomizeQueryUtil().updateTable(
            conn,
            new UserSessionParameters(username),
            pkAttrs,
            oldVO,
            newVO,
            "REG07_TASKS",
            attr2dbFields,
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
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing tasks",ex);
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
  public VOResponse deleteTasks(ArrayList list,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      stmt = conn.createStatement();

      TaskVO vo = null;
      for(int i=0;i<list.size();i++) {
        // logically delete the record in REG07...
        vo = (TaskVO)list.get(i);
        stmt.execute(
            "update REG07_TASKS set ENABLED='N' where "+
            "TASK_CODE='"+vo.getTaskCodeREG07()+"' and "+
            "COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01REG07()+"'"
        );
      }

      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing tasks",ex);
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

