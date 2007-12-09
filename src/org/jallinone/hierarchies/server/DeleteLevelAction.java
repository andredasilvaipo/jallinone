package org.jallinone.hierarchies.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import java.math.BigDecimal;
import org.jallinone.system.customizations.java.WindowCustomizationVO;
import org.jallinone.system.translations.server.TranslationUtils;
import org.openswing.swing.internationalization.server.ServerResourcesFactory;
import org.openswing.swing.internationalization.java.Resources;
import org.jallinone.hierarchies.java.HierarchyLevelVO;
import org.jallinone.system.progressives.server.ProgressiveUtils;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to delete the level in the specified hierarchy.</p>
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
public class DeleteLevelAction implements Action {


  public DeleteLevelAction() {
  }


  /**
   * @return request name
   */
  public final String getRequestName() {
    return "deleteLevel";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    Connection conn = null;
    PreparedStatement pstmt = null;
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();

    try {
      HierarchyLevelVO vo = (HierarchyLevelVO)inputPar;

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


      // retrieve nodes to delete...
      pstmt = conn.prepareStatement(
          "select HIE01_LEVELS.PROGRESSIVE,HIE01_LEVELS.PROGRESSIVE_HIE01,HIE01_LEVELS.LEV from HIE01_LEVELS "+
          "where ENABLED='Y' and PROGRESSIVE_HIE02=? and PROGRESSIVE>=? "+
          "order by LEV,PROGRESSIVE_HIE01,PROGRESSIVE"
      );
      pstmt.setBigDecimal(1,vo.getProgressiveHie02HIE01());
      pstmt.setBigDecimal(2,vo.getProgressiveHIE01());
      ResultSet rset = pstmt.executeQuery();

      HashSet currentLevelNodes = new HashSet();
      HashSet newLevelNodes = new HashSet();
      ArrayList nodesToDelete = new ArrayList();
      int currentLevel = -1;
      while(rset.next()) {
        if (currentLevel!=rset.getInt(3)) {
          // next level...
          currentLevel = rset.getInt(3);
          currentLevelNodes = newLevelNodes;
          newLevelNodes = new HashSet();
        }
        if (rset.getBigDecimal(1).equals(vo.getProgressiveHIE01())) {
          newLevelNodes.add(rset.getBigDecimal(1));
          nodesToDelete.add(rset.getBigDecimal(1));
        }
        else if (currentLevelNodes.contains(rset.getBigDecimal(2))) {
          newLevelNodes.add(rset.getBigDecimal(1));
          nodesToDelete.add(rset.getBigDecimal(1));
        }
      }
      rset.close();
      pstmt.close();

      // logically delete (update...) records in HIE01...
      String in = "";
      for(int i=0;i<nodesToDelete.size();i++)
        in += nodesToDelete.get(i)+",";
      in = in.substring(0,in.length()-1);
      pstmt = conn.prepareStatement("update HIE01_LEVELS set ENABLED='N' where PROGRESSIVE in ("+in+")");
      pstmt.execute();

      Response answer = new VOResponse(new Boolean(true));

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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while deleting the level (and all sub-levels...) in the specified hierarchy",ex);
      try {
        conn.rollback();
      }
      catch (Exception ex3) {
      }
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        pstmt.close();
      }
      catch (Exception ex2) {
      }
      try {
        ConnectionManager.releaseConnection(conn, context);
      }
      catch (Exception ex1) {
      }
    }

  }



}
