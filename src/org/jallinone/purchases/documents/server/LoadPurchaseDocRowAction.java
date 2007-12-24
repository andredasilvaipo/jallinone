package org.jallinone.purchases.documents.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.purchases.documents.java.*;
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
 * <p>Description: Action class used to fetch a specific purchase order row from DOC07 table.</p>
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
public class LoadPurchaseDocRowAction implements Action {


  public LoadPurchaseDocRowAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadPurchaseDocRow";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();
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

      PurchaseDocRowPK pk = (PurchaseDocRowPK)inputPar;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC07","DOC07_PURCHASE_ITEMS.COMPANY_CODE_SYS01");
      attribute2dbField.put("docTypeDOC07","DOC07_PURCHASE_ITEMS.DOC_TYPE");
      attribute2dbField.put("docYearDOC07","DOC07_PURCHASE_ITEMS.DOC_YEAR");
      attribute2dbField.put("docNumberDOC07","DOC07_PURCHASE_ITEMS.DOC_NUMBER");
      attribute2dbField.put("rowNumberDOC07","DOC07_PURCHASE_ITEMS.ROW_NUMBER");
      attribute2dbField.put("itemCodeItm01DOC07","DOC07_PURCHASE_ITEMS.ITEM_CODE_ITM01");
      attribute2dbField.put("supplierItemCodePur02DOC07","DOC07_PURCHASE_ITEMS.SUPPLIER_ITEM_CODE_PUR02");
      attribute2dbField.put("vatCodeItm01DOC07","DOC07_PURCHASE_ITEMS.VAT_CODE_ITM01");
      attribute2dbField.put("valuePur04DOC07","DOC07_PURCHASE_ITEMS.VALUE_PUR04");
      attribute2dbField.put("valueDOC07","DOC07_PURCHASE_ITEMS.VALUE");
      attribute2dbField.put("qtyDOC07","DOC07_PURCHASE_ITEMS.QTY");
      attribute2dbField.put("discountValueDOC07","DOC07_PURCHASE_ITEMS.DISCOUNT_VALUE");
      attribute2dbField.put("discountPercDOC07","DOC07_PURCHASE_ITEMS.DISCOUNT_PERC");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("vatValueDOC07","DOC07_PURCHASE_ITEMS.VAT_VALUE");

      attribute2dbField.put("vatDescriptionDOC07","DOC07_PURCHASE_ITEMS.VAT_DESCRIPTION");
      attribute2dbField.put("startDatePur04DOC07","DOC07_PURCHASE_ITEMS.START_DATE_PUR04");
      attribute2dbField.put("endDatePur04DOC07","DOC07_PURCHASE_ITEMS.END_DATE_PUR04");
      attribute2dbField.put("umCodePur02DOC07","DOC07_PURCHASE_ITEMS.UM_CODE_PUR02");
      attribute2dbField.put("decimalsReg02DOC07","DOC07_PURCHASE_ITEMS.DECIMALS_REG02");
      attribute2dbField.put("minPurchaseQtyPur02DOC07","DOC07_PURCHASE_ITEMS.MIN_PURCHASE_QTY_PUR02");
      attribute2dbField.put("multipleQtyPur02DOC07","DOC07_PURCHASE_ITEMS.MULTIPLE_QTY_PUR02");
      attribute2dbField.put("valueReg01DOC07","DOC07_PURCHASE_ITEMS.VALUE_REG01");
      attribute2dbField.put("deductibleReg01DOC07","DOC07_PURCHASE_ITEMS.DEDUCTIBLE_REG01");
      attribute2dbField.put("taxableIncomeDOC07","DOC07_PURCHASE_ITEMS.TAXABLE_INCOME");
      attribute2dbField.put("progressiveHie02DOC07","DOC07_PURCHASE_ITEMS.PROGRESSIVE_HIE02");
      attribute2dbField.put("deliveryDateDOC07","DOC07_PURCHASE_ITEMS.DELIVERY_DATE");
      attribute2dbField.put("inQtyDOC07","DOC07_PURCHASE_ITEMS.IN_QTY");
      attribute2dbField.put("orderQtyDOC07","DOC07_PURCHASE_ITEMS.ORDER_QTY");
      attribute2dbField.put("currencyCodeReg03DOC06","DOC06_PURCHASE.CURRENCY_CODE_REG03");
      attribute2dbField.put("invoiceQtyDOC07","DOC07_PURCHASE_ITEMS.INVOICE_QTY");

