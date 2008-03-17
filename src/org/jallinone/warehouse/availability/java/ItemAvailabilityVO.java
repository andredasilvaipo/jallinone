package org.jallinone.warehouse.availability.java;

import org.openswing.swing.message.receive.java.*;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store item availability in a warehouse and position.</p>
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
public class ItemAvailabilityVO extends ValueObjectImpl {

  private String companyCodeSys01WAR03;
  private String itemCodeItm01WAR03;
  private String locationDescriptionSYS10;
  private String descriptionSYS10;
  private java.math.BigDecimal availableQtyWAR03;
  private java.math.BigDecimal damagedQtyWAR03;
  private java.math.BigDecimal progressiveHie01WAR03;


  public ItemAvailabilityVO() {
  }


  public String getCompanyCodeSys01WAR03() {
    return companyCodeSys01WAR03;
  }
  public void setCompanyCodeSys01WAR03(String companyCodeSys01WAR03) {
    this.companyCodeSys01WAR03 = companyCodeSys01WAR03;
  }
  public String getItemCodeItm01WAR03() {
    return itemCodeItm01WAR03;
  }
  public void setItemCodeItm01WAR03(String itemCodeItm01WAR03) {
    this.itemCodeItm01WAR03 = itemCodeItm01WAR03;
  }
  public String getLocationDescriptionSYS10() {
    return locationDescriptionSYS10;
  }
  public void setLocationDescriptionSYS10(String locationDescriptionSYS10) {
    this.locationDescriptionSYS10 = locationDescriptionSYS10;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public java.math.BigDecimal getAvailableQtyWAR03() {
    return availableQtyWAR03;
  }
  public void setAvailableQtyWAR03(java.math.BigDecimal availableQtyWAR03) {
    this.availableQtyWAR03 = availableQtyWAR03;
  }
  public java.math.BigDecimal getDamagedQtyWAR03() {
    return damagedQtyWAR03;
  }
  public void setDamagedQtyWAR03(java.math.BigDecimal damagedQtyWAR03) {
    this.damagedQtyWAR03 = damagedQtyWAR03;
  }
  public java.math.BigDecimal getProgressiveHie01WAR03() {
    return progressiveHie01WAR03;
  }
  public void setProgressiveHie01WAR03(java.math.BigDecimal progressiveHie01WAR03) {
    this.progressiveHie01WAR03 = progressiveHie01WAR03;
  }

}
