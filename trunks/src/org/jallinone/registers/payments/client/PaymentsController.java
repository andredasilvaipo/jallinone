package org.jallinone.registers.payments.client;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.registers.payments.java.*;
import java.math.BigDecimal;
import javax.swing.*;
import org.openswing.swing.table.model.client.VOListTableModel;
import org.openswing.swing.util.client.ClientSettings;
import org.openswing.swing.message.receive.java.ErrorResponse;
import org.jallinone.commons.java.ApplicationConsts;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the grid controller for payments grid frame.
 * It's used also to update payment instalments.</p>
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
public class PaymentsController extends GridController {

  /** grid frame */
  private PaymentsGridFrame gridFrame = null;


  public PaymentsController() {
    gridFrame = new PaymentsGridFrame(this);
    MDIFrame.add(gridFrame,true);
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
   * @param rowNumbers row indexes related to the new rows to save
   * @param newValueObjects list of new value objects to save
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
    PaymentVO vo = null;


    for(int i=0;i<newValueObjects.size();i++) {
      vo = (PaymentVO)newValueObjects.get(i);
      if (vo.getInstalmentNumberREG10().intValue()<1) {
        return new ErrorResponse("you must define at least one instalment");
      }
    }
    Response res = ClientUtils.getData("insertPayments",newValueObjects);
    if (!res.isError()) {
      doubleClick(0,(ValueObject)((VOListResponse)res).getRows().get(0));
    }
    return res;
  }


  public Response updateRecords(int[] rowNumbers,ArrayList oldPersistentObjects,ArrayList persistentObjects) throws Exception {
    return ClientUtils.getData("updatePayments",new ArrayList[]{oldPersistentObjects,persistentObjects});
  }



  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    Response res = ClientUtils.getData("deletePayments",persistentObjects);
    if (!res.isError()) {
      gridFrame.getInstalmentsGrid().clearData();
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
    PaymentVO vo = (PaymentVO)persistentObject;
    gridFrame.getInstalmentsGrid().getOtherGridParams().put(ApplicationConsts.COMPANY_CODE_SYS01,vo.getCompanyCodeSys01REG10());
		gridFrame.getInstalmentsGrid().getOtherGridParams().put(ApplicationConsts.PAYMENT_CODE_REG10,vo.getPaymentCodeREG10());
    gridFrame.getInstalmentsGrid().reloadData();
  }



}
