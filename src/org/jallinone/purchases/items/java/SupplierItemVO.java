package org.jallinone.purchases.items.java;

import org.openswing.swing.message.receive.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a supplier item.</p>
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
public class SupplierItemVO extends ValueObjectImpl {

  private String companyCodeSys01PUR02;
  private String itemCodeItm01PUR02;
  private String supplierItemCodePUR02;
  private String enabledPUR02;
  private java.math.BigDecimal minPurchaseQtyPUR02;
  private java.math.BigDecimal multipleQtyPUR02;
  private java.math.BigDecimal progressiveReg04PUR02;
  private String descriptionSYS10;
  private java.math.BigDecimal decimalsREG02;
  private String umCodeReg02PUR02;
  private java.math.BigDecimal progressiveHie01PUR02;
  private java.math.BigDecimal progressiveHie02PUR02;
  private java.math.BigDecimal minSellingQtyDecimalsReg02ITM01;
  private java.math.BigDecimal valueREG05;
  private String minSellingQtyUmCodeReg02ITM01;
  private Boolean serialNumberRequiredITM01;


  public SupplierItemVO() {
  }


  public String getCompanyCodeSys01PUR02() {
    return companyCodeSys01PUR02;
  }
  public void setCompanyCodeSys01PUR02(String companyCodeSys01PUR02) {
    this.companyCodeSys01PUR02 = companyCodeSys01PUR02;
  }
  public String getItemCodeItm01PUR02() {
    return itemCodeItm01PUR02;
  }
  public void setItemCodeItm01PUR02(String itemCodeItm01PUR02) {
    this.itemCodeItm01PUR02 = itemCodeItm01PUR02;
  }
  public String getSupplierItemCodePUR02() {
    return supplierItemCodePUR02;
  }
  public void setSupplierItemCodePUR02(String supplierItemCodePUR02) {
    this.supplierItemCodePUR02 = supplierItemCodePUR02;
  }
  public String getEnabledPUR02() {
    return enabledPUR02;
  }
  public void setEnabledPUR02(String enabledPUR02) {
    this.enabledPUR02 = enabledPUR02;
  }
  public java.math.BigDecimal getMinPurchaseQtyPUR02() {
    return minPurchaseQtyPUR02;
  }
  public void setMinPurchaseQtyPUR02(java.math.BigDecimal minPurchaseQtyPUR02) {
    this.minPurchaseQtyPUR02 = minPurchaseQtyPUR02;
  }
  public java.math.BigDecimal getMultipleQtyPUR02() {
    return multipleQtyPUR02;
  }
  public void setMultipleQtyPUR02(java.math.BigDecimal multipleQtyPUR02) {
    this.multipleQtyPUR02 = multipleQtyPUR02;
  }
  public java.math.BigDecimal getProgressiveReg04PUR02() {
    return progressiveReg04PUR02;
  }
  public void setProgressiveReg04PUR02(java.math.BigDecimal progressiveReg04PUR02) {
    this.progressiveReg04PUR02 = progressiveReg04PUR02;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public java.math.BigDecimal getDecimalsREG02() {
    return decimalsREG02;
  }
  public void setDecimalsREG02(java.math.BigDecimal decimalsREG02) {
    this.decimalsREG02 = decimalsREG02;
  }
  public String getUmCodeReg02PUR02() {
    return umCodeReg02PUR02;
  }
  public void setUmCodeReg02PUR02(String umCodeReg02PUR02) {
    this.umCodeReg02PUR02 = umCodeReg02PUR02;
  }
  public java.math.BigDecimal getProgressiveHie01PUR02() {
    return progressiveHie01PUR02;
  }
  public void setProgressiveHie01PUR02(java.math.BigDecimal progressiveHie01PUR02) {
    this.progressiveHie01PUR02 = progressiveHie01PUR02;
  }
  public java.math.BigDecimal getProgressiveHie02PUR02() {
    return progressiveHie02PUR02;
  }
  public void setProgressiveHie02PUR02(java.math.BigDecimal progressiveHie02PUR02) {
    this.progressiveHie02PUR02 = progressiveHie02PUR02;
  }
  public java.math.BigDecimal getMinSellingQtyDecimalsReg02ITM01() {
    return minSellingQtyDecimalsReg02ITM01;
  }
  public String getMinSellingQtyUmCodeReg02ITM01() {
    return minSellingQtyUmCodeReg02ITM01;
  }
  public java.math.BigDecimal getValueREG05() {
    return valueREG05;
  }
  public void setValueREG05(java.math.BigDecimal valueREG05) {
    this.valueREG05 = valueREG05;
  }
  public void setMinSellingQtyUmCodeReg02ITM01(String minSellingQtyUmCodeReg02ITM01) {
    this.minSellingQtyUmCodeReg02ITM01 = minSellingQtyUmCodeReg02ITM01;
  }
  public void setMinSellingQtyDecimalsReg02ITM01(java.math.BigDecimal minSellingQtyDecimalsReg02ITM01) {
    this.minSellingQtyDecimalsReg02ITM01 = minSellingQtyDecimalsReg02ITM01;
  }
  public Boolean getSerialNumberRequiredITM01() {
    return serialNumberRequiredITM01;
  }
  public void setSerialNumberRequiredITM01(Boolean serialNumberRequiredITM01) {
    this.serialNumberRequiredITM01 = serialNumberRequiredITM01;
  }


}
