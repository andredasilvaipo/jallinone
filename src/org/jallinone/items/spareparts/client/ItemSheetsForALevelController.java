package org.jallinone.items.spareparts.client;

import org.openswing.swing.table.client.GridController;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.FilterWhereClause;
import org.openswing.swing.table.java.GridDataLocator;
import org.openswing.swing.mdi.client.MDIFrame;
import org.openswing.swing.util.client.ClientUtils;

import java.math.BigDecimal;
import javax.swing.*;
import org.jallinone.variants.java.VariantTypeVO;
import org.jallinone.items.spareparts.java.ItemSheetVO;
import org.openswing.swing.client.GridControl;
import org.jallinone.items.spareparts.java.SheetAttachedDocVO;
import org.jallinone.items.java.ItemPK;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.documents.client.DocumentController;
import org.jallinone.documents.java.DocumentPK;
import org.jallinone.items.spareparts.java.ItemSheetLevelVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the grid controller for sheets of a specific level.</p>
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
public class ItemSheetsForALevelController extends GridController {

	/** grid */
	private ItemSheetsForALevelFrame frame = null;


	public ItemSheetsForALevelController(ItemSheetsForALevelFrame frame) {
		this.frame = frame;
	}


	/**
	 * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
	 * @param rowNumbers row indexes related to the new rows to save
	 * @param newValueObjects list of new value objects to save
	 * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
	 */
	public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
		Response res = ClientUtils.getData("insertItemSheet",newValueObjects.get(0));
		if (res.isError())
			return res;
		Object vo = ((VOResponse)res).getVo();
		ArrayList vos = new ArrayList();
		vos.add(vo);
		return new VOListResponse(vos,false,vos.size());
	}



	/**
	 * Callback method invoked when the user has clicked on the insert button
	 * @param valueObject empty value object just created: the user can manage it to fill some attribute values
	 */
	public void createValueObject(ValueObject valueObject) throws Exception {
		ItemSheetVO vo = (ItemSheetVO)valueObject;
		String companyCode = frame.getLevelVO().getCompanyCodeSys01ITM29();
		vo.setCompanyCodeSys01ITM25(companyCode);
		vo.setLevITM25(frame.getLevelVO().getLevITM29());
		frame.getImagePanel().setImage(new byte[0]);
	}



	/**
	 * Callback method invoked when the data loading is completed.
	 * @param error <code>true</code> if data loading has terminated with errors, <code>false</code> otherwise
	 */
	public void loadDataCompleted(boolean error) {
		frame.getImagePanel().setImage(new byte[0]);
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
		ItemSheetVO vo = (ItemSheetVO)frame.getGrid().getVOListTableModel().getObjectForRow(rowNumber);
		if (attributeName.equals("sheetCodeITM25") && newValue!=null && (vo.getDescriptionSYS10()==null || vo.getDescriptionSYS10().equals("")))
			vo.setDescriptionSYS10(newValue.toString());
		if (attributeName.equals("imageITM25"))
			frame.getImagePanel().setImage((byte[])newValue);
		return true;
	}


	/**
	 * Method invoked when the user has clicked on save button and the grid is in EDIT mode.
	 * @param rowNumbers row indexes related to the changed/new rows
	 * @param oldPersistentObjects old value objects, previous the changes; it can contains null objects, in case of new inserted rows
	 * @param persistentObjects value objects related to the changed/new rows
	 * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
	 */
	public Response updateRecords(int[] rowNumbers,ArrayList oldPersistentObjects,ArrayList persistentObjects) throws Exception {
		return ClientUtils.getData("updateItemSheets",new Object[]{oldPersistentObjects,persistentObjects});
	}


	/**
	 * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
	 * @param persistentObjects value objects to delete (related to the currently selected rows)
	 * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
	 */
	public Response deleteRecords(ArrayList persistentObjects) throws Exception {
		// check if levels are contigous and are not still used...
		Response res = ClientUtils.getData("deleteItemSheets",persistentObjects);
		if (!res.isError())
				frame.getImagePanel().setImage(new byte[0]);
		return res;
	}


	/**
	 * Callback method invoked when the user has double clicked on the selected row of the grid.
	 * @param rowNumber selected row index
	 * @param persistentObject v.o. related to the selected row
	 */
	public void doubleClick(int rowNumber,ValueObject persistentObject) {
		ItemSheetVO vo = (ItemSheetVO)persistentObject;
		Response res = ClientUtils.getData("loadItemSheetImage",vo);
		if (!res.isError()) {
			vo = (ItemSheetVO)((VOResponse)res).getVo();
			frame.getImagePanel().setImage(vo.getImageITM25());
		}
	}


}
