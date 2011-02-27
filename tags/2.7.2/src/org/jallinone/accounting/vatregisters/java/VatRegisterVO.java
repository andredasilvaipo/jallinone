package org.jallinone.accounting.vatregisters.java;

import org.jallinone.system.customizations.java.BaseValueObject;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store vat register info.</p>
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
public class VatRegisterVO extends BaseValueObject {

  private String registerCodeACC04;
  private java.math.BigDecimal progressiveSys10ACC04;
  private String enabledACC04;
  private String descriptionSYS10;
  private String registerTypeACC04;
  private Boolean readOnlyACC04;
  private java.math.BigDecimal lastRecordNumberACC04;
  private String accountCodeAcc02ACC04;
  private String companyCodeSys01ACC04;
  private String accountDescriptionACC04;


  public VatRegisterVO() {
  }


  public String getRegisterCodeACC04() {
    return registerCodeACC04;
  }
  public void setRegisterCodeACC04(String registerCodeACC04) {
    this.registerCodeACC04 = registerCodeACC04;
  }
  public java.math.BigDecimal getProgressiveSys10ACC04() {
    return progressiveSys10ACC04;
  }
  public void setProgressiveSys10ACC04(java.math.BigDecimal progressiveSys10ACC04) {
    this.progressiveSys10ACC04 = progressiveSys10ACC04;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public String getEnabledACC04() {
    return enabledACC04;
  }
  public void setEnabledACC04(String enabledACC04) {
    this.enabledACC04 = enabledACC04;
  }
  public String getRegisterTypeACC04() {
    return registerTypeACC04;
  }
  public void setRegisterTypeACC04(String registerTypeACC04) {
    this.registerTypeACC04 = registerTypeACC04;
  }
  public Boolean getReadOnlyACC04() {
    return readOnlyACC04;
  }
  public void setReadOnlyACC04(Boolean readOnlyACC04) {
    this.readOnlyACC04 = readOnlyACC04;
  }
  public java.math.BigDecimal getLastRecordNumberACC04() {
    return lastRecordNumberACC04;
  }
  public void setLastRecordNumberACC04(java.math.BigDecimal lastRecordNumberACC04) {
    this.lastRecordNumberACC04 = lastRecordNumberACC04;
  }
  public String getAccountCodeAcc02ACC04() {
    return accountCodeAcc02ACC04;
  }
  public void setAccountCodeAcc02ACC04(String accountCodeAcc02ACC04) {
    this.accountCodeAcc02ACC04 = accountCodeAcc02ACC04;
  }
  public String getCompanyCodeSys01ACC04() {
    return companyCodeSys01ACC04;
  }
  public void setCompanyCodeSys01ACC04(String companyCodeSys01ACC04) {
    this.companyCodeSys01ACC04 = companyCodeSys01ACC04;
  }
  public String getAccountDescriptionACC04() {
    return accountDescriptionACC04;
  }
  public void setAccountDescriptionACC04(String accountDescriptionACC04) {
    this.accountDescriptionACC04 = accountDescriptionACC04;
  }

}