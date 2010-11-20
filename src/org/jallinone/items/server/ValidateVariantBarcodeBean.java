package org.jallinone.items.server;


import org.openswing.swing.server.*;
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
import org.openswing.swing.message.send.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.jallinone.items.java.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to validate an item code from ITM01 table.</p>
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
public class ValidateVariantBarcodeBean  implements ValidateVariantBarcode {


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




  public ValidateVariantBarcodeBean() {
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
   */
  public VariantBarcodeVO getVariantBarcode() {
	  throw new UnsupportedOperationException();
  }


  /**
   * Business logic to execute.
   */
  public VOListResponse validateVariantBarcode(LookupValidationParams pars,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
     String companyCodeSYS10 = (String)pars.getLookupValidationParameters().get(ApplicationConsts.COMPANY_CODE_SYS01);


      String sql =
          "select "+
          "ITM22_VARIANT_BARCODES.COMPANY_CODE_SYS01,ITM22_VARIANT_BARCODES.ITEM_CODE_ITM01,SYS10_TRANSLATIONS.DESCRIPTION,"+
          "ITM22_VARIANT_BARCODES.VARIANT_TYPE_ITM06,ITM22_VARIANT_BARCODES.VARIANT_TYPE_ITM07,ITM22_VARIANT_BARCODES.VARIANT_TYPE_ITM08,ITM22_VARIANT_BARCODES.VARIANT_TYPE_ITM09,ITM22_VARIANT_BARCODES.VARIANT_TYPE_ITM10,"+
          "ITM22_VARIANT_BARCODES.VARIANT_CODE_ITM11,ITM22_VARIANT_BARCODES.VARIANT_CODE_ITM12,ITM22_VARIANT_BARCODES.VARIANT_CODE_ITM13,ITM22_VARIANT_BARCODES.VARIANT_CODE_ITM14,ITM22_VARIANT_BARCODES.VARIANT_CODE_ITM15,"+
          "ITM22_VARIANT_BARCODES.BAR_CODE,ITM01_ITEMS.SERIAL_NUMBER_REQUIRED,ITM01_ITEMS.PROGRESSIVE_HIE02 "+
 					 "from ITM22_VARIANT_BARCODES,ITM01_ITEMS,SYS10_TRANSLATIONS where "+
          "ITM22_VARIANT_BARCODES.COMPANY_CODE_SYS01=? AND "+
          "ITM22_VARIANT_BARCODES.BAR_CODE=? AND "+
          "ITM22_VARIANT_BARCODES.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 AND "+
          "ITM22_VARIANT_BARCODES.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and ITM01_ITEMS.ENABLED='Y' AND "+
          "ITM01_ITEMS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE AND "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? ";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01ITM22","ITM22_VARIANT_BARCODES.COMPANY_CODE_SYS01");
      attribute2dbField.put("itemCodeItm01ITM22","ITM22_VARIANT_BARCODES.ITEM_CODE_ITM01");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("barCodeITM22","ITM22_VARIANT_BARCODES.BAR_CODE");
      attribute2dbField.put("variantTypeItm06ITM22","ITM22_VARIANT_BARCODES.VARIANT_TYPE_ITM06");
      attribute2dbField.put("variantCodeItm11ITM22","ITM22_VARIANT_BARCODES.VARIANT_CODE_ITM11");
      attribute2dbField.put("variantTypeItm07ITM22","ITM22_VARIANT_BARCODES.VARIANT_TYPE_ITM07");
      attribute2dbField.put("variantCodeItm12ITM22","ITM22_VARIANT_BARCODES.VARIANT_CODE_ITM12");
      attribute2dbField.put("variantTypeItm08ITM22","ITM22_VARIANT_BARCODES.VARIANT_TYPE_ITM08");
      attribute2dbField.put("variantCodeItm13ITM22","ITM22_VARIANT_BARCODES.VARIANT_CODE_ITM13");
      attribute2dbField.put("variantTypeItm09ITM22","ITM22_VARIANT_BARCODES.VARIANT_TYPE_ITM09");
      attribute2dbField.put("variantCodeItm14ITM22","ITM22_VARIANT_BARCODES.VARIANT_CODE_ITM14");
      attribute2dbField.put("variantTypeItm10ITM22","ITM22_VARIANT_BARCODES.VARIANT_TYPE_ITM10");
      attribute2dbField.put("variantCodeItm15ITM22","ITM22_VARIANT_BARCODES.VARIANT_CODE_ITM15");
			attribute2dbField.put("serialNumberRequiredITM01","ITM01_ITEMS.SERIAL_NUMBER_REQUIRED");
			attribute2dbField.put("progressiveHie02ITM01","ITM01_ITEMS.PROGRESSIVE_HIE02");


      ArrayList values = new ArrayList();
      values.add(companyCodeSYS10);
      values.add(pars.getCode());
      values.add(serverLanguageId);

      // read from ITM01 table...
      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          VariantBarcodeVO.class,
          "Y",
          "N",
          null,
          new GridParams(),
          true
      );

      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while validating variants barcode",ex);
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

