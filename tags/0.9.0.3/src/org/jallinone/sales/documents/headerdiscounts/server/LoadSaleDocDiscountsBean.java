package org.jallinone.sales.documents.headerdiscounts.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.sales.documents.headerdiscounts.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.sales.documents.java.SaleDocPK;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Helper class used to fetch customer discounts applied to a sale document from DOC05 table.</p>
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
public class LoadSaleDocDiscountsBean {


  public LoadSaleDocDiscountsBean() {
  }


  /**
   * Retrieve all header discounts defined for a specified sale document.
   * No commit or rollback are executed. No connection is created or released.
   */
  public final Response loadSaleDocDiscounts(Connection conn,GridParams gridParams,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();

    try {

      // fires the GenericEvent.CONNECTION_CREATED event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "LoadSaleDocDiscountsBean.loadSaleDocDiscounts",
        GenericEvent.CONNECTION_CREATED,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        gridParams,
        null
      ));
      String sql =
          "select DOC05_SELLING_DISCOUNTS.COMPANY_CODE_SYS01,DOC05_SELLING_DISCOUNTS.DISCOUNT_CODE_SAL03,"+
          "DOC05_SELLING_DISCOUNTS.MIN_VALUE,DOC05_SELLING_DISCOUNTS.MAX_VALUE,DOC05_SELLING_DISCOUNTS.MIN_PERC,"+
          "DOC05_SELLING_DISCOUNTS.MAX_PERC,DOC05_SELLING_DISCOUNTS.START_DATE,DOC05_SELLING_DISCOUNTS.END_DATE,"+
          "DOC05_SELLING_DISCOUNTS.DISCOUNT_DESCRIPTION,DOC05_SELLING_DISCOUNTS.VALUE,DOC05_SELLING_DISCOUNTS.PERC,"+
          "DOC05_SELLING_DISCOUNTS.DOC_TYPE,DOC05_SELLING_DISCOUNTS.DOC_YEAR,DOC05_SELLING_DISCOUNTS.DOC_NUMBER "+
          "from DOC05_SELLING_DISCOUNTS where "+
          "DOC05_SELLING_DISCOUNTS.COMPANY_CODE_SYS01=? and "+
          "DOC05_SELLING_DISCOUNTS.DOC_TYPE=? and "+
          "DOC05_SELLING_DISCOUNTS.DOC_YEAR=? and "+
          "DOC05_SELLING_DISCOUNTS.DOC_NUMBER=? ";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC05","DOC05_SELLING_DISCOUNTS.COMPANY_CODE_SYS01");
      attribute2dbField.put("discountCodeSal03DOC05","DOC05_SELLING_DISCOUNTS.DISCOUNT_CODE_SAL03");
      attribute2dbField.put("minValueDOC05","DOC05_SELLING_DISCOUNTS.MIN_VALUE");
      attribute2dbField.put("maxValueDOC05","DOC05_SELLING_DISCOUNTS.MAX_VALUE");
      attribute2dbField.put("minPercDOC05","DOC05_SELLING_DISCOUNTS.MIN_PERC");
      attribute2dbField.put("maxPercDOC05","DOC05_SELLING_DISCOUNTS.MAX_PERC");
      attribute2dbField.put("startDateDOC05","DOC05_SELLING_DISCOUNTS.START_DATE");
      attribute2dbField.put("endDateDOC05","DOC05_SELLING_DISCOUNTS.END_DATE");
      attribute2dbField.put("discountDescriptionDOC05","DOC05_SELLING_DISCOUNTS.DISCOUNT_DESCRIPTION");
      attribute2dbField.put("valueDOC05","DOC05_SELLING_DISCOUNTS.VALUE");
      attribute2dbField.put("percDOC05","DOC05_SELLING_DISCOUNTS.PERC");
      attribute2dbField.put("docTypeDOC05","DOC05_SELLING_DISCOUNTS.DOC_TYPE");
      attribute2dbField.put("docYearDOC05","DOC05_SELLING_DISCOUNTS.DOC_YEAR");
      attribute2dbField.put("docNumberDOC05","DOC05_SELLING_DISCOUNTS.DOC_NUMBER");

      ArrayList values = new ArrayList();
      SaleDocPK pk = (SaleDocPK)gridParams.getOtherGridParams().get(ApplicationConsts.SALE_DOC_PK);
      values.add( pk.getCompanyCodeSys01DOC01() );
      values.add( pk.getDocTypeDOC01() );
      values.add( pk.getDocYearDOC01() );
      values.add( pk.getDocNumberDOC01() );

      // read from DOC05 table...
      Response answer = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          SaleDocDiscountVO.class,
          "Y",
          "N",
          context,
          gridParams,
          true
      );

      // fires the GenericEvent.BEFORE_COMMIT event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "LoadSaleDocDiscountsBean.loadSaleDocDiscounts",
        GenericEvent.BEFORE_COMMIT,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        gridParams,
        answer
      ));

      return answer;

    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"loadSaleDocDiscounts","Error while fetching customer discounts list",ex);
      return new ErrorResponse(ex.getMessage());
    }
    finally {
    }

  }



}
