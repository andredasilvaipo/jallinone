package org.jallinone.sales.documents.java;

import org.openswing.swing.message.receive.java.*;
import org.jallinone.system.customizations.java.BaseValueObject;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store sale documents info (e.g. sale orders), for a detail form.</p>
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
public class DetailSaleDocVO extends BaseValueObject {

  private String companyCodeSys01DOC01;
  private String docTypeDOC01;
  private java.math.BigDecimal docYearDOC01;
  private java.math.BigDecimal docNumberDOC01;
  private java.math.BigDecimal progressiveReg04DOC01;
  private String name_1REG04;
  private String name_2REG04;
  private String companyCodeSys01Doc01DOC01;
  private String docTypeDoc01DOC01;
  private java.math.BigDecimal docYearDoc01DOC01;
  private java.math.BigDecimal docNumberDoc01DOC01;
  private java.sql.Date docDateDOC01;
  private String docStateDOC01;
  private String warehouseCodeWar01DOC01;
  private String descriptionWar01DOC01;
  private String pricelistCodeSal01DOC01;
  private String pricelistDescriptionDOC01;
  private String currencyCodeReg03DOC01;
  private String currencySymbolREG03;
  private String decimalSymbolREG03;
  private String thousandSymbolREG03;
  private java.math.BigDecimal decimalsREG03;
  private java.math.BigDecimal taxableIncomeDOC01;
  private java.math.BigDecimal totalVatDOC01;
  private java.math.BigDecimal allowanceDOC01;
  private java.math.BigDecimal depositDOC01;
  private java.math.BigDecimal totalDOC01;
  private String paymentCodeReg10DOC01;
  private String paymentDescriptionDOC01;
  private java.math.BigDecimal instalmentNumberDOC01;
  private java.math.BigDecimal stepDOC01;
  private String startDayDOC01;
  private java.math.BigDecimal firstInstalmentDaysDOC01;
  private String paymentTypeDescriptionDOC01;
  private String deliveryNoteDOC01;
  private String agentCodeSal10DOC01;
  private String name_1DOC01;
  private String name_2DOC01;
  private java.math.BigDecimal percentageDOC01;
  private String destinationCodeReg18DOC01;
  private String descriptionDOC01;
  private String addressDOC01;
  private String cityDOC01;
  private String zipDOC01;
  private String provinceDOC01;
  private String countryDOC01;
  private String noteDOC01;
  private String enabledDOC01;
  private String customerCodeSAL07;
  private String docRefNumberDOC01;
  private String headingNoteDOC01;
  private String footerNoteDOC01;

  private java.math.BigDecimal progressiveWkf01DOC01;
  private java.math.BigDecimal progressiveWkf08DOC01;
  private String descriptionWkf01DOC01;
  private String customerVatCodeReg01DOC01;
  private java.math.BigDecimal progressiveHie02WAR01;
  private java.math.BigDecimal progressiveHie01HIE02;
  private java.math.BigDecimal discountValueDOC01;
  private java.math.BigDecimal discountPercDOC01;
  private java.math.BigDecimal docSequenceDOC01;
  private java.math.BigDecimal docSequenceDoc01DOC01;
  private String sectionalDOC01;

  private java.sql.Date deliveryDateDOC01;

  private java.math.BigDecimal valueREG01;
  private java.math.BigDecimal deductibleREG01;
  private String vatDescriptionSYS10;


  public DetailSaleDocVO() {
  }


