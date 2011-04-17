package org.jallinone.scheduler.callouts.java;

import org.openswing.swing.message.receive.java.*;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store call-out request info for the grid.</p>
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
public class GridCallOutRequestVO extends ValueObjectImpl {

  private String companyCodeSys01SCH03;
  private java.math.BigDecimal requestYearSCH03;
  private java.math.BigDecimal progressiveSCH03;
  private String descriptionSCH03;
  private String callOutCodeSch10SCH03;
  private String callOutDescriptionSYS10;
  private String callOutStateSCH03;
  private String usernameSys03SCH03;
  private String name_1REG04;
  private String name_2REG04;
  private java.sql.Timestamp requestDateSCH03;
  private String prioritySCH03;


  public GridCallOutRequestVO() {
  }


  public String getCompanyCodeSys01SCH03() {
    return companyCodeSys01SCH03;
  }
  public java.math.BigDecimal getProgressiveSCH03() {
    return progressiveSCH03;
  }
  public java.math.BigDecimal getRequestYearSCH03() {
    return requestYearSCH03;
  }
  public void setRequestYearSCH03(java.math.BigDecimal requestYearSCH03) {
    this.requestYearSCH03 = requestYearSCH03;
  }
  public void setProgressiveSCH03(java.math.BigDecimal progressiveSCH03) {
    this.progressiveSCH03 = progressiveSCH03;
  }
  public void setCompanyCodeSys01SCH03(String companyCodeSys01SCH03) {
    this.companyCodeSys01SCH03 = companyCodeSys01SCH03;
  }
  public String getDescriptionSCH03() {
    return descriptionSCH03;
  }
  public void setDescriptionSCH03(String descriptionSCH03) {
    this.descriptionSCH03 = descriptionSCH03;
  }
  public String getCallOutCodeSch10SCH03() {
    return callOutCodeSch10SCH03;
  }
  public void setCallOutCodeSch10SCH03(String callOutCodeSch10SCH03) {
    this.callOutCodeSch10SCH03 = callOutCodeSch10SCH03;
  }
  public String getCallOutDescriptionSYS10() {
    return callOutDescriptionSYS10;
  }
  public void setCallOutDescriptionSYS10(String callOutDescriptionSYS10) {
    this.callOutDescriptionSYS10 = callOutDescriptionSYS10;
  }
  public String getCallOutStateSCH03() {
    return callOutStateSCH03;
  }
  public void setCallOutStateSCH03(String callOutStateSCH03) {
    this.callOutStateSCH03 = callOutStateSCH03;
  }
  public String getUsernameSys03SCH03() {
    return usernameSys03SCH03;
  }
  public void setUsernameSys03SCH03(String usernameSys03SCH03) {
    this.usernameSys03SCH03 = usernameSys03SCH03;
  }
  public String getName_1REG04() {
    return name_1REG04;
  }
  public void setName_1REG04(String name_1REG04) {
    this.name_1REG04 = name_1REG04;
  }
  public String getName_2REG04() {
    return name_2REG04;
  }
  public void setName_2REG04(String name_2REG04) {
    this.name_2REG04 = name_2REG04;
  }
  public String getPrioritySCH03() {
    return prioritySCH03;
  }
  public void setPrioritySCH03(String prioritySCH03) {
    this.prioritySCH03 = prioritySCH03;
  }
  public java.sql.Timestamp getRequestDateSCH03() {
    return requestDateSCH03;
  }
  public void setRequestDateSCH03(java.sql.Timestamp requestDateSCH03) {
    this.requestDateSCH03 = requestDateSCH03;
  }

}
