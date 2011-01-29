package org.jallinone.warehouse.documents.java;

import java.io.Serializable;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Primary key that identifies an in/out delivery note.</p>
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
public class DeliveryNotePK implements Serializable {

  private String companyCodeSys01DOC08;
  private String docTypeDOC08;
  private java.math.BigDecimal docYearDOC08;
  private java.math.BigDecimal docNumberDOC08;


  public DeliveryNotePK(String companyCodeSys01DOC08,String docTypeDOC08,java.math.BigDecimal docYearDOC08,java.math.BigDecimal docNumberDOC08) {
    this.companyCodeSys01DOC08 = companyCodeSys01DOC08;
    this.docTypeDOC08 = docTypeDOC08;
    this.docYearDOC08 = docYearDOC08;
    this.docNumberDOC08 = docNumberDOC08;
  }

  
  public DeliveryNotePK() {}
  

  public String getCompanyCodeSys01DOC08() {
    return companyCodeSys01DOC08;
  }
  public String getDocTypeDOC08() {
	  return docTypeDOC08;
  }
  public java.math.BigDecimal getDocYearDOC08() {
	  return docYearDOC08;
  }
  public java.math.BigDecimal getDocNumberDOC08() {
	  return docNumberDOC08;
  }


  public void setCompanyCodeSys01DOC08(String companyCodeSys01DOC08) {
	  this.companyCodeSys01DOC08 = companyCodeSys01DOC08;
  }


  public void setDocTypeDOC08(String docTypeDOC08) {
	  this.docTypeDOC08 = docTypeDOC08;
  }


  public void setDocYearDOC08(java.math.BigDecimal docYearDOC08) {
	  this.docYearDOC08 = docYearDOC08;
  }


  public void setDocNumberDOC08(java.math.BigDecimal docNumberDOC08) {
	  this.docNumberDOC08 = docNumberDOC08;
  }



  
}
