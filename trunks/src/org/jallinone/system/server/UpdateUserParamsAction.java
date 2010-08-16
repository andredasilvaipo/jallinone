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


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to store user parameters, based on the specified COMPANY_CODE.</p>
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
public class UpdateUserParamsAction implements Action {

  public UpdateUserParamsAction() {
  }


  /**
   * @return request name
   */
  public final String getRequestName() {
    return "updateUserParams";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      conn = ConnectionManager.getConnection(context);

      ValueObject[] vos = (ValueObject[])inputPar;
      UserParametersVO vo = (UserParametersVO)vos[1];

      // update customer code...
      pstmt = conn.prepareStatement("update SYS19_USER_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and USERNAME_SYS03=? and PARAM_CODE=?");
      pstmt.setString(1,vo.getCustomerCodeSAL07());
      pstmt.setString(2,vo.getCompanyCodeSys01SYS19());
      pstmt.setString(3,userSessionPars.getUsername());
      pstmt.setString(4,ApplicationConsts.CUSTOMER_CODE);
      int rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS19_USER_PARAMS(COMPANY_CODE_SYS01,USERNAME_SYS03,PARAM_CODE,VALUE) values(?,?,?,?)");
        pstmt.setString(1,vo.getCompanyCodeSys01SYS19());
        pstmt.setString(2,userSessionPars.getUsername());
        pstmt.setString(3,ApplicationConsts.CUSTOMER_CODE);
        pstmt.setString(4,vo.getCustomerCodeSAL07());
        pstmt.executeUpdate();
      }

