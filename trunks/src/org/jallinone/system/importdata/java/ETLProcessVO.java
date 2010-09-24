package org.jallinone.system.importdata.java;

import org.openswing.swing.message.receive.java.ValueObjectImpl;

/**
  * <p>Title: JAllInOne ERP/CRM application</p>
  * <p>Description: Value object used to store an ETL process.</p>
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
public class ETLProcessVO extends ValueObjectImpl {

  private byte[] localFile;
  private String fileFormatSYS23;
  private String companyCodeSys01SYS23;
  private String schedulingTypeSYS23;
  private java.sql.Timestamp startTimeSYS23;
  private String filenameSYS23;

  /** e.g. an item type */
  private Object subTypeValueSYS23;

  /** e.g. a pricelist code */
  private Object subTypeValue2SYS23;

  /** separator to use when extracting hierarchy levels for a record, starting from a single field in the file */
  private String levelsSepSYS23;

  /** used to fetch the correct hierarchy, in case of ImportDescriptorVO.hierarchyField filled */
  private java.math.BigDecimal progressiveHIE02;

  private java.math.BigDecimal progressiveSYS23;
  private String classNameSYS23;
  private String descriptionSYS23;


  public ETLProcessVO() {
  }


  public String getFileFormatSYS23() {
    return fileFormatSYS23;
  }
  public String getSchedulingTypeSYS23() {
    return schedulingTypeSYS23;
  }
  public java.sql.Timestamp getStartTimeSYS23() {
    return startTimeSYS23;
  }
  public void setStartTimeSYS23(java.sql.Timestamp startTimeSYS23) {
    this.startTimeSYS23 = startTimeSYS23;
  }
  public void setSchedulingTypeSYS23(String schedulingTypeSYS23) {
    this.schedulingTypeSYS23 = schedulingTypeSYS23;
  }
  public void setFileFormatSYS23(String fileFormatSYS23) {
    this.fileFormatSYS23 = fileFormatSYS23;
  }
  public java.math.BigDecimal getProgressiveSYS23() {
    return progressiveSYS23;
  }
  public void setProgressiveSYS23(java.math.BigDecimal progressiveSYS23) {
    this.progressiveSYS23 = progressiveSYS23;
  }
  public String getFilenameSYS23() {
    return filenameSYS23;
  }
  public void setFilenameSYS23(String filenameSYS23) {
    this.filenameSYS23 = filenameSYS23;
  }
  public Object getSubTypeValueSYS23() {
    return subTypeValueSYS23;
  }
  public void setSubTypeValueSYS23(Object subTypeValueSYS23) {
    this.subTypeValueSYS23 = subTypeValueSYS23;
  }
  public java.math.BigDecimal getProgressiveHIE02() {
    return progressiveHIE02;
  }
  public void setProgressiveHIE02(java.math.BigDecimal progressiveHIE02) {
    this.progressiveHIE02 = progressiveHIE02;
  }
  public String getCompanyCodeSys01SYS23() {
    return companyCodeSys01SYS23;
  }
  public void setCompanyCodeSys01SYS23(String companyCodeSys01SYS23) {
    this.companyCodeSys01SYS23 = companyCodeSys01SYS23;
  }
  public byte[] getLocalFile() {
    return localFile;
  }
  public void setLocalFile(byte[] localFile) {
    this.localFile = localFile;
  }
  public String getLevelsSepSYS23() {
    return levelsSepSYS23;
  }
  public void setLevelsSepSYS23(String levelsSepSYS23) {
    this.levelsSepSYS23 = levelsSepSYS23;
  }
  public String getClassNameSYS23() {
    return classNameSYS23;
  }
  public void setClassNameSYS23(String classNameSYS23) {
    this.classNameSYS23 = classNameSYS23;
  }
  public String getDescriptionSYS23() {
    return descriptionSYS23;
  }
  public void setDescriptionSYS23(String descriptionSYS23) {
    this.descriptionSYS23 = descriptionSYS23;
  }
  public Object getSubTypeValue2SYS23() {
    return subTypeValue2SYS23;
  }
  public void setSubTypeValue2SYS23(Object subTypeValue2SYS23) {
    this.subTypeValue2SYS23 = subTypeValue2SYS23;
  }
}
