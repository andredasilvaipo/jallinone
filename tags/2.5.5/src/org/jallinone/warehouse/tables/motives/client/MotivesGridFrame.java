package org.jallinone.warehouse.tables.motives.client;

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
import org.jallinone.commons.java.ApplicationConsts;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the warehouse motives grid frame.</p>
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
public class MotivesGridFrame extends InternalFrame {

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
  TextColumn colMotive = new TextColumn();
  TextColumn colDescr = new TextColumn();
  ExportButton exportButton = new ExportButton();
  ComboColumn colItemType = new ComboColumn();
  ComboColumn colQtySign = new ComboColumn();


  public MotivesGridFrame(GridController controller) {
    grid.setController(controller);
    grid.setGridDataLocator(gridDataLocator);
    gridDataLocator.setServerMethodName("loadWarehouseMotives");
    try {
      jbInit();
      setSize(680,400);
      setMinimumSize(new Dimension(580,400));

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
    grid.setValueObjectClassName("org.jallinone.warehouse.tables.motives.java.MotiveVO");
    this.setTitle(ClientSettings.getInstance().getResources().getResource("warehouse motives"));
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    grid.setDeleteButton(deleteButton);
    grid.setEditButton(editButton);
    grid.setExportButton(exportButton);
    grid.setFunctionId("WAR04");
    grid.setMaxSortedColumns(3);
    grid.setInsertButton(insertButton);
    grid.setNavBar(navigatorBar);
    grid.setReloadButton(reloadButton);
    grid.setSaveButton(saveButton);
		colMotive.setPreferredWidth(200);
    colMotive.setMaxCharacters(20);
    colMotive.setTrimText(true);
    colMotive.setUpperCase(true);
    colMotive.setColumnFilterable(true);
    colMotive.setColumnName("warehouseMotiveWAR04");
    colMotive.setColumnSortable(true);
    colMotive.setEditableOnInsert(true);
    colMotive.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colMotive.setSortingOrder(1);
    colDescr.setColumnFilterable(false);
    colDescr.setColumnName("descriptionSYS10");
    colDescr.setColumnSortable(true);
    colDescr.setEditableOnEdit(true);
    colDescr.setEditableOnInsert(true);
    colDescr.setHeaderColumnName("motiveDescription");
    colDescr.setPreferredWidth(290);
    colItemType.setDomainId("WAR_ITEM_TYPE");
    colItemType.setColumnDuplicable(true);
    colItemType.setColumnFilterable(true);
    colItemType.setColumnName("itemTypeWAR04");
    colItemType.setColumnSortable(true);
    colItemType.setEditableOnEdit(false);
    colItemType.setEditableOnInsert(true);
    colItemType.setPreferredWidth(80);
    colQtySign.setDomainId("WAR_QTY_SIGN");
    colQtySign.setColumnFilterable(true);
    colQtySign.setColumnName("qtySignWAR04");
    colQtySign.setColumnSortable(true);
    colQtySign.setEditableOnInsert(true);
    colQtySign.setPreferredWidth(80);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(saveButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(deleteButton, null);
    buttonsPanel.add(exportButton, null);
    buttonsPanel.add(navigatorBar, null);
    this.getContentPane().add(grid, BorderLayout.CENTER);
    grid.getColumnContainer().add(colMotive, null);
    grid.getColumnContainer().add(colDescr, null);
    grid.getColumnContainer().add(colItemType, null);
    grid.getColumnContainer().add(colQtySign, null);
  }

}
