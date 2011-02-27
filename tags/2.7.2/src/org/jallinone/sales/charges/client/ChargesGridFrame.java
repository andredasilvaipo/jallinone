package org.jallinone.sales.charges.client;

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
import org.jallinone.commons.client.*;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the charges grid frame.</p>
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
public class ChargesGridFrame extends InternalFrame {

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
  TextColumn colCharge = new TextColumn();
  TextColumn colDescr = new TextColumn();
  ExportButton exportButton = new ExportButton();
  DecimalColumn colValue = new DecimalColumn();
  DecimalColumn colPerc = new DecimalColumn();
  CodLookupColumn colCurrencyCod = new CodLookupColumn();
  CodLookupColumn colVatCode = new CodLookupColumn();
  TextColumn colVatDesc = new TextColumn();
  DecimalColumn colVatValue = new DecimalColumn();
  CompaniesComboColumn colCompanycode = new CompaniesComboColumn();

  LookupController currencyController = new LookupController();
  LookupServerDataLocator currencyDataLocator = new LookupServerDataLocator();

  LookupController vatController = new LookupController();
  LookupServerDataLocator vatDataLocator = new LookupServerDataLocator();
  DecimalColumn colVatDeductible = new DecimalColumn();


  public ChargesGridFrame(GridController controller) {
    grid.setController(controller);
    grid.setGridDataLocator(gridDataLocator);
    gridDataLocator.setServerMethodName("loadCharges");
    try {
      jbInit();
      setSize(750,500);
      setMinimumSize(new Dimension(750,500));


      // currency lookup...
      currencyDataLocator.setGridMethodName("loadCurrencies");
      currencyDataLocator.setValidationMethodName("validateCurrencyCode");
      colCurrencyCod.setLookupController(currencyController);
      colCurrencyCod.setControllerMethodName("getCurrenciesList");
      currencyController.setLookupDataLocator(currencyDataLocator);
      currencyController.setFrameTitle("currencies");
      currencyController.setLookupValueObjectClassName("org.jallinone.registers.currency.java.CurrencyVO");
      currencyController.addLookup2ParentLink("currencyCodeREG03", "currencyCodeReg03SAL06");
      currencyController.setAllColumnVisible(false);
      currencyController.setVisibleColumn("currencyCodeREG03", true);
      currencyController.setVisibleColumn("currencySymbolREG03", true);
      new CustomizedColumns(new BigDecimal(182),currencyController);

      // lookup vat...
      vatDataLocator.setGridMethodName("loadVats");
      vatDataLocator.setValidationMethodName("validateVatCode");
      colVatCode.setLookupController(vatController);
      colVatCode.setControllerMethodName("getVatsList");
      vatController.setLookupDataLocator(vatDataLocator);
      vatController.setFrameTitle("vats");
      vatController.setLookupValueObjectClassName("org.jallinone.registers.vat.java.VatVO");
      vatController.addLookup2ParentLink("vatCodeREG01", "vatCodeReg01SAL06");
      vatController.addLookup2ParentLink("descriptionSYS10", "vatDescriptionSYS10");
      vatController.addLookup2ParentLink("valueREG01","vatValueREG01");
      vatController.addLookup2ParentLink("deductibleREG01","vatDeductibleREG01");
      vatController.setAllColumnVisible(false);
      vatController.setVisibleColumn("vatCodeREG01", true);
      vatController.setVisibleColumn("descriptionSYS10", true);
      vatController.setVisibleColumn("valueREG01", true);
      CustomizedColumns vatCust = new CustomizedColumns(new BigDecimal(162),vatController);


      CustomizedColumns cust = new CustomizedColumns(new BigDecimal(292),grid);

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
    grid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    saveButton.setExecuteAsThread(true);
    grid.setValueObjectClassName("org.jallinone.sales.charges.java.ChargeVO");
    grid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    this.setTitle(ClientSettings.getInstance().getResources().getResource("charges"));
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    grid.setDeleteButton(deleteButton);
    grid.setEditButton(editButton);
    grid.setExportButton(exportButton);
    grid.setFunctionId("SAL06");
    grid.setMaxSortedColumns(3);
    grid.setInsertButton(insertButton);
    grid.setNavBar(navigatorBar);
    grid.setReloadButton(reloadButton);
    grid.setSaveButton(saveButton);
    colCharge.setMaxCharacters(20);
    colCharge.setTrimText(true);
    colCharge.setUpperCase(true);
    colCharge.setColumnFilterable(true);
    colCharge.setColumnName("chargeCodeSAL06");
    colCharge.setColumnSortable(true);
    colCharge.setEditableOnInsert(true);
    colCharge.setPreferredWidth(90);
    colCharge.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colCharge.setSortingOrder(1);
    colDescr.setColumnFilterable(false);
    colDescr.setColumnName("descriptionSYS10");
    colDescr.setColumnSortable(true);
    colDescr.setEditableOnEdit(true);
    colDescr.setEditableOnInsert(true);
    colDescr.setHeaderColumnName("chargeDescription");
    colDescr.setPreferredWidth(190);
    colValue.setDecimals(5);
    colValue.setMinValue(0.0);
    colValue.setColumnDuplicable(true);
    colValue.setColumnFilterable(true);
    colValue.setColumnName("valueSAL06");
    colValue.setColumnRequired(false);
    colValue.setColumnSortable(true);
    colValue.setEditableOnEdit(true);
    colValue.setEditableOnInsert(true);
    colValue.setPreferredWidth(70);
    colPerc.setDecimals(5);
    colPerc.setGrouping(false);
    colPerc.setMaxValue(100.0);
    colPerc.setMinValue(0.0);
    colPerc.setColumnDuplicable(true);
    colPerc.setColumnFilterable(true);
    colPerc.setColumnName("percSAL06");
    colPerc.setColumnRequired(false);
    colPerc.setColumnSortable(true);
    colPerc.setEditableOnEdit(true);
    colPerc.setEditableOnInsert(true);
    colPerc.setHeaderTextHorizontalAlignment(SwingConstants.CENTER);
    colPerc.setPreferredWidth(60);
    colCurrencyCod.setColumnDuplicable(true);
    colCurrencyCod.setColumnFilterable(true);
    colCurrencyCod.setColumnName("currencyCodeReg03SAL06");
    colCurrencyCod.setEditableOnEdit(true);
    colCurrencyCod.setEditableOnInsert(true);
    colCurrencyCod.setHeaderColumnName("currencyCodeReg03SAL06");
    colCurrencyCod.setPreferredWidth(90);
    colCurrencyCod.setMaxCharacters(20);
    colVatCode.setColumnDuplicable(true);
    colVatCode.setColumnFilterable(true);
    colVatCode.setColumnName("vatCodeReg01SAL06");
    colVatCode.setColumnRequired(false);
    colVatCode.setColumnSortable(true);
    colVatCode.setEditableOnEdit(true);
    colVatCode.setEditableOnInsert(true);
    colVatCode.setPreferredWidth(70);
    colVatCode.setMaxCharacters(20);
    colVatDesc.setColumnDuplicable(true);
    colVatDesc.setColumnName("vatDescriptionSYS10");
    colVatDesc.setColumnRequired(false);
    colVatDesc.setColumnSortable(false);
    colVatDesc.setEditableOnInsert(false);
    colVatDesc.setPreferredWidth(150);
    colVatValue.setColumnDuplicable(true);
    colVatValue.setColumnFilterable(true);
    colVatValue.setColumnName("vatValueREG01");
    colVatValue.setColumnRequired(false);
    colVatValue.setColumnSortable(true);
    colVatValue.setEditableOnEdit(false);
    colVatValue.setPreferredWidth(80);
    colCompanycode.setColumnDuplicable(true);
    colCompanycode.setColumnFilterable(true);
    colCompanycode.setColumnName("companyCodeSys01SAL06");
    colCompanycode.setColumnSortable(true);
    colCompanycode.setEditableOnInsert(true);
    colCompanycode.setPreferredWidth(100);
    colCompanycode.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colCompanycode.setSortingOrder(0);
    colCompanycode.setFunctionCode("SAL06");
    colVatDeductible.setColumnDuplicable(true);
    colVatDeductible.setColumnFilterable(true);
    colVatDeductible.setColumnName("vatDeductibleREG01");
    colVatDeductible.setColumnRequired(false);
    colVatDeductible.setColumnSortable(true);
    colVatDeductible.setPreferredWidth(80);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(saveButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(deleteButton, null);
    buttonsPanel.add(exportButton, null);
    buttonsPanel.add(navigatorBar, null);
    this.getContentPane().add(grid, BorderLayout.CENTER);
    grid.getColumnContainer().add(colCompanycode, null);
    grid.getColumnContainer().add(colCharge, null);
    grid.getColumnContainer().add(colDescr, null);
    grid.getColumnContainer().add(colValue, null);
    grid.getColumnContainer().add(colPerc, null);
    grid.getColumnContainer().add(colCurrencyCod, null);
    grid.getColumnContainer().add(colVatCode, null);
    grid.getColumnContainer().add(colVatDesc, null);
    grid.getColumnContainer().add(colVatValue, null);
    grid.getColumnContainer().add(colVatDeductible, null);
  }
  public GridControl getGrid() {
    return grid;
  }

}
