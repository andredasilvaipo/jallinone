package org.jallinone.sales.customers.client;

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
import org.jallinone.sales.customers.java.OrganizationCustomerVO;
import org.jallinone.sales.customers.java.PeopleCustomerVO;
import org.openswing.swing.table.java.ServerGridDataLocator;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.message.receive.java.*;
import java.util.Collection;
import org.jallinone.scheduler.activities.client.ScheduledActivitiesPanel;
import org.jallinone.scheduler.activities.client.ScheduledActivityController;
import org.jallinone.scheduler.activities.java.ScheduledActivityPK;
import org.jallinone.scheduler.activities.java.GridScheduledActivityVO;
import java.util.ArrayList;
import org.jallinone.scheduler.activities.java.ScheduledActivityVO;
import java.util.HashSet;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Detail frame related to a customer.</p>
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
public class CustomerDetailFrame extends InternalFrame {
  JPanel buttonsPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  InsertButton insertButton = new InsertButton();
  EditButton editButton = new EditButton();
  SaveButton saveButton = new SaveButton();
  ReloadButton reloadButton = new ReloadButton();
  DeleteButton deleteButton = new DeleteButton();
  OrganizationPanel organizationPanel = new OrganizationPanel(true);
  PeoplePanel peoplePanel = new PeoplePanel();
  LabelControl labelCompanyCode = new LabelControl();
  CompaniesComboControl controlCompanyCode = new CompaniesComboControl();
  JTabbedPane tabbedPane = new JTabbedPane();
  Form customerPanel = new Form();
  JPanel subjectPanel = new JPanel();
  JPanel cardPanel = new JPanel();
  CardLayout cardLayout1 = new CardLayout();
  ComboBoxControl controlSubjectType = new ComboBoxControl();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  LabelControl labelCustomerType = new LabelControl();
  TitledBorder titledBorder1;
  TitledBorder titledBorder2;
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  LabelControl labelCustomerCode = new LabelControl();
  TextControl controlCustomerCode = new TextControl();
  LabelControl labelPay = new LabelControl();
  CodLookupControl controlPayment = new CodLookupControl();
  TextControl controlPayDescr = new TextControl();
  LabelControl labelPricelist = new LabelControl();
  LabelControl labelBank = new LabelControl();
  CodLookupControl controlPricelist = new CodLookupControl();
  CodLookupControl controlBank = new CodLookupControl();
  TextControl controlPricelistDescr = new TextControl();
  TextControl controlBankDescr = new TextControl();

  LookupController bankController = new LookupController();
  LookupServerDataLocator bankDataLocator = new LookupServerDataLocator();

  LookupController payController = new LookupController();
  LookupServerDataLocator payDataLocator = new LookupServerDataLocator();

  LookupController pricelistController = new LookupController();
  LookupServerDataLocator pricelistDataLocator = new LookupServerDataLocator();

  JPanel subjectTypePanel = new JPanel();
  GridBagLayout gridBagLayout3 = new GridBagLayout();
  JPanel refPanel = new JPanel();
  JPanel discountsPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  BorderLayout borderLayout2 = new BorderLayout();
  JPanel discountButtonsPanel = new JPanel();
  GridControl discountsGrid = new GridControl();
  FlowLayout flowLayout3 = new FlowLayout();
  InsertButton insertButton2 = new InsertButton();
  EditButton editButton2 = new EditButton();
  SaveButton saveButton2 = new SaveButton();
  ReloadButton reloadButton2 = new ReloadButton();
  ExportButton exportButton2 = new ExportButton();
  NavigatorBar navigatorBar2 = new NavigatorBar();
  DeleteButton deleteButton2 = new DeleteButton();


  /** discounts grid data locator */
  private ServerGridDataLocator discountsGridDataLocator = new ServerGridDataLocator();

  ReferencesPanel referencesPanel = new ReferencesPanel();
  JPanel hierarPanel = new JPanel();
  SubjectHierarchyLevelsPanel hierarchiesPanel = new SubjectHierarchyLevelsPanel();
  BorderLayout borderLayout3 = new BorderLayout();
  BorderLayout borderLayout4 = new BorderLayout();
  TextColumn colDiscountCode = new TextColumn();
  TextColumn colDescr = new TextColumn();
  CodLookupColumn colCurrencyCode = new CodLookupColumn();
  DecimalColumn colMinValue = new DecimalColumn();
  DecimalColumn colMaxValue = new DecimalColumn();
  DecimalColumn colMinPerc = new DecimalColumn();
  DecimalColumn colMaxPerc = new DecimalColumn();
  DateColumn colStartDate = new DateColumn();
  DateColumn colEndDate = new DateColumn();

  LookupController currencyController = new LookupController();
  LookupServerDataLocator currencyDataLocator = new LookupServerDataLocator();
  JPanel destPanel = new JPanel();
  BorderLayout borderLayout5 = new BorderLayout();
  JPanel destButtonsPanel = new JPanel();
  FlowLayout flowLayout2 = new FlowLayout();
  ExportButton exportButton1 = new ExportButton();
  InsertButton insertButton1 = new InsertButton();
  EditButton editButton1 = new EditButton();
  CopyButton copyButton1 = new CopyButton();
  SaveButton saveButton1 = new SaveButton();
  ReloadButton reloadButton1 = new ReloadButton();
  DeleteButton deleteButton1 = new DeleteButton();
  GridControl destGrid = new GridControl();
  TextColumn colDestCod = new TextColumn();
  TextColumn colDestDescr = new TextColumn();
  TextColumn colDestAddress = new TextColumn();
  TextColumn colDestCity = new TextColumn();
  TextColumn colDestZip = new TextColumn();
  TextColumn colDestProv = new TextColumn();
  TextColumn colDestCountry = new TextColumn();

