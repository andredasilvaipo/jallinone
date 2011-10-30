package org.jallinone.items.java;

import org.openswing.swing.message.receive.java.ValueObjectImpl;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store an image related to a combination of variants..</p>
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
public class ItemVariantImageVO extends ValueObjectImpl {

	private String companyCodeSys01ITM33;
	private String itemCodeItm01ITM33;
	private String variantTypeItm06ITM33;
	private String variantCodeItm11ITM33;
	private String variantTypeItm07ITM33;
	private String variantCodeItm12ITM33;
	private String variantTypeItm08ITM33;
	private String variantCodeItm13ITM33;
	private String variantTypeItm09ITM33;
	private String variantCodeItm14ITM33;
	private String variantTypeItm10ITM33;
	private String variantCodeItm15ITM33;

	private String variantsDescription;

	private String smallImageITM33;
	private String largeImageITM33;
	private byte[] smallImage;
	private byte[] largeImage;


	public ItemVariantImageVO() {
	}


  public String getCompanyCodeSys01ITM33() {
    return companyCodeSys01ITM33;
  }
  public String getItemCodeItm01ITM33() {
    return itemCodeItm01ITM33;
  }
  public byte[] getLargeImage() {
    return largeImage;
  }
  public String getLargeImageITM33() {
    return largeImageITM33;
  }
  public byte[] getSmallImage() {
    return smallImage;
  }
  public String getSmallImageITM33() {
    return smallImageITM33;
  }
  public String getVariantCodeItm11ITM33() {
    return variantCodeItm11ITM33;
  }
  public String getVariantCodeItm12ITM33() {
    return variantCodeItm12ITM33;
  }
  public String getVariantCodeItm13ITM33() {
    return variantCodeItm13ITM33;
  }
  public String getVariantCodeItm14ITM33() {
    return variantCodeItm14ITM33;
  }
  public String getVariantCodeItm15ITM33() {
    return variantCodeItm15ITM33;
  }
  public String getVariantTypeItm06ITM33() {
    return variantTypeItm06ITM33;
  }
  public String getVariantTypeItm07ITM33() {
    return variantTypeItm07ITM33;
  }
  public String getVariantTypeItm08ITM33() {
    return variantTypeItm08ITM33;
  }
  public String getVariantTypeItm09ITM33() {
    return variantTypeItm09ITM33;
  }
  public String getVariantTypeItm10ITM33() {
    return variantTypeItm10ITM33;
  }
  public void setVariantTypeItm10ITM33(String variantTypeItm10ITM33) {
    this.variantTypeItm10ITM33 = variantTypeItm10ITM33;
  }
  public void setVariantTypeItm09ITM33(String variantTypeItm09ITM33) {
    this.variantTypeItm09ITM33 = variantTypeItm09ITM33;
  }
  public void setVariantTypeItm08ITM33(String variantTypeItm08ITM33) {
    this.variantTypeItm08ITM33 = variantTypeItm08ITM33;
  }
  public void setVariantTypeItm07ITM33(String variantTypeItm07ITM33) {
    this.variantTypeItm07ITM33 = variantTypeItm07ITM33;
  }
  public void setVariantTypeItm06ITM33(String variantTypeItm06ITM33) {
    this.variantTypeItm06ITM33 = variantTypeItm06ITM33;
  }
  public void setVariantCodeItm14ITM33(String variantCodeItm14ITM33) {
    this.variantCodeItm14ITM33 = variantCodeItm14ITM33;
  }
  public void setVariantCodeItm13ITM33(String variantCodeItm13ITM33) {
    this.variantCodeItm13ITM33 = variantCodeItm13ITM33;
  }
  public void setVariantCodeItm12ITM33(String variantCodeItm12ITM33) {
    this.variantCodeItm12ITM33 = variantCodeItm12ITM33;
  }
  public void setVariantCodeItm11ITM33(String variantCodeItm11ITM33) {
    this.variantCodeItm11ITM33 = variantCodeItm11ITM33;
  }
  public void setSmallImageITM33(String smallImageITM33) {
    this.smallImageITM33 = smallImageITM33;
  }
  public void setSmallImage(byte[] smallImage) {
    this.smallImage = smallImage;
  }
  public void setLargeImageITM33(String largeImageITM33) {
    this.largeImageITM33 = largeImageITM33;
  }
  public void setLargeImage(byte[] largeImage) {
    this.largeImage = largeImage;
  }
  public void setItemCodeItm01ITM33(String itemCodeItm01ITM33) {
    this.itemCodeItm01ITM33 = itemCodeItm01ITM33;
  }
  public void setCompanyCodeSys01ITM33(String companyCodeSys01ITM33) {
    this.companyCodeSys01ITM33 = companyCodeSys01ITM33;
  }
  public void setVariantCodeItm15ITM33(String variantCodeItm15ITM33) {
    this.variantCodeItm15ITM33 = variantCodeItm15ITM33;
  }
  public String getVariantsDescription() {
    return variantsDescription;
  }
  public void setVariantsDescription(String variantsDescription) {
    this.variantsDescription = variantsDescription;
  }


}

