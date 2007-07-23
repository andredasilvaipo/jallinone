package org.jallinone.warehouse.availability.java;

import org.openswing.swing.message.receive.java.*;

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
    return itemDescriptionSYS10;
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

}
