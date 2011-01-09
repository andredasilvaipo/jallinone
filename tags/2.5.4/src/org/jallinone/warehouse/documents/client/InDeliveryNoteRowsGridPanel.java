package org.jallinone.warehouse.documents.client;

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
import org.jallinone.warehouse.documents.java.*;
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
import org.jallinone.registers.measure.java.*;
import java.util.ArrayList;
import java.beans.Beans;
import org.jallinone.items.java.ItemPK;
import org.jallinone.purchases.items.java.SupplierItemPK;
import org.jallinone.purchases.documents.java.PurchaseDocPK;
import org.openswing.swing.table.client.GridController;
import org.jallinone.purchases.documents.java.GridPurchaseDocRowVO;
import org.jallinone.purchases.items.java.SupplierItemVO;
import org.jallinone.hierarchies.java.HierarchyLevelVO;
import java.util.HashSet;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Panel that contains the in delivery note rows grid + import order rows panel.</p>
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
public class InDeliveryNoteRowsGridPanel extends JPanel implements GenericButtonController {

  BorderLayout borderLayout1 = new BorderLayout();
  JPanel buttonsPanel = new JPanel();
  JSplitPane splitPane = new JSplitPane();
  FlowLayout flowLayout1 = new FlowLayout();
  InsertButton insertButton1 = new InsertButton();
  EditButton editButton1 = new EditButton();
  SaveButton saveButton1 = new SaveButton();
  ReloadButton reloadButton1 = new ReloadButton();
  DeleteButton deleteButton1 = new DeleteButton();
  NavigatorBar navigatorBar1 = new NavigatorBar();
  CopyButton copyButton1 = new CopyButton();
  GridControl grid = new GridControl();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  ExportButton exportButton1 = new ExportButton();
  DecimalColumn colRowNum = new DecimalColumn();
  TextColumn colSupplierItemCode = new TextColumn();
  TextColumn colItemDescr = new TextColumn();
  DecimalColumn colQty = new DecimalColumn();
  DecimalColumn colInQty = new DecimalColumn();
  TextColumn colUmCode = new TextColumn();

  /** header v.o. */
  private DetailDeliveryNoteVO parentVO = null;

  /** grid data locator */
  private ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();

  /** item code lookup controller */
  LookupController itemController = new LookupController();

  /** item code lookup data locator */
  LookupServerDataLocator itemDataLocator = new LookupServerDataLocator();

  LookupServerDataLocator levelDataLocator = new LookupServerDataLocator();
  TreeServerDataLocator treeLevelDataLocator = new TreeServerDataLocator();

  /** header panel */
  private Form headerPanel = null;

  /** detail frame */
  private InDeliveryNoteFrame frame = null;

  private Form detailPanel = new Form();

  IntegerColumn colYear = new IntegerColumn();
  CodLookupColumn colDocNumLookup = new CodLookupColumn();
  CodLookupColumn colItemCodeLookup = new CodLookupColumn();
  TextColumn colSupplierUmCode = new TextColumn();
  ComboColumn colItemType = new ComboColumn();

  LookupController docRefController = new LookupController();
  LookupServerDataLocator docRefDataLocator = new LookupServerDataLocator();

  CodLookupColumn colPositionLookup = new CodLookupColumn();

  /** warehouse position lookup controller */
  LookupController posController = new LookupController();
  LookupServerDataLocator posDataLocator = new LookupServerDataLocator();
  TreeServerDataLocator treeLevelPosDataLocator = new TreeServerDataLocator();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  TitledBorder titledBorder2;
  LabelControl labelPurOrderNr = new LabelControl();
  CodLookupControl controlPurOrderNr = new CodLookupControl();
  LabelControl labelDocYear = new LabelControl();
  NumericControl controlDocYear = new NumericControl();
  LabelControl labelGrid = new LabelControl();
  GridControl orderRows = new GridControl();
  GenericButton importButton = new GenericButton(new ImageIcon(ClientUtils.getImage("workflow.gif")));

  LookupController purOrderController = new LookupController();
  LookupServerDataLocator purOrderDataLocator = new LookupServerDataLocator();

  /** order rows grid data locator */
  private ServerGridDataLocator orderRowsDataLocator = new ServerGridDataLocator();

  TextColumn colPurchaseItemCode = new TextColumn();
  TextColumn colPurchaseSupplierItemCode = new TextColumn();
  TextColumn colPurchaseItemDescr = new TextColumn();
  DecimalColumn colPurchaseQty = new DecimalColumn();
  DecimalColumn colPurchaseInQty = new DecimalColumn();
  DecimalColumn colPurchaseSupplierInQty = new DecimalColumn();

  private InDeliveryNoteRowsController gridController = new InDeliveryNoteRowsController(this);

  CodLookupColumn colPurchasePositionLookup = new CodLookupColumn();
  LookupController posPurchaseController = new LookupController();
  LookupServerDataLocator posPurchaseDataLocator = new LookupServerDataLocator();
  TreeServerDataLocator treeLevelPosPurchaseDataLocator = new TreeServerDataLocator();

  private boolean serialNumberRequired = false;




