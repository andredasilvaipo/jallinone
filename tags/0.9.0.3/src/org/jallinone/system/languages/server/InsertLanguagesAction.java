package org.jallinone.system.languages.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.system.languages.java.*;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to insert new languages in SYS09 table.</p>
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
public class InsertLanguagesAction implements Action {


  public InsertLanguagesAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "insertLanguages";
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
      LanguageVO vo = null;
      StringBuffer sql = new StringBuffer("");
      BufferedReader br = null;
      ResultSet rset = null;

      ArrayList list = (ArrayList)inputPar;
      pstmt = conn.prepareStatement(
          "insert into SYS09_LANGUAGES(LANGUAGE_CODE,DESCRIPTION,CLIENT_LANGUAGE_CODE,CREATE_DATE,ENABLED) VALUES(?,?,?,?,'Y')"
      );

      for(int i=0;i<list.size();i++) {
        vo = (LanguageVO)list.get(i);

        // insert record in SYS01...
        pstmt.setString(1,vo.getLanguageCodeSYS09());
        pstmt.setString(2,vo.getDescriptionSYS09());
        pstmt.setString(3,vo.getClientLanguageCodeSYS09());
        pstmt.setDate(4,new java.sql.Date(System.currentTimeMillis()));
        pstmt.execute();

        // insert translations in SYS10...
        stmt = conn.createStatement();
        sql.delete(0,sql.length());
        br = new BufferedReader(new InputStreamReader(new FileInputStream(
            this.getClass().getResource("/").getPath() + "inssql_"+vo.getClientLanguageCodeSYS09()+".ini"
        )));
        String line = null;
        while ( (line = br.readLine()) != null) {
          sql.append(line);
          if (line.endsWith(";")) {
            if (sql.indexOf(":LANGUAGE_CODE") != -1) {
              sql = replace(sql, ":LANGUAGE_CODE",
                            "'" + vo.getLanguageCodeSYS09() + "'");
            }
            if (sql.toString().trim().length()>0) {
              stmt.execute(sql.toString().substring(0,sql.length() - 1));
            }
            sql.delete(0, sql.length());
          }

        }
        br.close();

        // insert all other translations...
        rset = stmt.executeQuery("select LANGUAGE_CODE from SYS09_LANGUAGES where ENABLED='Y' order by CREATE_DATE ASC");
        String oldLangCode = null;
        if (rset.next())
          oldLangCode = rset.getString(1);
        rset.close();
        if (oldLangCode!=null)
          stmt.execute(
            "insert into SYS10_TRANSLATIONS(PROGRESSIVE,DESCRIPTION,LANGUAGE_CODE) " +
            "select PROGRESSIVE,DESCRIPTION,'" + vo.getLanguageCodeSYS09() +
            "' from SYS10_TRANSLATIONS where LANGUAGE_CODE='"+oldLangCode+"' and not PROGRESSIVE in "+
            "(select PROGRESSIVE from SYS10_TRANSLATIONS where LANGUAGE_CODE='"+vo.getLanguageCodeSYS09()+"')"
          );
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
                   "executeCommand", "Error while inserting new languages", ex);
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

