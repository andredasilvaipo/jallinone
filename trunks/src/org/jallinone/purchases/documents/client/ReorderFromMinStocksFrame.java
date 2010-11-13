package org.jallinone.purchases.documents.client;

import java.math.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.jallinone.commons.client.*;
import org.jallinone.commons.java.*;
import org.jallinone.purchases.documents.java.*;
import org.jallinone.warehouse.java.*;
import org.openswing.swing.client.*;
import org.openswing.swing.form.client.*;
import org.openswing.swing.lookup.client.*;
import org.openswing.swing.mdi.client.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.table.client.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.table.java.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.util.java.*;
import org.jallinone.purchases.suppliers.java.GridSupplierVO;
import org.jallinone.registers.currency.java.CurrencyVO;
import org.jallinone.purchases.documents.java.PurchaseDocPK;
import org.openswing.swing.domains.java.Domain;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.items.java.ItemTypeVO;
import org.jallinone.items.java.ItemPK;
import org.jallinone.purchases.pricelist.java.SupplierPriceVO;


/**
  * <p>Title: JAllInOne ERP/CRM application</p>
  * <p>Description: Frame used for items reorder.</p>
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
public class ReorderFromMinStocksFrame extends InternalFrame {

  GridBagLayout gridBagLayout1 = new GridBagLayout();
  LabelControl labelSupplier = new LabelControl();
  CodLookupControl controlSupplierCode = new CodLookupControl();
  TextControl controlSupplierDescr = new TextControl();
  LabelControl labelCurrency = new LabelControl();

  LookupController supplierController = new LookupController();
  LookupServerDataLocator supplierDataLocator = new LookupServerDataLocator();

  LabelControl labelItemType = new LabelControl();

  private Form filterPanel = new Form();
  BorderLayout borderLayout1 = new BorderLayout();
  CodLookupControl controlCurrency = new CodLookupControl();
  LabelControl labelWarehouse = new LabelControl();
  CodLookupControl controlWarehouse = new CodLookupControl();
  TextControl controlWareDescr = new TextControl();
  GenericButton buttonSearch = new GenericButton(new ImageIcon(ClientUtils.getImage("budget.gif")));

  LookupController wareController = new LookupController();
  LookupServerDataLocator wareDataLocator = new LookupServerDataLocator();

  LookupController currencyController = new LookupController();
  LookupServerDataLocator currencyDataLocator = new LookupServerDataLocator();
  GridControl grid = new GridControl();
  CheckBoxColumn colSel = new CheckBoxColumn();
  TextColumn colItemCode = new TextColumn();
  TextColumn colItemDescr = new TextColumn();
  DecimalColumn colProp = new DecimalColumn();
  DecimalColumn colQty = new DecimalColumn();
  DecimalColumn colAvail = new DecimalColumn();
  DecimalColumn colPawned = new DecimalColumn();
  DecimalColumn colGood = new DecimalColumn();
  DecimalColumn colOrdered = new DecimalColumn();
  CodLookupColumn colSupplierCode = new CodLookupColumn();
  TextColumn colSupplierName_1 = new TextColumn();
  TextColumn colUM = new TextColumn();
  DecimalColumn colMinStock = new DecimalColumn();
  JPanel southPanel = new JPanel();
  GenericButton buttonCreate = new GenericButton(new ImageIcon(ClientUtils.getImage("docs.gif")));
  ServerGridDataLocator gridLocator = new ServerGridDataLocator();

  LookupController supplierController2 = new LookupController();
  LookupServerDataLocator supplierDataLocator2 = new LookupServerDataLocator();

  private WarehouseVO warehouseVO = null;
  private CurrencyVO currVO = null;
  private java.util.List itemTypesList = null;

  ComboBoxControl controlItemType = new ComboBoxControl();


  public ReorderFromMinStocksFrame() {
    try {
      jbInit();
      setTitle(ClientSettings.getInstance().getResources().getResource("items reorder"));

      grid.setController(new GridController() {

        /**
         * Callback method invoked when the data loading is completed.
         * @param error <code>true</code> if data loading has terminated with errors, <code>false</code> otherwise
         */
        public void loadDataCompleted(boolean error) {
          grid.setMode(Consts.EDIT);
        }

      });
      gridLocator.setServerMethodName("reorderFromMinStocks");
      grid.setGridDataLocator(gridLocator);
      grid.setShowWarnMessageBeforeReloading(false);

      init();

      filterPanel.setFormController(new FormController());
      filterPanel.setMode(Consts.INSERT);
      controlSupplierCode.setEnabled(false);

      setSize(750,550);
      MDIFrame.add(this,true);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void init() {

    // item type...
    Response res = ClientUtils.getData("loadItemTypes",new GridParams());
    Domain d = new Domain("ITEM_TYPES");
    if (!res.isError()) {
      ItemTypeVO vo = null;
      itemTypesList = ((VOListResponse)res).getRows();
      for(int i=0;i<itemTypesList.size();i++) {
        vo = (ItemTypeVO)itemTypesList.get(i);
        d.addDomainPair(vo.getProgressiveHie02ITM02(),vo.getDescriptionSYS10());
      }
    }
    controlItemType.setDomain(d);
    controlItemType.getComboBox().addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange()==e.SELECTED) {
          ReorderFromMinStockFilterVO vo = (ReorderFromMinStockFilterVO)filterPanel.getVOModel().getValueObject();
          vo.setProgressiveHie02ITM01((BigDecimal)controlItemType.getValue());
        }
      }
    });
    if (d.getDomainPairList().length>=1)
      controlItemType.getComboBox().setSelectedIndex(0);
    else
      controlItemType.getComboBox().setSelectedIndex(-1);


    // warehouse lookup...
    wareDataLocator.setGridMethodName("loadWarehouses");
    wareDataLocator.setValidationMethodName("validateWarehouseCode");

    controlWarehouse.setLookupController(wareController);
    controlWarehouse.setControllerMethodName("getWarehousesList");
    wareController.setForm(filterPanel);
    wareController.setLookupDataLocator(wareDataLocator);
    wareController.setFrameTitle("warehouses");
    wareController.setLookupValueObjectClassName("org.jallinone.warehouse.java.WarehouseVO");
    wareController.addLookup2ParentLink("companyCodeSys01WAR01", "companyCode");
    wareController.addLookup2ParentLink("warehouseCodeWAR01", "warehouseCode");
    wareController.addLookup2ParentLink("descriptionWAR01","warehouseDescription");
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
    wareController.addLookupListener(new LookupListener() {

      public void codeValidated(boolean validated) {}

      public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
        if (wareController.getLookupVO()==null) {
          controlSupplierCode.setValue(null);
          supplierController.forceValidate();
          controlSupplierCode.setEnabled(false);
          warehouseVO = null;
        }
        else {
          warehouseVO = (WarehouseVO)wareController.getLookupVO();
          supplierDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,warehouseVO.getCompanyCodeSys01WAR01());
          supplierDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,warehouseVO.getCompanyCodeSys01WAR01());
					grid.getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,warehouseVO.getCompanyCodeSys01WAR01());
          controlSupplierCode.setEnabled(true);
        }
      }

      public void beforeLookupAction(ValueObject parentVO) { }

      public void forceValidate() {}

    });


    // supplier lookup...
    supplierDataLocator.setGridMethodName("loadSuppliers");
    supplierDataLocator.setValidationMethodName("validateSupplierCode");

    controlSupplierCode.setLookupController(supplierController);
    controlSupplierCode.setControllerMethodName("getSuppliersList");
    supplierController.setForm(filterPanel);
    supplierController.setLookupDataLocator(supplierDataLocator);
    supplierController.setFrameTitle("suppliers");
    supplierController.setLookupValueObjectClassName("org.jallinone.purchases.suppliers.java.GridSupplierVO");
    supplierController.addLookup2ParentLink("supplierCodePUR01","supplierCode");
    supplierController.addLookup2ParentLink("progressiveREG04","progressiveREG04");
    supplierController.addLookup2ParentLink("name_1REG04", "name_1REG04");
    supplierController.setAllColumnVisible(false);
    supplierController.setVisibleColumn("companyCodeSys01REG04", true);
    supplierController.setVisibleColumn("supplierCodePUR01", true);
    supplierController.setVisibleColumn("name_1REG04", true);
    supplierController.setVisibleColumn("name_2REG04", true);
    supplierController.setVisibleColumn("cityREG04", true);
    supplierController.setVisibleColumn("provinceREG04", true);
    supplierController.setVisibleColumn("countryREG04", true);
    supplierController.setVisibleColumn("taxCodeREG04", true);
    supplierController.setHeaderColumnName("name_1REG04", "corporateName1");
    supplierController.setHeaderColumnName("cityREG04", "city");
    supplierController.setHeaderColumnName("provinceREG04", "prov");
    supplierController.setHeaderColumnName("countryREG04", "country");
    supplierController.setHeaderColumnName("taxCodeREG04", "taxCode");
    supplierController.setPreferredWidthColumn("name_1REG04", 200);
    supplierController.setPreferredWidthColumn("name_2REG04", 150);
    supplierController.setFramePreferedSize(new Dimension(750,500));


    // supplier lookup for column...
    supplierDataLocator2.setGridMethodName("loadSuppliers");
    supplierDataLocator2.setValidationMethodName("validateSupplierCode");

    colSupplierCode.setLookupController(supplierController2);
    colSupplierCode.setControllerMethodName("getSuppliersList");
    supplierController2.setForm(filterPanel);
    supplierController2.setLookupDataLocator(supplierDataLocator2);
    supplierController2.setFrameTitle("suppliers");
    supplierController2.setLookupValueObjectClassName("org.jallinone.purchases.suppliers.java.GridSupplierVO");
    supplierController2.addLookup2ParentLink("supplierCodePUR01","supplierCode");
    supplierController2.addLookup2ParentLink("name_1REG04", "name_1REG04");
    supplierController2.setAllColumnVisible(false);
    supplierController2.setVisibleColumn("companyCodeSys01REG04", true);
    supplierController2.setVisibleColumn("supplierCodePUR01", true);
    supplierController2.setVisibleColumn("name_1REG04", true);
    supplierController2.setVisibleColumn("name_2REG04", true);
    supplierController2.setVisibleColumn("cityREG04", true);
    supplierController2.setVisibleColumn("provinceREG04", true);
    supplierController2.setVisibleColumn("countryREG04", true);
    supplierController2.setVisibleColumn("taxCodeREG04", true);
    supplierController2.setHeaderColumnName("name_1REG04", "corporateName1");
    supplierController2.setHeaderColumnName("cityREG04", "city");
    supplierController2.setHeaderColumnName("provinceREG04", "prov");
    supplierController2.setHeaderColumnName("countryREG04", "country");
    supplierController2.setHeaderColumnName("taxCodeREG04", "taxCode");
    supplierController2.setPreferredWidthColumn("name_1REG04", 200);
    supplierController2.setPreferredWidthColumn("name_2REG04", 150);
    supplierController2.setFramePreferedSize(new Dimension(750,500));
    supplierController2.addLookupListener(new LookupListener() {

      public void codeValidated(boolean validated) {}

      public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
        ReorderFromMinStockVO vo = (ReorderFromMinStockVO)grid.getVOListTableModel().getObjectForRow(grid.getSelectedRow());
        GridSupplierVO lookupVO = (GridSupplierVO)supplierController2.getLookupVO();
        if (lookupVO!=null && lookupVO.getProgressiveREG04()!=null) {
          vo.setProgressiveREG04(lookupVO.getProgressiveREG04());

          // retrieve pricelist...
          GridParams gridParams = new GridParams();
          gridParams.getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_REG04,lookupVO.getProgressiveREG04());
          gridParams.getOtherGridParams().put(ApplicationConsts.DATE_FILTER,new java.sql.Date(System.currentTimeMillis()));
          gridParams.getOtherGridParams().put(ApplicationConsts.ITEM_PK,new ItemPK(vo.getCompanyCodeSys01(),vo.getItemCode()));
          Response res = ClientUtils.getData("loadSupplierPrices",gridParams);
          if (res.isError())
            vo.setPricelistCodePUR03(null);
          else {
            SupplierPriceVO priceVO = (SupplierPriceVO)((VOListResponse)res).getRows().get(0);
            vo.setPricelistCodePUR03(priceVO.getPricelistCodePur03PUR04());
            vo.setPricelistDescription(priceVO.getPricelistDescriptionSYS10());
          }
        }
        else
          vo.setProgressiveREG04(null);
      }

      public void beforeLookupAction(ValueObject parentVO) {
        ReorderFromMinStockVO vo = (ReorderFromMinStockVO)grid.getVOListTableModel().getObjectForRow(grid.getSelectedRow());
        supplierDataLocator2.getLookupFrameParams().put(ApplicationConsts.ITEM,vo.getItemCode());
        supplierDataLocator2.getLookupValidationParameters().put(ApplicationConsts.ITEM,vo.getItemCode());
      }

      public void forceValidate() {}

    });


    // currency lookup...
    currencyDataLocator.setGridMethodName("loadCurrencies");
    currencyDataLocator.setValidationMethodName("validateCurrencyCode");
    controlCurrency.setLookupController(currencyController);
    controlCurrency.setControllerMethodName("getCurrenciesList");
    currencyController.setLookupDataLocator(currencyDataLocator);
    currencyController.setFrameTitle("currencies");
    currencyController.setLookupValueObjectClassName("org.jallinone.registers.currency.java.CurrencyVO");
    currencyController.addLookup2ParentLink("currencyCodeREG03", "currencyCodeREG03");
    currencyController.setAllColumnVisible(false);
    currencyController.setVisibleColumn("currencyCodeREG03", true);
    currencyController.setVisibleColumn("currencySymbolREG03", true);
    new CustomizedColumns(new BigDecimal(182),currencyController);
    currencyController.addLookupListener(new LookupListener() {

      public void codeValidated(boolean validated) {}

      public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
        currVO = (CurrencyVO)currencyController.getLookupVO();
      }

      public void beforeLookupAction(ValueObject parentVO) {}

      public void forceValidate() {}

    });


  }


  private void jbInit() throws Exception {
    super.setAskBeforeClose(false);
    grid.setAutoLoadData(false);
    grid.setValueObjectClassName("org.jallinone.purchases.documents.java.ReorderFromMinStockVO");
    filterPanel.setVOClassName("org.jallinone.purchases.documents.java.ReorderFromMinStockFilterVO");
    labelSupplier.setText("supplier");
    this.getContentPane().setLayout(borderLayout1);
    labelItemType.setText("item type");
    controlCurrency.setAttributeName("currencyCodeREG03");
    controlCurrency.setLinkLabel(labelCurrency);
    controlCurrency.setMaxCharacters(20);
    controlCurrency.setRequired(true);
    labelWarehouse.setText("warehouseCodeWAR01");
    controlWarehouse.setAttributeName("warehouseCode");
    controlWarehouse.setLinkLabel(labelWarehouse);
    controlWarehouse.setMaxCharacters(20);
    controlWarehouse.setRequired(true);
    controlWareDescr.setAttributeName("warehouseDescription");
    controlWareDescr.setEnabledOnEdit(false);
    controlWareDescr.setEnabledOnInsert(false);
    buttonSearch.setButtonBehavior(Consts.BUTTON_IMAGE_AND_TEXT);
    buttonSearch.setHorizontalTextPosition(SwingConstants.TRAILING);
    buttonSearch.setText("search");
    buttonSearch.setExecuteAsThread(false);
    buttonSearch.addActionListener(new ReorderFromMinStocksFrame_buttonSearch_actionAdapter(this));
    colItemDescr.setPreferredWidth(250);
    colProp.setPreferredWidth(70);
    colQty.setEditableOnEdit(true);
    colQty.setPreferredWidth(40);
    colAvail.setPreferredWidth(70);
    colGood.setPreferredWidth(70);
    colPawned.setPreferredWidth(60);
    colOrdered.setPreferredWidth(70);
    colSupplierCode.setEditableOnEdit(true);
    colSupplierCode.setHeaderColumnName("supplierCodePUR01");
    colSupplierName_1.setPreferredWidth(200);
    colUM.setMinWidth(0);
    colUM.setPreferredWidth(40);
    colMinStock.setPreferredWidth(70);
    buttonCreate.setHorizontalTextPosition(SwingConstants.TRAILING);
    buttonCreate.setButtonBehavior(Consts.BUTTON_IMAGE_AND_TEXT);
    buttonCreate.setText("create orders");
    buttonCreate.addActionListener(new ReorderFromMinStocksFrame_buttonCreate_actionAdapter(this));
    colItemCode.setHeaderColumnName("itemCodeITM01");
    colItemCode.setPreferredWidth(80);
    colSel.setShowDeSelectAllInPopupMenu(true);
    colSel.setEditableOnEdit(true);
    colSel.setPreferredWidth(50);
    this.getContentPane().add(filterPanel, BorderLayout.NORTH);
    filterPanel.setLayout(gridBagLayout1);
    controlSupplierCode.setCanCopy(true);
    controlSupplierCode.setLinkLabel(labelSupplier);
    controlSupplierCode.setMaxCharacters(20);
    controlSupplierCode.setRequired(true);
    controlSupplierCode.setAttributeName("supplierCode");
    labelCurrency.setText("currencyCodeREG03");
    controlSupplierDescr.setCanCopy(true);
    controlSupplierDescr.setEnabledOnInsert(false);
    controlSupplierDescr.setEnabledOnEdit(false);
    controlSupplierDescr.setAttributeName("name_1REG04");
    filterPanel.add(labelSupplier,         new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlSupplierCode,                  new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(labelCurrency,         new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlSupplierDescr,        new GridBagConstraints(2, 1, 3, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 70, 0));
    filterPanel.add(controlCurrency,       new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(labelWarehouse,     new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlWarehouse,    new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlWareDescr,     new GridBagConstraints(2, 0, 4, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(buttonSearch,      new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    filterPanel.add(labelItemType,    new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    filterPanel.add(controlItemType,  new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 100, 0));
    this.getContentPane().add(grid, BorderLayout.CENTER);

    colSel.setColumnName("selected");
    colItemCode.setColumnName("itemCode");
    colItemDescr.setColumnName("itemDescription");
    colUM.setColumnName("umCodeREG02");
    colMinStock.setColumnName("minStockITM23");
    colProp.setColumnName("proposedQty");
    colQty.setColumnName("qty");
    colAvail.setColumnName("availableQty");
    colPawned.setColumnName("pawnedQty");
    colGood.setColumnName("goodQty");
    colOrdered.setColumnName("orderedQty");
    colSupplierCode.setColumnName("supplierCode");
    colSupplierName_1.setColumnName("name_1REG04");

    grid.getColumnContainer().add(colSel, null);
    grid.getColumnContainer().add(colItemCode, null);
    grid.getColumnContainer().add(colItemDescr, null);
    grid.getColumnContainer().add(colUM, null);
    grid.getColumnContainer().add(colMinStock, null);
    grid.getColumnContainer().add(colProp, null);
    grid.getColumnContainer().add(colQty, null);
    grid.getColumnContainer().add(colAvail, null);
    grid.getColumnContainer().add(colPawned, null);
    grid.getColumnContainer().add(colGood, null);
    grid.getColumnContainer().add(colOrdered, null);
    grid.getColumnContainer().add(colSupplierCode, null);
    grid.getColumnContainer().add(colSupplierName_1, null);
    this.getContentPane().add(southPanel,  BorderLayout.SOUTH);
    southPanel.add(buttonCreate, null);
  }


  void buttonSearch_actionPerformed(ActionEvent e) {
    if (!filterPanel.push()) {
//      OptionPane.showMessageDialog(MDIFrame.getInstance(),"you have to fill in all filtering conditions","Attention",JOptionPane.WARNING_MESSAGE);
    }
    else {
      ReorderFromMinStockFilterVO vo = (ReorderFromMinStockFilterVO)filterPanel.getVOModel().getValueObject();
      vo.setProgressiveHie02ITM01((BigDecimal)controlItemType.getValue());
      grid.getOtherGridParams().put(ApplicationConsts.FILTER_VO,filterPanel.getVOModel().getValueObject());
      grid.reloadData();
    }
  }


  public GridControl getGrid() {
    return grid;
  }


  void buttonCreate_actionPerformed(ActionEvent e) {
    ReorderFromMinStockVO vo = null;
    ArrayList vos = new ArrayList();
    for(int i=0;i<grid.getVOListTableModel().getRowCount();i++) {
      vo = (ReorderFromMinStockVO)grid.getVOListTableModel().getObjectForRow(i);
      if (vo.isSelected() &&
          vo.getQty()!=null && vo.getQty().doubleValue()>0 &&
          vo.getSupplierCode()!=null && !vo.getSupplierCode().equals("")) {
        vos.add(vo);
      }
    }
    if (vos.size()==0) {
      OptionPane.showMessageDialog(MDIFrame.getInstance(),"you must select at least one row and a qty greater than zero","Attention",JOptionPane.WARNING_MESSAGE);
    }
    else {
      Response res = ClientUtils.getData(
        "createPurchaseOrders",
        new Object[]{
          currVO,
          warehouseVO,
          itemTypesList.get(controlItemType.getSelectedIndex()),
          vos
        }
      );
      if (res.isError()) {
        OptionPane.showMessageDialog(MDIFrame.getInstance(),res.getErrorMessage(),"Attention",JOptionPane.ERROR_MESSAGE);
      }
      else {
        OptionPane.showMessageDialog(MDIFrame.getInstance(),"orders created","Attention",JOptionPane.INFORMATION_MESSAGE);
        java.util.List rows = ((VOListResponse)res).getRows();
        BigDecimal docNumber = null;
        for(int i=0;i<rows.size();i++) {
          docNumber = (BigDecimal)rows.get(i);
          new PurchaseDocController(
            null,
            new PurchaseDocPK(
              warehouseVO.getCompanyCodeSys01WAR01(),
              ApplicationConsts.PURCHASE_ORDER_DOC_TYPE,
              new BigDecimal(Calendar.getInstance().get(Calendar.YEAR)),
              docNumber
            )
          );
        }
      }
    }
  }

}

class ReorderFromMinStocksFrame_buttonSearch_actionAdapter implements java.awt.event.ActionListener {
  ReorderFromMinStocksFrame adaptee;

  ReorderFromMinStocksFrame_buttonSearch_actionAdapter(ReorderFromMinStocksFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.buttonSearch_actionPerformed(e);
  }
}

class ReorderFromMinStocksFrame_buttonCreate_actionAdapter implements java.awt.event.ActionListener {
  ReorderFromMinStocksFrame adaptee;

  ReorderFromMinStocksFrame_buttonCreate_actionAdapter(ReorderFromMinStocksFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.buttonCreate_actionPerformed(e);
  }
}
