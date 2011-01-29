package org.jallinone.expirations.client;

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
import org.jallinone.commons.client.CustomizedColumns;
import java.math.BigDecimal;
import org.jallinone.expirations.java.ExpirationVO;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.lookup.client.LookupListener;
import org.openswing.swing.message.receive.java.*;
import java.util.Collection;
import org.jallinone.sales.customers.java.GridCustomerVO;
import org.jallinone.purchases.suppliers.java.GridSupplierVO;
import java.util.Date;
import org.openswing.swing.domains.java.Domain;
import org.jallinone.registers.payments.java.PaymentTypeVO;
import java.awt.event.*;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.form.client.*;
import org.jallinone.expirations.java.PaymentVO;
import org.openswing.swing.table.java.GridDataLocator;
import java.util.Map;
import java.util.ArrayList;
import org.jallinone.registers.currency.java.CurrencyVO;
import javax.swing.border.*;
import org.jallinone.expirations.java.PaymentDistributionVO;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the payments payments frame.</p>
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
public class PaymentsGridFrame extends InternalFrame  {

	JPanel auxPanel = new JPanel();
	JPanel buttonsPanel = new JPanel();
	JPanel filterPanel = new JPanel();
	FlowLayout flowLayout1 = new FlowLayout();
	BorderLayout borderLayout1 = new BorderLayout();
	ReloadButton reloadButton = new ReloadButton();
	GridControl grid = new GridControl();

	SaveButton saveButton = new SaveButton();

	private ServerGridDataLocator payGridDataLocator = new ServerGridDataLocator();
	TextColumn colDescr = new TextColumn();
	CurrencyColumn colPayedValue = new CurrencyColumn();
	CurrencyColumn colValue = new CurrencyColumn();
	CheckBoxColumn colPayed = new CheckBoxColumn();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	LabelControl labelStartDate = new LabelControl();
	LabelControl labelCustomer = new LabelControl();
	LabelControl labelSupplier = new LabelControl();
	DateControl controlStartDate = new DateControl();
	LabelControl labelEndDate = new LabelControl();
	DateControl controlEndDate = new DateControl();
	CodLookupControl controlCodCustomer = new CodLookupControl();
	CodLookupControl controlCodSupplier = new CodLookupControl();
	TextControl controlCustName1 = new TextControl();
	TextControl controlCustName2 = new TextControl();
	TextControl controlSupplierName1 = new TextControl();
	TextControl controlSupplierName2 = new TextControl();
	LookupController customerController = new LookupController();
	LookupServerDataLocator customerDataLocator = new LookupServerDataLocator();
	LookupController supplierController = new LookupController();
	LookupServerDataLocator supplierDataLocator = new LookupServerDataLocator();
	LookupController payTypeController = new LookupController();
	LookupServerDataLocator payTypeDataLocator = new LookupServerDataLocator();

	LookupController accController = new LookupController();
	LookupServerDataLocator accDataLocator = new LookupServerDataLocator();

	LookupController currController = new LookupController();
	LookupServerDataLocator currDataLocator = new LookupServerDataLocator();

	CurrencyColumn colAlreadyPayed = new CurrencyColumn();

  /** expirations grid */
	private GridControl parentGrid = null;
  JPanel payDetailPanel = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  Form payForm = new Form();
  JPanel detailGridPanel = new JPanel();
  GridControl payGrid = new GridControl();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  InsertButton insertButton1 = new InsertButton();
  BorderLayout borderLayout3 = new BorderLayout();
  DateColumn colDate = new DateColumn();
  GenericButton buttonSearch = new GenericButton(new ImageIcon(ClientUtils.getImage("filter.gif")));
  FlowLayout flowLayout2 = new FlowLayout();
  CurrencyColumn colValueDOC27 = new CurrencyColumn();
  TextColumn colPayTypeCode = new TextColumn();
  TextColumn colTypeDescr = new TextColumn();
  TextColumn colAccCode = new TextColumn();
  TextColumn colAccDescr = new TextColumn();
  TextColumn colBankDescr = new TextColumn();
  TextColumn colBankCode = new TextColumn();
  LabelControl labelPayDate = new LabelControl();
  DateControl controlPayDate = new DateControl();
  LabelControl labelPay = new LabelControl();
  LabelControl labelAcc = new LabelControl();
  LabelControl labelBank = new LabelControl();
  LabelControl labelValue = new LabelControl();
  CurrencyControl controlValue = new CurrencyControl();
  CodLookupControl controlAccCode = new CodLookupControl();
  CodLookupControl controlPayCode = new CodLookupControl();
  CodLookupControl controlBankCode = new CodLookupControl();
  TextControl controlPayDescr = new TextControl();
  TextControl controlAccDescr = new TextControl();
  TextControl controlBankDescr = new TextControl();
  CodLookupColumn colSelExp = new CodLookupColumn();
  PayGridCurrencySettings payGridCurrencySettings = new PayGridCurrencySettings();
  PaymentCurrencySettings payCurrencySettings = new PaymentCurrencySettings();
  LookupController bankController = new LookupController();
  LookupServerDataLocator bankDataLocator = new LookupServerDataLocator();
	LookupController expController = new LookupController();
	LookupServerDataLocator expDataLocator = new LookupServerDataLocator();
  CodLookupControl controlCurrency = new CodLookupControl();
  TitledBorder titledBorder1;
  TitledBorder titledBorder2;


