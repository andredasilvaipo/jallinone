package org.jallinone.warehouse.documents.client;

import org.openswing.swing.table.client.Grid;
import org.openswing.swing.message.receive.java.*;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.mdi.client.MDIFrame;
import org.jallinone.commons.client.CompanyFormController;
import org.openswing.swing.util.java.Consts;
import org.jallinone.warehouse.documents.java.*;
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


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Detail Frame used for an in delivery note.</p>
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
public class InDeliveryNoteFrame extends InternalFrame implements GenericButtonController {

  /** detail form controller */
  private InDeliveryNoteController controller = null;
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
  InDeliveryNoteIdPanel delNoteIdHeadPanel = new InDeliveryNoteIdPanel(headerFormPanel);
  DeliveryNoteFooterPanel deliveryNoteFooterPanel = new DeliveryNoteFooterPanel(headerFormPanel);
  InDeliveryNoteRowsGridPanel rowsPanel = new InDeliveryNoteRowsGridPanel(this,headerFormPanel);
  GenericButton closeButton = new GenericButton(new ImageIcon(ClientUtils.getImage("workflow.gif")));
  DeliveryNoteWarehousePanel warePanel = new DeliveryNoteWarehousePanel(headerFormPanel);
  GenericButton printButton = new GenericButton(new ImageIcon(ClientUtils.getImage("printer.gif")));


