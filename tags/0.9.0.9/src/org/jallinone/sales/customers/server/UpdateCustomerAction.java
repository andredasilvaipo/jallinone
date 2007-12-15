package org.jallinone.sales.customers.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import java.math.BigDecimal;
import org.jallinone.system.progressives.server.ProgressiveUtils;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.subjects.java.Subject;
import org.jallinone.subjects.java.OrganizationVO;
import org.jallinone.subjects.server.OrganizationBean;
import org.jallinone.subjects.java.PeopleVO;
import org.jallinone.subjects.server.PeopleBean;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to update an existing customer.</p>
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
public class UpdateCustomerAction implements Action {


  public UpdateCustomerAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "updateCustomer";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
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

      Subject oldVO = (Subject)((ValueObject[])inputPar)[0];
      Subject newVO = (Subject)((ValueObject[])inputPar)[1];

      Response res = null;
      // update REG04...
      if (newVO.getSubjectTypeREG04().equals(ApplicationConsts.SUBJECT_ORGANIZATION_CUSTOMER)) {
        res = new OrganizationBean().update(conn,(OrganizationVO)oldVO,(OrganizationVO)newVO,userSessionPars,context);
      }
      else {
        res = new PeopleBean().update(conn,(PeopleVO)oldVO,(PeopleVO)newVO,userSessionPars,context);
      }

      if (res.isError()) {
        return res;
      }

      // update SAL07 table...
      Map attribute2dbField = new HashMap();
      attribute2dbField.put("paymentCodeReg10SAL07","PAYMENT_CODE_REG10");
      attribute2dbField.put("bankCodeReg12SAL07","BANK_CODE_REG12");
      attribute2dbField.put("agentProgressiveReg04SAL07","AGENT_PROGRESSIVE_REG04");
      attribute2dbField.put("trustAmountSAL07","TRUST_AMOUNT");
      attribute2dbField.put("vatCodeReg01SAL07","VAT_CODE_REG01");
      attribute2dbField.put("pricelistCodeSal01SAL07","PRICELIST_CODE_SAL01");
      attribute2dbField.put("creditAccountCodeAcc02SAL07","CREDIT_ACCOUNT_CODE_ACC02");
      attribute2dbField.put("itemsAccountCodeAcc02SAL07","ITEMS_ACCOUNT_CODE_ACC02");
      attribute2dbField.put("activitiesAccountCodeAcc02SAL07","ACTIVITIES_ACCOUNT_CODE_ACC02");
      attribute2dbField.put("chargesAccountCodeAcc02SAL07","CHARGES_ACCOUNT_CODE_ACC02");

      HashSet pkAttributes = new HashSet();
      attribute2dbField.put("companyCodeSys01REG04","COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveREG04","PROGRESSIVE_REG04");

      res = CustomizeQueryUtil.updateTable(
          conn,
          userSessionPars,
          pkAttributes,
          (ValueObject)oldVO,
          (ValueObject)newVO,
          "SAL07_CUSTOMERS",
          attribute2dbField,
          "Y",
          "N",
          context,
          true,
          new BigDecimal(282) // window identifier...
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while updating an existing customer",ex);
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
