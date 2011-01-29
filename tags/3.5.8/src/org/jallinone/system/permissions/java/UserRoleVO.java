package org.jallinone.system.permissions.java;

import org.openswing.swing.message.receive.java.*;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store roles associated to the specified user.</p>
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
public class UserRoleVO extends ValueObjectImpl {



  private String usernameSys03SYS14;
  private java.math.BigDecimal progressiveSys04SYS14;
  private Boolean selected;
  private String descriptionSYS10;


  public UserRoleVO() {
  }


  public String getUsernameSys03SYS14() {
    return usernameSys03SYS14;
  }
  public void setUsernameSys03SYS14(String usernameSys03SYS14) {
    this.usernameSys03SYS14 = usernameSys03SYS14;
  }
  public java.math.BigDecimal getProgressiveSys04SYS14() {
    return progressiveSys04SYS14;
  }
  public void setProgressiveSys04SYS14(java.math.BigDecimal progressiveSys04SYS14) {
    this.progressiveSys04SYS14 = progressiveSys04SYS14;
  }
  public Boolean getSelected() {
    return selected;
  }
  public void setSelected(Boolean selected) {
    this.selected = selected;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }

}
