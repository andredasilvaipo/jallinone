package org.jallinone.scheduler.callouts.java;

import org.jallinone.system.customizations.java.BaseValueObject;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store call-out type info.</p>
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
public class CallOutTypeVO extends BaseValueObject {



  private String companyCodeSys01SCH11;
  private java.math.BigDecimal progressiveHie02SCH11;
  private String enabledSCH11;
  private String descriptionSYS10;
  private java.math.BigDecimal progressiveHie01HIE02;


  public CallOutTypeVO() {
  }


  public String getCompanyCodeSys01SCH11() {
    return companyCodeSys01SCH11;
  }
  public void setCompanyCodeSys01SCH11(String companyCodeSys01SCH11) {
    this.companyCodeSys01SCH11 = companyCodeSys01SCH11;
  }
  public java.math.BigDecimal getProgressiveHie02SCH11() {
    return progressiveHie02SCH11;
  }
  public void setProgressiveHie02SCH11(java.math.BigDecimal progressiveHie02SCH11) {
    this.progressiveHie02SCH11 = progressiveHie02SCH11;
  }
  public String getEnabledSCH11() {
    return enabledSCH11;
  }
  public void setEnabledSCH11(String enabledSCH11) {
    this.enabledSCH11 = enabledSCH11;
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