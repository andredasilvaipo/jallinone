package org.jallinone.system.customizations.java;

import org.openswing.swing.message.receive.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used in the customization windows tree.</p>
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
public class WindowVO extends ValueObjectImpl{

  private java.math.BigDecimal progressiveSYS13;
  private String descriptionSYS10;
  private String tableNameSYS13;


  public WindowVO() {
  }


  public java.math.BigDecimal getProgressiveSYS13() {
    return progressiveSYS13;
  }
  public void setProgressiveSYS13(java.math.BigDecimal progressiveSYS13) {
    this.progressiveSYS13 = progressiveSYS13;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public String getTableNameSYS13() {
    return tableNameSYS13;
  }
  public void setTableNameSYS13(String tableNameSYS13) {
    this.tableNameSYS13 = tableNameSYS13;
  }

}
