package org.jallinone.purchases.items.java;

import org.openswing.swing.message.receive.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: PK of a supplier item.</p>
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
public class SupplierItemPK extends ValueObjectImpl {

  private String companyCodeSys01PUR02;
  private String itemCodeItm01PUR02;
  private java.math.BigDecimal progressiveReg04PUR02;


  public SupplierItemPK(String companyCodeSys01PUR02,String itemCodeItm01PUR02,java.math.BigDecimal progressiveReg04PUR02) {
    this.companyCodeSys01PUR02 = companyCodeSys01PUR02;
    this.itemCodeItm01PUR02 = itemCodeItm01PUR02;
    this.progressiveReg04PUR02 = progressiveReg04PUR02;
  }

  public SupplierItemPK() {}
  

  public String getCompanyCodeSys01PUR02() {
    return companyCodeSys01PUR02;
  }
  public String getItemCodeItm01PUR02() {
    return itemCodeItm01PUR02;
  }
  public java.math.BigDecimal getProgressiveReg04PUR02() {
    return progressiveReg04PUR02;
  }

  public void setCompanyCodeSys01PUR02(String companyCodeSys01PUR02) {
	  this.companyCodeSys01PUR02 = companyCodeSys01PUR02;
  }

  public void setItemCodeItm01PUR02(String itemCodeItm01PUR02) {
	  this.itemCodeItm01PUR02 = itemCodeItm01PUR02;
  }

  public void setProgressiveReg04PUR02(java.math.BigDecimal progressiveReg04PUR02) {
	  this.progressiveReg04PUR02 = progressiveReg04PUR02;
  }


  
  
}
