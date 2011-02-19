package org.jallinone.system.client;

import org.openswing.swing.mdi.client.InternalFrame;
import java.awt.*;
import org.openswing.swing.client.*;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.form.client.Form;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.domains.java.Domain;
import org.jallinone.system.companies.java.CompanyVO;
import java.util.ArrayList;
import org.jallinone.system.java.UserParametersVO;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.jallinone.commons.java.ApplicationConsts;
import javax.swing.*;
import org.openswing.swing.util.java.Consts;
import java.awt.event.*;
import org.openswing.swing.mdi.client.MDIFrame;
import javax.swing.border.*;
import org.openswing.swing.lookup.client.LookupListener;
import java.util.Collection;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Frame used to define user parameters, based on the specified company code.</p>
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

public class UserParametersFrame extends InternalFrame {


  GridBagLayout gridBagLayout1 = new GridBagLayout();
  LabelControl labelCustomer = new LabelControl();
  CodLookupControl controlCustomer = new CodLookupControl();
  TextControl controlName1 = new TextControl();
  TextControl controlName2 = new TextControl();
  JPanel topPanel = new JPanel();
  JPanel buttonsPanel = new JPanel();
  Form retailPanel = new Form();
  BorderLayout borderLayout2 = new BorderLayout();
  EditButton editButton1 = new EditButton();
  SaveButton saveButton1 = new SaveButton();
  ReloadButton reloadButton1 = new ReloadButton();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  LabelControl labelCompanies = new LabelControl();
  ComboBoxControl controlCompanies = new ComboBoxControl();

  LookupController customerController = new LookupController();
  LookupServerDataLocator customerDataLocator = new LookupServerDataLocator();
  LookupController warehouseController = new LookupController();
  LookupServerDataLocator warehouseDataLocator = new LookupServerDataLocator();
  TextControl controlPath = new TextControl();
  JButton buttonPath = new JButton();
  LabelControl labelReceiptMan = new LabelControl();
  JPanel accountPanel = new JPanel();
  GridBagLayout gridBagLayout3 = new GridBagLayout();
  TitledBorder titledBorder1;
  TitledBorder titledBorder2;
  TitledBorder titledBorder3;
  TitledBorder titledBorder4;
  GridBagLayout gridBagLayout4 = new GridBagLayout();
  LabelControl labelCreditAccount = new LabelControl();
  LabelControl labelItemAccount = new LabelControl();
  LabelControl labelActivities = new LabelControl();
  LabelControl labelCharges = new LabelControl();
  LabelControl labelDebit = new LabelControl();
  LabelControl labelPurchase = new LabelControl();
  LabelControl labelCase = new LabelControl();
  LabelControl labelBank = new LabelControl();
  LabelControl labelVatEnd = new LabelControl();

  CodLookupControl controlCreditsCode = new CodLookupControl();
  TextControl controlCreditsDescr = new TextControl();
  CodLookupControl controlItemsCode = new CodLookupControl();
  TextControl controlItemsDescr = new TextControl();
  CodLookupControl controlActCode = new CodLookupControl();
  TextControl controlActDescr = new TextControl();
  CodLookupControl controlChargesCode = new CodLookupControl();
  CodLookupControl controlCaseCode = new CodLookupControl();
  TextControl controlChargesDescr = new TextControl();
  CodLookupControl controlDebitsCode = new CodLookupControl();
  TextControl controlDebitsDescr = new TextControl();
  CodLookupControl controlCostsCode = new CodLookupControl();
  TextControl controlCostsDescr = new TextControl();
  TextControl controlCaseDescr = new TextControl();
  CodLookupControl controlBankCode = new CodLookupControl();
  CodLookupControl controlVatEndCode = new CodLookupControl();
  TextControl controlBankDescr = new TextControl();
  TextControl controlVatEndDescr = new TextControl();

