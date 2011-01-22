package org.jallinone.purchases.suppliers.client;

import java.math.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import org.jallinone.commons.client.*;
import org.jallinone.subjects.client.*;
import org.jallinone.subjects.java.*;
import org.openswing.swing.client.*;
import org.openswing.swing.form.client.*;
import org.openswing.swing.lookup.client.*;
import org.openswing.swing.mdi.client.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.util.java.Consts;
import org.jallinone.purchases.suppliers.java.*;
import org.openswing.swing.table.java.ServerGridDataLocator;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.table.columns.client.*;
import org.jallinone.hierarchies.client.*;
import org.openswing.swing.message.receive.java.*;
import java.util.Collection;
import org.jallinone.registers.measure.java.MeasureVO;
import org.jallinone.purchases.items.java.SupplierItemVO;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.domains.java.Domain;
import org.jallinone.items.java.ItemTypeVO;
import java.util.ArrayList;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.items.java.GridItemVO;
import org.jallinone.purchases.pricelist.client.*;
import java.util.HashSet;
import javax.swing.tree.DefaultMutableTreeNode;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Detail frame related to a supplier.</p>
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
public class SupplierDetailFrame extends InternalFrame {
  JPanel buttonsPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  InsertButton insertButton = new InsertButton();
  EditButton editButton = new EditButton();
  SaveButton saveButton = new SaveButton();
  ReloadButton reloadButton = new ReloadButton();
  DeleteButton deleteButton = new DeleteButton();
  OrganizationPanel organizationPanel = new OrganizationPanel(false);
  LabelControl labelCompanyCode = new LabelControl();
  CompaniesComboControl controlCompanyCode = new CompaniesComboControl();
  JTabbedPane tabbedPane = new JTabbedPane();
  Form supplierPanel = new Form();
  JPanel subjectPanel = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  TitledBorder titledBorder1;
  TitledBorder titledBorder2;
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  LabelControl labelSupplierCode = new LabelControl();
  TextControl controlSupplierCode = new TextControl();
  LabelControl labelPay = new LabelControl();
  CodLookupControl controlPayment = new CodLookupControl();
  TextControl controlPayDescr = new TextControl();
  LabelControl labelPricelist = new LabelControl();
  LabelControl labelBank = new LabelControl();
  CodLookupControl controlBank = new CodLookupControl();
  TextControl controlBankDescr = new TextControl();

  LookupController bankController = new LookupController();
  LookupServerDataLocator bankDataLocator = new LookupServerDataLocator();

  LookupController payController = new LookupController();
  LookupServerDataLocator payDataLocator = new LookupServerDataLocator();

  JPanel refPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  BorderLayout borderLayout2 = new BorderLayout();
  BorderLayout borderLayout4 = new BorderLayout();
  ReferencesPanel referencesPanel = new ReferencesPanel();
  JPanel hierarPanel = new JPanel();
  SubjectHierarchyLevelsPanel hierarchiesPanel = new SubjectHierarchyLevelsPanel();
  JPanel treeGridItemsPanel = new JPanel();
  BorderLayout borderLayout3 = new BorderLayout();
  JSplitPane itemsSplitPane = new JSplitPane();
  HierarTreePanel treePanel = new HierarTreePanel();
  JPanel itemsPanel = new JPanel();
  BorderLayout borderLayout5 = new BorderLayout();
  JPanel itemButtonsPanel = new JPanel();
  FlowLayout flowLayout2 = new FlowLayout();
  GridControl itemsGrid = new GridControl();
  InsertButton insertButton1 = new InsertButton();
  EditButton editButton1 = new EditButton();
  SaveButton saveButton1 = new SaveButton();
  ReloadButton reloadButton1 = new ReloadButton();
  ExportButton exportButton1 = new ExportButton();
  NavigatorBar navigatorBar1 = new NavigatorBar();
  DeleteButton deleteButton1 = new DeleteButton();
  JPanel itemHierarsPanel = new JPanel();
  LabelControl labelHierar = new LabelControl();
  ComboBoxControl controlHierarchy = new ComboBoxControl();
  GridBagLayout gridBagLayout3 = new GridBagLayout();
  CodLookupColumn colItemCode = new CodLookupColumn();
  TextColumn colItemDescr = new TextColumn();
  TextColumn colSupplierItemCode = new TextColumn();
  CodLookupColumn colUmCode = new CodLookupColumn();
  DecimalColumn colMinQty = new DecimalColumn();
  DecimalColumn colMultipleQty = new DecimalColumn();

