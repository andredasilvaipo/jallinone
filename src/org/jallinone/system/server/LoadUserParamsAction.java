package org.jallinone.system.server;

import org.openswing.swing.logger.server.*;
import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.internationalization.java.Language;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.system.java.UserParametersVO;
import org.jallinone.sales.customers.server.ValidateCustomerCodeAction;
import org.jallinone.sales.customers.java.CustomerPK;
import org.openswing.swing.message.send.java.LookupValidationParams;
import org.jallinone.sales.customers.java.GridCustomerVO;
import org.jallinone.sales.pricelist.server.ValidatePricelistCodeAction;
import org.jallinone.sales.pricelist.java.PricelistVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to retrieve user parameters, based on the specified COMPANY_CODE.</p>
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
public class LoadUserParamsAction implements Action {

  private ValidateCustomerCodeAction custBean = new ValidateCustomerCodeAction();


  public LoadUserParamsAction() {
  }


  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadUserParams";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      conn = ConnectionManager.getConnection(context);
      String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();

      String companyCode = (String)inputPar;

      UserParametersVO vo = new UserParametersVO();
      vo.setCompanyCodeSys01SYS19(companyCode);
      Response res = null;

      // retrieve customer...
      pstmt = conn.prepareStatement("select VALUE from SYS19_USER_PARAMS where COMPANY_CODE_SYS01=? and USERNAME_SYS03=? and PARAM_CODE=?");
      pstmt.setString(1,companyCode);
      pstmt.setString(2,userSessionPars.getUsername());
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
        res = custBean.executeCommand(
            pars,
            userSessionPars,
            request,
            response,
            userSession,
            context
        );
        if (res.isError())
          return res;
        ArrayList list = ((VOListResponse)res).getRows();
        if (list.size()>0) {
          GridCustomerVO custVO = (GridCustomerVO)list.get(0);
          vo.setName_1REG04(custVO.getName_1REG04());
          vo.setName_2REG04(custVO.getName_2REG04());
        }
      }

      // retrieve receipts management program path...
      pstmt.close();
      pstmt = conn.prepareStatement("select VALUE from SYS19_USER_PARAMS where COMPANY_CODE_SYS01=? and USERNAME_SYS03=? and PARAM_CODE=?");
      pstmt.setString(1,companyCode);
      pstmt.setString(2,userSessionPars.getUsername());
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
      pstmt.setString(2,userSessionPars.getUsername());
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
      pstmt.setString(2,userSessionPars.getUsername());
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
      pstmt.setString(2,userSessionPars.getUsername());
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
      pstmt.setString(2,userSessionPars.getUsername());
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
      pstmt.setString(2,userSessionPars.getUsername());
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
      pstmt.setString(2,userSessionPars.getUsername());
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
      pstmt.setString(2,userSessionPars.getUsername());
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
      pstmt.setString(2,userSessionPars.getUsername());
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
      pstmt.setString(2,userSessionPars.getUsername());
      pstmt.setString(3,ApplicationConsts.VAT_ENDORSE_ACCOUNT);
      pstmt.setString(4,serverLanguageId);
      rset = pstmt.executeQuery();
      while(rset.next()) {
        vo.setVatEndorseAccountCodeAcc02DOC19(rset.getString(1));
        vo.setVatEndorseAccountDescrDOC19(rset.getString(2));
      }
      rset.close();


      return new VOResponse(vo);
    } catch (Exception ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while loading user parameters",ex);
      return new ErrorResponse(ex.getMessage());
    } finally {
      try {
        pstmt.close();
      }
      catch (Exception ex) {
      }
      try {
        ConnectionManager.releaseConnection(conn,context);
      }
      catch (Exception ex2) {
      }
    }

  }


}
