package org.jallinone.sales.documents.server;

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
import org.jallinone.items.java.ItemPK;
import org.jallinone.registers.currency.server.CurrencyConversionUtils;
import org.jallinone.registers.payments.java.PaymentInstalmentVO;
import org.jallinone.registers.payments.java.PaymentVO;
import org.jallinone.registers.payments.server.PaymentsBean;
import org.jallinone.sales.documents.activities.java.SaleDocActivityVO;
import org.jallinone.sales.documents.activities.server.SaleDocActivitiesBean;
import org.jallinone.sales.documents.headercharges.java.SaleDocChargeVO;
import org.jallinone.sales.documents.headercharges.server.SaleDocChargesBean;
import org.jallinone.sales.documents.java.DetailSaleDocRowVO;
import org.jallinone.sales.documents.java.DetailSaleDocVO;
import org.jallinone.sales.documents.java.GridSaleDocRowVO;
import org.jallinone.sales.documents.java.SaleDocPK;
import org.jallinone.sales.documents.java.SaleDocRowPK;
import org.jallinone.system.java.CompanyParametersVO;
import org.jallinone.system.progressives.server.CompanyProgressiveUtils;
import org.jallinone.system.server.ParamsBean;
import org.jallinone.warehouse.availability.java.BookedItemQtyVO;
import org.jallinone.warehouse.availability.server.ItemAvailabilitiesBean;
import org.jallinone.warehouse.movements.java.WarehouseMovementVO;
import org.jallinone.warehouse.movements.server.AddMovementBean;
import org.jallinone.warehouse.movements.server.ManualMovementsBean;
import org.openswing.swing.logger.server.Logger;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.message.receive.java.VOResponse;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.message.send.java.LookupValidationParams;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to close a sale document, i.e.
 * - check if all items are available (for retail selling only)
 * - unload all items from the specified warehouse (for retail selling only)
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
public class CloseSaleDocBean  implements CloseSaleDoc {


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



  private LoadSaleDocRowsBean rowsAction;

  public void setRowsAction(LoadSaleDocRowsBean rowsAction) {
    this.rowsAction = rowsAction;
  }

  private LoadSaleDocBean docAction;

  public void setDocAction(LoadSaleDocBean docAction) {
    this.docAction = docAction;
  }

  private ItemAvailabilitiesBean availAction;

  public void setAvailAction(ItemAvailabilitiesBean availAction) {
    this.availAction = availAction;
  }

  private AddMovementBean movBean;

  public void setMovBean(AddMovementBean movBean) {
    this.movBean = movBean;
  }

  private ExportRetailSaleOnFileBean expAction;

  public void setExpAction(ExportRetailSaleOnFileBean expAction) {
    this.expAction = expAction;
  }


  private PaymentsBean payAction;

  public void setPayAction(PaymentsBean payAction) {
    this.payAction = payAction;
  }


  private InsertJournalItemBean insJornalItemAction;

  public void setInsJornalItemAction(InsertJournalItemBean insJornalItemAction) {
    this.insJornalItemAction = insJornalItemAction;
  }

  private UpdateTaxableIncomesBean taxableIncomeAction;

  public void setTaxableIncomeAction(UpdateTaxableIncomesBean taxableIncomeAction) {
    this.taxableIncomeAction = taxableIncomeAction;
  }

  private AccountingMovementsBean vatRegisterAction;

  public void setVatRegisterAction(AccountingMovementsBean vatRegisterAction) {
    this.vatRegisterAction = vatRegisterAction;
  }

  private ParamsBean parsBean;

  public void setParsBean(ParamsBean parsBean) {
    this.parsBean = parsBean;
  }

	private LoadSaleDocRowBean loadSaleDocRowBean;

	public void setLoadSaleDocRowBean(LoadSaleDocRowBean loadSaleDocRowBean) {
		this.loadSaleDocRowBean = loadSaleDocRowBean;
	}



  public CloseSaleDocBean() {}



  /**
   * Business logic to execute.
   */
  public VOResponse closeSaleDoc(
      HashMap variant1Descriptions,
      HashMap variant2Descriptions,
      HashMap variant3Descriptions,
      HashMap variant4Descriptions,
      HashMap variant5Descriptions,
      SaleDocPK pk,
      String t1,String t2,String t3,String t4,String t5,
      String t6,String t7,String t8,String t9,String t10,
      String t11,String t12,String t13,String t14,String t15,String t16,String t17,
      String serverLanguageId,String username,
      ArrayList companiesList
  ) throws Throwable {

    PreparedStatement pstmt = null;
    PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
    ResultSet rset = null;

    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      rowsAction.setConn(conn); // use same transaction...
      docAction.setConn(conn); // use same transaction...
      availAction.setConn(conn); // use same transaction...
      movBean.setConn(conn); // use same transaction...
      expAction.setConn(conn); // use same transaction...
      payAction.setConn(conn); // use same transaction...
      insJornalItemAction.setConn(conn); // use same transaction...
      taxableIncomeAction.setConn(conn); // use same transaction...
      vatRegisterAction.setConn(conn); // use same transaction...
      parsBean.setConn(conn); // use same transaction...
			loadSaleDocRowBean.setConn(conn);

      // retrieve document header...
      DetailSaleDocVO docVO = docAction.loadSaleDoc(pk,serverLanguageId,username,new ArrayList());

			if (docVO.getTotalDOC01()==null || docVO.getTotalDOC01().doubleValue()==0)
				throw new Exception(t16);

      // retrieve company currency code and currency conversion factor...
      String companyCurrencyCode = CurrencyConversionUtils.getCompanyCurrencyCode(docVO.getCompanyCodeSys01DOC01(),conn);
      BigDecimal conv = CurrencyConversionUtils.getConversionFactor(docVO.getCurrencyCodeReg03DOC01(),companyCurrencyCode,conn);

      // retrieve document item rows...
      GridParams gridParams = new GridParams();
      gridParams.getOtherGridParams().put(ApplicationConsts.SALE_DOC_PK,pk);
      Response res = rowsAction.loadSaleDocRows(variant1Descriptions,variant2Descriptions,variant3Descriptions,variant4Descriptions,variant5Descriptions,gridParams,serverLanguageId,username);
      if (res.isError())
        throw new Exception(res.getErrorMessage());
      ArrayList rows = new ArrayList(((VOListResponse)res).getRows());
      ArrayList itemRows = rows;


			// retrieve payment instalments...
			ArrayList compList = new ArrayList();
			compList.add(docVO.getCompanyCodeSys01DOC01());
			res = payAction.validatePaymentCode(new LookupValidationParams(docVO.getPaymentCodeReg10DOC01(),new HashMap()),serverLanguageId,username,new ArrayList(),compList);
			if (res.isError()) {
				throw new Exception(res.getErrorMessage());
			}
			PaymentVO payVO = (PaymentVO)((VOListResponse)res).getRows().get(0);

			gridParams = new GridParams();
			gridParams.getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,docVO.getCompanyCodeSys01DOC01());
			gridParams.getOtherGridParams().put(ApplicationConsts.PAYMENT_CODE_REG10,docVO.getPaymentCodeReg10DOC01());
			res = payAction.loadPaymentInstalments(gridParams,serverLanguageId,username);
			if (res.isError()) {
				throw new Exception(res.getErrorMessage());
			}
			ArrayList paymentInstallments = new ArrayList(((VOListResponse)res).getRows());


