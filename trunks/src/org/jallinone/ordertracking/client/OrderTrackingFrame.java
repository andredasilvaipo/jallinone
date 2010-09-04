package org.jallinone.ordertracking.client;

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
import javax.swing.border.*;
import org.openswing.swing.form.client.Form;
import org.jallinone.commons.client.CustomizedControls;
import org.openswing.swing.util.java.Consts;
import org.jallinone.commons.client.CustomizedColumns;
import org.jallinone.hierarchies.client.*;
import org.jallinone.commons.client.CompaniesComboColumn;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.message.receive.java.Response;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.domains.java.*;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.sales.documents.java.GridSaleDocVO;
import org.jallinone.commons.client.CompaniesComboControl;
import org.openswing.swing.form.client.FormController;
import org.jallinone.ordertracking.java.OrderTrackingFilterVO;
import java.util.Calendar;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import java.awt.event.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Frame that shows the orders tracking.</p>
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
public class OrderTrackingFrame extends InternalFrame {

  Form filterPanel = new Form();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  LabelControl labelSupplier = new LabelControl();
  LabelControl labelDocType = new LabelControl();
  LabelControl labelCustomer = new LabelControl();
  ComboBoxControl controlDocType = new ComboBoxControl();
  LabelControl labelDocYear = new LabelControl();
  NumericControl controlDocYear = new NumericControl();
  LabelControl labelDocState = new LabelControl();
  ComboBoxControl controlDocState = new ComboBoxControl();
  LabelControl labelDocDate = new LabelControl();
  DateControl controlDocDate = new DateControl();
  CodLookupControl controlCustomer = new CodLookupControl();
  CodLookupControl controlSupplier = new CodLookupControl();
  TextControl controlCustName1 = new TextControl();
  TextControl controlSupplierName1 = new TextControl();
  TextControl controlCustName2 = new TextControl();
  LookupController customerController = new LookupController();
  LookupServerDataLocator customerDataLocator = new LookupServerDataLocator();
  LookupController supplierController = new LookupController();
  LookupServerDataLocator supplierDataLocator = new LookupServerDataLocator();
  GenericButton buttonSearch = new GenericButton();
  private Domain docTypeDomain = new Domain("ALL_DOCS");
  OrderTrackingGridPanel orderTrackingGridPanel = new OrderTrackingGridPanel(new Color(220,220,220));


