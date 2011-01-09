package org.jallinone.scheduler.callouts.client;

import org.openswing.swing.mdi.client.InternalFrame;
import javax.swing.*;
import java.awt.*;
import org.openswing.swing.client.*;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.form.client.Form;
import java.math.BigDecimal;
import org.jallinone.commons.client.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.domains.java.*;
import org.jallinone.scheduler.callouts.java.*;
import java.util.ArrayList;
import org.openswing.swing.lookup.client.*;
import org.openswing.swing.tree.client.*;
import java.util.Collection;
import org.jallinone.registers.vat.java.VatVO;
import java.awt.event.*;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.*;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.table.java.ServerGridDataLocator;
import org.openswing.swing.table.columns.client.*;
import org.jallinone.warehouse.availability.client.*;
import org.jallinone.items.java.ItemTypeVO;
import org.jallinone.subjects.client.*;
import org.jallinone.subjects.java.PeopleVO;
import org.jallinone.subjects.java.OrganizationVO;
import org.openswing.swing.mdi.client.MDIFrame;
import org.jallinone.system.java.ButtonCompanyAuthorizations;
import org.jallinone.scheduler.activities.client.*;
import java.util.HashSet;
import org.jallinone.sales.customers.java.CustomerPK;
import org.jallinone.sales.customers.java.OrganizationCustomerVO;
import org.jallinone.sales.customers.java.PeopleCustomerVO;
import org.jallinone.sales.pricelist.java.PricelistVO;
import org.jallinone.registers.payments.java.PaymentVO;
import org.jallinone.sales.documents.java.DetailSaleDocVO;
import org.jallinone.scheduler.activities.java.ScheduledActivityVO;
import org.jallinone.accounting.accounts.java.AccountVO;
import org.openswing.swing.table.model.client.VOListTableModel;
import org.jallinone.scheduler.activities.java.ScheduledEmployeeVO;
import org.jallinone.registers.task.java.TaskVO;
import java.util.HashMap;
import org.openswing.swing.message.send.java.LookupValidationParams;
import org.jallinone.warehouse.java.WarehouseVO;
import org.jallinone.sales.documents.invoices.client.SaleInvoiceDocController;
import org.jallinone.sales.documents.java.SaleDocPK;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Dialog used to create an invoice from a call-out request having a scheduled activity linked to it.
* This window allows to define pricelist code and customed code if they are not yet defined.</p>
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
public class CreateInvoiceDialog extends JDialog {

  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel inputPanel = new JPanel();
  JPanel buttonsPanel = new JPanel();
  JButton createButton = new JButton();
  JButton annullButton = new JButton();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  LabelControl labelCustCode = new LabelControl();
  LabelControl labelPricelist = new LabelControl();
  TextControl controlCustCode = new TextControl();
  CodLookupControl controlPricelistCode = new CodLookupControl();
  TextControl controlPricelistDescr = new TextControl();
  LookupController pricelistController = new LookupController();
  LookupServerDataLocator pricelistDataLocator = new LookupServerDataLocator();
  private CallOutRequestFrame frame = null;
  LabelControl labelCurrencyCode = new LabelControl();
  TextControl controlCurrencyCode = new TextControl();
  LabelControl labelPayment = new LabelControl();
  CodLookupControl controlPayCode = new CodLookupControl();
  TextControl controlPayDescr = new TextControl();
  LookupController payController = new LookupController();
  LookupServerDataLocator payDataLocator = new LookupServerDataLocator();

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

  LabelControl labelWarehouseCode = new LabelControl();
  CodLookupControl controlWarehouseCode = new CodLookupControl();
  TextControl controlWareDescr = new TextControl();
  LookupController wareController = new LookupController();
  LookupServerDataLocator wareDataLocator = new LookupServerDataLocator();


