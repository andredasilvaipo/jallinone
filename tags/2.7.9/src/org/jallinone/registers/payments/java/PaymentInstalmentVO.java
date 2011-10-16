package org.jallinone.registers.payments.java;

import org.jallinone.system.customizations.java.BaseValueObject;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store payment instalment info.</p>
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
public class PaymentInstalmentVO extends BaseValueObject {


	private String companyCodeSys01REG17;
  private String paymentCodeReg10REG17;
  private String paymentTypeCodeReg11REG17;
  private java.math.BigDecimal rateNumberREG17;
  private java.math.BigDecimal instalmentDaysREG17;
  private String paymentTypeDescriptionSYS10;
  private java.math.BigDecimal percentageREG17;


  public PaymentInstalmentVO() {
  }


  public String getPaymentCodeReg10REG17() {
    return paymentCodeReg10REG17;
  }
  public void setPaymentCodeReg10REG17(String paymentCodeReg10REG17) {
    this.paymentCodeReg10REG17 = paymentCodeReg10REG17;
  }
  public String getPaymentTypeCodeReg11REG17() {
    return paymentTypeCodeReg11REG17;
  }
  public void setPaymentTypeCodeReg11REG17(String paymentTypeCodeReg11REG17) {
    this.paymentTypeCodeReg11REG17 = paymentTypeCodeReg11REG17;
  }
  public java.math.BigDecimal getRateNumberREG17() {
    return rateNumberREG17;
  }
  public void setRateNumberREG17(java.math.BigDecimal rateNumberREG17) {
    this.rateNumberREG17 = rateNumberREG17;
  }
  public java.math.BigDecimal getInstalmentDaysREG17() {
    return instalmentDaysREG17;
  }
  public void setInstalmentDaysREG17(java.math.BigDecimal instalmentDaysREG17) {
    this.instalmentDaysREG17 = instalmentDaysREG17;
  }
  public java.math.BigDecimal getPercentageREG17() {
    return percentageREG17;
  }
  public void setPercentageREG17(java.math.BigDecimal percentageREG17) {
    this.percentageREG17 = percentageREG17;
  }
  public String getPaymentTypeDescriptionSYS10() {
    return paymentTypeDescriptionSYS10;
  }
  public void setPaymentTypeDescriptionSYS10(String paymentTypeDescriptionSYS10) {
    this.paymentTypeDescriptionSYS10 = paymentTypeDescriptionSYS10;
  }
  public String getCompanyCodeSys01REG17() {
    return companyCodeSys01REG17;
  }
  public void setCompanyCodeSys01REG17(String companyCodeSys01REG17) {
    this.companyCodeSys01REG17 = companyCodeSys01REG17;
  }

}
