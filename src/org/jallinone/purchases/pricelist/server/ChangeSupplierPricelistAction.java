package org.jallinone.purchases.pricelist.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.purchases.pricelist.java.*;
import org.openswing.swing.server.QueryUtil;
import org.jallinone.commons.server.CustomizeQueryUtil;
import java.math.BigDecimal;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to update supplier item prices records in PUR04, according to start/end dates and delta value/percentage.</p>
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
public class ChangeSupplierPricelistAction implements Action {


  public ChangeSupplierPricelistAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "changeSupplierPricelist";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    Connection conn = null;
    PreparedStatement pstmt = null;
    try {
      String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();
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

      SupplierPricelistChanges changes = (SupplierPricelistChanges)inputPar;

      if (changes.getStartDate()!=null && changes.getEndDate()!=null) {
        String sql = "update PUR04_SUPPLIER_PRICES set START_DATE=?,END_DATE=? where COMPANY_CODE_SYS01=? and PRICELIST_CODE_PUR03=? and PROGRESSIVE_REG04=?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setDate(1,changes.getStartDate());
        pstmt.setDate(2,changes.getEndDate());
        pstmt.setString(3,changes.getCompanyCodeSys01PUR04());
        pstmt.setString(4,changes.getPricelistCodePur03PUR04());
        pstmt.setBigDecimal(5,changes.getProgressiveReg04PUR04());
        pstmt.execute();
        pstmt.close();
      }

      if (changes.getPercentage()!=null && !changes.isTruncateDecimals()) {
        String sql = "update PUR04_SUPPLIER_PRICES set VALUE=VALUE+VALUE*"+changes.getPercentage().doubleValue()+"/100 where COMPANY_CODE_SYS01=? and PRICELIST_CODE_PUR03=? and PROGRESSIVE_REG04=?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1,changes.getCompanyCodeSys01PUR04());
        pstmt.setString(2,changes.getPricelistCodePur03PUR04());
        pstmt.setBigDecimal(3,changes.getProgressiveReg04PUR04());
        pstmt.execute();
        pstmt.close();
      }

      if (changes.getPercentage()!=null && changes.isTruncateDecimals()) {
        String sql = "select ITEM_CODE_ITM01,VALUE from PUR04_SUPPLIER_PRICES where COMPANY_CODE_SYS01=? and PRICELIST_CODE_PUR03=? ";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1,changes.getCompanyCodeSys01PUR04());
        pstmt.setString(2,changes.getPricelistCodePur03PUR04());
        ResultSet rset = pstmt.executeQuery();
        PreparedStatement pstmt2 = conn.prepareStatement("update PUR04_SUPPLIER_PRICES set VALUE=? where COMPANY_CODE_SYS01=? and PRICELIST_CODE_PUR03=? and ITEM_CODE_ITM01=? and PROGRESSIVE_REG04=?");
        while(rset.next()) {
          pstmt2.setInt(1,(int)(rset.getDouble(2)+rset.getDouble(2)*changes.getPercentage().doubleValue()/100));
          pstmt2.setString(2,changes.getCompanyCodeSys01PUR04());
          pstmt2.setString(3,changes.getPricelistCodePur03PUR04());
          pstmt2.setString(4,rset.getString(1));
          pstmt2.setBigDecimal(5,changes.getProgressiveReg04PUR04());
          pstmt2.execute();
        }
        rset.close();
        pstmt.close();
        pstmt2.close();
      }

      if (changes.getDeltaValue()!=null) {
        String sql = "update PUR04_SUPPLIER_PRICES set VALUE=VALUE+"+changes.getDeltaValue().doubleValue()+" where COMPANY_CODE_SYS01=? and PRICELIST_CODE_PUR03=? and PROGRESSIVE_REG04=?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1,changes.getCompanyCodeSys01PUR04());
        pstmt.setString(2,changes.getPricelistCodePur03PUR04());
        pstmt.setBigDecimal(3,changes.getProgressiveReg04PUR04());
        pstmt.execute();
        pstmt.close();
      }

      Response answer =  new VOResponse(Boolean.TRUE);

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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while updating existing supplier item prices",ex);
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
        ConnectionManager.releaseConnection(conn, context);
      }
      catch (Exception ex1) {
      }
    }

  }



}
