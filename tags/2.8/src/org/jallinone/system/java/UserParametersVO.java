package org.jallinone.system.java;

import org.openswing.swing.message.receive.java.*;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store user parameters, based on the specified company code.</p>
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
public class UserParametersVO extends ValueObjectImpl {

  private String companyCodeSys01SYS19;
  private String customerCodeSAL07;
  private String warehouseCodeWAR01;
  private String warehouseDescriptionSYS10;
  private String name_1REG04;
  private String name_2REG04;
  private String receiptPath;
  private String creditAccountCodeAcc02SAL07;
  private String itemsAccountCodeAcc02SAL07;
  private String activitiesAccountCodeAcc02SAL07;
  private String chargesAccountCodeAcc02SAL07;
  private String debitAccountCodeAcc02PUR01;
  private String costsAccountCodeAcc02PUR01;
  private String caseAccountCodeAcc02DOC19;
  private String creditAccountDescrSAL07;
  private String itemsAccountDescrSAL07;
  private String activitiesAccountDescrSAL07;
  private String chargesAccountDescrSAL07;
  private String debitAccountDescrPUR01;
  private String costsAccountDescrPUR01;
  private String caseAccountDescrDOC19;
  private String bankAccountDescrDOC19;
  private String vatEndorseAccountDescrDOC19;
  private String bankAccountCodeAcc02DOC19;
  private String vatEndorseAccountCodeAcc02DOC19;
	private String roundingCostsAccountCodeAcc02DOC19;
	private String roundingCostsDescrDOC19;
	private String roundingProceedsAccountCodeAcc02DOC19;
	private String roundingProceedsDescrDOC19;


  public UserParametersVO() {
  }


