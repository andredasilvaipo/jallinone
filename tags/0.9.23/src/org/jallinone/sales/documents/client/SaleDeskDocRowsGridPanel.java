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


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Panel that contains the desk selling rows grid + row detail.
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
public class SaleDeskDocRowsGridPanel extends JPanel implements CurrencyColumnSettings,SaleDocument {

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
  Form detailPanel = new Form();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  TitledBorder titledBorder1;
  TitledBorder titledBorder2;
  ExportButton exportButton1 = new ExportButton();
  DecimalColumn colRowNum = new DecimalColumn();
  TextColumn colItemCode = new TextColumn();
  TextColumn colItemDescr = new TextColumn();
  DecimalColumn colQty = new DecimalColumn();
  DecimalColumn colOutQty = new DecimalColumn();
  CurrencyColumn colPriceUnit = new CurrencyColumn();
  CurrencyColumn colPrice = new CurrencyColumn();
  TextColumn colVatCode = new TextColumn();
  CurrencyColumn colVatValue = new CurrencyColumn();
  CurrencyColumn colTotalDisc = new CurrencyColumn();
  LabelControl labelItemCode = new LabelControl();
  CodLookupControl controlItemCode = new CodLookupControl();
  TextControl controlItemDescr = new TextControl();
  LabelControl labelQty = new LabelControl();
  NumericControl controlQty = new NumericControl();
  TextControl controlUmCode = new TextControl();
  LabelControl labelPriceUnit = new LabelControl();
  CurrencyControl controlPriceUnit = new CurrencyControl();
  LabelControl labelVat = new LabelControl();
  TextControl controlVatCode = new TextControl();
  TextControl controlVatDescr = new TextControl();
  LabelControl labelValueReg01 = new LabelControl();
  NumericControl controlValueReg01 = new NumericControl();
  LabelControl labelDeductibleReg01 = new LabelControl();
  NumericControl controlDeductibleReg01 = new NumericControl();
  LabelControl labelVatValue = new LabelControl();
  CurrencyControl controlTotalDisc = new CurrencyControl();
  LabelControl labelTotal = new LabelControl();
  CurrencyControl controlTotal = new CurrencyControl();
  LabelControl labelTotalDisc = new LabelControl();
  CurrencyControl controlVatValue = new CurrencyControl();

  /** header v.o. */
  private DetailSaleDocVO parentVO = null;

  /** grid data locator */
  private ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();

  /** item code lookup controller */
  LookupController itemController = new LookupController();

  /** item code lookup data locator */
  LookupServerDataLocator itemDataLocator = new LookupServerDataLocator();

  LookupServerDataLocator levelDataLocator = new LookupServerDataLocator();
  TreeServerDataLocator treeLevelDataLocator = new TreeServerDataLocator();

  ComboBoxControl controlItemType = new ComboBoxControl();

  /** header panel */
  private Form headerPanel = null;

  /** detail frame */
  private SaleDeskDocFrame frame = null;

  JTabbedPane itemTabbedPane = new JTabbedPane();
  BookedItemsPanel bookedItemsPanel = new BookedItemsPanel(false,true);
  OrderedItemsPanel orderedItemsPanel = new OrderedItemsPanel(false,true);

  BorderLayout borderLayout2 = new BorderLayout();
  JPanel southPanel = new JPanel();

  private boolean serialNumberRequired = false;
  LabelControl labelBarcode = new LabelControl();

  JPanel voidPanel = new JPanel();
  CurrencyControl controlDiscValue = new CurrencyControl();
  LabelControl labelDiscValue = new LabelControl();
  LabelControl labelDiscPerc = new LabelControl();
  NumericControl controlDiscPerc = new NumericControl();

  private int splitDiv = 260;

  private ProductVariantsPanel variantsPanel = new ProductVariantsPanel(
      new ProductVariantsController() {

        public BigDecimal validateQty(BigDecimal qty) {
          return qty;
        }

        public void qtyUpdated(BigDecimal qty) {
          updateTotals();
        }

      },
      detailPanel,
      controlItemCode,
      itemController,
      "loadProductVariantsMatrix",
      //"loadSaleDocVariantsRow",
      controlQty,
      splitPane,
      splitDiv
  );
  TextControl controlBarCode = new TextControl();
  LabelControl labelSN = new LabelControl();
  CodLookupControl controlSN = new CodLookupControl();
  LookupController serialNumberController = new LookupController();
  LookupServerDataLocator serialNumberLocator = new LookupServerDataLocator();


