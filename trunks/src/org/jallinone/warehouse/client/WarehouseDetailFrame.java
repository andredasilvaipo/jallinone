package org.jallinone.warehouse.client;

import org.openswing.swing.mdi.client.InternalFrame;
import org.openswing.swing.tree.client.*;
import java.awt.*;
import org.openswing.swing.mdi.client.MDIFrame;
import javax.swing.*;
import org.openswing.swing.client.*;
import org.openswing.swing.table.java.ServerGridDataLocator;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.form.client.*;
import org.jallinone.commons.client.*;
import java.math.BigDecimal;
import org.jallinone.hierarchies.client.HierarTreePanel;
import javax.swing.tree.DefaultMutableTreeNode;
import org.jallinone.hierarchies.java.HierarchyLevelVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.warehouse.availability.client.*;
import java.util.HashSet;
import org.jallinone.hierarchies.client.HierarTreeListener;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Warehouses grid frame.</p>
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
public class WarehouseDetailFrame extends InternalFrame {

  Form warehouseForm = new Form();
  JPanel buttonsPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  InsertButton insertButton = new InsertButton();
  EditButton editButton = new EditButton();
  CopyButton copyButton = new CopyButton();
  SaveButton saveButton = new SaveButton();
  ReloadButton reloadButton = new ReloadButton();
  DeleteButton deleteButton = new DeleteButton();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  BorderLayout borderLayout1 = new BorderLayout();
  TextControl controlZip = new TextControl();
  TextControl controlProv = new TextControl();
  TextControl controlCity = new TextControl();
  TextControl controlAddress = new TextControl();
  LabelControl labelAddress = new LabelControl();
  TextControl controlCountry = new TextControl();
  LabelControl labelZip = new LabelControl();
  LabelControl labelCity = new LabelControl();
  LabelControl labelCountry = new LabelControl();
  LabelControl labelProv = new LabelControl();
  TextControl controlWarCode = new TextControl();
  LabelControl labelWarCode = new LabelControl();
  LabelControl labelDescr = new LabelControl();
  TextControl controlDescr = new TextControl();
  JTabbedPane tabbedPane = new JTabbedPane();
  JPanel locationsPanel = new JPanel();
  JPanel warehousePanel = new JPanel();
  LabelControl labelCompanyCode = new LabelControl();
  CompaniesComboControl controlCompaniesCombo = new CompaniesComboControl();
  ComboBoxControl controlRoles = new ComboBoxControl();
  LabelControl labelRoles = new LabelControl();
  BorderLayout borderLayout2 = new BorderLayout();

