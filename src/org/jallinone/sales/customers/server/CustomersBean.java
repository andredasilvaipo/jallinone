package org.jallinone.sales.customers.server;

import org.openswing.swing.server.*;

import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.message.send.java.LookupValidationParams;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.sales.customers.java.*;
import org.jallinone.system.progressives.server.CompanyProgressiveUtils;
import org.jallinone.system.server.*;
import java.math.*;
import org.jallinone.subjects.java.*;
import org.jallinone.subjects.server.OrganizationBean;
import org.jallinone.subjects.server.PeopleBean;
import org.jallinone.accounting.accounts.java.AccountVO;
import org.jallinone.accounting.accounts.server.AccountsBean;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;



import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage customers.</p>
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
public class CustomersBean  implements Customers {


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

  private PeopleBean peopleBean;

  private OrganizationBean organizationBean;

  public void setPeopleBean(PeopleBean peopleBean) {
	  this.peopleBean = peopleBean;
  }

  public void setOrganizationBean(OrganizationBean organizationBean) {
	  this.organizationBean = organizationBean;
  }

  private AccountsBean accountAction;

  public void setAccountAction(AccountsBean accountAction) {
    this.accountAction = accountAction;
  }


  public CustomersBean() {
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
   */
  public GridCustomerVO getGridCustomer() {
	  throw new UnsupportedOperationException();
  }


  /**
   * Business logic to execute.
   */
  public VOResponse loadCustomer(CustomerPK pk,String serverLanguageId,String username,ArrayList companiesList, ArrayList customizedFields) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      accountAction.setConn(conn); // use same transaction...

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
          " from REG04_SUBJECTS,SAL07_CUSTOMERS,SYS10_TRANSLATIONS,REG10_PAY_MODES where "+
          "SAL07_CUSTOMERS.COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 and "+
          "SAL07_CUSTOMERS.PROGRESSIVE_REG04=REG04_SUBJECTS.PROGRESSIVE and "+
          "REG04_SUBJECTS.COMPANY_CODE_SYS01=? and REG04_SUBJECTS.PROGRESSIVE=? and "+
					"SAL07_CUSTOMERS.COMPANY_CODE_SYS01=REG10_PAY_MODES.COMPANY_CODE_SYS01 and "+
          "SAL07_CUSTOMERS.PAYMENT_CODE_REG10=REG10_PAY_MODES.PAYMENT_CODE and "+
          "REG10_PAY_MODES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
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
          " from REG04_SUBJECTS,SAL07_CUSTOMERS,SYS10_TRANSLATIONS,REG10_PAY_MODES where "+
          "SAL07_CUSTOMERS.COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 and "+
          "SAL07_CUSTOMERS.PROGRESSIVE_REG04=REG04_SUBJECTS.PROGRESSIVE and "+
          "REG04_SUBJECTS.COMPANY_CODE_SYS01=? and REG04_SUBJECTS.PROGRESSIVE=? and "+
					"SAL07_CUSTOMERS.COMPANY_CODE_SYS01=REG10_PAY_MODES.COMPANY_CODE_SYS01 and "+
          "SAL07_CUSTOMERS.PAYMENT_CODE_REG10=REG10_PAY_MODES.PAYMENT_CODE and "+
          "REG10_PAY_MODES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
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
          new UserSessionParameters(username),
          baseSQL,
          values,
          attribute2dbField,
          pk.getSubjectTypeREG04().equals(ApplicationConsts.SUBJECT_ORGANIZATION_CUSTOMER)?OrganizationCustomerVO.class:PeopleCustomerVO.class,
          "Y",
          "N",
          null,
          true,
          customizedFields
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
        Response aRes = accountAction.validateAccountCode(pars,serverLanguageId,username,companiesList,new ArrayList());
        if (!aRes.isError())
          vo.setCreditAccountDescrSAL07( ((AccountVO)((VOListResponse)aRes).getRows().get(0)).getDescriptionSYS10() );

        pars = new LookupValidationParams(vo.getItemsAccountCodeAcc02SAL07(),map);
        aRes = accountAction.validateAccountCode(pars,serverLanguageId,username,companiesList,new ArrayList());
        if (!aRes.isError())
          vo.setItemsAccountDescrSAL07( ((AccountVO)((VOListResponse)aRes).getRows().get(0)).getDescriptionSYS10() );

        pars = new LookupValidationParams(vo.getActivitiesAccountCodeAcc02SAL07(),map);
        aRes = accountAction.validateAccountCode(pars,serverLanguageId,username,companiesList,new ArrayList());
        if (!aRes.isError())
          vo.setActivitiesAccountDescrSAL07( ((AccountVO)((VOListResponse)aRes).getRows().get(0)).getDescriptionSYS10() );

        pars = new LookupValidationParams(vo.getChargesAccountCodeAcc02SAL07(),map);
        aRes = accountAction.validateAccountCode(pars,serverLanguageId,username,companiesList,new ArrayList());
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
        Response aRes = accountAction.validateAccountCode(pars,serverLanguageId,username,companiesList,new ArrayList());
        if (!aRes.isError())
          vo.setCreditAccountDescrSAL07( ((AccountVO)((VOListResponse)aRes).getRows().get(0)).getDescriptionSYS10() );

        pars = new LookupValidationParams(vo.getItemsAccountCodeAcc02SAL07(),map);
        aRes = accountAction.validateAccountCode(pars,serverLanguageId,username,companiesList,new ArrayList());
        if (!aRes.isError())
          vo.setItemsAccountDescrSAL07( ((AccountVO)((VOListResponse)aRes).getRows().get(0)).getDescriptionSYS10() );

        pars = new LookupValidationParams(vo.getActivitiesAccountCodeAcc02SAL07(),map);
        aRes = accountAction.validateAccountCode(pars,serverLanguageId,username,companiesList,new ArrayList());
        if (!aRes.isError())
          vo.setActivitiesAccountDescrSAL07( ((AccountVO)((VOListResponse)aRes).getRows().get(0)).getDescriptionSYS10() );

        pars = new LookupValidationParams(vo.getChargesAccountCodeAcc02SAL07(),map);
        aRes = accountAction.validateAccountCode(pars,serverLanguageId,username,companiesList,new ArrayList());
        if (!aRes.isError())
          vo.setChargesAccountDescrSAL07( ((AccountVO)((VOListResponse)aRes).getRows().get(0)).getDescriptionSYS10() );

      }

