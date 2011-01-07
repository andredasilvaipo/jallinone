package org.jallinone.items.java;

import org.jallinone.system.customizations.java.BaseValueObject;
import org.jallinone.variants.java.VariantsItemDescriptor;
import java.math.BigDecimal;
import java.sql.Date;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store item info for the detail.</p>
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
public class DetailItemVO extends BaseValueObject implements VariantsItemDescriptor {


  private String companyCodeSys01ITM01;
  private String itemCodeITM01;
  private String descriptionSYS10;
  private java.math.BigDecimal progressiveHie02ITM01;
  private java.math.BigDecimal progressiveHie01ITM01;
  private java.math.BigDecimal addProgressiveSys10ITM01;
  private java.math.BigDecimal progressiveSys10ITM01;
  private String addDescriptionSYS10;
  private java.math.BigDecimal minSellingQtyITM01;
  private String minSellingQtyUmCodeReg02ITM01;
  private java.math.BigDecimal minSellingQtyDecimalsREG02;
  private String vatCodeReg01ITM01;
  private String vatDescriptionSYS10;
  private java.math.BigDecimal grossWeightITM01;
  private String grossWeightUmCodeReg02ITM01;
  private java.math.BigDecimal netWeightITM01;
  private java.math.BigDecimal netWeightDecimalsREG02;
  private java.math.BigDecimal grossWeightDecimalsREG02;
  private String netWeightUmCodeReg02ITM01;
  private java.math.BigDecimal widthITM01;
  private java.math.BigDecimal widthDecimalsREG02;
  private String widthUmCodeReg02ITM01;
  private java.math.BigDecimal heightITM01;
  private java.math.BigDecimal heightDecimalsREG02;
  private String heightUmCodeReg02ITM01;
  private String noteITM01;
  private String enabledITM01;
  private String levelDescriptionSYS10;
  private String largeImageITM01;
  private String smallImageITM01;
  private byte[] smallImage;
  private byte[] largeImage;
  private java.math.BigDecimal vatValueREG01;
  private String colorDescriptionSYS10;
  private String sizeDescriptionSYS10;
  private java.math.BigDecimal vatDeductibleREG01;
  private Boolean serialNumberRequiredITM01;
  private java.math.BigDecimal versionITM01;
  private java.math.BigDecimal revisionITM01;
  private String manufactureCodePro01ITM01;
  private java.sql.Date startDateITM01;
  private String manufactureDescriptionSYS10;

  private Boolean useVariant1ITM01;
  private Boolean useVariant2ITM01;
  private Boolean useVariant3ITM01;
  private Boolean useVariant4ITM01;
  private Boolean useVariant5ITM01;

  private String barCodeITM01;
  private String barcodeTypeITM01;

  private Date lastPurchaseDate;
  private java.math.BigDecimal lastPurchaseCost;
  private java.math.BigDecimal lastPurchaseCostDecimals;
  private String lastPurchaseCostCurrencySymbol;
  private String lastPurchaseCostDecimalSymbol;
  private String lastPurchaseCostThousandSymbol;


  private java.math.BigDecimal avgPurchaseCost;
  private java.math.BigDecimal minStockITM23;

  private java.math.BigDecimal progressiveHie01HIE02;

	private Boolean noWarehouseMovITM01;
	private String sheetCodeItm25ITM01;


  public DetailItemVO() {
  }