  public InDeliveryNoteFrame(InDeliveryNoteController controller) {
    this.controller = controller;
    try {
      jbInit();
      setSize(750,550);
      setMinimumSize(new Dimension(750,550));
      setTitle(ClientSettings.getInstance().getResources().getResource("in delivery note"));

      init();

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * Method called by GenericButton.setEnabled method to check if the button must be disabled.
   * @param button button whose abilitation must be checked
   * @return <code>true</code> if no policy is defined in the form/grid for the specified button, <code>false</code> if there exists a disabilitation policy for the specified button (through addButtonsNotEnabledOnState form/grid method)
   */
  public boolean isButtonDisabled(GenericButton button) {
    DetailDeliveryNoteVO vo = null;
    if (headerFormPanel!=null && headerFormPanel.getVOModel()!=null)
      vo = (DetailDeliveryNoteVO)headerFormPanel.getVOModel().getValueObject();
    if (vo!=null && vo.getDocStateDOC08()!=null && vo.getDocStateDOC08().equals(ApplicationConsts.CLOSED))
      return true;
    else
      return false;
  }


  /**
   * Define input controls editable settings according to the document state.
   */
  private void init() {
    // disable warehouse lookup when the at least one line is added...
    HashSet attributeNameToDisable = new HashSet();
    attributeNameToDisable.add("warehouseCodeWar01DOC08");
    attributeNameToDisable.add("supplierCustomerCodeDOC08");
    headerFormPanel.addInputControlAttributesNotEditableOnState(attributeNameToDisable,"docStateDOC08",ApplicationConsts.HEADER_BLOCKED);


    // disable all editable fields when the delivery note is closed...
    attributeNameToDisable = new HashSet();
    attributeNameToDisable.add("warehouseCodeWar01DOC08");
    attributeNameToDisable.add("supplierCustomerCodeDOC08");
    attributeNameToDisable.add("carrierCodeReg09DOC08");
    attributeNameToDisable.add("docDateDOC08");
    attributeNameToDisable.add("deliveryDateDOC08");
    attributeNameToDisable.add("transportMotiveCodeReg20DOC08");
    attributeNameToDisable.add("noteDOC08");
    attributeNameToDisable.add("docRefDOC08");
    attributeNameToDisable.add("addressDOC08");
    attributeNameToDisable.add("cityDOC08");
    attributeNameToDisable.add("provinceDOC08");
    attributeNameToDisable.add("countryDOC08");
    attributeNameToDisable.add("zipDOC08");
    headerFormPanel.addInputControlAttributesNotEditableOnState(attributeNameToDisable,"docStateDOC08",ApplicationConsts.CLOSED);


    HashSet buttonsToDisable = new HashSet();
    buttonsToDisable.add(editButton1);
    buttonsToDisable.add(deleteButton1);
    headerFormPanel.addButtonsNotEnabled(buttonsToDisable,this);

  }


  private void jbInit() throws Exception {
    printButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("print document"));
    printButton.setEnabled(false);
    printButton.addActionListener(new InDeliveryNoteFrame_printButton_actionAdapter(this));

    headerFormPanel.setFunctionId("DOC08_IN");
    headerFormPanel.setFormController(controller);
    headerFormPanel.setVOClassName("org.jallinone.warehouse.documents.java.DetailDeliveryNoteVO");

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
    closeButton.addActionListener(new InDeliveryNoteFrame_closeButton_actionAdapter(this));
    this.getContentPane().add(mainPanel, BorderLayout.CENTER);
    mainPanel.add(tabbedPane,  BorderLayout.CENTER);
    tabbedPane.add(headerPanel,   "header");
    headerPanel.add(headerButtonsPanel,  BorderLayout.NORTH);
    headerButtonsPanel.add(insertButton1, null);
    headerButtonsPanel.add(editButton1, null);
    headerButtonsPanel.add(saveButton1, null);
    headerButtonsPanel.add(reloadButton1, null);
    headerButtonsPanel.add(deleteButton1, null);
    headerButtonsPanel.add(closeButton, null);
    headerButtonsPanel.add(printButton, null);

    tabbedPane.add(linesPanel,   "lines");
    linesPanel.add(rowsPanel, BorderLayout.CENTER);
    headerPanel.add(headerFormPanel,  BorderLayout.CENTER);
    headerFormPanel.add(delNoteIdHeadPanel,   new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    headerFormPanel.add(warePanel,   new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    headerFormPanel.add(deliveryNoteFooterPanel,    new GridBagConstraints(0, 4, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

    tabbedPane.setTitleAt(0,ClientSettings.getInstance().getResources().getResource("header"));
    tabbedPane.setTitleAt(1,ClientSettings.getInstance().getResources().getResource("lines"));

    closeButton.setToolTipText(ClientSettings.getInstance().getResources().getResource("delivery note closure"));

    closeButton.setEnabled(false);
  }


  public Form getHeaderFormPanel() {
    return headerFormPanel;
  }


  public void setButtonsEnabled(boolean enabled) {
    rowsPanel.setButtonsEnabled(enabled);
  }


  public void loadDataCompleted(boolean error,DeliveryNotePK pk) {
    DetailDeliveryNoteVO vo = (DetailDeliveryNoteVO)headerFormPanel.getVOModel().getValueObject();

    this.setTitle(ClientSettings.getInstance().getResources().getResource("in delivery note")+" - "+(vo.getDocSequenceDOC08()!=null?" - "+vo.getDocYearDOC08()+"/"+vo.getDocSequenceDOC08():"")+" - "+vo.getName_1REG04()+" "+(vo.getName_2REG04()==null?"":vo.getName_2REG04()));

    rowsPanel.setParentVO(vo);
    rowsPanel.getGrid().getOtherGridParams().put(ApplicationConsts.DELIVERY_NOTE_PK,pk);
    rowsPanel.getGrid().reloadData();

    if (vo.getDocStateDOC08().equals(ApplicationConsts.CLOSED)) {
      closeButton.setEnabled(false);
      setButtonsEnabled(false);
      rowsPanel.setButtonsEnabled(false);
    }
    if (!vo.getDocStateDOC08().equals(ApplicationConsts.HEADER_BLOCKED)) {
      closeButton.setEnabled(false);
    }
    else if (vo.getDocStateDOC08().equals(ApplicationConsts.HEADER_BLOCKED)) {
      closeButton.setEnabled(true);
    }

    if (vo.getDocStateDOC08().equals(ApplicationConsts.CONFIRMED) ||
        vo.getDocStateDOC08().equals(ApplicationConsts.CLOSED) ||
        vo.getDocStateDOC08().equals(ApplicationConsts.HEADER_BLOCKED)) {
      printButton.setEnabled(true);
    }
    else {
      printButton.setEnabled(false);
    }

    if (vo.getDocStateDOC08().equals(ApplicationConsts.CONFIRMED) ||
        vo.getDocStateDOC08().equals(ApplicationConsts.CLOSED) ||
        vo.getDocStateDOC08().equals(ApplicationConsts.HEADER_BLOCKED)) {
      printButton.setEnabled(true);
    }
    else {
      printButton.setEnabled(false);
    }

  }


  public InDeliveryNoteRowsGridPanel getRowsPanel() {
    return rowsPanel;
  }


  public GridControl getOrders() {
    return controller.getParentFrame().getGrid();
  }


  void closeButton_actionPerformed(ActionEvent e) {
    // view closure confirmation dialog...
    if (JOptionPane.showConfirmDialog(ClientUtils.getParentFrame(this),
                                  ClientSettings.getInstance().getResources().getResource("close delivery note?"),
                                  ClientSettings.getInstance().getResources().getResource("delivery note closure"),
                                  JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {

      Response res = ClientUtils.getData("closeDeliveryNote",headerFormPanel.getVOModel().getValueObject());
      if (!res.isError()) {
        headerFormPanel.setMode(Consts.READONLY);
        headerFormPanel.executeReload();
        getOrders().reloadCurrentBlockOfData();
      }
      else
        JOptionPane.showMessageDialog(
            ClientUtils.getParentFrame(this),
            res.getErrorMessage(),
            ClientSettings.getInstance().getResources().getResource("Error"),
            JOptionPane.ERROR_MESSAGE
        );


    }

  }


  public void enabledConfirmButton() {
    closeButton.setEnabled(true);
  }

  void printButton_actionPerformed(ActionEvent e) {
    DetailDeliveryNoteVO vo = (DetailDeliveryNoteVO)headerFormPanel.getVOModel().getValueObject();

    HashMap params = new HashMap();
    params.put("COMPANY_CODE",vo.getCompanyCodeSys01DOC08());
    params.put("DOC_TYPE",vo.getDocTypeDOC08());
    params.put("DOC_YEAR",vo.getDocYearDOC08());
    params.put("DOC_NUMBER",vo.getDocNumberDOC08());

    HashMap map = new HashMap();
    map.put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01DOC08());
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

class InDeliveryNoteFrame_closeButton_actionAdapter implements java.awt.event.ActionListener {
  InDeliveryNoteFrame adaptee;

  InDeliveryNoteFrame_closeButton_actionAdapter(InDeliveryNoteFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.closeButton_actionPerformed(e);
  }
}

class InDeliveryNoteFrame_printButton_actionAdapter implements java.awt.event.ActionListener {
  InDeliveryNoteFrame adaptee;

  InDeliveryNoteFrame_printButton_actionAdapter(InDeliveryNoteFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.printButton_actionPerformed(e);
  }
}
