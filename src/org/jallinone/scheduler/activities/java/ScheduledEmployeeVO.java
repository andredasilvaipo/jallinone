package org.jallinone.scheduler.activities.java;

import org.jallinone.system.customizations.java.BaseValueObject;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.gantt.java.Appointment;
import java.math.BigDecimal;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a scheduled employee (table SCH07).</p>
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
public class ScheduledEmployeeVO extends ValueObjectImpl implements Appointment {

  private java.math.BigDecimal progressiveSch06SCH07;
  private java.math.BigDecimal progressiveReg04SCH07;
  private String noteSCH07;
  private String companyCodeSys01SCH07;
  private java.sql.Timestamp startDateSCH07;
  private java.sql.Timestamp endDateSCH07;
  private java.math.BigDecimal durationSCH07;
  private String name_1REG04;
  private String name_2REG04;
  private String employeeCodeSCH01;
  private String taskCodeREG07;
  private String descriptionSYS10;
  private java.awt.Color foregroundColor;
  private java.awt.Color backgroundColor;
  private String button;
  private boolean enableDelete;
  private boolean enableEdit;
  private String descriptionSCH06;


  public ScheduledEmployeeVO() {
  }


  public java.sql.Timestamp getStartDateSCH07() {
    return startDateSCH07;
  }
  public void setStartDateSCH07(java.sql.Timestamp startDateSCH07) {
    this.startDateSCH07 = startDateSCH07;
  }
  public java.math.BigDecimal getProgressiveSch06SCH07() {
    return progressiveSch06SCH07;
  }
  public void setProgressiveSch06SCH07(java.math.BigDecimal progressiveSch06SCH07) {
    this.progressiveSch06SCH07 = progressiveSch06SCH07;
  }
  public java.math.BigDecimal getProgressiveReg04SCH07() {
    return progressiveReg04SCH07;
  }
  public void setProgressiveReg04SCH07(java.math.BigDecimal progressiveReg04SCH07) {
    this.progressiveReg04SCH07 = progressiveReg04SCH07;
  }
  public String getNoteSCH07() {
    return noteSCH07;
  }
  public void setNoteSCH07(String noteSCH07) {
    this.noteSCH07 = noteSCH07;
  }
  public java.math.BigDecimal getDurationSCH07() {
    return durationSCH07;
  }
  public java.sql.Timestamp getEndDateSCH07() {
    return endDateSCH07;
  }
  public void setEndDateSCH07(java.sql.Timestamp endDateSCH07) {
    this.endDateSCH07 = endDateSCH07;
  }
  public void setDurationSCH07(java.math.BigDecimal durationSCH07) {
    this.durationSCH07 = durationSCH07;
  }
  public String getCompanyCodeSys01SCH07() {
    return companyCodeSys01SCH07;
  }
  public void setCompanyCodeSys01SCH07(String companyCodeSys01SCH07) {
    this.companyCodeSys01SCH07 = companyCodeSys01SCH07;
  }
  public String getName_1REG04() {
    return name_1REG04;
  }
  public void setName_1REG04(String name_1REG04) {
    this.name_1REG04 = name_1REG04;
  }
  public String getName_2REG04() {
    return name_2REG04;
  }
  public void setName_2REG04(String name_2REG04) {
    this.name_2REG04 = name_2REG04;
  }
  public String getEmployeeCodeSCH01() {
    return employeeCodeSCH01;
  }
  public void setEmployeeCodeSCH01(String employeeCodeSCH01) {
    this.employeeCodeSCH01 = employeeCodeSCH01;
  }
  public String getTaskCodeREG07() {
    return taskCodeREG07;
  }
  public void setTaskCodeREG07(String taskCodeREG07) {
    this.taskCodeREG07 = taskCodeREG07;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }


  public java.sql.Timestamp getStartDate() {
    return getStartDateSCH07();
  }
  public void setStartDate(java.sql.Timestamp startDate) {
    setStartDateSCH07(startDate);
  }
  public java.sql.Timestamp getEndDate() {
    return getEndDateSCH07();
  }
  public void setEndDate(java.sql.Timestamp endDate) {
    setEndDateSCH07(endDate);
  }
  public java.awt.Color getForegroundColor() {
    return foregroundColor;
  }
  public void setForegroundColor(java.awt.Color foregroundColor) {
    this.foregroundColor = foregroundColor;
  }
  public java.awt.Color getBackgroundColor() {
    return backgroundColor;
  }
  public void setBackgroundColor(java.awt.Color backgroundColor) {
    this.backgroundColor = backgroundColor;
  }
  public String getDescription() {
    return getNoteSCH07();
  }
  public void setDescription(String description) {
    setNoteSCH07(description);
  }
  public String getButton() {
    return button;
  }
  public void setButton(String button) {
    this.button = button;
  }
  public BigDecimal getDuration() {
    return getDurationSCH07();
  }
  public void setDuration(BigDecimal duration) {
    setDurationSCH07(duration);
  }
  public void setEnableEdit(boolean enableEdit) {
    this.enableEdit = enableEdit;
  }
  public boolean isEnableEdit() {
    return enableEdit;
  }
  public void setEnableDelete(boolean enableDelete) {
    this.enableDelete = enableDelete;
  }
  public boolean isEnableDelete() {
    return enableDelete;
  }
  public Object clone() {
    try {
      return super.clone();
    }
    catch (CloneNotSupportedException ex) {
      return null;
    }
  }
  public String getDescriptionSCH06() {
    return descriptionSCH06;
  }
  public void setDescriptionSCH06(String descriptionSCH06) {
    this.descriptionSCH06 = descriptionSCH06;
  }

}