  public CreateInvoiceDialog(CallOutRequestFrame frame) {
    super(MDIFrame.getInstance(), ClientSettings.getInstance().getResources().getResource("invoice creation"), true);
    this.frame = frame;
    try {
      jbInit();


      // warehouse lookup...
      wareDataLocator.setGridMethodName("loadWarehouses");
      wareDataLocator.setValidationMethodName("validateWarehouseCode");

      controlWarehouseCode.setLookupController(wareController);
      controlWarehouseCode.setControllerMethodName("getWarehousesList");
      wareController.setLookupDataLocator(wareDataLocator);
      wareController.setFrameTitle("warehouses");
      wareController.setLookupValueObjectClassName("org.jallinone.warehouse.java.WarehouseVO");
//      wareController.addLookup2ParentLink("warehouseCodeWAR01", "warehouseCodeWar01DOC01");
//      wareController.addLookup2ParentLink("descriptionWAR01","descriptionWar01DOC01");
      wareController.setAllColumnVisible(false);
      wareController.setVisibleColumn("warehouseCodeWAR01", true);
      wareController.setVisibleColumn("descriptionWAR01", true);
      wareController.setVisibleColumn("addressWAR01", true);
      wareController.setVisibleColumn("cityWAR01", true);
      wareController.setVisibleColumn("zipWAR01", true);
      wareController.setVisibleColumn("provinceWAR01", true);
      wareController.setVisibleColumn("countryWAR01", true);
      wareController.setPreferredWidthColumn("descriptionWAR01",200);
      wareController.setFramePreferedSize(new Dimension(750,500));
      wareController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          WarehouseVO vo = (WarehouseVO)wareController.getLookupVO();
          controlWarehouseCode.setValue(vo.getWarehouseCodeWAR01());
          controlWareDescr.setValue(vo.getDescriptionWAR01());
        }

        public void beforeLookupAction(ValueObject parentVO) {}

        public void forceValidate() {}

      });


      // payments lookup...
      payDataLocator.setGridMethodName("loadPayments");
      payDataLocator.setValidationMethodName("validatePaymentCode");
      controlPayCode.setLookupController(payController);
      controlPayCode.setControllerMethodName("getPaymentsList");
      payController.setLookupDataLocator(payDataLocator);
      payController.setFrameTitle("payments");
      payController.setLookupValueObjectClassName("org.jallinone.registers.payments.java.PaymentVO");
//      payController.addLookup2ParentLink("paymentCodeREG10", "paymentCodeReg10SAL07");
//      payController.addLookup2ParentLink("descriptionSYS10","paymentDescriptionSYS10");
      payController.setAllColumnVisible(false);
      payController.setVisibleColumn("paymentCodeREG10", true);
      payController.setVisibleColumn("descriptionSYS10", true);
      payController.setPreferredWidthColumn("descriptionSYS10",200);
      new CustomizedColumns(new BigDecimal(212),payController);
      payController.addLookupListener(new LookupListener() {

         public void codeValidated(boolean validated) {}

         public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
           PaymentVO vo = (PaymentVO)payController.getLookupVO();
           controlPayCode.setValue(vo.getPaymentCodeREG10());
           controlPayDescr.setValue(vo.getDescriptionSYS10());
         }

         public void beforeLookupAction(ValueObject parentVO) {
         }

         public void forceValidate() {}

       });


      // pricelist lookup...
      pricelistDataLocator.setGridMethodName("loadPricelists");
      pricelistDataLocator.setValidationMethodName("validatePricelistCode");
      controlPricelistCode.setLookupController(pricelistController);
      controlPricelistCode.setControllerMethodName("getSalePricesList");
      pricelistController.setLookupDataLocator(pricelistDataLocator);
//      pricelistController.setForm(form);
      pricelistController.setFrameTitle("pricelists");
      pricelistController.setLookupValueObjectClassName("org.jallinone.sales.pricelist.java.PricelistVO");
