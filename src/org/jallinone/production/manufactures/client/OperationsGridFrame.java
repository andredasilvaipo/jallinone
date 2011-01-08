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


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the operations grid frame.</p>
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
public class OperationsGridFrame extends InternalFrame implements CurrencyColumnSettings {

  JPanel opPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel opButtonsPanel = new JPanel();
  FlowLayout flowLayout2 = new FlowLayout();
  GridControl operationsGridControl = new GridControl();
  TextColumn colNote = new TextColumn();
  EditButton editButton1 = new EditButton();
  SaveButton saveButton1 = new SaveButton();
  ReloadButton reloadButton1 = new ReloadButton();

  private ServerGridDataLocator opGridDataLocator = new ServerGridDataLocator();
  ExportButton exportButton1 = new ExportButton();
  DeleteButton deleteButton1 = new DeleteButton();
  InsertButton insertButton1 = new InsertButton();

  private CurrencyVO currencyVO = null;
  TextColumn colDescr = new TextColumn();
  CompaniesComboColumn colCompanyCode = new CompaniesComboColumn();
  DecimalColumn colQty = new DecimalColumn();
  IntegerColumn colDuration = new IntegerColumn();
  CurrencyColumn colValue = new CurrencyColumn();
  CodLookupColumn colMachCode = new CodLookupColumn();
  TextColumn colMachDescr = new TextColumn();
  CodLookupColumn colTaskCode = new CodLookupColumn();
  TextColumn colTaskDescr = new TextColumn();
  TextColumn colOpCod = new TextColumn();

  LookupController taskController = new LookupController();
  LookupServerDataLocator taskDataLocator = new LookupServerDataLocator();

  LookupController macController = new LookupController();
  LookupServerDataLocator macDataLocator = new LookupServerDataLocator();


