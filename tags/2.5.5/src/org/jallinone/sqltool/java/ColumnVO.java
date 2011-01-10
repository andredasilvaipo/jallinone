package org.jallinone.sqltool.java;

import org.openswing.swing.message.receive.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to define a table column info.</p>
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
public class ColumnVO extends ValueObjectImpl {


  private String columnType;
  private java.math.BigDecimal columnSize;
  private java.math.BigDecimal columnDec;
  private int columnSqlType;
  private String columnName;
  private String attributeName;
  private String columnHeaderName;
  private boolean columnRequired;
  private java.util.ArrayList columnValues;
  private Object defaultValue;
  private boolean columnVisible;
  private TableVO tableVO = null;

  /** indicate is the current column must be calculated as a progressive */
  private boolean progressive;

  
  public ColumnVO() {}

  
  public ColumnVO(TableVO tableVO) {
    this.tableVO = tableVO;
  }


  public String getColumnName() {
    return columnName;
  }

  public String getColumnNameWithoutTable() {
    try {
      return columnName.substring(columnName.indexOf(".") + 1);
    }
    catch (Exception ex) {
      return columnName;
    }
  }


  public void setColumnName(String columnName) {
    this.columnName = columnName;
  }
  public String getColumnType() {
    return columnType;
  }
  public void setColumnType(String columnType) {
    this.columnType = columnType;
  }
  public java.math.BigDecimal getColumnSize() {
    return columnSize;
  }
  public void setColumnSize(java.math.BigDecimal columnSize) {
    this.columnSize = columnSize;
  }
  public java.math.BigDecimal getColumnDec() {
    return columnDec;
  }
  public void setColumnDec(java.math.BigDecimal columnDec) {
    this.columnDec = columnDec;
  }
  public String getAttributeName() {
    return attributeName;
  }
  public void setAttributeName(String attributeName) {
    this.attributeName = attributeName;
  }
  public String getColumnHeaderName() {
    return columnHeaderName;
  }
  public void setColumnHeaderName(String columnHeaderName) {
    this.columnHeaderName = columnHeaderName;
  }
  public boolean getColumnRequired() {
    return columnRequired;
  }
  public void setColumnRequired(boolean columnRequired) {
    this.columnRequired = columnRequired;
  }
  public java.util.ArrayList getColumnValues() {
    return columnValues;
  }
  public void setColumnValues(java.util.ArrayList columnValues) {
    this.columnValues = columnValues;
  }
  public Object getDefaultValue() {
    return defaultValue;
  }
  public void setDefaultValue(Object defaultValue) {
    this.defaultValue = defaultValue;
  }
  public boolean getColumnVisible() {
    return columnVisible;
  }
  public void setColumnVisible(boolean columnVisible) {
    this.columnVisible = columnVisible;
  }
  public int getColumnSqlType() {
    return columnSqlType;
  }
  public void setColumnSqlType(int columnSqlType) {
    this.columnSqlType = columnSqlType;
  }


  public boolean getPrimaryKey() {
	  return tableVO.isPrimaryKey(columnName);
  }
  public boolean getProgressive() {
	  return progressive;
  }
  public void setProgressive(boolean progressive) {
	  this.progressive = progressive;
  }


  public void setTableVO(TableVO tableVO) {
	  this.tableVO = tableVO;
  }


  
  
}
