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
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch call-out requests from SCH03 table.</p>
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
public class LoadCallOutRequestsAction implements Action {


  public LoadCallOutRequestsAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadCallOutRequests";
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

      GridParams pars = (GridParams)inputPar;

      // retrieve companies list...
      ArrayList companiesList = ((JAIOUserSessionParameters)userSessionPars).getCompanyBa().getCompaniesList("SCH03");
      String companies = "";
      for(int i=0;i<companiesList.size();i++)
        companies += "'"+companiesList.get(i).toString()+"',";
      companies = companies.substring(0,companies.length()-1);

      String sql =
          "select SCH03_CALL_OUT_REQUESTS.COMPANY_CODE_SYS01,SCH03_CALL_OUT_REQUESTS.REQUEST_YEAR,SCH03_CALL_OUT_REQUESTS.PROGRESSIVE,"+
          "SCH03_CALL_OUT_REQUESTS.DESCRIPTION,SCH03_CALL_OUT_REQUESTS.CALL_OUT_CODE_SCH10,SYS10_TRANSLATIONS.DESCRIPTION,"+
          "SCH03_CALL_OUT_REQUESTS.CALL_OUT_STATE,SCH03_CALL_OUT_REQUESTS.USERNAME_SYS03,"+
          "REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,SCH03_CALL_OUT_REQUESTS.PRIORITY,SCH03_CALL_OUT_REQUESTS.REQUEST_DATE "+
          " from SCH03_CALL_OUT_REQUESTS,SCH10_CALL_OUTS,SYS10_TRANSLATIONS,REG04_SUBJECTS where "+
          "SCH03_CALL_OUT_REQUESTS.CALL_OUT_CODE_SCH10=SCH10_CALL_OUTS.CALL_OUT_CODE and "+
          "SCH10_CALL_OUTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "SCH03_CALL_OUT_REQUESTS.COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 and "+
          "SCH03_CALL_OUT_REQUESTS.PROGRESSIVE_REG04=REG04_SUBJECTS.PROGRESSIVE and "+
          "SCH03_CALL_OUT_REQUESTS.COMPANY_CODE_SYS01 in ("+companies+") ";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SCH03","SCH03_CALL_OUT_REQUESTS.COMPANY_CODE_SYS01");
      attribute2dbField.put("requestYearSCH03","SCH03_CALL_OUT_REQUESTS.REQUEST_YEAR");
      attribute2dbField.put("progressiveSCH03","SCH03_CALL_OUT_REQUESTS.PROGRESSIVE");
      attribute2dbField.put("descriptionSCH03","SCH03_CALL_OUT_REQUESTS.DESCRIPTION");
      attribute2dbField.put("callOutCodeSch10SCH03","SCH03_CALL_OUT_REQUESTS.CALL_OUT_CODE_SCH10");
      attribute2dbField.put("callOutDescriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("callOutStateSCH03","SCH03_CALL_OUT_REQUESTS.CALL_OUT_STATE");
      attribute2dbField.put("usernameSys03SCH03","SCH03_CALL_OUT_REQUESTS.USERNAME_SYS03");
      attribute2dbField.put("name_1REG04","REG04_SUBJECTS.NAME_1");
      attribute2dbField.put("name_2REG04","REG04_SUBJECTS.NAME_2");
      attribute2dbField.put("prioritySCH03","SCH03_CALL_OUT_REQUESTS.PRIORITY");
      attribute2dbField.put("requestDateSCH03","SCH03_CALL_OUT_REQUESTS.REQUEST_DATE");


      ArrayList values = new ArrayList();
      values.add(serverLanguageId);

      // read from SCH03 table...
      Response answer = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          GridCallOutRequestVO.class,
          "Y",
          "N",
          context,
          pars,
          50,
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching call-out requests list",ex);
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
