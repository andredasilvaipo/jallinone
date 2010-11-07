package org.jallinone.system.permissions.java;

import org.openswing.swing.message.receive.java.*;
import java.math.BigDecimal;





/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a role.</p>
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


public class RoleVO extends ValueObjectImpl {


  private java.math.BigDecimal progressiveSYS04;
  private String descriptionSYS10;
  private String enabledSYS04;
  private java.math.BigDecimal progressiveSys10SYS04;


  public RoleVO() {
  }


  public java.math.BigDecimal getProgressiveSYS04() {
    return progressiveSYS04;
  }
  public void setProgressiveSYS04(java.math.BigDecimal progressiveSYS04) {
    this.progressiveSYS04 = progressiveSYS04;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public String getEnabledSYS04() {
    return enabledSYS04;
  }
  public void setEnabledSYS04(String enabledSYS04) {
    this.enabledSYS04 = enabledSYS04;
  }
  public java.math.BigDecimal getProgressiveSys10SYS04() {
    return progressiveSys10SYS04;
  }
  public void setProgressiveSys10SYS04(java.math.BigDecimal progressiveSys10SYS04) {
    this.progressiveSys10SYS04 = progressiveSys10SYS04;
  }

}
