package org.jallinone.warehouse.tables.movements.java;

import org.openswing.swing.message.receive.java.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import org.jallinone.commons.java.*;



/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a warehouse motive.</p>
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
public class MovementVO extends ValueObjectImpl {

  private String qtySignWAR04;
  private String itemTypeWAR04;
  private java.math.BigDecimal progressiveWAR02;
  private String companyCodeSys01WAR02;
  private String warehouseCodeWar01WAR02;
  private String itemCodeItm01WAR02;
  private BigDecimal progressiveHie01WAR02;
  private String locationDescriptionSYS10;
  private java.sql.Timestamp movementDateWAR02;
  private String usernameWAR02;
  private String noteWAR02;
  private java.math.BigDecimal deltaQtyWAR02;
  private String warehouseMotiveWar04WAR02;
  private String motiveDescriptionSYS10;
  private String itemDescriptionSYS10;
  private String descriptionWAR01;
  private java.math.BigDecimal progressiveHie02ITM01;
  private ArrayList serialNumbers = new ArrayList();

  private String variantTypeItm06WAR02;
  private String variantCodeItm11WAR02;
  private String variantTypeItm07WAR02;
  private String variantCodeItm12WAR02;
  private String variantTypeItm08WAR02;
  private String variantCodeItm13WAR02;
  private String variantTypeItm09WAR02;
  private String variantCodeItm14WAR02;
  private String variantTypeItm10WAR02;
  private String variantCodeItm15WAR02;


  public MovementVO() {
  }


