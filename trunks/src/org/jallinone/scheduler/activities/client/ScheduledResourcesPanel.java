
package org.jallinone.scheduler.activities.client;

import org.openswing.swing.mdi.client.InternalFrame;
import org.openswing.swing.tree.client.*;
import java.awt.*;
import org.openswing.swing.mdi.client.MDIFrame;
import javax.swing.*;
import org.openswing.swing.client.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.form.client.*;
import org.jallinone.commons.client.*;
import java.math.BigDecimal;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.scheduler.activities.java.*;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.message.receive.java.*;
import java.util.Collection;
import org.openswing.swing.form.model.client.VOModel;
import java.awt.event.*;
import org.openswing.swing.lookup.client.*;
import org.openswing.swing.table.java.*;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.domains.java.Domain;
import org.jallinone.items.java.ItemTypeVO;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.jallinone.scheduler.activities.java.ScheduledActivityVO;
import org.jallinone.employees.java.GridEmployeeVO;
import org.jallinone.documents.java.GridDocumentVO;
import java.util.HashSet;
import org.jallinone.scheduler.gantt.client.ActivityGanttController;
import org.jallinone.documents.java.DocumentTypeVO;
import org.jallinone.items.spareparts.client.SparePartsCatalogueFrame;
import org.jallinone.items.spareparts.client.SparePartsCatalogueCallbacks;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Panel used with a scheduled activity to allocate resources, linke items, employees, machineries and to attach documents.</p>
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
public class ScheduledResourcesPanel extends JPanel {

  BorderLayout borderLayout1 = new BorderLayout();
  GridControl itemsGrid = new GridControl();
  NavigatorBar navigatorBar2 = new NavigatorBar();
  JPanel tasksButtonsPanel = new JPanel();
  NavigatorBar navigatorBar3 = new NavigatorBar();
  JPanel macsButtonsPanel = new JPanel();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  SaveButton saveButton4 = new SaveButton();
  GridControl tasksGrid = new GridControl();
  InsertButton insertButton4 = new InsertButton();
  InsertButton insertButton2 = new InsertButton();
  JPanel resourcesPanel = new JPanel();
  ReloadButton reloadButton3 = new ReloadButton();
  SaveButton saveButton2 = new SaveButton();
  DeleteButton deleteButton4 = new DeleteButton();
  TextColumn colItemDescr = new TextColumn();
  ReloadButton reloadButton4 = new ReloadButton();
  DeleteButton deleteButton3 = new DeleteButton();
  NavigatorBar navigatorBar4 = new NavigatorBar();
  FlowLayout flowLayout3 = new FlowLayout();
  GridControl macsGrid = new GridControl();
  CodLookupColumn colMacCode = new CodLookupColumn();
  TextColumn colTaskDescr = new TextColumn();
  DeleteButton deleteButton2 = new DeleteButton();
  TextColumn colMacDescr = new TextColumn();
  FlowLayout flowLayout4 = new FlowLayout();
  ComboColumn colItemType = new ComboColumn();
  ReloadButton reloadButton2 = new ReloadButton();
  SaveButton saveButton3 = new SaveButton();
  InsertButton insertButton3 = new InsertButton();
  CodLookupColumn colItemCode = new CodLookupColumn();
  JPanel itemsButtonsPanel = new JPanel();
  LookupServerDataLocator docDataLocator = new LookupServerDataLocator();
  LookupServerDataLocator macDataLocator = new LookupServerDataLocator();
  ServerGridDataLocator tasksGridDataLocator = new ServerGridDataLocator();
  LookupServerDataLocator itemDataLocator = new LookupServerDataLocator();
  TreeServerDataLocator itemTreeLevelDataLocator = new TreeServerDataLocator();
  LookupController itemController = new LookupController();
  ServerGridDataLocator itemsGridDataLocator = new ServerGridDataLocator();
  ServerGridDataLocator macsGridDataLocator = new ServerGridDataLocator();
  ServerGridDataLocator docsGridDataLocator = new ServerGridDataLocator();
  LookupServerDataLocator tasksDataLocator = new LookupServerDataLocator();


  LookupServerDataLocator levelDataLocator = new LookupServerDataLocator();
  TreeServerDataLocator treeLevelDataLocator = new TreeServerDataLocator();
  ComboColumn colDocType = new ComboColumn();


