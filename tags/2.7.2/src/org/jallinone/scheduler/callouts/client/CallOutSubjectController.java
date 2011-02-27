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
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.subjects.java.OrganizationVO;
import org.jallinone.subjects.java.PeopleVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Detail controller used for call-out request subject form.</p>
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
public class CallOutSubjectController extends CompanyFormController {

  /** call-out request frame */
  private CallOutRequestFrame frame = null;


  public CallOutSubjectController(CallOutRequestFrame frame) {
    this.frame = frame;
  }


  /**
   * Callback method invoked on pressing INSERT button, after changing to insert mode.
   */
  public void afterInsertData(Form form) {
    for(int i=1;i<frame.getTab().getTabCount();i++)
      frame.getTab().setEnabledAt(i,false);
    frame.getControlCompaniesCombo().setEnabled(true);
    frame.getControlSubjectType().setEnabled(true);

    if (frame.getActPanel().getMode()!=Consts.INSERT)
     frame.getActPanel().insert();

   if (frame.getCalloutPanel().getMode()!=Consts.INSERT)
     frame.getCalloutPanel().insert();
  }


  /**
   * This method must be overridden by the subclass to retrieve data and return the valorized value object.
   * @param valueObjectClass value object class
   * @return a VOResponse object if data loading is successfully completed, or an ErrorResponse object if an error occours
   */
  public Response loadData(Class valueObjectClass) {
    DetailCallOutRequestVO vo = (DetailCallOutRequestVO)frame.getCalloutPanel().getVOModel().getValueObject();

    GridParams gridParams = new GridParams();
    gridParams.getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01SCH03());
    gridParams.getOtherGridParams().put(ApplicationConsts.SUBJECT_TYPE,vo.getSubjectTypeReg04SCH03());
    gridParams.getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_REG04,vo.getProgressiveReg04SCH03());
    Response res = ClientUtils.getData("loadSubjectPerName",gridParams);
    if (res.isError())
      return res;
    else if (((VOListResponse)res).getRows().size()==0)
      return new ErrorResponse("reload non allowed");
    else
      return new VOResponse(((VOListResponse)res).getRows().get(0));
  }


  /**
   * Method called by the Form panel to insert new data.
   * @param newValueObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response insertRecord(ValueObject newPersistentObject) throws Exception {
    if (frame.getControlSubjectType().getValue().equals(ApplicationConsts.SUBJECT_ORGANIZATION)) {
      OrganizationVO vo = (OrganizationVO)newPersistentObject;
      vo.setCompanyCodeSys01REG04((String)frame.getControlCompaniesCombo().getValue());
      vo.setSubjectTypeREG04(ApplicationConsts.SUBJECT_ORGANIZATION_CONTACT);
    }
    else {
      PeopleVO vo = (PeopleVO)newPersistentObject;
      vo.setCompanyCodeSys01REG04((String)frame.getControlCompaniesCombo().getValue());
      vo.setSubjectTypeREG04(ApplicationConsts.SUBJECT_PEOPLE_CONTACT);
    }

    Response res = ClientUtils.getData("insertSubject",newPersistentObject);
    if (!res.isError()) {

      frame.getTab().setEnabledAt(1,true);
      frame.getControlCompaniesCombo().setEnabled(false);
      frame.getControlSubjectType().setEnabled(false);


//      DetailCallOutRequestVO vo = (DetailCallOutRequestVO)((VOResponse)res).getVo();
//      pk = new CallOutRequestPK(vo.getCompanyCodeSys01SCH03(),vo.getRequestYearSCH03(),vo.getProgressiveSCH03());
//      if (parentFrame!=null) {
//        parentFrame.getGrid().reloadData();
//      }

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
    Response res = ClientUtils.getData("updateSubject",new ValueObject[]{oldPersistentObject,persistentObject});
//    if (!res.isError()) {
//      if (parentFrame!=null) {
//        parentFrame.getGrid().reloadData();
//      }
//    }
    return res;
  }


  /**
   * Callback method called when the data loading is completed.
   * @param error <code>true</code> if an error occours during data loading, <code>false</code> if data loading is successfully completed
   */
  public void loadDataCompleted(boolean error) {
    DetailCallOutRequestVO vo = (DetailCallOutRequestVO)frame.getCalloutPanel().getVOModel().getValueObject();

    if (vo.getCallOutStateSCH03().equals(ApplicationConsts.CLOSED) ||
        vo.getCallOutStateSCH03().equals(ApplicationConsts.INVOICED)) {
      if (frame.getPeoplePanel().getEditButton()!=null)
        frame.getPeoplePanel().getEditButton().setEnabled(false);
      if (frame.getPeoplePanel().getReloadButton()!=null)
          frame.getPeoplePanel().getReloadButton().setEnabled(false);
      if (frame.getPeoplePanel().getDeleteButton()!=null)
        frame.getPeoplePanel().getDeleteButton().setEnabled(false);
      if (frame.getOrganizationPanel().getEditButton()!=null)
        frame.getOrganizationPanel().getEditButton().setEnabled(false);
      if (frame.getOrganizationPanel().getReloadButton()!=null)
        frame.getOrganizationPanel().getReloadButton().setEnabled(false);
      if (frame.getOrganizationPanel().getDeleteButton()!=null)
        frame.getOrganizationPanel().getDeleteButton().setEnabled(false);

    }

  }


}
