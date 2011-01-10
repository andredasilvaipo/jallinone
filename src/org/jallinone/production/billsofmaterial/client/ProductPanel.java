package org.jallinone.production.billsofmaterial.client;

import org.openswing.swing.mdi.client.InternalFrame;
import javax.swing.*;
import java.awt.*;
import org.openswing.swing.client.*;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.util.client.ClientSettings;
import java.math.BigDecimal;
import org.jallinone.commons.client.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.lookup.client.*;
import org.openswing.swing.tree.client.*;
import java.util.Collection;
import java.awt.event.*;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.table.java.ServerGridDataLocator;
import org.openswing.swing.table.columns.client.*;
import org.jallinone.items.java.ItemPK;
import org.jallinone.items.java.ItemTypeVO;
import org.jallinone.production.billsofmaterial.java.ComponentVO;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.border.*;
import org.jallinone.items.client.ItemFrame;
import org.jallinone.items.java.DetailItemVO;
import java.text.DecimalFormat;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.registers.currency.java.CurrencyVO;
import org.jallinone.items.java.GridItemVO;
import org.openswing.swing.mdi.client.MDIFrame;
import java.util.HashMap;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JRViewer;
import org.jallinone.production.billsofmaterial.java.AltComponentVO;
import org.jallinone.production.billsofmaterial.java.MaterialVO;
import org.jallinone.items.client.ItemController;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Product panel: it contains product info panel, product explosion panel and product implosion panel.</p>
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
public class ProductPanel extends JPanel {

  JTabbedPane tab = new JTabbedPane();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  LabelControl labelRev = new LabelControl();
  NumericControl controlRev = new NumericControl();
  LabelControl labelVer = new LabelControl();
  NumericControl controlVer = new NumericControl();
  LabelControl labelManCode = new LabelControl();
  LabelControl labelStartDate = new LabelControl();
  CodLookupControl controlManCode = new CodLookupControl();
  TextControl controlManDescr = new TextControl();

  LookupController manController = new LookupController();
  LookupServerDataLocator manDataLocator = new LookupServerDataLocator();

  JPanel componentsPanel = new JPanel();
  JPanel firstPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  BorderLayout borderLayout2 = new BorderLayout();
  BorderLayout borderLayout3 = new BorderLayout();
  JPanel componentsButtonsPanel = new JPanel();
  GridControl componentsGrid = new GridControl();
  FlowLayout flowLayout2 = new FlowLayout();
  InsertButton insertButton2 = new InsertButton();
  EditButton editButton2 = new EditButton();
  SaveButton saveButton2 = new SaveButton();
  ReloadButton reloadButton2 = new ReloadButton();
  DeleteButton deleteButton2 = new DeleteButton();
  ExportButton exportButton1 = new ExportButton();
  NavigatorBar navigatorBar1 = new NavigatorBar();

  private ServerGridDataLocator componentsGridDataLocator = new ServerGridDataLocator();
  private ServerGridDataLocator altComponentsGridDataLocator = new ServerGridDataLocator();


  TextColumn colDescr = new TextColumn();
  CodLookupColumn colItemCode = new CodLookupColumn();
  DecimalColumn colSeq = new DecimalColumn();
  DecimalColumn colVer = new DecimalColumn();
  DecimalColumn colRev = new DecimalColumn();
  DecimalColumn colQty = new DecimalColumn();
  DateColumn colStartDate = new DateColumn();
  DateColumn colEndDate = new DateColumn();

  ComboColumn colItemType = new ComboColumn();

  LookupController colLevelController = new LookupController();
  LookupServerDataLocator colLevelDataLocator = new LookupServerDataLocator();
  TreeServerDataLocator colTreeLevelDataLocator = new TreeServerDataLocator();
  LookupController colItemCodeController = new LookupController();
  LookupServerDataLocator colItemCodeDataLocator = new LookupServerDataLocator();

  LookupController colLevel2Controller = new LookupController();
  LookupServerDataLocator colLevel2DataLocator = new LookupServerDataLocator();
  TreeServerDataLocator colTreeLevel2DataLocator = new TreeServerDataLocator();
  LookupController colItemCode2Controller = new LookupController();
  LookupServerDataLocator colItemCode2DataLocator = new LookupServerDataLocator();


  TreeGridPanel explosionPanel = new TreeGridPanel();
  TreeGridPanel implosionPanel = new TreeGridPanel();

  JPanel topPanel = new JPanel();

  DateControl controlStartDate = new DateControl();

  TreeServerDataLocator explosionLocator = new TreeServerDataLocator();
  TreeServerDataLocator implosionLocator = new TreeServerDataLocator();

  TitledBorder titledBorder1;
  TitledBorder titledBorder2;