  public String getCompanyCodeSys01SYS19() {
    return companyCodeSys01SYS19;
  }
  public void setCompanyCodeSys01SYS19(String companyCodeSys01SYS19) {
    this.companyCodeSys01SYS19 = companyCodeSys01SYS19;
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
  public String getReceiptPath() {
    return receiptPath;
  }
  public void setReceiptPath(String receiptPath) {
    this.receiptPath = receiptPath;
  }
  public String getCreditAccountCodeAcc02SAL07() {
    return creditAccountCodeAcc02SAL07;
  }
  public void setCreditAccountCodeAcc02SAL07(String creditAccountCodeAcc02SAL07) {
    this.creditAccountCodeAcc02SAL07 = creditAccountCodeAcc02SAL07;
  }
  public String getItemsAccountCodeAcc02SAL07() {
    return itemsAccountCodeAcc02SAL07;
  }
  public void setItemsAccountCodeAcc02SAL07(String itemsAccountCodeAcc02SAL07) {
    this.itemsAccountCodeAcc02SAL07 = itemsAccountCodeAcc02SAL07;
  }
  public String getActivitiesAccountCodeAcc02SAL07() {
    return activitiesAccountCodeAcc02SAL07;
  }
  public void setActivitiesAccountCodeAcc02SAL07(String activitiesAccountCodeAcc02SAL07) {
    this.activitiesAccountCodeAcc02SAL07 = activitiesAccountCodeAcc02SAL07;
  }
  public String getChargesAccountCodeAcc02SAL07() {
    return chargesAccountCodeAcc02SAL07;
  }
  public void setChargesAccountCodeAcc02SAL07(String chargesAccountCodeAcc02SAL07) {
    this.chargesAccountCodeAcc02SAL07 = chargesAccountCodeAcc02SAL07;
  }
  public String getDebitAccountCodeAcc02PUR01() {
    return debitAccountCodeAcc02PUR01;
  }
  public void setDebitAccountCodeAcc02PUR01(String debitAccountCodeAcc02PUR01) {
    this.debitAccountCodeAcc02PUR01 = debitAccountCodeAcc02PUR01;
  }
  public String getCostsAccountCodeAcc02PUR01() {
    return costsAccountCodeAcc02PUR01;
  }
  public void setCostsAccountCodeAcc02PUR01(String costsAccountCodeAcc02PUR01) {
    this.costsAccountCodeAcc02PUR01 = costsAccountCodeAcc02PUR01;
  }
  public String getDebitAccountDescrPUR01() {
    return debitAccountDescrPUR01;
  }
  public String getItemsAccountDescrSAL07() {
    return itemsAccountDescrSAL07;
  }
  public String getCostsAccountDescrPUR01() {
    return costsAccountDescrPUR01;
  }
  public String getCreditAccountDescrSAL07() {
    return creditAccountDescrSAL07;
  }
  public String getChargesAccountDescrSAL07() {
    return chargesAccountDescrSAL07;
  }
  public String getActivitiesAccountDescrSAL07() {
    return activitiesAccountDescrSAL07;
  }
  public void setActivitiesAccountDescrSAL07(String activitiesAccountDescrSAL07) {
    this.activitiesAccountDescrSAL07 = activitiesAccountDescrSAL07;
  }
  public void setChargesAccountDescrSAL07(String chargesAccountDescrSAL07) {
    this.chargesAccountDescrSAL07 = chargesAccountDescrSAL07;
  }
  public void setCreditAccountDescrSAL07(String creditAccountDescrSAL07) {
    this.creditAccountDescrSAL07 = creditAccountDescrSAL07;
  }
  public void setCostsAccountDescrPUR01(String costsAccountDescrPUR01) {
    this.costsAccountDescrPUR01 = costsAccountDescrPUR01;
  }
  public void setDebitAccountDescrPUR01(String debitAccountDescrPUR01) {
    this.debitAccountDescrPUR01 = debitAccountDescrPUR01;
  }
  public void setItemsAccountDescrSAL07(String itemsAccountDescrSAL07) {
    this.itemsAccountDescrSAL07 = itemsAccountDescrSAL07;
  }
  public String getCaseAccountCodeAcc02DOC19() {
    return caseAccountCodeAcc02DOC19;
  }
  public String getCaseAccountDescrDOC19() {
    return caseAccountDescrDOC19;
  }
  public void setCaseAccountCodeAcc02DOC19(String caseAccountCodeAcc02DOC19) {
    this.caseAccountCodeAcc02DOC19 = caseAccountCodeAcc02DOC19;
  }
  public void setCaseAccountDescrDOC19(String caseAccountDescrDOC19) {
    this.caseAccountDescrDOC19 = caseAccountDescrDOC19;
  }
  public String getBankAccountDescrDOC19() {
    return bankAccountDescrDOC19;
  }
  public void setBankAccountDescrDOC19(String bankAccountDescrDOC19) {
    this.bankAccountDescrDOC19 = bankAccountDescrDOC19;
  }
  public void setVatEndorseAccountDescrDOC19(String vatEndorseAccountDescrDOC19) {
    this.vatEndorseAccountDescrDOC19 = vatEndorseAccountDescrDOC19;
  }
  public String getVatEndorseAccountDescrDOC19() {
    return vatEndorseAccountDescrDOC19;
  }
  public String getBankAccountCodeAcc02DOC19() {
    return bankAccountCodeAcc02DOC19;
  }
  public void setBankAccountCodeAcc02DOC19(String bankAccountCodeAcc02DOC19) {
    this.bankAccountCodeAcc02DOC19 = bankAccountCodeAcc02DOC19;
  }
  public void setVatEndorseAccountCodeAcc02DOC19(String vatEndorseAccountCodeAcc02DOC19) {
    this.vatEndorseAccountCodeAcc02DOC19 = vatEndorseAccountCodeAcc02DOC19;
  }
  public String getVatEndorseAccountCodeAcc02DOC19() {
    return vatEndorseAccountCodeAcc02DOC19;
  }
  public void setWarehouseCodeWAR01(String warehouseCodeWAR01) {
    this.warehouseCodeWAR01 = warehouseCodeWAR01;
  }
  public String getWarehouseCodeWAR01() {
    return warehouseCodeWAR01;
  }
  public String getWarehouseDescriptionSYS10() {
    return warehouseDescriptionSYS10;
  }
  public void setWarehouseDescriptionSYS10(String warehouseDescriptionSYS10) {
    this.warehouseDescriptionSYS10 = warehouseDescriptionSYS10;
  }
  public String getRoundingCostsAccountCodeAcc02DOC19() {
    return roundingCostsAccountCodeAcc02DOC19;
  }
  public String getRoundingProceedsAccountCodeAcc02DOC19() {
    return roundingProceedsAccountCodeAcc02DOC19;
  }
  public void setRoundingCostsAccountCodeAcc02DOC19(String roundingCostsAccountCodeAcc02DOC19) {
    this.roundingCostsAccountCodeAcc02DOC19 = roundingCostsAccountCodeAcc02DOC19;
  }
  public void setRoundingProceedsAccountCodeAcc02DOC19(String roundingProceedsAccountCodeAcc02DOC19) {
    this.roundingProceedsAccountCodeAcc02DOC19 = roundingProceedsAccountCodeAcc02DOC19;
  }
  public String getRoundingProceedsDescrDOC19() {
    return roundingProceedsDescrDOC19;
  }
  public String getRoundingCostsDescrDOC19() {
    return roundingCostsDescrDOC19;
  }
  public void setRoundingCostsDescrDOC19(String roundingCostsDescrDOC19) {
    this.roundingCostsDescrDOC19 = roundingCostsDescrDOC19;
  }
  public void setRoundingProceedsDescrDOC19(String roundingProceedsDescrDOC19) {
    this.roundingProceedsDescrDOC19 = roundingProceedsDescrDOC19;
  }

}
