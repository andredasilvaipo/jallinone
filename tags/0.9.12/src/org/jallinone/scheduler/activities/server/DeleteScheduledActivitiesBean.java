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
import org.jallinone.scheduler.activities.java.ScheduledActivityPK;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Helper class used to (phisically) delete existing scheduled activities:
 * it does not release the connection and it doesn not execute any commit/rollback.</p>
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
public class DeleteScheduledActivitiesBean {


  public DeleteScheduledActivitiesBean() {
  }


  /**
   * Business logic to execute.
   */
  public final Response deleteActivities(Connection conn,ArrayList pks,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    Statement stmt = null;
    try {

      // fires the GenericEvent.CONNECTION_CREATED event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "DeleteScheduledActivitiesBean.deleteActivities",
        GenericEvent.CONNECTION_CREATED,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        pks,
        null
      ));
      stmt = conn.createStatement();

      ScheduledActivityPK pk = null;
      for(int i=0;i<pks.size();i++) {
        pk = (ScheduledActivityPK)pks.get(i);

        // phisically delete the record in SCH07...
        stmt.execute(
            "delete from SCH07_SCHEDULED_EMPLOYEES where "+
            "COMPANY_CODE_SYS01='"+pk.getCompanyCodeSys01SCH06()+"' and "+
            "PROGRESSIVE_SCH06="+pk.getProgressiveSCH06()
        );

        // phisically delete the record in SCH08...
        stmt.execute(
            "delete from SCH08_ACT_ATTACHED_DOCS where "+
            "COMPANY_CODE_SYS01='"+pk.getCompanyCodeSys01SCH06()+"' and "+
            "PROGRESSIVE_SCH06="+pk.getProgressiveSCH06()
        );

        // phisically delete the record in SCH09...
        stmt.execute(
            "delete from SCH09_SCHEDULED_MACHINERIES where "+
            "COMPANY_CODE_SYS01='"+pk.getCompanyCodeSys01SCH06()+"' and "+
            "PROGRESSIVE_SCH06="+pk.getProgressiveSCH06()
        );

        // phisically delete the record in SCH15...
        stmt.execute(
            "delete from SCH15_SCHEDULED_ITEMS where "+
            "COMPANY_CODE_SYS01='"+pk.getCompanyCodeSys01SCH06()+"' and "+
            "PROGRESSIVE_SCH06="+pk.getProgressiveSCH06()
        );

        // update record in SCH03 if it is linked to the record in SCH06...
        stmt.execute(
            "update SCH03_CALL_OUT_REQUESTS set PROGRESSIVE_SCH06=null where "+
            "COMPANY_CODE_SYS01='"+pk.getCompanyCodeSys01SCH06()+"' and "+
            "PROGRESSIVE_SCH06="+pk.getProgressiveSCH06()
        );

        // phisically delete the record in SCH06...
        stmt.execute(
            "delete from SCH06_SCHEDULED_ACTIVITIES where "+
            "COMPANY_CODE_SYS01='"+pk.getCompanyCodeSys01SCH06()+"' and "+
            "PROGRESSIVE="+pk.getProgressiveSCH06()
        );

      }

      Response answer = new VOResponse(new Boolean(true));
      // fires the GenericEvent.BEFORE_COMMIT event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "DeleteScheduledActivitiesBean.deleteActivities",
        GenericEvent.BEFORE_COMMIT,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        pks,
        answer
      ));

      return answer;
    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"deleteActivities","Error while deleting existing scheduled activities",ex);
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
