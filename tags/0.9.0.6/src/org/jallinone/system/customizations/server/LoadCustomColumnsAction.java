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
 * <p>Description: Action class used to retrieve custom columns from SYS22 table.</p>
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
public class LoadCustomColumnsAction implements Action {


  public LoadCustomColumnsAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadCustomColumns";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
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

      String sql =
          "select SYS22_CUSTOM_COLUMNS.FUNCTION_CODE_SYS06,SYS22_CUSTOM_COLUMNS.COLUMN_NAME,SYS22_CUSTOM_COLUMNS.COLUMN_TYPE,"+
          "SYS22_CUSTOM_COLUMNS.CONSTRAINT_VALUES,SYS22_CUSTOM_COLUMNS.COLUMN_VISIBLE,SYS22_CUSTOM_COLUMNS.DEFAULT_VALUE_TEXT,"+
          "SYS22_CUSTOM_COLUMNS.DEFAULT_VALUE_DATE,SYS22_CUSTOM_COLUMNS.DEFAULT_VALUE_NUM,SYS22_CUSTOM_COLUMNS.IS_PARAM,"+
          "SYS22_CUSTOM_COLUMNS.IS_PARAM_REQUIRED from SYS22_CUSTOM_COLUMNS where "+
          "SYS22_CUSTOM_COLUMNS.FUNCTION_CODE_SYS06=? ";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("functionCodeSys06SYS22","SYS22_CUSTOM_COLUMNS.FUNCTION_CODE_SYS06");
      attribute2dbField.put("columnNameSYS22","SYS22_CUSTOM_COLUMNS.COLUMN_NAME");
      attribute2dbField.put("columnTypeSYS22","SYS22_CUSTOM_COLUMNS.COLUMN_TYPE");
      attribute2dbField.put("constraintValuesSYS22","SYS22_CUSTOM_COLUMNS.CONSTRAINT_VALUES");
      attribute2dbField.put("columnVisibleSYS22","SYS22_CUSTOM_COLUMNS.COLUMN_VISIBLE");
      attribute2dbField.put("defaultValueTextSYS22","SYS22_CUSTOM_COLUMNS.DEFAULT_VALUE_TEXT");
      attribute2dbField.put("defaultValueDateSYS22","SYS22_CUSTOM_COLUMNS.DEFAULT_VALUE_DATE");
      attribute2dbField.put("defaultValueNumSYS22","SYS22_CUSTOM_COLUMNS.DEFAULT_VALUE_NUM");
      attribute2dbField.put("isParamSYS22","SYS22_CUSTOM_COLUMNS.IS_PARAM");
      attribute2dbField.put("isParamRequiredSYS22","SYS22_CUSTOM_COLUMNS.IS_PARAM_REQUIRED");

      GridParams gridParams = (GridParams)inputPar;

      ArrayList values = new ArrayList();
      values.add(gridParams.getOtherGridParams().get(ApplicationConsts.FUNCTION_CODE_SYS06));

      // read from SYS22 table...
      Response res = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          CustomColumnVO.class,
          "Y",
          "N",
          context,
          gridParams,
          true
      );
      if (res.isError())
        return res;

      Response answer = res;

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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching custom columns list",ex);
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
