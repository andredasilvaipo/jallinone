package org.jallinone.production.orders.java;

import org.jallinone.system.customizations.java.BaseValueObject;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a production order header (table DOC22).</p>
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
public class ProdOrderVO extends BaseValueObject {


  private String companyCodeSys01DOC22;
  private String warehouseCodeWar01DOC22;
  private String warehouseCode2War01DOC22;
  private String docStateDOC22;
  private String descriptionWar01DOC22;
  private String description2War01DOC22;
  private java.math.BigDecimal docYearDOC22;
  private java.math.BigDecimal docNumberDOC22;
  private java.math.BigDecimal docSequenceDOC22;
  private java.sql.Date docDateDOC22;


  public ProdOrderVO() {
  }


  public String getCompanyCodeSys01DOC22() {
    return companyCodeSys01DOC22;
  }
  public void setCompanyCodeSys01DOC22(String companyCodeSys01DOC22) {
    this.companyCodeSys01DOC22 = companyCodeSys01DOC22;
  }
  public String getWarehouseCodeWar01DOC22() {
    return warehouseCodeWar01DOC22;
  }
  public void setWarehouseCodeWar01DOC22(String warehouseCodeWar01DOC22) {
    this.warehouseCodeWar01DOC22 = warehouseCodeWar01DOC22;
  }
  public String getWarehouseCode2War01DOC22() {
    return warehouseCode2War01DOC22;
  }
  public void setWarehouseCode2War01DOC22(String warehouseCode2War01DOC22) {
    this.warehouseCode2War01DOC22 = warehouseCode2War01DOC22;
  }
  public String getDocStateDOC22() {
    return docStateDOC22;
  }
  public void setDocStateDOC22(String docStateDOC22) {
    this.docStateDOC22 = docStateDOC22;
  }
  public String getDescriptionWar01DOC22() {
    return descriptionWar01DOC22;
  }
  public void setDescriptionWar01DOC22(String descriptionWar01DOC22) {
    this.descriptionWar01DOC22 = descriptionWar01DOC22;
  }
  public String getDescription2War01DOC22() {
    return description2War01DOC22;
  }
  public void setDescription2War01DOC22(String description2War01DOC22) {
    this.description2War01DOC22 = description2War01DOC22;
  }

  public java.math.BigDecimal getDocYearDOC22() {
    return docYearDOC22;
  }
  public void setDocYearDOC22(java.math.BigDecimal docYearDOC22) {
    this.docYearDOC22 = docYearDOC22;
  }
  public java.math.BigDecimal getDocNumberDOC22() {
    return docNumberDOC22;
  }
  public void setDocNumberDOC22(java.math.BigDecimal docNumberDOC22) {
    this.docNumberDOC22 = docNumberDOC22;
  }
  public java.math.BigDecimal getDocSequenceDOC22() {
    return docSequenceDOC22;
  }
  public void setDocSequenceDOC22(java.math.BigDecimal docSequenceDOC22) {
    this.docSequenceDOC22 = docSequenceDOC22;
  }
  public java.sql.Date getDocDateDOC22() {
    return docDateDOC22;
  }
  public void setDocDateDOC22(java.sql.Date docDateDOC22) {
    this.docDateDOC22 = docDateDOC22;
  }


}
