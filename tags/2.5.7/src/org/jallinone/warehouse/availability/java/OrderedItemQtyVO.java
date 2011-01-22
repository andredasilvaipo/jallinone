package org.jallinone.warehouse.availability.java;

import org.openswing.swing.message.receive.java.*;
import org.jallinone.commons.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store the future item quantities, based on purchase orders.</p>
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
public class OrderedItemQtyVO extends ValueObjectImpl {

  private java.util.Date deliveryDateDOC07;
  private java.math.BigDecimal orderQtyDOC07;
  private String minSellingQtyUmCodeReg02ITM01;
  private java.math.BigDecimal docYearDOC06;
  private java.math.BigDecimal docSequenceDOC06;
  private String warehouseCodeWar01DOC06;
  private String descriptionWAR01;

  private String variantTypeItm06DOC07;
  private String variantCodeItm11DOC07;
  private String variantTypeItm07DOC07;
  private String variantCodeItm12DOC07;
  private String variantTypeItm08DOC07;
  private String variantCodeItm13DOC07;
  private String variantTypeItm09DOC07;
  private String variantCodeItm14DOC07;
  private String variantTypeItm10DOC07;
  private String variantCodeItm15DOC07;
  private String descriptionSYS10;


  public OrderedItemQtyVO() {
  }


