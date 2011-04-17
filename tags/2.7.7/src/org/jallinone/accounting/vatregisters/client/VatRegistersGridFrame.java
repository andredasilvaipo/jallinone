package org.jallinone.accounting.vatregisters.client;

import org.openswing.swing.mdi.client.InternalFrame;
import org.openswing.swing.tree.client.*;
import java.awt.*;
import org.openswing.swing.mdi.client.MDIFrame;
import javax.swing.*;
import org.openswing.swing.client.*;
import org.openswing.swing.table.java.ServerGridDataLocator;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.table.client.GridController;
import org.jallinone.commons.client.CustomizedColumns;
import java.math.BigDecimal;
import org.jallinone.commons.client.ClientApplet;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.system.java.ButtonCompanyAuthorizations;
import org.jallinone.commons.client.ApplicationClientFacade;
import java.util.ArrayList;
import org.openswing.swing.domains.java.Domain;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.openswing.swing.lookup.client.LookupListener;
import org.openswing.swing.message.receive.java.*;
import java.util.Collection;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the vat register grid frame.</p>
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
public class VatRegistersGridFrame extends InternalFrame {

  JPanel buttonsPanel = new JPanel();
  InsertButton insertButton = new InsertButton();
  ReloadButton reloadButton = new ReloadButton();
  DeleteButton deleteButton = new DeleteButton();
  NavigatorBar navigatorBar = new NavigatorBar();
  GridControl grid = new GridControl();

  EditButton editButton = new EditButton();
  SaveButton saveButton = new SaveButton();

  /** grid data locator */
  private ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();

  TextColumn colVatRegister = new TextColumn();
  TextColumn colDescr = new TextColumn();
  ComboColumn colRegType = new ComboColumn();

  LabelControl labelCompany = new LabelControl();
  ComboBoxControl controlCompaniesCombo = new ComboBoxControl();


  ExportButton exportButton = new ExportButton();
  GridBagLayout gridBagLayout1 = new GridBagLayout();

  CodLookupColumn colAccountCodeLookup = new CodLookupColumn();
  TextColumn colAccountDescr = new TextColumn();

  /** account code lookup controller */
  LookupController accountController = new LookupController();

  /** account code lookup data locator */
  LookupServerDataLocator accountDataLocator = new LookupServerDataLocator();


  public VatRegistersGridFrame(GridController controller) {
    grid.setController(controller);
    grid.setGridDataLocator(gridDataLocator);
    gridDataLocator.setServerMethodName("loadVatRegisters");
    try {
      jbInit();
      setSize(750,300);
      setMinimumSize(new Dimension(750,300));

      // set domain in combo box...
      ClientApplet applet = ( (ApplicationClientFacade) MDIFrame.getInstance().getClientFacade()).getMainClass();
      ButtonCompanyAuthorizations bca = applet.getAuthorizations().getCompanyBa();
      ArrayList companiesList = bca.getCompaniesList("ACC04");
      Domain domain = new Domain("DOMAIN_ACC04");
      for (int i = 0; i < companiesList.size(); i++) {
        if (applet.getAuthorizations().getCompanyBa().isInsertEnabled(
            "ACC04",companiesList.get(i).toString()
        ))
          domain.addDomainPair(companiesList.get(i),companiesList.get(i).toString());
      }
      controlCompaniesCombo.setDomain(domain);
      controlCompaniesCombo.addItemListener(new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
          if (e.getStateChange()==e.SELECTED) {
            grid.getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
          }
        }
      });
      try {
        controlCompaniesCombo.getComboBox().setSelectedIndex(0);
      }
      catch (Exception ex) {
      }


