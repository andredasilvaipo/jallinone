package org.jallinone.sales.documents.itemdiscounts.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.sales.documents.itemdiscounts.java.*;
import java.math.BigDecimal;
import org.jallinone.sales.documents.java.SaleDocRowPK;
import org.jallinone.sales.documents.server.UpdateTaxableIncomesBean;
import org.jallinone.sales.documents.java.SaleDocPK;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Helper class used to insert a new item discount for a sale document row in the DOC04 table.</p>
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
public class InsertSaleDocRowDiscountBean {


  public InsertSaleDocRowDiscountBean() {
  }


  /**
   * Insert a new item row discount.
   * No commit or rollback is executed; no connection is created or released.
   */
  public final Response insertSaleDocRowDiscount(Connection conn,SaleItemDiscountVO vo,
                                       UserSessionParameters userSessionPars,
                                       HttpServletRequest request,
                                       HttpServletResponse response,
                                       HttpSession userSession,
                                       ServletContext context) {
    try {

      // fires the GenericEvent.CONNECTION_CREATED event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "InsertSaleDocRowDiscountBean.insertSaleDocRowDiscount",
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
      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC04","COMPANY_CODE_SYS01");
      attribute2dbField.put("discountCodeSal03DOC04","DISCOUNT_CODE_SAL03");
      attribute2dbField.put("minValueDOC04","MIN_VALUE");
      attribute2dbField.put("maxValueDOC04","MAX_VALUE");
      attribute2dbField.put("minPercDOC04","MIN_PERC");
      attribute2dbField.put("maxPercDOC04","MAX_PERC");
      attribute2dbField.put("startDateDOC04","START_DATE");
      attribute2dbField.put("endDateDOC04","END_DATE");
      attribute2dbField.put("discountDescriptionDOC04","DISCOUNT_DESCRIPTION");
      attribute2dbField.put("valueDOC04","VALUE");
      attribute2dbField.put("percDOC04","PERC");
      attribute2dbField.put("docTypeDOC04","DOC_TYPE");
      attribute2dbField.put("docYearDOC04","DOC_YEAR");
      attribute2dbField.put("docNumberDOC04","DOC_NUMBER");
      attribute2dbField.put("itemCodeItm01DOC04","ITEM_CODE_ITM01");
      attribute2dbField.put("minQtyDOC04","MIN_QTY");
      attribute2dbField.put("multipleQtyDOC04","MULTIPLE_QTY");

      Response res = null;
      // insert into DOC04...
      res = QueryUtil.insertTable(
          conn,
          userSessionPars,
          vo,
          "DOC04_SELLING_ITEM_DISCOUNTS",
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
        "InsertSaleDocRowDiscountBean.insertSaleDocRowDiscount",
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
      Logger.error(userSessionPars.getUsername(), this.getClass().getName(),
                   "insertSaleDocRowDiscount", "Error while inserting a new item discount", ex);
      return new ErrorResponse(ex.getMessage());
    }
    finally {
    }

  }



}