	public PaymentsGridFrame(
		  InternalFrame parentFrame,
	    GridControl parentGrid,
			String companyCode,
			BigDecimal progressiveREG04,
			java.util.Date startDate,
			java.util.Date endDate,
			String customerCode,
			String supplierCode
	) {
	  parentFrame.pushFrame(this);
		this.setParentFrame(parentFrame);
		this.parentGrid = parentGrid;

		payForm.setFormController(new PaymentController(this));

		payGrid.setController(new GridController() {

			/**
			 * Callback method invoked when the data loading is completed.
			 * @param error <code>true</code> if data loading has terminated with errors, <code>false</code> otherwise
			 */
			public void loadDataCompleted(boolean error) {
				if (payGrid.getVOListTableModel().getRowCount()>0) {
					if (payForm.getMode()!=Consts.READONLY)
						payForm.setMode(Consts.READONLY);
					else
						payForm.reload();
				}
			}


		/**
		 * Callback method invoked when the user has double clicked on the selected row of the grid.
		 * @param rowNumber selected row index
		 * @param persistentObject v.o. related to the selected row
		 */
			public void doubleClick(int rowNumber,ValueObject persistentObject) {
				if (payForm.getMode()!=Consts.READONLY)
					payForm.setMode(Consts.READONLY);
				else
					payForm.reload();
			}

		});
		payGrid.setGridDataLocator(payGridDataLocator);
		payGridDataLocator.setServerMethodName("loadExpirationPayments");

  	grid.setController(new PaymentDistributionsController(this));
		grid.setGridDataLocator(new GridDataLocator() {

			public Response loadData(int action, int startIndex, Map filteredColumns,
															 ArrayList currentSortedColumns,
															 ArrayList currentSortedVersusColumns,
															 Class valueObjectType, Map otherGridParams) {

	      PaymentVO vo = (PaymentVO)payGrid.getVOListTableModel().getObjectForRow(payGrid.getSelectedRow());
				return ClientUtils.getData("loadPaymentDistributions",new Object[]{
				  vo.getCompanyCodeSys01DOC27(),
					vo.getProgressiveDOC27()
				});
			}

		});

		try {
			jbInit();
			init();

			setSize(750,550);
			setMinimumSize(new Dimension(750,550));
			MDIFrame.add(this,true);

			controlStartDate.setValue(startDate);
			controlEndDate.setValue(endDate);
			if (companyCode!=null)
				payGrid.getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,companyCode);
			if (progressiveREG04!=null) {
				payGrid.getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_REG04,progressiveREG04);
				controlCodCustomer.setValue(customerCode);
				if (customerCode!=null)
					controlCodCustomer.validateCode(customerCode);
				controlCodSupplier.setValue(supplierCode);
				if (supplierCode!=null)
					controlCodSupplier.validateCode(supplierCode);

				payGrid.reloadData();
			}
			else {
				payForm.setMode(Consts.READONLY);
				buttonSearch.setEnabled(false);
			}

		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}


	private void init() {

			// expirations lookup...
			expDataLocator.setGridMethodName("loadExpirations");
			colSelExp.setLookupController(expController);
			expController.setLookupDataLocator(expDataLocator);
			expController.setFrameTitle("expirations");
			expController.setLookupValueObjectClassName("org.jallinone.expirations.java.ExpirationVO");
			expController.addLookup2ParentLink("progressiveDOC19", "progressiveDoc19DOC28");
			expController.addLookup2ParentLink("descriptionDOC19","descriptionDOC19");
			expController.addLookup2ParentLink("docTypeDOC19","docTypeDOC19");
			expController.addLookup2ParentLink("valueDOC19","valueDOC19");
			expController.addLookup2ParentLink("alreadyPayedDOC19","alreadyPayedDOC19");
			expController.addLookup2ParentLink("roundingAccountCodeAcc02DOC19","roundingAccountCodeAcc02DOC19");

			expController.setAllColumnVisible(false);
			expController.setVisibleColumn("descriptionDOC19", true);
			expController.setVisibleColumn("valueDOC19", true);
			expController.setVisibleColumn("alreadyPayedDOC19", true);
			expController.setVisibleColumn("docDateDOC19", true);
			expController.setVisibleColumn("expirationDateDOC19", true);
			expController.setVisibleColumn("paymentTypeCodeReg11DOC19", true);
			expController.setVisibleColumn("paymentDescriptionSYS10", true);

			expController.setHeaderColumnName("descriptionDOC19", "descriptionSYS10");
			expController.setHeaderColumnName("alreadyPayedDOC19", "already payed");
			expController.setHeaderColumnName("paymentTypeCodeReg11DOC19", "paymentTypeCodeREG11");
			expController.setHeaderColumnName("paymentDescriptionSYS10", "paymentTypeDescription");

			expController.setPreferredWidthColumn("descriptionDOC19",250);
			expController.setPreferredWidthColumn("paymentDescriptionSYS10",160);
			expController.setFramePreferedSize(new Dimension(750,400));
			expController.addLookupListener(new LookupListener() {

				public void codeValidated(boolean validated) {}

				public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
					ExpirationVO vo = (ExpirationVO)expController.getLookupVO();
					PaymentDistributionVO pVO = (PaymentDistributionVO)grid.getVOListTableModel().getObjectForRow(grid.getSelectedRow());
					if (vo!=null && vo.getProgressiveDOC19()!=null) {
						pVO.setPaymentValueDOC28(vo.getValueDOC19().subtract(vo.getAlreadyPayedDOC19()));
					}
					else {
						pVO.setPaymentValueDOC28(null);
					}
				}

				public void beforeLookupAction(ValueObject parentVO) {
					PaymentVO vo = (PaymentVO)payForm.getVOModel().getValueObject();
					expDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01DOC27());
					expDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01DOC27());
					expDataLocator.getLookupFrameParams().put(ApplicationConsts.PROGRESSIVE_REG04,vo.getProgressiveReg04DOC27());
					expDataLocator.getLookupValidationParameters().put(ApplicationConsts.PROGRESSIVE_REG04,vo.getProgressiveReg04DOC27());
					expDataLocator.getLookupFrameParams().put(ApplicationConsts.PAYED,"N");
					expDataLocator.getLookupValidationParameters().put(ApplicationConsts.PAYED,"N");
					expDataLocator.getLookupFrameParams().put(ApplicationConsts.CURRENCY_CODE_REG03,vo.getCurrencyCodeReg03DOC27());
					expDataLocator.getLookupValidationParameters().put(ApplicationConsts.CURRENCY_CODE_REG03,vo.getCurrencyCodeReg03DOC27());

				}

				public void forceValidate() {}

			});

			// banks lookup...
			bankDataLocator.setGridMethodName("loadBanks");
			bankDataLocator.setValidationMethodName("validateBankCode");
			controlBankCode.setLookupController(bankController);
			controlBankCode.setControllerMethodName("getBanksList");
			bankController.setLookupDataLocator(bankDataLocator);
			bankController.setFrameTitle("banks");
			bankController.setLookupValueObjectClassName("org.jallinone.registers.bank.java.BankVO");
			bankController.addLookup2ParentLink("bankCodeREG12", "bankCodeReg12DOC27");
			bankController.addLookup2ParentLink("descriptionREG12","descriptionREG12");
			bankController.setAllColumnVisible(false);
			bankController.setVisibleColumn("bankCodeREG12", true);
			bankController.setVisibleColumn("descriptionREG12", true);
			bankController.setPreferredWidthColumn("descriptionREG12",200);
			new CustomizedColumns(new BigDecimal(232),bankController);

			// customer lookup...
			customerDataLocator.setGridMethodName("loadCustomers");
			customerDataLocator.setValidationMethodName("validateCustomerCode");

			controlCodCustomer.setLookupController(customerController);
			controlCodCustomer.setControllerMethodName("getCustomersList");
			customerController.setLookupDataLocator(customerDataLocator);
