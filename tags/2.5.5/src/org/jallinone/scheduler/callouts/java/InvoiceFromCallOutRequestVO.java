package org.jallinone.scheduler.callouts.java;

import org.jallinone.system.customizations.java.BaseValueObject;
import org.jallinone.scheduler.activities.java.ScheduledActivityVO;
import org.jallinone.sales.documents.java.DetailSaleDocVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store call-out request v.o., invoice header v.o. and customer accounts.</p>
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
public class InvoiceFromCallOutRequestVO extends BaseValueObject {

  private DetailCallOutRequestVO callOutRequest;
  private DetailSaleDocVO saleVO = null;
  private String creditAccountCodeAcc02SAL07;
  private String itemsAccountCodeAcc02SAL07;
  private String activitiesAccountCodeAcc02SAL07;
  private String chargesAccountCodeAcc02SAL07;
  private boolean customerAlreadyExists;
  private ScheduledActivityVO actVO;


  public InvoiceFromCallOutRequestVO() {
  }


  public DetailCallOutRequestVO getCallOutRequest() {
    return callOutRequest;
  }
  public void setCallOutRequest(DetailCallOutRequestVO callOutRequest) {
    this.callOutRequest = callOutRequest;
  }
  public DetailSaleDocVO getSaleVO() {
    return saleVO;
  }
  public void setSaleVO(DetailSaleDocVO saleVO) {
    this.saleVO = saleVO;
  }
  public String getChargesAccountCodeAcc02SAL07() {
    return chargesAccountCodeAcc02SAL07;
  }
  public String getCreditAccountCodeAcc02SAL07() {
    return creditAccountCodeAcc02SAL07;
  }
  public String getItemsAccountCodeAcc02SAL07() {
    return itemsAccountCodeAcc02SAL07;
  }
  public String getActivitiesAccountCodeAcc02SAL07() {
    return activitiesAccountCodeAcc02SAL07;
  }
  public void setActivitiesAccountCodeAcc02SAL07(String activitiesAccountCodeAcc02SAL07) {
    this.activitiesAccountCodeAcc02SAL07 = activitiesAccountCodeAcc02SAL07;
  }
  public void setChargesAccountCodeAcc02SAL07(String chargesAccountCodeAcc02SAL07) {
    this.chargesAccountCodeAcc02SAL07 = chargesAccountCodeAcc02SAL07;
  }
  public void setCreditAccountCodeAcc02SAL07(String creditAccountCodeAcc02SAL07) {
    this.creditAccountCodeAcc02SAL07 = creditAccountCodeAcc02SAL07;
  }
  public void setItemsAccountCodeAcc02SAL07(String itemsAccountCodeAcc02SAL07) {
    this.itemsAccountCodeAcc02SAL07 = itemsAccountCodeAcc02SAL07;
  }
  public boolean isCustomerAlreadyExists() {
    return customerAlreadyExists;
  }
  public void setCustomerAlreadyExists(boolean customerAlreadyExists) {
    this.customerAlreadyExists = customerAlreadyExists;
  }
  public ScheduledActivityVO getActVO() {
    return actVO;
  }
  public void setActVO(ScheduledActivityVO actVO) {
    this.actVO = actVO;
  }




}