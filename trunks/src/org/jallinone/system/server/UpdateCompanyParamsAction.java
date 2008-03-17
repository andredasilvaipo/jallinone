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


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to store company parameters in SYS21 table, based on the specified COMPANY_CODE.</p>
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
public class UpdateCompanyParamsAction implements Action {

  public UpdateCompanyParamsAction() {
  }


  /**
   * @return request name
   */
  public final String getRequestName() {
    return "updateCompanyParams";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      conn = ConnectionManager.getConnection(context);

      Object[] vos = (Object[])inputPar;
      CompanyParametersVO vo = (CompanyParametersVO)vos[1];

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


      conn.commit();

      return new VOResponse(vo);
    } catch (Exception ex) {
      try {
        conn.rollback();
      }
      catch (SQLException ex1) {
      }
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while storing company parameters",ex);
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
