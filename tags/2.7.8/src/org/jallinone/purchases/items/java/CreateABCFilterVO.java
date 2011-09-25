package org.jallinone.purchases.items.java;

import org.openswing.swing.message.receive.java.ValueObjectImpl;
import java.math.BigDecimal;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description:Value object used to store settings for ABC classification.</p>
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
public class CreateABCFilterVO extends ValueObjectImpl {

  String companyCode;
  String warehouseCode;
  String warehouseDescription;
  java.sql.Date startDate;
  java.sql.Date endDate;
  String currencyCodeREG03;
  BigDecimal progressiveHie02ITM01;
  BigDecimal reportId;
  BigDecimal perc1Invoiced;
  BigDecimal perc2Invoiced;
  BigDecimal perc1Unsold;
  BigDecimal perc2Unsold;


  public CreateABCFilterVO() {
  }


  public String getCompanyCode() {
    return companyCode;
  }
  public String getCurrencyCodeREG03() {
    return currencyCodeREG03;
  }
  public java.sql.Date getEndDate() {
    return endDate;
  }
  public BigDecimal getProgressiveHie02ITM01() {
    return progressiveHie02ITM01;
  }
  public java.sql.Date getStartDate() {
    return startDate;
  }
  public String getWarehouseCode() {
    return warehouseCode;
  }
  public String getWarehouseDescription() {
    return warehouseDescription;
  }
  public void setWarehouseDescription(String warehouseDescription) {
    this.warehouseDescription = warehouseDescription;
  }
  public void setWarehouseCode(String warehouseCode) {
    this.warehouseCode = warehouseCode;
  }
  public void setStartDate(java.sql.Date startDate) {
    this.startDate = startDate;
  }
  public void setProgressiveHie02ITM01(BigDecimal progressiveHie02ITM01) {
    this.progressiveHie02ITM01 = progressiveHie02ITM01;
  }
  public void setEndDate(java.sql.Date endDate) {
    this.endDate = endDate;
  }
  public void setCurrencyCodeREG03(String currencyCodeREG03) {
    this.currencyCodeREG03 = currencyCodeREG03;
  }
  public void setCompanyCode(String companyCode) {
    this.companyCode = companyCode;
  }
  public BigDecimal getReportId() {
    return reportId;
  }
  public void setReportId(BigDecimal reportId) {
    this.reportId = reportId;
  }
  public BigDecimal getPerc1Invoiced() {
    return perc1Invoiced;
  }
  public BigDecimal getPerc1Unsold() {
    return perc1Unsold;
  }
  public BigDecimal getPerc2Invoiced() {
    return perc2Invoiced;
  }
  public BigDecimal getPerc2Unsold() {
    return perc2Unsold;
  }
  public void setPerc2Unsold(BigDecimal perc2Unsold) {
    this.perc2Unsold = perc2Unsold;
  }
  public void setPerc2Invoiced(BigDecimal perc2Invoiced) {
    this.perc2Invoiced = perc2Invoiced;
  }
  public void setPerc1Unsold(BigDecimal perc1Unsold) {
    this.perc1Unsold = perc1Unsold;
  }
  public void setPerc1Invoiced(BigDecimal perc1Invoiced) {
    this.perc1Invoiced = perc1Invoiced;
  }
}
