package org.jallinone.warehouse.client;

import org.jallinone.commons.client.CompanyGridController;
import org.openswing.swing.client.GridControl;
import org.openswing.swing.message.receive.java.ValueObject;
import org.jallinone.warehouse.java.InventoryVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.util.client.ClientUtils;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOResponse;
import org.openswing.swing.message.receive.java.VOListResponse;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description:Grid Controller used for inventory list grid.</p>
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
public class InventoryListController extends CompanyGridController {

  /** inventory list frame */
  private InventoryListFrame frame;

	public InventoryListController(InventoryListFrame frame) {
		this.frame = frame;
	}



	/**
	 * Callback method invoked when the user has double clicked on the selected row of the grid.
	 * @param rowNumber selected row index
	 * @param persistentObject v.o. related to the selected row
	 */
	public void doubleClick(int rowNumber,ValueObject persistentObject) {
		InventoryVO vo = (InventoryVO)persistentObject;
		InventoryItemsFrame f = new InventoryItemsFrame(frame,vo);
		f.setParentFrame(frame);
		frame.pushFrame(f);
	}


	/**
	 * @param grid grid
	 * @param row selected row index
	 * @param attributeName attribute name that identifies the selected grid column
	 * @return <code>true</code> if the selected cell is editable, <code>false</code> otherwise
	 */
	public boolean isCellEditable(GridControl grid,int row,String attributeName) {
		InventoryVO vo = (InventoryVO)grid.getVOListTableModel().getObjectForRow(row);
		if (!vo.getStateWAR06().equals(ApplicationConsts.OPENED))
			return false;
		return super.isCellEditable(grid,row,attributeName);
	}



	/**
	 * Callback method invoked before saving data when the grid was in INSERT mode (on pressing save button).
	 * @return <code>true</code> allows the saving to continue, <code>false</code> the saving is interrupted
	 */
	public boolean beforeInsertGrid(GridControl grid) {
		if (!super.beforeInsertGrid(grid))
			return false;

		if (frame.getCompanyCodeSys01()==null ||
				frame.getWarehouseCodeWar01()==null ||
				frame.getItemType()==null)
			return false;

		return true;
	}


	/**
	 * Callback method invoked when the user has clicked on the insert button
	 * @param valueObject empty value object just created: the user can manage it to fill some attribute values
	 */
	public void createValueObject(ValueObject valueObject) throws Exception {
		InventoryVO vo = (InventoryVO)valueObject;
		vo.setCompanyCodeSys01WAR06(frame.getCompanyCodeSys01());
		vo.setWarehouseCodeWar01WAR06(frame.getWarehouseCodeWar01());
		vo.setStartDateWAR06(new java.sql.Date(System.currentTimeMillis()));
		vo.setStateWAR06(ApplicationConsts.OPENED);
		vo.setItemTypeWAR06(frame.getItemType());
	}




	/**
	 * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
	 * @param rowNumbers row indexes related to the new rows to save
	 * @param newValueObjects list of new value objects to save
	 * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
	 */
	public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
		Response res = ClientUtils.getData("insertInventory",newValueObjects.get(0));
		if (res.isError())
			return res;
		ArrayList rows = new ArrayList();
		rows.add(((VOResponse)res).getVo());
		return new VOListResponse(rows,false,rows.size());
	}


	/**
	 * Method invoked when the user has clicked on save button and the grid is in EDIT mode.
	 * @param rowNumbers row indexes related to the changed rows
	 * @param oldPersistentObjects old value objects, previous the changes
	 * @param persistentObjects value objects relatied to the changed rows
	 * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
	 */
	public Response updateRecords(int[] rowNumbers,ArrayList oldPersistentObjects,ArrayList persistentObjects) throws Exception {
		return ClientUtils.getData("updateInventories",new Object[]{oldPersistentObjects,persistentObjects});
	}



	/**
	 * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
	 * @param persistentObjects value objects to delete (related to the currently selected rows)
	 * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
	 */
	public Response deleteRecords(ArrayList persistentObjects) throws Exception {
		return ClientUtils.getData("deleteInventories",persistentObjects);
	}




}
