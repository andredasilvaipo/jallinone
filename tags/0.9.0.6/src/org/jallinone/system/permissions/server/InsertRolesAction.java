package org.jallinone.system.permissions.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.system.permissions.java.RoleVO;
import java.math.BigDecimal;
import org.jallinone.system.progressives.server.ProgressiveUtils;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to insert new roles in SYS04 table.</p>
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
public class InsertRolesAction implements Action {


  public InsertRolesAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "insertRoles";
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
      RoleVO vo = null;

      BigDecimal oldProgressiveSYS04 = null;
      BigDecimal progressiveSYS04 = null;
      BigDecimal progressiveSys10SYS04 = null;
      ArrayList list = (ArrayList)inputPar;

      for(int i=0;i<list.size();i++) {
        vo = (RoleVO)list.get(i);

        // used for copy operation...
        oldProgressiveSYS04 = vo.getProgressiveSYS04();

        // generate new progressive for SYS04...
        progressiveSYS04 = ProgressiveUtils.getInternalProgressive("SYS04_ROLES","PROGRESSIVE",conn);
        vo.setProgressiveSYS04(progressiveSYS04);

        // insert record in SYS10...
        progressiveSys10SYS04 = TranslationUtils.insertTranslations(vo.getDescriptionSYS10(),conn);
        vo.setProgressiveSys10SYS04(progressiveSys10SYS04);

        // insert record in SYS04...
        pstmt = conn.prepareStatement(
            "insert into SYS04_ROLES(PROGRESSIVE,PROGRESSIVE_SYS10,ENABLED) VALUES(?,?,'Y')"
        );
        pstmt.setBigDecimal(1,vo.getProgressiveSYS04());
        pstmt.setBigDecimal(2,vo.getProgressiveSys10SYS04());
        pstmt.execute();
        pstmt.close();

        // link the new role to the current user...
        pstmt = conn.prepareStatement(
            "insert into SYS14_USER_ROLES(USERNAME_SYS03,PROGRESSIVE_SYS04) VALUES(?,?)"
        );
        pstmt.setString(1,userSessionPars.getUsername());
        pstmt.setBigDecimal(2,progressiveSYS04);
        pstmt.execute();

        if (!userSessionPars.getUsername().toUpperCase().equals("ADMIN")) {
          pstmt.close();
          // link the new role to the ADMIN user...
          pstmt = conn.prepareStatement(
              "insert into SYS14_USER_ROLES(USERNAME_SYS03,PROGRESSIVE_SYS04) VALUES('ADMIN',?)"
          );
          pstmt.setBigDecimal(1,progressiveSYS04);
          pstmt.execute();
        }

        // update user roles collection...
        ((JAIOUserSessionParameters)userSessionPars).getUserRoles().put(progressiveSYS04,vo.getDescriptionSYS10());

        if (oldProgressiveSYS04!=null) {
          // duplicate all old progressive settings...
          pstmt.close();
          pstmt = conn.prepareStatement(
              "insert into SYS07_ROLE_FUNCTIONS(PROGRESSIVE_SYS04,FUNCTION_CODE_SYS06,CAN_INS,CAN_UPD,CAN_DEL) "+
              "select ?,FUNCTION_CODE_SYS06,CAN_INS,CAN_UPD,CAN_DEL from SYS07_ROLE_FUNCTIONS where "+
              "PROGRESSIVE_SYS04=?"
          );
          pstmt.setBigDecimal(1,progressiveSYS04);
          pstmt.setBigDecimal(2,oldProgressiveSYS04);
          pstmt.execute();
          pstmt.close();

          pstmt = conn.prepareStatement(
              "insert into SYS02_COMPANIES_ACCESS(PROGRESSIVE_SYS04,FUNCTION_CODE_SYS06,CAN_INS,CAN_UPD,CAN_DEL,COMPANY_CODE_SYS01) "+
              "select ?,FUNCTION_CODE_SYS06,CAN_INS,CAN_UPD,CAN_DEL,COMPANY_CODE_SYS01 from SYS02_COMPANIES_ACCESS where "+
              "PROGRESSIVE_SYS04=?"
          );
          pstmt.setBigDecimal(1,progressiveSYS04);
          pstmt.setBigDecimal(2,oldProgressiveSYS04);
          pstmt.execute();
          pstmt.close();

        }
      }


      Response answer = new VOListResponse(list,false,list.size());

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
                   "executeCommand", "Error while inserting new roles", ex);
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


  /**
   * Replace the specified pattern with the new one.
   * @param b sql script
   * @param oldPattern pattern to replace
   * @param newPattern new pattern
   * @return sql script with substitutions
   */
  private StringBuffer replace(StringBuffer b,String oldPattern,String newPattern) {
    int i = -1;
    while((i=b.indexOf(oldPattern))!=-1) {
      b.replace(i,i+oldPattern.length(),newPattern);
    }
    return b;
  }


}

