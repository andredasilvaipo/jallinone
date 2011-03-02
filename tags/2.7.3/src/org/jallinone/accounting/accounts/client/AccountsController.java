package org.jallinone.accounting.accounts.client;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.accounting.accounts.java.*;
import java.math.BigDecimal;
import javax.swing.*;
import org.jallinone.commons.client.CompanyGridController;
import org.openswing.swing.client.GridControl;
import org.openswing.swing.util.client.ClientSettings;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the grid controller for accounts grid frame.</p>
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
public class AccountsController extends CompanyGridController {

  /** grid frame */
  private AccountsGridFrame gridFrame = null;


  public AccountsController() {
    this(null,null);
  }


  public AccountsController(String companyCodeSys01ACC01,String ledgerCode) {
    gridFrame = new AccountsGridFrame(this);
    MDIFrame.add(gridFrame);

    if (companyCodeSys01ACC01!=null && ledgerCode!=null) {
      gridFrame.getControlCompaniesCombo().setValue(companyCodeSys01ACC01);
      gridFrame.getControlLedgerCode().setValue(ledgerCode);
      gridFrame.getControlLedgerCode().getLookupController().forceValidate();
    }

  }


  /**
   * Callback method invoked before saving data when the grid was in INSERT mode (on pressing save button).
   * @return <code>true</code> allows the saving to continue, <code>false</code> the saving is interrupted
   */
  public boolean beforeInsertGrid(GridControl grid) {
    boolean ok = super.beforeInsertGrid(grid);
    if (!ok)
      return ok;

    if (gridFrame.getControlCompaniesCombo().getValue()==null ||
        gridFrame.getControlLedgerCode().getValue()==null ||
        gridFrame.getControlLedgerCode().getValue().equals("")   ) {
      JOptionPane.showMessageDialog(
          MDIFrame.getInstance(),
          ClientSettings.getInstance().getResources().getResource("insert not allowed"),
          ClientSettings.getInstance().getResources().getResource("Attention"),
          JOptionPane.WARNING_MESSAGE);
      return false;
    }
    return true;
  }


  /**
   * Callback method invoked when the user has clicked on the insert button
   * @param valueObject empty value object just created: the user can manage it to fill some attribute values
   */
  public void createValueObject(ValueObject valueObject) throws Exception {
    AccountVO vo = (AccountVO)valueObject;
    vo.setCompanyCodeSys01ACC02(gridFrame.getControlCompaniesCombo().getValue().toString());
    vo.setLedgerCodeAcc01ACC02(gridFrame.getControlLedgerCode().getValue().toString());
    vo.setLedgerDescriptionACC02(gridFrame.getControlLedgerDescr().getValue().toString());
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
   * @param rowNumbers row indexes related to the new rows to save
* @param newValueObjects list of new value objects to save
* @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
    return ClientUtils.getData("insertAccounts",newValueObjects);
  }

  /**
   * Method invoked when the user has clicked on save button and the grid is in EDIT mode.
   * @param rowNumbers row indexes related to the changed rows
   * @param oldPersistentObjects old value objects, previous the changes
   * @param persistentObjects value objects relatied to the changed rows
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response updateRecords(int[] rowNumbers,ArrayList oldPersistentObjects,ArrayList persistentObjects) throws Exception {
    return ClientUtils.getData("updateAccounts",new ArrayList[]{oldPersistentObjects,persistentObjects});
  }



  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    AccountVO vo = null;
    for(int i=0;i<persistentObjects.size();i++) {
      vo = (AccountVO)persistentObjects.get(i);
      if (!vo.getCanDelACC02().booleanValue())
        return new ErrorResponse("deleting not allowed");
    }

    return ClientUtils.getData("deleteAccounts",persistentObjects);
  }




}
