package org.jallinone.warehouse.movements.client;

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
import javax.swing.border.*;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.domains.java.Domain;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.items.java.ItemTypeVO;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.lookup.client.LookupListener;
import org.openswing.swing.message.receive.java.*;
import java.util.Collection;
import org.jallinone.warehouse.tables.movements.java.MovementVO;
import org.jallinone.warehouse.java.WarehouseVO;
import org.jallinone.items.java.GridItemVO;
import org.openswing.swing.util.java.Consts;
import org.jallinone.hierarchies.java.HierarchyLevelVO;
import org.openswing.swing.message.receive.java.VOResponse;
import org.jallinone.variants.client.ProductVariantsPanel;
import org.jallinone.variants.client.ProductVariantsController;
import javax.swing.JSplitPane;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Employee detail frame.</p>
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
public class ManualMovementDetailFrame extends InternalFrame {

  JPanel manualMovPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  JPanel buttonsPanel = new JPanel();
  SaveButton saveButton = new SaveButton();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  BorderLayout borderLayout1 = new BorderLayout();
  JTabbedPane tabbedPane = new JTabbedPane();
  BorderLayout borderLayout2 = new BorderLayout();
  BorderLayout borderLayout3 = new BorderLayout();
  TitledBorder titledBorder1;
  BorderLayout borderLayout4 = new BorderLayout();
  Form manualMovForm = new Form();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  LabelControl labelWarehouse = new LabelControl();
  CodLookupControl controlWarehouseCode = new CodLookupControl();
  TextControl controlWarehouseDescr = new TextControl();
  LabelControl labelItem = new LabelControl();
  ComboBoxControl controlItemType = new ComboBoxControl();
  CodLookupControl controlItem = new CodLookupControl();
  TextControl controlItemDescr = new TextControl();
  LabelControl labelPosition = new LabelControl();
  CodLookupControl controlPositionCode = new CodLookupControl();
  LabelControl labelWarItemType = new LabelControl();
  ComboBoxControl controlWarItemType = new ComboBoxControl();
  LabelControl labelQty = new LabelControl();
  NumericControl controlQty = new NumericControl();
  LabelControl labelMotive = new LabelControl();
  CodLookupControl controlMotiveCode = new CodLookupControl();
  TextControl controlMotiveDescr = new TextControl();
  LabelControl labelNote = new LabelControl();
  TextAreaControl controlNote = new TextAreaControl();
  TextControl controlUmCode = new TextControl();

  /** item code lookup data locator */
  LookupServerDataLocator itemDataLocator = new LookupServerDataLocator();

  /** item code lookup controller */
  LookupController itemController = new LookupController();

  LookupServerDataLocator levelDataLocator = new LookupServerDataLocator();
  TreeServerDataLocator treeLevelDataLocator = new TreeServerDataLocator();

  /** warehouse code lookup data locator */
  LookupServerDataLocator warDataLocator = new LookupServerDataLocator();

  /** warehouse code lookup controller */
  LookupController warController = new LookupController();

  /** warehouse position lookup controller */
  LookupController posController = new LookupController();
  LookupServerDataLocator posDataLocator = new LookupServerDataLocator();
  TreeServerDataLocator treeLevelPosDataLocator = new TreeServerDataLocator();

  /** warehouse motive code lookup data locator */
  LookupServerDataLocator motiveDataLocator = new LookupServerDataLocator();

  /** warehouse motive code lookup controller */
  LookupController motiveController = new LookupController();

  private boolean serialNumbersRequired = false;

  private ProductVariantsPanel variantsPanel = new ProductVariantsPanel(
      new ProductVariantsController() {

        public BigDecimal validateQty(BigDecimal qty) {
          return qty;
        }

        public void qtyUpdated(BigDecimal qty) {
        }

      },
      manualMovForm,
      controlItem,
      itemController,
      "loadProductVariantsMatrix",
      //"loadSaleDocVariantsRow",
      controlQty,
      null,
      0,
      true
  );


