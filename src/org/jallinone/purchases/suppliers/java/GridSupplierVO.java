package org.jallinone.purchases.suppliers.java;

import org.openswing.swing.message.receive.java.*;
import org.jallinone.subjects.java.Subject;
import org.jallinone.commons.java.ApplicationConsts;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a supplier.</p>
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
public class GridSupplierVO extends ValueObjectImpl implements Subject {

  private String companyCodeSys01REG04;
  private java.math.BigDecimal progressiveREG04;
  private String name_1REG04;
  private String name_2REG04;
  private String cityREG04;
  private String provinceREG04;
  private String countryREG04;
  private String taxCodeREG04;
  private String supplierCodePUR01;
  private String paymentCodeReg10PUR01;
  private String paymentDescriptionPUR01;
  private String debitAccountCodeAcc02PUR01;
  private String noteREG04;


  public GridSupplierVO() {
  }


  public String getSupplierDescription() {
    return
        (name_1REG04==null?"":name_1REG04+" ")+
        (name_2REG04==null?"":name_2REG04);
  }


  public void setSupplierDescription(String description) {
    name_1REG04 = description;
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
  public String getSupplierCodePUR01() {
    return supplierCodePUR01;
  }
  public void setSupplierCodePUR01(String supplierCodePUR01) {
    this.supplierCodePUR01 = supplierCodePUR01;
  }

  public String getSubjectTypeREG04() {
    return ApplicationConsts.SUBJECT_SUPPLIER;
  }

  public void setSubjectTypeREG04(String subjectTypeREG04) {}
  public String getPaymentCodeReg10PUR01() {
    return paymentCodeReg10PUR01;
  }
  public void setPaymentCodeReg10PUR01(String paymentCodeReg10PUR01) {
    this.paymentCodeReg10PUR01 = paymentCodeReg10PUR01;
  }
  public String getPaymentDescriptionPUR01() {
    return paymentDescriptionPUR01;
  }
  public void setPaymentDescriptionPUR01(String paymentDescriptionPUR01) {
    this.paymentDescriptionPUR01 = paymentDescriptionPUR01;
  }
  public String getDebitAccountCodeAcc02PUR01() {
    return debitAccountCodeAcc02PUR01;
  }
  public void setDebitAccountCodeAcc02PUR01(String debitAccountCodeAcc02PUR01) {
    this.debitAccountCodeAcc02PUR01 = debitAccountCodeAcc02PUR01;
  }
  public String getNoteREG04() {
    return noteREG04;
  }
  public void setNoteREG04(String noteREG04) {
    this.noteREG04 = noteREG04;
  }


}
