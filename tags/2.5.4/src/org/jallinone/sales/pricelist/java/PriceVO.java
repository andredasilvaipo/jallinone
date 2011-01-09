package org.jallinone.sales.pricelist.java;

import org.jallinone.system.customizations.java.BaseValueObject;
import org.jallinone.variants.java.VariantsItemDescriptor;
import java.math.BigDecimal;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store an item price.</p>
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
public class PriceVO extends BaseValueObject implements VariantsItemDescriptor {

  private String pricelistCodeSal01SAL02;
  private String companyCodeSys01SAL02;
  private String itemCodeItm01SAL02;
  private java.math.BigDecimal valueSAL02;
  private java.sql.Date startDateSAL02;
  private java.sql.Date endDateSAL02;
  private String itemDescriptionSYS10;
  private String pricelistDescriptionSYS10;
  private java.math.BigDecimal progressiveHie02ITM01;

  private Boolean useVariant1ITM01;
  private Boolean useVariant2ITM01;
  private Boolean useVariant3ITM01;
  private Boolean useVariant4ITM01;
  private Boolean useVariant5ITM01;


  public PriceVO() {
  }


  public String getPricelistCodeSal01SAL02() {
    return pricelistCodeSal01SAL02;
  }
  public void setPricelistCodeSal01SAL02(String pricelistCodeSal01SAL02) {
    this.pricelistCodeSal01SAL02 = pricelistCodeSal01SAL02;
  }
  public String getCompanyCodeSys01SAL02() {
    return companyCodeSys01SAL02;
  }
  public void setCompanyCodeSys01SAL02(String companyCodeSys01SAL02) {
    this.companyCodeSys01SAL02 = companyCodeSys01SAL02;
  }
  public java.math.BigDecimal getValueSAL02() {
    return valueSAL02;
  }
  public void setValueSAL02(java.math.BigDecimal valueSAL02) {
    this.valueSAL02 = valueSAL02;
  }
  public java.sql.Date getStartDateSAL02() {
    return startDateSAL02;
  }
  public void setStartDateSAL02(java.sql.Date startDateSAL02) {
    this.startDateSAL02 = startDateSAL02;
  }
  public String getItemCodeItm01SAL02() {
    return itemCodeItm01SAL02;
  }
  public void setItemCodeItm01SAL02(String itemCodeItm01SAL02) {
    this.itemCodeItm01SAL02 = itemCodeItm01SAL02;
  }
  public java.sql.Date getEndDateSAL02() {
    return endDateSAL02;
  }
  public void setEndDateSAL02(java.sql.Date endDateSAL02) {
    this.endDateSAL02 = endDateSAL02;
  }
  public String getItemDescriptionSYS10() {
    return itemDescriptionSYS10;
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
  public Boolean getUseVariant1ITM01() {
    return useVariant1ITM01;
  }
  public Boolean getUseVariant2ITM01() {
    return useVariant2ITM01;
  }
  public Boolean getUseVariant3ITM01() {
    return useVariant3ITM01;
  }
  public Boolean getUseVariant5ITM01() {
    return useVariant5ITM01;
  }
  public Boolean getUseVariant4ITM01() {
    return useVariant4ITM01;
  }
  public void setUseVariant1ITM01(Boolean useVariant1ITM01) {
    this.useVariant1ITM01 = useVariant1ITM01;
  }
  public void setUseVariant2ITM01(Boolean useVariant2ITM01) {
    this.useVariant2ITM01 = useVariant2ITM01;
  }
  public void setUseVariant3ITM01(Boolean useVariant3ITM01) {
    this.useVariant3ITM01 = useVariant3ITM01;
  }
  public void setUseVariant4ITM01(Boolean useVariant4ITM01) {
    this.useVariant4ITM01 = useVariant4ITM01;
  }
  public void setUseVariant5ITM01(Boolean useVariant5ITM01) {
    this.useVariant5ITM01 = useVariant5ITM01;
  }

  public String getCompanyCodeSys01() {
    return companyCodeSys01SAL02;
  }

  public String getItemCodeItm01() {
    return itemCodeItm01SAL02;
  }
  public BigDecimal getDecimalsREG02() {
    return new BigDecimal(0); // not used;
  }
  public void setDecimalsREG02(BigDecimal decimalsREG02) {
  }

}
