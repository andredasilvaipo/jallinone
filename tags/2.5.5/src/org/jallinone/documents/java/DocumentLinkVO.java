package org.jallinone.documents.java;

import org.jallinone.system.customizations.java.BaseValueObject;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a document link in DOC17.</p>
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
public class DocumentLinkVO extends BaseValueObject {

  private String companyCodeSys01DOC17;
  private java.math.BigDecimal progressiveDoc14DOC17;
  private java.math.BigDecimal progressiveHie01DOC17;
  private String levelDescriptionSYS10;
  private java.math.BigDecimal progressiveHIE02;


  public DocumentLinkVO() {
  }


  public String getCompanyCodeSys01DOC17() {
    return companyCodeSys01DOC17;
  }
  public void setCompanyCodeSys01DOC17(String companyCodeSys01DOC17) {
    this.companyCodeSys01DOC17 = companyCodeSys01DOC17;
  }
  public java.math.BigDecimal getProgressiveHie01DOC17() {
    return progressiveHie01DOC17;
  }
  public void setProgressiveHie01DOC17(java.math.BigDecimal progressiveHie01DOC17) {
    this.progressiveHie01DOC17 = progressiveHie01DOC17;
  }
  public java.math.BigDecimal getProgressiveDoc14DOC17() {
    return progressiveDoc14DOC17;
  }
  public void setProgressiveDoc14DOC17(java.math.BigDecimal progressiveDoc14DOC17) {
    this.progressiveDoc14DOC17 = progressiveDoc14DOC17;
  }
  public String getLevelDescriptionSYS10() {
    return levelDescriptionSYS10;
  }
  public void setLevelDescriptionSYS10(String levelDescriptionSYS10) {
    this.levelDescriptionSYS10 = levelDescriptionSYS10;
  }
  public java.math.BigDecimal getProgressiveHIE02() {
    return progressiveHIE02;
  }
  public void setProgressiveHIE02(java.math.BigDecimal progressiveHIE02) {
    this.progressiveHIE02 = progressiveHIE02;
  }

}