  /** destinations grid data locator */
  private ServerGridDataLocator destGridDataLocator = new ServerGridDataLocator();
  LabelControl labelAgent = new LabelControl();
  CodLookupControl controlAgentCod = new CodLookupControl();
  TextControl controlAgentName_1 = new TextControl();
  TextControl controlAgentName_2 = new TextControl();
  LabelControl labelTrustAmount = new LabelControl();
  NumericControl controlTrustAmount = new NumericControl();

  LookupController agentController = new LookupController();
  LookupServerDataLocator agentDataLocator = new LookupServerDataLocator();
  LabelControl labelVatCode = new LabelControl();
  CodLookupControl controlVatCode = new CodLookupControl();
  TextControl controlVatDescr = new TextControl();
  NumericControl controlVatDeductible = new NumericControl();
  NumericControl controlVatValue = new NumericControl();

  LookupController vatController = new LookupController();
  LookupServerDataLocator vatDataLocator = new LookupServerDataLocator();

  LabelControl labelCreditAccount = new LabelControl();
  LabelControl labelItemAccount = new LabelControl();
  LabelControl labelActivities = new LabelControl();
  LabelControl labelCharges = new LabelControl();
  CodLookupControl controlCreditsCode = new CodLookupControl();
  TextControl controlCreditsDescr = new TextControl();
  CodLookupControl controlItemsCode = new CodLookupControl();
  TextControl controlItemsDescr = new TextControl();
  CodLookupControl controlActCode = new CodLookupControl();
  TextControl controlActDescr = new TextControl();
  CodLookupControl controlChargesCode = new CodLookupControl();
  TextControl controlChargesDescr = new TextControl();

  LookupController creditController = new LookupController();
  LookupServerDataLocator creditDataLocator = new LookupServerDataLocator();
  LookupController itemsController = new LookupController();
  LookupServerDataLocator itemsDataLocator = new LookupServerDataLocator();
  LookupController chargesController = new LookupController();
  LookupServerDataLocator chargesDataLocator = new LookupServerDataLocator();
  LookupController actController = new LookupController();
  LookupServerDataLocator actDataLocator = new LookupServerDataLocator();

  private ScheduledActivitiesPanel panel = new ScheduledActivitiesPanel(false);

  private CustomerController customerController = null;
  NavigatorBar navigatorBar = new NavigatorBar();


  public CustomerDetailFrame(CustomerController controller) {
    this.customerController = controller;
    try {
      jbInit();
      setSize(750,550);
      setMinimumSize(new Dimension(750, 550));

      customerPanel.setFormController(controller);
      customerPanel.setInsertButton(insertButton);
      customerPanel.setEditButton(editButton);
      customerPanel.setReloadButton(reloadButton);
      customerPanel.setDeleteButton(deleteButton);
      customerPanel.setSaveButton(saveButton);
      customerPanel.setFunctionId("SAL07");

      // link the parent grid to the current Form...
      HashSet pk = new HashSet();
      pk.add("companyCodeSys01REG04");
      pk.add("progressiveREG04");
      pk.add("subjectTypeREG04");
      customerPanel.linkGrid(controller.getGridFrame().getGrid(),pk,true,true,true,navigatorBar);

      organizationPanel.setFunctionId("SAL07");
      peoplePanel.setFunctionId("SAL07");

      controlSubjectType.getComboBox().addItemListener(new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
          if (e.getStateChange()==e.SELECTED)
            subjectChanged((String)controlSubjectType.getValue());
        }
      });
      controlSubjectType.getComboBox().setSelectedIndex(0);

      // banks lookup...
      bankDataLocator.setGridMethodName("loadBanks");
      bankDataLocator.setValidationMethodName("validateBankCode");
      controlBank.setLookupController(bankController);
      controlBank.setControllerMethodName("getBanksList");
      bankController.setLookupDataLocator(bankDataLocator);
      bankController.setFrameTitle("banks");
      bankController.setLookupValueObjectClassName("org.jallinone.registers.bank.java.BankVO");
      bankController.addLookup2ParentLink("bankCodeREG12", "bankCodeReg12SAL07");
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
      payController.addLookup2ParentLink("paymentCodeREG10", "paymentCodeReg10SAL07");
      payController.addLookup2ParentLink("descriptionSYS10","paymentDescriptionSYS10");
      payController.setAllColumnVisible(false);
      payController.setVisibleColumn("paymentCodeREG10", true);
      payController.setVisibleColumn("descriptionSYS10", true);
      payController.setPreferredWidthColumn("descriptionSYS10",200);
      new CustomizedColumns(new BigDecimal(212),payController);
			payController.addLookupListener(new LookupListener() {

				public void beforeLookupAction(org.openswing.swing.message.receive.java.ValueObject parentVO) {
					Subject subVO = (Subject)customerPanel.getVOModel().getValueObject();
					payDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,subVO.getCompanyCodeSys01REG04());
					payDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,subVO.getCompanyCodeSys01REG04());
				}

				public void codeChanged(org.openswing.swing.message.receive.java.ValueObject parentVO,Collection parentChangedAttributes) {	}

				public void codeValidated(boolean validated) { }

