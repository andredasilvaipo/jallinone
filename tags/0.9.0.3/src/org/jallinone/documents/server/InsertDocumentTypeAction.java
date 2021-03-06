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
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to insert a new document type in DOC16 table.</p>
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
public class InsertDocumentTypeAction implements Action {


  public InsertDocumentTypeAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "insertDocumentType";
  }

  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,
                                       UserSessionParameters userSessionPars,
                                       HttpServletRequest request,
                                       HttpServletResponse response,
                                       HttpSession userSession,
                                       ServletContext context) {
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

      DocumentTypeVO vo = (DocumentTypeVO)inputPar;
      vo.setEnabledDOC16("Y");

      String companyCode = ((JAIOUserSessionParameters)userSessionPars).getCompanyBa().getCompaniesList("DOC16").get(0).toString();
      if (vo.getCompanyCodeSys01DOC16()==null)
        vo.setCompanyCodeSys01DOC16(companyCode);

      // generate PROGRESSIVE_HIE02 value...
      stmt = conn.createStatement();
      BigDecimal progressiveHIE02 = ProgressiveUtils.getInternalProgressive("HIE02_HIERARCHIES","PROGRESSIVE",conn);
      BigDecimal progressiveHIE01 = TranslationUtils.insertTranslations(vo.getDescriptionSYS10(),conn);
      stmt.execute("INSERT INTO HIE02_HIERARCHIES(PROGRESSIVE,ENABLED) VALUES("+progressiveHIE02+",'Y')");
      stmt.execute("INSERT INTO HIE01_LEVELS(PROGRESSIVE,PROGRESSIVE_HIE02,LEVEL,ENABLED) VALUES("+progressiveHIE01+","+progressiveHIE02+",0,'Y')");
      stmt.execute("UPDATE HIE02_HIERARCHIES SET PROGRESSIVE_HIE01="+progressiveHIE01+" WHERE PROGRESSIVE="+progressiveHIE02);
      vo.setProgressiveHie02DOC16(progressiveHIE02);
      vo.setProgressiveSys10DOC16(progressiveHIE01);

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC16","COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveHie02DOC16","PROGRESSIVE_HIE02");
      attribute2dbField.put("progressiveSys10DOC16","PROGRESSIVE_SYS10");
      attribute2dbField.put("enabledDOC16","ENABLED");

      // insert into DOC16...
      Response res = QueryUtil.insertTable(
          conn,
          userSessionPars,
          vo,
          "DOC16_DOC_HIERARCHY",
          attribute2dbField,
          "Y",
          "N",
          context,
          true
      );


      Response answer = new VOResponse(vo);

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
      Logger.error(userSessionPars.getUsername(), this.getClass().getName(),
                   "executeCommand", "Error while inserting a new documents", ex);
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

