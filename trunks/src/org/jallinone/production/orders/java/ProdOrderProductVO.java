package org.jallinone.production.orders.java;

import org.openswing.swing.message.receive.java.ValueObjectImpl;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a production order product (table DOC23).</p>
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
public class ProdOrderProductVO extends ValueObjectImpl {

  private String companyCodeSys01DOC23;
  private String itemCodeItm01DOC23;
  private String descriptionSYS10;
  private java.math.BigDecimal docYearDOC23;
  private java.math.BigDecimal docNumberDOC23;
  private java.math.BigDecimal qtyDOC23;
  private java.math.BigDecimal progressiveHie01DOC23;
  private String locationDescriptionSYS10;
  private String warehouseCodeWar01DOC22;
  private java.math.BigDecimal progressiveHie02DOC23;
/*
  private String variantTypeItm06DOC23;
  private String variantCodeItm11DOC23;
  private String variantTypeItm07DOC23;
  private String variantCodeItm12DOC23;
  private String variantTypeItm08DOC23;
  private String variantCodeItm13DOC23;
  private String variantTypeItm09DOC23;
  private String variantCodeItm14DOC23;
  private String variantTypeItm10DOC23;
  private String variantCodeItm15DOC23;
*/

  public ProdOrderProductVO() {
  }


  public String getCompanyCodeSys01DOC23() {
    return companyCodeSys01DOC23;
  }
  public void setCompanyCodeSys01DOC23(String companyCodeSys01DOC23) {
    this.companyCodeSys01DOC23 = companyCodeSys01DOC23;
  }
  public String getItemCodeItm01DOC23() {
    return itemCodeItm01DOC23;
  }
  public void setItemCodeItm01DOC23(String itemCodeItm01DOC23) {
    this.itemCodeItm01DOC23 = itemCodeItm01DOC23;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public java.math.BigDecimal getDocYearDOC23() {
    return docYearDOC23;
  }
  public void setDocYearDOC23(java.math.BigDecimal docYearDOC23) {
    this.docYearDOC23 = docYearDOC23;
  }
  public java.math.BigDecimal getDocNumberDOC23() {
    return docNumberDOC23;
  }
  public void setDocNumberDOC23(java.math.BigDecimal docNumberDOC23) {
    this.docNumberDOC23 = docNumberDOC23;
  }
  public java.math.BigDecimal getQtyDOC23() {
    return qtyDOC23;
  }
  public void setQtyDOC23(java.math.BigDecimal qtyDOC23) {
    this.qtyDOC23 = qtyDOC23;
  }
  public java.math.BigDecimal getProgressiveHie01DOC23() {
    return progressiveHie01DOC23;
  }
  public void setProgressiveHie01DOC23(java.math.BigDecimal progressiveHie01DOC23) {
    this.progressiveHie01DOC23 = progressiveHie01DOC23;
  }
  public String getLocationDescriptionSYS10() {
    return locationDescriptionSYS10;
  }
  public void setLocationDescriptionSYS10(String locationDescriptionSYS10) {
    this.locationDescriptionSYS10 = locationDescriptionSYS10;
  }
  public String getWarehouseCodeWar01DOC22() {
    return warehouseCodeWar01DOC22;
  }
  public void setWarehouseCodeWar01DOC22(String warehouseCodeWar01DOC22) {
    this.warehouseCodeWar01DOC22 = warehouseCodeWar01DOC22;
  }
  public java.math.BigDecimal getProgressiveHie02DOC23() {
    return progressiveHie02DOC23;
  }
  public void setProgressiveHie02DOC23(java.math.BigDecimal progressiveHie02DOC23) {
    this.progressiveHie02DOC23 = progressiveHie02DOC23;
  }
  /*
  public String getVariantCodeItm11DOC23() {
    return variantCodeItm11DOC23;
  }
  public String getVariantCodeItm12DOC23() {
    return variantCodeItm12DOC23;
  }
  public String getVariantCodeItm13DOC23() {
    return variantCodeItm13DOC23;
  }
  public String getVariantCodeItm14DOC23() {
    return variantCodeItm14DOC23;
  }
  public String getVariantCodeItm15DOC23() {
    return variantCodeItm15DOC23;
  }
  public String getVariantTypeItm06DOC23() {
    return variantTypeItm06DOC23;
  }
  public String getVariantTypeItm07DOC23() {
    return variantTypeItm07DOC23;
  }
  public String getVariantTypeItm08DOC23() {
    return variantTypeItm08DOC23;
  }
  public String getVariantTypeItm09DOC23() {
    return variantTypeItm09DOC23;
  }
  public String getVariantTypeItm10DOC23() {
    return variantTypeItm10DOC23;
  }
  public void setVariantTypeItm10DOC23(String variantTypeItm10DOC23) {
    this.variantTypeItm10DOC23 = variantTypeItm10DOC23;
  }
  public void setVariantTypeItm09DOC23(String variantTypeItm09DOC23) {
    this.variantTypeItm09DOC23 = variantTypeItm09DOC23;
  }
  public void setVariantTypeItm08DOC23(String variantTypeItm08DOC23) {
    this.variantTypeItm08DOC23 = variantTypeItm08DOC23;
  }
  public void setVariantTypeItm07DOC23(String variantTypeItm07DOC23) {
    this.variantTypeItm07DOC23 = variantTypeItm07DOC23;
  }
  public void setVariantTypeItm06DOC23(String variantTypeItm06DOC23) {
    this.variantTypeItm06DOC23 = variantTypeItm06DOC23;
  }
  public void setVariantCodeItm15DOC23(String variantCodeItm15DOC23) {
    this.variantCodeItm15DOC23 = variantCodeItm15DOC23;
  }
  public void setVariantCodeItm14DOC23(String variantCodeItm14DOC23) {
    this.variantCodeItm14DOC23 = variantCodeItm14DOC23;
  }
  public void setVariantCodeItm13DOC23(String variantCodeItm13DOC23) {
    this.variantCodeItm13DOC23 = variantCodeItm13DOC23;
  }
  public void setVariantCodeItm12DOC23(String variantCodeItm12DOC23) {
    this.variantCodeItm12DOC23 = variantCodeItm12DOC23;
  }
  public void setVariantCodeItm11DOC23(String variantCodeItm11DOC23) {
    this.variantCodeItm11DOC23 = variantCodeItm11DOC23;
  }
*/

}
