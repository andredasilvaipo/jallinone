package org.jallinone.items.client;

import org.openswing.swing.mdi.client.InternalFrame;
import javax.swing.*;
import java.awt.*;
import org.openswing.swing.client.*;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.form.client.Form;
import java.math.BigDecimal;
import org.jallinone.commons.client.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.domains.java.*;
import org.jallinone.items.java.ItemTypeVO;
import org.jallinone.hierarchies.java.HierarchyLevelVO;
import java.util.ArrayList;
import org.openswing.swing.lookup.client.*;
import org.openswing.swing.tree.client.*;
import java.util.Collection;
import org.jallinone.registers.measure.java.MeasureVO;
import org.jallinone.registers.vat.java.VatVO;
import org.jallinone.items.java.DetailItemVO;
import java.awt.event.*;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.*;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.table.java.ServerGridDataLocator;
import org.openswing.swing.table.columns.client.*;
import org.jallinone.items.java.ItemPK;
import org.jallinone.warehouse.availability.client.*;
import org.jallinone.production.billsofmaterial.client.ProductPanel;
import org.jallinone.purchases.pricelist.client.SupplierItemPricesPanel;
import java.util.HashSet;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.jallinone.variants.java.VariantNameVO;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import org.jallinone.variants.client.ProductVariantsPanel;
import org.jallinone.variants.client.ProductVariantsController;
import org.jallinone.items.spareparts.client.ItemSparePartsPanel;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Item detail frame.</p>
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
public class ItemFrame extends InternalFrame {

  JPanel buttonsPanel = new JPanel();
  JTabbedPane tab = new JTabbedPane();
  FlowLayout flowLayout1 = new FlowLayout();
  Form formPanel = new Form();
  JPanel imgPanel = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  InsertButton insertButton1 = new InsertButton();
  CopyButton copyButton1 = new CopyButton();
  EditButton editButton1 = new EditButton();
  SaveButton saveButton1 = new SaveButton();
  ReloadButton reloadButton1 = new ReloadButton();
  DeleteButton deleteButton1 = new DeleteButton();
  LabelControl labelItemCode = new LabelControl();
  TextControl controlItemCode = new TextControl();
  LabelControl labelDescr = new LabelControl();
  TextControl controlDescr = new TextControl();
  LabelControl labelAddDescr = new LabelControl();
  TextControl controlAddDescr = new TextControl();
  LabelControl labelitemType = new LabelControl();
  ComboBoxControl controlItemType = new ComboBoxControl();
  CodLookupControl controlLevel = new CodLookupControl();
  TextControl controlLevelDescr = new TextControl();
  LabelControl labelLevel = new LabelControl();
  LabelControl labelMinSellQty = new LabelControl();
  NumericControl controlMinSellQty = new NumericControl();
  CodLookupControl controlUMSellQty = new CodLookupControl();
  LabelControl labelVat = new LabelControl();
  CodLookupControl controlVat = new CodLookupControl();
  LabelControl labelGW = new LabelControl();
  NumericControl controlGW = new NumericControl();
  CodLookupControl controlUMGW = new CodLookupControl();
  LabelControl labelNW = new LabelControl();
  NumericControl controlNW = new NumericControl();
  CodLookupControl controlUMNW = new CodLookupControl();
  LabelControl labelW = new LabelControl();
  NumericControl controlW = new NumericControl();
  CodLookupControl controlUMW = new CodLookupControl();
  LabelControl labelH = new LabelControl();
  NumericControl controlH = new NumericControl();
  CodLookupControl controlUMH = new CodLookupControl();
  NumericControl controlVatValue = new NumericControl();
  TextControl controlVatDescr = new TextControl();
  LabelControl labelNote = new LabelControl();
  TextAreaControl controlNote = new TextAreaControl();
  CheckBoxControl controlNoWarehouseMov = new CheckBoxControl();
  LookupController levelController = new LookupController();
  LookupServerDataLocator levelDataLocator = new LookupServerDataLocator();
  TreeServerDataLocator treeLevelDataLocator = new TreeServerDataLocator();
  LookupController umController = new LookupController();
  LookupServerDataLocator umDataLocator = new LookupServerDataLocator();
  LookupController umGWController = new LookupController();
  LookupController umNWController = new LookupController();
  LookupController umWController = new LookupController();
  LookupController umHController = new LookupController();
  LookupController vatController = new LookupController();
  LookupServerDataLocator vatDataLocator = new LookupServerDataLocator();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  JButton smallImageButton = new JButton();
  org.jallinone.commons.client.ImagePanel smallImage = new org.jallinone.commons.client.ImagePanel();
  JButton largeImageButton = new JButton();
  org.jallinone.commons.client.ImagePanel largeImage = new org.jallinone.commons.client.ImagePanel();
  JButton clearSmallImageButton = new JButton();
  JButton clearLargeImageButton = new JButton();
  JPanel discountsPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel discountsButtonsPanel = new JPanel();
  GridControl discountsGrid = new GridControl();
  FlowLayout flowLayout2 = new FlowLayout();
  InsertButton insertButton2 = new InsertButton();
  EditButton editButton2 = new EditButton();
  SaveButton saveButton2 = new SaveButton();
  ReloadButton reloadButton2 = new ReloadButton();
  DeleteButton deleteButton2 = new DeleteButton();
  ExportButton exportButton1 = new ExportButton();
  NavigatorBar navigatorBar1 = new NavigatorBar();
  /** discounts grid data locator */
  private ServerGridDataLocator discountsGridDataLocator = new ServerGridDataLocator();
  TextColumn colDiscountCode = new TextColumn();
  TextColumn colDescr = new TextColumn();
  CodLookupColumn colCurrencyCode = new CodLookupColumn();
  DecimalColumn colMinValue = new DecimalColumn();
  DecimalColumn colMaxValue = new DecimalColumn();
  DecimalColumn colMinPerc = new DecimalColumn();
  DecimalColumn colMaxPerc = new DecimalColumn();
  DateColumn colStartDate = new DateColumn();
  DateColumn colEndDate = new DateColumn();
  DecimalColumn colMinQty = new DecimalColumn();
  CheckBoxColumn colMultipleQty = new CheckBoxColumn();
  LookupController currencyController = new LookupController();
  LookupServerDataLocator currencyDataLocator = new LookupServerDataLocator();
  JPanel pricesPanel = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  JPanel pricesButtonsPanel = new JPanel();
  GridControl pricesGrid = new GridControl();
  FlowLayout flowLayout3 = new FlowLayout();
  InsertButton insertButton3 = new InsertButton();
  CopyButton copyButton2 = new CopyButton();
  ReloadButton reloadButton3 = new ReloadButton();
  NavigatorBar navigatorBar2 = new NavigatorBar();
  DeleteButton deleteButton3 = new DeleteButton();
  ExportButton exportButton2 = new ExportButton();
  SaveButton saveButton3 = new SaveButton();
  EditButton editButton3 = new EditButton();
  DecimalColumn colValue = new DecimalColumn();
  DateColumn colPriceStartDate = new DateColumn();
  DateColumn colPriceEndDate = new DateColumn();
  CodLookupColumn colPricelistCode = new CodLookupColumn();
  TextColumn colPricelistDescr = new TextColumn();
  /** prices grid data locator */
  private ServerGridDataLocator pricesGridDataLocator = new ServerGridDataLocator();
  LookupController pricelistController = new LookupController();
  LookupServerDataLocator pricelistDataLocator = new LookupServerDataLocator();
  NumericControl controlVatDeductible = new NumericControl();
  CheckBoxControl controlSerialNumRequired = new CheckBoxControl();
  BookedItemsPanel bookedItemsPanel = new BookedItemsPanel(true, false);
  OrderedItemsPanel orderedItemsPanel = new OrderedItemsPanel(true, false);
  ItemAttachedDocsPanel docsPanel = new ItemAttachedDocsPanel();
  JPanel billOfMaterialsPanel = new JPanel();
  BorderLayout borderLayout3 = new BorderLayout();
  private Domain d = new Domain("ITEM_TYPES");
  private ProductPanel bomTabbedPane = new ProductPanel(this);
  private SupplierItemPricesPanel supplierPrices = new SupplierItemPricesPanel(this);
  JSplitPane pricesSplit = new JSplitPane();
  JSplitPane prices2Split = new JSplitPane();
  NavigatorBar navigatorBar = new NavigatorBar();
  BorderLayout borderLayout4 = new BorderLayout();
  JPanel varsPanel = new JPanel();
  ItemVariantsPanel variantsPanel = new ItemVariantsPanel();
  FlowLayout flowLayout4 = new FlowLayout();
  CheckBoxControl checkBoxControl1 = new CheckBoxControl();
  CheckBoxControl checkBoxControl2 = new CheckBoxControl();
  CheckBoxControl checkBoxControl3 = new CheckBoxControl();
  CheckBoxControl checkBoxControl4 = new CheckBoxControl();
  CheckBoxControl checkBoxControl5 = new CheckBoxControl();
  private java.util.List variantsNames = new ArrayList();
  JLabel jLabel1 = new JLabel();
  BorderLayout borderLayout5 = new BorderLayout();
  private VariantBarcodesPanel barcodesPanel = new VariantBarcodesPanel();
  private VariantMinStocksPanel minStocksPanel = new VariantMinStocksPanel();
  LabelControl labelBarcode = new LabelControl();
  TextControl controlBarcode = new TextControl();
  LabelControl labelAvgPurCost = new LabelControl();
  CurrencyControl controlAvgPurCost = new CurrencyControl();
  LabelControl labelLastPurCost = new LabelControl();
  CurrencyControl controlLastPurCost = new CurrencyControl();
  LabelControl labeLstPur = new LabelControl();
  DateControl controlLastPurDate = new DateControl();
  ComboBoxControl controlBarcodeType = new ComboBoxControl();
  private int splitDiv = 250;
  private ProductVariantsPanel variantsPricesPanel = new ProductVariantsPanel(
          new ProductVariantsController() {

            public BigDecimal validateQty(BigDecimal qty) {
              return qty;
            }

            public void qtyUpdated(BigDecimal qty) {
            }
          },
          new Form(),//detailPanel,
          null,//controlItemCode,
          null,//itemController,
          "loadProductVariantsMatrix",
          null,//controlQty,
          prices2Split,
          splitDiv,
          true);
  LabelControl labelMinStock = new LabelControl();
  NumericControl controlMinStock = new NumericControl();
  private CustomizedControls customizedControls = null;
	private ItemSparePartsPanel itemSparePartsPanel = new ItemSparePartsPanel(formPanel);