//      pricelistController.addLookup2ParentLink("pricelistCodeSAL01","pricelistCodeSal01DOC01");
//      pricelistController.addLookup2ParentLink("descriptionSYS10", "pricelistDescriptionDOC01");
//      pricelistController.addLookup2ParentLink("currencyCodeReg03SAL01","currencyCodeReg03DOC01");
      pricelistController.setAllColumnVisible(false);
      pricelistController.setVisibleColumn("pricelistCodeSAL01", true);
      pricelistController.setVisibleColumn("descriptionSYS10", true);
      pricelistController.setVisibleColumn("currencyCodeReg03SAL01", true);
      pricelistController.setPreferredWidthColumn("descriptionSYS10", 250);
      pricelistController.setFramePreferedSize(new Dimension(420,500));
      pricelistController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          PricelistVO vo = (PricelistVO)pricelistController.getLookupVO();
          controlPricelistCode.setValue(vo.getPricelistCodeSAL01());
          controlPricelistDescr.setValue(vo.getDescriptionSYS10());
          controlCurrencyCode.setValue(vo.getCurrencyCodeReg03SAL01());
        }

        public void beforeLookupAction(ValueObject parentVO) {
        }

        public void forceValidate() {}

      });


      // initialize pricelist lookup parameters...
      DetailCallOutRequestVO vo = (DetailCallOutRequestVO)frame.getCalloutPanel().getVOModel().getValueObject();
      pricelistDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01SCH03());
      pricelistDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01SCH03());
      wareDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01SCH03());
      wareDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01SCH03());


      // credit account code lookup...
      creditDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01SCH03());
      creditDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01SCH03());
      controlCreditsCode.setLookupController(creditController);
      controlCreditsCode.setControllerMethodName("getAccounts");
      creditController.setLookupDataLocator(creditDataLocator);
      creditDataLocator.setGridMethodName("loadAccounts");
      creditDataLocator.setValidationMethodName("validateAccountCode");
      creditController.setFrameTitle("accounts");
      creditController.setLookupValueObjectClassName("org.jallinone.accounting.accounts.java.AccountVO");
//      creditController.addLookup2ParentLink("accountCodeACC02", "creditAccountCodeAcc02SAL07");
//      creditController.addLookup2ParentLink("descriptionSYS10","creditAccountDescrSAL07");
      creditController.setFramePreferedSize(new Dimension(400,400));
      creditController.setAllColumnVisible(false);
      creditController.setVisibleColumn("accountCodeACC02", true);
      creditController.setVisibleColumn("descriptionSYS10", true);
      creditController.setPreferredWidthColumn("accountCodeACC02", 100);
      creditController.setPreferredWidthColumn("descriptionSYS10", 290);
      creditController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          AccountVO vo = (AccountVO)creditController.getLookupVO();
          controlCreditsCode.setValue(vo.getAccountCodeACC02());
          controlCreditsDescr.setValue(vo.getDescriptionSYS10());
        }

        public void beforeLookupAction(ValueObject parentVO) {
        }

        public void forceValidate() {}

      });


      // items account code lookup...
      itemsDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01SCH03());
      itemsDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01SCH03());
      controlItemsCode.setLookupController(itemsController);
      controlItemsCode.setControllerMethodName("getAccounts");
      itemsController.setLookupDataLocator(itemsDataLocator);
      itemsDataLocator.setGridMethodName("loadAccounts");
      itemsDataLocator.setValidationMethodName("validateAccountCode");
      itemsController.setFrameTitle("accounts");
      itemsController.setLookupValueObjectClassName("org.jallinone.accounting.accounts.java.AccountVO");
//      itemsController.addLookup2ParentLink("accountCodeACC02", "itemsAccountCodeAcc02SAL07");
//      itemsController.addLookup2ParentLink("descriptionSYS10","itemsAccountDescrSAL07");
      itemsController.setFramePreferedSize(new Dimension(400,400));
      itemsController.setAllColumnVisible(false);
      itemsController.setVisibleColumn("accountCodeACC02", true);
      itemsController.setVisibleColumn("descriptionSYS10", true);
      itemsController.setPreferredWidthColumn("accountCodeACC02", 100);
      itemsController.setPreferredWidthColumn("descriptionSYS10", 290);
      itemsController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          AccountVO vo = (AccountVO)itemsController.getLookupVO();
          controlItemsCode.setValue(vo.getAccountCodeACC02());
          controlItemsDescr.setValue(vo.getDescriptionSYS10());
        }

        public void beforeLookupAction(ValueObject parentVO) { }

        public void forceValidate() {}

      });


      // activities account code lookup...
      actDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01SCH03());
      actDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01SCH03());
      controlActCode.setLookupController(actController);
      controlActCode.setControllerMethodName("getAccounts");
      actController.setLookupDataLocator(actDataLocator);
      actDataLocator.setGridMethodName("loadAccounts");
      actDataLocator.setValidationMethodName("validateAccountCode");
      actController.setFrameTitle("accounts");
      actController.setLookupValueObjectClassName("org.jallinone.accounting.accounts.java.AccountVO");
