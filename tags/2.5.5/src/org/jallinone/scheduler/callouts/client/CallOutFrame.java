package org.jallinone.scheduler.callouts.client;

import org.openswing.swing.mdi.client.InternalFrame;
import javax.swing.*;
import java.awt.*;
import org.openswing.swing.client.*;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.form.client.Form;
import java.math.BigDecimal;
import org.jallinone.commons.client.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.domains.java.*;
import org.jallinone.scheduler.callouts.java.CallOutTypeVO;
import org.jallinone.hierarchies.java.HierarchyLevelVO;
import java.util.ArrayList;
import org.openswing.swing.lookup.client.*;
import org.openswing.swing.tree.client.*;
import java.util.Collection;
import org.jallinone.registers.measure.java.MeasureVO;
import org.jallinone.registers.vat.java.VatVO;
import org.jallinone.scheduler.callouts.java.CallOutVO;
import java.awt.event.*;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.*;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.table.java.ServerGridDataLocator;
import org.openswing.swing.table.columns.client.*;
import org.jallinone.scheduler.callouts.java.CallOutPK;
import org.jallinone.warehouse.availability.client.*;
import org.jallinone.registers.currency.java.CurrencyVO;
import org.jallinone.items.java.ItemTypeVO;
import org.jallinone.scheduler.callouts.java.CallOutItemVO;
import org.openswing.swing.mdi.client.MDIFrame;
import org.jallinone.system.java.ButtonCompanyAuthorizations;
import org.jallinone.scheduler.callouts.java.CallOutMachineryVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Call-Out detail frame.</p>
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
public class CallOutFrame extends InternalFrame {

  JPanel buttonsPanel = new JPanel();
  JTabbedPane tab = new JTabbedPane();
  FlowLayout flowLayout1 = new FlowLayout();
  Form formPanel = new Form();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  InsertButton insertButton1 = new InsertButton();
  CopyButton copyButton1 = new CopyButton();
  EditButton editButton1 = new EditButton();
  SaveButton saveButton1 = new SaveButton();
  ReloadButton reloadButton1 = new ReloadButton();
  DeleteButton deleteButton1 = new DeleteButton();
  ComboBoxControl controlCompaniesCombo = new ComboBoxControl();
  LabelControl labelCompanyCode = new LabelControl();
  LabelControl labelCallOutCode = new LabelControl();
  TextControl controlCallOutCode = new TextControl();
  LabelControl labelDescr = new LabelControl();
  TextControl controlDescr = new TextControl();
  LabelControl labelCallOutType = new LabelControl();
  ComboBoxControl controlCallOutType = new ComboBoxControl();
  CodLookupControl controlLevel = new CodLookupControl();
  TextControl controlLevelDescr = new TextControl();
  LabelControl labelLevel = new LabelControl();

  LookupController levelController = new LookupController();
  LookupServerDataLocator levelDataLocator = new LookupServerDataLocator();
  TreeServerDataLocator treeLevelDataLocator = new TreeServerDataLocator();

  BorderLayout borderLayout1 = new BorderLayout();
  JPanel macsButtonsPanel = new JPanel();
  GridControl macsGrid = new GridControl();
  FlowLayout flowLayout2 = new FlowLayout();
  InsertButton insertButton2 = new InsertButton();
  SaveButton saveButton2 = new SaveButton();
  ReloadButton reloadButton2 = new ReloadButton();
  DeleteButton deleteButton2 = new DeleteButton();
  NavigatorBar navigatorBar2 = new NavigatorBar();
  NavigatorBar navigatorBar3 = new NavigatorBar();
  NavigatorBar navigatorBar4 = new NavigatorBar();

  JPanel itemsButtonsPanel = new JPanel();
  GridControl itemsGrid = new GridControl();
  FlowLayout flowLayout3 = new FlowLayout();
  FlowLayout flowLayout4 = new FlowLayout();
  InsertButton insertButton3 = new InsertButton();
  SaveButton saveButton3 = new SaveButton();
  ReloadButton reloadButton3 = new ReloadButton();
  DeleteButton deleteButton3 = new DeleteButton();

  InsertButton insertButton4 = new InsertButton();
  SaveButton saveButton4 = new SaveButton();
  ReloadButton reloadButton4 = new ReloadButton();
  DeleteButton deleteButton4 = new DeleteButton();


