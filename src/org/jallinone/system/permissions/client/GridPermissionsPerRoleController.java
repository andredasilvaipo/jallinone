package org.jallinone.system.permissions.client;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.server.QueryUtil;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.client.GridControl;
import org.jallinone.system.permissions.java.GridPermissionsPerRoleVO;
import java.math.BigDecimal;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.util.client.ClientUtils;


/**
* <p>Title: JAllInOne</p>
* <p>Description: Controller for grids' columns permissions.</p>
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
public class GridPermissionsPerRoleController extends GridController {

  private GridPermissionsPerRolePanel panel = null;


  public GridPermissionsPerRoleController(GridPermissionsPerRolePanel panel) {
    this.panel = panel;
  }


  /**
   * @param grid grid
   * @param row selected row index
   * @param attributeName attribute name that identifies the selected grid column
   * @return <code>true</code> if the selected cell is editable, <code>false</code> otherwise
   */
  public boolean isCellEditable(GridControl grid,int row,String attributeName) {
    GridPermissionsPerRoleVO vo = (GridPermissionsPerRoleVO)grid.getVOListTableModel().getObjectForRow(row);
    if (attributeName.equals("editableInEdit") && !vo.isDefaultEditableInEdit())
      return false;
    if (attributeName.equals("editableInIns") && !vo.isDefaultEditableInIns())
      return false;
    if (attributeName.equals("required") && !vo.isEditableInIns() && !vo.isEditableInEdit())
      return true;
    if (attributeName.equals("required") && vo.isDefaultRequired())
      return false;
    if (attributeName.equals("visible"))
      return true;
    return grid.isFieldEditable(row,attributeName);
  }



  /**
   * Callback method invoked each time a cell is edited: this method define if the new value is valid.
   * This method is invoked ONLY if:
   * - the edited value is not equals to the old value OR it has exmplicitely called setCellAt or setValueAt
   * - the cell is editable
   * Default behaviour: cell value is valid.
   * @param rowNumber selected row index
   * @param attributeName attribute name related to the column currently selected
   * @param oldValue old cell value (before cell editing)
   * @param newValue new cell value (just edited)
   * @return <code>true</code> if cell value is valid, <code>false</code> otherwise
   */
  public boolean validateCell(int rowNumber,String attributeName,Object oldValue,Object newValue) {
//    GridPermissionsPerRoleVO vo = (GridPermissionsPerRoleVO)panel.getGrid().getVOListTableModel().getObjectForRow(rowNumber);
//    if (attributeName.equals("visible") &&
//        Boolean.TRUE.equals(newValue) &&
//        vo.isDefaultRequired())
//      return false;
//    else if (attributeName.equals("visible") &&
//        Boolean.FALSE.equals(newValue)) {
//      vo.setEditableInIns(false);
//      vo.setEditableInEdit(false);
//      return true;
//    }
//    if (attributeName.equals("required") &&
//        Boolean.TRUE.equals(newValue))
//      vo.setVisible(true);
    return true;
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in EDIT mode.
   * @param rowNumbers row indexes related to the changed rows
   * @param oldPersistentObjects old value objects, previous the changes
   * @param persistentObjects value objects relatied to the changed rows
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response updateRecords(int[] rowNumbers,ArrayList oldPersistentObjects,ArrayList persistentObjects) throws Exception {
    return ClientUtils.getData("updateGridPermissionsPerRole",new Object[]{panel.getFunctionCodeSYS06(),panel.getProgressiveSYS04(),persistentObjects});
  }



}
