package org.jallinone.expirations.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.openswing.swing.server.QueryUtil;
import java.math.BigDecimal;
import org.jallinone.expirations.java.ExpirationVO;
import org.jallinone.accounting.movements.java.*;
import org.jallinone.accounting.movements.server.InsertJournalItemBean;
import org.jallinone.accounting.movements.server.InsertVatRegisterBean;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.internationalization.server.ServerResourcesFactory;
import org.openswing.swing.internationalization.java.Resources;
import org.jallinone.system.server.LoadUserParamAction;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to update existing sale/purchase expirations.</p>
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
public class UpdateExpirationsAction implements Action {

  InsertJournalItemBean insJornalItemAction = new InsertJournalItemBean();
  LoadUserParamAction userParamAction = new LoadUserParamAction();


  public UpdateExpirationsAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "updateExpirations";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    Connection conn = null;
    PreparedStatement pstmt = null;
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


      ArrayList oldVOs = ((ArrayList[])inputPar)[0];
      ArrayList newVOs = ((ArrayList[])inputPar)[1];
      ExpirationVO oldVO = null;
      ExpirationVO newVO = null;
      Response res = null;

      HashSet pkAttrs = new HashSet();
      pkAttrs.add("companyCodeSys01DOC19");
      pkAttrs.add("progressiveDOC19");

      HashMap attribute2dbField = new HashMap();
      attribute2dbField.put("progressiveDOC19","PROGRESSIVE");
      attribute2dbField.put("companyCodeSys01DOC19","COMPANY_CODE_SYS01");
      attribute2dbField.put("docTypeDOC19","DOC_TYPE");
      attribute2dbField.put("docNumberDOC19","DOC_NUMBER");
      attribute2dbField.put("docYearDOC19","DOC_YEAR");
      attribute2dbField.put("docSequenceDOC19","DOC_SEQUENCE");
      attribute2dbField.put("name_1DOC19","NAME_1");
      attribute2dbField.put("name_2DOC19","NAME_2");
      attribute2dbField.put("descriptionDOC19","DESCRIPTION");
      attribute2dbField.put("valueDOC19","VALUE");
      attribute2dbField.put("payedDOC19","PAYED");
      attribute2dbField.put("docDateDOC19","DOC_DATE");
      attribute2dbField.put("expirationDateDOC19","EXPIRATION_DATE");
      attribute2dbField.put("progressiveReg04DOC19","PROGRESSIVE_REG04");
      attribute2dbField.put("customerSupplierCodeDOC19","CUSTOMER_SUPPLIER_CODE");

      JournalHeaderVO jhVO = null;
      // retrieve internationalization settings (Resources object)...
      ServerResourcesFactory factory = (ServerResourcesFactory)context.getAttribute(Controller.RESOURCES_FACTORY);
      Resources resources = factory.getResources(userSessionPars.getLanguageId());

      HashMap map = new HashMap();
      String bankAccountCode = null;

      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (ExpirationVO)oldVOs.get(i);
        newVO = (ExpirationVO)newVOs.get(i);

        res = new QueryUtil().updateTable(
            conn,
            userSessionPars,
            pkAttrs,
            oldVO,
            newVO,
            "DOC19_EXPIRATIONS",
            attribute2dbField,
            "Y",
            "N",
            context,
            true
        );
        if (res.isError()) {
          conn.rollback();
          return res;
        }

        map.put(ApplicationConsts.COMPANY_CODE_SYS01,newVO.getCompanyCodeSys01DOC19());
        map.put(ApplicationConsts.PARAM_CODE,ApplicationConsts.BANK_ACCOUNT);
        res = userParamAction.executeCommand(map,userSessionPars,request,response,userSession,context);
        if (res.isError()) {
          conn.rollback();
          return res;
        }
        bankAccountCode = ((VOResponse)res).getVo().toString();