  public SaleDeskDocRowsGridPanel(SaleDeskDocFrame frame,Form headerPanel) {
    this.frame = frame;
    this.headerPanel = headerPanel;
    try {
      jbInit();

      if (Beans.isDesignTime())
        return;

      init();

      grid.setController(new SaleDeskDocRowsController(this));
      grid.setGridDataLocator(gridDataLocator);
      gridDataLocator.setServerMethodName("loadSaleDocRows");

      detailPanel.setFormController(new SaleDeskDocRowController(this));
      detailPanel.setMode(Consts.READONLY);

      // item code lookup...
      itemDataLocator.setGridMethodName("loadPriceItems");
      itemDataLocator.setValidationMethodName("validatePriceItemCode");

      controlItemCode.setLookupController(itemController);
      controlItemCode.setControllerMethodName("getItemsList");
      itemController.setForm(detailPanel);
      itemController.setLookupDataLocator(itemDataLocator);
      itemController.setFrameTitle("items");

      itemController.setCodeSelectionWindow(itemController.TREE_GRID_FRAME);
      treeLevelDataLocator.setServerMethodName("loadHierarchy");
      itemDataLocator.setTreeDataLocator(treeLevelDataLocator);
      itemDataLocator.setNodeNameAttribute("descriptionSYS10");

      itemController.setLookupValueObjectClassName("org.jallinone.sales.documents.java.PriceItemVO");
      itemController.addLookup2ParentLink("itemCodeItm01SAL02", "itemCodeItm01DOC02");
      itemController.addLookup2ParentLink("itemDescriptionSYS10", "descriptionSYS10");
      itemController.addLookup2ParentLink("minSellingQtyITM01","minSellingQtyItm01DOC02");
      itemController.addLookup2ParentLink("decimalsREG02", "decimalsReg02DOC02");
      itemController.addLookup2ParentLink("minSellingQtyUmCodeReg02ITM01", "minSellingQtyUmCodeReg02DOC02");
      itemController.addLookup2ParentLink("vatCodeReg01ITM01", "vatCodeItm01DOC02");
      itemController.addLookup2ParentLink("vatDescriptionSYS10", "vatDescriptionDOC02");
      itemController.addLookup2ParentLink("deductibleREG01", "deductibleReg01DOC02");
      itemController.addLookup2ParentLink("valueREG01", "valueReg01DOC02");
      itemController.addLookup2ParentLink("valueSAL02", "valueSal02DOC02");
      itemController.addLookup2ParentLink("startDateSAL02", "startDateSal02DOC02");
      itemController.addLookup2ParentLink("endDateSAL02", "endDateSal02DOC02");

      itemController.setAllColumnVisible(false);
      itemController.setVisibleColumn("itemCodeItm01SAL02", true);
      itemController.setVisibleColumn("itemDescriptionSYS10", true);
      itemController.setVisibleColumn("minSellingQtyITM01", true);
      itemController.setVisibleColumn("minSellingQtyUmCodeReg02ITM01", true);
      itemController.setVisibleColumn("valueSAL02", true);
      itemController.setVisibleColumn("startDateSAL02", true);
      itemController.setVisibleColumn("endDateSAL02", true);
      itemController.setPreferredWidthColumn("itemDescriptionSYS10", 200);
      itemController.setPreferredWidthColumn("minSellingQtyITM01", 60);
      itemController.setPreferredWidthColumn("minSellingQtyUmCodeReg02ITM01", 50);
      itemController.setFramePreferedSize(new Dimension(750,500));
      itemController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          // fill in the detail form, according to the selected item settings...
          DetailSaleDocRowVO vo = (DetailSaleDocRowVO)detailPanel.getVOModel().getValueObject();
          if (vo.getItemCodeItm01DOC02()==null || vo.getItemCodeItm01DOC02().equals("")) {
            vo.setDescriptionSYS10(null);
            vo.setMinSellingQtyUmCodeReg02DOC02(null);
            vo.setMinSellingQtyItm01DOC02(null);
            vo.setDecimalsReg02DOC02(null);
            vo.setVatCodeItm01DOC02(null);
            vo.setVatDescriptionDOC02(null);
            vo.setDeductibleReg01DOC02(null);
            vo.setValueReg01DOC02(null);
            vo.setValueSal02DOC02(null);
            vo.setQtyDOC02(null);
            serialNumberRequired = false;
          }
          else {
            vo.setQtyDOC02(vo.getMinSellingQtyItm01DOC02());
            controlQty.setMinValue(vo.getMinSellingQtyItm01DOC02().doubleValue());
            controlQty.setDecimals(vo.getDecimalsReg02DOC02().intValue());

            PriceItemVO lookupVO = (PriceItemVO)itemController.getLookupVO();
            bookedItemsPanel.getControlItemType().setValue(lookupVO.getProgressiveHie02ITM01());
            bookedItemsPanel.getControlItemCode().setValue(vo.getItemCodeItm01DOC02());
            bookedItemsPanel.getControlItemCode().getLookupController().forceValidate();
            bookedItemsPanel.getGrid().reloadData();
            orderedItemsPanel.getControlItemType().setValue(lookupVO.getProgressiveHie02ITM01());
            orderedItemsPanel.getControlItemCode().setValue(vo.getItemCodeItm01DOC02());
            orderedItemsPanel.getControlItemCode().getLookupController().forceValidate();
            orderedItemsPanel.getGrid().reloadData();

            serialNumberRequired = ((PriceItemVO)itemController.getLookupVO()).getSerialNumberRequiredITM01().booleanValue();

          }
          updateTotals();
        }

        public void beforeLookupAction(ValueObject parentVO) {}

        public void forceValidate() {}

      });


      // barcode validation...
      controlBarCode.setTrimText(true);
      controlBarCode.setUpperCase(true);
      controlBarCode.addFocusListener(new FocusAdapter() {
        public void focusLost(FocusEvent e) {
          checkBarcode();
        }
      });
      controlBarCode.addKeyListener(new KeyAdapter() {
        public void keyPressed(KeyEvent e) {
          if (e.getKeyCode()==e.VK_ENTER)
            checkBarcode();
        }
      });


      // serial number lookup...
      serialNumberLocator.setGridMethodName("loadStoredSerialNumbers");
      serialNumberLocator.setValidationMethodName("validateStoredSerialNumber");

      controlSN.setAutoCompletitionWaitTime(0);
      controlSN.setLookupController(serialNumberController);
      serialNumberController.setLookupDataLocator(serialNumberLocator);
      serialNumberController.setFrameTitle("serialNumbers");
      serialNumberController.setLookupValueObjectClassName("org.jallinone.warehouse.java.StoredSerialNumberVO");

      serialNumberController.setAllColumnVisible(false);
      serialNumberController.setVisibleColumn("itemCodeItm01WAR05", true);
      serialNumberController.setVisibleColumn("serialNumberWAR05", true);
      serialNumberController.setVisibleColumn("barCodeWAR05", true);
      serialNumberController.setFramePreferedSize(new Dimension(350,500));
      serialNumberController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          try {
            if (controlSN.getValue()==null || controlSN.getValue().equals(""))
              return;

            DetailSaleDocRowVO vo = (DetailSaleDocRowVO)getDetailPanel().getVOModel().getValueObject();

            if (serialNumberController.getLookupVO()!=null) {
              // pre-fill qty = 1 in variants panel...
              StoredSerialNumberVO snVO = (StoredSerialNumberVO)serialNumberController.getLookupVO();
              variantsPanel.setVariantsBarcode(null);
              variantsPanel.setSN(snVO);

              ArrayList sn = new ArrayList();
              sn.add(snVO.getSerialNumberWAR05());
              vo.setSerialNumbers(sn);

              vo.setCompanyCodeSys01DOC02(snVO.getCompanyCodeSys01WAR05());
              vo.setItemCodeItm01DOC02(snVO.getItemCodeItm01WAR05());
              vo.setVariantTypeItm06DOC02(snVO.getVariantTypeItm06WAR05());
              vo.setVariantCodeItm11DOC02(snVO.getVariantCodeItm11WAR05());
              vo.setVariantTypeItm07DOC02(snVO.getVariantTypeItm07WAR05());
              vo.setVariantCodeItm12DOC02(snVO.getVariantCodeItm12WAR05());
              vo.setVariantTypeItm08DOC02(snVO.getVariantTypeItm08WAR05());
              vo.setVariantCodeItm13DOC02(snVO.getVariantCodeItm13WAR05());
              vo.setVariantTypeItm09DOC02(snVO.getVariantTypeItm09WAR05());
              vo.setVariantCodeItm14DOC02(snVO.getVariantCodeItm14WAR05());
              vo.setVariantTypeItm10DOC02(snVO.getVariantTypeItm10WAR05());
              vo.setVariantCodeItm15DOC02(snVO.getVariantCodeItm15WAR05());

            }
            else {
              variantsPanel.setVariantsBarcode(null);
              variantsPanel.setSN(null);
              vo.setSerialNumbers(new ArrayList());
              vo.setItemCodeItm01DOC02(null);
              vo.setVariantTypeItm06DOC02(null);
              vo.setVariantCodeItm11DOC02(null);
              vo.setVariantTypeItm07DOC02(null);
              vo.setVariantCodeItm12DOC02(null);
              vo.setVariantTypeItm08DOC02(null);
              vo.setVariantCodeItm13DOC02(null);
              vo.setVariantTypeItm09DOC02(null);
              vo.setVariantCodeItm14DOC02(null);
              vo.setVariantTypeItm10DOC02(null);
              vo.setVariantCodeItm15DOC02(null);
            }
            itemController.forceValidate();
          }
          catch (Exception ex) {
            ex.printStackTrace();
          }
        }

        public void beforeLookupAction(ValueObject parentVO) {
          DetailSaleDocVO vo = (DetailSaleDocVO)getParentVO();
          serialNumberLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01DOC01());
          serialNumberLocator.getLookupFrameParams().put(ApplicationConsts.PROGRESSIVE_HIE01,vo.getProgressiveHie01HIE02());
          serialNumberLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01DOC01());
          serialNumberLocator.getLookupValidationParameters().put(ApplicationConsts.PROGRESSIVE_HIE01,vo.getProgressiveHie01HIE02());
        }

        public void forceValidate() {}

      });



      colPrice.setDynamicSettings(this);
      colPriceUnit.setDynamicSettings(this);
      colTotalDisc.setDynamicSettings(this);
      colVatValue.setDynamicSettings(this);

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void checkBarcode() {
    if (controlBarCode.getValue()==null || controlBarCode.getValue().equals(""))
      return;

    try {
      controlSN.setEnabled(false);

      // validate variants barcode...
      DetailSaleDocRowVO vo = (DetailSaleDocRowVO)getDetailPanel().getVOModel().getValueObject();
      LookupValidationParams pars = new LookupValidationParams((String)controlBarCode.getValue(),new HashMap());
      pars.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,getParentVO().getCompanyCodeSys01DOC01());
      Response res = ClientUtils.getData("validateVariantBarcode",pars);
      if (!res.isError()) {
        java.util.List rows = ( (VOListResponse) res).getRows();
        if (rows.size() == 1) {
          // found variants barcode: pre-fill code and qty in variants matrix...
          VariantBarcodeVO itemVO = (VariantBarcodeVO)rows.get(0);
          vo.setSerialNumbers(new ArrayList());
          vo.setCompanyCodeSys01DOC02(itemVO.getCompanyCodeSys01ITM22());
          vo.setItemCodeItm01DOC02(itemVO.getItemCodeItm01ITM22());
          vo.setVariantTypeItm06DOC02(itemVO.getVariantTypeItm06ITM22());
          vo.setVariantCodeItm11DOC02(itemVO.getVariantCodeItm11ITM22());
          vo.setVariantTypeItm07DOC02(itemVO.getVariantTypeItm07ITM22());
          vo.setVariantCodeItm12DOC02(itemVO.getVariantCodeItm12ITM22());
          vo.setVariantTypeItm08DOC02(itemVO.getVariantTypeItm08ITM22());
          vo.setVariantCodeItm13DOC02(itemVO.getVariantCodeItm13ITM22());
          vo.setVariantTypeItm09DOC02(itemVO.getVariantTypeItm09ITM22());
          vo.setVariantCodeItm14DOC02(itemVO.getVariantCodeItm14ITM22());
          vo.setVariantTypeItm10DOC02(itemVO.getVariantTypeItm10ITM22());
          vo.setVariantCodeItm15DOC02(itemVO.getVariantCodeItm15ITM22());

          variantsPanel.setVariantsBarcode(itemVO);
          variantsPanel.setSN(null);
          controlItemCode.setValue(itemVO.getItemCodeItm01ITM22());
          itemController.forceValidate();
          controlBarCode.setValue(null);
          return;
        }
      }
      else {
        // validate item barcode...
        pars = new LookupValidationParams((String)controlBarCode.getValue(),new HashMap());
        pars.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,getParentVO().getCompanyCodeSys01DOC01());
        pars.getLookupValidationParameters().put(ApplicationConsts.PRICELIST,getParentVO().getPricelistCodeSal01DOC01());
        pars.getLookupValidationParameters().put(ApplicationConsts.VALIDATE_BARCODE,Boolean.TRUE);
        res = ClientUtils.getData("validatePriceItemCode",pars);
        if (!res.isError()) {
          java.util.List rows = ((VOListResponse)res).getRows();
          if (rows.size()==1) {
            PriceItemVO itemVO = (PriceItemVO)rows.get(0);
            vo.setSerialNumbers(new ArrayList());
            vo.setCompanyCodeSys01DOC02(itemVO.getCompanyCodeSys01());
            vo.setItemCodeItm01DOC02(itemVO.getItemCodeItm01());
            variantsPanel.setVariantsBarcode(null);
            variantsPanel.setSN(null);
            itemController.forceValidate();
            controlBarCode.setValue(null);
            return;
          }
        }
      }

      vo.setSerialNumbers(new ArrayList());
      variantsPanel.setVariantsBarcode(null);
      variantsPanel.setSN(null);
      itemController.forceValidate();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    finally {
      controlSN.setEnabled(true);
    }

  }


  /**
   * Retrieve item types and fill in the item types combo box and
   * set buttons disabilitation...
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
    controlItemType.getComboBox().addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange()==e.SELECTED && detailPanel.getMode()!=Consts.READONLY) {
          DetailSaleDocRowVO vo = (DetailSaleDocRowVO)detailPanel.getVOModel().getValueObject();
          vo.setItemCodeItm01DOC02(null);
          vo.setDescriptionSYS10(null);
          vo.setMinSellingQtyUmCodeReg02DOC02(null);
          vo.setVatCodeItm01DOC02(null);
          vo.setVatDescriptionDOC02(null);
          vo.setDeductibleReg01DOC02(null);
          vo.setValueReg01DOC02(null);
          vo.setValueSal02DOC02(null);
          vo.setQtyDOC02(null);

          int selIndex = ((JComboBox)e.getSource()).getSelectedIndex();
          Object selValue = d.getDomainPairList()[selIndex].getCode();
          treeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.PROGRESSIVE_HIE02,selValue);

          detailPanel.pull(controlItemCode.getAttributeName());
          try {
            controlItemCode.validateCode(null);
          }
          catch (Exception ex) {
          }

        }
      }
    });

    // set buttons disabilitation...
    HashSet buttonsToDisable = new HashSet();
    buttonsToDisable.add(insertButton1);
    buttonsToDisable.add(editButton1);
    buttonsToDisable.add(deleteButton1);
    buttonsToDisable.add(copyButton1);
    detailPanel.addButtonsNotEnabled(buttonsToDisable,frame);
    grid.addButtonsNotEnabled(buttonsToDisable,frame);
  }



  private void jbInit() throws Exception {

    controlBarCode.setEnabledOnEdit(false);
    controlBarCode.setMaxCharacters(255);

    titledBorder1 = new TitledBorder("");
    this.setLayout(borderLayout1);
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
    grid.setAutoLoadData(false);
    grid.setCopyButton(copyButton1);
    grid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    grid.setDeleteButton(deleteButton1);
    grid.setExportButton(exportButton1);
    grid.setFunctionId("DOC01_DESKSALES");
    grid.setMaxSortedColumns(3);
    grid.setNavBar(navigatorBar1);
    grid.setReloadButton(reloadButton1);
    grid.setValueObjectClassName("org.jallinone.sales.documents.java.GridSaleDocRowVO");
    detailPanel.setLayout(gridBagLayout1);
    detailPanel.setBorder(titledBorder1);
    detailPanel.setMinimumSize(new Dimension(740, 150));
    detailPanel.setInsertButton(insertButton1);
    detailPanel.setCopyButton(copyButton1);
    detailPanel.setEditButton(editButton1);
    detailPanel.setReloadButton(reloadButton1);
    detailPanel.setSaveButton(saveButton1);
    detailPanel.setVOClassName("org.jallinone.sales.documents.java.DetailSaleDocRowVO");
    detailPanel.setFunctionId("DOC01_DESKSALES");
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
    colItemDescr.setPreferredWidth(200);
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

    colPriceUnit.setColumnName("valueSal02DOC02");
    colPriceUnit.setDecimals(5);
    colPriceUnit.setPreferredWidth(90);
    colPrice.setColumnName("valueDOC02");
    colPrice.setColumnSortable(false);
    colPrice.setDecimals(5);
    colPrice.setPreferredWidth(90);
    colVatCode.setColumnName("vatCodeItm01DOC02");
    colVatCode.setColumnSortable(false);
    colVatCode.setHeaderColumnName("vatCode");
    colVatCode.setPreferredWidth(70);
    colVatValue.setColumnName("vatValueDOC02");
    colVatValue.setColumnSortable(false);
    colVatValue.setDecimals(5);
    colVatValue.setPreferredWidth(90);
    colTotalDisc.setColumnName("totalDiscountDOC02");
    colTotalDisc.setPreferredWidth(90);
    labelItemCode.setText("item");
    labelQty.setText("qtyDOC02");
    labelPriceUnit.setText("valueSal02DOC02");
    labelVat.setText("vatCode");
    labelValueReg01.setText("valueReg01DOC02");
    labelDeductibleReg01.setText("deductibleReg01DOC02");
    labelVatValue.setText("vatValueDOC02");
    labelTotal.setRequestFocusEnabled(true);
    labelTotal.setText("valueDOC02");
    labelTotalDisc.setText("totalDiscountDOC02");
    controlItemCode.setAttributeName("itemCodeItm01DOC02");
    controlItemCode.setCanCopy(true);
    controlItemCode.setEnabledOnEdit(false);
    controlItemCode.setLinkLabel(labelItemCode);
    controlItemCode.setMaxCharacters(20);
    controlItemCode.setRequired(true);
    controlItemDescr.setAttributeName("descriptionSYS10");
    controlItemDescr.setCanCopy(true);
    controlItemDescr.setEnabledOnInsert(false);
    controlItemDescr.setEnabledOnEdit(false);
    controlVatCode.setAttributeName("vatCodeItm01DOC02");
    controlVatCode.setCanCopy(true);
    controlVatCode.setEnabledOnInsert(false);
    controlVatCode.setEnabledOnEdit(false);
    controlVatDescr.setAttributeName("vatDescriptionDOC02");
    controlVatDescr.setCanCopy(true);
    controlVatDescr.setEnabledOnInsert(false);
    controlVatDescr.setEnabledOnEdit(false);
    controlValueReg01.setAttributeName("valueReg01DOC02");
    controlValueReg01.setCanCopy(true);
    controlValueReg01.setDecimals(5);
    controlValueReg01.setEnabledOnInsert(false);
    controlValueReg01.setEnabledOnEdit(false);
    controlDeductibleReg01.setAttributeName("deductibleReg01DOC02");
    controlDeductibleReg01.setCanCopy(true);
    controlDeductibleReg01.setDecimals(5);
    controlDeductibleReg01.setEnabledOnInsert(false);
    controlDeductibleReg01.setEnabledOnEdit(false);
    controlQty.setAttributeName("qtyDOC02");
    controlQty.setCanCopy(true);
    controlQty.setDecimals(5);
    controlQty.setLinkLabel(labelQty);
    controlQty.setMinValue(0.0);
    controlQty.setRequired(true);
    controlQty.addFocusListener(new SaleDeskDocRowsGridPanel_controlQty_focusAdapter(this));
    controlUmCode.setAttributeName("minSellingQtyUmCodeReg02DOC02");
    controlUmCode.setCanCopy(true);
    controlUmCode.setEnabledOnInsert(false);
    controlUmCode.setEnabledOnEdit(false);
    controlPriceUnit.setAttributeName("valueSal02DOC02");
    controlPriceUnit.setCanCopy(true);
    controlPriceUnit.setDecimals(5);
    controlPriceUnit.setEnabledOnEdit(true);
    controlPriceUnit.setLinkLabel(labelPriceUnit);
    controlPriceUnit.setMinValue(0.0);
    controlPriceUnit.setRequired(true);
    controlPriceUnit.addFocusListener(new SaleDeskDocRowsGridPanel_controlPriceUnit_focusAdapter(this));
    controlVatValue.setAttributeName("vatValueDOC02");
    controlVatValue.setCanCopy(true);
    controlVatValue.setDecimals(5);
    controlVatValue.setEnabledOnEdit(false);
    controlVatValue.setEnabledOnInsert(false);
    controlVatValue.setLinkLabel(labelVatValue);
    controlTotalDisc.setAttributeName("discountPercDOC02");
    controlTotalDisc.setCanCopy(true);
    controlTotalDisc.setDecimals(5);
    controlTotalDisc.setAttributeName("totalDiscountDOC02");
    controlTotalDisc.setLinkLabel(labelTotalDisc);
    controlTotalDisc.setMaxValue(100.0);
    controlTotalDisc.setMinValue(0.0);
    controlTotalDisc.setEnabledOnInsert(false);
    controlTotalDisc.setEnabledOnEdit(false);
    controlTotal.setAttributeName("valueDOC02");
    controlTotal.setCanCopy(true);
    controlTotal.setDecimals(5);
    controlTotal.setEnabledOnEdit(false);
    controlTotal.setEnabledOnInsert(false);
    controlTotal.setLinkLabel(labelTotal);

    insertButton1.setEnabled(false);
    copyButton1.setEnabled(false);
    saveButton1.setEnabled(false);
    deleteButton1.setEnabled(false);
    exportButton1.setEnabled(false);
    editButton1.setEnabled(false);
    controlItemType.setAttributeName("progressiveHie02DOC02");
    controlItemType.setCanCopy(true);
    controlItemType.setLinkLabel(labelItemCode);
    controlItemType.setRequired(true);
    controlItemType.setEnabledOnEdit(false);

    labelBarcode.setText("barcode");
    controlDiscValue.setDecimals(5);
    controlDiscValue.setCanCopy(true);
    controlDiscValue.setAttributeName("discountValue");
    controlDiscValue.addFocusListener(new SaleDeskDocRowsGridPanel_controlDiscValue_focusAdapter(this));
    controlDiscValue.setLinkLabel(labelDiscValue);
    labelDiscValue.setText("net discount value");
    labelDiscPerc.setText("discount perc");
    controlDiscPerc.setMinValue(0.0);
    controlDiscPerc.setMaxValue(100.0);
    controlDiscPerc.setLinkLabel(labelDiscPerc);
    controlDiscPerc.setDecimals(2);
    controlDiscPerc.setCanCopy(true);
    controlDiscPerc.setAttributeName("discountPercDOC02");
    controlDiscPerc.addFocusListener(new SaleDeskDocRowsGridPanel_controlDiscPerc_focusAdapter(this));
    labelSN.setText("serial number");
    controlSN.setEnabledOnEdit(false);
    controlSN.setLookupButtonVisible(false);
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
    grid.getColumnContainer().add(colItemCode, null);
    grid.getColumnContainer().add(colItemDescr, null);
    grid.getColumnContainer().add(colQty, null);
    grid.getColumnContainer().add(colPriceUnit, null);
    grid.getColumnContainer().add(colPrice, null);
    grid.getColumnContainer().add(colVatCode, null);
    grid.getColumnContainer().add(colVatValue, null);
    grid.getColumnContainer().add(colTotalDisc, null);
    grid.getColumnContainer().add(colOutQty, null);
    splitPane.add(itemTabbedPane, JSplitPane.BOTTOM);

    southPanel.setLayout(borderLayout2);
    southPanel.add(detailPanel,  BorderLayout.CENTER);

    itemTabbedPane.add(southPanel,"item");
    itemTabbedPane.add(bookedItemsPanel,"booked items and availability");
    itemTabbedPane.add(orderedItemsPanel,"future item availability");
    itemTabbedPane.setTitleAt(0,ClientSettings.getInstance().getResources().getResource("item"));
    itemTabbedPane.setTitleAt(1,ClientSettings.getInstance().getResources().getResource("booked items and availability"));
    itemTabbedPane.setTitleAt(2,ClientSettings.getInstance().getResources().getResource("future item availability"));

    detailPanel.add(labelItemCode,                                 new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 0, 0));
    detailPanel.add(controlItemCode,                                                  new GridBagConstraints(3, 1, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 30, 0));
    detailPanel.add(labelQty,                                  new GridBagConstraints(0, 4, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
    detailPanel.add(controlQty,                                   new GridBagConstraints(2, 4, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 5), 20, 0));
    detailPanel.add(labelVat,                                new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
    detailPanel.add(controlVatCode,                                    new GridBagConstraints(2, 3, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 20, 0));
    detailPanel.add(controlVatDescr,                                  new GridBagConstraints(4, 3, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 5, 5), 70, 0));
    detailPanel.add(labelValueReg01,                                new GridBagConstraints(6, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
    detailPanel.add(controlValueReg01,                                   new GridBagConstraints(7, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 20, 0));
    detailPanel.add(labelDeductibleReg01,                                new GridBagConstraints(8, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
    detailPanel.add(controlDeductibleReg01,                                 new GridBagConstraints(9, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 20, 0));
    detailPanel.add(controlItemDescr,                                  new GridBagConstraints(5, 1, 5, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    detailPanel.add(controlUmCode,                                 new GridBagConstraints(4, 4, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 5, 5), 70, 0));
    detailPanel.add(labelPriceUnit,                             new GridBagConstraints(6, 4, 1, 2, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
    detailPanel.add(controlPriceUnit,                              new GridBagConstraints(7, 4, 1, 2, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 20, 0));
    detailPanel.add(labelVatValue,                             new GridBagConstraints(8, 4, 1, 2, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
    detailPanel.add(controlTotalDisc,                                new GridBagConstraints(7, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 10, 5), 20, 0));
    detailPanel.add(labelTotalDisc,                               new GridBagConstraints(6, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 10, 5), 0, 0));
    detailPanel.add(controlVatValue,                              new GridBagConstraints(9, 4, 1, 2, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 20, 0));
    detailPanel.add(labelTotal,                               new GridBagConstraints(8, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 10, 5), 0, 0));
    detailPanel.add(controlTotal,                                new GridBagConstraints(9, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 10, 5), 20, 0));
    detailPanel.add(controlItemType,                                new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    detailPanel.add(voidPanel,                        new GridBagConstraints(0, 8, 2, 1, 0.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    detailPanel.add(controlDiscPerc,     new GridBagConstraints(9, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 20, 0));
    detailPanel.add(labelDiscPerc,   new GridBagConstraints(8, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
    detailPanel.add(controlDiscValue,    new GridBagConstraints(7, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
    detailPanel.add(labelDiscValue,   new GridBagConstraints(6, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
    detailPanel.add(variantsPanel,    new GridBagConstraints(0, 2, 10, 1, 1.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    detailPanel.add(labelBarcode,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(10, 5, 5, 5), 0, 0));
    detailPanel.add(controlBarCode,       new GridBagConstraints(2, 0, 3, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 0, 0), 0, 0));
    detailPanel.add(labelSN,    new GridBagConstraints(5, 0, 2, 1, 0.0, 0.0
            ,GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(10, 5, 5, 5), 0, 0));
    detailPanel.add(controlSN,    new GridBagConstraints(7, 0, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 0, 5), 0, 0));

    splitPane.setDividerLocation(250);

  }


  public void setButtonsEnabled(boolean enabled) {
    if (enabled) {
      insertButton1.setEnabled(enabled);
      reloadButton1.setEnabled(enabled);
    }
    else {
      insertButton1.setEnabled(enabled);
      editButton1.setEnabled(enabled);
      saveButton1.setEnabled(enabled);
      deleteButton1.setEnabled(enabled);
      exportButton1.setEnabled(enabled);
      reloadButton1.setEnabled(enabled);
      copyButton1.setEnabled(false);
    }

    bookedItemsPanel.setEnabled(enabled);
    orderedItemsPanel.setEnabled(enabled);
  }


  public void setParentVO(DetailSaleDocVO parentVO) {
    this.parentVO = parentVO;

    controlTotalDisc.setCurrencySymbol(parentVO.getCurrencySymbolREG03());
    controlTotalDisc.setDecimalSymbol(parentVO.getDecimalSymbolREG03().charAt(0));
    controlTotalDisc.setGroupingSymbol(parentVO.getThousandSymbolREG03().charAt(0));
    controlTotalDisc.setDecimals(parentVO.getDecimalsREG03().intValue());

    controlPriceUnit.setCurrencySymbol(parentVO.getCurrencySymbolREG03());
    controlPriceUnit.setDecimalSymbol(parentVO.getDecimalSymbolREG03().charAt(0));
    controlPriceUnit.setGroupingSymbol(parentVO.getThousandSymbolREG03().charAt(0));
    controlPriceUnit.setDecimals(parentVO.getDecimalsREG03().intValue());

    controlTotal.setCurrencySymbol(parentVO.getCurrencySymbolREG03());
    controlTotal.setDecimalSymbol(parentVO.getDecimalSymbolREG03().charAt(0));
    controlTotal.setGroupingSymbol(parentVO.getThousandSymbolREG03().charAt(0));
    controlTotal.setDecimals(parentVO.getDecimalsREG03().intValue());

    controlVatValue.setCurrencySymbol(parentVO.getCurrencySymbolREG03());
    controlVatValue.setDecimalSymbol(parentVO.getDecimalSymbolREG03().charAt(0));
    controlVatValue.setGroupingSymbol(parentVO.getThousandSymbolREG03().charAt(0));
    controlVatValue.setDecimals(parentVO.getDecimalsREG03().intValue());

    controlDiscValue.setCurrencySymbol(parentVO.getCurrencySymbolREG03());
    controlDiscValue.setDecimalSymbol(parentVO.getDecimalSymbolREG03().charAt(0));
    controlDiscValue.setGroupingSymbol(parentVO.getThousandSymbolREG03().charAt(0));
    controlDiscValue.setDecimals(parentVO.getDecimalsREG03().intValue());

    itemDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,parentVO.getCompanyCodeSys01DOC01());
    itemDataLocator.getLookupFrameParams().put(ApplicationConsts.PROGRESSIVE_REG04,parentVO.getProgressiveReg04DOC01());
    itemDataLocator.getLookupFrameParams().put(ApplicationConsts.PRICELIST,parentVO.getPricelistCodeSal01DOC01());
    itemDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,parentVO.getCompanyCodeSys01DOC01());
    itemDataLocator.getLookupValidationParameters().put(ApplicationConsts.PROGRESSIVE_REG04,parentVO.getProgressiveReg04DOC01());
    itemDataLocator.getLookupValidationParameters().put(ApplicationConsts.PRICELIST,parentVO.getPricelistCodeSal01DOC01());

    bookedItemsPanel.getGrid().getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,parentVO.getCompanyCodeSys01DOC01());
    bookedItemsPanel.getGrid().getOtherGridParams().put(ApplicationConsts.WAREHOUSE_CODE,parentVO.getWarehouseCodeWar01DOC01());
    bookedItemsPanel.setEnabled(true);

    orderedItemsPanel.getGrid().getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,parentVO.getCompanyCodeSys01DOC01());
    orderedItemsPanel.getGrid().getOtherGridParams().put(ApplicationConsts.WAREHOUSE_CODE,parentVO.getWarehouseCodeWar01DOC01());
    bookedItemsPanel.setEnabled(true);

    setButtonsEnabled(true);
    detailPanel.setMode(Consts.READONLY);

  }


  public GridControl getGrid() {
    return grid;
  }


  public Form getDetailPanel() {
    return detailPanel;
  }


  public DetailSaleDocVO getParentVO() {
    return parentVO;
  }


  void controlQty_focusLost(FocusEvent e) {
    updateTotals();
  }


  void controlPriceUnit_focusLost(FocusEvent e) {
    updateTotals();
  }


  /**
   * Method called when qty or price per unit or vat or discount value/percentage has been changed.
   */
  private void updateTotals() {

    DetailSaleDocRowVO vo = (DetailSaleDocRowVO)detailPanel.getVOModel().getValueObject();

    if (vo.getStartDateSal02DOC02()!=null &&
        vo.getEndDateSal02DOC02()!=null &&
        (vo.getStartDateSal02DOC02().getTime()>System.currentTimeMillis() ||
        vo.getEndDateSal02DOC02().getTime()<System.currentTimeMillis())) {
      JOptionPane.showMessageDialog(
          ClientUtils.getParentFrame(this),
          ClientSettings.getInstance().getResources().getResource("the price is no more valid"),
          ClientSettings.getInstance().getResources().getResource("Attention"),
          JOptionPane.ERROR_MESSAGE
      );
      return;
    }


    if (vo.getQtyDOC02()!=null && vo.getValueReg01DOC02()!=null && vo.getValueSal02DOC02()!=null) {
      vo.setTaxableIncomeDOC02(vo.getQtyDOC02().multiply(vo.getValueSal02DOC02()).setScale(parentVO.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP));

      // determine total discount...
      Response res = ClientUtils.getData("getSaleItemTotalDiscounts",vo);
      if (res.isError()) {
        JOptionPane.showMessageDialog(
            ClientUtils.getParentFrame(this),
            ClientSettings.getInstance().getResources().getResource("error on applying item discounts"),
            ClientSettings.getInstance().getResources().getResource("Attention"),
            JOptionPane.ERROR_MESSAGE
        );
        return;
      }
      DetailSaleDocRowVO newVO = (DetailSaleDocRowVO)((VOResponse)res).getVo();
      vo.setTotalDiscountDOC02( newVO.getTotalDiscountDOC02().setScale(parentVO.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP) );

      // apply total discount to taxable income...
      vo.setTaxableIncomeDOC02(vo.getTaxableIncomeDOC02().subtract(vo.getTotalDiscountDOC02()).setScale(parentVO.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP));

      // calculate row vat...
      double vatPerc = vo.getValueReg01DOC02().doubleValue()*(1d-vo.getDeductibleReg01DOC02().doubleValue()/100d)/100;
      vo.setVatValueDOC02(vo.getTaxableIncomeDOC02().multiply(new BigDecimal(vatPerc)).setScale(parentVO.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP));

      // calculate row total...
      vo.setValueDOC02(vo.getTaxableIncomeDOC02().add(vo.getVatValueDOC02()).setScale(parentVO.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP));
      detailPanel.pull("valueDOC02");
      detailPanel.pull("taxableIncomeDOC02");
      detailPanel.pull("vatValueDOC02");
      detailPanel.pull("totalDiscountDOC02");
      detailPanel.pull("qtyDOC02");

    }
    else {
      vo.setTotalDiscountDOC02(null);
      vo.setTaxableIncomeDOC02(null);
      vo.setVatValueDOC02(null);
      vo.setValueDOC02(null);
    }

  }


  public Form getHeaderPanel() {
    return headerPanel;
  }


  public GridControl getDesks() {
    return frame.getDesks();
  }


  public SaleDeskDocFrame getFrame() {
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


  public BookedItemsPanel getBookedItemsPanel() {
    return bookedItemsPanel;
  }
  public OrderedItemsPanel getOrderedItemsPanel() {
    return orderedItemsPanel;
  }

  public Form getHeaderFormPanel() {
    return frame.getHeaderFormPanel();
  }


  public ProductVariantsPanel getVariantsPanel() {
    return variantsPanel;
  }


  /**
   * Method NOT supported.
   */
  public boolean isButtonDisabled(GenericButton button) {
    return false;
  }


  /**
   * Method called by item row saving algorithm to prompt the list of serial numbers (if required).
   */
  public final boolean isSerialNumberRequired() {
    return serialNumberRequired;
  }


  void controlDiscValue_focusLost(FocusEvent e) {
    DetailSaleDocRowVO vo = (DetailSaleDocRowVO)detailPanel.getVOModel().getValueObject();
    if (controlDiscPerc.getValue()!=null && controlDiscValue.getValue()!=null)
      vo.setDiscountPercDOC02(null);

    //BigDecimal imp = controlPriceUnit.getBigDecimal().multiply(controlQty.getBigDecimal());
    BigDecimal vat = controlValueReg01.getBigDecimal();
    BigDecimal x = controlDiscValue.getBigDecimal();
    BigDecimal d = null;
    if (x!=null)
      d = x.divide(
        new BigDecimal(1).add(
          vat.divide(
            new BigDecimal(100),
            BigDecimal.ROUND_HALF_UP
          )
        ),
        controlDiscValue.getDecimals(),
        BigDecimal.ROUND_HALF_UP
      );
    vo.setDiscountValueDOC02(d);
    updateTotals();
  }


  void controlDiscPerc_focusLost(FocusEvent e) {
    DetailSaleDocRowVO vo = (DetailSaleDocRowVO)detailPanel.getVOModel().getValueObject();
    if (controlDiscPerc.getValue()!=null && controlDiscValue.getValue()!=null)
      vo.setDiscountValueDOC02(null);
    vo.setDiscountPercDOC02(controlDiscPerc.getBigDecimal());
    updateTotals();
  }
  public TextControl getControlBarCode() {
    return controlBarCode;
  }
  public CodLookupControl getControlSN() {
    return controlSN;
  }


}


