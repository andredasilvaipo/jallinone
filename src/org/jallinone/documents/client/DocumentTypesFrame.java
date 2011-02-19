package org.jallinone.documents.client;

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
import org.openswing.swing.util.java.Consts;
import org.jallinone.hierarchies.client.*;
import org.jallinone.commons.client.*;
import javax.swing.tree.DefaultMutableTreeNode;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the documents types grid frame + hieararchy detail.</p>
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
public class DocumentTypesFrame extends InternalFrame {

  JPanel buttonsPanel = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  InsertButton insertButton = new InsertButton();
  EditButton editButton = new EditButton();
  SaveButton saveButton = new SaveButton();
  ReloadButton reloadButton = new ReloadButton();
  DeleteButton deleteButton = new DeleteButton();
  NavigatorBar navigatorBar = new NavigatorBar();
  GridControl grid = new GridControl();
  TextColumn colDescr = new TextColumn();
  ExportButton exportButton = new ExportButton();
  JSplitPane detailPanel = new JSplitPane();
  TitledBorder titledBorder1;
  TitledBorder titledBorder2;
  BorderLayout borderLayout1 = new BorderLayout();
  HierarTreePanel hierarTreePanel = new HierarTreePanel();
  BorderLayout borderLayout2 = new BorderLayout();
  BorderLayout borderLayout3 = new BorderLayout();

  /** grid data locator */
  private ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();

  CompaniesComboColumn colCompanies = new CompaniesComboColumn();
  JSplitPane split = new JSplitPane();
  GridControl propsgrid = new GridControl();
  JPanel propsPanel = new JPanel();
  JPanel propsButtonsPanel = new JPanel();
  InsertButton insertButton1 = new InsertButton();
  FlowLayout flowLayout2 = new FlowLayout();
  EditButton editButton1 = new EditButton();
  SaveButton saveButton1 = new SaveButton();
  ReloadButton reloadButton1 = new ReloadButton();
  DeleteButton deleteButton1 = new DeleteButton();
  ComboColumn colPropType = new ComboColumn();
  TextColumn colPropDescr = new TextColumn();
  private ServerGridDataLocator propsgridDataLocator = new ServerGridDataLocator();


