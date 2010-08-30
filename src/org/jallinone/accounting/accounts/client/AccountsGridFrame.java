package org.jallinone.accounting.accounts.client;

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
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.form.client.Form;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.openswing.swing.lookup.client.LookupListener;
import org.openswing.swing.message.receive.java.*;
import java.util.Collection;
import org.jallinone.commons.client.ClientApplet;
import org.jallinone.commons.client.ApplicationClientFacade;
import org.jallinone.system.java.ButtonCompanyAuthorizations;
import java.util.ArrayList;
import org.openswing.swing.domains.java.Domain;
import org.jallinone.accounting.ledger.java.LedgerVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the accounts grid frame.</p>
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
public class AccountsGridFrame extends InternalFrame {

  JPanel buttonsPanel = new JPanel();
  JPanel topPanel = new JPanel();
  Form filterPanel = new Form();
  BorderLayout borderLayout1 = new BorderLayout();
  FlowLayout flowLayout1 = new FlowLayout();
  InsertButton insertButton = new InsertButton();
  ReloadButton reloadButton = new ReloadButton();
  DeleteButton deleteButton = new DeleteButton();
  NavigatorBar navigatorBar = new NavigatorBar();
  GridControl grid = new GridControl();

  EditButton editButton = new EditButton();
  SaveButton saveButton = new SaveButton();

  /** grid data locator */
  private ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();
  TextColumn colAccount = new TextColumn();
  TextColumn colDescr = new TextColumn();
  ComboColumn colAccountType = new ComboColumn();
  TextColumn colLedgerCode = new TextColumn();
  TextColumn colLedgerDescr = new TextColumn();

  ExportButton exportButton = new ExportButton();
  LabelControl labelLedger = new LabelControl();
  CodLookupControl controlLedgerCode = new CodLookupControl();
  TextControl controlLedgerDescr = new TextControl();

  /** ledger code lookup controller */
  LookupController ledgerController = new LookupController();

  /** ledger code lookup data locator */
  LookupServerDataLocator ledgerDataLocator = new LookupServerDataLocator();

  LabelControl labelCompany = new LabelControl();
  ComboBoxControl controlCompaniesCombo = new ComboBoxControl();
  GridBagLayout gridBagLayout1 = new GridBagLayout();


  public AccountsGridFrame(GridController controller) {
    grid.setController(controller);
    grid.setGridDataLocator(gridDataLocator);
    gridDataLocator.setServerMethodName("loadAccounts");
    try {
      jbInit();
      setSize(750,400);
      setMinimumSize(new Dimension(750,400));

      // set domain in combo box...
      ClientApplet applet = ( (ApplicationClientFacade) MDIFrame.getInstance().getClientFacade()).getMainClass();
      ButtonCompanyAuthorizations bca = applet.getAuthorizations().getCompanyBa();
      ArrayList companiesList = bca.getCompaniesList("ACC01");
      Domain domain = new Domain("DOMAIN_ACC01");
      for (int i = 0; i < companiesList.size(); i++) {
        if (applet.getAuthorizations().getCompanyBa().isInsertEnabled(
            "ACC01",companiesList.get(i).toString()
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

      // ledger code lookup...
      controlLedgerCode.setLookupController(ledgerController);
      controlLedgerCode.setControllerMethodName("getLedger");
      ledgerController.setLookupDataLocator(ledgerDataLocator);
      ledgerDataLocator.setGridMethodName("loadLedger");
      ledgerDataLocator.setValidationMethodName("validateLedgerCode");
      ledgerController.setFrameTitle("ledger");
      ledgerController.setLookupValueObjectClassName("org.jallinone.accounting.ledger.java.LedgerVO");
      ledgerController.addLookup2ParentLink("ledgerCodeACC01", "ledgerCodeACC01");
      ledgerController.addLookup2ParentLink("descriptionSYS10","descriptionSYS10");
      ledgerController.setFramePreferedSize(new Dimension(400,400));
      ledgerController.setAllColumnVisible(false);
      ledgerController.setVisibleColumn("ledgerCodeACC01",true);
      ledgerController.setVisibleColumn("descriptionSYS10",true);
      ledgerController.setPreferredWidthColumn("descriptionSYS10",290);
      ledgerController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          LedgerVO vo = (LedgerVO)parentVO;
          grid.getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());

          if (vo.getLedgerCodeACC01()!=null && !vo.getLedgerCodeACC01().equals(""))
            grid.getOtherGridParams().put(ApplicationConsts.LEDGER_CODE,vo.getLedgerCodeACC01());
          else
            grid.getOtherGridParams().remove(ApplicationConsts.LEDGER_CODE);
          grid.reloadData();
        }

        public void beforeLookupAction(ValueObject parentVO) {
          ledgerDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
          ledgerDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,controlCompaniesCombo.getValue());
        }

        public void forceValidate() {}

      });



