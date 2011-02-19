package org.jallinone.purchases.items.client;

import java.math.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.jallinone.commons.client.*;
import org.jallinone.commons.java.*;
import org.jallinone.items.java.*;
import org.jallinone.purchases.items.java.*;
import org.jallinone.registers.currency.java.*;
import org.jallinone.warehouse.java.*;
import org.openswing.swing.client.*;
import org.openswing.swing.domains.java.*;
import org.openswing.swing.form.client.*;
import org.openswing.swing.lookup.client.*;
import org.openswing.swing.mdi.client.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.*;
import org.openswing.swing.table.client.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.table.java.*;
import org.openswing.swing.util.client.*;
import org.openswing.swing.util.java.*;
import java.awt.geom.AffineTransform;


/**
  * <p>Title: JAllInOne ERP/CRM application</p>
  * <p>Description: Frame used for ABC classification, based on total invoice and availabilities per warehouse.</p>
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
public class ABCFrame extends InternalFrame {

  GridBagLayout gridBagLayout1 = new GridBagLayout();
  LabelControl labelStart = new LabelControl();
  LabelControl labelCurrency = new LabelControl();

  LabelControl labelItemType = new LabelControl();

  private Form filterPanel = new Form();
  BorderLayout borderLayout1 = new BorderLayout();
  CodLookupControl controlCurrency = new CodLookupControl();
  LabelControl labelWarehouse = new LabelControl();
  CodLookupControl controlWarehouse = new CodLookupControl();
  TextControl controlWareDescr = new TextControl();
  GenericButton buttonCreateABC = new GenericButton(new ImageIcon(ClientUtils.getImage("items.gif")));

  LookupController wareController = new LookupController();
  LookupServerDataLocator wareDataLocator = new LookupServerDataLocator();

  LookupController currencyController = new LookupController();
  LookupServerDataLocator currencyDataLocator = new LookupServerDataLocator();
  GridControl grid = new GridControl();
  CheckBoxColumn colSel = new CheckBoxColumn();
  TextColumn colItemCode = new TextColumn();
  TextColumn colItemDescr = new TextColumn();
  DecimalColumn colUnsol = new DecimalColumn();
  CurrencyColumn colSold = new CurrencyColumn();
  DecimalColumn colMinStock = new DecimalColumn();
  JPanel southPanel = new JPanel();
  ServerGridDataLocator gridLocator = new ServerGridDataLocator();


  private WarehouseVO warehouseVO = null;
  private CurrencyVO currVO = null;
  private java.util.List itemTypesList = null;

  ComboBoxControl controlItemType = new ComboBoxControl();
  DateControl controlStart = new DateControl();
  LabelControl labeTo = new LabelControl();
  DateControl controlTo = new DateControl();
  JPanel buttonsPanel = new JPanel();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  JButton buttonInvoiceA = new JButton();
  JButton buttonUnsoldC = new JButton();
  JButton buttonUnsoldB = new JButton();
  JButton buttonUnsoldA = new JButton();
  JButton buttonInvoiceB = new JButton();
  JButton buttonInvoiceC = new JButton();
  JToggleButton buttonAC = new JToggleButton();
  JToggleButton buttonAB = new JToggleButton();
  JToggleButton buttonBB = new JToggleButton();
  JToggleButton buttonBC = new JToggleButton();
  JToggleButton buttonCC = new JToggleButton();
  JToggleButton buttonCB = new JToggleButton();
  JToggleButton buttonCA = new JToggleButton();
  JButton buttonUnsoldBar = new JButton() {

    public void paint(Graphics g) {
      super.paint(g);
//      Graphics2D g2d = (Graphics2D)g;
//      AffineTransform at = new AffineTransform();
//      at.setToRotation(-Math.PI/2.0, getWidth()/2.0, getHeight()/2.0);
//      g2d.setTransform(at);

      int width = getWidth();
      int height = getHeight();
      String text = ClientSettings.getInstance().getResources().getResource("unsold");

      // String size
      FontMetrics fm = g.getFontMetrics();
      int shgt = fm.stringWidth( text );
      int swid = fm.getHeight();

      int x = (width-swid)/2 + fm.getAscent();
      int y = (height - shgt)/2 + shgt;

      Graphics2D g2d = (Graphics2D) g;
      g2d.translate( x, y );
      g2d.rotate( Math.toRadians(-90) );

      boolean isDown = true;

      g2d.setColor( getForeground() );
      int offset = isDown ? 1 : 0;
      g2d.drawString( text, offset, offset );
    }

  };
  JButton buttonInvoiceBar = new JButton();
  JToggleButton buttonAA = new JToggleButton();
  JToggleButton buttonBA = new JToggleButton();
  GenericButton buttonChangeMinStocks = new GenericButton(new ImageIcon(ClientUtils.getImage("warehouse.gif")));
  private BigDecimal reportId = null;
  LabelControl labelPerc1Invoiced = new LabelControl();
  LabelControl labelPerc1Unsold = new LabelControl();
  NumericControl controlPerc1Invoiced = new NumericControl();
  LabelControl labellPerc2Invoiced = new LabelControl();
  NumericControl controlPerc2Invoiced = new NumericControl();
  NumericControl controlPerc1Unsold = new NumericControl();
  NumericControl controlPerc2Unsold = new NumericControl();
  LabelControl labelPerc2Unsold = new LabelControl();
  JButton buttonAll = new JButton();
  TextColumn colABC = new TextColumn();

  private Color AA = new Color(252,183,88); // orange
  private Color AC = new Color(244,128,110); // red
  private Color CA = new Color(134,203,156); // green
  private Color CC = new Color(254,245,153); // yellow

//  private JMenuItem setToZero = new JMenuItem(ClientSettings.getInstance().getResources().getResource("set qty to zero"));


  public ABCFrame() {
    try {
      jbInit();
      setTitle(ClientSettings.getInstance().getResources().getResource("abc classification"));

      grid.setController(new GridController() {

//        /**
//         * Callback method invoked when a grid cell is selected.
//         * @param rowNumber selected row index
//         * @param columnIndex column index related to the column currently selected
//         * @param attributedName attribute name related to the column currently selected
//         * @param persistentObject v.o. related to the selected row
//         */
//        public void selectedCell(int rowNumber, int columnIndex, String attributedName, ValueObject persistentObject) {
//          setToZero.setVisible( attributedName.equals("minStockITM23") );
//        }


        /**
         * Method used to define the background color for each cell of the grid.
         * @param rowNumber selected row index
         * @param attributeName attribute name related to the column currently selected
         * @param value object contained in the selected cell
         * @return background color of the selected cell
         */
        public Color getBackgroundColor(int row,String attributeName,Object value) {
          if (attributeName.equals("abc")) {
            ABCVO vo = (ABCVO)grid.getVOListTableModel().getObjectForRow(row);
            if (vo.getAbc().equals("AA"))
              return AA;
            if (vo.getAbc().equals("AC"))
              return AC;
            if (vo.getAbc().equals("CA"))
              return CA;
            if (vo.getAbc().equals("CC"))
              return CC;
          }
          return ClientSettings.GRID_CELL_BACKGROUND;
        }


        /**
         * Callback method invoked when the data loading is completed.
         * @param error <code>true</code> if data loading has terminated with errors, <code>false</code> otherwise
         */
        public void loadDataCompleted(boolean error) {
          grid.setMode(Consts.EDIT);
        }

      });
      gridLocator.setServerMethodName("loadABC");
      grid.setGridDataLocator(gridLocator);
      grid.setShowWarnMessageBeforeReloading(false);

      init();

      filterPanel.setFormController(new FormController());
      filterPanel.setMode(Consts.INSERT);

      setSize(750,650);
      MDIFrame.add(this);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void init() {
    grid.setColorsInReadOnlyMode(false);
//    grid.addPopupCommand(setToZero);
//    setToZero.addActionListener(new ActionListener() {
//
//      public void actionPerformed(ActionEvent e) {
//        for(int i=0;i<grid.getVOListTableModel().getRowCount();i++)
//          ((ABCVO)grid.getVOListTableModel().getObjectForRow(i)).setMinStockITM23(new BigDecimal(0));
//      }
//
//    });


    setABCButtonsEnabled(false);

    Calendar cal = Calendar.getInstance();
    java.sql.Date today = new java.sql.Date(cal.getTimeInMillis());
    cal.set(cal.MONTH,0);
    cal.set(cal.DAY_OF_MONTH,1);
    java.sql.Date startYear = new java.sql.Date(cal.getTimeInMillis());

    controlStart.setValue(startYear);
    controlTo.setValue(today);

    controlPerc1Invoiced.setValue(new BigDecimal(70));
    controlPerc2Invoiced.setValue(new BigDecimal(14));
    controlPerc1Unsold.setValue(new BigDecimal(70));
    controlPerc2Unsold.setValue(new BigDecimal(14));

    // item type...
    Domain d = new Domain("ITEM_TYPES");
    controlItemType.setDomain(d);

    controlItemType.getComboBox().addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange()==e.SELECTED) {
          CreateABCFilterVO vo = (CreateABCFilterVO)filterPanel.getVOModel().getValueObject();
          vo.setProgressiveHie02ITM01((BigDecimal)controlItemType.getValue());
        }
      }
    });


    // warehouse lookup...
    wareDataLocator.setGridMethodName("loadWarehouses");
    wareDataLocator.setValidationMethodName("validateWarehouseCode");

    controlWarehouse.setLookupController(wareController);
    controlWarehouse.setControllerMethodName("getWarehousesList");
    wareController.setForm(filterPanel);
    wareController.setLookupDataLocator(wareDataLocator);
    wareController.setFrameTitle("warehouses");
    wareController.setLookupValueObjectClassName("org.jallinone.warehouse.java.WarehouseVO");
    wareController.addLookup2ParentLink("companyCodeSys01WAR01", "companyCode");
    wareController.addLookup2ParentLink("warehouseCodeWAR01", "warehouseCode");
    wareController.addLookup2ParentLink("descriptionWAR01","warehouseDescription");
    wareController.setAllColumnVisible(false);
    wareController.setVisibleColumn("warehouseCodeWAR01", true);
    wareController.setVisibleColumn("descriptionWAR01", true);
    wareController.setVisibleColumn("addressWAR01", true);
    wareController.setVisibleColumn("cityWAR01", true);
    wareController.setVisibleColumn("zipWAR01", true);
    wareController.setVisibleColumn("provinceWAR01", true);
    wareController.setVisibleColumn("countryWAR01", true);
    wareController.setPreferredWidthColumn("descriptionWAR01",200);
    wareController.setFramePreferedSize(new Dimension(750,500));
    wareController.addLookupListener(new LookupListener() {

      public void codeValidated(boolean validated) {}

      public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
        if (wareController.getLookupVO()==null) {
          warehouseVO = null;

          Domain d = new Domain("ITEM_TYPES");
          controlItemType.setDomain(d);
        }
        else {
          warehouseVO = (WarehouseVO)wareController.getLookupVO();

          GridParams gridParams = new GridParams();
          gridParams.getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,warehouseVO.getCompanyCodeSys01WAR01());
					grid.getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,warehouseVO.getCompanyCodeSys01WAR01());
          Domain d = new Domain("ITEM_TYPES");
          Response res = ClientUtils.getData("loadItemTypes",gridParams);
          if (!res.isError()) {
            ItemTypeVO vo = null;
            itemTypesList = ((VOListResponse)res).getRows();
            for(int i=0;i<itemTypesList.size();i++) {
              vo = (ItemTypeVO)itemTypesList.get(i);
              d.addDomainPair(vo.getProgressiveHie02ITM02(),vo.getDescriptionSYS10());
            }
          }
          controlItemType.setDomain(d);
          if (d.getDomainPairList().length>=1)
            controlItemType.getComboBox().setSelectedIndex(0);
          else
            controlItemType.getComboBox().setSelectedIndex(-1);

        }
      }

      public void beforeLookupAction(ValueObject parentVO) { }

      public void forceValidate() {}

    });



    // currency lookup...
    currencyDataLocator.setGridMethodName("loadCurrencies");
    currencyDataLocator.setValidationMethodName("validateCurrencyCode");
    controlCurrency.setLookupController(currencyController);
    controlCurrency.setControllerMethodName("getCurrenciesList");
    currencyController.setLookupDataLocator(currencyDataLocator);
    currencyController.setFrameTitle("currencies");
    currencyController.setLookupValueObjectClassName("org.jallinone.registers.currency.java.CurrencyVO");
    currencyController.addLookup2ParentLink("currencyCodeREG03", "currencyCodeREG03");
    currencyController.setAllColumnVisible(false);
    currencyController.setVisibleColumn("currencyCodeREG03", true);
    currencyController.setVisibleColumn("currencySymbolREG03", true);
    new CustomizedColumns(new BigDecimal(182),currencyController);
    currencyController.addLookupListener(new LookupListener() {

      public void beforeLookupAction(ValueObject parentVO) {}

      public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {
        grid.clearData();
      }

      public void codeValidated(boolean validated) { }

      public void forceValidate() { }

    });

    colSold.setDynamicSettings(new CurrencyColumnSettings() {

      public double getMaxValue(int row) {
        return Double.MAX_VALUE;
      }

      public double getMinValue(int row) {
        return 0.0;
      }

      public boolean isGrouping(int row) {
        return true;
      }

      public int getDecimals(int row) {
        currVO = (CurrencyVO)currencyController.getLookupVO();
        return currVO.getDecimalsREG03().intValue();
      }

      public String getCurrencySymbol(int row) {
        currVO = (CurrencyVO)currencyController.getLookupVO();
        return currVO.getCurrencySymbolREG03();
      }
    });

  }


  private void jbInit() throws Exception {
    super.setAskBeforeClose(false);
    grid.setAutoLoadData(false);
    grid.setValueObjectClassName("org.jallinone.purchases.items.java.ABCVO");
    filterPanel.setVOClassName("org.jallinone.purchases.items.java.CreateABCFilterVO");
    this.getContentPane().setLayout(borderLayout1);

    buttonChangeMinStocks.setButtonBehavior(Consts.BUTTON_IMAGE_AND_TEXT);
    buttonChangeMinStocks.setHorizontalTextPosition(SwingConstants.TRAILING);

    labelItemType.setText("item type");
    controlCurrency.setAttributeName("currencyCodeREG03");
    controlCurrency.setLinkLabel(labelCurrency);
    controlCurrency.setMaxCharacters(20);
    controlCurrency.setRequired(true);
    labelWarehouse.setText("warehouseCodeWAR01");
    controlWarehouse.setAttributeName("warehouseCode");
    controlWarehouse.setLinkLabel(labelWarehouse);
    controlWarehouse.setMaxCharacters(20);
    controlWarehouse.setRequired(true);
    controlWareDescr.setAttributeName("warehouseDescription");
    controlWareDescr.setEnabledOnEdit(false);
    controlWareDescr.setEnabledOnInsert(false);
    buttonCreateABC.setButtonBehavior(Consts.BUTTON_IMAGE_AND_TEXT);
    buttonCreateABC.setHorizontalTextPosition(SwingConstants.TRAILING);
    buttonCreateABC.setText("create abc");
    buttonCreateABC.setExecuteAsThread(false);
    buttonCreateABC.addActionListener(new ABCFrame_buttonCreateABC_actionAdapter(this));
    colItemDescr.setPreferredWidth(250);
    colUnsol.setHeaderColumnName("unsold");
    colUnsol.setPreferredWidth(70);
    colSold.setColumnName("sold");
    colSold.setEditableOnEdit(false);
    colSold.setHeaderColumnName("invoiced");
    colSold.setPreferredWidth(100);
    colMinStock.setColumnFilterable(true);
    colMinStock.setColumnRequired(false);
    colMinStock.setEditableOnEdit(true);
    colMinStock.setPreferredWidth(70);
    colItemCode.setColumnFilterable(true);
    colItemCode.setHeaderColumnName("itemCodeITM01");
    colItemCode.setPreferredWidth(80);
    colSel.setShowDeSelectAllInPopupMenu(true);
    colSel.setEditableOnEdit(true);
    colSel.setPreferredWidth(50);
    labeTo.setText("to date");
    labelStart.setText("from date");
    buttonsPanel.setLayout(gridBagLayout2);
    buttonInvoiceA.setFont(new java.awt.Font("Dialog", 1, 14));
    buttonInvoiceA.setBorderPainted(false);
    buttonInvoiceA.setText("A");
    buttonInvoiceA.addActionListener(new ABCFrame_buttonInvoiceA_actionAdapter(this));
    buttonUnsoldA.setText("A");
    buttonUnsoldC.setFont(new java.awt.Font("Dialog", 1, 14));
    buttonUnsoldC.setBorderPainted(false);
    buttonUnsoldC.setText("C");
    buttonUnsoldC.addActionListener(new ABCFrame_buttonUnsoldC_actionAdapter(this));
    buttonUnsoldB.setFont(new java.awt.Font("Dialog", 1, 14));
    buttonUnsoldB.setBorderPainted(false);
    buttonUnsoldB.setText("B");
    buttonUnsoldB.addActionListener(new ABCFrame_buttonUnsoldB_actionAdapter(this));
    buttonUnsoldA.setFont(new java.awt.Font("Dialog", 1, 14));
    buttonUnsoldA.setBorderPainted(false);
    buttonUnsoldA.setText("A");
    buttonUnsoldA.addActionListener(new ABCFrame_buttonUnsoldA_actionAdapter(this));
    buttonInvoiceB.setFont(new java.awt.Font("Dialog", 1, 14));
    buttonInvoiceB.setBorderPainted(false);
    buttonInvoiceB.setText("B");
    buttonInvoiceB.addActionListener(new ABCFrame_buttonInvoiceB_actionAdapter(this));
    buttonInvoiceC.setFont(new java.awt.Font("Dialog", 1, 14));
    buttonInvoiceC.setBorderPainted(false);
    buttonInvoiceC.setText("C");
    buttonInvoiceC.addActionListener(new ABCFrame_buttonInvoiceC_actionAdapter(this));
    buttonAA.setText(" ");
    buttonAB.setBorderPainted(false);
    buttonAB.setText(" ");
    buttonAB.addActionListener(new ABCFrame_buttonAB_actionAdapter(this));
    buttonAC.setText(" ");
    buttonAC.addActionListener(new ABCFrame_buttonAC_actionAdapter(this));
    buttonBA.setBorderPainted(false);
    buttonBA.setText(" ");
    buttonBA.addActionListener(new ABCFrame_buttonBA_actionAdapter(this));
    buttonBB.setBorderPainted(false);
    buttonBB.setText(" ");
    buttonBB.addActionListener(new ABCFrame_buttonBB_actionAdapter(this));
    buttonBC.setBorderPainted(false);
    buttonBC.setText(" ");
    buttonBC.addActionListener(new ABCFrame_buttonBC_actionAdapter(this));
    buttonCA.setText(" ");
    buttonCA.addActionListener(new ABCFrame_buttonCA_actionAdapter(this));
    buttonCB.setBorderPainted(false);
    buttonCB.setText(" ");
    buttonCB.addActionListener(new ABCFrame_buttonCB_actionAdapter(this));
    buttonCC.setText(" ");
    buttonCC.addActionListener(new ABCFrame_buttonCC_actionAdapter(this));
    buttonUnsoldBar.setFont(new java.awt.Font("Dialog", 1, 14));
    buttonUnsoldBar.setBorderPainted(false);
    buttonUnsoldBar.setHorizontalAlignment(SwingConstants.CENTER);
    buttonUnsoldBar.setHorizontalTextPosition(SwingConstants.TRAILING);
//    buttonUnsoldBar.setText(ClientSettings.getInstance().getResources().getResource("unsold"));
    buttonInvoiceBar.setFont(new java.awt.Font("Dialog", 1, 14));
    buttonInvoiceBar.setBorderPainted(false);
    buttonInvoiceBar.setText(ClientSettings.getInstance().getResources().getResource("invoiced"));

    buttonAC.setBackground(AC);
    buttonAC.setBorderPainted(false);
    buttonCC.setBackground(CC);
    buttonCC.setBorderPainted(false);
    buttonCA.setBackground(CA);
    buttonCA.setBorderPainted(false);
    buttonAA.setBackground(AA);
    buttonAA.setBorderPainted(false);

    buttonAA.addActionListener(new ABCFrame_buttonAA_actionAdapter(this));
    buttonChangeMinStocks.setText("change min stocks");
    buttonChangeMinStocks.addActionListener(new ABCFrame_buttonChangeMinStocks_actionAdapter(this));
    labelPerc1Invoiced.setText("perc1Invoiced");
    labelPerc1Unsold.setText("perc1Unsold");
    controlPerc1Invoiced.setAttributeName("perc1Invoiced");
    controlPerc1Invoiced.setLinkLabel(labelPerc1Invoiced);
    controlPerc1Invoiced.setMaxValue(100.0);
    controlPerc1Invoiced.setRequired(true);
    labellPerc2Invoiced.setText("perc2Invoiced");
    controlPerc2Invoiced.setAttributeName("perc2Invoiced");
    controlPerc2Invoiced.setLinkLabel(labellPerc2Invoiced);
    controlPerc2Invoiced.setMaxValue(100.0);
    controlPerc2Invoiced.setRequired(true);
    labelPerc2Unsold.setText("perc2Unsold");
    controlPerc1Unsold.setAttributeName("perc1Unsold");
    controlPerc1Unsold.setLinkLabel(labelPerc1Unsold);
    controlPerc1Unsold.setMaxValue(100.0);
    controlPerc1Unsold.setRequired(true);
    controlPerc2Unsold.setAttributeName("perc2Unsold");
    controlPerc2Unsold.setLinkLabel(labelPerc2Unsold);
    controlPerc2Unsold.setMaxValue(100.0);
    controlPerc2Unsold.setRequired(true);
    controlStart.setRequired(true);
    controlTo.setRequired(true);
    buttonAll.setToolTipText(ClientSettings.getInstance().getResources().getResource("select all"));
    buttonAll.setBorderPainted(false);
    buttonAll.setText(" ");
    buttonAll.addActionListener(new ABCFrame_buttonAll_actionAdapter(this));
    colABC.setHeaderColumnName("abc");
    colABC.setPreferredWidth(50);
    colABC.setTextAlignment(SwingConstants.CENTER);
    colABC.setColumnName("abc");
    this.getContentPane().add(filterPanel, BorderLayout.NORTH);
    filterPanel.setLayout(gridBagLayout1);
    labelCurrency.setText("currencyCodeREG03");
    filterPanel.add(labelStart,               new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(labelCurrency,                new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlCurrency,              new GridBagConstraints(1, 2, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(labelWarehouse,           new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlWarehouse,          new GridBagConstraints(1, 0, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlWareDescr,            new GridBagConstraints(4, 0, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(buttonCreateABC,              new GridBagConstraints(0, 5, 7, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 5, 5, 0), 0, 0));
    filterPanel.add(labelItemType,           new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlItemType,           new GridBagConstraints(5, 2, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlStart,          new GridBagConstraints(1, 1, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 50, 0));
    filterPanel.add(labeTo,       new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlTo,            new GridBagConstraints(5, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 80, 0));
    filterPanel.add(buttonsPanel,           new GridBagConstraints(6, 0, 1, 5, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(20, 5, 5, 5), 0, 0));
    buttonsPanel.add(buttonInvoiceA,     new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    buttonsPanel.add(buttonUnsoldC,      new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    buttonsPanel.add(buttonUnsoldB,      new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    buttonsPanel.add(buttonUnsoldA,      new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    buttonsPanel.add(buttonInvoiceB,    new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    buttonsPanel.add(buttonInvoiceC,    new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    buttonsPanel.add(buttonAC,    new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    buttonsPanel.add(buttonAB,    new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    buttonsPanel.add(buttonAA,  new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    buttonsPanel.add(buttonBA,  new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    buttonsPanel.add(buttonBB,    new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    buttonsPanel.add(buttonBC,    new GridBagConstraints(4, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    buttonsPanel.add(buttonCC,    new GridBagConstraints(4, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    buttonsPanel.add(buttonCB,    new GridBagConstraints(3, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    buttonsPanel.add(buttonCA,    new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    buttonsPanel.add(buttonInvoiceBar,     new GridBagConstraints(2, 0, 3, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    buttonsPanel.add(buttonUnsoldBar,     new GridBagConstraints(0, 2, 1, 3, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));
    buttonsPanel.add(buttonAll,  new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    filterPanel.add(labelPerc1Invoiced,      new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(labelPerc1Unsold,      new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlPerc1Invoiced,     new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    filterPanel.add(controlPerc1Unsold,     new GridBagConstraints(1, 4, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    filterPanel.add(labellPerc2Invoiced,   new GridBagConstraints(2, 3, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(labelPerc2Unsold,  new GridBagConstraints(3, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlPerc2Invoiced, new GridBagConstraints(4, 3, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    filterPanel.add(controlPerc2Unsold, new GridBagConstraints(4, 4, 2, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.getContentPane().add(grid, BorderLayout.CENTER);

    controlStart.setAttributeName("startDate");
    controlTo.setAttributeName("endDate");

    colSel.setColumnName("selected");
    colItemCode.setColumnName("itemCode");
    colItemDescr.setColumnName("itemDescription");
    colMinStock.setColumnName("minStockITM23");
    colUnsol.setColumnName("unsold");

    buttonAA.setToolTipText(ClientSettings.getInstance().getResources().getResource("abc aa"));
    buttonAC.setToolTipText(ClientSettings.getInstance().getResources().getResource("abc ac"));
    buttonCA.setToolTipText(ClientSettings.getInstance().getResources().getResource("abc ca"));
    buttonCC.setToolTipText(ClientSettings.getInstance().getResources().getResource("abc cc"));

    grid.getColumnContainer().add(colSel, null);
    grid.getColumnContainer().add(colItemCode, null);
    grid.getColumnContainer().add(colItemDescr, null);
    grid.getColumnContainer().add(colMinStock, null);
    grid.getColumnContainer().add(colUnsol, null);
    grid.getColumnContainer().add(colSold, null);
    grid.getColumnContainer().add(colABC, null);
    this.getContentPane().add(southPanel,  BorderLayout.SOUTH);
    southPanel.add(buttonChangeMinStocks, null);
  }


  void buttonCreateABC_actionPerformed(ActionEvent e) {
    if (!filterPanel.push()) {
//      OptionPane.showMessageDialog(MDIFrame.getInstance(),"you have to fill in all filtering conditions","Attention",JOptionPane.WARNING_MESSAGE);
    }
    else {
      CreateABCFilterVO vo = (CreateABCFilterVO)filterPanel.getVOModel().getValueObject();
      vo.setProgressiveHie02ITM01((BigDecimal)controlItemType.getValue());

      vo.setReportId(reportId);
      Response res = ClientUtils.getData("createABC",vo);
      if (res.isError()) {
        setABCButtonsEnabled(false);
        grid.clearData();
        OptionPane.showMessageDialog(MDIFrame.getInstance(),res.getErrorMessage(),"Error",JOptionPane.ERROR_MESSAGE);
      }
      else {
        reportId = (BigDecimal)((VOResponse)res).getVo();
        setABCButtonsEnabled(true);
        buttonAll_actionPerformed(null);
        reloadGrid();
      }
    }
  }


  public GridControl getGrid() {
    return grid;
  }

  void buttonAA_actionPerformed(ActionEvent e) {
    reloadGrid();
  }

  void buttonAB_actionPerformed(ActionEvent e) {
    reloadGrid();
  }

  void buttonAC_actionPerformed(ActionEvent e) {
    reloadGrid();
  }

  void buttonBA_actionPerformed(ActionEvent e) {
    reloadGrid();
  }

  void buttonBB_actionPerformed(ActionEvent e) {
    reloadGrid();
  }

  void buttonBC_actionPerformed(ActionEvent e) {
    reloadGrid();
  }

  void buttonCA_actionPerformed(ActionEvent e) {
    reloadGrid();
  }

  void buttonCB_actionPerformed(ActionEvent e) {
    reloadGrid();
  }

  void buttonCC_actionPerformed(ActionEvent e) {
    reloadGrid();
  }

  void buttonChangeMinStocks_actionPerformed(ActionEvent e) {
    ABCVO vo = null;
    ArrayList rows = new ArrayList();
    for(int i=0;i<grid.getVOListTableModel().getRowCount();i++) {
      vo = (ABCVO)grid.getVOListTableModel().getObjectForRow(i);
      if (vo.isSelected())
        rows.add(vo);
    }
    if (rows.size()==0)
      OptionPane.showMessageDialog(MDIFrame.getInstance(),"you have to select at least one row","Attention",JOptionPane.WARNING_MESSAGE);
    else {
      Response res = ClientUtils.getData("updateMinStocks",rows);
      if (res.isError())
        OptionPane.showMessageDialog(MDIFrame.getInstance(),res.getErrorMessage(),"Error while saving",JOptionPane.ERROR_MESSAGE);
      else
        OptionPane.showMessageDialog(MDIFrame.getInstance(),"min stocks updated","update completed",JOptionPane.INFORMATION_MESSAGE);
    }
  }


  private void setABCButtonsEnabled(boolean enabled) {
    buttonAA.setEnabled(enabled);
    buttonAB.setEnabled(enabled);
    buttonAC.setEnabled(enabled);
    buttonBA.setEnabled(enabled);
    buttonBB.setEnabled(enabled);
    buttonBC.setEnabled(enabled);
    buttonCA.setEnabled(enabled);
    buttonCB.setEnabled(enabled);
    buttonCC.setEnabled(enabled);
  }


  /**
   * Callback method invoked by closeFrame.
   * @return <code>true</code> allows the closing operation to continue, <code>false</code> the closing operation will be interrupted
   */
  protected boolean beforeCloseFrame() {
    if (reportId!=null)
      ClientUtils.getData("deleteABC",reportId);
    return true;
  }


  private void reloadGrid() {
    grid.clearData();
    ArrayList mask = new ArrayList();
    if (buttonAA.isSelected())
      mask.add("AA");
    if (buttonAB.isSelected())
      mask.add("AB");
    if (buttonAC.isSelected())
      mask.add("AC");
    if (buttonBA.isSelected())
      mask.add("BA");
    if (buttonBB.isSelected())
      mask.add("BB");
    if (buttonBC.isSelected())
      mask.add("BC");
    if (buttonCA.isSelected())
      mask.add("CA");
    if (buttonCB.isSelected())
      mask.add("CB");
    if (buttonCC.isSelected())
      mask.add("CC");
    grid.getOtherGridParams().put(ApplicationConsts.FILTER_VO,mask);
    grid.getOtherGridParams().put(ApplicationConsts.REPORT_ID,reportId);
    grid.reloadData();
  }

  void buttonInvoiceA_actionPerformed(ActionEvent e) {
    buttonAA.setSelected(true);
    buttonBA.setSelected(true);
    buttonCA.setSelected(true);
    reloadGrid();
  }

  void buttonInvoiceB_actionPerformed(ActionEvent e) {
    buttonAB.setSelected(true);
    buttonBB.setSelected(true);
    buttonCB.setSelected(true);
    reloadGrid();
  }

  void buttonInvoiceC_actionPerformed(ActionEvent e) {
    buttonAC.setSelected(true);
    buttonBC.setSelected(true);
    buttonCC.setSelected(true);
    reloadGrid();
  }

  void buttonUnsoldA_actionPerformed(ActionEvent e) {
    buttonAA.setSelected(true);
    buttonAB.setSelected(true);
    buttonAC.setSelected(true);
    reloadGrid();
  }

  void buttonUnsoldB_actionPerformed(ActionEvent e) {
    buttonBA.setSelected(true);
    buttonBB.setSelected(true);
    buttonBC.setSelected(true);
    reloadGrid();
  }

  void buttonUnsoldC_actionPerformed(ActionEvent e) {
    buttonCA.setSelected(true);
    buttonCB.setSelected(true);
    buttonCC.setSelected(true);
    reloadGrid();
  }

  void buttonAll_actionPerformed(ActionEvent e) {
    buttonAA.setSelected(true);
    buttonAB.setSelected(true);
    buttonAC.setSelected(true);
    buttonBA.setSelected(true);
    buttonBB.setSelected(true);
    buttonBC.setSelected(true);
    buttonCA.setSelected(true);
    buttonCB.setSelected(true);
    buttonCC.setSelected(true);
    reloadGrid();
  }



}

class ABCFrame_buttonCreateABC_actionAdapter implements java.awt.event.ActionListener {
  ABCFrame adaptee;

  ABCFrame_buttonCreateABC_actionAdapter(ABCFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.buttonCreateABC_actionPerformed(e);
  }
}

class ABCFrame_buttonAA_actionAdapter implements java.awt.event.ActionListener {
  ABCFrame adaptee;

  ABCFrame_buttonAA_actionAdapter(ABCFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.buttonAA_actionPerformed(e);
  }
}

class ABCFrame_buttonAB_actionAdapter implements java.awt.event.ActionListener {
  ABCFrame adaptee;

  ABCFrame_buttonAB_actionAdapter(ABCFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.buttonAB_actionPerformed(e);
  }
}

class ABCFrame_buttonAC_actionAdapter implements java.awt.event.ActionListener {
  ABCFrame adaptee;

  ABCFrame_buttonAC_actionAdapter(ABCFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.buttonAC_actionPerformed(e);
  }
}

class ABCFrame_buttonBA_actionAdapter implements java.awt.event.ActionListener {
  ABCFrame adaptee;

  ABCFrame_buttonBA_actionAdapter(ABCFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.buttonBA_actionPerformed(e);
  }
}

class ABCFrame_buttonBB_actionAdapter implements java.awt.event.ActionListener {
  ABCFrame adaptee;

  ABCFrame_buttonBB_actionAdapter(ABCFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.buttonBB_actionPerformed(e);
  }
}

class ABCFrame_buttonBC_actionAdapter implements java.awt.event.ActionListener {
  ABCFrame adaptee;

  ABCFrame_buttonBC_actionAdapter(ABCFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.buttonBC_actionPerformed(e);
  }
}

class ABCFrame_buttonCA_actionAdapter implements java.awt.event.ActionListener {
  ABCFrame adaptee;

  ABCFrame_buttonCA_actionAdapter(ABCFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.buttonCA_actionPerformed(e);
  }
}

class ABCFrame_buttonCB_actionAdapter implements java.awt.event.ActionListener {
  ABCFrame adaptee;

  ABCFrame_buttonCB_actionAdapter(ABCFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.buttonCB_actionPerformed(e);
  }
}

class ABCFrame_buttonCC_actionAdapter implements java.awt.event.ActionListener {
  ABCFrame adaptee;

  ABCFrame_buttonCC_actionAdapter(ABCFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.buttonCC_actionPerformed(e);
  }
}

class ABCFrame_buttonChangeMinStocks_actionAdapter implements java.awt.event.ActionListener {
  ABCFrame adaptee;

  ABCFrame_buttonChangeMinStocks_actionAdapter(ABCFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.buttonChangeMinStocks_actionPerformed(e);
  }
}

class ABCFrame_buttonInvoiceA_actionAdapter implements java.awt.event.ActionListener {
  ABCFrame adaptee;

  ABCFrame_buttonInvoiceA_actionAdapter(ABCFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.buttonInvoiceA_actionPerformed(e);
  }
}

class ABCFrame_buttonInvoiceB_actionAdapter implements java.awt.event.ActionListener {
  ABCFrame adaptee;

  ABCFrame_buttonInvoiceB_actionAdapter(ABCFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.buttonInvoiceB_actionPerformed(e);
  }
}

class ABCFrame_buttonInvoiceC_actionAdapter implements java.awt.event.ActionListener {
  ABCFrame adaptee;

  ABCFrame_buttonInvoiceC_actionAdapter(ABCFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.buttonInvoiceC_actionPerformed(e);
  }
}

class ABCFrame_buttonUnsoldA_actionAdapter implements java.awt.event.ActionListener {
  ABCFrame adaptee;

  ABCFrame_buttonUnsoldA_actionAdapter(ABCFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.buttonUnsoldA_actionPerformed(e);
  }
}

class ABCFrame_buttonUnsoldB_actionAdapter implements java.awt.event.ActionListener {
  ABCFrame adaptee;

  ABCFrame_buttonUnsoldB_actionAdapter(ABCFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.buttonUnsoldB_actionPerformed(e);
  }
}

class ABCFrame_buttonUnsoldC_actionAdapter implements java.awt.event.ActionListener {
  ABCFrame adaptee;

  ABCFrame_buttonUnsoldC_actionAdapter(ABCFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.buttonUnsoldC_actionPerformed(e);
  }
}

class ABCFrame_buttonAll_actionAdapter implements java.awt.event.ActionListener {
  ABCFrame adaptee;

  ABCFrame_buttonAll_actionAdapter(ABCFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.buttonAll_actionPerformed(e);
  }
}
