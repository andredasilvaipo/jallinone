package org.jallinone.items.java;

import org.jallinone.system.customizations.java.BaseValueObject;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store item type info.</p>
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
public class ItemTypeVO extends BaseValueObject {



  private String companyCodeSys01ITM02;
  private java.math.BigDecimal progressiveHie02ITM02;
  private String enabledITM02;
  private String descriptionSYS10;
  private java.math.BigDecimal progressiveHie01HIE02;


  public ItemTypeVO() {
  }


  public String getCompanyCodeSys01ITM02() {
    return companyCodeSys01ITM02;
  }
  public void setCompanyCodeSys01ITM02(String companyCodeSys01ITM02) {
    this.companyCodeSys01ITM02 = companyCodeSys01ITM02;
  }
  public java.math.BigDecimal getProgressiveHie02ITM02() {
    return progressiveHie02ITM02;
  }
  public void setProgressiveHie02ITM02(java.math.BigDecimal progressiveHie02ITM02) {
    this.progressiveHie02ITM02 = progressiveHie02ITM02;
  }
  public String getEnabledITM02() {
    return enabledITM02;
  }
  public void setEnabledITM02(String enabledITM02) {
    this.enabledITM02 = enabledITM02;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public java.math.BigDecimal getProgressiveHie01HIE02() {
    return progressiveHie01HIE02;
  }
  public void setProgressiveHie01HIE02(java.math.BigDecimal progressiveHie01HIE02) {
    this.progressiveHie01HIE02 = progressiveHie01HIE02;
  }



}