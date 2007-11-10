package org.jallinone.system.permissions.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.system.permissions.java.RoleVO;
import org.openswing.swing.internationalization.server.ServerResourcesFactory;
import org.openswing.swing.internationalization.java.Resources;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to (logically) delete an existing role.</p>
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
public class DeleteRoleAction implements Action {


  public DeleteRoleAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "deleteRole";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    Connection conn = null;
    Statement stmt = null;
    PreparedStatement pstmt = null;

    // retrieve internationalization settings (Resources object)...
    ServerResourcesFactory factory = (ServerResourcesFactory)context.getAttribute(Controller.RESOURCES_FACTORY);
    Resources res = factory.getResources(userSessionPars.getLanguageId());

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
      stmt = conn.createStatement();

      RoleVO vo = (RoleVO)inputPar;

      // the role will be removed only if no user has that role (except the current user...)
      ResultSet rset = stmt.executeQuery("select USERNAME_SYS03 from SYS14_USER_ROLES where PROGRESSIVE_SYS04="+vo.getProgressiveSYS04());
      int count = 0;
      while(rset.next()) {
        if (!rset.getString(1).equals(userSessionPars.getUsername().toUpperCase()))
          count++;
      }
      rset.close();
      if (count>0) {
        return new ErrorResponse(res.getResource("there are users with the current role associated."));
      }
      else {
        // phisically remove that role from all user associations (from SYS14 table...)
        stmt.execute("delete from SYS14_USER_ROLES where PROGRESSIVE_SYS04="+vo.getProgressiveSYS04());

        // phisically remove companies-role relations from SYS02 table...
        stmt.execute("delete from SYS02_COMPANIES_ACCESS where PROGRESSIVE_SYS04="+vo.getProgressiveSYS04());

        // phisically remove functions-role associations from SYS07 table...
        stmt.execute("delete from SYS07_ROLE_FUNCTIONS where PROGRESSIVE_SYS04="+vo.getProgressiveSYS04());

        // logically delete the record in SYS09...
        stmt.execute("update SYS04_ROLES set ENABLED='N' where PROGRESSIVE="+vo.getProgressiveSYS04());
      }

      Response answer = new VOResponse(new Boolean(true));

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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while deleting an existing role",ex);
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
