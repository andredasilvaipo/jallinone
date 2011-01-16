package org.jallinone.expirations.client;

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
import org.jallinone.commons.client.CustomizedColumns;
import java.math.BigDecimal;
import org.jallinone.expirations.java.ExpirationVO;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.lookup.client.LookupListener;
import org.openswing.swing.message.receive.java.*;
import java.util.Collection;
import org.jallinone.sales.customers.java.GridCustomerVO;
import org.jallinone.purchases.suppliers.java.GridSupplierVO;
import java.util.Date;
import org.openswing.swing.domains.java.Domain;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import org.openswing.swing.form.client.Form;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the sale/purchase expirations grid frame.</p>
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
public class ExpirationsGridFrame extends InternalFrame implements CurrencyColumnSettings {

  JPanel buttonsPanel = new JPanel();
  JPanel topPanel = new JPanel();
  Form filterPanel = new Form();
  FlowLayout flowLayout1 = new FlowLayout();
  BorderLayout borderLayout1 = new BorderLayout();
  ReloadButton reloadButton = new ReloadButton();
  NavigatorBar navigatorBar = new NavigatorBar();
  GridControl grid = new GridControl();

  EditButton editButton = new EditButton();
  SaveButton saveButton = new SaveButton();

  /** grid data locator */
  private ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();
  TextColumn colCompany = new TextColumn();
  TextColumn colDescr = new TextColumn();
  ExportButton exportButton = new ExportButton();
  DateColumn colExpDate = new DateColumn();
  DateColumn colDocDate = new DateColumn();
  ComboColumn colDocType = new ComboColumn();
  IntegerColumn colColNum = new IntegerColumn();
  IntegerColumn colDocYear = new IntegerColumn();
  CurrencyColumn colValue = new CurrencyColumn();
  CheckBoxColumn colPayed = new CheckBoxColumn();
  TextColumn colCustSupplCode = new TextColumn();
  TextColumn colName2 = new TextColumn();
  TextColumn colName1 = new TextColumn();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  LabelControl labelDocType = new LabelControl();
  ComboBoxControl controlDocType = new ComboBoxControl();
  LabelControl labelStartDate = new LabelControl();
  LabelControl labelCustomer = new LabelControl();
  LabelControl labelSupplier = new LabelControl();
  DateControl controlStartDate = new DateControl();
  LabelControl labelEndDate = new LabelControl();
  DateControl controlEndDate = new DateControl();
  CodLookupControl controlCodCustomer = new CodLookupControl();
  CodLookupControl controlCodSupplier = new CodLookupControl();
  TextControl controlCustName1 = new TextControl();
  TextControl controlCustName2 = new TextControl();
  TextControl controlSupplierName1 = new TextControl();
  TextControl controlSupplierName2 = new TextControl();
  CheckBoxControl controlShowPayed = new CheckBoxControl();
  LookupController customerController = new LookupController();
  LookupServerDataLocator customerDataLocator = new LookupServerDataLocator();
  LookupController supplierController = new LookupController();
  LookupServerDataLocator supplierDataLocator = new LookupServerDataLocator();


