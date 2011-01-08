package org.jallinone.scheduler.callouts.client;

import org.openswing.swing.table.client.Grid;
import org.openswing.swing.message.receive.java.*;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.mdi.client.MDIFrame;
import org.jallinone.commons.client.CompanyFormController;
import org.openswing.swing.util.java.Consts;
import org.jallinone.scheduler.callouts.java.*;
import org.openswing.swing.form.client.Form;
import org.jallinone.commons.java.ApplicationConsts;
import java.math.BigDecimal;
import org.jallinone.commons.client.ClientApplet;
import org.jallinone.commons.client.ApplicationClientFacade;
import org.jallinone.subjects.java.Subject;
import org.jallinone.scheduler.activities.java.ScheduledActivityVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Detail controller used for call-out request form.</p>
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
public class CallOutRequestController extends CompanyFormController {

  /** call-out request frame */
  private CallOutRequestFrame frame = null;

  /** parent frame */
  private CallOutRequestsFrame parentFrame = null;

  /** call-out request pk */
  private CallOutRequestPK pk = null;


  public CallOutRequestController(CallOutRequestsFrame parentFrame,CallOutRequestPK pk) {
    this.parentFrame = parentFrame;
    this.pk = pk;
    this.frame = new CallOutRequestFrame(this,parentFrame);
    MDIFrame.add(frame);

    if (parentFrame!=null) {
      frame.setParentFrame(parentFrame);
      parentFrame.pushFrame(frame);
    }

    if (pk!=null) {
      frame.getCalloutPanel().setMode(Consts.READONLY);
      frame.getCalloutPanel().executeReload();
    }
    else {
      frame.getCalloutPanel().insert();
      frame.getControlCompaniesCombo().getComboBox().setSelectedIndex(0);
      for(int i=1;i<frame.getTab().getTabCount();i++)
        frame.getTab().setEnabledAt(i,false);
      frame.getControlSubjectType().getComboBox().setSelectedIndex(0);
      frame.getSubjectForm().insert();
    }
  }


  /**
   * This method must be overridden by the subclass to retrieve data and return the valorized value object.
   * @param valueObjectClass value object class
   * @return a VOResponse object if data loading is successfully completed, or an ErrorResponse object if an error occours
   */
  public Response loadData(Class valueObjectClass) {
    return ClientUtils.getData("loadCallOutRequest",pk);
  }


  /**
   * Method called by the Form panel to insert new data.
   * @param newValueObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response insertRecord(ValueObject newPersistentObject) throws Exception {
    DetailCallOutRequestVO vo = (DetailCallOutRequestVO)newPersistentObject;
    Subject subVO = (Subject)frame.getSubjectForm().getVOModel().getValueObject();
    vo.setCompanyCodeSys01SCH03(subVO.getCompanyCodeSys01REG04());
    vo.setProgressiveReg04SCH03(subVO.getProgressiveREG04());
//    vo.setSubjectTypeReg04SCH03(subVO.getSubjectTypeREG04());
    Response res = ClientUtils.getData("insertCallOutRequest",newPersistentObject);
    if (!res.isError()) {
      vo = (DetailCallOutRequestVO)((VOResponse)res).getVo();
      pk = new CallOutRequestPK(vo.getCompanyCodeSys01SCH03(),vo.getRequestYearSCH03(),vo.getProgressiveSCH03());
      if (parentFrame!=null) {
        parentFrame.getGrid().reloadData();
      }

//      frame.getTasksGrid().getOtherGridParams().put(
//          ApplicationConsts.CALL_OUT_REQUEST_PK,
//          pk
//      );
//      frame.getMacsGrid().getOtherGridParams().put(
//          ApplicationConsts.CALL_OUT_REQUEST_PK,
//          pk
//      );
//      frame.getItemsGrid().getOtherGridParams().put(
//          ApplicationConsts.CALL_OUT_REQUEST_PK,
//          pk
//      );

      frame.setButtonsEnabled(true);
      frame.getTab().setEnabledAt(2,true);
      frame.getActPanel().insert();
      frame.getActPanel().getControlActType().setValue(ApplicationConsts.ACT_CALL_OUT);
      frame.getActPanel().getControlDescr().setValue(vo.getDescriptionSCH03());
      ScheduledActivityVO actVO = (ScheduledActivityVO)frame.getActPanel().getVOModel().getValueObject();
      actVO.setCompanyCodeSys01SCH06(vo.getCompanyCodeSys01SCH03());

      frame.getConfirmButton().setEnabled(true);
      frame.getInvoiceButton().setEnabled(false);
      frame.getViewInvoiceButton().setEnabled(false);
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
    Response res = ClientUtils.getData("updateCallOutRequest",new ValueObject[]{oldPersistentObject,persistentObject});
    if (!res.isError()) {
      if (parentFrame!=null) {
        parentFrame.getGrid().reloadData();
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
    DetailCallOutRequestVO vo = (DetailCallOutRequestVO)persistentObject;
    CallOutRequestPK pk = new CallOutRequestPK(vo.getCompanyCodeSys01SCH03(),vo.getRequestYearSCH03(),vo.getProgressiveSCH03());
    pks.add(pk);
    Response res = ClientUtils.getData("deleteCallOutRequests",pks);
    if (!res.isError()) {
      if (parentFrame!=null) {
        parentFrame.getGrid().reloadData();
      }
//      frame.getTasksGrid().clearData();
//      frame.getMacsGrid().clearData();
//      frame.getItemsGrid().clearData();

      frame.setButtonsEnabled(false);
    }
    return res;
  }


  /**
   * Callback method called by the Form panel when the Form is set to INSERT mode.
   * The method can pre-set some v.o. attributes, so that some input controls will have a predefined value associated.
   * @param persistentObject new value object
   */
  public void createPersistentObject(ValueObject persistentObject) throws Exception {
    DetailCallOutRequestVO vo = (DetailCallOutRequestVO)persistentObject;
    ClientApplet applet = ( (ApplicationClientFacade) MDIFrame.getInstance().getClientFacade()).getMainClass();

    vo.setSubjectTypeReg04SCH03((String)frame.getControlSubjectType().getValue());
    vo.setRequestDateSCH03(new java.sql.Timestamp(System.currentTimeMillis()));
    vo.setCallOutStateSCH03(ApplicationConsts.OPENED);
    vo.setPrioritySCH03(ApplicationConsts.PRIORITY_NORMAL);
    vo.setRequestYearSCH03(new BigDecimal(vo.getRequestDateSCH03().getYear()+1900));
    vo.setUsernameSys03SCH03(applet.getUsername());
		if (frame.getControlCallOutType().getComboBox().getModel().getSize()>0)
				frame.getControlCallOutType().getComboBox().setSelectedIndex(0);
    vo.setProgressiveHie02SCH10((BigDecimal)frame.getControlCallOutType().getValue());
  }


  /**
   * Callback method called when the Form mode is changed.
   * @param currentMode current Form mode
   */
  public void modeChanged(int currentMode) {
    if (currentMode==Consts.INSERT) {
      frame.getConfirmButton().setEnabled(false);
      frame.getInvoiceButton().setEnabled(false);
      frame.getViewInvoiceButton().setEnabled(false);
    }
  }



  /**
   * Callback method called when the data loading is completed.
   * @param error <code>true</code> if an error occours during data loading, <code>false</code> if data loading is successfully completed
   */
  public void loadDataCompleted(boolean error) {
    frame.loadDataCompleted(error,pk);
  }


}
