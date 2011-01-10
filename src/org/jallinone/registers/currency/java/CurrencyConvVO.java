package org.jallinone.registers.currency.java;

import org.openswing.swing.message.receive.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store currency conversion info.</p>
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
public class CurrencyConvVO extends ValueObjectImpl {

  private String currencyCodeReg03REG06;
  private String currencyCode2Reg03REG06;
  private java.math.BigDecimal valueREG06;


  public CurrencyConvVO() {
  }


  public String getCurrencyCodeReg03REG06() {
    return currencyCodeReg03REG06;
  }
  public void setCurrencyCodeReg03REG06(String currencyCodeReg03REG06) {
    this.currencyCodeReg03REG06 = currencyCodeReg03REG06;
  }
  public String getCurrencyCode2Reg03REG06() {
    return currencyCode2Reg03REG06;
  }
  public void setCurrencyCode2Reg03REG06(String currencyCode2Reg03REG06) {
    this.currencyCode2Reg03REG06 = currencyCode2Reg03REG06;
  }
  public java.math.BigDecimal getValueREG06() {
    return valueREG06;
  }
  public void setValueREG06(java.math.BigDecimal valueREG06) {
    this.valueREG06 = valueREG06;
  }

}
