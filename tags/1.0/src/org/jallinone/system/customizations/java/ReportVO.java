package org.jallinone.system.customizations.java;

import org.openswing.swing.message.receive.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value Object used to view a customizable report linked to a specific function.</p>
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
public class ReportVO extends ValueObjectImpl {

  private String companyCodeSys01SYS15;
  private String functionCodeSys06SYS15;
  private String reportNameSYS15;
  private Boolean customFunction;
  private String descriptionSYS10;


  public ReportVO() {
  }


  public String getCompanyCodeSys01SYS15() {
    return companyCodeSys01SYS15;
  }
  public void setCompanyCodeSys01SYS15(String companyCodeSys01SYS15) {
    this.companyCodeSys01SYS15 = companyCodeSys01SYS15;
  }
  public String getFunctionCodeSys06SYS15() {
    return functionCodeSys06SYS15;
  }
  public void setFunctionCodeSys06SYS15(String functionCodeSys06SYS15) {
    this.functionCodeSys06SYS15 = functionCodeSys06SYS15;
  }
  public String getReportNameSYS15() {
    return reportNameSYS15;
  }
  public void setReportNameSYS15(String reportNameSYS15) {
    this.reportNameSYS15 = reportNameSYS15;
  }
  public Boolean getCustomFunction() {
    return customFunction;
  }
  public void setCustomFunction(Boolean customFunction) {
    this.customFunction = customFunction;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }

}