  private ItemFrame frame = null;
  GenericButton printButton = new GenericButton(new ImageIcon(ClientUtils.getImage("printer.gif")));
  JPanel altPanel = new JPanel();
  BorderLayout borderLayout4 = new BorderLayout();
  JPanel altCompsButtonsPanel = new JPanel();
  GridControl altCompsGrid = new GridControl();
  FlowLayout flowLayout1 = new FlowLayout();
  InsertButton insertButton3 = new InsertButton();
  DeleteButton deleteButton3 = new DeleteButton();
  SaveButton saveButton3 = new SaveButton();
  ReloadButton reloadButton3 = new ReloadButton();
  ExportButton exportButton3 = new ExportButton();
  CodLookupColumn colAltItemCode = new CodLookupColumn();
  TextColumn colAltItemDescr = new TextColumn();
  TextColumn colUM = new TextColumn();


  public ProductPanel() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  public ProductPanel(ItemFrame frame) {
    this.frame = frame;
    try {

      jbInit();


      // manufactures lookup code...
      manDataLocator.setGridMethodName("loadManufactures");
      manDataLocator.setValidationMethodName("validateManufactureCode");
      controlManCode.setLookupController(manController);
      controlManCode.setControllerMethodName("getManufacturesList");
      manController.setLookupDataLocator(manDataLocator);
      manController.setForm(frame.getFormPanel());
      manController.setFrameTitle("manufactures");
      manController.setLookupValueObjectClassName("org.jallinone.production.manufactures.java.ManufactureVO");
      manController.addLookup2ParentLink("manufactureCodePRO01", "manufactureCodePro01ITM01");
      manController.addLookup2ParentLink("descriptionSYS10","manufactureDescriptionSYS10");
      manController.setAllColumnVisible(false);
      manController.setVisibleColumn("manufactureCodePRO01", true);
      manController.setVisibleColumn("descriptionSYS10", true);
      manController.setPreferredWidthColumn("descriptionSYS10", 200);
      manController.setFramePreferedSize(new Dimension(350,500));
      manController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          if (controlManCode.getValue()!=null) {
            if (altCompsGrid.getVOListTableModel().getRowCount()>0) {
              DetailItemVO itemVO = (DetailItemVO)ProductPanel.this.frame.getFormPanel().getVOModel().getValueObject();
              itemVO.setManufactureCodePro01ITM01(null);
              itemVO.setManufactureDescriptionSYS10(null);
              controlManCode.setValue(null);
              controlManDescr.setValue("");
              JOptionPane.showMessageDialog(
                MDIFrame.getInstance(),
                ClientSettings.getInstance().getResources().getResource("you cannot set the current item as a product, because it has alternative components"),
                ClientSettings.getInstance().getResources().getResource("Attention"),
                JOptionPane.WARNING_MESSAGE);
            }
            else {
              setCompButtonsEnabled(true);
              setAltButtonsEnabled(false);
            }
          } else {
            setCompButtonsEnabled(false);
            setAltButtonsEnabled(true);
          }
        }

        public void beforeLookupAction(ValueObject parentVO) {}

        public void forceValidate() {}

      });



      // item code lookup for the component column...
      colItemCodeDataLocator.setGridMethodName("loadItems");
      colItemCodeDataLocator.setValidationMethodName("validateItemCode");
      colItemCode.setLookupController(colItemCodeController);
      colItemCodeController.setLookupDataLocator(colItemCodeDataLocator);
      colItemCodeController.setFrameTitle("components");

      colItemCodeController.setCodeSelectionWindow(colItemCodeController.TREE_GRID_FRAME);
      colTreeLevelDataLocator.setServerMethodName("loadHierarchy");
      colItemCodeDataLocator.setTreeDataLocator(colTreeLevelDataLocator);
      colItemCodeDataLocator.setNodeNameAttribute("descriptionSYS10");

      colItemCodeController.setLookupValueObjectClassName("org.jallinone.items.java.GridItemVO");
      colItemCodeController.addLookup2ParentLink("companyCodeSys01ITM01", "companyCodeSys01ITM03");
      colItemCodeController.addLookup2ParentLink("itemCodeITM01", "itemCodeItm01ITM03");
      colItemCodeController.addLookup2ParentLink("descriptionSYS10", "descriptionSYS10");
      colItemCodeController.addLookup2ParentLink("minSellingQtyUmCodeReg02ITM01", "minSellingQtyUmCodeReg02ITM01");

      colItemCodeController.setAllColumnVisible(false);
      colItemCodeController.setVisibleColumn("companyCodeSys01ITM01", true);
      colItemCodeController.setVisibleColumn("itemCodeITM01", true);
      colItemCodeController.setVisibleColumn("descriptionSYS10", true);
      colItemCodeController.setPreferredWidthColumn("descriptionSYS10", 200);
      colItemCodeController.setFramePreferedSize(new Dimension(650,500));
      colItemCodeController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          GridItemVO vo = (GridItemVO)colItemCodeController.getLookupVO();
          if (vo.getSerialNumberRequiredITM01()!=null && vo.getSerialNumberRequiredITM01().booleanValue()) {
            JOptionPane.showMessageDialog(
              MDIFrame.getInstance(),
              ClientSettings.getInstance().getResources().getResource("the selected item requires serial number definition: this kind of item is not allowed"),
              ClientSettings.getInstance().getResources().getResource("Attention"),
              JOptionPane.WARNING_MESSAGE);
            ComponentVO compVO = (ComponentVO)parentVO;
            compVO.setItemCodeItm01ITM03(null);
            compVO.setDescriptionSYS10(null);
          }
        }

        public void beforeLookupAction(ValueObject parentVO) {
          ComponentVO vo = (ComponentVO)parentVO;
          DetailItemVO itemVO = (DetailItemVO)ProductPanel.this.frame.getFormPanel().getVOModel().getValueObject();
          colItemCodeDataLocator.getLookupFrameParams().put(ApplicationConsts.PROGRESSIVE_HIE02,vo.getProgressiveHIE02());
          colItemCodeDataLocator.getLookupValidationParameters().put(ApplicationConsts.PROGRESSIVE_HIE02,vo.getProgressiveHIE02());
          colItemCodeDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,itemVO.getCompanyCodeSys01ITM01());
          colItemCodeDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,itemVO.getCompanyCodeSys01ITM01());
          colTreeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.PROGRESSIVE_HIE02, vo.getProgressiveHIE02());
          colTreeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.PROGRESSIVE_HIE02, vo.getProgressiveHIE02());
          colItemCodeDataLocator.getLookupFrameParams().put(ApplicationConsts.PROGRESSIVE_HIE02,vo.getProgressiveHIE02());
          colItemCodeDataLocator.getLookupValidationParameters().put(ApplicationConsts.PROGRESSIVE_HIE02,vo.getProgressiveHIE02());
          colTreeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.COMPANY_CODE_SYS01, itemVO.getCompanyCodeSys01ITM01());
          colTreeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.COMPANY_CODE_SYS01, itemVO.getCompanyCodeSys01ITM01());
          colItemCodeDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,itemVO.getCompanyCodeSys01ITM01());
          colItemCodeDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,itemVO.getCompanyCodeSys01ITM01());
        }

        public void forceValidate() {}

      });


      // alternative item code lookup for the alternative components grid...
      colItemCode2DataLocator.setGridMethodName("loadItems");
      colItemCode2DataLocator.setValidationMethodName("validateItemCode");
      colItemCode2DataLocator.getLookupFrameParams().put(ApplicationConsts.COMPONENTS_ONLY,Boolean.TRUE);
      colItemCode2DataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPONENTS_ONLY,Boolean.TRUE);
      colAltItemCode.setLookupController(colItemCode2Controller);
      colItemCode2Controller.setLookupDataLocator(colItemCode2DataLocator);
      colItemCode2Controller.setFrameTitle("components");

      colItemCode2Controller.setCodeSelectionWindow(colItemCodeController.TREE_GRID_FRAME);
      colTreeLevel2DataLocator.setServerMethodName("loadHierarchy");
      colItemCode2DataLocator.setTreeDataLocator(colTreeLevel2DataLocator);
      colItemCode2DataLocator.setNodeNameAttribute("descriptionSYS10");

      colItemCode2Controller.setLookupValueObjectClassName("org.jallinone.items.java.GridItemVO");
      colItemCode2Controller.addLookup2ParentLink("companyCodeSys01ITM01", "companyCodeSys01ITM04");
      colItemCode2Controller.addLookup2ParentLink("itemCodeITM01", "itemCodeItm01ITM04");
      colItemCode2Controller.addLookup2ParentLink("descriptionSYS10", "descriptionSYS10");

      colItemCode2Controller.setAllColumnVisible(false);
      colItemCode2Controller.setVisibleColumn("companyCodeSys01ITM01", true);
      colItemCode2Controller.setVisibleColumn("itemCodeITM01", true);
      colItemCode2Controller.setVisibleColumn("descriptionSYS10", true);
      colItemCode2Controller.setPreferredWidthColumn("descriptionSYS10", 200);
      colItemCode2Controller.setFramePreferedSize(new Dimension(650,500));
      colItemCode2Controller.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          GridItemVO vo = (GridItemVO)colItemCode2Controller.getLookupVO();
          DetailItemVO itemVO = (DetailItemVO)ProductPanel.this.frame.getFormPanel().getVOModel().getValueObject();
          if (vo.getItemCodeITM01()!=null && vo.getItemCodeITM01().equals(itemVO.getItemCodeITM01())) {
            JOptionPane.showMessageDialog(
              MDIFrame.getInstance(),
              ClientSettings.getInstance().getResources().getResource("you cannot set the current item as alternative component"),
              ClientSettings.getInstance().getResources().getResource("Attention"),
              JOptionPane.WARNING_MESSAGE);
            AltComponentVO compVO = (AltComponentVO)parentVO;
            compVO.setItemCodeItm01ITM04(null);
            compVO.setDescriptionSYS10(null);
          }
        }

        public void beforeLookupAction(ValueObject parentVO) {
          DetailItemVO vo = (DetailItemVO)ProductPanel.this.frame.getFormPanel().getVOModel().getValueObject();
          colItemCode2DataLocator.getLookupFrameParams().put(ApplicationConsts.PROGRESSIVE_HIE02,vo.getProgressiveHie02ITM01());
          colItemCode2DataLocator.getLookupValidationParameters().put(ApplicationConsts.PROGRESSIVE_HIE02,vo.getProgressiveHie02ITM01());
          colItemCode2DataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01ITM01());
          colItemCode2DataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01ITM01());
          colTreeLevel2DataLocator.getTreeNodeParams().put(ApplicationConsts.PROGRESSIVE_HIE02, vo.getProgressiveHie02ITM01());
          colTreeLevel2DataLocator.getTreeNodeParams().put(ApplicationConsts.PROGRESSIVE_HIE02, vo.getProgressiveHie02ITM01());
          colItemCode2DataLocator.getLookupFrameParams().put(ApplicationConsts.PROGRESSIVE_HIE02,vo.getProgressiveHie02ITM01());
          colItemCode2DataLocator.getLookupValidationParameters().put(ApplicationConsts.PROGRESSIVE_HIE02,vo.getProgressiveHie02ITM01());
          colTreeLevel2DataLocator.getTreeNodeParams().put(ApplicationConsts.COMPANY_CODE_SYS01, vo.getCompanyCodeSys01ITM01());
          colTreeLevel2DataLocator.getTreeNodeParams().put(ApplicationConsts.COMPANY_CODE_SYS01, vo.getCompanyCodeSys01ITM01());
          colItemCode2DataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01ITM01());
          colItemCode2DataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01ITM01());
        }

        public void forceValidate() {}

      });


      // set explosion panel...
      explosionPanel.setLoadWhenVisibile(false);
      explosionPanel.setTreeController(new TreeController() {

        public void doubleClick(DefaultMutableTreeNode node) {
          MaterialVO vo = (MaterialVO)node.getUserObject();
          new ItemController(null,new ItemPK(vo.getCompanyCodeSys01ITM03(),vo.getItemCodeItm01ITM03()),false);
        }

        public void leftClick(DefaultMutableTreeNode node) {}

        public boolean rightClick(DefaultMutableTreeNode node) {
          return false;
        }

      });
      explosionLocator.setNodeNameAttribute("longDescriptionSYS10");
      explosionLocator.setServerMethodName("loadBillsOfMaterial");
      explosionPanel.addGridColumn("itemCodeItm01ITM03",170);
      explosionPanel.addGridColumn("descriptionSYS10",150);
      explosionPanel.addGridColumn("qtyITM03",50);
      explosionPanel.addGridColumn("minSellingQtyUmCodeReg02ITM01",30);
      explosionPanel.addGridColumn("valuePUR04",80);
      explosionPanel.addGridColumn("totalPrices",80);
      explosionPanel.addGridColumn("valuePRO02",80);
      explosionPanel.addGridColumn("totalCosts",80);
      explosionPanel.setFolderIconName("warehouse.gif");
      explosionPanel.setLeavesImageName("component.gif");
      explosionPanel.setTreeDataLocator(explosionLocator);
      explosionPanel.setExpandAllNodes(true);

      DecimalFormat qtyFormatter = new DecimalFormat("###0.#####");

      explosionPanel.setColumnFormatter("qtyITM03",qtyFormatter);


      // set implosion panel...
      implosionPanel.setLoadWhenVisibile(false);
      implosionPanel.setTreeController(new TreeController() {

        public void doubleClick(DefaultMutableTreeNode node) {}

        public void leftClick(DefaultMutableTreeNode node) {}

        public boolean rightClick(DefaultMutableTreeNode node) {
          return false;
        }

      });
      implosionLocator.setNodeNameAttribute("parentItemCodeItm01ITM03");
      implosionLocator.setServerMethodName("loadItemImplosion");
      implosionPanel.addGridColumn("parentItemCodeItm01ITM03",290);
      implosionPanel.addGridColumn("descriptionSYS10",200);
      implosionPanel.addGridColumn("qtyITM03",60);
      implosionPanel.addGridColumn("minSellingQtyUmCodeReg02ITM01",30);
      implosionPanel.setFolderIconName("warehouse.gif");
      implosionPanel.setLeavesImageName("warehouse.gif");
      implosionPanel.setTreeDataLocator(implosionLocator);
      implosionPanel.setExpandAllNodes(true);
      implosionPanel.setColumnFormatter("qtyITM03",qtyFormatter);


      init();

