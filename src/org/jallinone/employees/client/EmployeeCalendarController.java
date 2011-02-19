package org.jallinone.employees.client;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.employees.java.*;
import org.jallinone.commons.java.*;
import java.math.BigDecimal;
import javax.swing.*;
import org.jallinone.commons.client.CompanyGridController;
import org.openswing.swing.client.GridControl;
import org.jallinone.subjects.java.SubjectPK;
import org.openswing.swing.util.client.ClientSettings;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the grid controller for employee calendar grid.</p>
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
public class EmployeeCalendarController extends CompanyGridController {

  /** grid */
  private GridControl grid = null;


  public EmployeeCalendarController(GridControl grid) {
    this.grid = grid;
  }


  /**
   * Callback method invoked when the user has clicked on the insert button
   * @param valueObject empty value object just created: the user can manage it to fill some attribute values
   */
  public void createValueObject(ValueObject valueObject) throws Exception {
    EmployeeCalendarVO vo = (EmployeeCalendarVO)valueObject;
    vo.setCompanyCodeSys01SCH02((String)grid.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01));
    vo.setProgressiveReg04SCH02((BigDecimal)grid.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04));
    vo.setMorningStartHourSCH02(new java.sql.Timestamp(8*3600*1000));
    vo.setMorningEndHourSCH02(new java.sql.Timestamp(12*3600*1000));
    vo.setAfternoonStartHourSCH02(new java.sql.Timestamp(13*3600*1000));
    vo.setAfternoonEndHourSCH02(new java.sql.Timestamp(17*3600*1000));
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
   * @param rowNumbers row indexes related to the new rows to save
* @param newValueObjects list of new value objects to save
* @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
    EmployeeCalendarVO vo = null;
    for(int i=0;i<newValueObjects.size();i++) {
      vo = (EmployeeCalendarVO)newValueObjects.get(i);
      if (!checkTimes(vo)) {
        return new ErrorResponse(ClientSettings.getInstance().getResources().getResource("start time must be less than end time"));
      }
    }

    return ClientUtils.getData("insertEmployeeCalendars",newValueObjects);
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in EDIT mode.
   * @param rowNumbers row indexes related to the changed rows
   * @param oldPersistentObjects old value objects, previous the changes
   * @param persistentObjects value objects relatied to the changed rows
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response updateRecords(int[] rowNumbers,ArrayList oldPersistentObjects,ArrayList persistentObjects) throws Exception {
    for(int i=0;i<persistentObjects.size();i++)
      if (!checkTimes((EmployeeCalendarVO)persistentObjects.get(i))) {
        return new ErrorResponse(ClientSettings.getInstance().getResources().getResource("start time must be less than end time"));
      }
    return ClientUtils.getData("updateEmployeeCalendars",new ArrayList[]{oldPersistentObjects,persistentObjects});
  }


  /**
   * Check if start/end times are correct.
   */
  private boolean checkTimes(EmployeeCalendarVO vo) {
    if (vo.getMorningStartHourSCH02()!=null && vo.getMorningEndHourSCH02()==null ||
        vo.getMorningStartHourSCH02()==null && vo.getMorningEndHourSCH02()!=null ||
        vo.getAfternoonStartHourSCH02()!=null && vo.getAfternoonEndHourSCH02()==null ||
        vo.getAfternoonStartHourSCH02()==null && vo.getAfternoonEndHourSCH02()!=null) {
      return false;
    }
    if (vo.getMorningStartHourSCH02()!=null && vo.getMorningEndHourSCH02()!=null &&
        vo.getMorningStartHourSCH02().getTime()>vo.getMorningEndHourSCH02().getTime())
      return false;
    if (vo.getAfternoonStartHourSCH02()!=null && vo.getAfternoonEndHourSCH02()!=null &&
        vo.getAfternoonStartHourSCH02().getTime()>vo.getAfternoonEndHourSCH02().getTime())
      return false;
    return true;
  }


  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    return ClientUtils.getData("deleteEmployeeCalendars",persistentObjects);
  }




}
