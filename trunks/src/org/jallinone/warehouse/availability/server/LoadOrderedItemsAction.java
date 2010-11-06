package org.jallinone.warehouse.availability.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.warehouse.availability.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.openswing.swing.server.QueryUtil;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.items.java.ItemPK;
import java.math.BigDecimal;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;
import org.openswing.swing.message.receive.java.VOListResponse;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch future item availabilities for a specific warehouse and item.</p>
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
public class LoadOrderedItemsAction implements Action {


  public LoadOrderedItemsAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadOrderedItems";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();
    PreparedStatement pstmt = null;

    Connection conn = null;
    Statement stmt = null;
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

      GridParams gridPars = (GridParams)inputPar;
      ItemPK pk = (ItemPK)gridPars.getOtherGridParams().get(ApplicationConsts.ITEM_PK);
      if (pk==null)
        return new VOListResponse(new ArrayList(),false,0);

      String sql =
          "select DOC07_PURCHASE_ITEMS.DELIVERY_DATE,DOC07_PURCHASE_ITEMS.ORDER_QTY,"+
          "ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02,DOC07_PURCHASE_ITEMS.DOC_YEAR,DOC06_PURCHASE.DOC_SEQUENCE,"+
          "DOC06_PURCHASE.WAREHOUSE_CODE_WAR01,WAR01_WAREHOUSES.DESCRIPTION, "+
          "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM06,DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM11,"+
          "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM07,DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM12,"+
          "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM08,DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM13,"+
          "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM09,DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM14,"+
          "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM10,DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM15, "+
          "SYS10_ITM01.DESCRIPTION "+
          "from DOC07_PURCHASE_ITEMS,DOC06_PURCHASE,ITM01_ITEMS,WAR01_WAREHOUSES,SYS10_TRANSLATIONS SYS10_ITM01 "+
          "where "+
          "DOC07_PURCHASE_ITEMS.COMPANY_CODE_SYS01=DOC06_PURCHASE.COMPANY_CODE_SYS01 and "+
          "DOC07_PURCHASE_ITEMS.DOC_TYPE=DOC06_PURCHASE.DOC_TYPE and "+
          "DOC07_PURCHASE_ITEMS.DOC_YEAR=DOC06_PURCHASE.DOC_YEAR and "+
          "DOC07_PURCHASE_ITEMS.DOC_NUMBER=DOC06_PURCHASE.DOC_NUMBER and "+
          "DOC06_PURCHASE.DOC_STATE=? and "+
          "DOC06_PURCHASE.COMPANY_CODE_SYS01=WAR01_WAREHOUSES.COMPANY_CODE_SYS01 and "+
          "DOC06_PURCHASE.WAREHOUSE_CODE_WAR01=WAR01_WAREHOUSES.WAREHOUSE_CODE and "+
          "DOC07_PURCHASE_ITEMS.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
          "DOC07_PURCHASE_ITEMS.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and "+
          "DOC07_PURCHASE_ITEMS.COMPANY_CODE_SYS01=? and "+
          "DOC07_PURCHASE_ITEMS.ITEM_CODE_ITM01=? and "+
          "DOC07_PURCHASE_ITEMS.ORDER_QTY-DOC07_PURCHASE_ITEMS.IN_QTY>0 and "+
          "ITM01_ITEMS.PROGRESSIVE_SYS10=SYS10_ITM01.PROGRESSIVE and "+
          "SYS10_ITM01.LANGUAGE_CODE=? ";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("deliveryDateDOC07","DOC07_PURCHASE_ITEMS.DELIVERY_DATE");
      attribute2dbField.put("orderQtyDOC07","DOC07_PURCHASE_ITEMS.ORDER_QTY");
      attribute2dbField.put("minSellingQtyUmCodeReg02ITM01","ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02");
      attribute2dbField.put("docYearDOC06","DOC07_PURCHASE_ITEMS.DOC_YEAR");
      attribute2dbField.put("docSequenceDOC06","DOC06_PURCHASE.DOC_SEQUENCE");
      attribute2dbField.put("warehouseCodeWar01DOC06","DOC06_PURCHASE.WAREHOUSE_CODE_WAR01");
      attribute2dbField.put("descriptionWAR01","WAR01_WAREHOUSES.DESCRIPTION");

      attribute2dbField.put("variantTypeItm06DOC07","DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM06");
      attribute2dbField.put("variantCodeItm11DOC07","DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM11");
      attribute2dbField.put("variantTypeItm07DOC07","DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM07");
      attribute2dbField.put("variantCodeItm12DOC07","DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM12");
      attribute2dbField.put("variantTypeItm08DOC07","DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM08");
      attribute2dbField.put("variantCodeItm13DOC07","DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM13");
      attribute2dbField.put("variantTypeItm09DOC07","DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM09");
      attribute2dbField.put("variantCodeItm14DOC07","DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM14");
      attribute2dbField.put("variantTypeItm10DOC07","DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM10");
      attribute2dbField.put("variantCodeItm15DOC07","DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM15");
      attribute2dbField.put("descriptionSYS10","SYS10_ITM01.DESCRIPTION");

      ArrayList pars = new ArrayList();
      pars.add( ApplicationConsts.CONFIRMED );
      pars.add( pk.getCompanyCodeSys01ITM01() );
      pars.add( pk.getItemCodeITM01() );
      pars.add( serverLanguageId );

      if (gridPars.getOtherGridParams().get(ApplicationConsts.WAREHOUSE_CODE)!=null) {
        sql += " and DOC06_PURCHASE.WAREHOUSE_CODE_WAR01=? ";
        pars.add( gridPars.getOtherGridParams().get(ApplicationConsts.WAREHOUSE_CODE) );
      }

      Response answer = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          pars,
          attribute2dbField,
          OrderedItemQtyVO.class,
          "Y",
          "N",
          context,
          gridPars,
          50,
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching future item availabilities",ex);
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