  JPanel resourcesPanel = new JPanel();
  JPanel tasksButtonsPanel = new JPanel();
  GridControl tasksGrid = new GridControl();

  CodLookupColumn colTaskCode = new CodLookupColumn();
  TextColumn colTaskDescr = new TextColumn();

  CodLookupColumn colMacCode = new CodLookupColumn();
  TextColumn colMacDescr = new TextColumn();

  CodLookupColumn colItemCode = new CodLookupColumn();
  TextColumn colItemDescr = new TextColumn();

  private ServerGridDataLocator tasksGridDataLocator = new ServerGridDataLocator();
  private ServerGridDataLocator macsGridDataLocator = new ServerGridDataLocator();
  private ServerGridDataLocator itemsGridDataLocator = new ServerGridDataLocator();

  LookupController tasksController = new LookupController();
  LookupServerDataLocator tasksDataLocator = new LookupServerDataLocator();

  LookupController macController = new LookupController();
  LookupServerDataLocator macDataLocator = new LookupServerDataLocator();

  LookupController itemController = new LookupController();
  LookupServerDataLocator itemDataLocator = new LookupServerDataLocator();

  GridBagLayout gridBagLayout2 = new GridBagLayout();

  TreeServerDataLocator itemTreeLevelDataLocator = new TreeServerDataLocator();
  ComboColumn colItemType = new ComboColumn();


  public CallOutFrame(CallOutController controller) {
    try {

      jbInit();
      setSize(750,550);
      setMinimumSize(new Dimension(750,550));


      // fill in the company combo...
      ClientApplet applet = ( (ApplicationClientFacade) MDIFrame.getInstance().getClientFacade()).getMainClass();
      ButtonCompanyAuthorizations bca = applet.getAuthorizations().getCompanyBa();
      ArrayList companiesList = bca.getCompaniesList("SCH10");
      Domain domain = new Domain("DOMAIN_SCH10");
      for (int i = 0; i < companiesList.size(); i++) {
        if (applet.getAuthorizations().getCompanyBa().isInsertEnabled(
            "SCH10",companiesList.get(i).toString()
        ))
          domain.addDomainPair(companiesList.get(i),companiesList.get(i).toString());
      }
      controlCompaniesCombo.setDomain(domain);


      controlLevel.setLookupController(levelController);
      levelController.setLookupDataLocator(levelDataLocator);
      levelController.setCodeSelectionWindow(levelController.TREE_FRAME);
      levelController.setFrameTitle("hierarchy");
      levelController.setAllowTreeLeafSelectionOnly(true);
      levelController.getLookupDataLocator().setNodeNameAttribute("descriptionSYS10");
      levelController.setLookupValueObjectClassName("org.jallinone.hierarchies.java.HierarchyLevelVO");
      levelController.addLookup2ParentLink("progressiveHIE01", "progressiveHie01SCH10");
      levelController.addLookup2ParentLink("descriptionSYS10", "levelDescriptionSYS10");
      levelDataLocator.setTreeDataLocator(treeLevelDataLocator);
      treeLevelDataLocator.setServerMethodName("loadHierarchy");


      init();

      formPanel.setFormController(controller);

      CustomizedControls customizedControls = new CustomizedControls(tab,formPanel,ApplicationConsts.ID_CALL_OUTS);


      tasksGrid.setController(new CallOutTasksController(tasksGrid));
      tasksGrid.setGridDataLocator(tasksGridDataLocator);
      tasksGridDataLocator.setServerMethodName("loadCallOutTasks");

      macsGrid.setController(new CallOutMachineriesController(macsGrid));
      macsGrid.setGridDataLocator(macsGridDataLocator);
      macsGridDataLocator.setServerMethodName("loadCallOutMachineries");

      itemsGrid.setController(new CallOutItemsController(itemsGrid));
      itemsGrid.setGridDataLocator(itemsGridDataLocator);
      itemsGridDataLocator.setServerMethodName("loadCallOutItems");


      // task lookup...
      tasksDataLocator.setGridMethodName("loadTasks");
      tasksDataLocator.setValidationMethodName("validateTaskCode");
      colTaskCode.setLookupController(tasksController);
      colTaskCode.setControllerMethodName("getTasksList");
      tasksController.setLookupDataLocator(tasksDataLocator);
      tasksController.setFrameTitle("tasks");
      tasksController.setLookupValueObjectClassName("org.jallinone.registers.task.java.TaskVO");
      tasksController.addLookup2ParentLink("taskCodeREG07", "taskCodeReg07SCH12");
      tasksController.addLookup2ParentLink("descriptionSYS10", "descriptionSYS10");
      tasksController.setAllColumnVisible(false);
      tasksController.setVisibleColumn("taskCodeREG07", true);
      tasksController.setVisibleColumn("descriptionSYS10", true);
      tasksController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {}

        public void beforeLookupAction(ValueObject parentVO) {
          tasksDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
          tasksDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
        }

        public void forceValidate() {}

      });

