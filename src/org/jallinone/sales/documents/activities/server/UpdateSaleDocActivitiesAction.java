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
import org.openswing.swing.server.QueryUtil;
import java.math.BigDecimal;
import org.jallinone.sales.documents.java.SaleDocPK;
import org.jallinone.sales.documents.server.UpdateTaxableIncomesBean;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to update existing sale activities for a sale document.</p>
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
public class UpdateSaleDocActivitiesAction implements Action {

  private UpdateTaxableIncomesBean totals = new UpdateTaxableIncomesBean();


  public UpdateSaleDocActivitiesAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "updateSaleDocActivities";
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
      SaleDocActivityVO oldVO = null;
      SaleDocActivityVO newVO = null;
      Response res = null;

      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (SaleDocActivityVO)oldVOs.get(i);
        newVO = (SaleDocActivityVO)newVOs.get(i);

        HashSet pkAttrs = new HashSet();
        pkAttrs.add("companyCodeSys01DOC13");
        pkAttrs.add("docTypeDOC13");
        pkAttrs.add("docYearDOC13");
        pkAttrs.add("docNumberDOC13");
        pkAttrs.add("activityCodeSal09DOC13");

        HashMap attribute2dbField = new HashMap();
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
//        attribute2dbField.put("vatValueDOC13","VAT_VALUE"); this field is updated from  UpdateTaxableIncomesBean...
        attribute2dbField.put("vatDeductibleDOC13","VAT_DEDUCTIBLE");
        attribute2dbField.put("durationDOC13","DURATION");
        attribute2dbField.put("progressiveSch06DOC13","PROGRESSIVE_SCH06");
        attribute2dbField.put("currencyCodeReg03DOC13","CURRENCY_CODE_REG03");
        attribute2dbField.put("invoicedValueDOC13","INVOICED_VALUE");
        attribute2dbField.put("valueReg01DOC13","VALUE_REG01");
//        attribute2dbField.put("taxableIncomeDOC13","TAXABLE_INCOME"); this field is updated from  UpdateTaxableIncomesBean...


        res = new QueryUtil().updateTable(
            conn,
            userSessionPars,
            pkAttrs,
            oldVO,
            newVO,
            "DOC13_SELLING_ACTIVITIES",
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
        new SaleDocPK(newVO.getCompanyCodeSys01DOC13(),newVO.getDocTypeDOC13(),newVO.getDocYearDOC13(),newVO.getDocNumberDOC13()),
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while updating existing sale activitys",ex);
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