      if (pk.getDocTypeDOC01().equals(ApplicationConsts.SALE_DESK_DOC_TYPE)) {

				// there must be only one instalment and this instalment has 0 instalment days
				if (paymentInstallments.size()>1 || ((PaymentInstalmentVO)paymentInstallments.get(0)).getInstalmentDaysREG17().intValue()>0) {
					throw new Exception(t17);
       }

        // check if all items are available...
        GridSaleDocRowVO vo = null;
        DetailSaleDocRowVO detailVO = null;
        gridParams = new GridParams();
        BookedItemQtyVO availVO = null;
        java.util.List availRows = null;
        for(int i=0;i<rows.size();i++) {
          vo = (GridSaleDocRowVO)rows.get(i);
          gridParams.getOtherGridParams().put(ApplicationConsts.WAREHOUSE_CODE,docVO.getWarehouseCodeWar01DOC01());
          gridParams.getOtherGridParams().put(ApplicationConsts.ITEM_PK,new ItemPK(vo.getCompanyCodeSys01DOC02(),vo.getItemCodeItm01DOC02()));
          res = availAction.loadBookedItems(variant1Descriptions,variant2Descriptions,variant3Descriptions,variant4Descriptions,variant5Descriptions,gridParams,serverLanguageId,username,companiesList);
          if (res.isError()) {
            throw new Exception(res.getErrorMessage());
          }
          availRows = ((VOListResponse)res).getRows();
          if (availRows.size()>0) {
            availVO = (BookedItemQtyVO) availRows.get(0);
            if (availVO.getAvailableQtyWAR03().doubleValue()>=vo.getQtyDOC02().doubleValue()) {
              // unload item from the specified warehouse...
              res = loadSaleDocRowBean.loadSaleDocRow(
                  variant1Descriptions,variant2Descriptions,variant3Descriptions,variant4Descriptions,variant5Descriptions,
                  new SaleDocRowPK(
                    vo.getCompanyCodeSys01DOC02(),
                    vo.getDocTypeDOC02(),
                    vo.getDocYearDOC02(),
                    vo.getDocNumberDOC02(),
                    vo.getItemCodeItm01DOC02(),
                    vo.getVariantTypeItm06DOC02(),
                    vo.getVariantCodeItm11DOC02(),
                    vo.getVariantTypeItm07DOC02(),
                    vo.getVariantCodeItm12DOC02(),
                    vo.getVariantTypeItm08DOC02(),
                    vo.getVariantCodeItm13DOC02(),
                    vo.getVariantTypeItm09DOC02(),
                    vo.getVariantCodeItm14DOC02(),
                    vo.getVariantTypeItm10DOC02(),
                    vo.getVariantCodeItm15DOC02()

                  ),
                  serverLanguageId,
                  username

              );
              if (res.isError()) {
                throw new Exception(res.getErrorMessage());
              }
              detailVO = (DetailSaleDocRowVO)((VOResponse)res).getVo();
              WarehouseMovementVO movVO = new WarehouseMovementVO(
                  detailVO.getProgressiveHie01DOC02(),
                  vo.getQtyDOC02(),
                  vo.getCompanyCodeSys01DOC02(),
                  docVO.getWarehouseCodeWar01DOC01(),
                  vo.getItemCodeItm01DOC02(),
                  ApplicationConsts.WAREHOUSE_MOTIVE_DIRECTLY_UNLOAD,
                  ApplicationConsts.ITEM_GOOD,
                  t2+" "+docVO.getDocTypeDOC01()+"/"+docVO.getDocNumberDOC01()+"/"+docVO.getDocYearDOC01(),
                  detailVO.getSerialNumbers(),

                  vo.getVariantCodeItm11DOC02(),
                  vo.getVariantCodeItm12DOC02(),
                  vo.getVariantCodeItm13DOC02(),
                  vo.getVariantCodeItm14DOC02(),
                  vo.getVariantCodeItm15DOC02(),
                  vo.getVariantTypeItm06DOC02(),
                  vo.getVariantTypeItm07DOC02(),
                  vo.getVariantTypeItm08DOC02(),
                  vo.getVariantTypeItm09DOC02(),
                  vo.getVariantTypeItm10DOC02()

              );
              res = movBean.addWarehouseMovement(movVO,t15,serverLanguageId,username);
              if (res.isError()) {
                throw new Exception(res.getErrorMessage());
              }
            }
            else {
              throw new Exception(t1);
            }
          }
          else {
        	  throw new Exception(t1);
          }
        }
      } // end if SALE_DESK_DOC_TYPE (check item availabilities...)



