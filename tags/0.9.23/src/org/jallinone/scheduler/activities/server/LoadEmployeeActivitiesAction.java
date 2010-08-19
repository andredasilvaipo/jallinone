package org.jallinone.scheduler.activities.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.commons.server.CustomizeQueryUtil;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.scheduler.activities.java.EmployeeActivityVO;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch scheduled activities assigned to a specified employee.</p>
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
public class LoadEmployeeActivitiesAction implements Action {


  public LoadEmployeeActivitiesAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadEmployeeActivities";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();

    Connection conn = null;
    try {
      conn = ConnectionManager.getConnection(context);

      // fires the GenericEvent.CONNECTION_CREATED event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        getRequestName(),
        GenericEvent.CONNECTION_CREATED,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        inputPar,
        null
      ));

      GridParams gridPars = (GridParams)inputPar;

      // retrieve companies list...
      String companies = "";
      if (gridPars.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01)!=null) {
        companies = "'"+gridPars.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01)+"'";
      }
      else {
        ArrayList companiesList = ((JAIOUserSessionParameters)userSessionPars).getCompanyBa().getCompaniesList("SCH06_SCHEDULED_ACT");
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
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          EmployeeActivityVO.class,
          "Y",
          "N",
          context,
          gridPars,
          true
      );


      // fires the GenericEvent.BEFORE_COMMIT event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        getRequestName(),
        GenericEvent.BEFORE_COMMIT,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        inputPar,
        answer
      ));

      return answer;

    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching scheduled activities assigned to a specific employee",ex);
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        ConnectionManager.releaseConnection(conn, context);
      }
      catch (Exception ex1) {
      }
    }

  }



}
