package org.jallinone.warehouse.documents.java;

import org.openswing.swing.message.receive.java.*;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store delivery note header info for a grid.</p>
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
public class GridDeliveryNoteVO extends ValueObjectImpl {

  private String companyCodeSys01DOC08;
  private String docTypeDOC08;
  private String docStateDOC08;
  private String warehouseCodeWar01DOC08;
  private String docRefDOC08;
  private String descriptionDOC08;
  private String warehouseDescriptionDOC08;
  private java.util.Date docDateDOC08;
  private java.math.BigDecimal docYearDOC08;
  private java.math.BigDecimal docNumberDOC08;
  private java.math.BigDecimal progressiveReg04DOC08;
  private String supplierCustomerCodeDOC08;
  private String name_1REG04;
  private String name_2REG04;
  private java.math.BigDecimal docSequenceDOC08;


  public GridDeliveryNoteVO() {
  }


  public String getCompanyCodeSys01DOC08() {
    return companyCodeSys01DOC08;
  }
  public void setCompanyCodeSys01DOC08(String companyCodeSys01DOC08) {
    this.companyCodeSys01DOC08 = companyCodeSys01DOC08;
  }
  public String getDocTypeDOC08() {
    return docTypeDOC08;
  }
  public void setDocTypeDOC08(String docTypeDOC08) {
    this.docTypeDOC08 = docTypeDOC08;
  }
  public String getDocStateDOC08() {
    return docStateDOC08;
  }
  public void setDocStateDOC08(String docStateDOC08) {
    this.docStateDOC08 = docStateDOC08;
  }
  public String getWarehouseCodeWar01DOC08() {
    return warehouseCodeWar01DOC08;
  }
  public void setWarehouseCodeWar01DOC08(String warehouseCodeWar01DOC08) {
    this.warehouseCodeWar01DOC08 = warehouseCodeWar01DOC08;
  }
  public String getDocRefDOC08() {
    return docRefDOC08;
  }
  public void setDocRefDOC08(String docRefDOC08) {
    this.docRefDOC08 = docRefDOC08;
  }
  public String getDescriptionDOC08() {
    return descriptionDOC08;
  }
  public void setDescriptionDOC08(String descriptionDOC08) {
    this.descriptionDOC08 = descriptionDOC08;
  }
  public String getWarehouseDescriptionDOC08() {
    return warehouseDescriptionDOC08;
  }
  public void setWarehouseDescriptionDOC08(String warehouseDescriptionDOC08) {
    this.warehouseDescriptionDOC08 = warehouseDescriptionDOC08;
  }
  public java.util.Date getDocDateDOC08() {
    return docDateDOC08;
  }
  public void setDocDateDOC08(java.util.Date docDateDOC08) {
    this.docDateDOC08 = docDateDOC08;
  }
  public java.math.BigDecimal getDocYearDOC08() {
    return docYearDOC08;
  }
  public void setDocYearDOC08(java.math.BigDecimal docYearDOC08) {
    this.docYearDOC08 = docYearDOC08;
  }
  public java.math.BigDecimal getDocNumberDOC08() {
    return docNumberDOC08;
  }
  public void setDocNumberDOC08(java.math.BigDecimal docNumberDOC08) {
    this.docNumberDOC08 = docNumberDOC08;
  }
  public String getSupplierCustomerCodeDOC08() {
    return supplierCustomerCodeDOC08;
  }
  public void setSupplierCustomerCodeDOC08(String supplierCustomerCodeDOC08) {
    this.supplierCustomerCodeDOC08 = supplierCustomerCodeDOC08;
  }
  public void setName_1REG04(String name_1REG04) {
    this.name_1REG04 = name_1REG04;
  }
  public void setProgressiveReg04DOC08(java.math.BigDecimal progressiveReg04DOC08) {
    this.progressiveReg04DOC08 = progressiveReg04DOC08;
  }
  public String getName_1REG04() {
    return name_1REG04;
  }
  public java.math.BigDecimal getProgressiveReg04DOC08() {
    return progressiveReg04DOC08;
  }
  public String getName_2REG04() {
    return name_2REG04;
  }
  public void setName_2REG04(String name_2REG04) {
    this.name_2REG04 = name_2REG04;
  }
  public java.math.BigDecimal getDocSequenceDOC08() {
    return docSequenceDOC08;
  }
  public void setDocSequenceDOC08(java.math.BigDecimal docSequenceDOC08) {
    this.docSequenceDOC08 = docSequenceDOC08;
  }

}
