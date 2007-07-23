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
import org.openswing.swing.mdi.java.ApplicationFunction;
import org.jallinone.hierarchies.java.HierarchyLevelVO;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.system.permissions.java.RoleFunctionVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to retrieve functions related to the specified role.</p>
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
public class LoadRoleFunctionsAction implements Action {


  public LoadRoleFunctionsAction() {
  }


  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadRoleFunctions";
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
      BigDecimal progressiveSYS04 = (BigDecimal)params.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_SYS04);
      BigDecimal progressiveHIE01 = (BigDecimal)params.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_HIE01);

      // retrieve all subnodes of the specified node...
      pstmt = conn.prepareStatement(
          "select HIE01_LEVELS.PROGRESSIVE,HIE01_LEVELS.PROGRESSIVE_HIE01,HIE01_LEVELS.LEVEL from HIE01_LEVELS "+
          "where ENABLED='Y' and PROGRESSIVE_HIE02=2 and PROGRESSIVE>=? "+
          "order by LEVEL,PROGRESSIVE_HIE01,PROGRESSIVE"
      );
      pstmt.setBigDecimal(1,progressiveHIE01);
      ResultSet rset = pstmt.executeQuery();

      HashSet currentLevelNodes = new HashSet();
      HashSet newLevelNodes = new HashSet();
      String nodes = "";
      int currentLevel = -1;
      while(rset.next()) {
        if (currentLevel!=rset.getInt(3)) {
          // next level...
          currentLevel = rset.getInt(3);
          currentLevelNodes = newLevelNodes;
          newLevelNodes = new HashSet();
        }
        if (rset.getBigDecimal(1).equals(progressiveHIE01)) {
          newLevelNodes.add(rset.getBigDecimal(1));
          nodes += rset.getBigDecimal(1)+",";
        }
        else if (currentLevelNodes.contains(rset.getBigDecimal(2))) {
          newLevelNodes.add(rset.getBigDecimal(1));
          nodes += rset.getBigDecimal(1)+",";
        }
      }
      rset.close();
      pstmt.close();
      if (nodes.length()>0)
        nodes = nodes.substring(0,nodes.length()-1);

      // retrieve all USER functions + functions associated to the specified role...
      String roles = "";
      Enumeration en = ((JAIOUserSessionParameters)userSessionPars).getUserRoles().keys();
      while(en.hasMoreElements()) {
        roles += en.nextElement()+",";
      }
      if (roles.length()>0)
        roles = roles.substring(0,roles.length()-1);

      RoleFunctionVO vo = null;
      ArrayList list = new ArrayList();
      HashSet functions = new HashSet();
      pstmt = conn.prepareStatement(
          "select SYS06_FUNCTIONS.FUNCTION_CODE,SYS10_TRANSLATIONS.DESCRIPTION,SYS07_ROLE_FUNCTIONS.CAN_INS,SYS07_ROLE_FUNCTIONS.CAN_UPD,SYS07_ROLE_FUNCTIONS.CAN_DEL,SYS06_FUNCTIONS.USE_COMPANY_CODE,SYS07_ROLE_FUNCTIONS.PROGRESSIVE_SYS04 "+
          "from SYS06_FUNCTIONS,SYS07_ROLE_FUNCTIONS,SYS10_TRANSLATIONS,SYS18_FUNCTION_LINKS where "+
          "SYS07_ROLE_FUNCTIONS.PROGRESSIVE_SYS04 in ("+roles+") and "+
          "SYS07_ROLE_FUNCTIONS.FUNCTION_CODE_SYS06=SYS06_FUNCTIONS.FUNCTION_CODE and "+
          "SYS06_FUNCTIONS.FUNCTION_CODE=SYS18_FUNCTION_LINKS.FUNCTION_CODE_SYS06 and "+
          "SYS18_FUNCTION_LINKS.PROGRESSIVE_HIE01 in ("+nodes+") and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE='"+langId+"' and "+
          "SYS10_TRANSLATIONS.PROGRESSIVE=SYS06_FUNCTIONS.PROGRESSIVE_SYS10"
      );
      rset = pstmt.executeQuery();
      while(rset.next()) {
        vo = new RoleFunctionVO();
        if (rset.getBigDecimal(7).equals(progressiveSYS04)) {
          vo.setCanDelSYS07(Boolean.valueOf(rset.getString(5).equals("Y")));
          vo.setCanUpdSYS07(Boolean.valueOf(rset.getString(4).equals("Y")));
          vo.setCanInsSYS07(Boolean.valueOf(rset.getString(3).equals("Y")));
          vo.setCanView(Boolean.TRUE);
          if (functions.contains(rset.getString(1))) {
            functions.remove(rset.getString(1));
            for(int i=0;i<list.size();i++) {
              if (((RoleFunctionVO)list.get(i)).getFunctionCodeSys06SYS07().equals(rset.getString(1))) {
                list.remove(i);
                break;
              }
            }
          }
        }
        else {
          vo.setCanDelSYS07(Boolean.FALSE);
          vo.setCanUpdSYS07(Boolean.FALSE);
          vo.setCanInsSYS07(Boolean.FALSE);
          vo.setCanView(Boolean.FALSE);
        }
        vo.setDescriptionSYS10(rset.getString(2));
        vo.setFunctionCodeSys06SYS07(rset.getString(1));
        vo.setProgressiveSys04SYS07(progressiveSYS04);
        vo.setUseCompanyCodeSYS06(Boolean.valueOf(rset.getString(6).equals("Y")));
        if (!functions.contains(rset.getString(1))) {
          functions.add(rset.getString(1));
          list.add(vo);
        }
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
