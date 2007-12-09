package org.jallinone.purchases.documents.client;

import javax.swing.JPanel;
import java.awt.*;
import javax.swing.border.*;
import org.openswing.swing.client.*;
import org.openswing.swing.util.client.ClientSettings;

/**
  * <p>Title: JAllInOne ERP/CRM application</p>
  * <p>Description: Panel used in the purchase header to show purchase doc. totals.</p>
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
public class PurchaseTotalsPanel extends JPanel {

  GridBagLayout gridBagLayout1 = new GridBagLayout();
  TitledBorder titledBorder1;
  LabelControl labelTaxableIncome = new LabelControl();
  CurrencyControl controlTaxableIncome = new CurrencyControl();
  LabelControl labelTotalVat = new LabelControl();
  CurrencyControl controlTotalVat = new CurrencyControl();
  LabelControl labelTotal = new LabelControl();
  CurrencyControl controlTotal = new CurrencyControl();


  public PurchaseTotalsPanel() {
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
    controlTotal.setAttributeName("totalDOC06");
    controlTotalVat.setDecimals(5);
    controlTotalVat.setLinkLabel(labelTotalVat);
    controlTotalVat.setEnabledOnInsert(false);
    controlTotalVat.setEnabledOnEdit(false);
    controlTotalVat.setAttributeName("totalVatDOC06");
    controlTaxableIncome.setDecimals(5);
    controlTaxableIncome.setLinkLabel(labelTaxableIncome);
    controlTaxableIncome.setEnabledOnInsert(false);
    controlTaxableIncome.setEnabledOnEdit(false);
    controlTaxableIncome.setAttributeName("taxableIncomeDOC06");
    this.add(labelTotalVat,    new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlTotalVat,   new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelTotal,    new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlTotal,    new GridBagConstraints(5, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelTaxableIncome,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlTaxableIncome,  new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
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

}