  public ItemFrame(ItemController controller, boolean productsOnly) {
    try {
      if (productsOnly) {
        bomTabbedPane.setManufactureCodeRequired();
      }


      jbInit();
      setSize(750, 600);
      setMinimumSize(new Dimension(750, 600));


      // link the parent grid to the current Form...
      HashSet pk = new HashSet();
      pk.add("companyCodeSys01ITM01");
      pk.add("itemCodeITM01");
			if (controller.getParentFrame()!=null)
				formPanel.linkGrid(controller.getParentFrame().getGrid(), pk, true, true, true, navigatorBar);

      tab.addChangeListener(new ChangeListener() {

        public void stateChanged(ChangeEvent e) {
//          if (tab.getSelectedIndex()==8) {
          if (tab.getSelectedComponent() != null
                  && tab.getSelectedComponent().equals(getItemVariantsPanel())) {
            DetailItemVO vo = (DetailItemVO) formPanel.getVOModel().getValueObject();
            getItemVariantsPanel().setContent(vo, getVariantsNames());
            getItemVariantsPanel().revalidate();
            getItemVariantsPanel().repaint();
          } //          else if (tab.getSelectedIndex()==9) {
          else if (tab.getSelectedComponent() != null
                  && tab.getSelectedComponent().equals(getVariantBarcodesPanel())) {
            getVariantBarcodesPanel().setItem(formPanel);
            getVariantBarcodesPanel().revalidate();
            getVariantBarcodesPanel().repaint();
          } else if (tab.getSelectedComponent() != null
                  && tab.getSelectedComponent().equals(getVariantMinStockPanel())) {
            //else if (tab.getSelectedIndex()==10) {
            getVariantMinStockPanel().setItem(formPanel);
            getVariantMinStockPanel().revalidate();
            getVariantMinStockPanel().repaint();
          } else if (customizedControls != null
                  && tab.getSelectedComponent() != null
                  && tab.getSelectedComponent().equals(customizedControls)) {
            customizedControls.setContent();
            customizedControls.revalidate();
            customizedControls.repaint();
          } else if (tab.getSelectedComponent() != null
                  && tab.getSelectedComponent().equals(discountsPanel)) {
            discountsGrid.reloadData();
          } else if (tab.getSelectedComponent() != null
                  && tab.getSelectedComponent().equals(pricesSplit)) {
            supplierPrices.getPricesGrid().reloadData();
            pricesGrid.reloadData();
          } else if (tab.getSelectedComponent() != null
                  && tab.getSelectedComponent().equals(docsPanel)) {
            docsPanel.getDocsGrid().reloadData();
          } else if (tab.getSelectedComponent() != null
                  && tab.getSelectedComponent().equals(bookedItemsPanel)) {
            bookedItemsPanel.getGrid().reloadData();
          } else if (tab.getSelectedComponent() != null
                  && tab.getSelectedComponent().equals(orderedItemsPanel)) {
            orderedItemsPanel.getGrid().reloadData();
          } else if (tab.getSelectedComponent() != null
                  && tab.getSelectedComponent().equals(billOfMaterialsPanel)) {
            bomTabbedPane.getAltCompsGrid().reloadData();
            bomTabbedPane.getComponentsGrid().reloadData();
          } else if (tab.getSelectedComponent() != null
						     	&& tab.getSelectedComponent().equals(itemSparePartsPanel)) {
						DetailItemVO vo = (DetailItemVO) formPanel.getVOModel().getValueObject();
						itemSparePartsPanel.init(vo,false);
				   }

        }
      });

      discountsGrid.setAutoLoadData(false);
      supplierPrices.getPricesGrid().setAutoLoadData(false);
      pricesGrid.setAutoLoadData(false);
      docsPanel.getDocsGrid().setAutoLoadData(false);
      bookedItemsPanel.getGrid().setAutoLoadData(false);
      orderedItemsPanel.getGrid().setAutoLoadData(false);
      bomTabbedPane.getAltCompsGrid().setAutoLoadData(false);
      bomTabbedPane.getComponentsGrid().setAutoLoadData(false);

      controlLevel.setLookupController(levelController);
      levelController.setLookupDataLocator(levelDataLocator);
      levelController.setCodeSelectionWindow(levelController.TREE_FRAME);
      levelController.setFrameTitle("hierarchy");
      levelController.setAllowTreeLeafSelectionOnly(true);
      levelController.getLookupDataLocator().setNodeNameAttribute("descriptionSYS10");
      levelController.setLookupValueObjectClassName("org.jallinone.hierarchies.java.HierarchyLevelVO");
      levelController.addLookup2ParentLink("progressiveHIE01", "progressiveHie01ITM01");
      levelController.addLookup2ParentLink("descriptionSYS10", "levelDescriptionSYS10");
      levelDataLocator.setTreeDataLocator(treeLevelDataLocator);
      treeLevelDataLocator.setServerMethodName("loadHierarchy");

      umDataLocator.setGridMethodName("loadMeasures");
      umDataLocator.setValidationMethodName("validateMeasureCode");

      // u.m. selling qty...
      controlUMSellQty.setLookupController(umController);
      controlUMSellQty.setControllerMethodName("getMeasureUnitsList");
      umController.setLookupDataLocator(umDataLocator);
      umController.setFrameTitle("measures");
      umController.setLookupValueObjectClassName("org.jallinone.registers.measure.java.MeasureVO");
      umController.addLookup2ParentLink("umCodeREG02", "minSellingQtyUmCodeReg02ITM01");
      umController.addLookup2ParentLink("decimalsREG02", "minSellingQtyDecimalsREG02");
      umController.setAllColumnVisible(false);
      umController.setVisibleColumn("umCodeREG02", true);
      CustomizedColumns umCust = new CustomizedColumns(ApplicationConsts.ID_UM_GRID, umController);
      umController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {
        }

        public void codeChanged(ValueObject parentVO, Collection parentChangedAttributes) {
          MeasureVO vo = (MeasureVO) umController.getLookupVO();
          if (vo.getUmCodeREG02() != null) {
            controlMinSellQty.setDecimals(vo.getDecimalsREG02().intValue());
            controlMinSellQty.setValue(null);

            if (vo.getDecimalsREG02().doubleValue() > 0) {
              controlSerialNumRequired.setEnabled(false);
            } else if (formPanel.getMode() == Consts.INSERT) {
              controlSerialNumRequired.setEnabled(true);
            }
          } else {
            controlSerialNumRequired.setEnabled(false);
          }
        }

        public void beforeLookupAction(ValueObject parentVO) {
        }

        public void forceValidate() {
        }
      });

      // u.m. gross weight...
      controlUMGW.setLookupController(umGWController);
      controlUMGW.setControllerMethodName("getMeasureUnitsList");
      umGWController.setLookupDataLocator(umDataLocator);
      umGWController.setFrameTitle("measures");
      umGWController.setLookupValueObjectClassName("org.jallinone.registers.measure.java.MeasureVO");
      umGWController.addLookup2ParentLink("umCodeREG02", "grossWeightUmCodeReg02ITM01");
      umGWController.addLookup2ParentLink("decimalsREG02", "grossWeightDecimalsREG02");
      umGWController.setAllColumnVisible(false);
      umGWController.setVisibleColumn("umCodeREG02", true);
      CustomizedColumns umGWCust = new CustomizedColumns(ApplicationConsts.ID_UM_GRID, umGWController);
      umGWController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {
        }

        public void codeChanged(ValueObject parentVO, Collection parentChangedAttributes) {
          MeasureVO vo = (MeasureVO) umGWController.getLookupVO();
          if (vo != null && vo.getDecimalsREG02() != null) {
            controlGW.setDecimals(vo.getDecimalsREG02().intValue());
          }
          controlGW.setValue(null);
        }

        public void beforeLookupAction(ValueObject parentVO) {
        }

