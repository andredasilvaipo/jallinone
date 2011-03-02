package org.jallinone.sales.documents.itemdiscounts.java;

import org.openswing.swing.message.receive.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a discount applyable to an item (item discount or item hierarchy discount).</p>
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
public class SaleItemDiscountVO extends ValueObjectImpl {

  private String companyCodeSys01DOC04;
  private String discountCodeSal03DOC04;
  private java.math.BigDecimal minValueDOC04;
  private java.math.BigDecimal maxValueDOC04;
  private java.math.BigDecimal minPercDOC04;
  private java.math.BigDecimal maxPercDOC04;
  private java.sql.Date startDateDOC04;
  private java.sql.Date endDateDOC04;
  private String discountDescriptionDOC04;
  private java.math.BigDecimal valueDOC04;
  private java.math.BigDecimal percDOC04;
  private String docTypeDOC04;
  private java.math.BigDecimal docYearDOC04;
  private java.math.BigDecimal docNumberDOC04;
  private String itemCodeItm01DOC04;
  private java.math.BigDecimal minQtyDOC04;
  private Boolean multipleQtyDOC04;

  private String variantTypeItm06DOC04;
  private String variantCodeItm11DOC04;
  private String variantTypeItm07DOC04;
  private String variantCodeItm12DOC04;
  private String variantTypeItm08DOC04;
  private String variantCodeItm13DOC04;
  private String variantTypeItm09DOC04;
  private String variantCodeItm14DOC04;
  private String variantTypeItm10DOC04;
  private String variantCodeItm15DOC04;

  
  public SaleItemDiscountVO() {
  }

  



  public String getVariantTypeItm06DOC04() {
	  return variantTypeItm06DOC04;
  }





  public void setVariantTypeItm06DOC04(String variantTypeItm06DOC04) {
	  this.variantTypeItm06DOC04 = variantTypeItm06DOC04;
  }





  public String getVariantCodeItm11DOC04() {
	  return variantCodeItm11DOC04;
  }





  public void setVariantCodeItm11DOC04(String variantCodeItm11DOC04) {
	  this.variantCodeItm11DOC04 = variantCodeItm11DOC04;
  }





  public String getVariantTypeItm07DOC04() {
	  return variantTypeItm07DOC04;
  }





  public void setVariantTypeItm07DOC04(String variantTypeItm07DOC04) {
	  this.variantTypeItm07DOC04 = variantTypeItm07DOC04;
  }





  public String getVariantCodeItm12DOC04() {
	  return variantCodeItm12DOC04;
  }





  public void setVariantCodeItm12DOC04(String variantCodeItm12DOC04) {
	  this.variantCodeItm12DOC04 = variantCodeItm12DOC04;
  }





  public String getVariantTypeItm08DOC04() {
	  return variantTypeItm08DOC04;
  }





  public void setVariantTypeItm08DOC04(String variantTypeItm08DOC04) {
	  this.variantTypeItm08DOC04 = variantTypeItm08DOC04;
  }





  public String getVariantCodeItm13DOC04() {
	  return variantCodeItm13DOC04;
  }





  public void setVariantCodeItm13DOC04(String variantCodeItm13DOC04) {
	  this.variantCodeItm13DOC04 = variantCodeItm13DOC04;
  }





  public String getVariantTypeItm09DOC04() {
	  return variantTypeItm09DOC04;
  }





  public void setVariantTypeItm09DOC04(String variantTypeItm09DOC04) {
	  this.variantTypeItm09DOC04 = variantTypeItm09DOC04;
  }





  public String getVariantCodeItm14DOC04() {
	  return variantCodeItm14DOC04;
  }





  public void setVariantCodeItm14DOC04(String variantCodeItm14DOC04) {
	  this.variantCodeItm14DOC04 = variantCodeItm14DOC04;
  }





  public String getVariantTypeItm10DOC04() {
	  return variantTypeItm10DOC04;
  }





  public void setVariantTypeItm10DOC04(String variantTypeItm10DOC04) {
	  this.variantTypeItm10DOC04 = variantTypeItm10DOC04;
  }





  public String getVariantCodeItm15DOC04() {
	  return variantCodeItm15DOC04;
  }





  public void setVariantCodeItm15DOC04(String variantCodeItm15DOC04) {
	  this.variantCodeItm15DOC04 = variantCodeItm15DOC04;
  }





