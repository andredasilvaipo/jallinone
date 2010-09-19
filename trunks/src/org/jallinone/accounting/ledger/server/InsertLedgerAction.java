package org.jallinone.accounting.ledger.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.accounting.ledger.java.*;
import java.math.BigDecimal;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;
import org.jallinone.events.server.EventAction;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to insert new ledgers in ACC01 table.</p>
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
public class InsertLedgerAction implements EventAction {


  public InsertLedgerAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "insertLedger";
  }


  /**
   * @return input parameter class or data contained on it; value object class or Map of type <par name,par value>
   */
  public Object getInputParClass() {
    return LedgerVO.class;
  }


  /**
   * @return value object class contained in the Response object
   */
  public Class getValueObjectClass() {
    return LedgerVO.class;
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

      String companyCode = ((JAIOUserSessionParameters)userSessionPars).getCompanyBa().getCompaniesList("ACC01").get(0).toString();

      LedgerVO vo = null;

      java.util.List list = (ArrayList)inputPar;
      BigDecimal progressiveSYS10 = null;
      Response res = null;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("ledgerCodeACC01","LEDGER_CODE");
      attribute2dbField.put("progressiveSys10ACC01","PROGRESSIVE_SYS10");
      attribute2dbField.put("enabledACC01","ENABLED");
      attribute2dbField.put("companyCodeSys01ACC01","COMPANY_CODE_SYS01");
      attribute2dbField.put("accountTypeACC01","ACCOUNT_TYPE");

      for(int i=0;i<list.size();i++) {
        vo = (LedgerVO)list.get(i);
        vo.setEnabledACC01("Y");
        if (vo.getCompanyCodeSys01ACC01() == null)
          vo.setCompanyCodeSys01ACC01(companyCode);

        // insert record in SYS10...
        progressiveSYS10 = TranslationUtils.insertTranslations(vo.getDescriptionSYS10(),vo.getCompanyCodeSys01ACC01(),conn);
        vo.setProgressiveSys10ACC01(progressiveSYS10);

        // insert into ACC01...
        res = CustomizeQueryUtil.insertTable(
            conn,
            userSessionPars,
            vo,
            "ACC01_LEDGER",
            attribute2dbField,
            "Y",
            "N",
            context,
            true,
            ApplicationConsts.ID_LEDGER // window identifier...
        );
        if (res.isError()) {
          conn.rollback();
          return res;
        }
      }


      Response answer = new VOListResponse(list,false,list.size());

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
                   "executeCommand", "Error while inserting new ledgers", ex);
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