  public String getQtySignWAR04() {
    return qtySignWAR04;
  }
  public void setQtySignWAR04(String qtySignWAR04) {
    this.qtySignWAR04 = qtySignWAR04;
  }
  public String getItemTypeWAR04() {
    return itemTypeWAR04;
  }
  public void setItemTypeWAR04(String itemTypeWAR04) {
    this.itemTypeWAR04 = itemTypeWAR04;
  }
  public java.math.BigDecimal getProgressiveWAR02() {
    return progressiveWAR02;
  }
  public void setProgressiveWAR02(java.math.BigDecimal progressiveWAR02) {
    this.progressiveWAR02 = progressiveWAR02;
  }
  public String getCompanyCodeSys01WAR02() {
    return companyCodeSys01WAR02;
  }
  public void setCompanyCodeSys01WAR02(String companyCodeSys01WAR02) {
    this.companyCodeSys01WAR02 = companyCodeSys01WAR02;
  }
  public String getWarehouseCodeWar01WAR02() {
    return warehouseCodeWar01WAR02;
  }
  public void setWarehouseCodeWar01WAR02(String warehouseCodeWar01WAR02) {
    this.warehouseCodeWar01WAR02 = warehouseCodeWar01WAR02;
  }
  public String getItemCodeItm01WAR02() {
    return itemCodeItm01WAR02;
  }
  public void setItemCodeItm01WAR02(String itemCodeItm01WAR02) {
    this.itemCodeItm01WAR02 = itemCodeItm01WAR02;
  }
  public BigDecimal getProgressiveHie01WAR02() {
    return progressiveHie01WAR02;
  }
  public void setProgressiveHie01WAR02(BigDecimal progressiveHie01WAR02) {
    this.progressiveHie01WAR02 = progressiveHie01WAR02;
  }
  public String getLocationDescriptionSYS10() {
    return locationDescriptionSYS10;
  }
  public void setLocationDescriptionSYS10(String locationDescriptionSYS10) {
    this.locationDescriptionSYS10 = locationDescriptionSYS10;
  }
  public java.sql.Timestamp getMovementDateWAR02() {
    return movementDateWAR02;
  }
  public void setMovementDateWAR02(java.sql.Timestamp movementDateWAR02) {
    this.movementDateWAR02 = movementDateWAR02;
  }
  public String getUsernameWAR02() {
    return usernameWAR02;
  }
  public void setUsernameWAR02(String usernameWAR02) {
    this.usernameWAR02 = usernameWAR02;
  }
  public String getNoteWAR02() {
    return noteWAR02;
  }
  public void setNoteWAR02(String noteWAR02) {
    this.noteWAR02 = noteWAR02;
  }
  public java.math.BigDecimal getDeltaQtyWAR02() {
    return deltaQtyWAR02;
  }
  public void setDeltaQtyWAR02(java.math.BigDecimal deltaQtyWAR02) {
    this.deltaQtyWAR02 = deltaQtyWAR02;
  }
  public String getWarehouseMotiveWar04WAR02() {
    return warehouseMotiveWar04WAR02;
  }
  public void setWarehouseMotiveWar04WAR02(String warehouseMotiveWar04WAR02) {
    this.warehouseMotiveWar04WAR02 = warehouseMotiveWar04WAR02;
  }
  public String getMotiveDescriptionSYS10() {
    return motiveDescriptionSYS10;
  }
  public void setMotiveDescriptionSYS10(String motiveDescriptionSYS10) {
    this.motiveDescriptionSYS10 = motiveDescriptionSYS10;
  }
  public String getItemDescriptionSYS10() {
    String aux = itemDescriptionSYS10;
//    if (aux==null)
//      return null;
//
//    if (variantTypeItm06WAR02!=null && !ApplicationConsts.JOLLY.equals(variantTypeItm06WAR02))
//      aux += " "+variantTypeItm06WAR02;
//    if (variantCodeItm11WAR02!=null && !ApplicationConsts.JOLLY.equals(variantCodeItm11WAR02))
//      aux += " "+variantCodeItm11WAR02;
//
//    if (variantTypeItm07WAR02!=null && !ApplicationConsts.JOLLY.equals(variantTypeItm07WAR02))
//      aux += " "+variantTypeItm07WAR02;
//    if (variantCodeItm12WAR02!=null && !ApplicationConsts.JOLLY.equals(variantCodeItm12WAR02))
//      aux += " "+variantCodeItm12WAR02;
//
//    if (variantTypeItm08WAR02!=null && !ApplicationConsts.JOLLY.equals(variantTypeItm08WAR02))
//      aux += " "+variantTypeItm08WAR02;
//    if (variantCodeItm13WAR02!=null && !ApplicationConsts.JOLLY.equals(variantCodeItm13WAR02))
//      aux += " "+variantCodeItm13WAR02;
//
//    if (variantTypeItm09WAR02!=null && !ApplicationConsts.JOLLY.equals(variantTypeItm09WAR02))
//      aux += " "+variantTypeItm09WAR02;
//    if (variantCodeItm14WAR02!=null && !ApplicationConsts.JOLLY.equals(variantCodeItm14WAR02))
//      aux += " "+variantCodeItm14WAR02;
//
//    if (variantTypeItm10WAR02!=null && !ApplicationConsts.JOLLY.equals(variantTypeItm10WAR02))
//      aux += " "+variantTypeItm10WAR02;
//    if (variantCodeItm15WAR02!=null && !ApplicationConsts.JOLLY.equals(variantCodeItm15WAR02))
//      aux += " "+variantCodeItm15WAR02;
    return aux;
  }
  public void setItemDescriptionSYS10(String itemDescriptionSYS10) {
    this.itemDescriptionSYS10 = itemDescriptionSYS10;
  }
  public String getDescriptionWAR01() {
    return descriptionWAR01;
  }
  public void setDescriptionWAR01(String descriptionWAR01) {
    this.descriptionWAR01 = descriptionWAR01;
  }
  public java.math.BigDecimal getProgressiveHie02ITM01() {
    return progressiveHie02ITM01;
  }
  public void setProgressiveHie02ITM01(java.math.BigDecimal progressiveHie02ITM01) {
    this.progressiveHie02ITM01 = progressiveHie02ITM01;
  }
  public void setSerialNumbers(ArrayList serialNumbers) {
    this.serialNumbers = serialNumbers;
  }
  public ArrayList getSerialNumbers() {
    return serialNumbers;
  }
  public String getVariantCodeItm11WAR02() {
    return variantCodeItm11WAR02;
  }
  public String getVariantCodeItm13WAR02() {
    return variantCodeItm13WAR02;
  }
  public String getVariantCodeItm14WAR02() {
    return variantCodeItm14WAR02;
  }
  public String getVariantCodeItm15WAR02() {
    return variantCodeItm15WAR02;
  }
  public String getVariantTypeItm06WAR02() {
    return variantTypeItm06WAR02;
  }
  public String getVariantTypeItm07WAR02() {
    return variantTypeItm07WAR02;
  }
  public String getVariantTypeItm08WAR02() {
    return variantTypeItm08WAR02;
  }
  public String getVariantTypeItm09WAR02() {
    return variantTypeItm09WAR02;
  }
  public String getVariantTypeItm10WAR02() {
    return variantTypeItm10WAR02;
  }
  public void setVariantTypeItm10WAR02(String variantTypeItm10WAR02) {
    this.variantTypeItm10WAR02 = variantTypeItm10WAR02;
  }
  public void setVariantTypeItm09WAR02(String variantTypeItm09WAR02) {
    this.variantTypeItm09WAR02 = variantTypeItm09WAR02;
  }
  public void setVariantTypeItm08WAR02(String variantTypeItm08WAR02) {
    this.variantTypeItm08WAR02 = variantTypeItm08WAR02;
  }
  public void setVariantTypeItm07WAR02(String variantTypeItm07WAR02) {
    this.variantTypeItm07WAR02 = variantTypeItm07WAR02;
  }
  public void setVariantTypeItm06WAR02(String variantTypeItm06WAR02) {
    this.variantTypeItm06WAR02 = variantTypeItm06WAR02;
  }
  public void setVariantCodeItm15WAR02(String variantCodeItm15WAR02) {
    this.variantCodeItm15WAR02 = variantCodeItm15WAR02;
  }
  public void setVariantCodeItm14WAR02(String variantCodeItm14WAR02) {
    this.variantCodeItm14WAR02 = variantCodeItm14WAR02;
  }
  public void setVariantCodeItm13WAR02(String variantCodeItm13WAR02) {
    this.variantCodeItm13WAR02 = variantCodeItm13WAR02;
  }
  public void setVariantCodeItm12WAR02(String variantCodeItm12WAR02) {
    this.variantCodeItm12WAR02 = variantCodeItm12WAR02;
  }
  public void setVariantCodeItm11WAR02(String variantCodeItm11WAR02) {
    this.variantCodeItm11WAR02 = variantCodeItm11WAR02;
  }
  public String getVariantCodeItm12WAR02() {
    return variantCodeItm12WAR02;
  }

}
