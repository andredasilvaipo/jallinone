package org.jallinone.production.billsofmaterial.java;

import org.openswing.swing.message.receive.java.ValueObjectImpl;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a component (ITM03).</p>
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
public class ComponentVO extends ValueObjectImpl {


  private String companyCodeSys01ITM03;
  private String itemCodeItm01ITM03;
  private java.math.BigDecimal sequenceITM03;
  private java.math.BigDecimal versionITM03;
  private java.math.BigDecimal revisionITM03;
  private java.math.BigDecimal qtyITM03;
  private String minSellingQtyUmCodeReg02ITM01;
  private java.sql.Date startDateITM03;
  private java.sql.Date endDateITM03;
  private String descriptionSYS10;
  private String enabledITM03;

  /** item type */
  private java.math.BigDecimal progressiveHIE02;
  private String parentItemCodeItm01ITM03;


  public ComponentVO() {
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
  public String getParentItemCodeItm01ITM03() {
    return parentItemCodeItm01ITM03;
  }
  public void setParentItemCodeItm01ITM03(String parentItemCodeItm01ITM03) {
    this.parentItemCodeItm01ITM03 = parentItemCodeItm01ITM03;
  }
  public java.math.BigDecimal getSequenceITM03() {
    return sequenceITM03;
  }
  public void setSequenceITM03(java.math.BigDecimal sequenceITM03) {
    this.sequenceITM03 = sequenceITM03;
  }
  public java.math.BigDecimal getVersionITM03() {
    return versionITM03;
  }
  public void setVersionITM03(java.math.BigDecimal versionITM03) {
    this.versionITM03 = versionITM03;
  }
  public java.math.BigDecimal getRevisionITM03() {
    return revisionITM03;
  }
  public void setRevisionITM03(java.math.BigDecimal revisionITM03) {
    this.revisionITM03 = revisionITM03;
  }
  public java.math.BigDecimal getQtyITM03() {
    return qtyITM03;
  }
  public void setQtyITM03(java.math.BigDecimal qtyITM03) {
    this.qtyITM03 = qtyITM03;
  }
  public java.sql.Date getStartDateITM03() {
    return startDateITM03;
  }
  public void setStartDateITM03(java.sql.Date startDateITM03) {
    this.startDateITM03 = startDateITM03;
  }
  public java.sql.Date getEndDateITM03() {
    return endDateITM03;
  }
  public void setEndDateITM03(java.sql.Date endDateITM03) {
    this.endDateITM03 = endDateITM03;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public java.math.BigDecimal getProgressiveHIE02() {
    return progressiveHIE02;
  }
  public void setProgressiveHIE02(java.math.BigDecimal progressiveHIE02) {
    this.progressiveHIE02 = progressiveHIE02;
  }
  public String getEnabledITM03() {
    return enabledITM03;
  }
  public void setEnabledITM03(String enabledITM03) {
    this.enabledITM03 = enabledITM03;
  }
  public String getMinSellingQtyUmCodeReg02ITM01() {
    return minSellingQtyUmCodeReg02ITM01;
  }
  public void setMinSellingQtyUmCodeReg02ITM01(String minSellingQtyUmCodeReg02ITM01) {
    this.minSellingQtyUmCodeReg02ITM01 = minSellingQtyUmCodeReg02ITM01;
  }


}