  public InDeliveryNoteRowsGridPanel(InDeliveryNoteFrame frame,Form headerPanel) {
    this.frame = frame;
    this.headerPanel = headerPanel;
    try {
      jbInit();

      if (Beans.isDesignTime())
        return;

      init();

      grid.setController(gridController);
      grid.setGridDataLocator(gridDataLocator);
      gridDataLocator.setServerMethodName("loadInDeliveryNoteRows");


      // doc. ref. lookup...
      docRefDataLocator.setGridMethodName("loadPurchaseDocs");
      docRefDataLocator.setValidationMethodName("validatePurchaseDocNumber");
      docRefController.setLookupDataLocator(docRefDataLocator);
      docRefDataLocator.getLookupFrameParams().put(ApplicationConsts.DOC_STATE,ApplicationConsts.CONFIRMED);
      docRefDataLocator.getLookupValidationParameters().put(ApplicationConsts.DOC_STATE,ApplicationConsts.CONFIRMED);

      colDocNumLookup.setAllowOnlyNumbers(true);
      colDocNumLookup.setControllerMethodName("getPurchaseOrdersList");
      colDocNumLookup.setLookupController(docRefController);
      docRefController.setFrameTitle("confirmed purchase orders");
      docRefController.setLookupValueObjectClassName("org.jallinone.purchases.documents.java.GridPurchaseDocVO");
      docRefController.addLookup2ParentLink("docTypeDOC06","docTypeDoc06DOC09");
      docRefController.addLookup2ParentLink("docYearDOC06", "docYearDoc06DOC09");
      docRefController.addLookup2ParentLink("docNumberDOC06","docNumberDoc06DOC09");
      docRefController.addLookup2ParentLink("docSequenceDOC06","docSequenceDoc06DOC09");
      docRefController.setAllColumnVisible(false);
      docRefController.setVisibleColumn("companyCodeSys01DOC06", true);
//      docRefController.setVisibleColumn("docTypeDOC06", true);
      docRefController.setVisibleColumn("docYearDOC06", true);
      docRefController.setVisibleColumn("docSequenceDOC06", true);
      docRefController.setVisibleColumn("name_1REG04", true);
      docRefController.setVisibleColumn("docDateDOC06", true);
//      docRefController.setVisibleColumn("docStateDOC06", true);
      docRefController.setHeaderColumnName("name_1REG04", "corporateName1");
      docRefController.setPreferredWidthColumn("name_1REG04",200);
      docRefController.setFramePreferedSize(new Dimension(700,500));
      docRefController.setGroupingEnabledColumn("docYearDOC06", false);
//      docRefController.setDomainColumn("docTypeDOC06","DOC_TYPE");

      // item code lookup...
      itemDataLocator.setGridMethodName("loadSupplierItems");
      itemDataLocator.setValidationMethodName("validateSupplierItemCode");
      itemDataLocator.getLookupValidationParameters().put(ApplicationConsts.SHOW_ITEMS_WITHOUT_VARIANTS,Boolean.TRUE);
      itemDataLocator.getLookupFrameParams().put(ApplicationConsts.SHOW_ITEMS_WITHOUT_VARIANTS,Boolean.TRUE);
			itemDataLocator.getLookupValidationParameters().put(ApplicationConsts.SHOW_ONLY_MOVABLE_ITEMS,Boolean.TRUE);
      itemDataLocator.getLookupFrameParams().put(ApplicationConsts.SHOW_ONLY_MOVABLE_ITEMS,Boolean.TRUE);

      colItemCodeLookup.setLookupController(itemController);
      itemController.setLookupDataLocator(itemDataLocator);
      itemController.setFrameTitle("supplier items");

      itemController.setCodeSelectionWindow(itemController.TREE_GRID_FRAME);
      treeLevelDataLocator.setServerMethodName("loadHierarchy");
      itemDataLocator.setTreeDataLocator(treeLevelDataLocator);
      itemDataLocator.setNodeNameAttribute("descriptionSYS10");

      itemController.setLookupValueObjectClassName("org.jallinone.purchases.items.java.SupplierItemVO");
      itemController.addLookup2ParentLink("itemCodeItm01PUR02", "itemCodeItm01DOC09");
      itemController.addLookup2ParentLink("supplierItemCodePUR02","supplierItemCodePur02DOC09");
      itemController.addLookup2ParentLink("descriptionSYS10", "descriptionSYS10");
      itemController.addLookup2ParentLink("decimalsREG02", "supplierQtyDecimalsREG02");
      itemController.addLookup2ParentLink("umCodeReg02PUR02", "umCodeReg02PUR02");
      itemController.addLookup2ParentLink("minPurchaseQtyPUR02", "supplierQtyDOC09");
      itemController.addLookup2ParentLink("minSellingQtyUmCodeReg02ITM01","umCodeREG02");
      itemController.addLookup2ParentLink("minSellingQtyDecimalsReg02ITM01","decimalsREG02");
      itemController.addLookup2ParentLink("valueREG05","valueREG05");

      itemController.setAllColumnVisible(false);
      itemController.setVisibleColumn("itemCodeItm01PUR02", true);
      itemController.setVisibleColumn("supplierItemCodePUR02", true);
      itemController.setVisibleColumn("descriptionSYS10", true);
      itemController.setPreferredWidthColumn("descriptionSYS10", 200);
      itemController.setFramePreferedSize(new Dimension(650,500));
      itemController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          // fill in the grid v.o., according to the selected item settings...
          GridInDeliveryNoteRowVO vo = (GridInDeliveryNoteRowVO)grid.getVOListTableModel().getObjectForRow(grid.getSelectedRow());
          if (vo.getItemCodeItm01DOC09()==null || vo.getItemCodeItm01DOC09().equals("")) {
            vo.setSupplierItemCodePur02DOC09(null);
            vo.setDescriptionSYS10(null);
            vo.setUmCodeREG02(null);
            vo.setUmCodeReg02PUR02(null);
            vo.setQtyDOC09(null);
            vo.setSupplierQtyDOC09(null);
            vo.setQtyDOC09(null);
            serialNumberRequired = false;
          }
          else {
            if (vo.getSupplierQtyDOC09()!=null) {
              vo.setQtyDOC09(vo.getSupplierQtyDOC09().divide(vo.getValueREG05(),vo.getDecimalsREG02().intValue(),BigDecimal.ROUND_HALF_UP));
              grid.repaint();
            }

            serialNumberRequired = ((SupplierItemVO)itemController.getLookupVO()).getSerialNumberRequiredITM01().booleanValue();
          }
        }

        public void beforeLookupAction(ValueObject parentVO) {
          GridInDeliveryNoteRowVO vo = (GridInDeliveryNoteRowVO)grid.getVOListTableModel().getObjectForRow(grid.getSelectedRow());
          treeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.PROGRESSIVE_HIE02,vo.getProgressiveHie02DOC09());
          itemDataLocator.getLookupFrameParams().put(ApplicationConsts.PROGRESSIVE_HIE02,vo.getProgressiveHie02DOC09());
        }

