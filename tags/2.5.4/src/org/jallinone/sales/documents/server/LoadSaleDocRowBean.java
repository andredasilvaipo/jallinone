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
public class LoadSaleDocRowBean implements LoadSaleDocRow {


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


	public LoadSaleDocRowBean() {
	}


	/**
	 * Load a specific item row.
	 * No commit or rollback are executed; no connection is created or released.-
	 */
	public VOResponse loadSaleDocRow(
					 HashMap variant1Descriptions,
					 HashMap variant2Descriptions,
					 HashMap variant3Descriptions,
					 HashMap variant4Descriptions,
					 HashMap variant5Descriptions,
					 SaleDocRowPK pk, String serverLanguageId, String username) throws Throwable {
		Statement stmt = null;
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;

			Map attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01DOC02","DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01");
			attribute2dbField.put("docTypeDOC02","DOC02_SELLING_ITEMS.DOC_TYPE");
			attribute2dbField.put("docYearDOC02","DOC02_SELLING_ITEMS.DOC_YEAR");
			attribute2dbField.put("docNumberDOC02","DOC02_SELLING_ITEMS.DOC_NUMBER");
			attribute2dbField.put("rowNumberDOC02","DOC02_SELLING_ITEMS.ROW_NUMBER");
			attribute2dbField.put("itemCodeItm01DOC02","DOC02_SELLING_ITEMS.ITEM_CODE_ITM01");
			attribute2dbField.put("vatCodeItm01DOC02","DOC02_SELLING_ITEMS.VAT_CODE_ITM01");
			attribute2dbField.put("valueSal02DOC02","DOC02_SELLING_ITEMS.VALUE_SAL02");
			attribute2dbField.put("valueDOC02","DOC02_SELLING_ITEMS.VALUE");
			attribute2dbField.put("qtyDOC02","DOC02_SELLING_ITEMS.QTY");
			attribute2dbField.put("totalDiscountDOC02","DOC02_SELLING_ITEMS.TOTAL_DISCOUNT");
			attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
			attribute2dbField.put("vatValueDOC02","DOC02_SELLING_ITEMS.VAT_VALUE");

			attribute2dbField.put("vatDescriptionDOC02","DOC02_SELLING_ITEMS.VAT_DESCRIPTION");
			attribute2dbField.put("startDateSal02DOC02","DOC02_SELLING_ITEMS.START_DATE_SAL02");
			attribute2dbField.put("endDateSal02DOC02","DOC02_SELLING_ITEMS.END_DATE_SAL02");
			attribute2dbField.put("minSellingQtyUmCodeReg02DOC02","DOC02_SELLING_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02");
			attribute2dbField.put("decimalsReg02DOC02","DOC02_SELLING_ITEMS.DECIMALS_REG02");
			attribute2dbField.put("minSellingQtyItm01DOC02","DOC02_SELLING_ITEMS.MIN_SELLING_QTY_ITM01");
			attribute2dbField.put("valueReg01DOC02","DOC02_SELLING_ITEMS.VALUE_REG01");
			attribute2dbField.put("deductibleReg01DOC02","DOC02_SELLING_ITEMS.DEDUCTIBLE_REG01");
			attribute2dbField.put("taxableIncomeDOC02","DOC02_SELLING_ITEMS.TAXABLE_INCOME");
			attribute2dbField.put("progressiveHie02DOC02","DOC02_SELLING_ITEMS.PROGRESSIVE_HIE02");
			attribute2dbField.put("progressiveHie01ITM01","ITM01_ITEMS.PROGRESSIVE_HIE01");
			attribute2dbField.put("deliveryDateDOC02","DOC02_SELLING_ITEMS.DELIVERY_DATE");
			attribute2dbField.put("outQtyDOC02","DOC02_SELLING_ITEMS.OUT_QTY");
			attribute2dbField.put("currencyCodeReg03DOC01","DOC01_SELLING.CURRENCY_CODE_REG03");
			attribute2dbField.put("progressiveHie01DOC02","DOC02_SELLING_ITEMS.PROGRESSIVE_HIE01");
			attribute2dbField.put("discountValueDOC02","DOC02_SELLING_ITEMS.DISCOUNT_VALUE");
			attribute2dbField.put("discountPercDOC02","DOC02_SELLING_ITEMS.DISCOUNT_PERC");
			attribute2dbField.put("invoiceQtyDOC02","DOC02_SELLING_ITEMS.INVOICE_QTY");

			attribute2dbField.put("variantTypeItm06DOC02","DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM06");
			attribute2dbField.put("variantCodeItm11DOC02","DOC02_SELLING_ITEMS.VARIANT_CODE_ITM11");
			attribute2dbField.put("variantTypeItm07DOC02","DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM07");
			attribute2dbField.put("variantCodeItm12DOC02","DOC02_SELLING_ITEMS.VARIANT_CODE_ITM12");
			attribute2dbField.put("variantTypeItm08DOC02","DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM08");
			attribute2dbField.put("variantCodeItm13DOC02","DOC02_SELLING_ITEMS.VARIANT_CODE_ITM13");
			attribute2dbField.put("variantTypeItm09DOC02","DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM09");
			attribute2dbField.put("variantCodeItm14DOC02","DOC02_SELLING_ITEMS.VARIANT_CODE_ITM14");
			attribute2dbField.put("variantTypeItm10DOC02","DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM10");
			attribute2dbField.put("variantCodeItm15DOC02","DOC02_SELLING_ITEMS.VARIANT_CODE_ITM15");

			attribute2dbField.put("noWarehouseMovITM01","ITM01_ITEMS.NO_WAREHOUSE_MOV");


			String baseSQL =
					"select DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01,DOC02_SELLING_ITEMS.DOC_TYPE,DOC02_SELLING_ITEMS.DOC_YEAR,DOC02_SELLING_ITEMS.DOC_NUMBER,DOC02_SELLING_ITEMS.ROW_NUMBER,"+
					"DOC02_SELLING_ITEMS.ITEM_CODE_ITM01,DOC02_SELLING_ITEMS.VAT_CODE_ITM01,DOC02_SELLING_ITEMS.VALUE_SAL02,"+
					"DOC02_SELLING_ITEMS.VALUE,DOC02_SELLING_ITEMS.QTY,DOC02_SELLING_ITEMS.TOTAL_DISCOUNT,ITM01_ITEMS.PROGRESSIVE_HIE01,"+
					"SYS10_TRANSLATIONS.DESCRIPTION,DOC02_SELLING_ITEMS.VAT_VALUE,DOC02_SELLING_ITEMS.VAT_DESCRIPTION,DOC02_SELLING_ITEMS.START_DATE_SAL02,DOC02_SELLING_ITEMS.END_DATE_SAL02,"+
					"DOC02_SELLING_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02,DOC02_SELLING_ITEMS.DECIMALS_REG02,DOC02_SELLING_ITEMS.MIN_SELLING_QTY_ITM01,"+
					"DOC02_SELLING_ITEMS.VALUE_REG01,DOC02_SELLING_ITEMS.DEDUCTIBLE_REG01,DOC02_SELLING_ITEMS.TAXABLE_INCOME,DOC02_SELLING_ITEMS.PROGRESSIVE_HIE02,DOC02_SELLING_ITEMS.DELIVERY_DATE,"+
					"DOC02_SELLING_ITEMS.OUT_QTY,DOC01_SELLING.CURRENCY_CODE_REG03,DOC02_SELLING_ITEMS.PROGRESSIVE_HIE01, "+
					"DOC02_SELLING_ITEMS.DISCOUNT_VALUE,DOC02_SELLING_ITEMS.DISCOUNT_PERC,DOC02_SELLING_ITEMS.INVOICE_QTY, "+
					"ITM01_ITEMS.NO_WAREHOUSE_MOV,"+
					"DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM06,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM11,"+
					"DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM07,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM12,"+
					"DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM08,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM13,"+
					"DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM09,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM14,"+
					"DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM10,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM15 "+
					" from DOC02_SELLING_ITEMS,ITM01_ITEMS,SYS10_TRANSLATIONS,DOC01_SELLING where "+
					"DOC01_SELLING.COMPANY_CODE_SYS01=DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01 and "+
					"DOC01_SELLING.DOC_TYPE=DOC02_SELLING_ITEMS.DOC_TYPE and "+
					"DOC01_SELLING.DOC_YEAR=DOC02_SELLING_ITEMS.DOC_YEAR and "+
					"DOC01_SELLING.DOC_NUMBER=DOC02_SELLING_ITEMS.DOC_NUMBER and "+
					"DOC02_SELLING_ITEMS.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and "+
					"DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
					"ITM01_ITEMS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
					"SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
					"DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01=? and "+
					"DOC02_SELLING_ITEMS.DOC_TYPE=? and "+
					"DOC02_SELLING_ITEMS.DOC_YEAR=? and "+
					"DOC02_SELLING_ITEMS.DOC_NUMBER=? and "+
					"DOC02_SELLING_ITEMS.ITEM_CODE_ITM01=? and "+
					"DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM06=? and "+
					"DOC02_SELLING_ITEMS.VARIANT_CODE_ITM11=? and "+
					"DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM07=? and "+
					"DOC02_SELLING_ITEMS.VARIANT_CODE_ITM12=? and "+
					"DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM08=? and "+
					"DOC02_SELLING_ITEMS.VARIANT_CODE_ITM13=? and "+
					"DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM09=? and "+
					"DOC02_SELLING_ITEMS.VARIANT_CODE_ITM14=? and "+
					"DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM10=? and "+
					"DOC02_SELLING_ITEMS.VARIANT_CODE_ITM15=? ";

			ArrayList values = new ArrayList();
			values.add(serverLanguageId);
			values.add(pk.getCompanyCodeSys01DOC02());
			values.add(pk.getDocTypeDOC02());
			values.add(pk.getDocYearDOC02());
			values.add(pk.getDocNumberDOC02());
			values.add(pk.getItemCodeItm01DOC02());

			values.add(pk.getVariantTypeItm06DOC02());
			values.add(pk.getVariantCodeItm11DOC02());
			values.add(pk.getVariantTypeItm07DOC02());
			values.add(pk.getVariantCodeItm12DOC02());
			values.add(pk.getVariantTypeItm08DOC02());
			values.add(pk.getVariantCodeItm13DOC02());
			values.add(pk.getVariantTypeItm09DOC02());
			values.add(pk.getVariantCodeItm14DOC02());
			values.add(pk.getVariantTypeItm10DOC02());
			values.add(pk.getVariantCodeItm15DOC02());

			// read from DOC02 table...
			Response res = QueryUtil.getQuery(
					conn,
					new UserSessionParameters(username),
					baseSQL,
					values,
					attribute2dbField,
					DetailSaleDocRowVO.class,
					"Y",
					"N",
					null,
					true
			);


			if (!res.isError()) {
				DetailSaleDocRowVO vo = (DetailSaleDocRowVO)((VOResponse)res).getVo();

							String descr = vo.getDescriptionSYS10();

							// check supported variants for current item...
							if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm11DOC02())) {
								descr += " "+getVariantCodeAndTypeDesc(
									variant1Descriptions,
									vo,
									vo.getVariantTypeItm06DOC02(),
									vo.getVariantCodeItm11DOC02(),
									serverLanguageId,
									username
								);
							}
							if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm12DOC02())) {
								descr += " "+getVariantCodeAndTypeDesc(
									variant2Descriptions,
									vo,
									vo.getVariantTypeItm07DOC02(),
									vo.getVariantCodeItm12DOC02(),
									serverLanguageId,
									username
								);
							}
							if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm13DOC02())) {
								descr += " "+getVariantCodeAndTypeDesc(
									variant3Descriptions,
									vo,
									vo.getVariantTypeItm08DOC02(),
									vo.getVariantCodeItm13DOC02(),
									serverLanguageId,
									username
								);
							}
							if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm14DOC02())) {
								descr += " "+getVariantCodeAndTypeDesc(
									variant4Descriptions,
									vo,
									vo.getVariantTypeItm09DOC02(),
									vo.getVariantCodeItm14DOC02(),
									serverLanguageId,
									username
								);
							}
							if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm15DOC02())) {
								descr += " "+getVariantCodeAndTypeDesc(
									variant5Descriptions,
									vo,
									vo.getVariantTypeItm10DOC02(),
									vo.getVariantCodeItm15DOC02(),
									serverLanguageId,
									username
								);
							}
							vo.setDescriptionSYS10(descr);




				ResultSet rset = null;

				// retrieve position description, if defined...
				if (vo.getProgressiveHie01DOC02()!=null) {
					pstmt = conn.prepareStatement("select DESCRIPTION from SYS10_TRANSLATIONS where PROGRESSIVE=? and LANGUAGE_CODE=?");
					pstmt.setBigDecimal(1,vo.getProgressiveHie01DOC02());
					pstmt.setString(2,serverLanguageId);
					try {
						rset = pstmt.executeQuery();
						while(rset.next()) {
							vo.setPositionDescriptionSYS10( rset.getString(1) );
						}
					}
					catch (Exception ex3) {
						throw ex3;
					}
					finally {
						rset.close();
					}
					pstmt.close();
				}

				// retrieve serial numbers...
				ArrayList serialNums = null;

				pstmt = conn.prepareStatement(
					"select SERIAL_NUMBER from DOC18_SELLING_SERIAL_NUMBERS where "+
					"COMPANY_CODE_SYS01=? and "+
					"DOC_TYPE=? and "+
					"DOC_YEAR=? and "+
					"DOC_NUMBER=? and "+
					"ITEM_CODE_ITM01=? and "+
					"VARIANT_TYPE_ITM06=? and VARIANT_CODE_ITM11=? and "+
					"VARIANT_TYPE_ITM07=? and VARIANT_CODE_ITM12=? and "+
					"VARIANT_TYPE_ITM08=? and VARIANT_CODE_ITM13=? and "+
					"VARIANT_TYPE_ITM09=? and VARIANT_CODE_ITM14=? and "+
					"VARIANT_TYPE_ITM10=? and VARIANT_CODE_ITM15=? "
				 );
				 serialNums = new ArrayList();
				 vo.setSerialNumbers(serialNums);
				 pstmt.setString(1,vo.getCompanyCodeSys01DOC02());
				 pstmt.setString(2,vo.getDocTypeDOC02());
				 pstmt.setBigDecimal(3,vo.getDocYearDOC02());
				 pstmt.setBigDecimal(4,vo.getDocNumberDOC02());
				 pstmt.setString(5,vo.getItemCodeItm01DOC02());

				 pstmt.setString(6,vo.getVariantTypeItm06DOC02());
				 pstmt.setString(7,vo.getVariantCodeItm11DOC02());
				 pstmt.setString(8,vo.getVariantTypeItm07DOC02());
				 pstmt.setString(9,vo.getVariantCodeItm12DOC02());
				 pstmt.setString(10,vo.getVariantTypeItm08DOC02());
				 pstmt.setString(11,vo.getVariantCodeItm13DOC02());
				 pstmt.setString(12,vo.getVariantTypeItm09DOC02());
				 pstmt.setString(13,vo.getVariantCodeItm14DOC02());
				 pstmt.setString(14,vo.getVariantTypeItm10DOC02());
				 pstmt.setString(15,vo.getVariantCodeItm15DOC02());

				 try {
					 rset = pstmt.executeQuery();
					 while(rset.next()) {
						 serialNums.add(rset.getString(1));
					 }
				 }
				 catch (Exception ex3) {
					 throw ex3;
				 }
				 finally {
					 rset.close();
				}
			}

			Response answer = res;
			if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOResponse)answer;
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"loadSaleDocRow","Error while fetching an existing sale document row",ex);
			throw new Exception(ex.getMessage());
		}
		finally {
			try {
				stmt.close();
			}
			catch (Exception ex2) {

			try {
			} catch (Exception ex) {}
			}
			try {
				pstmt.close();
			}
			catch (Exception ex4) {
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



	private String getVariantCodeAndTypeDesc(
			HashMap variantDescriptions,
			DetailSaleDocRowVO vo,
			String varType,
			String varCode,
			String serverLanguageId,
			String username
	) throws Throwable {
		String varDescr = (String)variantDescriptions.get(varType+"_"+varCode);
		if (varDescr==null)
			varDescr = ApplicationConsts.JOLLY.equals(varCode)?"":varCode;
		return varDescr;
	}





}
