package org.jallinone.production.billsofmaterial.java;

import org.openswing.swing.message.receive.java.ValueObjectImpl;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a product/component (ITM01/ITM03),
* used to construct the bills of material.</p>
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
public class MaterialVO extends ValueObjectImpl {


  private String companyCodeSys01ITM03;
  private String itemCodeItm01ITM03;
  private String descriptionSYS10;

  /** may be null if this component is not a product */
  private String parentItemCodeItm01ITM03;
  private java.math.BigDecimal valuePUR04;
  private java.math.BigDecimal qtyITM03;
  private java.math.BigDecimal valuePRO02;
  private java.math.BigDecimal totalPrices;
  private java.math.BigDecimal totalCosts;
  private String minSellingQtyUmCodeReg02ITM01;


  public MaterialVO() {
  }


  public String getCompanyCodeSys01ITM03() {
    return companyCodeSys01ITM03;
  }
  public void setCompanyCodeSys01ITM03(String companyCodeSys01ITM03) {
    this.companyCodeSys01ITM03 = companyCodeSys01ITM03;
  }
  public String getItemCodeItm01ITM03() {
    return itemCodeItm01ITM03;
  }
  public void setItemCodeItm01ITM03(String itemCodeItm01ITM03) {
    this.itemCodeItm01ITM03 = itemCodeItm01ITM03;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public String getParentItemCodeItm01ITM03() {
    return parentItemCodeItm01ITM03;
  }
  public void setParentItemCodeItm01ITM03(String parentItemCodeItm01ITM03) {
    this.parentItemCodeItm01ITM03 = parentItemCodeItm01ITM03;
  }


  public String getLongDescriptionSYS10() {
    return itemCodeItm01ITM03+" - "+descriptionSYS10;
  }
  public java.math.BigDecimal getValuePUR04() {
    return valuePUR04;
  }
  public void setValuePUR04(java.math.BigDecimal valuePUR04) {
    this.valuePUR04 = valuePUR04;
  }
  public java.math.BigDecimal getQtyITM03() {
    return qtyITM03;
  }
  public void setQtyITM03(java.math.BigDecimal qtyITM03) {
    this.qtyITM03 = qtyITM03;
  }
  public java.math.BigDecimal getValuePRO02() {
    return valuePRO02;
  }
  public void setValuePRO02(java.math.BigDecimal valuePRO02) {
    this.valuePRO02 = valuePRO02;
  }
  public java.math.BigDecimal getTotalPrices() {
    return totalPrices;
  }
  public void setTotalPrices(java.math.BigDecimal totalPrices) {
    this.totalPrices = totalPrices;
  }
  public java.math.BigDecimal getTotalCosts() {
    return totalCosts;
  }
  public void setTotalCosts(java.math.BigDecimal totalCosts) {
    this.totalCosts = totalCosts;
  }
  public String getMinSellingQtyUmCodeReg02ITM01() {
    return minSellingQtyUmCodeReg02ITM01;
  }
  public void setMinSellingQtyUmCodeReg02ITM01(String minSellingQtyUmCodeReg02ITM01) {
    this.minSellingQtyUmCodeReg02ITM01 = minSellingQtyUmCodeReg02ITM01;
  }




}
