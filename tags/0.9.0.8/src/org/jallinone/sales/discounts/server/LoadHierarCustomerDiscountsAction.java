package org.jallinone.sales.discounts.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.warehouse.java.WarehouseVO;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.sales.customers.java.*;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.subjects.java.Subject;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.items.java.ItemPK;
import org.jallinone.sales.discounts.java.*;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch hierarchy customer discounts from SAL03/SAL10 tables.</p>
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
public class LoadHierarCustomerDiscountsAction implements Action {


  public LoadHierarCustomerDiscountsAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadHierarCustomerDiscounts";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();
    Connection conn = null;
    Statement stmt = null;
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
      BigDecimal progressiveHIE01 = (BigDecimal)gridParams.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_HIE01);
      BigDecimal progressiveHIE02 = (BigDecimal)gridParams.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_HIE02);
      BigDecimal rootProgressiveHIE01 = (BigDecimal)gridParams.getOtherGridParams().get(ApplicationConsts.ROOT_PROGRESSIVE_HIE01);

      // retrieve COMPANY_CODE from progressiveHIE02...
      stmt = conn.createStatement();
      ResultSet rset = stmt.executeQuery(
          "select COMPANY_CODE_SYS01 from REG08_SUBJECT_HIERARCHIES where PROGRESSIVE_HIE02="+progressiveHIE02
      );
      String companyCodeSYS01 = null;
      if(rset.next())
        companyCodeSYS01 = rset.getString(1);
      else {
        rset.close();
        return new ErrorResponse("Customer hierarchy not found.");
      }
      rset.close();

      // retrieve discounts...
      rset = stmt.executeQuery(
          "select DISCOUNT_CODE_SAL03 from SAL10_SUBJECT_HIERAR_DISCOUNTS where "+
          "COMPANY_CODE_SYS01='"+companyCodeSYS01+"' and PROGRESSIVE_HIE01="+progressiveHIE01
      );
      ArrayList discountCodes = new ArrayList();
      while(rset.next()) {
        discountCodes.add( rset.getString(1) );
      }
      rset.close();

      Response res = DiscountBean.getDiscountsList(
          conn,
          companyCodeSYS01,
          discountCodes,
          serverLanguageId,
          gridParams,
          userSessionPars,
          context,
          HierarCustomerDiscountVO.class
      );
      if (!res.isError()) {
        ArrayList rows = ((VOListResponse)res).getRows();
        for(int i=0;i<rows.size();i++) {
          ((HierarCustomerDiscountVO)rows.get(i)).setProgressiveHie01SAL10(progressiveHIE01);
        }

      }


      Response answer = res;

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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching hierarchy customer discounts list",ex);
      try {
        conn.rollback();
      }
      catch (Exception ex3) {
      }
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        stmt.close();
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