  LookupController docController = new LookupController();
  LookupController macController = new LookupController();
  LookupController tasksController = new LookupController();
  SaveButton saveButton5 = new SaveButton();
  InsertButton insertButton5 = new InsertButton();
  DeleteButton deleteButton5 = new DeleteButton();
  ReloadButton reloadButton5 = new ReloadButton();
  NavigatorBar navigatorBar5 = new NavigatorBar();
  FlowLayout flowLayout5 = new FlowLayout();
  JPanel docsButtonsPanel1 = new JPanel();
  GridControl docsGrid = new GridControl();
  CodLookupColumn colTaskCode = new CodLookupColumn();
  CodLookupColumn colEmployeeCode = new CodLookupColumn();
  TextColumn colName1 = new TextColumn();
  TextColumn colName2 = new TextColumn();
  DateTimeColumn colStartDate = new DateTimeColumn();
  DateTimeColumn colEndDate = new DateTimeColumn();
  IntegerColumn colDuration = new IntegerColumn();
  TextColumn colNote = new TextColumn();
  DateTimeColumn colMacStartDate = new DateTimeColumn();
  DateTimeColumn colMacEndDate = new DateTimeColumn();
  IntegerColumn colMacDuration = new IntegerColumn();
  TextColumn colMacNote = new TextColumn();
  DecimalColumn colQty = new DecimalColumn();
  CodLookupColumn colDocProg = new CodLookupColumn();
  TextColumn collDocName = new TextColumn();
  LookupServerDataLocator empDataLocator = new LookupServerDataLocator();
  LookupController empController = new LookupController();

  private ScheduledActivityVO actVO = null;
  EditButton editButton2 = new EditButton();
  EditButton editButton3 = new EditButton();
  EditButton editButton4 = new EditButton();

  JPanel docPanel = new JPanel();
  GridBagLayout gridBagLayout3 = new GridBagLayout();
  JTabbedPane tab = new JTabbedPane();
  private GenericButtonController genericButtonController = null;
  ButtonColumn colGantt = new ButtonColumn();
  GenericButton buttonSparePartsCat = new GenericButton(new ImageIcon(ClientUtils.getImage("hierarchies.gif")));
	private SparePartsCatalogueFrame frame = null;
	private String companyCodeSys01SCH03 = null;
	private String itemCodeItm01SCH03 = null;
	private BigDecimal progressiveHie02ITM01;