//      actController.addLookup2ParentLink("accountCodeACC02", "activitiesAccountCodeAcc02SAL07");
//      actController.addLookup2ParentLink("descriptionSYS10","activitiesAccountDescrSAL07");
      actController.setFramePreferedSize(new Dimension(400,400));
      actController.setAllColumnVisible(false);
      actController.setVisibleColumn("accountCodeACC02", true);
      actController.setVisibleColumn("descriptionSYS10", true);
      actController.setPreferredWidthColumn("accountCodeACC02", 100);
      actController.setPreferredWidthColumn("descriptionSYS10", 290);
      actController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          AccountVO vo = (AccountVO)actController.getLookupVO();
          controlActCode.setValue(vo.getAccountCodeACC02());
          controlActDescr.setValue(vo.getDescriptionSYS10());
        }

        public void beforeLookupAction(ValueObject parentVO) { }

        public void forceValidate() {}

      });


      // charges account code lookup...
      chargesDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01SCH03());
      chargesDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01SCH03());
      controlChargesCode.setLookupController(chargesController);
      controlChargesCode.setControllerMethodName("getAccounts");
      chargesController.setLookupDataLocator(chargesDataLocator);
      chargesDataLocator.setGridMethodName("loadAccounts");
      chargesDataLocator.setValidationMethodName("validateAccountCode");
      chargesController.setFrameTitle("accounts");
      chargesController.setLookupValueObjectClassName("org.jallinone.accounting.accounts.java.AccountVO");
