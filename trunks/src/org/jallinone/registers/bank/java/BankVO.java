package org.jallinone.registers.bank.java;

import org.jallinone.system.customizations.java.BaseValueObject;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store bank info.</p>
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
public class BankVO extends BaseValueObject {

  private String bankCodeREG12;
  private String enabledREG12;
  private String descriptionREG12;
  private String abiREG12;
  private String bbanREG12;
  private String addressREG12;
  private String ibanREG12;
  private String zipREG12;
  private String provinceREG12;
  private String countryREG12;
  private String cityREG12;


  public BankVO() {
  }


  public String getBankCodeREG12() {
    return bankCodeREG12;
  }
  public void setBankCodeREG12(String bankCodeREG12) {
    this.bankCodeREG12 = bankCodeREG12;
  }
  public String getDescriptionREG12() {
    return descriptionREG12;
  }
  public void setDescriptionREG12(String descriptionREG12) {
    this.descriptionREG12 = descriptionREG12;
  }
  public String getEnabledREG12() {
    return enabledREG12;
  }
  public void setEnabledREG12(String enabledREG12) {
    this.enabledREG12 = enabledREG12;
  }
  public String getAbiREG12() {
    return abiREG12;
  }
  public void setAbiREG12(String abiREG12) {
    this.abiREG12 = abiREG12;
  }
  public String getBbanREG12() {
    return bbanREG12;
  }
  public void setBbanREG12(String bbanREG12) {
    this.bbanREG12 = bbanREG12;
  }
  public String getAddressREG12() {
    return addressREG12;
  }
  public void setAddressREG12(String addressREG12) {
    this.addressREG12 = addressREG12;
  }
  public String getIbanREG12() {
    return ibanREG12;
  }
  public void setIbanREG12(String ibanREG12) {
    this.ibanREG12 = ibanREG12;
  }
  public String getZipREG12() {
    return zipREG12;
  }
  public void setZipREG12(String zipREG12) {
    this.zipREG12 = zipREG12;
  }
  public String getProvinceREG12() {
    return provinceREG12;
  }
  public void setProvinceREG12(String provinceREG12) {
    this.provinceREG12 = provinceREG12;
  }
  public String getCountryREG12() {
    return countryREG12;
  }
  public void setCountryREG12(String countryREG12) {
    this.countryREG12 = countryREG12;
  }
  public String getCityREG12() {
    return cityREG12;
  }
  public void setCityREG12(String cityREG12) {
    this.cityREG12 = cityREG12;
  }

}
