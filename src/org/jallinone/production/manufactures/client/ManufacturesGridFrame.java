package org.jallinone.production.manufactures.client;

import org.openswing.swing.mdi.client.InternalFrame;
import org.openswing.swing.tree.client.*;
import java.awt.*;
import org.openswing.swing.mdi.client.MDIFrame;
import javax.swing.*;
import org.openswing.swing.client.*;
import org.openswing.swing.table.java.ServerGridDataLocator;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.table.client.GridController;
import java.math.BigDecimal;
import javax.swing.border.*;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.openswing.swing.lookup.client.LookupListener;
import org.openswing.swing.message.receive.java.ValueObject;
import java.util.Collection;
import org.jallinone.registers.currency.java.CurrencyVO;
import org.jallinone.commons.client.*;
import org.jallinone.production.manufactures.java.OperationVO;
import org.jallinone.production.manufactures.java.ManufactureVO;
import org.openswing.swing.message.receive.java.Response;
import org.jallinone.production.manufactures.java.ManufacturePhaseVO;
import org.openswing.swing.util.client.ClientUtils;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.jallinone.registers.currency.java.CurrencyConvVO;
import org.openswing.swing.message.send.java.GridParams;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the manufactures grid frame.</p>
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
public class ManufacturesGridFrame extends InternalFrame implements CurrencyColumnSettings {

  JPanel buttonsPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  InsertButton insertButton = new InsertButton();
  ReloadButton reloadButton = new ReloadButton();
  DeleteButton deleteButton = new DeleteButton();
  NavigatorBar navigatorBar = new NavigatorBar();
  GridControl grid = new GridControl();

  EditButton editButton = new EditButton();
  SaveButton saveButton = new SaveButton();

  /** grid data locator */
  private ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();
  TextColumn colManufacture = new TextColumn();
  TextColumn colManDescription = new TextColumn();
  ExportButton exportButton = new ExportButton();
  JPanel phasesPanel = new JPanel();
  TitledBorder titledBorder1;
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel phasesButtonsPanel = new JPanel();
  FlowLayout flowLayout2 = new FlowLayout();
  GridControl phasesGridControl = new GridControl();
  TextColumn colNote = new TextColumn();
  DecimalColumn colNr = new DecimalColumn();
  EditButton editButton1 = new EditButton();
  SaveButton saveButton1 = new SaveButton();
  ReloadButton reloadButton1 = new ReloadButton();

  private ServerGridDataLocator phasesGridDataLocator = new ServerGridDataLocator();
  ExportButton exportButton1 = new ExportButton();
  DeleteButton deleteButton1 = new DeleteButton();
  InsertButton insertButton1 = new InsertButton();
  JSplitPane splitPane = new JSplitPane();

  TextColumn colDescr = new TextColumn();
  CompaniesComboColumn colCompanyCode = new CompaniesComboColumn();
  ComboColumn colManType = new ComboColumn();
  DecimalColumn colQty = new DecimalColumn();
  IntegerColumn colDuration = new IntegerColumn();
  CurrencyColumn colValue = new CurrencyColumn();
  IntegerColumn colPerc = new IntegerColumn();
  CodLookupColumn colMachCode = new CodLookupColumn();
  TextColumn colMachDescr = new TextColumn();
  CodLookupColumn colTaskCode = new CodLookupColumn();
  TextColumn colTaskDescr = new TextColumn();
  CodLookupColumn colAlternCode = new CodLookupColumn();
  TextColumn colAltDescr = new TextColumn();
  CodLookupColumn colOpCod = new CodLookupColumn();

  LookupController taskController = new LookupController();
  LookupServerDataLocator taskDataLocator = new LookupServerDataLocator();

  LookupController macController = new LookupController();
  LookupServerDataLocator macDataLocator = new LookupServerDataLocator();

  LookupController altController = new LookupController();
  LookupServerDataLocator altDataLocator = new LookupServerDataLocator();

  LookupController opController = new LookupController();
  LookupServerDataLocator opDataLocator = new LookupServerDataLocator();

  /** currency used with the current company */
  CurrencyVO companyCurrencyVO = null;


