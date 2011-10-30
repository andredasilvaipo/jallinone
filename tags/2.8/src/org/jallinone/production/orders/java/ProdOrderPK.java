package org.jallinone.production.orders.java;

import java.io.Serializable;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Primary key used to identify a production order (table DOC22).</p>
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
public class ProdOrderPK implements Serializable {

  private String companyCodeSys01DOC22;
  private java.math.BigDecimal docYearDOC22;
  private java.math.BigDecimal docNumberDOC22;



  public ProdOrderPK(String companyCodeSys01DOC22,java.math.BigDecimal docYearDOC22,java.math.BigDecimal docNumberDOC22) {
    this.companyCodeSys01DOC22 = companyCodeSys01DOC22;
    this.docYearDOC22 = docYearDOC22;
    this.docNumberDOC22 = docNumberDOC22;
  }

  
  public ProdOrderPK() {}

  

  public String getCompanyCodeSys01DOC22() {
    return companyCodeSys01DOC22;
  }
  public java.math.BigDecimal getDocNumberDOC22() {
    return docNumberDOC22;
  }
  public java.math.BigDecimal getDocYearDOC22() {
    return docYearDOC22;
  }


  public void setCompanyCodeSys01DOC22(String companyCodeSys01DOC22) {
	  this.companyCodeSys01DOC22 = companyCodeSys01DOC22;
  }


  public void setDocYearDOC22(java.math.BigDecimal docYearDOC22) {
	  this.docYearDOC22 = docYearDOC22;
  }


  public void setDocNumberDOC22(java.math.BigDecimal docNumberDOC22) {
	  this.docNumberDOC22 = docNumberDOC22;
  }


  
  

}
