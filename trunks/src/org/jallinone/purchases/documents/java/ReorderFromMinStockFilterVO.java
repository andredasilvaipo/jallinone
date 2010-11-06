package org.jallinone.purchases.documents.java;

import org.openswing.swing.message.receive.java.ValueObjectImpl;
import java.math.BigDecimal;

/**
  * <p>Title: JAllInOne ERP/CRM application</p>
  * <p>Description: Value object used to store filtering conditions used to fetch data ni reorder frame.</p>
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
public class ReorderFromMinStockFilterVO extends ValueObjectImpl {

   String companyCode;
   BigDecimal progressiveREG04;
   String warehouseCode;
   String warehouseDescription;
   String currencyCodeREG03;
   String supplierCode;
   String name_1REG04;
   BigDecimal progressiveHie02ITM01;


  public ReorderFromMinStockFilterVO() {
  }


  public String getCompanyCode() {
    return companyCode;
  }
  public String getCurrencyCodeREG03() {
    return currencyCodeREG03;
  }
  public BigDecimal getProgressiveREG04() {
    return progressiveREG04;
  }
  public String getWarehouseCode() {
    return warehouseCode;
  }
  public void setWarehouseCode(String warehouseCode) {
    this.warehouseCode = warehouseCode;
  }
  public void setProgressiveREG04(BigDecimal progressiveREG04) {
    this.progressiveREG04 = progressiveREG04;
  }
  public void setCurrencyCodeREG03(String currencyCodeREG03) {
    this.currencyCodeREG03 = currencyCodeREG03;
  }
  public void setCompanyCode(String companyCode) {
    this.companyCode = companyCode;
  }
  public String getName_1REG04() {
    return name_1REG04;
  }
  public String getSupplierCode() {
    return supplierCode;
  }
  public String getWarehouseDescription() {
    return warehouseDescription;
  }
  public void setWarehouseDescription(String warehouseDescription) {
    this.warehouseDescription = warehouseDescription;
  }
  public void setSupplierCode(String supplierCode) {
    this.supplierCode = supplierCode;
  }
  public void setName_1REG04(String name_1REG04) {
    this.name_1REG04 = name_1REG04;
  }
  public BigDecimal getProgressiveHie02ITM01() {
    return progressiveHie02ITM01;
  }
  public void setProgressiveHie02ITM01(BigDecimal progressiveHie02ITM01) {
    this.progressiveHie02ITM01 = progressiveHie02ITM01;
  }


}
