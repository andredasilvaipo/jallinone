package org.jallinone.system.server;

import org.openswing.swing.logger.server.*;
import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.internationalization.java.Language;
import org.jallinone.commons.java.ApplicationConsts;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to retrieve a user parameter, based on the specified COMPANY_CODE and username.
 * If no parameter is found at user level (SYS19 table), it will be retrieved at company level (SYS21 table).</p>
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
public class LoadUserParamAction implements Action {

  public LoadUserParamAction() {
  }


  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadUserParam";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      conn = ConnectionManager.getConnection(context);

      Map params = (Map)inputPar;
      String companyCode = (String)params.get(ApplicationConsts.COMPANY_CODE_SYS01);
      String paramCode = (String)params.get(ApplicationConsts.PARAM_CODE);

      // retrieve parameter at user+company level...
      pstmt = conn.prepareStatement("select VALUE from SYS19_USER_PARAMS where COMPANY_CODE_SYS01=? and USERNAME_SYS03=? and PARAM_CODE=?");
      pstmt.setString(1,companyCode);
      pstmt.setString(2,userSessionPars.getUsername());
      pstmt.setString(3,paramCode);
      ResultSet rset = pstmt.executeQuery();
      String value = null;
      boolean paramFound = false;
      while(rset.next()) {
        value = rset.getString(1);
        paramFound = true;
      }
      rset.close();

      if (!paramFound) {
        // retrieve parameter at company level...
        pstmt.close();
        pstmt = conn.prepareStatement("select VALUE from SYS21_COMPANY_PARAMS where COMPANY_CODE_SYS01=? and PARAM_CODE=?");
        pstmt.setString(1,companyCode);
        pstmt.setString(2,paramCode);
        rset = pstmt.executeQuery();
        while(rset.next()) {
          value = rset.getString(1);
        }
        rset.close();
      }

      return new VOResponse(value);
    } catch (Exception ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while loading a user parameter",ex);
      return new ErrorResponse(ex.getMessage());
    } finally {
      try {
        pstmt.close();
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
