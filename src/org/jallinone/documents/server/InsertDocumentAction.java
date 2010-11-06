package org.jallinone.documents.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.documents.java.DetailDocumentVO;
import org.jallinone.system.server.JAIOUserSessionParameters;
import java.math.BigDecimal;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.system.progressives.server.ProgressiveUtils;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.system.java.ApplicationParametersVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.documents.java.DocumentPK;
import org.jallinone.documents.java.DocumentLinkVO;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;
import org.jallinone.system.progressives.server.CompanyProgressiveUtils;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to insert a new document in DOC14 table.</p>
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
public class InsertDocumentAction implements Action {

  private InsertDocumentLinkBean link = new InsertDocumentLinkBean();
  private InsertDocumentVersionBean ver = new InsertDocumentVersionBean();


  public InsertDocumentAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "insertDocument";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
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

      String companyCode = ((JAIOUserSessionParameters)userSessionPars).getCompanyBa().getCompaniesList("DOC14").get(0).toString();

      DetailDocumentVO vo = (DetailDocumentVO)inputPar;
      if (vo.getCompanyCodeSys01DOC14()==null)
        vo.setCompanyCodeSys01DOC14(companyCode);

      // generate progressive for document description...
      BigDecimal progressiveDOC14 = CompanyProgressiveUtils.getInternalProgressive(vo.getCompanyCodeSys01DOC14(),"DOC14_DOCUMENTS","PROGRESSIVE",conn);
      vo.setProgressiveDOC14(progressiveDOC14);

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC14","COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveDOC14","PROGRESSIVE");
      attribute2dbField.put("descriptionDOC14","DESCRIPTION");
      attribute2dbField.put("filenameDOC14","FILENAME");

      // insert into DOC14...
      Response resDOC14 = CustomizeQueryUtil.insertTable(
          conn,
          userSessionPars,
          vo,
          "DOC14_DOCUMENTS",
          attribute2dbField,
          "Y",
          "N",
          context,
          true,
          ApplicationConsts.ID_DOCUMENT // window identifier...
      );
      if (resDOC14.isError()) {
        conn.rollback();
        return resDOC14;
      }

      // create a document link with the specified tree level...
      DocumentLinkVO linkVO = new DocumentLinkVO();
      linkVO.setCompanyCodeSys01DOC17(vo.getCompanyCodeSys01DOC14());
      linkVO.setProgressiveDoc14DOC17(vo.getProgressiveDOC14());
      linkVO.setProgressiveHie01DOC17(vo.getProgressiveHie01DOC17());
      Response res = link.insertDocumentLink(
        conn,
        linkVO,
        userSessionPars,
        request,
        response,
        userSession,
        context
      );
      if (res.isError()) {
        conn.rollback();
        return res;
      }

      // insert the new document version...
      res = ver.insertDocumentVersion(
        conn,
        new DocumentPK(vo.getCompanyCodeSys01DOC14(),vo.getProgressiveDOC14()),
        vo.getDocument(),
        userSessionPars,
        request,
        response,
        userSession,
        context
      );
      if (res.isError()) {
        conn.rollback();
        return res;
      }

      Response answer = resDOC14;

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

      conn.commit();

      // fires the GenericEvent.AFTER_COMMIT event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        getRequestName(),
        GenericEvent.AFTER_COMMIT,
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while inserting a new document",ex);
      try {
        conn.rollback();
      }
      catch (Exception ex3) {
      }
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
