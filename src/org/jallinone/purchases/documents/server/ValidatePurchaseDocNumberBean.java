package org.jallinone.purchases.documents.server;

import org.openswing.swing.server.*;
import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.purchases.documents.java.*;
import org.jallinone.system.server.*;
import org.jallinone.commons.server.*;
import java.math.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.commons.java.*;
import org.jallinone.hierarchies.java.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to validate a purchase doc number from DOC06 table, based on the specified COMPANY_CODE.</p>
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
public class ValidatePurchaseDocNumberBean  implements ValidatePurchaseDocNumber {


  private DataSource dataSource; 

  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  /** external connection */
  private Connection conn = null;
  
  /**
   * Set external connection. 
   */
  public void setConn(Connection conn) {
    this.conn = conn;
  }

  /**
   * Create local connection
   */
  public Connection getConn() throws Exception {
    
    Connection c = dataSource.getConnection(); c.setAutoCommit(false); return c;
  }




  public ValidatePurchaseDocNumberBean() {
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public GridPurchaseDocVO getGridPurchaseDoc() {
	  throw new UnsupportedOperationException();
  }
  

  /**
   * Business logic to execute.
   */
  public VOListResponse validatePurchaseDocNumber(LookupValidationParams pars,String serverLanguageId,String username,ArrayList companiesList) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      // retrieve companies list...
      String companies = "";
      if (pars.getLookupValidationParameters().get(ApplicationConsts.COMPANY_CODE_SYS01)!=null) {
        companies = "'"+pars.getLookupValidationParameters().get(ApplicationConsts.COMPANY_CODE_SYS01)+"'";
      }
      else {
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
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          GridPurchaseDocVO.class,
          "Y",
          "N",
          null,
          new GridParams(),
          50,
          true
      );
      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching purchase orders list",ex);
      throw new Exception(ex.getMessage());
    }
    finally {
        try {
            pstmt.close();
        }
        catch (Exception exx) {}
        try {
            if (this.conn==null && conn!=null) {
                // close only local connection
                conn.commit();
                conn.close();
            }

        }
        catch (Exception exx) {}
    }


  }



}

