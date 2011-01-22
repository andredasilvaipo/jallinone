package org.jallinone.purchases.documents.java;

import org.openswing.swing.message.receive.java.*;
import org.jallinone.system.customizations.java.BaseValueObject;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store purchase orders info, for a detail form.</p>
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
public class DetailPurchaseDocVO extends BaseValueObject {

  private String companyCodeSys01DOC06;
  private String docTypeDOC06;
  private String docStateDOC06;
  private String pricelistCodePur03DOC06;
  private String pricelistDescriptionDOC06;
  private String currencyCodeReg03DOC06;
  private String name_1REG04;
  private String name_2REG04;
  private java.math.BigDecimal docYearDOC06;
  private java.math.BigDecimal docNumberDOC06;
  private java.math.BigDecimal taxableIncomeDOC06;
  private java.math.BigDecimal totalVatDOC06;
  private java.math.BigDecimal totalDOC06;
  private java.sql.Date docDateDOC06;
  private java.math.BigDecimal progressiveReg04DOC06;
  private java.math.BigDecimal discountValueDOC06;
  private java.math.BigDecimal discountPercDOC06;
  private java.math.BigDecimal chargeValueDOC06;
  private java.math.BigDecimal chargePercDOC06;
  private java.math.BigDecimal instalmentNumberDOC06;
  private java.math.BigDecimal stepDOC06;
  private java.math.BigDecimal firstInstalmentDaysDOC06;
  private java.math.BigDecimal progressiveWkf01DOC06;
  private java.math.BigDecimal progressiveWkf08DOC06;
  private String companyCodeSys01Doc06DOC06;
  private String docTypeDoc06DOC06;
  private java.math.BigDecimal docYearDoc06DOC06;
  private java.math.BigDecimal docNumberDoc06DOC06;
  private String paymentCodeReg10DOC06;
  private String paymentDescriptionDOC06;
  private String startDayDOC06;
  private String paymentTypeDescriptionDOC06;
  private String descriptionWkf01DOC06;
  private String noteDOC06;
  private String enabledDOC06;
  private String supplierCodePUR01;
  private String currencySymbolREG03;
  private String decimalSymbolREG03;
  private String thousandSymbolREG03;
  private java.math.BigDecimal decimalsREG03;
  private String warehouseCodeWar01DOC06;
  private String descriptionWar01DOC06;
  private String addressWar01DOC06;
  private String cityWar01DOC06;
  private String zipWar01DOC06;
  private String provinceWar01DOC06;
  private String countryWar01DOC06;
  private java.math.BigDecimal docSequenceDOC06;
  private java.math.BigDecimal docSequenceDoc06DOC06;


  public DetailPurchaseDocVO() {
  }


