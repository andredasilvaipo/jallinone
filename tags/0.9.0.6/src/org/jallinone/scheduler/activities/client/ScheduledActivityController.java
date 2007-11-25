package org.jallinone.scheduler.activities.client;

import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.form.client.FormController;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.scheduler.activities.java.*;
import org.jallinone.commons.client.CompanyFormController;
import org.openswing.swing.form.client.Form;
import javax.swing.JOptionPane;
import org.jallinone.commons.client.ClientApplet;
import org.jallinone.commons.client.ApplicationClientFacade;
import org.openswing.swing.util.client.ClientSettings;
import org.jallinone.commons.java.ApplicationConsts;
import java.util.ArrayList;
import java.util.Calendar;
import java.math.BigDecimal;
import org.openswing.swing.client.CodLookupControl;
import org.openswing.swing.client.ComboBoxControl;
import org.openswing.swing.client.TextControl;
import org.openswing.swing.form.model.client.VOModel;
import org.openswing.swing.client.GridControl;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Form Controller used for scheduled activity detail frame.</p>
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
public class ScheduledActivityController extends CompanyFormController {

  /** pk */
  private ScheduledActivityPK pk = null;

  /** detail frame */
  private ScheduledActivityDetailFrame detailFrame = null;

  /** parent frame */
  private ScheduledActivitiesGridFrame gridFrame = null;

  private GridControl grid = null;


  public ScheduledActivityController(GridControl grid,ScheduledActivitiesGridFrame gridFrame,ScheduledActivityPK pk,boolean crm) {
    this.grid = grid;
    this.gridFrame = gridFrame;
    this.pk = pk;

    detailFrame = new ScheduledActivityDetailFrame(this,gridFrame,crm);
    MDIFrame.add(detailFrame);

    if (gridFrame!=null) {
      detailFrame.setParentFrame(gridFrame);
      gridFrame.pushFrame(detailFrame);
    }

    if (pk!=null) {
      detailFrame.getMainForm().setMode(Consts.READONLY);
//      detailFrame.getWarehouseForm().reload();
      detailFrame.getMainForm().getForm().executeReload();
    }
    else {
      detailFrame.getMainForm().insert();
    }
  }


  /**
   * This method must be overridden by the subclass to retrieve data and return the valorized value object.
   * @param valueObjectClass value object class
   * @return a VOResponse object if data loading is successfully completed, or an ErrorResponse object if an error occours
   */
  public Response loadData(Class valueObjectClass) {
    return ClientUtils.getData("loadScheduledActivity",pk);
  }


  /**
   * Method called by the Form panel to insert new data.
   * @param newValueObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response insertRecord(ValueObject newPersistentObject) throws Exception {
    ScheduledActivityVO vo = (ScheduledActivityVO)newPersistentObject;
    vo.setEmailAddressSCH06((String)detailFrame.getMainForm().getControlEmail().getValue());
    vo.setFaxNumberSCH06((String)detailFrame.getMainForm().getControlFax().getValue());
    vo.setPhoneNumberSCH06((String)detailFrame.getMainForm().getControlPhone().getValue());

    Response response = ClientUtils.getData("insertScheduledActivity",vo);
    if (!response.isError()) {
      vo = (ScheduledActivityVO)((VOResponse)response).getVo();
      detailFrame.getResPanel().setActVO(vo);
      if (grid!=null)
        grid.reloadData();

    }
    return response;
  }


  /**
   * Method called by the Form panel to update existing data.
   * @param oldPersistentObject original value object, previous to the changes
   * @param persistentObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response updateRecord(ValueObject oldPersistentObject,ValueObject persistentObject) throws Exception {
    ScheduledActivityVO vo = (ScheduledActivityVO)persistentObject;
    vo.setEmailAddressSCH06((String)detailFrame.getMainForm().getControlEmail().getValue());
    vo.setFaxNumberSCH06((String)detailFrame.getMainForm().getControlFax().getValue());
    vo.setPhoneNumberSCH06((String)detailFrame.getMainForm().getControlPhone().getValue());

    Response response = ClientUtils.getData("updateScheduledActivity",new ValueObject[]{oldPersistentObject,vo});
    if (!response.isError()) {
      if (grid!=null)
        grid.reloadData();
    }
    return response;
  }


  /**
   * Method called by the Form panel to delete existing data.
   * @param persistentObject value object to delete
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecord(ValueObject persistentObject) throws Exception {
    ScheduledActivityVO vo = (ScheduledActivityVO)persistentObject;
    ArrayList pks = new ArrayList();
    ScheduledActivityPK pk = new ScheduledActivityPK(
      vo.getCompanyCodeSys01SCH06(),
      vo.getProgressiveSCH06()
    );
    pks.add(pk);
    Response response = ClientUtils.getData("deleteScheduledActivities",pks);
    if (!response.isError()) {
      detailFrame.getTabbedPane().setEnabledAt(1,false);
      detailFrame.getResPanel().getItemsGrid().clearData();
      detailFrame.getResPanel().getMacsGrid().clearData();
      detailFrame.getResPanel().getDocsGrid().clearData();
      detailFrame.getResPanel().getTasksGrid().clearData();

      if (grid!=null)
        grid.reloadData();

    }

    return response;
  }


  /**
   * Callback method called when the data loading is completed.
   * @param error <code>true</code> if an error occours during data loading, <code>false</code> if data loading is successfully completed
   */
  public void loadDataCompleted(boolean error) {
    ScheduledActivityVO vo = (ScheduledActivityVO)detailFrame.getMainForm().getVOModel().getValueObject();

    detailFrame.getMainForm().getControlEmail().setValue(vo.getEmailAddressSCH06());
    detailFrame.getMainForm().getControlFax().setValue(vo.getFaxNumberSCH06());
    detailFrame.getMainForm().getControlPhone().setValue(vo.getPhoneNumberSCH06());

    detailFrame.getResPanel().setActVO(vo);
    ScheduledActivityPK pk = new ScheduledActivityPK(
      vo.getCompanyCodeSys01SCH06(),
      vo.getProgressiveSCH06()
    );
    detailFrame.getTabbedPane().setEnabledAt(1,true);
    detailFrame.getResPanel().getItemsGrid().getOtherGridParams().put(ApplicationConsts.SCHEDULED_ACTIVITY_PK,pk);
    detailFrame.getResPanel().getItemsGrid().reloadData();
    detailFrame.getResPanel().getMacsGrid().getOtherGridParams().put(ApplicationConsts.SCHEDULED_ACTIVITY_PK,pk);
    detailFrame.getResPanel().getMacsGrid().reloadData();
    detailFrame.getResPanel().getDocsGrid().getOtherGridParams().put(ApplicationConsts.SCHEDULED_ACTIVITY_PK,pk);
    detailFrame.getResPanel().getDocsGrid().reloadData();
    detailFrame.getResPanel().getTasksGrid().getOtherGridParams().put(ApplicationConsts.SCHEDULED_ACTIVITY_PK,pk);
    detailFrame.getResPanel().getTasksGrid().reloadData();

    if (!vo.getActivityStateSCH06().equals(ApplicationConsts.CLOSED) &&
        !vo.getActivityStateSCH06().equals(ApplicationConsts.INVOICED)) {
      detailFrame.getMainForm().getConfirmButton().setEnabled(true);
    }
  }


