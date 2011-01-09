package org.jallinone.sales.documents.server;

import org.openswing.swing.server.*;
import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;
import java.math.*;
import org.jallinone.system.progressives.server.*;
import org.jallinone.system.translations.server.*;
import org.jallinone.commons.server.*;
import org.jallinone.system.java.*;
import org.jallinone.commons.java.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.sales.documents.java.*;
import org.jallinone.sales.documents.server.*;
import org.jallinone.sales.documents.headercharges.server.*;
import org.jallinone.sales.documents.headerdiscounts.server.*;
import org.jallinone.sales.documents.itemdiscounts.server.*;
import org.jallinone.sales.documents.activities.server.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.*;
import org.openswing.swing.internationalization.server.*;
import org.openswing.swing.internationalization.java.*;
import org.jallinone.sales.customers.server.*;
import org.jallinone.sales.customers.java.*;
import org.jallinone.sales.documents.itemdiscounts.java.*;
import org.jallinone.sales.documents.headercharges.java.*;
import org.jallinone.sales.documents.activities.java.*;
import org.jallinone.sales.documents.headerdiscounts.java.*;
import org.jallinone.sales.documents.invoices.java.*;
import org.jallinone.sales.documents.invoices.java.*;
import org.jallinone.sales.documents.activities.server.*;
import org.jallinone.sales.documents.headerdiscounts.server.*;
import org.jallinone.sales.documents.itemdiscounts.server.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.jallinone.variants.java.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to create a sale invoice from a set of out delivery notes.
 * Item rows and discounts and charges and activities are added to the invoice only if they are not yet added in previous closed invoices.</p>
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
public class CreateInvoiceFromOutDelivNotesBean  implements CreateInvoiceFromOutDelivNotes {


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

  private LoadSaleDocBean loadSaleDocBean;

  public void setLoadSaleDocBean(LoadSaleDocBean loadSaleDocBean) {
	  this.loadSaleDocBean = loadSaleDocBean;
  }

  private SaleDocsBean docAction;

  public void setDocAction(SaleDocsBean docAction) {
    this.docAction = docAction;
  }

  private CustomersBean custAction;

  public void setCustAction(CustomersBean custAction) {
    this.custAction = custAction;
  }

  private SaleDocChargesBean chargesAction;

  public void setChargesAction(SaleDocChargesBean chargesAction) {
    this.chargesAction = chargesAction;
  }

  private SaleDocActivitiesBean actAction;

  public void setActAction(SaleDocActivitiesBean actAction) {
    this.actAction = actAction;
  }

  private SaleDocDiscountsBean discAction;

  public void setDiscAction(SaleDocDiscountsBean discAction) {
    this.discAction = discAction;
  }

  private SaleDocRowDiscountsBean itemDiscAction;

  public void setItemDiscAction(SaleDocRowDiscountsBean itemDiscAction) {
    this.itemDiscAction = itemDiscAction;
  }



  private UpdateTaxableIncomesBean totals;

  public void setTotals(UpdateTaxableIncomesBean totals) {
    this.totals = totals;
  }

  private InsertSaleItemBean bean;

  public void setBean(InsertSaleItemBean bean) {
	  this.bean = bean;
  }

  private InsertSaleDocRowDiscountBean insBean;

  public void setInsBean(InsertSaleDocRowDiscountBean insBean) {
	  this.insBean = insBean;
  }


	private LoadSaleDocRowBean loadSaleDocRowBean;

	public void setLoadSaleDocRowBean(LoadSaleDocRowBean loadSaleDocRowBean) {
		this.loadSaleDocRowBean = loadSaleDocRowBean;
	}



  public CreateInvoiceFromOutDelivNotesBean() {}