  public ExpirationsGridFrame(GridController controller) {
    grid.setController(controller);
    grid.setGridDataLocator(gridDataLocator);
    gridDataLocator.setServerMethodName("loadExpirations");
    try {
      jbInit();
      setSize(750,450);
      setMinimumSize(new Dimension(750,450));


      // customer lookup...
      customerDataLocator.setGridMethodName("loadCustomers");
      customerDataLocator.setValidationMethodName("validateCustomerCode");

      controlCodCustomer.setLookupController(customerController);
      controlCodCustomer.setControllerMethodName("getCustomersList");
      customerController.setLookupDataLocator(customerDataLocator);
      customerController.setForm(filterPanel);
      customerController.setFrameTitle("customers");
      customerController.setLookupValueObjectClassName("org.jallinone.sales.customers.java.GridCustomerVO");
      customerController.setAllColumnVisible(false);
      customerController.setVisibleColumn("companyCodeSys01REG04", true);
      customerController.setVisibleColumn("customerCodeSAL07", true);
      customerController.setVisibleColumn("name_1REG04", true);
      customerController.setVisibleColumn("name_2REG04", true);
      customerController.setVisibleColumn("cityREG04", true);
      customerController.setVisibleColumn("provinceREG04", true);
      customerController.setVisibleColumn("countryREG04", true);
      customerController.setVisibleColumn("taxCodeREG04", true);
      customerController.setHeaderColumnName("cityREG04", "city");
      customerController.setHeaderColumnName("provinceREG04", "prov");
      customerController.setHeaderColumnName("countryREG04", "country");
      customerController.setHeaderColumnName("taxCodeREG04", "taxCode");
      customerController.setPreferredWidthColumn("name_1REG04", 200);
      customerController.setPreferredWidthColumn("name_2REG04", 150);
      customerController.setFramePreferedSize(new Dimension(750,500));
//      customerDataLocator.getLookupFrameParams().put(ApplicationConsts.FILTER_COMPANY_FOR_INSERT,"DOC01_ORDERS");
//      customerDataLocator.getLookupValidationParameters().put(ApplicationConsts.FILTER_COMPANY_FOR_INSERT,"DOC01_ORDERS");
      customerController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          GridCustomerVO vo = (GridCustomerVO)customerController.getLookupVO();
          if (vo.getCustomerCodeSAL07()==null || vo.getCustomerCodeSAL07().equals("")) {
            controlCustName1.setText("");
            controlCustName2.setText("");
            grid.getOtherGridParams().remove(ApplicationConsts.COMPANY_CODE_SYS01);
            grid.getOtherGridParams().remove(ApplicationConsts.PROGRESSIVE_REG04);
          }
          else {
            controlCodCustomer.setValue(vo.getCustomerCodeSAL07());
            controlCustName1.setText(vo.getName_1REG04());
            controlCustName2.setText(vo.getName_2REG04());
            controlCodSupplier.setValue("");
            controlSupplierName1.setText("");
            controlSupplierName2.setText("");
            grid.getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01REG04());
            grid.getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_REG04,vo.getProgressiveREG04());
          }
        }

        public void beforeLookupAction(ValueObject parentVO) {}

        public void forceValidate() {}

      });


      // supplier lookup...
      supplierDataLocator.setGridMethodName("loadSuppliers");
      supplierDataLocator.setValidationMethodName("validateSupplierCode");

      controlCodSupplier.setLookupController(supplierController);
      controlCodSupplier.setControllerMethodName("getSuppliersList");
      supplierController.setLookupDataLocator(supplierDataLocator);
      supplierController.setForm(filterPanel);
      supplierController.setFrameTitle("suppliers");
      supplierController.setLookupValueObjectClassName("org.jallinone.purchases.suppliers.java.GridSupplierVO");
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
//      supplierDataLocator.getLookupFrameParams().put(ApplicationConsts.FILTER_COMPANY_FOR_INSERT,"DOC06_ORDERS");
//      supplierDataLocator.getLookupValidationParameters().put(ApplicationConsts.FILTER_COMPANY_FOR_INSERT,"DOC06_ORDERS");
      supplierController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          GridSupplierVO vo = (GridSupplierVO)supplierController.getLookupVO();
          if (vo.getSupplierCodePUR01()==null || vo.getSupplierCodePUR01().equals("")) {
            controlSupplierName1.setText("");
            controlSupplierName2.setText("");
            grid.getOtherGridParams().remove(ApplicationConsts.COMPANY_CODE_SYS01);
            grid.getOtherGridParams().remove(ApplicationConsts.PROGRESSIVE_REG04);
          }
          else {
            controlCodSupplier.setValue(vo.getSupplierCodePUR01());
            controlSupplierName1.setText(vo.getName_1REG04());
            controlSupplierName2.setText(vo.getName_2REG04());
            controlCodCustomer.setValue("");
            controlCustName1.setText("");
            controlCustName2.setText("");
            grid.getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01REG04());
            grid.getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_REG04,vo.getProgressiveREG04());
          }
        }

        public void beforeLookupAction(ValueObject parentVO) {}

        public void forceValidate() {}

      });

      controlStartDate.addDateChangedListener(new DateChangedListener() {
        public void dateChanged(Date oldDate,Date newDate) {
          if (newDate!=null)
            grid.getOtherGridParams().put(ApplicationConsts.START_DATE,newDate);
          else
            grid.getOtherGridParams().remove(ApplicationConsts.START_DATE);
        }
      });

      controlEndDate.addDateChangedListener(new DateChangedListener() {
        public void dateChanged(Date oldDate,Date newDate) {
          if (newDate!=null)
            grid.getOtherGridParams().put(ApplicationConsts.END_DATE,newDate);
          else
            grid.getOtherGridParams().remove(ApplicationConsts.END_DATE);
        }
      });

      final Domain d = new Domain("INVOICE_DOC_TYPES");
      d.addDomainPair("","all invoices");
			d.addDomainPair(ApplicationConsts.SALE_DESK_DOC_TYPE,"desk selling");
      d.addDomainPair(ApplicationConsts.SALE_GENERIC_INVOICE,"sale invoice");
      d.addDomainPair(ApplicationConsts.PURCHASE_GENERIC_INVOICE,"purchase invoice");
      controlDocType.setDomain(d);
      controlDocType.getComboBox().addItemListener(new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
          if (e.getStateChange() == e.SELECTED) {
            if (controlDocType.getValue()==null ||
                controlDocType.getValue().equals(""))
              grid.getOtherGridParams().remove(ApplicationConsts.DOC_TYPE);
            else
              grid.getOtherGridParams().put(ApplicationConsts.DOC_TYPE,controlDocType.getValue());
          }
        }
      });


      controlShowPayed.addItemListener(new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
          if (controlShowPayed.isSelected())
            // show all expirations: remove filter...
            grid.getOtherGridParams().remove(ApplicationConsts.PAYED);
          else
            // show NOT yet payed expirations: apply filter on "N" (not payed)
            grid.getOtherGridParams().put(ApplicationConsts.PAYED,"N");
        }
      });
