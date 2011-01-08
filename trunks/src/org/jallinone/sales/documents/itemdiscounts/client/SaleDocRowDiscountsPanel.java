package org.jallinone.sales.documents.itemdiscounts.client;

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
import org.jallinone.sales.documents.itemdiscounts.java.*;
import org.jallinone.sales.documents.java.DetailSaleDocRowVO;
import org.jallinone.sales.documents.java.SaleDocRowPK;
import org.jallinone.sales.documents.java.DetailSaleDocVO;
import org.jallinone.sales.documents.client.SaleOrderDocRowsGridPanel;
import java.util.HashSet;
import org.jallinone.sales.documents.client.SaleDocument;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Panel that contains the list of discounts applied to the sale document row (to the item).
 * Discounts can be item discounts or item hierarchy discounts.</p>
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
public class SaleDocRowDiscountsPanel extends JPanel implements CurrencyColumnSettings,GenericButtonController {

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

  /** item code lookup data locator */
  LookupServerDataLocator discountDataLocator = new LookupServerDataLocator();

  /** item code lookup controller */
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
  DecimalColumn colMinQty = new DecimalColumn();
  CheckBoxColumn colMultipleQty = new CheckBoxColumn();

  /** header v.o. */
  private DetailSaleDocVO headerParentVO = null;

  /** row v.o. */
  private DetailSaleDocRowVO parentVO = null;

  /** header frame */
  private SaleDocument parentFrame = null;

  /** rows grid */
  private GridControl parentGrid = null;

  /** row detail */
  private Form parentDetail = null;

  /** used to enable insert/edit of item rows and item discounts */
  private boolean enabledInsertEditDelete;


