package org.jallinone.registers.currency.client;

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
import org.jallinone.commons.client.CustomizedColumns;
import java.math.BigDecimal;
import javax.swing.border.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the currencies grid frame.</p>
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
public class CurrenciesGridFrame extends InternalFrame {

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
  TextColumn colCurrency = new TextColumn();
  TextColumn colCurrSymbol = new TextColumn();
  ExportButton exportButton = new ExportButton();
  TextColumn colDecSymbol = new TextColumn();
  TextColumn colThSymbol = new TextColumn();
  JPanel convPanel = new JPanel();
  TitledBorder titledBorder1;
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel convButtonsPanel = new JPanel();
  FlowLayout flowLayout2 = new FlowLayout();
  GridControl convGridControl = new GridControl();
  TextColumn colCurrCode = new TextColumn();
  TextColumn colCurrCode2 = new TextColumn();
  DecimalColumn decimalColumn1 = new DecimalColumn();
  EditButton editButton1 = new EditButton();
  SaveButton saveButton1 = new SaveButton();
  ReloadButton reloadButton1 = new ReloadButton();

  /** conv grid data locator */
  private ServerGridDataLocator convGridDataLocator = new ServerGridDataLocator();
  IntegerColumn colDec = new IntegerColumn();



  public CurrenciesGridFrame(GridController controller) {
    grid.setController(controller);
    grid.setGridDataLocator(gridDataLocator);
    gridDataLocator.setServerMethodName("loadCurrencies");
    try {
      jbInit();
      setSize(600,550);
      setMinimumSize(new Dimension(600,550));

      convGridControl.setController(new CurrenciesConvController(this));
      convGridControl.setGridDataLocator(convGridDataLocator);
      convGridDataLocator.setServerMethodName("loadCurrencyConvs");

      CustomizedColumns cust = new CustomizedColumns(new BigDecimal(182),grid);

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  public final void reloadData() {
    grid.reloadData();
  }


  private void jbInit() throws Exception {
    titledBorder1 = new TitledBorder("");
    grid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    saveButton.setExecuteAsThread(true);
    convGridControl.setValueObjectClassName("org.jallinone.registers.currency.java.CurrencyConvVO");
    grid.setValueObjectClassName("org.jallinone.registers.currency.java.CurrencyVO");
    this.setTitle(ClientSettings.getInstance().getResources().getResource("currencies"));
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    grid.setDeleteButton(deleteButton);
    grid.setEditButton(editButton);
    grid.setExportButton(exportButton);
    grid.setFunctionId("REG03");
    grid.setMaxSortedColumns(3);
    grid.setInsertButton(insertButton);
    grid.setNavBar(navigatorBar);
    grid.setReloadButton(reloadButton);
    grid.setSaveButton(saveButton);
    colCurrency.setMaxCharacters(20);
    colCurrency.setTrimText(true);
    colCurrency.setUpperCase(true);
    colCurrency.setColumnFilterable(true);
    colCurrency.setColumnName("currencyCodeREG03");
    colCurrency.setColumnSortable(true);
    colCurrency.setEditableOnInsert(true);
    colCurrency.setPreferredWidth(120);
    colCurrency.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colCurrency.setSortingOrder(1);
    colCurrSymbol.setMaxCharacters(3);
    colCurrSymbol.setColumnFilterable(false);
    colCurrSymbol.setColumnName("currencySymbolREG03");
    colCurrSymbol.setColumnSortable(true);
    colCurrSymbol.setEditableOnEdit(true);
    colCurrSymbol.setEditableOnInsert(true);
    colCurrSymbol.setPreferredWidth(120);
    colDecSymbol.setMaxCharacters(1);
    colDecSymbol.setRpadding(false);
    colDecSymbol.setColumnName("decimalSymbolREG03");
    colDecSymbol.setEditableOnEdit(true);
    colDecSymbol.setEditableOnInsert(true);
    colDecSymbol.setPreferredWidth(120);
    colThSymbol.setMaxCharacters(1);
    colThSymbol.setColumnName("thousandSymbolREG03");
    colThSymbol.setEditableOnEdit(true);
    colThSymbol.setEditableOnInsert(true);
    colThSymbol.setPreferredWidth(120);
    convPanel.setBorder(titledBorder1);
    convPanel.setLayout(borderLayout1);
    titledBorder1.setTitleColor(Color.blue);
    titledBorder1.setTitle(ClientSettings.getInstance().getResources().getResource("currency conversions"));
    convButtonsPanel.setLayout(flowLayout2);
    flowLayout2.setAlignment(FlowLayout.LEFT);
    convGridControl.setAutoLoadData(false);
    convGridControl.setEditButton(editButton1);
    convGridControl.setFunctionId("REG03");
    convGridControl.setPreferredSize(new Dimension(340, 200));
    convGridControl.setReloadButton(reloadButton1);
    convGridControl.setSaveButton(saveButton1);
    decimalColumn1.setDecimals(5);
    decimalColumn1.setColumnName("valueREG06");
    decimalColumn1.setColumnRequired(false);
    decimalColumn1.setEditableOnEdit(true);
    decimalColumn1.setPreferredWidth(120);
    colCurrCode.setColumnName("currencyCodeReg03REG06");
    colCurrCode2.setColumnName("currencyCode2Reg03REG06");
    colDec.setColumnDuplicable(true);
    colDec.setColumnFilterable(true);
    colDec.setColumnName("decimalsREG03");
    colDec.setColumnSortable(true);
    colDec.setEditableOnEdit(true);
    colDec.setEditableOnInsert(true);
    colDec.setHeaderColumnName("decimalsREG03");
    colDec.setPreferredWidth(90);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(saveButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(deleteButton, null);
    buttonsPanel.add(exportButton, null);
    buttonsPanel.add(navigatorBar, null);
    this.getContentPane().add(grid, BorderLayout.CENTER);
    grid.getColumnContainer().add(colCurrency, null);
    grid.getColumnContainer().add(colCurrSymbol, null);
    grid.getColumnContainer().add(colDecSymbol, null);
    grid.getColumnContainer().add(colThSymbol, null);
    grid.getColumnContainer().add(colDec, null);
    this.getContentPane().add(convPanel,  BorderLayout.SOUTH);
    convPanel.add(convButtonsPanel, BorderLayout.NORTH);
    convButtonsPanel.add(editButton1, null);
    convButtonsPanel.add(saveButton1, null);
    convButtonsPanel.add(reloadButton1, null);
    convPanel.add(convGridControl,  BorderLayout.CENTER);
    convGridControl.getColumnContainer().add(colCurrCode, null);
    convGridControl.getColumnContainer().add(colCurrCode2, null);
    convGridControl.getColumnContainer().add(decimalColumn1, null);
  }
  public GridControl getGrid() {
    return grid;
  }
  public GridControl getConvGridControl() {
    return convGridControl;
  }

}
