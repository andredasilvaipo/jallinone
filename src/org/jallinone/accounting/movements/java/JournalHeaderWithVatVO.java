package org.jallinone.accounting.movements.java;

import java.util.ArrayList;
import java.math.BigDecimal;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value Object used to create an item in the journal, using vat and based on the specified register.</p>
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
public class JournalHeaderWithVatVO extends JournalHeaderVO {


  private String customerCodeSAL07;
  private String name_1REG04;
  private String name_2REG04;
  private String registerCodeACC04;
  private String registerDescriptionACC04;
  private String supplierCodePUR01;
  private String accountCodeTypeACC06;
  private String paymentCodeREG10;
  private String paymentDescriptionREG10;
  private String accountCodeAcc02ACC04;
  private String accountDescriptionACC04;
  private String creditAccountCodeAcc02SAL07;
  private String debitAccountCodeAcc02PUR01;
  private ArrayList vats = new ArrayList();
  private BigDecimal progressiveREG04;
  private BigDecimal docSequenceDOC19;
  private String docTypeDOC19;
  private String currencyCodeREG01;
  private BigDecimal totalValue;


  public JournalHeaderWithVatVO() {
  }


  public final void addVat(VatRowVO vo) {
    vats.add(vo);
  }


  public final ArrayList getVats() {
    return vats;
  }


  public String getCustomerCodeSAL07() {
    return customerCodeSAL07;
  }
  public void setCustomerCodeSAL07(String customerCodeSAL07) {
    this.customerCodeSAL07 = customerCodeSAL07;
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
  public String getRegisterCodeACC04() {
    return registerCodeACC04;
  }
  public void setRegisterCodeACC04(String registerCodeACC04) {
    this.registerCodeACC04 = registerCodeACC04;
  }
  public String getRegisterDescriptionACC04() {
    return registerDescriptionACC04;
  }
  public void setRegisterDescriptionACC04(String registerDescriptionACC04) {
    this.registerDescriptionACC04 = registerDescriptionACC04;
  }
  public String getSupplierCodePUR01() {
    return supplierCodePUR01;
  }
  public void setSupplierCodePUR01(String supplierCodePUR01) {
    this.supplierCodePUR01 = supplierCodePUR01;
  }
  public String getAccountCodeTypeACC06() {
    return accountCodeTypeACC06;
  }
  public void setAccountCodeTypeACC06(String accountCodeTypeACC06) {
    this.accountCodeTypeACC06 = accountCodeTypeACC06;
  }
  public String getPaymentCodeREG10() {
    return paymentCodeREG10;
  }
  public String getPaymentDescriptionREG10() {
    return paymentDescriptionREG10;
  }
  public void setPaymentCodeREG10(String paymentCodeREG10) {
    this.paymentCodeREG10 = paymentCodeREG10;
  }
  public void setPaymentDescriptionREG10(String paymentDescriptionREG10) {
    this.paymentDescriptionREG10 = paymentDescriptionREG10;
  }
  public String getAccountCodeAcc02ACC04() {
    return accountCodeAcc02ACC04;
  }
  public String getAccountDescriptionACC04() {
    return accountDescriptionACC04;
  }
  public void setAccountDescriptionACC04(String accountDescriptionACC04) {
    this.accountDescriptionACC04 = accountDescriptionACC04;
  }
  public void setAccountCodeAcc02ACC04(String accountCodeAcc02ACC04) {
    this.accountCodeAcc02ACC04 = accountCodeAcc02ACC04;
  }
  public String getCreditAccountCodeAcc02SAL07() {
    return creditAccountCodeAcc02SAL07;
  }
  public String getDebitAccountCodeAcc02PUR01() {
    return debitAccountCodeAcc02PUR01;
  }
  public void setCreditAccountCodeAcc02SAL07(String creditAccountCodeAcc02SAL07) {
    this.creditAccountCodeAcc02SAL07 = creditAccountCodeAcc02SAL07;
  }
  public void setDebitAccountCodeAcc02PUR01(String debitAccountCodeAcc02PUR01) {
    this.debitAccountCodeAcc02PUR01 = debitAccountCodeAcc02PUR01;
  }
  public BigDecimal getProgressiveREG04() {
    return progressiveREG04;
  }
  public void setProgressiveREG04(BigDecimal progressiveREG04) {
    this.progressiveREG04 = progressiveREG04;
  }
  public BigDecimal getDocSequenceDOC19() {
    return docSequenceDOC19;
  }
  public String getDocTypeDOC19() {
    return docTypeDOC19;
  }
  public void setDocSequenceDOC19(BigDecimal docSequenceDOC19) {
    this.docSequenceDOC19 = docSequenceDOC19;
  }
  public void setDocTypeDOC19(String docTypeDOC19) {
    this.docTypeDOC19 = docTypeDOC19;
  }
  public String getCurrencyCodeREG01() {
    return currencyCodeREG01;
  }
  public void setCurrencyCodeREG01(String currencyCodeREG01) {
    this.currencyCodeREG01 = currencyCodeREG01;
  }
  public BigDecimal getTotalValue() {
    return totalValue;
  }
  public void setTotalValue(BigDecimal totalValue) {
    this.totalValue = totalValue;
  }


}