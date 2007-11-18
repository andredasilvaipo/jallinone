package org.jallinone.accounting.accounts.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.accounting.accounts.java.*;
import java.math.BigDecimal;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;
import org.jallinone.events.server.EventAction;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to insert new accounts in ACC02 table.</p>
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
public class InsertAccountsAction implements EventAction {


  public InsertAccountsAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "insertAccounts";
  }


  /**
   * @return input parameter class or data contained on it; value object class or Map of type <par name,par value>
   */
  public Object getInputParClass() {
    return AccountVO.class;
  }


  /**
   * @return value object class contained in the Response object
   */
  public Class getValueObjectClass() {
    return AccountVO.class;
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

      ArrayList rows = (ArrayList)inputPar;
      Response res = null;
      AccountVO vo = null;
      BigDecimal progressiveSYS10 = null;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("accountCodeACC02","ACCOUNT_CODE");
      attribute2dbField.put("progressiveSys10ACC02","PROGRESSIVE_SYS10");
      attribute2dbField.put("enabledACC02","ENABLED");
      attribute2dbField.put("companyCodeSys01ACC02","COMPANY_CODE_SYS01");
      attribute2dbField.put("accountTypeACC02","ACCOUNT_TYPE");
      attribute2dbField.put("ledgerCodeAcc01ACC02","LEDGER_CODE_ACC01");
      attribute2dbField.put("canDelACC02","CAN_DEL");

      for(int i=0;i<rows.size();i++) {
        vo = (AccountVO)rows.get(i);
        vo.setEnabledACC02("Y");

        if (vo.getCanDelACC02()==null)
          vo.setCanDelACC02(new Boolean(true));

        // insert record in SYS10...
        progressiveSYS10 = TranslationUtils.insertTranslations(vo.getDescriptionSYS10(),conn);
        vo.setProgressiveSys10ACC02(progressiveSYS10);

        // insert into ACC02...
        res = CustomizeQueryUtil.insertTable(
            conn,
            userSessionPars,
            vo,
            "ACC02_ACCOUNTS",
            attribute2dbField,
            "Y",
            "N",
            context,
            true,
            ApplicationConsts.ID_ACCOUNTS // window identifier...
        );
        if (res.isError()) {
          conn.rollback();
          return res;
        }

      }


      Response answer = new VOListResponse(rows,false,rows.size());

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
                   "executeCommand", "Error while inserting new accounts", ex);
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

