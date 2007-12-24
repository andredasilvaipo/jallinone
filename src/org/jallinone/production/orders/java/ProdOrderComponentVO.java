package org.jallinone.production.orders.java;

import org.openswing.swing.message.receive.java.ValueObjectImpl;
import java.util.ArrayList;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a production order required component (table DOC24).</p>
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
public class ProdOrderComponentVO extends ValueObjectImpl {

  private String companyCodeSys01DOC24;
  private String itemCodeItm01DOC24;
  private String descriptionSYS10;
  private java.math.BigDecimal docYearDOC24;
  private java.math.BigDecimal docNumberDOC24;
  private java.math.BigDecimal qtyDOC24;
  private java.math.BigDecimal availableQty;
  private ArrayList availabilities = new ArrayList(); // collection of ItemAvailabilityVO objects
  private java.math.BigDecimal progressiveHie01DOC24; // components warehouse hierarchy
  private String locationDescriptionSYS10;
  private String minSellingQtyUmCodeReg02ITM01;


  public ProdOrderComponentVO() {
  }


  public String getCompanyCodeSys01DOC24() {
    return companyCodeSys01DOC24;
  }
  public void setCompanyCodeSys01DOC24(String companyCodeSys01DOC24) {
    this.companyCodeSys01DOC24 = companyCodeSys01DOC24;
  }
  public String getItemCodeItm01DOC24() {
    return itemCodeItm01DOC24;
  }
  public void setItemCodeItm01DOC24(String itemCodeItm01DOC24) {
    this.itemCodeItm01DOC24 = itemCodeItm01DOC24;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public java.math.BigDecimal getDocYearDOC24() {
    return docYearDOC24;
  }
  public void setDocYearDOC24(java.math.BigDecimal docYearDOC24) {
    this.docYearDOC24 = docYearDOC24;
  }
  public java.math.BigDecimal getDocNumberDOC24() {
    return docNumberDOC24;
  }
  public void setDocNumberDOC24(java.math.BigDecimal docNumberDOC24) {
    this.docNumberDOC24 = docNumberDOC24;
  }
  public java.math.BigDecimal getQtyDOC24() {
    return qtyDOC24;
  }
  public void setQtyDOC24(java.math.BigDecimal qtyDOC24) {
    this.qtyDOC24 = qtyDOC24;
  }
  public java.math.BigDecimal getAvailableQty() {
    return availableQty;
  }
  public void setAvailableQty(java.math.BigDecimal availableQty) {
    this.availableQty = availableQty;
  }
  public ArrayList getAvailabilities() {
    return availabilities;
  }
  public void setAvailabilities(ArrayList availabilities) {
    this.availabilities = availabilities;
  }
  public java.math.BigDecimal getProgressiveHie01DOC24() {
    return progressiveHie01DOC24;
  }

  public void setProgressiveHie01DOC24(java.math.BigDecimal progressiveHie01DOC24) {
    this.progressiveHie01DOC24 = progressiveHie01DOC24;
  }
  public String getLocationDescriptionSYS10() {
    return locationDescriptionSYS10;
  }
  public void setLocationDescriptionSYS10(String locationDescriptionSYS10) {
    this.locationDescriptionSYS10 = locationDescriptionSYS10;
  }
  public String getMinSellingQtyUmCodeReg02ITM01() {
    return minSellingQtyUmCodeReg02ITM01;
  }
  public void setMinSellingQtyUmCodeReg02ITM01(String minSellingQtyUmCodeReg02ITM01) {
    this.minSellingQtyUmCodeReg02ITM01 = minSellingQtyUmCodeReg02ITM01;
  }




}
