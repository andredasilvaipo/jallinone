package org.jallinone.warehouse.availability.client;

import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.form.client.FormController;
import org.openswing.swing.message.receive.java.*;
import org.jallinone.warehouse.java.WarehousePK;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.warehouse.java.WarehouseVO;
import org.jallinone.commons.client.CompanyFormController;
import org.openswing.swing.form.client.Form;
import org.jallinone.commons.client.ClientApplet;
import org.jallinone.commons.client.ApplicationClientFacade;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.util.client.ClientSettings;
import java.awt.*;
import javax.swing.*;
import org.openswing.swing.client.*;
import javax.swing.border.*;
import org.openswing.swing.table.columns.client.*;
import java.awt.event.*;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.openswing.swing.tree.client.TreeServerDataLocator;
import org.openswing.swing.domains.java.Domain;
import org.jallinone.items.java.ItemTypeVO;
import java.util.ArrayList;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.lookup.client.LookupListener;
import java.util.Collection;
import org.jallinone.warehouse.availability.java.OrderedItemQtyVO;
import org.openswing.swing.table.client.GridController;
import org.openswing.swing.table.java.ServerGridDataLocator;
import org.jallinone.items.java.ItemPK;
import org.jallinone.items.java.GridItemVO;
import org.openswing.swing.lookup.client.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Panel that contains an item filter and a grid listing items availability in the future
 * (items ordered in purchase orders) and delivery dates and purchase order year/numbers.
 * Quantities are filters by the the specified warehouse.</p>
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
public class OrderedItemsPanel extends JPanel {

  GridBagLayout borderLayout2 = new GridBagLayout();
  JPanel topPanel = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  GridControl grid = new GridControl();
  TitledBorder titledBorder1;
  TitledBorder titledBorder2;
  JPanel buttonsPanel = new JPanel();
  Form filterPanel = new Form();
  FlowLayout flowLayout1 = new FlowLayout();
  ReloadButton reloadButton = new ReloadButton();
  ExportButton exportButton = new ExportButton();
  NavigatorBar navigatorBar = new NavigatorBar();
  LabelControl labelItem = new LabelControl();
  ComboBoxControl controlItemType = new ComboBoxControl();
  CodLookupControl controlItemCode = new CodLookupControl();
  TextControl controlItemDescr = new TextControl();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  DecimalColumn colFutureAvailQty = new DecimalColumn();

  /** item code lookup data locator */
  LookupServerDataLocator itemDataLocator = new LookupServerDataLocator();

  /** item code lookup controller */
  LookupController itemController = new LookupController();

  /** grid data locator */
  ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();

  LookupServerDataLocator levelDataLocator = new LookupServerDataLocator();
  TreeServerDataLocator treeLevelDataLocator = new TreeServerDataLocator();
  DateColumn colDeliveryDate = new DateColumn();
  DecimalColumn colDocYear = new DecimalColumn();
  DecimalColumn colDocNr = new DecimalColumn();
  TextColumn colUM = new TextColumn();

  TextColumn colWarCode = new TextColumn();
  TextColumn colWarDescr = new TextColumn();

  /** define if warehouse info should be displayed */
  private boolean viewWarehouse;

  /** define if item lookup should be displayed */
  private boolean viewItemLookup;
  TextColumn colDesc = new TextColumn();


