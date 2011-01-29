package org.jallinone.purchases.documents.server;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;

import javax.sql.DataSource;

import org.jallinone.accounting.movements.java.JournalHeaderVO;
import org.jallinone.accounting.movements.java.JournalRowVO;
import org.jallinone.accounting.movements.java.TaxableIncomeVO;
import org.jallinone.accounting.movements.java.VatRowVO;
import org.jallinone.accounting.movements.server.AccountingMovementsBean;
import org.jallinone.accounting.movements.server.InsertJournalItemBean;
import org.jallinone.accounting.vatregisters.server.VatRegistersBean;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.purchases.documents.java.DetailPurchaseDocRowVO;
import org.jallinone.purchases.documents.java.DetailPurchaseDocVO;
import org.jallinone.purchases.documents.java.GridPurchaseDocRowVO;
import org.jallinone.purchases.documents.java.PurchaseDocPK;
import org.jallinone.purchases.documents.java.PurchaseDocRowPK;
import org.jallinone.registers.currency.server.CurrencyConversionUtils;
import org.jallinone.registers.payments.java.PaymentInstalmentVO;
import org.jallinone.registers.payments.java.PaymentVO;
import org.jallinone.registers.payments.server.PaymentsBean;
import org.jallinone.system.progressives.server.CompanyProgressiveUtils;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.system.server.ParamsBean;
import org.jallinone.warehouse.movements.server.ManualMovementsBean;
import org.openswing.swing.logger.server.Logger;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.message.receive.java.VOResponse;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.message.send.java.LookupValidationParams;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to close a purchase document, i.e.
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
public class ClosePurchaseDocBean  implements ClosePurchaseDoc {


	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/** external connection */
	private Connection conn = null;

	/**
	 * Set external connection.
	 */
	public void setConn(Connection conn) {
		this.conn = conn;
	}

	/**
	 * Create local connection
	 */
	public Connection getConn() throws Exception {
		Connection c = dataSource.getConnection(); c.setAutoCommit(false); return c;
	}



	private LoadPurchaseDocRowsBean rowsAction;

	public void setRowsAction(LoadPurchaseDocRowsBean rowsAction) {
		this.rowsAction = rowsAction;
	}

	private LoadPurchaseDocBean docAction;

	public void setDocAction(LoadPurchaseDocBean docAction) {
		this.docAction = docAction;
	}

	private ManualMovementsBean movBean;

	public void setMovBean(ManualMovementsBean movBean) {
		this.movBean = movBean;
	}

	private LoadPurchaseDocRowBean rowAction;

	public void setRowAction(LoadPurchaseDocRowBean rowAction) {
		this.rowAction = rowAction;
	}

	private PaymentsBean payAction;

	public void setPayAction(PaymentsBean payAction) {
		this.payAction = payAction;
	}


	private InsertJournalItemBean insJornalItemAction;

	public void setInsJornalItemAction(InsertJournalItemBean insJornalItemAction) {
		this.insJornalItemAction = insJornalItemAction;
	}

	private PurchaseDocTaxableIncomesBean taxableIncomeAction;

	public void setTaxableIncomeAction(PurchaseDocTaxableIncomesBean taxableIncomeAction) {
		this.taxableIncomeAction = taxableIncomeAction;
	}

	private AccountingMovementsBean vatRegisterAction;

	public void setVatRegisterAction(AccountingMovementsBean vatRegisterAction) {
		this.vatRegisterAction = vatRegisterAction;
	}

	private ParamsBean userParamAction;

	public void setUserParamAction(ParamsBean userParamAction) {
		this.userParamAction = userParamAction;
	}



	public ClosePurchaseDocBean() {}