  public String getCompanyCodeSys01DOC06() {
    return companyCodeSys01DOC06;
  }
  public void setCompanyCodeSys01DOC06(String companyCodeSys01DOC06) {
    this.companyCodeSys01DOC06 = companyCodeSys01DOC06;
  }
  public String getDocTypeDOC06() {
    return docTypeDOC06;
  }
  public void setDocTypeDOC06(String docTypeDOC06) {
    this.docTypeDOC06 = docTypeDOC06;
  }
  public String getDocStateDOC06() {
    return docStateDOC06;
  }
  public void setDocStateDOC06(String docStateDOC06) {
    this.docStateDOC06 = docStateDOC06;
  }
  public String getPricelistCodePur03DOC06() {
    return pricelistCodePur03DOC06;
  }
  public void setPricelistCodePur03DOC06(String pricelistCodePur03DOC06) {
    this.pricelistCodePur03DOC06 = pricelistCodePur03DOC06;
  }
  public String getPricelistDescriptionDOC06() {
    return pricelistDescriptionDOC06;
  }
  public void setPricelistDescriptionDOC06(String pricelistDescriptionDOC06) {
    this.pricelistDescriptionDOC06 = pricelistDescriptionDOC06;
  }
  public String getCurrencyCodeReg03DOC06() {
    return currencyCodeReg03DOC06;
  }
  public void setCurrencyCodeReg03DOC06(String currencyCodeReg03DOC06) {
    this.currencyCodeReg03DOC06 = currencyCodeReg03DOC06;
  }
  public String getName_1REG04() {
    return name_1REG04;
  }
  public void setName_1REG04(String name_1REG04) {
    this.name_1REG04 = name_1REG04;
  }
  public java.math.BigDecimal getDocYearDOC06() {
    return docYearDOC06;
  }
  public void setDocYearDOC06(java.math.BigDecimal docYearDOC06) {
    this.docYearDOC06 = docYearDOC06;
  }
  public java.math.BigDecimal getDocNumberDOC06() {
    return docNumberDOC06;
  }
  public void setDocNumberDOC06(java.math.BigDecimal docNumberDOC06) {
    this.docNumberDOC06 = docNumberDOC06;
  }
  public java.math.BigDecimal getTaxableIncomeDOC06() {
    return taxableIncomeDOC06;
  }
  public void setTaxableIncomeDOC06(java.math.BigDecimal taxableIncomeDOC06) {
    this.taxableIncomeDOC06 = taxableIncomeDOC06;
  }
  public java.math.BigDecimal getTotalVatDOC06() {
    return totalVatDOC06;
  }
  public void setTotalVatDOC06(java.math.BigDecimal totalVatDOC06) {
    this.totalVatDOC06 = totalVatDOC06;
  }
  public java.math.BigDecimal getTotalDOC06() {
    return totalDOC06;
  }
  public void setTotalDOC06(java.math.BigDecimal totalDOC06) {
    this.totalDOC06 = totalDOC06;
  }
  public java.sql.Date getDocDateDOC06() {
    return docDateDOC06;
  }
  public void setDocDateDOC06(java.sql.Date docDateDOC06) {
    this.docDateDOC06 = docDateDOC06;
  }
  public java.math.BigDecimal getProgressiveReg04DOC06() {
    return progressiveReg04DOC06;
  }
  public void setProgressiveReg04DOC06(java.math.BigDecimal progressiveReg04DOC06) {
    this.progressiveReg04DOC06 = progressiveReg04DOC06;
  }
  public java.math.BigDecimal getDiscountValueDOC06() {
    return discountValueDOC06;
  }
  public void setDiscountValueDOC06(java.math.BigDecimal discountValueDOC06) {
    this.discountValueDOC06 = discountValueDOC06;
  }
  public java.math.BigDecimal getDiscountPercDOC06() {
    return discountPercDOC06;
  }
  public void setDiscountPercDOC06(java.math.BigDecimal discountPercDOC06) {
    this.discountPercDOC06 = discountPercDOC06;
  }
  public java.math.BigDecimal getChargeValueDOC06() {
    return chargeValueDOC06;
  }
  public void setChargeValueDOC06(java.math.BigDecimal chargeValueDOC06) {
    this.chargeValueDOC06 = chargeValueDOC06;
  }
  public java.math.BigDecimal getChargePercDOC06() {
    return chargePercDOC06;
  }
  public void setChargePercDOC06(java.math.BigDecimal chargePercDOC06) {
    this.chargePercDOC06 = chargePercDOC06;
  }
  public java.math.BigDecimal getInstalmentNumberDOC06() {
    return instalmentNumberDOC06;
  }
  public void setInstalmentNumberDOC06(java.math.BigDecimal instalmentNumberDOC06) {
    this.instalmentNumberDOC06 = instalmentNumberDOC06;
  }
  public java.math.BigDecimal getStepDOC06() {
    return stepDOC06;
  }
  public void setStepDOC06(java.math.BigDecimal stepDOC06) {
    this.stepDOC06 = stepDOC06;
  }
  public java.math.BigDecimal getFirstInstalmentDaysDOC06() {
    return firstInstalmentDaysDOC06;
  }
  public void setFirstInstalmentDaysDOC06(java.math.BigDecimal firstInstalmentDaysDOC06) {
    this.firstInstalmentDaysDOC06 = firstInstalmentDaysDOC06;
  }
  public java.math.BigDecimal getProgressiveWkf01DOC06() {
    return progressiveWkf01DOC06;
  }
  public void setProgressiveWkf01DOC06(java.math.BigDecimal progressiveWkf01DOC06) {
    this.progressiveWkf01DOC06 = progressiveWkf01DOC06;
  }
  public java.math.BigDecimal getProgressiveWkf08DOC06() {
    return progressiveWkf08DOC06;
  }
  public void setProgressiveWkf08DOC06(java.math.BigDecimal progressiveWkf08DOC06) {
    this.progressiveWkf08DOC06 = progressiveWkf08DOC06;
  }
  public String getCompanyCodeSys01Doc06DOC06() {
    return companyCodeSys01Doc06DOC06;
  }
  public void setCompanyCodeSys01Doc06DOC06(String companyCodeSys01Doc06DOC06) {
    this.companyCodeSys01Doc06DOC06 = companyCodeSys01Doc06DOC06;
  }
  public String getDocTypeDoc06DOC06() {
    return docTypeDoc06DOC06;
  }
  public void setDocTypeDoc06DOC06(String docTypeDoc06DOC06) {
    this.docTypeDoc06DOC06 = docTypeDoc06DOC06;
  }
  public java.math.BigDecimal getDocYearDoc06DOC06() {
    return docYearDoc06DOC06;
  }
  public void setDocYearDoc06DOC06(java.math.BigDecimal docYearDoc06DOC06) {
    this.docYearDoc06DOC06 = docYearDoc06DOC06;
  }
  public java.math.BigDecimal getDocNumberDoc06DOC06() {
    return docNumberDoc06DOC06;
  }
  public void setDocNumberDoc06DOC06(java.math.BigDecimal docNumberDoc06DOC06) {
    this.docNumberDoc06DOC06 = docNumberDoc06DOC06;
  }
  public String getPaymentCodeReg10DOC06() {
    return paymentCodeReg10DOC06;
  }
  public void setPaymentCodeReg10DOC06(String paymentCodeReg10DOC06) {
    this.paymentCodeReg10DOC06 = paymentCodeReg10DOC06;
  }
  public String getPaymentDescriptionDOC06() {
    return paymentDescriptionDOC06;
  }
  public void setPaymentDescriptionDOC06(String paymentDescriptionDOC06) {
    this.paymentDescriptionDOC06 = paymentDescriptionDOC06;
  }
  public String getStartDayDOC06() {
    return startDayDOC06;
  }
  public void setStartDayDOC06(String startDayDOC06) {
    this.startDayDOC06 = startDayDOC06;
  }
  public String getPaymentTypeDescriptionDOC06() {
    return paymentTypeDescriptionDOC06;
  }
  public void setPaymentTypeDescriptionDOC06(String paymentTypeDescriptionDOC06) {
    this.paymentTypeDescriptionDOC06 = paymentTypeDescriptionDOC06;
  }
  public String getDescriptionWkf01DOC06() {
    return descriptionWkf01DOC06;
  }
  public void setDescriptionWkf01DOC06(String descriptionWkf01DOC06) {
    this.descriptionWkf01DOC06 = descriptionWkf01DOC06;
  }
  public String getNoteDOC06() {
    return noteDOC06;
  }
  public void setNoteDOC06(String noteDOC06) {
    this.noteDOC06 = noteDOC06;
  }
  public String getEnabledDOC06() {
    return enabledDOC06;
  }
  public void setEnabledDOC06(String enabledDOC06) {
    this.enabledDOC06 = enabledDOC06;
  }
  public String getSupplierCodePUR01() {
    return supplierCodePUR01;
  }
  public void setSupplierCodePUR01(String supplierCodePUR01) {
    this.supplierCodePUR01 = supplierCodePUR01;
  }
  public String getCurrencySymbolREG03() {
    return currencySymbolREG03;
  }
  public void setCurrencySymbolREG03(String currencySymbolREG03) {
    this.currencySymbolREG03 = currencySymbolREG03;
  }
  public String getDecimalSymbolREG03() {
    return decimalSymbolREG03;
  }
  public void setDecimalSymbolREG03(String decimalSymbolREG03) {
    this.decimalSymbolREG03 = decimalSymbolREG03;
  }
  public String getThousandSymbolREG03() {
    return thousandSymbolREG03;
  }
  public void setThousandSymbolREG03(String thousandSymbolREG03) {
    this.thousandSymbolREG03 = thousandSymbolREG03;
  }
  public java.math.BigDecimal getDecimalsREG03() {
    return decimalsREG03;
  }
  public void setDecimalsREG03(java.math.BigDecimal decimalsREG03) {
    this.decimalsREG03 = decimalsREG03;
  }
  public String getWarehouseCodeWar01DOC06() {
    return warehouseCodeWar01DOC06;
  }
  public void setWarehouseCodeWar01DOC06(String warehouseCodeWar01DOC06) {
    this.warehouseCodeWar01DOC06 = warehouseCodeWar01DOC06;
  }
  public String getDescriptionWar01DOC06() {
    return descriptionWar01DOC06;
  }
  public void setDescriptionWar01DOC06(String descriptionWar01DOC06) {
    this.descriptionWar01DOC06 = descriptionWar01DOC06;
  }
  public String getAddressWar01DOC06() {
    return addressWar01DOC06;
  }
  public void setAddressWar01DOC06(String addressWar01DOC06) {
    this.addressWar01DOC06 = addressWar01DOC06;
  }
  public String getCityWar01DOC06() {
    return cityWar01DOC06;
  }
  public void setCityWar01DOC06(String cityWar01DOC06) {
    this.cityWar01DOC06 = cityWar01DOC06;
  }
  public String getZipWar01DOC06() {
    return zipWar01DOC06;
  }
  public void setZipWar01DOC06(String zipWar01DOC06) {
    this.zipWar01DOC06 = zipWar01DOC06;
  }
  public String getProvinceWar01DOC06() {
    return provinceWar01DOC06;
  }
  public void setProvinceWar01DOC06(String provinceWar01DOC06) {
    this.provinceWar01DOC06 = provinceWar01DOC06;
  }
  public String getCountryWar01DOC06() {
    return countryWar01DOC06;
  }
  public void setCountryWar01DOC06(String countryWar01DOC06) {
    this.countryWar01DOC06 = countryWar01DOC06;
  }
  public java.math.BigDecimal getDocSequenceDOC06() {
    return docSequenceDOC06;
  }
  public void setDocSequenceDOC06(java.math.BigDecimal docSequenceDOC06) {
    this.docSequenceDOC06 = docSequenceDOC06;
  }
  public java.math.BigDecimal getDocSequenceDoc06DOC06() {
    return docSequenceDoc06DOC06;
  }
  public void setDocSequenceDoc06DOC06(java.math.BigDecimal docSequenceDoc06DOC06) {
    this.docSequenceDoc06DOC06 = docSequenceDoc06DOC06;
  }
  public String getName_2REG04() {
    return name_2REG04;
  }
  public void setName_2REG04(String name_2REG04) {
    this.name_2REG04 = name_2REG04;
  }

}
