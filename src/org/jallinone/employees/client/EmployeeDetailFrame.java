package org.jallinone.employees.client;

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
import org.jallinone.subjects.client.PeoplePanel;
import javax.swing.border.*;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.jallinone.system.java.ButtonCompanyAuthorizations;
import java.util.ArrayList;
import org.openswing.swing.domains.java.Domain;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import org.openswing.swing.lookup.client.LookupListener;
import org.openswing.swing.message.receive.java.*;
import java.util.Collection;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.subjects.client.SubjectHierarchyLevelsPanel;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Employee detail frame.</p>
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
public class EmployeeDetailFrame extends InternalFrame {

  PeoplePanel form = new PeoplePanel();
  JPanel buttonsPanel = new JPanel();
  JPanel employeePanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  LabelControl labelCompanyCode = new LabelControl();
  InsertButton insertButton = new InsertButton();
  EditButton editButton = new EditButton();
  CopyButton copyButton = new CopyButton();
  SaveButton saveButton = new SaveButton();
  ReloadButton reloadButton = new ReloadButton();
  DeleteButton deleteButton = new DeleteButton();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  BorderLayout borderLayout1 = new BorderLayout();
  JTabbedPane tabbedPane = new JTabbedPane();
  ComboBoxControl controlCompaniesCombo = new ComboBoxControl();
  BorderLayout borderLayout2 = new BorderLayout();
  BorderLayout borderLayout3 = new BorderLayout();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  TitledBorder titledBorder1;
  TitledBorder titledBorder2;
  LabelControl labelEmployeeCode = new LabelControl();
  TextControl controlEmployeeCode = new TextControl();
  LabelControl labelTask = new LabelControl();
  CodLookupControl controlTaskCode = new CodLookupControl();
  TextControl controlTaskDescr = new TextControl();
  LabelControl labelDivision = new LabelControl();
  TextControl controlDivision = new TextControl();
  LabelControl labelOffice = new LabelControl();
  LabelControl labelPhone = new LabelControl();
  TextControl controlOffice = new TextControl();
  TextControl controlPhone = new TextControl();
  LabelControl labelEmail = new LabelControl();
  TextControl controlEmail = new TextControl();
  LabelControl labelEngagedDate = new LabelControl();
  LabelControl labelDismissalDate = new LabelControl();
  DateControl controlEngagedDate = new DateControl();
  DateControl controlDismissalDate = new DateControl();
  LabelControl labelLevel = new LabelControl();
  LabelControl labelSalary = new LabelControl();
  TextControl controlLevel = new TextControl();
  NumericControl controlSalary = new NumericControl();
  LabelControl labelManager = new LabelControl();
  LabelControl labelAssistant = new LabelControl();
  CodLookupControl controlManager = new CodLookupControl();
  CodLookupControl controlAssistant = new CodLookupControl();
  TextControl controlManagerFirstNAme = new TextControl();
  TextControl controlManagerLastName = new TextControl();
  TextControl controlAssistantFirstName = new TextControl();
  TextControl controlAssistantLastName = new TextControl();

  LookupController taskController = new LookupController();
  LookupServerDataLocator taskDataLocator = new LookupServerDataLocator();

  LookupController managerController = new LookupController();
  LookupServerDataLocator managerDataLocator = new LookupServerDataLocator();

  LookupController assistantController = new LookupController();
  LookupServerDataLocator assistantDataLocator = new LookupServerDataLocator();
  JPanel workingDaysPanel = new JPanel();
  BorderLayout borderLayout4 = new BorderLayout();
  JPanel workingDaysToolbarPanel = new JPanel();
  GridControl grid = new GridControl();
  FlowLayout flowLayout2 = new FlowLayout();
  InsertButton insertButton1 = new InsertButton();
  EditButton editButton1 = new EditButton();
  SaveButton saveButton1 = new SaveButton();
  DeleteButton deleteButton1 = new DeleteButton();
  ReloadButton reloadButton1 = new ReloadButton();
  CopyButton copyButton1 = new CopyButton();
  NavigatorBar navigatorBar1 = new NavigatorBar();
  ComboColumn colDayOfWeek = new ComboColumn();
  TimeColumn colStartM = new TimeColumn();
  TimeColumn colEndM = new TimeColumn();
  TimeColumn colStartA = new TimeColumn();
  TimeColumn colEndA = new TimeColumn();

