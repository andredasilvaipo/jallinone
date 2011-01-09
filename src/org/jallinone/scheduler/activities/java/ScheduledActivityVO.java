package org.jallinone.scheduler.activities.java;

import org.jallinone.system.customizations.java.BaseValueObject;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a scheduled activity (table SCH06).</p>
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
public class ScheduledActivityVO extends GridScheduledActivityVO {

  private String descriptionWkf01SCH06;
  private java.math.BigDecimal progressiveWkf01SCH06;
  private java.math.BigDecimal progressiveWkf08SCH06;
  private java.math.BigDecimal progressiveWkf02SCH06;
  private String noteSCH06;
  private String emailAddressSCH06;
  private String faxNumberSCH06;
  private String phoneNumberSCH06;


  public ScheduledActivityVO() {
  }


  public String getDescriptionWkf01SCH06() {
    return descriptionWkf01SCH06;
  }
  public void setDescriptionWkf01SCH06(String descriptionWkf01SCH06) {
    this.descriptionWkf01SCH06 = descriptionWkf01SCH06;
  }
  public java.math.BigDecimal getProgressiveWkf01SCH06() {
    return progressiveWkf01SCH06;
  }
  public void setProgressiveWkf01SCH06(java.math.BigDecimal progressiveWkf01SCH06) {
    this.progressiveWkf01SCH06 = progressiveWkf01SCH06;
  }
  public java.math.BigDecimal getProgressiveWkf08SCH06() {
    return progressiveWkf08SCH06;
  }
  public void setProgressiveWkf08SCH06(java.math.BigDecimal progressiveWkf08SCH06) {
    this.progressiveWkf08SCH06 = progressiveWkf08SCH06;
  }
  public java.math.BigDecimal getProgressiveWkf02SCH06() {
    return progressiveWkf02SCH06;
  }
  public void setProgressiveWkf02SCH06(java.math.BigDecimal progressiveWkf02SCH06) {
    this.progressiveWkf02SCH06 = progressiveWkf02SCH06;
  }
  public String getNoteSCH06() {
    return noteSCH06;
  }
  public void setNoteSCH06(String noteSCH06) {
    this.noteSCH06 = noteSCH06;
  }
  public String getPhoneNumberSCH06() {
    return phoneNumberSCH06;
  }
  public String getFaxNumberSCH06() {
    return faxNumberSCH06;
  }
  public String getEmailAddressSCH06() {
    return emailAddressSCH06;
  }
  public void setEmailAddressSCH06(String emailAddressSCH06) {
    this.emailAddressSCH06 = emailAddressSCH06;
  }
  public void setFaxNumberSCH06(String faxNumberSCH06) {
    this.faxNumberSCH06 = faxNumberSCH06;
  }
  public void setPhoneNumberSCH06(String phoneNumberSCH06) {
    this.phoneNumberSCH06 = phoneNumberSCH06;
  }


}
