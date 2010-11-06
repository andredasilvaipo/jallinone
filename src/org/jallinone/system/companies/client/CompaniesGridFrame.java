package org.jallinone.system.companies.client;

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


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the companies grid frame.</p>
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
public class CompaniesGridFrame extends InternalFrame {
  JPanel buttonsPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  InsertButton insertButton = new InsertButton();
  ReloadButton reloadButton = new ReloadButton();
  DeleteButton deleteButton = new DeleteButton();
  NavigatorBar navigatorBar = new NavigatorBar();
  GridControl grid = new GridControl();

  /** grid data locator */
  private ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();
  TextColumn colCompanyCode = new TextColumn();
  TextColumn colCorpName = new TextColumn();


  public CompaniesGridFrame(GridController controller) {
    grid.setController(controller);
    grid.setGridDataLocator(gridDataLocator);
    gridDataLocator.setServerMethodName("loadCompanies");
    try {
      jbInit();
      setSize(410,300);
      setMinimumSize(new Dimension(410,300));
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  public final void reloadData() {
    grid.reloadData();
  }


  private void jbInit() throws Exception {
    grid.setValueObjectClassName("org.jallinone.system.companies.java.CompanyVO");
    this.setTitle(ClientSettings.getInstance().getResources().getResource("companies list"));
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    grid.setDeleteButton(deleteButton);
    grid.setFunctionId("SYS01");
    grid.setInsertButton(insertButton);
    grid.setNavBar(navigatorBar);
    grid.setReloadButton(reloadButton);
    colCompanyCode.setColumnName("companyCodeSYS01");
    colCorpName.setColumnName("name_1REG04");
    colCorpName.setPreferredWidth(280);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(deleteButton, null);
    buttonsPanel.add(navigatorBar, null);
    this.getContentPane().add(grid, BorderLayout.CENTER);
    grid.getColumnContainer().add(colCompanyCode, null);
    grid.getColumnContainer().add(colCorpName, null);
  }

}