  LookupController itemController = new LookupController();
  LookupServerDataLocator itemDataLocator = new LookupServerDataLocator();

  LookupController umController = new LookupController();
  LookupServerDataLocator umDataLocator = new LookupServerDataLocator();

  /** items grid data locator */
  private ServerGridDataLocator itemsGridDataLocator = new ServerGridDataLocator();

  SupplierPricelistPanel supplierPricelistPanel = new SupplierPricelistPanel();

  JPanel detailPanel = new JPanel();
  GridBagLayout gridBagLayout4 = new GridBagLayout();
  GenericButton impAllItemsButton = new GenericButton(new ImageIcon(ClientUtils.getImage("doc3.gif")));

  LabelControl labelDebit = new LabelControl();
  LabelControl labelPurchase = new LabelControl();
  CodLookupControl controlDebitsCode = new CodLookupControl();
  TextControl controlDebitsDescr = new TextControl();
  CodLookupControl controlCostsCode = new CodLookupControl();
  TextControl controlCostsDescr = new TextControl();
  LookupController debitController = new LookupController();
  LookupServerDataLocator debitDataLocator = new LookupServerDataLocator();
  LookupController costsController = new LookupController();
  LookupServerDataLocator costsDataLocator = new LookupServerDataLocator();
  NavigatorBar navigatorBar = new NavigatorBar();


  public SupplierDetailFrame(final SupplierController controller) {
    try {
      jbInit();
      setSize(750,550);
      setMinimumSize(new Dimension(750, 550));

      supplierPanel.setFormController(controller);
      supplierPanel.setInsertButton(insertButton);
      supplierPanel.setEditButton(editButton);
      supplierPanel.setReloadButton(reloadButton);
      supplierPanel.setDeleteButton(deleteButton);
      supplierPanel.setSaveButton(saveButton);
      supplierPanel.setFunctionId("PUR01");
      organizationPanel.setFunctionId("PUR01");

      // link the parent grid to the current Form...
      HashSet pk = new HashSet();
      pk.add("companyCodeSys01REG04");
      pk.add("progressiveREG04");
      supplierPanel.linkGrid(controller.getGridFrame().getGrid(),pk,true,true,true,navigatorBar);


      // banks lookup...
      bankDataLocator.setGridMethodName("loadBanks");
      bankDataLocator.setValidationMethodName("validateBankCode");
      controlBank.setLookupController(bankController);
      controlBank.setControllerMethodName("getBanksList");
      bankController.setLookupDataLocator(bankDataLocator);
      bankController.setFrameTitle("banks");
      bankController.setLookupValueObjectClassName("org.jallinone.registers.bank.java.BankVO");
      bankController.addLookup2ParentLink("bankCodeREG12", "bankCodeReg12PUR01");
      bankController.addLookup2ParentLink("descriptionREG12","descriptionREG12");
      bankController.setAllColumnVisible(false);
      bankController.setVisibleColumn("bankCodeREG12", true);
      bankController.setVisibleColumn("descriptionREG12", true);
      bankController.setPreferredWidthColumn("descriptionREG12",200);
      new CustomizedColumns(new BigDecimal(232),bankController);

      // payments lookup...
      payDataLocator.setGridMethodName("loadPayments");
      payDataLocator.setValidationMethodName("validatePaymentCode");
      controlPayment.setLookupController(payController);
      controlPayment.setControllerMethodName("getPaymentsList");
      payController.setLookupDataLocator(payDataLocator);
      payController.setFrameTitle("payments");
      payController.setLookupValueObjectClassName("org.jallinone.registers.payments.java.PaymentVO");
      payController.addLookup2ParentLink("paymentCodeREG10", "paymentCodeReg10PUR01");
      payController.addLookup2ParentLink("descriptionSYS10","paymentDescriptionSYS10");
      payController.setAllColumnVisible(false);
      payController.setVisibleColumn("paymentCodeREG10", true);
      payController.setVisibleColumn("descriptionSYS10", true);
      payController.setPreferredWidthColumn("descriptionSYS10",200);
      new CustomizedColumns(new BigDecimal(212),payController);
			payController.addLookupListener(new LookupListener() {

				public void beforeLookupAction(org.openswing.swing.message.receive.java.ValueObject parentVO) {
					DetailSupplierVO vo = (DetailSupplierVO)parentVO;
					payDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01REG04());
					payDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01REG04());
				}

				public void codeChanged(org.openswing.swing.message.receive.java.ValueObject parentVO,Collection parentChangedAttributes) {	}

				public void codeValidated(boolean validated) { }

				public void forceValidate() { }

			});


