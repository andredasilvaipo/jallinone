package org.jallinone.system.permissions.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.sql.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.permissions.java.JAIOApplicationFunctionVO;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;
import org.openswing.swing.tree.java.OpenSwingTreeNode;
import org.openswing.swing.message.send.java.GridParams;
import java.math.BigDecimal;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.system.permissions.java.GridPermissionsPerRoleVO;
import org.openswing.swing.internationalization.server.ServerResourcesFactory;
import org.openswing.swing.internationalization.java.Resources;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to retrieve columns permissions for a spefic grid and role.</p>
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
public class LoadGridPermissionsPerRoleAction implements Action {


  public LoadGridPermissionsPerRoleAction() {
  }


  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadGridPermissionsPerRole";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String langId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();
    PreparedStatement pstmt = null;
    ResultSet rset = null;
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

      ServerResourcesFactory factory = (ServerResourcesFactory)context.getAttribute(Controller.RESOURCES_FACTORY);
      Resources resources = factory.getResources(userSessionPars.getLanguageId());

      GridParams gridParams = (GridParams)inputPar;
      BigDecimal progressiveSYS04 = (BigDecimal)gridParams.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_SYS04);
      String functionCodeSYS06 = (String)gridParams.getOtherGridParams().get(ApplicationConsts.FUNCTION_CODE_SYS06);

      String sql =
          "select SYS26_GRID_PERMISSIONS_DEFS.COLS_POS,SYS26_GRID_PERMISSIONS_DEFS.EDIT_COLS_IN_INS,"+
          "SYS26_GRID_PERMISSIONS_DEFS.EDIT_COLS_IN_EDIT,SYS26_GRID_PERMISSIONS_DEFS.REQUIRED_COLS,"+
          "SYS26_GRID_PERMISSIONS_DEFS.COLS_VIS,SYS26_GRID_PERMISSIONS_DEFS.COLS_NAME "+
          "from SYS26_GRID_PERMISSIONS_DEFS "+
          "where SYS26_GRID_PERMISSIONS_DEFS.FUNCTION_CODE_SYS06=? ";
      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1,functionCodeSYS06);
      rset = pstmt.executeQuery();
      ArrayList rows = new ArrayList();
      String[] colsPos = null;
      String[] editColsInIns = null;
      String[] editColsInEdit = null;
      String[] colsReq = null;
      String[] colsVis = null;
      String[] colsHeader = null;
      Hashtable cols = new Hashtable(); // collection of pairs: <attribute name,GridPermissionsPerRoleVO object>
      GridPermissionsPerRoleVO vo = null;
      while(rset.next()) {
        colsPos = rset.getString(1).split(",");
        editColsInIns = rset.getString(2).split(",");
        editColsInEdit = rset.getString(3).split(",");
        colsReq = rset.getString(4).split(",");
        colsVis = rset.getString(5).split(",");
        colsHeader = rset.getString(6).split(",");
        for(int i=0;i<colsPos.length;i++) {
          vo = new GridPermissionsPerRoleVO();
          vo.setColumnName(colsPos[i]);
          vo.setDescription(resources.getResource(colsHeader[i]));
          vo.setDefaultEditableInEdit(editColsInEdit[i].equals("true"));
          vo.setDefaultEditableInIns(editColsInIns[i].equals("true"));
          vo.setDefaultRequired(colsReq[i].equals("true"));
          vo.setDefaultVisible(colsVis[i].equals("true"));
          vo.setEditableInEdit(vo.isDefaultEditableInEdit());
          vo.setEditableInIns(vo.isDefaultEditableInIns());
          vo.setRequired(vo.isDefaultRequired());
          vo.setVisible(vo.isDefaultVisible());
          rows.add(vo);
          cols.put(vo.getColumnName(),vo);
        }
      }
      if (rows.size()==0)
        return new VOListResponse(new ArrayList(),false,0);

      rset.close();
      pstmt.close();
      sql =
          "select SYS27_GRID_PERMISSIONS.COLS_POS,SYS27_GRID_PERMISSIONS.EDIT_COLS_IN_INS,"+
          "SYS27_GRID_PERMISSIONS.EDIT_COLS_IN_EDIT,SYS27_GRID_PERMISSIONS.REQUIRED_COLS,"+
          "SYS27_GRID_PERMISSIONS.COLS_VIS "+
          "from SYS27_GRID_PERMISSIONS "+
          "where SYS27_GRID_PERMISSIONS.FUNCTION_CODE_SYS06=? and SYS27_GRID_PERMISSIONS.PROGRESSIVE_SYS04=?";
      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1,functionCodeSYS06);
      pstmt.setBigDecimal(2,progressiveSYS04);
      rset = pstmt.executeQuery();
      while(rset.next()) {
        colsPos = rset.getString(1).split(",");
        editColsInIns = rset.getString(2).split(",");
        editColsInEdit = rset.getString(3).split(",");
        colsReq = rset.getString(4).split(",");
        colsVis = rset.getString(5).split(",");

        for(int i=0;i<colsPos.length;i++) {
          vo.setColumnName(colsPos[i]);
          vo = (GridPermissionsPerRoleVO)cols.get(vo.getColumnName());
          //vo.setDescription("");
          vo.setEditableInEdit(editColsInEdit[i].equals("true"));
          vo.setEditableInIns(editColsInIns[i].equals("true"));
          vo.setRequired(colsReq[i].equals("true"));
          vo.setVisible(colsVis[i].equals("true"));
        }
      }
      VOListResponse answer = new VOListResponse(rows,false,rows.size());

      return answer;
    } catch (Exception ex1) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while retrieving columns permission",ex1);
      return new ErrorResponse(ex1.getMessage());
    } finally {
      try {
        rset.close();
      }
      catch (Exception ex) {
      }
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
