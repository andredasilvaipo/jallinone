package org.jallinone.documents.java;

import org.jallinone.system.customizations.java.BaseValueObject;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a document version in DOC15.</p>
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
public class DocumentVersionVO extends BaseValueObject {

  private String companyCodeSys01DOC15;
  private String createUsernameDOC15;
  private java.math.BigDecimal progressiveDoc14DOC15;
  private java.math.BigDecimal versionDOC15;
  private java.sql.Timestamp createDateDOC15;

  /** used to store the last document version (on insert new version) */
  private byte[] document;


  public DocumentVersionVO() {
  }


  public String getCompanyCodeSys01DOC15() {
    return companyCodeSys01DOC15;
  }
  public void setCompanyCodeSys01DOC15(String companyCodeSys01DOC15) {
    this.companyCodeSys01DOC15 = companyCodeSys01DOC15;
  }
  public String getCreateUsernameDOC15() {
    return createUsernameDOC15;
  }
  public void setCreateUsernameDOC15(String createUsernameDOC15) {
    this.createUsernameDOC15 = createUsernameDOC15;
  }
  public java.math.BigDecimal getProgressiveDoc14DOC15() {
    return progressiveDoc14DOC15;
  }
  public void setProgressiveDoc14DOC15(java.math.BigDecimal progressiveDoc14DOC15) {
    this.progressiveDoc14DOC15 = progressiveDoc14DOC15;
  }
  public java.math.BigDecimal getVersionDOC15() {
    return versionDOC15;
  }
  public void setVersionDOC15(java.math.BigDecimal versionDOC15) {
    this.versionDOC15 = versionDOC15;
  }
  public java.sql.Timestamp getCreateDateDOC15() {
    return createDateDOC15;
  }
  public void setCreateDateDOC15(java.sql.Timestamp createDateDOC15) {
    this.createDateDOC15 = createDateDOC15;
  }

}
