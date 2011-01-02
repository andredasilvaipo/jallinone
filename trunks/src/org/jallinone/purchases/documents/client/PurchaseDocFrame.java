package org.jallinone.purchases.documents.client;

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
import java.util.HashMap;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JRViewer;
import org.jallinone.purchases.documents.java.PurchaseDocRowPK;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Detail Frame used for a purchase order.</p>
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
public class PurchaseDocFrame extends InternalFrame implements GenericButtonController {

  /** detail form controller */
  private PurchaseDocController controller = null;
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
  PurchaseIdHeadPanel purchaseIdHeadPanel1 = new PurchaseIdHeadPanel(headerFormPanel,false,false);
  PurchaseSupplierHeadPanel purchaseSupplierHeadPanel1 = new PurchaseSupplierHeadPanel(headerFormPanel);
  PurchaseDiscChargesPanel purchaseDiscChargesPanel1 = new PurchaseDiscChargesPanel(headerFormPanel);
  PurchaseTotalsPanel purchaseTotalsPanel1 = new PurchaseTotalsPanel();
  PurchaseDocRowsGridPanel rowsPanel = new PurchaseDocRowsGridPanel(this,headerFormPanel);
  GenericButton confirmButton = new GenericButton(new ImageIcon(ClientUtils.getImage("workflow.gif")));
  PurchaseWarehousePanel warePanel = new PurchaseWarehousePanel(headerFormPanel);
  GenericButton printButton = new GenericButton(new ImageIcon(ClientUtils.getImage("printer.gif")));
  GenericButton printLabelsButton = new GenericButton(new ImageIcon(ClientUtils.getImage("barcode.gif")));
  NavigatorBar navigatorBar = new NavigatorBar();


  public PurchaseDocFrame(PurchaseDocController controller) {
    this.controller = controller;
    try {
      jbInit();
      setSize(750,610);
      setMinimumSize(new Dimension(750,610));
      setTitle(ClientSettings.getInstance().getResources().getResource("purchase order"));


      // link the parent grid to the current Form...
      HashSet pk = new HashSet();
      pk.add("companyCodeSys01DOC06");
      pk.add("docTypeDOC06");
      pk.add("docYearDOC06");
      pk.add("docNumberDOC06");
      if (controller.getParentFrame()!=null)
        headerFormPanel.linkGrid(controller.getParentFrame().getGrid(),pk,true,true,true,navigatorBar);


      CustomizedControls customizedControls = new CustomizedControls(tabbedPane,headerFormPanel,ApplicationConsts.ID_PURCHASE_ORDER);

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
    headerFormPanel.addInputControlAttributesNotEditableOnState(attributeNameToDisable,"docStateDOC06",ApplicationConsts.OPENED);

    // disable supplier and pricelist when the at least one line is added...
    attributeNameToDisable = new HashSet();
    attributeNameToDisable.add("supplierCodePUR01");
    attributeNameToDisable.add("pricelistCodePur03DOC06");
    attributeNameToDisable.add("docDateDOC06");
    attributeNameToDisable.add("paymentCodeReg10DOC06");
    headerFormPanel.addInputControlAttributesNotEditableOnState(attributeNameToDisable,"docStateDOC06",ApplicationConsts.HEADER_BLOCKED);


    // disable all editable fields when the order is confimed (closed)...
    attributeNameToDisable = new HashSet();
    attributeNameToDisable.add("supplierCodePUR01");
    attributeNameToDisable.add("pricelistCodePur03DOC06");
    attributeNameToDisable.add("docDateDOC06");
    attributeNameToDisable.add("paymentCodeReg10DOC06");
    attributeNameToDisable.add("noteDOC06");
    attributeNameToDisable.add("discountValueDOC06");
    attributeNameToDisable.add("discountPercDOC06");
    attributeNameToDisable.add("chargeValueDOC06");
    attributeNameToDisable.add("chargePercDOC06");
    attributeNameToDisable.add("warehouseCodeWar01DOC06");
    headerFormPanel.addInputControlAttributesNotEditableOnState(attributeNameToDisable,"docStateDOC06",ApplicationConsts.CONFIRMED);

    HashSet buttonsToDisable = new HashSet();
    buttonsToDisable.add(editButton1);
    buttonsToDisable.add(deleteButton1);
    headerFormPanel.addButtonsNotEnabled(buttonsToDisable,this);

  }


  private void jbInit() throws Exception {
    printButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("print document"));
    printButton.addActionListener(new PurchaseDocFrame_printButton_actionAdapter(this));

    printLabelsButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("print barcode labels"));
    printLabelsButton.addActionListener(new PurchaseDocFrame_printLabelsButton_actionAdapter(this));

