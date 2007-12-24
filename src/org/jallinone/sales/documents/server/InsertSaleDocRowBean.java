package org.jallinone.sales.documents.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.sales.documents.java.DetailSaleDocRowVO;
import org.jallinone.system.server.JAIOUserSessionParameters;
import java.math.BigDecimal;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.system.progressives.server.ProgressiveUtils;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.system.java.ApplicationParametersVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.message.send.java.LookupValidationParams;
import org.jallinone.sales.documents.java.SaleDocPK;
import org.jallinone.sales.documents.java.DetailSaleDocVO;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Helper class used to insert a new sale document item row in DOC02 table.</p>
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
public class InsertSaleDocRowBean {

  private ProgressiveUtils progBean = new ProgressiveUtils();


  public InsertSaleDocRowBean() {
  }


  /**
   * Insert new item row in DOC02 table.
   */
  public final Response insertSaleItem(Connection conn,DetailSaleDocRowVO vo,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    PreparedStatement pstmt = null;
    try {

      // fires the GenericEvent.CONNECTION_CREATED event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "InsertSaleDocRowBean.insertSaleItem",
        GenericEvent.CONNECTION_CREATED,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        vo,
        null
      ));
      if (vo.getOutQtyDOC02()==null)
        vo.setOutQtyDOC02(new BigDecimal(0));
      if (vo.getQtyDOC02()==null)
        vo.setQtyDOC02(new BigDecimal(0));
      if (vo.getInvoiceQtyDOC02()==null)
        vo.setInvoiceQtyDOC02(new BigDecimal(0));

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC02","COMPANY_CODE_SYS01");
      attribute2dbField.put("docTypeDOC02","DOC_TYPE");
      attribute2dbField.put("docYearDOC02","DOC_YEAR");
      attribute2dbField.put("docNumberDOC02","DOC_NUMBER");
      attribute2dbField.put("rowNumberDOC02","ROW_NUMBER");
      attribute2dbField.put("itemCodeItm01DOC02","ITEM_CODE_ITM01");
      attribute2dbField.put("vatCodeItm01DOC02","VAT_CODE_ITM01");
      attribute2dbField.put("valueSal02DOC02","VALUE_SAL02");
      attribute2dbField.put("valueDOC02","VALUE");
      attribute2dbField.put("qtyDOC02","QTY");
      attribute2dbField.put("totalDiscountDOC02","TOTAL_DISCOUNT");
      attribute2dbField.put("vatValueDOC02","VAT_VALUE");

      attribute2dbField.put("vatDescriptionDOC02","VAT_DESCRIPTION");
      attribute2dbField.put("startDateSal02DOC02","START_DATE_SAL02");
      attribute2dbField.put("endDateSal02DOC02","END_DATE_SAL02");
      attribute2dbField.put("decimalsReg02DOC02","DECIMALS_REG02");
      attribute2dbField.put("minSellingQtyItm01DOC02","MIN_SELLING_QTY_ITM01");
      attribute2dbField.put("minSellingQtyUmCodeReg02DOC02","MIN_SELLING_QTY_UM_CODE_REG02");
      attribute2dbField.put("valueReg01DOC02","VALUE_REG01");
      attribute2dbField.put("deductibleReg01DOC02","DEDUCTIBLE_REG01");
      attribute2dbField.put("taxableIncomeDOC02","TAXABLE_INCOME");
      attribute2dbField.put("progressiveHie02DOC02","PROGRESSIVE_HIE02");
      attribute2dbField.put("deliveryDateDOC02","DELIVERY_DATE");
      attribute2dbField.put("outQtyDOC02","OUT_QTY");
      attribute2dbField.put("progressiveHie01DOC02","PROGRESSIVE_HIE01");
      attribute2dbField.put("invoiceQtyDOC02","INVOICE_QTY");

      vo.setRowNumberDOC02( progBean.getInternalProgressive("DOC02_SELLING_ITEMS","ROW_NUMBER",conn) );

      // insert into DOC02...
      Response res = QueryUtil.insertTable(
          conn,
          userSessionPars,
          vo,
          "DOC02_SELLING_ITEMS",
          attribute2dbField,
          "Y",
          "N",
          context,
          true
      );

      if (res.isError()) {
        return res;
      }

      Response answer = new VOResponse(vo);


      // fires the GenericEvent.BEFORE_COMMIT event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "InsertSaleDocRowBean.insertSaleItem",
        GenericEvent.BEFORE_COMMIT,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        vo,
        answer
      ));


      return answer;
    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"insertSaleItem","Error while inserting a new sale document item row",ex);
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        pstmt.close();
      }
      catch (Exception ex2) {
      }
    }
  }



}