//      chargesController.addLookup2ParentLink("accountCodeACC02", "chargesAccountCodeAcc02SAL07");
//      chargesController.addLookup2ParentLink("descriptionSYS10","chargesAccountDescrSAL07");
      chargesController.setFramePreferedSize(new Dimension(400,400));
      chargesController.setAllColumnVisible(false);
      chargesController.setVisibleColumn("accountCodeACC02", true);
      chargesController.setVisibleColumn("descriptionSYS10", true);
      chargesController.setPreferredWidthColumn("accountCodeACC02", 100);
      chargesController.setPreferredWidthColumn("descriptionSYS10", 290);
      chargesController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          AccountVO vo = (AccountVO)chargesController.getLookupVO();
          controlChargesCode.setValue(vo.getAccountCodeACC02());
          controlChargesDescr.setValue(vo.getDescriptionSYS10());
        }

        public void beforeLookupAction(ValueObject parentVO) { }

        public void forceValidate() {}

      });


      // set default values...
      HashMap map = new HashMap();
      map.put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01SCH03());
      map.put(ApplicationConsts.PARAM_CODE,ApplicationConsts.CREDITS_ACCOUNT);
      Response response = ClientUtils.getData("loadUserParam",map);
      if (!response.isError()) {
        String code = (String)((VOResponse)response).getVo();
        controlCreditsCode.setValue(code);
        controlCreditsCode.getLookupController().forceValidate();
      }

      map.clear();
      map.put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01SCH03());
      map.put(ApplicationConsts.PARAM_CODE,ApplicationConsts.ITEMS_ACCOUNT);
      response = ClientUtils.getData("loadUserParam",map);
      if (!response.isError()) {
        String code = (String)((VOResponse)response).getVo();
        controlItemsCode.setValue(code);
        controlItemsCode.getLookupController().forceValidate();
      }

      map.clear();
      map.put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01SCH03());
      map.put(ApplicationConsts.PARAM_CODE,ApplicationConsts.ACTIVITIES_ACCOUNT);
      response = ClientUtils.getData("loadUserParam",map);
      if (!response.isError()) {
        String code = (String)((VOResponse)response).getVo();
        controlActCode.setValue(code);
        controlActCode.getLookupController().forceValidate();
      }

      map.clear();
      map.put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01SCH03());
      map.put(ApplicationConsts.PARAM_CODE,ApplicationConsts.CHARGES_ACCOUNT);
      response = ClientUtils.getData("loadUserParam",map);
      if (!response.isError()) {
        String code = (String)((VOResponse)response).getVo();
        controlChargesCode.setValue(code);
        controlChargesCode.getLookupController().forceValidate();
      }


      // check if there exist a customer with the specified progressive in REG04...
      String subjectTypeREG04 = null;
      if (vo.getSubjectTypeReg04SCH03().equals(ApplicationConsts.SUBJECT_ORGANIZATION))
        subjectTypeREG04 = ApplicationConsts.SUBJECT_ORGANIZATION_CUSTOMER;
      else
        subjectTypeREG04 = ApplicationConsts.SUBJECT_PEOPLE_CUSTOMER;
      CustomerPK pk = new CustomerPK(vo.getCompanyCodeSys01SCH03(),vo.getProgressiveReg04SCH03(),subjectTypeREG04);
      Response res = ClientUtils.getData("loadCustomer",pk);
      if (!res.isError()) {
        if (subjectTypeREG04.equals(ApplicationConsts.SUBJECT_ORGANIZATION_CUSTOMER)) {
          OrganizationCustomerVO custVO = (OrganizationCustomerVO)((VOResponse)res).getVo();
          controlCustCode.setValue(custVO.getCustomerCodeSAL07());
          controlCustCode.setEnabled(false);
          if (custVO.getPricelistCodeSal01SAL07()!=null) {
            controlPricelistCode.setValue(custVO.getPricelistCodeSal01SAL07());
            controlPricelistDescr.setValue(custVO.getPricelistDescriptionSYS10());
            controlPricelistCode.setEnabled(false);
          }
          controlCurrencyCode.setValue(custVO.getCurrencyCodeReg03SAL01());
          controlPayCode.setValue(custVO.getPaymentCodeReg10SAL07());
          controlPayCode.setEnabled(false);
          controlPayDescr.setValue(custVO.getPaymentDescriptionSYS10());
          controlCreditsCode.setValue(custVO.getCreditAccountCodeAcc02SAL07());
          controlItemsCode.setValue(custVO.getItemsAccountCodeAcc02SAL07());
          controlActCode.setValue(custVO.getActivitiesAccountCodeAcc02SAL07());
          controlChargesCode.setValue(custVO.getChargesAccountCodeAcc02SAL07());
          controlCreditsCode.setEnabled(false);
          controlItemsCode.setEnabled(false);
          controlActCode.setEnabled(false);
          controlChargesCode.setEnabled(false);
        }
        else {
          PeopleCustomerVO custVO = (PeopleCustomerVO)((VOResponse)res).getVo();
          controlCustCode.setValue(custVO.getCustomerCodeSAL07());
          controlCustCode.setEnabled(false);
          if (custVO.getPricelistCodeSal01SAL07()!=null) {
            controlPricelistCode.setValue(custVO.getPricelistCodeSal01SAL07());
            controlPricelistDescr.setValue(custVO.getPricelistDescriptionSYS10());
            controlPricelistCode.setEnabled(false);
            controlCurrencyCode.setValue(custVO.getCurrencyCodeReg03SAL01());
            controlPayCode.setValue(custVO.getPaymentCodeReg10SAL07());
            controlPayCode.setEnabled(false);
            controlPayDescr.setValue(custVO.getPaymentDescriptionSYS10());
            controlCreditsCode.setValue(custVO.getCreditAccountCodeAcc02SAL07());
            controlItemsCode.setValue(custVO.getItemsAccountCodeAcc02SAL07());
            controlActCode.setValue(custVO.getActivitiesAccountCodeAcc02SAL07());
            controlChargesCode.setValue(custVO.getChargesAccountCodeAcc02SAL07());
            controlCreditsCode.setEnabled(false);
            controlItemsCode.setEnabled(false);
            controlActCode.setEnabled(false);
            controlChargesCode.setEnabled(false);
          }
        }
      }


      setSize(750,370);
      ClientUtils.centerDialog(MDIFrame.getInstance(),this);
      setVisible(true);
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    mainPanel.setLayout(borderLayout1);
    inputPanel.setBorder(BorderFactory.createEtchedBorder());
    inputPanel.setLayout(gridBagLayout1);
    buttonsPanel.setBorder(BorderFactory.createEtchedBorder());
    createButton.setText(ClientSettings.getInstance().getResources().getResource("create invoice"));
    createButton.addActionListener(new CreateInvoiceDialog_createButton_actionAdapter(this));
    annullButton.setText(ClientSettings.getInstance().getResources().getResource("cancel"));
    annullButton.addActionListener(new CreateInvoiceDialog_annullButton_actionAdapter(this));
    labelCustCode.setText("customerCodeSAL07");
    labelPricelist.setText("pricelistCodeSAL01");
    controlCustCode.setLinkLabel(labelCustCode);
    controlCustCode.setMaxCharacters(20);
    controlCustCode.setRequired(true);
    controlCustCode.setTrimText(true);
    controlCustCode.setUpperCase(true);
    controlPricelistCode.setLinkLabel(labelPricelist);
    controlPricelistCode.setMaxCharacters(20);
    controlPricelistCode.setRequired(true);
    controlPricelistDescr.setEnabled(false);
    controlPricelistDescr.setEnabledOnEdit(false);

    controlWarehouseCode.setMaxCharacters(20);
    controlWarehouseCode.setRequired(true);
    controlWareDescr.setEnabled(false);
    labelWarehouseCode.setText("warehouseCodeWAR01");

    this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    labelCurrencyCode.setText("currencyCodeREG03");
    controlCurrencyCode.setEnabled(false);
    labelPayment.setText("paymentCodeREG10");
    controlPayCode.setLinkLabel(labelPayment);
    controlPayCode.setMaxCharacters(20);
    controlPayCode.setRequired(true);
    controlPayDescr.setEnabled(false);
    controlCreditsDescr.setEnabled(false);
    controlItemsDescr.setEnabled(false);
    controlActDescr.setEnabled(false);
    controlChargesDescr.setEnabled(false);
    getContentPane().add(mainPanel);
    mainPanel.add(inputPanel,  BorderLayout.CENTER);
    mainPanel.add(buttonsPanel,  BorderLayout.SOUTH);
    buttonsPanel.add(createButton, null);
    buttonsPanel.add(annullButton, null);
    inputPanel.add(labelCustCode,     new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    inputPanel.add(labelPricelist,      new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 5), 0, 0));
    inputPanel.add(controlCustCode,         new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    inputPanel.add(controlPricelistCode,       new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    inputPanel.add(controlPricelistDescr,       new GridBagConstraints(2, 2, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    inputPanel.add(labelCurrencyCode,     new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    inputPanel.add(controlCurrencyCode,   new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    inputPanel.add(labelPayment,   new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    inputPanel.add(controlPayCode,  new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    inputPanel.add(controlPayDescr,   new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));

    inputPanel.add(labelWarehouseCode,   new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    inputPanel.add(controlWarehouseCode,  new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    inputPanel.add(controlWareDescr,   new GridBagConstraints(2, 7, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));

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

    inputPanel.add(labelCreditAccount,      new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
    inputPanel.add(labelItemAccount,      new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
    inputPanel.add(labelActivities,      new GridBagConstraints(0, 10, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
    inputPanel.add(labelCharges,       new GridBagConstraints(0, 11, 1, 1, 0.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 0), 0, 0));
    inputPanel.add(controlCreditsCode,      new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    inputPanel.add(controlCreditsDescr,      new GridBagConstraints(2, 8, 4, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    inputPanel.add(controlItemsCode,      new GridBagConstraints(1, 9, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    inputPanel.add(controlItemsDescr,      new GridBagConstraints(2, 9, 4, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    inputPanel.add(controlActCode,      new GridBagConstraints(1, 10, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    inputPanel.add(controlActDescr,      new GridBagConstraints(2, 10, 4, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    inputPanel.add(controlChargesCode,      new GridBagConstraints(1, 11, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    inputPanel.add(controlChargesDescr,      new GridBagConstraints(2, 11, 4, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));

  }


  void annullButton_actionPerformed(ActionEvent e) {
    setVisible(false);
  }


  void createButton_actionPerformed(ActionEvent e) {
    try {
      // customer code can be omitted (and automatically generated by server side...
//      if (controlCustCode.getValue()==null) {
//        JOptionPane.showMessageDialog(
//            ClientUtils.getParentFrame(frame),
//            ClientSettings.getInstance().getResources().getResource("you must define the customer code"),
//            ClientSettings.getInstance().getResources().getResource("Attention"),
//            JOptionPane.ERROR_MESSAGE
//        );
//        return;
//      }
      if (controlPricelistCode.getValue()==null) {
        JOptionPane.showMessageDialog(
            ClientUtils.getParentFrame(frame),
            ClientSettings.getInstance().getResources().getResource("you must select a pricelist"),
            ClientSettings.getInstance().getResources().getResource("Attention"),
            JOptionPane.ERROR_MESSAGE
        );
        return;
      }
      if (controlPayCode.getValue()==null) {
        JOptionPane.showMessageDialog(
            ClientUtils.getParentFrame(frame),
            ClientSettings.getInstance().getResources().getResource("you must select a payment"),
            ClientSettings.getInstance().getResources().getResource("Attention"),
            JOptionPane.ERROR_MESSAGE
        );
        return;
      }
      if (controlWarehouseCode.getValue()==null) {
        JOptionPane.showMessageDialog(
            ClientUtils.getParentFrame(frame),
            ClientSettings.getInstance().getResources().getResource("you must select a warehouse"),
            ClientSettings.getInstance().getResources().getResource("Attention"),
            JOptionPane.ERROR_MESSAGE
        );
        return;
      }
      if (controlCreditsCode.getValue()==null ||
          controlItemsCode.getValue()==null ||
          controlActCode.getValue()==null ||
          controlChargesCode.getValue()==null) {
        JOptionPane.showMessageDialog(
            ClientUtils.getParentFrame(frame),
            ClientSettings.getInstance().getResources().getResource("you must select all account codes"),
            ClientSettings.getInstance().getResources().getResource("Attention"),
            JOptionPane.ERROR_MESSAGE
        );
        return;
      }

      // check if there exists a scheduled employee that has not a task with a sale activity code linked...
      DetailCallOutRequestVO vo = (DetailCallOutRequestVO)frame.getCalloutPanel().getVOModel().getValueObject();
      VOListTableModel model = frame.getResourcesPanel().getTasksGrid().getVOListTableModel();
      ScheduledEmployeeVO empVO = null;
      ArrayList tasks = null;
      TaskVO taskVO = null;
      Response res = null;
      HashMap map = new HashMap();
      map.put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01SCH03());
      for(int i=0;i<model.getRowCount();i++) {
        empVO = (ScheduledEmployeeVO)model.getObjectForRow(i);
        res = ClientUtils.getData("validateTaskCode",new LookupValidationParams(empVO.getTaskCodeREG07(),map));
        if (res.isError()) {
          JOptionPane.showMessageDialog(
               ClientUtils.getParentFrame(frame),
               ClientSettings.getInstance().getResources().getResource(res.getErrorMessage()),
               ClientSettings.getInstance().getResources().getResource("Error"),
               JOptionPane.ERROR_MESSAGE
           );
           return;
        }
        tasks = new ArrayList(((VOListResponse)res).getRows());
        if (tasks.size()!=1) {
           JOptionPane.showMessageDialog(
               ClientUtils.getParentFrame(frame),
               ClientSettings.getInstance().getResources().getResource("no task defined for the scheduled employee")+" "+empVO.getEmployeeCodeSCH01(),
               ClientSettings.getInstance().getResources().getResource("Error"),
               JOptionPane.ERROR_MESSAGE
           );
           return;
        }
        taskVO = (TaskVO)tasks.get(0);
        if (taskVO.getActivityCodeSal09REG07()==null) {
          JOptionPane.showMessageDialog(
              ClientUtils.getParentFrame(frame),
              ClientSettings.getInstance().getResources().getResource("no sale activity code linked to the task")+" "+taskVO.getTaskCodeREG07(),
              ClientSettings.getInstance().getResources().getResource("Error"),
              JOptionPane.ERROR_MESSAGE
          );
          return;
        }
      }

      ScheduledActivityVO actVO = (ScheduledActivityVO)frame.getActPanel().getVOModel().getValueObject();
      DetailSaleDocVO docVO = new DetailSaleDocVO();
      docVO.setCompanyCodeSys01DOC01(vo.getCompanyCodeSys01SCH03());
      docVO.setDocYearDOC01(vo.getRequestYearSCH03());
      docVO.setDocTypeDOC01(ApplicationConsts.SALE_INVOICE_DOC_TYPE);
      docVO.setDocDateDOC01(new java.sql.Date(vo.getRequestDateSCH03().getTime()));
      docVO.setCurrencyCodeReg03DOC01(controlCurrencyCode.getValue().toString());
      docVO.setPricelistCodeSal01DOC01(controlPricelistCode.getValue().toString());
      docVO.setPricelistDescriptionDOC01(controlPricelistDescr.getValue().toString());
      docVO.setCustomerCodeSAL07((String)controlCustCode.getValue());
      docVO.setDocStateDOC01(ApplicationConsts.OPENED);
      docVO.setPaymentCodeReg10DOC01(controlPayCode.getValue().toString());
      docVO.setPaymentDescriptionDOC01(controlPayDescr.getValue().toString());
      docVO.setDocRefNumberDOC01(vo.getProgressiveSCH03().toString());
      docVO.setNoteDOC01(ClientSettings.getInstance().getResources().getResource("invoice from call-out request nr.")+" "+vo.getProgressiveSCH03()+"/"+vo.getRequestYearSCH03());
      docVO.setProgressiveReg04DOC01(vo.getProgressiveReg04SCH03());
      docVO.setWarehouseCodeWar01DOC01(controlWarehouseCode.getValue().toString());
      docVO.setDescriptionWar01DOC01(controlWareDescr.getValue().toString());

      InvoiceFromCallOutRequestVO invVO = new InvoiceFromCallOutRequestVO();
      invVO.setSaleVO(docVO);
      invVO.setCallOutRequest(vo);
      invVO.setCustomerAlreadyExists(!controlCustCode.isEnabled());
      invVO.setActVO(actVO);
      invVO.setCreditAccountCodeAcc02SAL07(controlCreditsCode.getValue().toString());
      invVO.setItemsAccountCodeAcc02SAL07(controlItemsCode.getValue().toString());
      invVO.setActivitiesAccountCodeAcc02SAL07(controlActCode.getValue().toString());
      invVO.setChargesAccountCodeAcc02SAL07(controlChargesCode.getValue().toString());
      res = ClientUtils.getData("createInvoiceFromScheduledActivity", invVO);
      if (res.isError()) {
        JOptionPane.showMessageDialog(
            ClientUtils.getParentFrame(frame),
            ClientSettings.getInstance().getResources().getResource(res.getErrorMessage()),
            ClientSettings.getInstance().getResources().getResource("Error"),
            JOptionPane.ERROR_MESSAGE
        );
      }
      else {
        vo = (DetailCallOutRequestVO)((VOResponse)res).getVo();
        frame.getCalloutPanel().getVOModel().setValueObject(vo);
        frame.getCalloutPanel().pull("callOutStateSCH03");
        frame.getInvoiceButton().setEnabled(false);
        frame.getViewInvoiceButton().setEnabled(true);
        frame.getActPanel().getVOModel().setValueObject(vo.getScheduledActivityVO());
        frame.getActPanel().getForm().pull("activityStateSCH06");
        if (frame.getGridFrame()!=null)
          frame.getGridFrame().getGrid().reloadCurrentBlockOfData();

        new SaleInvoiceDocController(null,new SaleDocPK(
          vo.getCompanyCodeSys01SCH03(),
          vo.getDocTypeDoc01SCH03(),
          vo.getDocYearDoc01SCH03(),
          vo.getDocNumberDoc01SCH03()
        ));

        setVisible(false);
      }

    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}

class CreateInvoiceDialog_annullButton_actionAdapter implements java.awt.event.ActionListener {
  CreateInvoiceDialog adaptee;

  CreateInvoiceDialog_annullButton_actionAdapter(CreateInvoiceDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.annullButton_actionPerformed(e);
  }
}

class CreateInvoiceDialog_createButton_actionAdapter implements java.awt.event.ActionListener {
  CreateInvoiceDialog adaptee;

  CreateInvoiceDialog_createButton_actionAdapter(CreateInvoiceDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.createButton_actionPerformed(e);
  }
}