      // check if this document is a sale invoice and has a linked sale document:
      // if this is the case, then the linked document will be updated...
      if ((docVO.getDocTypeDOC01().equals(ApplicationConsts.SALE_INVOICE_FROM_DN_DOC_TYPE) ||
           docVO.getDocTypeDOC01().equals(ApplicationConsts.SALE_INVOICE_FROM_SD_DOC_TYPE)) &&
        docVO.getDocNumberDoc01DOC01()!=null
      ) {
        SaleDocPK refPK = new SaleDocPK(
          docVO.getCompanyCodeSys01Doc01DOC01(),
          docVO.getDocTypeDoc01DOC01(),
          docVO.getDocYearDoc01DOC01(),
          docVO.getDocNumberDoc01DOC01()
        );

        // retrieve ref. document item rows...
        GridSaleDocRowVO vo = null;
        DetailSaleDocRowVO refDetailVO = null;
        BigDecimal qty = null;
        BigDecimal invoiceQty = null;
        String docType = null;
        BigDecimal docYear = null;
        BigDecimal docNumber = null;
        BigDecimal rowNumber = null;
        for(int i=0;i<rows.size();i++) {
          vo = (GridSaleDocRowVO)rows.get(i);
          res = loadSaleDocRowBean.loadSaleDocRow(
              variant1Descriptions,variant2Descriptions,variant3Descriptions,variant4Descriptions,variant5Descriptions,
              new SaleDocRowPK(
                docVO.getCompanyCodeSys01Doc01DOC01(),
                docVO.getDocTypeDoc01DOC01(),
                docVO.getDocYearDoc01DOC01(),
                docVO.getDocNumberDoc01DOC01(),
                vo.getItemCodeItm01DOC02(),
                vo.getVariantTypeItm06DOC02(),
                vo.getVariantCodeItm11DOC02(),
                vo.getVariantTypeItm07DOC02(),
                vo.getVariantCodeItm12DOC02(),
                vo.getVariantTypeItm08DOC02(),
                vo.getVariantCodeItm13DOC02(),
                vo.getVariantTypeItm09DOC02(),
                vo.getVariantCodeItm14DOC02(),
                vo.getVariantTypeItm10DOC02(),
                vo.getVariantCodeItm15DOC02()
              ),
              serverLanguageId,
              username

          );
          if (res.isError()) {
            throw new Exception(res.getErrorMessage());
          }
          refDetailVO = (DetailSaleDocRowVO)((VOResponse)res).getVo();
          refDetailVO.setInvoiceQtyDOC02(
              refDetailVO.getInvoiceQtyDOC02().add(vo.getQtyDOC02()).setScale(docVO.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP)
          );
          if (refDetailVO.getInvoiceQtyDOC02().doubleValue()>refDetailVO.getQtyDOC02().doubleValue())
            refDetailVO.setInvoiceQtyDOC02( refDetailVO.getQtyDOC02() );

            // update ref. item row...
          pstmt = conn.prepareStatement(
            "update DOC02_SELLING_ITEMS set INVOICE_QTY=? where "+
            "COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? and ITEM_CODE_ITM01=? and "+
            "VARIANT_TYPE_ITM06=? and VARIANT_CODE_ITM11=? and "+
            "VARIANT_TYPE_ITM07=? and VARIANT_CODE_ITM12=? and "+
            "VARIANT_TYPE_ITM08=? and VARIANT_CODE_ITM13=? and "+
            "VARIANT_TYPE_ITM09=? and VARIANT_CODE_ITM14=? and "+
            "VARIANT_TYPE_ITM10=? and VARIANT_CODE_ITM15=? "
          );
          pstmt.setBigDecimal(1,refDetailVO.getInvoiceQtyDOC02());
          pstmt.setString(2,refDetailVO.getCompanyCodeSys01DOC02());
          pstmt.setString(3,refDetailVO.getDocTypeDOC02());
          pstmt.setBigDecimal(4,refDetailVO.getDocYearDOC02());
          pstmt.setBigDecimal(5,refDetailVO.getDocNumberDOC02());
          pstmt.setString(6,refDetailVO.getItemCodeItm01DOC02());

          pstmt.setString(7,refDetailVO.getVariantTypeItm06DOC02());
          pstmt.setString(8,refDetailVO.getVariantCodeItm11DOC02());
          pstmt.setString(9,refDetailVO.getVariantTypeItm07DOC02());
          pstmt.setString(10,refDetailVO.getVariantCodeItm12DOC02());
          pstmt.setString(11,refDetailVO.getVariantTypeItm08DOC02());
          pstmt.setString(12,refDetailVO.getVariantCodeItm13DOC02());
          pstmt.setString(13,refDetailVO.getVariantTypeItm09DOC02());
          pstmt.setString(14,refDetailVO.getVariantCodeItm14DOC02());
          pstmt.setString(15,refDetailVO.getVariantTypeItm10DOC02());
          pstmt.setString(16,refDetailVO.getVariantCodeItm15DOC02());

          pstmt.execute();
          pstmt.close();

          // update ref. item row in the out delivery note...
          pstmt2 = conn.prepareStatement(
            "select QTY,INVOICE_QTY,DOC_TYPE,DOC_YEAR,DOC_NUMBER,ROW_NUMBER from DOC10_OUT_DELIVERY_NOTE_ITEMS where "+
            "COMPANY_CODE_SYS01=? and DOC_TYPE_DOC01=? and DOC_YEAR_DOC01=? and DOC_NUMBER_DOC01=? and ITEM_CODE_ITM01=? and INVOICE_QTY<QTY and "+
            "VARIANT_TYPE_ITM06=? and VARIANT_CODE_ITM11=? and "+
            "VARIANT_TYPE_ITM07=? and VARIANT_CODE_ITM12=? and "+
            "VARIANT_TYPE_ITM08=? and VARIANT_CODE_ITM13=? and "+
            "VARIANT_TYPE_ITM09=? and VARIANT_CODE_ITM14=? and "+
            "VARIANT_TYPE_ITM10=? and VARIANT_CODE_ITM15=? "
          );
          qty = null;
          invoiceQty = null;

          pstmt2.setString(1,refDetailVO.getCompanyCodeSys01DOC02());
          pstmt2.setString(2,refDetailVO.getDocTypeDOC02());
          pstmt2.setBigDecimal(3,refDetailVO.getDocYearDOC02());
          pstmt2.setBigDecimal(4,refDetailVO.getDocNumberDOC02());
          pstmt2.setString(5,refDetailVO.getItemCodeItm01DOC02());

          pstmt2.setString(6,refDetailVO.getVariantTypeItm06DOC02());
          pstmt2.setString(7,refDetailVO.getVariantCodeItm11DOC02());
          pstmt2.setString(8,refDetailVO.getVariantTypeItm07DOC02());
          pstmt2.setString(9,refDetailVO.getVariantCodeItm12DOC02());
          pstmt2.setString(10,refDetailVO.getVariantTypeItm08DOC02());
          pstmt2.setString(11,refDetailVO.getVariantCodeItm13DOC02());
          pstmt2.setString(12,refDetailVO.getVariantTypeItm09DOC02());
          pstmt2.setString(13,refDetailVO.getVariantCodeItm14DOC02());
          pstmt2.setString(14,refDetailVO.getVariantTypeItm10DOC02());
          pstmt2.setString(15,refDetailVO.getVariantCodeItm15DOC02());

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
            if (invoiceQty.doubleValue()+vo.getQtyDOC02().doubleValue()<=qty.doubleValue())
              qty = invoiceQty.add(vo.getQtyDOC02());

            pstmt = conn.prepareStatement(
              "update DOC10_OUT_DELIVERY_NOTE_ITEMS set INVOICE_QTY=? where "+
              "COMPANY_CODE_SYS01=? and DOC_TYPE_DOC01=? and DOC_YEAR_DOC01=? and DOC_NUMBER_DOC01=? and ITEM_CODE_ITM01=? and "+
              "DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? and ROW_NUMBER=? and "+
              "VARIANT_TYPE_ITM06=? and VARIANT_CODE_ITM11=? and "+
              "VARIANT_TYPE_ITM07=? and VARIANT_CODE_ITM12=? and "+
              "VARIANT_TYPE_ITM08=? and VARIANT_CODE_ITM13=? and "+
              "VARIANT_TYPE_ITM09=? and VARIANT_CODE_ITM14=? and "+
              "VARIANT_TYPE_ITM10=? and VARIANT_CODE_ITM15=? "
            );
            pstmt.setBigDecimal(1,qty);
            pstmt.setString(2,refDetailVO.getCompanyCodeSys01DOC02());
            pstmt.setString(3,refDetailVO.getDocTypeDOC02());
            pstmt.setBigDecimal(4,refDetailVO.getDocYearDOC02());
            pstmt.setBigDecimal(5,refDetailVO.getDocNumberDOC02());
            pstmt.setString(6,refDetailVO.getItemCodeItm01DOC02());
            pstmt.setString(7,docType);
            pstmt.setBigDecimal(8,docYear);
            pstmt.setBigDecimal(9,docNumber);
            pstmt.setBigDecimal(10,rowNumber);

            pstmt.setString(11,refDetailVO.getVariantTypeItm06DOC02());
            pstmt.setString(12,refDetailVO.getVariantCodeItm11DOC02());
            pstmt.setString(13,refDetailVO.getVariantTypeItm07DOC02());
            pstmt.setString(14,refDetailVO.getVariantCodeItm12DOC02());
            pstmt.setString(15,refDetailVO.getVariantTypeItm08DOC02());
            pstmt.setString(16,refDetailVO.getVariantCodeItm13DOC02());
            pstmt.setString(17,refDetailVO.getVariantTypeItm09DOC02());
            pstmt.setString(18,refDetailVO.getVariantCodeItm14DOC02());
            pstmt.setString(19,refDetailVO.getVariantTypeItm10DOC02());
            pstmt.setString(20,refDetailVO.getVariantCodeItm15DOC02());

            pstmt.execute();
            pstmt.close();
          }
        } // end for (used to update invoice qtys in referred docs...)

