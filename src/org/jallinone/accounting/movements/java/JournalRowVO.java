package org.jallinone.accounting.movements.java;

import org.openswing.swing.message.receive.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value Object used to create an item in the journal.</p>
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
public class JournalRowVO extends ValueObjectImpl {

  private String companyCodeSys01ACC06;
  private java.math.BigDecimal progressiveAcc05ACC06;
  private java.math.BigDecimal itemYearAcc05ACC06;
  private java.math.BigDecimal progressiveACC06;
  private java.math.BigDecimal debitAmountACC06;
  private java.math.BigDecimal creditAmountACC06;
  private String accountCodeTypeACC06;
  private String accountCodeACC06;
  private String accountCodeAcc02ACC06;
  private String descriptionACC06;
  private String accountDescriptionACC06;


  public JournalRowVO() {
  }


  public java.math.BigDecimal getProgressiveACC06() {
    return progressiveACC06;
  }
  public void setProgressiveACC06(java.math.BigDecimal progressiveACC06) {
    this.progressiveACC06 = progressiveACC06;
  }
  public java.math.BigDecimal getDebitAmountACC06() {
    return debitAmountACC06;
  }
  public void setDebitAmountACC06(java.math.BigDecimal debitAmountACC06) {
    this.debitAmountACC06 = debitAmountACC06;
  }
  public java.math.BigDecimal getCreditAmountACC06() {
    return creditAmountACC06;
  }
  public void setCreditAmountACC06(java.math.BigDecimal creditAmountACC06) {
    this.creditAmountACC06 = creditAmountACC06;
  }
  public String getAccountCodeTypeACC06() {
    return accountCodeTypeACC06;
  }
  public void setAccountCodeTypeACC06(String accountCodeTypeACC06) {
    this.accountCodeTypeACC06 = accountCodeTypeACC06;
  }
  public String getAccountCodeACC06() {
    return accountCodeACC06;
  }
  public void setAccountCodeACC06(String accountCodeACC06) {
    this.accountCodeACC06 = accountCodeACC06;
  }
  public String getAccountCodeAcc02ACC06() {
    return accountCodeAcc02ACC06;
  }
  public void setAccountCodeAcc02ACC06(String accountCodeAcc02ACC06) {
    this.accountCodeAcc02ACC06 = accountCodeAcc02ACC06;
  }
  public String getDescriptionACC06() {
    return descriptionACC06;
  }
  public void setDescriptionACC06(String descriptionACC06) {
    this.descriptionACC06 = descriptionACC06;
  }
  public java.math.BigDecimal getProgressiveAcc05ACC06() {
    return progressiveAcc05ACC06;
  }
  public java.math.BigDecimal getItemYearAcc05ACC06() {
    return itemYearAcc05ACC06;
  }
  public String getCompanyCodeSys01ACC06() {
    return companyCodeSys01ACC06;
  }
  public void setCompanyCodeSys01ACC06(String companyCodeSys01ACC06) {
    this.companyCodeSys01ACC06 = companyCodeSys01ACC06;
  }
  public void setItemYearAcc05ACC06(java.math.BigDecimal itemYearAcc05ACC06) {
    this.itemYearAcc05ACC06 = itemYearAcc05ACC06;
  }
  public void setProgressiveAcc05ACC06(java.math.BigDecimal progressiveAcc05ACC06) {
    this.progressiveAcc05ACC06 = progressiveAcc05ACC06;
  }
  public String getAccountDescriptionACC06() {
    return accountDescriptionACC06;
  }
  public void setAccountDescriptionACC06(String accountDescriptionACC06) {
    this.accountDescriptionACC06 = accountDescriptionACC06;
  }

}
