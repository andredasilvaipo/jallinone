package org.jallinone.registers.measure.client;

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
import org.jallinone.commons.java.ApplicationConsts;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the measures grid frame.</p>
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
public class MeasuresGridFrame extends InternalFrame {

  JPanel buttonsPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  InsertButton insertButton = new InsertButton();
  ReloadButton reloadButton = new ReloadButton();
  DeleteButton deleteButton = new DeleteButton();
  NavigatorBar navigatorBar = new NavigatorBar();
  GridControl grid = new GridControl();

  EditButton editButton = new EditButton();
  SaveButton saveButton = new SaveButton();

  TextColumn colMeasure = new TextColumn();
  IntegerColumn colDecimals = new IntegerColumn();
  ExportButton exportButton = new ExportButton();
  JPanel convPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  TitledBorder titledBorder1;
  JPanel convButtonsPanel = new JPanel();
  FlowLayout flowLayout2 = new FlowLayout();
  GridControl convGridControl = new GridControl();
  TextColumn colUm = new TextColumn();
  TextColumn colConvUM = new TextColumn();
  DecimalColumn colConv = new DecimalColumn();

  /** grid data locator */
  private ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();

  /** conv grid data locator */
  private ServerGridDataLocator convGridDataLocator = new ServerGridDataLocator();
  ReloadButton reloadButton1 = new ReloadButton();
  SaveButton saveButton1 = new SaveButton();
  EditButton editButton1 = new EditButton();


  public MeasuresGridFrame(GridController controller) {
    grid.setController(controller);
    grid.setGridDataLocator(gridDataLocator);
    gridDataLocator.setServerMethodName("loadMeasures");

    convGridControl.setController(new MeasuresConvController(this));
    convGridControl.setGridDataLocator(convGridDataLocator);
    convGridDataLocator.setServerMethodName("loadMeasureConvs");

    try {
      jbInit();
      setSize(400,500);
      setMinimumSize(new Dimension(400,500));

      CustomizedColumns cust = new CustomizedColumns(ApplicationConsts.ID_UM_GRID,grid);

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
    grid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    saveButton.setExecuteAsThread(true);
    grid.setValueObjectClassName("org.jallinone.registers.measure.java.MeasureVO");
    convGridControl.setValueObjectClassName("org.jallinone.registers.measure.java.MeasureConvVO");
    this.setTitle(ClientSettings.getInstance().getResources().getResource("measures"));
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    grid.setDeleteButton(deleteButton);
    grid.setEditButton(editButton);
    grid.setExportButton(exportButton);
    grid.setFunctionId("REG02");
    grid.setMaxSortedColumns(3);
    grid.setInsertButton(insertButton);
    grid.setNavBar(navigatorBar);
    grid.setReloadButton(reloadButton);
    grid.setSaveButton(saveButton);
    colMeasure.setMaxCharacters(20);
    colMeasure.setTrimText(true);
    colMeasure.setUpperCase(true);
    colMeasure.setColumnFilterable(true);
    colMeasure.setColumnName("umCodeREG02");
    colMeasure.setColumnSortable(true);
    colMeasure.setEditableOnInsert(true);
    colMeasure.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colMeasure.setSortingOrder(1);
    colDecimals.setColumnFilterable(false);
    colDecimals.setColumnName("decimalsREG02");
    colDecimals.setColumnSortable(true);
    colDecimals.setEditableOnEdit(true);
    colDecimals.setEditableOnInsert(true);
    colDecimals.setPreferredWidth(100);
    convPanel.setLayout(borderLayout1);
    convPanel.setBorder(titledBorder1);
    titledBorder1.setTitleColor(Color.blue);
    titledBorder1.setTitle(ClientSettings.getInstance().getResources().getResource("measure conversions"));
    convButtonsPanel.setLayout(flowLayout2);
    flowLayout2.setAlignment(FlowLayout.LEFT);
    convGridControl.setAutoLoadData(false);
    convGridControl.setEditButton(editButton1);
    convGridControl.setFunctionId("REG02");
    convGridControl.setAllowColumnsPermission(false);
    convGridControl.setAllowColumnsProfile(false);
    convGridControl.setPreferredSize(new Dimension(320, 200));
    convGridControl.setReloadButton(reloadButton1);
    convGridControl.setSaveButton(saveButton1);
    colUm.setColumnName("umCodeREG05");
    colConvUM.setColumnName("umCodeReg02REG05");
    colConv.setDecimals(5);
    colConv.setColumnName("valueREG05");
    colConv.setColumnRequired(false);
    colConv.setEditableOnEdit(true);
    colConv.setPreferredWidth(150);
    reloadButton1.setText("reloadButton1");
    saveButton1.setText("saveButton1");
    editButton1.setText("editButton1");
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(saveButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(deleteButton, null);
    buttonsPanel.add(exportButton, null);
    buttonsPanel.add(navigatorBar, null);
    this.getContentPane().add(grid, BorderLayout.CENTER);
    grid.getColumnContainer().add(colMeasure, null);
    grid.getColumnContainer().add(colDecimals, null);
    this.getContentPane().add(convPanel,  BorderLayout.SOUTH);
    convPanel.add(convButtonsPanel, BorderLayout.NORTH);
    convButtonsPanel.add(editButton1, null);
    convButtonsPanel.add(saveButton1, null);
    convButtonsPanel.add(reloadButton1, null);
    convPanel.add(convGridControl,  BorderLayout.CENTER);
    convGridControl.getColumnContainer().add(colUm, null);
    convGridControl.getColumnContainer().add(colConvUM, null);
    convGridControl.getColumnContainer().add(colConv, null);
  }


  public GridControl getGrid() {
    return grid;
  }


  public GridControl getConvGridControl() {
    return convGridControl;
  }





}