  public ManufacturesGridFrame(GridController controller) {
    grid.setController(controller);
    grid.setGridDataLocator(gridDataLocator);
    gridDataLocator.setServerMethodName("loadManufactures");
    try {
      jbInit();
      setSize(750,550);
      setMinimumSize(new Dimension(750,550));


      phasesGridControl.setController(new ManufacturePhasesController(this));
      phasesGridControl.setGridDataLocator(phasesGridDataLocator);
      phasesGridDataLocator.setServerMethodName("loadManufacturePhases");

      CustomizedColumns cust = new CustomizedColumns(ApplicationConsts.ID_MANUFACTURE,grid);


      // alternative operation code lookup...
      altDataLocator.setGridMethodName("loadOperations");
      altDataLocator.setValidationMethodName("validateOperationCode");
      colAlternCode.setLookupController(altController);
      colAlternCode.setControllerMethodName("getOperationsList");
      altController.setLookupDataLocator(altDataLocator);
      altController.setFrameTitle("operations");
      altController.setLookupValueObjectClassName("org.jallinone.production.manufactures.java.OperationVO");
      altController.addLookup2ParentLink("operationCodePRO04", "substOperationCodePro04PRO02");
      altController.addLookup2ParentLink("descriptionSYS10", "description2");
      altController.setAllColumnVisible(false);
      altController.setVisibleColumn("operationCodePRO04", true);
      altController.setVisibleColumn("descriptionSYS10", true);
      altController.setPreferredWidthColumn("descriptionSYS10", 200);
      altController.setFramePreferedSize(new Dimension(350,500));
      altController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {}

        public void beforeLookupAction(ValueObject parentVO) {
          ManufactureVO vo = (ManufactureVO)grid.getVOListTableModel().getObjectForRow(grid.getSelectedRow());
          altController.getLookupDataLocator().getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01PRO01());
          altController.getLookupDataLocator().getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01PRO01());
        }

