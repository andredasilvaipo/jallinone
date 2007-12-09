package org.jallinone.sales.documents.java;

import org.openswing.swing.message.receive.java.*;
import org.jallinone.system.customizations.java.BaseValueObject;
import java.util.ArrayList;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store sale order row info, for the order row detail.</p>
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
public class DetailSaleDocRowVO extends BaseValueObject {

  private String companyCodeSys01DOC02;
  private String docTypeDOC02;
  private java.math.BigDecimal docYearDOC02;
  private java.math.BigDecimal docNumberDOC02;
  private String itemCodeItm01DOC02;
  private String vatCodeItm01DOC02;
  private String vatDescriptionDOC02;
  private java.math.BigDecimal valueSal02DOC02;
  private java.math.BigDecimal valueDOC02;
  private java.math.BigDecimal qtyDOC02;
  private java.sql.Date startDateSal02DOC02;
  private java.sql.Date endDateSal02DOC02;
  private String descriptionSYS10;
  private java.math.BigDecimal vatValueDOC02;
  private java.math.BigDecimal valueReg01DOC02;
  private java.math.BigDecimal deductibleReg01DOC02;
  private java.math.BigDecimal taxableIncomeDOC02;
  private java.math.BigDecimal progressiveHie02DOC02;
  private java.math.BigDecimal progressiveHie01ITM01;
  private java.math.BigDecimal rowNumberDOC02;
  private java.sql.Date deliveryDateDOC02;
  private java.math.BigDecimal outQtyDOC02;

  private String minSellingQtyUmCodeReg02DOC02;
  private java.math.BigDecimal decimalsReg02DOC02;
  private java.math.BigDecimal totalDiscountDOC02;
  private java.math.BigDecimal minSellingQtyItm01DOC02;

  private String currencyCodeReg03DOC01;

  private ArrayList serialNumbers = new ArrayList();
  private ArrayList barCodes = new ArrayList();

  private java.math.BigDecimal progressiveHie01DOC02;
  private String positionDescriptionSYS10;
  private java.math.BigDecimal discountValueDOC02;
  private java.math.BigDecimal discountPercDOC02;
  private java.math.BigDecimal invoiceQtyDOC02;


  public DetailSaleDocRowVO() {
  }


  public String getCompanyCodeSys01DOC02() {
    return companyCodeSys01DOC02;
  }
  public void setCompanyCodeSys01DOC02(String companyCodeSys01DOC02) {
    this.companyCodeSys01DOC02 = companyCodeSys01DOC02;
  }
  public String getDocTypeDOC02() {
    return docTypeDOC02;
  }
  public void setDocTypeDOC02(String docTypeDOC02) {
    this.docTypeDOC02 = docTypeDOC02;
  }

