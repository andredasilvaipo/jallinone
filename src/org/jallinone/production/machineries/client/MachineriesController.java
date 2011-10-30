package org.jallinone.production.machineries.client;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.production.machineries.java.*;
import java.math.BigDecimal;
import javax.swing.*;
import org.jallinone.subjects.java.OrganizationVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the grid controller for machineries grid frame.</p>
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
public class MachineriesController extends GridController {

  /** grid frame */
  private MachineriesGridFrame gridFrame = null;


  public MachineriesController() {
    gridFrame = new MachineriesGridFrame(this);
    MDIFrame.add(gridFrame);
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
   * @param rowNumbers row indexes related to the new rows to save
* @param newValueObjects list of new value objects to save
* @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
    return ClientUtils.getData("insertMachineries",newValueObjects);
  }


  /**
   * Method invoked when the user has clicked on save button and the grid is in EDIT mode.
   * @param rowNumbers row indexes related to the changed rows
   * @param oldPersistentObjects old value objects, previous the changes
   * @param persistentObjects value objects relatied to the changed rows
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
   */
  public Response updateRecords(int[] rowNumbers,ArrayList oldPersistentObjects,ArrayList persistentObjects) throws Exception {
    return ClientUtils.getData("updateMachineries",new ArrayList[]{oldPersistentObjects,persistentObjects});
  }



  /**
   * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
   * @param persistentObjects value objects to delete (related to the currently selected rows)
   * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
   */
  public Response deleteRecords(ArrayList persistentObjects) throws Exception {
    return ClientUtils.getData("deleteMachineries",persistentObjects);
  }


		/**
		 * Callback method invoked each time a cell is edited: this method define if the new value is valid.
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
			MachineryVO pvo = (MachineryVO)gridFrame.getGrid().getVOListTableModel().getObjectForRow(rowNumber);
			if (attributeName.equals("companyCodeSys01PRO03") && newValue!=null) {
				pvo.setCompanyCodeSys01PRO03(newValue.toString());
				Response res =	ClientUtils.getData("loadCompany",pvo.getCompanyCodeSys01PRO03());
				if (!res.isError()) {
					OrganizationVO compVO = (OrganizationVO)((VOResponse)res).getVo();
					if (compVO.getCurrencyCodeReg03()!=null && !compVO.getCurrencyCodeReg03().equals("")) {
						pvo.setCurrencyCodeReg03PRO03(compVO.getCurrencyCodeReg03());
						gridFrame.getColCurrency().forceValidate(rowNumber);
					}
				}
			}

			return true;
		}



}
