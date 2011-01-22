package org.jallinone.purchases.documents.client;

import javax.swing.JPanel;
import javax.swing.border.*;
import java.awt.*;
import org.openswing.swing.client.*;
import org.openswing.swing.util.client.*;
import javax.swing.BorderFactory;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.lookup.client.LookupListener;
import org.openswing.swing.message.receive.java.*;
import java.util.Collection;
import org.openswing.swing.form.client.Form;
import org.jallinone.purchases.documents.java.DetailPurchaseDocVO;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;
import java.util.ArrayList;
import org.jallinone.purchases.pricelist.java.SupplierPricelistVO;

/**
  * <p>Title: JAllInOne ERP/CRM application</p>
  * <p>Description: Panel used in the purchase header to show purchase supplier infos: supplier, pricelist, currency, etc.</p>
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
public class PurchaseSupplierHeadPanel extends JPanel {

  GridBagLayout gridBagLayout1 = new GridBagLayout();
  TitledBorder titledBorder1;
  LabelControl labelSupplier = new LabelControl();
  CodLookupControl controlSupplierCode = new CodLookupControl();
  TextControl controlSupplierDescr = new TextControl();
  LabelControl labelPricelist = new LabelControl();
  CodLookupControl controlPricelistCode = new CodLookupControl();
  TextControl controlPricelistDescr = new TextControl();
  LabelControl labelCurrency = new LabelControl();
  TextControl controlCurrency = new TextControl();
  LabelControl labelPayment = new LabelControl();
  CodLookupControl controlPaymentCode = new CodLookupControl();
  TextControl controlPayDescr = new TextControl();
  LabelControl labelNote = new LabelControl();
  TextAreaControl controlNote = new TextAreaControl();

  LookupController supplierController = new LookupController();
  LookupServerDataLocator supplierDataLocator = new LookupServerDataLocator();

  LookupController pricelistController = new LookupController();
  LookupServerDataLocator pricelistDataLocator = new LookupServerDataLocator();

  LookupController payController = new LookupController();
  LookupServerDataLocator payDataLocator = new LookupServerDataLocator();


  public PurchaseSupplierHeadPanel(final Form form) {
    try {
      jbInit();

      // supplier lookup...
      supplierDataLocator.setGridMethodName("loadSuppliers");
      supplierDataLocator.setValidationMethodName("validateSupplierCode");

      controlSupplierCode.setLookupController(supplierController);
      controlSupplierCode.setControllerMethodName("getSuppliersList");
      supplierController.setForm(form);
      supplierController.setLookupDataLocator(supplierDataLocator);
      supplierController.setFrameTitle("suppliers");
      supplierController.setLookupValueObjectClassName("org.jallinone.purchases.suppliers.java.GridSupplierVO");
      supplierController.addLookup2ParentLink("companyCodeSys01REG04", "companyCodeSys01DOC06");
      supplierController.addLookup2ParentLink("supplierCodePUR01","supplierCodePUR01");
      supplierController.addLookup2ParentLink("progressiveREG04","progressiveReg04DOC06");
      supplierController.addLookup2ParentLink("name_1REG04", "name_1REG04");
      supplierController.addLookup2ParentLink("paymentCodeReg10PUR01", "paymentCodeReg10DOC06");
      supplierController.addLookup2ParentLink("paymentDescriptionPUR01", "paymentDescriptionDOC06");
      supplierController.addLookup2ParentLink("noteREG04", "noteDOC06");
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
      supplierDataLocator.getLookupFrameParams().put(ApplicationConsts.FILTER_COMPANY_FOR_INSERT,"DOC06_ORDERS");
      supplierDataLocator.getLookupValidationParameters().put(ApplicationConsts.FILTER_COMPANY_FOR_INSERT,"DOC06_ORDERS");
      supplierController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          DetailPurchaseDocVO vo = (DetailPurchaseDocVO)form.getVOModel().getValueObject();
          if (vo.getSupplierCodePUR01()==null || vo.getSupplierCodePUR01().equals("")) {
            // remove supplier pricelist code and currency code...
            vo.setPricelistCodePur03DOC06(null);
            vo.setPricelistDescriptionDOC06(null);
            vo.setCurrencyCodeReg03DOC06(null);
          }
          else {
            GridParams gridParams = new GridParams();
            gridParams.getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01DOC06());
            gridParams.getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_REG04,vo.getProgressiveReg04DOC06());

            Response res = ClientUtils.getData("loadSupplierPricelists",gridParams);
            if (!res.isError()) {
              java.util.List rows = ((VOListResponse)res).getRows();
              if (rows.size()>0) {
                SupplierPricelistVO pricelistVO = (SupplierPricelistVO)rows.get(0);
                controlPricelistCode.setValue(pricelistVO.getPricelistCodePUR03());
                pricelistController.forceValidate();
              }
            }
          }
        }

        public void beforeLookupAction(ValueObject parentVO) {}

        public void forceValidate() {}

      });


      // pricelist lookup...
      pricelistDataLocator.setGridMethodName("loadSupplierPricelists");
      pricelistDataLocator.setValidationMethodName("validateSupplierPricelistCode");

      controlPricelistCode.setLookupController(pricelistController);
      pricelistController.setLookupDataLocator(pricelistDataLocator);
      pricelistController.setForm(form);
      pricelistController.setFrameTitle("supplierPricelists");
      pricelistController.setLookupValueObjectClassName("org.jallinone.purchases.pricelist.java.SupplierPricelistVO");
      pricelistController.addLookup2ParentLink("pricelistCodePUR03","pricelistCodePur03DOC06");
      pricelistController.addLookup2ParentLink("descriptionSYS10", "pricelistDescriptionDOC06");
      pricelistController.addLookup2ParentLink("currencyCodeReg03PUR03","currencyCodeReg03DOC06");
      pricelistController.setAllColumnVisible(false);
      pricelistController.setVisibleColumn("pricelistCodePUR03", true);
      pricelistController.setVisibleColumn("descriptionSYS10", true);
      pricelistController.setVisibleColumn("currencyCodeReg03PUR03", true);
      pricelistController.setPreferredWidthColumn("descriptionSYS10", 250);
      pricelistController.setFramePreferedSize(new Dimension(420,500));
      pricelistController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {}

        public void beforeLookupAction(ValueObject parentVO) {

          // retrieve function identifier...
          Container c = PurchaseSupplierHeadPanel.this.getParent();
          while (c != null && ! (c instanceof Form)) {
            c = c.getParent();
          }
          if (c != null) {
            Form form = (Form)c;
            DetailPurchaseDocVO vo = (DetailPurchaseDocVO)form.getVOModel().getValueObject();
            pricelistDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01DOC06());
            pricelistDataLocator.getLookupFrameParams().put(ApplicationConsts.PROGRESSIVE_REG04,vo.getProgressiveReg04DOC06());
            pricelistDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01DOC06());
            pricelistDataLocator.getLookupValidationParameters().put(ApplicationConsts.PROGRESSIVE_REG04,vo.getProgressiveReg04DOC06());
          }
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
      payController.addLookup2ParentLink("paymentCodeREG10", "paymentCodeReg10DOC06");
      payController.addLookup2ParentLink("descriptionSYS10","paymentDescriptionDOC06");
      payController.setAllColumnVisible(false);
      payController.setVisibleColumn("paymentCodeREG10", true);
      payController.setVisibleColumn("descriptionSYS10", true);
      payController.setPreferredWidthColumn("descriptionSYS10", 250);
      payController.setFramePreferedSize(new Dimension(350,500));
			payController.addLookupListener(new LookupListener() {

				public void beforeLookupAction(org.openswing.swing.message.receive.java.ValueObject parentVO) {
					DetailPurchaseDocVO vo = (DetailPurchaseDocVO)form.getVOModel().getValueObject();
					payDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01DOC06());
					payDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01DOC06());
				}

				public void codeChanged(org.openswing.swing.message.receive.java.ValueObject parentVO,Collection parentChangedAttributes) {	}

				public void codeValidated(boolean validated) { }

				public void forceValidate() { }

			});

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    titledBorder1 = new TitledBorder("");
    labelSupplier.setText("supplier");
    titledBorder1.setTitle(ClientSettings.getInstance().getResources().getResource("supplier data"));
    titledBorder1.setBorder(BorderFactory.createEtchedBorder());
    titledBorder1.setTitleColor(Color.blue);
    this.setBorder(titledBorder1);
    this.setLayout(gridBagLayout1);
    controlSupplierCode.setCanCopy(true);
    controlSupplierCode.setLinkLabel(labelSupplier);
    controlSupplierCode.setMaxCharacters(20);
    controlSupplierCode.setRequired(true);
    controlSupplierCode.setAttributeName("supplierCodePUR01");
    labelPricelist.setText("pricelist");
    labelCurrency.setText("currencyCodeREG03");
    labelPayment.setText("payment terms");
    labelNote.setText("note");
    controlSupplierDescr.setCanCopy(true);
    controlSupplierDescr.setEnabledOnInsert(false);
    controlSupplierDescr.setEnabledOnEdit(false);
    controlSupplierDescr.setAttributeName("name_1REG04");
    controlPricelistCode.setCanCopy(true);
    controlPricelistCode.setColumns(5);
    controlPricelistCode.setControllerClassName("");
    controlPricelistCode.setLinkLabel(labelPricelist);
    controlPricelistCode.setMaxCharacters(20);
    controlPricelistCode.setRequired(true);
    controlPricelistCode.setAttributeName("pricelistCodePur03DOC06");
    controlPaymentCode.setCanCopy(true);
    controlPaymentCode.setColumns(5);
    controlPaymentCode.setLinkLabel(labelPayment);
    controlPaymentCode.setMaxCharacters(20);
    controlPaymentCode.setRequired(true);
    controlPaymentCode.setAttributeName("paymentCodeReg10DOC06");
    controlCurrency.setCanCopy(true);
    controlCurrency.setEnabledOnInsert(false);
    controlCurrency.setEnabledOnEdit(false);
    controlCurrency.setAttributeName("currencyCodeReg03DOC06");
    controlNote.setCanCopy(true);
    controlNote.setLinkLabel(labelNote);
    controlNote.setMaxCharacters(2000);
    controlNote.setAttributeName("noteDOC06");
    controlPricelistDescr.setEnabledOnInsert(false);
    controlPricelistDescr.setEnabledOnEdit(false);
    controlPricelistDescr.setAttributeName("pricelistDescriptionDOC06");
    controlPayDescr.setEnabledOnInsert(false);
    controlPayDescr.setEnabledOnEdit(false);
    controlPayDescr.setAttributeName("paymentDescriptionDOC06");
    this.add(labelSupplier,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlSupplierCode,             new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelPricelist,     new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlPricelistCode,        new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlPricelistDescr,      new GridBagConstraints(5, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 70, 0));
    this.add(labelCurrency,     new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlCurrency,        new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelPayment,     new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlPaymentCode,        new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 30, 0));
    this.add(controlPayDescr,       new GridBagConstraints(5, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelNote,     new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlNote,     new GridBagConstraints(1, 2, 5, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlSupplierDescr,   new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 70, 0));
  }

}
