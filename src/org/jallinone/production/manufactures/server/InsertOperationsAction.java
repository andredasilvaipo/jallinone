package org.jallinone.production.manufactures.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.production.manufactures.java.*;
import java.math.BigDecimal;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.system.progressives.server.ProgressiveUtils;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to insert new operations in PRO04 table.</p>
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
public class InsertOperationsAction implements Action {


  public InsertOperationsAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "insertOperations";
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

     ArrayList vos = (ArrayList)inputPar;
      OperationVO vo = null;
      Response res = null;

      String companyCode = ((JAIOUserSessionParameters)userSessionPars).getCompanyBa().getCompaniesList("PRO04").get(0).toString();

      for(int i=0;i<vos.size();i++) {
        vo = (OperationVO)vos.get(i);
        if (vo.getCompanyCodeSys01PRO04()==null)
          vo.setCompanyCodeSys01PRO04(companyCode);
        vo.setEnabledPRO04("Y");
        vo.setProgressiveSys10PRO04(TranslationUtils.insertTranslations(vo.getDescriptionSYS10(),vo.getCompanyCodeSys01PRO04(),conn));

        Map attribute2dbField = new HashMap();
        attribute2dbField.put("companyCodeSys01PRO04","COMPANY_CODE_SYS01");
        attribute2dbField.put("operationCodePRO04","OPERATION_CODE");
        attribute2dbField.put("progressiveSys10PRO04","PROGRESSIVE_SYS10");
        attribute2dbField.put("qtyPRO04","QTY");
        attribute2dbField.put("taskCodeReg07PRO04","TASK_CODE_REG07");
        attribute2dbField.put("machineryCodePro03PRO04","MACHINERY_CODE_PRO03");
        attribute2dbField.put("durationPRO04","DURATION");
        attribute2dbField.put("valuePRO04","VALUE");
        attribute2dbField.put("notePRO04","NOTE");
        attribute2dbField.put("enabledPRO04","ENABLED");

        // insert into PRO04...
        res = QueryUtil.insertTable(
            conn,
            userSessionPars,
            vo,
            "PRO04_OPERATIONS",
            attribute2dbField,
            "Y",
            "N",
            context,
            true
        );
        if (res.isError()) {
          conn.rollback();
          return res;
        }

      }


      Response answer = new VOListResponse(vos,false,vos.size());

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
                   "executeCommand", "Error while inserting new operations", ex);
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

