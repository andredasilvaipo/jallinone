package org.jallinone.purchases.pricelist.client;

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
import java.awt.event.*;
import javax.swing.border.*;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.domains.java.Domain;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.items.java.ItemTypeVO;
import java.util.ArrayList;
import org.jallinone.purchases.pricelist.java.SupplierPriceVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.purchases.pricelist.java.SupplierPricelistVO;
import java.util.Date;
import org.jallinone.purchases.pricelist.java.SupplierPricelistChanges;
import org.jallinone.purchases.items.java.SupplierItemVO;
import org.jallinone.purchases.suppliers.java.DetailSupplierVO;
import org.openswing.swing.lookup.client.LookupListener;
import java.util.Collection;
import org.jallinone.items.client.ItemFrame;
import org.jallinone.items.java.DetailItemVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Panel containing suppliers item prices grid, for a specified item.</p>
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
public class SupplierItemPricesPanel extends JPanel {

  JPanel pricesPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();

  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JPanel pricesButtonsPanel = new JPanel();
  GridControl pricesGrid = new GridControl();
  FlowLayout flowLayout2 = new FlowLayout();
  TitledBorder titledBorder1;
  TitledBorder titledBorder2;
  InsertButton insertButton1 = new InsertButton();
  EditButton editButton1 = new EditButton();
  SaveButton saveButton1 = new SaveButton();
  DeleteButton deleteButton1 = new DeleteButton();
  ReloadButton reloadButton1 = new ReloadButton();
  ExportButton exportButton1 = new ExportButton();
  CopyButton copyButton1 = new CopyButton();
  NavigatorBar navigatorBar1 = new NavigatorBar();
  CodLookupColumn colSupplierCode = new CodLookupColumn();
  TextColumn colSupplierDescr = new TextColumn();
  DecimalColumn colValue = new DecimalColumn();
  DateColumn colStartDate = new DateColumn();
  DateColumn colEndDate = new DateColumn();
  CodLookupColumn colPricelistCode = new CodLookupColumn();
  TextColumn colPricelistDescr = new TextColumn();

  LookupController pricelistController = new LookupController();
  LookupServerDataLocator pricelistDataLocator = new LookupServerDataLocator();
  LookupController supplierController = new LookupController();
  LookupServerDataLocator supplierDataLocator = new LookupServerDataLocator();


  /** prices grid data locator */
  private ServerGridDataLocator pricesGridDataLocator = new ServerGridDataLocator();

  CopyButton copyButton2 = new CopyButton();

  private ItemFrame frame = null;


  public SupplierItemPricesPanel(ItemFrame frame) {
    this.frame = frame;
    pricesGrid.setController(new SupplierItemPricesController(this));
    pricesGrid.setGridDataLocator(pricesGridDataLocator);
    pricesGridDataLocator.setServerMethodName("loadSupplierPrices");

    try {
      jbInit();

      // supplier lookup...
      supplierDataLocator.setGridMethodName("loadSuppliers");
      supplierDataLocator.setValidationMethodName("validateSupplierCode");
      colSupplierCode.setLookupController(supplierController);
      colSupplierCode.setControllerMethodName("getSuppliersList");
      supplierController.setLookupDataLocator(supplierDataLocator);
      supplierController.setFrameTitle("suppliers");
      supplierController.setLookupValueObjectClassName("org.jallinone.purchases.suppliers.java.GridSupplierVO");
      supplierController.addLookup2ParentLink("supplierCodePUR01", "supplierCodePUR01");
      supplierController.addLookup2ParentLink("progressiveREG04", "progressiveReg04PUR04");
      supplierController.addLookup2ParentLink("supplierDescription", "supplierDescription");
      supplierController.setAllColumnVisible(false);
      supplierController.setVisibleColumn("supplierCodePUR01", true);
      supplierController.setVisibleColumn("name_1REG04", true);
      supplierController.setVisibleColumn("name_2REG04", true);
      supplierController.setFramePreferedSize(new Dimension(350,500));
      supplierController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {}

        public void beforeLookupAction(ValueObject parentVO) {
          DetailItemVO itemVO = (DetailItemVO)SupplierItemPricesPanel.this.frame.getFormPanel().getVOModel().getValueObject();
          supplierDataLocator.getLookupFrameParams().put(ApplicationConsts.PROGRESSIVE_HIE02,itemVO.getCompanyCodeSys01ITM01());
          supplierDataLocator.getLookupValidationParameters().put(ApplicationConsts.PROGRESSIVE_HIE02,itemVO.getCompanyCodeSys01ITM01());
        }

        public void forceValidate() {}

      });


