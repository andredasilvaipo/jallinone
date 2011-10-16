package org.jallinone.sales.documents.java;

import java.io.Serializable;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store sale documents (e.g. sale orders) info, for a grid.</p>
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
public class SaleDocPK implements Serializable {

  private String companyCodeSys01DOC01;
  private String docTypeDOC01;
  private java.math.BigDecimal docYearDOC01;
  private java.math.BigDecimal docNumberDOC01;


  public SaleDocPK(String companyCodeSys01DOC01,String docTypeDOC01,java.math.BigDecimal docYearDOC01,java.math.BigDecimal docNumberDOC01) {
    this.companyCodeSys01DOC01 = companyCodeSys01DOC01;
    this.docTypeDOC01 = docTypeDOC01;
    this.docYearDOC01 = docYearDOC01;
    this.docNumberDOC01 = docNumberDOC01;
  }

  
  public SaleDocPK() {}
  

  public String getCompanyCodeSys01DOC01() {
    return companyCodeSys01DOC01;
  }
  public String getDocTypeDOC01() {
    return docTypeDOC01;
  }
  public java.math.BigDecimal getDocYearDOC01() {
    return docYearDOC01;
  }
  public java.math.BigDecimal getDocNumberDOC01() {
	  return docNumberDOC01;
  }


  public void setCompanyCodeSys01DOC01(String companyCodeSys01DOC01) {
	  this.companyCodeSys01DOC01 = companyCodeSys01DOC01;
  }


  public void setDocTypeDOC01(String docTypeDOC01) {
	  this.docTypeDOC01 = docTypeDOC01;
  }


  public void setDocYearDOC01(java.math.BigDecimal docYearDOC01) {
	  this.docYearDOC01 = docYearDOC01;
  }


  public void setDocNumberDOC01(java.math.BigDecimal docNumberDOC01) {
	  this.docNumberDOC01 = docNumberDOC01;
  }

  
  
  
}
