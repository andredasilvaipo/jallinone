package org.jallinone.sales.pricelist.server;

import org.openswing.swing.server.*;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;

import java.sql.*;

import org.openswing.swing.customvo.java.CustomValueObject;
import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;
import org.jallinone.sales.pricelist.java.*;
import org.jallinone.variants.java.VariantNameVO;
import org.jallinone.variants.java.VariantsMatrixColumnVO;
import org.jallinone.variants.java.VariantsMatrixRowVO;
import org.jallinone.variants.java.VariantsMatrixUtils;
import org.jallinone.variants.java.VariantsMatrixVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.jallinone.items.java.DetailItemVO;
import org.jallinone.items.java.ItemPK;
import org.jallinone.items.java.VariantBarcodeVO;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage sale prices.</p>
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
public class SalePricesBean  implements SalePrices {


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




  public SalePricesBean() {
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
   */
  public PricelistVO getPricelist() {
	  throw new UnsupportedOperationException();
  }

  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
   */
  public CustomValueObject getCustomValueObject() {
	  throw new UnsupportedOperationException();
  }

  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
   */
  public PriceVO getPrice() {
	  throw new UnsupportedOperationException();
  }

  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
   */
  public DetailItemVO getDetailItem() {
	  throw new UnsupportedOperationException();
  }


  /**
   * Business logic to execute.
   */
  public VOListResponse insertPrices(ArrayList list,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
		PreparedStatement pstmt0 = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SAL02","COMPANY_CODE_SYS01");
      attribute2dbField.put("pricelistCodeSal01SAL02","PRICELIST_CODE_SAL01");
      attribute2dbField.put("itemCodeItm01SAL02","ITEM_CODE_ITM01");
      attribute2dbField.put("valueSAL02","VALUE");
      attribute2dbField.put("startDateSAL02","START_DATE");
      attribute2dbField.put("endDateSAL02","END_DATE");

			pstmt0 = conn.prepareStatement(
			  "select * from SAL02_ITEM_PRICES WHERE "+
				"COMPANY_CODE_SYS01=? AND PRICELIST_CODE_SAL01=? AND ITEM_CODE_ITM01=? AND NOT START_DATE=? AND  "+
				"(START_DATE>? AND END_DATE<? or START_DATE<? AND END_DATE>?) "
			);

 		  pstmt1 = conn.prepareStatement(
			 "select * from SAL02_ITEM_PRICES WHERE "+
			 "COMPANY_CODE_SYS01=? AND PRICELIST_CODE_SAL01=? AND ITEM_CODE_ITM01=? AND NOT START_DATE=? AND  "+
			 "(START_DATE>? AND END_DATE<? or START_DATE<? AND END_DATE>? or START_DATE>? ) "
		  );

			pstmt2 = conn.prepareStatement(
				 "UPDATE SAL02_ITEM_PRICES SET END_DATE=? WHERE "+
				 "COMPANY_CODE_SYS01=? AND PRICELIST_CODE_SAL01=? AND ITEM_CODE_ITM01=? AND NOT START_DATE=? AND "+
				 "(START_DATE<? AND END_DATE is null or START_DATE<? AND END_DATE>? ) "
			);

			pstmt3 = conn.prepareStatement(
				"UPDATE SAL02_ITEM_PRICES SET END_DATE=? WHERE "+
				"COMPANY_CODE_SYS01=? AND PRICELIST_CODE_SAL01=? AND ITEM_CODE_ITM01=? AND NOT START_DATE=? AND "+
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

      // insert into SAL02...
      PriceVO vo = null;
      Response res = null;
      for(int i=0;i<list.size();i++) {
        vo = (PriceVO)list.get(i);

				if (vo.getEndDateSAL02()!=null) {

					pstmt0.setString(1,vo.getCompanyCodeSys01SAL02());
					pstmt0.setString(2,vo.getPricelistCodeSal01SAL02());
					pstmt0.setString(3,vo.getItemCodeItm01());
					pstmt0.setDate(4,vo.getStartDateSAL02());
					pstmt0.setDate(5,vo.getStartDateSAL02());
					pstmt0.setDate(6,vo.getEndDateSAL02());
					pstmt0.setDate(7,vo.getEndDateSAL02());
					pstmt0.setDate(8,vo.getEndDateSAL02());
					ResultSet rset = pstmt0.executeQuery();
					boolean found = rset.next();
					rset.close();
					if (found)
						throw new Exception("change date interval");

					pstmt2.setDate(1,vo.getStartDateSAL02());
					pstmt2.setString(2,vo.getCompanyCodeSys01SAL02());
					pstmt2.setString(3,vo.getPricelistCodeSal01SAL02());
					pstmt2.setString(4,vo.getItemCodeItm01());
					pstmt2.setDate(5,vo.getStartDateSAL02());
					pstmt2.setDate(6,vo.getEndDateSAL02());
					pstmt2.setDate(7,vo.getStartDateSAL02());
					pstmt2.setDate(8,vo.getStartDateSAL02());
					pstmt2.execute();

				}
				else {

					pstmt1.setString(1,vo.getCompanyCodeSys01SAL02());
					pstmt1.setString(2,vo.getPricelistCodeSal01SAL02());
					pstmt1.setString(3,vo.getItemCodeItm01());
					pstmt1.setDate(4,vo.getStartDateSAL02());
					pstmt1.setDate(5,vo.getStartDateSAL02());
					pstmt1.setDate(6,vo.getEndDateSAL02());
					pstmt1.setDate(7,vo.getEndDateSAL02());
					pstmt1.setDate(8,vo.getEndDateSAL02());
					pstmt1.setDate(9,vo.getEndDateSAL02());
					ResultSet rset = pstmt1.executeQuery();
					boolean found = rset.next();
					rset.close();
					if (found)
						throw new Exception("change date interval");

					pstmt3.setDate(1,vo.getStartDateSAL02());
					pstmt3.setString(2,vo.getCompanyCodeSys01SAL02());
					pstmt3.setString(3,vo.getPricelistCodeSal01SAL02());
					pstmt3.setString(4,vo.getItemCodeItm01());
					pstmt3.setDate(5,vo.getStartDateSAL02());
					pstmt3.setDate(6,vo.getStartDateSAL02());
					pstmt3.setDate(7,vo.getStartDateSAL02());
					pstmt3.execute();

				}

        res = org.jallinone.commons.server.QueryUtilExtension.insertTable(
            conn,
            new UserSessionParameters(username),
            vo,
            "SAL02_ITEM_PRICES",
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
                   "executeCommand", "Error while inserting a new price", ex);
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
  public VOListResponse loadPrices(GridParams gridParams,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      String companyCodeSYS01 = null;
      String sql = null;


      if (gridParams.getOtherGridParams().get(ApplicationConsts.PRICELIST)!=null) {
        PricelistVO vo = (PricelistVO)gridParams.getOtherGridParams().get(ApplicationConsts.PRICELIST);
        companyCodeSYS01 = vo.getCompanyCodeSys01SAL01();

        sql =
            "select SAL02_ITEM_PRICES.COMPANY_CODE_SYS01,SAL02_ITEM_PRICES.PRICELIST_CODE_SAL01,SAL02_ITEM_PRICES.ITEM_CODE_ITM01,SAL02_ITEM_PRICES.VALUE,SAL02_ITEM_PRICES.START_DATE,SAL02_ITEM_PRICES.END_DATE,A.DESCRIPTION,ITM01_ITEMS.PROGRESSIVE_HIE02,"+
            "ITM01_ITEMS.USE_VARIANT_1,ITM01_ITEMS.USE_VARIANT_2,ITM01_ITEMS.USE_VARIANT_3,ITM01_ITEMS.USE_VARIANT_4,ITM01_ITEMS.USE_VARIANT_5, "+
						"REG03_CURRENCIES.CURRENCY_SYMBOL,REG03_CURRENCIES.DECIMALS "+
            " from SAL02_ITEM_PRICES,SYS10_COMPANY_TRANSLATIONS A,ITM01_ITEMS,SAL01_PRICELISTS,REG03_CURRENCIES where "+
						"SAL01_PRICELISTS.COMPANY_CODE_SYS01=SAL02_ITEM_PRICES.COMPANY_CODE_SYS01 and "+
						"SAL01_PRICELISTS.PRICELIST_CODE=SAL02_ITEM_PRICES.PRICELIST_CODE_SAL01 and "+
            "SAL02_ITEM_PRICES.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
            "SAL02_ITEM_PRICES.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and "+
						"ITM01_ITEMS.COMPANY_CODE_SYS01=A.COMPANY_CODE_SYS01 and "+
            "ITM01_ITEMS.PROGRESSIVE_SYS10=A.PROGRESSIVE and "+
            "A.LANGUAGE_CODE=? and SAL02_ITEM_PRICES.COMPANY_CODE_SYS01=? and ITM01_ITEMS.ENABLED='Y' and "+
            "SAL02_ITEM_PRICES.PRICELIST_CODE_SAL01='"+vo.getPricelistCodeSAL01()+"' AND "+
						"REG03_CURRENCIES.CURRENCY_CODE=SAL01_PRICELISTS.CURRENCY_CODE_REG03";
      }
      else {
        DetailItemVO vo = (DetailItemVO)gridParams.getOtherGridParams().get(ApplicationConsts.ITEM);
        companyCodeSYS01 = vo.getCompanyCodeSys01ITM01();

        sql =
            "select SAL02_ITEM_PRICES.COMPANY_CODE_SYS01,SAL02_ITEM_PRICES.PRICELIST_CODE_SAL01,SAL02_ITEM_PRICES.ITEM_CODE_ITM01,SAL02_ITEM_PRICES.VALUE,SAL02_ITEM_PRICES.START_DATE,SAL02_ITEM_PRICES.END_DATE,B.DESCRIPTION,"+
            "ITM01_ITEMS.USE_VARIANT_1,ITM01_ITEMS.USE_VARIANT_2,ITM01_ITEMS.USE_VARIANT_3,ITM01_ITEMS.USE_VARIANT_4,ITM01_ITEMS.USE_VARIANT_5, "+
						"REG03_CURRENCIES.CURRENCY_SYMBOL,REG03_CURRENCIES.DECIMALS "+
            " from SAL02_ITEM_PRICES,SYS10_COMPANY_TRANSLATIONS B,SAL01_PRICELISTS,ITM01_ITEMS,REG03_CURRENCIES where "+
            "SAL02_ITEM_PRICES.COMPANY_CODE_SYS01=SAL01_PRICELISTS.COMPANY_CODE_SYS01 and "+
            "SAL02_ITEM_PRICES.PRICELIST_CODE_SAL01=SAL01_PRICELISTS.PRICELIST_CODE and "+
						"SAL01_PRICELISTS.COMPANY_CODE_SYS01=B.COMPANY_CODE_SYS01 and "+
            "SAL01_PRICELISTS.PROGRESSIVE_SYS10=B.PROGRESSIVE and "+
            "B.LANGUAGE_CODE=? and SAL02_ITEM_PRICES.COMPANY_CODE_SYS01=? and "+
            "SAL02_ITEM_PRICES.ITEM_CODE_ITM01='"+vo.getItemCodeITM01()+"' and "+
            "SAL02_ITEM_PRICES.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
            "SAL02_ITEM_PRICES.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE AND "+
						"REG03_CURRENCIES.CURRENCY_CODE=SAL01_PRICELISTS.CURRENCY_CODE_REG03";
      }

      java.sql.Date filterDate = null;
      if (gridParams.getOtherGridParams().get(ApplicationConsts.DATE_FILTER)!=null) {
        filterDate = new java.sql.Date( ((java.util.Date)gridParams.getOtherGridParams().get(ApplicationConsts.DATE_FILTER)).getTime() );
        sql +=
					" and SAL02_ITEM_PRICES.START_DATE<=? and "+
					"    (SAL02_ITEM_PRICES.END_DATE>? or SAL02_ITEM_PRICES.END_DATE is null) ";
      }

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SAL02","SAL02_ITEM_PRICES.COMPANY_CODE_SYS01");
      attribute2dbField.put("pricelistCodeSal01SAL02","SAL02_ITEM_PRICES.PRICELIST_CODE_SAL01");
      attribute2dbField.put("itemCodeItm01SAL02","SAL02_ITEM_PRICES.ITEM_CODE_ITM01");
      attribute2dbField.put("valueSAL02","SAL02_ITEM_PRICES.VALUE");
      attribute2dbField.put("startDateSAL02","SAL02_ITEM_PRICES.START_DATE");
      attribute2dbField.put("endDateSAL02","SAL02_ITEM_PRICES.END_DATE");
      attribute2dbField.put("itemDescriptionSYS10","A.DESCRIPTION");
      attribute2dbField.put("pricelistDescriptionSYS10","B.DESCRIPTION");
      attribute2dbField.put("progressiveHie02ITM01","ITM01_ITEMS.PROGRESSIVE_HIE02");

      attribute2dbField.put("useVariant1ITM01","ITM01_ITEMS.USE_VARIANT_1");
      attribute2dbField.put("useVariant2ITM01","ITM01_ITEMS.USE_VARIANT_2");
      attribute2dbField.put("useVariant3ITM01","ITM01_ITEMS.USE_VARIANT_3");
      attribute2dbField.put("useVariant4ITM01","ITM01_ITEMS.USE_VARIANT_4");
      attribute2dbField.put("useVariant5ITM01","ITM01_ITEMS.USE_VARIANT_5");

      attribute2dbField.put("currencySymbolREG03","REG03_CURRENCIES.CURRENCY_SYMBOL");
      attribute2dbField.put("decimalsREG03","REG03_CURRENCIES.DECIMALS");


      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(companyCodeSYS01);
      if (filterDate!=null) {
        values.add(filterDate);
        values.add(filterDate);
      }

      // read from SAL02 table...
      Response res = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          PriceVO.class,
          "Y",
          "N",
          null,
          gridParams,
          50,
          true
      );

      if (!res.isError()) {
        java.util.List rows = ((VOListResponse)res).getRows();
        PriceVO vo = null;
        for(int i=0;i<rows.size();i++) {
          vo = (PriceVO)rows.get(i);
          if (gridParams.getOtherGridParams().get(ApplicationConsts.PRICELIST)!=null) {
            PricelistVO parentVO = (PricelistVO)gridParams.getOtherGridParams().get(ApplicationConsts.PRICELIST);
            vo.setPricelistDescriptionSYS10(parentVO.getDescriptionSYS10());
          }
          else {
            DetailItemVO parentVO = (DetailItemVO)gridParams.getOtherGridParams().get(ApplicationConsts.ITEM);
            vo.setItemDescriptionSYS10(parentVO.getDescriptionSYS10());
            vo.setProgressiveHie02ITM01(parentVO.getProgressiveHie02ITM01());
          }
        }

      }

      Response answer = res;
      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching prices list",ex);
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
  public VOListResponse loadVariantsPrice(VariantBarcodeVO barcodeVO,String priceListCode,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      String sql =
            "select SAL11_VARIANTS_PRICES.COMPANY_CODE_SYS01,SAL11_VARIANTS_PRICES.PRICELIST_CODE_SAL01,"+
            "SAL11_VARIANTS_PRICES.ITEM_CODE_ITM01,SAL11_VARIANTS_PRICES.VALUE,SAL11_VARIANTS_PRICES.START_DATE,SAL11_VARIANTS_PRICES.END_DATE,"+
            "A.DESCRIPTION,ITM01_ITEMS.PROGRESSIVE_HIE02,"+
            "SAL11_VARIANTS_PRICES.VARIANT_TYPE_ITM06,SAL11_VARIANTS_PRICES.VARIANT_CODE_ITM11,"+
            "SAL11_VARIANTS_PRICES.VARIANT_TYPE_ITM07,SAL11_VARIANTS_PRICES.VARIANT_CODE_ITM12,"+
            "SAL11_VARIANTS_PRICES.VARIANT_TYPE_ITM08,SAL11_VARIANTS_PRICES.VARIANT_CODE_ITM13,"+
            "SAL11_VARIANTS_PRICES.VARIANT_TYPE_ITM09,SAL11_VARIANTS_PRICES.VARIANT_CODE_ITM14,"+
            "SAL11_VARIANTS_PRICES.VARIANT_TYPE_ITM10,SAL11_VARIANTS_PRICES.VARIANT_CODE_ITM15 "+
            " from SAL11_VARIANTS_PRICES,SYS10_COMPANY_TRANSLATIONS A,ITM01_ITEMS where "+
            "SAL11_VARIANTS_PRICES.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
            "SAL11_VARIANTS_PRICES.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and "+
						"ITM01_ITEMS.COMPANY_CODE_SYS01=A.COMPANY_CODE_SYS01 and "+
            "ITM01_ITEMS.PROGRESSIVE_SYS10=A.PROGRESSIVE and "+
            "A.LANGUAGE_CODE=? and SAL11_VARIANTS_PRICES.COMPANY_CODE_SYS01=? and ITM01_ITEMS.ENABLED='Y' and "+
            "SAL11_VARIANTS_PRICES.PRICELIST_CODE_SAL01=? and "+
            "SAL11_VARIANTS_PRICES.ITEM_CODE_ITM01=? and "+
            "SAL11_VARIANTS_PRICES.VARIANT_TYPE_ITM06=? and SAL11_VARIANTS_PRICES.VARIANT_CODE_ITM11=? and "+
            "SAL11_VARIANTS_PRICES.VARIANT_TYPE_ITM07=? and SAL11_VARIANTS_PRICES.VARIANT_CODE_ITM12=? and "+
            "SAL11_VARIANTS_PRICES.VARIANT_TYPE_ITM08=? and SAL11_VARIANTS_PRICES.VARIANT_CODE_ITM13=? and "+
            "SAL11_VARIANTS_PRICES.VARIANT_TYPE_ITM09=? and SAL11_VARIANTS_PRICES.VARIANT_CODE_ITM14=? and "+
            "SAL11_VARIANTS_PRICES.VARIANT_TYPE_ITM10=? and SAL11_VARIANTS_PRICES.VARIANT_CODE_ITM15=? ";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SAL11","SAL11_VARIANTS_PRICES.COMPANY_CODE_SYS01");
      attribute2dbField.put("pricelistCodeSal01SAL11","SAL11_VARIANTS_PRICES.PRICELIST_CODE_SAL01");
      attribute2dbField.put("itemCodeItm01SAL11","SAL11_VARIANTS_PRICES.ITEM_CODE_ITM01");
      attribute2dbField.put("valueSAL11","SAL11_VARIANTS_PRICES.VALUE");
      attribute2dbField.put("startDateSAL11","SAL11_VARIANTS_PRICES.START_DATE");
      attribute2dbField.put("endDateSAL11","SAL11_VARIANTS_PRICES.END_DATE");
      attribute2dbField.put("itemDescriptionSYS10","A.DESCRIPTION");
      attribute2dbField.put("pricelistDescriptionSYS10","B.DESCRIPTION");
      attribute2dbField.put("progressiveHie02ITM01","ITM01_ITEMS.PROGRESSIVE_HIE02");

      attribute2dbField.put("variantTypeItm06SAL11","SAL11_VARIANTS_PRICES.VARIANT_TYPE_ITM06");
      attribute2dbField.put("variantCodeItm11SAL11","SAL11_VARIANTS_PRICES.VARIANT_CODE_ITM11");
      attribute2dbField.put("variantTypeItm07SAL11","SAL11_VARIANTS_PRICES.VARIANT_TYPE_ITM07");
      attribute2dbField.put("variantCodeItm12SAL11","SAL11_VARIANTS_PRICES.VARIANT_CODE_ITM12");
      attribute2dbField.put("variantTypeItm08SAL11","SAL11_VARIANTS_PRICES.VARIANT_TYPE_ITM08");
      attribute2dbField.put("variantCodeItm13SAL11","SAL11_VARIANTS_PRICES.VARIANT_CODE_ITM13");
      attribute2dbField.put("variantTypeItm09SAL11","SAL11_VARIANTS_PRICES.VARIANT_TYPE_ITM09");
      attribute2dbField.put("variantCodeItm14SAL11","SAL11_VARIANTS_PRICES.VARIANT_CODE_ITM14");
      attribute2dbField.put("variantTypeItm10SAL11","SAL11_VARIANTS_PRICES.VARIANT_TYPE_ITM10");
      attribute2dbField.put("variantCodeItm15SAL11","SAL11_VARIANTS_PRICES.VARIANT_CODE_ITM15");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(barcodeVO.getCompanyCodeSys01ITM22());
      values.add(priceListCode);
      values.add(barcodeVO.getItemCodeItm01ITM22());

      values.add(barcodeVO.getVariantTypeItm06ITM22());
      values.add(barcodeVO.getVariantCodeItm11ITM22());
      values.add(barcodeVO.getVariantTypeItm07ITM22());
      values.add(barcodeVO.getVariantCodeItm12ITM22());
      values.add(barcodeVO.getVariantTypeItm08ITM22());
      values.add(barcodeVO.getVariantCodeItm13ITM22());
      values.add(barcodeVO.getVariantTypeItm09ITM22());
      values.add(barcodeVO.getVariantCodeItm14ITM22());
      values.add(barcodeVO.getVariantTypeItm10ITM22());
      values.add(barcodeVO.getVariantCodeItm15ITM22());


      // read ALL from SAL11 table...
      Response res = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          VariantsPriceVO.class,
          "Y",
          "N",
          null,
          new GridParams(),
          true
      );

		  if (res.isError())
				throw new Exception(res.getErrorMessage());
			return (VOListResponse)res;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching a price for the specified barcode",ex);
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
  public VOListResponse loadVariantsPrices(GridParams params,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      VariantsMatrixVO matrixVO = (VariantsMatrixVO)params.getOtherGridParams().get(ApplicationConsts.VARIANTS_MATRIX_VO);
      ItemPK itemPK = matrixVO.getItemPK();
      String priceListCode = (String)params.getOtherGridParams().get(ApplicationConsts.PRICELIST);

      String sql =
            "select SAL11_VARIANTS_PRICES.COMPANY_CODE_SYS01,SAL11_VARIANTS_PRICES.PRICELIST_CODE_SAL01,"+
            "SAL11_VARIANTS_PRICES.ITEM_CODE_ITM01,SAL11_VARIANTS_PRICES.VALUE,SAL11_VARIANTS_PRICES.START_DATE,SAL11_VARIANTS_PRICES.END_DATE,"+
            "A.DESCRIPTION,ITM01_ITEMS.PROGRESSIVE_HIE02,"+
            "SAL11_VARIANTS_PRICES.VARIANT_TYPE_ITM06,SAL11_VARIANTS_PRICES.VARIANT_CODE_ITM11,"+
            "SAL11_VARIANTS_PRICES.VARIANT_TYPE_ITM07,SAL11_VARIANTS_PRICES.VARIANT_CODE_ITM12,"+
            "SAL11_VARIANTS_PRICES.VARIANT_TYPE_ITM08,SAL11_VARIANTS_PRICES.VARIANT_CODE_ITM13,"+
            "SAL11_VARIANTS_PRICES.VARIANT_TYPE_ITM09,SAL11_VARIANTS_PRICES.VARIANT_CODE_ITM14,"+
            "SAL11_VARIANTS_PRICES.VARIANT_TYPE_ITM10,SAL11_VARIANTS_PRICES.VARIANT_CODE_ITM15 "+
            " from SAL11_VARIANTS_PRICES,SYS10_COMPANY_TRANSLATIONS A,ITM01_ITEMS where "+
            "SAL11_VARIANTS_PRICES.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
            "SAL11_VARIANTS_PRICES.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and "+
						"ITM01_ITEMS.COMPANY_CODE_SYS01=A.COMPANY_CODE_SYS01 and "+
            "ITM01_ITEMS.PROGRESSIVE_SYS10=A.PROGRESSIVE and "+
            "A.LANGUAGE_CODE=? and SAL11_VARIANTS_PRICES.COMPANY_CODE_SYS01=? and ITM01_ITEMS.ENABLED='Y' and "+
            "SAL11_VARIANTS_PRICES.PRICELIST_CODE_SAL01=? and "+
            "SAL11_VARIANTS_PRICES.ITEM_CODE_ITM01=?";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SAL11","SAL11_VARIANTS_PRICES.COMPANY_CODE_SYS01");
      attribute2dbField.put("pricelistCodeSal01SAL11","SAL11_VARIANTS_PRICES.PRICELIST_CODE_SAL01");
      attribute2dbField.put("itemCodeItm01SAL11","SAL11_VARIANTS_PRICES.ITEM_CODE_ITM01");
      attribute2dbField.put("valueSAL11","SAL11_VARIANTS_PRICES.VALUE");
      attribute2dbField.put("startDateSAL11","SAL11_VARIANTS_PRICES.START_DATE");
      attribute2dbField.put("endDateSAL11","SAL11_VARIANTS_PRICES.END_DATE");
      attribute2dbField.put("itemDescriptionSYS10","A.DESCRIPTION");
      attribute2dbField.put("pricelistDescriptionSYS10","B.DESCRIPTION");
      attribute2dbField.put("progressiveHie02ITM01","ITM01_ITEMS.PROGRESSIVE_HIE02");

      attribute2dbField.put("variantTypeItm06SAL11","SAL11_VARIANTS_PRICES.VARIANT_TYPE_ITM06");
      attribute2dbField.put("variantCodeItm11SAL11","SAL11_VARIANTS_PRICES.VARIANT_CODE_ITM11");
      attribute2dbField.put("variantTypeItm07SAL11","SAL11_VARIANTS_PRICES.VARIANT_TYPE_ITM07");
      attribute2dbField.put("variantCodeItm12SAL11","SAL11_VARIANTS_PRICES.VARIANT_CODE_ITM12");
      attribute2dbField.put("variantTypeItm08SAL11","SAL11_VARIANTS_PRICES.VARIANT_TYPE_ITM08");
      attribute2dbField.put("variantCodeItm13SAL11","SAL11_VARIANTS_PRICES.VARIANT_CODE_ITM13");
      attribute2dbField.put("variantTypeItm09SAL11","SAL11_VARIANTS_PRICES.VARIANT_TYPE_ITM09");
      attribute2dbField.put("variantCodeItm14SAL11","SAL11_VARIANTS_PRICES.VARIANT_CODE_ITM14");
      attribute2dbField.put("variantTypeItm10SAL11","SAL11_VARIANTS_PRICES.VARIANT_TYPE_ITM10");
      attribute2dbField.put("variantCodeItm15SAL11","SAL11_VARIANTS_PRICES.VARIANT_CODE_ITM15");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(itemPK.getCompanyCodeSys01ITM01());
      values.add(priceListCode);
      values.add(itemPK.getItemCodeITM01());


      // read ALL from SAL11 table...
      Response res = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          VariantsPriceVO.class,
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
      VariantsPriceVO vo = null;
      CustomValueObject customVO = null;
      VariantsMatrixRowVO rowVO = null;
      VariantsMatrixColumnVO colVO = null;
      HashMap indexes = new HashMap();
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
        vo = (VariantsPriceVO)rows.get(i);

        if (varVO.getTableName().equals("ITM11_VARIANTS_1")) {
          customVO = (CustomValueObject)indexes.get(vo.getVariantTypeItm06SAL11()+" "+vo.getVariantCodeItm11SAL11());
        }
        else if (varVO.getTableName().equals("ITM12_VARIANTS_2")) {
          customVO = (CustomValueObject)indexes.get(vo.getVariantTypeItm07SAL11()+" "+vo.getVariantCodeItm12SAL11());
        }
        else if (varVO.getTableName().equals("ITM13_VARIANTS_3")) {
          customVO = (CustomValueObject)indexes.get(vo.getVariantTypeItm08SAL11()+" "+vo.getVariantCodeItm13SAL11());
        }
        else if (varVO.getTableName().equals("ITM14_VARIANTS_4")) {
          customVO = (CustomValueObject)indexes.get(vo.getVariantTypeItm09SAL11()+" "+vo.getVariantCodeItm14SAL11());
        }
        else if (varVO.getTableName().equals("ITM15_VARIANTS_5")) {
          customVO = (CustomValueObject)indexes.get(vo.getVariantTypeItm10SAL11()+" "+vo.getVariantCodeItm15SAL11());
        }

        if (matrixVO.getColumnDescriptors().length==0) {
          customVO.setAttributeNameN0(vo.getValueSAL11());
        }
        else {

          for(int j=0;j<matrixVO.getColumnDescriptors().length;j++) {
            colVO = (VariantsMatrixColumnVO)matrixVO.getColumnDescriptors()[j];
            if ((varVO.getTableName().equals("ITM11_VARIANTS_1")?true:colVO.getVariantCodeITM11().equals(vo.getVariantCodeItm11SAL11())) &&
                (varVO.getTableName().equals("ITM12_VARIANTS_2")?true:colVO.getVariantCodeITM12().equals(vo.getVariantCodeItm12SAL11())) &&
                (varVO.getTableName().equals("ITM13_VARIANTS_3")?true:colVO.getVariantCodeITM13().equals(vo.getVariantCodeItm13SAL11())) &&
                (varVO.getTableName().equals("ITM14_VARIANTS_4")?true:colVO.getVariantCodeITM14().equals(vo.getVariantCodeItm14SAL11())) &&
                (varVO.getTableName().equals("ITM15_VARIANTS_5")?true:colVO.getVariantCodeITM15().equals(vo.getVariantCodeItm15SAL11())) &&
                (varVO.getTableName().equals("ITM11_VARIANTS_1")?true:colVO.getVariantTypeITM06().equals(vo.getVariantTypeItm06SAL11())) &&
                (varVO.getTableName().equals("ITM12_VARIANTS_2")?true:colVO.getVariantTypeITM07().equals(vo.getVariantTypeItm07SAL11())) &&
                (varVO.getTableName().equals("ITM13_VARIANTS_3")?true:colVO.getVariantTypeITM08().equals(vo.getVariantTypeItm08SAL11())) &&
                (varVO.getTableName().equals("ITM14_VARIANTS_4")?true:colVO.getVariantTypeITM09().equals(vo.getVariantTypeItm09SAL11())) &&
                (varVO.getTableName().equals("ITM15_VARIANTS_5")?true:colVO.getVariantTypeITM10().equals(vo.getVariantTypeItm10SAL11()))) {
              try {
                CustomValueObject.class.getMethod("setAttributeNameN" + j,new Class[] {BigDecimal.class}).invoke(customVO, new Object[] {vo.getValueSAL11()});
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
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching prices list",ex);
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
  public VOListResponse updatePrices(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username) throws Throwable {
		PreparedStatement pstmt0 = null;
		PreparedStatement pstmt1 = null;
		PreparedStatement pstmt2 = null;
		PreparedStatement pstmt3 = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      PriceVO oldVO = null;
      PriceVO vo = null;
      Response res = null;


			pstmt0 = conn.prepareStatement(
				"select * from SAL02_ITEM_PRICES WHERE "+
				"COMPANY_CODE_SYS01=? AND PRICELIST_CODE_SAL01=? AND ITEM_CODE_ITM01=? AND NOT START_DATE=? AND  "+
				"(START_DATE>? AND END_DATE<? or START_DATE<? AND END_DATE>?) "
			);

			 pstmt1 = conn.prepareStatement(
			 "select * from SAL02_ITEM_PRICES WHERE "+
			 "COMPANY_CODE_SYS01=? AND PRICELIST_CODE_SAL01=? AND ITEM_CODE_ITM01=? AND NOT START_DATE=? AND  "+
			 "(START_DATE>? AND END_DATE<? or START_DATE<? AND END_DATE>? or START_DATE>? ) "
			);

			pstmt2 = conn.prepareStatement(
				 "UPDATE SAL02_ITEM_PRICES SET END_DATE=? WHERE "+
				 "COMPANY_CODE_SYS01=? AND PRICELIST_CODE_SAL01=? AND ITEM_CODE_ITM01=? AND NOT START_DATE=? AND "+
				 "(START_DATE<? AND END_DATE is null or START_DATE<? AND END_DATE>? ) "
			);

 		  pstmt3 = conn.prepareStatement(
				"UPDATE SAL02_ITEM_PRICES SET END_DATE=? WHERE "+
				"COMPANY_CODE_SYS01=? AND PRICELIST_CODE_SAL01=? AND ITEM_CODE_ITM01=? AND NOT START_DATE=? AND "+
				"(END_DATE is null or START_DATE<? AND END_DATE>? ) "
	 	  );


		 // if current price is valid for [d1,d2]
		 // then invervals to redefine are:
		 // [<d2,null] -> [...,d1]
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

		 HashSet pkAttrs = new HashSet();
		 pkAttrs.add("companyCodeSys01SAL02");
		 pkAttrs.add("pricelistCodeSal01SAL02");
		 pkAttrs.add("itemCodeItm01SAL02");
		 pkAttrs.add("startDateSAL02");

		 HashMap attribute2dbField = new HashMap();
		 attribute2dbField.put("companyCodeSys01SAL02","COMPANY_CODE_SYS01");
		 attribute2dbField.put("pricelistCodeSal01SAL02","PRICELIST_CODE_SAL01");
		 attribute2dbField.put("itemCodeItm01SAL02","ITEM_CODE_ITM01");
		 attribute2dbField.put("valueSAL02","VALUE");
		 attribute2dbField.put("startDateSAL02","START_DATE");
		 attribute2dbField.put("endDateSAL02","END_DATE");


     for(int i=0;i<oldVOs.size();i++) {
        oldVO = (PriceVO)oldVOs.get(i);
        vo = (PriceVO)newVOs.get(i);

				if (vo.getEndDateSAL02()!=null) {

					pstmt0.setString(1,vo.getCompanyCodeSys01SAL02());
					pstmt0.setString(2,vo.getPricelistCodeSal01SAL02());
					pstmt0.setString(3,vo.getItemCodeItm01());
					pstmt0.setDate(4,vo.getStartDateSAL02());
					pstmt0.setDate(5,vo.getStartDateSAL02());
					pstmt0.setDate(6,vo.getEndDateSAL02());
					pstmt0.setDate(7,vo.getEndDateSAL02());
					pstmt0.setDate(8,vo.getEndDateSAL02());
					ResultSet rset = pstmt0.executeQuery();
					boolean found = rset.next();
					rset.close();
					if (found)
						throw new Exception("change date interval");

					pstmt2.setDate(1,vo.getStartDateSAL02());
					pstmt2.setString(2,vo.getCompanyCodeSys01SAL02());
					pstmt2.setString(3,vo.getPricelistCodeSal01SAL02());
					pstmt2.setString(4,vo.getItemCodeItm01());
					pstmt2.setDate(5,vo.getStartDateSAL02());
					pstmt2.setDate(6,vo.getEndDateSAL02());
					pstmt2.setDate(7,vo.getStartDateSAL02());
					pstmt2.setDate(8,vo.getStartDateSAL02());
					pstmt2.execute();

				}
				else {

					pstmt1.setString(1,vo.getCompanyCodeSys01SAL02());
					pstmt1.setString(2,vo.getPricelistCodeSal01SAL02());
					pstmt1.setString(3,vo.getItemCodeItm01());
					pstmt1.setDate(4,vo.getStartDateSAL02());
					pstmt1.setDate(5,vo.getStartDateSAL02());
					pstmt1.setDate(6,vo.getEndDateSAL02());
					pstmt1.setDate(7,vo.getEndDateSAL02());
					pstmt1.setDate(8,vo.getEndDateSAL02());
					pstmt1.setDate(9,vo.getEndDateSAL02());
					ResultSet rset = pstmt1.executeQuery();
					boolean found = rset.next();
					rset.close();
					if (found)
						throw new Exception("change date interval");

					pstmt3.setDate(1,vo.getStartDateSAL02());
					pstmt3.setString(2,vo.getCompanyCodeSys01SAL02());
					pstmt3.setString(3,vo.getPricelistCodeSal01SAL02());
					pstmt3.setString(4,vo.getItemCodeItm01());
					pstmt3.setDate(5,vo.getStartDateSAL02());
					pstmt3.setDate(6,vo.getStartDateSAL02());
					pstmt3.setDate(7,vo.getStartDateSAL02());
					pstmt3.execute();

				}

        res = org.jallinone.commons.server.QueryUtilExtension.updateTable(
            conn,
            new UserSessionParameters(username),
            pkAttrs,
            oldVO,
            vo,
            "SAL02_ITEM_PRICES",
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
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing prices",ex);
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
  public VOResponse updateVariantsPrices(VariantsPrice variantsPrice,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      // remove all prices related to the specified item/pricelist...
      pstmt = conn.prepareStatement("delete from SAL11_VARIANTS_PRICES where COMPANY_CODE_SYS01=? and PRICELIST_CODE_SAL01=? and ITEM_CODE_ITM01=?");
      pstmt.setString(1,variantsPrice.getMatrixVO().getItemPK().getCompanyCodeSys01ITM01());
      pstmt.setString(2,variantsPrice.getPriceListCode());
      pstmt.setString(3,variantsPrice.getMatrixVO().getItemPK().getItemCodeITM01());
      pstmt.execute();


      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SAL11","COMPANY_CODE_SYS01");
      attribute2dbField.put("pricelistCodeSal01SAL11","PRICELIST_CODE_SAL01");
      attribute2dbField.put("itemCodeItm01SAL11","ITEM_CODE_ITM01");
      attribute2dbField.put("valueSAL11","VALUE");
      attribute2dbField.put("startDateSAL11","START_DATE");
      attribute2dbField.put("endDateSAL11","END_DATE");

      attribute2dbField.put("variantTypeItm06SAL11","VARIANT_TYPE_ITM06");
      attribute2dbField.put("variantCodeItm11SAL11","VARIANT_CODE_ITM11");
      attribute2dbField.put("variantTypeItm07SAL11","VARIANT_TYPE_ITM07");
      attribute2dbField.put("variantCodeItm12SAL11","VARIANT_CODE_ITM12");
      attribute2dbField.put("variantTypeItm08SAL11","VARIANT_TYPE_ITM08");
      attribute2dbField.put("variantCodeItm13SAL11","VARIANT_CODE_ITM13");
      attribute2dbField.put("variantTypeItm09SAL11","VARIANT_TYPE_ITM09");
      attribute2dbField.put("variantCodeItm14SAL11","VARIANT_CODE_ITM14");
      attribute2dbField.put("variantTypeItm10SAL11","VARIANT_TYPE_ITM10");
      attribute2dbField.put("variantCodeItm15SAL11","VARIANT_CODE_ITM15");


      // insert into SAL11...
      VariantsPriceVO vo = null;
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
            vo = new VariantsPriceVO();
            vo.setCompanyCodeSys01SAL11(variantsPrice.getMatrixVO().getItemPK().getCompanyCodeSys01ITM01());
            vo.setItemCodeItm01SAL11(variantsPrice.getMatrixVO().getItemPK().getItemCodeITM01());
            vo.setPricelistCodeSal01SAL11(variantsPrice.getPriceListCode());
            vo.setValueSAL11(price);
            vo.setStartDateSAL11(variantsPrice.getStartDate());
            vo.setEndDateSAL11(variantsPrice.getEndDate());
            VariantsMatrixUtils.setVariantTypesAndCodes(vo,"SAL11",variantsPrice.getMatrixVO(),rowVO,null);

            res = org.jallinone.commons.server.QueryUtilExtension.insertTable(
                conn,
                new UserSessionParameters(username),
                vo,
                "SAL11_VARIANTS_PRICES",
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
              vo = new VariantsPriceVO();
              vo.setCompanyCodeSys01SAL11(variantsPrice.getMatrixVO().getItemPK().getCompanyCodeSys01ITM01());
              vo.setItemCodeItm01SAL11(variantsPrice.getMatrixVO().getItemPK().getItemCodeITM01());
              vo.setPricelistCodeSal01SAL11(variantsPrice.getPriceListCode());
              vo.setValueSAL11(price);
              vo.setStartDateSAL11(variantsPrice.getStartDate());
              vo.setEndDateSAL11(variantsPrice.getEndDate());
              VariantsMatrixUtils.setVariantTypesAndCodes(vo,"SAL11",variantsPrice.getMatrixVO(),rowVO,colVO);

              res = org.jallinone.commons.server.QueryUtilExtension.insertTable(
                  conn,
                  new UserSessionParameters(username),
                  vo,
                  "SAL11_VARIANTS_PRICES",
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




  /**
   * Business logic to execute.
   */
  public VOResponse deletePrices(ArrayList list,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

			pstmt = conn.prepareStatement(
				 "delete from SAL02_ITEM_PRICES where COMPANY_CODE_SYS01=? and "+
				 "PRICELIST_CODE_SAL01=? and ITEM_CODE_ITM01=? and START_DATE=?");

      PriceVO vo = null;
      for(int i=0;i<list.size();i++) {
        vo = (PriceVO)list.get(i);
        // phisically delete records from SAL02...
				pstmt.setString(1,vo.getCompanyCodeSys01SAL02());
				pstmt.setString(2,vo.getPricelistCodeSal01SAL02());
				pstmt.setString(3,vo.getItemCodeItm01SAL02());
				pstmt.setDate(4,vo.getStartDateSAL02());
				pstmt.execute();
      }

      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting an existing prices",ex);
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

