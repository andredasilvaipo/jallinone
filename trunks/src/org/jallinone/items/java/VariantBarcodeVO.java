package org.jallinone.items.java;

import org.openswing.swing.message.receive.java.ValueObjectImpl;
import org.jallinone.commons.java.*;
import java.math.BigDecimal;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Barcode binded to a combination of variants of an item .</p>
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
public class VariantBarcodeVO extends ValueObjectImpl {

  private String companyCodeSys01ITM22;
  private String itemCodeItm01ITM22;
  private String descriptionSYS10;
  private String barCodeITM22;
  private String variantTypeItm06ITM22;
  private String variantCodeItm11ITM22;
  private String variantTypeItm07ITM22;
  private String variantCodeItm12ITM22;
  private String variantTypeItm08ITM22;
  private String variantCodeItm13ITM22;
  private String variantTypeItm09ITM22;
  private String variantCodeItm14ITM22;
  private String variantTypeItm10ITM22;
  private String variantCodeItm15ITM22;
  private String enabledITM22;
	private String serialNumberRequiredITM01;
	private BigDecimal progressiveHie02ITM01;


  public VariantBarcodeVO() {
  }
  public String getBarCodeITM22() {
    return barCodeITM22;
  }
  public String getCompanyCodeSys01ITM22() {
    return companyCodeSys01ITM22;
  }
  public String getDescriptionSYS10() {
    String aux = descriptionSYS10;
    if (aux==null)
      return null;

    if (variantTypeItm06ITM22!=null && !ApplicationConsts.JOLLY.equals(variantTypeItm06ITM22))
      aux += " "+variantTypeItm06ITM22;
    if (variantCodeItm11ITM22!=null && !ApplicationConsts.JOLLY.equals(variantCodeItm11ITM22))
      aux += " "+variantCodeItm11ITM22;

    if (variantTypeItm07ITM22!=null && !ApplicationConsts.JOLLY.equals(variantTypeItm07ITM22))
      aux += " "+variantTypeItm07ITM22;
    if (variantCodeItm12ITM22!=null && !ApplicationConsts.JOLLY.equals(variantCodeItm12ITM22))
      aux += " "+variantCodeItm12ITM22;

    if (variantTypeItm08ITM22!=null && !ApplicationConsts.JOLLY.equals(variantTypeItm08ITM22))
      aux += " "+variantTypeItm08ITM22;
    if (variantCodeItm13ITM22!=null && !ApplicationConsts.JOLLY.equals(variantCodeItm13ITM22))
      aux += " "+variantCodeItm13ITM22;

    if (variantTypeItm09ITM22!=null && !ApplicationConsts.JOLLY.equals(variantTypeItm09ITM22))
      aux += " "+variantTypeItm09ITM22;
    if (variantCodeItm14ITM22!=null && !ApplicationConsts.JOLLY.equals(variantCodeItm14ITM22))
      aux += " "+variantCodeItm14ITM22;

    if (variantTypeItm10ITM22!=null && !ApplicationConsts.JOLLY.equals(variantTypeItm10ITM22))
      aux += " "+variantTypeItm10ITM22;
    if (variantCodeItm15ITM22!=null && !ApplicationConsts.JOLLY.equals(variantCodeItm15ITM22))
      aux += " "+variantCodeItm15ITM22;
    return aux;
  }
  public String getItemCodeItm01ITM22() {
    return itemCodeItm01ITM22;
  }
  public String getVariantCodeItm11ITM22() {
    return variantCodeItm11ITM22;
  }
  public String getVariantCodeItm12ITM22() {
    return variantCodeItm12ITM22;
  }
  public String getVariantCodeItm13ITM22() {
    return variantCodeItm13ITM22;
  }
  public String getVariantCodeItm14ITM22() {
    return variantCodeItm14ITM22;
  }
  public String getVariantCodeItm15ITM22() {
    return variantCodeItm15ITM22;
  }
  public String getVariantTypeItm07ITM22() {
    return variantTypeItm07ITM22;
  }
  public String getVariantTypeItm08ITM22() {
    return variantTypeItm08ITM22;
  }
  public String getVariantTypeItm09ITM22() {
    return variantTypeItm09ITM22;
  }
  public String getVariantTypeItm10ITM22() {
    return variantTypeItm10ITM22;
  }
  public void setVariantTypeItm10ITM22(String variantTypeItm10ITM22) {
    this.variantTypeItm10ITM22 = variantTypeItm10ITM22;
  }
  public void setVariantTypeItm09ITM22(String variantTypeItm09ITM22) {
    this.variantTypeItm09ITM22 = variantTypeItm09ITM22;
  }
  public void setVariantTypeItm08ITM22(String variantTypeItm08ITM22) {
    this.variantTypeItm08ITM22 = variantTypeItm08ITM22;
  }
  public void setVariantTypeItm07ITM22(String variantTypeItm07ITM22) {
    this.variantTypeItm07ITM22 = variantTypeItm07ITM22;
  }
  public void setVariantTypeItm06ITM22(String variantTypeItm06ITM22) {
    this.variantTypeItm06ITM22 = variantTypeItm06ITM22;
  }
  public void setVariantCodeItm15ITM22(String variantCodeItm15ITM22) {
    this.variantCodeItm15ITM22 = variantCodeItm15ITM22;
  }
  public void setVariantCodeItm14ITM22(String variantCodeItm14ITM22) {
    this.variantCodeItm14ITM22 = variantCodeItm14ITM22;
  }
  public void setVariantCodeItm13ITM22(String variantCodeItm13ITM22) {
    this.variantCodeItm13ITM22 = variantCodeItm13ITM22;
  }
  public void setVariantCodeItm12ITM22(String variantCodeItm12ITM22) {
    this.variantCodeItm12ITM22 = variantCodeItm12ITM22;
  }
  public void setVariantCodeItm11ITM22(String variantCodeItm11ITM22) {
    this.variantCodeItm11ITM22 = variantCodeItm11ITM22;
  }
  public void setItemCodeItm01ITM22(String itemCodeItm01ITM22) {
    this.itemCodeItm01ITM22 = itemCodeItm01ITM22;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public void setCompanyCodeSys01ITM22(String companyCodeSys01ITM22) {
    this.companyCodeSys01ITM22 = companyCodeSys01ITM22;
  }
  public void setBarCodeITM22(String barCodeITM22) {
    this.barCodeITM22 = barCodeITM22;
  }
  public String getVariantTypeItm06ITM22() {
    return variantTypeItm06ITM22;
  }
  public String getEnabledITM22() {
    return enabledITM22;
  }
  public void setEnabledITM22(String enabledITM22) {
    this.enabledITM22 = enabledITM22;
  }
  public String getSerialNumberRequiredITM01() {
    return serialNumberRequiredITM01;
  }
  public void setSerialNumberRequiredITM01(String serialNumberRequiredITM01) {
    this.serialNumberRequiredITM01 = serialNumberRequiredITM01;
  }
  public BigDecimal getProgressiveHie02ITM01() {
    return progressiveHie02ITM01;
  }
  public void setProgressiveHie02ITM01(BigDecimal progressiveHie02ITM01) {
    this.progressiveHie02ITM01 = progressiveHie02ITM01;
  }



}
