package org.jallinone.sales.documents.java;

import org.openswing.swing.message.receive.java.*;
import org.jallinone.sales.pricelist.java.PriceVO;
import org.jallinone.variants.java.VariantsItemDescriptor;
import java.math.BigDecimal;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store an item with price.</p>
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
public class PriceItemVO extends PriceVO implements VariantsItemDescriptor {


  private String vatCodeReg01ITM01;
  private String vatDescriptionSYS10;
  private java.math.BigDecimal deductibleREG01;
  private java.math.BigDecimal valueREG01;
  private java.math.BigDecimal decimalsREG02;
  private java.math.BigDecimal minSellingQtyITM01;
  private String minSellingQtyUmCodeReg02ITM01;
  private java.math.BigDecimal progressiveHie01ITM01;
  private Boolean serialNumberRequiredITM01;

  private Boolean useVariant1ITM01;
  private Boolean useVariant2ITM01;
  private Boolean useVariant3ITM01;
  private Boolean useVariant4ITM01;
  private Boolean useVariant5ITM01;

	private Boolean noWarehouseMovITM01;


  public PriceItemVO() {
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
  public java.math.BigDecimal getDecimalsREG02() {
    return decimalsREG02;
  }
  public java.math.BigDecimal getMinSellingQtyITM01() {
    return minSellingQtyITM01;
  }
  public String getMinSellingQtyUmCodeReg02ITM01() {
    return minSellingQtyUmCodeReg02ITM01;
  }
  public void setMinSellingQtyUmCodeReg02ITM01(String minSellingQtyUmCodeReg02ITM01) {
    this.minSellingQtyUmCodeReg02ITM01 = minSellingQtyUmCodeReg02ITM01;
  }
  public void setMinSellingQtyITM01(java.math.BigDecimal minSellingQtyITM01) {
    this.minSellingQtyITM01 = minSellingQtyITM01;
  }
  public void setDecimalsREG02(java.math.BigDecimal decimalsREG02) {
    this.decimalsREG02 = decimalsREG02;
  }
  public java.math.BigDecimal getProgressiveHie01ITM01() {
    return progressiveHie01ITM01;
  }
  public void setProgressiveHie01ITM01(java.math.BigDecimal progressiveHie01ITM01) {
    this.progressiveHie01ITM01 = progressiveHie01ITM01;
  }
  public Boolean getSerialNumberRequiredITM01() {
    return serialNumberRequiredITM01;
  }
  public void setSerialNumberRequiredITM01(Boolean serialNumberRequiredITM01) {
    this.serialNumberRequiredITM01 = serialNumberRequiredITM01;
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
    return getCompanyCodeSys01SAL02();
  }
  public String getItemCodeItm01() {
    return getItemCodeItm01SAL02();
  }
  public Boolean getNoWarehouseMovITM01() {
    return noWarehouseMovITM01;
  }
  public void setNoWarehouseMovITM01(Boolean noWarehouseMovITM01) {
    this.noWarehouseMovITM01 = noWarehouseMovITM01;
  }



}
