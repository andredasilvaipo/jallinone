package org.jallinone.purchases.suppliers.server;

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
import org.jallinone.commons.server.CustomizeQueryUtil;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.subjects.java.Subject;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.subjects.java.SubjectPK;
import org.jallinone.purchases.suppliers.java.DetailSupplierVO;
import org.openswing.swing.message.send.java.LookupValidationParams;
import org.jallinone.accounting.accounts.server.ValidateAccountCodeAction;
import org.jallinone.accounting.accounts.java.AccountVO;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;



/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch a specific supplier from PUR01/REG04 table.</p>
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
public class LoadSupplierAction implements Action {

  private ValidateAccountCodeAction accountAction = new ValidateAccountCodeAction();


  public LoadSupplierAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadSupplier";
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

      SubjectPK pk = (SubjectPK)inputPar;

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
        " from REG04_SUBJECTS,PUR01_SUPPLIERS,SYS10_TRANSLATIONS,REG10_PAYMENTS where "+
        "PUR01_SUPPLIERS.COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 and "+
        "PUR01_SUPPLIERS.PROGRESSIVE_REG04=REG04_SUBJECTS.PROGRESSIVE and "+
        "REG04_SUBJECTS.COMPANY_CODE_SYS01=? and REG04_SUBJECTS.PROGRESSIVE=? and "+
        "PUR01_SUPPLIERS.PAYMENT_CODE_REG10=REG10_PAYMENTS.PAYMENT_CODE and "+
        "REG10_PAYMENTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
        "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
        "PUR01_SUPPLIERS.ENABLED='Y' ";

      ArrayList values = new ArrayList();
      values.add(pk.getCompanyCodeSys01REG04());
      values.add(pk.getProgressiveREG04());
      values.add(serverLanguageId);

      // read from REG04/PUR01 tables...
      Response res = CustomizeQueryUtil.getQuery(
          conn,
          userSessionPars,
          baseSQL,
          values,
          attribute2dbField,
          DetailSupplierVO.class,
          "Y",
          "N",
          context,
          true,
          ApplicationConsts.ID_SUPPLIER_GRID // window identifier...
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
        Response aRes = accountAction.executeCommand(pars,userSessionPars,request,response,userSession,context);
        if (!aRes.isError())
          vo.setDebitAccountDescrPUR01( ((AccountVO)((VOListResponse)aRes).getRows().get(0)).getDescriptionSYS10() );

        pars = new LookupValidationParams(vo.getCostsAccountCodeAcc02PUR01(),map);
        aRes = accountAction.executeCommand(pars,userSessionPars,request,response,userSession,context);
        if (!aRes.isError())
          vo.setCostsAccountDescrPUR01( ((AccountVO)((VOListResponse)aRes).getRows().get(0)).getDescriptionSYS10() );

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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching an existing supplier",ex);
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
