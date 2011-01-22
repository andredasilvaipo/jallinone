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
import org.jallinone.system.java.CompanyParametersVO;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.jallinone.commons.java.ApplicationConsts;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;
import org.openswing.swing.lookup.client.LookupListener;
import java.util.Collection;
import org.openswing.swing.util.java.Consts;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Frame used to define company parameters, based on the specified company code.</p>
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

public class CompanyParametersFrame extends InternalFrame {


  JPanel topPanel = new JPanel();
  JPanel buttonsPanel = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  EditButton editButton1 = new EditButton();
  SaveButton saveButton1 = new SaveButton();
  ReloadButton reloadButton1 = new ReloadButton();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  LabelControl labelCompanies = new LabelControl();
  ComboBoxControl controlCompanies = new ComboBoxControl();

  LookupController customerController = new LookupController();
  LookupServerDataLocator customerDataLocator = new LookupServerDataLocator();
  Form accountPanel = new Form();
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

  LookupController lossProfitEController = new LookupController();
  LookupServerDataLocator lossProfitEDataLocator = new LookupServerDataLocator();
  LookupController lossProfitPController = new LookupController();
  LookupServerDataLocator lossProfitPDataLocator = new LookupServerDataLocator();
  LookupController closingController = new LookupController();
  LookupServerDataLocator closingDataLocator = new LookupServerDataLocator();
  LookupController openingController = new LookupController();
  LookupServerDataLocator openingDataLocator = new LookupServerDataLocator();

  LabelControl labelLossProfitE = new LabelControl();
  CodLookupControl controlLossProfitECode = new CodLookupControl();
  TextControl controlLossProfitEDescr = new TextControl();
  LabelControl labelLossProfitP = new LabelControl();
  CodLookupControl controlLossProfitPCode = new CodLookupControl();
  TextControl controlLossProfitPDescr = new TextControl();
  LabelControl labelClosing = new LabelControl();
  CodLookupControl controlClosingCode = new CodLookupControl();
  TextControl controlClosingDescr = new TextControl();
  LabelControl labelOpening = new LabelControl();
  CodLookupControl controlOpeningCode = new CodLookupControl();
  TextControl controlOpeningDescr = new TextControl();

  JTabbedPane tabbed = new JTabbedPane();
  JPanel panel1 = new JPanel();
  JPanel panel2 = new JPanel();
  GridBagLayout gridBagLayout5 = new GridBagLayout();
  GridBagLayout gridBagLayout6 = new GridBagLayout();
  BorderLayout borderLayout3 = new BorderLayout();
  LabelControl labelAfternoonStartHour = new LabelControl();
  DateControl controlAfternoonStartHour = new DateControl();
  LabelControl labelAfternoonEndHour = new LabelControl();
  DateControl controlAfternoonEndHour = new DateControl();
  LabelControl labelMorningStartHour = new LabelControl();
  DateControl controlMorningStartHour = new DateControl();
  LabelControl labelMorningEndHour = new LabelControl();
  DateControl controlMorningEndHour = new DateControl();
  LabelControl labelSaleSectional = new LabelControl();
  TextControl controlSaleSectional = new TextControl();
  LabelControl labelInitialVal = new LabelControl();
  NumericControl controlInitialVal = new NumericControl();

	LabelControl labelRoundingCostsCode = new LabelControl();
	LabelControl labelRoundingProAccCode = new LabelControl();
	CodLookupControl controlRoundingC = new CodLookupControl();
	CodLookupControl controlRoundingP = new CodLookupControl();
	TextControl controlRoundingCdescr = new TextControl();
	TextControl controlRoundingPdescr = new TextControl();
	LookupController accCController = new LookupController();
	LookupServerDataLocator accCDataLocator = new LookupServerDataLocator();
	LookupController accPController = new LookupController();
	LookupServerDataLocator accPDataLocator = new LookupServerDataLocator();


