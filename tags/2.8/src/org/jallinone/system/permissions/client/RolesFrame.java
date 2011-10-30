package org.jallinone.system.permissions.client;

import org.openswing.swing.mdi.client.InternalFrame;
import javax.swing.*;
import java.awt.*;
import org.openswing.swing.client.*;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.tree.client.*;
import org.openswing.swing.table.java.ServerGridDataLocator;
import org.openswing.swing.util.client.ClientSettings;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Frame that shows roles list and, for the selected role, the functions (+companies) associated to it.</p>
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
public class RolesFrame extends InternalFrame {

  JSplitPane mainSplitPane = new JSplitPane();
  JPanel rolesPanel = new JPanel();
  JPanel roleDetailPanel = new JPanel();
  JPanel rolesButtonsPanel = new JPanel();
  JPanel roleGridPanel = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  FlowLayout flowLayout1 = new FlowLayout();
  BorderLayout borderLayout2 = new BorderLayout();
  GridControl rolesGridControl = new GridControl();
  TextColumn colRoleDescr = new TextColumn();
  InsertButton insertButton = new InsertButton();
  EditButton editButton = new EditButton();
  DeleteButton deleteButton = new DeleteButton();
  SaveButton saveButton = new SaveButton();
  ReloadButton reloadButton = new ReloadButton();
  BorderLayout borderLayout3 = new BorderLayout();
  JSplitPane functionsSplitPane = new JSplitPane();
  JPanel functionsPanel = new JPanel();
  JTabbedPane tab = new JTabbedPane();
  JPanel companiesPanel = new JPanel();
  GridPermissionsPerRolePanel colsPanel = new GridPermissionsPerRolePanel();
  BorderLayout borderLayout4 = new BorderLayout();
  JPanel functionsButtonsPanel = new JPanel();
  JSplitPane treeGridSplitPane = new JSplitPane();
  TreePanel treePanel = new TreePanel();
  GridControl functionsGridControl = new GridControl();
  EditButton editButton1 = new EditButton();
  FlowLayout flowLayout2 = new FlowLayout();
  SaveButton saveButton1 = new SaveButton();
  ReloadButton reloadButton1 = new ReloadButton();
  BorderLayout borderLayout5 = new BorderLayout();
  JPanel companiesButtonsPanel = new JPanel();
  FlowLayout flowLayout3 = new FlowLayout();
  EditButton editButton2 = new EditButton();
  SaveButton saveButton2 = new SaveButton();
  ReloadButton reloadButton2 = new ReloadButton();
  GridControl companiesGridControl = new GridControl();
  TextColumn colFunctionDescription = new TextColumn();
  CheckBoxColumn colCanIns = new CheckBoxColumn();
  CheckBoxColumn colCanUpd = new CheckBoxColumn();
  CheckBoxColumn colCanDel = new CheckBoxColumn();
  TextColumn colCompanyDescr = new TextColumn();
  CheckBoxColumn colCanInsCompany = new CheckBoxColumn();
  CheckBoxColumn colCanUpdCompany = new CheckBoxColumn();
  CheckBoxColumn colCanDelCompany = new CheckBoxColumn();
  TextColumn colCompanyCode = new TextColumn();
  CheckBoxColumn colCanView = new CheckBoxColumn();
  CheckBoxColumn colCanViewCompany = new CheckBoxColumn();

  /** roles grid data locator */
  private ServerGridDataLocator rolesGridDataLocator = new ServerGridDataLocator();

  /** functions grid data locator */
  private ServerGridDataLocator functionsGridDataLocator = new ServerGridDataLocator();

  /** companies grid data locator */
  private ServerGridDataLocator companiesGridDataLocator = new ServerGridDataLocator();

  /** tree data locator */
  private TreeServerDataLocator treeDataLocator = new TreeServerDataLocator();

  /** tree folders controller */
  private TreeFoldersController treeFoldersController = new TreeFoldersController(rolesGridControl,functionsGridControl);
  CopyButton copyButton = new CopyButton();


