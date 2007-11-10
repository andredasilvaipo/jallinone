package org.jallinone.warehouse.movements.java;

import org.openswing.swing.message.receive.java.*;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a warehouse movement for a grid frame.</p>
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
public class GridWarehouseMovementVO extends ValueObjectImpl {

  private java.math.BigDecimal progressiveWAR02;
  private java.math.BigDecimal progressiveHie01WAR02;
  private java.math.BigDecimal deltaQtyWAR02;
  private String companyCodeSys01WAR02;
  private String warehouseCodeWar01WAR02;
  private String warehouseDescriptionSYS10;
  private String itemCodeItm01WAR02;
  private String usernameWAR02;
  private String noteWAR02;
  private String warehouseMotiveWar04WAR02;
  private String warehouseMotiveDescriptionSYS10;
  private java.sql.Timestamp movementDateWAR02;
  private String locationDescriptionSYS10;
  private String itemTypeWAR04;
  private String qtySignWAR04;


  public GridWarehouseMovementVO() {
  }


  public java.math.BigDecimal getProgressiveWAR02() {
    return progressiveWAR02;
  }
  public void setProgressiveWAR02(java.math.BigDecimal progressiveWAR02) {
    this.progressiveWAR02 = progressiveWAR02;
  }
  public java.math.BigDecimal getProgressiveHie01WAR02() {
    return progressiveHie01WAR02;
  }
  public void setProgressiveHie01WAR02(java.math.BigDecimal progressiveHie01WAR02) {
    this.progressiveHie01WAR02 = progressiveHie01WAR02;
  }
  public java.math.BigDecimal getDeltaQtyWAR02() {
    return deltaQtyWAR02;
  }
  public void setDeltaQtyWAR02(java.math.BigDecimal deltaQtyWAR02) {
    this.deltaQtyWAR02 = deltaQtyWAR02;
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
  public String getWarehouseDescriptionSYS10() {
    return warehouseDescriptionSYS10;
  }
  public void setWarehouseDescriptionSYS10(String warehouseDescriptionSYS10) {
    this.warehouseDescriptionSYS10 = warehouseDescriptionSYS10;
  }
  public String getItemCodeItm01WAR02() {
    return itemCodeItm01WAR02;
  }
  public void setItemCodeItm01WAR02(String itemCodeItm01WAR02) {
    this.itemCodeItm01WAR02 = itemCodeItm01WAR02;
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
  public String getWarehouseMotiveWar04WAR02() {
    return warehouseMotiveWar04WAR02;
  }
  public void setWarehouseMotiveWar04WAR02(String warehouseMotiveWar04WAR02) {
    this.warehouseMotiveWar04WAR02 = warehouseMotiveWar04WAR02;
  }
  public String getWarehouseMotiveDescriptionSYS10() {
    return warehouseMotiveDescriptionSYS10;
  }
  public void setWarehouseMotiveDescriptionSYS10(String warehouseMotiveDescriptionSYS10) {
    this.warehouseMotiveDescriptionSYS10 = warehouseMotiveDescriptionSYS10;
  }
  public java.sql.Timestamp getMovementDateWAR02() {
    return movementDateWAR02;
  }
  public void setMovementDateWAR02(java.sql.Timestamp movementDateWAR02) {
    this.movementDateWAR02 = movementDateWAR02;
  }
  public String getLocationDescriptionSYS10() {
    return locationDescriptionSYS10;
  }
  public void setLocationDescriptionSYS10(String locationDescriptionSYS10) {
    this.locationDescriptionSYS10 = locationDescriptionSYS10;
  }
  public String getItemTypeWAR04() {
    return itemTypeWAR04;
  }
  public void setItemTypeWAR04(String itemTypeWAR04) {
    this.itemTypeWAR04 = itemTypeWAR04;
  }
  public String getQtySignWAR04() {
    return qtySignWAR04;
  }
  public void setQtySignWAR04(String qtySignWAR04) {
    this.qtySignWAR04 = qtySignWAR04;
  }

}
