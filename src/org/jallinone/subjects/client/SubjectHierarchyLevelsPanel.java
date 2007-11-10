package org.jallinone.subjects.client;

import java.awt.*;
import javax.swing.*;
import org.openswing.swing.client.*;
import org.openswing.swing.table.java.ServerGridDataLocator;
import org.openswing.swing.table.columns.client.*;
import org.openswing.swing.lookup.client.*;
import org.openswing.swing.tree.client.TreeServerDataLocator;
import org.openswing.swing.message.receive.java.*;
import java.util.Collection;
import org.jallinone.subjects.java.SubjectHierarchyLevelVO;
import org.jallinone.commons.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Panel containing the list of hierarchy levels associated to the specified subject.</p>
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
public class SubjectHierarchyLevelsPanel extends JPanel {
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel buttonsPanel = new JPanel();
  GridControl grid = new GridControl();
  FlowLayout flowLayout1 = new FlowLayout();
  EditButton editButton1 = new EditButton();
  SaveButton saveButton1 = new SaveButton();
  ReloadButton reloadButton1 = new ReloadButton();

  ServerGridDataLocator gridDataLocator = new ServerGridDataLocator();
  TextColumn colDescr = new TextColumn();
  CodLookupColumn colLevel = new CodLookupColumn();
  LookupController levelController = new LookupController();
  LookupServerDataLocator levelDataLocator = new LookupServerDataLocator();
  TreeServerDataLocator treeLevelDataLocator = new TreeServerDataLocator();
  DecimalColumn colProgressiveHie01 = new DecimalColumn();


  public SubjectHierarchyLevelsPanel() {
    try {
      jbInit();

      grid.setController(new SubjectHierarchyLevelsController(this));
      grid.setGridDataLocator(gridDataLocator);
      gridDataLocator.setServerMethodName("loadSubjectHierarchyLevels");

      levelDataLocator.setTreeDataLocator(treeLevelDataLocator);
      treeLevelDataLocator.setServerMethodName("loadHierarchy");


      colLevel.setLookupController(levelController);
      levelController.setLookupDataLocator(levelDataLocator);
      levelController.setFrameTitle("hierarchy");
      levelController.setCodeSelectionWindow(levelController.TREE_FRAME);
      levelController.setAllowTreeLeafSelectionOnly(true);
      levelController.getLookupDataLocator().setNodeNameAttribute("descriptionSYS10");
      levelController.setLookupValueObjectClassName("org.jallinone.hierarchies.java.HierarchyLevelVO");
      levelController.addLookup2ParentLink("progressiveHIE01", "progressiveHie01REG16");
      levelController.addLookup2ParentLink("descriptionSYS10", "levelDescriptionSYS10");
      levelController.addLookupListener(new LookupListener() {

        public void codeValidated(boolean validated) {}

        public void codeChanged(ValueObject parentVO,Collection parentChangedAttributes) {}

        public void forceValidate() {}

        /**
         * Method called before code validation and on lookup button click.
         */
        public void beforeLookupAction(ValueObject parentVO) {
          SubjectHierarchyLevelVO vo = (SubjectHierarchyLevelVO)parentVO;
          treeLevelDataLocator.getTreeNodeParams().put(
              ApplicationConsts.PROGRESSIVE_HIE02,
              vo.getProgressiveHie02REG16()
          );
        }

      });


      colLevel.setEnableCodBox(false);

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    grid.setValueObjectClassName("org.jallinone.subjects.java.SubjectHierarchyLevelVO");
    this.setLayout(borderLayout1);
    grid.setAutoLoadData(false);
    grid.setEditButton(editButton1);
    grid.setReloadButton(reloadButton1);
    grid.setSaveButton(saveButton1);
    buttonsPanel.setLayout(flowLayout1);
    flowLayout1.setAlignment(FlowLayout.LEFT);
    colDescr.setColumnName("descriptionSYS10");
    colDescr.setColumnSortable(false);
    colDescr.setHeaderColumnName("hierarchy");
    colDescr.setPreferredWidth(250);
    colDescr.setSortingOrder(0);
    colLevel.setColumnName("levelDescriptionSYS10");
    colLevel.setEditableOnEdit(true);
    colLevel.setPreferredWidth(250);
    colProgressiveHie01.setColumnName("progressiveHie01REG16");
    colProgressiveHie01.setColumnSelectable(false);
    colProgressiveHie01.setColumnVisible(false);
    this.add(buttonsPanel, BorderLayout.NORTH);
    this.add(grid,  BorderLayout.CENTER);
    grid.getColumnContainer().add(colDescr, null);
    buttonsPanel.add(editButton1, null);
    buttonsPanel.add(saveButton1, null);
    buttonsPanel.add(reloadButton1, null);
    grid.getColumnContainer().add(colLevel, null);
    grid.getColumnContainer().add(colProgressiveHie01, null);
  }


  public GridControl getGrid() {
    return grid;
  }


  public TreeServerDataLocator getTreeLevelDataLocator() {
    return treeLevelDataLocator;
  }


  public final void setButtonsEnabled(boolean enabled) {
    editButton1.setEnabled(enabled);
  }


  public final void setAllowTreeLeafSelectionOnly(boolean onlyLeaf) {
    levelController.setAllowTreeLeafSelectionOnly(onlyLeaf);
  }


}
