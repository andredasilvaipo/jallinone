package org.jallinone.employees.client;

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


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the employees grid frame.</p>
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
public class EmployeesGridFrame extends InternalFrame {

  JPanel buttonsPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  InsertButton insertButton = new InsertButton();
  ReloadButton reloadButton = new ReloadButton();
  DeleteButton deleteButton = new DeleteButton();
  NavigatorBar navigatorBar = new NavigatorBar();
  GridControl grid = new GridControl();


  /** grid data locator */
  private ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();
  TextColumn colEmployee = new TextColumn();
  TextColumn colTaskDescr = new TextColumn();
  ExportButton exportButton = new ExportButton();
  TextColumn colCompany = new TextColumn();
  TextColumn colFirstName = new TextColumn();
  TextColumn colLastName = new TextColumn();
  TextColumn colPhoneNr = new TextColumn();
  TextColumn colOffine = new TextColumn();


  public EmployeesGridFrame(GridController controller) {
    grid.setController(controller);
    grid.setGridDataLocator(gridDataLocator);
    gridDataLocator.setServerMethodName("loadEmployees");
    try {
      jbInit();
      setSize(750,300);
      setMinimumSize(new Dimension(750,300));

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
    grid.setValueObjectClassName("org.jallinone.employees.java.GridEmployeeVO");
    this.setTitle(ClientSettings.getInstance().getResources().getResource("employees"));
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    grid.setDeleteButton(deleteButton);
    grid.setExportButton(exportButton);
    grid.setFunctionId("SCH01");
    grid.setMaxSortedColumns(3);
    grid.setInsertButton(insertButton);
    grid.setNavBar(navigatorBar);
    grid.setReloadButton(reloadButton);
    colEmployee.setMaxCharacters(20);
    colEmployee.setTrimText(true);
    colEmployee.setUpperCase(true);
    colEmployee.setColumnDuplicable(false);
    colEmployee.setColumnFilterable(true);
    colEmployee.setColumnName("employeeCodeSCH01");
    colEmployee.setColumnSortable(true);
    colEmployee.setEditableOnInsert(true);
    colEmployee.setPreferredWidth(100);
    colEmployee.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colEmployee.setSortingOrder(1);
    colTaskDescr.setColumnFilterable(true);
    colTaskDescr.setColumnName("descriptionSYS10");
    colTaskDescr.setColumnSortable(true);
    colTaskDescr.setEditableOnEdit(true);
    colTaskDescr.setEditableOnInsert(true);
    colTaskDescr.setHeaderColumnName("taskDescription");
    colTaskDescr.setPreferredWidth(190);
    colCompany.setColumnDuplicable(true);
    colCompany.setColumnFilterable(true);
    colCompany.setColumnName("companyCodeSys01SCH01");
    colCompany.setColumnSortable(true);
    colCompany.setEditableOnInsert(true);
    colCompany.setHeaderColumnName("companyCodeSYS01");
    colCompany.setPreferredWidth(100);
    colCompany.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colCompany.setSortingOrder(0);
    colFirstName.setColumnDuplicable(false);
    colFirstName.setColumnFilterable(true);
    colFirstName.setColumnName("name_1REG04");
    colFirstName.setColumnSortable(true);
    colFirstName.setHeaderColumnName("firstname");
    colFirstName.setPreferredWidth(120);
    colLastName.setColumnFilterable(true);
    colLastName.setColumnName("name_2REG04");
    colLastName.setColumnSortable(true);
    colLastName.setHeaderColumnName("lastname");
    colLastName.setPreferredWidth(120);
    colPhoneNr.setColumnFilterable(true);
    colPhoneNr.setColumnName("phoneNumberSCH01");
    colPhoneNr.setColumnSortable(true);
    colOffine.setColumnFilterable(true);
    colOffine.setColumnName("officeSCH01");
    colOffine.setColumnSortable(true);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(deleteButton, null);
    buttonsPanel.add(exportButton, null);
    buttonsPanel.add(navigatorBar, null);
    this.getContentPane().add(grid, BorderLayout.CENTER);
    grid.getColumnContainer().add(colCompany, null);
    grid.getColumnContainer().add(colEmployee, null);
    grid.getColumnContainer().add(colFirstName, null);
    grid.getColumnContainer().add(colLastName, null);
    grid.getColumnContainer().add(colPhoneNr, null);
    grid.getColumnContainer().add(colOffine, null);
    grid.getColumnContainer().add(colTaskDescr, null);
  }

}
