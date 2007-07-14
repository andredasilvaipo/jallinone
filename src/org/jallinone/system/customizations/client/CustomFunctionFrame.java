package org.jallinone.system.customizations.client;

import org.openswing.swing.mdi.client.*;
import java.awt.*;
import javax.swing.*;
import org.openswing.swing.form.client.*;
import org.openswing.swing.client.*;
import org.openswing.swing.util.client.ClientSettings;
import javax.swing.border.*;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.sqltool.java.TableVO;
import org.jallinone.system.customizations.java.CustomFunctionVO;
import java.util.ArrayList;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.message.receive.java.*;
import java.util.StringTokenizer;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.table.java.ServerGridDataLocator;
import org.openswing.swing.table.client.GridController;
import org.jallinone.system.customizations.java.CustomColumnVO;
import javax.swing.ImageIcon;
import java.awt.event.*;
import org.openswing.swing.lookup.client.LookupController;
import org.openswing.swing.lookup.client.LookupServerDataLocator;
import org.openswing.swing.tree.client.TreeServerDataLocator;
import org.openswing.swing.lookup.client.LookupListener;
import java.util.Collection;
import java.math.BigDecimal;



/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Detail frame for a custom function.</p>
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
public class CustomFunctionFrame extends InternalFrame {

  BorderLayout borderLayout1 = new BorderLayout();
  JPanel buttonsPanel = new JPanel();
  Form headerFormPanel = new Form();
  FlowLayout flowLayout1 = new FlowLayout();
  InsertButton insertButton1 = new InsertButton();
  EditButton editButton1 = new EditButton();
  CopyButton copyButton1 = new CopyButton();
  SaveButton saveButton1 = new SaveButton();
  ReloadButton reloadButton1 = new ReloadButton();
  DeleteButton deleteButton1 = new DeleteButton();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  LabelControl labelSQL = new LabelControl();
  TextAreaControl controlSQL = new TextAreaControl();
  LabelControl labelfuncCode = new LabelControl();
  TextControl controlFuncCode = new TextControl();
  LabelControl labelAutoLoad = new LabelControl();
  CheckBoxControl controlAutoLoad = new CheckBoxControl();
  LabelControl labelCompanyAccess = new LabelControl();
  CheckBoxControl controlCompanyAccess = new CheckBoxControl();
  LabelControl labelNote = new LabelControl();
  TextAreaControl controlNote = new TextAreaControl();
  JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
  JPanel bottomPanel = new JPanel();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  JPanel mainTablesPanel = new JPanel();
  JPanel colsPanel = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  TitledBorder titledBorder1;
  TitledBorder titledBorder2;
  TitledBorder titledBorder3;
  BorderLayout borderLayout3 = new BorderLayout();
  JScrollPane tablesScrollPane = new JScrollPane();
  JList tablesList = new JList();
  JPanel colButtonsPanel = new JPanel();
  FlowLayout flowLayout2 = new FlowLayout();
  GridControl grid = new GridControl();
  EditButton editButton2 = new EditButton();
  SaveButton saveButton2 = new SaveButton();
  ReloadButton reloadButton2 = new ReloadButton();
  TextColumn colColName = new TextColumn();
  ComboColumn colColType = new ComboColumn();
  TextColumn colEnum = new TextColumn();
  CheckBoxColumn colColVisible = new CheckBoxColumn();
  TextColumn colDefText = new TextColumn();
  DecimalColumn colDefNum = new DecimalColumn();
  CheckBoxColumn colDefDate = new CheckBoxColumn();
  LabelControl labelDescr = new LabelControl();
  TextControl controlDescr = new TextControl();
  GenericButton showQuery = new GenericButton(new ImageIcon(ClientUtils.getImage("budget.gif")));

  ServerGridDataLocator dataLoc = new ServerGridDataLocator();
  LabelControl labelLevel = new LabelControl();
  CodLookupControl controlLevel = new CodLookupControl();
  TextControl textControl1 = new TextControl();

  LookupController posController = new LookupController();
  LookupServerDataLocator posDataLocator = new LookupServerDataLocator();
  TreeServerDataLocator treeLevelPosDataLocator = new TreeServerDataLocator();
  CheckBoxColumn colIsParam = new CheckBoxColumn();
  CheckBoxColumn colIsParamRequired = new CheckBoxColumn();


