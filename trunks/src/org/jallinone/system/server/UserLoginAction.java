package org.jallinone.system.server;

import org.jallinone.commons.server.JAIOBeanFactory;
import org.openswing.swing.server.*;

import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;

import java.sql.*;
import java.math.*;


import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to execute the user authentication: it returns the language identifier associated to the user.</p>
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
public class UserLoginAction extends LoginAction {


  private DataSource dataSource; 

  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  private Connection conn = null;
  
  public void setConn(Connection conn) {
    this.conn = conn;
  }

  public Connection getConn() throws Exception {
    if (conn!=null)
      return conn;
    return dataSource.getConnection();
  }


  public UserLoginAction() { }

  
  public final Response executeCommand(Object inputPar,UserSessionParameters pars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    try {
    	String username = ((String[])inputPar)[0];
    	String password = ((String[])inputPar)[1];

    	UserLogin bean = (UserLogin)JAIOBeanFactory.getInstance().getBean(UserLogin.class);
    	JAIOUserSessionParameters userSessionPars = bean.authenticateUser(username, password);

    	SessionIdGenerator gen = (SessionIdGenerator)context.getAttribute(Controller.SESSION_ID_GENERATOR);
    	Hashtable userSessions = (Hashtable)context.getAttribute(Controller.USER_SESSIONS);
    	HashSet authenticatedIds = (HashSet)context.getAttribute(Controller.SESSION_IDS);
  	
    	
    	TextResponse tr = new TextResponse(userSessionPars.getLanguageId());
    	tr.setSessionId(gen.getSessionId(request,response,userSession,context));
        userSessionPars.setSessionId(tr.getSessionId());
        userSessionPars.setUsername(username);

    	if (userSessionPars!=null) {
    		userSessions.remove(userSessionPars.getSessionId());
    		authenticatedIds.remove(userSessionPars.getSessionId());
    	}
        userSessions.put(tr.getSessionId(),userSessionPars);
        authenticatedIds.add(tr.getSessionId());
    	
    	return tr;
    } catch (Throwable ex1) {
      return new ErrorResponse(ex1.getMessage());
    } 
  }


}

