package org.jallinone.scheduler.activities.client;

import org.openswing.swing.mdi.client.InternalFrame;
import java.awt.*;
import org.openswing.swing.mdi.client.MDIFrame;
import javax.swing.*;
import org.openswing.swing.client.*;
import org.openswing.swing.table.java.ServerGridDataLocator;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.util.java.Consts;
import org.jallinone.scheduler.activities.java.*;
import java.awt.event.*;
import org.openswing.swing.util.client.ClientUtils;
import java.awt.event.*;
import org.jallinone.subjects.java.PeopleVO;
import org.jallinone.subjects.java.OrganizationVO;
import java.util.ArrayList;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupListener;
import org.openswing.swing.message.receive.java.*;
import java.util.Collection;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.employees.java.GridEmployeeVO;
import java.util.Calendar;
import org.jallinone.commons.client.CompanyGridController;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Scheduled activities grid panel.</p>
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
public class ScheduledActivitiesPanel extends JPanel {

  GridControl grid = new GridControl();
  JPanel topPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  BorderLayout borderLayout2 = new BorderLayout();
  JPanel buttonsPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  InsertButton insertButton = new InsertButton();
  ReloadButton reloadButton = new ReloadButton();
  DeleteButton deleteButton = new DeleteButton();
  NavigatorBar navigatorBar = new NavigatorBar();
  ExportButton exportButton = new ExportButton();
  TextColumn colCompanyCode = new TextColumn();
  TextColumn colActPlace = new TextColumn();
  TextColumn colManager1 = new TextColumn();
  TextColumn colManager2 = new TextColumn();
  TextColumn colSubject1 = new TextColumn();
  TextColumn colSubject2 = new TextColumn();
  TextColumn colActDescr = new TextColumn();

  /** grid data locator */
  private ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();
  DateTimeColumn colEstimatedEndDate = new DateTimeColumn();
  DateTimeColumn colStartDate = new DateTimeColumn();
  DateTimeColumn colEndDate = new DateTimeColumn();
  ComboColumn colActState = new ComboColumn();
  ComboColumn colPriority = new ComboColumn();
  JPanel filterPanel = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  LabelControl labelSubjectType = new LabelControl();
  ComboBoxControl controlSubjectType = new ComboBoxControl();
  TextControl controlName_1 = new TextControl();
  TextControl controlName_2 = new TextControl();
  LabelControl labelManager = new LabelControl();
  LabelControl labelActType = new LabelControl();
  ComboBoxControl controlActType = new ComboBoxControl();
  CodLookupControl controlManagerCode = new CodLookupControl();
  TextControl controlEmpName_1 = new TextControl();
  TextControl controlEmpName_2 = new TextControl();
  LabelControl labelState = new LabelControl();
  ComboBoxControl controlActState = new ComboBoxControl();
  LabelControl labelFromDate = new LabelControl();
  DateControl controlStartDate = new DateControl();
  GenericButton findButton = new GenericButton(new ImageIcon(ClientUtils.getImage("filter.gif")));
  LookupController peopleController = new LookupController();
  LookupServerDataLocator peopleDataLocator = new LookupServerDataLocator();
  LookupController orgController = new LookupController();
  LookupServerDataLocator orgDataLocator = new LookupServerDataLocator();
  CardLayout cardLayout2 = new CardLayout();
  JPanel cardPanel2 = new JPanel();
  CodLookupControl filterPeopleButton = new CodLookupControl();
  CodLookupControl filterOrgButton = new CodLookupControl();
  LookupServerDataLocator empDataLocator = new LookupServerDataLocator();
  LookupController empController = new LookupController();
  private Object progressiveManagerREG04 = null;

  /** flag used to define if subject type lookup must be showed */
  private boolean showSubjectType;


