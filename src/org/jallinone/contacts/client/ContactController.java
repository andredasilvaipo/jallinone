package org.jallinone.contacts.client;

import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.form.client.FormController;
import org.openswing.swing.message.receive.java.*;
import org.jallinone.contacts.java.*;
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
import org.jallinone.subjects.java.SubjectVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.subjects.java.SubjectPK;
import org.jallinone.subjects.java.OrganizationVO;
import org.jallinone.subjects.java.PeopleVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Form Controller used for contact (organization or person) detail frame.</p>
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
public class ContactController extends CompanyFormController {

  /** pk */
  private SubjectVO subVO = null;

  /** detail frame */
  private ContactDetailFrame detailFrame = null;

  /** parent frame */
  private ContactsGridFrame gridFrame = null;

  /** person contact organization */
  private SubjectPK organization = null;


  public ContactController(ContactsGridFrame gridFrame,SubjectVO subVO) {
    this(gridFrame,subVO,null);
  }


  public ContactController(ContactsGridFrame gridFrame,SubjectVO subVO,SubjectPK organization) {
    this.gridFrame = gridFrame;
    this.subVO = subVO;
    this.organization = organization;

    detailFrame = new ContactDetailFrame(this);
    MDIFrame.add(detailFrame);

    detailFrame.setParentFrame(gridFrame);
    gridFrame.pushFrame(detailFrame);

    if (subVO!=null) {
      detailFrame.subjectChanged(subVO.getSubjectTypeREG04());
      detailFrame.getCurrentForm().setMode(Consts.READONLY);
      detailFrame.getCurrentForm().executeReload();
    }
    else {
      detailFrame.getCurrentForm().insert();
    }

    if (organization!=null)
      detailFrame.getControlSubjectType().getComboBox().setSelectedIndex(1);
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
      GridContactVO gridVO = (GridContactVO)gridFrame.getGrid().getVOListTableModel().getObjectForRow(row);
      subVO = new SubjectVO(
        gridVO.getCompanyCodeSys01REG04(),
        gridVO.getProgressiveREG04(),
        gridVO.getName_1REG04(),
        gridVO.getName_2REG04(),
        gridVO.getSubjectTypeREG04()
      );
    }

