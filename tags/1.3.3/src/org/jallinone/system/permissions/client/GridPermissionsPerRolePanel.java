package org.jallinone.system.permissions.client;

import javax.swing.*;
import org.openswing.swing.client.*;
import java.awt.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.lookup.client.LookupController;
import java.sql.*;
import java.awt.event.*;
import org.openswing.swing.table.java.*;
import org.openswing.swing.mdi.client.InternalFrame;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.client.ClientSettings;
import java.math.BigDecimal;
import org.jallinone.commons.java.ApplicationConsts;


/**
* <p>Title: JAllInOne</p>
* <p>Description: Panel used to define grid's columns permissions.</p>
 * </p>
 * <p>Copyright: Copyright (C) 2006 Mauro Carniel</p>
 *
 * <p> This file is part of OpenSwing Framework.
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the (LGPL) Lesser General Public
 * License as published by the Free Software Foundation;
 *
 *                GNU LESSER GENERAL PUBLIC LICENSE
 *                 Version 2.1, February 1999
 *
 * This library is distributed in the hope that it will be useful,
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
public class GridPermissionsPerRolePanel extends JPanel {

  GridControl grid = new GridControl();
  JPanel topPanel = new JPanel();
  JPanel buttonsPanel = new JPanel();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  ReloadButton reloadButton = new ReloadButton();
  FlowLayout flowLayout1 = new FlowLayout();
  TextColumn colDescr = new TextColumn();
  CheckBoxColumn colVis = new CheckBoxColumn();
  CheckBoxColumn colEII = new CheckBoxColumn();
  CheckBoxColumn colEIE = new CheckBoxColumn();
  CheckBoxColumn colReq = new CheckBoxColumn();
  private Connection conn = null;
  EditButton editButton = new EditButton();
  SaveButton saveButton = new SaveButton();
  ServerGridDataLocator locator = new ServerGridDataLocator();
  FlowLayout flowLayout2 = new FlowLayout();
  BorderLayout borderLayout1 = new BorderLayout();
  BigDecimal progressiveSYS04 = null;
  String functionCodeSYS06 = null;


  public GridPermissionsPerRolePanel() {
    this.conn = conn;
    try {
      jbInit();
      locator.setServerMethodName("loadGridPermissionsPerRole");
      grid.setController(new GridPermissionsPerRoleController(this));
      grid.setGridDataLocator(locator);
      grid.setAutoLoadData(false);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  public void reloadGrid(BigDecimal progressiveSYS04,String functionCodeSYS06) {
    this.progressiveSYS04 = progressiveSYS04;
    this.functionCodeSYS06 = functionCodeSYS06;
    grid.getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_SYS04,progressiveSYS04);
    grid.getOtherGridParams().put(ApplicationConsts.FUNCTION_CODE_SYS06,functionCodeSYS06);
    grid.reloadData();
  }


  private void jbInit() throws Exception {
    grid.setVisibleStatusPanel(false);
    grid.setEditOnSingleRow(false);
    topPanel.setLayout(flowLayout2);
    flowLayout2.setAlignment(FlowLayout.LEFT);
    flowLayout2.setHgap(0);
    flowLayout2.setVgap(0);
    colVis.setHeaderColumnName("colVisibile");
    topPanel.add(buttonsPanel,               new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    grid.setEditButton(editButton);
    grid.setReloadButton(reloadButton);
    grid.setSaveButton(saveButton);
    grid.setValueObjectClassName("org.jallinone.system.permissions.java.GridPermissionsPerRoleVO");
    colDescr.setColumnFilterable(true);
    colDescr.setColumnName("description");
    colDescr.setColumnSortable(true);
    colDescr.setPreferredWidth(200);
    colVis.setColumnName("visible");
    colVis.setEditableOnEdit(true);
    colVis.setPreferredWidth(80);
    colVis.setColumnRequired(false);
    colEII.setColumnName("editableInIns");
    colEII.setEditableOnEdit(true);
    colEII.setColumnRequired(false);
    colEII.setPreferredWidth(80);
    colEIE.setColumnName("editableInEdit");
    colEIE.setColumnRequired(false);
    colEIE.setEditableOnEdit(true);
    colEIE.setPreferredWidth(80);
    colReq.setColumnName("required");
    colReq.setHeaderColumnName("colRequired");
    colReq.setColumnRequired(false);
    colReq.setEditableOnEdit(true);
    colReq.setPreferredWidth(80);
    this.setLayout(borderLayout1);
    this.add(grid, BorderLayout.CENTER);
    this.add(topPanel, BorderLayout.NORTH);
    buttonsPanel.add(editButton, null);
    buttonsPanel.add(saveButton, null);
    buttonsPanel.add(reloadButton, null);
    grid.getColumnContainer().add(colDescr, null);
    grid.getColumnContainer().add(colVis, null);
    grid.getColumnContainer().add(colEII, null);
    grid.getColumnContainer().add(colEIE, null);
    grid.getColumnContainer().add(colReq, null);


  }

  public GridControl getGrid() {
    return grid;
  }
  public String getFunctionCodeSYS06() {
    return functionCodeSYS06;
  }
  public BigDecimal getProgressiveSYS04() {
    return progressiveSYS04;
  }


}


