package org.jallinone.warehouse.documents.client;

import javax.swing.JPanel;
import javax.swing.border.*;
import java.awt.*;
import org.openswing.swing.client.*;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.form.client.Form;
import org.jallinone.commons.java.ApplicationConsts;

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
public class OutDeliveryNoteIdPanel extends JPanel {

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

  LookupController docRefController = new LookupController();
  LookupServerDataLocator docRefDataLocator = new LookupServerDataLocator();
  LabelControl labelCustomerCode = new LabelControl();
  CodLookupControl controlCustomerCode = new CodLookupControl();
  TextControl controlFirstName = new TextControl();
  TextControl controlLastName = new TextControl();

  LookupController customerController = new LookupController();
  LookupServerDataLocator customerDataLocator = new LookupServerDataLocator();
  LabelControl labelDelivDate = new LabelControl();
  DateControl controlDelivDate = new DateControl();


  public OutDeliveryNoteIdPanel(Form form) {
    try {
      jbInit();

      // customer lookup...
      customerDataLocator.setGridMethodName("loadCustomers");
      customerDataLocator.setValidationMethodName("validateCustomerCode");

      controlCustomerCode.setLookupController(customerController);
      controlCustomerCode.setControllerMethodName("getCustomersList");
      customerController.setForm(form);
      customerController.setLookupDataLocator(customerDataLocator);
      customerController.setFrameTitle("customers");
      customerController.setLookupValueObjectClassName("org.jallinone.sales.customers.java.GridCustomerVO");
      customerController.addLookup2ParentLink("companyCodeSys01REG04", "companyCodeSys01DOC08");
      customerController.addLookup2ParentLink("customerCodeSAL07","supplierCustomerCodeDOC08");
      customerController.addLookup2ParentLink("progressiveREG04","progressiveReg04DOC08");
      customerController.addLookup2ParentLink("name_1REG04", "name_1REG04");
      customerController.addLookup2ParentLink("name_2REG04", "name_2REG04");
      customerController.addLookup2ParentLink("noteREG04", "noteDOC08");
      customerController.setAllColumnVisible(false);
      customerController.setVisibleColumn("companyCodeSys01REG04", true);
      customerController.setVisibleColumn("customerCodeSAL07", true);
      customerController.setVisibleColumn("name_1REG04", true);
      customerController.setVisibleColumn("name_2REG04", true);
      customerController.setVisibleColumn("cityREG04", true);
      customerController.setVisibleColumn("provinceREG04", true);
      customerController.setVisibleColumn("countryREG04", true);
      customerController.setVisibleColumn("taxCodeREG04", true);
      customerController.setHeaderColumnName("cityREG04", "city");
      customerController.setHeaderColumnName("provinceREG04", "prov");
      customerController.setHeaderColumnName("countryREG04", "country");
      customerController.setHeaderColumnName("taxCodeREG04", "taxCode");
      customerController.setPreferredWidthColumn("name_1REG04", 200);
      customerController.setPreferredWidthColumn("name_2REG04", 150);
      customerController.setFramePreferedSize(new Dimension(750,500));

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
    labelCustomerCode.setText("customerCodeSAL07");
    controlCustomerCode.setMaxCharacters(20);
    controlCustomerCode.setRequired(true);
    controlCustomerCode.setAttributeName("supplierCustomerCodeDOC08");
    controlFirstName.setEnabledOnInsert(false);
    controlFirstName.setEnabledOnEdit(false);
    controlFirstName.setAttributeName("name_1REG04");
    controlLastName.setEnabledOnInsert(false);
    controlLastName.setEnabledOnEdit(false);
    controlLastName.setAttributeName("name_2REG04");
    labelDelivDate.setText("deliveryDate");
    controlDelivDate.setLinkLabel(labelDelivDate);
    controlDelivDate.setRequired(true);
    controlDelivDate.setAttributeName("deliveryDateDOC08");
    this.add(labelDocNum,        new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlDocNumber,                new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelDocRif,           new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlDocRif,               new GridBagConstraints(1, 1, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlDocYear,            new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    this.add(labelDocYear,      new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelDocDate,     new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlDocDate,         new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 60, 0));
    this.add(labelDocState,     new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelCustomerCode,     new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlCustomerCode,         new GridBagConstraints(5, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlFirstName,      new GridBagConstraints(6, 2, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    this.add(controlLastName,        new GridBagConstraints(8, 2, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 80, 0));
    this.add(controlDocState,    new GridBagConstraints(7, 0, 2, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelDelivDate,   new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlDelivDate,  new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 60, 0));
  }

}
