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
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch production order products from DOC23 table.</p>
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
public class LoadProdOrderProductsAction implements Action {


  public LoadProdOrderProductsAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadProdOrderProducts";
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


      GridParams pars = (GridParams)inputPar;
      ProdOrderPK pk = (ProdOrderPK)pars.getOtherGridParams().get(ApplicationConsts.PROD_ORDER_PK);

      String sql =
          "select DOC23_PRODUCTION_PRODUCTS.COMPANY_CODE_SYS01,DOC23_PRODUCTION_PRODUCTS.DOC_YEAR,"+
          "DOC23_PRODUCTION_PRODUCTS.DOC_NUMBER,DOC23_PRODUCTION_PRODUCTS.ITEM_CODE_ITM01,A.DESCRIPTION,"+
          "DOC23_PRODUCTION_PRODUCTS.QTY,DOC23_PRODUCTION_PRODUCTS.PROGRESSIVE_HIE01,B.DESCRIPTION,"+
          "DOC22_PRODUCTION_ORDER.WAREHOUSE_CODE_WAR01,DOC23_PRODUCTION_PRODUCTS.PROGRESSIVE_HIE02 "+
          " from DOC23_PRODUCTION_PRODUCTS,ITM01_ITEMS,SYS10_TRANSLATIONS A,SYS10_TRANSLATIONS B,DOC22_PRODUCTION_ORDER "+
          " where "+
          "DOC23_PRODUCTION_PRODUCTS.COMPANY_CODE_SYS01=DOC22_PRODUCTION_ORDER.COMPANY_CODE_SYS01 and "+
          "DOC23_PRODUCTION_PRODUCTS.DOC_YEAR=DOC22_PRODUCTION_ORDER.DOC_YEAR and "+
          "DOC23_PRODUCTION_PRODUCTS.DOC_NUMBER=DOC22_PRODUCTION_ORDER.DOC_NUMBER and "+
          "DOC23_PRODUCTION_PRODUCTS.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
          "DOC23_PRODUCTION_PRODUCTS.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and "+
          "DOC23_PRODUCTION_PRODUCTS.PROGRESSIVE_HIE01=B.PROGRESSIVE and "+
          "B.LANGUAGE_CODE=? and "+
          "ITM01_ITEMS.PROGRESSIVE_SYS10=A.PROGRESSIVE and "+
          "A.LANGUAGE_CODE=? and "+
          "DOC23_PRODUCTION_PRODUCTS.COMPANY_CODE_SYS01=? and "+
          "DOC23_PRODUCTION_PRODUCTS.DOC_YEAR=? and "+
          "DOC23_PRODUCTION_PRODUCTS.DOC_NUMBER=? ";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC23","DOC23_PRODUCTION_PRODUCTS.COMPANY_CODE_SYS01");
      attribute2dbField.put("docYearDOC23","DOC23_PRODUCTION_PRODUCTS.DOC_YEAR");
      attribute2dbField.put("docNumberDOC23","DOC23_PRODUCTION_PRODUCTS.DOC_NUMBER");
      attribute2dbField.put("itemCodeItm01DOC23","DOC23_PRODUCTION_PRODUCTS.ITEM_CODE_ITM01");
      attribute2dbField.put("descriptionSYS10","A.DESCRIPTION");
      attribute2dbField.put("qtyDOC23","DOC23_PRODUCTION_PRODUCTS.QTY");
      attribute2dbField.put("locationDescriptionSYS10","B.DESCRIPTION");
      attribute2dbField.put("progressiveHie01DOC23","DOC23_PRODUCTION_PRODUCTS.PROGRESSIVE_HIE01");
      attribute2dbField.put("warehouseCodeWar01DOC22","DOC22_PRODUCTION_ORDER.WAREHOUSE_CODE_WAR01");
      attribute2dbField.put("progressiveHie02DOC23","DOC23_PRODUCTION_PRODUCTS.PROGRESSIVE_HIE02");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(serverLanguageId);
      values.add(pk.getCompanyCodeSys01DOC22());
      values.add(pk.getDocYearDOC22());
      values.add(pk.getDocNumberDOC22());

      // read from DOC23 table...
      Response answer = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          ProdOrderProductVO.class,
          "Y",
          "N",
          context,
          pars,
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching production order products",ex);
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
