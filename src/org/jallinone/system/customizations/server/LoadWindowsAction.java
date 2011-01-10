package org.jallinone.system.customizations.server;

import org.openswing.swing.server.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;

import javax.swing.tree.*;
import javax.swing.tree.*;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.system.customizations.java.*;
import org.jallinone.system.server.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.openswing.swing.tree.java.*;


import org.jallinone.commons.server.JAIOBeanFactory;

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


  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    try {
      Response answer = loadWindows(inputPar,((JAIOUserSessionParameters)userSessionPars).getServerLanguageId(),userSessionPars.getUsername());
      return answer;
    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while processing request",ex);
      return new ErrorResponse(ex.getMessage());
    }
  }
  
  


  /**
   * Business logic to execute.
   */
  private VOResponse loadWindows(Object inputPar,String serverLanguageId,String username) throws Throwable {
    DefaultMutableTreeNode root = new OpenSwingTreeNode();
    DefaultTreeModel model = new DefaultTreeModel(root);
    Statement stmt = null;
    Connection conn = null;
    try {
      conn = ConnectionManager.getConnection(null);

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
          vo = new WindowVO();
          vo.setDescriptionSYS10(rset.getString(2));
          vo.setProgressiveSYS13(rset.getBigDecimal(1));
          vo.setTableNameSYS13(rset.getString(5));
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

      return new VOResponse(model);
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching customizable windows",ex);
      throw new Exception(ex.getMessage());
    }
    finally {
      try {
        stmt.close();
      }
      catch (Exception ex2) {
      }
      try {
    	  ConnectionManager.releaseConnection(conn, null);
      } catch (Exception e) {
      }
    }
  }


  
  
  
}

