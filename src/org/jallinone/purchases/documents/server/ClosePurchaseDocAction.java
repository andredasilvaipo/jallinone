package org.jallinone.purchases.documents.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.purchases.documents.java.DetailPurchaseDocRowVO;
import org.jallinone.system.server.JAIOUserSessionParameters;
import java.math.BigDecimal;
import org.jallinone.system.progressives.server.ProgressiveUtils;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.system.java.ApplicationParametersVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.message.send.java.LookupValidationParams;
import org.jallinone.purchases.documents.java.PurchaseDocPK;
import org.jallinone.purchases.documents.java.DetailPurchaseDocVO;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.jallinone.purchases.documents.java.GridPurchaseDocRowVO;
import org.jallinone.warehouse.availability.server.LoadBookedItemsAction;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.items.java.ItemPK;
import org.jallinone.warehouse.availability.java.BookedItemQtyVO;
import org.openswing.swing.internationalization.server.ServerResourcesFactory;
import org.openswing.swing.internationalization.java.Resources;
import org.jallinone.warehouse.movements.java.WarehouseMovementVO;
import org.jallinone.warehouse.movements.server.AddMovementBean;
import org.jallinone.purchases.documents.java.PurchaseDocRowPK;
import org.jallinone.registers.payments.server.ValidatePaymentCodeAction;
import org.jallinone.registers.payments.server.LoadPaymentInstalmentsAction;
import org.jallinone.registers.payments.java.PaymentVO;
import org.jallinone.registers.payments.java.PaymentInstalmentVO;
import org.jallinone.accounting.movements.java.*;
import org.jallinone.accounting.movements.server.InsertJournalItemBean;
import org.jallinone.accounting.movements.java.TaxableIncomeVO;
import org.jallinone.accounting.movements.server.InsertVatRegisterBean;
import org.jallinone.registers.currency.server.CurrencyConversionUtils;
import org.jallinone.system.server.LoadUserParamAction;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to close a purchase document, i.e.
 * - check if all items are available (for retail selling only)
 * - load all items into the specified warehouse (for retail selling only)
 * - change doc state to close
 * - calculate document sequence
 * Requirements:
 * - position must be defined for each item row
 * </p>
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
public class ClosePurchaseDocAction implements Action {

  LoadPurchaseDocRowsAction rowsAction = new LoadPurchaseDocRowsAction();
  LoadPurchaseDocAction docAction = new LoadPurchaseDocAction();
  AddMovementBean movBean = new AddMovementBean();
  LoadPurchaseDocRowAction rowAction = new LoadPurchaseDocRowAction();
  ValidatePaymentCodeAction payAction = new ValidatePaymentCodeAction();
  LoadPaymentInstalmentsAction paysAction = new LoadPaymentInstalmentsAction();
  InsertJournalItemBean insJornalItemAction = new InsertJournalItemBean();
  PurchaseDocTaxableIncomesBean taxableIncomeAction = new PurchaseDocTaxableIncomesBean();
  InsertVatRegisterBean vatRegisterAction =  new InsertVatRegisterBean();
  LoadUserParamAction userParamAction = new LoadUserParamAction();


  public ClosePurchaseDocAction() {}

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "closePurchaseDoc";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();
    Connection conn = null;
    PreparedStatement pstmt = null;
    PreparedStatement pstmt2 = null;
    ResultSet rset = null;
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
      PurchaseDocPK pk = (PurchaseDocPK)inputPar;

      // retrieve internationalization settings (Resources object)...
      ServerResourcesFactory factory = (ServerResourcesFactory)context.getAttribute(Controller.RESOURCES_FACTORY);
      Resources resources = factory.getResources(userSessionPars.getLanguageId());

      // retrieve document header...
      Response res = docAction.executeCommand(pk,userSessionPars,request,response,userSession,context);
      if (res.isError())
        return res;
      DetailPurchaseDocVO docVO = (DetailPurchaseDocVO)((VOResponse)res).getVo();

      // retrieve company currency code and currency conversion factor...
      String companyCurrencyCode = CurrencyConversionUtils.getCompanyCurrencyCode(docVO.getCompanyCodeSys01DOC06(),conn);
      BigDecimal conv = CurrencyConversionUtils.getConversionFactor(docVO.getCurrencyCodeReg03DOC06(),companyCurrencyCode,conn);