        public void forceValidate() {}

      });

      // warehouse position code lookup...
      colPositionLookup.setColumnRequired(false);
      colPositionLookup.setLookupController(posController);
      posController.setLookupDataLocator(posDataLocator);
      posController.setFrameTitle("warehouse positions");

      posController.setCodeSelectionWindow(posController.TREE_FRAME);
      treeLevelPosDataLocator.setServerMethodName("loadHierarchy");
      posDataLocator.setTreeDataLocator(treeLevelPosDataLocator);
      posDataLocator.setNodeNameAttribute("descriptionSYS10");
      posController.setAllowTreeLeafSelectionOnly(false);
      posController.setLookupValueObjectClassName("org.jallinone.hierarchies.java.HierarchyLevelVO");
      posController.addLookup2ParentLink("progressiveHIE01", "progressiveHie01DOC09");
      posController.addLookup2ParentLink("descriptionSYS10","locationDescriptionSYS10");
      posController.setFramePreferedSize(new Dimension(400,400));
      posController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) { }

        public void beforeLookupAction(ValueObject parentVO) {
          GridInDeliveryNoteRowVO vo = (GridInDeliveryNoteRowVO)parentVO;
          vo.setProgressiveHie02DOC09( InDeliveryNoteRowsGridPanel.this.parentVO.getProgressiveHie02WAR01() );
          treeLevelPosDataLocator.getTreeNodeParams().put(ApplicationConsts.PROGRESSIVE_HIE02,vo.getProgressiveHie02DOC09());
        }

        public void forceValidate() {}

      });



      // purchase order lookup...
      purOrderDataLocator.setGridMethodName("loadPurchaseDocs");
      purOrderDataLocator.setValidationMethodName("validatePurchaseDocNumber");
      purOrderController.setLookupDataLocator(purOrderDataLocator);
      purOrderDataLocator.getLookupFrameParams().put(ApplicationConsts.DOC_STATE,ApplicationConsts.CONFIRMED);
      purOrderDataLocator.getLookupValidationParameters().put(ApplicationConsts.DOC_STATE,ApplicationConsts.CONFIRMED);

      controlPurOrderNr.setLookupController(purOrderController);
      controlPurOrderNr.setControllerMethodName("getPurchaseOrdersList");
      controlPurOrderNr.setAllowOnlyNumbers(true);
      purOrderController.setFrameTitle("confirmed purchase orders");
      purOrderController.setLookupValueObjectClassName("org.jallinone.purchases.documents.java.GridPurchaseDocVO");
      purOrderController.addLookup2ParentLink("docTypeDOC06","docTypeDoc06DOC09");
      purOrderController.addLookup2ParentLink("docYearDOC06", "docYearDoc06DOC09");
      purOrderController.addLookup2ParentLink("docNumberDOC06","docNumberDoc06DOC09");
      purOrderController.addLookup2ParentLink("docSequenceDOC06","docSequenceDoc06DOC09");
      purOrderController.setAllColumnVisible(false);
      purOrderController.setVisibleColumn("companyCodeSys01DOC06", true);
//      purOrderController.setVisibleColumn("docTypeDOC06", true);
      purOrderController.setVisibleColumn("docYearDOC06", true);
      purOrderController.setVisibleColumn("docSequenceDOC06", true);
      purOrderController.setVisibleColumn("name_1REG04", true);
      purOrderController.setVisibleColumn("docDateDOC06", true);
