package org.jallinone.sales.pricelist.java;

import java.io.Serializable;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Changes that will be applied to the prices of the specified pricelist.</p>
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
public class PricelistChanges implements Serializable {


  private String companyCodeSys01SAL02;
  private String pricelistCodeSal01SAL02;
  private java.math.BigDecimal deltaValue;
  private java.math.BigDecimal percentage;
  private boolean truncateDecimals;
  private java.sql.Date startDate;
  private java.sql.Date endDate;


  public PricelistChanges() {}
  
  
  public PricelistChanges(String companyCodeSys01SAL02,String pricelistCodeSal01SAL02) {
    this.companyCodeSys01SAL02 = companyCodeSys01SAL02;
    this.pricelistCodeSal01SAL02 = pricelistCodeSal01SAL02;
  }


  public String getCompanyCodeSys01SAL02() {
    return companyCodeSys01SAL02;
  }
  public String getPricelistCodeSal01SAL02() {
    return pricelistCodeSal01SAL02;
  }
  public java.math.BigDecimal getDeltaValue() {
    return deltaValue;
  }
  public void setDeltaValue(java.math.BigDecimal deltaValue) {
    this.deltaValue = deltaValue;
  }
  public java.math.BigDecimal getPercentage() {
    return percentage;
  }
  public void setPercentage(java.math.BigDecimal percentage) {
    this.percentage = percentage;
  }
  public boolean isTruncateDecimals() {
    return truncateDecimals;
  }
  public void setTruncateDecimals(boolean truncateDecimals) {
    this.truncateDecimals = truncateDecimals;
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


  public void setCompanyCodeSys01SAL02(String companyCodeSys01SAL02) {
	  this.companyCodeSys01SAL02 = companyCodeSys01SAL02;
  }


  public void setPricelistCodeSal01SAL02(String pricelistCodeSal01SAL02) {
	  this.pricelistCodeSal01SAL02 = pricelistCodeSal01SAL02;
  }


  
  
  
}