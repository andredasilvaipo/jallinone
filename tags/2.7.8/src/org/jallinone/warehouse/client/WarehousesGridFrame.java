package org.jallinone.warehouse.client;

import org.openswing.swing.mdi.client.InternalFrame;
import org.openswing.swing.tree.client.*;
import java.awt.*;
import org.openswing.swing.mdi.client.MDIFrame;
import javax.swing.*;
import org.openswing.swing.client.*;
import org.openswing.swing.table.java.ServerGridDataLocator;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.util.java.Consts;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Warehouses grid frame.</p>
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
public class WarehousesGridFrame extends InternalFrame {
  GridControl grid = new GridControl();
  JPanel buttonsPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  InsertButton insertButton = new InsertButton();
  ReloadButton reloadButton = new ReloadButton();
  DeleteButton deleteButton = new DeleteButton();
  NavigatorBar navigatorBar = new NavigatorBar();
  ExportButton exportButton = new ExportButton();
  TextColumn colCompanyCode = new TextColumn();
  TextColumn colWarehouseCode = new TextColumn();
  TextColumn colDescr = new TextColumn();
  TextColumn colAddress = new TextColumn();
  TextColumn colCity = new TextColumn();
  TextColumn colProv = new TextColumn();
  TextColumn colCountry = new TextColumn();
  TextColumn colCompanyDescr = new TextColumn();

  /** grid data locator */
  private ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();


  public WarehousesGridFrame(WarehousesController controller) {
    grid.setController(controller);
    grid.setGridDataLocator(gridDataLocator);
    gridDataLocator.setServerMethodName("loadWarehouses");
    try {
      jbInit();
      setSize(750,400);
      setMinimumSize(new Dimension(750,400));
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * Reload grid data.
   */
  public final void reloadData() {
    grid.reloadData();
  }


  private void jbInit() throws Exception {
    grid.setValueObjectClassName("org.jallinone.warehouse.java.WarehouseVO");
    this.setTitle(ClientSettings.getInstance().getResources().getResource("warehouses"));
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    insertButton.setText("insertButton1");
    reloadButton.setText("reloadButton1");
    deleteButton.setText("deleteButton1");
    grid.setDeleteButton(deleteButton);
    grid.setExportButton(exportButton);
    grid.setFunctionId("WAR01");
    grid.setAllowColumnsProfile(false);
    grid.setAllowColumnsPermission(false);
    grid.setMaxSortedColumns(3);
    grid.setInsertButton(insertButton);
    grid.setNavBar(navigatorBar);
    grid.setReloadButton(reloadButton);
    colWarehouseCode.setColumnFilterable(true);
    colWarehouseCode.setColumnName("warehouseCodeWAR01");
    colWarehouseCode.setColumnSortable(true);
    colWarehouseCode.setPreferredWidth(110);
    colWarehouseCode.setSortVersus(Consts.ASC_SORTED);
    colWarehouseCode.setSortingOrder(2);
    colCompanyCode.setColumnFilterable(true);
    colCompanyCode.setColumnName("companyCodeSys01WAR01");
    colCompanyCode.setColumnSortable(true);
    colCompanyCode.setSortVersus(Consts.ASC_SORTED);
    colCompanyCode.setSortingOrder(1);
    colDescr.setColumnName("descriptionWAR01");
    colDescr.setColumnSortable(true);
    colDescr.setPreferredWidth(200);
    colAddress.setColumnName("addressWAR01");
    colAddress.setPreferredWidth(200);
    colCity.setColumnFilterable(true);
    colCity.setColumnName("cityWAR01");
    colCity.setColumnSortable(true);
    colProv.setColumnFilterable(true);
    colProv.setColumnName("provinceWAR01");
    colProv.setColumnSortable(true);
    colProv.setPreferredWidth(40);
    colCountry.setColumnFilterable(true);
    colCountry.setColumnName("countryWAR01");
    colCountry.setColumnSortable(true);
    colCountry.setPreferredWidth(80);
    colCompanyDescr.setColumnName("name_1REG04");
    colCompanyDescr.setPreferredWidth(160);
    this.getContentPane().add(grid, BorderLayout.CENTER);
    grid.getColumnContainer().add(colCompanyCode, null);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(deleteButton, null);
    buttonsPanel.add(navigatorBar, null);
    buttonsPanel.add(exportButton, null);
    grid.getColumnContainer().add(colCompanyDescr, null);
    grid.getColumnContainer().add(colWarehouseCode, null);
    grid.getColumnContainer().add(colDescr, null);
    grid.getColumnContainer().add(colAddress, null);
    grid.getColumnContainer().add(colCity, null);
    grid.getColumnContainer().add(colProv, null);
    grid.getColumnContainer().add(colCountry, null);
  }
  public GridControl getGrid() {
    return grid;
  }

}
