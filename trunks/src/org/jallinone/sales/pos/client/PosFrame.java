package org.jallinone.sales.pos.client;

import javax.swing.*;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.util.client.ClientSettings;
import java.awt.*;
import org.openswing.swing.client.*;
import org.openswing.swing.mdi.client.*;
import java.awt.event.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.message.receive.java.Response;
import java.util.Map;
import java.util.*;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.util.java.Consts;
import java.math.BigDecimal;
import org.jallinone.commons.client.ClientApplet;
import org.jallinone.commons.client.ApplicationClientFacade;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.sales.documents.java.DetailSaleDocRowVO;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.openswing.swing.lookup.client.LookupListener;
import org.openswing.swing.message.receive.java.ValueObject;
import org.jallinone.warehouse.java.StoredSerialNumberVO;
import org.openswing.swing.message.send.java.LookupValidationParams;
import org.jallinone.warehouse.java.WarehouseVO;
import org.jallinone.sales.customers.java.GridCustomerVO;
import org.jallinone.sales.customers.java.CustomerPK;
import org.jallinone.sales.customers.java.PeopleCustomerVO;
import org.openswing.swing.message.receive.java.VOResponse;
import org.jallinone.sales.documents.java.PriceItemVO;
import org.jallinone.registers.currency.java.CurrencyVO;
import org.jallinone.items.java.VariantBarcodeVO;
import org.jallinone.sales.documents.java.DetailSaleDocVO;
import org.openswing.swing.logger.client.Logger;
import org.jallinone.registers.payments.java.PaymentVO;
import org.jallinone.sales.documents.java.SaleDocPK;
import org.jallinone.sales.documents.invoices.client.SaleInvoiceDocFromSaleDocController;
import org.openswing.swing.client.MultiLineLabelControl;
import org.jallinone.sales.documents.java.ItemSoldToOtherCustomersVO;
import org.jallinone.sales.pricelist.java.VariantsPriceVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Frame used to manage a POS.</p>
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
public class PosFrame extends JFrame {

	JPanel mainPanel = new JPanel();
	BorderLayout borderLayout1 = new BorderLayout();
	JPanel buttonsPanel = new JPanel();
	JPanel topPanel = new JPanel();
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	GridBagLayout gridBagLayout2 = new GridBagLayout();
	FlowLayout flowLayout1 = new FlowLayout();
	LabelControl labelUsername = new LabelControl();
	TextControl controlUser = new TextControl();
	LabelControl labelComp = new LabelControl();
	TextControl controlComp = new TextControl();
	Clock controlClock = new Clock();
	GenericButton buttonExit = new GenericButton();
	JPanel itemsPanel = new JPanel();
	JPanel keysPanel = new JPanel();
	GridBagLayout gridBagLayout3 = new GridBagLayout();
	JButton b7 = new JButton();
	JButton b4 = new JButton();
	JButton b1 = new JButton();
	LabelControl labelCustomer = new LabelControl();
	TextControl controlCustomer = new TextControl();
	JButton b2 = new JButton();
	JButton b5 = new JButton();
	JButton b8 = new JButton();
	JButton b9 = new JButton();
	JButton bc = new JButton();
	JButton b6 = new JButton();
	JButton b3 = new JButton();
	JButton b0 = new JButton();
	JButton bv = new JButton();
	JButton bi = new JButton();
	JButton be = new JButton();
	GridBagLayout gridBagLayout4 = new GridBagLayout();
	LabelControl labelBarcode = new LabelControl();
	TextControl controlBarcode = new TextControl();
	GridControl grid = new GridControl();
	JPanel totPanel = new JPanel();
	TextColumn colItemCode = new TextColumn();
	TextColumn colItemDescr = new TextColumn();
	DecimalColumn colQta = new DecimalColumn();
	CurrencyColumn colTotDisc = new CurrencyColumn();
	CurrencyColumn colTot = new CurrencyColumn();
	JPanel voidPanel = new JPanel();
	GridBagLayout gridBagLayout5 = new GridBagLayout();
	LabelControl labelSubTot = new LabelControl();
	LabelControl labelDisc = new LabelControl();
	LabelControl labelPaid = new LabelControl();
	LabelControl labelTotal = new LabelControl();
	LabelControl labelChange = new LabelControl();
	LabelControl labelQty = new LabelControl();
	LabelControl labelItemPercDiscount = new LabelControl();
	LabelControl labelItemValDiscount = new LabelControl();
	LabelControl labelValDiscountTotal = new LabelControl();
	LabelControl labelValPaidTotal = new LabelControl();
	NumericControl controlQty = new NumericControl();
	CurrencyControl controlItemValDiscount = new CurrencyControl();
	CurrencyControl controlValDiscountTotal = new CurrencyControl();
	CurrencyControl controlValPaidTotal = new CurrencyControl();
	CurrencyControl controlSubtotal = new CurrencyControl();
	CurrencyControl controlDiscount = new CurrencyControl();
	CurrencyControl controlPaid = new CurrencyControl();
	CurrencyControl controlTotal = new CurrencyControl();
	CurrencyControl controlChange = new CurrencyControl();
	GenericButton buttonCustomer = new GenericButton();
	LabelControl labelWarehouse = new LabelControl();
	TextControl controlW = new TextControl();
	GenericButton buttonBack = new GenericButton();
	GenericButton buttonDel = new GenericButton();
	GenericButton buttonQty = new GenericButton();
	GenericButton buttonDiscount = new GenericButton();
	GenericButton buttonDiscountTotal = new GenericButton();
	GenericButton buttonPaidTotal = new GenericButton();
	GenericButton buttonStart = new GenericButton();
	GenericButton buttonClose = new GenericButton();
	LookupController barCodeController = new LookupController();
	LookupServerDataLocator barcodeLocator = new LookupServerDataLocator();
	private NumericControl controlDiscountPerc = new NumericControl();
	private JPanel panControlDiscountPerc = new JPanel(new BorderLayout());

	public static final int START_SALE = 0;
	public static final int INS_BARCODE = 1;
	public static final int OTHER_COMMANDS = 2;
	public static final int INS_QTY = 3;
	public static final int INS_DISCOUNT = 4;
	public static final int INS_DISCOUNT_TOTAL = 41;
	public static final int INS_CUSTOMER = 5;
	public static final int INS_PAID_TOTAL = 6;
	private int state = START_SALE;

	private ArrayList rows = new ArrayList();

	private WarehouseVO warehouseVO = null;
	private PeopleCustomerVO customerVO = null;
	private CurrencyVO currVO = null;
	private DetailSaleDocVO detailSaleDocVO = null;

	LabelControl labelCustomerCode = new LabelControl();
	CodLookupControl controlCustomerCode = new CodLookupControl();
	TextControl controlName1 = new TextControl();
	LabelControl labelPayment = new LabelControl();
	CodLookupControl controlPaymentCode = new CodLookupControl();
	TextControl controlPayDescr = new TextControl();
	LookupController customerController = new LookupController();
	LookupServerDataLocator customerDataLocator = new LookupServerDataLocator();

	LookupController payController = new LookupController();
	LookupServerDataLocator payDataLocator = new LookupServerDataLocator();
	TextControl controlName2 = new TextControl();
	JPanel void3Panel = new JPanel();
	private String defaultCustomerCode = null;
	private String defaultCompanyCodeSys01 = null;
	JPanel suggstmtPanel = new JPanel();
	MultiLineLabelControl labelSuggstmt = new MultiLineLabelControl();
	BorderLayout borderLayout2 = new BorderLayout();
  JPanel payPanel = new JPanel();
  GridBagLayout gridBagLayout6 = new GridBagLayout();


	public PosFrame(String companyCodeSys01,String customerCode,String warehouseCode) {
		super(ClientSettings.getInstance().getResources().getResource("point of sale"));
		super.setDefaultCloseOperation(super.DO_NOTHING_ON_CLOSE);
		super.setIconImage(ClientUtils.getImage(ClientSettings.ICON_FILENAME));
		super.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				buttonExit_actionPerformed(null);
			}

		});
		this.defaultCompanyCodeSys01 = companyCodeSys01;
		this.defaultCustomerCode = customerCode;
		setSize(800,600);
		//this.setExtendedState(this.getExtendedState() | client.getExtendedState());
		ClientUtils.centerFrame(this);
		try {
			jbInit();
			init(companyCodeSys01,customerCode,warehouseCode);
			setVisible(true);

			SwingUtilities.invokeLater(new Runnable() {

				public void run() {
					buttonStart.requestFocus();
					updateContext();
				}

			});

		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}


	private void init(String companyCodeSys01,String customerCode,String warehouseCode) {
		controlComp.setValue(companyCodeSys01);
		ApplicationClientFacade facade = (ApplicationClientFacade)MDIFrame.getInstance().getClientFacade();
		controlUser.setValue(facade.getMainClass().getUsername());
		controlCustomer.setValue(customerCode);
		controlW.setValue(warehouseCode);

		grid.setGridDataLocator(new GridDataLocator() {

			public Response loadData(int action, int startIndex, Map filteredColumns,
															 ArrayList currentSortedColumns,
															 ArrayList currentSortedVersusColumns,
															 Class valueObjectType, Map otherGridParams) {
				return new VOListResponse(rows,false,rows.size());
			}
		});


		// load warehouse info...
		LookupValidationParams pars = new LookupValidationParams(warehouseCode,new HashMap());
		pars.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,companyCodeSys01);
		Response res = ClientUtils.getData("validateWarehouseCode",pars);
		if (!res.isError()) {
			java.util.List vos = ((VOListResponse)res).getRows();
			if (vos.size()==1)
				warehouseVO = (WarehouseVO)vos.get(0);

			else
				warehouseVO = null;
		}
		else
			warehouseVO = null;


		// load customer info...
		pars = new LookupValidationParams(customerCode,new HashMap());
		pars.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,companyCodeSys01);
		res = ClientUtils.getData("validateCustomerCode",pars);
		GridCustomerVO gridCustomerVO = null;
		if (!res.isError()) {
			java.util.List vos = ((VOListResponse)res).getRows();
			if (vos.size()==1)
				gridCustomerVO = (GridCustomerVO)vos.get(0);
			else
				gridCustomerVO = null;
		}
		else
			gridCustomerVO = null;


		// load customer detail vo...
		res = ClientUtils.getData("loadCustomer",new CustomerPK(
				gridCustomerVO.getCompanyCodeSys01REG04(),
				gridCustomerVO.getProgressiveREG04(),
				ApplicationConsts.SUBJECT_PEOPLE_CUSTOMER
		));
		if (!res.isError())
			customerVO = (PeopleCustomerVO)((VOResponse)res).getVo();
		else
			customerVO = null;


		// load currency info...
		pars = new LookupValidationParams(customerVO.getCurrencyCodeReg03SAL01(),new HashMap());
		res = ClientUtils.getData("validateCurrencyCode",pars);
		if (!res.isError())
			currVO = (CurrencyVO)((VOListResponse)res).getRows().get(0);
		else
			currVO = null;


		// set currency format...
		controlChange.setCurrencySymbol(currVO.getCurrencySymbolREG03());
		controlChange.setDecimalSymbol(currVO.getDecimalSymbolREG03().charAt(0));
		controlChange.setGroupingSymbol(currVO.getThousandSymbolREG03().charAt(0));
		controlChange.setDecimals(currVO.getDecimalsREG03().intValue());
		controlDiscount.setCurrencySymbol(currVO.getCurrencySymbolREG03());
		controlDiscount.setDecimalSymbol(currVO.getDecimalSymbolREG03().charAt(0));
		controlDiscount.setGroupingSymbol(currVO.getThousandSymbolREG03().charAt(0));
		controlDiscount.setDecimals(currVO.getDecimalsREG03().intValue());
		controlPaid.setCurrencySymbol(currVO.getCurrencySymbolREG03());
		controlPaid.setDecimalSymbol(currVO.getDecimalSymbolREG03().charAt(0));
		controlPaid.setGroupingSymbol(currVO.getThousandSymbolREG03().charAt(0));
		controlPaid.setDecimals(currVO.getDecimalsREG03().intValue());
		controlSubtotal.setCurrencySymbol(currVO.getCurrencySymbolREG03());
		controlSubtotal.setDecimalSymbol(currVO.getDecimalSymbolREG03().charAt(0));
		controlSubtotal.setGroupingSymbol(currVO.getThousandSymbolREG03().charAt(0));
		controlSubtotal.setDecimals(currVO.getDecimalsREG03().intValue());
		controlTotal.setCurrencySymbol(currVO.getCurrencySymbolREG03());
		controlTotal.setDecimalSymbol(currVO.getDecimalSymbolREG03().charAt(0));
		controlTotal.setGroupingSymbol(currVO.getThousandSymbolREG03().charAt(0));
		controlTotal.setDecimals(currVO.getDecimalsREG03().intValue());
		controlQty.setDecimals(0);
		controlItemValDiscount.setCurrencySymbol(currVO.getCurrencySymbolREG03());
		controlItemValDiscount.setDecimalSymbol(currVO.getDecimalSymbolREG03().charAt(0));
		controlItemValDiscount.setGroupingSymbol(currVO.getThousandSymbolREG03().charAt(0));
		controlItemValDiscount.setDecimals(currVO.getDecimalsREG03().intValue());
		controlValDiscountTotal.setCurrencySymbol(currVO.getCurrencySymbolREG03());
		controlValDiscountTotal.setDecimalSymbol(currVO.getDecimalSymbolREG03().charAt(0));
		controlValDiscountTotal.setGroupingSymbol(currVO.getThousandSymbolREG03().charAt(0));
		controlValDiscountTotal.setDecimals(currVO.getDecimalsREG03().intValue());
		controlValPaidTotal.setCurrencySymbol(currVO.getCurrencySymbolREG03());
		controlValPaidTotal.setDecimalSymbol(currVO.getDecimalSymbolREG03().charAt(0));
		controlValPaidTotal.setGroupingSymbol(currVO.getThousandSymbolREG03().charAt(0));
		controlValPaidTotal.setDecimals(currVO.getDecimalsREG03().intValue());
		colTot.setCurrencySymbol(currVO.getCurrencySymbolREG03());
		colTot.setDecimals(currVO.getDecimalsREG03().intValue());
		colTotDisc.setCurrencySymbol(currVO.getCurrencySymbolREG03());
		colTotDisc.setDecimals(currVO.getDecimalsREG03().intValue());

		controlDiscountPerc.setDecimals(3);
		controlDiscountPerc.setColumns(5);
		panControlDiscountPerc.add(controlDiscountPerc, BorderLayout.CENTER);
		panControlDiscountPerc.add(new JLabel("%"), BorderLayout.EAST);

		// barcode lookup...
		controlBarcode.setTrimText(true);
		controlBarcode.setUpperCase(true);
