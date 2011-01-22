package org.jallinone.registers.payments.java;

import org.jallinone.system.customizations.java.BaseValueObject;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store payments info.</p>
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
public class PaymentVO extends BaseValueObject {


	private String companyCodeSys01REG10;
  private String paymentCodeREG10;
  private java.math.BigDecimal progressiveSys10REG10;
  private String enabledREG10;
  private String descriptionSYS10;
  private String paymentTypeCodeReg11REG10;
  private java.math.BigDecimal instalmentNumberREG10;
  private java.math.BigDecimal stepREG10;
  private String startDayREG10;
  private java.math.BigDecimal firstInstalmentDaysREG10;
  private String paymentTypeDescriptionSYS10;
	private String accountCodeAcc02REG11;


  public PaymentVO() {
  }


  public String getPaymentCodeREG10() {
    return paymentCodeREG10;
  }
  public void setPaymentCodeREG10(String paymentCodeREG10) {
    this.paymentCodeREG10 = paymentCodeREG10;
  }
  public java.math.BigDecimal getProgressiveSys10REG10() {
    return progressiveSys10REG10;
  }
  public void setProgressiveSys10REG10(java.math.BigDecimal progressiveSys10REG10) {
    this.progressiveSys10REG10 = progressiveSys10REG10;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public String getEnabledREG10() {
    return enabledREG10;
  }
  public void setEnabledREG10(String enabledREG10) {
    this.enabledREG10 = enabledREG10;
  }
  public String getPaymentTypeCodeReg11REG10() {
    return paymentTypeCodeReg11REG10;
  }
  public void setPaymentTypeCodeReg11REG10(String paymentTypeCodeReg11REG10) {
    this.paymentTypeCodeReg11REG10 = paymentTypeCodeReg11REG10;
  }
  public java.math.BigDecimal getInstalmentNumberREG10() {
    return instalmentNumberREG10;
  }
  public void setInstalmentNumberREG10(java.math.BigDecimal instalmentNumberREG10) {
    this.instalmentNumberREG10 = instalmentNumberREG10;
  }
  public java.math.BigDecimal getStepREG10() {
    return stepREG10;
  }
  public void setStepREG10(java.math.BigDecimal stepREG10) {
    this.stepREG10 = stepREG10;
  }
  public String getStartDayREG10() {
    return startDayREG10;
  }
  public void setStartDayREG10(String startDayREG10) {
    this.startDayREG10 = startDayREG10;
  }
  public java.math.BigDecimal getFirstInstalmentDaysREG10() {
    return firstInstalmentDaysREG10;
  }
  public void setFirstInstalmentDaysREG10(java.math.BigDecimal firstInstalmentDaysREG10) {
    this.firstInstalmentDaysREG10 = firstInstalmentDaysREG10;
  }
  public String getPaymentTypeDescriptionSYS10() {
    return paymentTypeDescriptionSYS10;
  }
  public void setPaymentTypeDescriptionSYS10(String paymentTypeDescriptionSYS10) {
    this.paymentTypeDescriptionSYS10 = paymentTypeDescriptionSYS10;
  }
  public String getCompanyCodeSys01REG10() {
    return companyCodeSys01REG10;
  }
  public void setCompanyCodeSys01REG10(String companyCodeSys01REG10) {
    this.companyCodeSys01REG10 = companyCodeSys01REG10;
  }
  public String getAccountCodeAcc02REG11() {
    return accountCodeAcc02REG11;
  }
  public void setAccountCodeAcc02REG11(String accountCodeAcc02REG11) {
    this.accountCodeAcc02REG11 = accountCodeAcc02REG11;
  }

}