  public String getCompanyCodeSys01ITM01() {
    return companyCodeSys01ITM01;
  }
  public void setCompanyCodeSys01ITM01(String companyCodeSys01ITM01) {
    this.companyCodeSys01ITM01 = companyCodeSys01ITM01;
  }
  public String getItemCodeITM01() {
    return itemCodeITM01;
  }
  public void setItemCodeITM01(String itemCodeITM01) {
    this.itemCodeITM01 = itemCodeITM01;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public java.math.BigDecimal getProgressiveHie02ITM01() {
    return progressiveHie02ITM01;
  }
  public void setProgressiveHie02ITM01(java.math.BigDecimal progressiveHie02ITM01) {
    this.progressiveHie02ITM01 = progressiveHie02ITM01;
  }
  public java.math.BigDecimal getProgressiveHie01ITM01() {
    return progressiveHie01ITM01;
  }
  public void setProgressiveHie01ITM01(java.math.BigDecimal progressiveHie01ITM01) {
    this.progressiveHie01ITM01 = progressiveHie01ITM01;
  }
  public java.math.BigDecimal getAddProgressiveSys10ITM01() {
    return addProgressiveSys10ITM01;
  }
  public void setAddProgressiveSys10ITM01(java.math.BigDecimal addProgressiveSys10ITM01) {
    this.addProgressiveSys10ITM01 = addProgressiveSys10ITM01;
  }
  public java.math.BigDecimal getProgressiveSys10ITM01() {
    return progressiveSys10ITM01;
  }
  public void setProgressiveSys10ITM01(java.math.BigDecimal progressiveSys10ITM01) {
    this.progressiveSys10ITM01 = progressiveSys10ITM01;
  }
  public String getAddDescriptionSYS10() {
    return addDescriptionSYS10;
  }
  public void setAddDescriptionSYS10(String addDescriptionSYS10) {
    this.addDescriptionSYS10 = addDescriptionSYS10;
  }
  public java.math.BigDecimal getMinSellingQtyITM01() {
    return minSellingQtyITM01;
  }
  public void setMinSellingQtyITM01(java.math.BigDecimal minSellingQtyITM01) {
    this.minSellingQtyITM01 = minSellingQtyITM01;
  }
  public String getMinSellingQtyUmCodeReg02ITM01() {
    return minSellingQtyUmCodeReg02ITM01;
  }
  public void setMinSellingQtyUmCodeReg02ITM01(String minSellingQtyUmCodeReg02ITM01) {
    this.minSellingQtyUmCodeReg02ITM01 = minSellingQtyUmCodeReg02ITM01;
  }
  public java.math.BigDecimal getMinSellingQtyDecimalsREG02() {
    return minSellingQtyDecimalsREG02;
  }
  public void setMinSellingQtyDecimalsREG02(java.math.BigDecimal minSellingQtyDecimalsREG02) {
    this.minSellingQtyDecimalsREG02 = minSellingQtyDecimalsREG02;
  }
  public String getVatCodeReg01ITM01() {
    return vatCodeReg01ITM01;
  }
  public void setVatCodeReg01ITM01(String vatCodeReg01ITM01) {
    this.vatCodeReg01ITM01 = vatCodeReg01ITM01;
  }
  public String getVatDescriptionSYS10() {
    return vatDescriptionSYS10;
  }
  public void setVatDescriptionSYS10(String vatDescriptionSYS10) {
    this.vatDescriptionSYS10 = vatDescriptionSYS10;
  }
  public java.math.BigDecimal getGrossWeightITM01() {
    return grossWeightITM01;
  }
  public void setGrossWeightITM01(java.math.BigDecimal grossWeightITM01) {
    this.grossWeightITM01 = grossWeightITM01;
  }
  public String getGrossWeightUmCodeReg02ITM01() {
    return grossWeightUmCodeReg02ITM01;
  }
  public void setGrossWeightUmCodeReg02ITM01(String grossWeightUmCodeReg02ITM01) {
    this.grossWeightUmCodeReg02ITM01 = grossWeightUmCodeReg02ITM01;
  }
  public java.math.BigDecimal getNetWeightITM01() {
    return netWeightITM01;
  }
  public void setNetWeightITM01(java.math.BigDecimal netWeightITM01) {
    this.netWeightITM01 = netWeightITM01;
  }
  public java.math.BigDecimal getNetWeightDecimalsREG02() {
    return netWeightDecimalsREG02;
  }
  public void setNetWeightDecimalsREG02(java.math.BigDecimal netWeightDecimalsREG02) {
    this.netWeightDecimalsREG02 = netWeightDecimalsREG02;
  }
  public java.math.BigDecimal getGrossWeightDecimalsREG02() {
    return grossWeightDecimalsREG02;
  }
  public void setGrossWeightDecimalsREG02(java.math.BigDecimal grossWeightDecimalsREG02) {
    this.grossWeightDecimalsREG02 = grossWeightDecimalsREG02;
  }
  public String getNetWeightUmCodeReg02ITM01() {
    return netWeightUmCodeReg02ITM01;
  }
  public void setNetWeightUmCodeReg02ITM01(String netWeightUmCodeReg02ITM01) {
    this.netWeightUmCodeReg02ITM01 = netWeightUmCodeReg02ITM01;
  }
  public java.math.BigDecimal getWidthITM01() {
    return widthITM01;
  }
  public void setWidthITM01(java.math.BigDecimal widthITM01) {
    this.widthITM01 = widthITM01;
  }
  public java.math.BigDecimal getWidthDecimalsREG02() {
    return widthDecimalsREG02;
  }
  public void setWidthDecimalsREG02(java.math.BigDecimal widthDecimalsREG02) {
    this.widthDecimalsREG02 = widthDecimalsREG02;
  }
  public String getWidthUmCodeReg02ITM01() {
    return widthUmCodeReg02ITM01;
  }
  public void setWidthUmCodeReg02ITM01(String widthUmCodeReg02ITM01) {
    this.widthUmCodeReg02ITM01 = widthUmCodeReg02ITM01;
  }
  public java.math.BigDecimal getHeightITM01() {
    return heightITM01;
  }
  public void setHeightITM01(java.math.BigDecimal heightITM01) {
    this.heightITM01 = heightITM01;
  }
  public java.math.BigDecimal getHeightDecimalsREG02() {
    return heightDecimalsREG02;
  }
  public void setHeightDecimalsREG02(java.math.BigDecimal heightDecimalsREG02) {
    this.heightDecimalsREG02 = heightDecimalsREG02;
  }
  public String getHeightUmCodeReg02ITM01() {
    return heightUmCodeReg02ITM01;
  }
  public void setHeightUmCodeReg02ITM01(String heightUmCodeReg02ITM01) {
    this.heightUmCodeReg02ITM01 = heightUmCodeReg02ITM01;
  }
  public String getNoteITM01() {
    return noteITM01;
  }
  public void setNoteITM01(String noteITM01) {
    this.noteITM01 = noteITM01;
  }

  public String getEnabledITM01() {
    return enabledITM01;
  }
  public void setEnabledITM01(String enabledITM01) {
    this.enabledITM01 = enabledITM01;
  }
  public String getSmallImageITM01() {
    return smallImageITM01;
  }
  public void setSmallImageITM01(String smallImageITM01) {
    this.smallImageITM01 = smallImageITM01;
  }
  public String getLargeImageITM01() {
    return largeImageITM01;
  }
  public void setLargeImageITM01(String largeImageITM01) {
    this.largeImageITM01 = largeImageITM01;
  }
  public String getLevelDescriptionSYS10() {
    return levelDescriptionSYS10;
  }
  public void setLevelDescriptionSYS10(String levelDescriptionSYS10) {
    this.levelDescriptionSYS10 = levelDescriptionSYS10;
  }
  public byte[] getSmallImage() {
    return smallImage;
  }
  public void setSmallImage(byte[] smallImage) {
    this.smallImage = smallImage;
  }
  public byte[] getLargeImage() {
    return largeImage;
  }
  public void setLargeImage(byte[] largeImage) {
    this.largeImage = largeImage;
  }
  public java.math.BigDecimal getVatValueREG01() {
    return vatValueREG01;
  }
  public void setVatValueREG01(java.math.BigDecimal vatValueREG01) {
    this.vatValueREG01 = vatValueREG01;
  }
  public String getColorDescriptionSYS10() {
    return colorDescriptionSYS10;
  }
  public void setColorDescriptionSYS10(String colorDescriptionSYS10) {
    this.colorDescriptionSYS10 = colorDescriptionSYS10;
  }
  public String getSizeDescriptionSYS10() {
    return sizeDescriptionSYS10;
  }
  public void setSizeDescriptionSYS10(String sizeDescriptionSYS10) {
    this.sizeDescriptionSYS10 = sizeDescriptionSYS10;
  }
  public java.math.BigDecimal getVatDeductibleREG01() {
    return vatDeductibleREG01;
  }
  public void setVatDeductibleREG01(java.math.BigDecimal vatDeductibleREG01) {
    this.vatDeductibleREG01 = vatDeductibleREG01;
  }
  public Boolean getSerialNumberRequiredITM01() {
    return serialNumberRequiredITM01;
  }
  public void setSerialNumberRequiredITM01(Boolean serialNumberRequiredITM01) {
    this.serialNumberRequiredITM01 = serialNumberRequiredITM01;
  }
  public java.math.BigDecimal getVersionITM01() {
    return versionITM01;
  }
  public void setVersionITM01(java.math.BigDecimal versionITM01) {
    this.versionITM01 = versionITM01;
  }
  public java.math.BigDecimal getRevisionITM01() {
    return revisionITM01;
  }
  public void setRevisionITM01(java.math.BigDecimal revisionITM01) {
    this.revisionITM01 = revisionITM01;
  }
  public String getManufactureCodePro01ITM01() {
    return manufactureCodePro01ITM01;
  }
  public void setManufactureCodePro01ITM01(String manufactureCodePro01ITM01) {
    this.manufactureCodePro01ITM01 = manufactureCodePro01ITM01;
  }
  public java.sql.Date getStartDateITM01() {
    return startDateITM01;
  }
  public void setStartDateITM01(java.sql.Date startDateITM01) {
    this.startDateITM01 = startDateITM01;
  }
  public String getManufactureDescriptionSYS10() {
    return manufactureDescriptionSYS10;
  }
  public void setManufactureDescriptionSYS10(String manufactureDescriptionSYS10) {
    this.manufactureDescriptionSYS10 = manufactureDescriptionSYS10;
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
  public Boolean getUseVariant4ITM01() {
    return useVariant4ITM01;
  }
  public Boolean getUseVariant5ITM01() {
    return useVariant5ITM01;
  }
  public void setUseVariant4ITM01(Boolean useVariant4ITM01) {
    this.useVariant4ITM01 = useVariant4ITM01;
  }
  public void setUseVariant5ITM01(Boolean useVariant5ITM01) {
    this.useVariant5ITM01 = useVariant5ITM01;
  }
  public void setUseVariant3ITM01(Boolean useVariant3ITM01) {
    this.useVariant3ITM01 = useVariant3ITM01;
  }
  public void setUseVariant2ITM01(Boolean useVariant2ITM01) {
    this.useVariant2ITM01 = useVariant2ITM01;
  }
  public void setUseVariant1ITM01(Boolean useVariant1ITM01) {
    this.useVariant1ITM01 = useVariant1ITM01;
  }

  /**
   * getCompanyCodeSys01
   *
   * @return String
   */
  public String getCompanyCodeSys01() {
    return getCompanyCodeSys01ITM01();
  }

  /**
   * getDecimalsREG02
   *
   * @return BigDecimal
   */
  public BigDecimal getDecimalsREG02() {
    return new BigDecimal(0); // not used
  }

  /**
   * getItemCodeItm01
   *
   * @return String
   */
  public String getItemCodeItm01() {
    return getItemCodeITM01();
  }
  public String getBarCodeITM01() {
    return barCodeITM01;
  }
  public void setBarCodeITM01(String barCodeITM01) {
    this.barCodeITM01 = barCodeITM01;
  }
  public java.math.BigDecimal getAvgPurchaseCost() {
    return avgPurchaseCost;
  }
  public void setAvgPurchaseCost(java.math.BigDecimal avgPurchaseCost) {
    this.avgPurchaseCost = avgPurchaseCost;
  }
  public java.math.BigDecimal getLastPurchaseCost() {
    return lastPurchaseCost;
  }
  public Date getLastPurchaseDate() {
    return lastPurchaseDate;
  }
  public void setLastPurchaseDate(Date lastPurchaseDate) {
    this.lastPurchaseDate = lastPurchaseDate;
  }
  public void setLastPurchaseCost(java.math.BigDecimal lastPurchaseCost) {
    this.lastPurchaseCost = lastPurchaseCost;
  }
  public String getLastPurchaseCostCurrencySymbol() {
    return lastPurchaseCostCurrencySymbol;
  }
  public java.math.BigDecimal getLastPurchaseCostDecimals() {
    return lastPurchaseCostDecimals;
  }
  public String getLastPurchaseCostDecimalSymbol() {
    return lastPurchaseCostDecimalSymbol;
  }
  public String getLastPurchaseCostThousandSymbol() {
    return lastPurchaseCostThousandSymbol;
  }
  public void setLastPurchaseCostThousandSymbol(String lastPurchaseCostThousandSymbol) {
    this.lastPurchaseCostThousandSymbol = lastPurchaseCostThousandSymbol;
  }
  public void setLastPurchaseCostDecimalSymbol(String lastPurchaseCostDecimalSymbol) {
    this.lastPurchaseCostDecimalSymbol = lastPurchaseCostDecimalSymbol;
  }
  public void setLastPurchaseCostDecimals(java.math.BigDecimal lastPurchaseCostDecimals) {
    this.lastPurchaseCostDecimals = lastPurchaseCostDecimals;
  }
  public void setLastPurchaseCostCurrencySymbol(String lastPurchaseCostCurrencySymbol) {
    this.lastPurchaseCostCurrencySymbol = lastPurchaseCostCurrencySymbol;
  }
  public String getBarcodeTypeITM01() {
    return barcodeTypeITM01;
  }
  public void setBarcodeTypeITM01(String barcodeTypeITM01) {
    this.barcodeTypeITM01 = barcodeTypeITM01;
  }
  public java.math.BigDecimal getMinStockITM23() {
    return minStockITM23;
  }
  public void setMinStockITM23(java.math.BigDecimal minStockITM23) {
    this.minStockITM23 = minStockITM23;
  }
  public java.math.BigDecimal getProgressiveHie01HIE02() {
    return progressiveHie01HIE02;
  }
  public void setProgressiveHie01HIE02(java.math.BigDecimal progressiveHie01HIE02) {
    this.progressiveHie01HIE02 = progressiveHie01HIE02;
  }
  public Boolean getNoWarehouseMovITM01() {
    return noWarehouseMovITM01;
  }
  public void setNoWarehouseMovITM01(Boolean noWarehouseMovITM01) {
    this.noWarehouseMovITM01 = noWarehouseMovITM01;
  }
  public String getSheetCodeItm25ITM01() {
    return sheetCodeItm25ITM01;
  }
  public void setSheetCodeItm25ITM01(String sheetCodeItm25ITM01) {
    this.sheetCodeItm25ITM01 = sheetCodeItm25ITM01;
  }


}
