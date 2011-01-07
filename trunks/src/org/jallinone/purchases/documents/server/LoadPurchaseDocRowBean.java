package org.jallinone.purchases.documents.server;

import org.openswing.swing.server.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.purchases.documents.java.*;
import org.jallinone.system.server.*;
import org.jallinone.commons.server.*;
import java.math.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.system.java.*;
import org.jallinone.commons.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to fetch a specific purchase order row from DOC07 table.</p>
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
public class LoadPurchaseDocRowBean  implements LoadPurchaseDocRow {


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


  public LoadPurchaseDocRowBean() {
  }



  /**
   * Business logic to execute.
   */
  public DetailPurchaseDocRowVO loadPurchaseDocRow(
		HashMap variant1Descriptions,
		HashMap variant2Descriptions,
		HashMap variant3Descriptions,
		HashMap variant4Descriptions,
		HashMap variant5Descriptions,
		PurchaseDocRowPK pk, String serverLanguageId, String username) throws
		Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC07","DOC07_PURCHASE_ITEMS.COMPANY_CODE_SYS01");
      attribute2dbField.put("docTypeDOC07","DOC07_PURCHASE_ITEMS.DOC_TYPE");
      attribute2dbField.put("docYearDOC07","DOC07_PURCHASE_ITEMS.DOC_YEAR");
      attribute2dbField.put("docNumberDOC07","DOC07_PURCHASE_ITEMS.DOC_NUMBER");
      attribute2dbField.put("rowNumberDOC07","DOC07_PURCHASE_ITEMS.ROW_NUMBER");
      attribute2dbField.put("itemCodeItm01DOC07","DOC07_PURCHASE_ITEMS.ITEM_CODE_ITM01");
      attribute2dbField.put("supplierItemCodePur02DOC07","DOC07_PURCHASE_ITEMS.SUPPLIER_ITEM_CODE_PUR02");
      attribute2dbField.put("vatCodeItm01DOC07","DOC07_PURCHASE_ITEMS.VAT_CODE_ITM01");
      attribute2dbField.put("valuePur04DOC07","DOC07_PURCHASE_ITEMS.VALUE_PUR04");
      attribute2dbField.put("valueDOC07","DOC07_PURCHASE_ITEMS.VALUE");
      attribute2dbField.put("qtyDOC07","DOC07_PURCHASE_ITEMS.QTY");
      attribute2dbField.put("discountValueDOC07","DOC07_PURCHASE_ITEMS.DISCOUNT_VALUE");
      attribute2dbField.put("discountPercDOC07","DOC07_PURCHASE_ITEMS.DISCOUNT_PERC");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("vatValueDOC07","DOC07_PURCHASE_ITEMS.VAT_VALUE");

      attribute2dbField.put("vatDescriptionDOC07","DOC07_PURCHASE_ITEMS.VAT_DESCRIPTION");
      attribute2dbField.put("startDatePur04DOC07","DOC07_PURCHASE_ITEMS.START_DATE_PUR04");
      attribute2dbField.put("endDatePur04DOC07","DOC07_PURCHASE_ITEMS.END_DATE_PUR04");
      attribute2dbField.put("umCodePur02DOC07","DOC07_PURCHASE_ITEMS.UM_CODE_PUR02");
      attribute2dbField.put("decimalsReg02DOC07","DOC07_PURCHASE_ITEMS.DECIMALS_REG02");
      attribute2dbField.put("minPurchaseQtyPur02DOC07","DOC07_PURCHASE_ITEMS.MIN_PURCHASE_QTY_PUR02");
      attribute2dbField.put("multipleQtyPur02DOC07","DOC07_PURCHASE_ITEMS.MULTIPLE_QTY_PUR02");
      attribute2dbField.put("valueReg01DOC07","DOC07_PURCHASE_ITEMS.VALUE_REG01");
      attribute2dbField.put("deductibleReg01DOC07","DOC07_PURCHASE_ITEMS.DEDUCTIBLE_REG01");
      attribute2dbField.put("taxableIncomeDOC07","DOC07_PURCHASE_ITEMS.TAXABLE_INCOME");
      attribute2dbField.put("progressiveHie02DOC07","DOC07_PURCHASE_ITEMS.PROGRESSIVE_HIE02");
      attribute2dbField.put("deliveryDateDOC07","DOC07_PURCHASE_ITEMS.DELIVERY_DATE");
      attribute2dbField.put("inQtyDOC07","DOC07_PURCHASE_ITEMS.IN_QTY");
      attribute2dbField.put("orderQtyDOC07","DOC07_PURCHASE_ITEMS.ORDER_QTY");
      attribute2dbField.put("currencyCodeReg03DOC06","DOC06_PURCHASE.CURRENCY_CODE_REG03");
      attribute2dbField.put("invoiceQtyDOC07","DOC07_PURCHASE_ITEMS.INVOICE_QTY");

