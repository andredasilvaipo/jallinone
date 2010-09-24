package org.jallinone.system.permissions.server;


import java.math.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.jallinone.events.server.*;
import org.jallinone.system.permissions.java.*;
import org.jallinone.system.server.*;
import org.openswing.swing.logger.server.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.server.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to update a columns' permissions for a specific grid and role.</p>
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
public class UpdateGridPermissionsPerRoleAction implements Action {


  public UpdateGridPermissionsPerRoleAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "updateGridPermissionsPerRole";
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

      Object[] objs = (Object[])inputPar;
      String functionCodeSYS06 = (String)objs[0];
      BigDecimal progressiveSYS04 = (BigDecimal)objs[1];
      ArrayList vos = (ArrayList)objs[2];

      pstmt = conn.prepareStatement("delete from SYS27_GRID_PERMISSIONS where FUNCTION_CODE_SYS06=? and PROGRESSIVE_SYS04=?");
      pstmt.setString(1,functionCodeSYS06);
      pstmt.setBigDecimal(2,progressiveSYS04);
      pstmt.execute();
      pstmt.close();
      pstmt = conn.prepareStatement("insert into SYS27_GRID_PERMISSIONS(COLS_POS,EDIT_COLS_IN_INS,EDIT_COLS_IN_EDIT,REQUIRED_COLS,COLS_VIS,FUNCTION_CODE_SYS06,PROGRESSIVE_SYS04) values(?,?,?,?,?,?,?)");
      GridPermissionsPerRoleVO vo = null;
      String colsPos = "";
      String editColsInIns = "";
      String editColsInEdit = "";
      String colsReq = "";
      String colsVis = "";
      for(int i=0;i<vos.size();i++) {
        vo = (GridPermissionsPerRoleVO)vos.get(i);
        colsPos += vo.getColumnName()+",";
        editColsInIns += (vo.isEditableInIns()?"true":"false")+",";
        editColsInEdit += (vo.isEditableInEdit()?"true":"false")+",";
        colsReq += (vo.isRequired()?"true":"false")+",";
        colsVis += (vo.isVisible()?"true":"false")+",";
      }
      colsPos = colsPos.substring(0,colsPos.length()-1);
      editColsInIns = editColsInIns.substring(0,editColsInIns.length()-1);
      editColsInEdit = editColsInEdit.substring(0,editColsInEdit.length()-1);
      colsReq = colsReq.substring(0,colsReq.length()-1);
      colsVis = colsVis.substring(0,colsVis.length()-1);

      pstmt.setString(1,colsPos);
      pstmt.setString(2,editColsInIns);
      pstmt.setString(3,editColsInEdit);
      pstmt.setString(4,colsReq);
      pstmt.setString(5,colsVis);
      pstmt.setString(6,functionCodeSYS06);
      pstmt.setBigDecimal(7,progressiveSYS04);
      pstmt.execute();
      Response answer = new VOListResponse(vos,false,vos.size());

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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while updating columns permission",ex);
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