	/**
	 * Business logic to execute.
	 */
	 public VOResponse closePurchaseDoc(
		 HashMap variant1Descriptions,
		 HashMap variant2Descriptions,
		 HashMap variant3Descriptions,
		 HashMap variant4Descriptions,
		 HashMap variant5Descriptions,
		 PurchaseDocPK pk, String t1, String t2, String t3, String t4, String t5,
		 String t6, String t7, String serverLanguageId, String username) throws
		 Throwable {

		 PreparedStatement pstmt = null;
		 PreparedStatement pstmt2 = null;
		 ResultSet rset = null;

		 Connection conn = null;
		 try {
			 if (this.conn==null) conn = getConn(); else conn = this.conn;
			 rowsAction.setConn(conn); // use same transaction...
			 docAction.setConn(conn); // use same transaction...
			 movBean.setConn(conn); // use same transaction...
			 rowAction.setConn(conn); // use same transaction...
			 payAction.setConn(conn); // use same transaction...
			 insJornalItemAction.setConn(conn); // use same transaction...
			 taxableIncomeAction.setConn(conn); // use same transaction...
			 vatRegisterAction.setConn(conn); // use same transaction...
			 userParamAction.setConn(conn); // use same transaction...


			 // retrieve document header...
			 DetailPurchaseDocVO docVO =docAction.loadPurchaseDoc(pk,serverLanguageId,username,new ArrayList());

			 // retrieve company currency code and currency conversion factor...
			 String companyCurrencyCode = CurrencyConversionUtils.getCompanyCurrencyCode(docVO.getCompanyCodeSys01DOC06(),conn);
			 BigDecimal conv = CurrencyConversionUtils.getConversionFactor(docVO.getCurrencyCodeReg03DOC06(),companyCurrencyCode,conn);

			 // retrieve document item rows...
			 GridParams gridParams = new GridParams();
			 gridParams.getOtherGridParams().put(ApplicationConsts.PURCHASE_DOC_PK,pk);
			 Response res = rowsAction.loadPurchaseDocRows(variant1Descriptions,variant2Descriptions,variant3Descriptions,variant4Descriptions,variant5Descriptions,gridParams,serverLanguageId,username);
			 if (res.isError())
				 throw new Exception(res.getErrorMessage());
			 java.util.List rows = ((VOListResponse)res).getRows();

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
					 refDetailVO = rowAction.loadPurchaseDocRow(
      				 variant1Descriptions,variant2Descriptions,variant3Descriptions,variant4Descriptions,variant5Descriptions,
							 new PurchaseDocRowPK(
									 docVO.getCompanyCodeSys01Doc06DOC06(),
									 docVO.getDocTypeDoc06DOC06(),
									 docVO.getDocYearDoc06DOC06(),
									 docVO.getDocNumberDoc06DOC06(),
									 vo.getItemCodeItm01DOC07(),
									 vo.getVariantTypeItm06DOC07(),
									 vo.getVariantCodeItm11DOC07(),
									 vo.getVariantTypeItm07DOC07(),
									 vo.getVariantCodeItm12DOC07(),
									 vo.getVariantTypeItm08DOC07(),
									 vo.getVariantCodeItm13DOC07(),
									 vo.getVariantTypeItm09DOC07(),
									 vo.getVariantCodeItm14DOC07(),
									 vo.getVariantTypeItm10DOC07(),
									 vo.getVariantCodeItm15DOC07()
							 ),
							 serverLanguageId,username
					 );
					 refDetailVO.setInvoiceQtyDOC07(
							 refDetailVO.getInvoiceQtyDOC07().add(vo.getQtyDOC07()).setScale(docVO.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP)
					 );
					 if (refDetailVO.getInvoiceQtyDOC07().doubleValue()>refDetailVO.getQtyDOC07().doubleValue())
						 refDetailVO.setInvoiceQtyDOC07( refDetailVO.getQtyDOC07() );

					 // update ref. item row...
					 pstmt = conn.prepareStatement(
							 "update DOC07_PURCHASE_ITEMS set INVOICE_QTY=? where "+
									 "COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? and ITEM_CODE_ITM01=? and "+
									 "VARIANT_TYPE_ITM06=? and VARIANT_CODE_ITM11=? and "+
									 "VARIANT_TYPE_ITM07=? and VARIANT_CODE_ITM12=? and "+
									 "VARIANT_TYPE_ITM08=? and VARIANT_CODE_ITM13=? and "+
									 "VARIANT_TYPE_ITM09=? and VARIANT_CODE_ITM14=? and "+
									 "VARIANT_TYPE_ITM10=? and VARIANT_CODE_ITM15=? "
					 );
					 pstmt.setBigDecimal(1,refDetailVO.getInvoiceQtyDOC07());
					 pstmt.setString(2,refDetailVO.getCompanyCodeSys01DOC07());
					 pstmt.setString(3,refDetailVO.getDocTypeDOC07());
					 pstmt.setBigDecimal(4,refDetailVO.getDocYearDOC07());
					 pstmt.setBigDecimal(5,refDetailVO.getDocNumberDOC07());
					 pstmt.setString(6,refDetailVO.getItemCodeItm01DOC07());

					 pstmt.setString(7,refDetailVO.getVariantTypeItm06DOC07());
					 pstmt.setString(8,refDetailVO.getVariantCodeItm11DOC07());
					 pstmt.setString(9,refDetailVO.getVariantTypeItm07DOC07());
					 pstmt.setString(10,refDetailVO.getVariantCodeItm12DOC07());
					 pstmt.setString(11,refDetailVO.getVariantTypeItm08DOC07());
					 pstmt.setString(12,refDetailVO.getVariantCodeItm13DOC07());
					 pstmt.setString(13,refDetailVO.getVariantTypeItm09DOC07());
					 pstmt.setString(14,refDetailVO.getVariantCodeItm14DOC07());
					 pstmt.setString(15,refDetailVO.getVariantTypeItm10DOC07());
					 pstmt.setString(16,refDetailVO.getVariantCodeItm15DOC07());

					 pstmt.execute();
					 pstmt.close();

					 // update ref. item row in the in delivery note...
					 pstmt2 = conn.prepareStatement(
							 "select QTY,INVOICE_QTY,DOC_TYPE,DOC_YEAR,DOC_NUMBER,ROW_NUMBER from DOC09_IN_DELIVERY_NOTE_ITEMS where "+
							 "COMPANY_CODE_SYS01=? and DOC_TYPE_DOC06=? and DOC_YEAR_DOC06=? and DOC_NUMBER_DOC06=? and ITEM_CODE_ITM01=? and INVOICE_QTY<QTY and "+
							 "VARIANT_TYPE_ITM06=? and VARIANT_CODE_ITM11=? and "+
							 "VARIANT_TYPE_ITM07=? and VARIANT_CODE_ITM12=? and "+
							 "VARIANT_TYPE_ITM08=? and VARIANT_CODE_ITM13=? and "+
							 "VARIANT_TYPE_ITM09=? and VARIANT_CODE_ITM14=? and "+
							 "VARIANT_TYPE_ITM10=? and VARIANT_CODE_ITM15=? "
					 );
					 qty = null;
					 invoiceQty = null;

					 pstmt2.setString(1,refDetailVO.getCompanyCodeSys01DOC07());
					 pstmt2.setString(2,refDetailVO.getDocTypeDOC07());
					 pstmt2.setBigDecimal(3,refDetailVO.getDocYearDOC07());
					 pstmt2.setBigDecimal(4,refDetailVO.getDocNumberDOC07());
					 pstmt2.setString(5,refDetailVO.getItemCodeItm01DOC07());

					 pstmt2.setString(6,refDetailVO.getVariantTypeItm06DOC07());
					 pstmt2.setString(7,refDetailVO.getVariantCodeItm11DOC07());
					 pstmt2.setString(8,refDetailVO.getVariantTypeItm07DOC07());
					 pstmt2.setString(9,refDetailVO.getVariantCodeItm12DOC07());
					 pstmt2.setString(10,refDetailVO.getVariantTypeItm08DOC07());
					 pstmt2.setString(11,refDetailVO.getVariantCodeItm13DOC07());
					 pstmt2.setString(12,refDetailVO.getVariantTypeItm09DOC07());
					 pstmt2.setString(13,refDetailVO.getVariantCodeItm14DOC07());
					 pstmt2.setString(14,refDetailVO.getVariantTypeItm10DOC07());
					 pstmt2.setString(15,refDetailVO.getVariantCodeItm15DOC07());

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
								 "DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? and ROW_NUMBER=? and "+
								 "VARIANT_TYPE_ITM06=? and VARIANT_CODE_ITM11=? and "+
								 "VARIANT_TYPE_ITM07=? and VARIANT_CODE_ITM12=? and "+
								 "VARIANT_TYPE_ITM08=? and VARIANT_CODE_ITM13=? and "+
								 "VARIANT_TYPE_ITM09=? and VARIANT_CODE_ITM14=? and "+
								 "VARIANT_TYPE_ITM10=? and VARIANT_CODE_ITM15=? "
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

						 pstmt.setString(11,refDetailVO.getVariantTypeItm06DOC07());
						 pstmt.setString(12,refDetailVO.getVariantCodeItm11DOC07());
						 pstmt.setString(13,refDetailVO.getVariantTypeItm07DOC07());
						 pstmt.setString(14,refDetailVO.getVariantCodeItm12DOC07());
						 pstmt.setString(15,refDetailVO.getVariantTypeItm08DOC07());
						 pstmt.setString(16,refDetailVO.getVariantCodeItm13DOC07());
						 pstmt.setString(17,refDetailVO.getVariantTypeItm09DOC07());
						 pstmt.setString(18,refDetailVO.getVariantCodeItm14DOC07());
						 pstmt.setString(19,refDetailVO.getVariantTypeItm10DOC07());
						 pstmt.setString(20,refDetailVO.getVariantCodeItm15DOC07());

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


			 int docSequenceDOC06 = docVO.getDocSequenceDOC06().intValue();

/*
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

	       rset = pstmt.executeQuery();
			 	 int docSequenceDOC06 = 1;
			 	 if (rset.next())
					 docSequenceDOC06 = rset.getInt(1)+1;
				 rset.close();
				 pstmt.close();
				 docVO.setDocSequenceDOC06(new BigDecimal(docSequenceDOC06));

			 }
			 else {
*/


	     // generate progressive for doc. sequence for purchase order...
	     if (docVO.getDocTypeDOC06().equals(ApplicationConsts.PURCHASE_ORDER_DOC_TYPE)) {

		     // other sale document (e.g. debiting note)...
				 pstmt = conn.prepareStatement(
						 "select max(DOC_SEQUENCE) from DOC06_PURCHASE where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_SEQUENCE is not null"
				 );
				 pstmt.setString(1,pk.getCompanyCodeSys01DOC06());
				 pstmt.setString(2,pk.getDocTypeDOC06());
				 pstmt.setBigDecimal(3,pk.getDocYearDOC06());

				 rset = pstmt.executeQuery();
				 docSequenceDOC06 = 1;
				 if (rset.next())
					 docSequenceDOC06 = rset.getInt(1)+1;
				 rset.close();
				 pstmt.close();
				 docVO.setDocSequenceDOC06(new BigDecimal(docSequenceDOC06));
			 }


			 // retrieve payment instalments...
			 ArrayList companiesList = new ArrayList();
			 companiesList.add(docVO.getCompanyCodeSys01DOC06());
			 res = payAction.validatePaymentCode(new LookupValidationParams(docVO.getPaymentCodeReg10DOC06(),new HashMap()),serverLanguageId,username,new ArrayList(),companiesList);
			 if (res.isError()) {
				 throw new Exception(res.getErrorMessage());
			 }
			 PaymentVO payVO = (PaymentVO)((VOListResponse)res).getRows().get(0);

			 gridParams = new GridParams();
			 gridParams.getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,docVO.getCompanyCodeSys01DOC06());
			 gridParams.getOtherGridParams().put(ApplicationConsts.PAYMENT_CODE_REG10,docVO.getPaymentCodeReg10DOC06());
			 res = payAction.loadPaymentInstalments(gridParams,serverLanguageId,username);
			 if (res.isError()) {
				 throw new Exception(res.getErrorMessage());
			 }
			 rows = ((VOListResponse)res).getRows();

/*
			 // create expirations in DOC19 ONLY if:
			 // - there are more than one instalment OR
			 // - there is only one instalment and this instalment has more than 0 instalment days
			 if (rows.size()>1 || (rows.size()==1 && ((PaymentInstalmentVO)rows.get(0)).getInstalmentDaysREG17().intValue()>0 )) {
*/

	    HashMap map = new HashMap();
			map.put(ApplicationConsts.COMPANY_CODE_SYS01,docVO.getCompanyCodeSys01DOC06());
			map.put(ApplicationConsts.PARAM_CODE,ApplicationConsts.ROUNDING_COSTS_CODE);
			res = userParamAction.loadUserParam(map,serverLanguageId,username);
			if (res.isError()) {
				throw new Exception(res.getErrorMessage());
			}
			String roundingAccountCode = ((VOResponse)res).getVo().toString();


      // create ALWAYS expirations in DOC19...
			 PaymentInstalmentVO inVO = null;
			 pstmt = conn.prepareStatement(
					 "insert into DOC19_EXPIRATIONS(COMPANY_CODE_SYS01,DOC_TYPE,DOC_YEAR,DOC_NUMBER,DOC_SEQUENCE,PROGRESSIVE,"+
					 "DOC_DATE,EXPIRATION_DATE,NAME_1,NAME_2,VALUE,DESCRIPTION,CUSTOMER_SUPPLIER_CODE,PROGRESSIVE_REG04,CURRENCY_CODE_REG03,"+
					 "PAYMENT_TYPE_CODE_REG11,PAYED,REAL_PAYMENT_TYPE_CODE_REG11,PAYED_DATE,PAYED_VALUE,REAL_ACCOUNT_CODE_ACC02,ROUNDING_ACCOUNT_CODE_ACC02,ALREADY_PAYED) "+
					 "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
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
				 pstmt.setBigDecimal(6,CompanyProgressiveUtils.getInternalProgressive(docVO.getCompanyCodeSys01DOC06(),"DOC19_EXPIRATIONS","PROGRESSIVE",conn));
				 pstmt.setDate(7,docVO.getDocDateDOC06());
				 pstmt.setDate(8,new java.sql.Date(startTime + inVO.getInstalmentDaysREG17().longValue()*86400*1000)); // expiration date
				 pstmt.setString(9,docVO.getName_1REG04());
				 pstmt.setString(10,docVO.getName_2REG04());

				 if (docVO.getDocTypeDOC06().equals(ApplicationConsts.PURCHASE_DEBIT_NOTE_DOC_TYPE))
					 amount = docVO.getTotalDOC06().multiply(inVO.getPercentageREG17()).divide(new BigDecimal(-100),BigDecimal.ROUND_HALF_UP).setScale(docVO.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP); // value
				 else
					 amount = docVO.getTotalDOC06().multiply(inVO.getPercentageREG17()).divide(new BigDecimal(100),BigDecimal.ROUND_HALF_UP).setScale(docVO.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP); // value

				 pstmt.setBigDecimal(11,CurrencyConversionUtils.convertCurrencyToCurrency(amount,conv));

				 if (docVO.getDocTypeDOC06().equals(ApplicationConsts.PURCHASE_DEBIT_NOTE_DOC_TYPE))
					 pstmt.setString(12,t1+" "+docVO.getDocSequenceDOC06()+"/"+docVO.getDocYearDOC06()+" - "+t2+" "+t3+" "+(i+1)+" - "+inVO.getPaymentTypeDescriptionSYS10()); // description
				 else
					 pstmt.setString(12,t4+" "+docVO.getDocSequenceDOC06()+"/"+docVO.getDocYearDOC06()+" - "+t2+" "+t3+" "+(i+1)+" - "+inVO.getPaymentTypeDescriptionSYS10()); // description

				 pstmt.setString(13,docVO.getSupplierCodePUR01());
				 pstmt.setBigDecimal(14,docVO.getProgressiveReg04DOC06());
				 pstmt.setString(15,companyCurrencyCode);
				 pstmt.setString(16,payVO.getPaymentTypeCodeReg11REG10());
				 pstmt.setString(17,"N");
				 pstmt.setString(18,null);
				 pstmt.setDate(19,null);
				 pstmt.setBigDecimal(20,null);
				 pstmt.setString(21,payVO.getAccountCodeAcc02REG11());
				 pstmt.setString(22,roundingAccountCode);
				 pstmt.setBigDecimal(23,new BigDecimal(0));

				 pstmt.execute();
			 }
			 pstmt.close();
//			 }


			 // change doc state to close...
			 pstmt = conn.prepareStatement("update DOC06_PURCHASE set DOC_STATE=? where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=?");
			 pstmt.setString(1,ApplicationConsts.CLOSED);
			 pstmt.setString(2,pk.getCompanyCodeSys01DOC06());
			 pstmt.setString(3,pk.getDocTypeDOC06());
			 pstmt.setBigDecimal(4,pk.getDocYearDOC06());
			 pstmt.setBigDecimal(5,pk.getDocNumberDOC06());
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
							 t5+" "+docVO.getDocSequenceDOC06()+"/"+docVO.getDocYearDOC06()+" - "+
							 t6+" "+docVO.getName_1REG04()+" "+(docVO.getName_2REG04()==null?"":docVO.getName_2REG04())
					 );
				 else
					 jhVO.setDescriptionACC05(
							 t7+" "+docVO.getDocSequenceDOC06()+"/"+docVO.getDocYearDOC06()+" - "+
							 t6+" "+docVO.getName_1REG04()+" "+(docVO.getName_2REG04()==null?"":docVO.getName_2REG04())
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
					 throw new Exception("supplier not found");
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
					 throw new Exception("vat register not found");
				 }
				 String vatAccountCodeAcc02 = rset.getString(1);
				 rset.close();
				 pstmt.close();

