package org.jallinone.production.orders.client;

import org.openswing.swing.table.client.Grid;
import org.openswing.swing.message.receive.java.*;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.production.orders.java.*;
import org.openswing.swing.mdi.client.MDIFrame;
import org.jallinone.commons.client.CompanyFormController;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.form.client.Form;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.mdi.client.InternalFrame;
import java.awt.*;
import javax.swing.*;
import org.openswing.swing.client.*;
import org.jallinone.commons.client.CustomizedControls;
import org.openswing.swing.util.client.ClientSettings;
import java.util.HashSet;
import java.awt.event.*;
import java.util.HashMap;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JRViewer;
import org.openswing.swing.table.java.ServerGridDataLocator;
import org.jallinone.commons.client.CompanyGridController;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.openswing.swing.lookup.client.LookupListener;
import java.util.Collection;
import org.jallinone.commons.client.CompaniesComboControl;
import org.openswing.swing.table.columns.client.CodLookupColumn;
import org.openswing.swing.table.columns.client.IntegerColumn;
import org.openswing.swing.table.columns.client.TextColumn;
import org.openswing.swing.table.columns.client.DecimalColumn;
import org.openswing.swing.table.columns.client.ComboColumn;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.items.java.ItemTypeVO;
import org.openswing.swing.domains.java.Domain;
import org.openswing.swing.tree.client.TreeServerDataLocator;
import org.jallinone.warehouse.java.WarehouseVO;
import org.jallinone.hierarchies.java.HierarchyLevelVO;
import org.jallinone.registers.currency.java.CurrencyVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Detail Frame used for a production order.</p>
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
public class ProdOrderFrame extends InternalFrame  {

  /** detail form controller */
  private ProdOrderController controller = null;
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  JSplitPane split = new JSplitPane();
  JPanel headerPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  JPanel headerButtonsPanel = new JPanel();
  BorderLayout borderLayout3 = new BorderLayout();
  JPanel productsPanel = new JPanel();
  BorderLayout borderLayout4 = new BorderLayout();
  Form headerFormPanel = new Form();
  InsertButton insertButton1 = new InsertButton();
  EditButton editButton1 = new EditButton();
  SaveButton saveButton1 = new SaveButton();
  ReloadButton reloadButton1 = new ReloadButton();
  DeleteButton deleteButton1 = new DeleteButton();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  GenericButton confirmButton = new GenericButton(new ImageIcon(ClientUtils.getImage("workflow.gif")));
  GenericButton printButton = new GenericButton(new ImageIcon(ClientUtils.getImage("printer.gif")));
  GridControl productsGrid = new GridControl();
  GridControl compsGrid = new GridControl();

  InsertButton insertButton2 = new InsertButton();
  EditButton editButton2 = new EditButton();
  SaveButton saveButton2 = new SaveButton();
  ReloadButton reloadButton2 = new ReloadButton();
  DeleteButton deleteButton2 = new DeleteButton();
  ExportButton exportButton2 = new ExportButton();

  JTabbedPane tab = new JTabbedPane();

  JPanel buttons2Panel = new JPanel();
  FlowLayout flowLayout2 = new FlowLayout();

  private ServerGridDataLocator productsGridDataLocator = new ServerGridDataLocator();
  private ServerGridDataLocator compsGridDataLocator = new ServerGridDataLocator();

  LabelControl labelCompany = new LabelControl();
  LabelControl labelNote = new LabelControl();
  LabelControl labelDocNum = new LabelControl();
  NumericControl controlDocNumber = new NumericControl();
  LabelControl labelDocYear = new LabelControl();
  NumericControl controlDocYear = new NumericControl();
  LabelControl labelDocDate = new LabelControl();
  DateControl controlDocDate = new DateControl();
  LabelControl labelDocState = new LabelControl();
  ComboBoxControl controlDocState = new ComboBoxControl();
  LabelControl labelWarehouseCode = new LabelControl();
  CodLookupControl controlWarehouseCode = new CodLookupControl();
  LabelControl labelWarehouse2Code = new LabelControl();
  CodLookupControl controlWarehouse2Code = new CodLookupControl();
  TextControl controlDescr = new TextControl();
  TextControl control2Descr = new TextControl();
  LookupController wareController = new LookupController();
  LookupServerDataLocator wareDataLocator = new LookupServerDataLocator();
  LookupController ware2Controller = new LookupController();
  LookupServerDataLocator ware2DataLocator = new LookupServerDataLocator();
  CompaniesComboControl controlCompany = new CompaniesComboControl();
  TextAreaControl controlNote = new TextAreaControl();

  CodLookupColumn colItemCode = new CodLookupColumn();
  TextColumn colItemDescr = new TextColumn();
  CodLookupColumn colLocation = new CodLookupColumn();
  DecimalColumn colQty = new DecimalColumn();

