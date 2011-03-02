package org.jallinone.sales.discounts.java;

import org.openswing.swing.message.receive.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a generic sale discount.</p>
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
public class DiscountVO extends ValueObjectImpl {

  private String companyCodeSys01SAL03;
  private String discountCodeSAL03;
  private String currencyCodeReg03SAL03;
  private String descriptionSYS10;
  private String discountTypeSAL03;
  private String enabledSAL03;
  private java.math.BigDecimal minValueSAL03;
  private java.math.BigDecimal maxValueSAL03;
  private java.math.BigDecimal minPercSAL03;
  private java.math.BigDecimal maxPercSAL03;
  private java.math.BigDecimal progressiveSys10SAL03;
  private java.sql.Date startDateSAL03;
  private java.sql.Date endDateSAL03;
  private java.math.BigDecimal minQtySAL03;
  private Boolean multipleQtySAL03;


  public DiscountVO() {
  }


  public String getCompanyCodeSys01SAL03() {
    return companyCodeSys01SAL03;
  }
  public void setCompanyCodeSys01SAL03(String companyCodeSys01SAL03) {
    this.companyCodeSys01SAL03 = companyCodeSys01SAL03;
  }
  public String getDiscountCodeSAL03() {
    return discountCodeSAL03;
  }
  public void setDiscountCodeSAL03(String discountCodeSAL03) {
    this.discountCodeSAL03 = discountCodeSAL03;
  }
  public String getCurrencyCodeReg03SAL03() {
    return currencyCodeReg03SAL03;
  }
  public void setCurrencyCodeReg03SAL03(String currencyCodeReg03SAL03) {
    this.currencyCodeReg03SAL03 = currencyCodeReg03SAL03;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public String getDiscountTypeSAL03() {
    return discountTypeSAL03;
  }
  public void setDiscountTypeSAL03(String discountTypeSAL03) {
    this.discountTypeSAL03 = discountTypeSAL03;
  }
  public String getEnabledSAL03() {
    return enabledSAL03;
  }
  public void setEnabledSAL03(String enabledSAL03) {
    this.enabledSAL03 = enabledSAL03;
  }
  public java.math.BigDecimal getMinValueSAL03() {
    return minValueSAL03;
  }
  public void setMinValueSAL03(java.math.BigDecimal minValueSAL03) {
    this.minValueSAL03 = minValueSAL03;
  }
  public java.math.BigDecimal getMaxValueSAL03() {
    return maxValueSAL03;
  }
  public void setMaxValueSAL03(java.math.BigDecimal maxValueSAL03) {
    this.maxValueSAL03 = maxValueSAL03;
  }
  public java.math.BigDecimal getMinPercSAL03() {
    return minPercSAL03;
  }
  public void setMinPercSAL03(java.math.BigDecimal minPercSAL03) {
    this.minPercSAL03 = minPercSAL03;
  }
  public java.math.BigDecimal getMaxPercSAL03() {
    return maxPercSAL03;
  }
  public void setMaxPercSAL03(java.math.BigDecimal maxPercSAL03) {
    this.maxPercSAL03 = maxPercSAL03;
  }
  public java.math.BigDecimal getProgressiveSys10SAL03() {
    return progressiveSys10SAL03;
  }
  public void setProgressiveSys10SAL03(java.math.BigDecimal progressiveSys10SAL03) {
    this.progressiveSys10SAL03 = progressiveSys10SAL03;
  }
  public java.sql.Date getStartDateSAL03() {
    return startDateSAL03;
  }
  public void setStartDateSAL03(java.sql.Date startDateSAL03) {
    this.startDateSAL03 = startDateSAL03;
  }
  public java.sql.Date getEndDateSAL03() {
    return endDateSAL03;
  }
  public void setEndDateSAL03(java.sql.Date endDateSAL03) {
    this.endDateSAL03 = endDateSAL03;
  }
  public java.math.BigDecimal getMinQtySAL03() {
    return minQtySAL03;
  }
  public Boolean getMultipleQtySAL03() {
    return multipleQtySAL03;
  }
  public void setMultipleQtySAL03(Boolean multipleQtySAL03) {
    this.multipleQtySAL03 = multipleQtySAL03;
  }
  public void setMinQtySAL03(java.math.BigDecimal minQtySAL03) {
    this.minQtySAL03 = minQtySAL03;
  }

}
