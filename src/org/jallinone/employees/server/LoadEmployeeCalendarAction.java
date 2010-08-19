package org.jallinone.employees.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.employees.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.commons.server.CustomizeQueryUtil;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.employees.java.EmployeeCalendarVO;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch employee calendars from SCH02 table.</p>
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
public class LoadEmployeeCalendarAction implements Action {


  public LoadEmployeeCalendarAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadEmployeeCalendar";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    Connection conn = null;
    PreparedStatement pstmt = null;
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

      String sql =
          "select SCH02_EMPLOYEE_CALENDAR.MORNING_START_HOUR,SCH02_EMPLOYEE_CALENDAR.MORNING_END_HOUR,"+
          "SCH02_EMPLOYEE_CALENDAR.AFTERNOON_START_HOUR,SCH02_EMPLOYEE_CALENDAR.AFTERNOON_END_HOUR "+
          "from SCH02_EMPLOYEE_CALENDAR where "+
          "SCH02_EMPLOYEE_CALENDAR.COMPANY_CODE_SYS01=? and "+
          "SCH02_EMPLOYEE_CALENDAR.PROGRESSIVE_REG04=? and "+
          "SCH02_EMPLOYEE_CALENDAR.DAY_OF_WEEK=?";
      pstmt = conn.prepareStatement(sql);

      GridParams gridParams = (GridParams)inputPar;
      ArrayList list = new ArrayList();

