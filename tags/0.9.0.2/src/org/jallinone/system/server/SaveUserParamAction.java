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
 * <p>Description: Action class used to store a user parameter, based on the specified COMPANY_CODE.</p>
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
public class SaveUserParamAction implements Action {

  public SaveUserParamAction() {
  }


  /**
   * @return request name
   */
  public final String getRequestName() {
    return "saveUserParam";
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
      String value = (String)params.get(ApplicationConsts.PARAM_VALUE);

      pstmt = conn.prepareStatement("update SYS19_USER_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and USERNAME_SYS03=? and PARAM_CODE=?");
      pstmt.setString(1,value);
      pstmt.setString(2,companyCode);
      pstmt.setString(3,userSessionPars.getUsername());
      pstmt.setString(4,paramCode);
      int rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS19_USER_PARAMS(COMPANY_CODE_SYS01,USERNAME_SYS03,PARAM_CODE,VALUE) values(?,?,?,?)");
        pstmt.setString(1,companyCode);
        pstmt.setString(2,userSessionPars.getUsername());
        pstmt.setString(3,paramCode);
        pstmt.setString(4,value);
        pstmt.executeUpdate();
      }
      conn.commit();

      return new VOResponse(Boolean.TRUE);
    } catch (Exception ex) {
      try {
        conn.rollback();
      }
      catch (SQLException ex1) {
      }
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while storing a user parameter",ex);
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
