package org.jallinone.employees.java;

import org.openswing.swing.message.receive.java.*;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value Object used to store an employee limited info.</p>
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
public class GridEmployeeVO extends ValueObjectImpl {

  private String companyCodeSys01SCH01;
  private String employeeCodeSCH01;
  private String name_1REG04;
  private String name_2REG04;
  private String descriptionSYS10;
  private String phoneNumberSCH01;
  private String officeSCH01;
  private java.math.BigDecimal progressiveReg04SCH01;
  private String taskCodeReg07SCH01;


  public GridEmployeeVO() {
  }


  public String getCompanyCodeSys01SCH01() {
    return companyCodeSys01SCH01;
  }
  public void setCompanyCodeSys01SCH01(String companyCodeSys01SCH01) {
    this.companyCodeSys01SCH01 = companyCodeSys01SCH01;
  }
  public String getEmployeeCodeSCH01() {
    return employeeCodeSCH01;
  }
  public void setEmployeeCodeSCH01(String employeeCodeSCH01) {
    this.employeeCodeSCH01 = employeeCodeSCH01;
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
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public String getPhoneNumberSCH01() {
    return phoneNumberSCH01;
  }
  public void setPhoneNumberSCH01(String phoneNumberSCH01) {
    this.phoneNumberSCH01 = phoneNumberSCH01;
  }
  public String getOfficeSCH01() {
    return officeSCH01;
  }
  public void setOfficeSCH01(String officeSCH01) {
    this.officeSCH01 = officeSCH01;
  }
  public java.math.BigDecimal getProgressiveReg04SCH01() {
    return progressiveReg04SCH01;
  }
  public void setProgressiveReg04SCH01(java.math.BigDecimal progressiveReg04SCH01) {
    this.progressiveReg04SCH01 = progressiveReg04SCH01;
  }
  public String getTaskCodeReg07SCH01() {
    return taskCodeReg07SCH01;
  }
  public void setTaskCodeReg07SCH01(String taskCodeReg07SCH01) {
    this.taskCodeReg07SCH01 = taskCodeReg07SCH01;
  }

}