      pstmt.setObject(1,gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01));
      pstmt.setObject(2,gridParams.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04));
      pstmt.setObject(3,String.valueOf(Calendar.SUNDAY));
      ResultSet rset = pstmt.executeQuery();
      if (rset.next()) {
        EmployeeCalendarVO vo = new EmployeeCalendarVO();
        vo.setCompanyCodeSys01SCH02((String)gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01));
        vo.setProgressiveReg04SCH02((BigDecimal)gridParams.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04));
        vo.setDayOfWeekSCH02(String.valueOf(Calendar.SUNDAY));
        vo.setMorningStartHourSCH02(rset.getTimestamp(1));
        vo.setMorningEndHourSCH02(rset.getTimestamp(2));
        vo.setAfternoonStartHourSCH02(rset.getTimestamp(3));
        vo.setAfternoonEndHourSCH02(rset.getTimestamp(4));
        list.add(vo);
      }
      rset.close();

      pstmt.setObject(1,gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01));
      pstmt.setObject(2,gridParams.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04));
      pstmt.setObject(3,String.valueOf(Calendar.MONDAY));
      rset = pstmt.executeQuery();
      if (rset.next()) {
        EmployeeCalendarVO vo = new EmployeeCalendarVO();
        vo.setCompanyCodeSys01SCH02((String)gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01));
        vo.setProgressiveReg04SCH02((BigDecimal)gridParams.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04));
        vo.setDayOfWeekSCH02(String.valueOf(Calendar.MONDAY));
        vo.setMorningStartHourSCH02(rset.getTimestamp(1));
        vo.setMorningEndHourSCH02(rset.getTimestamp(2));
        vo.setAfternoonStartHourSCH02(rset.getTimestamp(3));
        vo.setAfternoonEndHourSCH02(rset.getTimestamp(4));
        list.add(vo);
      }
      rset.close();

      pstmt.setObject(1,gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01));
      pstmt.setObject(2,gridParams.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04));
      pstmt.setObject(3,String.valueOf(Calendar.TUESDAY));
      rset = pstmt.executeQuery();
      if (rset.next()) {
        EmployeeCalendarVO vo = new EmployeeCalendarVO();
        vo.setCompanyCodeSys01SCH02((String)gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01));
        vo.setProgressiveReg04SCH02((BigDecimal)gridParams.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04));
        vo.setDayOfWeekSCH02(String.valueOf(Calendar.TUESDAY));
        vo.setMorningStartHourSCH02(rset.getTimestamp(1));
        vo.setMorningEndHourSCH02(rset.getTimestamp(2));
        vo.setAfternoonStartHourSCH02(rset.getTimestamp(3));
        vo.setAfternoonEndHourSCH02(rset.getTimestamp(4));
        list.add(vo);
      }
      rset.close();

      pstmt.setObject(1,gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01));
      pstmt.setObject(2,gridParams.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04));
      pstmt.setObject(3,String.valueOf(Calendar.WEDNESDAY));
      rset = pstmt.executeQuery();
      if (rset.next()) {
        EmployeeCalendarVO vo = new EmployeeCalendarVO();
        vo.setCompanyCodeSys01SCH02((String)gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01));
        vo.setProgressiveReg04SCH02((BigDecimal)gridParams.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04));
        vo.setDayOfWeekSCH02(String.valueOf(Calendar.WEDNESDAY));
        vo.setMorningStartHourSCH02(rset.getTimestamp(1));
        vo.setMorningEndHourSCH02(rset.getTimestamp(2));
        vo.setAfternoonStartHourSCH02(rset.getTimestamp(3));
        vo.setAfternoonEndHourSCH02(rset.getTimestamp(4));
        list.add(vo);
      }
      rset.close();

      pstmt.setObject(1,gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01));
      pstmt.setObject(2,gridParams.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04));
      pstmt.setObject(3,String.valueOf(Calendar.THURSDAY));
      rset = pstmt.executeQuery();
      if (rset.next()) {
        EmployeeCalendarVO vo = new EmployeeCalendarVO();
        vo.setCompanyCodeSys01SCH02((String)gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01));
        vo.setProgressiveReg04SCH02((BigDecimal)gridParams.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04));
        vo.setDayOfWeekSCH02(String.valueOf(Calendar.THURSDAY));
        vo.setMorningStartHourSCH02(rset.getTimestamp(1));
        vo.setMorningEndHourSCH02(rset.getTimestamp(2));
        vo.setAfternoonStartHourSCH02(rset.getTimestamp(3));
        vo.setAfternoonEndHourSCH02(rset.getTimestamp(4));
        list.add(vo);
      }
      rset.close();

      pstmt.setObject(1,gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01));
      pstmt.setObject(2,gridParams.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04));
      pstmt.setObject(3,String.valueOf(Calendar.FRIDAY));
      rset = pstmt.executeQuery();
      if (rset.next()) {
        EmployeeCalendarVO vo = new EmployeeCalendarVO();
        vo.setCompanyCodeSys01SCH02((String)gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01));
        vo.setProgressiveReg04SCH02((BigDecimal)gridParams.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04));
        vo.setDayOfWeekSCH02(String.valueOf(Calendar.FRIDAY));
        vo.setMorningStartHourSCH02(rset.getTimestamp(1));
        vo.setMorningEndHourSCH02(rset.getTimestamp(2));
        vo.setAfternoonStartHourSCH02(rset.getTimestamp(3));
        vo.setAfternoonEndHourSCH02(rset.getTimestamp(4));
        list.add(vo);
      }
      rset.close();

      pstmt.setObject(1,gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01));
      pstmt.setObject(2,gridParams.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04));
      pstmt.setObject(3,String.valueOf(Calendar.SATURDAY));
      rset = pstmt.executeQuery();
      if (rset.next()) {
        EmployeeCalendarVO vo = new EmployeeCalendarVO();
        vo.setCompanyCodeSys01SCH02((String)gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01));
        vo.setProgressiveReg04SCH02((BigDecimal)gridParams.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04));
        vo.setDayOfWeekSCH02(String.valueOf(Calendar.SATURDAY));
        vo.setMorningStartHourSCH02(rset.getTimestamp(1));
        vo.setMorningEndHourSCH02(rset.getTimestamp(2));
        vo.setAfternoonStartHourSCH02(rset.getTimestamp(3));
        vo.setAfternoonEndHourSCH02(rset.getTimestamp(4));
        list.add(vo);
      }
      rset.close();

      Response answer = new VOListResponse(list,false,list.size());

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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching employee calendar",ex);
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        pstmt.close();
      }
      catch (Exception ex2) {
      }
      try {
        ConnectionManager.releaseConnection(conn, context);
      }
      catch (Exception ex1) {
      }
    }

  }



}
