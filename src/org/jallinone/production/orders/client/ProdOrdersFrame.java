package org.jallinone.production.orders.client;

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
import org.openswing.swing.form.client.Form;
import org.jallinone.commons.client.CustomizedControls;
import org.openswing.swing.util.java.Consts;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.production.orders.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the production orders grid frame.</p>
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
public class ProdOrdersFrame extends InternalFrame {

  JPanel buttonsPanel = new JPanel();
  InsertButton insertButton1 = new InsertButton();
  ReloadButton reloadButton1 = new ReloadButton();
  DeleteButton deleteButton1 = new DeleteButton();
  ExportButton exportButton1 = new ExportButton();
  NavigatorBar navigatorBar1 = new NavigatorBar();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  GridControl grid = new GridControl();
  TextColumn colCompany = new TextColumn();

  /** grid data locator */
  private ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();

  IntegerColumn colDocYear = new IntegerColumn();
  IntegerColumn colDocNr = new IntegerColumn();
  TextColumn colWarcode = new TextColumn();
  TextColumn colWar2code = new TextColumn();
  TextColumn colWarDescr = new TextColumn();
  TextColumn colWar2Descr = new TextColumn();
  DateColumn colDocDate = new DateColumn();
  ComboColumn colDocState = new ComboColumn();


  public ProdOrdersFrame(ProdOrdersController itemsController) {
    try {
      jbInit();
      setSize(750,550);
      setMinimumSize(new Dimension(750,500));

      grid.setController(itemsController);
      grid.setGridDataLocator(gridDataLocator);
      gridDataLocator.setServerMethodName("loadProdOrders");

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    this.setTitle(ClientSettings.getInstance().getResources().getResource("production orders"));
    grid.setValueObjectClassName("org.jallinone.production.orders.java.ProdOrderVO");
    insertButton1.setText("insertButton1");
    reloadButton1.setText("reloadButton1");
    deleteButton1.setText("deleteButton1");
    exportButton1.setText("exportButton1");
    buttonsPanel.setLayout(gridBagLayout1);
    grid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    colCompany.setColumnFilterable(true);
    colCompany.setColumnName("companyCodeSys01DOC22");
    colCompany.setColumnSortable(true);
    colCompany.setPreferredWidth(100);
    colCompany.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colCompany.setSortingOrder(0);
    grid.setDeleteButton(deleteButton1);
    grid.setExportButton(exportButton1);
    grid.setFunctionId("DOC22");
    grid.setMaxSortedColumns(4);
    grid.setInsertButton(insertButton1);
    grid.setNavBar(navigatorBar1);
    grid.setReloadButton(reloadButton1);
    colDocYear.setColumnFilterable(true);
    colDocYear.setColumnName("docYearDOC22");
    colDocYear.setColumnSortable(true);
    colDocYear.setPreferredWidth(50);
    colDocYear.setSortingOrder(1);
    colDocYear.setSortVersus(org.openswing.swing.util.java.Consts.DESC_SORTED);
    colDocNr.setColumnFilterable(true);
    colDocNr.setColumnName("docSequenceDOC22");
    colDocNr.setColumnSortable(true);
    colDocNr.setPreferredWidth(70);
    colDocNr.setSortingOrder(2);
    colDocNr.setSortVersus(org.openswing.swing.util.java.Consts.DESC_SORTED);

    colWarcode.setColumnFilterable(true);
    colWarcode.setColumnName("warehouseCodeWar01DOC22");
    colWarcode.setColumnSortable(true);
    colWarcode.setPreferredWidth(90);

    colWar2code.setColumnFilterable(true);
    colWar2code.setColumnName("warehouseCode2War01DOC22");
    colWar2code.setColumnSortable(true);
    colWar2code.setPreferredWidth(90);

    colWarDescr.setColumnName("descriptionWar01DOC22");
    colWarDescr.setPreferredWidth(200);

    colWar2Descr.setColumnName("description2War01DOC22");
    colWar2Descr.setPreferredWidth(200);

    colDocDate.setColumnFilterable(true);
    colDocDate.setColumnName("docDateDOC22");
    colDocDate.setSortVersus(org.openswing.swing.util.java.Consts.DESC_SORTED);
    colDocDate.setSortingOrder(3);
    colDocDate.setColumnSortable(true);
    colDocDate.setPreferredWidth(80);
    colDocState.setDomainId("DOC22_STATES");
    colDocState.setColumnFilterable(true);
    colDocState.setColumnName("docStateDOC22");
    colDocState.setColumnSortable(true);
    colDocState.setPreferredWidth(80);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    this.getContentPane().add(grid, BorderLayout.CENTER);
    buttonsPanel.add(insertButton1,     new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    buttonsPanel.add(reloadButton1,    new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    buttonsPanel.add(deleteButton1,    new GridBagConstraints(7, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    buttonsPanel.add(exportButton1,    new GridBagConstraints(8, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    buttonsPanel.add(navigatorBar1,     new GridBagConstraints(9, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    grid.getColumnContainer().add(colCompany, null);
    grid.getColumnContainer().add(colDocYear, null);
    grid.getColumnContainer().add(colDocNr, null);
    grid.getColumnContainer().add(colDocDate, null);
    grid.getColumnContainer().add(colDocState, null);
    grid.getColumnContainer().add(colWarcode, null);
    grid.getColumnContainer().add(colWarDescr, null);
    grid.getColumnContainer().add(colWar2code, null);
    grid.getColumnContainer().add(colWar2Descr, null);
  }


  public GridControl getGrid() {
    return grid;
  }


  public ServerGridDataLocator getGridDataLocator() {
    return gridDataLocator;
  }




}
