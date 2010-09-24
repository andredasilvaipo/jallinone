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
import org.openswing.swing.util.client.ClientSettings;
import org.jallinone.system.permissions.java.*;
import org.openswing.swing.client.GridControl;
import org.jallinone.commons.java.ApplicationConsts;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Controller used to manage functions of a specific role.</p>
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
public class RoleFunctionsController extends GridController {

  /** role functions grid */
  private GridControl functionsGridControl = null;

  /** grid used to define function permissions per company, fixed a role and a function */
  private GridControl companiesGridControl = null;

  /** grid used to define columns permissions, fixed a role and a function */
  private GridPermissionsPerRolePanel colsPanel = null;


  public RoleFunctionsController(GridControl functionsGridControl,GridControl companiesGridControl,GridPermissionsPerRolePanel colsPanel) {
    this.functionsGridControl = functionsGridControl;
    this.companiesGridControl = companiesGridControl;
    this.colsPanel = colsPanel;
  }


  /**
   * Callback method invoked before saving data when the grid was in EDIT mode (on pressing save button).
   * @return <code>true</code> allows to go to EDIT mode, <code>false</code> the mode change is interrupted
   */
  public boolean beforeEditGrid(GridControl grid) {
    Object progressiveSYS04 = functionsGridControl.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_SYS04);
    if (progressiveSYS04==null)
      return false;
    if (progressiveSYS04.equals(new BigDecimal(2))) {
      JOptionPane.showMessageDialog(
          ClientUtils.getParentFrame(grid),
          ClientSettings.getInstance().getResources().getResource("you cannot change the administrator role"),
          ClientSettings.getInstance().getResources().getResource("Attention"),
          JOptionPane.WARNING_MESSAGE
      );
      return false;
    }
    return true;
  }



  /**
   * @param grid grid
   * @param row selected row index
   * @param attributeName attribute name that identifies the selected grid column
   * @return <code>true</code> if the selected cell is editable, <code>false</code> otherwise
   */
  public boolean isCellEditable(GridControl grid,int row,String attributeName) {
    RoleFunctionVO vo = (RoleFunctionVO)grid.getVOListTableModel().getObjectForRow(row);
    if (vo.getUseCompanyCodeSYS06().booleanValue() &&
        !attributeName.equals("canView"))
      return false;
    else if (attributeName.equals("functionCodeSys06SYS07"))
      return false;
    return grid.isFieldEditable(row,attributeName);
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in EDIT mode.
   * @param rowNumbers row indexes related to the changed rows
   * @param oldPersistentObjects old value objects, previous the changes
   * @param persistentObjects value objects relatied to the changed rows
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response updateRecords(int[] rowNumbers,ArrayList oldPersistentObjects,ArrayList persistentObjects) throws Exception {
    return ClientUtils.getData("updateRoleFunctions",new ArrayList[]{oldPersistentObjects,persistentObjects});
  }


  /**
   * Callback method invoked each time a cell is edited: this method define if the new value if valid.
   * This method is invoked ONLY if:
   * - the edited value is not equals to the old value OR it has exmplicitely called setCellAt or setValueAt
   * - the cell is editable
   * The method is called independently form the grid mode.
   * Default behaviour: cell value is valid.
   * @param rowNumber selected row index
   * @param attributeName attribute name related to the column currently selected
   * @param oldValue old cell value (before cell editing)
   * @param newValue new cell value (just edited)
   * @return <code>true</code> if cell value is valid, <code>false</code> otherwise
   */
  public boolean validateCell(int rowNumber,String attributeName,Object oldValue,Object newValue) {
    if (attributeName.equals("canInsSYS07") ||
        attributeName.equals("canUpdSYS07") ||
        attributeName.equals("canDelSYS07")) {
        if (((Boolean)newValue).booleanValue())
         ((RoleFunctionVO)functionsGridControl.getVOListTableModel().getObjectForRow(rowNumber)).setCanView(Boolean.TRUE);
    }
    else {
      if (attributeName.equals("canView")) {
        if (((Boolean)newValue).booleanValue()) {
         ((RoleFunctionVO)functionsGridControl.getVOListTableModel().getObjectForRow(rowNumber)).setCanInsSYS07(Boolean.TRUE);
         ((RoleFunctionVO)functionsGridControl.getVOListTableModel().getObjectForRow(rowNumber)).setCanUpdSYS07(Boolean.TRUE);
         ((RoleFunctionVO)functionsGridControl.getVOListTableModel().getObjectForRow(rowNumber)).setCanDelSYS07(Boolean.TRUE);
        }
        else {
          ((RoleFunctionVO)functionsGridControl.getVOListTableModel().getObjectForRow(rowNumber)).setCanInsSYS07(Boolean.FALSE);
          ((RoleFunctionVO)functionsGridControl.getVOListTableModel().getObjectForRow(rowNumber)).setCanUpdSYS07(Boolean.FALSE);
          ((RoleFunctionVO)functionsGridControl.getVOListTableModel().getObjectForRow(rowNumber)).setCanDelSYS07(Boolean.FALSE);
        }
      }
    }

    return true;
  }


  /**
   * Callback method invoked when the data loading is completed.
   * @param error <code>true</code> if data loading has terminated with errors, <code>false</code> otherwise
   */
  public void loadDataCompleted(boolean error) {
    companiesGridControl.clearData();
    colsPanel.getGrid().clearData();
  }


  /**
   * Callback method invoked when the user has double clicked on the selected row of the grid.
   * @param rowNumber selected row index
   * @param persistentObject v.o. related to the selected row
   */
  public void doubleClick(int rowNumber,ValueObject persistentObject) {
    if (functionsGridControl.getSelectedRow()!=-1) {
      RoleFunctionVO vo = (RoleFunctionVO)functionsGridControl.getVOListTableModel().getObjectForRow(functionsGridControl.getSelectedRow());
      if (vo.getUseCompanyCodeSYS06().booleanValue()) {
        companiesGridControl.getOtherGridParams().put(ApplicationConsts.PROGRESSIVE_SYS04,vo.getProgressiveSys04SYS07());
        companiesGridControl.getOtherGridParams().put(ApplicationConsts.FUNCTION_CODE_SYS06,vo.getFunctionCodeSys06SYS07());
        companiesGridControl.reloadData();

      }
      else {
        companiesGridControl.clearData();
      }

      colsPanel.reloadGrid(vo.getProgressiveSys04SYS07(),vo.getFunctionCodeSys06SYS07());
    }
  }



}
