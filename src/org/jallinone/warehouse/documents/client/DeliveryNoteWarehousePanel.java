package org.jallinone.warehouse.documents.client;

import javax.swing.JPanel;
import java.awt.*;
import javax.swing.border.*;
import javax.swing.BorderFactory;
import org.openswing.swing.client.*;
import java.awt.event.*;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.form.client.Form;
import org.jallinone.warehouse.documents.java.DetailDeliveryNoteVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.message.receive.java.*;
import javax.swing.JOptionPane;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.openswing.swing.lookup.client.LookupListener;
import java.util.Collection;


/**
  * <p>Title: JAllInOne ERP/CRM application</p>
  * <p>Description: Panel used in the in/out delivery note to show warehouse details.</p>
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
public class DeliveryNoteWarehousePanel extends JPanel {

  GridBagLayout gridBagLayout1 = new GridBagLayout();
  TitledBorder titledBorder1;
  LabelControl labelWarehouseCode = new LabelControl();
  CodLookupControl controlWarehouseCode = new CodLookupControl();
  TextControl controlZip = new TextControl();
  TextControl controlProv = new TextControl();
  TextControl controlCity = new TextControl();
  TextControl controlAddress = new TextControl();
  LabelControl labelAddress = new LabelControl();
  TextControl controlCountry = new TextControl();
  LabelControl labelZip = new LabelControl();
  LabelControl labelCity = new LabelControl();
  LabelControl labelCountry = new LabelControl();
  LabelControl labelProv = new LabelControl();
  TextControl controlDescr = new TextControl();

  /** delivery note detail form */
  private Form form = null;

  LookupController wareController = new LookupController();
  LookupServerDataLocator wareDataLocator = new LookupServerDataLocator();


  public DeliveryNoteWarehousePanel(final Form form) {
    this.form = form;
    try {
      jbInit();

      // warehouse lookup...
      wareDataLocator.setGridMethodName("loadWarehouses");
      wareDataLocator.setValidationMethodName("validateWarehouseCode");

      controlWarehouseCode.setLookupController(wareController);
      controlWarehouseCode.setControllerMethodName("getWarehousesList");
      wareController.setForm(form);
      wareController.setLookupDataLocator(wareDataLocator);
      wareController.setFrameTitle("warehouses");
      wareController.setLookupValueObjectClassName("org.jallinone.warehouse.java.WarehouseVO");
      wareController.addLookup2ParentLink("warehouseCodeWAR01", "warehouseCodeWar01DOC08");
      wareController.addLookup2ParentLink("descriptionWAR01","warehouseDescriptionDOC08");
      wareController.addLookup2ParentLink("progressiveHie02WAR01","progressiveHie02WAR01");
      wareController.addLookup2ParentLink("addressWAR01", "addressDOC08");
      wareController.addLookup2ParentLink("cityWAR01","cityDOC08");
      wareController.addLookup2ParentLink("zipWAR01","zipDOC08");
      wareController.addLookup2ParentLink("provinceWAR01","provinceDOC08");
      wareController.addLookup2ParentLink("countryWAR01","countryDOC08");
      wareController.setAllColumnVisible(false);
      wareController.setVisibleColumn("warehouseCodeWAR01", true);
      wareController.setVisibleColumn("descriptionWAR01", true);
      wareController.setVisibleColumn("addressWAR01", true);
      wareController.setVisibleColumn("cityWAR01", true);
      wareController.setVisibleColumn("zipWAR01", true);
      wareController.setVisibleColumn("provinceWAR01", true);
      wareController.setVisibleColumn("countryWAR01", true);
      wareController.setPreferredWidthColumn("descriptionWAR01",200);
      wareController.setFramePreferedSize(new Dimension(750,500));
      wareController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          DetailDeliveryNoteVO vo = (DetailDeliveryNoteVO)form.getVOModel().getValueObject();
          if (vo.getWarehouseCodeWar01DOC08()==null || vo.getWarehouseCodeWar01DOC08().equals("")) {
            vo.setWarehouseDescriptionDOC08(null);
            vo.setAddressDOC08(null);
            vo.setCityDOC08(null);
            vo.setProvinceDOC08(null);
            vo.setZipDOC08(null);
            vo.setCountryDOC08(null);
          }
        }

        public void beforeLookupAction(ValueObject parentVO) {
          DetailDeliveryNoteVO vo = (DetailDeliveryNoteVO)form.getVOModel().getValueObject();
          wareDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01DOC08());
          wareDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01DOC08());
        }

        public void forceValidate() {}

      });

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    titledBorder1 = new TitledBorder("");
    labelWarehouseCode.setText("warehouseCodeWAR01");
    titledBorder1.setTitle(ClientSettings.getInstance().getResources().getResource("warehouse"));
    titledBorder1.setTitleColor(Color.blue);
    this.setBorder(titledBorder1);
    this.setLayout(gridBagLayout1);
    controlWarehouseCode.setAttributeName("warehouseCodeWar01DOC08");
    controlWarehouseCode.setCanCopy(true);
    controlWarehouseCode.setLinkLabel(labelWarehouseCode);
    controlWarehouseCode.setMaxCharacters(20);
    controlWarehouseCode.setRequired(true);
    controlAddress.setEnabledOnInsert(true);
    controlAddress.setEnabledOnEdit(true);
    controlDescr.setCanCopy(true);
    controlDescr.setEnabledOnInsert(false);
    controlDescr.setEnabledOnEdit(false);
    controlCity.setEnabledOnInsert(true);
    controlCity.setEnabledOnEdit(true);
    controlZip.setEnabledOnInsert(true);
    controlZip.setEnabledOnEdit(true);
    controlCountry.setEnabledOnInsert(true);
    controlCountry.setEnabledOnEdit(true);
    controlProv.setEnabledOnEdit(true);
    this.add(labelWarehouseCode,     new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    controlZip.setAttributeName("zipDOC08");
    controlZip.setCanCopy(true);
    controlZip.setLinkLabel(labelZip);
    controlZip.setMaxCharacters(20);
    controlProv.setAttributeName("provinceDOC08");
    controlProv.setCanCopy(true);
    controlProv.setLinkLabel(labelProv);
    controlProv.setMaxCharacters(20);
    controlProv.setTrimText(true);
    controlProv.setUpperCase(true);
    controlCity.setAttributeName("cityDOC08");
    controlCity.setCanCopy(true);
    controlCity.setLinkLabel(labelCity);
    controlAddress.setAttributeName("addressDOC08");
    controlAddress.setCanCopy(true);
    controlAddress.setLinkLabel(labelAddress);
    labelAddress.setText("address");
    controlCountry.setAttributeName("countryDOC08");
    controlCountry.setCanCopy(true);
    controlCountry.setLinkLabel(labelCountry);
    controlCountry.setMaxCharacters(20);
    controlCountry.setTrimText(true);
    controlCountry.setUpperCase(true);
    labelZip.setText("zip");
    labelCity.setText("city");
    labelCountry.setText("country");
    labelProv.setText("prov");
    controlDescr.setAttributeName("warehouseDescriptionDOC08");
    this.add(labelZip,                  new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelCity,                new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelAddress,             new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlAddress,               new GridBagConstraints(2, 1, 4, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlCity,             new GridBagConstraints(2, 2, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 100, 0));
    this.add(controlZip,           new GridBagConstraints(5, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlCountry,          new GridBagConstraints(5, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelCountry,         new GridBagConstraints(4, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelProv,         new GridBagConstraints(0, 3, 1, 2, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 0), 0, 0));
    this.add(controlProv,         new GridBagConstraints(2, 3, 2, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlWarehouseCode,      new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 50, 0));
    this.add(controlDescr,  new GridBagConstraints(3, 0, 3, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

  }

}
