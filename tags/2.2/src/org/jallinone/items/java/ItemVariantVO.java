package org.jallinone.items.java;


import org.openswing.swing.message.receive.java.*;
import java.math.BigDecimal;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store the combination <item,variant,variant type>.</p>
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
public class ItemVariantVO extends ValueObjectImpl {

  private String tableName;
  private String companyCodeSys01;
  private String itemCodeItm01;
  private String variantCode;
  private String variantType;
  private String variantDesc;
  private String variantTypeDesc;
  private Boolean selected;
  private BigDecimal variantProgressiveSys10;
  private BigDecimal variantTypeProgressiveSys10;


  public ItemVariantVO() {
  }


  public String getCompanyCodeSys01() {
    return companyCodeSys01;
  }
  public void setCompanyCodeSys01(String companyCodeSys01) {
    this.companyCodeSys01 = companyCodeSys01;
  }
  public String getItemCodeItm01() {
    return itemCodeItm01;
  }
  public void setItemCodeItm01(String itemCodeItm01) {
    this.itemCodeItm01 = itemCodeItm01;
  }
  public Boolean getSelected() {
    return selected;
  }
  public String getVariantCode() {
    return variantCode;
  }
  public String getVariantDesc() {
    return variantDesc;
  }
  public String getVariantType() {
    return variantType;
  }
  public String getVariantTypeDesc() {
    return variantTypeDesc;
  }
  public void setVariantTypeDesc(String variantTypeDesc) {
    this.variantTypeDesc = variantTypeDesc;
  }
  public void setVariantType(String variantType) {
    this.variantType = variantType;
  }
  public void setVariantDesc(String variantDesc) {
    this.variantDesc = variantDesc;
  }
  public void setVariantCode(String variantCode) {
    this.variantCode = variantCode;
  }
  public void setSelected(Boolean selected) {
    this.selected = selected;
  }
  public BigDecimal getVariantProgressiveSys10() {
    return variantProgressiveSys10;
  }
  public BigDecimal getVariantTypeProgressiveSys10() {
    return variantTypeProgressiveSys10;
  }
  public void setVariantProgressiveSys10(BigDecimal variantProgressiveSys10) {
    this.variantProgressiveSys10 = variantProgressiveSys10;
  }
  public void setVariantTypeProgressiveSys10(BigDecimal variantTypeProgressiveSys10) {
    this.variantTypeProgressiveSys10 = variantTypeProgressiveSys10;
  }
  public String getTableName() {
    return tableName;
  }
  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

}
