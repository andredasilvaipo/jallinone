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


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Frame used to insert a new accounting item, without vat usage.</p>
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
public class AccountingItemNoVatFrame extends InternalFrame {
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
  LabelControl labelTotDebit = new LabelControl();
  CurrencyControl controlTotDebit = new CurrencyControl();
  LabelControl labelTotCredit = new LabelControl();
  CurrencyControl controlTotCredit = new CurrencyControl();
  LabelControl labelSbil = new LabelControl();
  CurrencyControl controlSbil = new CurrencyControl();
  JPanel totPanel = new JPanel();
  JPanel gridDetailPanel = new JPanel();
  GridBagLayout gridBagLayout4 = new GridBagLayout();
  LabelControl labelCSA = new LabelControl();
  ComboBoxControl controlCSA = new ComboBoxControl();
  LabelControl labelCode = new LabelControl();
  CodLookupControl controlScode = new CodLookupControl();
  CodLookupControl controlAcode = new CodLookupControl();
  TextControl controlCSADescr = new TextControl();
  LabelControl labelDebit = new LabelControl();
  CurrencyControl controlDebit = new CurrencyControl();
  LabelControl labelCredit = new LabelControl();
  CurrencyControl controlCredit = new CurrencyControl();
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
  ComboColumn colCSA = new ComboColumn();
  TextColumn colCode = new TextColumn();
  CurrencyColumn colDebit = new CurrencyColumn();
  CurrencyColumn colCredit = new CurrencyColumn();
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


