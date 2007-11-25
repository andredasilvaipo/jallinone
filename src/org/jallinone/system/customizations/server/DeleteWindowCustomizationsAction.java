package org.jallinone.system.customizations.server;

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
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to delete the selected customized columns of the specified window.</p>
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
public class DeleteWindowCustomizationsAction implements Action {


  public DeleteWindowCustomizationsAction() {
  }


  /**
   * @return request name
   */
  public final String getRequestName() {
    return "deleteWindowCustomizations";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    Connection conn = null;
    Statement stmt = null;
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();

    try {
      // delete the specified customized columns...
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
      ArrayList rows = (ArrayList)inputPar;
      WindowCustomizationVO vo = null;
      for(int i=0;i<rows.size();i++) {
        vo = (WindowCustomizationVO)rows.get(i);
        // delete from SYS12...
        stmt.execute(
            "delete from SYS12_WINDOW_CUSTOMIZATIONS where "+
            "PROGRESSIVE_SYS13="+vo.getProgressiveSys13SYS12()+" and "+
            "COLUMN_NAME='"+vo.getColumnNameSYS12()+"'"
        );

        // delete from SYS10...
        TranslationUtils.deleteTranslations(
            vo.getProgressiveSys10SYS12(),
            conn
        );
      }

      // maybe phisically delete the field from the table...
      ResultSet rset = stmt.executeQuery(
        "select COLUMN_NAME from SYS12_WINDOW_CUSTOMIZATIONS,SYS13_WINDOWS where "+
        "SYS12_WINDOW_CUSTOMIZATIONS.PROGRESSIVE_SYS13=SYS13_WINDOWS.PROGRESSIVE and "+
        "SYS13_WINDOWS.TABLE_NAME='"+vo.getTableNameSYS13()+"' and "+
        "SYS12_WINDOW_CUSTOMIZATIONS.COLUMN_NAME='"+vo.getColumnNameSYS12()+"'"
      );
      boolean columnAlreadyExist = false;
      if(rset.next())
        columnAlreadyExist = true;
      rset.close();
      if (!columnAlreadyExist)
        stmt.execute("alter table "+vo.getTableNameSYS13()+" drop column "+vo.getColumnNameSYS12());

      Response answer = new VOListResponse(rows,false,rows.size());

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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while deleting customized columns",ex);
      try {
        conn.rollback();
      }
      catch (Exception ex3) {
      }
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
