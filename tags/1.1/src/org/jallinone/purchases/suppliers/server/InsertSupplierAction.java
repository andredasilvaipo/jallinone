package org.jallinone.purchases.suppliers.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.purchases.suppliers.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import java.math.BigDecimal;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.system.progressives.server.ProgressiveUtils;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.subjects.java.Subject;
import org.jallinone.subjects.server.OrganizationBean;
import org.jallinone.subjects.java.PeopleVO;
import org.jallinone.subjects.server.PeopleBean;
import org.jallinone.subjects.java.OrganizationVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;



/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to insert a new supplier in PUR01/REG04 tables.</p>
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
public class InsertSupplierAction implements Action {


  public InsertSupplierAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "insertSupplier";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    Connection conn = null;
    PreparedStatement pstmt = null;
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
      String companyCode = ((JAIOUserSessionParameters)userSessionPars).getCompanyBa().getCompaniesList("PUR01").get(0).toString();

      DetailSupplierVO vo = (DetailSupplierVO)inputPar;
      if (vo.getCompanyCodeSys01REG04()==null)
        vo.setCompanyCodeSys01REG04(companyCode);
      vo.setSubjectTypeREG04(ApplicationConsts.SUBJECT_SUPPLIER);

      // check if supplier code is defined: if it's not defined then it will be defined as a progressive...
      if (vo.getSupplierCodePUR01()==null || vo.getSupplierCodePUR01().trim().equals("")) {
        vo.setSupplierCodePUR01( String.valueOf(ProgressiveUtils.getConsecutiveProgressive("PUR01_SUPPLIERS",vo.getCompanyCodeSys01REG04(),conn).intValue()) );
      }

      // insert into REG04...
      // test if there exists a supplier with the same supplier code...
      pstmt = conn.prepareStatement("select SUPPLIER_CODE from PUR01_SUPPLIERS where COMPANY_CODE_SYS01=? and SUPPLIER_CODE=?");
      pstmt.setString(1,vo.getCompanyCodeSys01REG04());
      pstmt.setString(2,vo.getSupplierCodePUR01());
      ResultSet rset = pstmt.executeQuery();
      if (rset.next()) {
        rset.close();
        return new ErrorResponse("supplier code already exist.");
      }
      rset.close();

      new OrganizationBean().insert(conn,true,vo,userSessionPars,context);
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
          userSessionPars,
          (ValueObject)vo,
          "PUR01_SUPPLIERS",
          attribute2dbField,
          "Y",
          "N",
          context,
          true,
          ApplicationConsts.ID_SUPPLIER_GRID // window identifier...
      );

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

      conn.commit();

      // fires the GenericEvent.AFTER_COMMIT event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        getRequestName(),
        GenericEvent.AFTER_COMMIT,
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while inserting a new supplier",ex);
      try {
        conn.rollback();
      }
      catch (Exception ex3) {
      }
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        pstmt.close();
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
