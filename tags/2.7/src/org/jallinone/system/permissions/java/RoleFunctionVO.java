package org.jallinone.system.permissions.java;

import org.openswing.swing.message.receive.java.*;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a function-role association.</p>
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
public class RoleFunctionVO extends ValueObjectImpl {



  private String functionCodeSys06SYS07;
  private java.math.BigDecimal progressiveSys04SYS07;
  private Boolean canView;
  private Boolean canInsSYS07;
  private Boolean canUpdSYS07;
  private Boolean canDelSYS07;
  private String descriptionSYS10;
  private Boolean useCompanyCodeSYS06;


  public RoleFunctionVO() {
  }


  public String getFunctionCodeSys06SYS07() {
    return functionCodeSys06SYS07;
  }
  public void setFunctionCodeSys06SYS07(String functionCodeSys06SYS07) {
    this.functionCodeSys06SYS07 = functionCodeSys06SYS07;
  }
  public java.math.BigDecimal getProgressiveSys04SYS07() {
    return progressiveSys04SYS07;
  }
  public void setProgressiveSys04SYS07(java.math.BigDecimal progressiveSys04SYS07) {
    this.progressiveSys04SYS07 = progressiveSys04SYS07;
  }
  public Boolean getCanView() {
    return canView;
  }
  public void setCanView(Boolean canView) {
    this.canView = canView;
  }
  public Boolean getCanInsSYS07() {
    return canInsSYS07;
  }
  public void setCanInsSYS07(Boolean canInsSYS07) {
    this.canInsSYS07 = canInsSYS07;
  }
  public Boolean getCanUpdSYS07() {
    return canUpdSYS07;
  }
  public void setCanUpdSYS07(Boolean canUpdSYS07) {
    this.canUpdSYS07 = canUpdSYS07;
  }
  public Boolean getCanDelSYS07() {
    return canDelSYS07;
  }
  public void setCanDelSYS07(Boolean canDelSYS07) {
    this.canDelSYS07 = canDelSYS07;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public Boolean getUseCompanyCodeSYS06() {
    return useCompanyCodeSYS06;
  }
  public void setUseCompanyCodeSYS06(Boolean useCompanyCodeSYS06) {
    this.useCompanyCodeSYS06 = useCompanyCodeSYS06;
  }

}
