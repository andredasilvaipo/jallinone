package org.jallinone.scheduler.activities.java;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a scheduled activity assigned to a specified employee.</p>
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
public class EmployeeActivityVO extends GridScheduledActivityVO {

  private String name_1REG04;
  private String name_2REG04;
  private String employeeCodeSCH01;
  private java.math.BigDecimal progressiveReg04SCH07;
  private java.sql.Timestamp endDateSCH07;
  private java.math.BigDecimal durationSCH07;
  private java.sql.Timestamp startDateSCH07;
  public EmployeeActivityVO() {
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
  public java.math.BigDecimal getProgressiveReg04SCH07() {
    return progressiveReg04SCH07;
  }
  public void setProgressiveReg04SCH07(java.math.BigDecimal progressiveReg04SCH07) {
    this.progressiveReg04SCH07 = progressiveReg04SCH07;
  }
  public java.sql.Timestamp getStartDateSCH07() {
    return startDateSCH07;
  }
  public void setStartDateSCH07(java.sql.Timestamp startDateSCH07) {
    this.startDateSCH07 = startDateSCH07;
  }
  public java.sql.Timestamp getEndDateSCH07() {
    return endDateSCH07;
  }
  public void setEndDateSCH07(java.sql.Timestamp endDateSCH07) {
    this.endDateSCH07 = endDateSCH07;
  }
  public java.math.BigDecimal getDurationSCH07() {
    return durationSCH07;
  }
  public void setDurationSCH07(java.math.BigDecimal durationSCH07) {
    this.durationSCH07 = durationSCH07;
  }

}
