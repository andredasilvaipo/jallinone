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
import org.jallinone.commons.server.CustomizeQueryUtil;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.scheduler.activities.java.ScheduledActivityVO;
import org.jallinone.scheduler.activities.java.ScheduledActivityPK;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch the specified scheduled activity from SCH06 table.</p>
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
public class LoadScheduledActivityAction implements Action {


  public LoadScheduledActivityAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadScheduledActivity";
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

      ScheduledActivityPK pk = (ScheduledActivityPK)inputPar;

      String sql =
          "select SCH06_SCHEDULED_ACTIVITIES.COMPANY_CODE_SYS01,SCH06_SCHEDULED_ACTIVITIES.DESCRIPTION,SCH06_SCHEDULED_ACTIVITIES.ACTIVITY_TYPE,"+
          "SCH06_SCHEDULED_ACTIVITIES.PRIORITY,SCH06_SCHEDULED_ACTIVITIES.ACTIVITY_STATE,SCH06_SCHEDULED_ACTIVITIES.ACTIVITY_PLACE,"+
          "SCH06_SCHEDULED_ACTIVITIES.DESCRIPTION_WKF01,"+
          "SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE,SCH06_SCHEDULED_ACTIVITIES.ESTIMATED_DURATION,SCH06_SCHEDULED_ACTIVITIES.DURATION,"+
          "SCH06_SCHEDULED_ACTIVITIES.COMPLETION_PERC,SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE_REG04_MANAGER,SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE_REG04_SUBJECT,"+
          "SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE_WKF01,SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE_WKF08,SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE_WKF02,"+
          "SCH06_SCHEDULED_ACTIVITIES.NOTE,SCH06_SCHEDULED_ACTIVITIES.START_DATE,SCH06_SCHEDULED_ACTIVITIES.ESTIMATED_END_DATE,SCH06_SCHEDULED_ACTIVITIES.END_DATE,"+
          "SCH06_SCHEDULED_ACTIVITIES.EXPIRATION_DATE,"+
          "REG04_MANAGER.NAME_1,REG04_MANAGER.NAME_2,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,REG04_SUBJECTS.SUBJECT_TYPE, "+
          "SCH06_SCHEDULED_ACTIVITIES.EMAIL_ADDRESS,SCH06_SCHEDULED_ACTIVITIES.FAX_NUMBER,SCH06_SCHEDULED_ACTIVITIES.PHONE_NUMBER "+
          "from SCH06_SCHEDULED_ACTIVITIES "+
          "LEFT OUTER JOIN "+
          "(select REG04_SUBJECTS.COMPANY_CODE_SYS01,REG04_SUBJECTS.PROGRESSIVE,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2 "+
          "from REG04_SUBJECTS) REG04_MANAGER ON "+
          "REG04_MANAGER.COMPANY_CODE_SYS01=SCH06_SCHEDULED_ACTIVITIES.COMPANY_CODE_SYS01 and "+
          "REG04_MANAGER.PROGRESSIVE=SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE_REG04_MANAGER "+
          "LEFT OUTER JOIN "+
          "(select REG04_SUBJECTS.COMPANY_CODE_SYS01,REG04_SUBJECTS.PROGRESSIVE,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,REG04_SUBJECTS.SUBJECT_TYPE "+
          "from REG04_SUBJECTS) REG04_SUBJECTS ON "+
          "REG04_SUBJECTS.COMPANY_CODE_SYS01=SCH06_SCHEDULED_ACTIVITIES.COMPANY_CODE_SYS01 and "+
          "REG04_SUBJECTS.PROGRESSIVE=SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE_REG04_SUBJECT "+
          "where SCH06_SCHEDULED_ACTIVITIES.COMPANY_CODE_SYS01=? and SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE=?";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SCH06","SCH06_SCHEDULED_ACTIVITIES.COMPANY_CODE_SYS01");
      attribute2dbField.put("descriptionSCH06","SCH06_SCHEDULED_ACTIVITIES.DESCRIPTION");
      attribute2dbField.put("activityTypeSCH06","SCH06_SCHEDULED_ACTIVITIES.ACTIVITY_TYPE");
      attribute2dbField.put("prioritySCH06","SCH06_SCHEDULED_ACTIVITIES.PRIORITY");
      attribute2dbField.put("activityStateSCH06","SCH06_SCHEDULED_ACTIVITIES.ACTIVITY_STATE");
      attribute2dbField.put("activityPlaceSCH06","SCH06_SCHEDULED_ACTIVITIES.ACTIVITY_PLACE");
      attribute2dbField.put("descriptionWkf01SCH06","SCH06_SCHEDULED_ACTIVITIES.DESCRIPTION_WKF01");
      attribute2dbField.put("progressiveSCH06","SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE");
      attribute2dbField.put("estimatedDurationSCH06","SCH06_SCHEDULED_ACTIVITIES.ESTIMATED_DURATION");
      attribute2dbField.put("durationSCH06","SCH06_SCHEDULED_ACTIVITIES.DURATION");
      attribute2dbField.put("completionPercSCH06","SCH06_SCHEDULED_ACTIVITIES.COMPLETION_PERC");
      attribute2dbField.put("progressiveReg04ManagerSCH06","SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE_REG04_MANAGER");
      attribute2dbField.put("progressiveReg04SubjectSCH06","SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE_REG04_SUBJECT");
      attribute2dbField.put("progressiveWkf01SCH06","SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE_WKF01");
      attribute2dbField.put("progressiveWkf08SCH06","SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE_WKF08");
      attribute2dbField.put("progressiveWkf02SCH06","SCH06_SCHEDULED_ACTIVITIES.PROGRESSIVE_WKF02");
      attribute2dbField.put("noteSCH06","SCH06_SCHEDULED_ACTIVITIES.NOTE");
      attribute2dbField.put("startDateSCH06","SCH06_SCHEDULED_ACTIVITIES.START_DATE");
      attribute2dbField.put("estimatedEndDateSCH06","SCH06_SCHEDULED_ACTIVITIES.ESTIMATED_END_DATE");
      attribute2dbField.put("endDateSCH06","SCH06_SCHEDULED_ACTIVITIES.END_DATE");
      attribute2dbField.put("expirationDateSCH06","SCH06_SCHEDULED_ACTIVITIES.EXPIRATION_DATE");
      attribute2dbField.put("managerName_1SCH06","REG04_MANAGER.NAME_1");
      attribute2dbField.put("managerName_2SCH06","REG04_MANAGER.NAME_2");
      attribute2dbField.put("subjectName_1SCH06","REG04_SUBJECTS.NAME_1");
      attribute2dbField.put("subjectName_2SCH06","REG04_SUBJECTS.NAME_2");
      attribute2dbField.put("emailAddressSCH06","SCH06_SCHEDULED_ACTIVITIES.EMAIL_ADDRESS");
      attribute2dbField.put("faxNumberSCH06","SCH06_SCHEDULED_ACTIVITIES.FAX_NUMBER");
      attribute2dbField.put("phoneNumberSCH06","SCH06_SCHEDULED_ACTIVITIES.PHONE_NUMBER");
      attribute2dbField.put("subjectTypeReg04SubjectSCH06","REG04_SUBJECTS.SUBJECT_TYPE");


      ArrayList values = new ArrayList();
      values.add(pk.getCompanyCodeSys01SCH06());
      values.add(pk.getProgressiveSCH06());

      // read from SCH06 table...
      Response res = CustomizeQueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          ScheduledActivityVO.class,
          "Y",
          "N",
          context,
          true,
          ApplicationConsts.ID_SCHEDULED_ACTIVITIES // window identifier...
      );

      if (!res.isError()) {
        ScheduledActivityVO vo = (ScheduledActivityVO)((VOResponse)res).getVo();
        if (vo.getSubjectTypeReg04SubjectSCH06()!=null) {
          if (vo.getSubjectTypeReg04SubjectSCH06().equals(ApplicationConsts.SUBJECT_ORGANIZATION_CONTACT) ||
              vo.getSubjectTypeReg04SubjectSCH06().equals(ApplicationConsts.SUBJECT_ORGANIZATION_CUSTOMER))
            vo.setSubjectTypeReg04SubjectSCH06(ApplicationConsts.SUBJECT_ORGANIZATION);
          else
            vo.setSubjectTypeReg04SubjectSCH06(ApplicationConsts.SUBJECT_PEOPLE);
        }
      }

      Response answer = res;

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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching the scheduled activity",ex);
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
