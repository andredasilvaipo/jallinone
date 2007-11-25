package org.jallinone.sales.documents.headerdiscounts.client;

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
import org.jallinone.sales.documents.headerdiscounts.java.*;
import org.jallinone.sales.documents.java.SaleDocPK;
import org.jallinone.sales.documents.java.DetailSaleDocVO;
import org.jallinone.sales.documents.client.SaleOrderDocRowsGridPanel;
import org.jallinone.sales.documents.client.SaleOrderDocFrame;
import java.util.HashSet;
import org.jallinone.sales.documents.client.SaleDocument;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Panel that contains the list of header discounts applied to the sale document.
 * Discounts can be custoemr discounts or customer hierarchy discounts.</p>
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
public class SaleDocDiscountsPanel extends JPanel implements CurrencyColumnSettings {

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

  /** customer code lookup data locator */
  LookupServerDataLocator discountDataLocator = new LookupServerDataLocator();

  /** customer code lookup controller */
  LookupController discountController = new LookupController();

  /** grid data locator */
  ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();
  BorderLayout borderLayout1 = new BorderLayout();
  InsertButton insertButton1 = new InsertButton();
  EditButton editButton1 = new EditButton();
  SaveButton saveButton1 = new SaveButton();
  DeleteButton deleteButton1 = new DeleteButton();
  CodLookupColumn colDiscountCode = new CodLookupColumn();
  TextColumn colDiscountDescr = new TextColumn();
  CurrencyColumn colValue = new CurrencyColumn();
  CurrencyColumn colMinValue = new CurrencyColumn();
  CurrencyColumn colMaxValue = new CurrencyColumn();
  DecimalColumn colPerc = new DecimalColumn();
  DecimalColumn colMinPerc = new DecimalColumn();
  DecimalColumn colMaxPerc = new DecimalColumn();

  /** header v.o. */
  private DetailSaleDocVO parentVO = null;

  /** parent frame */
  private SaleDocument parentFrame = null;

  /** used to enable insert/edit of item rows and item discounts */
  private boolean enabledInsertEdit;