  HierarTreePanel hierarTreePanel = new HierarTreePanel() {

    /**
     * Callback method invoked when the user has clicked the left mouse button a tree node.
     * @param node selected node
     */
    public void leftClick(DefaultMutableTreeNode node) {
      if (hierarTreePanel.getSelectedNode()!=null) {
        HierarchyLevelVO levelVO = (HierarchyLevelVO)hierarTreePanel.getSelectedNode().getUserObject();
        getAvailPanel().getGrid().getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_HIE01,levelVO.getProgressiveHIE01());
      }
    }

  };

  JSplitPane split = new JSplitPane();
  ItemAvailabilityPanel availPanel = new ItemAvailabilityPanel();
  BookedItemsPanel bookedItemsPanel = new BookedItemsPanel(false,true);
  OrderedItemsPanel orderedItemsPanel = new OrderedItemsPanel(false,true);
  NavigatorBar navigatorBar = new NavigatorBar();


  public WarehouseDetailFrame(WarehouseController controller) {
    try {
      jbInit();
      setSize(750,450);
      setMinimumSize(new Dimension(750,450));

      hierarTreePanel.addHierarTreeListener(new HierarTreeListener(){

        public void loadDataCompleted(boolean error) {
          if (hierarTreePanel.getTree().getRowCount()>0)
            hierarTreePanel.getTree().setSelectionRow(0);
          if (hierarTreePanel.getTree().getSelectionPath()!=null)
            hierarTreePanel.leftClick((DefaultMutableTreeNode)hierarTreePanel.getTree().getSelectionPath().getLastPathComponent());
        }

      });


      // link the parent grid to the current Form...
      HashSet pk = new HashSet();
      pk.add("companyCodeSys01WAR01");
      pk.add("warehouseCodeWAR01");
      warehouseForm.linkGrid(controller.getGridFrame().getGrid(),pk,true,true,true,navigatorBar);

      warehouseForm.setFormController(controller);

      CustomizedControls customizedControls = new CustomizedControls(tabbedPane,warehouseForm,new BigDecimal(462));

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    hierarTreePanel.setEnabled(false);
    warehousePanel.setLayout(borderLayout1);
    warehouseForm.setVOClassName("org.jallinone.warehouse.java.WarehouseVO");
    this.setTitle(ClientSettings.getInstance().getResources().getResource("warehouse detail"));
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    insertButton.setText("insertButton1");
    reloadButton.setText("reloadButton1");
    deleteButton.setText("deleteButton1");
    warehouseForm.setInsertButton(insertButton);
    warehouseForm.setCopyButton(copyButton);
    warehouseForm.setEditButton(editButton);
    warehouseForm.setReloadButton(reloadButton);
    warehouseForm.setDeleteButton(deleteButton);
    warehouseForm.setSaveButton(saveButton);
    warehouseForm.setFunctionId("WAR01");
    warehouseForm.setLayout(gridBagLayout1);
    controlZip.setAttributeName("zipWAR01");
    controlZip.setCanCopy(true);
    controlZip.setLinkLabel(labelZip);
    controlZip.setMaxCharacters(20);
    controlProv.setAttributeName("provinceWAR01");
    controlProv.setCanCopy(true);
    controlProv.setLinkLabel(labelProv);
    controlProv.setMaxCharacters(20);
    controlProv.setTrimText(true);
    controlProv.setUpperCase(true);
    controlCity.setAttributeName("cityWAR01");
    controlCity.setCanCopy(true);
    controlCity.setLinkLabel(labelCity);
    controlAddress.setAttributeName("addressWAR01");
    controlAddress.setCanCopy(true);
    controlAddress.setLinkLabel(labelAddress);
    labelAddress.setText("address");
    controlCountry.setAttributeName("countryWAR01");
    controlCountry.setCanCopy(true);
    controlCountry.setLinkLabel(labelCountry);
    controlCountry.setMaxCharacters(20);
    controlCountry.setTrimText(true);
    controlCountry.setUpperCase(true);
    labelZip.setText("zip");
    labelCity.setText("city");
    labelCountry.setText("country");
    labelProv.setText("prov");
    controlWarCode.setAttributeName("warehouseCodeWAR01");
    controlWarCode.setLinkLabel(labelWarCode);
    controlWarCode.setMaxCharacters(20);
    controlWarCode.setRequired(true);
    controlWarCode.setRpadding(false);
    controlWarCode.setTrimText(true);
    controlWarCode.setUpperCase(true);
    controlWarCode.setEnabledOnEdit(false);
    labelWarCode.setText("warehouseCodeWAR01");
    labelDescr.setText("descriptionWAR01");
    controlDescr.setAttributeName("descriptionWAR01");
    controlDescr.setLinkLabel(labelDescr);
    controlDescr.setRequired(true);
    labelCompanyCode.setText("companyCodeSys01WAR01");
    controlCompaniesCombo.setAttributeName("companyCodeSys01WAR01");
    controlCompaniesCombo.setCanCopy(true);
    controlCompaniesCombo.setLinkLabel(labelCompanyCode);
    controlCompaniesCombo.setRequired(true);
    controlCompaniesCombo.setEnabledOnEdit(false);
    labelRoles.setText("edit/delete warehouse role");
    controlRoles.setAttributeName("progressiveSys04WAR01");
    controlRoles.setDomainId("USERROLES");
    controlRoles.setLinkLabel(labelRoles);
    locationsPanel.setLayout(borderLayout2);
    tabbedPane.add(warehousePanel,   "warehousePanel");
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    this.getContentPane().add(tabbedPane,  BorderLayout.CENTER);
    warehouseForm.add(labelZip,               new GridBagConstraints(3, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    warehouseForm.add(labelCity,             new GridBagConstraints(0, 4, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    warehouseForm.add(labelAddress,          new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    warehouseForm.add(controlAddress,            new GridBagConstraints(2, 3, 3, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    warehouseForm.add(controlCity,          new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 100, 0));
    warehouseForm.add(controlZip,        new GridBagConstraints(4, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    warehouseForm.add(controlWarCode,         new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 100, 0));
    warehousePanel.add(warehouseForm,BorderLayout.CENTER);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(copyButton, null);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(saveButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(deleteButton, null);
    buttonsPanel.add(navigatorBar, null);
    warehouseForm.add(labelWarCode,       new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
    warehouseForm.add(labelDescr,      new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    warehouseForm.add(controlDescr,     new GridBagConstraints(2, 2, 3, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    warehouseForm.add(controlCountry,       new GridBagConstraints(4, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    warehouseForm.add(labelCountry,      new GridBagConstraints(3, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    warehouseForm.add(labelProv,      new GridBagConstraints(0, 5, 1, 2, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 0), 0, 0));
    warehouseForm.add(controlProv,      new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    warehouseForm.add(labelCompanyCode,    new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    tabbedPane.add(locationsPanel,    "locationsPanel");
    locationsPanel.add(split, BorderLayout.CENTER);
    split.setDividerLocation(150);
    split.add(hierarTreePanel,JSplitPane.LEFT);
    split.add(availPanel,JSplitPane.RIGHT);
    tabbedPane.add(bookedItemsPanel,  "bookedItemsPanel");
    tabbedPane.add(orderedItemsPanel,  "orderedItemsPanel");
    hierarTreePanel.setFunctionId("WAR01");
    tabbedPane.setTitleAt(0,ClientSettings.getInstance().getResources().getResource("warehouse detail"));
    tabbedPane.setTitleAt(1,ClientSettings.getInstance().getResources().getResource("positions"));
    tabbedPane.setTitleAt(2,ClientSettings.getInstance().getResources().getResource("bookedItemsPanel"));
    tabbedPane.setTitleAt(3,ClientSettings.getInstance().getResources().getResource("orderedItemsPanel"));

    warehouseForm.add(controlCompaniesCombo,         new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    warehouseForm.add(controlRoles,         new GridBagConstraints(2, 6, 1, 2, 0.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    warehouseForm.add(labelRoles,     new GridBagConstraints(0, 7, 2, 1, 0.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
  }


  public Form getWarehouseForm() {
    return warehouseForm;
  }


  public CopyButton getCopyButton() {
    return copyButton;
  }


  public DeleteButton getDeleteButton() {
    return deleteButton;
  }


  public EditButton getEditButton() {
    return editButton;
  }


  public InsertButton getInsertButton() {
    return insertButton;
  }


  public ReloadButton getReloadButton() {
    return reloadButton;
  }

  public SaveButton getSaveButton() {
    return saveButton;
  }


  /**
   * @return positions of the warehouse
   */
  public final HierarTreePanel getHierarTreePanel() {
    return hierarTreePanel;
  }
  public ItemAvailabilityPanel getAvailPanel() {
    return availPanel;
  }
  public BookedItemsPanel getBookedItemsPanel() {
    return bookedItemsPanel;
  }
  public OrderedItemsPanel getOrderedItemsPanel() {
    return orderedItemsPanel;
  }

}
