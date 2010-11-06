package org.jallinone.accounting.movements.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.LookupValidationParams;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.accounting.movements.java.JournalHeaderVO;
import org.jallinone.accounting.movements.java.JournalHeaderWithVatVO;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.system.progressives.server.ProgressiveUtils;
import org.openswing.swing.internationalization.server.ServerResourcesFactory;
import org.openswing.swing.internationalization.java.Resources;
import org.jallinone.system.server.LoadUserParamAction;
import org.jallinone.accounting.movements.java.JournalRowVO;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;
import org.jallinone.events.server.EventAction;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to endorse economic accounts to loss/profit economic account and
 * to loss/profit patrimonial account.</p>
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
public class EndorseEAccountsAction implements EventAction {

  private InsertJournalItemBean bean = new InsertJournalItemBean();


  public EndorseEAccountsAction() {
  }


  /**
   * @return request name
   */
  public final String getRequestName() {
    return "endorseEAccounts";
  }


  /**
   * @return input parameter class or data contained on it; value object class or Map of type <par name,par value>
   */
  public Object getInputParClass() {
    HashMap map = new HashMap();
    map.put(ApplicationConsts.COMPANY_CODE_SYS01,String.class);
    map.put(ApplicationConsts.DATE_FILTER,java.sql.Date.class);
    map.put(ApplicationConsts.LOSSPROFIT_E_ACCOUNT,String.class);
    map.put(ApplicationConsts.LOSSPROFIT_P_ACCOUNT,String.class);
    return map;
  }


