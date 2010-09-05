package org.jallinone.production.machineries.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.production.machineries.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.openswing.swing.message.send.java.LookupValidationParams;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.commons.server.CustomizeQueryUtil;
import java.math.BigDecimal;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch machineries from PRO03 table.</p>
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
public class ValidateMachineryCodeAction implements Action {


  public ValidateMachineryCodeAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "validateMachineryCode";
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
          "select PRO03_MACHINERIES.COMPANY_CODE_SYS01,PRO03_MACHINERIES.MACHINERY_CODE,PRO03_MACHINERIES.PROGRESSIVE_SYS10,SYS10_TRANSLATIONS.DESCRIPTION,PRO03_MACHINERIES.ENABLED, "+
          "PRO03_MACHINERIES.CURRENCY_CODE_REG03,PRO03_MACHINERIES.VALUE,PRO03_MACHINERIES.DURATION,PRO03_MACHINERIES.FINITE_CAPACITY "+
          "from PRO03_MACHINERIES,SYS10_TRANSLATIONS where "+
          "PRO03_MACHINERIES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "PRO03_MACHINERIES.ENABLED='Y' and "+
          "PRO03_MACHINERIES.MACHINERY_CODE='"+validationPars.getCode()+"' and "+
          "PRO03_MACHINERIES.COMPANY_CODE_SYS01='"+validationPars.getLookupValidationParameters().get(ApplicationConsts.COMPANY_CODE_SYS01)+"'";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("machineryCodePRO03","PRO03_MACHINERIES.MACHINERY_CODE");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("progressiveSys10PRO03","PRO03_MACHINERIES.PROGRESSIVE_SYS10");
      attribute2dbField.put("enabledPRO03","PRO03_MACHINERIES.ENABLED");
      attribute2dbField.put("companyCodeSys01PRO03","PRO03_MACHINERIES.COMPANY_CODE_SYS01");
      attribute2dbField.put("finiteCapacityPRO03","PRO03_MACHINERIES.FINITE_CAPACITY");
      attribute2dbField.put("valuePRO03","PRO03_MACHINERIES.VALUE");
      attribute2dbField.put("durationPRO03","PRO03_MACHINERIES.DURATION");
      attribute2dbField.put("currencyCodeReg03PRO03","PRO03_MACHINERIES.CURRENCY_CODE_REG03");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);

      GridParams gridParams = new GridParams();

      // read from PRO03 table...
      Response answer = CustomizeQueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          MachineryVO.class,
          "Y",
          "N",
          context,
          gridParams,
          true,
          ApplicationConsts.ID_MACHINERIES // window identifier...
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while validating machinery code",ex);
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
