package org.jallinone.purchases.pricelist.java;

import org.jallinone.system.customizations.java.BaseValueObject;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a supplier price list.</p>
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
public class SupplierPricelistVO extends BaseValueObject {

  private String pricelistCodePUR03;
  private String companyCodeSys01PUR03;
  private String descriptionSYS10;
  private java.math.BigDecimal progressiveSys10PUR03;
  private String currencyCodeReg03PUR03;
  private String oldPricelistCodePur03PUR04;
  private java.math.BigDecimal progressiveReg04PUR03;
  private java.math.BigDecimal oldProgressiveReg04PUR03;
  private String name_1REG04;


  public SupplierPricelistVO() {
  }
  public String getPricelistCodePUR03() {
    return pricelistCodePUR03;
  }
  public void setPricelistCodePUR03(String pricelistCodePUR03) {
    this.pricelistCodePUR03 = pricelistCodePUR03;
  }
  public String getCompanyCodeSys01PUR03() {
    return companyCodeSys01PUR03;
  }
  public void setCompanyCodeSys01PUR03(String companyCodeSys01PUR03) {
    this.companyCodeSys01PUR03 = companyCodeSys01PUR03;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public java.math.BigDecimal getProgressiveSys10PUR03() {
    return progressiveSys10PUR03;
  }
  public void setProgressiveSys10PUR03(java.math.BigDecimal progressiveSys10PUR03) {
    this.progressiveSys10PUR03 = progressiveSys10PUR03;
  }
  public String getCurrencyCodeReg03PUR03() {
    return currencyCodeReg03PUR03;
  }
  public void setCurrencyCodeReg03PUR03(String currencyCodeReg03PUR03) {
    this.currencyCodeReg03PUR03 = currencyCodeReg03PUR03;
  }
  public String getOldPricelistCodePur03PUR04() {
    return oldPricelistCodePur03PUR04;
  }
  public void setOldPricelistCodePur03PUR04(String oldPricelistCodePur03PUR04) {
    this.oldPricelistCodePur03PUR04 = oldPricelistCodePur03PUR04;
  }
  public java.math.BigDecimal getProgressiveReg04PUR03() {
    return progressiveReg04PUR03;
  }
  public void setProgressiveReg04PUR03(java.math.BigDecimal progressiveReg04PUR03) {
    this.progressiveReg04PUR03 = progressiveReg04PUR03;
  }
  public java.math.BigDecimal getOldProgressiveReg04PUR03() {
    return oldProgressiveReg04PUR03;
  }
  public void setOldProgressiveReg04PUR03(java.math.BigDecimal oldProgressiveReg04PUR03) {
    this.oldProgressiveReg04PUR03 = oldProgressiveReg04PUR03;
  }
  public String getName_1REG04() {
    return name_1REG04;
  }
  public void setName_1REG04(String name_1REG04) {
    this.name_1REG04 = name_1REG04;
  }

}