      String baseSQL =
          "select DOC07_PURCHASE_ITEMS.COMPANY_CODE_SYS01,DOC07_PURCHASE_ITEMS.DOC_TYPE,DOC07_PURCHASE_ITEMS.DOC_YEAR,DOC07_PURCHASE_ITEMS.DOC_NUMBER,DOC07_PURCHASE_ITEMS.ROW_NUMBER,"+
          "DOC07_PURCHASE_ITEMS.ITEM_CODE_ITM01,DOC07_PURCHASE_ITEMS.SUPPLIER_ITEM_CODE_PUR02,DOC07_PURCHASE_ITEMS.VAT_CODE_ITM01,DOC07_PURCHASE_ITEMS.VALUE_PUR04,"+
          "DOC07_PURCHASE_ITEMS.VALUE,DOC07_PURCHASE_ITEMS.QTY,DOC07_PURCHASE_ITEMS.DISCOUNT_VALUE,DOC07_PURCHASE_ITEMS.DISCOUNT_PERC,"+
          "SYS10_TRANSLATIONS.DESCRIPTION,DOC07_PURCHASE_ITEMS.VAT_VALUE,DOC07_PURCHASE_ITEMS.VAT_DESCRIPTION,DOC07_PURCHASE_ITEMS.START_DATE_PUR04,DOC07_PURCHASE_ITEMS.END_DATE_PUR04,"+
          "DOC07_PURCHASE_ITEMS.UM_CODE_PUR02,DOC07_PURCHASE_ITEMS.DECIMALS_REG02,DOC07_PURCHASE_ITEMS.MIN_PURCHASE_QTY_PUR02,DOC07_PURCHASE_ITEMS.MULTIPLE_QTY_PUR02,"+
          "DOC07_PURCHASE_ITEMS.VALUE_REG01,DOC07_PURCHASE_ITEMS.DEDUCTIBLE_REG01,DOC07_PURCHASE_ITEMS.TAXABLE_INCOME,DOC07_PURCHASE_ITEMS.PROGRESSIVE_HIE02,DOC07_PURCHASE_ITEMS.DELIVERY_DATE,"+
          "DOC07_PURCHASE_ITEMS.IN_QTY,DOC07_PURCHASE_ITEMS.ORDER_QTY,DOC06_PURCHASE.CURRENCY_CODE_REG03,DOC07_PURCHASE_ITEMS.INVOICE_QTY "+
          " from DOC07_PURCHASE_ITEMS,ITM01_ITEMS,SYS10_TRANSLATIONS,DOC06_PURCHASE where "+
          "DOC07_PURCHASE_ITEMS.COMPANY_CODE_SYS01=DOC06_PURCHASE.COMPANY_CODE_SYS01 and "+
          "DOC07_PURCHASE_ITEMS.DOC_TYPE=DOC06_PURCHASE.DOC_TYPE and "+
          "DOC07_PURCHASE_ITEMS.DOC_YEAR=DOC06_PURCHASE.DOC_YEAR and "+
          "DOC07_PURCHASE_ITEMS.DOC_NUMBER=DOC06_PURCHASE.DOC_NUMBER and "+
          "DOC07_PURCHASE_ITEMS.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and "+
          "DOC07_PURCHASE_ITEMS.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
          "ITM01_ITEMS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "DOC07_PURCHASE_ITEMS.COMPANY_CODE_SYS01=? and "+
          "DOC07_PURCHASE_ITEMS.DOC_TYPE=? and "+
          "DOC07_PURCHASE_ITEMS.DOC_YEAR=? and "+
          "DOC07_PURCHASE_ITEMS.DOC_NUMBER=? and "+
          "DOC07_PURCHASE_ITEMS.ITEM_CODE_ITM01=? ";

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(pk.getCompanyCodeSys01DOC07());
      values.add(pk.getDocTypeDOC07());
      values.add(pk.getDocYearDOC07());
      values.add(pk.getDocNumberDOC07());
      values.add(pk.getItemCodeItm01DOC07());

      // read from DOC07 table...
      Response res = QueryUtil.getQuery(
          conn,
          userSessionPars,
          baseSQL,
          values,
          attribute2dbField,
          DetailPurchaseDocRowVO.class,
          "Y",
          "N",
          context,
          true
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching an existing purchase order row",ex);
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
