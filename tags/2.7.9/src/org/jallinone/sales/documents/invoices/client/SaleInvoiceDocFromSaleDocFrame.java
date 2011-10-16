package org.jallinone.sales.documents.invoices.client;

import org.openswing.swing.table.client.Grid;
import org.openswing.swing.message.receive.java.*;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.sales.documents.java.GridSaleDocVO;
import org.openswing.swing.mdi.client.MDIFrame;
import org.jallinone.commons.client.CompanyFormController;
import org.jallinone.sales.documents.java.SaleDocPK;
import org.openswing.swing.util.java.Consts;
import org.jallinone.sales.documents.java.DetailSaleDocVO;
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
import org.jallinone.sales.documents.headerdiscounts.client.SaleDocDiscountsPanel;
import org.jallinone.sales.documents.headercharges.client.SaleDocChargesPanel;
import org.openswing.swing.client.GenericButtonController;
import org.jallinone.sales.documents.activities.client.SaleDocActivitiesPanel;
import org.jallinone.sales.documents.client.SaleDocument;
import org.jallinone.sales.documents.client.*;
import org.openswing.swing.lookup.client.LookupListener;
import java.util.Collection;
import java.util.HashMap;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JRViewer;
import org.jallinone.registers.payments.java.PaymentVO;
import org.openswing.swing.message.send.java.LookupValidationParams;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Detail Frame used for a sale invoice, created from a sale document (contract or order).</p>
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
public class SaleInvoiceDocFromSaleDocFrame extends InternalFrame implements InvoiceDocument {

  /** detail form controller */
  private SaleInvoiceDocFromSaleDocController controller = null;
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel mainPanel = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  JTabbedPane tabbedPane = new JTabbedPane();
  JPanel headerPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  JPanel headerButtonsPanel = new JPanel();
  BorderLayout borderLayout3 = new BorderLayout();
  JPanel linesPanel = new JPanel();
  BorderLayout borderLayout4 = new BorderLayout();
  Form headerFormPanel = new Form();
  InsertButton insertButton1 = new InsertButton();
  EditButton editButton1 = new EditButton();
  SaveButton saveButton1 = new SaveButton();
  ReloadButton reloadButton1 = new ReloadButton();
  DeleteButton deleteButton1 = new DeleteButton();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  SaleIdHeadPanel saleIdHeadPanel1 = new SaleIdHeadPanel(headerFormPanel,true,true);
  SaleCustomerHeadPanel saleCustomerHeadPanel1 = new SaleCustomerHeadPanel(false,headerFormPanel);
  SaleAgentPanel saleAgentPanel = new SaleAgentPanel(headerFormPanel);
  SaleNotesPanel saleNotesPanel = new SaleNotesPanel();
  SaleTotalsPanel saleTotalsPanel1 = new SaleTotalsPanel(headerFormPanel);
  SaleInvoiceDocRowsGridPanel rowsPanel = new SaleInvoiceDocRowsGridPanel(this,headerFormPanel,false,true);
  SaleWarehousePanel warePanel = new SaleWarehousePanel(headerFormPanel);
  JPanel addPanel = new JPanel();
  BorderLayout borderLayout5 = new BorderLayout();
  private SaleDocDiscountsPanel discPanel = new SaleDocDiscountsPanel(this,false);
  private SaleDocChargesPanel chargePanel = new SaleDocChargesPanel(this,false);
  private SaleDocActivitiesPanel actPanel = new SaleDocActivitiesPanel(this,false);
  GenericButton confirmButton = new GenericButton(new ImageIcon(ClientUtils.getImage("workflow.gif")));
  GenericButton printButton = new GenericButton(new ImageIcon(ClientUtils.getImage("printer.gif")));