  public java.util.Date getDeliveryDateDOC07() {
    return deliveryDateDOC07;
  }
  public void setDeliveryDateDOC07(java.util.Date deliveryDateDOC07) {
    this.deliveryDateDOC07 = deliveryDateDOC07;
  }
  public java.math.BigDecimal getOrderQtyDOC07() {
    return orderQtyDOC07;
  }
  public void setOrderQtyDOC07(java.math.BigDecimal orderQtyDOC07) {
    this.orderQtyDOC07 = orderQtyDOC07;
  }
  public String getMinSellingQtyUmCodeReg02ITM01() {
    return minSellingQtyUmCodeReg02ITM01;
  }
  public void setMinSellingQtyUmCodeReg02ITM01(String minSellingQtyUmCodeReg02ITM01) {
    this.minSellingQtyUmCodeReg02ITM01 = minSellingQtyUmCodeReg02ITM01;
  }
  public java.math.BigDecimal getDocYearDOC06() {
    return docYearDOC06;
  }
  public void setDocYearDOC06(java.math.BigDecimal docYearDOC06) {
    this.docYearDOC06 = docYearDOC06;
  }
  public java.math.BigDecimal getDocSequenceDOC06() {
    return docSequenceDOC06;
  }
  public void setDocSequenceDOC06(java.math.BigDecimal docSequenceDOC06) {
    this.docSequenceDOC06 = docSequenceDOC06;
  }
  public String getDescriptionWAR01() {
    return descriptionWAR01;
  }
  public String getWarehouseCodeWar01DOC06() {
    return warehouseCodeWar01DOC06;
  }
  public void setWarehouseCodeWar01DOC06(String warehouseCodeWar01DOC06) {
    this.warehouseCodeWar01DOC06 = warehouseCodeWar01DOC06;
  }
  public void setDescriptionWAR01(String descriptionWAR01) {
    this.descriptionWAR01 = descriptionWAR01;
  }
  public String getVariantCodeItm11DOC07() {
    return variantCodeItm11DOC07;
  }
  public String getVariantCodeItm13DOC07() {
    return variantCodeItm13DOC07;
  }
  public String getVariantCodeItm12DOC07() {
    return variantCodeItm12DOC07;
  }
  public String getVariantCodeItm14DOC07() {
    return variantCodeItm14DOC07;
  }
  public String getVariantCodeItm15DOC07() {
    return variantCodeItm15DOC07;
  }
  public String getVariantTypeItm06DOC07() {
    return variantTypeItm06DOC07;
  }
  public String getVariantTypeItm07DOC07() {
    return variantTypeItm07DOC07;
  }
  public String getVariantTypeItm08DOC07() {
    return variantTypeItm08DOC07;
  }
  public String getVariantTypeItm09DOC07() {
    return variantTypeItm09DOC07;
  }
  public String getVariantTypeItm10DOC07() {
    return variantTypeItm10DOC07;
  }
  public void setVariantTypeItm10DOC07(String variantTypeItm10DOC07) {
    this.variantTypeItm10DOC07 = variantTypeItm10DOC07;
  }
  public void setVariantTypeItm09DOC07(String variantTypeItm09DOC07) {
    this.variantTypeItm09DOC07 = variantTypeItm09DOC07;
  }
  public void setVariantTypeItm08DOC07(String variantTypeItm08DOC07) {
    this.variantTypeItm08DOC07 = variantTypeItm08DOC07;
  }
  public void setVariantTypeItm07DOC07(String variantTypeItm07DOC07) {
    this.variantTypeItm07DOC07 = variantTypeItm07DOC07;
  }
  public void setVariantTypeItm06DOC07(String variantTypeItm06DOC07) {
    this.variantTypeItm06DOC07 = variantTypeItm06DOC07;
  }
  public void setVariantCodeItm15DOC07(String variantCodeItm15DOC07) {
    this.variantCodeItm15DOC07 = variantCodeItm15DOC07;
  }
  public void setVariantCodeItm14DOC07(String variantCodeItm14DOC07) {
    this.variantCodeItm14DOC07 = variantCodeItm14DOC07;
  }
  public void setVariantCodeItm13DOC07(String variantCodeItm13DOC07) {
    this.variantCodeItm13DOC07 = variantCodeItm13DOC07;
  }
  public void setVariantCodeItm12DOC07(String variantCodeItm12DOC07) {
    this.variantCodeItm12DOC07 = variantCodeItm12DOC07;
  }
  public void setVariantCodeItm11DOC07(String variantCodeItm11DOC07) {
    this.variantCodeItm11DOC07 = variantCodeItm11DOC07;
  }
  public String getDescriptionSYS10() {
    String aux = descriptionSYS10;
//    if (aux==null)
//      return null;
//
//    if (variantTypeItm06DOC07!=null && !ApplicationConsts.JOLLY.equals(variantTypeItm06DOC07))
//      aux += " "+variantTypeItm06DOC07;
//    if (variantCodeItm11DOC07!=null && !ApplicationConsts.JOLLY.equals(variantCodeItm11DOC07))
//      aux += " "+variantCodeItm11DOC07;
//
//    if (variantTypeItm07DOC07!=null && !ApplicationConsts.JOLLY.equals(variantTypeItm07DOC07))
//      aux += " "+variantTypeItm07DOC07;
//    if (variantCodeItm12DOC07!=null && !ApplicationConsts.JOLLY.equals(variantCodeItm12DOC07))
//      aux += " "+variantCodeItm12DOC07;
//
//    if (variantTypeItm08DOC07!=null && !ApplicationConsts.JOLLY.equals(variantTypeItm08DOC07))
//      aux += " "+variantTypeItm08DOC07;
//    if (variantCodeItm13DOC07!=null && !ApplicationConsts.JOLLY.equals(variantCodeItm13DOC07))
//      aux += " "+variantCodeItm13DOC07;
//
//    if (variantTypeItm09DOC07!=null && !ApplicationConsts.JOLLY.equals(variantTypeItm09DOC07))
//      aux += " "+variantTypeItm09DOC07;
//    if (variantCodeItm14DOC07!=null && !ApplicationConsts.JOLLY.equals(variantCodeItm14DOC07))
//      aux += " "+variantCodeItm14DOC07;
//
//    if (variantTypeItm10DOC07!=null && !ApplicationConsts.JOLLY.equals(variantTypeItm10DOC07))
//      aux += " "+variantTypeItm10DOC07;
//    if (variantCodeItm15DOC07!=null && !ApplicationConsts.JOLLY.equals(variantCodeItm15DOC07))
//      aux += " "+variantCodeItm15DOC07;
    return aux;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }

}
