package org.jallinone.expirations.java;

import org.openswing.swing.message.receive.java.*;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store an expiration used in sale/purchase invoices..</p>
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
public class ExpirationVO extends ValueObjectImpl {

  private String companyCodeSys01DOC19;
  private String docTypeDOC19;
  private java.math.BigDecimal progressiveDOC19;
  private java.math.BigDecimal docYearDOC19;
  private java.math.BigDecimal docNumberDOC19;
  private java.math.BigDecimal docSequenceDOC19;
  private java.math.BigDecimal valueDOC19;
  private java.sql.Date docDateDOC19;
  private java.sql.Date expirationDateDOC19;
  private String name_1DOC19;
  private String name_2DOC19;
  private Boolean payedDOC19;
  private String customerSupplierCodeDOC19;
  private java.math.BigDecimal progressiveReg04DOC19;
  private String descriptionDOC19;
  private String currencyCodeReg03DOC19;
  private java.math.BigDecimal decimalsREG03;
  private String currencySymbolREG03;

	private java.sql.Date payedDateDOC19;
	private java.math.BigDecimal payedValueDOC19;
	private String realPaymentTypeCodeReg11DOC19;
	private String realPaymentDescriptionSYS10;
	private String paymentTypeCodeReg11DOC19;
	private String paymentDescriptionSYS10;

	private String roundingAccountCodeAcc02DOC19;
	private String roundingAcc02DescriptionSYS10;

	private String realAccountCodeAcc02DOC19;
	private String realAcc02DescriptionSYS10;

	private java.math.BigDecimal alreadyPayedDOC19;


  public ExpirationVO() {
  }


  public String getCompanyCodeSys01DOC19() {
    return companyCodeSys01DOC19;
  }
  public void setCompanyCodeSys01DOC19(String companyCodeSys01DOC19) {
    this.companyCodeSys01DOC19 = companyCodeSys01DOC19;
  }
  public String getDocTypeDOC19() {
    return docTypeDOC19;
  }
  public void setDocTypeDOC19(String docTypeDOC19) {
    this.docTypeDOC19 = docTypeDOC19;
  }
  public java.math.BigDecimal getDocYearDOC19() {
    return docYearDOC19;
  }
  public void setDocYearDOC19(java.math.BigDecimal docYearDOC19) {
    this.docYearDOC19 = docYearDOC19;
  }
  public java.math.BigDecimal getDocNumberDOC19() {
    return docNumberDOC19;
  }
  public void setDocNumberDOC19(java.math.BigDecimal docNumberDOC19) {
    this.docNumberDOC19 = docNumberDOC19;
  }
  public java.math.BigDecimal getDocSequenceDOC19() {
    return docSequenceDOC19;
  }
  public void setDocSequenceDOC19(java.math.BigDecimal docSequenceDOC19) {
    this.docSequenceDOC19 = docSequenceDOC19;
  }
  public java.math.BigDecimal getValueDOC19() {
    return valueDOC19;
  }
  public void setValueDOC19(java.math.BigDecimal valueDOC19) {
    this.valueDOC19 = valueDOC19;
  }
  public java.sql.Date getDocDateDOC19() {
    return docDateDOC19;
  }
  public void setDocDateDOC19(java.sql.Date docDateDOC19) {
    this.docDateDOC19 = docDateDOC19;
  }
  public String getDescriptionDOC19() {
    return descriptionDOC19;
  }
  public void setDescriptionDOC19(String descriptionDOC19) {
    this.descriptionDOC19 = descriptionDOC19;
  }
  public java.sql.Date getExpirationDateDOC19() {
    return expirationDateDOC19;
  }
  public void setExpirationDateDOC19(java.sql.Date expirationDateDOC19) {
    this.expirationDateDOC19 = expirationDateDOC19;
  }
  public String getName_1DOC19() {
    return name_1DOC19;
  }
  public void setName_1DOC19(String name_1DOC19) {
    this.name_1DOC19 = name_1DOC19;
  }
  public String getName_2DOC19() {
    return name_2DOC19;
  }
  public void setName_2DOC19(String name_2DOC19) {
    this.name_2DOC19 = name_2DOC19;
  }
  public Boolean getPayedDOC19() {
    return payedDOC19;
  }
  public void setPayedDOC19(Boolean payedDOC19) {
    this.payedDOC19 = payedDOC19;
  }
  public String getCustomerSupplierCodeDOC19() {
    return customerSupplierCodeDOC19;
  }
  public void setCustomerSupplierCodeDOC19(String customerSupplierCodeDOC19) {
    this.customerSupplierCodeDOC19 = customerSupplierCodeDOC19;
  }
  public java.math.BigDecimal getProgressiveReg04DOC19() {
    return progressiveReg04DOC19;
  }
  public void setProgressiveReg04DOC19(java.math.BigDecimal progressiveReg04DOC19) {
    this.progressiveReg04DOC19 = progressiveReg04DOC19;
  }
  public java.math.BigDecimal getProgressiveDOC19() {
    return progressiveDOC19;
  }
  public void setProgressiveDOC19(java.math.BigDecimal progressiveDOC19) {
    this.progressiveDOC19 = progressiveDOC19;
  }
  public String getCurrencyCodeReg03DOC19() {
    return currencyCodeReg03DOC19;
  }
  public void setCurrencyCodeReg03DOC19(String currencyCodeReg03DOC19) {
    this.currencyCodeReg03DOC19 = currencyCodeReg03DOC19;
  }
  public String getCurrencySymbolREG03() {
    return currencySymbolREG03;
  }
  public java.math.BigDecimal getDecimalsREG03() {
    return decimalsREG03;
  }
  public void setCurrencySymbolREG03(String currencySymbolREG03) {
    this.currencySymbolREG03 = currencySymbolREG03;
  }
  public void setDecimalsREG03(java.math.BigDecimal decimalsREG03) {
    this.decimalsREG03 = decimalsREG03;
  }

