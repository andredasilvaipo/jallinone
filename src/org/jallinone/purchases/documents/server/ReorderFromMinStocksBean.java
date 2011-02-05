package org.jallinone.purchases.documents.server;

import org.openswing.swing.server.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.warehouse.java.*;
import org.jallinone.system.server.*;
import org.openswing.swing.server.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.commons.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.jallinone.purchases.documents.java.*;

import java.math.*;
import java.util.*;
import org.jallinone.variants.java.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to retrieve the list of variants/items having a min stock declared in the item detail.</p>
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
public class ReorderFromMinStocksBean  implements ReorderFromMinStocks {


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




  public ReorderFromMinStocksBean() {
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
   */
  public ReorderFromMinStockVO getReorderFromMinStock(ReorderFromMinStockFilterVO pk) {
	  throw new UnsupportedOperationException();
  }


  /**
   * Business logic to execute.
   */
  public VOListResponse reorderFromMinStocks(
		HashMap variant1Descriptions,
		HashMap variant2Descriptions,
		HashMap variant3Descriptions,
		HashMap variant4Descriptions,
		HashMap variant5Descriptions,
		GridParams gridPars, String serverLanguageId, String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      ReorderFromMinStockFilterVO filterVO = (ReorderFromMinStockFilterVO)gridPars.getOtherGridParams().get(ApplicationConsts.FILTER_VO);
      String companyCode = (String)filterVO.getCompanyCode();
      BigDecimal progressiveREG04 = filterVO.getProgressiveREG04();
      String warehouseCode = filterVO.getWarehouseCode();
      String currencyCode = filterVO.getCurrencyCodeREG03();
      BigDecimal progressiveHIE02 = filterVO.getProgressiveHie02ITM01();


      // will be retrieved only items that
      // - belong to the specified item type
      // - have a MIN_STOCK defined
      // - are supplied by at leat one vendor
      // - are related to a specific company
      // NOTE: after calculating proposed qtys, some items could be removed from list, if min stock was not reached
      String sql =
          "select ITM23_VARIANT_MIN_STOCKS.MIN_STOCK, "+
          "ITM23_VARIANT_MIN_STOCKS.COMPANY_CODE_SYS01,ITM23_VARIANT_MIN_STOCKS.ITEM_CODE_ITM01,T1.DESCRIPTION, "+
          "ITM23_VARIANT_MIN_STOCKS.VARIANT_TYPE_ITM06,ITM23_VARIANT_MIN_STOCKS.VARIANT_TYPE_ITM07,ITM23_VARIANT_MIN_STOCKS.VARIANT_TYPE_ITM08,"+
          "ITM23_VARIANT_MIN_STOCKS.VARIANT_TYPE_ITM09,ITM23_VARIANT_MIN_STOCKS.VARIANT_TYPE_ITM10, "+
          "ITM23_VARIANT_MIN_STOCKS.VARIANT_CODE_ITM11,ITM23_VARIANT_MIN_STOCKS.VARIANT_CODE_ITM12,"+
          "ITM23_VARIANT_MIN_STOCKS.VARIANT_CODE_ITM13,ITM23_VARIANT_MIN_STOCKS.VARIANT_CODE_ITM14,ITM23_VARIANT_MIN_STOCKS.VARIANT_CODE_ITM15, "+
          "REG01_VATS.VAT_CODE,T2.DESCRIPTION,REG01_VATS.VALUE,REG01_VATS.DEDUCTIBLE "+
          " from ITM23_VARIANT_MIN_STOCKS,ITM01_ITEMS,REG01_VATS,SYS10_TRANSLATIONS T1,SYS10_TRANSLATIONS T2 where "+
          "ITM23_VARIANT_MIN_STOCKS.COMPANY_CODE_SYS01=? AND ITM23_VARIANT_MIN_STOCKS.MIN_STOCK>0 AND "+
          "ITM01_ITEMS.PROGRESSIVE_HIE02=? AND "+
          "ITM23_VARIANT_MIN_STOCKS.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 AND "+
          "ITM23_VARIANT_MIN_STOCKS.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE AND "+
          "ITM01_ITEMS.ENABLED='Y' AND "+
          "ITM01_ITEMS.PROGRESSIVE_SYS10=T1.PROGRESSIVE AND "+
          "T1.LANGUAGE_CODE=? AND "+
          "ITM01_ITEMS.VAT_CODE_REG01=REG01_VATS.VAT_CODE AND "+
          "REG01_VATS.PROGRESSIVE_SYS10=T2.PROGRESSIVE AND "+
          "T2.LANGUAGE_CODE=? AND "+
          "ITM23_VARIANT_MIN_STOCKS.ITEM_CODE_ITM01 IN "+
          "(SELECT PUR04_SUPPLIER_PRICES.ITEM_CODE_ITM01 FROM PUR04_SUPPLIER_PRICES,PUR03_SUPPLIER_PRICELISTS "+
          " WHERE PUR04_SUPPLIER_PRICES.COMPANY_CODE_SYS01=? AND "+
          " PUR04_SUPPLIER_PRICES.COMPANY_CODE_SYS01=PUR03_SUPPLIER_PRICELISTS.COMPANY_CODE_SYS01 AND " +
          " PUR04_SUPPLIER_PRICES.PROGRESSIVE_REG04=PUR03_SUPPLIER_PRICELISTS.PROGRESSIVE_REG04 AND " +
          " PUR04_SUPPLIER_PRICES.PRICELIST_CODE_PUR03=PUR03_SUPPLIER_PRICELISTS.PRICELIST_CODE AND " +
          " PUR03_SUPPLIER_PRICELISTS.CURRENCY_CODE_REG03=? AND "+
          " PUR04_SUPPLIER_PRICES.START_DATE<=? AND PUR04_SUPPLIER_PRICES.END_DATE>=? ) ";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("minStockITM23","ITM23_VARIANT_MIN_STOCKS.MIN_STOCK");
      attribute2dbField.put("companyCodeSys01","ITM23_VARIANT_MIN_STOCKS.COMPANY_CODE_SYS01");
      attribute2dbField.put("itemCode","ITM23_VARIANT_MIN_STOCKS.ITEM_CODE_ITM01");
      attribute2dbField.put("itemDescription","T1.DESCRIPTION");
      attribute2dbField.put("variantTypeITM06","ITM23_VARIANT_MIN_STOCKS.VARIANT_TYPE_ITM06");
      attribute2dbField.put("variantTypeITM07","ITM23_VARIANT_MIN_STOCKS.VARIANT_TYPE_ITM07");
      attribute2dbField.put("variantTypeITM08","ITM23_VARIANT_MIN_STOCKS.VARIANT_TYPE_ITM08");
      attribute2dbField.put("variantTypeITM09","ITM23_VARIANT_MIN_STOCKS.VARIANT_TYPE_ITM09");
      attribute2dbField.put("variantTypeITM10","ITM23_VARIANT_MIN_STOCKS.VARIANT_TYPE_ITM10");
      attribute2dbField.put("variantCodeITM11","ITM23_VARIANT_MIN_STOCKS.VARIANT_CODE_ITM11");
      attribute2dbField.put("variantCodeITM12","ITM23_VARIANT_MIN_STOCKS.VARIANT_CODE_ITM12");
      attribute2dbField.put("variantCodeITM13","ITM23_VARIANT_MIN_STOCKS.VARIANT_CODE_ITM13");
      attribute2dbField.put("variantCodeITM14","ITM23_VARIANT_MIN_STOCKS.VARIANT_CODE_ITM14");
      attribute2dbField.put("variantCodeITM15","ITM23_VARIANT_MIN_STOCKS.VARIANT_CODE_ITM15");

      attribute2dbField.put("vatCode","REG01_VATS.VAT_CODE");
      attribute2dbField.put("vatDescription","T2.DESCRIPTION");
      attribute2dbField.put("vatValue","REG01_VATS.VALUE");
      attribute2dbField.put("deductible","REG01_VATS.DEDUCTIBLE");

      java.sql.Date today = new java.sql.Date(System.currentTimeMillis());

      ArrayList params = new ArrayList();
      params.add(companyCode);
      params.add(progressiveHIE02);
      params.add(serverLanguageId);
      params.add(serverLanguageId);
      params.add(companyCode);
      params.add(currencyCode);
      params.add(today);
      params.add(today);

      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          params,
          attribute2dbField,
          ReorderFromMinStockVO.class,
          "Y",
          "N",
          null,
          gridPars,
          true
      );

      java.util.List rows = new ArrayList();
      if (!answer.isError())
        rows = ((VOListResponse)answer).getRows();
      else
        throw new Exception(answer.getErrorMessage());

      if (rows.size()>0) {
        // fetch supplier info for the just retrieved items ...
        sql =
            "SELECT PUR04_SUPPLIER_PRICES.ITEM_CODE_ITM01,PUR04_SUPPLIER_PRICES.PROGRESSIVE_REG04,"+
            "PUR01_SUPPLIERS.SUPPLIER_CODE,REG04_SUBJECTS.NAME_1,PUR04_SUPPLIER_PRICES.PRICELIST_CODE_PUR03,"+
            "PUR01_SUPPLIERS.PAYMENT_CODE_REG10,PUR04_SUPPLIER_PRICES.VALUE,SYS10_TRANSLATIONS.DESCRIPTION,"+
            "PUR02_SUPPLIER_ITEMS.SUPPLIER_ITEM_CODE,PUR04_SUPPLIER_PRICES.START_DATE,PUR04_SUPPLIER_PRICES.END_DATE, "+
            "PUR02_SUPPLIER_ITEMS.UM_CODE_REG02,PUR02_SUPPLIER_ITEMS.MIN_PURCHASE_QTY,PUR02_SUPPLIER_ITEMS.MULTIPLE_QTY "+
            " FROM "+
            "PUR04_SUPPLIER_PRICES,PUR01_SUPPLIERS,REG04_SUBJECTS,PUR03_SUPPLIER_PRICELISTS,PUR02_SUPPLIER_ITEMS,SYS10_TRANSLATIONS "+
            "WHERE PUR04_SUPPLIER_PRICES.COMPANY_CODE_SYS01=? AND "+
            "PUR02_SUPPLIER_ITEMS.COMPANY_CODE_SYS01=PUR04_SUPPLIER_PRICES.COMPANY_CODE_SYS01 AND "+
            "PUR02_SUPPLIER_ITEMS.PROGRESSIVE_REG04=PUR04_SUPPLIER_PRICES.PROGRESSIVE_REG04 AND "+
            "PUR02_SUPPLIER_ITEMS.ITEM_CODE_ITM01=PUR04_SUPPLIER_PRICES.ITEM_CODE_ITM01 AND "+
            "PUR02_SUPPLIER_ITEMS.PROGRESSIVE_HIE02=? AND "+
            "PUR04_SUPPLIER_PRICES.COMPANY_CODE_SYS01=PUR01_SUPPLIERS.COMPANY_CODE_SYS01 AND "+
            "PUR04_SUPPLIER_PRICES.PROGRESSIVE_REG04=PUR01_SUPPLIERS.PROGRESSIVE_REG04 AND "+
            "PUR01_SUPPLIERS.COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 AND "+
            "PUR01_SUPPLIERS.PROGRESSIVE_REG04=REG04_SUBJECTS.PROGRESSIVE AND "+
            "PUR04_SUPPLIER_PRICES.COMPANY_CODE_SYS01=PUR03_SUPPLIER_PRICELISTS.COMPANY_CODE_SYS01 AND " +
            "PUR04_SUPPLIER_PRICES.PROGRESSIVE_REG04=PUR03_SUPPLIER_PRICELISTS.PROGRESSIVE_REG04 AND " +
            "PUR04_SUPPLIER_PRICES.PRICELIST_CODE_PUR03=PUR03_SUPPLIER_PRICELISTS.PRICELIST_CODE AND " +
            "PUR03_SUPPLIER_PRICELISTS.CURRENCY_CODE_REG03=? AND "+
            "PUR04_SUPPLIER_PRICES.START_DATE<=? AND PUR04_SUPPLIER_PRICES.END_DATE>=? AND "+
            "PUR03_SUPPLIER_PRICELISTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
            "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
            "PUR04_SUPPLIER_PRICES.ITEM_CODE_ITM01 IN "+
            "(SELECT ITEM_CODE_ITM01 FROM ITM23_VARIANT_MIN_STOCKS "+
            " WHERE ITM23_VARIANT_MIN_STOCKS.COMPANY_CODE_SYS01=? AND ITM23_VARIANT_MIN_STOCKS.MIN_STOCK>0) "+
            "ORDER BY PUR04_SUPPLIER_PRICES.VALUE,PUR04_SUPPLIER_PRICES.PRICELIST_CODE_PUR03";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1,companyCode);
        pstmt.setBigDecimal(2,progressiveHIE02);
        pstmt.setString(3,currencyCode);
        pstmt.setDate(4,today);
        pstmt.setDate(5,today);
        pstmt.setString(6,serverLanguageId);
        pstmt.setString(7,companyCode);
        ResultSet rset = pstmt.executeQuery();
        HashMap suppliers = new HashMap(); // couples <itemCode,Supplier>
        String itemCode = null;
        Supplier s = null;
        BigDecimal currentProgressiveREG04 = null;
        while(rset.next()) {
          itemCode = rset.getString(1);
          currentProgressiveREG04 = rset.getBigDecimal(2);
          if (currentProgressiveREG04.equals(progressiveREG04) || // prefer always the specified supplier...
              !suppliers.containsKey(itemCode)) {
            s = new Supplier();
            s.progressiveREG04 = currentProgressiveREG04;
            s.supplierCode = rset.getString(3);
            s.name_1 = rset.getString(4);
            s.pricelistCodePUR03 = rset.getString(5);
            s.paymentCodeREG10 = rset.getString(6);
            s.unitPrice = rset.getBigDecimal(7);
            s.pricelistDescription = rset.getString(8);
            s.supplierItemCode = rset.getString(9);
            s.startDate = rset.getDate(10);
            s.endDate = rset.getDate(11);
            s.umCodeREG02 = rset.getString(12);
            s.minPurchaseQtyPUR02 = rset.getBigDecimal(13);
            s.multipleQtyPUR02 = rset.getBigDecimal(14);
            suppliers.put(itemCode,s);
          }
        }
        rset.close();
        pstmt.close();


        // retrieve available qtys for the same set of items...
        sql =
            "SELECT ITEM_CODE_ITM01,"+
            "VARIANT_TYPE_ITM06,VARIANT_CODE_ITM11,"+
            "VARIANT_TYPE_ITM07,VARIANT_CODE_ITM12,"+
            "VARIANT_TYPE_ITM08,VARIANT_CODE_ITM13,"+
            "VARIANT_TYPE_ITM09,VARIANT_CODE_ITM14,"+
            "VARIANT_TYPE_ITM10,VARIANT_CODE_ITM15,"+
            "SUM(AVAILABLE_QTY) FROM WAR03_ITEMS_AVAILABILITY WHERE "+
          "COMPANY_CODE_SYS01=? AND WAREHOUSE_CODE_WAR01=? AND EXISTS("+

          "SELECT * FROM ITM23_VARIANT_MIN_STOCKS WHERE "+
          "ITM23_VARIANT_MIN_STOCKS.COMPANY_CODE_SYS01=? AND "+
          "ITM23_VARIANT_MIN_STOCKS.ITEM_CODE_ITM01=WAR03_ITEMS_AVAILABILITY.ITEM_CODE_ITM01 AND "+
          "ITM23_VARIANT_MIN_STOCKS.VARIANT_TYPE_ITM06=WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM06 AND "+
          "ITM23_VARIANT_MIN_STOCKS.VARIANT_TYPE_ITM07=WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM07 AND "+
          "ITM23_VARIANT_MIN_STOCKS.VARIANT_TYPE_ITM08=WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM08 AND "+
          "ITM23_VARIANT_MIN_STOCKS.VARIANT_TYPE_ITM09=WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM09 AND "+
          "ITM23_VARIANT_MIN_STOCKS.VARIANT_TYPE_ITM10=WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM10 AND  "+
          "ITM23_VARIANT_MIN_STOCKS.VARIANT_CODE_ITM11=WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM11 AND "+
          "ITM23_VARIANT_MIN_STOCKS.VARIANT_CODE_ITM12=WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM12 AND "+
          "ITM23_VARIANT_MIN_STOCKS.VARIANT_CODE_ITM13=WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM13 AND "+
          "ITM23_VARIANT_MIN_STOCKS.VARIANT_CODE_ITM14=WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM14 AND "+
          "ITM23_VARIANT_MIN_STOCKS.VARIANT_CODE_ITM15=WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM15 AND "+
          "ITM23_VARIANT_MIN_STOCKS.MIN_STOCK>0) "+

          "GROUP BY ITEM_CODE_ITM01,"+
          "VARIANT_TYPE_ITM06,VARIANT_CODE_ITM11,"+
          "VARIANT_TYPE_ITM07,VARIANT_CODE_ITM12,"+
          "VARIANT_TYPE_ITM08,VARIANT_CODE_ITM13,"+
          "VARIANT_TYPE_ITM09,VARIANT_CODE_ITM14,"+
          "VARIANT_TYPE_ITM10,VARIANT_CODE_ITM15 ";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1,companyCode);
        pstmt.setString(2,warehouseCode);
        pstmt.setString(3,companyCode);
        rset = pstmt.executeQuery();
        HashMap availableQtys = new HashMap(); // couples <VariantItemPK,qty>
        VariantItemPK pk = null;
        while(rset.next()) {
          pk = new VariantItemPK(
            companyCode,
            rset.getString(1),
            rset.getString(2),
            rset.getString(3),
            rset.getString(4),
            rset.getString(5),
            rset.getString(6),
            rset.getString(7),
            rset.getString(8),
            rset.getString(9),
            rset.getString(10),
            rset.getString(11)
          );
          availableQtys.put(pk,rset.getBigDecimal(12));
        }
        rset.close();
        pstmt.close();