      // retrieve document item rows...
      GridParams gridParams = new GridParams();
      gridParams.getOtherGridParams().put(ApplicationConsts.PURCHASE_DOC_PK,pk);
      res = rowsAction.executeCommand(gridParams,userSessionPars,request,response,userSession,context);
      if (res.isError())
        return res;
      ArrayList rows = ((VOListResponse)res).getRows();

      // check if this document is a purchase invoice and has a linked purchase order document:
      // if this is the case, then the linked document will be updated...
      if ((docVO.getDocTypeDOC06().equals(ApplicationConsts.PURCHASE_INVOICE_FROM_DN_DOC_TYPE) ||
           docVO.getDocTypeDOC06().equals(ApplicationConsts.PURCHASE_INVOICE_FROM_PD_DOC_TYPE)) &&
        docVO.getDocNumberDoc06DOC06()!=null
      ) {
        PurchaseDocPK refPK = new PurchaseDocPK(
          docVO.getCompanyCodeSys01Doc06DOC06(),
          docVO.getDocTypeDoc06DOC06(),
          docVO.getDocYearDoc06DOC06(),
          docVO.getDocNumberDoc06DOC06()
        );

        // retrieve ref. document item rows...
        GridPurchaseDocRowVO vo = null;
        DetailPurchaseDocRowVO refDetailVO = null;
        BigDecimal qty = null;
        BigDecimal invoiceQty = null;
        String docType = null;
        BigDecimal docYear = null;
        BigDecimal docNumber = null;
        BigDecimal rowNumber = null;
        for(int i=0;i<rows.size();i++) {
          vo = (GridPurchaseDocRowVO)rows.get(i);
          res = rowAction.executeCommand(
              new PurchaseDocRowPK(
                docVO.getCompanyCodeSys01Doc06DOC06(),
                docVO.getDocTypeDoc06DOC06(),
                docVO.getDocYearDoc06DOC06(),
                docVO.getDocNumberDoc06DOC06(),
                vo.getItemCodeItm01DOC07()
              ),
              userSessionPars,
              request,
              response,
              userSession,
              context
          );
          if (res.isError()) {
            conn.rollback();
            return res;
          }
          refDetailVO = (DetailPurchaseDocRowVO)((VOResponse)res).getVo();
          refDetailVO.setInvoiceQtyDOC07(
              refDetailVO.getInvoiceQtyDOC07().add(vo.getQtyDOC07()).setScale(docVO.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP)
          );
          if (refDetailVO.getInvoiceQtyDOC07().doubleValue()>refDetailVO.getQtyDOC07().doubleValue())
            refDetailVO.setInvoiceQtyDOC07( refDetailVO.getQtyDOC07() );

            // update ref. item row...
          pstmt = conn.prepareStatement(
            "update DOC07_PURCHASE_ITEMS set INVOICE_QTY=? where "+
            "COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? and ITEM_CODE_ITM01=?"
          );
          pstmt.setBigDecimal(1,refDetailVO.getInvoiceQtyDOC07());
          pstmt.setString(2,refDetailVO.getCompanyCodeSys01DOC07());
          pstmt.setString(3,refDetailVO.getDocTypeDOC07());
          pstmt.setBigDecimal(4,refDetailVO.getDocYearDOC07());
          pstmt.setBigDecimal(5,refDetailVO.getDocNumberDOC07());
          pstmt.setString(6,refDetailVO.getItemCodeItm01DOC07());
          pstmt.execute();
          pstmt.close();

          // update ref. item row in the in delivery note...
          pstmt2 = conn.prepareStatement(
            "select QTY,INVOICE_QTY,DOC_TYPE,DOC_YEAR,DOC_NUMBER,ROW_NUMBER from DOC09_IN_DELIVERY_NOTE_ITEMS where "+
            "COMPANY_CODE_SYS01=? and DOC_TYPE_DOC06=? and DOC_YEAR_DOC06=? and DOC_NUMBER_DOC06=? and ITEM_CODE_ITM01=? and INVOICE_QTY<QTY"
          );
          qty = null;
          invoiceQty = null;

          pstmt2.setString(1,refDetailVO.getCompanyCodeSys01DOC07());
          pstmt2.setString(2,refDetailVO.getDocTypeDOC07());
          pstmt2.setBigDecimal(3,refDetailVO.getDocYearDOC07());
          pstmt2.setBigDecimal(4,refDetailVO.getDocNumberDOC07());
          pstmt2.setString(5,refDetailVO.getItemCodeItm01DOC07());
          rset = pstmt2.executeQuery();

          // it only updates one row, that matches the where clause...
          if(rset.next()) {
            qty = rset.getBigDecimal(1);
            invoiceQty = rset.getBigDecimal(2);
            docType = rset.getString(3);
            docYear = rset.getBigDecimal(4);
            docNumber = rset.getBigDecimal(5);
            rowNumber = rset.getBigDecimal(6);
          }
          rset.close();
          pstmt2.close();

          if (qty!=null && invoiceQty!=null) {
            if (invoiceQty.doubleValue()+vo.getQtyDOC07().doubleValue()<=qty.doubleValue())
              qty = invoiceQty.add(vo.getQtyDOC07());

            pstmt = conn.prepareStatement(
              "update DOC09_IN_DELIVERY_NOTE_ITEMS set INVOICE_QTY=? where "+
              "COMPANY_CODE_SYS01=? and DOC_TYPE_DOC06=? and DOC_YEAR_DOC06=? and DOC_NUMBER_DOC06=? and ITEM_CODE_ITM01=? and "+
              "DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? and ROW_NUMBER=?"
            );
            pstmt.setBigDecimal(1,qty);
            pstmt.setString(2,refDetailVO.getCompanyCodeSys01DOC07());
            pstmt.setString(3,refDetailVO.getDocTypeDOC07());
            pstmt.setBigDecimal(4,refDetailVO.getDocYearDOC07());
            pstmt.setBigDecimal(5,refDetailVO.getDocNumberDOC07());
            pstmt.setString(6,refDetailVO.getItemCodeItm01DOC07());
            pstmt.setString(7,docType);
            pstmt.setBigDecimal(8,docYear);
            pstmt.setBigDecimal(9,docNumber);
            pstmt.setBigDecimal(10,rowNumber);
            pstmt.execute();
            pstmt.close();
          }
        }

        // check if linked document can be closed...
        boolean canCloseLinkedDoc = true;
        pstmt = conn.prepareStatement(
          "select QTY from DOC07_PURCHASE_ITEMS where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? and QTY-INVOICE_QTY>0"
        );
        pstmt.setString(1,refPK.getCompanyCodeSys01DOC06());
        pstmt.setString(2,refPK.getDocTypeDOC06());
        pstmt.setBigDecimal(3,refPK.getDocYearDOC06());
        pstmt.setBigDecimal(4,refPK.getDocNumberDOC06());
        rset = pstmt.executeQuery();
        if (rset.next())
          canCloseLinkedDoc = false;
        rset.close();
        pstmt.close();

        if (canCloseLinkedDoc) {
          // the linked document can be closed...
          pstmt = conn.prepareStatement("update DOC06_PURCHASE set DOC_STATE=? where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=?");
          pstmt.setString(1,ApplicationConsts.CLOSED);
          pstmt.setString(2,refPK.getCompanyCodeSys01DOC06());
          pstmt.setString(3,refPK.getDocTypeDOC06());
          pstmt.setBigDecimal(4,refPK.getDocYearDOC06());
          pstmt.setBigDecimal(5,refPK.getDocNumberDOC06());
          pstmt.execute();
        }

      }