  public ScheduledResourcesPanel(GenericButtonController genericButtonController) {
    this.genericButtonController = genericButtonController;
    try {
      jbInit();

      tasksGrid.setController(new ScheduledEmployeesController(tasksGrid,this));
      tasksGrid.setGridDataLocator(tasksGridDataLocator);
      tasksGridDataLocator.setServerMethodName("loadScheduledEmployees");

      macsGrid.setController(new ScheduledMachineriesController(macsGrid,this));
      macsGrid.setGridDataLocator(macsGridDataLocator);
      macsGridDataLocator.setServerMethodName("loadScheduledMachineries");

      itemsGrid.setController(new ScheduledItemsController(itemsGrid,this));
      itemsGrid.setGridDataLocator(itemsGridDataLocator);
      itemsGridDataLocator.setServerMethodName("loadScheduledItems");

      docsGrid.setController(new AttachedDocsController(docsGrid,this));
      docsGrid.setGridDataLocator(docsGridDataLocator);
      docsGridDataLocator.setServerMethodName("loadAttachedDocs");


      // task lookup...
      tasksDataLocator.setGridMethodName("loadTasks");
      tasksDataLocator.setValidationMethodName("validateTaskCode");
      colTaskCode.setLookupController(tasksController);
      colTaskCode.setControllerMethodName("getTasksList");
      tasksController.setLookupDataLocator(tasksDataLocator);
      tasksController.setFrameTitle("tasks");
      tasksController.setLookupValueObjectClassName("org.jallinone.registers.task.java.TaskVO");
      tasksController.addLookup2ParentLink("taskCodeREG07", "taskCodeREG07");
      tasksController.addLookup2ParentLink("descriptionSYS10", "descriptionSYS10");
      tasksController.setAllColumnVisible(false);
      tasksController.setVisibleColumn("taskCodeREG07", true);
      tasksController.setVisibleColumn("descriptionSYS10", true);
      tasksController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          ScheduledEmployeeVO vo = (ScheduledEmployeeVO)parentVO;
          vo.setEmployeeCodeSCH01(null);
          vo.setName_1REG04(null);
          vo.setName_2REG04(null);
        }

        public void beforeLookupAction(ValueObject parentVO) {
          tasksDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,actVO.getCompanyCodeSys01SCH06());
          tasksDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,actVO.getCompanyCodeSys01SCH06());
        }

        public void forceValidate() {}

      });

      // employees lookup...
      empDataLocator.setGridMethodName("loadEmployees");
      empDataLocator.setValidationMethodName("validateEmployeeCode");
      colEmployeeCode.setLookupController(empController);
      colEmployeeCode.setControllerMethodName("getEmployeesList");
      empController.setLookupDataLocator(empDataLocator);
      empController.setFrameTitle("employees");
      empController.setLookupValueObjectClassName("org.jallinone.employees.java.GridEmployeeVO");
      empController.addLookup2ParentLink("employeeCodeSCH01", "employeeCodeSCH01");
      empController.addLookup2ParentLink("name_1REG04", "name_1REG04");
      empController.addLookup2ParentLink("name_2REG04", "name_2REG04");
      empController.setAllColumnVisible(false);
      empController.setVisibleColumn("employeeCodeSCH01", true);
      empController.setVisibleColumn("name_1REG04", true);
      empController.setVisibleColumn("name_2REG04", true);
      empController.setPreferredWidthColumn("name_1REG04",150);
      empController.setPreferredWidthColumn("name_2REG04",150);
      empController.setFramePreferedSize(new Dimension(430,400));
      empController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          GridEmployeeVO empVO = (GridEmployeeVO)empController.getLookupVO();
          ScheduledEmployeeVO vo = (ScheduledEmployeeVO)parentVO;
          vo.setProgressiveReg04SCH07(empVO.getProgressiveReg04SCH01());
        }

        public void beforeLookupAction(ValueObject parentVO) {
          ScheduledEmployeeVO vo = (ScheduledEmployeeVO)parentVO;
          empDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,actVO.getCompanyCodeSys01SCH06());
          empDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,actVO.getCompanyCodeSys01SCH06());
          empDataLocator.getLookupFrameParams().put(ApplicationConsts.TASK_CODE_REG07,vo.getTaskCodeREG07());
          empDataLocator.getLookupValidationParameters().put(ApplicationConsts.TASK_CODE_REG07,vo.getTaskCodeREG07());
        }

        public void forceValidate() { }

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
      itemController.addLookup2ParentLink("itemCodeITM01", "itemCodeItm01SCH15");
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
          ScheduledItemVO itemVO = (ScheduledItemVO)itemsGrid.getVOListTableModel().getObjectForRow(itemsGrid.getSelectedRow());
          itemTreeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.PROGRESSIVE_HIE02,itemVO.getProgressiveHie02ITM01());
          itemTreeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.COMPANY_CODE_SYS01,itemVO.getCompanyCodeSys01SCH15());
          itemDataLocator.getLookupFrameParams().put(ApplicationConsts.PROGRESSIVE_HIE02,itemVO.getProgressiveHie02ITM01());
          itemDataLocator.getLookupValidationParameters().put(ApplicationConsts.PROGRESSIVE_HIE02,itemVO.getProgressiveHie02ITM01());
          itemDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,itemVO.getCompanyCodeSys01SCH15());
          itemDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,itemVO.getCompanyCodeSys01SCH15());
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
      macController.addLookup2ParentLink("machineryCodePRO03", "machineryCodePro03SCH09");
      macController.addLookup2ParentLink("descriptionSYS10", "descriptionSYS10");
      macController.setAllColumnVisible(false);
      macController.setVisibleColumn("machineryCodePRO03", true);
      macController.setVisibleColumn("descriptionSYS10", true);
      macController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
        }

        public void beforeLookupAction(ValueObject parentVO) {
          macDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,actVO.getCompanyCodeSys01SCH06());
          macDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,actVO.getCompanyCodeSys01SCH06());
        }

        public void forceValidate() {}

      });


      // documents lookup...
      docDataLocator.setGridMethodName("loadDocuments");
      docDataLocator.setValidationMethodName("");
      colDocProg.setLookupController(docController);
