package org.jallinone.sales.documents.client;

import javax.swing.JPanel;
import java.awt.*;
import javax.swing.border.*;
import org.openswing.swing.client.*;
import org.openswing.swing.util.client.ClientSettings;
import java.awt.event.*;
import org.openswing.swing.form.client.Form;
import org.jallinone.sales.documents.java.DetailSaleDocVO;
import java.math.BigDecimal;
import javax.swing.JOptionPane;
import org.openswing.swing.util.client.ClientUtils;

/**
  * <p>Title: JAllInOne ERP/CRM application</p>
  * <p>Description: Panel used in the sale header to show sale doc. totals.</p>
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
public class SaleTotalsPanel extends JPanel {

  GridBagLayout gridBagLayout1 = new GridBagLayout();
  TitledBorder titledBorder1;
  LabelControl labelTaxableIncome = new LabelControl();
  CurrencyControl controlTaxableIncome = new CurrencyControl();
  LabelControl labelTotalVat = new LabelControl();
  CurrencyControl controlTotalVat = new CurrencyControl();
  LabelControl labelTotal = new LabelControl();
  CurrencyControl controlTotal = new CurrencyControl();
  LabelControl labelAllowance = new LabelControl();
  CurrencyControl controlAllowance = new CurrencyControl();
  CurrencyControl controlDeposit = new CurrencyControl();
  LabelControl labelDeposit = new LabelControl();

  private Form form = null;


  public SaleTotalsPanel(Form form) {
    this.form = form;
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    titledBorder1 = new TitledBorder("");
    labelTaxableIncome.setText("taxableIncome");
    titledBorder1.setTitle(ClientSettings.getInstance().getResources().getResource("totals"));
    titledBorder1.setTitleColor(Color.blue);
    this.setBorder(titledBorder1);
    this.setLayout(gridBagLayout1);
    labelTotalVat.setText("totalVat");
    labelTotal.setText("total");
    controlTotal.setDecimals(5);
    controlTotal.setLinkLabel(labelTotal);
    controlTotal.setEnabledOnInsert(false);
    controlTotal.setEnabledOnEdit(false);
    controlTotal.setAttributeName("totalDOC01");
    controlTotalVat.setDecimals(5);
    controlTotalVat.setLinkLabel(labelTotalVat);
    controlTotalVat.setEnabledOnInsert(false);
    controlTotalVat.setEnabledOnEdit(false);
    controlTotalVat.setAttributeName("totalVatDOC01");
    controlTaxableIncome.setDecimals(5);
    controlTaxableIncome.setLinkLabel(labelTaxableIncome);
    controlTaxableIncome.setEnabledOnInsert(false);
    controlTaxableIncome.setEnabledOnEdit(false);
    controlTaxableIncome.setAttributeName("taxableIncomeDOC01");
    controlAllowance.setAttributeName("allowanceDOC01");
    controlAllowance.addFocusListener(new SaleTotalsPanel_controlAllowance_focusAdapter(this));
    controlDeposit.setAttributeName("depositDOC01");
    controlDeposit.addFocusListener(new SaleTotalsPanel_controlDeposit_focusAdapter(this));
    labelAllowance.setText("allowanceDOC01");
    labelDeposit.setText("depositDOC01");
    this.add(labelTotalVat,     new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlTotalVat,    new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelTotal,     new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlTotal,     new GridBagConstraints(5, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelTaxableIncome,   new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlTaxableIncome,   new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelAllowance,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlAllowance,   new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlDeposit,   new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelDeposit,   new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
  }


  public CurrencyControl getControlTaxableIncome() {
    return controlTaxableIncome;
  }
  public CurrencyControl getControlTotal() {
    return controlTotal;
  }
  public CurrencyControl getControlTotalVat() {
    return controlTotalVat;
  }
  public CurrencyControl getControlAllowance() {
    return controlAllowance;
  }
  public CurrencyControl getControlDeposit() {
    return controlDeposit;
  }


  void controlAllowance_focusLost(FocusEvent e) {
    calculateTotals();
  }


  void controlDeposit_focusLost(FocusEvent e) {
    calculateTotals();
  }


  /**
   * Recalculate total on changing allowance/deposit values.
   */
  private void calculateTotals() {
    DetailSaleDocVO vo = (DetailSaleDocVO)form.getVOModel().getValueObject();
    if (vo.getAllowanceDOC01()==null)
      vo.setAllowanceDOC01(new BigDecimal(0));
    if (vo.getDepositDOC01()==null)
      vo.setDepositDOC01(new BigDecimal(0));
    if (vo.getTotalDOC01()==null)
      vo.setTotalDOC01(new BigDecimal(0));
    if (vo.getTotalVatDOC01()==null)
      vo.setTotalVatDOC01(new BigDecimal(0));
    if (vo.getTaxableIncomeDOC01()==null)
      vo.setTaxableIncomeDOC01(new BigDecimal(0));

    vo.setTotalDOC01(
        vo.getTaxableIncomeDOC01().
        add(vo.getTotalVatDOC01().
        subtract(vo.getAllowanceDOC01()).
        subtract(vo.getDepositDOC01()))
    );

    if (vo.getTotalDOC01().doubleValue()<0) {
      JOptionPane.showMessageDialog(
          ClientUtils.getParentFrame(this),
          ClientSettings.getInstance().getResources().getResource("document total cannot be less than zero!"),
          ClientSettings.getInstance().getResources().getResource("Attention"),
          JOptionPane.WARNING_MESSAGE
      );
      vo.setAllowanceDOC01(new BigDecimal(0));
      vo.setDepositDOC01(new BigDecimal(0));
      vo.setTotalDOC01(
          vo.getTaxableIncomeDOC01().
          add(vo.getTotalVatDOC01().
          subtract(vo.getAllowanceDOC01()).
          subtract(vo.getDepositDOC01()))
      );
    }

    form.pull();
  }


  public final void removeAllowanceAndDeposit() {
    this.remove(labelAllowance);
    this.remove(controlAllowance);
    this.remove(labelDeposit);
    this.remove(controlDeposit);
    this.revalidate();
  }


}

class SaleTotalsPanel_controlAllowance_focusAdapter extends java.awt.event.FocusAdapter {
  SaleTotalsPanel adaptee;

  SaleTotalsPanel_controlAllowance_focusAdapter(SaleTotalsPanel adaptee) {
    this.adaptee = adaptee;
  }
  public void focusLost(FocusEvent e) {
    adaptee.controlAllowance_focusLost(e);
  }
}

class SaleTotalsPanel_controlDeposit_focusAdapter extends java.awt.event.FocusAdapter {
  SaleTotalsPanel adaptee;

  SaleTotalsPanel_controlDeposit_focusAdapter(SaleTotalsPanel adaptee) {
    this.adaptee = adaptee;
  }
  public void focusLost(FocusEvent e) {
    adaptee.controlDeposit_focusLost(e);
  }
}