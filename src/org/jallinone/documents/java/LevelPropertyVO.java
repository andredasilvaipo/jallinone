package org.jallinone.documents.java;

import java.math.BigDecimal;
import org.openswing.swing.message.receive.java.*;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a level property for a document hierarchy.</p>
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
public class LevelPropertyVO extends ValueObjectImpl {

  private String companyCodeSys01DOC21;
  private String propertyTypeDOC21;
  private java.math.BigDecimal progressiveHie01DOC21;
  private java.math.BigDecimal progressiveHie02DOC21;
  private java.math.BigDecimal progressiveSys10DOC21;
  private String descriptionSYS10;


  public LevelPropertyVO() {
  }


  public String getCompanyCodeSys01DOC21() {
    return companyCodeSys01DOC21;
  }
  public void setCompanyCodeSys01DOC21(String companyCodeSys01DOC21) {
    this.companyCodeSys01DOC21 = companyCodeSys01DOC21;
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
  public java.math.BigDecimal getProgressiveSys10DOC21() {
    return progressiveSys10DOC21;
  }
  public void setProgressiveSys10DOC21(java.math.BigDecimal progressiveSys10DOC21) {
    this.progressiveSys10DOC21 = progressiveSys10DOC21;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }


}
