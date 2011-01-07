package org.jallinone.purchases.documents.server;

import javax.sql.DataSource;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
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
import org.jallinone.system.java.*;
import org.jallinone.commons.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Helper class used to fetch a specific purchase order from DOC06 table.</p>
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
public class LoadPurchaseDocBean implements LoadPurchaseDoc {


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




  public LoadPurchaseDocBean() {
  }


  /**
   * Load purtchase document, based on the Connection passed as argument.
   * This method does not create or release a connection; it does not commit or rollback.
   */
  public DetailPurchaseDocVO loadPurchaseDoc(PurchaseDocPK pk,String serverLanguageId,String username,ArrayList customizedFields)  throws Throwable{
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC06","DOC06_PURCHASE.COMPANY_CODE_SYS01");
      attribute2dbField.put("docTypeDOC06","DOC06_PURCHASE.DOC_TYPE");
      attribute2dbField.put("docStateDOC06","DOC06_PURCHASE.DOC_STATE");
      attribute2dbField.put("pricelistCodePur03DOC06","DOC06_PURCHASE.PRICELIST_CODE_PUR03");
      attribute2dbField.put("pricelistDescriptionDOC06","DOC06_PURCHASE.PRICELIST_DESCRIPTION");
      attribute2dbField.put("currencyCodeReg03DOC06","DOC06_PURCHASE.CURRENCY_CODE_REG03");
      attribute2dbField.put("name_1REG04","REG04_SUBJECTS.NAME_1");
      attribute2dbField.put("name_2REG04","REG04_SUBJECTS.NAME_2");
      attribute2dbField.put("docYearDOC06","DOC06_PURCHASE.DOC_YEAR");
      attribute2dbField.put("docNumberDOC06","DOC06_PURCHASE.DOC_NUMBER");
      attribute2dbField.put("taxableIncomeDOC06","DOC06_PURCHASE.TAXABLE_INCOME");
      attribute2dbField.put("totalVatDOC06","DOC06_PURCHASE.TOTAL_VAT");
      attribute2dbField.put("totalDOC06","DOC06_PURCHASE.TOTAL");
      attribute2dbField.put("docDateDOC06","DOC06_PURCHASE.DOC_DATE");
      attribute2dbField.put("supplierCodePUR01","PUR01_SUPPLIERS.SUPPLIER_CODE");

      attribute2dbField.put("progressiveReg04DOC06","DOC06_PURCHASE.PROGRESSIVE_REG04");
      attribute2dbField.put("companyCodeSys01Doc06DOC06","DOC06_PURCHASE.COMPANY_CODE_SYS01_DOC06");
      attribute2dbField.put("docTypeDoc06DOC06","DOC06_PURCHASE.DOC_TYPE_DOC06");
      attribute2dbField.put("docYearDoc06DOC06","DOC06_PURCHASE.DOC_YEAR_DOC06");
      attribute2dbField.put("docNumberDoc06DOC06","DOC06_PURCHASE.DOC_NUMBER_DOC06");
      attribute2dbField.put("discountValueDOC06","DOC06_PURCHASE.DISCOUNT_VALUE");
      attribute2dbField.put("discountPercDOC06","DOC06_PURCHASE.DISCOUNT_PERC");
      attribute2dbField.put("chargeValueDOC06","DOC06_PURCHASE.CHARGE_VALUE");
      attribute2dbField.put("chargePercDOC06","DOC06_PURCHASE.CHARGE_PERC");
      attribute2dbField.put("paymentCodeReg10DOC06","DOC06_PURCHASE.PAYMENT_CODE_REG10");
      attribute2dbField.put("paymentDescriptionDOC06","DOC06_PURCHASE.PAYMENT_DESCRIPTION");
      attribute2dbField.put("instalmentNumberDOC06","DOC06_PURCHASE.INSTALMENT_NUMBER");
      attribute2dbField.put("stepDOC06","DOC06_PURCHASE.STEP");
      attribute2dbField.put("startDayDOC06","DOC06_PURCHASE.START_DAY");
      attribute2dbField.put("firstInstalmentDaysDOC06","DOC06_PURCHASE.FIRST_INSTALMENT_DAYS");
      attribute2dbField.put("paymentTypeDescriptionDOC06","DOC06_PURCHASE.PAYMENT_TYPE_DESCRIPTION");
      attribute2dbField.put("progressiveWkf01DOC06","DOC06_PURCHASE.PROGRESSIVE_WKF01");
      attribute2dbField.put("progressiveWkf08DOC06","DOC06_PURCHASE.PROGRESSIVE_WKF08");
      attribute2dbField.put("descriptionWkf01DOC06","DOC06_PURCHASE.DESCRIPTION_WKF01");
      attribute2dbField.put("noteDOC06","DOC06_PURCHASE.NOTE");
      attribute2dbField.put("enabledDOC06","DOC06_PURCHASE.ENABLED");

      attribute2dbField.put("currencySymbolREG03","REG03_CURRENCIES.CURRENCY_SYMBOL");
      attribute2dbField.put("decimalSymbolREG03","REG03_CURRENCIES.DECIMAL_SYMBOL");
      attribute2dbField.put("thousandSymbolREG03","REG03_CURRENCIES.THOUSAND_SYMBOL");
      attribute2dbField.put("decimalsREG03","REG03_CURRENCIES.DECIMALS");
      attribute2dbField.put("warehouseCodeWar01DOC06","DOC06_PURCHASE.WAREHOUSE_CODE_WAR01");
      attribute2dbField.put("descriptionWar01DOC06","DOC06_PURCHASE.DESCRIPTION_WAR01");
      attribute2dbField.put("addressWar01DOC06","DOC06_PURCHASE.ADDRESS_WAR01");
      attribute2dbField.put("cityWar01DOC06","DOC06_PURCHASE.CITY_WAR01");
      attribute2dbField.put("provinceWar01DOC06","DOC06_PURCHASE.PROVINCE_WAR01");
      attribute2dbField.put("zipWar01DOC06","DOC06_PURCHASE.ZIP_WAR01");
      attribute2dbField.put("countryWar01DOC06","DOC06_PURCHASE.COUNTRY_WAR01");
      attribute2dbField.put("docSequenceDOC06","DOC06_PURCHASE.DOC_SEQUENCE");
      attribute2dbField.put("docSequenceDoc06DOC06","DOC06_PURCHASE.DOC_SEQUENCE_DOC06");

      HashSet pkAttributes = new HashSet();
      pkAttributes.add("companyCodeSys01DOC06");
      pkAttributes.add("docTypeDOC06");
      pkAttributes.add("docYearDOC06");
      pkAttributes.add("docNumberDOC06");

      String baseSQL =
          "select DOC06_PURCHASE.COMPANY_CODE_SYS01,DOC06_PURCHASE.DOC_TYPE,DOC06_PURCHASE.DOC_STATE,DOC06_PURCHASE.PRICELIST_CODE_PUR03,DOC06_PURCHASE.PRICELIST_DESCRIPTION,"+
          "DOC06_PURCHASE.CURRENCY_CODE_REG03,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,DOC06_PURCHASE.DOC_YEAR,DOC06_PURCHASE.DOC_NUMBER,DOC06_PURCHASE.TAXABLE_INCOME,"+
          "DOC06_PURCHASE.TOTAL_VAT,DOC06_PURCHASE.TOTAL,DOC06_PURCHASE.DOC_DATE,PUR01_SUPPLIERS.SUPPLIER_CODE,"+
          "DOC06_PURCHASE.PROGRESSIVE_REG04,DOC06_PURCHASE.COMPANY_CODE_SYS01_DOC06,DOC06_PURCHASE.DOC_TYPE_DOC06, "+
          "DOC06_PURCHASE.DOC_YEAR_DOC06,DOC06_PURCHASE.DOC_NUMBER_DOC06,DOC06_PURCHASE.DISCOUNT_VALUE,DOC06_PURCHASE.DISCOUNT_PERC, "+
          "DOC06_PURCHASE.CHARGE_VALUE,DOC06_PURCHASE.CHARGE_PERC,DOC06_PURCHASE.PAYMENT_CODE_REG10,DOC06_PURCHASE.PAYMENT_DESCRIPTION, "+
          "DOC06_PURCHASE.INSTALMENT_NUMBER,DOC06_PURCHASE.STEP,DOC06_PURCHASE.START_DAY,DOC06_PURCHASE.FIRST_INSTALMENT_DAYS, "+
          "DOC06_PURCHASE.PAYMENT_TYPE_DESCRIPTION,DOC06_PURCHASE.PROGRESSIVE_WKF01,DOC06_PURCHASE.PROGRESSIVE_WKF08, "+
          "DOC06_PURCHASE.DESCRIPTION_WKF01,DOC06_PURCHASE.NOTE,DOC06_PURCHASE.ENABLED, "+
          "REG03_CURRENCIES.CURRENCY_SYMBOL,REG03_CURRENCIES.DECIMAL_SYMBOL,REG03_CURRENCIES.THOUSAND_SYMBOL,REG03_CURRENCIES.DECIMALS,"+
          "DOC06_PURCHASE.WAREHOUSE_CODE_WAR01,DOC06_PURCHASE.DESCRIPTION_WAR01,DOC06_PURCHASE.ADDRESS_WAR01,DOC06_PURCHASE.CITY_WAR01,DOC06_PURCHASE.PROVINCE_WAR01,DOC06_PURCHASE.ZIP_WAR01,DOC06_PURCHASE.COUNTRY_WAR01, "+
          "DOC06_PURCHASE.DOC_SEQUENCE,DOC06_PURCHASE.DOC_SEQUENCE_DOC06 "+
          " from DOC06_PURCHASE,PUR03_SUPPLIER_PRICELISTS,SYS10_TRANSLATIONS,REG04_SUBJECTS,PUR01_SUPPLIERS,REG03_CURRENCIES where "+
          "DOC06_PURCHASE.PRICELIST_CODE_PUR03=PUR03_SUPPLIER_PRICELISTS.PRICELIST_CODE and "+
          "DOC06_PURCHASE.PROGRESSIVE_REG04=PUR03_SUPPLIER_PRICELISTS.PROGRESSIVE_REG04 and "+
          "DOC06_PURCHASE.COMPANY_CODE_SYS01=PUR03_SUPPLIER_PRICELISTS.COMPANY_CODE_SYS01 and "+
          "PUR03_SUPPLIER_PRICELISTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "DOC06_PURCHASE.PROGRESSIVE_REG04=REG04_SUBJECTS.PROGRESSIVE and "+
          "DOC06_PURCHASE.COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 and "+
          "DOC06_PURCHASE.PROGRESSIVE_REG04=PUR01_SUPPLIERS.PROGRESSIVE_REG04 and "+
          "DOC06_PURCHASE.COMPANY_CODE_SYS01=PUR01_SUPPLIERS.COMPANY_CODE_SYS01 and "+
          "DOC06_PURCHASE.COMPANY_CODE_SYS01=? and "+
          "DOC06_PURCHASE.DOC_TYPE=? and "+
          "DOC06_PURCHASE.DOC_YEAR=? and "+
          "DOC06_PURCHASE.DOC_NUMBER=? and "+
          "DOC06_PURCHASE.CURRENCY_CODE_REG03=REG03_CURRENCIES.CURRENCY_CODE ";

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(pk.getCompanyCodeSys01DOC06());
      values.add(pk.getDocTypeDOC06());
      values.add(pk.getDocYearDOC06());
      values.add(pk.getDocNumberDOC06());

      // read from DOC06 table...
      Response res = CustomizeQueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          baseSQL,
          values,
          attribute2dbField,
          DetailPurchaseDocVO.class,
          "Y",
          "N",
          null,
          true,
          customizedFields
      );


      Response answer = res;
      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (DetailPurchaseDocVO)((VOResponse)answer).getVo();
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"loadPurchaseDoc","Error while fetching an existing purchase order",ex);
      throw new Exception(ex.getMessage());
    }
    finally {
        try {
            stmt.close();
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

