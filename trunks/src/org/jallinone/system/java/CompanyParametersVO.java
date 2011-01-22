package org.jallinone.system.java;

import org.openswing.swing.message.receive.java.*;
import java.math.BigDecimal;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store company parameters, based on the specified company code.</p>
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
public class CompanyParametersVO extends ValueObjectImpl {

  private String companyCodeSys01SYS21;
  private String creditAccountCodeAcc02SAL07;
  private String itemsAccountCodeAcc02SAL07;
  private String activitiesAccountCodeAcc02SAL07;
  private String chargesAccountCodeAcc02SAL07;
  private String debitAccountCodeAcc02PUR01;
  private String costsAccountCodeAcc02PUR01;
  private String caseAccountCodeAcc02DOC21;
  private String creditAccountDescrSAL07;
  private String itemsAccountDescrSAL07;
  private String activitiesAccountDescrSAL07;
  private String chargesAccountDescrSAL07;
  private String debitAccountDescrPUR01;
  private String costsAccountDescrPUR01;
  private String caseAccountDescrDOC21;
  private String bankAccountDescrDOC21;
  private String vatEndorseAccountDescrDOC21;
  private String bankAccountCodeAcc02DOC21;
  private String vatEndorseAccountCodeAcc02DOC21;
  private String lossProfitEAccountCodeAcc02DOC21;
  private String lossProfitEAccountDescrDOC21;
  private String lossProfitPAccountCodeAcc02DOC21;
  private String lossProfitPAccountDescrDOC21;
  private String closingAccountCodeAcc02DOC21;
  private String closingAccountDescrDOC21;
  private String openingAccountCodeAcc02DOC21;
  private String openingAccountDescrDOC21;

  private java.sql.Timestamp morningStartHourSCH02;
  private java.sql.Timestamp morningEndHourSCH02;
  private java.sql.Timestamp afternoonStartHourSCH02;
  private java.sql.Timestamp afternoonEndHourSCH02;

  private String saleSectionalDOC01;

  private BigDecimal initialValueSYS21;

	private String roundingCostsAccountCodeAcc02DOC19;
	private String roundingCostsDescrDOC19;
	private String roundingProceedsAccountCodeAcc02DOC19;
	private String roundingProceedsDescrDOC19;


  public CompanyParametersVO() {
  }