class SaleDeskDocRowsGridPanel_controlQty_focusAdapter extends java.awt.event.FocusAdapter {
  SaleDeskDocRowsGridPanel adaptee;

  SaleDeskDocRowsGridPanel_controlQty_focusAdapter(SaleDeskDocRowsGridPanel adaptee) {
    this.adaptee = adaptee;
  }
  public void focusLost(FocusEvent e) {
    adaptee.controlQty_focusLost(e);
  }
}

class SaleDeskDocRowsGridPanel_controlPriceUnit_focusAdapter extends java.awt.event.FocusAdapter {
  SaleDeskDocRowsGridPanel adaptee;

  SaleDeskDocRowsGridPanel_controlPriceUnit_focusAdapter(SaleDeskDocRowsGridPanel adaptee) {
    this.adaptee = adaptee;
  }
  public void focusLost(FocusEvent e) {
    adaptee.controlPriceUnit_focusLost(e);
  }
}



class SaleDeskDocRowsGridPanel_controlDiscValue_focusAdapter extends java.awt.event.FocusAdapter {
  SaleDeskDocRowsGridPanel adaptee;

  SaleDeskDocRowsGridPanel_controlDiscValue_focusAdapter(SaleDeskDocRowsGridPanel adaptee) {
    this.adaptee = adaptee;
  }
  public void focusLost(FocusEvent e) {
    adaptee.controlDiscValue_focusLost(e);
  }
}

class SaleDeskDocRowsGridPanel_controlDiscPerc_focusAdapter extends java.awt.event.FocusAdapter {
  SaleDeskDocRowsGridPanel adaptee;

  SaleDeskDocRowsGridPanel_controlDiscPerc_focusAdapter(SaleDeskDocRowsGridPanel adaptee) {
    this.adaptee = adaptee;
  }
  public void focusLost(FocusEvent e) {
    adaptee.controlDiscPerc_focusLost(e);
  }
}
