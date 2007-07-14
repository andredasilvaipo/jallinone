package org.jallinone.warehouse.client;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.warehouse.java.*;
import java.math.BigDecimal;
import javax.swing.*;
import org.openswing.swing.util.client.ClientSettings;
import org.jallinone.commons.client.ClientApplet;
import org.jallinone.commons.client.CompanyGridController;
import org.openswing.swing.table.client.Grid;
import org.jallinone.commons.client.ApplicationClientFacade;
import org.openswing.swing.client.GridControl;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description:Grid Controller used for warehouse grid frame.</p>
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
public class WarehousesController extends CompanyGridController {


  /** window that shows the customizable windows list as a tree */
  private WarehousesGridFrame gridFrame = null;

  /** main class */
  private ClientApplet mainClass = null;


  public WarehousesController(ClientApplet mainClass) {
    this.mainClass = mainClass;
    gridFrame = new WarehousesGridFrame(this);
    MDIFrame.add(gridFrame);
  }


  /**
   * Callback method invoked when the user has double clicked on the selected row of the grid.
   * @param rowNumber selected row index
   * @param persistentObject v.o. related to the selected row
   */
  public void doubleClick(int rowNumber,ValueObject persistentObject) {
    new WarehouseController(
        gridFrame,
        ((WarehouseVO)persistentObject).getCompanyCodeSys01WAR01(),
        ((WarehouseVO)persistentObject).getWarehouseCodeWAR01()
    );
  }


  /**
   * Callback method invoked before saving data when the grid was in INSERT mode (on pressing save button).
   * @return <code>true</code> allows the saving to continue, <code>false</code> the saving is interrupted
   */
  public boolean beforeInsertGrid(GridControl grid) {
    if (super.beforeInsertGrid(grid)) {
      new WarehouseController(gridFrame,null,null);
    }
    return false;
  }



  /**
   * Callback method invoked before deleting data when the grid was in READONLY mode (on pressing delete button).
   * @return <code>true</code> allows the deleting to continue, <code>false</code> the deleting is interrupted
   */
  public boolean beforeDeleteGrid(GridControl grid) {
    boolean canDelete = super.beforeDeleteGrid(grid);
    if (!canDelete)
      return false;

    // warehouse row is erasable ONLY if the user has the "warehouse role" (progressiveSys04WAR01)...
    WarehouseVO vo = (WarehouseVO)grid.getVOListTableModel().getObjectForRow(grid.getSelectedRow());
    if (vo.getProgressiveSys04WAR01()==null)
      return true;
    ClientApplet applet = ( (ApplicationClientFacade) MDIFrame.getInstance().getClientFacade()).getMainClass();
    if (!applet.getAuthorizations().getUserRoles().containsKey(vo.getProgressiveSys04WAR01())) {
      JOptionPane.showMessageDialog(
        ClientUtils.getParentFrame(grid),
        ClientSettings.getInstance().getResources().getResource(
        "You are not allowed to delete data."),
        ClientSettings.getInstance().getResources().getResource(
        "Attention"),
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
    WarehouseVO vo = (WarehouseVO)persistentObjects.get(0);
    WarehousePK pk = new WarehousePK(vo.getCompanyCodeSys01WAR01(),vo.getWarehouseCodeWAR01());
    Response response = ClientUtils.getData("deleteWarehouse",pk);
    return response;
  }



}
