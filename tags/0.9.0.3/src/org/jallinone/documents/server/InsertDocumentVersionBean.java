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
import java.math.BigDecimal;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.system.progressives.server.ProgressiveUtils;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Helper class class used to insert a new document version in DOC15 table.</p>
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
public class InsertDocumentVersionBean {


  public InsertDocumentVersionBean() {
  }


  /**
   * Insert new document version in DOC15 and save the document in the file system.
   * This method does not create or release connection and does not commit/rollback connection.
   */
  public final Response insertDocumentVersion(Connection conn,
                                       DocumentPK pk,
                                       byte[] document,
                                       UserSessionParameters userSessionPars,
                                       HttpServletRequest request,
                                       HttpServletResponse response,
                                       HttpSession userSession,
                                       ServletContext context) {
    PreparedStatement pstmt = null;
    try {

      // fires the GenericEvent.CONNECTION_CREATED event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "InsertDocumentVersionBean.insertDocumentVersion",
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

      // calculate the next document version...
      DocumentVersionVO vo = new DocumentVersionVO();
      pstmt = conn.prepareStatement("select max(VERSION) from DOC15_DOCUMENT_VERSIONS where COMPANY_CODE_SYS01=? and PROGRESSIVE_DOC14=?");
      pstmt.setString(1,pk.getCompanyCodeSys01DOC14());
      pstmt.setBigDecimal(2,pk.getProgressiveDOC14());
      ResultSet rset = pstmt.executeQuery();
      if (rset.next())
        vo.setVersionDOC15(rset.getBigDecimal(1));
      rset.close();
      if (vo.getVersionDOC15()==null)
        vo.setVersionDOC15(new BigDecimal(0));

      vo.setVersionDOC15(vo.getVersionDOC15().add(new BigDecimal(1)));
      vo.setCompanyCodeSys01DOC15(pk.getCompanyCodeSys01DOC14());
      vo.setProgressiveDoc14DOC15(pk.getProgressiveDOC14());
      vo.setCreateDateDOC15(new java.sql.Timestamp(System.currentTimeMillis()));
      vo.setCreateUsernameDOC15(userSessionPars.getUsername());

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC15","COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveDoc14DOC15","PROGRESSIVE_DOC14");
      attribute2dbField.put("createDateDOC15","CREATE_DATE");
      attribute2dbField.put("createUsernameDOC15","CREATE_USERNAME");
      attribute2dbField.put("versionDOC15","VERSION");

      // insert into DOC15...
      Response res = QueryUtil.insertTable(
          conn,
          userSessionPars,
          vo,
          "DOC15_DOCUMENT_VERSIONS",
          attribute2dbField,
          "Y",
          "N",
          context,
          true
      );

      if (res.isError())
        return res;

      // save the document in the file system...
      String appPath = (String)((JAIOUserSessionParameters)userSessionPars).getAppParams().get(ApplicationConsts.DOC_PATH);
      appPath = appPath.replace('\\','/');
      if (!appPath.endsWith("/"))
        appPath += "/";
      if (!new File(appPath).isAbsolute()) {
        // relative path (to "WEB-INF/classes/" folder)
        appPath = this.getClass().getResource("/").getPath()+appPath;
      }
      new File(appPath).mkdirs();
      FileOutputStream out = new FileOutputStream(appPath+"DOC"+vo.getProgressiveDoc14DOC15()+"_"+vo.getVersionDOC15());
      out.write(document);
      out.close();

      Response answer = new VOResponse(vo);

      // fires the GenericEvent.BEFORE_COMMIT event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "InsertDocumentVersionBean.insertDocumentVersion",
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
      Logger.error(userSessionPars.getUsername(), this.getClass().getName(),
                   "insertDocumentVersion", "Error while inserting a new document version", ex);
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        pstmt.close();
      }
      catch (Exception ex2) {
      }
    }

  }



}

