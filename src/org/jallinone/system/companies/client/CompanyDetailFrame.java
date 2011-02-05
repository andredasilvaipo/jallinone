package org.jallinone.system.companies.client;

import org.openswing.swing.mdi.client.InternalFrame;
import javax.swing.*;
import java.awt.*;
import org.openswing.swing.client.*;
import org.jallinone.subjects.client.*;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Detail frame related to a company.</p>
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
public class CompanyDetailFrame extends InternalFrame {
  JPanel buttonsPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  InsertButton insertButton = new InsertButton();
  EditButton editButton = new EditButton();
  SaveButton saveButton = new SaveButton();
  ReloadButton reloadButton = new ReloadButton();
  DeleteButton deleteButton = new DeleteButton();
  OrganizationPanel organizationPanel = new OrganizationPanel(false);
  LabelControl labelCompanyCode = new LabelControl();
  TextControl controlCompanyCode = new TextControl();
  LabelControl labelCurrency = new LabelControl();
  CodLookupControl controlCurrency = new CodLookupControl();
  LookupController currencyController = new LookupController();
  LookupServerDataLocator currencyDataLocator = new LookupServerDataLocator();


  public CompanyDetailFrame(CompanyController controller) {
    try {
      jbInit();

      // customer lookup...
      currencyDataLocator.setGridMethodName("loadCurrencies");
      currencyDataLocator.setValidationMethodName("validateCurrencyCode");

      controlCurrency.setLookupController(currencyController);
      controlCurrency.setControllerMethodName("getCurrenciesList");
      currencyController.setLookupDataLocator(currencyDataLocator);
      currencyController.setFrameTitle("currencies");
      currencyController.setLookupValueObjectClassName("org.jallinone.registers.currency.java.CurrencyVO");
      currencyController.addLookup2ParentLink("currencyCodeREG03","currencyCodeReg03");
      currencyController.setAllColumnVisible(false);
      currencyController.setVisibleColumn("currencyCodeREG03", true);
      currencyController.setVisibleColumn("currencySymbolREG03", true);
      currencyController.setFramePreferedSize(new Dimension(170,500));
      currencyController.setPreferredWidthColumn("currencySymbolREG03",40);

      setSize(750,420);
      organizationPanel.setFormController(controller);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    this.setTitle(ClientSettings.getInstance().getResources().getResource("company detail"));
    organizationPanel.setVOClassName("org.jallinone.subjects.java.OrganizationVO");
    this.setMinimumSize(new Dimension(750, 420));
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    insertButton.setText("insertButton1");
    editButton.setText("editButton1");
    saveButton.setEnabled(false);
    saveButton.setText("saveButton1");
    reloadButton.setText("reloadButton1");
    deleteButton.setText("deleteButton1");
    organizationPanel.setInsertButton(insertButton);
    organizationPanel.setEditButton(editButton);
    organizationPanel.setReloadButton(reloadButton);
    organizationPanel.setDeleteButton(deleteButton);
    organizationPanel.setSaveButton(saveButton);
    organizationPanel.setFunctionId("SYS01");
    labelCompanyCode.setText("companyCodeSYS01");
    controlCompanyCode.setAttributeName("companyCodeSys01REG04");
    controlCompanyCode.setLinkLabel(labelCompanyCode);
    controlCompanyCode.setRequired(true);
    controlCompanyCode.setTrimText(true);
    controlCompanyCode.setUpperCase(true);
    controlCompanyCode.setEnabledOnEdit(false);
    labelCurrency.setText("currencyCodeREG03");
    controlCurrency.setAttributeName("currencyCodeReg03");
    controlCurrency.setEnabledOnEdit(false);
    controlCurrency.setLinkLabel(labelCurrency);
    controlCurrency.setMaxCharacters(20);
    controlCurrency.setRequired(true);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(saveButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(deleteButton, null);
    this.getContentPane().add(organizationPanel, BorderLayout.CENTER);
    organizationPanel.add(labelCompanyCode,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    organizationPanel.add(controlCompanyCode,  new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    organizationPanel.add(labelCurrency,  new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    organizationPanel.add(controlCurrency,  new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
  }


  /**
   * @return form panel
   */
  public final OrganizationPanel getOrganizationPanel() {
    return organizationPanel;
  }
  public DeleteButton getDeleteButton() {
    return deleteButton;
  }
  public EditButton getEditButton() {
    return editButton;
  }
  public InsertButton getInsertButton() {
    return insertButton;
  }
  public SaveButton getSaveButton() {
    return saveButton;
  }

}