  /** grid data locator */
  private ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();
  JPanel absencesPanel = new JPanel();
  BorderLayout borderLayout5 = new BorderLayout();
  JPanel absButtonsPanel = new JPanel();
  GridControl absGrid = new GridControl();
  FlowLayout flowLayout3 = new FlowLayout();
  InsertButton insertButton2 = new InsertButton();
  SaveButton saveButton2 = new SaveButton();
  ReloadButton reloadButton2 = new ReloadButton();
  CopyButton copyButton2 = new CopyButton();
  DeleteButton deleteButton2 = new DeleteButton();
  NavigatorBar navigatorBar2 = new NavigatorBar();
  ExportButton exportButton2 = new ExportButton();
  ComboColumn colActType = new ComboColumn();
  DateTimeColumn colStartDate = new DateTimeColumn();
  DateTimeColumn colEndDate = new DateTimeColumn();
  ServerGridDataLocator absDataLocator = new ServerGridDataLocator();

  JPanel hierarPanel = new JPanel();
  SubjectHierarchyLevelsPanel hierarchiesPanel = new SubjectHierarchyLevelsPanel();
  BorderLayout borderLayout6 = new BorderLayout();


  public EmployeeDetailFrame(EmployeeController controller) {
    try {
      jbInit();
      setSize(750,460);
      setMinimumSize(new Dimension(750,460));

      form.setFormController(controller);

      form.addLinkedPanel(employeePanel);

      grid.setController(new EmployeeCalendarController(grid));
      grid.setGridDataLocator(gridDataLocator);
      gridDataLocator.setServerMethodName("loadEmployeeCalendar");

      absDataLocator.setServerMethodName("loadEmployeeActivities");


      // fill in the company combo...
      ClientApplet applet = ( (ApplicationClientFacade) MDIFrame.getInstance().getClientFacade()).getMainClass();
      ButtonCompanyAuthorizations bca = applet.getAuthorizations().getCompanyBa();
      ArrayList companiesList = bca.getCompaniesList("SCH01");
      Domain domain = new Domain("DOMAIN_SCH01");
      for (int i = 0; i < companiesList.size(); i++) {
        if (applet.getAuthorizations().getCompanyBa().isInsertEnabled(
            "SCH01",companiesList.get(i).toString()
        ))
          domain.addDomainPair(companiesList.get(i),companiesList.get(i).toString());
      }
      controlCompaniesCombo.setDomain(domain);


      // task lookup...
      taskDataLocator.setGridMethodName("loadTasks");
      taskDataLocator.setValidationMethodName("validateTaskCode");
      controlTaskCode.setLookupController(taskController);
      controlTaskCode.setControllerMethodName("getTasksList");
      taskController.setLookupDataLocator(taskDataLocator);
      taskController.setFrameTitle("tasks");
      taskController.setLookupValueObjectClassName("org.jallinone.registers.task.java.TaskVO");
      taskController.addLookup2ParentLink("taskCodeREG07", "taskCodeReg07SCH01");
      taskController.addLookup2ParentLink("descriptionSYS10","taskDescriptionSYS10");
      taskController.setAllColumnVisible(false);
      taskController.setVisibleColumn("taskCodeREG07", true);
      taskController.setVisibleColumn("descriptionSYS10", true);
      taskController.setForm(form);
      taskController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {}

        public void beforeLookupAction(ValueObject parentVO) {
          taskDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
          taskDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
        }

        public void forceValidate() {}

      });
      new CustomizedColumns(new BigDecimal(192),taskController);

      // manager lookup....
      managerDataLocator.setGridMethodName("loadEmployees");
      managerDataLocator.setValidationMethodName("validateEmployeeCode");
      controlManager.setLookupController(managerController);
      controlManager.setControllerMethodName("getEmployeesList");
      managerController.setLookupDataLocator(managerDataLocator);
      managerController.setFrameTitle("employees");
      managerController.setLookupValueObjectClassName("org.jallinone.employees.java.GridEmployeeVO");
      managerController.addLookup2ParentLink("companyCodeSys01SCH01", "managerCompanyCodeSys01SCH01");
      managerController.addLookup2ParentLink("progressiveReg04SCH01","managerProgressiveReg04SCH01");
      managerController.addLookup2ParentLink("name_1REG04","managerName_1REG04");
      managerController.addLookup2ParentLink("name_2REG04","managerName_2REG04");
      managerController.setAllColumnVisible(false);
      managerController.setVisibleColumn("companyCodeSys01SCH01", true);
      managerController.setVisibleColumn("name_1REG04", true);
      managerController.setVisibleColumn("name_2REG04", true);
      managerController.setVisibleColumn("descriptionSYS10", true);
      managerController.setVisibleColumn("officeSCH01", true);
      managerController.setVisibleColumn("phoneNumberSCH01", true);
      managerController.setHeaderColumnName("name_1REG04","firstname");
      managerController.setHeaderColumnName("name_2REG04","lastname");
      managerController.setHeaderColumnName("companyCodeSys01SCH01","companyCodeSYS01");
      managerController.setHeaderColumnName("descriptionSYS10","taskDescription");
      managerController.setFramePreferedSize(new Dimension(650,400));
      managerController.setForm(form);

