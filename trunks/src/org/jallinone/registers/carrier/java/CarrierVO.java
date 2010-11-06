package org.jallinone.registers.carrier.java;

import org.jallinone.system.customizations.java.BaseValueObject;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store carrier info.</p>
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
public class CarrierVO extends BaseValueObject {
  private String carrierCodeREG09;
  private java.math.BigDecimal progressiveSys10REG09;
  private String enabledREG09;
  private String descriptionSYS10;


  public CarrierVO() {
  }


  public String getCarrierCodeREG09() {
    return carrierCodeREG09;
  }
  public void setCarrierCodeREG09(String carrierCodeREG09) {
    this.carrierCodeREG09 = carrierCodeREG09;
  }
  public java.math.BigDecimal getProgressiveSys10REG09() {
    return progressiveSys10REG09;
  }
  public void setProgressiveSys10REG09(java.math.BigDecimal progressiveSys10REG09) {
    this.progressiveSys10REG09 = progressiveSys10REG09;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public String getEnabledREG09() {
    return enabledREG09;
  }
  public void setEnabledREG09(String enabledREG09) {
    this.enabledREG09 = enabledREG09;
  }

}