				public void forceValidate() { }

			});

      // pricelists lookup...
      pricelistDataLocator.setGridMethodName("loadPricelists");
      pricelistDataLocator.setValidationMethodName("validatePricelistCode");
      controlPricelist.setLookupController(pricelistController);
      controlPricelist.setControllerMethodName("getSalePricesList");
      pricelistController.setLookupDataLocator(pricelistDataLocator);
      pricelistController.setFrameTitle("pricelists");
      pricelistController.setLookupValueObjectClassName("org.jallinone.sales.pricelist.java.PricelistVO");
      pricelistController.addLookup2ParentLink("pricelistCodeSAL01", "pricelistCodeSal01SAL07");
      pricelistController.addLookup2ParentLink("descriptionSYS10","pricelistDescriptionSYS10");
      pricelistController.setAllColumnVisible(false);
      pricelistController.setVisibleColumn("pricelistCodeSAL01", true);
      pricelistController.setVisibleColumn("descriptionSYS10", true);
      pricelistController.setPreferredWidthColumn("descriptionSYS10",200);
      pricelistController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {}

        public void beforeLookupAction(ValueObject parentVO) {
          Subject subVO = (Subject)customerPanel.getVOModel().getValueObject();
          pricelistDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,subVO.getCompanyCodeSys01REG04());
          pricelistDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,subVO.getCompanyCodeSys01REG04());
        }

        public void forceValidate() {}

      });
      new CustomizedColumns(new BigDecimal(302),pricelistController);

      discountsGrid.setController(new DiscountsController(this));
      discountsGrid.setGridDataLocator(discountsGridDataLocator);
      discountsGridDataLocator.setServerMethodName("loadCustomerDiscounts");

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
      new CustomizedColumns(new BigDecimal(182),currencyController);

      destGrid.setController(new DestinationsController(this));
      destGrid.setGridDataLocator(destGridDataLocator);
      destGridDataLocator.setServerMethodName("loadDestinations");

      // agent lookup...
      agentDataLocator.setGridMethodName("loadAgents");
      agentDataLocator.setValidationMethodName("validateAgentCode");
      controlAgentCod.setLookupController(agentController);
      controlAgentCod.setControllerMethodName("getAgentsList");
      agentController.setLookupDataLocator(agentDataLocator);
      agentController.setFrameTitle("agents");
      agentController.setLookupValueObjectClassName("org.jallinone.sales.agents.java.AgentVO");
      agentController.addLookup2ParentLink("agentCodeSAL10", "agentCodeSAL10");
      agentController.addLookup2ParentLink("name_1REG04", "agentName_1REG04");
      agentController.addLookup2ParentLink("name_2REG04", "agentName_2REG04");
      agentController.addLookup2ParentLink("progressiveReg04SAL10", "agentProgressiveReg04SAL07");
      agentController.setAllColumnVisible(false);
      agentController.setVisibleColumn("agentCodeSAL10", true);
      agentController.setVisibleColumn("name_1REG04", true);
      agentController.setVisibleColumn("name_2REG04", true);
      agentController.setVisibleColumn("name_2REG04", true);
      agentController.setVisibleColumn("descriptionSYS10", true);
      agentController.setVisibleColumn("percentageSAL10", true);
      agentController.setFramePreferedSize(new Dimension(600,400));
      new CustomizedColumns(new BigDecimal(342),agentController);

      // lookup vat...
      vatDataLocator.setGridMethodName("loadVats");
      vatDataLocator.setValidationMethodName("validateVatCode");
      controlVatCode.setLookupController(vatController);
      controlVatCode.setControllerMethodName("getVatsList");
      vatController.setLookupDataLocator(vatDataLocator);
      vatController.setFrameTitle("vats");
      vatController.setLookupValueObjectClassName("org.jallinone.registers.vat.java.VatVO");
      vatController.addLookup2ParentLink("vatCodeREG01", "vatCodeReg01SAL07");
      vatController.addLookup2ParentLink("descriptionSYS10", "vatDescriptionSYS10");
      vatController.addLookup2ParentLink("valueREG01","vatValueREG01");
      vatController.addLookup2ParentLink("deductibleREG01","vatDeductibleREG01");
      vatController.setAllColumnVisible(false);
      vatController.setVisibleColumn("vatCodeREG01", true);
      vatController.setVisibleColumn("descriptionSYS10", true);
      vatController.setVisibleColumn("valueREG01", true);
      vatController.setVisibleColumn("deductibleREG01", true);
      vatController.setPreferredWidthColumn("descriptionSYS10",200);
      vatController.setFramePreferedSize(new Dimension(510,400));
      CustomizedColumns vatCust = new CustomizedColumns(new BigDecimal(162),vatController);



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
      creditController.setPreferredWidthColumn("accountCodeACC02", 100);
      creditController.setPreferredWidthColumn("descriptionSYS10", 290);
      creditController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) { }

        public void beforeLookupAction(ValueObject parentVO) {
          creditDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanyCode.getValue());
          creditDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanyCode.getValue());
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
      itemsController.setVisibleColumn("accountCodeACC02", true);
      itemsController.setVisibleColumn("descriptionSYS10", true);
      itemsController.setFilterableColumn("accountCodeACC02", true);
      itemsController.setFilterableColumn("descriptionSYS10", true);
      itemsController.setSortedColumn("accountCodeACC02", "ASC", 1);
      itemsController.setSortableColumn("accountCodeACC02", true);
      itemsController.setSortableColumn("descriptionSYS10", true);
      itemsController.setPreferredWidthColumn("accountCodeACC02", 100);
      itemsController.setPreferredWidthColumn("descriptionSYS10", 290);
      itemsController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) { }

        public void beforeLookupAction(ValueObject parentVO) {
          itemsDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanyCode.getValue());
          itemsDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanyCode.getValue());
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
      actController.setVisibleColumn("accountCodeACC02", true);
      actController.setVisibleColumn("descriptionSYS10", true);
      actController.setFilterableColumn("accountCodeACC02", true);
      actController.setFilterableColumn("descriptionSYS10", true);
      actController.setSortedColumn("accountCodeACC02", "ASC", 1);
      actController.setSortableColumn("accountCodeACC02", true);
      actController.setSortableColumn("descriptionSYS10", true);
      actController.setPreferredWidthColumn("accountCodeACC02", 100);
      actController.setPreferredWidthColumn("descriptionSYS10", 290);
      actController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) { }

        public void beforeLookupAction(ValueObject parentVO) {
          actDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanyCode.getValue());
          actDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanyCode.getValue());
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
      chargesController.setVisibleColumn("accountCodeACC02", true);
      chargesController.setVisibleColumn("descriptionSYS10", true);
      chargesController.setFilterableColumn("accountCodeACC02", true);
      chargesController.setFilterableColumn("descriptionSYS10", true);
      chargesController.setSortedColumn("accountCodeACC02", "ASC", 1);
      chargesController.setSortableColumn("accountCodeACC02", true);
      chargesController.setSortableColumn("descriptionSYS10", true);
      chargesController.setPreferredWidthColumn("accountCodeACC02", 100);
      chargesController.setPreferredWidthColumn("descriptionSYS10", 290);
      chargesController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) { }

        public void beforeLookupAction(ValueObject parentVO) {
          chargesDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanyCode.getValue());
          chargesDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanyCode.getValue());
        }

        public void forceValidate() {}

      });




      CustomizedControls customizedControls = new CustomizedControls(tabbedPane,customerPanel,new BigDecimal(282));

      panel.getGrid().setAutoLoadData(false);

      panel.setController(new CompanyGridController() {


            /**
             * Callback method invoked when the user has double clicked on the selected row of the grid.
             * @param rowNumber selected row index
             * @param persistentObject v.o. related to the selected row
             */
            public void doubleClick(int rowNumber,ValueObject persistentObject) {
              new ScheduledActivityController(
                  panel.getGrid(),
                  null,
                  new ScheduledActivityPK(
                    ((GridScheduledActivityVO)persistentObject).getCompanyCodeSys01SCH06(),
                    ((GridScheduledActivityVO)persistentObject).getProgressiveSCH06()
                  ),
                  true
              );
            }


            /**
             * Callback method invoked before saving data when the grid was in INSERT mode (on pressing save button).
             * @return <code>true</code> allows the saving to continue, <code>false</code> the saving is interrupted
             */
            public boolean beforeInsertGrid(GridControl grid) {
              if (super.beforeInsertGrid(grid)) {
                ScheduledActivityController c = new ScheduledActivityController(panel.getGrid(),null,null,true);
                c.getControlSubjectType().setValue(controlSubjectType.getValue());

                if (controlSubjectType.getValue().equals(ApplicationConsts.SUBJECT_ORGANIZATION_CUSTOMER)) {
                  OrganizationCustomerVO model = (OrganizationCustomerVO)customerPanel.getVOModel().getValueObject();
                  c.getControlName_1Subject().setValue(model.getName_1REG04());
                  c.getControlName_2Subject().setValue(model.getName_2REG04());
                  ScheduledActivityVO actVO = (ScheduledActivityVO)c.getVOModel().getValueObject();
                  actVO.setProgressiveReg04SubjectSCH06(model.getProgressiveREG04());
                  c.getDetailFrame().getMainForm().getForm().pull("progressiveReg04SubjectSCH06");
                }
                else {
                  PeopleCustomerVO model = (PeopleCustomerVO)customerPanel.getVOModel().getValueObject();
                  c.getControlName_1Subject().setValue(model.getName_1REG04());
                  c.getControlName_2Subject().setValue(model.getName_2REG04());
                  ScheduledActivityVO actVO = (ScheduledActivityVO)c.getVOModel().getValueObject();
                  actVO.setProgressiveReg04SubjectSCH06(model.getProgressiveREG04());
                  c.getDetailFrame().getMainForm().getForm().pull("progressiveReg04SubjectSCH06");
                }

              }
              return false;
            }


            /**
             * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
             * @param persistentObjects value objects to delete (related to the currently selected rows)
             * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
             */
            public Response deleteRecords(ArrayList persistentObjects) throws Exception {
              GridScheduledActivityVO vo = null;
              ScheduledActivityPK pk = null;
              ArrayList pks = new ArrayList();
              for(int i=0;i<persistentObjects.size();i++) {
                vo = (GridScheduledActivityVO)persistentObjects.get(i);
                pk = new ScheduledActivityPK(vo.getCompanyCodeSys01SCH06(),vo.getProgressiveSCH06());
                pks.add(pk);
              }
              Response response = ClientUtils.getData("deleteScheduledActivities",pks);
              return response;
            }


            /**
             * Method used to define the background color for each cell of the grid.
             * @param rowNumber selected row index
             * @param attributedName attribute name related to the column currently selected
             * @param value object contained in the selected cell
             * @return background color of the selected cell
             */
            public Color getBackgroundColor(int row,String attributedName,Object value) {
              GridScheduledActivityVO vo = (GridScheduledActivityVO)panel.getGrid().getVOListTableModel().getObjectForRow(row);
              if (attributedName.equals("activityStateSCH06")) {
                Color color = null;
                if (vo.getActivityStateSCH06().equals(ApplicationConsts.CLOSED) ||
                    vo.getActivityStateSCH06().equals(ApplicationConsts.INVOICED))
                  return super.getBackgroundColor(row,attributedName,value);
                else return new Color(241,143,137);
              }
              else if (attributedName.equals("prioritySCH06")) {
                Color color = null;
                if (vo.getPrioritySCH06().equals(ApplicationConsts.PRIORITY_HIGHEST))
                  color = new Color(241,123,137);
                else if (vo.getPrioritySCH06().equals(ApplicationConsts.PRIORITY_HIGH))
                  color = new Color(248,176,181);
                else if (vo.getPrioritySCH06().equals(ApplicationConsts.PRIORITY_NORMAL))
                  color = new Color(191,246,207);
                else if (vo.getPrioritySCH06().equals(ApplicationConsts.PRIORITY_LOW))
                  color = new Color(191,226,207);
                else if (vo.getPrioritySCH06().equals(ApplicationConsts.PRIORITY_TRIVIAL))
                  color = new Color(191,206,207);
                return color;
              }
              else
                return super.getBackgroundColor(row,attributedName,value);

            }

      }); // end activities grid controller


    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    discountsGrid.setMaxNumberOfRowsOnInsert(50);
    destGrid.setMaxNumberOfRowsOnInsert(50);
    titledBorder1 = new TitledBorder("");
    titledBorder2 = new TitledBorder("");
    controlSubjectType.setDomainId("CUSTOMER_TYPE");
    controlSubjectType.setLinkLabel(labelCustomerType);
    controlSubjectType.setAttributeName("subjectTypeREG04");
    controlSubjectType.setEnabledOnEdit(false);
    controlSubjectType.setCanCopy(true);
    subjectPanel.setLayout(gridBagLayout1);
    labelCustomerType.setText("customer type");
    customerPanel.setBorder(titledBorder1);
    customerPanel.setLayout(gridBagLayout2);
    titledBorder1.setTitle(ClientSettings.getInstance().getResources().getResource("customer"));
    titledBorder1.setTitleColor(Color.blue);
    labelCustomerCode.setText("customerCodeSAL07");
    labelPay.setText("payment terms");
    labelPricelist.setText("pricelist");
    labelBank.setText("bank");
    controlCustomerCode.setAttributeName("customerCodeSAL07");
    controlCustomerCode.setCanCopy(false);
    controlCustomerCode.setLinkLabel(labelCustomerCode);
    controlCustomerCode.setMaxCharacters(20);
