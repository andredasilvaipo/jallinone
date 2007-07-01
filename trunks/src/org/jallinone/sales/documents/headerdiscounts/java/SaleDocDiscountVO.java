package org.jallinone.sales.documents.headerdiscounts.java;

import org.openswing.swing.message.receive.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store an hesder discount applyable to the specified customer (customer discount or customer hierarchy discount).</p>
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
public class SaleDocDiscountVO extends ValueObjectImpl {

  private String companyCodeSys01DOC05;
  private String discountCodeSal03DOC05;
  private java.math.BigDecimal minValueDOC05;
  private java.math.BigDecimal maxValueDOC05;
  private java.math.BigDecimal minPercDOC05;
  private java.math.BigDecimal maxPercDOC05;
  private java.sql.Date startDateDOC05;
  private java.sql.Date endDateDOC05;
  private String discountDescriptionDOC05;
  private java.math.BigDecimal valueDOC05;
  private java.math.BigDecimal percDOC05;
  private String docTypeDOC05;
  private java.math.BigDecimal docYearDOC05;
  private java.math.BigDecimal docNumberDOC05;


  public SaleDocDiscountVO() {
  }


  public String getDiscountDescriptionDOC05() {
    return discountDescriptionDOC05;
  }
  public void setDiscountDescriptionDOC05(String discountDescriptionDOC05) {
    this.discountDescriptionDOC05 = discountDescriptionDOC05;
  }
  public java.math.BigDecimal getValueDOC05() {
    return valueDOC05;
  }
  public void setValueDOC05(java.math.BigDecimal valueDOC05) {
    this.valueDOC05 = valueDOC05;
  }
  public java.math.BigDecimal getPercDOC05() {
    return percDOC05;
  }
  public void setPercDOC05(java.math.BigDecimal percDOC05) {
    this.percDOC05 = percDOC05;
  }
  public String getDocTypeDOC05() {
    return docTypeDOC05;
  }
  public void setDocTypeDOC05(String docTypeDOC05) {
    this.docTypeDOC05 = docTypeDOC05;
  }
  public java.math.BigDecimal getDocYearDOC05() {
    return docYearDOC05;
  }
  public void setDocYearDOC05(java.math.BigDecimal docYearDOC05) {
    this.docYearDOC05 = docYearDOC05;
  }
  public java.math.BigDecimal getDocNumberDOC05() {
    return docNumberDOC05;
  }
  public void setDocNumberDOC05(java.math.BigDecimal docNumberDOC05) {
    this.docNumberDOC05 = docNumberDOC05;
  }

  public String getCompanyCodeSys01DOC05() {
    return companyCodeSys01DOC05;
  }
  public java.sql.Date getEndDateDOC05() {
    return endDateDOC05;
  }
  public java.math.BigDecimal getMaxPercDOC05() {
    return maxPercDOC05;
  }
  public java.math.BigDecimal getMaxValueDOC05() {
    return maxValueDOC05;
  }
  public java.math.BigDecimal getMinPercDOC05() {
    return minPercDOC05;
  }
  public java.math.BigDecimal getMinValueDOC05() {
    return minValueDOC05;
  }
  public java.sql.Date getStartDateDOC05() {
    return startDateDOC05;
  }
  public void setStartDateDOC05(java.sql.Date startDateDOC05) {
    this.startDateDOC05 = startDateDOC05;
  }
  public void setMinValueDOC05(java.math.BigDecimal minValueDOC05) {
    this.minValueDOC05 = minValueDOC05;
  }
  public void setMinPercDOC05(java.math.BigDecimal minPercDOC05) {
    this.minPercDOC05 = minPercDOC05;
  }
  public void setMaxValueDOC05(java.math.BigDecimal maxValueDOC05) {
    this.maxValueDOC05 = maxValueDOC05;
  }
  public void setMaxPercDOC05(java.math.BigDecimal maxPercDOC05) {
    this.maxPercDOC05 = maxPercDOC05;
  }
  public void setEndDateDOC05(java.sql.Date endDateDOC05) {
    this.endDateDOC05 = endDateDOC05;
  }
  public void setDiscountCodeSal03DOC05(String discountCodeSal03DOC05) {
    this.discountCodeSal03DOC05 = discountCodeSal03DOC05;
  }
  public void setCompanyCodeSys01DOC05(String companyCodeSys01DOC05) {
    this.companyCodeSys01DOC05 = companyCodeSys01DOC05;
  }
  public String getDiscountCodeSal03DOC05() {
    return discountCodeSal03DOC05;
  }


}
