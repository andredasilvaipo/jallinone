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
import org.jallinone.system.java.CompanyParametersVO;
import org.jallinone.sales.customers.server.ValidateCustomerCodeAction;
import org.jallinone.sales.customers.java.CustomerPK;
import org.openswing.swing.message.send.java.LookupValidationParams;
import org.jallinone.sales.customers.java.GridCustomerVO;
import org.jallinone.sales.pricelist.server.ValidatePricelistCodeAction;
import org.jallinone.sales.pricelist.java.PricelistVO;
import java.math.BigDecimal;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to retrieve company parameters from SYS21, based on the specified COMPANY_CODE.</p>
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
public class LoadCompanyParamsAction implements Action {

  private ValidateCustomerCodeAction custBean = new ValidateCustomerCodeAction();


  public LoadCompanyParamsAction() {
  }


  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadCompanyParams";
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


      return new VOResponse(vo);
    } catch (Exception ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while loading company parameters",ex);
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
