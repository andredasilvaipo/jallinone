package org.jallinone.sales.documents.server;

import org.openswing.swing.server.*;

import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.sales.documents.activities.java.SaleDocActivityVO;
import org.jallinone.sales.documents.headercharges.java.SaleDocChargeVO;
import org.jallinone.sales.documents.headerdiscounts.java.SaleDocDiscountVO;
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
import org.jallinone.items.server.LoadItemVariantsBean;
import org.jallinone.items.java.ItemVariantVO;
import org.jallinone.items.java.ItemPK;

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
public class LoadSaleDocRowsBean implements LoadSaleDocRows {



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




	  /**
	   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
	   */
	  public GridSaleDocRowVO getGridSaleDocRow(SaleDocPK pk) {
		  throw new UnsupportedOperationException();
	  }

	  /**
	   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
	   */
	  public SaleDocActivityVO getSaleDocActivity(SaleDocPK pk) {
		  throw new UnsupportedOperationException();
	  }

	  /**
	   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
	   */
	  public SaleDocChargeVO getSaleDocCharge(SaleDocPK pk) {
		  throw new UnsupportedOperationException();
	  }

	  /**
	   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
	   */
	  public SaleDocDiscountVO getSaleDocDiscount(SaleDocPK pk) {
		  throw new UnsupportedOperationException();
	  }


