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


  public SaleItemDiscountVO() {
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
