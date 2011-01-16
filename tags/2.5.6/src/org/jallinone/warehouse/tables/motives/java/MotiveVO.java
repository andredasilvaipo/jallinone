package org.jallinone.warehouse.tables.motives.java;

import org.openswing.swing.message.receive.java.*;


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
public class MotiveVO extends ValueObjectImpl {

  private String warehouseMotiveWAR04;
  private java.math.BigDecimal progressiveSys10WAR04;
  private String qtySignWAR04;
  private String descriptionSYS10;
  private String itemTypeWAR04;
  private String enabledWAR04;


  public MotiveVO() {
  }


  public String getWarehouseMotiveWAR04() {
    return warehouseMotiveWAR04;
  }
  public void setWarehouseMotiveWAR04(String warehouseMotiveWAR04) {
    this.warehouseMotiveWAR04 = warehouseMotiveWAR04;
  }
  public java.math.BigDecimal getProgressiveSys10WAR04() {
    return progressiveSys10WAR04;
  }
  public void setProgressiveSys10WAR04(java.math.BigDecimal progressiveSys10WAR04) {
    this.progressiveSys10WAR04 = progressiveSys10WAR04;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
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
  public String getEnabledWAR04() {
    return enabledWAR04;
  }
  public void setEnabledWAR04(String enabledWAR04) {
    this.enabledWAR04 = enabledWAR04;
  }

}
