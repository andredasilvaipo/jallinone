package org.jallinone.employees.java;

import org.jallinone.subjects.java.PeopleVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value Object used to store an employee.</p>
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
public class DetailEmployeeVO extends PeopleVO {

  private String employeeCodeSCH01;
  private String taskCodeReg07SCH01;
  private String taskDescriptionSYS10;
  private String divisionSCH01;
  private String officeSCH01;
  private String phoneNumberSCH01;
  private String emailAddressSCH01;
  private java.math.BigDecimal managerProgressiveReg04SCH01;
  private java.math.BigDecimal assistantProgressiveReg04SCH01;
  private java.math.BigDecimal salarySCH01;
  private String levelSCH01;
  private String enabledSCH01;
  private java.sql.Date engagedDateSCH01;
  private java.sql.Date dismissalDateSCH01;
  private String managerCompanyCodeSys01SCH01;
  private String assistantCompanyCodeSys01SCH01;
  private String managerName_1REG04;
  private String managerName_2REG04;
  private String assistantName_1REG04;
  private String assistantName_2REG04;
  private String companyCodeSys01SCH01;
  private java.math.BigDecimal progressiveReg04SCH01;


  public DetailEmployeeVO() {
  }


  public String getEmployeeCodeSCH01() {
    return employeeCodeSCH01;
  }
  public void setEmployeeCodeSCH01(String employeeCodeSCH01) {
    this.employeeCodeSCH01 = employeeCodeSCH01;
  }
  public String getTaskCodeReg07SCH01() {
    return taskCodeReg07SCH01;
  }
  public void setTaskCodeReg07SCH01(String taskCodeReg07SCH01) {
    this.taskCodeReg07SCH01 = taskCodeReg07SCH01;
  }
  public String getTaskDescriptionSYS10() {
    return taskDescriptionSYS10;
  }
  public void setTaskDescriptionSYS10(String taskDescriptionSYS10) {
    this.taskDescriptionSYS10 = taskDescriptionSYS10;
  }
  public String getDivisionSCH01() {
    return divisionSCH01;
  }
  public void setDivisionSCH01(String divisionSCH01) {
    this.divisionSCH01 = divisionSCH01;
  }
  public String getOfficeSCH01() {
    return officeSCH01;
  }
  public void setOfficeSCH01(String officeSCH01) {
    this.officeSCH01 = officeSCH01;
  }
  public String getPhoneNumberSCH01() {
    return phoneNumberSCH01;
  }
  public void setPhoneNumberSCH01(String phoneNumberSCH01) {
    this.phoneNumberSCH01 = phoneNumberSCH01;
  }
  public String getEmailAddressSCH01() {
    return emailAddressSCH01;
  }
  public void setEmailAddressSCH01(String emailAddressSCH01) {
    this.emailAddressSCH01 = emailAddressSCH01;
  }
  public java.math.BigDecimal getManagerProgressiveReg04SCH01() {
    return managerProgressiveReg04SCH01;
  }
  public void setManagerProgressiveReg04SCH01(java.math.BigDecimal managerProgressiveReg04SCH01) {
    this.managerProgressiveReg04SCH01 = managerProgressiveReg04SCH01;
  }
  public java.math.BigDecimal getAssistantProgressiveReg04SCH01() {
    return assistantProgressiveReg04SCH01;
  }
  public void setAssistantProgressiveReg04SCH01(java.math.BigDecimal assistantProgressiveReg04SCH01) {
    this.assistantProgressiveReg04SCH01 = assistantProgressiveReg04SCH01;
  }
  public java.math.BigDecimal getSalarySCH01() {
    return salarySCH01;
  }
  public void setSalarySCH01(java.math.BigDecimal salarySCH01) {
    this.salarySCH01 = salarySCH01;
  }
  public String getLevelSCH01() {
    return levelSCH01;
  }
  public void setLevelSCH01(String levelSCH01) {
    this.levelSCH01 = levelSCH01;
  }
  public String getEnabledSCH01() {
    return enabledSCH01;
  }
  public void setEnabledSCH01(String enabledSCH01) {
    this.enabledSCH01 = enabledSCH01;
  }
  public java.sql.Date getEngagedDateSCH01() {
    return engagedDateSCH01;
  }
  public void setEngagedDateSCH01(java.sql.Date engagedDateSCH01) {
    this.engagedDateSCH01 = engagedDateSCH01;
  }
  public java.sql.Date getDismissalDateSCH01() {
    return dismissalDateSCH01;
  }
  public void setDismissalDateSCH01(java.sql.Date dismissalDateSCH01) {
    this.dismissalDateSCH01 = dismissalDateSCH01;
  }
  public String getManagerCompanyCodeSys01SCH01() {
    return managerCompanyCodeSys01SCH01;
  }
  public void setManagerCompanyCodeSys01SCH01(String managerCompanyCodeSys01SCH01) {
    this.managerCompanyCodeSys01SCH01 = managerCompanyCodeSys01SCH01;
  }
  public String getAssistantCompanyCodeSys01SCH01() {
    return assistantCompanyCodeSys01SCH01;
  }
  public void setAssistantCompanyCodeSys01SCH01(String assistantCompanyCodeSys01SCH01) {
    this.assistantCompanyCodeSys01SCH01 = assistantCompanyCodeSys01SCH01;
  }
  public String getManagerName_1REG04() {
    return managerName_1REG04;
  }
  public void setManagerName_1REG04(String managerName_1REG04) {
    this.managerName_1REG04 = managerName_1REG04;
  }
  public String getManagerName_2REG04() {
    return managerName_2REG04;
  }
  public void setManagerName_2REG04(String managerName_2REG04) {
    this.managerName_2REG04 = managerName_2REG04;
  }
  public String getAssistantName_1REG04() {
    return assistantName_1REG04;
  }
  public void setAssistantName_1REG04(String assistantName_1REG04) {
    this.assistantName_1REG04 = assistantName_1REG04;
  }
  public String getAssistantName_2REG04() {
    return assistantName_2REG04;
  }
  public void setAssistantName_2REG04(String assistantName_2REG04) {
    this.assistantName_2REG04 = assistantName_2REG04;
  }
  public String getCompanyCodeSys01SCH01() {
    return companyCodeSys01SCH01;
  }
  public void setCompanyCodeSys01SCH01(String companyCodeSys01SCH01) {
    this.companyCodeSys01SCH01 = companyCodeSys01SCH01;
  }
  public java.math.BigDecimal getProgressiveReg04SCH01() {
    return progressiveReg04SCH01;
  }
  public void setProgressiveReg04SCH01(java.math.BigDecimal progressiveReg04SCH01) {
    this.progressiveReg04SCH01 = progressiveReg04SCH01;
  }

}