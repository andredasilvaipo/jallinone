package org.jallinone.subjects.java;

import org.openswing.swing.message.receive.java.*;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a reference (table REG15).</p>
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
public class ReferenceVO extends ValueObjectImpl {

  private String companyCodeSys01REG15;
  private String referenceTypeREG15;
  private String valueREG15;
  private java.math.BigDecimal progressiveREG15;
  private java.math.BigDecimal progressiveReg04REG15;


  public ReferenceVO() {
  }


  public String getCompanyCodeSys01REG15() {
    return companyCodeSys01REG15;
  }
  public void setCompanyCodeSys01REG15(String companyCodeSys01REG15) {
    this.companyCodeSys01REG15 = companyCodeSys01REG15;
  }
  public String getReferenceTypeREG15() {
    return referenceTypeREG15;
  }
  public void setReferenceTypeREG15(String referenceTypeREG15) {
    this.referenceTypeREG15 = referenceTypeREG15;
  }
  public String getValueREG15() {
    return valueREG15;
  }
  public void setValueREG15(String valueREG15) {
    this.valueREG15 = valueREG15;
  }
  public java.math.BigDecimal getProgressiveREG15() {
    return progressiveREG15;
  }
  public void setProgressiveREG15(java.math.BigDecimal progressiveREG15) {
    this.progressiveREG15 = progressiveREG15;
  }
  public java.math.BigDecimal getProgressiveReg04REG15() {
    return progressiveReg04REG15;
  }
  public void setProgressiveReg04REG15(java.math.BigDecimal progressiveReg04REG15) {
    this.progressiveReg04REG15 = progressiveReg04REG15;
  }

}