  LookupController creditController = new LookupController();
  LookupServerDataLocator creditDataLocator = new LookupServerDataLocator();
  LookupController itemsController = new LookupController();
  LookupServerDataLocator itemsDataLocator = new LookupServerDataLocator();
  LookupController chargesController = new LookupController();
  LookupServerDataLocator chargesDataLocator = new LookupServerDataLocator();
  LookupController actController = new LookupController();
  LookupServerDataLocator actDataLocator = new LookupServerDataLocator();
  LookupController debitController = new LookupController();
  LookupServerDataLocator debitDataLocator = new LookupServerDataLocator();
  LookupController costsController = new LookupController();
  LookupServerDataLocator costsDataLocator = new LookupServerDataLocator();
  LookupController caseController = new LookupController();
  LookupServerDataLocator caseDataLocator = new LookupServerDataLocator();
  LookupController bankController = new LookupController();
  LookupServerDataLocator bankDataLocator = new LookupServerDataLocator();
  LookupController vatEndController = new LookupController();
  LookupServerDataLocator vatEndDataLocator = new LookupServerDataLocator();
  LabelControl labelWarehouse = new LabelControl();
  CodLookupControl controlWarehouse = new CodLookupControl();
  TextControl controlWareDescr = new TextControl();


  public UserParametersFrame(UserParametersController controller) {
    try {
      jbInit();

      retailPanel.setFormController(controller);

      // customer lookup...
      customerDataLocator.setGridMethodName("loadCustomers");
      customerDataLocator.setValidationMethodName("validateCustomerCode");

      controlCustomer.setLookupController(customerController);
      controlCustomer.setControllerMethodName("getCustomersList");
      customerController.setLookupDataLocator(customerDataLocator);
      customerController.setFrameTitle("customers");
      customerController.setLookupValueObjectClassName("org.jallinone.sales.customers.java.GridCustomerVO");
      customerController.addLookup2ParentLink("customerCodeSAL07","customerCodeSAL07");
      customerController.addLookup2ParentLink("name_1REG04", "name_1REG04");
      customerController.addLookup2ParentLink("name_2REG04", "name_2REG04");
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
      customerController.setHeaderColumnName("name_1REG04", "firstname");
      customerController.setHeaderColumnName("name_2REG04", "lastname");
      customerController.setHeaderColumnName("provinceREG04", "prov");
      customerController.setHeaderColumnName("countryREG04", "country");
      customerController.setHeaderColumnName("taxCodeREG04", "taxCode");
      customerController.setPreferredWidthColumn("name_1REG04", 200);
      customerController.setPreferredWidthColumn("name_2REG04", 150);
      customerController.setFramePreferedSize(new Dimension(750,500));
      customerDataLocator.getLookupFrameParams().put(ApplicationConsts.FILTER_COMPANY_FOR_INSERT,"DOC01_DESKSALES");
      customerDataLocator.getLookupValidationParameters().put(ApplicationConsts.FILTER_COMPANY_FOR_INSERT,"DOC01_DESKSALES");
      customerDataLocator.getLookupFrameParams().put(ApplicationConsts.SUBJECT_TYPE,ApplicationConsts.SUBJECT_PEOPLE_CUSTOMER);
      customerDataLocator.getLookupValidationParameters().put(ApplicationConsts.SUBJECT_TYPE,ApplicationConsts.SUBJECT_PEOPLE_CUSTOMER);


      // warehouse lookup...
      warehouseDataLocator.setGridMethodName("loadWarehouses");
      warehouseDataLocator.setValidationMethodName("validateWarehouseCode");

      controlWarehouse.setLookupController(warehouseController);
      controlWarehouse.setControllerMethodName("getWarehousesList");
      warehouseController.setLookupDataLocator(warehouseDataLocator);
      warehouseController.setFrameTitle("warehouses");
      warehouseController.setLookupValueObjectClassName("org.jallinone.warehouse.java.WarehouseVO");
      warehouseController.addLookup2ParentLink("warehouseCodeWAR01","warehouseCodeWAR01");
      warehouseController.addLookup2ParentLink("descriptionWAR01", "warehouseDescriptionSYS10");
      warehouseController.setAllColumnVisible(false);
      warehouseController.setVisibleColumn("companyCodeSys01WAR01", true);
      warehouseController.setVisibleColumn("warehouseCodeWAR01", true);
      warehouseController.setVisibleColumn("descriptionWAR01", true);
      warehouseController.setPreferredWidthColumn("warehouseCodeWAR01", 100);
      warehouseController.setPreferredWidthColumn("descriptionWAR01", 250);
      warehouseController.setFramePreferedSize(new Dimension(400,500));
      warehouseController.addLookupListener(new LookupListener() {

        public void beforeLookupAction(ValueObject parentVO) {
          UserParametersVO vo = (UserParametersVO)retailPanel.getVOModel().getValueObject();
          if (vo!=null) {
            warehouseDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01SYS19());
            warehouseDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01SYS19());
          }
        }

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) { }

        public void codeValidated(boolean validated) { }

        public void forceValidate() { }

      });


      // credit account code lookup...
      controlCreditsCode.setLookupController(creditController);
      controlCreditsCode.setControllerMethodName("getAccounts");
      creditController.setLookupDataLocator(creditDataLocator);
      creditDataLocator.setGridMethodName("loadAccounts");
      creditDataLocator.setValidationMethodName("validateAccountCode");
      creditController.setFrameTitle("accounts");
      creditController.setLookupValueObjectClassName("org.jallinone.accounting.accounts.java.AccountVO");
      creditController.addLookup2ParentLink("accountCodeACC02", "creditAccountCodeAcc02SAL07");
      creditController.addLookup2ParentLink("descriptionSYS10","creditAccountDescrSAL07");
      creditController.setFramePreferedSize(new Dimension(400,400));
      creditController.setAllColumnVisible(false);
      creditController.setVisibleColumn("accountCodeACC02", true);
      creditController.setVisibleColumn("descriptionSYS10", true);
      creditController.setFilterableColumn("accountCodeACC02", true);
      creditController.setFilterableColumn("descriptionSYS10", true);
      creditController.setSortedColumn("accountCodeACC02", "ASC", 1);
      creditController.setSortableColumn("accountCodeACC02", true);
      creditController.setSortableColumn("descriptionSYS10", true);
      creditController.setPreferredWidthColumn("accountCodeACC02",100);
      creditController.setPreferredWidthColumn("descriptionSYS10",290);
      creditController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) { }

        public void beforeLookupAction(ValueObject parentVO) {
          creditDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanies.getValue());
          creditDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanies.getValue());
        }

        public void forceValidate() {}

      });


      // items account code lookup...
      controlItemsCode.setLookupController(itemsController);
      controlItemsCode.setControllerMethodName("getAccounts");
      itemsController.setLookupDataLocator(itemsDataLocator);
      itemsDataLocator.setGridMethodName("loadAccounts");
      itemsDataLocator.setValidationMethodName("validateAccountCode");
      itemsController.setFrameTitle("accounts");
      itemsController.setLookupValueObjectClassName("org.jallinone.accounting.accounts.java.AccountVO");
      itemsController.addLookup2ParentLink("accountCodeACC02", "itemsAccountCodeAcc02SAL07");
      itemsController.addLookup2ParentLink("descriptionSYS10","itemsAccountDescrSAL07");
      itemsController.setFramePreferedSize(new Dimension(400,400));
      itemsController.setAllColumnVisible(false);
      itemsController.setFilterableColumn("accountCodeACC02", true);
      itemsController.setFilterableColumn("descriptionSYS10", true);
      itemsController.setSortedColumn("accountCodeACC02", "ASC", 1);
      itemsController.setSortableColumn("accountCodeACC02", true);
      itemsController.setSortableColumn("descriptionSYS10", true);
      itemsController.setVisibleColumn("accountCodeACC02", true);
      itemsController.setVisibleColumn("descriptionSYS10", true);
      itemsController.setPreferredWidthColumn("accountCodeACC02",100);
      itemsController.setPreferredWidthColumn("descriptionSYS10",290);
      itemsController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) { }

        public void beforeLookupAction(ValueObject parentVO) {
          itemsDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanies.getValue());
          itemsDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanies.getValue());
        }

        public void forceValidate() {}

      });


      // activities account code lookup...
      controlActCode.setLookupController(actController);
      controlActCode.setControllerMethodName("getAccounts");
      actController.setLookupDataLocator(actDataLocator);
      actDataLocator.setGridMethodName("loadAccounts");
      actDataLocator.setValidationMethodName("validateAccountCode");
      actController.setFrameTitle("accounts");
      actController.setLookupValueObjectClassName("org.jallinone.accounting.accounts.java.AccountVO");
      actController.addLookup2ParentLink("accountCodeACC02", "activitiesAccountCodeAcc02SAL07");
      actController.addLookup2ParentLink("descriptionSYS10","activitiesAccountDescrSAL07");
      actController.setFramePreferedSize(new Dimension(400,400));
      actController.setAllColumnVisible(false);
      actController.setFilterableColumn("accountCodeACC02", true);
      actController.setFilterableColumn("descriptionSYS10", true);
      actController.setSortedColumn("accountCodeACC02", "ASC", 1);
      actController.setSortableColumn("accountCodeACC02", true);
      actController.setSortableColumn("descriptionSYS10", true);
      actController.setVisibleColumn("accountCodeACC02", true);
      actController.setVisibleColumn("descriptionSYS10", true);
      actController.setPreferredWidthColumn("accountCodeACC02",100);
      actController.setPreferredWidthColumn("descriptionSYS10",290);
      actController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) { }

        public void beforeLookupAction(ValueObject parentVO) {
          actDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanies.getValue());
          actDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanies.getValue());
        }

        public void forceValidate() {}

      });


      // charges account code lookup...
      controlChargesCode.setLookupController(chargesController);
      controlChargesCode.setControllerMethodName("getAccounts");
      chargesController.setLookupDataLocator(chargesDataLocator);
      chargesDataLocator.setGridMethodName("loadAccounts");
      chargesDataLocator.setValidationMethodName("validateAccountCode");
      chargesController.setFrameTitle("accounts");
      chargesController.setLookupValueObjectClassName("org.jallinone.accounting.accounts.java.AccountVO");
      chargesController.addLookup2ParentLink("accountCodeACC02", "chargesAccountCodeAcc02SAL07");
      chargesController.addLookup2ParentLink("descriptionSYS10","chargesAccountDescrSAL07");
      chargesController.setFramePreferedSize(new Dimension(400,400));
      chargesController.setAllColumnVisible(false);
      chargesController.setFilterableColumn("accountCodeACC02", true);
      chargesController.setFilterableColumn("descriptionSYS10", true);
      chargesController.setSortedColumn("accountCodeACC02", "ASC", 1);
      chargesController.setSortableColumn("accountCodeACC02", true);
      chargesController.setSortableColumn("descriptionSYS10", true);
      chargesController.setVisibleColumn("accountCodeACC02", true);
      chargesController.setVisibleColumn("descriptionSYS10", true);
      chargesController.setPreferredWidthColumn("accountCodeACC02",100);
      chargesController.setPreferredWidthColumn("descriptionSYS10",290);
      chargesController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) { }

        public void beforeLookupAction(ValueObject parentVO) {
          chargesDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanies.getValue());
          chargesDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanies.getValue());
        }

        public void forceValidate() {}

      });


      // debit account code lookup...
      controlDebitsCode.setLookupController(debitController);
      controlDebitsCode.setControllerMethodName("getAccounts");
      debitController.setLookupDataLocator(debitDataLocator);
      debitDataLocator.setGridMethodName("loadAccounts");
      debitDataLocator.setValidationMethodName("validateAccountCode");
      debitController.setFrameTitle("accounts");
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
      debitController.setPreferredWidthColumn("accountCodeACC02",100);
      debitController.setPreferredWidthColumn("descriptionSYS10",290);
      debitController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) { }

        public void beforeLookupAction(ValueObject parentVO) {
          debitDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanies.getValue());
          debitDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanies.getValue());
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
      costsController.setPreferredWidthColumn("accountCodeACC02",100);
      costsController.setPreferredWidthColumn("descriptionSYS10",290);
      costsController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) { }

        public void beforeLookupAction(ValueObject parentVO) {
          costsDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanies.getValue());
          costsDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanies.getValue());
        }

        public void forceValidate() {}

      });


      // case account code lookup...
      controlCaseCode.setLookupController(caseController);
      controlCaseCode.setControllerMethodName("getAccounts");
      caseController.setLookupDataLocator(caseDataLocator);
      caseDataLocator.setGridMethodName("loadAccounts");
      caseDataLocator.setValidationMethodName("validateAccountCode");
      caseController.setFrameTitle("accounts");
      caseController.setLookupValueObjectClassName("org.jallinone.accounting.accounts.java.AccountVO");
      caseController.addLookup2ParentLink("accountCodeACC02", "caseAccountCodeAcc02DOC19");
      caseController.addLookup2ParentLink("descriptionSYS10","caseAccountDescrDOC19");
      caseController.setFramePreferedSize(new Dimension(400,400));
      caseController.setAllColumnVisible(false);
      caseController.setVisibleColumn("accountCodeACC02", true);
      caseController.setVisibleColumn("descriptionSYS10", true);
      caseController.setFilterableColumn("accountCodeACC02", true);
      caseController.setFilterableColumn("descriptionSYS10", true);
      caseController.setSortedColumn("accountCodeACC02", "ASC", 1);
      caseController.setSortableColumn("accountCodeACC02", true);
      caseController.setSortableColumn("descriptionSYS10", true);
      caseController.setPreferredWidthColumn("accountCodeACC02",100);
      caseController.setPreferredWidthColumn("descriptionSYS10",290);
      caseController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) { }

        public void beforeLookupAction(ValueObject parentVO) {
          caseDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanies.getValue());
          caseDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanies.getValue());
        }

        public void forceValidate() {}

      });

      // bank account code lookup...
      controlBankCode.setLookupController(bankController);
      controlBankCode.setControllerMethodName("getAccounts");
      bankController.setLookupDataLocator(bankDataLocator);
      bankDataLocator.setGridMethodName("loadAccounts");
      bankDataLocator.setValidationMethodName("validateAccountCode");
      bankController.setFrameTitle("accounts");
      bankController.setLookupValueObjectClassName("org.jallinone.accounting.accounts.java.AccountVO");
      bankController.addLookup2ParentLink("accountCodeACC02", "bankAccountCodeAcc02DOC19");
      bankController.addLookup2ParentLink("descriptionSYS10","bankAccountDescrDOC19");
      bankController.setFramePreferedSize(new Dimension(400,400));
      bankController.setAllColumnVisible(false);
      bankController.setVisibleColumn("accountCodeACC02", true);
      bankController.setVisibleColumn("descriptionSYS10", true);
      bankController.setFilterableColumn("accountCodeACC02", true);
      bankController.setFilterableColumn("descriptionSYS10", true);
      bankController.setSortedColumn("accountCodeACC02", "ASC", 1);
      bankController.setSortableColumn("accountCodeACC02", true);
      bankController.setSortableColumn("descriptionSYS10", true);
      bankController.setPreferredWidthColumn("accountCodeACC02",100);
      bankController.setPreferredWidthColumn("descriptionSYS10",290);
      bankController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) { }

        public void beforeLookupAction(ValueObject parentVO) {
          bankDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanies.getValue());
          bankDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanies.getValue());
        }

        public void forceValidate() {}

      });

      // vatEnd account code lookup...
      controlVatEndCode.setLookupController(vatEndController);
      controlVatEndCode.setControllerMethodName("getAccounts");
      vatEndController.setLookupDataLocator(vatEndDataLocator);
      vatEndDataLocator.setGridMethodName("loadAccounts");
      vatEndDataLocator.setValidationMethodName("validateAccountCode");
      vatEndController.setFrameTitle("accounts");
      vatEndController.setLookupValueObjectClassName("org.jallinone.accounting.accounts.java.AccountVO");
      vatEndController.addLookup2ParentLink("accountCodeACC02", "vatEndAccountCodeAcc02DOC19");
      vatEndController.addLookup2ParentLink("descriptionSYS10","vatEndAccountDescrDOC19");
      vatEndController.setFramePreferedSize(new Dimension(400,400));
      vatEndController.setAllColumnVisible(false);
      vatEndController.setVisibleColumn("accountCodeACC02", true);
      vatEndController.setVisibleColumn("descriptionSYS10", true);
      vatEndController.setFilterableColumn("accountCodeACC02", true);
      vatEndController.setFilterableColumn("descriptionSYS10", true);
      vatEndController.setSortedColumn("accountCodeACC02", "ASC", 1);
      vatEndController.setSortableColumn("accountCodeACC02", true);
      vatEndController.setSortableColumn("descriptionSYS10", true);
      vatEndController.setPreferredWidthColumn("accountCodeACC02",100);
      vatEndController.setPreferredWidthColumn("descriptionSYS10",290);
      vatEndController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) { }

        public void beforeLookupAction(ValueObject parentVO) {
          vatEndDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanies.getValue());
          vatEndDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanies.getValue());
        }

        public void forceValidate() {}

      });



      init();

      setSize(650,630);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * Retrieve comapnies list and fill in the companies combo box.
   */
  private void init() {
    Response res = ClientUtils.getData("loadCompanies",new GridParams());
    final Domain d = new Domain("COMPANIES");
    if (!res.isError()) {
      CompanyVO vo = null;
      java.util.List list = ((VOListResponse)res).getRows();
      for(int i=0;i<list.size();i++) {
        vo = (CompanyVO)list.get(i);
        d.addDomainPair(vo.getCompanyCodeSYS01(),vo.getName_1REG04());
      }
    }
    controlCompanies.setDomain(d);
    controlCompanies.getComboBox().addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange()==e.SELECTED) {
          UserParametersVO vo = (UserParametersVO)retailPanel.getVOModel().getValueObject();

          int selIndex = ((JComboBox)e.getSource()).getSelectedIndex();
          String companyCode = (String)d.getDomainPairList()[selIndex].getCode();
          retailPanel.executeReload();
        }
      }
    });

