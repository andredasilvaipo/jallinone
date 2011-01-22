package org.jallinone.sales.documents.server;

import org.openswing.swing.server.*;

import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.message.send.java.LookupValidationParams;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.registers.currency.java.CurrencyVO;
import org.jallinone.registers.currency.server.CurrenciesBean;
import org.jallinone.registers.payments.java.PaymentVO;
import org.jallinone.registers.payments.server.PaymentsBean;
import org.jallinone.sales.documents.java.*;
import org.jallinone.system.progressives.server.CompanyProgressiveUtils;
import org.jallinone.system.server.*;

import java.math.*;

import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage sales documents.</p>
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
public class LoadSaleDocBean implements LoadSaleDoc {


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
  
	  
	  /**
	   * Retrieve a sale document header.
	   * No commit or rollback are executed; no connection is created or released.
	   */
	  public DetailSaleDocVO loadSaleDoc(SaleDocPK pk,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable{
	    Statement stmt = null;
	    Connection conn = null;
	    try {
	      if (this.conn==null) conn = getConn(); else conn = this.conn;

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
	      attribute2dbField.put("allowanceDOC01","DOC01_SELLING.ALLOWANCE");
	      attribute2dbField.put("depositDOC01","DOC01_SELLING.DEPOSIT");
	      attribute2dbField.put("totalDOC01","DOC01_SELLING.TOTAL");
	      attribute2dbField.put("docDateDOC01","DOC01_SELLING.DOC_DATE");
	      attribute2dbField.put("customerCodeSAL07","SAL07_CUSTOMERS.CUSTOMER_CODE");

	      attribute2dbField.put("progressiveReg04DOC01","DOC01_SELLING.PROGRESSIVE_REG04");
	      attribute2dbField.put("companyCodeSys01Doc01DOC01","DOC01_SELLING.COMPANY_CODE_SYS01_DOC01");
	      attribute2dbField.put("docTypeDoc01DOC01","DOC01_SELLING.DOC_TYPE_DOC01");
	      attribute2dbField.put("docYearDoc01DOC01","DOC01_SELLING.DOC_YEAR_DOC01");
	      attribute2dbField.put("docNumberDoc01DOC01","DOC01_SELLING.DOC_NUMBER_DOC01");
	      attribute2dbField.put("paymentCodeReg10DOC01","DOC01_SELLING.PAYMENT_CODE_REG10");
	      attribute2dbField.put("paymentDescriptionDOC01","DOC01_SELLING.PAYMENT_DESCRIPTION");
	      attribute2dbField.put("instalmentNumberDOC01","DOC01_SELLING.INSTALMENT_NUMBER");
	      attribute2dbField.put("stepDOC01","DOC01_SELLING.STEP");
	      attribute2dbField.put("startDayDOC01","DOC01_SELLING.START_DAY");
	      attribute2dbField.put("firstInstalmentDaysDOC01","DOC01_SELLING.FIRST_INSTALMENT_DAYS");
	      attribute2dbField.put("paymentTypeDescriptionDOC01","DOC01_SELLING.PAYMENT_TYPE_DESCRIPTION");
	      attribute2dbField.put("progressiveWkf01DOC01","DOC01_SELLING.PROGRESSIVE_WKF01");
	      attribute2dbField.put("progressiveWkf08DOC01","DOC01_SELLING.PROGRESSIVE_WKF08");
	      attribute2dbField.put("descriptionWkf01DOC01","DOC01_SELLING.DESCRIPTION_WKF01");
	      attribute2dbField.put("noteDOC01","DOC01_SELLING.NOTE");
	      attribute2dbField.put("headingNoteDOC01","DOC01_SELLING.HEADING_NOTE");
	      attribute2dbField.put("footerNoteDOC01","DOC01_SELLING.FOOTER_NOTE");
	      attribute2dbField.put("deliveryNoteDOC01","DOC01_SELLING.DELIVERY_NOTE");
	      attribute2dbField.put("enabledDOC01","DOC01_SELLING.ENABLED");
	      attribute2dbField.put("docRefNumberDOC01","DOC01_SELLING.DOC_REF_NUMBER");
	      attribute2dbField.put("agentCodeSal10DOC01","DOC01_SELLING.AGENT_CODE_SAL10");
	      attribute2dbField.put("name_2DOC01","DOC01_SELLING.NAME_1");
	      attribute2dbField.put("name_1DOC01","DOC01_SELLING.NAME_2");
	      attribute2dbField.put("percentageDOC01","DOC01_SELLING.PERCENTAGE");

	      attribute2dbField.put("currencySymbolREG03","REG03_CURRENCIES.CURRENCY_SYMBOL");
	      attribute2dbField.put("decimalSymbolREG03","REG03_CURRENCIES.DECIMAL_SYMBOL");
	      attribute2dbField.put("thousandSymbolREG03","REG03_CURRENCIES.THOUSAND_SYMBOL");
	      attribute2dbField.put("decimalsREG03","REG03_CURRENCIES.DECIMALS");
	      attribute2dbField.put("warehouseCodeWar01DOC01","DOC01_SELLING.WAREHOUSE_CODE_WAR01");
	      attribute2dbField.put("descriptionWar01DOC01","DOC01_SELLING.DESCRIPTION_WAR01");
	      attribute2dbField.put("destinationCodeReg18DOC01","DOC01_SELLING.DESTINATION_CODE_REG18");
	      attribute2dbField.put("descriptionDOC01","DOC01_SELLING.DESCRIPTION");
	      attribute2dbField.put("addressDOC01","DOC01_SELLING.ADDRESS");
	      attribute2dbField.put("cityDOC01","DOC01_SELLING.CITY");
	      attribute2dbField.put("provinceDOC01","DOC01_SELLING.PROVINCE");
	      attribute2dbField.put("zipDOC01","DOC01_SELLING.ZIP");
	      attribute2dbField.put("countryDOC01","DOC01_SELLING.COUNTRY");
	      attribute2dbField.put("customerVatCodeReg01DOC01","DOC01_SELLING.CUSTOMER_VAT_CODE_REG01");
	      attribute2dbField.put("progressiveHie02WAR01","WAR01_WAREHOUSES.PROGRESSIVE_HIE02");
	      attribute2dbField.put("progressiveHie01HIE02","HIE02_HIERARCHIES.PROGRESSIVE_HIE01");
	      attribute2dbField.put("discountValueDOC01","DOC01_SELLING.DISCOUNT_VALUE");
	      attribute2dbField.put("discountPercDOC01","DOC01_SELLING.DISCOUNT_PERC");
	      attribute2dbField.put("docSequenceDOC01","DOC01_SELLING.DOC_SEQUENCE");
	      attribute2dbField.put("docSequenceDoc01DOC01","DOC01_SELLING.DOC_SEQUENCE_DOC01");
	      attribute2dbField.put("sectionalDOC01","DOC01_SELLING.SECTIONAL");

	      attribute2dbField.put("deliveryDateDOC01","DOC01_SELLING.DELIVERY_DATE");

	      attribute2dbField.put("valueREG01","REG01_VATS.VALUE");
	      attribute2dbField.put("deductibleREG01","REG01_VATS.DEDUCTIBLE");
	      attribute2dbField.put("vatDescriptionSYS10","REG01_VATS.DESCRIPTION");

	      HashSet pkAttributes = new HashSet();
	      pkAttributes.add("companyCodeSys01DOC01");
	      pkAttributes.add("docTypeDOC01");
	      pkAttributes.add("docYearDOC01");
	      pkAttributes.add("docNumberDOC01");

	      String baseSQL =
	          "select DOC01_SELLING.COMPANY_CODE_SYS01,DOC01_SELLING.DOC_TYPE,DOC01_SELLING.DOC_STATE,DOC01_SELLING.PRICELIST_CODE_SAL01,DOC01_SELLING.PRICELIST_DESCRIPTION,"+
	          "DOC01_SELLING.CURRENCY_CODE_REG03,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,DOC01_SELLING.DOC_YEAR,DOC01_SELLING.DOC_NUMBER,DOC01_SELLING.TAXABLE_INCOME,"+
	          "DOC01_SELLING.ALLOWANCE,DOC01_SELLING.DEPOSIT,DOC01_SELLING.TOTAL_VAT,DOC01_SELLING.TOTAL,DOC01_SELLING.DOC_DATE,SAL07_CUSTOMERS.CUSTOMER_CODE,"+
	          "DOC01_SELLING.PROGRESSIVE_REG04,DOC01_SELLING.COMPANY_CODE_SYS01_DOC01,DOC01_SELLING.DOC_TYPE_DOC01, "+
	          "DOC01_SELLING.DOC_YEAR_DOC01,DOC01_SELLING.DOC_NUMBER_DOC01,DOC01_SELLING.PAYMENT_CODE_REG10,DOC01_SELLING.PAYMENT_DESCRIPTION, "+
	          "DOC01_SELLING.INSTALMENT_NUMBER,DOC01_SELLING.STEP,DOC01_SELLING.START_DAY,DOC01_SELLING.FIRST_INSTALMENT_DAYS, "+
	          "DOC01_SELLING.PAYMENT_TYPE_DESCRIPTION,DOC01_SELLING.PROGRESSIVE_WKF01,DOC01_SELLING.PROGRESSIVE_WKF08, "+
	          "DOC01_SELLING.DESCRIPTION_WKF01,DOC01_SELLING.NOTE,DOC01_SELLING.ENABLED,DOC01_SELLING.DOC_REF_NUMBER, "+
	          "DOC01_SELLING.HEADING_NOTE,DOC01_SELLING.FOOTER_NOTE,DOC01_SELLING.DELIVERY_NOTE,"+
	          "DOC01_SELLING.DESTINATION_CODE_REG18,DOC01_SELLING.DESCRIPTION,DOC01_SELLING.DISCOUNT_VALUE,DOC01_SELLING.DISCOUNT_PERC,"+
	          "DOC01_SELLING.AGENT_CODE_SAL10,DOC01_SELLING.NAME_1,DOC01_SELLING.NAME_2,DOC01_SELLING.PERCENTAGE,"+
	          "REG03_CURRENCIES.CURRENCY_SYMBOL,REG03_CURRENCIES.DECIMAL_SYMBOL,REG03_CURRENCIES.THOUSAND_SYMBOL,REG03_CURRENCIES.DECIMALS,"+
	          "DOC01_SELLING.WAREHOUSE_CODE_WAR01,DOC01_SELLING.DESCRIPTION_WAR01,DOC01_SELLING.ADDRESS,DOC01_SELLING.CITY,DOC01_SELLING.PROVINCE,DOC01_SELLING.ZIP,"+
	          "DOC01_SELLING.COUNTRY,DOC01_SELLING.CUSTOMER_VAT_CODE_REG01,WAR01_WAREHOUSES.PROGRESSIVE_HIE02,"+
	          "DOC01_SELLING.DOC_SEQUENCE,DOC01_SELLING.DOC_SEQUENCE_DOC01,DOC01_SELLING.SECTIONAL,"+
	          "HIE02_HIERARCHIES.PROGRESSIVE_HIE01,DOC01_SELLING.DELIVERY_DATE, "+
	          "REG01_VATS.VALUE,REG01_VATS.DEDUCTIBLE,REG01_VATS.DESCRIPTION "+
	          " from SAL01_PRICELISTS,SYS10_TRANSLATIONS,REG04_SUBJECTS,SAL07_CUSTOMERS,REG03_CURRENCIES,"+
	          "WAR01_WAREHOUSES,HIE02_HIERARCHIES,DOC01_SELLING "+
	          "LEFT OUTER JOIN "+
	          "(select REG01_VATS.VAT_CODE,REG01_VATS.VALUE,REG01_VATS.DEDUCTIBLE,SYS10_TRANSLATIONS.DESCRIPTION "+
	          " from REG01_VATS,SYS10_TRANSLATIONS where "+
	          " REG01_VATS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and SYS10_TRANSLATIONS.LANGUAGE_CODE=?) REG01_VATS ON "+
	          "REG01_VATS.VAT_CODE=DOC01_SELLING.CUSTOMER_VAT_CODE_REG01 "+
	          "where "+
	          "DOC01_SELLING.PRICELIST_CODE_SAL01=SAL01_PRICELISTS.PRICELIST_CODE and "+
	          "DOC01_SELLING.COMPANY_CODE_SYS01=SAL01_PRICELISTS.COMPANY_CODE_SYS01 and "+
	          "SAL01_PRICELISTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
	          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
	          "DOC01_SELLING.PROGRESSIVE_REG04=REG04_SUBJECTS.PROGRESSIVE and "+
	          "DOC01_SELLING.COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 and "+
	          "DOC01_SELLING.PROGRESSIVE_REG04=SAL07_CUSTOMERS.PROGRESSIVE_REG04 and "+
	          "DOC01_SELLING.COMPANY_CODE_SYS01=SAL07_CUSTOMERS.COMPANY_CODE_SYS01 and "+
	          "DOC01_SELLING.COMPANY_CODE_SYS01=? and "+
	          "DOC01_SELLING.DOC_TYPE=? and "+
	          "DOC01_SELLING.DOC_YEAR=? and "+
	          "DOC01_SELLING.DOC_NUMBER=? and "+
	          "DOC01_SELLING.CURRENCY_CODE_REG03=REG03_CURRENCIES.CURRENCY_CODE and "+
	          "DOC01_SELLING.COMPANY_CODE_SYS01=WAR01_WAREHOUSES.COMPANY_CODE_SYS01 and "+
	          "DOC01_SELLING.WAREHOUSE_CODE_WAR01=WAR01_WAREHOUSES.WAREHOUSE_CODE and "+
	          "WAR01_WAREHOUSES.PROGRESSIVE_HIE02=HIE02_HIERARCHIES.PROGRESSIVE";

	      ArrayList values = new ArrayList();
	      values.add(serverLanguageId);
	      values.add(serverLanguageId);
	      values.add(pk.getCompanyCodeSys01DOC01());
	      values.add(pk.getDocTypeDOC01());
	      values.add(pk.getDocYearDOC01());
	      values.add(pk.getDocNumberDOC01());

	      // read from DOC01 table...
	      Response res = CustomizeQueryUtil.getQuery(
	          conn,
	          new UserSessionParameters(username),
	          baseSQL,
	          values,
	          attribute2dbField,
	          DetailSaleDocVO.class,
	          "Y",
	          "N",
	          null,
	          true,
	          customizedFields
	      );

	      Response answer = res;
	      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (DetailSaleDocVO)((VOResponse)answer).getVo();
	    }
	    catch (Throwable ex) {
	      Logger.error(username,this.getClass().getName(),"loadSaleDoc","Error while fetching an existing sale order",ex);
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
