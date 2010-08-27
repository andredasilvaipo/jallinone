package org.jallinone.sales.customers.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.warehouse.java.WarehouseVO;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.sales.customers.java.*;
import org.jallinone.commons.server.CustomizeQueryUtil;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.subjects.java.Subject;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.accounting.accounts.server.ValidateAccountCodeAction;
import org.openswing.swing.message.send.java.LookupValidationParams;
import org.jallinone.accounting.accounts.java.AccountVO;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch a specific customer from SAL07/REG04 table.</p>
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
public class LoadCustomerAction implements Action {

  private ValidateAccountCodeAction accountAction = new ValidateAccountCodeAction();


  public LoadCustomerAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadCustomer";
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

      CustomerPK pk = (CustomerPK)inputPar;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01REG04","REG04_SUBJECTS.COMPANY_CODE_SYS01");
      attribute2dbField.put("name_1REG04","REG04_SUBJECTS.NAME_1");
      attribute2dbField.put("name_2REG04","REG04_SUBJECTS.NAME_2");
      attribute2dbField.put("progressiveREG04","REG04_SUBJECTS.PROGRESSIVE");
      attribute2dbField.put("addressREG04","REG04_SUBJECTS.ADDRESS");
      attribute2dbField.put("cityREG04","REG04_SUBJECTS.CITY");
      attribute2dbField.put("provinceREG04","REG04_SUBJECTS.PROVINCE");
      attribute2dbField.put("countryREG04","REG04_SUBJECTS.COUNTRY");
      attribute2dbField.put("taxCodeREG04","REG04_SUBJECTS.TAX_CODE");
      attribute2dbField.put("customerCodeSAL07","SAL07_CUSTOMERS.CUSTOMER_CODE");
      attribute2dbField.put("subjectTypeREG04","REG04_SUBJECTS.SUBJECT_TYPE");
      attribute2dbField.put("zipREG04","REG04_SUBJECTS.ZIP");
      attribute2dbField.put("sexREG04","REG04_SUBJECTS.SEX");
      attribute2dbField.put("maritalStatusREG04","REG04_SUBJECTS.MARITAL_STATUS");
      attribute2dbField.put("nationalityREG04","REG04_SUBJECTS.NATIONALITY");
      attribute2dbField.put("birthdayREG04","REG04_SUBJECTS.BIRTHDAY");
      attribute2dbField.put("birthplaceREG04","REG04_SUBJECTS.BIRTHPLACE");
      attribute2dbField.put("phoneNumberREG04","REG04_SUBJECTS.PHONE_NUMBER");
      attribute2dbField.put("mobileNumberREG04","REG04_SUBJECTS.MOBILE_NUMBER");
      attribute2dbField.put("faxNumberREG04","REG04_SUBJECTS.FAX_NUMBER");
      attribute2dbField.put("emailAddressREG04","REG04_SUBJECTS.EMAIL_ADDRESS");
      attribute2dbField.put("webSiteREG04","REG04_SUBJECTS.WEB_SITE");
      attribute2dbField.put("lawfulSiteREG04","REG04_SUBJECTS.LAWFUL_SITE");
      attribute2dbField.put("noteREG04","REG04_SUBJECTS.NOTE");
      attribute2dbField.put("paymentCodeReg10SAL07","SAL07_CUSTOMERS.PAYMENT_CODE_REG10");
      attribute2dbField.put("bankCodeReg12SAL07","SAL07_CUSTOMERS.BANK_CODE_REG12");
      attribute2dbField.put("paymentDescriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("pricelistCodeSal01SAL07","SAL07_CUSTOMERS.PRICELIST_CODE_SAL01");
      attribute2dbField.put("agentProgressiveReg04SAL07","SAL07_CUSTOMERS.AGENT_PROGRESSIVE_REG04");
      attribute2dbField.put("trustAmountSAL07","SAL07_CUSTOMERS.TRUST_AMOUNT");
      attribute2dbField.put("vatCodeReg01SAL07","SAL07_CUSTOMERS.VAT_CODE_REG01");
      attribute2dbField.put("creditAccountCodeAcc02SAL07","SAL07_CUSTOMERS.CREDIT_ACCOUNT_CODE_ACC02");
      attribute2dbField.put("itemsAccountCodeAcc02SAL07","SAL07_CUSTOMERS.ITEMS_ACCOUNT_CODE_ACC02");
      attribute2dbField.put("activitiesAccountCodeAcc02SAL07","SAL07_CUSTOMERS.ACTIVITIES_ACCOUNT_CODE_ACC02");
      attribute2dbField.put("chargesAccountCodeAcc02SAL07","SAL07_CUSTOMERS.CHARGES_ACCOUNT_CODE_ACC02");

      HashSet pkAttributes = new HashSet();
      pkAttributes.add("companyCodeSys01REG04");
      pkAttributes.add("progressiveREG04");

      String baseSQL = null;
      if (pk.getSubjectTypeREG04().equals(ApplicationConsts.SUBJECT_ORGANIZATION_CUSTOMER)) {
        baseSQL =
          "select REG04_SUBJECTS.COMPANY_CODE_SYS01,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,REG04_SUBJECTS.PROGRESSIVE,REG04_SUBJECTS.ADDRESS,REG04_SUBJECTS.CITY,REG04_SUBJECTS.PROVINCE,REG04_SUBJECTS.COUNTRY,REG04_SUBJECTS.TAX_CODE,SAL07_CUSTOMERS.CUSTOMER_CODE,"+
          "REG04_SUBJECTS.SUBJECT_TYPE,REG04_SUBJECTS.ZIP,"+
          "REG04_SUBJECTS.PHONE_NUMBER,REG04_SUBJECTS.FAX_NUMBER,REG04_SUBJECTS.EMAIL_ADDRESS,"+
          "REG04_SUBJECTS.WEB_SITE,REG04_SUBJECTS.LAWFUL_SITE,REG04_SUBJECTS.NOTE,"+
          "SAL07_CUSTOMERS.PAYMENT_CODE_REG10,SAL07_CUSTOMERS.PRICELIST_CODE_SAL01,SAL07_CUSTOMERS.BANK_CODE_REG12,SYS10_TRANSLATIONS.DESCRIPTION,"+
          "SAL07_CUSTOMERS.AGENT_PROGRESSIVE_REG04,SAL07_CUSTOMERS.TRUST_AMOUNT,SAL07_CUSTOMERS.VAT_CODE_REG01,"+
          "SAL07_CUSTOMERS.CREDIT_ACCOUNT_CODE_ACC02,SAL07_CUSTOMERS.ITEMS_ACCOUNT_CODE_ACC02,SAL07_CUSTOMERS.ACTIVITIES_ACCOUNT_CODE_ACC02,SAL07_CUSTOMERS.CHARGES_ACCOUNT_CODE_ACC02 "+
          " from REG04_SUBJECTS,SAL07_CUSTOMERS,SYS10_TRANSLATIONS,REG10_PAYMENTS where "+
          "SAL07_CUSTOMERS.COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 and "+
          "SAL07_CUSTOMERS.PROGRESSIVE_REG04=REG04_SUBJECTS.PROGRESSIVE and "+
          "REG04_SUBJECTS.COMPANY_CODE_SYS01=? and REG04_SUBJECTS.PROGRESSIVE=? and "+
          "SAL07_CUSTOMERS.PAYMENT_CODE_REG10=REG10_PAYMENTS.PAYMENT_CODE and "+
          "REG10_PAYMENTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "SAL07_CUSTOMERS.ENABLED='Y' ";
      }
      else {
        baseSQL =
          "select REG04_SUBJECTS.COMPANY_CODE_SYS01,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,REG04_SUBJECTS.PROGRESSIVE,REG04_SUBJECTS.ADDRESS,REG04_SUBJECTS.CITY,REG04_SUBJECTS.PROVINCE,REG04_SUBJECTS.COUNTRY,REG04_SUBJECTS.TAX_CODE,SAL07_CUSTOMERS.CUSTOMER_CODE,"+
          "REG04_SUBJECTS.SUBJECT_TYPE,REG04_SUBJECTS.ZIP,REG04_SUBJECTS.SEX,REG04_SUBJECTS.MARITAL_STATUS,REG04_SUBJECTS.NATIONALITY,REG04_SUBJECTS.BIRTHDAY,"+
          "REG04_SUBJECTS.BIRTHPLACE,REG04_SUBJECTS.PHONE_NUMBER,REG04_SUBJECTS.MOBILE_NUMBER,REG04_SUBJECTS.FAX_NUMBER,REG04_SUBJECTS.EMAIL_ADDRESS,"+
          "REG04_SUBJECTS.WEB_SITE,REG04_SUBJECTS.NOTE,"+
          "SAL07_CUSTOMERS.PAYMENT_CODE_REG10,SAL07_CUSTOMERS.PRICELIST_CODE_SAL01,SAL07_CUSTOMERS.BANK_CODE_REG12,SYS10_TRANSLATIONS.DESCRIPTION,"+
          "SAL07_CUSTOMERS.AGENT_PROGRESSIVE_REG04,SAL07_CUSTOMERS.TRUST_AMOUNT,SAL07_CUSTOMERS.VAT_CODE_REG01,"+
          "SAL07_CUSTOMERS.CREDIT_ACCOUNT_CODE_ACC02,SAL07_CUSTOMERS.ITEMS_ACCOUNT_CODE_ACC02,SAL07_CUSTOMERS.ACTIVITIES_ACCOUNT_CODE_ACC02,SAL07_CUSTOMERS.CHARGES_ACCOUNT_CODE_ACC02 "+
          " from REG04_SUBJECTS,SAL07_CUSTOMERS,SYS10_TRANSLATIONS,REG10_PAYMENTS where "+
          "SAL07_CUSTOMERS.COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 and "+
          "SAL07_CUSTOMERS.PROGRESSIVE_REG04=REG04_SUBJECTS.PROGRESSIVE and "+
          "REG04_SUBJECTS.COMPANY_CODE_SYS01=? and REG04_SUBJECTS.PROGRESSIVE=? and "+
          "SAL07_CUSTOMERS.PAYMENT_CODE_REG10=REG10_PAYMENTS.PAYMENT_CODE and "+
          "REG10_PAYMENTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "SAL07_CUSTOMERS.ENABLED='Y' ";
      }


      ArrayList values = new ArrayList();
      values.add(pk.getCompanyCodeSys01SAL07());
      values.add(pk.getProgressiveReg04SAL07());
      values.add(serverLanguageId);

      // read from REG04/SAL07 tables...
      Response res = CustomizeQueryUtil.getQuery(
          conn,
          userSessionPars,
          baseSQL,
          values,
          attribute2dbField,
          pk.getSubjectTypeREG04().equals(ApplicationConsts.SUBJECT_ORGANIZATION_CUSTOMER)?OrganizationCustomerVO.class:PeopleCustomerVO.class,
          "Y",
          "N",
          context,
          true,
          new BigDecimal(282) // window identifier...
      );

      if (!res.isError() && pk.getSubjectTypeREG04().equals(ApplicationConsts.SUBJECT_ORGANIZATION_CUSTOMER)) {
        OrganizationCustomerVO vo = (OrganizationCustomerVO)((VOResponse)res).getVo();
        stmt = conn.createStatement();
        if (vo.getPricelistCodeSal01SAL07()!=null) {
          ResultSet rset = stmt.executeQuery(
            "select DESCRIPTION,CURRENCY_CODE_REG03 from SYS10_TRANSLATIONS,SAL01_PRICELISTS where "+
            "SYS10_TRANSLATIONS.PROGRESSIVE=SAL01_PRICELISTS.PROGRESSIVE_SYS10 and "+
            "SYS10_TRANSLATIONS.LANGUAGE_CODE='"+serverLanguageId+"' and SAL01_PRICELISTS.COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01REG04()+"' and "+
            "SAL01_PRICELISTS.PRICELIST_CODE='"+vo.getPricelistCodeSal01SAL07()+"'"
          );
          if (rset.next()) {
            vo.setPricelistDescriptionSYS10(rset.getString(1));
            vo.setCurrencyCodeReg03SAL01(rset.getString(2));
          }
          rset.close();
        }
        if (vo.getBankCodeReg12SAL07()!=null) {
          ResultSet rset = stmt.executeQuery(
            "select DESCRIPTION from REG12_BANKS where "+
            "REG12_BANKS.BANK_CODE='"+vo.getBankCodeReg12SAL07()+"'"
          );
          if (rset.next())
            vo.setDescriptionREG12(rset.getString(1));
          rset.close();
        }
        if (vo.getAgentProgressiveReg04SAL07()!=null) {
          ResultSet rset = stmt.executeQuery(
            "select REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,SAL10_AGENTS.AGENT_CODE from SAL10_AGENTS,REG04_SUBJECTS where "+
            "SAL10_AGENTS.COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 and "+
            "SAL10_AGENTS.PROGRESSIVE_REG04=REG04_SUBJECTS.PROGRESSIVE and "+
            "SAL10_AGENTS.PROGRESSIVE_REG04="+vo.getAgentProgressiveReg04SAL07()
          );
          if (rset.next()) {
            vo.setAgentName_1REG04(rset.getString(1));
            vo.setAgentName_2REG04(rset.getString(2));
            vo.setAgentCodeSAL10(rset.getString(3));
          }
          rset.close();
        }
        if (vo.getVatCodeReg01SAL07()!=null) {
          ResultSet rset = stmt.executeQuery(
            "select REG01_VATS.VALUE,REG01_VATS.DEDUCTIBLE,SYS10_TRANSLATIONS.DESCRIPTION from REG01_VATS,SYS10_TRANSLATIONS where "+
            "REG01_VATS.VAT_CODE='"+vo.getVatCodeReg01SAL07()+"' and REG01_VATS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and SYS10_TRANSLATIONS.LANGUAGE_CODE='"+serverLanguageId+"'"
          );
          if (rset.next()) {
            vo.setVatValueREG01(rset.getBigDecimal(1));
            vo.setVatDeductibleREG01(rset.getBigDecimal(2));
            vo.setVatDescriptionSYS10(rset.getString(3));
          }
          rset.close();
        }

        HashMap map = new HashMap();
        map.put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01REG04());
        LookupValidationParams pars = new LookupValidationParams(vo.getCreditAccountCodeAcc02SAL07(),map);
        Response aRes = accountAction.executeCommand(pars,userSessionPars,request,response,userSession,context);
        if (!aRes.isError())
          vo.setCreditAccountDescrSAL07( ((AccountVO)((VOListResponse)aRes).getRows().get(0)).getDescriptionSYS10() );

        pars = new LookupValidationParams(vo.getItemsAccountCodeAcc02SAL07(),map);
        aRes = accountAction.executeCommand(pars,userSessionPars,request,response,userSession,context);
        if (!aRes.isError())
          vo.setItemsAccountDescrSAL07( ((AccountVO)((VOListResponse)aRes).getRows().get(0)).getDescriptionSYS10() );

        pars = new LookupValidationParams(vo.getActivitiesAccountCodeAcc02SAL07(),map);
        aRes = accountAction.executeCommand(pars,userSessionPars,request,response,userSession,context);
        if (!aRes.isError())
          vo.setActivitiesAccountDescrSAL07( ((AccountVO)((VOListResponse)aRes).getRows().get(0)).getDescriptionSYS10() );

        pars = new LookupValidationParams(vo.getChargesAccountCodeAcc02SAL07(),map);
        aRes = accountAction.executeCommand(pars,userSessionPars,request,response,userSession,context);
        if (!aRes.isError())
          vo.setChargesAccountDescrSAL07( ((AccountVO)((VOListResponse)aRes).getRows().get(0)).getDescriptionSYS10() );

      }
      else if (!res.isError() && pk.getSubjectTypeREG04().equals(ApplicationConsts.SUBJECT_PEOPLE_CUSTOMER)) {
        PeopleCustomerVO vo = (PeopleCustomerVO)((VOResponse)res).getVo();
        stmt = conn.createStatement();
        if (vo.getPricelistCodeSal01SAL07()!=null) {
          ResultSet rset = stmt.executeQuery(
            "select DESCRIPTION,CURRENCY_CODE_REG03 from SYS10_TRANSLATIONS,SAL01_PRICELISTS where "+
            "SYS10_TRANSLATIONS.PROGRESSIVE=SAL01_PRICELISTS.PROGRESSIVE_SYS10 and "+
            "SYS10_TRANSLATIONS.LANGUAGE_CODE='"+serverLanguageId+"' and SAL01_PRICELISTS.COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01REG04()+"' and "+
            "SAL01_PRICELISTS.PRICELIST_CODE='"+vo.getPricelistCodeSal01SAL07()+"'"
          );
          if (rset.next()) {
            vo.setPricelistDescriptionSYS10(rset.getString(1));
            vo.setCurrencyCodeReg03SAL01(rset.getString(2));
          }
          rset.close();
        }
        if (vo.getBankCodeReg12SAL07()!=null) {
          ResultSet rset = stmt.executeQuery(
            "select DESCRIPTION from REG12_BANKS where "+
            "REG12_BANKS.COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01REG04()+"' and "+
            "REG12_BANKS.BANK_CODE='"+vo.getBankCodeReg12SAL07()+"'"
          );
          if (rset.next())
            vo.setDescriptionREG12(rset.getString(1));
          rset.close();
        }
        if (vo.getAgentProgressiveReg04SAL07()!=null) {
          ResultSet rset = stmt.executeQuery(
            "select REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,SAL10_AGENTS.AGENT_CODE from SAL10_AGENTS,REG04_SUBJECTS where "+
            "SAL10_AGENTS.COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 and "+
            "SAL10_AGENTS.PROGRESSIVE_REG04=REG04_SUBJECTS.PROGRESSIVE and "+
            "SAL10_AGENTS.PROGRESSIVE_REG04="+vo.getAgentProgressiveReg04SAL07()
          );
          if (rset.next()) {
            vo.setAgentName_1REG04(rset.getString(1));
            vo.setAgentName_2REG04(rset.getString(2));
            vo.setAgentCodeSAL10(rset.getString(3));
          }
          rset.close();
        }
        if (vo.getVatCodeReg01SAL07()!=null) {
          ResultSet rset = stmt.executeQuery(
            "select REG01_VATS.VALUE,REG01_VATS.DEDUCTIBLE,SYS10_TRANSLATIONS.DESCRIPTION from REG01_VATS,SYS10_TRANSLATIONS where "+
            "REG01_VATS.VAT_CODE='"+vo.getVatCodeReg01SAL07()+"' and REG01_VATS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and SYS10_TRANSLATIONS.LANGUAGE_CODE='"+serverLanguageId+"'"
          );
          if (rset.next()) {
            vo.setVatValueREG01(rset.getBigDecimal(1));
            vo.setVatDeductibleREG01(rset.getBigDecimal(2));
            vo.setVatDescriptionSYS10(rset.getString(3));
          }
          rset.close();
        }


        HashMap map = new HashMap();
        map.put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01REG04());
        LookupValidationParams pars = new LookupValidationParams(vo.getCreditAccountCodeAcc02SAL07(),map);
        Response aRes = accountAction.executeCommand(pars,userSessionPars,request,response,userSession,context);
        if (!aRes.isError())
          vo.setCreditAccountDescrSAL07( ((AccountVO)((VOListResponse)aRes).getRows().get(0)).getDescriptionSYS10() );

