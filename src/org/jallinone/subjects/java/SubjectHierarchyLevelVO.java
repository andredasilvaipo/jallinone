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
public class SubjectHierarchyLevelVO extends ValueObjectImpl {


  private String companyCodeSys01REG16;
  private String descriptionSYS10;
  private java.math.BigDecimal progressiveHie02REG16;
  private java.math.BigDecimal progressiveHie01REG16;
  private java.math.BigDecimal progressiveReg04REG16;
  private String levelDescriptionSYS10;


  public SubjectHierarchyLevelVO() {
  }


  public String getCompanyCodeSys01REG16() {
    return companyCodeSys01REG16;
  }
  public void setCompanyCodeSys01REG16(String companyCodeSys01REG16) {
    this.companyCodeSys01REG16 = companyCodeSys01REG16;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public java.math.BigDecimal getProgressiveHie02REG16() {
    return progressiveHie02REG16;
  }
  public void setProgressiveHie02REG16(java.math.BigDecimal progressiveHie02REG16) {
    this.progressiveHie02REG16 = progressiveHie02REG16;
  }
  public java.math.BigDecimal getProgressiveHie01REG16() {
    return progressiveHie01REG16;
  }
  public void setProgressiveHie01REG16(java.math.BigDecimal progressiveHie01REG16) {
    this.progressiveHie01REG16 = progressiveHie01REG16;
  }
  public java.math.BigDecimal getProgressiveReg04REG16() {
    return progressiveReg04REG16;
  }
  public void setProgressiveReg04REG16(java.math.BigDecimal progressiveReg04REG16) {
    this.progressiveReg04REG16 = progressiveReg04REG16;
  }
  public String getLevelDescriptionSYS10() {
    return levelDescriptionSYS10;
  }
  public void setLevelDescriptionSYS10(String levelDescriptionSYS10) {
    this.levelDescriptionSYS10 = levelDescriptionSYS10;
  }

}
