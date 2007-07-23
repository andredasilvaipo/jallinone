package org.jallinone.purchases.pricelist.client;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.purchases.pricelist.java.*;
import java.math.BigDecimal;
import javax.swing.*;
import org.jallinone.commons.client.CompanyGridController;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.client.GridControl;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the grid controller for supplier pricelist + supplier item prices frame.</p>
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
public class SupplierPricelistController extends CompanyGridController {

  /** grid frame */
  private SupplierPricelistPanel panel = null;

  private boolean firstTime = true;

 /** used for copy button */
 private String oldCompanyCodeSys01PUR03 = null;
 private String oldPricelistCodePUR03 = null;
 private BigDecimal oldProgressiveReg04PUR03 = null;


  public SupplierPricelistController(SupplierPricelistPanel panel) {
    this.panel = panel;
  }


  /**
   * Callback method invoked when the data loading is completed.
   * @param error <code>true</code> if data loading has terminated with errors, <code>false</code> otherwise
   */
  public void loadDataCompleted(boolean error) {
    if (firstTime && !error) {
      firstTime = false;
      if (panel.getGrid().getVOListTableModel().getRowCount()>0)
        doubleClick(0,panel.getGrid().getVOListTableModel().getObjectForRow(0));
    }
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
   * @param rowNumbers row indexes related to the new rows to save
   * @param newValueObjects list of new value objects to save
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
    SupplierPricelistVO vo = null;
    for(int i=0;i<newValueObjects.size();i++) {
      vo = (SupplierPricelistVO)newValueObjects.get(i);
      vo.setOldPricelistCodePur03PUR04(oldPricelistCodePUR03);
      vo.setOldProgressiveReg04PUR03(oldProgressiveReg04PUR03);
      vo.setCompanyCodeSys01PUR03(panel.getSupplierVO().getCompanyCodeSys01REG04());
      vo.setProgressiveReg04PUR03(panel.getSupplierVO().getProgressiveREG04());
    }
    Response res = ClientUtils.getData("insertSupplierPricelists",newValueObjects);
    if (!res.isError()) {
      doubleClick(0,(ValueObject)((VOListResponse)res).getRows().get(0));
    }

    oldCompanyCodeSys01PUR03 = null;
    oldPricelistCodePUR03 = null;
    oldProgressiveReg04PUR03 = null;

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
    return ClientUtils.getData("updateSupplierPricelists",new ArrayList[]{oldPersistentObjects,persistentObjects});
  }



  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    Response res = ClientUtils.getData("deleteSupplierPricelist",persistentObjects);
    if (!res.isError()) {
      panel.getPricesGrid().clearData();
      panel.setPricesPanelEnabled(false);
    }
    return res;
  }


  /**
   * Callback method invoked when the user has double clicked on the selected row of the grid.
   * @param rowNumber selected row index
   * @param persistentObject v.o. related to the selected row
   */
  public void doubleClick(int rowNumber,ValueObject persistentObject) {
    SupplierPricelistVO vo = (SupplierPricelistVO)persistentObject;
    panel.setPricesPanelEnabled(true);
    panel.getPricesGrid().getOtherGridParams().put(ApplicationConsts.PRICELIST,vo);
    panel.getTreeLevelDataLocator().getTreeNodeParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01PUR03());


    panel.getPricesGrid().reloadData();
  }


  /**
   * Callback method invoked on pressing COPY button.
   * @return <code>true</code> allows to go to INSERT mode (by copying data), <code>false</code> the mode change is interrupted
   */
  public boolean beforeCopyGrid(GridControl grid) {
    boolean ok = super.beforeCopyGrid(grid);
    if (!ok)
      return false;

    SupplierPricelistVO vo = (SupplierPricelistVO)panel.getGrid().getVOListTableModel().getObjectForRow(panel.getGrid().getSelectedRow());
    oldCompanyCodeSys01PUR03 = vo.getCompanyCodeSys01PUR03();
    oldPricelistCodePUR03 = vo.getPricelistCodePUR03();
    oldProgressiveReg04PUR03 = vo.getProgressiveReg04PUR03();

    return true;
  }


}
