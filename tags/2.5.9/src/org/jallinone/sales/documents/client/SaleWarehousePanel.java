package org.jallinone.sales.documents.client;

import javax.swing.JPanel;
import java.awt.*;
import javax.swing.border.*;
import javax.swing.BorderFactory;
import org.openswing.swing.client.*;
import java.awt.event.*;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.form.client.Form;
import org.jallinone.sales.documents.java.DetailSaleDocVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.message.receive.java.*;
import javax.swing.JOptionPane;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.openswing.swing.lookup.client.LookupListener;
import java.util.Collection;
import org.jallinone.subjects.java.SubjectPK;


/**
  * <p>Title: JAllInOne ERP/CRM application</p>
  * <p>Description: Panel used in the sale header to show warehouse and destination details.</p>
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
public class SaleWarehousePanel extends JPanel {

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

  /** sale detail form */
  private Form form = null;

  LookupController wareController = new LookupController();
  LookupServerDataLocator wareDataLocator = new LookupServerDataLocator();

  LookupController destController = new LookupController();
  LookupServerDataLocator destDataLocator = new LookupServerDataLocator();

  LabelControl labelDest = new LabelControl();
  CodLookupControl controlDestCode = new CodLookupControl();
  TextControl controlDestDescr = new TextControl();


  public SaleWarehousePanel(final Form form) {
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
      wareController.addLookup2ParentLink("warehouseCodeWAR01", "warehouseCodeWar01DOC01");
      wareController.addLookup2ParentLink("descriptionWAR01","descriptionWar01DOC01");
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

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {}

        public void beforeLookupAction(ValueObject parentVO) {
          DetailSaleDocVO vo = (DetailSaleDocVO)form.getVOModel().getValueObject();
          wareDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01DOC01());
          wareDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01DOC01());
        }

        public void forceValidate() {}

      });



      // destination lookup...
      destDataLocator.setGridMethodName("loadDestinations");
      destDataLocator.setValidationMethodName("validateDestinationCode");

      controlDestCode.setLookupController(destController);
      destController.setForm(form);
      destController.setLookupDataLocator(destDataLocator);
      destController.setFrameTitle("destinations");
      destController.setLookupValueObjectClassName("org.jallinone.sales.destinations.java.DestinationVO");
      destController.addLookup2ParentLink("destinationCodeREG18", "destinationCodeReg18DOC01");
      destController.addLookup2ParentLink("descriptionREG18","descriptionDOC01");
      destController.addLookup2ParentLink("addressREG18", "addressDOC01");
      destController.addLookup2ParentLink("cityREG18","cityDOC01");
      destController.addLookup2ParentLink("zipREG18","zipDOC01");
      destController.addLookup2ParentLink("provinceREG18","provinceDOC01");
      destController.addLookup2ParentLink("countryREG18","countryDOC01");
      destController.setAllColumnVisible(false);
      destController.setVisibleColumn("destinationCodeREG18", true);
      destController.setVisibleColumn("descriptionREG18", true);
      destController.setVisibleColumn("addressREG18", true);
      destController.setVisibleColumn("cityREG18", true);
      destController.setVisibleColumn("zipREG18", true);
      destController.setVisibleColumn("provinceREG18", true);
      destController.setVisibleColumn("countryREG18", true);

      destController.setHeaderColumnName("addressREG18", "address");
      destController.setHeaderColumnName("cityREG18", "city");
      destController.setHeaderColumnName("zipREG18", "zip");
      destController.setHeaderColumnName("provinceREG18", "province");
      destController.setHeaderColumnName("countryREG18", "country");

      destController.setPreferredWidthColumn("descriptionREG18",200);
      destController.setFramePreferedSize(new Dimension(750,500));
      destController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          DetailSaleDocVO vo = (DetailSaleDocVO)form.getVOModel().getValueObject();
          if (vo.getWarehouseCodeWar01DOC01()==null || vo.getWarehouseCodeWar01DOC01().equals("")) {
            vo.setDescriptionWar01DOC01(null);
            vo.setAddressDOC01(null);
            vo.setCityDOC01(null);
            vo.setProvinceDOC01(null);
            vo.setZipDOC01(null);
            vo.setCountryDOC01(null);
          }
        }

        public void beforeLookupAction(ValueObject parentVO) {
          DetailSaleDocVO vo = (DetailSaleDocVO)form.getVOModel().getValueObject();
          destDataLocator.getLookupFrameParams().put(ApplicationConsts.SUBJECT_PK,new SubjectPK(vo.getCompanyCodeSys01DOC01(),vo.getProgressiveReg04DOC01()));
          destDataLocator.getLookupValidationParameters().put(ApplicationConsts.SUBJECT_PK,new SubjectPK(vo.getCompanyCodeSys01DOC01(),vo.getProgressiveReg04DOC01()));
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
    controlWarehouseCode.setAttributeName("warehouseCodeWar01DOC01");
    controlWarehouseCode.setCanCopy(true);
    controlWarehouseCode.setLinkLabel(labelWarehouseCode);
    controlWarehouseCode.setMaxCharacters(20);
    controlWarehouseCode.setRequired(true);
    controlAddress.setEnabledOnInsert(false);
    controlAddress.setEnabledOnEdit(false);
    controlDescr.setCanCopy(true);
    controlDescr.setEnabledOnInsert(false);
    controlDescr.setEnabledOnEdit(false);
    controlCity.setEnabledOnInsert(false);
    controlCity.setEnabledOnEdit(false);
    controlZip.setEnabledOnInsert(false);
    controlZip.setEnabledOnEdit(false);
    controlCountry.setEnabledOnInsert(false);
    controlCountry.setEnabledOnEdit(false);
    controlProv.setEnabledOnInsert(false);
    controlProv.setEnabledOnEdit(false);
    labelDest.setText("destinationCodeREG18");
    controlDestCode.setAttributeName("destinationCodeReg18DOC01");
    controlDestCode.setLinkLabel(labelDest);
    controlDestCode.setMaxCharacters(20);
    controlDestDescr.setEnabledOnInsert(false);
    controlDestDescr.setEnabledOnEdit(false);
    controlDestDescr.setAttributeName("descriptionDOC01");
    this.add(labelWarehouseCode,          new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    controlZip.setAttributeName("zipDOC01");
    controlZip.setCanCopy(true);
    controlZip.setLinkLabel(labelZip);
    controlZip.setMaxCharacters(20);
    controlProv.setAttributeName("provinceDOC01");
    controlProv.setCanCopy(true);
    controlProv.setLinkLabel(labelProv);
    controlProv.setMaxCharacters(20);
    controlProv.setTrimText(true);
    controlProv.setUpperCase(true);
    controlCity.setAttributeName("cityDOC01");
    controlCity.setCanCopy(true);
    controlCity.setLinkLabel(labelCity);
    controlAddress.setAttributeName("addressDOC01");
    controlAddress.setCanCopy(true);
    controlAddress.setLinkLabel(labelAddress);
    labelAddress.setText("address");
    controlCountry.setAttributeName("countryDOC01");
    controlCountry.setCanCopy(true);
    controlCountry.setLinkLabel(labelCountry);
    controlCountry.setMaxCharacters(20);
    controlCountry.setTrimText(true);
    controlCountry.setUpperCase(true);
    labelZip.setText("zip");
    labelCity.setText("city");
    labelCountry.setText("country");
    labelProv.setText("prov");
    controlDescr.setAttributeName("descriptionWar01DOC01");
    controlDescr.setRequired(true);
    this.add(labelZip,                       new GridBagConstraints(4, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelCity,                     new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelAddress,                  new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
    this.add(controlAddress,                     new GridBagConstraints(2, 2, 8, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlCity,                  new GridBagConstraints(2, 3, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 100, 0));
    this.add(controlZip,                 new GridBagConstraints(5, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlWarehouseCode,          new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    this.add(controlDescr,       new GridBagConstraints(3, 0, 7, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelDest,       new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlDestCode,       new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlDestDescr,             new GridBagConstraints(3, 1, 7, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelProv,     new GridBagConstraints(6, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlProv,    new GridBagConstraints(7, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(labelCountry,   new GridBagConstraints(8, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.add(controlCountry,  new GridBagConstraints(9, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

  }
  public LookupController getWareController() {
    return wareController;
  }

}