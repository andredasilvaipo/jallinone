package org.jallinone.accounting.movements.client;

import org.openswing.swing.mdi.client.InternalFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import org.openswing.swing.client.*;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.util.client.ClientUtils;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JRViewer;
import java.util.HashMap;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.commons.client.ClientApplet;
import org.jallinone.commons.client.ApplicationClientFacade;
import org.jallinone.system.java.ButtonCompanyAuthorizations;
import java.util.ArrayList;
import org.openswing.swing.domains.java.Domain;
import org.openswing.swing.message.receive.java.VOResponse;
import org.openswing.swing.message.receive.java.Response;
import java.util.Calendar;
import java.util.Date;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.openswing.swing.lookup.client.LookupListener;
import org.openswing.swing.message.receive.java.*;
import java.util.Collection;
import org.openswing.swing.form.client.Form;
import org.openswing.swing.table.columns.client.*;
import org.jallinone.accounting.accountingmotives.java.AccountingMotiveVO;
import org.jallinone.accounting.movements.java.JournalRowVO;
import org.jallinone.accounting.accounts.java.AccountVO;
import org.jallinone.sales.customers.java.GridCustomerVO;
import org.jallinone.purchases.suppliers.java.GridSupplierVO;
import org.openswing.swing.table.java.GridDataLocator;
import java.util.Map;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.jallinone.registers.currency.java.CurrencyVO;
import org.jallinone.accounting.movements.java.JournalHeaderWithVatVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Frame used to insert a new accounting item, with vat usage.</p>
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
public class AccountingItemVatFrame extends InternalFrame {

  Form headerPanel = new Form();
  JPanel mainPanel = new JPanel();
  JPanel buttonsPanel = new JPanel();
  JPanel gridPanel = new JPanel();
  Form detailPanel = new Form();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  LabelControl labelItemNr = new LabelControl();
  LabelControl labelMotive = new LabelControl();
  LabelControl labelDate = new LabelControl();
  DateControl controlDate = new DateControl();
  CodLookupControl controlMotive = new CodLookupControl();
  TextControl controlMotiveDescr = new TextControl();
  LabelControl labelDescr = new LabelControl();
  TextControl controlDescr = new TextControl();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  GridBagLayout gridBagLayout5 = new GridBagLayout();
  GridControl grid = new GridControl();
  JPanel gridDetailPanel = new JPanel();
  LabelControl labelCS = new LabelControl();
  ComboBoxControl controlCS = new ComboBoxControl();
  LabelControl labelCode = new LabelControl();
  CodLookupControl controlScode = new CodLookupControl();
  CodLookupControl controlAcode = new CodLookupControl();
  TextControl controlCSDescr = new TextControl();
  LabelControl labelTaxableIncome = new LabelControl();
  CurrencyControl controlTaxableIncome = new CurrencyControl();
  LabelControl labelVatCode = new LabelControl();
  LabelControl labelMovDescr = new LabelControl();
  TextControl controlMovDescr = new TextControl();
  CodLookupControl controlCcode = new CodLookupControl();
  JPanel toolbarPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  InsertButton insertButton1 = new InsertButton();
  EditButton editButton1 = new EditButton();
  ReloadButton reloadButton1 = new ReloadButton();
  DeleteButton deleteButton1 = new DeleteButton();
  SaveButton saveButton1 = new SaveButton();
  TextColumn colCode = new TextColumn();
  CurrencyColumn colTaxableIncome = new CurrencyColumn();
  TextColumn colMovDescr = new TextColumn();
  SaveButton saveButton2 = new SaveButton();
  ComboBoxControl controlCompaniesCombo = new ComboBoxControl();
  LookupController motiveController = new LookupController();
  LookupServerDataLocator motiveDataLocator = new LookupServerDataLocator();
  LookupController accountController = new LookupController();
  LookupServerDataLocator accountDataLocator = new LookupServerDataLocator();
  LookupController customerController = new LookupController();
  LookupServerDataLocator customerDataLocator = new LookupServerDataLocator();
  LookupController supplierController = new LookupController();
  LookupController vatController = new LookupController();
  LookupServerDataLocator vatDataLocator = new LookupServerDataLocator();
  LookupController paymentController = new LookupController();
  LookupServerDataLocator paymentDataLocator = new LookupServerDataLocator();
  private int currencyDecimals = 0;

