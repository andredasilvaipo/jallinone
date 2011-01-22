package org.jallinone.sales.pricelist.client;

import org.openswing.swing.mdi.client.InternalFrame;
import org.openswing.swing.tree.client.*;
import java.awt.*;
import org.openswing.swing.mdi.client.MDIFrame;
import javax.swing.*;
import org.openswing.swing.client.*;
import org.openswing.swing.table.java.ServerGridDataLocator;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.table.client.GridController;
import java.math.BigDecimal;
import org.jallinone.commons.client.*;
import java.awt.event.*;
import javax.swing.border.*;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.domains.java.Domain;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.util.java.Consts;
import org.jallinone.items.java.ItemTypeVO;
import java.util.ArrayList;
import org.jallinone.sales.pricelist.java.PriceVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.sales.pricelist.java.PricelistVO;
import java.util.Date;
import org.jallinone.sales.pricelist.java.PricelistChanges;
import org.openswing.swing.lookup.client.LookupListener;
import java.util.Collection;
import org.jallinone.variants.client.ProductVariantsPanel;
import org.jallinone.variants.client.ProductVariantsController;
import org.openswing.swing.form.client.Form;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the pricelists + item prices frame.</p>
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
public class PricelistFrame extends InternalFrame implements DateChangedListener {

  JPanel pricelistsPanel = new JPanel();
  JPanel pricesPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel buttonsPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  InsertButton insertButton = new InsertButton();
  ReloadButton reloadButton = new ReloadButton();
  DeleteButton deleteButton = new DeleteButton();
  NavigatorBar navigatorBar = new NavigatorBar();
  GridControl grid = new GridControl();

  EditButton editButton = new EditButton();
  SaveButton saveButton = new SaveButton();

  TextColumn colPricelist = new TextColumn();
  TextColumn colDescr = new TextColumn();
  ExportButton exportButton = new ExportButton();
  JSplitPane split = new JSplitPane();
  JSplitPane prices2Split = new JSplitPane();
  CompaniesComboColumn colCompany = new CompaniesComboColumn();
  CodLookupColumn colCurrencyCode = new CodLookupColumn();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  LabelControl labelDateFilter = new LabelControl();
  DateControl controlDate = new DateControl();
  JPanel pricesButtonsPanel = new JPanel();
  GridControl pricesGrid = new GridControl();
  FlowLayout flowLayout2 = new FlowLayout();
  TitledBorder titledBorder1;
  TitledBorder titledBorder2;
  InsertButton insertButton1 = new InsertButton();
  EditButton editButton1 = new EditButton();
  SaveButton saveButton1 = new SaveButton();
  DeleteButton deleteButton1 = new DeleteButton();
  ReloadButton reloadButton1 = new ReloadButton();
  ExportButton exportButton1 = new ExportButton();
  CopyButton copyButton1 = new CopyButton();
  NavigatorBar navigatorBar1 = new NavigatorBar();
  ComboColumn colItemType = new ComboColumn();
  CodLookupColumn codtemCode = new CodLookupColumn();
  TextColumn colItemDescr = new TextColumn();
  DecimalColumn colValue = new DecimalColumn();
  DateColumn colStartDate = new DateColumn();
  DateColumn colEndDate = new DateColumn();

  /** pricelist grid data locator */
  private ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();

  /** prices grid data locator */
  private ServerGridDataLocator pricesGridDataLocator = new ServerGridDataLocator();

  LookupController currencyController = new LookupController();
  LookupServerDataLocator currencyDataLocator = new LookupServerDataLocator();

  LookupController itemController = new LookupController();
  LookupServerDataLocator itemDataLocator = new LookupServerDataLocator();

  TreeServerDataLocator treeLevelDataLocator = new TreeServerDataLocator();
  CopyButton copyButton2 = new CopyButton();
  GenericButton advancedButton = new GenericButton(new ImageIcon(ClientUtils.getImage("work2.gif")));
  GenericButton impAllButton = new GenericButton(new ImageIcon(ClientUtils.getImage("doc3.gif")));

