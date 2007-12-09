package org.jallinone.production.orders.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.production.orders.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.commons.server.CustomizeQueryUtil;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.system.java.ApplicationParametersVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch a specific production order from DOC22 table.</p>
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
public class LoadProdOrderAction implements Action {


  public LoadProdOrderAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadProdOrder";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();

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


      ProdOrderPK pk = (ProdOrderPK)inputPar;

      String sql =
          "select DOC22_PRODUCTION_ORDER.COMPANY_CODE_SYS01,DOC22_PRODUCTION_ORDER.DOC_STATE,DOC22_PRODUCTION_ORDER.DOC_YEAR,"+
          "DOC22_PRODUCTION_ORDER.DOC_NUMBER,DOC22_PRODUCTION_ORDER.DOC_DATE,DOC22_PRODUCTION_ORDER.WAREHOUSE_CODE_WAR01,"+
          "DOC22_PRODUCTION_ORDER.WAREHOUSE_CODE2_WAR01,DOC22_PRODUCTION_ORDER.DESCRIPTION_WAR01,DOC22_PRODUCTION_ORDER.DESCRIPTION2_WAR01,"+
          "DOC22_PRODUCTION_ORDER.NOTE,WAR01_WAREHOUSES.PROGRESSIVE_HIE02,DOC22_PRODUCTION_ORDER.DOC_SEQUENCE "+
          "from DOC22_PRODUCTION_ORDER,WAR01_WAREHOUSES where "+
          "DOC22_PRODUCTION_ORDER.COMPANY_CODE_SYS01=? and "+
          "DOC22_PRODUCTION_ORDER.DOC_YEAR=? and "+
          "DOC22_PRODUCTION_ORDER.DOC_NUMBER=? and "+
          "DOC22_PRODUCTION_ORDER.COMPANY_CODE_SYS01=WAR01_WAREHOUSES.COMPANY_CODE_SYS01 and "+
          "DOC22_PRODUCTION_ORDER.WAREHOUSE_CODE2_WAR01=WAR01_WAREHOUSES.WAREHOUSE_CODE ";


      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC22","DOC22_PRODUCTION_ORDER.COMPANY_CODE_SYS01");
      attribute2dbField.put("docStateDOC22","DOC22_PRODUCTION_ORDER.DOC_STATE");
      attribute2dbField.put("docYearDOC22","DOC22_PRODUCTION_ORDER.DOC_YEAR");
      attribute2dbField.put("docNumberDOC22","DOC22_PRODUCTION_ORDER.DOC_NUMBER");
      attribute2dbField.put("docSequenceDOC22","DOC22_PRODUCTION_ORDER.DOC_SEQUENCE");
      attribute2dbField.put("docDateDOC22","DOC22_PRODUCTION_ORDER.DOC_DATE");
      attribute2dbField.put("warehouseCodeWar01DOC22","DOC22_PRODUCTION_ORDER.WAREHOUSE_CODE_WAR01");
      attribute2dbField.put("warehouseCode2War01DOC22","DOC22_PRODUCTION_ORDER.WAREHOUSE_CODE2_WAR01");
      attribute2dbField.put("descriptionWar01DOC22","DOC22_PRODUCTION_ORDER.DESCRIPTION_WAR01");
      attribute2dbField.put("description2War01DOC22","DOC22_PRODUCTION_ORDER.DESCRIPTION2_WAR01");
      attribute2dbField.put("noteDOC22","DOC22_PRODUCTION_ORDER.NOTE");
      attribute2dbField.put("progressiveHie02WAR01","WAR01_WAREHOUSES.PROGRESSIVE_HIE02");

      ArrayList values = new ArrayList();
      values.add(pk.getCompanyCodeSys01DOC22());
      values.add(pk.getDocYearDOC22());
      values.add(pk.getDocNumberDOC22());

      // read from DOC22 table...
      Response answer = CustomizeQueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          DetailProdOrderVO.class,
          "Y",
          "N",
          context,
          true,
          ApplicationConsts.ID_PROD_ORDER
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching production orders list",ex);
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
