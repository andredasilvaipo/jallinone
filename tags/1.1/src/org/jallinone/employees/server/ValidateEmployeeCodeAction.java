package org.jallinone.employees.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.employees.java.*;
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
 * <p>Description: Action class used to validate an employee code from SCH01 table.</p>
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
public class ValidateEmployeeCodeAction implements Action {


  public ValidateEmployeeCodeAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "validateEmployeeCode";
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


      String companies = "";
      if (validationPars.getLookupValidationParameters().get(ApplicationConsts.COMPANY_CODE_SYS01)!=null) {
        companies ="'"+validationPars.getLookupValidationParameters().get(ApplicationConsts.COMPANY_CODE_SYS01)+"'";
      }
      else {
        // retrieve companies list...
        ArrayList companiesList = ((JAIOUserSessionParameters)userSessionPars).getCompanyBa().getCompaniesList("SCH01");
        for(int i=0;i<companiesList.size();i++)
          companies += "'"+companiesList.get(i).toString()+"',";
        companies = companies.substring(0,companies.length()-1);
      }

      String sql =
          "select SCH01_EMPLOYEES.COMPANY_CODE_SYS01,SCH01_EMPLOYEES.PROGRESSIVE_REG04,SCH01_EMPLOYEES.EMPLOYEE_CODE,"+
          "SCH01_EMPLOYEES.TASK_CODE_REG07,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,SYS10_TRANSLATIONS.DESCRIPTION,SCH01_EMPLOYEES.PHONE_NUMBER,SCH01_EMPLOYEES.OFFICE "+
          "from SCH01_EMPLOYEES,SYS10_TRANSLATIONS,REG07_TASKS,REG04_SUBJECTS where "+
          "SCH01_EMPLOYEES.COMPANY_CODE_SYS01=REG07_TASKS.COMPANY_CODE_SYS01 and "+
          "SCH01_EMPLOYEES.TASK_CODE_REG07=REG07_TASKS.TASK_CODE and "+
          "REG07_TASKS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "SCH01_EMPLOYEES.COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 and "+
          "SCH01_EMPLOYEES.PROGRESSIVE_REG04=REG04_SUBJECTS.PROGRESSIVE and "+
          "SCH01_EMPLOYEES.ENABLED='Y' and "+
          "SCH01_EMPLOYEES.COMPANY_CODE_SYS01 in ("+companies+") and "+
          "SCH01_EMPLOYEES.EMPLOYEE_CODE='"+validationPars.getCode()+"'";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SCH01","SCH01_EMPLOYEES.COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveReg04SCH01","SCH01_EMPLOYEES.PROGRESSIVE_REG04");
      attribute2dbField.put("name_1REG04","REG04_SUBJECTS.NAME_1");
      attribute2dbField.put("name_2REG04","REG04_SUBJECTS.NAME_2");
      attribute2dbField.put("phoneNumberSCH01","SCH01_EMPLOYEES.PHONE_NUMBER");
      attribute2dbField.put("officeSCH01","SCH01_EMPLOYEES.OFFICE");
      attribute2dbField.put("employeeCodeSCH01","SCH01_EMPLOYEES.EMPLOYEE_CODE");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("taskCodeReg07SCH01","SCH01_EMPLOYEES.TASK_CODE_REG07");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);

      if (validationPars.getLookupValidationParameters().get(ApplicationConsts.TASK_CODE_REG07)!=null) {
        sql += " and SCH01_EMPLOYEES.TASK_CODE_REG07='"+validationPars.getLookupValidationParameters().get(ApplicationConsts.TASK_CODE_REG07)+"'";
      }


      GridParams gridParams = new GridParams();

      // read from SCH01 table...
      Response answer = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          GridEmployeeVO.class,
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while validating employee code",ex);
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
