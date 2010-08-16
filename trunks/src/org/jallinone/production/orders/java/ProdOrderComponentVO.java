package org.jallinone.production.orders.java;

import org.openswing.swing.message.receive.java.ValueObjectImpl;
import java.util.ArrayList;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a production order required component (table DOC24).</p>
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
public class ProdOrderComponentVO extends ValueObjectImpl {

  private String companyCodeSys01DOC24;
  private String itemCodeItm01DOC24;
  private String descriptionSYS10;
  private java.math.BigDecimal docYearDOC24;
  private java.math.BigDecimal docNumberDOC24;
  private java.math.BigDecimal qtyDOC24;
  private java.math.BigDecimal availableQty;
  private ArrayList availabilities = new ArrayList(); // collection of ItemAvailabilityVO objects
  private java.math.BigDecimal progressiveHie01DOC24; // components warehouse hierarchy
  private String locationDescriptionSYS10;
  private String minSellingQtyUmCodeReg02ITM01;

  private String variantTypeItm06DOC24;
  private String variantCodeItm11DOC24;
  private String variantTypeItm07DOC24;
  private String variantCodeItm12DOC24;
  private String variantTypeItm08DOC24;
  private String variantCodeItm13DOC24;
  private String variantTypeItm09DOC24;
  private String variantCodeItm14DOC24;
  private String variantTypeItm10DOC24;
  private String variantCodeItm15DOC24;


  public ProdOrderComponentVO() {
  }


  public String getCompanyCodeSys01DOC24() {
    return companyCodeSys01DOC24;
  }
  public void setCompanyCodeSys01DOC24(String companyCodeSys01DOC24) {
    this.companyCodeSys01DOC24 = companyCodeSys01DOC24;
  }
  public String getItemCodeItm01DOC24() {
    return itemCodeItm01DOC24;
  }
  public void setItemCodeItm01DOC24(String itemCodeItm01DOC24) {
    this.itemCodeItm01DOC24 = itemCodeItm01DOC24;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public java.math.BigDecimal getDocYearDOC24() {
    return docYearDOC24;
  }
  public void setDocYearDOC24(java.math.BigDecimal docYearDOC24) {
    this.docYearDOC24 = docYearDOC24;
  }
  public java.math.BigDecimal getDocNumberDOC24() {
    return docNumberDOC24;
  }
  public void setDocNumberDOC24(java.math.BigDecimal docNumberDOC24) {
    this.docNumberDOC24 = docNumberDOC24;
  }
  public java.math.BigDecimal getQtyDOC24() {
    return qtyDOC24;
  }
  public void setQtyDOC24(java.math.BigDecimal qtyDOC24) {
    this.qtyDOC24 = qtyDOC24;
  }
  public java.math.BigDecimal getAvailableQty() {
    return availableQty;
  }
  public void setAvailableQty(java.math.BigDecimal availableQty) {
    this.availableQty = availableQty;
  }
  public ArrayList getAvailabilities() {
    return availabilities;
  }
  public void setAvailabilities(ArrayList availabilities) {
    this.availabilities = availabilities;
  }
  public java.math.BigDecimal getProgressiveHie01DOC24() {
    return progressiveHie01DOC24;
  }

  public void setProgressiveHie01DOC24(java.math.BigDecimal progressiveHie01DOC24) {
    this.progressiveHie01DOC24 = progressiveHie01DOC24;
  }
  public String getLocationDescriptionSYS10() {
    return locationDescriptionSYS10;
  }
  public void setLocationDescriptionSYS10(String locationDescriptionSYS10) {
    this.locationDescriptionSYS10 = locationDescriptionSYS10;
  }
  public String getMinSellingQtyUmCodeReg02ITM01() {
    return minSellingQtyUmCodeReg02ITM01;
  }
  public void setMinSellingQtyUmCodeReg02ITM01(String minSellingQtyUmCodeReg02ITM01) {
    this.minSellingQtyUmCodeReg02ITM01 = minSellingQtyUmCodeReg02ITM01;
  }
  public String getVariantCodeItm11DOC24() {
    return variantCodeItm11DOC24;
  }
  public String getVariantCodeItm12DOC24() {
    return variantCodeItm12DOC24;
  }
  public String getVariantCodeItm13DOC24() {
    return variantCodeItm13DOC24;
  }
  public String getVariantCodeItm14DOC24() {
    return variantCodeItm14DOC24;
  }
  public String getVariantCodeItm15DOC24() {
    return variantCodeItm15DOC24;
  }
  public String getVariantTypeItm06DOC24() {
    return variantTypeItm06DOC24;
  }
  public String getVariantTypeItm07DOC24() {
    return variantTypeItm07DOC24;
  }
  public String getVariantTypeItm08DOC24() {
    return variantTypeItm08DOC24;
  }
  public String getVariantTypeItm09DOC24() {
    return variantTypeItm09DOC24;
  }
  public String getVariantTypeItm10DOC24() {
    return variantTypeItm10DOC24;
  }
  public void setVariantTypeItm10DOC24(String variantTypeItm10DOC24) {
    this.variantTypeItm10DOC24 = variantTypeItm10DOC24;
  }
  public void setVariantTypeItm09DOC24(String variantTypeItm09DOC24) {
    this.variantTypeItm09DOC24 = variantTypeItm09DOC24;
  }
  public void setVariantTypeItm08DOC24(String variantTypeItm08DOC24) {
    this.variantTypeItm08DOC24 = variantTypeItm08DOC24;
  }
  public void setVariantTypeItm07DOC24(String variantTypeItm07DOC24) {
    this.variantTypeItm07DOC24 = variantTypeItm07DOC24;
  }
  public void setVariantTypeItm06DOC24(String variantTypeItm06DOC24) {
    this.variantTypeItm06DOC24 = variantTypeItm06DOC24;
  }
  public void setVariantCodeItm15DOC24(String variantCodeItm15DOC24) {
    this.variantCodeItm15DOC24 = variantCodeItm15DOC24;
  }
  public void setVariantCodeItm14DOC24(String variantCodeItm14DOC24) {
    this.variantCodeItm14DOC24 = variantCodeItm14DOC24;
  }
  public void setVariantCodeItm13DOC24(String variantCodeItm13DOC24) {
    this.variantCodeItm13DOC24 = variantCodeItm13DOC24;
  }
  public void setVariantCodeItm12DOC24(String variantCodeItm12DOC24) {
    this.variantCodeItm12DOC24 = variantCodeItm12DOC24;
  }
  public void setVariantCodeItm11DOC24(String variantCodeItm11DOC24) {
    this.variantCodeItm11DOC24 = variantCodeItm11DOC24;
  }




}
