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
import org.jallinone.warehouse.availability.java.ItemAvailabilityVO;
import org.openswing.swing.table.client.GridController;
import org.openswing.swing.table.java.ServerGridDataLocator;
import org.jallinone.items.java.ItemPK;
import org.openswing.swing.lookup.client.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Panel that contains an item filter and a grid listing items availability in the specified warehouse and position (if specified).</p>
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
public class ItemAvailabilityPanel extends JPanel {

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
  TextColumn colLocationDescription = new TextColumn();
  TextColumn colItemCode = new TextColumn();
  TextColumn colItemDescr = new TextColumn();
  DecimalColumn colAvailQty = new DecimalColumn();
  DecimalColumn colDamagedQty = new DecimalColumn();

  /** item code lookup data locator */
  LookupServerDataLocator itemDataLocator = new LookupServerDataLocator();

  /** item code lookup controller */
  LookupController itemController = new LookupController();

  /** grid data locator */
  ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();

  LookupServerDataLocator levelDataLocator = new LookupServerDataLocator();
  TreeServerDataLocator treeLevelDataLocator = new TreeServerDataLocator();


  public ItemAvailabilityPanel() {
    try {
      jbInit();

      grid.setController(new GridController());
      grid.setGridDataLocator(gridDataLocator);
      gridDataLocator.setServerMethodName("loadItemAvailabilities");

      filterPanel.setVOClassName("org.jallinone.warehouse.availability.java.ItemAvailabilityVO");

      // add item types...
      init();

      // item code lookup...
      itemDataLocator.setGridMethodName("loadItems");
      itemDataLocator.setValidationMethodName("validateItemCode");
			itemDataLocator.getLookupFrameParams().put(ApplicationConsts.SHOW_ONLY_MOVABLE_ITEMS,Boolean.TRUE);
			itemDataLocator.getLookupValidationParameters().put(ApplicationConsts.SHOW_ONLY_MOVABLE_ITEMS,Boolean.TRUE);

      controlItemCode.setLookupController(itemController);
      itemController.setLookupDataLocator(itemDataLocator);
      itemController.setFrameTitle("items");
      itemController.setShowErrorMessage(false);

      itemController.setCodeSelectionWindow(itemController.TREE_GRID_FRAME);
      treeLevelDataLocator.setServerMethodName("loadHierarchy");
      itemDataLocator.setTreeDataLocator(treeLevelDataLocator);
      itemDataLocator.setNodeNameAttribute("descriptionSYS10");

      itemController.setLookupValueObjectClassName("org.jallinone.items.java.GridItemVO");
      itemController.addLookup2ParentLink("companyCodeSys01ITM01", "companyCodeSys01WAR03");
      itemController.addLookup2ParentLink("itemCodeITM01", "itemCodeItm01WAR03");
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
          ItemAvailabilityVO vo = (ItemAvailabilityVO)filterPanel.getVOModel().getValueObject();
          if (vo.getItemCodeItm01WAR03()==null || vo.getItemCodeItm01WAR03().equals("")) {
            grid.getOtherGridParams().remove(ApplicationConsts.ITEM_PK);
          }
          else {
            grid.getOtherGridParams().put(ApplicationConsts.ITEM_PK,new ItemPK(
                vo.getCompanyCodeSys01WAR03(),
                vo.getItemCodeItm01WAR03()
            ));
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
    grid.setValueObjectClassName("org.jallinone.warehouse.availability.java.ItemAvailabilityVO");
    this.setBorder(titledBorder1);
    titledBorder1.setTitle(ClientSettings.getInstance().getResources().getResource("item availability"));
    titledBorder1.setTitleColor(Color.blue);
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    filterPanel.setLayout(gridBagLayout2);
    labelItem.setText("item");
    controlItemCode.setMaxCharacters(20);
    controlItemCode.setAttributeName("itemCodeItm01WAR03");
    controlItemDescr.setAttributeName("descriptionSYS10");
    colLocationDescription.setColumnFilterable(true);
    colLocationDescription.setColumnName("locationDescriptionSYS10");
    colLocationDescription.setColumnSortable(true);
    colLocationDescription.setPreferredWidth(120);
    colItemCode.setColumnFilterable(true);
    colItemCode.setColumnName("itemCodeItm01WAR03");
    colItemCode.setColumnSortable(true);
    colItemCode.setPreferredWidth(90);
    colItemCode.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colItemDescr.setColumnFilterable(true);
    colItemDescr.setColumnName("descriptionSYS10");
    colItemDescr.setColumnSortable(true);
    colItemDescr.setHeaderColumnName("itemDescriptionSYS10");
    colItemDescr.setPreferredWidth(200);
    colAvailQty.setColumnFilterable(true);
    colAvailQty.setColumnName("availableQtyWAR03");
    colAvailQty.setColumnSortable(true);
    colAvailQty.setPreferredWidth(70);
    colDamagedQty.setColumnFilterable(true);
    colDamagedQty.setColumnName("damagedQtyWAR03");
    colDamagedQty.setColumnSortable(true);
    colDamagedQty.setPreferredWidth(80);
    controlItemType.addItemListener(new ItemAvailabilityPanel_controlItemType_itemAdapter(this));
    this.add(topPanel,    new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.add(grid,    new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    grid.getColumnContainer().add(colLocationDescription, null);
    grid.getColumnContainer().add(colItemCode, null);
    grid.getColumnContainer().add(colItemDescr, null);
    grid.getColumnContainer().add(colAvailQty, null);
    grid.getColumnContainer().add(colDamagedQty, null);
    topPanel.add(buttonsPanel,     new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 0, 5), 0, 0));
    topPanel.add(filterPanel,    new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0, 0));
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(exportButton, null);
    buttonsPanel.add(navigatorBar, null);
    filterPanel.add(labelItem,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlItemType,   new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlItemCode,   new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlItemDescr,     new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
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
    reloadButton.setEnabled(enabled);
    exportButton.setEnabled(enabled);
    if (!enabled) {
      grid.clearData();
    }


  }



}

class ItemAvailabilityPanel_controlItemType_itemAdapter implements java.awt.event.ItemListener {
  ItemAvailabilityPanel adaptee;

  ItemAvailabilityPanel_controlItemType_itemAdapter(ItemAvailabilityPanel adaptee) {
    this.adaptee = adaptee;
  }
  public void itemStateChanged(ItemEvent e) {
    adaptee.controlItemType_itemStateChanged(e);
  }
}
