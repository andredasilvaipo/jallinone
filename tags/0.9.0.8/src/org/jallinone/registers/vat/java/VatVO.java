package org.jallinone.registers.vat.java;

import org.jallinone.system.customizations.java.BaseValueObject;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store vat info.</p>
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
public class VatVO extends BaseValueObject {
  private String vatCodeREG01;
  private java.math.BigDecimal progressiveSys10REG01;
  private String enabledREG01;
  private java.math.BigDecimal valueREG01;
  private String descriptionSYS10;
  private java.math.BigDecimal deductibleREG01;


  public VatVO() {
  }
  public String getVatCodeREG01() {
    return vatCodeREG01;
  }
  public void setVatCodeREG01(String vatCodeREG01) {
    this.vatCodeREG01 = vatCodeREG01;
  }
  public java.math.BigDecimal getProgressiveSys10REG01() {
    return progressiveSys10REG01;
  }
  public void setProgressiveSys10REG01(java.math.BigDecimal progressiveSys10REG01) {
    this.progressiveSys10REG01 = progressiveSys10REG01;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public String getEnabledREG01() {
    return enabledREG01;
  }
  public void setEnabledREG01(String enabledREG01) {
    this.enabledREG01 = enabledREG01;
  }
  public java.math.BigDecimal getValueREG01() {
    return valueREG01;
  }
  public void setValueREG01(java.math.BigDecimal valueREG01) {
    this.valueREG01 = valueREG01;
  }
  public java.math.BigDecimal getDeductibleREG01() {
    return deductibleREG01;
  }
  public void setDeductibleREG01(java.math.BigDecimal deductibleREG01) {
    this.deductibleREG01 = deductibleREG01;
  }

}