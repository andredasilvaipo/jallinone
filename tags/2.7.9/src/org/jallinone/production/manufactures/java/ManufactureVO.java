package org.jallinone.production.manufactures.java;

import org.jallinone.system.customizations.java.BaseValueObject;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store manufacture info.</p>
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
public class ManufactureVO extends BaseValueObject {

  private String manufactureCodePRO01;
  private String enabledPRO01;
  private String descriptionSYS10;
  private String companyCodeSys01PRO01;
  private java.math.BigDecimal progressiveSys10PRO01;


  public ManufactureVO() {
  }


  public String getManufactureCodePRO01() {
    return manufactureCodePRO01;
  }
  public void setManufactureCodePRO01(String manufactureCodePRO01) {
    this.manufactureCodePRO01 = manufactureCodePRO01;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public String getEnabledPRO01() {
    return enabledPRO01;
  }
  public void setEnabledPRO01(String enabledPRO01) {
    this.enabledPRO01 = enabledPRO01;
  }
  public String getCompanyCodeSys01PRO01() {
    return companyCodeSys01PRO01;
  }
  public void setCompanyCodeSys01PRO01(String companyCodeSys01PRO01) {
    this.companyCodeSys01PRO01 = companyCodeSys01PRO01;
  }

  public java.math.BigDecimal getProgressiveSys10PRO01() {
    return progressiveSys10PRO01;
  }
  public void setProgressiveSys10PRO01(java.math.BigDecimal progressiveSys10PRO01) {
    this.progressiveSys10PRO01 = progressiveSys10PRO01;
  }

}
