package org.jallinone.scheduler.activities.server;

import org.openswing.swing.server.*;

import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;
import org.jallinone.scheduler.activities.java.*;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage scheduled employees.</p>
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
public class ScheduledEmployeesBean  implements ScheduledEmployees {


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




  public ScheduledEmployeesBean() {
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public ScheduledEmployeeVO getScheduledEmployee(ScheduledActivityPK pk) {
	  throw new UnsupportedOperationException();
  }


  /**
   * Business logic to execute.
   */
  public VOListResponse loadScheduledEmployees(GridParams gridParams,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      String sql =
          "select SCH07_SCHEDULED_EMPLOYEES.COMPANY_CODE_SYS01,SCH07_SCHEDULED_EMPLOYEES.PROGRESSIVE_REG04,SCH07_SCHEDULED_EMPLOYEES.PROGRESSIVE_SCH06,"+
          "SCH07_SCHEDULED_EMPLOYEES.START_DATE,SCH07_SCHEDULED_EMPLOYEES.END_DATE,SCH07_SCHEDULED_EMPLOYEES.DURATION,"+
          "SCH07_SCHEDULED_EMPLOYEES.NOTE,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,SCH01_EMPLOYEES.EMPLOYEE_CODE,SCH01_EMPLOYEES.TASK_CODE_REG07,SYS10_TRANSLATIONS.DESCRIPTION "+
          "from SCH07_SCHEDULED_EMPLOYEES,REG04_SUBJECTS,SCH01_EMPLOYEES,REG07_TASKS,SYS10_TRANSLATIONS where "+
          "SCH07_SCHEDULED_EMPLOYEES.COMPANY_CODE_SYS01=SCH01_EMPLOYEES.COMPANY_CODE_SYS01 and "+
          "SCH07_SCHEDULED_EMPLOYEES.PROGRESSIVE_REG04=SCH01_EMPLOYEES.PROGRESSIVE_REG04 and "+
          "SCH07_SCHEDULED_EMPLOYEES.COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 and "+
          "SCH07_SCHEDULED_EMPLOYEES.PROGRESSIVE_REG04=REG04_SUBJECTS.PROGRESSIVE and "+
          "SCH01_EMPLOYEES.COMPANY_CODE_SYS01=REG07_TASKS.COMPANY_CODE_SYS01 and "+
          "SCH01_EMPLOYEES.TASK_CODE_REG07=REG07_TASKS.TASK_CODE and "+
          "REG07_TASKS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "SCH07_SCHEDULED_EMPLOYEES.COMPANY_CODE_SYS01=? and "+
          "SCH07_SCHEDULED_EMPLOYEES.PROGRESSIVE_SCH06=?";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SCH07","SCH07_SCHEDULED_EMPLOYEES.COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveReg04SCH07","SCH07_SCHEDULED_EMPLOYEES.PROGRESSIVE_REG04");
      attribute2dbField.put("progressiveSch06SCH07","SCH07_SCHEDULED_EMPLOYEES.PROGRESSIVE_SCH06");
      attribute2dbField.put("startDateSCH07","SCH07_SCHEDULED_EMPLOYEES.START_DATE");
      attribute2dbField.put("endDateSCH07","SCH07_SCHEDULED_EMPLOYEES.END_DATE");
      attribute2dbField.put("durationSCH07","SCH07_SCHEDULED_EMPLOYEES.DURATION");
      attribute2dbField.put("noteSCH07","SCH07_SCHEDULED_EMPLOYEES.NOTE");
      attribute2dbField.put("name_1REG04","REG04_SUBJECTS.NAME_1");
      attribute2dbField.put("name_2REG04","REG04_SUBJECTS.NAME_2");
      attribute2dbField.put("employeeCodeSCH01","SCH01_EMPLOYEES.EMPLOYEE_CODE");
      attribute2dbField.put("taskCodeREG07","SCH01_EMPLOYEES.TASK_CODE_REG07");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");

      ScheduledActivityPK pk = (ScheduledActivityPK)gridParams.getOtherGridParams().get(ApplicationConsts.SCHEDULED_ACTIVITY_PK);

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(pk.getCompanyCodeSys01SCH06());
      values.add(pk.getProgressiveSCH06());

      // read from SCH07 table...
      Response res = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          ScheduledEmployeeVO.class,
          "Y",
          "N",
          null,
          gridParams,
          true
      );

      if (!res.isError()) {
        HashSet taskCodes = new HashSet();
        ScheduledEmployeeVO vo = null;
        java.util.List rows = ((VOListResponse)res).getRows();
        for(int i=0;i<rows.size();i++) {
          vo = (ScheduledEmployeeVO)rows.get(i);
          taskCodes.add(vo.getTaskCodeREG07());
        }

        // retrieve tasks defined in the call-out...
        sql =
            "select SCH12_CALL_OUT_TASKS.TASK_CODE_REG07,SYS10_TRANSLATIONS.DESCRIPTION "+
            "from SCH12_CALL_OUT_TASKS,SCH03_CALL_OUT_REQUESTS,REG07_TASKS,SYS10_TRANSLATIONS where "+
            "SCH03_CALL_OUT_REQUESTS.COMPANY_CODE_SYS01=SCH12_CALL_OUT_TASKS.COMPANY_CODE_SYS01 and "+
            "SCH03_CALL_OUT_REQUESTS.CALL_OUT_CODE_SCH10=SCH12_CALL_OUT_TASKS.CALL_OUT_CODE_SCH10 and "+
            "SCH12_CALL_OUT_TASKS.COMPANY_CODE_SYS01=REG07_TASKS.COMPANY_CODE_SYS01 and "+
            "SCH12_CALL_OUT_TASKS.TASK_CODE_REG07=REG07_TASKS.TASK_CODE and "+
            "REG07_TASKS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
            "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
            "SCH03_CALL_OUT_REQUESTS.COMPANY_CODE_SYS01=? and "+
            "SCH03_CALL_OUT_REQUESTS.PROGRESSIVE_SCH06=?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1,serverLanguageId);
        pstmt.setString(2,pk.getCompanyCodeSys01SCH06());
        pstmt.setBigDecimal(3,pk.getProgressiveSCH06());
        ResultSet rset = pstmt.executeQuery();
        while(rset.next()) {
          vo = new ScheduledEmployeeVO();
          vo.setCompanyCodeSys01SCH07(pk.getCompanyCodeSys01SCH06());
          vo.setProgressiveSch06SCH07(pk.getProgressiveSCH06());
          vo.setTaskCodeREG07(rset.getString(1));
          vo.setDescriptionSYS10(rset.getString(2));
          if (!taskCodes.contains(vo.getTaskCodeREG07()))
            rows.add(vo);
        }
        rset.close();
      }

      Response answer = res;

      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching scheduled employees list",ex);
      throw new Exception(ex.getMessage());
    }
    finally {
        try {
            pstmt.close();
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
  public VOListResponse updateScheduledEmployees(ArrayList oldVos,ArrayList newVos,String serverLanguageId,String username) throws Throwable {
    
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      ScheduledEmployeeVO oldVO = null;
      ScheduledEmployeeVO newVO = null;

      HashSet pk = new HashSet();
      pk.add("companyCodeSys01SCH07");
      pk.add("progressiveSch06SCH07");
      pk.add("progressiveReg04SCH07");

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SCH07","COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveSch06SCH07","PROGRESSIVE_SCH06");
      attribute2dbField.put("progressiveReg04SCH07","PROGRESSIVE_REG04");
      attribute2dbField.put("startDateSCH07","START_DATE");
      attribute2dbField.put("endDateSCH07","END_DATE");
      attribute2dbField.put("durationSCH07","DURATION");
      attribute2dbField.put("noteSCH07","NOTE");

      Response res = null;
      for(int i=0;i<oldVos.size();i++) {
        oldVO = (ScheduledEmployeeVO)oldVos.get(i);
        newVO = (ScheduledEmployeeVO)newVos.get(i);

        // update record in SCH07...
        res = QueryUtil.updateTable(
            conn,
            new UserSessionParameters(username),
            pk,
            oldVO,
            newVO,
            "SCH07_SCHEDULED_EMPLOYEES",
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
      return new VOListResponse(newVos,false,newVos.size());
    }
    catch (Throwable ex) {
      Logger.error(username, this.getClass().getName(),
                   "executeCommand", "Error while updating scheduled employees", ex);
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
  public VOListResponse insertScheduledEmployees(ArrayList list,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      ScheduledEmployeeVO vo = null;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SCH07","COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveSch06SCH07","PROGRESSIVE_SCH06");
      attribute2dbField.put("progressiveReg04SCH07","PROGRESSIVE_REG04");
      attribute2dbField.put("startDateSCH07","START_DATE");
      attribute2dbField.put("endDateSCH07","END_DATE");
      attribute2dbField.put("durationSCH07","DURATION");
      attribute2dbField.put("noteSCH07","NOTE");

      Response res = null;
      for(int i=0;i<list.size();i++) {
        vo = (ScheduledEmployeeVO)list.get(i);

        // insert into SCH07...
        res = QueryUtil.insertTable(
            conn,
            new UserSessionParameters(username),
            vo,
            "SCH07_SCHEDULED_EMPLOYEES",
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

      return new VOListResponse(list,false,list.size());
    }
    catch (Throwable ex) {
      Logger.error(username, this.getClass().getName(),
                   "executeCommand", "Error while inserting new scheduled employees", ex);
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
  public VOResponse deleteScheduledEmployees(ArrayList list,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      pstmt = conn.prepareStatement(
        "delete from SCH07_SCHEDULED_EMPLOYEES where COMPANY_CODE_SYS01=? and PROGRESSIVE_SCH06=? and PROGRESSIVE_REG04=? and START_DATE=?"
      );

      ScheduledEmployeeVO vo = null;
      for(int i=0;i<list.size();i++) {
        // phisically delete the record in SCH07...
        vo = (ScheduledEmployeeVO)list.get(i);
        pstmt.setString(1,vo.getCompanyCodeSys01SCH07());
        pstmt.setBigDecimal(2,vo.getProgressiveSch06SCH07());
        pstmt.setBigDecimal(3,vo.getProgressiveReg04SCH07());
        pstmt.setTimestamp(4,vo.getStartDateSCH07());
        pstmt.execute();
      }

      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing scheduled employees",ex);
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
            pstmt.close();
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

