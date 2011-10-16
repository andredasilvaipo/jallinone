package org.jallinone.warehouse.client;

import org.jallinone.commons.client.CompanyGridController;
import org.openswing.swing.client.GridControl;
import org.openswing.swing.message.receive.java.ValueObject;
import org.jallinone.warehouse.java.InventoryVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.util.client.ClientUtils;
import java.util.ArrayList;
import org.openswing.swing.message.receive.java.Response;
import org.jallinone.warehouse.java.InventoryItemVO;
import java.math.BigDecimal;
import javax.swing.SwingUtilities;
import org.openswing.swing.message.receive.java.VOResponse;
import org.openswing.swing.message.receive.java.VOListResponse;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description:Grid Controller used for inventory items grid.</p>
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
public class InventoryItemsController extends CompanyGridController {

	/** inventory items frame */
	private InventoryItemsFrame frame;

	public InventoryItemsController(InventoryItemsFrame frame) {
		this.frame = frame;
	}


	/**
	 * @param grid grid
	 * @param row selected row index
	 * @param attributeName attribute name that identifies the selected grid column
	 * @return <code>true</code> if the selected cell is editable, <code>false</code> otherwise
	 */
	public boolean isCellEditable(GridControl grid,int row,String attributeName) {
		InventoryItemVO vo = (InventoryItemVO)grid.getVOListTableModel().getObjectForRow(row);
		if (attributeName.equals("realQtyWAR07") ||
				attributeName.equals("realProgressiveHie01WAR07"))
			return vo.getStateWAR07().equals(ApplicationConsts.CONFIRMED);
		return super.isCellEditable(grid,row,attributeName);
	}


	/**
	 * Callback method invoked when the user has clicked on the insert button
	 * @param valueObject empty value object just created: the user can manage it to fill some attribute values
	 */
	public void createValueObject(ValueObject valueObject) throws Exception {
		InventoryVO inventoryVO = frame.getInventory();
		InventoryItemVO vo = (InventoryItemVO)valueObject;
		vo.setCompanyCodeSys01WAR07(inventoryVO.getCompanyCodeSys01WAR06());
		vo.setProgressiveWar06WAR07(inventoryVO.getProgressiveWAR06());
		vo.setQtyWAR07(new BigDecimal(1));
		vo.setStateWAR07(ApplicationConsts.OPENED);
	}




	/**
	 * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
	 * @param rowNumbers row indexes related to the new rows to save
	 * @param newValueObjects list of new value objects to save
	 * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
	 */
	public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
		Response res = ClientUtils.getData("insertInventoryItem",newValueObjects.get(0));
		if (frame.getGrid().getVOListTableModel().getRowCount()>0)
			frame.getButtonConfirm().setEnabled(true);
		if (res.isError())
			return res;
		ArrayList rows = new ArrayList();
		rows.add( ((VOResponse)res).getVo() );
		return new VOListResponse(rows, false, rows.size() );
	}


	/**
	 * Method invoked when the user has clicked on save button and the grid is in EDIT mode.
	 * @param rowNumbers row indexes related to the changed rows
	 * @param oldPersistentObjects old value objects, previous the changes
	 * @param persistentObjects value objects relatied to the changed rows
	 * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
	 */
	public Response updateRecords(int[] rowNumbers,ArrayList oldPersistentObjects,ArrayList persistentObjects) throws Exception {
		return ClientUtils.getData("updateInventoryItems",new Object[]{oldPersistentObjects,persistentObjects});
	}



	/**
	 * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
	 * @param persistentObjects value objects to delete (related to the currently selected rows)
	 * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
	 */
	public Response deleteRecords(ArrayList persistentObjects) throws Exception {
		Response res = ClientUtils.getData("deleteInventoryItems",persistentObjects);

		SwingUtilities.invokeLater(new Runnable() {

	    public void run() {
				if (frame.getGrid().getVOListTableModel().getRowCount()>0)
					frame.getButtonConfirm().setEnabled(true);
				else
					frame.getButtonConfirm().setEnabled(false);
			}

		});

		return res;
	}


	/**
	 * Callback method invoked when the data loading is completed.
	 * @param error <code>true</code> if data loading has terminated with errors, <code>false</code> otherwise
	 */
	public void loadDataCompleted(boolean error) {
		if (frame.getGrid().getVOListTableModel().getRowCount()>0)
			frame.getButtonConfirm().setEnabled(true);
		else
			frame.getButtonConfirm().setEnabled(false);
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
		if ("qtyWAR07".equals(attributeName) || "realQtyWAR07".equals(attributeName)) {
			InventoryItemVO vo = (InventoryItemVO)frame.getGrid().getVOListTableModel().getObjectForRow(rowNumber);
			if (vo.getQtyWAR07()!=null && vo.getRealQtyWAR07()!=null)
				vo.setDeltaQtyWAR07(vo.getRealQtyWAR07().subtract(vo.getQtyWAR07()));
		}
		return true;
	}


}