      attribute2dbField.put("variantTypeItm06DOC07","DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM06");
      attribute2dbField.put("variantCodeItm11DOC07","DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM11");
      attribute2dbField.put("variantTypeItm07DOC07","DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM07");
      attribute2dbField.put("variantCodeItm12DOC07","DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM12");
      attribute2dbField.put("variantTypeItm08DOC07","DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM08");
      attribute2dbField.put("variantCodeItm13DOC07","DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM13");
      attribute2dbField.put("variantTypeItm09DOC07","DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM09");
      attribute2dbField.put("variantCodeItm14DOC07","DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM14");
      attribute2dbField.put("variantTypeItm10DOC07","DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM10");
      attribute2dbField.put("variantCodeItm15DOC07","DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM15");

			attribute2dbField.put("noWarehouseMovITM01","ITM01_ITEMS.NO_WAREHOUSE_MOV");

      String baseSQL =
          "select DOC07_PURCHASE_ITEMS.COMPANY_CODE_SYS01,DOC07_PURCHASE_ITEMS.DOC_TYPE,DOC07_PURCHASE_ITEMS.DOC_YEAR,DOC07_PURCHASE_ITEMS.DOC_NUMBER,DOC07_PURCHASE_ITEMS.ROW_NUMBER,"+
          "DOC07_PURCHASE_ITEMS.ITEM_CODE_ITM01,DOC07_PURCHASE_ITEMS.SUPPLIER_ITEM_CODE_PUR02,DOC07_PURCHASE_ITEMS.VAT_CODE_ITM01,DOC07_PURCHASE_ITEMS.VALUE_PUR04,"+
          "DOC07_PURCHASE_ITEMS.VALUE,DOC07_PURCHASE_ITEMS.QTY,DOC07_PURCHASE_ITEMS.DISCOUNT_VALUE,DOC07_PURCHASE_ITEMS.DISCOUNT_PERC,"+
          "SYS10_TRANSLATIONS.DESCRIPTION,DOC07_PURCHASE_ITEMS.VAT_VALUE,DOC07_PURCHASE_ITEMS.VAT_DESCRIPTION,DOC07_PURCHASE_ITEMS.START_DATE_PUR04,DOC07_PURCHASE_ITEMS.END_DATE_PUR04,"+
          "DOC07_PURCHASE_ITEMS.UM_CODE_PUR02,DOC07_PURCHASE_ITEMS.DECIMALS_REG02,DOC07_PURCHASE_ITEMS.MIN_PURCHASE_QTY_PUR02,DOC07_PURCHASE_ITEMS.MULTIPLE_QTY_PUR02,"+
          "DOC07_PURCHASE_ITEMS.VALUE_REG01,DOC07_PURCHASE_ITEMS.DEDUCTIBLE_REG01,DOC07_PURCHASE_ITEMS.TAXABLE_INCOME,DOC07_PURCHASE_ITEMS.PROGRESSIVE_HIE02,DOC07_PURCHASE_ITEMS.DELIVERY_DATE,"+
          "DOC07_PURCHASE_ITEMS.IN_QTY,DOC07_PURCHASE_ITEMS.ORDER_QTY,DOC06_PURCHASE.CURRENCY_CODE_REG03,DOC07_PURCHASE_ITEMS.INVOICE_QTY, "+
          "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM06,DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM11,"+
          "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM07,DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM12,"+
          "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM08,DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM13,"+
          "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM09,DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM14,"+
          "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM10,DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM15, "+
					"ITM01_ITEMS.NO_WAREHOUSE_MOV "+
          " from DOC07_PURCHASE_ITEMS,ITM01_ITEMS,SYS10_TRANSLATIONS,DOC06_PURCHASE where "+
          "DOC07_PURCHASE_ITEMS.COMPANY_CODE_SYS01=DOC06_PURCHASE.COMPANY_CODE_SYS01 and "+
          "DOC07_PURCHASE_ITEMS.DOC_TYPE=DOC06_PURCHASE.DOC_TYPE and "+
          "DOC07_PURCHASE_ITEMS.DOC_YEAR=DOC06_PURCHASE.DOC_YEAR and "+
          "DOC07_PURCHASE_ITEMS.DOC_NUMBER=DOC06_PURCHASE.DOC_NUMBER and "+
          "DOC07_PURCHASE_ITEMS.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and "+
          "DOC07_PURCHASE_ITEMS.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
          "ITM01_ITEMS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "DOC07_PURCHASE_ITEMS.COMPANY_CODE_SYS01=? and "+
          "DOC07_PURCHASE_ITEMS.DOC_TYPE=? and "+
          "DOC07_PURCHASE_ITEMS.DOC_YEAR=? and "+
          "DOC07_PURCHASE_ITEMS.DOC_NUMBER=? and "+
          "DOC07_PURCHASE_ITEMS.ITEM_CODE_ITM01=? and "+
          "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM06=? and "+
          "DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM11=? and "+
          "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM07=? and "+
          "DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM12=? and "+
          "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM08=? and "+
          "DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM13=? and "+
          "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM09=? and "+
          "DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM14=? and "+
          "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM10=? and "+
          "DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM15=? ";

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(pk.getCompanyCodeSys01DOC07());
      values.add(pk.getDocTypeDOC07());
      values.add(pk.getDocYearDOC07());
      values.add(pk.getDocNumberDOC07());
      values.add(pk.getItemCodeItm01DOC07());
      values.add(pk.getVariantTypeItm06DOC07());
      values.add(pk.getVariantCodeItm11DOC07());
      values.add(pk.getVariantTypeItm07DOC07());
      values.add(pk.getVariantCodeItm12DOC07());
      values.add(pk.getVariantTypeItm08DOC07());
      values.add(pk.getVariantCodeItm13DOC07());
      values.add(pk.getVariantTypeItm09DOC07());
      values.add(pk.getVariantCodeItm14DOC07());
      values.add(pk.getVariantTypeItm10DOC07());
      values.add(pk.getVariantCodeItm15DOC07());

