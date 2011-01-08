package org.jallinone.scheduler.callouts.client;

import org.jallinone.scheduler.activities.client.ScheduledActivityController;
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
import org.jallinone.scheduler.activities.client.ScheduledActivityPanel;
import org.jallinone.scheduler.callouts.java.DetailCallOutRequestVO;
import java.math.BigDecimal;
import java.util.Calendar;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Form Controller used for scheduled activity detail frame,
 * related to a call-out.</p>
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
public class CallOutActivityController extends CompanyFormController {

  private CallOutRequestFrame frame = null;


  public CallOutActivityController(CallOutRequestFrame frame) {
    this.frame = frame;
  }


  /**
   * This method must be overridden by the subclass to retrieve data and return the valorized value object.
   * @param valueObjectClass value object class
   * @return a VOResponse object if data loading is successfully completed, or an ErrorResponse object if an error occours
   */
  public Response loadData(Class valueObjectClass) {
    DetailCallOutRequestVO vo = (DetailCallOutRequestVO)frame.getCalloutPanel().getVOModel().getValueObject();
    return ClientUtils.getData("loadScheduledActivity",new ScheduledActivityPK(vo.getCompanyCodeSys01SCH03(),vo.getProgressiveSch06SCH03()));
  }


  /**
   * Method called by the Form panel to insert new data.
   * @param newValueObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response insertRecord(ValueObject newPersistentObject) throws Exception {
    DetailCallOutRequestVO vo = (DetailCallOutRequestVO)frame.getCalloutPanel().getVOModel().getValueObject();
    vo.setScheduledActivityVO((ScheduledActivityVO)newPersistentObject);
		vo.getScheduledActivityVO().setCompanyCodeSys01SCH06(vo.getCompanyCodeSys01SCH03());
    Response response = ClientUtils.getData("linkScheduledActivityToCallOutRequest",vo);
    if (!response.isError()) {
      vo = (DetailCallOutRequestVO)((VOResponse)response).getVo();
      response = new VOResponse(vo.getScheduledActivityVO());

      frame.getResourcesPanel().setActVO(vo.getScheduledActivityVO());
      frame.getCalloutPanel().getVOModel().setValueObject(vo);
      frame.getConfirmButton().setEnabled(false);
      frame.getInvoiceButton().setEnabled(false);
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
    Response response = ClientUtils.getData("updateScheduledActivity",new ValueObject[]{oldPersistentObject,persistentObject});
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
      frame.getTab().setEnabledAt(3,false);
      frame.getResourcesPanel().getItemsGrid().clearData();
      frame.getResourcesPanel().getMacsGrid().clearData();
      frame.getResourcesPanel().getDocsGrid().clearData();
      frame.getResourcesPanel().getTasksGrid().clearData();

      DetailCallOutRequestVO detVO = (DetailCallOutRequestVO)frame.getCalloutPanel().getVOModel().getValueObject();
      detVO.setProgressiveSch06SCH03(null);
      frame.getConfirmButton().setEnabled(true);
      frame.getActPanel().getConfirmButton().setEnabled(false);
      frame.getInvoiceButton().setEnabled(false);
    }

    return response;
  }


  /**
   * Callback method called when the data loading is completed.
   * @param error <code>true</code> if an error occours during data loading, <code>false</code> if data loading is successfully completed
   */
  public void loadDataCompleted(boolean error) {
    ScheduledActivityVO vo = (ScheduledActivityVO)frame.getActPanel().getVOModel().getValueObject();
    frame.getResourcesPanel().setActVO(vo);
    ScheduledActivityPK pk = new ScheduledActivityPK(
      vo.getCompanyCodeSys01SCH06(),
      vo.getProgressiveSCH06()
    );
    frame.getTab().setEnabledAt(3,true);
    frame.getResourcesPanel().getItemsGrid().getOtherGridParams().put(ApplicationConsts.SCHEDULED_ACTIVITY_PK,pk);
    frame.getResourcesPanel().getItemsGrid().reloadData();
    frame.getResourcesPanel().getMacsGrid().getOtherGridParams().put(ApplicationConsts.SCHEDULED_ACTIVITY_PK,pk);
    frame.getResourcesPanel().getMacsGrid().reloadData();
    frame.getResourcesPanel().getDocsGrid().getOtherGridParams().put(ApplicationConsts.SCHEDULED_ACTIVITY_PK,pk);
    frame.getResourcesPanel().getDocsGrid().reloadData();
    frame.getResourcesPanel().getTasksGrid().getOtherGridParams().put(ApplicationConsts.SCHEDULED_ACTIVITY_PK,pk);
    frame.getResourcesPanel().getTasksGrid().reloadData();

		if (vo!=null) {
			if (vo.getActivityStateSCH06()!=null &&
					!vo.getActivityStateSCH06().equals(ApplicationConsts.CLOSED) &&
					!vo.getActivityStateSCH06().equals(ApplicationConsts.INVOICED)) {
				frame.getActPanel().getConfirmButton().setEnabled(true);
				frame.getInvoiceButton().setEnabled(false);
			}
			else if (vo.getActivityStateSCH06()!=null && vo.getActivityStateSCH06().equals(ApplicationConsts.CLOSED))
				frame.getInvoiceButton().setEnabled(true);
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
    frame.getTab().setEnabledAt(3,true);

    ScheduledActivityVO vo = (ScheduledActivityVO)frame.getActPanel().getVOModel().getValueObject();
    ScheduledActivityPK pk = new ScheduledActivityPK(
      vo.getCompanyCodeSys01SCH06(),
      vo.getProgressiveSCH06()
    );

    frame.getResourcesPanel().getItemsGrid().getOtherGridParams().put(ApplicationConsts.SCHEDULED_ACTIVITY_PK,pk);
    frame.getResourcesPanel().getMacsGrid().getOtherGridParams().put(ApplicationConsts.SCHEDULED_ACTIVITY_PK,pk);
    frame.getResourcesPanel().getDocsGrid().getOtherGridParams().put(ApplicationConsts.SCHEDULED_ACTIVITY_PK,pk);
    frame.getResourcesPanel().getTasksGrid().getOtherGridParams().put(ApplicationConsts.SCHEDULED_ACTIVITY_PK,pk);
    frame.getResourcesPanel().getItemsGrid().reloadData();
    frame.getResourcesPanel().getMacsGrid().reloadData();
    frame.getResourcesPanel().getDocsGrid().reloadData();
    frame.getResourcesPanel().getTasksGrid().reloadData();

    frame.getActPanel().getConfirmButton().setEnabled(true);
    frame.getInvoiceButton().setEnabled(false);
  }


  /**
   * Callback method called by the Form panel when the Form is set to INSERT mode.
   * The method can pre-set some v.o. attributes, so that some input controls will have a predefined value associated.
   * @param persistentObject new value object
   */
  public void createPersistentObject(ValueObject PersistentObject) throws Exception {
    DetailCallOutRequestVO vo = (DetailCallOutRequestVO)frame.getCalloutPanel().getVOModel().getValueObject();
    ScheduledActivityVO actVO = (ScheduledActivityVO)frame.getActPanel().getVOModel().getValueObject();
    actVO.setCompanyCodeSys01SCH06(vo.getCompanyCodeSys01SCH03());
    actVO.setPrioritySCH06(ApplicationConsts.PRIORITY_NORMAL);
    actVO.setCompletionPercSCH06(new BigDecimal(0));
    Calendar cal = Calendar.getInstance();
    actVO.setEstimatedDurationSCH06(new BigDecimal(60));
    actVO.setEstimatedEndDateSCH06(new java.sql.Timestamp(cal.getTimeInMillis()+3600000L));
    actVO.setProgressiveReg04SubjectSCH06(vo.getProgressiveReg04SCH03());
    actVO.setStartDateSCH06(new java.sql.Timestamp(cal.getTimeInMillis()));
    actVO.setActivityStateSCH06(ApplicationConsts.OPENED);
    actVO.setActivityTypeSCH06(ApplicationConsts.ACT_CALL_OUT);
    actVO.setDescriptionSCH06(vo.getDescriptionSCH03());

  }



}
