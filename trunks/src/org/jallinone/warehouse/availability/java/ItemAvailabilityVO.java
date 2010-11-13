package org.jallinone.warehouse.availability.java;

import org.openswing.swing.message.receive.java.*;
import org.jallinone.commons.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store item availability in a warehouse and position.</p>
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
public class ItemAvailabilityVO extends ValueObjectImpl {

  private String companyCodeSys01WAR03;
  private String itemCodeItm01WAR03;
  private String locationDescriptionSYS10;
  private String descriptionSYS10;
  private java.math.BigDecimal availableQtyWAR03;
  private java.math.BigDecimal damagedQtyWAR03;
  private java.math.BigDecimal progressiveHie01WAR03;

  private String variantTypeItm06WAR03;
  private String variantCodeItm11WAR03;
  private String variantTypeItm07WAR03;
  private String variantCodeItm12WAR03;
  private String variantTypeItm08WAR03;
  private String variantCodeItm13WAR03;
  private String variantTypeItm09WAR03;
  private String variantCodeItm14WAR03;
  private String variantTypeItm10WAR03;
  private String variantCodeItm15WAR03;


  public ItemAvailabilityVO() {
  }


  public String getCompanyCodeSys01WAR03() {
    return companyCodeSys01WAR03;
  }
  public void setCompanyCodeSys01WAR03(String companyCodeSys01WAR03) {
    this.companyCodeSys01WAR03 = companyCodeSys01WAR03;
  }
  public String getItemCodeItm01WAR03() {
    return itemCodeItm01WAR03;
  }
  public void setItemCodeItm01WAR03(String itemCodeItm01WAR03) {
    this.itemCodeItm01WAR03 = itemCodeItm01WAR03;
  }
  public String getLocationDescriptionSYS10() {
    return locationDescriptionSYS10;
  }
  public void setLocationDescriptionSYS10(String locationDescriptionSYS10) {
    this.locationDescriptionSYS10 = locationDescriptionSYS10;
  }
  public String getDescriptionSYS10() {
    String aux = descriptionSYS10;
//    if (aux==null)
//      return null;
//
//    if (variantTypeItm06WAR03!=null && !ApplicationConsts.JOLLY.equals(variantTypeItm06WAR03))
//      aux += " "+variantTypeItm06WAR03;
//    if (variantCodeItm11WAR03!=null && !ApplicationConsts.JOLLY.equals(variantCodeItm11WAR03))
//      aux += " "+variantCodeItm11WAR03;
//
//    if (variantTypeItm07WAR03!=null && !ApplicationConsts.JOLLY.equals(variantTypeItm07WAR03))
//      aux += " "+variantTypeItm07WAR03;
//    if (variantCodeItm12WAR03!=null && !ApplicationConsts.JOLLY.equals(variantCodeItm12WAR03))
//      aux += " "+variantCodeItm12WAR03;
//
//    if (variantTypeItm08WAR03!=null && !ApplicationConsts.JOLLY.equals(variantTypeItm08WAR03))
//      aux += " "+variantTypeItm08WAR03;
//    if (variantCodeItm13WAR03!=null && !ApplicationConsts.JOLLY.equals(variantCodeItm13WAR03))
//      aux += " "+variantCodeItm13WAR03;
//
//    if (variantTypeItm09WAR03!=null && !ApplicationConsts.JOLLY.equals(variantTypeItm09WAR03))
//      aux += " "+variantTypeItm09WAR03;
//    if (variantCodeItm14WAR03!=null && !ApplicationConsts.JOLLY.equals(variantCodeItm14WAR03))
//      aux += " "+variantCodeItm14WAR03;
//
//    if (variantTypeItm10WAR03!=null && !ApplicationConsts.JOLLY.equals(variantTypeItm10WAR03))
//      aux += " "+variantTypeItm10WAR03;
//    if (variantCodeItm15WAR03!=null && !ApplicationConsts.JOLLY.equals(variantCodeItm15WAR03))
//      aux += " "+variantCodeItm15WAR03;
    return aux;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public java.math.BigDecimal getAvailableQtyWAR03() {
    return availableQtyWAR03;
  }
  public void setAvailableQtyWAR03(java.math.BigDecimal availableQtyWAR03) {
    this.availableQtyWAR03 = availableQtyWAR03;
  }
  public java.math.BigDecimal getDamagedQtyWAR03() {
    return damagedQtyWAR03;
  }
  public void setDamagedQtyWAR03(java.math.BigDecimal damagedQtyWAR03) {
    this.damagedQtyWAR03 = damagedQtyWAR03;
  }
  public java.math.BigDecimal getProgressiveHie01WAR03() {
    return progressiveHie01WAR03;
  }
  public void setProgressiveHie01WAR03(java.math.BigDecimal progressiveHie01WAR03) {
    this.progressiveHie01WAR03 = progressiveHie01WAR03;
  }
  public String getVariantCodeItm11WAR03() {
    return variantCodeItm11WAR03;
  }
  public String getVariantCodeItm12WAR03() {
    return variantCodeItm12WAR03;
  }
  public String getVariantCodeItm13WAR03() {
    return variantCodeItm13WAR03;
  }
  public String getVariantCodeItm14WAR03() {
    return variantCodeItm14WAR03;
  }
  public String getVariantCodeItm15WAR03() {
    return variantCodeItm15WAR03;
  }
  public String getVariantTypeItm06WAR03() {
    return variantTypeItm06WAR03;
  }
  public String getVariantTypeItm07WAR03() {
    return variantTypeItm07WAR03;
  }
  public String getVariantTypeItm08WAR03() {
    return variantTypeItm08WAR03;
  }
  public String getVariantTypeItm09WAR03() {
    return variantTypeItm09WAR03;
  }
  public String getVariantTypeItm10WAR03() {
    return variantTypeItm10WAR03;
  }
  public void setVariantTypeItm10WAR03(String variantTypeItm10WAR03) {
    this.variantTypeItm10WAR03 = variantTypeItm10WAR03;
  }
  public void setVariantTypeItm09WAR03(String variantTypeItm09WAR03) {
    this.variantTypeItm09WAR03 = variantTypeItm09WAR03;
  }
  public void setVariantTypeItm08WAR03(String variantTypeItm08WAR03) {
    this.variantTypeItm08WAR03 = variantTypeItm08WAR03;
  }
  public void setVariantTypeItm07WAR03(String variantTypeItm07WAR03) {
    this.variantTypeItm07WAR03 = variantTypeItm07WAR03;
  }
  public void setVariantTypeItm06WAR03(String variantTypeItm06WAR03) {
    this.variantTypeItm06WAR03 = variantTypeItm06WAR03;
  }
  public void setVariantCodeItm15WAR03(String variantCodeItm15WAR03) {
    this.variantCodeItm15WAR03 = variantCodeItm15WAR03;
  }
  public void setVariantCodeItm14WAR03(String variantCodeItm14WAR03) {
    this.variantCodeItm14WAR03 = variantCodeItm14WAR03;
  }
  public void setVariantCodeItm13WAR03(String variantCodeItm13WAR03) {
    this.variantCodeItm13WAR03 = variantCodeItm13WAR03;
  }
  public void setVariantCodeItm12WAR03(String variantCodeItm12WAR03) {
    this.variantCodeItm12WAR03 = variantCodeItm12WAR03;
  }
  public void setVariantCodeItm11WAR03(String variantCodeItm11WAR03) {
    this.variantCodeItm11WAR03 = variantCodeItm11WAR03;
  }

}
