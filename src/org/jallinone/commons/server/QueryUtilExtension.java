package org.jallinone.commons.server;

import java.lang.reflect.*;
import java.math.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;

import org.openswing.swing.internationalization.java.*;
import org.openswing.swing.logger.server.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.*;
import org.openswing.swing.util.java.*;
import java.io.BufferedInputStream;
import org.openswing.swing.server.UserSessionParameters;
import org.openswing.swing.server.QueryUtil;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Help class used to execute a insert/update operation, based on QueryUtil base class.</p>
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
public class QueryUtilExtension {


/**
 * This method esecute an insert on a table, by means of the value object and a subset of its fields: all field related to that table.
 * @param vo value object to use on insert
 * @param tableName table name to use on insert
 * @param attribute2dbField collection of pairs attributeName, corresponding database column (table.column) - for ALL fields related to the specified table
 * @param booleanTrueValue value to interpret as true
 * @param booleanFalseValue value to interpret as false
 * @param context servlet context; this may be null
 * @param logSQL <code>true</code> to log the SQL, <code>false</code> to no log the SQL
 * @return the insert response
 */
	public static Response insertTable(
			Connection conn,
			UserSessionParameters userSessionPars,
			ValueObject vo,
			String tableName,
			Map attribute2dbField,
			String booleanTrueValue,
			String booleanFalseValue,
			ServletContext context,
			boolean logSQL
	) throws Exception {
	   Map fieldsValues = new HashMap();
		 fieldsValues.put("CREATE_USER",userSessionPars==null?"UNDEFINED":userSessionPars.getUsername());
		 fieldsValues.put("CREATE_DATE",new java.sql.Timestamp(System.currentTimeMillis()));

		 return QueryUtil.insertTable(
			 conn,
			 userSessionPars,
			 vo,
			 tableName,
			 attribute2dbField,
			 booleanTrueValue,
			 booleanFalseValue,
			 fieldsValues,
			 context,
			 logSQL
		);
	}


	/**
	 * This method esecute many insert on a table, by means of a list of value objects and a subset of its fields: all field related to that table.
	 * @param vos list of ValueObject's to use on insert operations
	 * @param tableName table name to use on insert
	 * @param attribute2dbField collection of pairs attributeName, corresponding database column (table.column) - for ALL fields related to the specified table
	 * @param booleanTrueValue value to interpret as true
	 * @param booleanFalseValue value to interpret as false
	 * @param context servlet context; this may be null
	 * @param logSQL <code>true</code> to log the SQL, <code>false</code> to no log the SQL
	 * @return the insert response (VOListResponse object or an ErrorResponse object)
	 */
	public static Response insertTable(
			Connection conn,
			UserSessionParameters userSessionPars,
			ArrayList vos,
			String tableName,
			Map attribute2dbField,
			String booleanTrueValue,
			String booleanFalseValue,
			ServletContext context,
			boolean logSQL
	) throws Exception {
		 Map fieldsValues = new HashMap();
		 fieldsValues.put("CREATE_USER",userSessionPars==null?"UNDEFINED":userSessionPars.getUsername());
		 fieldsValues.put("CREATE_DATE",new java.sql.Timestamp(System.currentTimeMillis()));

		 return QueryUtil.insertTable(
			 conn,
			 userSessionPars,
			 vos,
			 tableName,
			 attribute2dbField,
			 booleanTrueValue,
			 booleanFalseValue,
			 fieldsValues,
			 context,
			 logSQL
		 );
	}


/**
 * This method esecute an update on a table, by means of the value object and a subset of its fields: all field related to that table.
 * The update operation verifies if the record is yet the same as when the v.o. was read (concurrent access resolution).
 * @param pkAttributes v.o. attributes related to the primary key of the table
 * @param oldVO previous value object to use on the where clause
 * @param newVO new value object to use on update
 * @param tableName table name to use on update
 * @param attribute2dbField collection of pairs attributeName, corresponding database column (table.column) - for ALL fields related to the specified table
 * @param booleanTrueValue value to interpret as true
 * @param booleanFalseValue value to interpret as false
 * @param context servlet context; this may be null
 * @param logSQL <code>true</code> to log the SQL, <code>false</code> to no log the SQL
 * @return the update response
 */
	public static Response updateTable(
			Connection conn,
			UserSessionParameters userSessionPars,
			HashSet pkAttributes,
			ValueObject oldVO,
			ValueObject newVO,
			String tableName,
			Map attribute2dbField,
			String booleanTrueValue,
			String booleanFalseValue,
			ServletContext context,
			boolean logSQL
	) throws Exception {
		 Map fieldsValuesToNotCompare = new HashMap();
		 fieldsValuesToNotCompare.put("LAST_UPDATE_USER",userSessionPars==null?"UNDEFINED":userSessionPars.getUsername());
		 fieldsValuesToNotCompare.put("LAST_UPDATE_DATE",new java.sql.Timestamp(System.currentTimeMillis()));

		 return QueryUtil.updateTable(
			 conn,
			 new UserSessionParameters(),
			 pkAttributes,
			 oldVO,
			 newVO,
			 tableName,
			 attribute2dbField,
			 booleanTrueValue,
			 booleanFalseValue,
			 fieldsValuesToNotCompare,
			 context,
			 logSQL
		 );
	}



}
