package org.jallinone.items.java;

import org.jallinone.commons.java.*;
import org.openswing.swing.message.receive.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to define item's qty to use when printing barcode labels.</p>
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
 * Software Foundation, Inc., 775 Mass Ave, Cambridge, MA 02139, USA.
 *
 *       The author may be contacted at:
 *           maurocarniel@tin.it</p>
 *
 * @author Mauro Carniel
 * @version 1.0
 */
public class ItemToPrintVO extends ValueObjectImpl {

  private String companyCodeSys01;
  private String itemCodeItm01;

  private String variantTypeItm06;
  private String variantCodeItm11;
  private String variantTypeItm07;
  private String variantCodeItm12;
  private String variantTypeItm08;
  private String variantCodeItm13;
  private String variantTypeItm09;
  private String variantCodeItm14;
  private String variantTypeItm10;
  private String variantCodeItm15;
  private String descriptionSYS10;
  private java.math.BigDecimal qty;


  public ItemToPrintVO() {}


  public String getCompanyCodeSys01() {
    return companyCodeSys01;
  }
  public String getItemCodeItm01() {
    return itemCodeItm01;
  }
  public String getVariantCodeItm11() {
    return variantCodeItm11;
  }
  public String getVariantCodeItm12() {
    return variantCodeItm12;
  }
  public String getVariantCodeItm13() {
    return variantCodeItm13;
  }
  public String getVariantCodeItm14() {
    return variantCodeItm14;
  }
  public String getVariantCodeItm15() {
    return variantCodeItm15;
  }
  public String getVariantTypeItm06() {
    return variantTypeItm06;
  }
  public String getVariantTypeItm07() {
    return variantTypeItm07;
  }
  public String getVariantTypeItm08() {
    return variantTypeItm08;
  }
  public String getVariantTypeItm09() {
    return variantTypeItm09;
  }
  public String getVariantTypeItm10() {
    return variantTypeItm10;
  }
  public void setCompanyCodeSys01(String companyCodeSys01) {
    this.companyCodeSys01 = companyCodeSys01;
  }
  public void setItemCodeItm01(String itemCodeItm01) {
    this.itemCodeItm01 = itemCodeItm01;
  }
  public void setVariantCodeItm11(String variantCodeItm11) {
    this.variantCodeItm11 = variantCodeItm11;
  }
  public void setVariantCodeItm12(String variantCodeItm12) {
    this.variantCodeItm12 = variantCodeItm12;
  }
  public void setVariantCodeItm13(String variantCodeItm13) {
    this.variantCodeItm13 = variantCodeItm13;
  }
  public void setVariantCodeItm14(String variantCodeItm14) {
    this.variantCodeItm14 = variantCodeItm14;
  }
  public void setVariantCodeItm15(String variantCodeItm15) {
    this.variantCodeItm15 = variantCodeItm15;
  }
  public void setVariantTypeItm06(String variantTypeItm06) {
    this.variantTypeItm06 = variantTypeItm06;
  }
  public void setVariantTypeItm07(String variantTypeItm07) {
    this.variantTypeItm07 = variantTypeItm07;
  }
  public void setVariantTypeItm08(String variantTypeItm08) {
    this.variantTypeItm08 = variantTypeItm08;
  }
  public void setVariantTypeItm09(String variantTypeItm09) {
    this.variantTypeItm09 = variantTypeItm09;
  }
  public void setVariantTypeItm10(String variantTypeItm10) {
    this.variantTypeItm10 = variantTypeItm10;
  }
  public String getDescriptionSYS10() {
    String aux = descriptionSYS10;
//    if (aux==null)
//      return null;
//    if (variantTypeItm06!=null && !ApplicationConsts.JOLLY.equals(variantTypeItm06))
//      aux += " "+variantTypeItm06;
//    if (variantCodeItm11!=null && !ApplicationConsts.JOLLY.equals(variantCodeItm11))
//      aux += " "+variantCodeItm11;
//
//    if (variantTypeItm07!=null && !ApplicationConsts.JOLLY.equals(variantTypeItm07))
//      aux += " "+variantTypeItm07;
//    if (variantCodeItm12!=null && !ApplicationConsts.JOLLY.equals(variantCodeItm12))
//      aux += " "+variantCodeItm12;
//
//    if (variantTypeItm08!=null && !ApplicationConsts.JOLLY.equals(variantTypeItm08))
//      aux += " "+variantTypeItm08;
//    if (variantCodeItm13!=null && !ApplicationConsts.JOLLY.equals(variantCodeItm13))
//      aux += " "+variantCodeItm13;
//
//    if (variantTypeItm09!=null && !ApplicationConsts.JOLLY.equals(variantTypeItm09))
//      aux += " "+variantTypeItm09;
//    if (variantCodeItm14!=null && !ApplicationConsts.JOLLY.equals(variantCodeItm14))
//      aux += " "+variantCodeItm14;
//
//    if (variantTypeItm10!=null && !ApplicationConsts.JOLLY.equals(variantTypeItm10))
//      aux += " "+variantTypeItm10;
//    if (variantCodeItm15!=null && !ApplicationConsts.JOLLY.equals(variantCodeItm15))
//      aux += " "+variantCodeItm15;
    return aux;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public java.math.BigDecimal getQty() {
    return qty;
  }
  public void setQty(java.math.BigDecimal qty) {
    this.qty = qty;
  }





}