        // retrieve sold qtys...
        sql =
            "SELECT DOC02_SELLING_ITEMS.ITEM_CODE_ITM01,"+
            "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM06,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM11,"+
            "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM07,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM12,"+
            "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM08,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM13,"+
            "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM09,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM14,"+
            "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM10,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM15, "+
            "SUM(DOC02_SELLING_ITEMS.QTY-DOC02_SELLING_ITEMS.OUT_QTY) "+
            "FROM DOC02_SELLING_ITEMS,DOC01_SELLING WHERE "+
            "DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01=? AND "+
            "DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01=DOC01_SELLING.COMPANY_CODE_SYS01 AND "+
            "DOC02_SELLING_ITEMS.DOC_TYPE=DOC01_SELLING.DOC_TYPE AND "+
            "DOC02_SELLING_ITEMS.DOC_YEAR=DOC01_SELLING.DOC_YEAR AND "+
            "DOC02_SELLING_ITEMS.DOC_NUMBER=DOC01_SELLING.DOC_NUMBER AND "+
            "DOC01_SELLING.DOC_STATE=? AND "+
            "DOC01_SELLING.DOC_TYPE IN (?,?,?) AND "+
            "DOC01_SELLING.WAREHOUSE_CODE_WAR01=? AND EXISTS("+