//      customerController.setForm(filterPanel);
			customerController.setFrameTitle("customers");
			customerController.setLookupValueObjectClassName("org.jallinone.sales.customers.java.GridCustomerVO");
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
//      customerDataLocator.getLookupFrameParams().put(ApplicationConsts.FILTER_COMPANY_FOR_INSERT,"DOC01_ORDERS");
//      customerDataLocator.getLookupValidationParameters().put(ApplicationConsts.FILTER_COMPANY_FOR_INSERT,"DOC01_ORDERS");
			customerController.addLookupListener(new LookupListener() {

				public void codeValidated(boolean validated) {}

				public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
					GridCustomerVO vo = (GridCustomerVO)customerController.getLookupVO();
					if (vo.getCustomerCodeSAL07()==null || vo.getCustomerCodeSAL07().equals("")) {
						controlCustName1.setText("");
						controlCustName2.setText("");
						payGrid.getOtherGridParams().remove(ApplicationConsts.COMPANY_CODE_SYS01);
						payGrid.getOtherGridParams().remove(ApplicationConsts.PROGRESSIVE_REG04);
						buttonSearch.setEnabled(false);
						payGrid.clearData();
						payForm.setMode(Consts.READONLY);

	          insertButton1.setEnabled(false);
						saveButton.setEnabled(false);
						reloadButton.setEnabled(false);
						grid.clearData();
					}
					else {
						controlCodCustomer.setValue(vo.getCustomerCodeSAL07());
						controlCustName1.setText(vo.getName_1REG04());
						controlCustName2.setText(vo.getName_2REG04());
						controlCodSupplier.setValue("");
						controlSupplierName1.setText("");
						controlSupplierName2.setText("");
						payGrid.getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01REG04());
						payGrid.getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_REG04,vo.getProgressiveREG04());
						buttonSearch.setEnabled(true);
						payGrid.clearData();
						payForm.setMode(Consts.READONLY);
						grid.clearData();

						insertButton1.setEnabled(true);
						saveButton.setEnabled(false);
						reloadButton.setEnabled(true);
					}
				}

				public void beforeLookupAction(ValueObject parentVO) {}

				public void forceValidate() {}

			});


			// supplier lookup...
			supplierDataLocator.setGridMethodName("loadSuppliers");
			supplierDataLocator.setValidationMethodName("validateSupplierCode");

			controlCodSupplier.setLookupController(supplierController);
			controlCodSupplier.setControllerMethodName("getSuppliersList");
			supplierController.setLookupDataLocator(supplierDataLocator);
