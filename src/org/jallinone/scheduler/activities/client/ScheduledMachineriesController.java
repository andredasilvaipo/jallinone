package org.jallinone.scheduler.activities.client;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.client.ClientUtils;
import java.math.BigDecimal;
import javax.swing.*;
import org.openswing.swing.client.GridControl;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.scheduler.activities.java.ScheduledMachineriesVO;
import org.jallinone.scheduler.activities.java.ScheduledActivityPK;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the grid controller for call-out machineries grid.</p>
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
public class ScheduledMachineriesController extends GridController {

  /** grid */
  private GridControl grid = null;

  /** grid container */
  private ScheduledResourcesPanel panel = null;


  public ScheduledMachineriesController(GridControl grid,ScheduledResourcesPanel panel) {
    this.grid = grid;
    this.panel = panel;
  }


  /**
   * Callback method invoked when the user has clicked on the insert button
   * @param valueObject empty value object just created: the user can manage it to fill some attribute values
   */
  public void createValueObject(ValueObject valueObject) throws Exception {
    ScheduledMachineriesVO vo = (ScheduledMachineriesVO)valueObject;
    ScheduledActivityPK pk = (ScheduledActivityPK)grid.getOtherGridParams().get(ApplicationConsts.SCHEDULED_ACTIVITY_PK);
    vo.setCompanyCodeSys01SCH09(pk.getCompanyCodeSys01SCH06());
    vo.setProgressiveSch06SCH09(pk.getProgressiveSCH06());
    vo.setStartDateSCH09(panel.getActVO().getStartDateSCH06());
    vo.setEndDateSCH09(panel.getActVO().getEstimatedEndDateSCH06());
    vo.setDurationSCH09(panel.getActVO().getEstimatedDurationSCH06());
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
    ScheduledMachineriesVO vo = (ScheduledMachineriesVO)grid.getVOListTableModel().getObjectForRow(rowNumber);
    if ((attributeName.equals("endDateSCH09")) &&
        newValue!=null && vo.getStartDateSCH09()!=null) {
      vo.setDurationSCH09(new BigDecimal(
        (((java.util.Date)newValue).getTime()-vo.getStartDateSCH09().getTime())/1000/60
      ));
    }
    else if ((attributeName.equals("startDateSCH09")) &&
        newValue!=null && vo.getEndDateSCH09()!=null) {
      vo.setDurationSCH09(new BigDecimal(
        (vo.getEndDateSCH09().getTime()-((java.util.Date)newValue).getTime())/1000/60
      ));
    }
    else if (
        attributeName.equals("durationSCH09") &&
        newValue!=null && vo.getStartDateSCH09()!=null) {
      vo.setEndDateSCH09(new java.sql.Timestamp(
        vo.getStartDateSCH09().getTime()+((BigDecimal)newValue).longValue()*1000*60
      ));
    }
    return true;
 }


  /**
   * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
   * @param rowNumbers row indexes related to the new rows to save
* @param newValueObjects list of new value objects to save
* @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
    return ClientUtils.getData("insertScheduledMachineries",newValueObjects);
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in EDIT mode.
   * @param rowNumbers row indexes related to the changed rows
   * @param oldPersistentObjects old value objects, previous the changes
   * @param persistentObjects value objects relatied to the changed rows
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response updateRecords(int[] rowNumbers,ArrayList oldPersistentObjects,ArrayList persistentObjects) throws Exception {
    ScheduledMachineriesVO oldVO = null;
    ScheduledMachineriesVO newVO = null;
    Response res = null;
    for(int i=0;i<oldPersistentObjects.size();i++) {
      oldVO = (ScheduledMachineriesVO)oldPersistentObjects.get(i);
      newVO = (ScheduledMachineriesVO)persistentObjects.get(i);
      if (oldVO.getStartDateSCH09()==null) {
        res = ClientUtils.getData("insertScheduledMachinery",newVO);
        if (res.isError())
          return res;
        persistentObjects.set(i,((VOResponse)res).getVo());
        oldPersistentObjects.set(i,((VOResponse)res).getVo());
      }
    }
    return ClientUtils.getData("updateScheduledMachineries",new ArrayList[]{oldPersistentObjects,persistentObjects});
  }


  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    return ClientUtils.getData("deleteScheduledMachineries",persistentObjects);
  }




}
