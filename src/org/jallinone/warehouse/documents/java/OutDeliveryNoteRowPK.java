package org.jallinone.warehouse.documents.java;

import java.io.Serializable;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store in delivery note row info.</p>
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
public class OutDeliveryNoteRowPK implements Serializable {

  private String companyCodeSys01DOC10;
  private String docTypeDOC10;
  private java.math.BigDecimal docYearDOC10;
  private java.math.BigDecimal docNumberDOC10;
  private java.math.BigDecimal docNumberDoc01DOC10;
  private java.math.BigDecimal docYearDoc01DOC10;
  private String docTypeDoc01DOC10;
  private java.math.BigDecimal rowNumberDOC10;
  private String itemCodeItm01DOC10;


  public OutDeliveryNoteRowPK(
      String companyCodeSys01DOC10,String docTypeDOC10,java.math.BigDecimal docYearDOC10,java.math.BigDecimal docNumberDOC10,
      String docTypeDoc01DOC10,java.math.BigDecimal docYearDoc01DOC10,java.math.BigDecimal docNumberDoc01DOC10,
      java.math.BigDecimal rowNumberDOC10,String itemCodeItm01DOC10
  ) {
    this.companyCodeSys01DOC10 = companyCodeSys01DOC10;
    this.docTypeDOC10 = docTypeDOC10;
    this.docYearDOC10 = docYearDOC10;
    this.docNumberDOC10 = docNumberDOC10;
    this.docTypeDoc01DOC10 = docTypeDoc01DOC10;
    this.docYearDoc01DOC10 = docYearDoc01DOC10;
    this.docNumberDoc01DOC10 = docNumberDoc01DOC10;
    this.rowNumberDOC10 = rowNumberDOC10;
    this.itemCodeItm01DOC10 = itemCodeItm01DOC10;
  }


  public String getCompanyCodeSys01DOC10() {
    return companyCodeSys01DOC10;
  }
  public String getDocTypeDOC10() {
    return docTypeDOC10;
  }
  public java.math.BigDecimal getDocYearDOC10() {
    return docYearDOC10;
  }
  public java.math.BigDecimal getDocNumberDOC10() {
    return docNumberDOC10;
  }
  public java.math.BigDecimal getDocNumberDoc01DOC10() {
    return docNumberDoc01DOC10;
  }
  public java.math.BigDecimal getDocYearDoc01DOC10() {
    return docYearDoc01DOC10;
  }
  public String getDocTypeDoc01DOC10() {
    return docTypeDoc01DOC10;
  }
  public String getItemCodeItm01DOC10() {
    return itemCodeItm01DOC10;
  }
  public java.math.BigDecimal getRowNumberDOC10() {
    return rowNumberDOC10;
  }

}
