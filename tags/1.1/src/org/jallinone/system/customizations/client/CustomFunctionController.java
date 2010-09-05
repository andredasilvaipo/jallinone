package org.jallinone.system.customizations.client;

import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.client.GridControl;
import org.openswing.swing.message.receive.java.*;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.form.client.FormController;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.form.client.Form;
import org.jallinone.system.customizations.java.CustomFunctionVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Grid controller used to manage custom functions.</p>
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
public class CustomFunctionController extends FormController {


  /** sale order frame */
  private CustomFunctionFrame frame = null;

  /** parent frame */
  private CustomFunctionsGridFrame parentFrame = null;

  /** custom function v.o. */
  private CustomFunctionVO vo = null;


  public CustomFunctionController(CustomFunctionsGridFrame parentFrame,CustomFunctionVO vo) {
    this.parentFrame = parentFrame;
    this.vo = vo;
    this.frame = new CustomFunctionFrame(this);
    MDIFrame.add(frame,true);

    if (parentFrame!=null) {
      frame.setParentFrame(parentFrame);
      parentFrame.pushFrame(frame);
    }

    if (vo!=null) {
      frame.getHeaderFormPanel().setMode(Consts.READONLY);
      frame.getHeaderFormPanel().executeReload();
    }
    else {
      frame.getHeaderFormPanel().insert();
    }
  }


  /**
   * Callback method invoked before saving data when the form was in EDIT mode (on pressing save button).
   * @return <code>true</code> allows to go to EDIT mode, <code>false</code> the mode change is interrupted
   */
  public boolean beforeInsertData(Form form) {
    boolean ok = super.beforeInsertData(form);

    if (ok) {
      frame.getGrid().clearData();
      frame.setButtonsEnabled(false);
      frame.clearList();
      frame.getShowQuery().setEnabled(false);
    }
    return ok;
  }


  /**
   * This method must be overridden by the subclass to retrieve data and return the valorized value object.
   * @param valueObjectClass value object class
   * @return a VOResponse object if data loading is successfully completed, or an ErrorResponse object if an error occours
   */
  public Response loadData(Class valueObjectClass) {
    return new VOResponse(vo);
  }


  /**
   * Method called by the Form panel to insert new data.
   * @param newValueObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response insertRecord(ValueObject newPersistentObject) throws Exception {
    vo = (CustomFunctionVO)newPersistentObject;
    String mainTables = "";
    Object[] sel = frame.getTablesList().getSelectedValues();
    if (sel!=null)
      for(int i=0;i<sel.length;i++)
        mainTables += sel[i]+(i<sel.length-1?",":"");
    vo.setMainTablesSYS16(mainTables);
    Response res = ClientUtils.getData("insertCustomFunction",newPersistentObject);
    if (!res.isError()) {
      vo = (CustomFunctionVO)((VOResponse)res).getVo();
      frame.loadDataCompleted(false,vo);
    }
    return res;
  }


  /**
   * Callback method invoked on pressing EDIT button, after changing to edit mode.
   */
  public void afterEditData(Form form) {
    frame.getTablesList().setEnabled(true);
    frame.getShowQuery().setEnabled(false);
  }


  /**
   * Callback method called after saving SUCCESSFULLY data in INSERT mode.
   */
  public void afterInsertData() {
    if (parentFrame!=null) {
      parentFrame.getGrid().reloadData();
    }

    frame.getGrid().getOtherGridParams().put(ApplicationConsts.FUNCTION_CODE_SYS06,vo.getFunctionCodeSys06SYS16());
    frame.getGrid().reloadData();
    frame.getTablesList().setEnabled(false);
    frame.setButtonsEnabled(true);
  }


  /**
   * Callback method called when the Form mode is changed.
   * @param currentMode current Form mode
   */
  public void modeChanged(int currentMode) {
  }


  /**
   * Method called by the Form panel to update existing data.
   * @param oldPersistentObject original value object, previous to the changes
   * @param persistentObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response updateRecord(ValueObject oldPersistentObject,ValueObject persistentObject) throws Exception {
    vo = (CustomFunctionVO)persistentObject;
    String mainTables = "";
    Object[] sel = frame.getTablesList().getSelectedValues();
    if (sel!=null)
      for(int i=0;i<sel.length;i++)
        mainTables += sel[i]+(i<sel.length-1?",":"");
    vo.setMainTablesSYS16(mainTables);

    Response res = ClientUtils.getData("updateCustomFunction",new ValueObject[]{oldPersistentObject,persistentObject});
    if (!res.isError()) {
//      if (parentFrame!=null) {
//        parentFrame.getGrid().reloadData();
//      }
      frame.getGrid().reloadData();
      frame.getTablesList().setEnabled(false);
      frame.getShowQuery().setEnabled(true);
    }
    return res;
  }

  /**
   * Method called by the Form panel to delete existing data.
   * @param persistentObject value object to delete
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecord(ValueObject persistentObject) throws Exception {
    ArrayList pks = new ArrayList();
    pks.add(vo.getFunctionCodeSys06SYS16());
    Response res = ClientUtils.getData("deleteCustomFunctions",pks);
    if (!res.isError()) {
      if (parentFrame!=null) {
        parentFrame.getGrid().reloadData();
      }
      frame.getGrid().clearData();
      frame.clearList();
      frame.getShowQuery().setEnabled(false);
    }
    return res;
  }


  /**
   * Callback method called by the Form panel when the Form is set to INSERT mode.
   * The method can pre-set some v.o. attributes, so that some input controls will have a predefined value associated.
   * @param persistentObject new value object
   */
  public void createPersistentObject(ValueObject persistentObject) throws Exception {
    CustomFunctionVO vo = (CustomFunctionVO)persistentObject;
  }



  /**
   * Callback method called when the data loading is completed.
   * @param error <code>true</code> if an error occours during data loading, <code>false</code> if data loading is successfully completed
   */
  public void loadDataCompleted(boolean error) {
    frame.loadDataCompleted(error,vo);
  }


  public CustomFunctionsGridFrame getParentFrame() {
    return parentFrame;
  }



}
