package org.jallinone.accounting.accounts.java;

import org.jallinone.system.customizations.java.BaseValueObject;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store account info.</p>
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
public class AccountVO extends BaseValueObject {

  private String accountCodeACC02;
  private java.math.BigDecimal progressiveSys10ACC02;
  private String enabledACC02;
  private String descriptionSYS10;
  private String companyCodeSys01ACC02;
  private String ledgerCodeAcc01ACC02;
  private String accountTypeACC02;
  private String ledgerDescriptionACC02;
  private Boolean canDelACC02;


  public AccountVO() {
  }


  public String getAccountCodeACC02() {
    return accountCodeACC02;
  }
  public void setAccountCodeACC02(String accountCodeACC02) {
    this.accountCodeACC02 = accountCodeACC02;
  }
  public java.math.BigDecimal getProgressiveSys10ACC02() {
    return progressiveSys10ACC02;
  }
  public void setProgressiveSys10ACC02(java.math.BigDecimal progressiveSys10ACC02) {
    this.progressiveSys10ACC02 = progressiveSys10ACC02;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public String getEnabledACC02() {
    return enabledACC02;
  }
  public void setEnabledACC02(String enabledACC02) {
    this.enabledACC02 = enabledACC02;
  }
  public String getCompanyCodeSys01ACC02() {
    return companyCodeSys01ACC02;
  }
  public void setCompanyCodeSys01ACC02(String companyCodeSys01ACC02) {
    this.companyCodeSys01ACC02 = companyCodeSys01ACC02;
  }
  public String getLedgerCodeAcc01ACC02() {
    return ledgerCodeAcc01ACC02;
  }
  public void setLedgerCodeAcc01ACC02(String ledgerCodeAcc01ACC02) {
    this.ledgerCodeAcc01ACC02 = ledgerCodeAcc01ACC02;
  }
  public String getAccountTypeACC02() {
    return accountTypeACC02;
  }
  public void setAccountTypeACC02(String accountTypeACC02) {
    this.accountTypeACC02 = accountTypeACC02;
  }
  public String getLedgerDescriptionACC02() {
    return ledgerDescriptionACC02;
  }
  public void setLedgerDescriptionACC02(String ledgerDescriptionACC02) {
    this.ledgerDescriptionACC02 = ledgerDescriptionACC02;
  }
  public Boolean getCanDelACC02() {
    return canDelACC02;
  }
  public void setCanDelACC02(Boolean canDelACC02) {
    this.canDelACC02 = canDelACC02;
  }

}