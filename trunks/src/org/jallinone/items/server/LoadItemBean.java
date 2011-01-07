package org.jallinone.items.server;

import javax.sql.DataSource;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.items.java.*;
import org.jallinone.system.server.*;
import org.jallinone.commons.server.*;
import java.math.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.system.java.*;
import org.jallinone.commons.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Bean used to fetch a specific item from ITM01 table.</p>
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
public class LoadItemBean implements LoadItem {


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




  public LoadItemBean() {
  }



  /**
   * Business logic to execute.
   */
  public BigDecimal getProgressiveHie02ITM01(ItemPK pk,String username) throws Throwable {
	  Statement stmt = null;
	  PreparedStatement pstmt = null;

	  Connection conn = null;
	  try {
		  if (this.conn==null) conn = getConn(); else conn = this.conn;

		  pstmt = conn.prepareStatement(
				  "SELECT HIE02_HIERARCHIES.PROGRESSIVE_HIE01 FROM "+
				  "HIE02_HIERARCHIES,ITM01_ITEMS WHERE " +
				  "ITM01_ITEMS.COMPANY_CODE_SYS01=? AND ITM01_ITEMS.ITEM_CODE=? AND " +
				  "ITM01_ITEMS.PROGRESSIVE_HIE02=HIE02_HIERARCHIES.PROGRESSIVE "
		  );
		  pstmt.setString(1,pk.getCompanyCodeSys01ITM01());
		  pstmt.setString(2,pk.getItemCodeITM01());
		  ResultSet rset = pstmt.executeQuery();
		  BigDecimal progressiveHie01HIE02 = null;
		  if (rset.next())
			  progressiveHie01HIE02 = rset.getBigDecimal(1);
		  rset.close();
		  pstmt.close();

		  return progressiveHie01HIE02;
	  }
	  catch (Throwable ex) {
		  Logger.error(username,this.getClass().getName(),"loadItem","Error while fetching an existing item",ex);
		  throw new Exception(ex.getMessage());
	  }
	  finally {
		  try {
			  stmt.close();
		  }
		  catch (Exception ex2) {
		  }
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
  public DetailItemVO loadItem(
    ItemPK pk,
    BigDecimal progressiveHie01HIE02,
    String serverLanguageId,String username,
    String imagePath,
    ArrayList customizedFields
  ) throws Throwable {
	ResultSet rset = null;
    Statement stmt = null;
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01ITM01","ITM01_ITEMS.COMPANY_CODE_SYS01");
      attribute2dbField.put("itemCodeITM01","ITM01_ITEMS.ITEM_CODE");
      attribute2dbField.put("descriptionSYS10","A.DESCRIPTION");
      attribute2dbField.put("progressiveHie02ITM01","ITM01_ITEMS.PROGRESSIVE_HIE02");
      attribute2dbField.put("progressiveHie01ITM01","ITM01_ITEMS.PROGRESSIVE_HIE01");
      attribute2dbField.put("addProgressiveSys10ITM01","ITM01_ITEMS.ADD_PROGRESSIVE_SYS10");
      attribute2dbField.put("progressiveSys10ITM01","ITM01_ITEMS.PROGRESSIVE_SYS10");
//      attribute2dbField.put("addDescriptionSYS10","B.DESCRIPTION");
      attribute2dbField.put("minSellingQtyITM01","ITM01_ITEMS.MIN_SELLING_QTY");
      attribute2dbField.put("minSellingQtyUmCodeReg02ITM01","ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02");
      attribute2dbField.put("minSellingQtyDecimalsREG02","REG02_A.DECIMALS");
      attribute2dbField.put("vatCodeReg01ITM01","ITM01_ITEMS.VAT_CODE_REG01");
      attribute2dbField.put("vatDescriptionSYS10","B.DESCRIPTION");
      attribute2dbField.put("grossWeightITM01","ITM01_ITEMS.GROSS_WEIGHT");
      attribute2dbField.put("grossWeightUmCodeReg02ITM01","ITM01_ITEMS.GROSS_WEIGHT_UM_CODE_REG02");
      attribute2dbField.put("netWeightITM01","ITM01_ITEMS.NET_WEIGHT");
//      attribute2dbField.put("netWeightDecimalsREG02","REG02_B.DECIMALS");
//      attribute2dbField.put("grossWeightDecimalsREG02","REG02_C.DECIMALS");
      attribute2dbField.put("netWeightUmCodeReg02ITM01","ITM01_ITEMS.NET_WEIGHT_UM_CODE_REG02");
      attribute2dbField.put("widthITM01","ITM01_ITEMS.WIDTH");
//      attribute2dbField.put("widthDecimalsREG02","REG02_D.DECIMALS");
      attribute2dbField.put("widthUmCodeReg02ITM01","ITM01_ITEMS.WIDTH_UM_CODE_REG02");
      attribute2dbField.put("heightITM01","ITM01_ITEMS.HEIGHT");
//      attribute2dbField.put("heightDecimalsREG02","REG02_E.DECIMALS");
      attribute2dbField.put("heightUmCodeReg02ITM01","ITM01_ITEMS.HEIGHT_UM_CODE_REG02");
      attribute2dbField.put("noteITM01","ITM01_ITEMS.NOTE");
      attribute2dbField.put("levelDescriptionSYS10","C.DESCRIPTION");
      attribute2dbField.put("largeImageITM01","ITM01_ITEMS.LARGE_IMAGE");
      attribute2dbField.put("smallImageITM01","ITM01_ITEMS.SMALL_IMAGE");
//      private byte[] smallImage;
//      private byte[] largeImage;
      attribute2dbField.put("vatValueREG01","REG01_VATS.VALUE");
      attribute2dbField.put("vatDeductibleREG01","REG01_VATS.DEDUCTIBLE");
//      attribute2dbField.put("colorDescriptionSYS10","D.DESCRIPTION");
//      attribute2dbField.put("sizeDescriptionSYS10","E.DESCRIPTION");
      attribute2dbField.put("enabledITM01","ITM01_ITEMS.ENABLED");
      attribute2dbField.put("serialNumberRequiredITM01","ITM01_ITEMS.SERIAL_NUMBER_REQUIRED");
      attribute2dbField.put("versionITM01","ITM01_ITEMS.VERSION");
      attribute2dbField.put("revisionITM01","ITM01_ITEMS.REVISION");
      attribute2dbField.put("manufactureCodePro01ITM01","ITM01_ITEMS.MANUFACTURE_CODE_PRO01");
      attribute2dbField.put("startDateITM01","ITM01_ITEMS.START_DATE");
      attribute2dbField.put("manufactureDescriptionSYS10","PRO01.DESCRIPTION");

      attribute2dbField.put("useVariant1ITM01","ITM01_ITEMS.USE_VARIANT_1");
      attribute2dbField.put("useVariant2ITM01","ITM01_ITEMS.USE_VARIANT_2");
      attribute2dbField.put("useVariant3ITM01","ITM01_ITEMS.USE_VARIANT_3");
      attribute2dbField.put("useVariant4ITM01","ITM01_ITEMS.USE_VARIANT_4");
      attribute2dbField.put("useVariant5ITM01","ITM01_ITEMS.USE_VARIANT_5");

      attribute2dbField.put("barCodeITM01","ITM01_ITEMS.BAR_CODE");
      attribute2dbField.put("barcodeTypeITM01","ITM01_ITEMS.BARCODE_TYPE");

			attribute2dbField.put("noWarehouseMovITM01","ITM01_ITEMS.NO_WAREHOUSE_MOV");
			attribute2dbField.put("sheetCodeItm25ITM01","ITM01_ITEMS.SHEET_CODE_ITM25");


      HashSet pkAttributes = new HashSet();
      pkAttributes.add("companyCodeSys01ITM01");
      pkAttributes.add("itemCodeITM01");

      String baseSQL =
          "select "+
          "ITM01_ITEMS.COMPANY_CODE_SYS01,ITM01_ITEMS.ITEM_CODE,ITM01_ITEMS.BAR_CODE,ITM01_ITEMS.BARCODE_TYPE,"+
          "A.DESCRIPTION,ITM01_ITEMS.PROGRESSIVE_HIE02,ITM01_ITEMS.PROGRESSIVE_HIE01,"+
          "ITM01_ITEMS.ADD_PROGRESSIVE_SYS10,ITM01_ITEMS.PROGRESSIVE_SYS10,ITM01_ITEMS.MIN_SELLING_QTY,ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02,"+
          "REG02_A.DECIMALS,ITM01_ITEMS.VAT_CODE_REG01,B.DESCRIPTION,ITM01_ITEMS.GROSS_WEIGHT,ITM01_ITEMS.GROSS_WEIGHT_UM_CODE_REG02,"+
          "ITM01_ITEMS.NET_WEIGHT,ITM01_ITEMS.NET_WEIGHT_UM_CODE_REG02,ITM01_ITEMS.WIDTH,"+
          "ITM01_ITEMS.WIDTH_UM_CODE_REG02,ITM01_ITEMS.HEIGHT,ITM01_ITEMS.HEIGHT_UM_CODE_REG02,ITM01_ITEMS.NOTE,"+
          "ITM01_ITEMS.LARGE_IMAGE,ITM01_ITEMS.SMALL_IMAGE,REG01_VATS.VALUE,REG01_VATS.DEDUCTIBLE,"+
          "C.DESCRIPTION,ITM01_ITEMS.SERIAL_NUMBER_REQUIRED,PRO01.DESCRIPTION,ITM01_ITEMS.VERSION,ITM01_ITEMS.REVISION, "+
          "ITM01_ITEMS.START_DATE,ITM01_ITEMS.MANUFACTURE_CODE_PRO01, "+
          "ITM01_ITEMS.USE_VARIANT_1,ITM01_ITEMS.USE_VARIANT_2,ITM01_ITEMS.USE_VARIANT_3,ITM01_ITEMS.USE_VARIANT_4,ITM01_ITEMS.USE_VARIANT_5," +
          "ITM01_ITEMS.NO_WAREHOUSE_MOV,ITM01_ITEMS.SHEET_CODE_ITM25 "+
          " from "+
          "SYS10_TRANSLATIONS A,SYS10_TRANSLATIONS B,REG01_VATS,REG02_MEASURE_UNITS REG02_A,SYS10_TRANSLATIONS C,HIE01_LEVELS,ITM01_ITEMS "+
          "LEFT OUTER JOIN "+
          "(select SYS10_TRANSLATIONS.DESCRIPTION,PRO01_MANUFACTURES.MANUFACTURE_CODE,PRO01_MANUFACTURES.COMPANY_CODE_SYS01 "+
          "from PRO01_MANUFACTURES,SYS10_TRANSLATIONS where "+
          "PRO01_MANUFACTURES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=?) PRO01 ON "+
          "PRO01.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
          "PRO01.MANUFACTURE_CODE=ITM01_ITEMS.MANUFACTURE_CODE_PRO01 "+
          "where "+
          "ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02=REG02_A.UM_CODE and "+
          "ITM01_ITEMS.PROGRESSIVE_SYS10=A.PROGRESSIVE and A.LANGUAGE_CODE=? and "+
          "ITM01_ITEMS.VAT_CODE_REG01=REG01_VATS.VAT_CODE and "+
          "REG01_VATS.PROGRESSIVE_SYS10=B.PROGRESSIVE and B.LANGUAGE_CODE=? and "+
          "HIE01_LEVELS.PROGRESSIVE=ITM01_ITEMS.PROGRESSIVE_HIE01 and "+
          "HIE01_LEVELS.PROGRESSIVE=C.PROGRESSIVE and C.LANGUAGE_CODE=? and "+
          "ITM01_ITEMS.COMPANY_CODE_SYS01=? and "+
          "ITM01_ITEMS.ITEM_CODE=?";

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(serverLanguageId);
      values.add(serverLanguageId);
      values.add(serverLanguageId);
      values.add(pk.getCompanyCodeSys01ITM01());
      values.add(pk.getItemCodeITM01());

      // read from ITM01 table...
      Response res = CustomizeQueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          baseSQL,
          values,
          attribute2dbField,
          DetailItemVO.class,
          "Y",
          "N",
          null,
          true,
          customizedFields
      );

      if (!res.isError()) {
        DetailItemVO vo = (DetailItemVO)((VOResponse)res).getVo();
        vo.setProgressiveHie01HIE02(progressiveHie01HIE02);

        stmt = conn.createStatement();

        if (vo.getAddProgressiveSys10ITM01()!=null) {
          // retrieve additional description...
          rset = stmt.executeQuery(
              "select DESCRIPTION from SYS10_TRANSLATIONS where "+
              "PROGRESSIVE="+vo.getAddProgressiveSys10ITM01()+" and LANGUAGE_CODE='"+serverLanguageId+"'"
          );
          if (rset.next())
            vo.setAddDescriptionSYS10(rset.getString(1));
          rset.close();
        }

        if (vo.getGrossWeightUmCodeReg02ITM01()!=null) {
          // retrieve gross weight decimals...
          rset = stmt.executeQuery(
              "select DECIMALS from REG02_MEASURE_UNITS where "+
              "UM_CODE='"+vo.getGrossWeightUmCodeReg02ITM01()+"'"
          );
          if (rset.next())
            vo.setGrossWeightDecimalsREG02(rset.getBigDecimal(1));
          rset.close();
        }

        if (vo.getNetWeightUmCodeReg02ITM01()!=null) {
          // retrieve net weight decimals...
          rset = stmt.executeQuery(
              "select DECIMALS from REG02_MEASURE_UNITS where "+
              "UM_CODE='"+vo.getNetWeightUmCodeReg02ITM01()+"'"
          );
          if (rset.next())
            vo.setNetWeightDecimalsREG02(rset.getBigDecimal(1));
          rset.close();
        }

        if (vo.getWidthUmCodeReg02ITM01()!=null) {
          // retrieve width decimals...
          rset = stmt.executeQuery(
              "select DECIMALS from REG02_MEASURE_UNITS where "+
              "UM_CODE='"+vo.getWidthUmCodeReg02ITM01()+"'"
          );
          if (rset.next())
            vo.setWidthDecimalsREG02(rset.getBigDecimal(1));
          rset.close();
        }

        if (vo.getHeightUmCodeReg02ITM01()!=null) {
          // retrieve height decimals...
          rset = stmt.executeQuery(
              "select DECIMALS from REG02_MEASURE_UNITS where "+
              "UM_CODE='"+vo.getHeightUmCodeReg02ITM01()+"'"
          );
          if (rset.next())
            vo.setHeightDecimalsREG02(rset.getBigDecimal(1));
          rset.close();
        }

        if (vo.getSmallImageITM01()!=null) {
          // load image from file system...
          String appPath = imagePath;
          appPath = appPath.replace('\\','/');
          if (!appPath.endsWith("/"))
            appPath += "/";
          if (!new File(appPath).isAbsolute()) {
            // relative path (to "WEB-INF/classes/" folder)
            appPath = this.getClass().getResource("/").getPath().replaceAll("%20"," ")+appPath;
          }
          File f = new File(appPath+vo.getSmallImageITM01());
          byte[] bytes = new byte[(int)f.length()];
          FileInputStream in = new FileInputStream(f);
          in.read(bytes);
          in.close();
          vo.setSmallImage(bytes);
        }

        if (vo.getLargeImageITM01()!=null) {
          // load image from file system...
          String appPath = imagePath;
          appPath = appPath.replace('\\','/');
          if (!appPath.endsWith("/"))
            appPath += "/";
          if (!new File(appPath).isAbsolute()) {
            // relative path (to "WEB-INF/classes/" folder)
            appPath = this.getClass().getResource("/").getPath().replaceAll("%20"," ")+appPath;
          }
          File f = new File(appPath+vo.getLargeImageITM01());
          byte[] bytes = new byte[(int)f.length()];
          FileInputStream in = new FileInputStream(f);
          in.read(bytes);
          in.close();
          vo.setLargeImage(bytes);
        }


        // retrieve last purchase cost...
        String sql =
            "SELECT DOC06_PURCHASE.DOC_DATE,(DOC07_PURCHASE_ITEMS.VALUE-DOC07_PURCHASE_ITEMS.VAT_VALUE)/QTY,"+
            "REG03_CURRENCIES.DECIMALS,REG03_CURRENCIES.CURRENCY_SYMBOL,REG03_CURRENCIES.DECIMAL_SYMBOL,REG03_CURRENCIES.THOUSAND_SYMBOL,REG03_CURRENCIES.CURRENCY_CODE "+
            "FROM DOC07_PURCHASE_ITEMS,DOC06_PURCHASE,REG03_CURRENCIES "+
            "WHERE DOC07_PURCHASE_ITEMS.COMPANY_CODE_SYS01=? AND "+
            "DOC07_PURCHASE_ITEMS.ITEM_CODE_ITM01=? AND "+
            "DOC07_PURCHASE_ITEMS.COMPANY_CODE_SYS01=DOC06_PURCHASE.COMPANY_CODE_SYS01 AND "+
            "DOC07_PURCHASE_ITEMS.DOC_TYPE=DOC06_PURCHASE.DOC_TYPE AND "+
            "DOC07_PURCHASE_ITEMS.DOC_YEAR=DOC06_PURCHASE.DOC_YEAR AND "+
            "DOC07_PURCHASE_ITEMS.DOC_NUMBER=DOC06_PURCHASE.DOC_NUMBER AND "+
            "DOC06_PURCHASE.DOC_TYPE='P' AND "+
            "DOC06_PURCHASE.ENABLED='Y' AND "+
            "REG03_CURRENCIES.CURRENCY_CODE=DOC06_PURCHASE.CURRENCY_CODE_REG03 "+
            "ORDER BY DOC06_PURCHASE.DOC_DATE DESC ";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1,pk.getCompanyCodeSys01ITM01());
        pstmt.setString(2,pk.getItemCodeITM01());
        rset = pstmt.executeQuery();
        BigDecimal decimals = new BigDecimal("2");
        String currencyCode = null;
        if(rset.next()) {
          vo.setLastPurchaseDate(rset.getDate(1));
          vo.setLastPurchaseCost(rset.getBigDecimal(2));
          decimals = rset.getBigDecimal(3);
          vo.setLastPurchaseCostDecimals(decimals);
          if (decimals!=null && vo.getLastPurchaseCost()!=null) {
            vo.setLastPurchaseCost(vo.getLastPurchaseCost().setScale(decimals.intValue(),BigDecimal.ROUND_HALF_UP));
            vo.setLastPurchaseCostCurrencySymbol(rset.getString(4));
            vo.setLastPurchaseCostDecimalSymbol(rset.getString(5));
            vo.setLastPurchaseCostThousandSymbol(rset.getString(6));
            currencyCode = rset.getString(7);
          }
        }
        rset.close();
        pstmt.close();

        if (currencyCode!=null) {
          // retrieve avg purchase cost...
          sql =
              "SELECT SUM((DOC07_PURCHASE_ITEMS.VALUE-DOC07_PURCHASE_ITEMS.VAT_VALUE)/QTY)/COUNT(*) "+
              "FROM DOC07_PURCHASE_ITEMS,DOC06_PURCHASE "+
              "WHERE DOC07_PURCHASE_ITEMS.COMPANY_CODE_SYS01=? AND "+
              "DOC07_PURCHASE_ITEMS.ITEM_CODE_ITM01=? AND "+
              "DOC07_PURCHASE_ITEMS.COMPANY_CODE_SYS01=DOC06_PURCHASE.COMPANY_CODE_SYS01 AND "+
              "DOC07_PURCHASE_ITEMS.DOC_TYPE=DOC06_PURCHASE.DOC_TYPE AND "+
              "DOC07_PURCHASE_ITEMS.DOC_YEAR=DOC06_PURCHASE.DOC_YEAR AND "+
              "DOC07_PURCHASE_ITEMS.DOC_NUMBER=DOC06_PURCHASE.DOC_NUMBER AND "+
              "DOC06_PURCHASE.DOC_TYPE='P' AND "+
              "DOC06_PURCHASE.ENABLED='Y' AND "+
              "DOC06_PURCHASE.CURRENCY_CODE_REG03=? ";
          pstmt = conn.prepareStatement(sql);
          pstmt.setString(1,pk.getCompanyCodeSys01ITM01());
          pstmt.setString(2,pk.getItemCodeITM01());
          pstmt.setString(3,currencyCode);
          rset = pstmt.executeQuery();
          if(rset.next()) {
            vo.setAvgPurchaseCost(rset.getBigDecimal(1));
            if (decimals!=null && vo.getAvgPurchaseCost()!=null)
              vo.setAvgPurchaseCost(vo.getAvgPurchaseCost().setScale(decimals.intValue(),BigDecimal.ROUND_HALF_UP));
          }
          rset.close();
          pstmt.close();
        }

        if (!Boolean.TRUE.equals(vo.getUseVariant1ITM01()) &&
            !Boolean.TRUE.equals(vo.getUseVariant1ITM01()) &&
            !Boolean.TRUE.equals(vo.getUseVariant1ITM01()) &&
            !Boolean.TRUE.equals(vo.getUseVariant1ITM01()) &&
            !Boolean.TRUE.equals(vo.getUseVariant1ITM01())) {

          // retrieve the min stock for the item that does not have variants...
          sql =
              "select MIN_STOCK from ITM23_VARIANT_MIN_STOCKS where "+
              "COMPANY_CODE_SYS01=? AND ITEM_CODE_ITM01=? AND "+
              "VARIANT_TYPE_ITM06=? and VARIANT_TYPE_ITM07=? and VARIANT_TYPE_ITM08=? and VARIANT_TYPE_ITM09=? and VARIANT_TYPE_ITM10=? and "+
              "VARIANT_CODE_ITM11=? and VARIANT_CODE_ITM12=? and VARIANT_CODE_ITM13=? and VARIANT_CODE_ITM14=? and VARIANT_CODE_ITM15=? ";

          pstmt = conn.prepareStatement(sql);
          pstmt.setString(1,vo.getCompanyCodeSys01());
          pstmt.setString(2,vo.getItemCodeITM01());
          pstmt.setString(3,ApplicationConsts.JOLLY);
          pstmt.setString(4,ApplicationConsts.JOLLY);
          pstmt.setString(5,ApplicationConsts.JOLLY);
          pstmt.setString(6,ApplicationConsts.JOLLY);
          pstmt.setString(7,ApplicationConsts.JOLLY);
          pstmt.setString(8,ApplicationConsts.JOLLY);
          pstmt.setString(9,ApplicationConsts.JOLLY);
          pstmt.setString(10,ApplicationConsts.JOLLY);
          pstmt.setString(11,ApplicationConsts.JOLLY);
          pstmt.setString(12,ApplicationConsts.JOLLY);
          rset = pstmt.executeQuery();
          if (rset.next())
            vo.setMinStockITM23(rset.getBigDecimal(1));
          rset.close();
          pstmt.close();
        }

      } // end if on isError


      if (res.isError())
    	  throw new Exception(res.getErrorMessage());
      else
    	  return (DetailItemVO)((VOResponse)res).getVo();
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"loadItem","Error while fetching an existing item",ex);
      throw new Exception(ex.getMessage());
    }
    finally {
      try {
        stmt.close();
      }
      catch (Exception ex2) {
      }
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

