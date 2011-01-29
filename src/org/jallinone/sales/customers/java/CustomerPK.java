package org.jallinone.sales.customers.java;

import java.io.Serializable;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Customer primary key.</p>
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
public class CustomerPK implements Serializable {

  private String companyCodeSys01SAL07;
  private java.math.BigDecimal progressiveReg04SAL07;
  private String subjectTypeREG04;


  public CustomerPK(String companyCodeSys01SAL07,java.math.BigDecimal progressiveReg04SAL07,String subjectTypeREG04) {
    this.companyCodeSys01SAL07 = companyCodeSys01SAL07;
    this.progressiveReg04SAL07 = progressiveReg04SAL07;
    this.subjectTypeREG04 = subjectTypeREG04;
  }

  
  public CustomerPK() {}
  

  public String getCompanyCodeSys01SAL07() {
    return companyCodeSys01SAL07;
  }

  public java.math.BigDecimal getProgressiveReg04SAL07() {
    return progressiveReg04SAL07;
  }
  public String getSubjectTypeREG04() {
    return subjectTypeREG04;
  }


  public void setCompanyCodeSys01SAL07(String companyCodeSys01SAL07) {
	  this.companyCodeSys01SAL07 = companyCodeSys01SAL07;
  }


  public void setProgressiveReg04SAL07(java.math.BigDecimal progressiveReg04SAL07) {
	  this.progressiveReg04SAL07 = progressiveReg04SAL07;
  }


  public void setSubjectTypeREG04(String subjectTypeREG04) {
	  this.subjectTypeREG04 = subjectTypeREG04;
  }



  
}