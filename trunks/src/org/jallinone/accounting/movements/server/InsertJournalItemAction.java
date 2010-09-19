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
import org.jallinone.registers.payments.server.ValidatePaymentCodeAction;
import org.jallinone.registers.payments.server.LoadPaymentInstalmentsAction;
import org.jallinone.registers.payments.java.PaymentVO;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.registers.payments.java.PaymentInstalmentVO;
import org.jallinone.system.progressives.server.ProgressiveUtils;
import org.openswing.swing.internationalization.server.ServerResourcesFactory;
import org.openswing.swing.internationalization.java.Resources;
import org.jallinone.system.server.LoadUserParamAction;
import org.jallinone.accounting.movements.java.JournalRowVO;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;
import org.jallinone.events.server.EventAction;
import org.jallinone.system.progressives.server.CompanyProgressiveUtils;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to insert a new item in the journal (ACC05 and ACC06 tables).</p>
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
public class InsertJournalItemAction implements EventAction {

  private InsertJournalItemBean bean = new InsertJournalItemBean();
  private InsertVatRegisterBean vatRegisterAction =  new InsertVatRegisterBean();
  private ValidatePaymentCodeAction payAction = new ValidatePaymentCodeAction();
  private LoadPaymentInstalmentsAction paysAction = new LoadPaymentInstalmentsAction();
  private LoadUserParamAction userParamAction = new LoadUserParamAction();


  public InsertJournalItemAction() {
  }


  /**
   * @return request name
   */
  public final String getRequestName() {
    return "insertJournalItem";
  }


