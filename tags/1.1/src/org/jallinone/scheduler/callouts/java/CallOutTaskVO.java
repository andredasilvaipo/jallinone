package org.jallinone.scheduler.callouts.java;

import org.openswing.swing.message.receive.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store call-out tasks info for the grid.</p>
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
public class CallOutTaskVO extends ValueObjectImpl {

  private String companyCodeSys01SCH12;
  private String callOutCodeSch10SCH12;
  private String descriptionSYS10;
  private String taskCodeReg07SCH12;


  public CallOutTaskVO() {
  }
  public String getCallOutCodeSch10SCH12() {
    return callOutCodeSch10SCH12;
  }
  public String getCompanyCodeSys01SCH12() {
    return companyCodeSys01SCH12;
  }
  public String getDescriptionSYS10() {
    return descriptionSYS10;
  }
  public String getTaskCodeReg07SCH12() {
    return taskCodeReg07SCH12;
  }
  public void setTaskCodeReg07SCH12(String taskCodeReg07SCH12) {
    this.taskCodeReg07SCH12 = taskCodeReg07SCH12;
  }
  public void setDescriptionSYS10(String descriptionSYS10) {
    this.descriptionSYS10 = descriptionSYS10;
  }
  public void setCompanyCodeSys01SCH12(String companyCodeSys01SCH12) {
    this.companyCodeSys01SCH12 = companyCodeSys01SCH12;
  }
  public void setCallOutCodeSch10SCH12(String callOutCodeSch10SCH12) {
    this.callOutCodeSch10SCH12 = callOutCodeSch10SCH12;
  }


}
