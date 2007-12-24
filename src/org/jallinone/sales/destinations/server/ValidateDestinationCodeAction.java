package org.jallinone.sales.destinations.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.sales.destinations.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.openswing.swing.message.send.java.LookupValidationParams;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.commons.server.CustomizeQueryUtil;
import java.math.BigDecimal;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.subjects.java.SubjectPK;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to validate a destination code from REG18 table.</p>
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
public class ValidateDestinationCodeAction implements Action {


  public ValidateDestinationCodeAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "validateDestinationCode";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
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
          "select REG18_DESTINATIONS.COMPANY_CODE_SYS01,REG18_DESTINATIONS.PROGRESSIVE_REG04,REG18_DESTINATIONS.DESTINATION_CODE,REG18_DESTINATIONS.DESCRIPTION,REG18_DESTINATIONS.ADDRESS,REG18_DESTINATIONS.CITY,REG18_DESTINATIONS.ZIP,REG18_DESTINATIONS.PROVINCE,REG18_DESTINATIONS.COUNTRY from REG18_DESTINATIONS where "+
          "REG18_DESTINATIONS.COMPANY_CODE_SYS01=? and REG18_DESTINATIONS.PROGRESSIVE_REG04=? and "+
          "REG18_DESTINATIONS.DESTINATION_CODE='"+validationPars.getCode()+"'";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01REG18","REG18_DESTINATIONS.COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveReg04REG18","REG18_DESTINATIONS.PROGRESSIVE_REG04");
      attribute2dbField.put("addressREG18","REG18_DESTINATIONS.ADDRESS");
      attribute2dbField.put("cityREG18","REG18_DESTINATIONS.CITY");
      attribute2dbField.put("zipREG18","REG18_DESTINATIONS.ZIP");
      attribute2dbField.put("provinceREG18","REG18_DESTINATIONS.PROVINCE");
      attribute2dbField.put("countryREG18","REG18_DESTINATIONS.COUNTRY");
      attribute2dbField.put("destinationCodeREG18","REG18_DESTINATIONS.DESTINATION_CODE");
      attribute2dbField.put("descriptionREG18","REG18_DESTINATIONS.DESCRIPTION");

      ArrayList values = new ArrayList();
      SubjectPK pk = (SubjectPK)validationPars.getLookupValidationParameters().get(ApplicationConsts.SUBJECT_PK);
      values.add(pk.getCompanyCodeSys01REG04());
      values.add(pk.getProgressiveREG04());

      GridParams gridParams = new GridParams();

      // read from REG18 table...
      Response answer = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          DestinationVO.class,
          "Y",
          "N",
          context,
          gridParams,
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while validating destination code",ex);
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
