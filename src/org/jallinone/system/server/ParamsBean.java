package org.jallinone.system.server;

import org.openswing.swing.logger.server.*;
import org.openswing.swing.server.*;
import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;

import java.sql.*;

import org.openswing.swing.internationalization.java.*;
import org.jallinone.commons.java.*;
import org.jallinone.system.java.*;
import org.jallinone.sales.customers.server.*;
import org.jallinone.sales.customers.java.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.sales.customers.java.*;
import org.jallinone.sales.pricelist.server.*;
import org.jallinone.sales.pricelist.java.*;
import org.jallinone.warehouse.java.WarehouseVO;
import org.jallinone.warehouse.server.WarehousesBean;

import java.math.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage company/user/app parameters.</p>
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
public class ParamsBean  implements Params {


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



  private CustomersBean custBean;

  public void setCustBean(CustomersBean custBean) {
    this.custBean = custBean;
  }


  private WarehousesBean wareBean;

  public void setWareBean(WarehousesBean wareBean) {
    this.wareBean = wareBean;
  }



  public ParamsBean() {
  }




  /**
   * Business logic to execute.
   */
  public VOResponse loadUserParams(String companyCode,String serverLanguageId,String username,ArrayList custCompaniesList,ArrayList warCompaniesList) throws Throwable {
    PreparedStatement pstmt = null;

    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      custBean.setConn(conn); // use same transaction...
      wareBean.setConn(conn); // use same transaction...

      UserParametersVO vo = new UserParametersVO();
      vo.setCompanyCodeSys01SYS19(companyCode);
      Response res = null;

      // retrieve customer...
      pstmt = conn.prepareStatement("select VALUE from SYS19_USER_PARAMS where COMPANY_CODE_SYS01=? and USERNAME_SYS03=? and PARAM_CODE=?");
      pstmt.setString(1,companyCode);
      pstmt.setString(2,username);
      pstmt.setString(3,ApplicationConsts.CUSTOMER_CODE);
      ResultSet rset = pstmt.executeQuery();
      while(rset.next()) {
        vo.setCustomerCodeSAL07(rset.getString(1));
      }
      rset.close();
      if (vo.getCustomerCodeSAL07()!=null) {
        HashMap map = new HashMap();
        map.put(ApplicationConsts.COMPANY_CODE_SYS01,companyCode);
        LookupValidationParams pars = new LookupValidationParams(vo.getCustomerCodeSAL07(),map);
        res = custBean.validateCustomerCode(
            pars,
            serverLanguageId,
            username,
            custCompaniesList
        );
        if (res.isError())
          throw new Exception(res.getErrorMessage());
        java.util.List list = ((VOListResponse)res).getRows();
        if (list.size()>0) {
          GridCustomerVO custVO = (GridCustomerVO)list.get(0);
          vo.setName_1REG04(custVO.getName_1REG04());
          vo.setName_2REG04(custVO.getName_2REG04());
        }
      }

      // retrieve warehouse...
      pstmt = conn.prepareStatement("select VALUE from SYS19_USER_PARAMS where COMPANY_CODE_SYS01=? and USERNAME_SYS03=? and PARAM_CODE=?");
      pstmt.setString(1,companyCode);
      pstmt.setString(2,username);
      pstmt.setString(3,ApplicationConsts.WAREHOUSE_CODE);
      rset = pstmt.executeQuery();
      while(rset.next()) {
        vo.setWarehouseCodeWAR01(rset.getString(1));
      }
      rset.close();
      if (vo.getWarehouseCodeWAR01()!=null) {
        HashMap map = new HashMap();
        map.put(ApplicationConsts.COMPANY_CODE_SYS01,companyCode);
        LookupValidationParams pars = new LookupValidationParams(vo.getWarehouseCodeWAR01(),map);
        res = wareBean.validateWarehouseCode(
            pars,
            serverLanguageId,
            username,
            warCompaniesList
        );
        if (res.isError())
        	throw new Exception(res.getErrorMessage());
        java.util.List list = ((VOListResponse)res).getRows();
        if (list.size()>0) {
          WarehouseVO wareVO = (WarehouseVO)list.get(0);
          vo.setWarehouseDescriptionSYS10(wareVO.getDescriptionWAR01());
        }
      }

      // retrieve receipts management program path...
      pstmt.close();
      pstmt = conn.prepareStatement("select VALUE from SYS19_USER_PARAMS where COMPANY_CODE_SYS01=? and USERNAME_SYS03=? and PARAM_CODE=?");
      pstmt.setString(1,companyCode);
      pstmt.setString(2,username);
      pstmt.setString(3,ApplicationConsts.RECEIPT_PATH);
      rset = pstmt.executeQuery();
      while(rset.next()) {
        vo.setReceiptPath(rset.getString(1));
      }
      rset.close();


      // retrieve credit account...
      pstmt.close();
      pstmt = conn.prepareStatement(
          "select VALUE,DESCRIPTION from SYS19_USER_PARAMS,ACC02_ACCOUNTS,SYS10_TRANSLATIONS where "+
          "SYS19_USER_PARAMS.COMPANY_CODE_SYS01=? and SYS19_USER_PARAMS.USERNAME_SYS03=? and SYS19_USER_PARAMS.PARAM_CODE=? and "+
          "SYS19_USER_PARAMS.COMPANY_CODE_SYS01=ACC02_ACCOUNTS.COMPANY_CODE_SYS01 and "+
          "SYS19_USER_PARAMS.VALUE=ACC02_ACCOUNTS.ACCOUNT_CODE and "+
          "ACC02_ACCOUNTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=?"
      );
      pstmt.setString(1,companyCode);
      pstmt.setString(2,username);
      pstmt.setString(3,ApplicationConsts.CREDITS_ACCOUNT);
      pstmt.setString(4,serverLanguageId);
      rset = pstmt.executeQuery();
      while(rset.next()) {
        vo.setCreditAccountCodeAcc02SAL07(rset.getString(1));
        vo.setCreditAccountDescrSAL07(rset.getString(2));
      }
      rset.close();

      // retrieve items account...
      pstmt.close();
      pstmt = conn.prepareStatement(
          "select VALUE,DESCRIPTION from SYS19_USER_PARAMS,ACC02_ACCOUNTS,SYS10_TRANSLATIONS where "+
          "SYS19_USER_PARAMS.COMPANY_CODE_SYS01=? and SYS19_USER_PARAMS.USERNAME_SYS03=? and SYS19_USER_PARAMS.PARAM_CODE=? and "+
          "SYS19_USER_PARAMS.COMPANY_CODE_SYS01=ACC02_ACCOUNTS.COMPANY_CODE_SYS01 and "+
          "SYS19_USER_PARAMS.VALUE=ACC02_ACCOUNTS.ACCOUNT_CODE and "+
          "ACC02_ACCOUNTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=?"
      );
      pstmt.setString(1,companyCode);
      pstmt.setString(2,username);
      pstmt.setString(3,ApplicationConsts.ITEMS_ACCOUNT);
      pstmt.setString(4,serverLanguageId);
      rset = pstmt.executeQuery();
      while(rset.next()) {
        vo.setItemsAccountCodeAcc02SAL07(rset.getString(1));
        vo.setItemsAccountDescrSAL07(rset.getString(2));
      }
      rset.close();

      // retrieve activities account...
      pstmt.close();
      pstmt = conn.prepareStatement(
          "select VALUE,DESCRIPTION from SYS19_USER_PARAMS,ACC02_ACCOUNTS,SYS10_TRANSLATIONS where "+
          "SYS19_USER_PARAMS.COMPANY_CODE_SYS01=? and SYS19_USER_PARAMS.USERNAME_SYS03=? and SYS19_USER_PARAMS.PARAM_CODE=? and "+
          "SYS19_USER_PARAMS.COMPANY_CODE_SYS01=ACC02_ACCOUNTS.COMPANY_CODE_SYS01 and "+
          "SYS19_USER_PARAMS.VALUE=ACC02_ACCOUNTS.ACCOUNT_CODE and "+
          "ACC02_ACCOUNTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=?"
      );
      pstmt.setString(1,companyCode);
      pstmt.setString(2,username);
      pstmt.setString(3,ApplicationConsts.ACTIVITIES_ACCOUNT);
      pstmt.setString(4,serverLanguageId);
      rset = pstmt.executeQuery();
      while(rset.next()) {
        vo.setActivitiesAccountCodeAcc02SAL07(rset.getString(1));
        vo.setActivitiesAccountDescrSAL07(rset.getString(2));
      }
      rset.close();

      // retrieve charges account...
      pstmt.close();
      pstmt = conn.prepareStatement(
          "select VALUE,DESCRIPTION from SYS19_USER_PARAMS,ACC02_ACCOUNTS,SYS10_TRANSLATIONS where "+
          "SYS19_USER_PARAMS.COMPANY_CODE_SYS01=? and SYS19_USER_PARAMS.USERNAME_SYS03=? and SYS19_USER_PARAMS.PARAM_CODE=? and "+
          "SYS19_USER_PARAMS.COMPANY_CODE_SYS01=ACC02_ACCOUNTS.COMPANY_CODE_SYS01 and "+
          "SYS19_USER_PARAMS.VALUE=ACC02_ACCOUNTS.ACCOUNT_CODE and "+
          "ACC02_ACCOUNTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=?"
      );
      pstmt.setString(1,companyCode);
      pstmt.setString(2,username);
      pstmt.setString(3,ApplicationConsts.CHARGES_ACCOUNT);
      pstmt.setString(4,serverLanguageId);
      rset = pstmt.executeQuery();
      while(rset.next()) {
        vo.setChargesAccountCodeAcc02SAL07(rset.getString(1));
        vo.setChargesAccountDescrSAL07(rset.getString(2));
      }
      rset.close();

      // retrieve debits account...
      pstmt.close();
      pstmt = conn.prepareStatement(
          "select VALUE,DESCRIPTION from SYS19_USER_PARAMS,ACC02_ACCOUNTS,SYS10_TRANSLATIONS where "+
          "SYS19_USER_PARAMS.COMPANY_CODE_SYS01=? and SYS19_USER_PARAMS.USERNAME_SYS03=? and SYS19_USER_PARAMS.PARAM_CODE=? and "+
          "SYS19_USER_PARAMS.COMPANY_CODE_SYS01=ACC02_ACCOUNTS.COMPANY_CODE_SYS01 and "+
          "SYS19_USER_PARAMS.VALUE=ACC02_ACCOUNTS.ACCOUNT_CODE and "+
          "ACC02_ACCOUNTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=?"
      );
      pstmt.setString(1,companyCode);
      pstmt.setString(2,username);
      pstmt.setString(3,ApplicationConsts.DEBITS_ACCOUNT);
      pstmt.setString(4,serverLanguageId);
      rset = pstmt.executeQuery();
      while(rset.next()) {
        vo.setDebitAccountCodeAcc02PUR01(rset.getString(1));
        vo.setDebitAccountDescrPUR01(rset.getString(2));
      }
      rset.close();

      // retrieve costs account...
      pstmt.close();
      pstmt = conn.prepareStatement(
          "select VALUE,DESCRIPTION from SYS19_USER_PARAMS,ACC02_ACCOUNTS,SYS10_TRANSLATIONS where "+
          "SYS19_USER_PARAMS.COMPANY_CODE_SYS01=? and SYS19_USER_PARAMS.USERNAME_SYS03=? and SYS19_USER_PARAMS.PARAM_CODE=? and "+
          "SYS19_USER_PARAMS.COMPANY_CODE_SYS01=ACC02_ACCOUNTS.COMPANY_CODE_SYS01 and "+
          "SYS19_USER_PARAMS.VALUE=ACC02_ACCOUNTS.ACCOUNT_CODE and "+
          "ACC02_ACCOUNTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=?"
      );
      pstmt.setString(1,companyCode);
      pstmt.setString(2,username);
      pstmt.setString(3,ApplicationConsts.COSTS_ACCOUNT);
      pstmt.setString(4,serverLanguageId);
      rset = pstmt.executeQuery();
      while(rset.next()) {
        vo.setCostsAccountCodeAcc02PUR01(rset.getString(1));
        vo.setCostsAccountDescrPUR01(rset.getString(2));
      }
      rset.close();

      // retrieve case account...
      pstmt.close();
      pstmt = conn.prepareStatement(
          "select VALUE,DESCRIPTION from SYS19_USER_PARAMS,ACC02_ACCOUNTS,SYS10_TRANSLATIONS where "+
          "SYS19_USER_PARAMS.COMPANY_CODE_SYS01=? and SYS19_USER_PARAMS.USERNAME_SYS03=? and SYS19_USER_PARAMS.PARAM_CODE=? and "+
          "SYS19_USER_PARAMS.COMPANY_CODE_SYS01=ACC02_ACCOUNTS.COMPANY_CODE_SYS01 and "+
          "SYS19_USER_PARAMS.VALUE=ACC02_ACCOUNTS.ACCOUNT_CODE and "+
          "ACC02_ACCOUNTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=?"
      );
      pstmt.setString(1,companyCode);
      pstmt.setString(2,username);
      pstmt.setString(3,ApplicationConsts.CASE_ACCOUNT);
      pstmt.setString(4,serverLanguageId);
      rset = pstmt.executeQuery();
      while(rset.next()) {
        vo.setCaseAccountCodeAcc02DOC19(rset.getString(1));
        vo.setCaseAccountDescrDOC19(rset.getString(2));
      }
      rset.close();

      // retrieve bank account...
      pstmt.close();
      pstmt = conn.prepareStatement(
          "select VALUE,DESCRIPTION from SYS19_USER_PARAMS,ACC02_ACCOUNTS,SYS10_TRANSLATIONS where "+
          "SYS19_USER_PARAMS.COMPANY_CODE_SYS01=? and SYS19_USER_PARAMS.USERNAME_SYS03=? and SYS19_USER_PARAMS.PARAM_CODE=? and "+
          "SYS19_USER_PARAMS.COMPANY_CODE_SYS01=ACC02_ACCOUNTS.COMPANY_CODE_SYS01 and "+
          "SYS19_USER_PARAMS.VALUE=ACC02_ACCOUNTS.ACCOUNT_CODE and "+
          "ACC02_ACCOUNTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=?"
      );
      pstmt.setString(1,companyCode);
      pstmt.setString(2,username);
      pstmt.setString(3,ApplicationConsts.BANK_ACCOUNT);
      pstmt.setString(4,serverLanguageId);
      rset = pstmt.executeQuery();
      while(rset.next()) {
        vo.setBankAccountCodeAcc02DOC19(rset.getString(1));
        vo.setBankAccountDescrDOC19(rset.getString(2));
      }
      rset.close();

      // retrieve vat endorse account...
      pstmt.close();
      pstmt = conn.prepareStatement(
          "select VALUE,DESCRIPTION from SYS19_USER_PARAMS,ACC02_ACCOUNTS,SYS10_TRANSLATIONS where "+
          "SYS19_USER_PARAMS.COMPANY_CODE_SYS01=? and SYS19_USER_PARAMS.USERNAME_SYS03=? and SYS19_USER_PARAMS.PARAM_CODE=? and "+
          "SYS19_USER_PARAMS.COMPANY_CODE_SYS01=ACC02_ACCOUNTS.COMPANY_CODE_SYS01 and "+
          "SYS19_USER_PARAMS.VALUE=ACC02_ACCOUNTS.ACCOUNT_CODE and "+
          "ACC02_ACCOUNTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=?"
      );
      pstmt.setString(1,companyCode);
      pstmt.setString(2,username);
      pstmt.setString(3,ApplicationConsts.VAT_ENDORSE_ACCOUNT);
      pstmt.setString(4,serverLanguageId);
      rset = pstmt.executeQuery();
      while(rset.next()) {
        vo.setVatEndorseAccountCodeAcc02DOC19(rset.getString(1));
        vo.setVatEndorseAccountDescrDOC19(rset.getString(2));
      }
      rset.close();


			// retrieve rounding costs account...
			pstmt.close();
			pstmt = conn.prepareStatement(
					"select VALUE,DESCRIPTION from SYS19_USER_PARAMS,ACC02_ACCOUNTS,SYS10_TRANSLATIONS where "+
					"SYS19_USER_PARAMS.COMPANY_CODE_SYS01=? and SYS19_USER_PARAMS.USERNAME_SYS03=? and SYS19_USER_PARAMS.PARAM_CODE=? and "+
					"SYS19_USER_PARAMS.COMPANY_CODE_SYS01=ACC02_ACCOUNTS.COMPANY_CODE_SYS01 and "+
					"SYS19_USER_PARAMS.VALUE=ACC02_ACCOUNTS.ACCOUNT_CODE and "+
					"ACC02_ACCOUNTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
					"SYS10_TRANSLATIONS.LANGUAGE_CODE=?"
			);
			pstmt.setString(1,companyCode);
			pstmt.setString(2,username);
			pstmt.setString(3,ApplicationConsts.ROUNDING_COSTS_CODE);
			pstmt.setString(4,serverLanguageId);
			rset = pstmt.executeQuery();
			while(rset.next()) {
				vo.setRoundingCostsAccountCodeAcc02DOC19(rset.getString(1));
				vo.setRoundingCostsDescrDOC19(rset.getString(2));
			}
			rset.close();

			// retrieve rounding proceeds account...
			pstmt.close();
			pstmt = conn.prepareStatement(
					"select VALUE,DESCRIPTION from SYS19_USER_PARAMS,ACC02_ACCOUNTS,SYS10_TRANSLATIONS where "+
					"SYS19_USER_PARAMS.COMPANY_CODE_SYS01=? and SYS19_USER_PARAMS.USERNAME_SYS03=? and SYS19_USER_PARAMS.PARAM_CODE=? and "+
					"SYS19_USER_PARAMS.COMPANY_CODE_SYS01=ACC02_ACCOUNTS.COMPANY_CODE_SYS01 and "+
					"SYS19_USER_PARAMS.VALUE=ACC02_ACCOUNTS.ACCOUNT_CODE and "+
					"ACC02_ACCOUNTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
					"SYS10_TRANSLATIONS.LANGUAGE_CODE=?"
			);
			pstmt.setString(1,companyCode);
			pstmt.setString(2,username);
			pstmt.setString(3,ApplicationConsts.ROUNDING_PROCEEDS_CODE);
			pstmt.setString(4,serverLanguageId);
			rset = pstmt.executeQuery();
			while(rset.next()) {
				vo.setRoundingProceedsAccountCodeAcc02DOC19(rset.getString(1));
				vo.setRoundingProceedsDescrDOC19(rset.getString(2));
			}
			rset.close();


      return new VOResponse(vo);
    } catch (Exception ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while loading user parameters",ex);
      throw new Exception(ex.getMessage());
    } finally {
      try {
        pstmt.close();
      }
      catch (Exception ex) {
      }

      try {
        custBean.setConn(null);
        wareBean.setConn(null);
      } catch (Exception ex) {}
    }

  }


  /**
   * Business logic to execute.
   */
  public VOResponse saveUserParam(HashMap params,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      String companyCode = (String)params.get(ApplicationConsts.COMPANY_CODE_SYS01);
      String paramCode = (String)params.get(ApplicationConsts.PARAM_CODE);
      String value = (String)params.get(ApplicationConsts.PARAM_VALUE);

      pstmt = conn.prepareStatement("update SYS19_USER_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and USERNAME_SYS03=? and PARAM_CODE=?");
      pstmt.setString(1,value);
      pstmt.setString(2,companyCode);
      pstmt.setString(3,username);
      pstmt.setString(4,paramCode);
      int rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS19_USER_PARAMS(COMPANY_CODE_SYS01,USERNAME_SYS03,PARAM_CODE,VALUE) values(?,?,?,?)");
        pstmt.setString(1,companyCode);
        pstmt.setString(2,username);
        pstmt.setString(3,paramCode);
        pstmt.setString(4,value);
        pstmt.executeUpdate();
      }

      return new VOResponse(Boolean.TRUE);
    } catch (Exception ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while storing a user parameter",ex);
      try {
    	  if (this.conn==null && conn!=null)
    		  // rollback only local connection
    		  conn.rollback();
      }
      catch (Exception ex3) {
      }
      throw new Exception(ex.getMessage());
    } finally {
      try {
        pstmt.close();
      }
      catch (Exception ex) {
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



  /**
   * Business logic to execute.
   */
  public VOResponse updateApplicationParams(HashMap applicationPars,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      // update receipts management program path...
      pstmt = conn.prepareStatement("update SYS11_APPLICATION_PARS set VALUE=? where PARAM_CODE=?");
      pstmt.setString(1,getImagePath(applicationPars));
      pstmt.setString(2,ApplicationConsts.IMAGE_PATH);
      int rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS11_APPLICATION_PARS(PARAM_CODE,VALUE) values(?,?)");
        pstmt.setString(1,ApplicationConsts.IMAGE_PATH);
        pstmt.setString(2,getImagePath(applicationPars));
        pstmt.executeUpdate();
      }

      // update documents repository path...
      pstmt = conn.prepareStatement("update SYS11_APPLICATION_PARS set VALUE=? where PARAM_CODE=?");
      pstmt.setString(1,getDocumentPath(applicationPars));
      pstmt.setString(2,ApplicationConsts.DOC_PATH);
      rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS11_APPLICATION_PARS(PARAM_CODE,VALUE) values(?,?)");
        pstmt.setString(1,ApplicationConsts.DOC_PATH);
        pstmt.setString(2,getDocumentPath(applicationPars));
        pstmt.executeUpdate();
      }

      // update increment value for progressives...
      pstmt = conn.prepareStatement("update SYS11_APPLICATION_PARS set VALUE=? where PARAM_CODE=?");
      pstmt.setString(1,getIncrementValue(applicationPars).toString());
      pstmt.setString(2,ApplicationConsts.INCREMENT_VALUE);
      rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS11_APPLICATION_PARS(PARAM_CODE,VALUE) values(?,?)");
        pstmt.setString(1,ApplicationConsts.INCREMENT_VALUE);
        pstmt.setString(2,getIncrementValue(applicationPars).toString());
        pstmt.executeUpdate();
      }

			// update flag for sale doc nums value for progressives...
			pstmt = conn.prepareStatement("update SYS11_APPLICATION_PARS set VALUE=? where PARAM_CODE=?");
			pstmt.setString(1,getManualDocNumInSaleDocs(applicationPars));
			pstmt.setString(2,ApplicationConsts.DOC_NUM_IN_SALE_DOCS);
			rows = pstmt.executeUpdate();
			pstmt.close();
			if (rows==0) {
				// record not yet exists: it will be inserted...
				pstmt = conn.prepareStatement("insert into SYS11_APPLICATION_PARS(PARAM_CODE,VALUE) values(?,?)");
				pstmt.setString(1,ApplicationConsts.DOC_NUM_IN_SALE_DOCS);
				pstmt.setString(2,getManualDocNumInSaleDocs(applicationPars));
				pstmt.executeUpdate();
			}




			// update "inv.neg.corr. for good items" value for progressives...
			pstmt = conn.prepareStatement("update SYS11_APPLICATION_PARS set VALUE=? where PARAM_CODE=?");
			pstmt.setString(1,getInvNegCorrForGoodItemsValue(applicationPars).toString());
			pstmt.setString(2,ApplicationConsts.NEG_INVCORR_GOOD_ITM);
			rows = pstmt.executeUpdate();
			pstmt.close();
			if (rows==0) {
				// record not yet exists: it will be inserted...
				pstmt = conn.prepareStatement("insert into SYS11_APPLICATION_PARS(PARAM_CODE,VALUE) values(?,?)");
				pstmt.setString(1,ApplicationConsts.NEG_INVCORR_GOOD_ITM);
				pstmt.setString(2,getInvNegCorrForGoodItemsValue(applicationPars).toString());
				pstmt.executeUpdate();
			}

			// update "inv.pos.corr. for good items"  value for progressives...
			pstmt = conn.prepareStatement("update SYS11_APPLICATION_PARS set VALUE=? where PARAM_CODE=?");
			pstmt.setString(1,getInvPosCorrForGoodItemsValue(applicationPars).toString());
			pstmt.setString(2,ApplicationConsts.POS_INVCORR_GOOD_ITM);
			rows = pstmt.executeUpdate();
			pstmt.close();
			if (rows==0) {
				// record not yet exists: it will be inserted...
				pstmt = conn.prepareStatement("insert into SYS11_APPLICATION_PARS(PARAM_CODE,VALUE) values(?,?)");
				pstmt.setString(1,ApplicationConsts.POS_INVCORR_GOOD_ITM);
				pstmt.setString(2,getInvPosCorrForGoodItemsValue(applicationPars).toString());
				pstmt.executeUpdate();
			}

			// update "inv.pos.corr. for damaged items"  value for progressives...
			pstmt = conn.prepareStatement("update SYS11_APPLICATION_PARS set VALUE=? where PARAM_CODE=?");
			pstmt.setString(1,getInvPosCorrForDamagedItemsValue(applicationPars).toString());
			pstmt.setString(2,ApplicationConsts.POS_INVCORR_DAMG_ITM);
			rows = pstmt.executeUpdate();
			pstmt.close();
			if (rows==0) {
				// record not yet exists: it will be inserted...
				pstmt = conn.prepareStatement("insert into SYS11_APPLICATION_PARS(PARAM_CODE,VALUE) values(?,?)");
				pstmt.setString(1,ApplicationConsts.POS_INVCORR_DAMG_ITM);
				pstmt.setString(2,getInvPosCorrForDamagedItemsValue(applicationPars).toString());
				pstmt.executeUpdate();
			}

			// update "inv.neg.corr. for damaged items"  value for progressives...
			pstmt = conn.prepareStatement("update SYS11_APPLICATION_PARS set VALUE=? where PARAM_CODE=?");
			pstmt.setString(1,getInvNegCorrForDamagedItemsValue(applicationPars));
			pstmt.setString(2,ApplicationConsts.NEG_INVCORR_DAMG_ITM);
			rows = pstmt.executeUpdate();
			pstmt.close();
			if (rows==0) {
				// record not yet exists: it will be inserted...
				pstmt = conn.prepareStatement("insert into SYS11_APPLICATION_PARS(PARAM_CODE,VALUE) values(?,?)");
				pstmt.setString(1,ApplicationConsts.NEG_INVCORR_DAMG_ITM);
				pstmt.setString(2,getInvNegCorrForDamagedItemsValue(applicationPars));
				pstmt.executeUpdate();
			}



      return new VOResponse(Boolean.TRUE);
    } catch (Exception ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while storing application parameters",ex);
      try {
    	  if (this.conn==null && conn!=null)
    		  // rollback only local connection
    		  conn.rollback();
      }
      catch (Exception ex3) {
      }
      throw new Exception(ex.getMessage());
    } finally {
      try {
        pstmt.close();
      }
      catch (Exception ex) {
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



  /**
   * Business logic to execute.
   */
  public VOResponse updateCompanyParams(CompanyParametersVO vo,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;


      // update credit account...
      pstmt = conn.prepareStatement("update SYS21_COMPANY_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and PARAM_CODE=?");
      pstmt.setString(1,vo.getCreditAccountCodeAcc02SAL07());
      pstmt.setString(2,vo.getCompanyCodeSys01SYS21());
      pstmt.setString(3,ApplicationConsts.CREDITS_ACCOUNT);
      int rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS21_COMPANY_PARAMS(COMPANY_CODE_SYS01,PARAM_CODE,VALUE) values(?,?,?)");
        pstmt.setString(1,vo.getCompanyCodeSys01SYS21());
        pstmt.setString(2,ApplicationConsts.CREDITS_ACCOUNT);
        pstmt.setString(3,vo.getCreditAccountCodeAcc02SAL07());
        pstmt.executeUpdate();
      }

      // update item account...
      pstmt = conn.prepareStatement("update SYS21_COMPANY_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and PARAM_CODE=?");
      pstmt.setString(1,vo.getItemsAccountCodeAcc02SAL07());
      pstmt.setString(2,vo.getCompanyCodeSys01SYS21());
      pstmt.setString(3,ApplicationConsts.ITEMS_ACCOUNT);
      rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS21_COMPANY_PARAMS(COMPANY_CODE_SYS01,PARAM_CODE,VALUE) values(?,?,?)");
        pstmt.setString(1,vo.getCompanyCodeSys01SYS21());
        pstmt.setString(2,ApplicationConsts.ITEMS_ACCOUNT);
        pstmt.setString(3,vo.getItemsAccountCodeAcc02SAL07());
        pstmt.executeUpdate();
      }

      // update charges account...
      pstmt = conn.prepareStatement("update SYS21_COMPANY_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and PARAM_CODE=?");
      pstmt.setString(1,vo.getChargesAccountCodeAcc02SAL07());
      pstmt.setString(2,vo.getCompanyCodeSys01SYS21());
      pstmt.setString(3,ApplicationConsts.CHARGES_ACCOUNT);
      rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS21_COMPANY_PARAMS(COMPANY_CODE_SYS01,PARAM_CODE,VALUE) values(?,?,?)");
        pstmt.setString(1,vo.getCompanyCodeSys01SYS21());
        pstmt.setString(2,ApplicationConsts.CHARGES_ACCOUNT);
        pstmt.setString(3,vo.getChargesAccountCodeAcc02SAL07());
        pstmt.executeUpdate();
      }

      // update activities account...
      pstmt = conn.prepareStatement("update SYS21_COMPANY_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and PARAM_CODE=?");
      pstmt.setString(1,vo.getActivitiesAccountCodeAcc02SAL07());
      pstmt.setString(2,vo.getCompanyCodeSys01SYS21());
      pstmt.setString(3,ApplicationConsts.ACTIVITIES_ACCOUNT);
      rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS21_COMPANY_PARAMS(COMPANY_CODE_SYS01,PARAM_CODE,VALUE) values(?,?,?)");
        pstmt.setString(1,vo.getCompanyCodeSys01SYS21());
        pstmt.setString(2,ApplicationConsts.ACTIVITIES_ACCOUNT);
        pstmt.setString(3,vo.getActivitiesAccountCodeAcc02SAL07());
        pstmt.executeUpdate();
      }

      // update debit account...
      pstmt = conn.prepareStatement("update SYS21_COMPANY_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and PARAM_CODE=?");
      pstmt.setString(1,vo.getDebitAccountCodeAcc02PUR01());
      pstmt.setString(2,vo.getCompanyCodeSys01SYS21());
      pstmt.setString(3,ApplicationConsts.DEBITS_ACCOUNT);
      rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS21_COMPANY_PARAMS(COMPANY_CODE_SYS01,PARAM_CODE,VALUE) values(?,?,?)");
        pstmt.setString(1,vo.getCompanyCodeSys01SYS21());
        pstmt.setString(2,ApplicationConsts.DEBITS_ACCOUNT);
        pstmt.setString(3,vo.getDebitAccountCodeAcc02PUR01());
        pstmt.executeUpdate();
      }

      // update costs account...
      pstmt = conn.prepareStatement("update SYS21_COMPANY_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and PARAM_CODE=?");
      pstmt.setString(1,vo.getCostsAccountCodeAcc02PUR01());
      pstmt.setString(2,vo.getCompanyCodeSys01SYS21());
      pstmt.setString(3,ApplicationConsts.COSTS_ACCOUNT);
      rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS21_COMPANY_PARAMS(COMPANY_CODE_SYS01,PARAM_CODE,VALUE) values(?,?,?)");
        pstmt.setString(1,vo.getCompanyCodeSys01SYS21());
        pstmt.setString(2,ApplicationConsts.COSTS_ACCOUNT);
        pstmt.setString(3,vo.getCostsAccountCodeAcc02PUR01());
        pstmt.executeUpdate();
      }

      // update case account...
      pstmt = conn.prepareStatement("update SYS21_COMPANY_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and PARAM_CODE=?");
      pstmt.setString(1,vo.getCaseAccountCodeAcc02DOC21());
      pstmt.setString(2,vo.getCompanyCodeSys01SYS21());
      pstmt.setString(3,ApplicationConsts.CASE_ACCOUNT);
      rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS21_COMPANY_PARAMS(COMPANY_CODE_SYS01,PARAM_CODE,VALUE) values(?,?,?)");
        pstmt.setString(1,vo.getCompanyCodeSys01SYS21());
        pstmt.setString(2,ApplicationConsts.CASE_ACCOUNT);
        pstmt.setString(3,vo.getCaseAccountCodeAcc02DOC21());
        pstmt.executeUpdate();
      }

      // update bank account...
      pstmt = conn.prepareStatement("update SYS21_COMPANY_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and PARAM_CODE=?");
      pstmt.setString(1,vo.getBankAccountCodeAcc02DOC21());
      pstmt.setString(2,vo.getCompanyCodeSys01SYS21());
      pstmt.setString(3,ApplicationConsts.BANK_ACCOUNT);
      rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS21_COMPANY_PARAMS(COMPANY_CODE_SYS01,PARAM_CODE,VALUE) values(?,?,?)");
        pstmt.setString(1,vo.getCompanyCodeSys01SYS21());
        pstmt.setString(2,ApplicationConsts.BANK_ACCOUNT);
        pstmt.setString(3,vo.getBankAccountCodeAcc02DOC21());
        pstmt.executeUpdate();
      }

      // update vat endorse account...
      pstmt = conn.prepareStatement("update SYS21_COMPANY_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and PARAM_CODE=?");
      pstmt.setString(1,vo.getVatEndorseAccountCodeAcc02DOC21());
      pstmt.setString(2,vo.getCompanyCodeSys01SYS21());
      pstmt.setString(3,ApplicationConsts.VAT_ENDORSE_ACCOUNT);
      rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS21_COMPANY_PARAMS(COMPANY_CODE_SYS01,PARAM_CODE,VALUE) values(?,?,?)");
        pstmt.setString(1,vo.getCompanyCodeSys01SYS21());
        pstmt.setString(2,ApplicationConsts.VAT_ENDORSE_ACCOUNT);
        pstmt.setString(3,vo.getVatEndorseAccountCodeAcc02DOC21());
        pstmt.executeUpdate();
      }

      // update loss/profit econ. endorse account...
      pstmt = conn.prepareStatement("update SYS21_COMPANY_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and PARAM_CODE=?");
      pstmt.setString(1,vo.getLossProfitEAccountCodeAcc02DOC21());
      pstmt.setString(2,vo.getCompanyCodeSys01SYS21());
      pstmt.setString(3,ApplicationConsts.LOSSPROFIT_E_ACCOUNT);
      rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS21_COMPANY_PARAMS(COMPANY_CODE_SYS01,PARAM_CODE,VALUE) values(?,?,?)");
        pstmt.setString(1,vo.getCompanyCodeSys01SYS21());
        pstmt.setString(2,ApplicationConsts.LOSSPROFIT_E_ACCOUNT);
        pstmt.setString(3,vo.getLossProfitEAccountCodeAcc02DOC21());
        pstmt.executeUpdate();
      }

      // update loss/profit patrim. endorse account...
      pstmt = conn.prepareStatement("update SYS21_COMPANY_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and PARAM_CODE=?");
      pstmt.setString(1,vo.getLossProfitPAccountCodeAcc02DOC21());
      pstmt.setString(2,vo.getCompanyCodeSys01SYS21());
      pstmt.setString(3,ApplicationConsts.LOSSPROFIT_P_ACCOUNT);
      rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS21_COMPANY_PARAMS(COMPANY_CODE_SYS01,PARAM_CODE,VALUE) values(?,?,?)");
        pstmt.setString(1,vo.getCompanyCodeSys01SYS21());
        pstmt.setString(2,ApplicationConsts.LOSSPROFIT_P_ACCOUNT);
        pstmt.setString(3,vo.getLossProfitPAccountCodeAcc02DOC21());
        pstmt.executeUpdate();
      }

      // update closing account...
      pstmt = conn.prepareStatement("update SYS21_COMPANY_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and PARAM_CODE=?");
      pstmt.setString(1,vo.getClosingAccountCodeAcc02DOC21());
      pstmt.setString(2,vo.getCompanyCodeSys01SYS21());
      pstmt.setString(3,ApplicationConsts.CLOSING_ACCOUNT);
      rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS21_COMPANY_PARAMS(COMPANY_CODE_SYS01,PARAM_CODE,VALUE) values(?,?,?)");
        pstmt.setString(1,vo.getCompanyCodeSys01SYS21());
        pstmt.setString(2,ApplicationConsts.CLOSING_ACCOUNT);
        pstmt.setString(3,vo.getClosingAccountCodeAcc02DOC21());
        pstmt.executeUpdate();
      }

      // update opening account...
      pstmt = conn.prepareStatement("update SYS21_COMPANY_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and PARAM_CODE=?");
      pstmt.setString(1,vo.getOpeningAccountCodeAcc02DOC21());
      pstmt.setString(2,vo.getCompanyCodeSys01SYS21());
      pstmt.setString(3,ApplicationConsts.OPENING_ACCOUNT);
      rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS21_COMPANY_PARAMS(COMPANY_CODE_SYS01,PARAM_CODE,VALUE) values(?,?,?)");
        pstmt.setString(1,vo.getCompanyCodeSys01SYS21());
        pstmt.setString(2,ApplicationConsts.OPENING_ACCOUNT);
        pstmt.setString(3,vo.getOpeningAccountCodeAcc02DOC21());
        pstmt.executeUpdate();
      }


      // update morning start hour...
      pstmt = conn.prepareStatement("update SYS21_COMPANY_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and PARAM_CODE=?");
      pstmt.setTimestamp(1,vo.getMorningStartHourSCH02());
      pstmt.setString(2,vo.getCompanyCodeSys01SYS21());
      pstmt.setString(3,ApplicationConsts.MORNING_START_HOUR);
      rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS21_COMPANY_PARAMS(COMPANY_CODE_SYS01,PARAM_CODE,VALUE) values(?,?,?)");
        pstmt.setString(1,vo.getCompanyCodeSys01SYS21());
        pstmt.setString(2,ApplicationConsts.MORNING_START_HOUR);
        pstmt.setTimestamp(3,vo.getMorningStartHourSCH02());
        pstmt.executeUpdate();
      }

      // update morning end hour...
      pstmt = conn.prepareStatement("update SYS21_COMPANY_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and PARAM_CODE=?");
      pstmt.setTimestamp(1,vo.getMorningEndHourSCH02());
      pstmt.setString(2,vo.getCompanyCodeSys01SYS21());
      pstmt.setString(3,ApplicationConsts.MORNING_END_HOUR);
      rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS21_COMPANY_PARAMS(COMPANY_CODE_SYS01,PARAM_CODE,VALUE) values(?,?,?)");
        pstmt.setString(1,vo.getCompanyCodeSys01SYS21());
        pstmt.setString(2,ApplicationConsts.MORNING_END_HOUR);
        pstmt.setTimestamp(3,vo.getMorningEndHourSCH02());
        pstmt.executeUpdate();
      }

      // update afternoon start hour...
      pstmt = conn.prepareStatement("update SYS21_COMPANY_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and PARAM_CODE=?");
      pstmt.setTimestamp(1,vo.getAfternoonStartHourSCH02());
      pstmt.setString(2,vo.getCompanyCodeSys01SYS21());
      pstmt.setString(3,ApplicationConsts.AFTERNOON_START_HOUR);
      rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS21_COMPANY_PARAMS(COMPANY_CODE_SYS01,PARAM_CODE,VALUE) values(?,?,?)");
        pstmt.setString(1,vo.getCompanyCodeSys01SYS21());
        pstmt.setString(2,ApplicationConsts.AFTERNOON_START_HOUR);
        pstmt.setTimestamp(3,vo.getAfternoonStartHourSCH02());
        pstmt.executeUpdate();
      }

      // update afternoon end hour...
      pstmt = conn.prepareStatement("update SYS21_COMPANY_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and PARAM_CODE=?");
      pstmt.setTimestamp(1,vo.getAfternoonEndHourSCH02());
      pstmt.setString(2,vo.getCompanyCodeSys01SYS21());
      pstmt.setString(3,ApplicationConsts.AFTERNOON_END_HOUR);
      rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS21_COMPANY_PARAMS(COMPANY_CODE_SYS01,PARAM_CODE,VALUE) values(?,?,?)");
        pstmt.setString(1,vo.getCompanyCodeSys01SYS21());
        pstmt.setString(2,ApplicationConsts.AFTERNOON_END_HOUR);
        pstmt.setTimestamp(3,vo.getAfternoonEndHourSCH02());
        pstmt.executeUpdate();
      }

      // update sale sectional...
      pstmt = conn.prepareStatement("update SYS21_COMPANY_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and PARAM_CODE=?");
      pstmt.setString(1,vo.getSaleSectionalDOC01());
      pstmt.setString(2,vo.getCompanyCodeSys01SYS21());
      pstmt.setString(3,ApplicationConsts.SALE_SECTIONAL);
      rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS21_COMPANY_PARAMS(COMPANY_CODE_SYS01,PARAM_CODE,VALUE) values(?,?,?)");
        pstmt.setString(1,vo.getCompanyCodeSys01SYS21());
        pstmt.setString(2,ApplicationConsts.SALE_SECTIONAL);
        pstmt.setString(3,vo.getSaleSectionalDOC01());
        pstmt.executeUpdate();
      }

      // update initial value for progressives...
      pstmt = conn.prepareStatement("update SYS21_COMPANY_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and PARAM_CODE=?");
      pstmt.setString(1,vo.getInitialValueSYS21().toString());
      pstmt.setString(2,vo.getCompanyCodeSys01SYS21());
      pstmt.setString(3,ApplicationConsts.INITIAL_VALUE);
      rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS21_COMPANY_PARAMS(COMPANY_CODE_SYS01,PARAM_CODE,VALUE) values(?,?,?)");
        pstmt.setString(1,vo.getCompanyCodeSys01SYS21());
        pstmt.setString(2,ApplicationConsts.INITIAL_VALUE);
        pstmt.setString(3,vo.getInitialValueSYS21().toString());
        pstmt.executeUpdate();
      }


			// update rounding costs account...
			pstmt = conn.prepareStatement("update SYS21_COMPANY_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and PARAM_CODE=?");
			pstmt.setString(1,vo.getRoundingCostsAccountCodeAcc02DOC19());
			pstmt.setString(2,vo.getCompanyCodeSys01SYS21());
			pstmt.setString(3,ApplicationConsts.ROUNDING_COSTS_CODE);
			rows = pstmt.executeUpdate();
			pstmt.close();
			if (rows==0) {
				// record not yet exists: it will be inserted...
				pstmt = conn.prepareStatement("insert into SYS21_COMPANY_PARAMS(COMPANY_CODE_SYS01,PARAM_CODE,VALUE) values(?,?,?)");
				pstmt.setString(1,vo.getCompanyCodeSys01SYS21());
				pstmt.setString(2,ApplicationConsts.ROUNDING_COSTS_CODE);
				pstmt.setString(3,vo.getRoundingCostsAccountCodeAcc02DOC19());
				pstmt.executeUpdate();
			}

			// update rounding proceeds account...
			pstmt = conn.prepareStatement("update SYS21_COMPANY_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and PARAM_CODE=?");
			pstmt.setString(1,vo.getRoundingProceedsAccountCodeAcc02DOC19());
			pstmt.setString(2,vo.getCompanyCodeSys01SYS21());
			pstmt.setString(3,ApplicationConsts.ROUNDING_PROCEEDS_CODE);
			rows = pstmt.executeUpdate();
			pstmt.close();
			if (rows==0) {
				// record not yet exists: it will be inserted...
				pstmt = conn.prepareStatement("insert into SYS21_COMPANY_PARAMS(COMPANY_CODE_SYS01,PARAM_CODE,VALUE) values(?,?,?)");
				pstmt.setString(1,vo.getCompanyCodeSys01SYS21());
				pstmt.setString(2,ApplicationConsts.ROUNDING_PROCEEDS_CODE);
				pstmt.setString(3,vo.getRoundingProceedsAccountCodeAcc02DOC19());
				pstmt.executeUpdate();
			}

      return new VOResponse(vo);
    } catch (Exception ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while storing company parameters",ex);
      try {
    	  if (this.conn==null && conn!=null)
    		  // rollback only local connection
    		  conn.rollback();
      }
      catch (Exception ex3) {
      }
      throw new Exception(ex.getMessage());
    } finally {
      try {
        pstmt.close();
      }
      catch (Exception ex) {
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




  /**
   * Business logic to execute.
   */
  public VOResponse updateUserParams(UserParametersVO vo,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;


      // update customer code...
      pstmt = conn.prepareStatement("update SYS19_USER_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and USERNAME_SYS03=? and PARAM_CODE=?");
      pstmt.setString(1,vo.getCustomerCodeSAL07());
      pstmt.setString(2,vo.getCompanyCodeSys01SYS19());
      pstmt.setString(3,username);
      pstmt.setString(4,ApplicationConsts.CUSTOMER_CODE);
      int rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS19_USER_PARAMS(COMPANY_CODE_SYS01,USERNAME_SYS03,PARAM_CODE,VALUE) values(?,?,?,?)");
        pstmt.setString(1,vo.getCompanyCodeSys01SYS19());
        pstmt.setString(2,username);
        pstmt.setString(3,ApplicationConsts.CUSTOMER_CODE);
        pstmt.setString(4,vo.getCustomerCodeSAL07());
        pstmt.executeUpdate();
      }

      // update warehouse code...
      pstmt = conn.prepareStatement("update SYS19_USER_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and USERNAME_SYS03=? and PARAM_CODE=?");
      pstmt.setString(1,vo.getWarehouseCodeWAR01());
      pstmt.setString(2,vo.getCompanyCodeSys01SYS19());
      pstmt.setString(3,username);
      pstmt.setString(4,ApplicationConsts.WAREHOUSE_CODE);
      rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS19_USER_PARAMS(COMPANY_CODE_SYS01,USERNAME_SYS03,PARAM_CODE,VALUE) values(?,?,?,?)");
        pstmt.setString(1,vo.getCompanyCodeSys01SYS19());
        pstmt.setString(2,username);
        pstmt.setString(3,ApplicationConsts.WAREHOUSE_CODE);
        pstmt.setString(4,vo.getWarehouseCodeWAR01());
        pstmt.executeUpdate();
      }

      // update receipts management program path...
      pstmt = conn.prepareStatement("update SYS19_USER_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and USERNAME_SYS03=? and PARAM_CODE=?");
      pstmt.setString(1,vo.getReceiptPath());
      pstmt.setString(2,vo.getCompanyCodeSys01SYS19());
      pstmt.setString(3,username);
      pstmt.setString(4,ApplicationConsts.RECEIPT_PATH);
      rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS19_USER_PARAMS(COMPANY_CODE_SYS01,USERNAME_SYS03,PARAM_CODE,VALUE) values(?,?,?,?)");
        pstmt.setString(1,vo.getCompanyCodeSys01SYS19());
        pstmt.setString(2,username);
        pstmt.setString(3,ApplicationConsts.RECEIPT_PATH);
        pstmt.setString(4,vo.getReceiptPath());
        pstmt.executeUpdate();
      }


      // update credit account...
      pstmt = conn.prepareStatement("update SYS19_USER_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and USERNAME_SYS03=? and PARAM_CODE=?");
      pstmt.setString(1,vo.getCreditAccountCodeAcc02SAL07());
      pstmt.setString(2,vo.getCompanyCodeSys01SYS19());
      pstmt.setString(3,username);
      pstmt.setString(4,ApplicationConsts.CREDITS_ACCOUNT);
      rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS19_USER_PARAMS(COMPANY_CODE_SYS01,USERNAME_SYS03,PARAM_CODE,VALUE) values(?,?,?,?)");
        pstmt.setString(1,vo.getCompanyCodeSys01SYS19());
        pstmt.setString(2,username);
        pstmt.setString(3,ApplicationConsts.CREDITS_ACCOUNT);
        pstmt.setString(4,vo.getCreditAccountCodeAcc02SAL07());
        pstmt.executeUpdate();
      }

      // update item account...
      pstmt = conn.prepareStatement("update SYS19_USER_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and USERNAME_SYS03=? and PARAM_CODE=?");
      pstmt.setString(1,vo.getItemsAccountCodeAcc02SAL07());
      pstmt.setString(2,vo.getCompanyCodeSys01SYS19());
      pstmt.setString(3,username);
      pstmt.setString(4,ApplicationConsts.ITEMS_ACCOUNT);
      rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS19_USER_PARAMS(COMPANY_CODE_SYS01,USERNAME_SYS03,PARAM_CODE,VALUE) values(?,?,?,?)");
        pstmt.setString(1,vo.getCompanyCodeSys01SYS19());
        pstmt.setString(2,username);
        pstmt.setString(3,ApplicationConsts.ITEMS_ACCOUNT);
        pstmt.setString(4,vo.getItemsAccountCodeAcc02SAL07());
        pstmt.executeUpdate();
      }

      // update charges account...
      pstmt = conn.prepareStatement("update SYS19_USER_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and USERNAME_SYS03=? and PARAM_CODE=?");
      pstmt.setString(1,vo.getChargesAccountCodeAcc02SAL07());
      pstmt.setString(2,vo.getCompanyCodeSys01SYS19());
      pstmt.setString(3,username);
      pstmt.setString(4,ApplicationConsts.CHARGES_ACCOUNT);
      rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS19_USER_PARAMS(COMPANY_CODE_SYS01,USERNAME_SYS03,PARAM_CODE,VALUE) values(?,?,?,?)");
        pstmt.setString(1,vo.getCompanyCodeSys01SYS19());
        pstmt.setString(2,username);
        pstmt.setString(3,ApplicationConsts.CHARGES_ACCOUNT);
        pstmt.setString(4,vo.getChargesAccountCodeAcc02SAL07());
        pstmt.executeUpdate();
      }

      // update activities account...
      pstmt = conn.prepareStatement("update SYS19_USER_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and USERNAME_SYS03=? and PARAM_CODE=?");
      pstmt.setString(1,vo.getActivitiesAccountCodeAcc02SAL07());
      pstmt.setString(2,vo.getCompanyCodeSys01SYS19());
      pstmt.setString(3,username);
      pstmt.setString(4,ApplicationConsts.ACTIVITIES_ACCOUNT);
      rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS19_USER_PARAMS(COMPANY_CODE_SYS01,USERNAME_SYS03,PARAM_CODE,VALUE) values(?,?,?,?)");
        pstmt.setString(1,vo.getCompanyCodeSys01SYS19());
        pstmt.setString(2,username);
        pstmt.setString(3,ApplicationConsts.ACTIVITIES_ACCOUNT);
        pstmt.setString(4,vo.getActivitiesAccountCodeAcc02SAL07());
        pstmt.executeUpdate();
      }

      // update debit account...
      pstmt = conn.prepareStatement("update SYS19_USER_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and USERNAME_SYS03=? and PARAM_CODE=?");
      pstmt.setString(1,vo.getDebitAccountCodeAcc02PUR01());
      pstmt.setString(2,vo.getCompanyCodeSys01SYS19());
      pstmt.setString(3,username);
      pstmt.setString(4,ApplicationConsts.DEBITS_ACCOUNT);
      rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS19_USER_PARAMS(COMPANY_CODE_SYS01,USERNAME_SYS03,PARAM_CODE,VALUE) values(?,?,?,?)");
        pstmt.setString(1,vo.getCompanyCodeSys01SYS19());
        pstmt.setString(2,username);
        pstmt.setString(3,ApplicationConsts.DEBITS_ACCOUNT);
        pstmt.setString(4,vo.getDebitAccountCodeAcc02PUR01());
        pstmt.executeUpdate();
      }

      // update costs account...
      pstmt = conn.prepareStatement("update SYS19_USER_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and USERNAME_SYS03=? and PARAM_CODE=?");
      pstmt.setString(1,vo.getCostsAccountCodeAcc02PUR01());
      pstmt.setString(2,vo.getCompanyCodeSys01SYS19());
      pstmt.setString(3,username);
      pstmt.setString(4,ApplicationConsts.COSTS_ACCOUNT);
      rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS19_USER_PARAMS(COMPANY_CODE_SYS01,USERNAME_SYS03,PARAM_CODE,VALUE) values(?,?,?,?)");
        pstmt.setString(1,vo.getCompanyCodeSys01SYS19());
        pstmt.setString(2,username);
        pstmt.setString(3,ApplicationConsts.COSTS_ACCOUNT);
        pstmt.setString(4,vo.getCostsAccountCodeAcc02PUR01());
        pstmt.executeUpdate();
      }

      // update case account...
      pstmt = conn.prepareStatement("update SYS19_USER_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and USERNAME_SYS03=? and PARAM_CODE=?");
      pstmt.setString(1,vo.getCaseAccountCodeAcc02DOC19());
      pstmt.setString(2,vo.getCompanyCodeSys01SYS19());
      pstmt.setString(3,username);
      pstmt.setString(4,ApplicationConsts.CASE_ACCOUNT);
      rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS19_USER_PARAMS(COMPANY_CODE_SYS01,USERNAME_SYS03,PARAM_CODE,VALUE) values(?,?,?,?)");
        pstmt.setString(1,vo.getCompanyCodeSys01SYS19());
        pstmt.setString(2,username);
        pstmt.setString(3,ApplicationConsts.CASE_ACCOUNT);
        pstmt.setString(4,vo.getCaseAccountCodeAcc02DOC19());
        pstmt.executeUpdate();
      }

      // update bank account...
      pstmt = conn.prepareStatement("update SYS19_USER_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and USERNAME_SYS03=? and PARAM_CODE=?");
      pstmt.setString(1,vo.getBankAccountCodeAcc02DOC19());
      pstmt.setString(2,vo.getCompanyCodeSys01SYS19());
      pstmt.setString(3,username);
      pstmt.setString(4,ApplicationConsts.BANK_ACCOUNT);
      rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS19_USER_PARAMS(COMPANY_CODE_SYS01,USERNAME_SYS03,PARAM_CODE,VALUE) values(?,?,?,?)");
        pstmt.setString(1,vo.getCompanyCodeSys01SYS19());
        pstmt.setString(2,username);
        pstmt.setString(3,ApplicationConsts.BANK_ACCOUNT);
        pstmt.setString(4,vo.getBankAccountCodeAcc02DOC19());
        pstmt.executeUpdate();
      }

      // update vat endorse account...
      pstmt = conn.prepareStatement("update SYS19_USER_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and USERNAME_SYS03=? and PARAM_CODE=?");
      pstmt.setString(1,vo.getVatEndorseAccountCodeAcc02DOC19());
      pstmt.setString(2,vo.getCompanyCodeSys01SYS19());
      pstmt.setString(3,username);
      pstmt.setString(4,ApplicationConsts.VAT_ENDORSE_ACCOUNT);
      rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS19_USER_PARAMS(COMPANY_CODE_SYS01,USERNAME_SYS03,PARAM_CODE,VALUE) values(?,?,?,?)");
        pstmt.setString(1,vo.getCompanyCodeSys01SYS19());
        pstmt.setString(2,username);
        pstmt.setString(3,ApplicationConsts.VAT_ENDORSE_ACCOUNT);
        pstmt.setString(4,vo.getVatEndorseAccountCodeAcc02DOC19());
        pstmt.executeUpdate();
      }

			// update rounding costs account...
			pstmt = conn.prepareStatement("update SYS19_USER_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and USERNAME_SYS03=? and PARAM_CODE=?");
			pstmt.setString(1,vo.getRoundingCostsAccountCodeAcc02DOC19());
			pstmt.setString(2,vo.getCompanyCodeSys01SYS19());
			pstmt.setString(3,username);
			pstmt.setString(4,ApplicationConsts.ROUNDING_COSTS_CODE);
			rows = pstmt.executeUpdate();
			pstmt.close();
			if (rows==0) {
				// record not yet exists: it will be inserted...
				pstmt = conn.prepareStatement("insert into SYS19_USER_PARAMS(COMPANY_CODE_SYS01,USERNAME_SYS03,PARAM_CODE,VALUE) values(?,?,?,?)");
				pstmt.setString(1,vo.getCompanyCodeSys01SYS19());
				pstmt.setString(2,username);
				pstmt.setString(3,ApplicationConsts.ROUNDING_COSTS_CODE);
				pstmt.setString(4,vo.getRoundingCostsAccountCodeAcc02DOC19());
				pstmt.executeUpdate();
			}

			// update rounding proceeds account...
			pstmt = conn.prepareStatement("update SYS19_USER_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and USERNAME_SYS03=? and PARAM_CODE=?");
			pstmt.setString(1,vo.getRoundingProceedsAccountCodeAcc02DOC19());
			pstmt.setString(2,vo.getCompanyCodeSys01SYS19());
			pstmt.setString(3,username);
			pstmt.setString(4,ApplicationConsts.ROUNDING_PROCEEDS_CODE);
			rows = pstmt.executeUpdate();
			pstmt.close();
			if (rows==0) {
				// record not yet exists: it will be inserted...
				pstmt = conn.prepareStatement("insert into SYS19_USER_PARAMS(COMPANY_CODE_SYS01,USERNAME_SYS03,PARAM_CODE,VALUE) values(?,?,?,?)");
				pstmt.setString(1,vo.getCompanyCodeSys01SYS19());
				pstmt.setString(2,username);
				pstmt.setString(3,ApplicationConsts.ROUNDING_PROCEEDS_CODE);
				pstmt.setString(4,vo.getRoundingProceedsAccountCodeAcc02DOC19());
				pstmt.executeUpdate();
			}

      return new VOResponse(vo);
    } catch (Exception ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while storing user parameters",ex);
      try {
    	  if (this.conn==null && conn!=null)
    		  // rollback only local connection
    		  conn.rollback();
      }
      catch (Exception ex3) {
      }
      throw new Exception(ex.getMessage());
    } finally {
      try {
        pstmt.close();
      }
      catch (Exception ex) {
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




  /**
   * Business logic to execute.
   */
  public VOResponse loadCompanyParams(String companyCode,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;

    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      custBean.setConn(conn); // use same transaction...


      CompanyParametersVO vo = new CompanyParametersVO();
      vo.setCompanyCodeSys01SYS21(companyCode);
      Response res = null;

      // retrieve credit account...
      pstmt = conn.prepareStatement(
          "select VALUE,DESCRIPTION from SYS21_COMPANY_PARAMS,ACC02_ACCOUNTS,SYS10_TRANSLATIONS where "+
          "SYS21_COMPANY_PARAMS.COMPANY_CODE_SYS01=? and SYS21_COMPANY_PARAMS.PARAM_CODE=? and "+
          "SYS21_COMPANY_PARAMS.COMPANY_CODE_SYS01=ACC02_ACCOUNTS.COMPANY_CODE_SYS01 and "+
          "SYS21_COMPANY_PARAMS.VALUE=ACC02_ACCOUNTS.ACCOUNT_CODE and "+
          "ACC02_ACCOUNTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=?"
      );
      pstmt.setString(1,companyCode);
      pstmt.setString(2,ApplicationConsts.CREDITS_ACCOUNT);
      pstmt.setString(3,serverLanguageId);
      ResultSet rset = pstmt.executeQuery();
      while(rset.next()) {
        vo.setCreditAccountCodeAcc02SAL07(rset.getString(1));
        vo.setCreditAccountDescrSAL07(rset.getString(2));
      }
      rset.close();

      // retrieve items account...
      pstmt.close();
      pstmt = conn.prepareStatement(
          "select VALUE,DESCRIPTION from SYS21_COMPANY_PARAMS,ACC02_ACCOUNTS,SYS10_TRANSLATIONS where "+
          "SYS21_COMPANY_PARAMS.COMPANY_CODE_SYS01=? and SYS21_COMPANY_PARAMS.PARAM_CODE=? and "+
          "SYS21_COMPANY_PARAMS.COMPANY_CODE_SYS01=ACC02_ACCOUNTS.COMPANY_CODE_SYS01 and "+
          "SYS21_COMPANY_PARAMS.VALUE=ACC02_ACCOUNTS.ACCOUNT_CODE and "+
          "ACC02_ACCOUNTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=?"
      );
      pstmt.setString(1,companyCode);
      pstmt.setString(2,ApplicationConsts.ITEMS_ACCOUNT);
      pstmt.setString(3,serverLanguageId);
      rset = pstmt.executeQuery();
      while(rset.next()) {
        vo.setItemsAccountCodeAcc02SAL07(rset.getString(1));
        vo.setItemsAccountDescrSAL07(rset.getString(2));
      }
      rset.close();

      // retrieve activities account...
      pstmt.close();
      pstmt = conn.prepareStatement(
          "select VALUE,DESCRIPTION from SYS21_COMPANY_PARAMS,ACC02_ACCOUNTS,SYS10_TRANSLATIONS where "+
          "SYS21_COMPANY_PARAMS.COMPANY_CODE_SYS01=? and SYS21_COMPANY_PARAMS.PARAM_CODE=? and "+
          "SYS21_COMPANY_PARAMS.COMPANY_CODE_SYS01=ACC02_ACCOUNTS.COMPANY_CODE_SYS01 and "+
          "SYS21_COMPANY_PARAMS.VALUE=ACC02_ACCOUNTS.ACCOUNT_CODE and "+
          "ACC02_ACCOUNTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=?"
      );
      pstmt.setString(1,companyCode);
      pstmt.setString(2,ApplicationConsts.ACTIVITIES_ACCOUNT);
      pstmt.setString(3,serverLanguageId);
      rset = pstmt.executeQuery();
      while(rset.next()) {
        vo.setActivitiesAccountCodeAcc02SAL07(rset.getString(1));
        vo.setActivitiesAccountDescrSAL07(rset.getString(2));
      }
      rset.close();

      // retrieve charges account...
      pstmt.close();
      pstmt = conn.prepareStatement(
          "select VALUE,DESCRIPTION from SYS21_COMPANY_PARAMS,ACC02_ACCOUNTS,SYS10_TRANSLATIONS where "+
          "SYS21_COMPANY_PARAMS.COMPANY_CODE_SYS01=? and SYS21_COMPANY_PARAMS.PARAM_CODE=? and "+
          "SYS21_COMPANY_PARAMS.COMPANY_CODE_SYS01=ACC02_ACCOUNTS.COMPANY_CODE_SYS01 and "+
          "SYS21_COMPANY_PARAMS.VALUE=ACC02_ACCOUNTS.ACCOUNT_CODE and "+
          "ACC02_ACCOUNTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=?"
      );
      pstmt.setString(1,companyCode);
      pstmt.setString(2,ApplicationConsts.CHARGES_ACCOUNT);
      pstmt.setString(3,serverLanguageId);
      rset = pstmt.executeQuery();
      while(rset.next()) {
        vo.setChargesAccountCodeAcc02SAL07(rset.getString(1));
        vo.setChargesAccountDescrSAL07(rset.getString(2));
      }
      rset.close();

      // retrieve debits account...
      pstmt.close();
      pstmt = conn.prepareStatement(
          "select VALUE,DESCRIPTION from SYS21_COMPANY_PARAMS,ACC02_ACCOUNTS,SYS10_TRANSLATIONS where "+
          "SYS21_COMPANY_PARAMS.COMPANY_CODE_SYS01=? and SYS21_COMPANY_PARAMS.PARAM_CODE=? and "+
          "SYS21_COMPANY_PARAMS.COMPANY_CODE_SYS01=ACC02_ACCOUNTS.COMPANY_CODE_SYS01 and "+
          "SYS21_COMPANY_PARAMS.VALUE=ACC02_ACCOUNTS.ACCOUNT_CODE and "+
          "ACC02_ACCOUNTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=?"
      );
      pstmt.setString(1,companyCode);
      pstmt.setString(2,ApplicationConsts.DEBITS_ACCOUNT);
      pstmt.setString(3,serverLanguageId);
      rset = pstmt.executeQuery();
      while(rset.next()) {
        vo.setDebitAccountCodeAcc02PUR01(rset.getString(1));
        vo.setDebitAccountDescrPUR01(rset.getString(2));
      }
      rset.close();

      // retrieve costs account...
      pstmt.close();
      pstmt = conn.prepareStatement(
          "select VALUE,DESCRIPTION from SYS21_COMPANY_PARAMS,ACC02_ACCOUNTS,SYS10_TRANSLATIONS where "+
          "SYS21_COMPANY_PARAMS.COMPANY_CODE_SYS01=? and SYS21_COMPANY_PARAMS.PARAM_CODE=? and "+
          "SYS21_COMPANY_PARAMS.COMPANY_CODE_SYS01=ACC02_ACCOUNTS.COMPANY_CODE_SYS01 and "+
          "SYS21_COMPANY_PARAMS.VALUE=ACC02_ACCOUNTS.ACCOUNT_CODE and "+
          "ACC02_ACCOUNTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=?"
      );
      pstmt.setString(1,companyCode);
      pstmt.setString(2,ApplicationConsts.COSTS_ACCOUNT);
      pstmt.setString(3,serverLanguageId);
      rset = pstmt.executeQuery();
      while(rset.next()) {
        vo.setCostsAccountCodeAcc02PUR01(rset.getString(1));
        vo.setCostsAccountDescrPUR01(rset.getString(2));
      }
      rset.close();

      // retrieve case account...
      pstmt.close();
      pstmt = conn.prepareStatement(
          "select VALUE,DESCRIPTION from SYS21_COMPANY_PARAMS,ACC02_ACCOUNTS,SYS10_TRANSLATIONS where "+
          "SYS21_COMPANY_PARAMS.COMPANY_CODE_SYS01=? and SYS21_COMPANY_PARAMS.PARAM_CODE=? and "+
          "SYS21_COMPANY_PARAMS.COMPANY_CODE_SYS01=ACC02_ACCOUNTS.COMPANY_CODE_SYS01 and "+
          "SYS21_COMPANY_PARAMS.VALUE=ACC02_ACCOUNTS.ACCOUNT_CODE and "+
          "ACC02_ACCOUNTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=?"
      );
      pstmt.setString(1,companyCode);
      pstmt.setString(2,ApplicationConsts.CASE_ACCOUNT);
      pstmt.setString(3,serverLanguageId);
      rset = pstmt.executeQuery();
      while(rset.next()) {
        vo.setCaseAccountCodeAcc02DOC21(rset.getString(1));
        vo.setCaseAccountDescrDOC21(rset.getString(2));
      }
      rset.close();

      // retrieve bank account...
      pstmt.close();
      pstmt = conn.prepareStatement(
          "select VALUE,DESCRIPTION from SYS21_COMPANY_PARAMS,ACC02_ACCOUNTS,SYS10_TRANSLATIONS where "+
          "SYS21_COMPANY_PARAMS.COMPANY_CODE_SYS01=? and SYS21_COMPANY_PARAMS.PARAM_CODE=? and "+
          "SYS21_COMPANY_PARAMS.COMPANY_CODE_SYS01=ACC02_ACCOUNTS.COMPANY_CODE_SYS01 and "+
          "SYS21_COMPANY_PARAMS.VALUE=ACC02_ACCOUNTS.ACCOUNT_CODE and "+
          "ACC02_ACCOUNTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=?"
      );
      pstmt.setString(1,companyCode);
      pstmt.setString(2,ApplicationConsts.BANK_ACCOUNT);
      pstmt.setString(3,serverLanguageId);
      rset = pstmt.executeQuery();
      while(rset.next()) {
        vo.setBankAccountCodeAcc02DOC21(rset.getString(1));
        vo.setBankAccountDescrDOC21(rset.getString(2));
      }
      rset.close();

      // retrieve vat endorse account...
      pstmt.close();
      pstmt = conn.prepareStatement(
          "select VALUE,DESCRIPTION from SYS21_COMPANY_PARAMS,ACC02_ACCOUNTS,SYS10_TRANSLATIONS where "+
          "SYS21_COMPANY_PARAMS.COMPANY_CODE_SYS01=? and SYS21_COMPANY_PARAMS.PARAM_CODE=? and "+
          "SYS21_COMPANY_PARAMS.COMPANY_CODE_SYS01=ACC02_ACCOUNTS.COMPANY_CODE_SYS01 and "+
          "SYS21_COMPANY_PARAMS.VALUE=ACC02_ACCOUNTS.ACCOUNT_CODE and "+
          "ACC02_ACCOUNTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=?"
      );
      pstmt.setString(1,companyCode);
      pstmt.setString(2,ApplicationConsts.VAT_ENDORSE_ACCOUNT);
      pstmt.setString(3,serverLanguageId);
      rset = pstmt.executeQuery();
      while(rset.next()) {
        vo.setVatEndorseAccountCodeAcc02DOC21(rset.getString(1));
        vo.setVatEndorseAccountDescrDOC21(rset.getString(2));
      }
      rset.close();

      // retrieve loss/profit econ. endorse account...
      pstmt.close();
      pstmt = conn.prepareStatement(
          "select VALUE,DESCRIPTION from SYS21_COMPANY_PARAMS,ACC02_ACCOUNTS,SYS10_TRANSLATIONS where "+
          "SYS21_COMPANY_PARAMS.COMPANY_CODE_SYS01=? and SYS21_COMPANY_PARAMS.PARAM_CODE=? and "+
          "SYS21_COMPANY_PARAMS.COMPANY_CODE_SYS01=ACC02_ACCOUNTS.COMPANY_CODE_SYS01 and "+
          "SYS21_COMPANY_PARAMS.VALUE=ACC02_ACCOUNTS.ACCOUNT_CODE and "+
          "ACC02_ACCOUNTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=?"
      );
      pstmt.setString(1,companyCode);
      pstmt.setString(2,ApplicationConsts.LOSSPROFIT_E_ACCOUNT);
      pstmt.setString(3,serverLanguageId);
      rset = pstmt.executeQuery();
      while(rset.next()) {
        vo.setLossProfitEAccountCodeAcc02DOC21(rset.getString(1));
        vo.setLossProfitEAccountDescrDOC21(rset.getString(2));
      }
      rset.close();

      // retrieve loss/profit patrim. endorse account...
      pstmt.close();
      pstmt = conn.prepareStatement(
          "select VALUE,DESCRIPTION from SYS21_COMPANY_PARAMS,ACC02_ACCOUNTS,SYS10_TRANSLATIONS where "+
          "SYS21_COMPANY_PARAMS.COMPANY_CODE_SYS01=? and SYS21_COMPANY_PARAMS.PARAM_CODE=? and "+
          "SYS21_COMPANY_PARAMS.COMPANY_CODE_SYS01=ACC02_ACCOUNTS.COMPANY_CODE_SYS01 and "+
          "SYS21_COMPANY_PARAMS.VALUE=ACC02_ACCOUNTS.ACCOUNT_CODE and "+
          "ACC02_ACCOUNTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=?"
      );
      pstmt.setString(1,companyCode);
      pstmt.setString(2,ApplicationConsts.LOSSPROFIT_P_ACCOUNT);
      pstmt.setString(3,serverLanguageId);
      rset = pstmt.executeQuery();
      while(rset.next()) {
        vo.setLossProfitPAccountCodeAcc02DOC21(rset.getString(1));
        vo.setLossProfitPAccountDescrDOC21(rset.getString(2));
      }
      rset.close();

      // retrieve closing account...
      pstmt.close();
      pstmt = conn.prepareStatement(
          "select VALUE,DESCRIPTION from SYS21_COMPANY_PARAMS,ACC02_ACCOUNTS,SYS10_TRANSLATIONS where "+
          "SYS21_COMPANY_PARAMS.COMPANY_CODE_SYS01=? and SYS21_COMPANY_PARAMS.PARAM_CODE=? and "+
          "SYS21_COMPANY_PARAMS.COMPANY_CODE_SYS01=ACC02_ACCOUNTS.COMPANY_CODE_SYS01 and "+
          "SYS21_COMPANY_PARAMS.VALUE=ACC02_ACCOUNTS.ACCOUNT_CODE and "+
          "ACC02_ACCOUNTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=?"
      );
      pstmt.setString(1,companyCode);
      pstmt.setString(2,ApplicationConsts.CLOSING_ACCOUNT);
      pstmt.setString(3,serverLanguageId);
      rset = pstmt.executeQuery();
      while(rset.next()) {
        vo.setClosingAccountCodeAcc02DOC21(rset.getString(1));
        vo.setClosingAccountDescrDOC21(rset.getString(2));
      }
      rset.close();

      // retrieve opening account...
      pstmt.close();
      pstmt = conn.prepareStatement(
          "select VALUE,DESCRIPTION from SYS21_COMPANY_PARAMS,ACC02_ACCOUNTS,SYS10_TRANSLATIONS where "+
          "SYS21_COMPANY_PARAMS.COMPANY_CODE_SYS01=? and SYS21_COMPANY_PARAMS.PARAM_CODE=? and "+
          "SYS21_COMPANY_PARAMS.COMPANY_CODE_SYS01=ACC02_ACCOUNTS.COMPANY_CODE_SYS01 and "+
          "SYS21_COMPANY_PARAMS.VALUE=ACC02_ACCOUNTS.ACCOUNT_CODE and "+
          "ACC02_ACCOUNTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=?"
      );
      pstmt.setString(1,companyCode);
      pstmt.setString(2,ApplicationConsts.OPENING_ACCOUNT);
      pstmt.setString(3,serverLanguageId);
      rset = pstmt.executeQuery();
      while(rset.next()) {
        vo.setOpeningAccountCodeAcc02DOC21(rset.getString(1));
        vo.setOpeningAccountDescrDOC21(rset.getString(2));
      }
      rset.close();




      // retrieve morning start hour...
      pstmt.close();
      pstmt = conn.prepareStatement(
          "select VALUE from SYS21_COMPANY_PARAMS where SYS21_COMPANY_PARAMS.COMPANY_CODE_SYS01=? and SYS21_COMPANY_PARAMS.PARAM_CODE=? "
      );
      pstmt.setString(1,companyCode);
      pstmt.setString(2,ApplicationConsts.MORNING_START_HOUR);
      rset = pstmt.executeQuery();
      while(rset.next()) {
        vo.setMorningStartHourSCH02(rset.getTimestamp(1));
      }
      rset.close();

      // retrieve morning end hour...
      pstmt.close();
      pstmt = conn.prepareStatement(
          "select VALUE from SYS21_COMPANY_PARAMS where SYS21_COMPANY_PARAMS.COMPANY_CODE_SYS01=? and SYS21_COMPANY_PARAMS.PARAM_CODE=? "
      );
      pstmt.setString(1,companyCode);
      pstmt.setString(2,ApplicationConsts.MORNING_END_HOUR);
      rset = pstmt.executeQuery();
      while(rset.next()) {
        vo.setMorningEndHourSCH02(rset.getTimestamp(1));
      }
      rset.close();

      // retrieve afternoon start hour...
      pstmt.close();
      pstmt = conn.prepareStatement(
          "select VALUE from SYS21_COMPANY_PARAMS where SYS21_COMPANY_PARAMS.COMPANY_CODE_SYS01=? and SYS21_COMPANY_PARAMS.PARAM_CODE=? "
      );
      pstmt.setString(1,companyCode);
      pstmt.setString(2,ApplicationConsts.AFTERNOON_START_HOUR);
      rset = pstmt.executeQuery();
      while(rset.next()) {
        vo.setAfternoonStartHourSCH02(rset.getTimestamp(1));
      }
      rset.close();

      // retrieve afternoon end hour...
      pstmt.close();
      pstmt = conn.prepareStatement(
          "select VALUE from SYS21_COMPANY_PARAMS where SYS21_COMPANY_PARAMS.COMPANY_CODE_SYS01=? and SYS21_COMPANY_PARAMS.PARAM_CODE=? "
      );
      pstmt.setString(1,companyCode);
      pstmt.setString(2,ApplicationConsts.AFTERNOON_END_HOUR);
      rset = pstmt.executeQuery();
      while(rset.next()) {
        vo.setAfternoonEndHourSCH02(rset.getTimestamp(1));
      }
      rset.close();

      // retrieve sectional for sale invoices...
      pstmt.close();
      pstmt = conn.prepareStatement(
          "select VALUE from SYS21_COMPANY_PARAMS where SYS21_COMPANY_PARAMS.COMPANY_CODE_SYS01=? and SYS21_COMPANY_PARAMS.PARAM_CODE=? "
      );
      pstmt.setString(1,companyCode);
      pstmt.setString(2,ApplicationConsts.SALE_SECTIONAL);
      rset = pstmt.executeQuery();
      while(rset.next()) {
        vo.setSaleSectionalDOC01(rset.getString(1));
      }
      rset.close();


      // retrieve initial value for progressives...
      pstmt.close();
      pstmt = conn.prepareStatement(
          "select VALUE from SYS21_COMPANY_PARAMS where SYS21_COMPANY_PARAMS.COMPANY_CODE_SYS01=? and SYS21_COMPANY_PARAMS.PARAM_CODE=? "
      );
      pstmt.setString(1,companyCode);
      pstmt.setString(2,ApplicationConsts.INITIAL_VALUE);
      rset = pstmt.executeQuery();
      while(rset.next()) {
        vo.setInitialValueSYS21(new BigDecimal(rset.getString(1)));
      }
      rset.close();


			// retrieve rounding costs account...
			pstmt.close();
			pstmt = conn.prepareStatement(
					"select VALUE,DESCRIPTION from SYS21_COMPANY_PARAMS,ACC02_ACCOUNTS,SYS10_TRANSLATIONS where "+
					"SYS21_COMPANY_PARAMS.COMPANY_CODE_SYS01=? and SYS21_COMPANY_PARAMS.PARAM_CODE=? and "+
					"SYS21_COMPANY_PARAMS.COMPANY_CODE_SYS01=ACC02_ACCOUNTS.COMPANY_CODE_SYS01 and "+
					"SYS21_COMPANY_PARAMS.VALUE=ACC02_ACCOUNTS.ACCOUNT_CODE and "+
					"ACC02_ACCOUNTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
					"SYS10_TRANSLATIONS.LANGUAGE_CODE=?"
			);
			pstmt.setString(1,companyCode);
			pstmt.setString(2,ApplicationConsts.ROUNDING_COSTS_CODE);
			pstmt.setString(3,serverLanguageId);
			rset = pstmt.executeQuery();
			while(rset.next()) {
				vo.setRoundingCostsAccountCodeAcc02DOC19(rset.getString(1));
				vo.setRoundingCostsDescrDOC19(rset.getString(2));
			}
			rset.close();

			// retrieve rounding proceeds account...
			pstmt.close();
			pstmt = conn.prepareStatement(
					"select VALUE,DESCRIPTION from SYS21_COMPANY_PARAMS,ACC02_ACCOUNTS,SYS10_TRANSLATIONS where "+
					"SYS21_COMPANY_PARAMS.COMPANY_CODE_SYS01=? and SYS21_COMPANY_PARAMS.PARAM_CODE=? and "+
					"SYS21_COMPANY_PARAMS.COMPANY_CODE_SYS01=ACC02_ACCOUNTS.COMPANY_CODE_SYS01 and "+
					"SYS21_COMPANY_PARAMS.VALUE=ACC02_ACCOUNTS.ACCOUNT_CODE and "+
					"ACC02_ACCOUNTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
					"SYS10_TRANSLATIONS.LANGUAGE_CODE=?"
			);
			pstmt.setString(1,companyCode);
			pstmt.setString(2,ApplicationConsts.ROUNDING_PROCEEDS_CODE);
			pstmt.setString(3,serverLanguageId);
			rset = pstmt.executeQuery();
			while(rset.next()) {
				vo.setRoundingProceedsAccountCodeAcc02DOC19(rset.getString(1));
				vo.setRoundingProceedsDescrDOC19(rset.getString(2));
			}
			rset.close();


      return new VOResponse(vo);
    } catch (Exception ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while loading company parameters",ex);
      try {
    	  if (this.conn==null && conn!=null)
    		  // rollback only local connection
    		  conn.rollback();
      }
      catch (Exception ex3) {
      }
      throw new Exception(ex.getMessage());
    } finally {
      try {
        pstmt.close();
      }
      catch (Exception ex) {
      }
      try {
        custBean.setConn(null);
      } catch (Exception ex) {}

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
  public VOResponse loadUserParam(HashMap params,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;



      String companyCode = (String)params.get(ApplicationConsts.COMPANY_CODE_SYS01);
      String paramCode = (String)params.get(ApplicationConsts.PARAM_CODE);

      // retrieve parameter at user+company level...
      pstmt = conn.prepareStatement("select VALUE from SYS19_USER_PARAMS where COMPANY_CODE_SYS01=? and USERNAME_SYS03=? and PARAM_CODE=?");
      pstmt.setString(1,companyCode);
      pstmt.setString(2,username);
      pstmt.setString(3,paramCode);
      ResultSet rset = pstmt.executeQuery();
      String value = null;
      boolean paramFound = false;
      while(rset.next()) {
        value = rset.getString(1);
        paramFound = true;
      }
      rset.close();

      if (!paramFound) {
        // retrieve parameter at company level...
        pstmt.close();
        pstmt = conn.prepareStatement("select VALUE from SYS21_COMPANY_PARAMS where COMPANY_CODE_SYS01=? and PARAM_CODE=?");
        pstmt.setString(1,companyCode);
        pstmt.setString(2,paramCode);
        rset = pstmt.executeQuery();
        while(rset.next()) {
          value = rset.getString(1);
        }
        rset.close();
      }

      return new VOResponse(value);
    } catch (Exception ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while loading a user parameter",ex);
      throw new Exception(ex.getMessage());
    } finally {
      try {
        pstmt.close();
      }
      catch (Exception ex) {
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






  /**
   * @erturn image repository path
   */
  public final String getImagePath(HashMap applicationPars) {
    return applicationPars==null?null:(String)applicationPars.get(ApplicationConsts.IMAGE_PATH);
  }


  /**
   * @erturn document repository path
   */
  public final String getDocumentPath(HashMap applicationPars) {
    return applicationPars==null?null:(String)applicationPars.get(ApplicationConsts.DOC_PATH);
  }


	/**
	 * @erturn flag for sale doc nums value for progressives
	 */
	public final String getManualDocNumInSaleDocs(HashMap applicationPars) {
		return applicationPars==null?null:(String)applicationPars.get(ApplicationConsts.DOC_NUM_IN_SALE_DOCS);
	}


  /**
   * @erturn increment value for progressives
   */
  public final BigDecimal getIncrementValue(HashMap applicationPars) {
	  String value = applicationPars==null?null:(String)applicationPars.get(ApplicationConsts.INCREMENT_VALUE);
	  return value==null?null:new BigDecimal(value);
  }


		/**
		 * @erturn increment value for progressives
		 */
		public final String getInvPosCorrForGoodItemsValue(HashMap applicationPars) {
			return applicationPars==null?null:(String)applicationPars.get(ApplicationConsts.POS_INVCORR_GOOD_ITM);
		}

		/**
		 * @erturn increment value for progressives
		 */
		public final String getInvNegCorrForGoodItemsValue(HashMap applicationPars) {
			return applicationPars==null?null:(String)applicationPars.get(ApplicationConsts.NEG_INVCORR_GOOD_ITM);
		}

		/**
		 * @erturn increment value for progressives
		 */
		public final String getInvPosCorrForDamagedItemsValue(HashMap applicationPars) {
			return applicationPars==null?null:(String)applicationPars.get(ApplicationConsts.POS_INVCORR_DAMG_ITM);
		}

		/**
		 * @erturn increment value for progressives
		 */
		public final String getInvNegCorrForDamagedItemsValue(HashMap applicationPars) {
			return applicationPars==null?null:(String)applicationPars.get(ApplicationConsts.NEG_INVCORR_DAMG_ITM);
		}





}