  public OperationsGridFrame(GridController controller) {
    try {
      jbInit();
      setSize(750,550);
      setMinimumSize(new Dimension(750,550));

      operationsGridControl.setController(controller);
      operationsGridControl.setGridDataLocator(opGridDataLocator);
      opGridDataLocator.setServerMethodName("loadOperations");


      // task lookup...
      taskDataLocator.setGridMethodName("loadTasks");
      taskDataLocator.setValidationMethodName("validateTaskCode");
      colTaskCode.setLookupController(taskController);
      colTaskCode.setControllerMethodName("getTasksList");
      taskController.setLookupDataLocator(taskDataLocator);
      taskController.setFrameTitle("tasks");
      taskController.setLookupValueObjectClassName("org.jallinone.registers.task.java.TaskVO");
      taskController.addLookup2ParentLink("taskCodeREG07", "taskCodeReg07PRO04");
      taskController.addLookup2ParentLink("descriptionSYS10","taskDescriptionSYS10");
      taskController.setAllColumnVisible(false);
      taskController.setVisibleColumn("taskCodeREG07", true);
      taskController.setVisibleColumn("descriptionSYS10", true);
      taskController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {}

        public void beforeLookupAction(ValueObject parentVO) {
          OperationVO vo = (OperationVO)parentVO;
          taskDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01PRO04());
          taskDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01PRO04());
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
      macController.addLookup2ParentLink("machineryCodePRO03", "machineryCodePro03PRO04");
      macController.addLookup2ParentLink("descriptionSYS10", "machineryDescriptionSYS10");
      macController.setAllColumnVisible(false);
      macController.setVisibleColumn("machineryCodePRO03", true);
      macController.setVisibleColumn("descriptionSYS10", true);
      macController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {}

        public void beforeLookupAction(ValueObject parentVO) {
          OperationVO vo = (OperationVO)parentVO;
          macDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01PRO04());
          macDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01PRO04());
        }

        public void forceValidate() {}

      });



    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    operationsGridControl.setValueObjectClassName("org.jallinone.production.manufactures.java.OperationVO");
    this.setTitle(ClientSettings.getInstance().getResources().getResource("operations"));
    opPanel.setLayout(borderLayout1);
    opButtonsPanel.setLayout(flowLayout2);
    flowLayout2.setAlignment(FlowLayout.LEFT);
    operationsGridControl.setDeleteButton(deleteButton1);
    operationsGridControl.setEditButton(editButton1);
    operationsGridControl.setExportButton(exportButton1);
    operationsGridControl.setFunctionId("PRO04");
    operationsGridControl.setMaxNumberOfRowsOnInsert(50);
    operationsGridControl.setInsertButton(insertButton1);
    operationsGridControl.setReloadButton(reloadButton1);
    operationsGridControl.setSaveButton(saveButton1);
    colNote.setColumnName("notePRO04");
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
    colQty.setColumnDuplicable(true);
    colQty.setColumnFilterable(true);
    colQty.setColumnRequired(false);
    colQty.setEditableOnEdit(true);
    colQty.setEditableOnInsert(true);
    colQty.setPreferredWidth(60);
    colQty.setDecimals(5);
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
    colTaskCode.setColumnDuplicable(true);
    colTaskCode.setColumnFilterable(true);
    colTaskCode.setColumnRequired(false);
    colTaskCode.setEditableOnEdit(true);
    colTaskCode.setEditableOnInsert(true);
    colTaskCode.setMaxCharacters(20);
    colTaskDescr.setColumnDuplicable(true);
    colTaskDescr.setColumnRequired(false);
    colTaskDescr.setPreferredWidth(150);
    colMachCode.setColumnDuplicable(true);
    colMachCode.setColumnFilterable(true);
    colMachCode.setColumnRequired(false);
    colMachCode.setEditableOnEdit(true);
    colMachCode.setEditableOnInsert(true);
    colMachCode.setMaxCharacters(20);
    colMachDescr.setColumnDuplicable(true);
    colMachDescr.setColumnRequired(false);
    colMachDescr.setPreferredWidth(150);

    colOpCod.setColumnFilterable(true);
    colOpCod.setColumnRequired(true);
    colOpCod.setEditableOnEdit(false);
    colOpCod.setEditableOnInsert(true);
    colOpCod.setMaxCharacters(20);
    colOpCod.setTrimText(true);
    colOpCod.setUpperCase(true);
    colOpCod.setColumnName("operationCodePRO04");

    this.getContentPane().add(opPanel, BorderLayout.CENTER);
    opPanel.add(opButtonsPanel, BorderLayout.NORTH);
    opButtonsPanel.add(insertButton1, null);
    opButtonsPanel.add(editButton1, null);
    opButtonsPanel.add(saveButton1, null);
    opButtonsPanel.add(reloadButton1, null);
    opButtonsPanel.add(deleteButton1, null);
    opButtonsPanel.add(exportButton1, null);
    opPanel.add(operationsGridControl,  BorderLayout.CENTER);
    colCompanyCode.setColumnName("companyCodeSys01PRO04");
    colCompanyCode.setFunctionCode("PRO04");
    operationsGridControl.getColumnContainer().add(colCompanyCode, null);
    operationsGridControl.getColumnContainer().add(colOpCod, null);
    operationsGridControl.getColumnContainer().add(colDescr, null);
    operationsGridControl.getColumnContainer().add(colDuration, null);
    operationsGridControl.getColumnContainer().add(colValue, null);
    operationsGridControl.getColumnContainer().add(colTaskCode, null);
    operationsGridControl.getColumnContainer().add(colTaskDescr, null);
    operationsGridControl.getColumnContainer().add(colQty, null);
    operationsGridControl.getColumnContainer().add(colMachCode, null);
    operationsGridControl.getColumnContainer().add(colMachDescr, null);
    operationsGridControl.getColumnContainer().add(colNote, null);
    colQty.setColumnName("qtyPRO04");
    colDuration.setColumnName("durationPRO04");
    colValue.setColumnName("valuePRO04");
    colValue.setDynamicSettings(this);
    colTaskCode.setColumnName("taskCodeReg07PRO04");
    colTaskDescr.setColumnName("taskDescriptionSYS10");
    colMachCode.setColumnName("machineryCodePro03PRO04");
    colMachDescr.setColumnName("machineryDescriptionSYS10");
  }


  public final void setButtonsEnabled(boolean enabled) {
    insertButton1.setEnabled(enabled);
    editButton1.setEnabled(enabled);
    deleteButton1.setEnabled(enabled);
    exportButton1.setEnabled(enabled);
    reloadButton1.setEnabled(enabled);
  }


  public GridControl getoperationsGridControl() {
    return operationsGridControl;
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
    if (currencyVO!=null)
      return currencyVO.getDecimalsREG03().intValue();
    else
      return 0;
  }

  public String getCurrencySymbol(int int0) {
    if (currencyVO!=null)
      return currencyVO.getCurrencySymbolREG03();
    else
    return "E";
  }
  public void setCurrencyVO(CurrencyVO currencyVO) {
    this.currencyVO = currencyVO;
  }



}
