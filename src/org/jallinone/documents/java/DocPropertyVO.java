package org.jallinone.documents.java;

import java.math.BigDecimal;
import org.openswing.swing.message.receive.java.*;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a level property value for a specific document.</p>
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
public class DocPropertyVO extends ValueObjectImpl {

  private String companyCodeSys01DOC20;
  private String propertyTypeDOC21;
  private java.math.BigDecimal progressiveHie01DOC21;
  private java.math.BigDecimal progressiveHie02DOC21;
  private java.math.BigDecimal progressiveSys10DOC20;
  private String descriptionSYS10;

  private java.math.BigDecimal progressiveDoc14DOC20;
  private String textValueDOC20;
  private java.math.BigDecimal numValueDOC20;
  private java.sql.Timestamp dateValueDOC20;


  public DocPropertyVO() {
  }


  public String getCompanyCodeSys01DOC20() {
    return companyCodeSys01DOC20;
  }
  public void setCompanyCodeSys01DOC20(String companyCodeSys01DOC20) {
    this.companyCodeSys01DOC20 = companyCodeSys01DOC20;
  }
  public String getPropertyTypeDOC21() {
    return propertyTypeDOC21;
  }
  public void setPropertyTypeDOC21(String propertyTypeDOC21) {
    this.propertyTypeDOC21 = propertyTypeDOC21;
  }
  public java.math.BigDecimal getProgressiveHie01DOC21() {
    return progressiveHie01DOC21;
  }
  public void setProgressiveHie01DOC21(java.math.BigDecimal progressiveHie01DOC21) {
    this.progressiveHie01DOC21 = progressiveHie01DOC21;
  }
  public java.math.BigDecimal getProgressiveHie02DOC21() {
    return progressiveHie02DOC21;
  }
  public void setProgressiveHie02DOC21(java.math.BigDecimal progressiveHie02DOC21) {
    this.progressiveHie02DOC21 = progressiveHie02DOC21;
  }
  public java.math.BigDecimal getProgressiveSys10DOC20() {
    return progressiveSys10DOC20;
  }
  public void setProgressiveSys10DOC20(java.math.BigDecimal progressiveSys10DOC20) {
    this.progressiveSys10DOC20 = progressiveSys10DOC20;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public java.math.BigDecimal getProgressiveDoc14DOC20() {
    return progressiveDoc14DOC20;
  }
  public void setProgressiveDoc14DOC20(java.math.BigDecimal progressiveDoc14DOC20) {
    this.progressiveDoc14DOC20 = progressiveDoc14DOC20;
  }
  public String getTextValueDOC20() {
    return textValueDOC20;
  }
  public void setTextValueDOC20(String textValueDOC20) {
    this.textValueDOC20 = textValueDOC20;
  }
  public java.math.BigDecimal getNumValueDOC20() {
    return numValueDOC20;
  }
  public void setNumValueDOC20(java.math.BigDecimal numValueDOC20) {
    this.numValueDOC20 = numValueDOC20;
  }
  public java.sql.Timestamp getDateValueDOC20() {
    return dateValueDOC20;
  }
  public void setDateValueDOC20(java.sql.Timestamp dateValueDOC20) {
    this.dateValueDOC20 = dateValueDOC20;
  }


}