//      colDocProg.setControllerMethodName("getDocumentsList");

      docController.setCodeSelectionWindow(docController.TREE_GRID_FRAME);
      treeLevelDataLocator.setServerMethodName("loadHierarchy");
      docDataLocator.setTreeDataLocator(treeLevelDataLocator);
      docDataLocator.setNodeNameAttribute("descriptionSYS10");


      docController.setLookupDataLocator(docDataLocator);
      docController.setFrameTitle("documents");
      docController.setLookupValueObjectClassName("org.jallinone.documents.java.GridDocumentVO");
      docController.addLookup2ParentLink("descriptionDOC14", "descriptionDOC14");
      docController.setAllColumnVisible(false);
      docController.setPreferredWidthColumn("descriptionDOC14",400);
      docController.setVisibleColumn("descriptionDOC14", true);
      docController.setFramePreferedSize(new Dimension(750,500));
      docController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          GridDocumentVO docVO = (GridDocumentVO)docController.getLookupVO();
          ActAttachedDocVO vo = (ActAttachedDocVO)parentVO;
          vo.setProgressiveDoc14SCH08(docVO.getProgressiveDOC14());
          vo.setProgressiveHie01SCH08(docVO.getProgressiveHie01DOC17());
          vo.setProgressiveHie02HIE01(docVO.getProgressiveHie02HIE01());
        }

        public void beforeLookupAction(ValueObject parentVO) {
          ActAttachedDocVO vo = (ActAttachedDocVO)parentVO;
          docDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,actVO.getCompanyCodeSys01SCH06());
          docDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,actVO.getCompanyCodeSys01SCH06());
          treeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.PROGRESSIVE_HIE02,vo.getProgressiveHie02HIE01());
        }

        public void forceValidate() {}

      });


      // set item types in call-out items grid...
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


      // set document types in doc.type column...
      res = ClientUtils.getData("loadDocumentTypes",new GridParams());
      d = new Domain("DOC_TYPES_DOC16");
      if (!res.isError()) {
        DocumentTypeVO vo = null;
        java.util.List list = ((VOListResponse)res).getRows();
        for(int i=0;i<list.size();i++) {
          vo = (DocumentTypeVO)list.get(i);
          d.addDomainPair(vo.getProgressiveHie02DOC16(),vo.getDescriptionSYS10());
        }
      }
      colDocType.setDomain(d);


      HashSet buttonsToDisable = new HashSet();
      buttonsToDisable.add(editButton2);
      buttonsToDisable.add(deleteButton2);
      buttonsToDisable.add(insertButton2);
      buttonsToDisable.add(reloadButton2);
      tasksGrid.addButtonsNotEnabled(buttonsToDisable,genericButtonController);

      buttonsToDisable = new HashSet();
      buttonsToDisable.add(editButton3);
      buttonsToDisable.add(deleteButton3);
      buttonsToDisable.add(insertButton3);
      buttonsToDisable.add(reloadButton3);
      macsGrid.addButtonsNotEnabled(buttonsToDisable,genericButtonController);

      buttonsToDisable = new HashSet();
      buttonsToDisable.add(editButton4);
      buttonsToDisable.add(deleteButton4);
      buttonsToDisable.add(insertButton4);
      buttonsToDisable.add(reloadButton4);
      itemsGrid.addButtonsNotEnabled(buttonsToDisable,genericButtonController);

      buttonsToDisable = new HashSet();
      buttonsToDisable.add(deleteButton5);
      buttonsToDisable.add(insertButton5);
      buttonsToDisable.add(reloadButton5);
      docsGrid.addButtonsNotEnabled(buttonsToDisable,genericButtonController);

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
	   buttonSparePartsCat.setToolTipText(ClientSettings.getInstance().getResources().getResource("spare parts catalogue"));

    docPanel.setLayout(gridBagLayout3);
    docsGrid.setMaxNumberOfRowsOnInsert(50);
    tasksGrid.setMaxNumberOfRowsOnInsert(50);
    itemsGrid.setMaxNumberOfRowsOnInsert(50);
    macsGrid.setMaxNumberOfRowsOnInsert(50);

    colGantt.addActionListener(new ScheduledResourcesPanel_ganttButton_actionAdapter(this));
    colGantt.setPreferredWidth(40);

    itemsGrid.setVisibleStatusPanel(false);
    itemsGrid.setValueObjectClassName("org.jallinone.scheduler.activities.java.ScheduledItemVO");
    itemsGrid.setSaveButton(saveButton4);
    itemsGrid.setReloadButton(reloadButton4);
    itemsGrid.setNavBar(navigatorBar4);
    itemsGrid.setInsertButton(insertButton4);
    //itemsGrid.setFunctionId("SCH15");
    itemsGrid.setDeleteButton(deleteButton4);
    itemsGrid.setEditButton(editButton4);
    itemsGrid.setAutoLoadData(false);
    this.setLayout(borderLayout1);
    tasksButtonsPanel.setLayout(flowLayout3);
    macsButtonsPanel.setLayout(flowLayout3);
    tasksGrid.setAutoLoadData(false);
    tasksGrid.setDeleteButton(deleteButton2);
    tasksGrid.setEditButton(editButton2);
    //tasksGrid.setFunctionId("SCH07");
    tasksGrid.setInsertButton(insertButton2);
    tasksGrid.setNavBar(navigatorBar2);
    tasksGrid.setReloadButton(reloadButton2);
    tasksGrid.setSaveButton(saveButton2);
    tasksGrid.setValueObjectClassName("org.jallinone.scheduler.activities.java.ScheduledEmployeeVO");
    tasksGrid.setVisibleStatusPanel(false);
    insertButton2.setText("insertButton2");
    resourcesPanel.setLayout(gridBagLayout2);
    reloadButton3.setText("reloadButton3");
    saveButton2.setText("saveButton2");
    colItemDescr.setColumnDuplicable(true);
    colItemDescr.setColumnFilterable(true);
    colItemDescr.setColumnName("descriptionSYS10");
    colItemDescr.setColumnSortable(true);
    colItemDescr.setHeaderColumnName("itemDescriptionSYS10");
    colItemDescr.setPreferredWidth(350);
    deleteButton3.setText("deleteButton3");
    flowLayout3.setAlignment(FlowLayout.LEFT);
    macsGrid.setVisibleStatusPanel(false);
    macsGrid.setAutoLoadData(false);
    macsGrid.setDeleteButton(deleteButton3);
    macsGrid.setEditButton(editButton3);
    //macsGrid.setFunctionId("SCH09");
    macsGrid.setInsertButton(insertButton3);
    macsGrid.setNavBar(navigatorBar3);
    macsGrid.setReloadButton(reloadButton3);
    macsGrid.setSaveButton(saveButton3);
    macsGrid.setValueObjectClassName("org.jallinone.scheduler.activities.java.ScheduledMachineriesVO");
    macsGrid.setVisibleStatusPanel(false);
    colMacCode.setColumnFilterable(true);
    colMacCode.setColumnSortable(true);
    colMacCode.setEditableOnEdit(false);
    colMacCode.setEditableOnInsert(true);
    colMacCode.setPreferredWidth(90);
    colMacCode.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colMacCode.setMaxCharacters(20);
    colMacCode.setColumnName("machineryCodePro03SCH09");
    colTaskDescr.setPreferredWidth(200);
    colTaskDescr.setColumnName("descriptionSYS10");
    colTaskDescr.setHeaderColumnName("taskDescription");
    deleteButton2.setText("deleteButton2");
    colMacDescr.setColumnDuplicable(true);
    colMacDescr.setColumnFilterable(true);
    colMacDescr.setColumnName("descriptionSYS10");
    colMacDescr.setColumnSortable(true);
    colMacDescr.setEditableOnInsert(false);
    colMacDescr.setHeaderColumnName("machineryDescription");
    colMacDescr.setPreferredWidth(200);
    flowLayout4.setAlignment(FlowLayout.LEFT);
    colItemType.setEditableOnInsert(true);
    colItemType.setColumnName("progressiveHie02ITM01");
    reloadButton2.setText("reloadButton2");
    saveButton3.setText("saveButton3");
    insertButton3.setText("insertButton3");
    colItemCode.setColumnFilterable(true);
    colItemCode.setColumnSortable(true);
    colItemCode.setEditableOnInsert(true);
    colItemCode.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colItemCode.setMaxCharacters(20);
    colItemCode.setColumnName("itemCodeItm01SCH15");
    itemsButtonsPanel.setLayout(flowLayout4);
    flowLayout5.setAlignment(FlowLayout.LEFT);
    docsButtonsPanel1.setLayout(flowLayout5);
    docsGrid.setAutoLoadData(false);
    docsGrid.setDeleteButton(deleteButton5);
    //docsGrid.setFunctionId("SCH08");
    docsGrid.setInsertButton(insertButton5);
    docsGrid.setNavBar(navigatorBar5);
    docsGrid.setReloadButton(reloadButton5);
    docsGrid.setSaveButton(saveButton5);
    docsGrid.setValueObjectClassName("org.jallinone.scheduler.activities.java.ActAttachedDocVO");
    docsGrid.setVisibleStatusPanel(false);
    colTaskCode.setColumnName("taskCodeREG07");
    colTaskCode.setEditableOnInsert(true);
    colTaskCode.setPreferredWidth(80);
    colTaskCode.setMaxCharacters(20);
    colEmployeeCode.setColumnName("employeeCodeSCH01");
    colEmployeeCode.setEditableOnEdit(true);
    colEmployeeCode.setEditableOnInsert(true);
    colEmployeeCode.setPreferredWidth(80);
    colEmployeeCode.setMaxCharacters(20);
    colName1.setColumnName("name_1REG04");
    colName1.setHeaderColumnName("firstname");
    colName1.setPreferredWidth(120);
    colName2.setColumnName("name_2REG04");
    colName2.setHeaderColumnName("lastname");
    colName2.setPreferredWidth(120);
    colStartDate.setColumnName("startDateSCH07");
    colStartDate.setEditableOnEdit(true);
    colStartDate.setEditableOnInsert(true);
    colStartDate.setPreferredWidth(150);
    colEndDate.setColumnName("endDateSCH07");
    colEndDate.setEditableOnEdit(true);
    colEndDate.setEditableOnInsert(true);
    colEndDate.setPreferredWidth(150);
    colDuration.setColumnName("durationSCH07");
    colDuration.setEditableOnEdit(true);
    colDuration.setEditableOnInsert(true);
    colDuration.setPreferredWidth(70);
    colNote.setMaxCharacters(2000);
    colNote.setRpadding(false);
    colNote.setColumnName("noteSCH07");
    colNote.setColumnRequired(false);
    colNote.setEditableOnEdit(true);
    colNote.setEditableOnInsert(true);
    colNote.setPreferredWidth(300);
    colMacStartDate.setColumnName("startDateSCH09");
    colMacStartDate.setEditableOnEdit(true);
    colMacStartDate.setEditableOnInsert(true);
    colMacStartDate.setPreferredWidth(150);
    colMacEndDate.setColumnName("endDateSCH09");
    colMacEndDate.setEditableOnEdit(true);
    colMacEndDate.setEditableOnInsert(true);
    colMacEndDate.setPreferredWidth(150);
    colMacDuration.setColumnName("durationSCH09");
    colMacDuration.setEditableOnEdit(true);
    colMacDuration.setEditableOnInsert(true);
    colMacDuration.setPreferredWidth(70);
    colMacDuration.setGrouping(false);
    colMacNote.setColumnName("noteSCH09");
    colMacNote.setColumnRequired(false);
    colMacNote.setEditableOnEdit(true);
    colMacNote.setEditableOnInsert(true);
    colMacNote.setPreferredWidth(300);
    colQty.setEditableOnInsert(true);
    colQty.setColumnName("qtySCH15");
    colQty.setEditableOnEdit(true);
    colDocProg.setAllowOnlyNumbers(true);
    colDocProg.setColumnName("progressiveDoc14SCH08");
    colDocProg.setHideCodeBox(true);
    colDocProg.setEditableOnInsert(true);
    colDocProg.setMinWidth(0);
    colDocProg.setPreferredWidth(40);
    collDocName.setColumnName("descriptionDOC14");
    collDocName.setPreferredWidth(400);
    editButton2.setText("editButton1");
    editButton3.setText("editButton1");
    editButton4.setText("editButton1");
    colDocType.setColumnFilterable(true);
    colDocType.setEditableOnInsert(true);
    colDocType.setPreferredWidth(200);
    colDocType.setPreferredWidth(200);
    colDocType.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colDocType.setColumnName("progressiveHie02HIE01");
    colDocType.setColumnRequired(false);
    colDocType.setHeaderColumnName("docType");
    buttonSparePartsCat.addActionListener(new ScheduledResourcesPanel_buttonSparePartsCat_actionAdapter(this));
    tasksButtonsPanel.add(insertButton2, null);
    tasksButtonsPanel.add(editButton2, null);
    tasksButtonsPanel.add(saveButton2, null);
    tasksButtonsPanel.add(reloadButton2, null);
    tasksButtonsPanel.add(deleteButton2, null);
    tasksButtonsPanel.add(navigatorBar2, null);
    resourcesPanel.add(tasksGrid,   new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    resourcesPanel.add(tasksButtonsPanel,   new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    resourcesPanel.add(macsButtonsPanel,   new GridBagConstraints(0, 4, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    macsButtonsPanel.add(insertButton3, null);
    macsButtonsPanel.add(editButton3, null);
    macsButtonsPanel.add(saveButton3, null);
    macsButtonsPanel.add(reloadButton3, null);
    macsButtonsPanel.add(deleteButton3, null);
    macsButtonsPanel.add(navigatorBar3, null);
    resourcesPanel.add(macsGrid,   new GridBagConstraints(0, 5, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    macsGrid.getColumnContainer().add(colMacCode, null);
    macsGrid.getColumnContainer().add(colMacDescr, null);
    macsGrid.getColumnContainer().add(colMacStartDate, null);
    macsGrid.getColumnContainer().add(colMacEndDate, null);
    macsGrid.getColumnContainer().add(colMacDuration, null);
    macsGrid.getColumnContainer().add(colMacNote, null);
    resourcesPanel.add(itemsButtonsPanel,   new GridBagConstraints(0, 6, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    itemsButtonsPanel.add(insertButton4, null);
    itemsButtonsPanel.add(editButton4, null);
    itemsButtonsPanel.add(saveButton4, null);
    itemsButtonsPanel.add(reloadButton4, null);
    itemsButtonsPanel.add(deleteButton4, null);
    itemsButtonsPanel.add(navigatorBar4, null);
    itemsButtonsPanel.add(buttonSparePartsCat, null);
    resourcesPanel.add(itemsGrid,   new GridBagConstraints(0, 7, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    itemsGrid.getColumnContainer().add(colItemType, null);
    itemsGrid.getColumnContainer().add(colItemCode, null);
    itemsGrid.getColumnContainer().add(colItemDescr, null);
    itemsGrid.getColumnContainer().add(colQty, null);
    docPanel.add(docsButtonsPanel1,      new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    docsButtonsPanel1.add(insertButton5, null);
    docsButtonsPanel1.add(saveButton5, null);
    docsButtonsPanel1.add(reloadButton5, null);
    docsButtonsPanel1.add(deleteButton5, null);
    docsButtonsPanel1.add(navigatorBar5, null);
    docPanel.add(docsGrid,      new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    this.add(tab, BorderLayout.CENTER);
    tab.add(resourcesPanel,"resourcesPanel");
    tab.add(docPanel,"docPanel");

    colGantt.setColumnName("button"); // a random attribute...
    colGantt.setHeaderColumnName("gantt"); // a random attribute...
    colGantt.setEditableOnEdit(true);
    colGantt.setEditableOnInsert(true);

    tab.setTitleAt(0,ClientSettings.getInstance().getResources().getResource("scheduled resources"));
    tab.setTitleAt(1,ClientSettings.getInstance().getResources().getResource("attached documents"));

    tasksGrid.getColumnContainer().add(colTaskCode, null);
    tasksGrid.getColumnContainer().add(colTaskDescr, null);
    tasksGrid.getColumnContainer().add(colGantt, null);
    tasksGrid.getColumnContainer().add(colEmployeeCode, null);
    tasksGrid.getColumnContainer().add(colName1, null);
    tasksGrid.getColumnContainer().add(colName2, null);
    tasksGrid.getColumnContainer().add(colStartDate, null);
    tasksGrid.getColumnContainer().add(colEndDate, null);
    tasksGrid.getColumnContainer().add(colDuration, null);
    tasksGrid.getColumnContainer().add(colNote, null);
    docsGrid.getColumnContainer().add(colDocType, null);
    docsGrid.getColumnContainer().add(colDocProg, null);
    docsGrid.getColumnContainer().add(collDocName, null);
  }


  public ScheduledActivityVO getActVO() {
    return actVO;
  }
  public void setActVO(ScheduledActivityVO actVO) {
    this.actVO = actVO;
  }
  public GridControl getTasksGrid() {
    return tasksGrid;
  }
  public GridControl getMacsGrid() {
    return macsGrid;
  }
  public GridControl getItemsGrid() {
    return itemsGrid;
  }
  public GridControl getDocsGrid() {
    return docsGrid;
  }


  public void setButtonsEnabled(boolean enabled) {
    insertButton2.setEnabled(enabled);
    insertButton3.setEnabled(enabled);
    insertButton4.setEnabled(enabled);
    insertButton5.setEnabled(enabled);
    editButton2.setEnabled(enabled);
    editButton3.setEnabled(enabled);
    editButton4.setEnabled(enabled);
    reloadButton2.setEnabled(enabled);
    reloadButton3.setEnabled(enabled);
    reloadButton4.setEnabled(enabled);
    reloadButton5.setEnabled(enabled);
    deleteButton2.setEnabled(enabled);
    deleteButton3.setEnabled(enabled);
    deleteButton4.setEnabled(enabled);
    deleteButton5.setEnabled(enabled);

  }


  void ganttButton_actionPerformed(ActionEvent e) {
    ScheduledEmployeeVO vo = (ScheduledEmployeeVO)tasksGrid.getVOListTableModel().getObjectForRow(tasksGrid.getSelectedRow());
    new ActivityGanttController(colEmployeeCode,tasksGrid.getMode()==Consts.INSERT,actVO.getStartDateSCH06(),actVO.getEstimatedEndDateSCH06(),vo);
  }



  void buttonSparePartsCat_actionPerformed(ActionEvent e) {
		if (frame==null) {
			frame = new SparePartsCatalogueFrame(true);
			frame.addInternalFrameListener(new InternalFrameAdapter() {

				public void internalFrameClosed(InternalFrameEvent e) {
					frame = null;
				}

			});
			frame.setCallbacks(new SparePartsCatalogueCallbacks() {

				/**
				 * Callback invoked when a user double clicks on a spare part
				 */
				public void sparePartDoubleClick(BigDecimal progressiveHie02ITM01,String companyCode,String itemCode,String itemDescr) {
					ClientUtils.getParentInternalFrame(ScheduledResourcesPanel.this).toFront();
					if (itemsGrid.getMode()==Consts.INSERT) {
						 ScheduledItemVO vo = (ScheduledItemVO)itemsGrid.getVOListTableModel().getObjectForRow(0);
						 vo.setProgressiveHie02ITM01(progressiveHie02ITM01);
						 vo.setCompanyCodeSys01SCH15(companyCode);
						 vo.setItemCodeItm01SCH15(itemCode);
						 vo.setDescriptionSYS10(itemDescr);
					}
					else if (itemsGrid.getMode()==Consts.READONLY) {
						 itemsGrid.getTable().insert();
						 ScheduledItemVO vo = (ScheduledItemVO)itemsGrid.getVOListTableModel().getObjectForRow(0);
						 vo.setProgressiveHie02ITM01(progressiveHie02ITM01);
						 vo.setCompanyCodeSys01SCH15(companyCode);
						 vo.setItemCodeItm01SCH15(itemCode);
						 vo.setDescriptionSYS10(itemDescr);
					}
					int modelIndex = itemsGrid.getTable().getGrid().getColumnIndex("qtySCH15");
					if (!itemsGrid.getTable().getGrid().hasFocus())
						itemsGrid.getTable().getGrid().requestFocus();
					itemsGrid.getTable().getGrid().editCellAt(0,itemsGrid.getTable().getGrid().convertColumnIndexToView(modelIndex));

				}

			});
		}
		else {
			frame.toFront();
		}
		frame.setItem(progressiveHie02ITM01,companyCodeSys01SCH03,itemCodeItm01SCH03);
  }


  public void setItem(BigDecimal progressiveHie02ITM01,String companyCodeSys01SCH03,String itemCodeItm01SCH03) {
		this.progressiveHie02ITM01 = progressiveHie02ITM01;
		this.companyCodeSys01SCH03 = companyCodeSys01SCH03;
    this.itemCodeItm01SCH03 = itemCodeItm01SCH03;
  }


}

class ScheduledResourcesPanel_ganttButton_actionAdapter implements java.awt.event.ActionListener {
  ScheduledResourcesPanel adaptee;

  ScheduledResourcesPanel_ganttButton_actionAdapter(ScheduledResourcesPanel adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.ganttButton_actionPerformed(e);
  }
}

class ScheduledResourcesPanel_buttonSparePartsCat_actionAdapter implements java.awt.event.ActionListener {
  ScheduledResourcesPanel adaptee;

  ScheduledResourcesPanel_buttonSparePartsCat_actionAdapter(ScheduledResourcesPanel adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.buttonSparePartsCat_actionPerformed(e);
  }
}

