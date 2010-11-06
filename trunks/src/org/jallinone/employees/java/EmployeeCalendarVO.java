package org.jallinone.employees.java;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.gantt.java.GanttWorkingHours;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value Object used to store an employee working week day.</p>
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
public class EmployeeCalendarVO extends ValueObjectImpl implements GanttWorkingHours {

  private String companyCodeSys01SCH02;
  private String dayOfWeekSCH02;
  private java.math.BigDecimal progressiveReg04SCH02;
  private java.sql.Timestamp morningStartHourSCH02;
  private java.sql.Timestamp morningEndHourSCH02;
  private java.sql.Timestamp afternoonStartHourSCH02;
  private java.sql.Timestamp afternoonEndHourSCH02;


  public EmployeeCalendarVO() {}


  public String getCompanyCodeSys01SCH02() {
    return companyCodeSys01SCH02;
  }
  public void setCompanyCodeSys01SCH02(String companyCodeSys01SCH02) {
    this.companyCodeSys01SCH02 = companyCodeSys01SCH02;
  }
  public String getDayOfWeekSCH02() {
    return dayOfWeekSCH02;
  }
  public void setDayOfWeekSCH02(String dayOfWeekSCH02) {
    this.dayOfWeekSCH02 = dayOfWeekSCH02;
  }
  public java.math.BigDecimal getProgressiveReg04SCH02() {
    return progressiveReg04SCH02;
  }
  public void setProgressiveReg04SCH02(java.math.BigDecimal progressiveReg04SCH02) {
    this.progressiveReg04SCH02 = progressiveReg04SCH02;
  }
  public java.sql.Timestamp getMorningStartHourSCH02() {
    return morningStartHourSCH02;
  }
  public void setMorningStartHourSCH02(java.sql.Timestamp morningStartHourSCH02) {
    this.morningStartHourSCH02 = morningStartHourSCH02;
  }
  public java.sql.Timestamp getMorningEndHourSCH02() {
    return morningEndHourSCH02;
  }
  public void setMorningEndHourSCH02(java.sql.Timestamp morningEndHourSCH02) {
    this.morningEndHourSCH02 = morningEndHourSCH02;
  }
  public java.sql.Timestamp getAfternoonStartHourSCH02() {
    return afternoonStartHourSCH02;
  }
  public void setAfternoonStartHourSCH02(java.sql.Timestamp afternoonStartHourSCH02) {
    this.afternoonStartHourSCH02 = afternoonStartHourSCH02;
  }
  public java.sql.Timestamp getAfternoonEndHourSCH02() {
    return afternoonEndHourSCH02;
  }
  public void setAfternoonEndHourSCH02(java.sql.Timestamp afternoonEndHourSCH02) {
    this.afternoonEndHourSCH02 = afternoonEndHourSCH02;
  }


  public java.sql.Timestamp getMorningStartHour() {
    return morningStartHourSCH02;
  }
  public void setMorningStartHour(java.sql.Timestamp morningStartHour) {
    setMorningStartHourSCH02(morningStartHour);
  }
  public java.sql.Timestamp getMorningEndHour() {
    return morningEndHourSCH02;
  }
  public void setMorningEndHour(java.sql.Timestamp morningEndHour) {
    setMorningEndHourSCH02(morningEndHour);
  }
  public java.sql.Timestamp getAfternoonStartHour() {
    return afternoonStartHourSCH02;
  }
  public void setAfternoonStartHour(java.sql.Timestamp afternoonStartHour) {
    setAfternoonStartHourSCH02(afternoonStartHour);
  }
  public java.sql.Timestamp getAfternoonEndHour() {
    return afternoonEndHourSCH02;
  }
  public void setAfternoonEndHour(java.sql.Timestamp afternoonEndHour) {
    setAfternoonEndHourSCH02(afternoonEndHour);
  }


}
