package org.jallinone.startup.server;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jallinone.startup.java.DbConnVO;
import org.openswing.swing.logger.server.Logger;
import org.openswing.swing.message.receive.java.ErrorResponse;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOResponse;
import org.openswing.swing.server.Action;
import org.openswing.swing.server.ConnectionManager;
import org.openswing.swing.server.UserSessionParameters;

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


  public final Response executeCommand(Object inputPars,
                                 UserSessionParameters userSessionPars,
                                 HttpServletRequest httpServletRequest,
                                 HttpServletResponse httpServletResponse,
                                 HttpSession httpSession,
                                 ServletContext servletContext) {
    try {
      DbConnVO vo = (DbConnVO)inputPars;
      return createConfigFile(vo,"ADMIN");
    }
    catch (Throwable ex) {
      Logger.error(null,this.getClass().getName(),"executeCommand","Error while processing request",ex);
      return new ErrorResponse(ex.getMessage());
    }
  }



  /**
   * Business logic to execute.
   */
  public VOResponse createConfigFile(DbConnVO vo,String username) throws Throwable {
	  if (!saveProperties(
			  vo.getDriverName(),
			  vo.getUsername(),
			  vo.getPassword(),
			  vo.getUrl()
	  ))
		  throw new Exception("An error occours while saving the database configuration file.");

	  Connection conn = null;
	  UpgradeBean upgradeBean = new UpgradeBean();
	  SQLExecutionBean executer = new SQLExecutionBean();
	  try {
		  ConnectionManager.initConnectionSource(
				  null,
				  "org.openswing.swing.server.PoolerConnectionSource"
		  );
	  }
	  catch (Exception ex) {
		  throw new Exception(ex.getMessage());
	  }
	  if (ConnectionManager.isConnectionSourceCreated()) {
		  // create the database structures...
		  try {
			  conn = ConnectionManager.getConnection(null);
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
			  throw new Exception("Error while creating database structures:\n"+ex.getMessage());
		  }
		  try {

			  if (vo.isCheckDbVersion()) {
				  // check for db schema existence...
				  Statement stmt = null;
				  ResultSet rset = null;
				  try {
					  stmt = conn.createStatement();
					  rset = stmt.executeQuery("SELECT VALUE FROM SYS11_APPLICATION_PARS WHERE PARAM_CODE='VERSION'");
					  if (rset.next()) {
						  upgradeBean.maybeUpgradeDB(conn);
						  return new VOResponse(Boolean.TRUE);
					  }
					  else {
						  removeConfigIni();
						  return new VOResponse(Boolean.FALSE);
					  }
				  }
				  catch (Exception ex2) {
					  removeConfigIni();
				  }
				  finally {
					  try {
						  rset.close();
					  }
					  catch (Exception ex4) {
					  }
					  try {
						  stmt.close();
					  }
					  catch (Exception ex5) {
					  }
				  }
				  return new VOResponse(Boolean.FALSE);
			  }


			  // read file of database structures...
			  executer.executeSQL(conn,vo,"defsql.ini");

			  // read file of insert statements...
			  executer.executeSQL(conn,vo,"inssql_"+vo.getClientLanguageCode()+".ini");


			  upgradeBean.maybeUpgradeDB(conn);

			  conn.commit();

		  }
		  catch (Throwable ex) {
			  Logger.error("NONAME",this.getClass().getName(),"executeCommand","Error while creating database structures",ex);
			  throw new Exception("Error while creating database structures:\n"+ex.getMessage());
		  }
		  finally {
			  try {
				  ConnectionManager.releaseConnection(conn,null);
			  }
			  catch (Exception ex3) {
			  }

		  }

		  return new VOResponse("Database configuration file successfully saved.");
	  }
	  else
		  throw new Exception("Database configuration settings are not valid.");
  }







  /**
   * Create a "pooler.ini" file with mandatory parameters only.
   * @param driverClass JDBC driver class name
   * @param user database username
   * @param password database password
   * @param url JDBC database connection URL
   */
  private boolean saveProperties(String driverClass,String user,String password,String url) {
    try {
      Properties props = new Properties();
      props.setProperty("driverClass", driverClass);
      props.setProperty("user", user);
      props.setProperty("password", password);
      props.setProperty("url", url);
      FileOutputStream out = new FileOutputStream(this.getClass().getResource("/").getPath().replaceAll("%20"," ")+"pooler.ini");
      props.store(out,"POOLER PROPERTIES");
      try {
        out.close();
      }
      catch (IOException ex1) {
      }

      // update also "conf/applicationContext.xml" file...
      String xmlFile = this.getClass().getResource("/").getPath().replaceAll("%20"," ")+"conf/applicationContext.xml";
      BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(xmlFile))));
      StringBuffer sb = new StringBuffer();
      String line = null;
      while((line=br.readLine())!=null) {

    	  if (line.indexOf("<property name=\"driverName\"")!=-1)
    	  	line = replaceValue(line,driverClass);
    	  else if (line.indexOf("<property name=\"url\"")!=-1)
  	  		line = replaceValue(line,url);
    	  else if (line.indexOf("<property name=\"user\"")!=-1)
  	  		line = replaceValue(line,user);
    	  else if (line.indexOf("<property name=\"password\"")!=-1)
  	  		line = replaceValue(line,password);

    	  sb.append(line).append("\n");
      }
      br.close();

      /*
		   <bean id="JALLINONE" class="org.enhydra.jdbc.standard.StandardDataSource" destroy-method="shutdown" >
		     <property name="driverName" value="com.mysql.jdbc.Driver"/>
		     <property name="url" value="jdbc:mysql://localhost:3306/j"/>
		     <property name="user" value="j"/>
		     <property name="password" value="j"/>
		   </bean>
       */

      PrintWriter pw = new PrintWriter(new FileOutputStream(xmlFile));
      String[] lines = sb.toString().split("\n");
      for(int i=0;i<lines.length;i++)
    	  pw.println(lines[i]);
      pw.close();

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


  private String replaceValue(String line,String value) {
	  int i1 = line.indexOf("value=\"");
	  if (i1==-1)
		  return line;
	  i1 += 7;
	  int i2 = line.indexOf("\"",i1);
	  if (i2==-1)
		  return line;
	  return line.substring(0,i1)+value+line.substring(i2);
  }


  /**
   * Remove "pooler.ini" file.
   */
  private void removeConfigIni() {
    try {
      Logger.debug(
          "NONAME",
          this.getClass().getName(),
          "removeConfigIni",
          "Removing connection pooler .ini file"
      );
      File f = new File(this.getClass().getResource("/").getPath().replaceAll("%20"," ")+"pooler.ini");
      boolean ok = f.delete();
      Logger.debug(
          "NONAME",
          this.getClass().getName(),
          "removeConfigIni",
          "Removing connection pooler .ini file: "+(ok?"ok":"remove failed!")
      );
      ConnectionManager.destroyConnectionSource();

    }
    catch (Throwable ex) {
      Logger.error(
          "NONAME",
          this.getClass().getName(),
          "removeConfigIni",
          "Error while removing connection pooler .ini file",
          ex
      );
    }
  }







}

