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


import org.jallinone.commons.server.JAIOBeanFactory;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch tables in database.</p>
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
public class LoadEntitiesAction implements Action {

	public LoadEntitiesAction() {
	}

	/**
	 * @return request name
	 */
	public final String getRequestName() {
		return "loadEntities";
	}


	public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
		String entityType = (String)inputPar;
		try {
			Response answer = loadEntities(entityType,((JAIOUserSessionParameters)userSessionPars).getServerLanguageId(),userSessionPars.getUsername());

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
	public VOListResponse loadEntities(String entityType,String serverLanguageId,String username) throws Throwable {
		ResultSet rset = null;
		Statement stmt = null;
		Connection conn = null;
		try {
			conn = ConnectionManager.getConnection(null);
			rset = conn.getMetaData().getTables(null,null,null,new String[]{entityType});
			stmt = rset.getStatement();
			ArrayList entities = new ArrayList();
			while(rset.next())
				if (rset.getString(3).indexOf("$")==-1)
					entities.add(rset.getString(3));

			return new VOListResponse(entities,false,entities.size());
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching entities list",ex);
			try {
				conn.rollback();
			}
			catch (Exception ex3) {
			}
			throw new Exception(ex.getMessage());
		}
		finally {
			try {
				rset.close();
			}
			catch (Exception ex2) {
			}
			try {
				stmt.close();
			}
			catch (Exception ex3) {
			}
			try {
				ConnectionManager.releaseConnection(conn, null);
			} catch (Exception e) {
			}

		}

	}



}