      CustomizedColumns cust = new CustomizedColumns(ApplicationConsts.ID_ACCOUNTS,grid);

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  public final void reloadData() {
    grid.reloadData();
  }


  private void jbInit() throws Exception {
    grid.setMaxNumberOfRowsOnInsert(50);
    filterPanel.setVOClassName("org.jallinone.accounting.ledger.java.LedgerVO");
    labelCompany.setText("companyCode");

    topPanel.setLayout(borderLayout1);
    labelLedger.setText("ledgerCodeACC01");
    filterPanel.setLayout(gridBagLayout1);
    controlLedgerDescr.setColumns(30);
    controlLedgerDescr.setEnabled(false);
    controlLedgerCode.setLinkLabel(labelLedger);
    controlLedgerCode.setAttributeName("ledgerCodeACC01");
    controlLedgerCode.setMaxCharacters(20);
    controlLedgerDescr.setAttributeName("descriptionSYS10");
    topPanel.add(filterPanel,BorderLayout.NORTH);

    filterPanel.add(labelCompany,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlCompaniesCombo,      new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 60, 0));

    filterPanel.add(labelLedger,    new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    topPanel.add(buttonsPanel,BorderLayout.SOUTH);

    grid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    saveButton.setExecuteAsThread(true);
    grid.setValueObjectClassName("org.jallinone.accounting.accounts.java.AccountVO");
    this.setTitle(ClientSettings.getInstance().getResources().getResource("accounts"));
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    grid.setDeleteButton(deleteButton);
    grid.setEditButton(editButton);
    grid.setExportButton(exportButton);
    grid.setFunctionId("ACC02");
    grid.setMaxSortedColumns(3);
    grid.setInsertButton(insertButton);
    grid.setNavBar(navigatorBar);
    grid.setReloadButton(reloadButton);
    grid.setSaveButton(saveButton);
    colAccount.setMaxCharacters(20);
    colAccount.setTrimText(true);
    colAccount.setUpperCase(true);
    colAccount.setColumnFilterable(true);
    colAccount.setColumnName("accountCodeACC02");
    colAccount.setColumnSortable(true);
    colAccount.setEditableOnInsert(true);
    colAccount.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colAccount.setSortingOrder(2);
    colDescr.setColumnFilterable(false);
    colDescr.setColumnName("descriptionSYS10");
    colDescr.setColumnSortable(true);
    colDescr.setEditableOnEdit(true);
    colDescr.setEditableOnInsert(true);
    colDescr.setPreferredWidth(220);

    colAccountType.setColumnFilterable(true);
    colAccountType.setColumnName("accountTypeACC02");
    colAccountType.setColumnSortable(true);
    colAccountType.setEditableOnEdit(false);
    colAccountType.setEditableOnInsert(true);
    colAccountType.setPreferredWidth(120);
    colAccountType.setDomainId("ACCOUNT_TYPE_ACC02");

    colLedgerCode.setColumnFilterable(true);
    colLedgerCode.setColumnName("ledgerCodeAcc01ACC02");
    colLedgerCode.setColumnSortable(true);
    colLedgerCode.setEditableOnEdit(false);
    colLedgerCode.setEditableOnInsert(false);
    colLedgerCode.setPreferredWidth(80);
    colLedgerCode.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colLedgerCode.setSortingOrder(1);

    colLedgerDescr.setColumnFilterable(false);
    colLedgerDescr.setColumnName("ledgerDescriptionACC02");
    colLedgerDescr.setColumnSortable(false);
    colLedgerDescr.setEditableOnEdit(false);
    colLedgerDescr.setEditableOnInsert(false);
    colLedgerDescr.setPreferredWidth(200);

    this.getContentPane().add(topPanel, BorderLayout.NORTH);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(saveButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(deleteButton, null);
    buttonsPanel.add(exportButton, null);
    buttonsPanel.add(navigatorBar, null);
    this.getContentPane().add(grid, BorderLayout.CENTER);
    grid.getColumnContainer().add(colAccount, null);
    grid.getColumnContainer().add(colDescr, null);
    grid.getColumnContainer().add(colAccountType, null);
    grid.getColumnContainer().add(colLedgerCode, null);
    grid.getColumnContainer().add(colLedgerDescr, null);

    filterPanel.add(controlLedgerCode,     new GridBagConstraints(1, 1, 1, 1, 0.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlLedgerDescr,    new GridBagConstraints(2, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
  }


  public ComboBoxControl getControlCompaniesCombo() {
    return controlCompaniesCombo;
  }


  public CodLookupControl getControlLedgerCode() {
    return controlLedgerCode;
  }
  public TextControl getControlLedgerDescr() {
    return controlLedgerDescr;
  }

}
