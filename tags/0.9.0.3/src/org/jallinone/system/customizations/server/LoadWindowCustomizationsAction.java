package org.jallinone.system.customizations.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import org.jallinone.system.customizations.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.openswing.swing.message.send.java.GridParams;
import java.math.BigDecimal;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch grid data for the custumizations window.</p>
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
public class LoadWindowCustomizationsAction implements Action {


  public LoadWindowCustomizationsAction() {
  }


  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadWindowCustomizations";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    Connection conn = null;
    Statement stmt = null;
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();

    try {
      GridParams pars = (GridParams)inputPar;

      if (pars.getOtherGridParams().size()==0)
        return new VOListResponse(new ArrayList(),false,0);

      BigDecimal progressiveSYS13 = (BigDecimal)pars.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_SYS13);
      String tableNameSYS13 = (String)pars.getOtherGridParams().get(ApplicationConsts.TABLE_NAME_SYS13);

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
          "select SYS12_WINDOW_CUSTOMIZATIONS.PROGRESSIVE_SYS13,SYS12_WINDOW_CUSTOMIZATIONS.COLUMN_NAME,SYS12_WINDOW_CUSTOMIZATIONS.COLUMN_TYPE,SYS12_WINDOW_CUSTOMIZATIONS.COLUMN_SIZE,SYS12_WINDOW_CUSTOMIZATIONS.COLUMN_DEC,SYS12_WINDOW_CUSTOMIZATIONS.PROGRESSIVE_SYS10,SYS12_WINDOW_CUSTOMIZATIONS.ATTRIBUTE_NAME,SYS10_TRANSLATIONS.DESCRIPTION from "+
          "SYS12_WINDOW_CUSTOMIZATIONS,SYS10_TRANSLATIONS where "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE='"+serverLanguageId+"' and "+
          "SYS12_WINDOW_CUSTOMIZATIONS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS12_WINDOW_CUSTOMIZATIONS.PROGRESSIVE_SYS13="+progressiveSYS13+" order by SYS12_WINDOW_CUSTOMIZATIONS.PROGRESSIVE_SYS10"
      );
      WindowCustomizationVO vo = null;
      ArrayList list = new ArrayList();

      while(rset.next()) {
        vo = new WindowCustomizationVO();
        vo.setProgressiveSys13SYS12(rset.getBigDecimal(1));
        vo.setColumnNameSYS12(rset.getString(2));
        vo.setColumnTypeSYS12(rset.getString(3));
        vo.setColumnSizeSYS12(rset.getBigDecimal(4));
        vo.setColumnDecSYS12(rset.getBigDecimal(5));
        vo.setProgressiveSys10SYS12(rset.getBigDecimal(6));
        vo.setAttributeNameSYS12(rset.getString(7));
        vo.setDescriptionSYS10(rset.getString(8));
        vo.setTableNameSYS13(tableNameSYS13);
        list.add(vo);
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
    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching window customizations",ex);
      return new ErrorResponse(ex.getMessage());
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
  }



}
