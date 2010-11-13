package org.jallinone.purchases.documents.java;

import org.openswing.swing.message.receive.java.ValueObjectImpl;
import java.math.BigDecimal;
import org.jallinone.commons.java.ApplicationConsts;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: value object used by the reorder grid.</p>
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
public class ReorderFromMinStockVO extends ValueObjectImpl {

  private boolean selected;

  private BigDecimal progressiveREG04; // supplier
  private String supplierCode;
  private String name_1REG04;
  private String pricelistCodePUR03;
  private String pricelistDescription;
  private String paymentCodeREG10;
  private String supplierItemCode;
  private java.sql.Date startDate;
  private java.sql.Date endDate;
  private BigDecimal minPurchaseQtyPUR02;
  private BigDecimal multipleQtyPUR02;
  private String umCodeREG02;


  private BigDecimal proposedQty;
  private BigDecimal qty;

  private BigDecimal goodQty;
  private BigDecimal pawnedQty;
  private BigDecimal availableQty;
  private BigDecimal orderedQty;

  private BigDecimal minStockITM23;
  private String companyCodeSys01;
  private String itemCode;
  private String itemDescription;
  private String variantTypeITM06;
  private String variantTypeITM07;
  private String variantTypeITM08;
  private String variantTypeITM09;
  private String variantTypeITM10;
  private String variantCodeITM11;
  private String variantCodeITM12;
  private String variantCodeITM13;
  private String variantCodeITM14;
  private String variantCodeITM15;
  private String vatCode;
  private String vatDescription;
  private BigDecimal vatValue;
  private BigDecimal deductible;
  private BigDecimal unitPrice;


  public ReorderFromMinStockVO() {
  }


