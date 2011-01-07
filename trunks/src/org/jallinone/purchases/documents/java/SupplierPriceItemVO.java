package org.jallinone.purchases.documents.java;

import org.openswing.swing.message.receive.java.*;
import org.jallinone.purchases.items.java.SupplierItemVO;
import org.jallinone.variants.java.VariantsItemDescriptor;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a supplier item with price.</p>
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
public class SupplierPriceItemVO extends SupplierItemVO implements VariantsItemDescriptor {


  private String vatCodeReg01ITM01;
  private String vatDescriptionSYS10;
  private java.math.BigDecimal deductibleREG01;
  private java.math.BigDecimal valueREG01;
  private java.math.BigDecimal valuePUR04;
  private java.sql.Date startDatePUR04;
  private java.sql.Date endDatePUR04;

  private Boolean useVariant1ITM01;
  private Boolean useVariant2ITM01;
  private Boolean useVariant3ITM01;
  private Boolean useVariant4ITM01;
  private Boolean useVariant5ITM01;

	private Boolean noWarehouseMovITM01;


  public SupplierPriceItemVO() {
  }


  public String getVatCodeReg01ITM01() {
    return vatCodeReg01ITM01;
  }
  public void setVatCodeReg01ITM01(String vatCodeReg01ITM01) {
    this.vatCodeReg01ITM01 = vatCodeReg01ITM01;
  }
  public String getVatDescriptionSYS10() {
    return vatDescriptionSYS10;
  }
  public void setVatDescriptionSYS10(String vatDescriptionSYS10) {
    this.vatDescriptionSYS10 = vatDescriptionSYS10;
  }
  public java.math.BigDecimal getValueREG01() {
    return valueREG01;
  }
  public void setValueREG01(java.math.BigDecimal valueREG01) {
    this.valueREG01 = valueREG01;
  }
  public java.math.BigDecimal getDeductibleREG01() {
    return deductibleREG01;
  }
  public void setDeductibleREG01(java.math.BigDecimal deductibleREG01) {
    this.deductibleREG01 = deductibleREG01;
  }
  public java.sql.Date getEndDatePUR04() {
    return endDatePUR04;
  }
  public java.math.BigDecimal getValuePUR04() {
    return valuePUR04;
  }
  public void setValuePUR04(java.math.BigDecimal valuePUR04) {
    this.valuePUR04 = valuePUR04;
  }
  public void setStartDatePUR04(java.sql.Date startDatePUR04) {
    this.startDatePUR04 = startDatePUR04;
  }
  public void setEndDatePUR04(java.sql.Date endDatePUR04) {
    this.endDatePUR04 = endDatePUR04;
  }
  public java.sql.Date getStartDatePUR04() {
    return startDatePUR04;
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
    return getCompanyCodeSys01PUR02();
  }
  public String getItemCodeItm01() {
    return getItemCodeItm01PUR02();
  }
  public Boolean getNoWarehouseMovITM01() {
    return noWarehouseMovITM01;
  }
  public void setNoWarehouseMovITM01(Boolean noWarehouseMovITM01) {
    this.noWarehouseMovITM01 = noWarehouseMovITM01;
  }

}
