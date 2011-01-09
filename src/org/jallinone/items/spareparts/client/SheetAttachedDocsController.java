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


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: This class is the grid controller for sheet's attached documents grid.</p>
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
public class SheetAttachedDocsController extends GridController {

	/** grid */
	private GridControl grid = null;


	public SheetAttachedDocsController(GridControl grid) {
		this.grid = grid;
	}


	/**
	 * Method invoked when the user has clicked on save button and the grid is in INSERT mode.
	 * @param rowNumbers row indexes related to the new rows to save
   * @param newValueObjects list of new value objects to save
   * @return an ErrorResponse value object in case of errors, VOListResponse if the operation is successfully completed
	 */
	public Response insertRecords(int[] rowNumbers, ArrayList newValueObjects) throws Exception {
		return ClientUtils.getData("insertSheetAttachedDocs",newValueObjects);
	}


	/**
	 * Callback method invoked when the user has clicked on the insert button
	 * @param valueObject empty value object just created: the user can manage it to fill some attribute values
	 */
	public void createValueObject(ValueObject valueObject) throws Exception {
		SheetAttachedDocVO vo = (SheetAttachedDocVO)valueObject;
		String companyCode = (String)grid.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01);
		String sheetCode = (String)grid.getOtherGridParams().get(ApplicationConsts.ID);
		vo.setCompanyCodeSys01ITM26(companyCode);
		vo.setSheetCodeItm25ITM26(sheetCode);
	}


	/**
	 * Method invoked when the user has clicked on delete button and the grid is in READONLY mode.
	 * @param persistentObjects value objects to delete (related to the currently selected rows)
	 * @return an ErrorResponse value object in case of errors, VOResponse if the operation is successfully completed
	 */
	public Response deleteRecords(ArrayList persistentObjects) throws Exception {
		return ClientUtils.getData("deleteItemAttachedDocs",persistentObjects);
	}


	/**
	 * Callback method invoked when the user has double clicked on the selected row of the grid.
	 * @param rowNumber selected row index
	 * @param persistentObject v.o. related to the selected row
	 */
	public void doubleClick(int rowNumber,ValueObject persistentObject) {
		SheetAttachedDocVO vo = (SheetAttachedDocVO)persistentObject;
		new DocumentController(
			null,
			new DocumentPK(vo.getCompanyCodeSys01ITM26(),vo.getProgressiveDoc14ITM26()),
			vo.getCompanyCodeSys01ITM26(),
			null
		);
	}




}
