package org.jallinone.warehouse.java;

import org.openswing.swing.message.receive.java.ValueObject;
import org.openswing.swing.message.receive.java.ValueObjectImpl;
import java.math.BigDecimal;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to define an inventory.</p>
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

public class InventoryVO extends ValueObjectImpl{

	private String companyCodeSys01WAR06;
	private BigDecimal progressiveWAR06;
	private String warehouseCodeWar01WAR06;
	private String descriptionWAR01;
	private String itemTypeWAR06;
  private String stateWAR06;
	private String descriptionWAR06;
	private java.sql.Date startDateWAR06;
	private java.sql.Date endDateWAR06;
	private String noteWAR06;
	private BigDecimal progressiveHie02WAR01;


	public InventoryVO() {
	}
  public String getCompanyCodeSys01WAR06() {
    return companyCodeSys01WAR06;
  }
  public String getDescriptionWAR06() {
    return descriptionWAR06;
  }
  public java.sql.Date getEndDateWAR06() {
    return endDateWAR06;
  }

  public String getNoteWAR06() {
    return noteWAR06;
  }
  public BigDecimal getProgressiveWAR06() {
    return progressiveWAR06;
  }
  public java.sql.Date getStartDateWAR06() {
    return startDateWAR06;
  }
  public String getStateWAR06() {
    return stateWAR06;
  }
  public String getWarehouseCodeWar01WAR06() {
    return warehouseCodeWar01WAR06;
  }
  public void setWarehouseCodeWar01WAR06(String warehouseCodeWar01WAR06) {
    this.warehouseCodeWar01WAR06 = warehouseCodeWar01WAR06;
  }
  public void setStateWAR06(String stateWAR06) {
    this.stateWAR06 = stateWAR06;
  }
  public void setStartDateWAR06(java.sql.Date startDateWAR06) {
    this.startDateWAR06 = startDateWAR06;
  }
  public void setProgressiveWAR06(BigDecimal progressiveWAR06) {
    this.progressiveWAR06 = progressiveWAR06;
  }
  public void setNoteWAR06(String noteWAR06) {
    this.noteWAR06 = noteWAR06;
  }

  public void setEndDateWAR06(java.sql.Date endDateWAR06) {
    this.endDateWAR06 = endDateWAR06;
  }
  public void setDescriptionWAR06(String descriptionWAR06) {
    this.descriptionWAR06 = descriptionWAR06;
  }
  public void setCompanyCodeSys01WAR06(String companyCodeSys01WAR06) {
    this.companyCodeSys01WAR06 = companyCodeSys01WAR06;
  }
  public String getItemTypeWAR06() {
    return itemTypeWAR06;
  }
  public void setItemTypeWAR06(String itemTypeWAR06) {
    this.itemTypeWAR06 = itemTypeWAR06;
  }
  public String getDescriptionWAR01() {
    return descriptionWAR01;
  }
  public void setDescriptionWAR01(String descriptionWAR01) {
    this.descriptionWAR01 = descriptionWAR01;
  }
  public BigDecimal getProgressiveHie02WAR01() {
    return progressiveHie02WAR01;
  }
  public void setProgressiveHie02WAR01(BigDecimal progressiveHie02WAR01) {
    this.progressiveHie02WAR01 = progressiveHie02WAR01;
  }





}
