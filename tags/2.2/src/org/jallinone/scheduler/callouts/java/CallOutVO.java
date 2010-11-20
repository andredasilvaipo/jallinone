package org.jallinone.scheduler.callouts.java;

import org.jallinone.system.customizations.java.BaseValueObject;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store call-out info for the grid/detail.</p>
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
public class CallOutVO extends BaseValueObject {

  private String companyCodeSys01SCH10;
  private String callOutCodeSCH10;
  private String descriptionSYS10;
  private java.math.BigDecimal progressiveHie02SCH10;
  private java.math.BigDecimal progressiveHie01SCH10;
  private java.math.BigDecimal progressiveSys10SCH10;
  private String levelDescriptionSYS10;
  private String enabledSCH10;


  public CallOutVO() {
  }


  public String getCompanyCodeSys01SCH10() {
    return companyCodeSys01SCH10;
  }
  public void setCompanyCodeSys01SCH10(String companyCodeSys01SCH10) {
    this.companyCodeSys01SCH10 = companyCodeSys01SCH10;
  }
  public String getCallOutCodeSCH10() {
    return callOutCodeSCH10;
  }
  public void setCallOutCodeSCH10(String callOutCodeSCH10) {
    this.callOutCodeSCH10 = callOutCodeSCH10;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public java.math.BigDecimal getProgressiveHie02SCH10() {
    return progressiveHie02SCH10;
  }
  public void setProgressiveHie02SCH10(java.math.BigDecimal progressiveHie02SCH10) {
    this.progressiveHie02SCH10 = progressiveHie02SCH10;
  }
  public java.math.BigDecimal getProgressiveHie01SCH10() {
    return progressiveHie01SCH10;
  }
  public void setProgressiveHie01SCH10(java.math.BigDecimal progressiveHie01SCH10) {
    this.progressiveHie01SCH10 = progressiveHie01SCH10;
  }
  public java.math.BigDecimal getProgressiveSys10SCH10() {
    return progressiveSys10SCH10;
  }
  public void setProgressiveSys10SCH10(java.math.BigDecimal progressiveSys10SCH10) {
    this.progressiveSys10SCH10 = progressiveSys10SCH10;
  }
  public String getLevelDescriptionSYS10() {
    return levelDescriptionSYS10;
  }
  public void setLevelDescriptionSYS10(String levelDescriptionSYS10) {
    this.levelDescriptionSYS10 = levelDescriptionSYS10;
  }
  public String getEnabledSCH10() {
    return enabledSCH10;
  }
  public void setEnabledSCH10(String enabledSCH10) {
    this.enabledSCH10 = enabledSCH10;
  }

}