  ArrayList vos = new ArrayList();
  LookupServerDataLocator supplierDataLocator = new LookupServerDataLocator();
  GridDataLocator gridDataLocator = new GridDataLocator() {

    public Response loadData(
        int action,
        int startIndex,
        Map filteredColumns,
        ArrayList currentSortedColumns,
        ArrayList currentSortedVersusColumns,
        Class valueObjectType,
        Map otherGridParams) {
      return new VOListResponse(vos,false,vos.size());
    }

  };
  NavigatorBar navigatorBar1 = new NavigatorBar();
  BorderLayout borderLayout1 = new BorderLayout();
  LabelControl labelAccount = new LabelControl();
  TextControl controlAccountDescr = new TextControl();
  CodLookupControl controlVatCode = new CodLookupControl();
  TextControl controlVatDescription = new TextControl();
  LabelControl labelRegCode = new LabelControl();
  CodLookupControl controlRegCode = new CodLookupControl();
  TextControl controlRegDescription = new TextControl();
  TextColumn colAccountDescr = new TextColumn();
  LookupServerDataLocator regDataLocator = new LookupServerDataLocator();
  LookupController regController = new LookupController();
  LabelControl labelPay = new LabelControl();
  CodLookupControl controlPayCode = new CodLookupControl();
  TextControl controlPayDescr = new TextControl();
  LabelControl labelDocNr = new LabelControl();
  NumericControl controlDocNr = new NumericControl();


  public AccountingItemVatFrame(AccountingItemVatController controller) {
    try {
      // set companies in combo box...
      ClientApplet applet = ( (ApplicationClientFacade) MDIFrame.getInstance().getClientFacade()).getMainClass();
      ButtonCompanyAuthorizations bca = applet.getAuthorizations().getCompanyBa();
      ArrayList companiesList = bca.getCompaniesList("ACC05_NEW_ITEM_VAT");
      Domain domain = new Domain("DOMAIN_ACC05_NEW_ITEM_VAT");
      for (int i = 0; i < companiesList.size(); i++) {
        if (applet.getAuthorizations().getCompanyBa().isInsertEnabled(
            "ACC05_NEW_ITEM_VAT",companiesList.get(i).toString()
        ))
          domain.addDomainPair(companiesList.get(i),companiesList.get(i).toString());
      }
      controlCompaniesCombo.setDomain(domain);
      controlCompaniesCombo.getComboBox().setSelectedIndex(0);
      controlCompaniesCombo.addItemListener(new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
          if (e.getStateChange() == e.SELECTED) {
            clearGrid();

            // retrieve currency code related to the current company...
            Response res = ClientUtils.getData("loadCompanyCurrency",controlCompaniesCombo.getValue());
            if (!res.isError()) {
              final CurrencyVO vo = (CurrencyVO)((VOResponse)res).getVo();
              currencyDecimals = vo.getDecimalsREG03().intValue();
              JournalHeaderWithVatVO jhVO = (JournalHeaderWithVatVO)headerPanel.getVOModel().getValueObject();
              jhVO.setCurrencyCodeREG01(vo.getCurrencyCodeREG03());

              // set currency settings on currency controls...
              controlTaxableIncome.setCurrencySymbol(vo.getCurrencySymbolREG03());
              controlTaxableIncome.setDecimals(vo.getDecimalsREG03().intValue());
              controlTaxableIncome.setDecimalSymbol(vo.getDecimalSymbolREG03().charAt(0));
              controlTaxableIncome.setGroupingSymbol(vo.getThousandSymbolREG03().charAt(0));

              colTaxableIncome.setDynamicSettings(new CurrencyColumnSettings() {

                public String getCurrencySymbol(int row) {
                  return vo.getCurrencySymbolREG03();
                }
                public int getDecimals(int row) {
                  return vo.getDecimalsREG03().intValue();
                }
                public double getMaxValue(int row) {
                  return Double.MAX_VALUE;
                }
                public double getMinValue(int row) {
                  return Double.MIN_VALUE;
                }
                public boolean isGrouping(int row) {
                  return true;
                }

              });

            }
          }
        }
      });



