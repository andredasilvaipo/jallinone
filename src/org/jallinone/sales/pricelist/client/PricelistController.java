package org.jallinone.sales.pricelist.client;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.sales.pricelist.java.*;
import java.math.BigDecimal;
import javax.swing.*;
import org.jallinone.commons.client.CompanyGridController;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.client.GridControl;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the grid controller for pricelist + item prices frame.</p>
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
public class PricelistController extends CompanyGridController {

  /** grid frame */
  private PricelistFrame frame = null;

  private boolean firstTime = true;

 /** used for copy button */
 private String oldCompanyCodeSys01SAL01 = null;
 private String oldPricelistCodeSAL01 = null;


  public PricelistController() {
    frame = new PricelistFrame(this);
    MDIFrame.add(frame,true);
  }


  /**
   * Callback method invoked when the data loading is completed.
   * @param error <code>true</code> if data loading has terminated with errors, <code>false</code> otherwise
   */
  public void loadDataCompleted(boolean error) {
    if (firstTime && !error) {
      firstTime = false;
      if (frame.getGrid().getVOListTableModel().getRowCount()>0)
        doubleClick(0,frame.getGrid().getVOListTableModel().getObjectForRow(0));
    }
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
   * @param rowNumbers row indexes related to the new rows to save
   * @param newValueObjects list of new value objects to save
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
    PricelistVO vo = null;

    for(int i=0;i<newValueObjects.size();i++) {
      vo = (PricelistVO)newValueObjects.get(i);
      vo.setOldPricelistCodeSal01SAL02(oldPricelistCodeSAL01);
    }

    Response res = ClientUtils.getData("insertPricelists",newValueObjects);
    if (!res.isError()) {
      doubleClick(0,(ValueObject)((VOListResponse)res).getRows().get(0));
    }

    oldCompanyCodeSys01SAL01 = null;
    oldPricelistCodeSAL01 = null;

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
    return ClientUtils.getData("updatePricelists",new ArrayList[]{oldPersistentObjects,persistentObjects});
  }



  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    Response res = ClientUtils.getData("deletePricelist",persistentObjects);
    if (!res.isError()) {
      frame.getPricesGrid().clearData();
      frame.setPricesPanelEnabled(false);
    }
    return res;
  }


  /**
   * Callback method invoked when the user has double clicked on the selected row of the grid.
   * @param rowNumber selected row index
   * @param persistentObject v.o. related to the selected row
   */
  public void doubleClick(int rowNumber,ValueObject persistentObject) {
    PricelistVO vo = (PricelistVO)persistentObject;
    frame.setPricesPanelEnabled(true);
    frame.getPricesGrid().getOtherGridParams().put(ApplicationConsts.PRICELIST,vo);
    frame.getTreeLevelDataLocator().getTreeNodeParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01SAL01());

    frame.getPricesGrid().reloadData();
  }


  /**
   * Callback method invoked on pressing COPY button.
   * @return <code>true</code> allows to go to INSERT mode (by copying data), <code>false</code> the mode change is interrupted
   */
  public boolean beforeCopyGrid(GridControl grid) {
    boolean ok = super.beforeCopyGrid(grid);
    if (!ok)
      return false;

    PricelistVO vo = (PricelistVO)frame.getGrid().getVOListTableModel().getObjectForRow(frame.getGrid().getSelectedRow());
    oldCompanyCodeSys01SAL01 = vo.getCompanyCodeSys01SAL01();
    oldPricelistCodeSAL01 = vo.getPricelistCodeSAL01();

    return true;
  }


}