  /**
   * @return value object class contained in the Response object
   */
  public Class getValueObjectClass() {
    return JournalHeaderVO.class;
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

      // retrieve internationalization settings (Resources object)...
      ServerResourcesFactory factory = (ServerResourcesFactory)context.getAttribute(Controller.RESOURCES_FACTORY);
      Resources resources = factory.getResources(userSessionPars.getLanguageId());

      // fetch data from client request...
      Map map = (Map)inputPar;
      String companyCode = (String)map.get(ApplicationConsts.COMPANY_CODE_SYS01);
      java.sql.Date endDate = new java.sql.Date(((java.util.Date)map.get(ApplicationConsts.DATE_FILTER)).getTime());
      String lossProfitEAccountCode = (String)map.get(ApplicationConsts.LOSSPROFIT_E_ACCOUNT);
      String lossProfitPAccountCode = (String)map.get(ApplicationConsts.LOSSPROFIT_P_ACCOUNT);

      pstmt = conn.prepareStatement(
        "select sum(ACC06_JOURNAL_ROWS.DEBIT_AMOUNT),sum(ACC06_JOURNAL_ROWS.CREDIT_AMOUNT),ACC06_JOURNAL_ROWS.ACCOUNT_CODE_ACC02 "+
        "from ACC06_JOURNAL_ROWS,ACC05_JOURNAL_HEADER,ACC02_ACCOUNTS,ACC01_LEDGER where "+
        "ACC06_JOURNAL_ROWS.COMPANY_CODE_SYS01=ACC05_JOURNAL_HEADER.COMPANY_CODE_SYS01 and "+
        "ACC06_JOURNAL_ROWS.ITEM_YEAR_ACC05=ACC05_JOURNAL_HEADER.ITEM_YEAR and "+
        "ACC06_JOURNAL_ROWS.PROGRESSIVE_ACC05=ACC05_JOURNAL_HEADER.PROGRESSIVE and "+
        "ACC06_JOURNAL_ROWS.COMPANY_CODE_SYS01=? and "+
        "ACC05_JOURNAL_HEADER.ITEM_DATE>=? and ACC05_JOURNAL_HEADER.ITEM_DATE<=? and "+
        "ACC06_JOURNAL_ROWS.COMPANY_CODE_SYS01=ACC02_ACCOUNTS.COMPANY_CODE_SYS01 and "+
        "ACC06_JOURNAL_ROWS.ACCOUNT_CODE_ACC02=ACC02_ACCOUNTS.ACCOUNT_CODE and "+
        "ACC02_ACCOUNTS.COMPANY_CODE_SYS01=ACC01_LEDGER.COMPANY_CODE_SYS01 and "+
        "ACC02_ACCOUNTS.LEDGER_CODE_ACC01=ACC01_LEDGER.LEDGER_CODE and "+
        "ACC01_LEDGER.ACCOUNT_TYPE=? and "+
        "ACC02_ACCOUNTS.ACCOUNT_TYPE=? and "+
        "NOT ACC06_JOURNAL_ROWS.ACCOUNT_CODE_ACC02=? "+
        "group by ACC06_JOURNAL_ROWS.ACCOUNT_CODE_ACC02"
      );
      Calendar cal = Calendar.getInstance();
      cal.set(cal.DAY_OF_MONTH,1);
      cal.set(cal.MONTH,0);
      cal.set(cal.YEAR,endDate.getYear()+1900);
      java.sql.Date startDate = new java.sql.Date(cal.getTimeInMillis());

      // retrieve settlement amounts for each economic account (except for lossProfitEAccountCode) that is a cost...
      pstmt.setString(1,companyCode);
      pstmt.setDate(2,startDate);
      pstmt.setDate(3,endDate);
      pstmt.setString(4,ApplicationConsts.ECONOMIC_ACCOUNT);
      pstmt.setString(5,ApplicationConsts.DEBIT_ACCOUNT);
      pstmt.setString(6,lossProfitEAccountCode);
      ResultSet rset = pstmt.executeQuery();

      // prepare the accounting item...
      JournalHeaderVO jhVO = new JournalHeaderVO();
      jhVO.setCompanyCodeSys01ACC05(companyCode);
      jhVO.setDescriptionACC05(resources.getResource("endorse loss/profit to econ. account"));
      jhVO.setAccountingMotiveCodeAcc03ACC05(ApplicationConsts.MOTIVE_LOSS_PROFIT_ENDORSING);
      jhVO.setItemDateACC05(endDate);
      jhVO.setItemYearACC05(new BigDecimal(endDate.getYear()+1900));

      // for each account code: create a row in the accounting item...
      JournalRowVO jrVO = null;
      BigDecimal totalSettlement = new BigDecimal(0);
      BigDecimal total = null;
      BigDecimal settlement1 = null;
      BigDecimal settlement2 = null;
      BigDecimal settlement = null;
      String accountCode = null;
      boolean rowsFound = false;
      while(rset.next()) {
        settlement1 = rset.getBigDecimal(1);
        if (settlement1==null)
          settlement1 = new BigDecimal(0);
        settlement2 = rset.getBigDecimal(2);
        if (settlement2==null)
          settlement2 = new BigDecimal(0);
        settlement = settlement1.subtract(settlement2);
        accountCode = rset.getString(3);

        jrVO = new JournalRowVO();
        jrVO.setCompanyCodeSys01ACC06(jhVO.getCompanyCodeSys01ACC05());
        jrVO.setAccountCodeAcc02ACC06(accountCode);
        jrVO.setAccountCodeACC06(accountCode);
        jrVO.setAccountCodeTypeACC06(ApplicationConsts.ACCOUNT_TYPE_ACCOUNT);
        jrVO.setCreditAmountACC06(settlement);
        jrVO.setDescriptionACC06("");
        jrVO.setItemYearAcc05ACC06(jhVO.getItemYearACC05());
        jrVO.setProgressiveAcc05ACC06(jhVO.getProgressiveACC05());
        jhVO.addJournalRow(jrVO);

        totalSettlement = totalSettlement.add(settlement);
        rowsFound = true;
      }
      rset.close();

      // add new row for the total settlement...
      jrVO = new JournalRowVO();
      jrVO.setCompanyCodeSys01ACC06(jhVO.getCompanyCodeSys01ACC05());
      jrVO.setAccountCodeAcc02ACC06(lossProfitEAccountCode);
      jrVO.setAccountCodeACC06(lossProfitEAccountCode);
      jrVO.setAccountCodeTypeACC06(ApplicationConsts.ACCOUNT_TYPE_ACCOUNT);
      jrVO.setDebitAmountACC06(totalSettlement);
      jrVO.setDescriptionACC06("");
      jrVO.setItemYearAcc05ACC06(jhVO.getItemYearACC05());
      jrVO.setProgressiveAcc05ACC06(jhVO.getProgressiveACC05());
      jhVO.addJournalRow(jrVO);
      total = totalSettlement;

      // insert the accounting item...
      Response res = bean.insertItem(conn,jhVO,userSessionPars,request,response,userSession,context);
      if (res.isError()) {
        conn.rollback();
        return res;
      }


      // retrieve settlement amounts for each economic account (except for lossProfitEAccountCode) that is a proceeds...
      pstmt.setString(1,companyCode);
      pstmt.setDate(2,startDate);
      pstmt.setDate(3,endDate);
      pstmt.setString(4,ApplicationConsts.ECONOMIC_ACCOUNT);
      pstmt.setString(5,ApplicationConsts.CREDIT_ACCOUNT);
      pstmt.setString(6,lossProfitEAccountCode);
      rset = pstmt.executeQuery();

      // prepare the accounting item...
      jhVO = new JournalHeaderVO();
      jhVO.setCompanyCodeSys01ACC05(companyCode);
      jhVO.setDescriptionACC05(resources.getResource("endorse loss/profit to econ. account"));
      jhVO.setAccountingMotiveCodeAcc03ACC05(ApplicationConsts.MOTIVE_LOSS_PROFIT_ENDORSING);
      jhVO.setItemDateACC05(endDate);
      jhVO.setItemYearACC05(new BigDecimal(endDate.getYear()+1900));

      // for each account code: create a row in the accounting item...
      totalSettlement = new BigDecimal(0);
      while(rset.next()) {
        settlement1 = rset.getBigDecimal(1);
        if (settlement1==null)
          settlement1 = new BigDecimal(0);
        settlement2 = rset.getBigDecimal(2);
        if (settlement2==null)
          settlement2 = new BigDecimal(0);
        settlement = settlement1.subtract(settlement2);

        settlement = settlement.negate();
        accountCode = rset.getString(3);

        jrVO = new JournalRowVO();
        jrVO.setCompanyCodeSys01ACC06(jhVO.getCompanyCodeSys01ACC05());
        jrVO.setAccountCodeAcc02ACC06(accountCode);
        jrVO.setAccountCodeACC06(accountCode);
        jrVO.setAccountCodeTypeACC06(ApplicationConsts.ACCOUNT_TYPE_ACCOUNT);
        jrVO.setDebitAmountACC06(settlement);
        jrVO.setDescriptionACC06("");
        jrVO.setItemYearAcc05ACC06(jhVO.getItemYearACC05());
        jrVO.setProgressiveAcc05ACC06(jhVO.getProgressiveACC05());
        jhVO.addJournalRow(jrVO);

        totalSettlement = totalSettlement.add(settlement);
        rowsFound = true;
      }
      rset.close();

      // add new row for the total settlement...
      jrVO = new JournalRowVO();
      jrVO.setCompanyCodeSys01ACC06(jhVO.getCompanyCodeSys01ACC05());
      jrVO.setAccountCodeAcc02ACC06(lossProfitEAccountCode);
      jrVO.setAccountCodeACC06(lossProfitEAccountCode);
      jrVO.setAccountCodeTypeACC06(ApplicationConsts.ACCOUNT_TYPE_ACCOUNT);
      jrVO.setCreditAmountACC06(totalSettlement);
      jrVO.setDescriptionACC06("");
      jrVO.setItemYearAcc05ACC06(jhVO.getItemYearACC05());
      jrVO.setProgressiveAcc05ACC06(jhVO.getProgressiveACC05());
      jhVO.addJournalRow(jrVO);
      total = total.subtract(totalSettlement); // loss-profit...

      // insert the accounting item...
      res = bean.insertItem(conn,jhVO,userSessionPars,request,response,userSession,context);
      if (res.isError()) {
        conn.rollback();
        return res;
      }

      pstmt.close();


      if (!rowsFound) {
        conn.rollback();
        return new ErrorResponse("no loss/profit found in the economic account");
      }


      // prepare the accounting item...
      jhVO = new JournalHeaderVO();
      jhVO.setCompanyCodeSys01ACC05(companyCode);
      if (total.doubleValue()<0) {
        // profit...
        jhVO.setDescriptionACC05(resources.getResource("endorse profit to patrimonial account"));
        jhVO.setAccountingMotiveCodeAcc03ACC05(ApplicationConsts.MOTIVE_PROFIT_ENDORSING);
      }
      else {
        // loss...
        jhVO.setDescriptionACC05(resources.getResource("endorse loss to patrimonial account"));
        jhVO.setAccountingMotiveCodeAcc03ACC05(ApplicationConsts.MOTIVE_LOSS_ENDORSING);
      }
      jhVO.setItemDateACC05(endDate);
      jhVO.setItemYearACC05(new BigDecimal(endDate.getYear()+1900));

      jrVO = new JournalRowVO();
      jrVO.setCompanyCodeSys01ACC06(jhVO.getCompanyCodeSys01ACC05());
      jrVO.setAccountCodeAcc02ACC06(lossProfitEAccountCode);
      jrVO.setAccountCodeACC06(lossProfitEAccountCode);
      jrVO.setAccountCodeTypeACC06(ApplicationConsts.ACCOUNT_TYPE_ACCOUNT);
      if (total.doubleValue()<0) {
        // profit...
        jrVO.setDebitAmountACC06(total.negate());
      }
      else {
        // loss...
        jrVO.setCreditAmountACC06(total);
      }
      jrVO.setDescriptionACC06("");
      jrVO.setItemYearAcc05ACC06(jhVO.getItemYearACC05());
      jrVO.setProgressiveAcc05ACC06(jhVO.getProgressiveACC05());
      jhVO.addJournalRow(jrVO);

      jrVO = new JournalRowVO();
      jrVO.setCompanyCodeSys01ACC06(jhVO.getCompanyCodeSys01ACC05());
      jrVO.setAccountCodeAcc02ACC06(lossProfitPAccountCode);
      jrVO.setAccountCodeACC06(lossProfitPAccountCode);
      jrVO.setAccountCodeTypeACC06(ApplicationConsts.ACCOUNT_TYPE_ACCOUNT);
      if (total.doubleValue()<0) {
        // profit...
        jrVO.setCreditAmountACC06(total.negate());
      }
      else {
        // loss...
        jrVO.setDebitAmountACC06(total);
      }
      jrVO.setDescriptionACC06("");
      jrVO.setItemYearAcc05ACC06(jhVO.getItemYearACC05());
      jrVO.setProgressiveAcc05ACC06(jhVO.getProgressiveACC05());
      jhVO.addJournalRow(jrVO);

      // insert the accounting item...
      res = bean.insertItem(conn,jhVO,userSessionPars,request,response,userSession,context);
      if (res.isError()) {
        conn.rollback();
        return res;
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while endorsing economic accounts",ex);
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
