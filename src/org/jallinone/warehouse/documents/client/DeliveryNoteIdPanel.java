package org.jallinone.warehouse.documents.client;

import javax.swing.JPanel;
import javax.swing.border.*;
import java.awt.*;
import org.openswing.swing.client.*;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.form.client.Form;

/**
  * <p>Title: JAllInOne ERP/CRM application</p>
  * <p>Description: Panel used in the in/out delivery note header to show delivery note identifier: doc number, doc year, etc.</p>
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
public class DeliveryNoteIdPanel extends JPanel {

  TitledBorder titledBorder1;
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  LabelControl labelDocNum = new LabelControl();
  NumericControl controlDocNumber = new NumericControl();
  LabelControl labelDocYear = new LabelControl();
  NumericControl controlDocYear = new NumericControl();
  LabelControl labelDocRif = new LabelControl();
  TextControl controlDocRif = new TextControl();
  LabelControl labelDocDate = new LabelControl();
  DateControl controlDocDate = new DateControl();
  LabelControl labelDocState = new LabelControl();
  ComboBoxControl controlDocState = new ComboBoxControl();



  public DeliveryNoteIdPanel(Form form) {
    try {
      jbInit();

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    titledBorder1 = new TitledBorder("");
    labelDocNum.setText("docNumber");
    titledBorder1.setTitle(ClientSettings.getInstance().getResources().getResource("document identification"));
    titledBorder1.setTitleColor(Color.blue);
    this.setBorder(titledBorder1);
    this.setLayout(gridBagLayout1);
    labelDocYear.setText("docYear");
    labelDocRif.setText("docRif");
    controlDocRif.setCanCopy(true);
    controlDocRif.setLinkLabel(labelDocRif);
    controlDocRif.setMaxCharacters(20);
    controlDocRif.setAttributeName("docRefDOC08");
    labelDocDate.setText("docDate");
    labelDocState.setText("docState");
    controlDocNumber.setLinkLabel(labelDocNum);
    controlDocNumber.setMaxCharacters(255);
    controlDocNumber.setRequired(false);
    controlDocNumber.setEnabledOnInsert(false);
    controlDocNumber.setEnabledOnEdit(false);
    controlDocNumber.setAttributeName("docSequenceDOC08");
    controlDocYear.setLinkLabel(labelDocYear);
    controlDocYear.setEnabledOnInsert(false);
    controlDocYear.setEnabledOnEdit(false);
    controlDocYear.setAttributeName("docYearDOC08");
    controlDocDate.setCanCopy(true);
    controlDocDate.setLinkLabel(labelDocDate);
    controlDocDate.setRequired(true);
    controlDocDate.setAttributeName("docDateDOC08");
    controlDocState.setCanCopy(false);
    controlDocState.setLinkLabel(labelDocDate);
    controlDocState.setRequired(true);
    controlDocState.setEnabledOnInsert(false);
    controlDocState.setEnabledOnEdit(false);
    controlDocState.setAttributeName("docStateDOC08");
    controlDocState.setDomainId("DOC08_STATES");
    this.add(labelDocNum,       new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlDocNumber,              new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 30, 0));
    this.add(labelDocRif,          new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlDocRif,              new GridBagConstraints(1, 1, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlDocYear,          new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 30, 0));
    this.add(labelDocYear,     new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelDocDate,    new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlDocDate,        new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 60, 0));
    this.add(labelDocState,   new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlDocState,      new GridBagConstraints(7, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 10, 0));
  }

}