package org.jallinone.warehouse.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.warehouse.java.WarehouseVO;
import org.jallinone.system.server.JAIOUserSessionParameters;
import java.math.BigDecimal;
import org.jallinone.system.progressives.server.ProgressiveUtils;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to update an existing warehouse.</p>
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
public class UpdateWarehouseAction implements Action {


  public UpdateWarehouseAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "updateWarehouse";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    Connection conn = null;
    Statement stmt = null;
    try {
      conn = ConnectionManager.getConnection(context);

      WarehouseVO oldVO = (WarehouseVO)((ValueObject[])inputPar)[0];
      WarehouseVO newVO = (WarehouseVO)((ValueObject[])inputPar)[1];

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01WAR01","COMPANY_CODE_SYS01");
      attribute2dbField.put("warehouseCodeWAR01","WAREHOUSE_CODE");
      attribute2dbField.put("descriptionWAR01","DESCRIPTION");
      attribute2dbField.put("progressiveHie02WAR01","PROGRESSIVE_HIE02");
      attribute2dbField.put("addressWAR01","ADDRESS");
      attribute2dbField.put("zipWAR01","ZIP");
      attribute2dbField.put("cityWAR01","CITY");
      attribute2dbField.put("provinceWAR01","PROVINCE");
      attribute2dbField.put("countryWAR01","COUNTRY");
      attribute2dbField.put("progressiveSys04WAR01","PROGRESSIVE_SYS04");

      HashSet pkAttributes = new HashSet();
      pkAttributes.add("companyCodeSys01WAR01");
      pkAttributes.add("warehouseCodeWAR01");

      // update WAR01 table...
      Response res = CustomizeQueryUtil.updateTable(
          conn,
          userSessionPars,
          pkAttributes,
          oldVO,
          newVO,
          "WAR01_WAREHOUSES",
          attribute2dbField,
          "Y",
          "N",
          context,
          true,
          new BigDecimal(462) // window identifier...
      );

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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while updating an existing warehouse",ex);
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
