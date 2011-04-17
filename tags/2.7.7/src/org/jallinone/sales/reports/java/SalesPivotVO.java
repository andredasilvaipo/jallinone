package org.jallinone.sales.reports.java;

import org.openswing.swing.message.receive.java.ValueObjectImpl;
import java.math.BigDecimal;
import org.openswing.swing.pivottable.java.PivotTableParameters;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object binded to the filter panel of the sales pivot table.</p>
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
public class SalesPivotVO extends ValueObjectImpl {

  private String companyCode = null;
  private String docType = null;
  private String docState = null;
  private BigDecimal docYear = null;
  private String customerCode = null;
  private BigDecimal amount = null;
  private BigDecimal progressiveHie02 = null; //item type
  private String itemCode = null;
  private PivotTableParameters pivotPars = null;


  public SalesPivotVO() {
  }


  public BigDecimal getAmount() {
    return amount;
  }
  public String getCompanyCode() {
    return companyCode;
  }
  public String getCustomerCode() {
    return customerCode;
  }
  public String getDocState() {
    return docState;
  }
  public String getDocType() {
    return docType;
  }
  public BigDecimal getDocYear() {
    return docYear;
  }
  public String getItemCode() {
    return itemCode;
  }
  public void setItemCode(String itemCode) {
    this.itemCode = itemCode;
  }
  public void setDocYear(BigDecimal docYear) {
    this.docYear = docYear;
  }
  public void setDocType(String docType) {
    this.docType = docType;
  }
  public void setDocState(String docState) {
    this.docState = docState;
  }
  public void setCustomerCode(String customerCode) {
    this.customerCode = customerCode;
  }
  public void setCompanyCode(String companyCode) {
    this.companyCode = companyCode;
  }
  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }
  public PivotTableParameters getPivotPars() {
    return pivotPars;
  }
  public void setPivotPars(PivotTableParameters pivotPars) {
    this.pivotPars = pivotPars;
  }
  public BigDecimal getProgressiveHie02() {
    return progressiveHie02;
  }
  public void setProgressiveHie02(BigDecimal progressiveHie02) {
    this.progressiveHie02 = progressiveHie02;
  }

}