  public OrderedItemsPanel(boolean viewWarehouse,boolean viewItemLookup) {
    this.viewWarehouse = viewWarehouse;
    this.viewItemLookup = viewItemLookup;
    try {
      jbInit();

      grid.setController(new GridController());
      grid.setGridDataLocator(gridDataLocator);
      gridDataLocator.setServerMethodName("loadOrderedItems");

      filterPanel.setVOClassName("org.jallinone.items.java.GridItemVO");

      // add item types...
      init();

      // item code lookup...
      itemDataLocator.setGridMethodName("loadItems");
      itemDataLocator.setValidationMethodName("validateItemCode");
			itemDataLocator.getLookupFrameParams().put(ApplicationConsts.SHOW_ONLY_MOVABLE_ITEMS,Boolean.TRUE);
			itemDataLocator.getLookupValidationParameters().put(ApplicationConsts.SHOW_ONLY_MOVABLE_ITEMS,Boolean.TRUE);
      itemController.setShowErrorMessage(false);

      controlItemCode.setLookupController(itemController);
      itemController.setLookupDataLocator(itemDataLocator);
      itemController.setFrameTitle("items");

      itemController.setCodeSelectionWindow(itemController.TREE_GRID_FRAME);
      treeLevelDataLocator.setServerMethodName("loadHierarchy");
      itemDataLocator.setTreeDataLocator(treeLevelDataLocator);
      itemDataLocator.setNodeNameAttribute("descriptionSYS10");

      itemController.setLookupValueObjectClassName("org.jallinone.items.java.GridItemVO");
      itemController.addLookup2ParentLink("companyCodeSys01ITM01", "companyCodeSys01ITM01");
      itemController.addLookup2ParentLink("itemCodeITM01", "itemCodeITM01");
      itemController.addLookup2ParentLink("descriptionSYS10", "descriptionSYS10");

      itemController.setAllColumnVisible(false);
      itemController.setVisibleColumn("companyCodeSys01ITM01", true);
      itemController.setVisibleColumn("itemCodeITM01", true);
      itemController.setVisibleColumn("descriptionSYS10", true);
      itemController.setPreferredWidthColumn("descriptionSYS10", 200);
      itemController.setFramePreferedSize(new Dimension(650,500));
      itemController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          // fill in the grid v.o., according to the selected item settings...
          GridItemVO vo = (GridItemVO)filterPanel.getVOModel().getValueObject();
          if (vo.getItemCodeITM01()==null || vo.getItemCodeITM01().equals("")) {
            grid.getOtherGridParams().remove(ApplicationConsts.ITEM_PK);
            reloadButton.setEnabled(false);
          }
          else {
            grid.getOtherGridParams().put(ApplicationConsts.ITEM_PK,new ItemPK(
                vo.getCompanyCodeSys01ITM01(),
                vo.getItemCodeITM01()
            ));
            reloadButton.setEnabled(true);
          }
        }

        public void beforeLookupAction(ValueObject parentVO) { }

        public void forceValidate() {}

      });

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * Retrieve item types and fill in the item types combo box.
   */
  private void init() {
    Response res = ClientUtils.getData("loadItemTypes",new GridParams());
    final Domain d = new Domain("ITEM_TYPES");
    if (!res.isError()) {
      ItemTypeVO vo = null;
      java.util.List list = ((VOListResponse)res).getRows();
      for(int i=0;i<list.size();i++) {
        vo = (ItemTypeVO)list.get(i);
        d.addDomainPair(vo.getProgressiveHie02ITM02(),vo.getDescriptionSYS10());
      }
    }
    controlItemType.setDomain(d);
  }


  private void jbInit() throws Exception {
    reloadButton.setEnabled(false);
    controlItemDescr.setEnabled(false);
    titledBorder1 = new TitledBorder("");
    titledBorder2 = new TitledBorder("");
    this.setLayout(gridBagLayout2);
    topPanel.setLayout(gridBagLayout1);
    grid.setAutoLoadData(false);
    grid.setExportButton(exportButton);
    //grid.setFunctionId("WAR01");
    grid.setMaxSortedColumns(3);
    grid.setNavBar(navigatorBar);
    grid.setReloadButton(reloadButton);
    grid.setValueObjectClassName("org.jallinone.warehouse.availability.java.OrderedItemQtyVO");
    this.setBorder(titledBorder1);
    titledBorder1.setTitle(ClientSettings.getInstance().getResources().getResource("future item availability"));
    titledBorder1.setTitleColor(Color.blue);
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    filterPanel.setLayout(gridBagLayout2);
    labelItem.setText("item");
    controlItemCode.setMaxCharacters(20);
    controlItemCode.setAttributeName("itemCodeITM01");
    controlItemDescr.setAttributeName("descriptionSYS10");
    controlItemDescr.setRequired(true);
    colFutureAvailQty.setColumnFilterable(true);
    colFutureAvailQty.setColumnName("orderQtyDOC07");
    colFutureAvailQty.setColumnSortable(true);
    colFutureAvailQty.setPreferredWidth(90);
    controlItemType.addItemListener(new OrderedItemsPanel_controlItemType_itemAdapter(this));
    colDeliveryDate.setColumnSortable(true);
    colDeliveryDate.setPreferredWidth(120);
    colDeliveryDate.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colDeliveryDate.setColumnName("deliveryDateDOC07");
    colDocYear.setColumnFilterable(true);
    colDocYear.setColumnSortable(true);
    colDocYear.setPreferredWidth(60);
    colDocYear.setGrouping(false);
    colDocYear.setColumnName("docYearDOC06");
    colDocNr.setColumnName("docSequenceDOC06");
    colDocNr.setPreferredWidth(80);
    colDocNr.setGrouping(false);
    colUM.setColumnName("minSellingQtyUmCodeReg02ITM01");
    colUM.setHeaderColumnName("minSellingQtyUmCodeReg02ITM01");
    colUM.setPreferredWidth(60);

    colWarCode.setColumnFilterable(true);
    colWarCode.setColumnName("warehouseCodeWar01DOC06");
    colWarCode.setColumnSortable(true);
    colWarCode.setPreferredWidth(90);
    colWarCode.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colWarDescr.setColumnName("descriptionWAR01");
    colWarDescr.setPreferredWidth(200);

    colDesc.setColumnName("descriptionSYS10");
    colDesc.setPreferredWidth(270);
    this.add(topPanel,    new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.add(grid,    new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    grid.getColumnContainer().add(colDesc, null);
    grid.getColumnContainer().add(colFutureAvailQty, null);
    grid.getColumnContainer().add(colUM, null);
    topPanel.add(buttonsPanel,     new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 0, 5), 0, 0));
    topPanel.add(filterPanel,    new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0, 0));
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(exportButton, null);
    buttonsPanel.add(navigatorBar, null);

    if (viewItemLookup) {
      filterPanel.add(labelItem,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
      filterPanel.add(controlItemType,   new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
      filterPanel.add(controlItemCode,   new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
      filterPanel.add(controlItemDescr,     new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    }

    grid.getColumnContainer().add(colDeliveryDate, null);
    grid.getColumnContainer().add(colDocYear, null);
    grid.getColumnContainer().add(colDocNr, null);

    if (viewWarehouse) {
      grid.getColumnContainer().add(colWarCode, null);
      grid.getColumnContainer().add(colWarDescr, null);
    }


  }


  void controlItemType_itemStateChanged(ItemEvent e) {
    if (e.getStateChange()==e.SELECTED) {
      controlItemCode.setValue(null);
      try {
        controlItemCode.validateCode(null);
      }
      catch (RestoreFocusOnInvalidCodeException ex) {
      }
      itemController.getLookupDataLocator().getLookupFrameParams().put(ApplicationConsts.PROGRESSIVE_HIE02,controlItemType.getValue());
      itemController.getLookupDataLocator().getLookupValidationParameters().put(ApplicationConsts.PROGRESSIVE_HIE02,controlItemType.getValue());
      treeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.PROGRESSIVE_HIE02,controlItemType.getValue());
      treeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.COMPANY_CODE_SYS01,grid.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01));
      itemController.getLookupDataLocator().getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,grid.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01));
      itemController.getLookupDataLocator().getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,grid.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01));

    }
  }


  public GridControl getGrid() {
    return grid;
  }


  /**
   * Enable or disable this panel.
   */
  public final void setEnabled(boolean enabled) {
    controlItemCode.setEnabled(enabled);
    controlItemType.setEnabled(enabled);
    navigatorBar.setEnabled(enabled);
    exportButton.setEnabled(enabled);
    if (!enabled) {
      grid.clearData();
      reloadButton.setEnabled(enabled);
    }


  }
  public CodLookupControl getControlItemCode() {
    return controlItemCode;
  }
  public ComboBoxControl getControlItemType() {
    return controlItemType;
  }



}

class OrderedItemsPanel_controlItemType_itemAdapter implements java.awt.event.ItemListener {
  OrderedItemsPanel adaptee;

  OrderedItemsPanel_controlItemType_itemAdapter(OrderedItemsPanel adaptee) {
    this.adaptee = adaptee;
  }
  public void itemStateChanged(ItemEvent e) {
    adaptee.controlItemType_itemStateChanged(e);
  }
}
