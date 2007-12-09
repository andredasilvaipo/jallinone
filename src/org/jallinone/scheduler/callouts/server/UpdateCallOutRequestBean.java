package org.jallinone.scheduler.callouts.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.scheduler.callouts.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import java.math.BigDecimal;
import org.jallinone.system.progressives.server.ProgressiveUtils;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.system.java.ApplicationParametersVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Helper class used to update an existing call-out request:
 * it does not create a connection and it doesn not commit/rollback transaction.</p>
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
public class UpdateCallOutRequestBean {


  public UpdateCallOutRequestBean() {
  }


  /**
   * Update call-out request.
   */
  public final Response updateCallOutRequest(Connection conn,DetailCallOutRequestVO oldVO,DetailCallOutRequestVO newVO,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();
    Statement stmt = null;
    try {

      // fires the GenericEvent.CONNECTION_CREATED event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "UpdateCallOutRequestBean.updateCallOutRequest",
        GenericEvent.CONNECTION_CREATED,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        newVO,
        null
      ));

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SCH03","COMPANY_CODE_SYS01");
      attribute2dbField.put("requestYearSCH03","REQUEST_YEAR");
      attribute2dbField.put("progressiveSCH03","PROGRESSIVE");
      attribute2dbField.put("descriptionSCH03","DESCRIPTION");
      attribute2dbField.put("callOutCodeSch10SCH03","CALL_OUT_CODE_SCH10");
      attribute2dbField.put("callOutStateSCH03","CALL_OUT_STATE");
      attribute2dbField.put("usernameSys03SCH03","USERNAME_SYS03");
      attribute2dbField.put("prioritySCH03","PRIORITY");
      attribute2dbField.put("requestDateSCH03","REQUEST_DATE");
      attribute2dbField.put("noteSCH03","NOTE");
      attribute2dbField.put("docTypeDoc01SCH03","DOC_TYPE_DOC01");
      attribute2dbField.put("docNumberDoc01SCH03","DOC_NUMBER_DOC01");
      attribute2dbField.put("docYearDoc01SCH03","DOC_YEAR_DOC01");
      attribute2dbField.put("progressiveReg04SCH03","PROGRESSIVE_REG04");
      attribute2dbField.put("progressiveSch06SCH03","PROGRESSIVE_SCH06");
      attribute2dbField.put("subjectTypeReg04SCH03","SUBJECT_TYPE_REG04");

      HashSet pkAttributes = new HashSet();
      pkAttributes.add("companyCodeSys01SCH03");
      pkAttributes.add("requestYearSCH03");
      pkAttributes.add("progressiveSCH03");

      // update SCH03 table...
      Response answer = CustomizeQueryUtil.updateTable(
          conn,
          userSessionPars,
          pkAttributes,
          oldVO,
          newVO,
          "SCH03_CALL_OUT_REQUESTS",
          attribute2dbField,
          "Y",
          "N",
          context,
          true,
          ApplicationConsts.ID_CALL_OUT_REQUESTS // window identifier...
      );

      // fires the GenericEvent.BEFORE_COMMIT event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "UpdateCallOutRequestBean.updateCallOutRequest",
        GenericEvent.BEFORE_COMMIT,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        newVO,
        answer
      ));


      return answer;

    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"updateCallOutRequest","Error while updating an existing call-out request",ex);
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        stmt.close();
      }
      catch (Exception ex2) {
      }
    }
  }



}