      // set controller on the header form...
      headerPanel.setFormController(controller);


      // motive code lookup...
      controlMotive.setLookupController(motiveController);
      controlMotive.setControllerMethodName("getAccountMotives");
      motiveController.setForm(headerPanel);
      motiveController.setLookupDataLocator(motiveDataLocator);
      motiveDataLocator.setGridMethodName("loadAccountingMotives");
      motiveDataLocator.setValidationMethodName("validateAccountingMotiveCode");
      motiveController.setFrameTitle("accountingMotives");
      motiveController.setLookupValueObjectClassName("org.jallinone.accounting.accountingmotives.java.AccountingMotiveVO");
      motiveController.addLookup2ParentLink("accountingMotiveCodeACC03", "accountingMotiveCodeAcc03ACC05");
      motiveController.addLookup2ParentLink("descriptionSYS10","motiveDescrACC05");
      motiveController.setFramePreferedSize(new Dimension(400,400));
      motiveController.setAllColumnVisible(false);
      motiveController.setVisibleColumn("accountingMotiveCodeACC03",true);
      motiveController.setVisibleColumn("descriptionSYS10",true);
      motiveController.setPreferredWidthColumn("descriptionSYS10",290);
      motiveController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
        }

        public void beforeLookupAction(ValueObject parentVO) {
          motiveDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
          motiveDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
        }

        public void forceValidate() {}

      });


      // register code lookup...
      controlRegCode.setLookupController(regController);
      controlRegCode.setControllerMethodName("getVatRegisters");
      regController.setForm(headerPanel);
      regController.setLookupDataLocator(regDataLocator);
      regDataLocator.setGridMethodName("loadVatRegisters");
      regDataLocator.setValidationMethodName("validateVatRegisterCode");
      regController.setFrameTitle("vat registers");
      regController.setLookupValueObjectClassName("org.jallinone.accounting.vatregisters.java.VatRegisterVO");
      regController.addLookup2ParentLink("registerCodeACC04", "registerCodeACC04");
      regController.addLookup2ParentLink("descriptionSYS10","registerDescriptionACC04");
      regController.addLookup2ParentLink("accountCodeAcc02ACC04","accountCodeAcc02ACC04");
      regController.addLookup2ParentLink("accountDescriptionACC04","accountDescriptionACC04");
      regController.setFramePreferedSize(new Dimension(400,400));
      regController.setAllColumnVisible(false);
      regController.setVisibleColumn("registerCodeACC04",true);
      regController.setVisibleColumn("descriptionSYS10",true);
      regController.setPreferredWidthColumn("descriptionSYS10",290);
      regController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
        }

        public void beforeLookupAction(ValueObject parentVO) {
          regDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
          regDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
        }

        public void forceValidate() {}

      });


      // vat code lookup...
      controlVatCode.setLookupController(vatController);
      controlVatCode.setControllerMethodName("getVatsList");
      vatController.setForm(detailPanel);
      vatController.setLookupDataLocator(vatDataLocator);
      vatDataLocator.setGridMethodName("loadVats");
      vatDataLocator.setValidationMethodName("validateVatCode");
      vatController.setFrameTitle("vats");
      vatController.setLookupValueObjectClassName("org.jallinone.registers.vat.java.VatVO");
      vatController.addLookup2ParentLink("vatCodeREG01", "vatCodeREG01");
      vatController.addLookup2ParentLink("descriptionSYS10","vatDescriptionREG01");
      vatController.addLookup2ParentLink("valueREG01","valueREG01");
      vatController.addLookup2ParentLink("deductibleREG01","deductibleREG01");
      vatController.setFramePreferedSize(new Dimension(400,400));
      vatController.setAllColumnVisible(false);
      vatController.setVisibleColumn("vatCodeREG01",true);
      vatController.setVisibleColumn("descriptionSYS10",true);
      vatController.setPreferredWidthColumn("descriptionSYS10",290);
      vatController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {}

        public void beforeLookupAction(ValueObject parentVO) {}

        public void forceValidate() {}

      });


      // payment code lookup...
      controlPayCode.setLookupController(paymentController);
      controlPayCode.setControllerMethodName("getPaymentsList");
      paymentController.setForm(headerPanel);
      paymentController.setLookupDataLocator(paymentDataLocator);
      paymentDataLocator.setGridMethodName("loadPayments");
      paymentDataLocator.setValidationMethodName("validatePaymentCode");
      paymentController.setFrameTitle("payments");
      paymentController.setLookupValueObjectClassName("org.jallinone.registers.payments.java.PaymentVO");
      paymentController.addLookup2ParentLink("paymentCodeREG10", "paymentCodeREG10");
      paymentController.addLookup2ParentLink("descriptionSYS10","paymentDescriptionREG10");
      paymentController.setFramePreferedSize(new Dimension(400,400));
      paymentController.setAllColumnVisible(false);
      paymentController.setVisibleColumn("paymentCodeREG10",true);
      paymentController.setVisibleColumn("descriptionSYS10",true);
      paymentController.setPreferredWidthColumn("descriptionSYS10",290);
      paymentController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {}

        public void beforeLookupAction(ValueObject parentVO) {
					paymentDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
					paymentDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
				}

        public void forceValidate() {}

      });


      // account code lookup...
      controlAcode.setLookupController(accountController);
      controlAcode.setControllerMethodName("getAccounts");
      accountController.setForm(detailPanel);
      accountController.setLookupDataLocator(accountDataLocator);
      accountDataLocator.setGridMethodName("loadAccounts");
      accountDataLocator.setValidationMethodName("validateAccountCode");
      accountController.setFrameTitle("accounts");
      accountController.setLookupValueObjectClassName("org.jallinone.accounting.accounts.java.AccountVO");
      accountController.addLookup2ParentLink("accountCodeACC02", "accountCodeACC06");
      accountController.addLookup2ParentLink("descriptionSYS10","accountDescriptionACC06");
      accountController.setFramePreferedSize(new Dimension(400,400));
      accountController.setAllColumnVisible(false);
      accountController.setVisibleColumn("accountCodeACC02",true);
      accountController.setVisibleColumn("descriptionSYS10",true);
      accountController.setPreferredWidthColumn("descriptionSYS10",290);
      accountController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          AccountVO accountVO = (AccountVO)accountController.getLookupVO();
          JournalRowVO vo = (JournalRowVO)detailPanel.getVOModel().getValueObject();
          vo.setAccountCodeAcc02ACC06(accountVO.getAccountCodeACC02());
          vo.setAccountCodeACC06(accountVO.getAccountCodeACC02());
