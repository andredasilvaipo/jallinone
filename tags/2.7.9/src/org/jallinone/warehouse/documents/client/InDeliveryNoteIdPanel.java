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
public class InDeliveryNoteIdPanel extends JPanel {

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
  LabelControl labelSupplierCode = new LabelControl();
  CodLookupControl controlSupplierCode = new CodLookupControl();
  TextControl controlSupplierDescr = new TextControl();

  LookupController supplierController = new LookupController();
  LookupServerDataLocator supplierDataLocator = new LookupServerDataLocator();
  LabelControl labelDelivDate = new LabelControl();
  DateControl controlDelivDate = new DateControl();


  public InDeliveryNoteIdPanel(Form form) {
    try {
      jbInit();

      // supplier lookup...
      supplierDataLocator.setGridMethodName("loadSuppliers");
      supplierDataLocator.setValidationMethodName("validateSupplierCode");

      controlSupplierCode.setLookupController(supplierController);
      controlSupplierCode.setControllerMethodName("getSuppliersList");
      supplierController.setForm(form);
      supplierController.setLookupDataLocator(supplierDataLocator);
      supplierController.setFrameTitle("suppliers");
      supplierController.setLookupValueObjectClassName("org.jallinone.purchases.suppliers.java.GridSupplierVO");
      supplierController.addLookup2ParentLink("companyCodeSys01REG04", "companyCodeSys01DOC08");
      supplierController.addLookup2ParentLink("supplierCodePUR01","supplierCustomerCodeDOC08");
      supplierController.addLookup2ParentLink("progressiveREG04","progressiveReg04DOC08");
      supplierController.addLookup2ParentLink("name_1REG04", "name_1REG04");
      supplierController.addLookup2ParentLink("noteREG04", "noteDOC08");
      supplierController.setAllColumnVisible(false);
      supplierController.setVisibleColumn("companyCodeSys01REG04", true);
      supplierController.setVisibleColumn("supplierCodePUR01", true);
      supplierController.setVisibleColumn("name_1REG04", true);
      supplierController.setVisibleColumn("name_2REG04", true);
      supplierController.setVisibleColumn("cityREG04", true);
      supplierController.setVisibleColumn("provinceREG04", true);
      supplierController.setVisibleColumn("countryREG04", true);
      supplierController.setVisibleColumn("taxCodeREG04", true);
      supplierController.setHeaderColumnName("name_1REG04", "corporateName1");
      supplierController.setHeaderColumnName("cityREG04", "city");
      supplierController.setHeaderColumnName("provinceREG04", "prov");
      supplierController.setHeaderColumnName("countryREG04", "country");
      supplierController.setHeaderColumnName("taxCodeREG04", "taxCode");
      supplierController.setPreferredWidthColumn("name_1REG04", 200);
      supplierController.setPreferredWidthColumn("name_2REG04", 150);
      supplierController.setFramePreferedSize(new Dimension(750,500));

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
    labelSupplierCode.setText("supplierCodePUR01");
    controlSupplierCode.setMaxCharacters(20);
    controlSupplierCode.setRequired(true);
    controlSupplierCode.setAttributeName("supplierCustomerCodeDOC08");
    controlSupplierDescr.setEnabledOnInsert(false);
    controlSupplierDescr.setEnabledOnEdit(false);
    controlSupplierDescr.setAttributeName("name_1REG04");
    labelDelivDate.setText("deliveryDate");
    controlDelivDate.setAttributeName("deliveryDateDOC08");
    controlDelivDate.setLinkLabel(labelDelivDate);
    controlDelivDate.setRequired(true);
    this.add(labelDocNum,       new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlDocNumber,               new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelDocRif,          new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlDocRif,              new GridBagConstraints(1, 1, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlDocYear,           new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    this.add(labelDocYear,     new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelDocDate,    new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlDocDate,          new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 60, 0));
    this.add(labelDocState,   new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlDocState,        new GridBagConstraints(7, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 60, 0));
    this.add(labelSupplierCode,    new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlSupplierCode,         new GridBagConstraints(5, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlSupplierDescr,    new GridBagConstraints(6, 2, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    this.add(labelDelivDate,   new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlDelivDate,      new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 60, 0));
  }

}
