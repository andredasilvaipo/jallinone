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
import org.openswing.swing.util.java.Consts;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the grid controller for sheet's levels.</p>
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
public class SheetLevelsController extends GridController {

	/** grid */
	private SheetLevelsFrame frame = null;


	public SheetLevelsController(SheetLevelsFrame frame) {
		this.frame = frame;
	}


	/**
	 * @param grid grid
	 * @param row selected row index
	 * @param attributeName attribute name that identifies the selected grid column
	 * @return <code>true</code> if the selected cell is editable, <code>false</code> otherwise
	 */
	public boolean isCellEditable(GridControl grid,int row,String attributeName) {
		if (frame.getGridLevels().getMode()!=Consts.READONLY &&
				row==0 &&
				!attributeName.equals("descriptionSYS10"))
			// no properties defined for first level (item models...)
			return false;
		return grid.isFieldEditable(row,attributeName);
	}


	/**
	 * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
	 * @param rowNumbers row indexes related to the new rows to save
	 * @param newValueObjects list of new value objects to save
	 * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
	 */
	public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
		return ClientUtils.getData("insertItemSheetLevels",newValueObjects);
	}


	/**
	 * Callback method invoked when the user has clicked on the insert button
	 * @param valueObject empty value object just created: the user can manage it to fill some attribute values
	 */
	public void createValueObject(ValueObject valueObject) throws Exception {
		ItemSheetLevelVO vo = (ItemSheetLevelVO)valueObject;
		String companyCode = (String)frame.getCompanyCodeSys01();
		BigDecimal level = new BigDecimal(frame.getGridLevels().getVOListTableModel().getRowCount());
		vo.setCompanyCodeSys01ITM29(companyCode);
		vo.setLevITM29(level);
	}


	/**
	 * Method invoked when the user has clicked on save button and the grid is in EDIT mode.
	 * @param rowNumbers row indexes related to the changed/new rows
	 * @param oldPersistentObjects old value objects, previous the changes; it can contains null objects, in case of new inserted rows
	 * @param persistentObjects value objects related to the changed/new rows
	 * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
	 */
	public Response updateRecords(int[] rowNumbers,ArrayList oldPersistentObjects,ArrayList persistentObjects) throws Exception {
		return ClientUtils.getData("updateItemSheetLevels",new Object[]{oldPersistentObjects,persistentObjects});
	}


	/**
	 * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
	 * @param persistentObjects value objects to delete (related to the currently selected rows)
	 * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
	 */
	public Response deleteRecords(ArrayList persistentObjects) throws Exception {
		// check if levels are contigous and are not still used...

		return ClientUtils.getData("deleteItemSheetLevels",persistentObjects);
	}




}
