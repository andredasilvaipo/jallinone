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
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.hierarchies.java.HierarchyLevelVO;
import org.openswing.swing.message.send.java.LookupValidationParams;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to validate an call-out code from SCH10 table.</p>
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
public class ValidateCallOutCodeAction implements Action {


  public ValidateCallOutCodeAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "validateCallOutCode";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();

    PreparedStatement pstmt = null;
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

      LookupValidationParams pars = (LookupValidationParams)inputPar;

      String companyCodeSYS10 = (String)pars.getLookupValidationParameters().get(ApplicationConsts.COMPANY_CODE_SYS01);

      String sql =
          "select SCH10_CALL_OUTS.COMPANY_CODE_SYS01,SCH10_CALL_OUTS.CALL_OUT_CODE,A.DESCRIPTION,B.DESCRIPTION,"+
          "SCH10_CALL_OUTS.PROGRESSIVE_HIE02,SCH10_CALL_OUTS.PROGRESSIVE_HIE01,SCH10_CALL_OUTS.PROGRESSIVE_SYS10 "+
          " from SCH10_CALL_OUTS,SYS10_TRANSLATIONS A,SYS10_TRANSLATIONS B,HIE01_LEVELS where "+
          "SCH10_CALL_OUTS.PROGRESSIVE_SYS10=A.PROGRESSIVE and "+
          "A.LANGUAGE_CODE=? and "+
          "HIE01_LEVELS.PROGRESSIVE=SCH10_CALL_OUTS.PROGRESSIVE_HIE01 and "+
          "HIE01_LEVELS.PROGRESSIVE=B.PROGRESSIVE and B.LANGUAGE_CODE=? and "+
          "SCH10_CALL_OUTS.COMPANY_CODE_SYS01 ='"+companyCodeSYS10+"' and "+
          "SCH10_CALL_OUTS.CALL_OUT_CODE ='"+pars.getCode()+"' and "+
          "SCH10_CALL_OUTS.ENABLED='Y' ";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SCH10","SCH10_CALL_OUTS.COMPANY_CODE_SYS01");
      attribute2dbField.put("callOutCodeSCH10","SCH10_CALL_OUTS.CALL_OUT_CODE");
      attribute2dbField.put("descriptionSYS10","A.DESCRIPTION");
      attribute2dbField.put("progressiveHie02SCH10","SCH10_CALL_OUTS.PROGRESSIVE_HIE02");
      attribute2dbField.put("progressiveHie01SCH10","SCH10_CALL_OUTS.PROGRESSIVE_HIE01");
      attribute2dbField.put("progressiveSys10SCH10","SCH10_CALL_OUTS.PROGRESSIVE_SYS10");
      attribute2dbField.put("levelDescriptionSYS10","B.DESCRIPTION");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(serverLanguageId);

      if (pars.getLookupValidationParameters().get(ApplicationConsts.PROGRESSIVE_HIE02)!=null) {
        sql += " and SCH10_CALL_OUTS.PROGRESSIVE_HIE02=?";
        values.add(pars.getLookupValidationParameters().get(ApplicationConsts.PROGRESSIVE_HIE02));
      }

      // read from SCH10 table...
      Response answer = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          CallOutVO.class,
          "Y",
          "N",
          context,
          new GridParams(),
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while validating call-out code",ex);
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
