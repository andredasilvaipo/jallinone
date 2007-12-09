package org.jallinone.production.manufactures.client;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.production.manufactures.java.*;
import java.math.BigDecimal;
import javax.swing.*;
import org.openswing.swing.table.client.Grid;
import org.openswing.swing.client.GridControl;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.registers.currency.java.CurrencyVO;
import org.openswing.swing.message.send.java.LookupValidationParams;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the grid controller for manufactures grid frame.</p>
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
public class ManufacturesController extends GridController {

  /** grid frame */
  private ManufacturesGridFrame gridFrame = null;


  public ManufacturesController() {
    gridFrame = new ManufacturesGridFrame(this);
    MDIFrame.add(gridFrame,true);
  }


  /**
   * Callback method invoked on pressing INSERT button.
   * @return <code>true</code> allows to go to INSERT mode, <code>false</code> the mode change is interrupted
   */
  public boolean beforeInsertGrid(GridControl grid) {
    gridFrame.getPhasesGridControl().clearData();
    gridFrame.setEnabled(false);
    return true;
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
   * @param rowNumbers row indexes related to the new rows to save
* @param newValueObjects list of new value objects to save
* @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
    Response res = ClientUtils.getData("insertManufacture",newValueObjects.get(0));
    if(!res.isError()) {
      doubleClick(0,(ValueObject)((VOListResponse)res).getRows().get(0));
    }
    return res;
  }

  /**
   * Method invoked when the user has clicked on save button and the grid is in EDIT mode.
   * @param rowNumbers row indexes related to the changed rows
   * @param oldPersistentObjects old value objects, previous the changes
   * @param persistentObjects value objects relatied to the changed rows
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response updateRecords(int[] rowNumbers,ArrayList oldPersistentObjects,ArrayList persistentObjects) throws Exception {
    return ClientUtils.getData("updateManufacture",new ManufactureVO[]{
      (ManufactureVO)oldPersistentObjects.get(0),
      (ManufactureVO)persistentObjects.get(0)
    });
  }



  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    Response res = ClientUtils.getData("deleteManufactures",persistentObjects);
    if (!res.isError()) {
      gridFrame.getPhasesGridControl().clearData();
      gridFrame.setEnabled(false);
    }
    return res;
  }


  /**
   * Callback method invoked when the data loading is completed.
   * @param error <code>true</code> if data loading has terminated with errors, <code>false</code> otherwise
   */
  public void loadDataCompleted(boolean error) {
    if (gridFrame.getGrid().getVOListTableModel().getRowCount()>0)
      doubleClick(0,gridFrame.getGrid().getVOListTableModel().getObjectForRow(0));
  }


  /**
   * Callback method invoked when the user has double clicked on the selected row of the grid.
   * @param rowNumber selected row index
   * @param persistentObject v.o. related to the selected row
   */
  public void doubleClick(int rowNumber,ValueObject persistentObject) {
    gridFrame.setEnabled(true);
    gridFrame.getPhasesGridControl().getOtherGridParams().put(ApplicationConsts.MANUFACTURE_VO,persistentObject);
    gridFrame.getPhasesGridControl().reloadData();

    // retrieve company currency...
    ManufactureVO vo = (ManufactureVO)persistentObject;
    Response res = ClientUtils.getData("loadCompanyCurrency",vo.getCompanyCodeSys01PRO01());
    if (!res.isError())
      gridFrame.setCompanyCurrencyVO((CurrencyVO)((VOResponse)res).getVo());


  }



}
