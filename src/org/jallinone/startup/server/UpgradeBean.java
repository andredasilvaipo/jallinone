package org.jallinone.startup.server;

import org.openswing.swing.server.*;
import org.openswing.swing.message.receive.java.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;
import org.jallinone.startup.java.DbConnVO;
import java.util.Properties;
import org.openswing.swing.logger.server.Logger;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import java.sql.*;
import java.util.ArrayList;
import org.jallinone.commons.java.ApplicationConsts;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Class invoked at startup in order to check for database updates to apply.</p>
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
public class UpgradeBean {


  public UpgradeBean() {
  }


  public final void maybeUpgradeDB(ServletContext context) {
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    try {
      conn = ConnectionManager.getConnection(context);
      pstmt = conn.prepareStatement("SELECT VALUE FROM SYS11_APPLICATION_PARS WHERE PARAM_CODE='VERSION'");
      rset = pstmt.executeQuery();
      int version = 1;
      if (rset.next())
        version = Integer.parseInt(rset.getString(1));
      rset.close();
      pstmt.close();

      if (version==ApplicationConsts.DB_VERSION)
        return;

      // retrieve supported languages...
      pstmt = conn.prepareStatement("SELECT DISTINCT CLIENT_LANGUAGE_CODE FROM SYS09_LANGUAGES WHERE ENABLED='Y'");
      rset = pstmt.executeQuery();
      ArrayList supportedLanguages = new ArrayList();
      while (rset.next())
        supportedLanguages.add(rset.getString(1));
      rset.close();
      pstmt.close();


      DbConnVO vo = loadProperties();

      SQLExecutionBean executer = new SQLExecutionBean();

      for(int i=version+1;i<=ApplicationConsts.DB_VERSION;i++) {
        // apply SQL script for database upgrading...

        Logger.debug("NONAME",this.getClass().getName(),"maybeUpgradeDB","Applying SQL script 'defsql"+i+".ini'");

        executer.executeSQL(conn,vo,"defsql"+i+".ini");
        for(int k=0;k<supportedLanguages.size();k++) {
          Logger.debug("NONAME",this.getClass().getName(),"maybeUpgradeDB","Applying SQL script 'inssql"+i+"_"+supportedLanguages.get(k)+".ini'");
          executer.executeSQL(conn,vo,"inssql"+i+"_"+supportedLanguages.get(k)+".ini");
        }
      }


      // update db version value...
      pstmt = conn.prepareStatement("UPDATE SYS11_APPLICATION_PARS SET VALUE=? WHERE PARAM_CODE='VERSION'");
      pstmt.setString(1,String.valueOf(ApplicationConsts.DB_VERSION));
      pstmt.execute();
      pstmt.close();

      conn.commit();
    }
    catch (Throwable ex) {
      try {
        conn.rollback();
      }
      catch (Exception ex2) {
      }
      Logger.error("NONAME",this.getClass().getName(),"maybeUpgradeDB","Error while checking for database version",ex);
    }
    finally {
      try {
        rset.close();
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
   * Load "pooler.ini" file.
   */
  private DbConnVO loadProperties() {
    DbConnVO vo = new DbConnVO();
    try {
      Properties props = new Properties();
      FileInputStream in = new FileInputStream(this.getClass().getResource("/").getPath().replaceAll("%20"," ")+"pooler.ini");
      props.load(in);
      vo.setDriverName(props.getProperty("driverClass"));
      vo.setUsername(props.getProperty("user"));
      vo.setPassword(props.getProperty("password"));
      vo.setUrl(props.getProperty("url"));
      try {
        in.close();
      }
      catch (Exception ex1) {
      }
      return vo;
    }
    catch (Throwable ex) {
      Logger.error(
          "NONAME",
          this.getClass().getName(),
          "loadProperties",
          "Error while loading connection pooler",
          ex
      );
      return null;
    }
  }


}
