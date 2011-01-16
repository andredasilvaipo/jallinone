package org.jallinone.scheduler.activities.java;

import java.io.Serializable;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Primary key used to retrieve/delete a specified scheduled activity (from table SCH06).</p>
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
public class ScheduledActivityPK implements Serializable {

  private String companyCodeSys01SCH06;
  private java.math.BigDecimal progressiveSCH06;


  public ScheduledActivityPK(String companyCodeSys01SCH06,java.math.BigDecimal progressiveSCH06) {
    this.companyCodeSys01SCH06 = companyCodeSys01SCH06;
    this.progressiveSCH06 = progressiveSCH06;
  }

  
  public ScheduledActivityPK() {}
  

  public String getCompanyCodeSys01SCH06() {
    return companyCodeSys01SCH06;
  }
  public java.math.BigDecimal getProgressiveSCH06() {
    return progressiveSCH06;
  }
  public void setCompanyCodeSys01SCH06(String companyCodeSys01SCH06) {
    this.companyCodeSys01SCH06 = companyCodeSys01SCH06;
  }
  public void setProgressiveSCH06(java.math.BigDecimal progressiveSCH06) {
    this.progressiveSCH06 = progressiveSCH06;
  }
  
  
  
  

}