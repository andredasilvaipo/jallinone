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
public class LoadCallOutRequestAction implements Action {


  public LoadCallOutRequestAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadCallOutRequest";
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

      CallOutRequestPK pk = (CallOutRequestPK)inputPar;

      HashSet pkAttributes = new HashSet();
      pkAttributes.add("companyCodeSys01SCH03");
      pkAttributes.add("requestYearSCH03");
      pkAttributes.add("progressiveSCH03");

      String sql =
          "select SCH03_CALL_OUT_REQUESTS.COMPANY_CODE_SYS01,SCH03_CALL_OUT_REQUESTS.REQUEST_YEAR,SCH03_CALL_OUT_REQUESTS.PROGRESSIVE,"+
          "SCH03_CALL_OUT_REQUESTS.DESCRIPTION,SCH03_CALL_OUT_REQUESTS.CALL_OUT_CODE_SCH10,SYS10_TRANSLATIONS.DESCRIPTION,"+
          "SCH03_CALL_OUT_REQUESTS.CALL_OUT_STATE,SCH03_CALL_OUT_REQUESTS.USERNAME_SYS03,SCH03_CALL_OUT_REQUESTS.SUBJECT_TYPE_REG04,"+
          "SCH03_CALL_OUT_REQUESTS.PRIORITY,SCH03_CALL_OUT_REQUESTS.REQUEST_DATE,SCH10_CALL_OUTS.PROGRESSIVE_HIE02,"+
          "SCH03_CALL_OUT_REQUESTS.NOTE,SCH03_CALL_OUT_REQUESTS.DOC_TYPE_DOC01,SCH03_CALL_OUT_REQUESTS.DOC_NUMBER_DOC01,"+
          "SCH03_CALL_OUT_REQUESTS.DOC_YEAR_DOC01,SCH03_CALL_OUT_REQUESTS.PROGRESSIVE_REG04,SCH03_CALL_OUT_REQUESTS.PROGRESSIVE_SCH06 "+
          " from SCH03_CALL_OUT_REQUESTS,SCH10_CALL_OUTS,SYS10_TRANSLATIONS where "+
          "SCH03_CALL_OUT_REQUESTS.CALL_OUT_CODE_SCH10=SCH10_CALL_OUTS.CALL_OUT_CODE and "+
          "SCH10_CALL_OUTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "SCH03_CALL_OUT_REQUESTS.COMPANY_CODE_SYS01=? and "+
          "SCH03_CALL_OUT_REQUESTS.REQUEST_YEAR=? and "+
          "SCH03_CALL_OUT_REQUESTS.PROGRESSIVE=? ";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SCH03","SCH03_CALL_OUT_REQUESTS.COMPANY_CODE_SYS01");
      attribute2dbField.put("requestYearSCH03","SCH03_CALL_OUT_REQUESTS.REQUEST_YEAR");
      attribute2dbField.put("progressiveSCH03","SCH03_CALL_OUT_REQUESTS.PROGRESSIVE");
      attribute2dbField.put("descriptionSCH03","SCH03_CALL_OUT_REQUESTS.DESCRIPTION");
      attribute2dbField.put("callOutCodeSch10SCH03","SCH03_CALL_OUT_REQUESTS.CALL_OUT_CODE_SCH10");
      attribute2dbField.put("callOutDescriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("callOutStateSCH03","SCH03_CALL_OUT_REQUESTS.CALL_OUT_STATE");
      attribute2dbField.put("usernameSys03SCH03","SCH03_CALL_OUT_REQUESTS.USERNAME_SYS03");
      attribute2dbField.put("prioritySCH03","SCH03_CALL_OUT_REQUESTS.PRIORITY");
      attribute2dbField.put("requestDateSCH03","SCH03_CALL_OUT_REQUESTS.REQUEST_DATE");
      attribute2dbField.put("noteSCH03","SCH03_CALL_OUT_REQUESTS.NOTE");
      attribute2dbField.put("docTypeDoc01SCH03","SCH03_CALL_OUT_REQUESTS.DOC_TYPE_DOC01");
      attribute2dbField.put("docNumberDoc01SCH03","SCH03_CALL_OUT_REQUESTS.DOC_NUMBER_DOC01");
      attribute2dbField.put("docYearDoc01SCH03","SCH03_CALL_OUT_REQUESTS.DOC_YEAR_DOC01");
      attribute2dbField.put("progressiveReg04SCH03","SCH03_CALL_OUT_REQUESTS.PROGRESSIVE_REG04");
      attribute2dbField.put("progressiveSch06SCH03","SCH03_CALL_OUT_REQUESTS.PROGRESSIVE_SCH06");
      attribute2dbField.put("subjectTypeReg04SCH03","SCH03_CALL_OUT_REQUESTS.SUBJECT_TYPE_REG04");
      attribute2dbField.put("progressiveHie02SCH10","SCH10_CALL_OUTS.PROGRESSIVE_HIE02");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(pk.getCompanyCodeSys01SCH03());
      values.add(pk.getRequestYearSCH03());
      values.add(pk.getProgressiveSCH03());

      // read from SCH03 table...
      Response answer = CustomizeQueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          DetailCallOutRequestVO.class,
          "Y",
          "N",
          context,
          true,
          ApplicationConsts.ID_CALL_OUT_REQUESTS // window identifier...
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching an existing call-out request",ex);
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
