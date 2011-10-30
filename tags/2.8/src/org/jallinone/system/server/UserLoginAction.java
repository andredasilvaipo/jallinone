package org.jallinone.system.server;

import org.jallinone.commons.server.JAIOBeanFactory;
import org.openswing.swing.server.*;

import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.jallinone.system.java.*;
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
    	UserLoginVO userLoginVO = bean.authenticateUser(username, password);
     	JAIOUserSessionParameters userSessionPars = new JAIOUserSessionParameters();
     	userSessionPars.setLanguageId(userLoginVO.getLanguageId());
     	userSessionPars.setServerLanguageId(userLoginVO.getServerLanguageId());
     	userSessionPars.setProgressiveReg04SYS03(userLoginVO.getProgressiveReg04SYS03());
        userSessionPars.setName_1(userLoginVO.getName_1());
        userSessionPars.setName_2(userLoginVO.getName_2());
        userSessionPars.setEmployeeCode(userLoginVO.getEmployeeCode());
        userSessionPars.setCompanyCodeSys01SYS03(userLoginVO.getCompanyCodeSys01SYS03());
        userSessionPars.setDefCompanyCodeSys01SYS03(userLoginVO.getDefCompanyCodeSys01SYS03());
     	
     	fillInFunctionCodesBasedOnCompany(username,userSessionPars);

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

  
  /**
   * Business logic to execute.
   */
  private void fillInFunctionCodesBasedOnCompany(String username,JAIOUserSessionParameters userSessionPars) throws Throwable {
	  Connection conn = null;
	  ResultSet rset2 = null;
	  PreparedStatement pstmt2 = null;
	  try {
		  conn = ConnectionManager.getConnection(null);
		  HashSet functionCodesBasedOnCompany = new HashSet();
		  pstmt2 = conn.prepareStatement(
				  "select FUNCTION_CODE FROM SYS06_FUNCTIONS WHERE USE_COMPANY_CODE='Y'"
		  );
		  rset2 = pstmt2.executeQuery();
		  while(rset2.next())
			  functionCodesBasedOnCompany.add(rset2.getString(1));
		  rset2.close();
		  pstmt2.close();
		  userSessionPars.setFunctionCodesBasedOnCompany(functionCodesBasedOnCompany);
	  } catch (Exception ex1) {
		  ex1.printStackTrace();
		  throw new Exception(ex1.getMessage());
	  } finally {
		  try {
			  rset2.close();
		  }
		  catch (Exception ex) {
		  }
		  try {
			  pstmt2.close();
		  }
		  catch (Exception ex) {
		  }
		  try {
			  ConnectionManager.releaseConnection(conn, null);
		  } catch (Exception e) {
		  }
	  }
  }
  

}