  public RolesFrame(RolesController rolesController) {
    try {
      jbInit();
      setSize(750,600);
      setMinimumSize(new Dimension(750,600));

      rolesGridControl.setController(rolesController);
      rolesGridControl.setGridDataLocator(rolesGridDataLocator);
      rolesGridDataLocator.setServerMethodName("loadRoles");

      treePanel.setTreeController(treeFoldersController);
      treeDataLocator.setServerMethodName("loadMenuFolders");
      treeDataLocator.setNodeNameAttribute("descriptionSYS10");
      treePanel.setTreeDataLocator(treeDataLocator);

      functionsGridControl.setController(new RoleFunctionsController(functionsGridControl,companiesGridControl,colsPanel));
      functionsGridControl.setGridDataLocator(functionsGridDataLocator);
      functionsGridDataLocator.setServerMethodName("loadRoleFunctions");

      companiesGridControl.setController(new RoleFunctionCompaniesController(functionsGridControl,companiesGridControl));
      companiesGridControl.setGridDataLocator(companiesGridDataLocator);
      companiesGridDataLocator.setServerMethodName("loadRoleFunctionCompanies");

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    rolesGridControl.setMaxNumberOfRowsOnInsert(50);
    companiesGridControl.setValueObjectClassName("org.jallinone.system.permissions.java.RoleFunctionCompanyVO");
    rolesGridControl.setValueObjectClassName("org.jallinone.system.permissions.java.RoleVO");
    rolesGridControl.setVisibleStatusPanel(false);
    functionsGridControl.setValueObjectClassName("org.jallinone.system.permissions.java.RoleFunctionVO");
    functionsGridControl.setVisibleStatusPanel(false);
    this.setTitle(ClientSettings.getInstance().getResources().getResource("roles"));
    mainSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
    mainSplitPane.setDividerSize(15);
    rolesPanel.setLayout(borderLayout1);
    rolesButtonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    roleGridPanel.setLayout(borderLayout2);
    colRoleDescr.setColumnName("descriptionSYS10");
    colRoleDescr.setEditableOnEdit(true);
    colRoleDescr.setEditableOnInsert(true);
    colRoleDescr.setHeaderColumnName("roleDescription");
    colRoleDescr.setPreferredWidth(150);
    rolesGridControl.setCopyButton(copyButton);
    rolesGridControl.setDeleteButton(deleteButton);
    rolesGridControl.setEditButton(editButton);
    rolesGridControl.setFunctionId("SYS04");
    rolesGridControl.setInsertButton(insertButton);
    rolesGridControl.setReloadButton(reloadButton);
    rolesGridControl.setSaveButton(saveButton);
    roleDetailPanel.setLayout(borderLayout3);
    functionsSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
    functionsPanel.setLayout(borderLayout4);
    functionsButtonsPanel.setLayout(flowLayout2);
    flowLayout2.setAlignment(FlowLayout.RIGHT);
    companiesPanel.setLayout(borderLayout5);
    companiesButtonsPanel.setLayout(flowLayout3);
    flowLayout3.setAlignment(FlowLayout.LEFT);
    functionsGridControl.setAutoLoadData(false);
    functionsGridControl.setEditButton(editButton1);
    functionsGridControl.setFunctionId("SYS04");
    functionsGridControl.setReloadButton(reloadButton1);
    functionsGridControl.setSaveButton(saveButton1);
    companiesGridControl.setAutoLoadData(false);
    companiesGridControl.setEditButton(editButton2);
    companiesGridControl.setFunctionId("SYS04");
    companiesGridControl.setReloadButton(reloadButton2);
    companiesGridControl.setSaveButton(saveButton2);
    companiesGridControl.setVisibleStatusPanel(false);
    colCanIns.setColumnName("canDelSYS07");
    colCanIns.setShowDeSelectAllInPopupMenu(true);
    colCanIns.setEditableOnEdit(true);
    colCanIns.setPreferredWidth(50);
    colCanUpd.setColumnName("canView");
    colCanUpd.setShowDeSelectAllInPopupMenu(true);
    colCanUpd.setEditableOnEdit(true);
    colCanUpd.setPreferredWidth(50);
    colCanDel.setColumnName("canInsSYS07");
    colCanDel.setShowDeSelectAllInPopupMenu(true);
    colCanDel.setEditableOnEdit(true);
    colCanDel.setPreferredWidth(50);
    colCompanyDescr.setColumnName("name_1REG04");
    colCompanyDescr.setPreferredWidth(225);
    colCanInsCompany.setColumnName("canView");
    colCanInsCompany.setShowDeSelectAllInPopupMenu(true);
    colCanInsCompany.setEditableOnEdit(true);
    colCanInsCompany.setPreferredWidth(50);
    colCanUpdCompany.setColumnName("canInsSYS02");
    colCanUpdCompany.setShowDeSelectAllInPopupMenu(true);
    colCanUpdCompany.setEditableOnEdit(true);
    colCanUpdCompany.setPreferredWidth(50);
    colCanDelCompany.setColumnName("canUpdSYS02");
    colCanDelCompany.setShowDeSelectAllInPopupMenu(true);
    colCanDelCompany.setEditableOnEdit(true);
    colCanDelCompany.setPreferredWidth(50);
    colCanView.setColumnName("canUpdSYS07");
    colCanView.setEditableOnEdit(true);
    colCanView.setShowDeSelectAllInPopupMenu(true);
    colCanView.setPreferredWidth(50);
    colCanViewCompany.setColumnName("canDelSYS02");
    colCanViewCompany.setShowDeSelectAllInPopupMenu(true);
    colCanViewCompany.setEditableOnEdit(true);
    colCanViewCompany.setPreferredWidth(50);
    colFunctionDescription.setColumnName("descriptionSYS10");
    colFunctionDescription.setHeaderColumnName("function");
    colFunctionDescription.setPreferredWidth(140);
    colCompanyCode.setColumnName("companyCodeSys01SYS02");
    this.getContentPane().add(mainSplitPane,  BorderLayout.CENTER);
    mainSplitPane.add(rolesPanel, JSplitPane.LEFT);
    mainSplitPane.add(roleDetailPanel, JSplitPane.RIGHT);
    rolesPanel.add(rolesButtonsPanel, BorderLayout.NORTH);
    rolesPanel.add(roleGridPanel, BorderLayout.CENTER);
    roleGridPanel.add(rolesGridControl, BorderLayout.CENTER);
    rolesGridControl.getColumnContainer().add(colRoleDescr, null);
    rolesButtonsPanel.add(insertButton, null);
    rolesButtonsPanel.add(copyButton, null);
    rolesButtonsPanel.add(editButton, null);
    rolesButtonsPanel.add(saveButton, null);
    rolesButtonsPanel.add(reloadButton, null);
    rolesButtonsPanel.add(deleteButton, null);
    mainSplitPane.setDividerLocation(180);
    roleDetailPanel.add(functionsSplitPane, BorderLayout.CENTER);
    functionsSplitPane.add(functionsPanel, JSplitPane.TOP);
    tab.add(companiesPanel,ClientSettings.getInstance().getResources().getResource("companies permissions"));
    tab.add(colsPanel,ClientSettings.getInstance().getResources().getResource("columns permissions"));

    functionsSplitPane.add(tab, JSplitPane.BOTTOM);
    functionsPanel.add(functionsButtonsPanel, BorderLayout.NORTH);
    functionsPanel.add(treeGridSplitPane, BorderLayout.CENTER);
    treeGridSplitPane.add(treePanel, JSplitPane.LEFT);
    treeGridSplitPane.add(functionsGridControl, JSplitPane.RIGHT);
    functionsButtonsPanel.add(editButton1, null);
    functionsButtonsPanel.add(saveButton1, null);
    functionsButtonsPanel.add(reloadButton1, null);
    companiesPanel.add(companiesButtonsPanel, BorderLayout.NORTH);
    companiesButtonsPanel.add(editButton2, null);
    companiesButtonsPanel.add(saveButton2, null);
    companiesButtonsPanel.add(reloadButton2, null);
    companiesPanel.add(companiesGridControl, BorderLayout.CENTER);
    companiesGridControl.getColumnContainer().add(colCompanyCode, null);
    companiesGridControl.getColumnContainer().add(colCompanyDescr, null);
    functionsGridControl.getColumnContainer().add(colFunctionDescription, null);
    functionsGridControl.getColumnContainer().add(colCanUpd, null);
    functionsGridControl.getColumnContainer().add(colCanDel, null);
    companiesGridControl.getColumnContainer().add(colCanInsCompany, null);
    companiesGridControl.getColumnContainer().add(colCanUpdCompany, null);
    companiesGridControl.getColumnContainer().add(colCanDelCompany, null);
    functionsGridControl.getColumnContainer().add(colCanView, null);
    functionsGridControl.getColumnContainer().add(colCanIns, null);
    companiesGridControl.getColumnContainer().add(colCanViewCompany, null);
    functionsSplitPane.setDividerLocation(400);
    treeGridSplitPane.setDividerLocation(170);
  }


  /**
   * @return tree folders controller
   */
  public final TreeFoldersController getTreeFoldersController() {
    return treeFoldersController;
  }


  /**
   * @return tree folders panel
   */
  public final TreePanel getTreePanel() {
    return treePanel;
  }


  /**
   * @return companies grid control
   */
  public final GridControl getCompaniesGridControl() {
    return companiesGridControl;
  }


  /**
   * @return functions grid control
   */
  public final GridControl getFunctionsGridControl() {
    return functionsGridControl;
  }

}
