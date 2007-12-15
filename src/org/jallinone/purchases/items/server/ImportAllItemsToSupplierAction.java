package org.jallinone.purchases.items.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.purchases.items.java.*;
import java.math.BigDecimal;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to insert all items in PUR02 table.</p>
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
public class ImportAllItemsToSupplierAction implements Action {


  public ImportAllItemsToSupplierAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "importAllItemsToSupplier";
  }

  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,
                                       UserSessionParameters userSessionPars,
                                       HttpServletRequest request,
                                       HttpServletResponse response,
                                       HttpSession userSession,
                                       ServletContext context) {
    Connection conn = null;
    PreparedStatement pstmt = null;
    PreparedStatement pstmt2 = null;
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

      SupplierItemVO vo = (SupplierItemVO)inputPar;

      pstmt = conn.prepareStatement(
        "select ITEM_CODE,PROGRESSIVE_HIE01,MIN_SELLING_QTY_UM_CODE_REG02 from ITM01_ITEMS where "+
        "ENABLED='Y' and COMPANY_CODE_SYS01=? and PROGRESSIVE_HIE02=?"
      );
      pstmt2 = conn.prepareStatement(
        "insert into PUR02_SUPPLIER_ITEMS(COMPANY_CODE_SYS01,ITEM_CODE_ITM01,SUPPLIER_ITEM_CODE,PROGRESSIVE_REG04,"+
        "PROGRESSIVE_HIE02,PROGRESSIVE_HIE01,MIN_PURCHASE_QTY,MULTIPLE_QTY,UM_CODE_REG02,ENABLED) values(?,?,?,?,?,?,?,?,?,?)"
      );

      pstmt.setString(1,vo.getCompanyCodeSys01PUR02());
      pstmt.setBigDecimal(2,vo.getProgressiveHie02PUR02());
      ResultSet rset = pstmt.executeQuery();
      while(rset.next()) {
        pstmt2.setString(1,vo.getCompanyCodeSys01PUR02());
        pstmt2.setString(2,rset.getString(1));
        pstmt2.setString(3,rset.getString(1));
        pstmt2.setBigDecimal(4,vo.getProgressiveReg04PUR02());
        pstmt2.setBigDecimal(5,vo.getProgressiveHie02PUR02());
        pstmt2.setBigDecimal(6,rset.getBigDecimal(2));
        pstmt2.setInt(7,1);
        pstmt2.setInt(8,1);
        pstmt2.setString(9,rset.getString(3));
        pstmt2.setString(10,"Y");
        try {
          pstmt2.executeUpdate();
        }
        catch (SQLException ex4) {
        }
      }
      rset.close();

      Response answer = new VOResponse(Boolean.TRUE);

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
      Logger.error(userSessionPars.getUsername(), this.getClass().getName(),
                   "executeCommand", "Error while inserting price for all items", ex);
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
        pstmt2.close();
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

