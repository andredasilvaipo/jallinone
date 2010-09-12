package org.jallinone.sales.customers.client;

import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.form.client.FormController;
import org.openswing.swing.message.receive.java.*;
import org.jallinone.sales.customers.java.*;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.commons.client.CompanyFormController;
import org.openswing.swing.form.client.Form;
import javax.swing.JOptionPane;
import org.jallinone.commons.client.ClientApplet;
import org.jallinone.commons.client.ApplicationClientFacade;
import org.openswing.swing.util.client.ClientSettings;
import org.jallinone.subjects.java.Subject;
import org.jallinone.system.java.ButtonCompanyAuthorizations;
import java.util.ArrayList;
import org.jallinone.subjects.java.SubjectPK;
import org.jallinone.commons.java.ApplicationConsts;
import java.util.HashMap;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Form Controller used for customer detail frame.</p>
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
public class CustomerController extends CompanyFormController {

  /** pk */
  private CustomerPK pk = null;

  /** detail frame */
  private CustomerDetailFrame detailFrame = null;

  /** parent frame */
  private CustomersGridFrame gridFrame = null;


  public CustomerController(CustomersGridFrame gridFrame,CustomerPK pk) {
    this.gridFrame = gridFrame;
    this.pk = pk;

    detailFrame = new CustomerDetailFrame(this);
    MDIFrame.add(detailFrame);

    detailFrame.setParentFrame(gridFrame);
    gridFrame.pushFrame(detailFrame);

    if (pk!=null) {
      detailFrame.subjectChanged(pk.getSubjectTypeREG04());
      detailFrame.getCurrentForm().setMode(Consts.READONLY);
      detailFrame.getCurrentForm().executeReload();
    }
    else {
      detailFrame.getCurrentForm().insert();
    }
  }


  /**
   * This method must be overridden by the subclass to retrieve data and return the valorized value object.
   * @param valueObjectClass value object class
   * @return a VOResponse object if data loading is successfully completed, or an ErrorResponse object if an error occours
   */
  public Response loadData(Class valueObjectClass) {
    // since this method could be invoked also when selecting another row on the linked grid,
    // the pk attribute must be recalculated from the grid...
    int row = gridFrame.getGrid().getSelectedRow();
    if (row!=-1) {
      Subject gridVO = (Subject)gridFrame.getGrid().getVOListTableModel().getObjectForRow(row);
      pk = new CustomerPK(gridVO.getCompanyCodeSys01REG04(),gridVO.getProgressiveREG04(),gridVO.getSubjectTypeREG04());
    }


    detailFrame.subjectChanged( pk.getSubjectTypeREG04() );
    return ClientUtils.getData("loadCustomer",pk);
  }


