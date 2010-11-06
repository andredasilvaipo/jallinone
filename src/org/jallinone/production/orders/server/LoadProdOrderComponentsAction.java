package org.jallinone.production.orders.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.production.orders.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch production order components from DOC24 table.</p>
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
public class LoadProdOrderComponentsAction implements Action {


  public LoadProdOrderComponentsAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadProdOrderComponents";
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


      GridParams pars = (GridParams)inputPar;
      ProdOrderPK pk = (ProdOrderPK)pars.getOtherGridParams().get(ApplicationConsts.PROD_ORDER_PK);

      String sql =
          "select DOC24_PRODUCTION_COMPONENTS.COMPANY_CODE_SYS01,DOC24_PRODUCTION_COMPONENTS.DOC_YEAR,"+
          "DOC24_PRODUCTION_COMPONENTS.DOC_NUMBER,DOC24_PRODUCTION_COMPONENTS.ITEM_CODE_ITM01,SYS10_TRANSLATIONS.DESCRIPTION,"+
          "DOC24_PRODUCTION_COMPONENTS.QTY,DOC24_PRODUCTION_COMPONENTS.PROGRESSIVE_HIE01,B.DESCRIPTION,ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02 "+
          " from DOC24_PRODUCTION_COMPONENTS,ITM01_ITEMS,SYS10_TRANSLATIONS,SYS10_TRANSLATIONS B "+
          " where "+
          "DOC24_PRODUCTION_COMPONENTS.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
          "DOC24_PRODUCTION_COMPONENTS.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and "+
          "ITM01_ITEMS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "DOC24_PRODUCTION_COMPONENTS.COMPANY_CODE_SYS01=? and "+
          "DOC24_PRODUCTION_COMPONENTS.DOC_YEAR=? and "+
          "DOC24_PRODUCTION_COMPONENTS.DOC_NUMBER=? and "+
          "DOC24_PRODUCTION_COMPONENTS.PROGRESSIVE_HIE01=B.PROGRESSIVE and "+
          "B.LANGUAGE_CODE=? ";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC24","DOC24_PRODUCTION_COMPONENTS.COMPANY_CODE_SYS01");
      attribute2dbField.put("docYearDOC24","DOC24_PRODUCTION_COMPONENTS.DOC_YEAR");
      attribute2dbField.put("docNumberDOC24","DOC24_PRODUCTION_COMPONENTS.DOC_NUMBER");
      attribute2dbField.put("itemCodeItm01DOC24","DOC24_PRODUCTION_COMPONENTS.ITEM_CODE_ITM01");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("qtyDOC24","DOC24_PRODUCTION_COMPONENTS.QTY");
      attribute2dbField.put("progressiveHie01DOC24","DOC24_PRODUCTION_COMPONENTS.PROGRESSIVE_HIE01");
      attribute2dbField.put("locationDescriptionSYS10","B.DESCRIPTION");
      attribute2dbField.put("minSellingQtyUmCodeReg02ITM01","ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(pk.getCompanyCodeSys01DOC22());
      values.add(pk.getDocYearDOC22());
      values.add(pk.getDocNumberDOC22());
      values.add(serverLanguageId);

      // read from DOC24 table...
      Response answer = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          ProdOrderComponentVO.class,
          "Y",
          "N",
          context,
          pars,
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching production order components",ex);
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
