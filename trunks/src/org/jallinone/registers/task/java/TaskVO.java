package org.jallinone.registers.task.java;

import org.jallinone.system.customizations.java.BaseValueObject;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store task info.</p>
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
public class TaskVO extends BaseValueObject {


  private String taskCodeREG07;
  private java.math.BigDecimal progressiveSys10REG07;
  private String enabledREG07;
  private String descriptionSYS10;
  private Boolean finiteCapacityREG07;
  private String activityDescriptionREG07;
  private String activityCodeSal09REG07;
  private String companyCodeSys01REG07;


  public TaskVO() {
  }


  public String getTaskCodeREG07() {
    return taskCodeREG07;
  }
  public void setTaskCodeREG07(String taskCodeREG07) {
    this.taskCodeREG07 = taskCodeREG07;
  }
  public java.math.BigDecimal getProgressiveSys10REG07() {
    return progressiveSys10REG07;
  }
  public void setProgressiveSys10REG07(java.math.BigDecimal progressiveSys10REG07) {
    this.progressiveSys10REG07 = progressiveSys10REG07;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public String getEnabledREG07() {
    return enabledREG07;
  }
  public void setEnabledREG07(String enabledREG07) {
    this.enabledREG07 = enabledREG07;
  }
  public Boolean getFiniteCapacityREG07() {
    return finiteCapacityREG07;
  }
  public void setFiniteCapacityREG07(Boolean finiteCapacityREG07) {
    this.finiteCapacityREG07 = finiteCapacityREG07;
  }
  public String getActivityDescriptionREG07() {
    return activityDescriptionREG07;
  }
  public void setActivityDescriptionREG07(String activityDescriptionREG07) {
    this.activityDescriptionREG07 = activityDescriptionREG07;
  }
  public String getActivityCodeSal09REG07() {
    return activityCodeSal09REG07;
  }
  public void setActivityCodeSal09REG07(String activityCodeSal09REG07) {
    this.activityCodeSal09REG07 = activityCodeSal09REG07;
  }
  public String getCompanyCodeSys01REG07() {
    return companyCodeSys01REG07;
  }
  public void setCompanyCodeSys01REG07(String companyCodeSys01REG07) {
    this.companyCodeSys01REG07 = companyCodeSys01REG07;
  }

}