  public BigDecimal getAvailableQty() {
    return availableQty;
  }
  public String getCompanyCodeSys01() {
    return companyCodeSys01;
  }
  public BigDecimal getGoodQty() {
    return goodQty;
  }
  public String getItemCode() {
    return itemCode;
  }
  public String getItemDescription() {
    String aux = itemDescription;
//    if (aux==null)
//      return null;
//    if (variantTypeITM06!=null && !ApplicationConsts.JOLLY.equals(variantTypeITM06))
//      aux += " "+variantTypeITM06;
//    if (variantCodeITM11!=null && !ApplicationConsts.JOLLY.equals(variantCodeITM11))
//      aux += " "+variantCodeITM11;
//
//    if (variantTypeITM07!=null && !ApplicationConsts.JOLLY.equals(variantTypeITM07))
//      aux += " "+variantTypeITM07;
//    if (variantCodeITM12!=null && !ApplicationConsts.JOLLY.equals(variantCodeITM12))
//      aux += " "+variantCodeITM12;
//
//    if (variantTypeITM08!=null && !ApplicationConsts.JOLLY.equals(variantTypeITM08))
//      aux += " "+variantTypeITM08;
//    if (variantCodeITM13!=null && !ApplicationConsts.JOLLY.equals(variantCodeITM13))
//      aux += " "+variantCodeITM13;
//
//    if (variantTypeITM09!=null && !ApplicationConsts.JOLLY.equals(variantTypeITM09))
//      aux += " "+variantTypeITM09;
//    if (variantCodeITM14!=null && !ApplicationConsts.JOLLY.equals(variantCodeITM14))
//      aux += " "+variantCodeITM14;
//
//    if (variantTypeITM10!=null && !ApplicationConsts.JOLLY.equals(variantTypeITM10))
//      aux += " "+variantTypeITM10;
//    if (variantCodeITM15!=null && !ApplicationConsts.JOLLY.equals(variantCodeITM15))
//      aux += " "+variantCodeITM15;
    return aux;

  }
  public BigDecimal getMinStockITM23() {
    return minStockITM23;
  }
  public String getName_1REG04() {
    return name_1REG04;
  }
  public BigDecimal getOrderedQty() {
    return orderedQty;
  }
  public BigDecimal getProgressiveREG04() {
    return progressiveREG04;
  }
  public BigDecimal getProposedQty() {
    return proposedQty;
  }
  public BigDecimal getQty() {
    return qty;
  }
  public boolean isSelected() {
    return selected;
  }
  public String getSupplierCode() {
    return supplierCode;
  }
  public String getUmCodeREG02() {
    return umCodeREG02;
  }
  public String getVariantCodeITM11() {
    return variantCodeITM11;
  }
  public String getVariantCodeITM12() {
    return variantCodeITM12;
  }
  public String getVariantCodeITM13() {
    return variantCodeITM13;
  }
  public String getVariantCodeITM14() {
    return variantCodeITM14;
  }
  public String getVariantCodeITM15() {
    return variantCodeITM15;
  }
  public String getVariantTypeITM06() {
    return variantTypeITM06;
  }
  public String getVariantTypeITM07() {
    return variantTypeITM07;
  }
  public String getVariantTypeITM08() {
    return variantTypeITM08;
  }
  public String getVariantTypeITM09() {
    return variantTypeITM09;
  }
  public String getVariantTypeITM10() {
    return variantTypeITM10;
  }
  public void setVariantTypeITM10(String variantTypeITM10) {
    this.variantTypeITM10 = variantTypeITM10;
  }
  public void setVariantTypeITM09(String variantTypeITM09) {
    this.variantTypeITM09 = variantTypeITM09;
  }
  public void setVariantTypeITM08(String variantTypeITM08) {
    this.variantTypeITM08 = variantTypeITM08;
  }
  public void setVariantTypeITM07(String variantTypeITM07) {
    this.variantTypeITM07 = variantTypeITM07;
  }
  public void setVariantTypeITM06(String variantTypeITM06) {
    this.variantTypeITM06 = variantTypeITM06;
  }
  public void setAvailableQty(BigDecimal availableQty) {
    this.availableQty = availableQty;
  }
  public void setCompanyCodeSys01(String companyCodeSys01) {
    this.companyCodeSys01 = companyCodeSys01;
  }
  public void setGoodQty(BigDecimal goodQty) {
    this.goodQty = goodQty;
  }
  public void setItemCode(String itemCode) {
    this.itemCode = itemCode;
  }
  public void setItemDescription(String itemDescription) {
    this.itemDescription = itemDescription;
  }
  public void setMinStockITM23(BigDecimal minStockITM23) {
    this.minStockITM23 = minStockITM23;
  }
  public void setName_1REG04(String name_1REG04) {
    this.name_1REG04 = name_1REG04;
  }
  public void setOrderedQty(BigDecimal orderedQty) {
    this.orderedQty = orderedQty;
  }
  public void setProgressiveREG04(BigDecimal progressiveREG04) {
    this.progressiveREG04 = progressiveREG04;
  }
  public void setProposedQty(BigDecimal proposedQty) {
    this.proposedQty = proposedQty;
  }
  public void setQty(BigDecimal qty) {
    this.qty = qty;
  }
  public void setSelected(boolean selected) {
    this.selected = selected;
  }
  public void setSupplierCode(String supplierCode) {
    this.supplierCode = supplierCode;
  }
  public void setUmCodeREG02(String umCodeREG02) {
    this.umCodeREG02 = umCodeREG02;
  }
  public void setVariantCodeITM11(String variantCodeITM11) {
    this.variantCodeITM11 = variantCodeITM11;
  }
  public void setVariantCodeITM12(String variantCodeITM12) {
    this.variantCodeITM12 = variantCodeITM12;
  }
  public void setVariantCodeITM13(String variantCodeITM13) {
    this.variantCodeITM13 = variantCodeITM13;
  }
  public BigDecimal getPawnedQty() {
    return pawnedQty;
  }
  public void setPawnedQty(BigDecimal pawnedQty) {
    this.pawnedQty = pawnedQty;
  }
  public void setVariantCodeITM15(String variantCodeITM15) {
    this.variantCodeITM15 = variantCodeITM15;
  }
  public void setVariantCodeITM14(String variantCodeITM14) {
    this.variantCodeITM14 = variantCodeITM14;
  }
  public String getPaymentCodeREG10() {
    return paymentCodeREG10;
  }
  public String getPricelistCodePUR03() {
    return pricelistCodePUR03;
  }
  public void setPaymentCodeREG10(String paymentCodeREG10) {
    this.paymentCodeREG10 = paymentCodeREG10;
  }
  public void setPricelistCodePUR03(String pricelistCodePUR03) {
    this.pricelistCodePUR03 = pricelistCodePUR03;
  }
  public BigDecimal getDeductible() {
    return deductible;
  }
  public void setDeductible(BigDecimal deductible) {
    this.deductible = deductible;
  }
  public String getVatCode() {
    return vatCode;
  }
  public String getVatDescription() {
    return vatDescription;
  }
  public BigDecimal getVatValue() {
    return vatValue;
  }
  public void setVatValue(BigDecimal vatValue) {
    this.vatValue = vatValue;
  }
  public void setVatDescription(String vatDescription) {
    this.vatDescription = vatDescription;
  }
  public void setVatCode(String vatCode) {
    this.vatCode = vatCode;
  }
  public BigDecimal getUnitPrice() {
    return unitPrice;
  }
  public void setUnitPrice(BigDecimal unitPrice) {
    this.unitPrice = unitPrice;
  }
  public String getPricelistDescription() {
    return pricelistDescription;
  }
  public void setPricelistDescription(String pricelistDescription) {
    this.pricelistDescription = pricelistDescription;
  }
  public String getSupplierItemCode() {
    return supplierItemCode;
  }
  public void setSupplierItemCode(String supplierItemCode) {
    this.supplierItemCode = supplierItemCode;
  }
  public java.sql.Date getStartDate() {
    return startDate;
  }
  public void setStartDate(java.sql.Date startDate) {
    this.startDate = startDate;
  }
  public java.sql.Date getEndDate() {
    return endDate;
  }
  public void setEndDate(java.sql.Date endDate) {
    this.endDate = endDate;
  }
  public BigDecimal getMultipleQtyPUR02() {
    return multipleQtyPUR02;
  }
  public BigDecimal getMinPurchaseQtyPUR02() {
    return minPurchaseQtyPUR02;
  }
  public void setMinPurchaseQtyPUR02(BigDecimal minPurchaseQtyPUR02) {
    this.minPurchaseQtyPUR02 = minPurchaseQtyPUR02;
  }
  public void setMultipleQtyPUR02(BigDecimal multipleQtyPUR02) {
    this.multipleQtyPUR02 = multipleQtyPUR02;
  }


}
