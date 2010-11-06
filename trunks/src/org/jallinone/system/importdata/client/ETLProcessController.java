package org.jallinone.system.importdata.client;

import org.openswing.swing.form.client.FormController;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.ValueObject;
import org.jallinone.system.importdata.java.ETLProcessVO;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.form.client.Form;
import org.openswing.swing.domains.java.Domain;
import org.jallinone.system.importdata.java.ETLProcessFieldVO;
import org.jallinone.system.importdata.java.SelectableFieldVO;
import org.openswing.swing.message.receive.java.ErrorResponse;
import org.openswing.swing.message.receive.java.VOResponse;
import org.openswing.swing.client.GridControl;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the Form controller for an ETL process.</p>
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
public class ETLProcessController extends FormController {

  private GridControl parentGrid = null;
  private ETLProcessFrame frame = null;
  private ETLProcessVO vo = null;


  public ETLProcessController(GridControl parentGrid,ETLProcessVO vo) {
    this.parentGrid = parentGrid;
    this.vo = vo;
    this.frame = new ETLProcessFrame(this);

    if (vo==null)
      frame.getMainPanel().setMode(Consts.INSERT);
    else {
      frame.getMainPanel().setMode(Consts.READONLY);
      frame.getMainPanel().reload();
    }
    MDIFrame.add(frame);
  }


  /**
   * This method must be overridden by the subclass to retrieve data and return the valorized value object.
   * If the method is not overridden, the current version will return a "demo" value object.
   * @param valueObjectClass value object class
   * @return a VOResponse object if data loading is successfully completed, or an ErrorResponse object if an error occours
   */
  public Response loadData(Class valueObjectClass) {
    return ClientUtils.getData("loadETLProcess",vo.getProgressiveSYS23());
  }


  /**
   * Callback method called when the data loading is completed.
   * @param error <code>true</code> if an error occours during data loading, <code>false</code> if data loading is successfully completed
   */
  public void loadDataCompleted(boolean error) {
    if (!error) {
      ETLProcessVO vo = (ETLProcessVO)frame.getMainPanel().getVOModel().getValueObject();
      frame.setControlImportType();
      frame.getButtonExecNow().setEnabled(true);
      frame.getGrid().getOtherGridParams().put(ApplicationConsts.FILTER_VO,vo);
      frame.getGrid().reloadData();
    }
  }


  /**
   * Callback method invoked on pressing EDIT button, after changing to edit mode.
   */
  public void afterEditData(Form form) {
    frame.getGrid().setMode(Consts.EDIT);
    frame.getButtonExecNow().setEnabled(false);
  }


  /**
   * Callback method invoked on pressing INSERT button, after changing to insert mode.
   */
  public void afterInsertData(Form form) {
    frame.getGrid().setMode(Consts.EDIT);
    frame.getButtonExecNow().setEnabled(false);
  }


  /**
   * Method called by the Form panel to insert new data.
   * @param newValueObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response insertRecord(ValueObject newPersistentObject) throws Exception {
    Response res = updateRecord(null,newPersistentObject);
    if (!res.isError()) {
      vo = (ETLProcessVO)((VOResponse)res).getVo();
      frame.getButtonExecNow().setEnabled(true);
    }
    return res;
  }


  /**
   * Method called by the Form panel to update existing data.
   * @param oldPersistentObject original value object, previous to the changes
   * @param persistentObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response updateRecord(ValueObject oldPersistentObject,ValueObject persistentObject) throws Exception {
    ETLProcessVO processVO = (ETLProcessVO)frame.getMainPanel().getVOModel().getValueObject();
    try {
      frame.getGrid().getTable().getGrid().stopCellEditing();
    }
    catch (Exception ex) {
    }

    // check for grid content...
    SelectableFieldVO vo = null;
    boolean atLeastOne = false;
    for(int i=0;i< frame.getGrid().getVOListTableModel().getRowCount();i++) {
      vo = (SelectableFieldVO)frame.getGrid().getVOListTableModel().getObjectForRow(i);
      if (vo.isSelected()) {
        atLeastOne = true;
        if ((processVO.getFileFormatSYS23().equals("XLS") ||
             processVO.getFileFormatSYS23().equals("CSV1") ||
             processVO.getFileFormatSYS23().equals("CSV2")) &&
            vo.getField().getPosSYS24()==null)
          return new ErrorResponse("A mandatory column is empty.");
        if (processVO.getFileFormatSYS23().equals("TXT") &&
            (vo.getField().getStartPosSYS24()==null ||
             vo.getField().getEndPosSYS24()==null))
          return new ErrorResponse("A mandatory column is empty.");
      }
    }
    if (!atLeastOne)
      return new ErrorResponse("you must select at least one column");

    Response res = ClientUtils.getData("insertETLProcess",new Object[]{processVO,frame.getGrid().getVOListTableModel().getDataVector()});
    if (!res.isError()) {
      frame.getGrid().setMode(Consts.READONLY);
      frame.getButtonExecNow().setEnabled(true);
      ETLProcessController.this.vo = (ETLProcessVO)((VOResponse)res).getVo();
      parentGrid.reloadCurrentBlockOfData();
    }
    return res;
  }

}
