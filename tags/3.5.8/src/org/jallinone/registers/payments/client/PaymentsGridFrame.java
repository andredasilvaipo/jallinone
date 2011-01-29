package org.jallinone.registers.payments.client;

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
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import javax.swing.border.*;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.table.model.client.VOListTableModel;
import org.jallinone.registers.payments.java.*;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.commons.client.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the payments grid frame.</p>
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
public class PaymentsGridFrame extends InternalFrame {

  JPanel buttonsPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  InsertButton insertButton = new InsertButton();
  ReloadButton reloadButton = new ReloadButton();
  DeleteButton deleteButton = new DeleteButton();
  NavigatorBar navigatorBar = new NavigatorBar();
  GridControl grid = new GridControl();

  SaveButton saveButton = new SaveButton();

  /** grid data locator */
  private ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();

  /** instalments grid data locator */
  private ServerGridDataLocator instalmentsGridDataLocator = new ServerGridDataLocator();

  TextColumn colPaymentType = new TextColumn();
  TextColumn colDescr = new TextColumn();
  ExportButton exportButton = new ExportButton();
  IntegerColumn colInstNr = new IntegerColumn();
  ComboColumn colStartDay = new ComboColumn();
  IntegerColumn colStep = new IntegerColumn();
  IntegerColumn colFirstInstDays = new IntegerColumn();
  CodLookupColumn colPayType = new CodLookupColumn();
  TextColumn colPayTypeDescr = new TextColumn();

  LookupController payTypeController = new LookupController();
  LookupServerDataLocator payTypeDataLocator = new LookupServerDataLocator();

  JSplitPane split = new JSplitPane();
  JPanel paymentsPanel = new JPanel();
  JPanel instalmentsPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  BorderLayout borderLayout2 = new BorderLayout();
  TitledBorder titledBorder1;
  TitledBorder titledBorder2;
  JPanel instalmentsButtonsPanel = new JPanel();
  FlowLayout flowLayout2 = new FlowLayout();
  GridControl instalmentsGrid = new GridControl();
  NavigatorBar navigatorBar1 = new NavigatorBar();
  ExportButton exportButton1 = new ExportButton();
  ReloadButton reloadButton1 = new ReloadButton();
  SaveButton saveButton1 = new SaveButton();
  EditButton editButton1 = new EditButton();
  IntegerColumn colRateNumber = new IntegerColumn();
  CodLookupColumn colPayTypeReg17 = new CodLookupColumn();
  TextColumn colPayTypeDescrReg17 = new TextColumn();
  DecimalColumn colPerc = new DecimalColumn();
  IntegerColumn colDays = new IntegerColumn();

  LookupController payTypeReg17Controller = new LookupController();
  LookupServerDataLocator payTypeReg17DataLocator = new LookupServerDataLocator();
  EditButton editButton = new EditButton();
  CompaniesComboColumn colCompany = new CompaniesComboColumn();