      // items lookup...
      itemDataLocator.setGridMethodName("loadItems");
      itemDataLocator.setValidationMethodName("validateItemCode");
      colItemCode.setLookupController(itemController);
      colItemCode.setControllerMethodName("getItemsList");
      itemController.setLookupDataLocator(itemDataLocator);
      itemController.setFrameTitle("items");
      itemController.setLookupValueObjectClassName("org.jallinone.items.java.GridItemVO");
      itemController.addLookup2ParentLink("companyCodeSys01ITM01", "companyCodeSys01PUR02");
      itemController.addLookup2ParentLink("itemCodeITM01", "itemCodeItm01PUR02");
      itemController.addLookup2ParentLink("descriptionSYS10","descriptionSYS10");
      itemController.setAllColumnVisible(false);
      itemController.setVisibleColumn("itemCodeITM01", true);
      itemController.setVisibleColumn("descriptionSYS10", true);
      itemController.setPreferredWidthColumn("descriptionSYS10",200);
      new CustomizedColumns(ApplicationConsts.ID_ITEMS_GRID,itemController);
      itemController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          GridItemVO vo = (GridItemVO)itemController.getLookupVO();
          if (vo.getItemCodeITM01()!=null) {
            SupplierItemVO supplierVO = (SupplierItemVO)parentVO;
            supplierVO.setSupplierItemCodePUR02(vo.getItemCodeITM01());
          }
        }

        public void beforeLookupAction(ValueObject parentVO) {}

        public void forceValidate() {}

      });



      // purchase um lookup...
      umDataLocator.setGridMethodName("loadMeasures");
      umDataLocator.setValidationMethodName("validateMeasureCode");
      colUmCode.setLookupController(umController);
      colUmCode.setControllerMethodName("getMeasureUnitsList");
      umController.setLookupDataLocator(umDataLocator);
      umController.setFrameTitle("measures");
      umController.setLookupValueObjectClassName("org.jallinone.registers.measure.java.MeasureVO");
      umController.addLookup2ParentLink("umCodeREG02", "umCodeReg02PUR02");
      umController.addLookup2ParentLink("decimalsREG02","decimalsREG02");
      umController.setAllColumnVisible(false);
      umController.setVisibleColumn("umCodeREG02", true);
      umController.setVisibleColumn("decimalsREG02", true);
      new CustomizedColumns(ApplicationConsts.ID_UM_GRID,umController);
      umController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          MeasureVO vo = (MeasureVO)umController.getLookupVO();
          if (vo.getUmCodeREG02()!=null) {
            colMinQty.setDecimals(vo.getDecimalsREG02().intValue());
            colMultipleQty.setDecimals(vo.getDecimalsREG02().intValue());
            SupplierItemVO supplierVO = (SupplierItemVO)parentVO;
            if (supplierVO.getMinPurchaseQtyPUR02()!=null)
              supplierVO.setMinPurchaseQtyPUR02(supplierVO.getMinPurchaseQtyPUR02().setScale(vo.getDecimalsREG02().intValue(),BigDecimal.ROUND_HALF_UP));
            if (supplierVO.getMultipleQtyPUR02()!=null)
              supplierVO.setMultipleQtyPUR02(supplierVO.getMultipleQtyPUR02().setScale(vo.getDecimalsREG02().intValue(),BigDecimal.ROUND_HALF_UP));
          }
        }

        public void beforeLookupAction(ValueObject parentVO) {}

        public void forceValidate() {}

      });

      itemsGrid.setController(new SupplierItemsController(this));
      itemsGrid.setGridDataLocator(itemsGridDataLocator);
      itemsGridDataLocator.setServerMethodName("loadSupplierItems");

      treePanel.addHierarTreeListener(new HierarTreeListener(){

        public void loadDataCompleted(boolean error) {
          if (treePanel.getTree().getRowCount()>0)
            treePanel.getTree().setSelectionRow(0);
          if (treePanel.getTree().getSelectionPath()!=null)
            controller.leftClick((DefaultMutableTreeNode)treePanel.getTree().getSelectionPath().getLastPathComponent());
        }

      });

      treePanel.setTreeController(controller);
      treePanel.setFunctionId("PUR01");

      init();

      itemsGrid.enableDrag(ApplicationConsts.ID_SUPPLIER_ITEMS_GRID.toString());


      // debit account code lookup...
      controlDebitsCode.setLookupController(debitController);
      controlDebitsCode.setControllerMethodName("getAccounts");
      debitController.setLookupDataLocator(debitDataLocator);
      debitDataLocator.setGridMethodName("loadAccounts");
      debitDataLocator.setValidationMethodName("validateAccountCode");
      debitController.setFrameTitle("accounts");
      debitController.setAllowTreeLeafSelectionOnly(false);
      debitController.setLookupValueObjectClassName("org.jallinone.accounting.accounts.java.AccountVO");
      debitController.addLookup2ParentLink("accountCodeACC02", "debitAccountCodeAcc02PUR01");
      debitController.addLookup2ParentLink("descriptionSYS10","debitAccountDescrPUR01");
      debitController.setFramePreferedSize(new Dimension(400,400));
      debitController.setAllColumnVisible(false);
      debitController.setVisibleColumn("accountCodeACC02", true);
      debitController.setVisibleColumn("descriptionSYS10", true);
      debitController.setFilterableColumn("accountCodeACC02", true);
      debitController.setFilterableColumn("descriptionSYS10", true);
      debitController.setSortedColumn("accountCodeACC02", "ASC", 1);
      debitController.setSortableColumn("accountCodeACC02", true);
      debitController.setSortableColumn("descriptionSYS10", true);
      debitController.setPreferredWidthColumn("accountCodeACC02", 100);
      debitController.setPreferredWidthColumn("descriptionSYS10", 290);
      debitController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) { }

        public void beforeLookupAction(ValueObject parentVO) {
          debitDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanyCode.getValue());
          debitDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanyCode.getValue());
        }

        public void forceValidate() {}

      });


      // costs account code lookup...
      controlCostsCode.setLookupController(costsController);
      controlCostsCode.setControllerMethodName("getAccounts");
      costsController.setLookupDataLocator(costsDataLocator);
      costsDataLocator.setGridMethodName("loadAccounts");
      costsDataLocator.setValidationMethodName("validateAccountCode");
      costsController.setFrameTitle("accounts");
      costsController.setAllowTreeLeafSelectionOnly(false);
      costsController.setLookupValueObjectClassName("org.jallinone.accounting.accounts.java.AccountVO");
      costsController.addLookup2ParentLink("accountCodeACC02", "costsAccountCodeAcc02PUR01");
      costsController.addLookup2ParentLink("descriptionSYS10","costsAccountDescrPUR01");
      costsController.setFramePreferedSize(new Dimension(400,400));
      costsController.setAllColumnVisible(false);
      costsController.setVisibleColumn("accountCodeACC02", true);
      costsController.setVisibleColumn("descriptionSYS10", true);
      costsController.setFilterableColumn("accountCodeACC02", true);
      costsController.setFilterableColumn("descriptionSYS10", true);
      costsController.setSortedColumn("accountCodeACC02", "ASC", 1);
      costsController.setSortableColumn("accountCodeACC02", true);
      costsController.setSortableColumn("descriptionSYS10", true);
      costsController.setPreferredWidthColumn("accountCodeACC02", 100);
      costsController.setPreferredWidthColumn("descriptionSYS10", 290);
      costsController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) { }

        public void beforeLookupAction(ValueObject parentVO) {
          costsDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanyCode.getValue());
          costsDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanyCode.getValue());
        }

        public void forceValidate() {}

      });



      CustomizedControls customizedControls = new CustomizedControls(tabbedPane,supplierPanel,ApplicationConsts.ID_SUPPLIER_GRID);
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
    Domain d = new Domain("ITEM_TYPES");
    if (!res.isError()) {
      ItemTypeVO vo = null;
      java.util.List list = ((VOListResponse)res).getRows();
      for(int i=0;i<list.size();i++) {
        vo = (ItemTypeVO)list.get(i);
        d.addDomainPair(vo.getProgressiveHie02ITM02(),vo.getDescriptionSYS10());
      }
    }
    controlHierarchy.setDomain(d);
    controlHierarchy.getComboBox().addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange()==e.SELECTED) {
          treePanel.setProgressiveHIE02((BigDecimal)controlHierarchy.getValue());
          DetailSupplierVO vo = (DetailSupplierVO)supplierPanel.getVOModel().getValueObject();
          treePanel.setCompanyCode(vo.getCompanyCodeSys01REG04());
          treePanel.reloadTree();
          itemsGrid.clearData();
        }
      }
    });
    if (d.getDomainPairList().length==1)
      controlHierarchy.getComboBox().setSelectedIndex(0);
    else
      controlHierarchy.getComboBox().setSelectedIndex(-1);
  }


  private void jbInit() throws Exception {
    itemsGrid.setMaxNumberOfRowsOnInsert(50);
    impAllItemsButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("import all items"));
    impAllItemsButton.addActionListener(new SupplierDetailFrame_impAllItemsButton_actionAdapter(this));

    detailPanel.setLayout(gridBagLayout4);
    itemsGrid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    supplierPanel.setVOClassName("org.jallinone.purchases.suppliers.java.DetailSupplierVO");
    supplierPanel.addLinkedPanel(organizationPanel);
    titledBorder1 = new TitledBorder("");
    titledBorder2 = new TitledBorder("");
    subjectPanel.setLayout(gridBagLayout1);
    supplierPanel.setBorder(titledBorder1);
    supplierPanel.setLayout(gridBagLayout2);
    titledBorder1.setTitle(ClientSettings.getInstance().getResources().getResource("supplier"));
    titledBorder1.setTitleColor(Color.blue);
    labelSupplierCode.setText("supplierCodePUR01");
    labelPay.setText("payment terms");
    labelBank.setText("bank");
    controlSupplierCode.setAttributeName("supplierCodePUR01");
    controlSupplierCode.setCanCopy(false);
    controlSupplierCode.setLinkLabel(labelSupplierCode);
    controlSupplierCode.setMaxCharacters(20);
