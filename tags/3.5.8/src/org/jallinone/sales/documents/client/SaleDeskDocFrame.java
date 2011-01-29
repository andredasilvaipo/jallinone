package org.jallinone.sales.documents.client;

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
import javax.swing.border.*;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.openswing.swing.lookup.client.LookupListener;
import java.util.Collection;
import java.util.Calendar;
import java.math.BigDecimal;
import org.jallinone.sales.documents.invoices.client.SaleInvoiceDocFromSaleDocController;
import java.util.HashMap;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JRViewer;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Detail Frame used for a desk selling.</p>
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
public class SaleDeskDocFrame extends InternalFrame implements SaleDocument {

  /** detail form controller */
  private SaleDeskDocController controller = null;
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
  SaleIdHeadPanel saleIdHeadPanel1 = new SaleIdHeadPanel(headerFormPanel,false,false);
  SaleCustomerHeadPanel saleCustomerHeadPanel1 = new SaleCustomerHeadPanel(true,headerFormPanel);
  SaleTotalsPanel saleTotalsPanel1 = new SaleTotalsPanel(headerFormPanel);
  SaleDeskDocRowsGridPanel rowsPanel = new SaleDeskDocRowsGridPanel(this,headerFormPanel);
  GenericButton confirmButton = new GenericButton(new ImageIcon(ClientUtils.getImage("workflow.gif")));
  JPanel warePanel = new JPanel();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  TextControl controlDescr = new TextControl();
  CodLookupControl controlWarehouseCode = new CodLookupControl();
  LabelControl labelWarehouseCode = new LabelControl();
  TitledBorder titledBorder1;

  LookupController wareController = new LookupController();
  LookupServerDataLocator wareDataLocator = new LookupServerDataLocator();
  private SaleDeskHeadDiscountPanel discPanel = new SaleDeskHeadDiscountPanel(headerFormPanel);
  GenericButton printButton = new GenericButton(new ImageIcon(ClientUtils.getImage("printer.gif")));
  NavigatorBar navigatorBar = new NavigatorBar();


  public SaleDeskDocFrame(SaleDeskDocController controller) {
    this.controller = controller;
    try {
      jbInit();
      setSize(750,630);
      setMinimumSize(new Dimension(750,630));
      setTitle(ClientSettings.getInstance().getResources().getResource("desk selling"));

      CustomizedControls customizedControls = new CustomizedControls(tabbedPane,headerFormPanel,ApplicationConsts.ID_SALE_DESK);

      saleCustomerHeadPanel1.getCustomerDataLocator().getLookupFrameParams().put(ApplicationConsts.SUBJECT_TYPE,ApplicationConsts.SUBJECT_PEOPLE_CUSTOMER);
      saleCustomerHeadPanel1.getCustomerDataLocator().getLookupValidationParameters().put(ApplicationConsts.SUBJECT_TYPE,ApplicationConsts.SUBJECT_PEOPLE_CUSTOMER);


      // link the parent grid to the current Form...
      HashSet pk = new HashSet();
      pk.add("companyCodeSys01DOC01");
      pk.add("docTypeDOC01");
      pk.add("docYearDOC01");
      pk.add("docNumberDOC01");
      if (controller.getParentFrame()!=null)
        headerFormPanel.linkGrid(controller.getParentFrame().getGrid(),pk,true,true,true,navigatorBar);


      // warehouse lookup...
      wareDataLocator.setGridMethodName("loadWarehouses");
      wareDataLocator.setValidationMethodName("validateWarehouseCode");

      controlWarehouseCode.setLookupController(wareController);
			controlWarehouseCode.setLinkLabel(labelWarehouseCode);
      controlWarehouseCode.setControllerMethodName("getWarehousesList");
      wareController.setLookupDataLocator(wareDataLocator);
      wareController.setFrameTitle("warehouses");
      wareController.setLookupValueObjectClassName("org.jallinone.warehouse.java.WarehouseVO");
      wareController.addLookup2ParentLink("warehouseCodeWAR01", "warehouseCodeWar01DOC01");
      wareController.addLookup2ParentLink("descriptionWAR01","descriptionWar01DOC01");
      wareController.addLookup2ParentLink("progressiveHie02WAR01","progressiveHie02WAR01");
      wareController.addLookup2ParentLink("progressiveHie01HIE02","progressiveHie01HIE02");
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

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {}

        public void beforeLookupAction(ValueObject parentVO) {
          DetailSaleDocVO vo = (DetailSaleDocVO)headerFormPanel.getVOModel().getValueObject();
          wareDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01DOC01());
          wareDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01DOC01());
        }

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
    saleTotalsPanel1.removeAllowanceAndDeposit();

    // disable discounts and charges when the no lines are not yet to added...
    HashSet attributeNameToDisable = new HashSet();
    attributeNameToDisable.add("allowanceDOC01");
    attributeNameToDisable.add("depositDOC01");
    attributeNameToDisable.add("discountValueDOC01");
    attributeNameToDisable.add("discountPercDOC01");
    headerFormPanel.addInputControlAttributesNotEditableOnState(attributeNameToDisable,"docStateDOC01",ApplicationConsts.OPENED);