//    controlCompanies.getComboBox().setSelectedIndex(0);
  }



  private void jbInit() throws Exception {
    titledBorder1 = new TitledBorder("");
    titledBorder2 = new TitledBorder("");
    titledBorder3 = new TitledBorder("");
    titledBorder4 = new TitledBorder("");
    retailPanel.setBorder(titledBorder2);
    retailPanel.setVOClassName("org.jallinone.system.java.UserParametersVO");
    retailPanel.setFunctionId("SYS19");

    labelCustomer.setText("retail sale customer");
    this.setTitle(ClientSettings.getInstance().getResources().getResource("user parameters"));
    this.getContentPane().setLayout(gridBagLayout3);
    topPanel.setLayout(gridBagLayout2);
    controlCustomer.setAttributeName("customerCodeSAL07");
    controlName1.setAttributeName("name_1REG04");
    controlName2.setAttributeName("name_2REG04");
    buttonPath.setMaximumSize(new Dimension(22, 20));
    buttonPath.setMinimumSize(new Dimension(22, 20));
    buttonPath.setPreferredSize(new Dimension(22, 20));
    buttonPath.setText("...");
    buttonPath.addActionListener(new UserParametersFrame_buttonPath_actionAdapter(this));
    labelReceiptMan.setText("receipt manager");
    topPanel.setBorder(titledBorder1);
    accountPanel.setBorder(titledBorder3);
    accountPanel.setLayout(gridBagLayout4);
    titledBorder1.setTitle(ClientSettings.getInstance().getResources().getResource("company filter"));
    titledBorder1.setTitleColor(Color.blue);
    titledBorder2.setTitle(ClientSettings.getInstance().getResources().getResource("retail selling"));
    titledBorder2.setTitleColor(Color.blue);
    titledBorder3.setTitle(ClientSettings.getInstance().getResources().getResource("accounting"));
    titledBorder3.setTitleColor(Color.blue);
    labelCreditAccount.setText("credits account");
    labelItemAccount.setText("selling items account");
    labelActivities.setText("selling activities account");
    labelCharges.setText("selling charges account");
    labelDebit.setText("debits account");
    labelPurchase.setText("purchase costs account");
    labelCase.setText("case account");
    labelBank.setText("bank account");
    labelVatEnd.setText("vat endorse account");
    controlCreditsCode.setLinkLabel(labelCreditAccount);
    controlCreditsCode.setMaxCharacters(20);
    controlCreditsCode.setRequired(true);
    controlCreditsDescr.setRequired(false);
    controlCreditsDescr.setEnabledOnInsert(false);
    controlCreditsDescr.setEnabledOnEdit(false);
    controlItemsCode.setLinkLabel(labelItemAccount);
    controlItemsCode.setMaxCharacters(20);
    controlItemsCode.setRequired(true);
    controlItemsDescr.setEnabledOnInsert(false);
    controlItemsDescr.setEnabledOnEdit(false);
    controlActCode.setLinkLabel(labelActivities);
    controlActCode.setMaxCharacters(20);
    controlActCode.setRequired(true);
    controlActDescr.setEnabledOnInsert(false);
    controlActDescr.setEnabledOnEdit(false);
    controlChargesCode.setLinkLabel(labelCharges);
    controlChargesCode.setMaxCharacters(20);
    controlChargesCode.setRequired(true);
    controlChargesDescr.setEnabledOnInsert(false);
    controlChargesDescr.setEnabledOnEdit(false);
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

    controlCaseCode.setLinkLabel(labelCase);
    controlCaseCode.setMaxCharacters(20);
    controlBankCode.setLinkLabel(labelBank);
    controlBankCode.setMaxCharacters(20);
    controlVatEndCode.setLinkLabel(labelVatEnd);
    controlVatEndCode.setMaxCharacters(20);
    controlCostsCode.setRequired(true);
    controlCaseDescr.setEnabledOnInsert(false);
    controlCaseDescr.setEnabledOnEdit(false);
    controlBankDescr.setEnabledOnInsert(false);
    controlBankDescr.setEnabledOnEdit(false);
    controlVatEndDescr.setEnabledOnInsert(false);
    controlVatEndDescr.setEnabledOnEdit(false);

    labelWarehouse.setText("retail warehouse");
    controlWarehouse.setLinkLabel(labelWarehouse);
    controlWarehouse.setMaxCharacters(20);
    controlWareDescr.setEnabledOnInsert(false);
    controlWareDescr.setEnabledOnEdit(false);
    controlWareDescr.setAttributeName("warehouseDescriptionSYS10");
    controlWarehouse.setAttributeName("warehouseCodeWAR01");
    topPanel.add(buttonsPanel,      new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

    controlCaseCode.setRequired(true);
    controlBankCode.setRequired(true);
    controlVatEndCode.setRequired(true);

    retailPanel.setEditButton(editButton1);
    retailPanel.setReloadButton(reloadButton1);
    retailPanel.setSaveButton(saveButton1);
    labelCompanies.setText("company");
    this.getContentPane().add(topPanel,    new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    buttonsPanel.add(editButton1,null);
    topPanel.add(labelCompanies,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    topPanel.add(controlCompanies,   new GridBagConstraints(1, 0, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    buttonsPanel.add(saveButton1,null);
    buttonsPanel.add(reloadButton1,null);
    this.getContentPane().add(retailPanel,   new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    retailPanel.setLayout(gridBagLayout1);

    controlCustomer.setLinkLabel(labelCustomer);
    controlCustomer.setMaxCharacters(20);
    controlName1.setEnabledOnInsert(false);
    controlName1.setEnabledOnEdit(false);
    controlName2.setEnabledOnInsert(false);
    controlName2.setEnabledOnEdit(false);

    controlPath.setAttributeName("receiptPath");

    retailPanel.add(labelCustomer,     new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    retailPanel.add(controlCustomer,      new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    retailPanel.add(controlName1,      new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    retailPanel.add(controlName2,        new GridBagConstraints(4, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    retailPanel.add(controlPath,    new GridBagConstraints(2, 2, 2, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    retailPanel.add(buttonPath,   new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    retailPanel.add(labelReceiptMan,   new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    retailPanel.add(labelWarehouse,  new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.getContentPane().add(accountPanel,    new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    accountPanel.add(labelCreditAccount,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    accountPanel.add(labelItemAccount,   new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    accountPanel.add(labelActivities,   new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    accountPanel.add(labelCharges,   new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    accountPanel.add(labelDebit,   new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    accountPanel.add(labelPurchase,    new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    accountPanel.add(labelCase,    new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    accountPanel.add(labelBank,    new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    accountPanel.add(labelVatEnd,    new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    accountPanel.add(controlCreditsCode,    new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    accountPanel.add(controlCreditsDescr,  new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    accountPanel.add(controlItemsCode,  new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    accountPanel.add(controlItemsDescr,  new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    accountPanel.add(controlActCode,  new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    accountPanel.add(controlActDescr,  new GridBagConstraints(2, 2, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    accountPanel.add(controlChargesCode,  new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    accountPanel.add(controlChargesDescr,  new GridBagConstraints(2, 3, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    accountPanel.add(controlDebitsCode,  new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    accountPanel.add(controlDebitsDescr,  new GridBagConstraints(2, 4, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    accountPanel.add(controlCostsCode,  new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    accountPanel.add(controlCostsDescr,  new GridBagConstraints(2, 5, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    accountPanel.add(controlCaseCode,  new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    accountPanel.add(controlCaseDescr,  new GridBagConstraints(2, 6, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

    accountPanel.add(controlBankCode,  new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    accountPanel.add(controlBankDescr,  new GridBagConstraints(2, 7, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    accountPanel.add(controlVatEndCode,  new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    accountPanel.add(controlVatEndDescr,  new GridBagConstraints(2, 8, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    retailPanel.add(controlWarehouse,  new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    retailPanel.add(controlWareDescr,  new GridBagConstraints(3, 1, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

    retailPanel.addLinkedPanel(accountPanel);

    controlCreditsCode.setAttributeName("creditAccountCodeAcc02SAL07");
    controlCreditsDescr.setAttributeName("creditAccountDescrSAL07");
    controlItemsCode.setAttributeName("itemsAccountCodeAcc02SAL07");
    controlItemsDescr.setAttributeName("itemsAccountDescrSAL07");
    controlActCode.setAttributeName("activitiesAccountCodeAcc02SAL07");
    controlActDescr.setAttributeName("activitiesAccountDescrSAL07");
    controlChargesCode.setAttributeName("chargesAccountCodeAcc02SAL07");
    controlChargesDescr.setAttributeName("chargesAccountDescrSAL07");
    controlDebitsCode.setAttributeName("debitAccountCodeAcc02PUR01");
    controlDebitsDescr.setAttributeName("debitAccountDescrPUR01");
    controlCostsCode.setAttributeName("costsAccountCodeAcc02PUR01");
    controlCostsDescr.setAttributeName("costsAccountDescrPUR01");
    controlCaseCode.setAttributeName("caseAccountCodeAcc02DOC19");
    controlCaseDescr.setAttributeName("caseAccountDescrDOC19");
    controlBankCode.setAttributeName("bankAccountCodeAcc02DOC19");
    controlBankDescr.setAttributeName("bankAccountDescrDOC19");
    controlVatEndCode.setAttributeName("vatEndorseAccountCodeAcc02DOC19");
    controlVatEndDescr.setAttributeName("vatEndorseAccountDescrDOC19");

  }


  public ComboBoxControl getControlCompanies() {
    return controlCompanies;
  }


  public Form getRetailPanel() {
    return retailPanel;
  }


  void buttonPath_actionPerformed(ActionEvent e) {
    JFileChooser f = new JFileChooser(".");
    f.setDialogTitle(ClientSettings.getInstance().getResources().getResource("receipts management program selection"));
    f.setFileSelectionMode(JFileChooser.FILES_ONLY);
    int a = f.showOpenDialog(MDIFrame.getInstance());
    if (a==JFileChooser.APPROVE_OPTION) {
      controlPath.setValue(f.getSelectedFile().getAbsolutePath());
    }
  }


  public JButton getButtonPath() {
    return buttonPath;
  }

}

class UserParametersFrame_buttonPath_actionAdapter implements java.awt.event.ActionListener {
  UserParametersFrame adaptee;

  UserParametersFrame_buttonPath_actionAdapter(UserParametersFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.buttonPath_actionPerformed(e);
  }
}
