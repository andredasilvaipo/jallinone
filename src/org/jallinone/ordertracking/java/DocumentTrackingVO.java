package org.jallinone.ordertracking.java;

import org.openswing.swing.message.receive.java.ValueObjectImpl;
import java.math.BigDecimal;
import java.sql.Date;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used by the order tracking grid frame.</p>
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
public class DocumentTrackingVO extends ValueObjectImpl {

  private String companyCodeSYS01;
  private String docType;
  private String docTypeDescr;
  private BigDecimal docYear;
  private BigDecimal docNumber;
  private String customerSupplierCode;
  private String name_1;
  private String name_2;
  private BigDecimal docSequence;
  private String docState;
  private Date docDate;


  public DocumentTrackingVO() {
  }


  public String getCompanyCodeSYS01() {
    return companyCodeSYS01;
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
  public void setDocYear(BigDecimal docYear) {
    this.docYear = docYear;
  }
  public void setDocType(String docType) {
    this.docType = docType;
  }
  public void setDocDate(Date docDate) {
    this.docDate = docDate;
  }
  public void setCompanyCodeSYS01(String companyCodeSYS01) {
    this.companyCodeSYS01 = companyCodeSYS01;
  }
  public BigDecimal getDocNumber() {
    return docNumber;
  }
  public BigDecimal getDocSequence() {
    return docSequence;
  }
  public String getDocState() {
    return docState;
  }
  public void setDocState(String docState) {
    this.docState = docState;
  }
  public void setDocSequence(BigDecimal docSequence) {
    this.docSequence = docSequence;
  }
  public void setDocNumber(BigDecimal docNumber) {
    this.docNumber = docNumber;
  }
  public String getCustomerSupplierCode() {
    return customerSupplierCode;
  }
  public void setCustomerSupplierCode(String customerSupplierCode) {
    this.customerSupplierCode = customerSupplierCode;
  }
  public String getName_2() {
    return name_2;
  }
  public String getName_1() {
    return name_1;
  }
  public void setName_1(String name_1) {
    this.name_1 = name_1;
  }
  public void setName_2(String name_2) {
    this.name_2 = name_2;
  }
  public String getDocTypeDescr() {
    return docTypeDescr;
  }
  public void setDocTypeDescr(String docTypeDescr) {
    this.docTypeDescr = docTypeDescr;
  }

}