    // disable customer and pricelist when the at least one line is added...
    attributeNameToDisable = new HashSet();
    attributeNameToDisable.add("customerCodeSAL07");
    attributeNameToDisable.add("pricelistCodeSal01DOC01");
    attributeNameToDisable.add("docDateDOC01");
//    attributeNameToDisable.add("paymentCodeReg10DOC01");
    attributeNameToDisable.add("warehouseCodeWar01DOC01");
    headerFormPanel.addInputControlAttributesNotEditableOnState(attributeNameToDisable,"docStateDOC01",ApplicationConsts.HEADER_BLOCKED);


    // disable all editable fields when the order is closed...
    attributeNameToDisable = new HashSet();
    attributeNameToDisable.add("allowanceDOC01");
    attributeNameToDisable.add("depositDOC01");
    attributeNameToDisable.add("customerCodeSAL07");
    attributeNameToDisable.add("pricelistCodeSal01DOC01");
    attributeNameToDisable.add("docDateDOC01");
    attributeNameToDisable.add("paymentCodeReg10DOC01");
    attributeNameToDisable.add("warehouseCodeWar01DOC01");
    attributeNameToDisable.add("discountValueDOC01");
    attributeNameToDisable.add("discountPercDOC01");
    headerFormPanel.addInputControlAttributesNotEditableOnState(attributeNameToDisable,"docStateDOC01",ApplicationConsts.CLOSED);

