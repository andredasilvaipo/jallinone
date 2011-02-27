package org.jallinone.purchases.documents.java;

import org.jallinone.commons.java.*;
import org.jallinone.system.customizations.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store purchase order row info, for the order row detail.</p>
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
public class DetailPurchaseDocRowVO extends BaseValueObject {

  private String companyCodeSys01DOC07;
  private String docTypeDOC07;
  private java.math.BigDecimal docYearDOC07;
  private java.math.BigDecimal docNumberDOC07;
  private String itemCodeItm01DOC07;
  private String supplierItemCodePur02DOC07;
  private String vatCodeItm01DOC07;
  private String vatDescriptionDOC07;
  private java.math.BigDecimal valuePur04DOC07;
  private java.math.BigDecimal valueDOC07;
  private java.math.BigDecimal qtyDOC07;
  private java.math.BigDecimal discountValueDOC07;
  private java.math.BigDecimal discountPercDOC07;
  private java.sql.Date startDatePur04DOC07;
  private java.sql.Date endDatePur04DOC07;
  private String umCodePur02DOC07;
  private java.math.BigDecimal decimalsReg02DOC07;
  private java.math.BigDecimal minPurchaseQtyPur02DOC07;
  private java.math.BigDecimal multipleQtyPur02DOC07;
  private String descriptionSYS10;
  private java.math.BigDecimal vatValueDOC07;
  private java.math.BigDecimal valueReg01DOC07;
  private java.math.BigDecimal deductibleReg01DOC07;
  private java.math.BigDecimal taxableIncomeDOC07;
  private java.math.BigDecimal progressiveHie02DOC07;
  private java.math.BigDecimal rowNumberDOC07;
  private java.sql.Date deliveryDateDOC07;
  private java.math.BigDecimal inQtyDOC07;
  private java.math.BigDecimal orderQtyDOC07;
  private java.math.BigDecimal invoiceQtyDOC07;
  private String currencyCodeReg03DOC06;

  private String variantTypeItm06DOC07;
  private String variantCodeItm11DOC07;
  private String variantTypeItm07DOC07;
  private String variantCodeItm12DOC07;
  private String variantTypeItm08DOC07;
  private String variantCodeItm13DOC07;
  private String variantTypeItm09DOC07;
  private String variantCodeItm14DOC07;
  private String variantTypeItm10DOC07;
  private String variantCodeItm15DOC07;

	private Boolean noWarehouseMovITM01;


  public DetailPurchaseDocRowVO() {
  }


  public String getCompanyCodeSys01DOC07() {
    return companyCodeSys01DOC07;
  }
  public void setCompanyCodeSys01DOC07(String companyCodeSys01DOC07) {
    this.companyCodeSys01DOC07 = companyCodeSys01DOC07;
  }
  public String getDocTypeDOC07() {
    return docTypeDOC07;
  }
  public void setDocTypeDOC07(String docTypeDOC07) {
    this.docTypeDOC07 = docTypeDOC07;
  }

