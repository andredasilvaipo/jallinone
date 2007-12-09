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
import java.math.BigDecimal;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to insert a new currency in REG03 table.</p>
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
public class InsertCurrencyAction implements Action {


  public InsertCurrencyAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "insertCurrency";
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
      CurrencyVO vo = (CurrencyVO)inputPar;
      vo.setEnabledREG03("Y");

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("currencyCodeREG03","CURRENCY_CODE");
      attribute2dbField.put("currencySymbolREG03","CURRENCY_SYMBOL");
      attribute2dbField.put("thousandSymbolREG03","THOUSAND_SYMBOL");
      attribute2dbField.put("decimalSymbolREG03","DECIMAL_SYMBOL");
      attribute2dbField.put("decimalsREG03","DECIMALS");
      attribute2dbField.put("enabledREG03","ENABLED");

      // insert into REG03...
      Response res = CustomizeQueryUtil.insertTable(
          conn,
          userSessionPars,
          vo,
          "REG03_CURRENCIES",
          attribute2dbField,
          "Y",
          "N",
          context,
          true,
          new BigDecimal(182) // window identifier...
      );

      // insert records in REG06...
      stmt = conn.createStatement();
      stmt.execute(
          "insert into REG06_CURRENCY_CONV(CURRENCY_CODE_REG03,CURRENCY_CODE2_REG03) "+
          "select '"+vo.getCurrencyCodeREG03()+"',CURRENCY_CODE from REG03_CURRENCIES where CURRENCY_CODE <> '"+vo.getCurrencyCodeREG03()+"' and ENABLED='Y'"
      );
      stmt.execute(
          "insert into REG06_CURRENCY_CONV(CURRENCY_CODE2_REG03,CURRENCY_CODE_REG03) "+
          "select '"+vo.getCurrencyCodeREG03()+"',CURRENCY_CODE from REG03_CURRENCIES where CURRENCY_CODE <> '"+vo.getCurrencyCodeREG03()+"' and ENABLED='Y'"
      );

      ArrayList list = new ArrayList();
      list.add(vo);
      Response answer = new VOListResponse(list,false,list.size());

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
                   "executeCommand", "Error while inserting a new currency", ex);
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

