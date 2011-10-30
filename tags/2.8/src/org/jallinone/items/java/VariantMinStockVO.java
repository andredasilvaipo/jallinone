package org.jallinone.items.java;

import org.openswing.swing.message.receive.java.ValueObjectImpl;
import org.jallinone.commons.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: M in stock defined for a combination of variants of an item.</p>
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
public class VariantMinStockVO extends ValueObjectImpl {

  private String companyCodeSys01ITM23;
  private String itemCodeItm01ITM23;
  private String descriptionSYS10;
  private String variantTypeItm06ITM23;
  private String variantCodeItm11ITM23;
  private String variantTypeItm07ITM23;
  private String variantCodeItm12ITM23;
  private String variantTypeItm08ITM23;
  private String variantCodeItm13ITM23;
  private String variantTypeItm09ITM23;
  private String variantCodeItm14ITM23;
  private String variantTypeItm10ITM23;
  private String variantCodeItm15ITM23;
  private String enabledITM23;
  private java.math.BigDecimal minStockITM23;


  public VariantMinStockVO() {
  }
  public java.math.BigDecimal getMinStockITM23() {
    return minStockITM23;
  }
  public String getCompanyCodeSys01ITM23() {
    return companyCodeSys01ITM23;
  }
  public String getDescriptionSYS10() {
    String aux = descriptionSYS10;
    if (aux==null)
      return null;

    if (variantTypeItm06ITM23!=null && !ApplicationConsts.JOLLY.equals(variantTypeItm06ITM23))
      aux += " "+variantTypeItm06ITM23;
    if (variantCodeItm11ITM23!=null && !ApplicationConsts.JOLLY.equals(variantCodeItm11ITM23))
      aux += " "+variantCodeItm11ITM23;

    if (variantTypeItm07ITM23!=null && !ApplicationConsts.JOLLY.equals(variantTypeItm07ITM23))
      aux += " "+variantTypeItm07ITM23;
    if (variantCodeItm12ITM23!=null && !ApplicationConsts.JOLLY.equals(variantCodeItm12ITM23))
      aux += " "+variantCodeItm12ITM23;

    if (variantTypeItm08ITM23!=null && !ApplicationConsts.JOLLY.equals(variantTypeItm08ITM23))
      aux += " "+variantTypeItm08ITM23;
    if (variantCodeItm13ITM23!=null && !ApplicationConsts.JOLLY.equals(variantCodeItm13ITM23))
      aux += " "+variantCodeItm13ITM23;

    if (variantTypeItm09ITM23!=null && !ApplicationConsts.JOLLY.equals(variantTypeItm09ITM23))
      aux += " "+variantTypeItm09ITM23;
    if (variantCodeItm14ITM23!=null && !ApplicationConsts.JOLLY.equals(variantCodeItm14ITM23))
      aux += " "+variantCodeItm14ITM23;

    if (variantTypeItm10ITM23!=null && !ApplicationConsts.JOLLY.equals(variantTypeItm10ITM23))
      aux += " "+variantTypeItm10ITM23;
    if (variantCodeItm15ITM23!=null && !ApplicationConsts.JOLLY.equals(variantCodeItm15ITM23))
      aux += " "+variantCodeItm15ITM23;
    return aux;
  }
  public String getItemCodeItm01ITM23() {
    return itemCodeItm01ITM23;
  }
  public String getVariantCodeItm11ITM23() {
    return variantCodeItm11ITM23;
  }
  public String getVariantCodeItm12ITM23() {
    return variantCodeItm12ITM23;
  }
  public String getVariantCodeItm13ITM23() {
    return variantCodeItm13ITM23;
  }
  public String getVariantCodeItm14ITM23() {
    return variantCodeItm14ITM23;
  }
  public String getVariantCodeItm15ITM23() {
    return variantCodeItm15ITM23;
  }
  public String getVariantTypeItm07ITM23() {
    return variantTypeItm07ITM23;
  }
  public String getVariantTypeItm08ITM23() {
    return variantTypeItm08ITM23;
  }
  public String getVariantTypeItm09ITM23() {
    return variantTypeItm09ITM23;
  }
  public String getVariantTypeItm10ITM23() {
    return variantTypeItm10ITM23;
  }
  public void setVariantTypeItm10ITM23(String variantTypeItm10ITM23) {
    this.variantTypeItm10ITM23 = variantTypeItm10ITM23;
  }
  public void setVariantTypeItm09ITM23(String variantTypeItm09ITM23) {
    this.variantTypeItm09ITM23 = variantTypeItm09ITM23;
  }
  public void setVariantTypeItm08ITM23(String variantTypeItm08ITM23) {
    this.variantTypeItm08ITM23 = variantTypeItm08ITM23;
  }
  public void setVariantTypeItm07ITM23(String variantTypeItm07ITM23) {
    this.variantTypeItm07ITM23 = variantTypeItm07ITM23;
  }
  public void setVariantTypeItm06ITM23(String variantTypeItm06ITM23) {
    this.variantTypeItm06ITM23 = variantTypeItm06ITM23;
  }
  public void setVariantCodeItm15ITM23(String variantCodeItm15ITM23) {
    this.variantCodeItm15ITM23 = variantCodeItm15ITM23;
  }
  public void setVariantCodeItm14ITM23(String variantCodeItm14ITM23) {
    this.variantCodeItm14ITM23 = variantCodeItm14ITM23;
  }
  public void setVariantCodeItm13ITM23(String variantCodeItm13ITM23) {
    this.variantCodeItm13ITM23 = variantCodeItm13ITM23;
  }
  public void setVariantCodeItm12ITM23(String variantCodeItm12ITM23) {
    this.variantCodeItm12ITM23 = variantCodeItm12ITM23;
  }
  public void setVariantCodeItm11ITM23(String variantCodeItm11ITM23) {
    this.variantCodeItm11ITM23 = variantCodeItm11ITM23;
  }
  public void setItemCodeItm01ITM23(String itemCodeItm01ITM23) {
    this.itemCodeItm01ITM23 = itemCodeItm01ITM23;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public void setCompanyCodeSys01ITM23(String companyCodeSys01ITM23) {
    this.companyCodeSys01ITM23 = companyCodeSys01ITM23;
  }
  public void setMinStockITM23(java.math.BigDecimal minStockITM23) {
    this.minStockITM23 = minStockITM23;
  }
  public String getVariantTypeItm06ITM23() {
    return variantTypeItm06ITM23;
  }
  public String getEnabledITM23() {
    return enabledITM23;
  }
  public void setEnabledITM23(String enabledITM23) {
    this.enabledITM23 = enabledITM23;
  }



}
