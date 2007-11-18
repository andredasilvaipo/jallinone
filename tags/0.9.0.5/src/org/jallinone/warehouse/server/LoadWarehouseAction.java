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
import org.jallinone.warehouse.java.WarehousePK;
import org.jallinone.commons.server.CustomizeQueryUtil;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch a specific warehouse from WAR01 table.</p>
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
public class LoadWarehouseAction implements Action {


  public LoadWarehouseAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadWarehouse";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    Connection conn = null;
    try {
      conn = ConnectionManager.getConnection(context);

      WarehousePK pk = (WarehousePK)inputPar;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01WAR01","WAR01_WAREHOUSES.COMPANY_CODE_SYS01");
      attribute2dbField.put("warehouseCodeWAR01","WAR01_WAREHOUSES.WAREHOUSE_CODE");
      attribute2dbField.put("descriptionWAR01","WAR01_WAREHOUSES.DESCRIPTION");
      attribute2dbField.put("progressiveHie02WAR01","WAR01_WAREHOUSES.PROGRESSIVE_HIE02");
      attribute2dbField.put("addressWAR01","WAR01_WAREHOUSES.ADDRESS");
      attribute2dbField.put("zipWAR01","WAR01_WAREHOUSES.ZIP");
      attribute2dbField.put("cityWAR01","WAR01_WAREHOUSES.CITY");
      attribute2dbField.put("provinceWAR01","WAR01_WAREHOUSES.PROVINCE");
      attribute2dbField.put("countryWAR01","WAR01_WAREHOUSES.COUNTRY");
      attribute2dbField.put("progressiveSys04WAR01","WAR01_WAREHOUSES.PROGRESSIVE_SYS04");
      attribute2dbField.put("name_1REG04","REG04_SUBJECTS.NAME_1");

      HashSet pkAttributes = new HashSet();
      pkAttributes.add("companyCodeSys01WAR01");
      pkAttributes.add("warehouseCodeWAR01");

      String baseSQL =
          "select WAR01_WAREHOUSES.COMPANY_CODE_SYS01,WAR01_WAREHOUSES.WAREHOUSE_CODE,WAR01_WAREHOUSES.DESCRIPTION,WAR01_WAREHOUSES.PROGRESSIVE_HIE02,WAR01_WAREHOUSES.ADDRESS,WAR01_WAREHOUSES.ZIP,WAR01_WAREHOUSES.CITY,WAR01_WAREHOUSES.PROVINCE,WAR01_WAREHOUSES.COUNTRY,WAR01_WAREHOUSES.PROGRESSIVE_SYS04,REG04_SUBJECTS.NAME_1 from "+
          "WAR01_WAREHOUSES,REG04_SUBJECTS where "+
          "WAR01_WAREHOUSES.COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 and "+
          "REG04_SUBJECTS.SUBJECT_TYPE='M' and "+
          "WAR01_WAREHOUSES.COMPANY_CODE_SYS01=? and "+
          "WAR01_WAREHOUSES.WAREHOUSE_CODE=?";

      ArrayList values = new ArrayList();
      values.add(pk.getCompanyCodeSys01WAR01());
      values.add(pk.getWarehouseCodeWAR01());

      // read from WAR01 table...
      Response res = CustomizeQueryUtil.getQuery(
          conn,
          userSessionPars,
          baseSQL,
          values,
          attribute2dbField,
          WarehouseVO.class,
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

      return answer;
    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching an existing warehouse",ex);
      try {
        conn.rollback();
      }
      catch (Exception ex3) {
      }
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
