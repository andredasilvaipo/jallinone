package org.jallinone.expirations.java;

import org.openswing.swing.message.receive.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store the detail of a payment related to an expiration for the same customer/supplier.</p>
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
public class PaymentDistributionVO extends ValueObjectImpl  {

		private String companyCodeSys01DOC28;
		private java.math.BigDecimal progressiveDoc27DOC28;
		private java.math.BigDecimal progressiveDoc19DOC28;
		private java.math.BigDecimal paymentValueDOC28;
		private Boolean payedDOC28;
		private String docTypeDOC19;
		private java.math.BigDecimal valueDOC19;
		private java.math.BigDecimal alreadyPayedDOC19;
		private String roundingAccountCodeAcc02DOC19;
  private String descriptionDOC19;


		public PaymentDistributionVO() {
		}


  public String getCompanyCodeSys01DOC28() {
    return companyCodeSys01DOC28;
  }
  public String getDescriptionDOC19() {
    return descriptionDOC19;
  }
  public java.math.BigDecimal getPaymentValueDOC28() {
    return paymentValueDOC28;
  }
  public java.math.BigDecimal getProgressiveDoc19DOC28() {
    return progressiveDoc19DOC28;
  }
  public java.math.BigDecimal getProgressiveDoc27DOC28() {
    return progressiveDoc27DOC28;
  }
  public void setProgressiveDoc27DOC28(java.math.BigDecimal progressiveDoc27DOC28) {
    this.progressiveDoc27DOC28 = progressiveDoc27DOC28;
  }
  public void setProgressiveDoc19DOC28(java.math.BigDecimal progressiveDoc19DOC28) {
    this.progressiveDoc19DOC28 = progressiveDoc19DOC28;
  }
  public void setPaymentValueDOC28(java.math.BigDecimal paymentValueDOC28) {
    this.paymentValueDOC28 = paymentValueDOC28;
  }
  public void setDescriptionDOC19(String descriptionDOC19) {
    this.descriptionDOC19 = descriptionDOC19;
  }
  public void setCompanyCodeSys01DOC28(String companyCodeSys01DOC28) {
    this.companyCodeSys01DOC28 = companyCodeSys01DOC28;
  }
  public Boolean getPayedDOC28() {
    return payedDOC28;
  }
  public void setPayedDOC28(Boolean payedDOC28) {
    this.payedDOC28 = payedDOC28;
  }
  public String getDocTypeDOC19() {
    return docTypeDOC19;
  }
  public void setDocTypeDOC19(String docTypeDOC19) {
    this.docTypeDOC19 = docTypeDOC19;
  }
  public java.math.BigDecimal getValueDOC19() {
    return valueDOC19;
  }
  public void setValueDOC19(java.math.BigDecimal valueDOC19) {
    this.valueDOC19 = valueDOC19;
  }
  public void setAlreadyPayedDOC19(java.math.BigDecimal alreadyPayedDOC19) {
    this.alreadyPayedDOC19 = alreadyPayedDOC19;
  }
  public java.math.BigDecimal getAlreadyPayedDOC19() {
    return alreadyPayedDOC19;
  }
  public String getRoundingAccountCodeAcc02DOC19() {
    return roundingAccountCodeAcc02DOC19;
  }
  public void setRoundingAccountCodeAcc02DOC19(String roundingAccountCodeAcc02DOC19) {
    this.roundingAccountCodeAcc02DOC19 = roundingAccountCodeAcc02DOC19;
  }


}
