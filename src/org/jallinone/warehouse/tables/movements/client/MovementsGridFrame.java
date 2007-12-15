package org.jallinone.warehouse.tables.movements.client;

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
 * <p>Description: This class is the warehouse movements grid frame.</p>
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
public class MovementsGridFrame extends InternalFrame {

  JPanel buttonsPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  ReloadButton reloadButton = new ReloadButton();
  NavigatorBar navigatorBar = new NavigatorBar();
  GridControl grid = new GridControl();


  /** grid data locator */
  private ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();
  TextColumn colWarehouseCode = new TextColumn();
  TextColumn colWarehouseDescr = new TextColumn();
  ExportButton exportButton = new ExportButton();
  ComboColumn colItemType = new ComboColumn();
  ComboColumn colQtySign = new ComboColumn();
  DateTimeColumn colMovDate = new DateTimeColumn();
  TextColumn colItemCode = new TextColumn();
  TextColumn colItemDescr = new TextColumn();
  DecimalColumn colDeltaQty = new DecimalColumn();
  TextColumn colPosition = new TextColumn();
  TextColumn colUsername = new TextColumn();
  TextColumn colNote = new TextColumn();
  TextColumn colMotiveCode = new TextColumn();
  TextColumn colMotiveDescr = new TextColumn();


  public MovementsGridFrame(GridController controller) {
    grid.setController(controller);
    grid.setGridDataLocator(gridDataLocator);
    gridDataLocator.setServerMethodName("loadWarehouseMovements");
    try {
      jbInit();
      setSize(750,400);
      setMinimumSize(new Dimension(750,400));

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  public final void reloadData() {
    grid.reloadData();
  }


  private void jbInit() throws Exception {
    grid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    grid.setValueObjectClassName("org.jallinone.warehouse.tables.movements.java.MovementVO");
    this.setTitle(ClientSettings.getInstance().getResources().getResource("warehouse movements"));
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    grid.setExportButton(exportButton);
    grid.setFunctionId("WAR02");
    grid.setMaxSortedColumns(3);
    grid.setNavBar(navigatorBar);
    grid.setReloadButton(reloadButton);
    colWarehouseCode.setMaxCharacters(20);
    colWarehouseCode.setTrimText(true);
    colWarehouseCode.setUpperCase(true);
    colWarehouseCode.setColumnFilterable(true);
    colWarehouseCode.setColumnName("warehouseCodeWar01WAR02");
    colWarehouseCode.setColumnSortable(true);
    colWarehouseCode.setEditableOnInsert(true);
    colWarehouseCode.setHeaderColumnName("warehouseCodeWar01DOC08");
    colWarehouseCode.setPreferredWidth(70);
    colWarehouseCode.setSortVersus(org.openswing.swing.util.java.Consts.NO_SORTED);
    colWarehouseCode.setSortingOrder(0);
    colWarehouseDescr.setColumnFilterable(false);
    colWarehouseDescr.setColumnName("descriptionWAR01");
    colWarehouseDescr.setColumnSortable(false);
    colWarehouseDescr.setEditableOnEdit(true);
    colWarehouseDescr.setEditableOnInsert(true);
    colWarehouseDescr.setHeaderColumnName("descriptionWAR01");
    colWarehouseDescr.setPreferredWidth(150);
    colItemType.setDomainId("WAR_ITEM_TYPE");
    colItemType.setColumnDuplicable(true);
    colItemType.setColumnFilterable(true);
    colItemType.setColumnName("itemTypeWAR04");
    colItemType.setColumnSortable(true);
    colItemType.setEditableOnEdit(false);
    colItemType.setEditableOnInsert(true);
    colItemType.setHeaderColumnName("type");
    colItemType.setPreferredWidth(70);
    colQtySign.setDomainId("WAR_QTY_SIGN");
    colQtySign.setColumnFilterable(true);
    colQtySign.setColumnName("qtySignWAR04");
    colQtySign.setColumnSortable(true);
    colQtySign.setEditableOnInsert(true);
    colQtySign.setHeaderColumnName("sign");
    colQtySign.setPreferredWidth(60);
    colMovDate.setColumnFilterable(true);
    colMovDate.setColumnName("movementDateWAR02");
    colMovDate.setColumnSortable(true);
    colMovDate.setPreferredWidth(120);
    colMovDate.setSortVersus(org.openswing.swing.util.java.Consts.DESC_SORTED);
    colItemCode.setColumnFilterable(true);
    colItemCode.setColumnName("itemCodeItm01WAR02");
    colItemCode.setColumnSortable(true);
    colItemCode.setPreferredWidth(70);
    colItemDescr.setColumnName("itemDescriptionSYS10");
    colItemDescr.setPreferredWidth(150);
    colDeltaQty.setColumnName("deltaQtyWAR02");
    colDeltaQty.setPreferredWidth(50);
    colPosition.setColumnFilterable(true);
    colPosition.setColumnName("locationDescriptionSYS10");
    colPosition.setColumnSortable(true);
    colPosition.setPreferredWidth(150);
    colUsername.setColumnName("usernameWAR02");
    colUsername.setPreferredWidth(80);
    colNote.setColumnFilterable(true);
    colNote.setColumnName("noteWAR02");
    colNote.setColumnSortable(true);
    colNote.setPreferredWidth(300);
    colMotiveCode.setColumnFilterable(true);
    colMotiveCode.setColumnName("warehouseMotiveWar04WAR02");
    colMotiveCode.setColumnSortable(true);
    colMotiveCode.setHeaderColumnName("warehouseMotiveWar04WAR02");
    colMotiveCode.setPreferredWidth(60);
    colMotiveDescr.setColumnName("motiveDescriptionSYS10");
    colMotiveDescr.setPreferredWidth(200);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(exportButton, null);
    buttonsPanel.add(navigatorBar, null);
    this.getContentPane().add(grid, BorderLayout.CENTER);
    grid.getColumnContainer().add(colMovDate, null);
    grid.getColumnContainer().add(colWarehouseCode, null);
    grid.getColumnContainer().add(colWarehouseDescr, null);
    grid.getColumnContainer().add(colItemCode, null);
    grid.getColumnContainer().add(colItemDescr, null);
    grid.getColumnContainer().add(colDeltaQty, null);
    grid.getColumnContainer().add(colQtySign, null);
    grid.getColumnContainer().add(colItemType, null);
    grid.getColumnContainer().add(colPosition, null);
    grid.getColumnContainer().add(colUsername, null);
    grid.getColumnContainer().add(colMotiveCode, null);
    grid.getColumnContainer().add(colMotiveDescr, null);
    grid.getColumnContainer().add(colNote, null);
  }

}
