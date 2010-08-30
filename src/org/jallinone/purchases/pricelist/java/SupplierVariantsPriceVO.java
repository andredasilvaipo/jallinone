package org.jallinone.purchases.pricelist.java;

import org.jallinone.system.customizations.java.BaseValueObject;
import org.jallinone.commons.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store the price related to a combination of variants for a supplier's item.</p>
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
public class SupplierVariantsPriceVO extends BaseValueObject {

  private String pricelistCodePur03PUR05;
  private String companyCodeSys01PUR05;
  private String itemCodeItm01PUR05;
  private java.math.BigDecimal valuePUR05;
  private java.sql.Date startDatePUR05;
  private java.sql.Date endDatePUR05;
  private java.math.BigDecimal progressiveReg04PUR05;
  private java.math.BigDecimal progressivePUR05;

  private String variantTypeItm06PUR05;
  private String variantCodeItm11PUR05;
  private String variantTypeItm07PUR05;
  private String variantCodeItm12PUR05;
  private String variantTypeItm08PUR05;
  private String variantCodeItm13PUR05;
  private String variantTypeItm09PUR05;
  private String variantCodeItm14PUR05;
  private String variantTypeItm10PUR05;
  private String variantCodeItm15PUR05;


  public SupplierVariantsPriceVO() {
  }



  public String getPricelistCodePur03PUR05() {
    return pricelistCodePur03PUR05;
  }
  public void setPricelistCodePur03PUR05(String pricelistCodePur03PUR05) {
    this.pricelistCodePur03PUR05 = pricelistCodePur03PUR05;
  }
  public String getCompanyCodeSys01PUR05() {
    return companyCodeSys01PUR05;
  }
  public void setCompanyCodeSys01PUR05(String companyCodeSys01PUR05) {
    this.companyCodeSys01PUR05 = companyCodeSys01PUR05;
  }
  public java.math.BigDecimal getValuePUR05() {
    return valuePUR05;
  }
  public void setValuePUR05(java.math.BigDecimal valuePUR05) {
    this.valuePUR05 = valuePUR05;
  }
  public java.sql.Date getStartDatePUR05() {
    return startDatePUR05;
  }
  public void setStartDatePUR05(java.sql.Date startDatePUR05) {
    this.startDatePUR05 = startDatePUR05;
  }
  public String getItemCodeItm01PUR05() {
    return itemCodeItm01PUR05;
  }
  public void setItemCodeItm01PUR05(String itemCodeItm01PUR05) {
    this.itemCodeItm01PUR05 = itemCodeItm01PUR05;
  }
  public java.sql.Date getEndDatePUR05() {
    return endDatePUR05;
  }
  public void setEndDatePUR05(java.sql.Date endDatePUR05) {
    this.endDatePUR05 = endDatePUR05;
  }
  public void setProgressiveReg04PUR05(java.math.BigDecimal progressiveReg04PUR05) {
    this.progressiveReg04PUR05 = progressiveReg04PUR05;
  }



  public String getVariantCodeItm11PUR05() {
    return variantCodeItm11PUR05;
  }
  public String getVariantCodeItm12PUR05() {
    return variantCodeItm12PUR05;
  }
  public String getVariantCodeItm13PUR05() {
    return variantCodeItm13PUR05;
  }
  public String getVariantCodeItm14PUR05() {
    return variantCodeItm14PUR05;
  }
  public String getVariantCodeItm15PUR05() {
    return variantCodeItm15PUR05;
  }
  public String getVariantTypeItm06PUR05() {
    return variantTypeItm06PUR05;
  }
  public String getVariantTypeItm07PUR05() {
    return variantTypeItm07PUR05;
  }
  public String getVariantTypeItm08PUR05() {
    return variantTypeItm08PUR05;
  }
  public String getVariantTypeItm09PUR05() {
    return variantTypeItm09PUR05;
  }
  public String getVariantTypeItm10PUR05() {
    return variantTypeItm10PUR05;
  }
  public void setVariantTypeItm10PUR05(String variantTypeItm10PUR05) {
    this.variantTypeItm10PUR05 = variantTypeItm10PUR05;
  }
  public void setVariantTypeItm09PUR05(String variantTypeItm09PUR05) {
    this.variantTypeItm09PUR05 = variantTypeItm09PUR05;
  }
  public void setVariantTypeItm08PUR05(String variantTypeItm08PUR05) {
    this.variantTypeItm08PUR05 = variantTypeItm08PUR05;
  }
  public void setVariantTypeItm07PUR05(String variantTypeItm07PUR05) {
    this.variantTypeItm07PUR05 = variantTypeItm07PUR05;
  }
  public void setVariantTypeItm06PUR05(String variantTypeItm06PUR05) {
    this.variantTypeItm06PUR05 = variantTypeItm06PUR05;
  }
  public void setVariantCodeItm15PUR05(String variantCodeItm15PUR05) {
    this.variantCodeItm15PUR05 = variantCodeItm15PUR05;
  }
  public void setVariantCodeItm14PUR05(String variantCodeItm14PUR05) {
    this.variantCodeItm14PUR05 = variantCodeItm14PUR05;
  }
  public void setVariantCodeItm13PUR05(String variantCodeItm13PUR05) {
    this.variantCodeItm13PUR05 = variantCodeItm13PUR05;
  }
  public void setVariantCodeItm12PUR05(String variantCodeItm12PUR05) {
    this.variantCodeItm12PUR05 = variantCodeItm12PUR05;
  }
  public void setVariantCodeItm11PUR05(String variantCodeItm11PUR05) {
    this.variantCodeItm11PUR05 = variantCodeItm11PUR05;
  }
  public java.math.BigDecimal getProgressiveReg04PUR05() {
    return progressiveReg04PUR05;
  }
  public java.math.BigDecimal getProgressivePUR05() {
    return progressivePUR05;
  }
  public void setProgressivePUR05(java.math.BigDecimal progressivePUR05) {
    this.progressivePUR05 = progressivePUR05;
  }


}