  public AccountingItemNoVatFrame(AccountingItemNoVatController controller) {
    try {
      // set companies in combo box...
      ClientApplet applet = ( (ApplicationClientFacade) MDIFrame.getInstance().getClientFacade()).getMainClass();
      ButtonCompanyAuthorizations bca = applet.getAuthorizations().getCompanyBa();
      ArrayList companiesList = bca.getCompaniesList("ACC05_NEW_ITEM_NOVAT");
      Domain domain = new Domain("DOMAIN_ACC05_NEW_ITEM_NOVAT");
      for (int i = 0; i < companiesList.size(); i++) {
        if (applet.getAuthorizations().getCompanyBa().isInsertEnabled(
            "ACC05_NEW_ITEM_NOVAT",companiesList.get(i).toString()
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

              // set currency settings on currency controls...
              controlDebit.setCurrencySymbol(vo.getCurrencySymbolREG03());
              controlDebit.setDecimals(vo.getDecimalsREG03().intValue());
              controlDebit.setDecimalSymbol(vo.getDecimalSymbolREG03().charAt(0));
              controlDebit.setGroupingSymbol(vo.getThousandSymbolREG03().charAt(0));

              controlCredit.setCurrencySymbol(vo.getCurrencySymbolREG03());
              controlCredit.setDecimals(vo.getDecimalsREG03().intValue());
              controlCredit.setDecimalSymbol(vo.getDecimalSymbolREG03().charAt(0));
              controlCredit.setGroupingSymbol(vo.getThousandSymbolREG03().charAt(0));

              controlTotDebit.setCurrencySymbol(vo.getCurrencySymbolREG03());
              controlTotDebit.setDecimals(vo.getDecimalsREG03().intValue());
              controlTotDebit.setDecimalSymbol(vo.getDecimalSymbolREG03().charAt(0));
              controlTotDebit.setGroupingSymbol(vo.getThousandSymbolREG03().charAt(0));

              controlTotCredit.setCurrencySymbol(vo.getCurrencySymbolREG03());
              controlTotCredit.setDecimals(vo.getDecimalsREG03().intValue());
              controlTotCredit.setDecimalSymbol(vo.getDecimalSymbolREG03().charAt(0));
              controlTotCredit.setGroupingSymbol(vo.getThousandSymbolREG03().charAt(0));

              controlSbil.setCurrencySymbol(vo.getCurrencySymbolREG03());
              controlSbil.setDecimals(vo.getDecimalsREG03().intValue());
              controlSbil.setDecimalSymbol(vo.getDecimalSymbolREG03().charAt(0));
              controlSbil.setGroupingSymbol(vo.getThousandSymbolREG03().charAt(0));

              colCredit.setDynamicSettings(new CurrencyColumnSettings() {

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

              colDebit.setDynamicSettings(new CurrencyColumnSettings() {

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


      // start motive code lookup...
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
          controlAcode.setValue(accountVO.getAccountCodeACC02());
          if (accountVO.getAccountTypeACC02()!=null) {
            if (accountVO.getAccountTypeACC02().equals(ApplicationConsts.DEBIT_ACCOUNT))
              controlDebit.requestFocus();
            else
              controlCredit.requestFocus();
          }
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
      customerController.setForm(detailPanel);
      customerController.setLookupDataLocator(customerDataLocator);
      customerController.setFrameTitle("customers");
      customerController.setLookupValueObjectClassName("org.jallinone.sales.customers.java.GridCustomerVO");
      customerController.addLookup2ParentLink("customerCodeSAL07","accountCodeACC06");
      customerController.addLookup2ParentLink("name_1REG04", "accountDescriptionACC06");
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
      customerDataLocator.getLookupFrameParams().put(ApplicationConsts.FILTER_COMPANY_FOR_INSERT,"ACC05_NEW_ITEM_NOVAT");
      customerDataLocator.getLookupValidationParameters().put(ApplicationConsts.FILTER_COMPANY_FOR_INSERT,"ACC05_NEW_ITEM_NOVAT");
      customerController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          GridCustomerVO custVO = (GridCustomerVO)customerController.getLookupVO();
          JournalRowVO vo = (JournalRowVO)detailPanel.getVOModel().getValueObject();
          vo.setAccountCodeAcc02ACC06(custVO.getCreditAccountCodeAcc02SAL07());
          vo.setAccountCodeACC06(custVO.getCustomerCodeSAL07());
          controlCcode.setValue(custVO.getCustomerCodeSAL07());
          controlDebit.requestFocus();
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
      supplierController.setForm(detailPanel);
      supplierController.setLookupDataLocator(supplierDataLocator);
      supplierController.setFrameTitle("suppliers");
      supplierController.setLookupValueObjectClassName("org.jallinone.purchases.suppliers.java.GridSupplierVO");
      supplierController.addLookup2ParentLink("supplierCodePUR01","accountCodeACC06");
      supplierController.addLookup2ParentLink("name_1REG04", "accountDescriptionACC06");
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
      supplierDataLocator.getLookupFrameParams().put(ApplicationConsts.FILTER_COMPANY_FOR_INSERT,"ACC05_NEW_ITEM_NOVAT");
      supplierDataLocator.getLookupValidationParameters().put(ApplicationConsts.FILTER_COMPANY_FOR_INSERT,"ACC05_NEW_ITEM_NOVAT");
      supplierController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          GridSupplierVO supplierVO = (GridSupplierVO)supplierController.getLookupVO();
          JournalRowVO vo = (JournalRowVO)detailPanel.getVOModel().getValueObject();
          vo.setAccountCodeAcc02ACC06(supplierVO.getDebitAccountCodeAcc02PUR01());
          vo.setAccountCodeACC06(supplierVO.getSupplierCodePUR01());
          controlScode.setValue(supplierVO.getSupplierCodePUR01());
          controlCredit.requestFocus();
        }

        public void beforeLookupAction(ValueObject parentVO) {
          supplierDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
          supplierDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
        }

        public void forceValidate() {}

      });


      // add item listener to enable customer/supplier/account code...
      controlCSA.addItemListener(new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
          if (e.getStateChange()==e.SELECTED) {
            updateCodes();
          }
          else {
            controlCcode.setEnabled(false);
            controlScode.setEnabled(false);
            controlAcode.setEnabled(false);
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
    JournalRowVO vo = (JournalRowVO)detailPanel.getVOModel().getValueObject();
    vo.setCompanyCodeSys01ACC06((String)controlCompaniesCombo.getValue());
    vo.setAccountCodeTypeACC06((String)controlCSA.getValue());

    if (vo.getAccountCodeTypeACC06().equals(ApplicationConsts.ACCOUNT_TYPE_ACCOUNT)) {
      controlCcode.setEnabled(false);
      controlScode.setEnabled(false);
      controlAcode.setEnabled(true && detailPanel.getMode()!=Consts.READONLY);
      controlCcode.setValue("");
      controlScode.setValue("");
      if (detailPanel.getMode()==Consts.READONLY)
        controlAcode.setValue(vo.getAccountCodeACC06());
    }
    else if (vo.getAccountCodeTypeACC06().equals(ApplicationConsts.ACCOUNT_TYPE_CUSTOMER)) {
      controlCcode.setEnabled(true && detailPanel.getMode()!=Consts.READONLY);
      controlScode.setEnabled(false);
      controlAcode.setEnabled(false);
      controlAcode.setValue("");
      controlScode.setValue("");
      if (detailPanel.getMode()==Consts.READONLY)
        controlCcode.setValue(vo.getAccountCodeACC06());
    }
    else if (vo.getAccountCodeTypeACC06().equals(ApplicationConsts.ACCOUNT_TYPE_SUPPLIER)) {
      controlCcode.setEnabled(false);
      controlScode.setEnabled(true && detailPanel.getMode()!=Consts.READONLY);
      controlAcode.setEnabled(false);
      controlCcode.setValue("");
      controlAcode.setValue("");
      if (detailPanel.getMode()==Consts.READONLY)
        controlScode.setValue(vo.getAccountCodeACC06());
    }
  }


  private void jbInit() throws Exception {
    headerPanel.setVOClassName("org.jallinone.accounting.movements.java.JournalHeaderVO");
    headerPanel.setFunctionId("ACC05_NEW_ITEM_NOVAT");

    grid.setDeleteButton(deleteButton1);
    grid.setValueObjectClassName("org.jallinone.accounting.movements.java.JournalRowVO");
    grid.setController(new AccountingItemNoVatGridController(this,vos));
    grid.setFunctionId("ACC05_NEW_ITEM_NOVAT");
    grid.setGridDataLocator(gridDataLocator);
    grid.setNavBar(navigatorBar1);

    detailPanel.setVOClassName("org.jallinone.accounting.movements.java.JournalRowVO");
    detailPanel.setFormController(new AccountingItemNoVatGridRowController(this,vos));
    detailPanel.setInsertButton(insertButton1);
    detailPanel.setEditButton(editButton1);
    detailPanel.setSaveButton(saveButton1);
    detailPanel.setReloadButton(reloadButton1);
    detailPanel.setFunctionId("ACC05_NEW_ITEM_NOVAT");
    detailPanel.setLayout(gridBagLayout5);

    controlDescr.setAttributeName("descriptionACC05");
    controlDate.setAttributeName("itemDateACC05");
    controlMotive.setAttributeName("accountingMotiveCodeAcc03ACC05");
    controlMotiveDescr.setAttributeName("motiveDescrACC05");

    controlCSA.setDomainId("ACCOUNT_CODE_TYPE_ACC05");
    colCSA.setDomainId("ACCOUNT_CODE_TYPE_ACC05");

    this.setTitle(ClientSettings.getInstance().getResources().getResource("new item without vat"));
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
    labelTotDebit.setText("totalDebit");
    labelTotCredit.setText("totalCredit");
    labelSbil.setText("lack of balance");
    controlTotCredit.setEnabled(false);
    controlTotCredit.setEnabledOnEdit(false);
    controlTotCredit.setEnabledOnInsert(false);
    controlTotDebit.setEnabled(false);
    controlTotDebit.setEnabledOnEdit(false);
    controlTotDebit.setEnabledOnInsert(false);
    controlSbil.setEnabled(false);
    controlSbil.setEnabledOnEdit(false);
    controlSbil.setEnabledOnInsert(false);
    totPanel.setBorder(BorderFactory.createEtchedBorder());
    totPanel.setLayout(gridBagLayout4);
    gridDetailPanel.setBorder(BorderFactory.createEtchedBorder());
    gridDetailPanel.setLayout(borderLayout1);
    mainPanel.setBorder(null);
    labelCSA.setText("cust./suppl./acc.");
    labelCode.setText("code");
    labelDebit.setText("debit");
    labelCredit.setToolTipText("");
    labelCredit.setText("credit");
    labelMovDescr.setText("description");
    controlCcode.setEnabled(false);
    controlCcode.setEnabledOnEdit(false);
    controlCcode.setEnabledOnInsert(false);
    controlCcode.setMaxCharacters(20);
//    controlCcode.setAttributeName("accountCodeACC06");
    controlScode.setEnabled(false);
    controlScode.setEnabledOnEdit(false);
    controlScode.setEnabledOnInsert(false);
    controlScode.setMaxCharacters(20);
//    controlScode.setAttributeName("accountCodeACC06");
    controlAcode.setEnabled(false);
    controlAcode.setEnabledOnEdit(false);
    controlAcode.setEnabledOnInsert(false);
//    controlAcode.setAttributeName("accountCodeACC06");
    controlAcode.setMaxCharacters(20);
    controlCSADescr.setEnabledOnInsert(false);
    controlCSADescr.setEnabledOnEdit(false);
    controlCSADescr.setEnabled(false);
    grid.setVisibleStatusPanel(false);
    toolbarPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    colMovDescr.setRpadding(false);
    colMovDescr.setColumnName("descriptionACC06");
    colMovDescr.setPreferredWidth(320);
    colCode.setColumnName("accountCodeACC06");
    colCode.setPreferredWidth(80);
    controlDebit.setAttributeName("debitAmountACC06");
    controlDebit.setColumns(10);
    controlDebit.addFocusListener(new AccountingItemNoVatFrame_controlDebit_focusAdapter(this));
    colCSA.setColumnName("accountCodeTypeACC06");
    colDebit.setColumnName("debitAmountACC06");
    colCredit.setColumnName("creditAmountACC06");
    controlCSA.setAttributeName("accountCodeTypeACC06");
    controlCredit.setAttributeName("creditAmountACC06");
    controlCredit.addFocusListener(new AccountingItemNoVatFrame_controlCredit_focusAdapter(this));
    controlCSADescr.setAttributeName("accountDescriptionACC06");
    controlCompaniesCombo.setAttributeName("companyCodeSys01ACC05");
    controlMovDescr.setAttributeName("descriptionACC06");
    saveButton2.setText("saveButton2");
    this.getContentPane().add(headerPanel, BorderLayout.NORTH);
    this.getContentPane().add(mainPanel, BorderLayout.CENTER);
    this.getContentPane().add(buttonsPanel,  BorderLayout.SOUTH);
    buttonsPanel.add(saveButton2, null);
    headerPanel.add(labelItemNr,     new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    headerPanel.add(labelMotive,     new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    headerPanel.add(labelDate,     new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    headerPanel.add(controlDate,    new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    headerPanel.add(controlMotive,    new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    headerPanel.add(controlMotiveDescr,      new GridBagConstraints(2, 1, 2, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    headerPanel.add(labelDescr,    new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    headerPanel.add(controlDescr,   new GridBagConstraints(1, 2, 3, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    headerPanel.add(controlCompaniesCombo,   new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    totPanel.add(labelTotDebit,         new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    totPanel.add(controlTotDebit,         new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    totPanel.add(labelTotCredit,        new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 15, 5, 5), 0, 0));
    totPanel.add(controlTotCredit,       new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    totPanel.add(labelSbil,      new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 15, 5, 5), 0, 0));
    totPanel.add(controlSbil,      new GridBagConstraints(5, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(totPanel,     new GridBagConstraints(0, 2, 6, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    mainPanel.add(gridDetailPanel,      new GridBagConstraints(0, 0, 6, 1, 0.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 1, 0));
    gridDetailPanel.add(grid, BorderLayout.CENTER);
    gridDetailPanel.add(detailPanel, BorderLayout.SOUTH);
    grid.getColumnContainer().add(colCSA, null);
    detailPanel.add(labelCSA,               new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    detailPanel.add(controlCSA,                new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 40, 0));
    detailPanel.add(labelCode,              new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 15, 5, 5), 0, 0));
    detailPanel.add(controlScode,              new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 40, 0));
    detailPanel.add(controlAcode,                  new GridBagConstraints(5, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 40, 0));
    detailPanel.add(labelDebit,         new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    detailPanel.add(controlDebit,           new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 40, 0));
    detailPanel.add(labelCredit,         new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 15, 5, 5), 0, 0));
    detailPanel.add(labelMovDescr,        new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    detailPanel.add(controlMovDescr,              new GridBagConstraints(1, 4, 7, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    detailPanel.add(controlCcode,        new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 40, 0));
    detailPanel.add(controlCredit,       new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 40, 0));
    gridDetailPanel.add(toolbarPanel, BorderLayout.NORTH);
    toolbarPanel.add(insertButton1, null);
    toolbarPanel.add(editButton1, null);
    toolbarPanel.add(saveButton1, null);
    toolbarPanel.add(reloadButton1, null);
    toolbarPanel.add(deleteButton1, null);
    toolbarPanel.add(navigatorBar1, null);
    detailPanel.add(controlCSADescr,   new GridBagConstraints(6, 2, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    grid.getColumnContainer().add(colCode, null);
    grid.getColumnContainer().add(colDebit, null);
    grid.getColumnContainer().add(colCredit, null);
    grid.getColumnContainer().add(colMovDescr, null);
  }


  private void clearGrid() {
    detailPanel.setMode(Consts.READONLY);
    controlCSA.getComboBox().setSelectedIndex(-1);
    insertButton1.setEnabled(true);
  }


  public Form getHeaderPanel() {
    return headerPanel;
  }


  void controlDebit_focusLost(FocusEvent e) {
    if (controlDebit.getValue()!=null) {
      controlCredit.setValue(null);
    }
  }


  void controlCredit_focusLost(FocusEvent e) {
    if (controlCredit.getValue()!=null) {
      controlDebit.setValue(null);
    }
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
  public ComboBoxControl getControlCSA() {
    return controlCSA;
  }
  public CurrencyControl getControlTotCredit() {
    return controlTotCredit;
  }
  public CurrencyControl getControlTotDebit() {
    return controlTotDebit;
  }
  public CurrencyControl getControlSbil() {
    return controlSbil;
  }



}

class AccountingItemNoVatFrame_controlDebit_focusAdapter extends java.awt.event.FocusAdapter {
  AccountingItemNoVatFrame adaptee;

  AccountingItemNoVatFrame_controlDebit_focusAdapter(AccountingItemNoVatFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void focusLost(FocusEvent e) {
    adaptee.controlDebit_focusLost(e);
  }
}

class AccountingItemNoVatFrame_controlCredit_focusAdapter extends java.awt.event.FocusAdapter {
  AccountingItemNoVatFrame adaptee;

  AccountingItemNoVatFrame_controlCredit_focusAdapter(AccountingItemNoVatFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void focusLost(FocusEvent e) {
    adaptee.controlCredit_focusLost(e);
  }
}