  public SaleDocRowDiscountsPanel(SaleDocument parentFrame,GridControl parentGrid,Form parentDetail,boolean enabledInsertEditDelete) {
    this.parentFrame = parentFrame;
    this.parentGrid = parentGrid;
    this.parentDetail = parentDetail;
    this.enabledInsertEditDelete = enabledInsertEditDelete;
    try {
      jbInit();

      // set buttons disabilitation...
      init();

      grid.setController(new SaleDocRowDiscountsController(this));
      grid.setGridDataLocator(gridDataLocator);
      gridDataLocator.setServerMethodName("loadSaleDocRowDiscounts");

      // discount code lookup...
      discountDataLocator.setGridMethodName("loadSaleItemDiscounts");
      discountDataLocator.setValidationMethodName("validateSaleItemDiscountCode");

      colDiscountCode.setLookupController(discountController);
      discountController.setLookupDataLocator(discountDataLocator);
      discountController.setFrameTitle("item discounts");

      discountController.setLookupValueObjectClassName("org.jallinone.sales.discounts.java.DiscountVO");
      discountController.addLookup2ParentLink("discountCodeSAL03", "discountCodeSal03DOC04");
      discountController.addLookup2ParentLink("descriptionSYS10", "discountDescriptionDOC04");
      discountController.addLookup2ParentLink("minValueSAL03", "minValueDOC04");
      discountController.addLookup2ParentLink("maxValueSAL03", "maxValueDOC04");
      discountController.addLookup2ParentLink("minPercSAL03", "minPercDOC04");
      discountController.addLookup2ParentLink("maxPercSAL03", "maxPercDOC04");
      discountController.addLookup2ParentLink("startDateSAL03", "startDateDOC04");
      discountController.addLookup2ParentLink("endDateSAL03", "endDateDOC04");
      discountController.addLookup2ParentLink("minQtySAL03", "minQtyDOC04");
      discountController.addLookup2ParentLink("multipleQtySAL03", "multipleQtyDOC04");

      discountController.setAllColumnVisible(false);
      discountController.setVisibleColumn("discountCodeSAL03", true);
      discountController.setVisibleColumn("descriptionSYS10", true);
      discountController.setVisibleColumn("minValueSAL03", true);
      discountController.setVisibleColumn("maxValueSAL03", true);
      discountController.setVisibleColumn("minPercSAL03", true);
      discountController.setVisibleColumn("maxPercSAL03", true);
      discountController.setVisibleColumn("startDateSAL03", true);
      discountController.setVisibleColumn("endDateSAL03", true);
      discountController.setVisibleColumn("minQtySAL03", true);
      discountController.setVisibleColumn("multipleQtySAL03", true);
      discountController.setPreferredWidthColumn("descriptionSYS10", 200);
      discountController.setPreferredWidthColumn("minValueSAL03", 70);
      discountController.setPreferredWidthColumn("maxValueSAL03", 70);
      discountController.setPreferredWidthColumn("minPercSAL03", 70);
      discountController.setPreferredWidthColumn("maxPercSAL03", 70);
      discountController.setPreferredWidthColumn("minQtySAL03", 40);
      discountController.setPreferredWidthColumn("multipleQtySAL03", 50);
      discountController.setFramePreferedSize(new Dimension(720,500));
      discountController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          // fill in the grid v.o., according to the selected item settings...
          SaleItemDiscountVO vo = (SaleItemDiscountVO)parentVO;
          if (vo.getDiscountCodeSal03DOC04()==null || vo.getDiscountCodeSal03DOC04().equals("")) {
            vo.setValueDOC04(null);
            vo.setPercDOC04(null);
          }
          else {
            vo.setValueDOC04(vo.getMinValueDOC04());
            vo.setPercDOC04(vo.getMinPercDOC04());
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
    grid.addButtonsNotEnabled(buttonsToDisable,this);
  }


  /**
   * Method called by GenericButton.setEnabled method to check if the button must be disabled.
   * @param button button whose abilitation must be checked
   * @return <code>true</code> if no policy is defined in the form/grid for the specified button, <code>false</code> if there exists a disabilitation policy for the specified button (through addButtonsNotEnabledOnState form/grid method)
   */
  public boolean isButtonDisabled(GenericButton button) {
    DetailSaleDocVO vo = (DetailSaleDocVO)parentFrame.getHeaderFormPanel().getVOModel().getValueObject();
    if (vo!=null && vo.getDocStateDOC01()!=null &&
        (vo.getDocStateDOC01().equals(ApplicationConsts.CONFIRMED) ||
         vo.getDocStateDOC01().equals(ApplicationConsts.CLOSED)))
      return true;
    else
      return false;
  }


  private void jbInit() throws Exception {
    grid.setMaxNumberOfRowsOnInsert(50);
    reloadButton.setEnabled(false);
    titledBorder2 = new TitledBorder("");
    this.setLayout(borderLayout1);
    topPanel.setLayout(gridBagLayout1);
    grid.setAutoLoadData(false);
    grid.setDeleteButton(deleteButton1);
    if (enabledInsertEditDelete) {
      grid.setEditButton(editButton1);
      grid.setInsertButton(insertButton1);
    }
    grid.setExportButton(exportButton);
//    grid.setFunctionId("DOC01_ORDERS");
    grid.setMaxSortedColumns(3);
    grid.setNavBar(navigatorBar);
    grid.setReloadButton(reloadButton);
    grid.setSaveButton(saveButton1);
    grid.setValueObjectClassName("org.jallinone.sales.documents.itemdiscounts.java.SaleItemDiscountVO");
    grid.setVisibleStatusPanel(false);
    this.setBorder(titledBorder2);
    titledBorder2.setTitle(ClientSettings.getInstance().getResources().getResource("item discounts"));
    titledBorder2.setTitleColor(Color.blue);
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    colDiscountCode.setEditableOnInsert(true);
    colDiscountCode.setColumnName("discountCodeSal03DOC04");
    colDiscountCode.setPreferredWidth(80);
    colDiscountCode.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colDiscountCode.setMaxCharacters(20);
    colDiscountDescr.setColumnName("discountDescriptionDOC04");
    colDiscountDescr.setPreferredWidth(200);
    colValue.setColumnName("valueDOC04");
    colValue.setColumnRequired(false);
    colValue.setEditableOnEdit(true);
    colValue.setEditableOnInsert(true);
    colValue.setPreferredWidth(80);
    colMinValue.setColumnName("minValueDOC04");
    colMinValue.setColumnRequired(false);
    colMinValue.setPreferredWidth(60);
    colMaxValue.setColumnName("maxValueDOC04");
    colMaxValue.setColumnRequired(false);
    colMaxValue.setPreferredWidth(60);
    colPerc.setColumnName("percDOC04");
    colPerc.setColumnRequired(false);
    colPerc.setEditableOnEdit(true);
    colPerc.setEditableOnInsert(true);
    colPerc.setPreferredWidth(80);
    colPerc.setDecimals(2);
    colPerc.setGrouping(false);
    colMinPerc.setColumnName("minPercDOC04");
    colMinPerc.setColumnRequired(false);
    colMinPerc.setPreferredWidth(60);
    colMinPerc.setDecimals(2);
    colMaxPerc.setColumnName("maxPercDOC04");
    colMaxPerc.setColumnRequired(false);
    colMaxPerc.setPreferredWidth(60);
    colMaxPerc.setDecimals(2);

    colMinQty.setColumnDuplicable(true);
    colMinQty.setColumnFilterable(true);
    colMinQty.setColumnSortable(true);
    colMinQty.setEditableOnEdit(false);
    colMinQty.setEditableOnInsert(false);
    colMinQty.setColumnRequired(true);
    colMinQty.setMinValue(1);
    colMinQty.setPreferredWidth(50);
    colMinQty.setColumnName("minQtyDOC04");

    colMultipleQty.setColumnDuplicable(true);
    colMultipleQty.setColumnFilterable(true);
    colMultipleQty.setColumnSortable(true);
    colMultipleQty.setEditableOnEdit(false);
    colMultipleQty.setEditableOnInsert(false);
    colMultipleQty.setColumnRequired(false);
    colMultipleQty.setPreferredWidth(50);
    colMultipleQty.setColumnName("multipleQtyDOC04");

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
    grid.getColumnContainer().add(colMinQty, null);
    grid.getColumnContainer().add(colMultipleQty, null);

    topPanel.add(buttonsPanel,      new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    if (enabledInsertEditDelete) {
      buttonsPanel.add(insertButton1, null);
      buttonsPanel.add(editButton1, null);
      buttonsPanel.add(saveButton1, null);
    }
    buttonsPanel.add(reloadButton, null);
    if (enabledInsertEditDelete) {
      buttonsPanel.add(deleteButton1, null);
    }

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
  public void setParentVO(DetailSaleDocVO headerParentVO,DetailSaleDocRowVO parentVO) {
    this.headerParentVO = headerParentVO;
    this.parentVO = parentVO;
		if (parentVO!=null) {
			discountDataLocator.getLookupFrameParams().put(ApplicationConsts.SALE_DOC_ROW_VO,parentVO);
			discountDataLocator.getLookupValidationParameters().put(ApplicationConsts.SALE_DOC_ROW_VO,parentVO);
			grid.getOtherGridParams().put(ApplicationConsts.SALE_DOC_ROW_PK,new SaleDocRowPK(
					parentVO.getCompanyCodeSys01DOC02(),
					parentVO.getDocTypeDOC02(),
					parentVO.getDocYearDOC02(),
					parentVO.getDocNumberDOC02(),
					parentVO.getItemCodeItm01DOC02(),
					parentVO.getVariantTypeItm06DOC02(),
					parentVO.getVariantCodeItm11DOC02(),
					parentVO.getVariantTypeItm07DOC02(),
					parentVO.getVariantCodeItm12DOC02(),
					parentVO.getVariantTypeItm08DOC02(),
					parentVO.getVariantCodeItm13DOC02(),
					parentVO.getVariantTypeItm09DOC02(),
					parentVO.getVariantCodeItm14DOC02(),
					parentVO.getVariantTypeItm10DOC02(),
					parentVO.getVariantCodeItm15DOC02()
			));
		}
  }


  public final DetailSaleDocRowVO getParentVO() {
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
      return headerParentVO.getDecimalsREG03().intValue();
    else
      return 0;
  }

  public String getCurrencySymbol(int int0) {
    if (parentVO!=null)
      return headerParentVO.getCurrencySymbolREG03();
    else
    return "E";
  }
  public SaleDocument getParentFrame() {
    return parentFrame;
  }
  public GridControl getParentGrid() {
    return parentGrid;
  }
  public Form getParentDetail() {
    return parentDetail;
  }

}

