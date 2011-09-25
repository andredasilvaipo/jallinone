package org.jallinone.sqltool.server;

import org.openswing.swing.server.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.registers.carrier.java.*;
import org.jallinone.system.server.*;
import org.jallinone.commons.server.*;
import java.math.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.sqltool.java.*;
import org.jallinone.sqltool.java.*;
import org.jallinone.sqltool.java.*;
import org.jallinone.commons.java.*;


import org.jallinone.commons.server.JAIOBeanFactory;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to execute a query statement, based on TableVO columns info.</p>
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
public class ExecuteQueryAction implements Action {

  public ExecuteQueryAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "executeQuery";
  }


  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
	  GridParams gridParams = (GridParams)inputPar;
	  try {
		  Response answer = executeQuery(gridParams,((JAIOUserSessionParameters)userSessionPars).getServerLanguageId(),userSessionPars.getUsername());

		  return answer;
	  }
	  catch (Throwable ex) {
		  Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while processing request",ex);
		  return new ErrorResponse(ex.getMessage());
	  }
  }



  /**
   * Business logic to execute.
   */
  public VOListResponse executeQuery(GridParams gridParams,String serverLanguageId,String username) throws Throwable {
	  Connection conn = null;
	  try {
		  conn = ConnectionManager.getConnection(null);
	
		  TableVO tableVO = (TableVO)gridParams.getOtherGridParams().get(ApplicationConsts.QUERY_INFO);
		  Vector whereValues = (Vector)gridParams.getOtherGridParams().get(ApplicationConsts.WHERE_VALUES);
		  String sql = tableVO.getSql();
		  ArrayList cols = tableVO.getColumns();
	
		  Map attribute2dbField = new HashMap();
		  ColumnVO vo = null;
		  for(int i=0;i<cols.size();i++) {
			  vo = (ColumnVO)cols.get(i);
			  attribute2dbField.put(vo.getAttributeName(),vo.getColumnName());
		  }
	
		  ArrayList values = new ArrayList();
		  if (whereValues!=null) {
			  values.addAll(whereValues);
		  }
	
		  Response res = QueryUtil.getQuery(
				  conn,
				  new UserSessionParameters(username),
				  sql,
				  values,
				  attribute2dbField,
				  RowVO.class,
				  "Y",
				  "N",
				  null,
				  gridParams,
				  50,
				  true
		  );
		  if (res.isError())
			  throw new Exception(res.getErrorMessage());
		  return (VOListResponse)res;
	  }
	  catch (Throwable ex) {
		  Logger.error(username,this.getClass().getName(),"executeCommand","Error while executing a query statement",ex);
		  try {
			  conn.rollback();
		  }
		  catch (Exception ex3) {
		  }
		  throw new Exception(ex.getMessage());
	  }
	  finally {
		  try {
			  ConnectionManager.releaseConnection(conn, null);
		  } catch (Exception e) {
		  }

	  }

  }



}

