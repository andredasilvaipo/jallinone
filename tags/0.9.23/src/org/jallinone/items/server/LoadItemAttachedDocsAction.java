package org.jallinone.items.server;

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
import org.jallinone.items.java.ItemAttachedDocVO;
import org.jallinone.items.java.ItemPK;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch attached documents to an item from ITM05 table.</p>
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
public class LoadItemAttachedDocsAction implements Action {


  public LoadItemAttachedDocsAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadItemAttachedDocs";
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

      String sql =
          "select ITM05_ITEM_ATTACHED_DOCS.COMPANY_CODE_SYS01,ITM05_ITEM_ATTACHED_DOCS.PROGRESSIVE_DOC14,"+
          "ITM05_ITEM_ATTACHED_DOCS.ITEM_CODE_ITM01,DOC14_DOCUMENTS.DESCRIPTION,ITM05_ITEM_ATTACHED_DOCS.PROGRESSIVE_HIE01, "+
          "HIE01_LEVELS.PROGRESSIVE_HIE02 "+
          "from ITM05_ITEM_ATTACHED_DOCS,DOC14_DOCUMENTS,HIE01_LEVELS where "+
          "ITM05_ITEM_ATTACHED_DOCS.COMPANY_CODE_SYS01=DOC14_DOCUMENTS.COMPANY_CODE_SYS01 and "+
          "ITM05_ITEM_ATTACHED_DOCS.PROGRESSIVE_DOC14=DOC14_DOCUMENTS.PROGRESSIVE and "+
          "HIE01_LEVELS.PROGRESSIVE=ITM05_ITEM_ATTACHED_DOCS.PROGRESSIVE_HIE01 and "+
          "ITM05_ITEM_ATTACHED_DOCS.COMPANY_CODE_SYS01=? and "+
          "ITM05_ITEM_ATTACHED_DOCS.ITEM_CODE_ITM01=?";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01ITM05","ITM05_ITEM_ATTACHED_DOCS.COMPANY_CODE_SYS01");
      attribute2dbField.put("descriptionDOC14","DOC14_DOCUMENTS.DESCRIPTION");
      attribute2dbField.put("itemCodeItm01ITM05","ITM05_ITEM_ATTACHED_DOCS.ITEM_CODE_ITM01");
      attribute2dbField.put("progressiveDoc14ITM05","ITM05_ITEM_ATTACHED_DOCS.PROGRESSIVE_DOC14");
      attribute2dbField.put("progressiveHie01ITM05","ITM05_ITEM_ATTACHED_DOCS.PROGRESSIVE_HIE01");
      attribute2dbField.put("progressiveHie02HIE01","HIE01_LEVELS.PROGRESSIVE_HIE02");

      GridParams gridParams = (GridParams)inputPar;
      ItemPK pk = (ItemPK)gridParams.getOtherGridParams().get(ApplicationConsts.ITEM_PK);

      ArrayList values = new ArrayList();
      values.add(pk.getCompanyCodeSys01ITM01());
      values.add(pk.getItemCodeITM01());

      // read from ITM05 table...
      Response answer = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          ItemAttachedDocVO.class,
          "Y",
          "N",
          context,
          gridParams,
          true
      );

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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching item attached documents list",ex);
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
