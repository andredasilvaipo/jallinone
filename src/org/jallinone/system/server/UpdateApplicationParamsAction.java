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
import org.jallinone.system.java.ApplicationParametersVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to store application parameters in SYS11 table.</p>
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
public class UpdateApplicationParamsAction implements Action {

  public UpdateApplicationParamsAction() {
  }


  /**
   * @return request name
   */
  public final String getRequestName() {
    return "updateApplicationParams";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters ApplicationSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession ApplicationSession,ServletContext context) {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      conn = ConnectionManager.getConnection(context);

      ValueObject[] vos = (ValueObject[])inputPar;
      ApplicationParametersVO vo = (ApplicationParametersVO)vos[1];

      // update receipts management program path...
      pstmt = conn.prepareStatement("update SYS11_APPLICATION_PARS set VALUE=? where PARAM_CODE=?");
      pstmt.setString(1,vo.getImagePath());
      pstmt.setString(2,ApplicationConsts.IMAGE_PATH);
      int rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS11_APPLICATION_PARS(PARAM_CODE,VALUE) values(?,?)");
        pstmt.setString(1,ApplicationConsts.IMAGE_PATH);
        pstmt.setString(2,vo.getImagePath());
        pstmt.executeUpdate();
      }

      conn.commit();

      return new VOResponse(vo);
    } catch (Exception ex) {
      try {
        conn.rollback();
      }
      catch (SQLException ex1) {
      }
      Logger.error(ApplicationSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while storing application parameters",ex);
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
