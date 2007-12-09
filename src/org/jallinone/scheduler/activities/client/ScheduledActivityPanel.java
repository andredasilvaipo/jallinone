package org.jallinone.scheduler.activities.client;


import org.openswing.swing.mdi.client.InternalFrame;
import org.openswing.swing.tree.client.*;
import java.awt.*;
import org.openswing.swing.mdi.client.MDIFrame;
import javax.swing.*;
import org.openswing.swing.client.*;
import org.openswing.swing.table.java.ServerGridDataLocator;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.form.client.*;
import org.jallinone.commons.client.*;
import java.math.BigDecimal;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.scheduler.activities.java.*;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.openswing.swing.lookup.client.LookupListener;
import org.openswing.swing.message.receive.java.*;
import java.util.Collection;
import org.openswing.swing.form.model.client.VOModel;
import java.awt.event.*;
import org.openswing.swing.util.client.ClientUtils;
import java.util.HashSet;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOResponse;
import org.jallinone.subjects.java.PeopleVO;
import org.jallinone.subjects.java.OrganizationVO;
import org.jallinone.employees.java.GridEmployeeVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Scheduled activity detail panel.</p>
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
public class ScheduledActivityPanel extends JPanel implements GenericButtonController {

  BorderLayout borderLayout1 = new BorderLayout();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  LabelControl labelActType = new LabelControl();
  ComboBoxControl controlActType = new ComboBoxControl();
  LabelControl labelEstEndDate = new LabelControl();
  DateControl controlEstEndDate = new DateControl();
  LabelControl labelEndDate = new LabelControl();
  DateControl controlEndDate = new DateControl();
  LabelControl labelExpDate = new LabelControl();
  DateControl controlExpDate = new DateControl();
  LabelControl labelStartDate = new LabelControl();
  DateControl controlStartDate = new DateControl();
  LabelControl labelActState = new LabelControl();
  ComboBoxControl controlActState = new ComboBoxControl();
  LabelControl labelDescr = new LabelControl();
  LabelControl labelDuration = new LabelControl();
  TextControl controlDescr = new TextControl();
  NumericControl controlDuration = new NumericControl();
  LabelControl labelEstimDuration = new LabelControl();
  NumericControl controlEstimDuration = new NumericControl();
  LabelControl labelPriority = new LabelControl();
  ComboBoxControl controlPriority = new ComboBoxControl();
  LabelControl labelCompletionPerc = new LabelControl();
  NumericControl controlCompletPerc = new NumericControl();
  LabelControl labelPlace = new LabelControl();
  TextControl controlActPlace = new TextControl();
  LabelControl labelSubjectType = new LabelControl();
  CodLookupControl filterPeopleButton = new CodLookupControl();
  CodLookupControl filterOrgButton = new CodLookupControl();
  CardLayout cardLayout2 = new CardLayout();
  JPanel cardPanel2 = new JPanel();
  TextControl controlName_1Subject = new TextControl();
  TextControl controlName_2Subject = new TextControl();
  ComboBoxControl controlSubjectType = new ComboBoxControl();
  LabelControl labelOptional = new LabelControl();
  JPanel cardPanel = new JPanel();
  CardLayout cardLayout1 = new CardLayout();
  TextControl controlEmail = new TextControl();
  TextControl controlFax = new TextControl();
  TextControl controlPhone = new TextControl();
  LabelControl labelNote = new LabelControl();
  TextAreaControl controlNote = new TextAreaControl();

  private boolean managerVisible = false;
  private boolean subjectVisible = true;
  private boolean additionalInfoVisible = true;
  private boolean insertButtonVisible = true;

  Form form = new Form();
  JPanel buttonsPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  InsertButton insertButton1 = new InsertButton();
  EditButton editButton1 = new EditButton();
  SaveButton saveButton1 = new SaveButton();
  ReloadButton reloadButton1 = new ReloadButton();
  DeleteButton deleteButton1 = new DeleteButton();
  GenericButton confirmButton = new GenericButton(new ImageIcon(ClientUtils.getImage("workflow.gif")));
  CompaniesComboControl controlCompaniesControl = new CompaniesComboControl();
  LabelControl labelCompanies = new LabelControl();
  LookupController peopleController = new LookupController();
  LookupServerDataLocator peopleDataLocator = new LookupServerDataLocator();
  LookupController orgController = new LookupController();
  LookupServerDataLocator orgDataLocator = new LookupServerDataLocator();

