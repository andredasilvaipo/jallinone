package org.jallinone.subjects.client;

import org.openswing.swing.form.client.Form;
import java.awt.*;
import javax.swing.border.*;
import org.openswing.swing.util.client.ClientSettings;
import javax.swing.*;
import org.openswing.swing.client.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.table.java.ServerGridDataLocator;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Panel that contains references (table REG15).</p>
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
public class ReferencesPanel extends JPanel {

  BorderLayout borderLayout1 = new BorderLayout();
  JPanel buttonsPanel = new JPanel();
  GridControl grid = new GridControl();
  FlowLayout flowLayout1 = new FlowLayout();
  InsertButton insertButton1 = new InsertButton();
  EditButton editButton1 = new EditButton();
  SaveButton saveButton1 = new SaveButton();
  ReloadButton reloadButton1 = new ReloadButton();
  DeleteButton deleteButton1 = new DeleteButton();
  ExportButton exportButton1 = new ExportButton();
  NavigatorBar navigatorBar1 = new NavigatorBar();
  ComboColumn colRefType = new ComboColumn();
  TextColumn colValue = new TextColumn();

  /** grid data locator */
  private ServerGridDataLocator refGridDataLocator = new ServerGridDataLocator();


  public ReferencesPanel() {
    try {
      jbInit();
      grid.setController(new ReferencesController(this));
      grid.setGridDataLocator(refGridDataLocator);
      refGridDataLocator.setServerMethodName("loadReferences");
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    grid.setMaxNumberOfRowsOnInsert(50);
    grid.setValueObjectClassName("org.jallinone.subjects.java.ReferenceVO");
    this.setLayout(borderLayout1);
    grid.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    insertButton1.setText("insertButton1");
    editButton1.setText("editButton1");
    saveButton1.setText("saveButton1");
    reloadButton1.setText("reloadButton1");
    deleteButton1.setText("deleteButton1");
    grid.setAutoLoadData(false);
    grid.setDeleteButton(deleteButton1);
    grid.setEditButton(editButton1);
    grid.setExportButton(exportButton1);
    grid.setInsertButton(insertButton1);
    grid.setReloadButton(reloadButton1);
    grid.setSaveButton(saveButton1);
    colRefType.setDomainId("REFERENCE_TYPE");
    colRefType.setColumnFilterable(true);
    colRefType.setColumnName("referenceTypeREG15");
    colRefType.setColumnSortable(true);
    colRefType.setEditableOnEdit(true);
    colRefType.setEditableOnInsert(true);
    colRefType.setPreferredWidth(200);
    colRefType.setSortVersus(org.openswing.swing.util.java.Consts.ASC_SORTED);
    colRefType.setSortingOrder(1);
    colValue.setColumnName("valueREG15");
    colValue.setEditableOnEdit(true);
    colValue.setEditableOnInsert(true);
    colValue.setPreferredWidth(200);
    this.add(buttonsPanel, BorderLayout.NORTH);
    this.add(grid, BorderLayout.CENTER);
    buttonsPanel.add(insertButton1, null);
    buttonsPanel.add(editButton1, null);
    buttonsPanel.add(saveButton1, null);
    buttonsPanel.add(reloadButton1, null);
    buttonsPanel.add(deleteButton1, null);
    buttonsPanel.add(exportButton1, null);
    buttonsPanel.add(navigatorBar1, null);
    grid.getColumnContainer().add(colRefType, null);
    grid.getColumnContainer().add(colValue, null);
  }


  public final GridControl getGrid() {
    return grid;
  }


  public final void setButtonsEnabled(boolean enabled) {
    insertButton1.setEnabled(enabled);
    editButton1.setEnabled(enabled);
    deleteButton1.setEnabled(enabled);
//    copyButton1.setEnabled(enabled);
  }



}