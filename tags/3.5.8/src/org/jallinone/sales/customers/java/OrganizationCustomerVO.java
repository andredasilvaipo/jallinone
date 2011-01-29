package org.jallinone.sales.customers.java;

import org.jallinone.subjects.java.OrganizationVO;
import org.jallinone.subjects.java.Subject;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a private customer.</p>
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
public class OrganizationCustomerVO extends OrganizationVO implements Subject {

  private String customerCodeSAL07;
  private String paymentCodeReg10SAL07;
  private String pricelistCodeSal01SAL07;
  private String bankCodeReg12SAL07;
  private String enabledSAL07;
  private String paymentDescriptionSYS10;
  private String descriptionREG12;
  private String pricelistDescriptionSYS10;
  private java.math.BigDecimal agentProgressiveReg04SAL07;
  private java.math.BigDecimal trustAmountSAL07;
  private String agentCodeSAL10;
  private String agentName_1REG04;
  private String agentName_2REG04;
  private String vatCodeReg01SAL07;
  private String vatDescriptionSYS10;
  private java.math.BigDecimal vatValueREG01;
  private java.math.BigDecimal vatDeductibleREG01;

  private String creditAccountCodeAcc02SAL07;
  private String itemsAccountCodeAcc02SAL07;
  private String activitiesAccountCodeAcc02SAL07;
  private String chargesAccountCodeAcc02SAL07;
  private String creditAccountDescrSAL07;
  private String itemsAccountDescrSAL07;
  private String activitiesAccountDescrSAL07;
  private String chargesAccountDescrSAL07;
  private String currencyCodeReg03SAL01;