  public ManualMovementDetailFrame(ManualMovementController controller) {
    try {
      jbInit();
      setSize(620,550);
      setMinimumSize(new Dimension(620,550));

      manualMovForm.setFormController(controller);

      // add item types...
      init();

      // warehouse code lookup...
      warDataLocator.setGridMethodName("loadWarehouses");
      warDataLocator.setValidationMethodName("validateWarehouseCode");

      controlWarehouseCode.setLookupController(warController);
      controlWarehouseCode.setControllerMethodName("getWarehousesList");

      warController.setLookupDataLocator(warDataLocator);
      warController.setFrameTitle("warehouses");

      warController.setLookupValueObjectClassName("org.jallinone.warehouse.java.WarehouseVO");
      warController.addLookup2ParentLink("companyCodeSys01WAR01", "companyCodeSys01WAR02");
      warController.addLookup2ParentLink("warehouseCodeWAR01", "warehouseCodeWar01WAR02");
      warController.addLookup2ParentLink("descriptionWAR01", "descriptionWAR01");
      warController.addLookup2ParentLink("progressiveHie01HIE02", "progressiveHie01WAR02");
      warController.addLookup2ParentLink("descriptionWAR01", "locationDescriptionSYS10");

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
          MovementVO vo = (MovementVO)manualMovForm.getVOModel().getValueObject();
          if (vo.getWarehouseCodeWar01WAR02()==null || vo.getWarehouseCodeWar01WAR02().equals("")) {
            controlItem.setValue(null);
            controlItemDescr.setValue(null);
            controlUmCode.setValue(null);
            controlQty.setValue(null);
            controlQty.setEnabled(false);
            controlItem.setEnabled(false);
            controlPositionCode.setValue(null);
            controlPositionCode.setEnabled(false);
          }
          else {
            controlItem.setValue(null);
            controlItemDescr.setValue(null);
            controlUmCode.setValue(null);
            controlQty.setValue(null);
            controlQty.setEnabled(false);
            controlItem.setEnabled(true);
            controlPositionCode.setEnabled(true);
            WarehouseVO lookupVO = (WarehouseVO)warController.getLookupVO();
            treeLevelPosDataLocator.getTreeNodeParams().put(ApplicationConsts.PROGRESSIVE_HIE02,lookupVO.getProgressiveHie02WAR01());

            // set root level as default position...
            Response res = ClientUtils.getData("getRootLevel",lookupVO.getProgressiveHie02WAR01());
            if (!res.isError()) {
              HierarchyLevelVO posVO = (HierarchyLevelVO)((VOResponse)res).getVo();
              vo.setProgressiveHie01WAR02(posVO.getProgressiveHIE01());
              vo.setLocationDescriptionSYS10(posVO.getDescriptionSYS10());
            }


          }
        }

        public void beforeLookupAction(ValueObject parentVO) { }

        public void forceValidate() {}

      });

      // item code lookup...
      itemDataLocator.setGridMethodName("loadItems");
      itemDataLocator.setValidationMethodName("validateItemCode");
			itemDataLocator.getLookupFrameParams().put(ApplicationConsts.SHOW_ONLY_MOVABLE_ITEMS,Boolean.TRUE);
			itemDataLocator.getLookupValidationParameters().put(ApplicationConsts.SHOW_ONLY_MOVABLE_ITEMS,Boolean.TRUE);

      controlItem.setLookupController(itemController);
      controlItem.setControllerMethodName("getItemsList");

      itemController.setLookupDataLocator(itemDataLocator);
      itemController.setFrameTitle("items");

      itemController.setCodeSelectionWindow(itemController.TREE_GRID_FRAME);
      treeLevelDataLocator.setServerMethodName("loadHierarchy");
      itemDataLocator.setTreeDataLocator(treeLevelDataLocator);
      itemDataLocator.setNodeNameAttribute("descriptionSYS10");

      itemController.setLookupValueObjectClassName("org.jallinone.items.java.GridItemVO");
      itemController.addLookup2ParentLink("itemCodeITM01", "itemCodeItm01WAR02");
      itemController.addLookup2ParentLink("descriptionSYS10", "itemDescriptionSYS10");

      itemController.setAllColumnVisible(false);
      itemController.setVisibleColumn("companyCodeSys01ITM01", true);
      itemController.setVisibleColumn("itemCodeITM01", true);
      itemController.setVisibleColumn("descriptionSYS10", true);
      itemController.setPreferredWidthColumn("descriptionSYS10", 200);
      itemController.setFramePreferedSize(new Dimension(650,500));
      itemController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          MovementVO vo = (MovementVO)manualMovForm.getVOModel().getValueObject();
          GridItemVO lookupVO = (GridItemVO)itemController.getLookupVO();
          if (vo.getItemCodeItm01WAR02()==null || vo.getItemCodeItm01WAR02().equals("") ) {
            controlUmCode.setValue(null);
            controlQty.setValue(null);
            controlQty.setEnabled(false);
          }
          else {
            controlUmCode.setValue(lookupVO.getMinSellingQtyUmCodeReg02ITM01());
            controlQty.setValue(null);
            controlQty.setEnabled(true);
            serialNumbersRequired = lookupVO.getSerialNumberRequiredITM01().booleanValue();
            controlQty.setDecimals(lookupVO.getDecimalsREG02().intValue());
          }
        }

        public void beforeLookupAction(ValueObject parentVO) {
          MovementVO vo = (MovementVO)manualMovForm.getVOModel().getValueObject();
          itemDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01WAR02());
          itemDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01WAR02());
          itemDataLocator.getLookupFrameParams().put(ApplicationConsts.PROGRESSIVE_HIE02,controlItemType.getValue());
          itemDataLocator.getLookupValidationParameters().put(ApplicationConsts.PROGRESSIVE_HIE02,controlItemType.getValue());

        	treeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01WAR02());
					treeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.PROGRESSIVE_HIE02,controlItemType.getValue());
        }

        public void forceValidate() {}

      });

      // warehouse position code lookup...
      controlPositionCode.setLookupController(posController);
      posController.setLookupDataLocator(posDataLocator);
      posController.setFrameTitle("warehouse positions");

      posController.setCodeSelectionWindow(posController.TREE_FRAME);
      treeLevelPosDataLocator.setServerMethodName("loadHierarchy");
      posDataLocator.setTreeDataLocator(treeLevelPosDataLocator);
      posDataLocator.setNodeNameAttribute("descriptionSYS10");

      posController.setLookupValueObjectClassName("org.jallinone.hierarchies.java.HierarchyLevelVO");
      posController.addLookup2ParentLink("progressiveHIE01", "progressiveHie01WAR02");
      posController.addLookup2ParentLink("descriptionSYS10","locationDescriptionSYS10");
      posController.setFramePreferedSize(new Dimension(400,400));

      // warehouse motive code lookup...
      motiveDataLocator.setGridMethodName("loadWarehouseMotives");
      motiveDataLocator.setValidationMethodName("validateWarehouseMotiveCode");

      controlMotiveCode.setLookupController(motiveController);
      controlMotiveCode.setControllerMethodName("getWarehouseMotivesList");

      motiveController.setLookupDataLocator(motiveDataLocator);
      motiveController.setFrameTitle("warehouse motives");

      motiveController.setLookupValueObjectClassName("org.jallinone.warehouse.tables.motives.java.MotiveVO");
      motiveController.addLookup2ParentLink("warehouseMotiveWAR04", "warehouseMotiveWar04WAR02");
      motiveController.addLookup2ParentLink("descriptionSYS10", "motiveDescriptionSYS10");

      motiveController.setAllColumnVisible(false);
      motiveController.setVisibleColumn("warehouseMotiveWAR04", true);
      motiveController.setVisibleColumn("descriptionSYS10", true);
      motiveController.setPreferredWidthColumn("descriptionSYS10", 250);
      motiveController.setFramePreferedSize(new Dimension(360,500));
      motiveController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          MovementVO vo = (MovementVO)manualMovForm.getVOModel().getValueObject();
          controlNote.setValue(vo.getMotiveDescriptionSYS10());
        }

        public void beforeLookupAction(ValueObject parentVO) {}

        public void forceValidate() {}

      });

      manualMovForm.setMode(Consts.INSERT);
      initControls();

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
    controlUmCode.setColumns(5);
    titledBorder1 = new TitledBorder("");
    this.setTitle(ClientSettings.getInstance().getResources().getResource("manual movement"));
    this.getContentPane().setLayout(borderLayout3);
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    manualMovPanel.setLayout(borderLayout4);
    manualMovForm.setBorder(titledBorder1);
    titledBorder1.setTitleColor(Color.blue);
    titledBorder1.setTitle(ClientSettings.getInstance().getResources().getResource("manual movement"));
    manualMovForm.setVOClassName("org.jallinone.warehouse.tables.movements.java.MovementVO");
    manualMovForm.setSaveButton(saveButton);
    manualMovForm.setFunctionId("WAR03_MOVEMENT");
    manualMovForm.setLayout(gridBagLayout2);
    labelWarehouse.setToolTipText("");
    labelWarehouse.setText("warehouse");
    labelItem.setText("item");
    labelPosition.setText("locationDescriptionSYS10");
    labelWarItemType.setText("itemTypeWAR04");
    labelQty.setText("deltaQtyWAR02");
    labelMotive.setText("warehouseMotiveWAR04");
    labelNote.setText("note");
    controlWarehouseCode.setAttributeName("warehouseCodeWar01WAR02");
    controlWarehouseCode.setLinkLabel(labelWarehouse);
    controlWarehouseCode.setMaxCharacters(20);
    controlWarehouseCode.setRequired(true);
    controlWarehouseDescr.setAttributeName("descriptionWAR01");
    controlWarehouseDescr.setEnabled(false);
    controlWarehouseDescr.setEnabledOnInsert(false);
    controlWarehouseDescr.setEnabledOnEdit(false);
    controlItemType.setLinkLabel(labelItem);
    controlItemType.setAttributeName("progressiveHie02ITM01");
    controlItem.setAttributeName("itemCodeItm01WAR02");
    controlItem.setLinkLabel(labelItem);
    controlItem.setMaxCharacters(20);
    controlItem.setRequired(true);
    controlItemDescr.setEnabled(false);
    controlItemDescr.setEnabledOnInsert(false);
    controlItemDescr.setEnabledOnEdit(false);
    controlItemDescr.setAttributeName("itemDescriptionSYS10");
    controlPositionCode.setAttributeName("locationDescriptionSYS10");
    controlPositionCode.setColumns(30);
    controlPositionCode.setLinkLabel(labelPosition);
    controlPositionCode.setRequired(true);
    controlPositionCode.setEnableCodBox(false);
    controlPositionCode.setEnabledOnEdit(false);
    controlPositionCode.setEnabledOnInsert(false);
    controlWarItemType.setAttributeName("itemTypeWAR04");
    controlWarItemType.setDomainId("WAR_ITEM_TYPE");
    controlWarItemType.setLinkLabel(labelWarItemType);
    controlWarItemType.setRequired(true);
    controlQty.setAttributeName("deltaQtyWAR02");
    controlQty.setLinkLabel(labelQty);
    controlQty.setMinValue(0.0);
    controlQty.setRequired(true);
    controlMotiveCode.setLinkLabel(labelMotive);
    controlMotiveCode.setMaxCharacters(20);
    controlMotiveCode.setRequired(true);
    controlMotiveCode.setAttributeName("warehouseMotiveWar04WAR02");
    controlMotiveDescr.setEnabled(false);
    controlMotiveDescr.setEnabledOnInsert(false);
    controlMotiveDescr.setEnabledOnEdit(false);
    controlMotiveDescr.setAttributeName("motiveDescriptionSYS10");
    controlNote.setLinkLabel(labelNote);
    controlNote.setRequired(true);
    controlNote.setAttributeName("noteWAR02");
    this.getContentPane().add(tabbedPane, BorderLayout.CENTER);
    tabbedPane.add(manualMovPanel, "manual movement");
    manualMovPanel.add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(saveButton, null);
    manualMovPanel.add(manualMovForm,  BorderLayout.CENTER);
    manualMovForm.add(labelWarehouse,       new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    manualMovForm.add(controlWarehouseCode,        new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    manualMovForm.add(controlWarehouseDescr,        new GridBagConstraints(3, 0, 3, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    manualMovForm.add(labelItem,       new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
    manualMovForm.add(controlItemType,        new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    manualMovForm.add(controlItem,        new GridBagConstraints(2, 1, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    manualMovForm.add(controlItemDescr,       new GridBagConstraints(4, 1, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    manualMovForm.add(labelPosition,      new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    manualMovForm.add(controlPositionCode,        new GridBagConstraints(1, 3, 5, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    manualMovForm.add(labelWarItemType,      new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    manualMovForm.add(controlWarItemType,     new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    manualMovForm.add(labelQty,      new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    manualMovForm.add(controlQty,      new GridBagConstraints(3, 4, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    manualMovForm.add(labelMotive,      new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    manualMovForm.add(controlMotiveCode,       new GridBagConstraints(1, 5, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    manualMovForm.add(controlMotiveDescr,      new GridBagConstraints(3, 5, 3, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    manualMovForm.add(labelNote,       new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    manualMovForm.add(controlNote,       new GridBagConstraints(1, 6, 5, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    manualMovForm.add(controlUmCode,    new GridBagConstraints(5, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    manualMovForm.add(variantsPanel,   new GridBagConstraints(0, 2, 6, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    tabbedPane.setTitleAt(0,ClientSettings.getInstance().getResources().getResource("manual movement"));
  }


  public void initControls() {
    controlUmCode.setEnabled(false);
    controlUmCode.setEnabledOnInsert(false);
    controlUmCode.setEnabledOnEdit(false);
    controlItem.setEnabled(false);
    controlQty.setEnabled(false);
    controlPositionCode.setEnabled(false);
  }


  public final boolean isSerialNumbersRequired() {
    return serialNumbersRequired;
  }
  public Form getManualMovForm() {
    return manualMovForm;
  }

  public ProductVariantsPanel getVariantsPanel() {
    return variantsPanel;
  }



}
