package org.jallinone.registers.bank.client;

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
import java.math.BigDecimal;
import javax.swing.border.*;
import org.openswing.swing.form.client.Form;
import org.jallinone.commons.client.CustomizedControls;
import org.openswing.swing.util.java.Consts;
import java.awt.event.*;
import javax.swing.text.MaskFormatter;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the banks grid frame.</p>
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
public class BanksFrame extends InternalFrame {

  JPanel buttonsPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  ReloadButton reloadButton = new ReloadButton();
  DeleteButton deleteButton = new DeleteButton();
  NavigatorBar navigatorBar = new NavigatorBar();
  GridControl grid = new GridControl();


  /** grid data locator */
  private ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();
  TextColumn colBank = new TextColumn();
  TextColumn colDescr = new TextColumn();
  ExportButton exportButton = new ExportButton();
  TextColumn colBban = new TextColumn();
  TextColumn colIban = new TextColumn();
  JPanel detailPanel = new JPanel();
  TitledBorder titledBorder1;
  TitledBorder titledBorder2;
  JPanel detailButtonsPanel = new JPanel();
  Form formPanel = new Form();
  BorderLayout borderLayout1 = new BorderLayout();
  InsertButton insertButton1 = new InsertButton();
  CopyButton copyButton1 = new CopyButton();
  EditButton editButton1 = new EditButton();
  SaveButton saveButton1 = new SaveButton();
  ReloadButton reloadButton1 = new ReloadButton();
  DeleteButton deleteButton1 = new DeleteButton();
  FlowLayout flowLayout2 = new FlowLayout();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  LabelControl labelBankCode = new LabelControl();
  TextControl controlBankCode = new TextControl();
  LabelControl labelDescr = new LabelControl();
  TextControl controlDescr = new TextControl();
  LabelControl labelBban = new LabelControl();
  FormattedTextControl controlBban = new FormattedTextControl();
  LabelControl labelIban = new LabelControl();
  FormattedTextControl controlIban = new FormattedTextControl();
  LabelControl labelAddress = new LabelControl();
  TextControl controlAddress = new TextControl();
  LabelControl labelCity = new LabelControl();
  TextControl controlCity = new TextControl();
  LabelControl labelZip = new LabelControl();
  TextControl controlZip = new TextControl();
  LabelControl labelProv = new LabelControl();
  TextControl controlProv = new TextControl();
  LabelControl labelCountry = new LabelControl();
  TextControl controlCountry = new TextControl();
  JTabbedPane tab = new JTabbedPane();

  private BankController bankController = new BankController(this);