				 // calculate taxable income rows, grouped by vat code...
				 res = taxableIncomeAction.calcTaxableIncomes(docVO,serverLanguageId,username);
				 if (res.isError()) {
					 throw new Exception(res.getErrorMessage());
				 }

				 // add taxable income rows to the accounting item...
				 java.util.List taxableIncomes = ((VOListResponse)res).getRows();
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

				 res = insJornalItemAction.insertJournalItem(jhVO,serverLanguageId,username);
				 if (res.isError()) {
					 throw new Exception(res.getErrorMessage());
				 }

				 // insert vat rows in sale vat register...
				 ArrayList vatRows = new ArrayList();
				 Enumeration en = vats.keys();
				 while(en.hasMoreElements())
					 vatRows.add(vats.get(en.nextElement()));
				 res = vatRegisterAction.insertVatRows(vatRows,serverLanguageId,username);
				 if (res.isError()) {
					 throw new Exception(res.getErrorMessage());
				 }

/*
				 // create an item registration for proceeding, according to expiration settings (e.g. invoice with cash payment):
				 // there must be only one instalment and this instalment has 0 instalment days
				 if (rows.size()==1 && ((PaymentInstalmentVO)rows.get(0)).getInstalmentDaysREG17().intValue()==0) {
					 HashMap map = new HashMap();
					 map.put(ApplicationConsts.COMPANY_CODE_SYS01,docVO.getCompanyCodeSys01DOC06());
					 map.put(ApplicationConsts.PARAM_CODE,ApplicationConsts.CASE_ACCOUNT);
					 res = userParamAction.loadUserParam(map,serverLanguageId,username);
					 if (res.isError()) {
						 throw new Exception(res.getErrorMessage());
					 }
					 String caseAccountCode = ((VOResponse)res).getVo().toString();

					 jhVO = new JournalHeaderVO();
					 jhVO.setCompanyCodeSys01ACC05(docVO.getCompanyCodeSys01DOC06());
					 if (docVO.getDocTypeDOC06().equals(ApplicationConsts.PURCHASE_DEBIT_NOTE_DOC_TYPE)) {
						 jhVO.setAccountingMotiveCodeAcc03ACC05(ApplicationConsts.MOTIVE_PURCHASE_INVOICE_PAYED);
						 jhVO.setDescriptionACC05(
								 t5+" "+docVO.getDocSequenceDOC06()+"/"+docVO.getDocYearDOC06()+" - "+
								 t6+" "+docVO.getName_1REG04()+" "+(docVO.getName_2REG04()==null?"":docVO.getName_2REG04())
						 );
					 }
					 else {
						 jhVO.setAccountingMotiveCodeAcc03ACC05(ApplicationConsts.MOTIVE_PURCHASE_INVOICE_PAYED);
						 jhVO.setDescriptionACC05(
								 t7+" "+docVO.getDocSequenceDOC06()+"/"+docVO.getDocYearDOC06()+" - "+
								 t6+" "+docVO.getName_1REG04()+" "+(docVO.getName_2REG04()==null?"":docVO.getName_2REG04())
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

					 res = insJornalItemAction.insertJournalItem(jhVO,serverLanguageId,username);
					 if (res.isError()) {
						 throw new Exception(res.getErrorMessage());
					 }
				 }
*/
			 }

			 return new VOResponse(new BigDecimal(docSequenceDOC06));
		 }
		 catch (Throwable ex) {
			 Logger.error(username,this.getClass().getName(),"executeCommand","Error while closing a purchase document",ex);
		      try {
		    	  if (this.conn==null && conn!=null)
		    		  // rollback only local connection
		    		  conn.rollback();
		      }
		      catch (Exception ex3) {
		      }
		      throw new Exception(ex.getMessage());
		 }
		 finally {
			 try {
				 pstmt.close();
			 }
			 catch (Exception ex2) {
			 }

			 try {
				 if (this.conn==null && conn!=null) {
					 // close only local connection
					 conn.commit();
					 conn.close();
				 }

			 }
			 catch (Exception exx) {}
			 try {
				 rowsAction.setConn(null);
				 docAction.setConn(null);
				 movBean.setConn(null);
				 rowAction.setConn(null);
				 payAction.setConn(null);
				 insJornalItemAction.setConn(null);
				 taxableIncomeAction.setConn(null);
				 vatRegisterAction.setConn(null);
				 userParamAction.setConn(null);
			 } catch (Exception ex) {}

		 }
	 }



}