      // assistant lookup....
      assistantDataLocator.setGridMethodName("loadEmployees");
      assistantDataLocator.setValidationMethodName("validateEmployeeCode");
      controlAssistant.setLookupController(assistantController);
      controlAssistant.setControllerMethodName("getEmployeesList");
      assistantController.setLookupDataLocator(assistantDataLocator);
      assistantController.setFrameTitle("employees");
      assistantController.setLookupValueObjectClassName("org.jallinone.employees.java.GridEmployeeVO");
      assistantController.addLookup2ParentLink("companyCodeSys01SCH01", "assistantCompanyCodeSys01SCH01");
      assistantController.addLookup2ParentLink("progressiveReg04SCH01","assistantProgressiveReg04SCH01");
      assistantController.setAllColumnVisible(false);
      assistantController.setVisibleColumn("companyCodeSys01SCH01", true);
      assistantController.setVisibleColumn("name_1REG04", true);
      assistantController.setVisibleColumn("name_2REG04", true);
      assistantController.setVisibleColumn("descriptionSYS10", true);
      assistantController.setVisibleColumn("officeSCH01", true);
      assistantController.setVisibleColumn("phoneNumberSCH01", true);
      assistantController.setHeaderColumnName("name_1REG04","firstname");
      assistantController.setHeaderColumnName("name_2REG04","lastname");
      assistantController.setHeaderColumnName("companyCodeSys01SCH01","companyCodeSYS01");
      assistantController.setHeaderColumnName("descriptionSYS10","taskDescription");
      assistantController.setFramePreferedSize(new Dimension(650,400));
      assistantController.setForm(form);
      assistantController.addLookup2ParentLink("name_1REG04","assistantName_1REG04");
      assistantController.addLookup2ParentLink("name_2REG04","assistantName_2REG04");

      CustomizedControls customizedControls = new CustomizedControls(tabbedPane,form,new BigDecimal(662));


    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    hierarPanel.setLayout(borderLayout6);


    absGrid.setMaxNumberOfRowsOnInsert(50);
    titledBorder1 = new TitledBorder("");
    titledBorder2 = new TitledBorder("");
    form.setVOClassName("org.jallinone.employees.java.DetailEmployeeVO");
    absGrid.setValueObjectClassName("org.jallinone.scheduler.activities.java.EmployeeActivityVO");
    absGrid.setController(new EmployeeAbsencesController(this,absGrid));
    absGrid.setGridDataLocator(absDataLocator);

