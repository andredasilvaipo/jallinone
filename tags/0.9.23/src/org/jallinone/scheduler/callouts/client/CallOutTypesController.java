package org.jallinone.scheduler.callouts.client;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.scheduler.callouts.java.*;
import java.math.BigDecimal;
import javax.swing.*;
import org.openswing.swing.util.java.Consts;
import org.jallinone.commons.client.CompanyGridController;
import org.openswing.swing.table.client.Grid;
import org.openswing.swing.client.GridControl;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the grid controller for call-out types grid frame.</p>
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
public class CallOutTypesController extends CompanyGridController {

  /** grid frame */
  private CallOutTypesFrame frame = null;


  public CallOutTypesController() {
    frame = new CallOutTypesFrame(this);
    MDIFrame.add(frame);
  }


  /**
   * Callback method invoked before saving data when the grid was in INSERT mode (on pressing save button).
   * @return <code>true</code> allows the saving to continue, <code>false</code> the saving is interrupted
   */
  public boolean beforeInsertGrid(GridControl grid) {
    boolean ok = super.beforeInsertGrid(grid);
    if (ok) {
      frame.getHierarTreePanel().setEnabled(false);
      frame.getHierarTreePanel().clearTree();
    }
    return ok;
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
   * @param rowNumbers row indexes related to the new rows to save
* @param newValueObjects list of new value objects to save
* @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
    ValueObject newValueObject = (ValueObject)newValueObjects.get(0);
    Response res = ClientUtils.getData("insertCallOutType",newValueObject);
    if (!res.isError()) {
      frame.getHierarTreePanel().setEnabled(true);
      frame.getHierarTreePanel().setProgressiveHIE02(((CallOutTypeVO)((VOResponse)res).getVo()).getProgressiveHie02SCH11());
      frame.getHierarTreePanel().setCompanyCode( ((CallOutTypeVO)((VOResponse)res).getVo()).getCompanyCodeSys01SCH11() );
      frame.getHierarTreePanel().reloadTree();

      ArrayList list = new ArrayList();
      list.add(((VOResponse)res).getVo());
      return new VOListResponse(list,false,list.size());
    }
    return res;
  }


  /**
   * Callback method invoked on pressing EDIT button.
   * @return <code>true</code> allows to go to EDIT mode, <code>false</code> the mode change is interrupted
   */
  public boolean beforeEditGrid(GridControl grid) {
    frame.getHierarTreePanel().setEnabled(false);
    return true;
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in EDIT mode.
   * @param rowNumbers row indexes related to the changed rows
   * @param oldPersistentObjects old value objects, previous the changes
   * @param persistentObjects value objects relatied to the changed rows
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response updateRecords(int[] rowNumbers,ArrayList oldPersistentObjects,ArrayList persistentObjects) throws Exception {
    Response res = ClientUtils.getData("updateCallOutTypes",new ArrayList[]{oldPersistentObjects,persistentObjects});
    if (!res.isError()) {
      doubleClick(0,frame.getGrid().getVOListTableModel().getObjectForRow(0));
      frame.getHierarTreePanel().setEnabled(true);
    }
    return res;
  }


  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    Response res = ClientUtils.getData("deleteCallOutType",persistentObjects.get(0));
    if (!res.isError()) {
      frame.getHierarTreePanel().setEnabled(false);
      frame.getHierarTreePanel().clearTree();
    }
    return res;
  }


  /**
   * Callback method invoked when the data loading is completed.
   * @param error <code>true</code> if data loading has terminated with errors, <code>false</code> otherwise
   */
  public void loadDataCompleted(boolean error) {
    if (frame.getGrid().getVOListTableModel().getRowCount()>0)
      doubleClick(0,frame.getGrid().getVOListTableModel().getObjectForRow(0));
  }


  /**
   * Callback method invoked when the user has double clicked on the selected row of the grid.
   * @param rowNumber selected row index
   * @param persistentObject v.o. related to the selected row
   */
  public void doubleClick(int rowNumber,ValueObject persistentObject) {
    CallOutTypeVO vo = (CallOutTypeVO)persistentObject;
    frame.getHierarTreePanel().setEnabled(true);
    frame.getHierarTreePanel().setProgressiveHIE02(vo.getProgressiveHie02SCH11());
    frame.getHierarTreePanel().setCompanyCode( vo.getCompanyCodeSys01SCH11() );
    frame.getHierarTreePanel().reloadTree();
  }



}
