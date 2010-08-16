package org.jallinone.documents.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.documents.java.*;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Helper class used to (phisically) delete existing document versions from DOC15 and
 * delete files from file system too.</p>
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
public class DeleteDocumentVersionsBean {


  public DeleteDocumentVersionsBean() {
  }


  /**
   * Phisically delete records from DOC15 and also delete files from file system.
   * This method does not create or release connection and does not commit or rollback the connection.
   */
  public final Response deleteDocumentVersions(Connection conn,ArrayList documentVersionVOs,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    Statement stmt = null;
    try {

      // fires the GenericEvent.CONNECTION_CREATED event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "DeleteDocumentVersionsBean.deleteDocumentVersions",
        GenericEvent.CONNECTION_CREATED,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        documentVersionVOs,
        null
      ));

      stmt = conn.createStatement();

      DocumentVersionVO vo = null;
      File file = null;
      for(int i=0;i<documentVersionVOs.size();i++) {
        // phisically delete the records in DOC15...
        vo = (DocumentVersionVO)documentVersionVOs.get(i);
        stmt.execute("delete from DOC15_DOCUMENT_VERSIONS where "+
                     "COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01DOC15()+"' and "+
                     "PROGRESSIVE_DOC14="+vo.getProgressiveDoc14DOC15()+" and "+
                     "VERSION="+vo.getVersionDOC15());

        // delete file from file system...
        String appPath = (String)((JAIOUserSessionParameters)userSessionPars).getAppParams().get(ApplicationConsts.DOC_PATH);
        appPath = appPath.replace('\\','/');
        if (!appPath.endsWith("/"))
          appPath += "/";
        if (!new File(appPath).isAbsolute()) {
          // relative path (to "WEB-INF/classes/" folder)
          appPath = this.getClass().getResource("/").getPath().replaceAll("%20"," ")+appPath;
        }
        file = new File(appPath+"DOC"+vo.getProgressiveDoc14DOC15()+"_"+vo.getVersionDOC15());
        file.delete();
      }

      Response answer = new VOResponse(new Boolean(true));

      // fires the GenericEvent.BEFORE_COMMIT event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "DeleteDocumentVersionsBean.deleteDocumentVersions",
        GenericEvent.BEFORE_COMMIT,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        documentVersionVOs,
        answer
      ));

      return answer;
    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"deleteDocumentVersions","Error while deleting existing document versions",ex);
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