  /**
   * Callback method called when reloading data is SUCCESSFULLY completed.
   */
  public void afterReloadData() {
  }


  /**
   * Callback method called when inserting data is SUCCESSFULLY completed.
   */
  public void afterInsertData() {
    detailFrame.getTabbedPane().setEnabledAt(1,true);

    ScheduledActivityVO vo = (ScheduledActivityVO)detailFrame.getMainForm().getVOModel().getValueObject();
    ScheduledActivityPK pk = new ScheduledActivityPK(
      vo.getCompanyCodeSys01SCH06(),
      vo.getProgressiveSCH06()
    );

    detailFrame.getResPanel().getItemsGrid().getOtherGridParams().put(ApplicationConsts.SCHEDULED_ACTIVITY_PK,pk);
    detailFrame.getResPanel().getMacsGrid().getOtherGridParams().put(ApplicationConsts.SCHEDULED_ACTIVITY_PK,pk);
    detailFrame.getResPanel().getDocsGrid().getOtherGridParams().put(ApplicationConsts.SCHEDULED_ACTIVITY_PK,pk);
    detailFrame.getResPanel().getTasksGrid().getOtherGridParams().put(ApplicationConsts.SCHEDULED_ACTIVITY_PK,pk);
    detailFrame.getResPanel().getItemsGrid().reloadData();
    detailFrame.getResPanel().getMacsGrid().reloadData();
    detailFrame.getResPanel().getDocsGrid().reloadData();
    detailFrame.getResPanel().getTasksGrid().reloadData();

    detailFrame.getMainForm().getConfirmButton().setEnabled(true);
  }


  /**
   * Callback method called by the Form panel when the Form is set to INSERT mode.
   * The method can pre-set some v.o. attributes, so that some input controls will have a predefined value associated.
   * @param persistentObject new value object
   */
  public void createPersistentObject(ValueObject PersistentObject) throws Exception {
    ScheduledActivityVO vo = (ScheduledActivityVO)detailFrame.getMainForm().getVOModel().getValueObject();
    detailFrame.getMainForm().getControlCompaniesControl().getComboBox().setSelectedIndex(0);
    vo.setCompanyCodeSys01SCH06((String)detailFrame.getMainForm().getControlCompaniesControl().getValue());
    vo.setPrioritySCH06(ApplicationConsts.PRIORITY_NORMAL);
    vo.setCompletionPercSCH06(new BigDecimal(0));
    Calendar cal = Calendar.getInstance();
    vo.setEstimatedDurationSCH06(new BigDecimal(60));
    vo.setEstimatedEndDateSCH06(new java.sql.Timestamp(cal.getTimeInMillis()+3600000L));
    vo.setStartDateSCH06(new java.sql.Timestamp(cal.getTimeInMillis()));
    vo.setActivityStateSCH06(ApplicationConsts.OPENED);
    vo.setActivityTypeSCH06(ApplicationConsts.ACT_APPOINTMENT);
  }


  public ScheduledActivityDetailFrame getDetailFrame() {
    return detailFrame;
  }


  /**
   * Callback method called when the Form mode is changed.
   * @param currentMode current Form mode
   */
  public void modeChanged(int currentMode) {
    if (currentMode==Consts.READONLY) {
      detailFrame.getMainForm().getControlEmail().setEnabled(false);
      detailFrame.getMainForm().getControlFax().setEnabled(false);
      detailFrame.getMainForm().getControlPhone().setEnabled(false);
    }
    else {
      detailFrame.getMainForm().getControlEmail().setEnabled(true);
      detailFrame.getMainForm().getControlFax().setEnabled(true);
      detailFrame.getMainForm().getControlPhone().setEnabled(true);
    }
  }


  public TextControl getControlName_1Subject() {
    return detailFrame.getControlName_1Subject();
  }
  public TextControl getControlName_2Subject() {
    return detailFrame.getControlName_2Subject();
  }
  public ComboBoxControl getControlSubjectType() {
    return detailFrame.getControlSubjectType();
  }


  public VOModel getVOModel() {
    return detailFrame.getMainForm().getVOModel();
  }



}
