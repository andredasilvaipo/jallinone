package org.jallinone.system.customizations.java;

import org.openswing.swing.message.receive.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object related to a custom function.</p>
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
public class CustomFunctionVO extends ValueObjectImpl {


  private java.math.BigDecimal progressiveSys10SYS06;
  private String sql4SYS16;
  private String functionCodeSys06SYS16;
  private String descriptionSYS10;
  private String sql2SYS16;
  private boolean autoLoadDataSYS16;
  private String sql5SYS16;
  private String sql3SYS16;
  private String sql1SYS16;
  private String noteSYS16;
  private String mainTablesSYS16;
  private boolean useCompanyCodeSYS06;
  private java.math.BigDecimal progressiveHie01SYS18;
  private String levelDescriptionSYS10;


  public CustomFunctionVO() {
  }


  public java.math.BigDecimal getProgressiveSys10SYS06() {
    return progressiveSys10SYS06;
  }
  public void setProgressiveSys10SYS06(java.math.BigDecimal progressiveSys10SYS06) {
    this.progressiveSys10SYS06 = progressiveSys10SYS06;
  }
  public String getSql2SYS16() {
    return sql2SYS16;
  }
  public void setSql2SYS16(String sql2SYS16) {
    this.sql2SYS16 = sql2SYS16;
  }
  public String getSql4SYS16() {
    return sql4SYS16;
  }
  public void setSql4SYS16(String sql4SYS16) {
    this.sql4SYS16 = sql4SYS16;
  }
  public String getSql3SYS16() {
    return sql3SYS16;
  }
  public void setSql3SYS16(String sql3SYS16) {
    this.sql3SYS16 = sql3SYS16;
  }
  public String getSql1SYS16() {
    return sql1SYS16;
  }
  public void setSql1SYS16(String sql1SYS16) {
    this.sql1SYS16 = sql1SYS16;
  }
  public String getSql5SYS16() {
    return sql5SYS16;
  }
  public void setSql5SYS16(String sql5SYS16) {
    this.sql5SYS16 = sql5SYS16;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public String getFunctionCodeSys06SYS16() {
    return functionCodeSys06SYS16;
  }
  public void setFunctionCodeSys06SYS16(String functionCodeSys06SYS16) {
    this.functionCodeSys06SYS16 = functionCodeSys06SYS16;
  }
  public boolean getAutoLoadDataSYS16() {
    return autoLoadDataSYS16;
  }
  public void setAutoLoadDataSYS16(boolean autoLoadDataSYS16) {
    this.autoLoadDataSYS16 = autoLoadDataSYS16;
  }
  public String getNoteSYS16() {
    return noteSYS16;
  }
  public void setNoteSYS16(String noteSYS16) {
    this.noteSYS16 = noteSYS16;
  }
  public String getMainTablesSYS16() {
    return mainTablesSYS16;
  }
  public void setMainTablesSYS16(String mainTablesSYS16) {
    this.mainTablesSYS16 = mainTablesSYS16;
  }


  public String getSql() {
    return
        (sql1SYS16==null?"":sql1SYS16)+
        (sql2SYS16==null?"":sql2SYS16)+
        (sql3SYS16==null?"":sql3SYS16)+
        (sql4SYS16==null?"":sql4SYS16)+
        (sql5SYS16==null?"":sql5SYS16);
  }


  public void setSql(String sql) {
    if (sql==null)
      sql1SYS16 = sql;
    else {
      if (sql.length()<=2000)
        sql1SYS16 = sql;
      else {
        sql1SYS16 = sql.substring(0,2000);
        sql = sql.substring(2000);
        if (sql.length()<=2000)
          sql2SYS16 = sql;
        else {
          sql2SYS16 = sql.substring(0,2000);
          sql = sql.substring(2000);
          if (sql.length()<=2000)
            sql3SYS16 = sql;
          else {
            sql3SYS16 = sql.substring(0,2000);
            sql = sql.substring(2000);
            if (sql.length()<=2000)
              sql4SYS16 = sql;
            else {
              sql4SYS16 = sql.substring(0,2000);
              sql = sql.substring(2000);
              if (sql.length()<=2000)
                sql5SYS16 = sql;
              else {
                sql5SYS16 = sql.substring(0,2000);
              }
            }
          }
        }
      }
    }
  }
  public boolean isUseCompanyCodeSYS06() {
    return useCompanyCodeSYS06;
  }
  public void setUseCompanyCodeSYS06(boolean useCompanyCodeSYS06) {
    this.useCompanyCodeSYS06 = useCompanyCodeSYS06;
  }
  public String getLevelDescriptionSYS10() {
    return levelDescriptionSYS10;
  }
  public java.math.BigDecimal getProgressiveHie01SYS18() {
    return progressiveHie01SYS18;
  }
  public void setProgressiveHie01SYS18(java.math.BigDecimal progressiveHie01SYS18) {
    this.progressiveHie01SYS18 = progressiveHie01SYS18;
  }
  public void setLevelDescriptionSYS10(String levelDescriptionSYS10) {
    this.levelDescriptionSYS10 = levelDescriptionSYS10;
  }


}
