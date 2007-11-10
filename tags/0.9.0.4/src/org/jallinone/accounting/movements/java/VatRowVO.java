package org.jallinone.accounting.movements.java;

import org.openswing.swing.message.receive.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value Object used to create an item in the journal.</p>
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
public class VatRowVO extends ValueObjectImpl {

  private String companyCodeSys01ACC07;
  private java.math.BigDecimal vatYearACC07;
  private java.math.BigDecimal progressiveACC07;
  private java.math.BigDecimal recordNumberACC07;
  private String registerCodeAcc04ACC07;
  private java.sql.Date vatDateACC07;
  private java.math.BigDecimal vatValueACC07;
  private String vatCodeACC07;
  private String vatDescriptionACC07;
  private java.math.BigDecimal taxableIncomeACC07;


  public VatRowVO() {
  }


  public java.math.BigDecimal getProgressiveACC07() {
    return progressiveACC07;
  }
  public void setProgressiveACC07(java.math.BigDecimal progressiveACC07) {
    this.progressiveACC07 = progressiveACC07;
  }
  public java.math.BigDecimal getRecordNumberACC07() {
    return recordNumberACC07;
  }
  public void setRecordNumberACC07(java.math.BigDecimal recordNumberACC07) {
    this.recordNumberACC07 = recordNumberACC07;
  }
  public java.sql.Date getVatDateACC07() {
    return vatDateACC07;
  }
  public void setVatDateACC07(java.sql.Date vatDateACC07) {
    this.vatDateACC07 = vatDateACC07;
  }
  public java.math.BigDecimal getVatValueACC07() {
    return vatValueACC07;
  }
  public void setVatValueACC07(java.math.BigDecimal vatValueACC07) {
    this.vatValueACC07 = vatValueACC07;
  }
  public String getRegisterCodeAcc04ACC07() {
    return registerCodeAcc04ACC07;
  }
  public void setRegisterCodeAcc04ACC07(String registerCodeAcc04ACC07) {
    this.registerCodeAcc04ACC07 = registerCodeAcc04ACC07;
  }
  public java.math.BigDecimal getVatYearACC07() {
    return vatYearACC07;
  }
  public String getCompanyCodeSys01ACC07() {
    return companyCodeSys01ACC07;
  }
  public void setCompanyCodeSys01ACC07(String companyCodeSys01ACC07) {
    this.companyCodeSys01ACC07 = companyCodeSys01ACC07;
  }
  public void setVatYearACC07(java.math.BigDecimal vatYearACC07) {
    this.vatYearACC07 = vatYearACC07;
  }
  public String getVatCodeACC07() {
    return vatCodeACC07;
  }
  public void setVatCodeACC07(String vatCodeACC07) {
    this.vatCodeACC07 = vatCodeACC07;
  }
  public String getVatDescriptionACC07() {
    return vatDescriptionACC07;
  }
  public void setVatDescriptionACC07(String vatDescriptionACC07) {
    this.vatDescriptionACC07 = vatDescriptionACC07;
  }
  public java.math.BigDecimal getTaxableIncomeACC07() {
    return taxableIncomeACC07;
  }
  public void setTaxableIncomeACC07(java.math.BigDecimal taxableIncomeACC07) {
    this.taxableIncomeACC07 = taxableIncomeACC07;
  }

}
