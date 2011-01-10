package org.jallinone.production.machineries.client;

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
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the machineries grid frame.</p>
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
public class MachineriesGridFrame extends InternalFrame {

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
  TextColumn colMachinery = new TextColumn();
  TextColumn colDescr = new TextColumn();
  ExportButton exportButton = new ExportButton();
  CompaniesComboColumn colCompany = new CompaniesComboColumn();
  CodLookupColumn colCurrency = new CodLookupColumn();
  DecimalColumn colValue = new DecimalColumn();
  DecimalColumn colDuration = new DecimalColumn();
  CheckBoxColumn colFiniteCap = new CheckBoxColumn();
  LookupController currencyController = new LookupController();
  LookupServerDataLocator currencyDataLocator = new LookupServerDataLocator();


  public MachineriesGridFrame(GridController controller) {
    grid.setController(controller);
    grid.setGridDataLocator(gridDataLocator);
    gridDataLocator.setServerMethodName("loadMachineries");
    try {
      jbInit();
      setSize(750,400);
      setMinimumSize(new Dimension(750,400));

      // currency lookup...
      currencyDataLocator.setGridMethodName("loadCurrencies");
      currencyDataLocator.setValidationMethodName("validateCurrencyCode");
      colCurrency.setLookupController(currencyController);
      colCurrency.setControllerMethodName("getCurrenciesList");
      currencyController.setLookupDataLocator(currencyDataLocator);
      currencyController.setFrameTitle("currencies");
      currencyController.setLookupValueObjectClassName("org.jallinone.registers.currency.java.CurrencyVO");
      currencyController.addLookup2ParentLink("currencyCodeREG03", "currencyCodeReg03PRO03");
      currencyController.setAllColumnVisible(false);
      currencyController.setVisibleColumn("currencyCodeREG03", true);
      currencyController.setVisibleColumn("currencySymbolREG03", true);
      new CustomizedColumns(new BigDecimal(182),currencyController);

      CustomizedColumns cust = new CustomizedColumns(ApplicationConsts.ID_MACHINERIES,grid);

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
    grid.setValueObjectClassName("org.jallinone.production.machineries.java.MachineryVO");
    this.setTitle(ClientSettings.getInstance().getResources().getResource("machineries"));
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    grid.setDeleteButton(deleteButton);
    grid.setEditButton(editButton);
    grid.setExportButton(exportButton);
    grid.setFunctionId("PRO03");
    grid.setMaxSortedColumns(3);
    grid.setInsertButton(insertButton);
    grid.setNavBar(navigatorBar);
    grid.setReloadButton(reloadButton);
    grid.setSaveButton(saveButton);
    colMachinery.setMaxCharacters(20);
    colMachinery.setTrimText(true);
    colMachinery.setUpperCase(true);
    colMachinery.setColumnFilterable(true);
    colMachinery.setColumnName("machineryCodePRO03");
    colMachinery.setColumnSortable(true);
    colMachinery.setEditableOnInsert(true);
    colMachinery.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colMachinery.setSortingOrder(1);
    colDescr.setColumnFilterable(false);
    colDescr.setColumnName("descriptionSYS10");
    colDescr.setColumnSortable(true);
    colDescr.setEditableOnEdit(true);
    colDescr.setEditableOnInsert(true);
    colDescr.setHeaderColumnName("machineryDescription");
    colDescr.setPreferredWidth(250);
    colCompany.setColumnName("companyCodeSys01PRO03");
    colCompany.setFunctionCode("PRO03");
    colCompany.setEditableOnEdit(false);
    colCompany.setEditableOnInsert(true);
    colCurrency.setColumnName("currencyCodeReg03PRO03");
    colCurrency.setEditableOnEdit(false);
    colCurrency.setEditableOnInsert(true);
    colValue.setColumnName("valuePRO03");
    colValue.setEditableOnEdit(true);
    colValue.setEditableOnInsert(true);
    colDuration.setColumnName("durationPRO03");
    colDuration.setEditableOnEdit(true);
    colDuration.setEditableOnInsert(true);
    colFiniteCap.setColumnName("finiteCapacityPRO03");
    colFiniteCap.setColumnRequired(false);
    colFiniteCap.setEditableOnEdit(true);
    colFiniteCap.setEditableOnInsert(true);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(saveButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(deleteButton, null);
    buttonsPanel.add(exportButton, null);
    buttonsPanel.add(navigatorBar, null);
    this.getContentPane().add(grid, BorderLayout.CENTER);
    grid.getColumnContainer().add(colCompany, null);
    grid.getColumnContainer().add(colMachinery, null);
    grid.getColumnContainer().add(colDescr, null);
    grid.getColumnContainer().add(colCurrency, null);
    grid.getColumnContainer().add(colValue, null);
    grid.getColumnContainer().add(colDuration, null);
    grid.getColumnContainer().add(colFiniteCap, null);
  }

}
