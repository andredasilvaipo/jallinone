package org.jallinone.warehouse.tables.movements.server;

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
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.warehouse.tables.movements.java.MovementVO;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch warehouse movements from WAR02 table.</p>
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
public class LoadMovementsAction implements Action {


  public LoadMovementsAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadWarehouseMovements";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();

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
          "select WAR04_WAREHOUSE_MOTIVES.QTY_SIGN,WAR04_WAREHOUSE_MOTIVES.ITEM_TYPE,WAR02_WAREHOUSE_MOVEMENTS.PROGRESSIVE,WAR02_WAREHOUSE_MOVEMENTS.COMPANY_CODE_SYS01,"+
          "WAR02_WAREHOUSE_MOVEMENTS.WAREHOUSE_CODE_WAR01,WAR02_WAREHOUSE_MOVEMENTS.ITEM_CODE_ITM01,WAR02_WAREHOUSE_MOVEMENTS.PROGRESSIVE_HIE01,"+
          "SYS10_LOC.DESCRIPTION,WAR02_WAREHOUSE_MOVEMENTS.MOVEMENT_DATE,WAR02_WAREHOUSE_MOVEMENTS.USERNAME,WAR02_WAREHOUSE_MOVEMENTS.NOTE,"+
          "WAR02_WAREHOUSE_MOVEMENTS.DELTA_QTY,WAR02_WAREHOUSE_MOVEMENTS.WAREHOUSE_MOTIVE_WAR04,SYS10_WAR04.DESCRIPTION,SYS10_ITM01.DESCRIPTION,"+
          "WAR01_WAREHOUSES.DESCRIPTION from "+
          "WAR04_WAREHOUSE_MOTIVES,WAR02_WAREHOUSE_MOVEMENTS,SYS10_TRANSLATIONS SYS10_LOC,SYS10_TRANSLATIONS SYS10_WAR04,"+
          "SYS10_TRANSLATIONS SYS10_ITM01,WAR01_WAREHOUSES,ITM01_ITEMS where "+
          "WAR02_WAREHOUSE_MOVEMENTS.WAREHOUSE_MOTIVE_WAR04=WAR04_WAREHOUSE_MOTIVES.WAREHOUSE_MOTIVE and "+
          "WAR04_WAREHOUSE_MOTIVES.PROGRESSIVE_SYS10=SYS10_WAR04.PROGRESSIVE and "+
          "SYS10_WAR04.LANGUAGE_CODE=? and "+
          "WAR02_WAREHOUSE_MOVEMENTS.PROGRESSIVE_HIE01=SYS10_LOC.PROGRESSIVE and "+
          "SYS10_LOC.LANGUAGE_CODE=? and "+
          "WAR02_WAREHOUSE_MOVEMENTS.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
          "WAR02_WAREHOUSE_MOVEMENTS.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and "+
          "ITM01_ITEMS.PROGRESSIVE_SYS10=SYS10_ITM01.PROGRESSIVE and "+
          "SYS10_ITM01.LANGUAGE_CODE=? and "+
          "WAR02_WAREHOUSE_MOVEMENTS.COMPANY_CODE_SYS01=WAR01_WAREHOUSES.COMPANY_CODE_SYS01 and "+
          "WAR02_WAREHOUSE_MOVEMENTS.WAREHOUSE_CODE_WAR01=WAR01_WAREHOUSES.WAREHOUSE_CODE ";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("qtySignWAR04","WAR04_WAREHOUSE_MOTIVES.QTY_SIGN");
      attribute2dbField.put("itemTypeWAR04","WAR04_WAREHOUSE_MOTIVES.ITEM_TYPE");
      attribute2dbField.put("progressiveWAR02","WAR02_WAREHOUSE_MOVEMENTS.PROGRESSIVE");
      attribute2dbField.put("companyCodeSys01WAR02","WAR02_WAREHOUSE_MOVEMENTS.COMPANY_CODE_SYS01");
      attribute2dbField.put("warehouseCodeWar01WAR02","WAR02_WAREHOUSE_MOVEMENTS.WAREHOUSE_CODE_WAR01");
      attribute2dbField.put("itemCodeItm01WAR02","WAR02_WAREHOUSE_MOVEMENTS.ITEM_CODE_ITM01");
      attribute2dbField.put("progressiveHie01WAR02","WAR02_WAREHOUSE_MOVEMENTS.PROGRESSIVE_HIE01");
      attribute2dbField.put("locationDescriptionSYS10","SYS10_LOC.DESCRIPTION");
      attribute2dbField.put("movementDateWAR02","WAR02_WAREHOUSE_MOVEMENTS.MOVEMENT_DATE");
      attribute2dbField.put("usernameWAR02","WAR02_WAREHOUSE_MOVEMENTS.USERNAME");
      attribute2dbField.put("noteWAR02","WAR02_WAREHOUSE_MOVEMENTS.NOTE");
      attribute2dbField.put("deltaQtyWAR02","WAR02_WAREHOUSE_MOVEMENTS.DELTA_QTY");
      attribute2dbField.put("warehouseMotiveWar04WAR02","WAR02_WAREHOUSE_MOVEMENTS.WAREHOUSE_MOTIVE_WAR04");
      attribute2dbField.put("motiveDescriptionSYS10","SYS10_WAR04.DESCRIPTION");
      attribute2dbField.put("itemDescriptionSYS10","SYS10_ITM01.DESCRIPTION");
      attribute2dbField.put("descriptionWAR01","WAR01_WAREHOUSES.DESCRIPTION");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(serverLanguageId);
      values.add(serverLanguageId);

      GridParams gridParams = (GridParams)inputPar;

      // read from WAR02 table...
      Response answer = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          MovementVO.class,
          "Y",
          "N",
          context,
          gridParams,
          true
      );

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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching warehouse movements list",ex);
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
