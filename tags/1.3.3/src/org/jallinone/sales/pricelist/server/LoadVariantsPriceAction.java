package org.jallinone.sales.pricelist.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.sales.pricelist.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.commons.server.CustomizeQueryUtil;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.items.java.DetailItemVO;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;
import org.jallinone.variants.java.VariantsMatrixVO;
import org.openswing.swing.customvo.java.CustomValueObject;
import org.jallinone.variants.java.VariantsMatrixRowVO;
import org.jallinone.variants.java.VariantsMatrixColumnVO;
import org.jallinone.items.java.ItemPK;
import org.jallinone.items.java.VariantBarcodeVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch a price, if any, from SAL11 table for a specific barcode and pricelist code.</p>
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
public class LoadVariantsPriceAction implements Action {


  public LoadVariantsPriceAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadVariantsPrice";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();

    Connection conn = null;
    try {
      conn = ConnectionManager.getConnection(context);

      // fires the GenericEvent.CONNECTION_CREATED event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        getRequestName(),
        GenericEvent.CONNECTION_CREATED,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        inputPar,
        null
      ));


      Object[] params = (Object[])inputPar;
      VariantBarcodeVO barcodeVO = (VariantBarcodeVO)params[0];
      String priceListCode = (String)params[1];

      String sql =
            "select SAL11_VARIANTS_PRICES.COMPANY_CODE_SYS01,SAL11_VARIANTS_PRICES.PRICELIST_CODE_SAL01,"+
            "SAL11_VARIANTS_PRICES.ITEM_CODE_ITM01,SAL11_VARIANTS_PRICES.VALUE,SAL11_VARIANTS_PRICES.START_DATE,SAL11_VARIANTS_PRICES.END_DATE,"+
            "A.DESCRIPTION,ITM01_ITEMS.PROGRESSIVE_HIE02,"+
            "SAL11_VARIANTS_PRICES.VARIANT_TYPE_ITM06,SAL11_VARIANTS_PRICES.VARIANT_CODE_ITM11,"+
            "SAL11_VARIANTS_PRICES.VARIANT_TYPE_ITM07,SAL11_VARIANTS_PRICES.VARIANT_CODE_ITM12,"+
            "SAL11_VARIANTS_PRICES.VARIANT_TYPE_ITM08,SAL11_VARIANTS_PRICES.VARIANT_CODE_ITM13,"+
            "SAL11_VARIANTS_PRICES.VARIANT_TYPE_ITM09,SAL11_VARIANTS_PRICES.VARIANT_CODE_ITM14,"+
            "SAL11_VARIANTS_PRICES.VARIANT_TYPE_ITM10,SAL11_VARIANTS_PRICES.VARIANT_CODE_ITM15 "+
            " from SAL11_VARIANTS_PRICES,SYS10_TRANSLATIONS A,ITM01_ITEMS where "+
            "SAL11_VARIANTS_PRICES.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
            "SAL11_VARIANTS_PRICES.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and "+
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
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          VariantsPriceVO.class,
          "Y",
          "N",
          context,
          new GridParams(),
          true
      );

     return res;
    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching a price for the specified barcode",ex);
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        ConnectionManager.releaseConnection(conn, context);
      }
      catch (Exception ex1) {
      }
    }

  }



}