//      purOrderController.setVisibleColumn("docStateDOC06", true);
      purOrderController.setHeaderColumnName("name_1REG04", "corporateName1");
      purOrderController.setPreferredWidthColumn("name_1REG04",200);
      purOrderController.setFramePreferedSize(new Dimension(700,500));
      purOrderController.setGroupingEnabledColumn("docYearDOC06", false);
//      purOrderController.setDomainColumn("docTypeDOC06","DOC_TYPE");
      purOrderController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          // fill in the grid v.o., according to the selected item settings...
          GridInDeliveryNoteRowVO vo = (GridInDeliveryNoteRowVO)detailPanel.getVOModel().getValueObject();
          if (vo.getDocNumberDoc06DOC09()==null) {
            orderRows.setMode(Consts.READONLY);
            orderRows.clearData();
            importButton.setEnabled(false);
          }
          else {
            PurchaseDocPK pk = new PurchaseDocPK(
                InDeliveryNoteRowsGridPanel.this.parentVO.getCompanyCodeSys01DOC08(),
                vo.getDocTypeDoc06DOC09(),
                vo.getDocYearDoc06DOC09(),
                vo.getDocNumberDoc06DOC09()
            );
            orderRows.getOtherGridParams().put(ApplicationConsts.PURCHASE_DOC_PK,pk);
            orderRows.reloadData();
          }
        }

        public void beforeLookupAction(ValueObject parentVO) { }

        public void forceValidate() {}

      });

      orderRows.setGridDataLocator(orderRowsDataLocator);
      orderRowsDataLocator.setServerMethodName("loadPurchaseDocAndDelivNoteRows");
      orderRows.setController(new GridController() {

        public void loadDataCompleted(boolean error) {
          if (error || orderRows.getVOListTableModel().getRowCount()==0)
            return;
          orderRows.setMode(Consts.EDIT);

          Response res = ClientUtils.getData("getRootLevel",parentVO.getProgressiveHie02WAR01());
          if (!res.isError()) {
            HierarchyLevelVO posVO = (HierarchyLevelVO)((VOResponse)res).getVo();
            GridInDeliveryNoteRowVO rowVO = null;
            for(int i=0;i<orderRows.getVOListTableModel().getRowCount();i++) {
              rowVO = (GridInDeliveryNoteRowVO)orderRows.getVOListTableModel().getObjectForRow(i);
              rowVO.setProgressiveHie01DOC09(posVO.getProgressiveHIE01());
              rowVO.setLocationDescriptionSYS10(posVO.getDescriptionSYS10());
            }
          }
          orderRows.repaint();

          importButton.setEnabled(true);
        }

      });




      // warehouse position code in purchase rows grid lookup...
      colPurchasePositionLookup.setLookupController(posPurchaseController);
      posPurchaseController.setLookupDataLocator(posPurchaseDataLocator);
      posPurchaseController.setFrameTitle("warehouse positions");

      posPurchaseController.setCodeSelectionWindow(posController.TREE_FRAME);
      treeLevelPosPurchaseDataLocator.setServerMethodName("loadHierarchy");
      posPurchaseDataLocator.setTreeDataLocator(treeLevelPosPurchaseDataLocator);
      posPurchaseDataLocator.setNodeNameAttribute("descriptionSYS10");
      posPurchaseController.setAllowTreeLeafSelectionOnly(false);
      posPurchaseController.setLookupValueObjectClassName("org.jallinone.hierarchies.java.HierarchyLevelVO");
      posPurchaseController.addLookup2ParentLink("progressiveHIE01", "progressiveHie01DOC09");
      posPurchaseController.addLookup2ParentLink("descriptionSYS10","locationDescriptionSYS10");
      posPurchaseController.setFramePreferedSize(new Dimension(400,400));
      posPurchaseController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) { }

        public void beforeLookupAction(ValueObject parentVO) {
          GridInDeliveryNoteRowVO vo = (GridInDeliveryNoteRowVO)parentVO;

          vo.setProgressiveHie02DOC09( InDeliveryNoteRowsGridPanel.this.parentVO.getProgressiveHie02WAR01() );
          treeLevelPosPurchaseDataLocator.getTreeNodeParams().put(ApplicationConsts.PROGRESSIVE_HIE02,vo.getProgressiveHie02DOC09());
        }

        public void forceValidate() {}

      });


    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * Method called by GenericButton.setEnabled method to check if the button must be disabled.
   * @param button button whose abilitation must be checked
   * @return <code>true</code> if no policy is defined in the form/grid for the specified button, <code>false</code> if there exists a disabilitation policy for the specified button (through addButtonsNotEnabledOnState form/grid method)
   */
  public boolean isButtonDisabled(GenericButton button) {
    if (parentVO!=null && parentVO.getDocStateDOC08()!=null && parentVO.getDocStateDOC08().equals(ApplicationConsts.CLOSED))
      return true;
    else
      return false;
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
    colItemType.setDomain(d);
    colItemType.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange()==e.SELECTED && grid.getMode()!=Consts.READONLY) {
          GridInDeliveryNoteRowVO vo = (GridInDeliveryNoteRowVO)grid.getVOListTableModel().getObjectForRow(grid.getSelectedRow());
          vo.setItemCodeItm01DOC09(null);
          vo.setSupplierItemCodePur02DOC09(null);
          vo.setDescriptionSYS10(null);
          vo.setUmCodeREG02(null);
          vo.setUmCodeReg02PUR02(null);
          vo.setQtyDOC09(null);
          vo.setSupplierQtyDOC09(null);
          vo.setQtyDOC09(null);

          int selIndex = ((JComboBox)e.getSource()).getSelectedIndex();
          Object selValue = d.getDomainPairList()[selIndex].getCode();
          treeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.PROGRESSIVE_HIE02,selValue);
          itemDataLocator.getLookupFrameParams().put(ApplicationConsts.PROGRESSIVE_HIE02,selValue);
        }
      }
    });

    HashSet buttonsToDisable = new HashSet();
    buttonsToDisable.add(insertButton1);
    buttonsToDisable.add(copyButton1);
    buttonsToDisable.add(editButton1);
    buttonsToDisable.add(deleteButton1);
    grid.addButtonsNotEnabled(buttonsToDisable,this);

  }



  private void jbInit() throws Exception {
    controlDocYear.setEnabled(false);
    titledBorder2 = new TitledBorder("");
    titledBorder2.setTitle(ClientSettings.getInstance().getResources().getResource("load items from purchase order"));
    titledBorder2.setTitleColor(Color.blue);
    this.setLayout(borderLayout1);
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
    grid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    grid.setAutoLoadData(false);
    grid.setCopyButton(copyButton1);
    grid.setDeleteButton(deleteButton1);
    grid.setEditButton(editButton1);
    grid.setExportButton(exportButton1);
    grid.setFunctionId("DOC08_IN");
    grid.setMaxSortedColumns(3);
    grid.setInsertButton(insertButton1);
    grid.setNavBar(navigatorBar1);
    grid.setReloadButton(reloadButton1);
    grid.setSaveButton(saveButton1);
    detailPanel.setVOClassName("org.jallinone.warehouse.documents.java.GridInDeliveryNoteRowVO");
    grid.setValueObjectClassName("org.jallinone.warehouse.documents.java.GridInDeliveryNoteRowVO");
    colRowNum.setColumnFilterable(false);
    colRowNum.setColumnName("rowNumberDOC09");
    colRowNum.setColumnRequired(false);
    colRowNum.setColumnVisible(false);
    colRowNum.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colRowNum.setColumnSelectable(false);

    colSupplierItemCode.setColumnName("supplierItemCodePur02DOC09");
    colSupplierItemCode.setColumnSortable(false);
    colSupplierItemCode.setHeaderColumnName("supplierItemCode");
    colSupplierItemCode.setPreferredWidth(90);
    colItemDescr.setColumnName("descriptionSYS10");
    colItemDescr.setColumnSortable(false);
    colItemDescr.setHeaderColumnName("itemDescriptionSYS10");
    colItemDescr.setPreferredWidth(200);
    colQty.setDecimals(5);
    colQty.setGrouping(false);
    colQty.setColumnName("qtyDOC09");
    colQty.setColumnSortable(false);
    colQty.setEditableOnEdit(true);
    colQty.setEditableOnInsert(true);
    colQty.setPreferredWidth(50);

    colInQty.setDecimals(5);
    colInQty.setGrouping(false);
    colInQty.setColumnName("supplierQtyDOC09");
    colInQty.setColumnSortable(false);
    colInQty.setEditableOnEdit(true);
    colInQty.setEditableOnInsert(true);
    colInQty.setPreferredWidth(80);

    colUmCode.setColumnName("umCodeREG02");
    colUmCode.setHeaderColumnName("um");
    colUmCode.setColumnSortable(false);
    colUmCode.setPreferredWidth(40);

    insertButton1.setEnabled(false);
    copyButton1.setEnabled(false);
    saveButton1.setEnabled(false);
    deleteButton1.setEnabled(false);
    exportButton1.setEnabled(false);
    editButton1.setEnabled(false);
    colYear.setColumnDuplicable(true);
    colYear.setColumnFilterable(true);
    colYear.setColumnName("docYearDoc06DOC09");
    colYear.setColumnSortable(true);
    colYear.setEditableOnEdit(false);
    colYear.setHeaderColumnName("docYearDoc06DOC09");
    colYear.setPreferredWidth(70);
    colDocNumLookup.setColumnDuplicable(true);
    colDocNumLookup.setColumnFilterable(true);
    colDocNumLookup.setColumnName("docSequenceDoc06DOC09");
    colDocNumLookup.setColumnSortable(true);
    colDocNumLookup.setEditableOnEdit(false);
    colDocNumLookup.setEditableOnInsert(true);
    colDocNumLookup.setHeaderColumnName("docSequenceDoc06DOC09");
    colDocNumLookup.setPreferredWidth(70);
    colDocNumLookup.setSortVersus(org.openswing.swing.util.java.Consts.NO_SORTED);
    colDocNumLookup.setAllowOnlyNumbers(true);
    colItemCodeLookup.setColumnName("itemCodeItm01DOC09");
    colItemCodeLookup.setEditableOnEdit(false);
    colItemCodeLookup.setEditableOnInsert(true);
    colItemCodeLookup.setPreferredWidth(90);
    colItemCodeLookup.setMaxCharacters(20);
    colSupplierUmCode.setColumnName("umCodeReg02PUR02");
    colSupplierUmCode.setEditableOnEdit(false);
    colSupplierUmCode.setEditableOnInsert(false);
    colSupplierUmCode.setPreferredWidth(90);
    colSupplierUmCode.setMaxCharacters(20);
    colItemType.setColumnName("progressiveHie02DOC09");
    colItemType.setEditableOnInsert(true);
    colItemType.setPreferredWidth(80);
    colPositionLookup.setColumnDuplicable(true);
    colPositionLookup.setColumnFilterable(true);
    colPositionLookup.setColumnName("locationDescriptionSYS10");
    colPositionLookup.setColumnSortable(true);
    colPositionLookup.setEditableOnEdit(true);
    colPositionLookup.setEditableOnInsert(true);
    colPositionLookup.setHeaderColumnName("locationDescriptionSYS10");
    colPositionLookup.setPreferredWidth(200);
    colPositionLookup.setEnableCodBox(false);
    detailPanel.setLayout(gridBagLayout2);
    detailPanel.setBorder(titledBorder2);
    labelPurOrderNr.setText("docSequenceDoc06DOC09");
    labelDocYear.setText("docYearDoc06DOC09");
    labelGrid.setText("purchase order rows");
    importButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("import purchase order rows"));
    importButton.setText(ClientSettings.getInstance().getResources().getResource("import purchase order rows"));
    importButton.setEnabled(false);
    importButton.addActionListener(new InDeliveryNoteRowsGridPanel_importButton_actionAdapter(this));
    controlPurOrderNr.setEnabledOnEdit(true);
    controlPurOrderNr.setLinkLabel(labelPurOrderNr);
    controlPurOrderNr.setMaxCharacters(20);
    controlPurOrderNr.setAttributeName("docSequenceDoc06DOC09");
    controlDocYear.setLinkLabel(labelDocYear);
    controlDocYear.setEnabledOnInsert(false);
    controlDocYear.setEnabledOnEdit(false);
    controlDocYear.setAttributeName("docYearDoc06DOC09");
    orderRows.setAutoLoadData(false);
    orderRows.setVisibleStatusPanel(false);
    colPurchaseInQty.setEditableOnEdit(false);
    colPurchaseSupplierInQty.setEditableOnEdit(true);
    this.add(buttonsPanel, BorderLayout.NORTH);
    this.add(splitPane,  BorderLayout.CENTER);
    buttonsPanel.add(insertButton1, null);
    buttonsPanel.add(copyButton1, null);
    buttonsPanel.add(editButton1, null);
    buttonsPanel.add(saveButton1, null);
    buttonsPanel.add(reloadButton1, null);
    buttonsPanel.add(exportButton1, null);
    buttonsPanel.add(deleteButton1, null);
    buttonsPanel.add(navigatorBar1, null);
    splitPane.add(grid, JSplitPane.TOP);
    grid.getColumnContainer().add(colRowNum, null);
    grid.getColumnContainer().add(colYear, null);
    grid.getColumnContainer().add(colDocNumLookup, null);
    grid.getColumnContainer().add(colItemType, null);
    grid.getColumnContainer().add(colItemCodeLookup, null);
    grid.getColumnContainer().add(colSupplierItemCode, null);
    grid.getColumnContainer().add(colItemDescr, null);
    grid.getColumnContainer().add(colQty, null);
    grid.getColumnContainer().add(colUmCode, null);
    grid.getColumnContainer().add(colInQty, null);
    splitPane.add(detailPanel, JSplitPane.BOTTOM);
    detailPanel.add(labelPurOrderNr,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    detailPanel.add(controlPurOrderNr,   new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 70, 0));
    detailPanel.add(labelDocYear, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    detailPanel.add(controlDocYear,   new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 70, 0));
    detailPanel.add(labelGrid,  new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    detailPanel.add(orderRows,  new GridBagConstraints(0, 2, 4, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    detailPanel.add(importButton,   new GridBagConstraints(0, 3, 4, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

    splitPane.setDividerLocation(220);
    grid.getColumnContainer().add(colSupplierUmCode, null);
    grid.getColumnContainer().add(colPositionLookup, null);

    colPurchaseItemCode.setColumnFilterable(false);
    colPurchaseItemCode.setColumnName("itemCodeItm01DOC09");
    colPurchaseItemCode.setColumnSortable(false);
    colPurchaseItemCode.setPreferredWidth(80);
    colPurchaseSupplierItemCode.setColumnName("supplierItemCodePur02DOC09");
    colPurchaseSupplierItemCode.setColumnSortable(false);
    colPurchaseSupplierItemCode.setHeaderColumnName("supplierItemCode");
    colPurchaseSupplierItemCode.setPreferredWidth(90);
    colPurchaseItemDescr.setColumnName("descriptionSYS10");
    colPurchaseItemDescr.setColumnSortable(false);
    colPurchaseItemDescr.setHeaderColumnName("itemDescriptionSYS10");
    colPurchaseItemDescr.setPreferredWidth(200);
    colPurchaseQty.setDecimals(5);
    colPurchaseQty.setGrouping(false);
    colPurchaseQty.setColumnName("qtyDOC07");
    colPurchaseQty.setColumnSortable(false);
    colPurchaseQty.setPreferredWidth(40);
    colPurchaseInQty.setDecimals(5);
    colPurchaseInQty.setGrouping(false);
    colPurchaseInQty.setColumnName("inQtyDOC07");
    colPurchaseInQty.setColumnSortable(false);
    colPurchaseInQty.setPreferredWidth(40);
    colPurchaseSupplierInQty.setDecimals(5);
    colPurchaseSupplierInQty.setGrouping(false);
    colPurchaseSupplierInQty.setColumnName("supplierQtyDOC09");
    colPurchaseSupplierInQty.setColumnSortable(false);
    colPurchaseSupplierInQty.setPreferredWidth(90);

    colPurchasePositionLookup.setColumnDuplicable(true);
    colPurchasePositionLookup.setColumnFilterable(true);
    colPurchasePositionLookup.setColumnName("locationDescriptionSYS10");
    colPurchasePositionLookup.setColumnSortable(true);
    colPurchasePositionLookup.setEditableOnEdit(true);
    colPurchasePositionLookup.setHeaderColumnName("locationDescriptionSYS10");
    colPurchasePositionLookup.setPreferredWidth(200);
    colPurchasePositionLookup.setEnableCodBox(false);

    orderRows.setValueObjectClassName("org.jallinone.warehouse.documents.java.GridInDeliveryNoteRowVO");
    orderRows.getColumnContainer().add(colPurchaseItemCode, null);
    orderRows.getColumnContainer().add(colPurchaseSupplierItemCode, null);
    orderRows.getColumnContainer().add(colPurchaseItemDescr, null);
    orderRows.getColumnContainer().add(colPurchaseQty, null);
    orderRows.getColumnContainer().add(colPurchaseInQty, null);
    orderRows.getColumnContainer().add(colPurchaseSupplierInQty, null);
    orderRows.getColumnContainer().add(colPurchasePositionLookup, null);
  }


  public void setButtonsEnabled(boolean enabled) {
    if (enabled) {
      insertButton1.setEnabled(enabled);
      reloadButton1.setEnabled(enabled);
      copyButton1.setEnabled(enabled);
    }
    else {
      copyButton1.setEnabled(enabled);
      insertButton1.setEnabled(enabled);
      editButton1.setEnabled(enabled);
      saveButton1.setEnabled(enabled);
      deleteButton1.setEnabled(enabled);
      exportButton1.setEnabled(enabled);
      reloadButton1.setEnabled(enabled);
      importButton.setEnabled(enabled);
    }
  }


  public void setParentVO(DetailDeliveryNoteVO parentVO) {
    this.parentVO = parentVO;

    itemDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,parentVO.getCompanyCodeSys01DOC08());
    itemDataLocator.getLookupFrameParams().put(ApplicationConsts.PROGRESSIVE_REG04,parentVO.getProgressiveReg04DOC08());
    itemDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,parentVO.getCompanyCodeSys01DOC08());
    itemDataLocator.getLookupValidationParameters().put(ApplicationConsts.PROGRESSIVE_REG04,parentVO.getProgressiveReg04DOC08());

    docRefDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,parentVO.getCompanyCodeSys01DOC08());
    docRefDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,parentVO.getCompanyCodeSys01DOC08());
    docRefDataLocator.getLookupFrameParams().put(ApplicationConsts.PROGRESSIVE_REG04,parentVO.getProgressiveReg04DOC08());
    docRefDataLocator.getLookupValidationParameters().put(ApplicationConsts.PROGRESSIVE_REG04,parentVO.getProgressiveReg04DOC08());

    purOrderDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,parentVO.getCompanyCodeSys01DOC08());
    purOrderDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,parentVO.getCompanyCodeSys01DOC08());
    purOrderDataLocator.getLookupFrameParams().put(ApplicationConsts.PROGRESSIVE_REG04,parentVO.getProgressiveReg04DOC08());
    purOrderDataLocator.getLookupValidationParameters().put(ApplicationConsts.PROGRESSIVE_REG04,parentVO.getProgressiveReg04DOC08());
    purOrderDataLocator.getLookupFrameParams().put(ApplicationConsts.WAREHOUSE_CODE,parentVO.getWarehouseCodeWar01DOC08());
    purOrderDataLocator.getLookupValidationParameters().put(ApplicationConsts.WAREHOUSE_CODE,parentVO.getWarehouseCodeWar01DOC08());

    docRefDataLocator.getLookupValidationParameters().put(ApplicationConsts.DOC_YEAR,parentVO.getDocYearDOC08());
    purOrderDataLocator.getLookupValidationParameters().put(ApplicationConsts.DOC_YEAR,parentVO.getDocYearDOC08());

    docRefDataLocator.getLookupFrameParams().put(ApplicationConsts.WAREHOUSE_CODE,parentVO.getWarehouseCodeWar01DOC08());
    docRefDataLocator.getLookupValidationParameters().put(ApplicationConsts.WAREHOUSE_CODE,parentVO.getWarehouseCodeWar01DOC08());


    setButtonsEnabled(true);

    if (parentVO.getDocStateDOC08().equals(ApplicationConsts.CLOSED))
      controlPurOrderNr.setEnabled(false);
    else
      controlPurOrderNr.setEnabled(true);
  }


  public GridControl getGrid() {
    return grid;
  }


  public DetailDeliveryNoteVO getParentVO() {
    return parentVO;
  }


  public Form getHeaderPanel() {
    return headerPanel;
  }


  public GridControl getOrders() {
    return frame.getOrders();
  }
  public InDeliveryNoteFrame getFrame() {
    return frame;
  }


  /**
   * <p>Description: Inner class used to define qty column settings for each grid row.</p>
   * @author Mauro Carniel
   * @version 1.0
   */
  class QtyColumnDynamicSettings implements DecimalColumnSettings {

    public double getMaxValue(int row) {
      return colQty.getMaxValue();
    }

    public double getMinValue(int row) {
      return colQty.getMinValue();
    }

    public boolean isGrouping(int row) {
      return colQty.isGrouping();
    }

    public int getDecimals(int row) {
      GridInDeliveryNoteRowVO vo = (GridInDeliveryNoteRowVO)grid.getVOListTableModel().getObjectForRow(row);
      return vo.getDecimalsREG02().intValue();
    }
  }


  /**
   * <p>Description: Inner class used to define (purchase um) qty column settings for each grid row.</p>
   * @author Mauro Carniel
   * @version 1.0
   */
  class SupplierQtyColumnDynamicSettings implements DecimalColumnSettings {

    public double getMaxValue(int row) {
      return colInQty.getMaxValue();
    }

    public double getMinValue(int row) {
      return colInQty.getMinValue();
    }

    public boolean isGrouping(int row) {
      return colInQty.isGrouping();
    }

    public int getDecimals(int row) {
      GridInDeliveryNoteRowVO vo = (GridInDeliveryNoteRowVO)grid.getVOListTableModel().getObjectForRow(row);
      return vo.getSupplierQtyDecimalsREG02().intValue();
    }
  }


  void importButton_actionPerformed(ActionEvent e) {
    orderRows.setMode(Consts.READONLY);

    GridInDeliveryNoteRowVO rowVO = null;
    Response res = null;
    ArrayList list = new ArrayList();
    for(int i=0;i<orderRows.getVOListTableModel().getRowCount();i++) {
      // scroll over the order rows and insert specified item quantities...
      rowVO = (GridInDeliveryNoteRowVO)orderRows.getVOListTableModel().getObjectForRow(i);
      if (rowVO.getSupplierQtyDOC09()!=null &&
          rowVO.getSupplierQtyDOC09().doubleValue()>0 &&
          rowVO.getProgressiveHie01DOC09()!=null) {
        try {
          rowVO.setWarehouseCodeWar01DOC08(parentVO.getWarehouseCodeWar01DOC08());
          rowVO.setDocNumberDOC09(parentVO.getDocNumberDOC08());
          rowVO.setDocTypeDOC09(parentVO.getDocTypeDOC08());
          rowVO.setDocYearDOC09(parentVO.getDocYearDOC08());
          if (rowVO.getValueREG05()!=null)
            rowVO.setQtyDOC09(rowVO.getSupplierQtyDOC09().divide(rowVO.getValueREG05(),rowVO.getDecimalsREG02().intValue(),BigDecimal.ROUND_HALF_UP));

          serialNumberRequired = rowVO.getSerialNumberRequiredITM01().booleanValue();

          list.clear();
          list.add(rowVO);
          res = gridController.insertRecords(new int[]{0}, list);
          if (res.isError()) {
            JOptionPane.showMessageDialog(
                ClientUtils.getParentFrame(this),
                res.getErrorMessage(),
                ClientSettings.getInstance().getResources().getResource("Error"),
                JOptionPane.ERROR_MESSAGE
            );
            break;
          }
          else {
            getFrame().enabledConfirmButton();
            frame.getHeaderFormPanel().reload();
          }
        }
        catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    }

    grid.reloadData();
    orderRows.clearData();
    orderRows.revalidate();
    orderRows.repaint();
    importButton.setEnabled(false);
    rowVO = (GridInDeliveryNoteRowVO)detailPanel.getVOModel().getValueObject();
    rowVO.setDocYearDoc06DOC09(null);
    rowVO.setDocNumberDoc06DOC09(null);
    detailPanel.pull();

  }


  /**
   * Method called by item row saving algorithm to prompt the list of serial numbers (if required).
   */
  public final boolean isSerialNumberRequired() {
    return serialNumberRequired;
  }


}

class InDeliveryNoteRowsGridPanel_importButton_actionAdapter implements java.awt.event.ActionListener {
  InDeliveryNoteRowsGridPanel adaptee;

  InDeliveryNoteRowsGridPanel_importButton_actionAdapter(InDeliveryNoteRowsGridPanel adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.importButton_actionPerformed(e);
  }
}

