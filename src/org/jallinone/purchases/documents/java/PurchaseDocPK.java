package org.jallinone.purchases.documents.java;

import java.io.Serializable;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store purchase orders info, for a grid.</p>
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
public class PurchaseDocPK implements Serializable {

  private String companyCodeSys01DOC06;
  private String docTypeDOC06;
  private java.math.BigDecimal docYearDOC06;
  private java.math.BigDecimal docNumberDOC06;


  public PurchaseDocPK(String companyCodeSys01DOC06,String docTypeDOC06,java.math.BigDecimal docYearDOC06,java.math.BigDecimal docNumberDOC06) {
	  this.companyCodeSys01DOC06 = companyCodeSys01DOC06;
	  this.docTypeDOC06 = docTypeDOC06;
	  this.docYearDOC06 = docYearDOC06;
	  this.docNumberDOC06 = docNumberDOC06;
  }


  public PurchaseDocPK() {}


  public String getCompanyCodeSys01DOC06() {
	  return companyCodeSys01DOC06;
  }
  public String getDocTypeDOC06() {
	  return docTypeDOC06;
  }
  public java.math.BigDecimal getDocYearDOC06() {
	  return docYearDOC06;
  }
  public java.math.BigDecimal getDocNumberDOC06() {
	  return docNumberDOC06;
  }


  public void setCompanyCodeSys01DOC06(String companyCodeSys01DOC06) {
	  this.companyCodeSys01DOC06 = companyCodeSys01DOC06;
  }


  public void setDocTypeDOC06(String docTypeDOC06) {
	  this.docTypeDOC06 = docTypeDOC06;
  }


  public void setDocYearDOC06(java.math.BigDecimal docYearDOC06) {
	  this.docYearDOC06 = docYearDOC06;
  }


  public void setDocNumberDOC06(java.math.BigDecimal docNumberDOC06) {
	  this.docNumberDOC06 = docNumberDOC06;
  }



  

}
