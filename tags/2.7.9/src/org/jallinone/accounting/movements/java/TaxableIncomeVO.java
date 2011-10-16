package org.jallinone.accounting.movements.java;

import org.openswing.swing.message.receive.java.*;
import org.jallinone.system.customizations.java.BaseValueObject;
import java.util.ArrayList;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store taxable income + vat value + vat code for a sale order,
 * where all sale doc rows are grouped by vat code.</p>
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
public class TaxableIncomeVO extends BaseValueObject {

  private String vatCode;
  private String vatDescription;
  private java.math.BigDecimal taxableIncome;
  private java.math.BigDecimal vatValue;

  public static final int ITEM_ROW_TYPE = 0;
  public static final int ACTIVITY_ROW_TYPE = 1;
  public static final int CHARGE_ROW_TYPE = 2;
  private int rowType;


  public TaxableIncomeVO() {
  }
  public java.math.BigDecimal getTaxableIncome() {
    return taxableIncome;
  }
  public String getVatCode() {
    return vatCode;
  }
  public String getVatDescription() {
    return vatDescription;
  }
  public java.math.BigDecimal getVatValue() {
    return vatValue;
  }
  public void setVatValue(java.math.BigDecimal vatValue) {
    this.vatValue = vatValue;
  }
  public void setVatDescription(String vatDescription) {
    this.vatDescription = vatDescription;
  }
  public void setVatCode(String vatCode) {
    this.vatCode = vatCode;
  }
  public void setTaxableIncome(java.math.BigDecimal taxableIncome) {
    this.taxableIncome = taxableIncome;
  }
  public int getRowType() {
    return rowType;
  }
  public void setRowType(int rowType) {
    this.rowType = rowType;
  }


}
