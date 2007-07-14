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
import org.jallinone.scheduler.callouts.java.CallOutPK;
import org.jallinone.scheduler.callouts.java.CallOutMachineryVO;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch call-out machineries from SCH13 table.</p>
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
public class LoadCallOutMachineriesAction implements Action {


  public LoadCallOutMachineriesAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadCallOutMachineries";
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
          "select SCH13_CALL_OUT_MACHINERIES.MACHINERY_CODE_PRO03,SCH13_CALL_OUT_MACHINERIES.COMPANY_CODE_SYS01,SCH13_CALL_OUT_MACHINERIES.CALL_OUT_CODE_SCH10,SYS10_TRANSLATIONS.DESCRIPTION "+
          "from SCH13_CALL_OUT_MACHINERIES,PRO03_MACHINERIES,SYS10_TRANSLATIONS where "+
          "SCH13_CALL_OUT_MACHINERIES.COMPANY_CODE_SYS01=PRO03_MACHINERIES.COMPANY_CODE_SYS01 and "+
          "SCH13_CALL_OUT_MACHINERIES.MACHINERY_CODE_PRO03=PRO03_MACHINERIES.MACHINERY_CODE and "+
          "PRO03_MACHINERIES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "SCH13_CALL_OUT_MACHINERIES.COMPANY_CODE_SYS01=? and "+
          "SCH13_CALL_OUT_MACHINERIES.CALL_OUT_CODE_SCH10=?";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SCH13","SCH13_CALL_OUT_MACHINERIES.COMPANY_CODE_SYS01");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("callOutCodeSch10SCH13","SCH13_CALL_OUT_MACHINERIES.CALL_OUT_CODE_SCH10");
      attribute2dbField.put("machineryCodePro03SCH13","SCH13_CALL_OUT_MACHINERIES.MACHINERY_CODE_PRO03");

      GridParams gridParams = (GridParams)inputPar;
      CallOutPK pk = (CallOutPK)gridParams.getOtherGridParams().get(ApplicationConsts.CALL_OUT_PK);

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(pk.getCompanyCodeSys01SCH10());
      values.add(pk.getCallOutCodeSCH10());


      // read from SCH13 table...
      Response answer = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          CallOutMachineryVO.class,
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching call-out machineries list",ex);
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