//      supplierController.setForm(filterPanel);
			supplierController.setFrameTitle("suppliers");
			supplierController.setLookupValueObjectClassName("org.jallinone.purchases.suppliers.java.GridSupplierVO");
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
//      supplierDataLocator.getLookupFrameParams().put(ApplicationConsts.FILTER_COMPANY_FOR_INSERT,"DOC06_ORDERS");
//      supplierDataLocator.getLookupValidationParameters().put(ApplicationConsts.FILTER_COMPANY_FOR_INSERT,"DOC06_ORDERS");
			supplierController.addLookupListener(new LookupListener() {

				public void codeValidated(boolean validated) {}

				public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
					GridSupplierVO vo = (GridSupplierVO)supplierController.getLookupVO();
					if (vo.getSupplierCodePUR01()==null || vo.getSupplierCodePUR01().equals("")) {
						controlSupplierName1.setText("");
						controlSupplierName2.setText("");
						payGrid.getOtherGridParams().remove(ApplicationConsts.COMPANY_CODE_SYS01);
						payGrid.getOtherGridParams().remove(ApplicationConsts.PROGRESSIVE_REG04);
						buttonSearch.setEnabled(false);
						payGrid.clearData();
						payForm.setMode(Consts.READONLY);

						insertButton1.setEnabled(false);
						saveButton.setEnabled(false);
						reloadButton.setEnabled(false);
					}
					else {
						controlCodSupplier.setValue(vo.getSupplierCodePUR01());
						controlSupplierName1.setText(vo.getName_1REG04());
						controlSupplierName2.setText(vo.getName_2REG04());
						controlCodCustomer.setValue("");
						controlCustName1.setText("");
						controlCustName2.setText("");
						payGrid.getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01REG04());
						payGrid.getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_REG04,vo.getProgressiveREG04());
						buttonSearch.setEnabled(true);
						payGrid.clearData();
						payForm.setMode(Consts.READONLY);

						insertButton1.setEnabled(true);
						reloadButton.setEnabled(true);
						saveButton.setEnabled(false);
					}
				}

				public void beforeLookupAction(ValueObject parentVO) {}

				public void forceValidate() {}

			});

			controlStartDate.addDateChangedListener(new DateChangedListener() {
				public void dateChanged(Date oldDate,Date newDate) {
					if (newDate!=null)
						payGrid.getOtherGridParams().put(ApplicationConsts.START_DATE,newDate);
					else
						payGrid.getOtherGridParams().remove(ApplicationConsts.START_DATE);
				}
			});

			controlEndDate.addDateChangedListener(new DateChangedListener() {
				public void dateChanged(Date oldDate,Date newDate) {
					if (newDate!=null)
						payGrid.getOtherGridParams().put(ApplicationConsts.END_DATE,newDate);
					else
						payGrid.getOtherGridParams().remove(ApplicationConsts.END_DATE);
				}
			});


	    colPayedValue.setDynamicSettings(payGridCurrencySettings);

			colValue.setDynamicSettings(payCurrencySettings);
			colPayedValue.setDynamicSettings(payCurrencySettings);
			colAlreadyPayed.setDynamicSettings(payCurrencySettings);

			// payment lookup...
			payTypeDataLocator.setGridMethodName("loadPaymentTypes");
			payTypeDataLocator.setValidationMethodName("validatePaymentTypeCode");

			controlPayCode.setLookupController(payTypeController);
			controlPayCode.setControllerMethodName("getPaymentTypesList");
			payTypeController.setLookupDataLocator(payTypeDataLocator);
			payTypeController.setFrameTitle("payment types");
			payTypeController.setLookupValueObjectClassName("org.jallinone.registers.payments.java.PaymentTypeVO");
			payTypeController.addLookup2ParentLink("paymentTypeCodeREG11", "paymentTypeCodeReg11DOC27");
			payTypeController.addLookup2ParentLink("descriptionSYS10","paymentDescriptionSYS10");
			payTypeController.setAllColumnVisible(false);
			payTypeController.setVisibleColumn("paymentTypeCodeREG11", true);
			payTypeController.setVisibleColumn("descriptionSYS10", true);
			new CustomizedColumns(new BigDecimal(222),payTypeController);
			payTypeController.addLookupListener(new LookupListener() {

				public void beforeLookupAction(ValueObject parentVO) {
				}

				public void codeChanged(ValueObject parentVO,
																Collection parentChangedAttributes) {
					PaymentTypeVO vo = (PaymentTypeVO)payTypeController.getLookupVO();
					PaymentVO payVO = (PaymentVO)parentVO;
					if (vo==null || vo.getAccountCodeAcc02REG11()==null) {
						payVO.setAccountCodeAcc02DOC27(null);
						payVO.setAcc02DescriptionSYS10(null);
					}
					else {
						payVO.setAccountCodeAcc02DOC27(vo.getAccountCodeAcc02REG11());
						payVO.setAcc02DescriptionSYS10(vo.getAcc02DescriptionSYS10());
					}
				}

				public void codeValidated(boolean validated) {
				}

				public void forceValidate() {
				}

			});


			// account lookup...
			accDataLocator.setGridMethodName("loadAccounts");
			accDataLocator.setValidationMethodName("validateAccountCode");

			controlAccCode.setLookupController(accController);
			controlAccCode.setControllerMethodName("getAccounts");
			accController.setLookupDataLocator(accDataLocator);
			accController.setFrameTitle("accounts");
			accController.setLookupValueObjectClassName("org.jallinone.accounting.accounts.java.AccountVO");
			accController.addLookup2ParentLink("accountCodeACC02", "accountCodeAcc02DOC27");
			accController.addLookup2ParentLink("descriptionSYS10","acc02DescriptionSYS10");
			accController.setAllColumnVisible(false);
			accController.setVisibleColumn("accountCodeACC02", true);
			accController.setVisibleColumn("descriptionSYS10", true);
			accController.setPreferredWidthColumn("descriptionSYS10", 200);
			accController.setFramePreferedSize(new Dimension(340,400));
			accController.setSortedColumn("accountCodeACC02","ASC",1);
			accController.setFilterableColumn("accountCodeACC02",true);
			accController.setFilterableColumn("descriptionSYS10",true);


			// currency lookup...
			currDataLocator.setGridMethodName("loadCurrencies");
			currDataLocator.setValidationMethodName("validateCurrencyCode");
			controlCurrency.setLookupController(currController);
			controlCurrency.setControllerMethodName("getCurrencies");
			currController.setLookupDataLocator(currDataLocator);
			currController.setFrameTitle("currencies");
			currController.setLookupValueObjectClassName("org.jallinone.registers.currency.java.CurrencyVO");
			currController.addLookup2ParentLink("currencyCodeREG03", "currencyCodeReg03DOC27");
			currController.addLookup2ParentLink("currencySymbolREG03","currencySymbolREG03");
			currController.addLookup2ParentLink("decimalsREG03","decimalsREG03");
			currController.setAllColumnVisible(false);
			currController.setVisibleColumn("currencyCodeREG03", true);
			currController.setVisibleColumn("currencySymbolREG03", true);
			currController.addLookupListener(new LookupListener() {

				public void beforeLookupAction(ValueObject parentVO) {
				}

				public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
					CurrencyVO cVO = (CurrencyVO)currController.getLookupVO();
					if (cVO!=null && cVO.getCurrencyCodeREG03()!=null) {
						controlValue.setCurrencySymbol(cVO.getCurrencySymbolREG03());
						controlValue.setDecimals(cVO.getDecimalsREG03().intValue());
						controlValue.setDecimalSymbol(cVO.getDecimalSymbolREG03().charAt(0));
						grid.getTable().insert();
						//grid.setMode(Consts.INSERT);
					}
					else {
						grid.clearData();
						grid.setMode(Consts.READONLY);
					}
				}

				public void codeValidated(boolean validated) {
				}

				public void forceValidate() {
				}

			});


  }


	private void jbInit() throws Exception {
		titledBorder1 = new TitledBorder("");
    titledBorder2 = new TitledBorder("");
    buttonsPanel.setLayout(flowLayout2);

		buttonSearch.setHorizontalTextPosition(SwingConstants.LEADING);

		grid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    grid.setShowFilterPanelOnGrid(false);
    grid.setShowWarnMessageBeforeReloading(false);
		saveButton.setExecuteAsThread(true);
		payGrid.setValueObjectClassName("org.jallinone.expirations.java.PaymentVO");
		grid.setValueObjectClassName("org.jallinone.expirations.java.PaymentDistributionVO");
    grid.setVisibleStatusPanel(false);
		this.setTitle(ClientSettings.getInstance().getResources().getResource("payments"));
		flowLayout1.setAlignment(FlowLayout.LEFT);
		grid.setAutoLoadData(false);
    grid.setMaxNumberOfRowsOnInsert(100);
    grid.setMaxSortedColumns(4);
		grid.setReloadButton(reloadButton);
    grid.setSaveButton(null);
		colDescr.setColumnFilterable(false);
    colDescr.setColumnSortable(false);
		colDescr.setColumnName("descriptionDOC19");
		colDescr.setEditableOnEdit(false);
		colDescr.setHeaderColumnName("descriptionSYS10");
		colDescr.setPreferredWidth(350);




		colPayedValue.setColumnName("paymentValueDOC28");
		colPayedValue.setEditableOnEdit(true);
    colPayedValue.setEditableOnInsert(true);
    colPayedValue.setHeaderColumnName("payment");
		colPayedValue.setPreferredWidth(70);
		colPayedValue.setColumnRequired(true);
		colPayedValue.setColumnSortable(true);

		colValue.setColumnFilterable(false);
    colValue.setColumnName("valueDOC19");
    colValue.setColumnSortable(false);
		colValue.setPreferredWidth(70);
		colValue.setRightMargin(2);
		colPayed.setColumnRequired(false);
    colPayed.setColumnSortable(false);
		colPayed.setShowDeSelectAllInPopupMenu(true);
    colPayed.setAdditionalHeaderColumnName("payedDOC19");
    colPayed.setColumnFilterable(false);
		colPayed.setColumnName("payedDOC28");
		colPayed.setEditableOnEdit(true);
    colPayed.setEditableOnInsert(true);
    colPayed.setHeaderColumnName("payedDOC19");
		colPayed.setPreferredWidth(60);
		filterPanel.setLayout(gridBagLayout1);
		labelStartDate.setText("startDate");
		labelCustomer.setText("customer");
		labelSupplier.setText("supplier");
		labelEndDate.setText("endDate");
		controlCodCustomer.setMaxCharacters(20);
		controlCustName1.setEnabled(false);
		controlCustName1.setEnabledOnInsert(false);
		controlCustName1.setEnabledOnEdit(false);
		controlCustName2.setEnabled(false);
		controlCustName2.setEnabledOnInsert(false);
		controlCustName2.setEnabledOnEdit(false);
		controlCodSupplier.setMaxCharacters(20);
		controlSupplierName1.setEnabled(false);
		controlSupplierName1.setEnabledOnInsert(false);
		controlSupplierName1.setEnabledOnEdit(false);
		controlSupplierName2.setEnabled(false);
		controlSupplierName2.setEnabledOnInsert(false);
		controlSupplierName2.setEnabledOnEdit(false);

		colAlreadyPayed.setColumnName("alreadyPayedDOC19");
    colAlreadyPayed.setColumnRequired(true);
		colAlreadyPayed.setHeaderColumnName("already payed");
		colAlreadyPayed.setPreferredWidth(80);
		payDetailPanel.setLayout(borderLayout2);
    payDetailPanel.setPreferredSize(new Dimension(100, 230));
    detailGridPanel.setBorder(titledBorder2);
    detailGridPanel.setMinimumSize(new Dimension(10, 10));
    detailGridPanel.setPreferredSize(new Dimension(550, 250));
    detailGridPanel.setLayout(borderLayout3);
    payForm.setLayout(gridBagLayout2);
		payGrid.setAutoLoadData(false);
    payGrid.setMaxSortedColumns(3);
    buttonSearch.setButtonBehavior(Consts.BUTTON_IMAGE_AND_TEXT);
    buttonSearch.setText("search");
    buttonSearch.addActionListener(new PaymentsGridFrame_buttonSearch_actionAdapter(this));
    flowLayout2.setAlignment(FlowLayout.LEFT);
    colDate.setColumnName("paymentDateDOC27");
    colDate.setColumnFilterable(true);
    colDate.setColumnSortable(true);
    colDate.setHeaderColumnName("payment date");
    colDate.setSortVersus(org.openswing.swing.util.java.Consts.DESC_SORTED);
    colDate.setSortingOrder(1);
    colValueDOC27.setColumnName("paymentValueDOC27");
    colValueDOC27.setHeaderColumnName("payment");
    colValueDOC27.setPreferredWidth(90);
    colPayTypeCode.setColumnName("paymentTypeCodeReg11DOC27");
    colPayTypeCode.setColumnFilterable(true);
    colPayTypeCode.setColumnSortable(true);
    colPayTypeCode.setHeaderColumnName("paymentTypeCodeREG11");
    colPayTypeCode.setPreferredWidth(90);
    colTypeDescr.setColumnName("paymentDescriptionSYS10");
    colTypeDescr.setHeaderColumnName("paymentTypeDescription");
    colTypeDescr.setPreferredWidth(200);
    colAccCode.setColumnName("accountCodeAcc02DOC27");
    colAccCode.setColumnFilterable(true);
    colAccCode.setColumnSortable(true);
    colAccCode.setHeaderColumnName("accountCodeACC02");
    colAccCode.setPreferredWidth(90);
    colBankDescr.setColumnName("descriptionREG12");
    colBankDescr.setColumnFilterable(true);
    colBankDescr.setColumnSortable(true);
    colBankDescr.setPreferredWidth(200);
    colBankCode.setPreferredWidth(90);
    colAccDescr.setColumnName("acc02DescriptionSYS10");
    colAccDescr.setColumnFilterable(true);
    colAccDescr.setHeaderColumnName("account description");
    colAccDescr.setPreferredWidth(200);
    colBankCode.setColumnName("bankCodeReg12DOC27");
    colBankCode.setColumnFilterable(true);
    colBankCode.setColumnSortable(true);
    colBankCode.setColumnVisible(true);
    colBankCode.setHeaderColumnName("bankCodeREG12");
    colBankCode.setPreferredWidth(80);
    payForm.setInsertButton(insertButton1);
    payForm.setReloadButton(reloadButton);
    payForm.setSaveButton(saveButton);
    payForm.setVOClassName("org.jallinone.expirations.java.PaymentVO");
    labelPayDate.setText("payment date");
    labelPay.setText("paymentTypeCodeREG11");
    labelAcc.setText("accountCodeACC02");
    labelBank.setText("bank");
    labelValue.setText("payment");
    controlBankDescr.setAttributeName("descriptionREG12");
    controlBankDescr.setEnabledOnInsert(false);
    controlBankDescr.setEnabledOnEdit(false);
    controlAccDescr.setAttributeName("acc02DescriptionSYS10");
    controlAccDescr.setEnabledOnInsert(false);
    controlAccDescr.setEnabledOnEdit(false);
    controlPayDescr.setAttributeName("paymentDescriptionSYS10");
    controlPayDescr.setEnabledOnInsert(false);
    controlPayDescr.setEnabledOnEdit(false);
    controlAccCode.setAttributeName("paymentTypeCodeReg11DOC27");
    controlPayCode.setAttributeName("paymentTypeCodeReg11DOC27");
    controlPayCode.setColumns(5);
    controlPayCode.setLinkLabel(labelPay);
    controlPayCode.setMaxCharacters(20);
    controlPayCode.setRequired(true);
    controlPayCode.addActionListener(new PaymentsGridFrame_controlPayCode_actionAdapter(this));
    controlAccCode.setRequired(true);
    controlAccCode.setAttributeName("accountCodeAcc02DOC27");
    controlAccCode.setColumns(5);
    controlAccCode.setLinkLabel(labelAcc);
    controlAccCode.setMaxCharacters(20);
    controlAccCode.setRequired(true);
    controlAccCode.addActionListener(new PaymentsGridFrame_controlAccCode_actionAdapter(this));
    controlBankCode.setAttributeName("bankCodeReg12DOC27");
    controlBankCode.setColumns(8);
    controlBankCode.setMaxCharacters(20);
    controlValue.setAttributeName("paymentValueDOC27");
    controlValue.setLinkLabel(labelValue);
    controlValue.setRequired(true);
    controlPayDate.setAttributeName("paymentDateDOC27");
    controlPayDate.setLinkLabel(labelPayDate);
    controlPayDate.setRequired(true);
    colSelExp.setColumnName("progressiveDoc19DOC28");
    colSelExp.setHideCodeBox(true);
    colSelExp.setEditableOnInsert(true);
    colSelExp.setHeaderColumnName("exp");
    colSelExp.setPreferredWidth(40);
    controlCurrency.setAttributeName("currencyCodeReg03DOC27");
    controlCurrency.setColumns(8);
    controlCurrency.setMaxCharacters(20);
    controlCurrency.setRequired(true);
    titledBorder2.setTitleColor(Color.blue);
    titledBorder2.setTitle(ClientSettings.getInstance().getResources().getResource("expirations"));
    filterPanel.add(labelStartDate,     new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		filterPanel.add(labelCustomer,    new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		filterPanel.add(labelSupplier,   new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlStartDate, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(labelEndDate, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    filterPanel.add(controlCodCustomer, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlCodSupplier, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlCustName1, new GridBagConstraints(2, 2, 2, 1, 1.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    filterPanel.add(controlCustName2, new GridBagConstraints(4, 2, 1, 1, 1.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    filterPanel.add(controlSupplierName1, new GridBagConstraints(2, 3, 2, 1, 1.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    filterPanel.add(controlSupplierName2, new GridBagConstraints(4, 3, 1, 1, 1.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    filterPanel.add(controlEndDate, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(buttonSearch,   new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    buttonsPanel.add(insertButton1, null);
    buttonsPanel.add(saveButton, null);
		buttonsPanel.add(reloadButton, null);
    this.getContentPane().add(payGrid,  BorderLayout.CENTER);
    payGrid.getColumnContainer().add(colDate, null);
    payGrid.getColumnContainer().add(colValueDOC27, null);
    payGrid.getColumnContainer().add(colPayTypeCode, null);
    payGrid.getColumnContainer().add(colTypeDescr, null);
    payGrid.getColumnContainer().add(colAccCode, null);
    payGrid.getColumnContainer().add(colAccDescr, null);
    payGrid.getColumnContainer().add(colBankCode, null);
    payGrid.getColumnContainer().add(colBankDescr, null);
    this.getContentPane().add(filterPanel, BorderLayout.NORTH);

		grid.getColumnContainer().add(colPayedValue, null);
    grid.getColumnContainer().add(colPayed, null);
		grid.getColumnContainer().add(colAlreadyPayed, null);

		grid.getColumnContainer().add(colValue, null);
    grid.getColumnContainer().add(colSelExp, null);


		grid.getColumnContainer().add(colDescr, null);
    this.getContentPane().add(payDetailPanel,  BorderLayout.SOUTH);
    payDetailPanel.add(payForm,  BorderLayout.CENTER);
    payForm.add(labelPayDate,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    payForm.add(controlPayDate,   new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    payForm.add(labelPay,  new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    payForm.add(labelAcc,  new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    payForm.add(labelBank,  new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    payForm.add(labelValue,  new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    payForm.add(controlPayCode,  new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    payForm.add(controlAccCode,  new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    payForm.add(controlBankCode,  new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    payForm.add(controlPayDescr,  new GridBagConstraints(2, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    payForm.add(controlAccDescr,  new GridBagConstraints(2, 2, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    payForm.add(controlBankDescr,  new GridBagConstraints(2, 3, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    payForm.add(controlValue,  new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    payForm.add(controlCurrency,  new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    payDetailPanel.add(detailGridPanel,  BorderLayout.EAST);
    detailGridPanel.add(grid,  BorderLayout.CENTER);
    detailGridPanel.add(grid, null);
    payDetailPanel.add(buttonsPanel, BorderLayout.NORTH);
	}



	class PaymentCurrencySettings implements CurrencyColumnSettings {

		public double getMaxValue(int row) {
			return Double.MAX_VALUE;
		}

		public double getMinValue(int row) {
			return 0.0;
		}


		public boolean isGrouping(int row) {
			return true;
		}


		public int getDecimals(int row) {
			PaymentVO vo = (PaymentVO)payForm.getVOModel().getValueObject();
			if (vo!=null && vo.getDecimalsREG03()!=null)
				return vo.getDecimalsREG03().intValue();
			else
				return 0;
		}


		public String getCurrencySymbol(int row) {
			PaymentVO vo = (PaymentVO)payForm.getVOModel().getValueObject();
			if (vo!=null && vo.getCurrencySymbolREG03()!=null)
				return vo.getCurrencySymbolREG03();
			else
			return "E";
		}

	}



	class PayGridCurrencySettings implements CurrencyColumnSettings {

		public double getMaxValue(int row) {
			return Double.MAX_VALUE;
		}

		public double getMinValue(int row) {
			return 0.0;
		}


		public boolean isGrouping(int row) {
			return true;
		}


		public int getDecimals(int row) {
			PaymentVO vo = (PaymentVO)payGrid.getVOListTableModel().getObjectForRow(row);
			if (vo!=null && vo.getDecimalsREG03()!=null)
				return vo.getDecimalsREG03().intValue();
			else
				return 0;
		}


		public String getCurrencySymbol(int row) {
			PaymentVO vo = (PaymentVO)payGrid.getVOListTableModel().getObjectForRow(row);
			if (vo!=null && vo.getCurrencySymbolREG03()!=null)
				return vo.getCurrencySymbolREG03();
			else
			return "E";
		}

	}


  void buttonSearch_actionPerformed(ActionEvent e) {
		payForm.setMode(Consts.READONLY);
		payGrid.reloadData();
  }


  public GridControl getGrid() {
    return grid;
  }
  public GridControl getPayGrid() {
    return payGrid;
  }
  public Form getPayForm() {
    return payForm;
  }
  public CurrencyControl getControlValue() {
    return controlValue;
  }

  void controlPayCode_actionPerformed(ActionEvent e) {

  }

  void controlAccCode_actionPerformed(ActionEvent e) {

  }
  public CodLookupControl getControlCodCustomer() {
    return controlCodCustomer;
  }
  public CodLookupControl getControlCodSupplier() {
    return controlCodSupplier;
  }
  public GridControl getParentGrid() {
    return parentGrid;
  }



}

class PaymentsGridFrame_buttonSearch_actionAdapter implements java.awt.event.ActionListener {
  PaymentsGridFrame adaptee;

  PaymentsGridFrame_buttonSearch_actionAdapter(PaymentsGridFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.buttonSearch_actionPerformed(e);
  }
}

class PaymentsGridFrame_controlPayCode_actionAdapter implements java.awt.event.ActionListener {
  PaymentsGridFrame adaptee;

  PaymentsGridFrame_controlPayCode_actionAdapter(PaymentsGridFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.controlPayCode_actionPerformed(e);
  }
}

class PaymentsGridFrame_controlAccCode_actionAdapter implements java.awt.event.ActionListener {
  PaymentsGridFrame adaptee;

  PaymentsGridFrame_controlAccCode_actionAdapter(PaymentsGridFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.controlAccCode_actionPerformed(e);
  }
}
