package org.jallinone.hierarchies.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.openswing.swing.internationalization.java.*;
import java.sql.*;
import java.math.BigDecimal;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.hierarchies.java.HierarchyLevelVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to retrieve a hierarchy tree model from HIE01 table, based on the specified hierarchy identifier..</p>
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
public class LoadHierarchyAction implements Action {
  public LoadHierarchyAction() {
  }


  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadHierarchy";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String langId = ( (JAIOUserSessionParameters) userSessionPars).getServerLanguageId();

    PreparedStatement pstmt = null;
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


      Map treePars = (Map)inputPar;
      BigDecimal progressiveHIE02 = (BigDecimal)treePars.get(ApplicationConsts.PROGRESSIVE_HIE02);

      // retrieve the whole tree...
      DefaultTreeModel model = null;
      pstmt = conn.prepareStatement(
          "select HIE01_LEVELS.PROGRESSIVE,HIE01_LEVELS.PROGRESSIVE_HIE01,HIE01_LEVELS.LEVEL,SYS10_TRANSLATIONS.DESCRIPTION "+
          "from HIE01_LEVELS,SYS10_TRANSLATIONS "+
          "where HIE01_LEVELS.PROGRESSIVE = SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE='"+langId+"' and ENABLED='Y' and PROGRESSIVE_HIE02=? "+
          "order by LEVEL,PROGRESSIVE_HIE01,PROGRESSIVE"
      );
      pstmt.setBigDecimal(1,progressiveHIE02);
      ResultSet rset = pstmt.executeQuery();

      Hashtable currentLevelNodes = new Hashtable();
      Hashtable newLevelNodes = new Hashtable();
      int currentLevel = -1;
      DefaultMutableTreeNode currentNode = null;
      DefaultMutableTreeNode parentNode = null;
      HierarchyLevelVO vo = null;
      while(rset.next()) {
        if (currentLevel!=rset.getInt(3)) {
          // next level...
          currentLevel = rset.getInt(3);
          currentLevelNodes = newLevelNodes;
          newLevelNodes = new Hashtable();
        }

        if (currentLevel==0) {
          // prepare a tree model with the root node...
          vo = new HierarchyLevelVO();
          vo.setEnabledHIE01("Y");
          vo.setLevelHIE01(rset.getBigDecimal(3));
          vo.setProgressiveHIE01(rset.getBigDecimal(1));
          vo.setProgressiveHie01HIE01(rset.getBigDecimal(2));
          vo.setProgressiveHie02HIE01(progressiveHIE02);
          vo.setDescriptionSYS10(rset.getString(4));
          currentNode = new DefaultMutableTreeNode(vo);
          model = new DefaultTreeModel(currentNode);
        }
        else {
          vo = new HierarchyLevelVO();
          vo.setEnabledHIE01("Y");
          vo.setLevelHIE01(rset.getBigDecimal(3));
          vo.setProgressiveHIE01(rset.getBigDecimal(1));
          vo.setProgressiveHie01HIE01(rset.getBigDecimal(2));
          vo.setProgressiveHie02HIE01(progressiveHIE02);
          vo.setDescriptionSYS10(rset.getString(4));
          currentNode = new DefaultMutableTreeNode(vo);

          parentNode = (DefaultMutableTreeNode)currentLevelNodes.get(new Integer(rset.getInt(2)));
          parentNode.add(currentNode);
        }

        newLevelNodes.put(new Integer(rset.getInt(1)),currentNode);

      }
      rset.close();

      Response answer = new VOResponse(model);

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

      return answer;
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
        ConnectionManager.releaseConnection(conn,context);
      }
      catch (Exception ex2) {
      }
    }
  }


}
