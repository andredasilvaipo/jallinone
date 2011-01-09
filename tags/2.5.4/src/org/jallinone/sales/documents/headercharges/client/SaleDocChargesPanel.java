package org.jallinone.sales.documents.headercharges.client;

import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.form.client.FormController;
import org.openswing.swing.message.receive.java.*;
import org.jallinone.warehouse.java.WarehousePK;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.warehouse.java.WarehouseVO;
import org.jallinone.commons.client.CompanyFormController;
import org.openswing.swing.form.client.Form;
import org.jallinone.commons.client.ClientApplet;
import org.jallinone.commons.client.ApplicationClientFacade;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.util.client.ClientSettings;
import java.awt.*;
import javax.swing.*;
import org.openswing.swing.client.*;
import javax.swing.border.*;
import org.openswing.swing.table.columns.client.*;
import java.awt.event.*;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.openswing.swing.tree.client.TreeServerDataLocator;
import org.openswing.swing.domains.java.Domain;
import java.util.ArrayList;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.lookup.client.LookupListener;
import java.util.Collection;
import org.openswing.swing.table.client.GridController;
import org.openswing.swing.table.java.ServerGridDataLocator;
import org.jallinone.sales.documents.headercharges.java.*;
import org.jallinone.sales.documents.java.SaleDocPK;
import org.jallinone.sales.documents.java.DetailSaleDocVO;
import org.jallinone.sales.documents.client.SaleOrderDocRowsGridPanel;
import org.jallinone.sales.documents.client.SaleOrderDocFrame;
import org.jallinone.sales.charges.java.ChargeVO;
import java.util.HashSet;
import org.jallinone.sales.documents.client.SaleDocument;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Panel that contains the list of charges applied to the sale document.
 * Charges can be custoemr charges or customer hierarchy charges.</p>
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
public class SaleDocChargesPanel extends JPanel implements CurrencyColumnSettings {

  GridBagLayout borderLayout2 = new GridBagLayout();
  JPanel topPanel = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  GridControl grid = new GridControl();
  TitledBorder titledBorder2;
  JPanel buttonsPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  ReloadButton reloadButton = new ReloadButton();
  ExportButton exportButton = new ExportButton();
  NavigatorBar navigatorBar = new NavigatorBar();

  /** charge code lookup data locator */
  LookupServerDataLocator chargeDataLocator = new LookupServerDataLocator();

  /** charge code lookup controller */
  LookupController chargeController = new LookupController();

  /** grid data locator */
  ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();

  BorderLayout borderLayout1 = new BorderLayout();
  InsertButton insertButton1 = new InsertButton();
  EditButton editButton1 = new EditButton();
  SaveButton saveButton1 = new SaveButton();
  DeleteButton deleteButton1 = new DeleteButton();
  CodLookupColumn colChargeCode = new CodLookupColumn();
  TextColumn colChargeDescr = new TextColumn();
  CurrencyColumn colValue = new CurrencyColumn();
  CurrencyColumn colInvoicedValue = new CurrencyColumn();
  CurrencyColumn colValueSal06 = new CurrencyColumn();
  DecimalColumn colPerc = new DecimalColumn();
  DecimalColumn colPercSal06 = new DecimalColumn();

  /** header v.o. */
  private DetailSaleDocVO parentVO = null;

  /** parent frame */
  private SaleDocument parentFrame = null;

  /** used to enable insert/edit of item rows and item discounts */
  private boolean enabledInsertEdit;


