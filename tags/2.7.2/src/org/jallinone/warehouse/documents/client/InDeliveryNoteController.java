package org.jallinone.warehouse.documents.client;

import org.openswing.swing.table.client.Grid;
import org.openswing.swing.message.receive.java.*;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.mdi.client.MDIFrame;
import org.jallinone.commons.client.CompanyFormController;
import org.openswing.swing.util.java.Consts;
import org.jallinone.warehouse.documents.java.*;
import org.openswing.swing.form.client.Form;
import org.jallinone.commons.java.ApplicationConsts;
import java.util.Calendar;
import java.math.BigDecimal;
import org.openswing.swing.util.client.ClientSettings;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Detail controller used for in delivery note detail frame.</p>
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
public class InDeliveryNoteController extends CompanyFormController {

  /** delivery note frame */
  private InDeliveryNoteFrame frame = null;

  /** parent frame */
  private InDeliveryNotesFrame parentFrame = null;

  /** delivery note pk */
  private DeliveryNotePK pk = null;


  public InDeliveryNoteController(InDeliveryNotesFrame parentFrame,DeliveryNotePK pk) {
    this.parentFrame = parentFrame;
    this.pk = pk;
    this.frame = new InDeliveryNoteFrame(this);
    MDIFrame.add(frame);

    if (parentFrame!=null) {
      frame.setParentFrame(parentFrame);
      parentFrame.pushFrame(frame);
    }

    if (pk!=null) {
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
      frame.getRowsPanel().getGrid().clearData();
      frame.setButtonsEnabled(false);
      frame.setTitle(ClientSettings.getInstance().getResources().getResource("in delivery note"));
    }
    return ok;
  }


  /**
   * This method must be overridden by the subclass to retrieve data and return the valorized value object.
   * @param valueObjectClass value object class
   * @return a VOResponse object if data loading is successfully completed, or an ErrorResponse object if an error occours
   */
  public Response loadData(Class valueObjectClass) {
    return ClientUtils.getData("loadInDeliveryNote",pk);
  }


  /**
   * Method called by the Form panel to insert new data.
   * @param newValueObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response insertRecord(ValueObject newPersistentObject) throws Exception {
    Response res = ClientUtils.getData("insertInDeliveryNote",newPersistentObject);
    if (!res.isError()) {
      DetailDeliveryNoteVO vo = (DetailDeliveryNoteVO)((VOResponse)res).getVo();
      pk = new DeliveryNotePK(
          vo.getCompanyCodeSys01DOC08(),
          vo.getDocTypeDOC08(),
          vo.getDocYearDOC08(),
          vo.getDocNumberDOC08()
      );

    }
    return res;
  }


  /**
   * Callback method called after saving SUCCESSFULLY data in INSERT mode.
   */
  public void afterInsertData() {
    if (parentFrame!=null) {
      parentFrame.getGrid().reloadCurrentBlockOfData();
    }

    DetailDeliveryNoteVO vo = (DetailDeliveryNoteVO)frame.getHeaderFormPanel().getVOModel().getValueObject();
    frame.getRowsPanel().setParentVO(vo);
    frame.getRowsPanel().getGrid().getOtherGridParams().put(ApplicationConsts.DELIVERY_NOTE_PK,pk);
    frame.getRowsPanel().getGrid().reloadData();
    frame.setButtonsEnabled(true);
  }


  /**
   * Method called by the Form panel to update existing data.
   * @param oldPersistentObject original value object, previous to the changes
   * @param persistentObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response updateRecord(ValueObject oldPersistentObject,ValueObject persistentObject) throws Exception {
    Response res = ClientUtils.getData("updateInDeliveryNote",new ValueObject[]{oldPersistentObject,persistentObject});
    if (!res.isError()) {
      if (parentFrame!=null) {
        parentFrame.getGrid().reloadCurrentBlockOfData();
        DetailDeliveryNoteVO vo = (DetailDeliveryNoteVO)frame.getHeaderFormPanel().getVOModel().getValueObject();
        frame.getRowsPanel().setParentVO(vo);
      }
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
    DetailDeliveryNoteVO vo = (DetailDeliveryNoteVO)persistentObject;

    pks.add(pk);
    Response res = ClientUtils.getData("deleteDeliveryNotes",pks);
    if (!res.isError()) {
      if (parentFrame!=null) {
        parentFrame.getGrid().reloadCurrentBlockOfData();
      }
      frame.getRowsPanel().getGrid().clearData();
    }
    return res;
  }


  /**
   * Callback method called by the Form panel when the Form is set to INSERT mode.
   * The method can pre-set some v.o. attributes, so that some input controls will have a predefined value associated.
   * @param persistentObject new value object
   */
  public void createPersistentObject(ValueObject persistentObject) throws Exception {
    DetailDeliveryNoteVO vo = (DetailDeliveryNoteVO)persistentObject;
    Calendar cal = Calendar.getInstance();
    vo.setDocYearDOC08(new BigDecimal(cal.get(cal.YEAR)));
    vo.setDocDateDOC08(new java.sql.Date(System.currentTimeMillis()));
    vo.setDeliveryDateDOC08(new java.sql.Date(System.currentTimeMillis()));
    vo.setDocTypeDOC08(ApplicationConsts.IN_DELIVERY_NOTE_DOC_TYPE);
    vo.setDocStateDOC08(ApplicationConsts.OPENED);
  }



  /**
   * Callback method called when the data loading is completed.
   * @param error <code>true</code> if an error occours during data loading, <code>false</code> if data loading is successfully completed
   */
  public void loadDataCompleted(boolean error) {
    frame.loadDataCompleted(error,pk);
  }


  public InDeliveryNotesFrame getParentFrame() {
    return parentFrame;
  }


  public DeliveryNotePK getPk() {
    return pk;
  }


  /**
   * Callback method invoked before saving data when the form was in EDIT mode (on pressing save button).
   * @return <code>true</code> allows to go to EDIT mode, <code>false</code> the mode change is interrupted
   */
  public boolean beforeEditData(Form form) {
    if (!super.beforeEditData(form))
      return false;
    DetailDeliveryNoteVO vo = (DetailDeliveryNoteVO)frame.getHeaderFormPanel().getVOModel().getValueObject();
    return !vo.getDocStateDOC08().equals(ApplicationConsts.CLOSED);
  }


  /**
   * Callback method invoked before deleting data when the form was in READONLY mode (on pressing delete button).
   * @return <code>true</code> allows the deleting to continue, <code>false</code> the deleting is interrupted
   */
  public boolean beforeDeleteData(Form form) {
    if (!super.beforeDeleteData(form))
      return false;
    DetailDeliveryNoteVO vo = (DetailDeliveryNoteVO)frame.getHeaderFormPanel().getVOModel().getValueObject();
    return !vo.getDocStateDOC08().equals(ApplicationConsts.CLOSED);
  }



}