  /**
   * Business logic to execute.
   */
  public VOResponse createInvoiceFromOutDelivNotes(
      HashMap variant1Descriptions,
      HashMap variant2Descriptions,
      HashMap variant3Descriptions,
      HashMap variant4Descriptions,
      HashMap variant5Descriptions,
      SaleInvoiceFromDelivNotesVO invoiceVO, String serverLanguageId,
      String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      rowsAction.setConn(conn); // use same transaction...
      docAction.setConn(conn); // use same transaction...
      custAction.setConn(conn); // use same transaction...
      chargesAction.setConn(conn); // use same transaction...
      actAction.setConn(conn); // use same transaction...
      discAction.setConn(conn); // use same transaction...
      itemDiscAction.setConn(conn); // use same transaction...
      totals.setConn(conn); // use same transaction...
      bean.setConn(conn);
      loadSaleDocBean.setConn(conn);
      insBean.setConn(conn);
			loadSaleDocRowBean.setConn(conn);

      DetailSaleDocVO docVO = invoiceVO.getDocVO();
      ArrayList delivNotes = invoiceVO.getSelectedDeliveryNotes();

      // insert header invoice...
      docVO.setDocStateDOC01(ApplicationConsts.HEADER_BLOCKED);
      Response res = docAction.insertSaleDoc(docVO,serverLanguageId,username,docVO.getCompanyCodeSys01DOC01(),new ArrayList());
      if (res.isError()) {
        throw new Exception(res.getErrorMessage());
      }

      SaleDocPK refPK = new SaleDocPK(
        docVO.getCompanyCodeSys01Doc01DOC01(),
        docVO.getDocTypeDoc01DOC01(),
        docVO.getDocYearDoc01DOC01(),
        docVO.getDocNumberDoc01DOC01()
      );


			// retrieve deliv.note requests related to the specified sale doc..
			pstmt = conn.prepareStatement(
				"select DOC_YEAR,DOC_NUMBER from DOC01_SELLING WHERE COMPANY_CODE_SYS01=? AND DOC_TYPE_DOC01=? AND DOC_YEAR_DOC01=? AND DOC_NUMBER_DOC01=?"
			);
			pstmt.setString(1,docVO.getCompanyCodeSys01DOC01()); // starting sale doc...
			pstmt.setString(2,docVO.getDocTypeDoc01DOC01());
			pstmt.setBigDecimal(3,docVO.getDocYearDoc01DOC01());
			pstmt.setBigDecimal(4,docVO.getDocNumberDoc01DOC01());
			ResultSet rset = pstmt.executeQuery();
			String where = "";
			while(rset.next()) {
				where +=
					" DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_YEAR_DOC01="+rset.getInt(1)+" and "+
					" DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_NUMBER_DOC01="+rset.getInt(2)+" or ";
			}
			rset.close();
			pstmt.close();
			if (where.length()==0) {
				throw new Exception("Invalid documents");
			}
			where = " and ("+where.substring(0,where.length()-3)+") ";


      // retrieve the list of items referred by the selected delivery notes...
      Hashtable selectedItems = new Hashtable(); // collection of pairs <itemcode,qty>
      BigDecimal qty = null;
      pstmt = conn.prepareStatement(
        "select DOC10_OUT_DELIVERY_NOTE_ITEMS.QTY,DOC10_OUT_DELIVERY_NOTE_ITEMS.ITEM_CODE_ITM01,"+
        "VARIANT_TYPE_ITM06,VARIANT_CODE_ITM11, "+
        "VARIANT_TYPE_ITM07,VARIANT_CODE_ITM12, "+
        "VARIANT_TYPE_ITM08,VARIANT_CODE_ITM13, "+
        "VARIANT_TYPE_ITM09,VARIANT_CODE_ITM14, "+
        "VARIANT_TYPE_ITM10,VARIANT_CODE_ITM15  "+
        "from DOC10_OUT_DELIVERY_NOTE_ITEMS where "+
        "DOC10_OUT_DELIVERY_NOTE_ITEMS.COMPANY_CODE_SYS01=? and "+
        "DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_TYPE=? and "+
        "DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_YEAR=? and "+
        "DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_NUMBER=? and "+
				"DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_TYPE_DOC01=?  "+ // deliv. req. note
				where
      );
      OutDeliveryNotesVO delivVO = null;
      VariantItemPK variantItemPK = null;
      for(int i=0;i<delivNotes.size();i++) {
        delivVO = (OutDeliveryNotesVO)delivNotes.get(i);
        pstmt.setString(1,delivVO.getCompanyCodeSys01DOC08());
        pstmt.setString(2,delivVO.getDocTypeDOC08());
        pstmt.setBigDecimal(3,delivVO.getDocYearDOC08());
        pstmt.setBigDecimal(4,delivVO.getDocNumberDOC08());
        pstmt.setString(5,ApplicationConsts.DELIVERY_REQUEST_DOC_TYPE);
        rset = pstmt.executeQuery();
        while(rset.next()) {

          variantItemPK = new VariantItemPK(
            delivVO.getCompanyCodeSys01DOC08(),
            rset.getString(2),
            rset.getString(3),
            rset.getString(4),
            rset.getString(5),
            rset.getString(6),
            rset.getString(7),
            rset.getString(8),
            rset.getString(9),
            rset.getString(10),
            rset.getString(11),
            rset.getString(12)
          );

          qty = rset.getBigDecimal(1);
          if (selectedItems.contains(variantItemPK))
            qty = qty.add( (BigDecimal)selectedItems.get(variantItemPK) );
          selectedItems.put(variantItemPK,qty);
        }
        rset.close();
      }



      // retrieve ref. document item rows...
      GridParams gridParams = new GridParams();
      gridParams.getOtherGridParams().put(ApplicationConsts.SALE_DOC_PK,refPK);
      res = rowsAction.loadSaleDocRows(variant1Descriptions,variant2Descriptions,variant3Descriptions,variant4Descriptions,variant5Descriptions,gridParams,serverLanguageId,username);
      if (res.isError()) {
        throw new Exception(res.getErrorMessage());
      }
      java.util.List rows = ((VOListResponse)res).getRows();



      // create invoice items rows..
      GridSaleDocRowVO gridRowVO = null;
      DetailSaleDocRowVO rowVO = null;
      java.util.List discRows = null;
      SaleDocRowPK docRowPK = null;
      SaleItemDiscountVO itemDiscVO = null;
      gridParams = new GridParams();
      for(int i=0;i<rows.size();i++) {
        gridRowVO = (GridSaleDocRowVO)rows.get(i);

        // retrieve row detail...
        docRowPK = new SaleDocRowPK(
            gridRowVO.getCompanyCodeSys01DOC02(),
            gridRowVO.getDocTypeDOC02(),
            gridRowVO.getDocYearDOC02(),
            gridRowVO.getDocNumberDOC02(),
            gridRowVO.getItemCodeItm01DOC02(),
            gridRowVO.getVariantTypeItm06DOC02(),
            gridRowVO.getVariantCodeItm11DOC02(),
            gridRowVO.getVariantTypeItm07DOC02(),
            gridRowVO.getVariantCodeItm12DOC02(),
            gridRowVO.getVariantTypeItm08DOC02(),
            gridRowVO.getVariantCodeItm13DOC02(),
            gridRowVO.getVariantTypeItm09DOC02(),
            gridRowVO.getVariantCodeItm14DOC02(),
            gridRowVO.getVariantTypeItm10DOC02(),
            gridRowVO.getVariantCodeItm15DOC02()

        );
        res = loadSaleDocRowBean.loadSaleDocRow(variant1Descriptions,variant2Descriptions,variant3Descriptions,variant4Descriptions,variant5Descriptions,docRowPK,serverLanguageId,username);
        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }
        rowVO = (DetailSaleDocRowVO)((VOResponse)res).getVo();

        // check if the row to be inserted is in the selected delivery notes...
        if (!selectedItems.containsKey(getVariantItemPK(rowVO)))
          continue;

        rowVO.setDocTypeDOC02(docVO.getDocTypeDOC01());
        rowVO.setDocNumberDOC02(docVO.getDocNumberDOC01());

        if (rowVO.getInvoiceQtyDOC02().doubleValue()<rowVO.getQtyDOC02().doubleValue()) {

          // this is the invoice qty to set
          qty = (BigDecimal)selectedItems.get(getVariantItemPK(rowVO));

          // check if the invoice qty is less or equals to max invoice qty...
          if (qty.doubleValue()<=rowVO.getQtyDOC02().subtract(rowVO.getInvoiceQtyDOC02()).doubleValue())
            rowVO.setQtyDOC02(qty);
          else
            rowVO.setQtyDOC02(rowVO.getQtyDOC02().subtract(rowVO.getInvoiceQtyDOC02().setScale(rowVO.getDecimalsReg02DOC02().intValue(),BigDecimal.ROUND_HALF_UP)));

          rowVO.setTaxableIncomeDOC02(rowVO.getQtyDOC02().multiply(rowVO.getValueSal02DOC02()).setScale(docVO.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP));
          rowVO.setTotalDiscountDOC02( new BigDecimal(0) );

          // calculate row vat...
          double vatPerc = rowVO.getValueReg01DOC02().doubleValue()*(1d-rowVO.getDeductibleReg01DOC02().doubleValue()/100d)/100;
          rowVO.setVatValueDOC02(rowVO.getTaxableIncomeDOC02().multiply(new BigDecimal(vatPerc)).setScale(docVO.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP));

          // calculate row total...
          rowVO.setValueDOC02(rowVO.getTaxableIncomeDOC02().add(rowVO.getVatValueDOC02()));

          res = bean.insertSaleItem(
						variant1Descriptions,
						variant2Descriptions,
						variant3Descriptions,
						variant4Descriptions,
						variant5Descriptions,
				    rowVO, serverLanguageId,username
				  );
          if (res.isError()) {
            throw new Exception(res.getErrorMessage());
          }

          // create item discounts...
          gridParams.getOtherGridParams().put(ApplicationConsts.SALE_DOC_ROW_PK,docRowPK);
          res = itemDiscAction.loadSaleDocRowDiscounts(gridParams,serverLanguageId,username);
          if (res.isError()) {
            throw new Exception(res.getErrorMessage());
          }
          discRows = ((VOListResponse)res).getRows();
          for(int j=0;j<discRows.size();j++) {
            itemDiscVO = (SaleItemDiscountVO)discRows.get(j);
            itemDiscVO.setDocTypeDOC04(docVO.getDocTypeDOC01());
            itemDiscVO.setDocNumberDOC04(docVO.getDocNumberDOC01());
            res = insBean.insertSaleDocRowDiscount(itemDiscVO,serverLanguageId,username);
            if (res.isError()) {
              throw new Exception(res.getErrorMessage());
            }
          }

        }
      }



