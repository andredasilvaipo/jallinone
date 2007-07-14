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
import org.openswing.swing.server.QueryUtil;
import org.jallinone.system.permissions.java.*;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to update the modified users info.</p>
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
public class UpdateUsersAction implements Action {


  public UpdateUsersAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "updateUsers";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
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

      ArrayList oldVOs = ((ArrayList[])inputPar)[0];
      ArrayList newVOs = ((ArrayList[])inputPar)[1];
      UserVO oldVO = null;
      UserVO newVO = null;
      Response res = null;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SYS03","COMPANY_CODE_SYS01");
      attribute2dbField.put("createDateSYS03","CREATE_DATE");
      attribute2dbField.put("firstNameSYS03","FIRST_NAME");
      attribute2dbField.put("languageCodeSys09SYS03","LANGUAGE_CODE_SYS09");
      attribute2dbField.put("lastNameSYS03","LAST_NAME");
      attribute2dbField.put("passwdExpirationSYS03","PASSWD_EXPIRATION");
      attribute2dbField.put("passwdSYS03","PASSWD");
      attribute2dbField.put("progressiveReg04SYS03","PROGRESSIVE_REG04");
      attribute2dbField.put("usernameCreateSYS03","USERNAME_CREATE");
      attribute2dbField.put("usernameSYS03","USERNAME");

      HashSet pkAttrs = new HashSet();
      pkAttrs.add("usernameSYS03");

      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (UserVO)oldVOs.get(i);
        newVO = (UserVO)newVOs.get(i);

        QueryUtil.updateTable(
            conn,
            userSessionPars,
            pkAttrs,
            oldVO,
            newVO,
            "SYS03_USERS",
            attribute2dbField,
            "Y",
            "N",
            context,
            false
        );
      }

      Response answer = new VOListResponse(newVOs,false,newVOs.size());

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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while updating existing users info",ex);
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
