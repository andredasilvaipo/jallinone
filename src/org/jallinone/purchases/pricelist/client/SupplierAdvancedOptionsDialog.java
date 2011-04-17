package org.jallinone.purchases.pricelist.client;

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
import org.jallinone.commons.client.CustomizedColumns;
import org.jallinone.hierarchies.client.*;
import org.jallinone.commons.client.CompaniesComboColumn;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.message.receive.java.Response;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.domains.java.*;
import org.jallinone.items.java.ItemTypeVO;
import org.jallinone.commons.java.ApplicationConsts;
import java.awt.event.*;
import org.jallinone.purchases.pricelist.java.SupplierPricelistChanges;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Dialog used to define start/end validation dates, prices delta to apply to all supplier items.</p>
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
public class SupplierAdvancedOptionsDialog extends JDialog {
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel mainPanel = new JPanel();
  JPanel buttonsPanel = new JPanel();
  JButton okButton = new JButton();
  JButton cancelButton = new JButton();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  LabelControl labelStartDate = new LabelControl();
  LabelControl labelEndDate = new LabelControl();
  DateControl controlStartDate = new DateControl();
  DateControl controlEndDate = new DateControl();
  NumericControl controlPrice = new NumericControl();

  JRadioButton radioPerc = new JRadioButton();
  JRadioButton radioValue = new JRadioButton();
  ButtonGroup buttonGroup1 = new ButtonGroup();
  NumericControl controlPerc = new NumericControl();
  JCheckBox controlRound = new JCheckBox();
  JPanel datePanel = new JPanel();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  TitledBorder titledBorder1;
  TitledBorder titledBorder2;
  JPanel prciesPanel = new JPanel();
  GridBagLayout gridBagLayout3 = new GridBagLayout();
  JCheckBox controlEnabledDate = new JCheckBox();
  JCheckBox controlEnablePrice = new JCheckBox();

  SupplierPricelistChanges pricelistChanges = null;
  SupplierPricelistPanel panel = null;