//          controlAcode.setValue(accountVO.getAccountCodeACC02());
        }

        public void beforeLookupAction(ValueObject parentVO) {
          accountDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
          accountDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
        }

        public void forceValidate() {}

      });


      // customer lookup...
      customerDataLocator.setGridMethodName("loadCustomers");
      customerDataLocator.setValidationMethodName("validateCustomerCode");
      controlCcode.setLookupController(customerController);
      controlCcode.setControllerMethodName("getCustomersList");
      customerController.setForm(headerPanel);
      customerController.setLookupDataLocator(customerDataLocator);
      customerController.setFrameTitle("customers");
      customerController.setLookupValueObjectClassName("org.jallinone.sales.customers.java.GridCustomerVO");
      customerController.addLookup2ParentLink("customerCodeSAL07","customerCodeSAL07");
      customerController.addLookup2ParentLink("name_1REG04", "name_1REG04");
      customerController.addLookup2ParentLink("progressiveREG04", "progressiveREG04");
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
      customerDataLocator.getLookupFrameParams().put(ApplicationConsts.FILTER_COMPANY_FOR_INSERT,"ACC05_NEW_ITEM_VAT");
      customerDataLocator.getLookupValidationParameters().put(ApplicationConsts.FILTER_COMPANY_FOR_INSERT,"ACC05_NEW_ITEM_VAT");
      customerController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          GridCustomerVO custVO = (GridCustomerVO)customerController.getLookupVO();
          JournalHeaderWithVatVO vo = (JournalHeaderWithVatVO)headerPanel.getVOModel().getValueObject();
          vo.setCreditAccountCodeAcc02SAL07(custVO.getCreditAccountCodeAcc02SAL07());
        }

        public void beforeLookupAction(ValueObject parentVO) {
          customerDataLocator.getLookupFrameParams().put(ApplicationConsts.SUBJECT_TYPE,ApplicationConsts.SUBJECT_ORGANIZATION_CUSTOMER);
          customerDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
          customerDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
          customerController.setHeaderColumnName("name_1REG04", "corporateName1");
          customerController.setHeaderColumnName("name_2REG04", "corporateName2");
        }

        public void forceValidate() {}

      });



      // supplier lookup...
      supplierDataLocator.setGridMethodName("loadSuppliers");
      supplierDataLocator.setValidationMethodName("validateSupplierCode");
      controlScode.setLookupController(supplierController);
      controlScode.setControllerMethodName("getSuppliersList");
      supplierController.setForm(headerPanel);
      supplierController.setLookupDataLocator(supplierDataLocator);
      supplierController.setFrameTitle("suppliers");
      supplierController.setLookupValueObjectClassName("org.jallinone.purchases.suppliers.java.GridSupplierVO");
      supplierController.addLookup2ParentLink("supplierCodePUR01","supplierCodePUR01");
      supplierController.addLookup2ParentLink("name_1REG04", "name_1REG04");
      supplierController.addLookup2ParentLink("progressiveREG04", "progressiveREG04");
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
      supplierDataLocator.getLookupFrameParams().put(ApplicationConsts.FILTER_COMPANY_FOR_INSERT,"ACC05_NEW_ITEM_VAT");
      supplierDataLocator.getLookupValidationParameters().put(ApplicationConsts.FILTER_COMPANY_FOR_INSERT,"ACC05_NEW_ITEM_VAT");
      supplierController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          GridSupplierVO supplierVO = (GridSupplierVO)supplierController.getLookupVO();
          JournalHeaderWithVatVO vo = (JournalHeaderWithVatVO)headerPanel.getVOModel().getValueObject();
          vo.setDebitAccountCodeAcc02PUR01(supplierVO.getDebitAccountCodeAcc02PUR01());
        }

        public void beforeLookupAction(ValueObject parentVO) {
          supplierDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
          supplierDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
        }

        public void forceValidate() {}

      });


      // add item listener to enable customer/supplier code...
      controlCS.addItemListener(new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
          if (e.getStateChange()==e.SELECTED) {
            updateCodes();
          }
          else {
            controlCcode.setEnabled(false);
            controlScode.setEnabled(false);
          }
        }
      });


      jbInit();

      setSize(750,500);
      setMinimumSize(new Dimension(750,500));
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  public void updateCodes() {
    JournalHeaderWithVatVO vo = (JournalHeaderWithVatVO)headerPanel.getVOModel().getValueObject();
    vo.setCompanyCodeSys01ACC05((String)controlCompaniesCombo.getValue());
    vo.setAccountCodeTypeACC06((String)controlCS.getValue());

    if (vo.getAccountCodeTypeACC06().equals(ApplicationConsts.ACCOUNT_TYPE_CUSTOMER)) {
      controlCcode.setEnabled(true && headerPanel.getMode()!=Consts.READONLY);
      controlScode.setEnabled(false);
      controlScode.setValue("");
      vo.setDocTypeDOC19(ApplicationConsts.SALE_GENERIC_INVOICE);
//      if (detailPanel.getMode()==Consts.READONLY)
//        controlCcode.setValue(vo.getAccountCodeACC06());
    }
    else if (vo.getAccountCodeTypeACC06().equals(ApplicationConsts.ACCOUNT_TYPE_SUPPLIER)) {
      controlCcode.setEnabled(false);
      controlScode.setEnabled(true && headerPanel.getMode()!=Consts.READONLY);
      controlCcode.setValue("");
      vo.setDocTypeDOC19(ApplicationConsts.PURCHASE_GENERIC_INVOICE);
//      if (detailPanel.getMode()==Consts.READONLY)
//        controlScode.setValue(vo.getAccountCodeACC06());
    }
  }


  private void jbInit() throws Exception {
    headerPanel.setVOClassName("org.jallinone.accounting.movements.java.JournalHeaderWithVatVO");
    headerPanel.setFunctionId("ACC05_NEW_ITEM_VAT");

    grid.setDeleteButton(deleteButton1);
    grid.setValueObjectClassName("org.jallinone.accounting.movements.java.JournalRowWithVatVO");
    grid.setController(new AccountingItemVatGridController(this,vos));
    grid.setFunctionId("ACC05_NEW_ITEM_VAT");
    grid.setGridDataLocator(gridDataLocator);
    grid.setNavBar(navigatorBar1);

    detailPanel.setVOClassName("org.jallinone.accounting.movements.java.JournalRowWithVatVO");
    detailPanel.setFormController(new AccountingItemVatGridRowController(this,vos));
    detailPanel.setInsertButton(insertButton1);
    detailPanel.setEditButton(editButton1);
    detailPanel.setSaveButton(saveButton1);
    detailPanel.setReloadButton(reloadButton1);
    detailPanel.setFunctionId("ACC05_NEW_ITEM_VAT");
    detailPanel.setLayout(gridBagLayout5);

    controlDescr.setAttributeName("descriptionACC05");
    controlDate.setAttributeName("itemDateACC05");
    controlMotive.setAttributeName("accountingMotiveCodeAcc03ACC05");
    controlMotiveDescr.setAttributeName("motiveDescrACC05");
    controlMotiveDescr.setEnabled(false);

    controlCS.setDomainId("ACCOUNT_CODE_TYPE_CS_ACC05");
    controlCS.setLinkLabel(labelCS);
    controlCS.setRequired(true);

    this.setTitle(ClientSettings.getInstance().getResources().getResource("new item with vat"));
    buttonsPanel.setBorder(BorderFactory.createEtchedBorder());
    headerPanel.setBorder(BorderFactory.createEtchedBorder());
    headerPanel.setSaveButton(saveButton2);
    headerPanel.setLayout(gridBagLayout1);
    mainPanel.setLayout(gridBagLayout2);
    labelItemNr.setText("companyCodeSYS01");
    labelMotive.setText("accountingMotive");
    labelDate.setText("registrationDate");
    labelDescr.setText("description");
    controlMotive.setLinkLabel(labelMotive);
    controlMotive.setMaxCharacters(20);
    controlMotive.setRequired(true);
    controlDate.setLinkLabel(labelDate);
    controlDate.setRequired(true);
    controlMotiveDescr.setEnabledOnInsert(false);
    controlMotiveDescr.setEnabledOnEdit(false);
    controlDescr.setLinkLabel(labelDescr);
    controlDescr.setRequired(true);
    gridDetailPanel.setBorder(BorderFactory.createEtchedBorder());
    gridDetailPanel.setLayout(borderLayout1);
    mainPanel.setBorder(null);
    labelCS.setText("cust./suppl.");
    labelCode.setText("code");
    labelTaxableIncome.setText("taxableIncome");
    labelVatCode.setToolTipText("");
    labelVatCode.setText("vatCodeREG01");
    labelMovDescr.setText("description");
    controlCcode.setAttributeName("customerCodeSAL07");
    controlCcode.setEnabled(false);
    controlCcode.setEnabledOnEdit(false);
    controlCcode.setEnabledOnInsert(false);
    controlCcode.setMaxCharacters(20);
    controlScode.setAttributeName("supplierCodePUR01");
    controlScode.setEnabled(false);
    controlScode.setEnabledOnEdit(false);
    controlScode.setEnabledOnInsert(false);
    controlScode.setMaxCharacters(20);
//    controlAcode.setEnabled(false);
    controlAcode.setAttributeName("accountCodeACC06");
//    controlAcode.setEnabledOnEdit(false);
//    controlAcode.setEnabledOnInsert(false);
    controlAcode.setLinkLabel(labelAccount);
    controlAcode.setMaxCharacters(20);
    controlAcode.setRequired(true);
    controlCSDescr.setEnabledOnInsert(false);
    controlCSDescr.setEnabledOnEdit(false);
    controlCSDescr.setEnabled(false);
    grid.setVisibleStatusPanel(false);
    toolbarPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    colMovDescr.setRpadding(false);
    colMovDescr.setColumnName("descriptionACC06");
    colMovDescr.setPreferredWidth(320);
    colCode.setColumnName("accountCodeACC06");
    colCode.setPreferredWidth(80);
    controlTaxableIncome.setAttributeName("taxableIncome");
    controlTaxableIncome.setColumns(10);
    controlTaxableIncome.setLinkLabel(labelTaxableIncome);
    controlTaxableIncome.setRequired(true);
    colTaxableIncome.setColumnName("taxableIncome");
    controlCS.setAttributeName("accountCodeTypeACC06");
    controlCSDescr.setAttributeName("name_1REG04");
    controlCompaniesCombo.setAttributeName("companyCodeSys01ACC05");
    saveButton2.setText("saveButton2");
    labelAccount.setText("accountCodeACC02");
    controlAccountDescr.setAttributeName("accountDescriptionACC06");
    controlAccountDescr.setEnabled(false);
    controlAccountDescr.setEnabledOnInsert(false);
    controlAccountDescr.setEnabledOnEdit(false);
    controlVatCode.setAttributeName("vatCodeREG01");
    controlVatCode.setLinkLabel(labelVatCode);
    controlVatCode.setMaxCharacters(20);
    controlVatCode.setRequired(true);
    labelRegCode.setText("registerCodeACC04");
    controlRegCode.setAttributeName("registerCodeACC04");
    controlRegCode.setLinkLabel(labelRegCode);
    controlRegCode.setMaxCharacters(20);
    controlRegCode.setRequired(true);
    controlRegDescription.setAttributeName("registerDescriptionACC04");
    controlRegDescription.setEnabled(false);
    controlRegDescription.setEnabledOnInsert(false);
    controlRegDescription.setEnabledOnEdit(false);
    colAccountDescr.setColumnName("accountDescriptionACC06");
    colAccountDescr.setPreferredWidth(210);
    controlVatDescription.setAttributeName("vatDescriptionREG01");
    controlVatDescription.setEnabled(false);
    controlVatDescription.setEnabledOnInsert(false);
    controlVatDescription.setEnabledOnEdit(false);
    labelPay.setText("payment");
    controlPayCode.setLinkLabel(labelPay);
    controlPayCode.setMaxCharacters(20);
    controlPayCode.setRequired(true);
    controlPayCode.setAttributeName("paymentCodeREG10");
    controlPayDescr.setEnabled(false);
    controlPayDescr.setEnabledOnInsert(false);
    controlPayDescr.setEnabledOnEdit(false);
    controlPayDescr.setAttributeName("paymentDescriptionREG10");
    labelDocNr.setText("docNr");
    controlDocNr.setLinkLabel(labelDocNr);
    controlDocNr.setRequired(true);
    controlDocNr.setAttributeName("docSequenceDOC19");
    this.getContentPane().add(headerPanel, BorderLayout.NORTH);
    this.getContentPane().add(mainPanel, BorderLayout.CENTER);
    this.getContentPane().add(buttonsPanel,  BorderLayout.SOUTH);
    buttonsPanel.add(saveButton2, null);
    headerPanel.add(labelItemNr,           new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    headerPanel.add(labelMotive,           new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    headerPanel.add(labelDate,           new GridBagConstraints(2, 0, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    headerPanel.add(controlMotive,           new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    headerPanel.add(controlMotiveDescr,             new GridBagConstraints(2, 1, 5, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    headerPanel.add(controlDescr,            new GridBagConstraints(3, 2, 4, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    headerPanel.add(controlCompaniesCombo,         new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    headerPanel.add(labelCS,       new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
    headerPanel.add(controlCS,        new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 50, 0));
    headerPanel.add(labelCode,        new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    headerPanel.add(controlCcode,      new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 40, 0));
    headerPanel.add(controlScode,       new GridBagConstraints(4, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 40, 0));
    headerPanel.add(controlCSDescr,      new GridBagConstraints(5, 3, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    headerPanel.add(controlDate,    new GridBagConstraints(4, 0, 2, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 50, 0));
    mainPanel.add(gridDetailPanel,       new GridBagConstraints(0, 0, 6, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 1, 0));
    gridDetailPanel.add(grid, BorderLayout.CENTER);
    gridDetailPanel.add(detailPanel, BorderLayout.SOUTH);
    detailPanel.add(labelTaxableIncome,               new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    detailPanel.add(controlTaxableIncome,                 new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 40, 0));
    detailPanel.add(labelVatCode,               new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 15, 5, 5), 0, 0));
    detailPanel.add(labelMovDescr,              new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    detailPanel.add(controlMovDescr,                    new GridBagConstraints(1, 2, 5, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    detailPanel.add(controlAcode,      new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    detailPanel.add(labelAccount,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    gridDetailPanel.add(toolbarPanel, BorderLayout.NORTH);
    controlMovDescr.setAttributeName("descriptionACC06");
    toolbarPanel.add(insertButton1, null);
    toolbarPanel.add(editButton1, null);
    toolbarPanel.add(saveButton1, null);
    toolbarPanel.add(reloadButton1, null);
    toolbarPanel.add(deleteButton1, null);
    toolbarPanel.add(navigatorBar1, null);
    grid.getColumnContainer().add(colCode, null);
    grid.getColumnContainer().add(colAccountDescr, null);
    grid.getColumnContainer().add(colTaxableIncome, null);
    grid.getColumnContainer().add(colMovDescr, null);
    detailPanel.add(controlAccountDescr,   new GridBagConstraints(2, 0, 3, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    detailPanel.add(controlVatCode,     new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    detailPanel.add(controlVatDescription,  new GridBagConstraints(4, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));

    headerPanel.add(labelRegCode,      new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
    headerPanel.add(controlRegCode,         new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    headerPanel.add(controlRegDescription,        new GridBagConstraints(2, 5, 5, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    headerPanel.add(labelPay,   new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    headerPanel.add(controlPayCode,   new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    headerPanel.add(controlPayDescr,  new GridBagConstraints(2, 4, 5, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    headerPanel.add(labelDescr, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
    headerPanel.add(labelDocNr,   new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    headerPanel.add(controlDocNr,   new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
  }


  private void clearGrid() {
    detailPanel.setMode(Consts.READONLY);
    controlCS.getComboBox().setSelectedIndex(-1);
    insertButton1.setEnabled(true);
  }


  public Form getHeaderPanel() {
    return headerPanel;
  }


  public Form getDetailPanel() {
    return detailPanel;
  }
  public InsertButton getInsertButton1() {
    return insertButton1;
  }
  public GridControl getGrid() {
    return grid;
  }
  public ComboBoxControl getcontrolCS() {
    return controlCS;
  }
  public ComboBoxControl getControlCS() {
    return controlCS;
  }
  public int getCurrencyDecimals() {
    return currencyDecimals;
  }


}

