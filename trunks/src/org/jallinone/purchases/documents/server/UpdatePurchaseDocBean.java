package org.jallinone.purchases.documents.server;

import org.openswing.swing.server.*;
import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;

import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.purchases.documents.java.*;
import org.jallinone.system.server.*;
import java.math.*;
import org.jallinone.system.progressives.server.*;
import org.jallinone.system.translations.server.*;
import org.jallinone.commons.server.*;
import org.jallinone.system.java.*;
import org.jallinone.commons.java.*;
import org.jallinone.registers.payments.java.*;
import org.jallinone.registers.payments.server.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.registers.currency.server.*;
import org.jallinone.registers.currency.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to update an existing purchase order.</p>
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
public class UpdatePurchaseDocBean  implements UpdatePurchaseDoc {


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

  private PurchaseDocTotalsBean totalBean;

  public void setTotalBean(PurchaseDocTotalsBean totalBean) {
    this.totalBean = totalBean;
  }



  public UpdatePurchaseDocBean() {
  }



  /**
   * Business logic to execute.
   */
  public VOResponse updatePurchaseDoc(DetailPurchaseDocVO oldVO,DetailPurchaseDocVO newVO,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      payBean.setConn(conn); // use same transaction...
      currBean.setConn(conn); // use same transaction...
      totalBean.setConn(conn); // use same transaction...


      // retrieve payment info...
			ArrayList companiesList = new ArrayList();
			companiesList.add(newVO.getCompanyCodeSys01DOC06());
      LookupValidationParams pars = new LookupValidationParams(newVO.getPaymentCodeReg10DOC06(),new HashMap());
      Response payResponse = payBean.validatePaymentCode(pars,serverLanguageId,username,new ArrayList(),companiesList);
      if (payResponse.isError())
        return new VOResponse(payResponse.getErrorMessage());
      PaymentVO payVO = (PaymentVO)((VOListResponse)payResponse).getRows().get(0);
      newVO.setFirstInstalmentDaysDOC06(payVO.getFirstInstalmentDaysREG10());
      newVO.setInstalmentNumberDOC06(payVO.getInstalmentNumberREG10());
      newVO.setPaymentTypeDescriptionDOC06(payVO.getPaymentTypeDescriptionSYS10());
      newVO.setStartDayDOC06(payVO.getStartDayREG10());
      newVO.setStepDOC06(payVO.getStepREG10());

      // retrieve currency info...
      pars = new LookupValidationParams(newVO.getCurrencyCodeReg03DOC06(),new HashMap());
      Response currResponse = currBean.validateCurrencyCode(pars,serverLanguageId,username,new ArrayList());
      if (currResponse.isError())
        return new VOResponse(currResponse.getErrorMessage());
      CurrencyVO currVO = (CurrencyVO)((VOListResponse)currResponse).getRows().get(0);
      newVO.setCurrencySymbolREG03(currVO.getCurrencySymbolREG03());
      newVO.setDecimalSymbolREG03(currVO.getDecimalSymbolREG03());
      newVO.setThousandSymbolREG03(currVO.getThousandSymbolREG03());
      newVO.setDecimalsREG03(currVO.getDecimalsREG03());

      // recalculate totals...
      Response totalResponse = totalBean.calcDocTotals(newVO,serverLanguageId,username);
      if (totalResponse.isError())
        return new VOResponse(totalResponse.getErrorMessage());

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC06","COMPANY_CODE_SYS01");
      attribute2dbField.put("docTypeDOC06","DOC_TYPE");
      attribute2dbField.put("docStateDOC06","DOC_STATE");
      attribute2dbField.put("pricelistCodePur03DOC06","PRICELIST_CODE_PUR03");
      attribute2dbField.put("pricelistDescriptionDOC06","PRICELIST_DESCRIPTION");
      attribute2dbField.put("currencyCodeReg03DOC06","CURRENCY_CODE_REG03");
      attribute2dbField.put("docYearDOC06","DOC_YEAR");
      attribute2dbField.put("docNumberDOC06","DOC_NUMBER");
      attribute2dbField.put("taxableIncomeDOC06","TAXABLE_INCOME");
      attribute2dbField.put("totalVatDOC06","TOTAL_VAT");
      attribute2dbField.put("totalDOC06","TOTAL");
      attribute2dbField.put("docDateDOC06","DOC_DATE");

      attribute2dbField.put("progressiveReg04DOC06","PROGRESSIVE_REG04");
      attribute2dbField.put("companyCodeSys01Doc06DOC06","COMPANY_CODE_SYS01_DOC06");
      attribute2dbField.put("docTypeDoc06DOC06","DOC_TYPE_DOC06");
      attribute2dbField.put("docYearDoc06DOC06","DOC_YEAR_DOC06");
      attribute2dbField.put("docSequenceDOC06","DOC_SEQUENCE");
      attribute2dbField.put("docNumberDoc06DOC06","DOC_NUMBER_DOC06");
      attribute2dbField.put("discountValueDOC06","DISCOUNT_VALUE");
      attribute2dbField.put("discountPercDOC06","DISCOUNT_PERC");
      attribute2dbField.put("chargeValueDOC06","CHARGE_VALUE");
      attribute2dbField.put("chargePercDOC06","CHARGE_PERC");
      attribute2dbField.put("paymentCodeReg10DOC06","PAYMENT_CODE_REG10");
      attribute2dbField.put("paymentDescriptionDOC06","PAYMENT_DESCRIPTION");
      attribute2dbField.put("instalmentNumberDOC06","INSTALMENT_NUMBER");
      attribute2dbField.put("stepDOC06","STEP");
      attribute2dbField.put("startDayDOC06","START_DAY");
      attribute2dbField.put("firstInstalmentDaysDOC06","FIRST_INSTALMENT_DAYS");
      attribute2dbField.put("paymentTypeDescriptionDOC06","PAYMENT_TYPE_DESCRIPTION");
      attribute2dbField.put("progressiveWkf01DOC06","PROGRESSIVE_WKF01");
      attribute2dbField.put("progressiveWkf08DOC06","PROGRESSIVE_WKF08");
      attribute2dbField.put("descriptionWkf01DOC06","DESCRIPTION_WKF01");
      attribute2dbField.put("noteDOC06","NOTE");
      attribute2dbField.put("enabledDOC06","ENABLED");
      attribute2dbField.put("warehouseCodeWar01DOC06","WAREHOUSE_CODE_WAR01");
      attribute2dbField.put("descriptionWar01DOC06","DESCRIPTION_WAR01");
      attribute2dbField.put("addressWar01DOC06","ADDRESS_WAR01");
      attribute2dbField.put("cityWar01DOC06","CITY_WAR01");
      attribute2dbField.put("provinceWar01DOC06","PROVINCE_WAR01");
      attribute2dbField.put("zipWar01DOC06","ZIP_WAR01");
      attribute2dbField.put("countryWar01DOC06","COUNTRY_WAR01");

      HashSet pkAttributes = new HashSet();
      pkAttributes.add("companyCodeSys01DOC06");
      pkAttributes.add("docTypeDOC06");
      pkAttributes.add("docYearDOC06");
      pkAttributes.add("docNumberDOC06");

      // update DOC06 table...
      Response res = CustomizeQueryUtil.updateTable(
          conn,
          new UserSessionParameters(username),
          pkAttributes,
          oldVO,
          newVO,
          "DOC06_PURCHASE",
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
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating an existing purchase order",ex);
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
        totalBean.setConn(null);
      } catch (Exception ex) {}
    }
  }



}