    detailFrame.subjectChanged( subVO.getSubjectTypeREG04() );
    return ClientUtils.getData("loadContact",subVO);
  }


  /**
   * Callback method called when the data loading is completed.
   * @param error <code>true</code> if an error occours during data loading, <code>false</code> if data loading is successfully completed
   */
  public void loadDataCompleted(boolean error) {
    SubjectPK pk = new SubjectPK(
        subVO.getCompanyCodeSys01REG04(),
        subVO.getProgressiveREG04()
    );
    detailFrame.getReferencesPanel().getGrid().getOtherGridParams().put(
        ApplicationConsts.SUBJECT_PK,
        pk
    );
    detailFrame.getReferencesPanel().getGrid().reloadData();

    detailFrame.getHierarchiesPanel().getGrid().getOtherGridParams().put(
        ApplicationConsts.SUBJECT_PK,
        pk
    );
    detailFrame.getHierarchiesPanel().getGrid().getOtherGridParams().put(
        ApplicationConsts.SUBJECT_TYPE,
        ApplicationConsts.SUBJECT_ORGANIZATION_CONTACT
    );
    detailFrame.getHierarchiesPanel().getGrid().reloadData();

    detailFrame.getContactsPanel().getGrid().getOtherGridParams().put(
        ApplicationConsts.SUBJECT_PK,
        pk
    );
    detailFrame.getContactsPanel().getGrid().reloadData();

    detailFrame.getActivitiesGrid().getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,pk.getCompanyCodeSys01REG04());
    detailFrame.getActivitiesGrid().getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_REG04_SUBJECT,pk.getProgressiveREG04());
    detailFrame.getActivitiesGrid().reloadData();


    // disable people contacts panel if this is not an organization...
    if (organization!=null || subVO.getSubjectTypeREG04().equals(ApplicationConsts.SUBJECT_PEOPLE_CONTACT))
      detailFrame.getTabbedPane().setEnabledAt(3,false);

    detailFrame.setButtonsEnabled(true);
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
      detailFrame.getContactsPanel().getGrid().clearData();
      detailFrame.setButtonsEnabled(false);
      detailFrame.getTabbedPane().setEnabledAt(3,false);
      detailFrame.getActivitiesGrid().clearData();

    }
    return ok;
  }


  /**
   * Method called by the Form panel to insert new data.
   * @param newValueObject value object to save
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response insertRecord(ValueObject newPersistentObject) throws Exception {
    Subject sub = (Subject)newPersistentObject;
    sub.setSubjectTypeREG04((String)detailFrame.getControlSubjectType().getValue());
    if (sub.getSubjectTypeREG04().equals(ApplicationConsts.SUBJECT_ORGANIZATION_CONTACT)) {
      OrganizationVO vo = (OrganizationVO)sub;
      if (organization!=null) {
        vo.setCompanyCodeSys01Reg04REG04(organization.getCompanyCodeSys01REG04());
        vo.setProgressiveReg04REG04(organization.getProgressiveREG04());
      }
    }
    else {
      PeopleVO vo = (PeopleVO)sub;
      if (organization!=null) {
        vo.setCompanyCodeSys01Reg04REG04(organization.getCompanyCodeSys01REG04());
        vo.setProgressiveReg04REG04(organization.getProgressiveREG04());
      }
    }

    Response response = ClientUtils.getData("insertContact",newPersistentObject);
    if (!response.isError()) {
      sub = (Subject)((VOResponse)response).getVo();
      subVO = new SubjectVO(
          sub.getCompanyCodeSys01REG04(),
          sub.getProgressiveREG04(),
          null, // it's not very good...
          null, // it's not very good...
          sub.getSubjectTypeREG04()
      );
      SubjectPK pk = new SubjectPK(
          sub.getCompanyCodeSys01REG04(),
          sub.getProgressiveREG04()
      );

//      gridFrame.reloadData();

      detailFrame.getReferencesPanel().getGrid().getOtherGridParams().put(
          ApplicationConsts.SUBJECT_PK,
          pk
      );
      detailFrame.getReferencesPanel().getGrid().reloadData();

      detailFrame.getHierarchiesPanel().getGrid().getOtherGridParams().put(
          ApplicationConsts.SUBJECT_PK,
          pk
      );

      detailFrame.getHierarchiesPanel().getGrid().getOtherGridParams().put(
          ApplicationConsts.SUBJECT_TYPE,
          ApplicationConsts.SUBJECT_ORGANIZATION_CONTACT
      );
      detailFrame.getHierarchiesPanel().getGrid().reloadData();

      detailFrame.getContactsPanel().getGrid().getOtherGridParams().put(
          ApplicationConsts.SUBJECT_PK,
          pk
      );
      detailFrame.getContactsPanel().getGrid().reloadData();

      detailFrame.getActivitiesGrid().getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,pk.getCompanyCodeSys01REG04());
      detailFrame.getActivitiesGrid().getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_REG04_SUBJECT,pk.getProgressiveREG04());
      detailFrame.getActivitiesGrid().reloadData();

      // disable people contacts panel if this is not an organization...
      if (organization!=null || subVO.getSubjectTypeREG04().equals(ApplicationConsts.SUBJECT_PEOPLE_CONTACT))
        detailFrame.getTabbedPane().setEnabledAt(3,false);
      else
        detailFrame.getTabbedPane().setEnabledAt(3,true);


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
    Response response = ClientUtils.getData("updateContact",new ValueObject[]{oldPersistentObject,persistentObject});
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
    Response response = ClientUtils.getData("deleteContacts",list);
    if (!response.isError()) {
      gridFrame.reloadData();
      detailFrame.getReferencesPanel().getGrid().clearData();
      detailFrame.getHierarchiesPanel().getGrid().clearData();
      detailFrame.getContactsPanel().getGrid().clearData();
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

      if (organization==null)
        detailFrame.getControlSubjectType().getComboBox().setSelectedIndex(0);
      else
        detailFrame.getControlSubjectType().getComboBox().setSelectedIndex(1);
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
    if (applet.getAuthorizations().isOneCompany()) {
      ButtonCompanyAuthorizations bca = applet.getAuthorizations().getCompanyBa();
      ArrayList companiesList = bca.getCompaniesList("REG04_CONTACTS");
      ((Subject)persistentObject).setCompanyCodeSys01REG04( companiesList.get(0).toString() );
    }
  }


  public final SubjectPK getOrganization() {
    return organization;
  }
  public ContactsGridFrame getGridFrame() {
    return gridFrame;
  }




}