      // create charges...
      gridParams = new GridParams();
      gridParams.getOtherGridParams().put(ApplicationConsts.SALE_DOC_PK,refPK);
      res = rowsAction.loadSaleDocCharges(gridParams,serverLanguageId,username);
      SaleDocChargeVO chargeVO = null;
      if (res.isError()) {
        throw new Exception(res.getErrorMessage());
      }
      rows = ((VOListResponse)res).getRows();
      for(int i=0;i<rows.size();i++) {
        chargeVO = (SaleDocChargeVO)rows.get(i);
        chargeVO.setDocTypeDOC03(docVO.getDocTypeDOC01());
        chargeVO.setDocNumberDOC03(docVO.getDocNumberDOC01());
        if (chargeVO.getValueDOC03()==null ||
            chargeVO.getValueDOC03()!=null && chargeVO.getInvoicedValueDOC03().doubleValue()<chargeVO.getValueDOC03().doubleValue()) {
          if (chargeVO.getValueDOC03()!=null)
            chargeVO.setValueDOC03(chargeVO.getValueDOC03().subtract(chargeVO.getInvoicedValueDOC03()).setScale(docVO.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP));

          res = chargesAction.insertSaleDocCharge(chargeVO,username);
          if (res.isError()) {
            throw new Exception(res.getErrorMessage());
          }
        }
      }