/*
		controlBarcode.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				checkBarcode();
			}
		});
 */
		controlBarcode.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==e.VK_ENTER || e.getKeyCode()==e.VK_TAB)
					checkBarcode();
			}
		});


/*
					try {
						if (barCodeController.getLookupVO()!=null) {
							StoredSerialNumberVO snVO = (StoredSerialNumberVO)barCodeController.getLookupVO();

							LookupValidationParams pars = new LookupValidationParams(snVO.getItemCodeItm01WAR05(),new HashMap());
							pars.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,snVO.getCompanyCodeSys01WAR05());
							pars.getLookupValidationParameters().put(ApplicationConsts.PRICELIST,customerVO.getPricelistCodeSal01SAL07());
							Response res = ClientUtils.getData("validatePriceItemCode",pars);
							PriceItemVO priceItemVO = null;
							if (!res.isError()) {
								java.util.List vos = ((VOListResponse)res).getRows();
								if (vos.size()==1)
									priceItemVO = (PriceItemVO)vos.get(0);
								else
									priceItemVO = null;
							}
							else
								priceItemVO = null;

							if (priceItemVO!=null) {
								if (snVO.getSerialNumberWAR05()==null) {
									// no bar code found in WAR05, but barcode found in ITM01:
									// check if the item ha not variants...
									if (priceItemVO.getUseVariant1ITM01().equals(ApplicationConsts.JOLLY) &&
											priceItemVO.getUseVariant2ITM01().equals(ApplicationConsts.JOLLY) &&
											priceItemVO.getUseVariant3ITM01().equals(ApplicationConsts.JOLLY) &&
											priceItemVO.getUseVariant4ITM01().equals(ApplicationConsts.JOLLY) &&
											priceItemVO.getUseVariant5ITM01().equals(ApplicationConsts.JOLLY))
										maybeAddRow(snVO,priceItemVO);
								}
								else {
									// serial num & bar code found:
									maybeAddRow(snVO,priceItemVO);
								}
							}
						}

						controlBarcode.setValue(null);
					}
					catch (Exception ex) {
						ex.printStackTrace();
					}

			public void beforeLookupAction(ValueObject parentVO) {
				barcodeLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,customerVO.getCompanyCodeSys01REG04());
				barcodeLocator.getLookupFrameParams().put(ApplicationConsts.PROGRESSIVE_HIE01,warehouseVO.getProgressiveHie01HIE02());
				barcodeLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,customerVO.getCompanyCodeSys01REG04());
				barcodeLocator.getLookupValidationParameters().put(ApplicationConsts.PROGRESSIVE_HIE01,warehouseVO.getProgressiveHie01HIE02());
				barcodeLocator.getLookupValidationParameters().put(ApplicationConsts.VALIDATE_BARCODE,Boolean.TRUE);
				barcodeLocator.getLookupValidationParameters().put(ApplicationConsts.PRICELIST,customerVO.getPricelistCodeSal01SAL07());
			}
 */


		// customer lookup...
		customerDataLocator.setGridMethodName("loadCustomers");
		customerDataLocator.setValidationMethodName("validateCustomerCode");

		controlCustomerCode.setLookupController(customerController);
		controlCustomerCode.setControllerMethodName("getCustomersList");
		customerController.setLookupDataLocator(customerDataLocator);
		customerController.setFrameTitle("customers");
		customerController.setLookupValueObjectClassName("org.jallinone.sales.customers.java.GridCustomerVO");
/*
		customerController.addLookup2ParentLink("companyCodeSys01REG04", "companyCodeSys01DOC01");
		customerController.addLookup2ParentLink("customerCodeSAL07","customerCodeSAL07");
		customerController.addLookup2ParentLink("progressiveREG04","progressiveReg04DOC01");
		customerController.addLookup2ParentLink("name_1REG04", "name_1REG04");
		customerController.addLookup2ParentLink("name_2REG04", "name_2REG04");
		customerController.addLookup2ParentLink("paymentCodeReg10SAL07", "paymentCodeReg10DOC01");
		customerController.addLookup2ParentLink("paymentDescriptionSAL07", "paymentDescriptionDOC01");
		customerController.addLookup2ParentLink("vatCodeReg01SAL07", "customerVatCodeReg01DOC01");
*/
		customerController.setAllColumnVisible(false);
		customerController.setOnInvalidCode(customerController.ON_INVALID_CODE_RESTORE_LAST_VALID_CODE);
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
		customerController.setHeaderColumnName("name_1REG04", "corporateName1");
		customerController.setHeaderColumnName("name_2REG04", "corporateName2");
		customerController.setPreferredWidthColumn("name_1REG04", 200);
		customerController.setPreferredWidthColumn("name_2REG04", 150);
		customerController.setFramePreferedSize(new Dimension(750,500));
		customerDataLocator.getLookupFrameParams().put(ApplicationConsts.FILTER_COMPANY_FOR_INSERT,"DOC01_ORDERS");
		customerDataLocator.getLookupValidationParameters().put(ApplicationConsts.FILTER_COMPANY_FOR_INSERT,"DOC01_ORDERS");
		customerController.addLookupListener(new LookupListener() {

			public void codeValidated(boolean validated) {}

			public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
				if (customerController.getLookupVO()==null) {
				}
				else {
					GridCustomerVO vo = (GridCustomerVO)customerController.getLookupVO();
					Response res = ClientUtils.getData("loadCustomer",new CustomerPK(
							vo.getCompanyCodeSys01REG04(),
							vo.getProgressiveREG04(),
							ApplicationConsts.SUBJECT_PEOPLE_CUSTOMER
					));
					if (!res.isError()) {
						customerVO = (PeopleCustomerVO)((VOResponse)res).getVo();
						controlName1.setText(customerVO.getName_1REG04());
						controlName2.setText(customerVO.getName_2REG04());
						controlCustomer.setText(customerVO.getCustomerCodeSAL07());

						if (customerVO.getPricelistCodeSal01SAL07()!=null) {
							detailSaleDocVO.setPricelistCodeSal01DOC01(customerVO.getPricelistCodeSal01SAL07());
							detailSaleDocVO.setPricelistDescriptionDOC01(customerVO.getPricelistDescriptionSYS10());
						}
					}
				}
			}

			public void beforeLookupAction(ValueObject parentVO) {
				customerDataLocator.getLookupFrameParams().put(ApplicationConsts.SUBJECT_TYPE,ApplicationConsts.SUBJECT_PEOPLE_CUSTOMER);
			}

			public void forceValidate() {}

		});
		controlCustomerCode.setValue(customerVO.getCustomerCodeSAL07());
		controlName1.setText(customerVO.getName_1REG04());
		controlName2.setText(customerVO.getName_2REG04());


		// payment lookup...
		payDataLocator.setGridMethodName("loadPayments");
		payDataLocator.setValidationMethodName("validatePaymentCode");

		controlPaymentCode.setLookupController(payController);
		controlPaymentCode.setControllerMethodName("getPaymentsList");
		payController.setOnInvalidCode(customerController.ON_INVALID_CODE_RESTORE_LAST_VALID_CODE);
		payController.setLookupDataLocator(payDataLocator);
		payController.setFrameTitle("payments");
		payController.setLookupValueObjectClassName("org.jallinone.registers.payments.java.PaymentVO");
		payController.addLookup2ParentLink("paymentCodeREG10", "paymentCodeReg10DOC01");
		payController.addLookup2ParentLink("descriptionSYS10","paymentDescriptionDOC01");
		payController.setAllColumnVisible(false);
		payController.setVisibleColumn("paymentCodeREG10", true);
		payController.setVisibleColumn("descriptionSYS10", true);
		payController.setPreferredWidthColumn("paymentCodeREG10",100);
		payController.setPreferredWidthColumn("descriptionSYS10",250);
		payController.setFramePreferedSize(new Dimension(400,300));

		payController.addLookupListener(new LookupListener() {

			public void codeValidated(boolean validated) {}

			public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
				PaymentVO vo = (PaymentVO)payController.getLookupVO();
				if (payController.getLookupVO()==null) {
				}
				else {
					 detailSaleDocVO.setPaymentCodeReg10DOC01(vo.getPaymentCodeREG10());
					 detailSaleDocVO.setPaymentDescriptionDOC01(vo.getDescriptionSYS10());
					 controlPayDescr.setText(detailSaleDocVO.getPaymentDescriptionDOC01());
				}
			}

			public void beforeLookupAction(ValueObject parentVO) {
				GridCustomerVO vo = (GridCustomerVO)customerController.getLookupVO();
				customerDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01REG04());
				customerDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01REG04());
				customerDataLocator.getLookupFrameParams().put(ApplicationConsts.SUBJECT_TYPE,ApplicationConsts.SUBJECT_PEOPLE_CUSTOMER);
			}

			public void forceValidate() {}

		});
		controlPaymentCode.setValue(customerVO.getPaymentCodeReg10SAL07());
		controlPayDescr.setText(customerVO.getPaymentDescriptionSYS10());



		// listen for key events...
		JPanel content = (JPanel)this.getContentPane();
		content.registerKeyboardAction(
			new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (buttonBack.isEnabled())
				buttonBack_actionPerformed(e);
			}

		},
			KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
			JComponent.WHEN_IN_FOCUSED_WINDOW
		);
		content.registerKeyboardAction(
			new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (bi.isEnabled())
				bi_actionPerformed(e);
			}

		},
			KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
			JComponent.WHEN_IN_FOCUSED_WINDOW
		);
		content.registerKeyboardAction(
			new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (buttonDel.isEnabled())
				buttonDel_actionPerformed(e);
			}

		},
			KeyStroke.getKeyStroke(KeyEvent.VK_1, 0),
			JComponent.WHEN_IN_FOCUSED_WINDOW
		);
		content.registerKeyboardAction(
			new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (buttonQty.isEnabled())
					buttonQty_actionPerformed(e);
				}

			},
			KeyStroke.getKeyStroke(KeyEvent.VK_2, 0),
			JComponent.WHEN_IN_FOCUSED_WINDOW
		);
		content.registerKeyboardAction(
			new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (buttonDiscount.isEnabled())
					buttonDiscount_actionPerformed(e);
				}

			},
			KeyStroke.getKeyStroke(KeyEvent.VK_3, 0),
			JComponent.WHEN_IN_FOCUSED_WINDOW
		);
		content.registerKeyboardAction(
			new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (buttonCustomer.isEnabled())
					buttonCustomer_actionPerformed(e);
				}

			},
			KeyStroke.getKeyStroke(KeyEvent.VK_4, 0),
			JComponent.WHEN_IN_FOCUSED_WINDOW
		);
		content.registerKeyboardAction(
			new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (buttonStart.isEnabled())
					buttonStart_actionPerformed(e);
				}

			},
			KeyStroke.getKeyStroke(KeyEvent.VK_5, 0),
			JComponent.WHEN_IN_FOCUSED_WINDOW
		);
		content.registerKeyboardAction(
			new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (buttonClose.isEnabled())
					buttonClose_actionPerformed(e);
				}

			},
			KeyStroke.getKeyStroke(KeyEvent.VK_6, 0),
			JComponent.WHEN_IN_FOCUSED_WINDOW
		);
		content.registerKeyboardAction(
			new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (buttonExit.isEnabled())
					buttonExit_actionPerformed(e);
				}

			},
			KeyStroke.getKeyStroke(KeyEvent.VK_7, 0),
			JComponent.WHEN_IN_FOCUSED_WINDOW
		);