    headerFormPanel.setFunctionId("DOC06_ORDERS");
    headerFormPanel.setFormController(controller);
    headerFormPanel.setVOClassName("org.jallinone.purchases.documents.java.DetailPurchaseDocVO");

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
    confirmButton.addActionListener(new PurchaseDocFrame_confirmButton_actionAdapter(this));
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
    printLabelsButton.setEnabled(false);
    headerButtonsPanel.add(printButton, null);
    headerButtonsPanel.add(printLabelsButton, null);
    headerButtonsPanel.add(navigatorBar, null);
    tabbedPane.add(linesPanel,   "lines");
    linesPanel.add(rowsPanel, BorderLayout.CENTER);
    headerPanel.add(headerFormPanel,  BorderLayout.CENTER);
    headerFormPanel.add(purchaseIdHeadPanel1,   new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    headerFormPanel.add(purchaseSupplierHeadPanel1,    new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    headerFormPanel.add(warePanel,   new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    headerFormPanel.add(purchaseDiscChargesPanel1,   new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    headerFormPanel.add(purchaseTotalsPanel1,   new GridBagConstraints(0, 4, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

    tabbedPane.setTitleAt(0,ClientSettings.getInstance().getResources().getResource("header"));
    tabbedPane.setTitleAt(1,ClientSettings.getInstance().getResources().getResource("lines"));

    confirmButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("order confirmation"));
    confirmButton.setEnabled(false);
  }


  public Form getHeaderFormPanel() {
    return headerFormPanel;
  }


  public void setButtonsEnabled(boolean enabled) {
    rowsPanel.setButtonsEnabled(enabled);
    if (!enabled)
      confirmButton.setEnabled(false);
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

    this.setTitle(ClientSettings.getInstance().getResources().getResource("purchase order")+" "+(vo.getDocSequenceDOC06()!=null?" - "+vo.getDocYearDOC06()+"/"+vo.getDocSequenceDOC06():"")+" - "+vo.getName_1REG04()+" "+(vo.getName_2REG04()==null?"":vo.getName_2REG04()));

    rowsPanel.setParentVO(vo);
    rowsPanel.getGrid().getOtherGridParams().put(ApplicationConsts.PURCHASE_DOC_PK,pk);
    rowsPanel.getGrid().reloadData();

    if (vo.getDocStateDOC06().equals(ApplicationConsts.CONFIRMED) ||
        vo.getDocStateDOC06().equals(ApplicationConsts.CLOSED)) {
      confirmButton.setEnabled(false);
      setButtonsEnabled(false);
      rowsPanel.setButtonsEnabled(false);
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
      printLabelsButton.setEnabled(true);
    }
    else {
      printButton.setEnabled(false);
      printLabelsButton.setEnabled(false);
    }

  }


  /**
   * Method called by GenericButton.setEnabled method to check if the button must be disabled.
   * @param button button whose abilitation must be checked
   * @return <code>true</code> if no policy is defined in the form/grid for the specified button, <code>false</code> if there exists a disabilitation policy for the specified button (through addButtonsNotEnabledOnState form/grid method)
   */
  public boolean isButtonDisabled(GenericButton button) {
    DetailPurchaseDocVO vo = null;
    if (headerFormPanel!=null && headerFormPanel.getVOModel()!=null)
      vo = (DetailPurchaseDocVO)headerFormPanel.getVOModel().getValueObject();
    if (vo!=null && vo.getDocStateDOC06()!=null &&
        (vo.getDocStateDOC06().equals(ApplicationConsts.CONFIRMED) ||
         vo.getDocStateDOC06().equals(ApplicationConsts.CLOSED)))
      return true;
    else
      return false;
  }


  public void enabledConfirmButton() {
    confirmButton.setEnabled(true);
  }


  public PurchaseDocRowsGridPanel getRowsPanel() {
    return rowsPanel;
  }


  public GridControl getOrders() {
    return controller.getParentFrame().getGrid();
  }


  void confirmButton_actionPerformed(ActionEvent e) {
    // view confirmation dialog...
    if (JOptionPane.showConfirmDialog(ClientUtils.getParentFrame(this),
                                  ClientSettings.getInstance().getResources().getResource("confirm purchase order?"),
                                  ClientSettings.getInstance().getResources().getResource("order confirmation"),
                                  JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {

      Response res = ClientUtils.getData("confirmPurchaseOrder",controller.getPk());
      if (!res.isError()) {
        headerFormPanel.setMode(Consts.READONLY);
        headerFormPanel.executeReload();
        getOrders().reloadCurrentBlockOfData();
      }

    }

  }


  void printLabelsButton_actionPerformed(ActionEvent e) {
    DetailPurchaseDocVO vo = (DetailPurchaseDocVO)headerFormPanel.getVOModel().getValueObject();

    HashMap map = new HashMap();
    map.put(
      ApplicationConsts.PURCHASE_DOC_PK,
      new PurchaseDocPK(
        vo.getCompanyCodeSys01DOC06(),
        vo.getDocTypeDOC06(),
        vo.getDocYearDOC06(),
        vo.getDocNumberDOC06()
      )
    );
    Response res = ClientUtils.getData("createBarcodeLabelsDataFromPurchaseDoc",map);
    if (res.isError()) {
      JOptionPane.showMessageDialog(
        ClientUtils.getParentFrame(this),
        res.getErrorMessage(),
        ClientSettings.getInstance().getResources().getResource("print barcode labels"),
        JOptionPane.ERROR_MESSAGE
      );
    }
    else {
      Object reportId = ((VOResponse)res).getVo();


      HashMap params = new HashMap();
      params.put("REPORT_ID",reportId);

      map = new HashMap();
      map.put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01DOC06());
      map.put(ApplicationConsts.FUNCTION_CODE_SYS06,"BARCODE_LABELS");
      map.put(ApplicationConsts.EXPORT_PARAMS,params);
      res = ClientUtils.getData("getJasperReport",map);
      if (!res.isError()) {
        JasperPrint print = (JasperPrint)((VOResponse)res).getVo();
        JRViewer viewer = new JRViewer(print);
        JFrame frame = new JFrame();
        frame.setSize(MDIFrame.getInstance().getSize());
        frame.setContentPane(viewer);
        frame.setTitle(this.getTitle());
        frame.setIconImage(MDIFrame.getInstance().getIconImage());
        frame.setVisible(true);
        res = ClientUtils.getData("deleteBarcodeLabelsData",reportId);
      } else
        JOptionPane.showMessageDialog(
          ClientUtils.getParentFrame(this),
          res.getErrorMessage(),
          ClientSettings.getInstance().getResources().getResource("print barcode labels"),
          JOptionPane.ERROR_MESSAGE
        );
    }
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
    } else
      JOptionPane.showMessageDialog(
        ClientUtils.getParentFrame(this),
        res.getErrorMessage(),
        ClientSettings.getInstance().getResources().getResource("print document"),
        JOptionPane.ERROR_MESSAGE
      );

  }



}

class PurchaseDocFrame_confirmButton_actionAdapter implements java.awt.event.ActionListener {
  PurchaseDocFrame adaptee;

  PurchaseDocFrame_confirmButton_actionAdapter(PurchaseDocFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.confirmButton_actionPerformed(e);
  }
}

class PurchaseDocFrame_printButton_actionAdapter implements java.awt.event.ActionListener {
  PurchaseDocFrame adaptee;

  PurchaseDocFrame_printButton_actionAdapter(PurchaseDocFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.printButton_actionPerformed(e);
  }
}

class PurchaseDocFrame_printLabelsButton_actionAdapter implements java.awt.event.ActionListener {
  PurchaseDocFrame adaptee;

  PurchaseDocFrame_printLabelsButton_actionAdapter(PurchaseDocFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.printLabelsButton_actionPerformed(e);
  }
}
