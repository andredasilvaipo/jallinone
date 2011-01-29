package org.jallinone.system.permissions.server;

import org.openswing.swing.server.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import javax.swing.tree.*;
import javax.swing.tree.*;
import java.sql.*;

import org.jallinone.system.server.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.system.permissions.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.openswing.swing.tree.java.*;


import org.jallinone.commons.server.JAIOBeanFactory;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to retrieve all application functions.</p>
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
public class LoadFunctionsAction implements Action {

  public LoadFunctionsAction() {
  }


  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadFunctions";
  }


  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    try {
      Response answer = loadFunctions(((JAIOUserSessionParameters)userSessionPars).getServerLanguageId(),userSessionPars.getUsername());

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
  public VOResponse loadFunctions(String langId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      conn = ConnectionManager.getConnection(null);

      // retrieve functions...
      Hashtable functions = new Hashtable();
      ArrayList functionsPerNode = null;
      JAIOApplicationFunctionVO f = null;
      HashSet functionsAdded = new HashSet();
      pstmt = conn.prepareStatement(
          "select SYS06_FUNCTIONS.FUNCTION_CODE,SYS06_FUNCTIONS.IMAGE_NAME,SYS06_FUNCTIONS.METHOD_NAME,"+
          "SYS18_FUNCTION_LINKS.PROGRESSIVE_HIE01,SYS10_TRANSLATIONS.DESCRIPTION,SYS18_FUNCTION_LINKS.POS_ORDER,SYS06_FUNCTIONS.PROGRESSIVE_SYS10 "+
          "from SYS06_FUNCTIONS,SYS10_TRANSLATIONS,SYS18_FUNCTION_LINKS "+
          "where "+
          "SYS18_FUNCTION_LINKS.FUNCTION_CODE_SYS06=SYS06_FUNCTIONS.FUNCTION_CODE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE='"+langId+"' and "+
          "SYS10_TRANSLATIONS.PROGRESSIVE=SYS06_FUNCTIONS.PROGRESSIVE_SYS10 order by "+
          "SYS18_FUNCTION_LINKS.PROGRESSIVE_HIE01,SYS18_FUNCTION_LINKS.POS_ORDER"
      );
      ResultSet rset = pstmt.executeQuery();
      while(rset.next()) {
        f = new JAIOApplicationFunctionVO(
          rset.getString(5),
          rset.getString(1),
          rset.getString(2),
          rset.getString(3),
          rset.getBigDecimal(4),
          rset.getBigDecimal(6),
          rset.getBigDecimal(7)
        );

        functionsPerNode = (ArrayList)functions.get(new Integer(rset.getInt(4)));
        if (functionsPerNode==null) {
          functionsPerNode = new ArrayList();
          functions.put(new Integer(rset.getInt(4)),functionsPerNode);
        }

        if (!functionsAdded.contains(new Integer(rset.getInt(4))+"-"+f.getFunctionId())) {
          functionsAdded.add(new Integer(rset.getInt(4))+"-"+f.getFunctionId());
          functionsPerNode.add(f);
        }

      }
      pstmt.close();


      // retrieve the whole tree...
      DefaultTreeModel model = null;
      pstmt = conn.prepareStatement(
          "select HIE01_LEVELS.PROGRESSIVE,HIE01_LEVELS.PROGRESSIVE_HIE01,HIE01_LEVELS.LEV,SYS10_TRANSLATIONS.DESCRIPTION "+
          "from HIE01_LEVELS,SYS10_TRANSLATIONS "+
          "where HIE01_LEVELS.PROGRESSIVE = SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE='"+langId+"' and ENABLED='Y' and PROGRESSIVE_HIE02=2 "+
          "order by LEV,PROGRESSIVE_HIE01,PROGRESSIVE"
      );
      rset = pstmt.executeQuery();
      Hashtable currentLevelNodes = new Hashtable();
      Hashtable newLevelNodes = new Hashtable();
      int currentLevel = -1;
      JAIOApplicationFunctionVO currentVO = null;
      DefaultMutableTreeNode currentNode = null;
      DefaultMutableTreeNode parentNode = null;
      while(rset.next()) {
        if (currentLevel!=rset.getInt(3)) {
          // next level...
          currentLevel = rset.getInt(3);
          currentLevelNodes = newLevelNodes;
          newLevelNodes = new Hashtable();
        }

        if (currentLevel==0) {
          // prepare a tree model with the root node...
          currentVO = new JAIOApplicationFunctionVO();
          currentVO.setDescription("");
          currentNode = new OpenSwingTreeNode(currentVO);
          model = new DefaultTreeModel(currentNode);
        }
        else {
          currentVO = new JAIOApplicationFunctionVO(rset.getString(4),null,rset.getBigDecimal(1),rset.getBigDecimal(2));
          currentNode = new OpenSwingTreeNode(currentVO);

          parentNode = (DefaultMutableTreeNode)currentLevelNodes.get(new Integer(rset.getInt(2)));
          parentNode.add(currentNode);
        }

        newLevelNodes.put(new Integer(rset.getInt(1)),currentNode);

        // add functions to the node...
        functionsPerNode = (ArrayList)functions.get(new Integer(rset.getInt(1)));
        if (functionsPerNode!=null)
          for(int i=0;i<functionsPerNode.size();i++) {
            currentNode.add(new OpenSwingTreeNode(functionsPerNode.get(i)));
          }

      }

      return new VOResponse(model);
    } catch (Exception ex1) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while retrieving application functions",ex1);
      throw new Exception(ex1.getMessage());
    } finally {
      try {
        pstmt.close();
      }
      catch (Exception ex) {
      }
      try {
    	  ConnectionManager.releaseConnection(conn, null);
      } catch (Exception e) {
      }      
    }
  }


}