            "SELECT * FROM ITM23_VARIANT_MIN_STOCKS WHERE "+
            "ITM23_VARIANT_MIN_STOCKS.COMPANY_CODE_SYS01=? AND "+
            "ITM23_VARIANT_MIN_STOCKS.ITEM_CODE_ITM01=DOC02_SELLING_ITEMS.ITEM_CODE_ITM01 AND "+
            "ITM23_VARIANT_MIN_STOCKS.VARIANT_TYPE_ITM06=DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM06 AND "+
            "ITM23_VARIANT_MIN_STOCKS.VARIANT_TYPE_ITM07=DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM07 AND "+
            "ITM23_VARIANT_MIN_STOCKS.VARIANT_TYPE_ITM08=DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM08 AND "+
            "ITM23_VARIANT_MIN_STOCKS.VARIANT_TYPE_ITM09=DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM09 AND "+
            "ITM23_VARIANT_MIN_STOCKS.VARIANT_TYPE_ITM10=DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM10 AND  "+
            "ITM23_VARIANT_MIN_STOCKS.VARIANT_CODE_ITM11=DOC02_SELLING_ITEMS.VARIANT_CODE_ITM11 AND "+
            "ITM23_VARIANT_MIN_STOCKS.VARIANT_CODE_ITM12=DOC02_SELLING_ITEMS.VARIANT_CODE_ITM12 AND "+
            "ITM23_VARIANT_MIN_STOCKS.VARIANT_CODE_ITM13=DOC02_SELLING_ITEMS.VARIANT_CODE_ITM13 AND "+
            "ITM23_VARIANT_MIN_STOCKS.VARIANT_CODE_ITM14=DOC02_SELLING_ITEMS.VARIANT_CODE_ITM14 AND "+
            "ITM23_VARIANT_MIN_STOCKS.VARIANT_CODE_ITM15=DOC02_SELLING_ITEMS.VARIANT_CODE_ITM15 AND "+
            "ITM23_VARIANT_MIN_STOCKS.MIN_STOCK>0) "+

