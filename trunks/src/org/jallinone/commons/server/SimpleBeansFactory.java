package org.jallinone.commons.server;

import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.server.ConnectionManager;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Beans factory based on simple beans instantiation.</p>
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
public class SimpleBeansFactory implements BeansFactory {

	
	/**
	 * Init this factory.
	 */
	public void initBeansFactory(Properties p) throws Throwable {
	}	
	
	
	/**
	 * Create a bean of the specified class.
	 * @param clazz bean's class type
	 * @return an instance of the bean
	 * @throws Throwable in case of errors 
	 */
	public Object getBean(Class clazz) throws Throwable {
		return getBean(clazz,new SimpleDataSource());
	}
		
	
	private Object getBean(Class clazz,final SimpleDataSource ds) throws Throwable {
		if (!clazz.isInterface())
			throw new Exception(clazz.getName()+" is not an interface");
		
		String beanName = clazz.getName()+"Bean";
		Class beanClass = Class.forName(beanName);
		final Object bean = beanClass.newInstance();

		InvocationHandler handler = new InvocationHandler() {

 		  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
 			  try {
				Object retValue = method.invoke(bean,args);
				if (!method.getName().equals("setDataSource") &&
					!method.getName().equals("setConn") &&
					!method.getName().equals("getConn")) {
					
				  if (ds!=null && ds.getCurrentConn()!=null) {
					  ds.getCurrentConn().commit();
					  ds.releaseConnection(ds.getCurrentConn());
				  }
				}
				return retValue;
			} catch (Throwable e) {
				if (e instanceof InvocationTargetException)
					e = ((InvocationTargetException)e).getTargetException();
				//e.printStackTrace();
				
				// in case of error the connection must be rolled back...
				if (ds!=null && ds.getCurrentConn()!=null) {
					ds.getCurrentConn().rollback();
					ds.releaseConnection(ds.getCurrentConn());
					
				}				
				//throw e;
				Response res = null;
				if (Response.class.isAssignableFrom(method.getReturnType())) {
					res = (Response)method.getReturnType().newInstance();
					method.getReturnType().getMethod("setErrorMessage", new Class[]{String.class}).invoke(res, new Object[]{e.getMessage()});
					method.getReturnType().getMethod("setError", new Class[]{Boolean.TYPE}).invoke(res, new Object[]{Boolean.TRUE});
					return res;
				}
				else 
					throw e;
			}
		  }
			
		};
		Object proxy = Proxy.newProxyInstance(
				clazz.getClassLoader(),
	        new Class[] { clazz },
	        handler
	    );
		fillInBean(bean,beanClass,ds);
		
		return proxy;
	}

	
	private void fillInBean(Object bean,Class beanClass,SimpleDataSource ds) throws Throwable {
		// set datasource property (if exists)
		try {
			beanClass.getMethod("setDataSource", new Class[]{DataSource.class}).invoke(bean, new Object[]{ds});
		} catch (Exception e) {
		}

		// analyze other properties...
		Method[] setters = beanClass.getMethods();
		for(Method m: setters) {
			if (m.getName().startsWith("set") && 
				m.getParameterTypes().length==1 &&
				m.getParameterTypes()[0].getName().endsWith("Bean")
			) {
				// found an injected bean...	
				if (beanClass.getName().endsWith("Bean")) {
					Object injectedBean = m.getParameterTypes()[0].newInstance();
					fillInBean(injectedBean,m.getParameterTypes()[0],ds);
					m.invoke(bean, new Object[]{injectedBean});
				}
			}
		}
	}
	

	/**
	 * Destroy this factory.
	 */
	public void destroyBeansFactory() throws Throwable {

	}
	
	
	class SimpleDataSource implements DataSource {
		
		private PrintWriter pw;
		private int loginTimeout = 1000;
		private Connection currentConn = null;
		private int openedConns = 0;
		
		
		public Connection getCurrentConn() {
			return currentConn;
		}

		
		public int getOpenedConns() {
			return openedConns;
		}

		
		public void releaseConnection(Connection conn) {
			ConnectionManager.releaseConnection(conn, null);
			openedConns--;
		}
		

		/**
		 * <p>Attempts to establish a connection with the data source that
		 * this <code>DataSource</code> object represents.
		 *
		 * @return  a connection to the data source
		 * @exception SQLException if a database access error occurs
		 */
		public Connection getConnection() throws SQLException {
			try { 
				Connection conn =  ConnectionManager.getConnection(null);
				openedConns++;
				currentConn = conn;
				return conn;
			}
			catch(Throwable t) {
				t.printStackTrace();
				throw new SQLException(t.getMessage());
			}
		}

		/**
		 * <p>Attempts to establish a connection with the data source that
		 * this <code>DataSource</code> object represents.
		 *
		 * @param username the database user on whose behalf the connection is 
		 *  being made
		 * @param password the user's password
		 * @return  a connection to the data source
		 * @exception SQLException if a database access error occurs
		 * @since 1.4
		 */
		public Connection getConnection(String username, String password) throws SQLException {
			return getConnection();
		}

		@Override
		public PrintWriter getLogWriter() throws SQLException {
			return pw;
		}

		@Override
		public int getLoginTimeout() throws SQLException {
			return loginTimeout;
		}

		@Override
		public void setLogWriter(PrintWriter out) throws SQLException {
			pw = out;
		}

		@Override
		public void setLoginTimeout(int seconds) throws SQLException {
			loginTimeout = seconds;
		}

		@Override
		public boolean isWrapperFor(Class<?> iface) throws SQLException {
			return false;
		}

		@Override
		public <T> T unwrap(Class<T> iface) throws SQLException {
			return null;
		}			

	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
