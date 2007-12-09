package org.jallinone.accounting.vatregisters.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.accounting.vatregisters.java.*;
import org.openswing.swing.server.QueryUtil;
import org.jallinone.commons.server.CustomizeQueryUtil;
import java.math.BigDecimal;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.accounting.movements.server.InsertJournalItemBean;
import org.jallinone.accounting.movements.java.JournalHeaderVO;
import org.openswing.swing.internationalization.server.ServerResourcesFactory;
import org.openswing.swing.internationalization.java.Resources;
import org.jallinone.accounting.movements.java.JournalRowVO;
import java.text.SimpleDateFormat;
import org.jallinone.system.server.LoadUserParamAction;
import org.openswing.swing.util.java.Consts;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to endorse vat to tresury, based on vat registers content.</p>
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
public class VatEndorseAction implements Action {

  InsertJournalItemBean insJornalItemAction = new InsertJournalItemBean();
  LoadUserParamAction userParamAction = new LoadUserParamAction();


  public VatEndorseAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "vatEndorse";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    Connection conn = null;
    PreparedStatement pstmt = null;
    PreparedStatement pstmt2 = null;
    try {
      String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();
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

      Map map = (Map)inputPar;
      String companyCode = (String)map.get(ApplicationConsts.COMPANY_CODE_SYS01);
      java.util.Date toDate = (java.util.Date)map.get(ApplicationConsts.DATE_FILTER);

      // retrieve last record number/account code for each vat register...
      String sql = "select REGISTER_CODE,LAST_RECORD_NUMBER,LAST_VAT_DATE,ACCOUNT_CODE_ACC02 from ACC04_VAT_REGISTERS where COMPANY_CODE_SYS01=?";
      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1,companyCode);
      ResultSet rset = pstmt.executeQuery();
      Hashtable lastRecordNumbers = new Hashtable();
      Hashtable lastVatDates = new Hashtable();
      Hashtable accountCodes = new Hashtable();
      String regCode = null;
      BigDecimal lastRN = null;
      java.sql.Date lastVD = null;
      while(rset.next()) {
        regCode = rset.getString(1);
        lastRN = rset.getBigDecimal(2);
        lastVD = rset.getDate(3);
        if (lastRN==null)
          lastRN = new BigDecimal(0);
        lastRecordNumbers.put(regCode,lastRN);
        if (lastVD==null) {
          Calendar cal = Calendar.getInstance();
          cal.set(cal.MONTH,0);
          cal.set(cal.DAY_OF_MONTH,1);
          lastVD = new java.sql.Date(cal.getTimeInMillis());
        }
        lastVatDates.put(regCode,lastVD);
        accountCodes.put(regCode,rset.getString(4));
      }
      rset.close();
      pstmt.close();

      // retrieve vat endorse account code...
      Map params = new HashMap();
      params.put(ApplicationConsts.COMPANY_CODE_SYS01,companyCode);
      params.put(ApplicationConsts.PARAM_CODE,ApplicationConsts.VAT_ENDORSE_ACCOUNT);
      Response res = userParamAction.executeCommand(params,userSessionPars,request,response,userSession,context);
      if (res.isError()) {
        conn.rollback();
        return res;
      }
      String vatEndorseAccountCode = ((VOResponse)res).getVo().toString();

      // retrieve internationalization settings (Resources object)...
      ServerResourcesFactory factory = (ServerResourcesFactory)context.getAttribute(Controller.RESOURCES_FACTORY);
      Resources resources = factory.getResources(userSessionPars.getLanguageId());

      SimpleDateFormat sdf = new SimpleDateFormat(resources.getDateMask(Consts.TYPE_DATE));

      // for each vat register: create an accounting item to endorse vat amount from the register to th tresury...
      Enumeration en = lastRecordNumbers.keys();
      sql =
          "select VAT_VALUE,RECORD_NUMBER from ACC07_VAT_ROWS where COMPANY_CODE_SYS01=? and REGISTER_CODE_ACC04=? and "+
          "RECORD_NUMBER>? and VAT_DATE<=?";
      pstmt = conn.prepareStatement(sql);
      BigDecimal amount = null;
      BigDecimal newLastRN = null;
      JournalHeaderVO jhVO = null;
      JournalRowVO jrVO = null;
      String accountCode = null;

