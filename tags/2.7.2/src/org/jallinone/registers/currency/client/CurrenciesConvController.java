package org.jallinone.registers.currency.client;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.client.ClientUtils;
import org.jallinone.registers.currency.java.*;
import java.math.BigDecimal;
import javax.swing.*;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.client.GridControl;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the grid controller for currency conversions grid frame.</p>
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
public class CurrenciesConvController extends GridController {

  /** grid frame */
  private CurrenciesGridFrame gridFrame = null;


  public CurrenciesConvController(CurrenciesGridFrame gridFrame) {
    this.gridFrame = gridFrame;
  }


		/**
		 * Callback method invoked on pressing INSERT button.
		 * @return <code>true</code> allows to go to INSERT mode, <code>false</code> the mode change is interrupted
		 */
		public boolean beforeInsertGrid(GridControl grid) {
			return gridFrame.getGrid().getVOListTableModel().getRowCount()>0;
		}


		/**
		 * Callback method invoked when the user has clicked on the insert button
		 * @param valueObject empty value object just created: the user can manage it to fill some attribute values
		 */
		public void createValueObject(ValueObject valueObject) throws Exception {
			String currCode = (String)gridFrame.getConvGridControl().getOtherGridParams().get(ApplicationConsts.CURRENCY_CODE_REG03);
			CurrencyConvVO vo = (CurrencyConvVO)valueObject;
			vo.setStartDateREG06(new java.sql.Date(System.currentTimeMillis()));
			vo.setCurrencyCodeReg03REG06(currCode);
		}



		/**
		 * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
		 * @param rowNumbers row indexes related to the new rows to save
		 * @param newValueObjects list of new value objects to save
	 	 * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
		 */
		public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
			Response res = ClientUtils.getData("insertCurrencyConvs",newValueObjects);
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
    return ClientUtils.getData("updateCurrencyConvs",new ArrayList[]{oldPersistentObjects,persistentObjects});
  }



		/**
		 * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
		 * @param persistentObjects value objects to delete (related to the currently selected rows)
		 * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
		 */
		public Response deleteRecords(ArrayList persistentObjects) throws Exception {
			Response res = ClientUtils.getData("deleteCurrencyConvs",persistentObjects);
			return res;
		}


}
