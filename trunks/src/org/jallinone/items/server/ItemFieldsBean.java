package org.jallinone.items.server;

import java.sql.Connection;
import javax.sql.DataSource;
import org.jallinone.items.java.ItemFieldVO;
import org.openswing.swing.message.receive.java.VOListResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import org.openswing.swing.logger.server.Logger;
import java.util.HashMap;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.server.UserSessionParameters;
import java.util.HashSet;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.send.java.GridParams;
import java.util.Map;
import org.openswing.swing.server.QueryUtil;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.*;
import java.util.Iterator;
import org.jallinone.system.translations.server.CompanyTranslationUtils;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Bean used for item fields management.</p>
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
public class ItemFieldsBean implements ItemFields {


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


	public ItemFieldsBean() {
	}





	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
	 */
	public ItemFieldVO getItemField() {
		throw new UnsupportedOperationException();
	}


	public VOListResponse loadItemFields(String companyCodeSys01,BigDecimal progressiveHIE02,String serverLanguageId,String username) throws Throwable {
		Connection conn = null;
		ResultSet rset = null;
		PreparedStatement pstmt = null;
		Statement stmt = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;

	    HashSet toSkip = new HashSet();
			toSkip.add("COMPANY_CODE_SYS01");
			toSkip.add("ITEM_CODE");
			toSkip.add("PROGRESSIVE_SYS10");
			toSkip.add("ADD_PROGRESSIVE_SYS10");
			toSkip.add("PROGRESSIVE_HIE01");
			toSkip.add("PROGRESSIVE_HIE02");
			toSkip.add("SMALL_IMAGE");
			toSkip.add("LARGE_IMAGE");
			toSkip.add("SERIAL_NUMBER_REQUIRED");
			toSkip.add("USE_VARIANT_1");
			toSkip.add("USE_VARIANT_2");
			toSkip.add("USE_VARIANT_3");
			toSkip.add("USE_VARIANT_4");
			toSkip.add("USE_VARIANT_5");
			toSkip.add("BARCODE_TYPE");
			toSkip.add("SHEET_CODE_ITM25");
			toSkip.add("CREATE_USER");
			toSkip.add("CREATE_DATE");
			toSkip.add("LAST_UPDATE_USER");
			toSkip.add("LAST_UPDATE_DATE");
			toSkip.add("ENABLED");

    	rset = conn.getMetaData().getColumns(conn.getCatalog(),null,"ITM01_ITEMS",null);
			stmt = rset.getStatement();
			HashMap map = new HashMap();
			int t;
			while(rset.next()) {
				if (!toSkip.contains(rset.getString(4))) {
					t = rset.getInt(5);
					if (t==Types.BIGINT || t==Types.NUMERIC || t==Types.SMALLINT)
				    t = Types.INTEGER;
					if (t==Types.CHAR)
						t = Types.VARCHAR;
					if (t==Types.DOUBLE || t==Types.FLOAT || t==Types.REAL)
 						t = Types.DECIMAL;

					map.put(rset.getString(4), new Integer(t));
				}
			}


			String sql =
					"select ITM32_ITEM_FIELDS.COMPANY_CODE_SYS01,ITM32_ITEM_FIELDS.PROGRESSIVE_HIE02,ITM32_ITEM_FIELDS.FIELD_NAME,"+
					"ITM32_ITEM_FIELDS.FIELD_TYPE,ITM32_ITEM_FIELDS.POS,ITM32_ITEM_FIELDS.SELECTED, "+
					"SYS10_COMPANY_TRANSLATIONS.DESCRIPTION,ITM32_ITEM_FIELDS.PROGRESSIVE_SYS10 from ITM32_ITEM_FIELDS,SYS10_COMPANY_TRANSLATIONS "+
					"where ITM32_ITEM_FIELDS.COMPANY_CODE_SYS01=? and ITM32_ITEM_FIELDS.PROGRESSIVE_HIE02=? AND "+
					"ITM32_ITEM_FIELDS.COMPANY_CODE_SYS01=SYS10_COMPANY_TRANSLATIONS.COMPANY_CODE_SYS01 AND "+
					"ITM32_ITEM_FIELDS.PROGRESSIVE_SYS10=SYS10_COMPANY_TRANSLATIONS.PROGRESSIVE AND "+
					"SYS10_COMPANY_TRANSLATIONS.LANGUAGE_CODE=? "+
					"ORDER BY ITM32_ITEM_FIELDS.POS ";

			Map attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01ITM32","ITM32_ITEM_FIELDS.COMPANY_CODE_SYS01");
			attribute2dbField.put("progressiveHie02ITM32","ITM32_ITEM_FIELDS.PROGRESSIVE_HIE02");
			attribute2dbField.put("fieldNameITM32","ITM32_ITEM_FIELDS.FIELD_NAME");
			attribute2dbField.put("fieldTypeITM32","ITM32_ITEM_FIELDS.FIELD_TYPE");
			attribute2dbField.put("posTM32","ITM32_ITEM_FIELDS.POS");
			attribute2dbField.put("selectedITM32","ITM32_ITEM_FIELDS.SELECTED");
			attribute2dbField.put("descriptionSYS10","SYS10_COMPANY_TRANSLATIONS.DESCRIPTION");
			attribute2dbField.put("progressiveSys10ITM32","ITM32_ITEM_FIELDS.PROGRESSIVE_SYS10");

			ArrayList values = new ArrayList();
			values.add(companyCodeSys01);
			values.add(progressiveHIE02);
			values.add(serverLanguageId);

			// read from ITM32 table...
			Response answer = QueryUtil.getQuery(
					conn,
					new UserSessionParameters(username),
					sql,
					values,
					attribute2dbField,
					ItemFieldVO.class,
					"Y",
					"N",
					null,
					new GridParams(),
					true
			);
			if (answer.isError())
				throw new Exception(answer.getErrorMessage());
			else {

				// remove all fields from db...
				pstmt = conn.prepareStatement(
					"DELETE FROM ITM32_ITEM_FIELDS WHERE "+
					"ITM32_ITEM_FIELDS.COMPANY_CODE_SYS01=? and "+
					"ITM32_ITEM_FIELDS.PROGRESSIVE_HIE02=?"
				);
				pstmt.setString(1,companyCodeSys01);
				pstmt.setBigDecimal(2,progressiveHIE02);
				pstmt.execute();
				pstmt.close();

				// res contains the list of fields already set (in the past)
				// now it will be removed any field not more available in the table...
				// map will contain only NEW fields, not yet set
				VOListResponse res = (VOListResponse)answer;
				ItemFieldVO vo = null;
				int i=0;
				while(i<res.getRows().size()) {
					vo = (ItemFieldVO)res.getRows().get(i);
					if (map.containsKey(vo.getFieldNameITM32())) {
						  map.remove(vo.getFieldNameITM32());
							i++;
					}
					else
						res.getRows().remove(i);
				}



				pstmt = conn.prepareStatement(
					"INSERT INTO ITM32_ITEM_FIELDS(COMPANY_CODE_SYS01,PROGRESSIVE_HIE02,FIELD_NAME,FIELD_TYPE,POS,PROGRESSIVE_SYS10,CREATE_USER,CREATE_DATE,SELECTED) "+
					"VALUES(?,?,?,?,?,?,?,?,?)"
				);

				// instance new object for new fields...
				Iterator it = map.keySet().iterator();
				String field = null;
				Integer type = null;
				while(it.hasNext()) {
					field = it.next().toString();
					type = (Integer)map.get(field);

	        vo = new ItemFieldVO();
					vo.setCompanyCodeSys01ITM32(companyCodeSys01);
					vo.setProgressiveHie02ITM32(progressiveHIE02);
					vo.setFieldNameITM32(field);
					vo.setDescriptionSYS10(field);
					vo.setFieldTypeITM32(new BigDecimal(type.intValue()));
					vo.setPosTM32(new BigDecimal(res.getRows().size()+1));
					vo.setSelectedITM32(Boolean.FALSE);
        	res.getRows().add(vo);
				}

	      res.setResultSetLength(res.getRows().size());
				res.setTotalAmountOfRows(res.getRows().size());


				// re-insert ALL fields...
				for(i=0;i<res.getRows().size();i++) {

					vo = (ItemFieldVO)res.getRows().get(i);
					pstmt.setString(1,companyCodeSys01);
					pstmt.setBigDecimal(2,progressiveHIE02);
					pstmt.setString(3,vo.getFieldNameITM32());
					pstmt.setInt(4,vo.getFieldTypeITM32().intValue());
					pstmt.setInt(5,i+1);

	        if (vo.getProgressiveSys10ITM32()==null) {
						vo.setProgressiveSys10ITM32( CompanyTranslationUtils.insertTranslations(vo.getDescriptionSYS10(),companyCodeSys01,username,conn) );
					}
					else {
						CompanyTranslationUtils.updateTranslation(companyCodeSys01,vo.getDescriptionSYS10(),vo.getDescriptionSYS10(),vo.getProgressiveSys10ITM32(),serverLanguageId,username,conn);
					}
					pstmt.setBigDecimal(6, vo.getProgressiveSys10ITM32());
					pstmt.setString(7,username);
					pstmt.setTimestamp(8,new Timestamp(System.currentTimeMillis()));
					pstmt.setString(9,vo.getSelectedITM32().booleanValue()?"Y":"N");
					pstmt.execute();

				}


				return res;
			}
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching item fields list",ex);
			throw new Exception(ex.getMessage());
		}
		finally {
				try {
						if (rset==null && rset!=null) {
								rset.close();
						}

				}
				catch (Exception exx) {}
				try {
						if (stmt==null && stmt!=null) {
								stmt.close();
						}

				}
				catch (Exception exx) {}
				try {
						if (pstmt==null && pstmt!=null) {
								pstmt.close();
						}

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


	public VOListResponse updateItemFields(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username) throws Throwable {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;
			ItemFieldVO vo = null;
			ItemFieldVO oldVO = null;

			pstmt = conn.prepareStatement(
			  "UPDATE ITM32_ITEM_FIELDS SET POS=?,SELECTED=? WHERE COMPANY_CODE_SYS01=? AND PROGRESSIVE_HIE02=? AND FIELD_NAME=? "
			);

			for(int i=0;i<newVOs.size();i++) {
				vo = (ItemFieldVO)newVOs.get(i);
				oldVO = (ItemFieldVO)oldVOs.get(i);

	      CompanyTranslationUtils.updateTranslation(vo.getCompanyCodeSys01ITM32(),oldVO.getDescriptionSYS10(),vo.getDescriptionSYS10(),vo.getProgressiveSys10ITM32(),serverLanguageId,username,conn);

  			pstmt.setBigDecimal(1,vo.getPosTM32());
				if (vo.getSelectedITM32().booleanValue())
					pstmt.setString(2,"Y");
				else
					pstmt.setString(2,"N");
				pstmt.setString(3,vo.getCompanyCodeSys01ITM32());
				pstmt.setBigDecimal(4,vo.getProgressiveHie02ITM32());
				pstmt.setString(5,vo.getFieldNameITM32());
				pstmt.execute();
			}

			return new VOListResponse(newVOs,false,newVOs.size());
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing item fields",ex);
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
						if (pstmt!=null) {
								pstmt.close();
						}

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
