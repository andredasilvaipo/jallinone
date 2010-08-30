package org.jallinone.sales.documents.headercharges.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.sales.documents.headercharges.java.*;
import java.math.BigDecimal;
import org.jallinone.sales.documents.java.SaleDocPK;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.sales.documents.server.UpdateTaxableIncomesBean;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Helper class used to insert a new charge for a sale document in the DOC03 table.</p>
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
public class InsertSaleDocChargeBean {


  public InsertSaleDocChargeBean() {
  }


  /**
   * Insert a new charge for a sale document.
   * No commit or rollback are executed; no connection is created or released.
   */
  public final Response insertSaleDocCharge(Connection conn,SaleDocChargeVO vo,
                                       UserSessionParameters userSessionPars,
                                       HttpServletRequest request,
                                       HttpServletResponse response,
                                       HttpSession userSession,
                                       ServletContext context) {
    try {

      // fires the GenericEvent.CONNECTION_CREATED event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "InsertSaleDocChargeBean.insertSaleDocCharge",
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
      attribute2dbField.put("companyCodeSys01DOC03","COMPANY_CODE_SYS01");
      attribute2dbField.put("chargeCodeSal06DOC03","CHARGE_CODE_SAL06");
      attribute2dbField.put("valueSal06DOC03","VALUE_SAL06");
      attribute2dbField.put("percSal06DOC03","PERC_SAL06");
      attribute2dbField.put("chargeDescriptionDOC03","CHARGE_DESCRIPTION");
      attribute2dbField.put("valueDOC03","VALUE");
      attribute2dbField.put("percDOC03","PERC");
      attribute2dbField.put("docTypeDOC03","DOC_TYPE");
      attribute2dbField.put("docYearDOC03","DOC_YEAR");
      attribute2dbField.put("docNumberDOC03","DOC_NUMBER");
      attribute2dbField.put("vatCodeSal06DOC03","VAT_CODE_SAL06");
      attribute2dbField.put("vatDescriptionDOC03","VAT_DESCRIPTION");
      attribute2dbField.put("vatValueDOC03","VAT_VALUE");
      attribute2dbField.put("vatDeductibleDOC03","VAT_DEDUCTIBLE");
      attribute2dbField.put("invoicedValueDOC03","INVOICED_VALUE");
      attribute2dbField.put("valueReg01DOC03","VALUE_REG01");
      attribute2dbField.put("taxableIncomeDOC03","TAXABLE_INCOME");

      Response res = null;
      if (vo.getInvoicedValueDOC03()==null && vo.getValueDOC03()!=null)
        vo.setInvoicedValueDOC03(new BigDecimal(0));
      vo.setTaxableIncomeDOC03(vo.getValueDOC03());

      // insert into DOC03...
      res = QueryUtil.insertTable(
          conn,
          userSessionPars,
          vo,
          "DOC03_SELLING_CHARGES",
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
        "InsertSaleDocChargeBean.insertSaleDocCharge",
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
                   "insertSaleDocCharge", "Error while inserting a new sale charge", ex);
      return new ErrorResponse(ex.getMessage());
    }
    finally {
    }

  }



}

