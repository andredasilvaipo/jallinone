package org.jallinone.subjects.client;

import org.openswing.swing.mdi.client.InternalFrame;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import org.jallinone.hierarchies.client.*;
import org.openswing.swing.client.*;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.table.java.ServerGridDataLocator;
import org.jallinone.subjects.java.*;
import org.openswing.swing.table.columns.client.*;
import org.jallinone.commons.client.*;
import org.jallinone.commons.java.ApplicationConsts;
import javax.swing.tree.DefaultMutableTreeNode;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Value object used to store a subject hierarchy.</p>
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
public class SubjectHierarchiesFrame extends InternalFrame {

  JPanel hiearchiesPanel = new JPanel();
  JSplitPane splitPane = new JSplitPane();
  TitledBorder titledBorder1;
  HierarTreePanel treePanel = new HierarTreePanel();
  JPanel subjectesPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel subjectsButtonsPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  GridControl subjectsGrid = new GridControl();
  BorderLayout borderLayout2 = new BorderLayout();
  JPanel hierButtonsPanel = new JPanel();
  FlowLayout flowLayout2 = new FlowLayout();
  GridControl grid = new GridControl();
  InsertButton insertButton1 = new InsertButton();
  EditButton editButton1 = new EditButton();
  SaveButton saveButton1 = new SaveButton();
  ReloadButton reloadButton1 = new ReloadButton();
  DeleteButton deleteButton1 = new DeleteButton();
  ReloadButton reloadButton2 = new ReloadButton();
  DeleteButton deleteButton2 = new DeleteButton();
  NavigatorBar navigatorBar1 = new NavigatorBar();
  JSplitPane split = new JSplitPane();

  /** hierarchies grid data locator */
  private ServerGridDataLocator hierGridDataLocator = new ServerGridDataLocator();

  /** subjects grid data locator */
  private ServerGridDataLocator subGridDataLocator = new ServerGridDataLocator();

  /** function code */
  private String functionId = null;

  /** subjects controller */
  private HierarSubjectsController subjectController = null;
  TextColumn colDescr = new TextColumn();
  TextColumn colName1 = new TextColumn();
  TextColumn colName2 = new TextColumn();
  ComboColumn comboColumn1 = new ComboColumn();
  ExportButton exportButton1 = new ExportButton();
  CompaniesComboColumn colCompaniesCombo = new CompaniesComboColumn();
  BorderLayout borderLayout3 = new BorderLayout();

  private SubjectHierarchiesController controller = null;


