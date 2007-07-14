package org.jallinone.warehouse.documents.java;

import org.openswing.swing.message.receive.java.*;
import java.util.ArrayList;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store an out delivery note header info for a detail form.</p>
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
public class GridOutDeliveryNoteRowVO extends ValueObjectImpl {

  private String companyCodeSys01DOC10;
  private String docTypeDOC10;
  private java.math.BigDecimal docYearDOC10;
  private java.math.BigDecimal docNumberDOC10;
  private String docTypeDoc01DOC10;
  private java.math.BigDecimal docYearDoc01DOC10;
  private java.math.BigDecimal docNumberDoc01DOC10;
  private String itemCodeItm01DOC10;
  private String descriptionSYS10;
  private java.math.BigDecimal qtyDOC10;
  private String umCodeREG02;
  private java.math.BigDecimal decimalsREG02;
  private java.math.BigDecimal rowNumberDOC10;
  private java.math.BigDecimal progressiveHie02DOC10;
  private java.math.BigDecimal progressiveHie01DOC10;
  private String locationDescriptionSYS10;
  private String warehouseCodeWar01DOC08;
  private java.math.BigDecimal qtyDOC02;
  private java.math.BigDecimal outQtyDOC02;
  private ArrayList serialNumbers = new ArrayList();
  private ArrayList barCodes = new ArrayList();
  private Boolean serialNumberRequiredITM01;
  private java.math.BigDecimal docSequenceDoc01DOC10;
  private java.math.BigDecimal invoiceQtyDOC10;


  public GridOutDeliveryNoteRowVO() {
  }


  public String getCompanyCodeSys01DOC10() {
    return companyCodeSys01DOC10;
  }
  public void setCompanyCodeSys01DOC10(String companyCodeSys01DOC10) {
    this.companyCodeSys01DOC10 = companyCodeSys01DOC10;
  }
  public String getDocTypeDOC10() {
    return docTypeDOC10;
  }
  public void setDocTypeDOC10(String docTypeDOC10) {
    this.docTypeDOC10 = docTypeDOC10;
  }
  public String getDocTypeDoc01DOC10() {
    return docTypeDoc01DOC10;
  }
  public void setDocTypeDoc01DOC10(String docTypeDoc01DOC10) {
    this.docTypeDoc01DOC10 = docTypeDoc01DOC10;
  }
  public String getItemCodeItm01DOC10() {
    return itemCodeItm01DOC10;
  }
  public void setItemCodeItm01DOC10(String itemCodeItm01DOC10) {
    this.itemCodeItm01DOC10 = itemCodeItm01DOC10;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public java.math.BigDecimal getDocYearDOC10() {
    return docYearDOC10;
  }
  public void setDocYearDOC10(java.math.BigDecimal docYearDOC10) {
    this.docYearDOC10 = docYearDOC10;
  }
  public java.math.BigDecimal getDocNumberDOC10() {
    return docNumberDOC10;
  }
  public void setDocNumberDOC10(java.math.BigDecimal docNumberDOC10) {
    this.docNumberDOC10 = docNumberDOC10;
  }
  public java.math.BigDecimal getQtyDOC10() {
    return qtyDOC10;
  }
  public void setQtyDOC10(java.math.BigDecimal qtyDOC10) {
    this.qtyDOC10 = qtyDOC10;
  }
  public java.math.BigDecimal getDocYearDoc01DOC10() {
    return docYearDoc01DOC10;
  }
  public void setDocYearDoc01DOC10(java.math.BigDecimal docYearDoc01DOC10) {
    this.docYearDoc01DOC10 = docYearDoc01DOC10;
  }
  public java.math.BigDecimal getDocNumberDoc01DOC10() {
    return docNumberDoc01DOC10;
  }
  public void setDocNumberDoc01DOC10(java.math.BigDecimal docNumberDoc01DOC10) {
    this.docNumberDoc01DOC10 = docNumberDoc01DOC10;
  }
  public java.math.BigDecimal getDecimalsREG02() {
    return decimalsREG02;
  }
  public void setDecimalsREG02(java.math.BigDecimal decimalsREG02) {
    this.decimalsREG02 = decimalsREG02;
  }
  public String getUmCodeREG02() {
    return umCodeREG02;
  }
  public void setUmCodeREG02(String umCodeREG02) {
    this.umCodeREG02 = umCodeREG02;
  }
  public void setRowNumberDOC10(java.math.BigDecimal rowNumberDOC10) {
    this.rowNumberDOC10 = rowNumberDOC10;
  }
  public java.math.BigDecimal getRowNumberDOC10() {
    return rowNumberDOC10;
  }
  public java.math.BigDecimal getProgressiveHie02DOC10() {
    return progressiveHie02DOC10;
  }
  public void setProgressiveHie02DOC10(java.math.BigDecimal progressiveHie02DOC10) {
    this.progressiveHie02DOC10 = progressiveHie02DOC10;
  }
  public java.math.BigDecimal getProgressiveHie01DOC10() {
    return progressiveHie01DOC10;
  }
  public void setProgressiveHie01DOC10(java.math.BigDecimal progressiveHie01DOC10) {
    this.progressiveHie01DOC10 = progressiveHie01DOC10;
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
  public java.math.BigDecimal getQtyDOC02() {
    return qtyDOC02;
  }
  public void setQtyDOC02(java.math.BigDecimal qtyDOC02) {
    this.qtyDOC02 = qtyDOC02;
  }
  public java.math.BigDecimal getOutQtyDOC02() {
    return outQtyDOC02;
  }
  public void setOutQtyDOC02(java.math.BigDecimal outQtyDOC02) {
    this.outQtyDOC02 = outQtyDOC02;
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
  public java.math.BigDecimal getDocSequenceDoc01DOC10() {
    return docSequenceDoc01DOC10;
  }
  public void setDocSequenceDoc01DOC10(java.math.BigDecimal docSequenceDoc01DOC10) {
    this.docSequenceDoc01DOC10 = docSequenceDoc01DOC10;
  }
  public java.math.BigDecimal getInvoiceQtyDOC10() {
    return invoiceQtyDOC10;
  }
  public void setInvoiceQtyDOC10(java.math.BigDecimal invoiceQtyDOC10) {
    this.invoiceQtyDOC10 = invoiceQtyDOC10;
  }

}
