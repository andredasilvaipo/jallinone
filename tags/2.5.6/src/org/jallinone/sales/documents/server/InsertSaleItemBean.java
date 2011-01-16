package org.jallinone.sales.documents.server;

import org.openswing.swing.server.*;

import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.sales.documents.java.*;
import org.jallinone.system.progressives.server.CompanyProgressiveUtils;
import org.jallinone.system.server.*;
import org.jallinone.variants.java.VariantNameVO;
import org.jallinone.variants.java.VariantsMatrixColumnVO;
import org.jallinone.variants.java.VariantsMatrixRowVO;
import org.jallinone.variants.java.VariantsMatrixUtils;
import org.jallinone.variants.java.VariantsMatrixVO;

import java.math.*;

import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage sales order rows.</p>
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
public class InsertSaleItemBean implements InsertSaleItem {



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

		private UpdateTaxableIncomesBean totals;

		public void setTotals(UpdateTaxableIncomesBean totals) {
			this.totals = totals;
		}


	  /**
	   * Insert new item row in DOC02 table.
	   */
	  public VOResponse insertSaleItem(
 		  HashMap variant1Descriptions,
			HashMap variant2Descriptions,
			HashMap variant3Descriptions,
			HashMap variant4Descriptions,
			HashMap variant5Descriptions,
		  DetailSaleDocRowVO vo,String serverLanguageId,String username
		) throws Throwable {
	    PreparedStatement pstmt = null;
	    Connection conn = null;
	    try {
	      if (this.conn==null) conn = getConn(); else conn = this.conn;
				totals.setConn(conn); // use same transaction...


	      if (vo.getOutQtyDOC02()==null)
	        vo.setOutQtyDOC02(new BigDecimal(0));
	      if (vo.getQtyDOC02()==null)
	        vo.setQtyDOC02(new BigDecimal(0));
	      if (vo.getInvoiceQtyDOC02()==null)
	        vo.setInvoiceQtyDOC02(new BigDecimal(0));

				if ((vo.getDocTypeDOC02().equals(ApplicationConsts.SALE_CONTRACT_DOC_TYPE) ||
						 vo.getDocTypeDOC02().equals(ApplicationConsts.SALE_ORDER_DOC_TYPE) ||
						 vo.getDocTypeDOC02().equals(ApplicationConsts.SALE_DESK_DOC_TYPE) ||
						 vo.getDocTypeDOC02().equals(ApplicationConsts.SALE_ESTIMATE_DOC_TYPE)) &&
						Boolean.TRUE.equals(vo.getNoWarehouseMovITM01()))
					vo.setOutQtyDOC02(vo.getQtyDOC02());

				if (vo.getVariantCodeItm11DOC02()==null)
					vo.setVariantCodeItm11DOC02("*");
				if (vo.getVariantCodeItm12DOC02()==null)
					vo.setVariantCodeItm12DOC02("*");
				if (vo.getVariantCodeItm13DOC02()==null)
					vo.setVariantCodeItm13DOC02("*");
				if (vo.getVariantCodeItm14DOC02()==null)
					vo.setVariantCodeItm14DOC02("*");
				if (vo.getVariantCodeItm15DOC02()==null)
					vo.setVariantCodeItm15DOC02("*");
				if (vo.getVariantTypeItm06DOC02()==null)
					vo.setVariantTypeItm06DOC02("*");
				if (vo.getVariantTypeItm07DOC02()==null)
					vo.setVariantTypeItm07DOC02("*");
				if (vo.getVariantTypeItm08DOC02()==null)
					vo.setVariantTypeItm08DOC02("*");
				if (vo.getVariantTypeItm09DOC02()==null)
					vo.setVariantTypeItm09DOC02("*");
				if (vo.getVariantTypeItm10DOC02()==null)
					vo.setVariantTypeItm10DOC02("*");


	      Map attribute2dbField = new HashMap();
	      attribute2dbField.put("companyCodeSys01DOC02","COMPANY_CODE_SYS01");
	      attribute2dbField.put("docTypeDOC02","DOC_TYPE");
	      attribute2dbField.put("docYearDOC02","DOC_YEAR");
	      attribute2dbField.put("docNumberDOC02","DOC_NUMBER");
	      attribute2dbField.put("rowNumberDOC02","ROW_NUMBER");
	      attribute2dbField.put("itemCodeItm01DOC02","ITEM_CODE_ITM01");
	      attribute2dbField.put("vatCodeItm01DOC02","VAT_CODE_ITM01");
	      attribute2dbField.put("valueSal02DOC02","VALUE_SAL02");
	      attribute2dbField.put("valueDOC02","VALUE");
	      attribute2dbField.put("qtyDOC02","QTY");
	      attribute2dbField.put("totalDiscountDOC02","TOTAL_DISCOUNT");
	      attribute2dbField.put("vatValueDOC02","VAT_VALUE");

	      attribute2dbField.put("vatDescriptionDOC02","VAT_DESCRIPTION");
	      attribute2dbField.put("startDateSal02DOC02","START_DATE_SAL02");
	      attribute2dbField.put("endDateSal02DOC02","END_DATE_SAL02");
	      attribute2dbField.put("decimalsReg02DOC02","DECIMALS_REG02");
	      attribute2dbField.put("minSellingQtyItm01DOC02","MIN_SELLING_QTY_ITM01");
	      attribute2dbField.put("minSellingQtyUmCodeReg02DOC02","MIN_SELLING_QTY_UM_CODE_REG02");
	      attribute2dbField.put("valueReg01DOC02","VALUE_REG01");
	      attribute2dbField.put("deductibleReg01DOC02","DEDUCTIBLE_REG01");
	      attribute2dbField.put("taxableIncomeDOC02","TAXABLE_INCOME");
	      attribute2dbField.put("progressiveHie02DOC02","PROGRESSIVE_HIE02");
	      attribute2dbField.put("deliveryDateDOC02","DELIVERY_DATE");
	      attribute2dbField.put("outQtyDOC02","OUT_QTY");
	      attribute2dbField.put("progressiveHie01DOC02","PROGRESSIVE_HIE01");
	      attribute2dbField.put("invoiceQtyDOC02","INVOICE_QTY");

	      attribute2dbField.put("variantTypeItm06DOC02","VARIANT_TYPE_ITM06");
	      attribute2dbField.put("variantCodeItm11DOC02","VARIANT_CODE_ITM11");
	      attribute2dbField.put("variantTypeItm07DOC02","VARIANT_TYPE_ITM07");
	      attribute2dbField.put("variantCodeItm12DOC02","VARIANT_CODE_ITM12");
	      attribute2dbField.put("variantTypeItm08DOC02","VARIANT_TYPE_ITM08");
	      attribute2dbField.put("variantCodeItm13DOC02","VARIANT_CODE_ITM13");
	      attribute2dbField.put("variantTypeItm09DOC02","VARIANT_TYPE_ITM09");
	      attribute2dbField.put("variantCodeItm14DOC02","VARIANT_CODE_ITM14");
	      attribute2dbField.put("variantTypeItm10DOC02","VARIANT_TYPE_ITM10");
	      attribute2dbField.put("variantCodeItm15DOC02","VARIANT_CODE_ITM15");

	      vo.setRowNumberDOC02( CompanyProgressiveUtils.getInternalProgressive(vo.getCompanyCodeSys01DOC02(),"DOC02_SELLING_ITEMS","ROW_NUMBER",conn) );

	      // insert into DOC02...
	      Response res = QueryUtil.insertTable(
	          conn,
	          new UserSessionParameters(username),
	          vo,
	          "DOC02_SELLING_ITEMS",
	          attribute2dbField,
	          "Y",
	          "N",
	          null,
	          true
	      );

	      if (res.isError()) {
	        throw new Exception(res.getErrorMessage());
	      }

				// update doc state...
				SaleDocPK pk = new SaleDocPK(
						vo.getCompanyCodeSys01DOC02(),
						vo.getDocTypeDOC02(),
						vo.getDocYearDOC02(),
						vo.getDocNumberDOC02()
				);
				pstmt = conn.prepareStatement("update DOC01_SELLING set DOC_STATE=? where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? and DOC_STATE=?");
				pstmt.setString(1,ApplicationConsts.HEADER_BLOCKED);
				pstmt.setString(2,pk.getCompanyCodeSys01DOC01());
				pstmt.setString(3,pk.getDocTypeDOC01());
				pstmt.setBigDecimal(4,pk.getDocYearDOC01());
				pstmt.setBigDecimal(5,pk.getDocNumberDOC01());
				pstmt.setString(6,ApplicationConsts.OPENED);
				pstmt.execute();

				// recalculate totals...
				res = totals.updateTaxableIncomes(
					variant1Descriptions,
					variant2Descriptions,
					variant3Descriptions,
					variant4Descriptions,
					variant5Descriptions,
					pk,
					serverLanguageId,
					username

				);

	      return new VOResponse(vo);
	    }
	    catch (Throwable ex) {
	      Logger.error(username,this.getClass().getName(),"insertSaleItem","Error while inserting a new sale document item row",ex);
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
	    }
	  }




}
