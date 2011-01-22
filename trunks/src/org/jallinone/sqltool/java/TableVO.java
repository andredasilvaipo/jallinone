package org.jallinone.sqltool.java;

import org.openswing.swing.message.receive.java.ValueObjectImpl;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Enumeration;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to define a parameter in query defined in SYS16.</p>
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
public class TableVO extends ValueObjectImpl {

  /** SQL script */
  private String sql;

  /** list of ColumnVO objects */
  private ArrayList columns = new ArrayList();

  /** list of String related to tables where insert/update operations are allowed */
  private ArrayList mainTables = new ArrayList();

  /** convert column header from column name to "decorated" column name */
  private boolean convertColumnHeaders;

  /** collection of objects <tablename,ArrayList of tablename.columnname> */
  private Hashtable primaryKeys = new Hashtable();

  /** collection of objects <FKEntryVO,ForeignKeyVO> */
  private Hashtable foreingKeys = new Hashtable();

  /** collection of objects <table.columnname,attributeName> */
  private Hashtable columnsMapping = new Hashtable();


  public TableVO() {}


  public TableVO(String sql,ArrayList mainTables,boolean convertColumnHeaders) {
    this.sql = sql;
    this.mainTables = mainTables;
    this.convertColumnHeaders = convertColumnHeaders;
  }


  public String getSql() {
    return sql;
  }


  public void addColumn(ColumnVO vo) {
    columns.add(vo);
    columnsMapping.put(vo.getColumnName(),vo.getAttributeName());
  }


  public ArrayList getColumns() {
    return columns;
  }
  public ArrayList getMainTables() {
    return mainTables;
  }
  public boolean isConvertColumnHeaders() {
    return convertColumnHeaders;
  }


  public void addPrimaryKeys(String tableName,ArrayList pks) {
    primaryKeys.put(tableName,pks);
  }


  public ArrayList getAllTables() {
    ArrayList list = new ArrayList();
    Enumeration en = primaryKeys.keys();
    while(en.hasMoreElements()) {
      list.add(en.nextElement());
    }
    return list;
  }


  public ArrayList getPrimaryKeys(String tableName) {
    return (ArrayList)primaryKeys.get(tableName);
  }


  public boolean isPrimaryKey(String columnName) {
    if (columnName.indexOf(".")==-1) {
			ArrayList pks = getPrimaryKeys("");
			if (pks==null)
				return false;
			return pks.contains(columnName);
    }
		ArrayList pks = getPrimaryKeys(columnName.substring(0,columnName.indexOf(".")));
    if (pks==null)
      return false;
    return pks.contains(columnName);
  }
  public void setSql(String sql) {
    this.sql = sql;
  }
  public Hashtable getForeingKeys() {
    return foreingKeys;
  }
  public void setForeingKeys(Hashtable foreingKeys) {
    this.foreingKeys = foreingKeys;
  }



  public void setColumns(ArrayList columns) {
	  this.columns = columns;
  }


  public void setMainTables(ArrayList mainTables) {
	  this.mainTables = mainTables;
  }


  public void setConvertColumnHeaders(boolean convertColumnHeaders) {
	  this.convertColumnHeaders = convertColumnHeaders;
  }


  public void setPrimaryKeys(Hashtable primaryKeys) {
	  this.primaryKeys = primaryKeys;
  }


  public void setColumnsMapping(Hashtable columnsMapping) {
	  this.columnsMapping = columnsMapping;
  }


/**
   * @param columnName table.columnname
   * @return related attribute name
   */
  public String getAttributeName(String columnName) {
    return (String)columnsMapping.get(columnName);
  }


}
