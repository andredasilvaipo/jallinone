package org.jallinone.sales.documents.client;

import javax.swing.border.*;
import java.awt.*;
import org.openswing.swing.client.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.lookup.client.LookupListener;
import org.openswing.swing.message.receive.java.*;
import java.util.Collection;
import org.openswing.swing.form.client.Form;
import org.jallinone.sales.documents.java.DetailSaleDocVO;
import org.openswing.swing.util.client.ClientSettings;
import org.jallinone.sales.customers.java.CustomerPK;
import org.openswing.swing.message.receive.java.*;
import org.jallinone.sales.customers.java.OrganizationCustomerVO;
import org.jallinone.commons.client.CompaniesComboControl;
import javax.swing.*;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.math.BigDecimal;

/**
  * <p>Title: JAllInOne ERP/CRM application</p>
  * <p>Description: Panel used in the sale header to show sale customer infos: customer, pricelist, currency, etc.</p>
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
public class SaleCustomerHeadPanel extends JPanel {

  GridBagLayout gridBagLayout1 = new GridBagLayout();
  TitledBorder titledBorder1;
  LabelControl labelCustomer = new LabelControl();
  CodLookupControl controlCustomerCode = new CodLookupControl();
  TextControl controlName1 = new TextControl();
  LabelControl labelPricelist = new LabelControl();
  CodLookupControl controlPricelistCode = new CodLookupControl();
  TextControl controlPricelistDescr = new TextControl();
  LabelControl labelCurrency = new LabelControl();
  TextControl controlCurrency = new TextControl();
  LabelControl labelPayment = new LabelControl();
  CodLookupControl controlPaymentCode = new CodLookupControl();
  TextControl controlPayDescr = new TextControl();

  LookupController customerController = new LookupController();
  LookupServerDataLocator customerDataLocator = new LookupServerDataLocator();

  LookupController pricelistController = new LookupController();
  LookupServerDataLocator pricelistDataLocator = new LookupServerDataLocator();

  LookupController payController = new LookupController();
  LookupServerDataLocator payDataLocator = new LookupServerDataLocator();
  TextControl controlName2 = new TextControl();

  LabelControl companyLabel = new LabelControl();
  CompaniesComboControl controlCompaniesCombo = new CompaniesComboControl();

  private boolean maybeShowCompaniesCombo = false;
  LabelControl labelVat = new LabelControl();
  TextControl controlD = new TextControl();
  LabelControl labelDeductible = new LabelControl();
  NumericControl controlDed = new NumericControl();
  NumericControl controlVatValue = new NumericControl();
  TextControl controlVatCode = new TextControl();
  LabelControl labelPerc = new LabelControl();


  public SaleCustomerHeadPanel(boolean maybeShowCompaniesCombo,final Form form) {
    this.maybeShowCompaniesCombo = maybeShowCompaniesCombo;
    try {

      // customer lookup...
      customerDataLocator.setGridMethodName("loadCustomers");
      customerDataLocator.setValidationMethodName("validateCustomerCode");

      controlCustomerCode.setLookupController(customerController);
      controlCustomerCode.setControllerMethodName("getCustomersList");
      customerController.setForm(form);
      customerController.setLookupDataLocator(customerDataLocator);
      customerController.setFrameTitle("customers");
      customerController.setLookupValueObjectClassName("org.jallinone.sales.customers.java.GridCustomerVO");
      customerController.addLookup2ParentLink("companyCodeSys01REG04", "companyCodeSys01DOC01");
      customerController.addLookup2ParentLink("customerCodeSAL07","customerCodeSAL07");
      customerController.addLookup2ParentLink("progressiveREG04","progressiveReg04DOC01");
      customerController.addLookup2ParentLink("name_1REG04", "name_1REG04");
      customerController.addLookup2ParentLink("name_2REG04", "name_2REG04");
      customerController.addLookup2ParentLink("paymentCodeReg10SAL07", "paymentCodeReg10DOC01");
      customerController.addLookup2ParentLink("paymentDescriptionSAL07", "paymentDescriptionDOC01");
      customerController.addLookup2ParentLink("vatCodeReg01SAL07", "customerVatCodeReg01DOC01");
      customerController.addLookup2ParentLink("valueREG01", "valueREG01");
      customerController.addLookup2ParentLink("deductibleREG01", "deductibleREG01");
      customerController.addLookup2ParentLink("vatDescriptionSYS10", "vatDescriptionSYS10");
      customerController.addLookup2ParentLink("noteREG04", "deliveryNoteDOC01");
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
      customerDataLocator.getLookupFrameParams().put(ApplicationConsts.FILTER_COMPANY_FOR_INSERT,"DOC01_ORDERS");
      customerDataLocator.getLookupValidationParameters().put(ApplicationConsts.FILTER_COMPANY_FOR_INSERT,"DOC01_ORDERS");
      customerController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          DetailSaleDocVO vo = (DetailSaleDocVO)form.getVOModel().getValueObject();
          if (vo.getCustomerCodeSAL07()==null || vo.getCustomerCodeSAL07().equals("")) {
            // customer not defined: remove customer pricelist code and currency code...
            vo.setPricelistCodeSal01DOC01(null);
            vo.setPricelistDescriptionDOC01(null);
            vo.setCurrencyCodeReg03DOC01(null);
          }
          else {
            Response res = ClientUtils.getData("loadCustomer",new CustomerPK(
                vo.getCompanyCodeSys01DOC01(),
                vo.getProgressiveReg04DOC01(),
                ApplicationConsts.SUBJECT_ORGANIZATION_CUSTOMER
            ));
            if (!res.isError()) {
              OrganizationCustomerVO custVO = (OrganizationCustomerVO)((VOResponse)res).getVo();
              if (custVO.getPricelistCodeSal01SAL07()!=null) {
                controlPricelistCode.setValue(custVO.getPricelistCodeSal01SAL07());
                pricelistController.forceValidate();
              }
            }
          }
        }

        public void beforeLookupAction(ValueObject parentVO) {
          if (customerDataLocator.getLookupFrameParams().get(ApplicationConsts.SUBJECT_TYPE)==null)
            return;
          if (customerDataLocator.getLookupFrameParams().get(ApplicationConsts.SUBJECT_TYPE).equals(ApplicationConsts.SUBJECT_ORGANIZATION_CUSTOMER)) {
            customerController.setHeaderColumnName("name_1REG04", "corporateName1");
            customerController.setHeaderColumnName("name_2REG04", "corporateName2");
          }
          else if (customerDataLocator.getLookupFrameParams().get(ApplicationConsts.SUBJECT_TYPE).equals(ApplicationConsts.SUBJECT_PEOPLE_CUSTOMER)) {
            customerController.setHeaderColumnName("name_1REG04", "firstname");
            customerController.setHeaderColumnName("name_2REG04", "lastname");
          }
        }

        public void forceValidate() {}

      });


      // pricelist lookup...
      pricelistDataLocator.setGridMethodName("loadPricelists");
      pricelistDataLocator.setValidationMethodName("validatePricelistCode");

      controlPricelistCode.setLookupController(pricelistController);
      controlPricelistCode.setControllerMethodName("getSalePricesList");
      pricelistController.setLookupDataLocator(pricelistDataLocator);
      pricelistController.setForm(form);
      pricelistController.setFrameTitle("pricelists");
      pricelistController.setLookupValueObjectClassName("org.jallinone.sales.pricelist.java.PricelistVO");
      pricelistController.addLookup2ParentLink("pricelistCodeSAL01","pricelistCodeSal01DOC01");
      pricelistController.addLookup2ParentLink("descriptionSYS10", "pricelistDescriptionDOC01");
      pricelistController.addLookup2ParentLink("currencyCodeReg03SAL01","currencyCodeReg03DOC01");
      pricelistController.setAllColumnVisible(false);
      pricelistController.setVisibleColumn("pricelistCodeSAL01", true);
      pricelistController.setVisibleColumn("descriptionSYS10", true);
      pricelistController.setVisibleColumn("currencyCodeReg03SAL01", true);
      pricelistController.setPreferredWidthColumn("descriptionSYS10", 250);
      pricelistController.setFramePreferedSize(new Dimension(420,500));
      pricelistController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {}

        public void beforeLookupAction(ValueObject parentVO) {
          // retrieve function identifier...
          DetailSaleDocVO vo = (DetailSaleDocVO)form.getVOModel().getValueObject();
          pricelistDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01DOC01());
          pricelistDataLocator.getLookupFrameParams().put(ApplicationConsts.PROGRESSIVE_REG04,vo.getProgressiveReg04DOC01());
          pricelistDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01DOC01());
          pricelistDataLocator.getLookupValidationParameters().put(ApplicationConsts.PROGRESSIVE_REG04,vo.getProgressiveReg04DOC01());
        }

        public void forceValidate() {}

      });


      // payment lookup...
      payDataLocator.setGridMethodName("loadPayments");
      payDataLocator.setValidationMethodName("validatePaymentCode");

      controlPaymentCode.setLookupController(payController);
      controlPaymentCode.setControllerMethodName("getPaymentsList");
      payController.setForm(form);
      payController.setLookupDataLocator(payDataLocator);
      payController.setFrameTitle("payments");
      payController.setLookupValueObjectClassName("org.jallinone.registers.payments.java.PaymentVO");
      payController.addLookup2ParentLink("paymentCodeREG10", "paymentCodeReg10DOC01");
      payController.addLookup2ParentLink("descriptionSYS10","paymentDescriptionDOC01");
      payController.setAllColumnVisible(false);
      payController.setVisibleColumn("paymentCodeREG10", true);
      payController.setVisibleColumn("descriptionSYS10", true);
      payController.setPreferredWidthColumn("descriptionSYS10", 250);
      payController.setFramePreferedSize(new Dimension(350,500));
			payController.addLookupListener(new LookupListener() {

				public void beforeLookupAction(org.openswing.swing.message.receive.java.ValueObject parentVO) {
					DetailSaleDocVO vo = (DetailSaleDocVO)form.getVOModel().getValueObject();
					payDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01DOC01());
					payDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01DOC01());
				}

				public void codeChanged(org.openswing.swing.message.receive.java.ValueObject parentVO,Collection parentChangedAttributes) {	}

				public void codeValidated(boolean validated) { }

				public void forceValidate() { }

			});

      jbInit();

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {

    companyLabel.setFont(new java.awt.Font("MS Sans Serif", 0, 11));
    companyLabel.setLabel("companyCodeSYS01");

    controlCompaniesCombo.setLinkLabel(companyLabel);
    controlCompaniesCombo.setFunctionCode(customerController.getForm().getFunctionId());

    titledBorder1 = new TitledBorder("");
    labelCustomer.setText("customer");
    titledBorder1.setTitle(ClientSettings.getInstance().getResources().getResource("customer data"));
    titledBorder1.setBorder(BorderFactory.createEtchedBorder());
    titledBorder1.setTitleColor(Color.blue);
    this.setBorder(titledBorder1);
    this.setLayout(gridBagLayout1);
    controlCustomerCode.setCanCopy(true);
    controlCustomerCode.setLinkLabel(labelCustomer);
    controlCustomerCode.setMaxCharacters(20);
    controlCustomerCode.setRequired(true);
    controlCustomerCode.setAttributeName("customerCodeSAL07");
    labelPricelist.setText("pricelist");
    labelCurrency.setText("currencyCodeREG03");
    labelPayment.setText("payment terms");
    controlName1.setCanCopy(true);
    controlName1.setEnabledOnInsert(false);
    controlName1.setEnabledOnEdit(false);
    controlName1.setAttributeName("name_1REG04");
    controlPricelistCode.setCanCopy(true);
    controlPricelistCode.setLinkLabel(labelPricelist);
    controlPricelistCode.setMaxCharacters(20);
    controlPricelistCode.setRequired(true);
    controlPricelistCode.setAttributeName("pricelistCodeSal01DOC01");
    controlPaymentCode.setCanCopy(true);
    controlPaymentCode.setLinkLabel(labelPayment);
    controlPaymentCode.setMaxCharacters(20);
    controlPaymentCode.setRequired(true);
    controlPaymentCode.setAttributeName("paymentCodeReg10DOC01");
    controlCurrency.setCanCopy(true);
    controlCurrency.setEnabledOnInsert(false);
    controlCurrency.setEnabledOnEdit(false);
    controlCurrency.setAttributeName("currencyCodeReg03DOC01");
    controlPricelistDescr.setEnabledOnInsert(false);
    controlPricelistDescr.setEnabledOnEdit(false);
    controlPricelistDescr.setAttributeName("pricelistDescriptionDOC01");
    controlPayDescr.setEnabledOnInsert(false);
    controlPayDescr.setEnabledOnEdit(false);
    controlPayDescr.setAttributeName("paymentDescriptionDOC01");
    controlName2.setEnabledOnInsert(false);
    controlName2.setEnabledOnEdit(false);
    controlName2.setAttributeName("name_2REG04");

    controlD.setEnabledOnInsert(false);
    controlD.setEnabledOnEdit(false);
    controlD.setAttributeName("vatDescriptionSYS10");


    if (maybeShowCompaniesCombo) {
      this.add(companyLabel,         new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
      this.add(controlCompaniesCombo,               new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
              ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 70, 0));

      controlCompaniesCombo.setAttributeName("companyCodeSys01DOC01");
      controlCompaniesCombo.addItemListener(new ItemListener() {

        public void itemStateChanged(ItemEvent e) {
          Object companyCodeSys01 = controlCompaniesCombo.getValue();
          if (companyCodeSys01==null)
            companyCodeSys01 = controlCompaniesCombo.getDomain().getDomainPairList()[0].getCode();

          customerDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,companyCodeSys01);
          customerDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,companyCodeSys01);
        }

      });

    }

    labelVat.setText("vatCodeREG01");
    labelDeductible.setText("deductibleREG01");
    controlDed.setEnabledOnInsert(false);
    controlDed.setEnabledOnEdit(false);
    controlDed.setAttributeName("deductibleREG01");
    controlVatValue.setEnabledOnInsert(false);
    controlVatValue.setAttributeName("valueREG01");
    controlVatValue.setEnabledOnEdit(false);
    controlVatCode.setAttributeName("customerVatCodeReg01DOC01");
    controlVatCode.setEnabledOnInsert(false);
    controlVatCode.setEnabledOnEdit(false);
    labelPerc.setText("valueREG01");
    this.add(labelCustomer,            new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlCustomerCode,                  new GridBagConstraints(1, 0, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 70, 0));
    this.add(labelPayment,              new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlName1,              new GridBagConstraints(4, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 200, 0));
    this.add(labelPricelist,          new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlPricelistCode,          new GridBagConstraints(1, 1, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 70, 0));
    this.add(controlPricelistDescr,           new GridBagConstraints(4, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlPaymentCode,        new GridBagConstraints(1, 2, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 70, 0));
    this.add(controlPayDescr,        new GridBagConstraints(4, 2, 3, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelCurrency,      new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlCurrency,      new GridBagConstraints(6, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 70, 0));
    this.add(controlName2,       new GridBagConstraints(5, 0, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    this.add(labelVat,       new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlD,      new GridBagConstraints(4, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 70, 0));
    this.add(labelDeductible,     new GridBagConstraints(5, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlDed,    new GridBagConstraints(6, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlVatValue,    new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    this.add(controlVatCode,    new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    this.add(labelPerc,   new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 10, 5, 5), 0, 0));
  }


  public LookupServerDataLocator getCustomerDataLocator() {
    return customerDataLocator;
  }
  public LookupController getCustomerController() {
    return customerController;
  }
  public CompaniesComboControl getControlCompaniesCombo() {
    return controlCompaniesCombo;
  }
  public CodLookupControl getControlCustomerCode() {
    return controlCustomerCode;
  }


}