/*
		content.registerKeyboardAction(
			new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (b8.isEnabled())
					b8_actionPerformed(e);
				}

			},
			KeyStroke.getKeyStroke(KeyEvent.VK_8, 0),
			JComponent.WHEN_IN_FOCUSED_WINDOW
		);
		content.registerKeyboardAction(
			new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (b9.isEnabled())
					b9_actionPerformed(e);
				}

			},
			KeyStroke.getKeyStroke(KeyEvent.VK_9, 0),
			JComponent.WHEN_IN_FOCUSED_WINDOW
		);
		content.registerKeyboardAction(
			new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (b0.isEnabled())
					b0_actionPerformed(e);
				}

			},
			KeyStroke.getKeyStroke(KeyEvent.VK_0, 0),
			JComponent.WHEN_IN_FOCUSED_WINDOW
		);
*/

		controlQty.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==e.VK_ESCAPE && buttonBack.isEnabled())
					buttonBack_actionPerformed(null);
			}
		});
		controlItemValDiscount.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==e.VK_ESCAPE && buttonBack.isEnabled())
					buttonBack_actionPerformed(null);
			}
		});
		controlValDiscountTotal.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==e.VK_ESCAPE && buttonBack.isEnabled())
					buttonBack_actionPerformed(null);
			}
		});
		controlValPaidTotal.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==e.VK_ESCAPE && buttonBack.isEnabled())
					buttonBack_actionPerformed(null);
			}
		});


	} // end init method



	private void checkBarcode() {
		if (controlBarcode.getValue()==null || controlBarcode.getValue().equals("")) {
			if (!controlBarcode.hasFocus())
				controlBarcode.requestFocus();
			return;
		}
		try {
			// validate variants barcode...
			VariantBarcodeVO barcodeVO = null;
			LookupValidationParams pars = new LookupValidationParams((String)controlBarcode.getValue(),new HashMap());
			pars.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,customerVO.getCompanyCodeSys01REG04());
			Response res = ClientUtils.getData("validateVariantBarcode",pars);
			if (!res.isError()) {
				java.util.List rows = ( (VOListResponse) res).getRows();
				if (rows.size() == 1) {
					// found variants barcode: pre-fill code and qty in variants matrix...
					barcodeVO = (VariantBarcodeVO)rows.get(0);
				}
			}

			if (barcodeVO!=null) {
				pars = new LookupValidationParams(barcodeVO.getItemCodeItm01ITM22(),new HashMap());
				pars.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,customerVO.getCompanyCodeSys01REG04());
				pars.getLookupValidationParameters().put(ApplicationConsts.PRICELIST,customerVO.getPricelistCodeSal01SAL07());
			}
			else {
				pars = new LookupValidationParams((String)controlBarcode.getValue(),new HashMap());
				pars.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,customerVO.getCompanyCodeSys01REG04());
				pars.getLookupValidationParameters().put(ApplicationConsts.PRICELIST,customerVO.getPricelistCodeSal01SAL07());
				pars.getLookupValidationParameters().put(ApplicationConsts.VALIDATE_BARCODE,Boolean.TRUE);
			}

			res = ClientUtils.getData("validatePriceItemCode",pars);
			PriceItemVO priceItemVO = null;
			if (!res.isError()) {
				java.util.List vos = ((VOListResponse)res).getRows();
				if (vos.size()==1)
					priceItemVO = (PriceItemVO)vos.get(0);
				else
					priceItemVO = null;
			}
			else
				priceItemVO = null;

			if (priceItemVO!=null) {
				addRow(barcodeVO, priceItemVO);
				controlBarcode.setValue(null);
			}
			else if (!controlBarcode.hasFocus())
				controlBarcode.requestFocus();

		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}




	private void addRow(VariantBarcodeVO barcodeVO,PriceItemVO priceItemVO) {
		ArrayList sn = new ArrayList();
		DetailSaleDocRowVO vo = new DetailSaleDocRowVO();
		if (barcodeVO!=null) {
			vo.setVariantCodeItm11DOC02(barcodeVO.getVariantCodeItm11ITM22());
			vo.setVariantCodeItm12DOC02(barcodeVO.getVariantCodeItm12ITM22());
			vo.setVariantCodeItm13DOC02(barcodeVO.getVariantCodeItm13ITM22());
			vo.setVariantCodeItm14DOC02(barcodeVO.getVariantCodeItm14ITM22());
			vo.setVariantCodeItm15DOC02(barcodeVO.getVariantCodeItm15ITM22());
			vo.setVariantTypeItm06DOC02(barcodeVO.getVariantTypeItm06ITM22());
			vo.setVariantTypeItm07DOC02(barcodeVO.getVariantTypeItm07ITM22());
			vo.setVariantTypeItm08DOC02(barcodeVO.getVariantTypeItm08ITM22());
			vo.setVariantTypeItm09DOC02(barcodeVO.getVariantTypeItm09ITM22());
			vo.setVariantTypeItm10DOC02(barcodeVO.getVariantTypeItm10ITM22());
		}
		else {
			vo.setVariantCodeItm11DOC02(ApplicationConsts.JOLLY);
			vo.setVariantCodeItm12DOC02(ApplicationConsts.JOLLY);
			vo.setVariantCodeItm13DOC02(ApplicationConsts.JOLLY);
			vo.setVariantCodeItm14DOC02(ApplicationConsts.JOLLY);
			vo.setVariantCodeItm15DOC02(ApplicationConsts.JOLLY);
			vo.setVariantTypeItm06DOC02(ApplicationConsts.JOLLY);
			vo.setVariantTypeItm07DOC02(ApplicationConsts.JOLLY);
			vo.setVariantTypeItm08DOC02(ApplicationConsts.JOLLY);
			vo.setVariantTypeItm09DOC02(ApplicationConsts.JOLLY);
			vo.setVariantTypeItm10DOC02(ApplicationConsts.JOLLY);
		}

		// check for variants level price...
		if (priceItemVO.getUseVariant1ITM01().equals(Boolean.TRUE) ||
				priceItemVO.getUseVariant2ITM01().equals(Boolean.TRUE) ||
				priceItemVO.getUseVariant3ITM01().equals(Boolean.TRUE) ||
				priceItemVO.getUseVariant4ITM01().equals(Boolean.TRUE) ||
				priceItemVO.getUseVariant5ITM01().equals(Boolean.TRUE)) {
			Response res = ClientUtils.getData("loadVariantsPrice",new Object[]{barcodeVO,customerVO.getPricelistCodeSal01SAL07()});
			if (!res.isError()) {
				java.util.List rows = ((VOListResponse)res).getRows();
				if (rows.size()==1) {
					VariantsPriceVO vpVO = (VariantsPriceVO)rows.get(0);
					priceItemVO.setValueSAL02(vpVO.getValueSAL11());
				}
			}
		}


		vo.setCompanyCodeSys01DOC02(priceItemVO.getCompanyCodeSys01());
		Calendar cal = Calendar.getInstance();
		vo.setDocYearDOC02(new BigDecimal(cal.get(cal.YEAR)));
		vo.setDocTypeDOC02(ApplicationConsts.SALE_DESK_DOC_TYPE);
		vo.setDeliveryDateDOC02(new java.sql.Date(System.currentTimeMillis()));
		vo.setCurrencyCodeReg03DOC01(customerVO.getCurrencyCodeReg03SAL01());
		vo.setProgressiveHie01DOC02(priceItemVO.getProgressiveHie01ITM01());
		vo.setDecimalsReg02DOC02(priceItemVO.getDecimalsREG02());
		vo.setDescriptionSYS10(priceItemVO.getItemDescriptionSYS10());
		vo.setItemCodeItm01DOC02(priceItemVO.getItemCodeItm01());
		vo.setMinSellingQtyItm01DOC02(priceItemVO.getMinSellingQtyITM01());
		vo.setMinSellingQtyUmCodeReg02DOC02(priceItemVO.getMinSellingQtyUmCodeReg02ITM01());
		vo.setProgressiveHie01ITM01(priceItemVO.getProgressiveHie01ITM01());
		vo.setProgressiveHie02DOC02(warehouseVO.getProgressiveHie02WAR01());
		vo.setQtyDOC02(new BigDecimal(1));
		vo.setSerialNumbers(sn);
		vo.setStartDateSal02DOC02(priceItemVO.getStartDateSAL02());
		vo.setEndDateSal02DOC02(priceItemVO.getEndDateSAL02());
		vo.setValueReg01DOC02(priceItemVO.getValueREG01());
		vo.setValueSal02DOC02(priceItemVO.getValueSAL02());
		vo.setVatCodeItm01DOC02(priceItemVO.getVatCodeReg01ITM01());
		vo.setVatDescriptionDOC02(priceItemVO.getVatDescriptionSYS10());
		vo.setDeductibleReg01DOC02(priceItemVO.getDeductibleREG01());

		// check if there already exist another row for the same item + variants...
		DetailSaleDocRowVO oldVO = null;
		int rowFound = -1;
		for(int i=0;i<grid.getVOListTableModel().getRowCount();i++) {
			oldVO = (DetailSaleDocRowVO)grid.getVOListTableModel().getObjectForRow(i);
			if (oldVO.getCompanyCodeSys01DOC02().equals(vo.getCompanyCodeSys01DOC02()) &&
					oldVO.getItemCodeItm01DOC02().equals(vo.getItemCodeItm01DOC02()) &&
					oldVO.getVariantCodeItm11DOC02().equals(vo.getVariantCodeItm11DOC02()) &&
					oldVO.getVariantCodeItm12DOC02().equals(vo.getVariantCodeItm12DOC02()) &&
					oldVO.getVariantCodeItm13DOC02().equals(vo.getVariantCodeItm13DOC02()) &&
					oldVO.getVariantCodeItm14DOC02().equals(vo.getVariantCodeItm14DOC02()) &&
					oldVO.getVariantCodeItm15DOC02().equals(vo.getVariantCodeItm15DOC02()) &&
					oldVO.getVariantTypeItm06DOC02().equals(vo.getVariantTypeItm06DOC02()) &&
					oldVO.getVariantTypeItm07DOC02().equals(vo.getVariantTypeItm07DOC02()) &&
					oldVO.getVariantTypeItm08DOC02().equals(vo.getVariantTypeItm08DOC02()) &&
					oldVO.getVariantTypeItm09DOC02().equals(vo.getVariantTypeItm09DOC02()) &&
					oldVO.getVariantTypeItm10DOC02().equals(vo.getVariantTypeItm10DOC02())) {
				rowFound = i;
				break;
			}
		}
		int selRow = rowFound;
		if (rowFound!=-1) {
			DetailSaleDocRowVO voFound = (DetailSaleDocRowVO)grid.getVOListTableModel().getObjectForRow(rowFound);
			vo = voFound;
			vo.setQtyDOC02(vo.getQtyDOC02().add(new BigDecimal(1)));
		}
		else {
			grid.getVOListTableModel().addObject(vo);
			selRow = grid.getVOListTableModel().getRowCount()-1;
		}

		updateRow(vo);
		grid.repaint();
		grid.setRowSelectionInterval(selRow,selRow);

		updateContext();
		updateTotals();

		new Thread() {
			public void run() {
				ArrayList items = new ArrayList();
				DetailSaleDocRowVO rowVO = null;
				for(int i=0;i<grid.getVOListTableModel().getRowCount();i++) {
					rowVO = (DetailSaleDocRowVO)grid.getVOListTableModel().getObjectForRow(i);
					items.add(rowVO.getItemCodeItm01DOC02());
				}
				Response res = ClientUtils.getData("loadItemsSoldToOtherCustomers",new Object[]{defaultCompanyCodeSys01,items});
				if (!res.isError()) {
					java.util.List rows = ((VOListResponse)res).getRows();
					if (rows.size()==0)
							labelSuggstmt.setText("");
					else {
						String aux = "";
						ItemSoldToOtherCustomersVO vo = null;
						for(int i=0;i<rows.size();i++) {
							vo = (ItemSoldToOtherCustomersVO)rows.get(i);
							aux += vo.getCustomersNr().intValue()+" "+vo.getItemDescriptionSY10()+"\n";
						}
						aux = aux.substring(0,aux.length()-1);
						labelSuggstmt.setText(
							ClientSettings.getInstance().getResources().getResource("customers that have bought these items also bought")+"\n"+
							aux
						);
							labelSuggstmt.setMinimumSize(new Dimension(labelSuggstmt.getWidth(),15*rows.size()+20));
							labelSuggstmt.setSize(labelSuggstmt.getWidth(),15*rows.size()+20);
					}
				}
			}
		}.start();

		controlBarcode.requestFocus();
	}


	private void updateRow(DetailSaleDocRowVO vo) {
		if (vo.getTotalDiscountDOC02()==null)
			vo.setTotalDiscountDOC02(new BigDecimal(0));
		vo.setTaxableIncomeDOC02(vo.getValueSal02DOC02().multiply(vo.getQtyDOC02()).subtract(vo.getTotalDiscountDOC02()));
		vo.setVatValueDOC02(vo.getTaxableIncomeDOC02().multiply(vo.getValueReg01DOC02()).divide(new BigDecimal(100),currVO.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP));
		vo.setValueDOC02(vo.getTaxableIncomeDOC02().add(vo.getVatValueDOC02()));
	}


	private void jbInit() throws Exception {

		labelCustomerCode.setText("customer");
		controlCustomerCode.setMaxCharacters(20);
		controlCustomerCode.setCodBoxVisible(false);
		labelPayment.setText("payment");
		controlName1.setEnabled(false);
		controlName2.setEnabled(false);
		controlName1.setColumns(10);
		controlName2.setColumns(10);
		controlPaymentCode.setMaxCharacters(20);
		controlPaymentCode.setCodBoxVisible(false);

		this.getContentPane().setLayout(borderLayout1);
		mainPanel.setLayout(gridBagLayout1);
		topPanel.setLayout(gridBagLayout2);
		buttonsPanel.setLayout(flowLayout1);
		flowLayout1.setAlignment(FlowLayout.LEFT);
		labelUsername.setLabel("usernameSYS03");
		controlUser.setColumns(10);
		controlUser.setEnabled(false);
		labelComp.setLabel("companyCodeSYS01");
		controlComp.setColumns(10);
		controlComp.setEnabled(false);
		topPanel.setBorder(BorderFactory.createEtchedBorder());
		buttonsPanel.setBorder(BorderFactory.createEtchedBorder());
		buttonExit.addActionListener(new PosFrame_buttonExit_actionAdapter(this));
		keysPanel.setLayout(gridBagLayout3);
		b7.setFont(new java.awt.Font("Dialog", 1, 14));
		b7.setMinimumSize(new Dimension(50, 50));
		b7.setPreferredSize(new Dimension(50, 50));
		b7.setText("7");
		b7.addActionListener(new PosFrame_b7_actionAdapter(this));
		b4.setFont(new java.awt.Font("Dialog", 1, 14));
		b4.setMinimumSize(new Dimension(50, 50));
		b4.setPreferredSize(new Dimension(50, 50));
		b4.setText("4");
		b4.addActionListener(new PosFrame_b4_actionAdapter(this));
		b1.setFont(new java.awt.Font("Dialog", 1, 14));
		b1.setMinimumSize(new Dimension(50, 50));
		b1.setPreferredSize(new Dimension(50, 50));
		b1.setText("1");
		b1.addActionListener(new PosFrame_b1_actionAdapter(this));
		labelCustomer.setLabel("customerCodeSAL07");
		controlCustomer.setColumns(10);
		controlCustomer.setEnabled(false);
		b2.setFont(new java.awt.Font("Dialog", 1, 14));
		b2.setMinimumSize(new Dimension(50, 50));
		b2.setPreferredSize(new Dimension(50, 50));
		b2.setText("2");
		b2.addActionListener(new PosFrame_b2_actionAdapter(this));
		b5.setFont(new java.awt.Font("Dialog", 1, 14));
		b5.setMinimumSize(new Dimension(50, 50));
		b5.setPreferredSize(new Dimension(50, 50));
		b5.setText("5");
		b5.addActionListener(new PosFrame_b5_actionAdapter(this));
		b8.setFont(new java.awt.Font("Dialog", 1, 14));
		b8.setMinimumSize(new Dimension(50, 50));
		b8.setPreferredSize(new Dimension(50, 50));
		b8.setText("8");
		b8.addActionListener(new PosFrame_b8_actionAdapter(this));
		b9.setFont(new java.awt.Font("Dialog", 1, 14));
		b9.setMinimumSize(new Dimension(50, 50));
		b9.setPreferredSize(new Dimension(50, 50));
		b9.setText("9");
		b9.addActionListener(new PosFrame_b9_actionAdapter(this));
		bc.setFont(new java.awt.Font("Dialog", 1, 14));
		bc.setMinimumSize(new Dimension(80, 50));
		bc.setPreferredSize(new Dimension(80, 50));
		bc.setText(ClientSettings.getInstance().getResources().getResource("cancel"));
		bc.addActionListener(new PosFrame_bc_actionAdapter(this));
		b6.setFont(new java.awt.Font("Dialog", 1, 14));
		b6.setMinimumSize(new Dimension(50, 50));
		b6.setPreferredSize(new Dimension(50, 50));
		b6.setText("6");
		b6.addActionListener(new PosFrame_b6_actionAdapter(this));
		b3.setFont(new java.awt.Font("Dialog", 1, 14));
		b3.setMinimumSize(new Dimension(50, 50));
		b3.setPreferredSize(new Dimension(50, 50));
		b3.setText("3");
		b3.addActionListener(new PosFrame_b3_actionAdapter(this));
		b0.setFont(new java.awt.Font("Dialog", 1, 14));
		b0.setMinimumSize(new Dimension(100, 50));
		b0.setPreferredSize(new Dimension(100, 50));
		b0.setText("0");
		b0.addActionListener(new PosFrame_b0_actionAdapter(this));
		bv.setFont(new java.awt.Font("Dialog", 1, 14));
		bv.setMinimumSize(new Dimension(50, 50));
		bv.setPreferredSize(new Dimension(50, 50));
		bv.setText(",");
		bv.addActionListener(new PosFrame_bv_actionAdapter(this));
		bi.setFont(new java.awt.Font("Dialog", 1, 14));
		bi.setMinimumSize(new Dimension(80, 100));
		bi.setPreferredSize(new Dimension(80, 100));
		bi.setText(ClientSettings.getInstance().getResources().getResource("enter"));
		bi.addActionListener(new PosFrame_bi_actionAdapter(this));
		be.setFont(new java.awt.Font("Dialog", 1, 14));
		be.setMinimumSize(new Dimension(80, 50));
		be.setPreferredSize(new Dimension(80, 50));
		be.setText("ESC");
		be.addActionListener(new PosFrame_be_actionAdapter(this));
		itemsPanel.setLayout(gridBagLayout4);
		labelBarcode.setFont(new java.awt.Font("Dialog", 1, 14));
		labelBarcode.setLabel("barcode");
		controlBarcode.setColumns(20);
		controlBarcode.setEnabled(false);
		controlBarcode.setFont(new java.awt.Font("Dialog", 1, 14));

		controlQty.setFont(new java.awt.Font("Dialog", 1, 14));
		controlItemValDiscount.setFont(new java.awt.Font("Dialog", 1, 14));
		controlValDiscountTotal.setFont(new java.awt.Font("Dialog", 1, 14));
		controlValPaidTotal.setFont(new java.awt.Font("Dialog", 1, 14));

		controlBarcode.setMaxCharacters(255);
		colItemDescr.setColumnName("descriptionSYS10");
		colItemDescr.setPreferredWidth(220);
		colQta.setColumnName("qtyDOC02");
		colQta.setPreferredWidth(50);
		grid.setAutoLoadData(false);
		grid.setValueObjectClassName("org.jallinone.sales.documents.java.DetailSaleDocRowVO");
		grid.setVisibleStatusPanel(false);
		colItemCode.setColumnName("itemCodeItm01DOC02");
		colItemCode.setEditableOnInsert(false);
		colItemCode.setPreferredWidth(80);
		colTot.setColumnName("valueDOC02");
		colTot.setPreferredWidth(70);
		colTotDisc.setColumnName("discountValue");
		colTotDisc.setPreferredWidth(70);
		totPanel.setLayout(gridBagLayout5);
		labelSubTot.setLabel("subtotal");
		labelDisc.setLabel("discount");
		labelPaid.setLabel("payed");
		labelTotal.setFont(new java.awt.Font("Dialog", 1, 14));
		labelTotal.setLabel("total");
		labelChange.setFont(new java.awt.Font("Dialog", 1, 14));
		labelChange.setLabel("change");
		controlSubtotal.setColumns(15);
		controlSubtotal.setEnabled(false);
		controlDiscount.setColumns(15);
		controlDiscount.setEnabled(false);
		controlDiscount.addFocusListener(new PosFrame_controlDiscount_focusAdapter(this));
		controlPaid.setColumns(15);
		controlPaid.setEnabled(false);
		controlPaid.addFocusListener(new PosFrame_controlPaid_focusAdapter(this));
		controlTotal.setColumns(15);
		controlTotal.setFont(new java.awt.Font("Dialog", 1, 14));
		controlTotal.setEnabled(false);
		controlChange.setColumns(15);
		controlChange.setFont(new java.awt.Font("Dialog", 1, 14));
		controlChange.setEnabled(false);
		controlDiscountPerc.setEnabled(false);
		controlDiscountPerc.addFocusListener(new PosFrame_controlDiscountPerc_focusAdapter(this));

		buttonBack.setButtonBehavior(Consts.BUTTON_IMAGE_AND_TEXT);
		buttonBack.setEnabled(false);
		buttonBack.setText("ESC back");
		buttonBack.setToolTipText(ClientSettings.getInstance().getResources().getResource("0 back tooltip"));
		buttonBack.setIcon(new ImageIcon(ClientUtils.getImage("back.gif")));
		buttonBack.setMinimumSize(new Dimension(80,50));
		buttonBack.setBorder(BorderFactory.createEmptyBorder());

		buttonDel.setButtonBehavior(Consts.BUTTON_IMAGE_AND_TEXT);
		buttonDel.setEnabled(false);
		buttonDel.setText("1 remove");
		buttonDel.setToolTipText(ClientSettings.getInstance().getResources().getResource("1 remove tooltip"));
		buttonDel.setIcon(new ImageIcon(ClientUtils.getImage("remove.gif")));
		buttonDel.setMinimumSize(new Dimension(80,50));
		buttonDel.setBorder(BorderFactory.createEmptyBorder());

		buttonQty.setButtonBehavior(Consts.BUTTON_IMAGE_AND_TEXT);
		buttonQty.setEnabled(false);
		buttonQty.setText("2 qty");
		buttonQty.setToolTipText(ClientSettings.getInstance().getResources().getResource("2 qty tooltip"));
		buttonQty.setIcon(new ImageIcon(ClientUtils.getImage("warehouse.gif")));
		buttonQty.setMinimumSize(new Dimension(80,50));
		buttonQty.setBorder(BorderFactory.createEmptyBorder());

		buttonDiscount.setButtonBehavior(Consts.BUTTON_IMAGE_AND_TEXT);
		buttonDiscount.setEnabled(false);
		buttonDiscount.setText("3 discount");
		buttonDiscount.setToolTipText(ClientSettings.getInstance().getResources().getResource("3 discount tooltip"));
		buttonDiscount.setIcon(new ImageIcon(ClientUtils.getImage("coins.gif")));
		buttonDiscount.setMinimumSize(new Dimension(80,50));
		buttonDiscount.setBorder(BorderFactory.createEmptyBorder());

		buttonDiscountTotal.setButtonBehavior(Consts.BUTTON_TEXT_ONLY);
		buttonDiscountTotal.setText("discount");

		buttonPaidTotal.setButtonBehavior(Consts.BUTTON_TEXT_ONLY);
		buttonPaidTotal.setText("payed");

		buttonCustomer.setButtonBehavior(Consts.BUTTON_IMAGE_AND_TEXT);
		buttonCustomer.setEnabled(true);
		buttonCustomer.setText("4 customer");
		buttonCustomer.setToolTipText(ClientSettings.getInstance().getResources().getResource("4 customer tooltip"));
		buttonCustomer.setMinimumSize(new Dimension(80,50));
		buttonCustomer.addActionListener(new PosFrame_buttonCustomer_actionAdapter(this));
		buttonCustomer.setIcon(new ImageIcon(ClientUtils.getImage("customers.gif")));
		buttonCustomer.setBorder(BorderFactory.createEmptyBorder());

		buttonStart.setButtonBehavior(Consts.BUTTON_IMAGE_AND_TEXT);
		buttonStart.setText("5 start");
		buttonStart.setToolTipText(ClientSettings.getInstance().getResources().getResource("5 start tooltip"));
		buttonStart.addActionListener(new PosFrame_buttonStart_actionAdapter(this));
		buttonStart.setIcon(new ImageIcon(ClientUtils.getImage("coins.gif")));
		buttonStart.setMinimumSize(new Dimension(90,50));
		buttonStart.setBorder(BorderFactory.createEmptyBorder());

		buttonClose.setButtonBehavior(Consts.BUTTON_IMAGE_AND_TEXT);
		buttonClose.setEnabled(false);
		buttonClose.setText("6 close");
		buttonClose.setToolTipText(ClientSettings.getInstance().getResources().getResource("6 close tooltip"));
		buttonClose.setIcon(new ImageIcon(ClientUtils.getImage("banks.gif")));
		buttonClose.setMinimumSize(new Dimension(90,50));
		buttonClose.setBorder(BorderFactory.createEmptyBorder());

		buttonExit.setButtonBehavior(Consts.BUTTON_IMAGE_AND_TEXT);
		buttonExit.setText("7 exit");
		buttonExit.setToolTipText(ClientSettings.getInstance().getResources().getResource("7 exit tooltip"));
		buttonExit.setIcon(new ImageIcon(ClientUtils.getImage("close.gif")));
		buttonExit.setMinimumSize(new Dimension(90,50));
		buttonExit.setBorder(BorderFactory.createEmptyBorder());

		labelWarehouse.setFont(new java.awt.Font("MS Sans Serif", 0, 11));
		labelWarehouse.setLabel("warehouse");
		controlW.setColumns(10);
		controlW.setEnabled(false);
		controlClock.setBorder(null);
		buttonBack.addActionListener(new PosFrame_buttonBack_actionAdapter(this));
		buttonDel.addActionListener(new PosFrame_buttonDel_actionAdapter(this));
		buttonQty.addActionListener(new PosFrame_buttonQty_actionAdapter(this));
		buttonDiscount.addActionListener(new PosFrame_buttonDiscount_actionAdapter(this));
		buttonDiscountTotal.addActionListener(new PosFrame_buttonDiscountTotal_actionAdapter(this));
		buttonPaidTotal.addActionListener(new PosFrame_buttonPaidTotal_actionAdapter(this));
		buttonClose.addActionListener(new PosFrame_buttonClose_actionAdapter(this));
		controlPayDescr.setColumns(20);
    controlPayDescr.setEnabled(false);
		suggstmtPanel.setLayout(borderLayout2);
		suggstmtPanel.setPreferredSize(new Dimension(400, 40));
		payPanel.setLayout(gridBagLayout6);
    this.getContentPane().add(mainPanel, BorderLayout.CENTER);
		this.getContentPane().add(topPanel, BorderLayout.NORTH);
		topPanel.add(labelUsername,      new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		topPanel.add(controlUser,      new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		topPanel.add(labelComp,       new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 15, 5, 5), 0, 0));
		topPanel.add(controlComp,      new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
		topPanel.add(controlClock,              new GridBagConstraints(8, 0, 1, 1, 1.0, 0.0
						,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 120, 0));
		topPanel.add(labelCustomer,      new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 15, 5, 5), 0, 0));
		mainPanel.add(itemsPanel,             new GridBagConstraints(0, 0, 2, 4, 1.0, 1.0
						,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
		mainPanel.add(keysPanel,        new GridBagConstraints(2, 0, 1, 2, 0.0, 0.0
						,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		keysPanel.add(b7,     new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
						,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		keysPanel.add(b4,     new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
						,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		keysPanel.add(b1,     new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
						,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		topPanel.add(controlCustomer,    new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
		topPanel.add(labelWarehouse,    new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 15, 5, 5), 0, 0));
		keysPanel.add(b2,    new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
						,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		keysPanel.add(b5,    new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
						,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		keysPanel.add(b8,    new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
						,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		keysPanel.add(b9,    new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
						,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		keysPanel.add(bc,    new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
						,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		keysPanel.add(b6,    new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
						,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		keysPanel.add(b3,    new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
						,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		keysPanel.add(be,    new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
						,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		keysPanel.add(b0,    new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0
						,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		keysPanel.add(bv,   new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
						,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		keysPanel.add(bi,    new GridBagConstraints(3, 2, 1, 2, 0.0, 0.0
						,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		mainPanel.add(totPanel,       new GridBagConstraints(2, 2, 1, 3, 0.0, 0.0
						,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		itemsPanel.add(grid,         new GridBagConstraints(0, 1, 4, 1, 1.0, 1.0
						,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
		labelSuggstmt.setMinimumSize(new Dimension(400,40));
		labelSuggstmt.setPreferredSize(new Dimension(400,40));
		labelSuggstmt.setMinimumSize(new Dimension(400,40));

		grid.getColumnContainer().add(colItemCode, null);
		grid.getColumnContainer().add(colItemDescr, null);
		grid.getColumnContainer().add(colQta, null);
		grid.getColumnContainer().add(colTotDisc, null);
		grid.getColumnContainer().add(colTot, null);
		labelQty.setText("saleQtyDOC02");
		labelQty.setFont(new java.awt.Font("Dialog", 1, 14));
		labelQty.setFont(new java.awt.Font("Dialog", 1, 14));
		labelItemValDiscount.setFont(new java.awt.Font("Dialog", 1, 14));
		labelValDiscountTotal.setFont(new java.awt.Font("Dialog", 1, 14));
		labelValPaidTotal.setFont(new java.awt.Font("Dialog", 1, 14));
		labelItemPercDiscount.setText("discount perc");
		labelItemValDiscount.setText("discount value");
		labelValDiscountTotal.setText("discount on total value");
		labelValPaidTotal.setText("payed");
		itemsPanel.add(voidPanel,     new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0
						,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		totPanel.add(labelSubTot,    new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		totPanel.add(buttonDiscountTotal,     new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		totPanel.add(buttonPaidTotal,     new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		totPanel.add(labelTotal,     new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		totPanel.add(labelChange,     new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		totPanel.add(controlSubtotal,    new GridBagConstraints(2, 2, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		totPanel.add(controlDiscount,    new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		totPanel.add(panControlDiscountPerc,    new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		totPanel.add(controlPaid,    new GridBagConstraints(2, 4, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		totPanel.add(controlTotal,    new GridBagConstraints(2, 5, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		totPanel.add(controlChange,    new GridBagConstraints(2, 6, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		totPanel.add(labelPayment,     new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
		totPanel.add(void3Panel,    new GridBagConstraints(0, 0, 2, 1, 0.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    totPanel.add(payPanel,    new GridBagConstraints(2, 1, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    payPanel.add(controlPayDescr,      new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 5), 0, 0));
    payPanel.add(controlPaymentCode,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		mainPanel.add(suggstmtPanel,      new GridBagConstraints(0, 4, 1, 1, 1.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 0, 5), 0, 0));
		suggstmtPanel.add(labelSuggstmt, BorderLayout.CENTER);
		topPanel.add(controlW,  new GridBagConstraints(7, 0, 1, 1, 0.0, 0.0
						,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
		this.getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
		buttonsPanel.add(buttonBack, null);
		buttonsPanel.add(buttonDel, null);
		buttonsPanel.add(buttonQty, null);
		buttonsPanel.add(buttonDiscount, null);
		buttonsPanel.add(buttonCustomer, null);
		buttonsPanel.add(buttonStart, null);
		buttonsPanel.add(buttonClose, null);
		buttonsPanel.add(buttonExit, null);
	}

	void buttonExit_actionPerformed(ActionEvent e) {

		Object[] opt = new Object[]{
				ClientSettings.getInstance().getResources().getResource("yes"),
				ClientSettings.getInstance().getResources().getResource("no")
		};
		if (JOptionPane.showOptionDialog(
				this,
				ClientSettings.getInstance().getResources().getResource("are you sure to quit"),
				ClientSettings.getInstance().getResources().getResource("quit window"),
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				opt,
				opt[0]
		)==0)  {
			setVisible(false);
			dispose();
		}

	}


	private void updateContext() {
		if (state==START_SALE) {
			buttonBack.setEnabled(false);
			buttonDel.setEnabled(false);
			buttonQty.setEnabled(false);
			buttonDiscount.setEnabled(false);
			buttonCustomer.setEnabled(true);
			buttonStart.setEnabled(true);
			buttonClose.setEnabled(false);
			buttonExit.setEnabled(true);

			Calendar cal = Calendar.getInstance();
			detailSaleDocVO = new DetailSaleDocVO();
			detailSaleDocVO.setCompanyCodeSys01DOC01(customerVO.getCompanyCodeSys01REG04());
			detailSaleDocVO.setCustomerCodeSAL07(customerVO.getCustomerCodeSAL07());
			detailSaleDocVO.setDocYearDOC01(new BigDecimal(cal.get(cal.YEAR)));
			detailSaleDocVO.setDocDateDOC01(new java.sql.Date(System.currentTimeMillis()));
			detailSaleDocVO.setDocTypeDOC01(ApplicationConsts.SALE_DESK_DOC_TYPE);
			detailSaleDocVO.setDocStateDOC01(ApplicationConsts.OPENED);
			detailSaleDocVO.setWarehouseCodeWar01DOC01(warehouseVO.getWarehouseCodeWAR01());
			detailSaleDocVO.setDescriptionWar01DOC01(warehouseVO.getDescriptionWAR01());
			detailSaleDocVO.setProgressiveHie02WAR01(warehouseVO.getProgressiveHie02WAR01());
			detailSaleDocVO.setProgressiveHie01HIE02(warehouseVO.getProgressiveHie01HIE02());
			detailSaleDocVO.setProgressiveReg04DOC01(customerVO.getProgressiveREG04());
			detailSaleDocVO.setName_1REG04(customerVO.getName_1REG04());
			detailSaleDocVO.setName_2REG04(customerVO.getName_2REG04());
			detailSaleDocVO.setPaymentCodeReg10DOC01(customerVO.getPaymentCodeReg10SAL07());
			detailSaleDocVO.setPaymentDescriptionDOC01(customerVO.getPaymentDescriptionSYS10());
			detailSaleDocVO.setCustomerVatCodeReg01DOC01(customerVO.getVatCodeReg01SAL07());
			detailSaleDocVO.setPricelistCodeSal01DOC01(customerVO.getPricelistCodeSal01SAL07());
			detailSaleDocVO.setCurrencyCodeReg03DOC01(currVO.getCurrencyCodeREG03());
			detailSaleDocVO.setPricelistDescriptionDOC01(customerVO.getPricelistDescriptionSYS10());

		}
		else if (state==INS_BARCODE) {

			controlBarcode.setEnabled(true);

			removeItemsPanelContent();
			itemsPanel.add(labelBarcode,          new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
							,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			itemsPanel.add(controlBarcode,        new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
									,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
			itemsPanel.revalidate();
			itemsPanel.repaint();

			if (grid.getVOListTableModel().getRowCount()==0)
				buttonBack.setEnabled(false);
			else
				buttonBack.setEnabled(true);

			buttonDel.setEnabled(false);
			buttonQty.setEnabled(false);
			buttonDiscount.setEnabled(false);
			buttonCustomer.setEnabled(false);
			buttonStart.setEnabled(false);
			buttonClose.setEnabled(false);
			buttonExit.setEnabled(false);

			SwingUtilities.invokeLater(new Runnable() {

				public void run() {
					controlBarcode.requestFocus();
				}

			});
		}
		else if (state==OTHER_COMMANDS) {
			buttonBack.setEnabled(true);
			buttonDel.setEnabled(true);
			buttonQty.setEnabled(true);
			buttonDiscount.setEnabled(true);
			buttonCustomer.setEnabled(false);
			buttonClose.setEnabled(true);
			buttonExit.setEnabled(false);
		}
		else if (state==INS_QTY) {

			removeItemsPanelContent();
			itemsPanel.add(labelQty,          new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
							,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			itemsPanel.add(controlQty,        new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
									,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
			itemsPanel.revalidate();
			itemsPanel.repaint();

			buttonBack.setEnabled(true);
			buttonDel.setEnabled(false);
			buttonQty.setEnabled(false);
			buttonDiscount.setEnabled(false);
			buttonCustomer.setEnabled(false);
			buttonClose.setEnabled(false);
			buttonExit.setEnabled(false);

			DetailSaleDocRowVO vo = (DetailSaleDocRowVO)grid.getVOListTableModel().getObjectForRow(grid.getSelectedRow());
			controlQty.setValue(vo.getQtyDOC02());
			((JTextField)controlQty.getBindingComponent()).selectAll();

			SwingUtilities.invokeLater(new Runnable() {

				public void run() {
					controlQty.requestFocus();
				}

			});

		}
		else if (state==INS_DISCOUNT) {

			removeItemsPanelContent();
			itemsPanel.add(labelItemValDiscount,          new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
							,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			itemsPanel.add(controlItemValDiscount,        new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
									,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
			itemsPanel.revalidate();
			itemsPanel.repaint();

			buttonBack.setEnabled(true);
			buttonDel.setEnabled(false);
			buttonQty.setEnabled(false);
			buttonDiscount.setEnabled(false);
			buttonCustomer.setEnabled(false);
			buttonClose.setEnabled(false);
			buttonExit.setEnabled(false);

			DetailSaleDocRowVO vo = (DetailSaleDocRowVO)grid.getVOListTableModel().getObjectForRow(grid.getSelectedRow());
			controlItemValDiscount.setValue(vo.getDiscountValue());

			SwingUtilities.invokeLater(new Runnable() {

				public void run() {
					controlItemValDiscount.requestFocus();
				}

			});
		}
		else if (state==INS_DISCOUNT_TOTAL) {

			removeItemsPanelContent();
			itemsPanel.add(labelValDiscountTotal,          new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
							,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			itemsPanel.add(controlValDiscountTotal,        new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
									,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
			itemsPanel.revalidate();
			itemsPanel.repaint();

			buttonBack.setEnabled(true);
			buttonDel.setEnabled(false);
			buttonQty.setEnabled(false);
			buttonDiscount.setEnabled(false);
			buttonCustomer.setEnabled(false);
			buttonClose.setEnabled(false);
			buttonExit.setEnabled(false);

			controlBarcode.setEnabled(false);

			SwingUtilities.invokeLater(new Runnable() {

				public void run() {
					controlValDiscountTotal.requestFocus();
				}

			});
		}
		else if (state==INS_PAID_TOTAL) {

			removeItemsPanelContent();
			itemsPanel.add(labelValPaidTotal,          new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
							,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			itemsPanel.add(controlValPaidTotal,        new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
									,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
			itemsPanel.revalidate();
			itemsPanel.repaint();

			buttonBack.setEnabled(true);
			buttonDel.setEnabled(false);
			buttonQty.setEnabled(false);
			buttonDiscount.setEnabled(false);
			buttonCustomer.setEnabled(false);
			buttonClose.setEnabled(false);
			buttonExit.setEnabled(false);

			controlBarcode.setEnabled(false);

			SwingUtilities.invokeLater(new Runnable() {

				public void run() {
					PosFrame.this.updateTotalFromDiscount();
					controlValPaidTotal.setValue(controlTotal.getValue());
					controlValPaidTotal.requestFocus();
				}

			});
		}
		else if (state==INS_CUSTOMER) {

			removeItemsPanelContent();
			itemsPanel.add(labelCustomerCode,          new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
							,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			itemsPanel.add(controlCustomerCode,        new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
							,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
			itemsPanel.add(controlName1,       new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0
							,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
			itemsPanel.add(controlName2,       new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0
							,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
			itemsPanel.revalidate();
			itemsPanel.repaint();

			buttonBack.setEnabled(true);
			buttonDel.setEnabled(false);
			buttonQty.setEnabled(false);
			buttonDiscount.setEnabled(false);
			buttonCustomer.setEnabled(false);
			buttonClose.setEnabled(false);
			buttonExit.setEnabled(false);

			SwingUtilities.invokeLater(new Runnable() {

				public void run() {
					controlCustomerCode.requestFocus();
				}

			});

		}
		else {
			buttonBack.setEnabled(false);
			buttonDel.setEnabled(false);
			buttonQty.setEnabled(false);
			buttonDiscount.setEnabled(false);
			buttonCustomer.setEnabled(false);
			buttonClose.setEnabled(false);
			buttonExit.setEnabled(false);
		}

	}


	private void removeItemsPanelContent() {
		ArrayList compsToRemove = new ArrayList();
		for(int i=0;i<itemsPanel.getComponentCount();i++)
			if (!itemsPanel.getComponent(i).equals(voidPanel) &&
					!itemsPanel.getComponent(i).equals(grid))
				compsToRemove.add(itemsPanel.getComponent(i));
		for(int i=0;i<compsToRemove.size();i++)
			itemsPanel.remove((Component)compsToRemove.get(i));
	}


	void controlPaid_focusLost(FocusEvent e) {
		updateTotalFromPaid();
	}

	void updateTotalFromPaid() {
		BigDecimal total = controlTotal.getBigDecimal();
		BigDecimal paid = controlPaid.getBigDecimal();
		if (total != null && paid != null) {
			if (paid.doubleValue() < total.doubleValue()) {
				controlPaid.setValue(null);
				controlChange.setValue(null);
			} else {
				controlChange.setValue(paid.subtract(total));
			}
		}
	}

	void controlDiscount_focusLost(FocusEvent e) {
		updateDiscountPerc();
		updateTotalFromDiscount();
	}

	void controlDiscountPerc_focusLost(FocusEvent e) {
		BigDecimal subtotal = controlSubtotal.getBigDecimal();
		BigDecimal discountPerc = controlDiscountPerc.getBigDecimal();
		if (discountPerc!=null && subtotal!=null) {
			controlDiscount.setValue(subtotal.multiply(discountPerc).divide(new BigDecimal(100d),5,BigDecimal.ROUND_HALF_UP));
// no outboxing	must be used, in order to ensure java 1.4 compatibilty
// moreover, the primitive division is not the best choice (do not ensure correct decimals...)
//			controlDiscount.setValue(subtotal.doubleValue()*discountPerc.doubleValue()/100);
		} else {
			controlDiscount.setValue(null);
		}
		updateTotalFromDiscount();
	}

	private void updateDiscountPerc() {
		BigDecimal subtotal = controlSubtotal.getBigDecimal();
		BigDecimal discount = controlDiscount.getBigDecimal();
		if (subtotal!=null && discount!=null) {
// no outboxing	must be used, in order to ensure java 1.4 compatibilty
// moreover, the primitive division is not the best choice (do not ensure correct decimals...)
			controlDiscountPerc.setValue( discount.divide(subtotal,5,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100d)) );
	} else {
			controlDiscountPerc.setValue(null);
		}
	}

	private void updateTotalFromDiscount() {
		BigDecimal subtotal = controlSubtotal.getBigDecimal();
		BigDecimal discount = controlDiscount.getBigDecimal();
		if (subtotal!=null && discount!=null) {
			if (discount.doubleValue()>subtotal.doubleValue())
				controlDiscount.setValue(null);
			else
				controlTotal.setValue(subtotal.subtract(discount));
		}
	}

	private void updateTotals() {
		detailSaleDocVO.setTaxableIncomeDOC01(new BigDecimal(0));
		detailSaleDocVO.setTotalVatDOC01(new BigDecimal(0));
		BigDecimal subtotal = new BigDecimal(0);
		DetailSaleDocRowVO vo = null;
		for(int i=0;i<grid.getVOListTableModel().getRowCount();i++) {
			vo = (DetailSaleDocRowVO)grid.getVOListTableModel().getObjectForRow(i);
			subtotal = subtotal.add(vo.getValueDOC02());

			detailSaleDocVO.setTaxableIncomeDOC01(detailSaleDocVO.getTaxableIncomeDOC01().add(vo.getTaxableIncomeDOC02()));
			detailSaleDocVO.setTotalVatDOC01(detailSaleDocVO.getTotalVatDOC01().add(vo.getVatValueDOC02()));
		}
		controlSubtotal.setValue(subtotal);
		controlTotal.setValue(subtotal);

		BigDecimal discount = controlDiscount.getBigDecimal();
		if (subtotal!=null && discount!=null) {
			if (discount.doubleValue()>subtotal.doubleValue()) {
				controlDiscount.setValue(null);
				controlDiscountPerc.setValue(null);
			} else
				controlTotal.setValue(subtotal.subtract(discount));
		}
		else
			discount = new BigDecimal(0);

		BigDecimal total = controlTotal.getBigDecimal();
		BigDecimal paid = controlPaid.getBigDecimal();
		if (total!=null && paid!=null) {
			if (paid.doubleValue()<total.doubleValue())
				controlPaid.setValue(null);
			else
				controlChange.setValue(paid.subtract(total));
		}

		detailSaleDocVO.setAllowanceDOC01(discount);
		detailSaleDocVO.setTotalDOC01(total);
		if (!total.setScale(currVO.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP).equals(
				 detailSaleDocVO.getTaxableIncomeDOC01().add(detailSaleDocVO.getTotalVatDOC01()).subtract(discount).setScale(currVO.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP))) {
			Logger.error(
				this.getClass().getName(),
				"updateTotals",
				"Error on computing total: "+total+" vs "+detailSaleDocVO.getTaxableIncomeDOC01().add(detailSaleDocVO.getTotalVatDOC01()).subtract(discount),
				null
			);
		}

		grid.repaint();
	}

	void buttonCustomer_actionPerformed(ActionEvent e) {
		state = INS_CUSTOMER;
		updateContext();
	}

	void buttonClose_actionPerformed(ActionEvent e) {
		Object[] opt = new Object[]{
				ClientSettings.getInstance().getResources().getResource("yes"),
				ClientSettings.getInstance().getResources().getResource("no")
		};

		updateTotalFromDiscount();
		updateTotalFromPaid();
		if (controlDiscount.getValue()!=null)
			detailSaleDocVO.setDiscountValueDOC01((BigDecimal)controlDiscount.getValue());

		if (controlTotal.getBigDecimal().floatValue() < 0) {
			JOptionPane.showMessageDialog(
							this,
							ClientSettings.getInstance().getResources().getResource("negative total"),
							ClientSettings.getInstance().getResources().getResource("sale closing"),
							JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (controlPaid.getBigDecimal() == null || controlPaid.getBigDecimal().floatValue() < 0) {
			state = INS_PAID_TOTAL;
			updateContext();
			return;
		}

		if (JOptionPane.showOptionDialog(
				this,
				ClientSettings.getInstance().getResources().getResource("confirm sale closing"),
				ClientSettings.getInstance().getResources().getResource("sale closing"),
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				opt,
				opt[0]
		)!=0)  {
			return;
		}

		Response res = ClientUtils.getData("insertSaleDoc",detailSaleDocVO);
		if (res.isError()) {
			Logger.error(
				this.getClass().getName(),
				"buttonClose_actionPerformed",
				"Error on creating document: "+res.getErrorMessage(),
				null
			);
			return;
		}
		detailSaleDocVO = (DetailSaleDocVO)((VOResponse)res).getVo();
		DetailSaleDocRowVO rowVO = null;
		for(int i=0;i<grid.getVOListTableModel().getRowCount();i++) {
			rowVO = (DetailSaleDocRowVO)grid.getVOListTableModel().getObjectForRow(i);
			rowVO.setDocNumberDOC02(detailSaleDocVO.getDocNumberDOC01());
			res = ClientUtils.getData("insertSaleDocRow",rowVO);
			if (res.isError()) {
					Logger.error(
						this.getClass().getName(),
						"buttonClose_actionPerformed",
						"Error on creating document row: "+res.getErrorMessage(),
						null
					);
					//return;
			}
		}

		// after rows insertion, total doc has been updated, without considering total discount:
		// sale doc header will be reloaded, in order to apply header discount and update total amount...
		SaleDocPK pk = new SaleDocPK(detailSaleDocVO.getCompanyCodeSys01DOC01(),detailSaleDocVO.getDocTypeDOC01(),detailSaleDocVO.getDocYearDOC01(),detailSaleDocVO.getDocNumberDOC01());
		res = ClientUtils.getData("loadSaleDoc",pk);
		if (res.isError()) {
			Logger.error(
				this.getClass().getName(),
				"buttonClose_actionPerformed",
				"Error on creating document: "+res.getErrorMessage(),
				null
			);
			return;
		}
		DetailSaleDocVO oldDetailSaleDocVO = (DetailSaleDocVO)((VOResponse)res).getVo();

		res = ClientUtils.getData("updateSaleDoc",new ValueObject[]{oldDetailSaleDocVO,detailSaleDocVO});
		if (res.isError()) {
			Logger.error(
				this.getClass().getName(),
				"buttonClose_actionPerformed",
				"Error on creating document: "+res.getErrorMessage(),
				null
			);
			return;
		}


		res = ClientUtils.getData("closeSaleDoc",new SaleDocPK(
			detailSaleDocVO.getCompanyCodeSys01DOC01(),
			detailSaleDocVO.getDocTypeDOC01(),
			detailSaleDocVO.getDocYearDOC01(),
			detailSaleDocVO.getDocNumberDOC01()
		));
		if (!res.isError()) {

			// prompt user if a sale invoice must be created...
			if (JOptionPane.showConfirmDialog(
				this,
				ClientSettings.getInstance().getResources().getResource("create sale invoice?"),
				ClientSettings.getInstance().getResources().getResource("desk selling invoice"),
				JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION
			) {

				DetailSaleDocVO invoiceVO = null;
				try {
					invoiceVO = (DetailSaleDocVO) detailSaleDocVO.clone();
				}
				catch (CloneNotSupportedException ex) {
					ex.printStackTrace();
				}
				Calendar cal = Calendar.getInstance();
				invoiceVO.setCompanyCodeSys01Doc01DOC01(detailSaleDocVO.getCompanyCodeSys01DOC01());
				invoiceVO.setDocTypeDoc01DOC01(detailSaleDocVO.getDocTypeDOC01());
				invoiceVO.setDocYearDoc01DOC01(detailSaleDocVO.getDocYearDOC01());
				invoiceVO.setDocNumberDoc01DOC01(detailSaleDocVO.getDocNumberDOC01());
				invoiceVO.setDocSequenceDoc01DOC01(((BigDecimal)((VOResponse)res).getVo()));
				invoiceVO.setDocYearDOC01(new BigDecimal(cal.get(cal.YEAR)));
				invoiceVO.setDocDateDOC01(new java.sql.Date(System.currentTimeMillis()));
				invoiceVO.setDocTypeDOC01(ApplicationConsts.SALE_INVOICE_FROM_SD_DOC_TYPE);
				invoiceVO.setDocStateDOC01(ApplicationConsts.OPENED);
				invoiceVO.setDocNumberDOC01(null);
				invoiceVO.setDocSequenceDOC01(null);

				res = ClientUtils.getData("createInvoiceFromSaleDoc",invoiceVO);
				if (!res.isError()) {
					invoiceVO = (DetailSaleDocVO)((VOResponse)res).getVo();
					new SaleInvoiceDocFromSaleDocController(null,new SaleDocPK(
						invoiceVO.getCompanyCodeSys01DOC01(),
						invoiceVO.getDocTypeDOC01(),
						invoiceVO.getDocYearDOC01(),
						invoiceVO.getDocNumberDOC01()
					));
				}
				else
					JOptionPane.showMessageDialog(
							this,
							res.getErrorMessage(),
							ClientSettings.getInstance().getResources().getResource("desk selling invoice"),
							JOptionPane.ERROR_MESSAGE
					);
			}

		}
		else {
			JOptionPane.showMessageDialog(
					this,
					res.getErrorMessage(),
					ClientSettings.getInstance().getResources().getResource("desk selling confirmation"),
					JOptionPane.ERROR_MESSAGE
			);
		}


		// clear up all fields...
		controlBarcode.setEnabled(false);
		controlDiscount.setValue(null);
		controlDiscount.setEnabled(false);
		controlDiscountPerc.setValue(null);
		controlDiscountPerc.setEnabled(false);
		controlPaid.setValue(null);
		controlPaid.setEnabled(false);
		controlTotal.setValue(null);
		controlSubtotal.setValue(null);
		controlChange.setValue(null);
		labelSuggstmt.setText("\n \n");

		LookupValidationParams pars = new LookupValidationParams(defaultCustomerCode,new HashMap());
		pars.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,defaultCompanyCodeSys01);
		res = ClientUtils.getData("validateCustomerCode",pars);
		GridCustomerVO gridCustomerVO = null;
		if (!res.isError()) {
			java.util.List vos = ((VOListResponse)res).getRows();
			if (vos.size()==1)
				gridCustomerVO = (GridCustomerVO)vos.get(0);
			else
				gridCustomerVO = null;
		}
		else
			gridCustomerVO = null;
		controlCustomer.setValue(customerVO.getCustomerCodeSAL07());
		detailSaleDocVO.setCustomerCodeSAL07(customerVO.getCustomerCodeSAL07());
		detailSaleDocVO.setPaymentCodeReg10DOC01(customerVO.getPaymentCodeReg10SAL07());
		detailSaleDocVO.setPaymentDescriptionDOC01(customerVO.getPaymentDescriptionSYS10());
		controlPayDescr.setValue(customerVO.getPaymentDescriptionSYS10());

		rows.clear();
		grid.reloadData();
		state = START_SALE;
		updateContext();
	}

	void buttonDiscount_actionPerformed(ActionEvent e) {
		state = INS_DISCOUNT;
		updateContext();
	}

	void buttonDiscountTotal_actionPerformed(ActionEvent e) {
		state = INS_DISCOUNT_TOTAL;
		updateContext();
	}

	void buttonPaidTotal_actionPerformed(ActionEvent e) {
		state = INS_PAID_TOTAL;
		updateContext();
	}

	void buttonQty_actionPerformed(ActionEvent e) {
		state = INS_QTY;
		updateContext();
	}

	void buttonDel_actionPerformed(ActionEvent e) {
		grid.getVOListTableModel().removeObjectAt(grid.getSelectedRow());
		grid.setRowSelectionInterval(grid.getVOListTableModel().getRowCount()-1,grid.getVOListTableModel().getRowCount()-1);
		updateTotals();
		controlBarcode.setEnabled(true);
		state = INS_BARCODE;
		updateContext();

		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				controlBarcode.requestFocus();
			}

		});
	}

	void buttonBack_actionPerformed(ActionEvent e) {
		if (state==INS_BARCODE) {
			controlBarcode.setValue(null);
			controlBarcode.setEnabled(false);
			state = OTHER_COMMANDS;
			updateContext();
			this.requestFocus();
		}
		else if (state==OTHER_COMMANDS || state==INS_QTY || state==INS_DISCOUNT ||
						state==INS_DISCOUNT_TOTAL || state==INS_PAID_TOTAL) {
			controlBarcode.setEnabled(true);
			state = INS_BARCODE;
			updateContext();

			SwingUtilities.invokeLater(new Runnable() {

				public void run() {
					controlBarcode.requestFocus();
				}

			});
		}
	}

	void buttonStart_actionPerformed(ActionEvent e) {
		rows.clear();
		grid.reloadData();
		controlBarcode.setEnabled(true);
		controlDiscount.setEnabled(true);
		controlDiscountPerc.setEnabled(true);
		controlPaid.setEnabled(true);
		state = INS_BARCODE;
		updateContext();

		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				controlBarcode.requestFocus();
			}

		});
	}

	void be_actionPerformed(ActionEvent e) {
		if (buttonBack.isEnabled())
			buttonBack_actionPerformed(null);
	}

	void bi_actionPerformed(ActionEvent e) {
		if (controlBarcode.isEnabled() &&
				controlBarcode.getValue()!=null)
			checkBarcode();
		else if (controlBarcode.isEnabled() &&
						!controlBarcode.hasFocus())
			controlBarcode.requestFocus();
		else if (controlQty.isVisible() &&
						 controlQty.getBigDecimal()!=null) {
			DetailSaleDocRowVO vo = (DetailSaleDocRowVO)grid.getVOListTableModel().getObjectForRow(grid.getSelectedRow());
			vo.setQtyDOC02(controlQty.getBigDecimal());
			updateRow(vo);
			updateTotals();
			controlQty.setValue(null);
			state = INS_BARCODE;
			updateContext();
		}
		else if (controlItemValDiscount.isVisible() &&
						 controlItemValDiscount.getBigDecimal()!=null) {
			DetailSaleDocRowVO vo = (DetailSaleDocRowVO)grid.getVOListTableModel().getObjectForRow(grid.getSelectedRow());
			vo.setDiscountValue(controlItemValDiscount.getBigDecimal());

			BigDecimal vat = vo.getValueReg01DOC02();
			BigDecimal x = vo.getDiscountValue();
			BigDecimal d = null;
			if (x!=null)
				d = x.divide(
					new BigDecimal(1).add(
						vat.divide(
							new BigDecimal(100),
							BigDecimal.ROUND_HALF_UP
						)
					),
					currVO.getDecimalsREG03().intValue(),
					BigDecimal.ROUND_HALF_UP
				);
			vo.setDiscountValueDOC02(d);
			vo.setDiscountPercDOC02(null);
			vo.setTotalDiscountDOC02(vo.getDiscountValueDOC02());


			updateRow(vo);
			updateTotals();
			controlItemValDiscount.setValue(null);
			state = INS_BARCODE;
			updateContext();
		}
		else if (controlValDiscountTotal.isVisible() &&
						 controlValDiscountTotal.getBigDecimal()!=null) {
			controlBarcode.setEnabled(true);
			controlDiscount.setValue(controlValDiscountTotal.getBigDecimal());
			updateDiscountPerc();
			updateTotalFromDiscount();
			controlValDiscountTotal.setValue(null);
			state = INS_BARCODE;
			updateContext();
		}
		else if (controlValPaidTotal.isVisible() &&
						 controlValPaidTotal.getBigDecimal()!=null) {
			controlBarcode.setEnabled(true);
			controlPaid.setValue(controlValPaidTotal.getBigDecimal());
			updateTotalFromPaid();
			controlValPaidTotal.setValue(null);
			state = OTHER_COMMANDS;
			updateContext();
			buttonClose.doClick();
		}
	}

	void bc_actionPerformed(ActionEvent e) {
		if (controlBarcode.isEnabled()) {
			String value = (String)controlBarcode.getValue();
			if (value==null)
				value = "";
			if (value.length()>0)
				value = value.substring(0,value.length()-1);
			controlBarcode.setValue(value);
			controlBarcode.requestFocus();
		}
	}

	void b0_actionPerformed(ActionEvent e) {
		if (controlBarcode.isEnabled()) {
			String value = (String)controlBarcode.getValue();
			if (value==null)
				value = "";
			controlBarcode.setValue(value+"0");
			controlBarcode.requestFocus();
		}
	}

	void bv_actionPerformed(ActionEvent e) {
		if (controlBarcode.isEnabled()) {
			String value = (String)controlBarcode.getValue();
			if (value==null)
				value = "";
			controlBarcode.setValue(value+".");
			controlBarcode.requestFocus();
		}
	}

	void b1_actionPerformed(ActionEvent e) {
		if (buttonDel.isEnabled())
			buttonDel_actionPerformed(e);
		else if (controlBarcode.isEnabled()) {
			String value = (String)controlBarcode.getValue();
			if (value==null)
				value = "";
			controlBarcode.setValue(value+"1");
			controlBarcode.requestFocus();
		}
	}

	void b2_actionPerformed(ActionEvent e) {
		if (buttonQty.isEnabled())
			buttonQty_actionPerformed(e);
		else if (controlBarcode.isEnabled()) {
			String value = (String)controlBarcode.getValue();
			if (value==null)
				value = "";
			controlBarcode.setValue(value+"2");
			controlBarcode.requestFocus();
		}
	}

	void b3_actionPerformed(ActionEvent e) {
		if (buttonDiscount.isEnabled())
			buttonDiscount_actionPerformed(e);
		else if (controlBarcode.isEnabled()) {
			String value = (String)controlBarcode.getValue();
			if (value==null)
				value = "";
			controlBarcode.setValue(value+"3");
			controlBarcode.requestFocus();
		}
	}

	void b4_actionPerformed(ActionEvent e) {
		if (buttonCustomer.isEnabled())
			buttonCustomer_actionPerformed(e);
		else if (controlBarcode.isEnabled()) {
			String value = (String)controlBarcode.getValue();
			if (value==null)
				value = "";
			controlBarcode.setValue(value+"4");
			controlBarcode.requestFocus();
		}
	}

	void b5_actionPerformed(ActionEvent e) {
		if (buttonStart.isEnabled())
			buttonStart_actionPerformed(e);
		else if (controlBarcode.isEnabled()) {
			String value = (String)controlBarcode.getValue();
			if (value==null)
				value = "";
			controlBarcode.setValue(value+"5");
			controlBarcode.requestFocus();
		}

	}

	void b6_actionPerformed(ActionEvent e) {
		if (buttonClose.isEnabled())
			buttonClose_actionPerformed(e);
		else if (controlBarcode.isEnabled()) {
			String value = (String)controlBarcode.getValue();
			if (value==null)
				value = "";
			controlBarcode.setValue(value+"6");
			controlBarcode.requestFocus();
		}

	}

	void b7_actionPerformed(ActionEvent e) {
		if (buttonExit.isEnabled())
			buttonExit_actionPerformed(e);
		else if (controlBarcode.isEnabled()) {
			String value = (String)controlBarcode.getValue();
			if (value==null)
				value = "";
			controlBarcode.setValue(value+"7");
			controlBarcode.requestFocus();
		}

	}

	void b8_actionPerformed(ActionEvent e) {
		if (controlBarcode.isEnabled()) {
			String value = (String)controlBarcode.getValue();
			if (value==null)
				value = "";
			controlBarcode.setValue(value+"8");
			controlBarcode.requestFocus();
		}

	}

	void b9_actionPerformed(ActionEvent e) {
		if (controlBarcode.isEnabled()) {
			String value = (String)controlBarcode.getValue();
			if (value==null)
				value = "";
			controlBarcode.setValue(value+"9");
			controlBarcode.requestFocus();
		}

	}




}

class PosFrame_buttonExit_actionAdapter implements java.awt.event.ActionListener {
	PosFrame adaptee;

	PosFrame_buttonExit_actionAdapter(PosFrame adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.buttonExit_actionPerformed(e);
	}
}

class PosFrame_controlPaid_focusAdapter extends java.awt.event.FocusAdapter {
	PosFrame adaptee;

	PosFrame_controlPaid_focusAdapter(PosFrame adaptee) {
		this.adaptee = adaptee;
	}
	public void focusLost(FocusEvent e) {
		adaptee.controlPaid_focusLost(e);
	}
}

class PosFrame_controlDiscount_focusAdapter extends java.awt.event.FocusAdapter {
	PosFrame adaptee;

	PosFrame_controlDiscount_focusAdapter(PosFrame adaptee) {
		this.adaptee = adaptee;
	}
	public void focusLost(FocusEvent e) {
		adaptee.controlDiscount_focusLost(e);
	}
}

class PosFrame_controlDiscountPerc_focusAdapter extends java.awt.event.FocusAdapter {
	PosFrame adaptee;

	PosFrame_controlDiscountPerc_focusAdapter(PosFrame adaptee) {
		this.adaptee = adaptee;
	}
	public void focusLost(FocusEvent e) {
		adaptee.controlDiscountPerc_focusLost(e);
	}
}

class PosFrame_buttonCustomer_actionAdapter implements java.awt.event.ActionListener {
	PosFrame adaptee;

	PosFrame_buttonCustomer_actionAdapter(PosFrame adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.buttonCustomer_actionPerformed(e);
	}
}

class PosFrame_buttonClose_actionAdapter implements java.awt.event.ActionListener {
	PosFrame adaptee;

	PosFrame_buttonClose_actionAdapter(PosFrame adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.buttonClose_actionPerformed(e);
	}
}

class PosFrame_buttonDiscount_actionAdapter implements java.awt.event.ActionListener {
	PosFrame adaptee;

	PosFrame_buttonDiscount_actionAdapter(PosFrame adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.buttonDiscount_actionPerformed(e);
	}
}

class PosFrame_buttonDiscountTotal_actionAdapter implements java.awt.event.ActionListener {
	PosFrame adaptee;

	PosFrame_buttonDiscountTotal_actionAdapter(PosFrame adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.buttonDiscountTotal_actionPerformed(e);
	}
}

class PosFrame_buttonPaidTotal_actionAdapter implements java.awt.event.ActionListener {
	PosFrame adaptee;

	PosFrame_buttonPaidTotal_actionAdapter(PosFrame adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.buttonPaidTotal_actionPerformed(e);
	}
}

class PosFrame_buttonQty_actionAdapter implements java.awt.event.ActionListener {
	PosFrame adaptee;

	PosFrame_buttonQty_actionAdapter(PosFrame adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.buttonQty_actionPerformed(e);
	}
}

class PosFrame_buttonDel_actionAdapter implements java.awt.event.ActionListener {
	PosFrame adaptee;

	PosFrame_buttonDel_actionAdapter(PosFrame adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.buttonDel_actionPerformed(e);
	}
}

class PosFrame_buttonBack_actionAdapter implements java.awt.event.ActionListener {
	PosFrame adaptee;

	PosFrame_buttonBack_actionAdapter(PosFrame adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.buttonBack_actionPerformed(e);
	}
}

class PosFrame_buttonStart_actionAdapter implements java.awt.event.ActionListener {
	PosFrame adaptee;

	PosFrame_buttonStart_actionAdapter(PosFrame adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.buttonStart_actionPerformed(e);
	}
}

class PosFrame_be_actionAdapter implements java.awt.event.ActionListener {
	PosFrame adaptee;

	PosFrame_be_actionAdapter(PosFrame adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.be_actionPerformed(e);
	}
}

class PosFrame_bi_actionAdapter implements java.awt.event.ActionListener {
	PosFrame adaptee;

	PosFrame_bi_actionAdapter(PosFrame adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.bi_actionPerformed(e);
	}
}

class PosFrame_bc_actionAdapter implements java.awt.event.ActionListener {
	PosFrame adaptee;

	PosFrame_bc_actionAdapter(PosFrame adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.bc_actionPerformed(e);
	}
}

class PosFrame_b0_actionAdapter implements java.awt.event.ActionListener {
	PosFrame adaptee;

	PosFrame_b0_actionAdapter(PosFrame adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.b0_actionPerformed(e);
	}
}

class PosFrame_bv_actionAdapter implements java.awt.event.ActionListener {
	PosFrame adaptee;

	PosFrame_bv_actionAdapter(PosFrame adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.bv_actionPerformed(e);
	}
}

class PosFrame_b1_actionAdapter implements java.awt.event.ActionListener {
	PosFrame adaptee;

	PosFrame_b1_actionAdapter(PosFrame adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.b1_actionPerformed(e);
	}
}

class PosFrame_b2_actionAdapter implements java.awt.event.ActionListener {
	PosFrame adaptee;

	PosFrame_b2_actionAdapter(PosFrame adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.b2_actionPerformed(e);
	}
}

class PosFrame_b3_actionAdapter implements java.awt.event.ActionListener {
	PosFrame adaptee;

	PosFrame_b3_actionAdapter(PosFrame adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.b3_actionPerformed(e);
	}
}

class PosFrame_b4_actionAdapter implements java.awt.event.ActionListener {
	PosFrame adaptee;

	PosFrame_b4_actionAdapter(PosFrame adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.b4_actionPerformed(e);
	}
}

class PosFrame_b5_actionAdapter implements java.awt.event.ActionListener {
	PosFrame adaptee;

	PosFrame_b5_actionAdapter(PosFrame adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.b5_actionPerformed(e);
	}
}

class PosFrame_b6_actionAdapter implements java.awt.event.ActionListener {
	PosFrame adaptee;

	PosFrame_b6_actionAdapter(PosFrame adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.b6_actionPerformed(e);
	}
}

class PosFrame_b7_actionAdapter implements java.awt.event.ActionListener {
	PosFrame adaptee;

	PosFrame_b7_actionAdapter(PosFrame adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.b7_actionPerformed(e);
	}
}

class PosFrame_b8_actionAdapter implements java.awt.event.ActionListener {
	PosFrame adaptee;

	PosFrame_b8_actionAdapter(PosFrame adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.b8_actionPerformed(e);
	}
}

class PosFrame_b9_actionAdapter implements java.awt.event.ActionListener {
	PosFrame adaptee;

	PosFrame_b9_actionAdapter(PosFrame adaptee) {
		this.adaptee = adaptee;
	}
	public void actionPerformed(ActionEvent e) {
		adaptee.b9_actionPerformed(e);
	}
}
