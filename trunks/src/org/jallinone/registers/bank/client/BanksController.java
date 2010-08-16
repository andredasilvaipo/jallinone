package org.jallinone.registers.bank.client;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.registers.bank.java.*;
import java.math.BigDecimal;
import javax.swing.*;
import org.openswing.swing.util.java.Consts;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the grid controller for banks grid frame.</p>
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
public class BanksController extends GridController {

  /** grid frame */
  private BanksFrame frame = null;


  public BanksController() {
    frame = new BanksFrame(this);
    MDIFrame.add(frame);
    frame.getFormPanel().setMode(Consts.READONLY);
  }


  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    Response res = ClientUtils.getData("deleteBanks",persistentObjects);
    if (!res.isError()) {
      frame.getBankController().setBank(null);
      frame.getFormPanel().setMode(Consts.INSERT);
      frame.getFormPanel().setMode(Consts.READONLY);
    }
    return res;
  }


//  /**
//   * Callback method invoked when the data loading is completed.
//   * @param error <code>true</code> if data loading has terminated with errors, <code>false</code> otherwise
//   */
//  public void loadDataCompleted(boolean error) {
//    if (frame.getGrid().getVOListTableModel().getRowCount()>0)
//      doubleClick(0,frame.getGrid().getVOListTableModel().getObjectForRow(0));
//  }



  /**
   * Callback method invoked when the user has double clicked on the selected row of the grid.
   * @param rowNumber selected row index
   * @param persistentObject v.o. related to the selected row
   */
  public void doubleClick(int rowNumber,ValueObject persistentObject) {
    BankVO vo = (BankVO)persistentObject;
    frame.getBankController().setBank(vo);
    frame.getFormPanel().setMode(Consts.READONLY);
//    frame.getFormPanel().reload();
    frame.getFormPanel().executeReload();
    frame.getFormPanel().setFocusOnForm();
  }



}
