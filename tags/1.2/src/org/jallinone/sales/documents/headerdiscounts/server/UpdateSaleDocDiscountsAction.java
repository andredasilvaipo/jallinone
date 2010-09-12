package org.jallinone.sales.documents.headerdiscounts.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.sales.documents.headerdiscounts.java.*;
import org.openswing.swing.server.QueryUtil;
import java.math.BigDecimal;
import org.jallinone.sales.documents.java.SaleDocPK;
import org.jallinone.sales.documents.server.UpdateTaxableIncomesBean;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to update existing customer discounts for a sale document.</p>
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
public class UpdateSaleDocDiscountsAction implements Action {

  private UpdateTaxableIncomesBean totals = new UpdateTaxableIncomesBean();


  public UpdateSaleDocDiscountsAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "updateSaleDocDiscounts";
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
      SaleDocDiscountVO oldVO = null;
      SaleDocDiscountVO newVO = null;
      Response res = null;

      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (SaleDocDiscountVO)oldVOs.get(i);
        newVO = (SaleDocDiscountVO)newVOs.get(i);

        HashSet pkAttrs = new HashSet();
        pkAttrs.add("companyCodeSys01DOC05");
        pkAttrs.add("docTypeDOC05");
        pkAttrs.add("docYearDOC05");
        pkAttrs.add("docNumberDOC05");
        pkAttrs.add("discountCodeSal03DOC05");

        HashMap attribute2dbField = new HashMap();
        attribute2dbField.put("companyCodeSys01DOC05","COMPANY_CODE_SYS01");
        attribute2dbField.put("discountCodeSal03DOC05","DISCOUNT_CODE_SAL03");
        attribute2dbField.put("minValueDOC05","MIN_VALUE");
        attribute2dbField.put("maxValueDOC05","MAX_VALUE");
        attribute2dbField.put("minPercDOC05","MIN_PERC");
        attribute2dbField.put("maxPercDOC05","MAX_PERC");
        attribute2dbField.put("startDateDOC05","START_DATE");
        attribute2dbField.put("endDateDOC05","END_DATE");
        attribute2dbField.put("discountDescriptionDOC05","DISCOUNT_DESCRIPTION");
        attribute2dbField.put("valueDOC05","VALUE");
        attribute2dbField.put("percDOC05","PERC");
        attribute2dbField.put("docTypeDOC05","DOC_TYPE");
        attribute2dbField.put("docYearDOC05","DOC_YEAR");
        attribute2dbField.put("docNumberDOC05","DOC_NUMBER");


        res = new QueryUtil().updateTable(
            conn,
            userSessionPars,
            pkAttrs,
            oldVO,
            newVO,
            "DOC05_SELLING_DISCOUNTS",
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
        new SaleDocPK(newVO.getCompanyCodeSys01DOC05(),newVO.getDocTypeDOC05(),newVO.getDocYearDOC05(),newVO.getDocNumberDOC05()),
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while updating existing customer discounts",ex);
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
