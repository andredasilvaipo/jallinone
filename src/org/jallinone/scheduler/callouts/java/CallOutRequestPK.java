package org.jallinone.scheduler.callouts.java;

import java.io.Serializable;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Primary key for a call-out request.</p>
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
public class CallOutRequestPK implements Serializable {

  private String companyCodeSys01SCH03;
  private java.math.BigDecimal requestYearSCH03;
  private java.math.BigDecimal progressiveSCH03;

  
  public CallOutRequestPK() {}
  

  public CallOutRequestPK(String companyCodeSys01SCH03,java.math.BigDecimal requestYearSCH03,java.math.BigDecimal progressiveSCH03) {
    this.companyCodeSys01SCH03 = companyCodeSys01SCH03;
    this.requestYearSCH03 = requestYearSCH03;
    this.progressiveSCH03 = progressiveSCH03;
  }


  public String getCompanyCodeSys01SCH03() {
    return companyCodeSys01SCH03;
  }
  public void setCompanyCodeSys01SCH03(String companyCodeSys01SCH03) {
    this.companyCodeSys01SCH03 = companyCodeSys01SCH03;
  }
  public java.math.BigDecimal getRequestYearSCH03() {
    return requestYearSCH03;
  }
  public void setRequestYearSCH03(java.math.BigDecimal requestYearSCH03) {
    this.requestYearSCH03 = requestYearSCH03;
  }
  public java.math.BigDecimal getProgressiveSCH03() {
    return progressiveSCH03;
  }
  public void setProgressiveSCH03(java.math.BigDecimal progressiveSCH03) {
    this.progressiveSCH03 = progressiveSCH03;
  }

  
  
  
}