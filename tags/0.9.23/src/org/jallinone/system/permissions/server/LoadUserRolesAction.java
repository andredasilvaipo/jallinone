package org.jallinone.system.permissions.server;

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
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.system.permissions.java.UserRoleVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to retrieve roles related to the specified user.</p>
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
public class LoadUserRolesAction implements Action {


  public LoadUserRolesAction() {
  }


  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadUserRoles";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String langId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();

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
      GridParams params = (GridParams)inputPar;

      // retrieve all (current) user roles...
      String roles = "";
      Enumeration en = ((JAIOUserSessionParameters)userSessionPars).getUserRoles().keys();
      while(en.hasMoreElements()) {
        roles += en.nextElement()+",";
      }
      if (roles.length()>0)
        roles = roles.substring(0,roles.length()-1);

      UserRoleVO vo = null;
      ArrayList list = new ArrayList();
      Hashtable rolesSet = new Hashtable();
      pstmt = conn.prepareStatement(
          "select SYS14_USER_ROLES.PROGRESSIVE_SYS04,SYS10_TRANSLATIONS.DESCRIPTION from "+
          "SYS14_USER_ROLES,SYS10_TRANSLATIONS,SYS04_ROLES where "+
          "SYS04_ROLES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "SYS14_USER_ROLES.PROGRESSIVE_SYS04=SYS04_ROLES.PROGRESSIVE and "+
          "SYS04_ROLES.ENABLED='Y' and "+
          "SYS04_ROLES.PROGRESSIVE in ("+roles+") and "+
          "SYS14_USER_ROLES.USERNAME_SYS03=?"
      );
      pstmt.setString(1,langId);
      pstmt.setString(2,userSessionPars.getUsername());
      ResultSet rset = pstmt.executeQuery();
      while(rset.next()) {
        vo = new UserRoleVO();
        vo.setDescriptionSYS10(rset.getString(2));
        vo.setProgressiveSys04SYS14(rset.getBigDecimal(1));
        vo.setUsernameSys03SYS14((String)params.getOtherGridParams().get(ApplicationConsts.USERNAME_SYS03));
        vo.setSelected(Boolean.FALSE);
        list.add(vo);
        rolesSet.put(rset.getBigDecimal(1),new Integer(list.size()-1));
      }
      rset.close();
      pstmt.close();

      // retrieve roles associated to the specified user that are ALSO roles of the current user...
      pstmt = conn.prepareStatement(
          "select SYS14_USER_ROLES.PROGRESSIVE_SYS04,SYS10_TRANSLATIONS.DESCRIPTION from "+
          "SYS14_USER_ROLES,SYS10_TRANSLATIONS,SYS04_ROLES where "+
          "SYS04_ROLES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "SYS14_USER_ROLES.PROGRESSIVE_SYS04=SYS04_ROLES.PROGRESSIVE and "+
          "SYS04_ROLES.ENABLED='Y' and "+
          "SYS04_ROLES.PROGRESSIVE in ("+roles+") and "+
          "SYS14_USER_ROLES.USERNAME_SYS03=?"
      );
      pstmt.setString(1,langId);
      pstmt.setString(2,(String)params.getOtherGridParams().get(ApplicationConsts.USERNAME_SYS03));
      rset = pstmt.executeQuery();
      while(rset.next()) {
        vo = (UserRoleVO)list.get(((Integer)rolesSet.get(rset.getBigDecimal(1))).intValue());
        vo.setSelected(Boolean.TRUE);
      }
      rset.close();

      Response answer = new VOListResponse(list,false,list.size());

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