  public SaleDocChargesPanel(SaleDocument parentFrame,boolean enabledInsertEdit) {
    this.parentFrame = parentFrame;
    this.enabledInsertEdit = enabledInsertEdit;
    try {
      jbInit();

      // set buttons disabilitation...
      init();

      grid.setController(new SaleDocChargesController(this));
      grid.setGridDataLocator(gridDataLocator);
      gridDataLocator.setServerMethodName("loadSaleDocCharges");

      // charge code lookup...
      chargeDataLocator.setGridMethodName("loadCharges");
      chargeDataLocator.setValidationMethodName("validateChargeCode");

      colChargeCode.setLookupController(chargeController);
      colChargeCode.setControllerMethodName("getSaleChargesList");
      chargeController.setLookupDataLocator(chargeDataLocator);
      chargeController.setFrameTitle("charges");

      chargeController.setLookupValueObjectClassName("org.jallinone.sales.charges.java.ChargeVO");
      chargeController.addLookup2ParentLink("chargeCodeSAL06", "chargeCodeSal06DOC03");
      chargeController.addLookup2ParentLink("descriptionSYS10", "chargeDescriptionDOC03");
      chargeController.addLookup2ParentLink("valueSAL06", "valueSal06DOC03");
      chargeController.addLookup2ParentLink("percSAL06", "percSal06DOC03");
      chargeController.addLookup2ParentLink("vatCodeReg01SAL06", "vatCodeSal06DOC03");
      chargeController.addLookup2ParentLink("vatDescriptionSYS10", "vatDescriptionDOC03");
      chargeController.addLookup2ParentLink("vatValueREG01", "valueReg01DOC03");
      chargeController.addLookup2ParentLink("vatDeductibleREG01", "vatDeductibleDOC03");

      chargeController.setAllColumnVisible(false);
      chargeController.setVisibleColumn("chargeCodeSAL06", true);
      chargeController.setVisibleColumn("descriptionSYS10", true);
      chargeController.setVisibleColumn("valueSAL06", true);
      chargeController.setVisibleColumn("percSAL06", true);
      chargeController.setVisibleColumn("vatCodeReg01SAL06", true);
      chargeController.setVisibleColumn("vatDescriptionSYS10", true);
      chargeController.setVisibleColumn("vatValueREG01", true);
      chargeController.setVisibleColumn("vatDeductibleREG01", true);
      chargeController.setPreferredWidthColumn("chargeCodeSAL06", 80);
      chargeController.setPreferredWidthColumn("valueSAL06", 60);
      chargeController.setPreferredWidthColumn("percSAL06", 60);
      chargeController.setPreferredWidthColumn("descriptionSYS10", 200);
      chargeController.setPreferredWidthColumn("vatCodeReg01SAL06", 80);
      chargeController.setPreferredWidthColumn("vatDescriptionSYS10", 150);
      chargeController.setPreferredWidthColumn("vatValueREG01", 60);
      chargeController.setPreferredWidthColumn("vatDeductibleREG01", 60);
      chargeController.setFramePreferedSize(new Dimension(650,500));
      chargeController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          SaleDocChargeVO vo = (SaleDocChargeVO)parentVO;
          if (vo.getChargeCodeSal06DOC03()==null || vo.getChargeCodeSal06DOC03().equals("")) {
            vo.setValueDOC03(null);
            vo.setPercDOC03(null);
          }
          else {
            ChargeVO lookupVO = (ChargeVO)chargeController.getLookupVO();
            vo.setValueDOC03(lookupVO.getValueSAL06());
            vo.setPercDOC03(lookupVO.getPercSAL06());
          }
        }

        public void beforeLookupAction(ValueObject parentVO) {
          chargeDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,SaleDocChargesPanel.this.parentVO.getCompanyCodeSys01DOC01());
          chargeDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,SaleDocChargesPanel.this.parentVO.getCompanyCodeSys01DOC01());
        }

        public void forceValidate() {}

      });


      colValue.setDynamicSettings(this);
      colInvoicedValue.setDynamicSettings(this);
      colValueSal06.setDynamicSettings(this);

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * Set buttons disabilitation.
   */
  private void init() {
    HashSet buttonsToDisable = new HashSet();
    buttonsToDisable.add(insertButton1);
    buttonsToDisable.add(editButton1);
    buttonsToDisable.add(deleteButton1);
    grid.addButtonsNotEnabled(buttonsToDisable,parentFrame);
  }


  private void jbInit() throws Exception {
    grid.setMaxNumberOfRowsOnInsert(50);
    reloadButton.setEnabled(false);
    titledBorder2 = new TitledBorder("");
    this.setLayout(borderLayout1);
    topPanel.setLayout(gridBagLayout1);
    grid.setAutoLoadData(false);
    if (enabledInsertEdit) {
      grid.setInsertButton(insertButton1);
      grid.setEditButton(editButton1);
    }
    grid.setDeleteButton(deleteButton1);
    grid.setExportButton(exportButton);
    grid.setFunctionId("DOC01_ORDERS");
    grid.setMaxSortedColumns(3);
    grid.setNavBar(navigatorBar);
    grid.setReloadButton(reloadButton);
    grid.setSaveButton(saveButton1);
    grid.setValueObjectClassName("org.jallinone.sales.documents.headercharges.java.SaleDocChargeVO");
    grid.setVisibleStatusPanel(false);
    this.setBorder(titledBorder2);
    titledBorder2.setTitle(ClientSettings.getInstance().getResources().getResource("charges"));
    titledBorder2.setTitleColor(Color.blue);
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    colChargeCode.setEditableOnInsert(true);
    colChargeCode.setColumnName("chargeCodeSal06DOC03");
    colChargeCode.setPreferredWidth(100);
    colChargeCode.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colChargeCode.setMaxCharacters(20);
    colChargeDescr.setColumnName("chargeDescriptionDOC03");
    colChargeDescr.setPreferredWidth(200);
    colValue.setColumnName("valueDOC03");
    colValue.setColumnRequired(false);
    colValue.setEditableOnEdit(true);
    colValue.setEditableOnInsert(true);
    colValue.setPreferredWidth(100);

    colInvoicedValue.setColumnName("invoicedValueDOC03");
    colInvoicedValue.setColumnRequired(false);
    colInvoicedValue.setEditableOnEdit(false);
    colInvoicedValue.setEditableOnInsert(false);
    colInvoicedValue.setPreferredWidth(100);

    colValueSal06.setColumnName("valueSal06DOC03");
    colValueSal06.setColumnRequired(false);
    colValueSal06.setPreferredWidth(100);
    colPerc.setColumnName("percDOC03");
    colPerc.setColumnRequired(false);
    colPerc.setEditableOnEdit(true);
    colPerc.setEditableOnInsert(true);
    colPerc.setPreferredWidth(100);
    colPerc.setGrouping(false);
    colPercSal06.setColumnName("percSal06DOC03");
    colPercSal06.setColumnRequired(false);
    colPercSal06.setPreferredWidth(100);
    this.add(topPanel, BorderLayout.NORTH);
    this.add(grid, BorderLayout.CENTER);
    grid.getColumnContainer().add(colChargeCode, null);
    grid.getColumnContainer().add(colChargeDescr, null);
    grid.getColumnContainer().add(colValue, null);
    grid.getColumnContainer().add(colValueSal06, null);
    if (enabledInsertEdit)
      grid.getColumnContainer().add(colInvoicedValue, null);
    grid.getColumnContainer().add(colPerc, null);
    grid.getColumnContainer().add(colPercSal06, null);
    topPanel.add(buttonsPanel,      new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    if (enabledInsertEdit) {
      buttonsPanel.add(insertButton1, null);
      buttonsPanel.add(editButton1, null);
      buttonsPanel.add(saveButton1, null);
    }

    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(deleteButton1, null);
    buttonsPanel.add(exportButton, null);
    buttonsPanel.add(navigatorBar, null);
  }


  public GridControl getGrid() {
    return grid;
  }


  /**
   * Enable or disable this panel.
   */
  public final void setEnabled(boolean enabled) {
    navigatorBar.setEnabled(enabled);
    exportButton.setEnabled(enabled);
    if (!enabled) {
      grid.clearData();
      reloadButton.setEnabled(enabled);
      insertButton1.setEnabled(enabled);
      editButton1.setEnabled(enabled);
      deleteButton1.setEnabled(enabled);
      saveButton1.setEnabled(enabled);
      reloadButton.setEnabled(enabled);
    }
    else {
      insertButton1.setEnabled(enabled);
      editButton1.setEnabled(enabled);
      deleteButton1.setEnabled(enabled);
      reloadButton.setEnabled(enabled);
    }
  }


  /**
   * Method called by the parent panel (item row) on inserting/loading an item row.
   */
  public void setParentVO(DetailSaleDocVO parentVO) {
    this.parentVO = parentVO;
    chargeDataLocator.getLookupFrameParams().put(ApplicationConsts.SALE_DOC_VO,parentVO);
    chargeDataLocator.getLookupValidationParameters().put(ApplicationConsts.SALE_DOC_VO,parentVO);
    grid.getOtherGridParams().put(ApplicationConsts.SALE_DOC_PK,new SaleDocPK(
        parentVO.getCompanyCodeSys01DOC01(),
        parentVO.getDocTypeDOC01(),
        parentVO.getDocYearDOC01(),
        parentVO.getDocNumberDOC01()
    ));
  }


  public final DetailSaleDocVO getParentVO() {
    return parentVO;
  }

  public double getMaxValue(int int0) {
    return Double.MAX_VALUE;
  }

  public double getMinValue(int int0) {
    return 0.0;
  }

  public boolean isGrouping(int int0) {
    return true;
  }

  public int getDecimals(int int0) {
    if (parentVO!=null)
      return parentVO.getDecimalsREG03().intValue();
    else
      return 0;
  }

  public String getCurrencySymbol(int int0) {
    if (parentVO!=null)
      return parentVO.getCurrencySymbolREG03();
    else
    return "E";
  }

  public SaleDocument getParentFrame() {
    return parentFrame;
  }

}

