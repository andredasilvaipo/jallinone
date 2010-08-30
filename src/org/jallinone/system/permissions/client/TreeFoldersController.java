package org.jallinone.system.permissions.client;

import org.openswing.swing.tree.client.TreeController;
import javax.swing.tree.DefaultMutableTreeNode;
import org.openswing.swing.client.GridControl;
import org.jallinone.hierarchies.java.HierarchyLevelVO;
import org.jallinone.system.permissions.java.RoleFunctionVO;
import org.jallinone.system.permissions.java.RoleVO;
import org.jallinone.commons.java.ApplicationConsts;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Controller linked to the folders tree, containing the application functions.</p>
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
public class TreeFoldersController implements TreeController {

  /** functions grid */
  private GridControl functionsGridControl = null;

  /**roles grid */
  private GridControl rolesGridControl = null;


  public TreeFoldersController(GridControl rolesGridControl,GridControl functionsGridControl) {
    this.rolesGridControl = rolesGridControl;
    this.functionsGridControl = functionsGridControl;
  }


  /**
   * Callback method invoked when the user has clicked the left mouse button.
   * @param node selected node
   */
  public void leftClick(DefaultMutableTreeNode node) {
    if (rolesGridControl.getSelectedRow()!=-1) {
      HierarchyLevelVO f = (HierarchyLevelVO)node.getUserObject();
      RoleVO vo = (RoleVO)rolesGridControl.getVOListTableModel().getObjectForRow(rolesGridControl.getSelectedRow());
      functionsGridControl.getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_SYS04,vo.getProgressiveSYS04());
      functionsGridControl.getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_HIE01,f.getProgressiveHIE01());
      functionsGridControl.reloadData();
    }
  }


  /**
   * Callback method invoked when the user has clicked the right mouse button.
   * @param node selected node
   */
  public boolean rightClick(DefaultMutableTreeNode node) {
    return false;
  }



  /**
   * Callback method invoked when the user has doubled clicked.
   * @param node selected node
   */
  public void doubleClick(DefaultMutableTreeNode node) {
  }



}