  public SaleInvoiceDocFromSaleDocFrame(SaleInvoiceDocFromSaleDocController controller) {
    this.controller = controller;
    try {
      jbInit();
      setSize(750,630);
      setMinimumSize(new Dimension(750,630));
      setTitle(ClientSettings.getInstance().getResources().getResource("sale invoice from sale document"));

//      CustomizedControls customizedControls = new CustomizedControls(tabbedPane,headerFormPanel,ApplicationConsts.ID_SALE_ESTIMATE);

//      saleCustomerHeadPanel1.getCustomerDataLocator().getLookupFrameParams().put(ApplicationConsts.SUBJECT_TYPE,ApplicationConsts.SUBJECT_PEOPLE_CUSTOMER);
//      saleCustomerHeadPanel1.getCustomerDataLocator().getLookupValidationParameters().put(ApplicationConsts.SUBJECT_TYPE,ApplicationConsts.SUBJECT_PEOPLE_CUSTOMER);


      saleIdHeadPanel1.getControlDocRifLookup().getLookupController().addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          GridSaleDocVO vo = (GridSaleDocVO)saleIdHeadPanel1.getControlDocRifLookup().getLookupController().getLookupVO();
          if (vo.getDocNumberDOC01()!=null) {
            // pre-set header data...
            Response res = ClientUtils.getData("loadSaleDoc",new SaleDocPK(
                vo.getCompanyCodeSys01DOC01(),
                vo.getDocTypeDOC01(),
                vo.getDocYearDOC01(),
                vo.getDocNumberDOC01()
            ));
            if (!res.isError()) {
              DetailSaleDocVO saleDocVO = (DetailSaleDocVO)((VOResponse)res).getVo();
              DetailSaleDocVO oldSaleDocVO = (DetailSaleDocVO)headerFormPanel.getVOModel().getValueObject();
              saleDocVO.setCompanyCodeSys01Doc01DOC01(vo.getCompanyCodeSys01DOC01());
              saleDocVO.setDocTypeDoc01DOC01(vo.getDocTypeDOC01());
              saleDocVO.setDocYearDoc01DOC01(vo.getDocYearDOC01());
              saleDocVO.setDocNumberDoc01DOC01(vo.getDocNumberDOC01());
              saleDocVO.setDocSequenceDoc01DOC01(vo.getDocSequenceDOC01());
              saleDocVO.setDocTypeDOC01(ApplicationConsts.SALE_INVOICE_FROM_SD_DOC_TYPE);
              saleDocVO.setDocNumberDOC01(null);
              saleDocVO.setDocSequenceDOC01(null);
              saleDocVO.setDocStateDOC01(ApplicationConsts.OPENED);
              saleDocVO.setDocDateDOC01(oldSaleDocVO.getDocDateDOC01());
              headerFormPanel.getVOModel().setValueObject(saleDocVO);
              headerFormPanel.pull();
            }
          }
          else {
            // reset header data...
            try {
              DetailSaleDocVO saleDocVO = new DetailSaleDocVO();
              SaleInvoiceDocFromSaleDocFrame.this.controller.createPersistentObject(saleDocVO);
              saleDocVO.setDocTypeDoc01DOC01(ApplicationConsts.SALE_ORDER_DOC_TYPE);
              headerFormPanel.getVOModel().setValueObject(saleDocVO);
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
    attributeNameToDisable.add("allowanceDOC01");
    attributeNameToDisable.add("depositDOC01");
    attributeNameToDisable.add("customerCodeSAL07");
    attributeNameToDisable.add("pricelistCodeSal01DOC01");
    attributeNameToDisable.add("paymentCodeReg10DOC01");
    attributeNameToDisable.add("warehouseCodeWar01DOC01");
    attributeNameToDisable.add("destinationCodeReg18DOC01");
    headerFormPanel.addInputControlAttributesNotEditableOnState(attributeNameToDisable,"docStateDOC01",ApplicationConsts.OPENED);

    // disable customer and pricelist when the at least one line is added...
    attributeNameToDisable = new HashSet();
    attributeNameToDisable.add("customerCodeSAL07");
    attributeNameToDisable.add("pricelistCodeSal01DOC01");
    attributeNameToDisable.add("docDateDOC01");
    attributeNameToDisable.add("paymentCodeReg10DOC01");
    attributeNameToDisable.add("paymentCodeReg10DOC01");
    attributeNameToDisable.add("warehouseCodeWar01DOC01");
    attributeNameToDisable.add("destinationCodeReg18DOC01");
    headerFormPanel.addInputControlAttributesNotEditableOnState(attributeNameToDisable,"docStateDOC01",ApplicationConsts.HEADER_BLOCKED);


    // disable all editable fields when the order is confimed (closed)...
    attributeNameToDisable = new HashSet();
    attributeNameToDisable.add("allowanceDOC01");
    attributeNameToDisable.add("depositDOC01");
    attributeNameToDisable.add("customerCodeSAL07");
    attributeNameToDisable.add("pricelistCodeSal01DOC01");
    attributeNameToDisable.add("docDateDOC01");
    attributeNameToDisable.add("paymentCodeReg10DOC01");
    attributeNameToDisable.add("destinationCodeReg18DOC01");
    attributeNameToDisable.add("noteDOC01");
    attributeNameToDisable.add("headingNoteDOC01");
    attributeNameToDisable.add("footerNoteDOC01");
    attributeNameToDisable.add("deliveryNoteDOC01");
    attributeNameToDisable.add("warehouseCodeWar01DOC01");
    headerFormPanel.addInputControlAttributesNotEditableOnState(attributeNameToDisable,"docStateDOC01",ApplicationConsts.CLOSED);

    HashSet buttonsToDisable = new HashSet();
    buttonsToDisable.add(editButton1);
    buttonsToDisable.add(deleteButton1);
    headerFormPanel.addButtonsNotEnabled(buttonsToDisable,this);

  }


  private void jbInit() throws Exception {
    printButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("print document"));
    printButton.addActionListener(new SaleInvoiceDocFromSaleDocFrame_printButton_actionAdapter(this));

    saleIdHeadPanel1.getControlDocRifLookup().setRequired(true);

    addPanel.setLayout(borderLayout5);
    confirmButton.addActionListener(new SaleInvoiceDocFromSaleDocFrame_confirmButton_actionAdapter(this));

    headerFormPanel.setFunctionId("DOC01_INV_FROM_SD");
    headerFormPanel.setFormController(controller);
    headerFormPanel.setVOClassName("org.jallinone.sales.documents.java.DetailSaleDocVO");

    headerPanel.setLayout(borderLayout3);
    headerButtonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    mainPanel.setLayout(borderLayout2);
    this.getContentPane().setLayout(borderLayout1);
    linesPanel.setLayout(borderLayout4);
    insertButton1.setText("insertButton1");
    reloadButton1.setText("reloadButton1");
    deleteButton1.setText("deleteButton1");
    headerFormPanel.setLayout(gridBagLayout1);
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
    headerFormPanel.add(saleIdHeadPanel1,   new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    headerFormPanel.add(saleCustomerHeadPanel1,     new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    headerFormPanel.add(warePanel,   new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    headerFormPanel.add(saleTotalsPanel1,    new GridBagConstraints(0, 3, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

    addPanel.add(saleAgentPanel,BorderLayout.NORTH);
    addPanel.add(saleNotesPanel,BorderLayout.CENTER);

    confirmButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("invoice closing"));
    confirmButton.setEnabled(false);

    tabbedPane.add(addPanel, "notes");
    tabbedPane.add(linesPanel,   "items");
    tabbedPane.add(discPanel,   "customer discounts");
    tabbedPane.add(chargePanel,   "charges");
    tabbedPane.add(actPanel,   "activities");

    headerFormPanel.addLinkedPanel(addPanel);


    tabbedPane.setTitleAt(0,ClientSettings.getInstance().getResources().getResource("header"));
    tabbedPane.setTitleAt(1,ClientSettings.getInstance().getResources().getResource("notes"));
    tabbedPane.setTitleAt(2,ClientSettings.getInstance().getResources().getResource("items"));
    tabbedPane.setTitleAt(3,ClientSettings.getInstance().getResources().getResource("customer discounts"));
    tabbedPane.setTitleAt(4,ClientSettings.getInstance().getResources().getResource("charges"));
    tabbedPane.setTitleAt(5,ClientSettings.getInstance().getResources().getResource("sale activities"));

  }


  public Form getHeaderFormPanel() {
    return headerFormPanel;
  }


  public void setButtonsEnabled(boolean enabled) {
    rowsPanel.setButtonsEnabled(enabled);
    discPanel.setEnabled(enabled);
    chargePanel.setEnabled(enabled);
    actPanel.setEnabled(enabled);
  }


  public void loadDataCompleted(boolean error,SaleDocPK pk) {
    DetailSaleDocVO vo = (DetailSaleDocVO)headerFormPanel.getVOModel().getValueObject();

    saleTotalsPanel1.getControlTaxableIncome().setDecimals(vo.getDecimalsREG03().intValue());
    saleTotalsPanel1.getControlTaxableIncome().setCurrencySymbol(vo.getCurrencySymbolREG03());
    saleTotalsPanel1.getControlTaxableIncome().setDecimalSymbol(vo.getDecimalSymbolREG03().charAt(0));
    saleTotalsPanel1.getControlTaxableIncome().setGroupingSymbol(vo.getThousandSymbolREG03().charAt(0));

    saleTotalsPanel1.getControlTotal().setDecimals(vo.getDecimalsREG03().intValue());
    saleTotalsPanel1.getControlTotal().setCurrencySymbol(vo.getCurrencySymbolREG03());
    saleTotalsPanel1.getControlTotal().setDecimalSymbol(vo.getDecimalSymbolREG03().charAt(0));
    saleTotalsPanel1.getControlTotal().setGroupingSymbol(vo.getThousandSymbolREG03().charAt(0));

    saleTotalsPanel1.getControlTotalVat().setDecimals(vo.getDecimalsREG03().intValue());
    saleTotalsPanel1.getControlTotalVat().setCurrencySymbol(vo.getCurrencySymbolREG03());
    saleTotalsPanel1.getControlTotalVat().setDecimalSymbol(vo.getDecimalSymbolREG03().charAt(0));
    saleTotalsPanel1.getControlTotalVat().setGroupingSymbol(vo.getThousandSymbolREG03().charAt(0));


    saleTotalsPanel1.getControlAllowance().setDecimals(vo.getDecimalsREG03().intValue());
    saleTotalsPanel1.getControlAllowance().setCurrencySymbol(vo.getCurrencySymbolREG03());
    saleTotalsPanel1.getControlAllowance().setDecimalSymbol(vo.getDecimalSymbolREG03().charAt(0));
    saleTotalsPanel1.getControlAllowance().setGroupingSymbol(vo.getThousandSymbolREG03().charAt(0));

    saleTotalsPanel1.getControlDeposit().setDecimals(vo.getDecimalsREG03().intValue());
    saleTotalsPanel1.getControlDeposit().setCurrencySymbol(vo.getCurrencySymbolREG03());
    saleTotalsPanel1.getControlDeposit().setDecimalSymbol(vo.getDecimalSymbolREG03().charAt(0));
    saleTotalsPanel1.getControlDeposit().setGroupingSymbol(vo.getThousandSymbolREG03().charAt(0));

    rowsPanel.setParentVO(vo);
    rowsPanel.getGrid().getOtherGridParams().put(ApplicationConsts.SALE_DOC_PK,pk);
    rowsPanel.getGrid().reloadData();

    discPanel.setParentVO(vo);
    discPanel.getGrid().getOtherGridParams().put(ApplicationConsts.SALE_DOC_PK,pk);
    discPanel.getGrid().reloadData();

    chargePanel.setParentVO(vo);
    chargePanel.getGrid().getOtherGridParams().put(ApplicationConsts.SALE_DOC_PK,pk);
    chargePanel.getGrid().reloadData();

    actPanel.setParentVO(vo);
    actPanel.getGrid().getOtherGridParams().put(ApplicationConsts.SALE_DOC_PK,pk);
    actPanel.getGrid().reloadData();

    if (vo.getDocStateDOC01().equals(ApplicationConsts.CLOSED) ||
        vo.getDocStateDOC01().equals(ApplicationConsts.CONFIRMED)) {
      confirmButton.setEnabled(false);
      setButtonsEnabled(false);
      rowsPanel.setButtonsEnabled(false);
    }
    if (!vo.getDocStateDOC01().equals(ApplicationConsts.HEADER_BLOCKED)) {
      confirmButton.setEnabled(false);
    }
    if (vo.getDocStateDOC01().equals(ApplicationConsts.HEADER_BLOCKED)) {
      confirmButton.setEnabled(true);
    }

    if (vo.getDocStateDOC01().equals(ApplicationConsts.CONFIRMED) ||
        vo.getDocStateDOC01().equals(ApplicationConsts.CLOSED) ||
        vo.getDocStateDOC01().equals(ApplicationConsts.HEADER_BLOCKED)) {
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
    DetailSaleDocVO vo = (DetailSaleDocVO)headerFormPanel.getVOModel().getValueObject();
    if (vo!=null && vo.getDocStateDOC01()!=null && vo.getDocStateDOC01().equals(ApplicationConsts.CLOSED))
      return true;
    else
      return false;
  }



  public SaleInvoiceDocRowsGridPanel getRowsPanel() {
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
                                  ClientSettings.getInstance().getResources().getResource("close sale invoice?"),
                                  ClientSettings.getInstance().getResources().getResource("invoice closing"),
                                  JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {

      Response res = ClientUtils.getData("closeSaleDoc",controller.getPk());
      if (!res.isError()) {
        confirmButton.setEnabled(false);
        headerFormPanel.setMode(Consts.READONLY);
        headerFormPanel.executeReload();
        if (getInvoices()!=null)
          getInvoices().reloadCurrentBlockOfData();


					// check if there is only one instalment and this instalment has 0 instalment days
					DetailSaleDocVO vo = (DetailSaleDocVO)headerFormPanel.getVOModel().getValueObject();
					res = ClientUtils.getData("validatePaymentCode",new LookupValidationParams(vo.getPaymentCodeReg10DOC01(),new HashMap()));
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
												vo.getCompanyCodeSys01DOC01(),
												vo.getDocTypeDOC01(),
												vo.getDocYearDOC01(),
												vo.getDocNumberDOC01(),
												vo.getDocSequenceDOC01()
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


  public SaleDocDiscountsPanel getDiscPanel() {
    return discPanel;
  }


  public SaleDocChargesPanel getChargePanel() {
    return chargePanel;
  }


  public SaleDocActivitiesPanel getActPanel() {
    return actPanel;
  }


  public DetailSaleDocVO getParentVO() {
    return (DetailSaleDocVO)headerFormPanel.getVOModel().getValueObject();
  }
  public SaleCustomerHeadPanel getSaleCustomerHeadPanel1() {
    return saleCustomerHeadPanel1;
  }
  public SaleWarehousePanel getWarePanel() {
    return warePanel;
  }


  public String getFunctionId() {
    return "DOC01_INV_FROM_SD";
  }


  public void enabledConfirmButton() {
    confirmButton.setEnabled(true);
  }

  void printButton_actionPerformed(ActionEvent e) {
    DetailSaleDocVO vo = (DetailSaleDocVO)headerFormPanel.getVOModel().getValueObject();

    HashMap params = new HashMap();
    params.put("COMPANY_CODE",vo.getCompanyCodeSys01DOC01());
    params.put("DOC_TYPE",vo.getDocTypeDOC01());
    params.put("DOC_YEAR",vo.getDocYearDOC01());
    params.put("DOC_NUMBER",vo.getDocNumberDOC01());

    HashMap map = new HashMap();
    map.put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01DOC01());
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

class SaleInvoiceDocFromSaleDocFrame_confirmButton_actionAdapter implements java.awt.event.ActionListener {
  SaleInvoiceDocFromSaleDocFrame adaptee;

  SaleInvoiceDocFromSaleDocFrame_confirmButton_actionAdapter(SaleInvoiceDocFromSaleDocFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.confirmButton_actionPerformed(e);
  }
}

class SaleInvoiceDocFromSaleDocFrame_printButton_actionAdapter implements java.awt.event.ActionListener {
  SaleInvoiceDocFromSaleDocFrame adaptee;

  SaleInvoiceDocFromSaleDocFrame_printButton_actionAdapter(SaleInvoiceDocFromSaleDocFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.printButton_actionPerformed(e);
  }
}
