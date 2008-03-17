package org.jallinone.sales.documents.activities.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.sales.documents.activities.java.*;
import java.math.BigDecimal;
import org.jallinone.sales.documents.java.SaleDocPK;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Helper class used to insert a new sale activity for a sale document in the DOC13 table.</p>
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
public class InsertSaleDocActivityBean {


  public InsertSaleDocActivityBean() {
  }


  /**
   * Insert new activity in the sale document.
   * It does not execute any commit or rollback operations.
   */
  public final Response insertSaleActivity(Connection conn,
                                       SaleDocActivityVO vo,
                                       UserSessionParameters userSessionPars,
                                       HttpServletRequest request,
                                       HttpServletResponse response,
                                       HttpSession userSession,
                                       ServletContext context) {
    PreparedStatement pstmt = null;
    try {

      // fires the GenericEvent.CONNECTION_CREATED event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "InsertSaleDocActivityBean.insertSaleActivity",
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
      if (vo.getInvoicedValueDOC13()==null)
          vo.setInvoicedValueDOC13(new BigDecimal(0));
      vo.setTaxableIncomeDOC13(vo.getValueDOC13());
      vo.setVatValueDOC13(new BigDecimal(0));

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC13","COMPANY_CODE_SYS01");
      attribute2dbField.put("activityCodeSal09DOC13","ACTIVITY_CODE_SAL09");
      attribute2dbField.put("valueSal09DOC13","VALUE_SAL09");
      attribute2dbField.put("activityDescriptionDOC13","ACTIVITY_DESCRIPTION");
      attribute2dbField.put("valueDOC13","VALUE");
      attribute2dbField.put("docTypeDOC13","DOC_TYPE");
      attribute2dbField.put("docYearDOC13","DOC_YEAR");
      attribute2dbField.put("docNumberDOC13","DOC_NUMBER");
      attribute2dbField.put("vatCodeSal09DOC13","VAT_CODE_SAL09");
      attribute2dbField.put("vatDescriptionDOC13","VAT_DESCRIPTION");
      attribute2dbField.put("vatValueDOC13","VAT_VALUE");
      attribute2dbField.put("vatDeductibleDOC13","VAT_DEDUCTIBLE");
      attribute2dbField.put("durationDOC13","DURATION");
      attribute2dbField.put("currencyCodeReg03DOC13","CURRENCY_CODE_REG03");
      attribute2dbField.put("progressiveSch06DOC13","PROGRESSIVE_SCH06");
      attribute2dbField.put("invoicedValueDOC13","INVOICED_VALUE");
      attribute2dbField.put("valueReg01DOC13","VALUE_REG01");
      attribute2dbField.put("taxableIncomeDOC13","TAXABLE_INCOME");


      // insert into DOC13...
      Response res = QueryUtil.insertTable(
          conn,
          userSessionPars,
          vo,
          "DOC13_SELLING_ACTIVITIES",
          attribute2dbField,
          "Y",
          "N",
          context,
          true
      );

      if (res.isError()) {
        return res;
      }

      pstmt = conn.prepareStatement("update DOC01_SELLING set DOC_STATE=? where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=?");
      pstmt.setString(1,ApplicationConsts.HEADER_BLOCKED);
      pstmt.setString(2,vo.getCompanyCodeSys01DOC13());
      pstmt.setString(3,vo.getDocTypeDOC13());
      pstmt.setBigDecimal(4,vo.getDocYearDOC13());
      pstmt.setBigDecimal(5,vo.getDocNumberDOC13());
      pstmt.execute();

      Response answer = new VOResponse(vo);

      // fires the GenericEvent.BEFORE_COMMIT event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "InsertSaleDocActivityBean.insertSaleActivity",
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
                   "executeCommand", "Error while inserting a new sale activity", ex);
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

