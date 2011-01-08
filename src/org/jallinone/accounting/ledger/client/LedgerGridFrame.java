package org.jallinone.accounting.ledger.client;

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


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the ledger grid frame.</p>
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
public class LedgerGridFrame extends InternalFrame {

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
  TextColumn colLedger = new TextColumn();
  TextColumn colDescr = new TextColumn();
  ExportButton exportButton = new ExportButton();
  ComboColumn colAccountType = new ComboColumn();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  LabelControl labelCompany = new LabelControl();
  ComboBoxControl controlCompaniesCombo = new ComboBoxControl();


  public LedgerGridFrame(GridController controller) {
    grid.setController(controller);
    grid.setGridDataLocator(gridDataLocator);
    gridDataLocator.setServerMethodName("loadLedger");
    try {
      jbInit();
      setSize(600,500);
      setMinimumSize(new Dimension(600,500));

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

      CustomizedColumns cust = new CustomizedColumns(ApplicationConsts.ID_LEDGER,grid);

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
    grid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    saveButton.setExecuteAsThread(true);
    grid.setValueObjectClassName("org.jallinone.accounting.ledger.java.LedgerVO");
    this.setTitle(ClientSettings.getInstance().getResources().getResource("ledger"));
    buttonsPanel.setLayout(gridBagLayout1);
    grid.setDeleteButton(deleteButton);
    grid.setEditButton(editButton);
    grid.setExportButton(exportButton);
    grid.setFunctionId("ACC01");
    grid.setMaxSortedColumns(3);
    grid.setInsertButton(insertButton);
    grid.setNavBar(navigatorBar);
    grid.setReloadButton(reloadButton);
    grid.setSaveButton(saveButton);
    colLedger.setMaxCharacters(20);
    colLedger.setTrimText(true);
    colLedger.setUpperCase(true);
    colLedger.setColumnFilterable(true);
    colLedger.setColumnName("ledgerCodeACC01");
    colLedger.setColumnSortable(true);
    colLedger.setEditableOnInsert(true);
    colLedger.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colLedger.setSortingOrder(1);
    colDescr.setColumnFilterable(false);
    colDescr.setColumnName("descriptionSYS10");
    colDescr.setColumnSortable(true);
    colDescr.setEditableOnEdit(true);
    colDescr.setEditableOnInsert(true);
    colDescr.setPreferredWidth(290);

    colAccountType.setColumnFilterable(true);
    colAccountType.setColumnName("accountTypeACC01");
    colAccountType.setDomainId("ACCOUNT_TYPE_ACC01");
    colAccountType.setColumnSortable(true);
    colAccountType.setEditableOnEdit(false);
    colAccountType.setEditableOnInsert(true);
    colAccountType.setPreferredWidth(180);

    labelCompany.setText("companyCode");
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(insertButton,    new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    buttonsPanel.add(editButton,    new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    buttonsPanel.add(saveButton,    new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    buttonsPanel.add(reloadButton,    new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    buttonsPanel.add(deleteButton,    new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    buttonsPanel.add(exportButton,    new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    buttonsPanel.add(navigatorBar,    new GridBagConstraints(6, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 0), 0, 0));
    buttonsPanel.add(labelCompany,   new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 10, 5), 0, 0));
    buttonsPanel.add(controlCompaniesCombo,     new GridBagConstraints(3, 0, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 10, 5), 0, 0));

    this.getContentPane().add(grid, BorderLayout.CENTER);
    grid.getColumnContainer().add(colLedger, null);
    grid.getColumnContainer().add(colDescr, null);
    grid.getColumnContainer().add(colAccountType, null);
  }


  public ComboBoxControl getControlCompaniesCombo() {
    return controlCompaniesCombo;
  }

}
