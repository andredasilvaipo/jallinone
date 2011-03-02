package org.jallinone.system.importdata.java;

import org.openswing.swing.message.receive.java.ValueObjectImpl;

/**
  * <p>Title: JAllInOne ERP/CRM application</p>
  * <p>Description: Value object used to store a specific field descriptor of an import process.</p>
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
public class ETLProcessFieldVO extends ValueObjectImpl {


  private String fieldNameSYS24;

  /** filled with a language code if fieldName is related to a progressive field for a description in SYS10 or with '*' otherwise */
  private String languageCodeSYS24;



  private String dateFormatSYS24;

  private java.math.BigDecimal progressiveSys23SYS24;
  private java.math.BigDecimal progressiveSYS24;
  private java.math.BigDecimal endPosSYS24;
  private java.math.BigDecimal posSYS24;
  private java.math.BigDecimal startPosSYS24;


  public ETLProcessFieldVO() {
  }


  public String getDateFormatSYS24() {
    return dateFormatSYS24;
  }
  public String getFieldNameSYS24() {
    return fieldNameSYS24;
  }
  public java.math.BigDecimal getEndPosSYS24() {
    return endPosSYS24;
  }
  public java.math.BigDecimal getProgressiveSys23SYS24() {
    return progressiveSys23SYS24;
  }
  public java.math.BigDecimal getStartPosSYS24() {
    return startPosSYS24;
  }
  public void setStartPosSYS24(java.math.BigDecimal startPosSYS24) {
    this.startPosSYS24 = startPosSYS24;
  }
  public void setProgressiveSys23SYS24(java.math.BigDecimal progressiveSys23SYS24) {
    this.progressiveSys23SYS24 = progressiveSys23SYS24;
  }
  public void setFieldNameSYS24(String fieldNameSYS24) {
    this.fieldNameSYS24 = fieldNameSYS24;
  }
  public void setEndPosSYS24(java.math.BigDecimal endPosSYS24) {
    this.endPosSYS24 = endPosSYS24;
  }
  public void setDateFormatSYS24(String dateFormatSYS24) {
    this.dateFormatSYS24 = dateFormatSYS24;
  }
  public String getLanguageCodeSYS24() {
    return languageCodeSYS24;
  }
  public void setLanguageCodeSYS24(String languageCodeSYS24) {
    this.languageCodeSYS24 = languageCodeSYS24;
  }
  public java.math.BigDecimal getPosSYS24() {
    return posSYS24;
  }
  public void setPosSYS24(java.math.BigDecimal posSYS24) {
    this.posSYS24 = posSYS24;
  }
  public java.math.BigDecimal getProgressiveSYS24() {
    return progressiveSYS24;
  }
  public void setProgressiveSYS24(java.math.BigDecimal progressiveSYS24) {
    this.progressiveSYS24 = progressiveSYS24;
  }



}
