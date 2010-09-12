package org.jallinone.ordertracking.java;

import org.openswing.swing.message.receive.java.ValueObjectImpl;
import java.math.BigDecimal;
import java.sql.Date;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used by the filter panel of order tracking frame.</p>
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
public class OrderTrackingFilterVO extends ValueObjectImpl {

  private String docType;
  private String customerCode;
  private String supplierCode;
  private String customerName1;
  private String customerName2;
  private String supplierName1;
  private BigDecimal docYear;
  private Date docDate;
  private String docState;
  private BigDecimal progressiveREG04;


  public OrderTrackingFilterVO() {
  }



  public String getCustomerCode() {
    return customerCode;
  }
  public String getCustomerName1() {
    return customerName1;
  }
  public String getCustomerName2() {
    return customerName2;
  }
  public Date getDocDate() {
    return docDate;
  }
  public String getDocType() {
    return docType;
  }
  public BigDecimal getDocYear() {
    return docYear;
  }
  public String getSupplierCode() {
    return supplierCode;
  }
  public String getSupplierName1() {
    return supplierName1;
  }
  public void setSupplierName1(String supplierName1) {
    this.supplierName1 = supplierName1;
  }
  public void setSupplierCode(String supplierCode) {
    this.supplierCode = supplierCode;
  }
  public void setDocYear(BigDecimal docYear) {
    this.docYear = docYear;
  }
  public void setDocType(String docType) {
    this.docType = docType;
  }
  public void setDocDate(Date docDate) {
    this.docDate = docDate;
  }
  public void setCustomerName2(String customerName2) {
    this.customerName2 = customerName2;
  }
  public void setCustomerName1(String customerName1) {
    this.customerName1 = customerName1;
  }
  public void setCustomerCode(String customerCode) {
    this.customerCode = customerCode;
  }
  public String getDocState() {
    return docState;
  }
  public void setDocState(String docState) {
    this.docState = docState;
  }
  public BigDecimal getProgressiveREG04() {
    return progressiveREG04;
  }
  public void setProgressiveREG04(BigDecimal progressiveREG04) {
    this.progressiveREG04 = progressiveREG04;
  }
}
