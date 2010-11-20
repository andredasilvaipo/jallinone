package org.jallinone.registers.currency.java;

import org.jallinone.system.customizations.java.BaseValueObject;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store currency info.</p>
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
public class CurrencyVO extends BaseValueObject {

  private String currencyCodeREG03;
  private String enabledREG03;
  private String currencySymbolREG03;
  private String decimalSymbolREG03;
  private String thousandSymbolREG03;
  private java.math.BigDecimal decimalsREG03;


  public CurrencyVO() {
  }


  public String getCurrencyCodeREG03() {
    return currencyCodeREG03;
  }
  public void setCurrencyCodeREG03(String currencyCodeREG03) {
    this.currencyCodeREG03 = currencyCodeREG03;
  }
  public String getCurrencySymbolREG03() {
    return currencySymbolREG03;
  }
  public void setCurrencySymbolREG03(String currencySymbolREG03) {
    this.currencySymbolREG03 = currencySymbolREG03;
  }
  public String getEnabledREG03() {
    return enabledREG03;
  }
  public void setEnabledREG03(String enabledREG03) {
    this.enabledREG03 = enabledREG03;
  }
  public String getDecimalSymbolREG03() {
    return decimalSymbolREG03;
  }
  public void setDecimalSymbolREG03(String decimalSymbolREG03) {
    this.decimalSymbolREG03 = decimalSymbolREG03;
  }
  public String getThousandSymbolREG03() {
    return thousandSymbolREG03;
  }
  public void setThousandSymbolREG03(String thousandSymbolREG03) {
    this.thousandSymbolREG03 = thousandSymbolREG03;
  }
  public java.math.BigDecimal getDecimalsREG03() {
    return decimalsREG03;
  }
  public void setDecimalsREG03(java.math.BigDecimal decimalsREG03) {
    this.decimalsREG03 = decimalsREG03;
  }

}