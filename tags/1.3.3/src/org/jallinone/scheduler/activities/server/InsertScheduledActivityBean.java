package org.jallinone.scheduler.activities.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import java.math.BigDecimal;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.system.progressives.server.ProgressiveUtils;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.scheduler.activities.java.ScheduledActivityVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;
import org.jallinone.system.progressives.server.CompanyProgressiveUtils;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Helper class used to insert a new scheduled activity in SCH06 table.
 * This class hinerits a Connection that does not release and does not execute any commit/roolback.</p>
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
public class InsertScheduledActivityBean {


  public InsertScheduledActivityBean() {
  }




  /**
   * Insert a new record in SCH06.
   */
  public final Response insertActivity(Connection conn,ScheduledActivityVO vo,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    Statement stmt = null;
    try {

      // fires the GenericEvent.CONNECTION_CREATED event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "InsertScheduledActivityBean.insertActivity",
        GenericEvent.CONNECTION_CREATED,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        vo,
        null
      ));

      // generate PROGRESSIVE value...
      stmt = conn.createStatement();
      BigDecimal progressive = CompanyProgressiveUtils.getInternalProgressive(vo.getCompanyCodeSys01SCH06(),"SCH06_SCHEDULED_ACTIVITIES","PROGRESSIVE",conn);
      vo.setProgressiveSCH06(progressive);

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SCH06","COMPANY_CODE_SYS01");
      attribute2dbField.put("descriptionSCH06","DESCRIPTION");
      attribute2dbField.put("activityTypeSCH06","ACTIVITY_TYPE");
      attribute2dbField.put("prioritySCH06","PRIORITY");
      attribute2dbField.put("activityStateSCH06","ACTIVITY_STATE");
      attribute2dbField.put("activityPlaceSCH06","ACTIVITY_PLACE");
      attribute2dbField.put("descriptionWkf01SCH06","DESCRIPTION_WKF01");
      attribute2dbField.put("progressiveSCH06","PROGRESSIVE");
      attribute2dbField.put("estimatedDurationSCH06","ESTIMATED_DURATION");
      attribute2dbField.put("durationSCH06","DURATION");
      attribute2dbField.put("completionPercSCH06","COMPLETION_PERC");
      attribute2dbField.put("progressiveReg04ManagerSCH06","PROGRESSIVE_REG04_MANAGER");
      attribute2dbField.put("progressiveReg04SubjectSCH06","PROGRESSIVE_REG04_SUBJECT");
      attribute2dbField.put("progressiveWkf01SCH06","PROGRESSIVE_WKF01");
      attribute2dbField.put("progressiveWkf08SCH06","PROGRESSIVE_WKF08");
      attribute2dbField.put("progressiveWkf02SCH06","PROGRESSIVE_WKF02");
      attribute2dbField.put("noteSCH06","NOTE");
      attribute2dbField.put("startDateSCH06","START_DATE");
      attribute2dbField.put("estimatedEndDateSCH06","ESTIMATED_END_DATE");
      attribute2dbField.put("endDateSCH06","END_DATE");
      attribute2dbField.put("expirationDateSCH06","EXPIRATION_DATE");
      attribute2dbField.put("emailAddressSCH06","EMAIL_ADDRESS");
      attribute2dbField.put("faxNumberSCH06","FAX_NUMBER");
      attribute2dbField.put("phoneNumberSCH06","PHONE_NUMBER");

      // insert into SCH06...
      Response res = CustomizeQueryUtil.insertTable(
          conn,
          userSessionPars,
          vo,
          "SCH06_SCHEDULED_ACTIVITIES",
          attribute2dbField,
          "Y",
          "N",
          context,
          true,
          ApplicationConsts.ID_SCHEDULED_ACTIVITIES // window identifier...
      );

      Response answer = res;

      // fires the GenericEvent.BEFORE_COMMIT event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "InsertScheduledActivityBean.insertActivity",
        GenericEvent.BEFORE_COMMIT,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        vo,
        answer
      ));

      return answer;
    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"insertActivity","Error while inserting a new scheduled activity",ex);
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