  public DocumentTypesFrame(final DocumentTypesController controller) {
    try {
      grid.setController(controller);
      grid.setGridDataLocator(gridDataLocator);
      gridDataLocator.setServerMethodName("loadDocumentTypes");

      hierarTreePanel.setTreeController(controller);

      propsgrid.setController(new LevelPropertiesController(this));
      propsgrid.setGridDataLocator(propsgridDataLocator);
      propsgridDataLocator.setServerMethodName("loadLevelProperties");

      hierarTreePanel.addHierarTreeListener(new HierarTreeListener(){

        public void loadDataCompleted(boolean error) {
          if (hierarTreePanel.getTree().getRowCount()>0)
            hierarTreePanel.getTree().setSelectionRow(0);
          if (hierarTreePanel.getTree().getSelectionPath()!=null)
            controller.leftClick((DefaultMutableTreeNode)hierarTreePanel.getTree().getSelectionPath().getLastPathComponent());
        }

      });

      jbInit();
      setSize(750,560);
      setMinimumSize(new Dimension(750,560));

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  public final void reloadData() {
    grid.reloadData();
  }


  private void jbInit() throws Exception {
    propsgrid.setMaxNumberOfRowsOnInsert(50);
    propsPanel.setLayout(borderLayout3);
    hierarTreePanel.setEnabled(false);
    hierarTreePanel.setFunctionId("DOC16");
    titledBorder1 = new TitledBorder("");
    titledBorder2 = new TitledBorder("");
    grid.setValueObjectClassName("org.jallinone.documents.java.DocumentTypeVO");
    this.setTitle(ClientSettings.getInstance().getResources().getResource("document types"));
    this.getContentPane().setLayout(borderLayout2);
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    grid.setDeleteButton(deleteButton);
    grid.setEditButton(editButton);
    grid.setExportButton(exportButton);
    grid.setFunctionId("DOC16");
    grid.setMaxSortedColumns(3);
    grid.setInsertButton(insertButton);
    grid.setNavBar(navigatorBar);
    grid.setReloadButton(reloadButton);
    grid.setSaveButton(saveButton);
    colDescr.setColumnFilterable(true);
    colDescr.setColumnName("descriptionSYS10");
    colDescr.setColumnSortable(true);
    colDescr.setEditableOnEdit(true);
    colDescr.setEditableOnInsert(true);
    colDescr.setHeaderColumnName("document type");
    colDescr.setPreferredWidth(390);
    titledBorder1.setTitleColor(Color.blue);
    titledBorder1.setTitle(ClientSettings.getInstance().getResources().getResource("hierarchy"));
    titledBorder2.setTitleColor(Color.blue);
    titledBorder2.setTitle(ClientSettings.getInstance().getResources().getResource("properties"));
    detailPanel.setBorder(titledBorder1);
    colCompanies.setColumnDuplicable(true);
    colCompanies.setColumnFilterable(true);
    colCompanies.setColumnName("companyCodeSys01DOC16");
    colCompanies.setEditableOnInsert(true);
    colCompanies.setEditableOnEdit(false);
    colCompanies.setPreferredWidth(200);
    propsgrid.setAutoLoadData(false);
    propsgrid.setDeleteButton(deleteButton1);
    propsgrid.setEditButton(editButton1);
    propsgrid.setFunctionId("DOC16");
    propsgrid.setInsertButton(insertButton1);
    propsgrid.setReloadButton(reloadButton1);
    propsgrid.setSaveButton(saveButton1);
    propsgrid.setValueObjectClassName("org.jallinone.documents.java.LevelPropertyVO");
    insertButton1.setText("insertButton1");
    propsButtonsPanel.setLayout(flowLayout2);
    flowLayout2.setAlignment(FlowLayout.LEFT);
    editButton1.setText("editButton1");
    saveButton1.setText("saveButton1");
    reloadButton1.setText("reloadButton1");
    deleteButton1.setText("deleteButton1");
    colPropType.setDomainId("PROPERTY_TYPE_DOC21");
    colPropType.setColumnName("propertyTypeDOC21");
    colPropType.setEditableOnInsert(true);
    colPropDescr.setColumnName("descriptionSYS10");
    colPropDescr.setEditableOnEdit(true);
    colPropDescr.setEditableOnInsert(true);
    colPropDescr.setPreferredWidth(200);
    propsPanel.setBorder(titledBorder2);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(saveButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(deleteButton, null);
    buttonsPanel.add(exportButton, null);
    buttonsPanel.add(navigatorBar, null);
    split.setDividerLocation(200);
    split.setOrientation(JSplitPane.VERTICAL_SPLIT);
    split.add(grid,JSplitPane.TOP);
    split.add(detailPanel,JSplitPane.BOTTOM);

    propsPanel.add(propsButtonsPanel,BorderLayout.NORTH);
    propsButtonsPanel.add(insertButton1, null);
    propsButtonsPanel.add(editButton1, null);
    propsButtonsPanel.add(saveButton1, null);
    propsButtonsPanel.add(reloadButton1, null);
    propsButtonsPanel.add(deleteButton1, null);
    propsPanel.add(propsgrid,BorderLayout.CENTER);
    propsgrid.getColumnContainer().add(colPropType, null);

    detailPanel.setDividerLocation(290);
    detailPanel.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
    detailPanel.add(hierarTreePanel,JSplitPane.LEFT);
    detailPanel.add(propsPanel,JSplitPane.RIGHT);

    this.getContentPane().add(split, BorderLayout.CENTER);
    grid.getColumnContainer().add(colCompanies,null);
    grid.getColumnContainer().add(colDescr, null);
    propsgrid.getColumnContainer().add(colPropDescr, null);
    colCompanies.setFunctionCode("DOC16");
  }


  public HierarTreePanel getHierarTreePanel() {
    return hierarTreePanel;
  }


  public GridControl getGrid() {
    return grid;
  }
  public GridControl getPropsgrid() {
    return propsgrid;
  }


}
