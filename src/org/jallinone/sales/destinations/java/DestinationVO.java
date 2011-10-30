package org.jallinone.sales.destinations.java;

import org.openswing.swing.message.receive.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store destination info.</p>
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
public class DestinationVO extends ValueObjectImpl {
  private String destinationCodeREG18;
  private String descriptionREG18;
  private String companyCodeSys01REG18;
  private String addressREG18;
  private String cityREG18;
  private String zipREG18;
  private String provinceREG18;
  private String countryREG18;
  private java.math.BigDecimal progressiveReg04REG18;


  public DestinationVO() {
  }


  public String getDestinationCodeREG18() {
    return destinationCodeREG18;
  }
  public void setDestinationCodeREG18(String destinationCodeREG18) {
    this.destinationCodeREG18 = destinationCodeREG18;
  }
  public String getDescriptionREG18() {
    return descriptionREG18;
  }
  public void setDescriptionREG18(String descriptionREG18) {
    this.descriptionREG18 = descriptionREG18;
  }
  public String getCompanyCodeSys01REG18() {
    return companyCodeSys01REG18;
  }
  public void setCompanyCodeSys01REG18(String companyCodeSys01REG18) {
    this.companyCodeSys01REG18 = companyCodeSys01REG18;
  }
  public String getAddressREG18() {
    return addressREG18;
  }
  public void setAddressREG18(String addressREG18) {
    this.addressREG18 = addressREG18;
  }
  public String getCityREG18() {
    return cityREG18;
  }
  public void setCityREG18(String cityREG18) {
    this.cityREG18 = cityREG18;
  }
  public String getZipREG18() {
    return zipREG18;
  }
  public void setZipREG18(String zipREG18) {
    this.zipREG18 = zipREG18;
  }
  public String getProvinceREG18() {
    return provinceREG18;
  }
  public void setProvinceREG18(String provinceREG18) {
    this.provinceREG18 = provinceREG18;
  }
  public String getCountryREG18() {
    return countryREG18;
  }
  public void setCountryREG18(String countryREG18) {
    this.countryREG18 = countryREG18;
  }
  public java.math.BigDecimal getProgressiveReg04REG18() {
    return progressiveReg04REG18;
  }
  public void setProgressiveReg04REG18(java.math.BigDecimal progressiveReg04REG18) {
    this.progressiveReg04REG18 = progressiveReg04REG18;
  }

}
