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
 * <p>Description: Action class used to validate an operation code from PRO04 table.</p>
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
public class ValidateOperationCodeAction implements Action {


  public ValidateOperationCodeAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "validateOperationCode";
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
        ArrayList companiesList = ( (JAIOUserSessionParameters) userSessionPars).getCompanyBa().getCompaniesList("PRO04");
        for (int i = 0; i < companiesList.size(); i++)
          companies += "'" + companiesList.get(i).toString() + "',";
        companies = companies.substring(0, companies.length() - 1);
      }

      String sql =
          "select PRO04_OPERATIONS.COMPANY_CODE_SYS01,PRO04_OPERATIONS.OPERATION_CODE,SYS10_TRANSLATIONS.DESCRIPTION,"+
          "PRO04_OPERATIONS.PROGRESSIVE_SYS10,PRO04_OPERATIONS.QTY,PRO04_OPERATIONS.TASK_CODE_REG07,"+
          "PRO04_OPERATIONS.MACHINERY_CODE_PRO03,PRO04_OPERATIONS.DURATION,PRO04_OPERATIONS.VALUE,"+
          "PRO04_OPERATIONS.NOTE,TASKS.DESCRIPTION,PRO03.DESCRIPTION "+
          "from SYS10_TRANSLATIONS,PRO04_OPERATIONS "+
          "LEFT OUTER JOIN "+
          "(select SYS10_TRANSLATIONS.DESCRIPTION,REG07_TASKS.COMPANY_CODE_SYS01,REG07_TASKS.TASK_CODE "+
          "from REG07_TASKS,SYS10_TRANSLATIONS where "+
          "REG07_TASKS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=?) TASKS ON "+
          "TASKS.COMPANY_CODE_SYS01=PRO04_OPERATIONS.COMPANY_CODE_SYS01 and "+
          "TASKS.TASK_CODE=PRO04_OPERATIONS.TASK_CODE_REG07 "+
          "LEFT OUTER JOIN "+
          "(select SYS10_TRANSLATIONS.DESCRIPTION,PRO03_MACHINERIES.COMPANY_CODE_SYS01,PRO03_MACHINERIES.MACHINERY_CODE "+
          "from PRO03_MACHINERIES,SYS10_TRANSLATIONS where "+
          "PRO03_MACHINERIES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=?) PRO03 ON "+
          "PRO03.COMPANY_CODE_SYS01=PRO04_OPERATIONS.COMPANY_CODE_SYS01 and "+
          "PRO03.MACHINERY_CODE=PRO04_OPERATIONS.MACHINERY_CODE_PRO03 "+
          "where "+
          "PRO04_OPERATIONS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "PRO04_OPERATIONS.COMPANY_CODE_SYS01 in ("+companies+") and "+
          "PRO04_OPERATIONS.ENABLED='Y' and "+
          "PRO04_OPERATIONS.OPERATION_CODE='"+validationPars.getCode()+"'";


      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01PRO04","PRO04_OPERATIONS.COMPANY_CODE_SYS01");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("operationCodePRO04","PRO04_OPERATIONS.OPERATION_CODE");
      attribute2dbField.put("progressiveSys10PRO04","PRO04_OPERATIONS.PROGRESSIVE_SYS10");
      attribute2dbField.put("qtyPRO04","PRO04_OPERATIONS.QTY");
      attribute2dbField.put("taskCodeReg07PRO04","PRO04_OPERATIONS.TASK_CODE_REG07");
      attribute2dbField.put("machineryCodePro03PRO04","PRO04_OPERATIONS.MACHINERY_CODE_PRO03");
      attribute2dbField.put("durationPRO04","PRO04_OPERATIONS.DURATION");
      attribute2dbField.put("valuePRO04","PRO04_OPERATIONS.VALUE");
      attribute2dbField.put("notePRO04","PRO04_OPERATIONS.NOTE");
      attribute2dbField.put("taskDescriptionSYS10","TASKS.DESCRIPTION");
      attribute2dbField.put("machineryDescriptionSYS10","PRO03.DESCRIPTION");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(serverLanguageId);
      values.add(serverLanguageId);

      // read from PRO04 table...
      Response answer = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          OperationVO.class,
          "Y",
          "N",
          context,
          new GridParams(),
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while validating operation code",ex);
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