        public void forceValidate() {}

      });


      // operation code lookup...
      opDataLocator.setGridMethodName("loadOperations");
      opDataLocator.setValidationMethodName("validateOperationCode");
      colOpCod.setLookupController(opController);
      colOpCod.setControllerMethodName("getOperationsList");
      opController.setLookupDataLocator(opDataLocator);
      opController.setFrameTitle("operations");
      opController.setLookupValueObjectClassName("org.jallinone.production.manufactures.java.OperationVO");
      opController.addLookup2ParentLink("operationCodePRO04", "operationCodePro04PRO02");
      opController.addLookup2ParentLink("descriptionSYS10", "descriptionSYS10");
      opController.addLookup2ParentLink("qtyPRO04", "qtyPRO02");
      opController.addLookup2ParentLink("durationPRO04", "durationPRO02");
      opController.addLookup2ParentLink("notePRO04", "notePRO02");
      opController.addLookup2ParentLink("taskCodeReg07PRO04", "taskCodeReg07PRO02");
      opController.addLookup2ParentLink("taskDescriptionSYS10", "taskDescriptionSYS10");
      opController.addLookup2ParentLink("machineryCodePro03PRO04", "machineryCodePro03PRO02");
      opController.addLookup2ParentLink("machineryDescriptionSYS10", "machineryDescriptionSYS10");
      opController.addLookup2ParentLink("valuePRO04", "valuePRO02");
      opController.setAllColumnVisible(false);
      opController.setVisibleColumn("operationCodePRO04", true);
      opController.setVisibleColumn("descriptionSYS10", true);
      opController.setPreferredWidthColumn("descriptionSYS10", 200);
      opController.setFramePreferedSize(new Dimension(350,500));
      opController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {}

        public void beforeLookupAction(ValueObject parentVO) {
          ManufactureVO vo = (ManufactureVO)grid.getVOListTableModel().getObjectForRow(grid.getSelectedRow());
          opController.getLookupDataLocator().getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01PRO01());
          opController.getLookupDataLocator().getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01PRO01());
        }

        public void forceValidate() {}

      });


      // task lookup...
      taskDataLocator.setGridMethodName("loadTasks");
      taskDataLocator.setValidationMethodName("validateTaskCode");
      colTaskCode.setLookupController(taskController);
      colTaskCode.setControllerMethodName("getTasksList");
      taskController.setLookupDataLocator(taskDataLocator);
      taskController.setFrameTitle("tasks");
      taskController.setLookupValueObjectClassName("org.jallinone.registers.task.java.TaskVO");
      taskController.addLookup2ParentLink("taskCodeREG07", "taskCodeReg07PRO02");
      taskController.addLookup2ParentLink("descriptionSYS10","taskDescriptionSYS10");
      taskController.setAllColumnVisible(false);
      taskController.setVisibleColumn("taskCodeREG07", true);
      taskController.setVisibleColumn("descriptionSYS10", true);
      taskController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {}

        public void beforeLookupAction(ValueObject parentVO) {
          ManufacturePhaseVO vo = (ManufacturePhaseVO)parentVO;
          taskDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01PRO02());
          taskDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01PRO02());
        }

        public void forceValidate() {}

      });
      new CustomizedColumns(new BigDecimal(192),taskController);


      // machineries lookup...
       macDataLocator.setGridMethodName("loadMachineries");
       macDataLocator.setValidationMethodName("validateMachineryCode");
       colMachCode.setLookupController(macController);
       colMachCode.setControllerMethodName("getMachineriesList");
       macController.setLookupDataLocator(macDataLocator);
       macController.setFrameTitle("machineries");
       macController.setLookupValueObjectClassName("org.jallinone.production.machineries.java.MachineryVO");
       macController.addLookup2ParentLink("machineryCodePRO03", "machineryCodePro03PRO02");
       macController.addLookup2ParentLink("descriptionSYS10", "machineryDescriptionSYS10");
       macController.setAllColumnVisible(false);
       macController.setVisibleColumn("machineryCodePRO03", true);
       macController.setVisibleColumn("descriptionSYS10", true);
       macController.addLookupListener(new LookupListener() {

         public void codeValidated(boolean validated) {}

         public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {}

         public void beforeLookupAction(ValueObject parentVO) {
           ManufacturePhaseVO vo = (ManufacturePhaseVO)parentVO;
           macDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01PRO02());
           macDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01PRO02());
         }

         public void forceValidate() {}

       });

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  public final void reloadData() {
    grid.reloadData();
  }


  private void jbInit() throws Exception {
    splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
    titledBorder1 = new TitledBorder("");
    grid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    saveButton.setExecuteAsThread(true);
    phasesGridControl.setValueObjectClassName("org.jallinone.production.manufactures.java.ManufacturePhaseVO");
    grid.setValueObjectClassName("org.jallinone.production.manufactures.java.ManufactureVO");
    this.setTitle(ClientSettings.getInstance().getResources().getResource("production cycles"));
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    grid.setDeleteButton(deleteButton);
    grid.setEditButton(editButton);
    grid.setExportButton(exportButton);
    grid.setFunctionId("PRO01");
    grid.setMaxSortedColumns(3);
    grid.setInsertButton(insertButton);
    grid.setNavBar(navigatorBar);
    grid.setReloadButton(reloadButton);
    grid.setSaveButton(saveButton);
    colManufacture.setMaxCharacters(20);
    colManufacture.setTrimText(true);
    colManufacture.setUpperCase(true);
    colManufacture.setColumnFilterable(true);
    colManufacture.setColumnName("manufactureCodePRO01");
    colManufacture.setColumnSortable(true);
    colManufacture.setEditableOnInsert(true);
    colManufacture.setPreferredWidth(100);
    colManufacture.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colManufacture.setSortingOrder(1);
    colManDescription.setMaxCharacters(255);
    colManDescription.setColumnDuplicable(true);
    colManDescription.setColumnFilterable(false);
    colManDescription.setColumnName("descriptionSYS10");
    colManDescription.setColumnSortable(true);
    colManDescription.setEditableOnEdit(true);
    colManDescription.setEditableOnInsert(true);
    colManDescription.setPreferredWidth(300);
    phasesPanel.setBorder(titledBorder1);
    phasesPanel.setLayout(borderLayout1);
    titledBorder1.setTitleColor(Color.blue);
    titledBorder1.setTitle(ClientSettings.getInstance().getResources().getResource("production cycles"));
    phasesButtonsPanel.setLayout(flowLayout2);
    flowLayout2.setAlignment(FlowLayout.LEFT);
    phasesGridControl.setAutoLoadData(false);
    phasesGridControl.setDeleteButton(deleteButton1);
    phasesGridControl.setEditButton(editButton1);
    phasesGridControl.setExportButton(exportButton1);
    phasesGridControl.setFunctionId("PRO01");
    phasesGridControl.setMaxNumberOfRowsOnInsert(50);
    phasesGridControl.setInsertButton(insertButton1);
//    phasesGridControl.setPreferredSize(new Dimension(340, 200));
    phasesGridControl.setReloadButton(reloadButton1);
    phasesGridControl.setSaveButton(saveButton1);
    colNr.setDecimals(5);
    colNr.setColumnFilterable(true);
    colNr.setColumnName("phaseNumberPRO02");
    colNr.setColumnRequired(true);
    colNr.setColumnSortable(true);
    colNr.setEditableOnEdit(true);
    colNr.setEditableOnInsert(true);
    colNr.setPreferredWidth(50);
    colNr.setSortingOrder(0);
    colNr.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colNote.setColumnName("notePRO02");
    colNote.setColumnRequired(false);
    colNote.setEditableOnEdit(true);
    colNote.setEditableOnInsert(true);
    colNote.setPreferredWidth(300);
    colDescr.setColumnDuplicable(true);
    colDescr.setColumnFilterable(true);
    colDescr.setColumnName("descriptionSYS10");
    colDescr.setColumnRequired(true);
    colDescr.setColumnSortable(false);
    colDescr.setEditableOnEdit(true);
    colDescr.setEditableOnInsert(true);
    colDescr.setPreferredWidth(200);
    colCompanyCode.setColumnName("companyCodeSys01PRO01");
    colCompanyCode.setFunctionCode("PRO01");
    colManType.setColumnDuplicable(true);
    colManType.setColumnFilterable(true);
    colManType.setColumnSortable(true);
    colManType.setEditableOnEdit(true);
    colManType.setEditableOnInsert(true);
    colManType.setPreferredWidth(80);
    colQty.setColumnDuplicable(true);
    colQty.setColumnFilterable(true);
    colQty.setColumnRequired(false);
    colQty.setEditableOnEdit(true);
    colQty.setEditableOnInsert(true);
    colQty.setPreferredWidth(60);
    colQty.setDecimals(5);

    colOpCod.setColumnName("operationCodePro04PRO02");
    colOpCod.setColumnDuplicable(true);
    colOpCod.setColumnFilterable(true);
    colOpCod.setColumnRequired(true);
    colOpCod.setEditableOnEdit(false);
    colOpCod.setEditableOnInsert(true);

    colDuration.setColumnDuplicable(true);
    colDuration.setColumnFilterable(true);
    colDuration.setEditableOnEdit(true);
    colDuration.setEditableOnInsert(true);
    colDuration.setPreferredWidth(60);
    colValue.setColumnDuplicable(true);
    colValue.setColumnFilterable(true);
    colValue.setColumnSortable(true);
    colValue.setEditableOnEdit(true);
    colValue.setEditableOnInsert(true);
    colValue.setPreferredWidth(80);
    colPerc.setColumnDuplicable(true);
    colPerc.setColumnFilterable(true);
    colPerc.setEditableOnEdit(true);
    colPerc.setEditableOnInsert(true);
    colPerc.setPreferredWidth(60);
    colTaskCode.setColumnDuplicable(true);
    colTaskCode.setColumnFilterable(true);
    colTaskCode.setColumnRequired(false);
    colTaskCode.setEditableOnEdit(true);
    colTaskCode.setEditableOnInsert(true);
    colTaskCode.setMaxCharacters(20);
    colTaskDescr.setColumnDuplicable(true);
    colTaskDescr.setColumnRequired(false);
    colTaskDescr.setPreferredWidth(200);
    colMachCode.setColumnDuplicable(true);
    colMachCode.setColumnFilterable(true);
    colMachCode.setColumnRequired(false);
    colMachCode.setEditableOnEdit(true);
    colMachCode.setEditableOnInsert(true);
    colMachCode.setMaxCharacters(20);
    colMachDescr.setColumnDuplicable(true);
    colMachDescr.setColumnRequired(false);
    colMachDescr.setPreferredWidth(200);

    colAlternCode.setColumnDuplicable(true);
    colAlternCode.setColumnRequired(false);
    colAlternCode.setEditableOnEdit(true);
    colAlternCode.setEditableOnInsert(true);
    colAlternCode.setPreferredWidth(100);

    colAltDescr.setColumnDuplicable(true);
    colAltDescr.setColumnRequired(false);
    colAltDescr.setEditableOnEdit(false);
    colAltDescr.setEditableOnInsert(false);
    colAltDescr.setPreferredWidth(200);

    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(saveButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(deleteButton, null);
    buttonsPanel.add(exportButton, null);
    buttonsPanel.add(navigatorBar, null);
    this.getContentPane().add(splitPane, BorderLayout.CENTER);
    splitPane.add(grid,JSplitPane.TOP);
    splitPane.add(phasesPanel,  JSplitPane.BOTTOM);
    phasesPanel.add(phasesButtonsPanel, BorderLayout.NORTH);
    phasesButtonsPanel.add(insertButton1, null);
    phasesButtonsPanel.add(editButton1, null);
    phasesButtonsPanel.add(saveButton1, null);
    phasesButtonsPanel.add(reloadButton1, null);
    phasesButtonsPanel.add(deleteButton1, null);
    phasesButtonsPanel.add(exportButton1, null);
    phasesPanel.add(phasesGridControl,  BorderLayout.CENTER);
    splitPane.setDividerLocation(200);
    grid.getColumnContainer().add(colCompanyCode, null);
    grid.getColumnContainer().add(colManufacture, null);
    grid.getColumnContainer().add(colManDescription, null);
    colManType.setColumnName("manufactureTypePRO02");
    colManType.setDomainId("MANUFACTURE_TYPE");
    phasesGridControl.getColumnContainer().add(colOpCod, null);
    phasesGridControl.getColumnContainer().add(colDescr, null);
    phasesGridControl.getColumnContainer().add(colNr, null);
    phasesGridControl.getColumnContainer().add(colManType, null);
    phasesGridControl.getColumnContainer().add(colDuration, null);
    phasesGridControl.getColumnContainer().add(colValue, null);
    phasesGridControl.getColumnContainer().add(colPerc, null);
    phasesGridControl.getColumnContainer().add(colTaskCode, null);
    phasesGridControl.getColumnContainer().add(colTaskDescr, null);
    phasesGridControl.getColumnContainer().add(colQty, null);
    phasesGridControl.getColumnContainer().add(colMachCode, null);
    phasesGridControl.getColumnContainer().add(colMachDescr, null);
    phasesGridControl.getColumnContainer().add(colAlternCode, null);
    phasesGridControl.getColumnContainer().add(colAltDescr, null);
    phasesGridControl.getColumnContainer().add(colNote, null);
    colQty.setColumnName("qtyPRO02");
    colDuration.setColumnName("durationPRO02");
    colValue.setColumnName("valuePRO02");
    colPerc.setColumnName("completionPercPRO02");
    colValue.setDynamicSettings(this);
    colTaskCode.setColumnName("taskCodeReg07PRO02");
    colTaskDescr.setColumnName("taskDescriptionSYS10");
    colMachCode.setColumnName("machineryCodePro03PRO02");
    colMachDescr.setColumnName("machineryDescriptionSYS10");
    colAlternCode.setColumnName("substOperationCodePro04PRO02");
    colAltDescr.setColumnName("description2");
  }


  public final void setButtonsEnabled(boolean enabled) {
    insertButton1.setEnabled(enabled);
    editButton1.setEnabled(enabled);
    deleteButton1.setEnabled(enabled);
    exportButton1.setEnabled(enabled);
    reloadButton1.setEnabled(enabled);
  }


  public GridControl getGrid() {
    return grid;
  }


  public GridControl getPhasesGridControl() {
    return phasesGridControl;
  }



  public double getMaxValue(int int0) {
    return Double.MAX_VALUE;
  }

  public double getMinValue(int int0) {
    return 0.0;
  }

  public boolean isGrouping(int int0) {
    return true;
  }

  public int getDecimals(int int0) {
    if (companyCurrencyVO!=null)
      return companyCurrencyVO.getDecimalsREG03().intValue();
    else
      return 0;
  }

  public String getCurrencySymbol(int int0) {
    if (companyCurrencyVO!=null)
      return companyCurrencyVO.getCurrencySymbolREG03();
    else
    return "E";
  }
  public void setCompanyCurrencyVO(CurrencyVO companyCurrencyVO) {
    this.companyCurrencyVO = companyCurrencyVO;
  }



}
