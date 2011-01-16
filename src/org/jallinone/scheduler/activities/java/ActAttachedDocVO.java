package org.jallinone.scheduler.activities.java;

import org.jallinone.system.customizations.java.BaseValueObject;
import org.openswing.swing.message.receive.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store an attached document, linked to a scheduled activity (table SCH08).</p>
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
public class ActAttachedDocVO extends ValueObjectImpl {

  private java.math.BigDecimal progressiveSch06SCH08;
  private java.math.BigDecimal progressiveDoc14SCH08;
  private String companyCodeSys01SCH08;
  private String descriptionDOC14;
  private java.math.BigDecimal progressiveHie02HIE01;
  private java.math.BigDecimal progressiveHie01SCH08;


  public ActAttachedDocVO() {
  }


  public java.math.BigDecimal getProgressiveSch06SCH08() {
    return progressiveSch06SCH08;
  }
  public void setProgressiveSch06SCH08(java.math.BigDecimal progressiveSch06SCH08) {
    this.progressiveSch06SCH08 = progressiveSch06SCH08;
  }
  public java.math.BigDecimal getProgressiveDoc14SCH08() {
    return progressiveDoc14SCH08;
  }
  public void setProgressiveDoc14SCH08(java.math.BigDecimal progressiveDoc14SCH08) {
    this.progressiveDoc14SCH08 = progressiveDoc14SCH08;
  }
  public String getCompanyCodeSys01SCH08() {
    return companyCodeSys01SCH08;
  }
  public void setCompanyCodeSys01SCH08(String companyCodeSys01SCH08) {
    this.companyCodeSys01SCH08 = companyCodeSys01SCH08;
  }
  public String getDescriptionDOC14() {
    return descriptionDOC14;
  }
  public void setDescriptionDOC14(String descriptionDOC14) {
    this.descriptionDOC14 = descriptionDOC14;
  }
  public java.math.BigDecimal getProgressiveHie02HIE01() {
    return progressiveHie02HIE01;
  }
  public void setProgressiveHie02HIE01(java.math.BigDecimal progressiveHie02HIE01) {
    this.progressiveHie02HIE01 = progressiveHie02HIE01;
  }
  public java.math.BigDecimal getProgressiveHie01SCH08() {
    return progressiveHie01SCH08;
  }
  public void setProgressiveHie01SCH08(java.math.BigDecimal progressiveHie01SCH08) {
    this.progressiveHie01SCH08 = progressiveHie01SCH08;
  }


}
