package org.jallinone.startup.java;

import org.openswing.swing.message.receive.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value Object used to pass to the server side the database connection settings and
 * initialization data, used in CreateConfigFileAction class.</p>
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
public class DbConnVO extends ValueObjectImpl {

  private String driverName;
  private String url;
  private String username;
  private String password;
  private String companyCode;
  private String companyDescription;
  private String languageCode;
  private String languageDescription;
  private String clientLanguageCode;
  private String adminPassword;

  private String currencyCodeREG03;
  private String currencySymbolREG03;
  private String decimalSymbolREG03;
  private String thousandSymbolREG03;
  private java.math.BigDecimal decimalsREG03;



  public DbConnVO() {
  }


  public String getDriverName() {
    return driverName;
  }


  public void setDriverName(String driverName) {
    this.driverName = driverName;
  }


  public String getUrl() {
    return url;
  }


  public void setUrl(String url) {
    this.url = url;
  }


  public String getUsername() {
    return username;
  }


  public void setUsername(String username) {
    this.username = username;
  }


  public String getPassword() {
    return password;
  }


  public void setPassword(String password) {
    this.password = password;
  }
  public String getCompanyCode() {
    return companyCode;
  }
  public void setCompanyCode(String companyCode) {
    this.companyCode = companyCode;
  }
  public String getCompanyDescription() {
    return companyDescription;
  }
  public void setCompanyDescription(String companyDescription) {
    this.companyDescription = companyDescription;
  }
  public String getLanguageCode() {
    return languageCode;
  }
  public void setLanguageCode(String languageCode) {
    this.languageCode = languageCode;
  }
  public String getLanguageDescription() {
    return languageDescription;
  }
  public void setLanguageDescription(String languageDescription) {
    this.languageDescription = languageDescription;
  }
  public String getClientLanguageCode() {
    return clientLanguageCode;
  }
  public void setClientLanguageCode(String clientLanguageCode) {
    this.clientLanguageCode = clientLanguageCode;
  }
  public String getAdminPassword() {
    return adminPassword;
  }
  public void setAdminPassword(String adminPassword) {
    this.adminPassword = adminPassword;
  }
  public String getCurrencyCodeREG03() {
    return currencyCodeREG03;
  }
  public String getCurrencySymbolREG03() {
    return currencySymbolREG03;
  }
  public java.math.BigDecimal getDecimalsREG03() {
    return decimalsREG03;
  }
  public String getDecimalSymbolREG03() {
    return decimalSymbolREG03;
  }
  public String getThousandSymbolREG03() {
    return thousandSymbolREG03;
  }
  public void setThousandSymbolREG03(String thousandSymbolREG03) {
    this.thousandSymbolREG03 = thousandSymbolREG03;
  }
  public void setDecimalSymbolREG03(String decimalSymbolREG03) {
    this.decimalSymbolREG03 = decimalSymbolREG03;
  }
  public void setDecimalsREG03(java.math.BigDecimal decimalsREG03) {
    this.decimalsREG03 = decimalsREG03;
  }
  public void setCurrencySymbolREG03(String currencySymbolREG03) {
    this.currencySymbolREG03 = currencySymbolREG03;
  }
  public void setCurrencyCodeREG03(String currencyCodeREG03) {
    this.currencyCodeREG03 = currencyCodeREG03;
  }

}
