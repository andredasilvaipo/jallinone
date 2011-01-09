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
import org.jallinone.items.spareparts.java.ItemSheetVO;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.message.send.java.LookupValidationParams;
import org.jallinone.items.spareparts.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Bean used to manage item sheets in ITM25 table.</p>
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
public class ItemSheetsBean implements ItemSheets {


	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/** external connection */
	private Connection conn = null;

	/**
	 * Set external connection.
	 */
	public void setConn(Connection conn) {
		this.conn = conn;
	}

	/**
	 * Create local connection
	 */
	public Connection getConn() throws Exception {
		Connection c = dataSource.getConnection(); c.setAutoCommit(false); return c;
	}


	public ItemSheetsBean() {
	}


	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
	 */
	public ItemSheetLevelVO getItemSheetLevel() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
	 */
	public SheetSparePartVO getSheetSparePart() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
	 */
	public ItemSparePartVO getItemSparePart() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
	 */
	public SheetAttachedDocVO getSheetAttachedDoc() {
		throw new UnsupportedOperationException();
	}




	public VOResponse loadItemSheetImage(ItemSheetVO vo,String serverLanguageId,String username,String imagePath) throws Throwable {
		try {
			if (vo.getImageNameITM25()!=null) {
				// load image from file system...
				String appPath = imagePath;
				appPath = appPath.replace('\\','/');
				if (!appPath.endsWith("/"))
					appPath += "/";
				if (!new File(appPath).isAbsolute()) {
					// relative path (to "WEB-INF/classes/" folder)
					appPath = this.getClass().getResource("/").getPath().replaceAll("%20"," ")+appPath;
				}
				File f = new File(appPath+vo.getImageNameITM25());
				byte[] bytes = new byte[(int)f.length()];
				FileInputStream in = new FileInputStream(f);
				in.read(bytes);
				in.close();
				vo.setImageITM25(bytes);
			}
			return new VOResponse(vo);

		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching item sheet's image",ex);
			throw new Exception(ex.getMessage());
		}
	}


