package org.jallinone.warehouse.java;

import org.jallinone.system.customizations.java.BaseValueObject;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Warehouse value object.</p>
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
public class WarehouseVO extends BaseValueObject {

  private String companyCodeSys01WAR01;
  private String warehouseCodeWAR01;
  private String descriptionWAR01;
  private String addressWAR01;
  private String cityWAR01;
  private String zipWAR01;
  private String provinceWAR01;
  private String countryWAR01;
  private String name_1REG04;
  private java.math.BigDecimal progressiveSys04WAR01;
  private java.math.BigDecimal progressiveHie02WAR01;
  private String enabledWAR01;
  private java.math.BigDecimal progressiveHie01HIE02;


  public WarehouseVO() {
  }


  public String getCompanyCodeSys01WAR01() {
    return companyCodeSys01WAR01;
  }
  public void setCompanyCodeSys01WAR01(String companyCodeSys01WAR01) {
    this.companyCodeSys01WAR01 = companyCodeSys01WAR01;
  }
  public String getWarehouseCodeWAR01() {
    return warehouseCodeWAR01;
  }
  public void setWarehouseCodeWAR01(String warehouseCodeWAR01) {
    this.warehouseCodeWAR01 = warehouseCodeWAR01;
  }
  public java.math.BigDecimal getProgressiveHie02WAR01() {
    return progressiveHie02WAR01;
  }
  public void setProgressiveHie02WAR01(java.math.BigDecimal progressiveHie02WAR01) {
    this.progressiveHie02WAR01 = progressiveHie02WAR01;
  }
  public String getDescriptionWAR01() {
    return descriptionWAR01;
  }
  public void setDescriptionWAR01(String descriptionWAR01) {
    this.descriptionWAR01 = descriptionWAR01;
  }
  public String getAddressWAR01() {
    return addressWAR01;
  }
  public void setAddressWAR01(String addressWAR01) {
    this.addressWAR01 = addressWAR01;
  }
  public String getCityWAR01() {
    return cityWAR01;
  }
  public void setCityWAR01(String cityWAR01) {
    this.cityWAR01 = cityWAR01;
  }
  public String getZipWAR01() {
    return zipWAR01;
  }
  public void setZipWAR01(String zipWAR01) {
    this.zipWAR01 = zipWAR01;
  }
  public String getProvinceWAR01() {
    return provinceWAR01;
  }
  public void setProvinceWAR01(String provinceWAR01) {
    this.provinceWAR01 = provinceWAR01;
  }
  public String getCountryWAR01() {
    return countryWAR01;
  }
  public void setCountryWAR01(String countryWAR01) {
    this.countryWAR01 = countryWAR01;
  }
  public String getName_1REG04() {
    return name_1REG04;
  }
  public void setName_1REG04(String name_1REG04) {
    this.name_1REG04 = name_1REG04;
  }
  public java.math.BigDecimal getProgressiveSys04WAR01() {
    return progressiveSys04WAR01;
  }
  public void setProgressiveSys04WAR01(java.math.BigDecimal progressiveSys04WAR01) {
    this.progressiveSys04WAR01 = progressiveSys04WAR01;
  }
  public String getEnabledWAR01() {
    return enabledWAR01;
  }
  public void setEnabledWAR01(String enabledWAR01) {
    this.enabledWAR01 = enabledWAR01;
  }
  public java.math.BigDecimal getProgressiveHie01HIE02() {
    return progressiveHie01HIE02;
  }
  public void setProgressiveHie01HIE02(java.math.BigDecimal progressiveHie01HIE02) {
    this.progressiveHie01HIE02 = progressiveHie01HIE02;
  }

}
