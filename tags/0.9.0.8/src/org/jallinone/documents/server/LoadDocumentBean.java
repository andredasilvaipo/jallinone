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
import org.jallinone.system.java.ApplicationParametersVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Helper class used to fetch a specific document from DOC14 table.</p>
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
public class LoadDocumentBean {


  public LoadDocumentBean() {
  }


  /**
   * Retrieve DetailDocumentVO object from the specified DocumentPK value.
   * This method does not create or release connection.
   */
  public final Response loadDocument(Connection conn,DocumentPK pk,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    Statement stmt = null;
    try {

      // fires the GenericEvent.CONNECTION_CREATED event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "LoadDocumentBean.loadDocument",
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

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC14","DOC14_DOCUMENTS.COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveDOC14","DOC14_DOCUMENTS.PROGRESSIVE");
      attribute2dbField.put("descriptionDOC14","DOC14_DOCUMENTS.DESCRIPTION");
      attribute2dbField.put("filenameDOC14","DOC14_DOCUMENTS.FILENAME");

      HashSet pkAttributes = new HashSet();
      pkAttributes.add("companyCodeSys01DOC14");
      pkAttributes.add("progressiveDOC14");

      String baseSQL =
          "select "+
          "DOC14_DOCUMENTS.COMPANY_CODE_SYS01,DOC14_DOCUMENTS.PROGRESSIVE,DOC14_DOCUMENTS.DESCRIPTION,DOC14_DOCUMENTS.FILENAME "+
          " from "+
          "DOC14_DOCUMENTS "+
          " where "+
          "DOC14_DOCUMENTS.COMPANY_CODE_SYS01=? and "+
          "DOC14_DOCUMENTS.PROGRESSIVE=?";

      ArrayList values = new ArrayList();
      values.add(pk.getCompanyCodeSys01DOC14());
      values.add(pk.getProgressiveDOC14());

      // read from DOC14 table...
      Response res = CustomizeQueryUtil.getQuery(
          conn,
          userSessionPars,
          baseSQL,
          values,
          attribute2dbField,
          DetailDocumentVO.class,
          "Y",
          "N",
          context,
          true,
          ApplicationConsts.ID_DOCUMENT // window identifier...
      );

      Response answer = res;

      // fires the GenericEvent.BEFORE_COMMIT event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "LoadDocumentBean.loadDocument",
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"loadDocument","Error while fetching an existing document",ex);
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
