package org.jallinone.sales.documents.headercharges.java;

import org.openswing.swing.message.receive.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a charge in a sale document.</p>
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
public class SaleDocChargeVO extends ValueObjectImpl {

  private String companyCodeSys01DOC03;
  private String chargeCodeSal06DOC03;
  private java.math.BigDecimal valueSal06DOC03;
  private java.math.BigDecimal percSal06DOC03;
  private String chargeDescriptionDOC03;
  private java.math.BigDecimal valueDOC03;
  private java.math.BigDecimal percDOC03;
  private String docTypeDOC03;
  private java.math.BigDecimal docYearDOC03;
  private java.math.BigDecimal docNumberDOC03;
  private String vatCodeSal06DOC03;
  private String vatDescriptionDOC03;
  private java.math.BigDecimal vatValueDOC03;
  private java.math.BigDecimal vatDeductibleDOC03;
  private java.math.BigDecimal invoicedValueDOC03;
  private java.math.BigDecimal valueReg01DOC03;
  private java.math.BigDecimal taxableIncomeDOC03;

  public SaleDocChargeVO() {
  }


  public String getChargeDescriptionDOC03() {
    return chargeDescriptionDOC03;
  }
  public void setChargeDescriptionDOC03(String chargeDescriptionDOC03) {
    this.chargeDescriptionDOC03 = chargeDescriptionDOC03;
  }
  public java.math.BigDecimal getValueDOC03() {
    return valueDOC03;
  }
  public void setValueDOC03(java.math.BigDecimal valueDOC03) {
    this.valueDOC03 = valueDOC03;
  }
  public java.math.BigDecimal getPercDOC03() {
    return percDOC03;
  }
  public void setPercDOC03(java.math.BigDecimal percDOC03) {
    this.percDOC03 = percDOC03;
  }
  public String getDocTypeDOC03() {
    return docTypeDOC03;
  }
  public void setDocTypeDOC03(String docTypeDOC03) {
    this.docTypeDOC03 = docTypeDOC03;
  }
  public java.math.BigDecimal getDocYearDOC03() {
    return docYearDOC03;
  }
  public void setDocYearDOC03(java.math.BigDecimal docYearDOC03) {
    this.docYearDOC03 = docYearDOC03;
  }
  public java.math.BigDecimal getDocNumberDOC03() {
    return docNumberDOC03;
  }
  public void setDocNumberDOC03(java.math.BigDecimal docNumberDOC03) {
    this.docNumberDOC03 = docNumberDOC03;
  }

  public String getCompanyCodeSys01DOC03() {
    return companyCodeSys01DOC03;
  }
  public java.math.BigDecimal getValueSal06DOC03() {
    return valueSal06DOC03;
  }
  public java.math.BigDecimal getPercSal06DOC03() {
    return percSal06DOC03;
  }
  public void setPercSal06DOC03(java.math.BigDecimal percSal06DOC03) {
    this.percSal06DOC03 = percSal06DOC03;
  }
  public void setValueSal06DOC03(java.math.BigDecimal valueSal06DOC03) {
    this.valueSal06DOC03 = valueSal06DOC03;
  }
  public void setChargeCodeSal06DOC03(String chargeCodeSal06DOC03) {
    this.chargeCodeSal06DOC03 = chargeCodeSal06DOC03;
  }
  public void setCompanyCodeSys01DOC03(String companyCodeSys01DOC03) {
    this.companyCodeSys01DOC03 = companyCodeSys01DOC03;
  }
  public String getChargeCodeSal06DOC03() {
    return chargeCodeSal06DOC03;
  }
  public String getVatCodeSal06DOC03() {
    return vatCodeSal06DOC03;
  }
  public void setVatCodeSal06DOC03(String vatCodeSal06DOC03) {
    this.vatCodeSal06DOC03 = vatCodeSal06DOC03;
  }
  public String getVatDescriptionDOC03() {
    return vatDescriptionDOC03;
  }
  public void setVatDescriptionDOC03(String vatDescriptionDOC03) {
    this.vatDescriptionDOC03 = vatDescriptionDOC03;
  }
  public java.math.BigDecimal getVatDeductibleDOC03() {
    return vatDeductibleDOC03;
  }
  public java.math.BigDecimal getVatValueDOC03() {
    return vatValueDOC03;
  }
  public void setVatDeductibleDOC03(java.math.BigDecimal vatDeductibleDOC03) {
    this.vatDeductibleDOC03 = vatDeductibleDOC03;
  }
  public void setVatValueDOC03(java.math.BigDecimal vatValueDOC03) {
    this.vatValueDOC03 = vatValueDOC03;
  }
  public java.math.BigDecimal getInvoicedValueDOC03() {
    return invoicedValueDOC03;
  }
  public void setInvoicedValueDOC03(java.math.BigDecimal invoicedValueDOC03) {
    this.invoicedValueDOC03 = invoicedValueDOC03;
  }
  public java.math.BigDecimal getTaxableIncomeDOC03() {
    return taxableIncomeDOC03;
  }
  public java.math.BigDecimal getValueReg01DOC03() {
    return valueReg01DOC03;
  }
  public void setValueReg01DOC03(java.math.BigDecimal valueReg01DOC03) {
    this.valueReg01DOC03 = valueReg01DOC03;
  }
  public void setTaxableIncomeDOC03(java.math.BigDecimal taxableIncomeDOC03) {
    this.taxableIncomeDOC03 = taxableIncomeDOC03;
  }


}
