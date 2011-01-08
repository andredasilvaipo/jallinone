package org.jallinone.scheduler.callouts.java;

import org.jallinone.system.customizations.java.BaseValueObject;
import org.jallinone.scheduler.activities.java.ScheduledActivityVO;
import org.jallinone.sales.documents.java.DetailSaleDocVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store call-out request info for the detail.</p>
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
public class DetailCallOutRequestVO extends BaseValueObject {

  private String companyCodeSys01SCH03;
  private java.math.BigDecimal requestYearSCH03;
  private java.math.BigDecimal progressiveSCH03;
  private String descriptionSCH03;
  private String callOutCodeSch10SCH03;
  private String callOutDescriptionSYS10;
  private String callOutStateSCH03;
  private String usernameSys03SCH03;
  private java.sql.Timestamp requestDateSCH03;
  private String noteSCH03;
  private String docTypeDoc01SCH03;
  private java.math.BigDecimal docYearDoc01SCH03;
  private java.math.BigDecimal progressiveReg04SCH03;
  private java.math.BigDecimal progressiveSch06SCH03;
  private String subjectTypeReg04SCH03;
  private java.math.BigDecimal progressiveHie02SCH10;
  private String prioritySCH03;

	private java.math.BigDecimal progressiveHie02ITM01;
	private String itemCodeItm01SCH03;
	private String descriptionSYS10;


  private ScheduledActivityVO scheduledActivityVO;
  private java.math.BigDecimal docNumberDoc01SCH03;


  public DetailCallOutRequestVO() {
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
  public String getCallOutCodeSch10SCH03() {
    return callOutCodeSch10SCH03;
  }
  public String getCallOutDescriptionSYS10() {
    return callOutDescriptionSYS10;
  }
  public String getCallOutStateSCH03() {
    return callOutStateSCH03;
  }
  public String getDescriptionSCH03() {
    return descriptionSCH03;
  }
  public String getPrioritySCH03() {
    return prioritySCH03;
  }
  public String getUsernameSys03SCH03() {
    return usernameSys03SCH03;
  }
  public java.sql.Timestamp getRequestDateSCH03() {
    return requestDateSCH03;
  }
  public void setCallOutCodeSch10SCH03(String callOutCodeSch10SCH03) {
    this.callOutCodeSch10SCH03 = callOutCodeSch10SCH03;
  }
  public void setCallOutDescriptionSYS10(String callOutDescriptionSYS10) {
    this.callOutDescriptionSYS10 = callOutDescriptionSYS10;
  }
  public void setCallOutStateSCH03(String callOutStateSCH03) {
    this.callOutStateSCH03 = callOutStateSCH03;
  }
  public void setDescriptionSCH03(String descriptionSCH03) {
    this.descriptionSCH03 = descriptionSCH03;
  }

  public void setPrioritySCH03(String prioritySCH03) {
    this.prioritySCH03 = prioritySCH03;
  }
  public void setRequestDateSCH03(java.sql.Timestamp requestDateSCH03) {
    this.requestDateSCH03 = requestDateSCH03;
  }
  public void setUsernameSys03SCH03(String usernameSys03SCH03) {
    this.usernameSys03SCH03 = usernameSys03SCH03;
  }
  public String getNoteSCH03() {
    return noteSCH03;
  }
  public void setNoteSCH03(String noteSCH03) {
    this.noteSCH03 = noteSCH03;
  }
  public String getDocTypeDoc01SCH03() {
    return docTypeDoc01SCH03;
  }
  public void setDocTypeDoc01SCH03(String docTypeDoc01SCH03) {
    this.docTypeDoc01SCH03 = docTypeDoc01SCH03;
  }
  public java.math.BigDecimal getDocNumberDoc01SCH03() {
    return docNumberDoc01SCH03;
  }
  public void setDocNumberDoc01SCH03(java.math.BigDecimal docNumberDoc01SCH03) {
    this.docNumberDoc01SCH03 = docNumberDoc01SCH03;
  }
  public java.math.BigDecimal getDocYearDoc01SCH03() {
    return docYearDoc01SCH03;
  }
  public void setDocYearDoc01SCH03(java.math.BigDecimal docYearDoc01SCH03) {
    this.docYearDoc01SCH03 = docYearDoc01SCH03;
  }
  public java.math.BigDecimal getProgressiveReg04SCH03() {
    return progressiveReg04SCH03;
  }
  public void setProgressiveReg04SCH03(java.math.BigDecimal progressiveReg04SCH03) {
    this.progressiveReg04SCH03 = progressiveReg04SCH03;
  }
  public java.math.BigDecimal getProgressiveSch06SCH03() {
    return progressiveSch06SCH03;
  }
  public void setProgressiveSch06SCH03(java.math.BigDecimal progressiveSch06SCH03) {
    this.progressiveSch06SCH03 = progressiveSch06SCH03;
  }
  public String getSubjectTypeReg04SCH03() {
    return subjectTypeReg04SCH03;
  }
  public void setSubjectTypeReg04SCH03(String subjectTypeReg04SCH03) {
    this.subjectTypeReg04SCH03 = subjectTypeReg04SCH03;
  }
  public java.math.BigDecimal getProgressiveHie02SCH10() {
    return progressiveHie02SCH10;
  }
  public void setProgressiveHie02SCH10(java.math.BigDecimal progressiveHie02SCH10) {
    this.progressiveHie02SCH10 = progressiveHie02SCH10;
  }
  public ScheduledActivityVO getScheduledActivityVO() {
    return scheduledActivityVO;
  }
  public void setScheduledActivityVO(ScheduledActivityVO scheduledActivityVO) {
    this.scheduledActivityVO = scheduledActivityVO;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public String getItemCodeItm01SCH03() {
    return itemCodeItm01SCH03;
  }
  public java.math.BigDecimal getProgressiveHie02ITM01() {
    return progressiveHie02ITM01;
  }
  public void setProgressiveHie02ITM01(java.math.BigDecimal progressiveHie02ITM01) {
    this.progressiveHie02ITM01 = progressiveHie02ITM01;
  }
  public void setItemCodeItm01SCH03(String itemCodeItm01SCH03) {
    this.itemCodeItm01SCH03 = itemCodeItm01SCH03;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }



}
