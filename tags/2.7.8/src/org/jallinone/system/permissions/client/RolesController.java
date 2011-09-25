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
import org.jallinone.system.permissions.java.RoleVO;
import org.openswing.swing.util.client.ClientSettings;
import javax.swing.tree.DefaultMutableTreeNode;
import org.jallinone.commons.client.ClientApplet;
import org.jallinone.commons.client.ApplicationClientFacade;
import org.openswing.swing.client.GridControl;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Controller used to manage the roles grid.</p>
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
public class RolesController extends GridController {

  /** grid frame */
  private RolesFrame gridFrame = null;

  /** role identifier, used on COPY operation */
  private BigDecimal currentProgressiveSYS04 = null;


  public RolesController() {
    gridFrame = new RolesFrame(this);
    MDIFrame.add(gridFrame);
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
   * @param rowNumbers row indexes related to the new rows to save
* @param newValueObjects list of new value objects to save
* @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
    RoleVO vo = null;


    for(int i=0;i<newValueObjects.size();i++) {
      vo = (RoleVO)newValueObjects.get(i);
      vo.setProgressiveSYS04(currentProgressiveSYS04);
    }

    Response response = ClientUtils.getData("insertRoles",newValueObjects);
    if (!response.isError()) {
      currentProgressiveSYS04 = null;
      List rows = ((VOListResponse)response).getRows();
      ClientApplet applet = ( (ApplicationClientFacade) MDIFrame.getInstance().getClientFacade()).getMainClass();
      for(int i=0;i<rows.size();i++) {
        vo = (RoleVO)rows.get(i);
        applet.getAuthorizations().getUserRoles().put(vo.getProgressiveSYS04(),vo.getDescriptionSYS10());
      }
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
    return ClientUtils.getData("updateRoles",new ArrayList[]{oldPersistentObjects,persistentObjects});
  }


  /**
   * Callback method invoked before deleting data when the grid was in READONLY mode (on pressing delete button).
   * @return <code>true</code> allows the deleting to continue, <code>false</code> the deleting is interrupted
   */
  public boolean beforeDeleteGrid(GridControl grid) {
    RoleVO vo = (RoleVO)grid.getVOListTableModel().getObjectForRow(grid.getSelectedRow());
    if (vo.getProgressiveSYS04().equals(new BigDecimal(2))) {
      JOptionPane.showMessageDialog(
          ClientUtils.getParentFrame(grid),
          ClientSettings.getInstance().getResources().getResource("you cannot delete the administrator role"),
          ClientSettings.getInstance().getResources().getResource("Attention"),
          JOptionPane.WARNING_MESSAGE
      );
      return false;
    }
    return true;
  }


  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    Response response = ClientUtils.getData("deleteRole",persistentObjects.get(0));
    if (!response.isError()) {
      gridFrame.getFunctionsGridControl().clearData();
      gridFrame.getCompaniesGridControl().clearData();
    }
    return response;
  }


  /**
   * Callback method invoked when the user has double clicked on the selected row of the grid.
   * @param rowNumber selected row index
   * @param persistentObject v.o. related to the selected row
   */
  public void doubleClick(int rowNumber,ValueObject persistentObject) {
    DefaultMutableTreeNode selNode = gridFrame.getTreePanel().getSelectedNode();
    if (selNode!=null) {
      gridFrame.getTreeFoldersController().leftClick(selNode);
    }

  }


  /**
   * Callback method invoked on pressing COPY button.
   * @return <code>true</code> allows to go to INSERT mode (by copying data), <code>false</code> the mode change is interrupted
   */
  public boolean beforeCopyGrid(GridControl grid) {
    RoleVO vo = (RoleVO)grid.getVOListTableModel().getObjectForRow(grid.getSelectedRow());
    currentProgressiveSYS04 = vo.getProgressiveSYS04();
    return true;
  }



}
