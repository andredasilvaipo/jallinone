package org.jallinone.system.customizations.client;

import org.openswing.swing.mdi.client.InternalFrame;
import org.openswing.swing.table.client.GridController;
import javax.swing.*;
import java.awt.*;
import org.openswing.swing.client.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.table.java.ServerGridDataLocator;
import org.openswing.swing.util.client.ClientSettings;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Grid Frame used to list all custom functions.</p>
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
public class CustomFunctionsGridFrame extends InternalFrame {
  JPanel buttonsPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  GridControl grid = new GridControl();
  InsertButton insertButton1 = new InsertButton();
  DeleteButton deleteButton1 = new DeleteButton();
  ReloadButton reloadButton1 = new ReloadButton();
  NavigatorBar navigatorBar1 = new NavigatorBar();
  ExportButton exportButton1 = new ExportButton();
  TextColumn colFunctionCode = new TextColumn();
  TextColumn colDescription = new TextColumn();
  ServerGridDataLocator dataLocator = new ServerGridDataLocator();


  public CustomFunctionsGridFrame(GridController controller) {
    try {
      jbInit();
      setSize(380,500);
      grid.setController(controller);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    this.setTitle(ClientSettings.getInstance().getResources().getResource("custom functions"));
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    grid.setDeleteButton(deleteButton1);
    grid.setExportButton(exportButton1);
    grid.setFunctionId("SYS16");
    grid.setMaxSortedColumns(2);
    grid.setInsertButton(insertButton1);
    grid.setNavBar(navigatorBar1);
    grid.setReloadButton(reloadButton1);
    grid.setValueObjectClassName("org.jallinone.system.customizations.java.CustomFunctionVO");
    grid.setGridDataLocator(dataLocator);
    dataLocator.setServerMethodName("loadCustomFunctions");
    colDescription.setColumnFilterable(true);
    colDescription.setColumnSortable(true);
    colDescription.setColumnName("descriptionSYS10");
    colDescription.setPreferredWidth(250);
    colFunctionCode.setColumnFilterable(true);
    colFunctionCode.setColumnSortable(true);
    colFunctionCode.setColumnName("functionCodeSys06SYS16");
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    this.getContentPane().add(grid, BorderLayout.CENTER);
    buttonsPanel.add(insertButton1, null);
    buttonsPanel.add(reloadButton1, null);
    buttonsPanel.add(deleteButton1, null);
    buttonsPanel.add(exportButton1, null);
    buttonsPanel.add(navigatorBar1, null);
    grid.getColumnContainer().add(colFunctionCode, null);
    grid.getColumnContainer().add(colDescription, null);
  }
  public GridControl getGrid() {
    return grid;
  }

}
