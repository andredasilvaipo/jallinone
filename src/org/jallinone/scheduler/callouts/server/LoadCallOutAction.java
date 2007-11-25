package org.jallinone.scheduler.callouts.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.scheduler.callouts.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.commons.server.CustomizeQueryUtil;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.system.java.ApplicationParametersVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch a specific call-out from SCH10 table.</p>
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
public class LoadCallOutAction implements Action {


  public LoadCallOutAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadCallOut";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();
    Connection conn = null;
    Statement stmt = null;
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

      CallOutPK pk = (CallOutPK)inputPar;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SCH10","SCH10_CALL_OUTS.COMPANY_CODE_SYS01");
      attribute2dbField.put("callOutCodeSCH10","SCH10_CALL_OUTS.CALL_OUT_CODE");
      attribute2dbField.put("descriptionSYS10","A.DESCRIPTION");
      attribute2dbField.put("levelDescriptionSYS10","B.DESCRIPTION");
      attribute2dbField.put("progressiveHie02SCH10","SCH10_CALL_OUTS.PROGRESSIVE_HIE02");
      attribute2dbField.put("progressiveHie01SCH10","SCH10_CALL_OUTS.PROGRESSIVE_HIE01");
      attribute2dbField.put("progressiveSys10SCH10","SCH10_CALL_OUTS.PROGRESSIVE_SYS10");
      attribute2dbField.put("enabledSCH10","SCH10_CALL_OUTS.ENABLED");

      HashSet pkAttributes = new HashSet();
      pkAttributes.add("companyCodeSys01SCH10");
      pkAttributes.add("callOutCodeSCH10");

      String baseSQL =
          "select "+
          "SCH10_CALL_OUTS.COMPANY_CODE_SYS01,SCH10_CALL_OUTS.CALL_OUT_CODE,A.DESCRIPTION,B.DESCRIPTION,"+
          "SCH10_CALL_OUTS.PROGRESSIVE_HIE02,SCH10_CALL_OUTS.PROGRESSIVE_HIE01,SCH10_CALL_OUTS.PROGRESSIVE_SYS10 "+
          " from SCH10_CALL_OUTS,SYS10_TRANSLATIONS A,SYS10_TRANSLATIONS B,HIE01_LEVELS "+
          " where "+
          "SCH10_CALL_OUTS.PROGRESSIVE_SYS10=A.PROGRESSIVE and A.LANGUAGE_CODE=? and "+
          "HIE01_LEVELS.PROGRESSIVE=SCH10_CALL_OUTS.PROGRESSIVE_HIE01 and "+
          "HIE01_LEVELS.PROGRESSIVE=B.PROGRESSIVE and B.LANGUAGE_CODE=? and "+
          "SCH10_CALL_OUTS.COMPANY_CODE_SYS01=? and "+
          "SCH10_CALL_OUTS.CALL_OUT_CODE=?";

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(serverLanguageId);
      values.add(pk.getCompanyCodeSys01SCH10());
      values.add(pk.getCallOutCodeSCH10());

      // read from SCH10 table...
      Response answer = CustomizeQueryUtil.getQuery(
          conn,
          userSessionPars,
          baseSQL,
          values,
          attribute2dbField,
          CallOutVO.class,
          "Y",
          "N",
          context,
          true,
          ApplicationConsts.ID_CALL_OUTS // window identifier...
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching an existing call-out",ex);
      try {
        conn.rollback();
      }
      catch (Exception ex3) {
      }
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        stmt.close();
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
