package org.jallinone.system.importdata.client;

import org.openswing.swing.mdi.client.InternalFrame;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;
import org.openswing.swing.client.*;
import java.awt.event.*;
import org.openswing.swing.util.java.Consts;
import org.openswing.swing.domains.java.Domain;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.form.client.FormController;
import org.openswing.swing.form.client.Form;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.table.java.ServerGridDataLocator;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.items.java.ItemTypeVO;
import org.openswing.swing.message.receive.java.VOListResponse;
import java.math.BigDecimal;
import org.jallinone.system.importdata.java.*;
import org.openswing.swing.domains.java.DomainPair;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.io.*;
import java.util.ArrayList;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.message.receive.java.VOResponse;
import org.jallinone.commons.client.ClientApplet;
import org.jallinone.system.java.ButtonCompanyAuthorizations;
import org.jallinone.commons.client.ApplicationClientFacade;
import org.jallinone.sales.pricelist.java.PricelistVO;
import org.jallinone.purchases.items.java.SupplierItemVO;
import org.jallinone.purchases.suppliers.java.GridSupplierVO;
import org.jallinone.purchases.pricelist.java.SupplierPricelistVO;

/**
  * <p>Title: JAllInOne ERP/CRM application</p>
  * <p>Description: Frame used for data import.</p>
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
public class ETLProcessFrame extends InternalFrame {

  Form mainPanel = new Form();
  JPanel settingsPanel = new JPanel();
  JPanel schedPanel = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  TitledBorder titledBorder1;
  TitledBorder titledBorder2;
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  LabelControl labelFormat = new LabelControl();
  ComboBoxControl controlFormat = new ComboBoxControl();
  ButtonGroup buttonGroup1 = new ButtonGroup();
  RadioButtonControl controlEachWeek = new RadioButtonControl();
  RadioButtonControl controlEachDay = new RadioButtonControl();
  LabelControl labelStartDate = new LabelControl();
  DateControl controlStartDate = new DateControl();
  RadioButtonControl controlNoSched = new RadioButtonControl();
  ButtonGroup buttonGroup2 = new ButtonGroup();
  JPanel groupPanel = new JPanel();
  GridBagLayout gridBagLayout3 = new GridBagLayout();
  JPanel timePanel = new JPanel();
  GridBagLayout gridBagLayout4 = new GridBagLayout();
  private final static String XLS = "XLS";
  private final static String CSV1 = "CSV1";
  private final static String CSV2 = "CSV2";
  private final static String TXT = "TXT";
  GridControl grid = new GridControl();
  TextColumn colLabel = new TextColumn();
  IntegerColumn colPos = new IntegerColumn();
  IntegerColumn colStart = new IntegerColumn();
  IntegerColumn colEnd = new IntegerColumn();
  LabelControl labelLocalFile = new LabelControl();
  GenericButton buttonLocalFile = new GenericButton(new ImageIcon(ClientUtils.getImage("localfile.gif")));
  TextControl controlLocalFile = new TextControl();
  LabelControl labelRemoteFile = new LabelControl();
  GenericButton buttonRemoteFile = new GenericButton(new ImageIcon(ClientUtils.getImage("remotefile.gif")));
  TextControl controlRemoteFile = new TextControl();
  LabelControl labelDescr = new LabelControl();
  TextAreaControl controlDescr = new TextAreaControl();
  ServerGridDataLocator gridLocator = new ServerGridDataLocator();
  JPanel buttonsPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  InsertButton insertButton1 = new InsertButton();
  EditButton editButton1 = new EditButton();
  SaveButton saveButton1 = new SaveButton();
  ReloadButton reloadButton1 = new ReloadButton();
  CheckBoxColumn colSel = new CheckBoxColumn();
  LabelControl labelClassName = new LabelControl();
  ComboBoxControl controlImportType = new ComboBoxControl();
  GenericButton buttonExecNow = new GenericButton(new ImageIcon(ClientUtils.getImage("params.gif")));
  JPanel previewPanel = new JPanel();
  TitledBorder titledBorder3;
  GridBagLayout gridBagLayout5 = new GridBagLayout();
  GridBagLayout gridBagLayout6 = new GridBagLayout();
  GenericButton buttonPreview = new GenericButton(new ImageIcon(ClientUtils.getImage("preview.gif")));
  JScrollPane scrollPane = new JScrollPane();
  PreviewPanel prePanel = new PreviewPanel(grid);
  ComboColumn colDateFormat = new ComboColumn();
  GenericButton buttonRemoveLocalFile = new GenericButton(new ImageIcon(ClientUtils.getImage("remove.gif")));
  GenericButton buttonRemoveRemoteFile = new GenericButton(new ImageIcon(ClientUtils.getImage("remove.gif")));
  LabelControl labelSep = new LabelControl();
  TextControl controlSep = new TextControl();
  JPanel localPanel = new JPanel();
  JPanel remotePanel = new JPanel();
  FlowLayout flowLayout2 = new FlowLayout();
  FlowLayout flowLayout3 = new FlowLayout();


  public ETLProcessFrame(FormController formController) {
    try {
      jbInit();

      mainPanel.setFormController(formController);
      mainPanel.setVOClassName("org.jallinone.system.importdata.java.ETLProcessVO");
      grid.setValueObjectClassName("org.jallinone.system.importdata.java.SelectableFieldVO");
      gridLocator.setServerMethodName("loadSelectableFields");
      grid.setGridDataLocator(gridLocator);
      grid.setController(new ETLProcessFieldsController(this));
      grid.setShowWarnMessageBeforeReloading(false);
      grid.setColorsInReadOnlyMode(false);

      init();

      setSize(750,620);
      MDIFrame.add(this);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void init() {
    // define date formats...
    Domain df = new Domain("DATE_FORMAT");
    df.addDomainPair("dd-MM-yyyy","dd-MM-yyyy");
    df.addDomainPair("dd/MM/yyyy","dd/MM/yyyy");
    df.addDomainPair("ddMMyyyy","ddMMyyyy");
    df.addDomainPair("yyyy-MM-dd","yyyy-MM-dd");
    df.addDomainPair("yyyy/MM/dd","yyyy/MM/dd");
    df.addDomainPair("yyyyMMdd","yyyyMMdd");
    colDateFormat.setDomain(df);


    // define the domain about data import type:
    // - add all item types
    Domain dit = new Domain("DATA_IMPORT_TYPE");
    int pos = 0;
    DataImportType ditVO = null;
    java.util.List itemTypesList = null;
    Response res = ClientUtils.getData("loadItemTypes",new GridParams());
    if (!res.isError()) {
      ItemTypeVO vo = null;
      itemTypesList = ((VOListResponse)res).getRows();
      for(int i=0;i<itemTypesList.size();i++) {
        vo = (ItemTypeVO)itemTypesList.get(i);
        ditVO = new DataImportType();
        ditVO.comboItemIndex = pos++;
        ditVO.progressiveHIE02 = vo.getProgressiveHie02ITM02();
        ditVO.subTypeCode = vo.getProgressiveHie02ITM02();
        ditVO.className = ImportItemsDescriptorVO.class.getName();
        ditVO.companyCodeSys01 = vo.getCompanyCodeSys01ITM02();

        dit.addDomainPair(
          ditVO,
          ClientSettings.getInstance().getResources().getResource("import")+" "+vo.getDescriptionSYS10()
        );
      }
    }

    // add to the domain about data import type:
    // - customers
    ClientApplet applet = ( (ApplicationClientFacade) MDIFrame.getInstance().getClientFacade()).getMainClass();
    ButtonCompanyAuthorizations bca = applet.getAuthorizations().getCompanyBa();
    ArrayList companiesList = bca.getCompaniesList("SAL07");
    for(int i=0;i<companiesList.size();i++) {
      if (applet.getAuthorizations().getCompanyBa().isInsertEnabled("SAL07",companiesList.get(i).toString())) {
        ditVO = new DataImportType();
        ditVO.comboItemIndex = pos++;
        ditVO.className = ImportCustomersDescriptorVO.class.getName();
        ditVO.companyCodeSys01 = companiesList.get(i).toString();
        dit.addDomainPair(
          ditVO,
          ClientSettings.getInstance().getResources().getResource("import")+" "+ClientSettings.getInstance().getResources().getResource("customers")+" "+companiesList.get(i).toString()
        );
      }
    }


    // add to the domain about data import type:
    // - sale pricelists
    res = ClientUtils.getData("loadPricelists",new GridParams());
    if (!res.isError()) {
      PricelistVO vo = null;
      java.util.List list = ((VOListResponse)res).getRows();
      for(int i=0;i<list.size();i++) {
        vo = (PricelistVO)list.get(i);
        ditVO = new DataImportType();
        ditVO.comboItemIndex = pos++;
        ditVO.subTypeCode = vo.getPricelistCodeSAL01();
        ditVO.className = ImportSalePricesDescriptorVO.class.getName();
        ditVO.companyCodeSys01 = vo.getCompanyCodeSys01SAL01();

        dit.addDomainPair(
          ditVO,
          ClientSettings.getInstance().getResources().getResource("import sale prices")+" "+
          ClientSettings.getInstance().getResources().getResource("for pricelist")+" "+vo.getDescriptionSYS10()
        );
      }
    }

    // add to the domain about data import type:
    // - supplier items
    res = ClientUtils.getData("loadSuppliers",new GridParams());
    if (!res.isError()) {
      GridSupplierVO vo = null;
      ItemTypeVO itemTypeVO = null;
      java.util.List list = ((VOListResponse)res).getRows();
      for(int i=0;i<list.size();i++) {
        vo = (GridSupplierVO)list.get(i);
        for(int j=0;j<itemTypesList.size();j++) {
          itemTypeVO = (ItemTypeVO)itemTypesList.get(j);
          ditVO = new DataImportType();
          ditVO.comboItemIndex = pos++;
          ditVO.subTypeCode = vo.getProgressiveREG04();
          ditVO.subTypeCode2 = itemTypeVO.getProgressiveHie02ITM02();
          ditVO.progressiveHIE02 = itemTypeVO.getProgressiveHie02ITM02();
          ditVO.className = ImportSupplierItemsDescriptorVO.class.getName();
          ditVO.companyCodeSys01 = vo.getCompanyCodeSys01REG04();

          dit.addDomainPair(
            ditVO,
            ClientSettings.getInstance().getResources().getResource("import")+" "+
            itemTypeVO.getDescriptionSYS10()+" "+
            ClientSettings.getInstance().getResources().getResource("for supplier")+" "+
            vo.getName_1REG04()
          );
        }
      }
    }

    // add to the domain about data import type:
    // - supplier prices
    res = ClientUtils.getData("loadSupplierPricelists",new GridParams());
    if (!res.isError()) {
      SupplierPricelistVO vo = null;
      java.util.List list = ((VOListResponse)res).getRows();
      for(int i=0;i<list.size();i++) {
        vo = (SupplierPricelistVO)list.get(i);
        ditVO = new DataImportType();
        ditVO.comboItemIndex = pos++;
        ditVO.subTypeCode = vo.getPricelistCodePUR03();
        ditVO.subTypeCode2 = vo.getProgressiveReg04PUR03();
        ditVO.className = ImportSupplierPricesDescriptorVO.class.getName();
        ditVO.companyCodeSys01 = vo.getCompanyCodeSys01PUR03();

        dit.addDomainPair(
          ditVO,
          ClientSettings.getInstance().getResources().getResource("import prices")+" "+
          ClientSettings.getInstance().getResources().getResource("for supplier")+" "+
          vo.getName_1REG04()+" "+
          ClientSettings.getInstance().getResources().getResource("in pricelist")+" "+vo.getDescriptionSYS10()
        );
      }
    }

    controlImportType.setDomain(dit);
    controlImportType.addItemListener(new ItemListener() {

      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange()==e.SELECTED) {
          DataImportType vo = (DataImportType)controlImportType.getValue();
          ETLProcessVO processVO = (ETLProcessVO)mainPanel.getVOModel().getValueObject();
          processVO.setCompanyCodeSys01SYS23(vo.companyCodeSys01);
          processVO.setClassNameSYS23(vo.className);
          processVO.setProgressiveHIE02(vo.progressiveHIE02);
          processVO.setSubTypeValueSYS23(vo.subTypeCode);
          processVO.setSubTypeValue2SYS23(vo.subTypeCode2);
          grid.clearData();
          grid.getOtherGridParams().put(ApplicationConsts.FILTER_VO,processVO);
          grid.reloadData();
          grid.setMode(Consts.EDIT);
          controlDescr.setValue(controlImportType.getComboBox().getSelectedItem());
        }
      }

    });


    Domain d = new Domain("FILE_TYPES");
    d.addDomainPair(XLS,"Excel");
    d.addDomainPair(CSV1,"CSV(;)");
    d.addDomainPair(CSV2,"CSV(,)");
    d.addDomainPair(TXT,"Txt file");
    controlFormat.setDomain(d);
    controlFormat.addItemListener(new ItemListener() {

      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange()==e.SELECTED) {
          ETLProcessVO vo = (ETLProcessVO)getMainPanel().getVOModel().getValueObject();
          vo.setLocalFile(null);
          controlLocalFile.setText("");
          controlRemoteFile.setText("");
        }
      }

    });
  }


  private void jbInit() throws Exception {
    controlSep.setAttributeName("levelsSepSYS23");
    titledBorder3 = new TitledBorder("");
    buttonExecNow.setEnabled(false);
    buttonExecNow.setToolTipText(ClientSettings.getInstance().getResources().getResource("start process now"));
    colLabel.setColumnName("label");
    colSel.setShowDeSelectAllInPopupMenu(true);
    colLabel.setHeaderColumnName("description");
    colLabel.setPreferredWidth(150);
    colStart.setColumnName("field.startPosSYS24");
    colEnd.setColumnName("field.endPosSYS24");
    colPos.setColumnName("field.posSYS24");
    colStart.setHeaderColumnName("field.start");
    colEnd.setHeaderColumnName("field.end");
    colPos.setHeaderColumnName("field.pos");
    colStart.setColumnRequired(false);
    colEnd.setColumnRequired(false);
    colPos.setColumnRequired(false);
    colSel.setColumnName("selected");
    colSel.setEditableOnEdit(true);
    colStart.setEditableOnEdit(true);
    colEnd.setEditableOnEdit(true);
    colPos.setEditableOnEdit(true);
    colSel.setDeSelectAllCells(true);
    controlStartDate.setDateType(Consts.TYPE_DATE_TIME);
    controlStartDate.setAttributeName("startTimeSYS23");
    titledBorder1 = new TitledBorder("");
    titledBorder2 = new TitledBorder("");
    this.setTitle(ClientSettings.getInstance().getResources().getResource("etl process"));
    mainPanel.setLayout(gridBagLayout5);
    schedPanel.setLayout(gridBagLayout1);
    settingsPanel.setBorder(titledBorder1);
    settingsPanel.setLayout(gridBagLayout2);
    titledBorder1.setTitle(ClientSettings.getInstance().getResources().getResource("import settings"));
    titledBorder1.setTitleColor(Color.blue);
    titledBorder2.setTitle(ClientSettings.getInstance().getResources().getResource("scheduler settings"));
    titledBorder2.setTitleColor(Color.blue);
    schedPanel.setBorder(titledBorder2);
    labelFormat.setLabel("file format");
    controlEachWeek.setText("each week");
    controlEachWeek.setSelectedValue(ApplicationConsts.SCHEDULING_TYPE_EVERY_WEEK);
    controlEachWeek.addItemListener(new ETLProcessFrame_controlEachWeek_itemAdapter(this));
    controlEachDay.setText("each day");
    controlEachDay.setSelectedValue(ApplicationConsts.SCHEDULING_TYPE_EVERY_DAY);
    controlEachDay.addItemListener(new ETLProcessFrame_controlEachDay_itemAdapter(this));
    labelStartDate.setLabel("start date");
    controlNoSched.setSelectedValue(ApplicationConsts.SCHEDULING_TYPE_NO_SCHED);
    controlNoSched.setSelected(true);
    controlNoSched.setText("no scheduling");
    controlNoSched.addItemListener(new ETLProcessFrame_controlNoSched_itemAdapter(this));
    groupPanel.setBorder(BorderFactory.createEtchedBorder());
    groupPanel.setLayout(gridBagLayout3);
    timePanel.setLayout(gridBagLayout4);
    grid.setAutoLoadData(false);
    colPos.setPreferredWidth(40);
    colStart.setPreferredWidth(50);
    colEnd.setPreferredWidth(50);
    labelLocalFile.setLabel("local file");
    buttonLocalFile.setText("...");
    buttonLocalFile.addActionListener(new ETLProcessFrame_buttonLocalFile_actionAdapter(this));
    labelRemoteFile.setLabel("remote file");
    buttonRemoteFile.setText("...");
    buttonRemoteFile.addActionListener(new ETLProcessFrame_buttonRemoteFile_actionAdapter(this));
    controlLocalFile.setEnabled(false);
    controlLocalFile.setEnabledOnInsert(false);
    controlLocalFile.setEnabledOnEdit(false);
    controlRemoteFile.setAttributeName("filenameSYS23");
    controlRemoteFile.setMaxCharacters(255);
    controlRemoteFile.setEnabled(false);
    controlRemoteFile.setEnabledOnInsert(false);
    controlRemoteFile.setEnabledOnEdit(false);
    controlFormat.setAttributeName("fileFormatSYS23");
    controlFormat.setEnabledOnEdit(false);
    labelDescr.setLabel("description");
    controlDescr.setAttributeName("descriptionSYS23");
    controlDescr.setRequired(true);
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    mainPanel.setEditButton(editButton1);
    mainPanel.setInsertButton(insertButton1);
    mainPanel.setReloadButton(reloadButton1);
    mainPanel.setSaveButton(saveButton1);
    colSel.setHeaderColumnName("sel.");
    colSel.setPreferredWidth(30);
    labelClassName.setLabel("import type");
    labelClassName.setText("data import type");
    controlImportType.setRequired(true);
    controlImportType.setEnabledOnEdit(false);
//    controlImportType.setAttributeName("classNameSYS23");
    buttonExecNow.setExecuteAsThread(true);
    buttonExecNow.addActionListener(new ETLProcessFrame_buttonExecNow_actionAdapter(this));
    previewPanel.setBorder(titledBorder3);
    previewPanel.setLayout(gridBagLayout6);
    titledBorder3.setTitleColor(Color.blue);
    titledBorder3.setTitle(ClientSettings.getInstance().getResources().getResource("file preview"));
    buttonPreview.addActionListener(new ETLProcessFrame_buttonPreview_actionAdapter(this));
    colDateFormat.setEditableOnEdit(true);
    colDateFormat.setColumnName("field.dateFormatSYS24");
    colDateFormat.setColumnRequired(false);
    buttonRemoveRemoteFile.addActionListener(new ETLProcessFrame_buttonRemoveRemoteFile_actionAdapter(this));
    buttonRemoveLocalFile.addActionListener(new ETLProcessFrame_buttonRemoveLocalFile_actionAdapter(this));
    scrollPane.setPreferredSize(new Dimension(100, 30));
    prePanel.setOpaque(true);
    labelSep.setText("levels separator");
    localPanel.setLayout(flowLayout2);
    flowLayout2.setAlignment(FlowLayout.LEFT);
    remotePanel.setLayout(flowLayout3);
    flowLayout3.setAlignment(FlowLayout.LEFT);
    this.getContentPane().add(mainPanel,  BorderLayout.WEST);
    mainPanel.add(settingsPanel,   new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    mainPanel.add(schedPanel,   new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    settingsPanel.add(labelFormat,              new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    settingsPanel.add(controlFormat,                new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    settingsPanel.add(labelLocalFile,            new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    buttonGroup2.add(controlNoSched);
    buttonGroup2.add(controlEachDay);
    buttonGroup2.add(controlEachWeek);
    controlNoSched.setButtonGroup(buttonGroup2);
    controlEachDay.setButtonGroup(buttonGroup2);
    controlEachWeek.setButtonGroup(buttonGroup2);

    controlNoSched.setAttributeName("schedulingTypeSYS23");
    controlEachDay.setAttributeName("schedulingTypeSYS23");
    controlEachWeek.setAttributeName("schedulingTypeSYS23");

    schedPanel.add(groupPanel,       new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    groupPanel.add(controlNoSched,     new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    groupPanel.add(controlEachWeek,   new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    groupPanel.add(controlEachDay,  new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    schedPanel.add(timePanel,      new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    timePanel.add(labelStartDate,    new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    timePanel.add(controlStartDate,     new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    this.getContentPane().add(grid,  BorderLayout.CENTER);
    grid.getColumnContainer().add(colSel, null);
    grid.getColumnContainer().add(colLabel, null);
    grid.getColumnContainer().add(colPos, null);
    grid.getColumnContainer().add(colStart, null);
    grid.getColumnContainer().add(colEnd, null);
    grid.getColumnContainer().add(colDateFormat, null);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    settingsPanel.add(controlLocalFile,             new GridBagConstraints(2, 3, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    settingsPanel.add(labelRemoteFile,              new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    settingsPanel.add(controlRemoteFile,                    new GridBagConstraints(2, 4, 2, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 140, 0));
    settingsPanel.add(labelDescr,           new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
    settingsPanel.add(controlDescr,             new GridBagConstraints(1, 1, 3, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    settingsPanel.add(labelClassName,         new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    buttonsPanel.add(insertButton1, null);
    buttonsPanel.add(editButton1, null);
    buttonsPanel.add(saveButton1, null);
    buttonsPanel.add(reloadButton1, null);
    buttonsPanel.add(buttonExecNow, null);
    settingsPanel.add(controlImportType,         new GridBagConstraints(1, 0, 3, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    settingsPanel.add(labelSep,       new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    mainPanel.add(previewPanel,    new GridBagConstraints(0, 1, 1, 1, 0.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    previewPanel.add(buttonPreview,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    previewPanel.add(scrollPane,   new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    prePanel.setPreferredSize(new Dimension(2000, 30));
    scrollPane.getViewport().add(prePanel, null);
    settingsPanel.add(controlSep,       new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    settingsPanel.add(localPanel,        new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    localPanel.add(buttonLocalFile, null);
    localPanel.add(buttonRemoveLocalFile, null);
    settingsPanel.add(remotePanel,      new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    remotePanel.add(buttonRemoteFile, null);
    remotePanel.add(buttonRemoveRemoteFile, null);
  }






  void controlNoSched_itemStateChanged(ItemEvent e) {
    if (e.getStateChange()==e.SELECTED) {
      controlStartDate.setValue(null);
      controlStartDate.setEnabled(false);
    }
  }


  void controlEachDay_itemStateChanged(ItemEvent e) {
    if (e.getStateChange()==e.SELECTED) {
      controlStartDate.setValue(new java.sql.Timestamp(System.currentTimeMillis()));
      controlStartDate.setEnabled(true);
    }
  }


  void controlEachWeek_itemStateChanged(ItemEvent e) {
    if (e.getStateChange()==e.SELECTED) {
      controlStartDate.setValue(new java.sql.Timestamp(System.currentTimeMillis()));
      controlStartDate.setEnabled(true);
    }
  }


  public Form getMainPanel() {
    return mainPanel;
  }
  public GridControl getGrid() {
    return grid;
  }


  void buttonRemoteFile_actionPerformed(ActionEvent e) {
    final ListControl list = new ListControl();
    Domain model = new Domain("FILES");
    final JDialog d = new JDialog(MDIFrame.getInstance(),"File",true);
    d.getContentPane().add(new JScrollPane(list),BorderLayout.CENTER);
    Response res = ClientUtils.getData("getFolderContent",new Object[]{controlFormat.getValue(),null});
    if (!res.isError()) {
      java.util.List rows = ((VOListResponse)res).getRows();
      File f = null;
      for(int i=0;i<rows.size();i++) {
        f = (File) rows.get(i);
        model.addDomainPair(f,f.getAbsolutePath());
      }
    }
    list.setDomain(model);
    list.addMouseListener(new MouseAdapter() {

      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount()==2) {
          ArrayList fff = (ArrayList)list.getValue();
          File f = (File)fff.get(0);
          if (f.isDirectory()) {
            Response res = ClientUtils.getData("getFolderContent",new Object[]{controlFormat.getValue(),f});
            if (!res.isError()) {
              java.util.List rows = ((VOListResponse)res).getRows();
              Domain model = new Domain("FILES");
              File ff = null;
              for(int i=0;i<rows.size();i++) {
                ff = (File) rows.get(i);
                if (ff.getName().equals(""))
                  model.addDomainPair(ff,ff.getAbsolutePath());
                else
                  model.addDomainPair(ff,ff.isDirectory()?"["+ff.getName()+"]":ff.getName());
              }
              list.setDomain(model);
            }
          }
          else {
            ETLProcessVO vo = (ETLProcessVO)getMainPanel().getVOModel().getValueObject();
            vo.setFilenameSYS23(f.getAbsolutePath());
            controlRemoteFile.setText(f.getAbsolutePath());
            d.setVisible(false);
          }
        }
      }

    });
    d.setSize(300,400);
    ClientUtils.centerDialog(MDIFrame.getInstance(),d);
    d.setDefaultCloseOperation(d.DISPOSE_ON_CLOSE);
    d.setVisible(true);
  }


  void buttonLocalFile_actionPerformed(ActionEvent e) {
    JFileChooser f = new JFileChooser(System.getProperty("user.home"));
    f.setFileFilter(new FileFilter() {

      public boolean accept(File f) {
        if (f.isDirectory() ||
            f.getName().toLowerCase().endsWith(".xls") && XLS.equals(controlFormat.getValue()) ||
            f.getName().toLowerCase().endsWith(".csv") && CSV1.equals(controlFormat.getValue()) ||
            f.getName().toLowerCase().endsWith(".csv") && CSV2.equals(controlFormat.getValue()) ||
            f.getName().toLowerCase().endsWith(".txt") && TXT.equals(controlFormat.getValue()));
        return false;
      }

      public String getDescription() {
        return (String)controlFormat.getValue();
      }

    });
    int res = f.showOpenDialog(this);
    if (res==f.APPROVE_OPTION) {
      File file = f.getSelectedFile();
      ETLProcessVO vo = (ETLProcessVO)getMainPanel().getVOModel().getValueObject();
      BufferedInputStream in = null;
      try {
        in = new BufferedInputStream(new FileInputStream(file));
        byte[] bytes = new byte[0];
        byte[] aux = null;
        byte[] bb = new byte[10000];
        int len = 0;
        while((len=in.read(bb))>0) {
          aux = bytes;
          bytes = new byte[aux.length+len];
          System.arraycopy(aux,0,bytes,0,aux.length);
          System.arraycopy(bb,0,bytes,aux.length,len);
        }
        in.close();
        vo.setLocalFile(bytes);
        controlLocalFile.setText(file.getAbsolutePath());
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
      finally {
        try {
          in.close();
        }
        catch (Exception ex1) {
        }
      }
    }
  }


  public void setControlImportType() {
    ETLProcessVO vo = (ETLProcessVO)getMainPanel().getVOModel().getValueObject();
    int index = -1;
    DataImportType ditVO = null;
    DomainPair[] dp = controlImportType.getDomain().getDomainPairList();
    for(int i=0;i<dp.length;i++) {
      ditVO = (DataImportType)dp[i].getCode();
      if (ditVO.className.equals(vo.getClassNameSYS23()) &&
          (ditVO.progressiveHIE02==null && vo.getProgressiveHIE02()==null || ditVO.progressiveHIE02.equals(vo.getProgressiveHIE02())) &&
          (ditVO.subTypeCode==null && vo.getSubTypeValueSYS23()==null || ditVO.subTypeCode.toString().equals(vo.getSubTypeValueSYS23().toString())) &&
          (ditVO.subTypeCode2==null && vo.getSubTypeValue2SYS23()==null || ditVO.subTypeCode2.toString().equals(vo.getSubTypeValue2SYS23().toString()))
      ) {
        index = i;
        break;
      }
    }
    controlImportType.setSelectedIndex(index);
  }


  class DataImportType {

    public Object subTypeCode;
    public Object subTypeCode2;
    public BigDecimal progressiveHIE02;
    public int comboItemIndex = -1;
    public String className;
    public String companyCodeSys01;

    public boolean equals(Object o) {
      if (!(o instanceof DataImportType))
          return false;
      DataImportType oo = (DataImportType)o;
      return comboItemIndex==oo.comboItemIndex;
    }

    public int hashCode() {
        return String.valueOf(comboItemIndex).hashCode();
    }

  }


  void buttonExecNow_actionPerformed(ActionEvent e) {
    ETLProcessVO vo = (ETLProcessVO)getMainPanel().getVOModel().getValueObject();
    Response res = ClientUtils.getData("startETLProcess",vo);
    if (res.isError()) {
      OptionPane.showMessageDialog(MDIFrame.getInstance(),res.getErrorMessage(),"Error",JOptionPane.ERROR_MESSAGE);
    }
    else {
      String[] values = ((VOResponse)res).getVo().toString().split("\n");
      String msg =
          ClientSettings.getInstance().getResources().getResource("execution time")+": "+values[0]+"s\n"+
          ClientSettings.getInstance().getResources().getResource("records read")+": "+values[1]+"\n"+
          ClientSettings.getInstance().getResources().getResource("records inserted")+": "+values[2]+"\n"+
          ClientSettings.getInstance().getResources().getResource("records updated")+": "+values[3];
      OptionPane.showMessageDialog(
        MDIFrame.getInstance(),
        msg,
        "data import completed",
        JOptionPane.INFORMATION_MESSAGE
      );
    }
  }


  public GenericButton getButtonExecNow() {
    return buttonExecNow;
  }

  void buttonPreview_actionPerformed(ActionEvent e) {
    ETLProcessVO vo = (ETLProcessVO)getMainPanel().getVOModel().getValueObject();
    prePanel.setContent(vo);
  }

  void buttonRemoveRemoteFile_actionPerformed(ActionEvent e) {
    ETLProcessVO vo = (ETLProcessVO)getMainPanel().getVOModel().getValueObject();
    vo.setFilenameSYS23(null);
    controlRemoteFile.setText(null);
  }

  void buttonRemoveLocalFile_actionPerformed(ActionEvent e) {
    ETLProcessVO vo = (ETLProcessVO)getMainPanel().getVOModel().getValueObject();
    vo.setLocalFile(null);
    controlLocalFile.setText(null);
  }



}

class ETLProcessFrame_controlNoSched_itemAdapter implements java.awt.event.ItemListener {
  ETLProcessFrame adaptee;

  ETLProcessFrame_controlNoSched_itemAdapter(ETLProcessFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void itemStateChanged(ItemEvent e) {
    adaptee.controlNoSched_itemStateChanged(e);
  }
}

class ETLProcessFrame_controlEachDay_itemAdapter implements java.awt.event.ItemListener {
  ETLProcessFrame adaptee;

  ETLProcessFrame_controlEachDay_itemAdapter(ETLProcessFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void itemStateChanged(ItemEvent e) {
    adaptee.controlEachDay_itemStateChanged(e);
  }
}

class ETLProcessFrame_controlEachWeek_itemAdapter implements java.awt.event.ItemListener {
  ETLProcessFrame adaptee;

  ETLProcessFrame_controlEachWeek_itemAdapter(ETLProcessFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void itemStateChanged(ItemEvent e) {
    adaptee.controlEachWeek_itemStateChanged(e);
  }
}

class ETLProcessFrame_buttonRemoteFile_actionAdapter implements java.awt.event.ActionListener {
  ETLProcessFrame adaptee;

  ETLProcessFrame_buttonRemoteFile_actionAdapter(ETLProcessFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.buttonRemoteFile_actionPerformed(e);
  }
}

class ETLProcessFrame_buttonLocalFile_actionAdapter implements java.awt.event.ActionListener {
  ETLProcessFrame adaptee;

  ETLProcessFrame_buttonLocalFile_actionAdapter(ETLProcessFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.buttonLocalFile_actionPerformed(e);
  }
}

class ETLProcessFrame_buttonExecNow_actionAdapter implements java.awt.event.ActionListener {
  ETLProcessFrame adaptee;

  ETLProcessFrame_buttonExecNow_actionAdapter(ETLProcessFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.buttonExecNow_actionPerformed(e);
  }
}

class ETLProcessFrame_buttonPreview_actionAdapter implements java.awt.event.ActionListener {
  ETLProcessFrame adaptee;

  ETLProcessFrame_buttonPreview_actionAdapter(ETLProcessFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.buttonPreview_actionPerformed(e);
  }
}

class ETLProcessFrame_buttonRemoveRemoteFile_actionAdapter implements java.awt.event.ActionListener {
  ETLProcessFrame adaptee;

  ETLProcessFrame_buttonRemoveRemoteFile_actionAdapter(ETLProcessFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.buttonRemoveRemoteFile_actionPerformed(e);
  }
}

class ETLProcessFrame_buttonRemoveLocalFile_actionAdapter implements java.awt.event.ActionListener {
  ETLProcessFrame adaptee;

  ETLProcessFrame_buttonRemoveLocalFile_actionAdapter(ETLProcessFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.buttonRemoveLocalFile_actionPerformed(e);
  }
}
