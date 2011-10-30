package org.jallinone.scheduler.activities.java;

import org.jallinone.system.customizations.java.BaseValueObject;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a scheduled activity for the grid (table SCH06).</p>
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
public class GridScheduledActivityVO extends BaseValueObject {

  private String companyCodeSys01SCH06;
  private String descriptionSCH06;
  private String activityTypeSCH06;
  private String prioritySCH06;
  private String activityStateSCH06;
  private String activityPlaceSCH06;
  private java.math.BigDecimal progressiveSCH06;
  private java.math.BigDecimal estimatedDurationSCH06;
  private java.math.BigDecimal durationSCH06;
  private java.math.BigDecimal completionPercSCH06;
  private java.sql.Timestamp startDateSCH06;
  private java.sql.Timestamp estimatedEndDateSCH06;
  private java.sql.Timestamp endDateSCH06;
  private java.sql.Timestamp expirationDateSCH06;

  private String managerName_1SCH06;
  private String managerName_2SCH06;
  private String subjectName_1SCH06;
  private String subjectName_2SCH06;

  private java.math.BigDecimal progressiveReg04ManagerSCH06;
  private java.math.BigDecimal progressiveReg04SubjectSCH06;
  private String subjectTypeReg04SubjectSCH06;


  public GridScheduledActivityVO() {
  }


  public String getCompanyCodeSys01SCH06() {
    return companyCodeSys01SCH06;
  }
  public void setCompanyCodeSys01SCH06(String companyCodeSys01SCH06) {
    this.companyCodeSys01SCH06 = companyCodeSys01SCH06;
  }
  public String getDescriptionSCH06() {
    return descriptionSCH06;
  }
  public void setDescriptionSCH06(String descriptionSCH06) {
    this.descriptionSCH06 = descriptionSCH06;
  }
  public String getActivityTypeSCH06() {
    return activityTypeSCH06;
  }
  public void setActivityTypeSCH06(String activityTypeSCH06) {
    this.activityTypeSCH06 = activityTypeSCH06;
  }
  public String getPrioritySCH06() {
    return prioritySCH06;
  }
  public void setPrioritySCH06(String prioritySCH06) {
    this.prioritySCH06 = prioritySCH06;
  }
  public String getActivityStateSCH06() {
    return activityStateSCH06;
  }
  public void setActivityStateSCH06(String activityStateSCH06) {
    this.activityStateSCH06 = activityStateSCH06;
  }
  public String getActivityPlaceSCH06() {
    return activityPlaceSCH06;
  }
  public void setActivityPlaceSCH06(String activityPlaceSCH06) {
    this.activityPlaceSCH06 = activityPlaceSCH06;
  }
  public java.math.BigDecimal getProgressiveSCH06() {
    return progressiveSCH06;
  }
  public void setProgressiveSCH06(java.math.BigDecimal progressiveSCH06) {
    this.progressiveSCH06 = progressiveSCH06;
  }
  public java.math.BigDecimal getEstimatedDurationSCH06() {
    return estimatedDurationSCH06;
  }
  public void setEstimatedDurationSCH06(java.math.BigDecimal estimatedDurationSCH06) {
    this.estimatedDurationSCH06 = estimatedDurationSCH06;
  }
  public java.math.BigDecimal getDurationSCH06() {
    return durationSCH06;
  }
  public void setDurationSCH06(java.math.BigDecimal durationSCH06) {
    this.durationSCH06 = durationSCH06;
  }
  public java.math.BigDecimal getCompletionPercSCH06() {
    return completionPercSCH06;
  }
  public void setCompletionPercSCH06(java.math.BigDecimal completionPercSCH06) {
    this.completionPercSCH06 = completionPercSCH06;
  }
  public java.sql.Timestamp getStartDateSCH06() {
    return startDateSCH06;
  }
  public void setStartDateSCH06(java.sql.Timestamp startDateSCH06) {
    this.startDateSCH06 = startDateSCH06;
  }
  public java.sql.Timestamp getEstimatedEndDateSCH06() {
    return estimatedEndDateSCH06;
  }
  public void setEstimatedEndDateSCH06(java.sql.Timestamp estimatedEndDateSCH06) {
    this.estimatedEndDateSCH06 = estimatedEndDateSCH06;
  }
  public java.sql.Timestamp getEndDateSCH06() {
    return endDateSCH06;
  }
  public void setEndDateSCH06(java.sql.Timestamp endDateSCH06) {
    this.endDateSCH06 = endDateSCH06;
  }
  public java.sql.Timestamp getExpirationDateSCH06() {
    return expirationDateSCH06;
  }
  public void setExpirationDateSCH06(java.sql.Timestamp expirationDateSCH06) {
    this.expirationDateSCH06 = expirationDateSCH06;
  }
  public String getManagerName_2SCH06() {
    return managerName_2SCH06;
  }
  public String getManagerName_1SCH06() {
    return managerName_1SCH06;
  }
  public String getSubjectName_1SCH06() {
    return subjectName_1SCH06;
  }
  public String getSubjectName_2SCH06() {
    return subjectName_2SCH06;
  }
  public void setSubjectName_2SCH06(String subjectName_2SCH06) {
    this.subjectName_2SCH06 = subjectName_2SCH06;
  }
  public void setSubjectName_1SCH06(String subjectName_1SCH06) {
    this.subjectName_1SCH06 = subjectName_1SCH06;
  }
  public void setManagerName_2SCH06(String managerName_2SCH06) {
    this.managerName_2SCH06 = managerName_2SCH06;
  }
  public void setManagerName_1SCH06(String managerName_1SCH06) {
    this.managerName_1SCH06 = managerName_1SCH06;
  }

  public java.math.BigDecimal getProgressiveReg04ManagerSCH06() {
    return progressiveReg04ManagerSCH06;
  }
  public void setProgressiveReg04ManagerSCH06(java.math.BigDecimal progressiveReg04ManagerSCH06) {
    this.progressiveReg04ManagerSCH06 = progressiveReg04ManagerSCH06;
  }
  public java.math.BigDecimal getProgressiveReg04SubjectSCH06() {
    return progressiveReg04SubjectSCH06;
  }
  public void setProgressiveReg04SubjectSCH06(java.math.BigDecimal progressiveReg04SubjectSCH06) {
    this.progressiveReg04SubjectSCH06 = progressiveReg04SubjectSCH06;
  }
  public String getSubjectTypeReg04SubjectSCH06() {
    return subjectTypeReg04SubjectSCH06;
  }
  public void setSubjectTypeReg04SubjectSCH06(String subjectTypeReg04SubjectSCH06) {
    this.subjectTypeReg04SubjectSCH06 = subjectTypeReg04SubjectSCH06;
  }


}
