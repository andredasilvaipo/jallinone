package org.jallinone.startup.server;

import javax.sql.DataSource;

import org.openswing.swing.server.*;
import org.openswing.swing.message.receive.java.*;
import javax.servlet.http.*;
import javax.servlet.http.*;
import javax.servlet.http.*;
import javax.servlet.*;
import org.jallinone.startup.java.*;
import java.util.*;
import org.openswing.swing.logger.server.*;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.*;
import org.jallinone.commons.java.*;


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


  public UpgradeBean() { }


  public final void maybeUpgradeDB(Connection conn) throws Throwable {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    try {
      SQLExecutionBean executer = new SQLExecutionBean();

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
      pstmt = conn.prepareStatement("SELECT DISTINCT CLIENT_LANGUAGE_CODE,LANGUAGE_CODE FROM SYS09_LANGUAGES WHERE ENABLED='Y'");
      rset = pstmt.executeQuery();
      ArrayList supportedClientLanguages = new ArrayList();
			ArrayList supportedLanguages = new ArrayList();
      while (rset.next()) {
        supportedClientLanguages.add(rset.getString(1));
				supportedLanguages.add(rset.getString(2));
      }
			rset.close();
      pstmt.close();


      DbConnVO vo = loadProperties();

      for(int i=version+1;i<=ApplicationConsts.DB_VERSION;i++) {
        // apply SQL script for database upgrading...

        Logger.debug("NONAME",this.getClass().getName(),"maybeUpgradeDB","Applying SQL script 'defsql"+i+".ini'");

        executer.executeSQL(conn,vo,"defsql"+i+".ini");
        for(int k=0;k<supportedClientLanguages.size();k++) {
					vo.setLanguageCode(supportedLanguages.get(k).toString());
          Logger.debug("NONAME",this.getClass().getName(),"maybeUpgradeDB","Applying SQL script 'inssql"+i+"_"+supportedClientLanguages.get(k)+".ini'");
          executer.executeSQL(conn,vo,"inssql"+i+"_"+supportedClientLanguages.get(k)+".ini");
        }
      }


      // execute some other "custom" updates...
      pstmt = conn.prepareStatement("SELECT COMPANY_CODE FROM SYS01_COMPANIES WHERE ENABLED='Y'");
      rset = pstmt.executeQuery();
      String companyCode = null;
      if (rset.next())
        companyCode = rset.getString(1);
      rset.close();
      pstmt.close();
      pstmt = conn.prepareStatement("UPDATE SYS03_USERS SET DEF_COMPANY_CODE_SYS01=? WHERE DEF_COMPANY_CODE_SYS01 IS NULL");
      pstmt.setString(1,companyCode);
      pstmt.execute();
      pstmt.close();


      // update db version value...
      pstmt = conn.prepareStatement("UPDATE SYS11_APPLICATION_PARS SET VALUE=? WHERE PARAM_CODE='VERSION'");
      pstmt.setString(1,String.valueOf(ApplicationConsts.DB_VERSION));
      pstmt.execute();
      pstmt.close();
    }
    catch (Throwable ex) {
      Logger.error("NONAME",this.getClass().getName(),"maybeUpgradeDB","Error while checking for database version",ex);
      throw ex;
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