  public OrderTrackingFrame() {
    try {
      jbInit();
      setSize(750,610);
      setMinimumSize(new Dimension(750,610));
      setTitle(ClientSettings.getInstance().getResources().getResource("order tracking"));

      init();

      MDIFrame.add(this);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void init() {
    filterPanel.setFormController(new FormController() {


      /**
       * Callback method invoked on pressing INSERT button, after changing to insert mode.
       */
      public void afterInsertData(Form form) {
        OrderTrackingFilterVO vo = (OrderTrackingFilterVO)form.getVOModel().getValueObject();
        Calendar cal = Calendar.getInstance();
        vo.setDocYear(new BigDecimal(cal.get(cal.YEAR)));
        controlDocYear.setValue(vo.getDocYear());
      }

    });
    filterPanel.setMode(Consts.INSERT);


    // set combo state content...
    controlDocState.setDomainId("DOC06_STATES");

    // set combo docs content...
    docTypeDomain.addDomainPair(ApplicationConsts.SALE_ORDER_DOC_TYPE,"sale order");
    docTypeDomain.addDomainPair(ApplicationConsts.SALE_CONTRACT_DOC_TYPE,"sale contract");
    docTypeDomain.addDomainPair(ApplicationConsts.SALE_DESK_DOC_TYPE,"desk selling");
    docTypeDomain.addDomainPair(ApplicationConsts.SALE_ESTIMATE_DOC_TYPE,"sale estimate");
    docTypeDomain.addDomainPair(ApplicationConsts.PURCHASE_ORDER_DOC_TYPE,"purchase order");
    docTypeDomain.addDomainPair(ApplicationConsts.IN_DELIVERY_NOTE_DOC_TYPE,"in delivery note");
    docTypeDomain.addDomainPair(ApplicationConsts.DELIVERY_REQUEST_DOC_TYPE,"delivery request");
    docTypeDomain.addDomainPair(ApplicationConsts.OUT_DELIVERY_NOTE_DOC_TYPE,"out delivery note");
    docTypeDomain.addDomainPair(ApplicationConsts.SALE_INVOICE_DOC_TYPE,"sale invoice");
    docTypeDomain.addDomainPair(ApplicationConsts.SALE_INVOICE_FROM_DN_DOC_TYPE,"sale invoice from delivery notes");
    docTypeDomain.addDomainPair(ApplicationConsts.SALE_INVOICE_FROM_SD_DOC_TYPE,"sale invoice from sale document");
    docTypeDomain.addDomainPair(ApplicationConsts.SALE_CREDIT_NOTE_DOC_TYPE,"credit note");
    docTypeDomain.addDomainPair(ApplicationConsts.SALE_GENERIC_INVOICE,"sale generic document");
    docTypeDomain.addDomainPair(ApplicationConsts.PURCHASE_INVOICE_DOC_TYPE,"purchase invoice");
    docTypeDomain.addDomainPair(ApplicationConsts.PURCHASE_INVOICE_FROM_DN_DOC_TYPE,"purchase invoice from delivery notes");
    docTypeDomain.addDomainPair(ApplicationConsts.PURCHASE_INVOICE_FROM_PD_DOC_TYPE,"purchase invoice from purchase document");
    docTypeDomain.addDomainPair(ApplicationConsts.PURCHASE_DEBIT_NOTE_DOC_TYPE,"debiting note");
    docTypeDomain.addDomainPair(ApplicationConsts.PURCHASE_GENERIC_INVOICE,"purchase generic document");

    controlDocType.setDomain(docTypeDomain);



    // customer lookup...
    customerDataLocator.setGridMethodName("loadCustomers");
    customerDataLocator.setValidationMethodName("validateCustomerCode");

    controlCustomer.setLookupController(customerController);
    controlCustomer.setControllerMethodName("getCustomersList");
    customerController.setForm(filterPanel);
    customerController.setLookupDataLocator(customerDataLocator);
    customerController.setFrameTitle("customers");
    customerController.setLookupValueObjectClassName("org.jallinone.sales.customers.java.GridCustomerVO");
    customerController.addLookup2ParentLink("companyCodeSys01REG04", "companyCodeSYS01");
    customerController.addLookup2ParentLink("customerCodeSAL07","customerCode");
    customerController.addLookup2ParentLink("progressiveREG04","progressiveREG04");
    customerController.addLookup2ParentLink("name_1REG04", "customerName1");
    customerController.addLookup2ParentLink("name_2REG04", "customerName2");
    customerController.setAllColumnVisible(false);
    customerController.setVisibleColumn("companyCodeSys01REG04", true);
    customerController.setFilterableColumn("companyCodeSys01REG04", true);
    customerController.setFilterableColumn("customerCodeSAL07", true);
    customerController.setFilterableColumn("name_1REG04", true);
    customerController.setFilterableColumn("name_2REG04", true);
    customerController.setFilterableColumn("cityREG04", true);
    customerController.setFilterableColumn("provinceREG04", true);

    customerController.setSortableColumn("companyCodeSys01REG04", true);
    customerController.setSortableColumn("customerCodeSAL07", true);
    customerController.setSortableColumn("name_1REG04", true);
    customerController.setSortableColumn("name_2REG04", true);
    customerController.setSortableColumn("cityREG04", true);
    customerController.setSortableColumn("provinceREG04", true);

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
//    customerDataLocator.getLookupFrameParams().put(ApplicationConsts.FILTER_COMPANY_FOR_INSERT,"DOC01_ORDERS");
//    customerDataLocator.getLookupValidationParameters().put(ApplicationConsts.FILTER_COMPANY_FOR_INSERT,"DOC01_ORDERS");


    // supplier lookup...
    supplierDataLocator.setGridMethodName("loadSuppliers");
    supplierDataLocator.setValidationMethodName("validateSupplierCode");

    controlSupplier.setLookupController(supplierController);
    controlSupplier.setControllerMethodName("getSuppliersList");
    supplierController.setForm(filterPanel);
    supplierController.setLookupDataLocator(supplierDataLocator);
    supplierController.setFrameTitle("suppliers");
    supplierController.setLookupValueObjectClassName("org.jallinone.purchases.suppliers.java.GridSupplierVO");
    supplierController.addLookup2ParentLink("companyCodeSys01REG04", "companyCodeSYS01");
    supplierController.addLookup2ParentLink("supplierCodePUR01","supplierCode");
    supplierController.addLookup2ParentLink("progressiveREG04","progressiveREG04");
    supplierController.addLookup2ParentLink("name_1REG04", "supplierName1");
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
//    supplierDataLocator.getLookupFrameParams().put(ApplicationConsts.FILTER_COMPANY_FOR_INSERT,"DOC06_ORDERS");
//    supplierDataLocator.getLookupValidationParameters().put(ApplicationConsts.FILTER_COMPANY_FOR_INSERT,"DOC06_ORDERS");


  }


  /**
   * Define input controls editable settings according to the document state.
   */
  private void jbInit() {
    this.setAskBeforeClose(false);
    filterPanel.setFunctionId("ORDER_TRACKING");
    filterPanel.setVOClassName("org.jallinone.ordertracking.java.OrderTrackingFilterVO");
    filterPanel.setLayout(gridBagLayout1);
    labelSupplier.setLabel("supplierCodePUR01");
    labelDocType.setLabel("docType");
    labelCustomer.setLabel("customerCodeSAL07");
    labelDocYear.setLabel("docYear");
    labelDocState.setLabel("docState");
    labelDocDate.setLabel("docDate");
    controlCustName2.setAttributeName("customerName2");
    controlCustName2.setEnabledOnInsert(false);
    controlCustName2.setEnabledOnEdit(false);
    controlCustName1.setAttributeName("customerName1");
    controlCustName1.setEnabledOnInsert(false);
    controlCustName1.setEnabledOnEdit(false);
    controlSupplierName1.setAttributeName("supplierName1");
    controlSupplierName1.setEnabledOnInsert(false);
    controlSupplierName1.setEnabledOnEdit(false);
    controlSupplier.setAttributeName("supplierCode");
    controlSupplier.setMaxCharacters(20);
    controlCustomer.setAttributeName("customerCode");
    controlCustomer.setMaxCharacters(20);
    controlDocType.setAttributeName("docType");
    controlDocType.setLinkLabel(labelDocType);
    controlDocType.setRequired(true);
    controlDocYear.setAttributeName("docYear");
    controlDocState.setAttributeName("docState");
    controlDocDate.setAttributeName("docDate");
    buttonSearch.addActionListener(new OrderTrackingFrame_buttonSearch_actionAdapter(this));
    this.getContentPane().add(filterPanel, BorderLayout.NORTH);
    this.getContentPane().add(orderTrackingGridPanel, BorderLayout.CENTER);

    filterPanel.add(labelDocType,    new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(labelCustomer,    new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlDocType,      new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(labelDocYear,     new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlDocYear,     new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(labelDocState,     new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlDocState,      new GridBagConstraints(5, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(labelSupplier,  new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(labelDocDate,   new GridBagConstraints(6, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlDocDate,     new GridBagConstraints(7, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 80, 0));
    filterPanel.add(controlCustomer,   new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlSupplier,   new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlCustName1,   new GridBagConstraints(2, 2, 4, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlSupplierName1,   new GridBagConstraints(2, 3, 4, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlCustName2,  new GridBagConstraints(6, 2, 2, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(buttonSearch,  new GridBagConstraints(7, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    buttonSearch.setIcon(new ImageIcon(ClientUtils.getImage("examine.gif")));
  }


  void buttonSearch_actionPerformed(ActionEvent e) {
    if (filterPanel.push()) {
      orderTrackingGridPanel.getGrid().getOtherGridParams().put(ApplicationConsts.PROPERTIES_FILTER,filterPanel.getVOModel().getValueObject());
      orderTrackingGridPanel.getGrid().reloadData();
    }
  }


}

class OrderTrackingFrame_buttonSearch_actionAdapter implements java.awt.event.ActionListener {
  OrderTrackingFrame adaptee;

  OrderTrackingFrame_buttonSearch_actionAdapter(OrderTrackingFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.buttonSearch_actionPerformed(e);
  }
}
