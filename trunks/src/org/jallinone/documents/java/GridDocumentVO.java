package org.jallinone.documents.java;

import org.jallinone.system.customizations.java.BaseValueObject;
import java.math.BigDecimal;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to show a document in the grid.</p>
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
public class GridDocumentVO extends BaseValueObject {

  private String companyCodeSys01DOC14;
  private String descriptionDOC14;
  private String filenameDOC14;
  private java.math.BigDecimal progressiveDOC14;
  private java.math.BigDecimal progressiveHie02HIE01;
  private java.math.BigDecimal progressiveHie01DOC17;


  public GridDocumentVO() {
  }


  public String getCompanyCodeSys01DOC14() {
    return companyCodeSys01DOC14;
  }
  public void setCompanyCodeSys01DOC14(String companyCodeSys01DOC14) {
    this.companyCodeSys01DOC14 = companyCodeSys01DOC14;
  }
  public String getDescriptionDOC14() {
    return descriptionDOC14;
  }
  public void setDescriptionDOC14(String descriptionDOC14) {
    this.descriptionDOC14 = descriptionDOC14;
  }
  public java.math.BigDecimal getProgressiveDOC14() {
    return progressiveDOC14;
  }
  public void setProgressiveDOC14(java.math.BigDecimal progressiveDOC14) {
    this.progressiveDOC14 = progressiveDOC14;
  }
  public java.math.BigDecimal getProgressiveHie01DOC17() {
    return progressiveHie01DOC17;
  }
  public java.math.BigDecimal getProgressiveHie02HIE01() {
    return progressiveHie02HIE01;
  }
  public void setProgressiveHie01DOC17(java.math.BigDecimal progressiveHie01DOC17) {
    this.progressiveHie01DOC17 = progressiveHie01DOC17;
  }
  public void setProgressiveHie02HIE01(java.math.BigDecimal progressiveHie02HIE01) {
    this.progressiveHie02HIE01 = progressiveHie02HIE01;
  }
  public String getFilenameDOC14() {
    return filenameDOC14;
  }
  public void setFilenameDOC14(String filenameDOC14) {
    this.filenameDOC14 = filenameDOC14;
  }


}