  public SaleDocDiscountsPanel(SaleDocument parentFrame,boolean enabledInsertEdit) {
    this.parentFrame = parentFrame;
    this.enabledInsertEdit = enabledInsertEdit;
    try {
      jbInit();

      init();

      grid.setController(new SaleDocDiscountsController(this));
      grid.setGridDataLocator(gridDataLocator);
      gridDataLocator.setServerMethodName("loadSaleDocDiscounts");

      // discount code lookup...
      discountDataLocator.setGridMethodName("loadSaleHeaderDiscounts");
      discountDataLocator.setValidationMethodName("validateSaleHeaderDiscountCode");

      colDiscountCode.setLookupController(discountController);
      discountController.setLookupDataLocator(discountDataLocator);
      discountController.setFrameTitle("customer discounts");

      discountController.setLookupValueObjectClassName("org.jallinone.sales.discounts.java.DiscountVO");
      discountController.addLookup2ParentLink("discountCodeSAL03", "discountCodeSal03DOC05");
      discountController.addLookup2ParentLink("descriptionSYS10", "discountDescriptionDOC05");
      discountController.addLookup2ParentLink("minValueSAL03", "minValueDOC05");
      discountController.addLookup2ParentLink("maxValueSAL03", "maxValueDOC05");
      discountController.addLookup2ParentLink("minPercSAL03", "minPercDOC05");
      discountController.addLookup2ParentLink("maxPercSAL03", "maxPercDOC05");
      discountController.addLookup2ParentLink("startDateSAL03", "startDateDOC05");
      discountController.addLookup2ParentLink("endDateSAL03", "endDateDOC05");

      discountController.setAllColumnVisible(false);
      discountController.setVisibleColumn("discountCodeSAL03", true);
      discountController.setVisibleColumn("descriptionSYS10", true);
      discountController.setVisibleColumn("minValueSAL03", true);
      discountController.setVisibleColumn("maxValueSAL03", true);
      discountController.setVisibleColumn("minPercSAL03", true);
      discountController.setVisibleColumn("maxPercSAL03", true);
      discountController.setVisibleColumn("startDateSAL03", true);
      discountController.setVisibleColumn("endDateSAL03", true);
      discountController.setPreferredWidthColumn("descriptionSYS10", 200);
      discountController.setPreferredWidthColumn("minValueSAL03", 70);
      discountController.setPreferredWidthColumn("maxValueSAL03", 70);
      discountController.setPreferredWidthColumn("minPercSAL03", 70);
      discountController.setPreferredWidthColumn("maxPercSAL03", 70);
      discountController.setFramePreferedSize(new Dimension(650,500));
      discountController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          SaleDocDiscountVO vo = (SaleDocDiscountVO)parentVO;
          if (vo.getDiscountCodeSal03DOC05()==null || vo.getDiscountCodeSal03DOC05().equals("")) {
            vo.setValueDOC05(null);
            vo.setPercDOC05(null);
          }
          else {
            vo.setValueDOC05(vo.getMinValueDOC05());
            vo.setPercDOC05(null);
          }
        }

        public void beforeLookupAction(ValueObject parentVO) { }

        public void forceValidate() {}

      });


      colValue.setDynamicSettings(this);
      colMinValue.setDynamicSettings(this);
      colMaxValue.setDynamicSettings(this);

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
    grid.setValueObjectClassName("org.jallinone.sales.documents.headerdiscounts.java.SaleDocDiscountVO");
    grid.setVisibleStatusPanel(false);
    this.setBorder(titledBorder2);
    titledBorder2.setTitle(ClientSettings.getInstance().getResources().getResource("customer discounts"));
    titledBorder2.setTitleColor(Color.blue);
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    colDiscountCode.setEditableOnInsert(true);
    colDiscountCode.setColumnName("discountCodeSal03DOC05");
    colDiscountCode.setPreferredWidth(80);
    colDiscountCode.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colDiscountCode.setMaxCharacters(20);
    colDiscountDescr.setColumnName("discountDescriptionDOC05");
    colDiscountDescr.setPreferredWidth(200);
    colValue.setColumnName("valueDOC05");
    colValue.setColumnRequired(false);
    colValue.setEditableOnEdit(true);
    colValue.setEditableOnInsert(true);
    colValue.setPreferredWidth(80);
    colMinValue.setColumnName("minValueDOC05");
    colMinValue.setColumnRequired(false);
    colMinValue.setPreferredWidth(60);
    colMaxValue.setColumnName("maxValueDOC05");
    colMaxValue.setColumnRequired(false);
    colMaxValue.setPreferredWidth(60);
    colPerc.setColumnName("percDOC05");
    colPerc.setColumnRequired(false);
    colPerc.setEditableOnEdit(true);
    colPerc.setEditableOnInsert(true);
    colPerc.setPreferredWidth(80);
    colPerc.setGrouping(false);
    colMinPerc.setColumnName("minPercDOC05");
    colMinPerc.setColumnRequired(false);
    colMinPerc.setPreferredWidth(60);
    colMaxPerc.setColumnName("maxPercDOC05");
    colMaxPerc.setColumnRequired(false);
    colMaxPerc.setPreferredWidth(60);
    this.add(topPanel, BorderLayout.NORTH);
    this.add(grid, BorderLayout.CENTER);
    grid.getColumnContainer().add(colDiscountCode, null);
    grid.getColumnContainer().add(colDiscountDescr, null);
    grid.getColumnContainer().add(colValue, null);
    grid.getColumnContainer().add(colMinValue, null);
    grid.getColumnContainer().add(colMaxValue, null);
    grid.getColumnContainer().add(colPerc, null);
    grid.getColumnContainer().add(colMinPerc, null);
    grid.getColumnContainer().add(colMaxPerc, null);
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
    discountDataLocator.getLookupFrameParams().put(ApplicationConsts.SALE_DOC_VO,parentVO);
    discountDataLocator.getLookupValidationParameters().put(ApplicationConsts.SALE_DOC_VO,parentVO);
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

