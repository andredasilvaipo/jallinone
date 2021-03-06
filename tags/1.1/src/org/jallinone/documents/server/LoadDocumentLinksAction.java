package org.jallinone.documents.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.documents.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.commons.server.CustomizeQueryUtil;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch documents links from DOC17 table.</p>
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
public class LoadDocumentLinksAction implements Action {


  public LoadDocumentLinksAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadDocumentLinks";
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
          "select DOC17_DOCUMENT_LINKS.COMPANY_CODE_SYS01,DOC17_DOCUMENT_LINKS.PROGRESSIVE_DOC14,DOC17_DOCUMENT_LINKS.PROGRESSIVE_HIE01,"+
          "HIE01_LEVELS.PROGRESSIVE_HIE02,SYS10_TRANSLATIONS.DESCRIPTION "+
          " from DOC17_DOCUMENT_LINKS,SYS10_TRANSLATIONS,HIE01_LEVELS where "+
          "DOC17_DOCUMENT_LINKS.PROGRESSIVE_HIE01=HIE01_LEVELS.PROGRESSIVE and "+
          "HIE01_LEVELS.PROGRESSIVE=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "DOC17_DOCUMENT_LINKS.COMPANY_CODE_SYS01=? and "+
          "DOC17_DOCUMENT_LINKS.PROGRESSIVE_DOC14=?";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC17","DOC17_DOCUMENT_LINKS.COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveDoc14DOC17","DOC17_DOCUMENT_LINKS.PROGRESSIVE_DOC14");
      attribute2dbField.put("progressiveHie01DOC17","DOC17_DOCUMENT_LINKS.PROGRESSIVE_HIE01");
      attribute2dbField.put("progressiveHIE02","HIE01_LEVELS.PROGRESSIVE_HIE02");
      attribute2dbField.put("levelDescriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");

      GridParams gridParams = (GridParams)inputPar;
      DocumentPK pk = (DocumentPK)gridParams.getOtherGridParams().get(ApplicationConsts.DOCUMENT_PK);

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(pk.getCompanyCodeSys01DOC14());
      values.add(pk.getProgressiveDOC14());

      // read from DOC17 table...
      Response answer = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          DocumentLinkVO.class,
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching documents links list",ex);
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