      // pricelist lookup...
      pricelistDataLocator.setGridMethodName("loadSupplierPricelists");
      pricelistDataLocator.setValidationMethodName("validateSupplierPricelistCode");
      colPricelistCode.setLookupController(pricelistController);
      pricelistController.setLookupDataLocator(pricelistDataLocator);
      pricelistController.setFrameTitle("supplier pricelists");
      pricelistController.setLookupValueObjectClassName("org.jallinone.purchases.pricelist.java.SupplierPricelistVO");
      pricelistController.addLookup2ParentLink("pricelistCodePUR03", "pricelistCodePur03PUR04");
      pricelistController.addLookup2ParentLink("descriptionSYS10", "pricelistDescriptionSYS10");
      pricelistController.setAllColumnVisible(false);
      pricelistController.setVisibleColumn("pricelistCodePUR03", true);
      pricelistController.setVisibleColumn("descriptionSYS10", true);
      pricelistController.setFramePreferedSize(new Dimension(350,500));
      pricelistController.setPreferredWidthColumn("descriptionSYS10",200);
      pricelistController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {}

        public void beforeLookupAction(ValueObject parentVO) {
          SupplierPriceVO priceVO = (SupplierPriceVO)parentVO;
          pricelistDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,priceVO.getCompanyCodeSys01PUR04());
          pricelistDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,priceVO.getCompanyCodeSys01PUR04());
          pricelistDataLocator.getLookupFrameParams().put(ApplicationConsts.PROGRESSIVE_REG04,priceVO.getProgressiveReg04PUR04());
          pricelistDataLocator.getLookupValidationParameters().put(ApplicationConsts.PROGRESSIVE_REG04,priceVO.getProgressiveReg04PUR04());
        }

        public void forceValidate() {}

      });

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    pricesGrid.setVisibleStatusPanel(false);
    pricesGrid.setMaxNumberOfRowsOnInsert(50);
    pricesGrid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

    titledBorder1 = new TitledBorder("");
    titledBorder2 = new TitledBorder("");
    colPricelistCode.setMaxCharacters(20);
    colPricelistCode.setColumnFilterable(true);
    colPricelistCode.setColumnRequired(true);
    colPricelistCode.setColumnName("pricelistCodePur03PUR04");
    colPricelistCode.setColumnSortable(true);
    colPricelistCode.setEditableOnEdit(false);
    colPricelistCode.setEditableOnInsert(true);
    colPricelistCode.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colPricelistCode.setSortingOrder(1);
    colPricelistDescr.setColumnDuplicable(true);
    colPricelistDescr.setColumnFilterable(false);
    colPricelistDescr.setColumnName("pricelistDescriptionSYS10");
    colPricelistDescr.setColumnSortable(true);
    colPricelistDescr.setEditableOnEdit(false);
    colPricelistDescr.setEditableOnInsert(false);
    colPricelistDescr.setHeaderColumnName("pricelistDescription");
    colPricelistDescr.setPreferredWidth(250);

    colSupplierCode.setColumnDuplicable(true);
    colSupplierCode.setColumnFilterable(true);
    colSupplierCode.setColumnName("supplierCodePUR01");
    colSupplierCode.setColumnRequired(true);
    colSupplierCode.setColumnSortable(true);
    colSupplierCode.setEditableOnEdit(false);
    colSupplierCode.setEditableOnInsert(true);
    colSupplierCode.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colSupplierCode.setSortingOrder(0);
    colSupplierCode.setMaxCharacters(20);
    colSupplierDescr.setColumnDuplicable(true);
    colSupplierDescr.setColumnFilterable(false);
    colSupplierDescr.setColumnName("supplierDescription");
    colSupplierDescr.setColumnSortable(true);
    colSupplierDescr.setEditableOnEdit(false);
    colSupplierDescr.setEditableOnInsert(false);
    colSupplierDescr.setPreferredWidth(250);

    pricesPanel.setLayout(gridBagLayout1);
    pricesButtonsPanel.setLayout(flowLayout2);
    flowLayout2.setAlignment(FlowLayout.LEFT);
    pricesPanel.setBorder(titledBorder1);
    titledBorder1.setTitleColor(Color.blue);
    titledBorder1.setTitle(ClientSettings.getInstance().getResources().getResource("purchase item prices"));
    pricesGrid.setAutoLoadData(false);
    pricesGrid.setCopyButton(copyButton1);
    pricesGrid.setDeleteButton(deleteButton1);
    pricesGrid.setEditButton(editButton1);
    pricesGrid.setExportButton(exportButton1);
    pricesGrid.setFunctionId("PUR01");
    pricesGrid.setMaxSortedColumns(3);
    pricesGrid.setInsertButton(insertButton1);
    pricesGrid.setNavBar(navigatorBar1);
    pricesGrid.setReloadButton(reloadButton1);
    pricesGrid.setSaveButton(saveButton1);
    pricesGrid.setValueObjectClassName("org.jallinone.purchases.pricelist.java.SupplierPriceVO");
    colValue.setDecimals(5);
    colValue.setMinValue(0.0);
    colValue.setColumnDuplicable(true);
    colValue.setColumnFilterable(true);
    colValue.setEditableOnEdit(true);
    colValue.setEditableOnInsert(true);
    colValue.setColumnName("valuePUR04");
    colValue.setPreferredWidth(60);
    colStartDate.setColumnDuplicable(true);
    colStartDate.setColumnFilterable(true);
    colStartDate.setColumnName("startDatePUR04");
    colStartDate.setEditableOnEdit(true);
    colStartDate.setEditableOnInsert(true);
    colEndDate.setColumnDuplicable(true);
    colEndDate.setColumnFilterable(true);
    colEndDate.setColumnName("endDatePUR04");
    colEndDate.setEditableOnEdit(true);
    colEndDate.setEditableOnInsert(true);
    this.setLayout(new BorderLayout());
    this.add(pricesPanel,BorderLayout.CENTER);
    pricesPanel.add(pricesButtonsPanel,    new GridBagConstraints(0, 1, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    pricesButtonsPanel.add(insertButton1, null);
    pricesButtonsPanel.add(copyButton1, null);
    pricesButtonsPanel.add(editButton1, null);
    pricesButtonsPanel.add(saveButton1, null);
    pricesButtonsPanel.add(reloadButton1, null);
    pricesButtonsPanel.add(deleteButton1, null);
    pricesButtonsPanel.add(exportButton1, null);
    pricesButtonsPanel.add(navigatorBar1, null);

    pricesPanel.add(pricesGrid,   new GridBagConstraints(0, 2, 2, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    pricesGrid.getColumnContainer().add(colValue, null);
    pricesGrid.getColumnContainer().add(colStartDate, null);
    pricesGrid.getColumnContainer().add(colEndDate, null);
    pricesGrid.getColumnContainer().add(colSupplierCode, null);
    pricesGrid.getColumnContainer().add(colSupplierDescr, null);
    pricesGrid.getColumnContainer().add(colPricelistCode, null);
    pricesGrid.getColumnContainer().add(colPricelistDescr, null);
  }


  public GridControl getPricesGrid() {
    return pricesGrid;
  }


  public final void setButtonsEnabled(boolean enabled) {
    insertButton1.setEnabled(enabled);
    copyButton1.setEnabled(enabled);
    editButton1.setEnabled(enabled);
    deleteButton1.setEnabled(enabled);
    exportButton1.setEnabled(enabled);
  }


  public ItemFrame getFrame() {
    return frame;
  }



}

