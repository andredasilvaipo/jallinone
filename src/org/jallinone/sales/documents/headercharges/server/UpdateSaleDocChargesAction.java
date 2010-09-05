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
import org.openswing.swing.server.QueryUtil;
import java.math.BigDecimal;
import org.jallinone.sales.documents.java.SaleDocPK;
import org.jallinone.sales.documents.server.UpdateTaxableIncomesBean;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to update existing charges for a sale document.</p>
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
public class UpdateSaleDocChargesAction implements Action {

  private UpdateTaxableIncomesBean totals = new UpdateTaxableIncomesBean();


  public UpdateSaleDocChargesAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "updateSaleDocCharges";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    Connection conn = null;
    try {
      String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();
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

      ArrayList oldVOs = ((ArrayList[])inputPar)[0];
      ArrayList newVOs = ((ArrayList[])inputPar)[1];
      SaleDocChargeVO oldVO = null;
      SaleDocChargeVO newVO = null;
      Response res = null;

      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (SaleDocChargeVO)oldVOs.get(i);
        newVO = (SaleDocChargeVO)newVOs.get(i);
        if (newVO.getValueDOC03()!=null)
          newVO.setInvoicedValueDOC03(new BigDecimal(0));
        else
          newVO.setInvoicedValueDOC03(null);

        HashSet pkAttrs = new HashSet();
        pkAttrs.add("companyCodeSys01DOC03");
        pkAttrs.add("docTypeDOC03");
        pkAttrs.add("docYearDOC03");
        pkAttrs.add("docNumberDOC03");
        pkAttrs.add("chargeCodeSal06DOC03");

        HashMap attribute2dbField = new HashMap();
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
//        attribute2dbField.put("vatValueDOC03","VAT_VALUE"); this field is updated from  UpdateTaxableIncomesBean...
        attribute2dbField.put("vatDeductibleDOC03","VAT_DEDUCTIBLE");
        attribute2dbField.put("invoicedValueDOC03","INVOICED_VALUE");
        attribute2dbField.put("valueReg01DOC03","VALUE_REG01");
//        attribute2dbField.put("taxableIncomeDOC03","TAXABLE_INCOME"); this field is updated from  UpdateTaxableIncomesBean...

        res = new QueryUtil().updateTable(
            conn,
            userSessionPars,
            pkAttrs,
            oldVO,
            newVO,
            "DOC03_SELLING_CHARGES",
            attribute2dbField,
            "Y",
            "N",
            context,
            true
        );
        if (res.isError()) {
          conn.rollback();
          return res;
        }
      }

      res = totals.updateTaxableIncomes(
        conn,
        new SaleDocPK(newVO.getCompanyCodeSys01DOC03(),newVO.getDocTypeDOC03(),newVO.getDocYearDOC03(),newVO.getDocNumberDOC03()),
        userSessionPars,
        request,
        response,
        userSession,
        context
      );
      if (res.isError()) {
        conn.rollback();
        return res;
      }

      Response answer = new VOListResponse(newVOs,false,newVOs.size());

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

      conn.commit();

      // fires the GenericEvent.AFTER_COMMIT event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        getRequestName(),
        GenericEvent.AFTER_COMMIT,
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while updating existing sale charges",ex);
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