  public java.math.BigDecimal getDocYearDOC02() {
    return docYearDOC02;
  }
  public void setDocYearDOC02(java.math.BigDecimal docYearDOC02) {
    this.docYearDOC02 = docYearDOC02;
  }
  public java.math.BigDecimal getDocNumberDOC02() {
    return docNumberDOC02;
  }
  public void setDocNumberDOC02(java.math.BigDecimal docNumberDOC02) {
    this.docNumberDOC02 = docNumberDOC02;
  }
  public String getItemCodeItm01DOC02() {
    return itemCodeItm01DOC02;
  }
  public void setItemCodeItm01DOC02(String itemCodeItm01DOC02) {
    this.itemCodeItm01DOC02 = itemCodeItm01DOC02;
  }
  public String getVatCodeItm01DOC02() {
    return vatCodeItm01DOC02;
  }
  public void setVatCodeItm01DOC02(String vatCodeItm01DOC02) {
    this.vatCodeItm01DOC02 = vatCodeItm01DOC02;
  }
  public String getVatDescriptionDOC02() {
    return vatDescriptionDOC02;
  }
  public void setVatDescriptionDOC02(String vatDescriptionDOC02) {
    this.vatDescriptionDOC02 = vatDescriptionDOC02;
  }
  public java.math.BigDecimal getValueSal02DOC02() {
    return valueSal02DOC02;
  }
  public void setValueSal02DOC02(java.math.BigDecimal valueSal02DOC02) {
    this.valueSal02DOC02 = valueSal02DOC02;
  }
  public java.math.BigDecimal getValueDOC02() {
    return valueDOC02;
  }
  public void setValueDOC02(java.math.BigDecimal valueDOC02) {
    this.valueDOC02 = valueDOC02;
  }
  public java.math.BigDecimal getQtyDOC02() {
    return qtyDOC02;
  }
  public void setQtyDOC02(java.math.BigDecimal qtyDOC02) {
    this.qtyDOC02 = qtyDOC02;
  }
  public java.sql.Date getStartDateSal02DOC02() {
    return startDateSal02DOC02;
  }
  public void setStartDateSal02DOC02(java.sql.Date startDateSal02DOC02) {
    this.startDateSal02DOC02 = startDateSal02DOC02;
  }
  public java.sql.Date getEndDateSal02DOC02() {
    return endDateSal02DOC02;
  }
  public void setEndDateSal02DOC02(java.sql.Date endDateSal02DOC02) {
    this.endDateSal02DOC02 = endDateSal02DOC02;
  }
  public String getMinSellingQtyUmCodeReg02DOC02() {
    return minSellingQtyUmCodeReg02DOC02;
  }
  public void setMinSellingQtyUmCodeReg02DOC02(String minSellingQtyUmCodeReg02DOC02) {
    this.minSellingQtyUmCodeReg02DOC02 = minSellingQtyUmCodeReg02DOC02;
  }
  public java.math.BigDecimal getDecimalsReg02DOC02() {
    return decimalsReg02DOC02;
  }
  public void setDecimalsReg02DOC02(java.math.BigDecimal decimalsReg02DOC02) {
    this.decimalsReg02DOC02 = decimalsReg02DOC02;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public java.math.BigDecimal getVatValueDOC02() {
    return vatValueDOC02;
  }
  public void setVatValueDOC02(java.math.BigDecimal vatValueDOC02) {
    this.vatValueDOC02 = vatValueDOC02;
  }
  public java.math.BigDecimal getValueReg01DOC02() {
    return valueReg01DOC02;
  }
  public void setValueReg01DOC02(java.math.BigDecimal valueReg01DOC02) {
    this.valueReg01DOC02 = valueReg01DOC02;
  }
  public java.math.BigDecimal getDeductibleReg01DOC02() {
    return deductibleReg01DOC02;
  }
  public void setDeductibleReg01DOC02(java.math.BigDecimal deductibleReg01DOC02) {
    this.deductibleReg01DOC02 = deductibleReg01DOC02;
  }
  public java.math.BigDecimal getTaxableIncomeDOC02() {
    return taxableIncomeDOC02;
  }
  public void setTaxableIncomeDOC02(java.math.BigDecimal taxableIncomeDOC02) {
    this.taxableIncomeDOC02 = taxableIncomeDOC02;
  }
  public java.math.BigDecimal getProgressiveHie02DOC02() {
    return progressiveHie02DOC02;
  }
  public void setProgressiveHie02DOC02(java.math.BigDecimal progressiveHie02DOC02) {
    this.progressiveHie02DOC02 = progressiveHie02DOC02;
  }
  public java.math.BigDecimal getRowNumberDOC02() {
    return rowNumberDOC02;
  }
  public void setRowNumberDOC02(java.math.BigDecimal rowNumberDOC02) {
    this.rowNumberDOC02 = rowNumberDOC02;
  }
  public java.sql.Date getDeliveryDateDOC02() {
    return deliveryDateDOC02;
  }
  public void setDeliveryDateDOC02(java.sql.Date deliveryDateDOC02) {
    this.deliveryDateDOC02 = deliveryDateDOC02;
  }
  public java.math.BigDecimal getOutQtyDOC02() {
    return outQtyDOC02;
  }
  public void setOutQtyDOC02(java.math.BigDecimal outQtyDOC02) {
    this.outQtyDOC02 = outQtyDOC02;
  }
  public java.math.BigDecimal getTotalDiscountDOC02() {
    return totalDiscountDOC02;
  }
  public void setTotalDiscountDOC02(java.math.BigDecimal totalDiscountDOC02) {
    this.totalDiscountDOC02 = totalDiscountDOC02;
  }
  public java.math.BigDecimal getMinSellingQtyItm01DOC02() {
    return minSellingQtyItm01DOC02;
  }
  public void setMinSellingQtyItm01DOC02(java.math.BigDecimal minSellingQtyItm01DOC02) {
    this.minSellingQtyItm01DOC02 = minSellingQtyItm01DOC02;
  }
  public java.math.BigDecimal getProgressiveHie01ITM01() {
    return progressiveHie01ITM01;
  }
  public void setProgressiveHie01ITM01(java.math.BigDecimal progressiveHie01ITM01) {
    this.progressiveHie01ITM01 = progressiveHie01ITM01;
  }
  public String getCurrencyCodeReg03DOC01() {
    return currencyCodeReg03DOC01;
  }
  public void setCurrencyCodeReg03DOC01(String currencyCodeReg03DOC01) {
    this.currencyCodeReg03DOC01 = currencyCodeReg03DOC01;
  }
  public ArrayList getBarCodes() {
    return barCodes;
  }
  public void setBarCodes(ArrayList barCodes) {
    this.barCodes = barCodes;
  }
  public void setSerialNumbers(ArrayList serialNumbers) {
    this.serialNumbers = serialNumbers;
  }
  public ArrayList getSerialNumbers() {
    return serialNumbers;
  }
  public java.math.BigDecimal getProgressiveHie01DOC02() {
    return progressiveHie01DOC02;
  }
  public void setProgressiveHie01DOC02(java.math.BigDecimal progressiveHie01DOC02) {
    this.progressiveHie01DOC02 = progressiveHie01DOC02;
  }
  public String getPositionDescriptionSYS10() {
    return positionDescriptionSYS10;
  }
  public void setPositionDescriptionSYS10(String positionDescriptionSYS10) {
    this.positionDescriptionSYS10 = positionDescriptionSYS10;
  }
  public java.math.BigDecimal getDiscountPercDOC02() {
    return discountPercDOC02;
  }
  public java.math.BigDecimal getDiscountValueDOC02() {
    return discountValueDOC02;
  }
  public void setDiscountPercDOC02(java.math.BigDecimal discountPercDOC02) {
    this.discountPercDOC02 = discountPercDOC02;
  }
  public void setDiscountValueDOC02(java.math.BigDecimal discountValueDOC02) {
    this.discountValueDOC02 = discountValueDOC02;
  }
  public java.math.BigDecimal getInvoiceQtyDOC02() {
    return invoiceQtyDOC02;
  }
  public void setInvoiceQtyDOC02(java.math.BigDecimal invoiceQtyDOC02) {
    this.invoiceQtyDOC02 = invoiceQtyDOC02;
  }


}
