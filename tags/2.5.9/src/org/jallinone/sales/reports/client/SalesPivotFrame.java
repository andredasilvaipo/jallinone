package org.jallinone.sales.reports.client;

import java.math.BigDecimal;
import java.text.*;
import java.util.*;

import java.awt.*;
import javax.swing.*;

import org.openswing.swing.client.*;
import org.openswing.swing.mdi.client.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.pivottable.aggregators.java.*;
import org.openswing.swing.pivottable.client.*;
import org.openswing.swing.pivottable.functions.java.*;
import org.openswing.swing.pivottable.java.*;
import org.openswing.swing.util.client.*;
import org.jallinone.commons.client.*;
import org.openswing.swing.form.client.Form;
import org.openswing.swing.form.client.FormController;
import org.openswing.swing.util.java.Consts;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.sales.reports.java.SalesPivotVO;
import org.openswing.swing.lookup.client.*;
import org.openswing.swing.tree.client.TreeServerDataLocator;
import org.jallinone.items.java.GridItemVO;
import org.jallinone.sales.customers.java.GridCustomerVO;
import org.openswing.swing.pivottable.java.InputFilter;
import org.openswing.swing.domains.java.Domain;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.items.java.ItemTypeVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Internal frame used to show a pivot table for sales.</p>
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
public class SalesPivotFrame extends InternalFrame {




  PivotTable pivotTable = new PivotTable();
  JPanel buttonsPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  ReloadButton reloadButton1 = new ReloadButton();
  ExportButton exportButton1 = new ExportButton();
  FilterButton filterButton1 = new FilterButton();
  HashMap filters = new HashMap();
  JPanel northPanel = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  Form filterPanel = new Form();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  LabelControl labelDocType = new LabelControl();
  LabelControl labelDocState = new LabelControl();
  LabelControl labelYear = new LabelControl();
  LabelControl labelCustomer = new LabelControl();
  LabelControl labelCompany = new LabelControl();
  LabelControl labelItem = new LabelControl();
  CompaniesComboControl controlComp = new CompaniesComboControl();
  ComboBoxControl controlDocTypes = new ComboBoxControl();
  ComboBoxControl controlDocStates = new ComboBoxControl();
  NumericControl controlYear = new NumericControl();
  CodLookupControl controlCust = new CodLookupControl();
  CodLookupControl controlItem = new CodLookupControl();
  TextControl controlCustName1 = new TextControl();
  TextControl controlITemDescr = new TextControl();
  TextControl controlCustName2 = new TextControl();

  /** item code lookup controller */
  LookupController itemController = new LookupController();

  /** item code lookup data locator */
  LookupServerDataLocator itemDataLocator = new LookupServerDataLocator();

  LookupServerDataLocator levelDataLocator = new LookupServerDataLocator();
  TreeServerDataLocator treeLevelDataLocator = new TreeServerDataLocator();
  ComboBoxControl controlItemType = new ComboBoxControl();

  LookupController customerController = new LookupController();
  LookupServerDataLocator customerDataLocator = new LookupServerDataLocator();


