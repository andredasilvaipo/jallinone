package org.jallinone.subjects.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.subjects.java.*;
import java.math.BigDecimal;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.system.progressives.server.ProgressiveUtils;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;



/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to insert a new subject hierarchy in REG08 table.</p>
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
public class InsertSubjectHierarchyAction implements Action {


  public InsertSubjectHierarchyAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "insertSubjectHierarchy";
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
      SubjectHierarchyVO vo = (SubjectHierarchyVO)inputPar;

      // insert record in SYS10...
      BigDecimal progressiveSYS10 = TranslationUtils.insertTranslations(vo.getDescriptionSYS10(),conn);
      vo.setProgressiveSys10REG08(progressiveSYS10);

      // insert into HIE02...
      vo.setProgressiveHie02REG08( ProgressiveUtils.getInternalProgressive("HIE02_HIERARCHIES","PROGRESSIVE",conn) );
      pstmt = conn.prepareStatement("insert into HIE02_HIERARCHIES(PROGRESSIVE,ENABLED) values(?,'Y')");
      pstmt.setBigDecimal(1,vo.getProgressiveHie02REG08());
      pstmt.execute();
      pstmt.close();

      // insert into HIE01...
      BigDecimal progressiveHIE01 = TranslationUtils.insertTranslations(vo.getDescriptionSYS10(),conn);
      pstmt = conn.prepareStatement("insert into HIE01_LEVELS(PROGRESSIVE,PROGRESSIVE_HIE02,LEV,ENABLED) values(?,?,0,'Y')");
      pstmt.setBigDecimal(1,progressiveHIE01);
      pstmt.setBigDecimal(2,vo.getProgressiveHie02REG08());
      pstmt.execute();
      pstmt.close();
      pstmt = conn.prepareStatement("update HIE02_HIERARCHIES set PROGRESSIVE_HIE01=? where PROGRESSIVE=?");
      pstmt.setBigDecimal(1,progressiveHIE01);
      pstmt.setBigDecimal(2,vo.getProgressiveHie02REG08());
      pstmt.execute();

      // insert into REG08...
      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01REG08","COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveSys10REG08","PROGRESSIVE_SYS10");
      attribute2dbField.put("subjectTypeREG08","SUBJECT_TYPE");
      attribute2dbField.put("progressiveHie02REG08","PROGRESSIVE_HIE02");

      Response res = QueryUtil.insertTable(
          conn,
          userSessionPars,
          vo,
          "REG08_SUBJECT_HIERARCHIES",
          attribute2dbField,
          "Y",
          "N",
          context,
          true
      );

      if (res.isError()) {
        conn.rollback();
        return res;
      }

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
                   "executeCommand", "Error while inserting a new SubjectHierarchy", ex);
      try {
        conn.rollback();
      }
      catch (Exception ex3) {
      }
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