        // generate an accounting item if the row has been payed...
        if (!oldVO.getPayedDOC19().booleanValue() && newVO.getPayedDOC19().booleanValue()) {

          jhVO = new JournalHeaderVO();
          jhVO.setCompanyCodeSys01ACC05(newVO.getCompanyCodeSys01DOC19());
          String creditDebitAccountCode = null;
          String accountCodeTypeACC06 = null;
          if (newVO.getDocTypeDOC19().equals(ApplicationConsts.SALE_INVOICE_DOC_TYPE) ||
              newVO.getDocTypeDOC19().equals(ApplicationConsts.SALE_INVOICE_FROM_DN_DOC_TYPE) ||
              newVO.getDocTypeDOC19().equals(ApplicationConsts.SALE_INVOICE_FROM_SD_DOC_TYPE) ||
              newVO.getDocTypeDOC19().equals(ApplicationConsts.SALE_CREDIT_NOTE_DOC_TYPE) ||
              newVO.getDocTypeDOC19().equals(ApplicationConsts.SALE_GENERIC_INVOICE)) {
            jhVO.setAccountingMotiveCodeAcc03ACC05(ApplicationConsts.MOTIVE_INVOICE_PROCEEDS);
            jhVO.setDescriptionACC05(
                newVO.getDescriptionDOC19()+" - "+
                resources.getResource("customer")+" "+newVO.getName_1DOC19()+" "+(newVO.getName_2DOC19()==null?"":newVO.getName_2DOC19())
            );

            // determine account codes defined for the current customer...
            pstmt = conn.prepareStatement(
              "select CREDIT_ACCOUNT_CODE_ACC02 from SAL07_CUSTOMERS where COMPANY_CODE_SYS01=? and PROGRESSIVE_REG04=?"
            );
            pstmt.setString(1,newVO.getCompanyCodeSys01DOC19());
            pstmt.setBigDecimal(2,newVO.getProgressiveReg04DOC19());
            ResultSet rset = pstmt.executeQuery();
            if (!rset.next()) {
              rset.close();
              return new ErrorResponse("customer not found");
            }
            creditDebitAccountCode = rset.getString(1);
            rset.close();
            pstmt.close();

            accountCodeTypeACC06 = ApplicationConsts.ACCOUNT_TYPE_CUSTOMER;
          }
          else {
            jhVO.setAccountingMotiveCodeAcc03ACC05(ApplicationConsts.MOTIVE_PURCHASE_INVOICE_PAYED);
            jhVO.setDescriptionACC05(
                newVO.getDescriptionDOC19()+" - "+
                resources.getResource("supplier")+" "+newVO.getName_1DOC19()+" "+(newVO.getName_2DOC19()==null?"":newVO.getName_2DOC19())
            );

            // determine account codes defined for the current supplier...
            pstmt = conn.prepareStatement(
              "select DEBIT_ACCOUNT_CODE_ACC02 from PUR01_SUPPLIERS where COMPANY_CODE_SYS01=? and PROGRESSIVE_REG04=?"
            );
            pstmt.setString(1,newVO.getCompanyCodeSys01DOC19());
            pstmt.setBigDecimal(2,newVO.getProgressiveReg04DOC19());
            ResultSet rset = pstmt.executeQuery();
            if (!rset.next()) {
              rset.close();
              return new ErrorResponse("supplier not found");
            }
            creditDebitAccountCode = rset.getString(1);
            rset.close();
            pstmt.close();

            accountCodeTypeACC06 = ApplicationConsts.ACCOUNT_TYPE_SUPPLIER;
          }
          jhVO.setItemDateACC05(new java.sql.Date(System.currentTimeMillis()));
          jhVO.setItemYearACC05(new BigDecimal(Calendar.getInstance().get(Calendar.YEAR)));

          JournalRowVO jrVO = new JournalRowVO();
          jrVO.setCompanyCodeSys01ACC06(jhVO.getCompanyCodeSys01ACC05());
          jrVO.setAccountCodeAcc02ACC06(creditDebitAccountCode);
          jrVO.setAccountCodeACC06(newVO.getCustomerSupplierCodeDOC19());
          jrVO.setAccountCodeTypeACC06(accountCodeTypeACC06);
          jrVO.setCreditAmountACC06(newVO.getValueDOC19());
          jrVO.setDescriptionACC06("");
          jrVO.setItemYearAcc05ACC06(jhVO.getItemYearACC05());
          jrVO.setProgressiveAcc05ACC06(jhVO.getProgressiveACC05());
          jhVO.addJournalRow(jrVO);

          jrVO = new JournalRowVO();
          jrVO.setCompanyCodeSys01ACC06(jhVO.getCompanyCodeSys01ACC05());
          jrVO.setAccountCodeAcc02ACC06(bankAccountCode);
          jrVO.setAccountCodeACC06(bankAccountCode);
          jrVO.setAccountCodeTypeACC06(ApplicationConsts.ACCOUNT_TYPE_ACCOUNT);
          jrVO.setDebitAmountACC06(newVO.getValueDOC19());
          jrVO.setDescriptionACC06("");
          jrVO.setItemYearAcc05ACC06(jhVO.getItemYearACC05());
          jrVO.setProgressiveAcc05ACC06(jhVO.getProgressiveACC05());
          jhVO.addJournalRow(jrVO);


          res = insJornalItemAction.insertItem(conn,jhVO,userSessionPars,request,response,userSession,context);
          if (res.isError()) {
            conn.rollback();
            return res;
          }

        }

      }

      Response answer = new VOListResponse(newVOs,false,newVOs.size());

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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while updating existing expirations",ex);
      try {
        conn.rollback();
      }
      catch (Exception ex3) {
      }
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        if (pstmt!=null)
          pstmt.close();
      }
      catch (Exception ex1) {
      }
      try {
        ConnectionManager.releaseConnection(conn, context);
      }
      catch (Exception ex1) {
      }
    }

  }



}
