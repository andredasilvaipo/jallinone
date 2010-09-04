package org.jallinone.sales.documents.java;

import org.openswing.swing.message.receive.java.*;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store sale documents info (e.g. sale orders), for a grid.</p>
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
public class GridSaleDocVO extends ValueObjectImpl {

  private String companyCodeSys01DOC01;
  private String docTypeDOC01;
  private String docStateDOC01;
  private String pricelistCodeSal01DOC01;
  private String pricelistDescriptionDOC01;
  private String currencyCodeReg03DOC01;
  private String name_1REG04;
  private String name_2REG04;
  private java.math.BigDecimal docYearDOC01;
  private java.math.BigDecimal docNumberDOC01;
  private java.math.BigDecimal taxableIncomeDOC01;
  private java.math.BigDecimal totalVatDOC01;
  private java.math.BigDecimal totalDOC01;
  private java.sql.Date docDateDOC01;
  private String customerCodeSAL07;
  private java.math.BigDecimal decimalsREG03;
  private String currencySymbolREG03;
  private String thousandSymbolREG03;
  private String decimalSymbolREG03;
  private java.math.BigDecimal docSequenceDOC01;
  private String sectionalDOC01;
  private java.sql.Date deliveryDateDOC01;


  public GridSaleDocVO() {
  }


  public String getCompanyCodeSys01DOC01() {
    return companyCodeSys01DOC01;
  }
  public void setCompanyCodeSys01DOC01(String companyCodeSys01DOC01) {
    this.companyCodeSys01DOC01 = companyCodeSys01DOC01;
  }
  public String getDocTypeDOC01() {
    return docTypeDOC01;
  }
  public void setDocTypeDOC01(String docTypeDOC01) {
    this.docTypeDOC01 = docTypeDOC01;
  }
  public String getDocStateDOC01() {
    return docStateDOC01;
  }
  public void setDocStateDOC01(String docStateDOC01) {
    this.docStateDOC01 = docStateDOC01;
  }
  public String getPricelistCodeSal01DOC01() {
    return pricelistCodeSal01DOC01;
  }
  public void setPricelistCodeSal01DOC01(String pricelistCodeSal01DOC01) {
    this.pricelistCodeSal01DOC01 = pricelistCodeSal01DOC01;
  }
  public String getPricelistDescriptionDOC01() {
    return pricelistDescriptionDOC01;
  }
  public void setPricelistDescriptionDOC01(String pricelistDescriptionDOC01) {
    this.pricelistDescriptionDOC01 = pricelistDescriptionDOC01;
  }
  public String getCurrencyCodeReg03DOC01() {
    return currencyCodeReg03DOC01;
  }
  public void setCurrencyCodeReg03DOC01(String currencyCodeReg03DOC01) {
    this.currencyCodeReg03DOC01 = currencyCodeReg03DOC01;
  }
  public String getName_1REG04() {
    return name_1REG04;
  }
  public void setName_1REG04(String name_1REG04) {
    this.name_1REG04 = name_1REG04;
  }
  public java.math.BigDecimal getDocYearDOC01() {
    return docYearDOC01;
  }
  public void setDocYearDOC01(java.math.BigDecimal docYearDOC01) {
    this.docYearDOC01 = docYearDOC01;
  }
  public java.math.BigDecimal getDocNumberDOC01() {
    return docNumberDOC01;
  }
  public void setDocNumberDOC01(java.math.BigDecimal docNumberDOC01) {
    this.docNumberDOC01 = docNumberDOC01;
  }
  public java.math.BigDecimal getTaxableIncomeDOC01() {
    return taxableIncomeDOC01;
  }
  public void setTaxableIncomeDOC01(java.math.BigDecimal taxableIncomeDOC01) {
    this.taxableIncomeDOC01 = taxableIncomeDOC01;
  }
  public java.math.BigDecimal getTotalVatDOC01() {
    return totalVatDOC01;
  }
  public void setTotalVatDOC01(java.math.BigDecimal totalVatDOC01) {
    this.totalVatDOC01 = totalVatDOC01;
  }
  public java.math.BigDecimal getTotalDOC01() {
    return totalDOC01;
  }
  public void setTotalDOC01(java.math.BigDecimal totalDOC01) {
    this.totalDOC01 = totalDOC01;
  }
  public java.sql.Date getDocDateDOC01() {
    return docDateDOC01;
  }
  public void setDocDateDOC01(java.sql.Date docDateDOC01) {
    this.docDateDOC01 = docDateDOC01;
  }
  public String getCustomerCodeSAL07() {
    return customerCodeSAL07;
  }
  public void setCustomerCodeSAL07(String customerCodeSAL07) {
    this.customerCodeSAL07 = customerCodeSAL07;
  }
  public String getName_2REG04() {
    return name_2REG04;
  }
  public void setName_2REG04(String name_2REG04) {
    this.name_2REG04 = name_2REG04;
  }
  public java.math.BigDecimal getDecimalsREG03() {
    return decimalsREG03;
  }
  public void setDecimalsREG03(java.math.BigDecimal decimalsREG03) {
    this.decimalsREG03 = decimalsREG03;
  }
  public String getCurrencySymbolREG03() {
    return currencySymbolREG03;
  }
  public String getDecimalSymbolREG03() {
    return decimalSymbolREG03;
  }
  public String getThousandSymbolREG03() {
    return thousandSymbolREG03;
  }
  public void setThousandSymbolREG03(String thousandSymbolREG03) {
    this.thousandSymbolREG03 = thousandSymbolREG03;
  }
  public void setDecimalSymbolREG03(String decimalSymbolREG03) {
    this.decimalSymbolREG03 = decimalSymbolREG03;
  }
  public void setCurrencySymbolREG03(String currencySymbolREG03) {
    this.currencySymbolREG03 = currencySymbolREG03;
  }
  public java.math.BigDecimal getDocSequenceDOC01() {
    return docSequenceDOC01;
  }
  public void setDocSequenceDOC01(java.math.BigDecimal docSequenceDOC01) {
    this.docSequenceDOC01 = docSequenceDOC01;
  }
  public String getSectionalDOC01() {
    return sectionalDOC01;
  }
  public void setSectionalDOC01(String sectionalDOC01) {
    this.sectionalDOC01 = sectionalDOC01;
  }
  public java.sql.Date getDeliveryDateDOC01() {
    return deliveryDateDOC01;
  }
  public void setDeliveryDateDOC01(java.sql.Date deliveryDateDOC01) {
    this.deliveryDateDOC01 = deliveryDateDOC01;
  }

}
