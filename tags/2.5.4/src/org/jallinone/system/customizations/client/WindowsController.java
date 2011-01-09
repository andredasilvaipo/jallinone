package org.jallinone.system.customizations.client;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.client.ClientUtils;
import org.openswing.swing.tree.client.TreeController;
import javax.swing.tree.DefaultMutableTreeNode;
import org.jallinone.system.customizations.java.*;
import java.math.BigDecimal;
import javax.swing.*;
import org.openswing.swing.util.client.ClientSettings;
import org.jallinone.commons.java.ApplicationConsts;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Tree & Grid Controller used for customizations window.</p>
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
public class WindowsController extends GridController implements TreeController {


  /** window that shows the customizable windows list as a tree */
  private WindowsTreeGridFrame treeGridFrame = null;

  /** pk for SYS12 table */
  private BigDecimal progressiveSYS13 = null;

  /** working table name */
  private String tableNameSYS13 = null;


  public WindowsController() {
    treeGridFrame = new WindowsTreeGridFrame(this);
    MDIFrame.add(treeGridFrame);
  }


  /**
   * Callback method invoked when the user has clicked the left mouse button a tree node.
   * @param node selected node
   */
  public void leftClick(DefaultMutableTreeNode node) {
    if (node.isLeaf()) {
      progressiveSYS13 = ((WindowVO)node.getUserObject()).getProgressiveSYS13();
      tableNameSYS13 = ((WindowVO)node.getUserObject()).getTableNameSYS13();
      treeGridFrame.getGrid().getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_SYS13,progressiveSYS13);
      treeGridFrame.getGrid().getOtherGridParams().put(ApplicationConsts.TABLE_NAME_SYS13,tableNameSYS13);
    }
    else {
      progressiveSYS13 = null;
      tableNameSYS13 = null;
      treeGridFrame.getGrid().getOtherGridParams().clear();
    }
    treeGridFrame.getGrid().reloadData();
  }


  /**
   * Callback method invoked when the user has clicked the right mouse button a tree node.
   * @param node selected node
   */
  public boolean rightClick(DefaultMutableTreeNode node) {
    return false;
  }


  /**
   * Callback method invoked when the user has doubled clicked a tree node.
   * @param node selected node
   */
  public void doubleClick(DefaultMutableTreeNode node) {

  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
   * @param rowNumbers row indexes related to the new rows to save
* @param newValueObjects list of new value objects to save
* @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
    WindowCustomizationVO vo = null;

    for(int i=0;i<newValueObjects.size();i++) {
      vo = (WindowCustomizationVO)newValueObjects.get(i);
      if (vo.getColumnTypeSYS12().equals("S") && vo.getColumnSizeSYS12()==null) {
        return new ErrorResponse("You must specify the column length");
      }
      else if (vo.getColumnTypeSYS12().equals("N") &&
        (vo.getColumnSizeSYS12()==null || vo.getColumnDecSYS12()==null)) {
        return new ErrorResponse("You must specify the number of integers and decimals for the column");
      }
      vo.setProgressiveSys13SYS12(progressiveSYS13);
      vo.setTableNameSYS13(tableNameSYS13);
    }

    Response response = ClientUtils.getData("insertWindowCustomizations",newValueObjects);
    return response;
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in EDIT mode.
   * @param rowNumbers row indexes related to the changed rows
   * @param oldPersistentObjects old value objects, previous the changes
   * @param persistentObjects value objects relatied to the changed rows
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response updateRecords(int[] rowNumbers,ArrayList oldPersistentObjects,ArrayList persistentObjects) throws Exception {
    Response response = ClientUtils.getData("updateWindowCustomizations",new ArrayList[]{oldPersistentObjects,persistentObjects});
    return response;
  }


  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    Response response = ClientUtils.getData("deleteWindowCustomizations",persistentObjects);
    return response;
  }



}