      // update warehouse code...
      pstmt = conn.prepareStatement("update SYS19_USER_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and USERNAME_SYS03=? and PARAM_CODE=?");
      pstmt.setString(1,vo.getWarehouseCodeWAR01());
      pstmt.setString(2,vo.getCompanyCodeSys01SYS19());
      pstmt.setString(3,userSessionPars.getUsername());
      pstmt.setString(4,ApplicationConsts.WAREHOUSE_CODE);
      rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS19_USER_PARAMS(COMPANY_CODE_SYS01,USERNAME_SYS03,PARAM_CODE,VALUE) values(?,?,?,?)");
        pstmt.setString(1,vo.getCompanyCodeSys01SYS19());
        pstmt.setString(2,userSessionPars.getUsername());
        pstmt.setString(3,ApplicationConsts.WAREHOUSE_CODE);
        pstmt.setString(4,vo.getWarehouseCodeWAR01());
        pstmt.executeUpdate();
      }

      // update receipts management program path...
      pstmt = conn.prepareStatement("update SYS19_USER_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and USERNAME_SYS03=? and PARAM_CODE=?");
      pstmt.setString(1,vo.getReceiptPath());
      pstmt.setString(2,vo.getCompanyCodeSys01SYS19());
      pstmt.setString(3,userSessionPars.getUsername());
      pstmt.setString(4,ApplicationConsts.RECEIPT_PATH);
      rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS19_USER_PARAMS(COMPANY_CODE_SYS01,USERNAME_SYS03,PARAM_CODE,VALUE) values(?,?,?,?)");
        pstmt.setString(1,vo.getCompanyCodeSys01SYS19());
        pstmt.setString(2,userSessionPars.getUsername());
        pstmt.setString(3,ApplicationConsts.RECEIPT_PATH);
        pstmt.setString(4,vo.getReceiptPath());
        pstmt.executeUpdate();
      }


      // update credit account...
      pstmt = conn.prepareStatement("update SYS19_USER_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and USERNAME_SYS03=? and PARAM_CODE=?");
      pstmt.setString(1,vo.getCreditAccountCodeAcc02SAL07());
      pstmt.setString(2,vo.getCompanyCodeSys01SYS19());
      pstmt.setString(3,userSessionPars.getUsername());
      pstmt.setString(4,ApplicationConsts.CREDITS_ACCOUNT);
      rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS19_USER_PARAMS(COMPANY_CODE_SYS01,USERNAME_SYS03,PARAM_CODE,VALUE) values(?,?,?,?)");
        pstmt.setString(1,vo.getCompanyCodeSys01SYS19());
        pstmt.setString(2,userSessionPars.getUsername());
        pstmt.setString(3,ApplicationConsts.CREDITS_ACCOUNT);
        pstmt.setString(4,vo.getCreditAccountCodeAcc02SAL07());
        pstmt.executeUpdate();
      }

      // update item account...
      pstmt = conn.prepareStatement("update SYS19_USER_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and USERNAME_SYS03=? and PARAM_CODE=?");
      pstmt.setString(1,vo.getItemsAccountCodeAcc02SAL07());
      pstmt.setString(2,vo.getCompanyCodeSys01SYS19());
      pstmt.setString(3,userSessionPars.getUsername());
      pstmt.setString(4,ApplicationConsts.ITEMS_ACCOUNT);
      rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS19_USER_PARAMS(COMPANY_CODE_SYS01,USERNAME_SYS03,PARAM_CODE,VALUE) values(?,?,?,?)");
        pstmt.setString(1,vo.getCompanyCodeSys01SYS19());
        pstmt.setString(2,userSessionPars.getUsername());
        pstmt.setString(3,ApplicationConsts.ITEMS_ACCOUNT);
        pstmt.setString(4,vo.getItemsAccountCodeAcc02SAL07());
        pstmt.executeUpdate();
      }

      // update charges account...
      pstmt = conn.prepareStatement("update SYS19_USER_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and USERNAME_SYS03=? and PARAM_CODE=?");
      pstmt.setString(1,vo.getChargesAccountCodeAcc02SAL07());
      pstmt.setString(2,vo.getCompanyCodeSys01SYS19());
      pstmt.setString(3,userSessionPars.getUsername());
      pstmt.setString(4,ApplicationConsts.CHARGES_ACCOUNT);
      rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS19_USER_PARAMS(COMPANY_CODE_SYS01,USERNAME_SYS03,PARAM_CODE,VALUE) values(?,?,?,?)");
        pstmt.setString(1,vo.getCompanyCodeSys01SYS19());
        pstmt.setString(2,userSessionPars.getUsername());
        pstmt.setString(3,ApplicationConsts.CHARGES_ACCOUNT);
        pstmt.setString(4,vo.getChargesAccountCodeAcc02SAL07());
        pstmt.executeUpdate();
      }

      // update activities account...
      pstmt = conn.prepareStatement("update SYS19_USER_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and USERNAME_SYS03=? and PARAM_CODE=?");
      pstmt.setString(1,vo.getActivitiesAccountCodeAcc02SAL07());
      pstmt.setString(2,vo.getCompanyCodeSys01SYS19());
      pstmt.setString(3,userSessionPars.getUsername());
      pstmt.setString(4,ApplicationConsts.ACTIVITIES_ACCOUNT);
      rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS19_USER_PARAMS(COMPANY_CODE_SYS01,USERNAME_SYS03,PARAM_CODE,VALUE) values(?,?,?,?)");
        pstmt.setString(1,vo.getCompanyCodeSys01SYS19());
        pstmt.setString(2,userSessionPars.getUsername());
        pstmt.setString(3,ApplicationConsts.ACTIVITIES_ACCOUNT);
        pstmt.setString(4,vo.getActivitiesAccountCodeAcc02SAL07());
        pstmt.executeUpdate();
      }

      // update debit account...
      pstmt = conn.prepareStatement("update SYS19_USER_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and USERNAME_SYS03=? and PARAM_CODE=?");
      pstmt.setString(1,vo.getDebitAccountCodeAcc02PUR01());
      pstmt.setString(2,vo.getCompanyCodeSys01SYS19());
      pstmt.setString(3,userSessionPars.getUsername());
      pstmt.setString(4,ApplicationConsts.DEBITS_ACCOUNT);
      rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS19_USER_PARAMS(COMPANY_CODE_SYS01,USERNAME_SYS03,PARAM_CODE,VALUE) values(?,?,?,?)");
        pstmt.setString(1,vo.getCompanyCodeSys01SYS19());
        pstmt.setString(2,userSessionPars.getUsername());
        pstmt.setString(3,ApplicationConsts.DEBITS_ACCOUNT);
        pstmt.setString(4,vo.getDebitAccountCodeAcc02PUR01());
        pstmt.executeUpdate();
      }

      // update costs account...
      pstmt = conn.prepareStatement("update SYS19_USER_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and USERNAME_SYS03=? and PARAM_CODE=?");
      pstmt.setString(1,vo.getCostsAccountCodeAcc02PUR01());
      pstmt.setString(2,vo.getCompanyCodeSys01SYS19());
      pstmt.setString(3,userSessionPars.getUsername());
      pstmt.setString(4,ApplicationConsts.COSTS_ACCOUNT);
      rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS19_USER_PARAMS(COMPANY_CODE_SYS01,USERNAME_SYS03,PARAM_CODE,VALUE) values(?,?,?,?)");
        pstmt.setString(1,vo.getCompanyCodeSys01SYS19());
        pstmt.setString(2,userSessionPars.getUsername());
        pstmt.setString(3,ApplicationConsts.COSTS_ACCOUNT);
        pstmt.setString(4,vo.getCostsAccountCodeAcc02PUR01());
        pstmt.executeUpdate();
      }

      // update case account...
      pstmt = conn.prepareStatement("update SYS19_USER_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and USERNAME_SYS03=? and PARAM_CODE=?");
      pstmt.setString(1,vo.getCaseAccountCodeAcc02DOC19());
      pstmt.setString(2,vo.getCompanyCodeSys01SYS19());
      pstmt.setString(3,userSessionPars.getUsername());
      pstmt.setString(4,ApplicationConsts.CASE_ACCOUNT);
      rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS19_USER_PARAMS(COMPANY_CODE_SYS01,USERNAME_SYS03,PARAM_CODE,VALUE) values(?,?,?,?)");
        pstmt.setString(1,vo.getCompanyCodeSys01SYS19());
        pstmt.setString(2,userSessionPars.getUsername());
        pstmt.setString(3,ApplicationConsts.CASE_ACCOUNT);
        pstmt.setString(4,vo.getCaseAccountCodeAcc02DOC19());
        pstmt.executeUpdate();
      }

      // update bank account...
      pstmt = conn.prepareStatement("update SYS19_USER_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and USERNAME_SYS03=? and PARAM_CODE=?");
      pstmt.setString(1,vo.getBankAccountCodeAcc02DOC19());
      pstmt.setString(2,vo.getCompanyCodeSys01SYS19());
      pstmt.setString(3,userSessionPars.getUsername());
      pstmt.setString(4,ApplicationConsts.BANK_ACCOUNT);
      rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS19_USER_PARAMS(COMPANY_CODE_SYS01,USERNAME_SYS03,PARAM_CODE,VALUE) values(?,?,?,?)");
        pstmt.setString(1,vo.getCompanyCodeSys01SYS19());
        pstmt.setString(2,userSessionPars.getUsername());
        pstmt.setString(3,ApplicationConsts.BANK_ACCOUNT);
        pstmt.setString(4,vo.getBankAccountCodeAcc02DOC19());
        pstmt.executeUpdate();
      }

      // update vat endorse account...
      pstmt = conn.prepareStatement("update SYS19_USER_PARAMS set VALUE=? where COMPANY_CODE_SYS01=? and USERNAME_SYS03=? and PARAM_CODE=?");
      pstmt.setString(1,vo.getVatEndorseAccountCodeAcc02DOC19());
      pstmt.setString(2,vo.getCompanyCodeSys01SYS19());
      pstmt.setString(3,userSessionPars.getUsername());
      pstmt.setString(4,ApplicationConsts.VAT_ENDORSE_ACCOUNT);
      rows = pstmt.executeUpdate();
      pstmt.close();
      if (rows==0) {
        // record not yet exists: it will be inserted...
        pstmt = conn.prepareStatement("insert into SYS19_USER_PARAMS(COMPANY_CODE_SYS01,USERNAME_SYS03,PARAM_CODE,VALUE) values(?,?,?,?)");
        pstmt.setString(1,vo.getCompanyCodeSys01SYS19());
        pstmt.setString(2,userSessionPars.getUsername());
        pstmt.setString(3,ApplicationConsts.VAT_ENDORSE_ACCOUNT);
        pstmt.setString(4,vo.getVatEndorseAccountCodeAcc02DOC19());
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while storing user parameters",ex);
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