  public OrganizationCustomerVO() {
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
  public void setPaymentCodeReg10SAL07(String paymentCodeReg10SAL07) {
    this.paymentCodeReg10SAL07 = paymentCodeReg10SAL07;
  }
  public String getPricelistCodeSal01SAL07() {
    return pricelistCodeSal01SAL07;
  }
  public void setPricelistCodeSal01SAL07(String pricelistCodeSal01SAL07) {
    this.pricelistCodeSal01SAL07 = pricelistCodeSal01SAL07;
  }
  public String getBankCodeReg12SAL07() {
    return bankCodeReg12SAL07;
  }
  public void setBankCodeReg12SAL07(String bankCodeReg12SAL07) {
    this.bankCodeReg12SAL07 = bankCodeReg12SAL07;
  }
  public String getEnabledSAL07() {
    return enabledSAL07;
  }
  public void setEnabledSAL07(String enabledSAL07) {
    this.enabledSAL07 = enabledSAL07;
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
  public String getPricelistDescriptionSYS10() {
    return pricelistDescriptionSYS10;
  }
  public void setPricelistDescriptionSYS10(String pricelistDescriptionSYS10) {
    this.pricelistDescriptionSYS10 = pricelistDescriptionSYS10;
  }
  public java.math.BigDecimal getAgentProgressiveReg04SAL07() {
    return agentProgressiveReg04SAL07;
  }
  public void setAgentProgressiveReg04SAL07(java.math.BigDecimal agentProgressiveReg04SAL07) {
    this.agentProgressiveReg04SAL07 = agentProgressiveReg04SAL07;
  }
  public java.math.BigDecimal getTrustAmountSAL07() {
    return trustAmountSAL07;
  }
  public void setTrustAmountSAL07(java.math.BigDecimal trustAmountSAL07) {
    this.trustAmountSAL07 = trustAmountSAL07;
  }
  public String getAgentCodeSAL10() {
    return agentCodeSAL10;
  }
  public void setAgentCodeSAL10(String agentCodeSAL10) {
    this.agentCodeSAL10 = agentCodeSAL10;
  }
  public String getAgentName_1REG04() {
    return agentName_1REG04;
  }
  public void setAgentName_1REG04(String agentName_1REG04) {
    this.agentName_1REG04 = agentName_1REG04;
  }
  public String getAgentName_2REG04() {
    return agentName_2REG04;
  }
  public void setAgentName_2REG04(String agentName_2REG04) {
    this.agentName_2REG04 = agentName_2REG04;
  }
  public String getVatCodeReg01SAL07() {
    return vatCodeReg01SAL07;
  }
  public void setVatCodeReg01SAL07(String vatCodeReg01SAL07) {
    this.vatCodeReg01SAL07 = vatCodeReg01SAL07;
  }
  public String getVatDescriptionSYS10() {
    return vatDescriptionSYS10;
  }
  public void setVatDescriptionSYS10(String vatDescriptionSYS10) {
    this.vatDescriptionSYS10 = vatDescriptionSYS10;
  }
  public java.math.BigDecimal getVatValueREG01() {
    return vatValueREG01;
  }
  public void setVatValueREG01(java.math.BigDecimal vatValueREG01) {
    this.vatValueREG01 = vatValueREG01;
  }
  public java.math.BigDecimal getVatDeductibleREG01() {
    return vatDeductibleREG01;
  }
  public void setVatDeductibleREG01(java.math.BigDecimal vatDeductibleREG01) {
    this.vatDeductibleREG01 = vatDeductibleREG01;
  }
  public String getActivitiesAccountCodeAcc02SAL07() {
    return activitiesAccountCodeAcc02SAL07;
  }
  public String getActivitiesAccountDescrSAL07() {
    return activitiesAccountDescrSAL07;
  }
  public String getChargesAccountCodeAcc02SAL07() {
    return chargesAccountCodeAcc02SAL07;
  }
  public String getChargesAccountDescrSAL07() {
    return chargesAccountDescrSAL07;
  }
  public String getCreditAccountCodeAcc02SAL07() {
    return creditAccountCodeAcc02SAL07;
  }
  public String getCreditAccountDescrSAL07() {
    return creditAccountDescrSAL07;
  }
  public String getItemsAccountDescrSAL07() {
    return itemsAccountDescrSAL07;
  }
  public void setItemsAccountDescrSAL07(String itemsAccountDescrSAL07) {
    this.itemsAccountDescrSAL07 = itemsAccountDescrSAL07;
  }
  public void setItemsAccountCodeAcc02SAL07(String itemsAccountCodeAcc02SAL07) {
    this.itemsAccountCodeAcc02SAL07 = itemsAccountCodeAcc02SAL07;
  }
  public void setCreditAccountDescrSAL07(String creditAccountDescrSAL07) {
    this.creditAccountDescrSAL07 = creditAccountDescrSAL07;
  }
  public void setCreditAccountCodeAcc02SAL07(String creditAccountCodeAcc02SAL07) {
    this.creditAccountCodeAcc02SAL07 = creditAccountCodeAcc02SAL07;
  }
  public void setChargesAccountDescrSAL07(String chargesAccountDescrSAL07) {
    this.chargesAccountDescrSAL07 = chargesAccountDescrSAL07;
  }
  public void setChargesAccountCodeAcc02SAL07(String chargesAccountCodeAcc02SAL07) {
    this.chargesAccountCodeAcc02SAL07 = chargesAccountCodeAcc02SAL07;
  }
  public void setActivitiesAccountDescrSAL07(String activitiesAccountDescrSAL07) {
    this.activitiesAccountDescrSAL07 = activitiesAccountDescrSAL07;
  }
  public void setActivitiesAccountCodeAcc02SAL07(String activitiesAccountCodeAcc02SAL07) {
    this.activitiesAccountCodeAcc02SAL07 = activitiesAccountCodeAcc02SAL07;
  }
  public String getItemsAccountCodeAcc02SAL07() {
    return itemsAccountCodeAcc02SAL07;
  }
  public String getCurrencyCodeReg03SAL01() {
    return currencyCodeReg03SAL01;
  }
  public void setCurrencyCodeReg03SAL01(String currencyCodeReg03SAL01) {
    this.currencyCodeReg03SAL01 = currencyCodeReg03SAL01;
  }


}
