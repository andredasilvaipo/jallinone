package org.jallinone.warehouse.availability.java;

import org.openswing.swing.message.receive.java.*;

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

}
