package org.jallinone.sales.documents.activities.client;

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
import org.jallinone.sales.documents.activities.java.*;
import org.jallinone.sales.documents.java.SaleDocPK;
import org.jallinone.sales.documents.java.DetailSaleDocVO;
import org.jallinone.sales.documents.client.SaleOrderDocRowsGridPanel;
import org.jallinone.sales.documents.client.SaleOrderDocFrame;
import org.jallinone.sales.activities.java.SaleActivityVO;
import java.util.HashSet;
import org.jallinone.sales.documents.client.SaleDocument;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Panel that contains the list of sale activities applied to the sale document.
 * Activities can be custoemr activitys or customer hierarchy activitys.</p>
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
public class SaleDocActivitiesPanel extends JPanel implements CurrencyColumnSettings {

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

  /** activity code lookup data locator */
  LookupServerDataLocator activityDataLocator = new LookupServerDataLocator();

  /** activity code lookup controller */
  LookupController activityController = new LookupController();

  /** grid data locator */
  ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();

  BorderLayout borderLayout1 = new BorderLayout();
  InsertButton insertButton1 = new InsertButton();
  EditButton editButton1 = new EditButton();
  SaveButton saveButton1 = new SaveButton();
  DeleteButton deleteButton1 = new DeleteButton();
  CodLookupColumn colActivityCode = new CodLookupColumn();
  TextColumn colActivityDescr = new TextColumn();
  CurrencyColumn colValue = new CurrencyColumn();
  CurrencyColumn colInvoicedValue = new CurrencyColumn();
  CurrencyColumn colValueSal09 = new CurrencyColumn();

  /** header v.o. */
  private DetailSaleDocVO parentVO = null;

  /** parent frame */
  private SaleDocument parentFrame = null;

  TextColumn colCurrCode = new TextColumn();
  DecimalColumn colDuration = new DecimalColumn();
  TextColumn colVatCode = new TextColumn();
  TextColumn colVatDescr = new TextColumn();
  DecimalColumn colVatValue = new DecimalColumn();
  DecimalColumn colVatDeductible = new DecimalColumn();
  CodLookupColumn colScheduledAct = new CodLookupColumn();

  /** scheduled activity lookup data locator */
  LookupServerDataLocator schActivityDataLocator = new LookupServerDataLocator();

  /** scheduled activity lookup controller */
  LookupController schActivityController = new LookupController();

  /** used to enable insert/edit of item rows and item discounts */
  private boolean enabledInsertEdit;


