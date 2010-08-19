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
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;



/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to (phisically) delete an existing subject hierarchy and related links (tables REG08 and REG16).</p>
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
public class DeleteSubjectHierarchyAction implements Action {


  public DeleteSubjectHierarchyAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "deleteSubjectHierarchy";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
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
      stmt = conn.createStatement();

      java.util.List list =  (ArrayList)inputPar;
      SubjectHierarchyVO vo = null;
      for(int i=0;i<list.size();i++) {
        vo = (SubjectHierarchyVO)list.get(i);

        // phisically delete the record in SYS10...
        TranslationUtils.deleteTranslations(vo.getProgressiveSys10REG08(),conn);

        // phisically delete records in REG16...
        stmt.execute(
            "delete REG16_SUBJECT_LINKS where COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01REG08()+"' and "+
            "PROGRESSIVE_HIE01 in (SELECT PROGRESSIVE from HIE01_LEVELS where PROGRESSIVE_HIE02="+vo.getProgressiveHie02REG08()+")"
        );

        // phisically delete the record in REG08...
        stmt.execute(
            "delete REG08_SUBJECT_HIERARCHIES where COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01REG08()+"' and "+
            "SUBJECT_TYPE='"+vo.getSubjectTypeREG08()+"' and PROGRESSIVE_SYS10="+vo.getProgressiveSys10REG08()
        );

        // phisically delete records in HIE01...
        stmt.execute(
            "update HIE02_HIERARCHIES set PROGRESSIVE_HIE01=null where PROGRESSIVE="+vo.getProgressiveHie02REG08()
        );
        stmt.execute(
            "delete HIE01_LEVELS where PROGRESSIVE_HIE02="+vo.getProgressiveHie02REG08()
        );

        // phisically delete record in HIE02...
        stmt.execute(
            "delete HIE02_HIERARCHIES where PROGRESSIVE="+vo.getProgressiveHie02REG08()
        );

      }

      Response answer = new VOResponse(new Boolean(true));

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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while deleting an existing subject hierarchy",ex);
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
