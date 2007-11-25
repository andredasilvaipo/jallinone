package org.jallinone.sales.documents.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.sales.documents.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.commons.server.CustomizeQueryUtil;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.system.java.ApplicationParametersVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Helper class used to fetch a specific sale document row from DOC02 table.</p>
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
public class LoadSaleDocRowBean {


  public LoadSaleDocRowBean() {
  }


  /**
   * Load a specific item row.
   * No commit or rollback are executed; no connection is created or released.-
   */
  public final Response loadSaleDocRow(Connection conn,SaleDocRowPK pk,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();
    Statement stmt = null;
    PreparedStatement pstmt = null;
    try {

      // fires the GenericEvent.CONNECTION_CREATED event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "LoadSaleDocRowBean.loadSaleDocRow",
        GenericEvent.CONNECTION_CREATED,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        pk,
        null
      ));
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

      attribute2dbField.put("vatDescriptionDOC02","DOC02_SELLING_ITEMS.VAT_DESCRIPTION");
      attribute2dbField.put("startDateSal02DOC02","DOC02_SELLING_ITEMS.START_DATE_SAL02");
      attribute2dbField.put("endDateSal02DOC02","DOC02_SELLING_ITEMS.END_DATE_SAL02");
      attribute2dbField.put("minSellingQtyUmCodeReg02DOC02","DOC02_SELLING_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02");
      attribute2dbField.put("decimalsReg02DOC02","DOC02_SELLING_ITEMS.DECIMALS_REG02");
      attribute2dbField.put("minSellingQtyItm01DOC02","DOC02_SELLING_ITEMS.MIN_SELLING_QTY_ITM01");
      attribute2dbField.put("valueReg01DOC02","DOC02_SELLING_ITEMS.VALUE_REG01");
      attribute2dbField.put("deductibleReg01DOC02","DOC02_SELLING_ITEMS.DEDUCTIBLE_REG01");
      attribute2dbField.put("taxableIncomeDOC02","DOC02_SELLING_ITEMS.TAXABLE_INCOME");
      attribute2dbField.put("progressiveHie02DOC02","DOC02_SELLING_ITEMS.PROGRESSIVE_HIE02");
      attribute2dbField.put("progressiveHie01ITM01","ITM01_ITEMS.PROGRESSIVE_HIE01");
      attribute2dbField.put("deliveryDateDOC02","DOC02_SELLING_ITEMS.DELIVERY_DATE");
      attribute2dbField.put("outQtyDOC02","DOC02_SELLING_ITEMS.OUT_QTY");
      attribute2dbField.put("currencyCodeReg03DOC01","DOC01_SELLING.CURRENCY_CODE_REG03");
      attribute2dbField.put("progressiveHie01DOC02","DOC02_SELLING_ITEMS.PROGRESSIVE_HIE01");
      attribute2dbField.put("discountValueDOC02","DOC02_SELLING_ITEMS.DISCOUNT_VALUE");
      attribute2dbField.put("discountPercDOC02","DOC02_SELLING_ITEMS.DISCOUNT_PERC");
      attribute2dbField.put("invoiceQtyDOC02","DOC02_SELLING_ITEMS.INVOICE_QTY");

