package org.jallinone.registers.bank.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.registers.bank.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.commons.server.CustomizeQueryUtil;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch banks from REG12 table.</p>
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
public class LoadBanksAction implements Action {


  public LoadBanksAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadBanks";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
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
      String sql =
          "select REG12_BANKS.BANK_CODE,REG12_BANKS.DESCRIPTION,REG12_BANKS.CAB,REG12_BANKS.ABI,REG12_BANKS.ADDRESS,REG12_BANKS.CITY,REG12_BANKS.ZIP,REG12_BANKS.PROVINCE,REG12_BANKS.COUNTRY,REG12_BANKS.ENABLED from REG12_BANKS where "+
          "REG12_BANKS.ENABLED='Y'";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("bankCodeREG12","REG12_BANKS.BANK_CODE");
      attribute2dbField.put("descriptionREG12","REG12_BANKS.DESCRIPTION");
      attribute2dbField.put("cabREG12","REG12_BANKS.CAB");
      attribute2dbField.put("abiREG12","REG12_BANKS.ABI");
      attribute2dbField.put("addressREG12","REG12_BANKS.ADDRESS");
      attribute2dbField.put("cityREG12","REG12_BANKS.CITY");
      attribute2dbField.put("zipREG12","REG12_BANKS.ZIP");
      attribute2dbField.put("provinceREG12","REG12_BANKS.PROVINCE");
      attribute2dbField.put("countryREG12","REG12_BANKS.COUNTRY");
      attribute2dbField.put("enabledREG12","REG12_BANKS.ENABLED");

      ArrayList values = new ArrayList();

      GridParams gridParams = (GridParams)inputPar;

      // read from REG12 table...
      Response answer = CustomizeQueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          BankVO.class,
          "Y",
          "N",
          context,
          gridParams,
          50,
          true,
          new BigDecimal(232) // window identifier...
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching banks list",ex);
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
