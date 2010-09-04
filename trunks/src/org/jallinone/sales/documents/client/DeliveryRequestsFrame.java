package org.jallinone.sales.documents.client;

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
import org.jallinone.sales.documents.java.GridSaleDocVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the delivery request grid frame.</p>
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
public class DeliveryRequestsFrame extends InternalFrame implements CurrencyColumnSettings {

  JPanel buttonsPanel = new JPanel();
  ReloadButton reloadButton1 = new ReloadButton();
  ExportButton exportButton1 = new ExportButton();
  NavigatorBar navigatorBar1 = new NavigatorBar();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  GridControl grid = new GridControl();
  TextColumn colCompany = new TextColumn();

  /** grid data locator */
  private ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();
  IntegerColumn colDocYear = new IntegerColumn();
  IntegerColumn colDocNr = new IntegerColumn();
  DateColumn colDocDate = new DateColumn();
  ComboColumn colDocState = new ComboColumn();
  DateColumn colDelivDate = new DateColumn();
  TextColumn colCust = new TextColumn();
  TextColumn colName1 = new TextColumn();
  TextColumn colName2 = new TextColumn();


  public DeliveryRequestsFrame(DeliveryRequestsController itemsController) {
    try {
      jbInit();
      setSize(750,550);
      setMinimumSize(new Dimension(750,500));

      grid.setController(itemsController);
      grid.setGridDataLocator(gridDataLocator);
      grid.getOtherGridParams().put(ApplicationConsts.DOC_TYPE,ApplicationConsts.DELIVERY_REQUEST_DOC_TYPE);
      gridDataLocator.setServerMethodName("loadSaleDocs");

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    this.setTitle(ClientSettings.getInstance().getResources().getResource("delivery requests"));
    grid.setValueObjectClassName("org.jallinone.sales.documents.java.GridSaleDocVO");
    reloadButton1.setText("reloadButton1");
    exportButton1.setText("exportButton1");
    buttonsPanel.setLayout(gridBagLayout1);
    grid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    colCompany.setColumnFilterable(true);
    colCompany.setColumnName("companyCodeSys01DOC01");
    colCompany.setColumnSortable(true);
    colCompany.setPreferredWidth(100);
    colCompany.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colCompany.setSortingOrder(0);
    grid.setExportButton(exportButton1);
    grid.setFunctionId("DELIV_REQ_LIST");
    grid.setLockedColumns(3);
    grid.setMaxSortedColumns(4);
    grid.setNavBar(navigatorBar1);
    grid.setReloadButton(reloadButton1);
    colDocYear.setColumnFilterable(true);
    colDocYear.setColumnName("docYearDOC01");
    colDocYear.setColumnSortable(true);
    colDocYear.setPreferredWidth(50);
    colDocYear.setSortingOrder(1);
    colDocYear.setSortVersus(org.openswing.swing.util.java.Consts.DESC_SORTED);
    colDocNr.setColumnFilterable(true);
    colDocNr.setColumnName("docSequenceDOC01");
    colDocNr.setColumnSortable(true);
    colDocNr.setPreferredWidth(80);
    colDocNr.setSortVersus(org.openswing.swing.util.java.Consts.NO_SORTED);
    colDocNr.setSortingOrder(0);
    colDocDate.setColumnFilterable(true);
    colDocDate.setColumnName("docDateDOC01");
    colDocDate.setColumnSortable(true);
    colDocState.setDomainId("DOC01_STATES");
    colDocState.setColumnFilterable(true);
    colDocState.setColumnName("docStateDOC01");
    colDocState.setColumnSortable(true);
    colDocState.setPreferredWidth(80);
    colDelivDate.setColumnName("deliveryDateDOC01");
    colDelivDate.setStrictUsage(false);
    colDelivDate.setSortVersus(org.openswing.swing.util.java.Consts.DESC_SORTED);
    colDelivDate.setSortingOrder(2);
    colCust.setColumnName("customerCodeSAL07");
    colCust.setColumnRequired(true);
    colName1.setColumnName("name_1REG04");
    colName1.setPreferredWidth(200);
    colName2.setColumnName("name_2REG04");
    colName2.setPreferredWidth(170);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    this.getContentPane().add(grid, BorderLayout.CENTER);
    buttonsPanel.add(reloadButton1,      new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    buttonsPanel.add(exportButton1,      new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    buttonsPanel.add(navigatorBar1,       new GridBagConstraints(7, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    grid.getColumnContainer().add(colCompany, null);
    grid.getColumnContainer().add(colDocYear, null);
    grid.getColumnContainer().add(colDocNr, null);
    grid.getColumnContainer().add(colDelivDate, null);
    grid.getColumnContainer().add(colDocDate, null);
    grid.getColumnContainer().add(colDocState, null);
    grid.getColumnContainer().add(colCust, null);
    grid.getColumnContainer().add(colName1, null);
    grid.getColumnContainer().add(colName2, null);
  }


  public GridControl getGrid() {
    return grid;
  }


  public ServerGridDataLocator getGridDataLocator() {
    return gridDataLocator;
  }


  public double getMaxValue(int row) {
    return Double.MAX_VALUE;
  }

  public double getMinValue(int row) {
    return 0.0;
  }

  public boolean isGrouping(int row) {
    return true;
  }

  public int getDecimals(int row) {
    try {
      GridSaleDocVO vo = (GridSaleDocVO) grid.getVOListTableModel().getObjectForRow(row);
      return vo.getDecimalsREG03().intValue();
    }
    catch (Exception ex) {
      return 0;
    }
  }

  public String getCurrencySymbol(int row) {
    try {
      GridSaleDocVO vo = (GridSaleDocVO) grid.getVOListTableModel().getObjectForRow(row);
      return vo.getCurrencySymbolREG03();
    }
    catch (Exception ex) {
      return "E";
    }
  }



}