      String baseSQL =
          "select DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01,DOC02_SELLING_ITEMS.DOC_TYPE,DOC02_SELLING_ITEMS.DOC_YEAR,DOC02_SELLING_ITEMS.DOC_NUMBER,DOC02_SELLING_ITEMS.ROW_NUMBER,"+
          "DOC02_SELLING_ITEMS.ITEM_CODE_ITM01,DOC02_SELLING_ITEMS.VAT_CODE_ITM01,DOC02_SELLING_ITEMS.VALUE_SAL02,"+
          "DOC02_SELLING_ITEMS.VALUE,DOC02_SELLING_ITEMS.QTY,DOC02_SELLING_ITEMS.TOTAL_DISCOUNT,ITM01_ITEMS.PROGRESSIVE_HIE01,"+
          "SYS10_TRANSLATIONS.DESCRIPTION,DOC02_SELLING_ITEMS.VAT_VALUE,DOC02_SELLING_ITEMS.VAT_DESCRIPTION,DOC02_SELLING_ITEMS.START_DATE_SAL02,DOC02_SELLING_ITEMS.END_DATE_SAL02,"+
          "DOC02_SELLING_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02,DOC02_SELLING_ITEMS.DECIMALS_REG02,DOC02_SELLING_ITEMS.MIN_SELLING_QTY_ITM01,"+
          "DOC02_SELLING_ITEMS.VALUE_REG01,DOC02_SELLING_ITEMS.DEDUCTIBLE_REG01,DOC02_SELLING_ITEMS.TAXABLE_INCOME,DOC02_SELLING_ITEMS.PROGRESSIVE_HIE02,DOC02_SELLING_ITEMS.DELIVERY_DATE,"+
          "DOC02_SELLING_ITEMS.OUT_QTY,DOC01_SELLING.CURRENCY_CODE_REG03,DOC02_SELLING_ITEMS.PROGRESSIVE_HIE01, "+
          "DOC02_SELLING_ITEMS.DISCOUNT_VALUE,DOC02_SELLING_ITEMS.DISCOUNT_PERC,DOC02_SELLING_ITEMS.INVOICE_QTY "+
          " from DOC02_SELLING_ITEMS,ITM01_ITEMS,SYS10_TRANSLATIONS,DOC01_SELLING where "+
          "DOC01_SELLING.COMPANY_CODE_SYS01=DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01 and "+
          "DOC01_SELLING.DOC_TYPE=DOC02_SELLING_ITEMS.DOC_TYPE and "+
          "DOC01_SELLING.DOC_YEAR=DOC02_SELLING_ITEMS.DOC_YEAR and "+
          "DOC01_SELLING.DOC_NUMBER=DOC02_SELLING_ITEMS.DOC_NUMBER and "+
          "DOC02_SELLING_ITEMS.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and "+
          "DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
          "ITM01_ITEMS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01=? and "+
          "DOC02_SELLING_ITEMS.DOC_TYPE=? and "+
          "DOC02_SELLING_ITEMS.DOC_YEAR=? and "+
          "DOC02_SELLING_ITEMS.DOC_NUMBER=? and "+
          "DOC02_SELLING_ITEMS.ITEM_CODE_ITM01=? ";

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(pk.getCompanyCodeSys01DOC02());
      values.add(pk.getDocTypeDOC02());
      values.add(pk.getDocYearDOC02());
      values.add(pk.getDocNumberDOC02());
      values.add(pk.getItemCodeItm01DOC02());

      // read from DOC02 table...
      Response res = QueryUtil.getQuery(
          conn,
          userSessionPars,
          baseSQL,
          values,
          attribute2dbField,
          DetailSaleDocRowVO.class,
          "Y",
          "N",
          context,
          true
      );


      if (!res.isError()) {
        DetailSaleDocRowVO vo = (DetailSaleDocRowVO)((VOResponse)res).getVo();
        ResultSet rset = null;

        // retrieve position description, if defined...
        if (vo.getProgressiveHie01DOC02()!=null) {
          pstmt = conn.prepareStatement("select DESCRIPTION from SYS10_TRANSLATIONS where PROGRESSIVE=? and LANGUAGE_CODE=?");
          pstmt.setBigDecimal(1,vo.getProgressiveHie01DOC02());
          pstmt.setString(2,serverLanguageId);
          try {
            rset = pstmt.executeQuery();
            while(rset.next()) {
              vo.setPositionDescriptionSYS10( rset.getString(1) );
            }
          }
          catch (Exception ex3) {
            throw ex3;
          }
          finally {
            rset.close();
          }
          pstmt.close();
        }

        // retrieve serial numbers...
        ArrayList serialNums = null;
        ArrayList barCodes = null;

        pstmt = conn.prepareStatement(
          "select SERIAL_NUMBER,BAR_CODE from DOC18_SELLING_SERIAL_NUMBERS where "+
          "COMPANY_CODE_SYS01=? and "+
          "DOC_TYPE=? and "+
          "DOC_YEAR=? and "+
          "DOC_NUMBER=? and "+
          "ITEM_CODE_ITM01=?"
         );
         serialNums = new ArrayList();
         barCodes = new ArrayList();
         vo.setSerialNumbers(serialNums);
         vo.setBarCodes(barCodes);
         pstmt.setString(1,vo.getCompanyCodeSys01DOC02());
         pstmt.setString(2,vo.getDocTypeDOC02());
         pstmt.setBigDecimal(3,vo.getDocYearDOC02());
         pstmt.setBigDecimal(4,vo.getDocNumberDOC02());
         pstmt.setString(5,vo.getItemCodeItm01DOC02());
         try {
           rset = pstmt.executeQuery();
           while(rset.next()) {
             serialNums.add(rset.getString(1));
             barCodes.add(rset.getString(2));
           }
         }
         catch (Exception ex3) {
           throw ex3;
         }
         finally {
           rset.close();
        }
      }

      Response answer = res;

      // fires the GenericEvent.BEFORE_COMMIT event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "LoadSaleDocRowBean.loadSaleDocRow",
        GenericEvent.BEFORE_COMMIT,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        pk,
        answer
      ));

      return answer;
    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"loadSaleDocRow","Error while fetching an existing sale document row",ex);
      return new ErrorResponse(ex.getMessage());
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
      catch (Exception ex4) {
      }
    }

  }



}