  public String getCompanyCodeSys01SYS21() {
    return companyCodeSys01SYS21;
  }
  public void setCompanyCodeSys01SYS21(String companyCodeSys01SYS21) {
    this.companyCodeSys01SYS21 = companyCodeSys01SYS21;
  }
  public String getCreditAccountCodeAcc02SAL07() {
    return creditAccountCodeAcc02SAL07;
  }
  public void setCreditAccountCodeAcc02SAL07(String creditAccountCodeAcc02SAL07) {
    this.creditAccountCodeAcc02SAL07 = creditAccountCodeAcc02SAL07;
  }
  public String getItemsAccountCodeAcc02SAL07() {
    return itemsAccountCodeAcc02SAL07;
  }
  public void setItemsAccountCodeAcc02SAL07(String itemsAccountCodeAcc02SAL07) {
    this.itemsAccountCodeAcc02SAL07 = itemsAccountCodeAcc02SAL07;
  }
  public String getActivitiesAccountCodeAcc02SAL07() {
    return activitiesAccountCodeAcc02SAL07;
  }
  public void setActivitiesAccountCodeAcc02SAL07(String activitiesAccountCodeAcc02SAL07) {
    this.activitiesAccountCodeAcc02SAL07 = activitiesAccountCodeAcc02SAL07;
  }
  public String getChargesAccountCodeAcc02SAL07() {
    return chargesAccountCodeAcc02SAL07;
  }
  public void setChargesAccountCodeAcc02SAL07(String chargesAccountCodeAcc02SAL07) {
    this.chargesAccountCodeAcc02SAL07 = chargesAccountCodeAcc02SAL07;
  }
  public String getDebitAccountCodeAcc02PUR01() {
    return debitAccountCodeAcc02PUR01;
  }
  public void setDebitAccountCodeAcc02PUR01(String debitAccountCodeAcc02PUR01) {
    this.debitAccountCodeAcc02PUR01 = debitAccountCodeAcc02PUR01;
  }
  public String getCostsAccountCodeAcc02PUR01() {
    return costsAccountCodeAcc02PUR01;
  }
  public void setCostsAccountCodeAcc02PUR01(String costsAccountCodeAcc02PUR01) {
    this.costsAccountCodeAcc02PUR01 = costsAccountCodeAcc02PUR01;
  }
  public String getDebitAccountDescrPUR01() {
    return debitAccountDescrPUR01;
  }
  public String getItemsAccountDescrSAL07() {
    return itemsAccountDescrSAL07;
  }
  public String getCostsAccountDescrPUR01() {
    return costsAccountDescrPUR01;
  }
  public String getCreditAccountDescrSAL07() {
    return creditAccountDescrSAL07;
  }
  public String getChargesAccountDescrSAL07() {
    return chargesAccountDescrSAL07;
  }
  public String getActivitiesAccountDescrSAL07() {
    return activitiesAccountDescrSAL07;
  }
  public void setActivitiesAccountDescrSAL07(String activitiesAccountDescrSAL07) {
    this.activitiesAccountDescrSAL07 = activitiesAccountDescrSAL07;
  }
  public void setChargesAccountDescrSAL07(String chargesAccountDescrSAL07) {
    this.chargesAccountDescrSAL07 = chargesAccountDescrSAL07;
  }
  public void setCreditAccountDescrSAL07(String creditAccountDescrSAL07) {
    this.creditAccountDescrSAL07 = creditAccountDescrSAL07;
  }
  public void setCostsAccountDescrPUR01(String costsAccountDescrPUR01) {
    this.costsAccountDescrPUR01 = costsAccountDescrPUR01;
  }
  public void setDebitAccountDescrPUR01(String debitAccountDescrPUR01) {
    this.debitAccountDescrPUR01 = debitAccountDescrPUR01;
  }
  public void setItemsAccountDescrSAL07(String itemsAccountDescrSAL07) {
    this.itemsAccountDescrSAL07 = itemsAccountDescrSAL07;
  }
  public String getCaseAccountCodeAcc02DOC21() {
    return caseAccountCodeAcc02DOC21;
  }
  public String getCaseAccountDescrDOC21() {
    return caseAccountDescrDOC21;
  }
  public void setCaseAccountCodeAcc02DOC21(String caseAccountCodeAcc02DOC21) {
    this.caseAccountCodeAcc02DOC21 = caseAccountCodeAcc02DOC21;
  }
  public void setCaseAccountDescrDOC21(String caseAccountDescrDOC21) {
    this.caseAccountDescrDOC21 = caseAccountDescrDOC21;
  }
  public String getBankAccountDescrDOC21() {
    return bankAccountDescrDOC21;
  }
  public void setBankAccountDescrDOC21(String bankAccountDescrDOC21) {
    this.bankAccountDescrDOC21 = bankAccountDescrDOC21;
  }
  public void setVatEndorseAccountDescrDOC21(String vatEndorseAccountDescrDOC21) {
    this.vatEndorseAccountDescrDOC21 = vatEndorseAccountDescrDOC21;
  }
  public String getVatEndorseAccountDescrDOC21() {
    return vatEndorseAccountDescrDOC21;
  }
  public String getBankAccountCodeAcc02DOC21() {
    return bankAccountCodeAcc02DOC21;
  }
  public void setBankAccountCodeAcc02DOC21(String bankAccountCodeAcc02DOC21) {
    this.bankAccountCodeAcc02DOC21 = bankAccountCodeAcc02DOC21;
  }
  public void setVatEndorseAccountCodeAcc02DOC21(String vatEndorseAccountCodeAcc02DOC21) {
    this.vatEndorseAccountCodeAcc02DOC21 = vatEndorseAccountCodeAcc02DOC21;
  }
  public String getVatEndorseAccountCodeAcc02DOC21() {
    return vatEndorseAccountCodeAcc02DOC21;
  }
  public String getClosingAccountCodeAcc02DOC21() {
    return closingAccountCodeAcc02DOC21;
  }
  public String getClosingAccountDescrDOC21() {
    return closingAccountDescrDOC21;
  }
  public String getLossProfitEAccountCodeAcc02DOC21() {
    return lossProfitEAccountCodeAcc02DOC21;
  }
  public String getLossProfitEAccountDescrDOC21() {
    return lossProfitEAccountDescrDOC21;
  }
  public String getLossProfitPAccountCodeAcc02DOC21() {
    return lossProfitPAccountCodeAcc02DOC21;
  }
  public String getLossProfitPAccountDescrDOC21() {
    return lossProfitPAccountDescrDOC21;
  }
  public String getOpeningAccountCodeAcc02DOC21() {
    return openingAccountCodeAcc02DOC21;
  }
  public String getOpeningAccountDescrDOC21() {
    return openingAccountDescrDOC21;
  }
  public void setOpeningAccountDescrDOC21(String openingAccountDescrDOC21) {
    this.openingAccountDescrDOC21 = openingAccountDescrDOC21;
  }
  public void setLossProfitPAccountDescrDOC21(String lossProfitPAccountDescrDOC21) {
    this.lossProfitPAccountDescrDOC21 = lossProfitPAccountDescrDOC21;
  }
  public void setLossProfitPAccountCodeAcc02DOC21(String lossProfitPAccountCodeAcc02DOC21) {
    this.lossProfitPAccountCodeAcc02DOC21 = lossProfitPAccountCodeAcc02DOC21;
  }
  public void setLossProfitEAccountDescrDOC21(String lossProfitEAccountDescrDOC21) {
    this.lossProfitEAccountDescrDOC21 = lossProfitEAccountDescrDOC21;
  }
  public void setLossProfitEAccountCodeAcc02DOC21(String lossProfitEAccountCodeAcc02DOC21) {
    this.lossProfitEAccountCodeAcc02DOC21 = lossProfitEAccountCodeAcc02DOC21;
  }
  public void setClosingAccountDescrDOC21(String closingAccountDescrDOC21) {
    this.closingAccountDescrDOC21 = closingAccountDescrDOC21;
  }
  public void setClosingAccountCodeAcc02DOC21(String closingAccountCodeAcc02DOC21) {
    this.closingAccountCodeAcc02DOC21 = closingAccountCodeAcc02DOC21;
  }
  public void setOpeningAccountCodeAcc02DOC21(String openingAccountCodeAcc02DOC21) {
    this.openingAccountCodeAcc02DOC21 = openingAccountCodeAcc02DOC21;
  }
  public java.sql.Timestamp getAfternoonEndHourSCH02() {
    return afternoonEndHourSCH02;
  }
  public java.sql.Timestamp getAfternoonStartHourSCH02() {
    return afternoonStartHourSCH02;
  }
  public void setAfternoonEndHourSCH02(java.sql.Timestamp afternoonEndHourSCH02) {
    this.afternoonEndHourSCH02 = afternoonEndHourSCH02;
  }
  public void setAfternoonStartHourSCH02(java.sql.Timestamp afternoonStartHourSCH02) {
    this.afternoonStartHourSCH02 = afternoonStartHourSCH02;
  }
  public java.sql.Timestamp getMorningEndHourSCH02() {
    return morningEndHourSCH02;
  }
  public java.sql.Timestamp getMorningStartHourSCH02() {
    return morningStartHourSCH02;
  }
  public void setMorningEndHourSCH02(java.sql.Timestamp morningEndHourSCH02) {
    this.morningEndHourSCH02 = morningEndHourSCH02;
  }
  public void setMorningStartHourSCH02(java.sql.Timestamp morningStartHourSCH02) {
    this.morningStartHourSCH02 = morningStartHourSCH02;
  }
  public String getSaleSectionalDOC01() {
    return saleSectionalDOC01;
  }
  public void setSaleSectionalDOC01(String saleSectionalDOC01) {
    this.saleSectionalDOC01 = saleSectionalDOC01;
  }
  public BigDecimal getInitialValueSYS21() {
    return initialValueSYS21;
  }
  public void setInitialValueSYS21(BigDecimal initialValueSYS21) {
    this.initialValueSYS21 = initialValueSYS21;
  }
  public String getRoundingCostsAccountCodeAcc02DOC19() {
    return roundingCostsAccountCodeAcc02DOC19;
  }
  public String getRoundingCostsDescrDOC19() {
    return roundingCostsDescrDOC19;
  }
  public String getRoundingProceedsAccountCodeAcc02DOC19() {
    return roundingProceedsAccountCodeAcc02DOC19;
  }
  public String getRoundingProceedsDescrDOC19() {
    return roundingProceedsDescrDOC19;
  }
  public void setRoundingProceedsDescrDOC19(String roundingProceedsDescrDOC19) {
    this.roundingProceedsDescrDOC19 = roundingProceedsDescrDOC19;
  }
  public void setRoundingProceedsAccountCodeAcc02DOC19(String roundingProceedsAccountCodeAcc02DOC19) {
    this.roundingProceedsAccountCodeAcc02DOC19 = roundingProceedsAccountCodeAcc02DOC19;
  }
  public void setRoundingCostsDescrDOC19(String roundingCostsDescrDOC19) {
    this.roundingCostsDescrDOC19 = roundingCostsDescrDOC19;
  }
  public void setRoundingCostsAccountCodeAcc02DOC19(String roundingCostsAccountCodeAcc02DOC19) {
    this.roundingCostsAccountCodeAcc02DOC19 = roundingCostsAccountCodeAcc02DOC19;
  }

}
