package org.jallinone.sales.discounts.client;

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
import javax.swing.border.*;
import org.openswing.swing.form.client.Form;
import org.jallinone.commons.client.CustomizedControls;
import org.openswing.swing.util.java.Consts;
import org.jallinone.commons.client.CustomizedColumns;
import org.jallinone.hierarchies.client.*;
import org.jallinone.commons.client.CompaniesComboColumn;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.message.receive.java.Response;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.domains.java.*;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import org.jallinone.items.java.ItemTypeVO;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.subjects.java.SubjectHierarchyVO;
import javax.swing.tree.DefaultMutableTreeNode;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the items tree+grid frame.</p>
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
public class HierarCustomerDiscountsFrame extends InternalFrame {
  JPanel buttonsPanel = new JPanel();
  JSplitPane split = new JSplitPane();
  LabelControl labelCustomerType = new LabelControl();
  InsertButton insertButton1 = new InsertButton();
  ReloadButton reloadButton1 = new ReloadButton();
  DeleteButton deleteButton1 = new DeleteButton();
  ExportButton exportButton1 = new ExportButton();
  NavigatorBar navigatorBar1 = new NavigatorBar();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  ComboBoxControl comboBoxControl1 = new ComboBoxControl();
  HierarTreePanel hierarTreePanel = new HierarTreePanel();
  GridControl discountsGrid = new GridControl();

  /** grid data locator */
  private ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();

  TextColumn colDiscountCode = new TextColumn();
  TextColumn colDescr = new TextColumn();
  CodLookupColumn colCurrencyCode = new CodLookupColumn();
  DecimalColumn colMinValue = new DecimalColumn();
  DecimalColumn colMaxValue = new DecimalColumn();
  DecimalColumn colMinPerc = new DecimalColumn();
  DecimalColumn colMaxPerc = new DecimalColumn();
  DateColumn colStartDate = new DateColumn();
  DateColumn colEndDate = new DateColumn();

  LookupController currencyController = new LookupController();
  LookupServerDataLocator currencyDataLocator = new LookupServerDataLocator();
  SaveButton saveButton1 = new SaveButton();
  EditButton editButton1 = new EditButton();


