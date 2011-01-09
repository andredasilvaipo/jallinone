package org.jallinone.scheduler.gantt.server;

import org.openswing.swing.server.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;
import java.math.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.commons.java.*;
import org.jallinone.scheduler.activities.java.*;
import org.openswing.swing.gantt.java.*;
import org.jallinone.employees.server.*;
import org.jallinone.employees.java.*;

import java.awt.*;

import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import org.jallinone.commons.server.JAIOBeanFactory;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch scheduled employees from SCH07 table,
 * and filtering them by task code, date interval and scheduled activity id.</p>
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
public class LoadScheduledEmployeesOnGanttAction implements Action {

  public LoadScheduledEmployeesOnGanttAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadScheduledEmployeesOnGantt";
  }


  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    try {
      HashMap map = (HashMap)inputPar;
      Response answer = loadScheduledEmployeesOnGantt(map,((JAIOUserSessionParameters)userSessionPars).getServerLanguageId(),userSessionPars.getUsername());

    return answer;
    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while processing request",ex);
      return new ErrorResponse(ex.getMessage());
    }
  }
  
  

  /**
   * Business logic to execute.
   */
  public VOListResponse loadScheduledEmployeesOnGantt(HashMap map,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      conn = ConnectionManager.getConnection(null);
      EmployeesBean empCal = new EmployeesBean();
      empCal.setConn(conn); // use same transaction...


      ScheduledEmployeeVO empVO = (ScheduledEmployeeVO)map.get(ApplicationConsts.SCHEDULED_EMPLOYEE);
      java.sql.Date fromDate = new java.sql.Date(((java.util.Date)map.get(ApplicationConsts.START_DATE)).getTime());
      java.sql.Date toDate = new java.sql.Date(((java.util.Date)map.get(ApplicationConsts.END_DATE)).getTime()+86400000L); // the day after...


      String sql =
          "select SCH01_EMPLOYEES.COMPANY_CODE_SYS01,SCH01_EMPLOYEES.PROGRESSIVE_REG04,SCH07_SCHEDULED_EMPLOYEES.PROGRESSIVE_SCH06,"+
          "SCH07_SCHEDULED_EMPLOYEES.START_DATE,SCH07_SCHEDULED_EMPLOYEES.END_DATE,SCH07_SCHEDULED_EMPLOYEES.DURATION,"+
          "SCH07_SCHEDULED_EMPLOYEES.NOTE,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,SCH01_EMPLOYEES.EMPLOYEE_CODE,SCH01_EMPLOYEES.TASK_CODE_REG07,"+
          "SYS10_TRANSLATIONS.DESCRIPTION "+
          "from REG04_SUBJECTS,REG07_TASKS,SYS10_TRANSLATIONS,SCH01_EMPLOYEES "+
          "LEFT OUTER JOIN "+
          "(select SCH07_SCHEDULED_EMPLOYEES.COMPANY_CODE_SYS01,SCH07_SCHEDULED_EMPLOYEES.PROGRESSIVE_REG04,"+
          "SCH07_SCHEDULED_EMPLOYEES.PROGRESSIVE_SCH06,SCH07_SCHEDULED_EMPLOYEES.START_DATE,"+
          "SCH07_SCHEDULED_EMPLOYEES.END_DATE,SCH07_SCHEDULED_EMPLOYEES.DURATION,SCH07_SCHEDULED_EMPLOYEES.NOTE "+
          "from SCH07_SCHEDULED_EMPLOYEES) SCH07_SCHEDULED_EMPLOYEES ON "+
          "SCH07_SCHEDULED_EMPLOYEES.COMPANY_CODE_SYS01=SCH01_EMPLOYEES.COMPANY_CODE_SYS01 and "+
          "SCH07_SCHEDULED_EMPLOYEES.PROGRESSIVE_REG04=SCH01_EMPLOYEES.PROGRESSIVE_REG04 and "+
//          "SCH07_SCHEDULED_EMPLOYEES.PROGRESSIVE_SCH06=? and "+
          "SCH07_SCHEDULED_EMPLOYEES.START_DATE>=? and "+
          "SCH07_SCHEDULED_EMPLOYEES.END_DATE<? "+
          " where "+
          "SCH01_EMPLOYEES.COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 and "+
          "SCH01_EMPLOYEES.PROGRESSIVE_REG04=REG04_SUBJECTS.PROGRESSIVE and "+
          "SCH01_EMPLOYEES.COMPANY_CODE_SYS01=REG07_TASKS.COMPANY_CODE_SYS01 and "+
          "SCH01_EMPLOYEES.TASK_CODE_REG07=REG07_TASKS.TASK_CODE and "+
          "REG07_TASKS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "SCH01_EMPLOYEES.COMPANY_CODE_SYS01=? and "+
          "SCH01_EMPLOYEES.TASK_CODE_REG07=? "+
          " order by SCH01_EMPLOYEES.EMPLOYEE_CODE,SCH07_SCHEDULED_EMPLOYEES.START_DATE";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SCH07","SCH01_EMPLOYEES.COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveReg04SCH07","SCH01_EMPLOYEES.PROGRESSIVE_REG04");
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
      attribute2dbField.put("description","SCH07_SCHEDULED_EMPLOYEES.NOTE");

      GridParams gridParams = new GridParams();

      ArrayList values = new ArrayList();
//      values.add(empVO.getProgressiveSch06SCH07());
      values.add(fromDate);
      values.add(toDate);
      values.add(serverLanguageId);
      values.add(empVO.getCompanyCodeSys01SCH07());
      values.add(empVO.getTaskCodeREG07());

      if (empVO.getProgressiveReg04SCH07()!=null) {
        sql += " and SCH07_SCHEDULED_EMPLOYEES.PROGRESSIVE_REG04=? ";
        values.add(empVO.getProgressiveReg04SCH07());
      }
      if (empVO.getStartDateSCH07()!=null) {
        sql += " and SCH07_SCHEDULED_EMPLOYEES.START_DATE=? ";
        values.add(empVO.getStartDateSCH07());
      }

      // read from SCH01/SCH07/REG04 tables...
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

      if (res.isError())
        throw new Exception(res.getErrorMessage());

      java.util.List rows = ((VOListResponse)res).getRows();
      ArrayList ganttRows = new ArrayList();
      GanttRowVO ganttVO = null;
      ScheduledEmployeeVO vo = null;
      HashSet appointments = null;
      gridParams = new GridParams();
      Response calRes = null;
      EmployeeCalendarVO empCalVO = null;
      java.util.List calRows = null;
      int i=0;
      String empCode = null;
      Hashtable days = new Hashtable();
      while(i<rows.size()) {
        vo = (ScheduledEmployeeVO)rows.get(i);
        vo.setEnableDelete(false);
        vo.setEnableEdit(
          empVO.getProgressiveSch06SCH07().equals(vo.getProgressiveSch06SCH07()) &&
          empVO.getProgressiveReg04SCH07().equals(vo.getProgressiveReg04SCH07())
        );

        if (!vo.getEmployeeCodeSCH01().equals(empCode)) {
          // new employee...
          empCode = vo.getEmployeeCodeSCH01();
          ganttVO = new GanttRowVO();
          ganttVO.setAppointmentClass(ScheduledEmployeeVO.class);

          // set appointments...
          appointments = new HashSet();
          ganttVO.setAppointments(appointments);

          // set employee calendar (working hours for each week day)...
          gridParams.getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01SCH07());
          gridParams.getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_REG04,vo.getProgressiveReg04SCH07());
          calRes = empCal.loadEmployeeCalendar(gridParams,serverLanguageId,username);
          if (calRes.isError())
            throw new Exception(res.getErrorMessage());
          calRows = ((VOListResponse)calRes).getRows();
          days.clear();
          for(int j=0;j<calRows.size();j++)
            days.put( ((EmployeeCalendarVO)calRows.get(j)).getDayOfWeekSCH02(),calRows.get(j) );
          if (days.containsKey(String.valueOf(Calendar.SUNDAY)))
            ganttVO.setSundayWorkingHours((EmployeeCalendarVO)days.get(String.valueOf(Calendar.SUNDAY)));
          if (days.containsKey(String.valueOf(Calendar.MONDAY)))
            ganttVO.setMondayWorkingHours((EmployeeCalendarVO)days.get(String.valueOf(Calendar.MONDAY)));
          if (days.containsKey(String.valueOf(Calendar.TUESDAY)))
            ganttVO.setTuesdayWorkingHours((EmployeeCalendarVO)days.get(String.valueOf(Calendar.TUESDAY)));
          if (days.containsKey(String.valueOf(Calendar.WEDNESDAY)))
            ganttVO.setWednesdayWorkingHours((EmployeeCalendarVO)days.get(String.valueOf(Calendar.WEDNESDAY)));
          if (days.containsKey(String.valueOf(Calendar.THURSDAY)))
            ganttVO.setThursdayWorkingHours((EmployeeCalendarVO)days.get(String.valueOf(Calendar.THURSDAY)));
          if (days.containsKey(String.valueOf(Calendar.FRIDAY)))
            ganttVO.setFridayWorkingHours((EmployeeCalendarVO)days.get(String.valueOf(Calendar.FRIDAY)));
          if (days.containsKey(String.valueOf(Calendar.SATURDAY)))
            ganttVO.setSaturdayWorkingHours((EmployeeCalendarVO)days.get(String.valueOf(Calendar.SATURDAY)));

          // set the row legend...
          ganttVO.setLegend(new Object[] {
            vo.getEmployeeCodeSCH01(),
            vo.getProgressiveReg04SCH07(),
            vo.getName_1REG04()+" "+vo.getName_2REG04()
          });

          ganttRows.add(ganttVO);
        }
        else {
          // employee already added...
          appointments = ganttVO.getAppointments();
        }

        if (vo.getStartDate()!=null && vo.getEndDate()!=null)
          appointments.add(vo);
        if (vo.isEnableEdit())
          vo.setForegroundColor(Color.black);
        else if (empVO.getProgressiveSch06SCH07().equals(vo.getProgressiveSch06SCH07()))
          vo.setForegroundColor(Color.lightGray);
        else
          vo.setForegroundColor(Color.GRAY);

        i++;
      }

      return new VOListResponse(ganttRows,false,ganttRows.size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching scheduled employees for the gantt diagram",ex);
      throw new Exception(ex.getMessage());
    }
    finally {
      try {
        pstmt.close();
      }
      catch (Exception ex2) {
      }
      try {
    	  ConnectionManager.releaseConnection(conn, null);
      } catch (Exception e) {
      }
    }

  }



  
}

