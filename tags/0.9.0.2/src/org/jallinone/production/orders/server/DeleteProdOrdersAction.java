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
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to (phisically) delete existing production orders.</p>
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
public class DeleteProdOrdersAction implements Action {


  public DeleteProdOrdersAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "deleteProdOrders";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    Connection conn = null;
    PreparedStatement pstmt = null;
    PreparedStatement pstmt23 = null;
    PreparedStatement pstmt24 = null;
    PreparedStatement pstmt25 = null;
    PreparedStatement pstmt26 = null;
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


      ArrayList list = (ArrayList)inputPar;
      ProdOrderPK pk = null;

      pstmt = conn.prepareStatement(
          "delete from DOC22_PRODUCTION_ORDER where COMPANY_CODE_SYS01=? and DOC_YEAR=? and DOC_NUMBER=?"
      );
      pstmt23 = conn.prepareStatement(
          "delete from DOC23_PRODUCTION_PRODUCTS where COMPANY_CODE_SYS01=? and DOC_YEAR=? and DOC_NUMBER=?"
      );
      pstmt24 = conn.prepareStatement(
          "delete from DOC24_PRODUCTION_COMPONENTS where COMPANY_CODE_SYS01=? and DOC_YEAR=? and DOC_NUMBER=?"
      );
      pstmt25 = conn.prepareStatement(
          "delete from DOC25_PRODUCTION_PROD_COMPS where COMPANY_CODE_SYS01=? and DOC_YEAR=? and DOC_NUMBER=?"
      );
      pstmt26 = conn.prepareStatement(
          "delete from DOC26_PRODUCTION_OPERATIONS where COMPANY_CODE_SYS01=? and DOC_YEAR=? and DOC_NUMBER=?"
      );

      for(int i=0;i<list.size();i++) {
        pk = (ProdOrderPK)list.get(i);

        // phisically delete the record in DOC23...
        pstmt23.setString(1,pk.getCompanyCodeSys01DOC22());
        pstmt23.setBigDecimal(2,pk.getDocYearDOC22());
        pstmt23.setBigDecimal(3,pk.getDocNumberDOC22());
        pstmt23.execute();

        // phisically delete the record in DOC24...
        pstmt24.setString(1,pk.getCompanyCodeSys01DOC22());
        pstmt24.setBigDecimal(2,pk.getDocYearDOC22());
        pstmt24.setBigDecimal(3,pk.getDocNumberDOC22());
        pstmt24.execute();

        // phisically delete the record in DOC25...
        pstmt25.setString(1,pk.getCompanyCodeSys01DOC22());
        pstmt25.setBigDecimal(2,pk.getDocYearDOC22());
        pstmt25.setBigDecimal(3,pk.getDocNumberDOC22());
        pstmt25.execute();

        // phisically delete the record in DOC26...
        pstmt26.setString(1,pk.getCompanyCodeSys01DOC22());
        pstmt26.setBigDecimal(2,pk.getDocYearDOC22());
        pstmt26.setBigDecimal(3,pk.getDocNumberDOC22());
        pstmt26.execute();

        // phisically delete the record in DOC22...
        pstmt.setString(1,pk.getCompanyCodeSys01DOC22());
        pstmt.setBigDecimal(2,pk.getDocYearDOC22());
        pstmt.setBigDecimal(3,pk.getDocNumberDOC22());
        pstmt.execute();
      }


      Response answer = new VOResponse(new Boolean(true));

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

      conn.commit();

      // fires the GenericEvent.AFTER_COMMIT event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        getRequestName(),
        GenericEvent.AFTER_COMMIT,
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while deleting existing production orders",ex);
      try {
        conn.rollback();
      }
      catch (Exception ex3) {
      }
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        pstmt.close();
      }
      catch (Exception ex2) {
      }
      try {
        pstmt23.close();
      }
      catch (Exception ex2) {
      }
      try {
        pstmt24.close();
      }
      catch (Exception ex2) {
      }
      try {
        pstmt25.close();
      }
      catch (Exception ex2) {
      }
      try {
        pstmt26.close();
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