    this.setTitle(ClientSettings.getInstance().getResources().getResource("employee detail"));
    this.getContentPane().setLayout(borderLayout3);
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    insertButton.setText("insertButton1");
    reloadButton.setText("reloadButton1");
    deleteButton.setText("deleteButton1");
    form.setInsertButton(insertButton);
    form.setCopyButton(copyButton);
    form.setEditButton(editButton);
    form.setReloadButton(reloadButton);
    form.setDeleteButton(deleteButton);
    form.setSaveButton(saveButton);
    form.setFunctionId("SCH01");
    labelCompanyCode.setText("companyCodeSys01SCH01");
    controlCompaniesCombo.setAttributeName("companyCodeSys01SCH01");
    controlCompaniesCombo.setCanCopy(true);
    controlCompaniesCombo.setLinkLabel(labelCompanyCode);
    controlCompaniesCombo.setRequired(true);
    controlCompaniesCombo.setEnabledOnEdit(false);
    employeePanel.setLayout(gridBagLayout2);
    employeePanel.setBorder(titledBorder1);
    titledBorder1.setTitleColor(Color.blue);
    titledBorder1.setTitle(ClientSettings.getInstance().getResources().getResource("employee"));
    labelEmployeeCode.setText("employeeCodeSCH01");
    labelTask.setText("taskCodeReg07SCH01");
    labelDivision.setText("divisionSCH01");
    labelOffice.setText("officeSCH01");
    labelPhone.setText("phoneNumberSCH01");
    labelEmail.setText("email");
    labelEngagedDate.setText("engagedDateSCH01");
    labelDismissalDate.setText("dismissalDateSCH01");
    labelLevel.setText("levelSCH01");
    labelSalary.setText("salarySCH01");
    labelManager.setText("manager");
    labelAssistant.setText("assistant");
    controlEmployeeCode.setAttributeName("employeeCodeSCH01");
    controlEmployeeCode.setCanCopy(false);
    controlEmployeeCode.setLinkLabel(labelEmployeeCode);
    controlEmployeeCode.setMaxCharacters(20);
    controlEmployeeCode.setRequired(true);
    controlEmployeeCode.setTrimText(true);
    controlEmployeeCode.setUpperCase(true);
    controlEmployeeCode.setEnabledOnEdit(false);
    controlTaskCode.setAttributeName("taskCodeReg07SCH01");
    controlTaskCode.setCanCopy(true);
    controlTaskCode.setLinkLabel(labelTask);
    controlTaskCode.setMaxCharacters(20);
    controlTaskCode.setRequired(true);
    controlTaskDescr.setAttributeName("taskDescriptionSYS10");
    controlTaskDescr.setCanCopy(true);
    controlTaskDescr.setEnabledOnInsert(false);
    controlTaskDescr.setEnabledOnEdit(false);
    controlDivision.setAttributeName("divisionSCH01");
    controlDivision.setCanCopy(true);
    controlDivision.setLinkLabel(labelDivision);
    controlOffice.setAttributeName("officeSCH01");
    controlOffice.setCanCopy(true);
    controlOffice.setLinkLabel(labelOffice);
    controlPhone.setAttributeName("phoneNumberSCH01");
    controlPhone.setCanCopy(true);
    controlPhone.setLinkLabel(labelPhone);
    controlPhone.setMaxCharacters(20);
    controlEmail.setAttributeName("emailAddressSCH01");
    controlEmail.setLinkLabel(labelEmail);
    controlEngagedDate.setAttributeName("engagedDateSCH01");
    controlEngagedDate.setCanCopy(false);
    controlEngagedDate.setLinkLabel(labelEngagedDate);
    controlDismissalDate.setAttributeName("dismissalDateSCH01");
    controlDismissalDate.setLinkLabel(labelDismissalDate);
    controlLevel.setAttributeName("levelSCH01");
    controlLevel.setCanCopy(true);
    controlLevel.setLinkLabel(labelLevel);
    controlLevel.setMaxCharacters(20);
    controlSalary.setAttributeName("salarySCH01");
    controlSalary.setCanCopy(false);
    controlSalary.setDecimals(5);
    controlSalary.setGrouping(true);
    controlSalary.setLinkLabel(labelSalary);
    controlManager.setAttributeName("managerProgressiveReg04SCH01");
    controlManager.setCanCopy(true);
    controlManager.setCodBoxVisible(false);
    controlManager.setLinkLabel(labelManager);
    controlManagerFirstNAme.setAttributeName("managerName_1REG04");
    controlManagerFirstNAme.setCanCopy(true);
    controlManagerFirstNAme.setEnabledOnInsert(false);
    controlManagerFirstNAme.setEnabledOnEdit(false);
    controlAssistant.setAttributeName("assistantProgressiveReg04SCH01");
    controlAssistant.setCanCopy(true);
    controlAssistant.setCodBoxVisible(false);
    controlAssistantFirstName.setAttributeName("assistantName_1REG04");
    controlAssistantFirstName.setCanCopy(true);
    controlAssistantFirstName.setEnabledOnInsert(false);
    controlAssistantFirstName.setEnabledOnEdit(false);
    controlAssistantLastName.setAttributeName("assistantName_2REG04");
    controlAssistantLastName.setCanCopy(true);
    controlAssistantLastName.setEnabledOnInsert(false);
    controlAssistantLastName.setEnabledOnEdit(false);
    controlManagerLastName.setAttributeName("managerName_2REG04");
    controlManagerLastName.setEnabledOnInsert(false);
    controlManagerLastName.setEnabledOnEdit(false);
    workingDaysPanel.setLayout(borderLayout4);
    workingDaysToolbarPanel.setLayout(flowLayout2);
    flowLayout2.setAlignment(FlowLayout.LEFT);
    insertButton1.setText("insertButton1");
    deleteButton1.setText("deleteButton1");
    reloadButton1.setText("reloadButton1");
    grid.setAutoLoadData(false);
    grid.setCopyButton(copyButton1);
    grid.setDeleteButton(deleteButton1);
    grid.setEditButton(editButton1);
    grid.setFunctionId("SCH01");
    grid.setInsertButton(insertButton1);
    grid.setNavBar(navigatorBar1);
    grid.setReloadButton(reloadButton1);
    grid.setSaveButton(saveButton1);
    grid.setValueObjectClassName("org.jallinone.employees.java.EmployeeCalendarVO");
    colDayOfWeek.setDomainId("DAY_OF_WEEK");
    colDayOfWeek.setColumnName("dayOfWeekSCH02");
    colDayOfWeek.setColumnSortable(false);
    colDayOfWeek.setEditableOnEdit(false);
    colDayOfWeek.setEditableOnInsert(true);
    colDayOfWeek.setPreferredWidth(120);
    colStartM.setColumnDuplicable(true);
    colStartM.setColumnRequired(false);
    colStartM.setEditableOnEdit(true);
    colStartM.setEditableOnInsert(true);
    colStartM.setPreferredWidth(120);
    colEndM.setColumnDuplicable(true);
    colEndM.setColumnRequired(false);
    colEndM.setEditableOnEdit(true);
    colEndM.setEditableOnInsert(true);
    colEndM.setPreferredWidth(120);
    colStartA.setColumnDuplicable(true);
    colStartA.setColumnRequired(false);
    colStartA.setEditableOnEdit(true);
    colStartA.setEditableOnInsert(true);
    colStartA.setPreferredWidth(120);
    colEndA.setColumnDuplicable(true);
    colEndA.setColumnRequired(false);
    colEndA.setEditableOnEdit(true);
    colEndA.setEditableOnInsert(true);
    colEndA.setPreferredWidth(120);
    absencesPanel.setLayout(borderLayout5);
    absGrid.setAutoLoadData(false);
    absGrid.setCopyButton(copyButton2);
    absGrid.setDeleteButton(deleteButton2);
    absGrid.setExportButton(exportButton2);
    absGrid.setFunctionId("SCH06_SCHEDULED_ACT");
    absGrid.setInsertButton(insertButton2);
    absGrid.setNavBar(navigatorBar2);
    absGrid.setReloadButton(reloadButton2);
    absGrid.setSaveButton(saveButton2);
    absButtonsPanel.setLayout(flowLayout3);
    flowLayout3.setAlignment(FlowLayout.LEFT);
    insertButton2.setText("insertButton2");
    reloadButton2.setText("reloadButton2");
    deleteButton2.setText("deleteButton2");
    colActType.setColumnName("activityTypeSCH06");
    colActType.setDomainId("ABSENCE_TYPE");
    colActType.setColumnDuplicable(true);
    colActType.setColumnFilterable(true);
    colActType.setColumnSortable(true);
    colActType.setEditableOnInsert(true);
    colStartDate.setColumnName("startDateSCH07");
    colStartDate.setColumnDuplicable(true);
    colStartDate.setColumnFilterable(true);
    colStartDate.setEditableOnInsert(true);
    colStartDate.setPreferredWidth(150);
    colStartDate.setSortVersus(org.openswing.swing.util.java.Consts.DESC_SORTED);
    colStartDate.setSortingOrder(1);
    colEndDate.setColumnName("endDateSCH07");
    colEndDate.setColumnDuplicable(true);
    colEndDate.setColumnFilterable(true);
    colEndDate.setColumnSortable(true);
    colEndDate.setEditableOnEdit(true);
    colEndDate.setEditableOnInsert(true);
    colEndDate.setPreferredWidth(150);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    this.getContentPane().add(tabbedPane, BorderLayout.CENTER);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(copyButton, null);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(saveButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(deleteButton, null);
    form.add(labelCompanyCode,     new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    tabbedPane.add(form, "form");
    tabbedPane.add(employeePanel, "employeePanel");
    employeePanel.add(labelEmployeeCode,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    employeePanel.add(controlEmployeeCode,    new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    employeePanel.add(labelTask,   new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    employeePanel.add(controlTaskCode,   new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    employeePanel.add(controlTaskDescr,    new GridBagConstraints(5, 0, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    employeePanel.add(labelDivision,   new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    employeePanel.add(controlDivision,   new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    employeePanel.add(labelOffice,     new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    employeePanel.add(labelPhone,   new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    employeePanel.add(controlOffice,   new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    employeePanel.add(controlPhone,   new GridBagConstraints(6, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    employeePanel.add(labelEmail,   new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    employeePanel.add(controlEmail,   new GridBagConstraints(1, 2, 4, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    employeePanel.add(labelEngagedDate,   new GridBagConstraints(5, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    employeePanel.add(labelDismissalDate,   new GridBagConstraints(5, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    employeePanel.add(controlEngagedDate,   new GridBagConstraints(6, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    employeePanel.add(controlDismissalDate,   new GridBagConstraints(6, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    employeePanel.add(labelLevel,   new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    employeePanel.add(labelSalary,   new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    employeePanel.add(controlLevel,    new GridBagConstraints(1, 3, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
    employeePanel.add(controlSalary,   new GridBagConstraints(4, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    employeePanel.add(labelManager,   new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    employeePanel.add(labelAssistant,   new GridBagConstraints(0, 5, 1, 1, 0.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    employeePanel.add(controlManager,      new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 0), 0, 0));
    employeePanel.add(controlAssistant,      new GridBagConstraints(1, 5, 1, 1, 0.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 0), 0, 0));
    employeePanel.add(controlManagerFirstNAme,    new GridBagConstraints(2, 4, 2, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    employeePanel.add(controlManagerLastName,     new GridBagConstraints(4, 4, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    employeePanel.add(controlAssistantFirstName,    new GridBagConstraints(2, 5, 2, 1, 0.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    employeePanel.add(controlAssistantLastName,    new GridBagConstraints(4, 5, 2, 1, 0.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    tabbedPane.add(workingDaysPanel,  "workingDays");
    tabbedPane.add(absencesPanel,   "absences");
    tabbedPane.add(hierarPanel,  "hierarchies");

    tabbedPane.setTitleAt(0,ClientSettings.getInstance().getResources().getResource("people data"));
    tabbedPane.setTitleAt(1,ClientSettings.getInstance().getResources().getResource("employee data"));
    tabbedPane.setTitleAt(2,ClientSettings.getInstance().getResources().getResource("working days"));
    tabbedPane.setTitleAt(3,ClientSettings.getInstance().getResources().getResource("absences"));
    tabbedPane.setTitleAt(4,ClientSettings.getInstance().getResources().getResource("hierarchies"));

    hierarPanel.add(hierarchiesPanel,  BorderLayout.CENTER);
    hierarchiesPanel.setAllowTreeLeafSelectionOnly(false);

    form.add(controlCompaniesCombo,          new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    workingDaysPanel.add(workingDaysToolbarPanel, BorderLayout.NORTH);
    workingDaysPanel.add(grid,  BorderLayout.CENTER);
    workingDaysToolbarPanel.add(insertButton1, null);
    workingDaysToolbarPanel.add(copyButton1, null);
    workingDaysToolbarPanel.add(editButton1, null);
    workingDaysToolbarPanel.add(saveButton1, null);
    workingDaysToolbarPanel.add(deleteButton1, null);
    workingDaysToolbarPanel.add(reloadButton1, null);
    workingDaysToolbarPanel.add(navigatorBar1, null);
    grid.getColumnContainer().add(colDayOfWeek, null);
    grid.getColumnContainer().add(colStartM, null);
    grid.getColumnContainer().add(colEndM, null);
    grid.getColumnContainer().add(colStartA, null);
    grid.getColumnContainer().add(colEndA, null);
    absencesPanel.add(absButtonsPanel, BorderLayout.NORTH);
    absencesPanel.add(absGrid,  BorderLayout.CENTER);
    absGrid.getColumnContainer().add(colActType, null);
    absButtonsPanel.add(insertButton2, null);
    absButtonsPanel.add(copyButton2, null);
    absButtonsPanel.add(saveButton2, null);
    absButtonsPanel.add(reloadButton2, null);
    absButtonsPanel.add(deleteButton2, null);
    absButtonsPanel.add(exportButton2, null);
    absButtonsPanel.add(navigatorBar2, null);
    absGrid.getColumnContainer().add(colStartDate, null);
    absGrid.getColumnContainer().add(colEndDate, null);

    colStartM.setColumnName("morningStartHourSCH02");
    colEndM.setColumnName("morningEndHourSCH02");
    colStartA.setColumnName("afternoonStartHourSCH02");
    colEndA.setColumnName("afternoonEndHourSCH02");

  }


  public SubjectHierarchyLevelsPanel getHierarchiesPanel() {
    return hierarchiesPanel;
  }


  public Form getForm() {
    return form;
  }


  public GridControl getGrid() {
    return grid;
  }
  public GridControl getAbsGrid() {
    return absGrid;
  }



}
