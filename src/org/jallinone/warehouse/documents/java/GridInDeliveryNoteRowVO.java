package org.jallinone.warehouse.documents.java;

import org.openswing.swing.message.receive.java.*;
import java.util.ArrayList;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store delivery note header info for a detail form.</p>
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
public class GridInDeliveryNoteRowVO extends ValueObjectImpl {

  private String companyCodeSys01DOC09;
  private String docTypeDOC09;
  private java.math.BigDecimal docYearDOC09;
  private java.math.BigDecimal docNumberDOC09;
  private String docTypeDoc06DOC09;
  private java.math.BigDecimal docYearDoc06DOC09;
  private java.math.BigDecimal docNumberDoc06DOC09;
  private String itemCodeItm01DOC09;
  private java.math.BigDecimal supplierQtyDOC09;
  private String descriptionSYS10;
  private java.math.BigDecimal qtyDOC09;
  private String umCodeREG02;
  private java.math.BigDecimal supplierQtyDecimalsREG02;
  private String umCodeReg02PUR02;
  private java.math.BigDecimal valueREG05;
  private java.math.BigDecimal decimalsREG02;
  private java.math.BigDecimal rowNumberDOC09;
  private String supplierItemCodePur02DOC09;
  private java.math.BigDecimal progressiveHie02DOC09;
  private java.math.BigDecimal progressiveHie01DOC09;
  private String locationDescriptionSYS10;
  private String warehouseCodeWar01DOC08;
  private java.math.BigDecimal qtyDOC07;
  private java.math.BigDecimal inQtyDOC07;
  private ArrayList serialNumbers = new ArrayList();
  private ArrayList barCodes = new ArrayList();
  private Boolean serialNumberRequiredITM01;
  private java.math.BigDecimal docSequenceDoc06DOC09;
  private java.math.BigDecimal invoiceQtyDOC09;


  public GridInDeliveryNoteRowVO() {
  }


