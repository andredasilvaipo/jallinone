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
import org.jallinone.scheduler.activities.java.EmployeeActivityVO;
import org.jallinone.scheduler.activities.java.ScheduledActivityVO;
import org.jallinone.scheduler.activities.java.ScheduledEmployeeVO;
import org.jallinone.scheduler.activities.java.ScheduledActivityPK;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the grid controller for employee absences grid.</p>
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
public class EmployeeAbsencesController extends CompanyGridController {

  /** grid */
  private GridControl grid = null;

  /** grid parent frame */
  private EmployeeDetailFrame detailFrame = null;


  public EmployeeAbsencesController(EmployeeDetailFrame detailFrame,GridControl grid) {
    this.detailFrame = detailFrame;
    this.grid = grid;
  }


  /**
   * Callback method invoked when the user has clicked on the insert button
   * @param valueObject empty value object just created: the user can manage it to fill some attribute values
   */
  public void createValueObject(ValueObject valueObject) throws Exception {
    EmployeeActivityVO vo = (EmployeeActivityVO)valueObject;
    DetailEmployeeVO empVO = (DetailEmployeeVO)detailFrame.getForm().getVOModel().getValueObject();
    vo.setCompanyCodeSys01SCH06((String)grid.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01));
    vo.setProgressiveReg04SCH07((BigDecimal)grid.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04));
    vo.setStartDateSCH07(new java.sql.Timestamp(System.currentTimeMillis()));
    vo.setEndDateSCH07(new java.sql.Timestamp(System.currentTimeMillis()));
    vo.setDurationSCH07(new BigDecimal(0));
    vo.setEmployeeCodeSCH01(empVO.getCompanyCodeSys01SCH01());
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
   * @param rowNumbers row indexes related to the new rows to save
* @param newValueObjects list of new value objects to save
* @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
    ValueObject newValueObject = (ValueObject)newValueObjects.get(0);
    if (!checkTimes((EmployeeActivityVO)newValueObject)) {
      return new ErrorResponse(ClientSettings.getInstance().getResources().getResource("start time must be less than end time"));
    }
    EmployeeActivityVO vo = (EmployeeActivityVO)newValueObject;
    ScheduledActivityVO actVO = new ScheduledActivityVO();
    actVO.setCompanyCodeSys01SCH06(vo.getCompanyCodeSys01SCH06());
    actVO.setCompletionPercSCH06(new BigDecimal(0));
    actVO.setDescriptionSCH06(vo.getDescriptionSCH06());
    actVO.setEstimatedDurationSCH06(vo.getDurationSCH07());
    actVO.setEstimatedEndDateSCH06(vo.getEndDateSCH07());
    actVO.setPrioritySCH06(ApplicationConsts.PRIORITY_NORMAL);
    actVO.setStartDateSCH06(vo.getStartDateSCH07());
    actVO.setActivityTypeSCH06(vo.getActivityTypeSCH06());
    actVO.setActivityStateSCH06(ApplicationConsts.OPENED);
    Response res = ClientUtils.getData("insertScheduledActivity",actVO);
    if (res.isError())
      return res;
    actVO = (ScheduledActivityVO)((VOResponse)res).getVo();
    ScheduledEmployeeVO schEmpVO = new ScheduledEmployeeVO();
    schEmpVO.setCompanyCodeSys01SCH07(actVO.getCompanyCodeSys01SCH06());
    schEmpVO.setProgressiveReg04SCH07(vo.getProgressiveReg04SCH07());
    schEmpVO.setDurationSCH07(vo.getDurationSCH07());
    schEmpVO.setStartDateSCH07(actVO.getStartDateSCH06());
    schEmpVO.setEndDateSCH07(vo.getEndDateSCH07());
    schEmpVO.setProgressiveSch06SCH07(actVO.getProgressiveSCH06());
    ArrayList list = new ArrayList();
    list.add(schEmpVO);
    res = ClientUtils.getData("insertScheduledEmployees",list);
    if (res.isError())
      return res;
    vo.setProgressiveSCH06(actVO.getProgressiveSCH06());

    return res;
  }


  /**
   * Check if start/end times are correct.
   */
  private boolean checkTimes(EmployeeActivityVO vo) {
    if (vo.getStartDateSCH07().getTime()>vo.getEndDateSCH07().getTime())
      return false;
    return true;
  }


  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    EmployeeActivityVO vo = null;
    ScheduledActivityPK pk = null;
    ArrayList rows = new ArrayList();
    for(int i=0;i<persistentObjects.size();i++) {
      vo = (EmployeeActivityVO)persistentObjects.get(i);
      pk = new ScheduledActivityPK(
        vo.getCompanyCodeSys01SCH06(),
        vo.getProgressiveSCH06()
      );
      rows.add(pk);
    }

    return ClientUtils.getData("deleteScheduledActivities",rows);
  }


  /**
   * Callback method invoked each time a cell is edited: this method define if the new value if valid.
   * This method is invoked ONLY if:
   * - the edited value is not equals to the old value OR it has exmplicitely called setCellAt or setValueAt
   * - the cell is editable
   * Default behaviour: cell value is valid.
   * @param rowNumber selected row index
   * @param attributeName attribute name related to the column currently selected
   * @param oldValue old cell value (before cell editing)
   * @param newValue new cell value (just edited)
   * @return <code>true</code> if cell value is valid, <code>false</code> otherwise
   */
  public boolean validateCell(int rowNumber,String attributeName,Object oldValue,Object newValue) {
    EmployeeActivityVO vo = (EmployeeActivityVO)grid.getVOListTableModel().getObjectForRow(rowNumber);
    if (attributeName.equals("activityTypeSCH06") && newValue!=null) {
      if (newValue.equals(ApplicationConsts.ACT_ABSENCE)) {
        vo.setDescriptionSCH06(ClientSettings.getInstance().getResources().getResource("absence"));
      }
      else if (newValue.equals(ApplicationConsts.ACT_HOLIDAY)) {
        vo.setDescriptionSCH06(ClientSettings.getInstance().getResources().getResource("holiday"));
      }
      else if (newValue.equals(ApplicationConsts.ACT_ILLNESS)) {
        vo.setDescriptionSCH06(ClientSettings.getInstance().getResources().getResource("illness"));
      }
    }

    if ((attributeName.equals("endDateSCH07")) &&
        newValue!=null && vo.getStartDateSCH07()!=null) {
      vo.setDurationSCH07(new BigDecimal(
        (((java.util.Date)newValue).getTime()-vo.getStartDateSCH07().getTime())/1000/60
      ));
    }
    else if ((attributeName.equals("startDateSCH07")) &&
        newValue!=null && vo.getEndDateSCH07()!=null) {
      vo.setDurationSCH07(new BigDecimal(
        (vo.getEndDateSCH07().getTime()-((java.util.Date)newValue).getTime())/1000/60
      ));
    }



    return true;
  }


}
