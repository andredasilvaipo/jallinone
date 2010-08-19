package org.jallinone.accounting.accountingmotives.java;

import org.jallinone.system.customizations.java.BaseValueObject;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store accountingMotive info.</p>
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
public class AccountingMotiveVO extends BaseValueObject {

  private String accountingMotiveCodeACC03;
  private java.math.BigDecimal progressiveSys10ACC03;
  private String enabledACC03;
  private String descriptionSYS10;
  private Boolean canDelACC03;


  public AccountingMotiveVO() {
  }


  public String getAccountingMotiveCodeACC03() {
    return accountingMotiveCodeACC03;
  }
  public void setAccountingMotiveCodeACC03(String accountingMotiveCodeACC03) {
    this.accountingMotiveCodeACC03 = accountingMotiveCodeACC03;
  }
  public java.math.BigDecimal getProgressiveSys10ACC03() {
    return progressiveSys10ACC03;
  }
  public void setProgressiveSys10ACC03(java.math.BigDecimal progressiveSys10ACC03) {
    this.progressiveSys10ACC03 = progressiveSys10ACC03;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public String getEnabledACC03() {
    return enabledACC03;
  }
  public void setEnabledACC03(String enabledACC03) {
    this.enabledACC03 = enabledACC03;
  }
  public Boolean getCanDelACC03() {
    return canDelACC03;
  }
  public void setCanDelACC03(Boolean canDelACC03) {
    this.canDelACC03 = canDelACC03;
  }

}