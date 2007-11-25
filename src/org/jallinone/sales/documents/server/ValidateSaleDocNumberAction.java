package org.jallinone.sales.documents.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.sales.documents.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.commons.server.CustomizeQueryUtil;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.hierarchies.java.HierarchyLevelVO;
import org.openswing.swing.message.send.java.LookupValidationParams;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to validate a sale doc number from DOC01 table, based on the specified COMPANY_CODE.</p>
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
public class ValidateSaleDocNumberAction implements Action {


  public ValidateSaleDocNumberAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "validateSaleDocNumber";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();

    PreparedStatement pstmt = null;
    Connection conn = null;
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

      LookupValidationParams pars = (LookupValidationParams)inputPar;

      // retrieve companies list...
      String companies = "";
      if (pars.getLookupValidationParameters().get(ApplicationConsts.COMPANY_CODE_SYS01)!=null) {
        companies = "'"+pars.getLookupValidationParameters().get(ApplicationConsts.COMPANY_CODE_SYS01)+"'";
      }
      else {
        ArrayList companiesList = ((JAIOUserSessionParameters)userSessionPars).getCompanyBa().getCompaniesList("DOC01_ORDERS");
        for(int i=0;i<companiesList.size();i++)
          companies += "'"+companiesList.get(i).toString()+"',";
        companies = companies.substring(0,companies.length()-1);
      }

      String sql =
          "select DOC01_SELLING.COMPANY_CODE_SYS01,DOC01_SELLING.DOC_TYPE,DOC01_SELLING.DOC_STATE,DOC01_SELLING.PRICELIST_CODE_SAL01,DOC01_SELLING.PRICELIST_DESCRIPTION,"+
          "DOC01_SELLING.CURRENCY_CODE_REG03,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,DOC01_SELLING.DOC_YEAR,DOC01_SELLING.DOC_NUMBER,DOC01_SELLING.TAXABLE_INCOME,"+
          "DOC01_SELLING.TOTAL_VAT,DOC01_SELLING.TOTAL,DOC01_SELLING.DOC_DATE,SAL07_CUSTOMERS.CUSTOMER_CODE,"+
          "REG03_CURRENCIES.DECIMALS,REG03_CURRENCIES.CURRENCY_SYMBOL,REG03_CURRENCIES.THOUSAND_SYMBOL,REG03_CURRENCIES.DECIMAL_SYMBOL, "+
          "DOC01_SELLING.DOC_SEQUENCE "+
          " from DOC01_SELLING,SAL01_PRICELISTS,SAL07_CUSTOMERS,SYS10_TRANSLATIONS,REG04_SUBJECTS,REG03_CURRENCIES where "+
          "DOC01_SELLING.CURRENCY_CODE_REG03=REG03_CURRENCIES.CURRENCY_CODE and "+
          "DOC01_SELLING.PRICELIST_CODE_SAL01=SAL01_PRICELISTS.PRICELIST_CODE and "+
          "DOC01_SELLING.COMPANY_CODE_SYS01=SAL01_PRICELISTS.COMPANY_CODE_SYS01 and "+
          "SAL01_PRICELISTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "DOC01_SELLING.PROGRESSIVE_REG04=REG04_SUBJECTS.PROGRESSIVE and "+
          "DOC01_SELLING.COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 and "+
          "DOC01_SELLING.PROGRESSIVE_REG04=SAL07_CUSTOMERS.PROGRESSIVE_REG04 and "+
          "DOC01_SELLING.COMPANY_CODE_SYS01=SAL07_CUSTOMERS.COMPANY_CODE_SYS01 and "+
          "DOC01_SELLING.COMPANY_CODE_SYS01 in ("+companies+") and "+
          "DOC01_SELLING.ENABLED='Y' and "+
          "DOC01_SELLING.DOC_SEQUENCE=? ";

      if (pars.getLookupValidationParameters().get(ApplicationConsts.DOC_STATE)!=null) {
        sql += " and DOC01_SELLING.DOC_STATE='"+pars.getLookupValidationParameters().get(ApplicationConsts.DOC_STATE)+"'";
      }
      if (pars.getLookupValidationParameters().get(ApplicationConsts.DOC_TYPE)!=null) {
        sql += " and DOC01_SELLING.DOC_TYPE='"+pars.getLookupValidationParameters().get(ApplicationConsts.DOC_TYPE)+"'";
      }
      if (pars.getLookupValidationParameters().get(ApplicationConsts.DOC_YEAR)!=null) {
        sql += " and DOC01_SELLING.DOC_YEAR="+pars.getLookupValidationParameters().get(ApplicationConsts.DOC_YEAR);
      }
      if (pars.getLookupValidationParameters().get(ApplicationConsts.WAREHOUSE_CODE)!=null) {
        sql += " and DOC01_SELLING.WAREHOUSE_CODE_WAR01='"+pars.getLookupValidationParameters().get(ApplicationConsts.WAREHOUSE_CODE)+"'";
      }

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC01","DOC01_SELLING.COMPANY_CODE_SYS01");
      attribute2dbField.put("docTypeDOC01","DOC01_SELLING.DOC_TYPE");
      attribute2dbField.put("docStateDOC01","DOC01_SELLING.DOC_STATE");
      attribute2dbField.put("pricelistCodeSal01DOC01","DOC01_SELLING.PRICELIST_CODE_SAL01");
      attribute2dbField.put("pricelistDescriptionDOC01","DOC01_SELLING.PRICELIST_DESCRIPTION");
      attribute2dbField.put("currencyCodeReg03DOC01","DOC01_SELLING.CURRENCY_CODE_REG03");
      attribute2dbField.put("name_1REG04","REG04_SUBJECTS.NAME_1");
      attribute2dbField.put("name_2REG04","REG04_SUBJECTS.NAME_2");
      attribute2dbField.put("docYearDOC01","DOC01_SELLING.DOC_YEAR");
      attribute2dbField.put("docNumberDOC01","DOC01_SELLING.DOC_NUMBER");
      attribute2dbField.put("taxableIncomeDOC01","DOC01_SELLING.TAXABLE_INCOME");
      attribute2dbField.put("totalVatDOC01","DOC01_SELLING.TOTAL_VAT");
      attribute2dbField.put("totalDOC01","DOC01_SELLING.TOTAL");
      attribute2dbField.put("docDateDOC01","DOC01_SELLING.DOC_DATE");
      attribute2dbField.put("customerCodeSAL07","SAL07_CUSTOMERS.CUSTOMER_CODE");
      attribute2dbField.put("decimalsREG03","REG03_CURRENCIES.DECIMALS");
      attribute2dbField.put("currencySymbolREG03","REG03_CURRENCIES.CURRENCY_SYMBOL");
      attribute2dbField.put("thousandSymbolREG03","REG03_CURRENCIES.THOUSAND_SYMBOL");
      attribute2dbField.put("decimalSymbolREG03","REG03_CURRENCIES.DECIMAL_SYMBOL");
      attribute2dbField.put("docSequenceDOC01","DOC01_SELLING.DOC_SEQUENCE");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(pars.getCode());

      if (pars.getLookupValidationParameters().get(ApplicationConsts.PROGRESSIVE_REG04)!=null) {
        sql += " and DOC01_SELLING.PROGRESSIVE_REG04=?";
        values.add( pars.getLookupValidationParameters().get(ApplicationConsts.PROGRESSIVE_REG04) );
      }


      // read from DOC01 table...
      Response answer = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          GridSaleDocVO.class,
          "Y",
          "N",
          context,
          new GridParams(),
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching sale orders list",ex);
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        pstmt.close();
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
