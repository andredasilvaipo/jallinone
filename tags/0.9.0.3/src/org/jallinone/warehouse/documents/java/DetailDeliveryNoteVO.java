package org.jallinone.warehouse.documents.java;

import java.math.BigDecimal;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store delivery note header info for a detail form.</p>
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
public class DetailDeliveryNoteVO extends GridDeliveryNoteVO {

  private String addressDOC08;
  private String cityDOC08;
  private String provinceDOC08;
  private String countryDOC08;
  private String zipDOC08;
  private String destinationCodeReg18DOC08;
  private String noteDOC08;
  private String enabledDOC08;
  private String carrierCodeReg09DOC08;
  private String carrierDescriptionSYS10;
  private BigDecimal progressiveHie02WAR01;
  private java.math.BigDecimal docSequenceDOC08;
  private java.util.Date deliveryDateDOC08;
  private String transportMotiveCodeReg20DOC08;
  private String transportMotiveDescriptionSYS10;


  public DetailDeliveryNoteVO() {
  }


  public String getAddressDOC08() {
    return addressDOC08;
  }
  public void setAddressDOC08(String addressDOC08) {
    this.addressDOC08 = addressDOC08;
  }
  public String getCityDOC08() {
    return cityDOC08;
  }
  public void setCityDOC08(String cityDOC08) {
    this.cityDOC08 = cityDOC08;
  }
  public String getProvinceDOC08() {
    return provinceDOC08;
  }
  public void setProvinceDOC08(String provinceDOC08) {
    this.provinceDOC08 = provinceDOC08;
  }
  public String getCountryDOC08() {
    return countryDOC08;
  }
  public void setCountryDOC08(String countryDOC08) {
    this.countryDOC08 = countryDOC08;
  }
  public String getZipDOC08() {
    return zipDOC08;
  }
  public void setZipDOC08(String zipDOC08) {
    this.zipDOC08 = zipDOC08;
  }
  public String getDestinationCodeReg18DOC08() {
    return destinationCodeReg18DOC08;
  }
  public void setDestinationCodeReg18DOC08(String destinationCodeReg18DOC08) {
    this.destinationCodeReg18DOC08 = destinationCodeReg18DOC08;
  }
  public String getNoteDOC08() {
    return noteDOC08;
  }
  public void setNoteDOC08(String noteDOC08) {
    this.noteDOC08 = noteDOC08;
  }
  public String getEnabledDOC08() {
    return enabledDOC08;
  }
  public void setEnabledDOC08(String enabledDOC08) {
    this.enabledDOC08 = enabledDOC08;
  }
  public String getCarrierCodeReg09DOC08() {
    return carrierCodeReg09DOC08;
  }
  public void setCarrierCodeReg09DOC08(String carrierCodeReg09DOC08) {
    this.carrierCodeReg09DOC08 = carrierCodeReg09DOC08;
  }
  public String getCarrierDescriptionSYS10() {
    return carrierDescriptionSYS10;
  }
  public void setCarrierDescriptionSYS10(String carrierDescriptionSYS10) {
    this.carrierDescriptionSYS10 = carrierDescriptionSYS10;
  }
  public BigDecimal getProgressiveHie02WAR01() {
    return progressiveHie02WAR01;
  }
  public void setProgressiveHie02WAR01(BigDecimal progressiveHie02WAR01) {
    this.progressiveHie02WAR01 = progressiveHie02WAR01;
  }
  public java.math.BigDecimal getDocSequenceDOC08() {
    return docSequenceDOC08;
  }
  public void setDocSequenceDOC08(java.math.BigDecimal docSequenceDOC08) {
    this.docSequenceDOC08 = docSequenceDOC08;
  }
  public java.util.Date getDeliveryDateDOC08() {
    return deliveryDateDOC08;
  }
  public String getTransportMotiveCodeReg20DOC08() {
    return transportMotiveCodeReg20DOC08;
  }
  public String getTransportMotiveDescriptionSYS10() {
    return transportMotiveDescriptionSYS10;
  }
  public void setTransportMotiveDescriptionSYS10(String transportMotiveDescriptionSYS10) {
    this.transportMotiveDescriptionSYS10 = transportMotiveDescriptionSYS10;
  }
  public void setTransportMotiveCodeReg20DOC08(String transportMotiveCodeReg20DOC08) {
    this.transportMotiveCodeReg20DOC08 = transportMotiveCodeReg20DOC08;
  }
  public void setDeliveryDateDOC08(java.util.Date deliveryDateDOC08) {
    this.deliveryDateDOC08 = deliveryDateDOC08;
  }

}