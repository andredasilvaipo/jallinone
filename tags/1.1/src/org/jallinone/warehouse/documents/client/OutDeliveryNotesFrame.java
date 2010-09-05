package org.jallinone.warehouse.documents.client;

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
import org.jallinone.commons.client.CustomizedColumns;
import org.jallinone.hierarchies.client.*;
import org.jallinone.commons.client.CompaniesComboColumn;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.message.receive.java.Response;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.domains.java.*;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import org.jallinone.commons.java.ApplicationConsts;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the in delivery notes grid frame.</p>
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
public class OutDeliveryNotesFrame extends InternalFrame {

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
  TextColumn colWarehousecode = new TextColumn();
  TextColumn colWarehouseDescr = new TextColumn();
  DateColumn colDocDate = new DateColumn();
  ComboColumn colDocState = new ComboColumn();
  TextColumn colDocRef = new TextColumn();
  TextColumn colFirstName = new TextColumn();
  TextColumn colLastName = new TextColumn();
  TextColumn colCustomerCode = new TextColumn();


  public OutDeliveryNotesFrame(OutDeliveryNotesController itemsController) {
    try {
      jbInit();
      setSize(750,550);
      setMinimumSize(new Dimension(750,500));

      grid.setController(itemsController);
      grid.setGridDataLocator(gridDataLocator);
      gridDataLocator.setServerMethodName("loadOutDeliveryNotes");

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    this.setTitle(ClientSettings.getInstance().getResources().getResource("out delivery notes"));
    grid.setValueObjectClassName("org.jallinone.warehouse.documents.java.GridDeliveryNoteVO");
    insertButton1.setText("insertButton1");
    reloadButton1.setText("reloadButton1");
    deleteButton1.setText("deleteButton1");
    exportButton1.setText("exportButton1");
    buttonsPanel.setLayout(gridBagLayout1);
    grid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    colCompany.setColumnFilterable(true);
    colCompany.setColumnName("companyCodeSys01DOC08");
    colCompany.setColumnSortable(true);
    colCompany.setPreferredWidth(100);
    colCompany.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colCompany.setSortingOrder(0);
    grid.setDeleteButton(deleteButton1);
    grid.setExportButton(exportButton1);
    grid.setFunctionId("DOC08_OUT");
    grid.setMaxSortedColumns(4);
    grid.setInsertButton(insertButton1);
    grid.setNavBar(navigatorBar1);
    grid.setReloadButton(reloadButton1);
    colDocYear.setColumnFilterable(true);
    colDocYear.setColumnName("docYearDOC08");
    colDocYear.setColumnSortable(true);
    colDocYear.setPreferredWidth(50);
    colDocYear.setSortingOrder(1);
    colDocYear.setSortVersus(org.openswing.swing.util.java.Consts.DESC_SORTED);
    colDocNr.setColumnFilterable(true);
    colDocNr.setColumnName("docSequenceDOC08");
    colDocNr.setColumnSortable(true);
    colDocNr.setPreferredWidth(80);
    colDocNr.setSortingOrder(2);
    colDocNr.setSortVersus(org.openswing.swing.util.java.Consts.DESC_SORTED);
    colWarehousecode.setColumnFilterable(true);
    colWarehousecode.setColumnName("warehouseCodeWar01DOC08");
    colWarehousecode.setColumnSortable(true);
    colWarehousecode.setPreferredWidth(90);
    colWarehouseDescr.setColumnName("warehouseDescriptionDOC08");
    colWarehouseDescr.setPreferredWidth(200);
    colDocDate.setColumnFilterable(true);
    colDocDate.setColumnName("docDateDOC08");
    colDocDate.setColumnSortable(true);
    colDocDate.setPreferredWidth(80);
    colDocState.setDomainId("DOC08_STATES");
    colDocState.setColumnFilterable(true);
    colDocState.setColumnName("docStateDOC08");
    colDocState.setColumnSortable(true);
    colDocState.setPreferredWidth(80);
    colDocRef.setColumnFilterable(true);
    colDocRef.setColumnName("docRefDOC08");
    colDocRef.setColumnSortable(true);
    colDocRef.setPreferredWidth(150);
    colCustomerCode.setColumnFilterable(true);
    colCustomerCode.setColumnName("supplierCustomerCodeDOC08");
    colCustomerCode.setColumnSortable(true);
    colCustomerCode.setHeaderColumnName("customerCodeSAL07");
    colCustomerCode.setPreferredWidth(90);
    colFirstName.setColumnName("name_1REG04");
    colFirstName.setColumnSortable(true);
    colFirstName.setPreferredWidth(180);
    colLastName.setColumnName("name_2REG04");
    colLastName.setColumnSortable(true);
    colLastName.setPreferredWidth(150);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
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
    this.getContentPane().add(grid, BorderLayout.CENTER);
    grid.getColumnContainer().add(colCompany, null);
    grid.getColumnContainer().add(colDocYear, null);
    grid.getColumnContainer().add(colDocNr, null);
    grid.getColumnContainer().add(colWarehousecode, null);
    grid.getColumnContainer().add(colWarehouseDescr, null);
    grid.getColumnContainer().add(colDocDate, null);
    grid.getColumnContainer().add(colDocState, null);
    grid.getColumnContainer().add(colCustomerCode, null);
    grid.getColumnContainer().add(colFirstName, null);
    grid.getColumnContainer().add(colLastName, null);
    grid.getColumnContainer().add(colDocRef, null);
  }


  public GridControl getGrid() {
    return grid;
  }


  public ServerGridDataLocator getGridDataLocator() {
    return gridDataLocator;
  }


}