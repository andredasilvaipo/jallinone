package org.jallinone.system.customizations.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.customizations.java.WindowVO;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;
import org.jallinone.events.server.*;
import org.openswing.swing.tree.java.OpenSwingTreeNode;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch tree data for the windows tree grid frame.</p>
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
public class LoadWindowsAction implements Action {


  public LoadWindowsAction() {
  }


  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadWindows";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    DefaultMutableTreeNode root = new OpenSwingTreeNode();
    DefaultTreeModel model = new DefaultTreeModel(root);
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();

    Connection conn = null;
    Statement stmt = null;
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
      ResultSet rset = stmt.executeQuery(
          "select SYS13_WINDOWS.PROGRESSIVE,A.DESCRIPTION,B.DESCRIPTION,SYS06_FUNCTIONS.FUNCTION_CODE,SYS13_WINDOWS.TABLE_NAME from "+
          "SYS13_WINDOWS,SYS10_TRANSLATIONS A,SYS10_TRANSLATIONS B,SYS06_FUNCTIONS where "+
          "A.LANGUAGE_CODE='"+serverLanguageId+"' and B.LANGUAGE_CODE='"+serverLanguageId+"' and "+
          "SYS13_WINDOWS.PROGRESSIVE=A.PROGRESSIVE and "+
          "SYS06_FUNCTIONS.PROGRESSIVE_SYS10=B.PROGRESSIVE and "+
          "SYS13_WINDOWS.FUNCTION_CODE_SYS06=SYS06_FUNCTIONS.FUNCTION_CODE order by "+
          "B.DESCRIPTION,SYS13_WINDOWS.PROGRESSIVE"
      );
      WindowVO vo = null;
      String code = null;
      DefaultMutableTreeNode parentNode = null;
      while(rset.next()) {
        if (!rset.getString(4).equals(code)) {
          // new level 1 node...
          vo = new WindowVO();
          vo.setDescriptionSYS10(rset.getString(3));
          vo.setProgressiveSYS13(rset.getBigDecimal(1));
          vo.setTableNameSYS13(rset.getString(5));
          parentNode = new OpenSwingTreeNode(vo);
          root.add(parentNode);
          code = rset.getString(4);
          parentNode.add(new OpenSwingTreeNode(vo));
        }
        else {
          // new level 2 node...
          vo = new WindowVO();
          vo.setProgressiveSYS13(rset.getBigDecimal(1));
          vo.setDescriptionSYS10(rset.getString(2));
          vo.setTableNameSYS13(rset.getString(5));
          parentNode.add(new OpenSwingTreeNode(vo));
        }
      }

      rset.close();
    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching customizable windows",ex);
    }
    finally {
      try {
        stmt.close();
      }
      catch (Exception ex2) {
      }
      try {
        ConnectionManager.releaseConnection(conn, context);
      }
      catch (Exception ex1) {
      }
    }

    Response answer =  new VOResponse(model);

    try {
      // fires the GenericEvent.BEFORE_COMMIT event...
      EventsManager.getInstance().processEvent(new GenericEvent(
          this,
          getRequestName(),
          GenericEvent.BEFORE_COMMIT,
          (JAIOUserSessionParameters) userSessionPars,
          request,
          response,
          userSession,
          context,
          conn,
          inputPar,
          answer
          ));
    }
    catch (EventsManagerException ex3) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching customizable windows",ex3);
    }

    return answer;

  }



}