      // read from DOC07 table...
      Response res = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          baseSQL,
          values,
          attribute2dbField,
          DetailPurchaseDocRowVO.class,
          "Y",
          "N",
          null,
          true
      );

			Response answer = res;
			if (answer.isError())
				throw new Exception(answer.getErrorMessage());
			DetailPurchaseDocRowVO vo = (DetailPurchaseDocRowVO) ( (VOResponse)answer).getVo();
			String descr = vo.getDescriptionSYS10();

			// check supported variants for current item...
			if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm11DOC07())) {
				descr += " "+getVariantCodeAndTypeDesc(
					variant1Descriptions,
					vo,
					vo.getVariantTypeItm06DOC07(),
					vo.getVariantCodeItm11DOC07(),
					serverLanguageId,
					username
				);
			}
			if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm12DOC07())) {
				descr += " "+getVariantCodeAndTypeDesc(
					variant2Descriptions,
					vo,
					vo.getVariantTypeItm07DOC07(),
					vo.getVariantCodeItm12DOC07(),
					serverLanguageId,
					username
				);
			}
			if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm13DOC07())) {
				descr += " "+getVariantCodeAndTypeDesc(
					variant3Descriptions,
					vo,
					vo.getVariantTypeItm08DOC07(),
					vo.getVariantCodeItm13DOC07(),
					serverLanguageId,
					username
				);
			}
			if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm14DOC07())) {
				descr += " "+getVariantCodeAndTypeDesc(
					variant4Descriptions,
					vo,
					vo.getVariantTypeItm09DOC07(),
					vo.getVariantCodeItm14DOC07(),
					serverLanguageId,
					username
				);
			}
			if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm15DOC07())) {
				descr += " "+getVariantCodeAndTypeDesc(
					variant5Descriptions,
					vo,
					vo.getVariantTypeItm10DOC07(),
					vo.getVariantCodeItm15DOC07(),
					serverLanguageId,
					username
				);
			}
			vo.setDescriptionSYS10(descr);


	    return vo;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching an existing purchase order row",ex);
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
            stmt.close();
        }
        catch (Exception exx) {}
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
				DetailPurchaseDocRowVO vo,
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