  public SupplierAdvancedOptionsDialog(SupplierPricelistPanel panel,SupplierPricelistChanges pricelistChanges) {
    super(MDIFrame.getInstance(),ClientSettings.getInstance().getResources().getResource("changes to pricelist"),true);
    this.panel = panel;
    this.pricelistChanges = pricelistChanges;
    try {
      jbInit();
      setSize(450,350);
      ClientUtils.centerDialog(MDIFrame.getInstance(),this);

      setEnabledDateInterval(false);
      setEnabledPrices(false);

      setVisible(true);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    titledBorder1 = new TitledBorder("");
    titledBorder2 = new TitledBorder("");
    this.getContentPane().setLayout(borderLayout1);
    buttonsPanel.setBorder(BorderFactory.createEtchedBorder());
    okButton.setText(ClientSettings.getInstance().getResources().getResource("ok"));
    okButton.addActionListener(new SupplierAdvancedOptionsDialog_okButton_actionAdapter(this));
    cancelButton.setText(ClientSettings.getInstance().getResources().getResource("cancel"));
    cancelButton.addActionListener(new SupplierAdvancedOptionsDialog_cancelButton_actionAdapter(this));

    cancelButton.setMnemonic(ClientSettings.getInstance().getResources().getResource("cancelmnemonic").charAt(0));
    okButton.setMnemonic(ClientSettings.getInstance().getResources().getResource("okmnemonic").charAt(0));
    mainPanel.setLayout(gridBagLayout1);
    labelStartDate.setText("new start validation date");
    labelEndDate.setText("new end validation date");
    controlPrice.setDecimals(5);
    controlPrice.setGrouping(true);
    this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    radioPerc.setText(ClientSettings.getInstance().getResources().getResource("add to all prices a %"));
    radioPerc.addItemListener(new SupplierAdvancedOptionsDialog_radioPerc_itemAdapter(this));
    radioValue.setSelected(true);
    radioValue.setText(ClientSettings.getInstance().getResources().getResource("add this delta to all prices"));
    radioValue.addItemListener(new SupplierAdvancedOptionsDialog_radioValue_itemAdapter(this));
    controlPerc.setDecimals(5);
    controlRound.setEnabled(false);
    controlRound.setText(ClientSettings.getInstance().getResources().getResource("truncate decimals"));
    datePanel.setLayout(gridBagLayout2);
    datePanel.setBorder(titledBorder1);
    titledBorder1.setTitle(ClientSettings.getInstance().getResources().getResource("date interval"));
    titledBorder1.setTitleColor(Color.blue);
    titledBorder2.setTitle(ClientSettings.getInstance().getResources().getResource("item prices"));
    titledBorder2.setTitleColor(Color.blue);
    prciesPanel.setBorder(titledBorder2);
    prciesPanel.setLayout(gridBagLayout3);
    controlEnabledDate.setText(ClientSettings.getInstance().getResources().getResource("change date interval"));
    controlEnabledDate.addItemListener(new SupplierAdvancedOptionsDialog_controlEnabledDate_itemAdapter(this));
    controlEnablePrice.setText(ClientSettings.getInstance().getResources().getResource("change prices"));
    controlEnablePrice.addItemListener(new SupplierAdvancedOptionsDialog_controlEnablePrice_itemAdapter(this));
    this.getContentPane().add(mainPanel,  BorderLayout.CENTER);
    this.getContentPane().add(buttonsPanel,  BorderLayout.SOUTH);
    buttonsPanel.add(okButton, null);
    buttonsPanel.add(cancelButton, null);
    buttonGroup1.add(radioValue);
    buttonGroup1.add(radioPerc);
    mainPanel.add(datePanel,           new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    datePanel.add(labelStartDate,      new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    datePanel.add(labelEndDate,     new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    datePanel.add(controlStartDate,      new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 50, 0));
    datePanel.add(controlEndDate,         new GridBagConstraints(1, 2, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 50, 0));
    mainPanel.add(prciesPanel,        new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    prciesPanel.add(controlPrice,          new GridBagConstraints(1, 1, 2, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    prciesPanel.add(controlEnablePrice,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    prciesPanel.add(radioValue,    new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    prciesPanel.add(radioPerc,    new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    prciesPanel.add(controlPerc,   new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    prciesPanel.add(controlRound,   new GridBagConstraints(2, 2, 1, 1, 0.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 15, 5, 5), 0, 0));
    datePanel.add(controlEnabledDate,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
  }


  void cancelButton_actionPerformed(ActionEvent e) {
    setVisible(false);
  }


  private void setEnabledDateInterval(boolean enabled) {
    controlStartDate.setEnabled(enabled);
    controlEndDate.setEnabled(enabled);
  }


  private void setEnabledPrices(boolean enabled) {
    radioValue.setEnabled(enabled);
    radioPerc.setEnabled(enabled);
    controlPerc.setEnabled(false);
    controlPrice.setEnabled(false);
    controlRound.setEnabled(false);
    if (enabled) {
      radioValue.setSelected(true);
      controlPrice.setEnabled(true);
    }
  }


  void okButton_actionPerformed(ActionEvent e) {
    if (controlEnabledDate.isSelected() &&
        (controlStartDate.getDate()==null || controlEndDate.getDate()==null)) {
      JOptionPane.showMessageDialog(
          MDIFrame.getInstance(),
          ClientSettings.getInstance().getResources().getResource("validation dates are required."),
          ClientSettings.getInstance().getResources().getResource("Attention"),
          JOptionPane.WARNING_MESSAGE
      );
      return;
    }
    if (controlEnablePrice.isSelected() && radioValue.isSelected() && controlPrice.getValue()==null) {
      JOptionPane.showMessageDialog(
          MDIFrame.getInstance(),
          ClientSettings.getInstance().getResources().getResource("price value is required."),
          ClientSettings.getInstance().getResources().getResource("Attention"),
          JOptionPane.WARNING_MESSAGE
      );
      return;
    }
    if (controlEnablePrice.isSelected() && radioPerc.isSelected() && controlPerc.getValue()==null) {
      JOptionPane.showMessageDialog(
          MDIFrame.getInstance(),
          ClientSettings.getInstance().getResources().getResource("price % is required."),
          ClientSettings.getInstance().getResources().getResource("Attention"),
          JOptionPane.WARNING_MESSAGE
      );
      return;
    }

    if (controlEnabledDate.isSelected()) {
      pricelistChanges.setStartDate(new java.sql.Date(controlStartDate.getDate().getTime()));
      pricelistChanges.setEndDate(new java.sql.Date(controlEndDate.getDate().getTime()));
    }

    if (controlEnablePrice.isSelected() && radioValue.isSelected()) {
      pricelistChanges.setDeltaValue(controlPrice.getBigDecimal());
    }

    if (controlEnablePrice.isSelected() && radioPerc.isSelected()) {
      pricelistChanges.setPercentage(controlPerc.getBigDecimal());

      if (controlRound.isSelected())
        pricelistChanges.setTruncateDecimals(true);
    }


    // change prices...
    Response res = ClientUtils.getData("changeSupplierPricelist",pricelistChanges);
    if (res.isError()) {
      JOptionPane.showMessageDialog(
          MDIFrame.getInstance(),
          ClientSettings.getInstance().getResources().getResource("Error while saving")+"\n"+res.getErrorMessage(),
          ClientSettings.getInstance().getResources().getResource("Saving error"),
          JOptionPane.WARNING_MESSAGE
      );
      return;
    }

    panel.getPricesGrid().reloadData();

    setVisible(false);
  }


  void controlEnabledDate_itemStateChanged(ItemEvent e) {
    setEnabledDateInterval(controlEnabledDate.isSelected());
  }


  void controlEnablePrice_itemStateChanged(ItemEvent e) {
    setEnabledPrices(controlEnablePrice.isSelected());
  }


  void radioValue_itemStateChanged(ItemEvent e) {
    if (radioValue.isSelected()) {
      controlPerc.setEnabled(false);
      controlRound.setEnabled(false);
      controlPrice.setEnabled(true);
    }
    else {
      controlPerc.setEnabled(true);
      controlRound.setEnabled(true);
      controlPrice.setEnabled(false);
    }
  }

  void radioPerc_itemStateChanged(ItemEvent e) {
    if (radioValue.isSelected()) {
      controlPerc.setEnabled(false);
      controlRound.setEnabled(false);
      controlPrice.setEnabled(true);
    }
    else {
      controlPerc.setEnabled(true);
      controlRound.setEnabled(true);
      controlPrice.setEnabled(false);
    }
  }



}

class SupplierAdvancedOptionsDialog_cancelButton_actionAdapter implements java.awt.event.ActionListener {
  SupplierAdvancedOptionsDialog adaptee;

  SupplierAdvancedOptionsDialog_cancelButton_actionAdapter(SupplierAdvancedOptionsDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.cancelButton_actionPerformed(e);
  }
}

class SupplierAdvancedOptionsDialog_okButton_actionAdapter implements java.awt.event.ActionListener {
  SupplierAdvancedOptionsDialog adaptee;

  SupplierAdvancedOptionsDialog_okButton_actionAdapter(SupplierAdvancedOptionsDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.okButton_actionPerformed(e);
  }
}

class SupplierAdvancedOptionsDialog_controlEnabledDate_itemAdapter implements java.awt.event.ItemListener {
  SupplierAdvancedOptionsDialog adaptee;

  SupplierAdvancedOptionsDialog_controlEnabledDate_itemAdapter(SupplierAdvancedOptionsDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void itemStateChanged(ItemEvent e) {
    adaptee.controlEnabledDate_itemStateChanged(e);
  }
}

class SupplierAdvancedOptionsDialog_controlEnablePrice_itemAdapter implements java.awt.event.ItemListener {
  SupplierAdvancedOptionsDialog adaptee;

  SupplierAdvancedOptionsDialog_controlEnablePrice_itemAdapter(SupplierAdvancedOptionsDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void itemStateChanged(ItemEvent e) {
    adaptee.controlEnablePrice_itemStateChanged(e);
  }
}

class SupplierAdvancedOptionsDialog_radioValue_itemAdapter implements java.awt.event.ItemListener {
  SupplierAdvancedOptionsDialog adaptee;

  SupplierAdvancedOptionsDialog_radioValue_itemAdapter(SupplierAdvancedOptionsDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void itemStateChanged(ItemEvent e) {
    adaptee.radioValue_itemStateChanged(e);
  }
}

class SupplierAdvancedOptionsDialog_radioPerc_itemAdapter implements java.awt.event.ItemListener {
  SupplierAdvancedOptionsDialog adaptee;

  SupplierAdvancedOptionsDialog_radioPerc_itemAdapter(SupplierAdvancedOptionsDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void itemStateChanged(ItemEvent e) {
    adaptee.radioPerc_itemStateChanged(e);
  }
}
