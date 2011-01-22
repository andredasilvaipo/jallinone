package org.jallinone.commons.server;

import org.openswing.swing.server.*;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import java.sql.*;
import java.util.Enumeration;
import java.util.Properties;

import org.jallinone.events.server.EventsManager;
import org.jallinone.startup.server.SQLExecutionBean;
import org.jallinone.startup.server.UpgradeBean;
import org.jallinone.system.scheduler.server.ETLProcessesScheduler;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Server controller callbacks.</p>
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
public class ApplicationControllerCallbacks extends ControllerCallbacks {

  public ApplicationControllerCallbacks() {
  }


  /**
   * Method called by the init method of Controller class, as last instruction.
   */
  public void afterInit(ServletContext context) {
	  EventsManager.getInstance();

	  try {
		  String beansFactoryClassName = context.getInitParameter("beansFactory");
		  BeansFactory bf = (BeansFactory)Class.forName(beansFactoryClassName).newInstance();
		  Properties p = new Properties();
		  Enumeration en = context.getInitParameterNames();
		  String key = null;
		  while(en.hasMoreElements()) {
			  key = en.nextElement().toString();
			  p.setProperty(key,context.getInitParameter(key).toString());
		  }
		  bf.initBeansFactory(p);
		  JAIOBeanFactory.initBeansFactory(bf);
	  }
	  catch (Throwable t) {
		  t.printStackTrace();
	  }

	  // check for additional sql scripts...
	  if (ConnectionManager.isConnectionSourceCreated()) {
		  Connection conn = null;
		  try {
			  conn = ConnectionManager.getConnection(null);
			  UpgradeBean b = new UpgradeBean();
			  b.maybeUpgradeDB(conn);
			  conn.commit();
			  ETLProcessesScheduler.getInstance().restartProcesses();
		  }
		  catch (Throwable t) {
			  try {
					if (conn!=null)
						conn.rollback();
			  } catch (SQLException e) {
				  e.printStackTrace();
			  }
			  t.printStackTrace();
		  }
		  finally {
			  if (conn!=null)
				try {
					ConnectionManager.releaseConnection(conn,null);
				} catch (Exception e) {
				}
		  }
	  }

  }


  public void destroy(ServletContext context) {
	  ETLProcessesScheduler.getInstance().stopProcesses();
	  try {
		  JAIOBeanFactory.destroyBeansFactory();
	  }
	  catch (Throwable t) {
		  t.printStackTrace();
	  }
  }

}
