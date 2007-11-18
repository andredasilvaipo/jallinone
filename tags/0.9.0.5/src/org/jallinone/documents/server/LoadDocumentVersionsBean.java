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
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Helper class used to retrieve document versions from DOC15 table.</p>
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
public class LoadDocumentVersionsBean {


  public LoadDocumentVersionsBean() {
  }


  /**
   * Retrieve the list of DocumentVersionVO objects, based on the specified DocumentPK.
   * This method does not open or clone a connection.
   */
  public final Response getDocumentVersions(Connection conn,DocumentPK pk,GridParams gridParams,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    try {

      // fires the GenericEvent.CONNECTION_CREATED event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "LoadDocumentVersionsBean.getDocumentVersions",
        GenericEvent.CONNECTION_CREATED,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        pk,
        null
      ));


      String sql =
          "select DOC15_DOCUMENT_VERSIONS.COMPANY_CODE_SYS01,DOC15_DOCUMENT_VERSIONS.PROGRESSIVE_DOC14,DOC15_DOCUMENT_VERSIONS.VERSION,"+
          "DOC15_DOCUMENT_VERSIONS.CREATE_DATE,DOC15_DOCUMENT_VERSIONS.CREATE_USERNAME from "+
          "DOC15_DOCUMENT_VERSIONS where "+
          "DOC15_DOCUMENT_VERSIONS.COMPANY_CODE_SYS01=? and "+
          "DOC15_DOCUMENT_VERSIONS.PROGRESSIVE_DOC14=?";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC15","DOC15_DOCUMENT_VERSIONS.COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveDoc14DOC15","DOC15_DOCUMENT_VERSIONS.PROGRESSIVE_DOC14");
      attribute2dbField.put("versionDOC15","DOC15_DOCUMENT_VERSIONS.VERSION");
      attribute2dbField.put("createDateDOC15","DOC15_DOCUMENT_VERSIONS.CREATE_DATE");
      attribute2dbField.put("createUsernameDOC15","DOC15_DOCUMENT_VERSIONS.CREATE_USERNAME");

      ArrayList values = new ArrayList();
      values.add(pk.getCompanyCodeSys01DOC14());
      values.add(pk.getProgressiveDOC14());

      // read from DOC17 table...
      Response answer = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          DocumentVersionVO.class,
          "Y",
          "N",
          context,
          gridParams,
          true
      );

      // fires the GenericEvent.BEFORE_COMMIT event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "LoadDocumentVersionsBean.getDocumentVersions",
        GenericEvent.BEFORE_COMMIT,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        pk,
        answer
      ));

  return answer;

    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"getDocumentVersions","Error while fetching document versions list",ex);
      return new ErrorResponse(ex.getMessage());
    }
    finally {
    }

  }



}
