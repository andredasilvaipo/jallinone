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
public class SaleDocsBean  implements SaleDocs {


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


  private PaymentsBean payBean;

  public void setPayBean(PaymentsBean payBean) {
    this.payBean = payBean;
  }

  private CurrenciesBean currBean;

  public void setCurrBean(CurrenciesBean currBean) {
    this.currBean = currBean;
  }



  private ParamsBean userBean;

  public void setUserBean(ParamsBean userBean) {
    this.userBean = userBean;
  }

  private UpdateTaxableIncomesBean totals;

  public void setTotals(UpdateTaxableIncomesBean totals) {
    this.totals = totals;
  }

  private LoadSaleDocBean bean;

  public void setBean(LoadSaleDocBean bean) {
	  this.bean = bean;
  }


  public SaleDocsBean() {
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
   */
  public GridSaleDocVO getGridSaleDoc() {
	  throw new UnsupportedOperationException();
  }


  /**
   * Business logic to execute.
   */
  public VOResponse updateSaleDoc(
      HashMap variant1Descriptions,
      HashMap variant2Descriptions,
      HashMap variant3Descriptions,
      HashMap variant4Descriptions,
      HashMap variant5Descriptions,
      DetailSaleDocVO oldVO, DetailSaleDocVO newVO, String serverLanguageId,
      String username, ArrayList customizedFields) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      payBean.setConn(conn); // use same transaction...
      currBean.setConn(conn); // use same transaction...
      userBean.setConn(conn); // use same transaction...
      totals.setConn(conn); // use same transaction...
      bean.setConn(conn);

      // retrieve payment info...
			ArrayList companiesList = new ArrayList();
			companiesList.add(newVO.getCompanyCodeSys01DOC01());
      LookupValidationParams pars = new LookupValidationParams(newVO.getPaymentCodeReg10DOC01(),new HashMap());
      Response payResponse = payBean.validatePaymentCode(pars,serverLanguageId,username,new ArrayList(),companiesList);
      if (payResponse.isError())
        throw new Exception(payResponse.getErrorMessage());
      PaymentVO payVO = (PaymentVO)((VOListResponse)payResponse).getRows().get(0);
      newVO.setFirstInstalmentDaysDOC01(payVO.getFirstInstalmentDaysREG10());
      newVO.setInstalmentNumberDOC01(payVO.getInstalmentNumberREG10());
      newVO.setPaymentTypeDescriptionDOC01(payVO.getPaymentTypeDescriptionSYS10());
      newVO.setStartDayDOC01(payVO.getStartDayREG10());
      newVO.setStepDOC01(payVO.getStepREG10());

      // retrieve currency info...
      pars = new LookupValidationParams(newVO.getCurrencyCodeReg03DOC01(),new HashMap());
      Response currResponse = currBean.validateCurrencyCode(pars,serverLanguageId,username,new ArrayList());
      if (currResponse.isError())
        throw new Exception(currResponse.getErrorMessage());

      CurrencyVO currVO = (CurrencyVO)((VOListResponse)currResponse).getRows().get(0);
      newVO.setCurrencySymbolREG03(currVO.getCurrencySymbolREG03());
      newVO.setDecimalSymbolREG03(currVO.getDecimalSymbolREG03());
      newVO.setThousandSymbolREG03(currVO.getThousandSymbolREG03());
      newVO.setDecimalsREG03(currVO.getDecimalsREG03());

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC01","COMPANY_CODE_SYS01");
      attribute2dbField.put("docTypeDOC01","DOC_TYPE");
      attribute2dbField.put("docStateDOC01","DOC_STATE");
      attribute2dbField.put("pricelistCodeSal01DOC01","PRICELIST_CODE_SAL01");
      attribute2dbField.put("pricelistDescriptionDOC01","PRICELIST_DESCRIPTION");
      attribute2dbField.put("currencyCodeReg03DOC01","CURRENCY_CODE_REG03");
      attribute2dbField.put("docYearDOC01","DOC_YEAR");
      attribute2dbField.put("docNumberDOC01","DOC_NUMBER");
      attribute2dbField.put("taxableIncomeDOC01","TAXABLE_INCOME");
      attribute2dbField.put("totalVatDOC01","TOTAL_VAT");
      attribute2dbField.put("totalDOC01","TOTAL");
      attribute2dbField.put("docDateDOC01","DOC_DATE");

      attribute2dbField.put("progressiveReg04DOC01","PROGRESSIVE_REG04");
      attribute2dbField.put("companyCodeSys01Doc01DOC01","COMPANY_CODE_SYS01_DOC01");
      attribute2dbField.put("docTypeDoc01DOC01","DOC_TYPE_DOC01");
      attribute2dbField.put("docYearDoc01DOC01","DOC_YEAR_DOC01");
      attribute2dbField.put("docNumberDoc01DOC01","DOC_NUMBER_DOC01");
      attribute2dbField.put("paymentCodeReg10DOC01","PAYMENT_CODE_REG10");
      attribute2dbField.put("paymentDescriptionDOC01","PAYMENT_DESCRIPTION");
      attribute2dbField.put("instalmentNumberDOC01","INSTALMENT_NUMBER");
      attribute2dbField.put("stepDOC01","STEP");
      attribute2dbField.put("startDayDOC01","START_DAY");
      attribute2dbField.put("firstInstalmentDaysDOC01","FIRST_INSTALMENT_DAYS");
      attribute2dbField.put("paymentTypeDescriptionDOC01","PAYMENT_TYPE_DESCRIPTION");
      attribute2dbField.put("progressiveWkf01DOC01","PROGRESSIVE_WKF01");
      attribute2dbField.put("progressiveWkf08DOC01","PROGRESSIVE_WKF08");
      attribute2dbField.put("descriptionWkf01DOC01","DESCRIPTION_WKF01");
      attribute2dbField.put("noteDOC01","NOTE");
      attribute2dbField.put("enabledDOC01","ENABLED");
      attribute2dbField.put("warehouseCodeWar01DOC01","WAREHOUSE_CODE_WAR01");
      attribute2dbField.put("descriptionWar01DOC01","DESCRIPTION_WAR01");
      attribute2dbField.put("addressDOC01","ADDRESS");
      attribute2dbField.put("cityDOC01","CITY");
      attribute2dbField.put("provinceDOC01","PROVINCE");
      attribute2dbField.put("zipDOC01","ZIP");
      attribute2dbField.put("countryDOC01","COUNTRY");

      attribute2dbField.put("allowanceDOC01","ALLOWANCE");
      attribute2dbField.put("depositDOC01","DEPOSIT");
      attribute2dbField.put("headingNoteDOC01","HEADING_NOTE");
      attribute2dbField.put("footerNoteDOC01","FOOTER_NOTE");
      attribute2dbField.put("deliveryNoteDOC01","DELIVERY_NOTE");
      attribute2dbField.put("docRefNumberDOC01","DOC_REF_NUMBER");
      attribute2dbField.put("agentCodeSal10DOC01","AGENT_CODE_SAL10");
      attribute2dbField.put("name_2DOC01","NAME_1");
      attribute2dbField.put("name_1DOC01","NAME_2");
      attribute2dbField.put("percentageDOC01","PERCENTAGE");
      attribute2dbField.put("destinationCodeReg18DOC01","DESTINATION_CODE_REG18");
      attribute2dbField.put("descriptionDOC01","DESCRIPTION");
      attribute2dbField.put("customerVatCodeReg01DOC01","CUSTOMER_VAT_CODE_REG01");
      attribute2dbField.put("discountValueDOC01","DISCOUNT_VALUE");
      attribute2dbField.put("discountPercDOC01","DISCOUNT_PERC");
      attribute2dbField.put("docSequenceDOC01","DOC_SEQUENCE");

      attribute2dbField.put("deliveryDateDOC01","DELIVERY_DATE");

      HashSet pkAttributes = new HashSet();
      pkAttributes.add("companyCodeSys01DOC01");
      pkAttributes.add("docTypeDOC01");
      pkAttributes.add("docYearDOC01");
      pkAttributes.add("docNumberDOC01");

      // update DOC01 table...
      Response res = CustomizeQueryUtil.updateTable(
          conn,
          new UserSessionParameters(username),
          pkAttributes,
          oldVO,
          newVO,
          "DOC01_SELLING",
          attribute2dbField,
          "Y",
          "N",
          null,
          true,
          customizedFields
      );

      SaleDocPK pk = new SaleDocPK(newVO.getCompanyCodeSys01DOC01(),newVO.getDocTypeDOC01(),newVO.getDocYearDOC01(),newVO.getDocNumberDOC01());
      Response totalsRes = totals.updateTaxableIncomes(
          variant1Descriptions,
          variant2Descriptions,
          variant3Descriptions,
          variant4Descriptions,
          variant5Descriptions,
          pk,
          serverLanguageId,
          username
      );
      if (totalsRes.isError()) {
    	  throw new Exception(totalsRes.getErrorMessage());
      }

      Response answer = new VOResponse(bean.loadSaleDoc(pk,serverLanguageId,username,customizedFields));


      // store default warehouse code as user parameter (in SYS19 table)...
      HashMap map = new HashMap();
      map.put(ApplicationConsts.COMPANY_CODE_SYS01,newVO.getCompanyCodeSys01DOC01());
      map.put(ApplicationConsts.PARAM_CODE,ApplicationConsts.WAREHOUSE_CODE);
      map.put(ApplicationConsts.PARAM_VALUE,newVO.getWarehouseCodeWar01DOC01());
      userBean.saveUserParam(map,serverLanguageId,username);

      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating an existing sale header document",ex);
      try {
    		if (this.conn==null && conn!=null)
    			// rollback only local connection
    			conn.rollback();
    	}
    	catch (Exception ex3) {
    	}

      throw new Exception(ex.getMessage());
    }
    finally {
      try {
        stmt.close();
      }
      catch (Exception ex2) {
      }
      try {
    	  if (this.conn==null && conn!=null) {
    		  // close only local connection
    		  conn.commit();
    		  conn.close();
    	  }

      }
      catch (Exception exx) {}

      try {
        payBean.setConn(null);
        currBean.setConn(null);
        userBean.setConn(null);
        totals.setConn(null);
        bean.setConn(null);
      } catch (Exception ex) {}
    }
  }





  /**
   * Business logic to execute.
   */
  public VOListResponse validateSaleDocNumber(LookupValidationParams pars,String serverLanguageId,String username,ArrayList companiesList) throws Throwable {
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
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          GridSaleDocVO.class,
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
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching sale orders list",ex);
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




  /**
   * Business logic to execute.
   */
  public VOListResponse loadSaleDocs(GridParams pars,String serverLanguageId,String username,ArrayList companiesList) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      // retrieve companies list...
      String companies = "";
      if (pars.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01)!=null) {
        companies = "'"+pars.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01)+"'";
      }
      else {
        for(int i=0;i<companiesList.size();i++)
          companies += "'"+companiesList.get(i).toString()+"',";
        companies = companies.substring(0,companies.length()-1);
      }

      String sql =
          "select DOC01_SELLING.COMPANY_CODE_SYS01,DOC01_SELLING.DOC_TYPE,DOC01_SELLING.DOC_STATE,DOC01_SELLING.PRICELIST_CODE_SAL01,DOC01_SELLING.PRICELIST_DESCRIPTION,"+
          "DOC01_SELLING.CURRENCY_CODE_REG03,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,DOC01_SELLING.DOC_YEAR,DOC01_SELLING.DOC_NUMBER,DOC01_SELLING.TAXABLE_INCOME,"+
          "DOC01_SELLING.TOTAL_VAT,DOC01_SELLING.TOTAL,DOC01_SELLING.DOC_DATE,SAL07_CUSTOMERS.CUSTOMER_CODE,"+
          "REG03_CURRENCIES.DECIMALS,REG03_CURRENCIES.CURRENCY_SYMBOL,REG03_CURRENCIES.THOUSAND_SYMBOL,REG03_CURRENCIES.DECIMAL_SYMBOL,DOC01_SELLING.DOC_SEQUENCE,"+
          "DOC01_SELLING.SECTIONAL,DOC01_SELLING.DELIVERY_DATE "+
          " from DOC01_SELLING,SAL01_PRICELISTS,SYS10_TRANSLATIONS,REG04_SUBJECTS,SAL07_CUSTOMERS,REG03_CURRENCIES where "+
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
          "DOC01_SELLING.ENABLED='Y'";

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
      attribute2dbField.put("sectionalDOC01","DOC01_SELLING.SECTIONAL");
      attribute2dbField.put("deliveryDateDOC01","DOC01_SELLING.DELIVERY_DATE");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);

      if (pars.getOtherGridParams().get(ApplicationConsts.DOC_TYPE)!=null) {
    	  String[] types = pars.getOtherGridParams().get(ApplicationConsts.DOC_TYPE).toString().split(",");
    	  String inClause = "";
    	  for(int u=0;u<types.length;u++) {
    		  inClause += "'"+types[u]+"',";
    	  }
    	  if (inClause.length()>0)
    		  inClause = inClause.substring(0,inClause.length()-1);
    	  sql += " and DOC01_SELLING.DOC_TYPE in ("+inClause+")";
      }
      if (pars.getOtherGridParams().get(ApplicationConsts.DOC_STATE)!=null) {
        sql += " and DOC01_SELLING.DOC_STATE='"+pars.getOtherGridParams().get(ApplicationConsts.DOC_STATE)+"'";
      }
      if (pars.getOtherGridParams().get(ApplicationConsts.DOC_YEAR)!=null) {
        sql += " and DOC01_SELLING.DOC_YEAR="+pars.getOtherGridParams().get(ApplicationConsts.DOC_YEAR);
      }
      if (pars.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04)!=null) {
        sql += " and DOC01_SELLING.PROGRESSIVE_REG04=?";
        values.add( pars.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04) );
      }
      if (pars.getOtherGridParams().get(ApplicationConsts.WAREHOUSE_CODE)!=null) {
        sql += " and DOC01_SELLING.WAREHOUSE_CODE_WAR01='"+pars.getOtherGridParams().get(ApplicationConsts.WAREHOUSE_CODE)+"'";
      }
      if (pars.getOtherGridParams().get(ApplicationConsts.DELIV_DATE_LESS_OR_EQUALS_TO)!=null) {
        sql += " and DOC01_SELLING.DELIVERY_DATE<=? ";
        values.add(pars.getOtherGridParams().get(ApplicationConsts.DELIV_DATE_LESS_OR_EQUALS_TO));
      }




      // read from DOC01 table...
      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          GridSaleDocVO.class,
          "Y",
          "N",
          null,
          pars,
          50,
          true
      );



      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching sale documents list",ex);
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





  /**
   * Insert header sale document in DOC01 table.
   * This method does not execute commit or rollback operations.
   */
  public VOResponse insertSaleDoc(DetailSaleDocVO vo,String serverLanguageId,String username,String companyCode,ArrayList customizedFields)  throws Throwable{
    PreparedStatement pstmt = null;

    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      payBean.setConn(conn); // use same transaction...
      currBean.setConn(conn); // use same transaction...

      vo.setEnabledDOC01("Y");
      vo.setTaxableIncomeDOC01(new BigDecimal(0));
      vo.setTotalDOC01(new BigDecimal(0));
      vo.setAllowanceDOC01(new BigDecimal(0));
      vo.setDepositDOC01(new BigDecimal(0));
      vo.setTotalVatDOC01(new BigDecimal(0));

      if (vo.getCompanyCodeSys01DOC01()==null)
        vo.setCompanyCodeSys01DOC01(companyCode);

      // retrieve payment info...
			ArrayList companiesList = new ArrayList();
			companiesList.add(vo.getCompanyCodeSys01DOC01());
      LookupValidationParams pars = new LookupValidationParams(vo.getPaymentCodeReg10DOC01(),new HashMap());
      Response payResponse = payBean.validatePaymentCode(pars,serverLanguageId,username,new ArrayList(),companiesList);
      if (payResponse.isError())
        throw new Exception(payResponse.getErrorMessage());

      PaymentVO payVO = (PaymentVO)((VOListResponse)payResponse).getRows().get(0);
      vo.setFirstInstalmentDaysDOC01(payVO.getFirstInstalmentDaysREG10());
      vo.setInstalmentNumberDOC01(payVO.getInstalmentNumberREG10());
      vo.setPaymentTypeDescriptionDOC01(payVO.getPaymentTypeDescriptionSYS10());
      vo.setStartDayDOC01(payVO.getStartDayREG10());
      vo.setStepDOC01(payVO.getStepREG10());

      // retrieve currency info...
      pars = new LookupValidationParams(vo.getCurrencyCodeReg03DOC01(),new HashMap());
      Response currResponse = currBean.validateCurrencyCode(pars,serverLanguageId,username,new ArrayList());
      if (currResponse.isError())
    	  throw new Exception(currResponse.getErrorMessage());
      CurrencyVO currVO = (CurrencyVO)((VOListResponse)currResponse).getRows().get(0);
      vo.setCurrencySymbolREG03(currVO.getCurrencySymbolREG03());
      vo.setDecimalSymbolREG03(currVO.getDecimalSymbolREG03());
      vo.setThousandSymbolREG03(currVO.getThousandSymbolREG03());
      vo.setDecimalsREG03(currVO.getDecimalsREG03());

      // generate internal progressive for doc. number...
      vo.setDocNumberDOC01(CompanyProgressiveUtils.getInternalProgressive(vo.getCompanyCodeSys01DOC01(),"DOC01_SELLING","DOC_NUMBER",conn));

      // if doc type is estimate, then generate progressive for doc sequence, too...
      if (vo.getDocTypeDOC01().equals(ApplicationConsts.SALE_ESTIMATE_DOC_TYPE)) {
        // generate progressive for doc. sequence...
        pstmt = conn.prepareStatement(
          "select max(DOC_SEQUENCE) from DOC01_SELLING where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_SEQUENCE is not null"
        );
        pstmt.setString(1,vo.getCompanyCodeSys01DOC01());
        pstmt.setString(2,vo.getDocTypeDOC01());
        pstmt.setBigDecimal(3,vo.getDocYearDOC01());
        ResultSet rset = pstmt.executeQuery();
        int docSequenceDOC01 = 1;
        if (rset.next())
          docSequenceDOC01 = rset.getInt(1)+1;
        rset.close();
        pstmt.close();
        vo.setDocSequenceDOC01(new BigDecimal(docSequenceDOC01));
      }


      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC01","COMPANY_CODE_SYS01");
      attribute2dbField.put("docTypeDOC01","DOC_TYPE");
      attribute2dbField.put("docStateDOC01","DOC_STATE");
      attribute2dbField.put("pricelistCodeSal01DOC01","PRICELIST_CODE_SAL01");
      attribute2dbField.put("pricelistDescriptionDOC01","PRICELIST_DESCRIPTION");
      attribute2dbField.put("currencyCodeReg03DOC01","CURRENCY_CODE_REG03");
      attribute2dbField.put("docYearDOC01","DOC_YEAR");
      attribute2dbField.put("docNumberDOC01","DOC_NUMBER");
      attribute2dbField.put("taxableIncomeDOC01","TAXABLE_INCOME");
      attribute2dbField.put("totalVatDOC01","TOTAL_VAT");
      attribute2dbField.put("totalDOC01","TOTAL");
      attribute2dbField.put("docDateDOC01","DOC_DATE");

      attribute2dbField.put("progressiveReg04DOC01","PROGRESSIVE_REG04");
      attribute2dbField.put("companyCodeSys01Doc01DOC01","COMPANY_CODE_SYS01_DOC01");
      attribute2dbField.put("docTypeDoc01DOC01","DOC_TYPE_DOC01");
      attribute2dbField.put("docYearDoc01DOC01","DOC_YEAR_DOC01");
      attribute2dbField.put("docNumberDoc01DOC01","DOC_NUMBER_DOC01");
      attribute2dbField.put("paymentCodeReg10DOC01","PAYMENT_CODE_REG10");
      attribute2dbField.put("paymentDescriptionDOC01","PAYMENT_DESCRIPTION");
      attribute2dbField.put("instalmentNumberDOC01","INSTALMENT_NUMBER");
      attribute2dbField.put("stepDOC01","STEP");
      attribute2dbField.put("startDayDOC01","START_DAY");
      attribute2dbField.put("firstInstalmentDaysDOC01","FIRST_INSTALMENT_DAYS");
      attribute2dbField.put("paymentTypeDescriptionDOC01","PAYMENT_TYPE_DESCRIPTION");
      attribute2dbField.put("progressiveWkf01DOC01","PROGRESSIVE_WKF01");
      attribute2dbField.put("progressiveWkf08DOC01","PROGRESSIVE_WKF08");
      attribute2dbField.put("descriptionWkf01DOC01","DESCRIPTION_WKF01");
      attribute2dbField.put("noteDOC01","NOTE");
      attribute2dbField.put("enabledDOC01","ENABLED");
      attribute2dbField.put("warehouseCodeWar01DOC01","WAREHOUSE_CODE_WAR01");
      attribute2dbField.put("descriptionWar01DOC01","DESCRIPTION_WAR01");
      attribute2dbField.put("addressDOC01","ADDRESS");
      attribute2dbField.put("cityDOC01","CITY");
      attribute2dbField.put("provinceDOC01","PROVINCE");
      attribute2dbField.put("zipDOC01","ZIP");
      attribute2dbField.put("countryDOC01","COUNTRY");

      attribute2dbField.put("allowanceDOC01","ALLOWANCE");
      attribute2dbField.put("depositDOC01","DEPOSIT");
      attribute2dbField.put("headingNoteDOC01","HEADING_NOTE");
      attribute2dbField.put("footerNoteDOC01","FOOTER_NOTE");
      attribute2dbField.put("deliveryNoteDOC01","DELIVERY_NOTE");
      attribute2dbField.put("docRefNumberDOC01","DOC_REF_NUMBER");
      attribute2dbField.put("agentCodeSal10DOC01","AGENT_CODE_SAL10");
      attribute2dbField.put("name_2DOC01","NAME_1");
      attribute2dbField.put("name_1DOC01","NAME_2");
      attribute2dbField.put("percentageDOC01","PERCENTAGE");
      attribute2dbField.put("destinationCodeReg18DOC01","DESTINATION_CODE_REG18");
      attribute2dbField.put("descriptionDOC01","DESCRIPTION");
      attribute2dbField.put("customerVatCodeReg01DOC01","CUSTOMER_VAT_CODE_REG01");
      attribute2dbField.put("docSequenceDOC01","DOC_SEQUENCE");
      attribute2dbField.put("docSequenceDoc01DOC01","DOC_SEQUENCE_DOC01");
      attribute2dbField.put("sectionalDOC01","DOC01_SELLING.SECTIONAL");

      attribute2dbField.put("deliveryDateDOC01","DOC01_SELLING.DELIVERY_DATE");

      // insert into DOC01...
      Response res = CustomizeQueryUtil.insertTable(
          conn,
          new UserSessionParameters(username),
          vo,
          "DOC01_SELLING",
          attribute2dbField,
          "Y",
          "N",
          null,
          true,
          customizedFields
      );

      Response answer = res;
      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"insertSaleDoc","Error while inserting a new sale header document",ex);
      throw new Exception(ex.getMessage());
    }
    finally {
      try {
        pstmt.close();
      }
      catch (Exception ex2) {
      }
      try {
    	  if (this.conn==null && conn!=null) {
    		  // close only local connection
    		  conn.commit();
    		  conn.close();
    	  }

      }
      catch (Exception exx) {}
      try {
        payBean.setConn(null);
        currBean.setConn(null);
      } catch (Exception ex) {}
    }
  }




  /**
   * Business logic to execute.
   */
  public VOResponse deleteSaleDocs(ArrayList list,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;

    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;




      SaleDocPK pk = null;

      pstmt = conn.prepareStatement(
          "update DOC01_SELLING set ENABLED='N' where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=?"
      );

      for(int i=0;i<list.size();i++) {
        pk = (SaleDocPK)list.get(i);

        // logically delete the record in DOC01...
        pstmt.setString(1,pk.getCompanyCodeSys01DOC01());
        pstmt.setString(2,pk.getDocTypeDOC01());
        pstmt.setBigDecimal(3,pk.getDocYearDOC01());
        pstmt.setBigDecimal(4,pk.getDocNumberDOC01());
        pstmt.execute();
      }


      Response answer = new VOResponse(new Boolean(true));





      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing sale documents",ex);
      try {
    		if (this.conn==null && conn!=null)
    			// rollback only local connection
    			conn.rollback();
    	}
    	catch (Exception ex3) {
    	}

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