        // retrieve document value charges...
        gridParams = new GridParams();
        gridParams.getOtherGridParams().put(ApplicationConsts.SALE_DOC_PK,pk);
        res = rowsAction.loadSaleDocCharges(gridParams,serverLanguageId,username);
        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }

        SaleDocChargeVO chargeVO = null;
        rows = new ArrayList(((VOListResponse)res).getRows());
        BigDecimal valueDOC03,invoicedValueDOC03 = new BigDecimal(0);
        for(int i=0;i<rows.size();i++) {
          chargeVO = (SaleDocChargeVO)rows.get(i);
          if (chargeVO.getValueDOC03()!=null) {

            pstmt = conn.prepareStatement(
              "select VALUE,INVOICED_VALUE from DOC03_SELLING_CHARGES where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? and CHARGE_CODE_SAL06=?"
            );
            pstmt.setString(1,refPK.getCompanyCodeSys01DOC01());
            pstmt.setString(2,refPK.getDocTypeDOC01());
            pstmt.setBigDecimal(3,refPK.getDocYearDOC01());
            pstmt.setBigDecimal(4,refPK.getDocNumberDOC01());
            pstmt.setString(5,chargeVO.getChargeCodeSal06DOC03());
            rset = pstmt.executeQuery();
            if (rset.next()) {
              valueDOC03 = rset.getBigDecimal(1);
              invoicedValueDOC03 = rset.getBigDecimal(2);

              invoicedValueDOC03 = invoicedValueDOC03.add(chargeVO.getValueDOC03()).setScale(docVO.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP);
              if (invoicedValueDOC03.doubleValue()>valueDOC03.doubleValue())
                invoicedValueDOC03 = valueDOC03;

            }
            rset.next();
            pstmt.close();

            pstmt = conn.prepareStatement(
              "update DOC03_SELLING_CHARGES set INVOICED_VALUE=? where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? and CHARGE_CODE_SAL06=?"
            );
            pstmt.setBigDecimal(1,invoicedValueDOC03);
            pstmt.setString(2,refPK.getCompanyCodeSys01DOC01());
            pstmt.setString(3,refPK.getDocTypeDOC01());
            pstmt.setBigDecimal(4,refPK.getDocYearDOC01());
            pstmt.setBigDecimal(5,refPK.getDocNumberDOC01());
            pstmt.setString(6,chargeVO.getChargeCodeSal06DOC03());
            pstmt.execute();
            pstmt.close();

          }
        } // end for (used to update invoice qtys in referred docs...)

        // retrieve document activities...
        gridParams = new GridParams();
        gridParams.getOtherGridParams().put(ApplicationConsts.SALE_DOC_PK,pk);
        res = rowsAction.loadSaleDocActivities(gridParams,serverLanguageId,username);
        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }

        BigDecimal valueDOC13,invoicedValueDOC13 = new BigDecimal(0);
        SaleDocActivityVO actVO = null;
        rows = new ArrayList(((VOListResponse)res).getRows());
        for(int i=0;i<rows.size();i++) {
          actVO = (SaleDocActivityVO)rows.get(i);
          pstmt = conn.prepareStatement(
            "select VALUE,INVOICED_VALUE from DOC13_SELLING_ACTIVITIES where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? and ACTIVITY_CODE_SAL09=?"
          );
          pstmt.setString(1,refPK.getCompanyCodeSys01DOC01());
          pstmt.setString(2,refPK.getDocTypeDOC01());
          pstmt.setBigDecimal(3,refPK.getDocYearDOC01());
          pstmt.setBigDecimal(4,refPK.getDocNumberDOC01());
          pstmt.setString(5,actVO.getActivityCodeSal09DOC13());
          rset = pstmt.executeQuery();
          if (rset.next()) {
            valueDOC13 = rset.getBigDecimal(1);
            invoicedValueDOC13 = rset.getBigDecimal(2);

            invoicedValueDOC13 = invoicedValueDOC03.add(actVO.getValueDOC13()).setScale(docVO.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP);
            if (invoicedValueDOC13.doubleValue()>valueDOC13.doubleValue())
              invoicedValueDOC13 = valueDOC13;

          }
          rset.next();
          pstmt.close();

          pstmt = conn.prepareStatement(
            "update DOC13_SELLING_ACTIVITIES set INVOICED_VALUE=? where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? and ACTIVITY_CODE_SAL09=?"
          );
          pstmt.setBigDecimal(1,invoicedValueDOC13);
          pstmt.setString(2,refPK.getCompanyCodeSys01DOC01());
          pstmt.setString(3,refPK.getDocTypeDOC01());
          pstmt.setBigDecimal(4,refPK.getDocYearDOC01());
          pstmt.setBigDecimal(5,refPK.getDocNumberDOC01());
          pstmt.setString(6,actVO.getActivityCodeSal09DOC13());
          pstmt.execute();
          pstmt.close();
        } // end for (used to update invoice qtys in referred docs...)

        // check if linked document can be closed...
        boolean canCloseLinkedDoc = true;
        pstmt = conn.prepareStatement(
          "select QTY from DOC02_SELLING_ITEMS where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? and QTY-INVOICE_QTY>0"
        );
        pstmt.setString(1,refPK.getCompanyCodeSys01DOC01());
        pstmt.setString(2,refPK.getDocTypeDOC01());
        pstmt.setBigDecimal(3,refPK.getDocYearDOC01());
        pstmt.setBigDecimal(4,refPK.getDocNumberDOC01());
        rset = pstmt.executeQuery();
        if (rset.next())
          canCloseLinkedDoc = false;
        rset.close();
        pstmt.close();

        if (canCloseLinkedDoc) {
          pstmt = conn.prepareStatement(
            "select VALUE from DOC13_SELLING_ACTIVITIES where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? and VALUE-INVOICED_VALUE>0"
          );
          pstmt.setString(1,refPK.getCompanyCodeSys01DOC01());
          pstmt.setString(2,refPK.getDocTypeDOC01());
          pstmt.setBigDecimal(3,refPK.getDocYearDOC01());
          pstmt.setBigDecimal(4,refPK.getDocNumberDOC01());
          rset = pstmt.executeQuery();
          if (rset.next())
            canCloseLinkedDoc = false;
          rset.close();
          pstmt.close();

          if (canCloseLinkedDoc) {
            pstmt = conn.prepareStatement(
              "select VALUE from DOC03_SELLING_CHARGES where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? and VALUE is not null and VALUE-INVOICED_VALUE>0"
            );
            pstmt.setString(1,refPK.getCompanyCodeSys01DOC01());
            pstmt.setString(2,refPK.getDocTypeDOC01());
            pstmt.setBigDecimal(3,refPK.getDocYearDOC01());
            pstmt.setBigDecimal(4,refPK.getDocNumberDOC01());
            rset = pstmt.executeQuery();
            if (rset.next())
              canCloseLinkedDoc = false;
            rset.close();
            pstmt.close();

            if (canCloseLinkedDoc) {
              // the linked document can be closed...
              pstmt = conn.prepareStatement("update DOC01_SELLING set DOC_STATE=? where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=?");
              pstmt.setString(1,ApplicationConsts.CLOSED);
              pstmt.setString(2,refPK.getCompanyCodeSys01DOC01());
              pstmt.setString(3,refPK.getDocTypeDOC01());
              pstmt.setBigDecimal(4,refPK.getDocYearDOC01());
              pstmt.setBigDecimal(5,refPK.getDocNumberDOC01());
              pstmt.execute();
            }
          }
        }

      } // end if (check if doc is of type SALE_INVOICE_FROM_DN_DOC_TYPE or SALE_INVOICE_FROM_SD_DOC_TYPE...)



      // generate progressive for doc. sequence...
      if (docVO.getDocTypeDOC01().equals(ApplicationConsts.SALE_INVOICE_DOC_TYPE) ||
          docVO.getDocTypeDOC01().equals(ApplicationConsts.SALE_INVOICE_FROM_DN_DOC_TYPE) ||
          docVO.getDocTypeDOC01().equals(ApplicationConsts.SALE_INVOICE_FROM_SD_DOC_TYPE)) {
        // invoice...
        pstmt = conn.prepareStatement(
          "select max(DOC_SEQUENCE) from DOC01_SELLING where COMPANY_CODE_SYS01=? and DOC_TYPE in (?,?,?) and DOC_YEAR=? and DOC_SEQUENCE is not null"
        );
        pstmt.setString(1,pk.getCompanyCodeSys01DOC01());
        pstmt.setString(2,ApplicationConsts.SALE_INVOICE_DOC_TYPE);
        pstmt.setString(3,ApplicationConsts.SALE_INVOICE_FROM_DN_DOC_TYPE);
        pstmt.setString(4,ApplicationConsts.SALE_INVOICE_FROM_SD_DOC_TYPE);
        pstmt.setBigDecimal(5,pk.getDocYearDOC01());

        Response sectRes = parsBean.loadCompanyParams(docVO.getCompanyCodeSys01DOC01(),serverLanguageId,username);
        if (!res.isError()) {
          CompanyParametersVO parsVO = (CompanyParametersVO)((VOResponse)sectRes).getVo();
          docVO.setSectionalDOC01(parsVO.getSaleSectionalDOC01());
        }

      }
      else {
        // other sale document (e.g. credit note)...
        pstmt = conn.prepareStatement(
          "select max(DOC_SEQUENCE) from DOC01_SELLING where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_SEQUENCE is not null"
        );
        pstmt.setString(1,pk.getCompanyCodeSys01DOC01());
        pstmt.setString(2,pk.getDocTypeDOC01());
        pstmt.setBigDecimal(3,pk.getDocYearDOC01());
      }
      rset = pstmt.executeQuery();
      int docSequenceDOC01 = 1;
      if (rset.next())
        docSequenceDOC01 = rset.getInt(1)+1;
      rset.close();
      pstmt.close();
      if (docVO.getDocSequenceDOC01() == null || docVO.getDocSequenceDOC01().intValue() == 0) {
        docVO.setDocSequenceDOC01(new BigDecimal(docSequenceDOC01));
      } else {
        docSequenceDOC01 = docVO.getDocSequenceDOC01().intValue();
      }


