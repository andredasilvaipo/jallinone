package org.jallinone.sales.documents.client;

import java.awt.*;
import javax.swing.*;
import org.openswing.swing.client.*;
import javax.swing.border.*;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.form.client.Form;
import java.awt.event.*;
import org.openswing.swing.table.java.ServerGridDataLocator;
import org.openswing.swing.util.java.Consts;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.sales.documents.java.*;
import java.math.BigDecimal;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.openswing.swing.lookup.client.LookupListener;
import org.openswing.swing.message.receive.java.*;
import java.util.Collection;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.tree.client.TreeServerDataLocator;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.domains.java.Domain;
import org.jallinone.items.java.ItemTypeVO;
import java.util.ArrayList;
import java.beans.Beans;
import org.jallinone.warehouse.availability.client.ItemAvailabilityPanel;
import org.jallinone.warehouse.availability.client.BookedItemsPanel;
import org.jallinone.warehouse.availability.client.OrderedItemsPanel;
import org.jallinone.sales.documents.itemdiscounts.client.*;
import java.util.HashSet;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Panel that contains head discounts for a retail selling.</p>
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
public class SaleDeskHeadDiscountPanel extends JPanel {

  TitledBorder titledBorder1;
  CurrencyControl controlDiscValue = new CurrencyControl();
  LabelControl labelDiscValue = new LabelControl();
  LabelControl labelDiscPerc = new LabelControl();
  NumericControl controlDiscPerc = new NumericControl();
  GridBagLayout gridBagLayout1 = new GridBagLayout();

  private Form parentForm = null;


  public SaleDeskHeadDiscountPanel(Form parentForm) {
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
    controlDiscPerc.setMinValue(0.0);
    controlDiscPerc.setMaxValue(100.0);
    controlDiscPerc.setLinkLabel(labelDiscPerc);
    controlDiscPerc.setDecimals(2);
    controlDiscPerc.setCanCopy(true);
    controlDiscPerc.setAttributeName("discountPercDOC01");
    controlDiscPerc.addFocusListener(new SaleDeskHeadDiscountPanel_controlDiscPerc_focusAdapter(this));
    labelDiscPerc.setText("discount perc");
    labelDiscValue.setText("discount value");
    controlDiscValue.setDecimals(5);
    controlDiscValue.setCanCopy(true);
    controlDiscValue.setAttributeName("discountValueDOC01");
    controlDiscValue.addFocusListener(new SaleDeskHeadDiscountPanel_controlDiscValue_focusAdapter(this));
    controlDiscValue.setLinkLabel(labelDiscValue);
    titledBorder1.setTitle(ClientSettings.getInstance().getResources().getResource("discounts"));
    titledBorder1.setTitleColor(Color.blue);
    this.setBorder(titledBorder1);
    this.setLayout(gridBagLayout1);
    this.add(controlDiscValue,     new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelDiscValue,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelDiscPerc,   new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlDiscPerc,     new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
  }


  void controlDiscValue_focusLost(FocusEvent e) {
    DetailSaleDocVO vo = (DetailSaleDocVO)parentForm.getVOModel().getValueObject();
    if (controlDiscPerc.getValue()!=null && controlDiscValue.getValue()!=null)
      vo.setDiscountPercDOC01(null);
    vo.setDiscountValueDOC01(controlDiscValue.getBigDecimal());
    recalculateTotals();
  }


  void controlDiscPerc_focusLost(FocusEvent e) {
    DetailSaleDocVO vo = (DetailSaleDocVO)parentForm.getVOModel().getValueObject();
    if (controlDiscPerc.getValue()!=null && controlDiscValue.getValue()!=null)
      vo.setDiscountValueDOC01(null);
    vo.setDiscountPercDOC01(controlDiscPerc.getBigDecimal());
    recalculateTotals();
  }


  private void recalculateTotals() {
    DetailSaleDocVO vo = (DetailSaleDocVO)parentForm.getVOModel().getValueObject();
    Response res = ClientUtils.getData("getSaleDocTotals",vo);
    if (res.isError()) {
      JOptionPane.showMessageDialog(
          ClientUtils.getParentFrame(this),
          ClientSettings.getInstance().getResources().getResource("Error while loading data")+":\n"+res.getErrorMessage(),
          ClientSettings.getInstance().getResources().getResource("Loading Data Error"),
          JOptionPane.ERROR_MESSAGE
      );
    }
    else {
      DetailSaleDocVO newVO = (DetailSaleDocVO)((VOResponse)res).getVo();
      vo.setTotalDOC01(newVO.getTotalDOC01());
      vo.setTaxableIncomeDOC01(newVO.getTaxableIncomeDOC01());
      vo.setTotalVatDOC01(newVO.getTotalVatDOC01());
      parentForm.pull();
    }
  }
  public CurrencyControl getControlDiscValue() {
    return controlDiscValue;
  }


}


class SaleDeskHeadDiscountPanel_controlDiscValue_focusAdapter extends java.awt.event.FocusAdapter {
  SaleDeskHeadDiscountPanel adaptee;

  SaleDeskHeadDiscountPanel_controlDiscValue_focusAdapter(SaleDeskHeadDiscountPanel adaptee) {
    this.adaptee = adaptee;
  }
  public void focusLost(FocusEvent e) {
    adaptee.controlDiscValue_focusLost(e);
  }
}

class SaleDeskHeadDiscountPanel_controlDiscPerc_focusAdapter extends java.awt.event.FocusAdapter {
  SaleDeskHeadDiscountPanel adaptee;

  SaleDeskHeadDiscountPanel_controlDiscPerc_focusAdapter(SaleDeskHeadDiscountPanel adaptee) {
    this.adaptee = adaptee;
  }
  public void focusLost(FocusEvent e) {
    adaptee.controlDiscPerc_focusLost(e);
  }
}
