package org.jallinone.system.customizations.java;

import org.openswing.swing.message.receive.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to define column properties for the query defined in SYS16.</p>
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
public class CustomColumnVO extends ValueObjectImpl {


  private String columnTypeSYS22;
  private String constraintValuesSYS22;
  private String columnNameSYS22;
  private String functionCodeSys06SYS22;
  private boolean columnVisibleSYS22;
  private String defaultValueTextSYS22;
  private java.math.BigDecimal defaultValueNumSYS22;
  private Boolean defaultValueDateSYS22;
  private boolean isParamSYS22;
  private boolean isParamRequiredSYS22;


  public CustomColumnVO() {
  }


  public String getFunctionCodeSys06SYS22() {
    return functionCodeSys06SYS22;
  }
  public void setFunctionCodeSys06SYS22(String functionCodeSys06SYS22) {
    this.functionCodeSys06SYS22 = functionCodeSys06SYS22;
  }
  public String getColumnNameSYS22() {
    return columnNameSYS22;
  }
  public void setColumnNameSYS22(String columnNameSYS22) {
    this.columnNameSYS22 = columnNameSYS22;
  }
  public String getColumnTypeSYS22() {
    return columnTypeSYS22;
  }
  public void setColumnTypeSYS22(String columnTypeSYS22) {
    this.columnTypeSYS22 = columnTypeSYS22;
  }
  public Boolean getDefaultValueDateSYS22() {
    return defaultValueDateSYS22;
  }
  public void setDefaultValueDateSYS22(Boolean defaultValueDateSYS22) {
    this.defaultValueDateSYS22 = defaultValueDateSYS22;
  }
  public String getDefaultValueTextSYS22() {
    return defaultValueTextSYS22;
  }
  public void setDefaultValueTextSYS22(String defaultValueTextSYS22) {
    this.defaultValueTextSYS22 = defaultValueTextSYS22;
  }
  public java.math.BigDecimal getDefaultValueNumSYS22() {
    return defaultValueNumSYS22;
  }
  public void setDefaultValueNumSYS22(java.math.BigDecimal defaultValueNumSYS22) {
    this.defaultValueNumSYS22 = defaultValueNumSYS22;
  }
  public String getConstraintValuesSYS22() {
    return constraintValuesSYS22;
  }
  public void setConstraintValuesSYS22(String constraintValuesSYS22) {
    this.constraintValuesSYS22 = constraintValuesSYS22;
  }
  public boolean isColumnVisibleSYS22() {
    return columnVisibleSYS22;
  }
  public void setColumnVisibleSYS22(boolean columnVisibleSYS22) {
    this.columnVisibleSYS22 = columnVisibleSYS22;
  }
  public boolean isIsParamRequiredSYS22() {
    return isParamRequiredSYS22;
  }
  public boolean isIsParamSYS22() {
    return isParamSYS22;
  }
  public void setIsParamRequiredSYS22(boolean isParamRequiredSYS22) {
    this.isParamRequiredSYS22 = isParamRequiredSYS22;
  }
  public void setIsParamSYS22(boolean isParamSYS22) {
    this.isParamSYS22 = isParamSYS22;
  }

}
