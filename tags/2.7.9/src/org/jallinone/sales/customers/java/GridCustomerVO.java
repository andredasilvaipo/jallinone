package org.jallinone.sales.customers.java;

import org.openswing.swing.message.receive.java.*;
import org.jallinone.subjects.java.Subject;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a customer (private or organization).</p>
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
public class GridCustomerVO extends ValueObjectImpl implements Subject {

  private String companyCodeSys01REG04;
  private java.math.BigDecimal progressiveREG04;
  private String subjectTypeREG04;
  private String name_1REG04;
  private String name_2REG04;
  private String cityREG04;
  private String provinceREG04;
  private String countryREG04;
  private String taxCodeREG04;
  private String customerCodeSAL07;
  private String paymentCodeReg10SAL07;
  private String paymentDescriptionSAL07;
  private String vatCodeReg01SAL07;
  private String creditAccountCodeAcc02SAL07;
  private String noteREG04;
  private java.math.BigDecimal valueREG01;
  private java.math.BigDecimal deductibleREG01;
  private String vatDescriptionSYS10;


  public GridCustomerVO() {
  }


  public String getCompanyCodeSys01REG04() {
    return companyCodeSys01REG04;
  }
  public void setCompanyCodeSys01REG04(String companyCodeSys01REG04) {
    this.companyCodeSys01REG04 = companyCodeSys01REG04;
  }
  public java.math.BigDecimal getProgressiveREG04() {
    return progressiveREG04;
  }
  public void setProgressiveREG04(java.math.BigDecimal progressiveREG04) {
    this.progressiveREG04 = progressiveREG04;
  }
  public String getSubjectTypeREG04() {
    return subjectTypeREG04;
  }
  public void setSubjectTypeREG04(String subjectTypeREG04) {
    this.subjectTypeREG04 = subjectTypeREG04;
  }
  public String getName_1REG04() {
    return name_1REG04;
  }
  public void setName_1REG04(String name_1REG04) {
    this.name_1REG04 = name_1REG04;
  }
  public String getName_2REG04() {
    return name_2REG04;
  }
  public void setName_2REG04(String name_2REG04) {
    this.name_2REG04 = name_2REG04;
  }
  public String getCityREG04() {
    return cityREG04;
  }
  public void setCityREG04(String cityREG04) {
    this.cityREG04 = cityREG04;
  }
  public String getProvinceREG04() {
    return provinceREG04;
  }
  public void setProvinceREG04(String provinceREG04) {
    this.provinceREG04 = provinceREG04;
  }
  public String getCountryREG04() {
    return countryREG04;
  }
  public void setCountryREG04(String countryREG04) {
    this.countryREG04 = countryREG04;
  }
  public String getTaxCodeREG04() {
    return taxCodeREG04;
  }
  public void setTaxCodeREG04(String taxCodeREG04) {
    this.taxCodeREG04 = taxCodeREG04;
  }
  public String getCustomerCodeSAL07() {
    return customerCodeSAL07;
  }
  public void setCustomerCodeSAL07(String customerCodeSAL07) {
    this.customerCodeSAL07 = customerCodeSAL07;
  }
  public String getPaymentCodeReg10SAL07() {
    return paymentCodeReg10SAL07;
  }
  public String getPaymentDescriptionSAL07() {
    return paymentDescriptionSAL07;
  }
  public void setPaymentCodeReg10SAL07(String paymentCodeReg10SAL07) {
    this.paymentCodeReg10SAL07 = paymentCodeReg10SAL07;
  }
  public void setPaymentDescriptionSAL07(String paymentDescriptionSAL07) {
    this.paymentDescriptionSAL07 = paymentDescriptionSAL07;
  }
  public String getVatCodeReg01SAL07() {
    return vatCodeReg01SAL07;
  }
  public void setVatCodeReg01SAL07(String vatCodeReg01SAL07) {
    this.vatCodeReg01SAL07 = vatCodeReg01SAL07;
  }
  public String getCreditAccountCodeAcc02SAL07() {
    return creditAccountCodeAcc02SAL07;
  }
  public void setCreditAccountCodeAcc02SAL07(String creditAccountCodeAcc02SAL07) {
    this.creditAccountCodeAcc02SAL07 = creditAccountCodeAcc02SAL07;
  }
  public String getNoteREG04() {
    return noteREG04;
  }
  public void setNoteREG04(String noteREG04) {
    this.noteREG04 = noteREG04;
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
  public String getVatDescriptionSYS10() {
    return vatDescriptionSYS10;
  }
  public void setVatDescriptionSYS10(String vatDescriptionSYS10) {
    this.vatDescriptionSYS10 = vatDescriptionSYS10;
  }

}