  public SubjectHierarchiesFrame(final SubjectHierarchiesController controller,String functionId,String title,String subjectTypeREG08,boolean loadOnlyCurrentLevel) {
    this.controller = controller;
    this.functionId = functionId;
    try {
      jbInit();
      setSize(750,550);
      setMinimumSize(new Dimension(750,550));
      setTitle(ClientSettings.getInstance().getResources().getResource(title));

      subjectsGrid.setGridDataLocator(subGridDataLocator);
      subjectController = new HierarSubjectsController(this);
      subjectsGrid.setController(subjectController);
      subGridDataLocator.setServerMethodName("loadHierarSubjects");
      subjectsGrid.getOtherGridParams().put(ApplicationConsts.SUBJECT_TYPE,subjectTypeREG08);
      subjectsGrid.getOtherGridParams().put(ApplicationConsts.LOAD_ONLY_CURRENT_LEVEL,new Boolean(loadOnlyCurrentLevel));

      grid.setGridDataLocator(hierGridDataLocator);
      grid.setController(controller);
      hierGridDataLocator.setServerMethodName("loadSubjectHierarchies");
      grid.getOtherGridParams().put(ApplicationConsts.SUBJECT_TYPE,subjectTypeREG08);
      grid.getOtherGridParams().put(ApplicationConsts.FUCTION_CODE,functionId);

      treePanel.addHierarTreeListener(new HierarTreeListener(){

        public void loadDataCompleted(boolean error) {
          if (treePanel.getTree().getRowCount()>0)
            treePanel.getTree().setSelectionRow(0);
          if (treePanel.getTree().getSelectionPath()!=null)
            controller.leftClick((DefaultMutableTreeNode)treePanel.getTree().getSelectionPath().getLastPathComponent());
        }

      });

      treePanel.setFunctionId(functionId);
      treePanel.setTreeController(controller);

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }



  private void jbInit() throws Exception {
    grid.setValueObjectClassName("org.jallinone.subjects.java.SubjectHierarchyVO");
    subjectsGrid.setValueObjectClassName("org.jallinone.subjects.java.SubjectVO");
    subjectsGrid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

    titledBorder1 = new TitledBorder("");
    this.getContentPane().setLayout(borderLayout3);
    hiearchiesPanel.setBorder(titledBorder1);
    hiearchiesPanel.setLayout(borderLayout2);
    titledBorder1.setTitleColor(Color.blue);
    titledBorder1.setTitle(ClientSettings.getInstance().getResources().getResource("hierarchies"));
    subjectesPanel.setLayout(borderLayout1);
    subjectsButtonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    subjectsGrid.setAutoLoadData(false);
    subjectsGrid.setDeleteButton(deleteButton2);
    subjectsGrid.setExportButton(exportButton1);
    subjectsGrid.setFunctionId(functionId);
    subjectsGrid.setMaxSortedColumns(3);
    subjectsGrid.setNavBar(navigatorBar1);
    subjectsGrid.setReloadButton(reloadButton2);
    hierButtonsPanel.setLayout(flowLayout2);
    flowLayout2.setAlignment(FlowLayout.LEFT);
    splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
    grid.setDeleteButton(deleteButton1);
    grid.setEditButton(editButton1);
    grid.setFunctionId(functionId);
    grid.setInsertButton(insertButton1);
    grid.setReloadButton(reloadButton1);
    grid.setSaveButton(saveButton1);
    colDescr.setColumnFilterable(true);
    colDescr.setColumnName("descriptionSYS10");
    colDescr.setColumnSortable(true);
    colDescr.setEditableOnEdit(true);
    colDescr.setEditableOnInsert(true);
    colDescr.setPreferredWidth(180);
    colDescr.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colName1.setColumnFilterable(true);
    colName1.setColumnName("name_1REG04");
    colName1.setColumnSortable(true);
    colName1.setPreferredWidth(180);
    colName1.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colName1.setSortingOrder(1);
    colName2.setColumnFilterable(true);
    colName2.setColumnName("name_2REG04");
    colName2.setColumnSortable(true);
    colName2.setPreferredWidth(160);
    comboColumn1.setDomainId("SUBJECT_TYPE");
    comboColumn1.setColumnFilterable(true);
    comboColumn1.setColumnName("subjectTypeREG04");
    comboColumn1.setColumnSortable(true);

    colCompaniesCombo.setPreferredWidth(90);
    this.getContentPane().add(split,BorderLayout.CENTER);
    split.add(hiearchiesPanel, JSplitPane.LEFT);
    split.add(splitPane, JSplitPane.RIGHT);


    splitPane.add(treePanel, JSplitPane.LEFT);
    splitPane.add(subjectesPanel, JSplitPane.RIGHT);
    subjectesPanel.add(subjectsButtonsPanel,  BorderLayout.NORTH);
    subjectsButtonsPanel.add(reloadButton2, null);
    subjectsButtonsPanel.add(deleteButton2, null);
    subjectsButtonsPanel.add(navigatorBar1, null);
    subjectesPanel.add(subjectsGrid, BorderLayout.CENTER);
    subjectsGrid.getColumnContainer().add(colName1, null);
    subjectsGrid.getColumnContainer().add(colName2, null);
    subjectsGrid.getColumnContainer().add(comboColumn1, null);
    hiearchiesPanel.add(hierButtonsPanel, BorderLayout.NORTH);
    hierButtonsPanel.add(insertButton1, null);
    hierButtonsPanel.add(editButton1, null);
    hierButtonsPanel.add(saveButton1, null);
    hierButtonsPanel.add(reloadButton1, null);
    hierButtonsPanel.add(deleteButton1, null);
    hiearchiesPanel.add(grid,  BorderLayout.CENTER);
    grid.getColumnContainer().add(colCompaniesCombo, null);

    colCompaniesCombo.setColumnFilterable(true);
    colCompaniesCombo.setColumnName("companyCodeSys01REG08");
    colCompaniesCombo.setColumnSortable(true);
    colCompaniesCombo.setEditableOnInsert(true);
    colCompaniesCombo.setFunctionCode(functionId);

    grid.getColumnContainer().add(colDescr, null);
    subjectsButtonsPanel.add(exportButton1, null);
    splitPane.setDividerLocation(180);
    split.setDividerLocation(250);
  }


  public HierarTreePanel getTreePanel() {
    return treePanel;
  }
  public GridControl getSubjectsGrid() {
    return subjectsGrid;
  }
  public GridControl getSubjectHierarchiesGrid() {
    return grid;
  }


  public SubjectHierarchiesController getController() {
    return controller;
  }

}
