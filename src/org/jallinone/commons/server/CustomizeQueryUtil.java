package org.jallinone.commons.server;

import org.openswing.swing.server.QueryUtil;
import java.util.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.logger.server.Logger;
import java.sql.*;
import javax.servlet.ServletContext;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;
import java.lang.reflect.*;
import java.math.BigDecimal;
import org.openswing.swing.internationalization.java.ResourcesFactory;
import org.openswing.swing.server.UserSessionParameters;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.system.java.CustomizedWindows;
import org.jallinone.system.customizations.java.WindowCustomizationVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Help class used to execute a query/insert/update, based on custom fields.</p>
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
public class CustomizeQueryUtil {


  /**
   * This method read the WHOLE result set.
   * @param baseSQL SQL to change by adding filter and order clauses
   * @param values binding values related to baseSQL
   * @param attribute2dbField collection of pairs attributeName, corresponding database column (table.column) - for ALL fields is the select clause
   * @param valueObjectClass value object class to use to generate the result
   * @param booleanTrueValue read value to interpret as true
   * @param booleanFalseValue read value to interpret as false
   * @param context servlet context
   * @param gridParams grid parameters (filtering/ordering settings, starting row to read, read versus)
   * @param logQuery <code>true</code> to log the query, <code>false</code> to no log the query
   * @param progressiveSYS13 window identifier
   * @return a list of value objects or an error response
   */
  public static Response getQuery(
    Connection conn,
    UserSessionParameters userSessionPars,
    String baseSQL,
    ArrayList values,
    Map attribute2dbField,
    Class valueObjectClass,
    String booleanTrueValue,
    String booleanFalseValue,
    ServletContext context,
    GridParams gridParams,
    boolean logQuery,
    ArrayList list
  ) throws Exception {
	if (list==null)
		list = new ArrayList();
	  
    String select = baseSQL.substring(0,baseSQL.toLowerCase().indexOf(" from "));
    String otherSQL = baseSQL.substring(baseSQL.toLowerCase().indexOf(" from "));
    WindowCustomizationVO vo = null;
    for(int i=0;i<list.size();i++) {
      vo = (WindowCustomizationVO)list.get(i);
      attribute2dbField.put(
          vo.getAttributeNameSYS12(),
          vo.getTableNameSYS13()+"."+vo.getColumnNameSYS12()
      );
      select += ","+vo.getTableNameSYS13()+"."+vo.getColumnNameSYS12();
    }
    baseSQL = select+otherSQL;

    return QueryUtil.getQuery(
      conn,
      userSessionPars,
      baseSQL,
      values,
      attribute2dbField,
      valueObjectClass,
      booleanTrueValue,
      booleanFalseValue,
      context,
      gridParams,
      logQuery
    );
  }


  /**
  * This method read a block of record from the result set.
  * @param baseSQL SQL to change by adding filter and order clauses
  * @param values binding values related to baseSQL
  * @param attribute2dbField collection of pairs attributeName, corresponding database column (table.column) - for ALL fields is the select clause
  * @param valueObjectClass value object class to use to generate the result
  * @param booleanTrueValue read value to interpret as true
  * @param booleanFalseValue read value to interpret as false
  * @param context servlet context
  * @param gridParams grid parameters (filtering/ordering settings, starting row to read, read versus)
  * @param blockSize number of rows to read
  * @param logQuery <code>true</code> to log the query, <code>false</code> to no log the query
  * @param progressiveSYS13 window identifier
  * @return a list of value objects or an error response
  */
  public static Response getQuery(
    Connection conn,
    UserSessionParameters userSessionPars,
    String baseSQL,
    ArrayList values,
    Map attribute2dbField,
    Class valueObjectClass,
    String booleanTrueValue,
    String booleanFalseValue,
    ServletContext context,
    GridParams gridParams,
    int blockSize,
    boolean logQuery,
    ArrayList list
  ) throws Exception {
    if (list==null)
	  list = new ArrayList();

    String select = baseSQL.substring(0,baseSQL.toLowerCase().indexOf(" from "));
    String otherSQL = baseSQL.substring(baseSQL.toLowerCase().indexOf(" from "));

    WindowCustomizationVO vo = null;
    for(int i=0;i<list.size();i++) {
      vo = (WindowCustomizationVO)list.get(i);
      attribute2dbField.put(
          vo.getAttributeNameSYS12(),
          vo.getTableNameSYS13()+"."+vo.getColumnNameSYS12()
      );
      select += ","+vo.getTableNameSYS13()+"."+vo.getColumnNameSYS12();
    }
    baseSQL = select+otherSQL;

    return QueryUtil.getQuery(
      conn,
      userSessionPars,
      baseSQL,
      values,
      attribute2dbField,
      valueObjectClass,
      booleanTrueValue,
      booleanFalseValue,
      context,
      gridParams,
      blockSize,
      logQuery
    );
  }


