package org.jallinone.purchases.items.java;

import org.openswing.swing.message.receive.java.ValueObjectImpl;
import java.math.BigDecimal;
import org.jallinone.commons.java.ApplicationConsts;

/**
  * <p>Title: JAllInOne ERP/CRM application</p>
  * <p>Description: Value object used in the ABC classification.</p>
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
public class ABCVO extends ValueObjectImpl {

   private boolean selected;

   private BigDecimal minStockITM23;
   private String companyCodeSys01;
   private String itemCode;
   private String itemDescription;
   private String variantTypeITM06;
   private String variantTypeITM07;
   private String variantTypeITM08;
   private String variantTypeITM09;
   private String variantTypeITM10;
   private String variantCodeITM11;
   private String variantCodeITM12;
   private String variantCodeITM13;
   private String variantCodeITM14;
   private String variantCodeITM15;
   private BigDecimal sold;
   private BigDecimal unsold;
   private String unsoldGrade;
   private String invoicedGrade;


  public ABCVO() {
  }


  public String getItemCode() {
    return itemCode;
  }

  public String getItemDescription() {
    String aux = itemDescription;
//    if (aux==null)
//      return null;
//    if (variantTypeITM06!=null && !ApplicationConsts.JOLLY.equals(variantTypeITM06))
//      aux += " "+variantTypeITM06;
//    if (variantCodeITM11!=null && !ApplicationConsts.JOLLY.equals(variantCodeITM11))
//      aux += " "+variantCodeITM11;
//
//    if (variantTypeITM07!=null && !ApplicationConsts.JOLLY.equals(variantTypeITM07))
//      aux += " "+variantTypeITM07;
//    if (variantCodeITM12!=null && !ApplicationConsts.JOLLY.equals(variantCodeITM12))
//      aux += " "+variantCodeITM12;
//
//    if (variantTypeITM08!=null && !ApplicationConsts.JOLLY.equals(variantTypeITM08))
//      aux += " "+variantTypeITM08;
//    if (variantCodeITM13!=null && !ApplicationConsts.JOLLY.equals(variantCodeITM13))
//      aux += " "+variantCodeITM13;
//
//    if (variantTypeITM09!=null && !ApplicationConsts.JOLLY.equals(variantTypeITM09))
//      aux += " "+variantTypeITM09;
//    if (variantCodeITM14!=null && !ApplicationConsts.JOLLY.equals(variantCodeITM14))
//      aux += " "+variantCodeITM14;
//
//    if (variantTypeITM10!=null && !ApplicationConsts.JOLLY.equals(variantTypeITM10))
//      aux += " "+variantTypeITM10;
//    if (variantCodeITM15!=null && !ApplicationConsts.JOLLY.equals(variantCodeITM15))
//      aux += " "+variantCodeITM15;
    return aux;

  }
  public BigDecimal getMinStockITM23() {
    return minStockITM23;
  }
  public boolean isSelected() {
    return selected;
  }
  public String getVariantCodeITM11() {
    return variantCodeITM11;
  }
  public String getVariantCodeITM12() {
    return variantCodeITM12;
  }
  public String getVariantCodeITM13() {
    return variantCodeITM13;
  }
  public String getVariantCodeITM14() {
    return variantCodeITM14;
  }
  public String getVariantCodeITM15() {
    return variantCodeITM15;
  }
  public String getVariantTypeITM06() {
    return variantTypeITM06;
  }
  public String getVariantTypeITM07() {
    return variantTypeITM07;
  }
  public String getVariantTypeITM08() {
    return variantTypeITM08;
  }
  public String getVariantTypeITM09() {
    return variantTypeITM09;
  }
  public String getVariantTypeITM10() {
    return variantTypeITM10;
  }
  public void setVariantTypeITM10(String variantTypeITM10) {
    this.variantTypeITM10 = variantTypeITM10;
  }
  public void setVariantTypeITM09(String variantTypeITM09) {
    this.variantTypeITM09 = variantTypeITM09;
  }
  public void setVariantTypeITM08(String variantTypeITM08) {
    this.variantTypeITM08 = variantTypeITM08;
  }
  public void setVariantTypeITM07(String variantTypeITM07) {
    this.variantTypeITM07 = variantTypeITM07;
  }
  public void setVariantTypeITM06(String variantTypeITM06) {
    this.variantTypeITM06 = variantTypeITM06;
  }
  public void setCompanyCodeSys01(String companyCodeSys01) {
    this.companyCodeSys01 = companyCodeSys01;
  }

  public void setItemCode(String itemCode) {
    this.itemCode = itemCode;
  }
  public void setItemDescription(String itemDescription) {
    this.itemDescription = itemDescription;
  }
  public void setMinStockITM23(BigDecimal minStockITM23) {
    this.minStockITM23 = minStockITM23;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }
  public String getCompanyCodeSys01() {
    return companyCodeSys01;
  }
  public BigDecimal getSold() {
    return sold==null?new BigDecimal(0):sold;
  }
  public BigDecimal getUnsold() {
    return unsold;
  }
  public void setSold(BigDecimal sold) {
    this.sold = sold;
  }
  public void setUnsold(BigDecimal unsold) {
    this.unsold = unsold;
  }
  public void setVariantCodeITM11(String variantCodeITM11) {
    this.variantCodeITM11 = variantCodeITM11;
  }
  public void setVariantCodeITM12(String variantCodeITM12) {
    this.variantCodeITM12 = variantCodeITM12;
  }
  public void setVariantCodeITM13(String variantCodeITM13) {
    this.variantCodeITM13 = variantCodeITM13;
  }
  public void setVariantCodeITM14(String variantCodeITM14) {
    this.variantCodeITM14 = variantCodeITM14;
  }
  public void setVariantCodeITM15(String variantCodeITM15) {
    this.variantCodeITM15 = variantCodeITM15;
  }
  public String getAbc() {
    return unsoldGrade+(invoicedGrade==null?"C":invoicedGrade);
  }

  public String getUnsoldGrade() {
    return unsoldGrade;
  }
  public void setUnsoldGrade(String unsoldGrade) {
    this.unsoldGrade = unsoldGrade;
  }
  public void setInvoicedGrade(String invoicedGrade) {
    this.invoicedGrade = invoicedGrade;
  }
  public String getInvoicedGrade() {
    return invoicedGrade;
  }


}
