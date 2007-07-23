package org.jallinone.production.manufactures.java;

import org.openswing.swing.message.receive.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store an operation in a manufacture cycle info.</p>
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
public class OperationVO extends ValueObjectImpl {

  private String companyCodeSys01PRO04;
  private String manufactureCodePro01PRO04;
  private String taskCodeReg07PRO04;
  private String taskDescriptionSYS10;
  private String machineryCodePro03PRO04;
  private String machineryDescriptionSYS10;
  private String notePRO04;
  private String enabledPRO04;
  private java.math.BigDecimal progressiveSys10PRO04;
  private java.math.BigDecimal qtyPRO04;
  private java.math.BigDecimal durationPRO04;
  private java.math.BigDecimal valuePRO04;
  private String descriptionSYS10;
  private String operationCodePRO04;


  public OperationVO() {
  }


  public String getCompanyCodeSys01PRO04() {
    return companyCodeSys01PRO04;
  }
  public void setCompanyCodeSys01PRO04(String companyCodeSys01PRO04) {
    this.companyCodeSys01PRO04 = companyCodeSys01PRO04;
  }
  public String getManufactureCodePro01PRO04() {
    return manufactureCodePro01PRO04;
  }
  public void setManufactureCodePro01PRO04(String manufactureCodePro01PRO04) {
    this.manufactureCodePro01PRO04 = manufactureCodePro01PRO04;
  }
  public String getTaskCodeReg07PRO04() {
    return taskCodeReg07PRO04;
  }
  public void setTaskCodeReg07PRO04(String taskCodeReg07PRO04) {
    this.taskCodeReg07PRO04 = taskCodeReg07PRO04;
  }
  public String getTaskDescriptionSYS10() {
    return taskDescriptionSYS10;
  }
  public void setTaskDescriptionSYS10(String taskDescriptionSYS10) {
    this.taskDescriptionSYS10 = taskDescriptionSYS10;
  }
  public String getMachineryCodePro03PRO04() {
    return machineryCodePro03PRO04;
  }
  public void setMachineryCodePro03PRO04(String machineryCodePro03PRO04) {
    this.machineryCodePro03PRO04 = machineryCodePro03PRO04;
  }
  public String getMachineryDescriptionSYS10() {
    return machineryDescriptionSYS10;
  }
  public void setMachineryDescriptionSYS10(String machineryDescriptionSYS10) {
    this.machineryDescriptionSYS10 = machineryDescriptionSYS10;
  }
  public String getNotePRO04() {
    return notePRO04;
  }
  public void setNotePRO04(String notePRO04) {
    this.notePRO04 = notePRO04;
  }
  public String getEnabledPRO04() {
    return enabledPRO04;
  }
  public void setEnabledPRO04(String enabledPRO04) {
    this.enabledPRO04 = enabledPRO04;
  }
  public java.math.BigDecimal getProgressiveSys10PRO04() {
    return progressiveSys10PRO04;
  }
  public void setProgressiveSys10PRO04(java.math.BigDecimal progressiveSys10PRO04) {
    this.progressiveSys10PRO04 = progressiveSys10PRO04;
  }
  public java.math.BigDecimal getQtyPRO04() {
    return qtyPRO04;
  }
  public void setQtyPRO04(java.math.BigDecimal qtyPRO04) {
    this.qtyPRO04 = qtyPRO04;
  }
  public java.math.BigDecimal getDurationPRO04() {
    return durationPRO04;
  }
  public void setDurationPRO04(java.math.BigDecimal durationPRO04) {
    this.durationPRO04 = durationPRO04;
  }
  public java.math.BigDecimal getValuePRO04() {
    return valuePRO04;
  }
  public void setValuePRO04(java.math.BigDecimal valuePRO04) {
    this.valuePRO04 = valuePRO04;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public String getOperationCodePRO04() {
    return operationCodePRO04;
  }
  public void setOperationCodePRO04(String operationCodePRO04) {
    this.operationCodePRO04 = operationCodePRO04;
  }

}