  public BanksFrame(GridController controller) {
    grid.setController(controller);
    grid.setGridDataLocator(gridDataLocator);
    gridDataLocator.setServerMethodName("loadBanks");
    try {
      jbInit();
      setSize(750,550);
      setMinimumSize(new Dimension(620,550));

      formPanel.setFormController(bankController);

      CustomizedControls customizedControls = new CustomizedControls(tab,formPanel,new BigDecimal(232));


   }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  public final void reloadData() {
    grid.reloadData();
  }


  private void jbInit() throws Exception {
    formPanel.setVOClassName("org.jallinone.registers.bank.java.BankVO");
    titledBorder1 = new TitledBorder("");
    titledBorder2 = new TitledBorder("");
    grid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    grid.setValueObjectClassName("org.jallinone.registers.bank.java.BankVO");
    this.setTitle(ClientSettings.getInstance().getResources().getResource("banks"));
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    grid.setDeleteButton(deleteButton);
    grid.setExportButton(exportButton);
    grid.setFunctionId("REG12");
    grid.setMaxSortedColumns(3);
    grid.setNavBar(navigatorBar);
    grid.setReloadButton(reloadButton);
    colBank.setMaxCharacters(20);
    colBank.setTrimText(true);
    colBank.setUpperCase(true);
    colBank.setColumnFilterable(true);
    colBank.setColumnName("bankCodeREG12");
    colBank.setColumnSortable(true);
    colBank.setEditableOnInsert(true);
    colBank.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colBank.setSortingOrder(1);
    colDescr.setColumnFilterable(false);
    colDescr.setColumnName("descriptionREG12");
    colDescr.setColumnSortable(true);
    colDescr.setEditableOnEdit(true);
    colDescr.setEditableOnInsert(true);
    colDescr.setPreferredWidth(290);
    //colBban.setMaxCharacters(26);
    colBban.setPreferredWidth(150);
    colBban.setColumnFilterable(true);
    colBban.setColumnName("bbanREG12");
    colBban.setColumnSortable(true);
    colBban.setEditableOnEdit(false);
    colIban.setColumnName("ibanREG12");
    colIban.setPreferredWidth(250);
    detailPanel.setBorder(titledBorder1);
    detailPanel.setLayout(borderLayout1);
    titledBorder1.setTitleColor(Color.blue);
    titledBorder1.setTitle(ClientSettings.getInstance().getResources().getResource("bank"));
    formPanel.setInsertButton(insertButton1);
    formPanel.setCopyButton(copyButton1);
    formPanel.setEditButton(editButton1);
    formPanel.setReloadButton(reloadButton1);
    formPanel.setDeleteButton(deleteButton1);
    formPanel.setSaveButton(saveButton1);
    formPanel.setFunctionId("REG12");
    formPanel.setLayout(gridBagLayout1);
    detailButtonsPanel.setLayout(flowLayout2);
    flowLayout2.setAlignment(FlowLayout.LEFT);
    labelBankCode.setText("bankCodeREG12");
    labelDescr.setText("descriptionREG12");
    labelBban.setText("bbanREG12");
    labelIban.setText("ibanREG12");
    labelAddress.setText("address");
    labelCity.setText("city");
    labelZip.setText("zip");
    labelProv.setText("prov");
    labelCountry.setText("country");
    controlBankCode.setAttributeName("bankCodeREG12");
    controlBankCode.setLinkLabel(labelBankCode);
    controlBankCode.setMaxCharacters(20);
    controlBankCode.setRequired(true);
    controlBankCode.setTrimText(true);
    controlBankCode.setUpperCase(true);
    controlBankCode.setEnabledOnEdit(false);
    controlDescr.setAttributeName("descriptionREG12");
    controlDescr.setLinkLabel(labelDescr);
    controlDescr.setRequired(true);
    controlBban.setAttributeName("bbanREG12");
    controlBban.setCanCopy(false);
    controlBban.setLinkLabel(labelBban);
//    controlBban.setMaxCharacters(26);
    controlBban.setRequired(true);
//    controlBban.setTrimText(true);
//    controlBban.setUpperCase(true);

    MaskFormatter mask1 = new MaskFormatter("AAAA AAAA AAA* **** **** **** **** **");
//    mask1.setPlaceholderCharacter(' ');
 
    MaskFormatter mask2 = new MaskFormatter("AAAA AAAA AAAA AAA* **** **** **** **** **");
//    mask2.setPlaceholderCharacter(' ');
    
//    MaskFormatter mask1 = new MaskFormatter("A AAAAA AAAAA AAAAAAAAAAAA");
    controlBban.setFormatter(mask1);
    controlBban.addFocusListener(new BanksFrame_controlBban_focusAdapter(this));

//    MaskFormatter mask2 = new MaskFormatter("AAAA AAAA AAAA AAAA AAAA AAA");
    controlIban.setFormatter(mask2);
    controlIban.addFocusListener(new BanksFrame_controlIban_focusAdapter(this));


    controlIban.setAttributeName("ibanREG12");
    controlIban.setLinkLabel(labelIban);
//    controlIban.setMaxCharacters(33);
    controlIban.setRequired(true);
//    controlIban.setTrimText(true);
//    controlIban.setUpperCase(true);
    controlAddress.setAttributeName("addressREG12");
    controlAddress.setCanCopy(true);
    controlAddress.setLinkLabel(labelAddress);
    controlCity.setAttributeName("cityREG12");
    controlCity.setCanCopy(true);
    controlCity.setLinkLabel(labelCity);
    controlZip.setAttributeName("zipREG12");
    controlZip.setCanCopy(true);
    controlZip.setLinkLabel(labelZip);
    controlZip.setMaxCharacters(20);
    controlZip.setEnabledOnEdit(true);
    controlProv.setAttributeName("provinceREG12");
    controlProv.setCanCopy(true);
    controlProv.setLinkLabel(labelProv);
    controlProv.setMaxCharacters(20);
    controlProv.setTrimText(true);
    controlProv.setUpperCase(true);
    controlCountry.setAttributeName("countryREG12");
    controlCountry.setCanCopy(true);
    controlCountry.setLinkLabel(labelCountry);
    controlCountry.setMaxCharacters(20);
    controlCountry.setTrimText(true);
    controlCountry.setUpperCase(true);
    copyButton1.setEnabled(false);
    saveButton1.setEnabled(false);
    editButton1.setEnabled(false);
    deleteButton1.setEnabled(false);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(deleteButton, null);
    buttonsPanel.add(exportButton, null);
    buttonsPanel.add(navigatorBar, null);
    this.getContentPane().add(grid, BorderLayout.CENTER);
    grid.getColumnContainer().add(colBank, null);
    grid.getColumnContainer().add(colDescr, null);
    grid.getColumnContainer().add(colBban, null);
    grid.getColumnContainer().add(colIban, null);
    this.getContentPane().add(detailPanel,  BorderLayout.SOUTH);
    detailPanel.add(detailButtonsPanel,  BorderLayout.NORTH);
    detailPanel.add(tab, BorderLayout.CENTER);
    tab.add(formPanel,"bankDetail");
    tab.setTitleAt(0,ClientSettings.getInstance().getResources().getResource("bank detail"));
    formPanel.add(labelBankCode,         new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(controlBankCode,            new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(controlDescr,           new GridBagConstraints(3, 0, 4, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(labelBban,        new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(controlBban,             new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(labelAddress,        new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(controlAddress,       new GridBagConstraints(1, 2, 5, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(labelCity,      new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(controlCity,      new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(labelZip,      new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    formPanel.add(controlProv,      new GridBagConstraints(6, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(labelCountry,     new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(controlCountry,     new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(controlIban,      new GridBagConstraints(5, 1, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(labelProv,   new GridBagConstraints(5, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(controlZip,   new GridBagConstraints(3, 3, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(labelIban,  new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    formPanel.add(labelDescr, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    detailButtonsPanel.add(insertButton1, null);
    detailButtonsPanel.add(copyButton1, null);
    detailButtonsPanel.add(editButton1, null);
    detailButtonsPanel.add(saveButton1, null);
    detailButtonsPanel.add(reloadButton1, null);
    detailButtonsPanel.add(deleteButton1, null);
  }


  public BankController getBankController() {
    return bankController;
  }


  public Form getFormPanel() {
    return formPanel;
  }


  public GridControl getGrid() {
    return grid;
  }

  void controlBban_focusLost(FocusEvent e) {
    controlBban.setText(controlBban.getText().toUpperCase());
  }

  void controlIban_focusLost(FocusEvent e) {
    controlIban.setText(controlIban.getText().toUpperCase());
  }




}

class BanksFrame_controlBban_focusAdapter extends java.awt.event.FocusAdapter {
  BanksFrame adaptee;

  BanksFrame_controlBban_focusAdapter(BanksFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void focusLost(FocusEvent e) {
    adaptee.controlBban_focusLost(e);
  }
}

class BanksFrame_controlIban_focusAdapter extends java.awt.event.FocusAdapter {
  BanksFrame adaptee;

  BanksFrame_controlIban_focusAdapter(BanksFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void focusLost(FocusEvent e) {
    adaptee.controlIban_focusLost(e);
  }
}


