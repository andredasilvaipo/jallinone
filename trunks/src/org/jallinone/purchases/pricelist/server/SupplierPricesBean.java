package org.jallinone.purchases.pricelist.server;

import org.openswing.swing.server.*;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;

import java.sql.*;

import org.openswing.swing.customvo.java.CustomValueObject;
import org.openswing.swing.logger.server.*;
import org.jallinone.system.progressives.server.CompanyProgressiveUtils;
import org.jallinone.system.server.*;
import org.jallinone.variants.java.VariantNameVO;
import org.jallinone.variants.java.VariantsMatrixColumnVO;
import org.jallinone.variants.java.VariantsMatrixRowVO;
import org.jallinone.variants.java.VariantsMatrixUtils;
import org.jallinone.variants.java.VariantsMatrixVO;
import org.jallinone.purchases.items.java.SupplierItemVO;
import org.jallinone.purchases.items.server.SupplierItemsBean;
import org.jallinone.purchases.pricelist.java.*;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.jallinone.items.java.DetailItemVO;
import org.jallinone.items.java.ItemPK;
import org.jallinone.items.server.LoadItemBean;


import javax.sql.DataSource;
import java.sql.ResultSet;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage supplier item prices.</p>
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
public class SupplierPricesBean  implements SupplierPrices {


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



  private SupplierItemsBean insItem;

  public void setInsItem(SupplierItemsBean insItem) {
    this.insItem = insItem;
  }

  private LoadItemBean loadItem;

  public void setLoadItem(LoadItemBean loadItem) {
    this.loadItem = loadItem;
  }


