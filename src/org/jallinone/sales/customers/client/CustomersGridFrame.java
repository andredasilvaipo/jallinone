package org.jallinone.sales.customers.client;

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
import org.jallinone.subjects.java.*;
import org.jallinone.subjects.client.*;
import org.jallinone.commons.java.ApplicationConsts;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Customer grid frame: it shows private customers + organization customers.</p>
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
public class CustomersGridFrame extends InternalFrame implements SubjectFrame {

  GridControl grid = new GridControl();
  JPanel buttonsPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  InsertButton insertButton = new InsertButton();
  ReloadButton reloadButton = new ReloadButton();
  DeleteButton deleteButton = new DeleteButton();
  NavigatorBar navigatorBar = new NavigatorBar();
  ExportButton exportButton = new ExportButton();
  TextColumn colCompanyCode = new TextColumn();
  TextColumn colName2 = new TextColumn();
  TextColumn colCustomerCode = new TextColumn();
  TextColumn colTaxCode = new TextColumn();
  TextColumn colCity = new TextColumn();
  TextColumn colProv = new TextColumn();
  TextColumn colCountry = new TextColumn();
  TextColumn colName1 = new TextColumn();

  /** grid data locator */
  private ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();
  ComboColumn colCustomerType = new ComboColumn();


  public CustomersGridFrame(CustomersController controller) {
    grid.setController(controller);
    grid.setGridDataLocator(gridDataLocator);
    gridDataLocator.setServerMethodName("loadCustomers");
    try {
      jbInit();
      setSize(750,550);
      setMinimumSize(new Dimension(750,550));

      grid.enableDrag(ApplicationConsts.ID_CUSTOMER_GRID.toString());

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
    grid.setValueObjectClassName("org.jallinone.sales.customers.java.GridCustomerVO");
    this.setTitle(ClientSettings.getInstance().getResources().getResource("customers"));
    grid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    insertButton.setText("insertButton1");
    reloadButton.setText("reloadButton1");
    deleteButton.setText("deleteButton1");
    grid.setDeleteButton(deleteButton);
    grid.setExportButton(exportButton);
    grid.setFunctionId("SAL07");
    grid.setMaxSortedColumns(3);
    grid.setInsertButton(insertButton);
    grid.setNavBar(navigatorBar);
    grid.setReloadButton(reloadButton);
    colName2.setColumnFilterable(true);
    colName2.setColumnName("name_2REG04");
    colName2.setColumnSortable(true);
    colName2.setPreferredWidth(180);
    colName2.setSortingOrder(0);
    colCompanyCode.setColumnFilterable(true);
    colCompanyCode.setColumnName("companyCodeSys01REG04");
    colCompanyCode.setColumnSortable(true);
    colCompanyCode.setSortVersus(Consts.ASC_SORTED);
    colCompanyCode.setSortingOrder(1);
    colCustomerCode.setColumnFilterable(true);
    colCustomerCode.setColumnName("customerCodeSAL07");
    colCustomerCode.setColumnSortable(true);
    colCustomerCode.setPreferredWidth(110);
    colTaxCode.setColumnName("taxCodeREG04");
    colTaxCode.setHeaderColumnName("taxCodeREG04");
    colTaxCode.setPreferredWidth(200);
    colCity.setColumnFilterable(true);
    colCity.setColumnName("cityREG04");
    colCity.setColumnSortable(true);
    colCity.setHeaderColumnName("city");
    colProv.setColumnFilterable(true);
    colProv.setColumnName("provinceREG04");
    colProv.setColumnSortable(true);
    colProv.setHeaderColumnName("prov");
    colProv.setPreferredWidth(40);
    colCountry.setColumnFilterable(true);
    colCountry.setColumnName("countryREG04");
    colCountry.setColumnSortable(true);
    colCountry.setHeaderColumnName("country");
    colCountry.setPreferredWidth(80);
    colName1.setColumnFilterable(true);
    colName1.setColumnName("name_1REG04");
    colName1.setColumnSortable(true);
    colName1.setHeaderColumnName("");
    colName1.setPreferredWidth(180);
    colName1.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colName1.setSortingOrder(2);
    colCustomerType.setDomainId("CUSTOMER_TYPE");
    colCustomerType.setColumnFilterable(true);
    colCustomerType.setColumnName("subjectTypeREG04");
    colCustomerType.setColumnSortable(true);
    colCustomerType.setHeaderColumnName("customer type");
    colCustomerType.setPreferredWidth(100);
    this.getContentPane().add(grid, BorderLayout.CENTER);
    grid.getColumnContainer().add(colCompanyCode, null);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(deleteButton, null);
    buttonsPanel.add(navigatorBar, null);
    buttonsPanel.add(exportButton, null);
    grid.getColumnContainer().add(colName1, null);
    grid.getColumnContainer().add(colName2, null);
    grid.getColumnContainer().add(colCustomerType, null);
    grid.getColumnContainer().add(colCustomerCode, null);
    grid.getColumnContainer().add(colCity, null);
    grid.getColumnContainer().add(colProv, null);
    grid.getColumnContainer().add(colCountry, null);
    grid.getColumnContainer().add(colTaxCode, null);
  }


  public GridControl getGrid() {
    return grid;
  }

}