package org.jallinone.purchases.documents.invoices.client;

import org.openswing.swing.table.client.Grid;
import org.openswing.swing.message.receive.java.*;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.purchases.documents.java.GridPurchaseDocVO;
import org.openswing.swing.mdi.client.MDIFrame;
import org.jallinone.commons.client.CompanyFormController;
import org.jallinone.purchases.documents.java.PurchaseDocPK;
import org.openswing.swing.util.java.Consts;
import org.jallinone.purchases.documents.java.DetailPurchaseDocVO;
import org.openswing.swing.form.client.Form;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.mdi.client.InternalFrame;
import java.awt.*;
import javax.swing.*;
import org.openswing.swing.client.*;
import org.jallinone.commons.client.CustomizedControls;
import org.openswing.swing.util.client.ClientSettings;
import java.util.HashSet;
import java.awt.event.*;
import org.openswing.swing.client.GenericButtonController;
import org.jallinone.purchases.documents.client.PurchaseDocument;
import org.jallinone.purchases.documents.client.*;
import org.openswing.swing.lookup.client.LookupListener;
import java.util.Collection;
import java.util.HashMap;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JRViewer;
import org.jallinone.registers.payments.java.PaymentVO;
import org.openswing.swing.message.send.java.LookupValidationParams;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Detail Frame used for a purchase invoice, created from a set of in delivery notes,
 * related to the same purchase document (contract or order).</p>
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
 * but WITHOUT ANY WARRANTY; within even the implied warranty of
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
public class PurchaseInvoiceDocFromDelivNotesFrame extends InternalFrame implements InvoiceDocument {

  /** detail form controller */
  private PurchaseInvoiceDocFromDelivNotesController controller = null;
  BorderLayout borderLayin1 = new BorderLayout();
  JPanel mainPanel = new JPanel();
  BorderLayout borderLayin2 = new BorderLayout();
  JTabbedPane tabbedPane = new JTabbedPane();
  JPanel headerPanel = new JPanel();
  FlowLayout flowLayin1 = new FlowLayout();
  JPanel headerButtonsPanel = new JPanel();
  BorderLayout borderLayin3 = new BorderLayout();
  JPanel linesPanel = new JPanel();
  BorderLayout borderLayin4 = new BorderLayout();
  Form headerFormPanel = new Form();
  InsertButton insertButton1 = new InsertButton();
  EditButton editButton1 = new EditButton();
  SaveButton saveButton1 = new SaveButton();
  ReloadButton reloadButton1 = new ReloadButton();
  DeleteButton deleteButton1 = new DeleteButton();
  GridBagLayout gridBagLayin1 = new GridBagLayout();
  PurchaseIdHeadPanel purchaseIdHeadPanel1 = new PurchaseIdHeadPanel(headerFormPanel,true,true);
  PurchaseSupplierHeadPanel purchaseSupplierHeadPanel1 = new PurchaseSupplierHeadPanel(headerFormPanel);
  PurchaseTotalsPanel purchaseTotalsPanel1 = new PurchaseTotalsPanel();
  PurchaseInvoiceDocRowsGridPanel rowsPanel = new PurchaseInvoiceDocRowsGridPanel(this,headerFormPanel,false,false);
  PurchaseWarehousePanel warePanel = new PurchaseWarehousePanel(headerFormPanel);
  BorderLayout borderLayout5 = new BorderLayout();
  private InDeliveryNotesFromPurchaseDocPanel delivPanel = new InDeliveryNotesFromPurchaseDocPanel(this);
  GenericButton confirmButton = new GenericButton(new ImageIcon(ClientUtils.getImage("workflow.gif")));
  GenericButton printButton = new GenericButton(new ImageIcon(ClientUtils.getImage("printer.gif")));
	PurchaseDiscChargesPanel purchaseDiscChargesPanel1 = new PurchaseDiscChargesPanel(headerFormPanel);