  public HierarCustomerDiscountsFrame(final HierarCustomerDiscountsController controller) {
    try {
      jbInit();
      setSize(750,500);
      setMinimumSize(new Dimension(750,500));

      hierarTreePanel.addHierarTreeListener(new HierarTreeListener(){

        public void loadDataCompleted(boolean error) {
          if (hierarTreePanel.getTree().getRowCount()>0)
            hierarTreePanel.getTree().setSelectionRow(0);
          if (hierarTreePanel.getTree().getSelectionPath()!=null)
            controller.leftClick((DefaultMutableTreeNode)hierarTreePanel.getTree().getSelectionPath().getLastPathComponent());
        }

      });

      discountsGrid.setController(controller);
      discountsGrid.setGridDataLocator(gridDataLocator);
      gridDataLocator.setServerMethodName("loadHierarCustomerDiscounts");
      hierarTreePanel.setFunctionId("REG08_CUSTOMERS");
      hierarTreePanel.setTreeController(controller);

      init();

      // currency lookup...
      currencyDataLocator.setGridMethodName("loadCurrencies");
      currencyDataLocator.setValidationMethodName("validateCurrencyCode");
      colCurrencyCode.setLookupController(currencyController);
      colCurrencyCode.setControllerMethodName("getCurrenciesList");
      currencyController.setLookupDataLocator(currencyDataLocator);
      currencyController.setFrameTitle("currencies");
      currencyController.setLookupValueObjectClassName("org.jallinone.registers.currency.java.CurrencyVO");
      currencyController.addLookup2ParentLink("currencyCodeREG03", "currencyCodeReg03SAL03");
      currencyController.setAllColumnVisible(false);
      currencyController.setVisibleColumn("currencyCodeREG03", true);
      currencyController.setVisibleColumn("currencySymbolREG03", true);
      new CustomizedColumns(new BigDecimal(182),currencyController);


    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  /**
   * Retrieve item types and fill in the item types combo box.
   */
  private void init() {
    GridParams gridParams = new GridParams();
    gridParams.getOtherGridParams().put(ApplicationConsts.SUBJECT_TYPE,ApplicationConsts.SUBJECT_ORGANIZATION_CUSTOMER);
    gridParams.getOtherGridParams().put(ApplicationConsts.FUCTION_CODE,"SAL03");
    Response res = ClientUtils.getData("loadSubjectHierarchies",gridParams);

    Domain d = new Domain("CUSTOMER_TYPES");
    if (!res.isError()) {
      SubjectHierarchyVO vo = null;
      java.util.List list = ((VOListResponse)res).getRows();
      for(int i=0;i<list.size();i++) {
        vo = (SubjectHierarchyVO)list.get(i);
        d.addDomainPair(vo.getProgressiveHie02REG08(),vo.getDescriptionSYS10());
      }
    }
    comboBoxControl1.setDomain(d);
    comboBoxControl1.getComboBox().addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange()==e.SELECTED) {
          hierarTreePanel.setProgressiveHIE02((BigDecimal)comboBoxControl1.getValue());
          hierarTreePanel.reloadTree();
          discountsGrid.clearData();
        }
      }
    });
    if (d.getDomainPairList().length==1)
      comboBoxControl1.getComboBox().setSelectedIndex(0);
    else
      comboBoxControl1.getComboBox().setSelectedIndex(-1);
  }


  private void jbInit() throws Exception {
    discountsGrid.setMaxNumberOfRowsOnInsert(50);
    this.setTitle(ClientSettings.getInstance().getResources().getResource("customers hierarchy discounts"));
    discountsGrid.setValueObjectClassName("org.jallinone.sales.discounts.java.HierarCustomerDiscountVO");
    labelCustomerType.setText("customer hierarchies");
    insertButton1.setText("insertButton1");
    reloadButton1.setText("reloadButton1");
    deleteButton1.setText("deleteButton1");
    exportButton1.setText("exportButton1");
    buttonsPanel.setLayout(gridBagLayout1);
    discountsGrid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    discountsGrid.setAutoLoadData(false);
    discountsGrid.setDeleteButton(deleteButton1);
    discountsGrid.setEditButton(editButton1);
    discountsGrid.setExportButton(exportButton1);
    discountsGrid.setFunctionId("SAL03");
    discountsGrid.setMaxSortedColumns(3);
    discountsGrid.setInsertButton(insertButton1);
    discountsGrid.setNavBar(navigatorBar1);
    discountsGrid.setReloadButton(reloadButton1);
    discountsGrid.setSaveButton(saveButton1);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    this.getContentPane().add(split, BorderLayout.CENTER);
    buttonsPanel.add(labelCustomerType,      new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    buttonsPanel.add(insertButton1,       new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    buttonsPanel.add(reloadButton1,      new GridBagConstraints(8, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    buttonsPanel.add(deleteButton1,      new GridBagConstraints(9, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    buttonsPanel.add(exportButton1,      new GridBagConstraints(10, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    buttonsPanel.add(navigatorBar1,      new GridBagConstraints(11, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    buttonsPanel.add(comboBoxControl1,        new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 100, 0));
    buttonsPanel.add(saveButton1,   new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    buttonsPanel.add(editButton1,   new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 5, 5), 0, 0));
    split.add(hierarTreePanel, JSplitPane.LEFT);
    split.add(discountsGrid, JSplitPane.RIGHT);
    split.setDividerLocation(200);

    colDiscountCode.setMaxCharacters(20);
    colDiscountCode.setTrimText(true);
    colDiscountCode.setUpperCase(true);
    colDiscountCode.setColumnFilterable(true);
    colDiscountCode.setColumnName("discountCodeSAL03");
    colDiscountCode.setColumnSortable(true);
    colDiscountCode.setEditableOnInsert(true);
    colDiscountCode.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colDiscountCode.setSortingOrder(0);
    colDescr.setColumnDuplicable(true);
    colDescr.setColumnFilterable(true);
    colDescr.setColumnName("descriptionSYS10");
    colDescr.setColumnSortable(true);
    colDescr.setEditableOnEdit(true);
    colDescr.setEditableOnInsert(true);
    colDescr.setPreferredWidth(200);
    colDescr.setHeaderColumnName("discountDescription");
    colCurrencyCode.setColumnDuplicable(true);
    colCurrencyCode.setColumnFilterable(true);
    colCurrencyCode.setColumnName("currencyCodeReg03SAL03");
    colCurrencyCode.setColumnSortable(true);
    colCurrencyCode.setEditableOnEdit(true);
    colCurrencyCode.setEditableOnInsert(true);
    colCurrencyCode.setMaxCharacters(20);
    colCurrencyCode.setPreferredWidth(70);
    colMinValue.setDecimals(2);
    colMinValue.setMinValue(0.0);
    colMinValue.setColumnDuplicable(true);
    colMinValue.setColumnRequired(false);
    colMinValue.setEditableOnEdit(true);
    colMinValue.setColumnName("minValueSAL03");
    colMinValue.setEditableOnInsert(true);
    colMinValue.setPreferredWidth(80);
    colMaxValue.setDecimals(2);
    colMaxValue.setMinValue(0.0);
    colMaxValue.setColumnDuplicable(true);
    colMaxValue.setColumnRequired(false);
    colMaxValue.setEditableOnEdit(true);
    colMaxValue.setEditableOnInsert(true);
    colMaxValue.setColumnName("maxValueSAL03");
    colMaxValue.setPreferredWidth(80);
    colMinPerc.setDecimals(2);
    colMinPerc.setGrouping(false);
    colMinPerc.setMaxValue(100.0);
    colMinPerc.setMinValue(0.0);
    colMinPerc.setColumnDuplicable(true);
    colMinPerc.setColumnFilterable(false);
    colMinPerc.setColumnRequired(false);
    colMinPerc.setEditableOnEdit(true);
    colMinPerc.setEditableOnInsert(true);
    colMinPerc.setPreferredWidth(70);
    colMinPerc.setColumnName("minPercSAL03");
    colMaxPerc.setDecimals(2);
    colMaxPerc.setGrouping(false);
    colMaxPerc.setMaxValue(100.0);
    colMaxPerc.setMinValue(0.0);
    colMaxPerc.setColumnDuplicable(true);
    colMaxPerc.setColumnRequired(false);
    colMaxPerc.setEditableOnEdit(true);
    colMaxPerc.setEditableOnInsert(true);
    colMaxPerc.setPreferredWidth(70);
    colMaxPerc.setColumnName("maxPercSAL03");
    colStartDate.setColumnDuplicable(true);
    colStartDate.setColumnFilterable(true);
    colStartDate.setColumnSortable(true);
    colStartDate.setEditableOnEdit(true);
    colStartDate.setEditableOnInsert(true);
    colStartDate.setColumnName("startDateSAL03");
    colEndDate.setColumnDuplicable(true);
    colEndDate.setColumnFilterable(true);
    colEndDate.setColumnSortable(true);
    colEndDate.setEditableOnEdit(true);
    colEndDate.setEditableOnInsert(true);
    colEndDate.setColumnName("endDateSAL03");

    discountsGrid.getColumnContainer().add(colDiscountCode, null);
    discountsGrid.getColumnContainer().add(colDescr, null);
    discountsGrid.getColumnContainer().add(colCurrencyCode, null);
    discountsGrid.getColumnContainer().add(colMinValue, null);
    discountsGrid.getColumnContainer().add(colMaxValue, null);
    discountsGrid.getColumnContainer().add(colMinPerc, null);
    discountsGrid.getColumnContainer().add(colMaxPerc, null);
    discountsGrid.getColumnContainer().add(colStartDate, null);
    discountsGrid.getColumnContainer().add(colEndDate, null);

  }


  public GridControl getGrid() {
    return discountsGrid;
  }


  public ServerGridDataLocator getGridDataLocator() {
    return gridDataLocator;
  }
  public HierarTreePanel getHierarTreePanel() {
    return hierarTreePanel;
  }

}
