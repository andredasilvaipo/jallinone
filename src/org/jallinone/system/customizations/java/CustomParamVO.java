package org.jallinone.system.customizations.java;

import org.openswing.swing.message.receive.java.*;


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
public class CustomParamVO extends ValueObjectImpl {


  private String columnTypeSYS17;
  private java.math.BigDecimal columnSizeSYS17;
  private java.math.BigDecimal columnDecSYS17;
  private java.math.BigDecimal progressiveSys10SYS17;
  private String lookupCodeSYS17;
  private String descriptionSYS10;
  private String columnNameSYS17;
  private String lookupSqlSYS17;
  private String functionCodeSys06SYS17;


  public CustomParamVO() {
  }


  public String getFunctionCodeSys06SYS17() {
    return functionCodeSys06SYS17;
  }
  public void setFunctionCodeSys06SYS17(String functionCodeSys06SYS17) {
    this.functionCodeSys06SYS17 = functionCodeSys06SYS17;
  }
  public String getColumnNameSYS17() {
    return columnNameSYS17;
  }
  public void setColumnNameSYS17(String columnNameSYS17) {
    this.columnNameSYS17 = columnNameSYS17;
  }
  public String getColumnTypeSYS17() {
    return columnTypeSYS17;
  }
  public void setColumnTypeSYS17(String columnTypeSYS17) {
    this.columnTypeSYS17 = columnTypeSYS17;
  }
  public java.math.BigDecimal getColumnSizeSYS17() {
    return columnSizeSYS17;
  }
  public void setColumnSizeSYS17(java.math.BigDecimal columnSizeSYS17) {
    this.columnSizeSYS17 = columnSizeSYS17;
  }
  public java.math.BigDecimal getColumnDecSYS17() {
    return columnDecSYS17;
  }
  public void setColumnDecSYS17(java.math.BigDecimal columnDecSYS17) {
    this.columnDecSYS17 = columnDecSYS17;
  }
  public java.math.BigDecimal getProgressiveSys10SYS17() {
    return progressiveSys10SYS17;
  }
  public void setProgressiveSys10SYS17(java.math.BigDecimal progressiveSys10SYS17) {
    this.progressiveSys10SYS17 = progressiveSys10SYS17;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public String getLookupCodeSYS17() {
    return lookupCodeSYS17;
  }
  public void setLookupCodeSYS17(String lookupCodeSYS17) {
    this.lookupCodeSYS17 = lookupCodeSYS17;
  }
  public String getLookupSqlSYS17() {
    return lookupSqlSYS17;
  }
  public void setLookupSqlSYS17(String lookupSqlSYS17) {
    this.lookupSqlSYS17 = lookupSqlSYS17;
  }

}