  public ScheduledActivitiesPanel(boolean showSubjectType) {
    this.showSubjectType = showSubjectType;
    grid.setGridDataLocator(gridDataLocator);
    gridDataLocator.setServerMethodName("loadScheduledActivities");
    try {
      jbInit();
      init();

      setSize(750,400);
      setMinimumSize(new Dimension(750,400));
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  public final void setController(CompanyGridController controller) {
    grid.setController(controller);
  }


  private void init() {
    Calendar cal = Calendar.getInstance();
    cal.set(cal.HOUR_OF_DAY,0);
    cal.set(cal.MINUTE,0);
    cal.set(cal.SECOND,0);
    cal.set(cal.MILLISECOND,0);
    controlStartDate.setValue(cal.getTime());
    grid.getOtherGridParams().put(ApplicationConsts.START_DATE,controlStartDate.getValue());

    // employees lookup...
    empDataLocator.setGridMethodName("loadEmployees");
    empDataLocator.setValidationMethodName("validateEmployeeCode");
    controlManagerCode.setLookupController(empController);
    controlManagerCode.setControllerMethodName("getEmployeesList");
    empController.setLookupDataLocator(empDataLocator);
    empController.setFrameTitle("employees");
    empController.setLookupValueObjectClassName("org.jallinone.employees.java.GridEmployeeVO");
//    empController.addLookup2ParentLink("employeeCodeSCH01", "employeeCodeSCH01");
//    empController.addLookup2ParentLink("companyCodeSys01SCH01", "companyCodeSys01SYS03");
//    empController.addLookup2ParentLink("name_1REG04", "firstNameSYS03");
//    empController.addLookup2ParentLink("name_2REG04", "lastNameSYS03");
//    empController.addLookup2ParentLink("progressiveReg04SCH01", "progressiveReg04SYS03");
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
        controlEmpName_1.setValue(vo.getName_1REG04());
        controlEmpName_2.setValue(vo.getName_2REG04());
        controlManagerCode.setValue(vo.getEmployeeCodeSCH01());
        progressiveManagerREG04 = vo.getProgressiveReg04SCH01();
      }


      /**
       * Method called before code validation and on lookup button click.
       */
      public void beforeLookupAction(ValueObject parentVO) {}


      /**
       * Validation is forced.
       */
      public void forceValidate() {}

    });

