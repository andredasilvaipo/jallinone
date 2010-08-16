package org.jallinone.items.client;

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
import javax.swing.tree.DefaultMutableTreeNode;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the items types grid frame + hieararchy detail.</p>
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
public class ItemTypesFrame extends InternalFrame {

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
  JPanel detailPanel = new JPanel();
  TitledBorder titledBorder1;
  TitledBorder titledBorder2;
  BorderLayout borderLayout1 = new BorderLayout();
  HierarTreePanel hierarTreePanel = new HierarTreePanel();
  BorderLayout borderLayout2 = new BorderLayout();

  /** grid data locator */
  private ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();

  CompaniesComboColumn colCompanies = new CompaniesComboColumn();
  JSplitPane split = new JSplitPane();


  public ItemTypesFrame(GridController controller) {
    grid.setController(controller);
    grid.setGridDataLocator(gridDataLocator);
    gridDataLocator.setServerMethodName("loadItemTypes");
    try {
      jbInit();
      setSize(500,560);
      setMinimumSize(new Dimension(500,560));

      CustomizedColumns cust = new CustomizedColumns(new BigDecimal(272),grid);

      hierarTreePanel.addHierarTreeListener(new HierarTreeListener(){

        public void loadDataCompleted(boolean error) {
          if (hierarTreePanel.getTree().getRowCount()>0)
            hierarTreePanel.getTree().setSelectionRow(0);
          if (hierarTreePanel.getTree().getSelectionPath()!=null)
            hierarTreePanel.leftClick((DefaultMutableTreeNode)hierarTreePanel.getTree().getSelectionPath().getLastPathComponent());
        }

      });

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  public final void reloadData() {
    grid.reloadData();
  }


  private void jbInit() throws Exception {
    hierarTreePanel.setEnabled(false);
    hierarTreePanel.setFunctionId("ITM02");
    titledBorder1 = new TitledBorder("");
    grid.setValueObjectClassName("org.jallinone.items.java.ItemTypeVO");
    this.setTitle(ClientSettings.getInstance().getResources().getResource("item types"));
    this.getContentPane().setLayout(borderLayout2);
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    grid.setDeleteButton(deleteButton);
    grid.setEditButton(editButton);
    grid.setExportButton(exportButton);
    grid.setFunctionId("ITM02");
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
    colDescr.setHeaderColumnName("item type");
    colDescr.setPreferredWidth(290);
    titledBorder1.setTitleColor(Color.blue);
    titledBorder1.setTitle(ClientSettings.getInstance().getResources().getResource("hierarchy"));
    detailPanel.setBorder(titledBorder1);
    detailPanel.setLayout(borderLayout1);
    colCompanies.setColumnDuplicable(true);
    colCompanies.setColumnFilterable(true);
    colCompanies.setColumnName("companyCodeSys01ITM02");
    colCompanies.setEditableOnInsert(true);
    colCompanies.setEditableOnEdit(false);
    colCompanies.setPreferredWidth(200);
    this.getContentPane().add(buttonsPanel, BorderLayout.NORTH);
    buttonsPanel.add(insertButton, null);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(saveButton, null);
    buttonsPanel.add(reloadButton, null);
    buttonsPanel.add(deleteButton, null);
    buttonsPanel.add(exportButton, null);
    buttonsPanel.add(navigatorBar, null);
    split.setDividerLocation(200);
    split.setOrientation(split.VERTICAL_SPLIT);
    split.add(grid,split.TOP);
    split.add(detailPanel,split.BOTTOM);
    this.getContentPane().add(split, BorderLayout.CENTER);
    grid.getColumnContainer().add(colCompanies,null);
    grid.getColumnContainer().add(colDescr, null);
    colCompanies.setFunctionCode("ITM02");
    detailPanel.add(hierarTreePanel,  BorderLayout.CENTER);
  }


  public HierarTreePanel getHierarTreePanel() {
    return hierarTreePanel;
  }


  public GridControl getGrid() {
    return grid;
  }


}
