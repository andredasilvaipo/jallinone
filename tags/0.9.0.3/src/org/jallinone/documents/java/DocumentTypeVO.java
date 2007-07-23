package org.jallinone.documents.java;

import org.jallinone.system.customizations.java.BaseValueObject;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a document type.</p>
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
public class DocumentTypeVO extends BaseValueObject {



  private String companyCodeSys01DOC16;
  private java.math.BigDecimal progressiveHie02DOC16;
  private String enabledDOC16;
  private String descriptionSYS10;
  private java.math.BigDecimal progressiveSys10DOC16;
  private java.math.BigDecimal progressiveHie01HIE02;


  public DocumentTypeVO() {
  }


  public String getCompanyCodeSys01DOC16() {
    return companyCodeSys01DOC16;
  }
  public void setCompanyCodeSys01DOC16(String companyCodeSys01DOC16) {
    this.companyCodeSys01DOC16 = companyCodeSys01DOC16;
  }
  public java.math.BigDecimal getProgressiveHie02DOC16() {
    return progressiveHie02DOC16;
  }
  public void setProgressiveHie02DOC16(java.math.BigDecimal progressiveHie02DOC16) {
    this.progressiveHie02DOC16 = progressiveHie02DOC16;
  }
  public String getEnabledDOC16() {
    return enabledDOC16;
  }
  public void setEnabledDOC16(String enabledDOC16) {
    this.enabledDOC16 = enabledDOC16;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public java.math.BigDecimal getProgressiveSys10DOC16() {
    return progressiveSys10DOC16;
  }
  public void setProgressiveSys10DOC16(java.math.BigDecimal progressiveSys10DOC16) {
    this.progressiveSys10DOC16 = progressiveSys10DOC16;
  }
  public java.math.BigDecimal getProgressiveHie01HIE02() {
    return progressiveHie01HIE02;
  }
  public void setProgressiveHie01HIE02(java.math.BigDecimal progressiveHie01HIE02) {
    this.progressiveHie01HIE02 = progressiveHie01HIE02;
  }



}
