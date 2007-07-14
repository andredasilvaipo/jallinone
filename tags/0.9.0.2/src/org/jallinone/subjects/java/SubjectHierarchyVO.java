package org.jallinone.subjects.java;

import org.openswing.swing.message.receive.java.*;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a subject hierarchy.</p>
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
public class SubjectHierarchyVO extends ValueObjectImpl {


  private String companyCodeSys01REG08;
  private String subjectTypeREG08;
  private String descriptionSYS10;
  private java.math.BigDecimal progressiveSys10REG08;
  private java.math.BigDecimal progressiveHie02REG08;


  public SubjectHierarchyVO() {
  }


  public String getCompanyCodeSys01REG08() {
    return companyCodeSys01REG08;
  }
  public void setCompanyCodeSys01REG08(String companyCodeSys01REG08) {
    this.companyCodeSys01REG08 = companyCodeSys01REG08;
  }
  public String getSubjectTypeREG08() {
    return subjectTypeREG08;
  }
  public void setSubjectTypeREG08(String subjectTypeREG08) {
    this.subjectTypeREG08 = subjectTypeREG08;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public java.math.BigDecimal getProgressiveSys10REG08() {
    return progressiveSys10REG08;
  }
  public void setProgressiveSys10REG08(java.math.BigDecimal progressiveSys10REG08) {
    this.progressiveSys10REG08 = progressiveSys10REG08;
  }
  public java.math.BigDecimal getProgressiveHie02REG08() {
    return progressiveHie02REG08;
  }
  public void setProgressiveHie02REG08(java.math.BigDecimal progressiveHie02REG08) {
    this.progressiveHie02REG08 = progressiveHie02REG08;
  }

}
