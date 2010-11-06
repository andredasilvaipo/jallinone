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
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.system.permissions.java.JAIOApplicationFunctionVO;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to update an application function.</p>
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
public class UpdateFunctionAction implements Action {


  public UpdateFunctionAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "updateFunction";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();
    Connection conn = null;
    PreparedStatement pstmt = null;
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

      JAIOApplicationFunctionVO oldVO = ((JAIOApplicationFunctionVO[])inputPar)[0];
      JAIOApplicationFunctionVO newVO = ((JAIOApplicationFunctionVO[])inputPar)[1];
      Response res = null;

      if (!newVO.getDescription().equals(oldVO.getDescription())) {
        // description is changed...
        if (!newVO.isFolder())
          TranslationUtils.updateTranslation(oldVO.getDescription(),newVO.getDescription(),newVO.getProgressiveSys10SYS06(),serverLanguageId,conn);
        else
          TranslationUtils.updateTranslation(oldVO.getDescription(),newVO.getDescription(),newVO.getProgressiveHIE01(),serverLanguageId,conn);
      }


      if (newVO.isCopyNode()) {
        // the node must be duplicated...
        // the node must be moved...
        if (!newVO.isFolder() &&
            (!oldVO.getPosOrderSYS18().equals(newVO.getPosOrderSYS18()) ||
             !oldVO.getProgressiveHie01SYS18().equals(newVO.getProgressiveHie01SYS18()))) {
          // update function position...
          pstmt = conn.prepareStatement(
            "insert into SYS18_FUNCTION_LINKS(PROGRESSIVE_HIE01,POS_ORDER,FUNCTION_CODE_SYS06) values(?,?,?)"
          );
          pstmt.setBigDecimal(1,newVO.getProgressiveHie01SYS18());
          pstmt.setBigDecimal(2,newVO.getPosOrderSYS18());
          pstmt.setString(3,newVO.getFunctionId());
          pstmt.executeUpdate();
        }
      }
      else {
        // the node must be moved...
        if (!newVO.isFolder() &&
            (!oldVO.getPosOrderSYS18().equals(newVO.getPosOrderSYS18()) ||
             !oldVO.getProgressiveHie01SYS18().equals(newVO.getProgressiveHie01SYS18()))) {
          // update function position...
          pstmt = conn.prepareStatement(
            "update SYS18_FUNCTION_LINKS set PROGRESSIVE_HIE01=?,POS_ORDER=? where FUNCTION_CODE_SYS06=? and PROGRESSIVE_HIE01=? and POS_ORDER=?"
          );
          pstmt.setBigDecimal(1,newVO.getProgressiveHie01SYS18());
          pstmt.setBigDecimal(2,newVO.getPosOrderSYS18());
          pstmt.setString(3,newVO.getFunctionId());
          pstmt.setBigDecimal(4,oldVO.getProgressiveHie01SYS18());
          pstmt.setBigDecimal(5,oldVO.getPosOrderSYS18());
          int num = pstmt.executeUpdate();
          if (num==0) {
            conn.rollback();
            return new ErrorResponse("Updating not performed: the record was previously updated.");
          }
        }

      }

      Response answer = new VOResponse(newVO);

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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while updating an application function",ex);
      try {
        pstmt.close();
      }
      catch (Exception ex2) {
      }
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
