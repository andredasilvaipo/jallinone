package org.jallinone.production.billsofmaterial.java;

import org.openswing.swing.message.receive.java.ValueObjectImpl;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store an alternative component (ITM04).</p>
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
public class AltComponentVO extends ValueObjectImpl {


  private String companyCodeSys01ITM04;
  private String itemCodeItm01ITM04;
  private java.math.BigDecimal progressiveITM04;
  private String currentItemCodeItm01ITM04;
  private String descriptionSYS10;
  private String minSellingQtyUmCodeReg02ITM01;


  public AltComponentVO() {
  }


  public String getCompanyCodeSys01ITM04() {
    return companyCodeSys01ITM04;
  }
  public void setCompanyCodeSys01ITM04(String companyCodeSys01ITM04) {
    this.companyCodeSys01ITM04 = companyCodeSys01ITM04;
  }
  public String getItemCodeItm01ITM04() {
    return itemCodeItm01ITM04;
  }
  public void setItemCodeItm01ITM04(String itemCodeItm01ITM04) {
    this.itemCodeItm01ITM04 = itemCodeItm01ITM04;
  }
  public java.math.BigDecimal getProgressiveITM04() {
    return progressiveITM04;
  }
  public void setProgressiveITM04(java.math.BigDecimal progressiveITM04) {
    this.progressiveITM04 = progressiveITM04;
  }
  public String getCurrentItemCodeItm01ITM04() {
    return currentItemCodeItm01ITM04;
  }
  public void setCurrentItemCodeItm01ITM04(String currentItemCodeItm01ITM04) {
    this.currentItemCodeItm01ITM04 = currentItemCodeItm01ITM04;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public String getMinSellingQtyUmCodeReg02ITM01() {
    return minSellingQtyUmCodeReg02ITM01;
  }
  public void setMinSellingQtyUmCodeReg02ITM01(String minSellingQtyUmCodeReg02ITM01) {
    this.minSellingQtyUmCodeReg02ITM01 = minSellingQtyUmCodeReg02ITM01;
  }


}