  public java.math.BigDecimal getDocYearDOC07() {
    return docYearDOC07;
  }
  public void setDocYearDOC07(java.math.BigDecimal docYearDOC07) {
    this.docYearDOC07 = docYearDOC07;
  }
  public java.math.BigDecimal getDocNumberDOC07() {
    return docNumberDOC07;
  }
  public void setDocNumberDOC07(java.math.BigDecimal docNumberDOC07) {
    this.docNumberDOC07 = docNumberDOC07;
  }
  public String getItemCodeItm01DOC07() {
    return itemCodeItm01DOC07;
  }
  public void setItemCodeItm01DOC07(String itemCodeItm01DOC07) {
    this.itemCodeItm01DOC07 = itemCodeItm01DOC07;
  }
  public String getSupplierItemCodePur02DOC07() {
    return supplierItemCodePur02DOC07;
  }
  public void setSupplierItemCodePur02DOC07(String supplierItemCodePur02DOC07) {
    this.supplierItemCodePur02DOC07 = supplierItemCodePur02DOC07;
  }
  public String getVatCodeItm01DOC07() {
    return vatCodeItm01DOC07;
  }
  public void setVatCodeItm01DOC07(String vatCodeItm01DOC07) {
    this.vatCodeItm01DOC07 = vatCodeItm01DOC07;
  }
  public String getVatDescriptionDOC07() {
    return vatDescriptionDOC07;
  }
  public void setVatDescriptionDOC07(String vatDescriptionDOC07) {
    this.vatDescriptionDOC07 = vatDescriptionDOC07;
  }
  public java.math.BigDecimal getValuePur04DOC07() {
    return valuePur04DOC07;
  }
  public void setValuePur04DOC07(java.math.BigDecimal valuePur04DOC07) {
    this.valuePur04DOC07 = valuePur04DOC07;
  }
  public java.math.BigDecimal getValueDOC07() {
    return valueDOC07;
  }
  public void setValueDOC07(java.math.BigDecimal valueDOC07) {
    this.valueDOC07 = valueDOC07;
  }
  public java.math.BigDecimal getQtyDOC07() {
    return qtyDOC07;
  }
  public void setQtyDOC07(java.math.BigDecimal qtyDOC07) {
    this.qtyDOC07 = qtyDOC07;
  }
  public java.math.BigDecimal getDiscountValueDOC07() {
    return discountValueDOC07;
  }
  public void setDiscountValueDOC07(java.math.BigDecimal discountValueDOC07) {
    this.discountValueDOC07 = discountValueDOC07;
  }
  public java.math.BigDecimal getDiscountPercDOC07() {
    return discountPercDOC07;
  }
  public void setDiscountPercDOC07(java.math.BigDecimal discountPercDOC07) {
    this.discountPercDOC07 = discountPercDOC07;
  }
  public java.sql.Date getStartDatePur04DOC07() {
    return startDatePur04DOC07;
  }
  public void setStartDatePur04DOC07(java.sql.Date startDatePur04DOC07) {
    this.startDatePur04DOC07 = startDatePur04DOC07;
  }
  public java.sql.Date getEndDatePur04DOC07() {
    return endDatePur04DOC07;
  }
  public void setEndDatePur04DOC07(java.sql.Date endDatePur04DOC07) {
    this.endDatePur04DOC07 = endDatePur04DOC07;
  }
  public String getUmCodePur02DOC07() {
    return umCodePur02DOC07;
  }
  public void setUmCodePur02DOC07(String umCodePur02DOC07) {
    this.umCodePur02DOC07 = umCodePur02DOC07;
  }
  public java.math.BigDecimal getDecimalsReg02DOC07() {
    return decimalsReg02DOC07;
  }
  public void setDecimalsReg02DOC07(java.math.BigDecimal decimalsReg02DOC07) {
    this.decimalsReg02DOC07 = decimalsReg02DOC07;
  }
  public java.math.BigDecimal getMinPurchaseQtyPur02DOC07() {
    return minPurchaseQtyPur02DOC07;
  }
  public void setMinPurchaseQtyPur02DOC07(java.math.BigDecimal minPurchaseQtyPur02DOC07) {
    this.minPurchaseQtyPur02DOC07 = minPurchaseQtyPur02DOC07;
  }
  public java.math.BigDecimal getMultipleQtyPur02DOC07() {
    return multipleQtyPur02DOC07;
  }
  public void setMultipleQtyPur02DOC07(java.math.BigDecimal multipleQtyPur02DOC07) {
    this.multipleQtyPur02DOC07 = multipleQtyPur02DOC07;
  }
  public String getDescriptionSYS10() {
    String aux = descriptionSYS10;
//    if (aux==null)
//      return null;
//    if (variantTypeItm06DOC07!=null && !ApplicationConsts.JOLLY.equals(variantTypeItm06DOC07))
//      aux += " "+variantTypeItm06DOC07;
//    if (variantCodeItm11DOC07!=null && !ApplicationConsts.JOLLY.equals(variantCodeItm11DOC07))
//      aux += " "+variantCodeItm11DOC07;
//
//    if (variantTypeItm07DOC07!=null && !ApplicationConsts.JOLLY.equals(variantTypeItm07DOC07))
//      aux += " "+variantTypeItm07DOC07;
//    if (variantCodeItm12DOC07!=null && !ApplicationConsts.JOLLY.equals(variantCodeItm12DOC07))
//      aux += " "+variantCodeItm12DOC07;
//
//    if (variantTypeItm08DOC07!=null && !ApplicationConsts.JOLLY.equals(variantTypeItm08DOC07))
//      aux += " "+variantTypeItm08DOC07;
//    if (variantCodeItm13DOC07!=null && !ApplicationConsts.JOLLY.equals(variantCodeItm13DOC07))
//      aux += " "+variantCodeItm13DOC07;
//
//    if (variantTypeItm09DOC07!=null && !ApplicationConsts.JOLLY.equals(variantTypeItm09DOC07))
//      aux += " "+variantTypeItm09DOC07;
//    if (variantCodeItm14DOC07!=null && !ApplicationConsts.JOLLY.equals(variantCodeItm14DOC07))
//      aux += " "+variantCodeItm14DOC07;
//
//    if (variantTypeItm10DOC07!=null && !ApplicationConsts.JOLLY.equals(variantTypeItm10DOC07))
//      aux += " "+variantTypeItm10DOC07;
//    if (variantCodeItm15DOC07!=null && !ApplicationConsts.JOLLY.equals(variantCodeItm15DOC07))
//      aux += " "+variantCodeItm15DOC07;
    return aux;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public java.math.BigDecimal getVatValueDOC07() {
    return vatValueDOC07;
  }
  public void setVatValueDOC07(java.math.BigDecimal vatValueDOC07) {
    this.vatValueDOC07 = vatValueDOC07;
  }
  public java.math.BigDecimal getValueReg01DOC07() {
    return valueReg01DOC07;
  }
  public void setValueReg01DOC07(java.math.BigDecimal valueReg01DOC07) {
    this.valueReg01DOC07 = valueReg01DOC07;
  }
  public java.math.BigDecimal getDeductibleReg01DOC07() {
    return deductibleReg01DOC07;
  }
  public void setDeductibleReg01DOC07(java.math.BigDecimal deductibleReg01DOC07) {
    this.deductibleReg01DOC07 = deductibleReg01DOC07;
  }
  public java.math.BigDecimal getTaxableIncomeDOC07() {
    return taxableIncomeDOC07;
  }
  public void setTaxableIncomeDOC07(java.math.BigDecimal taxableIncomeDOC07) {
    this.taxableIncomeDOC07 = taxableIncomeDOC07;
  }
  public java.math.BigDecimal getProgressiveHie02DOC07() {
    return progressiveHie02DOC07;
  }
  public void setProgressiveHie02DOC07(java.math.BigDecimal progressiveHie02DOC07) {
    this.progressiveHie02DOC07 = progressiveHie02DOC07;
  }
  public java.math.BigDecimal getRowNumberDOC07() {
    return rowNumberDOC07;
  }
  public void setRowNumberDOC07(java.math.BigDecimal rowNumberDOC07) {
    this.rowNumberDOC07 = rowNumberDOC07;
  }
  public java.sql.Date getDeliveryDateDOC07() {
    return deliveryDateDOC07;
  }
  public void setDeliveryDateDOC07(java.sql.Date deliveryDateDOC07) {
    this.deliveryDateDOC07 = deliveryDateDOC07;
  }
  public java.math.BigDecimal getInQtyDOC07() {
    return inQtyDOC07;
  }
  public void setInQtyDOC07(java.math.BigDecimal inQtyDOC07) {
    this.inQtyDOC07 = inQtyDOC07;
  }
  public java.math.BigDecimal getOrderQtyDOC07() {
    return orderQtyDOC07;
  }
  public void setOrderQtyDOC07(java.math.BigDecimal orderQtyDOC07) {
    this.orderQtyDOC07 = orderQtyDOC07;
  }
  public java.math.BigDecimal getInvoiceQtyDOC07() {
    return invoiceQtyDOC07;
  }
  public void setInvoiceQtyDOC07(java.math.BigDecimal invoiceQtyDOC07) {
    this.invoiceQtyDOC07 = invoiceQtyDOC07;
  }
  public String getCurrencyCodeReg03DOC06() {
    return currencyCodeReg03DOC06;
  }
  public void setCurrencyCodeReg03DOC06(String currencyCodeReg03DOC06) {
    this.currencyCodeReg03DOC06 = currencyCodeReg03DOC06;
  }

  public String getVariantCodeItm11DOC07() {
    return variantCodeItm11DOC07;
  }
  public String getVariantCodeItm12DOC07() {
    return variantCodeItm12DOC07;
  }
  public String getVariantCodeItm13DOC07() {
    return variantCodeItm13DOC07;
  }
  public String getVariantCodeItm14DOC07() {
    return variantCodeItm14DOC07;
  }
  public String getVariantCodeItm15DOC07() {
    return variantCodeItm15DOC07;
  }
  public String getVariantTypeItm06DOC07() {
    return variantTypeItm06DOC07;
  }
  public String getVariantTypeItm07DOC07() {
    return variantTypeItm07DOC07;
  }
  public String getVariantTypeItm08DOC07() {
    return variantTypeItm08DOC07;
  }
  public String getVariantTypeItm09DOC07() {
    return variantTypeItm09DOC07;
  }
  public String getVariantTypeItm10DOC07() {
    return variantTypeItm10DOC07;
  }
  public void setVariantTypeItm10DOC07(String variantTypeItm10DOC07) {
    this.variantTypeItm10DOC07 = variantTypeItm10DOC07;
  }
  public void setVariantTypeItm09DOC07(String variantTypeItm09DOC07) {
    this.variantTypeItm09DOC07 = variantTypeItm09DOC07;
  }
  public void setVariantTypeItm08DOC07(String variantTypeItm08DOC07) {
    this.variantTypeItm08DOC07 = variantTypeItm08DOC07;
  }
  public void setVariantTypeItm07DOC07(String variantTypeItm07DOC07) {
    this.variantTypeItm07DOC07 = variantTypeItm07DOC07;
  }
  public void setVariantTypeItm06DOC07(String variantTypeItm06DOC07) {
    this.variantTypeItm06DOC07 = variantTypeItm06DOC07;
  }
  public void setVariantCodeItm15DOC07(String variantCodeItm15DOC07) {
    this.variantCodeItm15DOC07 = variantCodeItm15DOC07;
  }
  public void setVariantCodeItm14DOC07(String variantCodeItm14DOC07) {
    this.variantCodeItm14DOC07 = variantCodeItm14DOC07;
  }
  public void setVariantCodeItm13DOC07(String variantCodeItm13DOC07) {
    this.variantCodeItm13DOC07 = variantCodeItm13DOC07;
  }
  public void setVariantCodeItm12DOC07(String variantCodeItm12DOC07) {
    this.variantCodeItm12DOC07 = variantCodeItm12DOC07;
  }
  public void setVariantCodeItm11DOC07(String variantCodeItm11DOC07) {
    this.variantCodeItm11DOC07 = variantCodeItm11DOC07;
  }
  public Boolean getNoWarehouseMovITM01() {
    return noWarehouseMovITM01;
  }
  public void setNoWarehouseMovITM01(Boolean noWarehouseMovITM01) {
    this.noWarehouseMovITM01 = noWarehouseMovITM01;
  }


}