      // create activities...
      res = rowsAction.loadSaleDocActivities(gridParams,serverLanguageId,username);
      if (res.isError()) {
        throw new Exception(res.getErrorMessage());
      }
      SaleDocActivityVO actVO = null;
      rows = ((VOListResponse)res).getRows();
      for(int i=0;i<rows.size();i++) {
        actVO = (SaleDocActivityVO)rows.get(i);
        actVO.setDocTypeDOC13(docVO.getDocTypeDOC01());
        actVO.setDocNumberDOC13(docVO.getDocNumberDOC01());
        if (actVO.getInvoicedValueDOC13().doubleValue()<actVO.getValueDOC13().doubleValue()) {
          actVO.setValueDOC13(actVO.getValueDOC13().subtract(actVO.getInvoicedValueDOC13()).setScale(docVO.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP));
          res = actAction.insertSaleActivity(actVO,username);
          if (res.isError()) {
            throw new Exception(res.getErrorMessage());
          }
        }
      }



      // create header discounts...
      res = rowsAction.loadSaleDocDiscounts(gridParams,serverLanguageId,username);
      if (res.isError()) {
        throw new Exception(res.getErrorMessage());
      }
      SaleDocDiscountVO discVO = null;
      rows = ((VOListResponse)res).getRows();
      for(int i=0;i<rows.size();i++) {
        discVO = (SaleDocDiscountVO)rows.get(i);
        discVO.setDocTypeDOC05(docVO.getDocTypeDOC01());
        discVO.setDocNumberDOC05(docVO.getDocNumberDOC01());
        res = discAction.insertSaleDocDiscount(discVO,username);
        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }
      }



      // recalculate all taxable incomes, vats, totals...
      SaleDocPK pk = new SaleDocPK(
        docVO.getCompanyCodeSys01DOC01(),
        docVO.getDocTypeDOC01(),
        docVO.getDocYearDOC01(),
        docVO.getDocNumberDOC01()
      );
      res = totals.updateTaxableIncomes(
          variant1Descriptions,
          variant2Descriptions,
          variant3Descriptions,
          variant4Descriptions,
          variant5Descriptions,
          pk, serverLanguageId, username);
      if (res.isError()) {
        throw new Exception(res.getErrorMessage());
      }



      // reload doc header with updated totals...
      return new VOResponse(loadSaleDocBean.loadSaleDoc(pk,serverLanguageId,username,new ArrayList()));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while creating a sale invoice from a set of out delivery notes",ex);
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
          custAction.setConn(null);
          chargesAction.setConn(null);
          actAction.setConn(null);
          discAction.setConn(null);
          itemDiscAction.setConn(null);
          totals.setConn(null);
          bean.setConn(null);
          loadSaleDocBean.setConn(null);
          insBean.setConn(null);
					loadSaleDocRowBean.setConn(conn);
        } catch (Exception ex) {}

    }
  }


  private VariantItemPK getVariantItemPK(DetailSaleDocRowVO vo) {
    return new VariantItemPK(
      vo.getCompanyCodeSys01DOC02(),
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
    );
  }


}

