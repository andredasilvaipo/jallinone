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
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.scheduler.activities.java.ScheduledActivityPK;
import org.jallinone.scheduler.activities.java.ScheduledMachineriesVO;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch scheduled machineries from SCH09 table.</p>
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
public class LoadScheduledMachineriesAction implements Action {


  public LoadScheduledMachineriesAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadScheduledMachineries";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();

    Connection conn = null;
    PreparedStatement pstmt = null;
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
      String sql =
          "select SCH09_SCHEDULED_MACHINERIES.MACHINERY_CODE_PRO03,SCH09_SCHEDULED_MACHINERIES.COMPANY_CODE_SYS01,SCH09_SCHEDULED_MACHINERIES.PROGRESSIVE_SCH06,"+
          "SYS10_TRANSLATIONS.DESCRIPTION,SCH09_SCHEDULED_MACHINERIES.START_DATE,SCH09_SCHEDULED_MACHINERIES.END_DATE,SCH09_SCHEDULED_MACHINERIES.DURATION,"+
          "SCH09_SCHEDULED_MACHINERIES.NOTE "+
          "from SCH09_SCHEDULED_MACHINERIES,PRO03_MACHINERIES,SYS10_TRANSLATIONS where "+
          "SCH09_SCHEDULED_MACHINERIES.COMPANY_CODE_SYS01=PRO03_MACHINERIES.COMPANY_CODE_SYS01 and "+
          "SCH09_SCHEDULED_MACHINERIES.MACHINERY_CODE_PRO03=PRO03_MACHINERIES.MACHINERY_CODE and "+
          "PRO03_MACHINERIES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "SCH09_SCHEDULED_MACHINERIES.COMPANY_CODE_SYS01=? and "+
          "SCH09_SCHEDULED_MACHINERIES.PROGRESSIVE_SCH06=?";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SCH09","SCH09_SCHEDULED_MACHINERIES.COMPANY_CODE_SYS01");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("progressiveSch06SCH09","SCH09_SCHEDULED_MACHINERIES.PROGRESSIVE_SCH06");
      attribute2dbField.put("machineryCodePro03SCH09","SCH09_SCHEDULED_MACHINERIES.MACHINERY_CODE_PRO03");
      attribute2dbField.put("startDateSCH09","SCH09_SCHEDULED_MACHINERIES.START_DATE");
      attribute2dbField.put("endDateSCH09","SCH09_SCHEDULED_MACHINERIES.END_DATE");
      attribute2dbField.put("durationSCH09","SCH09_SCHEDULED_MACHINERIES.DURATION");
      attribute2dbField.put("noteSCH09","SCH09_SCHEDULED_MACHINERIES.NOTE");

      GridParams gridParams = (GridParams)inputPar;
      ScheduledActivityPK pk = (ScheduledActivityPK)gridParams.getOtherGridParams().get(ApplicationConsts.SCHEDULED_ACTIVITY_PK);

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(pk.getCompanyCodeSys01SCH06());
      values.add(pk.getProgressiveSCH06());


      // read from SCH09 table...
      Response res = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          ScheduledMachineriesVO.class,
          "Y",
          "N",
          context,
          gridParams,
          true
      );

      if (!res.isError()) {
        HashSet macCodes = new HashSet();
        ScheduledMachineriesVO vo = null;
        java.util.List rows = ((VOListResponse)res).getRows();
        for(int i=0;i<rows.size();i++) {
          vo = (ScheduledMachineriesVO)rows.get(i);
          macCodes.add(vo.getMachineryCodePro03SCH09());
        }

        // retrieve tasks defined in the call-out...
        sql =
            "select SCH13_CALL_OUT_MACHINERIES.MACHINERY_CODE_PRO03,SYS10_TRANSLATIONS.DESCRIPTION "+
            "from SCH13_CALL_OUT_MACHINERIES,SCH03_CALL_OUT_REQUESTS,PRO03_MACHINERIES,SYS10_TRANSLATIONS where "+
            "SCH03_CALL_OUT_REQUESTS.COMPANY_CODE_SYS01=SCH13_CALL_OUT_MACHINERIES.COMPANY_CODE_SYS01 and "+
            "SCH03_CALL_OUT_REQUESTS.CALL_OUT_CODE_SCH10=SCH13_CALL_OUT_MACHINERIES.CALL_OUT_CODE_SCH10 and "+
            "SCH13_CALL_OUT_MACHINERIES.COMPANY_CODE_SYS01=PRO03_MACHINERIES.COMPANY_CODE_SYS01 and "+
            "SCH13_CALL_OUT_MACHINERIES.MACHINERY_CODE_PRO03=PRO03_MACHINERIES.MACHINERY_CODE and "+
            "PRO03_MACHINERIES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
            "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
            "SCH03_CALL_OUT_REQUESTS.COMPANY_CODE_SYS01=? and "+
            "SCH03_CALL_OUT_REQUESTS.PROGRESSIVE_SCH06=?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1,serverLanguageId);
        pstmt.setString(2,pk.getCompanyCodeSys01SCH06());
        pstmt.setBigDecimal(3,pk.getProgressiveSCH06());
        ResultSet rset = pstmt.executeQuery();
        while(rset.next()) {
          vo = new ScheduledMachineriesVO();
          vo.setCompanyCodeSys01SCH09(pk.getCompanyCodeSys01SCH06());
          vo.setProgressiveSch06SCH09(pk.getProgressiveSCH06());
          vo.setMachineryCodePro03SCH09(rset.getString(1));
          vo.setDescriptionSYS10(rset.getString(2));
          if (!macCodes.contains(vo.getMachineryCodePro03SCH09()))
            rows.add(vo);
        }
        rset.close();
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching scheduled machineries list",ex);
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        pstmt.close();
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
