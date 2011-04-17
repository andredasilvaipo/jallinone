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

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Dialog used to define start/end validation date and a unique price for dragged supplier items.</p>
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
public class SupplierImportItemsDialog extends JDialog {
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel mainPanel = new JPanel();
  JPanel buttonsPanel = new JPanel();
  JButton okButton = new JButton();
  JButton cancelButton = new JButton();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  LabelControl labelStartDate = new LabelControl();
  LabelControl labelEndDate = new LabelControl();
  LabelControl labelPrice = new LabelControl();
  DateControl controlStartDate = new DateControl();
  DateControl controlEndDate = new DateControl();
  NumericControl controlPrice = new NumericControl();

  private SupplierImportItems importItems = null;
  private ArrayList items = null;


  public SupplierImportItemsDialog(ArrayList items,SupplierImportItems importItems) {
    super(MDIFrame.getInstance(),ClientSettings.getInstance().getResources().getResource("import supplier items"),true);
    this.items = items;
    this.importItems = importItems;
    try {
      jbInit();
      setSize(300,180);
      ClientUtils.centerDialog(MDIFrame.getInstance(),this);
      setVisible(true);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    this.getContentPane().setLayout(borderLayout1);
    buttonsPanel.setBorder(BorderFactory.createEtchedBorder());
    okButton.setText(ClientSettings.getInstance().getResources().getResource("ok"));
    okButton.addActionListener(new SupplierImportItemsDialog_okButton_actionAdapter(this));
    cancelButton.setText(ClientSettings.getInstance().getResources().getResource("cancel"));
    cancelButton.addActionListener(new SupplierImportItemsDialog_cancelButton_actionAdapter(this));

    cancelButton.setMnemonic(ClientSettings.getInstance().getResources().getResource("cancelmnemonic").charAt(0));
    okButton.setMnemonic(ClientSettings.getInstance().getResources().getResource("okmnemonic").charAt(0));
    mainPanel.setLayout(gridBagLayout1);
    labelStartDate.setText("startDatePUR04");
    labelEndDate.setText("endDatePUR04");
    labelPrice.setRequestFocusEnabled(true);
    labelPrice.setText("valuePUR04");
    controlPrice.setDecimals(5);
    this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    this.getContentPane().add(mainPanel,  BorderLayout.CENTER);
    this.getContentPane().add(buttonsPanel,  BorderLayout.SOUTH);
    buttonsPanel.add(okButton, null);
    buttonsPanel.add(cancelButton, null);
    mainPanel.add(labelStartDate,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(labelEndDate,  new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(labelPrice,   new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(controlStartDate,  new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(controlEndDate,    new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(controlPrice,    new GridBagConstraints(1, 2, 1, 1, 0.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
  }


  void cancelButton_actionPerformed(ActionEvent e) {
    setVisible(false);
  }


  void okButton_actionPerformed(ActionEvent e) {
    if (controlStartDate.getDate()==null ||
        controlEndDate.getDate()==null ||
        controlPrice.getValue()==null) {
      JOptionPane.showMessageDialog(
          MDIFrame.getInstance(),
          ClientSettings.getInstance().getResources().getResource("validation dates and price are required."),
          ClientSettings.getInstance().getResources().getResource("Attention"),
          JOptionPane.WARNING_MESSAGE
      );
      return;
    }
    importItems.saveItems(items,controlStartDate.getDate(),controlEndDate.getDate(),controlPrice.getBigDecimal());
    setVisible(false);
  }

}

class SupplierImportItemsDialog_cancelButton_actionAdapter implements java.awt.event.ActionListener {
  SupplierImportItemsDialog adaptee;

  SupplierImportItemsDialog_cancelButton_actionAdapter(SupplierImportItemsDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.cancelButton_actionPerformed(e);
  }
}

class SupplierImportItemsDialog_okButton_actionAdapter implements java.awt.event.ActionListener {
  SupplierImportItemsDialog adaptee;

  SupplierImportItemsDialog_okButton_actionAdapter(SupplierImportItemsDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.okButton_actionPerformed(e);
  }
}