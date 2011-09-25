package org.jallinone.contacts.client;

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


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Detail frame used to convert a contact to a customer.</p>
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
public class Contact2CustomerDetailFrame extends InternalFrame {
  JPanel buttonsPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  SaveButton saveButton = new SaveButton();
  Form customerPanel = new Form();
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

  GridBagLayout gridBagLayout3 = new GridBagLayout();
  BorderLayout borderLayout1 = new BorderLayout();
  BorderLayout borderLayout2 = new BorderLayout();

  BorderLayout borderLayout3 = new BorderLayout();
  BorderLayout borderLayout4 = new BorderLayout();

  LabelControl labelAgent = new LabelControl();
  CodLookupControl controlAgentCod = new CodLookupControl();
  TextControl controlAgentName_1 = new TextControl();
  TextControl controlAgentName_2 = new TextControl();

  LookupController agentController = new LookupController();
  LookupServerDataLocator agentDataLocator = new LookupServerDataLocator();

  LabelControl labelVatCode = new LabelControl();
  CodLookupControl controlVatCode = new CodLookupControl();
  TextControl controlVatDescr = new TextControl();
  NumericControl controlVatDeductible = new NumericControl();
  NumericControl controlVatValue = new NumericControl();

  LookupController vatController = new LookupController();
  LookupServerDataLocator vatDataLocator = new LookupServerDataLocator();
  NumericControl controlTrustAmount = new NumericControl();
  LabelControl labelTrustAmount = new LabelControl();

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

  public Contact2CustomerDetailFrame(Contact2CustomerController controller) {
    try {
      jbInit();
      setSize(750,450);
      setMinimumSize(new Dimension(750, 450));

      customerPanel.setFormController(controller);
      customerPanel.setSaveButton(saveButton);
      customerPanel.setFunctionId("SAL07");

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
      agentController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {}

        public void beforeLookupAction(ValueObject parentVO) {
          Subject subVO = (Subject)customerPanel.getVOModel().getValueObject();
          agentDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,subVO.getCompanyCodeSys01REG04());
          agentDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,subVO.getCompanyCodeSys01REG04());
        }

        public void forceValidate() {}

      });
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


      new CustomizedColumns(new BigDecimal(302),pricelistController);

      CustomizedControls customizedControls = new CustomizedControls(customerPanel,customerPanel,new BigDecimal(282));
    }
    catch(Exception e) {
      e.printStackTrace();
    }

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
          //creditDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanyCode.getValue());
          //creditDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanyCode.getValue());
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
          //itemsDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanyCode.getValue());
          //itemsDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanyCode.getValue());
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
          //actDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanyCode.getValue());
          //actDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanyCode.getValue());
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
          //chargesDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanyCode.getValue());
          //chargesDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanyCode.getValue());
        }

        public void forceValidate() {}

      });


  }


  private void jbInit() throws Exception {
    titledBorder1 = new TitledBorder("");
    titledBorder2 = new TitledBorder("");
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
    labelAgent.setText("agentCodeSAL10");
    controlAgentCod.setCanCopy(true);
//    controlAgentCod.setEnabledOnInsert(false);
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
    this.setTitle(ClientSettings.getInstance().getResources().getResource("contact to customer conversion"));
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    saveButton.setEnabled(false);
    saveButton.setText("saveButton1");

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

    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(saveButton, null);
    this.getContentPane().add(customerPanel, BorderLayout.CENTER);

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
    customerPanel.add(labelTrustAmount,           new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    customerPanel.add(controlTrustAmount,       new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    customerPanel.add(labelVatCode,          new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    customerPanel.add(controlVatCode,       new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    customerPanel.add(controlVatDescr,       new GridBagConstraints(2, 7, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    customerPanel.add(controlVatDeductible,      new GridBagConstraints(4, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    customerPanel.add(controlVatValue,     new GridBagConstraints(3, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
    customerPanel.add(labelCreditAccount,           new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    customerPanel.add(controlCreditsCode,               new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    customerPanel.add(controlCreditsDescr,            new GridBagConstraints(2, 8, 4, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    customerPanel.add(labelItemAccount,           new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    customerPanel.add(controlItemsCode,               new GridBagConstraints(1, 9, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    customerPanel.add(controlItemsDescr,            new GridBagConstraints(2, 9, 4, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    customerPanel.add(labelActivities,           new GridBagConstraints(0, 10, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    customerPanel.add(controlActCode,               new GridBagConstraints(1, 10, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    customerPanel.add(controlActDescr,            new GridBagConstraints(2, 10, 4, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    customerPanel.add(labelCharges,            new GridBagConstraints(0, 11, 1, 1, 0.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    customerPanel.add(controlChargesCode,                new GridBagConstraints(1, 11, 1, 1, 0.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    customerPanel.add(controlChargesDescr,             new GridBagConstraints(2, 11, 4, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
  }


  public void setSubjectType(String subjectTypeREG04) {
    if (subjectTypeREG04.equals(ApplicationConsts.SUBJECT_ORGANIZATION_CUSTOMER)) {
      customerPanel.setVOClassName("org.jallinone.sales.customers.java.OrganizationCustomerVO");
      customerPanel.getVOModel().setValueObject(new OrganizationCustomerVO());
    }
    else {
      customerPanel.setVOClassName("org.jallinone.sales.customers.java.PeopleCustomerVO");
      customerPanel.getVOModel().setValueObject(new PeopleCustomerVO());
    }
  }


  public Form getCurrentForm() {
    return customerPanel;
  }


  public LookupServerDataLocator getAgentDataLocator() {
    return agentDataLocator;
  }


  public CodLookupControl getControlCreditsCode() {
    return controlCreditsCode;
  }


  public CodLookupControl getControlItemsCode() {
    return controlItemsCode;
  }


  public CodLookupControl getControlActCode() {
    return controlActCode;
  }


  public CodLookupControl getControlChargesCode() {
    return controlChargesCode;
  }
}
