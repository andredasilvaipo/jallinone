package org.jallinone.sales.documents.client;

import java.awt.*;
import javax.swing.*;
import org.openswing.swing.client.*;
import javax.swing.border.*;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.form.client.Form;
import java.awt.event.*;
import org.openswing.swing.table.java.ServerGridDataLocator;
import org.openswing.swing.util.java.Consts;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.sales.documents.java.*;
import java.math.BigDecimal;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.openswing.swing.lookup.client.LookupListener;
import org.openswing.swing.message.receive.java.*;
import java.util.Collection;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.tree.client.TreeServerDataLocator;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.domains.java.Domain;
import org.jallinone.items.java.ItemTypeVO;
import java.util.ArrayList;
import java.beans.Beans;
import org.jallinone.warehouse.availability.client.ItemAvailabilityPanel;
import org.jallinone.warehouse.availability.client.BookedItemsPanel;
import org.jallinone.warehouse.availability.client.OrderedItemsPanel;
import org.jallinone.sales.documents.itemdiscounts.client.*;
import java.util.HashSet;
import org.jallinone.variants.client.ProductVariantsPanel;
import org.jallinone.variants.client.ProductVariantsController;
import org.jallinone.warehouse.java.StoredSerialNumberVO;
import org.openswing.swing.message.send.java.LookupValidationParams;
import java.util.HashMap;
import org.jallinone.items.java.VariantBarcodeVO;
import org.jallinone.variants.client.ProductVariantsPanelController;
import org.openswing.swing.customvo.java.CustomValueObject;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Panel that contains the delivery request rows grid + row detail.
 * CONSTRAINT: items should be stored ONLY in the root location of the specified warehouse</p>
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
public class DeliveryRequestRowsGridPanel extends JPanel implements CurrencyColumnSettings,SaleDocument {

  BorderLayout borderLayout1 = new BorderLayout();
  JPanel buttonsPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  ReloadButton reloadButton1 = new ReloadButton();
  NavigatorBar navigatorBar1 = new NavigatorBar();
  GridControl grid = new GridControl();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  TitledBorder titledBorder1;
  TitledBorder titledBorder2;
  ExportButton exportButton1 = new ExportButton();
  DecimalColumn colRowNum = new DecimalColumn();
  TextColumn colItemCode = new TextColumn();
  TextColumn colItemDescr = new TextColumn();
  DecimalColumn colQty = new DecimalColumn();
  DecimalColumn colOutQty = new DecimalColumn();


  /** header v.o. */
  private DetailSaleDocVO parentVO = null;

  /** grid data locator */
  private ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();

  /** header panel */
  private Form headerPanel = null;

  /** detail frame */
  private DeliveryRequestFrame frame = null;

  BorderLayout borderLayout2 = new BorderLayout();



  public DeliveryRequestRowsGridPanel(DeliveryRequestFrame frame,Form headerPanel) {
    this.frame = frame;
    this.headerPanel = headerPanel;
    try {
      jbInit();

      if (Beans.isDesignTime())
        return;

      grid.setController(new DeliveryRequestRowsController(this));
      grid.setGridDataLocator(gridDataLocator);
      gridDataLocator.setServerMethodName("loadSaleDocRows");

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }






  private void jbInit() throws Exception {

    titledBorder1 = new TitledBorder("");
    this.setLayout(borderLayout1);
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    grid.setAutoLoadData(false);
    grid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    grid.setExportButton(exportButton1);
    grid.setFunctionId("DELIV_REQ_LIST");
    grid.setMaxSortedColumns(3);
    grid.setNavBar(navigatorBar1);
    grid.setReloadButton(reloadButton1);
    grid.setValueObjectClassName("org.jallinone.sales.documents.java.GridSaleDocRowVO");
    titledBorder1.setTitle(ClientSettings.getInstance().getResources().getResource("line detail"));
    titledBorder1.setTitleColor(Color.blue);
    colRowNum.setColumnFilterable(false);
    colRowNum.setColumnName("rowNumberDOC02");
    colRowNum.setColumnVisible(false);
    colRowNum.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colRowNum.setColumnSelectable(false);

    colItemCode.setColumnFilterable(false);
    colItemCode.setColumnName("itemCodeItm01DOC02");
    colItemCode.setColumnSortable(false);
    colItemCode.setPreferredWidth(80);
    colItemDescr.setColumnName("descriptionSYS10");
    colItemDescr.setColumnSortable(false);
    colItemDescr.setHeaderColumnName("itemDescriptionSYS10");
    colItemDescr.setPreferredWidth(300);
    colQty.setDecimals(5);
    colQty.setGrouping(false);
    colQty.setColumnName("qtyDOC02");
    colQty.setColumnSortable(false);
    colQty.setPreferredWidth(60);

    colOutQty.setDecimals(5);
    colOutQty.setGrouping(false);
    colOutQty.setColumnName("outQtyDOC02");
    colOutQty.setColumnSortable(false);
    colOutQty.setPreferredWidth(60);


    this.add(buttonsPanel, BorderLayout.NORTH);
    this.add(grid, BorderLayout.CENTER);
    buttonsPanel.add(reloadButton1, null);
    buttonsPanel.add(exportButton1, null);
    buttonsPanel.add(navigatorBar1, null);
    grid.getColumnContainer().add(colRowNum, null);
    grid.getColumnContainer().add(colItemCode, null);
    grid.getColumnContainer().add(colItemDescr, null);
    grid.getColumnContainer().add(colQty, null);
    grid.getColumnContainer().add(colOutQty, null);


  }



  public void setParentVO(DetailSaleDocVO parentVO) {
    this.parentVO = parentVO;
  }


  public GridControl getGrid() {
    return grid;
  }


  public DetailSaleDocVO getParentVO() {
    return parentVO;
  }



  public Form getHeaderPanel() {
    return headerPanel;
  }


  public GridControl getDelivReqs() {
    return frame.getDelivReqs();
  }


  public DeliveryRequestFrame getFrame() {
    return frame;
  }


  public double getMaxValue(int int0) {
    return Double.MAX_VALUE;
  }

  public double getMinValue(int int0) {
    return 0.0;
  }

  public boolean isGrouping(int int0) {
    return true;
  }

  public int getDecimals(int int0) {
    if (parentVO!=null)
      return parentVO.getDecimalsREG03().intValue();
    else
      return 0;
  }

  public String getCurrencySymbol(int int0) {
    if (parentVO!=null)
      return parentVO.getCurrencySymbolREG03();
    else
    return "E";
  }


  public Form getHeaderFormPanel() {
    return frame.getHeaderFormPanel();
  }



  /**
   * Method NOT supported.
   */
  public boolean isButtonDisabled(GenericButton button) {
    return false;
  }

}
