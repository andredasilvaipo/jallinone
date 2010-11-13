package org.jallinone.warehouse.availability.java;

import org.openswing.swing.message.receive.java.*;
import org.jallinone.commons.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store the booked item quantity and available quantity, per warehouse,
 * eventually filtered by a specified item.</p>
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
public class BookedItemQtyVO extends ValueObjectImpl {

  private java.math.BigDecimal bookQtyDOC02;
  private java.math.BigDecimal availableQtyWAR03;
  private String itemCodeItm01DOC02;
  private String itemDescriptionSYS10;
  private String companyCodeSys01WAR03;
  private String warehouseCodeWar01WAR03;
  private String descriptionWAR01;

  private String variantTypeItm06DOC02;
  private String variantCodeItm11DOC02;
  private String variantTypeItm07DOC02;
  private String variantCodeItm12DOC02;
  private String variantTypeItm08DOC02;
  private String variantCodeItm13DOC02;
  private String variantTypeItm09DOC02;
  private String variantCodeItm14DOC02;
  private String variantTypeItm10DOC02;
  private String variantCodeItm15DOC02;


  public BookedItemQtyVO() {
  }


  public java.math.BigDecimal getBookQtyDOC02() {
    return bookQtyDOC02;
  }
  public void setBookQtyDOC02(java.math.BigDecimal bookQtyDOC02) {
    this.bookQtyDOC02 = bookQtyDOC02;
  }
  public java.math.BigDecimal getAvailableQtyWAR03() {
    return availableQtyWAR03;
  }
  public void setAvailableQtyWAR03(java.math.BigDecimal availableQtyWAR03) {
    this.availableQtyWAR03 = availableQtyWAR03;
  }
  public String getItemCodeItm01DOC02() {
    return itemCodeItm01DOC02;
  }
  public void setItemCodeItm01DOC02(String itemCodeItm01DOC02) {
    this.itemCodeItm01DOC02 = itemCodeItm01DOC02;
  }
  public String getItemDescriptionSYS10() {
    String aux = itemDescriptionSYS10;
//    if (aux==null)
//      return null;
//
//    if (variantTypeItm06DOC02!=null && !ApplicationConsts.JOLLY.equals(variantTypeItm06DOC02))
//      aux += " "+variantTypeItm06DOC02;
//    if (variantCodeItm11DOC02!=null && !ApplicationConsts.JOLLY.equals(variantCodeItm11DOC02))
//      aux += " "+variantCodeItm11DOC02;
//
//    if (variantTypeItm07DOC02!=null && !ApplicationConsts.JOLLY.equals(variantTypeItm07DOC02))
//      aux += " "+variantTypeItm07DOC02;
//    if (variantCodeItm12DOC02!=null && !ApplicationConsts.JOLLY.equals(variantCodeItm12DOC02))
//      aux += " "+variantCodeItm12DOC02;
//
//    if (variantTypeItm08DOC02!=null && !ApplicationConsts.JOLLY.equals(variantTypeItm08DOC02))
//      aux += " "+variantTypeItm08DOC02;
//    if (variantCodeItm13DOC02!=null && !ApplicationConsts.JOLLY.equals(variantCodeItm13DOC02))
//      aux += " "+variantCodeItm13DOC02;
//
//    if (variantTypeItm09DOC02!=null && !ApplicationConsts.JOLLY.equals(variantTypeItm09DOC02))
//      aux += " "+variantTypeItm09DOC02;
//    if (variantCodeItm14DOC02!=null && !ApplicationConsts.JOLLY.equals(variantCodeItm14DOC02))
//      aux += " "+variantCodeItm14DOC02;
//
//    if (variantTypeItm10DOC02!=null && !ApplicationConsts.JOLLY.equals(variantTypeItm10DOC02))
//      aux += " "+variantTypeItm10DOC02;
//    if (variantCodeItm15DOC02!=null && !ApplicationConsts.JOLLY.equals(variantCodeItm15DOC02))
//      aux += " "+variantCodeItm15DOC02;
    return aux;
  }
  public void setItemDescriptionSYS10(String itemDescriptionSYS10) {
    this.itemDescriptionSYS10 = itemDescriptionSYS10;
  }
  public String getCompanyCodeSys01WAR03() {
    return companyCodeSys01WAR03;
  }
  public void setCompanyCodeSys01WAR03(String companyCodeSys01WAR03) {
    this.companyCodeSys01WAR03 = companyCodeSys01WAR03;
  }
  public String getWarehouseCodeWar01WAR03() {
    return warehouseCodeWar01WAR03;
  }
  public void setWarehouseCodeWar01WAR03(String warehouseCodeWar01WAR03) {
    this.warehouseCodeWar01WAR03 = warehouseCodeWar01WAR03;
  }
  public String getDescriptionWAR01() {
    return descriptionWAR01;
  }
  public void setDescriptionWAR01(String descriptionWAR01) {
    this.descriptionWAR01 = descriptionWAR01;
  }
  public String getVariantCodeItm11DOC02() {
    return variantCodeItm11DOC02;
  }
  public String getVariantCodeItm12DOC02() {
    return variantCodeItm12DOC02;
  }
  public String getVariantCodeItm13DOC02() {
    return variantCodeItm13DOC02;
  }
  public String getVariantCodeItm14DOC02() {
    return variantCodeItm14DOC02;
  }
  public String getVariantCodeItm15DOC02() {
    return variantCodeItm15DOC02;
  }
  public String getVariantTypeItm06DOC02() {
    return variantTypeItm06DOC02;
  }
  public String getVariantTypeItm07DOC02() {
    return variantTypeItm07DOC02;
  }
  public String getVariantTypeItm08DOC02() {
    return variantTypeItm08DOC02;
  }
  public String getVariantTypeItm09DOC02() {
    return variantTypeItm09DOC02;
  }
  public String getVariantTypeItm10DOC02() {
    return variantTypeItm10DOC02;
  }
  public void setVariantTypeItm10DOC02(String variantTypeItm10DOC02) {
    this.variantTypeItm10DOC02 = variantTypeItm10DOC02;
  }
  public void setVariantTypeItm09DOC02(String variantTypeItm09DOC02) {
    this.variantTypeItm09DOC02 = variantTypeItm09DOC02;
  }
  public void setVariantTypeItm08DOC02(String variantTypeItm08DOC02) {
    this.variantTypeItm08DOC02 = variantTypeItm08DOC02;
  }
  public void setVariantTypeItm07DOC02(String variantTypeItm07DOC02) {
    this.variantTypeItm07DOC02 = variantTypeItm07DOC02;
  }
  public void setVariantCodeItm15DOC02(String variantCodeItm15DOC02) {
    this.variantCodeItm15DOC02 = variantCodeItm15DOC02;
  }
  public void setVariantTypeItm06DOC02(String variantTypeItm06DOC02) {
    this.variantTypeItm06DOC02 = variantTypeItm06DOC02;
  }
  public void setVariantCodeItm14DOC02(String variantCodeItm14DOC02) {
    this.variantCodeItm14DOC02 = variantCodeItm14DOC02;
  }
  public void setVariantCodeItm13DOC02(String variantCodeItm13DOC02) {
    this.variantCodeItm13DOC02 = variantCodeItm13DOC02;
  }
  public void setVariantCodeItm12DOC02(String variantCodeItm12DOC02) {
    this.variantCodeItm12DOC02 = variantCodeItm12DOC02;
  }
  public void setVariantCodeItm11DOC02(String variantCodeItm11DOC02) {
    this.variantCodeItm11DOC02 = variantCodeItm11DOC02;
  }

}
