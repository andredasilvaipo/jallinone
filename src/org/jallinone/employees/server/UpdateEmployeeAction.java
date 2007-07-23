package org.jallinone.employees.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.employees.java.*;
import org.openswing.swing.server.QueryUtil;
import org.jallinone.commons.server.CustomizeQueryUtil;
import java.math.BigDecimal;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.subjects.server.PeopleBean;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to update an existing employee.</p>
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
public class UpdateEmployeeAction implements Action {


  public UpdateEmployeeAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "updateEmployee";
  }


  private PeopleBean bean = new PeopleBean();


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    Connection conn = null;
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


      DetailEmployeeVO oldVO = (DetailEmployeeVO)((ValueObject[])inputPar)[0];
      DetailEmployeeVO newVO = (DetailEmployeeVO)((ValueObject[])inputPar)[1];

      // update REG04 table...
      Response res = bean.update(conn,oldVO,newVO,userSessionPars,context);
      if (res.isError()) {
        conn.rollback();
        return res;
      }

      HashSet pkAttrs = new HashSet();
      pkAttrs.add("companyCodeSys01SCH01");
      pkAttrs.add("progressiveReg04SCH01");

      HashMap attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SCH01","COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveReg04SCH01","PROGRESSIVE_REG04");
      attribute2dbField.put("employeeCodeSCH01","EMPLOYEE_CODE");
      attribute2dbField.put("phoneNumberSCH01","PHONE_NUMBER");
      attribute2dbField.put("officeSCH01","OFFICE");
      attribute2dbField.put("taskCodeReg07SCH01","TASK_CODE_REG07");
      attribute2dbField.put("divisionSCH01","DIVISION");
      attribute2dbField.put("emailAddressSCH01","EMAIL_ADDRESS");
      attribute2dbField.put("managerProgressiveReg04SCH01","MANAGER_PROGRESSIVE_REG04");
      attribute2dbField.put("assistantProgressiveReg04SCH01","ASSISTANT_PROGRESSIVE_REG04");
      attribute2dbField.put("salarySCH01","SALARY");
      attribute2dbField.put("levelSCH01","LEVEL");
      attribute2dbField.put("enabledSCH01","ENABLED");
      attribute2dbField.put("engagedDateSCH01","ENGAGED_DATE");
      attribute2dbField.put("dismissalDateSCH01","DISMISSAL_DATE");
      attribute2dbField.put("managerCompanyCodeSys01SCH01","COMPANY_CODE_SYS01_MANAGER_SCH01");
      attribute2dbField.put("assistantCompanyCodeSys01SCH01","COMPANY_CODE_SYS01_ASSISTANT_SCH01");


      res = new CustomizeQueryUtil().updateTable(
          conn,
          userSessionPars,
          pkAttrs,
          oldVO,
          newVO,
          "SCH01_EMPLOYEES",
          attribute2dbField,
          "Y",
          "N",
          context,
          true,
          new BigDecimal(662)
      );
      if (res.isError()) {
        conn.rollback();
        return res;
      }

      Response answer =  new VOResponse(newVO);

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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while updating an existing employee",ex);
      try {
        conn.rollback();
      }
      catch (Exception ex3) {
      }
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
