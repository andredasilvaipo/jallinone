package org.jallinone.purchases.documents.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.purchases.documents.java.*;
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
 * <p>Description: Action class used to validate a purchase doc number from DOC06 table, based on the specified COMPANY_CODE.</p>
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
public class ValidatePurchaseDocNumberAction implements Action {


  public ValidatePurchaseDocNumberAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "validatePurchaseDocNumber";
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
        ArrayList companiesList = ((JAIOUserSessionParameters)userSessionPars).getCompanyBa().getCompaniesList("DOC06_ORDERS");
        for(int i=0;i<companiesList.size();i++)
          companies += "'"+companiesList.get(i).toString()+"',";
        companies = companies.substring(0,companies.length()-1);
      }

      String sql =
          "select DOC06_PURCHASE.COMPANY_CODE_SYS01,DOC06_PURCHASE.DOC_TYPE,DOC06_PURCHASE.DOC_STATE,DOC06_PURCHASE.PRICELIST_CODE_PUR03,DOC06_PURCHASE.PRICELIST_DESCRIPTION,"+
          "DOC06_PURCHASE.CURRENCY_CODE_REG03,REG04_SUBJECTS.NAME_1,DOC06_PURCHASE.DOC_YEAR,DOC06_PURCHASE.DOC_NUMBER,DOC06_PURCHASE.TAXABLE_INCOME,"+
          "DOC06_PURCHASE.TOTAL_VAT,DOC06_PURCHASE.TOTAL,DOC06_PURCHASE.DOC_DATE,PUR01_SUPPLIERS.SUPPLIER_CODE,"+
          "REG03_CURRENCIES.DECIMALS,REG03_CURRENCIES.CURRENCY_SYMBOL,REG03_CURRENCIES.THOUSAND_SYMBOL,REG03_CURRENCIES.DECIMAL_SYMBOL, "+
          "DOC06_PURCHASE.DOC_SEQUENCE "+
          " from DOC06_PURCHASE,PUR03_SUPPLIER_PRICELISTS,SYS10_TRANSLATIONS,REG04_SUBJECTS,PUR01_SUPPLIERS,REG03_CURRENCIES where "+
          "DOC06_PURCHASE.CURRENCY_CODE_REG03=REG03_CURRENCIES.CURRENCY_CODE and "+
          "DOC06_PURCHASE.PRICELIST_CODE_PUR03=PUR03_SUPPLIER_PRICELISTS.PRICELIST_CODE and "+
          "DOC06_PURCHASE.PROGRESSIVE_REG04=PUR03_SUPPLIER_PRICELISTS.PROGRESSIVE_REG04 and "+
          "DOC06_PURCHASE.COMPANY_CODE_SYS01=PUR03_SUPPLIER_PRICELISTS.COMPANY_CODE_SYS01 and "+
          "PUR03_SUPPLIER_PRICELISTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "DOC06_PURCHASE.PROGRESSIVE_REG04=REG04_SUBJECTS.PROGRESSIVE and "+
          "DOC06_PURCHASE.COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 and "+
          "DOC06_PURCHASE.PROGRESSIVE_REG04=PUR01_SUPPLIERS.PROGRESSIVE_REG04 and "+
          "DOC06_PURCHASE.COMPANY_CODE_SYS01=PUR01_SUPPLIERS.COMPANY_CODE_SYS01 and "+
          "DOC06_PURCHASE.COMPANY_CODE_SYS01 in ("+companies+") and "+
          "DOC06_PURCHASE.ENABLED='Y' and "+
          "DOC06_PURCHASE.DOC_SEQUENCE=? and "+
          "DOC06_PURCHASE.DOC_STATE=? ";

      if (pars.getLookupValidationParameters().get(ApplicationConsts.DOC_STATE)!=null) {
        sql += " and DOC06_PURCHASE.DOC_STATE='"+pars.getLookupValidationParameters().get(ApplicationConsts.DOC_STATE)+"'";
      }
      if (pars.getLookupValidationParameters().get(ApplicationConsts.DOC_YEAR)!=null) {
        sql += " and DOC06_PURCHASE.DOC_YEAR="+pars.getLookupValidationParameters().get(ApplicationConsts.DOC_YEAR);
      }
      if (pars.getLookupValidationParameters().get(ApplicationConsts.WAREHOUSE_CODE)!=null) {
        sql += " and DOC06_PURCHASE.WAREHOUSE_CODE_WAR01='"+pars.getLookupValidationParameters().get(ApplicationConsts.WAREHOUSE_CODE)+"'";
      }

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC06","DOC06_PURCHASE.COMPANY_CODE_SYS01");
      attribute2dbField.put("docTypeDOC06","DOC06_PURCHASE.DOC_TYPE");
      attribute2dbField.put("docStateDOC06","DOC06_PURCHASE.DOC_STATE");
      attribute2dbField.put("pricelistCodePur03DOC06","DOC06_PURCHASE.PRICELIST_CODE_PUR03");
      attribute2dbField.put("pricelistDescriptionDOC06","DOC06_PURCHASE.PRICELIST_DESCRIPTION");
      attribute2dbField.put("currencyCodeReg03DOC06","DOC06_PURCHASE.CURRENCY_CODE_REG03");
      attribute2dbField.put("name_1REG04","REG04_SUBJECTS.NAME_1");
      attribute2dbField.put("docYearDOC06","DOC06_PURCHASE.DOC_YEAR");
      attribute2dbField.put("docNumberDOC06","DOC06_PURCHASE.DOC_NUMBER");
      attribute2dbField.put("taxableIncomeDOC06","DOC06_PURCHASE.TAXABLE_INCOME");
      attribute2dbField.put("totalVatDOC06","DOC06_PURCHASE.TOTAL_VAT");
      attribute2dbField.put("totalDOC06","DOC06_PURCHASE.TOTAL");
      attribute2dbField.put("docDateDOC06","DOC06_PURCHASE.DOC_DATE");
      attribute2dbField.put("supplierCodePUR01","PUR01_SUPPLIERS.SUPPLIER_CODE");
      attribute2dbField.put("decimalsREG03","REG03_CURRENCIES.DECIMALS");
      attribute2dbField.put("currencySymbolREG03","REG03_CURRENCIES.CURRENCY_SYMBOL");
      attribute2dbField.put("thousandSymbolREG03","REG03_CURRENCIES.THOUSAND_SYMBOL");
      attribute2dbField.put("decimalSymbolREG03","REG03_CURRENCIES.DECIMAL_SYMBOL");
      attribute2dbField.put("docSequenceDOC06","DOC06_PURCHASE.DOC_SEQUENCE");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(pars.getCode());
      values.add(ApplicationConsts.CONFIRMED);

      if (pars.getLookupValidationParameters().get(ApplicationConsts.PROGRESSIVE_REG04)!=null) {
        sql += " and DOC06_PURCHASE.PROGRESSIVE_REG04=?";
        values.add( pars.getLookupValidationParameters().get(ApplicationConsts.PROGRESSIVE_REG04) );
      }


      // read from DOC06 table...
      Response answer = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          GridPurchaseDocVO.class,
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching purchase orders list",ex);
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
