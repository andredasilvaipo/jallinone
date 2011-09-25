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
import org.jallinone.scheduler.callouts.java.CallOutItemVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.scheduler.callouts.java.CallOutPK;
import org.jallinone.scheduler.activities.java.ScheduledItemVO;
import org.jallinone.scheduler.activities.java.ScheduledActivityPK;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the grid controller for call-out items grid</p>
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
public class ScheduledItemsController extends GridController {

  /** grid */
  private GridControl grid = null;

  /** grid container */
  private ScheduledResourcesPanel panel = null;


  public ScheduledItemsController(GridControl grid,ScheduledResourcesPanel panel) {
    this.grid = grid;
    this.panel = panel;
  }


  /**
   * Callback method invoked when the user has clicked on the insert button
   * @param valueObject empty value object just created: the user can manage it to fill some attribute values
   */
  public void createValueObject(ValueObject valueObject) throws Exception {
    ScheduledItemVO vo = (ScheduledItemVO)valueObject;
    ScheduledActivityPK pk = (ScheduledActivityPK)grid.getOtherGridParams().get(ApplicationConsts.SCHEDULED_ACTIVITY_PK);
    vo.setCompanyCodeSys01SCH15(pk.getCompanyCodeSys01SCH06());
    vo.setProgressiveSch06SCH15(pk.getProgressiveSCH06());
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
   * @param rowNumbers row indexes related to the new rows to save
* @param newValueObjects list of new value objects to save
* @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
    return ClientUtils.getData("insertScheduledItems",newValueObjects);
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in EDIT mode.
   * @param rowNumbers row indexes related to the changed rows
   * @param oldPersistentObjects old value objects, previous the changes
   * @param persistentObjects value objects relatied to the changed rows
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response updateRecords(int[] rowNumbers,ArrayList oldPersistentObjects,ArrayList persistentObjects) throws Exception {
    ScheduledItemVO oldVO = null;
    ScheduledItemVO newVO = null;
    Response res = null;
    for(int i=0;i<oldPersistentObjects.size();i++) {
      oldVO = (ScheduledItemVO)oldPersistentObjects.get(i);
      newVO = (ScheduledItemVO)persistentObjects.get(i);
      if (oldVO.getQtySCH15()==null) {
        res = ClientUtils.getData("insertScheduledItem",newVO);
        if (res.isError())
          return res;
        persistentObjects.set(i,((VOResponse)res).getVo());
        oldPersistentObjects.set(i,((VOResponse)res).getVo());
      }
    }
    return ClientUtils.getData("updateScheduledItems",new ArrayList[]{oldPersistentObjects,persistentObjects});
  }



  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    return ClientUtils.getData("deleteScheduledItems",persistentObjects);
  }




}