  public String getCompanyCodeSys01DOC09() {
    return companyCodeSys01DOC09;
  }
  public void setCompanyCodeSys01DOC09(String companyCodeSys01DOC09) {
    this.companyCodeSys01DOC09 = companyCodeSys01DOC09;
  }
  public String getDocTypeDOC09() {
    return docTypeDOC09;
  }
  public void setDocTypeDOC09(String docTypeDOC09) {
    this.docTypeDOC09 = docTypeDOC09;
  }
  public String getDocTypeDoc06DOC09() {
    return docTypeDoc06DOC09;
  }
  public void setDocTypeDoc06DOC09(String docTypeDoc06DOC09) {
    this.docTypeDoc06DOC09 = docTypeDoc06DOC09;
  }
  public String getItemCodeItm01DOC09() {
    return itemCodeItm01DOC09;
  }
  public void setItemCodeItm01DOC09(String itemCodeItm01DOC09) {
    this.itemCodeItm01DOC09 = itemCodeItm01DOC09;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public java.math.BigDecimal getDocYearDOC09() {
    return docYearDOC09;
  }
  public void setDocYearDOC09(java.math.BigDecimal docYearDOC09) {
    this.docYearDOC09 = docYearDOC09;
  }
  public java.math.BigDecimal getDocNumberDOC09() {
    return docNumberDOC09;
  }
  public void setDocNumberDOC09(java.math.BigDecimal docNumberDOC09) {
    this.docNumberDOC09 = docNumberDOC09;
  }
  public java.math.BigDecimal getQtyDOC09() {
    return qtyDOC09;
  }
  public void setQtyDOC09(java.math.BigDecimal qtyDOC09) {
    this.qtyDOC09 = qtyDOC09;
  }
  public java.math.BigDecimal getDocYearDoc06DOC09() {
    return docYearDoc06DOC09;
  }
  public void setDocYearDoc06DOC09(java.math.BigDecimal docYearDoc06DOC09) {
    this.docYearDoc06DOC09 = docYearDoc06DOC09;
  }
  public java.math.BigDecimal getDocNumberDoc06DOC09() {
    return docNumberDoc06DOC09;
  }
  public void setDocNumberDoc06DOC09(java.math.BigDecimal docNumberDoc06DOC09) {
    this.docNumberDoc06DOC09 = docNumberDoc06DOC09;
  }
  public java.math.BigDecimal getSupplierQtyDOC09() {
    return supplierQtyDOC09;
  }
  public void setSupplierQtyDOC09(java.math.BigDecimal supplierQtyDOC09) {
    this.supplierQtyDOC09 = supplierQtyDOC09;
  }
  public java.math.BigDecimal getValueREG05() {
    return valueREG05;
  }
  public void setValueREG05(java.math.BigDecimal valueREG05) {
    this.valueREG05 = valueREG05;
  }
  public java.math.BigDecimal getDecimalsREG02() {
    return decimalsREG02;
  }
  public void setDecimalsREG02(java.math.BigDecimal decimalsREG02) {
    this.decimalsREG02 = decimalsREG02;
  }
  public java.math.BigDecimal getSupplierQtyDecimalsREG02() {
    return supplierQtyDecimalsREG02;
  }
  public void setSupplierQtyDecimalsREG02(java.math.BigDecimal supplierQtyDecimalsREG02) {
    this.supplierQtyDecimalsREG02 = supplierQtyDecimalsREG02;
  }
  public String getUmCodeREG02() {
    return umCodeREG02;
  }
  public void setUmCodeREG02(String umCodeREG02) {
    this.umCodeREG02 = umCodeREG02;
  }
  public String getUmCodeReg02PUR02() {
    return umCodeReg02PUR02;
  }
  public void setUmCodeReg02PUR02(String umCodeReg02PUR02) {
    this.umCodeReg02PUR02 = umCodeReg02PUR02;
  }
  public void setRowNumberDOC09(java.math.BigDecimal rowNumberDOC09) {
    this.rowNumberDOC09 = rowNumberDOC09;
  }
  public java.math.BigDecimal getRowNumberDOC09() {
    return rowNumberDOC09;
  }
  public String getSupplierItemCodePur02DOC09() {
    return supplierItemCodePur02DOC09;
  }
  public void setSupplierItemCodePur02DOC09(String supplierItemCodePur02DOC09) {
    this.supplierItemCodePur02DOC09 = supplierItemCodePur02DOC09;
  }
  public java.math.BigDecimal getProgressiveHie02DOC09() {
    return progressiveHie02DOC09;
  }
  public void setProgressiveHie02DOC09(java.math.BigDecimal progressiveHie02DOC09) {
    this.progressiveHie02DOC09 = progressiveHie02DOC09;
  }
  public java.math.BigDecimal getProgressiveHie01DOC09() {
    return progressiveHie01DOC09;
  }
  public void setProgressiveHie01DOC09(java.math.BigDecimal progressiveHie01DOC09) {
    this.progressiveHie01DOC09 = progressiveHie01DOC09;
  }
  public String getLocationDescriptionSYS10() {
    return locationDescriptionSYS10;
  }
  public void setLocationDescriptionSYS10(String locationDescriptionSYS10) {
    this.locationDescriptionSYS10 = locationDescriptionSYS10;
  }
  public String getWarehouseCodeWar01DOC08() {
    return warehouseCodeWar01DOC08;
  }
  public void setWarehouseCodeWar01DOC08(String warehouseCodeWar01DOC08) {
    this.warehouseCodeWar01DOC08 = warehouseCodeWar01DOC08;
  }
  public java.math.BigDecimal getQtyDOC07() {
    return qtyDOC07;
  }
  public void setQtyDOC07(java.math.BigDecimal qtyDOC07) {
    this.qtyDOC07 = qtyDOC07;
  }
  public java.math.BigDecimal getInQtyDOC07() {
    return inQtyDOC07;
  }
  public void setInQtyDOC07(java.math.BigDecimal inQtyDOC07) {
    this.inQtyDOC07 = inQtyDOC07;
  }
  public ArrayList getSerialNumbers() {
    return serialNumbers;
  }
  public void setSerialNumbers(ArrayList serialNumbers) {
    this.serialNumbers = serialNumbers;
  }
  public ArrayList getBarCodes() {
    return barCodes;
  }
  public void setBarCodes(ArrayList barCodes) {
    this.barCodes = barCodes;
  }
  public Boolean getSerialNumberRequiredITM01() {
    return serialNumberRequiredITM01;
  }
  public void setSerialNumberRequiredITM01(Boolean serialNumberRequiredITM01) {
    this.serialNumberRequiredITM01 = serialNumberRequiredITM01;
  }
  public java.math.BigDecimal getDocSequenceDoc06DOC09() {
    return docSequenceDoc06DOC09;
  }
  public void setDocSequenceDoc06DOC09(java.math.BigDecimal docSequenceDoc06DOC09) {
    this.docSequenceDoc06DOC09 = docSequenceDoc06DOC09;
  }
  public java.math.BigDecimal getInvoiceQtyDOC09() {
    return invoiceQtyDOC09;
  }
  public void setInvoiceQtyDOC09(java.math.BigDecimal invoiceQtyDOC09) {
    this.invoiceQtyDOC09 = invoiceQtyDOC09;
  }

}