  /**
   * Callback method called when the data loading is completed.
   * @param error <code>true</code> if an error occours during data loading, <code>false</code> if data loading is successfully completed
   */
  public void loadDataCompleted(boolean error) {
    detailFrame.getReferencesPanel().getGrid().getOtherGridParams().put(
        ApplicationConsts.SUBJECT_PK,
        new SubjectPK(pk.getCompanyCodeSys01SAL07(),pk.getProgressiveReg04SAL07())
    );
    detailFrame.getReferencesPanel().getGrid().reloadData();
    detailFrame.getHierarchiesPanel().getGrid().getOtherGridParams().put(
        ApplicationConsts.SUBJECT_PK,
        new SubjectPK(pk.getCompanyCodeSys01SAL07(),pk.getProgressiveReg04SAL07())
    );
    detailFrame.getHierarchiesPanel().getGrid().getOtherGridParams().put(
        ApplicationConsts.SUBJECT_TYPE,
        ApplicationConsts.SUBJECT_ORGANIZATION_CUSTOMER
    );
    detailFrame.getHierarchiesPanel().getGrid().reloadData();

    detailFrame.getDiscountsGrid().getOtherGridParams().put(
        ApplicationConsts.SUBJECT_PK,
        pk
    );
    detailFrame.getDiscountsGrid().reloadData();

    detailFrame.getDestGrid().getOtherGridParams().put(
        ApplicationConsts.SUBJECT_PK,
        new SubjectPK(pk.getCompanyCodeSys01SAL07(),pk.getProgressiveReg04SAL07())
    );
    detailFrame.getDestGrid().reloadData();

    detailFrame.getAgentDataLocator().getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,pk.getCompanyCodeSys01SAL07());
    detailFrame.getAgentDataLocator().getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,pk.getCompanyCodeSys01SAL07());

    detailFrame.getActivitiesGrid().getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,pk.getCompanyCodeSys01SAL07());
    detailFrame.getActivitiesGrid().getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_REG04_SUBJECT,pk.getProgressiveReg04SAL07());
    detailFrame.getActivitiesGrid().reloadData();

    detailFrame.setButtonsEnabled(true);

    if (pk.getSubjectTypeREG04().equals(ApplicationConsts.SUBJECT_ORGANIZATION_CUSTOMER)) {
      OrganizationCustomerVO vo = (OrganizationCustomerVO)detailFrame.getCurrentForm().getVOModel().getValueObject();
      detailFrame.setTitle(ClientSettings.getInstance().getResources().getResource("customer")+" "+vo.getCustomerCodeSAL07()+" - "+vo.getName_1REG04()+" "+(vo.getName_2REG04()==null?"":vo.getName_2REG04()));
    }
    else {
      PeopleCustomerVO vo = (PeopleCustomerVO)detailFrame.getCurrentForm().getVOModel().getValueObject();
      detailFrame.setTitle(ClientSettings.getInstance().getResources().getResource("customer")+" "+vo.getCustomerCodeSAL07()+" - "+vo.getName_1REG04()+" "+(vo.getName_2REG04()==null?"":vo.getName_2REG04()));
    }


  }


  /**
   * Callback method invoked before saving data when the form was in INSERT mode (on pressing save button).
   * @return <code>true</code> allows to go to INSERT mode, <code>false</code> the mode change is interrupted
   */
  public boolean beforeInsertData(Form form) {
    boolean ok = super.beforeInsertData(form);
    if (ok) {
      detailFrame.getReferencesPanel().getGrid().clearData();
      detailFrame.getHierarchiesPanel().getGrid().clearData();
      detailFrame.getDiscountsGrid().clearData();
      detailFrame.getDestGrid().clearData();
      detailFrame.getActivitiesGrid().clearData();
      detailFrame.setButtonsEnabled(false);
    }
    return ok;
  }


  /**
   * Method called by the Form panel to insert new data.
   * @param newValueObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response insertRecord(ValueObject newPersistentObject) throws Exception {
    ((Subject)newPersistentObject).setSubjectTypeREG04((String)detailFrame.getControlSubjectType().getValue());
    Response response = ClientUtils.getData("insertCustomer",newPersistentObject);
    if (!response.isError()) {
      Subject sub = (Subject)((VOResponse)response).getVo();
      pk = new CustomerPK(
        sub.getCompanyCodeSys01REG04(),
        sub.getProgressiveREG04(),
        sub.getSubjectTypeREG04()
      );
//      gridFrame.reloadData();
      detailFrame.getReferencesPanel().getGrid().getOtherGridParams().put(
          ApplicationConsts.SUBJECT_PK,
          new SubjectPK(pk.getCompanyCodeSys01SAL07(),pk.getProgressiveReg04SAL07())
      );
      detailFrame.getReferencesPanel().getGrid().reloadData();
      detailFrame.getHierarchiesPanel().getGrid().getOtherGridParams().put(
          ApplicationConsts.SUBJECT_PK,
          new SubjectPK(pk.getCompanyCodeSys01SAL07(),pk.getProgressiveReg04SAL07())
      );
      detailFrame.getHierarchiesPanel().getGrid().getOtherGridParams().put(
          ApplicationConsts.SUBJECT_TYPE,
          ApplicationConsts.SUBJECT_ORGANIZATION_CUSTOMER
      );
      detailFrame.getHierarchiesPanel().getGrid().reloadData();

      detailFrame.getDiscountsGrid().getOtherGridParams().put(
          ApplicationConsts.SUBJECT_PK,
          pk
      );
      detailFrame.getDiscountsGrid().reloadData();

      detailFrame.getDestGrid().getOtherGridParams().put(
          ApplicationConsts.SUBJECT_PK,
          new SubjectPK(pk.getCompanyCodeSys01SAL07(),pk.getProgressiveReg04SAL07())
      );
      detailFrame.getDestGrid().reloadData();

      detailFrame.getActivitiesGrid().getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,pk.getCompanyCodeSys01SAL07());
      detailFrame.getActivitiesGrid().getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_REG04_SUBJECT,pk.getProgressiveReg04SAL07());
      detailFrame.getActivitiesGrid().reloadData();

      detailFrame.setTitle(ClientSettings.getInstance().getResources().getResource("customer"));

      detailFrame.setButtonsEnabled(true);
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
    Response response = ClientUtils.getData("updateCustomer",new ValueObject[]{oldPersistentObject,persistentObject});
    if (!response.isError()) {
//      gridFrame.reloadData();
    }
    return response;
  }


  /**
   * Method called by the Form panel to delete existing data.
   * @param persistentObject value object to delete
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecord(ValueObject persistentObject) throws Exception {
    ArrayList list = new ArrayList();
    list.add(persistentObject);
    Response response = ClientUtils.getData("deleteCustomers",list);
    if (!response.isError()) {
      gridFrame.reloadData();
      detailFrame.getReferencesPanel().getGrid().clearData();
      detailFrame.getHierarchiesPanel().getGrid().clearData();
      detailFrame.getDiscountsGrid().clearData();
      detailFrame.getDestGrid().clearData();
      detailFrame.getActivitiesGrid().clearData();
    }
    return response;
  }



  /**
   * Callback method called when the Form mode is changed.
   * @param currentMode current Form mode
   */
  public void modeChanged(int currentMode) {
    if (currentMode==Consts.INSERT) {
      detailFrame.getControlSubjectType().setEnabled(true);
      detailFrame.getControlSubjectType().getComboBox().setSelectedIndex(0);
    }
    else if (currentMode==Consts.EDIT || currentMode==Consts.READONLY) {
      detailFrame.getControlSubjectType().setEnabled(false);
    }
  }



  /**
   * Callback method called by the Form panel when the Form is set to INSERT mode.
   * The method can pre-set some v.o. attributes, so that some input controls will have a predefined value associated.
   * @param persistentObject new value object
   */
  public void createPersistentObject(ValueObject persistentObject) throws Exception {
    ClientApplet applet = ( (ApplicationClientFacade) MDIFrame.getInstance().getClientFacade()).getMainClass();
    String companyCode = null;
    if (applet.getAuthorizations().isOneCompany()) {
      ButtonCompanyAuthorizations bca = applet.getAuthorizations().getCompanyBa();
      ArrayList companiesList = bca.getCompaniesList("SAL07");
      companyCode = companiesList.get(0).toString();
      ((Subject)persistentObject).setCompanyCodeSys01REG04( companyCode );
    }

    // set default values...
    HashMap map = new HashMap();
    map.put(ApplicationConsts.COMPANY_CODE_SYS01,companyCode);
    map.put(ApplicationConsts.PARAM_CODE,ApplicationConsts.CREDITS_ACCOUNT);
    Response response = ClientUtils.getData("loadUserParam",map);
    if (!response.isError()) {
      String code = (String)((VOResponse)response).getVo();
      detailFrame.getControlCreditsCode().setValue(code);
      detailFrame.getControlCreditsCode().getLookupController().forceValidate();
    }

    map.clear();
    map.put(ApplicationConsts.COMPANY_CODE_SYS01,companyCode);
    map.put(ApplicationConsts.PARAM_CODE,ApplicationConsts.ITEMS_ACCOUNT);
    response = ClientUtils.getData("loadUserParam",map);
    if (!response.isError()) {
      String code = (String)((VOResponse)response).getVo();
      detailFrame.getControlItemsCode() .setValue(code);
      detailFrame.getControlItemsCode().getLookupController().forceValidate();
    }

    map.clear();
    map.put(ApplicationConsts.COMPANY_CODE_SYS01,companyCode);
    map.put(ApplicationConsts.PARAM_CODE,ApplicationConsts.ACTIVITIES_ACCOUNT);
    response = ClientUtils.getData("loadUserParam",map);
    if (!response.isError()) {
      String code = (String)((VOResponse)response).getVo();
      detailFrame.getControlActCode() .setValue(code);
      detailFrame.getControlActCode().getLookupController().forceValidate();
    }

    map.clear();
    map.put(ApplicationConsts.COMPANY_CODE_SYS01,companyCode);
    map.put(ApplicationConsts.PARAM_CODE,ApplicationConsts.CHARGES_ACCOUNT);
    response = ClientUtils.getData("loadUserParam",map);
    if (!response.isError()) {
      String code = (String)((VOResponse)response).getVo();
      detailFrame.getControlChargesCode().setValue(code);
      detailFrame.getControlChargesCode().getLookupController().forceValidate();
    }

  }
  public CustomerPK getPk() {
    return pk;
  }
  public CustomersGridFrame getGridFrame() {
    return gridFrame;
  }




}
