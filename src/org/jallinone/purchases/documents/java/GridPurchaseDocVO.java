package org.jallinone.purchases.documents.java;

import org.openswing.swing.message.receive.java.*;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store purchase orders info, for a grid.</p>
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
public class GridPurchaseDocVO extends ValueObjectImpl {

  private String companyCodeSys01DOC06;
  private String docTypeDOC06;
  private String docStateDOC06;
  private String pricelistCodePur03DOC06;
  private String pricelistDescriptionDOC06;
  private String currencyCodeReg03DOC06;
  private String name_1REG04;
  private String name_2REG04;
  private java.math.BigDecimal docYearDOC06;
  private java.math.BigDecimal docNumberDOC06;
  private java.math.BigDecimal taxableIncomeDOC06;
  private java.math.BigDecimal totalVatDOC06;
  private java.math.BigDecimal totalDOC06;
  private java.sql.Date docDateDOC06;
  private String supplierCodePUR01;
  private java.math.BigDecimal decimalsREG03;
  private String currencySymbolREG03;
  private String thousandSymbolREG03;
  private String decimalSymbolREG03;
  private java.math.BigDecimal docSequenceDOC06;


  public GridPurchaseDocVO() {
  }


  public String getCompanyCodeSys01DOC06() {
    return companyCodeSys01DOC06;
  }
  public void setCompanyCodeSys01DOC06(String companyCodeSys01DOC06) {
    this.companyCodeSys01DOC06 = companyCodeSys01DOC06;
  }
  public String getDocTypeDOC06() {
    return docTypeDOC06;
  }
  public void setDocTypeDOC06(String docTypeDOC06) {
    this.docTypeDOC06 = docTypeDOC06;
  }
  public String getDocStateDOC06() {
    return docStateDOC06;
  }
  public void setDocStateDOC06(String docStateDOC06) {
    this.docStateDOC06 = docStateDOC06;
  }
  public String getPricelistCodePur03DOC06() {
    return pricelistCodePur03DOC06;
  }
  public void setPricelistCodePur03DOC06(String pricelistCodePur03DOC06) {
    this.pricelistCodePur03DOC06 = pricelistCodePur03DOC06;
  }
  public String getPricelistDescriptionDOC06() {
    return pricelistDescriptionDOC06;
  }
  public void setPricelistDescriptionDOC06(String pricelistDescriptionDOC06) {
    this.pricelistDescriptionDOC06 = pricelistDescriptionDOC06;
  }
  public String getCurrencyCodeReg03DOC06() {
    return currencyCodeReg03DOC06;
  }
  public void setCurrencyCodeReg03DOC06(String currencyCodeReg03DOC06) {
    this.currencyCodeReg03DOC06 = currencyCodeReg03DOC06;
  }
  public String getName_1REG04() {
    return name_1REG04;
  }
  public void setName_1REG04(String name_1REG04) {
    this.name_1REG04 = name_1REG04;
  }
  public java.math.BigDecimal getDocYearDOC06() {
    return docYearDOC06;
  }
  public void setDocYearDOC06(java.math.BigDecimal docYearDOC06) {
    this.docYearDOC06 = docYearDOC06;
  }
  public java.math.BigDecimal getDocNumberDOC06() {
    return docNumberDOC06;
  }
  public void setDocNumberDOC06(java.math.BigDecimal docNumberDOC06) {
    this.docNumberDOC06 = docNumberDOC06;
  }
  public java.math.BigDecimal getTaxableIncomeDOC06() {
    return taxableIncomeDOC06;
  }
  public void setTaxableIncomeDOC06(java.math.BigDecimal taxableIncomeDOC06) {
    this.taxableIncomeDOC06 = taxableIncomeDOC06;
  }
  public java.math.BigDecimal getTotalVatDOC06() {
    return totalVatDOC06;
  }
  public void setTotalVatDOC06(java.math.BigDecimal totalVatDOC06) {
    this.totalVatDOC06 = totalVatDOC06;
  }
  public java.math.BigDecimal getTotalDOC06() {
    return totalDOC06;
  }
  public void setTotalDOC06(java.math.BigDecimal totalDOC06) {
    this.totalDOC06 = totalDOC06;
  }
  public java.sql.Date getDocDateDOC06() {
    return docDateDOC06;
  }
  public void setDocDateDOC06(java.sql.Date docDateDOC06) {
    this.docDateDOC06 = docDateDOC06;
  }
  public String getSupplierCodePUR01() {
    return supplierCodePUR01;
  }
  public void setSupplierCodePUR01(String supplierCodePUR01) {
    this.supplierCodePUR01 = supplierCodePUR01;
  }
  public String getCurrencySymbolREG03() {
    return currencySymbolREG03;
  }
  public java.math.BigDecimal getDecimalsREG03() {
    return decimalsREG03;
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
  public void setDecimalsREG03(java.math.BigDecimal decimalsREG03) {
    this.decimalsREG03 = decimalsREG03;
  }
  public void setCurrencySymbolREG03(String currencySymbolREG03) {
    this.currencySymbolREG03 = currencySymbolREG03;
  }
  public java.math.BigDecimal getDocSequenceDOC06() {
    return docSequenceDOC06;
  }
  public void setDocSequenceDOC06(java.math.BigDecimal docSequenceDOC06) {
    this.docSequenceDOC06 = docSequenceDOC06;
  }
  public String getName_2REG04() {
    return name_2REG04;
  }
  public void setName_2REG04(String name_2REG04) {
    this.name_2REG04 = name_2REG04;
  }

}
