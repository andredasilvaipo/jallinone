package org.jallinone.system.permissions.java;

import org.openswing.swing.message.receive.java.*;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a function-role-company association.</p>
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
public class RoleFunctionCompanyVO extends ValueObjectImpl {



  private String functionCodeSys06SYS02;
  private java.math.BigDecimal progressiveSys04SYS02;
  private Boolean canView;
  private Boolean canInsSYS02;
  private Boolean canUpdSYS02;
  private Boolean canDelSYS02;
  private String name_1REG04;
  private String companyCodeSys01SYS02;


  public RoleFunctionCompanyVO() {
  }


  public String getFunctionCodeSys06SYS02() {
    return functionCodeSys06SYS02;
  }
  public void setFunctionCodeSys06SYS02(String functionCodeSys06SYS02) {
    this.functionCodeSys06SYS02 = functionCodeSys06SYS02;
  }
  public java.math.BigDecimal getProgressiveSys04SYS02() {
    return progressiveSys04SYS02;
  }
  public void setProgressiveSys04SYS02(java.math.BigDecimal progressiveSys04SYS02) {
    this.progressiveSys04SYS02 = progressiveSys04SYS02;
  }
  public Boolean getCanView() {
    return canView;
  }
  public void setCanView(Boolean canView) {
    this.canView = canView;
  }
  public Boolean getCanInsSYS02() {
    return canInsSYS02;
  }
  public void setCanInsSYS02(Boolean canInsSYS02) {
    this.canInsSYS02 = canInsSYS02;
  }
  public Boolean getCanUpdSYS02() {
    return canUpdSYS02;
  }
  public void setCanUpdSYS02(Boolean canUpdSYS02) {
    this.canUpdSYS02 = canUpdSYS02;
  }
  public Boolean getCanDelSYS02() {
    return canDelSYS02;
  }
  public void setCanDelSYS02(Boolean canDelSYS02) {
    this.canDelSYS02 = canDelSYS02;
  }
  public String getName_1REG04() {
    return name_1REG04;
  }
  public void setName_1REG04(String name_1REG04) {
    this.name_1REG04 = name_1REG04;
  }
  public String getCompanyCodeSys01SYS02() {
    return companyCodeSys01SYS02;
  }
  public void setCompanyCodeSys01SYS02(String companyCodeSys01SYS02) {
    this.companyCodeSys01SYS02 = companyCodeSys01SYS02;
  }

}