            "GROUP BY DOC02_SELLING_ITEMS.ITEM_CODE_ITM01,"+
            "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM06,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM11,"+
            "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM07,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM12,"+
            "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM08,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM13,"+
            "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM09,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM14,"+
            "DOC02_SELLING_ITEMS.VARIANT_TYPE_ITM10,DOC02_SELLING_ITEMS.VARIANT_CODE_ITM15 ";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1,companyCode);
        pstmt.setString(2,ApplicationConsts.CONFIRMED);
        pstmt.setString(3,ApplicationConsts.SALE_ORDER_DOC_TYPE);
        pstmt.setString(4,ApplicationConsts.SALE_CONTRACT_DOC_TYPE);
        pstmt.setString(5,ApplicationConsts.SALE_DESK_DOC_TYPE);
        pstmt.setString(6,warehouseCode);
        pstmt.setString(7,companyCode);
        rset = pstmt.executeQuery();
        HashMap soldQtys = new HashMap(); // couples <VariantItemPK,qty>
        while(rset.next()) {
          pk = new VariantItemPK(
            companyCode,
            rset.getString(1),
            rset.getString(2),
            rset.getString(3),
            rset.getString(4),
            rset.getString(5),
            rset.getString(6),
            rset.getString(7),
            rset.getString(8),
            rset.getString(9),
            rset.getString(10),
            rset.getString(11)
          );
          soldQtys.put(pk,rset.getBigDecimal(12));
        }
        rset.close();
        pstmt.close();


        // retrieve ordered qtys...
        sql =
            "select DOC07_PURCHASE_ITEMS.ITEM_CODE_ITM01,"+
            "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM06,DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM11,"+
            "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM07,DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM12,"+
            "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM08,DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM13,"+
            "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM09,DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM14,"+
            "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM10,DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM15, "+
            "SUM(DOC07_PURCHASE_ITEMS.QTY-DOC07_PURCHASE_ITEMS.IN_QTY) "+
            "from DOC07_PURCHASE_ITEMS,DOC06_PURCHASE where "+
            "DOC07_PURCHASE_ITEMS.COMPANY_CODE_SYS01=DOC06_PURCHASE.COMPANY_CODE_SYS01 AND "+
            "DOC07_PURCHASE_ITEMS.DOC_TYPE=DOC06_PURCHASE.DOC_TYPE AND "+
            "DOC07_PURCHASE_ITEMS.DOC_YEAR=DOC06_PURCHASE.DOC_YEAR AND "+
            "DOC07_PURCHASE_ITEMS.DOC_NUMBER=DOC06_PURCHASE.DOC_NUMBER AND "+
            "DOC07_PURCHASE_ITEMS.COMPANY_CODE_SYS01=? and "+
            "DOC06_PURCHASE.DOC_STATE=? and "+
            "DOC06_PURCHASE.DOC_TYPE=? and "+
            "DOC06_PURCHASE.WAREHOUSE_CODE_WAR01=? AND EXISTS("+

            "SELECT * FROM ITM23_VARIANT_MIN_STOCKS WHERE "+
            "ITM23_VARIANT_MIN_STOCKS.COMPANY_CODE_SYS01=? AND "+
            "ITM23_VARIANT_MIN_STOCKS.ITEM_CODE_ITM01=DOC07_PURCHASE_ITEMS.ITEM_CODE_ITM01 AND "+
            "ITM23_VARIANT_MIN_STOCKS.VARIANT_TYPE_ITM06=DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM06 AND "+
            "ITM23_VARIANT_MIN_STOCKS.VARIANT_TYPE_ITM07=DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM07 AND "+
            "ITM23_VARIANT_MIN_STOCKS.VARIANT_TYPE_ITM08=DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM08 AND "+
            "ITM23_VARIANT_MIN_STOCKS.VARIANT_TYPE_ITM09=DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM09 AND "+
            "ITM23_VARIANT_MIN_STOCKS.VARIANT_TYPE_ITM10=DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM10 AND  "+
            "ITM23_VARIANT_MIN_STOCKS.VARIANT_CODE_ITM11=DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM11 AND "+
            "ITM23_VARIANT_MIN_STOCKS.VARIANT_CODE_ITM12=DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM12 AND "+
            "ITM23_VARIANT_MIN_STOCKS.VARIANT_CODE_ITM13=DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM13 AND "+
            "ITM23_VARIANT_MIN_STOCKS.VARIANT_CODE_ITM14=DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM14 AND "+
            "ITM23_VARIANT_MIN_STOCKS.VARIANT_CODE_ITM15=DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM15 AND "+
            "ITM23_VARIANT_MIN_STOCKS.MIN_STOCK>0) "+

            "GROUP BY DOC07_PURCHASE_ITEMS.ITEM_CODE_ITM01,"+
            "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM06,DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM11,"+
            "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM07,DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM12,"+
            "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM08,DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM13,"+
            "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM09,DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM14,"+
            "DOC07_PURCHASE_ITEMS.VARIANT_TYPE_ITM10,DOC07_PURCHASE_ITEMS.VARIANT_CODE_ITM15 ";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1,companyCode);
        pstmt.setString(2,ApplicationConsts.CONFIRMED);
        pstmt.setString(3,ApplicationConsts.PURCHASE_ORDER_DOC_TYPE);
        pstmt.setString(4,warehouseCode);
        pstmt.setString(5,companyCode);
        rset = pstmt.executeQuery();
        HashMap orderedQtys = new HashMap(); // couples <VariantItemPK,qty>
        while(rset.next()) {
          pk = new VariantItemPK(
            companyCode,
            rset.getString(1),
            rset.getString(2),
            rset.getString(3),
            rset.getString(4),
            rset.getString(5),
            rset.getString(6),
            rset.getString(7),
            rset.getString(8),
            rset.getString(9),
            rset.getString(10),
            rset.getString(11)
          );
          orderedQtys.put(pk,rset.getBigDecimal(12));
        }
        rset.close();
        pstmt.close();


        // fill in all other attributes...
        ReorderFromMinStockVO vo = null;
        int i=0;
				String descr = null;
        while(i<rows.size()) {
          vo = (ReorderFromMinStockVO)rows.get(i);

					descr = vo.getItemDescription();

					// check supported variants for current item...
					if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeITM11())) {
						descr += " "+getVariantCodeAndTypeDesc(
							variant1Descriptions,
							vo,
							vo.getVariantTypeITM06(),
							vo.getVariantCodeITM11(),
							serverLanguageId,
							username
						);
					}
					if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeITM12())) {
						descr += " "+getVariantCodeAndTypeDesc(
							variant2Descriptions,
							vo,
							vo.getVariantTypeITM07(),
							vo.getVariantCodeITM12(),
							serverLanguageId,
							username
						);
					}
					if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeITM13())) {
						descr += " "+getVariantCodeAndTypeDesc(
							variant3Descriptions,
							vo,
							vo.getVariantTypeITM08(),
							vo.getVariantCodeITM13(),
							serverLanguageId,
							username
						);
					}
					if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeITM14())) {
						descr += " "+getVariantCodeAndTypeDesc(
							variant4Descriptions,
							vo,
							vo.getVariantTypeITM09(),
							vo.getVariantCodeITM14(),
							serverLanguageId,
							username
						);
					}
					if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeITM15())) {
						descr += " "+getVariantCodeAndTypeDesc(
							variant5Descriptions,
							vo,
							vo.getVariantTypeITM10(),
							vo.getVariantCodeITM15(),
							serverLanguageId,
							username
						);
					}
					vo.setItemDescription(descr);



          pk = new VariantItemPK(
            companyCode,
            vo.getItemCode(),
            vo.getVariantTypeITM06(),
            vo.getVariantCodeITM11(),
            vo.getVariantTypeITM07(),
            vo.getVariantCodeITM12(),
            vo.getVariantTypeITM08(),
            vo.getVariantCodeITM13(),
            vo.getVariantTypeITM09(),
            vo.getVariantCodeITM14(),
            vo.getVariantTypeITM10(),
            vo.getVariantCodeITM15()
          );


          // fill in supplier info...
          s = (Supplier)suppliers.get(vo.getItemCode());
          vo.setProgressiveREG04(s.progressiveREG04);
          vo.setSupplierCode(s.supplierCode);
          vo.setName_1REG04(s.name_1);
          vo.setPricelistCodePUR03(s.pricelistCodePUR03);
          vo.setPaymentCodeREG10(s.paymentCodeREG10);
          vo.setUnitPrice(s.unitPrice);
          vo.setPricelistDescription(s.pricelistDescription);
          vo.setSupplierItemCode(s.supplierItemCode);
          vo.setStartDate(s.startDate);
          vo.setEndDate(s.endDate);
          vo.setUmCodeREG02(s.umCodeREG02);
          vo.setMinPurchaseQtyPUR02(s.minPurchaseQtyPUR02);
          vo.setMultipleQtyPUR02(s.multipleQtyPUR02);


          // fill in ordered qtys...
          if (orderedQtys.containsKey(pk)) {
            vo.setOrderedQty((BigDecimal)orderedQtys.get(pk));
          }
          else
            vo.setOrderedQty(new BigDecimal(0));


          // fill in available qtys...
          if (availableQtys.containsKey(pk)) {
            vo.setGoodQty((BigDecimal)availableQtys.get(pk));
          }
          else
            vo.setGoodQty(new BigDecimal(0));


            // fill in pawned qtys...
            if (soldQtys.containsKey(pk)) {
              vo.setPawnedQty((BigDecimal)soldQtys.get(pk));
            }
            else
              vo.setPawnedQty(new BigDecimal(0));


            // set available qtys...
            vo.setAvailableQty(vo.getGoodQty().subtract(vo.getPawnedQty()));

            // set qtys...
            vo.setProposedQty(vo.getMinStockITM23().subtract(vo.getGoodQty()).add(vo.getPawnedQty()).subtract(vo.getOrderedQty()));
            if (vo.getProposedQty().doubleValue()<0) {
              // skip row...
              rows.remove(i);
            }
            else {
              vo.setQty(vo.getProposedQty());
              i++;
            }
        } // end for

      }

      return new VOListResponse(rows,false,rows.size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while retrieving data for reorder list",ex);
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

			private String getVariantCodeAndTypeDesc(
					HashMap variantDescriptions,
					ReorderFromMinStockVO vo,
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




  class Supplier {

    public String supplierCode;
    public String name_1;
    public BigDecimal progressiveREG04;
    public String pricelistCodePUR03;
    public String pricelistDescription;
    public String paymentCodeREG10;
    public BigDecimal unitPrice;
    public String supplierItemCode;
    public java.sql.Date startDate;
    public java.sql.Date endDate;
    public BigDecimal minPurchaseQtyPUR02;
    public BigDecimal multipleQtyPUR02;
    public String umCodeREG02;

  }


}

