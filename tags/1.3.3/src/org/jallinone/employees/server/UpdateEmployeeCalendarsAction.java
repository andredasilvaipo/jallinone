package org.jallinone.employees.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.employees.java.*;
import java.math.BigDecimal;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.subjects.server.PeopleBean;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to update employee calendars.</p>
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
public class UpdateEmployeeCalendarsAction implements Action {


  public UpdateEmployeeCalendarsAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "updateEmployeeCalendars";
  }


  private PeopleBean bean = new PeopleBean();


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    Connection conn = null;
    try {
      String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();
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


      ArrayList oldList = ((ArrayList[])inputPar)[0];
      ArrayList newList = ((ArrayList[])inputPar)[1];
      EmployeeCalendarVO oldVO = null;
      EmployeeCalendarVO newVO = null;


      HashSet pkAttrs = new HashSet();
      pkAttrs.add("companyCodeSys01SCH02");
      pkAttrs.add("progressiveReg04SCH02");
      pkAttrs.add("dayOfWeekSCH02");

      HashMap attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SCH02","COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveReg04SCH02","PROGRESSIVE_REG04");
      attribute2dbField.put("dayOfWeekSCH02","DAY_OF_WEEK");
      attribute2dbField.put("morningStartHourSCH02","MORNING_START_HOUR");
      attribute2dbField.put("morningEndHourSCH02","MORNING_END_HOUR");
      attribute2dbField.put("afternoonStartHourSCH02","AFTERNOON_START_HOUR");
      attribute2dbField.put("afternoonEndHourSCH02","AFTERNOON_END_HOUR");

      Response res = null;
      for(int i=0;i<newList.size();i++) {
        oldVO = (EmployeeCalendarVO)oldList.get(i);
        newVO = (EmployeeCalendarVO)newList.get(i);

        res = new QueryUtil().updateTable(
            conn,
            userSessionPars,
            pkAttrs,
            oldVO,
            newVO,
            "SCH02_EMPLOYEE_CALENDAR",
            attribute2dbField,
            "Y",
            "N",
            context,
            true
        );
        if (res.isError()) {
          conn.rollback();
          return res;
        }

      }

      Response answer = new VOListResponse(newList,false,newList.size());

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

      conn.commit();

      // fires the GenericEvent.AFTER_COMMIT event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        getRequestName(),
        GenericEvent.AFTER_COMMIT,
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while updating employee calendars",ex);
      try {
        conn.rollback();
      }
      catch (Exception ex3) {
      }
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
