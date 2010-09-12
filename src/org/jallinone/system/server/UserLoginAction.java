package org.jallinone.system.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import java.math.BigDecimal;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to execute the user authentication: it returns the language identifier associated to the user.</p>
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
public class UserLoginAction extends LoginAction {


  public UserLoginAction() {
  }


  /**
   * Login operation.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    PreparedStatement pstmt = null;
    Statement stmt = null;
    PreparedStatement pstmt2 = null;
    Connection conn = null;
    try {
      String username = ((String[])inputPar)[0];
      String password = ((String[])inputPar)[1];

      conn = ConnectionManager.getConnection(context);
      pstmt = conn.prepareStatement(
          "select LANGUAGE_CODE_SYS09,PASSWD_EXPIRATION,COMPANY_CODE_SYS01,PROGRESSIVE_REG04 from SYS03_USERS where "+
          "USERNAME=? and PASSWD=?"
      );
      pstmt.setString(1,username.toUpperCase());
      pstmt.setString(2,password);
      ResultSet rset = pstmt.executeQuery();
      String serverLanguageId = null;
      String companyCodeSys01SYS03 = null;
      BigDecimal progressiveReg04SYS03 = null;
      if (rset.next()) {

        // verify date expiration...
        java.sql.Date expDate = rset.getDate(2);
        if (expDate!=null && expDate.compareTo(new java.sql.Date(System.currentTimeMillis()))<=0)
          return new ErrorResponse("Account Expired");

        serverLanguageId = rset.getString(1);
        companyCodeSys01SYS03 = rset.getString(3);
        progressiveReg04SYS03 = rset.getBigDecimal(4);

        stmt = conn.createStatement();
        ResultSet rset2 = stmt.executeQuery(
            "select CLIENT_LANGUAGE_CODE from SYS09_LANGUAGES where LANGUAGE_CODE='"+serverLanguageId+"'"
        );
        rset2.next();
        String languageId = rset2.getString(1);
        rset2.close();

        // if progressiveReg04SYS03 is not null, then retrieve employee data...
        String name_1 = null;
        String name_2 = null;
        String empCode = null;
        if (progressiveReg04SYS03!=null) {
          pstmt2 = conn.prepareStatement(
              "select REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,SCH01_EMPLOYEES.EMPLOYEE_CODE from REG04_SUBJECTS,SCH01_EMPLOYEES where "+
              "SCH01_EMPLOYEES.COMPANY_CODE_SYS01=? and "+
              "SCH01_EMPLOYEES.PROGRESSIVE_REG04=? and "+
              "SCH01_EMPLOYEES.COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 and "+
              "SCH01_EMPLOYEES.PROGRESSIVE_REG04=REG04_SUBJECTS.PROGRESSIVE"
          );
          pstmt2.setString(1,companyCodeSys01SYS03);
          pstmt2.setBigDecimal(2,progressiveReg04SYS03);
          rset2 = pstmt2.executeQuery();
          rset2.next();
          name_1 = rset2.getString(1);
          name_2 = rset2.getString(2);
          empCode = rset2.getString(3);
          rset2.close();
        }


        TextResponse tr = new TextResponse(languageId);
        SessionIdGenerator gen = (SessionIdGenerator)context.getAttribute(Controller.SESSION_ID_GENERATOR);
        tr.setSessionId(gen.getSessionId(request,response,userSession,context));

        Hashtable userSessions = (Hashtable)context.getAttribute(Controller.USER_SESSIONS);
        HashSet authenticatedIds = (HashSet)context.getAttribute(Controller.SESSION_IDS);
        if (userSessionPars!=null) {
          userSessions.remove(userSessionPars.getSessionId());
          authenticatedIds.remove(userSessionPars.getSessionId());
        }
        userSessionPars = new JAIOUserSessionParameters();
        userSessionPars.setSessionId(tr.getSessionId());
        userSessionPars.setUsername(username);
        userSessions.put(tr.getSessionId(),userSessionPars);
        userSessionPars.setLanguageId(languageId);
        ((JAIOUserSessionParameters)userSessionPars).setServerLanguageId(serverLanguageId);
        ((JAIOUserSessionParameters)userSessionPars).setProgressiveReg04SYS03(progressiveReg04SYS03);
        ((JAIOUserSessionParameters)userSessionPars).setName_1(name_1);
        ((JAIOUserSessionParameters)userSessionPars).setName_2(name_2);
        ((JAIOUserSessionParameters)userSessionPars).setEmployeeCode(empCode);
        ((JAIOUserSessionParameters)userSessionPars).setCompanyCodeSys01SYS03(companyCodeSys01SYS03);

        authenticatedIds.add(tr.getSessionId());
        return tr;
      }
      else
        return new ErrorResponse("Account not valid");
    } catch (Exception ex1) {
      ex1.printStackTrace();
      return new ErrorResponse(ex1.getMessage());
    } finally {
      try {
        pstmt.close();
      }
      catch (Exception ex) {
      }
      try {
        stmt.close();
      }
      catch (Exception ex) {
      }
      try {
        pstmt2.close();
      }
      catch (Exception ex) {
      }
      try {
        ConnectionManager.releaseConnection(conn,context);
      }
      catch (Exception ex2) {
      }
    }

  }


}