  private boolean addCompanyControl = true;
  private CloseActivity controller = null;
  CodLookupControl controlManagerCode = new CodLookupControl();
  LabelControl labelManager = new LabelControl();
  LookupServerDataLocator empDataLocator = new LookupServerDataLocator();
  LookupController empController = new LookupController();
  TextControl controlEmpName_1 = new TextControl();
  TextControl controlEmpName_2 = new TextControl();


  public ScheduledActivityPanel(boolean addCompanyControl,CloseActivity controller) {
    this.addCompanyControl = addCompanyControl;
    this.controller = controller;
    try {
      jbInit();
      init();
      form.setFunctionId("SCH06_SCHEDULED_ACT");
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  public final void setFunctionId(String functionId) {
    form.setFunctionId(functionId);
  }


  private void init() {
    controlActType.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange()==e.SELECTED) {
          if (controlActType.getValue().equals(ApplicationConsts.ACT_APPOINTMENT) ||
              controlActType.getValue().equals(ApplicationConsts.ACT_GENERIC_TASK) ||
              controlActType.getValue().equals(ApplicationConsts.ACT_MEETING) ||
              controlActType.getValue().equals(ApplicationConsts.ACT_ABSENCE) ||
              controlActType.getValue().equals(ApplicationConsts.ACT_HOLIDAY) ||
              controlActType.getValue().equals(ApplicationConsts.ACT_ILLNESS) ||
              controlActType.getValue().equals(ApplicationConsts.ACT_CALL_OUT)) {
            setAdditionalInfoVisible(false);
          }
          else {
            setAdditionalInfoVisible(true);
            if (controlActType.getValue().equals(ApplicationConsts.ACT_SEND_EMAIL))
              labelOptional.setText("email");
            else if (controlActType.getValue().equals(ApplicationConsts.ACT_SEND_FAX))
              labelOptional.setText("fax");
            else if (controlActType.getValue().equals(ApplicationConsts.ACT_PHONE_CALL))
              labelOptional.setText("telephone");

          }
        }
      }
    });


    // employees lookup...
    empDataLocator.setGridMethodName("loadEmployees");
    empDataLocator.setValidationMethodName("validateEmployeeCode");
    controlManagerCode.setLookupController(empController);
    controlManagerCode.setControllerMethodName("getEmployeesList");
    empController.setLookupDataLocator(empDataLocator);
    empController.setFrameTitle("employees");
    empController.setLookupValueObjectClassName("org.jallinone.employees.java.GridEmployeeVO");
    empController.addLookup2ParentLink("name_1REG04", "managerName_1SCH06");
    empController.addLookup2ParentLink("name_2REG04", "managerName_2SCH06");
    empController.addLookup2ParentLink("progressiveReg04SCH01", "progressiveReg04ManagerSCH06");
    empController.setAllColumnVisible(false);
    empController.setVisibleColumn("employeeCodeSCH01", true);
    empController.setVisibleColumn("name_1REG04", true);
    empController.setVisibleColumn("name_2REG04", true);
    empController.setPreferredWidthColumn("name_1REG04",150);
    empController.setPreferredWidthColumn("name_2REG04",150);
    empController.setFramePreferedSize(new Dimension(430,400));
    empController.addLookupListener(new LookupListener() {

      /**
       * Method called when lookup code is validated and when code is selected on lookup grid frame.
       * @param validated <code>true</code> if lookup code is correclty validated, <code>false</code> otherwise
       */
      public void codeValidated(boolean validated) {}


      /**
       * Method called when lookup code is changed (also when is set to "" or null)
       * @param parentVO lookup container v.o.
       * @param parentChangedAttributes lookup container v.o. attributes
       */
      public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
        GridEmployeeVO vo = (GridEmployeeVO)empController.getLookupVO();
        controlManagerCode.setValue(vo.getEmployeeCodeSCH01());
        controlEmpName_1.setValue(vo.getName_1REG04());
        controlEmpName_2.setValue(vo.getName_2REG04());
      }


      /**
       * Method called before code validation and on lookup button click.
       */
      public void beforeLookupAction(ValueObject parentVO) {
        ScheduledActivityVO vo = (ScheduledActivityVO)form.getVOModel().getValueObject();
        empDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01SCH06());
      }


      /**
       * Validation is forced.
       */
      public void forceValidate() {}

    });



    controlActType.getComboBox().setSelectedIndex(0);


    HashSet buttonsToDisable = new HashSet();
    buttonsToDisable.add(editButton1);
    buttonsToDisable.add(deleteButton1);
    buttonsToDisable.add(insertButton1);
    buttonsToDisable.add(reloadButton1);
    form.addButtonsNotEnabled(buttonsToDisable,this);


    // people lookup...
    peopleDataLocator.setGridMethodName("loadSubjectPerName");
    peopleDataLocator.setValidationMethodName("");
    filterPeopleButton.setLookupController(peopleController);
    peopleController.setLookupDataLocator(peopleDataLocator);
    peopleController.setFrameTitle("people");
    peopleController.setLookupValueObjectClassName("org.jallinone.subjects.java.PeopleVO");
    peopleController.addLookup2ParentLink("progressiveREG04","progressiveReg04SubjectSCH06");
    peopleController.addLookup2ParentLink("name_1REG04","subjectName_1SCH06");
    peopleController.addLookup2ParentLink("name_2REG04","subjectName_2SCH06");
    peopleController.setAllColumnVisible(false);
    peopleController.setVisibleColumn("name_1REG04", true);
    peopleController.setVisibleColumn("name_2REG04", true);
    peopleController.setVisibleColumn("addressREG04", true);
    peopleController.setVisibleColumn("cityREG04", true);
    peopleController.setVisibleColumn("provinceREG04", true);
    peopleController.setVisibleColumn("countryREG04", true);
    peopleController.setVisibleColumn("zipREG04", true);
    peopleController.setHeaderColumnName("addressREG04", "address");
    peopleController.setHeaderColumnName("cityREG04", "city");
    peopleController.setHeaderColumnName("provinceREG04", "prov");
    peopleController.setHeaderColumnName("countryREG04", "country");
    peopleController.setHeaderColumnName("zipREG04", "zip");
    peopleController.setPreferredWidthColumn("name_1REG04", 120);
    peopleController.setPreferredWidthColumn("name_2REG04", 120);
    peopleController.setPreferredWidthColumn("addressREG04", 200);
    peopleController.setPreferredWidthColumn("provinceREG04", 50);
    peopleController.setPreferredWidthColumn("countryREG04", 70);
    peopleController.setPreferredWidthColumn("zipREG04", 50);
    peopleController.setFramePreferedSize(new Dimension(740,500));
    peopleController.addLookupListener(new LookupListener() {

      public void codeValidated(boolean validated) {}

      public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
      }

      public void beforeLookupAction(ValueObject parentVO) {
        ScheduledActivityVO vo = (ScheduledActivityVO)form.getVOModel().getValueObject();
        peopleDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01SCH06());
        peopleDataLocator.getLookupFrameParams().put(ApplicationConsts.SUBJECT_TYPE,ApplicationConsts.SUBJECT_PEOPLE);
      }

      public void forceValidate() {}

    });


    // organization lookup...
    orgDataLocator.setGridMethodName("loadSubjectPerName");
    orgDataLocator.setValidationMethodName("");
    filterOrgButton.setLookupController(orgController);
    orgController.setLookupDataLocator(orgDataLocator);
    orgController.setFrameTitle("organizations");
    orgController.setLookupValueObjectClassName("org.jallinone.subjects.java.OrganizationVO");
    orgController.addLookup2ParentLink("progressiveREG04","progressiveReg04SubjectSCH06");
    orgController.addLookup2ParentLink("name_1REG04","subjectName_1SCH06");
    orgController.addLookup2ParentLink("name_2REG04","subjectName_2SCH06");
    orgController.setAllColumnVisible(false);
    orgController.setVisibleColumn("name_1REG04", true);
    orgController.setVisibleColumn("addressREG04", true);
    orgController.setVisibleColumn("cityREG04", true);
    orgController.setVisibleColumn("provinceREG04", true);
    orgController.setVisibleColumn("countryREG04", true);
    orgController.setVisibleColumn("zipREG04", true);
    orgController.setHeaderColumnName("addressREG04", "address");
    orgController.setHeaderColumnName("cityREG04", "city");
    orgController.setHeaderColumnName("provinceREG04", "prov");
    orgController.setHeaderColumnName("countryREG04", "country");
    orgController.setHeaderColumnName("zipREG04", "zip");
    orgController.setPreferredWidthColumn("name_1REG04", 150);
    orgController.setPreferredWidthColumn("addressREG04", 200);
    orgController.setPreferredWidthColumn("provinceREG04", 50);
    orgController.setPreferredWidthColumn("zipREG04", 50);
    orgController.setFramePreferedSize(new Dimension(740,500));
    orgController.addLookupListener(new LookupListener() {

      public void codeValidated(boolean validated) {}

      public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) { }

      public void beforeLookupAction(ValueObject parentVO) {
        ScheduledActivityVO vo = (ScheduledActivityVO)form.getVOModel().getValueObject();
        orgDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01SCH06());
        orgDataLocator.getLookupFrameParams().put(ApplicationConsts.SUBJECT_TYPE,ApplicationConsts.SUBJECT_ORGANIZATION);
      }

      public void forceValidate() {}

    });

    controlSubjectType.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange()==e.SELECTED) {
          cardLayout2.show(cardPanel2, (String) controlSubjectType.getValue());
          ScheduledActivityVO vo = (ScheduledActivityVO)form.getVOModel().getValueObject();
          vo.setSubjectTypeReg04SubjectSCH06((String) controlSubjectType.getValue());
        }
      }
    });

  }


  private void jbInit() throws Exception {
    controlCompaniesControl.setFunctionCode("SCH06_SCHEDULED_ACT");
    controlCompaniesControl.setAttributeName("companyCodeSys01SCH06");
    this.setLayout(borderLayout1);
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    insertButton1.setText("insertButton1");
    editButton1.setText("editButton1");
    saveButton1.setText("saveButton1");
    reloadButton1.setText("reloadButton1");
    form.setInsertButton(insertButton1);
    form.setEditButton(editButton1);
    form.setReloadButton(reloadButton1);
    form.setDeleteButton(deleteButton1);
    form.setSaveButton(saveButton1);
    controlEndDate.setEnabledOnInsert(false);
    controlEstEndDate.addFocusListener(new ScheduledActivityPanel_controlEstEndDate_focusAdapter(this));
    controlDuration.setEnabledOnInsert(false);
    controlEstimDuration.addFocusListener(new ScheduledActivityPanel_controlEstimDuration_focusAdapter(this));
    confirmButton.addActionListener(new ScheduledActivityPanel_confirmButton_actionAdapter(this));
    labelCompanies.setText("companyCodeSYS01");
    controlCompaniesControl.setDomainId("");
    controlCompaniesControl.setLinkLabel(labelCompanies);
    controlCompaniesControl.setRequired(true);
    controlCompaniesControl.setEnabledOnEdit(false);
    controlCompaniesControl.setFunctionCode("SCH06");
    this.add(buttonsPanel,BorderLayout.NORTH);
    buttonsPanel.add(insertButton1, null);
    buttonsPanel.add(editButton1, null);
    buttonsPanel.add(saveButton1, null);
    buttonsPanel.add(reloadButton1, null);
    buttonsPanel.add(deleteButton1, null);
    buttonsPanel.add(confirmButton, null);

    confirmButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("close activity"));
    confirmButton.setEnabled(false);


    this.add(form,BorderLayout.CENTER);
    labelActType.setText("activityTypeSCH06");
    form.setVOClassName("org.jallinone.scheduler.activities.java.ScheduledActivityVO");
    form.setLayout(gridBagLayout1);
    labelEstEndDate.setText("estimatedEndDateSCH06");
    labelEndDate.setText("endDateSCH06");
    labelExpDate.setToolTipText("");
    labelExpDate.setText("expirationDateSCH06");
    labelStartDate.setToolTipText("");
    labelStartDate.setText("startDateSCH06");
    labelActState.setText("activityStateSCH06");
    labelDescr.setText("descriptionSCH06");
    labelDuration.setText("durationSCH06");
    labelEstimDuration.setText("estimatedDurationSCH06");
    labelPriority.setText("prioritySCH06");
    labelCompletionPerc.setText("completionPercSCH06");
    labelPlace.setText("activityPlaceSCH06");
    labelSubjectType.setText("subject type");

    labelManager.setText("manager");

    filterOrgButton.setAllowOnlyNumbers(true);
    filterOrgButton.setCodBoxVisible(false);
    filterOrgButton.setEnabledOnEdit(false);
    filterOrgButton.setLinkLabel(labelSubjectType);
    filterPeopleButton.setAllowOnlyNumbers(true);
    filterPeopleButton.setCodBoxVisible(false);
    filterPeopleButton.setEnabledOnEdit(false);
    filterPeopleButton.setLinkLabel(labelSubjectType);

    cardPanel.setLayout(cardLayout1);
    cardPanel2.setLayout(cardLayout2);
    labelNote.setToolTipText("");
    labelNote.setText("noteSCH06");
    controlActType.setDomainId("ACTIVITY_TYPE_NO_CALL_OUT");
    controlActType.setLinkLabel(labelActType);
    controlActType.setRequired(true);
    controlActType.setEnabledOnEdit(false);
    controlActState.setDomainId("ACTIVITY_STATE");
    controlActState.setLinkLabel(labelActState);
    controlActState.setRequired(false);
    controlActState.setEnabledOnInsert(false);
    controlActState.setEnabledOnEdit(false);
    controlStartDate.setLinkLabel(labelStartDate);
    controlStartDate.setRequired(true);
    controlEstEndDate.setLinkLabel(labelEstEndDate);
    controlEndDate.setLinkLabel(labelEndDate);
    controlExpDate.setLinkLabel(labelExpDate);
    controlDuration.setLinkLabel(labelDuration);
    controlDuration.setRequired(false);
    controlEstimDuration.setLinkLabel(labelEstimDuration);
    controlEstimDuration.setRequired(true);
    controlPriority.setDomainId("ACTIVITY_PRIORITY");
    controlPriority.setLinkLabel(labelPriority);
    controlPriority.setRequired(true);
    controlDescr.setLinkLabel(labelDescr);
    controlDescr.setRequired(true);
    controlCompletPerc.setLinkLabel(labelCompletionPerc);
    controlSubjectType.setDomainId("SUBJECT_TYPE_2");
    controlSubjectType.setLinkLabel(labelSubjectType);
    controlSubjectType.setRequired(false);
    controlSubjectType.setEnabledOnEdit(false);

    filterOrgButton.setAttributeName("progressiveReg04SubjectSCH06");
    filterPeopleButton.setAttributeName("progressiveReg04SubjectSCH06");
    cardPanel2.add(filterOrgButton,ApplicationConsts.SUBJECT_ORGANIZATION);
    cardPanel2.add(filterPeopleButton,ApplicationConsts.SUBJECT_PEOPLE);

    controlManagerCode.setMaxCharacters(20);

    controlName_1Subject.setEnabledOnInsert(false);
    controlName_1Subject.setEnabledOnEdit(false);
    controlName_2Subject.setEnabledOnInsert(false);
    controlName_2Subject.setEnabledOnEdit(false);

    controlEmpName_1.setAttributeName("managerName_1SCH06");
    controlEmpName_1.setEnabledOnInsert(false);
    controlEmpName_1.setEnabledOnEdit(false);
    controlEmpName_2.setAttributeName("managerName_2SCH06");
    controlEmpName_2.setEnabledOnInsert(false);
    controlEmpName_2.setEnabledOnEdit(false);

    labelOptional.setText("");
    controlEmail.setAttributeName("emailAddressSCH06");
    controlEmail.setLinkLabel(labelOptional);
    controlFax.setAttributeName("faxNumberSCH06");
    controlFax.setLinkLabel(labelOptional);
    controlPhone.setAttributeName("phoneNumberSCH06");
    controlPhone.setLinkLabel(labelOptional);
    controlNote.setAttributeName("noteSCH06");
    controlNote.setLinkLabel(labelNote);
    controlNote.setMaxCharacters(2000);
    form.add(labelActType,            new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
    form.add(controlActType,               new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 50, 0));
    form.add(controlEstEndDate,                new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 90, 0));
    form.add(controlEndDate,               new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 90, 0));
    form.add(labelExpDate,              new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    form.add(controlExpDate,                  new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 90, 0));
    form.add(labelActState,              new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    form.add(controlActState,              new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 40, 0));
    form.add(labelDescr,             new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    form.add(labelDuration,             new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    form.add(controlDescr,            new GridBagConstraints(1, 4, 5, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    form.add(controlDuration,            new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 40, 0));
    form.add(labelEstimDuration,            new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    form.add(controlEstimDuration,            new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 40, 0));
    form.add(labelPriority,            new GridBagConstraints(4, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    form.add(controlPriority,              new GridBagConstraints(5, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 40, 0));
    form.add(labelCompletionPerc,            new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    form.add(controlCompletPerc,            new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 40, 0));
    form.add(labelPlace,            new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    form.add(controlActPlace,            new GridBagConstraints(3, 5, 6, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    form.add(labelSubjectType,            new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    form.add(controlName_2Subject,             new GridBagConstraints(5, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    form.add(controlName_1Subject,            new GridBagConstraints(3, 6, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    form.add(cardPanel2,           new GridBagConstraints(2, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    form.add(controlSubjectType,            new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    form.add(labelOptional,            new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    form.add(cardPanel,            new GridBagConstraints(1, 7, 5, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    controlActType.setAttributeName("activityTypeSCH06");
    controlActState.setAttributeName("activityStateSCH06");
    controlStartDate.setAttributeName("startDateSCH06");
    controlStartDate.setDateType(Consts.TYPE_DATE_TIME);
    controlEstEndDate.setAttributeName("estimatedEndDateSCH06");
    controlEstEndDate.setDateType(Consts.TYPE_DATE_TIME);
    controlEndDate.setAttributeName("endDateSCH06");
    controlEndDate.setDateType(Consts.TYPE_DATE_TIME);
    controlExpDate.setAttributeName("expirationDateSCH06");
    controlExpDate.setDateType(Consts.TYPE_DATE_TIME);
    cardPanel.add(controlEmail,  "EMAIL");
    cardPanel.add(controlFax,   "FAX");
    cardPanel.add(controlPhone,   "PHONE");
    controlDuration.setAttributeName("durationSCH06");
    controlEstimDuration.setAttributeName("estimatedDurationSCH06");
    controlPriority.setAttributeName("prioritySCH06");
    controlDescr.setAttributeName("descriptionSCH06");
    controlCompletPerc.setAttributeName("completionPercSCH06");
    controlActPlace.setAttributeName("activityPlaceSCH06");
    controlSubjectType.setAttributeName("subjectTypeReg04SubjectSCH06");
    controlName_1Subject.setAttributeName("subjectName_1SCH06");
    controlName_2Subject.setAttributeName("subjectName_2SCH06");
    form.add(labelNote,             new GridBagConstraints(0, 11, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    form.add(controlNote,            new GridBagConstraints(1, 11, 5, 1, 0.01, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    form.add(labelStartDate,      new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    form.add(controlStartDate,     new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 90, 0));
    form.add(labelEstEndDate,   new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    form.add(labelEndDate,  new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

    if (addCompanyControl) {
      form.add(controlCompaniesControl,    new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
      form.add(labelCompanies,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    }

  }


  /**
   * Show/hide the additional "row", used for an email address, fax number or phone number.
   */
  private final void setAdditionalInfoVisible(boolean additionalInfoVisible) {
    if (additionalInfoVisible!=this.additionalInfoVisible) {
      if (additionalInfoVisible) {
        form.add(labelOptional,   new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0
                ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        form.add(cardPanel,   new GridBagConstraints(1, 9, 5, 1, 1.0, 0.0
                ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        form.revalidate();
        form.repaint();
      }
      else {
        form.remove(labelOptional);
        form.remove(cardPanel);
        form.revalidate();
        form.repaint();
      }
    }
    this.additionalInfoVisible = additionalInfoVisible;
  }


  /**
   * Show/hide subject "row" in the panel.
   */

  public final void setSubjectVisible(boolean subjectVisible) {
    if (subjectVisible!=this.subjectVisible) {
      if (subjectVisible) {
        form.add(labelSubjectType,   new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0
                ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        form.add(controlName_2Subject,    new GridBagConstraints(5, 8, 1, 1, 0.0, 0.0
                ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
        form.add(controlName_1Subject,   new GridBagConstraints(3, 8, 2, 1, 0.0, 0.0
                ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        form.add(cardPanel2,  new GridBagConstraints(2, 8, 1, 1, 0.0, 0.0
                ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        form.add(controlSubjectType,   new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0
                ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        form.revalidate();
        form.repaint();
      }
      else {
        form.remove(labelSubjectType);
        form.remove(controlName_2Subject);
        form.remove(controlName_1Subject);
        form.remove(cardPanel2);
        form.remove(controlSubjectType);
        form.revalidate();
        form.repaint();
      }
    }
    this.subjectVisible = subjectVisible;
  }


  /**
   * Show/hide employee manager "row" in the panel.
   */

  public final void setManagerVisible(boolean managerVisible) {
    if (managerVisible!=this.managerVisible) {
      if (managerVisible) {
        form.add(labelManager,   new GridBagConstraints(0, 10, 1, 1, 0.0, 0.0
                ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
        form.add(controlManagerCode,   new GridBagConstraints(1, 10, 1, 1, 0.0, 0.0
                ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        form.add(controlEmpName_1,    new GridBagConstraints(2, 10, 1, 1, 0.0, 0.0
                ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
        form.add(controlEmpName_2,   new GridBagConstraints(3, 10, 2, 1, 0.0, 0.0
                ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
        form.revalidate();
        form.repaint();
      }
      else {
        form.remove(labelManager);
        form.remove(controlEmpName_1);
        form.remove(controlEmpName_2);
        form.remove(controlManagerCode);
        form.revalidate();
        form.repaint();
      }
    }
    this.managerVisible = managerVisible;
  }


  public TextControl getControlDescr() {
    return controlDescr;
  }


  public ComboBoxControl getControlActType() {
    return controlActType;
  }


  public void setFormController(FormController controller) {
    form.setFormController(controller);
  }


  public void setMode(int mode) {
    form.setMode(mode);
  }


  public int getMode() {
    return form.getMode();
  }


  public void insert() {
    form.insert();
  }


  public VOModel getVOModel() {
    return form.getVOModel();
  }


  public void setInsertButtonVisible(boolean insertButtonVisible) {
    if (insertButtonVisible!=this.insertButtonVisible) {
      if (insertButtonVisible) {
        buttonsPanel.add(insertButton1,null);
      }
      else {
        buttonsPanel.remove(insertButton1);
      }
      buttonsPanel.revalidate();
      buttonsPanel.repaint();
    }
    this.insertButtonVisible = insertButtonVisible;
  }


  public void reload() {
    form.reload();
  }


  public Form getForm() {
    return form;
  }


  void controlEstEndDate_focusLost(FocusEvent e) {
    if (form.getMode()!=Consts.READONLY &&
        controlEstEndDate.getValue()!=null && controlStartDate.getValue()!=null) {
      ScheduledActivityVO vo = (ScheduledActivityVO)form.getVOModel().getValueObject();
      vo.setEstimatedDurationSCH06(new BigDecimal(
        (((java.util.Date)controlEstEndDate.getValue()).getTime()-((java.util.Date)controlStartDate.getValue()).getTime())/1000/60
      ));
      form.pull("estimatedDurationSCH06");
    }
 }


  void controlEstimDuration_focusLost(FocusEvent e) {
    if (form.getMode()!=Consts.READONLY &&
        controlEstimDuration.getValue()!=null && controlStartDate.getValue()!=null) {
      ScheduledActivityVO vo = (ScheduledActivityVO)form.getVOModel().getValueObject();
      vo.setEstimatedEndDateSCH06(new java.sql.Timestamp(
        ((java.util.Date)controlStartDate.getValue()).getTime()+((BigDecimal)controlEstimDuration.getValue()).longValue()*60*1000
      ));
      form.pull("estimatedEndDateSCH06");
    }
  }


  void confirmButton_actionPerformed(ActionEvent e) {
    try {
      if (JOptionPane.showConfirmDialog(ClientUtils.getParentFrame(this),
                                    ClientSettings.getInstance().getResources().getResource("confirm activity closing?"),
                                    ClientSettings.getInstance().getResources().getResource("activity closing"),
                                    JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION)
       return;

      ScheduledActivityVO vo = (ScheduledActivityVO) form.getVOModel().getValueObject();

      Response res = ClientUtils.getData("closeScheduledActivity", vo);
      if (res.isError()) {
        JOptionPane.showMessageDialog(
            ClientUtils.getParentFrame(this),
            ClientSettings.getInstance().getResources().getResource(res.getErrorMessage()),
            ClientSettings.getInstance().getResources().getResource("Error"),
            JOptionPane.ERROR_MESSAGE
        );
      }
      else {
        vo = (ScheduledActivityVO)((VOResponse)res).getVo();
        controlActState.setValue(vo.getActivityStateSCH06());
        form.getVOModel().setValueObject(vo);
        form.pull("activityStateSCH06");
        confirmButton.setEnabled(false);
        insertButton1.setEnabled(false);
        editButton1.setEnabled(false);
        deleteButton1.setEnabled(false);
        reloadButton1.setEnabled(false);
        controller.closeActivity();
      }

    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  /**
   * Method called by GenericButton.setEnabled method to check if the button must be disabled.
   * @param button button whose abilitation must be checked
   * @return <code>true</code> if no policy is defined in the form/grid for the specified button, <code>false</code> if there exists a disabilitation policy for the specified button (through addButtonsNotEnabledOnState form/grid method)
   */
  public boolean isButtonDisabled(GenericButton button) {
    ScheduledActivityVO vo = (ScheduledActivityVO)form.getVOModel().getValueObject();
    if (vo!=null && vo.getActivityStateSCH06()!=null && (
      vo.getActivityStateSCH06().equals(ApplicationConsts.CLOSED) ||
      vo.getActivityStateSCH06().equals(ApplicationConsts.INVOICED)))
      return true;
    else
      return false;
  }


  public GenericButton getConfirmButton() {
    return confirmButton;
  }
  public CompaniesComboControl getControlCompaniesControl() {
    return controlCompaniesControl;
  }
  public TextControl getControlEmail() {
    return controlEmail;
  }
  public TextControl getControlFax() {
    return controlFax;
  }
  public TextControl getControlPhone() {
    return controlPhone;
  }
  public ComboBoxControl getControlSubjectType() {
    return controlSubjectType;
  }
  public TextControl getControlName_1Subject() {
    return controlName_1Subject;
  }
  public TextControl getControlName_2Subject() {
    return controlName_2Subject;
  }



}

class ScheduledActivityPanel_controlEstEndDate_focusAdapter extends java.awt.event.FocusAdapter {
  ScheduledActivityPanel adaptee;

  ScheduledActivityPanel_controlEstEndDate_focusAdapter(ScheduledActivityPanel adaptee) {
    this.adaptee = adaptee;
  }
  public void focusLost(FocusEvent e) {
    adaptee.controlEstEndDate_focusLost(e);
  }
}

class ScheduledActivityPanel_controlEstimDuration_focusAdapter extends java.awt.event.FocusAdapter {
  ScheduledActivityPanel adaptee;

  ScheduledActivityPanel_controlEstimDuration_focusAdapter(ScheduledActivityPanel adaptee) {
    this.adaptee = adaptee;
  }
  public void focusLost(FocusEvent e) {
    adaptee.controlEstimDuration_focusLost(e);
  }
}

class ScheduledActivityPanel_confirmButton_actionAdapter implements java.awt.event.ActionListener {
  ScheduledActivityPanel adaptee;

  ScheduledActivityPanel_confirmButton_actionAdapter(ScheduledActivityPanel adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.confirmButton_actionPerformed(e);
  }
}