      // people lookup...
    peopleDataLocator.setGridMethodName("loadSubjectPerName");
    peopleDataLocator.setValidationMethodName("");
    filterPeopleButton.setLookupController(peopleController);
    peopleController.setLookupDataLocator(peopleDataLocator);
    peopleController.setFrameTitle("people");
    peopleController.setLookupValueObjectClassName("org.jallinone.subjects.java.PeopleVO");
//    peopleController.addLookup2ParentLink("progressiveREG04","progressiveReg04SubjectSCH06");
//    peopleController.addLookup2ParentLink("name_1REG04","subjectName_1SCH06");
//    peopleController.addLookup2ParentLink("name_2REG04","subjectName_2SCH06");
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
        PeopleVO vo = (PeopleVO)peopleController.getLookupVO();
        controlName_1.setValue(vo.getName_1REG04());
        controlName_2.setValue(vo.getName_2REG04());
        filterPeopleButton.setValue(vo.getProgressiveREG04());
      }

      public void beforeLookupAction(ValueObject parentVO) {
//        peopleDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,empVO.getCompanyCodeSys01REG04());
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
//    orgController.addLookup2ParentLink("progressiveREG04","progressiveReg04SubjectSCH06");
//    orgController.addLookup2ParentLink("name_1REG04","subjectName_1SCH06");
//    orgController.addLookup2ParentLink("name_2REG04","subjectName_2SCH06");
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

      public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
        OrganizationVO vo = (OrganizationVO)orgController.getLookupVO();
        controlName_1.setValue(vo.getName_1REG04());
        controlName_2.setValue(vo.getName_2REG04());
        filterOrgButton.setValue(vo.getProgressiveREG04());
      }

      public void beforeLookupAction(ValueObject parentVO) {
//        orgDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,empVO.getCompanyCodeSys01REG04());
        orgDataLocator.getLookupFrameParams().put(ApplicationConsts.SUBJECT_TYPE,ApplicationConsts.SUBJECT_ORGANIZATION);
      }

      public void forceValidate() {}

    });

    controlSubjectType.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (controlSubjectType.getComboBox().getSelectedIndex()==-1) {
          controlName_1.setValue("");
          controlName_2.setValue("");
          filterOrgButton.setValue(null);
          filterPeopleButton.setValue(null);
        }
        else if (e.getStateChange()==e.SELECTED) {
          cardLayout2.show(cardPanel2, (String) controlSubjectType.getValue());
        }
      }
    });

  }


  /**
   * Reload grid data.
   */
  public final void reloadData() {
    grid.reloadData();
  }


  private void jbInit() throws Exception {
    cardPanel2.setLayout(cardLayout2);
    cardPanel2.add(filterOrgButton,ApplicationConsts.SUBJECT_ORGANIZATION);
    cardPanel2.add(filterPeopleButton,ApplicationConsts.SUBJECT_PEOPLE);

    controlActType.setDomainId("ACTIVITY_TYPE");

    topPanel.setLayout(borderLayout2);
    filterPanel.setBorder(BorderFactory.createEtchedBorder());
    filterPanel.setLayout(gridBagLayout1);
    labelSubjectType.setText("subject type");
    labelManager.setText("manager");
    labelActType.setText("activityTypeSCH06");
    labelState.setText("activityStateSCH06");
    labelFromDate.setText("startDateSCH06");
    findButton.addActionListener(new ScheduledActivitiesPanel_findButton_actionAdapter(this));
    findButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("search"));

    controlSubjectType.setDomainId("SUBJECT_TYPE_2");
    filterOrgButton.setAllowOnlyNumbers(true);
    filterOrgButton.setCodBoxVisible(false);
    filterPeopleButton.setAllowOnlyNumbers(true);
    filterPeopleButton.setCodBoxVisible(false);
    controlName_1.setEnabled(false);
    controlName_2.setEnabled(false);
    controlEmpName_1.setEnabled(false);
    controlEmpName_2.setEnabled(false);
    controlActState.setDomainId("ACTIVITY_STATE");
    topPanel.add(buttonsPanel,BorderLayout.SOUTH);
    grid.setValueObjectClassName("org.jallinone.scheduler.activities.java.GridScheduledActivityVO");
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    insertButton.setText("insertButton1");
    reloadButton.setText("reloadButton1");
    deleteButton.setText("deleteButton1");
    grid.setDeleteButton(deleteButton);
    grid.setExportButton(exportButton);
    grid.setFunctionId("SCH06_SCHEDULED_ACT");
    grid.setLockedColumns(2);
    grid.setMaxSortedColumns(3);
    grid.setInsertButton(insertButton);
    grid.setNavBar(navigatorBar);
    grid.setReloadButton(reloadButton);
    colActPlace.setColumnFilterable(true);
    colActPlace.setColumnName("activityPlaceSCH06");
    colActPlace.setColumnSortable(true);
    colActPlace.setPreferredWidth(110);
    colCompanyCode.setColumnFilterable(true);
    colCompanyCode.setColumnName("companyCodeSys01SCH06");
    colCompanyCode.setColumnSortable(true);
    colCompanyCode.setSortVersus(Consts.ASC_SORTED);
    colCompanyCode.setSortingOrder(1);
    colManager1.setColumnName("managerName_1SCH06");
    colManager1.setColumnSortable(true);
    colManager1.setPreferredWidth(120);
    colManager2.setColumnName("managerName_2SCH06");
    colManager2.setPreferredWidth(120);
    colSubject1.setColumnFilterable(true);
    colSubject1.setColumnName("subjectName_1SCH06");
    colSubject1.setColumnSortable(true);
    colSubject1.setPreferredWidth(120);
    colSubject2.setColumnFilterable(true);
    colSubject2.setColumnName("subjectName_2SCH06");
    colSubject2.setColumnSortable(true);
    colSubject2.setPreferredWidth(120);
    colActDescr.setColumnName("descriptionSCH06");
    colActDescr.setPreferredWidth(300);
    colStartDate.setColumnFilterable(true);
    colStartDate.setColumnName("startDateSCH06");
    colStartDate.setColumnSortable(true);
    colStartDate.setPreferredWidth(120);
    colStartDate.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colStartDate.setSortingOrder(1);
    colEstimatedEndDate.setColumnFilterable(true);
    colEstimatedEndDate.setColumnName("estimatedEndDateSCH06");
    colEstimatedEndDate.setColumnSortable(true);
    colEstimatedEndDate.setPreferredWidth(120);
    colEndDate.setColumnName("endDateSCH06");
    colEndDate.setPreferredWidth(120);
    colActState.setPreferredWidth(70);
    colActState.setDomainId("ACTIVITY_STATE");
    colActState.setColumnName("activityStateSCH06");
    colPriority.setPreferredWidth(70);
    colPriority.setDomainId("ACTIVITY_PRIORITY");
    colPriority.setColumnName("prioritySCH06");
    this.setLayout(borderLayout1);
    this.add(grid, BorderLayout.CENTER);
    grid.getColumnContainer().add(colCompanyCode, null);
    this.add(topPanel, BorderLayout.NORTH);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(deleteButton, null);
    buttonsPanel.add(navigatorBar, null);
    buttonsPanel.add(exportButton, null);
    topPanel.add(filterPanel,  BorderLayout.CENTER);
    grid.getColumnContainer().add(colActDescr, null);
    grid.getColumnContainer().add(colStartDate, null);
    grid.getColumnContainer().add(colActState, null);
    grid.getColumnContainer().add(colPriority, null);
    grid.getColumnContainer().add(colActPlace, null);
    grid.getColumnContainer().add(colEstimatedEndDate, null);
    grid.getColumnContainer().add(colEndDate, null);
    grid.getColumnContainer().add(colManager1, null);
    grid.getColumnContainer().add(colManager2, null);
    grid.getColumnContainer().add(colSubject1, null);
    grid.getColumnContainer().add(colSubject2, null);
    if (showSubjectType) {
      filterPanel.add(labelSubjectType,          new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
      filterPanel.add(cardPanel2,           new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
              ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
      filterPanel.add(controlName_1,             new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 150, 0));
      filterPanel.add(controlName_2,            new GridBagConstraints(4, 0, 1, 1, 1.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 150, 0));
      filterPanel.add(controlSubjectType,  new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    }
    else {
      filterPanel.add(new JPanel(),  new GridBagConstraints(0, 0, 4, 1, 1.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    }
    filterPanel.add(labelManager,         new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(labelActType,        new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlActType,           new GridBagConstraints(1, 2, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlManagerCode,         new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlEmpName_2,          new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 150, 0));
    filterPanel.add(labelState,        new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlActState,         new GridBagConstraints(1, 3, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(labelFromDate,       new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlStartDate,      new GridBagConstraints(1, 4, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(findButton,        new GridBagConstraints(4, 3, 1, 2, 0.0, 0.0
            ,GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlEmpName_1,       new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 150, 0));
  }


  public GridControl getGrid() {
    return grid;
  }


  void findButton_actionPerformed(ActionEvent e) {
    if (controlStartDate.getValue()!=null)
      grid.getOtherGridParams().put(ApplicationConsts.START_DATE,controlStartDate.getValue());
    else
      grid.getOtherGridParams().remove(ApplicationConsts.START_DATE);
    if (controlManagerCode.getValue()!=null)
      grid.getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_REG04_MANAGER,progressiveManagerREG04);
    else
      grid.getOtherGridParams().remove(ApplicationConsts.PROGRESSIVE_REG04_MANAGER);
    if (controlSubjectType.getValue()!=null) {
      if (controlSubjectType.getValue().equals(ApplicationConsts.SUBJECT_ORGANIZATION))
        grid.getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_REG04_SUBJECT,filterOrgButton.getValue());
      else if (controlSubjectType.getValue().equals(ApplicationConsts.SUBJECT_PEOPLE))
        grid.getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_REG04_SUBJECT,filterPeopleButton.getValue());
    }
    else
      grid.getOtherGridParams().remove(ApplicationConsts.PROGRESSIVE_REG04_SUBJECT);
    if (controlActType.getValue()!=null)
      grid.getOtherGridParams().put(ApplicationConsts.ACTIVITY_TYPE,controlActType.getValue());
    else
      grid.getOtherGridParams().remove(ApplicationConsts.ACTIVITY_TYPE);
    if (controlActState.getValue()!=null)
      grid.getOtherGridParams().put(ApplicationConsts.ACTIVITY_STATE,controlActState.getValue());
    else
      grid.getOtherGridParams().remove(ApplicationConsts.ACTIVITY_STATE);

    grid.reloadData();
  }


  public void setButtonsEnabled(boolean enabled) {
    insertButton.setEnabled(enabled);
    deleteButton.setEnabled(enabled);
    exportButton.setEnabled(enabled);
  }




}

class ScheduledActivitiesPanel_findButton_actionAdapter implements java.awt.event.ActionListener {
  ScheduledActivitiesPanel adaptee;

  ScheduledActivitiesPanel_findButton_actionAdapter(ScheduledActivitiesPanel adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.findButton_actionPerformed(e);
  }
}