      // items lookup...
      itemDataLocator.setGridMethodName("loadItems");
      itemDataLocator.setValidationMethodName("validateItemCode");
      colItemCode.setLookupController(itemController);
      colItemCode.setControllerMethodName("getItemsList");
      itemController.setCodeSelectionWindow(itemController.TREE_GRID_FRAME);
      itemController.setLookupDataLocator(itemDataLocator);
      itemTreeLevelDataLocator.setServerMethodName("loadHierarchy");
      itemDataLocator.setTreeDataLocator(itemTreeLevelDataLocator);
      itemController.setFrameTitle("items");
      itemController.setLookupValueObjectClassName("org.jallinone.items.java.GridItemVO");
      itemController.addLookup2ParentLink("itemCodeITM01", "itemCodeItm01SCH14");
      itemController.addLookup2ParentLink("descriptionSYS10", "descriptionSYS10");
      itemController.setAllColumnVisible(false);
      itemController.setVisibleColumn("itemCodeITM01", true);
      itemController.setVisibleColumn("descriptionSYS10", true);
      itemController.setFramePreferedSize(new Dimension(600,500));
      itemController.setPreferredWidthColumn("descriptionSYS10",200);
      itemDataLocator.setNodeNameAttribute("descriptionSYS10");
      new CustomizedColumns(new BigDecimal(262),itemController);
      itemController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {}

