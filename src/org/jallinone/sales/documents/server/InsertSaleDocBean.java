package org.jallinone.sales.documents.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.sales.documents.java.DetailSaleDocVO;
import org.jallinone.system.server.JAIOUserSessionParameters;
import java.math.BigDecimal;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.system.progressives.server.ProgressiveUtils;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.system.server.*;
import org.jallinone.system.java.ApplicationParametersVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.registers.payments.server.ValidatePaymentCodeAction;
import org.jallinone.registers.payments.java.PaymentVO;
import org.openswing.swing.message.send.java.LookupValidationParams;
import org.jallinone.registers.currency.server.ValidateCurrencyCodeAction;
import org.jallinone.registers.currency.java.CurrencyVO;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Helper class used to insert a new sale header document in DOC01 table.</p>
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
public class InsertSaleDocBean {

  private ValidatePaymentCodeAction payBean = new ValidatePaymentCodeAction();
  private ValidateCurrencyCodeAction currBean = new ValidateCurrencyCodeAction();


  public InsertSaleDocBean() {
  }


  /**
   * Insert header sale document in DOC01 table.
   * This method does not execute commit or rollback operations.
   */
  public final Response insertSaleDoc(Connection conn,DetailSaleDocVO vo,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    PreparedStatement pstmt = null;
    try {

      // fires the GenericEvent.CONNECTION_CREATED event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "InsertSaleDocBean.insertSaleDoc",
        GenericEvent.CONNECTION_CREATED,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        vo,
        null
      ));
      String companyCode = ((JAIOUserSessionParameters)userSessionPars).getCompanyBa().getCompaniesList("DOC01_ORDERS").get(0).toString();

      vo.setEnabledDOC01("Y");
      vo.setTaxableIncomeDOC01(new BigDecimal(0));
      vo.setTotalDOC01(new BigDecimal(0));
      vo.setAllowanceDOC01(new BigDecimal(0));
      vo.setDepositDOC01(new BigDecimal(0));
      vo.setTotalVatDOC01(new BigDecimal(0));

      if (vo.getCompanyCodeSys01DOC01()==null)
        vo.setCompanyCodeSys01DOC01(companyCode);

      // retrieve payment info...
      LookupValidationParams pars = new LookupValidationParams(vo.getPaymentCodeReg10DOC01(),new HashMap());
      Response payResponse = payBean.executeCommand(pars,userSessionPars,request, response,userSession,context);
      if (payResponse.isError())
        return payResponse;
      PaymentVO payVO = (PaymentVO)((VOListResponse)payResponse).getRows().get(0);
      vo.setFirstInstalmentDaysDOC01(payVO.getFirstInstalmentDaysREG10());
      vo.setInstalmentNumberDOC01(payVO.getInstalmentNumberREG10());
      vo.setPaymentTypeDescriptionDOC01(payVO.getPaymentTypeDescriptionSYS10());
      vo.setStartDayDOC01(payVO.getStartDayREG10());
      vo.setStepDOC01(payVO.getStepREG10());

      // retrieve currency info...
      pars = new LookupValidationParams(vo.getCurrencyCodeReg03DOC01(),new HashMap());
      Response currResponse = currBean.executeCommand(pars,userSessionPars,request, response,userSession,context);
      if (currResponse.isError())
        return currResponse;
      CurrencyVO currVO = (CurrencyVO)((VOListResponse)currResponse).getRows().get(0);
      vo.setCurrencySymbolREG03(currVO.getCurrencySymbolREG03());
      vo.setDecimalSymbolREG03(currVO.getDecimalSymbolREG03());
      vo.setThousandSymbolREG03(currVO.getThousandSymbolREG03());
      vo.setDecimalsREG03(currVO.getDecimalsREG03());

      // generate internal progressive for doc. number...
      vo.setDocNumberDOC01(ProgressiveUtils.getInternalProgressive("DOC01_SELLING","DOC_NUMBER",conn));

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

      // insert into DOC01...
      Response res = CustomizeQueryUtil.insertTable(
          conn,
          userSessionPars,
          vo,
          "DOC01_SELLING",
          attribute2dbField,
          "Y",
          "N",
          context,
          true,
          ApplicationConsts.ID_SALE_ORDER // window identifier...
      );

      Response answer = res;

      // fires the GenericEvent.BEFORE_COMMIT event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "InsertSaleDocBean.insertSaleDoc",
        GenericEvent.BEFORE_COMMIT,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        vo,
        answer
      ));


      return answer;
    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"insertSaleDoc","Error while inserting a new sale header document",ex);
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        pstmt.close();
      }
      catch (Exception ex2) {
      }
    }
  }



}
