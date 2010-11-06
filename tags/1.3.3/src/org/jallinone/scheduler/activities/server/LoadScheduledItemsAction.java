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
import org.jallinone.commons.java.ApplicationConsts;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.scheduler.activities.java.ScheduledActivityPK;
import org.jallinone.scheduler.activities.java.ScheduledItemVO;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch scheduled items from SCH15 table.</p>
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
public class LoadScheduledItemsAction implements Action {


  public LoadScheduledItemsAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadScheduledItems";
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
          "select SCH15_SCHEDULED_ITEMS.ITEM_CODE_ITM01,SCH15_SCHEDULED_ITEMS.COMPANY_CODE_SYS01,ITM01_ITEMS.PROGRESSIVE_HIE02,"+
          "SCH15_SCHEDULED_ITEMS.PROGRESSIVE_SCH06,SYS10_TRANSLATIONS.DESCRIPTION,SCH15_SCHEDULED_ITEMS.QTY "+
          "from SCH15_SCHEDULED_ITEMS,ITM01_ITEMS,SYS10_TRANSLATIONS where "+
          "SCH15_SCHEDULED_ITEMS.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
          "SCH15_SCHEDULED_ITEMS.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and "+
          "ITM01_ITEMS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "SCH15_SCHEDULED_ITEMS.COMPANY_CODE_SYS01=? and "+
          "SCH15_SCHEDULED_ITEMS.PROGRESSIVE_SCH06=?";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SCH15","SCH15_SCHEDULED_ITEMS.COMPANY_CODE_SYS01");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("progressiveSch06SCH15","SCH15_SCHEDULED_ITEMS.PROGRESSIVE_SCH06");
      attribute2dbField.put("itemCodeItm01SCH15","SCH15_SCHEDULED_ITEMS.ITEM_CODE_ITM01");
      attribute2dbField.put("qtySCH15","SCH15_SCHEDULED_ITEMS.QTY");
      attribute2dbField.put("progressiveHie02ITM01","ITM01_ITEMS.PROGRESSIVE_HIE02");

      GridParams gridParams = (GridParams)inputPar;
      ScheduledActivityPK pk = (ScheduledActivityPK)gridParams.getOtherGridParams().get(ApplicationConsts.SCHEDULED_ACTIVITY_PK);

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(pk.getCompanyCodeSys01SCH06());
      values.add(pk.getProgressiveSCH06());


      // read from SCH15 table...
      Response res = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          ScheduledItemVO.class,
          "Y",
          "N",
          context,
          gridParams,
          true
      );

      if (!res.isError()) {
        HashSet itemCodes = new HashSet();
        ScheduledItemVO vo = null;
        java.util.List rows = ((VOListResponse)res).getRows();
        for(int i=0;i<rows.size();i++) {
          vo = (ScheduledItemVO)rows.get(i);
          itemCodes.add(vo.getItemCodeItm01SCH15());
        }

        // retrieve tasks defined in the call-out...
        sql =
            "select SCH14_CALL_OUT_ITEMS.ITEM_CODE_ITM01,SYS10_TRANSLATIONS.DESCRIPTION,ITM01_ITEMS.PROGRESSIVE_HIE02 "+
            "from SCH14_CALL_OUT_ITEMS,SCH03_CALL_OUT_REQUESTS,ITM01_ITEMS,SYS10_TRANSLATIONS where "+
            "SCH03_CALL_OUT_REQUESTS.COMPANY_CODE_SYS01=SCH14_CALL_OUT_ITEMS.COMPANY_CODE_SYS01 and "+
            "SCH03_CALL_OUT_REQUESTS.CALL_OUT_CODE_SCH10=SCH14_CALL_OUT_ITEMS.CALL_OUT_CODE_SCH10 and "+
            "SCH14_CALL_OUT_ITEMS.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
            "SCH14_CALL_OUT_ITEMS.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and "+
            "ITM01_ITEMS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
            "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
            "SCH03_CALL_OUT_REQUESTS.COMPANY_CODE_SYS01=? and "+
            "SCH03_CALL_OUT_REQUESTS.PROGRESSIVE_SCH06=?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1,serverLanguageId);
        pstmt.setString(2,pk.getCompanyCodeSys01SCH06());
        pstmt.setBigDecimal(3,pk.getProgressiveSCH06());
        ResultSet rset = pstmt.executeQuery();
        while(rset.next()) {
          vo = new ScheduledItemVO();
          vo.setCompanyCodeSys01SCH15(pk.getCompanyCodeSys01SCH06());
          vo.setProgressiveSch06SCH15(pk.getProgressiveSCH06());
          vo.setItemCodeItm01SCH15(rset.getString(1));
          vo.setDescriptionSYS10(rset.getString(2));
          vo.setProgressiveHie02ITM01(rset.getBigDecimal(3));
          if (!itemCodes.contains(vo.getItemCodeItm01SCH15()))
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching scheduled items list",ex);
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
