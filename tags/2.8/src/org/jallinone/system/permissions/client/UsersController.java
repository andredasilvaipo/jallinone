package org.jallinone.system.permissions.client;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.system.companies.java.*;
import java.math.BigDecimal;
import javax.swing.*;
import org.openswing.swing.table.client.Grid;
import org.jallinone.system.permissions.java.*;
import org.openswing.swing.util.client.ClientSettings;
import javax.swing.tree.DefaultMutableTreeNode;
import org.jallinone.commons.client.ClientApplet;
import org.jallinone.commons.client.ApplicationClientFacade;
import org.openswing.swing.client.GridControl;
import org.jallinone.commons.java.ApplicationConsts;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Controller used to manage the users grid.</p>
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
public class UsersController extends GridController {

  /** grid frame */
  private UsersFrame gridFrame = null;

  /** user identifier, used on COPY operation */
  private String currentUsernameSYS03 = null;


  public UsersController() {
    gridFrame = new UsersFrame(this);
    MDIFrame.add(gridFrame,true);
  }


  /**
   * Callback method invoked on pressing INSERT button.
   * @return <code>true</code> allows to go to INSERT mode, <code>false</code> the mode change is interrupted
   */
  public boolean beforeInsertGrid(GridControl grid) {
    gridFrame.getUserRolesGridControl().clearData();
    return true;
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
   * @param rowNumbers row indexes related to the new rows to save
* @param newValueObjects list of new value objects to save
* @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
    UserVO vo = (UserVO)newValueObjects.get(0);
    vo.setOldUsernameSYS03(currentUsernameSYS03);

    Response response = ClientUtils.getData("insertUser",vo);
    if (!response.isError()) {
      currentUsernameSYS03 = null;
      ArrayList list = new ArrayList();
      list.add(((VOResponse)response).getVo());
      return new VOListResponse(list,false,list.size());
    }
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
    return ClientUtils.getData("updateUsers",new ArrayList[]{oldPersistentObjects,persistentObjects});
  }


  /**
   * Callback method invoked before deleting data when the grid was in READONLY mode (on pressing delete button).
   * @return <code>true</code> allows the deleting to continue, <code>false</code> the deleting is interrupted
   */
  public boolean beforeDeleteGrid(GridControl grid) {
    UserVO vo = null;
    for(int i=0;i<grid.getSelectedRows().length;i++) {
      vo = (UserVO)grid.getVOListTableModel().getObjectForRow(grid.getSelectedRows()[i]);
      if (vo.getUsernameSYS03().equals("ADMIN")) {
        JOptionPane.showMessageDialog(
            ClientUtils.getParentFrame(grid),
            ClientSettings.getInstance().getResources().getResource("you cannot delete administrator user"),
            ClientSettings.getInstance().getResources().getResource("Attention"),
            JOptionPane.WARNING_MESSAGE
        );
        return false;
      }
    }
    return true;
  }


  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    Response response = ClientUtils.getData("deleteUsers",persistentObjects);
    if (!response.isError()) {
      gridFrame.getUserRolesGridControl().clearData();
    }
    return response;
  }


  /**
   * Callback method invoked when the user has double clicked on the selected row of the grid.
   * @param rowNumber selected row index
   * @param persistentObject v.o. related to the selected row
   */
  public void doubleClick(int rowNumber,ValueObject persistentObject) {
    UserVO vo = (UserVO)gridFrame.getUsersGridControl().getVOListTableModel().getObjectForRow(gridFrame.getUsersGridControl().getSelectedRow());
    gridFrame.getUserRolesGridControl().getOtherGridParams().put(ApplicationConsts.USERNAME_SYS03,vo.getUsernameSYS03());
    gridFrame.getUserRolesGridControl().reloadData();
  }


  /**
   * Callback method invoked on pressing COPY button.
   * @return <code>true</code> allows to go to INSERT mode (by copying data), <code>false</code> the mode change is interrupted
   */
  public boolean beforeCopyGrid(GridControl grid) {
    UserVO vo = (UserVO)grid.getVOListTableModel().getObjectForRow(grid.getSelectedRow());
    currentUsernameSYS03 = vo.getUsernameSYS03();
    gridFrame.getUserRolesGridControl().clearData();
    return true;
  }


  /**
   * Callback method invoked each time a cell is edited: this method define if the new value if valid.
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
    if (attributeName.equals("passwdExpirationSYS03") &&
        newValue!=null &&
        ((Date)newValue).getTime()<System.currentTimeMillis()) {
      JOptionPane.showMessageDialog(
          ClientUtils.getParentFrame(gridFrame),
          ClientSettings.getInstance().getResources().getResource("you must specify a date greater than today."),
          ClientSettings.getInstance().getResources().getResource("Attention"),
          JOptionPane.WARNING_MESSAGE
      );
      return false;
    }

    return true;
  }


}