        public void beforeLookupAction(ValueObject parentVO) {
          CallOutVO vo = (CallOutVO)formPanel.getVOModel().getValueObject();
          CallOutItemVO itemVO = (CallOutItemVO)itemsGrid.getVOListTableModel().getObjectForRow(itemsGrid.getSelectedRow());
          itemTreeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.PROGRESSIVE_HIE02,itemVO.getProgressiveHie02ITM01());
          itemTreeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01SCH10());
          itemDataLocator.getLookupFrameParams().put(ApplicationConsts.PROGRESSIVE_HIE02,itemVO.getProgressiveHie02ITM01());
          itemDataLocator.getLookupValidationParameters().put(ApplicationConsts.PROGRESSIVE_HIE02,itemVO.getProgressiveHie02ITM01());
          itemDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01SCH10());
          itemDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01SCH10());
        }

        public void forceValidate() {}

      });



      // machineries lookup...
      macDataLocator.setGridMethodName("loadMachineries");
      macDataLocator.setValidationMethodName("validateMachineryCode");
      colMacCode.setLookupController(macController);
      colMacCode.setControllerMethodName("getMachineriesList");
      macController.setLookupDataLocator(macDataLocator);
      macController.setFrameTitle("machineries");
      macController.setLookupValueObjectClassName("org.jallinone.production.machineries.java.MachineryVO");
      macController.addLookup2ParentLink("machineryCodePRO03", "machineryCodePro03SCH13");
      macController.addLookup2ParentLink("descriptionSYS10", "descriptionSYS10");
      macController.setAllColumnVisible(false);
      macController.setVisibleColumn("machineryCodePRO03", true);
      macController.setVisibleColumn("descriptionSYS10", true);
      macController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {}

        public void beforeLookupAction(ValueObject parentVO) {
          CallOutVO vo = (CallOutVO)formPanel.getVOModel().getValueObject();
          macDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01SCH10());
          macDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01SCH10());
        }

        public void forceValidate() {}

      });

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * Callback method called when the data loading is completed.
   * @param error <code>true</code> if an error occours during data loading, <code>false</code> if data loading is successfully completed
   */
  public final void loadDataCompleted(boolean error,CallOutPK pk) {
    CallOutVO vo = (CallOutVO)formPanel.getVOModel().getValueObject();

    tasksGrid.getOtherGridParams().put(ApplicationConsts.CALL_OUT_PK,pk);
    tasksGrid.reloadData();

    macsGrid.getOtherGridParams().put(ApplicationConsts.CALL_OUT_PK,pk);
    macsGrid.reloadData();

    itemsGrid.getOtherGridParams().put(ApplicationConsts.CALL_OUT_PK,pk);
    itemsGrid.reloadData();
  }


  /**
   * Retrieve call-out types and fill in the call-out types combo box.
   */
  private void init() {
    Response res = ClientUtils.getData("loadItemTypes",new GridParams());
    Domain d = new Domain("ITEM_TYPES");
    if (!res.isError()) {
      ItemTypeVO vo = null;
      java.util.List list = ((VOListResponse)res).getRows();
      for(int i=0;i<list.size();i++) {
        vo = (ItemTypeVO)list.get(i);
        d.addDomainPair(vo.getProgressiveHie02ITM02(),vo.getDescriptionSYS10());
      }
    }
    colItemType.setDomain(d);

    res = ClientUtils.getData("loadCallOutTypes",new GridParams());
    d = new Domain("CALL_OUT_TYPES");
    if (!res.isError()) {
      CallOutTypeVO vo = null;
      java.util.List list = ((VOListResponse)res).getRows();
      for(int i=0;i<list.size();i++) {
        vo = (CallOutTypeVO)list.get(i);
        d.addDomainPair(vo.getProgressiveHie02SCH11(),vo.getDescriptionSYS10());
      }
    }
    controlCallOutType.setDomain(d);
    controlCallOutType.getComboBox().addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == e.SELECTED) {
//          formPanel.getBinding((ValueObject)formPanel.getVOModel().getValueObject(),"progressiveHie02SCH10").push();
          controlLevel.getCodBox().setText(null);
          controlLevelDescr.setText("");
          treeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.PROGRESSIVE_HIE02, controlCallOutType.getValue());
        }
      }
    });
    if (d.getDomainPairList().length==1)
      controlCallOutType.getComboBox().setSelectedIndex(0);
    else
      controlCallOutType.getComboBox().setSelectedIndex(-1);

  }


  private void jbInit() throws Exception {
    macsGrid.setVisibleStatusPanel(false);
    itemsGrid.setMaxNumberOfRowsOnInsert(50);
    macsGrid.setMaxNumberOfRowsOnInsert(50);
    tasksGrid.setMaxNumberOfRowsOnInsert(50);

    this.setTitle(ClientSettings.getInstance().getResources().getResource("call-out detail"));
    formPanel.setVOClassName("org.jallinone.scheduler.callouts.java.CallOutVO");
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    formPanel.setLayout(gridBagLayout1);
    insertButton1.setText("insertButton1");
    editButton1.setText("editButton1");
    saveButton1.setText("saveButton1");
    reloadButton1.setText("reloadButton1");
    deleteButton1.setText("deleteButton1");
    formPanel.setInsertButton(insertButton1);
    formPanel.setCopyButton(copyButton1);
    formPanel.setEditButton(editButton1);
    formPanel.setReloadButton(reloadButton1);
    formPanel.setDeleteButton(deleteButton1);
    formPanel.setSaveButton(saveButton1);
    formPanel.setFunctionId("SCH10");
    labelCompanyCode.setText("companyCode");
    labelCallOutCode.setText("callOutCodeSCH10");
    labelDescr.setText("descriptionSYS10");
    labelCallOutType.setText("call-out type");
    labelLevel.setText("level");
    controlCompaniesCombo.setAttributeName("companyCodeSys01SCH10");
    controlCompaniesCombo.setCanCopy(true);
    controlCompaniesCombo.setLinkLabel(labelCompanyCode);
    controlCompaniesCombo.setRequired(true);
    controlCompaniesCombo.setEnabledOnEdit(false);
    controlCallOutCode.setAttributeName("callOutCodeSCH10");
    controlCallOutCode.setLinkLabel(labelCallOutCode);
    controlCallOutCode.setMaxCharacters(20);
    controlCallOutCode.setRequired(true);
    controlCallOutCode.setTrimText(true);
    controlCallOutCode.setUpperCase(true);
    controlCallOutCode.setEnabledOnEdit(false);
    controlDescr.setAttributeName("descriptionSYS10");
    controlDescr.setCanCopy(true);
    controlDescr.setLinkLabel(labelDescr);
    controlDescr.setRequired(true);
    controlCallOutType.setAttributeName("progressiveHie02SCH10");
    controlCallOutType.setCanCopy(true);
    controlCallOutType.setLinkLabel(labelCallOutType);
    controlCallOutType.setRequired(true);
    controlLevel.setAllowOnlyNumbers(true);
    controlLevel.setAttributeName("progressiveHie01SCH10");
    controlLevel.setCanCopy(true);
    controlLevel.setCodBoxVisible(false);
    controlLevel.setLinkLabel(labelLevel);
    controlLevel.setRequired(true);
    controlLevelDescr.setAttributeName("levelDescriptionSYS10");
    controlLevelDescr.setCanCopy(true);
    controlLevelDescr.setEnabledOnInsert(false);
    controlLevelDescr.setEnabledOnEdit(false);

    macsGrid.setAutoLoadData(false);
    macsGrid.setDeleteButton(deleteButton3);
    macsGrid.setFunctionId("SCH10");
    macsGrid.setInsertButton(insertButton3);
    macsGrid.setNavBar(navigatorBar3);
    macsGrid.setReloadButton(reloadButton3);
    macsGrid.setSaveButton(saveButton3);
    macsButtonsPanel.setLayout(flowLayout3);
    macsGrid.setValueObjectClassName("org.jallinone.scheduler.callouts.java.CallOutMachineryVO");
    macsGrid.setVisibleStatusPanel(false);

    itemsGrid.setAutoLoadData(false);
    itemsGrid.setDeleteButton(deleteButton4);
    itemsGrid.setFunctionId("SCH10");
    itemsGrid.setInsertButton(insertButton4);
    itemsGrid.setNavBar(navigatorBar4);
    itemsGrid.setReloadButton(reloadButton4);
    itemsGrid.setSaveButton(saveButton4);
    itemsButtonsPanel.setLayout(flowLayout4);
    itemsGrid.setValueObjectClassName("org.jallinone.scheduler.callouts.java.CallOutItemVO");
    itemsGrid.setVisibleStatusPanel(false);


    flowLayout2.setAlignment(FlowLayout.LEFT);
    insertButton2.setText("insertButton2");
    saveButton2.setText("saveButton2");
    reloadButton2.setText("reloadButton2");
    deleteButton2.setText("deleteButton2");
    resourcesPanel.setLayout(gridBagLayout2);
    tasksGrid.setAutoLoadData(false);
    tasksGrid.setDeleteButton(deleteButton2);
    tasksGrid.setFunctionId("SAL01");
    tasksGrid.setInsertButton(insertButton2);
    tasksGrid.setNavBar(navigatorBar2);
    tasksGrid.setReloadButton(reloadButton2);
    tasksGrid.setSaveButton(saveButton2);
    tasksGrid.setValueObjectClassName("org.jallinone.scheduler.callouts.java.CallOutTaskVO");
    tasksGrid.setVisibleStatusPanel(false);
    tasksButtonsPanel.setLayout(flowLayout3);
    flowLayout3.setAlignment(FlowLayout.LEFT);
    insertButton3.setText("insertButton3");
    reloadButton3.setText("reloadButton3");
    deleteButton3.setText("deleteButton3");
    saveButton3.setText("saveButton3");
    colTaskCode.setColumnFilterable(true);
    colTaskCode.setColumnSortable(true);
    colTaskCode.setEditableOnInsert(true);
    colTaskCode.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colTaskCode.setMaxCharacters(20);
    colTaskCode.setColumnName("taskCodeReg07SCH12");
    colTaskDescr.setPreferredWidth(300);
    colTaskDescr.setColumnName("descriptionSYS10");
    colTaskDescr.setHeaderColumnName("descriptionSYS10");
    flowLayout4.setAlignment(FlowLayout.LEFT);
    colItemType.setEditableOnInsert(true);
    colMacDescr.setEditableOnEdit(false);
    this.getContentPane().add(buttonsPanel,  BorderLayout.NORTH);
    this.getContentPane().add(tab, BorderLayout.CENTER);
    tab.add(formPanel,   "detailPanel");
    formPanel.add(labelCompanyCode,           new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(controlCompaniesCombo,              new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(labelCallOutCode,           new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(labelDescr,            new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(controlDescr,             new GridBagConstraints(3, 1, 2, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(controlLevelDescr,             new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(labelCallOutType,           new GridBagConstraints(0, 2, 1, 1, 0.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(controlCallOutType,            new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(controlLevel,              new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    buttonsPanel.add(insertButton1, null);
    buttonsPanel.add(copyButton1, null);
    buttonsPanel.add(editButton1, null);
    buttonsPanel.add(saveButton1, null);
    buttonsPanel.add(reloadButton1, null);
    buttonsPanel.add(deleteButton1, null);

    macsButtonsPanel.add(insertButton3, null);
    macsButtonsPanel.add(saveButton3, null);
    macsButtonsPanel.add(reloadButton3, null);
    macsButtonsPanel.add(deleteButton3, null);
    macsButtonsPanel.add(navigatorBar3, null);

    colMacCode.setColumnFilterable(true);
    colMacCode.setColumnSortable(true);
    colMacCode.setEditableOnInsert(true);
    colMacCode.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colMacCode.setMaxCharacters(20);
    colMacCode.setColumnName("machineryCodePro03SCH13");

    colMacDescr.setColumnDuplicable(true);
    colMacDescr.setColumnFilterable(true);
    colMacDescr.setColumnName("descriptionSYS10");
    colMacDescr.setColumnSortable(true);
    colMacDescr.setEditableOnInsert(true);
    colMacDescr.setPreferredWidth(300);

    tab.add(resourcesPanel,    "resoucesPanel");

    resourcesPanel.add(tasksButtonsPanel,   new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    resourcesPanel.add(tasksGrid,   new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    tasksButtonsPanel.add(insertButton2, null);
    tasksButtonsPanel.add(saveButton2, null);
    tasksButtonsPanel.add(reloadButton2, null);
    tasksButtonsPanel.add(deleteButton2, null);
    tasksButtonsPanel.add(navigatorBar2, null);

    tab.setTitleAt(0,ClientSettings.getInstance().getResources().getResource("call-out detail"));
    tab.setTitleAt(1,ClientSettings.getInstance().getResources().getResource("call-out resources"));

    tasksGrid.getColumnContainer().add(colTaskCode, null);
    tasksGrid.getColumnContainer().add(colTaskDescr, null);
    formPanel.add(controlCallOutCode,    new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(labelLevel,   new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    macsGrid.getColumnContainer().add(colMacCode, null);
    macsGrid.getColumnContainer().add(colMacDescr, null);

    resourcesPanel.add(macsButtonsPanel,   new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    resourcesPanel.add(macsGrid,   new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

    itemsButtonsPanel.add(insertButton4, null);
    itemsButtonsPanel.add(saveButton4, null);
    itemsButtonsPanel.add(reloadButton4, null);
    itemsButtonsPanel.add(deleteButton4, null);
    itemsButtonsPanel.add(navigatorBar4, null);

    colItemCode.setColumnFilterable(true);
    colItemCode.setColumnSortable(true);
    colItemCode.setEditableOnInsert(true);
    colItemCode.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colItemCode.setMaxCharacters(20);
    colItemCode.setColumnName("itemCodeItm01SCH14");

    colItemDescr.setColumnDuplicable(true);
    colItemDescr.setColumnFilterable(true);
    colItemDescr.setColumnName("descriptionSYS10");
    colItemDescr.setColumnSortable(true);
    colItemDescr.setEditableOnInsert(true);
    colItemDescr.setPreferredWidth(300);

    colItemType.setColumnName("progressiveHie02ITM01");
    itemsGrid.getColumnContainer().add(colItemType, null);
    itemsGrid.getColumnContainer().add(colItemCode, null);
    itemsGrid.getColumnContainer().add(colItemDescr, null);

    resourcesPanel.add(itemsButtonsPanel,   new GridBagConstraints(0, 4, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    resourcesPanel.add(itemsGrid,   new GridBagConstraints(0, 5, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

  }


  public Form getFormPanel() {
    return formPanel;
  }



  public final void setButtonsEnabled(boolean enabled) {
    insertButton2.setEnabled(enabled);
    deleteButton2.setEnabled(enabled);

    insertButton3.setEnabled(enabled);
    deleteButton3.setEnabled(enabled);

    insertButton4.setEnabled(enabled);
    deleteButton4.setEnabled(enabled);
  }


  public GridControl getMacsGrid() {
    return macsGrid;
  }


  public GridControl getTasksGrid() {
    return tasksGrid;
  }


  public GridControl getItemsGrid() {
    return itemsGrid;
  }



}