      Response answer = res;
      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching an existing customer",ex);
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
        accountAction.setConn(null);
      } catch (Exception ex) {}
    }

  }



  /**
   * Business logic to execute.
   */
  public VOListResponse loadCustomers(GridParams gridPars,String serverLanguageId,String username,ArrayList companiesList) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      // retrieve companies list...
      String companies = "";
      if (gridPars.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01)!=null) {
        companies = "'"+gridPars.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01)+"'";
      }
      else {
        for(int i=0;i<companiesList.size();i++)
          companies += "'"+companiesList.get(i).toString()+"',";
        companies = companies.substring(0,companies.length()-1);
      }

      String sql =
          "select REG04_SUBJECTS.COMPANY_CODE_SYS01,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,REG04_SUBJECTS.PROGRESSIVE,REG04_SUBJECTS.CITY,REG04_SUBJECTS.PROVINCE,REG04_SUBJECTS.COUNTRY,REG04_SUBJECTS.TAX_CODE,SAL07_CUSTOMERS.CUSTOMER_CODE,REG04_SUBJECTS.SUBJECT_TYPE,"+
          "SAL07_CUSTOMERS.PAYMENT_CODE_REG10,SYS10_TRANSLATIONS.DESCRIPTION,SAL07_CUSTOMERS.VAT_CODE_REG01,SAL07_CUSTOMERS.CREDIT_ACCOUNT_CODE_ACC02,REG04_SUBJECTS.NOTE,"+
          "REG01_VATS.VALUE,REG01_VATS.DEDUCTIBLE,REG01_VATS.DESCRIPTION "+
          " from REG04_SUBJECTS,SYS10_TRANSLATIONS,REG10_PAY_MODES,SAL07_CUSTOMERS "+
          "LEFT OUTER JOIN "+
          "(select REG01_VATS.VAT_CODE,REG01_VATS.VALUE,REG01_VATS.DEDUCTIBLE,SYS10_TRANSLATIONS.DESCRIPTION "+
          " from REG01_VATS,SYS10_TRANSLATIONS where "+
          " REG01_VATS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and SYS10_TRANSLATIONS.LANGUAGE_CODE=? ) REG01_VATS ON "+
          "REG01_VATS.VAT_CODE=SAL07_CUSTOMERS.VAT_CODE_REG01 "+
          "where "+
          "SAL07_CUSTOMERS.COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 and "+
          "SAL07_CUSTOMERS.PROGRESSIVE_REG04=REG04_SUBJECTS.PROGRESSIVE and "+
					"SAL07_CUSTOMERS.COMPANY_CODE_SYS01=REG10_PAY_MODES.COMPANY_CODE_SYS01 and "+
          "SAL07_CUSTOMERS.PAYMENT_CODE_REG10=REG10_PAY_MODES.PAYMENT_CODE and "+
          "REG10_PAY_MODES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "SAL07_CUSTOMERS.ENABLED='Y' and SAL07_CUSTOMERS.COMPANY_CODE_SYS01 in ("+companies+") ";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01REG04","REG04_SUBJECTS.COMPANY_CODE_SYS01");
      attribute2dbField.put("name_1REG04","REG04_SUBJECTS.NAME_1");
      attribute2dbField.put("name_2REG04","REG04_SUBJECTS.NAME_2");
      attribute2dbField.put("progressiveREG04","REG04_SUBJECTS.PROGRESSIVE");
      attribute2dbField.put("cityREG04","REG04_SUBJECTS.CITY");
      attribute2dbField.put("provinceREG04","REG04_SUBJECTS.PROVINCE");
      attribute2dbField.put("countryREG04","REG04_SUBJECTS.COUNTRY");
      attribute2dbField.put("taxCodeREG04","REG04_SUBJECTS.TAX_CODE");
      attribute2dbField.put("customerCodeSAL07","SAL07_CUSTOMERS.CUSTOMER_CODE");
      attribute2dbField.put("subjectTypeREG04","REG04_SUBJECTS.SUBJECT_TYPE");
      attribute2dbField.put("paymentCodeReg10SAL07","SAL07_CUSTOMERS.PAYMENT_CODE_REG10");
      attribute2dbField.put("paymentDescriptionSAL07","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("vatCodeReg01SAL07","SAL07_CUSTOMERS.VAT_CODE_REG01");
      attribute2dbField.put("creditAccountCodeAcc02SAL07","SAL07_CUSTOMERS.CREDIT_ACCOUNT_CODE_ACC02");
      attribute2dbField.put("noteREG04","REG04_SUBJECTS.NOTE");
      attribute2dbField.put("valueREG01","REG01_VATS.VALUE");
      attribute2dbField.put("deductibleREG01","REG01_VATS.DEDUCTIBLE");
      attribute2dbField.put("vatDescriptionSYS10","REG01_VATS.DESCRIPTION");

      ArrayList pars = new ArrayList();
      pars.add(serverLanguageId);
      pars.add(serverLanguageId);

      if (gridPars.getOtherGridParams().get(ApplicationConsts.SUBJECT_TYPE)!=null) {
        sql += " and REG04_SUBJECTS.SUBJECT_TYPE='"+gridPars.getOtherGridParams().get(ApplicationConsts.SUBJECT_TYPE)+"'";
      }

      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          pars,
          attribute2dbField,
          GridCustomerVO.class,
          "Y",
          "N",
          null,
          gridPars,
          50,
          true
      );



      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching customers list",ex);
      throw new Exception(ex.getMessage());
    }
    finally {
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
  public VOResponse updateOrganization(OrganizationVO oldVO,OrganizationVO newVO,String t1,String t2,String serverLanguageId,String username, ArrayList customizedFields) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      peopleBean.setConn(conn);
      organizationBean.setConn(conn);

      Response res = null;
      // update REG04...
      res = organizationBean.update(oldVO,newVO,t2,serverLanguageId,username);
      if (res.isError()) {
        throw new Exception(res.getErrorMessage());
      }

      // update SAL07 table...
      Map attribute2dbField = new HashMap();
      attribute2dbField.put("paymentCodeReg10SAL07","PAYMENT_CODE_REG10");
      attribute2dbField.put("bankCodeReg12SAL07","BANK_CODE_REG12");
      attribute2dbField.put("agentProgressiveReg04SAL07","AGENT_PROGRESSIVE_REG04");
      attribute2dbField.put("trustAmountSAL07","TRUST_AMOUNT");
      attribute2dbField.put("vatCodeReg01SAL07","VAT_CODE_REG01");
      attribute2dbField.put("pricelistCodeSal01SAL07","PRICELIST_CODE_SAL01");
      attribute2dbField.put("creditAccountCodeAcc02SAL07","CREDIT_ACCOUNT_CODE_ACC02");
      attribute2dbField.put("itemsAccountCodeAcc02SAL07","ITEMS_ACCOUNT_CODE_ACC02");
      attribute2dbField.put("activitiesAccountCodeAcc02SAL07","ACTIVITIES_ACCOUNT_CODE_ACC02");
      attribute2dbField.put("chargesAccountCodeAcc02SAL07","CHARGES_ACCOUNT_CODE_ACC02");

      HashSet pkAttributes = new HashSet();
      attribute2dbField.put("companyCodeSys01REG04","COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveREG04","PROGRESSIVE_REG04");

      res = CustomizeQueryUtil.updateTable(
          conn,
          new UserSessionParameters(username),
          pkAttributes,
          (ValueObject)oldVO,
          (ValueObject)newVO,
          "SAL07_CUSTOMERS",
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
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating an existing customer",ex);
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
          peopleBean.setConn(null);
          organizationBean.setConn(null);
      } catch (Exception ex) {}
    }
  }


  /**
   * Business logic to execute.
   */
  public VOResponse updatePeople(PeopleVO oldVO,PeopleVO newVO,String t1,String t2,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    Statement stmt = null;

    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      peopleBean.setConn(conn);
      organizationBean.setConn(conn);

      Response res = null;
      // update REG04...
      res = peopleBean.update(oldVO,newVO,t1,serverLanguageId,username);

      if (res.isError()) {
        throw new Exception(res.getErrorMessage());
      }

      // update SAL07 table...
      Map attribute2dbField = new HashMap();
      attribute2dbField.put("paymentCodeReg10SAL07","PAYMENT_CODE_REG10");
      attribute2dbField.put("bankCodeReg12SAL07","BANK_CODE_REG12");
      attribute2dbField.put("agentProgressiveReg04SAL07","AGENT_PROGRESSIVE_REG04");
      attribute2dbField.put("trustAmountSAL07","TRUST_AMOUNT");
      attribute2dbField.put("vatCodeReg01SAL07","VAT_CODE_REG01");
      attribute2dbField.put("pricelistCodeSal01SAL07","PRICELIST_CODE_SAL01");
      attribute2dbField.put("creditAccountCodeAcc02SAL07","CREDIT_ACCOUNT_CODE_ACC02");
      attribute2dbField.put("itemsAccountCodeAcc02SAL07","ITEMS_ACCOUNT_CODE_ACC02");
      attribute2dbField.put("activitiesAccountCodeAcc02SAL07","ACTIVITIES_ACCOUNT_CODE_ACC02");
      attribute2dbField.put("chargesAccountCodeAcc02SAL07","CHARGES_ACCOUNT_CODE_ACC02");

      HashSet pkAttributes = new HashSet();
      attribute2dbField.put("companyCodeSys01REG04","COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveREG04","PROGRESSIVE_REG04");

      res = CustomizeQueryUtil.updateTable(
          conn,
          new UserSessionParameters(username),
          pkAttributes,
          (ValueObject)oldVO,
          (ValueObject)newVO,
          "SAL07_CUSTOMERS",
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
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating an existing customer",ex);
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
          peopleBean.setConn(null);
          organizationBean.setConn(null);
      } catch (Exception ex) {}
    }
  }





  /**
   * Business logic to execute.
   */
  public VOListResponse validateCustomerCode(LookupValidationParams lookupPars,String serverLanguageId,String username,ArrayList companiesList) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      // retrieve companies list...
      String companies = "";

      // if client transmit a specific function code then use it to filter company code list by INSERT authorizations,
      // otherwise retrieve company code list from READ authorizations...
      if (lookupPars.getLookupValidationParameters().get(ApplicationConsts.COMPANY_CODE_SYS01)!=null) {
    	  companies = "'"+lookupPars.getLookupValidationParameters().get(ApplicationConsts.COMPANY_CODE_SYS01)+"',";
      }
      else if (lookupPars.getLookupValidationParameters().get(ApplicationConsts.FILTER_COMPANY_FOR_INSERT)!=null) {
    	  for(int i=0;i<companiesList.size();i++)
    		  companies += "'"+companiesList.get(i).toString()+"',";
      } else {
    	  for(int i=0;i<companiesList.size();i++)
    		  companies += "'"+companiesList.get(i).toString()+"',";
      }

      if (companies.length()>0)
    	  companies = companies.substring(0,companies.length()-1);


      String sql =
    	  "select REG04_SUBJECTS.COMPANY_CODE_SYS01,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,REG04_SUBJECTS.PROGRESSIVE,REG04_SUBJECTS.CITY,REG04_SUBJECTS.PROVINCE,REG04_SUBJECTS.COUNTRY,REG04_SUBJECTS.TAX_CODE,SAL07_CUSTOMERS.CUSTOMER_CODE,REG04_SUBJECTS.SUBJECT_TYPE,"+
    	  "SAL07_CUSTOMERS.PAYMENT_CODE_REG10,SYS10_TRANSLATIONS.DESCRIPTION,SAL07_CUSTOMERS.VAT_CODE_REG01,SAL07_CUSTOMERS.CREDIT_ACCOUNT_CODE_ACC02,"+
          "REG04_SUBJECTS.NOTE,REG01_VATS.VALUE,REG01_VATS.DEDUCTIBLE,REG01_VATS.DESCRIPTION "+
          " from REG04_SUBJECTS,SYS10_TRANSLATIONS,REG10_PAY_MODES,SAL07_CUSTOMERS "+
          "LEFT OUTER JOIN "+
          "(select REG01_VATS.VAT_CODE,REG01_VATS.VALUE,REG01_VATS.DEDUCTIBLE,SYS10_TRANSLATIONS.DESCRIPTION "+
          " from REG01_VATS,SYS10_TRANSLATIONS where "+
          " REG01_VATS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and SYS10_TRANSLATIONS.LANGUAGE_CODE=?) REG01_VATS ON "+
          "REG01_VATS.VAT_CODE=SAL07_CUSTOMERS.VAT_CODE_REG01  "+
          "where "+
          "SAL07_CUSTOMERS.COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 and "+
          "SAL07_CUSTOMERS.PROGRESSIVE_REG04=REG04_SUBJECTS.PROGRESSIVE and "+
					"SAL07_CUSTOMERS.COMPANY_CODE_SYS01=REG10_PAY_MODES.COMPANY_CODE_SYS01 and "+
          "SAL07_CUSTOMERS.PAYMENT_CODE_REG10=REG10_PAY_MODES.PAYMENT_CODE and "+
          "REG10_PAY_MODES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and SAL07_CUSTOMERS.COMPANY_CODE_SYS01 in ("+companies+") and "+
          "SAL07_CUSTOMERS.ENABLED='Y' and SAL07_CUSTOMERS.CUSTOMER_CODE=? ";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01REG04","REG04_SUBJECTS.COMPANY_CODE_SYS01");
      attribute2dbField.put("name_1REG04","REG04_SUBJECTS.NAME_1");
      attribute2dbField.put("name_2REG04","REG04_SUBJECTS.NAME_2");
      attribute2dbField.put("progressiveREG04","REG04_SUBJECTS.PROGRESSIVE");
      attribute2dbField.put("cityREG04","REG04_SUBJECTS.CITY");
      attribute2dbField.put("provinceREG04","REG04_SUBJECTS.PROVINCE");
      attribute2dbField.put("countryREG04","REG04_SUBJECTS.COUNTRY");
      attribute2dbField.put("taxCodeREG04","REG04_SUBJECTS.TAX_CODE");
      attribute2dbField.put("customerCodeSAL07","SAL07_CUSTOMERS.CUSTOMER_CODE");
      attribute2dbField.put("subjectTypeREG04","REG04_SUBJECTS.SUBJECT_TYPE");
      attribute2dbField.put("paymentCodeReg10SAL07","SAL07_CUSTOMERS.PAYMENT_CODE_REG10");
      attribute2dbField.put("paymentDescriptionSAL07","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("vatCodeReg01SAL07","SAL07_CUSTOMERS.VAT_CODE_REG01");
      attribute2dbField.put("creditAccountCodeAcc02SAL07","SAL07_CUSTOMERS.CREDIT_ACCOUNT_CODE_ACC02");
      attribute2dbField.put("noteREG04","REG04_SUBJECTS.NOTE");
      attribute2dbField.put("valueREG01","REG01_VATS.VALUE");
      attribute2dbField.put("deductibleREG01","REG01_VATS.DEDUCTIBLE");
      attribute2dbField.put("vatDescriptionSYS10","REG01_VATS.DESCRIPTION");


      ArrayList pars = new ArrayList();
      pars.add(serverLanguageId);
      pars.add(serverLanguageId);
      pars.add(lookupPars.getCode());

      if (lookupPars.getLookupValidationParameters().get(ApplicationConsts.SUBJECT_TYPE)!=null) {
        sql += " and REG04_SUBJECTS.SUBJECT_TYPE='"+lookupPars.getLookupValidationParameters().get(ApplicationConsts.SUBJECT_TYPE)+"'";
      }

      GridParams gridPars = new GridParams();
      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          pars,
          attribute2dbField,
          GridCustomerVO.class,
          "Y",
          "N",
          null,
          gridPars,
          50,
          true
      );

      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while validating a customer code",ex);
      throw new Exception(ex.getMessage());
    }
    finally {
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
  public VOResponse insertOrganization(OrganizationCustomerVO vo,String t1,String t2,String serverLanguageId,String username,ArrayList companiesList, ArrayList customizedFields) throws Throwable {
    PreparedStatement pstmt = null;

    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      peopleBean.setConn(conn);
      organizationBean.setConn(conn);
      String companyCode = companiesList.get(0).toString();
      if (vo.getCompanyCodeSys01REG04()==null)
        vo.setCompanyCodeSys01REG04(companyCode);


      // check if customer code is defined: if it's not defined then it will be defined as a progressive...
      if (vo.getCustomerCodeSAL07()==null || vo.getCustomerCodeSAL07().trim().equals("")) {
    	  vo.setCustomerCodeSAL07( String.valueOf(CompanyProgressiveUtils.getConsecutiveProgressive(vo.getCompanyCodeSys01REG04(),"SAL07_CUSTOMERS",vo.getCompanyCodeSys01REG04(),conn).intValue()) );
      }

      // test if there exists a customer with the same customer code...
      pstmt = conn.prepareStatement("select CUSTOMER_CODE from SAL07_CUSTOMERS where COMPANY_CODE_SYS01=? and CUSTOMER_CODE=?");
      pstmt.setString(1,vo.getCompanyCodeSys01REG04());
      pstmt.setString(2,vo.getCustomerCodeSAL07());
      ResultSet rset = pstmt.executeQuery();
      if (rset.next()) {
    	  rset.close();
    	  throw new Exception("customer code already exist.");
      }
      rset.close();

      organizationBean.insert(true,vo,t2,serverLanguageId,username);
      vo.setEnabledSAL07("Y");


      // insert into SAL07...
      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01REG04","COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveREG04","PROGRESSIVE_REG04");
      attribute2dbField.put("customerCodeSAL07","CUSTOMER_CODE");
      attribute2dbField.put("paymentCodeReg10SAL07","PAYMENT_CODE_REG10");
      attribute2dbField.put("pricelistCodeSal01SAL07","PRICELIST_CODE_SAL01");
      attribute2dbField.put("bankCodeReg12SAL07","BANK_CODE_REG12");
      attribute2dbField.put("agentProgressiveReg04SAL07","AGENT_PROGRESSIVE_REG04");
      attribute2dbField.put("trustAmountSAL07","TRUST_AMOUNT");
      attribute2dbField.put("enabledSAL07","ENABLED");
      attribute2dbField.put("vatCodeReg01SAL07","VAT_CODE_REG01");
      attribute2dbField.put("creditAccountCodeAcc02SAL07","CREDIT_ACCOUNT_CODE_ACC02");
      attribute2dbField.put("itemsAccountCodeAcc02SAL07","ITEMS_ACCOUNT_CODE_ACC02");
      attribute2dbField.put("activitiesAccountCodeAcc02SAL07","ACTIVITIES_ACCOUNT_CODE_ACC02");
      attribute2dbField.put("chargesAccountCodeAcc02SAL07","CHARGES_ACCOUNT_CODE_ACC02");

      Response res = CustomizeQueryUtil.insertTable(
          conn,
          new UserSessionParameters(username),
          (ValueObject)vo,
          "SAL07_CUSTOMERS",
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
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while inserting a new customer",ex);
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
          peopleBean.setConn(null);
          organizationBean.setConn(null);
      } catch (Exception ex) {}
    }
  }



  /**
   * Business logic to execute.
   */
  public VOResponse insertPeople(PeopleCustomerVO vo,String t1,String t2,String serverLanguageId,String username,ArrayList companiesList, ArrayList customizedFields) throws Throwable {
    PreparedStatement pstmt = null;

    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      peopleBean.setConn(conn);
      organizationBean.setConn(conn);

      String companyCode = companiesList.get(0).toString();

      if (vo.getCompanyCodeSys01REG04()==null)
        vo.setCompanyCodeSys01REG04(companyCode);

      // check if customer code is defined: if it's not defined then it will be defined as a progressive...
      if (vo.getCustomerCodeSAL07()==null || vo.getCustomerCodeSAL07().trim().equals("")) {
    	  vo.setCustomerCodeSAL07( String.valueOf(CompanyProgressiveUtils.getInternalProgressive(vo.getCompanyCodeSys01REG04(),"SAL07_CUSTOMERS",vo.getCompanyCodeSys01REG04(),conn).intValue()) );
      }

      // test if there exists a customer with the same customer code...
      pstmt = conn.prepareStatement("select CUSTOMER_CODE from SAL07_CUSTOMERS where COMPANY_CODE_SYS01=? and CUSTOMER_CODE=?");
      pstmt.setString(1,vo.getCompanyCodeSys01REG04());
      pstmt.setString(2,vo.getCustomerCodeSAL07());
      ResultSet rset = pstmt.executeQuery();
      if (rset.next()) {
    	  rset.close();
    	  throw new Exception("customer code already exist.");
      }
      rset.close();

      peopleBean.insert(true,vo,t1,serverLanguageId,username);
      vo.setEnabledSAL07("Y");


      // insert into SAL07...
      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01REG04","COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveREG04","PROGRESSIVE_REG04");
      attribute2dbField.put("customerCodeSAL07","CUSTOMER_CODE");
      attribute2dbField.put("paymentCodeReg10SAL07","PAYMENT_CODE_REG10");
      attribute2dbField.put("pricelistCodeSal01SAL07","PRICELIST_CODE_SAL01");
      attribute2dbField.put("bankCodeReg12SAL07","BANK_CODE_REG12");
      attribute2dbField.put("agentProgressiveReg04SAL07","AGENT_PROGRESSIVE_REG04");
      attribute2dbField.put("trustAmountSAL07","TRUST_AMOUNT");
      attribute2dbField.put("enabledSAL07","ENABLED");
      attribute2dbField.put("vatCodeReg01SAL07","VAT_CODE_REG01");
      attribute2dbField.put("creditAccountCodeAcc02SAL07","CREDIT_ACCOUNT_CODE_ACC02");
      attribute2dbField.put("itemsAccountCodeAcc02SAL07","ITEMS_ACCOUNT_CODE_ACC02");
      attribute2dbField.put("activitiesAccountCodeAcc02SAL07","ACTIVITIES_ACCOUNT_CODE_ACC02");
      attribute2dbField.put("chargesAccountCodeAcc02SAL07","CHARGES_ACCOUNT_CODE_ACC02");

      Response res = CustomizeQueryUtil.insertTable(
          conn,
          new UserSessionParameters(username),
          (ValueObject)vo,
          "SAL07_CUSTOMERS",
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
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while inserting a new customer",ex);
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
          peopleBean.setConn(null);
          organizationBean.setConn(null);
      } catch (Exception ex) {}
    }
  }





  /**
   * Business logic to execute.
   */
  public VOResponse deleteCustomers(ArrayList list,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      stmt = conn.createStatement();

      Subject vo = null;

      for(int i=0;i<list.size();i++) {
        vo = (Subject)list.get(i);

        // logically delete the record in REG04...
        stmt.execute(
            "update REG04_SUBJECTS set ENABLED='N' where "+
            "COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01REG04()+"' and "+
            "PROGRESSIVE='"+vo.getProgressiveREG04()+"'"
        );

        // logically delete the record in SAL07...
        stmt.execute(
            "update SAL07_CUSTOMERS set ENABLED='N' where "+
            "COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01REG04()+"' and "+
            "PROGRESSIVE_REG04='"+vo.getProgressiveREG04()+"'"
        );
      }

      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting an existing customer",ex);
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
    }

  }



}

