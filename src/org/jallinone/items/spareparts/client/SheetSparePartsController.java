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
import org.jallinone.items.spareparts.java.SheetSparePartVO;
import org.jallinone.items.client.ItemController;
import org.jallinone.items.java.ItemPK;
import org.jallinone.commons.java.ApplicationConsts;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the grid controller for sheet's spare parts grid.</p>
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
public class SheetSparePartsController extends GridController {

	/** grid panel */
	private ItemSheetsGridPanel gridPanel = null;


	public SheetSparePartsController(ItemSheetsGridPanel gridPanel) {
		this.gridPanel = gridPanel;
	}


	/**
	 * Callback method invoked when the data loading is completed.
	 * @param error <code>true</code> if data loading has terminated with errors, <code>false</code> otherwise
	 */
	public void loadDataCompleted(boolean error) {
		if (!error)
			gridPanel.loadSparePartsCompleted();
	}


	public void doubleClick(int rowNumber,ValueObject persistentObject) {
		SheetSparePartVO vo = (SheetSparePartVO)persistentObject;
		if (gridPanel.getCallbacks()!=null)
			gridPanel.getCallbacks().sparePartDoubleClick(vo.getProgressiveHie02ITM01(),vo.getCompanyCodeSys01ITM27(),vo.getItemCodeItm01ITM27(),vo.getDescriptionSYS10());
		else
			new ItemController(null,new ItemPK(vo.getCompanyCodeSys01ITM27(),vo.getItemCodeItm01ITM27()),false);
	}


	/**
	 * Callback method invoked when the user has clicked on the insert button
	 * @param valueObject empty value object just created: the user can manage it to fill some attribute values
	 */
	public void createValueObject(ValueObject valueObject) throws Exception {
		SheetSparePartVO vo = (SheetSparePartVO)valueObject;
		String companyCode = (String)gridPanel.getGrid().getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01);
		String sheetCode = (String)gridPanel.getGrid().getOtherGridParams().get(ApplicationConsts.ID);
		vo.setCompanyCodeSys01ITM27(companyCode);
		vo.setSheetCodeItm25ITM27(sheetCode);
	}


	/**
	 * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
	 * @param rowNumbers row indexes related to the new rows to save
	 * @param newValueObjects list of new value objects to save
	 * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
	 */
	public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
		Response res = ClientUtils.getData("insertSheetSpareParts",newValueObjects);
		if (res.isError())
			return res;
		if (gridPanel.getItemSparePartsGrid()!=null)
			gridPanel.getItemSparePartsGrid().reloadData();
		return res;
	}


	/**
	 * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
	 * @param persistentObjects value objects to delete (related to the currently selected rows)
	 * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
	 */
	public Response deleteRecords(ArrayList persistentObjects) throws Exception {
		Response res = ClientUtils.getData("deleteSheetSpareParts",persistentObjects);
		if (res.isError())
			return res;
		if (gridPanel.getItemSparePartsGrid()!=null)
			gridPanel.getItemSparePartsGrid().reloadData();
		return res;
	}




}
