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
import org.openswing.swing.server.QueryUtil;
import java.math.BigDecimal;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to update existing operations.</p>
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
public class UpdateOperationsAction implements Action {


  public UpdateOperationsAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "updateOperations";
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


      ArrayList oldVOs = ((ArrayList[])inputPar)[0];
      ArrayList newVOs = ((ArrayList[])inputPar)[1];
      OperationVO oldVO = null;
      OperationVO newVO = null;
      Response res = null;

      String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();

      HashSet pkAttrs = new HashSet();
      pkAttrs.add("companyCodeSys01PRO04");
      pkAttrs.add("operationCodePRO04");

      HashMap attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01PRO04","COMPANY_CODE_SYS01");
      attribute2dbField.put("operationCodePRO04","OPERATION_CODE");
      attribute2dbField.put("progressiveSys10PRO04","PROGRESSIVE_SYS10");
      attribute2dbField.put("qtyPRO04","QTY");
      attribute2dbField.put("taskCodeReg07PRO04","TASK_CODE_REG07");
      attribute2dbField.put("machineryCodePro03PRO04","MACHINERY_CODE_PRO03");
      attribute2dbField.put("durationPRO04","DURATION");
      attribute2dbField.put("valuePRO04","VALUE");
      attribute2dbField.put("notePRO04","NOTE");


      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (OperationVO)oldVOs.get(i);
        newVO = (OperationVO)newVOs.get(i);

        TranslationUtils.updateTranslation(oldVO.getDescriptionSYS10(),newVO.getDescriptionSYS10(),newVO.getProgressiveSys10PRO04(),serverLanguageId,conn);

        res = new QueryUtil().updateTable(
            conn,
            userSessionPars,
            pkAttrs,
            oldVO,
            newVO,
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

      Response answer = new VOListResponse(newVOs,false,newVOs.size());

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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while updating existing operations",ex);
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