      // account code lookup...
      colAccountCodeLookup.setLookupController(accountController);
      colAccountCodeLookup.setControllerMethodName("getAccounts");
      accountController.setLookupDataLocator(accountDataLocator);
      accountDataLocator.setGridMethodName("loadAccounts");
      accountDataLocator.setValidationMethodName("validateAccountCode");
      accountController.setFrameTitle("accounts");
      accountController.setAllowTreeLeafSelectionOnly(false);
      accountController.setLookupValueObjectClassName("org.jallinone.accounting.accounts.java.AccountVO");
      accountController.addLookup2ParentLink("accountCodeACC02", "accountCodeAcc02ACC04");
      accountController.addLookup2ParentLink("descriptionSYS10","accountDescriptionACC04");
      accountController.setFramePreferedSize(new Dimension(400,400));
      accountController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) { }

        public void beforeLookupAction(ValueObject parentVO) {
          accountDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
          accountDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
        }

        public void forceValidate() {}

      });





      CustomizedColumns cust = new CustomizedColumns(ApplicationConsts.ID_VAT_REGISTERS,grid);

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  public final void reloadData() {
    grid.reloadData();
  }


  private void jbInit() throws Exception {
    labelCompany.setText("companyCode");
    grid.setMaxNumberOfRowsOnInsert(50);
    grid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    saveButton.setExecuteAsThread(true);
    grid.setValueObjectClassName("org.jallinone.accounting.vatregisters.java.VatRegisterVO");
    this.setTitle(ClientSettings.getInstance().getResources().getResource("vat register"));
    buttonsPanel.setLayout(gridBagLayout1);
    grid.setDeleteButton(deleteButton);
    grid.setEditButton(editButton);
    grid.setExportButton(exportButton);
    grid.setFunctionId("ACC04");
    grid.setMaxSortedColumns(3);
    grid.setInsertButton(insertButton);
    grid.setNavBar(navigatorBar);
    grid.setReloadButton(reloadButton);
    grid.setSaveButton(saveButton);
    colVatRegister.setMaxCharacters(20);
    colVatRegister.setTrimText(true);
    colVatRegister.setUpperCase(true);
    colVatRegister.setColumnFilterable(true);
    colVatRegister.setColumnName("registerCodeACC04");
    colVatRegister.setColumnSortable(true);
    colVatRegister.setEditableOnInsert(true);
    colVatRegister.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colVatRegister.setSortingOrder(1);
    colVatRegister.setPreferredWidth(80);
    colDescr.setColumnFilterable(false);
    colDescr.setColumnName("descriptionSYS10");
    colDescr.setColumnSortable(true);
    colDescr.setEditableOnEdit(true);
    colDescr.setEditableOnInsert(true);
    colDescr.setPreferredWidth(240);

    colRegType.setColumnFilterable(true);
    colRegType.setColumnName("registerTypeACC04");
    colRegType.setColumnSortable(true);
    colRegType.setEditableOnEdit(false);
    colRegType.setEditableOnInsert(true);
    colRegType.setPreferredWidth(120);
    colRegType.setDomainId("REGISTER_TYPE_ACC04");

    colAccountCodeLookup.setColumnFilterable(true);
    colAccountCodeLookup.setColumnName("accountCodeAcc02ACC04");
    colAccountCodeLookup.setColumnSortable(true);
    colAccountCodeLookup.setEditableOnEdit(false);
    colAccountCodeLookup.setEditableOnInsert(true);
    colAccountCodeLookup.setPreferredWidth(80);

    colAccountDescr.setColumnFilterable(false);
    colAccountDescr.setColumnName("accountDescriptionACC04");
    colAccountDescr.setColumnSortable(false);
    colAccountDescr.setEditableOnEdit(false);
    colAccountDescr.setEditableOnInsert(false);
    colAccountDescr.setPreferredWidth(200);

    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(insertButton,  new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    buttonsPanel.add(editButton,  new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    buttonsPanel.add(saveButton,  new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    buttonsPanel.add(reloadButton,  new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    buttonsPanel.add(deleteButton,  new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    buttonsPanel.add(exportButton,  new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    buttonsPanel.add(navigatorBar,  new GridBagConstraints(6, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 0), 0, 0));
    buttonsPanel.add(labelCompany,   new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 10, 5), 0, 0));
    buttonsPanel.add(controlCompaniesCombo,     new GridBagConstraints(3, 0, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 10, 5), 0, 0));

    this.getContentPane().add(grid, BorderLayout.CENTER);
    grid.getColumnContainer().add(colVatRegister, null);
    grid.getColumnContainer().add(colDescr, null);
    grid.getColumnContainer().add(colRegType, null);
    grid.getColumnContainer().add(colAccountCodeLookup, null);
    grid.getColumnContainer().add(colAccountDescr, null);
  }


  public ComboBoxControl getControlCompaniesCombo() {
    return controlCompaniesCombo;
  }

}