//      CustomizedControls customizedControls = new CustomizedControls(tab,formPanel,new BigDecimal(262));

      componentsGrid.setController(new ComponentsController(this));
      componentsGrid.setGridDataLocator(componentsGridDataLocator);
      componentsGridDataLocator.setServerMethodName("loadComponents");

      altCompsGrid.setController(new AltComponentsController(this));
      altCompsGrid.setGridDataLocator(altComponentsGridDataLocator);
      altComponentsGridDataLocator.setServerMethodName("loadAltComponents");

      frame.getFormPanel().addLinkedPanel(topPanel);

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * Callback method called when the data loading is completed.
   * @param error <code>true</code> if an error occours during data loading, <code>false</code> if data loading is successfully completed
   */
  public final void loadDataCompleted(boolean error,ItemPK pk) {
    if (error)
        return;
    DetailItemVO vo = (DetailItemVO)frame.getFormPanel().getVOModel().getValueObject();

    componentsGrid.getOtherGridParams().put(ApplicationConsts.ITEM_PK,pk);

    altCompsGrid.getOtherGridParams().put(ApplicationConsts.ITEM_PK,pk);

    explosionLocator.getTreeNodeParams().put(ApplicationConsts.ITEM_PK,pk);
    explosionPanel.reloadTree();

    implosionLocator.getTreeNodeParams().put(ApplicationConsts.ITEM_PK,pk);
    implosionPanel.reloadTree();

    if (controlManCode.getValue()!=null) {
      componentsGrid.reloadData();
      setCompButtonsEnabled(true);
      setAltButtonsEnabled(false);
    }
    else {
      componentsGrid.clearData();
      setCompButtonsEnabled(false);
      altCompsGrid.reloadData();
      setAltButtonsEnabled(true);
    }


    Response res = ClientUtils.getData("loadCompanyCurrency",vo.getCompanyCodeSys01ITM01());
    if (!res.isError()) {
      CurrencyVO currVO = (CurrencyVO)((VOResponse)res).getVo();
      String pattern = currVO.getCurrencySymbolREG03()+" #,##0.";
      for(int i=0;i<currVO.getDecimalsREG03().intValue();i++)
        pattern += "0";
      if (currVO.getDecimalsREG03().intValue()==0)
        pattern = "#";

      DecimalFormat currFormatter = new DecimalFormat(pattern);
      explosionPanel.setColumnFormatter("valuePUR04",currFormatter);
      explosionPanel.setColumnFormatter("totalPrices",currFormatter);
      explosionPanel.setColumnFormatter("valuePRO02",currFormatter);
      explosionPanel.setColumnFormatter("totalCosts",currFormatter);
    }

  }


  public void setManufactureCodeRequired() {
    controlManCode.setRequired(true);
  }


  /**
   * Retrieve item types and fill in the item types combo box.
   */
  private void init() {
    colItemType.setDomain(frame.getItemTypes());
    colItemType.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == e.SELECTED) {
          ComponentVO vo = (ComponentVO)componentsGrid.getVOListTableModel().getObjectForRow(componentsGrid.getSelectedRow());
          colTreeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.PROGRESSIVE_HIE02, vo.getProgressiveHIE02());
          colTreeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.PROGRESSIVE_HIE02, vo.getProgressiveHIE02());
          colItemCodeDataLocator.getLookupFrameParams().put(ApplicationConsts.PROGRESSIVE_HIE02,vo.getProgressiveHIE02());
          colItemCodeDataLocator.getLookupValidationParameters().put(ApplicationConsts.PROGRESSIVE_HIE02,vo.getProgressiveHIE02());

          colTreeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.COMPANY_CODE_SYS01, vo.getCompanyCodeSys01ITM03());
          colTreeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.COMPANY_CODE_SYS01, vo.getCompanyCodeSys01ITM03());
          colItemCodeDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01ITM03());
          colItemCodeDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01ITM03());
        }
      }
    });


  }


  private void jbInit() throws Exception {
    printButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("print bill of materials"));
    printButton.addActionListener(new ProductPanel_printButton_actionAdapter(this));
    printButton.setEnabled(false);

    this.setLayout(borderLayout3);
    titledBorder1 = new TitledBorder("");
    titledBorder2 = new TitledBorder("");
    componentsGrid.setMaxNumberOfRowsOnInsert(50);
    componentsGrid.setValueObjectClassName("org.jallinone.production.billsofmaterial.java.ComponentVO");
    componentsGrid.setAllowColumnsPermission(false);
    componentsGrid.setAllowColumnsProfile(false);

    topPanel.setLayout(gridBagLayout1);

    controlManCode.setLinkLabel(labelManCode);
    altPanel.setLayout(borderLayout4);
    altCompsGrid.setAutoLoadData(false);
    altCompsGrid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    altCompsGrid.setDeleteButton(deleteButton3);
    altCompsGrid.setExportButton(exportButton3);
    altCompsGrid.setFunctionId("ITM03");
    //altCompsGrid.setFunctionId("ITM03");
    altCompsGrid.setMaxNumberOfRowsOnInsert(50);
    altCompsGrid.setInsertButton(insertButton3);
    altCompsGrid.setReloadButton(reloadButton3);
    altCompsGrid.setSaveButton(saveButton3);
    altCompsGrid.setValueObjectClassName("org.jallinone.production.billsofmaterial.java.AltComponentVO");
    altCompsGrid.setAllowColumnsPermission(false);
    altCompsGrid.setAllowColumnsProfile(false);
    altCompsButtonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    insertButton3.setText("insertButton1");
    deleteButton3.setText("deleteButton1");
    saveButton3.setText("saveButton1");
    reloadButton3.setText("reloadButton1");
    exportButton3.setText("exportButton2");
    colAltItemCode.setColumnFilterable(true);
    colAltItemCode.setColumnSortable(true);
    colAltItemCode.setEditableOnEdit(false);
    colAltItemCode.setEditableOnInsert(true);
    colAltItemCode.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colAltItemCode.setMaxCharacters(20);
    colAltItemDescr.setColumnFilterable(true);
    colAltItemDescr.setColumnRequired(false);
    colAltItemDescr.setColumnSortable(true);
    colAltItemDescr.setPreferredWidth(250);
    colUM.setColumnName("minSellingQtyUmCodeReg02ITM01");
    colUM.setColumnRequired(false);
    colUM.setPreferredWidth(40);
    topPanel.add(controlRev,          new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    topPanel.add(controlVer,      new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    topPanel.add(labelVer,      new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    topPanel.add(labelRev,   new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    topPanel.add(labelStartDate,   new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    topPanel.add(controlStartDate,  new GridBagConstraints(5, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    topPanel.add(labelManCode,  new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    topPanel.add(controlManCode,  new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    topPanel.add(controlManDescr,   new GridBagConstraints(2, 3, 4, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

    labelRev.setText("revisionITM01");
    labelVer.setText("versionITM01");
    labelManCode.setText("manufactureCodePro01ITM01");
    labelStartDate.setText("startDateITM01");
    controlRev.setAttributeName("revisionITM01");
    controlRev.setCanCopy(true);
    controlRev.setDecimals(0);
    controlRev.setLinkLabel(labelRev);
    controlRev.setRequired(true);
    controlVer.setAttributeName("versionITM01");
    controlVer.setCanCopy(true);
    controlVer.setDecimals(0);
    controlVer.setLinkLabel(labelVer);
    controlVer.setRequired(true);
    controlManCode.setAttributeName("manufactureCodePro01ITM01");
    controlManCode.setCanCopy(true);
    controlManCode.setMaxCharacters(20);
    controlManDescr.setAttributeName("manufactureDescriptionSYS10");
    controlManDescr.setCanCopy(true);
    controlManDescr.setEnabledOnInsert(false);
    controlManDescr.setEnabledOnEdit(false);
    componentsPanel.setLayout(borderLayout1);
    componentsGrid.setAutoLoadData(false);
    componentsGrid.setDeleteButton(deleteButton2);
    componentsGrid.setEditButton(editButton2);
    componentsGrid.setExportButton(exportButton1);
    componentsGrid.setFunctionId("ITM03");
    componentsGrid.setInsertButton(insertButton2);
    componentsGrid.setNavBar(navigatorBar1);
    componentsGrid.setReloadButton(reloadButton2);
    componentsGrid.setSaveButton(saveButton2);
    componentsButtonsPanel.setLayout(flowLayout2);
    flowLayout2.setAlignment(FlowLayout.LEFT);
    insertButton2.setText("insertButton2");
    editButton2.setText("editButton2");
    saveButton2.setText("saveButton2");
    reloadButton2.setText("reloadButton2");
    deleteButton2.setText("deleteButton2");
    exportButton1.setText("exportButton1");
    controlStartDate.setLinkLabel(labelStartDate);
    controlStartDate.setRequired(true);
    controlStartDate.setAttributeName("startDateITM01");
    colVer.setColumnRequired(true);
    colSeq.setColumnRequired(true);
    colSeq.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colRev.setColumnRequired(true);
    colQty.setColumnRequired(true);
    colEndDate.setColumnRequired(false);
    componentsPanel.setBorder(titledBorder1);
    titledBorder1.setTitleColor(Color.blue);
    titledBorder1.setTitle(ClientSettings.getInstance().getResources().getResource("components"));
    this.add(tab, BorderLayout.CENTER);

    firstPanel.setLayout(borderLayout2);
    firstPanel.add(topPanel,BorderLayout.NORTH);
    firstPanel.add(componentsPanel,BorderLayout.CENTER);

    tab.add(firstPanel,   "productInfo");
    tab.add(explosionPanel,   "explosionPanel");
    tab.add(implosionPanel,   "implosionPanel");
    tab.add(altPanel,  "alternativeComponents");

    tab.setTitleAt(0,ClientSettings.getInstance().getResources().getResource("productInfo"));
    tab.setTitleAt(1,ClientSettings.getInstance().getResources().getResource("explosionPanel"));
    tab.setTitleAt(2,ClientSettings.getInstance().getResources().getResource("implosionPanel"));
    tab.setTitleAt(3,ClientSettings.getInstance().getResources().getResource("alternativeComponents"));

    componentsPanel.add(componentsButtonsPanel, BorderLayout.NORTH);
    componentsPanel.add(componentsGrid,  BorderLayout.CENTER);
    componentsButtonsPanel.add(insertButton2, null);
    componentsButtonsPanel.add(editButton2, null);
    componentsButtonsPanel.add(saveButton2, null);
    componentsButtonsPanel.add(reloadButton2, null);
    componentsButtonsPanel.add(deleteButton2, null);
    componentsButtonsPanel.add(exportButton1, null);
    componentsButtonsPanel.add(navigatorBar1, null);
    componentsButtonsPanel.add(printButton, null);

    colDescr.setColumnDuplicable(true);
    colDescr.setColumnFilterable(true);
    colDescr.setColumnName("descriptionSYS10");
    colDescr.setColumnSortable(true);
    colDescr.setEditableOnEdit(false);
    colDescr.setEditableOnInsert(false);
    colDescr.setPreferredWidth(200);
    colDescr.setHeaderColumnName("itemDescription");
    colItemCode.setColumnDuplicable(true);
    colItemCode.setColumnFilterable(true);
    colItemCode.setColumnName("itemCodeItm01ITM03");
    colItemCode.setColumnSortable(true);
    colItemCode.setEditableOnInsert(true);
    colItemCode.setMaxCharacters(20);
    colSeq.setDecimals(2);
    colSeq.setMinValue(0.0);
    colSeq.setColumnDuplicable(true);
    colSeq.setEditableOnEdit(true);
    colSeq.setColumnName("sequenceITM03");
    colSeq.setEditableOnInsert(true);
    colSeq.setPreferredWidth(60);
    colVer.setDecimals(2);
    colVer.setMinValue(0.0);
    colVer.setColumnDuplicable(true);
    colVer.setEditableOnEdit(true);
    colVer.setEditableOnInsert(true);
    colVer.setColumnName("versionITM03");
    colVer.setPreferredWidth(70);
    colRev.setDecimals(2);
    colRev.setGrouping(false);
    colRev.setMaxValue(100.0);
    colRev.setMinValue(0.0);
    colRev.setColumnDuplicable(true);
    colRev.setColumnFilterable(false);
    colRev.setEditableOnEdit(true);
    colRev.setEditableOnInsert(true);
    colRev.setPreferredWidth(65);
    colRev.setColumnName("revisionITM03");
    colQty.setDecimals(2);
    colQty.setGrouping(false);
    colQty.setMaxValue(100.0);
    colQty.setMinValue(0.0);
    colQty.setColumnDuplicable(true);
    colQty.setEditableOnEdit(true);
    colQty.setEditableOnInsert(true);
    colQty.setPreferredWidth(60);
    colQty.setColumnName("qtyITM03");
    colStartDate.setColumnDuplicable(true);
    colStartDate.setColumnFilterable(true);
    colStartDate.setColumnSortable(true);
    colStartDate.setEditableOnEdit(true);
    colStartDate.setEditableOnInsert(true);
    colStartDate.setColumnName("startDateITM03");
    colEndDate.setColumnDuplicable(true);
    colEndDate.setColumnFilterable(true);
    colEndDate.setColumnSortable(true);
    colEndDate.setEditableOnEdit(true);
    colEndDate.setEditableOnInsert(true);
    colEndDate.setColumnName("endDateITM03");

    colItemType.setColumnDuplicable(true);
    colItemType.setColumnFilterable(true);
    colItemType.setColumnName("progressiveHIE02");
    colItemType.setColumnSortable(true);
    colItemType.setEditableOnEdit(false);
    colItemType.setEditableOnInsert(true);
    colItemType.setPreferredWidth(80);
    colItemType.setHeaderColumnName("itemType");

    componentsGrid.getColumnContainer().add(colItemType, null);
    componentsGrid.getColumnContainer().add(colItemCode, null);
    componentsGrid.getColumnContainer().add(colDescr, null);
    componentsGrid.getColumnContainer().add(colSeq, null);
    componentsGrid.getColumnContainer().add(colVer, null);
    componentsGrid.getColumnContainer().add(colRev, null);
    componentsGrid.getColumnContainer().add(colQty, null);
    componentsGrid.getColumnContainer().add(colUM, null);
    componentsGrid.getColumnContainer().add(colStartDate, null);
    componentsGrid.getColumnContainer().add(colEndDate, null);
    altPanel.add(altCompsButtonsPanel, BorderLayout.NORTH);
    altPanel.add(altCompsGrid,  BorderLayout.CENTER);
    altCompsButtonsPanel.add(insertButton3, null);
    altCompsButtonsPanel.add(saveButton3, null);
    altCompsButtonsPanel.add(reloadButton3, null);
    altCompsButtonsPanel.add(deleteButton3, null);
    altCompsButtonsPanel.add(exportButton3, null);
    altCompsGrid.getColumnContainer().add(colAltItemCode, null);
    altCompsGrid.getColumnContainer().add(colAltItemDescr, null);
    colAltItemCode.setColumnName("itemCodeItm01ITM04");
    colAltItemDescr.setColumnName("descriptionSYS10");
  }


  void printButton_actionPerformed(ActionEvent e) {
    DetailItemVO vo = (DetailItemVO)frame.getFormPanel().getVOModel().getValueObject();
    HashMap map = new HashMap();
    map.put(ApplicationConsts.ITEM_PK,new ItemPK(vo.getCompanyCodeSys01ITM01(),vo.getItemCodeITM01()));
    Response res = ClientUtils.getData("createBillOfMaterialsData",map);
    if (res.isError()) {
      JOptionPane.showMessageDialog(
        ClientUtils.getParentFrame(this),
        res.getErrorMessage(),
        ClientSettings.getInstance().getResources().getResource("print document"),
        JOptionPane.ERROR_MESSAGE
      );
    }
    else {
      Object reportId = ((VOResponse)res).getVo();
      HashMap params = new HashMap();
      params.put("COMPANY_CODE",vo.getCompanyCodeSys01ITM01());
      params.put("ITEM_CODE",vo.getItemCodeITM01());
      params.put("DESCRIPTION",vo.getDescriptionSYS10());
      params.put("REPORT_ID",reportId);
      params.put("MANUFACTURE_CODE_PRO01",vo.getManufactureCodePro01ITM01());

      map = new HashMap();
      map.put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01ITM01());
      map.put(ApplicationConsts.FUNCTION_CODE_SYS06,frame.getFormPanel().getFunctionId());
      map.put(ApplicationConsts.EXPORT_PARAMS,params);
      res = ClientUtils.getData("getJasperReport",map);
      if (!res.isError()) {
        JasperPrint print = (JasperPrint)((VOResponse)res).getVo();
        JRViewer viewer = new JRViewer(print);
        JFrame frame = new JFrame();
        frame.setSize(MDIFrame.getInstance().getSize());
        frame.setContentPane(viewer);
        frame.setTitle(ClientSettings.getInstance().getResources().getResource("billofmaterial"));
        frame.setIconImage(MDIFrame.getInstance().getIconImage());
        frame.setVisible(true);
        res = ClientUtils.getData("deleteBillOfMaterialsData",reportId);
      } else JOptionPane.showMessageDialog(
          ClientUtils.getParentFrame(this),
          res.getErrorMessage(),
          ClientSettings.getInstance().getResources().getResource("print document"),
          JOptionPane.ERROR_MESSAGE
        );
    }
  }


  public final void setCompButtonsEnabled(boolean enabled) {
    if (enabled && controlManCode.getValue()==null)
      return;
    insertButton2.setEnabled(enabled);
    editButton2.setEnabled(enabled);
    deleteButton2.setEnabled(enabled);
    exportButton1.setEnabled(enabled);
    reloadButton2.setEnabled(enabled);

    DetailItemVO vo = (DetailItemVO)frame.getFormPanel().getVOModel().getValueObject();
    if (!enabled || vo.getManufactureCodePro01ITM01()!=null)
      printButton.setEnabled(enabled);
  }


  public final void setAltButtonsEnabled(boolean enabled) {
    if (enabled && controlManCode.getValue()!=null)
      return;

    insertButton3.setEnabled(enabled);
    deleteButton3.setEnabled(enabled);
    exportButton3.setEnabled(enabled);
    reloadButton3.setEnabled(enabled);

  }


  public GridControl getComponentsGrid() {
    return componentsGrid;
  }


  public TreeGridPanel getExplosionPanel() {
    return explosionPanel;
  }


  public TreeGridPanel getImplosionPanel() {
    return implosionPanel;
  }
  public ItemFrame getFrame() {
    return frame;
  }
  public GridControl getAltCompsGrid() {
    return altCompsGrid;
  }



}

class ProductPanel_printButton_actionAdapter implements java.awt.event.ActionListener {
  ProductPanel adaptee;

  ProductPanel_printButton_actionAdapter(ProductPanel adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.printButton_actionPerformed(e);
  }
}