  public java.sql.Date getPayedDateDOC19() {
    return payedDateDOC19;
  }
  public void setPayedDateDOC19(java.sql.Date payedDateDOC19) {
    this.payedDateDOC19 = payedDateDOC19;
  }
  public void setPayedValueDOC19(java.math.BigDecimal payedValueDOC19) {
    this.payedValueDOC19 = payedValueDOC19;
  }
  public void setPaymentDescriptionSYS10(String paymentDescriptionSYS10) {
    this.paymentDescriptionSYS10 = paymentDescriptionSYS10;
  }
  public void setPaymentTypeCodeReg11DOC19(String paymentTypeCodeReg11DOC19) {
    this.paymentTypeCodeReg11DOC19 = paymentTypeCodeReg11DOC19;
  }
  public java.math.BigDecimal getPayedValueDOC19() {
    return payedValueDOC19;
  }
  public String getPaymentDescriptionSYS10() {
    return paymentDescriptionSYS10;
  }
  public String getPaymentTypeCodeReg11DOC19() {
    return paymentTypeCodeReg11DOC19;
  }
  public String getRealPaymentDescriptionSYS10() {
    return realPaymentDescriptionSYS10;
  }
  public String getRealPaymentTypeCodeReg11DOC19() {
    return realPaymentTypeCodeReg11DOC19;
  }
  public void setRealPaymentDescriptionSYS10(String realPaymentDescriptionSYS10) {
    this.realPaymentDescriptionSYS10 = realPaymentDescriptionSYS10;
  }
  public void setRealPaymentTypeCodeReg11DOC19(String realPaymentTypeCodeReg11DOC19) {
    this.realPaymentTypeCodeReg11DOC19 = realPaymentTypeCodeReg11DOC19;
  }
  public String getRealAcc02DescriptionSYS10() {
    return realAcc02DescriptionSYS10;
  }
  public String getRealAccountCodeAcc02DOC19() {
    return realAccountCodeAcc02DOC19;
  }
  public String getRoundingAcc02DescriptionSYS10() {
    return roundingAcc02DescriptionSYS10;
  }
  public String getRoundingAccountCodeAcc02DOC19() {
    return roundingAccountCodeAcc02DOC19;
  }
  public void setRoundingAccountCodeAcc02DOC19(String roundingAccountCodeAcc02DOC19) {
    this.roundingAccountCodeAcc02DOC19 = roundingAccountCodeAcc02DOC19;
  }
  public void setRoundingAcc02DescriptionSYS10(String roundingAcc02DescriptionSYS10) {
    this.roundingAcc02DescriptionSYS10 = roundingAcc02DescriptionSYS10;
  }
  public void setRealAccountCodeAcc02DOC19(String realAccountCodeAcc02DOC19) {
    this.realAccountCodeAcc02DOC19 = realAccountCodeAcc02DOC19;
  }
  public void setRealAcc02DescriptionSYS10(String realAcc02DescriptionSYS10) {
    this.realAcc02DescriptionSYS10 = realAcc02DescriptionSYS10;
  }
  public java.math.BigDecimal getAlreadyPayedDOC19() {
    return alreadyPayedDOC19;
  }
  public void setAlreadyPayedDOC19(java.math.BigDecimal alreadyPayedDOC19) {
    this.alreadyPayedDOC19 = alreadyPayedDOC19;
  }

}