      sql = "update ACC04_VAT_REGISTERS set LAST_RECORD_NUMBER=?,LAST_VAT_DATE=? where COMPANY_CODE_SYS01=? and REGISTER_CODE=? and (LAST_RECORD_NUMBER=? || LAST_RECORD_NUMBER is null)";
      pstmt2 = conn.prepareStatement(sql);

      while(en.hasMoreElements()) {
        // retrieve total vat amount for the specified vat register...
        amount = new BigDecimal(0);
        regCode = en.nextElement().toString();
        lastRN = (BigDecimal)lastRecordNumbers.get(regCode);
        lastVD = (java.sql.Date)lastVatDates.get(regCode);
        accountCode = (String)accountCodes.get(regCode);
        pstmt.setString(1,companyCode);
        pstmt.setString(2,regCode);
        pstmt.setBigDecimal(3,lastRN);
        pstmt.setDate(4,new java.sql.Date(toDate.getTime()));
        rset = pstmt.executeQuery();
        while(rset.next()) {
          amount = amount.add(rset.getBigDecimal(1));
          newLastRN = rset.getBigDecimal(2);
        }
        rset.close();

        if (amount.doubleValue()!=0) {
          lastVD.setTime(lastVD.getTime()+86400*1000);

          // create the accounting item...
          jhVO = new JournalHeaderVO();
          jhVO.setCompanyCodeSys01ACC05(companyCode);
          jhVO.setAccountingMotiveCodeAcc03ACC05(ApplicationConsts.MOTIVE_VAT_ENDORSING);
          jhVO.setDescriptionACC05(
            resources.getResource("endorse vat from register")+" "+
            regCode+" "+
            resources.getResource("period")+" "+sdf.format(lastVD)+" - "+sdf.format(toDate)

          );
          jhVO.setItemDateACC05(new java.sql.Date(System.currentTimeMillis()));
          jhVO.setItemYearACC05(new BigDecimal(Calendar.getInstance().get(Calendar.YEAR)));

          jrVO = new JournalRowVO();
          jrVO.setCompanyCodeSys01ACC06(jhVO.getCompanyCodeSys01ACC05());
          jrVO.setAccountCodeAcc02ACC06(accountCode);
          jrVO.setAccountCodeACC06(accountCode);
          jrVO.setAccountCodeTypeACC06(ApplicationConsts.ACCOUNT_TYPE_ACCOUNT);
          jrVO.setDebitAmountACC06(amount);
          jrVO.setDescriptionACC06("");
          jrVO.setItemYearAcc05ACC06(jhVO.getItemYearACC05());
          jrVO.setProgressiveAcc05ACC06(jhVO.getProgressiveACC05());
          jhVO.addJournalRow(jrVO);

          jrVO = new JournalRowVO();
          jrVO.setCompanyCodeSys01ACC06(jhVO.getCompanyCodeSys01ACC05());
          jrVO.setAccountCodeAcc02ACC06(vatEndorseAccountCode);
          jrVO.setAccountCodeACC06(vatEndorseAccountCode);
          jrVO.setAccountCodeTypeACC06(ApplicationConsts.ACCOUNT_TYPE_ACCOUNT);
          jrVO.setCreditAmountACC06(amount);
          jrVO.setDescriptionACC06("");
          jrVO.setItemYearAcc05ACC06(jhVO.getItemYearACC05());
          jrVO.setProgressiveAcc05ACC06(jhVO.getProgressiveACC05());
          jhVO.addJournalRow(jrVO);

          res = insJornalItemAction.insertItem(conn,jhVO,userSessionPars,request,response,userSession,context);
          if (res.isError()) {
            conn.rollback();
            return res;
          }

          // update last record number in the current vat register...
          pstmt2.setBigDecimal(1,newLastRN);
          pstmt2.setDate(2,new java.sql.Date(toDate.getTime()));
          pstmt2.setString(3,companyCode);
          pstmt2.setString(4,regCode);
          pstmt2.setBigDecimal(5,lastRN);
          int upd = pstmt2.executeUpdate();
          if (upd==0) {
            conn.rollback();
            return new ErrorResponse("Updating not performed: the record was previously updated.");
          }

        }
      }

      Response answer = new VOResponse(Boolean.TRUE);

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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while endorsing vat",ex);
      try {
        conn.rollback();
      }
      catch (Exception ex3) {
      }
      return new ErrorResponse(ex.toString());
    }
    finally {
      try {
        pstmt.close();
      }
      catch (Exception ex2) {
      }
      try {
        pstmt2.close();
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