	/**
	 * Business logic to execute.
	 */
	public VOListResponse loadItemSheets(
	    GridParams pars,String serverLanguageId,String username,
			String parentCompanyCodeSys01ITM25,String parentSheetCodeItm25ITM25,
			BigDecimal levITM25
	) throws Throwable {
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;

			Map attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01ITM25","ITM25_ITEM_SHEETS.COMPANY_CODE_SYS01");
			attribute2dbField.put("sheetCodeITM25","ITM25_ITEM_SHEETS.SHEET_CODE");
			attribute2dbField.put("progressiveSys10ITM25","ITM25_ITEM_SHEETS.PROGRESSIVE_SYS10");
			attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
			attribute2dbField.put("imageNameITM25","ITM25_ITEM_SHEETS.IMAGE_NAME");
			attribute2dbField.put("levITM25","ITM25_ITEM_SHEETS.LEV");

			attribute2dbField.put("sProp0ITM25","ITM25_ITEM_SHEETS.S_PROP0");
			attribute2dbField.put("sProp1ITM25","ITM25_ITEM_SHEETS.S_PROP1");
			attribute2dbField.put("sProp2ITM25","ITM25_ITEM_SHEETS.S_PROP2");
			attribute2dbField.put("sProp3ITM25","ITM25_ITEM_SHEETS.S_PROP3");
			attribute2dbField.put("sProp4ITM25","ITM25_ITEM_SHEETS.S_PROP4");
			attribute2dbField.put("sProp5ITM25","ITM25_ITEM_SHEETS.S_PROP5");
			attribute2dbField.put("sProp6ITM25","ITM25_ITEM_SHEETS.S_PROP6");
			attribute2dbField.put("sProp7ITM25","ITM25_ITEM_SHEETS.S_PROP7");
			attribute2dbField.put("sProp8ITM25","ITM25_ITEM_SHEETS.S_PROP8");
			attribute2dbField.put("sProp9ITM25","ITM25_ITEM_SHEETS.S_PROP9");

			attribute2dbField.put("dProp0ITM25","ITM25_ITEM_SHEETS.D_PROP0");
			attribute2dbField.put("dProp1ITM25","ITM25_ITEM_SHEETS.D_PROP1");
			attribute2dbField.put("dProp2ITM25","ITM25_ITEM_SHEETS.D_PROP2");
			attribute2dbField.put("dProp3ITM25","ITM25_ITEM_SHEETS.D_PROP3");
			attribute2dbField.put("dProp4ITM25","ITM25_ITEM_SHEETS.D_PROP4");
			attribute2dbField.put("dProp5ITM25","ITM25_ITEM_SHEETS.D_PROP5");
			attribute2dbField.put("dProp6ITM25","ITM25_ITEM_SHEETS.D_PROP6");
			attribute2dbField.put("dProp7ITM25","ITM25_ITEM_SHEETS.D_PROP7");
			attribute2dbField.put("dProp8ITM25","ITM25_ITEM_SHEETS.D_PROP8");
			attribute2dbField.put("dProp9ITM25","ITM25_ITEM_SHEETS.D_PROP9");

			attribute2dbField.put("nProp0ITM25","ITM25_ITEM_SHEETS.N_PROP0");
			attribute2dbField.put("nProp1ITM25","ITM25_ITEM_SHEETS.N_PROP1");
			attribute2dbField.put("nProp2ITM25","ITM25_ITEM_SHEETS.N_PROP2");
			attribute2dbField.put("nProp3ITM25","ITM25_ITEM_SHEETS.N_PROP3");
			attribute2dbField.put("nProp4ITM25","ITM25_ITEM_SHEETS.N_PROP4");
			attribute2dbField.put("nProp5ITM25","ITM25_ITEM_SHEETS.N_PROP5");
			attribute2dbField.put("nProp6ITM25","ITM25_ITEM_SHEETS.N_PROP6");
			attribute2dbField.put("nProp7ITM25","ITM25_ITEM_SHEETS.N_PROP7");
			attribute2dbField.put("nProp8ITM25","ITM25_ITEM_SHEETS.N_PROP8");
			attribute2dbField.put("nProp9ITM25","ITM25_ITEM_SHEETS.N_PROP9");

			// possibile usages:
			// list of children sheets, starting from a parent sheet (for each subsheet, retrieve also SubsheetVO subobject)
			// list of possible sheets to add to a parent sheet, starting from a specific level (to filter) and a parent sheet to use to exclude children sheets
			// list of possible sheets having a specified level...

			String select =
					"select ITM25_ITEM_SHEETS.COMPANY_CODE_SYS01,ITM25_ITEM_SHEETS.SHEET_CODE,"+
					"ITM25_ITEM_SHEETS.PROGRESSIVE_SYS10,SYS10_TRANSLATIONS.DESCRIPTION,"+
					"ITM25_ITEM_SHEETS.IMAGE_NAME,ITM25_ITEM_SHEETS.LEV,"+
					"ITM25_ITEM_SHEETS.S_PROP0,ITM25_ITEM_SHEETS.S_PROP1,ITM25_ITEM_SHEETS.S_PROP2,ITM25_ITEM_SHEETS.S_PROP3,ITM25_ITEM_SHEETS.S_PROP4,"+
					"ITM25_ITEM_SHEETS.S_PROP5,ITM25_ITEM_SHEETS.S_PROP6,ITM25_ITEM_SHEETS.S_PROP7,ITM25_ITEM_SHEETS.S_PROP8,ITM25_ITEM_SHEETS.S_PROP9,"+
					"ITM25_ITEM_SHEETS.D_PROP0,ITM25_ITEM_SHEETS.D_PROP1,ITM25_ITEM_SHEETS.D_PROP2,ITM25_ITEM_SHEETS.D_PROP3,ITM25_ITEM_SHEETS.D_PROP4,"+
					"ITM25_ITEM_SHEETS.D_PROP5,ITM25_ITEM_SHEETS.D_PROP6,ITM25_ITEM_SHEETS.D_PROP7,ITM25_ITEM_SHEETS.D_PROP8,ITM25_ITEM_SHEETS.D_PROP9,"+
					"ITM25_ITEM_SHEETS.N_PROP0,ITM25_ITEM_SHEETS.N_PROP1,ITM25_ITEM_SHEETS.N_PROP2,ITM25_ITEM_SHEETS.N_PROP3,ITM25_ITEM_SHEETS.N_PROP4,"+
					"ITM25_ITEM_SHEETS.N_PROP5,ITM25_ITEM_SHEETS.N_PROP6,ITM25_ITEM_SHEETS.N_PROP7,ITM25_ITEM_SHEETS.N_PROP8,ITM25_ITEM_SHEETS.N_PROP9 ";
			String from =
					" from ITM25_ITEM_SHEETS,SYS10_TRANSLATIONS ";
			String where =
				  " where "+
					"ITM25_ITEM_SHEETS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
					"SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
					"ITM25_ITEM_SHEETS.ENABLED='Y' ";

			ArrayList values = new ArrayList();
			values.add(serverLanguageId);

			if (parentCompanyCodeSys01ITM25!=null && parentSheetCodeItm25ITM25!=null && levITM25==null) {
				// list of children sheets, starting from a parent sheet
				select +=
					",ALIAS.POLYGON ";
				from +=
					",ITM30_SUB_SHEETS ALIAS ";
				where +=
					" and ITM25_ITEM_SHEETS.COMPANY_CODE_SYS01=? "+
					" and ALIAS.COMPANY_CODE_SYS01=ITM25_ITEM_SHEETS.COMPANY_CODE_SYS01 "+
					" and ALIAS.SHEET_CODE_ITM25=ITM25_ITEM_SHEETS.SHEET_CODE "+
				  " and ALIAS.PARENT_SHEET_CODE_ITM25=? "+
					" and NOT ALIAS.PARENT_SHEET_CODE_ITM25=ITM25_ITEM_SHEETS.SHEET_CODE ";

				values.add(parentCompanyCodeSys01ITM25);
			  values.add(parentSheetCodeItm25ITM25);

				attribute2dbField.put("subsheet.polygonITM30","ALIAS.POLYGON");
			}
			else if (parentCompanyCodeSys01ITM25!=null && parentSheetCodeItm25ITM25!=null && levITM25!=null) {
				// list of possible sheets to add to a parent sheet, starting from a specific level (to filter) and a parent sheet to use to exclude children sheets
				where +=
					" and ITM25_ITEM_SHEETS.COMPANY_CODE_SYS01=? "+
					" and ITM25_ITEM_SHEETS.LEV=? "+
					" and ITM25_ITEM_SHEETS.SHEET_CODE not in (select SHEET_CODE_ITM25 from ITM30_SUB_SHEETS where "+
					"   COMPANY_CODE_SYS01=? and "+
					"   PARENT_SHEET_CODE_ITM25=? "+
					" ) ";

				values.add(parentCompanyCodeSys01ITM25);
				values.add(levITM25);
				values.add(parentCompanyCodeSys01ITM25);
				values.add(parentSheetCodeItm25ITM25);
			}
			else if (parentCompanyCodeSys01ITM25!=null && parentSheetCodeItm25ITM25==null && levITM25!=null) {
				// list of possible sheets having a specified level...
				where +=
					" and ITM25_ITEM_SHEETS.COMPANY_CODE_SYS01=? "+
					" and ITM25_ITEM_SHEETS.LEV=? ";

				values.add(parentCompanyCodeSys01ITM25);
				values.add(levITM25);
			}


			// read from ITM25 table...
			Response answer = QueryUtil.getQuery(
					conn,
					new UserSessionParameters(username),
					select+from+where,
					values,
					attribute2dbField,
					ItemSheetVO.class,
					"Y",
					"N",
					null,
					pars,
					50,
					true
			);

			if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching item sheets list",ex);
			throw new Exception(ex.getMessage());
		}
		finally {
				try {
						pstmt.close();
				}
				catch (Exception exx) {}
				try {
						if (this.conn==null && conn!=null) {
								// close only local connection
								conn.commit();
								conn.close();
						}
				}
				catch (Exception exx) {}
		}
	}


	public VOListResponse validateSheetCode(LookupValidationParams pars,String serverLanguageId,String username) throws Throwable {
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;

			String companyCodeSYS10 = (String)pars.getLookupValidationParameters().get(ApplicationConsts.COMPANY_CODE_SYS01);
			String parentCode = (String)pars.getLookupValidationParameters().get(ApplicationConsts.ID);
			BigDecimal levITM25 = (BigDecimal)pars.getLookupValidationParameters().get(ApplicationConsts.LEVEL);

			Map attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01ITM25","ITM25_ITEM_SHEETS.COMPANY_CODE_SYS01");
			attribute2dbField.put("sheetCodeITM25","ITM25_ITEM_SHEETS.SHEET_CODE");
			attribute2dbField.put("progressiveSys10ITM25","ITM25_ITEM_SHEETS.PROGRESSIVE_SYS10");
			attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
			attribute2dbField.put("imageNameITM25","ITM25_ITEM_SHEETS.IMAGE_NAME");
			attribute2dbField.put("levITM25","ITM25_ITEM_SHEETS.LEV");

			attribute2dbField.put("sProp0ITM25","ITM25_ITEM_SHEETS.S_PROP0");
			attribute2dbField.put("sProp1ITM25","ITM25_ITEM_SHEETS.S_PROP1");
			attribute2dbField.put("sProp2ITM25","ITM25_ITEM_SHEETS.S_PROP2");
			attribute2dbField.put("sProp3ITM25","ITM25_ITEM_SHEETS.S_PROP3");
			attribute2dbField.put("sProp4ITM25","ITM25_ITEM_SHEETS.S_PROP4");
			attribute2dbField.put("sProp5ITM25","ITM25_ITEM_SHEETS.S_PROP5");
			attribute2dbField.put("sProp6ITM25","ITM25_ITEM_SHEETS.S_PROP6");
			attribute2dbField.put("sProp7ITM25","ITM25_ITEM_SHEETS.S_PROP7");
			attribute2dbField.put("sProp8ITM25","ITM25_ITEM_SHEETS.S_PROP8");
			attribute2dbField.put("sProp9ITM25","ITM25_ITEM_SHEETS.S_PROP9");

			attribute2dbField.put("dProp0ITM25","ITM25_ITEM_SHEETS.D_PROP0");
			attribute2dbField.put("dProp1ITM25","ITM25_ITEM_SHEETS.D_PROP1");
			attribute2dbField.put("dProp2ITM25","ITM25_ITEM_SHEETS.D_PROP2");
			attribute2dbField.put("dProp3ITM25","ITM25_ITEM_SHEETS.D_PROP3");
			attribute2dbField.put("dProp4ITM25","ITM25_ITEM_SHEETS.D_PROP4");
			attribute2dbField.put("dProp5ITM25","ITM25_ITEM_SHEETS.D_PROP5");
			attribute2dbField.put("dProp6ITM25","ITM25_ITEM_SHEETS.D_PROP6");
			attribute2dbField.put("dProp7ITM25","ITM25_ITEM_SHEETS.D_PROP7");
			attribute2dbField.put("dProp8ITM25","ITM25_ITEM_SHEETS.D_PROP8");
			attribute2dbField.put("dProp9ITM25","ITM25_ITEM_SHEETS.D_PROP9");

			attribute2dbField.put("nProp0ITM25","ITM25_ITEM_SHEETS.N_PROP0");
			attribute2dbField.put("nProp1ITM25","ITM25_ITEM_SHEETS.N_PROP1");
			attribute2dbField.put("nProp2ITM25","ITM25_ITEM_SHEETS.N_PROP2");
			attribute2dbField.put("nProp3ITM25","ITM25_ITEM_SHEETS.N_PROP3");
			attribute2dbField.put("nProp4ITM25","ITM25_ITEM_SHEETS.N_PROP4");
			attribute2dbField.put("nProp5ITM25","ITM25_ITEM_SHEETS.N_PROP5");
			attribute2dbField.put("nProp6ITM25","ITM25_ITEM_SHEETS.N_PROP6");
			attribute2dbField.put("nProp7ITM25","ITM25_ITEM_SHEETS.N_PROP7");
			attribute2dbField.put("nProp8ITM25","ITM25_ITEM_SHEETS.N_PROP8");
			attribute2dbField.put("nProp9ITM25","ITM25_ITEM_SHEETS.N_PROP9");

			// possibile usages:
			// list of sheets (one sheet...), using the validated sheet code
			// list of children sheets, starting from a parent sheet (for each subsheet, retrieve also SubsheetVO subobject)
			// list of possible sheets to add to a parent sheet, starting from a specific level (to filter) and a parent sheet to use to exclude children sheets

			String select =
					"select ITM25_ITEM_SHEETS.COMPANY_CODE_SYS01,ITM25_ITEM_SHEETS.SHEET_CODE,"+
					"ITM25_ITEM_SHEETS.PROGRESSIVE_SYS10,SYS10_TRANSLATIONS.DESCRIPTION,"+
					"ITM25_ITEM_SHEETS.IMAGE_NAME,ITM25_ITEM_SHEETS.LEV,"+
					"ITM25_ITEM_SHEETS.S_PROP0,ITM25_ITEM_SHEETS.S_PROP1,ITM25_ITEM_SHEETS.S_PROP2,ITM25_ITEM_SHEETS.S_PROP3,ITM25_ITEM_SHEETS.S_PROP4,"+
					"ITM25_ITEM_SHEETS.S_PROP5,ITM25_ITEM_SHEETS.S_PROP6,ITM25_ITEM_SHEETS.S_PROP7,ITM25_ITEM_SHEETS.S_PROP8,ITM25_ITEM_SHEETS.S_PROP9,"+
					"ITM25_ITEM_SHEETS.D_PROP0,ITM25_ITEM_SHEETS.D_PROP1,ITM25_ITEM_SHEETS.D_PROP2,ITM25_ITEM_SHEETS.D_PROP3,ITM25_ITEM_SHEETS.D_PROP4,"+
					"ITM25_ITEM_SHEETS.D_PROP5,ITM25_ITEM_SHEETS.D_PROP6,ITM25_ITEM_SHEETS.D_PROP7,ITM25_ITEM_SHEETS.D_PROP8,ITM25_ITEM_SHEETS.D_PROP9,"+
					"ITM25_ITEM_SHEETS.N_PROP0,ITM25_ITEM_SHEETS.N_PROP1,ITM25_ITEM_SHEETS.N_PROP2,ITM25_ITEM_SHEETS.N_PROP3,ITM25_ITEM_SHEETS.N_PROP4,"+
					"ITM25_ITEM_SHEETS.N_PROP5,ITM25_ITEM_SHEETS.N_PROP6,ITM25_ITEM_SHEETS.N_PROP7,ITM25_ITEM_SHEETS.N_PROP8,ITM25_ITEM_SHEETS.N_PROP9 ";
			String from =
					" from ITM25_ITEM_SHEETS,SYS10_TRANSLATIONS ";
			String where =
					" where "+
					"ITM25_ITEM_SHEETS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
					"SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
					"ITM25_ITEM_SHEETS.ENABLED='Y' and "+
					"ITM25_ITEM_SHEETS.COMPANY_CODE_SYS01=? and "+
					"ITM25_ITEM_SHEETS.SHEET_CODE=? ";

			ArrayList values = new ArrayList();
			values.add(serverLanguageId);
			values.add(companyCodeSYS10);
			values.add(pars.getCode());

			if (parentCode==null) {
				// list of sheets, using the validated sheet code
				where +=
					" and ITM25_ITEM_SHEETS.LEV=? ";

				values.add(new Long(1));
			}
			else if (parentCode!=null && levITM25==null) {
				// list of children sheets, starting from a parent sheet
				select +=
					",ALIAS.POLYGON ";
				from +=
					",ITM30_SUB_SHEETS ALIAS ";
				where +=
					" and ALIAS.PARENT_COMPANY_CODE_SYS01=ITM25_ITEM_SHEETS.COMPANY_CODE_SYS01 "+
  				" and ALIAS.SHEET_CODE_ITM25=ITM25_ITEM_SHEETS.SHEET_CODE "+
					" and ALIAS.PARENT_SHEET_CODE_ITM25=? ";

				values.add(parentCode);

				attribute2dbField.put("subsheet.polygon","ALIAS.POLYGON");
			}
			else if (parentCode!=null && levITM25!=null) {
				// list of possible sheets to add to a parent sheet, starting from a specific level (to filter) and a parent sheet to use to exclude children sheets
				where +=
					" and ITM25_ITEM_SHEETS.LEV=? "+
					" and ITM25_ITEM_SHEETS.SHEET_CODE not in (select SHEET_CODE_ITM25 from ITM30_SUB_SHEETS where "+
					"   COMPANY_CODE_SYS01=? and "+
					"   PARENT_SHEET_CODE_ITM25=? "+
					" ) ";

				values.add(levITM25);
				values.add(companyCodeSYS10);
				values.add(parentCode);
			}


			// read from ITM25 table...
			Response answer = QueryUtil.getQuery(
					conn,
					new UserSessionParameters(username),
					select+from+where,
					values,
					attribute2dbField,
					ItemSheetVO.class,
					"Y",
					"N",
					null,
					new GridParams(),
					50,
					true
			);

			if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while validating an item sheet",ex);
			throw new Exception(ex.getMessage());
		}
		finally {
				try {
						pstmt.close();
				}
				catch (Exception exx) {}
				try {
						if (this.conn==null && conn!=null) {
								// close only local connection
								conn.commit();
								conn.close();
						}
				}
				catch (Exception exx) {}
		}
	}



	/**
	 * Business logic to execute.
	 */
	public VOResponse insertItemSheet(ItemSheetVO vo,String serverLanguageId,String username,String imagePath) throws Throwable {
		Statement stmt = null;
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;

			vo.setEnabledITM25("Y");

			// generate progressive for sheet description...
			BigDecimal progressiveSYS10 = TranslationUtils.insertTranslations(vo.getDescriptionSYS10(),vo.getCompanyCodeSys01ITM25(),conn);
			vo.setProgressiveSys10ITM25(progressiveSYS10);

			Map attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01ITM25","COMPANY_CODE_SYS01");
			attribute2dbField.put("sheetCodeITM25","SHEET_CODE");
			attribute2dbField.put("progressiveSys10ITM25","PROGRESSIVE_SYS10");
//			attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
			attribute2dbField.put("levITM25","LEV");
			attribute2dbField.put("enabledITM25","ENABLED");

			attribute2dbField.put("sProp0ITM25","S_PROP0");
			attribute2dbField.put("sProp1ITM25","S_PROP1");
			attribute2dbField.put("sProp2ITM25","S_PROP2");
			attribute2dbField.put("sProp3ITM25","S_PROP3");
			attribute2dbField.put("sProp4ITM25","S_PROP4");
			attribute2dbField.put("sProp5ITM25","S_PROP5");
			attribute2dbField.put("sProp6ITM25","S_PROP6");
			attribute2dbField.put("sProp7ITM25","S_PROP7");
			attribute2dbField.put("sProp8ITM25","S_PROP8");
			attribute2dbField.put("sProp9ITM25","S_PROP9");

			attribute2dbField.put("dProp0ITM25","D_PROP0");
			attribute2dbField.put("dProp1ITM25","D_PROP1");
			attribute2dbField.put("dProp2ITM25","D_PROP2");
			attribute2dbField.put("dProp3ITM25","D_PROP3");
			attribute2dbField.put("dProp4ITM25","D_PROP4");
			attribute2dbField.put("dProp5ITM25","D_PROP5");
			attribute2dbField.put("dProp6ITM25","D_PROP6");
			attribute2dbField.put("dProp7ITM25","D_PROP7");
			attribute2dbField.put("dProp8ITM25","D_PROP8");
			attribute2dbField.put("dProp9ITM25","D_PROP9");

			attribute2dbField.put("nProp0ITM25","N_PROP0");
			attribute2dbField.put("nProp1ITM25","N_PROP1");
			attribute2dbField.put("nProp2ITM25","N_PROP2");
			attribute2dbField.put("nProp3ITM25","N_PROP3");
			attribute2dbField.put("nProp4ITM25","N_PROP4");
			attribute2dbField.put("nProp5ITM25","N_PROP5");
			attribute2dbField.put("nProp6ITM25","N_PROP6");
			attribute2dbField.put("nProp7ITM25","N_PROP7");
			attribute2dbField.put("nProp8ITM25","N_PROP8");
			attribute2dbField.put("nProp9ITM25","N_PROP9");

			if (vo.getImageITM25()!=null) {
				// save image on file system...
				String appPath = imagePath;
				appPath = appPath.replace('\\','/');
				if (!appPath.endsWith("/"))
					appPath += "/";
				if (!new File(appPath).isAbsolute()) {
					// relative path (to "WEB-INF/classes/" folder)
					appPath = this.getClass().getResource("/").getPath().replaceAll("%20"," ")+appPath;
				}

				BigDecimal imageProgressive = CompanyProgressiveUtils.getInternalProgressive(vo.getCompanyCodeSys01ITM25(),"ITM25_ITEM_SHEETS","IMG",conn);
				String relativePath = FileUtils.getFilePath(appPath,"ITM25");
				vo.setImageNameITM25(relativePath+"IMG"+imageProgressive);
//				attribute2dbField.put("smallImageITM01",imageProgressive);
				attribute2dbField.put("imageNameITM25","IMAGE_NAME");

				new File(appPath+relativePath).mkdirs();
				FileOutputStream out = new FileOutputStream(appPath+vo.getImageNameITM25());
				out.write(vo.getImageITM25());
				out.close();
			}


			// insert into ITM25...
			Response res = QueryUtil.insertTable(
					conn,
					new UserSessionParameters(username),
					vo,
					"ITM25_ITEM_SHEETS",
					attribute2dbField,
					"Y",
					"N",
					null,
					true
			);

		  if (!res.isError() && vo.getLevITM25().intValue()==1) {
				 pstmt = conn.prepareStatement("insert into ITM30_SUB_SHEETS(COMPANY_CODE_SYS01,PARENT_SHEET_CODE_ITM25,SHEET_CODE_ITM25) VALUES(?,?,?)");
				 pstmt.setString(1,vo.getCompanyCodeSys01ITM25());
				 pstmt.setString(2,vo.getSheetCodeITM25());
				 pstmt.setString(3,vo.getSheetCodeITM25());
				 pstmt.execute();
				 pstmt.close();
		  }

			Response answer = res;

			if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOResponse)answer;
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while inserting a new item sheet",ex);
			try {
				if (this.conn==null && conn!=null)
					// rollback only local connection
					conn.rollback();
			}
			catch (Exception ex3) {
			}
			throw new Exception(ex.getMessage());
		}
		finally {
			try {
				stmt.close();
			}
			catch (Exception ex2) {
			}
			try {
				pstmt.close();
			}
			catch (Exception ex2) {
			}

			try {
					if (this.conn==null && conn!=null) {
							// close only local connection
							conn.commit();
							conn.close();
					}

			}
			catch (Exception exx) {}
		}
	}


	/**
	 * Business logic to execute.
	 */
	public VOListResponse updateItemSheets(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username,String imagePath) throws Throwable {
		Statement stmt = null;
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;

			Map attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01ITM25","COMPANY_CODE_SYS01");
			attribute2dbField.put("sheetCodeITM25","SHEET_CODE");
			attribute2dbField.put("progressiveSys10ITM25","PROGRESSIVE_SYS10");
//			attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
			attribute2dbField.put("imageNameITM25","IMAGE_NAME");
			attribute2dbField.put("levITM25","LEV");

			attribute2dbField.put("sProp0ITM25","S_PROP0");
			attribute2dbField.put("sProp1ITM25","S_PROP1");
			attribute2dbField.put("sProp2ITM25","S_PROP2");
			attribute2dbField.put("sProp3ITM25","S_PROP3");
			attribute2dbField.put("sProp4ITM25","S_PROP4");
			attribute2dbField.put("sProp5ITM25","S_PROP5");
			attribute2dbField.put("sProp6ITM25","S_PROP6");
			attribute2dbField.put("sProp7ITM25","S_PROP7");
			attribute2dbField.put("sProp8ITM25","S_PROP8");
			attribute2dbField.put("sProp9ITM25","S_PROP9");

			attribute2dbField.put("dProp0ITM25","D_PROP0");
			attribute2dbField.put("dProp1ITM25","D_PROP1");
			attribute2dbField.put("dProp2ITM25","D_PROP2");
			attribute2dbField.put("dProp3ITM25","D_PROP3");
			attribute2dbField.put("dProp4ITM25","D_PROP4");
			attribute2dbField.put("dProp5ITM25","D_PROP5");
			attribute2dbField.put("dProp6ITM25","D_PROP6");
			attribute2dbField.put("dProp7ITM25","D_PROP7");
			attribute2dbField.put("dProp8ITM25","D_PROP8");
			attribute2dbField.put("dProp9ITM25","D_PROP9");

			attribute2dbField.put("nProp0ITM25","N_PROP0");
			attribute2dbField.put("nProp1ITM25","N_PROP1");
			attribute2dbField.put("nProp2ITM25","N_PROP2");
			attribute2dbField.put("nProp3ITM25","N_PROP3");
			attribute2dbField.put("nProp4ITM25","N_PROP4");
			attribute2dbField.put("nProp5ITM25","N_PROP5");
			attribute2dbField.put("nProp6ITM25","N_PROP6");
			attribute2dbField.put("nProp7ITM25","N_PROP7");
			attribute2dbField.put("nProp8ITM25","N_PROP8");
			attribute2dbField.put("nProp9ITM25","N_PROP9");

			HashSet pkAttributes = new HashSet();
			pkAttributes.add("companyCodeSys01ITM25");
			pkAttributes.add("sheetCodeITM25");

			ItemSheetVO oldVO = null;
			ItemSheetVO newVO = null;
			for(int i=0;i<oldVOs.size();i++) {
				oldVO = (ItemSheetVO)oldVOs.get(i);
				newVO = (ItemSheetVO)newVOs.get(i);

				// update sheet description...
				TranslationUtils.updateTranslation(
						oldVO.getDescriptionSYS10(),
						newVO.getDescriptionSYS10(),
						newVO.getProgressiveSys10ITM25(),
						serverLanguageId,
						conn
				);

				if (oldVO.getImageITM25()!=null && newVO.getImageITM25()==null) {
					 // remove image from file system...
					 String appPath = imagePath;
					 appPath = appPath.replace('\\','/');
					 if (!appPath.endsWith("/"))
						 appPath += "/";
					 if (!new File(appPath).isAbsolute()) {
						 // relative path (to "WEB-INF/classes/" folder)
						 appPath = this.getClass().getResource("/").getPath().replaceAll("%20"," ")+appPath;
					 }
					 new File(appPath+oldVO.getImageITM25()).delete();
				}
				else if (newVO.getImageITM25()!=null) {
					 // save image on file system...
					 String appPath = imagePath;
					 appPath = appPath.replace('\\','/');
					 if (!appPath.endsWith("/"))
						 appPath += "/";
					 if (!new File(appPath).isAbsolute()) {
						 // relative path (to "WEB-INF/classes/" folder)
						 appPath = this.getClass().getResource("/").getPath().replaceAll("%20"," ")+appPath;
					 }
					 new File(appPath).mkdirs();

					 if (oldVO.getImageITM25()==null) {
						 String relativePath = FileUtils.getFilePath(appPath,"ITM25");
						 BigDecimal imageProgressive = CompanyProgressiveUtils.getInternalProgressive(newVO.getCompanyCodeSys01ITM25(),"ITM25_ITEM_SHEETS","IMG",conn);
						 newVO.setImageNameITM25(relativePath+"IMG"+imageProgressive);
						 new File(appPath+relativePath).mkdirs();
					 }
					 else
						 newVO.setImageNameITM25(oldVO.getImageNameITM25());

					 File f = new File(appPath+newVO.getImageNameITM25());
					 f.delete();
					 FileOutputStream out = new FileOutputStream(f);
					 out.write(newVO.getImageITM25());
					 out.close();
				}


				// update ITM01 table...
				Response res = QueryUtil.updateTable(
						conn,
						new UserSessionParameters(username),
						pkAttributes,
						oldVO,
						newVO,
						"ITM25_ITEM_SHEETS",
						attribute2dbField,
						"Y",
						"N",
						null,
						true
				);

				if (res.isError()) throw new Exception(res.getErrorMessage());

			}

			return new VOListResponse(newVOs,false,newVOs.size());

		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing item sheets",ex);
			try {
				if (this.conn==null && conn!=null)
					// rollback only local connection
					conn.rollback();
			}
			catch (Exception ex3) {
			}
				throw new Exception(ex.getMessage());
		}
		finally {
			try {
				stmt.close();
			}
			catch (Exception ex2) {
			}
			try {
				pstmt.close();
			}
			catch (Exception ex2) {
			}

			try {
					if (this.conn==null && conn!=null) {
							// close only local connection
							conn.commit();
							conn.close();
					}

			}
			catch (Exception exx) {}
		}
	}




	/**
	 * Business logic to execute.
	 */
	public VOResponse deleteItemSheets(ArrayList list,String serverLanguageId,String username) throws Throwable {
		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;
			ItemSheetVO vo = null;
			stmt = conn.createStatement();

			for(int i=0;i<list.size();i++) {
				vo = (ItemSheetVO)list.get(i);

				// check if there exists a relation among this sheet and another one: if exists then interrupt delete operation...
				rset = stmt.executeQuery(
						"select * from ITM30_SUB_SHEETS where "+
						"COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01ITM25()+"' and "+
						"(SHEET_CODE_ITM25='"+vo.getSheetCodeITM25()+"' or PARENT_SHEET_CODE_ITM25='"+vo.getSheetCodeITM25()+"')"
				);
				if (rset.next())
					throw new Exception("cannot delete sheet: it is currently referenced by another sheet");
				rset.close();

				// check if there exists a relation among this sheet and an item: if exists then interrupt delete operation...
				rset = stmt.executeQuery(
						"select * from ITM01_ITEMS where "+
						"COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01ITM25()+"' and "+
						"SHEET_CODE_ITM25='"+vo.getSheetCodeITM25()+"'"
				);
				if (rset.next())
					throw new Exception("cannot delete sheet: it is currently referenced in an item");
				rset.close();

				// delete spare parts linked to current sheet...
				stmt.executeUpdate(
						"delete from ITM27_SHEETS_SPARE_PARTS where "+
						"COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01ITM25()+"' and "+
						"SHEET_CODE_ITM25='"+vo.getSheetCodeITM25()+"'"
				);

       // retrieve root sheets to update...
				ArrayList rootSheetsToUpdate = new ArrayList();
				rset = stmt.executeQuery(
						"select ROOT_SHEET_CODE_ITM25 from ITM24_LEAFSHEETS where "+
						"COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01ITM25()+"' and "+
						"SHEET_CODE_ITM25='"+vo.getSheetCodeITM25()+"'"
				);
				while(rset.next()) {
					rootSheetsToUpdate.add(rset.getString(1));
				}
				rset.close();

				// delete link with root sheets...
				stmt.executeUpdate(
							"DELETE from ITM24_LEAFSHEETS where "+
							"COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01ITM25()+"' and "+
							"SHEET_CODE_ITM25='"+vo.getSheetCodeITM25()+"'"
				);

				// logically delete the record in ITM25...
				stmt.execute(
						"update ITM25_ITEM_SHEETS set ENABLED='N' where "+
						"COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01ITM25()+"' and "+
						"SHEET_CODE='"+vo.getSheetCodeITM25()+"'"
				);

	      for(int k=0;k<rootSheetsToUpdate.size();k++)
					recalculateRootSheetSpareParts(conn,vo.getCompanyCodeSys01ITM25(),rootSheetsToUpdate.get(k).toString());
			}

			return new VOResponse(new Boolean(true));
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing item sheets",ex);
			try {
				if (this.conn==null && conn!=null)
					// rollback only local connection
					conn.rollback();
			}
			catch (Exception ex3) {
			}
			throw new Exception(ex.getMessage());
		}
		finally {
			try {
				rset.close();
			}
			catch (Exception ex2) {
			}
			try {
				stmt.close();
			}
			catch (Exception ex2) {
			}
			try {
				pstmt.close();
			}
			catch (Exception ex2) {
			}
			try {
				if (this.conn==null && conn!=null) {
					// close only local connection
					conn.commit();
					conn.close();
				}
			}
			catch (Exception exx) {}
		}
	}




	/**
	 * Business logic to execute.
	 */
	public VOResponse insertChildrenSheets(ItemSheetVO parentVO,List list,String serverLanguageId,String username) throws Throwable {
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet rset = null;
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;
			ItemSheetVO vo = null;
     	HashSet leafSheetCodes = new HashSet();
	    pstmt = conn.prepareStatement("insert into ITM30_SUB_SHEETS(COMPANY_CODE_SYS01,PARENT_SHEET_CODE_ITM25,SHEET_CODE_ITM25) VALUES(?,?,?)");
			for(int i=0;i<list.size();i++) {
				vo = (ItemSheetVO)list.get(i);

				pstmt.setString(1,vo.getCompanyCodeSys01ITM25());
				pstmt.setString(2,parentVO.getSheetCodeITM25());
				pstmt.setString(3,vo.getSheetCodeITM25());
				pstmt.execute();

				retrieveLeafSheets(conn,leafSheetCodes,vo.getCompanyCodeSys01ITM25(),vo.getSheetCodeITM25());
			}
			pstmt.close();

			// retrieve all root nodes of the current (parent) sheet...
			HashSet rootSheetCodes = new HashSet();
			retrieveRootSheets(conn,rootSheetCodes,parentVO.getCompanyCodeSys01ITM25(),parentVO.getSheetCodeITM25());

			if (rootSheetCodes.size()>0 && leafSheetCodes.size()>0) {

				// for each root sheet:
				// for each leaf: insert it in ITM24_LEAFSHEETS...
				if (rootSheetCodes.size()>0) {
					pstmt = conn.prepareStatement(
						"insert into ITM24_LEAFSHEETS(COMPANY_CODE_SYS01,ROOT_SHEET_CODE_ITM25,SHEET_CODE_ITM25) values(?,?,?)"
					);
					pstmt2 = conn.prepareStatement(
						"select SHEET_CODE_ITM25 from ITM24_LEAFSHEETS where "+
						"COMPANY_CODE_SYS01=? and ROOT_SHEET_CODE_ITM25=? and SHEET_CODE_ITM25=?"
					);

	        String rootSheetCode = null;
					String sheetCode = null;
	        Iterator it = rootSheetCodes.iterator();
					while(it.hasNext()) {
						rootSheetCode = it.next().toString();
						it = leafSheetCodes.iterator();
						while(it.hasNext()) {
							sheetCode = it.next().toString();
							pstmt2.setString(1,vo.getCompanyCodeSys01ITM25());
							pstmt2.setString(2,rootSheetCode);
							pstmt2.setString(3,sheetCode);
							rset = pstmt2.executeQuery();
							if (!rset.next()) {
								pstmt.setString(1,vo.getCompanyCodeSys01ITM25());
								pstmt.setString(2,rootSheetCode);
								pstmt.setString(3,sheetCode);
								pstmt.execute();
							}
							rset.close();
						}

						// regenerate root sheet's spare parts...
						recalculateRootSheetSpareParts(conn,vo.getCompanyCodeSys01ITM25(),rootSheetCode);

					} // end while on root sheets...
				}
			}

			return new VOResponse(new Boolean(true));
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while inserting children sheets",ex);
			try {
				if (this.conn==null && conn!=null)
					// rollback only local connection
					conn.rollback();
			}
			catch (Exception ex3) {
			}
			throw new Exception(ex.getMessage());
		}
		finally {
			try {
				rset.close();
			}
			catch (Exception ex2) {
			}
			try {
				pstmt2.close();
			}
			catch (Exception ex2) {
			}
			try {
				pstmt.close();
			}
			catch (Exception ex2) {
			}
			try {
				if (this.conn==null && conn!=null) {
					// close only local connection
					conn.commit();
					conn.close();
				}
			}
			catch (Exception exx) {}
		}
	}


	public VOResponse updateSubsheet(SubsheetVO vo,String serverLanguageId,String username) throws Throwable {
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;
			pstmt = conn.prepareStatement(
			  "update ITM30_SUB_SHEETS set POLYGON=? where "+
				"COMPANY_CODE_SYS01=? and PARENT_SHEET_CODE_ITM25=? and SHEET_CODE_ITM25=?");
			pstmt.setString(1,vo.getPolygonITM30());
			pstmt.setString(2,vo.getCompanyCodeSys01ITM30());
			pstmt.setString(3,vo.getParentSheetCodeItm25ITM30());
			pstmt.setString(4,vo.getSheetCodeItm25ITM30());
			pstmt.execute();

			return new VOResponse(new Boolean(true));
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while updatating a polygon for a sheet",ex);
			try {
				if (this.conn==null && conn!=null)
					// rollback only local connection
					conn.rollback();
			}
			catch (Exception ex3) {
			}
			throw new Exception(ex.getMessage());
		}
		finally {
			try {
				pstmt.close();
			}
			catch (Exception ex2) {
			}
			try {
				if (this.conn==null && conn!=null) {
					// close only local connection
					conn.commit();
					conn.close();
				}
			}
			catch (Exception exx) {}
		}
	}


	/**
	 * Business logic to execute.
	 */
	public VOResponse deleteChildrenSheets(ItemSheetVO parentVO,List list,String serverLanguageId,String username) throws Throwable {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;
			ItemSheetVO vo = null;
			 HashSet leafSheetCodes = new HashSet();
			pstmt = conn.prepareStatement("delete from ITM30_SUB_SHEETS where COMPANY_CODE_SYS01=? and PARENT_SHEET_CODE_ITM25=? and SHEET_CODE_ITM25=?");
			for(int i=0;i<list.size();i++) {
				vo = (ItemSheetVO)list.get(i);

				pstmt.setString(1,vo.getCompanyCodeSys01ITM25());
				pstmt.setString(2,parentVO.getSheetCodeITM25());
				pstmt.setString(3,vo.getSheetCodeITM25());
				pstmt.execute();

				retrieveLeafSheets(conn,leafSheetCodes,vo.getCompanyCodeSys01ITM25(),vo.getSheetCodeITM25());
			}
			pstmt.close();

			// retrieve all root nodes of the current sheet...
			HashSet rootSheetCodes = new HashSet();
			retrieveRootSheets(conn,rootSheetCodes,parentVO.getCompanyCodeSys01ITM25(),parentVO.getSheetCodeITM25());

			if (rootSheetCodes.size()>0 && leafSheetCodes.size()>0) {
				// for each root sheet:
				// for each leaf: delete it in ITM24_LEAFSHEETS...
				pstmt = conn.prepareStatement(
					"delete from ITM24_LEAFSHEETS where COMPANY_CODE_SYS01=? and ROOT_SHEET_CODE_ITM25=? and SHEET_CODE_ITM25=?"
				);

				String rootSheetCode = null;
				String sheetCode = null;
				Iterator it = rootSheetCodes.iterator();
				while(it.hasNext()) {
					rootSheetCode = it.next().toString();
					it = leafSheetCodes.iterator();
					while(it.hasNext()) {
						sheetCode = it.next().toString();
						pstmt.setString(1,vo.getCompanyCodeSys01ITM25());
						pstmt.setString(2,rootSheetCode);
						pstmt.setString(3,sheetCode);
						pstmt.execute();
					}

					// regenerate root sheet's spare parts...
					recalculateRootSheetSpareParts(conn,vo.getCompanyCodeSys01ITM25(),rootSheetCode);

				} // end while on root sheets...
			}

			return new VOResponse(new Boolean(true));
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting children sheets",ex);
			try {
				if (this.conn==null && conn!=null)
					// rollback only local connection
					conn.rollback();
			}
			catch (Exception ex3) {
			}
			throw new Exception(ex.getMessage());
		}
		finally {
			try {
				rset.close();
			}
			catch (Exception ex2) {
			}
			try {
				pstmt.close();
			}
			catch (Exception ex2) {
			}
			try {
				if (this.conn==null && conn!=null) {
					// close only local connection
					conn.commit();
					conn.close();
				}
			}
			catch (Exception exx) {}
		}
	}







	/**
	 * Remove from ITM28_ROOT_S_SPARE_PARTS all spare parts linked to a specific root sheet and reinsert them,
	 * according to ITM24_LEAFSHEETS content.
	 * Pre-condition: ITM24_LEAFSHEETS must be already correctly filled.
	 */
	private void recalculateRootSheetSpareParts(Connection conn,String companyCodeSys01ITM25,String rootSheetCodeITM25) throws Exception {
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(
			  "delete from ITM28_ROOT_S_SPARE_PARTS where COMPANY_CODE_SYS01=? and ROOT_SHEET_CODE_ITM25=?"
			);
 		  pstmt.setString(1,companyCodeSys01ITM25);
		  pstmt.setString(2,rootSheetCodeITM25);
			pstmt.execute();
			pstmt.close();

			pstmt = conn.prepareStatement(
				"insert into ITM28_ROOT_S_SPARE_PARTS(COMPANY_CODE_SYS01,ROOT_SHEET_CODE_ITM25,ITEM_CODE_ITM01) "+
				"select ?,?,ITEM_CODE_ITM01 from ITM27_SHEETS_SPARE_PARTS where "+
				"COMPANY_CODE_SYS01=? and SHEET_CODE_ITM25 in ("+
				"  select SHEET_CODE_ITM25 from ITM24_LEAFSHEETS where COMPANY_CODE_SYS01=? and ROOT_SHEET_CODE_ITM25=?"+
				")"
			);
			pstmt.setString(1,companyCodeSys01ITM25);
			pstmt.setString(2,rootSheetCodeITM25);
			pstmt.setString(3,companyCodeSys01ITM25);
			pstmt.setString(4,companyCodeSys01ITM25);
			pstmt.setString(5,rootSheetCodeITM25);
			pstmt.execute();
			pstmt.close();
		}
		finally {
			try {
				pstmt.close();
			}
			catch (Exception ex2) {
			}
		}
	}


	/**
	 * Retrieve the list of SHEET_CODEs that are leaves and are descendent of the specified sheet.
	 */
	private void retrieveLeafSheets(Connection conn,HashSet sheetCodes,String companyCodeSys01ITM25,String sheetCodeITM25) throws Exception {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			// retrieve level for leaf nodes...
			pstmt = conn.prepareStatement(
				"select max(LEV) from ITM29_SHEET_LEVELS where COMPANY_CODE_SYS01=?"
			);
			pstmt.setString(1,companyCodeSys01ITM25);
			int level = 0;
			rset= pstmt.executeQuery();
			if (rset.next())
				level = rset.getInt(1);
			rset.close();
			pstmt.close();
			if (level==0)
				return;

     // retrieve level for current node...
			pstmt = conn.prepareStatement(
				"select LEV,SHEET_CODE from ITM25_ITEM_SHEETS where COMPANY_CODE_SYS01=? and SHEET_CODE=?"
			);
			pstmt.setString(1,companyCodeSys01ITM25);
			pstmt.setString(2,sheetCodeITM25);
			rset = pstmt.executeQuery();
			if (rset.next())
				if (level==rset.getInt(1)) {
					// found a leaf...
					sheetCodes.add(rset.getString(2));
					return;
			  }
			rset.close();
			pstmt.close();

			pstmt = conn.prepareStatement(
				"select ITM30_SUB_SHEETS.SHEET_CODE_ITM25,ITM27_SHEETS_SPARE_PARTS.ITEM_CODE_ITM01 from "+
				"ITM30_SUB_SHEETS "+
				"left outer join ITM27_SHEETS_SPARE_PARTS on "+
				"ITM30_SUB_SHEETS.COMPANY_CODE_SYS01=ITM27_SHEETS_SPARE_PARTS.COMPANY_CODE_SYS01 and "+
				"ITM30_SUB_SHEETS.SHEET_CODE_ITM25=ITM27_SHEETS_SPARE_PARTS.SHEET_CODE_ITM25 "+
				"where ITM30_SUB_SHEETS.COMPANY_CODE_SYS01=? and ITM30_SUB_SHEETS.PARENT_SHEET_CODE_ITM25=? "
			);
			pstmt.setString(1,companyCodeSys01ITM25);
			pstmt.setString(2,sheetCodeITM25);
			rset = pstmt.executeQuery();
			HashSet sheetCodesToCheck = new HashSet();

			while(rset.next()) {
				if (rset.getString(2)!=null) {
					// found a leaf...
					if (!sheetCodes.contains(rset.getString(1)))
						sheetCodes.add(rset.getString(1));
				}
				else if (!sheetCodesToCheck.contains(rset.getString(1)))
					sheetCodesToCheck.add(rset.getString(1));
			}
			rset.close();
			pstmt.close();

			Iterator it = sheetCodesToCheck.iterator();
			while(it.hasNext())
				retrieveLeafSheets(conn,sheetCodes,companyCodeSys01ITM25,it.next().toString());

		}
		finally {
			try {
				rset.close();
			}
			catch (Exception ex2) {
			}
			try {
				pstmt.close();
			}
			catch (Exception ex2) {
			}
		}
	}


	/**
	 * Retrieve the list of SHEET_CODEs that are roots of the specified sheet.
	 */
	private void retrieveRootSheets(Connection conn,HashSet sheetCodes,String companyCodeSys01ITM25,String sheetCodeITM25) throws Exception {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			pstmt = conn.prepareStatement(
				"select ITM30_SUB_SHEETS.PARENT_SHEET_CODE_ITM25 from ITM30_SUB_SHEETS "+
				"where ITM30_SUB_SHEETS.COMPANY_CODE_SYS01=? and "+
				"ITM30_SUB_SHEETS.SHEET_CODE_ITM25=? "
			);
			pstmt.setString(1,companyCodeSys01ITM25);
			pstmt.setString(2,sheetCodeITM25);
			rset = pstmt.executeQuery();
			HashSet sheetCodesToCheck = new HashSet();
			while(rset.next()) {
				if (rset.getString(1).equals(sheetCodeITM25))
					sheetCodes.add(sheetCodeITM25);
				else
					sheetCodesToCheck.add(rset.getString(1));
			}
			rset.close();
			pstmt.close();

			Iterator it = sheetCodesToCheck.iterator();
			while(it.hasNext())
				retrieveRootSheets(conn,sheetCodes,companyCodeSys01ITM25,it.next().toString());

		}
		finally {
			try {
				rset.close();
			}
			catch (Exception ex2) {
			}
			try {
				pstmt.close();
			}
			catch (Exception ex2) {
			}
	  }
	}


	public VOListResponse insertItemSheetLevels(ArrayList vos,String serverLanguageId,String username) throws Throwable {
		ResultSet rset = null;
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;
			ItemSheetLevelVO vo = (ItemSheetLevelVO)vos.get(0);

			// check if there are spare parts already linked to existing sheets: in this case do not allow to insert other levels...
			pstmt = conn.prepareStatement("select * from ITM27_SHEETS_SPARE_PARTS where COMPANY_CODE_SYS01=?");
			pstmt.setString(1,vo.getCompanyCodeSys01ITM29());
			rset = pstmt.executeQuery();
			if (rset.next()) {
				throw new Exception("cannot insert levels: there are sheets binded to spare parts");
			}
			rset.close();
			pstmt.close();

			Map attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01ITM29","COMPANY_CODE_SYS01");
			attribute2dbField.put("levITM29","LEV");
			attribute2dbField.put("progressiveSys10ITM29","PROGRESSIVE_SYS10");
//			attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");

			attribute2dbField.put("sProp0ProgressiveSys10ITM29","S_PROP0_PROGRESSIVE_SYS10");
			attribute2dbField.put("sProp1ProgressiveSys10ITM29","S_PROP1_PROGRESSIVE_SYS10");
			attribute2dbField.put("sProp2ProgressiveSys10ITM29","S_PROP2_PROGRESSIVE_SYS10");
			attribute2dbField.put("sProp3ProgressiveSys10ITM29","S_PROP3_PROGRESSIVE_SYS10");
			attribute2dbField.put("sProp4ProgressiveSys10ITM29","S_PROP4_PROGRESSIVE_SYS10");
			attribute2dbField.put("sProp5ProgressiveSys10ITM29","S_PROP5_PROGRESSIVE_SYS10");
			attribute2dbField.put("sProp6ProgressiveSys10ITM29","S_PROP6_PROGRESSIVE_SYS10");
			attribute2dbField.put("sProp7ProgressiveSys10ITM29","S_PROP7_PROGRESSIVE_SYS10");
			attribute2dbField.put("sProp8ProgressiveSys10ITM29","S_PROP8_PROGRESSIVE_SYS10");
			attribute2dbField.put("sProp9ProgressiveSys10ITM29","S_PROP9_PROGRESSIVE_SYS10");

			attribute2dbField.put("dProp0ProgressiveSys10ITM29","D_PROP0_PROGRESSIVE_SYS10");
			attribute2dbField.put("dProp1ProgressiveSys10ITM29","D_PROP1_PROGRESSIVE_SYS10");
			attribute2dbField.put("dProp2ProgressiveSys10ITM29","D_PROP2_PROGRESSIVE_SYS10");
			attribute2dbField.put("dProp3ProgressiveSys10ITM29","D_PROP3_PROGRESSIVE_SYS10");
			attribute2dbField.put("dProp4ProgressiveSys10ITM29","D_PROP4_PROGRESSIVE_SYS10");
			attribute2dbField.put("dProp5ProgressiveSys10ITM29","D_PROP5_PROGRESSIVE_SYS10");
			attribute2dbField.put("dProp6ProgressiveSys10ITM29","D_PROP6_PROGRESSIVE_SYS10");
			attribute2dbField.put("dProp7ProgressiveSys10ITM29","D_PROP7_PROGRESSIVE_SYS10");
			attribute2dbField.put("dProp8ProgressiveSys10ITM29","D_PROP8_PROGRESSIVE_SYS10");
			attribute2dbField.put("dProp9ProgressiveSys10ITM29","D_PROP9_PROGRESSIVE_SYS10");

			attribute2dbField.put("nProp0ProgressiveSys10ITM29","N_PROP0_PROGRESSIVE_SYS10");
			attribute2dbField.put("nProp1ProgressiveSys10ITM29","N_PROP1_PROGRESSIVE_SYS10");
			attribute2dbField.put("nProp2ProgressiveSys10ITM29","N_PROP2_PROGRESSIVE_SYS10");
			attribute2dbField.put("nProp3ProgressiveSys10ITM29","N_PROP3_PROGRESSIVE_SYS10");
			attribute2dbField.put("nProp4ProgressiveSys10ITM29","N_PROP4_PROGRESSIVE_SYS10");
			attribute2dbField.put("nProp5ProgressiveSys10ITM29","N_PROP5_PROGRESSIVE_SYS10");
			attribute2dbField.put("nProp6ProgressiveSys10ITM29","N_PROP6_PROGRESSIVE_SYS10");
			attribute2dbField.put("nProp7ProgressiveSys10ITM29","N_PROP7_PROGRESSIVE_SYS10");
			attribute2dbField.put("nProp8ProgressiveSys10ITM29","N_PROP8_PROGRESSIVE_SYS10");
			attribute2dbField.put("nProp9ProgressiveSys10ITM29","N_PROP9_PROGRESSIVE_SYS10");

			Response res = null;
				BigDecimal progressiveSYS10 = null;
			for(int i=0;i<vos.size();i++) {
				vo = (ItemSheetLevelVO)vos.get(i);

				// generate progressive for level description...
				progressiveSYS10 = TranslationUtils.insertTranslations(vo.getDescriptionSYS10(),vo.getCompanyCodeSys01ITM29(),conn);
				vo.setProgressiveSys10ITM29(progressiveSYS10);

      	String value = null;
				for(int j=0;j<=9;j++) {
					value = (String)ItemSheetLevelVO.class.getMethod("getSProp"+j+"DescriptionSys10ITM29",new Class[0]).invoke(vo,new Object[0]);
					if (value!=null && !value.equals("")) {
						progressiveSYS10 = TranslationUtils.insertTranslations(value,vo.getCompanyCodeSys01ITM29(),conn);
						ItemSheetLevelVO.class.getMethod("setSProp"+j+"ProgressiveSys10ITM29",new Class[]{BigDecimal.class}).invoke(vo,new Object[]{progressiveSYS10});
					}
				}
				for(int j=0;j<=9;j++) {
					value = (String)ItemSheetLevelVO.class.getMethod("getDProp"+j+"DescriptionSys10ITM29",new Class[0]).invoke(vo,new Object[0]);
					if (value!=null && !value.equals("")) {
						progressiveSYS10 = TranslationUtils.insertTranslations(value,vo.getCompanyCodeSys01ITM29(),conn);
						ItemSheetLevelVO.class.getMethod("setDProp"+j+"ProgressiveSys10ITM29",new Class[]{BigDecimal.class}).invoke(vo,new Object[]{progressiveSYS10});
					}
				}
				for(int j=0;j<=9;j++) {
					value = (String)ItemSheetLevelVO.class.getMethod("getNProp"+j+"DescriptionSys10ITM29",new Class[0]).invoke(vo,new Object[0]);
					if (value!=null && !value.equals("")) {
						progressiveSYS10 = TranslationUtils.insertTranslations(value,vo.getCompanyCodeSys01ITM29(),conn);
						ItemSheetLevelVO.class.getMethod("setNProp"+j+"ProgressiveSys10ITM29",new Class[]{BigDecimal.class}).invoke(vo,new Object[]{progressiveSYS10});
					}
				}

				// insert into ITM29...
				res = QueryUtil.insertTable(
						conn,
						new UserSessionParameters(username),
						vo,
						"ITM29_SHEET_LEVELS",
						attribute2dbField,
						"Y",
						"N",
						null,
						true
				);

				if (res.isError()) throw new Exception(res.getErrorMessage());

			} // end for on vos...


			return new VOListResponse(vos,false,vos.size());
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while (re)inserting all sheet levels",ex);
			try {
				if (this.conn==null && conn!=null)
					// rollback only local connection
					conn.rollback();
			}
			catch (Exception ex3) {
			}
			throw new Exception(ex.getMessage());
		}
		finally {
			try {
				rset.close();
			}
			catch (Exception ex2) {
			}
			try {
				pstmt.close();
			}
			catch (Exception ex2) {
			}

			try {
					if (this.conn==null && conn!=null) {
							// close only local connection
							conn.commit();
							conn.close();
					}

			}
			catch (Exception exx) {}
		}
	}


	public VOListResponse updateItemSheetLevels(ArrayList oldVos,ArrayList newVos,String serverLanguageId,String username) throws Throwable {
		Statement stmt = null;
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;

			Map attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01ITM29","COMPANY_CODE_SYS01");
			attribute2dbField.put("levITM29","LEV");
			attribute2dbField.put("progressiveSys10ITM29","PROGRESSIVE_SYS10");
//			attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");

			attribute2dbField.put("sProp0ProgressiveSys10ITM29","S_PROP0_PROGRESSIVE_SYS10");
			attribute2dbField.put("sProp1ProgressiveSys10ITM29","S_PROP1_PROGRESSIVE_SYS10");
			attribute2dbField.put("sProp2ProgressiveSys10ITM29","S_PROP2_PROGRESSIVE_SYS10");
			attribute2dbField.put("sProp3ProgressiveSys10ITM29","S_PROP3_PROGRESSIVE_SYS10");
			attribute2dbField.put("sProp4ProgressiveSys10ITM29","S_PROP4_PROGRESSIVE_SYS10");
			attribute2dbField.put("sProp5ProgressiveSys10ITM29","S_PROP5_PROGRESSIVE_SYS10");
			attribute2dbField.put("sProp6ProgressiveSys10ITM29","S_PROP6_PROGRESSIVE_SYS10");
			attribute2dbField.put("sProp7ProgressiveSys10ITM29","S_PROP7_PROGRESSIVE_SYS10");
			attribute2dbField.put("sProp8ProgressiveSys10ITM29","S_PROP8_PROGRESSIVE_SYS10");
			attribute2dbField.put("sProp9ProgressiveSys10ITM29","S_PROP9_PROGRESSIVE_SYS10");

			attribute2dbField.put("dProp0ProgressiveSys10ITM29","D_PROP0_PROGRESSIVE_SYS10");
			attribute2dbField.put("dProp1ProgressiveSys10ITM29","D_PROP1_PROGRESSIVE_SYS10");
			attribute2dbField.put("dProp2ProgressiveSys10ITM29","D_PROP2_PROGRESSIVE_SYS10");
			attribute2dbField.put("dProp3ProgressiveSys10ITM29","D_PROP3_PROGRESSIVE_SYS10");
			attribute2dbField.put("dProp4ProgressiveSys10ITM29","D_PROP4_PROGRESSIVE_SYS10");
			attribute2dbField.put("dProp5ProgressiveSys10ITM29","D_PROP5_PROGRESSIVE_SYS10");
			attribute2dbField.put("dProp6ProgressiveSys10ITM29","D_PROP6_PROGRESSIVE_SYS10");
			attribute2dbField.put("dProp7ProgressiveSys10ITM29","D_PROP7_PROGRESSIVE_SYS10");
			attribute2dbField.put("dProp8ProgressiveSys10ITM29","D_PROP8_PROGRESSIVE_SYS10");
			attribute2dbField.put("dProp9ProgressiveSys10ITM29","D_PROP9_PROGRESSIVE_SYS10");

			attribute2dbField.put("nProp0ProgressiveSys10ITM29","N_PROP0_PROGRESSIVE_SYS10");
			attribute2dbField.put("nProp1ProgressiveSys10ITM29","N_PROP1_PROGRESSIVE_SYS10");
			attribute2dbField.put("nProp2ProgressiveSys10ITM29","N_PROP2_PROGRESSIVE_SYS10");
			attribute2dbField.put("nProp3ProgressiveSys10ITM29","N_PROP3_PROGRESSIVE_SYS10");
			attribute2dbField.put("nProp4ProgressiveSys10ITM29","N_PROP4_PROGRESSIVE_SYS10");
			attribute2dbField.put("nProp5ProgressiveSys10ITM29","N_PROP5_PROGRESSIVE_SYS10");
			attribute2dbField.put("nProp6ProgressiveSys10ITM29","N_PROP6_PROGRESSIVE_SYS10");
			attribute2dbField.put("nProp7ProgressiveSys10ITM29","N_PROP7_PROGRESSIVE_SYS10");
			attribute2dbField.put("nProp8ProgressiveSys10ITM29","N_PROP8_PROGRESSIVE_SYS10");
			attribute2dbField.put("nProp9ProgressiveSys10ITM29","N_PROP9_PROGRESSIVE_SYS10");

			HashSet pkAttrs = new HashSet();
			pkAttrs.add("companyCodeSys01ITM29");
			pkAttrs.add("levITM29");

			Response res = null;
			ItemSheetLevelVO oldVO = null;
			ItemSheetLevelVO newVO = null;
			String oldValue = null;
			String newValue = null;
			BigDecimal progressive = null;
			for(int i=0;i<oldVos.size();i++) {
				oldVO = (ItemSheetLevelVO)oldVos.get(i);
				newVO = (ItemSheetLevelVO)newVos.get(i);

				TranslationUtils.updateTranslation(
				  oldVO.getDescriptionSYS10(),
					newVO.getDescriptionSYS10(),
					newVO.getProgressiveSys10ITM29(),
					serverLanguageId,
					conn)
				;

				for(int j=0;j<=9;j++) {
					oldValue = (String)ItemSheetLevelVO.class.getMethod("getSProp"+j+"DescriptionSys10ITM29",new Class[0]).invoke(oldVO,new Object[0]);
					newValue = (String)ItemSheetLevelVO.class.getMethod("getSProp"+j+"DescriptionSys10ITM29",new Class[0]).invoke(newVO,new Object[0]);
					progressive = (BigDecimal)ItemSheetLevelVO.class.getMethod("getSProp"+j+"ProgressiveSys10ITM29",new Class[0]).invoke(newVO,new Object[0]);
					if (progressive!=null)
						TranslationUtils.updateTranslation(oldValue,newValue,progressive,serverLanguageId,conn);
					else	if (newValue!=null && !newValue.equals("")) {
						progressive = TranslationUtils.insertTranslations(newValue,newVO.getCompanyCodeSys01ITM29(),conn);
						ItemSheetLevelVO.class.getMethod("setSProp"+j+"ProgressiveSys10ITM29",new Class[]{BigDecimal.class}).invoke(newVO,new Object[]{progressive});
					}
				}
				for(int j=0;j<=9;j++) {
					oldValue = (String)ItemSheetLevelVO.class.getMethod("getDProp"+j+"DescriptionSys10ITM29",new Class[0]).invoke(oldVO,new Object[0]);
					newValue = (String)ItemSheetLevelVO.class.getMethod("getDProp"+j+"DescriptionSys10ITM29",new Class[0]).invoke(newVO,new Object[0]);
					progressive = (BigDecimal)ItemSheetLevelVO.class.getMethod("getDProp"+j+"ProgressiveSys10ITM29",new Class[0]).invoke(newVO,new Object[0]);
					if (progressive!=null)
						TranslationUtils.updateTranslation(oldValue,newValue,progressive,serverLanguageId,conn);
					else	if (newValue!=null && !newValue.equals("")) {
						progressive = TranslationUtils.insertTranslations(newValue,newVO.getCompanyCodeSys01ITM29(),conn);
						ItemSheetLevelVO.class.getMethod("setDProp"+j+"ProgressiveSys10ITM29",new Class[]{BigDecimal.class}).invoke(newVO,new Object[]{progressive});
					}
				}
				for(int j=0;j<=9;j++) {
					oldValue = (String)ItemSheetLevelVO.class.getMethod("getNProp"+j+"DescriptionSys10ITM29",new Class[0]).invoke(oldVO,new Object[0]);
					newValue = (String)ItemSheetLevelVO.class.getMethod("getNProp"+j+"DescriptionSys10ITM29",new Class[0]).invoke(newVO,new Object[0]);
					progressive = (BigDecimal)ItemSheetLevelVO.class.getMethod("getNProp"+j+"ProgressiveSys10ITM29",new Class[0]).invoke(newVO,new Object[0]);
					if (progressive!=null)
						TranslationUtils.updateTranslation(oldValue,newValue,progressive,serverLanguageId,conn);
					else	if (newValue!=null && !newValue.equals("")) {
						progressive = TranslationUtils.insertTranslations(newValue,newVO.getCompanyCodeSys01ITM29(),conn);
						ItemSheetLevelVO.class.getMethod("setDProp"+j+"ProgressiveSys10ITM29",new Class[]{BigDecimal.class}).invoke(newVO,new Object[]{progressive});
					}
				}

				// update ITM29...
				res = QueryUtil.updateTable(
						conn,
						new UserSessionParameters(username),
						pkAttrs,
						oldVO,
						newVO,
						"ITM29_SHEET_LEVELS",
						attribute2dbField,
						"Y",
						"N",
						null,
						true
				);

			} // end for on vos...

			return new VOListResponse(newVos,false,newVos.size());
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating sheet levels",ex);
			try {
				if (this.conn==null && conn!=null)
					// rollback only local connection
					conn.rollback();
			}
			catch (Exception ex3) {
			}
			throw new Exception(ex.getMessage());
		}
		finally {
			try {
				stmt.close();
			}
			catch (Exception ex2) {
			}
			try {
				pstmt.close();
			}
			catch (Exception ex2) {
			}

			try {
					if (this.conn==null && conn!=null) {
							// close only local connection
							conn.commit();
							conn.close();
					}

			}
			catch (Exception exx) {}
		}
	}


	public VOResponse deleteItemSheetLevels(ArrayList vos,String serverLanguageId,String username) throws Throwable {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;

			pstmt = conn.prepareStatement("delete from ITM29_SHEET_LEVELS where COMPANY_CODE_SYS01=? AND LEV=?");

			ItemSheetLevelVO vo = null;
			String value = null;
			BigDecimal progressive = null;
			int maxLevel = 0;
			for(int i=0;i<vos.size();i++) {
				vo = (ItemSheetLevelVO)vos.get(i);

				for(int j=0;j<=9;j++) {
					progressive = (BigDecimal)ItemSheetLevelVO.class.getMethod("getSProp"+j+"ProgressiveSys10ITM29",new Class[0]).invoke(vo,new Object[0]);
					if (progressive!=null)
						TranslationUtils.deleteTranslations(progressive,conn);
					progressive = (BigDecimal)ItemSheetLevelVO.class.getMethod("getNProp"+j+"ProgressiveSys10ITM29",new Class[0]).invoke(vo,new Object[0]);
					if (progressive!=null)
						TranslationUtils.deleteTranslations(progressive,conn);
					progressive = (BigDecimal)ItemSheetLevelVO.class.getMethod("getDProp"+j+"ProgressiveSys10ITM29",new Class[0]).invoke(vo,new Object[0]);
					if (progressive!=null)
						TranslationUtils.deleteTranslations(progressive,conn);
				}

	      pstmt.setString(1,vo.getCompanyCodeSys01ITM29());
				pstmt.setBigDecimal(2,vo.getLevITM29());
				pstmt.execute();

	      if (maxLevel<vo.getLevITM29().intValue())
					maxLevel = vo.getLevITM29().intValue();

			} // end for on vos...
			pstmt.close();

			// check if levels are not still used...
			pstmt = conn.prepareStatement("select * from ITM25_ITEM_SHEETS where COMPANY_CODE_SYS01=? where LEV>=?");
			pstmt.setString(1,vo.getCompanyCodeSys01ITM29());
			pstmt.setInt(2,maxLevel);
			rset = pstmt.executeQuery();
			if (rset.next()) {
				throw new Exception("cannot delete levels: there are sheets having these levels");
			}
			rset.close();


			return new VOResponse(Boolean.TRUE);
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting sheet levels",ex);
			try {
				if (this.conn==null && conn!=null)
					// rollback only local connection
					conn.rollback();
			}
			catch (Exception ex3) {
			}
			throw new Exception(ex.getMessage());
		}
		finally {
			try {
				rset.close();
			}
			catch (Exception ex2) {
			}
			try {
				pstmt.close();
			}
			catch (Exception ex2) {
			}

			try {
					if (this.conn==null && conn!=null) {
							// close only local connection
							conn.commit();
							conn.close();
					}

			}
			catch (Exception exx) {}
		}
	}



	/**
	 * Business logic to execute.
	 */
	public VOListResponse loadItemSheetLevels(String serverLanguageId,String username,ArrayList companiesList) throws Throwable {
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;

			// retrieve companies list...
			String companies = "";
			for(int i=0;i<companiesList.size();i++)
				companies += "'"+companiesList.get(i).toString()+"',";
			companies = companies.substring(0,companies.length()-1);

			String sql =
					"select ITM29_SHEET_LEVELS.COMPANY_CODE_SYS01,ITM29_SHEET_LEVELS.LEV,ITM29_SHEET_LEVELS.PROGRESSIVE_SYS10,SYS10_TRANSLATIONS.DESCRIPTION,"+
					"ITM29_SHEET_LEVELS.S_PROP0_PROGRESSIVE_SYS10,ITM29_SHEET_LEVELS.S_PROP1_PROGRESSIVE_SYS10,ITM29_SHEET_LEVELS.S_PROP2_PROGRESSIVE_SYS10,ITM29_SHEET_LEVELS.S_PROP3_PROGRESSIVE_SYS10,ITM29_SHEET_LEVELS.S_PROP4_PROGRESSIVE_SYS10,"+
					"ITM29_SHEET_LEVELS.S_PROP5_PROGRESSIVE_SYS10,ITM29_SHEET_LEVELS.S_PROP6_PROGRESSIVE_SYS10,ITM29_SHEET_LEVELS.S_PROP7_PROGRESSIVE_SYS10,ITM29_SHEET_LEVELS.S_PROP8_PROGRESSIVE_SYS10,ITM29_SHEET_LEVELS.S_PROP9_PROGRESSIVE_SYS10,"+
					"ITM29_SHEET_LEVELS.D_PROP0_PROGRESSIVE_SYS10,ITM29_SHEET_LEVELS.D_PROP1_PROGRESSIVE_SYS10,ITM29_SHEET_LEVELS.D_PROP2_PROGRESSIVE_SYS10,ITM29_SHEET_LEVELS.D_PROP3_PROGRESSIVE_SYS10,ITM29_SHEET_LEVELS.D_PROP4_PROGRESSIVE_SYS10,"+
					"ITM29_SHEET_LEVELS.D_PROP5_PROGRESSIVE_SYS10,ITM29_SHEET_LEVELS.D_PROP6_PROGRESSIVE_SYS10,ITM29_SHEET_LEVELS.D_PROP7_PROGRESSIVE_SYS10,ITM29_SHEET_LEVELS.D_PROP8_PROGRESSIVE_SYS10,ITM29_SHEET_LEVELS.D_PROP9_PROGRESSIVE_SYS10,"+
					"ITM29_SHEET_LEVELS.N_PROP0_PROGRESSIVE_SYS10,ITM29_SHEET_LEVELS.N_PROP1_PROGRESSIVE_SYS10,ITM29_SHEET_LEVELS.N_PROP2_PROGRESSIVE_SYS10,ITM29_SHEET_LEVELS.N_PROP3_PROGRESSIVE_SYS10,ITM29_SHEET_LEVELS.N_PROP4_PROGRESSIVE_SYS10,"+
					"ITM29_SHEET_LEVELS.N_PROP5_PROGRESSIVE_SYS10,ITM29_SHEET_LEVELS.N_PROP6_PROGRESSIVE_SYS10,ITM29_SHEET_LEVELS.N_PROP7_PROGRESSIVE_SYS10,ITM29_SHEET_LEVELS.N_PROP8_PROGRESSIVE_SYS10,ITM29_SHEET_LEVELS.N_PROP9_PROGRESSIVE_SYS10,"+
					"ST0.DESCRIPTION,ST1.DESCRIPTION,ST2.DESCRIPTION,ST3.DESCRIPTION,ST4.DESCRIPTION,"+
					"ST5.DESCRIPTION,ST6.DESCRIPTION,ST7.DESCRIPTION,ST8.DESCRIPTION,ST9.DESCRIPTION,"+
					"DT0.DESCRIPTION,DT1.DESCRIPTION,DT2.DESCRIPTION,DT3.DESCRIPTION,DT4.DESCRIPTION,"+
					"DT5.DESCRIPTION,DT6.DESCRIPTION,DT7.DESCRIPTION,DT8.DESCRIPTION,DT9.DESCRIPTION,"+
					"NT0.DESCRIPTION,NT1.DESCRIPTION,NT2.DESCRIPTION,NT3.DESCRIPTION,NT4.DESCRIPTION,"+
					"NT5.DESCRIPTION,NT6.DESCRIPTION,NT7.DESCRIPTION,NT8.DESCRIPTION,NT9.DESCRIPTION "+
					"from SYS10_TRANSLATIONS,ITM29_SHEET_LEVELS "+

					"LEFT OUTER JOIN SYS10_TRANSLATIONS ST0 ON ITM29_SHEET_LEVELS.S_PROP0_PROGRESSIVE_SYS10=ST0.PROGRESSIVE AND ST0.LANGUAGE_CODE=? "+
					"LEFT OUTER JOIN SYS10_TRANSLATIONS ST1 ON ITM29_SHEET_LEVELS.S_PROP1_PROGRESSIVE_SYS10=ST1.PROGRESSIVE AND ST1.LANGUAGE_CODE=? "+
					"LEFT OUTER JOIN SYS10_TRANSLATIONS ST2 ON ITM29_SHEET_LEVELS.S_PROP2_PROGRESSIVE_SYS10=ST2.PROGRESSIVE AND ST2.LANGUAGE_CODE=? "+
					"LEFT OUTER JOIN SYS10_TRANSLATIONS ST3 ON ITM29_SHEET_LEVELS.S_PROP3_PROGRESSIVE_SYS10=ST3.PROGRESSIVE AND ST3.LANGUAGE_CODE=? "+
					"LEFT OUTER JOIN SYS10_TRANSLATIONS ST4 ON ITM29_SHEET_LEVELS.S_PROP4_PROGRESSIVE_SYS10=ST4.PROGRESSIVE AND ST4.LANGUAGE_CODE=? "+
					"LEFT OUTER JOIN SYS10_TRANSLATIONS ST5 ON ITM29_SHEET_LEVELS.S_PROP5_PROGRESSIVE_SYS10=ST5.PROGRESSIVE AND ST5.LANGUAGE_CODE=? "+
					"LEFT OUTER JOIN SYS10_TRANSLATIONS ST6 ON ITM29_SHEET_LEVELS.S_PROP6_PROGRESSIVE_SYS10=ST6.PROGRESSIVE AND ST6.LANGUAGE_CODE=? "+
					"LEFT OUTER JOIN SYS10_TRANSLATIONS ST7 ON ITM29_SHEET_LEVELS.S_PROP7_PROGRESSIVE_SYS10=ST7.PROGRESSIVE AND ST7.LANGUAGE_CODE=? "+
					"LEFT OUTER JOIN SYS10_TRANSLATIONS ST8 ON ITM29_SHEET_LEVELS.S_PROP8_PROGRESSIVE_SYS10=ST8.PROGRESSIVE AND ST8.LANGUAGE_CODE=? "+
					"LEFT OUTER JOIN SYS10_TRANSLATIONS ST9 ON ITM29_SHEET_LEVELS.S_PROP9_PROGRESSIVE_SYS10=ST9.PROGRESSIVE AND ST9.LANGUAGE_CODE=? "+

					"LEFT OUTER JOIN SYS10_TRANSLATIONS DT0 ON ITM29_SHEET_LEVELS.D_PROP0_PROGRESSIVE_SYS10=DT0.PROGRESSIVE AND DT0.LANGUAGE_CODE=? "+
					"LEFT OUTER JOIN SYS10_TRANSLATIONS DT1 ON ITM29_SHEET_LEVELS.D_PROP1_PROGRESSIVE_SYS10=DT1.PROGRESSIVE AND DT1.LANGUAGE_CODE=? "+
					"LEFT OUTER JOIN SYS10_TRANSLATIONS DT2 ON ITM29_SHEET_LEVELS.D_PROP2_PROGRESSIVE_SYS10=DT2.PROGRESSIVE AND DT2.LANGUAGE_CODE=? "+
					"LEFT OUTER JOIN SYS10_TRANSLATIONS DT3 ON ITM29_SHEET_LEVELS.D_PROP3_PROGRESSIVE_SYS10=DT3.PROGRESSIVE AND DT3.LANGUAGE_CODE=? "+
					"LEFT OUTER JOIN SYS10_TRANSLATIONS DT4 ON ITM29_SHEET_LEVELS.D_PROP4_PROGRESSIVE_SYS10=DT4.PROGRESSIVE AND DT4.LANGUAGE_CODE=? "+
					"LEFT OUTER JOIN SYS10_TRANSLATIONS DT5 ON ITM29_SHEET_LEVELS.D_PROP5_PROGRESSIVE_SYS10=DT5.PROGRESSIVE AND DT5.LANGUAGE_CODE=? "+
					"LEFT OUTER JOIN SYS10_TRANSLATIONS DT6 ON ITM29_SHEET_LEVELS.D_PROP6_PROGRESSIVE_SYS10=DT6.PROGRESSIVE AND DT6.LANGUAGE_CODE=? "+
					"LEFT OUTER JOIN SYS10_TRANSLATIONS DT7 ON ITM29_SHEET_LEVELS.D_PROP7_PROGRESSIVE_SYS10=DT7.PROGRESSIVE AND DT7.LANGUAGE_CODE=? "+
					"LEFT OUTER JOIN SYS10_TRANSLATIONS DT8 ON ITM29_SHEET_LEVELS.D_PROP8_PROGRESSIVE_SYS10=DT8.PROGRESSIVE AND DT8.LANGUAGE_CODE=? "+
					"LEFT OUTER JOIN SYS10_TRANSLATIONS DT9 ON ITM29_SHEET_LEVELS.D_PROP9_PROGRESSIVE_SYS10=DT9.PROGRESSIVE AND DT9.LANGUAGE_CODE=? "+

					"LEFT OUTER JOIN SYS10_TRANSLATIONS NT0 ON ITM29_SHEET_LEVELS.N_PROP0_PROGRESSIVE_SYS10=NT0.PROGRESSIVE AND NT0.LANGUAGE_CODE=? "+
					"LEFT OUTER JOIN SYS10_TRANSLATIONS NT1 ON ITM29_SHEET_LEVELS.N_PROP1_PROGRESSIVE_SYS10=NT1.PROGRESSIVE AND NT1.LANGUAGE_CODE=? "+
					"LEFT OUTER JOIN SYS10_TRANSLATIONS NT2 ON ITM29_SHEET_LEVELS.N_PROP2_PROGRESSIVE_SYS10=NT2.PROGRESSIVE AND NT2.LANGUAGE_CODE=? "+
					"LEFT OUTER JOIN SYS10_TRANSLATIONS NT3 ON ITM29_SHEET_LEVELS.N_PROP3_PROGRESSIVE_SYS10=NT3.PROGRESSIVE AND NT3.LANGUAGE_CODE=? "+
					"LEFT OUTER JOIN SYS10_TRANSLATIONS NT4 ON ITM29_SHEET_LEVELS.N_PROP4_PROGRESSIVE_SYS10=NT4.PROGRESSIVE AND NT4.LANGUAGE_CODE=? "+
					"LEFT OUTER JOIN SYS10_TRANSLATIONS NT5 ON ITM29_SHEET_LEVELS.N_PROP5_PROGRESSIVE_SYS10=NT5.PROGRESSIVE AND NT5.LANGUAGE_CODE=? "+
					"LEFT OUTER JOIN SYS10_TRANSLATIONS NT6 ON ITM29_SHEET_LEVELS.N_PROP6_PROGRESSIVE_SYS10=NT6.PROGRESSIVE AND NT6.LANGUAGE_CODE=? "+
					"LEFT OUTER JOIN SYS10_TRANSLATIONS NT7 ON ITM29_SHEET_LEVELS.N_PROP7_PROGRESSIVE_SYS10=NT7.PROGRESSIVE AND NT7.LANGUAGE_CODE=? "+
					"LEFT OUTER JOIN SYS10_TRANSLATIONS NT8 ON ITM29_SHEET_LEVELS.N_PROP8_PROGRESSIVE_SYS10=NT8.PROGRESSIVE AND NT8.LANGUAGE_CODE=? "+
					"LEFT OUTER JOIN SYS10_TRANSLATIONS NT9 ON ITM29_SHEET_LEVELS.N_PROP9_PROGRESSIVE_SYS10=NT9.PROGRESSIVE AND NT9.LANGUAGE_CODE=? "+

					" where "+
					"ITM29_SHEET_LEVELS.COMPANY_CODE_SYS01 in ("+companies+") and "+
					"ITM29_SHEET_LEVELS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
					"SYS10_TRANSLATIONS.LANGUAGE_CODE=? "+
					"ORDER BY ITM29_SHEET_LEVELS.LEV ";


			ArrayList values = new ArrayList();
			values.add(serverLanguageId);

			values.add(serverLanguageId);
			values.add(serverLanguageId);
			values.add(serverLanguageId);
			values.add(serverLanguageId);
			values.add(serverLanguageId);
			values.add(serverLanguageId);
			values.add(serverLanguageId);
			values.add(serverLanguageId);
			values.add(serverLanguageId);
			values.add(serverLanguageId);

			values.add(serverLanguageId);
			values.add(serverLanguageId);
			values.add(serverLanguageId);
			values.add(serverLanguageId);
			values.add(serverLanguageId);
			values.add(serverLanguageId);
			values.add(serverLanguageId);
			values.add(serverLanguageId);
			values.add(serverLanguageId);
			values.add(serverLanguageId);

			values.add(serverLanguageId);
			values.add(serverLanguageId);
			values.add(serverLanguageId);
			values.add(serverLanguageId);
			values.add(serverLanguageId);
			values.add(serverLanguageId);
			values.add(serverLanguageId);
			values.add(serverLanguageId);
			values.add(serverLanguageId);
			values.add(serverLanguageId);


			Map attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01ITM29","ITM29_SHEET_LEVELS.COMPANY_CODE_SYS01");
			attribute2dbField.put("levITM29","ITM29_SHEET_LEVELS.LEV");
			attribute2dbField.put("progressiveSys10ITM29","ITM29_SHEET_LEVELS.PROGRESSIVE_SYS10");
			attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");

			attribute2dbField.put("sProp0ProgressiveSys10ITM29","ITM29_SHEET_LEVELS.S_PROP0_PROGRESSIVE_SYS10");
			attribute2dbField.put("sProp1ProgressiveSys10ITM29","ITM29_SHEET_LEVELS.S_PROP1_PROGRESSIVE_SYS10");
			attribute2dbField.put("sProp2ProgressiveSys10ITM29","ITM29_SHEET_LEVELS.S_PROP2_PROGRESSIVE_SYS10");
			attribute2dbField.put("sProp3ProgressiveSys10ITM29","ITM29_SHEET_LEVELS.S_PROP3_PROGRESSIVE_SYS10");
			attribute2dbField.put("sProp4ProgressiveSys10ITM29","ITM29_SHEET_LEVELS.S_PROP4_PROGRESSIVE_SYS10");
			attribute2dbField.put("sProp5ProgressiveSys10ITM29","ITM29_SHEET_LEVELS.S_PROP5_PROGRESSIVE_SYS10");
			attribute2dbField.put("sProp6ProgressiveSys10ITM29","ITM29_SHEET_LEVELS.S_PROP6_PROGRESSIVE_SYS10");
			attribute2dbField.put("sProp7ProgressiveSys10ITM29","ITM29_SHEET_LEVELS.S_PROP7_PROGRESSIVE_SYS10");
			attribute2dbField.put("sProp8ProgressiveSys10ITM29","ITM29_SHEET_LEVELS.S_PROP8_PROGRESSIVE_SYS10");
			attribute2dbField.put("sProp9ProgressiveSys10ITM29","ITM29_SHEET_LEVELS.S_PROP9_PROGRESSIVE_SYS10");

			attribute2dbField.put("dProp0ProgressiveSys10ITM29","ITM29_SHEET_LEVELS.D_PROP0_PROGRESSIVE_SYS10");
			attribute2dbField.put("dProp1ProgressiveSys10ITM29","ITM29_SHEET_LEVELS.D_PROP1_PROGRESSIVE_SYS10");
			attribute2dbField.put("dProp2ProgressiveSys10ITM29","ITM29_SHEET_LEVELS.D_PROP2_PROGRESSIVE_SYS10");
			attribute2dbField.put("dProp3ProgressiveSys10ITM29","ITM29_SHEET_LEVELS.D_PROP3_PROGRESSIVE_SYS10");
			attribute2dbField.put("dProp4ProgressiveSys10ITM29","ITM29_SHEET_LEVELS.D_PROP4_PROGRESSIVE_SYS10");
			attribute2dbField.put("dProp5ProgressiveSys10ITM29","ITM29_SHEET_LEVELS.D_PROP5_PROGRESSIVE_SYS10");
			attribute2dbField.put("dProp6ProgressiveSys10ITM29","ITM29_SHEET_LEVELS.D_PROP6_PROGRESSIVE_SYS10");
			attribute2dbField.put("dProp7ProgressiveSys10ITM29","ITM29_SHEET_LEVELS.D_PROP7_PROGRESSIVE_SYS10");
			attribute2dbField.put("dProp8ProgressiveSys10ITM29","ITM29_SHEET_LEVELS.D_PROP8_PROGRESSIVE_SYS10");
			attribute2dbField.put("dProp9ProgressiveSys10ITM29","ITM29_SHEET_LEVELS.D_PROP9_PROGRESSIVE_SYS10");

			attribute2dbField.put("nProp0ProgressiveSys10ITM29","ITM29_SHEET_LEVELS.N_PROP0_PROGRESSIVE_SYS10");
			attribute2dbField.put("nProp1ProgressiveSys10ITM29","ITM29_SHEET_LEVELS.N_PROP1_PROGRESSIVE_SYS10");
			attribute2dbField.put("nProp2ProgressiveSys10ITM29","ITM29_SHEET_LEVELS.N_PROP2_PROGRESSIVE_SYS10");
			attribute2dbField.put("nProp3ProgressiveSys10ITM29","ITM29_SHEET_LEVELS.N_PROP3_PROGRESSIVE_SYS10");
			attribute2dbField.put("nProp4ProgressiveSys10ITM29","ITM29_SHEET_LEVELS.N_PROP4_PROGRESSIVE_SYS10");
			attribute2dbField.put("nProp5ProgressiveSys10ITM29","ITM29_SHEET_LEVELS.N_PROP5_PROGRESSIVE_SYS10");
			attribute2dbField.put("nProp6ProgressiveSys10ITM29","ITM29_SHEET_LEVELS.N_PROP6_PROGRESSIVE_SYS10");
			attribute2dbField.put("nProp7ProgressiveSys10ITM29","ITM29_SHEET_LEVELS.N_PROP7_PROGRESSIVE_SYS10");
			attribute2dbField.put("nProp8ProgressiveSys10ITM29","ITM29_SHEET_LEVELS.N_PROP8_PROGRESSIVE_SYS10");
			attribute2dbField.put("nProp9ProgressiveSys10ITM29","ITM29_SHEET_LEVELS.N_PROP9_PROGRESSIVE_SYS10");

			attribute2dbField.put("sProp0DescriptionSys10ITM29","ST0.DESCRIPTION");
			attribute2dbField.put("sProp1DescriptionSys10ITM29","ST1.DESCRIPTION");
			attribute2dbField.put("sProp2DescriptionSys10ITM29","ST2.DESCRIPTION");
			attribute2dbField.put("sProp3DescriptionSys10ITM29","ST3.DESCRIPTION");
			attribute2dbField.put("sProp4DescriptionSys10ITM29","ST4.DESCRIPTION");
			attribute2dbField.put("sProp5DescriptionSys10ITM29","ST5.DESCRIPTION");
			attribute2dbField.put("sProp6DescriptionSys10ITM29","ST6.DESCRIPTION");
			attribute2dbField.put("sProp7DescriptionSys10ITM29","ST7.DESCRIPTION");
			attribute2dbField.put("sProp8DescriptionSys10ITM29","ST8.DESCRIPTION");
			attribute2dbField.put("sProp9DescriptionSys10ITM29","ST9.DESCRIPTION");

			attribute2dbField.put("dProp0DescriptionSys10ITM29","DT0.DESCRIPTION");
			attribute2dbField.put("dProp1DescriptionSys10ITM29","DT1.DESCRIPTION");
			attribute2dbField.put("dProp2DescriptionSys10ITM29","DT2.DESCRIPTION");
			attribute2dbField.put("dProp3DescriptionSys10ITM29","DT3.DESCRIPTION");
			attribute2dbField.put("dProp4DescriptionSys10ITM29","DT4.DESCRIPTION");
			attribute2dbField.put("dProp5DescriptionSys10ITM29","DT5.DESCRIPTION");
			attribute2dbField.put("dProp6DescriptionSys10ITM29","DT6.DESCRIPTION");
			attribute2dbField.put("dProp7DescriptionSys10ITM29","DT7.DESCRIPTION");
			attribute2dbField.put("dProp8DescriptionSys10ITM29","DT8.DESCRIPTION");
			attribute2dbField.put("dProp9DescriptionSys10ITM29","DT9.DESCRIPTION");

			attribute2dbField.put("nProp0DescriptionSys10ITM29","NT0.DESCRIPTION");
			attribute2dbField.put("nProp1DescriptionSys10ITM29","NT1.DESCRIPTION");
			attribute2dbField.put("nProp2DescriptionSys10ITM29","NT2.DESCRIPTION");
			attribute2dbField.put("nProp3DescriptionSys10ITM29","NT3.DESCRIPTION");
			attribute2dbField.put("nProp4DescriptionSys10ITM29","NT4.DESCRIPTION");
			attribute2dbField.put("nProp5DescriptionSys10ITM29","NT5.DESCRIPTION");
			attribute2dbField.put("nProp6DescriptionSys10ITM29","NT6.DESCRIPTION");
			attribute2dbField.put("nProp7DescriptionSys10ITM29","NT7.DESCRIPTION");
			attribute2dbField.put("nProp8DescriptionSys10ITM29","NT8.DESCRIPTION");
			attribute2dbField.put("nProp9DescriptionSys10ITM29","NT9.DESCRIPTION");

			// read from ITM29 table...
			Response answer = QueryUtil.getQuery(
					conn,
					new UserSessionParameters(username),
					sql,
					values,
					attribute2dbField,
					ItemSheetLevelVO.class,
					"Y",
					"N",
					null,
					new GridParams(),
					true
			);

			if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching item sheets levels",ex);
			throw new Exception(ex.getMessage());
		}
		finally {
				try {
						pstmt.close();
				}
				catch (Exception exx) {}
				try {
						if (this.conn==null && conn!=null) {
								// close only local connection
								conn.commit();
								conn.close();
						}
				}
				catch (Exception exx) {}
		}
	}





	/**
	 * Business logic to execute.
	 */
	public VOListResponse loadSheetSpareParts(
		  GridParams gridParams,
			String serverLanguageId,String username,
			String companyCodeSys01ITM25,String sheetCodeITM25
	) throws Throwable {
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;

			String sql =
					"select ITM27_SHEETS_SPARE_PARTS.COMPANY_CODE_SYS01,ITM27_SHEETS_SPARE_PARTS.SHEET_CODE_ITM25,"+
					"ITM27_SHEETS_SPARE_PARTS.ITEM_CODE_ITM01,SYS10_TRANSLATIONS.DESCRIPTION,ITM01_ITEMS.PROGRESSIVE_HIE02 "+
					" from ITM27_SHEETS_SPARE_PARTS,ITM01_ITEMS,SYS10_TRANSLATIONS where "+
					"ITM27_SHEETS_SPARE_PARTS.COMPANY_CODE_SYS01=? and "+
					"ITM27_SHEETS_SPARE_PARTS.SHEET_CODE_ITM25=? and "+
					"ITM27_SHEETS_SPARE_PARTS.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
					"ITM27_SHEETS_SPARE_PARTS.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and "+
					"ITM01_ITEMS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
					"SYS10_TRANSLATIONS.LANGUAGE_CODE=? ";

			ArrayList values = new ArrayList();
			values.add(companyCodeSys01ITM25);
			values.add(sheetCodeITM25);
			values.add(serverLanguageId);

			Map attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01ITM27","ITM27_SHEETS_SPARE_PARTS.COMPANY_CODE_SYS01");
			attribute2dbField.put("sheetCodeItm25ITM27","ITM27_SHEETS_SPARE_PARTS.SHEET_CODE_ITM25");
			attribute2dbField.put("itemCodeItm01ITM27","ITM27_SHEETS_SPARE_PARTS.ITEM_CODE_ITM01");
			attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
			attribute2dbField.put("progressiveHie02ITM01","ITM01_ITEMS.PROGRESSIVE_HIE02");

			// read from ITM27 table...
			Response answer = QueryUtil.getQuery(
					conn,
					new UserSessionParameters(username),
					sql,
					values,
					attribute2dbField,
					SheetSparePartVO.class,
					"Y",
					"N",
					null,
					gridParams,
					50,
					true
			);

			if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching sheet's spare parts list",ex);
			throw new Exception(ex.getMessage());
		}
		finally {
				try {
						pstmt.close();
				}
				catch (Exception exx) {}
				try {
						if (this.conn==null && conn!=null) {
								// close only local connection
								conn.commit();
								conn.close();
						}
				}
				catch (Exception exx) {}
		}
	}


	public VOListResponse insertSheetSpareParts(ArrayList vos,String serverLanguageId,String username) throws Throwable {
		ResultSet rset = null;
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;

			Map attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01ITM27","COMPANY_CODE_SYS01");
			attribute2dbField.put("sheetCodeItm25ITM27","SHEET_CODE_ITM25");
			attribute2dbField.put("itemCodeItm01ITM27","ITEM_CODE_ITM01");


		  SheetSparePartVO vo = null;
			for(int i=0;i<vos.size();i++) {
				vo = (SheetSparePartVO)vos.get(i);

				// insert into ITM27...
				Response res = QueryUtil.insertTable(
							conn,
							new UserSessionParameters(username),
							vos,
							"ITM27_SHEETS_SPARE_PARTS",
							attribute2dbField,
							"Y",
							"N",
							null,
							true
				);
				if (res.isError()) throw new Exception(res.getErrorMessage());

			}

			// retrieve all root sheets having the specified sheet code as leaf...
			pstmt = conn.prepareStatement(
				"select ROOT_SHEET_CODE_ITM25 FROM ITM24_LEAFSHEETS WHERE COMPANY_CODE_SYS01=? AND SHEET_CODE_ITM25=?"
			);
			pstmt.setString(1,vo.getCompanyCodeSys01ITM27());
			pstmt.setString(2,vo.getSheetCodeItm25ITM27());
			rset = pstmt.executeQuery();
			while(rset.next()) {
				recalculateRootSheetSpareParts(conn,vo.getCompanyCodeSys01ITM27(),rset.getString(1));
			}
			rset.close();
			pstmt.close();

			return new VOListResponse(vos,false,vos.size());
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while inserting sheet's spare parts",ex);
			try {
				if (this.conn==null && conn!=null)
					// rollback only local connection
					conn.rollback();
			}
			catch (Exception ex3) {
			}
			throw new Exception(ex.getMessage());
		}
		finally {
			try {
				rset.close();
			}
			catch (Exception ex2) {
			}
			try {
				pstmt.close();
			}
			catch (Exception ex2) {
			}

			try {
					if (this.conn==null && conn!=null) {
							// close only local connection
							conn.commit();
							conn.close();
					}

			}
			catch (Exception exx) {}
		}
	}


	public VOResponse deleteSheetSpareParts(ArrayList vos,String serverLanguageId,String username) throws Throwable {
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;
			SheetSparePartVO vo = null;

			pstmt = conn.prepareStatement("delete from ITM27_SHEETS_SPARE_PARTS where COMPANY_CODE_SYS01=? and ITEM_CODE_ITM01=? and SHEET_CODE_ITM25=?");

			pstmt2 = conn.prepareStatement(
				"delete from ITM28_ROOT_S_SPARE_PARTS where "+
				"COMPANY_CODE_SYS01=? and ITEM_CODE_ITM01=? and "+
				"ROOT_SHEET_CODE_ITM25 in ("+
				"  select ITM24_LEAFSHEETS.ROOT_SHEET_CODE_ITM25 from ITM24_LEAFSHEETS,ITM27_SHEETS_SPARE_PARTS where "+
				"  ITM24_LEAFSHEETS.COMPANY_CODE_SYS01=? and "+
				"  ITM24_LEAFSHEETS.SHEET_CODE_ITM25=? and "+
				"  ITM24_LEAFSHEETS.COMPANY_CODE_SYS01=ITM27_SHEETS_SPARE_PARTS.COMPANY_CODE_SYS01 and "+
				"  ITM24_LEAFSHEETS.SHEET_CODE_ITM25=ITM27_SHEETS_SPARE_PARTS.SHEET_CODE_ITM25 and "+
				"  ITM27_SHEETS_SPARE_PARTS.ITEM_CODE_ITM01=? "+
				")"
			);

			for(int i=0;i<vos.size();i++) {
				vo = (SheetSparePartVO)vos.get(i);

				// delete link also as root sheet's spare part in ITM30...
				pstmt2.setString(1,vo.getCompanyCodeSys01ITM27());
				pstmt2.setString(2,vo.getItemCodeItm01ITM27());
      	pstmt2.setString(3,vo.getCompanyCodeSys01ITM27());
				pstmt2.setString(4,vo.getSheetCodeItm25ITM27());
				pstmt2.setString(5,vo.getItemCodeItm01ITM27());
				pstmt2.execute();

				// phisically delete all records...
				pstmt.setString(1,vo.getCompanyCodeSys01ITM27());
				pstmt.setString(2,vo.getItemCodeItm01ITM27());
				pstmt.setString(3,vo.getSheetCodeItm25ITM27());
				pstmt.execute();

			}
			pstmt.close();

			return new VOResponse(Boolean.TRUE);
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while inserting sheet's spare parts",ex);
			try {
				if (this.conn==null && conn!=null)
					// rollback only local connection
					conn.rollback();
			}
			catch (Exception ex3) {
			}
			throw new Exception(ex.getMessage());
		}
		finally {
			try {
				pstmt.close();
			}
			catch (Exception ex2) {
			}
			try {
				pstmt2.close();
			}
			catch (Exception ex2) {
			}

			try {
					if (this.conn==null && conn!=null) {
							// close only local connection
							conn.commit();
							conn.close();
					}

			}
			catch (Exception exx) {}
		}
	}









	/**
	 * Business logic to execute.
	 */
	public VOListResponse loadSheetAttachedDocs(GridParams gridParams,String serverLanguageId,String username) throws Throwable {
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;

			String sql =
					"select ITM26_SHEETS_ATTACHED_DOCS.COMPANY_CODE_SYS01,ITM26_SHEETS_ATTACHED_DOCS.PROGRESSIVE_DOC14,"+
					"ITM26_SHEETS_ATTACHED_DOCS.SHEET_CODE_ITM25,DOC14_DOCUMENTS.DESCRIPTION,ITM26_SHEETS_ATTACHED_DOCS.PROGRESSIVE_HIE01, "+
					"HIE01_LEVELS.PROGRESSIVE_HIE02 "+
					"from ITM26_SHEETS_ATTACHED_DOCS,DOC14_DOCUMENTS,HIE01_LEVELS where "+
					"ITM26_SHEETS_ATTACHED_DOCS.COMPANY_CODE_SYS01=DOC14_DOCUMENTS.COMPANY_CODE_SYS01 and "+
					"ITM26_SHEETS_ATTACHED_DOCS.PROGRESSIVE_DOC14=DOC14_DOCUMENTS.PROGRESSIVE and "+
					"HIE01_LEVELS.PROGRESSIVE=ITM26_SHEETS_ATTACHED_DOCS.PROGRESSIVE_HIE01 and "+
					"ITM26_SHEETS_ATTACHED_DOCS.COMPANY_CODE_SYS01=? and "+
					"ITM26_SHEETS_ATTACHED_DOCS.SHEET_CODE_ITM25=?";

			Map attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01ITM26","ITM26_SHEETS_ATTACHED_DOCS.COMPANY_CODE_SYS01");
			attribute2dbField.put("descriptionDOC14","DOC14_DOCUMENTS.DESCRIPTION");
			attribute2dbField.put("sheetCodeItm25ITM26","ITM26_SHEETS_ATTACHED_DOCS.SHEET_CODE_ITM25");
			attribute2dbField.put("progressiveDoc14ITM26","ITM26_SHEETS_ATTACHED_DOCS.PROGRESSIVE_DOC14");
			attribute2dbField.put("progressiveHie01ITM26","ITM26_SHEETS_ATTACHED_DOCS.PROGRESSIVE_HIE01");
			attribute2dbField.put("progressiveHie02HIE01","HIE01_LEVELS.PROGRESSIVE_HIE02");

			String companyCode = (String)gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01);
			String sheetCode = (String)gridParams.getOtherGridParams().get(ApplicationConsts.ID);

			ArrayList values = new ArrayList();
			values.add(companyCode);
			values.add(sheetCode);

			// read from ITM26 table...
			Response answer = QueryUtil.getQuery(
					conn,
					new UserSessionParameters(username),
					sql,
					values,
					attribute2dbField,
					SheetAttachedDocVO.class,
					"Y",
					"N",
					null,
					gridParams,
					true
			);

			if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching sheet's attached documents list",ex);
			try {
				if (this.conn==null && conn!=null)
					// rollback only local connection
					conn.rollback();
			}
			catch (Exception ex3) {
			}
			throw new Exception(ex.getMessage());
		}
		finally {
				try {
						if (this.conn==null && conn!=null) {
								// close only local connection
								conn.commit();
								conn.close();
						}

				}
				catch (Exception exx) {}
		}


	}



	/**
	 * Business logic to execute.
	 */
	public VOListResponse insertSheetAttachedDocs(ArrayList list,String serverLanguageId,String username) throws Throwable {
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;
			SheetAttachedDocVO vo = null;


			Map attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01ITM26","COMPANY_CODE_SYS01");
			attribute2dbField.put("sheetCodeItm25ITM26","SHEET_CODE_ITM25");
			attribute2dbField.put("progressiveDoc14ITM26","PROGRESSIVE_DOC14");
			attribute2dbField.put("progressiveHie01ITM26","PROGRESSIVE_HIE01");

			Response res = null;
			for(int i=0;i<list.size();i++) {
				vo = (SheetAttachedDocVO)list.get(i);

				// insert into ITM26...
				res = QueryUtil.insertTable(
						conn,
						new UserSessionParameters(username),
						vo,
						"ITM26_SHEETS_ATTACHED_DOCS",
						attribute2dbField,
						"Y",
						"N",
						null,
						true
				);
				if (res.isError()) {
					throw new Exception(res.getErrorMessage());
				}
			}


			return new VOListResponse(list,false,list.size());
		}
		catch (Throwable ex) {
			Logger.error(username, this.getClass().getName(),
					"executeCommand", "Error while inserting new attached documents to the specified sheet", ex);
			try {
				if (this.conn==null && conn!=null)
					// rollback only local connection
					conn.rollback();
			}
			catch (Exception ex3) {
			}
			throw new Exception(ex.getMessage());
		}
		finally {
				try {
						if (this.conn==null && conn!=null) {
								// close only local connection
								conn.commit();
								conn.close();
						}
			}
				catch (Exception exx) {}
		}
	}


	/**
	 * Business logic to execute.
	 */
	public VOResponse deleteSheetAttachedDocs(ArrayList list,String serverLanguageId,String username) throws Throwable {
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;

			pstmt = conn.prepareStatement(
				"delete from ITM26_SHEETS_ATTACHED_DOCS where COMPANY_CODE_SYS01=? and SHEET_CODE_ITM25=? and PROGRESSIVE_DOC14=? and PROGRESSIVE_HIE01=?"
			);

			SheetAttachedDocVO vo = null;
			for(int i=0;i<list.size();i++) {
				// phisically delete the record in ITM26...
				vo = (SheetAttachedDocVO)list.get(i);
				pstmt.setString(1,vo.getCompanyCodeSys01ITM26());
				pstmt.setString(2,vo.getSheetCodeItm25ITM26());
				pstmt.setBigDecimal(3,vo.getProgressiveDoc14ITM26());
				pstmt.setBigDecimal(4,vo.getProgressiveHie01ITM26());
				pstmt.execute();
			}

			return  new VOResponse(new Boolean(true));
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing sheet's attached documents",ex);
			try {
				if (this.conn==null && conn!=null)
					// rollback only local connection
					conn.rollback();
			}
			catch (Exception ex3) {
			}
			throw new Exception(ex.getMessage());
		}
		finally {
				try {
						pstmt.close();
				}
				catch (Exception exx) {}
				try {
						if (this.conn==null && conn!=null) {
								// close only local connection
								conn.commit();
								conn.close();
						}

				}
				catch (Exception exx) {}
		}
	}




	/**
	 * Business logic to execute.
	 */
	public VOListResponse loadItemSpareParts(
			GridParams gridParams,
			String serverLanguageId,String username,
			String companyCodeSys01ITM01,String itemCodeITM01
	) throws Throwable {
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;

			String sql =
					"select ITM28_ROOT_S_SPARE_PARTS.COMPANY_CODE_SYS01,ITM28_ROOT_S_SPARE_PARTS.ROOT_SHEET_CODE_ITM25,"+
					"ITM28_ROOT_S_SPARE_PARTS.ITEM_CODE_ITM01,SYS10_TRANSLATIONS.DESCRIPTION,ITM01_ITEMS.PROGRESSIVE_HIE02 "+
					" from ITM28_ROOT_S_SPARE_PARTS,ITM01_ITEMS I,ITM01_ITEMS,SYS10_TRANSLATIONS where "+
					"I.COMPANY_CODE_SYS01=? and "+
					"I.ITEM_CODE=? and "+
					"I.COMPANY_CODE_SYS01=ITM28_ROOT_S_SPARE_PARTS.COMPANY_CODE_SYS01 and "+
					"I.SHEET_CODE_ITM25=ITM28_ROOT_S_SPARE_PARTS.ROOT_SHEET_CODE_ITM25 and "+
					"ITM28_ROOT_S_SPARE_PARTS.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
					"ITM28_ROOT_S_SPARE_PARTS.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and "+
					"ITM01_ITEMS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
					"SYS10_TRANSLATIONS.LANGUAGE_CODE=? ";

			ArrayList values = new ArrayList();
			values.add(companyCodeSys01ITM01);
			values.add(itemCodeITM01);
			values.add(serverLanguageId);

			Map attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01ITM28","ITM28_ROOT_S_SPARE_PARTS.COMPANY_CODE_SYS01");
			attribute2dbField.put("rootSheetCodeItm25ITM28","ITM28_ROOT_S_SPARE_PARTS.ROOT_SHEET_CODE_ITM25");
			attribute2dbField.put("itemCodeItm01ITM28","ITM28_ROOT_S_SPARE_PARTS.ITEM_CODE_ITM01");
			attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
			attribute2dbField.put("progressiveHie02ITM01","ITM01_ITEMS.PROGRESSIVE_HIE02");

			// read from ITM28 table...
			Response answer = QueryUtil.getQuery(
					conn,
					new UserSessionParameters(username),
					sql,
					values,
					attribute2dbField,
					ItemSparePartVO.class,
					"Y",
					"N",
					null,
					gridParams,
					50,
					true
			);

			if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching item's spare parts list",ex);
			throw new Exception(ex.getMessage());
		}
		finally {
				try {
						pstmt.close();
				}
				catch (Exception exx) {}
				try {
						if (this.conn==null && conn!=null) {
								// close only local connection
								conn.commit();
								conn.close();
						}
				}
				catch (Exception exx) {}
		}
	}






}
