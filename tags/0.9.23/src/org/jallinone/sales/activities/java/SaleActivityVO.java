package org.jallinone.sales.activities.java;

import org.jallinone.system.customizations.java.BaseValueObject;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store sale activities info.</p>
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
public class SaleActivityVO extends BaseValueObject {


  private String activityCodeSAL09;
  private java.math.BigDecimal progressiveSys10SAL09;
  private String descriptionSYS10;
  private String companyCodeSys01SAL09;
  private java.math.BigDecimal valueSAL09;
  private String currencyCodeReg03SAL09;
  private String vatCodeReg01SAL09;
  private String vatDescriptionSYS10;
  private java.math.BigDecimal vatValueREG01;
  private java.math.BigDecimal vatDeductibleREG01;
  private java.math.BigDecimal decimalsREG02;
  private String currencySymbolREG03;
  private java.math.BigDecimal decimalsREG03;
  private String umCodeReg02SAL09;


  public SaleActivityVO() {
  }


  public String getSaleActivityCodeSAL09() {
    return activityCodeSAL09;
  }
  public void setSaleActivityCodeSAL09(String activityCodeSAL09) {
    this.activityCodeSAL09 = activityCodeSAL09;
  }
  public java.math.BigDecimal getProgressiveSys10SAL09() {
    return progressiveSys10SAL09;
  }
  public void setProgressiveSys10SAL09(java.math.BigDecimal progressiveSys10SAL09) {
    this.progressiveSys10SAL09 = progressiveSys10SAL09;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public String getCompanyCodeSys01SAL09() {
    return companyCodeSys01SAL09;
  }
  public void setCompanyCodeSys01SAL09(String companyCodeSys01SAL09) {
    this.companyCodeSys01SAL09 = companyCodeSys01SAL09;
  }
  public java.math.BigDecimal getValueSAL09() {
    return valueSAL09;
  }
  public void setValueSAL09(java.math.BigDecimal valueSAL09) {
    this.valueSAL09 = valueSAL09;
  }
  public String getCurrencyCodeReg03SAL09() {
    return currencyCodeReg03SAL09;
  }
  public void setCurrencyCodeReg03SAL09(String currencyCodeReg03SAL09) {
    this.currencyCodeReg03SAL09 = currencyCodeReg03SAL09;
  }
  public String getVatCodeReg01SAL09() {
    return vatCodeReg01SAL09;
  }
  public void setVatCodeReg01SAL09(String vatCodeReg01SAL09) {
    this.vatCodeReg01SAL09 = vatCodeReg01SAL09;
  }
  public String getVatDescriptionSYS10() {
    return vatDescriptionSYS10;
  }
  public void setVatDescriptionSYS10(String vatDescriptionSYS10) {
    this.vatDescriptionSYS10 = vatDescriptionSYS10;
  }
  public java.math.BigDecimal getVatValueREG01() {
    return vatValueREG01;
  }
  public void setVatValueREG01(java.math.BigDecimal vatValueREG01) {
    this.vatValueREG01 = vatValueREG01;
  }
  public java.math.BigDecimal getVatDeductibleREG01() {
    return vatDeductibleREG01;
  }
  public void setVatDeductibleREG01(java.math.BigDecimal vatDeductibleREG01) {
    this.vatDeductibleREG01 = vatDeductibleREG01;
  }
  public java.math.BigDecimal getDecimalsREG02() {
    return decimalsREG02;
  }
  public void setDecimalsREG02(java.math.BigDecimal decimalsREG02) {
    this.decimalsREG02 = decimalsREG02;
  }
  public String getCurrencySymbolREG03() {
    return currencySymbolREG03;
  }
  public void setCurrencySymbolREG03(String currencySymbolREG03) {
    this.currencySymbolREG03 = currencySymbolREG03;
  }
  public String getActivityCodeSAL09() {
    return activityCodeSAL09;
  }
  public void setActivityCodeSAL09(String activityCodeSAL09) {
    this.activityCodeSAL09 = activityCodeSAL09;
  }
  public void setDecimalsREG03(java.math.BigDecimal decimalsREG03) {
    this.decimalsREG03 = decimalsREG03;
  }
  public java.math.BigDecimal getDecimalsREG03() {
    return decimalsREG03;
  }
  public String getUmCodeReg02SAL09() {
    return umCodeReg02SAL09;
  }
  public void setUmCodeReg02SAL09(String umCodeReg02SAL09) {
    this.umCodeReg02SAL09 = umCodeReg02SAL09;
  }


}