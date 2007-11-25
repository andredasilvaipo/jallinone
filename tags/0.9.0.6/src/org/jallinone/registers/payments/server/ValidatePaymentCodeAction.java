package org.jallinone.registers.payments.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.registers.payments.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.openswing.swing.message.send.java.LookupValidationParams;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.commons.server.CustomizeQueryUtil;
import java.math.BigDecimal;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;



/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch payment from REG10 table.</p>
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
public class ValidatePaymentCodeAction implements Action {


  public ValidatePaymentCodeAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "validatePaymentCode";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();

    Connection conn = null;
    try {


      LookupValidationParams validationPars = (LookupValidationParams)inputPar;

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
          "select REG10_PAYMENTS.PAYMENT_CODE,REG10_PAYMENTS.PROGRESSIVE_SYS10,SYS10_TRANSLATIONS.DESCRIPTION,"+
          "REG10_PAYMENTS.ENABLED,REG10_PAYMENTS.STEP,REG10_PAYMENTS.INSTALMENT_NUMBER,REG10_PAYMENTS.START_DAY,"+
          "REG10_PAYMENTS.PAYMENT_TYPE_CODE_REG11,REG10_PAYMENTS.FIRST_INSTALMENT_DAYS,SYS10_REG11.DESCRIPTION "+
          " from REG10_PAYMENTS,SYS10_TRANSLATIONS,SYS10_TRANSLATIONS SYS10_REG11,REG11_PAYMENT_TYPES where "+
          "REG10_PAYMENTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "REG10_PAYMENTS.PAYMENT_TYPE_CODE_REG11=REG11_PAYMENT_TYPES.PAYMENT_TYPE_CODE and "+
          "REG11_PAYMENT_TYPES.PROGRESSIVE_SYS10=SYS10_REG11.PROGRESSIVE and "+
          "SYS10_REG11.LANGUAGE_CODE=? and "+
          "REG10_PAYMENTS.ENABLED='Y' and "+
          "REG10_PAYMENTS.PAYMENT_CODE='"+validationPars.getCode()+"'";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("paymentCodeREG10","REG10_PAYMENTS.PAYMENT_CODE");
      attribute2dbField.put("progressiveSys10REG10","REG10_PAYMENTS.PROGRESSIVE_SYS10");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("enabledREG10","REG10_PAYMENTS.ENABLED");
      attribute2dbField.put("stepREG10","REG10_PAYMENTS.STEP");
      attribute2dbField.put("instalmentNumberREG10","REG10_PAYMENTS.INSTALMENT_NUMBER");
      attribute2dbField.put("startDayREG10","REG10_PAYMENTS.START_DAY");
      attribute2dbField.put("paymentTypeCodeReg11REG10","REG10_PAYMENTS.PAYMENT_TYPE_CODE_REG11");
      attribute2dbField.put("firstInstalmentDaysREG10","REG10_PAYMENTS.FIRST_INSTALMENT_DAYS");
      attribute2dbField.put("paymentTypeDescriptionSYS10","SYS10_REG11.DESCRIPTION");


      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(serverLanguageId);

      GridParams gridParams = new GridParams();

      // read from REG11 table...
      Response answer = CustomizeQueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          PaymentVO.class,
          "Y",
          "N",
          context,
          gridParams,
          true,
          new BigDecimal(212) // window identifier...
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while validating payment code",ex);
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