/*
      // create expirations in DOC19 ONLY if:
      // - there are more than one instalment OR
      // - there is only one instalment and this instalment has more than 0 instalment days
      if (paymentInstallments.size()>1 || (paymentInstallments.size()==1 && ((PaymentInstalmentVO)paymentInstallments.get(0)).getInstalmentDaysREG17().intValue()>0 )) {
*/


			HashMap map = new HashMap();
			map.put(ApplicationConsts.COMPANY_CODE_SYS01,docVO.getCompanyCodeSys01DOC01());
			map.put(ApplicationConsts.PARAM_CODE,ApplicationConsts.ROUNDING_PROCEEDS_CODE);
			res = parsBean.loadUserParam(map,serverLanguageId,username);
			if (res.isError()) {
				throw new Exception(res.getErrorMessage());
			}
			String roundingAccountCode = ((VOResponse)res).getVo().toString();

      // create ALWAYS expirations in DOC19...
      PaymentInstalmentVO inVO = null;
      pstmt = conn.prepareStatement(
        "insert into DOC19_EXPIRATIONS(COMPANY_CODE_SYS01,DOC_TYPE,DOC_YEAR,DOC_NUMBER,DOC_SEQUENCE,PROGRESSIVE,"+
				 "DOC_DATE,EXPIRATION_DATE,NAME_1,NAME_2,VALUE,DESCRIPTION,CUSTOMER_SUPPLIER_CODE,PROGRESSIVE_REG04,"+
				 "CURRENCY_CODE_REG03,PAYMENT_TYPE_CODE_REG11,PAYED,REAL_PAYMENT_TYPE_CODE_REG11,PAYED_DATE,PAYED_VALUE,REAL_ACCOUNT_CODE_ACC02,ROUNDING_ACCOUNT_CODE_ACC02,ALREADY_PAYED) "+
				 "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
      );
      long startTime = docVO.getDocDateDOC01().getTime(); // invoice date...
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
			BigDecimal progressiveDOC19 = null;

			pstmt3 = conn.prepareStatement(
				"INSERT INTO DOC27_PAYMENTS(COMPANY_CODE_SYS01,PROGRESSIVE,PAYMENT_DATE,PAYMENT_VALUE,CUSTOMER_SUPPLIER_CODE,"+
				"ACCOUNT_CODE_ACC02,PAYMENT_TYPE_CODE_REG11,CURRENCY_CODE_REG03,PROGRESSIVE_REG04) "+
				"VALUES (?,?,?,?,?,?,?,?,?)"
			);
			pstmt2 = conn.prepareStatement(
				"INSERT INTO DOC28_PAYMENT_DISTRIBUTION(COMPANY_CODE_SYS01,PROGRESSIVE_DOC27,PROGRESSIVE_DOC19,"+
				"PAYMENT_VALUE,PAYED) VALUES (?,?,?,?,?)"
			);

      for(int i=0;i<paymentInstallments.size();i++) {
				progressiveDOC19 = CompanyProgressiveUtils.getInternalProgressive(docVO.getCompanyCodeSys01DOC01(),"DOC19_EXPIRATIONS","PROGRESSIVE",conn);

        inVO = (PaymentInstalmentVO)paymentInstallments.get(i);
        pstmt.setString(1,docVO.getCompanyCodeSys01DOC01());
        pstmt.setString(2,docVO.getDocTypeDOC01());
        pstmt.setBigDecimal(3,docVO.getDocYearDOC01());
        pstmt.setBigDecimal(4,docVO.getDocNumberDOC01());
        pstmt.setBigDecimal(5,docVO.getDocSequenceDOC01());
        pstmt.setBigDecimal(6,progressiveDOC19);
        pstmt.setDate(7,docVO.getDocDateDOC01());
        pstmt.setDate(8,new java.sql.Date(startTime + inVO.getInstalmentDaysREG17().longValue()*86400*1000)); // expiration date
        pstmt.setString(9,docVO.getName_1REG04());
        pstmt.setString(10,docVO.getName_2REG04());

        if (docVO.getDocTypeDOC01().equals(ApplicationConsts.SALE_CREDIT_NOTE_DOC_TYPE))
          amount = docVO.getTotalDOC01().multiply(inVO.getPercentageREG17()).divide(new BigDecimal(-100),BigDecimal.ROUND_HALF_UP).setScale(docVO.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP); // value
        else
          amount = docVO.getTotalDOC01().multiply(inVO.getPercentageREG17()).divide(new BigDecimal(100),BigDecimal.ROUND_HALF_UP).setScale(docVO.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP); // value

        pstmt.setBigDecimal(11,CurrencyConversionUtils.convertCurrencyToCurrency(amount,conv));
				if (docVO.getDocTypeDOC01().equals(ApplicationConsts.SALE_DESK_DOC_TYPE))
					pstmt.setString(12,t3+" "+docVO.getDocSequenceDOC01()+"/"+docVO.getDocYearDOC01()+" - "+t8+" "+t9+" "+(i+1)+" - "+inVO.getPaymentTypeDescriptionSYS10()); // description
        else if (docVO.getDocTypeDOC01().equals(ApplicationConsts.SALE_CREDIT_NOTE_DOC_TYPE))
          pstmt.setString(12,t7+" "+docVO.getDocSequenceDOC01()+"/"+docVO.getDocYearDOC01()+" - "+t8+" "+t9+" "+(i+1)+" - "+inVO.getPaymentTypeDescriptionSYS10()); // description
        else
          pstmt.setString(12,t10+" "+docVO.getDocSequenceDOC01()+"/"+docVO.getDocYearDOC01()+" - "+t8+" "+t9+" "+(i+1)+" - "+inVO.getPaymentTypeDescriptionSYS10()); // description

        pstmt.setString(13,docVO.getCustomerCodeSAL07());
        pstmt.setBigDecimal(14,docVO.getProgressiveReg04DOC01());
        pstmt.setString(15,companyCurrencyCode);
				pstmt.setString(16,payVO.getPaymentTypeCodeReg11REG10());

	      if (pk.getDocTypeDOC01().equals(ApplicationConsts.SALE_DESK_DOC_TYPE)) {
					pstmt.setString(17,"Y");
					pstmt.setString(18,payVO.getPaymentTypeCodeReg11REG10());
					pstmt.setDate(19,new java.sql.Date(startTime + inVO.getInstalmentDaysREG17().longValue()*86400*1000));
					pstmt.setBigDecimal(20,CurrencyConversionUtils.convertCurrencyToCurrency(amount,conv));
					pstmt.setString(21,payVO.getAccountCodeAcc02REG11());
					pstmt.setString(22,roundingAccountCode);
					pstmt.setBigDecimal(23,CurrencyConversionUtils.convertCurrencyToCurrency(amount,conv));
        }
				else {
					pstmt.setString(17,"N");
					pstmt.setString(18,null);
					pstmt.setDate(19,null);
					pstmt.setBigDecimal(20,null);
					pstmt.setString(21,payVO.getAccountCodeAcc02REG11());
					pstmt.setString(22,roundingAccountCode);
					pstmt.setBigDecimal(23,new BigDecimal(0));
				}

        pstmt.execute();

	      if (pk.getDocTypeDOC01().equals(ApplicationConsts.SALE_DESK_DOC_TYPE)) {

					// insert record in DOC27...
					BigDecimal progressiveDOC27 = CompanyProgressiveUtils.getInternalProgressive(
						 docVO.getCompanyCodeSys01DOC01(),
						 "DOC27_PAYMENTS",
						 "PROGRESSIVE",
						 conn
					);
					pstmt3.setString(1,docVO.getCompanyCodeSys01DOC01());
					pstmt3.setBigDecimal(2,progressiveDOC27);
					pstmt3.setDate(3,docVO.getDocDateDOC01());
					pstmt3.setBigDecimal(4,docVO.getTotalDOC01());
					pstmt3.setString(5,docVO.getCustomerCodeSAL07());
					pstmt3.setString(6,payVO.getAccountCodeAcc02REG11());
					pstmt3.setString(7,docVO.getPaymentCodeReg10DOC01());
					pstmt3.setString(8,docVO.getCurrencyCodeReg03DOC01());
					pstmt3.setBigDecimal(9,docVO.getProgressiveReg04DOC01());
					pstmt3.execute();

					// insert record in DOC28...
					pstmt2.setString(1,docVO.getCompanyCodeSys01DOC01());
					pstmt2.setBigDecimal(2,progressiveDOC27);
					pstmt2.setBigDecimal(3,progressiveDOC19);
					pstmt2.setBigDecimal(4,docVO.getTotalDOC01());
					pstmt2.setString(5,"Y");
					pstmt2.execute();

				} // end if on sale desk




      }
      pstmt.close();
