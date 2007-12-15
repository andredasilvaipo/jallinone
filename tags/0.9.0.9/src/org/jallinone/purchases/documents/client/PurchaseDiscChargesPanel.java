package org.jallinone.purchases.documents.client;

import javax.swing.JPanel;
import java.awt.*;
import javax.swing.border.*;
import javax.swing.BorderFactory;
import org.openswing.swing.client.*;
import java.awt.event.*;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.form.client.Form;
import org.jallinone.purchases.documents.java.DetailPurchaseDocVO;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.message.receive.java.*;
import javax.swing.JOptionPane;


/**
  * <p>Title: JAllInOne ERP/CRM application</p>
  * <p>Description: Panel used in the purchase header to show purchase header discounts and charges.</p>
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
public class PurchaseDiscChargesPanel extends JPanel {
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  TitledBorder titledBorder1;
  LabelControl labelDiscValue = new LabelControl();
  CurrencyControl controlDiscValue = new CurrencyControl();
  LabelControl labelDiscPerc = new LabelControl();
  NumericControl controlDiscPerc = new NumericControl();
  LabelControl labelChargeValue = new LabelControl();
  LabelControl labelChargePerc = new LabelControl();
  CurrencyControl controlChargeValue = new CurrencyControl();
  NumericControl controlChargePerc = new NumericControl();

  /** parent form */
  private Form parentForm = null;


  public PurchaseDiscChargesPanel(Form parentForm) {
    this.parentForm = parentForm;
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    titledBorder1 = new TitledBorder("");
    labelDiscValue.setText("discount value");
    titledBorder1.setTitle(ClientSettings.getInstance().getResources().getResource("discounts and charges"));
    titledBorder1.setBorder(BorderFactory.createEtchedBorder());
    titledBorder1.setTitleColor(Color.blue);
    this.setBorder(titledBorder1);
    this.setLayout(gridBagLayout1);
    labelDiscPerc.setText("discount perc");
    labelChargeValue.setText("charge value");
    labelChargePerc.setText("charge perc");
    controlDiscValue.setLinkLabel(labelDiscValue);
    controlDiscValue.addFocusListener(new PurchaseDiscChargesPanel_controlDiscValue_focusAdapter(this));
    controlDiscValue.setAttributeName("discountValueDOC06");
    controlDiscValue.setCanCopy(true);
    controlDiscValue.setDecimals(5);
    controlDiscPerc.addFocusListener(new PurchaseDiscChargesPanel_controlDiscPerc_focusAdapter(this));
    controlDiscPerc.setAttributeName("discountPercDOC06");
    controlDiscPerc.setCanCopy(true);
    controlDiscPerc.setDecimals(2);
    controlDiscPerc.setLinkLabel(labelDiscPerc);
    controlDiscPerc.setMaxValue(100.0);
    controlDiscPerc.setMinValue(0.0);
    controlChargeValue.addFocusListener(new PurchaseDiscChargesPanel_controlChargeValue_focusAdapter(this));
    controlChargeValue.setAttributeName("chargeValueDOC06");
    controlChargeValue.setCanCopy(true);
    controlChargeValue.setDecimals(5);
    controlChargeValue.setLinkLabel(labelChargeValue);
    controlChargePerc.addFocusListener(new PurchaseDiscChargesPanel_controlChargePerc_focusAdapter(this));
    controlChargePerc.setAttributeName("chargePercDOC06");
    controlChargePerc.setCanCopy(true);
    controlChargePerc.setDecimals(2);
    controlChargePerc.setLinkLabel(labelChargePerc);
    controlChargePerc.setMaxValue(100.0);
    controlChargePerc.setMinValue(0.0);
    this.add(labelDiscValue,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlDiscValue,   new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelDiscPerc,   new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlDiscPerc,   new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelChargeValue,   new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelChargePerc,   new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlChargeValue,   new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlChargePerc,    new GridBagConstraints(3, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
  }


  private void recalculateTotals() {
    DetailPurchaseDocVO vo = (DetailPurchaseDocVO)parentForm.getVOModel().getValueObject();
    Response res = ClientUtils.getData("getPurchaseDocTotals",vo);
    if (res.isError()) {
      JOptionPane.showMessageDialog(
          ClientUtils.getParentFrame(this),
          ClientSettings.getInstance().getResources().getResource("Error while loading data")+":\n"+res.getErrorMessage(),
          ClientSettings.getInstance().getResources().getResource("Loading Data Error"),
          JOptionPane.ERROR_MESSAGE
      );
    }
    else {
      DetailPurchaseDocVO newVO = (DetailPurchaseDocVO)((VOResponse)res).getVo();
      vo.setTotalDOC06(newVO.getTotalDOC06());
      vo.setTaxableIncomeDOC06(newVO.getTaxableIncomeDOC06());
      vo.setTotalVatDOC06(newVO.getTotalVatDOC06());
      parentForm.pull();
    }
  }


  void controlDiscValue_focusLost(FocusEvent e) {
    DetailPurchaseDocVO vo = (DetailPurchaseDocVO)parentForm.getVOModel().getValueObject();
    if (controlDiscPerc.getValue()!=null && controlDiscValue.getValue()!=null)
      vo.setDiscountPercDOC06(null);
    vo.setDiscountValueDOC06(controlDiscValue.getBigDecimal());
    recalculateTotals();
  }


  void controlDiscPerc_focusLost(FocusEvent e) {
    DetailPurchaseDocVO vo = (DetailPurchaseDocVO)parentForm.getVOModel().getValueObject();
    if (controlDiscPerc.getValue()!=null && controlDiscValue.getValue()!=null)
      vo.setDiscountValueDOC06(null);
    vo.setDiscountPercDOC06(controlDiscPerc.getBigDecimal());
    recalculateTotals();
  }


  void controlChargeValue_focusLost(FocusEvent e) {
    DetailPurchaseDocVO vo = (DetailPurchaseDocVO)parentForm.getVOModel().getValueObject();
    if (controlChargePerc.getValue()!=null && controlChargeValue.getValue()!=null)
      vo.setChargePercDOC06(null);
    vo.setChargeValueDOC06(controlChargeValue.getBigDecimal());
    recalculateTotals();
  }


  void controlChargePerc_focusLost(FocusEvent e) {
    DetailPurchaseDocVO vo = (DetailPurchaseDocVO)parentForm.getVOModel().getValueObject();
    if (controlChargePerc.getValue()!=null && controlChargeValue.getValue()!=null)
      vo.setChargeValueDOC06(null);
    vo.setChargePercDOC06(controlChargePerc.getBigDecimal());
    recalculateTotals();
  }


  public CurrencyControl getControlChargeValue() {
    return controlChargeValue;
  }


  public CurrencyControl getControlDiscValue() {
    return controlDiscValue;
  }




}

class PurchaseDiscChargesPanel_controlDiscValue_focusAdapter extends java.awt.event.FocusAdapter {
  PurchaseDiscChargesPanel adaptee;

  PurchaseDiscChargesPanel_controlDiscValue_focusAdapter(PurchaseDiscChargesPanel adaptee) {
    this.adaptee = adaptee;
  }
  public void focusLost(FocusEvent e) {
    adaptee.controlDiscValue_focusLost(e);
  }
}

class PurchaseDiscChargesPanel_controlDiscPerc_focusAdapter extends java.awt.event.FocusAdapter {
  PurchaseDiscChargesPanel adaptee;

  PurchaseDiscChargesPanel_controlDiscPerc_focusAdapter(PurchaseDiscChargesPanel adaptee) {
    this.adaptee = adaptee;
  }
  public void focusLost(FocusEvent e) {
    adaptee.controlDiscPerc_focusLost(e);
  }
}

class PurchaseDiscChargesPanel_controlChargeValue_focusAdapter extends java.awt.event.FocusAdapter {
  PurchaseDiscChargesPanel adaptee;

  PurchaseDiscChargesPanel_controlChargeValue_focusAdapter(PurchaseDiscChargesPanel adaptee) {
    this.adaptee = adaptee;
  }
  public void focusLost(FocusEvent e) {
    adaptee.controlChargeValue_focusLost(e);
  }
}

class PurchaseDiscChargesPanel_controlChargePerc_focusAdapter extends java.awt.event.FocusAdapter {
  PurchaseDiscChargesPanel adaptee;

  PurchaseDiscChargesPanel_controlChargePerc_focusAdapter(PurchaseDiscChargesPanel adaptee) {
    this.adaptee = adaptee;
  }
  public void focusLost(FocusEvent e) {
    adaptee.controlChargePerc_focusLost(e);
  }
}