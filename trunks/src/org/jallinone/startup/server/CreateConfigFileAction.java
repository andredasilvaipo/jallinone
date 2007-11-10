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

        // read file of database structures...
        executeSQL(conn,vo,"defsql.ini");

        // read file of insert statements...
        executeSQL(conn,vo,"inssql_"+vo.getClientLanguageCode()+".ini");

        conn.commit();
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
   * Execute the SQL scripts contained in the specified file.
   * @param fileName file to read
   */
  private void executeSQL(Connection conn,DbConnVO vo,String fileName) throws Throwable {
    PreparedStatement pstmt = null;
    StringBuffer sql = new StringBuffer("");
    try {
      BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(this.getClass().getResource("/").getPath() + fileName)));
      String line = null;
      ArrayList vals = new ArrayList();
      int pos = -1;
      String oldfk = null;
      String oldIndex = null;
      StringBuffer newIndex = null;
      StringBuffer newfk = null;
      ArrayList fks = new ArrayList();
      ArrayList indexes = new ArrayList();
      boolean fkFound = false;
      boolean indexFound = false;
      String defaultValue = null;
      boolean useDefaultValue =
          conn.getMetaData().getDriverName().equals("oracle.jdbc.driver.OracleDriver") ||
          conn.getMetaData().getDriverName().equals("com.microsoft.jdbc.sqlserver.SQLServerDriver") ||
          conn.getMetaData().getDriverName().equals("com.mysql.jdbc.Driver");
      while ( (line = br.readLine()) != null) {
        sql.append(line);
        if (line.endsWith(";")) {
          if (vo.getDriverName().equals("oracle.jdbc.driver.OracleDriver")) {
            sql = replace(sql, " VARCHAR(", " VARCHAR2(");
            sql = replace(sql, " NUMERIC(", " NUMBER(");
            sql = replace(sql, " DECIMAL(", " NUMBER(");
            sql = replace(sql, " TIMESTAMP ", " DATE ");
            sql = replace(sql, " DATETIME ", " DATE ");
          }
          else if (vo.getDriverName().equals("com.microsoft.jdbc.sqlserver.SQLServerDriver")) {
            sql = replace(sql, "TIMESTAMP", "DATETIME");
          }

          sql = replace(sql,"ON DELETE NO ACTION","");
          sql = replace(sql,"ON UPDATE NO ACTION","");

          if (sql.indexOf(":COMPANY_CODE") != -1) {
            sql = replace(sql, ":COMPANY_CODE", "'" + vo.getCompanyCode() + "'");
          }
          if (sql.indexOf(":COMPANY_DESCRIPTION") != -1) {
            sql = replace(sql, ":COMPANY_DESCRIPTION",
                          "'" + vo.getCompanyDescription() + "'");
          }
          if (sql.indexOf(":LANGUAGE_CODE") != -1) {
            sql = replace(sql, ":LANGUAGE_CODE",
                          "'" + vo.getLanguageCode() + "'");
          }
          if (sql.indexOf(":LANGUAGE_DESCRIPTION") != -1) {
            sql = replace(sql, ":LANGUAGE_DESCRIPTION",
                          "'" + vo.getLanguageDescription() + "'");
          }
          if (sql.indexOf(":CLIENT_LANGUAGE_CODE") != -1) {
            sql = replace(sql, ":CLIENT_LANGUAGE_CODE",
                          "'" + vo.getClientLanguageCode() + "'");
          }
          if (sql.indexOf(":PASSWORD") != -1) {
            sql = replace(sql, ":PASSWORD", "'" + vo.getAdminPassword() + "'");
          }
          if (sql.indexOf(":DATE") != -1) {
            sql = replace(sql, ":DATE", "?");
            vals.add(new Date(System.currentTimeMillis()));
          }

          if (sql.indexOf(":CURRENCY_CODE") != -1) {
            sql = replace(sql, ":CURRENCY_CODE", "'" + vo.getCurrencyCodeREG03() + "'");
          }
          if (sql.indexOf(":CURRENCY_SYMBOL") != -1) {
            sql = replace(sql, ":CURRENCY_SYMBOL", "'" + vo.getCurrencySymbolREG03() + "'");
          }
          if (sql.indexOf(":DECIMALS") != -1) {
            sql = replace(sql, ":DECIMALS", vo.getDecimalsREG03().toString());
          }
          if (sql.indexOf(":DECIMAL_SYMBOL") != -1) {
            sql = replace(sql, ":DECIMAL_SYMBOL", "'" + vo.getDecimalSymbolREG03() + "'");
          }
          if (sql.indexOf(":THOUSAND_SYMBOL") != -1) {
            sql = replace(sql, ":THOUSAND_SYMBOL", "'" + vo.getThousandSymbolREG03() + "'");
          }

          if (!useDefaultValue)
            while((pos=sql.indexOf("DEFAULT "))!=-1) {
              defaultValue = sql.substring(pos, sql.indexOf(",", pos));
              sql = replace(
                  sql,
                  defaultValue,
                  ""
              );
            }

          fkFound = false;
          while((pos=sql.indexOf("FOREIGN KEY"))!=-1) {
            oldfk = sql.substring(pos,sql.indexOf(")",sql.indexOf(")",pos)+1)+1);
            sql = replace(
                sql,
                oldfk,
                ""
            );
            newfk = new StringBuffer("ALTER TABLE ");
            newfk.append(sql.substring(sql.indexOf(" TABLE ")+7,sql.indexOf("(")).trim());
            newfk.append(" ADD ");
            newfk.append(oldfk);
            fks.add(newfk);
            fkFound = true;
          }

          if (fkFound)
            sql = removeCommasAtEnd(sql);

          indexFound = false;
          while((pos=sql.indexOf("INDEX "))!=-1) {
            oldIndex = sql.substring(pos,sql.indexOf(")",pos)+1);
            sql = replace(
                sql,
                oldIndex,
                ""
            );
            newIndex = new StringBuffer("CREATE ");
            newIndex.append(oldIndex.substring(0,oldIndex.indexOf("(")));
            newIndex.append(" ON ");
            newIndex.append(sql.substring(sql.indexOf(" TABLE ")+7,sql.indexOf("(")).trim());
            newIndex.append( oldIndex.substring(oldIndex.indexOf("(")) );
            indexes.add(newIndex);
            indexFound = true;
          }

          if (indexFound)
            sql = removeCommasAtEnd(sql);


          if (sql.toString().trim().length()>0) {
            pstmt = conn.prepareStatement(sql.toString().substring(0,sql.length() - 1));
            for (int i = 0; i < vals.size(); i++) {
              pstmt.setObject(i + 1, vals.get(i));
            }
            pstmt.execute();
            pstmt.close();
          }

          sql.delete(0, sql.length());
          vals.clear();
        }
      }
      br.close();

      for(int i=0;i<fks.size();i++) {
        sql = (StringBuffer)fks.get(i);
        pstmt = conn.prepareStatement(sql.toString());
        try {
          pstmt.execute();
        }
        catch (SQLException ex4) {
          System.out.println(ex4.toString());
        }
        pstmt.close();
      }

      for(int i=0;i<indexes.size();i++) {
        sql = (StringBuffer)indexes.get(i);
        pstmt = conn.prepareStatement(sql.toString());
        try {
          pstmt.execute();
        }
        catch (SQLException ex3) {
          System.out.println(ex3.toString());
        }
        pstmt.close();
      }

    }
    catch (Throwable ex) {
      try {
        Logger.error("NONAME", this.getClass().getName(), "executeSQL",
                     "Invalid SQL: " + sql, null);
      }
      catch (Exception ex2) {
      }
      throw ex;
    }
    finally {
      try {
        if (pstmt!=null)
          pstmt.close();
      }
      catch (SQLException ex1) {
      }
    }
  }


  /**
   * Remove "," symbols at the end of the script.
   * Example: "INDEX WKF10_INSTANCE_PROPERTIES_FKIndex2(PROGRESSIVE_WKF01, PROGRESSIVE_WKF08),  , );
   * @param sql script to analyze
   * @return sql script, without "," symbols at the end
   */
  private StringBuffer removeCommasAtEnd(StringBuffer sql) {
    int i=sql.length()-3;
    while(i>0 && (sql.charAt(i)==' ' || sql.charAt(i)==','))
      i--;
    sql = sql.replace(i+1,sql.length()-2," ");

    return sql;
  }


  /**
   * Replace the specified pattern with the new one.
   * @param b sql script
   * @param oldPattern pattern to replace
   * @param newPattern new pattern
   * @return sql script with substitutions
   */
  private StringBuffer replace(StringBuffer b,String oldPattern,String newPattern) {
    int i = 0;
    while((i=b.indexOf(oldPattern,i))!=-1) {
      b.replace(i,i+oldPattern.length(),newPattern);
      i = i+oldPattern.length();
    }
    return b;
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
      FileOutputStream out = new FileOutputStream(this.getClass().getResource("/").getFile()+"pooler.ini");
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