  public String getCompanyCodeSys01DOC01() {
    return companyCodeSys01DOC01;
  }
  public void setCompanyCodeSys01DOC01(String companyCodeSys01DOC01) {
    this.companyCodeSys01DOC01 = companyCodeSys01DOC01;
  }
  public String getDocTypeDOC01() {
    return docTypeDOC01;
  }
  public void setDocTypeDOC01(String docTypeDOC01) {
    this.docTypeDOC01 = docTypeDOC01;
  }
  public String getDocStateDOC01() {
    return docStateDOC01;
  }
  public void setDocStateDOC01(String docStateDOC01) {
    this.docStateDOC01 = docStateDOC01;
  }
  public String getPricelistCodeSal01DOC01() {
    return pricelistCodeSal01DOC01;
  }
  public void setPricelistCodeSal01DOC01(String pricelistCodeSal01DOC01) {
    this.pricelistCodeSal01DOC01 = pricelistCodeSal01DOC01;
  }
  public String getPricelistDescriptionDOC01() {
    return pricelistDescriptionDOC01;
  }
  public void setPricelistDescriptionDOC01(String pricelistDescriptionDOC01) {
    this.pricelistDescriptionDOC01 = pricelistDescriptionDOC01;
  }
  public String getCurrencyCodeReg03DOC01() {
    return currencyCodeReg03DOC01;
  }
  public void setCurrencyCodeReg03DOC01(String currencyCodeReg03DOC01) {
    this.currencyCodeReg03DOC01 = currencyCodeReg03DOC01;
  }
  public String getName_1REG04() {
    return name_1REG04;
  }
  public void setName_1REG04(String name_1REG04) {
    this.name_1REG04 = name_1REG04;
  }
  public java.math.BigDecimal getDocYearDOC01() {
    return docYearDOC01;
  }
  public void setDocYearDOC01(java.math.BigDecimal docYearDOC01) {
    this.docYearDOC01 = docYearDOC01;
  }
  public java.math.BigDecimal getDocNumberDOC01() {
    return docNumberDOC01;
  }
  public void setDocNumberDOC01(java.math.BigDecimal docNumberDOC01) {
    this.docNumberDOC01 = docNumberDOC01;
  }
  public java.math.BigDecimal getTaxableIncomeDOC01() {
    return taxableIncomeDOC01;
  }
  public void setTaxableIncomeDOC01(java.math.BigDecimal taxableIncomeDOC01) {
    this.taxableIncomeDOC01 = taxableIncomeDOC01;
  }
  public java.math.BigDecimal getTotalVatDOC01() {
    return totalVatDOC01;
  }
  public void setTotalVatDOC01(java.math.BigDecimal totalVatDOC01) {
    this.totalVatDOC01 = totalVatDOC01;
  }
  public java.math.BigDecimal getTotalDOC01() {
    return totalDOC01;
  }
  public void setTotalDOC01(java.math.BigDecimal totalDOC01) {
    this.totalDOC01 = totalDOC01;
  }
  public java.sql.Date getDocDateDOC01() {
    return docDateDOC01;
  }
  public void setDocDateDOC01(java.sql.Date docDateDOC01) {
    this.docDateDOC01 = docDateDOC01;
  }
  public java.math.BigDecimal getProgressiveReg04DOC01() {
    return progressiveReg04DOC01;
  }
  public void setProgressiveReg04DOC01(java.math.BigDecimal progressiveReg04DOC01) {
    this.progressiveReg04DOC01 = progressiveReg04DOC01;
  }
  public java.math.BigDecimal getInstalmentNumberDOC01() {
    return instalmentNumberDOC01;
  }
  public void setInstalmentNumberDOC01(java.math.BigDecimal instalmentNumberDOC01) {
    this.instalmentNumberDOC01 = instalmentNumberDOC01;
  }
  public java.math.BigDecimal getStepDOC01() {
    return stepDOC01;
  }
  public void setStepDOC01(java.math.BigDecimal stepDOC01) {
    this.stepDOC01 = stepDOC01;
  }
  public java.math.BigDecimal getFirstInstalmentDaysDOC01() {
    return firstInstalmentDaysDOC01;
  }
  public void setFirstInstalmentDaysDOC01(java.math.BigDecimal firstInstalmentDaysDOC01) {
    this.firstInstalmentDaysDOC01 = firstInstalmentDaysDOC01;
  }
  public java.math.BigDecimal getProgressiveWkf01DOC01() {
    return progressiveWkf01DOC01;
  }
  public void setProgressiveWkf01DOC01(java.math.BigDecimal progressiveWkf01DOC01) {
    this.progressiveWkf01DOC01 = progressiveWkf01DOC01;
  }
  public java.math.BigDecimal getProgressiveWkf08DOC01() {
    return progressiveWkf08DOC01;
  }
  public void setProgressiveWkf08DOC01(java.math.BigDecimal progressiveWkf08DOC01) {
    this.progressiveWkf08DOC01 = progressiveWkf08DOC01;
  }
  public String getCompanyCodeSys01Doc01DOC01() {
    return companyCodeSys01Doc01DOC01;
  }
  public void setCompanyCodeSys01Doc01DOC01(String companyCodeSys01Doc01DOC01) {
    this.companyCodeSys01Doc01DOC01 = companyCodeSys01Doc01DOC01;
  }
  public String getDocTypeDoc01DOC01() {
    return docTypeDoc01DOC01;
  }
  public void setDocTypeDoc01DOC01(String docTypeDoc01DOC01) {
    this.docTypeDoc01DOC01 = docTypeDoc01DOC01;
  }
  public java.math.BigDecimal getDocYearDoc01DOC01() {
    return docYearDoc01DOC01;
  }
  public void setDocYearDoc01DOC01(java.math.BigDecimal docYearDoc01DOC01) {
    this.docYearDoc01DOC01 = docYearDoc01DOC01;
  }
  public java.math.BigDecimal getDocNumberDoc01DOC01() {
    return docNumberDoc01DOC01;
  }
  public void setDocNumberDoc01DOC01(java.math.BigDecimal docNumberDoc01DOC01) {
    this.docNumberDoc01DOC01 = docNumberDoc01DOC01;
  }
  public String getPaymentCodeReg10DOC01() {
    return paymentCodeReg10DOC01;
  }
  public void setPaymentCodeReg10DOC01(String paymentCodeReg10DOC01) {
    this.paymentCodeReg10DOC01 = paymentCodeReg10DOC01;
  }
  public String getPaymentDescriptionDOC01() {
    return paymentDescriptionDOC01;
  }
  public void setPaymentDescriptionDOC01(String paymentDescriptionDOC01) {
    this.paymentDescriptionDOC01 = paymentDescriptionDOC01;
  }
  public String getStartDayDOC01() {
    return startDayDOC01;
  }
  public void setStartDayDOC01(String startDayDOC01) {
    this.startDayDOC01 = startDayDOC01;
  }
  public String getPaymentTypeDescriptionDOC01() {
    return paymentTypeDescriptionDOC01;
  }
  public void setPaymentTypeDescriptionDOC01(String paymentTypeDescriptionDOC01) {
    this.paymentTypeDescriptionDOC01 = paymentTypeDescriptionDOC01;
  }
  public String getDescriptionWkf01DOC01() {
    return descriptionWkf01DOC01;
  }
  public void setDescriptionWkf01DOC01(String descriptionWkf01DOC01) {
    this.descriptionWkf01DOC01 = descriptionWkf01DOC01;
  }
  public String getNoteDOC01() {
    return noteDOC01;
  }
  public void setNoteDOC01(String noteDOC01) {
    this.noteDOC01 = noteDOC01;
  }
  public String getEnabledDOC01() {
    return enabledDOC01;
  }
  public void setEnabledDOC01(String enabledDOC01) {
    this.enabledDOC01 = enabledDOC01;
  }
  public String getCustomerCodeSAL07() {
    return customerCodeSAL07;
  }
  public void setCustomerCodeSAL07(String customerCodeSAL07) {
    this.customerCodeSAL07 = customerCodeSAL07;
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
  public String getWarehouseCodeWar01DOC01() {
    return warehouseCodeWar01DOC01;
  }
  public void setWarehouseCodeWar01DOC01(String warehouseCodeWar01DOC01) {
    this.warehouseCodeWar01DOC01 = warehouseCodeWar01DOC01;
  }
  public String getDescriptionWar01DOC01() {
    return descriptionWar01DOC01;
  }
  public void setDescriptionWar01DOC01(String descriptionWar01DOC01) {
    this.descriptionWar01DOC01 = descriptionWar01DOC01;
  }
  public java.math.BigDecimal getAllowanceDOC01() {
    return allowanceDOC01;
  }
  public void setAllowanceDOC01(java.math.BigDecimal allowanceDOC01) {
    this.allowanceDOC01 = allowanceDOC01;
  }
  public java.math.BigDecimal getDepositDOC01() {
    return depositDOC01;
  }
  public void setDepositDOC01(java.math.BigDecimal depositDOC01) {
    this.depositDOC01 = depositDOC01;
  }
  public String getAgentCodeSal10DOC01() {
    return agentCodeSal10DOC01;
  }
  public void setAgentCodeSal10DOC01(String agentCodeSal10DOC01) {
    this.agentCodeSal10DOC01 = agentCodeSal10DOC01;
  }
  public String getName_1DOC01() {
    return name_1DOC01;
  }
  public void setName_1DOC01(String name_1DOC01) {
    this.name_1DOC01 = name_1DOC01;
  }
  public String getName_2DOC01() {
    return name_2DOC01;
  }
  public void setName_2DOC01(String name_2DOC01) {
    this.name_2DOC01 = name_2DOC01;
  }
  public java.math.BigDecimal getPercentageDOC01() {
    return percentageDOC01;
  }
  public void setPercentageDOC01(java.math.BigDecimal percentageDOC01) {
    this.percentageDOC01 = percentageDOC01;
  }
  public String getDeliveryNoteDOC01() {
    return deliveryNoteDOC01;
  }
  public void setDeliveryNoteDOC01(String deliveryNoteDOC01) {
    this.deliveryNoteDOC01 = deliveryNoteDOC01;
  }
  public String getDestinationCodeReg18DOC01() {
    return destinationCodeReg18DOC01;
  }
  public void setDestinationCodeReg18DOC01(String destinationCodeReg18DOC01) {
    this.destinationCodeReg18DOC01 = destinationCodeReg18DOC01;
  }
  public String getDescriptionDOC01() {
    return descriptionDOC01;
  }
  public void setDescriptionDOC01(String descriptionDOC01) {
    this.descriptionDOC01 = descriptionDOC01;
  }
  public String getDocRefNumberDOC01() {
    return docRefNumberDOC01;
  }
  public void setDocRefNumberDOC01(String docRefNumberDOC01) {
    this.docRefNumberDOC01 = docRefNumberDOC01;
  }
  public String getHeadingNoteDOC01() {
    return headingNoteDOC01;
  }
  public void setHeadingNoteDOC01(String headingNoteDOC01) {
    this.headingNoteDOC01 = headingNoteDOC01;
  }
  public String getFooterNoteDOC01() {
    return footerNoteDOC01;
  }
  public void setFooterNoteDOC01(String footerNoteDOC01) {
    this.footerNoteDOC01 = footerNoteDOC01;
  }
  public String getAddressDOC01() {
    return addressDOC01;
  }
  public void setAddressDOC01(String addressDOC01) {
    this.addressDOC01 = addressDOC01;
  }
  public void setCountryDOC01(String countryDOC01) {
    this.countryDOC01 = countryDOC01;
  }
  public String getCountryDOC01() {
    return countryDOC01;
  }
  public String getCityDOC01() {
    return cityDOC01;
  }
  public void setCityDOC01(String cityDOC01) {
    this.cityDOC01 = cityDOC01;
  }
  public String getProvinceDOC01() {
    return provinceDOC01;
  }
  public void setProvinceDOC01(String provinceDOC01) {
    this.provinceDOC01 = provinceDOC01;
  }
  public void setZipDOC01(String zipDOC01) {
    this.zipDOC01 = zipDOC01;
  }
  public String getZipDOC01() {
    return zipDOC01;
  }
  public String getName_2REG04() {
    return name_2REG04;
  }
  public void setName_2REG04(String name_2REG04) {
    this.name_2REG04 = name_2REG04;
  }
  public String getCustomerVatCodeReg01DOC01() {
    return customerVatCodeReg01DOC01;
  }
  public void setCustomerVatCodeReg01DOC01(String customerVatCodeReg01DOC01) {
    this.customerVatCodeReg01DOC01 = customerVatCodeReg01DOC01;
  }
  public java.math.BigDecimal getProgressiveHie02WAR01() {
    return progressiveHie02WAR01;
  }
  public void setProgressiveHie02WAR01(java.math.BigDecimal progressiveHie02WAR01) {
    this.progressiveHie02WAR01 = progressiveHie02WAR01;
  }
  public java.math.BigDecimal getDiscountPercDOC01() {
    return discountPercDOC01;
  }
  public java.math.BigDecimal getDiscountValueDOC01() {
    return discountValueDOC01;
  }
  public void setDiscountPercDOC01(java.math.BigDecimal discountPercDOC01) {
    this.discountPercDOC01 = discountPercDOC01;
  }
  public void setDiscountValueDOC01(java.math.BigDecimal discountValueDOC01) {
    this.discountValueDOC01 = discountValueDOC01;
  }
  public java.math.BigDecimal getDocSequenceDOC01() {
    return docSequenceDOC01;
  }
  public void setDocSequenceDOC01(java.math.BigDecimal docSequenceDOC01) {
    this.docSequenceDOC01 = docSequenceDOC01;
  }
  public java.math.BigDecimal getDocSequenceDoc01DOC01() {
    return docSequenceDoc01DOC01;
  }
  public void setDocSequenceDoc01DOC01(java.math.BigDecimal docSequenceDoc01DOC01) {
    this.docSequenceDoc01DOC01 = docSequenceDoc01DOC01;
  }
  public String getSectionalDOC01() {
    return sectionalDOC01;
  }
  public void setSectionalDOC01(String sectionalDOC01) {
    this.sectionalDOC01 = sectionalDOC01;
  }
  public java.math.BigDecimal getProgressiveHie01HIE02() {
    return progressiveHie01HIE02;
  }
  public void setProgressiveHie01HIE02(java.math.BigDecimal progressiveHie01HIE02) {
    this.progressiveHie01HIE02 = progressiveHie01HIE02;
  }
  public java.sql.Date getDeliveryDateDOC01() {
    return deliveryDateDOC01;
  }
  public void setDeliveryDateDOC01(java.sql.Date deliveryDateDOC01) {
    this.deliveryDateDOC01 = deliveryDateDOC01;
  }
  public java.math.BigDecimal getDeductibleREG01() {
    return deductibleREG01;
  }
  public void setDeductibleREG01(java.math.BigDecimal deductibleREG01) {
    this.deductibleREG01 = deductibleREG01;
  }
  public java.math.BigDecimal getValueREG01() {
    return valueREG01;
  }
  public void setValueREG01(java.math.BigDecimal valueREG01) {
    this.valueREG01 = valueREG01;
  }
  public String getVatDescriptionSYS10() {
    return vatDescriptionSYS10;
  }
  public void setVatDescriptionSYS10(String vatDescriptionSYS10) {
    this.vatDescriptionSYS10 = vatDescriptionSYS10;
  }

}
