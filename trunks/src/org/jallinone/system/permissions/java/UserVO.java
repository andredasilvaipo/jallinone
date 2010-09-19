package org.jallinone.system.permissions.java;

import org.openswing.swing.message.receive.java.*;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store user info.</p>
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
public class UserVO extends ValueObjectImpl {

  private String usernameSYS03;
  private String passwdSYS03;
  private java.sql.Date passwdExpirationSYS03;
  private String languageCodeSys09SYS03;
  private String firstNameSYS03;
  private String lastNameSYS03;
  private String companyCodeSys01SYS03;
  private java.math.BigDecimal progressiveReg04SYS03;
  private java.sql.Date createDateSYS03;
  private String usernameCreateSYS03;
  private String oldUsernameSYS03;
  private String employeeCodeSCH01;
  private String defCompanyCodeSys01SYS03;


  public UserVO() {
  }


  public String getUsernameSYS03() {
    return usernameSYS03;
  }
  public void setUsernameSYS03(String usernameSYS03) {
    this.usernameSYS03 = usernameSYS03;
  }
  public String getPasswdSYS03() {
    return passwdSYS03;
  }
  public void setPasswdSYS03(String passwdSYS03) {
    this.passwdSYS03 = passwdSYS03;
  }
  public java.sql.Date getPasswdExpirationSYS03() {
    return passwdExpirationSYS03;
  }
  public void setPasswdExpirationSYS03(java.sql.Date passwdExpirationSYS03) {
    this.passwdExpirationSYS03 = passwdExpirationSYS03;
  }
  public String getLanguageCodeSys09SYS03() {
    return languageCodeSys09SYS03;
  }
  public void setLanguageCodeSys09SYS03(String languageCodeSys09SYS03) {
    this.languageCodeSys09SYS03 = languageCodeSys09SYS03;
  }
  public String getFirstNameSYS03() {
    return firstNameSYS03;
  }
  public void setFirstNameSYS03(String firstNameSYS03) {
    this.firstNameSYS03 = firstNameSYS03;
  }
  public String getLastNameSYS03() {
    return lastNameSYS03;
  }
  public void setLastNameSYS03(String lastNameSYS03) {
    this.lastNameSYS03 = lastNameSYS03;
  }
  public String getCompanyCodeSys01SYS03() {
    return companyCodeSys01SYS03;
  }
  public void setCompanyCodeSys01SYS03(String companyCodeSys01SYS03) {
    this.companyCodeSys01SYS03 = companyCodeSys01SYS03;
  }
  public java.math.BigDecimal getProgressiveReg04SYS03() {
    return progressiveReg04SYS03;
  }
  public void setProgressiveReg04SYS03(java.math.BigDecimal progressiveReg04SYS03) {
    this.progressiveReg04SYS03 = progressiveReg04SYS03;
  }
  public java.sql.Date getCreateDateSYS03() {
    return createDateSYS03;
  }
  public void setCreateDateSYS03(java.sql.Date createDateSYS03) {
    this.createDateSYS03 = createDateSYS03;
  }
  public String getUsernameCreateSYS03() {
    return usernameCreateSYS03;
  }
  public void setUsernameCreateSYS03(String usernameCreateSYS03) {
    this.usernameCreateSYS03 = usernameCreateSYS03;
  }
  public String getOldUsernameSYS03() {
    return oldUsernameSYS03;
  }
  public void setOldUsernameSYS03(String oldUsernameSYS03) {
    this.oldUsernameSYS03 = oldUsernameSYS03;
  }
  public String getEmployeeCodeSCH01() {
    return employeeCodeSCH01;
  }
  public void setEmployeeCodeSCH01(String employeeCodeSCH01) {
    this.employeeCodeSCH01 = employeeCodeSCH01;
  }
  public String getDefCompanyCodeSys01SYS03() {
    return defCompanyCodeSys01SYS03;
  }
  public void setDefCompanyCodeSys01SYS03(String defCompanyCodeSys01SYS03) {
    this.defCompanyCodeSys01SYS03 = defCompanyCodeSys01SYS03;
  }

}
