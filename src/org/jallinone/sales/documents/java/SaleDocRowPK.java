package org.jallinone.sales.documents.java;

import java.io.Serializable;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store sale order row info, for a grid.</p>
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
 * Software Foundation, Inc., 775 Mass Ave, Cambridge, MA 02139, USA.
 *
 *       The author may be contacted at:
 *           maurocarniel@tin.it</p>
 *
 * @author Mauro Carniel
 * @version 1.0
 */
public class SaleDocRowPK implements Serializable {

  private String companyCodeSys01DOC02;
  private String docTypeDOC02;
  private java.math.BigDecimal docYearDOC02;
  private java.math.BigDecimal docNumberDOC02;
  private String itemCodeItm01DOC02;


  public SaleDocRowPK(String companyCodeSys01DOC02,String docTypeDOC02,java.math.BigDecimal docYearDOC02,java.math.BigDecimal docNumberDOC02,String itemCodeItm01DOC02) {
    this.companyCodeSys01DOC02 = companyCodeSys01DOC02;
    this.docTypeDOC02 = docTypeDOC02;
    this.docYearDOC02 = docYearDOC02;
    this.docNumberDOC02 = docNumberDOC02;
    this.itemCodeItm01DOC02 = itemCodeItm01DOC02;
  }


  public String getCompanyCodeSys01DOC02() {
    return companyCodeSys01DOC02;
  }
  public String getDocTypeDOC02() {
    return docTypeDOC02;
  }
  public java.math.BigDecimal getDocYearDOC02() {
    return docYearDOC02;
  }
  public java.math.BigDecimal getDocNumberDOC02() {
    return docNumberDOC02;
  }
  public String getItemCodeItm01DOC02() {
    return itemCodeItm01DOC02;
  }


}
