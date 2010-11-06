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
import org.jallinone.system.permissions.java.*;
import org.openswing.swing.server.QueryUtil;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch users from SYS03 table.</p>
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
public class LoadUsersAction implements Action {


  public LoadUsersAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadUsers";
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
      String sql =
          "select SYS03_USERS.USERNAME,SYS03_USERS.PASSWD,SYS03_USERS.PASSWD_EXPIRATION,SYS03_USERS.LANGUAGE_CODE_SYS09,"+
          "SYS03_USERS.FIRST_NAME,SYS03_USERS.LAST_NAME,SYS03_USERS.COMPANY_CODE_SYS01,SYS03_USERS.PROGRESSIVE_REG04,"+
          "SYS03_USERS.USERNAME_CREATE,SYS03_USERS.CREATE_DATE,SCH01_EMPLOYEES.EMPLOYEE_CODE,SYS03_USERS.DEF_COMPANY_CODE_SYS01 "+
          "from SYS03_USERS LEFT OUTER JOIN "+
          "(select SCH01_EMPLOYEES.COMPANY_CODE_SYS01,SCH01_EMPLOYEES.PROGRESSIVE_REG04,SCH01_EMPLOYEES.EMPLOYEE_CODE "+
          "from SCH01_EMPLOYEES) SCH01_EMPLOYEES ON "+
          "SCH01_EMPLOYEES.COMPANY_CODE_SYS01=SYS03_USERS.COMPANY_CODE_SYS01 and "+
          "SCH01_EMPLOYEES.PROGRESSIVE_REG04=SYS03_USERS.PROGRESSIVE_REG04 ";
      GridParams pars = (GridParams)inputPar;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SYS03","SYS03_USERS.COMPANY_CODE_SYS01");
      attribute2dbField.put("createDateSYS03","SYS03_USERS.CREATE_DATE");
      attribute2dbField.put("firstNameSYS03","SYS03_USERS.FIRST_NAME");
      attribute2dbField.put("languageCodeSys09SYS03","SYS03_USERS.LANGUAGE_CODE_SYS09");
      attribute2dbField.put("lastNameSYS03","SYS03_USERS.LAST_NAME");
      attribute2dbField.put("passwdExpirationSYS03","SYS03_USERS.PASSWD_EXPIRATION");
      attribute2dbField.put("passwdSYS03","SYS03_USERS.PASSWD");
      attribute2dbField.put("progressiveReg04SYS03","SYS03_USERS.PROGRESSIVE_REG04");
      attribute2dbField.put("usernameCreateSYS03","SYS03_USERS.USERNAME_CREATE");
      attribute2dbField.put("usernameSYS03","SYS03_USERS.USERNAME");
      attribute2dbField.put("employeeCodeSCH01","SCH01_EMPLOYEES.EMPLOYEE_CODE");
      attribute2dbField.put("defCompanyCodeSys01SYS03","SYS03_USERS.DEF_COMPANY_CODE_SYS01");


      Response answer = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          new ArrayList(),
          attribute2dbField,
          UserVO.class,
          "Y",
          "N",
          context,
          pars,
          50,
          false
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching users list",ex);
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
