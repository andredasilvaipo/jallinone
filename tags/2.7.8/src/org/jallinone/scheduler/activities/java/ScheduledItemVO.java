package org.jallinone.scheduler.activities.java;

import org.jallinone.system.customizations.java.BaseValueObject;
import org.openswing.swing.message.receive.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a scheduled item (table SCH15).</p>
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
public class ScheduledItemVO extends ValueObjectImpl {

  private java.math.BigDecimal progressiveSch06SCH15;
  private String companyCodeSys01SCH15;
  private String itemCodeItm01SCH15;
  private String descriptionSYS10;
  private java.math.BigDecimal progressiveHie02ITM01;
  private java.math.BigDecimal qtySCH15;


  public ScheduledItemVO() {
  }


  public java.math.BigDecimal getProgressiveSch06SCH15() {
    return progressiveSch06SCH15;
  }
  public void setProgressiveSch06SCH15(java.math.BigDecimal progressiveSch06SCH15) {
    this.progressiveSch06SCH15 = progressiveSch06SCH15;
  }
  public String getItemCodeItm01SCH15() {
    return itemCodeItm01SCH15;
  }
  public void setItemCodeItm01SCH15(String itemCodeItm01SCH15) {
    this.itemCodeItm01SCH15 = itemCodeItm01SCH15;
  }
  public String getCompanyCodeSys01SCH15() {
    return companyCodeSys01SCH15;
  }
  public void setCompanyCodeSys01SCH15(String companyCodeSys01SCH15) {
    this.companyCodeSys01SCH15 = companyCodeSys01SCH15;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public java.math.BigDecimal getProgressiveHie02ITM01() {
    return progressiveHie02ITM01;
  }
  public void setProgressiveHie02ITM01(java.math.BigDecimal progressiveHie02ITM01) {
    this.progressiveHie02ITM01 = progressiveHie02ITM01;
  }
  public java.math.BigDecimal getQtySCH15() {
    return qtySCH15;
  }
  public void setQtySCH15(java.math.BigDecimal qtySCH15) {
    this.qtySCH15 = qtySCH15;
  }


}
