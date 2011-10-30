package org.jallinone.expirations.java;

import org.openswing.swing.message.receive.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a payment related to one or more expirations for the same customer/supplier.</p>
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
public class PaymentVO  extends ValueObjectImpl {

	private String companyCodeSys01DOC27;
	private java.math.BigDecimal progressiveDOC27;
	private java.sql.Date paymentDateDOC27;
	private java.math.BigDecimal paymentValueDOC27;
	private String name_1REG04;
	private String name_2REG04;
	private java.math.BigDecimal progressiveReg04DOC27;
	private String customerSupplierCodeDOC27;
	private String paymentTypeCodeReg11DOC27;
	private String paymentDescriptionSYS10;
	private String accountCodeAcc02DOC27;
	private String acc02DescriptionSYS10;
	private String bankCodeReg12DOC27;
	private String descriptionREG12;

	private String currencyCodeReg03DOC27;
	private java.math.BigDecimal decimalsREG03;
	private String currencySymbolREG03;


	public PaymentVO() {
	}


  public String getAcc02DescriptionSYS10() {
    return acc02DescriptionSYS10;
  }
  public String getAccountCodeAcc02DOC27() {
    return accountCodeAcc02DOC27;
  }
  public String getBankCodeReg12DOC27() {
    return bankCodeReg12DOC27;
  }
  public String getCompanyCodeSys01DOC27() {
    return companyCodeSys01DOC27;
  }
  public String getCustomerSupplierCodeDOC27() {
    return customerSupplierCodeDOC27;
  }
  public String getDescriptionREG12() {
    return descriptionREG12;
  }
  public String getName_1REG04() {
    return name_1REG04;
  }
  public String getName_2REG04() {
    return name_2REG04;
  }
  public java.sql.Date getPaymentDateDOC27() {
    return paymentDateDOC27;
  }
  public String getPaymentDescriptionSYS10() {
    return paymentDescriptionSYS10;
  }
  public String getPaymentTypeCodeReg11DOC27() {
    return paymentTypeCodeReg11DOC27;
  }
  public java.math.BigDecimal getPaymentValueDOC27() {
    return paymentValueDOC27;
  }
  public java.math.BigDecimal getProgressiveDOC27() {
    return progressiveDOC27;
  }
  public void setProgressiveDOC27(java.math.BigDecimal progressiveDOC27) {
    this.progressiveDOC27 = progressiveDOC27;
  }
  public void setPaymentValueDOC27(java.math.BigDecimal paymentValueDOC27) {
    this.paymentValueDOC27 = paymentValueDOC27;
  }
  public void setPaymentTypeCodeReg11DOC27(String paymentTypeCodeReg11DOC27) {
    this.paymentTypeCodeReg11DOC27 = paymentTypeCodeReg11DOC27;
  }
  public void setPaymentDescriptionSYS10(String paymentDescriptionSYS10) {
    this.paymentDescriptionSYS10 = paymentDescriptionSYS10;
  }
  public void setPaymentDateDOC27(java.sql.Date paymentDateDOC27) {
    this.paymentDateDOC27 = paymentDateDOC27;
  }
  public void setName_2REG04(String name_2REG04) {
    this.name_2REG04 = name_2REG04;
  }
  public void setName_1REG04(String name_1REG04) {
    this.name_1REG04 = name_1REG04;
  }
  public void setDescriptionREG12(String descriptionREG12) {
    this.descriptionREG12 = descriptionREG12;
  }
  public void setCustomerSupplierCodeDOC27(String customerSupplierCodeDOC27) {
    this.customerSupplierCodeDOC27 = customerSupplierCodeDOC27;
  }
  public void setCompanyCodeSys01DOC27(String companyCodeSys01DOC27) {
    this.companyCodeSys01DOC27 = companyCodeSys01DOC27;
  }
  public void setBankCodeReg12DOC27(String bankCodeReg12DOC27) {
    this.bankCodeReg12DOC27 = bankCodeReg12DOC27;
  }
  public void setAccountCodeAcc02DOC27(String accountCodeAcc02DOC27) {
    this.accountCodeAcc02DOC27 = accountCodeAcc02DOC27;
  }
  public void setAcc02DescriptionSYS10(String acc02DescriptionSYS10) {
    this.acc02DescriptionSYS10 = acc02DescriptionSYS10;
  }
  public String getCurrencyCodeReg03DOC27() {
    return currencyCodeReg03DOC27;
  }
  public String getCurrencySymbolREG03() {
    return currencySymbolREG03;
  }
  public java.math.BigDecimal getDecimalsREG03() {
    return decimalsREG03;
  }
  public void setDecimalsREG03(java.math.BigDecimal decimalsREG03) {
    this.decimalsREG03 = decimalsREG03;
  }
  public void setCurrencySymbolREG03(String currencySymbolREG03) {
    this.currencySymbolREG03 = currencySymbolREG03;
  }
  public void setCurrencyCodeReg03DOC27(String currencyCodeReg03DOC27) {
    this.currencyCodeReg03DOC27 = currencyCodeReg03DOC27;
  }
  public java.math.BigDecimal getProgressiveReg04DOC27() {
    return progressiveReg04DOC27;
  }
  public void setProgressiveReg04DOC27(java.math.BigDecimal progressiveReg04DOC27) {
    this.progressiveReg04DOC27 = progressiveReg04DOC27;
  }




}
