package org.jallinone.purchases.pricelist.java;

import org.jallinone.system.customizations.java.BaseValueObject;
import org.jallinone.variants.java.VariantsItemDescriptor;
import java.math.BigDecimal;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a supplier item price.</p>
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
public class SupplierPriceVO extends BaseValueObject implements VariantsItemDescriptor {

  private String pricelistCodePur03PUR04;
  private String companyCodeSys01PUR04;
  private String itemCodeItm01PUR04;
  private java.math.BigDecimal valuePUR04;
  private java.sql.Date startDatePUR04;
  private java.sql.Date endDatePUR04;
  private String itemDescriptionSYS10;
  private String pricelistDescriptionSYS10;
  private java.math.BigDecimal progressiveHie02ITM01;
  private java.math.BigDecimal progressiveReg04PUR04;
  private String name_1REG04;
  private String name_2REG04;
  private String supplierCodePUR01;

  private Boolean useVariant1ITM01;
  private Boolean useVariant2ITM01;
  private Boolean useVariant3ITM01;
  private Boolean useVariant4ITM01;
  private Boolean useVariant5ITM01;


  public SupplierPriceVO() {
  }


  public String getSupplierDescription() {
    return
        (name_1REG04==null?"":name_1REG04+" ")+
        (name_2REG04==null?"":name_2REG04);
  }


  public void setSupplierDescription(String description) {
    name_1REG04 = description;
  }


  public String getPricelistCodePur03PUR04() {
    return pricelistCodePur03PUR04;
  }
  public void setPricelistCodePur03PUR04(String pricelistCodePur03PUR04) {
    this.pricelistCodePur03PUR04 = pricelistCodePur03PUR04;
  }
  public String getCompanyCodeSys01PUR04() {
    return companyCodeSys01PUR04;
  }
  public void setCompanyCodeSys01PUR04(String companyCodeSys01PUR04) {
    this.companyCodeSys01PUR04 = companyCodeSys01PUR04;
  }
  public java.math.BigDecimal getValuePUR04() {
    return valuePUR04;
  }
  public void setValuePUR04(java.math.BigDecimal valuePUR04) {
    this.valuePUR04 = valuePUR04;
  }
  public java.sql.Date getStartDatePUR04() {
    return startDatePUR04;
  }
  public void setStartDatePUR04(java.sql.Date startDatePUR04) {
    this.startDatePUR04 = startDatePUR04;
  }
  public String getItemCodeItm01PUR04() {
    return itemCodeItm01PUR04;
  }
  public void setItemCodeItm01PUR04(String itemCodeItm01PUR04) {
    this.itemCodeItm01PUR04 = itemCodeItm01PUR04;
  }
  public java.sql.Date getEndDatePUR04() {
    return endDatePUR04;
  }
  public void setEndDatePUR04(java.sql.Date endDatePUR04) {
    this.endDatePUR04 = endDatePUR04;
  }
  public String getItemDescriptionSYS10() {
    return itemDescriptionSYS10;
  }
  public void setItemDescriptionSYS10(String itemDescriptionSYS10) {
    this.itemDescriptionSYS10 = itemDescriptionSYS10;
  }
  public String getPricelistDescriptionSYS10() {
    return pricelistDescriptionSYS10;
  }
  public void setPricelistDescriptionSYS10(String pricelistDescriptionSYS10) {
    this.pricelistDescriptionSYS10 = pricelistDescriptionSYS10;
  }
  public java.math.BigDecimal getProgressiveHie02ITM01() {
    return progressiveHie02ITM01;
  }
  public void setProgressiveHie02ITM01(java.math.BigDecimal progressiveHie02ITM01) {
    this.progressiveHie02ITM01 = progressiveHie02ITM01;
  }
  public java.math.BigDecimal getProgressiveReg04PUR04() {
    return progressiveReg04PUR04;
  }
  public void setProgressiveReg04PUR04(java.math.BigDecimal progressiveReg04PUR04) {
    this.progressiveReg04PUR04 = progressiveReg04PUR04;
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
  public String getSupplierCodePUR01() {
    return supplierCodePUR01;
  }
  public void setSupplierCodePUR01(String supplierCodePUR01) {
    this.supplierCodePUR01 = supplierCodePUR01;
  }
  public Boolean getUseVariant1ITM01() {
    return useVariant1ITM01;
  }
  public Boolean getUseVariant2ITM01() {
    return useVariant2ITM01;
  }
  public Boolean getUseVariant3ITM01() {
    return useVariant3ITM01;
  }
  public Boolean getUseVariant4ITM01() {
    return useVariant4ITM01;
  }
  public Boolean getUseVariant5ITM01() {
    return useVariant5ITM01;
  }
  public void setUseVariant5ITM01(Boolean useVariant5ITM01) {
    this.useVariant5ITM01 = useVariant5ITM01;
  }
  public void setUseVariant4ITM01(Boolean useVariant4ITM01) {
    this.useVariant4ITM01 = useVariant4ITM01;
  }
  public void setUseVariant3ITM01(Boolean useVariant3ITM01) {
    this.useVariant3ITM01 = useVariant3ITM01;
  }
  public void setUseVariant2ITM01(Boolean useVariant2ITM01) {
    this.useVariant2ITM01 = useVariant2ITM01;
  }
  public void setUseVariant1ITM01(Boolean useVariant1ITM01) {
    this.useVariant1ITM01 = useVariant1ITM01;
  }



  public String getCompanyCodeSys01() {
    return companyCodeSys01PUR04;
  }

  public String getItemCodeItm01() {
    return itemCodeItm01PUR04;
  }
  public BigDecimal getDecimalsREG02() {
    return new BigDecimal(0); // not used;
  }


}