//    controlCustomerCode.setRequired(true);
    controlCustomerCode.setTrimText(true);
    controlCustomerCode.setUpperCase(true);
    controlCustomerCode.setEnabledOnEdit(false);
    controlPayment.setAttributeName("paymentCodeReg10SAL07");
    controlPayment.setCanCopy(true);
    controlPayment.setLinkLabel(labelPay);
    controlPayment.setMaxCharacters(20);
    controlPayment.setRequired(true);
    controlPayDescr.setAttributeName("paymentDescriptionSYS10");
    controlPayDescr.setCanCopy(true);
    controlPayDescr.setEnabledOnInsert(false);
    controlPayDescr.setEnabledOnEdit(false);
    controlPricelist.setAttributeName("pricelistCodeSal01SAL07");
    controlPricelist.setCanCopy(true);
    controlPricelist.setLinkLabel(labelPricelist);
    controlPricelist.setMaxCharacters(20);
    controlPricelistDescr.setAttributeName("pricelistDescriptionSYS10");
    controlPricelistDescr.setCanCopy(true);
    controlPricelistDescr.setMaxCharacters(255);
    controlPricelistDescr.setEnabledOnInsert(false);
    controlPricelistDescr.setEnabledOnEdit(false);
    controlBank.setAttributeName("bankCodeReg12SAL07");
    controlBank.setCanCopy(true);
    controlBank.setLinkLabel(labelBank);
    controlBank.setMaxCharacters(20);
    controlBankDescr.setAttributeName("descriptionREG12");
    controlBankDescr.setCanCopy(true);
    controlBankDescr.setEnabledOnInsert(false);
    controlBankDescr.setEnabledOnEdit(false);
    subjectTypePanel.setLayout(gridBagLayout3);
    cardPanel.setLayout(cardLayout1);
    refPanel.setLayout(borderLayout1);
    discountsPanel.setLayout(borderLayout2);
    discountButtonsPanel.setLayout(flowLayout3);
    flowLayout3.setAlignment(FlowLayout.LEFT);
    insertButton2.setText("insertButton2");
    editButton2.setText("editButton2");
    saveButton2.setText("saveButton2");
    reloadButton2.setText("reloadButton2");
    deleteButton2.setText("deleteButton2");
    discountsGrid.setAutoLoadData(false);
    discountsGrid.setDeleteButton(deleteButton2);
    discountsGrid.setEditButton(editButton2);
    discountsGrid.setExportButton(exportButton2);
    discountsGrid.setFunctionId("SAL07");
    discountsGrid.setMaxSortedColumns(3);
    discountsGrid.setInsertButton(insertButton2);
    discountsGrid.setNavBar(navigatorBar2);
    discountsGrid.setReloadButton(reloadButton2);
    discountsGrid.setSaveButton(saveButton2);
    discountsGrid.setValueObjectClassName("org.jallinone.sales.discounts.java.CustomerDiscountVO");
    hierarPanel.setLayout(borderLayout4);
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
    destPanel.setLayout(borderLayout5);
    destButtonsPanel.setLayout(flowLayout2);
    flowLayout2.setAlignment(FlowLayout.LEFT);
    exportButton1.setText("exportButton1");
    insertButton1.setText("insertButton1");
    editButton1.setText("editButton1");
    copyButton1.setText("copyButton1");
    saveButton1.setText("saveButton1");
    reloadButton1.setText("reloadButton1");
    deleteButton1.setText("deleteButton1");
    destGrid.setAutoLoadData(false);
    destGrid.setCopyButton(copyButton1);
    destGrid.setDeleteButton(deleteButton1);
    destGrid.setEditButton(editButton1);
    destGrid.setExportButton(exportButton1);
    destGrid.setFunctionId("SAL07");
    destGrid.setMaxSortedColumns(3);
    destGrid.setInsertButton(insertButton1);
    destGrid.setReloadButton(reloadButton1);
    destGrid.setSaveButton(saveButton1);
    destGrid.setValueObjectClassName("org.jallinone.sales.destinations.java.DestinationVO");
    colDestCod.setMaxCharacters(20);
    colDestCod.setColumnName("destinationCodeREG18");
    colDestCod.setTrimText(true);
    colDestCod.setUpperCase(true);
    colDestCod.setColumnFilterable(true);
    colDestCod.setColumnSortable(true);
    colDestCod.setEditableOnInsert(true);
    colDestCod.setPreferredWidth(90);
    colDestCod.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colDestDescr.setColumnName("descriptionREG18");
    colDestDescr.setColumnDuplicable(true);
    colDestDescr.setColumnSortable(true);
    colDestDescr.setEditableOnEdit(true);
    colDestDescr.setEditableOnInsert(true);
    colDestDescr.setPreferredWidth(250);
    colDestAddress.setColumnName("addressREG18");
    colDestAddress.setHeaderColumnName("address");
    colDestAddress.setColumnDuplicable(true);
    colDestAddress.setColumnFilterable(true);
    colDestAddress.setColumnSortable(true);
    colDestAddress.setEditableOnEdit(true);
    colDestAddress.setEditableOnInsert(true);
    colDestAddress.setPreferredWidth(250);
    colDestCity.setColumnName("cityREG18");
    colDestCity.setHeaderColumnName("city");
    colDestCity.setColumnDuplicable(true);
    colDestCity.setColumnFilterable(true);
    colDestCity.setColumnSortable(true);
    colDestCity.setEditableOnEdit(true);
    colDestCity.setEditableOnInsert(true);
    colDestZip.setColumnName("zipREG18");
    colDestZip.setHeaderColumnName("zip");
    colDestZip.setMaxCharacters(20);
    colDestZip.setColumnDuplicable(true);
    colDestZip.setColumnFilterable(true);
    colDestZip.setColumnSortable(true);
    colDestZip.setEditableOnEdit(true);
    colDestZip.setEditableOnInsert(true);
    colDestZip.setPreferredWidth(60);
    colDestProv.setColumnName("provinceREG18");
    colDestProv.setHeaderColumnName("prov");
    colDestProv.setMaxCharacters(20);
    colDestProv.setTrimText(true);
    colDestProv.setUpperCase(true);
    colDestProv.setColumnDuplicable(true);
    colDestProv.setColumnFilterable(true);
    colDestProv.setColumnSortable(true);
    colDestProv.setEditableOnEdit(true);
    colDestProv.setEditableOnInsert(true);
    colDestProv.setPreferredWidth(70);
    colDestCountry.setColumnName("countryREG18");
    colDestCountry.setHeaderColumnName("country");
    colDestCountry.setMaxCharacters(20);
    colDestCountry.setTrimText(true);
    colDestCountry.setUpperCase(true);
    colDestCountry.setColumnDuplicable(true);
    colDestCountry.setColumnFilterable(true);
    colDestCountry.setColumnSortable(true);
    colDestCountry.setEditableOnEdit(true);
    colDestCountry.setEditableOnInsert(true);
    colDestCountry.setPreferredWidth(90);
    labelAgent.setText("agentCodeSAL10");
    controlAgentCod.setCanCopy(true);
    controlAgentCod.setEnabledOnInsert(false);
    controlAgentCod.setLinkLabel(labelAgent);
    controlAgentCod.setMaxCharacters(20);
    controlAgentName_1.setCanCopy(true);
    controlAgentName_1.setEnabledOnInsert(false);
    controlAgentName_1.setEnabledOnEdit(false);
    controlAgentName_2.setCanCopy(true);
    controlAgentName_2.setEnabledOnInsert(false);
    controlAgentName_2.setEnabledOnEdit(false);
    labelTrustAmount.setText("trustAmountSAL07");
    controlTrustAmount.setLinkLabel(labelTrustAmount);
    labelVatCode.setText("vatCode");
    controlVatCode.setLinkLabel(labelVatCode);
    controlVatCode.setMaxCharacters(20);
    controlVatCode.setAttributeName("vatCodeReg01SAL07");
    controlVatValue.setEnabledOnInsert(false);
    controlVatValue.setEnabledOnEdit(false);
    controlVatValue.setAttributeName("vatValueREG01");
    controlVatDeductible.setEnabledOnInsert(false);
    controlVatDeductible.setEnabledOnEdit(false);
    controlVatDeductible.setAttributeName("vatDeductibleREG01");
    controlVatDescr.setEnabledOnInsert(false);
    controlVatDescr.setEnabledOnEdit(false);
    controlVatDescr.setAttributeName("vatDescriptionSYS10");
    cardPanel.add(organizationPanel,ApplicationConsts.SUBJECT_ORGANIZATION_CUSTOMER);
    cardPanel.add(peoplePanel,ApplicationConsts.SUBJECT_PEOPLE_CUSTOMER);
    subjectTypePanel.add(labelCustomerType,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    subjectTypePanel.add(controlSubjectType,   new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 130, 0));
    subjectPanel.add(subjectTypePanel,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    subjectPanel.add(cardPanel,      new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    this.setTitle(ClientSettings.getInstance().getResources().getResource("customer detail"));
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
    tabbedPane.add(subjectPanel,   "generic data");
    this.getContentPane().add(tabbedPane, BorderLayout.CENTER);
    customerPanel.add(labelCompanyCode,            new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    customerPanel.add(controlCompanyCode,           new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 50, 0));

    tabbedPane.add(customerPanel,    "customerPanel");
    customerPanel.add(labelCustomerCode,           new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
    customerPanel.add(controlCustomerCode,                    new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    customerPanel.add(labelPay,           new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    customerPanel.add(controlPayment,               new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    customerPanel.add(controlPayDescr,            new GridBagConstraints(2, 2, 4, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    customerPanel.add(labelPricelist,           new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    customerPanel.add(labelBank,             new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
    customerPanel.add(controlPricelist,           new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    customerPanel.add(controlBank,             new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    customerPanel.add(controlPricelistDescr,            new GridBagConstraints(2, 3, 4, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    customerPanel.add(controlBankDescr,            new GridBagConstraints(2, 4, 4, 4, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0, 0));
    customerPanel.add(labelAgent,          new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
    tabbedPane.add(refPanel,   "references");
    refPanel.add(referencesPanel, BorderLayout.CENTER);
    tabbedPane.add(hierarPanel,  "hierarchies");
    tabbedPane.add(discountsPanel,   "discounts");
    discountsPanel.add(discountButtonsPanel,  BorderLayout.NORTH);
    discountsPanel.add(discountsGrid, BorderLayout.CENTER);
    discountsGrid.getColumnContainer().add(colDiscountCode, null);
    discountButtonsPanel.add(insertButton2, null);
    discountButtonsPanel.add(editButton2, null);
    discountButtonsPanel.add(saveButton2, null);
    discountButtonsPanel.add(reloadButton2, null);
    discountButtonsPanel.add(deleteButton2, null);
    discountButtonsPanel.add(exportButton2, null);
    discountButtonsPanel.add(navigatorBar2, null);
    hierarPanel.add(hierarchiesPanel,  BorderLayout.CENTER);
    discountsGrid.getColumnContainer().add(colDescr, null);
    discountsGrid.getColumnContainer().add(colCurrencyCode, null);
    discountsGrid.getColumnContainer().add(colMinValue, null);
    discountsGrid.getColumnContainer().add(colMaxValue, null);
    discountsGrid.getColumnContainer().add(colMinPerc, null);
    discountsGrid.getColumnContainer().add(colMaxPerc, null);
    discountsGrid.getColumnContainer().add(colStartDate, null);
    discountsGrid.getColumnContainer().add(colEndDate, null);
    tabbedPane.add(destPanel,   "destinations");
    destPanel.add(destButtonsPanel, BorderLayout.NORTH);
    destButtonsPanel.add(insertButton1, null);
    destButtonsPanel.add(copyButton1, null);
    destButtonsPanel.add(editButton1, null);
    destButtonsPanel.add(saveButton1, null);
    destButtonsPanel.add(reloadButton1, null);
    destButtonsPanel.add(deleteButton1, null);
    destButtonsPanel.add(exportButton1, null);
    destPanel.add(destGrid,  BorderLayout.CENTER);
    destGrid.getColumnContainer().add(colDestCod, null);
    destGrid.getColumnContainer().add(colDestDescr, null);
    destGrid.getColumnContainer().add(colDestAddress, null);
    destGrid.getColumnContainer().add(colDestCity, null);
    destGrid.getColumnContainer().add(colDestZip, null);
    destGrid.getColumnContainer().add(colDestProv, null);
    destGrid.getColumnContainer().add(colDestCountry, null);
    controlAgentCod.setAttributeName("agentCodeSAL10");
    controlAgentName_1.setAttributeName("agentName_1REG04");
    controlAgentName_2.setAttributeName("agentName_2REG04");
    controlTrustAmount.setAttributeName("trustAmountSAL07");
    customerPanel.add(controlAgentCod,           new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    customerPanel.add(controlAgentName_1,            new GridBagConstraints(2, 5, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    customerPanel.add(controlAgentName_2,           new GridBagConstraints(3, 5, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    customerPanel.add(labelTrustAmount,          new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
    customerPanel.add(controlTrustAmount,       new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    customerPanel.add(labelVatCode,         new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    customerPanel.add(controlVatCode,        new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    customerPanel.add(controlVatDescr,       new GridBagConstraints(2, 7, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0, 0));
    customerPanel.add(controlVatDeductible,      new GridBagConstraints(4, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    customerPanel.add(controlVatValue,     new GridBagConstraints(3, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 0), 0, 0));

    labelCreditAccount.setText("credits account");
    labelItemAccount.setText("selling items account");
    labelActivities.setText("selling activities account");
    labelCharges.setText("selling charges account");
    controlCreditsCode.setAttributeName("creditAccountCodeAcc02SAL07");
    controlCreditsDescr.setAttributeName("creditAccountDescrSAL07");
    controlItemsCode.setAttributeName("itemsAccountCodeAcc02SAL07");
    controlItemsDescr.setAttributeName("itemsAccountDescrSAL07");
    controlActCode.setAttributeName("activitiesAccountCodeAcc02SAL07");
    controlActDescr.setAttributeName("activitiesAccountDescrSAL07");
    controlChargesCode.setAttributeName("chargesAccountCodeAcc02SAL07");
    controlChargesDescr.setAttributeName("chargesAccountDescrSAL07");

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

    customerPanel.add(labelCreditAccount,      new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
    customerPanel.add(labelItemAccount,      new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
    customerPanel.add(labelActivities,      new GridBagConstraints(0, 10, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
    customerPanel.add(labelCharges,       new GridBagConstraints(0, 11, 1, 1, 0.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
    customerPanel.add(controlCreditsCode,      new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    customerPanel.add(controlCreditsDescr,      new GridBagConstraints(2, 8, 4, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    customerPanel.add(controlItemsCode,      new GridBagConstraints(1, 9, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    customerPanel.add(controlItemsDescr,      new GridBagConstraints(2, 9, 4, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    customerPanel.add(controlActCode,      new GridBagConstraints(1, 10, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    customerPanel.add(controlActDescr,      new GridBagConstraints(2, 10, 4, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    customerPanel.add(controlChargesCode,      new GridBagConstraints(1, 11, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    customerPanel.add(controlChargesDescr,      new GridBagConstraints(2, 11, 4, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));

    tabbedPane.add(panel,   "activities");

    tabbedPane.setTitleAt(0,ClientSettings.getInstance().getResources().getResource("generic data"));
    tabbedPane.setTitleAt(1,ClientSettings.getInstance().getResources().getResource("customer data"));
    tabbedPane.setTitleAt(2,ClientSettings.getInstance().getResources().getResource("references"));
    tabbedPane.setTitleAt(3,ClientSettings.getInstance().getResources().getResource("hierarchies"));
    tabbedPane.setTitleAt(4,ClientSettings.getInstance().getResources().getResource("discounts"));
    tabbedPane.setTitleAt(5,ClientSettings.getInstance().getResources().getResource("destinations"));
    tabbedPane.setTitleAt(6,ClientSettings.getInstance().getResources().getResource("scheduled activities"));

  }



  public void subjectChanged(String subjectTypeREG04) {
    cardLayout1.show(cardPanel,subjectTypeREG04);
    if (controlSubjectType.getValue()==null ||
        !controlSubjectType.getValue().equals(subjectTypeREG04))
        controlSubjectType.setValue(subjectTypeREG04);

    if (subjectTypeREG04.equals(ApplicationConsts.SUBJECT_ORGANIZATION_CUSTOMER)) {
      customerPanel.setVOClassName("org.jallinone.sales.customers.java.OrganizationCustomerVO");
      customerPanel.getVOModel().setValueObject(new OrganizationCustomerVO());

      customerPanel.removeLinkedPanel(peoplePanel);
      customerPanel.addLinkedPanel(organizationPanel);

    }
    else {
      customerPanel.setVOClassName("org.jallinone.sales.customers.java.PeopleCustomerVO");
      customerPanel.getVOModel().setValueObject(new PeopleCustomerVO());

      customerPanel.removeLinkedPanel(organizationPanel);
      customerPanel.addLinkedPanel(peoplePanel);

    }

  }


  public Form getCurrentForm() {
    return customerPanel;
  }


  public ComboBoxControl getControlSubjectType() {
    return controlSubjectType;
  }


  public ReferencesPanel getReferencesPanel() {
    return referencesPanel;
  }
  public SubjectHierarchyLevelsPanel getHierarchiesPanel() {
    return hierarchiesPanel;
  }
  public GridControl getDiscountsGrid() {
    return discountsGrid;
  }


  public final void setButtonsEnabled(boolean enabled) {
    insertButton2.setEnabled(enabled);
    editButton2.setEnabled(enabled);
    deleteButton2.setEnabled(enabled);
    exportButton2.setEnabled(enabled);
//    copyButton2.setEnabled(enabled);
    referencesPanel.setButtonsEnabled(enabled);
    hierarchiesPanel.setButtonsEnabled(enabled);

    insertButton1.setEnabled(enabled);
    editButton1.setEnabled(enabled);
    deleteButton1.setEnabled(enabled);
    exportButton1.setEnabled(enabled);
    copyButton1.setEnabled(enabled);

    panel.setButtonsEnabled(enabled);
  }


  public GridControl getActivitiesGrid() {
    return panel.getGrid();
  }


  public GridControl getDestGrid() {
    return destGrid;
  }


  public LookupServerDataLocator getAgentDataLocator() {
    return agentDataLocator;
  }


  public CodLookupControl getControlCreditsCode() {
    return controlCreditsCode;
  }


  public CodLookupControl getControlChargesCode() {
    return controlChargesCode;
  }


  public CodLookupControl getControlItemsCode() {
    return controlItemsCode;
  }


  public CodLookupControl getControlActCode() {
    return controlActCode;
  }




}
