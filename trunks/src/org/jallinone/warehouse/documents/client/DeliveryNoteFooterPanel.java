package org.jallinone.warehouse.documents.client;

import javax.swing.JPanel;
import javax.swing.border.*;
import java.awt.*;
import org.openswing.swing.client.*;
import javax.swing.BorderFactory;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.lookup.client.LookupListener;
import org.openswing.swing.message.receive.java.*;
import java.util.Collection;
import org.openswing.swing.form.client.Form;
import org.jallinone.warehouse.documents.java.DetailDeliveryNoteVO;
import org.openswing.swing.util.client.ClientSettings;

/**
  * <p>Title: JAllInOne ERP/CRM application</p>
  * <p>Description: Panel used in the in/out delivery note to show note, carrier, etc.</p>
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
public class DeliveryNoteFooterPanel extends JPanel {

  GridBagLayout gridBagLayout1 = new GridBagLayout();
  TitledBorder titledBorder1;
  LabelControl labelCarrier = new LabelControl();
  CodLookupControl controlCarrierCode = new CodLookupControl();
  TextControl controlCarrierDescr = new TextControl();
  LabelControl labelNote = new LabelControl();
  TextAreaControl controlNote = new TextAreaControl();

  LookupController carrierController = new LookupController();
  LookupServerDataLocator carrierDataLocator = new LookupServerDataLocator();
  LabelControl labelTransportMotive = new LabelControl();
  CodLookupControl controlTransportMotive = new CodLookupControl();
  TextControl controlTransportMotiveDescr = new TextControl();

  LookupController transportMotiveController = new LookupController();
  LookupServerDataLocator transportMotiveDataLocator = new LookupServerDataLocator();


  public DeliveryNoteFooterPanel(Form form) {
    try {
      jbInit();

      // carrier lookup...
      carrierDataLocator.setGridMethodName("loadCarriers");
      carrierDataLocator.setValidationMethodName("validateCarrierCode");

      controlCarrierCode.setLookupController(carrierController);
      controlCarrierCode.setControllerMethodName("getCarriersList");
      carrierController.setForm(form);
      carrierController.setLookupDataLocator(carrierDataLocator);
      carrierController.setFrameTitle("carriers");
      carrierController.setLookupValueObjectClassName("org.jallinone.registers.carrier.java.CarrierVO");
      carrierController.addLookup2ParentLink("carrierCodeREG09", "carrierCodeReg09DOC08");
      carrierController.addLookup2ParentLink("descriptionSYS10","carrierDescriptionSYS10");
      carrierController.setAllColumnVisible(false);
      carrierController.setVisibleColumn("carrierCodeREG09", true);
      carrierController.setVisibleColumn("descriptionSYS10", true);
      carrierController.setPreferredWidthColumn("descriptionSYS10", 250);
      carrierController.setFramePreferedSize(new Dimension(350,500));


      // transport motives lookup...
      transportMotiveDataLocator.setGridMethodName("loadTransportMotives");
      transportMotiveDataLocator.setValidationMethodName("validateTransportMotiveCode");

      controlTransportMotive.setLookupController(transportMotiveController);
      controlTransportMotive.setControllerMethodName("getTransportMotivesList");
      transportMotiveController.setForm(form);
      transportMotiveController.setLookupDataLocator(transportMotiveDataLocator);
      transportMotiveController.setFrameTitle("transport motives");
      transportMotiveController.setLookupValueObjectClassName("org.jallinone.registers.transportmotives.java.TransportMotiveVO");
      transportMotiveController.addLookup2ParentLink("transportMotiveCodeREG20", "transportMotiveCodeReg20DOC08");
      transportMotiveController.addLookup2ParentLink("descriptionSYS10","transportMotiveDescriptionSYS10");
      transportMotiveController.setAllColumnVisible(false);
      transportMotiveController.setVisibleColumn("transportMotiveCodeREG20", true);
      transportMotiveController.setVisibleColumn("descriptionSYS10", true);
      transportMotiveController.setPreferredWidthColumn("descriptionSYS10", 250);
      transportMotiveController.setFramePreferedSize(new Dimension(350,500));

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    titledBorder1 = new TitledBorder("");
    labelCarrier.setText("carrierCodeREG09");
    titledBorder1.setTitle(ClientSettings.getInstance().getResources().getResource("other data"));
    titledBorder1.setBorder(BorderFactory.createEtchedBorder());
    titledBorder1.setTitleColor(Color.blue);
    this.setBorder(titledBorder1);
    this.setLayout(gridBagLayout1);
    labelNote.setText("note");
    controlCarrierDescr.setCanCopy(true);
    controlCarrierDescr.setEnabledOnInsert(false);
    controlCarrierDescr.setEnabledOnEdit(false);
    controlCarrierDescr.setAttributeName("carrierDescriptionSYS10");
    controlCarrierCode.setCanCopy(true);
    controlCarrierCode.setLinkLabel(labelCarrier);
    controlCarrierCode.setMaxCharacters(20);
    controlCarrierCode.setRequired(true);
    controlCarrierCode.setAttributeName("carrierCodeReg09DOC08");
    controlNote.setCanCopy(true);
    controlNote.setLinkLabel(labelNote);
    controlNote.setMaxCharacters(2000);
    controlNote.setAttributeName("noteDOC08");
    labelTransportMotive.setText("transportMotiveCodeREG20");
    controlTransportMotive.setAttributeName("transportMotiveCodeReg20DOC08");
    controlTransportMotive.setLinkLabel(labelTransportMotive);
    controlTransportMotive.setMaxCharacters(20);
    controlTransportMotive.setRequired(true);
    controlTransportMotiveDescr.setAttributeName("transportMotiveDescriptionSYS10");
    controlTransportMotiveDescr.setEnabledOnInsert(false);
    controlTransportMotiveDescr.setEnabledOnEdit(false);
    this.add(labelCarrier,      new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlCarrierCode,           new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 70, 0));
    this.add(controlCarrierDescr,    new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelNote,      new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlNote,      new GridBagConstraints(1, 2, 5, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelTransportMotive,   new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlTransportMotive,  new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlTransportMotiveDescr,  new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
  }

}