  public CustomFunctionFrame(CustomFunctionController controller) {
    try {
      jbInit();
      setSize(750,500);
      setMinimumSize(new Dimension(750,500));
      headerFormPanel.setFormController(controller);

      grid.setGridDataLocator(dataLoc);
      dataLoc.setServerMethodName("loadCustomColumns");
      grid.setController(new CustomColumnsController(grid));

      // tree menu lookup...
      controlLevel.setLookupController(posController);
      posController.setLookupDataLocator(posDataLocator);
      posController.setFrameTitle("tree menu folders");

      posController.setCodeSelectionWindow(posController.TREE_FRAME);
      treeLevelPosDataLocator.setServerMethodName("loadHierarchy");
      posDataLocator.setTreeDataLocator(treeLevelPosDataLocator);
      posDataLocator.setNodeNameAttribute("descriptionSYS10");
      posController.setAllowTreeLeafSelectionOnly(false);
      posController.setLookupValueObjectClassName("org.jallinone.hierarchies.java.HierarchyLevelVO");
      posController.addLookup2ParentLink("progressiveHIE01", "progressiveHie01SYS18");
      posController.addLookup2ParentLink("descriptionSYS10","levelDescriptionSYS10");
      posController.setFramePreferedSize(new Dimension(400,400));
      posController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) { }

        public void beforeLookupAction(ValueObject parentVO) {
          CustomFunctionVO vo = (CustomFunctionVO)parentVO;
          treeLevelPosDataLocator.getTreeNodeParams().put(ApplicationConsts.PROGRESSIVE_HIE02,new BigDecimal(2));  // tree menu hierarchy identifier...
        }

        public void forceValidate() {}

      });

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    titledBorder1 = new TitledBorder("");
    titledBorder2 = new TitledBorder("");
    titledBorder3 = new TitledBorder("");
    headerFormPanel.setLayout(gridBagLayout1);
    this.getContentPane().setLayout(borderLayout1);
    showQuery.setToolTipText(ClientSettings.getInstance().getResources().getResource("show data"));
    this.setTitle(ClientSettings.getInstance().getResources().getResource("custom function"));
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    headerFormPanel.setCopyButton(copyButton1);
    headerFormPanel.setDeleteButton(deleteButton1);
    headerFormPanel.setEditButton(editButton1);
    headerFormPanel.setFunctionId("SYS16");
    headerFormPanel.setInsertButton(insertButton1);
    headerFormPanel.setReloadButton(reloadButton1);
    headerFormPanel.setSaveButton(saveButton1);
    headerFormPanel.setVOClassName("org.jallinone.system.customizations.java.CustomFunctionVO");
    labelSQL.setLabel("select");
    labelfuncCode.setLabel("functionCodeSYS06");
    controlFuncCode.setAttributeName("functionCodeSys06SYS16");
    controlFuncCode.setLinkLabel(labelfuncCode);
    controlFuncCode.setMaxCharacters(20);
    controlFuncCode.setRequired(true);
    controlFuncCode.setTrimText(true);
    controlFuncCode.setUpperCase(true);
    controlFuncCode.setEnabledOnEdit(false);
    controlSQL.setAttributeName("sql");
    controlSQL.setLinkLabel(labelfuncCode);
    controlSQL.setMaxCharacters(10000);
    controlSQL.setRequired(true);
    labelAutoLoad.setLabel("autoLoadDataSYS16");
    controlAutoLoad.setAttributeName("autoLoadDataSYS16");
    controlAutoLoad.setCanCopy(true);
    labelCompanyAccess.setLabel("useCompanyCodeSYS06");
    controlCompanyAccess.setAttributeName("useCompanyCodeSYS06");
    controlCompanyAccess.setCanCopy(true);
    controlCompanyAccess.setEnabledOnEdit(false);
    controlCompanyAccess.setText("");
    labelNote.setLabel("note");
    controlNote.setAttributeName("noteSYS16");
    controlNote.setLinkLabel(labelNote);
    controlNote.setMaxCharacters(2000);
    bottomPanel.setLayout(gridBagLayout2);
    mainTablesPanel.setLayout(borderLayout2);
    mainTablesPanel.setBorder(titledBorder3);
    colsPanel.setBorder(titledBorder2);
    colsPanel.setLayout(borderLayout3);
    titledBorder2.setTitleColor(Color.blue);
    titledBorder2.setTitle(ClientSettings.getInstance().getResources().getResource("column properties"));
    titledBorder3.setTitleColor(Color.blue);
    titledBorder3.setTitle(ClientSettings.getInstance().getResources().getResource("tables to save"));
    colButtonsPanel.setLayout(flowLayout2);
    flowLayout2.setAlignment(FlowLayout.LEFT);
    grid.setAutoLoadData(false);
    grid.setEditButton(editButton2);
    grid.setFunctionId("SYS16");
    grid.setReloadButton(reloadButton2);
    grid.setSaveButton(saveButton2);
    grid.setValueObjectClassName("org.jallinone.system.customizations.java.CustomColumnVO");
    grid.setVisibleStatusPanel(false);
    colColName.setColumnFilterable(true);
    colColName.setSortVersus(org.openswing.swing.util.java.Consts.NO_SORTED);
    colColName.setColumnName("columnNameSYS22");
    colColType.setDomainId("SYS22_COL_TYPES");
    colColType.setEditableOnEdit(true);
    colColType.setColumnName("columnTypeSYS22");
    colColVisible.setColumnName("columnVisibleSYS22");
    colColVisible.setColumnRequired(false);
    colColVisible.setEditableOnEdit(true);
    colColVisible.setPreferredWidth(50);
    colEnum.setColumnName("constraintValuesSYS22");
    colEnum.setColumnRequired(false);
    colEnum.setEditableOnEdit(true);
    colDefDate.setColumnRequired(false);
    colDefDate.setEditableOnEdit(true);
    colDefDate.setColumnName("defaultValueDateSYS22");
    colDefDate.setPreferredWidth(50);
    colDefNum.setColumnRequired(false);
    colDefNum.setPreferredWidth(60);
    colDefNum.setEditableOnEdit(true);
    colDefNum.setColumnName("defaultValueNumSYS22");
    colDefText.setColumnRequired(false);
    colDefText.setEditableOnEdit(true);
    colDefText.setPreferredWidth(90);
    colDefText.setColumnName("defaultValueTextSYS22");
    labelDescr.setLabel("description");
    controlDescr.setAttributeName("descriptionSYS10");
    controlDescr.setRequired(true);
    showQuery.addActionListener(new CustomFunctionFrame_showQuery_actionAdapter(this));
    labelLevel.setLabel("treePosition");
    controlLevel.setAllowOnlyNumbers(true);
    controlLevel.setAttributeName("progressiveHie01SYS18");
    controlLevel.setCodBoxVisible(false);
    controlLevel.setLinkLabel(labelLevel);
    controlLevel.setMaxCharacters(20);
    textControl1.setAttributeName("levelDescriptionSYS10");
    textControl1.setEnabledOnInsert(false);
    textControl1.setEnabledOnEdit(false);
    colIsParam.setColumnName("isParamSYS22");
    colIsParam.setColumnRequired(false);
    colIsParam.setEditableOnEdit(true);
    colIsParam.setEditableOnInsert(true);
    colIsParam.setPreferredWidth(50);
    colIsParamRequired.setColumnName("isParamRequiredSYS22");
    colIsParamRequired.setColumnRequired(false);
    colIsParamRequired.setEditableOnEdit(true);
    colIsParamRequired.setEditableOnInsert(true);
    colIsParamRequired.setPreferredWidth(70);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(insertButton1, null);
    buttonsPanel.add(copyButton1, null);
    buttonsPanel.add(editButton1, null);
    buttonsPanel.add(saveButton1, null);
    buttonsPanel.add(reloadButton1, null);
    buttonsPanel.add(deleteButton1, null);
    buttonsPanel.add(showQuery, null);
    splitPane.add(headerFormPanel,JSplitPane.TOP);
    splitPane.add(bottomPanel,JSplitPane.BOTTOM);
    bottomPanel.add(mainTablesPanel,  new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    mainTablesPanel.add(tablesScrollPane,  BorderLayout.CENTER);
    bottomPanel.add(colsPanel,    new GridBagConstraints(1, 0, 4, 1, 1.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 380, 0));
    splitPane.setDividerLocation(300);
    this.getContentPane().add(splitPane,  BorderLayout.CENTER);
    headerFormPanel.add(labelSQL,           new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    headerFormPanel.add(controlSQL,          new GridBagConstraints(1, 3, 5, 1, 1.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    headerFormPanel.add(labelfuncCode,        new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    headerFormPanel.add(controlFuncCode,        new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 0), 0, 0));
    headerFormPanel.add(labelAutoLoad,         new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 25, 5, 0), 0, 0));
    headerFormPanel.add(controlAutoLoad,      new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    headerFormPanel.add(labelCompanyAccess,       new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 25, 5, 0), 0, 0));
    headerFormPanel.add(controlCompanyAccess,    new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    headerFormPanel.add(labelNote,     new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    headerFormPanel.add(controlNote,        new GridBagConstraints(1, 4, 5, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 50));
    headerFormPanel.add(labelDescr,             new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
    headerFormPanel.add(controlDescr,   new GridBagConstraints(1, 2, 5, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
    headerFormPanel.add(labelLevel,  new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    tablesScrollPane.getViewport().add(tablesList, null);
    colsPanel.add(colButtonsPanel, BorderLayout.NORTH);
    colsPanel.add(grid,  BorderLayout.CENTER);
    grid.getColumnContainer().add(colColName, null);
    grid.getColumnContainer().add(colColType, null);
    grid.getColumnContainer().add(colColVisible, null);
    grid.getColumnContainer().add(colEnum, null);
    grid.getColumnContainer().add(colDefText, null);
    colButtonsPanel.add(editButton2, null);
    colButtonsPanel.add(saveButton2, null);
    colButtonsPanel.add(reloadButton2, null);
    grid.getColumnContainer().add(colDefNum, null);
    grid.getColumnContainer().add(colDefDate, null);
    headerFormPanel.add(controlLevel,    new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
    headerFormPanel.add(textControl1,   new GridBagConstraints(2, 1, 4, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 0, 5, 5), 0, 0));
    grid.getColumnContainer().add(colIsParam, null);
    grid.getColumnContainer().add(colIsParamRequired, null);
  }


  /**
   * Method called by form controller after form data loading.
   */
  public void loadDataCompleted(boolean error,CustomFunctionVO vo) {
    if (!error) {
      ArrayList mainTables = new ArrayList();
      StringTokenizer st = new StringTokenizer(vo.getMainTablesSYS16(),",");
      while(st.hasMoreTokens())
        mainTables.add(st.nextToken());

      TableVO tableVO = new TableVO(vo.getSql(),mainTables,true);
      Response res = ClientUtils.getData("getQueryInfo",tableVO);
      if (res.isError()) {
        JOptionPane.showMessageDialog(
          MDIFrame.getInstance(),
          ClientSettings.getInstance().getResources().getResource(res.getErrorMessage()),
          ClientSettings.getInstance().getResources().getResource("Error"),
          JOptionPane.WARNING_MESSAGE
        );
        return;
      }

      tableVO = (TableVO)((VOResponse)res).getVo();
      DefaultListModel model = new DefaultListModel();
      ArrayList tables = tableVO.getAllTables();
      for(int i=0;i<tables.size();i++)
        model.addElement(tables.get(i));

      tablesList.setModel(model);
      tablesList.revalidate();
      tablesList.repaint();

      int[] indeces = new int[mainTables.size()];
      for(int i=0;i<mainTables.size();i++)
        indeces[i] = model.indexOf(mainTables.get(i));
      tablesList.setSelectedIndices(indeces);

      tablesList.setEnabled(false);

      grid.getOtherGridParams().put(ApplicationConsts.FUNCTION_CODE_SYS06,vo.getFunctionCodeSys06SYS16());
      grid.reloadData();
    }
  }


  public void setButtonsEnabled(boolean enabled) {
    editButton2.setEnabled(enabled);
    saveButton2.setEnabled(enabled);
    reloadButton2.setEnabled(enabled);
    showQuery.setEnabled(enabled);
  }


  public void clearList() {
    DefaultListModel model = new DefaultListModel();
    tablesList.setModel(model);
    tablesList.revalidate();
    tablesList.repaint();
  }


  public JList getTablesList() {
    return tablesList;
  }
  public Form getHeaderFormPanel() {
    return headerFormPanel;
  }
  public GridControl getGrid() {
    return grid;
  }

  void showQuery_actionPerformed(ActionEvent e) {
    new ShowCustomFunctionFrame((String)controlFuncCode.getValue());
  }
  public GenericButton getShowQuery() {
    return showQuery;
  }


}

class CustomFunctionFrame_showQuery_actionAdapter implements java.awt.event.ActionListener {
  CustomFunctionFrame adaptee;

  CustomFunctionFrame_showQuery_actionAdapter(CustomFunctionFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.showQuery_actionPerformed(e);
  }
}
