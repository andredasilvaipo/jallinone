package org.jallinone.sales.documents.itemdiscounts.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.sales.documents.itemdiscounts.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.sales.documents.java.SaleDocRowPK;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch item discounts applied to a sale document row from DOC04 table.</p>
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
public class LoadSaleDocRowDiscountsAction implements Action {


  public LoadSaleDocRowDiscountsAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadSaleDocRowDiscounts";
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

      GridParams gridParams = (GridParams)inputPar;

      String sql =
          "select DOC04_SELLING_ITEM_DISCOUNTS.COMPANY_CODE_SYS01,DOC04_SELLING_ITEM_DISCOUNTS.DISCOUNT_CODE_SAL03,"+
          "DOC04_SELLING_ITEM_DISCOUNTS.MIN_VALUE,DOC04_SELLING_ITEM_DISCOUNTS.MAX_VALUE,DOC04_SELLING_ITEM_DISCOUNTS.MIN_PERC,"+
          "DOC04_SELLING_ITEM_DISCOUNTS.MAX_PERC,DOC04_SELLING_ITEM_DISCOUNTS.START_DATE,DOC04_SELLING_ITEM_DISCOUNTS.END_DATE,"+
          "DOC04_SELLING_ITEM_DISCOUNTS.DISCOUNT_DESCRIPTION,DOC04_SELLING_ITEM_DISCOUNTS.VALUE,DOC04_SELLING_ITEM_DISCOUNTS.PERC,"+
          "DOC04_SELLING_ITEM_DISCOUNTS.DOC_TYPE,DOC04_SELLING_ITEM_DISCOUNTS.DOC_YEAR,DOC04_SELLING_ITEM_DISCOUNTS.DOC_NUMBER,"+
          "DOC04_SELLING_ITEM_DISCOUNTS.ITEM_CODE_ITM01,DOC04_SELLING_ITEM_DISCOUNTS.MIN_QTY,DOC04_SELLING_ITEM_DISCOUNTS.MULTIPLE_QTY "+
          "from DOC04_SELLING_ITEM_DISCOUNTS where "+
          "DOC04_SELLING_ITEM_DISCOUNTS.COMPANY_CODE_SYS01=? and "+
          "DOC04_SELLING_ITEM_DISCOUNTS.DOC_TYPE=? and "+
          "DOC04_SELLING_ITEM_DISCOUNTS.DOC_YEAR=? and "+
          "DOC04_SELLING_ITEM_DISCOUNTS.DOC_NUMBER=? and "+
          "DOC04_SELLING_ITEM_DISCOUNTS.ITEM_CODE_ITM01=? ";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC04","DOC04_SELLING_ITEM_DISCOUNTS.COMPANY_CODE_SYS01");
      attribute2dbField.put("discountCodeSal03DOC04","DOC04_SELLING_ITEM_DISCOUNTS.DISCOUNT_CODE_SAL03");
      attribute2dbField.put("minValueDOC04","DOC04_SELLING_ITEM_DISCOUNTS.MIN_VALUE");
      attribute2dbField.put("maxValueDOC04","DOC04_SELLING_ITEM_DISCOUNTS.MAX_VALUE");
      attribute2dbField.put("minPercDOC04","DOC04_SELLING_ITEM_DISCOUNTS.MIN_PERC");
      attribute2dbField.put("maxPercDOC04","DOC04_SELLING_ITEM_DISCOUNTS.MAX_PERC");
      attribute2dbField.put("startDateDOC04","DOC04_SELLING_ITEM_DISCOUNTS.START_DATE");
      attribute2dbField.put("endDateDOC04","DOC04_SELLING_ITEM_DISCOUNTS.END_DATE");
      attribute2dbField.put("discountDescriptionDOC04","DOC04_SELLING_ITEM_DISCOUNTS.DISCOUNT_DESCRIPTION");
      attribute2dbField.put("valueDOC04","DOC04_SELLING_ITEM_DISCOUNTS.VALUE");
      attribute2dbField.put("percDOC04","DOC04_SELLING_ITEM_DISCOUNTS.PERC");
      attribute2dbField.put("docTypeDOC04","DOC04_SELLING_ITEM_DISCOUNTS.DOC_TYPE");
      attribute2dbField.put("docYearDOC04","DOC04_SELLING_ITEM_DISCOUNTS.DOC_YEAR");
      attribute2dbField.put("docNumberDOC04","DOC04_SELLING_ITEM_DISCOUNTS.DOC_NUMBER");
      attribute2dbField.put("itemCodeItm01DOC04","DOC04_SELLING_ITEM_DISCOUNTS.ITEM_CODE_ITM01");
      attribute2dbField.put("minQtyDOC04","DOC04_SELLING_ITEM_DISCOUNTS.MIN_QTY");
      attribute2dbField.put("multipleQtyDOC04","DOC04_SELLING_ITEM_DISCOUNTS.MULTIPLE_QTY");

      ArrayList values = new ArrayList();
      SaleDocRowPK pk = (SaleDocRowPK)gridParams.getOtherGridParams().get(ApplicationConsts.SALE_DOC_ROW_PK);
      values.add( pk.getCompanyCodeSys01DOC02() );
      values.add( pk.getDocTypeDOC02() );
      values.add( pk.getDocYearDOC02() );
      values.add( pk.getDocNumberDOC02() );
      values.add( pk.getItemCodeItm01DOC02() );

      // read from DOC04 table...
      Response answer = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          SaleItemDiscountVO.class,
          "Y",
          "N",
          context,
          gridParams,
          true
      );


      // fires the GenericEvent.BEFORE_COMMIT event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        getRequestName(),
        GenericEvent.BEFORE_COMMIT,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        inputPar,
        answer
      ));
      return answer;

    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching item discounts list",ex);
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