  public PurchaseInvoiceDocFromDelivNotesFrame(PurchaseInvoiceDocFromDelivNotesController controller) {
    this.controller = controller;
    try {
      jbInit();
      setSize(750,630);
      setMinimumSize(new Dimension(750,630));
      setTitle(ClientSettings.getInstance().getResources().getResource("purchase invoice from delivery notes"));

//      CustomizedControls customizedControls = new CustomizedControls(tabbedPane,headerFormPanel,ApplicationConsts.ID_PURCHASE_ESTIMATE);

//      purchaseSupplierHeadPanel1.getSupplierDataLocator().getLookupFrameParams().put(ApplicationConsts.SUBJECT_TYPE,ApplicationConsts.SUBJECT_PEOPLE_CUSTOMER);
//      purchaseSupplierHeadPanel1.getSupplierDataLocator().getLookupValidationParameters().put(ApplicationConsts.SUBJECT_TYPE,ApplicationConsts.SUBJECT_PEOPLE_CUSTOMER);


      purchaseIdHeadPanel1.getControlDocRifLookup().getLookupController().addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          GridPurchaseDocVO vo = (GridPurchaseDocVO)purchaseIdHeadPanel1.getControlDocRifLookup().getLookupController().getLookupVO();
          if (vo.getDocNumberDOC06()!=null) {
            // pre-set header data...
        	PurchaseDocPK pk = new PurchaseDocPK(
              vo.getCompanyCodeSys01DOC06(),
              vo.getDocTypeDOC06(),
              vo.getDocYearDOC06(),
              vo.getDocNumberDOC06()
            );

            Response res = ClientUtils.getData("loadPurchaseDoc",pk);
            if (!res.isError()) {
              DetailPurchaseDocVO purchaseDocVO = (DetailPurchaseDocVO)((VOResponse)res).getVo();
              DetailPurchaseDocVO oldPurchaseDocVO = (DetailPurchaseDocVO)headerFormPanel.getVOModel().getValueObject();
              purchaseDocVO.setCompanyCodeSys01Doc06DOC06(vo.getCompanyCodeSys01DOC06());
              purchaseDocVO.setDocTypeDoc06DOC06(vo.getDocTypeDOC06());
              purchaseDocVO.setDocYearDoc06DOC06(vo.getDocYearDOC06());
              purchaseDocVO.setDocNumberDoc06DOC06(vo.getDocNumberDOC06());
              purchaseDocVO.setDocSequenceDoc06DOC06(vo.getDocSequenceDOC06());
              purchaseDocVO.setDocTypeDOC06(ApplicationConsts.PURCHASE_INVOICE_FROM_DN_DOC_TYPE);
              purchaseDocVO.setDocNumberDOC06(null);
              purchaseDocVO.setDocSequenceDOC06(oldPurchaseDocVO.getDocSequenceDOC06());
              purchaseDocVO.setDocStateDOC06(ApplicationConsts.OPENED);
              purchaseDocVO.setDocDateDOC06(oldPurchaseDocVO.getDocDateDOC06());
              headerFormPanel.getVOModel().setValueObject(purchaseDocVO);

              delivPanel.getGrid().getOtherGridParams().put(ApplicationConsts.PURCHASE_DOC_VO,purchaseDocVO);
              delivPanel.getGrid().setMode(Consts.READONLY);
              delivPanel.getGrid().reloadData();
              delivPanel.setEnabled(true);

              headerFormPanel.pull();

              loadDataCompleted(true,pk);
            }
          }
          else {
            // reset header data...
            try {
              DetailPurchaseDocVO purchaseDocVO = new DetailPurchaseDocVO();
              PurchaseInvoiceDocFromDelivNotesFrame.this.controller.createPersistentObject(purchaseDocVO);
              purchaseDocVO.setDocTypeDoc06DOC06(ApplicationConsts.PURCHASE_ORDER_DOC_TYPE);
              headerFormPanel.getVOModel().setValueObject(purchaseDocVO);

              delivPanel.getGrid().clearData();
              delivPanel.setEnabled(false);


              headerFormPanel.pull();
            }
            catch (Exception ex) {
            }
          }
        }

        public void beforeLookupAction(ValueObject parentVO) {}

        public void forceValidate() {}

      });


      init();

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * Define input controls editable settings according to the document state.
   */
  private void init() {
		// disable discounts and charges when the no lines are not yet to added...
		HashSet attributeNameToDisable = new HashSet();
		attributeNameToDisable.add("discountValueDOC06");
		attributeNameToDisable.add("discountPercDOC06");
		attributeNameToDisable.add("chargeValueDOC06");
		attributeNameToDisable.add("chargePercDOC06");
    attributeNameToDisable.add("supplierCodePUR01");
    attributeNameToDisable.add("pricelistCodePur03DOC06");
    attributeNameToDisable.add("paymentCodeReg10DOC06");
    attributeNameToDisable.add("warehouseCodeWar01DOC06");
    attributeNameToDisable.add("destinationCodeReg18DOC06");
    attributeNameToDisable.add("noteDOC06");
    headerFormPanel.addInputControlAttributesNotEditableOnState(attributeNameToDisable,"docStateDOC06",ApplicationConsts.OPENED);

    // disable supplier and pricelist when the at least one line is added...
    attributeNameToDisable = new HashSet();
    attributeNameToDisable.add("supplierCodePUR01");
    attributeNameToDisable.add("pricelistCodePur03DOC06");
    attributeNameToDisable.add("docDateDOC06");
    attributeNameToDisable.add("paymentCodeReg10DOC06");
    attributeNameToDisable.add("paymentCodeReg10DOC06");
    attributeNameToDisable.add("warehouseCodeWar01DOC06");
    attributeNameToDisable.add("destinationCodeReg18DOC06");
    headerFormPanel.addInputControlAttributesNotEditableOnState(attributeNameToDisable,"docStateDOC06",ApplicationConsts.HEADER_BLOCKED);


    // disable all editable fields when the order is confimed (closed)...
    attributeNameToDisable = new HashSet();
    attributeNameToDisable.add("supplierCodePUR01");
    attributeNameToDisable.add("pricelistCodePur03DOC06");
    attributeNameToDisable.add("docDateDOC06");
    attributeNameToDisable.add("paymentCodeReg10DOC06");
    attributeNameToDisable.add("destinationCodeReg18DOC06");
    attributeNameToDisable.add("noteDOC06");
    attributeNameToDisable.add("headingNoteDOC06");
    attributeNameToDisable.add("footerNoteDOC06");
    attributeNameToDisable.add("deliveryNoteDOC06");
    attributeNameToDisable.add("warehouseCodeWar01DOC06");
    headerFormPanel.addInputControlAttributesNotEditableOnState(attributeNameToDisable,"docStateDOC06",ApplicationConsts.CLOSED);

    HashSet buttonsToDisable = new HashSet();
    buttonsToDisable.add(editButton1);
    buttonsToDisable.add(deleteButton1);
    headerFormPanel.addButtonsNotEnabled(buttonsToDisable,this);

  }


  private void jbInit() throws Exception {
    printButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("print document"));
    printButton.addActionListener(new PurchaseInvoiceDocFromDelivNotesFrame_printButton_actionAdapter(this));

    purchaseIdHeadPanel1.getControlDocRifLookup().setRequired(true);

    confirmButton.addActionListener(new PurchaseInvoiceDocFromDelivNotesFrame_confirmButton_actionAdapter(this));

    headerFormPanel.setFunctionId("DOC06_INV_FROM_DN");
    headerFormPanel.setFormController(controller);
    headerFormPanel.setVOClassName("org.jallinone.purchases.documents.java.DetailPurchaseDocVO");

    headerPanel.setLayout(borderLayin3);
    headerButtonsPanel.setLayout(flowLayin1);
    flowLayin1.setAlignment(FlowLayout.LEFT);
    mainPanel.setLayout(borderLayin2);
    this.getContentPane().setLayout(borderLayin1);
    linesPanel.setLayout(borderLayin4);
    insertButton1.setText("insertButton1");
    reloadButton1.setText("reloadButton1");
    deleteButton1.setText("deleteButton1");
    headerFormPanel.setLayout(gridBagLayin1);
    headerFormPanel.setInsertButton(insertButton1);
    headerFormPanel.setEditButton(editButton1);
    headerFormPanel.setReloadButton(reloadButton1);
    headerFormPanel.setDeleteButton(deleteButton1);
    headerFormPanel.setSaveButton(saveButton1);
    this.getContentPane().add(mainPanel, BorderLayout.CENTER);
    mainPanel.add(tabbedPane,  BorderLayout.CENTER);
    tabbedPane.add(headerPanel,   "header");
    headerPanel.add(headerButtonsPanel,  BorderLayout.NORTH);
    headerButtonsPanel.add(insertButton1, null);
    headerButtonsPanel.add(editButton1, null);
    headerButtonsPanel.add(saveButton1, null);
    headerButtonsPanel.add(reloadButton1, null);
    headerButtonsPanel.add(deleteButton1, null);
    headerButtonsPanel.add(confirmButton, null);
    printButton.setEnabled(false);
    headerButtonsPanel.add(printButton, null);
    linesPanel.add(rowsPanel, BorderLayout.CENTER);
    headerPanel.add(headerFormPanel,  BorderLayout.CENTER);
    headerFormPanel.add(purchaseIdHeadPanel1,   new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    headerFormPanel.add(purchaseSupplierHeadPanel1,     new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    headerFormPanel.add(warePanel,   new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		headerFormPanel.add(purchaseDiscChargesPanel1,   new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0
						,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    headerFormPanel.add(purchaseTotalsPanel1,    new GridBagConstraints(0, 4, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

    confirmButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("invoice closing"));
    confirmButton.setEnabled(false);

    tabbedPane.add(delivPanel, "delivery notes");
    tabbedPane.add(linesPanel,   "items");

    tabbedPane.setTitleAt(0,ClientSettings.getInstance().getResources().getResource("header"));
    tabbedPane.setTitleAt(1,ClientSettings.getInstance().getResources().getResource("delivery notes"));
    tabbedPane.setTitleAt(2,ClientSettings.getInstance().getResources().getResource("items"));

  }


  public Form getHeaderFormPanel() {
    return headerFormPanel;
  }


  public void setButtonsEnabled(boolean enabled) {
    rowsPanel.setButtonsEnabled(enabled);
    delivPanel.setEnabled(enabled);
  }


  public final void updateCurrencySettings(DetailPurchaseDocVO vo) {
		purchaseTotalsPanel1.getControlTaxableIncome().setDecimals(vo.getDecimalsREG03().intValue());
		purchaseTotalsPanel1.getControlTaxableIncome().setCurrencySymbol(vo.getCurrencySymbolREG03());
		purchaseTotalsPanel1.getControlTaxableIncome().setDecimalSymbol(vo.getDecimalSymbolREG03().charAt(0));
		purchaseTotalsPanel1.getControlTaxableIncome().setGroupingSymbol(vo.getThousandSymbolREG03().charAt(0));

		purchaseTotalsPanel1.getControlTotal().setDecimals(vo.getDecimalsREG03().intValue());
		purchaseTotalsPanel1.getControlTotal().setCurrencySymbol(vo.getCurrencySymbolREG03());
		purchaseTotalsPanel1.getControlTotal().setDecimalSymbol(vo.getDecimalSymbolREG03().charAt(0));
		purchaseTotalsPanel1.getControlTotal().setGroupingSymbol(vo.getThousandSymbolREG03().charAt(0));

		purchaseTotalsPanel1.getControlTotalVat().setDecimals(vo.getDecimalsREG03().intValue());
		purchaseTotalsPanel1.getControlTotalVat().setCurrencySymbol(vo.getCurrencySymbolREG03());
		purchaseTotalsPanel1.getControlTotalVat().setDecimalSymbol(vo.getDecimalSymbolREG03().charAt(0));
		purchaseTotalsPanel1.getControlTotalVat().setGroupingSymbol(vo.getThousandSymbolREG03().charAt(0));

		purchaseDiscChargesPanel1.getControlChargeValue().setDecimals(vo.getDecimalsREG03().intValue());
		purchaseDiscChargesPanel1.getControlChargeValue().setCurrencySymbol(vo.getCurrencySymbolREG03());
		purchaseDiscChargesPanel1.getControlChargeValue().setDecimalSymbol(vo.getDecimalSymbolREG03().charAt(0));
		purchaseDiscChargesPanel1.getControlChargeValue().setGroupingSymbol(vo.getThousandSymbolREG03().charAt(0));

		purchaseDiscChargesPanel1.getControlDiscValue().setDecimals(vo.getDecimalsREG03().intValue());
		purchaseDiscChargesPanel1.getControlDiscValue().setCurrencySymbol(vo.getCurrencySymbolREG03());
		purchaseDiscChargesPanel1.getControlDiscValue().setDecimalSymbol(vo.getDecimalSymbolREG03().charAt(0));
		purchaseDiscChargesPanel1.getControlDiscValue().setGroupingSymbol(vo.getThousandSymbolREG03().charAt(0));

  }

  public void loadDataCompleted(boolean error,PurchaseDocPK pk) {
    DetailPurchaseDocVO vo = (DetailPurchaseDocVO)headerFormPanel.getVOModel().getValueObject();
    updateCurrencySettings(vo);

    rowsPanel.setParentVO(vo);
    rowsPanel.getGrid().getOtherGridParams().put(ApplicationConsts.PURCHASE_DOC_PK,pk);
    rowsPanel.getGrid().reloadData();

    if (vo.getDocStateDOC06().equals(ApplicationConsts.CLOSED) ||
        vo.getDocStateDOC06().equals(ApplicationConsts.CONFIRMED)) {
      confirmButton.setEnabled(false);
      setButtonsEnabled(false);
      rowsPanel.setButtonsEnabled(false);

      delivPanel.getGrid().getOtherGridParams().put(ApplicationConsts.PURCHASE_DOC_VO,vo);
      delivPanel.getGrid().setMode(Consts.READONLY);
      delivPanel.getGrid().reloadData();
    }
    if (!vo.getDocStateDOC06().equals(ApplicationConsts.HEADER_BLOCKED)) {
      confirmButton.setEnabled(false);
    }
    if (vo.getDocStateDOC06().equals(ApplicationConsts.HEADER_BLOCKED)) {
      confirmButton.setEnabled(true);
    }

    if (vo.getDocStateDOC06().equals(ApplicationConsts.CONFIRMED) ||
        vo.getDocStateDOC06().equals(ApplicationConsts.CLOSED) ||
        vo.getDocStateDOC06().equals(ApplicationConsts.HEADER_BLOCKED)) {
      printButton.setEnabled(true);
    }
    else {
      printButton.setEnabled(false);
    }

  }


  /**
   * Method called by GenericButton.setEnabled method to check if the button must be disabled.
   * @param button button whose abilitation must be checked
   * @return <code>true</code> if no policy is defined in the form/grid for the specified button, <code>false</code> if there exists a disabilitation policy for the specified button (through addButtonsNotEnabledOnState form/grid method)
   */
  public boolean isButtonDisabled(GenericButton button) {
    DetailPurchaseDocVO vo = (DetailPurchaseDocVO)headerFormPanel.getVOModel().getValueObject();
    if (vo!=null && vo.getDocStateDOC06()!=null && vo.getDocStateDOC06().equals(ApplicationConsts.CLOSED))
      return true;
    else
      return false;
  }



  public PurchaseInvoiceDocRowsGridPanel getRowsPanel() {
    return rowsPanel;
  }


  public GridControl getInvoices() {
    if (controller.getParentFrame()!=null)
      return controller.getParentFrame().getGrid();
    else
      return null;
  }


  void confirmButton_actionPerformed(ActionEvent e) {
    // view close dialog...
    if (JOptionPane.showConfirmDialog(ClientUtils.getParentFrame(this),
                                  ClientSettings.getInstance().getResources().getResource("close purchase invoice?"),
                                  ClientSettings.getInstance().getResources().getResource("invoice closing"),
                                  JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {

      Response res = ClientUtils.getData("closePurchaseDoc",controller.getPk());
      if (!res.isError()) {
        headerFormPanel.setMode(Consts.READONLY);
        headerFormPanel.executeReload();
        if (getInvoices()!=null)
          getInvoices().reloadCurrentBlockOfData();

					// check if there is only one instalment and this instalment has 0 instalment days
					DetailPurchaseDocVO vo = (DetailPurchaseDocVO)headerFormPanel.getVOModel().getValueObject();
					res = ClientUtils.getData("validatePaymentCode",new LookupValidationParams(vo.getPaymentCodeReg10DOC06(),new HashMap()));
					if (res.isError()) {
						OptionPane.showMessageDialog(
									ClientUtils.getParentFrame(this),
									res.getErrorMessage(),
									ClientSettings.getInstance().getResources().getResource("invoice closing"),
									JOptionPane.ERROR_MESSAGE
						);
					}
					else {
						PaymentVO payVO = (PaymentVO)((VOListResponse)res).getRows().get(0);
						if (payVO.getInstalmentNumberREG10().intValue()==1 &&
								payVO.getStepREG10().intValue()==0 &&
								payVO.getFirstInstalmentDaysREG10().intValue()==0 &&
								payVO.getStartDayREG10().equals(ApplicationConsts.START_DAY_INVOICE_DATE)) {
							// there is only one instalment and this instalment has 0 instalment days:
							// prompt user for an immediate payment...
							if (OptionPane.showConfirmDialog(
										ClientUtils.getParentFrame(this),
										"create payment immediately",
										ClientSettings.getInstance().getResources().getResource("invoice closing"),
										JOptionPane.YES_NO_OPTION,
										JOptionPane.QUESTION_MESSAGE
							)==JOptionPane.YES_OPTION) {
								res = ClientUtils.getData("payImmediately",new Object[]{
												vo.getCompanyCodeSys01DOC06(),
												vo.getDocTypeDOC06(),
												vo.getDocYearDOC06(),
												vo.getDocNumberDOC06(),
												vo.getDocSequenceDOC06()
								});

								if (res.isError())
									OptionPane.showMessageDialog(
												ClientUtils.getParentFrame(this),
												res.getErrorMessage(),
												ClientSettings.getInstance().getResources().getResource("invoice closing"),
												JOptionPane.ERROR_MESSAGE
									);

							}
						}
					}

      }
      else JOptionPane.showMessageDialog(
              ClientUtils.getParentFrame(this),
              res.getErrorMessage(),
              ClientSettings.getInstance().getResources().getResource("invoice closing"),
              JOptionPane.ERROR_MESSAGE
            );

    }

  }


  public InDeliveryNotesFromPurchaseDocPanel getDelivPanel() {
    return delivPanel;
  }


  public DetailPurchaseDocVO getParentVO() {
    return (DetailPurchaseDocVO)headerFormPanel.getVOModel().getValueObject();
  }
  public PurchaseSupplierHeadPanel getPurchaseSupplierHeadPanel1() {
    return purchaseSupplierHeadPanel1;
  }
  public PurchaseWarehousePanel getWarePanel() {
    return warePanel;
  }


  public String getFunctionId() {
    return "DOC06_INV_FROM_DN";
  }


  public void enabledConfirmButton() {
    confirmButton.setEnabled(true);
  }

  void printButton_actionPerformed(ActionEvent e) {
    DetailPurchaseDocVO vo = (DetailPurchaseDocVO)headerFormPanel.getVOModel().getValueObject();

    HashMap params = new HashMap();
    params.put("COMPANY_CODE",vo.getCompanyCodeSys01DOC06());
    params.put("DOC_TYPE",vo.getDocTypeDOC06());
    params.put("DOC_YEAR",vo.getDocYearDOC06());
    params.put("DOC_NUMBER",vo.getDocNumberDOC06());

    HashMap map = new HashMap();
    map.put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01DOC06());
    map.put(ApplicationConsts.FUNCTION_CODE_SYS06,headerFormPanel.getFunctionId());
    map.put(ApplicationConsts.EXPORT_PARAMS,params);
    Response res = ClientUtils.getData("getJasperReport",map);
    if (!res.isError()) {
      JasperPrint print = (JasperPrint)((VOResponse)res).getVo();
      JRViewer viewer = new JRViewer(print);
      JFrame frame = new JFrame();
      frame.setSize(MDIFrame.getInstance().getSize());
      frame.setContentPane(viewer);
      frame.setTitle(this.getTitle());
      frame.setIconImage(MDIFrame.getInstance().getIconImage());
      frame.setVisible(true);
    } else JOptionPane.showMessageDialog(
        ClientUtils.getParentFrame(this),
        res.getErrorMessage(),
        ClientSettings.getInstance().getResources().getResource("print document"),
        JOptionPane.ERROR_MESSAGE
      );

  }


}

class PurchaseInvoiceDocFromDelivNotesFrame_confirmButton_actionAdapter implements java.awt.event.ActionListener {
  PurchaseInvoiceDocFromDelivNotesFrame adaptee;

  PurchaseInvoiceDocFromDelivNotesFrame_confirmButton_actionAdapter(PurchaseInvoiceDocFromDelivNotesFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.confirmButton_actionPerformed(e);
  }
}

class PurchaseInvoiceDocFromDelivNotesFrame_printButton_actionAdapter implements java.awt.event.ActionListener {
  PurchaseInvoiceDocFromDelivNotesFrame adaptee;

  PurchaseInvoiceDocFromDelivNotesFrame_printButton_actionAdapter(PurchaseInvoiceDocFromDelivNotesFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.printButton_actionPerformed(e);
  }
}
