package org.jallinone.purchases.suppliers.server;

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
import org.jallinone.purchases.suppliers.java.GridSupplierVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.message.send.java.LookupValidationParams;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;



/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to validate a supplier code from PUR01 table.</p>
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
public class ValidateSupplierCodeAction implements Action {


  public ValidateSupplierCodeAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "validateSupplierCode";
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

      LookupValidationParams lookupPars = (LookupValidationParams)inputPar;

      // retrieve companies list...
      String companies = "";

      // if client transmit a specific function code then use it to filter company code list by INSERT authorizations,
      // otherwise retrieve company code list from READ authorizations...
      if (lookupPars.getLookupValidationParameters().get(ApplicationConsts.FILTER_COMPANY_FOR_INSERT)!=null) {
       String functionCode = (String)lookupPars.getLookupValidationParameters().get(ApplicationConsts.FILTER_COMPANY_FOR_INSERT);
       ArrayList companiesList = ((JAIOUserSessionParameters)userSessionPars).getCompanyBa().getCompaniesList(functionCode);
       for(int i=0;i<companiesList.size();i++)
         if (((JAIOUserSessionParameters)userSessionPars).getCompanyBa().isInsertEnabled(functionCode,companiesList.get(i).toString()))
           companies += "'"+companiesList.get(i).toString()+"',";
     } else {
       ArrayList companiesList = ((JAIOUserSessionParameters)userSessionPars).getCompanyBa().getCompaniesList("PUR01");
       for(int i=0;i<companiesList.size();i++)
         companies += "'"+companiesList.get(i).toString()+"',";
     }


      if (companies.length()>0)
        companies = companies.substring(0,companies.length()-1);

      String sql =
          "select REG04_SUBJECTS.COMPANY_CODE_SYS01,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,REG04_SUBJECTS.PROGRESSIVE,REG04_SUBJECTS.CITY,REG04_SUBJECTS.PROVINCE,REG04_SUBJECTS.COUNTRY,REG04_SUBJECTS.TAX_CODE,PUR01_SUPPLIERS.SUPPLIER_CODE,"+
          "PUR01_SUPPLIERS.PAYMENT_CODE_REG10,SYS10_TRANSLATIONS.DESCRIPTION,PUR01_SUPPLIERS.DEBIT_ACCOUNT_CODE_ACC02 "+
          " from REG04_SUBJECTS,PUR01_SUPPLIERS,REG10_PAYMENTS,SYS10_TRANSLATIONS where "+
          "PUR01_SUPPLIERS.COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 and "+
          "PUR01_SUPPLIERS.PROGRESSIVE_REG04=REG04_SUBJECTS.PROGRESSIVE and "+
          "PUR01_SUPPLIERS.ENABLED='Y' and PUR01_SUPPLIERS.COMPANY_CODE_SYS01 in ("+companies+") and "+
          "PUR01_SUPPLIERS.PAYMENT_CODE_REG10=REG10_PAYMENTS.PAYMENT_CODE and "+
          "REG10_PAYMENTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and PUR01_SUPPLIERS.SUPPLIER_CODE=?";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01REG04","REG04_SUBJECTS.COMPANY_CODE_SYS01");
      attribute2dbField.put("name_1REG04","REG04_SUBJECTS.NAME_1");
      attribute2dbField.put("name_2REG04","REG04_SUBJECTS.NAME_2");
      attribute2dbField.put("progressiveREG04","REG04_SUBJECTS.PROGRESSIVE");
      attribute2dbField.put("cityREG04","REG04_SUBJECTS.CITY");
      attribute2dbField.put("provinceREG04","REG04_SUBJECTS.PROVINCE");
      attribute2dbField.put("countryREG04","REG04_SUBJECTS.COUNTRY");
      attribute2dbField.put("taxCodeREG04","REG04_SUBJECTS.TAX_CODE");
      attribute2dbField.put("supplierCodePUR01","PUR01_SUPPLIERS.SUPPLIER_CODE");
      attribute2dbField.put("paymentCodeReg10PUR01","PUR01_SUPPLIERS.PAYMENT_CODE_REG10");
      attribute2dbField.put("paymentDescriptionPUR01","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("debitAccountCodeAcc02PUR01","PUR01_SUPPLIERS.DEBIT_ACCOUNT_CODE_ACC02");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(lookupPars.getCode());

      GridParams gridParams = new GridParams();

      Response answer = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          GridSupplierVO.class,
          "Y",
          "N",
          context,
          gridParams,
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while validating a supplier code",ex);
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