        pars = new LookupValidationParams(vo.getItemsAccountCodeAcc02SAL07(),map);
        aRes = accountAction.executeCommand(pars,userSessionPars,request,response,userSession,context);
        if (!aRes.isError())
          vo.setItemsAccountDescrSAL07( ((AccountVO)((VOListResponse)aRes).getRows().get(0)).getDescriptionSYS10() );

        pars = new LookupValidationParams(vo.getActivitiesAccountCodeAcc02SAL07(),map);
        aRes = accountAction.executeCommand(pars,userSessionPars,request,response,userSession,context);
        if (!aRes.isError())
          vo.setActivitiesAccountDescrSAL07( ((AccountVO)((VOListResponse)aRes).getRows().get(0)).getDescriptionSYS10() );

        pars = new LookupValidationParams(vo.getChargesAccountCodeAcc02SAL07(),map);
        aRes = accountAction.executeCommand(pars,userSessionPars,request,response,userSession,context);
        if (!aRes.isError())
          vo.setChargesAccountDescrSAL07( ((AccountVO)((VOListResponse)aRes).getRows().get(0)).getDescriptionSYS10() );

      }

      Response answer = res;

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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching an existing customer",ex);
      try {
        conn.rollback();
      }
      catch (Exception ex3) {
      }
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        stmt.close();
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
