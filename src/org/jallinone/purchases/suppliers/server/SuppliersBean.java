package org.jallinone.purchases.suppliers.server;

import org.openswing.swing.server.*;

import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.message.send.java.LookupValidationParams;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.purchases.suppliers.java.DetailSupplierVO;
import org.jallinone.purchases.suppliers.java.GridSupplierVO;
import org.jallinone.system.progressives.server.CompanyProgressiveUtils;
import org.jallinone.system.server.*;
import java.math.*;
import org.jallinone.subjects.java.*;
import org.jallinone.subjects.java.*;
import org.jallinone.subjects.server.OrganizationBean;
import org.jallinone.accounting.accounts.java.AccountVO;
import org.jallinone.accounting.accounts.server.AccountsBean;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;




import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage suppliers.</p>
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
public class SuppliersBean  implements Suppliers {


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



  private OrganizationBean organizationBean;

  public void setOrganizationBean(OrganizationBean organizationBean) {
	  this.organizationBean = organizationBean;
  }


  private AccountsBean accountAction;

  public void setAccountAction(AccountsBean accountAction) {
    this.accountAction = accountAction;
  }


  public SuppliersBean() {
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
   */
  public GridSupplierVO getGridSupplier() {
	  throw new UnsupportedOperationException();
  }


  /**
   * Business logic to execute.
   */
  public VOResponse loadSupplier(SubjectPK pk,String serverLanguageId,String username,ArrayList companiesList,ArrayList customizedFields) throws Throwable {
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
      attribute2dbField.put("supplierCodePUR01","PUR01_SUPPLIERS.SUPPLIER_CODE");
      attribute2dbField.put("zipREG04","REG04_SUBJECTS.ZIP");
      attribute2dbField.put("sexREG04","REG04_SUBJECTS.SEX");
      attribute2dbField.put("phoneNumberREG04","REG04_SUBJECTS.PHONE_NUMBER");
      attribute2dbField.put("faxNumberREG04","REG04_SUBJECTS.FAX_NUMBER");
      attribute2dbField.put("emailAddressREG04","REG04_SUBJECTS.EMAIL_ADDRESS");
      attribute2dbField.put("webSiteREG04","REG04_SUBJECTS.WEB_SITE");
      attribute2dbField.put("lawfulSiteREG04","REG04_SUBJECTS.LAWFUL_SITE");
      attribute2dbField.put("noteREG04","REG04_SUBJECTS.NOTE");
      attribute2dbField.put("paymentCodeReg10PUR01","PUR01_SUPPLIERS.PAYMENT_CODE_REG10");
      attribute2dbField.put("bankCodeReg12PUR01","PUR01_SUPPLIERS.BANK_CODE_REG12");
      attribute2dbField.put("paymentDescriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("debitAccountCodeAcc02PUR01","PUR01_SUPPLIERS.DEBIT_ACCOUNT_CODE_ACC02");
      attribute2dbField.put("costsAccountCodeAcc02PUR01","PUR01_SUPPLIERS.COSTS_ACCOUNT_CODE_ACC02");

      HashSet pkAttributes = new HashSet();
      pkAttributes.add("companyCodeSys01REG04");
      pkAttributes.add("progressiveREG04");

      String baseSQL =
        "select REG04_SUBJECTS.COMPANY_CODE_SYS01,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,REG04_SUBJECTS.PROGRESSIVE,REG04_SUBJECTS.ADDRESS,REG04_SUBJECTS.CITY,REG04_SUBJECTS.PROVINCE,REG04_SUBJECTS.COUNTRY,REG04_SUBJECTS.TAX_CODE,PUR01_SUPPLIERS.SUPPLIER_CODE,"+
        "REG04_SUBJECTS.ZIP,REG04_SUBJECTS.PHONE_NUMBER,REG04_SUBJECTS.FAX_NUMBER,REG04_SUBJECTS.EMAIL_ADDRESS,"+
        "REG04_SUBJECTS.WEB_SITE,REG04_SUBJECTS.LAWFUL_SITE,REG04_SUBJECTS.NOTE,"+
        "PUR01_SUPPLIERS.PAYMENT_CODE_REG10,PUR01_SUPPLIERS.BANK_CODE_REG12,SYS10_TRANSLATIONS.DESCRIPTION,"+
        "PUR01_SUPPLIERS.DEBIT_ACCOUNT_CODE_ACC02,PUR01_SUPPLIERS.COSTS_ACCOUNT_CODE_ACC02 "+
        " from REG04_SUBJECTS,PUR01_SUPPLIERS,SYS10_TRANSLATIONS,REG10_PAY_MODES where "+
        "PUR01_SUPPLIERS.COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 and "+
        "PUR01_SUPPLIERS.PROGRESSIVE_REG04=REG04_SUBJECTS.PROGRESSIVE and "+
        "REG04_SUBJECTS.COMPANY_CODE_SYS01=? and REG04_SUBJECTS.PROGRESSIVE=? and "+
				"PUR01_SUPPLIERS.COMPANY_CODE_SYS01=REG10_PAY_MODES.COMPANY_CODE_SYS01 and "+
        "PUR01_SUPPLIERS.PAYMENT_CODE_REG10=REG10_PAY_MODES.PAYMENT_CODE and "+
        "REG10_PAY_MODES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
        "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
        "PUR01_SUPPLIERS.ENABLED='Y' ";

      ArrayList values = new ArrayList();
      values.add(pk.getCompanyCodeSys01REG04());
      values.add(pk.getProgressiveREG04());
      values.add(serverLanguageId);

      // read from REG04/PUR01 tables...
      Response res = CustomizeQueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          baseSQL,
          values,
          attribute2dbField,
          DetailSupplierVO.class,
          "Y",
          "N",
          null,
          true,
          customizedFields
      );

      if (!res.isError()) {
        DetailSupplierVO vo = (DetailSupplierVO)((VOResponse)res).getVo();
        stmt = conn.createStatement();
        if (vo.getBankCodeReg12PUR01()!=null) {
          ResultSet rset = stmt.executeQuery(
            "select DESCRIPTION from REG12_BANKS where "+
            "REG12_BANKS.BANK_CODE='"+vo.getBankCodeReg12PUR01()+"'"
          );
          if (rset.next())
            vo.setDescriptionREG12(rset.getString(1));
          rset.close();
        }


        HashMap map = new HashMap();
        map.put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01REG04());
        LookupValidationParams pars = new LookupValidationParams(vo.getDebitAccountCodeAcc02PUR01(),map);
        Response aRes = accountAction.validateAccountCode(pars,serverLanguageId,username,companiesList,new ArrayList());
        if (!aRes.isError())
          vo.setDebitAccountDescrPUR01( ((AccountVO)((VOListResponse)aRes).getRows().get(0)).getDescriptionSYS10() );

        pars = new LookupValidationParams(vo.getCostsAccountCodeAcc02PUR01(),map);
        aRes = accountAction.validateAccountCode(pars,serverLanguageId,username,companiesList,new ArrayList());
        if (!aRes.isError())
          vo.setCostsAccountDescrPUR01( ((AccountVO)((VOListResponse)aRes).getRows().get(0)).getDescriptionSYS10() );

      }

      Response answer = res;
      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching an existing supplier",ex);
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
  public VOListResponse loadSuppliers(GridParams gridPars,String serverLanguageId,String username,ArrayList companiesList) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      // retrieve companies list...
      String companies = "";

      // if client transmit a specific function code then use it to filter company code list by INSERT authorizations,
      // otherwise retrieve company code list from READ authorizations...
      if (gridPars.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01)!=null) {
        companies = "'"+gridPars.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01)+"',";
      }
      else if (gridPars.getOtherGridParams().get(ApplicationConsts.FILTER_COMPANY_FOR_INSERT)!=null) {
        for(int i=0;i<companiesList.size();i++)
            companies += "'"+companiesList.get(i).toString()+"',";
      } else {
        for(int i=0;i<companiesList.size();i++)
          companies += "'"+companiesList.get(i).toString()+"',";
      }

      if (companies.length()>0)
        companies = companies.substring(0,companies.length()-1);

      String sql =
          "select REG04_SUBJECTS.COMPANY_CODE_SYS01,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,REG04_SUBJECTS.PROGRESSIVE,REG04_SUBJECTS.CITY,REG04_SUBJECTS.PROVINCE,REG04_SUBJECTS.COUNTRY,REG04_SUBJECTS.TAX_CODE,PUR01_SUPPLIERS.SUPPLIER_CODE,"+
          "PUR01_SUPPLIERS.PAYMENT_CODE_REG10,SYS10_TRANSLATIONS.DESCRIPTION,PUR01_SUPPLIERS.DEBIT_ACCOUNT_CODE_ACC02,REG04_SUBJECTS.NOTE "+
          " from REG04_SUBJECTS,PUR01_SUPPLIERS,REG10_PAY_MODES,SYS10_TRANSLATIONS where "+
          "PUR01_SUPPLIERS.COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 and "+
          "PUR01_SUPPLIERS.PROGRESSIVE_REG04=REG04_SUBJECTS.PROGRESSIVE and "+
          "PUR01_SUPPLIERS.ENABLED='Y' and PUR01_SUPPLIERS.COMPANY_CODE_SYS01 in ("+companies+") and "+
					"PUR01_SUPPLIERS.COMPANY_CODE_SYS01=REG10_PAY_MODES.COMPANY_CODE_SYS01 and "+
          "PUR01_SUPPLIERS.PAYMENT_CODE_REG10=REG10_PAY_MODES.PAYMENT_CODE and "+
          "REG10_PAY_MODES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=?";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01REG04","REG04_SUBJECTS.COMPANY_CODE_SYS01");
      attribute2dbField.put("name_1REG04","REG04_SUBJECTS.NAME_1");
      attribute2dbField.put("name_2REG04","REG04_SUBJECTS.NAME_2");
      attribute2dbField.put("progressiveREG04","REG04_SUBJECTS.PROGRESSIVE");
      attribute2dbField.put("cityREG04","REG04_SUBJECTS.CITY");
      attribute2dbField.put("provinceREG04","REG04_SUBJECTS.PROVINCE");
      attribute2dbField.put("countryREG04","REG04_SUBJECTS.COUNTRY");
      attribute2dbField.put("taxCodeREG04","REG04_SUBJECTS.TAX_CODE");
      attribute2dbField.put("supplierCodePUR01","PUR01_SUPPLIERS.SUPPLIER_CODE");
      attribute2dbField.put("paymentCodeReg10PUR01","PUR01_SUPPLIERS.PAYMENT_CODE_REG10");
      attribute2dbField.put("paymentDescriptionPUR01","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("debitAccountCodeAcc02PUR01","PUR01_SUPPLIERS.DEBIT_ACCOUNT_CODE_ACC02");
      attribute2dbField.put("noteREG04","REG04_SUBJECTS.NOTE");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);

      if (gridPars.getOtherGridParams().get(ApplicationConsts.ITEM)!=null) {
        sql +=
            " AND EXISTS(SELECT * FROM PUR04_SUPPLIER_PRICES WHERE "+
            "PUR04_SUPPLIER_PRICES.COMPANY_CODE_SYS01=PUR01_SUPPLIERS.COMPANY_CODE_SYS01 AND "+
            "PUR04_SUPPLIER_PRICES.PROGRESSIVE_REG04=PUR01_SUPPLIERS.PROGRESSIVE_REG04 AND "+
            "PUR04_SUPPLIER_PRICES.ITEM_CODE_ITM01=? AND "+
            "PUR04_SUPPLIER_PRICES.START_DATE<=? AND "+
            "PUR04_SUPPLIER_PRICES.END_DATE>=? ) ";
        java.sql.Date today = new java.sql.Date(System.currentTimeMillis());

        values.add(gridPars.getOtherGridParams().get(ApplicationConsts.ITEM));
        values.add(today);
        values.add(today);
      }

      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          GridSupplierVO.class,
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
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching suppliers list",ex);
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
  public VOResponse updateSupplier(OrganizationVO oldVO,OrganizationVO newVO,String t1,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    Statement stmt = null;

    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      organizationBean.setConn(conn);


      Response res = null;
      // update REG04...
      res = organizationBean.update((OrganizationVO)oldVO,(OrganizationVO)newVO,t1,serverLanguageId,username);

      if (res.isError()) {
        throw new Exception(res.getErrorMessage());
      }

      // update PUR01 table...
      Map attribute2dbField = new HashMap();
      attribute2dbField.put("paymentCodeReg10PUR01","PAYMENT_CODE_REG10");
      attribute2dbField.put("bankCodeReg12PUR01","BANK_CODE_REG12");
      attribute2dbField.put("debitAccountCodeAcc02PUR01","DEBIT_ACCOUNT_CODE_ACC02");
      attribute2dbField.put("costsAccountCodeAcc02PUR01","COSTS_ACCOUNT_CODE_ACC02");

      HashSet pkAttributes = new HashSet();
      attribute2dbField.put("companyCodeSys01REG04","COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveREG04","PROGRESSIVE_REG04");

      res = CustomizeQueryUtil.updateTable(
          conn,
          new UserSessionParameters(username),
          pkAttributes,
          (ValueObject)oldVO,
          (ValueObject)newVO,
          "PUR01_SUPPLIERS",
          attribute2dbField,
          "Y",
          "N",
          null,
          true,
          customizedFields
      );

      Response answer =  res;
      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating an existing supplier",ex);
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
    	  organizationBean.setConn(null);
      } catch (Exception ex) {}
    }
  }




  /**
   * Business logic to execute.
   */
  public VOListResponse validateSupplierCode(LookupValidationParams lookupPars,String serverLanguageId,String username,ArrayList companiesList) throws Throwable {
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
          "select REG04_SUBJECTS.COMPANY_CODE_SYS01,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,REG04_SUBJECTS.PROGRESSIVE,REG04_SUBJECTS.CITY,REG04_SUBJECTS.PROVINCE,REG04_SUBJECTS.COUNTRY,REG04_SUBJECTS.TAX_CODE,PUR01_SUPPLIERS.SUPPLIER_CODE,"+
          "PUR01_SUPPLIERS.PAYMENT_CODE_REG10,SYS10_TRANSLATIONS.DESCRIPTION,PUR01_SUPPLIERS.DEBIT_ACCOUNT_CODE_ACC02,REG04_SUBJECTS.NOTE "+
          " from REG04_SUBJECTS,PUR01_SUPPLIERS,REG10_PAY_MODES,SYS10_TRANSLATIONS where "+
          "PUR01_SUPPLIERS.COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 and "+
          "PUR01_SUPPLIERS.PROGRESSIVE_REG04=REG04_SUBJECTS.PROGRESSIVE and "+
          "PUR01_SUPPLIERS.ENABLED='Y' and PUR01_SUPPLIERS.COMPANY_CODE_SYS01 in ("+companies+") and "+
					"PUR01_SUPPLIERS.COMPANY_CODE_SYS01=REG10_PAY_MODES.COMPANY_CODE_SYS01 and "+
          "PUR01_SUPPLIERS.PAYMENT_CODE_REG10=REG10_PAY_MODES.PAYMENT_CODE and "+
          "REG10_PAY_MODES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and PUR01_SUPPLIERS.SUPPLIER_CODE=?";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01REG04","REG04_SUBJECTS.COMPANY_CODE_SYS01");
      attribute2dbField.put("name_1REG04","REG04_SUBJECTS.NAME_1");
      attribute2dbField.put("name_2REG04","REG04_SUBJECTS.NAME_2");
      attribute2dbField.put("progressiveREG04","REG04_SUBJECTS.PROGRESSIVE");
      attribute2dbField.put("cityREG04","REG04_SUBJECTS.CITY");
      attribute2dbField.put("provinceREG04","REG04_SUBJECTS.PROVINCE");
      attribute2dbField.put("countryREG04","REG04_SUBJECTS.COUNTRY");
      attribute2dbField.put("taxCodeREG04","REG04_SUBJECTS.TAX_CODE");
      attribute2dbField.put("supplierCodePUR01","PUR01_SUPPLIERS.SUPPLIER_CODE");
      attribute2dbField.put("paymentCodeReg10PUR01","PUR01_SUPPLIERS.PAYMENT_CODE_REG10");
      attribute2dbField.put("paymentDescriptionPUR01","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("debitAccountCodeAcc02PUR01","PUR01_SUPPLIERS.DEBIT_ACCOUNT_CODE_ACC02");
      attribute2dbField.put("noteREG04","REG04_SUBJECTS.NOTE");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(lookupPars.getCode());

      if (lookupPars.getLookupValidationParameters().get(ApplicationConsts.ITEM)!=null) {
        sql +=
            " AND EXISTS(SELECT * FROM PUR04_SUPPLIER_PRICES WHERE "+
            "PUR04_SUPPLIER_PRICES.COMPANY_CODE_SYS01=PUR01_SUPPLIERS.COMPANY_CODE_SYS01 AND "+
            "PUR04_SUPPLIER_PRICES.PROGRESSIVE_REG04=PUR01_SUPPLIERS.PROGRESSIVE_REG04 AND "+
            "PUR04_SUPPLIER_PRICES.ITEM_CODE_ITM01=? AND "+
            "PUR04_SUPPLIER_PRICES.START_DATE<=? AND "+
            "PUR04_SUPPLIER_PRICES.END_DATE>=? ) ";
        java.sql.Date today = new java.sql.Date(System.currentTimeMillis());

        values.add(lookupPars.getLookupValidationParameters().get(ApplicationConsts.ITEM));
        values.add(today);
        values.add(today);
      }


      GridParams gridParams = new GridParams();

      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          GridSupplierVO.class,
          "Y",
          "N",
          null,
          gridParams,
          50,
          true
      );

      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while validating a supplier code",ex);
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
  public VOResponse insertSupplier(DetailSupplierVO vo,String t1,String serverLanguageId,String username,ArrayList companiesList,ArrayList customizedFields) throws Throwable {
    PreparedStatement pstmt = null;

    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      organizationBean.setConn(conn);
      String companyCode = companiesList.get(0).toString();

      if (vo.getCompanyCodeSys01REG04()==null)
        vo.setCompanyCodeSys01REG04(companyCode);
      vo.setSubjectTypeREG04(ApplicationConsts.SUBJECT_SUPPLIER);

      // check if supplier code is defined: if it's not defined then it will be defined as a progressive...
      if (vo.getSupplierCodePUR01()==null || vo.getSupplierCodePUR01().trim().equals("")) {
        vo.setSupplierCodePUR01( String.valueOf(CompanyProgressiveUtils.getConsecutiveProgressive(vo.getCompanyCodeSys01REG04(),"PUR01_SUPPLIERS",vo.getCompanyCodeSys01REG04(),conn).intValue()) );
      }

      // insert into REG04...
      // test if there exists a supplier with the same supplier code...
      pstmt = conn.prepareStatement("select SUPPLIER_CODE from PUR01_SUPPLIERS where COMPANY_CODE_SYS01=? and SUPPLIER_CODE=?");
      pstmt.setString(1,vo.getCompanyCodeSys01REG04());
      pstmt.setString(2,vo.getSupplierCodePUR01());
      ResultSet rset = pstmt.executeQuery();
      if (rset.next()) {
        rset.close();
        VOResponse res = new VOResponse();
        res.setErrorMessage("supplier code already exist.");
        return res;
      }
      rset.close();

      organizationBean.insert(true,vo,t1,serverLanguageId,username);
      vo.setEnabledPUR01("Y");

      // insert into PUR01...
      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01REG04","COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveREG04","PROGRESSIVE_REG04");
      attribute2dbField.put("supplierCodePUR01","SUPPLIER_CODE");
      attribute2dbField.put("paymentCodeReg10PUR01","PAYMENT_CODE_REG10");
      attribute2dbField.put("bankCodeReg12PUR01","BANK_CODE_REG12");
      attribute2dbField.put("enabledPUR01","ENABLED");
      attribute2dbField.put("debitAccountCodeAcc02PUR01","DEBIT_ACCOUNT_CODE_ACC02");
      attribute2dbField.put("costsAccountCodeAcc02PUR01","COSTS_ACCOUNT_CODE_ACC02");

      Response res = CustomizeQueryUtil.insertTable(
          conn,
          new UserSessionParameters(username),
          (ValueObject)vo,
          "PUR01_SUPPLIERS",
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
    	Logger.error(username,this.getClass().getName(),"executeCommand","Error while inserting a new supplier",ex);
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
    	  organizationBean.setConn(null);
      } catch (Exception ex) {}
    }
  }




  /**
   * Business logic to execute.
   */
  public VOResponse deleteSuppliers(ArrayList list,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    PreparedStatement pstmt = null;

    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;



      stmt = conn.createStatement();

      SubjectPK pk = null;

      for(int i=0;i<list.size();i++) {
        pk = (SubjectPK)list.get(i);

        // logically delete the record in REG04...
        stmt.execute(
            "update REG04_SUBJECTS set ENABLED='N' where "+
            "COMPANY_CODE_SYS01='"+pk.getCompanyCodeSys01REG04()+"' and "+
            "PROGRESSIVE='"+pk.getProgressiveREG04()+"'"
        );

        // logically delete the record in PUR01...
        stmt.execute(
            "update PUR01_SUPPLIERS set ENABLED='N' where "+
            "COMPANY_CODE_SYS01='"+pk.getCompanyCodeSys01REG04()+"' and "+
            "PROGRESSIVE_REG04='"+pk.getProgressiveREG04()+"'"
        );
      }
      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
    	Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing suppliers",ex);
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

