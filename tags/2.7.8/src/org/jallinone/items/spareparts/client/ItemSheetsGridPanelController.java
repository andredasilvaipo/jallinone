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


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the grid controller for sheets grid panel.</p>
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
public class ItemSheetsGridPanelController extends GridController {

	/** grid panel */
	private ItemSheetsGridPanel gridPanel = null;


	public ItemSheetsGridPanelController(ItemSheetsGridPanel gridPanel) {
		this.gridPanel = gridPanel;
	}


	/**
	 * Callback method invoked when the user has selected another row.
	 * @param rowNumber selected row index
	 */
	public void rowChanged(int rowNumber) {
		gridPanel.getImgPanel().repaint();
	}


	/**
	 * Callback method invoked when the user has double clicked on the selected row of the grid.
	 * @param rowNumber selected row index
	 * @param persistentObject v.o. related to the selected row
	 */
	public void doubleClick(int rowNumber,ValueObject persistentObject) {
		ItemSheetVO vo = (ItemSheetVO)persistentObject;
		gridPanel.addLink(vo);
	}


	/**
	 * Callback method invoked when the data loading is completed.
	 * @param error <code>true</code> if data loading has terminated with errors, <code>false</code> otherwise
	 */
	public void loadDataCompleted(boolean error) {
		gridPanel.getMenuStart().setEnabled( gridPanel.getGrid().getVOListTableModel().getRowCount()>0 );
		gridPanel.getMenuSave().setEnabled( gridPanel.getGrid().getVOListTableModel().getRowCount()>0 );
		gridPanel.getPoints().clear();
		gridPanel.loadSheetsCompleted();
	}



	/**
	 * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
	 * @param rowNumbers row indexes related to the new rows to save
	 * @param newValueObjects list of new value objects to save
	 * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
	 */
	public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
		Response res = ClientUtils.getData("insertChildrenSheets",new Object[]{gridPanel.getParentVO(),newValueObjects});
		if (res.isError())
			return res;
		return new VOListResponse(newValueObjects,false,newValueObjects.size());
	}


	/**
	 * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
	 * @param persistentObjects value objects to delete (related to the currently selected rows)
	 * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
	 */
	public Response deleteRecords(ArrayList persistentObjects) throws Exception {
		return ClientUtils.getData("deleteChildrenSheets",new Object[]{gridPanel.getParentVO(),persistentObjects});
	}




}
