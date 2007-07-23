package org.jallinone.production.machineries.java;

import org.jallinone.system.customizations.java.BaseValueObject;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a machinery.</p>
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
public class MachineryVO extends BaseValueObject {

  private String companyCodeSys01PRO03;
  private String descriptionSYS10;
  private String machineryCodePRO03;
  private String currencyCodeReg03PRO03;
  private String finiteCapacityPRO03;
  private String enabledPRO03;
  private java.math.BigDecimal progressiveSys10PRO03;
  private java.math.BigDecimal valuePRO03;
  private java.math.BigDecimal durationPRO03;


  public MachineryVO() {
  }


  public String getCompanyCodeSys01PRO03() {
    return companyCodeSys01PRO03;
  }
  public void setCompanyCodeSys01PRO03(String companyCodeSys01PRO03) {
    this.companyCodeSys01PRO03 = companyCodeSys01PRO03;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public String getMachineryCodePRO03() {
    return machineryCodePRO03;
  }
  public void setMachineryCodePRO03(String machineryCodePRO03) {
    this.machineryCodePRO03 = machineryCodePRO03;
  }
  public String getCurrencyCodeReg03PRO03() {
    return currencyCodeReg03PRO03;
  }
  public void setCurrencyCodeReg03PRO03(String currencyCodeReg03PRO03) {
    this.currencyCodeReg03PRO03 = currencyCodeReg03PRO03;
  }
  public String getFiniteCapacityPRO03() {
    return finiteCapacityPRO03;
  }
  public void setFiniteCapacityPRO03(String finiteCapacityPRO03) {
    this.finiteCapacityPRO03 = finiteCapacityPRO03;
  }
  public String getEnabledPRO03() {
    return enabledPRO03;
  }
  public void setEnabledPRO03(String enabledPRO03) {
    this.enabledPRO03 = enabledPRO03;
  }
  public java.math.BigDecimal getProgressiveSys10PRO03() {
    return progressiveSys10PRO03;
  }
  public void setProgressiveSys10PRO03(java.math.BigDecimal progressiveSys10PRO03) {
    this.progressiveSys10PRO03 = progressiveSys10PRO03;
  }
  public java.math.BigDecimal getValuePRO03() {
    return valuePRO03;
  }
  public void setValuePRO03(java.math.BigDecimal valuePRO03) {
    this.valuePRO03 = valuePRO03;
  }
  public java.math.BigDecimal getDurationPRO03() {
    return durationPRO03;
  }
  public void setDurationPRO03(java.math.BigDecimal durationPRO03) {
    this.durationPRO03 = durationPRO03;
  }

}