  private int splitDiv = 550;

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
      true
  );



  public PricelistFrame(GridController controller) {
    grid.setController(controller);
    grid.setGridDataLocator(gridDataLocator);
    gridDataLocator.setServerMethodName("loadPricelists");

    pricesGrid.setController(new PricesController(this));
    pricesGrid.setGridDataLocator(pricesGridDataLocator);
    pricesGridDataLocator.setServerMethodName("loadPrices");

    try {
      jbInit();
      setSize(750,550);
      setMinimumSize(new Dimension(750,550));

      // currency lookup...
      currencyDataLocator.setGridMethodName("loadCurrencies");
      currencyDataLocator.setValidationMethodName("validateCurrencyCode");
      colCurrencyCode.setLookupController(currencyController);
      colCurrencyCode.setControllerMethodName("getCurrenciesList");
      currencyController.setLookupDataLocator(currencyDataLocator);
      currencyController.setFrameTitle("currencies");
      currencyController.setLookupValueObjectClassName("org.jallinone.registers.currency.java.CurrencyVO");
      currencyController.addLookup2ParentLink("currencyCodeREG03", "currencyCodeReg03SAL01");
      currencyController.setAllColumnVisible(false);
      currencyController.setVisibleColumn("currencyCodeREG03", true);
      currencyController.setVisibleColumn("currencySymbolREG03", true);
      new CustomizedColumns(new BigDecimal(182),currencyController);

      // item lookup...
      itemDataLocator.setGridMethodName("loadItems");
      itemDataLocator.setValidationMethodName("validateItemCode");
      codtemCode.setLookupController(itemController);
      codtemCode.setControllerMethodName("getItemsList");
      itemController.setCodeSelectionWindow(itemController.TREE_GRID_FRAME);
      itemController.setLookupDataLocator(itemDataLocator);
      treeLevelDataLocator.setServerMethodName("loadHierarchy");
      itemDataLocator.setTreeDataLocator(treeLevelDataLocator);
      itemController.setFrameTitle("items");
      itemController.setLookupValueObjectClassName("org.jallinone.items.java.GridItemVO");
      itemController.addLookup2ParentLink("itemCodeITM01", "itemCodeItm01SAL02");
      itemController.addLookup2ParentLink("descriptionSYS10", "itemDescriptionSYS10");
      itemController.setAllColumnVisible(false);
      itemController.setVisibleColumn("itemCodeITM01", true);
      itemController.setVisibleColumn("descriptionSYS10", true);
      itemController.setFramePreferedSize(new Dimension(600,500));
      itemController.setPreferredWidthColumn("descriptionSYS10",200);
      itemDataLocator.setNodeNameAttribute("descriptionSYS10");
      new CustomizedColumns(new BigDecimal(262),itemController);
      itemController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {}

        public void beforeLookupAction(ValueObject parentVO) {
          PriceVO priceVO = (PriceVO)parentVO;
          treeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.PROGRESSIVE_HIE02,priceVO.getProgressiveHie02ITM01());
          treeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.COMPANY_CODE_SYS01,((PricelistVO)grid.getVOListTableModel().getObjectForRow(grid.getSelectedRow())).getCompanyCodeSys01SAL01());
          itemDataLocator.getLookupFrameParams().put(ApplicationConsts.PROGRESSIVE_HIE02,priceVO.getProgressiveHie02ITM01());
          itemDataLocator.getLookupValidationParameters().put(ApplicationConsts.PROGRESSIVE_HIE02,priceVO.getProgressiveHie02ITM01());
          itemDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,((PricelistVO)grid.getVOListTableModel().getObjectForRow(grid.getSelectedRow())).getCompanyCodeSys01SAL01());
          itemDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,((PricelistVO)grid.getVOListTableModel().getObjectForRow(grid.getSelectedRow())).getCompanyCodeSys01SAL01());
        }

        public void forceValidate() {}

      });

      init();

      CustomizedColumns cust = new CustomizedColumns(new BigDecimal(302),grid);

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  public ProductVariantsPanel getVariantsPricesPanel() {
    return variantsPricesPanel;
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
        if (e.getStateChange()==e.SELECTED) {
          PriceVO vo = (PriceVO)pricesGrid.getVOListTableModel().getObjectForRow(pricesGrid.getSelectedRow());
          PricelistVO pricelistVO = (PricelistVO)grid.getVOListTableModel().getObjectForRow(0);
          vo.setItemCodeItm01SAL02(null);
          int selIndex = ((JComboBox)e.getSource()).getSelectedIndex();
          Object selValue = d.getDomainPairList()[selIndex].getCode();
          treeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.PROGRESSIVE_HIE02,selValue);
          treeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.COMPANY_CODE_SYS01,pricelistVO.getCompanyCodeSys01SAL01());
          itemDataLocator.getLookupFrameParams().put(ApplicationConsts.PROGRESSIVE_HIE02,selValue);
          itemDataLocator.getLookupValidationParameters().put(ApplicationConsts.PROGRESSIVE_HIE02,selValue);
          itemDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,pricelistVO.getCompanyCodeSys01SAL01());
          itemDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,pricelistVO.getCompanyCodeSys01SAL01());
          vo.setCompanyCodeSys01SAL02(pricelistVO.getCompanyCodeSys01SAL01());
          vo.setPricelistCodeSal01SAL02(pricelistVO.getPricelistCodeSAL01());
        }
      }
    });
  }



  public final void reloadData() {
    grid.reloadData();
  }


  private void jbInit() throws Exception {
    grid.setMaxNumberOfRowsOnInsert(50);
    pricesGrid.setMaxNumberOfRowsOnInsert(50);
    pricesGrid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

    saveButton.setExecuteAsThread(true);
    advancedButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("change prices"));
    impAllButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("import all items"));


    titledBorder1 = new TitledBorder("");
    titledBorder2 = new TitledBorder("");
    pricelistsPanel.setLayout(borderLayout1);
    saveButton.setExecuteAsThread(true);
    grid.setValueObjectClassName("org.jallinone.sales.pricelist.java.PricelistVO");
    this.setTitle(ClientSettings.getInstance().getResources().getResource("pricelists"));
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    grid.setCopyButton(copyButton2);
    grid.setDeleteButton(deleteButton);
    grid.setEditButton(editButton);
    grid.setExportButton(exportButton);
    grid.setFunctionId("SAL01");
    grid.setMaxSortedColumns(3);
    grid.setInsertButton(insertButton);
    grid.setNavBar(navigatorBar);
    grid.setReloadButton(reloadButton);
    grid.setSaveButton(saveButton);
    colPricelist.setMaxCharacters(20);
    colPricelist.setTrimText(true);
    colPricelist.setUpperCase(true);
    colPricelist.setColumnFilterable(true);
    colPricelist.setColumnName("pricelistCodeSAL01");
    colPricelist.setColumnSortable(true);
    colPricelist.setEditableOnInsert(true);
    colPricelist.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colPricelist.setSortingOrder(1);
    colDescr.setColumnDuplicable(true);
    colDescr.setColumnFilterable(false);
    colDescr.setColumnName("descriptionSYS10");
    colDescr.setColumnSortable(true);
    colDescr.setEditableOnEdit(true);
    colDescr.setEditableOnInsert(true);
    colDescr.setHeaderColumnName("pricelistDescription");
    colDescr.setPreferredWidth(290);
    split.setOrientation(JSplitPane.VERTICAL_SPLIT);
    colCompany.setColumnDuplicable(true);
    colCompany.setColumnFilterable(true);
    colCompany.setColumnName("companyCodeSys01SAL01");
    colCompany.setFunctionCode("SAL01");
    colCompany.setColumnSortable(true);
    colCompany.setEditableOnInsert(true);
    colCompany.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colCompany.setSortingOrder(1);
    colCurrencyCode.setColumnDuplicable(true);
    colCurrencyCode.setColumnFilterable(true);
    colCurrencyCode.setColumnName("currencyCodeReg03SAL01");
    colCurrencyCode.setColumnSortable(true);
    colCurrencyCode.setEditableOnEdit(false);
    colCurrencyCode.setEditableOnInsert(true);
    colCurrencyCode.setMaxCharacters(20);
    pricesPanel.setLayout(gridBagLayout1);
    labelDateFilter.setText("validity date filter");
    controlDate.addDateChangedListener(this);
    pricesButtonsPanel.setLayout(flowLayout2);
    flowLayout2.setAlignment(FlowLayout.LEFT);
    pricesPanel.setBorder(titledBorder1);
    titledBorder1.setTitleColor(Color.blue);
    titledBorder1.setTitle(ClientSettings.getInstance().getResources().getResource("item prices"));
    pricesGrid.setAutoLoadData(false);
    pricesGrid.setCopyButton(copyButton1);
    pricesGrid.setDeleteButton(deleteButton1);
    pricesGrid.setEditButton(editButton1);
    pricesGrid.setExportButton(exportButton1);
    pricesGrid.setFunctionId("SAL01");
    pricesGrid.setMaxSortedColumns(3);
    pricesGrid.setInsertButton(insertButton1);
    pricesGrid.setNavBar(navigatorBar1);
    pricesGrid.setReloadButton(reloadButton1);
    pricesGrid.setSaveButton(saveButton1);
    pricesGrid.setValueObjectClassName("org.jallinone.sales.pricelist.java.PriceVO");
    colItemType.setColumnDuplicable(true);
    colItemType.setColumnFilterable(false);
    colItemType.setColumnName("progressiveHie02ITM01");
    colItemType.setColumnSortable(false);
    colItemType.setEditableOnInsert(true);
    colItemType.setSortVersus(org.openswing.swing.util.java.Consts.NO_SORTED);
    codtemCode.setColumnDuplicable(false);
    codtemCode.setColumnFilterable(true);
    codtemCode.setColumnName("itemCodeItm01SAL02");
    codtemCode.setEditableOnInsert(true);
    codtemCode.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    codtemCode.setMaxCharacters(20);
    colItemDescr.setColumnDuplicable(false);
    colItemDescr.setColumnName("itemDescriptionSYS10");
    colItemDescr.setPreferredWidth(250);
    colValue.setDecimals(5);
    colValue.setMinValue(0.0);
    colValue.setColumnDuplicable(true);
    colValue.setColumnFilterable(true);
    colValue.setEditableOnEdit(true);
    colValue.setEditableOnInsert(true);
    colValue.setColumnName("valueSAL02");
    colStartDate.setColumnDuplicable(true);
    colStartDate.setColumnFilterable(true);
    colStartDate.setColumnName("startDateSAL02");
    colStartDate.setEditableOnEdit(true);
    colStartDate.setEditableOnInsert(true);
    colEndDate.setColumnDuplicable(true);
    colEndDate.setColumnFilterable(true);
    colEndDate.setColumnName("endDateSAL02");
    colEndDate.setEditableOnEdit(true);
    colEndDate.setEditableOnInsert(true);
    advancedButton.addActionListener(new PricelistFrame_advancedButton_actionAdapter(this));
    impAllButton.addActionListener(new PricelistFrame_impAllButton_actionAdapter(this));

    prices2Split.setOrientation(JSplitPane.VERTICAL_SPLIT);
    prices2Split.add(pricesPanel,JSplitPane.TOP);
    prices2Split.add(variantsPricesPanel,JSplitPane.BOTTOM);
    variantsPricesPanel.setServerGridMethodName("loadVariantsPrices");

    this.getContentPane().add(split,BorderLayout.CENTER);
    split.add(pricelistsPanel,JSplitPane.TOP);
    split.add(prices2Split,JSplitPane.BOTTOM);

    pricesPanel.add(labelDateFilter,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    pricesPanel.add(controlDate,    new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 80, 0));
    pricesPanel.add(pricesButtonsPanel,    new GridBagConstraints(0, 1, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    pricesButtonsPanel.add(insertButton1, null);
    pricesButtonsPanel.add(copyButton1, null);
    pricesButtonsPanel.add(editButton1, null);
    pricesButtonsPanel.add(saveButton1, null);
    pricesButtonsPanel.add(reloadButton1, null);
    pricesButtonsPanel.add(deleteButton1, null);
    pricesButtonsPanel.add(exportButton1, null);
    pricesButtonsPanel.add(navigatorBar1, null);
    pricesPanel.add(pricesGrid,   new GridBagConstraints(0, 2, 2, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    pricesGrid.getColumnContainer().add(colItemType, null);
    pricesGrid.getColumnContainer().add(codtemCode, null);
    pricelistsPanel.add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(copyButton2, null);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(saveButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(deleteButton, null);
    buttonsPanel.add(exportButton, null);
    buttonsPanel.add(navigatorBar, null);
    buttonsPanel.add(advancedButton, null);
    buttonsPanel.add(impAllButton, null);
    pricelistsPanel.add(grid, BorderLayout.CENTER);
    grid.getColumnContainer().add(colCompany, null);
    grid.getColumnContainer().add(colPricelist, null);
    grid.getColumnContainer().add(colDescr, null);
    grid.getColumnContainer().add(colCurrencyCode, null);
    pricesGrid.getColumnContainer().add(colItemDescr, null);
    pricesGrid.getColumnContainer().add(colValue, null);
    pricesGrid.getColumnContainer().add(colStartDate, null);
    pricesGrid.getColumnContainer().add(colEndDate, null);
    split.setDividerLocation(200);
    prices2Split.setDividerLocation(splitDiv);
  }


  public void dateChanged(Date oldDate,Date newDate) {
	  if (grid.getMode()!=Consts.READONLY)
		  return;
	  if (controlDate.getDate()==null)
		  pricesGrid.getOtherGridParams().remove(ApplicationConsts.DATE_FILTER);
	  else
		  pricesGrid.getOtherGridParams().put(ApplicationConsts.DATE_FILTER,newDate);
	  pricesGrid.reloadData();
  }


  public GridControl getGrid() {
    return grid;
  }


  public GridControl getPricesGrid() {
    return pricesGrid;
  }


  public final void setPricesPanelEnabled(boolean enabled) {
    if (enabled) {
      insertButton1.setEnabled(true);
      copyButton1.setEnabled(true);
      editButton1.setEnabled(true);
      deleteButton1.setEnabled(true);
      exportButton1.setEnabled(true);
    }
    else {
      insertButton1.setEnabled(false);
      copyButton1.setEnabled(false);
      editButton1.setEnabled(false);
      deleteButton1.setEnabled(false);
      exportButton1.setEnabled(false);
    }
  }


  public TreeServerDataLocator getTreeLevelDataLocator() {
    return treeLevelDataLocator;
  }


  /**
   * Method invoked when pressing advanced button: it will show a dialog window to apply changes on the whole items of the current pricelist.
   */
  void advancedButton_actionPerformed(ActionEvent e) {
    if (grid.getSelectedRow()!=-1) {
      PricelistVO vo = (PricelistVO) grid.getVOListTableModel().getObjectForRow(grid.getSelectedRow());
      new AdvancedOptionsDialog(this,new PricelistChanges(
          vo.getCompanyCodeSys01SAL01(),
          vo.getPricelistCodeSAL01()
      ));
    }
  }


  /**
   * Method invoked when pressing import all items button: it will show a dialog window to apply specified settings on the whole imported items, inside the current pricelist.
   */
  void impAllButton_actionPerformed(ActionEvent e) {
    if (grid.getSelectedRow()!=-1) {
      final PricelistVO vo = (PricelistVO) grid.getVOListTableModel().getObjectForRow(grid.getSelectedRow());
      ArrayList list = new ArrayList();
      new ImportItemsDialog(list,new ImportItems() {
        public void saveItems(ArrayList items,Date startDate,Date endDate,BigDecimal price) {
          PricelistChanges itemsVO = new PricelistChanges(
            vo.getCompanyCodeSys01SAL01(),
            vo.getPricelistCodeSAL01()
          );
          itemsVO.setStartDate(new java.sql.Date(startDate.getTime()));
          itemsVO.setEndDate(new java.sql.Date(endDate.getTime()));
          itemsVO.setDeltaValue(price);
          Response res = ClientUtils.getData("importAllItems",itemsVO);
          if (!res.isError())
            pricesGrid.reloadData();
        }
      });
    }
  }


}

class PricelistFrame_advancedButton_actionAdapter implements java.awt.event.ActionListener {
  PricelistFrame adaptee;

  PricelistFrame_advancedButton_actionAdapter(PricelistFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.advancedButton_actionPerformed(e);
  }
}

class PricelistFrame_impAllButton_actionAdapter implements java.awt.event.ActionListener {
  PricelistFrame adaptee;

  PricelistFrame_impAllButton_actionAdapter(PricelistFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.impAllButton_actionPerformed(e);
  }
}
