package org.jallinone.purchases.suppliers.java;

import org.jallinone.subjects.java.OrganizationVO;


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
public class DetailSupplierVO extends OrganizationVO {

  private String supplierCodePUR01;
  private String paymentCodeReg10PUR01;
  private String bankCodeReg12PUR01;
  private String enabledPUR01;
  private String paymentDescriptionSYS10;
  private String descriptionREG12;
  private String debitAccountCodeAcc02PUR01;
  private String costsAccountCodeAcc02PUR01;
  private String debitAccountDescrPUR01;
  private String costsAccountDescrPUR01;


  public DetailSupplierVO() {
  }


  public String getSupplierCodePUR01() {
    return supplierCodePUR01;
  }
  public void setSupplierCodePUR01(String supplierCodePUR01) {
    this.supplierCodePUR01 = supplierCodePUR01;
  }
  public String getPaymentCodeReg10PUR01() {
    return paymentCodeReg10PUR01;
  }
  public void setPaymentCodeReg10PUR01(String paymentCodeReg10PUR01) {
    this.paymentCodeReg10PUR01 = paymentCodeReg10PUR01;
  }
  public String getBankCodeReg12PUR01() {
    return bankCodeReg12PUR01;
  }
  public void setBankCodeReg12PUR01(String bankCodeReg12PUR01) {
    this.bankCodeReg12PUR01 = bankCodeReg12PUR01;
  }
  public String getEnabledPUR01() {
    return enabledPUR01;
  }
  public void setEnabledPUR01(String enabledPUR01) {
    this.enabledPUR01 = enabledPUR01;
  }
  public String getPaymentDescriptionSYS10() {
    return paymentDescriptionSYS10;
  }
  public void setPaymentDescriptionSYS10(String paymentDescriptionSYS10) {
    this.paymentDescriptionSYS10 = paymentDescriptionSYS10;
  }

  public String getDescriptionREG12() {
    return descriptionREG12;
  }
  public void setDescriptionREG12(String descriptionREG12) {
    this.descriptionREG12 = descriptionREG12;
  }
  public String getCostsAccountCodeAcc02PUR01() {
    return costsAccountCodeAcc02PUR01;
  }
  public String getCostsAccountDescrPUR01() {
    return costsAccountDescrPUR01;
  }
  public String getDebitAccountCodeAcc02PUR01() {
    return debitAccountCodeAcc02PUR01;
  }
  public String getDebitAccountDescrPUR01() {
    return debitAccountDescrPUR01;
  }
  public void setDebitAccountDescrPUR01(String debitAccountDescrPUR01) {
    this.debitAccountDescrPUR01 = debitAccountDescrPUR01;
  }
  public void setDebitAccountCodeAcc02PUR01(String debitAccountCodeAcc02PUR01) {
    this.debitAccountCodeAcc02PUR01 = debitAccountCodeAcc02PUR01;
  }
  public void setCostsAccountDescrPUR01(String costsAccountDescrPUR01) {
    this.costsAccountDescrPUR01 = costsAccountDescrPUR01;
  }
  public void setCostsAccountCodeAcc02PUR01(String costsAccountCodeAcc02PUR01) {
    this.costsAccountCodeAcc02PUR01 = costsAccountCodeAcc02PUR01;
  }

}
