package org.jallinone.production.manufactures.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.production.manufactures.java.*;
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
 * <p>Description: Action class used to validate a manufacture code from PRO01 table.</p>
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
public class ValidateManufactureCodeAction implements Action {


  public ValidateManufactureCodeAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "validateManufactureCode";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();
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

      LookupValidationParams validationPars = (LookupValidationParams)inputPar;

      // retrieve companies list...
      String companies = "";
      if (validationPars.getLookupValidationParameters().get(ApplicationConsts.COMPANY_CODE_SYS01)!=null)
        companies = "'"+(String)validationPars.getLookupValidationParameters().get(ApplicationConsts.COMPANY_CODE_SYS01)+"'";
      else {
        ArrayList companiesList = ( (JAIOUserSessionParameters) userSessionPars).getCompanyBa().getCompaniesList("PRO01");
        for (int i = 0; i < companiesList.size(); i++)
          companies += "'" + companiesList.get(i).toString() + "',";
        companies = companies.substring(0, companies.length() - 1);
      }

      String sql =
          "select PRO01_MANUFACTURES.COMPANY_CODE_SYS01,SYS10_TRANSLATIONS.DESCRIPTION,PRO01_MANUFACTURES.MANUFACTURE_CODE,"+
          "PRO01_MANUFACTURES.PROGRESSIVE_SYS10 "+
          "from PRO01_MANUFACTURES,SYS10_TRANSLATIONS where "+
          "PRO01_MANUFACTURES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "PRO01_MANUFACTURES.COMPANY_CODE_SYS01 in ("+companies+") and "+
          "PRO01_MANUFACTURES.ENABLED='Y' and "+
          "PRO01_MANUFACTURES.MANUFACTURE_CODE='"+validationPars.getCode()+"'";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01PRO01","PRO01_MANUFACTURES.COMPANY_CODE_SYS01");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("manufactureCodePRO01","PRO01_MANUFACTURES.MANUFACTURE_CODE");
      attribute2dbField.put("progressiveSys10PRO01","PRO01_MANUFACTURES.PROGRESSIVE_SYS10");


      ArrayList values = new ArrayList();
      values.add(serverLanguageId);

      // read from PRO01 table...
      Response answer = CustomizeQueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          ManufactureVO.class,
          "Y",
          "N",
          context,
          new GridParams(),
          true,
          ApplicationConsts.ID_MANUFACTURE // window identifier...
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while validating manufacture code",ex);
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
