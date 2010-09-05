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
import org.openswing.swing.server.QueryUtil;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.sales.customers.java.*;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch customers from SAL07+REG04 tables.</p>
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
public class LoadCustomersAction implements Action {


  public LoadCustomersAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadCustomers";
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
      GridParams gridPars = (GridParams)inputPar;

      // retrieve companies list...
      String companies = "";
      if (gridPars.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01)!=null) {
        companies = "'"+gridPars.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01)+"'";
      }
      else {
        ArrayList companiesList = ((JAIOUserSessionParameters)userSessionPars).getCompanyBa().getCompaniesList("SAL07");
        for(int i=0;i<companiesList.size();i++)
          companies += "'"+companiesList.get(i).toString()+"',";
        companies = companies.substring(0,companies.length()-1);
      }

      String sql =
          "select REG04_SUBJECTS.COMPANY_CODE_SYS01,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,REG04_SUBJECTS.PROGRESSIVE,REG04_SUBJECTS.CITY,REG04_SUBJECTS.PROVINCE,REG04_SUBJECTS.COUNTRY,REG04_SUBJECTS.TAX_CODE,SAL07_CUSTOMERS.CUSTOMER_CODE,REG04_SUBJECTS.SUBJECT_TYPE,"+
          "SAL07_CUSTOMERS.PAYMENT_CODE_REG10,SYS10_TRANSLATIONS.DESCRIPTION,SAL07_CUSTOMERS.VAT_CODE_REG01,SAL07_CUSTOMERS.CREDIT_ACCOUNT_CODE_ACC02,REG04_SUBJECTS.NOTE "+
          " from REG04_SUBJECTS,SAL07_CUSTOMERS,SYS10_TRANSLATIONS,REG10_PAYMENTS where "+
          "SAL07_CUSTOMERS.COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 and "+
          "SAL07_CUSTOMERS.PROGRESSIVE_REG04=REG04_SUBJECTS.PROGRESSIVE and "+
          "SAL07_CUSTOMERS.PAYMENT_CODE_REG10=REG10_PAYMENTS.PAYMENT_CODE and "+
          "REG10_PAYMENTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "SAL07_CUSTOMERS.ENABLED='Y' and SAL07_CUSTOMERS.COMPANY_CODE_SYS01 in ("+companies+") ";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01REG04","REG04_SUBJECTS.COMPANY_CODE_SYS01");
      attribute2dbField.put("name_1REG04","REG04_SUBJECTS.NAME_1");
      attribute2dbField.put("name_2REG04","REG04_SUBJECTS.NAME_2");
      attribute2dbField.put("progressiveREG04","REG04_SUBJECTS.PROGRESSIVE");
      attribute2dbField.put("cityREG04","REG04_SUBJECTS.CITY");
      attribute2dbField.put("provinceREG04","REG04_SUBJECTS.PROVINCE");
      attribute2dbField.put("countryREG04","REG04_SUBJECTS.COUNTRY");
      attribute2dbField.put("taxCodeREG04","REG04_SUBJECTS.TAX_CODE");
      attribute2dbField.put("customerCodeSAL07","SAL07_CUSTOMERS.CUSTOMER_CODE");
      attribute2dbField.put("subjectTypeREG04","REG04_SUBJECTS.SUBJECT_TYPE");
      attribute2dbField.put("paymentCodeReg10SAL07","SAL07_CUSTOMERS.PAYMENT_CODE_REG10");
      attribute2dbField.put("paymentDescriptionSAL07","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("vatCodeReg01SAL07","SAL07_CUSTOMERS.VAT_CODE_REG01");
      attribute2dbField.put("creditAccountCodeAcc02SAL07","SAL07_CUSTOMERS.CREDIT_ACCOUNT_CODE_ACC02");
      attribute2dbField.put("noteREG04","REG04_SUBJECTS.NOTE");


      ArrayList pars = new ArrayList();
      pars.add(serverLanguageId);

      if (gridPars.getOtherGridParams().get(ApplicationConsts.SUBJECT_TYPE)!=null) {
        sql += " and REG04_SUBJECTS.SUBJECT_TYPE='"+gridPars.getOtherGridParams().get(ApplicationConsts.SUBJECT_TYPE)+"'";
      }

      Response answer = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          pars,
          attribute2dbField,
          GridCustomerVO.class,
          "Y",
          "N",
          context,
          gridPars,
          50,
          true
      );

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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching customers list",ex);
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