//      controlShowPayed.setSelected(true);
      grid.getOtherGridParams().put(ApplicationConsts.PAYED,"N");

      colValue.setDynamicSettings(this);









			Domain docTypeDomain = new Domain("DOC_TYPE");
			docTypeDomain.addDomainPair(ApplicationConsts.PURCHASE_INVOICE_DOC_TYPE,"purchase invoice");
			docTypeDomain.addDomainPair(ApplicationConsts.PURCHASE_INVOICE_FROM_DN_DOC_TYPE,"purchase invoice from delivery notes");
			docTypeDomain.addDomainPair(ApplicationConsts.PURCHASE_INVOICE_FROM_PD_DOC_TYPE,"purchase invoice from purchase document");
			docTypeDomain.addDomainPair(ApplicationConsts.PURCHASE_DEBIT_NOTE_DOC_TYPE,"debiting note");
			docTypeDomain.addDomainPair(ApplicationConsts.PURCHASE_GENERIC_INVOICE,"purchase generic document");
			docTypeDomain.addDomainPair(ApplicationConsts.SALE_INVOICE_DOC_TYPE,"sale invoice");
			docTypeDomain.addDomainPair(ApplicationConsts.SALE_INVOICE_FROM_DN_DOC_TYPE,"sale invoice from delivery notes");
			docTypeDomain.addDomainPair(ApplicationConsts.SALE_INVOICE_FROM_SD_DOC_TYPE,"sale invoice from sale document");
			docTypeDomain.addDomainPair(ApplicationConsts.SALE_CREDIT_NOTE_DOC_TYPE,"credit note");
			docTypeDomain.addDomainPair(ApplicationConsts.SALE_GENERIC_INVOICE,"sale generic document");
			docTypeDomain.addDomainPair(ApplicationConsts.SALE_DESK_DOC_TYPE,"desk selling");
		  colDocType.setDomain(docTypeDomain);



    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  public final void reloadData() {
    grid.reloadData();
  }


  private void jbInit() throws Exception {
    grid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    saveButton.setExecuteAsThread(true);
    grid.setValueObjectClassName("org.jallinone.expirations.java.ExpirationVO");
    this.setTitle(ClientSettings.getInstance().getResources().getResource("expirations"));
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    grid.setEditButton(editButton);
    grid.setExportButton(exportButton);
    grid.setFunctionId("DOC19");
    grid.setMaxSortedColumns(4);
    grid.setNavBar(navigatorBar);
    grid.setReloadButton(reloadButton);
    grid.setSaveButton(saveButton);
    colCompany.setTrimText(true);
    colCompany.setUpperCase(true);
    colCompany.setColumnFilterable(true);
    colCompany.setColumnName("companyCodeSys01DOC19");
    colCompany.setColumnSortable(true);
    colCompany.setEditableOnInsert(true);
    colCompany.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colCompany.setSortingOrder(1);
    colDescr.setColumnFilterable(false);
    colDescr.setColumnName("descriptionDOC19");
    colDescr.setColumnSortable(true);
    colDescr.setEditableOnEdit(false);
    colDescr.setHeaderColumnName("descriptionSYS10");
    colDescr.setPreferredWidth(250);
    colExpDate.setColumnFilterable(true);
    colExpDate.setColumnName("expirationDateDOC19");
    colExpDate.setColumnSortable(true);
    colExpDate.setEditableOnEdit(true);
    colExpDate.setEditableOnInsert(false);
//    colExpDate.setSortVersus(org.openswing.swing.util.java.Consts.DESC_SORTED);
    colExpDate.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colDocDate.setColumnName("docDateDOC19");
    colDocDate.setColumnSortable(true);
    colDocType.setColumnFilterable(true);
    colDocType.setColumnName("docTypeDOC19");
    colDocType.setColumnSortable(true);
    colDocType.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colDocType.setSortingOrder(1);
		colDocType.setPreferredWidth(200);
    colColNum.setColumnFilterable(true);
    colColNum.setColumnName("docSequenceDOC19");
    colColNum.setColumnSortable(true);
    colColNum.setPreferredWidth(60);
    colColNum.setSortingOrder(2);
    colColNum.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colDocYear.setColumnFilterable(true);
    colDocYear.setColumnName("docYearDOC19");
    colDocYear.setColumnSortable(true);
    colDocYear.setPreferredWidth(60);
    colValue.setColumnName("valueDOC19");
    colValue.setColumnSortable(true);
    colPayed.setColumnFilterable(true);
    colPayed.setShowDeSelectAllInPopupMenu(true);
    colPayed.setColumnName("payedDOC19");
    colPayed.setColumnSortable(true);
    colPayed.setEditableOnEdit(true);
    colPayed.setPreferredWidth(60);
    colCustSupplCode.setColumnFilterable(true);
    colCustSupplCode.setColumnName("customerSupplierCodeDOC19");
    colCustSupplCode.setColumnSortable(true);
    colCustSupplCode.setPreferredWidth(90);
    colName1.setColumnName("name_1DOC19");
    colName1.setColumnRequired(false);
    colName1.setPreferredWidth(200);
    colName2.setColumnName("name_2DOC19");
    colName2.setColumnRequired(false);
    colName2.setPreferredWidth(150);
    topPanel.setLayout(borderLayout1);
    filterPanel.setLayout(gridBagLayout1);
    labelDocType.setText("docType");
    labelStartDate.setText("startDate");
    labelCustomer.setText("customer");
    labelSupplier.setText("supplier");
    labelEndDate.setText("endDate");
    controlShowPayed.setText("show also payed expirations");
    controlCodCustomer.setMaxCharacters(20);
    controlCustName1.setEnabled(false);
    controlCustName1.setEnabledOnInsert(false);
    controlCustName1.setEnabledOnEdit(false);
    controlCustName2.setEnabled(false);
    controlCustName2.setEnabledOnInsert(false);
    controlCustName2.setEnabledOnEdit(false);
    controlCodSupplier.setMaxCharacters(20);
    controlSupplierName1.setEnabled(false);
    controlSupplierName1.setEnabledOnInsert(false);
    controlSupplierName1.setEnabledOnEdit(false);
    controlSupplierName2.setEnabled(false);
    controlSupplierName2.setEnabledOnInsert(false);
    controlSupplierName2.setEnabledOnEdit(false);
    topPanel.add(filterPanel,BorderLayout.CENTER);
    filterPanel.add(labelDocType,      new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlDocType,       new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 20, 0));
    filterPanel.add(labelStartDate,     new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(labelCustomer,    new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(labelSupplier,   new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    topPanel.add(buttonsPanel,BorderLayout.SOUTH);
    this.getContentPane().add(topPanel, BorderLayout.NORTH);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(saveButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(exportButton, null);
    buttonsPanel.add(navigatorBar, null);
    this.getContentPane().add(grid, BorderLayout.CENTER);
    grid.getColumnContainer().add(colCompany, null);
    grid.getColumnContainer().add(colPayed, null);
    grid.getColumnContainer().add(colValue, null);
    grid.getColumnContainer().add(colExpDate, null);
    grid.getColumnContainer().add(colDocDate, null);
    grid.getColumnContainer().add(colDescr, null);
    grid.getColumnContainer().add(colDocType, null);
    grid.getColumnContainer().add(colColNum, null);
    grid.getColumnContainer().add(colDocYear, null);
    grid.getColumnContainer().add(colCustSupplCode, null);
    grid.getColumnContainer().add(colName1, null);
    grid.getColumnContainer().add(colName2, null);
    filterPanel.add(controlStartDate,   new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(labelEndDate,    new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    filterPanel.add(controlCodCustomer,     new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlCodSupplier,    new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlCustName1,    new GridBagConstraints(2, 2, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    filterPanel.add(controlCustName2,     new GridBagConstraints(4, 2, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    filterPanel.add(controlSupplierName1,     new GridBagConstraints(2, 3, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    filterPanel.add(controlSupplierName2,     new GridBagConstraints(4, 3, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    filterPanel.add(controlShowPayed,    new GridBagConstraints(2, 0, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlEndDate,  new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
  }




  public double getMaxValue(int row) {
    return Double.MAX_VALUE;
  }

  public double getMinValue(int row) {
    return 0.0;
  }

  public boolean isGrouping(int row) {
    return true;
  }

  public int getDecimals(int row) {
    ExpirationVO vo = (ExpirationVO)grid.getVOListTableModel().getObjectForRow(row);
    if (vo!=null)
      return vo.getDecimalsREG03().intValue();
    else
      return 0;
  }

  public String getCurrencySymbol(int row) {
    ExpirationVO vo = (ExpirationVO)grid.getVOListTableModel().getObjectForRow(row);
    if (vo!=null)
      return vo.getCurrencySymbolREG03();
    else
    return "E";
  }


}