      // generate progressive for doc. sequence...
      if (docVO.getDocTypeDOC06().equals(ApplicationConsts.PURCHASE_INVOICE_DOC_TYPE) ||
          docVO.getDocTypeDOC06().equals(ApplicationConsts.PURCHASE_INVOICE_FROM_DN_DOC_TYPE) ||
          docVO.getDocTypeDOC06().equals(ApplicationConsts.PURCHASE_INVOICE_FROM_PD_DOC_TYPE)) {
        // invoice...
        pstmt = conn.prepareStatement(
          "select max(DOC_SEQUENCE) from DOC06_PURCHASE where COMPANY_CODE_SYS01=? and DOC_TYPE in (?,?,?) and DOC_YEAR=? and DOC_SEQUENCE is not null"
        );
        pstmt.setString(1,pk.getCompanyCodeSys01DOC06());
        pstmt.setString(2,ApplicationConsts.PURCHASE_INVOICE_DOC_TYPE);
        pstmt.setString(3,ApplicationConsts.PURCHASE_INVOICE_FROM_DN_DOC_TYPE);
        pstmt.setString(4,ApplicationConsts.PURCHASE_INVOICE_FROM_PD_DOC_TYPE);
        pstmt.setBigDecimal(5,pk.getDocYearDOC06());
      }
      else {
        // other sale document (e.g. debiting note)...
        pstmt = conn.prepareStatement(
          "select max(DOC_SEQUENCE) from DOC06_PURCHASE where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_SEQUENCE is not null"
        );
        pstmt.setString(1,pk.getCompanyCodeSys01DOC06());
        pstmt.setString(2,pk.getDocTypeDOC06());
        pstmt.setBigDecimal(3,pk.getDocYearDOC06());
      }
      rset = pstmt.executeQuery();
      int docSequenceDOC06 = 1;
      if (rset.next())
        docSequenceDOC06 = rset.getInt(1)+1;
      rset.close();
      pstmt.close();
      docVO.setDocSequenceDOC06(new BigDecimal(docSequenceDOC06));


