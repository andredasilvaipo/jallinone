package org.jallinone.accounting.ledger.java;

import org.jallinone.system.customizations.java.BaseValueObject;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store ledger info.</p>
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
public class LedgerVO extends BaseValueObject {
  private String ledgerCodeACC01;
  private java.math.BigDecimal progressiveSys10ACC01;
  private String enabledACC01;
  private String descriptionSYS10;
  private String companyCodeSys01ACC01;
  private String accountTypeACC01;


  public LedgerVO() {
  }


  public String getLedgerCodeACC01() {
    return ledgerCodeACC01;
  }
  public void setLedgerCodeACC01(String ledgerCodeACC01) {
    this.ledgerCodeACC01 = ledgerCodeACC01;
  }
  public java.math.BigDecimal getProgressiveSys10ACC01() {
    return progressiveSys10ACC01;
  }
  public void setProgressiveSys10ACC01(java.math.BigDecimal progressiveSys10ACC01) {
    this.progressiveSys10ACC01 = progressiveSys10ACC01;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public String getEnabledACC01() {
    return enabledACC01;
  }
  public void setEnabledACC01(String enabledACC01) {
    this.enabledACC01 = enabledACC01;
  }
  public String getCompanyCodeSys01ACC01() {
    return companyCodeSys01ACC01;
  }
  public void setCompanyCodeSys01ACC01(String companyCodeSys01ACC01) {
    this.companyCodeSys01ACC01 = companyCodeSys01ACC01;
  }
  public String getAccountTypeACC01() {
    return accountTypeACC01;
  }
  public void setAccountTypeACC01(String accountTypeACC01) {
    this.accountTypeACC01 = accountTypeACC01;
  }

}