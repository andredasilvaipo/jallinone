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
import org.openswing.swing.mdi.client.InternalFrame;
import org.jallinone.hierarchies.client.HierarTreePanel;
import javax.swing.tree.DefaultMutableTreeNode;
import org.jallinone.hierarchies.java.HierarchyLevelVO;
import org.jallinone.hierarchies.client.HierarTreeListener;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Internal Frame that contains a warehouse filter, an item filter and a grid listing items availability in the specified warehouse and position (if specified).</p>
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
public class ItemAvailabilityFrame extends InternalFrame {

  ItemAvailabilityPanel availPanel = new ItemAvailabilityPanel();
  Form warehousePanel = new Form();
  LabelControl labelWarehouse = new LabelControl();
  CodLookupControl controlWarehouseCod = new CodLookupControl();
  TextControl controlWarehouseDescr = new TextControl();
  FlowLayout flowLayout1 = new FlowLayout(FlowLayout.LEFT);

  /** warehouse code lookup data locator */
  LookupServerDataLocator warDataLocator = new LookupServerDataLocator();

  /** warehouse code lookup controller */
  LookupController warController = new LookupController();

  JSplitPane split = new JSplitPane();

  HierarTreePanel hierarTreePanel = new HierarTreePanel() {

    /**
     * Callback method invoked when the user has clicked the left mouse button a tree node.
     * @param node selected node
     */
    public void leftClick(DefaultMutableTreeNode node) {
      if (hierarTreePanel.getSelectedNode()!=null) {
        HierarchyLevelVO levelVO = (HierarchyLevelVO)hierarTreePanel.getSelectedNode().getUserObject();
        availPanel.getGrid().getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_HIE01,levelVO.getProgressiveHIE01());
        availPanel.getGrid().reloadData();
      }
    }

  };


  public ItemAvailabilityFrame() {
    try {
      jbInit();


      hierarTreePanel.addHierarTreeListener(new HierarTreeListener(){

        public void loadDataCompleted(boolean error) {
          if (hierarTreePanel.getTree().getRowCount()>0)
            hierarTreePanel.getTree().setSelectionRow(0);
          if (hierarTreePanel.getTree().getSelectionPath()!=null)
            hierarTreePanel.leftClick((DefaultMutableTreeNode)hierarTreePanel.getTree().getSelectionPath().getLastPathComponent());
        }

      });


      // warehouse code lookup...
      warDataLocator.setGridMethodName("loadWarehouses");
      warDataLocator.setValidationMethodName("validateWarehouseCode");

      controlWarehouseCod.setLookupController(warController);
      warController.setLookupDataLocator(warDataLocator);
      warController.setFrameTitle("warehouses");

      warController.setLookupValueObjectClassName("org.jallinone.warehouse.java.WarehouseVO");
      warController.addLookup2ParentLink("companyCodeSys01WAR01", "companyCodeSys01WAR01");
      warController.addLookup2ParentLink("warehouseCodeWAR01", "warehouseCodeWAR01");
      warController.addLookup2ParentLink("progressiveHie02WAR01", "progressiveHie02WAR01");
      warController.addLookup2ParentLink("descriptionWAR01", "descriptionWAR01");

      warController.setAllColumnVisible(false);
      warController.setVisibleColumn("companyCodeSys01WAR01", true);
      warController.setVisibleColumn("warehouseCodeWAR01", true);
      warController.setVisibleColumn("descriptionWAR01", true);
      warController.setPreferredWidthColumn("descriptionWAR01", 250);
      warController.setFramePreferedSize(new Dimension(460,500));
      warController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          // fill in the grid v.o., according to the selected war settings...
          WarehouseVO vo = (WarehouseVO)warehousePanel.getVOModel().getValueObject();
          if (vo.getWarehouseCodeWAR01()==null || vo.getWarehouseCodeWAR01().equals("")) {

            hierarTreePanel.setEnabled(false);
            hierarTreePanel.clearTree();

            availPanel.getGrid().clearData();
            availPanel.setEnabled(false);
          }
          else {

//            hierarTreePanel.setEnabled(true);
            hierarTreePanel.setCompanyCode(vo.getCompanyCodeSys01WAR01());
            hierarTreePanel.setProgressiveHIE02(vo.getProgressiveHie02WAR01());
            hierarTreePanel.reloadTree();

            availPanel.setEnabled(true);
            availPanel.getGrid().getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01WAR01());
            availPanel.getGrid().getOtherGridParams().put(ApplicationConsts.WAREHOUSE_CODE,vo.getWarehouseCodeWAR01());
            availPanel.getGrid().getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_HIE02,vo.getProgressiveHie02WAR01());
            if (hierarTreePanel.getSelectedNode()!=null) {
              HierarchyLevelVO levelVO = (HierarchyLevelVO)hierarTreePanel.getSelectedNode().getUserObject();
              availPanel.getGrid().getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_HIE01,levelVO.getProgressiveHIE01());
            }

          }
        }

        public void beforeLookupAction(ValueObject parentVO) { }

        public void forceValidate() {}

      });


      setSize(800,500);
      MDIFrame.getInstance().add(this);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    this.setTitle(ClientSettings.getInstance().getResources().getResource("item availability"));

    hierarTreePanel.setEnabled(false);
    availPanel.setEnabled(false);
    controlWarehouseDescr.setRequired(true);
    controlWarehouseDescr.setColumns(50);

    split.setDividerLocation(150);
    split.add(hierarTreePanel,JSplitPane.LEFT);
    split.add(availPanel,JSplitPane.RIGHT);
    //hierarTreePanel.setFunctionId("WAR01");

    warehousePanel.setVOClassName("org.jallinone.warehouse.java.WarehouseVO");
    labelWarehouse.setText("warehouse");
    controlWarehouseCod.setEnabled(true);
    controlWarehouseCod.setMaxCharacters(20);
    controlWarehouseCod.setAttributeName("warehouseCodeWAR01");
    controlWarehouseDescr.setEnabled(false);
    controlWarehouseDescr.setAttributeName("descriptionWAR01");
    warehousePanel.setLayout(flowLayout1);
    warehousePanel.add(labelWarehouse, null);
    warehousePanel.add(controlWarehouseCod, null);
    this.getContentPane().add(warehousePanel, BorderLayout.NORTH);
    this.getContentPane().add(split, BorderLayout.CENTER);
    warehousePanel.add(controlWarehouseDescr, null);

  }

}