      // retrieve payment instalments...
      res = payAction.executeCommand(new LookupValidationParams(docVO.getPaymentCodeReg10DOC06(),new HashMap()),userSessionPars,request,response,userSession,context);
      if (res.isError()) {
        conn.rollback();
        return res;
      }
      PaymentVO payVO = (PaymentVO)((VOListResponse)res).getRows().get(0);

      gridParams = new GridParams();
      gridParams.getOtherGridParams().put(ApplicationConsts.PAYMENT_CODE_REG10,docVO.getPaymentCodeReg10DOC06());
      res = paysAction.executeCommand(gridParams,userSessionPars,request,response,userSession,context);
      if (res.isError()) {
        conn.rollback();
        return res;
      }
      rows = ((VOListResponse)res).getRows();

      // create expirations in DOC19 ONLY if:
      // - there are more than one instalment OR
      // - there is only one instalment and this instalment has more than 0 instalment days
      if (rows.size()>1 || (rows.size()==1 && ((PaymentInstalmentVO)rows.get(0)).getInstalmentDaysREG17().intValue()>0 )) {
        PaymentInstalmentVO inVO = null;
        pstmt = conn.prepareStatement(
          "insert into DOC19_EXPIRATIONS(COMPANY_CODE_SYS01,DOC_TYPE,DOC_YEAR,DOC_NUMBER,DOC_SEQUENCE,PROGRESSIVE,DOC_DATE,EXPIRATION_DATE,NAME_1,NAME_2,VALUE,PAYED,DESCRIPTION,CUSTOMER_SUPPLIER_CODE,PROGRESSIVE_REG04,CURRENCY_CODE_REG03) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
        );
        long startTime = docVO.getDocDateDOC06().getTime(); // invoice date...
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
          pstmt.setString(1,docVO.getCompanyCodeSys01DOC06());
          pstmt.setString(2,docVO.getDocTypeDOC06());
          pstmt.setBigDecimal(3,docVO.getDocYearDOC06());
          pstmt.setBigDecimal(4,docVO.getDocNumberDOC06());
          pstmt.setBigDecimal(5,docVO.getDocSequenceDOC06());
          pstmt.setBigDecimal(6,ProgressiveUtils.getConsecutiveProgressive("DOC19_EXPIRATIONS","PROGRESSIVE",conn));
          pstmt.setDate(7,docVO.getDocDateDOC06());
          pstmt.setDate(8,new java.sql.Date(startTime + inVO.getInstalmentDaysREG17().longValue()*86400*1000)); // expiration date
          pstmt.setString(9,docVO.getName_1REG04());
          pstmt.setString(10,docVO.getName_2REG04());

          if (docVO.getDocTypeDOC06().equals(ApplicationConsts.PURCHASE_DEBIT_NOTE_DOC_TYPE))
            amount = docVO.getTotalDOC06().multiply(inVO.getPercentageREG17()).divide(new BigDecimal(-100),BigDecimal.ROUND_HALF_UP).setScale(docVO.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP); // value
          else
            amount = docVO.getTotalDOC06().multiply(inVO.getPercentageREG17()).divide(new BigDecimal(100),BigDecimal.ROUND_HALF_UP).setScale(docVO.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP); // value

          pstmt.setBigDecimal(11,CurrencyConversionUtils.convertCurrencyToCurrency(amount,conv));
          pstmt.setString(12,"N");

          if (docVO.getDocTypeDOC06().equals(ApplicationConsts.PURCHASE_DEBIT_NOTE_DOC_TYPE))
            pstmt.setString(13,resources.getResource("debiting note")+" "+docVO.getDocSequenceDOC06()+"/"+docVO.getDocYearDOC06()+" - "+resources.getResource("valueREG01")+" "+resources.getResource("rateNumberREG17")+" "+(i+1)+" - "+inVO.getPaymentTypeDescriptionSYS10()); // description
          else
            pstmt.setString(13,resources.getResource("purchase invoice")+" "+docVO.getDocSequenceDOC06()+"/"+docVO.getDocYearDOC06()+" - "+resources.getResource("valueREG01")+" "+resources.getResource("rateNumberREG17")+" "+(i+1)+" - "+inVO.getPaymentTypeDescriptionSYS10()); // description

          pstmt.setString(14,docVO.getSupplierCodePUR01());
          pstmt.setBigDecimal(15,docVO.getProgressiveReg04DOC06());
          pstmt.setString(16,companyCurrencyCode);
          pstmt.execute();
        }
        pstmt.close();
      }


      // change doc state to close...
      pstmt = conn.prepareStatement("update DOC06_PURCHASE set DOC_STATE=?,DOC_SEQUENCE=? where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=?");
      pstmt.setString(1,ApplicationConsts.CLOSED);
      pstmt.setInt(2,docSequenceDOC06);
      pstmt.setString(3,pk.getCompanyCodeSys01DOC06());
      pstmt.setString(4,pk.getDocTypeDOC06());
      pstmt.setBigDecimal(5,pk.getDocYearDOC06());
      pstmt.setBigDecimal(6,pk.getDocNumberDOC06());
      pstmt.execute();



      // check if this document is a purchase invoice or a debiting note and has a linked purchase order document:
      // if this is the case, then the linked document will be updated...
      if (docVO.getDocTypeDOC06().equals(ApplicationConsts.PURCHASE_INVOICE_FROM_DN_DOC_TYPE) ||
          docVO.getDocTypeDOC06().equals(ApplicationConsts.PURCHASE_INVOICE_FROM_PD_DOC_TYPE) ||
          docVO.getDocTypeDOC06().equals(ApplicationConsts.PURCHASE_INVOICE_DOC_TYPE) ||
          docVO.getDocTypeDOC06().equals(ApplicationConsts.PURCHASE_DEBIT_NOTE_DOC_TYPE)
      ) {
         JournalHeaderVO jhVO = new JournalHeaderVO();
         jhVO.setCompanyCodeSys01ACC05(docVO.getCompanyCodeSys01DOC06());
         jhVO.setAccountingMotiveCodeAcc03ACC05(ApplicationConsts.MOTIVE_PURCHASE_INVOICE);
         jhVO.setItemDateACC05(new java.sql.Date(System.currentTimeMillis()));
         jhVO.setItemYearACC05(new BigDecimal(Calendar.getInstance().get(Calendar.YEAR)));

         if (docVO.getDocTypeDOC06().equals(ApplicationConsts.PURCHASE_DEBIT_NOTE_DOC_TYPE))
           jhVO.setDescriptionACC05(
               resources.getResource("noteNumber")+" "+docVO.getDocSequenceDOC06()+"/"+docVO.getDocYearDOC06()+" - "+
               resources.getResource("supplier")+" "+docVO.getName_1REG04()+" "+(docVO.getName_2REG04()==null?"":docVO.getName_2REG04())
           );
         else
           jhVO.setDescriptionACC05(
               resources.getResource("invoiceNumber")+" "+docVO.getDocSequenceDOC06()+"/"+docVO.getDocYearDOC06()+" - "+
               resources.getResource("supplier")+" "+docVO.getName_1REG04()+" "+(docVO.getName_2REG04()==null?"":docVO.getName_2REG04())
           );

         // determine account codes defined for the current customer...
         pstmt = conn.prepareStatement(
           "select DEBIT_ACCOUNT_CODE_ACC02,COSTS_ACCOUNT_CODE_ACC02 from PUR01_SUPPLIERS where COMPANY_CODE_SYS01=? and PROGRESSIVE_REG04=?"
         );
         pstmt.setString(1,pk.getCompanyCodeSys01DOC06());
         pstmt.setBigDecimal(2,docVO.getProgressiveReg04DOC06());
         rset = pstmt.executeQuery();
         if (!rset.next()) {
           rset.close();
           return new ErrorResponse("supplier not found");
         }
         String debitAccountCodeAcc02 = rset.getString(1);
         String costsAccountCodeAcc02 = rset.getString(2);
         rset.close();
         pstmt.close();

         // determine account code defined for vat in sale vat register...
         pstmt = conn.prepareStatement(
           "select ACCOUNT_CODE_ACC02 from ACC04_VAT_REGISTERS where COMPANY_CODE_SYS01=? and REGISTER_CODE=?"
         );
         pstmt.setString(1,pk.getCompanyCodeSys01DOC06());
         pstmt.setString(2,ApplicationConsts.VAT_REGISTER_PURCHASE);
         rset = pstmt.executeQuery();
         if (!rset.next()) {
           rset.close();
           return new ErrorResponse("vat register not found");
         }
         String vatAccountCodeAcc02 = rset.getString(1);
         rset.close();
         pstmt.close();

         // calculate taxable income rows, grouped by vat code...
         res = taxableIncomeAction.calcTaxableIncomes(conn,docVO,userSessionPars,request,response,userSession,context);
         if (res.isError()) {
           conn.rollback();
           return res;
         }

         // add taxable income rows to the accounting item...
         ArrayList taxableIncomes = ((VOListResponse)res).getRows();
         TaxableIncomeVO tVO = null;
         JournalRowVO jrVO = null;
         BigDecimal totalVat = new BigDecimal(0);
         Hashtable vats = new Hashtable(); // collections of: vat code,VatRowVO
         VatRowVO vatVO = null;
         for(int i=0;i<taxableIncomes.size();i++) {
           tVO = (TaxableIncomeVO)taxableIncomes.get(i);
           totalVat = totalVat.add(tVO.getVatValue());
           jrVO = new JournalRowVO();
           jrVO.setCompanyCodeSys01ACC06(jhVO.getCompanyCodeSys01ACC05());
           jrVO.setAccountCodeAcc02ACC06(costsAccountCodeAcc02);
           jrVO.setAccountCodeACC06(costsAccountCodeAcc02);
           jrVO.setAccountCodeTypeACC06(ApplicationConsts.ACCOUNT_TYPE_ACCOUNT);

           if (docVO.getDocTypeDOC06().equals(ApplicationConsts.PURCHASE_DEBIT_NOTE_DOC_TYPE))
             jrVO.setDebitAmountACC06(tVO.getTaxableIncome().negate());
           else
             jrVO.setDebitAmountACC06(tVO.getTaxableIncome());
           jrVO.setDebitAmountACC06(CurrencyConversionUtils.convertCurrencyToCurrency(jrVO.getDebitAmountACC06(),conv));

           jrVO.setDescriptionACC06("");
           jrVO.setItemYearAcc05ACC06(jhVO.getItemYearACC05());
           jrVO.setProgressiveAcc05ACC06(jhVO.getProgressiveACC05());
           jhVO.addJournalRow(jrVO);

           // prepare vat row for sale vat register...
           vatVO = (VatRowVO)vats.get(tVO.getVatCode());
           if (vatVO==null) {
             vatVO = new VatRowVO();
             vatVO.setCompanyCodeSys01ACC07(docVO.getCompanyCodeSys01DOC06());
             vatVO.setRegisterCodeAcc04ACC07(ApplicationConsts.VAT_REGISTER_PURCHASE);
             vatVO.setTaxableIncomeACC07(tVO.getTaxableIncome());
             vatVO.setTaxableIncomeACC07(CurrencyConversionUtils.convertCurrencyToCurrency(vatVO.getTaxableIncomeACC07(),conv));
             vatVO.setVatCodeACC07(tVO.getVatCode());
             vatVO.setVatDateACC07(new java.sql.Date(System.currentTimeMillis()));
             vatVO.setVatDescriptionACC07(tVO.getVatDescription());

             if (docVO.getDocTypeDOC06().equals(ApplicationConsts.PURCHASE_DEBIT_NOTE_DOC_TYPE))
               vatVO.setVatValueACC07(tVO.getVatValue().negate());
             else
               vatVO.setVatValueACC07(tVO.getVatValue());
             vatVO.setVatValueACC07(CurrencyConversionUtils.convertCurrencyToCurrency(vatVO.getVatValueACC07(),conv));

             vatVO.setVatYearACC07(new BigDecimal(Calendar.getInstance().get(Calendar.YEAR)));
           }
           else {
             vatVO.setTaxableIncomeACC07(vatVO.getTaxableIncomeACC07().add(tVO.getTaxableIncome()));
             vatVO.setTaxableIncomeACC07(CurrencyConversionUtils.convertCurrencyToCurrency(vatVO.getTaxableIncomeACC07(),conv));
             if (docVO.getDocTypeDOC06().equals(ApplicationConsts.PURCHASE_DEBIT_NOTE_DOC_TYPE))
               vatVO.setVatValueACC07(vatVO.getVatValueACC07().add(tVO.getVatValue()).negate());
             else
               vatVO.setVatValueACC07(vatVO.getVatValueACC07().add(tVO.getVatValue()));
             vatVO.setVatValueACC07(CurrencyConversionUtils.convertCurrencyToCurrency(vatVO.getVatValueACC07(),conv));
           }
           vats.put(tVO.getVatCode(),vatVO);
         }

         // add total vat value to the accounting item...
         jrVO = new JournalRowVO();
         jrVO.setCompanyCodeSys01ACC06(jhVO.getCompanyCodeSys01ACC05());
         jrVO.setAccountCodeAcc02ACC06(vatAccountCodeAcc02);
         jrVO.setAccountCodeACC06(vatAccountCodeAcc02);
         jrVO.setAccountCodeTypeACC06(ApplicationConsts.ACCOUNT_TYPE_ACCOUNT);

         if (docVO.getDocTypeDOC06().equals(ApplicationConsts.PURCHASE_DEBIT_NOTE_DOC_TYPE))
           jrVO.setDebitAmountACC06(totalVat.negate());
         else
           jrVO.setDebitAmountACC06(totalVat);
          jrVO.setDebitAmountACC06(CurrencyConversionUtils.convertCurrencyToCurrency(jrVO.getDebitAmountACC06(),conv));

         jrVO.setDescriptionACC06("");
         jrVO.setItemYearAcc05ACC06(jhVO.getItemYearACC05());
         jrVO.setProgressiveAcc05ACC06(jhVO.getProgressiveACC05());
         jhVO.addJournalRow(jrVO);

         // add total debit value to the accounting item...
         jrVO = new JournalRowVO();
         jrVO.setCompanyCodeSys01ACC06(jhVO.getCompanyCodeSys01ACC05());
         jrVO.setAccountCodeAcc02ACC06(debitAccountCodeAcc02);
         jrVO.setAccountCodeACC06(docVO.getSupplierCodePUR01());
         jrVO.setAccountCodeTypeACC06(ApplicationConsts.ACCOUNT_TYPE_SUPPLIER);

         if (docVO.getDocTypeDOC06().equals(ApplicationConsts.PURCHASE_DEBIT_NOTE_DOC_TYPE))
           jrVO.setCreditAmountACC06(docVO.getTotalDOC06().negate());
         else
           jrVO.setCreditAmountACC06(docVO.getTotalDOC06());
         jrVO.setCreditAmountACC06(CurrencyConversionUtils.convertCurrencyToCurrency(jrVO.getCreditAmountACC06(),conv));

         jrVO.setDescriptionACC06("");
         jrVO.setItemYearAcc05ACC06(jhVO.getItemYearACC05());
         jrVO.setProgressiveAcc05ACC06(jhVO.getProgressiveACC05());
         jhVO.addJournalRow(jrVO);

         res = insJornalItemAction.insertItem(conn,jhVO,userSessionPars,request,response,userSession,context);
         if (res.isError()) {
           conn.rollback();
           return res;
         }

         // insert vat rows in sale vat register...
         ArrayList vatRows = new ArrayList();
         Enumeration en = vats.keys();
         while(en.hasMoreElements())
           vatRows.add(vats.get(en.nextElement()));
         res = vatRegisterAction.insertVatRows(conn,vatRows,userSessionPars,request,response,userSession,context);
         if (res.isError()) {
           conn.rollback();
           return res;
         }


         // create an item registration for proceeding, according to expiration settings (e.g. invoice with cash payment):
         // there must be only one instalment and this instalment has 0 instalment days
         if (rows.size()==1 && ((PaymentInstalmentVO)rows.get(0)).getInstalmentDaysREG17().intValue()==0) {
           HashMap map = new HashMap();
           map.put(ApplicationConsts.COMPANY_CODE_SYS01,docVO.getCompanyCodeSys01DOC06());
           map.put(ApplicationConsts.PARAM_CODE,ApplicationConsts.CASE_ACCOUNT);
           res = userParamAction.executeCommand(map,userSessionPars,request,response,userSession,context);
           if (res.isError()) {
             conn.rollback();
             return res;
           }
           String caseAccountCode = ((VOResponse)res).getVo().toString();

           jhVO = new JournalHeaderVO();
           jhVO.setCompanyCodeSys01ACC05(docVO.getCompanyCodeSys01DOC06());
           if (docVO.getDocTypeDOC06().equals(ApplicationConsts.PURCHASE_DEBIT_NOTE_DOC_TYPE)) {
             jhVO.setAccountingMotiveCodeAcc03ACC05(ApplicationConsts.MOTIVE_PURCHASE_INVOICE_PAYED);
             jhVO.setDescriptionACC05(
                 resources.getResource("noteNumber")+" "+docVO.getDocSequenceDOC06()+"/"+docVO.getDocYearDOC06()+" - "+
                 resources.getResource("supplier")+" "+docVO.getName_1REG04()+" "+(docVO.getName_2REG04()==null?"":docVO.getName_2REG04())
             );
           }
           else {
             jhVO.setAccountingMotiveCodeAcc03ACC05(ApplicationConsts.MOTIVE_PURCHASE_INVOICE_PAYED);
             jhVO.setDescriptionACC05(
                 resources.getResource("invoiceNumber")+" "+docVO.getDocSequenceDOC06()+"/"+docVO.getDocYearDOC06()+" - "+
                 resources.getResource("supplier")+" "+docVO.getName_1REG04()+" "+(docVO.getName_2REG04()==null?"":docVO.getName_2REG04())
             );
           }

           jhVO.setItemDateACC05(new java.sql.Date(System.currentTimeMillis()));
           jhVO.setItemYearACC05(new BigDecimal(Calendar.getInstance().get(Calendar.YEAR)));

           jrVO = new JournalRowVO();
           jrVO.setCompanyCodeSys01ACC06(jhVO.getCompanyCodeSys01ACC05());
           jrVO.setAccountCodeAcc02ACC06(debitAccountCodeAcc02);
           jrVO.setAccountCodeACC06(docVO.getSupplierCodePUR01());
           jrVO.setAccountCodeTypeACC06(ApplicationConsts.ACCOUNT_TYPE_SUPPLIER);

           if (docVO.getDocTypeDOC06().equals(ApplicationConsts.PURCHASE_DEBIT_NOTE_DOC_TYPE))
             jrVO.setDebitAmountACC06(docVO.getTotalDOC06().negate());
           else
             jrVO.setDebitAmountACC06(docVO.getTotalDOC06());
           jrVO.setDebitAmountACC06(CurrencyConversionUtils.convertCurrencyToCurrency(jrVO.getDebitAmountACC06(),conv));

           jrVO.setDescriptionACC06("");
           jrVO.setItemYearAcc05ACC06(jhVO.getItemYearACC05());
           jrVO.setProgressiveAcc05ACC06(jhVO.getProgressiveACC05());
           jhVO.addJournalRow(jrVO);

           jrVO = new JournalRowVO();
           jrVO.setCompanyCodeSys01ACC06(jhVO.getCompanyCodeSys01ACC05());
           jrVO.setAccountCodeAcc02ACC06(caseAccountCode);
           jrVO.setAccountCodeACC06(caseAccountCode);
           jrVO.setAccountCodeTypeACC06(ApplicationConsts.ACCOUNT_TYPE_ACCOUNT);

           if (docVO.getDocTypeDOC06().equals(ApplicationConsts.PURCHASE_DEBIT_NOTE_DOC_TYPE))
             jrVO.setCreditAmountACC06(docVO.getTotalDOC06().negate());
           else
             jrVO.setCreditAmountACC06(docVO.getTotalDOC06());
           jrVO.setCreditAmountACC06(CurrencyConversionUtils.convertCurrencyToCurrency(jrVO.getCreditAmountACC06(),conv));

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

      Response answer = new VOResponse(new BigDecimal(docSequenceDOC06));

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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while closing a purchase document",ex);
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
