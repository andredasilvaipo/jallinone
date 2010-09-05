package org.jallinone.system.customizations.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.jallinone.system.customizations.java.CustomColumnVO;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to update existing custom columns in SYS22 table.</p>
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
public class UpdateCustomColumnsAction implements Action {


  public UpdateCustomColumnsAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "updateCustomColumns";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
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

      ArrayList oldVOs = ((ArrayList[])inputPar)[0];
      ArrayList newVOs = ((ArrayList[])inputPar)[1];

      CustomColumnVO oldVO = null;
      CustomColumnVO newVO = null;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("functionCodeSys06SYS22","FUNCTION_CODE_SYS06");
      attribute2dbField.put("columnNameSYS22","COLUMN_NAME");
      attribute2dbField.put("columnTypeSYS22","COLUMN_TYPE");
      attribute2dbField.put("constraintValuesSYS22","CONSTRAINT_VALUES");
      attribute2dbField.put("columnVisibleSYS22","COLUMN_VISIBLE");
      attribute2dbField.put("defaultValueTextSYS22","DEFAULT_VALUE_TEXT");
      attribute2dbField.put("defaultValueDateSYS22","DEFAULT_VALUE_DATE");
      attribute2dbField.put("defaultValueNumSYS22","DEFAULT_VALUE_NUM");
      attribute2dbField.put("isParamSYS22","IS_PARAM");
      attribute2dbField.put("isParamRequiredSYS22","IS_PARAM_REQUIRED");

      HashSet pkAttrs = new HashSet();
      pkAttrs.add("functionCodeSys06SYS22");
      pkAttrs.add("columnNameSYS22");

      // update SYS22 table...
      Response res = null;
      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (CustomColumnVO)oldVOs.get(i);
        newVO = (CustomColumnVO)newVOs.get(i);
        res = QueryUtil.updateTable(
            conn,
            userSessionPars,
            pkAttrs,
            oldVO,
            newVO,
            "SYS22_CUSTOM_COLUMNS",
            attribute2dbField,
            "Y",
            "N",
            context,
            true
        );
        if (res.isError()) {
          conn.rollback();
          return res;
        }
      }

      Response answer = new VOListResponse(newVOs,false,newVOs.size());

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
      try {
        conn.rollback();
      }
      catch (Exception e) {

      }
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while updating existing custom columns",ex);
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        ConnectionManager.releaseConnection(conn, context);
      }
      catch (Exception ex1) {
      }
    }

  }



}
