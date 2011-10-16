package org.jallinone.items.spareparts.server;

import org.openswing.swing.server.*;
import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.items.java.*;
import org.jallinone.system.server.*;
import java.math.*;
import org.jallinone.commons.server.*;
import org.jallinone.system.progressives.server.*;
import org.jallinone.system.translations.server.*;
import org.jallinone.system.java.*;
import org.jallinone.commons.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.openswing.swing.internationalization.server.*;
import org.openswing.swing.internationalization.java.*;
import org.jallinone.system.progressives.server.*;


import javax.sql.DataSource;
import org.jallinone.documents.server.FileUtils;
import org.jallinone.items.spareparts.java.*;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.message.send.java.LookupValidationParams;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Bean used to manage item shhets in ITM25 table.</p>
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
public interface ItemSheets {

	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
	 */
	public ItemSheetLevelVO getItemSheetLevel();

	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
	 */
	public SheetSparePartVO getSheetSparePart();

	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
	 */
	public SheetAttachedDocVO getSheetAttachedDoc();

	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
	 */
	public ItemSparePartVO getItemSparePart();




	public VOResponse loadItemSheetImage(ItemSheetVO vo,String serverLanguageId,String username,String imagePath) throws Throwable;

	public VOListResponse loadItemSheets(
			GridParams pars,String serverLanguageId,String username,
			String parentCompanyCodeSys01ITM25,String parentSheetCodeItm25ITM25,
			BigDecimal levelITM25
	) throws Throwable;

	public VOListResponse validateSheetCode(LookupValidationParams pars,String serverLanguageId,String username) throws Throwable;

	public VOResponse insertItemSheet(ItemSheetVO vo,String serverLanguageId,String username,String imagePath) throws Throwable;

	public VOListResponse updateItemSheets(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username,String imagePath) throws Throwable;

	public VOResponse deleteItemSheets(ArrayList list,String serverLanguageId,String username) throws Throwable;

	public VOListResponse loadItemSheetLevels(String serverLanguageId,String username,ArrayList companiesList) throws Throwable;

	public VOListResponse insertItemSheetLevels(ArrayList vos,String serverLanguageId,String username) throws Throwable;
	public VOListResponse updateItemSheetLevels(ArrayList oldVos,ArrayList newVos,String serverLanguageId,String username) throws Throwable;
	public VOResponse deleteItemSheetLevels(ArrayList vos,String serverLanguageId,String username) throws Throwable;

	public VOListResponse loadSheetSpareParts(
			GridParams gridParams,
			String serverLanguageId,String username,
			String companyCodeSys01ITM25,String sheetCodeITM25
	) throws Throwable;


	public VOListResponse insertSheetSpareParts(ArrayList vos,String serverLanguageId,String username) throws Throwable;

	public VOResponse deleteSheetSpareParts(ArrayList vos,String serverLanguageId,String username) throws Throwable;

	public VOListResponse loadSheetAttachedDocs(GridParams gridParams,String serverLanguageId,String username) throws Throwable;

 	public VOResponse deleteSheetAttachedDocs(ArrayList list,String serverLanguageId,String username) throws Throwable;

	public VOListResponse insertSheetAttachedDocs(ArrayList list,String serverLanguageId,String username) throws Throwable;

	public VOResponse insertChildrenSheets(ItemSheetVO parentVO,List list,String serverLanguageId,String username) throws Throwable;

	public VOResponse updateSubsheet(SubsheetVO vo,String serverLanguageId,String username) throws Throwable;

	public VOResponse deleteChildrenSheets(ItemSheetVO parentVO,List list,String serverLanguageId,String username) throws Throwable;


	public VOListResponse loadItemSpareParts(
			GridParams gridParams,
			String serverLanguageId,String username,
			String companyCodeSys01ITM01,String itemCodeITM01
	) throws Throwable;



}
