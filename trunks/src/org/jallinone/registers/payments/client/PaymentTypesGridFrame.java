package org.jallinone.registers.payments.client;

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
import java.math.BigDecimal;
import org.jallinone.commons.client.*;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the payment types grid frame.</p>
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
public class PaymentTypesGridFrame extends InternalFrame {

  JPanel buttonsPanel = new JPanel();
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
  TextColumn colPaymentType = new TextColumn();
  TextColumn colDescr = new TextColumn();
  ExportButton exportButton = new ExportButton();
  CompaniesComboColumn colCompany = new CompaniesComboColumn();
  CodLookupColumn colAccCode = new CodLookupColumn();
  TextColumn colAccDescr = new TextColumn();
	LookupController accController = new LookupController();
	LookupServerDataLocator accDataLocator = new LookupServerDataLocator();


  public PaymentTypesGridFrame(GridController controller) {
    grid.setController(controller);
    grid.setGridDataLocator(gridDataLocator);
    gridDataLocator.setServerMethodName("loadPaymentTypes");
    try {
      jbInit();
      setSize(750,300);
      setMinimumSize(new Dimension(750,300));

      CustomizedColumns cust = new CustomizedColumns(new BigDecimal(222),grid);

			// account lookup...
			accDataLocator.setGridMethodName("loadAccounts");
			accDataLocator.setValidationMethodName("validateAccountCode");

			colAccCode.setLookupController(accController);
			accController.setLookupDataLocator(accDataLocator);
			accController.setFrameTitle("accounts");
			accController.setLookupValueObjectClassName("org.jallinone.accounting.accounts.java.AccountVO");
			accController.addLookup2ParentLink("accountCodeACC02", "accountCodeAcc02REG11");
			accController.addLookup2ParentLink("descriptionSYS10","acc02DescriptionSYS10");
			accController.setAllColumnVisible(false);
			accController.setVisibleColumn("accountCodeACC02", true);
			accController.setVisibleColumn("descriptionSYS10", true);
			accController.setPreferredWidthColumn("descriptionSYS10", 200);
			accController.setFramePreferedSize(new Dimension(340,400));
			accController.setSortedColumn("accountCodeACC02","ASC",1);
			accController.setFilterableColumn("accountCodeACC02",true);
			accController.setFilterableColumn("descriptionSYS10",true);
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
    grid.setValueObjectClassName("org.jallinone.registers.payments.java.PaymentTypeVO");
    this.setTitle(ClientSettings.getInstance().getResources().getResource("payment types"));
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    grid.setDeleteButton(deleteButton);
    grid.setEditButton(editButton);
    grid.setExportButton(exportButton);
    grid.setFunctionId("REG11");
    grid.setMaxSortedColumns(3);
    grid.setInsertButton(insertButton);
    grid.setNavBar(navigatorBar);
    grid.setReloadButton(reloadButton);
    grid.setSaveButton(saveButton);
    colPaymentType.setMaxCharacters(20);
    colPaymentType.setTrimText(true);
    colPaymentType.setUpperCase(true);
    colPaymentType.setColumnFilterable(true);
    colPaymentType.setColumnName("paymentTypeCodeREG11");
    colPaymentType.setColumnSortable(true);
    colPaymentType.setEditableOnInsert(true);
    colPaymentType.setPreferredWidth(130);
    colPaymentType.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colPaymentType.setSortingOrder(1);
    colDescr.setColumnFilterable(false);
    colDescr.setColumnName("descriptionSYS10");
    colDescr.setColumnSortable(true);
    colDescr.setEditableOnEdit(true);
    colDescr.setEditableOnInsert(true);
    colDescr.setHeaderColumnName("paymentTypeDescription");
    colDescr.setPreferredWidth(230);
    colCompany.setColumnName("companyCodeSys01REG11");
    colCompany.setEditableOnInsert(true);
    colCompany.setHeaderColumnName("companyCode");
    colCompany.setPreferredWidth(90);
    colCompany.setFunctionCode("REG11");
    colAccCode.setColumnName("accountCodeAcc02REG11");
    colAccCode.setEditableOnEdit(true);
    colAccCode.setEditableOnInsert(true);
    colAccCode.setHeaderColumnName("accountCodeACC02");
    colAccCode.setPreferredWidth(80);
    colAccDescr.setColumnName("acc02DescriptionSYS10");
    colAccDescr.setHeaderColumnName("accountDescriptionACC06");
    colAccDescr.setPreferredWidth(180);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(saveButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(deleteButton, null);
    buttonsPanel.add(exportButton, null);
    buttonsPanel.add(navigatorBar, null);
    this.getContentPane().add(grid, BorderLayout.CENTER);
    grid.getColumnContainer().add(colCompany, null);
    grid.getColumnContainer().add(colPaymentType, null);
    grid.getColumnContainer().add(colDescr, null);
    grid.getColumnContainer().add(colAccCode, null);
    grid.getColumnContainer().add(colAccDescr, null);
  }

}
