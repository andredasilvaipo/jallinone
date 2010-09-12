package org.jallinone.sales.documents.activities.java;

import org.openswing.swing.message.receive.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a sale doc activity in a sale document.</p>
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
public class SaleDocActivityVO extends ValueObjectImpl {

  private String companyCodeSys01DOC13;
  private String activityCodeSal09DOC13;
  private java.math.BigDecimal valueSal09DOC13;
  private String activityDescriptionDOC13;
  private java.math.BigDecimal valueDOC13;
  private String docTypeDOC13;
  private java.math.BigDecimal docYearDOC13;
  private java.math.BigDecimal docNumberDOC13;
  private String vatCodeSal09DOC13;
  private String vatDescriptionDOC13;
  private java.math.BigDecimal vatValueDOC13;
  private java.math.BigDecimal vatDeductibleDOC13;
  private java.math.BigDecimal durationDOC13;
  private String currencyCodeReg03DOC13;
  private java.math.BigDecimal progressiveSch06DOC13;
  private String descriptionSCH06;
  private String currencySymbolREG03;
  private java.math.BigDecimal invoicedValueDOC13;
  private java.math.BigDecimal decimalsREG02;
  private String umCodeReg02DOC13;
  private java.math.BigDecimal valueReg01DOC13;
  private java.math.BigDecimal taxableIncomeDOC13;


  public SaleDocActivityVO() {
  }


  public String getActivityDescriptionDOC13() {
    return activityDescriptionDOC13;
  }
  public void setActivityDescriptionDOC13(String activityDescriptionDOC13) {
    this.activityDescriptionDOC13 = activityDescriptionDOC13;
  }
  public java.math.BigDecimal getValueDOC13() {
    return valueDOC13;
  }
  public void setValueDOC13(java.math.BigDecimal valueDOC13) {
    this.valueDOC13 = valueDOC13;
  }
  public String getDocTypeDOC13() {
    return docTypeDOC13;
  }
  public void setDocTypeDOC13(String docTypeDOC13) {
    this.docTypeDOC13 = docTypeDOC13;
  }
  public java.math.BigDecimal getDocYearDOC13() {
    return docYearDOC13;
  }
  public void setDocYearDOC13(java.math.BigDecimal docYearDOC13) {
    this.docYearDOC13 = docYearDOC13;
  }
  public java.math.BigDecimal getDocNumberDOC13() {
    return docNumberDOC13;
  }
  public void setDocNumberDOC13(java.math.BigDecimal docNumberDOC13) {
    this.docNumberDOC13 = docNumberDOC13;
  }

  public String getCompanyCodeSys01DOC13() {
    return companyCodeSys01DOC13;
  }
  public java.math.BigDecimal getValueSal09DOC13() {
    return valueSal09DOC13;
  }
  public void setValueSal09DOC13(java.math.BigDecimal valueSal09DOC13) {
    this.valueSal09DOC13 = valueSal09DOC13;
  }
  public void setActivityCodeSal09DOC13(String activityCodeSal09DOC13) {
    this.activityCodeSal09DOC13 = activityCodeSal09DOC13;
  }
  public void setCompanyCodeSys01DOC13(String companyCodeSys01DOC13) {
    this.companyCodeSys01DOC13 = companyCodeSys01DOC13;
  }
  public String getActivityCodeSal09DOC13() {
    return activityCodeSal09DOC13;
  }
  public String getVatCodeSal09DOC13() {
    return vatCodeSal09DOC13;
  }
  public void setVatCodeSal09DOC13(String vatCodeSal09DOC13) {
    this.vatCodeSal09DOC13 = vatCodeSal09DOC13;
  }
  public String getVatDescriptionDOC13() {
    return vatDescriptionDOC13;
  }
  public void setVatDescriptionDOC13(String vatDescriptionDOC13) {
    this.vatDescriptionDOC13 = vatDescriptionDOC13;
  }
  public java.math.BigDecimal getVatDeductibleDOC13() {
    return vatDeductibleDOC13;
  }
  public java.math.BigDecimal getVatValueDOC13() {
    return vatValueDOC13;
  }
  public void setVatDeductibleDOC13(java.math.BigDecimal vatDeductibleDOC13) {
    this.vatDeductibleDOC13 = vatDeductibleDOC13;
  }
  public void setVatValueDOC13(java.math.BigDecimal vatValueDOC13) {
    this.vatValueDOC13 = vatValueDOC13;
  }
  public java.math.BigDecimal getDurationDOC13() {
    return durationDOC13;
  }
  public void setDurationDOC13(java.math.BigDecimal durationDOC13) {
    this.durationDOC13 = durationDOC13;
  }
  public String getCurrencyCodeReg03DOC13() {
    return currencyCodeReg03DOC13;
  }
  public void setCurrencyCodeReg03DOC13(String currencyCodeReg03DOC13) {
    this.currencyCodeReg03DOC13 = currencyCodeReg03DOC13;
  }
  public java.math.BigDecimal getProgressiveSch06DOC13() {
    return progressiveSch06DOC13;
  }
  public void setProgressiveSch06DOC13(java.math.BigDecimal progressiveSch06DOC13) {
    this.progressiveSch06DOC13 = progressiveSch06DOC13;
  }
  public String getDescriptionSCH06() {
    return descriptionSCH06;
  }
  public void setDescriptionSCH06(String descriptionSCH06) {
    this.descriptionSCH06 = descriptionSCH06;
  }
  public String getCurrencySymbolREG03() {
    return currencySymbolREG03;
  }
  public void setCurrencySymbolREG03(String currencySymbolREG03) {
    this.currencySymbolREG03 = currencySymbolREG03;
  }
  public java.math.BigDecimal getInvoicedValueDOC13() {
    return invoicedValueDOC13;
  }
  public void setInvoicedValueDOC13(java.math.BigDecimal invoicedValueDOC13) {
    this.invoicedValueDOC13 = invoicedValueDOC13;
  }
  public java.math.BigDecimal getDecimalsREG02() {
    return decimalsREG02;
  }
  public void setDecimalsREG02(java.math.BigDecimal decimalsREG02) {
    this.decimalsREG02 = decimalsREG02;
  }
  public String getUmCodeReg02DOC13() {
    return umCodeReg02DOC13;
  }
  public void setUmCodeReg02DOC13(String umCodeReg02DOC13) {
    this.umCodeReg02DOC13 = umCodeReg02DOC13;
  }
  public java.math.BigDecimal getTaxableIncomeDOC13() {
    return taxableIncomeDOC13;
  }
  public java.math.BigDecimal getValueReg01DOC13() {
    return valueReg01DOC13;
  }
  public void setValueReg01DOC13(java.math.BigDecimal valueReg01DOC13) {
    this.valueReg01DOC13 = valueReg01DOC13;
  }
  public void setTaxableIncomeDOC13(java.math.BigDecimal taxableIncomeDOC13) {
    this.taxableIncomeDOC13 = taxableIncomeDOC13;
  }



}