  /**
  * This method read one record from the result set.
  * @param baseSQL SQL to change by adding filter and order clauses
  * @param values binding values related to baseSQL
  * @param attribute2dbField collection of pairs attributeName, corresponding database column (table.column) - for ALL fields is the select clause
  * @param valueObjectClass value object class to use to generate the result
  * @param booleanTrueValue read value to interpret as true
  * @param booleanFalseValue read value to interpret as false
  * @param context servlet context
  * @param logQuery <code>true</code> to log the query, <code>false</code> to no log the query
  * @param progressiveSYS13 window identifier
  * @return a list of value objects or an error response
  */
  public static Response getQuery(
    Connection conn,
    UserSessionParameters userSessionPars,
    String baseSQL,
    ArrayList values,
    Map attribute2dbField,
    Class valueObjectClass,
    String booleanTrueValue,
    String booleanFalseValue,
    ServletContext context,
    boolean logQuery,
    ArrayList list
  ) throws Exception {
	if (list==null)
  	  list = new ArrayList();

    String select = baseSQL.substring(0,baseSQL.toLowerCase().indexOf(" from "));
    String otherSQL = baseSQL.substring(baseSQL.toLowerCase().indexOf(" from "));

    WindowCustomizationVO vo = null;
    for(int i=0;i<list.size();i++) {
      vo = (WindowCustomizationVO)list.get(i);
      attribute2dbField.put(
          vo.getAttributeNameSYS12(),
          vo.getTableNameSYS13()+"."+vo.getColumnNameSYS12()
      );
      select += ","+vo.getTableNameSYS13()+"."+vo.getColumnNameSYS12();
    }
    baseSQL = select+otherSQL;

    return QueryUtil.getQuery(
      conn,
      userSessionPars,
      baseSQL,
      values,
      attribute2dbField,
      valueObjectClass,
      booleanTrueValue,
      booleanFalseValue,
      context,
      logQuery
    );

  }



   /**
    * This method esecute an insert on a table, by means of the value object and a subset of its fields: all field related to that table.
    * @param vo value object to use on insert
    * @param tableName table name to use on insert
    * @param attribute2dbField collection of pairs attributeName, corresponding database column (table.column) - for ALL fields related to the specified table
    * @param booleanTrueValue value to interpret as true
    * @param booleanFalseValue value to interpret as false
    * @param context servlet context
    * @param logSQL <code>true</code> to log the SQL, <code>false</code> to no log the SQL
    * @param progressiveSYS13 window identifier
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
       boolean logSQL,
       ArrayList list
   ) throws Exception {
	if (list==null)
		list = new ArrayList();

    WindowCustomizationVO winVO = null;
    for(int i=0;i<list.size();i++) {
      winVO = (WindowCustomizationVO)list.get(i);
      attribute2dbField.put(
          winVO.getAttributeNameSYS12(),
          winVO.getColumnNameSYS12()
      );
    }

    return QueryUtil.insertTable(
       conn,
       userSessionPars,
       vo,
       tableName,
       attribute2dbField,
       booleanTrueValue,
       booleanFalseValue,
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
   * @param context servlet context
   * @param logSQL <code>true</code> to log the SQL, <code>false</code> to no log the SQL
   * @param progressiveSYS13 window identifier
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
      boolean logSQL,
      ArrayList list
  ) throws Exception {
	if (list==null)
		list = new ArrayList();

    WindowCustomizationVO winVO = null;
    for(int i=0;i<list.size();i++) {
      winVO = (WindowCustomizationVO)list.get(i);
      attribute2dbField.put(
          winVO.getAttributeNameSYS12(),
          winVO.getColumnNameSYS12()
      );
    }

    return QueryUtil.updateTable(
        conn,
        userSessionPars,
        pkAttributes,
        oldVO,
        newVO,
        tableName,
        attribute2dbField,
        booleanTrueValue,
        booleanFalseValue,
        context,
        logSQL
    );
  }



}