  public CompanyParametersFrame(CompanyParametersController controller) {
    try {
      jbInit();

      accountPanel.setFormController(controller);


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
      caseController.addLookup2ParentLink("accountCodeACC02", "caseAccountCodeAcc02DOC21");
      caseController.addLookup2ParentLink("descriptionSYS10","caseAccountDescrDOC21");
      caseController.setFramePreferedSize(new Dimension(400,400));
      caseController.setAllColumnVisible(false);
      caseController.setVisibleColumn("accountCodeACC02", true);
      caseController.setVisibleColumn("descriptionSYS10", true);
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
      bankController.addLookup2ParentLink("accountCodeACC02", "bankAccountCodeAcc02DOC21");
      bankController.addLookup2ParentLink("descriptionSYS10","bankAccountDescrDOC21");
      bankController.setFramePreferedSize(new Dimension(400,400));
      bankController.setAllColumnVisible(false);
      bankController.setVisibleColumn("accountCodeACC02", true);
      bankController.setVisibleColumn("descriptionSYS10", true);
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
      vatEndController.addLookup2ParentLink("accountCodeACC02", "vatEndAccountCodeAcc02DOC21");
      vatEndController.addLookup2ParentLink("descriptionSYS10","vatEndAccountDescrDOC21");
      vatEndController.setFramePreferedSize(new Dimension(400,400));
      vatEndController.setAllColumnVisible(false);
      vatEndController.setVisibleColumn("accountCodeACC02", true);
      vatEndController.setVisibleColumn("descriptionSYS10", true);
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

      /*
          private String lossProfitEAccountCodeAcc02DOC21;
          private String lossProfitEAccountDescrSAL07;
          private String lossProfitPAccountCodeAcc02DOC21;
          private String lossProfitPAccountDescrSAL07;
          private String closingAccountCodeAcc02DOC21;
          private String closingAccountDescrSAL07;
          private String openingAccountCodeAcc02DOC21;
          private String openingAccountDescrSAL07;

      */


     // lossProfit econ. account code lookup...
     controlLossProfitECode.setLookupController(lossProfitEController);
     controlLossProfitECode.setControllerMethodName("getAccounts");
     lossProfitEController.setLookupDataLocator(lossProfitEDataLocator);
     lossProfitEDataLocator.setGridMethodName("loadAccounts");
     lossProfitEDataLocator.setValidationMethodName("validateAccountCode");
     lossProfitEController.setFrameTitle("accounts");
     lossProfitEController.setLookupValueObjectClassName("org.jallinone.accounting.accounts.java.AccountVO");
     lossProfitEController.addLookup2ParentLink("accountCodeACC02", "lossProfitEAccountCodeAcc02DOC21");
     lossProfitEController.addLookup2ParentLink("descriptionSYS10","lossProfitEAccountDescrDOC21");
     lossProfitEController.setFramePreferedSize(new Dimension(400,400));
     lossProfitEController.setAllColumnVisible(false);
     lossProfitEController.setVisibleColumn("accountCodeACC02", true);
     lossProfitEController.setVisibleColumn("descriptionSYS10", true);
     lossProfitEController.setPreferredWidthColumn("accountCodeACC02",100);
     lossProfitEController.setPreferredWidthColumn("descriptionSYS10",290);
     lossProfitEController.addLookupListener(new LookupListener() {

       public void codeValidated(boolean validated) {}

       public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) { }

       public void beforeLookupAction(ValueObject parentVO) {
         lossProfitEDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanies.getValue());
         lossProfitEDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanies.getValue());
       }

       public void forceValidate() {}

     });


     // lossProfit patrim. account code lookup...
     controlLossProfitPCode.setLookupController(lossProfitPController);
     controlLossProfitPCode.setControllerMethodName("getAccounts");
     lossProfitPController.setLookupDataLocator(lossProfitPDataLocator);
     lossProfitPDataLocator.setGridMethodName("loadAccounts");
     lossProfitPDataLocator.setValidationMethodName("validateAccountCode");
     lossProfitPController.setFrameTitle("accounts");
     lossProfitPController.setLookupValueObjectClassName("org.jallinone.accounting.accounts.java.AccountVO");
     lossProfitPController.addLookup2ParentLink("accountCodeACC02", "lossProfitPAccountCodeAcc02DOC21");
     lossProfitPController.addLookup2ParentLink("descriptionSYS10","lossProfitPAccountDescrDOC21");
     lossProfitPController.setFramePreferedSize(new Dimension(400,400));
     lossProfitPController.setAllColumnVisible(false);
     lossProfitPController.setVisibleColumn("accountCodeACC02", true);
     lossProfitPController.setVisibleColumn("descriptionSYS10", true);
     lossProfitPController.setPreferredWidthColumn("accountCodeACC02",100);
     lossProfitPController.setPreferredWidthColumn("descriptionSYS10",290);
     lossProfitPController.addLookupListener(new LookupListener() {

       public void codeValidated(boolean validated) {}

       public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) { }

       public void beforeLookupAction(ValueObject parentVO) {
         lossProfitPDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanies.getValue());
         lossProfitPDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanies.getValue());
       }

       public void forceValidate() {}

     });


     // closing account code lookup...
     controlClosingCode.setLookupController(closingController);
     controlClosingCode.setControllerMethodName("getAccounts");
     closingController.setLookupDataLocator(closingDataLocator);
     closingDataLocator.setGridMethodName("loadAccounts");
     closingDataLocator.setValidationMethodName("validateAccountCode");
     closingController.setFrameTitle("accounts");
     closingController.setLookupValueObjectClassName("org.jallinone.accounting.accounts.java.AccountVO");
     closingController.addLookup2ParentLink("accountCodeACC02", "closingAccountCodeAcc02DOC21");
     closingController.addLookup2ParentLink("descriptionSYS10","closingAccountDescrDOC21");
     closingController.setFramePreferedSize(new Dimension(400,400));
     closingController.setAllColumnVisible(false);
     closingController.setVisibleColumn("accountCodeACC02", true);
     closingController.setVisibleColumn("descriptionSYS10", true);
     closingController.setPreferredWidthColumn("accountCodeACC02",100);
     closingController.setPreferredWidthColumn("descriptionSYS10",290);
     closingController.addLookupListener(new LookupListener() {

       public void codeValidated(boolean validated) {}

       public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) { }

       public void beforeLookupAction(ValueObject parentVO) {
         closingDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanies.getValue());
         closingDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanies.getValue());
       }

       public void forceValidate() {}

     });


     // opening account code lookup...
     controlOpeningCode.setLookupController(openingController);
     controlOpeningCode.setControllerMethodName("getAccounts");
     openingController.setLookupDataLocator(openingDataLocator);
     openingDataLocator.setGridMethodName("loadAccounts");
     openingDataLocator.setValidationMethodName("validateAccountCode");
     openingController.setFrameTitle("accounts");
     openingController.setLookupValueObjectClassName("org.jallinone.accounting.accounts.java.AccountVO");
     openingController.addLookup2ParentLink("accountCodeACC02", "openingAccountCodeAcc02DOC21");
     openingController.addLookup2ParentLink("descriptionSYS10","openingAccountDescrDOC21");
     openingController.setFramePreferedSize(new Dimension(400,400));
     openingController.setAllColumnVisible(false);
     openingController.setVisibleColumn("accountCodeACC02", true);
     openingController.setVisibleColumn("descriptionSYS10", true);
     openingController.setPreferredWidthColumn("accountCodeACC02",100);
     openingController.setPreferredWidthColumn("descriptionSYS10",290);
     openingController.addLookupListener(new LookupListener() {

       public void codeValidated(boolean validated) {}

       public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) { }

       public void beforeLookupAction(ValueObject parentVO) {
         openingDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanies.getValue());
         openingDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanies.getValue());
       }

       public void forceValidate() {}

     });


		 // rounding costs account lookup...
		 accCDataLocator.setGridMethodName("loadAccounts");
		 accCDataLocator.setValidationMethodName("validateAccountCode");

		 controlRoundingC.setLookupController(accCController);
		 controlRoundingC.setControllerMethodName("getAccounts");
		 accCController.setLookupDataLocator(accCDataLocator);
		 accCController.setFrameTitle("accounts");
		 accCController.setLookupValueObjectClassName("org.jallinone.accounting.accounts.java.AccountVO");
		 accCController.addLookup2ParentLink("accountCodeACC02", "roundingCostsAccountCodeAcc02DOC19");
		 accCController.addLookup2ParentLink("descriptionSYS10","roundingCostsDescrDOC19");
		 accCController.setAllColumnVisible(false);
		 accCController.setVisibleColumn("accountCodeACC02", true);
		 accCController.setVisibleColumn("descriptionSYS10", true);
		 accCController.setPreferredWidthColumn("descriptionSYS10", 200);
		 accCController.setFramePreferedSize(new Dimension(340,400));
		 accCController.setSortedColumn("accountCodeACC02","ASC",1);
		 accCController.setFilterableColumn("accountCodeACC02",true);
		 accCController.setFilterableColumn("descriptionSYS10",true);
		 accCController.addLookupListener(new LookupListener() {

				public void codeValidated(boolean validated) {}

				public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) { }

				public void beforeLookupAction(ValueObject parentVO) {
					accCDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanies.getValue());
					accCDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanies.getValue());
				}

				public void forceValidate() {}

			});


		 // rounding proceeds account lookup...
		 accPDataLocator.setGridMethodName("loadAccounts");
		 accPDataLocator.setValidationMethodName("validateAccountCode");

		 controlRoundingP.setLookupController(accPController);
		 controlRoundingP.setControllerMethodName("getAccounts");
		 accPController.setLookupDataLocator(accPDataLocator);
		 accPController.setFrameTitle("accounts");
		 accPController.setLookupValueObjectClassName("org.jallinone.accounting.accounts.java.AccountVO");
		 accPController.addLookup2ParentLink("accountCodeACC02", "roundingProceedsAccountCodeAcc02DOC19");
		 accPController.addLookup2ParentLink("descriptionSYS10","roundingProceedsDescrDOC19");
		 accPController.setAllColumnVisible(false);
		 accPController.setVisibleColumn("accountCodeACC02", true);
		 accPController.setVisibleColumn("descriptionSYS10", true);
		 accPController.setPreferredWidthColumn("descriptionSYS10", 200);
		 accPController.setFramePreferedSize(new Dimension(340,400));
		 accPController.setSortedColumn("accountCodeACC02","ASC",1);
		 accPController.setFilterableColumn("accountCodeACC02",true);
		 accPController.setFilterableColumn("descriptionSYS10",true);
		 accPController.addLookupListener(new LookupListener() {

				 public void codeValidated(boolean validated) {}

				 public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) { }

				 public void beforeLookupAction(ValueObject parentVO) {
					 accPDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanies.getValue());
					 accPDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompanies.getValue());
				 }

				 public void forceValidate() {}

			});


      init();

      setSize(650,520);
      setMinimumSize(new Dimension(650,520));
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
          CompanyParametersVO vo = (CompanyParametersVO)accountPanel.getVOModel().getValueObject();

          int selIndex = ((JComboBox)e.getSource()).getSelectedIndex();
          String companyCode = (String)d.getDomainPairList()[selIndex].getCode();
          accountPanel.executeReload();
        }
      }
    });

  }



  private void jbInit() throws Exception {
		labelRoundingCostsCode.setText("rounding costs account code");
		labelRoundingProAccCode.setText("rounding proceeds account code");
		controlRoundingC.setAttributeName("roundingCostsAccountCodeAcc02DOC19");
    controlRoundingC.setLinkLabel(labelRoundingCostsCode);
    controlRoundingC.setMaxCharacters(20);
    controlRoundingC.setRequired(true);
		controlRoundingP.setAttributeName("roundingProceedsAccountCodeAcc02DOC19");
    controlRoundingP.setLinkLabel(labelRoundingProAccCode);
    controlRoundingP.setMaxCharacters(20);
    controlRoundingP.setRequired(true);
		controlRoundingCdescr.setAttributeName("roundingCostsDescrDOC19");
    controlRoundingCdescr.setEnabled(false);
		controlRoundingCdescr.setEnabledOnInsert(false);
		controlRoundingCdescr.setEnabledOnEdit(false);
		controlRoundingPdescr.setAttributeName("roundingProceedsDescrDOC19");
    controlRoundingPdescr.setEnabled(false);
		controlRoundingPdescr.setEnabledOnInsert(false);
		controlRoundingPdescr.setEnabledOnEdit(false);

    panel1.setLayout(gridBagLayout5);
    panel2.setLayout(gridBagLayout6);
    labelAfternoonStartHour.setText("afternoonStartHourSCH02");
    labelAfternoonEndHour.setText("afternoonEndHourSCH02");
    labelMorningStartHour.setText("morningStartHourSCH02");
    labelMorningEndHour.setText("morningEndHourSCH02");
    controlMorningStartHour.setRequired(true);
    controlMorningEndHour.setRequired(true);
    controlAfternoonEndHour.setRequired(true);
    controlAfternoonStartHour.setRequired(true);
    controlMorningStartHour.setAttributeName("afternoonStartHourSCH02");
    controlMorningEndHour.setAttributeName("afternoonEndHourSCH02");
    controlAfternoonEndHour.setAttributeName("morningEndHourSCH02");
    controlAfternoonStartHour.setAttributeName("morningStartHourSCH02");
    controlMorningStartHour.setDateType(Consts.TYPE_TIME);
    controlMorningEndHour.setDateType(Consts.TYPE_TIME);
    controlAfternoonEndHour.setDateType(Consts.TYPE_TIME);
    controlAfternoonStartHour.setDateType(Consts.TYPE_TIME);

    labelSaleSectional.setText("sectionalDOC01");
    controlSaleSectional.setAttributeName("saleSectionalDOC01");
    controlSaleSectional.setColumns(20);
    controlSaleSectional.setLinkLabel(labelSaleSectional);
    controlSaleSectional.setMaxCharacters(20);
    labelInitialVal.setTextAlignment(SwingConstants.LEFT);
    labelInitialVal.setText("initial value in progr");
    controlInitialVal.setRequired(true);
    tabbed.add(panel1, "panel1");
    tabbed.add(panel2, "panel2");

    tabbed.setTitleAt(0,ClientSettings.getInstance().getResources().getResource("user level parameters"));
    tabbed.setTitleAt(1,ClientSettings.getInstance().getResources().getResource("company level parameters"));

    accountPanel.setLayout(borderLayout3);
    accountPanel.add(tabbed,BorderLayout.CENTER);

    titledBorder1 = new TitledBorder("");
    titledBorder2 = new TitledBorder("");
    titledBorder3 = new TitledBorder("");
    titledBorder4 = new TitledBorder("");

    accountPanel.setVOClassName("org.jallinone.system.java.CompanyParametersVO");
    accountPanel.setFunctionId("SYS21");

    this.setTitle(ClientSettings.getInstance().getResources().getResource("company parameters"));
    this.getContentPane().setLayout(gridBagLayout3);
    topPanel.setLayout(gridBagLayout2);
    topPanel.setBorder(titledBorder1);
    accountPanel.setBorder(titledBorder3);

    accountPanel.setEditButton(editButton1);
    accountPanel.setReloadButton(reloadButton1);
    accountPanel.setSaveButton(saveButton1);

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
    labelLossProfitE.setText("loss/profit econ. endorse account");
    labelLossProfitP.setText("loss/profit patrim. endorse account");
    labelClosing.setText("accounts closing account");
    labelOpening.setText("accounts opening account");
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
    controlCostsCode.setRequired(true);
    controlCaseDescr.setEnabledOnInsert(false);
    controlCaseDescr.setEnabledOnEdit(false);
    controlBankDescr.setEnabledOnInsert(false);
    controlBankDescr.setEnabledOnEdit(false);
    controlVatEndCode.setLinkLabel(labelVatEnd);
    controlVatEndCode.setMaxCharacters(20);
    controlVatEndDescr.setEnabledOnInsert(false);
    controlVatEndDescr.setEnabledOnEdit(false);
    controlVatEndCode.setRequired(true);

    controlLossProfitECode.setLinkLabel(labelLossProfitE);
    controlLossProfitECode.setMaxCharacters(20);
    controlLossProfitEDescr.setEnabledOnInsert(false);
    controlLossProfitEDescr.setEnabledOnEdit(false);
    controlLossProfitECode.setRequired(true);

    controlLossProfitPCode.setLinkLabel(labelLossProfitP);
    controlLossProfitPCode.setMaxCharacters(20);
    controlLossProfitPDescr.setEnabledOnInsert(false);
    controlLossProfitPDescr.setEnabledOnEdit(false);
    controlLossProfitPCode.setRequired(true);

    controlClosingCode.setLinkLabel(labelClosing);
    controlClosingCode.setMaxCharacters(20);
    controlClosingDescr.setEnabledOnInsert(false);
    controlClosingDescr.setEnabledOnEdit(false);
    controlClosingCode.setRequired(true);

    controlOpeningCode.setLinkLabel(labelOpening);
    controlOpeningCode.setMaxCharacters(20);
    controlOpeningDescr.setEnabledOnInsert(false);
    controlOpeningDescr.setEnabledOnEdit(false);
    controlOpeningCode.setRequired(true);

    topPanel.add(buttonsPanel,      new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

    controlCaseCode.setRequired(true);
    controlBankCode.setRequired(true);

    controlInitialVal.setAttributeName("initialValueSYS21");

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



    this.getContentPane().add(accountPanel,    new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    panel1.add(labelCreditAccount,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    panel1.add(labelItemAccount,   new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    panel1.add(labelActivities,   new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    panel1.add(labelCharges,   new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    panel1.add(labelDebit,   new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    panel1.add(labelPurchase,    new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    panel1.add(labelCase,    new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    panel1.add(labelBank,    new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    panel1.add(labelVatEnd,    new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    panel1.add(controlCreditsCode,    new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    panel1.add(controlCreditsDescr,  new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    panel1.add(controlItemsCode,  new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    panel1.add(controlItemsDescr,  new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    panel1.add(controlActCode,  new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    panel1.add(controlActDescr,  new GridBagConstraints(2, 2, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    panel1.add(controlChargesCode,  new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    panel1.add(controlChargesDescr,  new GridBagConstraints(2, 3, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    panel1.add(controlDebitsCode,  new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    panel1.add(controlDebitsDescr,  new GridBagConstraints(2, 4, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    panel1.add(controlCostsCode,  new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    panel1.add(controlCostsDescr,  new GridBagConstraints(2, 5, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    panel1.add(controlCaseCode,  new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    panel1.add(controlCaseDescr,  new GridBagConstraints(2, 6, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

    panel1.add(controlBankCode,  new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    panel1.add(controlBankDescr,  new GridBagConstraints(2, 7, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    panel1.add(controlVatEndCode,  new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    panel1.add(controlVatEndDescr,  new GridBagConstraints(2, 8, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));


    panel2.add(labelLossProfitE,                 new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    panel2.add(controlLossProfitECode,               new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    panel2.add(controlLossProfitEDescr,                new GridBagConstraints(2, 0, 2, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

    panel2.add(labelLossProfitP,                 new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    panel2.add(controlLossProfitPCode,               new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    panel2.add(controlLossProfitPDescr,                new GridBagConstraints(2, 1, 2, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

    panel2.add(labelClosing,                 new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    panel2.add(controlClosingCode,               new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    panel2.add(controlClosingDescr,                new GridBagConstraints(2, 2, 2, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

    panel2.add(labelOpening,                    new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    panel2.add(controlOpeningCode,                new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    panel2.add(controlOpeningDescr,               new GridBagConstraints(2, 3, 2, 5, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0, 0));



		panel2.add(controlRoundingP,       new GridBagConstraints(1, 5, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		panel2.add(controlRoundingCdescr,     new GridBagConstraints(2, 4, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		panel2.add(controlRoundingPdescr,        new GridBagConstraints(2, 5, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		panel2.add(labelRoundingCostsCode,     new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
							,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		panel2.add(labelRoundingProAccCode,       new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		panel2.add(controlRoundingC,     new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
							,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));



    panel2.add(labelAfternoonStartHour,                 new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
    panel2.add(controlAfternoonStartHour,               new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    panel2.add(controlAfternoonEndHour,             new GridBagConstraints(3, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    panel2.add(labelMorningStartHour,           new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    panel2.add(controlMorningEndHour,         new GridBagConstraints(3, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    panel2.add(controlMorningStartHour,      new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    panel2.add(labelMorningEndHour,       new GridBagConstraints(2, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 9, 5), 0, 0));
    panel2.add(labelAfternoonEndHour,     new GridBagConstraints(2, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    panel2.add(controlSaleSectional,     new GridBagConstraints(1, 8, 2, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    panel2.add(labelSaleSectional,   new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    panel2.add(labelInitialVal,   new GridBagConstraints(0, 9, 1, 1, 0.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    panel2.add(controlInitialVal,  new GridBagConstraints(1, 9, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

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
    controlCaseCode.setAttributeName("caseAccountCodeAcc02DOC21");
    controlCaseDescr.setAttributeName("caseAccountDescrDOC21");
    controlBankCode.setAttributeName("bankAccountCodeAcc02DOC21");
    controlBankDescr.setAttributeName("bankAccountDescrDOC21");
    controlVatEndCode.setAttributeName("vatEndorseAccountCodeAcc02DOC21");
    controlVatEndDescr.setAttributeName("vatEndorseAccountDescrDOC21");

    controlLossProfitECode.setAttributeName("lossProfitEAccountCodeAcc02DOC21");
    controlLossProfitEDescr.setAttributeName("lossProfitEAccountDescrDOC21");
    controlLossProfitPCode.setAttributeName("lossProfitPAccountCodeAcc02DOC21");
    controlLossProfitPDescr.setAttributeName("lossProfitPAccountDescrDOC21");
    controlClosingCode.setAttributeName("closingAccountCodeAcc02DOC21");
    controlClosingDescr.setAttributeName("closingAccountDescrDOC21");
    controlOpeningCode.setAttributeName("openingAccountCodeAcc02DOC21");
    controlOpeningDescr.setAttributeName("openingAccountDescrDOC21");

  }


  public ComboBoxControl getControlCompanies() {
    return controlCompanies;
  }


  public Form getAccountPanel() {
    return accountPanel;
  }


}
