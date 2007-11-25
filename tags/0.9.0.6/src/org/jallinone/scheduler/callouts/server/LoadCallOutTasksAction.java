package org.jallinone.scheduler.callouts.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.scheduler.callouts.java.CallOutTaskVO;
import org.jallinone.scheduler.callouts.java.CallOutPK;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch call-out tasks from SCH12 table.</p>
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
public class LoadCallOutTasksAction implements Action {


  public LoadCallOutTasksAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadCallOutTasks";
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
     String sql =
          "select SCH12_CALL_OUT_TASKS.COMPANY_CODE_SYS01,SCH12_CALL_OUT_TASKS.TASK_CODE_REG07,SCH12_CALL_OUT_TASKS.CALL_OUT_CODE_SCH10,SYS10_TRANSLATIONS.DESCRIPTION "+
          "from SCH12_CALL_OUT_TASKS,REG07_TASKS,SYS10_TRANSLATIONS where "+
          "SCH12_CALL_OUT_TASKS.TASK_CODE_REG07=REG07_TASKS.TASK_CODE and "+
          "REG07_TASKS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "SCH12_CALL_OUT_TASKS.COMPANY_CODE_SYS01=? and "+
          "SCH12_CALL_OUT_TASKS.CALL_OUT_CODE_SCH10=?";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SCH12","SCH12_CALL_OUT_TASKS.COMPANY_CODE_SYS01");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("callOutCodeSch10SCH12","SCH12_CALL_OUT_TASKS.CALL_OUT_CODE_SCH10");
      attribute2dbField.put("taskCodeReg07SCH12","SCH12_CALL_OUT_TASKS.TASK_CODE_REG07");

      GridParams gridParams = (GridParams)inputPar;
      CallOutPK pk = (CallOutPK)gridParams.getOtherGridParams().get(ApplicationConsts.CALL_OUT_PK);

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(pk.getCompanyCodeSys01SCH10());
      values.add(pk.getCallOutCodeSCH10());


      // read from SCH12 table...
      Response answer = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          CallOutTaskVO.class,
          "Y",
          "N",
          context,
          gridParams,
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching call-out tasks list",ex);
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
