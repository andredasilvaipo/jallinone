package org.jallinone.sales.pricelist.java;

import org.jallinone.system.customizations.java.BaseValueObject;
import org.jallinone.commons.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store the price related to a combination of variants for an item.</p>
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
public class VariantsPriceVO extends BaseValueObject {

  private String pricelistCodeSal01SAL11;
  private String companyCodeSys01SAL11;
  private String itemCodeItm01SAL11;
  private java.math.BigDecimal valueSAL11;
  private java.sql.Date startDateSAL11;
  private java.sql.Date endDateSAL11;
  private String itemDescriptionSYS10;
  private String pricelistDescriptionSYS10;
  private java.math.BigDecimal progressiveHie02ITM01;

  private String variantTypeItm06SAL11;
  private String variantCodeItm11SAL11;
  private String variantTypeItm07SAL11;
  private String variantCodeItm12SAL11;
  private String variantTypeItm08SAL11;
  private String variantCodeItm13SAL11;
  private String variantTypeItm09SAL11;
  private String variantCodeItm14SAL11;
  private String variantTypeItm10SAL11;
  private String variantCodeItm15SAL11;


  public VariantsPriceVO() {
  }


  public String getPricelistCodeSal01SAL11() {
    return pricelistCodeSal01SAL11;
  }
  public void setPricelistCodeSal01SAL11(String pricelistCodeSal01SAL11) {
    this.pricelistCodeSal01SAL11 = pricelistCodeSal01SAL11;
  }
  public String getCompanyCodeSys01SAL11() {
    return companyCodeSys01SAL11;
  }
  public void setCompanyCodeSys01SAL11(String companyCodeSys01SAL11) {
    this.companyCodeSys01SAL11 = companyCodeSys01SAL11;
  }
  public java.math.BigDecimal getValueSAL11() {
    return valueSAL11;
  }
  public void setValueSAL11(java.math.BigDecimal valueSAL11) {
    this.valueSAL11 = valueSAL11;
  }
  public java.sql.Date getStartDateSAL11() {
    return startDateSAL11;
  }
  public void setStartDateSAL11(java.sql.Date startDateSAL11) {
    this.startDateSAL11 = startDateSAL11;
  }
  public String getItemCodeItm01SAL11() {
    return itemCodeItm01SAL11;
  }
  public void setItemCodeItm01SAL11(String itemCodeItm01SAL11) {
    this.itemCodeItm01SAL11 = itemCodeItm01SAL11;
  }
  public java.sql.Date getEndDateSAL11() {
    return endDateSAL11;
  }
  public void setEndDateSAL11(java.sql.Date endDateSAL11) {
    this.endDateSAL11 = endDateSAL11;
  }
  public String getItemDescriptionSYS10() {
    String aux = itemDescriptionSYS10;
    if (aux==null)
      return null;

    if (variantTypeItm06SAL11!=null && !ApplicationConsts.JOLLY.equals(variantTypeItm06SAL11))
      aux += " "+variantTypeItm06SAL11;
    if (variantCodeItm11SAL11!=null && !ApplicationConsts.JOLLY.equals(variantCodeItm11SAL11))
      aux += " "+variantCodeItm11SAL11;

    if (variantTypeItm07SAL11!=null && !ApplicationConsts.JOLLY.equals(variantTypeItm07SAL11))
      aux += " "+variantTypeItm07SAL11;
    if (variantCodeItm12SAL11!=null && !ApplicationConsts.JOLLY.equals(variantCodeItm12SAL11))
      aux += " "+variantCodeItm12SAL11;

    if (variantTypeItm08SAL11!=null && !ApplicationConsts.JOLLY.equals(variantTypeItm08SAL11))
      aux += " "+variantTypeItm08SAL11;
    if (variantCodeItm13SAL11!=null && !ApplicationConsts.JOLLY.equals(variantCodeItm13SAL11))
      aux += " "+variantCodeItm13SAL11;

    if (variantTypeItm09SAL11!=null && !ApplicationConsts.JOLLY.equals(variantTypeItm09SAL11))
      aux += " "+variantTypeItm09SAL11;
    if (variantCodeItm14SAL11!=null && !ApplicationConsts.JOLLY.equals(variantCodeItm14SAL11))
      aux += " "+variantCodeItm14SAL11;

    if (variantTypeItm10SAL11!=null && !ApplicationConsts.JOLLY.equals(variantTypeItm10SAL11))
      aux += " "+variantTypeItm10SAL11;
    if (variantCodeItm15SAL11!=null && !ApplicationConsts.JOLLY.equals(variantCodeItm15SAL11))
      aux += " "+variantCodeItm15SAL11;
    return aux;
  }
  public void setItemDescriptionSYS10(String itemDescriptionSYS10) {
    this.itemDescriptionSYS10 = itemDescriptionSYS10;
  }
  public String getPricelistDescriptionSYS10() {
    return pricelistDescriptionSYS10;
  }
  public void setPricelistDescriptionSYS10(String pricelistDescriptionSYS10) {
    this.pricelistDescriptionSYS10 = pricelistDescriptionSYS10;
  }
  public java.math.BigDecimal getProgressiveHie02ITM01() {
    return progressiveHie02ITM01;
  }
  public void setProgressiveHie02ITM01(java.math.BigDecimal progressiveHie02ITM01) {
    this.progressiveHie02ITM01 = progressiveHie02ITM01;
  }
  public String getVariantCodeItm11SAL11() {
    return variantCodeItm11SAL11;
  }
  public String getVariantCodeItm12SAL11() {
    return variantCodeItm12SAL11;
  }
  public String getVariantCodeItm13SAL11() {
    return variantCodeItm13SAL11;
  }
  public String getVariantCodeItm14SAL11() {
    return variantCodeItm14SAL11;
  }
  public String getVariantCodeItm15SAL11() {
    return variantCodeItm15SAL11;
  }
  public String getVariantTypeItm06SAL11() {
    return variantTypeItm06SAL11;
  }
  public String getVariantTypeItm07SAL11() {
    return variantTypeItm07SAL11;
  }
  public String getVariantTypeItm08SAL11() {
    return variantTypeItm08SAL11;
  }
  public String getVariantTypeItm09SAL11() {
    return variantTypeItm09SAL11;
  }
  public String getVariantTypeItm10SAL11() {
    return variantTypeItm10SAL11;
  }
  public void setVariantTypeItm10SAL11(String variantTypeItm10SAL11) {
    this.variantTypeItm10SAL11 = variantTypeItm10SAL11;
  }
  public void setVariantTypeItm09SAL11(String variantTypeItm09SAL11) {
    this.variantTypeItm09SAL11 = variantTypeItm09SAL11;
  }
  public void setVariantTypeItm08SAL11(String variantTypeItm08SAL11) {
    this.variantTypeItm08SAL11 = variantTypeItm08SAL11;
  }
  public void setVariantTypeItm07SAL11(String variantTypeItm07SAL11) {
    this.variantTypeItm07SAL11 = variantTypeItm07SAL11;
  }
  public void setVariantTypeItm06SAL11(String variantTypeItm06SAL11) {
    this.variantTypeItm06SAL11 = variantTypeItm06SAL11;
  }
  public void setVariantCodeItm15SAL11(String variantCodeItm15SAL11) {
    this.variantCodeItm15SAL11 = variantCodeItm15SAL11;
  }
  public void setVariantCodeItm14SAL11(String variantCodeItm14SAL11) {
    this.variantCodeItm14SAL11 = variantCodeItm14SAL11;
  }
  public void setVariantCodeItm13SAL11(String variantCodeItm13SAL11) {
    this.variantCodeItm13SAL11 = variantCodeItm13SAL11;
  }
  public void setVariantCodeItm12SAL11(String variantCodeItm12SAL11) {
    this.variantCodeItm12SAL11 = variantCodeItm12SAL11;
  }
  public void setVariantCodeItm11SAL11(String variantCodeItm11SAL11) {
    this.variantCodeItm11SAL11 = variantCodeItm11SAL11;
  }


}
