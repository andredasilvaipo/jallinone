package org.jallinone.scheduler.activities.server;

import org.openswing.swing.server.*;

import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.system.progressives.server.CompanyProgressiveUtils;
import org.jallinone.system.server.*;
import java.math.*;

import org.jallinone.scheduler.activities.java.*;
import org.jallinone.commons.java.*;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Manages scheduled activities.</p>
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
public class ScheduledActivitiesBean implements ScheduledActivities {


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




  public ScheduledActivitiesBean() {
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public GridScheduledActivityVO getGridScheduledActivity() {
	  throw new UnsupportedOperationException();	  
  }
  

  /**
   * Business logic to execute.
   */
  public VOListResponse loadEmployeeActivities(GridParams gridPars,String serverLanguageId,String username,ArrayList companiesList) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      // retrieve companies list...
      String companies = "";
      if (gridPars.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01)!=null) {
        companies = "'"+gridPars.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01)+"'";
      }
      else {
        for(int i=0;i<companiesList.size();i++)
          companies += "'"+companiesList.get(i).toString()+"',";
        companies = companies.substring(0,companies.length()-1);
      }

      String sql =
          "select SCH06_SCHEDULED_ACTIVITIES.COMPANY_CODE_SYS01,SCH06_SCHEDULED_ACTIVITIES.DESCRIPTION,SCH06_SCHEDULED_ACTIVITIES.ACTIVITY_TYPE,"+
          "SCH06_SCHEDULED_ACTIVITIES.PRIORITY,SCH06_SCHEDULED_ACTIVITIES.ACTIVITY_STATE,SCH06_SCHEDULED_ACTIVITIES.ACTIVITY_PLACE,"+
          "SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE,SCH06_SCHEDULED_ACTIVITIES.ESTIMATED_DURATION,SCH06_SCHEDULED_ACTIVITIES.DURATION,"+
          "SCH06_SCHEDULED_ACTIVITIES.COMPLETION_PERC,SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE_REG04_MANAGER,SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE_REG04_SUBJECT,"+
          "SCH06_SCHEDULED_ACTIVITIES.START_DATE,SCH06_SCHEDULED_ACTIVITIES.ESTIMATED_END_DATE,SCH06_SCHEDULED_ACTIVITIES.END_DATE,"+
          "SCH06_SCHEDULED_ACTIVITIES.EXPIRATION_DATE, "+
          "REG04_MANAGER.NAME_1,REG04_MANAGER.NAME_2,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,REG04_SUBJECTS.SUBJECT_TYPE, "+
          "SCH07_SCHEDULED_EMPLOYEES.START_DATE,SCH07_SCHEDULED_EMPLOYEES.END_DATE,SCH07_SCHEDULED_EMPLOYEES.DURATION,"+
          "SCH01_EMPLOYEES.EMPLOYEE_CODE,REG04_EMP.NAME_1,REG04_EMP.NAME_2 "+
          "from SCH07_SCHEDULED_EMPLOYEES,REG04_SUBJECTS REG04_EMP,SCH01_EMPLOYEES,SCH06_SCHEDULED_ACTIVITIES "+
          "LEFT OUTER JOIN "+
          "(select REG04_SUBJECTS.COMPANY_CODE_SYS01,REG04_SUBJECTS.PROGRESSIVE,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2 "+
          "from REG04_SUBJECTS) REG04_MANAGER ON "+
          "REG04_MANAGER.COMPANY_CODE_SYS01=SCH06_SCHEDULED_ACTIVITIES.COMPANY_CODE_SYS01 and "+
          "REG04_MANAGER.PROGRESSIVE=SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE_REG04_MANAGER "+
          "LEFT OUTER JOIN "+
          "(select REG04_SUBJECTS.COMPANY_CODE_SYS01,REG04_SUBJECTS.PROGRESSIVE,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,REG04_SUBJECTS.SUBJECT_TYPE "+
          "from REG04_SUBJECTS) REG04_SUBJECTS ON "+
          "REG04_SUBJECTS.COMPANY_CODE_SYS01=SCH06_SCHEDULED_ACTIVITIES.COMPANY_CODE_SYS01 and "+
          "REG04_SUBJECTS.PROGRESSIVE=SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE_REG04_SUBJECT "+
          "where SCH06_SCHEDULED_ACTIVITIES.COMPANY_CODE_SYS01 in ("+companies+") and "+
          "SCH07_SCHEDULED_EMPLOYEES.COMPANY_CODE_SYS01=SCH06_SCHEDULED_ACTIVITIES.COMPANY_CODE_SYS01 and "+
          "SCH07_SCHEDULED_EMPLOYEES.PROGRESSIVE_SCH06=SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE and "+
          "SCH07_SCHEDULED_EMPLOYEES.COMPANY_CODE_SYS01=SCH01_EMPLOYEES.COMPANY_CODE_SYS01 and "+
          "SCH07_SCHEDULED_EMPLOYEES.PROGRESSIVE_REG04=SCH01_EMPLOYEES.PROGRESSIVE_REG04 and "+
          "REG04_EMP.COMPANY_CODE_SYS01=SCH01_EMPLOYEES.COMPANY_CODE_SYS01 and "+
          "REG04_EMP.PROGRESSIVE=SCH01_EMPLOYEES.PROGRESSIVE_REG04 ";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SCH06","SCH06_SCHEDULED_ACTIVITIES.COMPANY_CODE_SYS01");
      attribute2dbField.put("descriptionSCH06","SCH06_SCHEDULED_ACTIVITIES.DESCRIPTION");
      attribute2dbField.put("activityTypeSCH06","SCH06_SCHEDULED_ACTIVITIES.ACTIVITY_TYPE");
      attribute2dbField.put("prioritySCH06","SCH06_SCHEDULED_ACTIVITIES.PRIORITY");
      attribute2dbField.put("activityStateSCH06","SCH06_SCHEDULED_ACTIVITIES.ACTIVITY_STATE");
      attribute2dbField.put("activityPlaceSCH06","SCH06_SCHEDULED_ACTIVITIES.ACTIVITY_PLACE");
      attribute2dbField.put("progressiveSCH06","SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE");
      attribute2dbField.put("estimatedDurationSCH06","SCH06_SCHEDULED_ACTIVITIES.ESTIMATED_DURATION");
      attribute2dbField.put("durationSCH06","SCH06_SCHEDULED_ACTIVITIES.DURATION");
      attribute2dbField.put("completionPercSCH06","SCH06_SCHEDULED_ACTIVITIES.COMPLETION_PERC");
      attribute2dbField.put("progressiveReg04ManagerSCH06","SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE_REG04_MANAGER");
      attribute2dbField.put("progressiveReg04SubjectSCH06","SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE_REG04_SUBJECT");
      attribute2dbField.put("startDateSCH06","SCH06_SCHEDULED_ACTIVITIES.START_DATE");
      attribute2dbField.put("estimatedEndDateSCH06","SCH06_SCHEDULED_ACTIVITIES.ESTIMATED_END_DATE");
      attribute2dbField.put("endDateSCH06","SCH06_SCHEDULED_ACTIVITIES.END_DATE");
      attribute2dbField.put("expirationDateSCH06","SCH06_SCHEDULED_ACTIVITIES.EXPIRATION_DATE");
      attribute2dbField.put("managerName_1SCH06","REG04_MANAGER.NAME_1");
      attribute2dbField.put("managerName_2SCH06","REG04_MANAGER.NAME_2");
      attribute2dbField.put("subjectName_1SCH06","REG04_SUBJECTS.NAME_1");
      attribute2dbField.put("subjectName_2SCH06","REG04_SUBJECTS.NAME_2");
      attribute2dbField.put("subjectTypeReg04SubjectSCH06","REG04_SUBJECTS.SUBJECT_TYPE");
      attribute2dbField.put("startDateSCH07","SCH07_SCHEDULED_EMPLOYEES.START_DATE");
      attribute2dbField.put("endDateSCH07","SCH07_SCHEDULED_EMPLOYEES.END_DATE");
      attribute2dbField.put("durationSCH07","SCH07_SCHEDULED_EMPLOYEES.DURATION");
      attribute2dbField.put("employeeCodeSCH01","SCH01_EMPLOYEES.EMPLOYEE_CODE");
      attribute2dbField.put("name_1REG04","REG04_EMP.NAME_1");
      attribute2dbField.put("name_2REG04","REG04_EMP.NAME_2");

      ArrayList values = new ArrayList();
      if (gridPars.getOtherGridParams().get(ApplicationConsts.START_DATE)!=null) {
        sql += " and SCH06_SCHEDULED_ACTIVITIES.START_DATE>=?";
        values.add(gridPars.getOtherGridParams().get(ApplicationConsts.START_DATE));
      }
      if (gridPars.getOtherGridParams().get(ApplicationConsts.END_DATE)!=null) {
        sql += " and SCH06_SCHEDULED_ACTIVITIES.END_DATE<=?";
        values.add(gridPars.getOtherGridParams().get(ApplicationConsts.END_DATE));
      }
      if (gridPars.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04_MANAGER)!=null) {
        sql += " and SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE_REG04_MANAGER=?";
        values.add(gridPars.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04_MANAGER));
      }
      if (gridPars.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04_SUBJECT)!=null) {
        sql += " and SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE_REG04_SUBJECT=?";
        values.add(gridPars.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04_SUBJECT));
      }
      if (gridPars.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04)!=null) {
        sql += " and SCH07_SCHEDULED_EMPLOYEES.PROGRESSIVE_REG04=?";
        values.add(gridPars.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04));
      }
      if (gridPars.getOtherGridParams().get(ApplicationConsts.ACTIVITY_TYPE)!=null) {
        sql += " and SCH06_SCHEDULED_ACTIVITIES.ACTIVITY_TYPE in ("+gridPars.getOtherGridParams().get(ApplicationConsts.ACTIVITY_TYPE)+")";
      }
      if (gridPars.getOtherGridParams().get(ApplicationConsts.DATE_FILTER)!=null) {
        java.util.Date date = (java.util.Date)gridPars.getOtherGridParams().get(ApplicationConsts.DATE_FILTER);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(cal.HOUR_OF_DAY,0);
        cal.set(cal.MINUTE,0);
        cal.set(cal.SECOND,0);
        cal.set(cal.MILLISECOND,0);
        java.sql.Timestamp startDate = new java.sql.Timestamp(cal.getTimeInMillis());
        cal.set(cal.HOUR_OF_DAY,23);
        cal.set(cal.MINUTE,23);
        cal.set(cal.SECOND,59);
        cal.set(cal.MILLISECOND,999);
        java.sql.Timestamp endDate = new java.sql.Timestamp(cal.getTimeInMillis());
        sql += " and (SCH07_SCHEDULED_EMPLOYEES.START_DATE>=? and SCH07_SCHEDULED_EMPLOYEES.START_DATE<=? or "+
               "     SCH07_SCHEDULED_EMPLOYEES.END_DATE>=? and SCH07_SCHEDULED_EMPLOYEES.END_DATE<=? or "+
               "     SCH07_SCHEDULED_EMPLOYEES.START_DATE<=? and SCH07_SCHEDULED_EMPLOYEES.END_DATE>=? )";
        values.add(startDate);
        values.add(endDate);
        values.add(startDate);
        values.add(endDate);
        values.add(startDate);
        values.add(endDate);
      }

      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          EmployeeActivityVO.class,
          "Y",
          "N",
          null,
          gridPars,
          true
      );
      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching scheduled activities assigned to a specific employee",ex);
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
  public VOResponse loadScheduledActivity(ScheduledActivityPK pk,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      String sql =
          "select SCH06_SCHEDULED_ACTIVITIES.COMPANY_CODE_SYS01,SCH06_SCHEDULED_ACTIVITIES.DESCRIPTION,SCH06_SCHEDULED_ACTIVITIES.ACTIVITY_TYPE,"+
          "SCH06_SCHEDULED_ACTIVITIES.PRIORITY,SCH06_SCHEDULED_ACTIVITIES.ACTIVITY_STATE,SCH06_SCHEDULED_ACTIVITIES.ACTIVITY_PLACE,"+
          "SCH06_SCHEDULED_ACTIVITIES.DESCRIPTION_WKF01,"+
          "SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE,SCH06_SCHEDULED_ACTIVITIES.ESTIMATED_DURATION,SCH06_SCHEDULED_ACTIVITIES.DURATION,"+
          "SCH06_SCHEDULED_ACTIVITIES.COMPLETION_PERC,SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE_REG04_MANAGER,SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE_REG04_SUBJECT,"+
          "SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE_WKF01,SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE_WKF08,SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE_WKF02,"+
          "SCH06_SCHEDULED_ACTIVITIES.NOTE,SCH06_SCHEDULED_ACTIVITIES.START_DATE,SCH06_SCHEDULED_ACTIVITIES.ESTIMATED_END_DATE,SCH06_SCHEDULED_ACTIVITIES.END_DATE,"+
          "SCH06_SCHEDULED_ACTIVITIES.EXPIRATION_DATE,"+
          "REG04_MANAGER.NAME_1,REG04_MANAGER.NAME_2,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,REG04_SUBJECTS.SUBJECT_TYPE, "+
          "SCH06_SCHEDULED_ACTIVITIES.EMAIL_ADDRESS,SCH06_SCHEDULED_ACTIVITIES.FAX_NUMBER,SCH06_SCHEDULED_ACTIVITIES.PHONE_NUMBER "+
          "from SCH06_SCHEDULED_ACTIVITIES "+
          "LEFT OUTER JOIN "+
          "(select REG04_SUBJECTS.COMPANY_CODE_SYS01,REG04_SUBJECTS.PROGRESSIVE,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2 "+
          "from REG04_SUBJECTS) REG04_MANAGER ON "+
          "REG04_MANAGER.COMPANY_CODE_SYS01=SCH06_SCHEDULED_ACTIVITIES.COMPANY_CODE_SYS01 and "+
          "REG04_MANAGER.PROGRESSIVE=SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE_REG04_MANAGER "+
          "LEFT OUTER JOIN "+
          "(select REG04_SUBJECTS.COMPANY_CODE_SYS01,REG04_SUBJECTS.PROGRESSIVE,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,REG04_SUBJECTS.SUBJECT_TYPE "+
          "from REG04_SUBJECTS) REG04_SUBJECTS ON "+
          "REG04_SUBJECTS.COMPANY_CODE_SYS01=SCH06_SCHEDULED_ACTIVITIES.COMPANY_CODE_SYS01 and "+
          "REG04_SUBJECTS.PROGRESSIVE=SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE_REG04_SUBJECT "+
          "where SCH06_SCHEDULED_ACTIVITIES.COMPANY_CODE_SYS01=? and SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE=?";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SCH06","SCH06_SCHEDULED_ACTIVITIES.COMPANY_CODE_SYS01");
      attribute2dbField.put("descriptionSCH06","SCH06_SCHEDULED_ACTIVITIES.DESCRIPTION");
      attribute2dbField.put("activityTypeSCH06","SCH06_SCHEDULED_ACTIVITIES.ACTIVITY_TYPE");
      attribute2dbField.put("prioritySCH06","SCH06_SCHEDULED_ACTIVITIES.PRIORITY");
      attribute2dbField.put("activityStateSCH06","SCH06_SCHEDULED_ACTIVITIES.ACTIVITY_STATE");
      attribute2dbField.put("activityPlaceSCH06","SCH06_SCHEDULED_ACTIVITIES.ACTIVITY_PLACE");
      attribute2dbField.put("descriptionWkf01SCH06","SCH06_SCHEDULED_ACTIVITIES.DESCRIPTION_WKF01");
      attribute2dbField.put("progressiveSCH06","SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE");
      attribute2dbField.put("estimatedDurationSCH06","SCH06_SCHEDULED_ACTIVITIES.ESTIMATED_DURATION");
      attribute2dbField.put("durationSCH06","SCH06_SCHEDULED_ACTIVITIES.DURATION");
      attribute2dbField.put("completionPercSCH06","SCH06_SCHEDULED_ACTIVITIES.COMPLETION_PERC");
      attribute2dbField.put("progressiveReg04ManagerSCH06","SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE_REG04_MANAGER");
      attribute2dbField.put("progressiveReg04SubjectSCH06","SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE_REG04_SUBJECT");
      attribute2dbField.put("progressiveWkf01SCH06","SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE_WKF01");
      attribute2dbField.put("progressiveWkf08SCH06","SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE_WKF08");
      attribute2dbField.put("progressiveWkf02SCH06","SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE_WKF02");
      attribute2dbField.put("noteSCH06","SCH06_SCHEDULED_ACTIVITIES.NOTE");
      attribute2dbField.put("startDateSCH06","SCH06_SCHEDULED_ACTIVITIES.START_DATE");
      attribute2dbField.put("estimatedEndDateSCH06","SCH06_SCHEDULED_ACTIVITIES.ESTIMATED_END_DATE");
      attribute2dbField.put("endDateSCH06","SCH06_SCHEDULED_ACTIVITIES.END_DATE");
      attribute2dbField.put("expirationDateSCH06","SCH06_SCHEDULED_ACTIVITIES.EXPIRATION_DATE");
      attribute2dbField.put("managerName_1SCH06","REG04_MANAGER.NAME_1");
      attribute2dbField.put("managerName_2SCH06","REG04_MANAGER.NAME_2");
      attribute2dbField.put("subjectName_1SCH06","REG04_SUBJECTS.NAME_1");
      attribute2dbField.put("subjectName_2SCH06","REG04_SUBJECTS.NAME_2");
      attribute2dbField.put("emailAddressSCH06","SCH06_SCHEDULED_ACTIVITIES.EMAIL_ADDRESS");
      attribute2dbField.put("faxNumberSCH06","SCH06_SCHEDULED_ACTIVITIES.FAX_NUMBER");
      attribute2dbField.put("phoneNumberSCH06","SCH06_SCHEDULED_ACTIVITIES.PHONE_NUMBER");
      attribute2dbField.put("subjectTypeReg04SubjectSCH06","REG04_SUBJECTS.SUBJECT_TYPE");


      ArrayList values = new ArrayList();
      values.add(pk.getCompanyCodeSys01SCH06());
      values.add(pk.getProgressiveSCH06());

      // read from SCH06 table...
      Response res = CustomizeQueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          ScheduledActivityVO.class,
          "Y",
          "N",
          null,
          true,
          customizedFields
      );

      if (!res.isError()) {
        ScheduledActivityVO vo = (ScheduledActivityVO)((VOResponse)res).getVo();
        if (vo.getSubjectTypeReg04SubjectSCH06()!=null) {
          if (vo.getSubjectTypeReg04SubjectSCH06().equals(ApplicationConsts.SUBJECT_ORGANIZATION_CONTACT) ||
              vo.getSubjectTypeReg04SubjectSCH06().equals(ApplicationConsts.SUBJECT_ORGANIZATION_CUSTOMER))
            vo.setSubjectTypeReg04SubjectSCH06(ApplicationConsts.SUBJECT_ORGANIZATION);
          else
            vo.setSubjectTypeReg04SubjectSCH06(ApplicationConsts.SUBJECT_PEOPLE);
        }
      }

      Response answer = res;
      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching the scheduled activity",ex);
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
   * Update the scheduled activity.
   */
  public VOResponse updateActivity(ScheduledActivityVO oldVO,ScheduledActivityVO newVO,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SCH06","COMPANY_CODE_SYS01");
      attribute2dbField.put("descriptionSCH06","DESCRIPTION");
      attribute2dbField.put("activityTypeSCH06","ACTIVITY_TYPE");
      attribute2dbField.put("prioritySCH06","PRIORITY");
      attribute2dbField.put("activityStateSCH06","ACTIVITY_STATE");
      attribute2dbField.put("activityPlaceSCH06","ACTIVITY_PLACE");
      attribute2dbField.put("descriptionWkf01SCH06","DESCRIPTION_WKF01");
      attribute2dbField.put("progressiveSCH06","PROGRESSIVE");
      attribute2dbField.put("estimatedDurationSCH06","ESTIMATED_DURATION");
      attribute2dbField.put("durationSCH06","DURATION");
      attribute2dbField.put("completionPercSCH06","COMPLETION_PERC");
      attribute2dbField.put("progressiveReg04ManagerSCH06","PROGRESSIVE_REG04_MANAGER");
      attribute2dbField.put("progressiveReg04SubjectSCH06","PROGRESSIVE_REG04_SUBJECT");
      attribute2dbField.put("progressiveWkf01SCH06","PROGRESSIVE_WKF01");
      attribute2dbField.put("progressiveWkf08SCH06","PROGRESSIVE_WKF08");
      attribute2dbField.put("progressiveWkf02SCH06","PROGRESSIVE_WKF02");
      attribute2dbField.put("noteSCH06","NOTE");
      attribute2dbField.put("startDateSCH06","START_DATE");
      attribute2dbField.put("estimatedEndDateSCH06","ESTIMATED_END_DATE");
      attribute2dbField.put("endDateSCH06","END_DATE");
      attribute2dbField.put("expirationDateSCH06","EXPIRATION_DATE");
      attribute2dbField.put("emailAddressSCH06","EMAIL_ADDRESS");
      attribute2dbField.put("faxNumberSCH06","FAX_NUMBER");
      attribute2dbField.put("phoneNumberSCH06","PHONE_NUMBER");


      HashSet pkAttributes = new HashSet();
      pkAttributes.add("companyCodeSys01SCH06");
      pkAttributes.add("progressiveSCH06");

      // update SCH06 table...
      Response res = CustomizeQueryUtil.updateTable(
          conn,
          new UserSessionParameters(username),
          pkAttributes,
          oldVO,
          newVO,
          "SCH06_SCHEDULED_ACTIVITIES",
          attribute2dbField,
          "Y",
          "N",
          null,
          true,
          customizedFields
      );

      Response answer = res;
      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"updateActivity","Error while updating an existing scheduled activity",ex);
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
  public VOListResponse loadScheduledActivities(GridParams gridPars,String serverLanguageId,String username,ArrayList companiesList,ArrayList customizedFields) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      // retrieve companies list...
      String companies = "";
      if (gridPars.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01)!=null) {
        companies = "'"+gridPars.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01)+"'";
      }
      else {
        for(int i=0;i<companiesList.size();i++)
          companies += "'"+companiesList.get(i).toString()+"',";
        companies = companies.substring(0,companies.length()-1);
      }

      String sql =
          "select SCH06_SCHEDULED_ACTIVITIES.COMPANY_CODE_SYS01,SCH06_SCHEDULED_ACTIVITIES.DESCRIPTION,SCH06_SCHEDULED_ACTIVITIES.ACTIVITY_TYPE,"+
          "SCH06_SCHEDULED_ACTIVITIES.PRIORITY,SCH06_SCHEDULED_ACTIVITIES.ACTIVITY_STATE,SCH06_SCHEDULED_ACTIVITIES.ACTIVITY_PLACE,"+
          "SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE,SCH06_SCHEDULED_ACTIVITIES.ESTIMATED_DURATION,SCH06_SCHEDULED_ACTIVITIES.DURATION,"+
          "SCH06_SCHEDULED_ACTIVITIES.COMPLETION_PERC,SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE_REG04_MANAGER,SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE_REG04_SUBJECT,"+
          "SCH06_SCHEDULED_ACTIVITIES.START_DATE,SCH06_SCHEDULED_ACTIVITIES.ESTIMATED_END_DATE,SCH06_SCHEDULED_ACTIVITIES.END_DATE,"+
          "SCH06_SCHEDULED_ACTIVITIES.EXPIRATION_DATE, "+
          "REG04_MANAGER.NAME_1,REG04_MANAGER.NAME_2,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,REG04_SUBJECTS.SUBJECT_TYPE "+
          "from SCH06_SCHEDULED_ACTIVITIES "+
          "LEFT OUTER JOIN "+
          "(select REG04_SUBJECTS.COMPANY_CODE_SYS01,REG04_SUBJECTS.PROGRESSIVE,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2 "+
          "from REG04_SUBJECTS) REG04_MANAGER ON "+
          "REG04_MANAGER.COMPANY_CODE_SYS01=SCH06_SCHEDULED_ACTIVITIES.COMPANY_CODE_SYS01 and "+
          "REG04_MANAGER.PROGRESSIVE=SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE_REG04_MANAGER "+
          "LEFT OUTER JOIN "+
          "(select REG04_SUBJECTS.COMPANY_CODE_SYS01,REG04_SUBJECTS.PROGRESSIVE,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,REG04_SUBJECTS.SUBJECT_TYPE "+
          "from REG04_SUBJECTS) REG04_SUBJECTS ON "+
          "REG04_SUBJECTS.COMPANY_CODE_SYS01=SCH06_SCHEDULED_ACTIVITIES.COMPANY_CODE_SYS01 and "+
          "REG04_SUBJECTS.PROGRESSIVE=SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE_REG04_SUBJECT "+
          "where SCH06_SCHEDULED_ACTIVITIES.COMPANY_CODE_SYS01 in ("+companies+")";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SCH06","SCH06_SCHEDULED_ACTIVITIES.COMPANY_CODE_SYS01");
      attribute2dbField.put("descriptionSCH06","SCH06_SCHEDULED_ACTIVITIES.DESCRIPTION");
      attribute2dbField.put("activityTypeSCH06","SCH06_SCHEDULED_ACTIVITIES.ACTIVITY_TYPE");
      attribute2dbField.put("prioritySCH06","SCH06_SCHEDULED_ACTIVITIES.PRIORITY");
      attribute2dbField.put("activityStateSCH06","SCH06_SCHEDULED_ACTIVITIES.ACTIVITY_STATE");
      attribute2dbField.put("activityPlaceSCH06","SCH06_SCHEDULED_ACTIVITIES.ACTIVITY_PLACE");
      attribute2dbField.put("progressiveSCH06","SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE");
      attribute2dbField.put("estimatedDurationSCH06","SCH06_SCHEDULED_ACTIVITIES.ESTIMATED_DURATION");
      attribute2dbField.put("durationSCH06","SCH06_SCHEDULED_ACTIVITIES.DURATION");
      attribute2dbField.put("completionPercSCH06","SCH06_SCHEDULED_ACTIVITIES.COMPLETION_PERC");
      attribute2dbField.put("progressiveReg04ManagerSCH06","SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE_REG04_MANAGER");
      attribute2dbField.put("progressiveReg04SubjectSCH06","SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE_REG04_SUBJECT");
      attribute2dbField.put("startDateSCH06","SCH06_SCHEDULED_ACTIVITIES.START_DATE");
      attribute2dbField.put("estimatedEndDateSCH06","SCH06_SCHEDULED_ACTIVITIES.ESTIMATED_END_DATE");
      attribute2dbField.put("endDateSCH06","SCH06_SCHEDULED_ACTIVITIES.END_DATE");
      attribute2dbField.put("expirationDateSCH06","SCH06_SCHEDULED_ACTIVITIES.EXPIRATION_DATE");
      attribute2dbField.put("managerName_1SCH06","REG04_MANAGER.NAME_1");
      attribute2dbField.put("managerName_2SCH06","REG04_MANAGER.NAME_2");
      attribute2dbField.put("subjectName_1SCH06","REG04_SUBJECTS.NAME_1");
      attribute2dbField.put("subjectName_2SCH06","REG04_SUBJECTS.NAME_2");
      attribute2dbField.put("subjectTypeReg04SubjectSCH06","REG04_SUBJECTS.SUBJECT_TYPE");

      ArrayList values = new ArrayList();
      if (gridPars.getOtherGridParams().get(ApplicationConsts.START_DATE)!=null) {
        sql += " and SCH06_SCHEDULED_ACTIVITIES.START_DATE>=?";
        values.add(gridPars.getOtherGridParams().get(ApplicationConsts.START_DATE));
      }
      if (gridPars.getOtherGridParams().get(ApplicationConsts.END_DATE)!=null) {
        sql += " and SCH06_SCHEDULED_ACTIVITIES.END_DATE<=?";
        values.add(gridPars.getOtherGridParams().get(ApplicationConsts.END_DATE));
      }
      if (gridPars.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04_MANAGER)!=null) {
        sql += " and SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE_REG04_MANAGER=?";
        values.add(gridPars.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04_MANAGER));
      }
      if (gridPars.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04_SUBJECT)!=null) {
        sql += " and SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE_REG04_SUBJECT=?";
        values.add(gridPars.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04_SUBJECT));
      }
      if (gridPars.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04)!=null) {
        sql +=
          " and SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE in "+
          "(select SCH07_SCHEDULED_EMPLOYEES.PROGRESSIVE_SCH06 from SCH07_SCHEDULED_EMPLOYEES where "+
          "SCH07_SCHEDULED_EMPLOYEES.COMPANY_CODE_SYS01=SCH06_SCHEDULED_ACTIVITIES.COMPANY_CODE_SYS01 and SCH07_SCHEDULED_EMPLOYEES.PROGRESSIVE_REG04=?)";
        values.add(gridPars.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04));
      }
      if (gridPars.getOtherGridParams().get(ApplicationConsts.ACTIVITY_TYPE)!=null) {
        sql += " and SCH06_SCHEDULED_ACTIVITIES.ACTIVITY_TYPE=?";
        values.add(gridPars.getOtherGridParams().get(ApplicationConsts.ACTIVITY_TYPE));
      }
      if (gridPars.getOtherGridParams().get(ApplicationConsts.ACTIVITY_STATE)!=null) {
        sql += " and SCH06_SCHEDULED_ACTIVITIES.ACTIVITY_STATE=?";
        values.add(gridPars.getOtherGridParams().get(ApplicationConsts.ACTIVITY_STATE));
      }

      // read from SCH06 table...
      Response answer = CustomizeQueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          GridScheduledActivityVO.class,
          "Y",
          "N",
          null,
          gridPars,
          50,
          true,
          customizedFields
      );
      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching scheduled activities list",ex);
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
  public VOResponse deleteActivities(ArrayList pks,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      stmt = conn.createStatement();

      ScheduledActivityPK pk = null;
      for(int i=0;i<pks.size();i++) {
        pk = (ScheduledActivityPK)pks.get(i);

        // phisically delete the record in SCH07...
        stmt.execute(
            "delete from SCH07_SCHEDULED_EMPLOYEES where "+
            "COMPANY_CODE_SYS01='"+pk.getCompanyCodeSys01SCH06()+"' and "+
            "PROGRESSIVE_SCH06="+pk.getProgressiveSCH06()
        );

        // phisically delete the record in SCH08...
        stmt.execute(
            "delete from SCH08_ACT_ATTACHED_DOCS where "+
            "COMPANY_CODE_SYS01='"+pk.getCompanyCodeSys01SCH06()+"' and "+
            "PROGRESSIVE_SCH06="+pk.getProgressiveSCH06()
        );

        // phisically delete the record in SCH09...
        stmt.execute(
            "delete from SCH09_SCHEDULED_MACHINERIES where "+
            "COMPANY_CODE_SYS01='"+pk.getCompanyCodeSys01SCH06()+"' and "+
            "PROGRESSIVE_SCH06="+pk.getProgressiveSCH06()
        );

        // phisically delete the record in SCH15...
        stmt.execute(
            "delete from SCH15_SCHEDULED_ITEMS where "+
            "COMPANY_CODE_SYS01='"+pk.getCompanyCodeSys01SCH06()+"' and "+
            "PROGRESSIVE_SCH06="+pk.getProgressiveSCH06()
        );

        // update record in SCH03 if it is linked to the record in SCH06...
        stmt.execute(
            "update SCH03_CALL_OUT_REQUESTS set PROGRESSIVE_SCH06=null where "+
            "COMPANY_CODE_SYS01='"+pk.getCompanyCodeSys01SCH06()+"' and "+
            "PROGRESSIVE_SCH06="+pk.getProgressiveSCH06()
        );

        // phisically delete the record in SCH06...
        stmt.execute(
            "delete from SCH06_SCHEDULED_ACTIVITIES where "+
            "COMPANY_CODE_SYS01='"+pk.getCompanyCodeSys01SCH06()+"' and "+
            "PROGRESSIVE="+pk.getProgressiveSCH06()
        );

      }

      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"deleteActivities","Error while deleting existing scheduled activities",ex);
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
   * Insert a new record in SCH06.
   */
  public VOResponse insertActivity(ScheduledActivityVO vo,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      // generate PROGRESSIVE value...
      stmt = conn.createStatement();
      BigDecimal progressive = CompanyProgressiveUtils.getInternalProgressive(vo.getCompanyCodeSys01SCH06(),"SCH06_SCHEDULED_ACTIVITIES","PROGRESSIVE",conn);
      vo.setProgressiveSCH06(progressive);

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SCH06","COMPANY_CODE_SYS01");
      attribute2dbField.put("descriptionSCH06","DESCRIPTION");
      attribute2dbField.put("activityTypeSCH06","ACTIVITY_TYPE");
      attribute2dbField.put("prioritySCH06","PRIORITY");
      attribute2dbField.put("activityStateSCH06","ACTIVITY_STATE");
      attribute2dbField.put("activityPlaceSCH06","ACTIVITY_PLACE");
      attribute2dbField.put("descriptionWkf01SCH06","DESCRIPTION_WKF01");
      attribute2dbField.put("progressiveSCH06","PROGRESSIVE");
      attribute2dbField.put("estimatedDurationSCH06","ESTIMATED_DURATION");
      attribute2dbField.put("durationSCH06","DURATION");
      attribute2dbField.put("completionPercSCH06","COMPLETION_PERC");
      attribute2dbField.put("progressiveReg04ManagerSCH06","PROGRESSIVE_REG04_MANAGER");
      attribute2dbField.put("progressiveReg04SubjectSCH06","PROGRESSIVE_REG04_SUBJECT");
      attribute2dbField.put("progressiveWkf01SCH06","PROGRESSIVE_WKF01");
      attribute2dbField.put("progressiveWkf08SCH06","PROGRESSIVE_WKF08");
      attribute2dbField.put("progressiveWkf02SCH06","PROGRESSIVE_WKF02");
      attribute2dbField.put("noteSCH06","NOTE");
      attribute2dbField.put("startDateSCH06","START_DATE");
      attribute2dbField.put("estimatedEndDateSCH06","ESTIMATED_END_DATE");
      attribute2dbField.put("endDateSCH06","END_DATE");
      attribute2dbField.put("expirationDateSCH06","EXPIRATION_DATE");
      attribute2dbField.put("emailAddressSCH06","EMAIL_ADDRESS");
      attribute2dbField.put("faxNumberSCH06","FAX_NUMBER");
      attribute2dbField.put("phoneNumberSCH06","PHONE_NUMBER");

      // insert into SCH06...
      Response res = CustomizeQueryUtil.insertTable(
          conn,
          new UserSessionParameters(username),
          vo,
          "SCH06_SCHEDULED_ACTIVITIES",
          attribute2dbField,
          "Y",
          "N",
          null,
          true,
          customizedFields
      );

      Response answer = res;
      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"insertActivity","Error while inserting a new scheduled activity",ex);
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
  public VOResponse closeScheduledActivity(ScheduledActivityVO vo,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      vo.setActivityStateSCH06(ApplicationConsts.CLOSED);

      pstmt = conn.prepareStatement(
        "update SCH06_SCHEDULED_ACTIVITIES set ACTIVITY_STATE=? where COMPANY_CODE_SYS01=? and PROGRESSIVE=? and ACTIVITY_STATE=?"
      );
      pstmt.setString(1,ApplicationConsts.CLOSED);
      pstmt.setString(2,vo.getCompanyCodeSys01SCH06());
      pstmt.setBigDecimal(3,vo.getProgressiveSCH06());
      pstmt.setString(4,ApplicationConsts.OPENED);
      int rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        throw new Exception("Updating not performed: the record was previously updated.");
      }

      pstmt = conn.prepareStatement(
        "update SCH03_CALL_OUT_REQUESTS set CALL_OUT_STATE=? where COMPANY_CODE_SYS01=? and PROGRESSIVE_SCH06=? "
      );
      pstmt.setString(1,ApplicationConsts.CLOSED);
      pstmt.setString(2,vo.getCompanyCodeSys01SCH06());
      pstmt.setBigDecimal(3,vo.getProgressiveSCH06());
      pstmt.executeUpdate();

      return new VOResponse(vo);
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing scheduled items",ex);
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