  public PaymentsGridFrame(GridController controller) {
    grid.setController(controller);
    grid.setGridDataLocator(gridDataLocator);
    gridDataLocator.setServerMethodName("loadPayments");

    instalmentsGrid.setController(new GridController() {

      public Response updateRecords(int[] rowNumbers,ArrayList oldPersistentObjects,ArrayList persistentObjects) throws Exception {
        // verify percentage total...
        VOListTableModel model = instalmentsGrid.getVOListTableModel();
        PaymentInstalmentVO vo = null;
        double tot = 0;
        for(int i=0;i<model.getRowCount();i++) {
          vo = (PaymentInstalmentVO)model.getObjectForRow(i);
          tot += vo.getPercentageREG17().doubleValue();
        }
        if (tot!=100d) {
          return new ErrorResponse("the sum of percentages must be 100");
        }

        return ClientUtils.getData("updatePaymentInstalments",new ArrayList[]{oldPersistentObjects,persistentObjects});
      }

    });
    instalmentsGrid.setGridDataLocator(instalmentsGridDataLocator);
    instalmentsGridDataLocator.setServerMethodName("loadPaymentInstalments");

    try {
      jbInit();
      setSize(750,500);
      setMinimumSize(new Dimension(750,500));

      CustomizedColumns cust = new CustomizedColumns(new BigDecimal(212),grid);

      // lookup payment types for REG10...
      payTypeDataLocator.setGridMethodName("loadPaymentTypes");
      payTypeDataLocator.setValidationMethodName("validatePaymentTypeCode");

      colPayType.setLookupController(payTypeController);
      colPayType.setControllerMethodName("getPaymentTypesList");
      payTypeController.setLookupDataLocator(payTypeDataLocator);
      payTypeController.setFrameTitle("payment types");
      payTypeController.setLookupValueObjectClassName("org.jallinone.registers.payments.java.PaymentTypeVO");
      payTypeController.addLookup2ParentLink("paymentTypeCodeREG11", "paymentTypeCodeReg11REG10");
      payTypeController.addLookup2ParentLink("descriptionSYS10","paymentTypeDescriptionSYS10");
      payTypeController.setAllColumnVisible(false);
      payTypeController.setVisibleColumn("paymentTypeCodeREG11", true);
      payTypeController.setVisibleColumn("descriptionSYS10", true);
      new CustomizedColumns(new BigDecimal(222),payTypeController);

      // lookup payment types for REG17...
      payTypeReg17DataLocator.setGridMethodName("loadPaymentTypes");
      payTypeReg17DataLocator.setValidationMethodName("validatePaymentTypeCode");

      colPayTypeReg17.setLookupController(payTypeReg17Controller);
      colPayTypeReg17.setControllerMethodName("getPaymentTypesList");
      payTypeReg17Controller.setLookupDataLocator(payTypeReg17DataLocator);
      payTypeReg17Controller.setFrameTitle("payment types");
      payTypeReg17Controller.setLookupValueObjectClassName("org.jallinone.registers.payments.java.PaymentTypeVO");
      payTypeReg17Controller.addLookup2ParentLink("paymentTypeCodeREG11", "paymentTypeCodeReg11REG17");
      payTypeReg17Controller.addLookup2ParentLink("descriptionSYS10","paymentTypeDescriptionSYS10");
      payTypeReg17Controller.setAllColumnVisible(false);
      payTypeReg17Controller.setVisibleColumn("paymentTypeCodeREG11", true);
      payTypeReg17Controller.setVisibleColumn("descriptionSYS10", true);
      new CustomizedColumns(new BigDecimal(222),payTypeReg17Controller);

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  public final void reloadData() {
    grid.reloadData();
  }


  private void jbInit() throws Exception {
    grid.setMaxNumberOfRowsOnInsert(50);
    titledBorder1 = new TitledBorder("");
    titledBorder2 = new TitledBorder("");
    grid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    saveButton.setExecuteAsThread(true);
    grid.setValueObjectClassName("org.jallinone.registers.payments.java.PaymentVO");
    instalmentsGrid.setValueObjectClassName("org.jallinone.registers.payments.java.PaymentInstalmentVO");
    this.setTitle(ClientSettings.getInstance().getResources().getResource("payments"));
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    grid.setDeleteButton(deleteButton);
    grid.setEditButton(editButton);
    grid.setExportButton(exportButton);
    grid.setFunctionId("REG10");
    grid.setMaxSortedColumns(3);
    grid.setInsertButton(insertButton);
    grid.setNavBar(navigatorBar);
    grid.setReloadButton(reloadButton);
    grid.setSaveButton(saveButton);
    colPaymentType.setMaxCharacters(20);
    colPaymentType.setTrimText(true);
    colPaymentType.setUpperCase(true);
    colPaymentType.setColumnFilterable(true);
    colPaymentType.setColumnName("paymentCodeREG10");
    colPaymentType.setColumnSortable(true);
    colPaymentType.setEditableOnInsert(true);
    colPaymentType.setPreferredWidth(100);
    colPaymentType.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colPaymentType.setSortingOrder(1);
    colDescr.setColumnFilterable(false);
    colDescr.setColumnName("descriptionSYS10");
    colDescr.setColumnSortable(true);
    colDescr.setEditableOnEdit(true);
    colDescr.setEditableOnInsert(true);
    colDescr.setHeaderColumnName("paymentDescription");
    colDescr.setPreferredWidth(240);
    colInstNr.setColumnDuplicable(false);
    colInstNr.setColumnFilterable(true);
    colInstNr.setColumnName("instalmentNumberREG10");
    colInstNr.setColumnSortable(true);
    colInstNr.setEditableOnInsert(true);
    colInstNr.setPreferredWidth(90);
    colInstNr.setMinValue(1);
    colStep.setMinValue(1);
    colStartDay.setDomainId("START_DAY");
    colStartDay.setColumnFilterable(true);
    colStartDay.setColumnName("startDayREG10");
    colStartDay.setColumnSortable(true);
    colStartDay.setEditableOnEdit(true);
    colStartDay.setEditableOnInsert(true);
    colStartDay.setPreferredWidth(80);
    colStep.setColumnFilterable(true);
    colStep.setColumnName("stepREG10");
    colStep.setColumnSortable(true);
    colStep.setEditableOnInsert(true);
    colStep.setPreferredWidth(70);
    colFirstInstDays.setColumnFilterable(true);
    colFirstInstDays.setColumnName("firstInstalmentDaysREG10");
    colFirstInstDays.setColumnSortable(true);
    colFirstInstDays.setEditableOnInsert(true);
    colFirstInstDays.setPreferredWidth(90);
    colPayType.setColumnFilterable(true);
    colPayType.setColumnName("paymentTypeCodeReg11REG10");
    colPayType.setColumnSortable(true);
    colPayType.setEditableOnEdit(true);
    colPayType.setEditableOnInsert(true);
    colPayType.setPreferredWidth(110);
    colPayType.setMaxCharacters(20);
    colPayTypeDescr.setColumnFilterable(false);
    colPayTypeDescr.setColumnName("paymentTypeDescriptionSYS10");
    colPayTypeDescr.setPreferredWidth(150);
    split.setOrientation(JSplitPane.VERTICAL_SPLIT);
    instalmentsPanel.setLayout(borderLayout2);
    instalmentsPanel.setBorder(titledBorder1);
    titledBorder1.setTitle(ClientSettings.getInstance().getResources().getResource("instalments"));
    titledBorder1.setTitleColor(Color.blue);
    instalmentsButtonsPanel.setLayout(flowLayout2);
    flowLayout2.setAlignment(FlowLayout.LEFT);
    instalmentsGrid.setAutoLoadData(false);
    instalmentsGrid.setEditButton(editButton1);
    instalmentsGrid.setExportButton(exportButton1);
    instalmentsGrid.setFunctionId("REG10");
    instalmentsGrid.setNavBar(navigatorBar1);
    instalmentsGrid.setReloadButton(reloadButton1);
    instalmentsGrid.setSaveButton(saveButton1);
    exportButton1.setText("exportButton1");
    reloadButton1.setText("reloadButton1");
    saveButton1.setText("saveButton1");
    editButton1.setText("editButton1");
    colRateNumber.setColumnName("rateNumberREG17");
    colRateNumber.setPreferredWidth(50);
    colPayTypeReg17.setColumnName("paymentTypeCodeReg11REG17");
    colPayTypeReg17.setEditableOnEdit(true);
    colPayTypeDescrReg17.setColumnName("paymentTypeDescriptionSYS10");
    colPayTypeDescrReg17.setPreferredWidth(200);
    colPerc.setDecimals(5);
    colPerc.setMinValue(0);
    colPerc.setMaxValue(100);
    colPerc.setColumnName("percentageREG17");
    colPerc.setEditableOnEdit(true);
    colDays.setColumnName("instalmentDaysREG17");
    editButton.setText("editButton2");
    colCompany.setColumnName("companyCodeSys01REG10");
    colCompany.setEditableOnInsert(true);
    colCompany.setHeaderColumnName("companyCode");
    colCompany.setFunctionCode("REG10");
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(saveButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(deleteButton, null);
    buttonsPanel.add(exportButton, null);
    buttonsPanel.add(navigatorBar, null);
    paymentsPanel.setLayout(borderLayout1);
    paymentsPanel.add(buttonsPanel,BorderLayout.NORTH);
    paymentsPanel.add(grid,BorderLayout.CENTER);
    split.add(paymentsPanel,JSplitPane.TOP);
    split.add(instalmentsPanel,JSplitPane.BOTTOM);
    instalmentsPanel.add(instalmentsButtonsPanel, BorderLayout.NORTH);
    this.getContentPane().add(split, BorderLayout.CENTER);
    grid.getColumnContainer().add(colCompany, null);
    grid.getColumnContainer().add(colPaymentType, null);
    grid.getColumnContainer().add(colDescr, null);
    grid.getColumnContainer().add(colInstNr, null);
    grid.getColumnContainer().add(colStartDay, null);
    grid.getColumnContainer().add(colStep, null);
    grid.getColumnContainer().add(colFirstInstDays, null);
    grid.getColumnContainer().add(colPayType, null);
    grid.getColumnContainer().add(colPayTypeDescr, null);
    instalmentsPanel.add(instalmentsGrid,  BorderLayout.CENTER);
    instalmentsGrid.getColumnContainer().add(colRateNumber, null);
    instalmentsButtonsPanel.add(editButton1, null);
    instalmentsButtonsPanel.add(saveButton1, null);
    instalmentsButtonsPanel.add(reloadButton1, null);
    instalmentsButtonsPanel.add(exportButton1, null);
    instalmentsButtonsPanel.add(navigatorBar1, null);
    instalmentsGrid.getColumnContainer().add(colPayTypeReg17, null);
    instalmentsGrid.getColumnContainer().add(colPayTypeDescrReg17, null);
    instalmentsGrid.getColumnContainer().add(colPerc, null);
    instalmentsGrid.getColumnContainer().add(colDays, null);
    split.setDividerLocation(260);
  }


  public GridControl getGrid() {
    return grid;
  }


  public GridControl getInstalmentsGrid() {
    return instalmentsGrid;
  }


}
