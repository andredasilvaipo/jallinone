package org.jallinone.sales.documents.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.sales.documents.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.commons.server.CustomizeQueryUtil;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Helper class used to fetch sale order rows from DOC02 table.</p>
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
public class LoadSaleDocRowsBean {


  public LoadSaleDocRowsBean() {
  }


  /**
   * Load all item rows for the specified sale document.
   * No commit or rollback are executed; no connection is created or released.
   */
  public final Response loadSaleDocRows(Connection conn,GridParams pars,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();
    try {

      // fires the GenericEvent.CONNECTION_CREATED event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "LoadSaleDocRowsBean.loadSaleDocRows",
        GenericEvent.CONNECTION_CREATED,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        pars,
        null
      ));
     SaleDocPK pk = (SaleDocPK)pars.getOtherGridParams().get(ApplicationConsts.SALE_DOC_PK);

      String sql =
          "select DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01,DOC02_SELLING_ITEMS.DOC_TYPE,DOC02_SELLING_ITEMS.DOC_YEAR,DOC02_SELLING_ITEMS.DOC_NUMBER,DOC02_SELLING_ITEMS.ROW_NUMBER,"+
          "DOC02_SELLING_ITEMS.ITEM_CODE_ITM01,DOC02_SELLING_ITEMS.VAT_CODE_ITM01,DOC02_SELLING_ITEMS.VALUE_SAL02,"+
          "DOC02_SELLING_ITEMS.VALUE,DOC02_SELLING_ITEMS.QTY,DOC02_SELLING_ITEMS.TOTAL_DISCOUNT,"+
          "SYS10_TRANSLATIONS.DESCRIPTION,DOC02_SELLING_ITEMS.VAT_VALUE,DOC02_SELLING_ITEMS.OUT_QTY, "+
          "DOC02_SELLING_ITEMS.INVOICE_QTY, "+
          "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM06,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM11,"+
          "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM07,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM12,"+
          "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM08,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM13,"+
          "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM09,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM14,"+
          "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM10,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM15, "+
          "DOC02_SELLING_ITEMS.DELIVERY_DATE "+
          " from DOC02_SELLING_ITEMS,ITM01_ITEMS,SYS10_TRANSLATIONS where "+
          "DOC02_SELLING_ITEMS.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and "+
          "DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
          "ITM01_ITEMS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01=? and "+
          "DOC02_SELLING_ITEMS.DOC_TYPE=? and "+
          "DOC02_SELLING_ITEMS.DOC_YEAR=? and "+
          "DOC02_SELLING_ITEMS.DOC_NUMBER=? ";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC02","DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01");
      attribute2dbField.put("docTypeDOC02","DOC02_SELLING_ITEMS.DOC_TYPE");
      attribute2dbField.put("docYearDOC02","DOC02_SELLING_ITEMS.DOC_YEAR");
      attribute2dbField.put("docNumberDOC02","DOC02_SELLING_ITEMS.DOC_NUMBER");
      attribute2dbField.put("rowNumberDOC02","DOC02_SELLING_ITEMS.ROW_NUMBER");
      attribute2dbField.put("itemCodeItm01DOC02","DOC02_SELLING_ITEMS.ITEM_CODE_ITM01");
      attribute2dbField.put("vatCodeItm01DOC02","DOC02_SELLING_ITEMS.VAT_CODE_ITM01");
      attribute2dbField.put("valueSal02DOC02","DOC02_SELLING_ITEMS.VALUE_SAL02");
      attribute2dbField.put("valueDOC02","DOC02_SELLING_ITEMS.VALUE");
      attribute2dbField.put("qtyDOC02","DOC02_SELLING_ITEMS.QTY");
      attribute2dbField.put("totalDiscountDOC02","DOC02_SELLING_ITEMS.TOTAL_DISCOUNT");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("vatValueDOC02","DOC02_SELLING_ITEMS.VAT_VALUE");
      attribute2dbField.put("outQtyDOC02","DOC02_SELLING_ITEMS.OUT_QTY");
      attribute2dbField.put("invoiceQtyDOC02","DOC02_SELLING_ITEMS.INVOICE_QTY");

      attribute2dbField.put("variantTypeItm06DOC02","DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM06");
      attribute2dbField.put("variantCodeItm11DOC02","DOC02_SELLING_ITEMS.VARIANT_CODE_ITM11");
      attribute2dbField.put("variantTypeItm07DOC02","DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM07");
      attribute2dbField.put("variantCodeItm12DOC02","DOC02_SELLING_ITEMS.VARIANT_CODE_ITM12");
      attribute2dbField.put("variantTypeItm08DOC02","DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM08");
      attribute2dbField.put("variantCodeItm13DOC02","DOC02_SELLING_ITEMS.VARIANT_CODE_ITM13");
      attribute2dbField.put("variantTypeItm09DOC02","DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM09");
      attribute2dbField.put("variantCodeItm14DOC02","DOC02_SELLING_ITEMS.VARIANT_CODE_ITM14");
      attribute2dbField.put("variantTypeItm10DOC02","DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM10");
      attribute2dbField.put("variantCodeItm15DOC02","DOC02_SELLING_ITEMS.VARIANT_CODE_ITM15");

      attribute2dbField.put("deliveryDateDOC02","DOC02_SELLING_ITEMS.DELIVERY_DATE");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(pk.getCompanyCodeSys01DOC01());
      values.add(pk.getDocTypeDOC01());
      values.add(pk.getDocYearDOC01());
      values.add(pk.getDocNumberDOC01());

      // read from DOC02 table...
      Response answer = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          GridSaleDocRowVO.class,
          "Y",
          "N",
          context,
          pars,
          true
      );

      // fires the GenericEvent.BEFORE_COMMIT event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "LoadSaleDocRowsBean.loadSaleDocRows",
        GenericEvent.BEFORE_COMMIT,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        pars,
        answer
      ));


      return answer;

    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"loadSaleDocRows","Error while fetching sale order rows list",ex);
      return new ErrorResponse(ex.getMessage());
    }
    finally {
    }

  }



}
