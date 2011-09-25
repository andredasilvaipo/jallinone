package org.jallinone.scheduler.callouts.java;

import org.openswing.swing.message.receive.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store call-out items info for the grid.</p>
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
public class CallOutItemVO extends ValueObjectImpl {

  private String companyCodeSys01SCH14;
  private String callOutCodeSch10SCH14;
  private String descriptionSYS10;
  private String itemCodeItm01SCH14;
  private java.math.BigDecimal progressiveHie02ITM01;


  public CallOutItemVO() {
  }


  public String getCallOutCodeSch10SCH14() {
    return callOutCodeSch10SCH14;
  }
  public String getCompanyCodeSys01SCH14() {
    return companyCodeSys01SCH14;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public String getItemCodeItm01SCH14() {
    return itemCodeItm01SCH14;
  }
  public void setItemCodeItm01SCH14(String itemCodeItm01SCH14) {
    this.itemCodeItm01SCH14 = itemCodeItm01SCH14;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public void setCompanyCodeSys01SCH14(String companyCodeSys01SCH14) {
    this.companyCodeSys01SCH14 = companyCodeSys01SCH14;
  }
  public void setCallOutCodeSch10SCH14(String callOutCodeSch10SCH14) {
    this.callOutCodeSch10SCH14 = callOutCodeSch10SCH14;
  }
  public java.math.BigDecimal getProgressiveHie02ITM01() {
    return progressiveHie02ITM01;
  }
  public void setProgressiveHie02ITM01(java.math.BigDecimal progressiveHie02ITM01) {
    this.progressiveHie02ITM01 = progressiveHie02ITM01;
  }


}