  public SupplierPricesBean() {
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
   */
  public SupplierVariantsPriceVO getSupplierVariantsPrice() {
	  throw new UnsupportedOperationException();
  }

  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
   */
  public SupplierPriceVO getSupplierPrice(SupplierPricelistVO pk) {
	  throw new UnsupportedOperationException();
  }

  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
   */
  public CustomValueObject getCustomValueObject() {
	  throw new UnsupportedOperationException();
  }


  /**
   * Business logic to execute.
   */
  public VOListResponse loadSupplierPrices(GridParams gridParams,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      String companyCodeSYS01 = null;
      String sql = null;

      SupplierPricelistVO vo = (SupplierPricelistVO)gridParams.getOtherGridParams().get(ApplicationConsts.PRICELIST);
      if (vo!=null)
        companyCodeSYS01 = vo.getCompanyCodeSys01PUR03();
      ItemPK pk = (ItemPK)gridParams.getOtherGridParams().get(ApplicationConsts.ITEM_PK);
      if (pk!=null)
        companyCodeSYS01 = pk.getCompanyCodeSys01ITM01();
      BigDecimal progressiveREG04 = (BigDecimal)gridParams.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04);

      sql =
          "select PUR04_SUPPLIER_ITEM_PRICES.COMPANY_CODE_SYS01,PUR04_SUPPLIER_ITEM_PRICES.PRICELIST_CODE_PUR03,PUR04_SUPPLIER_ITEM_PRICES.PROGRESSIVE_REG04,PUR04_SUPPLIER_ITEM_PRICES.ITEM_CODE_ITM01,PUR04_SUPPLIER_ITEM_PRICES.VALUE,PUR04_SUPPLIER_ITEM_PRICES.START_DATE,PUR04_SUPPLIER_ITEM_PRICES.END_DATE,A.DESCRIPTION,ITM01_ITEMS.PROGRESSIVE_HIE02,"+
          "REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,PUR01_SUPPLIERS.SUPPLIER_CODE,B.DESCRIPTION, "+
          "ITM01_ITEMS.USE_VARIANT_1,ITM01_ITEMS.USE_VARIANT_2,ITM01_ITEMS.USE_VARIANT_3,ITM01_ITEMS.USE_VARIANT_4,ITM01_ITEMS.USE_VARIANT_5, "+
					"REG03_CURRENCIES.CURRENCY_SYMBOL,REG03_CURRENCIES.DECIMALS "+
          " from PUR04_SUPPLIER_ITEM_PRICES,SYS10_COMPANY_TRANSLATIONS A,SYS10_COMPANY_TRANSLATIONS B,ITM01_ITEMS,REG04_SUBJECTS,PUR01_SUPPLIERS,PUR03_SUPPLIER_PRICELISTS,REG03_CURRENCIES where "+
          "PUR04_SUPPLIER_ITEM_PRICES.COMPANY_CODE_SYS01=PUR03_SUPPLIER_PRICELISTS.COMPANY_CODE_SYS01 and "+
          "PUR04_SUPPLIER_ITEM_PRICES.PROGRESSIVE_REG04=PUR03_SUPPLIER_PRICELISTS.PROGRESSIVE_REG04 and "+
          "PUR04_SUPPLIER_ITEM_PRICES.PRICELIST_CODE_PUR03=PUR03_SUPPLIER_PRICELISTS.PRICELIST_CODE and "+
					"PUR03_SUPPLIER_PRICELISTS.COMPANY_CODE_SYS01=B.COMPANY_CODE_SYS01 and "+
          "PUR03_SUPPLIER_PRICELISTS.PROGRESSIVE_SYS10=B.PROGRESSIVE and "+
          "B.LANGUAGE_CODE=? and "+
          "PUR04_SUPPLIER_ITEM_PRICES.COMPANY_CODE_SYS01=PUR01_SUPPLIERS.COMPANY_CODE_SYS01 and "+
          "PUR04_SUPPLIER_ITEM_PRICES.PROGRESSIVE_REG04=PUR01_SUPPLIERS.PROGRESSIVE_REG04 and "+
          "PUR04_SUPPLIER_ITEM_PRICES.COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 and "+
          "PUR04_SUPPLIER_ITEM_PRICES.PROGRESSIVE_REG04=REG04_SUBJECTS.PROGRESSIVE and "+
          "PUR04_SUPPLIER_ITEM_PRICES.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
          "PUR04_SUPPLIER_ITEM_PRICES.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and "+
					"ITM01_ITEMS.COMPANY_CODE_SYS01=A.COMPANY_CODE_SYS01 and "+
          "ITM01_ITEMS.PROGRESSIVE_SYS10=A.PROGRESSIVE and "+
          "A.LANGUAGE_CODE=? and PUR04_SUPPLIER_ITEM_PRICES.COMPANY_CODE_SYS01=? and ITM01_ITEMS.ENABLED='Y' AND "+
					"REG03_CURRENCIES.CURRENCY_CODE=PUR03_SUPPLIER_PRICELISTS.CURRENCY_CODE_REG03";

      if (vo!=null) {
        sql +=
            " and PUR04_SUPPLIER_ITEM_PRICES.PRICELIST_CODE_PUR03='" +vo.getPricelistCodePUR03() + "' "+
            " and PUR04_SUPPLIER_ITEM_PRICES.PROGRESSIVE_REG04="+vo.getProgressiveReg04PUR03();
      }
      if (progressiveREG04!=null) {
        sql +=
            " and PUR04_SUPPLIER_ITEM_PRICES.PROGRESSIVE_REG04="+progressiveREG04;
      }
      if (pk!=null)
        sql += " and ITM01_ITEMS.ITEM_CODE='"+pk.getItemCodeITM01()+"' ";

      java.sql.Date filterDate = null;
      if (gridParams.getOtherGridParams().get(ApplicationConsts.DATE_FILTER)!=null) {
        filterDate = new java.sql.Date( ((java.util.Date)gridParams.getOtherGridParams().get(ApplicationConsts.DATE_FILTER)).getTime() );
        sql +=
					" and PUR04_SUPPLIER_ITEM_PRICES.START_DATE<=? "+
					" and (PUR04_SUPPLIER_ITEM_PRICES.END_DATE>? or PUR04_SUPPLIER_ITEM_PRICES.END_DATE is null) ";
      }


      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01PUR04","PUR04_SUPPLIER_ITEM_PRICES.COMPANY_CODE_SYS01");
      attribute2dbField.put("pricelistCodePur03PUR04","PUR04_SUPPLIER_ITEM_PRICES.PRICELIST_CODE_PUR03");
      attribute2dbField.put("progressiveReg04PUR04","PUR04_SUPPLIER_ITEM_PRICES.PROGRESSIVE_REG04");
      attribute2dbField.put("itemCodeItm01PUR04","PUR04_SUPPLIER_ITEM_PRICES.ITEM_CODE_ITM01");
      attribute2dbField.put("valuePUR04","PUR04_SUPPLIER_ITEM_PRICES.VALUE");
      attribute2dbField.put("startDatePUR04","PUR04_SUPPLIER_ITEM_PRICES.START_DATE");
      attribute2dbField.put("endDatePUR04","PUR04_SUPPLIER_ITEM_PRICES.END_DATE");
      attribute2dbField.put("itemDescriptionSYS10","A.DESCRIPTION");
      attribute2dbField.put("pricelistDescriptionSYS10","B.DESCRIPTION");
      attribute2dbField.put("progressiveHie02ITM01","ITM01_ITEMS.PROGRESSIVE_HIE02");
      attribute2dbField.put("name_1REG04","REG04_SUBJECTS.NAME_1");
      attribute2dbField.put("name_2REG04","REG04_SUBJECTS.NAME_2");
      attribute2dbField.put("supplierCodePUR01","PUR01_SUPPLIERS.SUPPLIER_CODE");

      attribute2dbField.put("useVariant1ITM01","ITM01_ITEMS.USE_VARIANT_1");
      attribute2dbField.put("useVariant2ITM01","ITM01_ITEMS.USE_VARIANT_2");
      attribute2dbField.put("useVariant3ITM01","ITM01_ITEMS.USE_VARIANT_3");
      attribute2dbField.put("useVariant4ITM01","ITM01_ITEMS.USE_VARIANT_4");
      attribute2dbField.put("useVariant5ITM01","ITM01_ITEMS.USE_VARIANT_5");

      attribute2dbField.put("currencySymbolREG03","REG03_CURRENCIES.CURRENCY_SYMBOL");
      attribute2dbField.put("decimalsREG03","REG03_CURRENCIES.DECIMALS");


      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(serverLanguageId);
      values.add(companyCodeSYS01);
      if (filterDate!=null) {
        values.add(filterDate);
        values.add(filterDate);
      }

      // read from PUR04 table...
      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          SupplierPriceVO.class,
          "Y",
          "N",
          null,
          gridParams,
          50,
          true
      );

      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;
    }
    catch (Throwable ex) {
    	Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching supplier item prices list",ex);
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




  /**
   * Business logic to execute.
   */
  public VOListResponse loadSupplierVariantsPrices(VariantsMatrixVO matrixVO,ItemPK itemPK,String priceListCode,BigDecimal progressiveReg04,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      String sql =
            "select PUR05_SUP_ITEM_VARIANTS_PRICES.PROGRESSIVE,PUR05_SUP_ITEM_VARIANTS_PRICES.COMPANY_CODE_SYS01,"+
            "PUR05_SUP_ITEM_VARIANTS_PRICES.PROGRESSIVE_REG04,PUR05_SUP_ITEM_VARIANTS_PRICES.PRICELIST_CODE_PUR03,"+
            "PUR05_SUP_ITEM_VARIANTS_PRICES.ITEM_CODE_ITM01,PUR05_SUP_ITEM_VARIANTS_PRICES.VALUE,"+
            "PUR05_SUP_ITEM_VARIANTS_PRICES.START_DATE,PUR05_SUP_ITEM_VARIANTS_PRICES.END_DATE,"+
            "PUR05_SUP_ITEM_VARIANTS_PRICES.VARIANT_TYPE_ITM06,PUR05_SUP_ITEM_VARIANTS_PRICES.VARIANT_CODE_ITM11,"+
            "PUR05_SUP_ITEM_VARIANTS_PRICES.VARIANT_TYPE_ITM07,PUR05_SUP_ITEM_VARIANTS_PRICES.VARIANT_CODE_ITM12,"+
            "PUR05_SUP_ITEM_VARIANTS_PRICES.VARIANT_TYPE_ITM08,PUR05_SUP_ITEM_VARIANTS_PRICES.VARIANT_CODE_ITM13,"+
            "PUR05_SUP_ITEM_VARIANTS_PRICES.VARIANT_TYPE_ITM09,PUR05_SUP_ITEM_VARIANTS_PRICES.VARIANT_CODE_ITM14,"+
            "PUR05_SUP_ITEM_VARIANTS_PRICES.VARIANT_TYPE_ITM10,PUR05_SUP_ITEM_VARIANTS_PRICES.VARIANT_CODE_ITM15 "+
            " from PUR05_SUP_ITEM_VARIANTS_PRICES where "+
            "PUR05_SUP_ITEM_VARIANTS_PRICES.COMPANY_CODE_SYS01=? and "+
            "PUR05_SUP_ITEM_VARIANTS_PRICES.PROGRESSIVE_REG04=? and "+
            "PUR05_SUP_ITEM_VARIANTS_PRICES.PRICELIST_CODE_PUR03=? and "+
            "PUR05_SUP_ITEM_VARIANTS_PRICES.ITEM_CODE_ITM01=?";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("progressivePUR05","PUR05_SUP_ITEM_VARIANTS_PRICES.PROGRESSIVE");
      attribute2dbField.put("companyCodeSys01PUR05","PUR05_SUP_ITEM_VARIANTS_PRICES.COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveReg04PUR05","PUR05_SUP_ITEM_VARIANTS_PRICES.PROGRESSIVE_REG04");
      attribute2dbField.put("pricelistCodePur03PUR05","PUR05_SUP_ITEM_VARIANTS_PRICES.PRICELIST_CODE_PUR03");
      attribute2dbField.put("itemCodeItm01PUR05","PUR05_SUP_ITEM_VARIANTS_PRICES.ITEM_CODE_ITM01");
      attribute2dbField.put("valuePUR05","PUR05_SUP_ITEM_VARIANTS_PRICES.VALUE");
      attribute2dbField.put("startDatePUR05","PUR05_SUP_ITEM_VARIANTS_PRICES.START_DATE");
      attribute2dbField.put("endDatePUR05","PUR05_SUP_ITEM_VARIANTS_PRICES.END_DATE");
      attribute2dbField.put("itemDescriptionSYS10","A.DESCRIPTION");
      attribute2dbField.put("pricelistDescriptionSYS10","B.DESCRIPTION");
      attribute2dbField.put("progressiveHie02ITM01","ITM01_ITEMS.PROGRESSIVE_HIE02");

      attribute2dbField.put("variantTypeItm06PUR05","PUR05_SUP_ITEM_VARIANTS_PRICES.VARIANT_TYPE_ITM06");
      attribute2dbField.put("variantCodeItm11PUR05","PUR05_SUP_ITEM_VARIANTS_PRICES.VARIANT_CODE_ITM11");
      attribute2dbField.put("variantTypeItm07PUR05","PUR05_SUP_ITEM_VARIANTS_PRICES.VARIANT_TYPE_ITM07");
      attribute2dbField.put("variantCodeItm12PUR05","PUR05_SUP_ITEM_VARIANTS_PRICES.VARIANT_CODE_ITM12");
      attribute2dbField.put("variantTypeItm08PUR05","PUR05_SUP_ITEM_VARIANTS_PRICES.VARIANT_TYPE_ITM08");
      attribute2dbField.put("variantCodeItm13PUR05","PUR05_SUP_ITEM_VARIANTS_PRICES.VARIANT_CODE_ITM13");
      attribute2dbField.put("variantTypeItm09PUR05","PUR05_SUP_ITEM_VARIANTS_PRICES.VARIANT_TYPE_ITM09");
      attribute2dbField.put("variantCodeItm14PUR05","PUR05_SUP_ITEM_VARIANTS_PRICES.VARIANT_CODE_ITM14");
      attribute2dbField.put("variantTypeItm10PUR05","PUR05_SUP_ITEM_VARIANTS_PRICES.VARIANT_TYPE_ITM10");
      attribute2dbField.put("variantCodeItm15PUR05","PUR05_SUP_ITEM_VARIANTS_PRICES.VARIANT_CODE_ITM15");

      ArrayList values = new ArrayList();
      values.add(itemPK.getCompanyCodeSys01ITM01());
      values.add(progressiveReg04);
      values.add(priceListCode);
      values.add(itemPK.getItemCodeITM01());


      // read ALL from PUR05 table...
      Response res = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          SupplierVariantsPriceVO.class,
          "Y",
          "N",
          null,
          new GridParams(),
          true
      );

      if (res.isError())
        throw new Exception(res.getErrorMessage());


      java.util.List rows = ((VOListResponse)res).getRows();


      // convert the records list in matrix format...
      ArrayList matrixRows = new ArrayList();
      SupplierVariantsPriceVO vo = null;
      CustomValueObject customVO = null;
      VariantsMatrixRowVO rowVO = null;
      VariantsMatrixColumnVO colVO = null;
      HashMap indexes = new HashMap();
      int cols = matrixVO.getColumnDescriptors().length==0?1:matrixVO.getColumnDescriptors().length;
      for(int i=0;i<matrixVO.getRowDescriptors().length;i++) {
        rowVO = (VariantsMatrixRowVO)matrixVO.getRowDescriptors()[i];

        customVO = new CustomValueObject();
        customVO.setAttributeNameS0(rowVO.getRowDescription());
        matrixRows.add(customVO);
        indexes.put(
          VariantsMatrixUtils.getVariantType(matrixVO,rowVO)+" "+VariantsMatrixUtils.getVariantCode(matrixVO,rowVO),
          customVO
        );
      }
      VariantNameVO varVO = (VariantNameVO)matrixVO.getManagedVariants()[0];
      for(int i=0;i<rows.size();i++) {
        vo = (SupplierVariantsPriceVO)rows.get(i);

        if (varVO.getTableName().equals("ITM11_VARIANTS_1")) {
          customVO = (CustomValueObject)indexes.get(vo.getVariantTypeItm06PUR05()+" "+vo.getVariantCodeItm11PUR05());
        }
        else if (varVO.getTableName().equals("ITM12_VARIANTS_2")) {
          customVO = (CustomValueObject)indexes.get(vo.getVariantTypeItm07PUR05()+" "+vo.getVariantCodeItm12PUR05());
        }
        else if (varVO.getTableName().equals("ITM13_VARIANTS_3")) {
          customVO = (CustomValueObject)indexes.get(vo.getVariantTypeItm08PUR05()+" "+vo.getVariantCodeItm13PUR05());
        }
        else if (varVO.getTableName().equals("ITM14_VARIANTS_4")) {
          customVO = (CustomValueObject)indexes.get(vo.getVariantTypeItm09PUR05()+" "+vo.getVariantCodeItm14PUR05());
        }
        else if (varVO.getTableName().equals("ITM15_VARIANTS_5")) {
          customVO = (CustomValueObject)indexes.get(vo.getVariantTypeItm10PUR05()+" "+vo.getVariantCodeItm15PUR05());
        }

        if (matrixVO.getColumnDescriptors().length==0) {
          customVO.setAttributeNameN0(vo.getValuePUR05());
        }
        else {

          for(int j=0;j<cols;j++) {
            colVO = (VariantsMatrixColumnVO)matrixVO.getColumnDescriptors()[j];
            if ((varVO.getTableName().equals("ITM11_VARIANTS_1")?true:colVO.getVariantCodeITM11().equals(vo.getVariantCodeItm11PUR05())) &&
                (varVO.getTableName().equals("ITM12_VARIANTS_2")?true:colVO.getVariantCodeITM12().equals(vo.getVariantCodeItm12PUR05())) &&
                (varVO.getTableName().equals("ITM13_VARIANTS_3")?true:colVO.getVariantCodeITM13().equals(vo.getVariantCodeItm13PUR05())) &&
                (varVO.getTableName().equals("ITM14_VARIANTS_4")?true:colVO.getVariantCodeITM14().equals(vo.getVariantCodeItm14PUR05())) &&
                (varVO.getTableName().equals("ITM15_VARIANTS_5")?true:colVO.getVariantCodeITM15().equals(vo.getVariantCodeItm15PUR05())) &&
                (varVO.getTableName().equals("ITM11_VARIANTS_1")?true:colVO.getVariantTypeITM06().equals(vo.getVariantTypeItm06PUR05())) &&
                (varVO.getTableName().equals("ITM12_VARIANTS_2")?true:colVO.getVariantTypeITM07().equals(vo.getVariantTypeItm07PUR05())) &&
                (varVO.getTableName().equals("ITM13_VARIANTS_3")?true:colVO.getVariantTypeITM08().equals(vo.getVariantTypeItm08PUR05())) &&
                (varVO.getTableName().equals("ITM14_VARIANTS_4")?true:colVO.getVariantTypeITM09().equals(vo.getVariantTypeItm09PUR05())) &&
                (varVO.getTableName().equals("ITM15_VARIANTS_5")?true:colVO.getVariantTypeITM10().equals(vo.getVariantTypeItm10PUR05()))) {
              try {
                CustomValueObject.class.getMethod("setAttributeNameN" + j,new Class[] {BigDecimal.class}).invoke(customVO, new Object[] {vo.getValuePUR05()});
              }
              catch (Throwable ex) {
                ex.printStackTrace();
              }
              break;
            }
          }

        } // end else
      } // end for on rows

      return new VOListResponse(matrixRows,false,matrixRows.size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching supplier's prices list",ex);
  	try {
		if (this.conn==null && conn!=null)
			// rollback only local connection
			conn.rollback();
	}
	catch (Exception ex3) {
	}      throw new Exception(ex.getMessage());
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
   * Business logic to execute.
   */
  public VOListResponse updateSupplierPrices(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
		PreparedStatement pstmt0 = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

			pstmt0 = conn.prepareStatement(
				"select * from PUR04_SUPPLIER_ITEM_PRICES WHERE "+
				"COMPANY_CODE_SYS01=? AND PRICELIST_CODE_PUR03=? AND PROGRESSIVE_REG04=? AND ITEM_CODE_ITM01=? AND NOT START_DATE=? AND  "+
				"(START_DATE>? AND END_DATE<? or START_DATE<? AND END_DATE>?) "
			);

			 pstmt1 = conn.prepareStatement(
			 "select * from PUR04_SUPPLIER_ITEM_PRICES WHERE "+
			 "COMPANY_CODE_SYS01=? AND PRICELIST_CODE_PUR03=? AND PROGRESSIVE_REG04=? AND ITEM_CODE_ITM01=? AND NOT START_DATE=? AND  "+
			 "(START_DATE>? AND END_DATE<? or START_DATE<? AND END_DATE>? or START_DATE>? ) "
			);

			pstmt2 = conn.prepareStatement(
				 "UPDATE PUR04_SUPPLIER_ITEM_PRICES SET END_DATE=? WHERE "+
				 "COMPANY_CODE_SYS01=? AND PRICELIST_CODE_PUR03=? AND PROGRESSIVE_REG04=? AND ITEM_CODE_ITM01=? AND NOT START_DATE=? AND "+
				 "(START_DATE<? AND END_DATE is null or START_DATE<? AND END_DATE>? ) "
			);

			 pstmt3 = conn.prepareStatement(
				"UPDATE PUR04_SUPPLIER_ITEM_PRICES SET END_DATE=? WHERE "+
				"COMPANY_CODE_SYS01=? AND PRICELIST_CODE_PUR03=? AND PROGRESSIVE_REG04=? AND ITEM_CODE_ITM01=? AND NOT START_DATE=? AND "+
				"(END_DATE is null or START_DATE<? AND END_DATE>? ) "
			 );


			 // if current price is valid for [d1,d2]
			 // then invervals to redefine are:
			 // [...,null] -> [...,d1]
			 // [<d1,>d1] -> [...,d1]
			 // [>d1,<d2] -> error
			 // [<d2,>d2] -> error

			 // if current price is valid for [d1,null]
			 // then invervals to redefine are:
			 // [...,null] -> [...,d1]
			 // [<d1,>d1] -> [...,d1]
			 // [>d1,<d2] -> error
			 // [<d2,>d2] -> error
			 // [>d2,>d2] -> error

      SupplierPriceVO oldVO = null;
      SupplierPriceVO vo = null;
      Response res = null;

			HashSet pkAttrs = new HashSet();
			pkAttrs.add("companyCodeSys01PUR04");
			pkAttrs.add("pricelistCodePur03PUR04");
			pkAttrs.add("itemCodeItm01PUR04");
			pkAttrs.add("progressiveReg04PUR04");
			pkAttrs.add("startDatePUR04");

			HashMap attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01PUR04","COMPANY_CODE_SYS01");
			attribute2dbField.put("pricelistCodePur03PUR04","PRICELIST_CODE_PUR03");
			attribute2dbField.put("progressiveReg04PUR04","PROGRESSIVE_REG04");
			attribute2dbField.put("itemCodeItm01PUR04","ITEM_CODE_ITM01");
			attribute2dbField.put("valuePUR04","VALUE");
			attribute2dbField.put("startDatePUR04","START_DATE");
			attribute2dbField.put("endDatePUR04","END_DATE");


      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (SupplierPriceVO)oldVOs.get(i);
        vo = (SupplierPriceVO)newVOs.get(i);

				if (vo.getEndDatePUR04()!=null) {

					pstmt0.setString(1,vo.getCompanyCodeSys01PUR04());
					pstmt0.setString(2,vo.getPricelistCodePur03PUR04());
					pstmt0.setBigDecimal(3,vo.getProgressiveReg04PUR04());
					pstmt0.setString(4,vo.getItemCodeItm01());
					pstmt0.setDate(5,vo.getStartDatePUR04());
					pstmt0.setDate(6,vo.getStartDatePUR04());
					pstmt0.setDate(7,vo.getEndDatePUR04());
					pstmt0.setDate(8,vo.getEndDatePUR04());
					pstmt0.setDate(9,vo.getEndDatePUR04());
					ResultSet rset = pstmt0.executeQuery();
					boolean found = rset.next();
					rset.close();
					if (found)
						throw new Exception("change date interval");

					pstmt2.setDate(1,vo.getStartDatePUR04());
					pstmt2.setString(2,vo.getCompanyCodeSys01PUR04());
					pstmt2.setString(3,vo.getPricelistCodePur03PUR04());
					pstmt2.setBigDecimal(4,vo.getProgressiveReg04PUR04());
					pstmt2.setString(5,vo.getItemCodeItm01());
					pstmt2.setDate(6,vo.getStartDatePUR04());
					pstmt2.setDate(7,vo.getEndDatePUR04());
					pstmt2.setDate(8,vo.getStartDatePUR04());
					pstmt2.setDate(9,vo.getStartDatePUR04());
					pstmt2.execute();

				}
				else {

					pstmt1.setString(1,vo.getCompanyCodeSys01PUR04());
					pstmt1.setString(2,vo.getPricelistCodePur03PUR04());
					pstmt1.setBigDecimal(3,vo.getProgressiveReg04PUR04());
					pstmt1.setString(4,vo.getItemCodeItm01());
					pstmt1.setDate(5,vo.getStartDatePUR04());
					pstmt1.setDate(6,vo.getStartDatePUR04());
					pstmt1.setDate(7,vo.getEndDatePUR04());
					pstmt1.setDate(8,vo.getEndDatePUR04());
					pstmt1.setDate(9,vo.getEndDatePUR04());
					pstmt1.setDate(10,vo.getEndDatePUR04());
					ResultSet rset = pstmt1.executeQuery();
					boolean found = rset.next();
					rset.close();
					if (found)
						throw new Exception("change date interval");

					pstmt3.setDate(1,vo.getStartDatePUR04());
					pstmt3.setString(2,vo.getCompanyCodeSys01PUR04());
					pstmt3.setString(3,vo.getPricelistCodePur03PUR04());
					pstmt3.setBigDecimal(4,vo.getProgressiveReg04PUR04());
					pstmt3.setString(5,vo.getItemCodeItm01());
					pstmt3.setDate(6,vo.getStartDatePUR04());
					pstmt3.setDate(7,vo.getStartDatePUR04());
					pstmt3.setDate(8,vo.getStartDatePUR04());
					pstmt3.execute();

				}

        res = org.jallinone.commons.server.QueryUtilExtension.updateTable(
            conn,
            new UserSessionParameters(username),
            pkAttrs,
            oldVO,
            vo,
            "PUR04_SUPPLIER_ITEM_PRICES",
            attribute2dbField,
            "Y",
            "N",
            null,
            true
        );
        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }
      }

      return new VOListResponse(newVOs,false,newVOs.size());
    }
    catch (Throwable ex) {
    	Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing supplier prices",ex);
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
				if (pstmt0!=null)
					pstmt0.close();
			}
			catch (Exception ex1) {
			}
			try {
				if (pstmt1!=null)
					pstmt1.close();
			}
			catch (Exception ex1) {
			}
			try {
				if (pstmt2!=null)
					pstmt2.close();
			}
			catch (Exception ex1) {
			}
			try {
				if (pstmt3!=null)
					pstmt3.close();
			}
			catch (Exception ex1) {
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




  /**
   * Business logic to execute.
   */
  public VOResponse updateSupplierVariantsPrices(VariantsPrice variantsPrice,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;

    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      // remove all prices related to the specified item/pricelist...
      pstmt = conn.prepareStatement("delete from PUR05_SUP_ITEM_VARIANTS_PRICES where COMPANY_CODE_SYS01=? and PROGRESSIVE_REG04=? and PRICELIST_CODE_PUR03=? and ITEM_CODE_ITM01=?");
      pstmt.setString(1,variantsPrice.getMatrixVO().getItemPK().getCompanyCodeSys01ITM01());
      pstmt.setBigDecimal(2,variantsPrice.getProgressiveReg04());
      pstmt.setString(3,variantsPrice.getPriceListCode());
      pstmt.setString(4,variantsPrice.getMatrixVO().getItemPK().getItemCodeITM01());
      pstmt.execute();

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("progressivePUR05","PROGRESSIVE");
      attribute2dbField.put("companyCodeSys01PUR05","COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveReg04PUR05","PROGRESSIVE_REG04");
      attribute2dbField.put("pricelistCodePur03PUR05","PRICELIST_CODE_PUR03");
      attribute2dbField.put("itemCodeItm01PUR05","ITEM_CODE_ITM01");
      attribute2dbField.put("valuePUR05","VALUE");
      attribute2dbField.put("startDatePUR05","START_DATE");
      attribute2dbField.put("endDatePUR05","END_DATE");

      attribute2dbField.put("variantTypeItm06PUR05","VARIANT_TYPE_ITM06");
      attribute2dbField.put("variantCodeItm11PUR05","VARIANT_CODE_ITM11");
      attribute2dbField.put("variantTypeItm07PUR05","VARIANT_TYPE_ITM07");
      attribute2dbField.put("variantCodeItm12PUR05","VARIANT_CODE_ITM12");
      attribute2dbField.put("variantTypeItm08PUR05","VARIANT_TYPE_ITM08");
      attribute2dbField.put("variantCodeItm13PUR05","VARIANT_CODE_ITM13");
      attribute2dbField.put("variantTypeItm09PUR05","VARIANT_TYPE_ITM09");
      attribute2dbField.put("variantCodeItm14PUR05","VARIANT_CODE_ITM14");
      attribute2dbField.put("variantTypeItm10PUR05","VARIANT_TYPE_ITM10");
      attribute2dbField.put("variantCodeItm15PUR05","VARIANT_CODE_ITM15");


      // insert into PUR05...
      SupplierVariantsPriceVO vo = null;
      Response res = null;
      Object[] row = null;
      VariantsMatrixColumnVO colVO = null;
      VariantsMatrixRowVO rowVO = null;
      BigDecimal price = null;
      for(int i=0;i<variantsPrice.getCells().length;i++) {
        row = variantsPrice.getCells()[i];
        rowVO = (VariantsMatrixRowVO)variantsPrice.getMatrixVO().getRowDescriptors()[i];

        if (variantsPrice.getMatrixVO().getColumnDescriptors().length==0) {

          if (variantsPrice.getCells()[i][0]!=null) {
        	try {
				price = (BigDecimal)variantsPrice.getCells()[i][0];
			} catch (Exception e) {
				continue;
			}

            vo = new SupplierVariantsPriceVO();
            vo.setCompanyCodeSys01PUR05(variantsPrice.getMatrixVO().getItemPK().getCompanyCodeSys01ITM01());
            vo.setProgressivePUR05(CompanyProgressiveUtils.getInternalProgressive(vo.getCompanyCodeSys01PUR05(),"PUR05_SUPPLIER_VARIANTS_PRICES","PROGRESSIVE",conn));
            vo.setItemCodeItm01PUR05(variantsPrice.getMatrixVO().getItemPK().getItemCodeITM01());
            vo.setProgressiveReg04PUR05(variantsPrice.getProgressiveReg04());
            vo.setPricelistCodePur03PUR05(variantsPrice.getPriceListCode());
            vo.setValuePUR05(price);
            vo.setStartDatePUR05(variantsPrice.getStartDate());
            vo.setEndDatePUR05(variantsPrice.getEndDate());
            VariantsMatrixUtils.setVariantTypesAndCodes(vo,"PUR05",variantsPrice.getMatrixVO(),rowVO,null);

            res = org.jallinone.commons.server.QueryUtilExtension.insertTable(
                conn,
                new UserSessionParameters(username),
                vo,
                "PUR05_SUP_ITEM_VARIANTS_PRICES",
                attribute2dbField,
                "Y",
                "N",
                null,
                true
            );
            if (res.isError()) {
              throw new Exception(res.getErrorMessage());
            }
          }

        }
        else
          for(int k=0;k<variantsPrice.getMatrixVO().getColumnDescriptors().length;k++) {

            colVO = (VariantsMatrixColumnVO)variantsPrice.getMatrixVO().getColumnDescriptors()[k];
            if (variantsPrice.getCells()[i][k]!=null) {
	          try {
	        		price = (BigDecimal)variantsPrice.getCells()[i][k];
	          } catch (Exception e) {
	        		continue;
	          }
              vo = new SupplierVariantsPriceVO();
              vo.setCompanyCodeSys01PUR05(variantsPrice.getMatrixVO().getItemPK().getCompanyCodeSys01ITM01());
              vo.setProgressivePUR05(CompanyProgressiveUtils.getInternalProgressive(vo.getCompanyCodeSys01PUR05(),"PUR05_SUP_ITEM_VARIANTS_PRICES","PROGRESSIVE",conn));
              vo.setItemCodeItm01PUR05(variantsPrice.getMatrixVO().getItemPK().getItemCodeITM01());
              vo.setProgressiveReg04PUR05(variantsPrice.getProgressiveReg04());
              vo.setPricelistCodePur03PUR05(variantsPrice.getPriceListCode());
              vo.setValuePUR05(price);
              vo.setStartDatePUR05(variantsPrice.getStartDate());
              vo.setEndDatePUR05(variantsPrice.getEndDate());
              VariantsMatrixUtils.setVariantTypesAndCodes(vo,"PUR05",variantsPrice.getMatrixVO(),rowVO,colVO);

              res = org.jallinone.commons.server.QueryUtilExtension.insertTable(
                  conn,
                  new UserSessionParameters(username),
                  vo,
                  "PUR05_SUP_ITEM_VARIANTS_PRICES",
                  attribute2dbField,
                  "Y",
                  "N",
                  null,
                  true
              );
              if (res.isError()) {
                throw new Exception(res.getErrorMessage());
              }

            } // end if on cell not null
          } // end inner for
      } // end outer for

      return new VOResponse(Boolean.TRUE);
    }
    catch (Throwable ex) {
    	Logger.error(username, this.getClass().getName(),
    			"executeCommand", "Error while inserting price for variants", ex);
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

    }
  }


  private boolean containsVariant(VariantsMatrixVO vo,String tableName) {
    for(int i=0;i<vo.getManagedVariants().length;i++)
      if (((VariantNameVO)vo.getManagedVariants()[i]).getTableName().equals(tableName))
        return true;
    return false;
  }




  /**
   * Business logic to execute.
   */
  public VOResponse importAllSupplierItems(SupplierPricelistChanges vo,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    PreparedStatement pstmt2 = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      pstmt = conn.prepareStatement(
        "select ITEM_CODE_ITM01 from PUR02_SUPPLIER_ITEMS where ENABLED='Y' and COMPANY_CODE_SYS01=? and PROGRESSIVE_REG04=?"
      );
      pstmt2 = conn.prepareStatement(
        "insert into PUR04_SUPPLIER_ITEM_PRICES(COMPANY_CODE_SYS01,PRICELIST_CODE_PUR03,ITEM_CODE_ITM01,VALUE,START_DATE,END_DATE,PROGRESSIVE_REG04,CREATE_USER,CREATE_DATE) values(?,?,?,?,?,?,?,?,?)"
      );

      pstmt.setString(1,vo.getCompanyCodeSys01PUR04());
      pstmt.setBigDecimal(2,vo.getProgressiveReg04PUR04());
      ResultSet rset = pstmt.executeQuery();
      while(rset.next()) {
        pstmt2.setString(1,vo.getCompanyCodeSys01PUR04());
        pstmt2.setString(2,vo.getPricelistCodePur03PUR04());
        pstmt2.setString(3,rset.getString(1));
        pstmt2.setBigDecimal(4,vo.getDeltaValue());
        pstmt2.setDate(5,vo.getStartDate());
        pstmt2.setDate(6,vo.getEndDate());
        pstmt2.setBigDecimal(7,vo.getProgressiveReg04PUR04());
				pstmt2.setString(8,username);
				pstmt2.setTimestamp(9,new java.sql.Timestamp(System.currentTimeMillis()));
        try {
          pstmt2.executeUpdate();
        }
        catch (SQLException ex4) {
        }
      }
      rset.close();

      return new VOResponse(Boolean.TRUE);
    }
    catch (Throwable ex) {
      Logger.error(username, this.getClass().getName(),
    		  "executeCommand", "Error while inserting price for all items", ex);
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
   * Business logic to execute.
   */
  public VOListResponse insertSupplierPrices(ArrayList list,String serverLanguageId,String username,String imagePath,ArrayList companiesList,ArrayList customizedFields) throws Throwable {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    Connection conn = null;
		PreparedStatement pstmt0 = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      insItem.setConn(conn); // use same transaction...
      loadItem.setConn(conn); // use same transaction...

      pstmt = conn.prepareStatement(
        "select ITEM_CODE_ITM01 from PUR02_SUPPLIER_ITEMS where "+
        "COMPANY_CODE_SYS01=? and PROGRESSIVE_REG04=? and ITEM_CODE_ITM01=?"
      );

			pstmt0 = conn.prepareStatement(
				"select * from PUR04_SUPPLIER_ITEM_PRICES WHERE "+
				"COMPANY_CODE_SYS01=? AND PRICELIST_CODE_PUR03=? and PROGRESSIVE_REG04=? AND ITEM_CODE_ITM01=? AND NOT START_DATE=? AND  "+
				"(START_DATE>? AND END_DATE<? or START_DATE<? AND END_DATE>?) "
			);

			pstmt1 = conn.prepareStatement(
			 "select * from PUR04_SUPPLIER_ITEM_PRICES WHERE "+
			 "COMPANY_CODE_SYS01=? AND PRICELIST_CODE_PUR03=? and PROGRESSIVE_REG04=? AND ITEM_CODE_ITM01=? AND NOT START_DATE=? AND  "+
			 "(START_DATE>? AND END_DATE<? or START_DATE<? AND END_DATE>? or START_DATE>? ) "
			);

			pstmt2 = conn.prepareStatement(
				 "UPDATE PUR04_SUPPLIER_ITEM_PRICES SET END_DATE=? WHERE "+
				 "COMPANY_CODE_SYS01=? AND PRICELIST_CODE_PUR03=? and PROGRESSIVE_REG04=? AND ITEM_CODE_ITM01=? AND NOT START_DATE=? AND "+
				 "(START_DATE<? AND END_DATE is null or START_DATE<? AND END_DATE>? ) "
			);

		  pstmt3 = conn.prepareStatement(
				"UPDATE PUR04_SUPPLIER_ITEM_PRICES SET END_DATE=? WHERE "+
				"COMPANY_CODE_SYS01=? AND PRICELIST_CODE_PUR03=? and PROGRESSIVE_REG04=? AND ITEM_CODE_ITM01=? AND NOT START_DATE=? AND "+
				"(END_DATE is null or START_DATE<? AND END_DATE>? ) "
		  );


			// if current price is valid for [d1,d2]
			// then invervals to redefine are:
			// [...,null] -> [...,d1]
			// [<d1,>d1] -> [...,d1]
			// [>d1,<d2] -> error
			// [<d2,>d2] -> error

			// if current price is valid for [d1,null]
			// then invervals to redefine are:
			// [...,null] -> [...,d1]
			// [<d1,>d1] -> [...,d1]
			// [>d1,<d2] -> error
			// [<d2,>d2] -> error
			// [>d2,>d2] -> error


      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01PUR04","COMPANY_CODE_SYS01");
      attribute2dbField.put("pricelistCodePur03PUR04","PRICELIST_CODE_PUR03");
      attribute2dbField.put("progressiveReg04PUR04","PROGRESSIVE_REG04");
      attribute2dbField.put("itemCodeItm01PUR04","ITEM_CODE_ITM01");
      attribute2dbField.put("valuePUR04","VALUE");
      attribute2dbField.put("startDatePUR04","START_DATE");
      attribute2dbField.put("endDatePUR04","END_DATE");

      // insert into PUR04...
      SupplierPriceVO vo = null;
      Response res = null;
      ArrayList items = new ArrayList();
      SupplierItemVO itemVO = new SupplierItemVO();
      items.add(itemVO);
      DetailItemVO detailItemVO = null;
      for(int i=0;i<list.size();i++) {
        vo = (SupplierPriceVO)list.get(i);

        // check if the item is already defined in a supplier-pricelist, otherwise it will be added...
        pstmt.setString(1,vo.getCompanyCodeSys01PUR04());
        pstmt.setBigDecimal(2,vo.getProgressiveReg04PUR04());
        pstmt.setString(3,vo.getItemCodeItm01PUR04());
        rset = pstmt.executeQuery();
        if (!rset.next()) {
          // item not found: it will be added...
        	ItemPK pk = new ItemPK(vo.getCompanyCodeSys01PUR04(),vo.getItemCodeItm01PUR04());
        	BigDecimal prog = loadItem.getProgressiveHie02ITM01(pk, username);

          res = new VOResponse(loadItem.loadItem(pk,prog,serverLanguageId,username,imagePath,new ArrayList()));
          if (res.isError()) {
            throw new Exception(res.getErrorMessage());
          }
          detailItemVO = (DetailItemVO)((VOResponse)res).getVo();
          itemVO.setCompanyCodeSys01PUR02(vo.getCompanyCodeSys01PUR04());
          itemVO.setDecimalsREG02(detailItemVO.getMinSellingQtyDecimalsREG02());
          itemVO.setItemCodeItm01PUR02(vo.getItemCodeItm01PUR04());
          itemVO.setMinPurchaseQtyPUR02(detailItemVO.getMinSellingQtyITM01());
          itemVO.setMultipleQtyPUR02(itemVO.getMinPurchaseQtyPUR02());
          itemVO.setProgressiveHie01PUR02(detailItemVO.getProgressiveHie01ITM01());
          itemVO.setProgressiveHie02PUR02(detailItemVO.getProgressiveHie02ITM01());
          itemVO.setProgressiveReg04PUR02(vo.getProgressiveReg04PUR04());
          itemVO.setSupplierItemCodePUR02(vo.getItemCodeItm01PUR04());
          itemVO.setUmCodeReg02PUR02(detailItemVO.getMinSellingQtyUmCodeReg02ITM01());

          res = insItem.insertSupplierItems(items,serverLanguageId,username,companiesList,customizedFields);
          if (res.isError()) {
            throw new Exception(res.getErrorMessage());
          }
        }
        rset.close();

				if (vo.getEndDatePUR04()!=null) {

					pstmt0.setString(1,vo.getCompanyCodeSys01PUR04());
					pstmt0.setString(2,vo.getPricelistCodePur03PUR04());
					pstmt0.setBigDecimal(3,vo.getProgressiveReg04PUR04());
					pstmt0.setString(4,vo.getItemCodeItm01());
					pstmt0.setDate(5,vo.getStartDatePUR04());
					pstmt0.setDate(6,vo.getStartDatePUR04());
					pstmt0.setDate(7,vo.getEndDatePUR04());
					pstmt0.setDate(8,vo.getEndDatePUR04());
					pstmt0.setDate(9,vo.getEndDatePUR04());
					rset = pstmt0.executeQuery();
					boolean found = rset.next();
					rset.close();
					if (found)
						throw new Exception("change date interval");

					pstmt2.setDate(1,vo.getStartDatePUR04());
					pstmt2.setString(2,vo.getCompanyCodeSys01PUR04());
					pstmt2.setString(3,vo.getPricelistCodePur03PUR04());
					pstmt2.setBigDecimal(4,vo.getProgressiveReg04PUR04());
					pstmt2.setString(5,vo.getItemCodeItm01());
					pstmt2.setDate(6,vo.getStartDatePUR04());
					pstmt2.setDate(7,vo.getEndDatePUR04());
					pstmt2.setDate(8,vo.getStartDatePUR04());
					pstmt2.setDate(9,vo.getStartDatePUR04());
					pstmt2.execute();

				}
				else {

					pstmt1.setString(1,vo.getCompanyCodeSys01PUR04());
					pstmt1.setString(2,vo.getPricelistCodePur03PUR04());
					pstmt1.setBigDecimal(3,vo.getProgressiveReg04PUR04());
					pstmt1.setString(4,vo.getItemCodeItm01());
					pstmt1.setDate(5,vo.getStartDatePUR04());
					pstmt1.setDate(6,vo.getStartDatePUR04());
					pstmt1.setDate(7,vo.getEndDatePUR04());
					pstmt1.setDate(8,vo.getEndDatePUR04());
					pstmt1.setDate(9,vo.getEndDatePUR04());
					pstmt1.setDate(10,vo.getEndDatePUR04());
					rset = pstmt1.executeQuery();
					boolean found = rset.next();
					rset.close();
					if (found)
						throw new Exception("change date interval");

					pstmt3.setDate(1,vo.getStartDatePUR04());
					pstmt3.setString(2,vo.getCompanyCodeSys01PUR04());
					pstmt3.setString(3,vo.getPricelistCodePur03PUR04());
					pstmt3.setBigDecimal(4,vo.getProgressiveReg04PUR04());
					pstmt3.setString(5,vo.getItemCodeItm01());
					pstmt3.setDate(6,vo.getStartDatePUR04());
					pstmt3.setDate(7,vo.getStartDatePUR04());
					pstmt3.setDate(8,vo.getStartDatePUR04());
					pstmt3.execute();

				}


        res = org.jallinone.commons.server.QueryUtilExtension.insertTable(
            conn,
            new UserSessionParameters(username),
            vo,
            "PUR04_SUPPLIER_ITEM_PRICES",
            attribute2dbField,
            "Y",
            "N",
            null,
            true
        );
        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }

      }

      return new VOListResponse(list,false,list.size());
    }
    catch (Throwable ex) {
    	Logger.error(username, this.getClass().getName(),
    			"executeCommand", "Error while inserting a new supplier item price", ex);
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
        if (rset != null) {
          rset.close();
        }
      }
      catch (Exception ex4) {
      }
      try {
        pstmt.close();
      }
      catch (Exception ex2) {
      }
			try {
				if (pstmt0!=null)
					pstmt0.close();
			}
			catch (Exception ex1) {
			}
			try {
				if (pstmt1!=null)
					pstmt1.close();
			}
			catch (Exception ex1) {
			}
			try {
				if (pstmt2!=null)
					pstmt2.close();
			}
			catch (Exception ex1) {
			}
			try {
				if (pstmt3!=null)
					pstmt3.close();
			}
			catch (Exception ex1) {
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
        insItem.setConn(null);
        loadItem.setConn(null);
      } catch (Exception ex) {}
    }

  }





  /**
   * Business logic to execute.
   */
  public VOResponse deleteSupplierPrices(ArrayList list,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

			pstmt = conn.prepareStatement(
 			  "delete from PUR04_SUPPLIER_ITEM_PRICES "+
				 "where COMPANY_CODE_SYS01=? and PRICELIST_CODE_PUR03=? and ITEM_CODE_ITM01=? and PROGRESSIVE_REG04=? and START_DATE=?"
			);

      SupplierPriceVO vo = null;
      for(int i=0;i<list.size();i++) {
        vo = (SupplierPriceVO)list.get(i);
        // phisically delete records from PUR04...
				pstmt.setString(1,vo.getCompanyCodeSys01PUR04());
				pstmt.setString(2,vo.getPricelistCodePur03PUR04());
				pstmt.setString(3,vo.getItemCodeItm01PUR04());
				pstmt.setBigDecimal(4,vo.getProgressiveReg04PUR04());
				pstmt.setDate(5,vo.getStartDatePUR04());
      }

      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
    	Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing supplier item prices",ex);
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



}