  public String getDiscountDescriptionDOC04() {
	  return discountDescriptionDOC04;
  }
  public void setDiscountDescriptionDOC04(String discountDescriptionDOC04) {
	  this.discountDescriptionDOC04 = discountDescriptionDOC04;
  }
  public java.math.BigDecimal getValueDOC04() {
	  return valueDOC04;
  }
  public void setValueDOC04(java.math.BigDecimal valueDOC04) {
	  this.valueDOC04 = valueDOC04;
  }
  public java.math.BigDecimal getPercDOC04() {
	  return percDOC04;
  }
  public void setPercDOC04(java.math.BigDecimal percDOC04) {
	  this.percDOC04 = percDOC04;
  }
  public String getDocTypeDOC04() {
	  return docTypeDOC04;
  }
  public void setDocTypeDOC04(String docTypeDOC04) {
	  this.docTypeDOC04 = docTypeDOC04;
  }
  public java.math.BigDecimal getDocYearDOC04() {
	  return docYearDOC04;
  }
  public void setDocYearDOC04(java.math.BigDecimal docYearDOC04) {
	  this.docYearDOC04 = docYearDOC04;
  }
  public java.math.BigDecimal getDocNumberDOC04() {
	  return docNumberDOC04;
  }
  public void setDocNumberDOC04(java.math.BigDecimal docNumberDOC04) {
	  this.docNumberDOC04 = docNumberDOC04;
  }
  public String getItemCodeItm01DOC04() {
    return itemCodeItm01DOC04;
  }
  public void setItemCodeItm01DOC04(String itemCodeItm01DOC04) {
    this.itemCodeItm01DOC04 = itemCodeItm01DOC04;
  }
  public String getCompanyCodeSys01DOC04() {
    return companyCodeSys01DOC04;
  }
  public java.sql.Date getEndDateDOC04() {
    return endDateDOC04;
  }
  public java.math.BigDecimal getMaxPercDOC04() {
    return maxPercDOC04;
  }
  public java.math.BigDecimal getMaxValueDOC04() {
    return maxValueDOC04;
  }
  public java.math.BigDecimal getMinPercDOC04() {
    return minPercDOC04;
  }
  public java.math.BigDecimal getMinValueDOC04() {
    return minValueDOC04;
  }
  public java.sql.Date getStartDateDOC04() {
    return startDateDOC04;
  }
  public void setStartDateDOC04(java.sql.Date startDateDOC04) {
    this.startDateDOC04 = startDateDOC04;
  }
  public void setMinValueDOC04(java.math.BigDecimal minValueDOC04) {
    this.minValueDOC04 = minValueDOC04;
  }
  public void setMinPercDOC04(java.math.BigDecimal minPercDOC04) {
    this.minPercDOC04 = minPercDOC04;
  }
  public void setMaxValueDOC04(java.math.BigDecimal maxValueDOC04) {
    this.maxValueDOC04 = maxValueDOC04;
  }
  public void setMaxPercDOC04(java.math.BigDecimal maxPercDOC04) {
    this.maxPercDOC04 = maxPercDOC04;
  }
  public void setEndDateDOC04(java.sql.Date endDateDOC04) {
    this.endDateDOC04 = endDateDOC04;
  }
  public void setDiscountCodeSal03DOC04(String discountCodeSal03DOC04) {
    this.discountCodeSal03DOC04 = discountCodeSal03DOC04;
  }
  public void setCompanyCodeSys01DOC04(String companyCodeSys01DOC04) {
    this.companyCodeSys01DOC04 = companyCodeSys01DOC04;
  }
  public String getDiscountCodeSal03DOC04() {
    return discountCodeSal03DOC04;
  }
  public java.math.BigDecimal getMinQtyDOC04() {
    return minQtyDOC04;
  }
  public Boolean getMultipleQtyDOC04() {
    return multipleQtyDOC04;
  }
  public void setMinQtyDOC04(java.math.BigDecimal minQtyDOC04) {
    this.minQtyDOC04 = minQtyDOC04;
  }
  public void setMultipleQtyDOC04(Boolean multipleQtyDOC04) {
    this.multipleQtyDOC04 = multipleQtyDOC04;
  }


}
