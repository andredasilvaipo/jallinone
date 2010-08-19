package org.jallinone.scheduler.activities.java;

import org.jallinone.system.customizations.java.BaseValueObject;
import org.openswing.swing.message.receive.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a scheduled machineries (table SCH09).</p>
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
public class ScheduledMachineriesVO extends ValueObjectImpl {

  private java.math.BigDecimal progressiveSch06SCH09;
  private String noteSCH09;
  private String companyCodeSys01SCH09;
  private java.sql.Timestamp startDateSCH09;
  private java.sql.Timestamp endDateSCH09;
  private java.math.BigDecimal durationSCH09;
  private String machineryCodePro03SCH09;
  private String descriptionSYS10; // machinery description...


  public ScheduledMachineriesVO() {
  }


  public java.sql.Timestamp getStartDateSCH09() {
    return startDateSCH09;
  }
  public void setStartDateSCH09(java.sql.Timestamp startDateSCH09) {
    this.startDateSCH09 = startDateSCH09;
  }
  public java.math.BigDecimal getProgressiveSch06SCH09() {
    return progressiveSch06SCH09;
  }
  public void setProgressiveSch06SCH09(java.math.BigDecimal progressiveSch06SCH09) {
    this.progressiveSch06SCH09 = progressiveSch06SCH09;
  }
  public String getMachineryCodePro03SCH09() {
    return machineryCodePro03SCH09;
  }
  public void setMachineryCodePro03SCH09(String machineryCodePro03SCH09) {
    this.machineryCodePro03SCH09 = machineryCodePro03SCH09;
  }
  public String getNoteSCH09() {
    return noteSCH09;
  }
  public void setNoteSCH09(String noteSCH09) {
    this.noteSCH09 = noteSCH09;
  }
  public java.math.BigDecimal getDurationSCH09() {
    return durationSCH09;
  }
  public java.sql.Timestamp getEndDateSCH09() {
    return endDateSCH09;
  }
  public void setEndDateSCH09(java.sql.Timestamp endDateSCH09) {
    this.endDateSCH09 = endDateSCH09;
  }
  public void setDurationSCH09(java.math.BigDecimal durationSCH09) {
    this.durationSCH09 = durationSCH09;
  }
  public String getCompanyCodeSys01SCH09() {
    return companyCodeSys01SCH09;
  }
  public void setCompanyCodeSys01SCH09(String companyCodeSys01SCH09) {
    this.companyCodeSys01SCH09 = companyCodeSys01SCH09;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }


}
