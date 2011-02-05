package org.jallinone.sales.documents.server;

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
import org.jallinone.commons.java.*;
import org.jallinone.hierarchies.java.*;
import org.jallinone.sales.documents.java.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch customer items + price from SAL01/SAL02 table.</p>
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
public class ValidatePriceItemCodeBean implements ValidatePriceItemCode {


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




  public ValidatePriceItemCodeBean() {
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
   */
  public PriceItemVO getPriceItem() {
	  throw new UnsupportedOperationException();
  }


  /**
   * Business logic to execute.
   */
  public VOListResponse validatePriceItemCode(LookupValidationParams pars,String serverLanguageId,String username) throws Throwable{
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      String companyCodeSYS10 = (String)pars.getLookupValidationParameters().get(ApplicationConsts.COMPANY_CODE_SYS01);
      String pricelistCodeSAL01 = (String)pars.getLookupValidationParameters().get(ApplicationConsts.PRICELIST);

      String sql =
          "select SAL02_PRICES.COMPANY_CODE_SYS01,SAL02_PRICES.ITEM_CODE_ITM01,"+
          "ITM01_ITEMS.PROGRESSIVE_HIE02,ITM01_ITEMS.PROGRESSIVE_HIE01,ITM01_ITEMS.MIN_SELLING_QTY,ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02,"+
          "SYS10_TRANSLATIONS.DESCRIPTION,REG02_MEASURE_UNITS.DECIMALS,ITM01_ITEMS.NO_WAREHOUSE_MOV,"+
          "ITM01_ITEMS.VAT_CODE_REG01,SYS10_VAT.DESCRIPTION,REG01_VATS.DEDUCTIBLE,REG01_VATS.VALUE,"+
          "SAL02_PRICES.VALUE,SAL02_PRICES.START_DATE,SAL02_PRICES.END_DATE,ITM01_ITEMS.SERIAL_NUMBER_REQUIRED, "+
          "ITM01_ITEMS.USE_VARIANT_1,ITM01_ITEMS.USE_VARIANT_2,ITM01_ITEMS.USE_VARIANT_3,ITM01_ITEMS.USE_VARIANT_4,ITM01_ITEMS.USE_VARIANT_5 "+
          " from SAL02_PRICES,SYS10_TRANSLATIONS,ITM01_ITEMS,REG02_MEASURE_UNITS,SYS10_TRANSLATIONS SYS10_VAT,REG01_VATS where "+
          "ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02=REG02_MEASURE_UNITS.UM_CODE and "+
          "SAL02_PRICES.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
          "SAL02_PRICES.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and "+
          "ITM01_ITEMS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "SAL02_PRICES.COMPANY_CODE_SYS01 = ? and "+
          "ITM01_ITEMS.VAT_CODE_REG01=REG01_VATS.VAT_CODE and "+
          "REG01_VATS.PROGRESSIVE_SYS10=SYS10_VAT.PROGRESSIVE and "+
          "SYS10_VAT.LANGUAGE_CODE=? and "+
          "SAL02_PRICES.PRICELIST_CODE_SAL01=? and "+
          "SAL02_PRICES.START_DATE<=? and "+
          "SAL02_PRICES.END_DATE>? ";
      if (Boolean.TRUE.equals(pars.getLookupValidationParameters().get(ApplicationConsts.VALIDATE_BARCODE))) {
        sql += " and ITM01_ITEMS.BAR_CODE=? ";
      }
      else {
        sql += " and SAL02_PRICES.ITEM_CODE_ITM01=? ";
      }

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SAL02","SAL02_PRICES.COMPANY_CODE_SYS01");
      attribute2dbField.put("itemCodeItm01SAL02","SAL02_PRICES.ITEM_CODE_ITM01");
      attribute2dbField.put("progressiveHie02ITM01","ITM01_ITEMS.PROGRESSIVE_HIE02");
      attribute2dbField.put("progressiveHie01ITM01","ITM01_ITEMS.PROGRESSIVE_HIE01");
      attribute2dbField.put("minSellingQtyITM01","ITM01_ITEMS.MIN_SELLING_QTY");
      attribute2dbField.put("minSellingQtyUmCodeReg02ITM01","ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02");
      attribute2dbField.put("itemDescriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("decimalsREG02","REG02_MEASURE_UNITS.DECIMALS");

      attribute2dbField.put("vatCodeReg01ITM01","ITM01_ITEMS.VAT_CODE_REG01");
      attribute2dbField.put("vatDescriptionSYS10","SYS10_VAT.DESCRIPTION");
      attribute2dbField.put("deductibleREG01","REG01_VATS.DEDUCTIBLE");
      attribute2dbField.put("valueREG01","REG01_VATS.VALUE");
      attribute2dbField.put("valueSAL02","SAL02_PRICES.VALUE");
      attribute2dbField.put("startDateSAL02","SAL02_PRICES.START_DATE");
      attribute2dbField.put("endDateSAL02","SAL02_PRICES.END_DATE");
      attribute2dbField.put("serialNumberRequiredITM01","ITM01_ITEMS.SERIAL_NUMBER_REQUIRED");
			attribute2dbField.put("noWarehouseMovITM01","ITM01_ITEMS.NO_WAREHOUSE_MOV");

      attribute2dbField.put("useVariant1ITM01","ITM01_ITEMS.USE_VARIANT_1");
      attribute2dbField.put("useVariant2ITM01","ITM01_ITEMS.USE_VARIANT_2");
      attribute2dbField.put("useVariant3ITM01","ITM01_ITEMS.USE_VARIANT_3");
      attribute2dbField.put("useVariant4ITM01","ITM01_ITEMS.USE_VARIANT_4");
      attribute2dbField.put("useVariant5ITM01","ITM01_ITEMS.USE_VARIANT_5");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(companyCodeSYS10);
      values.add(serverLanguageId);
      values.add(pricelistCodeSAL01);
      values.add(new java.sql.Date(System.currentTimeMillis()));
      values.add(new java.sql.Date(System.currentTimeMillis()));
      values.add(pars.getCode());

      // read from SAL02 table...
      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          PriceItemVO.class,
          "Y",
          "N",
          null,
          new GridParams(),
          true
      );
      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while validating item code",ex);
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

