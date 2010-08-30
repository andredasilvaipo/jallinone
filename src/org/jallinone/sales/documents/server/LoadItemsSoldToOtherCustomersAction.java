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
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.hierarchies.java.HierarchyLevelVO;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch items sold to other customers which have bought the same items.</p>
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
public class LoadItemsSoldToOtherCustomersAction implements Action {


  public LoadItemsSoldToOtherCustomersAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadItemsSoldToOtherCustomers";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();

    PreparedStatement pstmt = null;
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

      Object[] objs = (Object[])inputPar;
      String companyCodeSys01 = objs[0].toString();
      ArrayList itemsList = (ArrayList)objs[1];
      String items = "";
      for(int i=0;i<itemsList.size();i++)
        items += "'"+itemsList.get(i).toString()+"',";
      items = items.substring(0,items.length()-1);

      String sql =
          "SELECT COUNT(SYS10_TRANSLATIONS.DESCRIPTION) AS CUSTOMERS_NR,SYS10_TRANSLATIONS.DESCRIPTION FROM DOC02_SELLING_ITEMS,ITM01_ITEMS,SYS10_TRANSLATIONS "+
          "WHERE "+
          "DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01=? AND "+
          "DOC02_SELLING_ITEMS.DOC_TYPE=? AND "+
          "DOC02_SELLING_ITEMS.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 AND "+
          "DOC02_SELLING_ITEMS.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE AND "+
          "ITM01_ITEMS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE AND "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? AND "+
          "DOC02_SELLING_ITEMS.DOC_NUMBER IN ( "+
          "SELECT D.DOC_NUMBER FROM DOC02_SELLING_ITEMS D WHERE "+
          "D.COMPANY_CODE_SYS01=? AND "+
          "D.DOC_TYPE=? AND "+
          "D.ITEM_CODE_ITM01 IN ("+items+") "+
          ") "+
          "AND NOT DOC02_SELLING_ITEMS.ITEM_CODE_ITM01 IN ("+items+") "+
          "GROUP BY SYS10_TRANSLATIONS.DESCRIPTION "+
          "ORDER BY COUNT(SYS10_TRANSLATIONS.DESCRIPTION) DESC ";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("itemDescriptionSY10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("customersNr","CUSTOMERS_NR");

      ArrayList values = new ArrayList();
      values.add(companyCodeSys01);
      values.add(ApplicationConsts.SALE_DESK_DOC_TYPE);
      values.add(serverLanguageId);
      values.add(companyCodeSys01);
      values.add(ApplicationConsts.SALE_DESK_DOC_TYPE);

      // read from DOC01 table...
      Response answer = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          ItemSoldToOtherCustomersVO.class,
          "Y",
          "N",
          context,
          new GridParams(),
          50,
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching sale documents list",ex);
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        pstmt.close();
      }
      catch (Exception ex2) {
      }
      try {
        ConnectionManager.releaseConnection(conn, context);
      }
      catch (Exception ex1) {
      }
    }

  }



}