	  /**
	   * Load all item rows for the specified sale document.
	   * No commit or rollback are executed; no connection is created or released.
	   */
	  public VOListResponse loadSaleDocRows(
              HashMap variant1Descriptions,
              HashMap variant2Descriptions,
              HashMap variant3Descriptions,
              HashMap variant4Descriptions,
              HashMap variant5Descriptions,
              GridParams pars,
              String serverLanguageId,
              String username
          )  throws Throwable{
	    Connection conn = null;
	    try {
	      if (this.conn==null) conn = getConn(); else conn = this.conn;
	      SaleDocPK pk = (SaleDocPK)pars.getOtherGridParams().get(ApplicationConsts.SALE_DOC_PK);

	      String sql =
	          "select DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01,DOC02_SELLING_ITEMS.DOC_TYPE,DOC02_SELLING_ITEMS.DOC_YEAR,DOC02_SELLING_ITEMS.DOC_NUMBER,DOC02_SELLING_ITEMS.ROW_NUMBER,"+
	          "DOC02_SELLING_ITEMS.ITEM_CODE_ITM01,DOC02_SELLING_ITEMS.VAT_CODE_ITM01,DOC02_SELLING_ITEMS.VALUE_SAL02,"+
	          "DOC02_SELLING_ITEMS.VALUE,DOC02_SELLING_ITEMS.QTY,DOC02_SELLING_ITEMS.TOTAL_DISCOUNT,"+
	          "SYS10_TRANSLATIONS.DESCRIPTION,DOC02_SELLING_ITEMS.VAT_VALUE,DOC02_SELLING_ITEMS.OUT_QTY, "+
	          "DOC02_SELLING_ITEMS.INVOICE_QTY,ITM01_ITEMS.SERIAL_NUMBER_REQUIRED, "+
	          "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM06,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM11,"+
	          "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM07,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM12,"+
	          "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM08,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM13,"+
	          "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM09,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM14,"+
	          "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM10,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM15, "+
	          "DOC02_SELLING_ITEMS.DELIVERY_DATE,ITM01_ITEMS.NO_WAREHOUSE_MOV "+
	          " from DOC02_SELLING_ITEMS,ITM01_ITEMS,SYS10_TRANSLATIONS where "+
	          "DOC02_SELLING_ITEMS.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and "+
	          "DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
	          "ITM01_ITEMS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
	          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
	          "DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01=? and "+
	          "DOC02_SELLING_ITEMS.DOC_TYPE=? and "+
	          "DOC02_SELLING_ITEMS.DOC_YEAR=? and "+
	          "DOC02_SELLING_ITEMS.DOC_NUMBER=? ";

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
	      attribute2dbField.put("outQtyDOC02","DOC02_SELLING_ITEMS.OUT_QTY");
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

	      attribute2dbField.put("deliveryDateDOC02","DOC02_SELLING_ITEMS.DELIVERY_DATE");
				attribute2dbField.put("serialNumberRequiredITM01","ITM01_ITEMS.SERIAL_NUMBER_REQUIRED");
				attribute2dbField.put("noWarehouseMovITM01","ITM01_ITEMS.NO_WAREHOUSE_MOV");

	      ArrayList values = new ArrayList();
	      values.add(serverLanguageId);
	      values.add(pk.getCompanyCodeSys01DOC01());
	      values.add(pk.getDocTypeDOC01());
	      values.add(pk.getDocYearDOC01());
	      values.add(pk.getDocNumberDOC01());

	      // read from DOC02 table...
	      Response answer = QueryUtil.getQuery(
	          conn,
	          new UserSessionParameters(username),
	          sql,
	          values,
	          attribute2dbField,
	          GridSaleDocRowVO.class,
	          "Y",
	          "N",
	          null,
	          pars,
	          true
	      );

	      if (answer.isError())
                  throw new Exception(answer.getErrorMessage());
              else {
                GridSaleDocRowVO vo = null;
                List rows = ((VOListResponse)answer).getRows();
                String descr = null;
                for(int i=0;i<rows.size();i++) {
                  vo = (GridSaleDocRowVO)rows.get(i);
                  descr = vo.getDescriptionSYS10();

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

                } // end for on rows...


                return (VOListResponse) answer;
              }

	    }
	    catch (Throwable ex) {
	      Logger.error(username,this.getClass().getName(),"loadSaleDocRows","Error while fetching sale order rows list",ex);
	      throw new Exception(ex.getMessage());
	    }
	    finally {
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
        GridSaleDocRowVO vo,
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




	  /**
	   * Retrive all activities defined for the specified sale document.
	   * No commit or rollback are executed. No connection is created or released.
	   */
	  public VOListResponse loadSaleDocActivities(GridParams gridParams,String serverLanguageId,String username) throws Throwable {
	    PreparedStatement pstmt = null;
	    Connection conn = null;
	    try {
	      if (this.conn==null) conn = getConn(); else conn = this.conn;


	      String sql =
	          "select DOC13_SELLING_ACTIVITIES.COMPANY_CODE_SYS01,DOC13_SELLING_ACTIVITIES.ACTIVITY_CODE_SAL09,"+
	          "DOC13_SELLING_ACTIVITIES.VALUE,DOC13_SELLING_ACTIVITIES.VALUE_SAL09,DOC13_SELLING_ACTIVITIES.ACTIVITY_DESCRIPTION,"+
	          "DOC13_SELLING_ACTIVITIES.DOC_TYPE,DOC13_SELLING_ACTIVITIES.DOC_YEAR,DOC13_SELLING_ACTIVITIES.DOC_NUMBER, "+
	          "DOC13_SELLING_ACTIVITIES.VAT_CODE_SAL09,DOC13_SELLING_ACTIVITIES.VAT_DESCRIPTION,DOC13_SELLING_ACTIVITIES.VAT_VALUE,DOC13_SELLING_ACTIVITIES.VAT_DEDUCTIBLE, "+
	          "DOC13_SELLING_ACTIVITIES.DURATION,DOC13_SELLING_ACTIVITIES.PROGRESSIVE_SCH06, "+
	          "REG03_CURRENCIES.CURRENCY_SYMBOL,DOC13_SELLING_ACTIVITIES.CURRENCY_CODE_REG03,DOC13_SELLING_ACTIVITIES.INVOICED_VALUE, "+
	          "DOC13_SELLING_ACTIVITIES.TAXABLE_INCOME,DOC13_SELLING_ACTIVITIES.VALUE_REG01 "+
	          "from DOC13_SELLING_ACTIVITIES,REG03_CURRENCIES where "+
	          "DOC13_SELLING_ACTIVITIES.CURRENCY_CODE_REG03=REG03_CURRENCIES.CURRENCY_CODE and "+
	          "DOC13_SELLING_ACTIVITIES.COMPANY_CODE_SYS01=? and "+
	          "DOC13_SELLING_ACTIVITIES.DOC_TYPE=? and "+
	          "DOC13_SELLING_ACTIVITIES.DOC_YEAR=? and "+
	          "DOC13_SELLING_ACTIVITIES.DOC_NUMBER=? ";

	      Map attribute2dbField = new HashMap();
	      attribute2dbField.put("companyCodeSys01DOC13","DOC13_SELLING_ACTIVITIES.COMPANY_CODE_SYS01");
	      attribute2dbField.put("activityCodeSal09DOC13","DOC13_SELLING_ACTIVITIES.ACTIVITY_CODE_SAL09");
	      attribute2dbField.put("valueSal09DOC13","DOC13_SELLING_ACTIVITIES.VALUE_SAL09");
	      attribute2dbField.put("activityDescriptionDOC13","DOC13_SELLING_ACTIVITIES.ACTIVITY_DESCRIPTION");
	      attribute2dbField.put("valueDOC13","DOC13_SELLING_ACTIVITIES.VALUE");
	      attribute2dbField.put("docTypeDOC13","DOC13_SELLING_ACTIVITIES.DOC_TYPE");
	      attribute2dbField.put("docYearDOC13","DOC13_SELLING_ACTIVITIES.DOC_YEAR");
	      attribute2dbField.put("docNumberDOC13","DOC13_SELLING_ACTIVITIES.DOC_NUMBER");
	      attribute2dbField.put("vatCodeSal09DOC13","DOC13_SELLING_ACTIVITIES.VAT_CODE_SAL09");
	      attribute2dbField.put("vatDescriptionDOC13","DOC13_SELLING_ACTIVITIES.VAT_DESCRIPTION");
	      attribute2dbField.put("vatValueDOC13","DOC13_SELLING_ACTIVITIES.VAT_VALUE");
	      attribute2dbField.put("vatDeductibleDOC13","DOC13_SELLING_ACTIVITIES.VAT_DEDUCTIBLE");
	      attribute2dbField.put("durationDOC13","DOC13_SELLING_ACTIVITIES.DURATION");
	      attribute2dbField.put("progressiveSch06DOC13","DOC13_SELLING_ACTIVITIES.PROGRESSIVE_SCH06");
	      attribute2dbField.put("currencySymbolREG03","REG03_CURRENCIES.CURRENCY_SYMBOL");
	      attribute2dbField.put("currencyCodeReg03DOC13","DOC13_SELLING_ACTIVITIES.CURRENCY_CODE_REG03");
	      attribute2dbField.put("invoicedValueDOC13","DOC13_SELLING_ACTIVITIES.INVOICED_VALUE");
	      attribute2dbField.put("valueReg01DOC13","DOC13_SELLING_ACTIVITIES.VALUE_REG01");
	      attribute2dbField.put("taxableIncomeDOC13","DOC13_SELLING_ACTIVITIES.TAXABLE_INCOME");

	      ArrayList values = new ArrayList();
	      SaleDocPK pk = (SaleDocPK)gridParams.getOtherGridParams().get(ApplicationConsts.SALE_DOC_PK);
	      values.add( pk.getCompanyCodeSys01DOC01() );
	      values.add( pk.getDocTypeDOC01() );
	      values.add( pk.getDocYearDOC01() );
	      values.add( pk.getDocNumberDOC01() );

	      // read from DOC13 table...
	      Response res = QueryUtil.getQuery(
	          conn,
	          new UserSessionParameters(username),
	          sql,
	          values,
	          attribute2dbField,
	          SaleDocActivityVO.class,
	          "Y",
	          "N",
	          null,
	          gridParams,
	          true
	      );
	      if (res.isError())
	        throw new Exception(res.getErrorMessage());

	      java.util.List list = ((VOListResponse)res).getRows();
	      SaleDocActivityVO vo = null;
	      sql =
	         "select SCH06_SCHEDULED_ACTIVITIES.DESCRIPTION "+
	         "from SCH06_SCHEDULED_ACTIVITIES,REG01_VATS where "+
	         "SCH06_SCHEDULED_ACTIVITIES.COMPANY_CODE_SYS01=? and "+
	         "SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE=?";
	      pstmt = conn.prepareStatement(sql);
	      ResultSet rset = null;
	      for(int i=0;i<list.size();i++) {
	        vo = (SaleDocActivityVO)list.get(i);
	        if (vo.getProgressiveSch06DOC13()!=null) {
	          // retrieve scheduled activity description from SCH06...
	          pstmt.setString(1,vo.getCompanyCodeSys01DOC13());
	          pstmt.setBigDecimal(2,vo.getProgressiveSch06DOC13());
	          rset = pstmt.executeQuery();
	          if (rset.next()) {
	            vo.setDescriptionSCH06(rset.getString(1));
	          }
	          rset.close();
	        }
	      }

	      Response answer = res;
	      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;
	    }
	    catch (Throwable ex) {
	      Logger.error(username,this.getClass().getName(),"loadSaleDocActivities","Error while fetching sale activities list",ex);
	      throw new Exception(ex.getMessage());
	    }
	    finally {
	        try {
	            pstmt.close();
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





	  /**
	   * Retrieve all charges defined for a specified sale document.
	   * No commit or rollback are executed. No connection is created or released.
	   */
	  public VOListResponse loadSaleDocCharges(GridParams gridParams,String serverLanguageId,String username) throws Throwable {
	    Connection conn = null;
	    try {
	      if (this.conn==null) conn = getConn(); else conn = this.conn;

	      String sql =
	          "select DOC03_SELLING_CHARGES.COMPANY_CODE_SYS01,DOC03_SELLING_CHARGES.CHARGE_CODE_SAL06,"+
	          "DOC03_SELLING_CHARGES.VALUE,DOC03_SELLING_CHARGES.VALUE_SAL06,DOC03_SELLING_CHARGES.PERC,"+
	          "DOC03_SELLING_CHARGES.PERC_SAL06,DOC03_SELLING_CHARGES.CHARGE_DESCRIPTION,"+
	          "DOC03_SELLING_CHARGES.DOC_TYPE,DOC03_SELLING_CHARGES.DOC_YEAR,DOC03_SELLING_CHARGES.DOC_NUMBER, "+
	          "DOC03_SELLING_CHARGES.VAT_CODE_SAL06,DOC03_SELLING_CHARGES.VAT_DESCRIPTION,DOC03_SELLING_CHARGES.VAT_VALUE,"+
	          "DOC03_SELLING_CHARGES.VAT_DEDUCTIBLE,DOC03_SELLING_CHARGES.INVOICED_VALUE, "+
	          "DOC03_SELLING_CHARGES.VALUE_REG01,DOC03_SELLING_CHARGES.TAXABLE_INCOME "+
	          "from DOC03_SELLING_CHARGES where "+
	          "DOC03_SELLING_CHARGES.COMPANY_CODE_SYS01=? and "+
	          "DOC03_SELLING_CHARGES.DOC_TYPE=? and "+
	          "DOC03_SELLING_CHARGES.DOC_YEAR=? and "+
	          "DOC03_SELLING_CHARGES.DOC_NUMBER=? ";

	      Map attribute2dbField = new HashMap();
	      attribute2dbField.put("companyCodeSys01DOC03","DOC03_SELLING_CHARGES.COMPANY_CODE_SYS01");
	      attribute2dbField.put("chargeCodeSal06DOC03","DOC03_SELLING_CHARGES.CHARGE_CODE_SAL06");
	      attribute2dbField.put("valueSal06DOC03","DOC03_SELLING_CHARGES.VALUE_SAL06");
	      attribute2dbField.put("percSal06DOC03","DOC03_SELLING_CHARGES.PERC_SAL06");
	      attribute2dbField.put("chargeDescriptionDOC03","DOC03_SELLING_CHARGES.CHARGE_DESCRIPTION");
	      attribute2dbField.put("valueDOC03","DOC03_SELLING_CHARGES.VALUE");
	      attribute2dbField.put("percDOC03","DOC03_SELLING_CHARGES.PERC");
	      attribute2dbField.put("docTypeDOC03","DOC03_SELLING_CHARGES.DOC_TYPE");
	      attribute2dbField.put("docYearDOC03","DOC03_SELLING_CHARGES.DOC_YEAR");
	      attribute2dbField.put("docNumberDOC03","DOC03_SELLING_CHARGES.DOC_NUMBER");
	      attribute2dbField.put("vatCodeSal06DOC03","DOC03_SELLING_CHARGES.VAT_CODE_SAL06");
	      attribute2dbField.put("vatDescriptionDOC03","DOC03_SELLING_CHARGES.VAT_DESCRIPTION");
	      attribute2dbField.put("vatValueDOC03","DOC03_SELLING_CHARGES.VAT_VALUE");
	      attribute2dbField.put("vatDeductibleDOC03","DOC03_SELLING_CHARGES.VAT_DEDUCTIBLE");
	      attribute2dbField.put("invoicedValueDOC03","DOC03_SELLING_CHARGES.INVOICED_VALUE");
	      attribute2dbField.put("valueReg01DOC03","DOC03_SELLING_CHARGES.VALUE_REG01");
	      attribute2dbField.put("taxableIncomeDOC03","DOC03_SELLING_CHARGES.TAXABLE_INCOME");

	      ArrayList values = new ArrayList();
	      SaleDocPK pk = (SaleDocPK)gridParams.getOtherGridParams().get(ApplicationConsts.SALE_DOC_PK);
	      values.add( pk.getCompanyCodeSys01DOC01() );
	      values.add( pk.getDocTypeDOC01() );
	      values.add( pk.getDocYearDOC01() );
	      values.add( pk.getDocNumberDOC01() );

	      // read from DOC03 table...
	      Response answer = QueryUtil.getQuery(
	          conn,
	          new UserSessionParameters(username),
	          sql,
	          values,
	          attribute2dbField,
	          SaleDocChargeVO.class,
	          "Y",
	          "N",
	          null,
	          gridParams,
	          true
	      );

	      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

	    }
	    catch (Throwable ex) {
	      Logger.error(username,this.getClass().getName(),"loadSaleDocCharges","Error while fetching sale charges list",ex);
	      throw new Exception(ex.getMessage());
	    }
	    finally {
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




	  /**
	   * Retrieve all header discounts defined for a specified sale document.
	   * No commit or rollback are executed. No connection is created or released.
	   */
	  public VOListResponse loadSaleDocDiscounts(GridParams gridParams,String serverLanguageId,String username) throws Throwable {
	    Connection conn = null;
	    try {
	      if (this.conn==null) conn = getConn(); else conn = this.conn;

	      String sql =
	          "select DOC05_SELLING_DISCOUNTS.COMPANY_CODE_SYS01,DOC05_SELLING_DISCOUNTS.DISCOUNT_CODE_SAL03,"+
	          "DOC05_SELLING_DISCOUNTS.MIN_VALUE,DOC05_SELLING_DISCOUNTS.MAX_VALUE,DOC05_SELLING_DISCOUNTS.MIN_PERC,"+
	          "DOC05_SELLING_DISCOUNTS.MAX_PERC,DOC05_SELLING_DISCOUNTS.START_DATE,DOC05_SELLING_DISCOUNTS.END_DATE,"+
	          "DOC05_SELLING_DISCOUNTS.DISCOUNT_DESCRIPTION,DOC05_SELLING_DISCOUNTS.VALUE,DOC05_SELLING_DISCOUNTS.PERC,"+
	          "DOC05_SELLING_DISCOUNTS.DOC_TYPE,DOC05_SELLING_DISCOUNTS.DOC_YEAR,DOC05_SELLING_DISCOUNTS.DOC_NUMBER "+
	          "from DOC05_SELLING_DISCOUNTS where "+
	          "DOC05_SELLING_DISCOUNTS.COMPANY_CODE_SYS01=? and "+
	          "DOC05_SELLING_DISCOUNTS.DOC_TYPE=? and "+
	          "DOC05_SELLING_DISCOUNTS.DOC_YEAR=? and "+
	          "DOC05_SELLING_DISCOUNTS.DOC_NUMBER=? ";

	      Map attribute2dbField = new HashMap();
	      attribute2dbField.put("companyCodeSys01DOC05","DOC05_SELLING_DISCOUNTS.COMPANY_CODE_SYS01");
	      attribute2dbField.put("discountCodeSal03DOC05","DOC05_SELLING_DISCOUNTS.DISCOUNT_CODE_SAL03");
	      attribute2dbField.put("minValueDOC05","DOC05_SELLING_DISCOUNTS.MIN_VALUE");
	      attribute2dbField.put("maxValueDOC05","DOC05_SELLING_DISCOUNTS.MAX_VALUE");
	      attribute2dbField.put("minPercDOC05","DOC05_SELLING_DISCOUNTS.MIN_PERC");
	      attribute2dbField.put("maxPercDOC05","DOC05_SELLING_DISCOUNTS.MAX_PERC");
	      attribute2dbField.put("startDateDOC05","DOC05_SELLING_DISCOUNTS.START_DATE");
	      attribute2dbField.put("endDateDOC05","DOC05_SELLING_DISCOUNTS.END_DATE");
	      attribute2dbField.put("discountDescriptionDOC05","DOC05_SELLING_DISCOUNTS.DISCOUNT_DESCRIPTION");
	      attribute2dbField.put("valueDOC05","DOC05_SELLING_DISCOUNTS.VALUE");
	      attribute2dbField.put("percDOC05","DOC05_SELLING_DISCOUNTS.PERC");
	      attribute2dbField.put("docTypeDOC05","DOC05_SELLING_DISCOUNTS.DOC_TYPE");
	      attribute2dbField.put("docYearDOC05","DOC05_SELLING_DISCOUNTS.DOC_YEAR");
	      attribute2dbField.put("docNumberDOC05","DOC05_SELLING_DISCOUNTS.DOC_NUMBER");

	      ArrayList values = new ArrayList();
	      SaleDocPK pk = (SaleDocPK)gridParams.getOtherGridParams().get(ApplicationConsts.SALE_DOC_PK);
	      values.add( pk.getCompanyCodeSys01DOC01() );
	      values.add( pk.getDocTypeDOC01() );
	      values.add( pk.getDocYearDOC01() );
	      values.add( pk.getDocNumberDOC01() );

	      // read from DOC05 table...
	      Response answer = QueryUtil.getQuery(
	          conn,
	          new UserSessionParameters(username),
	          sql,
	          values,
	          attribute2dbField,
	          SaleDocDiscountVO.class,
	          "Y",
	          "N",
	          null,
	          gridParams,
	          true
	      );
	      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

	    }
	    catch (Throwable ex) {
	      Logger.error(username,this.getClass().getName(),"loadSaleDocDiscounts","Error while fetching customer discounts list",ex);
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