  public SaleDocActivitiesPanel(SaleDocument parentFrame,boolean enabledInsertEdit) {
    this.parentFrame = parentFrame;
    this.enabledInsertEdit = enabledInsertEdit;
    try {
      jbInit();

      // set buttons disabilitation...
      init();

      grid.setController(new SaleDocActivitiesController(this));
      grid.setGridDataLocator(gridDataLocator);
      gridDataLocator.setServerMethodName("loadSaleDocActivities");

      // activity code lookup...
      activityDataLocator.setGridMethodName("loadSaleActivities");
      activityDataLocator.setValidationMethodName("validateSaleActivityCode");

      colActivityCode.setLookupController(activityController);
      colActivityCode.setControllerMethodName("getSalesActivitiesList");
      activityController.setLookupDataLocator(activityDataLocator);
      activityController.setFrameTitle("sale activities");

      activityController.setLookupValueObjectClassName("org.jallinone.sales.activities.java.SaleActivityVO");
      activityController.addLookup2ParentLink("activityCodeSAL09", "activityCodeSal09DOC13");
      activityController.addLookup2ParentLink("descriptionSYS10", "activityDescriptionDOC13");
      activityController.addLookup2ParentLink("valueSAL09", "valueSal09DOC13");
      activityController.addLookup2ParentLink("vatCodeReg01SAL09", "vatCodeSal09DOC13");
      activityController.addLookup2ParentLink("vatDescriptionSYS10", "vatDescriptionDOC13");
      activityController.addLookup2ParentLink("vatValueREG01", "valueReg01DOC13");
      activityController.addLookup2ParentLink("vatDeductibleREG01", "vatDeductibleDOC13");
      activityController.addLookup2ParentLink("umCodeReg02SAL09", "umCodeReg02DOC13");
      activityController.addLookup2ParentLink("decimalsREG02", "decimalsREG02");
      activityController.addLookup2ParentLink("currencyCodeReg03SAL09", "currencyCodeReg03DOC13");

      activityController.setAllColumnVisible(false);
      activityController.setVisibleColumn("activityCodeSAL09", true);
      activityController.setVisibleColumn("descriptionSYS10", true);
      activityController.setVisibleColumn("valueSAL09", true);
      activityController.setVisibleColumn("vatCodeReg01SAL09", true);
      activityController.setVisibleColumn("vatDescriptionSYS10", true);
      activityController.setVisibleColumn("vatValueREG01", true);
      activityController.setVisibleColumn("vatDeductibleREG01", true);
      activityController.setPreferredWidthColumn("activityCodeSAL09", 80);
      activityController.setPreferredWidthColumn("valueSAL09", 60);
      activityController.setPreferredWidthColumn("descriptionSYS10", 200);
      activityController.setPreferredWidthColumn("vatCodeReg01SAL09", 80);
      activityController.setPreferredWidthColumn("vatDescriptionSYS10", 150);
      activityController.setPreferredWidthColumn("vatValueREG01", 60);
      activityController.setPreferredWidthColumn("vatDeductibleREG01", 60);
      activityController.setFramePreferedSize(new Dimension(650,500));
      activityController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          SaleDocActivityVO vo = (SaleDocActivityVO)parentVO;
          if (vo.getActivityCodeSal09DOC13()==null || vo.getActivityCodeSal09DOC13().equals("")) {
            vo.setValueDOC13(null);
            vo.setDurationDOC13(null);
          }
          else {
            SaleActivityVO lookupVO = (SaleActivityVO)activityController.getLookupVO();
            vo.setValueDOC13(lookupVO.getValueSAL09());
          }
        }

        public void beforeLookupAction(ValueObject parentVO) {
          activityDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,SaleDocActivitiesPanel.this.parentVO.getCompanyCodeSys01DOC01());
          activityDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,SaleDocActivitiesPanel.this.parentVO.getCompanyCodeSys01DOC01());
          activityDataLocator.getLookupFrameParams().put(ApplicationConsts.CURRENCY_CODE_REG03,SaleDocActivitiesPanel.this.parentVO.getCurrencyCodeReg03DOC01());
          activityDataLocator.getLookupValidationParameters().put(ApplicationConsts.CURRENCY_CODE_REG03,SaleDocActivitiesPanel.this.parentVO.getCurrencyCodeReg03DOC01());
        }

        public void forceValidate() {}

      });


      // scheduled activity lookup...
      schActivityDataLocator.setGridMethodName("loadScheduledActivities");
      schActivityDataLocator.setValidationMethodName("");

      colScheduledAct.setLookupController(schActivityController);
      colScheduledAct.setControllerMethodName("getScheduledActivitiesList");
      schActivityController.setLookupDataLocator(schActivityDataLocator);
      schActivityController.setFrameTitle("scheduled activities");

      schActivityController.setLookupValueObjectClassName("org.jallinone.scheduler.activities.java.ScheduledActivityVO");
      schActivityController.addLookup2ParentLink("progressiveSCH06", "progressiveSch06DOC13");
      schActivityController.addLookup2ParentLink("descriptionSCH06", "descriptionSCH06");
      schActivityController.addLookup2ParentLink("durationSCH06", "durationDOC13");

      schActivityController.setAllColumnVisible(false);
      schActivityController.setVisibleColumn("descriptionSCH06", true);
      schActivityController.setVisibleColumn("descriptionSCH06", true);
      schActivityController.setVisibleColumn("durationSCH06", true);
      schActivityController.setVisibleColumn("startDateSCH06", true);
      schActivityController.setVisibleColumn("endDateSCH06", true);
      schActivityController.setSortedColumn("startDateSCH06", "ASC");
      schActivityController.setFramePreferedSize(new Dimension(650,500));
      schActivityController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) { }

        public void beforeLookupAction(ValueObject parentVO) {
          schActivityDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,SaleDocActivitiesPanel.this.parentVO.getCompanyCodeSys01DOC01());
          schActivityDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,SaleDocActivitiesPanel.this.parentVO.getCompanyCodeSys01DOC01());
          schActivityDataLocator.getLookupFrameParams().put(ApplicationConsts.ACTIVITY_CODE,((SaleDocActivityVO)parentVO).getActivityCodeSal09DOC13());
          schActivityDataLocator.getLookupValidationParameters().put(ApplicationConsts.ACTIVITY_CODE,((SaleDocActivityVO)parentVO).getActivityCodeSal09DOC13());
        }

        public void forceValidate() {}

      });





      colValue.setDynamicSettings(this);
      colValueSal09.setDynamicSettings(this);
      colInvoicedValue.setDynamicSettings(this);
      colDuration.setDynamicSettings(new DecimalColumnSettings(){

        /**
         * @return max number of decimals
         */
        public int getDecimals(int row) {
          return 0;
        }

        /**
         * @return maximum value
         */
        public double getMaxValue(int row) {
          return Double.MAX_VALUE;
        }

        /**
         * @return minimum value
         */
        public double getMinValue(int row) {
          return 0;
        }

        /**
         * @return boolean thousands symbol visibility
         */
        public boolean isGrouping(int row) {
          return false;
        }

    });


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
    grid.setDeleteButton(deleteButton1);
    if (enabledInsertEdit) {
      grid.setEditButton(editButton1);
      grid.setInsertButton(insertButton1);
    }
    grid.setExportButton(exportButton);
    grid.setFunctionId("DOC01_ORDERS");
    grid.setMaxSortedColumns(3);
    grid.setNavBar(navigatorBar);
    grid.setReloadButton(reloadButton);
    grid.setSaveButton(saveButton1);
    grid.setValueObjectClassName("org.jallinone.sales.documents.activities.java.SaleDocActivityVO");
    grid.setVisibleStatusPanel(false);
    this.setBorder(titledBorder2);
    titledBorder2.setTitle(ClientSettings.getInstance().getResources().getResource("sale activities"));
    titledBorder2.setTitleColor(Color.blue);
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    colActivityCode.setEditableOnInsert(true);
    colActivityCode.setColumnName("activityCodeSal09DOC13");
    colActivityCode.setPreferredWidth(80);
    colActivityCode.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colActivityCode.setMaxCharacters(20);
    colActivityDescr.setColumnName("activityDescriptionDOC13");
    colActivityDescr.setPreferredWidth(200);
    colValue.setColumnName("valueDOC13");
    colValue.setColumnRequired(false);
    colValue.setEditableOnEdit(false);
    colValue.setPreferredWidth(100);

    colInvoicedValue.setColumnName("invoicedValueDOC13");
    colInvoicedValue.setColumnRequired(false);
    colInvoicedValue.setEditableOnEdit(false);
    colInvoicedValue.setPreferredWidth(90);

    colValueSal09.setColumnName("valueSal09DOC13");
    colValueSal09.setColumnRequired(false);
    colValueSal09.setPreferredWidth(90);
    colCurrCode.setColumnDuplicable(false);
    colCurrCode.setColumnFilterable(true);
    colCurrCode.setColumnName("currencyCodeReg03DOC13");
    colCurrCode.setColumnSortable(true);
    colCurrCode.setPreferredWidth(70);
    colDuration.setColumnName("durationDOC13");
    colDuration.setEditableOnEdit(true);
    colDuration.setEditableOnInsert(true);
    colDuration.setPreferredWidth(70);
    colVatCode.setColumnName("vatCodeSal09DOC13");
    colVatCode.setPreferredWidth(70);
    colScheduledAct.setColumnName("descriptionSCH06");
    colScheduledAct.setColumnRequired(false);
    colScheduledAct.setEditableOnEdit(true);
    colScheduledAct.setEditableOnInsert(true);
    colScheduledAct.setEnableCodBox(false);
    colScheduledAct.setPreferredWidth(200);
    colVatDeductible.setColumnName("vatDeductibleDOC13");
    colVatDeductible.setPreferredWidth(80);
    colVatValue.setColumnName("valueReg01DOC13");
    colVatValue.setPreferredWidth(80);
    colVatDescr.setColumnName("vatDescriptionDOC13");
    colVatDescr.setPreferredWidth(150);
    this.add(topPanel, BorderLayout.NORTH);
    this.add(grid, BorderLayout.CENTER);
    grid.getColumnContainer().add(colActivityCode, null);
    grid.getColumnContainer().add(colActivityDescr, null);
    grid.getColumnContainer().add(colDuration, null);
    grid.getColumnContainer().add(colValue, null);
    grid.getColumnContainer().add(colValueSal09, null);
    if (enabledInsertEdit)
      grid.getColumnContainer().add(colInvoicedValue, null);
    grid.getColumnContainer().add(colCurrCode, null);
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
    grid.getColumnContainer().add(colVatCode, null);
    grid.getColumnContainer().add(colVatDescr, null);
    grid.getColumnContainer().add(colVatValue, null);
    grid.getColumnContainer().add(colVatDeductible, null);
    grid.getColumnContainer().add(colScheduledAct, null);
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
    activityDataLocator.getLookupFrameParams().put(ApplicationConsts.SALE_DOC_VO,parentVO);
    activityDataLocator.getLookupValidationParameters().put(ApplicationConsts.SALE_DOC_VO,parentVO);
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