  /**
   * @return input parameter class or data contained on it; value object class or Map of type <par name,par value>
   */
  public Object getInputParClass() {
    return JournalHeaderVO.class;
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

      Response responseVO = bean.insertItem(conn,(JournalHeaderVO)inputPar,userSessionPars,request,response,userSession,context);
      if (responseVO.isError()) {
        conn.rollback();
        return responseVO;
      }

      if (inputPar instanceof JournalHeaderWithVatVO) {
        JournalHeaderWithVatVO vo = (JournalHeaderWithVatVO)inputPar;

        // insert vat rows in the specified vat register...
        Response regRes = vatRegisterAction.insertVatRows(conn,vo.getVats(),userSessionPars,request,response,userSession,context);
        if (regRes.isError()) {
          conn.rollback();
          return regRes;
        }

        // retrieve payment instalments...
        Response payRes = payAction.executeCommand(new LookupValidationParams(vo.getPaymentCodeREG10(),new HashMap()),userSessionPars,request,response,userSession,context);
        if (payRes.isError()) {
          conn.rollback();
          return payRes;
        }
        PaymentVO payVO = (PaymentVO)((VOListResponse)payRes).getRows().get(0);
        GridParams gridParams = new GridParams();
        gridParams.getOtherGridParams().put(ApplicationConsts.PAYMENT_CODE_REG10,vo.getPaymentCodeREG10());
        payRes = paysAction.executeCommand(gridParams,userSessionPars,request,response,userSession,context);
        if (payRes.isError()) {
          conn.rollback();
          return payRes;
        }
        java.util.List rows = ((VOListResponse)payRes).getRows();

        // create expirations in DOC19 ONLY if:
        // - there are more than one instalment OR
        // - there is only one instalment and this instalment has more than 0 instalment days
        if (rows.size()>1 || (rows.size()==1 && ((PaymentInstalmentVO)rows.get(0)).getInstalmentDaysREG17().intValue()>0 )) {

          // retrieve internationalization settings (Resources object)...
          ServerResourcesFactory factory = (ServerResourcesFactory)context.getAttribute(Controller.RESOURCES_FACTORY);
          Resources resources = factory.getResources(userSessionPars.getLanguageId());

          PaymentInstalmentVO inVO = null;
          pstmt = conn.prepareStatement(
            "insert into DOC19_EXPIRATIONS(COMPANY_CODE_SYS01,DOC_TYPE,DOC_YEAR,DOC_NUMBER,DOC_SEQUENCE,PROGRESSIVE,DOC_DATE,EXPIRATION_DATE,NAME_1,NAME_2,VALUE,PAYED,DESCRIPTION,CUSTOMER_SUPPLIER_CODE,PROGRESSIVE_REG04,CURRENCY_CODE_REG03) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
          );
          long startTime = vo.getItemDateACC05().getTime(); // item date...
          if (payVO.getStartDayREG10().equals(ApplicationConsts.START_DAY_END_MONTH)) {
            Calendar cal = Calendar.getInstance();
            if (cal.get(cal.MONTH)==10 || cal.get(cal.MONTH)==3 || cal.get(cal.MONTH)==5 || cal.get(cal.MONTH)==8)
              cal.set(cal.DAY_OF_MONTH,30);
            else if (cal.get(cal.MONTH)==1) {
              if (cal.get(cal.YEAR)%4==0)
                cal.set(cal.DAY_OF_MONTH,29);
              else
                cal.set(cal.DAY_OF_MONTH,28);
            } else
              cal.set(cal.DAY_OF_MONTH,31);
            startTime = cal.getTime().getTime();
          }
          BigDecimal amount = null;

          for(int i=0;i<rows.size();i++) {
            inVO = (PaymentInstalmentVO)rows.get(i);
            pstmt.setString(1,vo.getCompanyCodeSys01ACC05());
            pstmt.setString(2,vo.getDocTypeDOC19());
            pstmt.setBigDecimal(3,vo.getItemYearACC05());
            pstmt.setBigDecimal(4,null);
            pstmt.setBigDecimal(5,vo.getDocSequenceDOC19());
            pstmt.setBigDecimal(6,CompanyProgressiveUtils.getInternalProgressive(vo.getCompanyCodeSys01ACC05(),"DOC19_EXPIRATIONS","PROGRESSIVE",conn));
            pstmt.setDate(7,vo.getItemDateACC05());
            pstmt.setDate(8,new java.sql.Date(startTime + inVO.getInstalmentDaysREG17().longValue()*86400*1000)); // expiration date
            pstmt.setString(9,vo.getName_1REG04());
            pstmt.setString(10,vo.getName_2REG04());
            amount = vo.getTotalValue().multiply(inVO.getPercentageREG17()).divide(new BigDecimal(100),BigDecimal.ROUND_HALF_UP).setScale(vo.getTotalValue().scale(),BigDecimal.ROUND_HALF_UP); // value

            pstmt.setBigDecimal(11,amount);
            pstmt.setString(12,"N");

            if (vo.getDocTypeDOC19().equals(ApplicationConsts.SALE_GENERIC_INVOICE))
              pstmt.setString(13,
                resources.getResource("sale generic document")+" "+
                vo.getDocSequenceDOC19()+"/"+vo.getItemYearACC05()+" - "+
                resources.getResource("valueREG01")+" "+
                resources.getResource("rateNumberREG17")+" "+
                (i+1)+" - "+inVO.getPaymentTypeDescriptionSYS10()
              ); // description
            else
              pstmt.setString(13,
                resources.getResource("purchase generic document")+" "+
                vo.getDocSequenceDOC19()+"/"+vo.getItemYearACC05()+" - "+
                resources.getResource("valueREG01")+" "+
                resources.getResource("rateNumberREG17")+" "+
                (i+1)+" - "+inVO.getPaymentTypeDescriptionSYS10()
              ); // description
            pstmt.setString(14,vo.getCustomerCodeSAL07());
            pstmt.setBigDecimal(15,vo.getProgressiveREG04());
            pstmt.setString(16,vo.getCurrencyCodeREG01());
            pstmt.execute();
          }
          pstmt.close();
        }







        // create an item registration for proceeds, according to expiration settings (e.g. retail selling):
        // there must be only one instalment and this instalment has 0 instalment days
        if (rows.size()==1 && ((PaymentInstalmentVO)rows.get(0)).getInstalmentDaysREG17().intValue()==0) {

          // retrieve internationalization settings (Resources object)...
          ServerResourcesFactory factory = (ServerResourcesFactory)context.getAttribute(Controller.RESOURCES_FACTORY);
          Resources resources = factory.getResources(userSessionPars.getLanguageId());

          HashMap map = new HashMap();
          map.put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01ACC05());
          map.put(ApplicationConsts.PARAM_CODE,ApplicationConsts.CASE_ACCOUNT);
          Response res = userParamAction.executeCommand(map,userSessionPars,request,response,userSession,context);
          if (res.isError()) {
            conn.rollback();
            return res;
          }
          String caseAccountCode = ((VOResponse)res).getVo().toString();

          JournalHeaderVO jhVO = new JournalHeaderVO();
          jhVO.setCompanyCodeSys01ACC05(vo.getCompanyCodeSys01ACC05());
          if (vo.getDocTypeDOC19().equals(ApplicationConsts.SALE_GENERIC_INVOICE)) {
            jhVO.setDescriptionACC05(
                resources.getResource("sale generic document")+" "+vo.getDocSequenceDOC19()+"/"+vo.getItemYearACC05()+" - "+
                resources.getResource("customer")+" "+vo.getName_1REG04()+" "+(vo.getName_2REG04()==null?"":vo.getName_2REG04())
            );
            jhVO.setAccountingMotiveCodeAcc03ACC05(ApplicationConsts.MOTIVE_INVOICE_PROCEEDS);
          }
          else {
            jhVO.setDescriptionACC05(
                resources.getResource("purchase generic document")+" "+vo.getDocSequenceDOC19()+"/"+vo.getItemYearACC05()+" - "+
                resources.getResource("supplier")+" "+vo.getName_1REG04()+" "+(vo.getName_2REG04()==null?"":vo.getName_2REG04())
            );
            jhVO.setAccountingMotiveCodeAcc03ACC05(ApplicationConsts.MOTIVE_PURCHASE_INVOICE_PAYED);
          }

          jhVO.setItemDateACC05(new java.sql.Date(System.currentTimeMillis()));
          jhVO.setItemYearACC05(new BigDecimal(Calendar.getInstance().get(Calendar.YEAR)));

          JournalRowVO jrVO = new JournalRowVO();
          jrVO.setCompanyCodeSys01ACC06(jhVO.getCompanyCodeSys01ACC05());
          if (vo.getDocTypeDOC19().equals(ApplicationConsts.SALE_GENERIC_INVOICE)) {
            jrVO.setAccountCodeAcc02ACC06(vo.getCreditAccountCodeAcc02SAL07());
            jrVO.setAccountCodeACC06(vo.getCustomerCodeSAL07());
            jrVO.setAccountCodeTypeACC06(ApplicationConsts.ACCOUNT_TYPE_CUSTOMER);
            jrVO.setCreditAmountACC06(vo.getTotalValue());
          }
          else {
            jrVO.setAccountCodeAcc02ACC06(vo.getDebitAccountCodeAcc02PUR01());
            jrVO.setAccountCodeACC06(vo.getSupplierCodePUR01());
            jrVO.setAccountCodeTypeACC06(ApplicationConsts.ACCOUNT_TYPE_SUPPLIER);
            jrVO.setDebitAmountACC06(vo.getTotalValue());
          }
          jrVO.setDescriptionACC06("");
          jrVO.setItemYearAcc05ACC06(jhVO.getItemYearACC05());
          jrVO.setProgressiveAcc05ACC06(jhVO.getProgressiveACC05());
          jhVO.addJournalRow(jrVO);

          jrVO = new JournalRowVO();
          jrVO.setCompanyCodeSys01ACC06(jhVO.getCompanyCodeSys01ACC05());
          jrVO.setAccountCodeAcc02ACC06(caseAccountCode);
          jrVO.setAccountCodeACC06(caseAccountCode);
          jrVO.setAccountCodeTypeACC06(ApplicationConsts.ACCOUNT_TYPE_ACCOUNT);
          if (vo.getDocTypeDOC19().equals(ApplicationConsts.SALE_GENERIC_INVOICE)) {
            jrVO.setDebitAmountACC06(vo.getTotalValue());
          }
          else {
            jrVO.setCreditAmountACC06(vo.getTotalValue());
          }
          jrVO.setDescriptionACC06("");
          jrVO.setItemYearAcc05ACC06(jhVO.getItemYearACC05());
          jrVO.setProgressiveAcc05ACC06(jhVO.getProgressiveACC05());
          jhVO.addJournalRow(jrVO);
          Response proceedsRes = bean.insertItem(conn,jhVO,userSessionPars,request,response,userSession,context);
          if (proceedsRes.isError()) {
            conn.rollback();
            return proceedsRes;
          }
        }

      }

      Response answer = responseVO;

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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while inserting a new item in the journal",ex);
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
