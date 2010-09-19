package org.jallinone.startup.server;

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.jallinone.startup.java.*;
import org.openswing.swing.logger.server.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.server.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action Class called by StartupFrame class to create the pooler config.xml file.</p>
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
public class CreateConfigFileAction implements Action {


  public CreateConfigFileAction() {
  }


  /**
   * @return request name
   */
  public final String getRequestName() {
    return "createConfigFile";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPars,
                                 UserSessionParameters userSessionParameters,
                                 HttpServletRequest httpServletRequest,
                                 HttpServletResponse httpServletResponse,
                                 HttpSession httpSession,
                                 ServletContext servletContext) {
    DbConnVO vo = (DbConnVO)inputPars;
    if (!saveProperties(
        servletContext,
        vo.getDriverName(),
        vo.getUsername(),
        vo.getPassword(),
        vo.getUrl()
    ))
      return new ErrorResponse("An error occours while saving the database configuration file.");

    try {
      ConnectionManager.initConnectionSource(
          null,
          "org.openswing.swing.server.PoolerConnectionSource"
      );
    }
    catch (Exception ex) {
      return new ErrorResponse(ex.getMessage());
    }
    if (ConnectionManager.isConnectionSourceCreated()) {
      // create the database structures...
      Connection conn = null;
      try {
        conn = ConnectionManager.getConnection(servletContext);
      }
      catch (Exception ex) {
        try {
          new File(this.getClass().getResource("/").getFile() + "pooler.ini").delete();
          ConnectionManager.initConnectionSource(
              null,
              "org.openswing.swing.server.PoolerConnectionSource"
              );
        }
        catch (Throwable ex1) {
        }

        Logger.error("NONAME",this.getClass().getName(),"executeCommand","Error while creating database structures",ex);
        return new ErrorResponse("Error while creating database structures:\n"+ex.getMessage());
      }
      try {
        SQLExecutionBean executer = new SQLExecutionBean();

        // read file of database structures...
        executer.executeSQL(conn,vo,"defsql.ini");

        // read file of insert statements...
        executer.executeSQL(conn,vo,"inssql_"+vo.getClientLanguageCode()+".ini");

        conn.commit();

        new UpgradeBean().maybeUpgradeDB(servletContext);

      }
      catch (Throwable ex) {
        Logger.error("NONAME",this.getClass().getName(),"executeCommand","Error while creating database structures",ex);
        return new ErrorResponse("Error while creating database structures:\n"+ex.getMessage());
      }
      finally {
        try {
          ConnectionManager.releaseConnection(conn, servletContext);
        }
        catch (Exception ex3) {
        }
      }

      return new TextResponse("Database configuration file successfully saved.");
    }
    else
      return new ErrorResponse("Database configuration settings are not valid.");
  }







  /**
   * Create a "pooler.ini" file with mandatory parameters only.
   * @param driverClass JDBC driver class name
   * @param user database username
   * @param password database password
   * @param url JDBC database connection URL
   */
  private boolean saveProperties(ServletContext context,String driverClass,String user,String password,String url) {
    try {
      Properties props = new Properties();
      props.setProperty("driverClass", driverClass);
      props.setProperty("user", user);
      props.setProperty("password", password);
      props.setProperty("url", url);
      FileOutputStream out = new FileOutputStream(this.getClass().getResource("/").getPath().replaceAll("%20"," ")+"pooler.ini");
      props.save(out,"POOLER PROPERTIES");
      try {
        out.close();
      }
      catch (IOException ex1) {
      }
      return true;
    }
    catch (Throwable ex) {
      Logger.error(
          "NONAME",
          this.getClass().getName(),
          "saveProperties",
          "Error while creating connection pooler",
          ex
      );
      return false;
    }
  }


}