  TextColumn colCompItemCode = new TextColumn();
  TextColumn colCompItemDescr = new TextColumn();
  DecimalColumn colCompQty = new DecimalColumn();
  DecimalColumn colCompAvail = new DecimalColumn();
  ComboColumn colItemType = new ComboColumn();

  LookupController itemController = new LookupController();
  LookupServerDataLocator itemDataLocator = new LookupServerDataLocator();

  LookupController levelController = new LookupController();
  LookupServerDataLocator levelDataLocator = new LookupServerDataLocator();
  TreeServerDataLocator treeLevelDataLocator = new TreeServerDataLocator();

  /** warehouse position lookup controller */
  LookupController posController = new LookupController();
  LookupServerDataLocator posDataLocator = new LookupServerDataLocator();
  TreeServerDataLocator treeLevelPosDataLocator = new TreeServerDataLocator();

  TextColumn colLocationDescr = new TextColumn();
  TextColumn colMU = new TextColumn();
  DecimalColumn colProgressiveHie01 = new DecimalColumn();


  public ProdOrderFrame(ProdOrderController controller) {
    this.controller = controller;
    try {
      jbInit();
      setSize(750,610);
      setMinimumSize(new Dimension(750,610));
      setTitle(ClientSettings.getInstance().getResources().getResource("production order"));

      CustomizedControls customizedControls = new CustomizedControls(tab,headerFormPanel,ApplicationConsts.ID_PROD_ORDER);

      init();

      productsGrid.setController(new ProdOrderProductsController(this));
      productsGrid.setGridDataLocator(productsGridDataLocator);
      productsGridDataLocator.setServerMethodName("loadProdOrderProducts");

      compsGrid.setController(new CompanyGridController(){

        /**
         * Method used to define the background color for each cell of the grid.
         * @param rowNumber selected row index
         * @param attributedName attribute name related to the column currently selected
         * @param value object contained in the selected cell
         * @return background color of the selected cell
         */
        public Color getBackgroundColor(int row,String attributedName,Object value) {
          ProdOrderComponentVO vo = (ProdOrderComponentVO)compsGrid.getVOListTableModel().getObjectForRow(row);
          if (attributedName.equals("availableQty") && vo.getAvailableQty()!=null) {
            if (vo.getAvailableQty().doubleValue()<vo.getQtyDOC24().doubleValue())
              return new Color(200,100,100);
          }
          return super.getBackgroundColor(row,attributedName,value);
        }

      });
      compsGrid.setGridDataLocator(compsGridDataLocator);
      compsGridDataLocator.setServerMethodName("loadProdOrderComponents");


      // warehouse position code lookup...
      colLocation.setLookupController(posController);
      posController.setLookupDataLocator(posDataLocator);
      posController.setFrameTitle("warehouse positions");

      posController.setCodeSelectionWindow(posController.TREE_FRAME);
      treeLevelPosDataLocator.setServerMethodName("loadHierarchy");
      posDataLocator.setTreeDataLocator(treeLevelPosDataLocator);
      posDataLocator.setNodeNameAttribute("descriptionSYS10");

      posController.setLookupValueObjectClassName("org.jallinone.hierarchies.java.HierarchyLevelVO");
      posController.addLookup2ParentLink("progressiveHIE01", "progressiveHie01DOC23");
      posController.addLookup2ParentLink("descriptionSYS10","locationDescriptionSYS10");
      posController.setFramePreferedSize(new Dimension(400,400));
      posController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
        	HierarchyLevelVO lookupVO = (HierarchyLevelVO)posController.getLookupVO();
        	if (lookupVO!=null && "".equals(lookupVO.getDescriptionSYS10()))
        		((ProdOrderProductVO)parentVO).setLocationDescriptionSYS10(" ");
        }

        public void beforeLookupAction(ValueObject parentVO) {
          DetailProdOrderVO vo = (DetailProdOrderVO)headerFormPanel.getVOModel().getValueObject();
          treeLevelPosDataLocator.getTreeNodeParams().put(ApplicationConsts.PROGRESSIVE_HIE02,vo.getProgressiveHie02WAR01());

        }

        public void forceValidate() {}

      });


      // warehouse lookup...
      wareDataLocator.setGridMethodName("loadWarehouses");
      wareDataLocator.setValidationMethodName("validateWarehouseCode");

      controlWarehouseCode.setLookupController(wareController);
      controlWarehouseCode.setControllerMethodName("getWarehousesList");
      wareController.setLookupDataLocator(wareDataLocator);
      wareController.setFrameTitle("warehouses");
      wareController.setLookupValueObjectClassName("org.jallinone.warehouse.java.WarehouseVO");
      wareController.addLookup2ParentLink("companyCodeSys01WAR01", "companyCodeSys01DOC22");
      wareController.addLookup2ParentLink("warehouseCodeWAR01", "warehouseCodeWar01DOC22");
      wareController.addLookup2ParentLink("descriptionWAR01","descriptionWar01DOC22");
      wareController.setAllColumnVisible(false);
      wareController.setVisibleColumn("warehouseCodeWAR01", true);
      wareController.setVisibleColumn("descriptionWAR01", true);
      wareController.setVisibleColumn("addressWAR01", true);
      wareController.setVisibleColumn("cityWAR01", true);
      wareController.setVisibleColumn("zipWAR01", true);
      wareController.setVisibleColumn("provinceWAR01", true);
      wareController.setVisibleColumn("countryWAR01", true);
      wareController.setPreferredWidthColumn("descriptionWAR01",200);
      wareController.setFramePreferedSize(new Dimension(750,500));


      // destination warehouse lookup...
      ware2DataLocator.setGridMethodName("loadWarehouses");
      ware2DataLocator.setValidationMethodName("validateWarehouseCode");

      controlWarehouse2Code.setLookupController(ware2Controller);
      controlWarehouse2Code.setControllerMethodName("getWarehousesList");
      ware2Controller.setLookupDataLocator(ware2DataLocator);
      ware2Controller.setFrameTitle("warehouses");
      ware2Controller.setLookupValueObjectClassName("org.jallinone.warehouse.java.WarehouseVO");
      ware2Controller.addLookup2ParentLink("warehouseCodeWAR01", "warehouseCode2War01DOC22");
      ware2Controller.addLookup2ParentLink("descriptionWAR01","description2War01DOC22");
      ware2Controller.addLookup2ParentLink("progressiveHie02WAR01","progressiveHie02WAR01");
      ware2Controller.setAllColumnVisible(false);
      ware2Controller.setVisibleColumn("warehouseCodeWAR01", true);
      ware2Controller.setVisibleColumn("descriptionWAR01", true);
      ware2Controller.setVisibleColumn("addressWAR01", true);
      ware2Controller.setVisibleColumn("cityWAR01", true);
      ware2Controller.setVisibleColumn("zipWAR01", true);
      ware2Controller.setVisibleColumn("provinceWAR01", true);
      ware2Controller.setVisibleColumn("countryWAR01", true);
      ware2Controller.setPreferredWidthColumn("descriptionWAR01",200);
      ware2Controller.setFramePreferedSize(new Dimension(750,500));
      ware2Controller.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          WarehouseVO lookupVO = (WarehouseVO)ware2Controller.getLookupVO();
          treeLevelPosDataLocator.getTreeNodeParams().put(ApplicationConsts.PROGRESSIVE_HIE02,lookupVO.getProgressiveHie02WAR01());

        }

        public void beforeLookupAction(ValueObject parentVO) {
          DetailProdOrderVO vo = (DetailProdOrderVO)headerFormPanel.getVOModel().getValueObject();
          ware2DataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01DOC22());
          ware2DataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01DOC22());
        }

        public void forceValidate() {}

      });


      // item code lookup...
      itemDataLocator.setGridMethodName("loadItems");
      itemDataLocator.setValidationMethodName("validateItemCode");
      itemDataLocator.getLookupFrameParams().put(ApplicationConsts.PRODUCTS_ONLY,Boolean.TRUE);
      itemDataLocator.getLookupValidationParameters().put(ApplicationConsts.PRODUCTS_ONLY,Boolean.TRUE);

      colItemCode.setLookupController(itemController);
      colItemCode.setControllerMethodName("getItemsList");
      itemController.setLookupDataLocator(itemDataLocator);
      itemController.setFrameTitle("products");

      itemController.setCodeSelectionWindow(itemController.TREE_GRID_FRAME);
      treeLevelDataLocator.setServerMethodName("loadHierarchy");
      itemDataLocator.setTreeDataLocator(treeLevelDataLocator);
      itemDataLocator.setNodeNameAttribute("descriptionSYS10");

      itemController.setLookupValueObjectClassName("org.jallinone.items.java.GridItemVO");
      itemController.addLookup2ParentLink("itemCodeITM01", "itemCodeItm01DOC23");
      itemController.addLookup2ParentLink("descriptionSYS10", "descriptionSYS10");

      itemController.setAllColumnVisible(false);
      itemController.setVisibleColumn("itemCodeITM01", true);
      itemController.setVisibleColumn("descriptionSYS10", true);
      itemController.setPreferredWidthColumn("descriptionSYS10", 200);
      itemController.setFramePreferedSize(new Dimension(550,500));
      itemController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {}

        public void beforeLookupAction(ValueObject parentVO) {
          DetailProdOrderVO vo = (DetailProdOrderVO)headerFormPanel.getVOModel().getValueObject();
          itemDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01DOC22());
          itemDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01DOC22());

          ProdOrderProductVO prodVO = (ProdOrderProductVO)productsGrid.getVOListTableModel().getObjectForRow(productsGrid.getSelectedRow());
          treeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.PROGRESSIVE_HIE02, prodVO.getProgressiveHie02DOC23());

        }

        public void forceValidate() {}

      });

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * Define input controls editable settings according to the document state.
   */
  private void init() {
    // disable warehouses when at least one product is added...
    HashSet attributeNameToDisable = new HashSet();
    attributeNameToDisable.add("warehouseCodeWar01DOC22");
    attributeNameToDisable.add("warehouseCode2War01DOC22");
    attributeNameToDisable.add("docDateDOC22");
    headerFormPanel.addInputControlAttributesNotEditableOnState(attributeNameToDisable,"docStateDOC22",ApplicationConsts.HEADER_BLOCKED);

    // disable all editable fields when the order is confimed or closed)...
    attributeNameToDisable = new HashSet();
    attributeNameToDisable.add("warehouseCodeWar01DOC22");
    attributeNameToDisable.add("warehouseCode2War01DOC22");
    attributeNameToDisable.add("docDateDOC22");
    attributeNameToDisable.add("noteDOC22");
    headerFormPanel.addInputControlAttributesNotEditableOnState(attributeNameToDisable,"docStateDOC22",ApplicationConsts.CONFIRMED);
    headerFormPanel.addInputControlAttributesNotEditableOnState(attributeNameToDisable,"docStateDOC22",ApplicationConsts.CLOSED);

    // disable buttons according to order state...
    HashSet buttonsToDisable = new HashSet();
    buttonsToDisable.add(editButton1);
    buttonsToDisable.add(deleteButton1);
    headerFormPanel.addButtonsNotEnabled(buttonsToDisable,new GenericButtonController() {

       public boolean isButtonDisabled(GenericButton button) {
         DetailProdOrderVO vo = null;
         if (headerFormPanel!=null && headerFormPanel.getVOModel()!=null)
           vo = (DetailProdOrderVO)headerFormPanel.getVOModel().getValueObject();
         if (vo!=null && vo.getDocStateDOC22()!=null && vo.getDocStateDOC22().equals(ApplicationConsts.CONFIRMED))
           return true;
         else
           return false;
       }

    });


    buttonsToDisable = new HashSet();
    buttonsToDisable.add(editButton1);
    headerFormPanel.addButtonsNotEnabled(buttonsToDisable,new GenericButtonController() {

       public boolean isButtonDisabled(GenericButton button) {
         DetailProdOrderVO vo = null;
         if (headerFormPanel!=null && headerFormPanel.getVOModel()!=null)
           vo = (DetailProdOrderVO)headerFormPanel.getVOModel().getValueObject();
         if (vo!=null && vo.getDocStateDOC22()!=null && vo.getDocStateDOC22().equals(ApplicationConsts.CLOSED))
           return true;
         else
           return false;
       }

    });


    // (product) item type...
    Domain d = new Domain("ITEM_TYPES");
    Response res = ClientUtils.getData("loadItemTypes",new GridParams());
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
        if (e.getStateChange() == e.SELECTED) {
          ProdOrderProductVO vo = (ProdOrderProductVO)productsGrid.getVOListTableModel().getObjectForRow(productsGrid.getSelectedRow());
//          if (formPanel.getMode()==Consts.EDIT || formPanel.getMode()==Consts.INSERT) {
//            controlLevel.getCodBox().setText(null);
//            controlLevelDescr.setText("");
//          }
          treeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.PROGRESSIVE_HIE02, vo.getProgressiveHie02DOC23());
        }
      }
    });
  }


  private void jbInit() throws Exception {
    labelCompany.setText("company");
    labelNote.setText("note");
    split.setOrientation(JSplitPane.VERTICAL_SPLIT);
    split.setDividerLocation(200);
    controlCompany.setAttributeName("companyCodeSys01DOC22");
    controlCompany.setFunctionCode("DOC22");
    controlNote.setAttributeName("noteDOC22");

    labelWarehouseCode.setText("descriptionWar01DOC22");
    labelWarehouse2Code.setText("description2War01DOC22");

    printButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("print document"));
    printButton.addActionListener(new ProdOrderFrame_printButton_actionAdapter(this));

    headerFormPanel.setFunctionId("DOC22");
    headerFormPanel.setFormController(controller);
    headerFormPanel.setVOClassName("org.jallinone.production.orders.java.DetailProdOrderVO");

    headerPanel.setLayout(borderLayout3);
    headerButtonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    mainPanel.setLayout(borderLayout2);
    this.getContentPane().setLayout(borderLayout1);
    productsPanel.setLayout(borderLayout4);
    insertButton1.setText("insertButton1");
    reloadButton1.setText("reloadButton1");
    deleteButton1.setText("deleteButton1");
    headerFormPanel.setLayout(gridBagLayout1);
    headerFormPanel.setInsertButton(insertButton1);
    headerFormPanel.setEditButton(editButton1);
    headerFormPanel.setReloadButton(reloadButton1);
    headerFormPanel.setDeleteButton(deleteButton1);
    headerFormPanel.setSaveButton(saveButton1);
    confirmButton.addActionListener(new ProdOrderFrame_confirmButton_actionAdapter(this));
    this.getContentPane().add(mainPanel, BorderLayout.CENTER);
    mainPanel.add(headerPanel,  BorderLayout.NORTH);
    mainPanel.add(split,  BorderLayout.CENTER);
    headerPanel.add(headerButtonsPanel,  BorderLayout.NORTH);
    headerButtonsPanel.add(insertButton1, null);
    headerButtonsPanel.add(editButton1, null);
    headerButtonsPanel.add(saveButton1, null);
    headerButtonsPanel.add(reloadButton1, null);
    headerButtonsPanel.add(deleteButton1, null);
    headerButtonsPanel.add(confirmButton, null);
    printButton.setEnabled(false);
    headerButtonsPanel.add(printButton, null);
    split.add(productsPanel, JSplitPane.TOP);
    productsGrid.setVisibleStatusPanel(false);
    compsGrid.setVisibleStatusPanel(false);
    split.add(compsGrid, JSplitPane.BOTTOM);
    productsPanel.add(buttons2Panel, BorderLayout.NORTH);
    productsPanel.add(productsGrid, BorderLayout.CENTER);
    tab.add(headerFormPanel,"headerFormPanel");
    headerPanel.add(tab,  BorderLayout.CENTER);

    tab.setTitleAt(0,ClientSettings.getInstance().getResources().getResource("headerFormPanel"));

    labelDocYear.setText("docYear");
    labelDocDate.setText("docDate");
    labelDocState.setText("docState");
    labelDocNum.setText("docNumber");
    controlDocNumber.setLinkLabel(labelDocNum);
    controlDocNumber.setMaxCharacters(255);
    controlDocNumber.setRequired(false);
    controlDocNumber.setEnabledOnInsert(false);
    controlDocNumber.setEnabledOnEdit(false);
    controlDocNumber.setAttributeName("docSequenceDOC22");
    controlDocYear.setLinkLabel(labelDocYear);
    controlDocYear.setEnabledOnInsert(false);
    controlDocYear.setEnabledOnEdit(false);
    controlDocYear.setAttributeName("docYearDOC22");
    controlDocDate.setCanCopy(true);
    controlDocDate.setLinkLabel(labelDocDate);
    controlDocDate.setRequired(true);
    controlDocDate.setAttributeName("docDateDOC22");
    controlDocState.setCanCopy(false);
    controlDocState.setLinkLabel(labelDocDate);
    controlDocState.setRequired(true);
    controlDocState.setEnabledOnInsert(false);
    controlDocState.setEnabledOnEdit(false);
    controlDocState.setAttributeName("docStateDOC22");
    controlDocState.setDomainId("DOC22_STATES");

    controlCompany.setLinkLabel(labelCompany);
    headerFormPanel.add(labelCompany,       new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    headerFormPanel.add(controlCompany,       new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    headerFormPanel.add(labelDocNum,       new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    headerFormPanel.add(controlDocNumber,              new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 30, 0));
    headerFormPanel.add(controlDocYear,          new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 30, 0));
    headerFormPanel.add(labelDocYear,     new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    headerFormPanel.add(labelDocDate,    new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    headerFormPanel.add(controlDocDate,        new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 60, 0));
    headerFormPanel.add(labelDocState,   new GridBagConstraints(6, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    headerFormPanel.add(controlDocState,      new GridBagConstraints(7, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 10, 0));


    controlWarehouseCode.setAttributeName("warehouseCodeWar01DOC22");
    controlWarehouseCode.setCanCopy(true);
    controlWarehouseCode.setLinkLabel(labelWarehouseCode);
    controlWarehouseCode.setMaxCharacters(20);
    controlWarehouseCode.setRequired(true);
    controlDescr.setCanCopy(true);
    controlDescr.setEnabledOnInsert(false);
    controlDescr.setEnabledOnEdit(false);
    controlDescr.setAttributeName("descriptionWar01DOC22");

    controlWarehouse2Code.setAttributeName("warehouseCode2War01DOC22");
    controlWarehouse2Code.setCanCopy(true);
    controlWarehouse2Code.setLinkLabel(labelWarehouse2Code);
    controlWarehouse2Code.setMaxCharacters(20);
    controlWarehouse2Code.setRequired(true);
    control2Descr.setCanCopy(true);
    control2Descr.setEnabledOnInsert(false);
    control2Descr.setEnabledOnEdit(false);
    control2Descr.setAttributeName("description2War01DOC22");

    headerFormPanel.add(labelWarehouseCode,     new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    headerFormPanel.add(controlWarehouseCode,     new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
    headerFormPanel.add(controlDescr,  new GridBagConstraints(2, 2, 6, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

    headerFormPanel.add(labelWarehouse2Code,     new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
        ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    headerFormPanel.add(controlWarehouse2Code,     new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
    headerFormPanel.add(control2Descr,  new GridBagConstraints(2, 3, 6, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));


    headerFormPanel.add(labelNote,  new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    headerFormPanel.add(controlNote,  new GridBagConstraints(1, 4, 7, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 50));

    confirmButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("order confirmation"));
    confirmButton.setEnabled(false);

    productsGrid.setValueObjectClassName("org.jallinone.production.orders.java.ProdOrderProductVO");
    productsGrid.setMaxNumberOfRowsOnInsert(50);
    productsGrid.setAutoLoadData(false);
    productsGrid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    colItemType.setColumnFilterable(true);
    colItemType.setColumnName("progressiveHie02DOC23");
    colItemType.setEditableOnEdit(false);
    colItemType.setEditableOnInsert(true);
    colItemType.setColumnDuplicable(true);
    colItemType.setColumnRequired(true);
    colItemType.setColumnSortable(true);

    colItemCode.setColumnFilterable(true);
    colItemCode.setColumnName("itemCodeItm01DOC23");
    colItemCode.setEditableOnEdit(false);
    colItemCode.setEditableOnInsert(true);
    colItemCode.setColumnDuplicable(true);
    colItemCode.setColumnRequired(true);
    colItemCode.setColumnSortable(true);
    colItemCode.setSortingOrder(1);
    colItemCode.setSortVersus(Consts.ASC_SORTED);

    colItemDescr.setColumnFilterable(true);
    colItemDescr.setPreferredWidth(250);
    colItemDescr.setColumnName("descriptionSYS10");
    colItemDescr.setEditableOnEdit(false);
    colItemDescr.setEditableOnInsert(false);
    colItemDescr.setColumnDuplicable(true);
    colItemDescr.setColumnSortable(true);

    colProgressiveHie01.setColumnName("progressiveHie01DOC23");
    colProgressiveHie01.setColumnVisible(false);
    colProgressiveHie01.setColumnSelectable(false);
    
    colQty.setColumnFilterable(true);
    colQty.setPreferredWidth(70);
    colQty.setColumnName("qtyDOC23");
    colQty.setEditableOnEdit(true);
    colQty.setEditableOnInsert(true);
    colQty.setColumnDuplicable(true);
    colQty.setColumnRequired(true);
    colQty.setColumnSortable(true);

    colLocation.setColumnName("locationDescriptionSYS10");
    colLocation.setColumnDuplicable(true);
    colLocation.setColumnRequired(true);
    colLocation.setEnableCodBox(false);
    colLocation.setEditableOnEdit(true);
    colLocation.setPreferredWidth(190);
    colLocation.setEditableOnInsert(true);

    productsGrid.getColumnContainer().add(colItemType,null);
    productsGrid.getColumnContainer().add(colItemCode,null);
    productsGrid.getColumnContainer().add(colItemDescr,null);
    productsGrid.getColumnContainer().add(colQty,null);
    productsGrid.getColumnContainer().add(colLocation,null);
    productsGrid.getColumnContainer().add(colProgressiveHie01,null);
    
    productsGrid.setFunctionId("DOC22");

    compsGrid.setAutoLoadData(false);
    compsGrid.setValueObjectClassName("org.jallinone.production.orders.java.ProdOrderComponentVO");
    compsGrid.setMaxNumberOfRowsOnInsert(50);
    colCompItemCode.setColumnFilterable(false);
    colCompItemCode.setColumnName("itemCodeItm01DOC24");
    colCompItemCode.setEditableOnEdit(false);
    colCompItemCode.setEditableOnInsert(false);
    colCompItemCode.setColumnSortable(false);

    colCompItemDescr.setColumnFilterable(false);
    colCompItemDescr.setPreferredWidth(250);
    colCompItemDescr.setColumnName("descriptionSYS10");
    colCompItemDescr.setEditableOnEdit(false);
    colCompItemDescr.setEditableOnInsert(false);
    colCompItemDescr.setColumnSortable(false);

    colCompQty.setColumnFilterable(false);
    colCompQty.setColumnName("qtyDOC24");
    colCompQty.setEditableOnEdit(false);
    colCompQty.setEditableOnInsert(false);
    colCompQty.setColumnSortable(false);
    colCompQty.setPreferredWidth(70);

    colCompAvail.setPreferredWidth(70);
    colCompAvail.setColumnFilterable(false);
    colCompAvail.setColumnName("availableQty");
    colCompAvail.setEditableOnEdit(false);
    colCompAvail.setEditableOnInsert(false);
    colCompAvail.setColumnSortable(false);

    colLocationDescr.setColumnFilterable(false);
    colLocationDescr.setColumnName("locationDescriptionSYS10");
    colLocationDescr.setEditableOnEdit(false);
    colLocationDescr.setEditableOnInsert(false);
    colLocationDescr.setColumnSortable(false);
    colLocationDescr.setPreferredWidth(180);

    colMU.setColumnFilterable(false);
    colMU.setColumnName("minSellingQtyUmCodeReg02ITM01");
    colMU.setEditableOnEdit(false);
    colMU.setEditableOnInsert(false);
    colMU.setColumnSortable(false);
    colMU.setPreferredWidth(40);

    compsGrid.setFunctionId("DOC22");
    compsGrid.getColumnContainer().add(colCompItemCode,null);
    compsGrid.getColumnContainer().add(colCompItemDescr,null);
    compsGrid.getColumnContainer().add(colCompQty,null);
    compsGrid.getColumnContainer().add(colMU,null);
    compsGrid.getColumnContainer().add(colCompAvail,null);
    compsGrid.getColumnContainer().add(colLocationDescr,null);

    buttons2Panel.setLayout(flowLayout2);
    flowLayout2.setAlignment(FlowLayout.LEFT);
    buttons2Panel.add(insertButton2, null);
    buttons2Panel.add(editButton2, null);
    buttons2Panel.add(saveButton2, null);
    buttons2Panel.add(reloadButton2, null);
    buttons2Panel.add(deleteButton2, null);
    buttons2Panel.add(exportButton2, null);

    productsGrid.setInsertButton(insertButton2);
    productsGrid.setEditButton(editButton2);
    productsGrid.setReloadButton(reloadButton2);
    productsGrid.setSaveButton(saveButton2);
    productsGrid.setExportButton(exportButton2);
    productsGrid.setDeleteButton(deleteButton2);
  }


  public Form getHeaderFormPanel() {
    return headerFormPanel;
  }


  public void setButtonsEnabled(boolean enabled) {
    if (enabled) {
      insertButton2.setEnabled(enabled);
      editButton2.setEnabled(enabled);
      reloadButton2.setEnabled(enabled);
      deleteButton2.setEnabled(enabled);
      exportButton2.setEnabled(enabled);
    }
    else {
      insertButton2.setEnabled(enabled);
      editButton2.setEnabled(enabled);
      saveButton2.setEnabled(enabled);
      reloadButton2.setEnabled(enabled);
      deleteButton2.setEnabled(enabled);
      exportButton2.setEnabled(enabled);
    }
  }


  public void loadDataCompleted(boolean error,ProdOrderPK pk) {
    if (error)
      return;
    DetailProdOrderVO vo = (DetailProdOrderVO)headerFormPanel.getVOModel().getValueObject();

    productsGrid.getOtherGridParams().put(ApplicationConsts.PROD_ORDER_PK,pk);
    productsGrid.reloadData();
    compsGrid.getOtherGridParams().put(ApplicationConsts.PROD_ORDER_PK,pk);

    if (vo.getDocStateDOC22().equals(ApplicationConsts.CONFIRMED) ||
        vo.getDocStateDOC22().equals(ApplicationConsts.CLOSED)) {
      compsGridDataLocator.setServerMethodName("loadProdOrderComponents");
      compsGrid.reloadData();
      setButtonsEnabled(false);
      if (vo.getDocStateDOC22().equals(ApplicationConsts.CONFIRMED)) {
        confirmButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("order closing"));
        enabledConfirmButton();
      }
    }
    else if (vo.getDocStateDOC22().equals(ApplicationConsts.HEADER_BLOCKED)) {
      confirmButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("order confirmation"));
      enabledConfirmButton();
    }

    if (vo.getDocStateDOC22().equals(ApplicationConsts.CONFIRMED) ||
        vo.getDocStateDOC22().equals(ApplicationConsts.CLOSED)) {
      printButton.setEnabled(true);
    }
    else {
      printButton.setEnabled(false);
    }

  }



  public void enabledConfirmButton() {
    confirmButton.setEnabled(true);
  }


  public GridControl getOrders() {
    return controller.getParentFrame().getGrid();
  }


  void confirmButton_actionPerformed(ActionEvent e) {
    // view confirmation dialog...
    DetailProdOrderVO vo = (DetailProdOrderVO)headerFormPanel.getVOModel().getValueObject();
    int ret = 0;
    String methodName = null;
    if (vo.getDocStateDOC22().equals(ApplicationConsts.HEADER_BLOCKED)) {
      ret = JOptionPane.showConfirmDialog(ClientUtils.getParentFrame(this),
                                  ClientSettings.getInstance().getResources().getResource("confirm production order?"),
                                  ClientSettings.getInstance().getResources().getResource("order confirmation"),
                                  JOptionPane.YES_NO_OPTION);
      methodName = "confirmProdOrder";
    }
    else if (vo.getDocStateDOC22().equals(ApplicationConsts.CONFIRMED)) {
      ret = JOptionPane.showConfirmDialog(ClientUtils.getParentFrame(this),
                                  ClientSettings.getInstance().getResources().getResource("close production order?"),
                                  ClientSettings.getInstance().getResources().getResource("order closing"),
                                  JOptionPane.YES_NO_OPTION);
      methodName = "closeProdOrder";
    }

    if (ret==JOptionPane.YES_OPTION) {
      Response res = ClientUtils.getData(methodName,vo);
      if (!res.isError()) {
        if (vo.getDocStateDOC22().equals(ApplicationConsts.CONFIRMED))
          confirmButton.setEnabled(false);
        headerFormPanel.setMode(Consts.READONLY);
        headerFormPanel.executeReload();
        getOrders().reloadCurrentBlockOfData();
      }
      else
        JOptionPane.showMessageDialog(
          ClientUtils.getParentFrame(this),
          res.getErrorMessage(),
          ClientSettings.getInstance().getResources().getResource("Error"),
          JOptionPane.ERROR_MESSAGE
        );

    }

  }

  void printButton_actionPerformed(ActionEvent e) {
    DetailProdOrderVO vo = (DetailProdOrderVO)headerFormPanel.getVOModel().getValueObject();

    Response res = ClientUtils.getData("loadCompanyCurrency",vo.getCompanyCodeSys01DOC22());
    if (!res.isError()) {
      CurrencyVO currVO = (CurrencyVO)((VOResponse)res).getVo();

      HashMap params = new HashMap();
      params.put("COMPANY_CODE",vo.getCompanyCodeSys01DOC22());
      params.put("DOC_YEAR",vo.getDocYearDOC22());
      params.put("DOC_NUMBER",vo.getDocNumberDOC22());
      params.put("DECIMALS",currVO.getDecimalsREG03());

      HashMap map = new HashMap();
      map.put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01DOC22());
      map.put(ApplicationConsts.FUNCTION_CODE_SYS06,headerFormPanel.getFunctionId());
      map.put(ApplicationConsts.EXPORT_PARAMS,params);
      res = ClientUtils.getData("getJasperReport",map);
      if (!res.isError()) {
        JasperPrint print = (JasperPrint)((VOResponse)res).getVo();
        JRViewer viewer = new JRViewer(print);
        JFrame frame = new JFrame();
        frame.setSize(MDIFrame.getInstance().getSize());
        frame.setContentPane(viewer);
        frame.setTitle(this.getTitle());
        frame.setIconImage(MDIFrame.getInstance().getIconImage());
        frame.setVisible(true);
      } else JOptionPane.showMessageDialog(
          ClientUtils.getParentFrame(this),
          res.getErrorMessage(),
          ClientSettings.getInstance().getResources().getResource("print document"),
          JOptionPane.ERROR_MESSAGE
        );
    }
    else
      JOptionPane.showMessageDialog(
                ClientUtils.getParentFrame(this),
                res.getErrorMessage(),
                ClientSettings.getInstance().getResources().getResource("print document"),
                JOptionPane.ERROR_MESSAGE
              );
  }

  public GridControl getCompsGrid() {
    return compsGrid;
  }
  public GridControl getProductsGrid() {
    return productsGrid;
  }
  public ServerGridDataLocator getCompsGridDataLocator() {
    return compsGridDataLocator;
  }
  public GenericButton getConfirmButton() {
    return confirmButton;
  }


}

class ProdOrderFrame_confirmButton_actionAdapter implements java.awt.event.ActionListener {
  ProdOrderFrame adaptee;

  ProdOrderFrame_confirmButton_actionAdapter(ProdOrderFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.confirmButton_actionPerformed(e);
  }
}

class ProdOrderFrame_printButton_actionAdapter implements java.awt.event.ActionListener {
  ProdOrderFrame adaptee;

  ProdOrderFrame_printButton_actionAdapter(ProdOrderFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.printButton_actionPerformed(e);
  }
}
