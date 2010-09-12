package org.jallinone.registers.currency.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.registers.currency.java.*;
import org.openswing.swing.server.QueryUtil;
import java.math.BigDecimal;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to update existing currency conversions.</p>
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
public class UpdateCurrencyConvsAction implements Action {


  public UpdateCurrencyConvsAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "updateCurrencyConvs";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
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
      stmt = conn.createStatement();

      ArrayList oldVOs = ((ArrayList[])inputPar)[0];
      ArrayList newVOs = ((ArrayList[])inputPar)[1];
      CurrencyConvVO oldVO = null;
      CurrencyConvVO newVO = null;
      Response res = null;

      String sql = null;
      BigDecimal conv = null;
      BigDecimal invConv = null;
      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (CurrencyConvVO)oldVOs.get(i);
        newVO = (CurrencyConvVO)newVOs.get(i);
        conv = null;
        invConv = null;
        if (newVO.getValueREG06()!=null) {
          conv = newVO.getValueREG06().setScale(5,BigDecimal.ROUND_HALF_UP);
          invConv = new BigDecimal(1d/newVO.getValueREG06().doubleValue()).setScale(5,BigDecimal.ROUND_HALF_UP);
        }

        sql =
            "update REG06_CURRENCY_CONV set VALUE="+
            (newVO.getValueREG06()==null?"null":conv.toString())+" where "+
            "CURRENCY_CODE_REG03='"+newVO.getCurrencyCodeReg03REG06()+"' and "+
            "CURRENCY_CODE2_REG03='"+newVO.getCurrencyCode2Reg03REG06()+"' and "+
            "VALUE"+(oldVO.getValueREG06()==null?" is null":"="+oldVO.getValueREG06());


        stmt.execute(sql);

        sql =
            "update REG06_CURRENCY_CONV set VALUE="+
            (newVO.getValueREG06()==null?"null":invConv.toString())+" where "+
            "CURRENCY_CODE_REG03='"+newVO.getCurrencyCode2Reg03REG06()+"' and "+
            "CURRENCY_CODE2_REG03='"+newVO.getCurrencyCodeReg03REG06()+"'";


        stmt.execute(sql);

      }

      Response answer = new VOListResponse(newVOs,false,newVOs.size());

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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while updating existing measure conversions",ex);
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
      catch (SQLException ex2) {
      }
      try {
        ConnectionManager.releaseConnection(conn, context);
      }
      catch (Exception ex1) {
      }
    }

  }



}