        public void forceValidate() {
        }
      });

      // u.m. net weight...
      controlUMNW.setLookupController(umNWController);
      controlUMNW.setControllerMethodName("getMeasureUnitsList");
      umNWController.setLookupDataLocator(umDataLocator);
      umNWController.setFrameTitle("measures");
      umNWController.setLookupValueObjectClassName("org.jallinone.registers.measure.java.MeasureVO");
      umNWController.addLookup2ParentLink("umCodeREG02", "netWeightUmCodeReg02ITM01");
      umNWController.addLookup2ParentLink("decimalsREG02", "netWeightDecimalsREG02");
      umNWController.setAllColumnVisible(false);
      umNWController.setVisibleColumn("umCodeREG02", true);
      CustomizedColumns umNWCust = new CustomizedColumns(ApplicationConsts.ID_UM_GRID, umNWController);
      umNWController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {
        }

        public void codeChanged(ValueObject parentVO, Collection parentChangedAttributes) {
          MeasureVO vo = (MeasureVO) umNWController.getLookupVO();
          controlNW.setDecimals(vo.getDecimalsREG02().intValue());
          controlNW.setValue(null);
        }

        public void beforeLookupAction(ValueObject parentVO) {
        }

        public void forceValidate() {
        }
      });

      // u.m. width...
      controlUMW.setLookupController(umWController);
      controlUMW.setControllerMethodName("getMeasureUnitsList");
      umWController.setLookupDataLocator(umDataLocator);
      umWController.setFrameTitle("measures");
      umWController.setLookupValueObjectClassName("org.jallinone.registers.measure.java.MeasureVO");
      umWController.addLookup2ParentLink("umCodeREG02", "widthUmCodeReg02ITM01");
      umWController.addLookup2ParentLink("decimalsREG02", "widthDecimalsREG02");
      umWController.setAllColumnVisible(false);
      umWController.setVisibleColumn("umCodeREG02", true);
      CustomizedColumns umWCust = new CustomizedColumns(ApplicationConsts.ID_UM_GRID, umWController);
      umWController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {
        }

        public void codeChanged(ValueObject parentVO, Collection parentChangedAttributes) {
          MeasureVO vo = (MeasureVO) umWController.getLookupVO();
          controlW.setDecimals(vo.getDecimalsREG02().intValue());
          controlW.setValue(null);
        }

        public void beforeLookupAction(ValueObject parentVO) {
        }

        public void forceValidate() {
        }
      });

      // u.m. height...
      controlUMH.setLookupController(umHController);
      controlUMH.setControllerMethodName("getMeasureUnitsList");
      umHController.setLookupDataLocator(umDataLocator);
      umHController.setFrameTitle("measures");
      umHController.setLookupValueObjectClassName("org.jallinone.registers.measure.java.MeasureVO");
      umHController.addLookup2ParentLink("umCodeREG02", "heightUmCodeReg02ITM01");
      umHController.addLookup2ParentLink("decimalsREG02", "heightDecimalsREG02");
      umHController.setAllColumnVisible(false);
      umHController.setVisibleColumn("umCodeREG02", true);
      CustomizedColumns umHCust = new CustomizedColumns(ApplicationConsts.ID_UM_GRID, umHController);
      umHController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {
        }

        public void codeChanged(ValueObject parentVO, Collection parentChangedAttributes) {
          MeasureVO vo = (MeasureVO) umHController.getLookupVO();
          controlH.setDecimals(vo.getDecimalsREG02().intValue());
          controlH.setValue(null);
        }

        public void beforeLookupAction(ValueObject parentVO) {
        }

        public void forceValidate() {
        }
      });

      // lookup vat...
      vatDataLocator.setGridMethodName("loadVats");
      vatDataLocator.setValidationMethodName("validateVatCode");
      controlVat.setLookupController(vatController);
      controlVat.setControllerMethodName("getVatsList");
      vatController.setLookupDataLocator(vatDataLocator);
      vatController.setFrameTitle("vats");
      vatController.setLookupValueObjectClassName("org.jallinone.registers.vat.java.VatVO");
      vatController.addLookup2ParentLink("vatCodeREG01", "vatCodeReg01ITM01");
      vatController.addLookup2ParentLink("descriptionSYS10", "vatDescriptionSYS10");
      vatController.addLookup2ParentLink("valueREG01", "vatValueREG01");
      vatController.addLookup2ParentLink("deductibleREG01", "vatDeductibleREG01");
      vatController.setAllColumnVisible(false);
      vatController.setVisibleColumn("vatCodeREG01", true);
      vatController.setVisibleColumn("descriptionSYS10", true);
      vatController.setVisibleColumn("valueREG01", true);
      vatController.setVisibleColumn("deductibleREG01", true);
      vatController.setPreferredWidthColumn("descriptionSYS10", 200);
      vatController.setFramePreferedSize(new Dimension(510, 400));
      CustomizedColumns vatCust = new CustomizedColumns(new BigDecimal(162), vatController);


      init();

      formPanel.setFormController(controller);
      formPanel.addLinkedPanel(imgPanel);

      discountsGrid.setController(new DiscountsController(this));
      discountsGrid.setGridDataLocator(discountsGridDataLocator);
      discountsGridDataLocator.setServerMethodName("loadItemDiscounts");

      // currency lookup...
      currencyDataLocator.setGridMethodName("loadCurrencies");
      currencyDataLocator.setValidationMethodName("validateCurrencyCode");
      colCurrencyCode.setLookupController(currencyController);
      colCurrencyCode.setControllerMethodName("getCurrenciesList");
      currencyController.setLookupDataLocator(currencyDataLocator);
      currencyController.setFrameTitle("currencies");
      currencyController.setLookupValueObjectClassName("org.jallinone.registers.currency.java.CurrencyVO");
      currencyController.addLookup2ParentLink("currencyCodeREG03", "currencyCodeReg03SAL03");
      currencyController.setAllColumnVisible(false);
      currencyController.setVisibleColumn("currencyCodeREG03", true);
      currencyController.setVisibleColumn("currencySymbolREG03", true);
      new CustomizedColumns(new BigDecimal(182), currencyController);

      pricesGrid.setController(new PricesController(this));
      pricesGrid.setGridDataLocator(pricesGridDataLocator);
      pricesGridDataLocator.setServerMethodName("loadPrices");

      // pricelist lookup...
      pricelistDataLocator.setGridMethodName("loadPricelists");
      pricelistDataLocator.setValidationMethodName("validatePricelistCode");
      colPricelistCode.setLookupController(pricelistController);
      colPricelistCode.setControllerMethodName("getSalePricesList");
      pricelistController.setLookupDataLocator(pricelistDataLocator);
      pricelistController.setFrameTitle("pricelists");
      pricelistController.setLookupValueObjectClassName("org.jallinone.sales.pricelist.java.PricelistVO");
      pricelistController.addLookup2ParentLink("pricelistCodeSAL01", "pricelistCodeSal01SAL02");
      pricelistController.addLookup2ParentLink("descriptionSYS10", "pricelistDescriptionSYS10");
      pricelistController.setAllColumnVisible(false);
      pricelistController.setVisibleColumn("pricelistCodeSAL01", true);
      pricelistController.setVisibleColumn("descriptionSYS10", true);
      new CustomizedColumns(new BigDecimal(302), pricelistController);
      pricelistController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {
        }

        public void codeChanged(ValueObject parentVO, Collection parentChangedAttributes) {
        }

        public void beforeLookupAction(ValueObject parentVO) {
          DetailItemVO itemVO = (DetailItemVO) getFormPanel().getVOModel().getValueObject();
          pricelistDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01, itemVO.getCompanyCodeSys01ITM01());
          pricelistDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01, itemVO.getCompanyCodeSys01ITM01());
        }

        public void forceValidate() {
        }
      });


      if (controller.getPK() != null) // read only...
      {
        setVariants(controller.getPK().getCompanyCodeSys01ITM01());
      }


			if (controller.getParentFrame()!=null) {
				HierarchyLevelVO levelVO = (HierarchyLevelVO) controller.getParentFrame().getHierarTreePanel().getSelectedNode().getUserObject();
				customizedControls = new CustomizedControls(tab, formPanel, levelVO.getProgressiveHie01HIE02());
			}

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public final void setVariants(String companyCodeSys01) {
    Response res = ClientUtils.getData("loadVariantsNames", companyCodeSys01);
    if (!res.isError()) {
      variantsNames = ((VOListResponse) res).getRows();
      VariantNameVO vo = null;
      boolean atLeastOne = false;
      CheckBoxControl control = null;
      for (int i = 0; i < variantsNames.size(); i++) {
        vo = (VariantNameVO) variantsNames.get(i);

        switch (i) {
          case 0:
            control = checkBoxControl1;
            break;
          case 1:
            control = checkBoxControl2;
            break;
          case 2:
            control = checkBoxControl3;
            break;
          case 3:
            control = checkBoxControl4;
            break;
          case 4:
            control = checkBoxControl5;
            break;
        }

        if (vo.getDescriptionSYS10().equals(ApplicationConsts.JOLLY)) {
          control.setVisible(false);
          control.setText("");
        } else {
          control.setVisible(true);
          control.setText(vo.getDescriptionSYS10());
          atLeastOne = true;
        }
      } // end for

      if (atLeastOne) {
        tab.remove(getItemVariantsPanel());
        tab.remove(getVariantBarcodesPanel());
        tab.remove(getVariantMinStockPanel());
        tab.add(getItemVariantsPanel(), 9);
        tab.add(getVariantBarcodesPanel(), 10);
        tab.add(getVariantMinStockPanel(), 11);
        tab.setTitleAt(9, ClientSettings.getInstance().getResources().getResource("variantsPanel"));
        tab.setTitleAt(10, ClientSettings.getInstance().getResources().getResource("barcodesPanel"));
        tab.setTitleAt(11, ClientSettings.getInstance().getResources().getResource("min stock"));

//         tab.setIconAt(8,new ImageIcon(ClientUtils.getImage("colors.gif")));
//         tab.setIconAt(9,new ImageIcon(ClientUtils.getImage("barcode.gif")));
//         tab.setIconAt(10,new ImageIcon(ClientUtils.getImage("open.gif")));
        tab.revalidate();
        tab.repaint();
      } else {
        tab.remove(getItemVariantsPanel());
        tab.remove(getVariantBarcodesPanel());
        tab.remove(getVariantMinStockPanel());
        tab.revalidate();
      }

      varsPanel.revalidate();
      varsPanel.repaint();

    } // end if on res.isError (variants...)
  }

  public final Domain getItemTypes() {
    return d;
  }

  public ProductVariantsPanel getVariantsPricesPanel() {
    return variantsPricesPanel;
  }

  /**
   * Callback method called when the data loading is completed.
   * @param error <code>true</code> if an error occours during data loading, <code>false</code> if data loading is successfully completed
   */
  public final void loadDataCompleted(boolean error, ItemPK pk) {
    if (error) {
      return;
    }

    DetailItemVO vo = (DetailItemVO) formPanel.getVOModel().getValueObject();
    controlMinSellQty.setDecimals(vo.getMinSellingQtyDecimalsREG02().intValue());
    if (vo.getGrossWeightDecimalsREG02() != null) {
      controlGW.setDecimals(vo.getGrossWeightDecimalsREG02().intValue());
    }
    if (vo.getNetWeightDecimalsREG02() != null) {
      controlNW.setDecimals(vo.getNetWeightDecimalsREG02().intValue());
    }
    if (vo.getWidthDecimalsREG02() != null) {
      controlW.setDecimals(vo.getWidthDecimalsREG02().intValue());
    }
    if (vo.getHeightDecimalsREG02() != null) {
      controlH.setDecimals(vo.getHeightDecimalsREG02().intValue());
    }
    if (vo.getSmallImage() != null) {
      smallImage.setImage(vo.getSmallImage());
    } else {
      smallImage.setImage(null);
    }
    if (vo.getLargeImage() != null) {
      largeImage.setImage(vo.getLargeImage());
    } else {
      largeImage.setImage(null);
    }

    discountsGrid.getOtherGridParams().put(
            ApplicationConsts.ITEM_PK,
            pk);
    discountsGrid.reloadData();
    setButtonsEnabled(true);

    variantsPricesPanel.removeAll();
    pricesGrid.getOtherGridParams().put(ApplicationConsts.ITEM, vo);
    pricesGrid.reloadData();

    getBookedItemsPanel().setEnabled(true);
    getOrderedItemsPanel().setEnabled(true);
    getBookedItemsPanel().getGrid().reloadData();
    getOrderedItemsPanel().getGrid().reloadData();
    getBookedItemsPanel().getGrid().getOtherGridParams().put(ApplicationConsts.ITEM_PK, new ItemPK(vo.getCompanyCodeSys01ITM01(), vo.getItemCodeITM01()));
    getOrderedItemsPanel().getGrid().getOtherGridParams().put(ApplicationConsts.ITEM_PK, new ItemPK(vo.getCompanyCodeSys01ITM01(), vo.getItemCodeITM01()));

    supplierPrices.getPricesGrid().getOtherGridParams().put(ApplicationConsts.ITEM_PK, new ItemPK(vo.getCompanyCodeSys01ITM01(), vo.getItemCodeITM01()));
    supplierPrices.setButtonsEnabled(true);
    supplierPrices.getPricesGrid().reloadData();

    getDocsPanel().setItemVO(vo);
    getDocsPanel().getDocsGrid().getOtherGridParams().put(ApplicationConsts.ITEM_PK, pk);
    getDocsPanel().getDocsGrid().reloadData();

    getBomTabbedPane().loadDataCompleted(error, pk);

    setBarcodeControl();
    setMinStockControl();

    if (vo.getLastPurchaseCostDecimals() != null) {
      controlLastPurCost.setDecimals(vo.getLastPurchaseCostDecimals().intValue());
      controlLastPurCost.setCurrencySymbol(vo.getLastPurchaseCostCurrencySymbol());
      controlLastPurCost.setDecimalSymbol(vo.getLastPurchaseCostDecimalSymbol().charAt(0));
      controlLastPurCost.setGroupingSymbol(vo.getLastPurchaseCostThousandSymbol().charAt(0));

      controlAvgPurCost.setDecimals(vo.getLastPurchaseCostDecimals().intValue());
      controlAvgPurCost.setCurrencySymbol(vo.getLastPurchaseCostCurrencySymbol());
      controlAvgPurCost.setDecimalSymbol(vo.getLastPurchaseCostDecimalSymbol().charAt(0));
      controlAvgPurCost.setGroupingSymbol(vo.getLastPurchaseCostThousandSymbol().charAt(0));
    }

    this.setTitle(ClientSettings.getInstance().getResources().getResource("item detail") + " - " + vo.getItemCodeITM01() + " - " + vo.getDescriptionSYS10());

//    if (tab.getSelectedIndex()==8) {
    if (tab.getSelectedComponent() != null
            && tab.getSelectedComponent().equals(getItemVariantsPanel())) {
      getItemVariantsPanel().setContent(vo, getVariantsNames());
      getItemVariantsPanel().revalidate();
      getItemVariantsPanel().repaint();
    } //    else if (tab.getSelectedIndex()==9) {
    else if (tab.getSelectedComponent() != null
            && tab.getSelectedComponent().equals(getVariantBarcodesPanel())) {
      getVariantBarcodesPanel().setItem(formPanel);
      getVariantBarcodesPanel().revalidate();
      getVariantBarcodesPanel().repaint();
    } //    else if (tab.getSelectedIndex()==10) {
    else if (tab.getSelectedComponent() != null
            && tab.getSelectedComponent().equals(getVariantMinStockPanel())) {
      getVariantMinStockPanel().setItem(formPanel);
      getVariantMinStockPanel().revalidate();
      getVariantMinStockPanel().repaint();
    } else if (customizedControls != null
            && tab.getSelectedComponent() != null
            && tab.getSelectedComponent().equals(customizedControls)) {
      customizedControls.setContent();
      customizedControls.revalidate();
      customizedControls.repaint();
    }
		else if (tab.getSelectedComponent() != null
						&& tab.getSelectedComponent().equals(getItemSparePartsPanel())) {
			getItemSparePartsPanel().init(vo, false);
		} //    else if (tab.getSelectedIndex()==9) {
  }

  /**
   * Retrieve item types and fill in the item types combo box.
   */
  private void init() {
    Response res = ClientUtils.getData("loadItemTypes", new GridParams());
    if (!res.isError()) {
      ItemTypeVO vo = null;
      java.util.List list = ((VOListResponse) res).getRows();
      for (int i = 0; i < list.size(); i++) {
        vo = (ItemTypeVO) list.get(i);
        d.addDomainPair(vo.getProgressiveHie02ITM02(), vo.getDescriptionSYS10());
      }
    }
    controlItemType.setDomain(d);
    controlItemType.getComboBox().addItemListener(new ItemListener() {

      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == e.SELECTED) {
//          formPanel.getBinding((ValueObject)formPanel.getVOModel().getValueObject(),"progressiveHie02ITM01").push();
/*
          if (formPanel.getMode()==Consts.EDIT || formPanel.getMode()==Consts.INSERT) {
          controlLevel.getCodBox().setText(null);
          controlLevelDescr.setText("");
          }
           */
          treeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.PROGRESSIVE_HIE02, controlItemType.getValue());
        }
      }
    });
    if (d.getDomainPairList().length == 1) {
      controlItemType.getComboBox().setSelectedIndex(0);
    } else {
      controlItemType.getComboBox().setSelectedIndex(-1);
    }
  }

  private void jbInit() throws Exception {
    pricesGrid.setVisibleStatusPanel(false);
    pricesSplit.setOrientation(JSplitPane.VERTICAL_SPLIT);
    prices2Split.setOrientation(JSplitPane.VERTICAL_SPLIT);
    discountsGrid.setMaxNumberOfRowsOnInsert(50);
    discountsGrid.setValueObjectClassName("org.jallinone.sales.discounts.java.ItemDiscountVO");

    pricesGrid.setMaxNumberOfRowsOnInsert(50);

    this.setTitle(ClientSettings.getInstance().getResources().getResource("item detail"));
    formPanel.setVOClassName("org.jallinone.items.java.DetailItemVO");
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    formPanel.setLayout(gridBagLayout1);
    insertButton1.setText("insertButton1");
    editButton1.setText("editButton1");
    saveButton1.setText("saveButton1");
    reloadButton1.setText("reloadButton1");
    deleteButton1.setText("deleteButton1");
    formPanel.setInsertButton(insertButton1);
    formPanel.setCopyButton(copyButton1);
    formPanel.setEditButton(editButton1);
    formPanel.setReloadButton(reloadButton1);
    formPanel.setDeleteButton(deleteButton1);
    formPanel.setSaveButton(saveButton1);
    formPanel.setFunctionId("ITM01");
    labelItemCode.setText("itemCodeITM01");
    labelDescr.setText("descriptionSYS10");
    labelAddDescr.setText("addDescr");
    labelitemType.setText("item type");
    labelLevel.setText("level");
    labelMinSellQty.setText("minSellingQtyITM01");
    labelVat.setText("vat");
    labelGW.setText("grossWeightITM01");
    labelNW.setText("netWeightITM01");
    labelW.setText("widthITM01");
    labelH.setText("heightITM01");
    labelNote.setText("note");
    controlNote.setAttributeName("noteITM01");
    controlNote.setCanCopy(true);
    controlNote.setLinkLabel(labelNote);
    controlNote.setMaxCharacters(2000);
    controlNoWarehouseMov.setAttributeName("noWarehouseMovITM01");
    controlNoWarehouseMov.setCanCopy(true);
    controlNoWarehouseMov.setEnabledOnInsert(true);
    controlNoWarehouseMov.setEnabledOnEdit(false);
    controlNoWarehouseMov.setPreferredSize(new java.awt.Dimension(259, 17));
    controlNoWarehouseMov.setText(ClientSettings.getInstance().getResources().getResource("no warehouse movements"));
    controlNoWarehouseMov.addItemListener(new ItemFrame_controlNoWarehouseMov_itemAdapter(this));
    controlItemCode.setAttributeName("itemCodeITM01");
    controlItemCode.setCanCopy(false);
    controlItemCode.setLinkLabel(labelItemCode);
    controlItemCode.setMaxCharacters(20);
    controlItemCode.setRequired(true);
    controlItemCode.setTrimText(true);
    controlItemCode.setUpperCase(true);
    controlItemCode.setEnabledOnEdit(false);
    controlDescr.setAttributeName("descriptionSYS10");
    controlDescr.setCanCopy(true);
    controlDescr.setLinkLabel(labelDescr);
    controlDescr.setRequired(true);
    controlItemType.setAttributeName("progressiveHie02ITM01");
    controlItemType.setCanCopy(true);
    controlItemType.setLinkLabel(labelitemType);
    controlItemType.setRequired(true);
    controlItemType.setEnabledOnInsert(false);
    controlItemType.setEnabledOnEdit(false);
    controlLevel.setAttributeName("progressiveHie01ITM01");
    controlLevel.setCanCopy(true);
    controlLevel.setCodBoxVisible(false);
    controlLevel.setColumns(0);
    controlLevel.setLinkLabel(labelLevel);
    controlLevel.setRequired(true);
    controlLevel.setAllowOnlyNumbers(true);
    controlLevelDescr.setAttributeName("levelDescriptionSYS10");
    controlLevelDescr.setCanCopy(true);
    controlLevelDescr.setEnabledOnInsert(false);
    controlLevelDescr.setEnabledOnEdit(false);
    controlAddDescr.setAttributeName("addDescriptionSYS10");
    controlAddDescr.setCanCopy(true);
    controlAddDescr.setLinkLabel(labelAddDescr);
    controlMinSellQty.setAttributeName("minSellingQtyITM01");
    controlMinSellQty.setCanCopy(true);
    controlMinSellQty.setDecimals(5);
    controlMinSellQty.setLinkLabel(labelMinSellQty);
    controlMinSellQty.setRequired(true);
    controlUMSellQty.setAttributeName("minSellingQtyUmCodeReg02ITM01");
    controlUMSellQty.setCanCopy(true);
    controlUMSellQty.setLinkLabel(labelMinSellQty);
    controlUMSellQty.setMaxCharacters(20);
    controlUMSellQty.setColumns(5);
    controlUMSellQty.setRequired(true);
    controlVat.setAttributeName("vatCodeReg01ITM01");
    controlVat.setCanCopy(true);
    controlVat.setColumns(10);
    controlVat.setLinkLabel(labelVat);
    controlVat.setMaxCharacters(20);
    controlVat.setRequired(true);
    controlVatDescr.setAttributeName("vatDescriptionSYS10");
    controlVatDescr.setLinkLabel(labelVat);
    controlVatDescr.setEnabledOnInsert(false);
    controlVatDescr.setEnabledOnEdit(false);
    controlVatValue.setAttributeName("vatValueREG01");
    controlVatValue.setCanCopy(true);
    controlVatValue.setDecimals(5);
    controlVatValue.setEnabledOnInsert(false);
    controlVatValue.setEnabledOnEdit(false);
    controlGW.setAttributeName("grossWeightITM01");
    controlGW.setCanCopy(true);
    controlGW.setDecimals(5);
    controlGW.setLinkLabel(labelGW);
    controlUMGW.setAttributeName("grossWeightUmCodeReg02ITM01");
    controlUMGW.setCanCopy(true);
    controlUMGW.setColumns(5);
    controlUMGW.setLinkLabel(labelGW);
    controlUMGW.setMaxCharacters(20);
    controlNW.setAttributeName("netWeightITM01");
    controlNW.setCanCopy(true);
    controlNW.setColumns(10);
    controlNW.setDecimals(5);
    controlNW.setLinkLabel(labelNW);
    controlUMNW.setAttributeName("netWeightUmCodeReg02ITM01");
    controlUMNW.setCanCopy(true);
    controlUMNW.setColumns(5);
    controlUMNW.setLinkLabel(labelNW);
    controlUMNW.setMaxCharacters(20);
    controlW.setAttributeName("widthITM01");
    controlW.setCanCopy(true);
    controlW.setDecimals(5);
    controlW.setLinkLabel(labelW);
    controlUMW.setAttributeName("widthUmCodeReg02ITM01");
    controlUMW.setCanCopy(true);
    controlUMW.setColumns(5);
    controlUMW.setLinkLabel(labelW);
    controlUMW.setMaxCharacters(20);
    controlH.setAttributeName("heightITM01");
    controlH.setCanCopy(true);
    controlH.setDecimals(5);
    controlH.setLinkLabel(labelH);
    controlUMH.setAttributeName("heightUmCodeReg02ITM01");
    controlUMH.setCanCopy(true);
    controlUMH.setColumns(5);
    controlUMH.setLinkLabel(labelH);
    controlUMH.setMaxCharacters(20);
    imgPanel.setLayout(gridBagLayout2);
    smallImageButton.setText(ClientSettings.getInstance().getResources().getResource("load small image"));
    smallImageButton.addActionListener(new ItemFrame_smallImageButton_actionAdapter(this));
    largeImageButton.setText(ClientSettings.getInstance().getResources().getResource("load large image"));
    largeImageButton.addActionListener(new ItemFrame_largeImageButton_actionAdapter(this));
    clearSmallImageButton.setText(ClientSettings.getInstance().getResources().getResource("clear small image"));
    clearSmallImageButton.addActionListener(new ItemFrame_clearSmallImageButton_actionAdapter(this));
    clearLargeImageButton.setText(ClientSettings.getInstance().getResources().getResource("clear large image"));
    clearLargeImageButton.addActionListener(new ItemFrame_clearLargeImageButton_actionAdapter(this));
    discountsPanel.setLayout(borderLayout1);
    discountsGrid.setAutoLoadData(false);
    discountsGrid.setDeleteButton(deleteButton2);
    discountsGrid.setEditButton(editButton2);
    discountsGrid.setExportButton(exportButton1);
    discountsGrid.setFunctionId("SAL03");
    discountsGrid.setInsertButton(insertButton2);
    discountsGrid.setNavBar(navigatorBar1);
    discountsGrid.setReloadButton(reloadButton2);
    discountsGrid.setSaveButton(saveButton2);
    discountsButtonsPanel.setLayout(flowLayout2);
    flowLayout2.setAlignment(FlowLayout.LEFT);
    insertButton2.setText("insertButton2");
    editButton2.setText("editButton2");
    saveButton2.setText("saveButton2");
    reloadButton2.setText("reloadButton2");
    deleteButton2.setText("deleteButton2");
    exportButton1.setText("exportButton1");
    pricesPanel.setLayout(borderLayout2);
    pricesGrid.setAutoLoadData(false);
    pricesGrid.setCopyButton(copyButton2);
    pricesGrid.setDeleteButton(deleteButton3);
    pricesGrid.setEditButton(editButton3);
    pricesGrid.setExportButton(exportButton2);
    pricesGrid.setFunctionId("SAL01");
    pricesGrid.setInsertButton(insertButton3);
    pricesGrid.setNavBar(navigatorBar2);
    pricesGrid.setReloadButton(reloadButton3);
    pricesGrid.setSaveButton(saveButton3);
    pricesGrid.setValueObjectClassName("org.jallinone.sales.pricelist.java.PriceVO");
    pricesButtonsPanel.setLayout(flowLayout3);
    flowLayout3.setAlignment(FlowLayout.LEFT);
    insertButton3.setText("insertButton3");
    reloadButton3.setText("reloadButton3");
    deleteButton3.setText("deleteButton3");
    exportButton2.setText("exportButton2");
    saveButton3.setText("saveButton3");
    editButton3.setText("editButton3");
    colPricelistCode.setColumnFilterable(true);
    colPricelistCode.setColumnSortable(true);
    colPricelistCode.setEditableOnInsert(true);
    colPricelistCode.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colPricelistCode.setMaxCharacters(20);
    colPricelistCode.setColumnName("pricelistCodeSal01SAL02");
    colPricelistDescr.setPreferredWidth(250);
    colPricelistDescr.setColumnName("pricelistDescriptionSYS10");
    controlVatDeductible.setAttributeName("vatDeductibleREG01");
    controlVatDeductible.setCanCopy(true);
    controlVatDeductible.setEnabledOnInsert(false);
    controlVatDeductible.setEnabledOnEdit(false);
    controlSerialNumRequired.setCanCopy(true);
    controlSerialNumRequired.setEnabledOnEdit(false);
    controlSerialNumRequired.setAttributeName("serialNumberRequiredITM01");
    controlSerialNumRequired.setText("serial number required");
    billOfMaterialsPanel.setLayout(borderLayout3);
    checkBoxControl1.setEnabledOnEdit(false);
    checkBoxControl1.addItemListener(new ItemFrame_checkBoxControl1_itemAdapter(this));

    checkBoxControl1.setAttributeName("useVariant1ITM01");
    checkBoxControl2.setAttributeName("useVariant2ITM01");
    checkBoxControl3.setAttributeName("useVariant3ITM01");
    checkBoxControl4.setAttributeName("useVariant4ITM01");
    checkBoxControl5.setAttributeName("useVariant5ITM01");

    varsPanel.setLayout(flowLayout4);
    flowLayout4.setAlignment(FlowLayout.LEFT);
    checkBoxControl2.setEnabledOnEdit(false);
    checkBoxControl2.addItemListener(new ItemFrame_checkBoxControl2_itemAdapter(this));
    checkBoxControl3.setEnabledOnEdit(false);
    checkBoxControl3.addItemListener(new ItemFrame_checkBoxControl3_itemAdapter(this));
    checkBoxControl4.setEnabledOnEdit(false);
    checkBoxControl4.addItemListener(new ItemFrame_checkBoxControl4_itemAdapter(this));
    checkBoxControl5.setEnabledOnEdit(false);
    checkBoxControl5.addItemListener(new ItemFrame_checkBoxControl5_itemAdapter(this));
    jLabel1.setText("100 x 100 pixels");
    labelBarcode.setText("barcode");
    labelAvgPurCost.setText("avg. purchase cost");
    labelLastPurCost.setText("last purchase cost");
    labeLstPur.setText("last purchase date");
    controlLastPurDate.setAttributeName("lastPurchaseDate");
    controlLastPurDate.setEnabledOnInsert(false);
    controlLastPurDate.setEnabledOnEdit(false);
    controlLastPurCost.setAttributeName("lastPurchaseCost");
    controlLastPurCost.setEnabledOnInsert(false);
    controlLastPurCost.setEnabledOnEdit(false);
    controlAvgPurCost.setAttributeName("avgPurchaseCost");
    controlAvgPurCost.setEnabledOnInsert(false);
    controlAvgPurCost.setEnabledOnEdit(false);
    controlBarcodeType.setAttributeName("barcodeTypeITM01");
    controlBarcodeType.setDomainId("BARCODE_TYPES");
    controlBarcodeType.setRequired(true);
    labelMinStock.setText("min stock");
    controlMinStock.setAttributeName("minStockITM23");
    controlMinStock.setDecimals(5);
    controlBarcode.setMaxCharacters(1000);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    this.getContentPane().add(tab, BorderLayout.CENTER);
    tab.add(formPanel, "detailPanel");
//    tab.setIconAt(0,new ImageIcon(ClientUtils.getImage("items.gif")));
    formPanel.add(labelItemCode, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(controlItemCode, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(labelDescr, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(controlDescr, new GridBagConstraints(3, 0, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(labelAddDescr, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(controlAddDescr, new GridBagConstraints(1, 2, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(labelitemType, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(controlItemType, new GridBagConstraints(1, 1, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(labelVat, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(controlVat, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 50, 0));
    formPanel.add(labelGW, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(controlGW, new GridBagConstraints(1, 4, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(controlUMGW, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(labelNW, new GridBagConstraints(3, 4, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(controlNW, new GridBagConstraints(6, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 50, 0));
    formPanel.add(controlUMNW, new GridBagConstraints(5, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(labelW, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(controlW, new GridBagConstraints(1, 5, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(controlUMW, new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(labelH, new GridBagConstraints(3, 5, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(controlH, new GridBagConstraints(6, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 50, 0));
    formPanel.add(controlUMH, new GridBagConstraints(5, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 40, 0));
    formPanel.add(controlVatValue, new GridBagConstraints(4, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(controlVatDescr, new GridBagConstraints(2, 3, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    formPanel.add(labelNote, new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(controlNote, new GridBagConstraints(1, 9, 6, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    tab.add(imgPanel, "imagePanel");
//    tab.setIconAt(1,new ImageIcon(ClientUtils.getImage("chart.gif")));
    imgPanel.add(smallImageButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 0), 0, 0));
    imgPanel.add(smallImage, new GridBagConstraints(2, 0, 1, 2, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 100, 100));
    imgPanel.add(largeImageButton, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
    imgPanel.add(largeImage, new GridBagConstraints(2, 2, 1, 2, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    buttonsPanel.add(insertButton1, null);
    buttonsPanel.add(copyButton1, null);
    buttonsPanel.add(editButton1, null);
    buttonsPanel.add(saveButton1, null);
    buttonsPanel.add(reloadButton1, null);
    buttonsPanel.add(deleteButton1, null);
    buttonsPanel.add(navigatorBar, null);
    formPanel.add(controlUMSellQty, new GridBagConstraints(5, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(controlMinSellQty, new GridBagConstraints(6, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(labelMinSellQty, new GridBagConstraints(3, 2, 2, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 110, 0));
    formPanel.add(controlVatDeductible, new GridBagConstraints(5, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(labelLevel, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(controlSerialNumRequired, new GridBagConstraints(6, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 120, 0));
    formPanel.add(varsPanel, new GridBagConstraints(0, 8, 7, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    imgPanel.add(clearSmallImageButton, new GridBagConstraints(1, 0, 1, 2, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    imgPanel.add(clearLargeImageButton, new GridBagConstraints(1, 2, 1, 2, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    imgPanel.add(jLabel1, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    tab.add(discountsPanel, "discountPanel");
//    tab.setIconAt(2,new ImageIcon(ClientUtils.getImage("calc.gif")));
    discountsPanel.add(discountsButtonsPanel, BorderLayout.NORTH);
    discountsPanel.add(discountsGrid, BorderLayout.CENTER);
    discountsButtonsPanel.add(insertButton2, null);
    discountsButtonsPanel.add(editButton2, null);
    discountsButtonsPanel.add(saveButton2, null);
    discountsButtonsPanel.add(reloadButton2, null);
    discountsButtonsPanel.add(deleteButton2, null);
    discountsButtonsPanel.add(exportButton1, null);
    discountsButtonsPanel.add(navigatorBar1, null);

    colDiscountCode.setMaxCharacters(20);
    colDiscountCode.setTrimText(true);
    colDiscountCode.setUpperCase(true);
    colDiscountCode.setColumnFilterable(true);
    colDiscountCode.setColumnName("discountCodeSAL03");
    colDiscountCode.setColumnSortable(true);
    colDiscountCode.setEditableOnInsert(true);
    colDiscountCode.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colDiscountCode.setSortingOrder(0);
    colDescr.setColumnDuplicable(true);
    colDescr.setColumnFilterable(true);
    colDescr.setColumnName("descriptionSYS10");
    colDescr.setColumnSortable(true);
    colDescr.setEditableOnEdit(true);
    colDescr.setEditableOnInsert(true);
    colDescr.setPreferredWidth(200);
    colDescr.setHeaderColumnName("discountDescription");
    colCurrencyCode.setColumnDuplicable(true);
    colCurrencyCode.setColumnFilterable(true);
    colCurrencyCode.setColumnName("currencyCodeReg03SAL03");
    colCurrencyCode.setColumnSortable(true);
    colCurrencyCode.setEditableOnEdit(true);
    colCurrencyCode.setEditableOnInsert(true);
    colCurrencyCode.setMaxCharacters(20);
    colCurrencyCode.setPreferredWidth(70);
    colMinValue.setDecimals(2);
    colMinValue.setMinValue(0.0);
    colMinValue.setColumnDuplicable(true);
    colMinValue.setColumnRequired(false);
    colMinValue.setEditableOnEdit(true);
    colMinValue.setColumnName("minValueSAL03");
    colMinValue.setEditableOnInsert(true);
    colMinValue.setPreferredWidth(80);
    colMaxValue.setDecimals(2);
    colMaxValue.setMinValue(0.0);
    colMaxValue.setColumnDuplicable(true);
    colMaxValue.setColumnRequired(false);
    colMaxValue.setEditableOnEdit(true);
    colMaxValue.setEditableOnInsert(true);
    colMaxValue.setColumnName("maxValueSAL03");
    colMaxValue.setPreferredWidth(80);
    colMinPerc.setDecimals(2);
    colMinPerc.setGrouping(false);
    colMinPerc.setMaxValue(100.0);
    colMinPerc.setMinValue(0.0);
    colMinPerc.setColumnDuplicable(true);
    colMinPerc.setColumnFilterable(false);
    colMinPerc.setColumnRequired(false);
    colMinPerc.setEditableOnEdit(true);
    colMinPerc.setEditableOnInsert(true);
    colMinPerc.setPreferredWidth(70);
    colMinPerc.setColumnName("minPercSAL03");
    colMaxPerc.setDecimals(2);
    colMaxPerc.setGrouping(false);
    colMaxPerc.setMaxValue(100.0);
    colMaxPerc.setMinValue(0.0);
    colMaxPerc.setColumnDuplicable(true);
    colMaxPerc.setColumnRequired(false);
    colMaxPerc.setEditableOnEdit(true);
    colMaxPerc.setEditableOnInsert(true);
    colMaxPerc.setPreferredWidth(70);
    colMaxPerc.setColumnName("maxPercSAL03");
    colStartDate.setColumnDuplicable(true);
    colStartDate.setColumnFilterable(true);
    colStartDate.setColumnSortable(true);
    colStartDate.setEditableOnEdit(true);
    colStartDate.setEditableOnInsert(true);
    colStartDate.setColumnName("startDateSAL03");
    colEndDate.setColumnDuplicable(true);
    colEndDate.setColumnFilterable(true);
    colEndDate.setColumnSortable(true);
    colEndDate.setEditableOnEdit(true);
    colEndDate.setEditableOnInsert(true);
    colEndDate.setColumnName("endDateSAL03");

    colMinQty.setColumnDuplicable(true);
    colMinQty.setColumnFilterable(true);
    colMinQty.setColumnSortable(true);
    colMinQty.setEditableOnEdit(true);
    colMinQty.setEditableOnInsert(true);
    colMinQty.setColumnRequired(true);
    colMinQty.setMinValue(1);
    colMinQty.setPreferredWidth(50);
    colMinQty.setColumnName("minQtySAL03");

    colMultipleQty.setColumnDuplicable(true);
    colMultipleQty.setColumnFilterable(true);
    colMultipleQty.setColumnSortable(true);
    colMultipleQty.setEditableOnEdit(true);
    colMultipleQty.setEditableOnInsert(true);
    colMultipleQty.setColumnRequired(false);
    colMultipleQty.setPreferredWidth(50);
    colMultipleQty.setColumnName("multipleQtySAL03");

    discountsGrid.getColumnContainer().add(colDiscountCode, null);
    discountsGrid.getColumnContainer().add(colDescr, null);
    discountsGrid.getColumnContainer().add(colCurrencyCode, null);
    discountsGrid.getColumnContainer().add(colMinValue, null);
    discountsGrid.getColumnContainer().add(colMaxValue, null);
    discountsGrid.getColumnContainer().add(colMinPerc, null);
    discountsGrid.getColumnContainer().add(colMaxPerc, null);
    discountsGrid.getColumnContainer().add(colStartDate, null);
    discountsGrid.getColumnContainer().add(colEndDate, null);
    discountsGrid.getColumnContainer().add(colMinQty, null);
    discountsGrid.getColumnContainer().add(colMultipleQty, null);
    prices2Split.add(pricesPanel, JSplitPane.TOP);
    variantsPricesPanel.setServerGridMethodName("loadVariantsPrices");
    prices2Split.add(variantsPricesPanel, JSplitPane.BOTTOM);
    pricesSplit.add(prices2Split, JSplitPane.TOP);
    pricesSplit.add(supplierPrices, JSplitPane.BOTTOM);
    tab.add(pricesSplit, "pricePanel");
//    tab.setIconAt(3,new ImageIcon(ClientUtils.getImage("report.gif")));

    tab.add(docsPanel, "docsPanel");
    tab.add(bookedItemsPanel, "bookedItemsPanel");
    tab.add(orderedItemsPanel, "orderedItemsPanel");
    tab.add(billOfMaterialsPanel, "billOfMaterials");

//    tab.setIconAt(4,new ImageIcon(ClientUtils.getImage("doc2.gif")));
//    tab.setIconAt(5,new ImageIcon(ClientUtils.getImage("itemsavail.gif")));
//    tab.setIconAt(6,new ImageIcon(ClientUtils.getImage("docs.gif")));
//    tab.setIconAt(7,new ImageIcon(ClientUtils.getImage("bills.gif")));

    pricesPanel.add(pricesButtonsPanel, BorderLayout.NORTH);
    pricesPanel.add(pricesGrid, BorderLayout.CENTER);
    pricesButtonsPanel.add(insertButton3, null);
    pricesButtonsPanel.add(copyButton2, null);
    pricesButtonsPanel.add(editButton3, null);
    pricesButtonsPanel.add(saveButton3, null);
    pricesButtonsPanel.add(reloadButton3, null);
    pricesButtonsPanel.add(deleteButton3, null);
    pricesButtonsPanel.add(exportButton2, null);
    pricesButtonsPanel.add(navigatorBar2, null);

		tab.add(itemSparePartsPanel, "itemSparePartsPanel");
    tab.add(variantsPanel, "variantsPanel");
    tab.add(barcodesPanel, "barcodesPanel");
    tab.add(minStocksPanel, "minStocksPanel");

//    tab.setIconAt(8,new ImageIcon(ClientUtils.getImage("colors.gif")));
//    tab.setIconAt(9,new ImageIcon(ClientUtils.getImage("barcode.gif")));
//    tab.setIconAt(10,new ImageIcon(ClientUtils.getImage("open.gif")));

    tab.setTitleAt(0, ClientSettings.getInstance().getResources().getResource("item detail"));
    tab.setTitleAt(1, ClientSettings.getInstance().getResources().getResource("images"));
    tab.setTitleAt(2, ClientSettings.getInstance().getResources().getResource("discounts"));
    tab.setTitleAt(3, ClientSettings.getInstance().getResources().getResource("item prices"));
    tab.setTitleAt(4, ClientSettings.getInstance().getResources().getResource("docsPanel"));
    tab.setTitleAt(5, ClientSettings.getInstance().getResources().getResource("bookedItemsPanel"));
    tab.setTitleAt(6, ClientSettings.getInstance().getResources().getResource("orderedItemsPanel"));
    tab.setTitleAt(7, ClientSettings.getInstance().getResources().getResource("billofmaterial"));
		tab.setTitleAt(8, ClientSettings.getInstance().getResources().getResource("item spare parts"));
    tab.setTitleAt(9, ClientSettings.getInstance().getResources().getResource("variantsPanel"));
    tab.setTitleAt(10, ClientSettings.getInstance().getResources().getResource("barcodesPanel"));
    tab.setTitleAt(11, ClientSettings.getInstance().getResources().getResource("min stock"));

    tab.repaint();
    repaint();

    colValue.setDecimals(5);
    colValue.setMinValue(0.0);
    colValue.setColumnDuplicable(true);
    colValue.setColumnFilterable(true);
    colValue.setEditableOnEdit(true);
    colValue.setEditableOnInsert(true);
    colValue.setColumnName("valueSAL02");
    colPriceStartDate.setColumnDuplicable(true);
    colPriceStartDate.setColumnFilterable(true);
    colPriceStartDate.setColumnName("startDateSAL02");
    colPriceStartDate.setEditableOnEdit(true);
    colPriceStartDate.setEditableOnInsert(true);
    colPriceEndDate.setColumnDuplicable(true);
    colPriceEndDate.setColumnFilterable(true);
    colPriceEndDate.setColumnName("endDateSAL02");
    colPriceEndDate.setEditableOnEdit(true);
    colPriceEndDate.setEditableOnInsert(true);

    pricesGrid.getColumnContainer().add(colPricelistCode, null);
    pricesGrid.getColumnContainer().add(colPricelistDescr, null);
    pricesGrid.getColumnContainer().add(colValue, null);
    pricesGrid.getColumnContainer().add(colPriceStartDate, null);
    pricesGrid.getColumnContainer().add(colPriceEndDate, null);
    billOfMaterialsPanel.add(bomTabbedPane, BorderLayout.CENTER);
    varsPanel.add(controlNoWarehouseMov);
    varsPanel.add(checkBoxControl1);
    varsPanel.add(checkBoxControl2, null);
    varsPanel.add(checkBoxControl3, null);
    varsPanel.add(checkBoxControl4, null);
    varsPanel.add(checkBoxControl5, null);
    formPanel.add(controlLevel, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    formPanel.add(controlLevelDescr, new GridBagConstraints(5, 1, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(labelBarcode, new GridBagConstraints(2, 7, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(controlBarcode, new GridBagConstraints(5, 7, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(labelAvgPurCost, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(controlAvgPurCost, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 120, 0));
    formPanel.add(labelLastPurCost, new GridBagConstraints(2, 6, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 130, 0));
    formPanel.add(controlLastPurCost, new GridBagConstraints(3, 6, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(labeLstPur, new GridBagConstraints(5, 6, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(controlLastPurDate, new GridBagConstraints(6, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 100, 0));
    formPanel.add(controlBarcodeType, new GridBagConstraints(3, 7, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(labelMinStock, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(controlMinStock, new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    prices2Split.setDividerLocation(250);
    pricesSplit.setDividerLocation(280);

    controlBarcode.setAttributeName("barCodeITM01");

  }

  public Form getFormPanel() {
    return formPanel;
  }

  void smallImageButton_actionPerformed(ActionEvent e) {
    byte[] bytes = loadImage();
    if (bytes != null) {
      DetailItemVO vo = (DetailItemVO) formPanel.getVOModel().getValueObject();
      vo.setSmallImage(bytes);
      smallImage.setImage(bytes);
    }
  }

  void clearSmallImageButton_actionPerformed(ActionEvent e) {
    smallImage.setImage(null);
    DetailItemVO vo = (DetailItemVO) formPanel.getVOModel().getValueObject();
    vo.setSmallImage(null);
    vo.setSmallImageITM01(null);
  }

  void largeImageButton_actionPerformed(ActionEvent e) {
    byte[] bytes = loadImage();
    if (bytes != null) {
      DetailItemVO vo = (DetailItemVO) formPanel.getVOModel().getValueObject();
      vo.setLargeImage(bytes);
      largeImage.setImage(bytes);
    }
  }

  void clearLargeImageButton_actionPerformed(ActionEvent e) {
    largeImage.setImage(null);
    DetailItemVO vo = (DetailItemVO) formPanel.getVOModel().getValueObject();
    vo.setLargeImage(null);
    vo.setLargeImageITM01(null);
  }

  public byte[] loadImage() {
    JFileChooser f = new JFileChooser();
    f.setFileFilter(new FileFilter() {

      /**
       * Whether the given file is accepted by this filter.
       */
      public boolean accept(File f) {
        return f.getName().toLowerCase().endsWith(".jpg")
                || f.getName().toLowerCase().endsWith(".gif")
                || f.isDirectory();
      }

      /**
       * The description of this filter. For example: "JPG and GIF Images"
       * @see FileView#getName
       */
      public String getDescription() {
        return ".jpg/.gif image files";
      }
    });
    int res = f.showOpenDialog(ClientUtils.getParentFrame(this));
    byte[] bytes = null;
    if (res == f.APPROVE_OPTION) {
      try {
        bytes = new byte[(int) f.getSelectedFile().length()];
        FileInputStream in = new FileInputStream(f.getSelectedFile());
        in.read(bytes);
        in.close();
      } catch (Throwable ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(
                ClientUtils.getParentFrame(this),
                ex.getMessage(),
                ClientSettings.getInstance().getResources().getResource("Error"),
                JOptionPane.WARNING_MESSAGE);
      }
    }
    return bytes;
  }

  public org.jallinone.commons.client.ImagePanel getSmallImage() {
    return smallImage;
  }

  public org.jallinone.commons.client.ImagePanel getLargeImage() {
    return largeImage;
  }

  public final void setButtonsEnabled(boolean enabled) {
    insertButton2.setEnabled(enabled);
    editButton2.setEnabled(enabled);
    deleteButton2.setEnabled(enabled);
    exportButton1.setEnabled(enabled);
//    copyButton2.setEnabled(enabled);

    insertButton3.setEnabled(enabled);
    editButton3.setEnabled(enabled);
    deleteButton3.setEnabled(enabled);
    exportButton2.setEnabled(enabled);
    copyButton2.setEnabled(enabled);
  }

  public GridControl getDiscountsGrid() {
    return discountsGrid;
  }

  public GridControl getPricesGrid() {
    return pricesGrid;
  }

  public BookedItemsPanel getBookedItemsPanel() {
    return bookedItemsPanel;
  }

  public OrderedItemsPanel getOrderedItemsPanel() {
    return orderedItemsPanel;
  }

  public ItemAttachedDocsPanel getDocsPanel() {
    return docsPanel;
  }

  public ProductPanel getBomTabbedPane() {
    return bomTabbedPane;
  }

  public SupplierItemPricesPanel getSupplierPrices() {
    return supplierPrices;
  }

  public ItemVariantsPanel getItemVariantsPanel() {
    return variantsPanel;
  }

  public VariantBarcodesPanel getVariantBarcodesPanel() {
    return barcodesPanel;
  }

  public VariantMinStocksPanel getVariantMinStockPanel() {
    return minStocksPanel;
  }

  public java.util.List getVariantsNames() {
    return variantsNames;
  }

  void checkBoxControl1_itemStateChanged(ItemEvent e) {
    setBarcodeControl();
    setMinStockControl();
  }

  void checkBoxControl2_itemStateChanged(ItemEvent e) {
    setBarcodeControl();
    setMinStockControl();
  }

  void checkBoxControl3_itemStateChanged(ItemEvent e) {
    setBarcodeControl();
    setMinStockControl();
  }

  void checkBoxControl4_itemStateChanged(ItemEvent e) {
    setBarcodeControl();
    setMinStockControl();
  }

  void checkBoxControl5_itemStateChanged(ItemEvent e) {
    setBarcodeControl();
    setMinStockControl();
  }

  void controlNoWarehouseMov_itemStateChanged(ItemEvent e) {
    setTabControl();
  }

  private void setBarcodeControl() {
    controlBarcode.setEnabledOnEdit(
            !checkBoxControl1.isSelected()
            && !checkBoxControl2.isSelected()
            && !checkBoxControl3.isSelected()
            && !checkBoxControl4.isSelected()
            && !checkBoxControl5.isSelected());

    if (formPanel.getMode() == Consts.INSERT || formPanel.getMode() == Consts.EDIT) {
      controlBarcode.setEnabled(
              !checkBoxControl1.isSelected()
              && !checkBoxControl2.isSelected()
              && !checkBoxControl3.isSelected()
              && !checkBoxControl4.isSelected()
              && !checkBoxControl5.isSelected());
      if (!controlBarcode.isEnabled()) {
        controlBarcode.setValue(null);
      }
    }
  }

  private void setMinStockControl() {
    controlMinStock.setEnabledOnEdit(
            !checkBoxControl1.isSelected()
            && !checkBoxControl2.isSelected()
            && !checkBoxControl3.isSelected()
            && !checkBoxControl4.isSelected()
            && !checkBoxControl5.isSelected());

    if (formPanel.getMode() == Consts.INSERT || formPanel.getMode() == Consts.EDIT) {
      controlMinStock.setEnabled(
              !checkBoxControl1.isSelected()
              && !checkBoxControl2.isSelected()
              && !checkBoxControl3.isSelected()
              && !checkBoxControl4.isSelected()
              && !checkBoxControl5.isSelected());
      if (!controlMinStock.isEnabled()) {
        controlMinStock.setValue(null);
      }
    }
  }

  private void setTabControl() {
    tab.setEnabledAt(5, !controlNoWarehouseMov.isSelected());
    tab.setEnabledAt(6, !controlNoWarehouseMov.isSelected());
		tab.setEnabledAt(7, !controlNoWarehouseMov.isSelected());
		tab.setEnabledAt(8, !controlNoWarehouseMov.isSelected());
		tab.setEnabledAt(9, !controlNoWarehouseMov.isSelected());
		tab.setEnabledAt(10, !controlNoWarehouseMov.isSelected());
    tab.setEnabledAt(11, !controlNoWarehouseMov.isSelected());
  }
  public ItemSparePartsPanel getItemSparePartsPanel() {
    return itemSparePartsPanel;
  }
}

class ItemFrame_smallImageButton_actionAdapter implements java.awt.event.ActionListener {

  ItemFrame adaptee;

  ItemFrame_smallImageButton_actionAdapter(ItemFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.smallImageButton_actionPerformed(e);
  }
}

class ItemFrame_clearSmallImageButton_actionAdapter implements java.awt.event.ActionListener {

  ItemFrame adaptee;

  ItemFrame_clearSmallImageButton_actionAdapter(ItemFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.clearSmallImageButton_actionPerformed(e);
  }
}

class ItemFrame_largeImageButton_actionAdapter implements java.awt.event.ActionListener {

  ItemFrame adaptee;

  ItemFrame_largeImageButton_actionAdapter(ItemFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.largeImageButton_actionPerformed(e);
  }
}

class ItemFrame_clearLargeImageButton_actionAdapter implements java.awt.event.ActionListener {

  ItemFrame adaptee;

  ItemFrame_clearLargeImageButton_actionAdapter(ItemFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.clearLargeImageButton_actionPerformed(e);
  }
}

class ItemFrame_checkBoxControl1_itemAdapter implements java.awt.event.ItemListener {

  ItemFrame adaptee;

  ItemFrame_checkBoxControl1_itemAdapter(ItemFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void itemStateChanged(ItemEvent e) {
    adaptee.checkBoxControl1_itemStateChanged(e);
  }
}

class ItemFrame_checkBoxControl2_itemAdapter implements java.awt.event.ItemListener {

  ItemFrame adaptee;

  ItemFrame_checkBoxControl2_itemAdapter(ItemFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void itemStateChanged(ItemEvent e) {
    adaptee.checkBoxControl2_itemStateChanged(e);
  }
}

class ItemFrame_checkBoxControl3_itemAdapter implements java.awt.event.ItemListener {

  ItemFrame adaptee;

  ItemFrame_checkBoxControl3_itemAdapter(ItemFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void itemStateChanged(ItemEvent e) {
    adaptee.checkBoxControl3_itemStateChanged(e);
  }
}

class ItemFrame_checkBoxControl4_itemAdapter implements java.awt.event.ItemListener {

  ItemFrame adaptee;

  ItemFrame_checkBoxControl4_itemAdapter(ItemFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void itemStateChanged(ItemEvent e) {
    adaptee.checkBoxControl4_itemStateChanged(e);
  }
}

class ItemFrame_checkBoxControl5_itemAdapter implements java.awt.event.ItemListener {

  ItemFrame adaptee;

  ItemFrame_checkBoxControl5_itemAdapter(ItemFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void itemStateChanged(ItemEvent e) {
    adaptee.checkBoxControl5_itemStateChanged(e);
  }
}

class ItemFrame_controlNoWarehouseMov_itemAdapter implements java.awt.event.ItemListener {

  ItemFrame adaptee;

  ItemFrame_controlNoWarehouseMov_itemAdapter(ItemFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void itemStateChanged(ItemEvent e) {
    adaptee.controlNoWarehouseMov_itemStateChanged(e);
  }
}