  public SalesPivotFrame() {
    try {
      jbInit();
      setSize(700,600);
      this.setTitle(ClientSettings.getInstance().getResources().getResource("pivot table for sales"));
      init();
      
      MDIFrame.add(this);
      filterPanel.setMode(Consts.INSERT);

      Calendar cal = Calendar.getInstance();
      SalesPivotVO vo= (SalesPivotVO)filterPanel.getVOModel().getValueObject();
      controlYear.setValue(new BigDecimal(String.valueOf(cal.get(cal.YEAR))));
      vo.setDocYear((BigDecimal)controlYear.getValue());
      controlDocStates.setValue(ApplicationConsts.CLOSED);

      
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void init() {

      Response res = ClientUtils.getData("loadItemTypes",new GridParams());
      final Domain d = new Domain("ITEM_TYPES");
      if (!res.isError()) {
        ItemTypeVO vo = null;
        java.util.List list = ((VOListResponse)res).getRows();
        for(int i=0;i<list.size();i++) {
          vo = (ItemTypeVO)list.get(i);
          d.addDomainPair(vo.getProgressiveHie02ITM02(),vo.getDescriptionSYS10());
        }
      }
      controlItemType.setDomain(d);
      controlItemType.getComboBox().addItemListener(new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
          if (e.getStateChange() == e.SELECTED) {
            int selIndex = ( (JComboBox) e.getSource()).getSelectedIndex();
            Object selValue = d.getDomainPairList()[selIndex].getCode();
            treeLevelDataLocator.getTreeNodeParams().put(ApplicationConsts.PROGRESSIVE_HIE02, selValue);
          }
        }
      });


      // item code lookup...
      itemDataLocator.setGridMethodName("loadItems");
      itemDataLocator.setValidationMethodName("validateItemCode");

      controlItem.setLookupController(itemController);
      controlItem.setControllerMethodName("getItemsList");
      itemController.setForm(filterPanel);
      itemController.setLookupDataLocator(itemDataLocator);
      itemController.setFrameTitle("items");

      itemController.setCodeSelectionWindow(itemController.TREE_GRID_FRAME);
      treeLevelDataLocator.setServerMethodName("loadHierarchy");
      itemDataLocator.setTreeDataLocator(treeLevelDataLocator);
      itemDataLocator.setNodeNameAttribute("descriptionSYS10");

      itemController.setLookupValueObjectClassName("org.jallinone.items.java.GridItemVO");
      itemController.addLookup2ParentLink("itemCodeITM01", "itemCode");

      itemController.setAllColumnVisible(false);
      itemController.setVisibleColumn("itemCodeITM01", true);
      itemController.setVisibleColumn("descriptionSYS10", true);
      itemController.setPreferredWidthColumn("descriptionSYS10", 250);
      itemController.setFramePreferedSize(new Dimension(680,500));
      itemController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          if (itemController.getLookupVO()==null) {
            controlITemDescr.setText("");
          }
          else {
            GridItemVO vo = (GridItemVO)itemController.getLookupVO();
            controlITemDescr.setText(vo.getDescriptionSYS10());
          }
        }

        public void beforeLookupAction(ValueObject parentVO) {
          itemController.getLookupDataLocator().getLookupFrameParams().put(ApplicationConsts.PROGRESSIVE_HIE02,controlItemType.getValue());
          itemController.getLookupDataLocator().getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlComp.getValue());
          itemController.getLookupDataLocator().getLookupValidationParameters().put(ApplicationConsts.PROGRESSIVE_HIE02,controlItemType.getValue());
          itemController.getLookupDataLocator().getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,controlComp.getValue());
        }

        public void forceValidate() {}

      });



      // customer lookup...
      customerDataLocator.setGridMethodName("loadCustomers");
      customerDataLocator.setValidationMethodName("validateCustomerCode");

      controlCust.setLookupController(customerController);
      controlCust.setControllerMethodName("getCustomersList");
      customerController.setForm(filterPanel);
      customerController.setLookupDataLocator(customerDataLocator);
      customerController.setFrameTitle("customers");
      customerController.setLookupValueObjectClassName("org.jallinone.sales.customers.java.GridCustomerVO");
      customerController.addLookup2ParentLink("customerCodeSAL07","customerCode");
      customerController.setAllColumnVisible(false);
      customerController.setVisibleColumn("companyCodeSys01REG04", true);
      customerController.setFilterableColumn("companyCodeSys01REG04", true);
      customerController.setFilterableColumn("customerCodeSAL07", true);
      customerController.setFilterableColumn("name_1REG04", true);
      customerController.setFilterableColumn("name_2REG04", true);
      customerController.setFilterableColumn("cityREG04", true);
      customerController.setFilterableColumn("provinceREG04", true);

      customerController.setSortableColumn("companyCodeSys01REG04", true);
      customerController.setSortableColumn("customerCodeSAL07", true);
      customerController.setSortableColumn("name_1REG04", true);
      customerController.setSortableColumn("name_2REG04", true);
      customerController.setSortableColumn("cityREG04", true);
      customerController.setSortableColumn("provinceREG04", true);

      customerController.setVisibleColumn("customerCodeSAL07", true);
      customerController.setVisibleColumn("name_1REG04", true);
      customerController.setVisibleColumn("name_2REG04", true);
      customerController.setVisibleColumn("cityREG04", true);
      customerController.setVisibleColumn("provinceREG04", true);
      customerController.setVisibleColumn("countryREG04", true);
      customerController.setVisibleColumn("taxCodeREG04", true);
      customerController.setHeaderColumnName("cityREG04", "city");
      customerController.setHeaderColumnName("provinceREG04", "prov");
      customerController.setHeaderColumnName("countryREG04", "country");
      customerController.setHeaderColumnName("taxCodeREG04", "taxCode");
      customerController.setPreferredWidthColumn("name_1REG04", 200);
      customerController.setPreferredWidthColumn("name_2REG04", 150);
      customerController.setFramePreferedSize(new Dimension(750,500));
      customerController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
          if (customerController.getLookupVO()==null) {
            controlCustName1.setText("");
            controlCustName2.setText("");
          }
          else {
            GridCustomerVO vo = (GridCustomerVO)customerController.getLookupVO();
            controlCustName1.setText(vo.getName_1REG04());
            controlCustName2.setText(vo.getName_2REG04());
          }
        }

        public void beforeLookupAction(ValueObject parentVO) {
          customerDataLocator.getLookupFrameParams().put(ApplicationConsts.COMPANY_CODE_SYS01,controlComp.getValue());
          customerDataLocator.getLookupValidationParameters().put(ApplicationConsts.COMPANY_CODE_SYS01,controlComp.getValue());
        }

        public void forceValidate() {}

      });




      pivotTable.setController(new PivotTableController(){

        public Response getPivotTableModel(PivotTableParameters pars) {
          filterPanel.pull();
          SalesPivotVO vo= (SalesPivotVO)filterPanel.getVOModel().getValueObject();
          vo.setPivotPars(pars);
          pars.setInputFilter(new InputFilter());
          Response res = ClientUtils.getData("salesPivot",vo);
          if (res.isError())
            OptionPane.showMessageDialog(SalesPivotFrame.this,res.getErrorMessage(),"Error while loading data",JOptionPane.ERROR_MESSAGE);
          return res;
        }

      });

      RowField rowFieldItem = new RowField("itemCodeItm01DOC02",100);
      RowField rowFieldCustomer = new RowField("customerCodeSAL07",150);
      RowField rowFieldWarehouse = new RowField("warehouseCodeWar01DOC01",100);

      pivotTable.getAllRowFields().add(rowFieldItem);
      pivotTable.getAllRowFields().add(rowFieldCustomer);
      pivotTable.getAllRowFields().add(rowFieldWarehouse);

      pivotTable.addRowField(rowFieldWarehouse);
