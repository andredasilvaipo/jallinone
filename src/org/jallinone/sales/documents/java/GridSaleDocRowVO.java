package org.jallinone.sales.documents.java;

import org.openswing.swing.message.receive.java.*;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store sale order row info, for the order rows grid.</p>
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
public class GridSaleDocRowVO extends ValueObjectImpl {

  private String companyCodeSys01DOC02;
  private String docTypeDOC02;
  private java.math.BigDecimal docYearDOC02;
  private java.math.BigDecimal docNumberDOC02;
  private String itemCodeItm01DOC02;
  private java.math.BigDecimal rowNumberDOC02;
  private String descriptionSYS10;
  private java.math.BigDecimal valueSal02DOC02;
  private String vatCodeItm01DOC02;

  private java.math.BigDecimal valueDOC02;
  private java.math.BigDecimal qtyDOC02;
  private java.math.BigDecimal vatValueDOC02;
  private java.math.BigDecimal outQtyDOC02;
  private java.math.BigDecimal totalDiscountDOC02;
  private java.math.BigDecimal invoiceQtyDOC02;


  public GridSaleDocRowVO() {
  }


  public String getCompanyCodeSys01DOC02() {
    return companyCodeSys01DOC02;
  }
  public void setCompanyCodeSys01DOC02(String companyCodeSys01DOC02) {
    this.companyCodeSys01DOC02 = companyCodeSys01DOC02;
  }
  public String getDocTypeDOC02() {
    return docTypeDOC02;
  }
  public void setDocTypeDOC02(String docTypeDOC02) {
    this.docTypeDOC02 = docTypeDOC02;
  }

  public java.math.BigDecimal getDocYearDOC02() {
    return docYearDOC02;
  }
  public void setDocYearDOC02(java.math.BigDecimal docYearDOC02) {
    this.docYearDOC02 = docYearDOC02;
  }
  public java.math.BigDecimal getDocNumberDOC02() {
    return docNumberDOC02;
  }
  public void setDocNumberDOC02(java.math.BigDecimal docNumberDOC02) {
    this.docNumberDOC02 = docNumberDOC02;
  }
  public String getItemCodeItm01DOC02() {
    return itemCodeItm01DOC02;
  }
  public void setItemCodeItm01DOC02(String itemCodeItm01DOC02) {
    this.itemCodeItm01DOC02 = itemCodeItm01DOC02;
  }
  public String getVatCodeItm01DOC02() {
    return vatCodeItm01DOC02;
  }
  public void setVatCodeItm01DOC02(String vatCodeItm01DOC02) {
    this.vatCodeItm01DOC02 = vatCodeItm01DOC02;
  }
  public java.math.BigDecimal getValueSal02DOC02() {
    return valueSal02DOC02;
  }
  public void setValueSal02DOC02(java.math.BigDecimal valueSal02DOC02) {
    this.valueSal02DOC02 = valueSal02DOC02;
  }
  public java.math.BigDecimal getValueDOC02() {
    return valueDOC02;
  }
  public void setValueDOC02(java.math.BigDecimal valueDOC02) {
    this.valueDOC02 = valueDOC02;
  }
  public java.math.BigDecimal getQtyDOC02() {
    return qtyDOC02;
  }
  public void setQtyDOC02(java.math.BigDecimal qtyDOC02) {
    this.qtyDOC02 = qtyDOC02;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public java.math.BigDecimal getVatValueDOC02() {
    return vatValueDOC02;
  }
  public void setVatValueDOC02(java.math.BigDecimal vatValueDOC02) {
    this.vatValueDOC02 = vatValueDOC02;
  }
  public java.math.BigDecimal getRowNumberDOC02() {
    return rowNumberDOC02;
  }
  public void setRowNumberDOC02(java.math.BigDecimal rowNumberDOC02) {
    this.rowNumberDOC02 = rowNumberDOC02;
  }
  public void setOutQtyDOC02(java.math.BigDecimal outQtyDOC02) {
    this.outQtyDOC02 = outQtyDOC02;
  }
  public java.math.BigDecimal getOutQtyDOC02() {
    return outQtyDOC02;
  }
  public java.math.BigDecimal getTotalDiscountDOC02() {
    return totalDiscountDOC02;
  }
  public void setTotalDiscountDOC02(java.math.BigDecimal totalDiscountDOC02) {
    this.totalDiscountDOC02 = totalDiscountDOC02;
  }
  public java.math.BigDecimal getInvoiceQtyDOC02() {
    return invoiceQtyDOC02;
  }
  public void setInvoiceQtyDOC02(java.math.BigDecimal invoiceQtyDOC02) {
    this.invoiceQtyDOC02 = invoiceQtyDOC02;
  }




}
