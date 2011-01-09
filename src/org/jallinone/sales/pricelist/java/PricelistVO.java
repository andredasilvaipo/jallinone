package org.jallinone.sales.pricelist.java;

import org.jallinone.system.customizations.java.BaseValueObject;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a sale price list.</p>
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
public class PricelistVO extends BaseValueObject {

  private String pricelistCodeSAL01;
  private String companyCodeSys01SAL01;
  private String descriptionSYS10;
  private java.math.BigDecimal progressiveSys10SAL01;
  private String currencyCodeReg03SAL01;
  private String oldPricelistCodeSal01SAL02;


  public PricelistVO() {
  }
  public String getPricelistCodeSAL01() {
    return pricelistCodeSAL01;
  }
  public void setPricelistCodeSAL01(String pricelistCodeSAL01) {
    this.pricelistCodeSAL01 = pricelistCodeSAL01;
  }
  public String getCompanyCodeSys01SAL01() {
    return companyCodeSys01SAL01;
  }
  public void setCompanyCodeSys01SAL01(String companyCodeSys01SAL01) {
    this.companyCodeSys01SAL01 = companyCodeSys01SAL01;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public java.math.BigDecimal getProgressiveSys10SAL01() {
    return progressiveSys10SAL01;
  }
  public void setProgressiveSys10SAL01(java.math.BigDecimal progressiveSys10SAL01) {
    this.progressiveSys10SAL01 = progressiveSys10SAL01;
  }
  public String getCurrencyCodeReg03SAL01() {
    return currencyCodeReg03SAL01;
  }
  public void setCurrencyCodeReg03SAL01(String currencyCodeReg03SAL01) {
    this.currencyCodeReg03SAL01 = currencyCodeReg03SAL01;
  }
  public String getOldPricelistCodeSal01SAL02() {
    return oldPricelistCodeSal01SAL02;
  }
  public void setOldPricelistCodeSal01SAL02(String oldPricelistCodeSal01SAL02) {
    this.oldPricelistCodeSal01SAL02 = oldPricelistCodeSal01SAL02;
  }

}