//      pivotTable.addRowField(rowFieldItem);

      ColumnField columnFieldYear = new ColumnField("docDateDOC01","year",new YearAggregator());
      ColumnField columnFieldQuarter = new ColumnField("docDateDOC01","quarter",new QuarterAggregator());

      pivotTable.getAllColumnFields().add(columnFieldYear);
      pivotTable.getAllColumnFields().add(columnFieldQuarter);

      pivotTable.addColumnField(columnFieldYear);
      pivotTable.addColumnField(columnFieldQuarter);

      NumberFormat nf = NumberFormat.getCurrencyInstance();
      Currency currency = Currency.getInstance(Locale.getDefault()); // to review...
      nf.setCurrency(currency);
      nf.setMaximumFractionDigits(2);
      nf.setMinimumFractionDigits(2);
      nf.setGroupingUsed(true);

      DataField dataFieldTotal = new DataField("totalDOC01",80,"sellAmount",new SumFunction()); // sum function...
      dataFieldTotal.setFormatter(nf);

      NumberFormat nf2 = NumberFormat.getIntegerInstance();

      DataField dataFieldItemQty = new DataField("qtyDOC02",80,"sellQty",new SumFunction()); // sum function...
      dataFieldItemQty.setFormatter(nf2);

      pivotTable.getAllDataFields().add(dataFieldTotal);
      pivotTable.getAllDataFields().add(dataFieldItemQty);
      pivotTable.addDataField(dataFieldTotal);


      pivotTable.setDataFieldRenderer(new DataFieldRenderer() {

        /**
         * @param currentColor current color to set
         * @param rowPath GenericNodeKey row fields path that identify current row
         * @param colPath GenericNodeKey column fields path that identify current column
         * @param value value to show in the specified cell
         * @param row current row
         * @param col current column
         * @return Color background color to set
         */
        public Color getBackgroundColor(Color currentColor,GenericNodeKey rowPath,GenericNodeKey colPath,Object value,int row,int col) {
          if (rowPath.getPath().length<pivotTable.getPivotTableParameters().getRowFields().size() ||
              colPath.getPath().length<pivotTable.getPivotTableParameters().getColumnFields().size()+1) {
            int c = 200+rowPath.getPath().length*colPath.getPath().length*5;
            return new Color(c,c,c);
          }
          return currentColor;
        }


        /**
         * @param currentColor current color to set
         * @param rowPath GenericNodeKey row fields path that identify current row
         * @param colPath GenericNodeKey column fields path that identify current column
         * @param value value to show in the specified cell
         * @param row current row
         * @param col current column
         * @return Color foreground color to set
         */
        public Color getForegroundColor(Color currentColor,GenericNodeKey rowPath,GenericNodeKey colPath,Object value,int row,int col) {
          return currentColor;
        }


        /**
         * @param currentFont current font to set
         * @param rowPath GenericNodeKey row fields path that identify current row
         * @param colPath GenericNodeKey column fields path that identify current column
         * @param value value to show in the specified cell
         * @param row current row
         * @param col current column
         * @return font to set
         */
        public Font getFont(Font currentFont,GenericNodeKey rowPath,GenericNodeKey colPath,Object value,int row,int col) {
          if (rowPath.getPath().length<pivotTable.getPivotTableParameters().getRowFields().size() ||
              colPath.getPath().length<pivotTable.getPivotTableParameters().getColumnFields().size()+1)
            return new Font(currentFont.getFontName(),Font.BOLD,currentFont.getSize());
          return currentFont;
        }

      });



      controlComp.addItemListener(new ItemListener() {

        public void itemStateChanged(ItemEvent e) {
          Object companyCodeSys01 = controlComp.getValue();
          if (companyCodeSys01==null)
            companyCodeSys01 = controlComp.getDomain().getDomainPairList()[0].getCode();

          SalesPivotVO vo= (SalesPivotVO)filterPanel.getVOModel().getValueObject();
          vo.setCompanyCode((String)companyCodeSys01);
        }

      });

  }


  private void jbInit() throws Exception {
    setAskBeforeClose(false);

    filterPanel.setFunctionId("SALES_PIVOT");
    filterPanel.setVOClassName("org.jallinone.sales.reports.java.SalesPivotVO");
    filterPanel.setFormController(new FormController());

    controlDocTypes.setAttributeName("docType");
    controlDocStates.setAttributeName("docState");
    controlYear.setAttributeName("docYear");
    controlYear.setColumns(4);
    controlYear.setMaxCharacters(4);
    controlCust.setAttributeName("customerCode");
    controlCust.setMaxCharacters(20);
    controlComp.setAttributeName("companyCode");
    controlItem.setAttributeName("itemCode");
    controlItem.setMaxCharacters(20);
    controlItemType.setAttributeName("progressiveHie02");
    controlDocStates.setDomainId("DOC01_STATES");
    controlDocTypes.setDomainId("SALE_DOC_TYPE");

    controlComp.setLinkLabel(labelCompany);
    controlComp.setFunctionCode("SALES_PIVOT");

    controlITemDescr.setEnabledOnEdit(false);
    controlITemDescr.setEnabledOnInsert(false);

    controlCustName1.setEnabledOnEdit(false);
    controlCustName1.setEnabledOnInsert(false);
    controlCustName2.setEnabledOnEdit(false);
    controlCustName2.setEnabledOnInsert(false);

    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    pivotTable.setExportButton(exportButton1);
    pivotTable.setFilterButton(filterButton1);
    pivotTable.setReloadButton(reloadButton1);
    northPanel.setLayout(gridBagLayout1);
    filterPanel.setLayout(gridBagLayout2);
    labelDocType.setLabel("docTypeDOC01");
    labelDocState.setLabel("docStateDOC01");
    labelYear.setLabel("docYearDOC01");
    labelCustomer.setLabel("customerCodeSAL07");
    labelCompany.setLabel("companyCodeSYS01");
    labelItem.setLabel("itemCodeITM01");
    this.getContentPane().add(pivotTable, BorderLayout.CENTER);
    buttonsPanel.add(reloadButton1, null);
    buttonsPanel.add(exportButton1, null);
    buttonsPanel.add(filterButton1, null);
    this.getContentPane().add(northPanel, BorderLayout.NORTH);
    northPanel.add(filterPanel,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(labelDocType,    new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(labelDocState,    new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(labelYear,    new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(labelCustomer,     new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(labelCompany,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(labelItem,  new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    northPanel.add(buttonsPanel,  new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));

    filterPanel.add(controlComp,   new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 100, 0));
    filterPanel.add(controlDocTypes,   new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlDocStates,   new GridBagConstraints(3, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 50, 0));
    filterPanel.add(controlYear,   new GridBagConstraints(5, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 20, 0));
    filterPanel.add(controlCust,   new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlItem,    new GridBagConstraints(2, 3, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlCustName1,   new GridBagConstraints(2, 2, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlCustName2,   new GridBagConstraints(4, 2, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlITemDescr,    new GridBagConstraints(3, 3, 3, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlItemType,      new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));



  }




}
