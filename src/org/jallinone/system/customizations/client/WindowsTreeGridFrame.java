package org.jallinone.system.customizations.client;

import org.openswing.swing.mdi.client.InternalFrame;
import org.openswing.swing.tree.client.*;
import java.awt.*;
import org.openswing.swing.mdi.client.MDIFrame;
import javax.swing.*;
import org.openswing.swing.client.*;
import org.openswing.swing.table.java.ServerGridDataLocator;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.util.client.ClientSettings;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Customizations tree window: windows are grouped by function code.</p>
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
public class WindowsTreeGridFrame extends InternalFrame {

  /** tree panel */
  private TreePanel treePanel = new TreePanel();

  /** tree controller */
  private WindowsController controller = null;

  /** tree data locator */
  private TreeServerDataLocator treeDataLocator = new TreeServerDataLocator();

  /** grid data locator */
  private ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();

  private JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

  private JPanel gridPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel buttonsPanel = new JPanel();
  GridControl grid = new GridControl();
  FlowLayout flowLayout1 = new FlowLayout();
  InsertButton insertButton = new InsertButton();
  EditButton editButton = new EditButton();
  CopyButton copyButton = new CopyButton();
  ReloadButton reloadButton = new ReloadButton();
  DeleteButton deleteButton = new DeleteButton();
  NavigatorBar navigatorBar = new NavigatorBar();
  SaveButton saveButton = new SaveButton();
  TextColumn colColumnName = new TextColumn();
  ComboColumn colColumnType = new ComboColumn();
  IntegerColumn colColumnSize = new IntegerColumn();
  IntegerColumn colColumnDec = new IntegerColumn();
  TextColumn colDescription = new TextColumn();


  public WindowsTreeGridFrame(WindowsController controller) {
    this.controller = controller;
    try {
      jbInit();

      treeDataLocator.setServerMethodName("loadWindows");
      treeDataLocator.setNodeNameAttribute("descriptionSYS10");

      treePanel.setLeavesImageName(ClientSettings.getInstance().PERC_TREE_NODE);
      treePanel.setTreeDataLocator(treeDataLocator);
      treePanel.setTreeController(controller);

      gridDataLocator.setServerMethodName("loadWindowCustomizations");
      grid.setAutoLoadData(false);
      grid.setController(controller);
      grid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

      setSize(750,500);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    grid.setMaxNumberOfRowsOnInsert(50);
    grid.setValueObjectClassName("org.jallinone.system.customizations.java.WindowCustomizationVO");
    this.setTitle(ClientSettings.getInstance().getResources().getResource("customizable windows"));
    gridPanel.setLayout(borderLayout1);
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    grid.setCopyButton(copyButton);
    grid.setDeleteButton(deleteButton);
    grid.setEditButton(editButton);
    grid.setFunctionId("SYS13");
    grid.setGridDataLocator(gridDataLocator);
    grid.setInsertButton(insertButton);
    grid.setNavBar(navigatorBar);
    grid.setReloadButton(reloadButton);
    grid.setSaveButton(saveButton);
    colColumnName.setTrimText(true);
    colColumnName.setUpperCase(true);
    colColumnName.setColumnName("columnNameSYS12");
    colColumnName.setEditableOnInsert(true);
    colColumnName.setPreferredWidth(150);
    colColumnType.setDomainId("COLUMN_TYPE");
    colColumnType.setColumnDuplicable(true);
    colColumnType.setColumnName("columnTypeSYS12");
    colColumnType.setEditableOnInsert(true);
    colColumnType.setPreferredWidth(70);
    colColumnSize.setColumnDuplicable(true);
    colColumnSize.setColumnName("columnSizeSYS12");
    colColumnSize.setColumnRequired(false);
    colColumnSize.setEditableOnEdit(false);
    colColumnSize.setEditableOnInsert(true);
    colColumnSize.setPreferredWidth(50);
    colColumnDec.setColumnDuplicable(true);
    colColumnDec.setColumnName("columnDecSYS12");
    colColumnDec.setColumnRequired(false);
    colColumnDec.setEditableOnInsert(true);
    colColumnDec.setPreferredWidth(50);
    colDescription.setColumnDuplicable(false);
    colDescription.setColumnName("descriptionSYS10");
    colDescription.setEditableOnEdit(true);
    colDescription.setEditableOnInsert(true);
    colDescription.setPreferredWidth(200);
    split.add(treePanel,split.LEFT);
    split.add(gridPanel,split.RIGHT);
    gridPanel.add(buttonsPanel, BorderLayout.NORTH);
    gridPanel.add(grid, BorderLayout.CENTER);
    grid.getColumnContainer().add(colColumnName, null);
    grid.getColumnContainer().add(colColumnType, null);
    split.setDividerLocation(250);
    this.getContentPane().add(split, BorderLayout.CENTER);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(copyButton, null);
    buttonsPanel.add(saveButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(deleteButton, null);
    buttonsPanel.add(navigatorBar, null);
    grid.getColumnContainer().add(colColumnSize, null);
    grid.getColumnContainer().add(colColumnDec, null);
    grid.getColumnContainer().add(colDescription, null);
  }


  public final GridControl getGrid() {
    return grid;
  }

}