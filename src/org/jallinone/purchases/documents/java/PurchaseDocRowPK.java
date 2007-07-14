package org.jallinone.purchases.documents.java;

import java.io.Serializable;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store purchase order row info, for a grid.</p>
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
public class PurchaseDocRowPK implements Serializable {

  private String companyCodeSys01DOC07;
  private String docTypeDOC07;
  private java.math.BigDecimal docYearDOC07;
  private java.math.BigDecimal docNumberDOC07;
  private String itemCodeItm01DOC07;


  public PurchaseDocRowPK(String companyCodeSys01DOC07,String docTypeDOC07,java.math.BigDecimal docYearDOC07,java.math.BigDecimal docNumberDOC07,String itemCodeItm01DOC07) {
    this.companyCodeSys01DOC07 = companyCodeSys01DOC07;
    this.docTypeDOC07 = docTypeDOC07;
    this.docYearDOC07 = docYearDOC07;
    this.docNumberDOC07 = docNumberDOC07;
    this.itemCodeItm01DOC07 = itemCodeItm01DOC07;
  }


  public String getCompanyCodeSys01DOC07() {
    return companyCodeSys01DOC07;
  }
  public String getDocTypeDOC07() {
    return docTypeDOC07;
  }
  public java.math.BigDecimal getDocYearDOC07() {
    return docYearDOC07;
  }
  public java.math.BigDecimal getDocNumberDOC07() {
    return docNumberDOC07;
  }
  public String getItemCodeItm01DOC07() {
    return itemCodeItm01DOC07;
  }


}