    HashSet buttonsToDisable = new HashSet();
    buttonsToDisable.add(editButton1);
    buttonsToDisable.add(deleteButton1);
    headerFormPanel.addButtonsNotEnabled(buttonsToDisable,this);

  }


  private void jbInit() throws Exception {
    printButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("print document"));
    printButton.addActionListener(new SaleDeskDocFrame_printButton_actionAdapter(this));

    titledBorder1 = new TitledBorder("");
    headerFormPanel.setFunctionId("DOC01_DESKSALES");
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
    confirmButton.addActionListener(new SaleDeskDocFrame_confirmButton_actionAdapter(this));
    warePanel.setLayout(gridBagLayout2);
    controlDescr.setCanCopy(true);
    controlDescr.setEnabledOnInsert(false);
    controlDescr.setEnabledOnEdit(false);
    controlDescr.setAttributeName("descriptionWar01DOC01");
    controlDescr.setRequired(false);
    controlWarehouseCode.setAttributeName("warehouseCodeWar01DOC01");
    controlWarehouseCode.setCanCopy(true);
    controlWarehouseCode.setLinkLabel(labelWarehouseCode);
    controlWarehouseCode.setMaxCharacters(20);
    controlWarehouseCode.setRequired(true);
    labelWarehouseCode.setText("warehouseCodeWAR01");
    warePanel.setBorder(titledBorder1);
    titledBorder1.setTitle(ClientSettings.getInstance().getResources().getResource("warehouse"));
    titledBorder1.setTitleColor(Color.blue);
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
    headerButtonsPanel.add(navigatorBar, null);
    linesPanel.add(rowsPanel, BorderLayout.CENTER);
    headerPanel.add(headerFormPanel,  BorderLayout.CENTER);
    headerFormPanel.add(saleIdHeadPanel1,   new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    headerFormPanel.add(saleCustomerHeadPanel1,     new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    headerFormPanel.add(warePanel,   new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    warePanel.add(controlDescr,   new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    warePanel.add(controlWarehouseCode,   new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 20, 0));
    warePanel.add(labelWarehouseCode,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    headerFormPanel.add(discPanel,    new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    headerFormPanel.add(saleTotalsPanel1,    new GridBagConstraints(0, 4, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

    tabbedPane.add(linesPanel,   "items");

    tabbedPane.setTitleAt(0,ClientSettings.getInstance().getResources().getResource("header"));
    tabbedPane.setTitleAt(1,ClientSettings.getInstance().getResources().getResource("items"));

    confirmButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("desk selling confirmation"));
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


  public void loadDataCompleted(boolean error,SaleDocPK pk) {
    DetailSaleDocVO vo = (DetailSaleDocVO)headerFormPanel.getVOModel().getValueObject();

    this.setTitle(ClientSettings.getInstance().getResources().getResource("desk selling")+(vo.getDocSequenceDOC01()!=null?" - "+vo.getDocYearDOC01()+"/"+vo.getDocSequenceDOC01():"")+" - "+vo.getName_1REG04()+" "+(vo.getName_2REG04()==null?"":vo.getName_2REG04()));

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

    discPanel.getControlDiscValue().setDecimals(vo.getDecimalsREG03().intValue());
    discPanel.getControlDiscValue().setCurrencySymbol(vo.getCurrencySymbolREG03());
    discPanel.getControlDiscValue().setDecimalSymbol(vo.getDecimalSymbolREG03().charAt(0));
    discPanel.getControlDiscValue().setGroupingSymbol(vo.getThousandSymbolREG03().charAt(0));

    rowsPanel.setParentVO(vo);
    rowsPanel.getGrid().getOtherGridParams().put(ApplicationConsts.SALE_DOC_PK,pk);
    rowsPanel.getGrid().reloadData();

    if (vo.getDocStateDOC01().equals(ApplicationConsts.CLOSED)) {
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
    if (headerFormPanel.getVOModel()==null)
      return false;
    DetailSaleDocVO vo = (DetailSaleDocVO)headerFormPanel.getVOModel().getValueObject();
    if (vo!=null && vo.getDocStateDOC01()!=null && vo.getDocStateDOC01().equals(ApplicationConsts.CLOSED))
      return true;
    else
      return false;
  }


  public void enabledConfirmButton() {
    confirmButton.setEnabled(true);
  }


  public SaleDeskDocRowsGridPanel getRowsPanel() {
    return rowsPanel;
  }


  public GridControl getDesks() {
    return controller.getParentFrame().getGrid();
  }


  void confirmButton_actionPerformed(ActionEvent e) {
    new Thread() {
      public void run() {
        closeDoc();
      }
    }.start();
  }


  private void closeDoc() {
    try {
      // view confirmation dialog...
      if (JOptionPane.showConfirmDialog(ClientUtils.getParentFrame(this),
                                    ClientSettings.getInstance().getResources().getResource("confirm desk selling?"),
                                    ClientSettings.getInstance().getResources().getResource("desk selling confirmation"),
                                    JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {

        Response res = ClientUtils.getData("closeSaleDoc",controller.getPk());
        if (!res.isError()) {
          headerFormPanel.setMode(Consts.READONLY);
          headerFormPanel.executeReload();
          getDesks().reloadCurrentBlockOfData();

          // prompt user if a sale invoice must be created...
          if (JOptionPane.showConfirmDialog(ClientUtils.getParentFrame(this),
                                        ClientSettings.getInstance().getResources().getResource("create sale invoice?"),
                                        ClientSettings.getInstance().getResources().getResource("desk selling invoice"),
                                        JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {

            DetailSaleDocVO docVO = (DetailSaleDocVO)headerFormPanel.getVOModel().getValueObject();
            DetailSaleDocVO invoiceVO = (DetailSaleDocVO)docVO.clone();
            Calendar cal = Calendar.getInstance();
            invoiceVO.setCompanyCodeSys01Doc01DOC01(docVO.getCompanyCodeSys01DOC01());
            invoiceVO.setDocTypeDoc01DOC01(docVO.getDocTypeDOC01());
            invoiceVO.setDocYearDoc01DOC01(docVO.getDocYearDOC01());
            invoiceVO.setDocNumberDoc01DOC01(docVO.getDocNumberDOC01());
            invoiceVO.setDocSequenceDoc01DOC01(docVO.getDocSequenceDOC01());
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
                  ClientUtils.getParentFrame(this),
                  res.getErrorMessage(),
                  ClientSettings.getInstance().getResources().getResource("desk selling invoice"),
                  JOptionPane.ERROR_MESSAGE
              );
          }

        }
        else
          JOptionPane.showMessageDialog(
              ClientUtils.getParentFrame(this),
              res.getErrorMessage(),
              ClientSettings.getInstance().getResources().getResource("desk selling confirmation"),
              JOptionPane.ERROR_MESSAGE
          );
      }
    }
    catch (Exception ex) {
      JOptionPane.showMessageDialog(
          ClientUtils.getParentFrame(this),
          ex.getMessage(),
          ClientSettings.getInstance().getResources().getResource("desk selling confirmation"),
          JOptionPane.ERROR_MESSAGE
      );
    }
  }


  public DetailSaleDocVO getParentVO() {
    return (DetailSaleDocVO)headerFormPanel.getVOModel().getValueObject();
  }

  public SaleCustomerHeadPanel getSaleCustomerHeadPanel1() {
    return saleCustomerHeadPanel1;
  }
  public LookupController getWareController() {
    return wareController;
  }


  public final void showRowsPanel() {
    tabbedPane.setSelectedIndex(1);
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
  public CodLookupControl getControlWarehouseCode() {
    return controlWarehouseCode;
  }

}

class SaleDeskDocFrame_confirmButton_actionAdapter implements java.awt.event.ActionListener {
  SaleDeskDocFrame adaptee;

  SaleDeskDocFrame_confirmButton_actionAdapter(SaleDeskDocFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.confirmButton_actionPerformed(e);
  }
}

class SaleDeskDocFrame_printButton_actionAdapter implements java.awt.event.ActionListener {
  SaleDeskDocFrame adaptee;

  SaleDeskDocFrame_printButton_actionAdapter(SaleDeskDocFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.printButton_actionPerformed(e);
  }
}
