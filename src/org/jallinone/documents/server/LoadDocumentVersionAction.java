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
 * <p>Description: Action class used to fetch a specific document version from the file system.
 * This class temporally stores the document retrieved (byte[]) in the user session so that the client can
 * requests it via HTTP protocol and shows the document in a web page</p>
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
public class LoadDocumentVersionAction implements Action {

  private LoadDocumentBean loadDoc = new LoadDocumentBean();


  public LoadDocumentVersionAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadDocumentVersion";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();
    Connection conn = null;
    Statement stmt = null;
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


      DocumentVersionVO vo = (DocumentVersionVO)inputPar;

      // retrieve document file name...
      Response res = loadDoc.loadDocument(
        conn,
        new DocumentPK(vo.getCompanyCodeSys01DOC15(),vo.getProgressiveDoc14DOC15()),
        userSessionPars,
        request,
        response,
        userSession,
        context
      );
      if (res.isError())
        return res;

      // retrieve file extension...
      DetailDocumentVO docVO = (DetailDocumentVO)((VOResponse)res).getVo();

      // read file from file system...
      String appPath = (String)((JAIOUserSessionParameters)userSessionPars).getAppParams().get(ApplicationConsts.DOC_PATH);
      appPath = appPath.replace('\\','/');
      if (!appPath.endsWith("/"))
        appPath += "/";
      if (!new File(appPath).isAbsolute()) {
        // relative path (to "WEB-INF/classes/" folder)
        appPath = this.getClass().getResource("/").getPath()+appPath;
      }
      File file = new File(appPath+"DOC"+vo.getProgressiveDoc14DOC15()+"_"+vo.getVersionDOC15());
      FileInputStream fis = new FileInputStream(file);
      byte[] doc = new byte[(int)file.length()];
      fis.read(doc);
      fis.close();

      // store in application session the document...
      String docId = System.currentTimeMillis()+"_"+docVO.getFilenameDOC14().toLowerCase();
      context.setAttribute(docId,doc);

      Response answer = new VOResponse(docId);

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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching an existing document version",ex);
      try {
        conn.rollback();
      }
      catch (Exception ex3) {
      }
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        stmt.close();
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
