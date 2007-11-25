package org.jallinone.sales.agents.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.sales.agents.java.*;
import java.math.BigDecimal;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.openswing.swing.internationalization.server.ServerResourcesFactory;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to insert new agents in SAL10 table.</p>
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
public class InsertAgentsAction implements Action {


  public InsertAgentsAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "insertAgents";
  }

  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,
                                       UserSessionParameters userSessionPars,
                                       HttpServletRequest request,
                                       HttpServletResponse response,
                                       HttpSession userSession,
                                       ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();
    Connection conn = null;

    PreparedStatement pstmt = null;
    try {
      // retrieve internationalization settings (Resources object)...
      ServerResourcesFactory factory = (ServerResourcesFactory)context.getAttribute(Controller.RESOURCES_FACTORY);

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

      AgentVO vo = null;

      ArrayList list = (ArrayList)inputPar;
      pstmt = conn.prepareStatement("select AGENT_CODE from SAL10_AGENTS where COMPANY_CODE_SYS01=? and AGENT_CODE=?");

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SAL10","COMPANY_CODE_SYS01");
      attribute2dbField.put("percentageSAL10","PERCENTAGE");
      attribute2dbField.put("progressiveReg04SAL10","PROGRESSIVE_REG04");
      attribute2dbField.put("agentCodeSAL10","AGENT_CODE");
      attribute2dbField.put("progressiveSys10SAL10","PROGRESSIVE_SYS10");

      Response res = null;
      for(int i=0;i<list.size();i++) {
        vo = (AgentVO)list.get(i);

        // check if there exists an agent with the same code...
        pstmt.setString(1,vo.getCompanyCodeSys01SAL10());
        pstmt.setString(2,vo.getAgentCodeSAL10());
        ResultSet rset = pstmt.executeQuery();
        if (rset.next()) {
          rset.close();
          conn.rollback();
          return new ErrorResponse(factory.getResources(serverLanguageId).getResource("an agent with the same code already exists."));
        }
        rset.close();

        // insert into SAL10...
        res = CustomizeQueryUtil.insertTable(
            conn,
            userSessionPars,
            vo,
            "SAL10_AGENTS",
            attribute2dbField,
            "Y",
            "N",
            context,
            true,
            new BigDecimal(342) // window identifier...
        );
          if (res.isError()) {
          conn.rollback();
          return res;
        }
      }


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
      Logger.error(userSessionPars.getUsername(), this.getClass().getName(),
                   "executeCommand", "Error while inserting new agents", ex);
      try {
        conn.rollback();
      }
      catch (Exception ex3) {
      }
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