//    controlSupplierCode.setRequired(true);
    controlSupplierCode.setTrimText(true);
    controlSupplierCode.setUpperCase(true);
    controlSupplierCode.setEnabledOnEdit(false);
    controlPayment.setAttributeName("paymentCodeReg10PUR01");
    controlPayment.setCanCopy(true);
    controlPayment.setLinkLabel(labelPay);
    controlPayment.setMaxCharacters(20);
    controlPayment.setRequired(true);
    controlPayDescr.setAttributeName("paymentDescriptionSYS10");
    controlPayDescr.setCanCopy(true);
    controlPayDescr.setEnabledOnInsert(false);
    controlPayDescr.setEnabledOnEdit(false);
    controlBank.setAttributeName("bankCodeReg12PUR01");
    controlBank.setCanCopy(true);
    controlBank.setLinkLabel(labelBank);
    controlBank.setMaxCharacters(20);
    controlBankDescr.setAttributeName("descriptionREG12");
    controlBankDescr.setCanCopy(true);
    controlBankDescr.setEnabledOnInsert(false);
    controlBankDescr.setEnabledOnEdit(false);
    refPanel.setLayout(borderLayout1);
    hierarPanel.setLayout(borderLayout4);
    treeGridItemsPanel.setLayout(borderLayout3);
    itemsSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
    itemsSplitPane.setDividerSize(5);
    itemsPanel.setLayout(borderLayout5);
    itemButtonsPanel.setLayout(flowLayout2);
    flowLayout2.setAlignment(FlowLayout.LEFT);
    itemsGrid.setAutoLoadData(false);
    itemsGrid.setDeleteButton(deleteButton1);
    itemsGrid.setEditButton(editButton1);
    itemsGrid.setExportButton(exportButton1);
    itemsGrid.setFunctionId("PUR01");
    itemsGrid.setMaxSortedColumns(3);
    itemsGrid.setInsertButton(insertButton1);
    itemsGrid.setNavBar(navigatorBar1);
    itemsGrid.setReloadButton(reloadButton1);
    itemsGrid.setSaveButton(saveButton1);
    itemsGrid.setValueObjectClassName("org.jallinone.purchases.items.java.SupplierItemVO");
    insertButton1.setText("insertButton1");
    editButton1.setText("editButton1");
    saveButton1.setText("saveButton1");
    reloadButton1.setText("reloadButton1");
    deleteButton1.setText("deleteButton1");
    itemHierarsPanel.setLayout(gridBagLayout3);
    labelHierar.setText("item hierarchies");
    colItemCode.setColumnFilterable(true);
    colItemCode.setColumnName("itemCodeItm01PUR02");
    colItemCode.setColumnSortable(true);
    colItemCode.setEditableOnInsert(true);
    colItemCode.setHeaderColumnName("itemCodeITM01");
    colItemCode.setPreferredWidth(90);
    colItemCode.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colItemCode.setMaxCharacters(20);
    colItemDescr.setColumnFilterable(true);
    colItemDescr.setColumnName("descriptionSYS10");
    colItemDescr.setColumnSortable(true);
    colItemDescr.setHeaderColumnName("itemDescriptionSYS10");
    colItemDescr.setPreferredWidth(200);
    colSupplierItemCode.setMaxCharacters(20);
    colSupplierItemCode.setTrimText(true);
    colSupplierItemCode.setUpperCase(true);
    colSupplierItemCode.setColumnFilterable(true);
    colSupplierItemCode.setColumnName("supplierItemCodePUR02");
    colSupplierItemCode.setColumnSortable(true);
    colSupplierItemCode.setEditableOnEdit(true);
    colSupplierItemCode.setEditableOnInsert(true);
    colSupplierItemCode.setHeaderColumnName("supplierItemCodePUR02");
    colSupplierItemCode.setPreferredWidth(120);
    colUmCode.setColumnDuplicable(true);
    colUmCode.setColumnFilterable(true);
    colUmCode.setColumnName("umCodeReg02PUR02");
    colUmCode.setEditableOnEdit(true);
    colUmCode.setEditableOnInsert(true);
    colUmCode.setHeaderColumnName("umCodeReg02PUR02");
    colUmCode.setMaxCharacters(20);
    colMinQty.setDecimals(2);
    colMinQty.setGrouping(false);
    colMinQty.setColumnDuplicable(true);
    colMinQty.setColumnFilterable(true);
    colMinQty.setColumnSortable(true);
    colMinQty.setEditableOnEdit(true);
    colMinQty.setEditableOnInsert(true);
    colMinQty.setPreferredWidth(80);
    colMinQty.setColumnName("minPurchaseQtyPUR02");
    colMultipleQty.setGrouping(false);
    colMultipleQty.setColumnDuplicable(true);
    colMultipleQty.setColumnFilterable(true);
    colMultipleQty.setColumnSortable(true);
    colMultipleQty.setEditableOnEdit(true);
    colMultipleQty.setEditableOnInsert(true);
    colMultipleQty.setPreferredWidth(80);
    colMultipleQty.setColumnName("multipleQtyPUR02");
    subjectPanel.add(organizationPanel,       new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    this.setTitle(ClientSettings.getInstance().getResources().getResource("supplier detail"));
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    insertButton.setText("insertButton1");
    editButton.setText("editButton1");
    saveButton.setEnabled(false);
    saveButton.setText("saveButton1");
    reloadButton.setText("reloadButton1");
    deleteButton.setText("deleteButton1");

    labelCompanyCode.setText("companyCodeSys01REG04");
    controlCompanyCode.setAttributeName("companyCodeSys01REG04");
    controlCompanyCode.setLinkLabel(labelCompanyCode);
    controlCompanyCode.setRequired(true);
    controlCompanyCode.setEnabledOnEdit(false);

    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(saveButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(deleteButton, null);
    buttonsPanel.add(navigatorBar, null);
//    tabbedPane.add(subjectPanel,   "generic data");
    this.getContentPane().add(tabbedPane, BorderLayout.CENTER);
    supplierPanel.add(labelCompanyCode,        new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    supplierPanel.add(controlCompanyCode,           new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

//    tabbedPane.add(supplierPanel,    "supplierPanel");
    supplierPanel.add(labelSupplierCode,       new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
    supplierPanel.add(controlSupplierCode,                       new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    supplierPanel.add(labelPay,       new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    supplierPanel.add(controlPayment,              new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 40, 0));
    supplierPanel.add(controlPayDescr,       new GridBagConstraints(2, 2, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    supplierPanel.add(labelPricelist,       new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    supplierPanel.add(labelBank,         new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
    supplierPanel.add(controlBank,           new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 40, 0));
    supplierPanel.add(controlBankDescr,       new GridBagConstraints(2, 4, 2, 3, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0, 0));

    supplierPanel.add(labelDebit,           new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    supplierPanel.add(labelPurchase,            new GridBagConstraints(0, 6, 1, 1, 0.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 15, 5), 0, 0));
    supplierPanel.add(controlDebitsCode,         new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 40, 0));
    supplierPanel.add(controlDebitsDescr,           new GridBagConstraints(2, 5, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0, 0));
    supplierPanel.add(controlCostsCode,          new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 40, 0));
    supplierPanel.add(controlCostsDescr,       new GridBagConstraints(2, 6, 2, 3, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0, 0));

    labelDebit.setText("debits account");
    labelPurchase.setText("purchase costs account");
    controlDebitsCode.setAttributeName("debitAccountCodeAcc02PUR01");
    controlDebitsDescr.setAttributeName("debitAccountDescrPUR01");
    controlCostsCode.setAttributeName("costsAccountCodeAcc02PUR01");
    controlCostsDescr.setAttributeName("costsAccountDescrPUR01");


    detailPanel.add(subjectPanel,        new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    detailPanel.add(supplierPanel,        new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    tabbedPane.add(detailPanel,   "supplier data");

    tabbedPane.add(refPanel,   "references");
    refPanel.add(referencesPanel, BorderLayout.CENTER);
    tabbedPane.add(hierarPanel,  "hierarchies");
    hierarPanel.add(hierarchiesPanel,  BorderLayout.CENTER);
    tabbedPane.add(treeGridItemsPanel,    "supplierItems");
    treeGridItemsPanel.add(itemsSplitPane,  BorderLayout.CENTER);
    itemsSplitPane.add(treePanel, JSplitPane.LEFT);
    itemsSplitPane.add(itemsPanel, JSplitPane.RIGHT);
    itemsPanel.add(itemButtonsPanel, BorderLayout.NORTH);
    itemsPanel.add(itemsGrid, BorderLayout.CENTER);
    itemsGrid.getColumnContainer().add(colItemCode, null);
    itemButtonsPanel.add(insertButton1, null);
    itemButtonsPanel.add(editButton1, null);
    itemButtonsPanel.add(saveButton1, null);
    itemButtonsPanel.add(reloadButton1, null);
    itemButtonsPanel.add(deleteButton1, null);
    itemButtonsPanel.add(exportButton1, null);
    itemButtonsPanel.add(navigatorBar1, null);
    itemButtonsPanel.add(impAllItemsButton, null);

    controlDebitsCode.setLinkLabel(labelDebit);
    controlDebitsCode.setMaxCharacters(20);
    controlDebitsCode.setRequired(true);
    controlDebitsDescr.setEnabledOnInsert(false);
    controlDebitsDescr.setEnabledOnEdit(false);
    controlCostsCode.setLinkLabel(labelPurchase);
    controlCostsCode.setMaxCharacters(20);
    controlCostsCode.setRequired(true);
    controlCostsDescr.setEnabledOnInsert(false);
    controlCostsDescr.setEnabledOnEdit(false);

    treeGridItemsPanel.add(itemHierarsPanel, BorderLayout.NORTH);
    itemHierarsPanel.add(labelHierar,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    itemHierarsPanel.add(controlHierarchy,      new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 100, 0));
    tabbedPane.add(supplierPricelistPanel,  "supplierPricelistPanel");
    itemsGrid.getColumnContainer().add(colItemDescr, null);
    itemsGrid.getColumnContainer().add(colSupplierItemCode, null);
    itemsGrid.getColumnContainer().add(colUmCode, null);
    itemsGrid.getColumnContainer().add(colMinQty, null);
    itemsGrid.getColumnContainer().add(colMultipleQty, null);

    tabbedPane.setTitleAt(0,ClientSettings.getInstance().getResources().getResource("supplier data"));
    tabbedPane.setTitleAt(1,ClientSettings.getInstance().getResources().getResource("references"));
    tabbedPane.setTitleAt(2,ClientSettings.getInstance().getResources().getResource("hierarchies"));
    tabbedPane.setTitleAt(3,ClientSettings.getInstance().getResources().getResource("supplierItems"));
    tabbedPane.setTitleAt(4,ClientSettings.getInstance().getResources().getResource("supplierPricelists"));
    itemsSplitPane.setDividerLocation(200);
  }


  public Form getCurrentForm() {
    return supplierPanel;
  }


  public ReferencesPanel getReferencesPanel() {
    return referencesPanel;
  }


  public SubjectHierarchyLevelsPanel getHierarchiesPanel() {
    return hierarchiesPanel;
  }


  public final void setButtonsEnabled(boolean enabled) {
    referencesPanel.setButtonsEnabled(enabled);
    hierarchiesPanel.setButtonsEnabled(enabled);
    supplierPricelistPanel.setButtonsEnabled(enabled);

    insertButton1.setEnabled(enabled);
    editButton1.setEnabled(enabled);
    deleteButton1.setEnabled(enabled);
    exportButton1.setEnabled(enabled);

    treePanel.setEnabled(enabled);

  }


  public GridControl getItemsGrid() {
    return itemsGrid;
  }


  public HierarTreePanel getTreePanel() {
    return treePanel;
  }


  public LookupController getItemController() {
    return itemController;
  }


  public LookupServerDataLocator getItemDataLocator() {
    return itemDataLocator;
  }


  public SupplierPricelistPanel getSupplierPricelistPanel() {
    return supplierPricelistPanel;
  }



  /**
   * Method invoked when pressing import all items button: it add all items to the supplier items collection.
   */
  void impAllItemsButton_actionPerformed(ActionEvent e) {
    DetailSupplierVO vo = (DetailSupplierVO)supplierPanel.getVOModel().getValueObject();
    SupplierItemVO itemVO = new SupplierItemVO();
    itemVO.setCompanyCodeSys01PUR02(vo.getCompanyCodeSys01REG04());
    itemVO.setProgressiveReg04PUR02(vo.getProgressiveREG04());
    itemVO.setProgressiveHie02PUR02((BigDecimal)controlHierarchy.getValue());
    Response res = ClientUtils.getData("importAllItemsToSupplier",itemVO);
    if (!res.isError())
      itemsGrid.reloadData();
  }
  public CodLookupControl getControlDebitsCode() {
    return controlDebitsCode;
  }
  public CodLookupControl getControlCostsCode() {
    return controlCostsCode;
  }


}

class SupplierDetailFrame_impAllItemsButton_actionAdapter implements java.awt.event.ActionListener {
  SupplierDetailFrame adaptee;

  SupplierDetailFrame_impAllItemsButton_actionAdapter(SupplierDetailFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.impAllItemsButton_actionPerformed(e);
  }
}