//      }



      // change doc state to close...
      pstmt = conn.prepareStatement("update DOC01_SELLING set DOC_STATE=?,DOC_SEQUENCE=?,SECTIONAL=? where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=?");
      pstmt.setString(1,ApplicationConsts.CLOSED);
      pstmt.setInt(2,docSequenceDOC01);
      pstmt.setString(3,docVO.getSectionalDOC01());
      pstmt.setString(4,pk.getCompanyCodeSys01DOC01());
      pstmt.setString(5,pk.getDocTypeDOC01());
      pstmt.setBigDecimal(6,pk.getDocYearDOC01());
      pstmt.setBigDecimal(7,pk.getDocNumberDOC01());
      int rowNum = pstmt.executeUpdate();



      // if the document is a retail sale doc then
      // export the document in a text file format and call an external application to manage it...
      if (docVO.getDocTypeDOC01().equals(ApplicationConsts.SALE_DESK_DOC_TYPE)) {
    	res = expAction.exportToFile(t11,t12,t13,t14,pk,docVO,itemRows,serverLanguageId,username);
        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }
      }



      // if the document is a sale invoice or a sale retail document or a credit note, then generate an accounting item...
      if (docVO.getDocTypeDOC01().equals(ApplicationConsts.SALE_INVOICE_FROM_DN_DOC_TYPE) ||
          docVO.getDocTypeDOC01().equals(ApplicationConsts.SALE_INVOICE_FROM_SD_DOC_TYPE) ||
          docVO.getDocTypeDOC01().equals(ApplicationConsts.SALE_INVOICE_DOC_TYPE) ||
          docVO.getDocTypeDOC01().equals(ApplicationConsts.SALE_CREDIT_NOTE_DOC_TYPE) ||
          docVO.getDocTypeDOC01().equals(ApplicationConsts.SALE_DESK_DOC_TYPE)) {
        JournalHeaderVO jhVO = new JournalHeaderVO();
        jhVO.setCompanyCodeSys01ACC05(docVO.getCompanyCodeSys01DOC01());
        if (docVO.getDocTypeDOC01().equals(ApplicationConsts.SALE_DESK_DOC_TYPE))
          jhVO.setAccountingMotiveCodeAcc03ACC05(ApplicationConsts.MOTIVE_SALE_RETAIL);
        else
          jhVO.setAccountingMotiveCodeAcc03ACC05(ApplicationConsts.MOTIVE_SALE_INVOICE);
        jhVO.setItemDateACC05(new java.sql.Date(System.currentTimeMillis()));
        jhVO.setItemYearACC05(new BigDecimal(Calendar.getInstance().get(Calendar.YEAR)));
        if (docVO.getDocTypeDOC01().equals(ApplicationConsts.SALE_DESK_DOC_TYPE))
          jhVO.setDescriptionACC05(
              t3+" "+docVO.getDocSequenceDOC01()+"/"+docVO.getDocYearDOC01()
          );
        else if (docVO.getDocTypeDOC01().equals(ApplicationConsts.SALE_CREDIT_NOTE_DOC_TYPE))
          jhVO.setDescriptionACC05(
              t4+" "+docVO.getDocSequenceDOC01()+"/"+docVO.getDocYearDOC01()+" - "+
              t5+" "+docVO.getName_1REG04()+" "+(docVO.getName_2REG04()==null?"":docVO.getName_2REG04())
          );
        else
          jhVO.setDescriptionACC05(
              t6+" "+docVO.getDocSequenceDOC01()+"/"+docVO.getDocYearDOC01()+" - "+
              t5+" "+docVO.getName_1REG04()+" "+(docVO.getName_2REG04()==null?"":docVO.getName_2REG04())
          );

        // determine account codes defined for the current customer...
        pstmt = conn.prepareStatement(
          "select CREDIT_ACCOUNT_CODE_ACC02,ITEMS_ACCOUNT_CODE_ACC02,ACTIVITIES_ACCOUNT_CODE_ACC02,CHARGES_ACCOUNT_CODE_ACC02 "+
          "from SAL07_CUSTOMERS where COMPANY_CODE_SYS01=? and PROGRESSIVE_REG04=?"
        );
        pstmt.setString(1,pk.getCompanyCodeSys01DOC01());
        pstmt.setBigDecimal(2,docVO.getProgressiveReg04DOC01());
        rset = pstmt.executeQuery();
        if (!rset.next()) {
          rset.close();
          throw new Exception("customer not found");
        }
        String creditAccountCodeAcc02 = rset.getString(1);
        String itemsAccountCodeAcc02 = rset.getString(2);
        String actAccountCodeAcc02 = rset.getString(3);
        String chargesAccountCodeAcc02 = rset.getString(4);
        rset.close();
        pstmt.close();

        // determine account code defined for vat in sale vat register...
        pstmt = conn.prepareStatement(
          "select ACCOUNT_CODE_ACC02 from ACC04_VAT_REGISTERS where COMPANY_CODE_SYS01=? and REGISTER_CODE=?"
        );
        pstmt.setString(1,pk.getCompanyCodeSys01DOC01());
        if (docVO.getDocTypeDOC01().equals(ApplicationConsts.SALE_DESK_DOC_TYPE))
          pstmt.setString(2,ApplicationConsts.VAT_REGISTER_RETAIL);
        else
          pstmt.setString(2,ApplicationConsts.VAT_REGISTER_SELLING);
        rset = pstmt.executeQuery();
        if (!rset.next()) {
          rset.close();
          throw new Exception("vat register not found");
        }
        String vatAccountCodeAcc02 = rset.getString(1);
        rset.close();
        pstmt.close();

        // calculate taxable income rows, grouped by vat code...
        res = taxableIncomeAction.updateTaxableIncomes(
            variant1Descriptions,
            variant2Descriptions,
            variant3Descriptions,
            variant4Descriptions,
            variant5Descriptions,
            pk, serverLanguageId, username);
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
          if (tVO.getRowType()==tVO.ITEM_ROW_TYPE) {
            jrVO.setAccountCodeAcc02ACC06(itemsAccountCodeAcc02);
            jrVO.setAccountCodeACC06(itemsAccountCodeAcc02);
          }
          else if (tVO.getRowType()==tVO.ACTIVITY_ROW_TYPE) {
            jrVO.setAccountCodeAcc02ACC06(actAccountCodeAcc02);
            jrVO.setAccountCodeACC06(actAccountCodeAcc02);
          }
          else if (tVO.getRowType()==tVO.CHARGE_ROW_TYPE) {
            jrVO.setAccountCodeAcc02ACC06(chargesAccountCodeAcc02);
            jrVO.setAccountCodeACC06(chargesAccountCodeAcc02);
          }
          jrVO.setAccountCodeTypeACC06(ApplicationConsts.ACCOUNT_TYPE_ACCOUNT);

          if (docVO.getDocTypeDOC01().equals(ApplicationConsts.SALE_CREDIT_NOTE_DOC_TYPE))
            jrVO.setCreditAmountACC06(tVO.getTaxableIncome().negate());
          else
            jrVO.setCreditAmountACC06(tVO.getTaxableIncome());
          jrVO.setCreditAmountACC06(CurrencyConversionUtils.convertCurrencyToCurrency(jrVO.getCreditAmountACC06(),conv));

          jrVO.setDescriptionACC06("");
          jrVO.setItemYearAcc05ACC06(jhVO.getItemYearACC05());
          jrVO.setProgressiveAcc05ACC06(jhVO.getProgressiveACC05());
          jhVO.addJournalRow(jrVO);

          // prepare vat row for sale vat register...
          vatVO = (VatRowVO)vats.get(tVO.getVatCode());
          if (vatVO==null) {
            vatVO = new VatRowVO();
            vatVO.setCompanyCodeSys01ACC07(docVO.getCompanyCodeSys01DOC01());
            if (docVO.getDocTypeDOC01().equals(ApplicationConsts.SALE_DESK_DOC_TYPE))
              vatVO.setRegisterCodeAcc04ACC07(ApplicationConsts.VAT_REGISTER_RETAIL);
            else
              vatVO.setRegisterCodeAcc04ACC07(ApplicationConsts.VAT_REGISTER_SELLING);
            vatVO.setTaxableIncomeACC07(tVO.getTaxableIncome());
            vatVO.setTaxableIncomeACC07(CurrencyConversionUtils.convertCurrencyToCurrency(vatVO.getTaxableIncomeACC07(),conv));
            vatVO.setVatCodeACC07(tVO.getVatCode());
            vatVO.setVatDateACC07(new java.sql.Date(System.currentTimeMillis()));
            vatVO.setVatDescriptionACC07(tVO.getVatDescription());

            if (docVO.getDocTypeDOC01().equals(ApplicationConsts.SALE_CREDIT_NOTE_DOC_TYPE))
              vatVO.setVatValueACC07(tVO.getVatValue().negate());
            else
              vatVO.setVatValueACC07(tVO.getVatValue());
            vatVO.setVatValueACC07(CurrencyConversionUtils.convertCurrencyToCurrency(vatVO.getVatValueACC07(),conv));

            vatVO.setVatYearACC07(new BigDecimal(Calendar.getInstance().get(Calendar.YEAR)));
          }
          else {
            vatVO.setTaxableIncomeACC07(vatVO.getTaxableIncomeACC07().add(tVO.getTaxableIncome()));
            vatVO.setTaxableIncomeACC07(CurrencyConversionUtils.convertCurrencyToCurrency(vatVO.getTaxableIncomeACC07(),conv));
            if (docVO.getDocTypeDOC01().equals(ApplicationConsts.SALE_CREDIT_NOTE_DOC_TYPE))
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

        if (docVO.getDocTypeDOC01().equals(ApplicationConsts.SALE_CREDIT_NOTE_DOC_TYPE))
          jrVO.setCreditAmountACC06(totalVat.negate());
        else
          jrVO.setCreditAmountACC06(totalVat);
        jrVO.setCreditAmountACC06(CurrencyConversionUtils.convertCurrencyToCurrency(jrVO.getCreditAmountACC06(),conv));

        jrVO.setDescriptionACC06("");
        jrVO.setItemYearAcc05ACC06(jhVO.getItemYearACC05());
        jrVO.setProgressiveAcc05ACC06(jhVO.getProgressiveACC05());
        jhVO.addJournalRow(jrVO);

        // add total credit value to the accounting item...
        jrVO = new JournalRowVO();
        jrVO.setCompanyCodeSys01ACC06(jhVO.getCompanyCodeSys01ACC05());
        jrVO.setAccountCodeAcc02ACC06(creditAccountCodeAcc02);
        jrVO.setAccountCodeACC06(docVO.getCustomerCodeSAL07());
        jrVO.setAccountCodeTypeACC06(ApplicationConsts.ACCOUNT_TYPE_CUSTOMER);

        if (docVO.getDocTypeDOC01().equals(ApplicationConsts.SALE_CREDIT_NOTE_DOC_TYPE))
          jrVO.setDebitAmountACC06(docVO.getTotalDOC01().negate());
        else
          jrVO.setDebitAmountACC06(docVO.getTotalDOC01());
        jrVO.setDebitAmountACC06(CurrencyConversionUtils.convertCurrencyToCurrency(jrVO.getDebitAmountACC06(),conv));

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
        // create an item registration for proceeds, according to expiration settings (e.g. retail selling):
        // there must be only one instalment and this instalment has 0 instalment days
		    if (paymentInstallments.size()==1 && ((PaymentInstalmentVO)paymentInstallments.get(0)).getInstalmentDaysREG17().intValue()==0) {
*/

       	if (pk.getDocTypeDOC01().equals(ApplicationConsts.SALE_DESK_DOC_TYPE)) {

          map = new HashMap();
          map.put(ApplicationConsts.COMPANY_CODE_SYS01,docVO.getCompanyCodeSys01DOC01());
          map.put(ApplicationConsts.PARAM_CODE,ApplicationConsts.CASE_ACCOUNT);
          res = parsBean.loadUserParam(map,serverLanguageId,username);
          if (res.isError()) {
            throw new Exception(res.getErrorMessage());
          }
          String caseAccountCode = ((VOResponse)res).getVo().toString();


          jhVO = new JournalHeaderVO();
          jhVO.setCompanyCodeSys01ACC05(docVO.getCompanyCodeSys01DOC01());
          if (docVO.getDocTypeDOC01().equals(ApplicationConsts.SALE_DESK_DOC_TYPE)) {
            jhVO.setDescriptionACC05(
                t3+" "+docVO.getDocSequenceDOC01()+"/"+docVO.getDocYearDOC01()
            );
            jhVO.setAccountingMotiveCodeAcc03ACC05(ApplicationConsts.MOTIVE_SALE_RETAIL_PROCEEDS);
          }
          else if (docVO.getDocTypeDOC01().equals(ApplicationConsts.SALE_CREDIT_NOTE_DOC_TYPE)) {
            jhVO.setAccountingMotiveCodeAcc03ACC05(ApplicationConsts.MOTIVE_INVOICE_PROCEEDS);
            jhVO.setDescriptionACC05(
                t4+" "+docVO.getDocSequenceDOC01()+"/"+docVO.getDocYearDOC01()+" - "+
                t5+" "+docVO.getName_1REG04()+" "+(docVO.getName_2REG04()==null?"":docVO.getName_2REG04())
            );
          }
          else {
            jhVO.setAccountingMotiveCodeAcc03ACC05(ApplicationConsts.MOTIVE_INVOICE_PROCEEDS);
            jhVO.setDescriptionACC05(
                t6+" "+docVO.getDocSequenceDOC01()+"/"+docVO.getDocYearDOC01()+" - "+
                t5+" "+docVO.getName_1REG04()+" "+(docVO.getName_2REG04()==null?"":docVO.getName_2REG04())
            );
          }

          jhVO.setItemDateACC05(new java.sql.Date(System.currentTimeMillis()));
          jhVO.setItemYearACC05(new BigDecimal(Calendar.getInstance().get(Calendar.YEAR)));

          jrVO = new JournalRowVO();
          jrVO.setCompanyCodeSys01ACC06(jhVO.getCompanyCodeSys01ACC05());
          jrVO.setAccountCodeAcc02ACC06(creditAccountCodeAcc02);
          jrVO.setAccountCodeACC06(docVO.getCustomerCodeSAL07());
          jrVO.setAccountCodeTypeACC06(ApplicationConsts.ACCOUNT_TYPE_CUSTOMER);

          if (docVO.getDocTypeDOC01().equals(ApplicationConsts.SALE_CREDIT_NOTE_DOC_TYPE))
            jrVO.setCreditAmountACC06(docVO.getTotalDOC01().negate());
          else
            jrVO.setCreditAmountACC06(docVO.getTotalDOC01());
          jrVO.setCreditAmountACC06(CurrencyConversionUtils.convertCurrencyToCurrency(jrVO.getCreditAmountACC06(),conv));

          jrVO.setDescriptionACC06("");
          jrVO.setItemYearAcc05ACC06(jhVO.getItemYearACC05());
          jrVO.setProgressiveAcc05ACC06(jhVO.getProgressiveACC05());
          jhVO.addJournalRow(jrVO);

          jrVO = new JournalRowVO();
          jrVO.setCompanyCodeSys01ACC06(jhVO.getCompanyCodeSys01ACC05());
          jrVO.setAccountCodeAcc02ACC06(caseAccountCode);
          jrVO.setAccountCodeACC06(caseAccountCode);
          jrVO.setAccountCodeTypeACC06(ApplicationConsts.ACCOUNT_TYPE_ACCOUNT);

          if (docVO.getDocTypeDOC01().equals(ApplicationConsts.SALE_CREDIT_NOTE_DOC_TYPE))
            jrVO.setDebitAmountACC06(docVO.getTotalDOC01().negate());
          else
            jrVO.setDebitAmountACC06(docVO.getTotalDOC01());
          jrVO.setDebitAmountACC06(CurrencyConversionUtils.convertCurrencyToCurrency(jrVO.getDebitAmountACC06(),conv));

          jrVO.setDescriptionACC06("");
          jrVO.setItemYearAcc05ACC06(jhVO.getItemYearACC05());
          jrVO.setProgressiveAcc05ACC06(jhVO.getProgressiveACC05());
          jhVO.addJournalRow(jrVO);

          res = insJornalItemAction.insertJournalItem(jhVO,serverLanguageId,username);
          if (res.isError()) {
            throw new Exception(res.getErrorMessage());
          }
        }

      }

      return new VOResponse(new BigDecimal(docSequenceDOC01));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while closing a sale document",ex);
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
				pstmt2.close();
			}
			catch (Exception ex2) {
			}
			try {
				pstmt3.close();
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
        availAction.setConn(null);
        movBean.setConn(null);
        expAction.setConn(null);
        payAction.setConn(null);
        insJornalItemAction.setConn(null);
        taxableIncomeAction.setConn(null);
        vatRegisterAction.setConn(null);
        parsBean.setConn(null);
				loadSaleDocRowBean.setConn(null);
      } catch (Exception ex) {}
    }
  }



}

