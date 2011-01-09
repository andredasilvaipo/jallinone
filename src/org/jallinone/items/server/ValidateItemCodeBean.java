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
public class ValidateItemCodeBean  implements ValidateItemCode {


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




  public ValidateItemCodeBean() {
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
   */
  public GridItemVO getGridItem() {
	  throw new UnsupportedOperationException();
  }


  /**
   * Business logic to execute.
   */
  public VOListResponse validateItemCode(LookupValidationParams pars,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;


      String companyCodeSYS10 = (String)pars.getLookupValidationParameters().get(ApplicationConsts.COMPANY_CODE_SYS01);
      BigDecimal progressiveHIE02 = (BigDecimal)pars.getLookupValidationParameters().get(ApplicationConsts.PROGRESSIVE_HIE02);
      Boolean productsOnly = (Boolean)pars.getLookupValidationParameters().get(ApplicationConsts.PRODUCTS_ONLY);
      Boolean compsOnly = (Boolean)pars.getLookupValidationParameters().get(ApplicationConsts.COMPONENTS_ONLY);
			Boolean showOnlyPurchasedItems = (Boolean)pars.getLookupValidationParameters().get(ApplicationConsts.SHOW_ONLY_PURCHASED_ITEMS); // used in call-out request to filter purchased items only
			BigDecimal progressiveREG04 = (BigDecimal)pars.getLookupValidationParameters().get(ApplicationConsts.PROGRESSIVE_REG04);


			Map attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01ITM01","ITM01_ITEMS.COMPANY_CODE_SYS01");
			attribute2dbField.put("itemCodeITM01","ITM01_ITEMS.ITEM_CODE");
			attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
			attribute2dbField.put("progressiveHie02ITM01","ITM01_ITEMS.PROGRESSIVE_HIE02");
			attribute2dbField.put("minSellingQtyUmCodeReg02ITM01","ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02");
			attribute2dbField.put("progressiveHie01ITM01","ITM01_ITEMS.PROGRESSIVE_HIE01");
			attribute2dbField.put("serialNumberRequiredITM01","ITM01_ITEMS.SERIAL_NUMBER_REQUIRED");
			attribute2dbField.put("decimalsREG02","REG02_MEASURE_UNITS.DECIMALS");
			attribute2dbField.put("noWarehouseMovITM01","ITM01_ITEMS.NO_WAREHOUSE_MOV");
			attribute2dbField.put("sheetCodeItm25ITM01","ITM01_ITEMS.SHEET_CODE_ITM25");

			attribute2dbField.put("useVariant1ITM01","ITM01_ITEMS.USE_VARIANT_1");
			attribute2dbField.put("useVariant2ITM01","ITM01_ITEMS.USE_VARIANT_2");
			attribute2dbField.put("useVariant3ITM01","ITM01_ITEMS.USE_VARIANT_3");
			attribute2dbField.put("useVariant4ITM01","ITM01_ITEMS.USE_VARIANT_4");
			attribute2dbField.put("useVariant5ITM01","ITM01_ITEMS.USE_VARIANT_5");



      String select =
          "select ITM01_ITEMS.COMPANY_CODE_SYS01,ITM01_ITEMS.ITEM_CODE,SYS10_TRANSLATIONS.DESCRIPTION,ITM01_ITEMS.PROGRESSIVE_HIE02,ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02,"+
          "ITM01_ITEMS.PROGRESSIVE_HIE01,ITM01_ITEMS.SERIAL_NUMBER_REQUIRED,REG02_MEASURE_UNITS.DECIMALS, "+
					"ITM01_ITEMS.NO_WAREHOUSE_MOV,ITM01_ITEMS.SHEET_CODE_ITM25,"+
          "ITM01_ITEMS.USE_VARIANT_1,ITM01_ITEMS.USE_VARIANT_2,ITM01_ITEMS.USE_VARIANT_3,ITM01_ITEMS.USE_VARIANT_4,ITM01_ITEMS.USE_VARIANT_5 ";
			String from =
          "from ITM01_ITEMS,SYS10_TRANSLATIONS,REG02_MEASURE_UNITS ";
			String where =
					"where "+
          "ITM01_ITEMS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "ITM01_ITEMS.COMPANY_CODE_SYS01 ='"+companyCodeSYS10+"' and ";

          if (Boolean.TRUE.equals(pars.getLookupValidationParameters().get(ApplicationConsts.VALIDATE_BARCODE))) {
            where += "ITM01_ITEMS.BAR_CODE = '"+pars.getCode()+"' and ";
          }
          else {
            where += "ITM01_ITEMS.ITEM_CODE = '"+pars.getCode()+"' and ";
          }

      where +=
          "ITM01_ITEMS.ENABLED='Y' and "+
          "ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02=REG02_MEASURE_UNITS.UM_CODE ";

      if (progressiveHIE02!=null)
        where += " and ITM01_ITEMS.PROGRESSIVE_HIE02="+progressiveHIE02;

      if (productsOnly!=null && productsOnly.booleanValue())
        where += " and ITM01_ITEMS.MANUFACTURE_CODE_PRO01 is not null ";

      if (compsOnly!=null && compsOnly.booleanValue())
        where += " and ITM01_ITEMS.MANUFACTURE_CODE_PRO01 is null ";

      if (Boolean.TRUE.equals(pars.getLookupValidationParameters().get(ApplicationConsts.SHOW_ITEMS_WITHOUT_VARIANTS)))
          where +=
            " and ITM01_ITEMS.USE_VARIANT_1='N' "+
            " and ITM01_ITEMS.USE_VARIANT_2='N' "+
            " and ITM01_ITEMS.USE_VARIANT_3='N' "+
            " and ITM01_ITEMS.USE_VARIANT_4='N' "+
            " and ITM01_ITEMS.USE_VARIANT_5='N' ";

			if (Boolean.TRUE.equals(pars.getLookupValidationParameters().get(ApplicationConsts.SHOW_ONLY_MOVABLE_ITEMS)))
					where +=
						" and ITM01_ITEMS.NO_WAREHOUSE_MOV='N' ";

			if (Boolean.TRUE.equals(showOnlyPurchasedItems) && progressiveREG04!=null) {
					select +=
						",DOC01_SELLING.DOC_DATE ";
					from +=
						"INNER JOIN DOC01_SELLING ON "+
						" ITM01_ITEMS.COMPANY_CODE_SYS01=DOC01_SELLING.COMPANY_CODE_SYS01 and "+
						" DOC01_SELLING.DOC_STATE='"+ApplicationConsts.CLOSED+"' AND NOT DOC01_SELLING.DOC_TYPE='"+ApplicationConsts.SALE_ESTIMATE_DOC_TYPE+"' AND "+
						" DOC01_SELLING.ENABLED='Y' AND DOC01_SELLING.PROGRESSIVE_REG04="+progressiveREG04+
						" AND EXISTS (SELECT * FROM DOC02_SELLING_ITEMS where "+
						" DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01=DOC01_SELLING.COMPANY_CODE_SYS01 and "+
						" DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01=DOC01_SELLING.COMPANY_CODE_SYS01 AND "+
						" DOC02_SELLING_ITEMS.DOC_TYPE=DOC01_SELLING.DOC_TYPE AND "+
						" DOC02_SELLING_ITEMS.DOC_YEAR=DOC01_SELLING.DOC_YEAR AND "+
						" DOC02_SELLING_ITEMS.DOC_NUMBER=DOC01_SELLING.DOC_NUMBER AND "+
						" DOC02_SELLING_ITEMS.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE "+
						") ";

					 attribute2dbField.put("docDateDOC01","DOC01_SELLING.DOC_DATE");
			}


      ArrayList values = new ArrayList();
      values.add(serverLanguageId);

      // read from ITM01 table...
      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          select+from+where,
          values,
          attribute2dbField,
          GridItemVO.class,
          "Y",
          "N",
          null,
          new GridParams(),
          true
      );

      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching items list",ex);
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

