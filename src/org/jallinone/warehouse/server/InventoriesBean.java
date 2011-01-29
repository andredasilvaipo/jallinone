package org.jallinone.warehouse.server;

import org.jallinone.warehouse.java.*;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.message.receive.java.VOResponse;
import java.util.ArrayList;
import org.openswing.swing.message.send.java.GridParams;
import java.math.BigDecimal;
import javax.sql.DataSource;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.warehouse.java.*;
import org.jallinone.warehouse.java.InventoryVO;
import java.util.Map;
import java.util.HashMap;
import org.openswing.swing.server.QueryUtil;
import org.openswing.swing.server.UserSessionParameters;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.logger.server.Logger;
import java.util.HashSet;
import java.sql.*;
import org.jallinone.hierarchies.server.HierarchiesBean;
import org.jallinone.hierarchies.java.HierarchyLevelVO;
import org.jallinone.system.progressives.server.CompanyProgressiveUtils;
import java.util.List;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage the inventory.</p>
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

public class InventoriesBean implements Inventories {


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


  private HierarchiesBean bean;

	public void setBean(HierarchiesBean bean) {
		this.bean = bean;
	}





	public InventoryVO insertInventory(InventoryVO vo,String serverLanguageId,String username) throws Throwable {
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;

			HashMap attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01WAR06","COMPANY_CODE_SYS01");
			attribute2dbField.put("progressiveWAR06","PROGRESSIVE");
			attribute2dbField.put("warehouseCodeWar01WAR06","WAREHOUSE_CODE_WAR01");
			attribute2dbField.put("itemTypeWAR06","ITEM_TYPE");
			attribute2dbField.put("stateWAR06","STATE");
			attribute2dbField.put("descriptionWAR06","DESCRIPTION");
			attribute2dbField.put("startDateWAR06","START_DATE");
			attribute2dbField.put("endDateWAR06","END_DATE");
			attribute2dbField.put("noteWAR06","NOTE");

			BigDecimal progressiveWAR06 = CompanyProgressiveUtils.getInternalProgressive(vo.getCompanyCodeSys01WAR06(),"WAR06_INVENTORIES","PROGRESSIVE",conn);
			vo.setProgressiveWAR06(progressiveWAR06);

			Response res = new QueryUtil().insertTable(
					conn,
					new UserSessionParameters(username),
					vo,
					"WAR06_INVENTORIES",
					attribute2dbField,
					"Y",
					"N",
					null,
					true
			);
			if (res.isError()) {
				throw new Exception(res.getErrorMessage());
			}

			return vo;
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while inserting an inventory",ex);
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


	public VOListResponse loadInventories(GridParams gridParams,String serverLanguageId,String username) throws Throwable {
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;

			String companyCodeSys01 = (String)gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01);
			String warehouseCodeWAR01 = (String)gridParams.getOtherGridParams().get(ApplicationConsts.WAREHOUSE_CODE);

			String sql =
				"select WAR06_INVENTORIES.COMPANY_CODE_SYS01,WAR06_INVENTORIES.PROGRESSIVE,WAR06_INVENTORIES.WAREHOUSE_CODE_WAR01,"+
				"WAR06_INVENTORIES.STATE,WAR06_INVENTORIES.DESCRIPTION,WAR06_INVENTORIES.START_DATE,WAR06_INVENTORIES.END_DATE, "+
				"WAR01_WAREHOUSES.DESCRIPTION,WAR06_INVENTORIES.ITEM_TYPE,WAR01_WAREHOUSES.PROGRESSIVE_HIE02 "+
				"from WAR06_INVENTORIES,WAR01_WAREHOUSES where "+
				"WAR06_INVENTORIES.COMPANY_CODE_SYS01=? AND "+
				"WAR06_INVENTORIES.COMPANY_CODE_SYS01=WAR01_WAREHOUSES.COMPANY_CODE_SYS01 AND "+
				"WAR06_INVENTORIES.WAREHOUSE_CODE_WAR01=WAR01_WAREHOUSES.WAREHOUSE_CODE ";

			Map attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01WAR06","WAR06_INVENTORIES.COMPANY_CODE_SYS01");
			attribute2dbField.put("progressiveWAR06","WAR06_INVENTORIES.PROGRESSIVE");
			attribute2dbField.put("warehouseCodeWar01WAR06","WAR06_INVENTORIES.WAREHOUSE_CODE_WAR01");
			attribute2dbField.put("stateWAR06","WAR06_INVENTORIES.STATE");
			attribute2dbField.put("itemTypeWAR06","WAR06_INVENTORIES.ITEM_TYPE");
			attribute2dbField.put("descriptionWAR06","WAR06_INVENTORIES.DESCRIPTION");
			attribute2dbField.put("startDateWAR06","WAR06_INVENTORIES.START_DATE");
			attribute2dbField.put("endDateWAR06","WAR06_INVENTORIES.END_DATE");
			attribute2dbField.put("noteWAR06","WAR06_INVENTORIES.NOTE");
			attribute2dbField.put("descriptionWAR01","WAR01_WAREHOUSES.DESCRIPTION");
			attribute2dbField.put("progressiveHie02WAR01","WAR01_WAREHOUSES.PROGRESSIVE_HIE02");

			ArrayList values = new ArrayList();
			values.add(companyCodeSys01);

	    if (warehouseCodeWAR01!=null && !"".equals(warehouseCodeWAR01)) {
				sql += " AND WAR06_INVENTORIES.WAREHOUSE_CODE_WAR01=? ";
				values.add(warehouseCodeWAR01);
			}


			// read from WAR06 table...
			Response answer = QueryUtil.getQuery(
					conn,
					new UserSessionParameters(username),
					sql,
					values,
					attribute2dbField,
					InventoryVO.class,
					"Y",
					"N",
					null,
					gridParams,
					true
			);
			if (answer.isError())
				throw new Exception(answer.getErrorMessage());
			else return (VOListResponse)answer;

		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching inventories list",ex);
			throw new Exception(ex.getMessage());
		}
		finally {
			try {
				if (this.conn == null && conn != null) {
					// close only local connection
					conn.commit();
					conn.close();
				}

			}
			catch (Exception exx) {}
		}
  }


	public VOListResponse updateInventories(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username) throws Throwable {
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;

			InventoryVO oldVO = null;
			InventoryVO newVO = null;
			Response res = null;

			HashSet pkAttrs = new HashSet();
			pkAttrs.add("companyCodeSys01WAR06");
			pkAttrs.add("progressiveWAR06");

			for(int i=0;i<oldVOs.size();i++) {
				oldVO = (InventoryVO)oldVOs.get(i);
				newVO = (InventoryVO)newVOs.get(i);


				HashMap attribute2dbField = new HashMap();
				attribute2dbField.put("warehouseCodeWar01WAR06","WAREHOUSE_CODE_WAR01");
				attribute2dbField.put("stateWAR06","STATE");
				attribute2dbField.put("itemTypeWAR06","ITEM_TYPE");
				attribute2dbField.put("descriptionWAR06","DESCRIPTION");
				attribute2dbField.put("startDateWAR06","START_DATE");
				attribute2dbField.put("endDateWAR06","END_DATE");
				attribute2dbField.put("noteWAR06","NOTE");
				attribute2dbField.put("companyCodeSys01WAR06","COMPANY_CODE_SYS01");
				attribute2dbField.put("progressiveWAR06","PROGRESSIVE");

				res = new QueryUtil().updateTable(
						conn,
						new UserSessionParameters(username),
						pkAttrs,
						oldVO,
						newVO,
						"WAR06_INVENTORIES",
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

			return new VOListResponse(newVOs,false,newVOs.size());
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing inventories",ex);
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


	public VOResponse deleteInventories(ArrayList list,String serverLanguageId,String username) throws Throwable {
		Statement stmt = null;
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;
			stmt = conn.createStatement();

			InventoryVO vo = null;
			for(int i=0;i<list.size();i++) {
				vo = (InventoryVO)list.get(i);

				// phisically  delete records in WAR07...
				stmt.execute(
				  "delete from WAR08_INVENTORY_S_N where PROGRESSIVE_WAR06="+vo.getProgressiveWAR06()
				);

      	// phisically  delete records in WAR07...
				stmt.execute(
					"delete from WAR07_INVENTORY_ITEMS where PROGRESSIVE_WAR06="+vo.getProgressiveWAR06()
				);

				// phisically delete the record in WAR06...
				stmt.execute(
					"delete from WAR06_INVENTORIES "+
					"where COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01WAR06()+"' and PROGRESSIVE="+vo.getProgressiveWAR06()
				);
			}

			return new VOResponse(new Boolean(true));
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing inventories",ex);
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
					if (this.conn==null && conn!=null) {
 							// close only local connection
							conn.commit();
							conn.close();
					}
			}
			catch (Exception exx) {}
		}
	}


	public VOResponse importInventoryItems(
		 InventoryVO vo,
		 BigDecimal progressiveHIE02,
		 BigDecimal progressiveHIE01,
		 String serverLanguageId,
		 String username
	) throws Throwable {
		 Connection conn = null;
		 PreparedStatement pstmt = null;
		 try {
			 if (this.conn==null) conn = getConn(); else conn = this.conn;
			 conn.setAutoCommit(true);
			 bean.setConn(conn);

       String sql =
				"update WAR06_INVENTORIES set STATE=? "+
				"where COMPANY_CODE_SYS01=? and PROGRESSIVE=? and STATE=? ";
	     pstmt = conn.prepareStatement(sql);
			 pstmt.setString(1,ApplicationConsts.IN_PROGRESS);
			 pstmt.setString(2,vo.getCompanyCodeSys01WAR06());
			 pstmt.setBigDecimal(3,vo.getProgressiveWAR06());
			 pstmt.setString(4,ApplicationConsts.OPENED);
			 int rows = pstmt.executeUpdate();
			 pstmt.close();
			 if (rows==0)
				 throw new Exception("an import in inventory is already in progress");


       String sql_1 =
					"INSERT INTO WAR07_INVENTORY_ITEMS("+
					"COMPANY_CODE_SYS01," +
					"PROGRESSIVE_WAR06," +
					"ITEM_CODE_ITM01," +
					"VARIANT_TYPE_ITM06," +
					"VARIANT_CODE_ITM11," +
					"VARIANT_TYPE_ITM07," +
					"VARIANT_CODE_ITM12," +
					"VARIANT_TYPE_ITM08," +
					"VARIANT_CODE_ITM13," +
					"VARIANT_TYPE_ITM09," +
					"VARIANT_CODE_ITM14," +
					"VARIANT_TYPE_ITM10," +
					"VARIANT_CODE_ITM15," +
					"STATE," +
					"PROGRESSIVE_HIE01," +
					"PROGRESSIVE_HIE02," +
					"QTY" +
					") SELECT " +
					"WAR03_ITEMS_AVAILABILITY.COMPANY_CODE_SYS01," +
					"?,"+
					"WAR03_ITEMS_AVAILABILITY.ITEM_CODE_ITM01," +
					"WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM06," +
					"WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM11," +
					"WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM07," +
					"WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM12," +
					"WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM08," +
					"WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM13," +
					"WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM09," +
					"WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM14," +
					"WAR03_ITEMS_AVAILABILITY.VARIANT_TYPE_ITM10," +
					"WAR03_ITEMS_AVAILABILITY.VARIANT_CODE_ITM15," +
					"?,"+
					"WAR03_ITEMS_AVAILABILITY.PROGRESSIVE_HIE01," +
					"ITM01_ITEMS.PROGRESSIVE_HIE02," +
					(vo.getItemTypeWAR06().equals(ApplicationConsts.ITEM_GOOD)?"WAR03_ITEMS_AVAILABILITY.AVAILABLE_QTY":"WAR03_ITEMS_AVAILABILITY.DAMAGED_QTY")+" ";
			 String sql_2 =
					"FROM WAR03_ITEMS_AVAILABILITY,ITM01_ITEMS ";
			 String sql_3 =
					"WHERE "+
					"WAR03_ITEMS_AVAILABILITY.COMPANY_CODE_SYS01=? AND " +
					"WAR03_ITEMS_AVAILABILITY.WAREHOUSE_CODE_WAR01=? AND "+
					"NOT "+(vo.getItemTypeWAR06().equals(ApplicationConsts.ITEM_GOOD)?"WAR03_ITEMS_AVAILABILITY.AVAILABLE_QTY":"WAR03_ITEMS_AVAILABILITY.DAMAGED_QTY")+"=0 AND "+
				  "ITM01_ITEMS.COMPANY_CODE_SYS01=WAR03_ITEMS_AVAILABILITY.COMPANY_CODE_SYS01 AND "+
				  "ITM01_ITEMS.ITEM_CODE=WAR03_ITEMS_AVAILABILITY.ITEM_CODE_ITM01 ";

				ArrayList params = new ArrayList();
				params.add(vo.getProgressiveWAR06());
				params.add(ApplicationConsts.OPENED);
				params.add(vo.getCompanyCodeSys01WAR06());
				params.add(vo.getWarehouseCodeWar01WAR06());

				if (progressiveHIE02!=null) {
					sql_3 += " AND ITM01_ITEMS.PROGRESSIVE_HIE02=? ";
					params.add(progressiveHIE02);
				}
				if (progressiveHIE01!=null) {
					VOListResponse res = bean.getLeaves(
							 progressiveHIE02,
							 progressiveHIE01,
							 serverLanguageId,
							 username
					);

         	sql_3 += " AND ITM01_ITEMS.PROGRESSIVE_HIE01 IN (";
					for(int i=0;i<res.getRows().size();i++)
 				    sql_3 += ((HierarchyLevelVO)res.getRows().get(i)).getProgressiveHIE01()+",";
					sql_3 += progressiveHIE01;
					sql_3 += ") ";
				}


			  pstmt = conn.prepareStatement(sql_1+sql_2+sql_3);
			  for(int i=0;i<params.size();i++) {
				  pstmt.setObject(i+1,params.get(i));
			  }
			  rows = pstmt.executeUpdate();
			  pstmt.close();


				sql_1 =
					"insert into WAR08_INVENTORY_S_N("+
					"SERIAL_NUMBER,"+
					"COMPANY_CODE_SYS01," +
					"PROGRESSIVE_WAR06," +
					"ITEM_CODE_ITM01," +
					"VARIANT_TYPE_ITM06," +
					"VARIANT_TYPE_ITM07," +
					"VARIANT_TYPE_ITM08," +
					"VARIANT_TYPE_ITM09," +
					"VARIANT_TYPE_ITM10," +
					"VARIANT_CODE_ITM11," +
					"VARIANT_CODE_ITM12," +
					"VARIANT_CODE_ITM13," +
					"VARIANT_CODE_ITM14," +
					"VARIANT_CODE_ITM15," +
					"PROGRESSIVE_HIE01) "+
					"select "	 +
					"WAR05_STORED_SERIAL_NUMBERS.SERIAL_NUMBER,"+
					"WAR05_STORED_SERIAL_NUMBERS.COMPANY_CODE_SYS01," +
					"?," +
					"WAR05_STORED_SERIAL_NUMBERS.ITEM_CODE_ITM01," +
					"WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM06," +
					"WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM07," +
					"WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM08," +
					"WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM09," +
					"WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM10," +
					"WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM11," +
					"WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM12," +
					"WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM13," +
					"WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM14," +
					"WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM15," +
					"WAR05_STORED_SERIAL_NUMBERS.PROGRESSIVE_HIE01 ";
				sql_2 =
					"from WAR05_STORED_SERIAL_NUMBERS,WAR01_WAREHOUSES ";
				sql_3 =
					 "WHERE "+
					 "WAR05_STORED_SERIAL_NUMBERS.COMPANY_CODE_SYS01=? AND " +
					 "WAR05_STORED_SERIAL_NUMBERS.COMPANY_CODE_SYS01=WAR01_WAREHOUSES.COMPANY_CODE_SYS01 AND "+
					 "WAR01_WAREHOUSES.WAREHOUSE_CODE=? AND "+
					 "WAR05_STORED_SERIAL_NUMBERS.PROGRESSIVE_HIE01 in (select PROGRESSIVE from HIE01_LEVELS where PROGRESSIVE_HIE02=WAR01_WAREHOUSES.PROGRESSIVE_HIE02)";

				 params = new ArrayList();
				 params.add(vo.getProgressiveWAR06());
				 params.add(vo.getCompanyCodeSys01WAR06());
				 params.add(vo.getWarehouseCodeWar01WAR06());

				 if (progressiveHIE02!=null || progressiveHIE01!=null) {
					 sql_2 += ",ITM01_ITEMS ";
					 sql_3 +=
						 " AND ITM01_ITEMS.COMPANY_CODE_SYS01=WAR05_STORED_SERIAL_NUMBERS.COMPANY_CODE_SYS01 "+
						 "	AND ITM01_ITEMS.ITEM_CODE=WAR05_STORED_SERIAL_NUMBERS.ITEM_CODE_ITM01 ";
				 }

				 if (progressiveHIE02!=null) {
					 sql_3 += " AND ITM01_ITEMS.PROGRESSIVE_HIE02=? ";
					 params.add(progressiveHIE02);
				 }
				 if (progressiveHIE01!=null) {
					 VOListResponse res = bean.getLeaves(
								progressiveHIE02,
								progressiveHIE01,
								serverLanguageId,
								username
					 );

						sql_3 += " AND ITM01_ITEMS.PROGRESSIVE_HIE01 IN (";
					 for(int i=0;i<res.getRows().size();i++)
							sql_3 += ((HierarchyLevelVO)res.getRows().get(i)).getProgressiveHIE01()+",";
					 sql_3 += progressiveHIE01;
					 sql_3 += ") ";
				 }


				pstmt = conn.prepareStatement(sql_1+sql_2+sql_3);
				for(int i=0;i<params.size();i++) {
					 pstmt.setObject(i+1,params.get(i));
				}
				try {
					pstmt.executeUpdate();
				}
				catch (SQLException ex3) {
				}
				pstmt.close();


			  Logger.debug(username,this.getClass().getName(),"importInventoryItems","Inserted "+rows+" rows in inventory.");

			  return new VOResponse(Boolean.FALSE);
		 }
		 catch (Throwable ex) {
			 Logger.error(username,this.getClass().getName(),"importInventoryItems","Error while importing inventory items",ex);

			 throw new Exception(ex.getMessage());
		 }
		 finally {
			try {
				pstmt.close();
			}
			catch (Exception ex1) {
			}

			try {
				String sql =
					"update WAR06_INVENTORIES set STATE=? " +
					"where COMPANY_CODE_SYS01=? and PROGRESSIVE=? and STATE=? ";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, ApplicationConsts.OPENED);
				pstmt.setString(2, vo.getCompanyCodeSys01WAR06());
				pstmt.setBigDecimal(3, vo.getProgressiveWAR06());
				pstmt.setString(4, ApplicationConsts.IN_PROGRESS);
				pstmt.executeUpdate();
				pstmt.close();
			}
			catch (Exception ex1) {
		  }

			try {
				bean.setConn(null);
			}
			catch (Exception ex2) {
			}

			try {
					 if (this.conn==null && conn!=null) {
						   conn.setAutoCommit(false);

							 // close only local connection
							 conn.commit();
							 conn.close();
					 }

			}
			catch (Exception exx) {}
		}
  }


	public InventoryItemVO insertInventoryItem(InventoryItemVO vo,String serverLanguageId,String username) throws Throwable {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;

//	    // check if the same item has been already inserted in the same location...
//			String sql =
//				"select * from WAR07_INVENTORY_ITEMS " +
//				"where "+
//				"COMPANY_CODE_SYS01=? and "+
//				"PROGRESSIVE_WAR06=? and "+
//				"ITEM_CODE_ITM01=? and "+
//				"VARIANT_TYPE_ITM06=? and "+
//				"VARIANT_TYPE_ITM07=? and "+
//				"VARIANT_TYPE_ITM08=? and "+
//				"VARIANT_TYPE_ITM09=? and "+
//				"VARIANT_TYPE_ITM10=? and "+
//				"VARIANT_CODE_ITM11=? and "+
//				"VARIANT_CODE_ITM12=? and "+
//				"VARIANT_CODE_ITM13=? and "+
//				"VARIANT_CODE_ITM14=? and "+
//				"VARIANT_CODE_ITM15=? and "+
//				"PROGRESSIVE_HIE01=? ";
//
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setString(1, vo.getCompanyCodeSys01WAR07());
//			pstmt.setBigDecimal(2, vo.getProgressiveWar06WAR07());
//			pstmt.setString(3, vo.getItemCodeItm01WAR07());
//			pstmt.setString(4, vo.getVariantTypeItm06());
//			pstmt.setString(5, vo.getVariantTypeItm07());
//			pstmt.setString(6, vo.getVariantTypeItm08());
//			pstmt.setString(7, vo.getVariantTypeItm09());
//			pstmt.setString(8, vo.getVariantTypeItm10());
//			pstmt.setString(9,  vo.getVariantCodeItm11());
//			pstmt.setString(10, vo.getVariantCodeItm12());
//			pstmt.setString(11, vo.getVariantCodeItm13());
//			pstmt.setString(12, vo.getVariantCodeItm14());
//			pstmt.setString(13, vo.getVariantCodeItm15());
//			pstmt.setBigDecimal(14, vo.getProgressiveHie01WAR07());
//			ResultSet rset = pstmt.executeQuery();
//			boolean found = false;
//			if (rset.next())
//				found = true;
//			rset.close();
//			pstmt.close();
//			if (found) {
//				throw new Exception("cannot insert twice the same item for the same location");
//			}
//
//
//			// check if the same item has been already inserted in the same REAL location...
//			sql =
//				"select * from WAR07_INVENTORY_ITEMS " +
//				"where "+
//				"COMPANY_CODE_SYS01=? and "+
//				"PROGRESSIVE_WAR06=? and "+
//				"ITEM_CODE_ITM01=? and "+
//				"VARIANT_TYPE_ITM06=? and "+
//				"VARIANT_TYPE_ITM07=? and "+
//				"VARIANT_TYPE_ITM08=? and "+
//				"VARIANT_TYPE_ITM09=? and "+
//				"VARIANT_TYPE_ITM10=? and "+
//				"VARIANT_CODE_ITM11=? and "+
//				"VARIANT_CODE_ITM12=? and "+
//				"VARIANT_CODE_ITM13=? and "+
//				"VARIANT_CODE_ITM14=? and "+
//				"VARIANT_CODE_ITM15=? and "+
//				"REAL_PROGRESSIVE_HIE01=? ";
//
//			pstmt = conn.prepareStatement(sql);
//			pstmt.setString(1, vo.getCompanyCodeSys01WAR07());
//			pstmt.setBigDecimal(2, vo.getProgressiveWar06WAR07());
//			pstmt.setString(3, vo.getItemCodeItm01WAR07());
//			pstmt.setString(4, vo.getVariantTypeItm06());
//			pstmt.setString(5, vo.getVariantTypeItm07());
//			pstmt.setString(6, vo.getVariantTypeItm08());
//			pstmt.setString(7, vo.getVariantTypeItm09());
//			pstmt.setString(8, vo.getVariantTypeItm10());
//			pstmt.setString(9,  vo.getVariantCodeItm11());
//			pstmt.setString(10, vo.getVariantCodeItm12());
//			pstmt.setString(11, vo.getVariantCodeItm13());
//			pstmt.setString(12, vo.getVariantCodeItm14());
//			pstmt.setString(13, vo.getVariantCodeItm15());
//			pstmt.setBigDecimal(14, vo.getRealProgressiveHie01WAR07());
//			rset = pstmt.executeQuery();
//			found = false;
//			if (rset.next())
//				found = true;
//			rset.close();
//			pstmt.close();
//			if (found) {
//				throw new Exception("cannot insert twice the same item for the same real location");
//			}

			if (vo.getQtyWAR07()!=null && vo.getRealQtyWAR07()!=null)
				vo.setDeltaQtyWAR07(vo.getRealQtyWAR07().subtract(vo.getQtyWAR07()));

			if (vo.getVariantCodeItm11()==null)
				vo.setVariantCodeItm11(ApplicationConsts.JOLLY);
			if (vo.getVariantCodeItm12()==null)
				vo.setVariantCodeItm12(ApplicationConsts.JOLLY);
			if (vo.getVariantCodeItm13()==null)
				vo.setVariantCodeItm13(ApplicationConsts.JOLLY);
			if (vo.getVariantCodeItm14()==null)
				vo.setVariantCodeItm14(ApplicationConsts.JOLLY);
			if (vo.getVariantCodeItm15()==null)
				vo.setVariantCodeItm15(ApplicationConsts.JOLLY);
			if (vo.getVariantTypeItm06()==null)
				vo.setVariantTypeItm06(ApplicationConsts.JOLLY);
			if (vo.getVariantTypeItm07()==null)
				vo.setVariantTypeItm07(ApplicationConsts.JOLLY);
			if (vo.getVariantTypeItm08()==null)
				vo.setVariantTypeItm08(ApplicationConsts.JOLLY);
			if (vo.getVariantTypeItm09()==null)
				vo.setVariantTypeItm09(ApplicationConsts.JOLLY);
			if (vo.getVariantTypeItm10()==null)
				vo.setVariantTypeItm10(ApplicationConsts.JOLLY);


			HashMap attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01WAR07","COMPANY_CODE_SYS01");
			attribute2dbField.put("progressiveWar06WAR07","PROGRESSIVE_WAR06");
			attribute2dbField.put("itemCodeItm01WAR07","ITEM_CODE_ITM01");
			attribute2dbField.put("variantTypeItm06","VARIANT_TYPE_ITM06");
			attribute2dbField.put("variantTypeItm07","VARIANT_TYPE_ITM07");
			attribute2dbField.put("variantTypeItm08","VARIANT_TYPE_ITM08");
			attribute2dbField.put("variantTypeItm09","VARIANT_TYPE_ITM09");
			attribute2dbField.put("variantTypeItm10","VARIANT_TYPE_ITM10");
			attribute2dbField.put("variantCodeItm11","VARIANT_CODE_ITM11");
			attribute2dbField.put("variantCodeItm12","VARIANT_CODE_ITM12");
			attribute2dbField.put("variantCodeItm13","VARIANT_CODE_ITM13");
			attribute2dbField.put("variantCodeItm14","VARIANT_CODE_ITM14");
			attribute2dbField.put("variantCodeItm15","VARIANT_CODE_ITM15");
			attribute2dbField.put("stateWAR07","STATE");
			attribute2dbField.put("progressiveHie01WAR07","PROGRESSIVE_HIE01");
			attribute2dbField.put("progressiveHie02WAR07","PROGRESSIVE_HIE02");
			attribute2dbField.put("realProgressiveHie01WAR07","REAL_PROGRESSIVE_HIE01");
			attribute2dbField.put("qtyWAR07","QTY");
			attribute2dbField.put("realQtyWAR07","REAL_QTY");
			attribute2dbField.put("deltaQtyWAR07","DELTA_QTY");


			Response res = new QueryUtil().insertTable(
					conn,
					new UserSessionParameters(username),
					vo,
					"WAR07_INVENTORY_ITEMS",
					attribute2dbField,
					"Y",
					"N",
					null,
					true
			);
			if (res.isError()) {
				throw new Exception(res.getErrorMessage());
			}

			pstmt = conn.prepareStatement(
			  "insert into WAR08_INVENTORY_S_N("+
			  "SERIAL_NUMBER,"+
			  "COMPANY_CODE_SYS01," +
				"PROGRESSIVE_WAR06," +
				"ITEM_CODE_ITM01," +
				"VARIANT_TYPE_ITM06," +
				"VARIANT_TYPE_ITM07," +
				"VARIANT_TYPE_ITM08," +
				"VARIANT_TYPE_ITM09," +
				"VARIANT_TYPE_ITM10," +
				"VARIANT_CODE_ITM11," +
				"VARIANT_CODE_ITM12," +
				"VARIANT_CODE_ITM13," +
				"VARIANT_CODE_ITM14," +
				"VARIANT_CODE_ITM15," +
				"PROGRESSIVE_HIE01) "+
				"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
			);
			for(int i=0;i<vo.getSerialNumbers().size();i++) {
				pstmt.setString(1,vo.getSerialNumbers().get(i).toString());
				pstmt.setString(2, vo.getCompanyCodeSys01WAR07());
				pstmt.setBigDecimal(3, vo.getProgressiveWar06WAR07());
				pstmt.setString(4, vo.getItemCodeItm01WAR07());
				pstmt.setString(5, vo.getVariantTypeItm06());
				pstmt.setString(6, vo.getVariantTypeItm07());
				pstmt.setString(7, vo.getVariantTypeItm08());
				pstmt.setString(8, vo.getVariantTypeItm09());
				pstmt.setString(9, vo.getVariantTypeItm10());
				pstmt.setString(10,  vo.getVariantCodeItm11());
				pstmt.setString(11, vo.getVariantCodeItm12());
				pstmt.setString(12, vo.getVariantCodeItm13());
				pstmt.setString(13, vo.getVariantCodeItm14());
				pstmt.setString(14, vo.getVariantCodeItm15());
				pstmt.setBigDecimal(15, vo.getProgressiveHie01WAR07());
				pstmt.execute();
			}
			pstmt.close();

			return vo;
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while inserting an inventory item",ex);
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


	public VOListResponse loadInventoryItems(
		 HashMap variant1Descriptions,
		 HashMap variant2Descriptions,
		 HashMap variant3Descriptions,
		 HashMap variant4Descriptions,
		 HashMap variant5Descriptions,
		 GridParams gridParams,String serverLanguageId,String username) throws Throwable {
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;

			String companyCodeSys01 = (String)gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01);
			BigDecimal progressiveWAR06 = (BigDecimal)gridParams.getOtherGridParams().get(ApplicationConsts.ID);

			String sql =
				"select WAR07_INVENTORY_ITEMS.COMPANY_CODE_SYS01,"+
				"WAR07_INVENTORY_ITEMS.PROGRESSIVE_WAR06,WAR07_INVENTORY_ITEMS.ITEM_CODE_ITM01,"+
				"WAR07_INVENTORY_ITEMS.VARIANT_TYPE_ITM06,WAR07_INVENTORY_ITEMS.VARIANT_CODE_ITM11,"+
				"WAR07_INVENTORY_ITEMS.VARIANT_TYPE_ITM07,WAR07_INVENTORY_ITEMS.VARIANT_CODE_ITM12,"+
				"WAR07_INVENTORY_ITEMS.VARIANT_TYPE_ITM08,WAR07_INVENTORY_ITEMS.VARIANT_CODE_ITM13,"+
				"WAR07_INVENTORY_ITEMS.VARIANT_TYPE_ITM09,WAR07_INVENTORY_ITEMS.VARIANT_CODE_ITM14,"+
				"WAR07_INVENTORY_ITEMS.VARIANT_TYPE_ITM10,WAR07_INVENTORY_ITEMS.VARIANT_CODE_ITM15,"+
				"WAR07_INVENTORY_ITEMS.STATE,WAR07_INVENTORY_ITEMS.PROGRESSIVE_HIE01,"+
				"WAR07_INVENTORY_ITEMS.REAL_PROGRESSIVE_HIE01,WAR07_INVENTORY_ITEMS.QTY,"+
				"WAR07_INVENTORY_ITEMS.REAL_QTY,WAR07_INVENTORY_ITEMS.DELTA_QTY, "+
				"SYS10_TRANSLATIONS.DESCRIPTION,WAR07_INVENTORY_ITEMS.PROGRESSIVE_HIE02 "+
			  "from WAR07_INVENTORY_ITEMS,ITM01_ITEMS,SYS10_TRANSLATIONS where "+
				"WAR07_INVENTORY_ITEMS.COMPANY_CODE_SYS01=? AND "+
				"WAR07_INVENTORY_ITEMS.PROGRESSIVE_WAR06=? AND "+
				"WAR07_INVENTORY_ITEMS.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 AND "+
				"WAR07_INVENTORY_ITEMS.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE AND "+
				"ITM01_ITEMS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE AND "+
				"SYS10_TRANSLATIONS.LANGUAGE_CODE=? ";

			Map attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01WAR07","WAR07_INVENTORY_ITEMS.COMPANY_CODE_SYS01");
			attribute2dbField.put("progressiveWar06WAR07","WAR07_INVENTORY_ITEMS.PROGRESSIVE_WAR06");
			attribute2dbField.put("itemCodeItm01WAR07","WAR07_INVENTORY_ITEMS.ITEM_CODE_ITM01");
			attribute2dbField.put("variantTypeItm06","WAR07_INVENTORY_ITEMS.VARIANT_TYPE_ITM06");
			attribute2dbField.put("variantTypeItm07","WAR07_INVENTORY_ITEMS.VARIANT_TYPE_ITM07");
			attribute2dbField.put("variantTypeItm08","WAR07_INVENTORY_ITEMS.VARIANT_TYPE_ITM08");
			attribute2dbField.put("variantTypeItm09","WAR07_INVENTORY_ITEMS.VARIANT_TYPE_ITM09");
			attribute2dbField.put("variantTypeItm10","WAR07_INVENTORY_ITEMS.VARIANT_TYPE_ITM10");
			attribute2dbField.put("variantCodeItm11","WAR07_INVENTORY_ITEMS.VARIANT_CODE_ITM11");
			attribute2dbField.put("variantCodeItm12","WAR07_INVENTORY_ITEMS.VARIANT_CODE_ITM12");
			attribute2dbField.put("variantCodeItm13","WAR07_INVENTORY_ITEMS.VARIANT_CODE_ITM13");
			attribute2dbField.put("variantCodeItm14","WAR07_INVENTORY_ITEMS.VARIANT_CODE_ITM14");
			attribute2dbField.put("variantCodeItm15","WAR07_INVENTORY_ITEMS.VARIANT_CODE_ITM15");
			attribute2dbField.put("stateWAR07","WAR07_INVENTORY_ITEMS.STATE");
			attribute2dbField.put("progressiveHie01WAR07","WAR07_INVENTORY_ITEMS.PROGRESSIVE_HIE01");
			attribute2dbField.put("progressiveHie02WAR07","WAR07_INVENTORY_ITEMS.PROGRESSIVE_HIE02");
			attribute2dbField.put("realProgressiveHie01WAR07","WAR07_INVENTORY_ITEMS.REAL_PROGRESSIVE_HIE01");
			attribute2dbField.put("qtyWAR07","WAR07_INVENTORY_ITEMS.QTY");
			attribute2dbField.put("realQtyWAR07","WAR07_INVENTORY_ITEMS.REAL_QTY");
			attribute2dbField.put("deltaQtyWAR07","WAR07_INVENTORY_ITEMS.DELTA_QTY");
			attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");

			ArrayList values = new ArrayList();
			values.add(companyCodeSys01);
			values.add(progressiveWAR06);
			values.add(serverLanguageId);

			int blockSize = 50;
			if (gridParams.getOtherGridParams().get(ApplicationConsts.BLOCK_SIZE)!=null)
				blockSize = Integer.parseInt(gridParams.getOtherGridParams().get(ApplicationConsts.BLOCK_SIZE).toString());

			// read from WAR07 table...
			Response answer = QueryUtil.getQuery(
					conn,
					new UserSessionParameters(username),
					sql,
					values,
					attribute2dbField,
					InventoryItemVO.class,
					"Y",
					"N",
					null,
					gridParams,
					blockSize,
					true
			);
			if (answer.isError())
				throw new Exception(answer.getErrorMessage());
			VOListResponse res = (VOListResponse)answer;
			List rows = res.getRows();
			String descr = null;
			InventoryItemVO vo = null;
			for(int i=0;i<rows.size();i++) {
				vo = (InventoryItemVO)rows.get(i);
				descr = vo.getDescriptionSYS10();

				// check supported variants for current item...
				if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm11())) {
					descr += " "+getVariantCodeAndTypeDesc(
						variant1Descriptions,
						vo,
						vo.getVariantTypeItm06(),
						vo.getVariantCodeItm11(),
						serverLanguageId,
						username
					);
				}
				if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm12())) {
					descr += " "+getVariantCodeAndTypeDesc(
						variant2Descriptions,
						vo,
						vo.getVariantTypeItm07(),
						vo.getVariantCodeItm12(),
						serverLanguageId,
						username
					);
				}
				if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm13())) {
					descr += " "+getVariantCodeAndTypeDesc(
						variant3Descriptions,
						vo,
						vo.getVariantTypeItm08(),
						vo.getVariantCodeItm13(),
						serverLanguageId,
						username
					);
				}
				if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm14())) {
					descr += " "+getVariantCodeAndTypeDesc(
						variant4Descriptions,
						vo,
						vo.getVariantTypeItm09(),
						vo.getVariantCodeItm14(),
						serverLanguageId,
						username
					);
				}
				if (!ApplicationConsts.JOLLY.equals(vo.getVariantCodeItm15())) {
					descr += " "+getVariantCodeAndTypeDesc(
						variant5Descriptions,
						vo,
						vo.getVariantTypeItm10(),
						vo.getVariantCodeItm15(),
						serverLanguageId,
						username
					);
				}
				vo.setDescriptionSYS10(descr);

			} // end for on rows...


			return res;
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching inventories list",ex);
			throw new Exception(ex.getMessage());
		}
		finally {
			try {
				if (this.conn == null && conn != null) {
					// close only local connection
					conn.commit();
					conn.close();
				}

			}
			catch (Exception exx) {}
		}
	}


	private String getVariantCodeAndTypeDesc(
			HashMap variantDescriptions,
			InventoryItemVO vo,
			String varType,
			String varCode,
			String serverLanguageId,
			String username
	) throws Throwable {
		String varDescr = (String)variantDescriptions.get(varType+"_"+varCode);
		if (varDescr==null)
			varDescr = ApplicationConsts.JOLLY.equals(varCode)?"":varCode;
		return varDescr;
	}



	public VOListResponse updateInventoryItems(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username) throws Throwable {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;

			InventoryItemVO oldVO = null;
			InventoryItemVO newVO = null;
			Response res = null;

			HashSet pkAttrs = new HashSet();
			pkAttrs.add("companyCodeSys01WAR07");
			pkAttrs.add("progressiveWar06WAR07");
			pkAttrs.add("itemCodeItm01WAR07");
			pkAttrs.add("variantTypeItm06");
			pkAttrs.add("variantTypeItm07");
			pkAttrs.add("variantTypeItm08");
			pkAttrs.add("variantTypeItm09");
			pkAttrs.add("variantTypeItm10");
			pkAttrs.add("variantCodeItm11");
			pkAttrs.add("variantCodeItm12");
			pkAttrs.add("variantCodeItm13");
			pkAttrs.add("variantCodeItm14");
			pkAttrs.add("variantCodeItm15");
			pkAttrs.add("progressiveHie01WAR07");

			HashMap attribute2dbField = new HashMap();
			attribute2dbField.put("stateWAR07","STATE");
			attribute2dbField.put("realProgressiveHie01WAR07","REAL_PROGRESSIVE_HIE01");
			attribute2dbField.put("qtyWAR07","QTY");
			attribute2dbField.put("realQtyWAR07","REAL_QTY");
			attribute2dbField.put("deltaQtyWAR07","DELTA_QTY");
			attribute2dbField.put("companyCodeSys01WAR07","COMPANY_CODE_SYS01");
			attribute2dbField.put("progressiveWar06WAR07","PROGRESSIVE_WAR06");
			attribute2dbField.put("itemCodeItm01WAR07","ITEM_CODE_ITM01");
			attribute2dbField.put("variantTypeItm06","VARIANT_TYPE_ITM06");
			attribute2dbField.put("variantTypeItm07","VARIANT_TYPE_ITM07");
			attribute2dbField.put("variantTypeItm08","VARIANT_TYPE_ITM08");
			attribute2dbField.put("variantTypeItm09","VARIANT_TYPE_ITM09");
			attribute2dbField.put("variantTypeItm10","VARIANT_TYPE_ITM10");
			attribute2dbField.put("variantCodeItm11","VARIANT_CODE_ITM11");
			attribute2dbField.put("variantCodeItm12","VARIANT_CODE_ITM12");
			attribute2dbField.put("variantCodeItm13","VARIANT_CODE_ITM13");
			attribute2dbField.put("variantCodeItm14","VARIANT_CODE_ITM14");
			attribute2dbField.put("variantCodeItm15","VARIANT_CODE_ITM15");
			attribute2dbField.put("progressiveHie01WAR07","PROGRESSIVE_HIE01");

			for(int i=0;i<oldVOs.size();i++) {
				oldVO = (InventoryItemVO)oldVOs.get(i);
				newVO = (InventoryItemVO)newVOs.get(i);

				if (newVO.getQtyWAR07()!=null && newVO.getRealQtyWAR07()!=null)
					newVO.setDeltaQtyWAR07(newVO.getRealQtyWAR07().subtract(newVO.getQtyWAR07()));

//				// check if the same item has been already inserted in the same location...
//				String sql =
//					"select * from WAR07_INVENTORY_ITEMS " +
//					"where "+
//					"COMPANY_CODE_SYS01=? and "+
//					"PROGRESSIVE_WAR06=? and "+
//					"ITEM_CODE_ITM01=? and "+
//					"VARIANT_TYPE_ITM06=? and "+
//					"VARIANT_TYPE_ITM07=? and "+
//					"VARIANT_TYPE_ITM08=? and "+
//					"VARIANT_TYPE_ITM09=? and "+
//					"VARIANT_TYPE_ITM10=? and "+
//					"VARIANT_CODE_ITM11=? and "+
//					"VARIANT_CODE_ITM12=? and "+
//					"VARIANT_CODE_ITM13=? and "+
//					"VARIANT_CODE_ITM14=? and "+
//					"VARIANT_CODE_ITM15=? and "+
//					"PROGRESSIVE_HIE01=? and "+
//				  "NOT PROGRESSIVE=?  ";
//
//				pstmt = conn.prepareStatement(sql);
//				pstmt.setString(1, newVO.getCompanyCodeSys01WAR07());
//				pstmt.setBigDecimal(2, newVO.getProgressiveWar06WAR07());
//				pstmt.setString(3, newVO.getItemCodeItm01WAR07());
//				pstmt.setString(4, newVO.getVariantTypeItm06());
//				pstmt.setString(5, newVO.getVariantTypeItm07());
//				pstmt.setString(6, newVO.getVariantTypeItm08());
//				pstmt.setString(7, newVO.getVariantTypeItm09());
//				pstmt.setString(8, newVO.getVariantTypeItm10());
//				pstmt.setString(9,  newVO.getVariantCodeItm11());
//				pstmt.setString(10, newVO.getVariantCodeItm12());
//				pstmt.setString(11, newVO.getVariantCodeItm13());
//				pstmt.setString(12, newVO.getVariantCodeItm14());
//				pstmt.setString(13, newVO.getVariantCodeItm15());
//				pstmt.setBigDecimal(14, newVO.getProgressiveHie01WAR07());
//				pstmt.setBigDecimal(15, newVO.getProgressiveWAR07());
//				ResultSet rset = pstmt.executeQuery();
//				boolean found = false;
//				if (rset.next())
//					found = true;
//				rset.close();
//				pstmt.close();
//				if (found) {
//					throw new Exception("cannot change item's location, since the same location is already used");
//				}
//
//
//				// check if the same item has been already inserted in the same REAL location...
//				sql =
//					"select * from WAR07_INVENTORY_ITEMS " +
//					"where "+
//					"COMPANY_CODE_SYS01=? and "+
//					"PROGRESSIVE_WAR06=? and "+
//					"ITEM_CODE_ITM01=? and "+
//					"VARIANT_TYPE_ITM06=? and "+
//					"VARIANT_TYPE_ITM07=? and "+
//					"VARIANT_TYPE_ITM08=? and "+
//					"VARIANT_TYPE_ITM09=? and "+
//					"VARIANT_TYPE_ITM10=? and "+
//					"VARIANT_CODE_ITM11=? and "+
//					"VARIANT_CODE_ITM12=? and "+
//					"VARIANT_CODE_ITM13=? and "+
//					"VARIANT_CODE_ITM14=? and "+
//					"VARIANT_CODE_ITM15=? and "+
//					"REAL_PROGRESSIVE_HIE01=? and "+
//				  "NOT PROGRESSIVE=? ";
//
//				pstmt = conn.prepareStatement(sql);
//				pstmt.setString(1, newVO.getCompanyCodeSys01WAR07());
//				pstmt.setBigDecimal(2, newVO.getProgressiveWar06WAR07());
//				pstmt.setString(3, newVO.getItemCodeItm01WAR07());
//				pstmt.setString(4, newVO.getVariantTypeItm06());
//				pstmt.setString(5, newVO.getVariantTypeItm07());
//				pstmt.setString(6, newVO.getVariantTypeItm08());
//				pstmt.setString(7, newVO.getVariantTypeItm09());
//				pstmt.setString(8, newVO.getVariantTypeItm10());
//				pstmt.setString(9,  newVO.getVariantCodeItm11());
//				pstmt.setString(10, newVO.getVariantCodeItm12());
//				pstmt.setString(11, newVO.getVariantCodeItm13());
//				pstmt.setString(12, newVO.getVariantCodeItm14());
//				pstmt.setString(13, newVO.getVariantCodeItm15());
//				pstmt.setBigDecimal(14, newVO.getRealProgressiveHie01WAR07());
//				pstmt.setBigDecimal(15, newVO.getProgressiveWAR07());
//				rset = pstmt.executeQuery();
//				found = false;
//				if (rset.next())
//					found = true;
//				rset.close();
//				pstmt.close();
//				if (found) {
//					throw new Exception("cannot change item's real location, since the same real location is already used");
//				}




				res = new QueryUtil().updateTable(
						conn,
						new UserSessionParameters(username),
						pkAttrs,
						oldVO,
						newVO,
						"WAR07_INVENTORY_ITEMS",
						attribute2dbField,
						"Y",
						"N",
						null,
						true
				);
				if (res.isError()) {
					throw new Exception(res.getErrorMessage());
				}



				pstmt = conn.prepareStatement(
					"insert into WAR08_INVENTORY_S_N("+
					"SERIAL_NUMBER,"+
					"COMPANY_CODE_SYS01," +
					"PROGRESSIVE_WAR06," +
					"ITEM_CODE_ITM01," +
					"VARIANT_TYPE_ITM06," +
					"VARIANT_TYPE_ITM07," +
					"VARIANT_TYPE_ITM08," +
					"VARIANT_TYPE_ITM09," +
					"VARIANT_TYPE_ITM10," +
					"VARIANT_CODE_ITM11," +
					"VARIANT_CODE_ITM12," +
					"VARIANT_CODE_ITM13," +
					"VARIANT_CODE_ITM14," +
					"VARIANT_CODE_ITM15," +
					"PROGRESSIVE_HIE01) "+
					"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
				);
				for(int j=0;j<newVO.getSerialNumbers().size();j++) {
					pstmt.setString(1,newVO.getSerialNumbers().get(j).toString());
					pstmt.setString(2, newVO.getCompanyCodeSys01WAR07());
					pstmt.setBigDecimal(3, newVO.getProgressiveWar06WAR07());
					pstmt.setString(4, newVO.getItemCodeItm01WAR07());
					pstmt.setString(5, newVO.getVariantTypeItm06());
					pstmt.setString(6, newVO.getVariantTypeItm07());
					pstmt.setString(7, newVO.getVariantTypeItm08());
					pstmt.setString(8, newVO.getVariantTypeItm09());
					pstmt.setString(9, newVO.getVariantTypeItm10());
					pstmt.setString(10,  newVO.getVariantCodeItm11());
					pstmt.setString(11, newVO.getVariantCodeItm12());
					pstmt.setString(12, newVO.getVariantCodeItm13());
					pstmt.setString(13, newVO.getVariantCodeItm14());
					pstmt.setString(14, newVO.getVariantCodeItm15());
					pstmt.setBigDecimal(15, newVO.getProgressiveHie01WAR07());
					pstmt.execute();
				}
				pstmt.close();


			} // end for

			return new VOListResponse(newVOs,false,newVOs.size());
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing inventory items",ex);
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


	public VOResponse deleteInventoryItems(ArrayList list,String serverLanguageId,String username) throws Throwable {
		Statement stmt = null;
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;
			stmt = conn.createStatement();

			InventoryItemVO vo = null;
			for(int i=0;i<list.size();i++) {
				vo = (InventoryItemVO)list.get(i);

				// phisically  delete records in WAR07...
				pstmt = conn.prepareStatement(
				   "delete from WAR08_INVENTORY_S_N where "+
					 "COMPANY_CODE_SYS01=? and " +
					 "PROGRESSIVE_WAR06=? and " +
					 "ITEM_CODE_ITM01=? and " +
					 "VARIANT_TYPE_ITM06=? and " +
					 "VARIANT_TYPE_ITM07=? and " +
					 "VARIANT_TYPE_ITM08=? and " +
					 "VARIANT_TYPE_ITM09=? and " +
					 "VARIANT_TYPE_ITM10=? and " +
					 "VARIANT_CODE_ITM11=? and " +
					 "VARIANT_CODE_ITM12=? and " +
					 "VARIANT_CODE_ITM13=? and " +
					 "VARIANT_CODE_ITM14=? and " +
					 "VARIANT_CODE_ITM15=? and " +
					 "PROGRESSIVE_HIE01=? "
				);
				pstmt.setString(1, vo.getCompanyCodeSys01WAR07());
				pstmt.setBigDecimal(2, vo.getProgressiveWar06WAR07());
				pstmt.setString(3, vo.getItemCodeItm01WAR07());
				pstmt.setString(4, vo.getVariantTypeItm06());
				pstmt.setString(5, vo.getVariantTypeItm07());
				pstmt.setString(6, vo.getVariantTypeItm08());
				pstmt.setString(7, vo.getVariantTypeItm09());
				pstmt.setString(8, vo.getVariantTypeItm10());
				pstmt.setString(9, vo.getVariantCodeItm11());
				pstmt.setString(10, vo.getVariantCodeItm12());
				pstmt.setString(11, vo.getVariantCodeItm13());
				pstmt.setString(12, vo.getVariantCodeItm14());
				pstmt.setString(13, vo.getVariantCodeItm15());
				pstmt.setBigDecimal(14, vo.getProgressiveHie01WAR07());
				pstmt.execute();
				pstmt.close();

				// phisically delete the record in WAR07...
				pstmt = conn.prepareStatement(
					"delete from WAR07_INVENTORY_ITEMS where "+
					"COMPANY_CODE_SYS01=? and " +
					"PROGRESSIVE_WAR06=? and " +
					"ITEM_CODE_ITM01=? and " +
					"VARIANT_TYPE_ITM06=? and " +
					"VARIANT_TYPE_ITM07=? and " +
					"VARIANT_TYPE_ITM08=? and " +
					"VARIANT_TYPE_ITM09=? and " +
					"VARIANT_TYPE_ITM10=? and " +
					"VARIANT_CODE_ITM11=? and " +
					"VARIANT_CODE_ITM12=? and " +
					"VARIANT_CODE_ITM13=? and " +
					"VARIANT_CODE_ITM14=? and " +
					"VARIANT_CODE_ITM15=? and " +
					"PROGRESSIVE_HIE01=? "
				);
				pstmt.setString(1, vo.getCompanyCodeSys01WAR07());
				pstmt.setBigDecimal(2, vo.getProgressiveWar06WAR07());
				pstmt.setString(3, vo.getItemCodeItm01WAR07());
				pstmt.setString(4, vo.getVariantTypeItm06());
				pstmt.setString(5, vo.getVariantTypeItm07());
				pstmt.setString(6, vo.getVariantTypeItm08());
				pstmt.setString(7, vo.getVariantTypeItm09());
				pstmt.setString(8, vo.getVariantTypeItm10());
				pstmt.setString(9, vo.getVariantCodeItm11());
				pstmt.setString(10, vo.getVariantCodeItm12());
				pstmt.setString(11, vo.getVariantCodeItm13());
				pstmt.setString(12, vo.getVariantCodeItm14());
				pstmt.setString(13, vo.getVariantCodeItm15());
				pstmt.setBigDecimal(14, vo.getProgressiveHie01WAR07());
				pstmt.execute();
				pstmt.close();

			}

			return new VOResponse(new Boolean(true));
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing inventory items",ex);
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


	public VOResponse closeInventory(
			 String companyCodeSys01,
			 String warehouseCodeWAR01,
			 BigDecimal progressiveWAR06,
			 String itemType,
			 String note,
			 String serverLanguageId,
			 String username
	) throws Throwable {
		 PreparedStatement pstmt = null;
		 PreparedStatement pstmt2 = null;
		 PreparedStatement pstmt3 = null;
		 PreparedStatement pstmt4 = null;
		 Connection conn = null;
		 try {
			 if (this.conn==null) conn = getConn(); else conn = this.conn;

	     // retrieve mov. motives...
			String sql = "SELECT VALUE from SYS11_APPLICATION_PARS WHERE PARAM_CODE=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, itemType.equals(ApplicationConsts.ITEM_GOOD)?ApplicationConsts.NEG_INVCORR_GOOD_ITM:ApplicationConsts.NEG_INVCORR_DAMG_ITM);
			ResultSet rset = pstmt.executeQuery();
			String negWarehouseMotiveWar04 = null;
			if (rset.next())
				negWarehouseMotiveWar04 = rset.getString(1);
			else {
				rset.close();
				pstmt.close();
				throw new Exception("movement motive related to inventory correction not found");
			}
			rset.close();
			pstmt.close();

			sql = "SELECT VALUE from SYS11_APPLICATION_PARS WHERE PARAM_CODE=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, itemType.equals(ApplicationConsts.ITEM_GOOD)?ApplicationConsts.POS_INVCORR_GOOD_ITM:ApplicationConsts.POS_INVCORR_DAMG_ITM);
			rset = pstmt.executeQuery();
			String posWarehouseMotiveWar04 = null;
			if (rset.next())
				posWarehouseMotiveWar04 = rset.getString(1);
			else {
				rset.close();
				pstmt.close();
				throw new Exception("movement motive related to inventory correction not found");
			}
			rset.close();
			pstmt.close();



      // change state to in progress...
			 sql =
			 	 "update WAR06_INVENTORIES set STATE=? " +
			 	 "where COMPANY_CODE_SYS01=? and PROGRESSIVE=? and STATE=? ";
			 pstmt = conn.prepareStatement(sql);
			 pstmt.setString(1, ApplicationConsts.IN_PROGRESS);
			 pstmt.setString(2, companyCodeSys01);
			 pstmt.setBigDecimal(3, progressiveWAR06);
			 pstmt.setString(4, ApplicationConsts.CONFIRMED);
			 int rows = pstmt.executeUpdate();
			 pstmt.close();
			 if (rows==0)
		  		throw new Exception("cannot confirm the inventory");

      // retrieve current progressive in WAR02...
			 pstmt = conn.prepareStatement(
					 "select SYS20_COMPANY_PROGRESSIVES.VALUE,SYS21_COMPANY_PARAMS.VALUE,SYS11_APPLICATION_PARS.VALUE "+
					 "from SYS21_COMPANY_PARAMS,SYS11_APPLICATION_PARS LEFT OUTER JOIN SYS20_COMPANY_PROGRESSIVES "+
					 "ON SYS20_COMPANY_PROGRESSIVES.COMPANY_CODE_SYS01=? and "+
					 "   SYS20_COMPANY_PROGRESSIVES.TABLE_NAME='WAR02_WAREHOUSE_MOVEMENTS' and "+
					 "   SYS20_COMPANY_PROGRESSIVES.COLUMN_NAME='PROGRESSIVE' "+
					 "WHERE SYS11_APPLICATION_PARS.PARAM_CODE='INCREMENT_VALUE' and "+
					 "      SYS21_COMPANY_PARAMS.COMPANY_CODE_SYS01=? and "+
					 "      SYS21_COMPANY_PARAMS.PARAM_CODE='INITIAL_VALUE' "
			 );
			 pstmt.setString(1,companyCodeSys01);
			 pstmt.setString(2,companyCodeSys01);
			 rset = pstmt.executeQuery();
			 rset.next();
			 BigDecimal progressive = rset.getBigDecimal(1);
			 BigDecimal initialValue = rset.getBigDecimal(2);
			 BigDecimal incrementValue = rset.getBigDecimal(3);
			 rset.close();
			 pstmt.close();


       // retrieve rows to insert...
			 String sqlWAR07 = "select "+
						 "COMPANY_CODE_SYS01, "+
						 "ITEM_CODE_ITM01,    "+
						 "VARIANT_TYPE_ITM06, "+
						 "VARIANT_CODE_ITM11, "+
						 "VARIANT_TYPE_ITM07, "+
						 "VARIANT_CODE_ITM12, "+
						 "VARIANT_TYPE_ITM08, "+
						 "VARIANT_CODE_ITM13, "+
						 "VARIANT_TYPE_ITM09, "+
						 "VARIANT_CODE_ITM14, "+
						 "VARIANT_TYPE_ITM10, "+
						 "VARIANT_CODE_ITM15, "+
						 "DELTA_QTY,"+
						 "PROGRESSIVE_HIE01,"+
						 "STATE "+
						 "from WAR07_INVENTORY_ITEMS where "+
						 "COMPANY_CODE_SYS01=? and "+
						 "PROGRESSIVE_WAR06=? and  "+
						 "DELTA_QTY is not null and "+
						 "not DELTA_QTY=0 and "+
						 "STATE=?";
//		 	  pstmt = conn.prepareStatement(sqlWAR07,ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
		 	  pstmt = conn.prepareStatement(sqlWAR07);
				pstmt.setString(1,companyCodeSys01);
				pstmt.setBigDecimal(2,progressiveWAR06);
				pstmt.setString(3,ApplicationConsts.CONFIRMED);
	      rset = pstmt.executeQuery();
				rset.last();

	      // retrieve number of rows to insert...
				rows = rset.getRow();

	      // update current progressive in WAR02..
      	if (progressive!=null) {
					// record found: it will be incremented by 10...
					pstmt2 = conn.prepareStatement(
							"update SYS20_COMPANY_PROGRESSIVES set VALUE=VALUE+"+(incrementValue.longValue()*rows)+" where "+
							"COMPANY_CODE_SYS01=? and TABLE_NAME='WAR02_WAREHOUSE_MOVEMENTS' and COLUMN_NAME='PROGRESSIVE' and VALUE="+progressive
					);
					pstmt2.setString(1,companyCodeSys01);
					rows = pstmt2.executeUpdate();
					pstmt2.close();
					if (rows==0)
						throw new Exception("Updating not performed: the record was previously updated.");

					progressive = new BigDecimal(progressive.intValue()+incrementValue.intValue());
				}
				else
					throw new Exception("internal error on calculating progressive in WAR02");

				conn.commit();

				String sqlWAR02 =
							"insert into WAR02_WAREHOUSE_MOVEMENTS("+
							"PROGRESSIVE,"+
							"COMPANY_CODE_SYS01,"+
							"WAREHOUSE_CODE_WAR01,"+
							"ITEM_CODE_ITM01,"+
							"VARIANT_TYPE_ITM06,VARIANT_CODE_ITM11,"+
							"VARIANT_TYPE_ITM07,VARIANT_CODE_ITM12,"+
							"VARIANT_TYPE_ITM08,VARIANT_CODE_ITM13,"+
							"VARIANT_TYPE_ITM09,VARIANT_CODE_ITM14,"+
							"VARIANT_TYPE_ITM10,VARIANT_CODE_ITM15, "+
							"DELTA_QTY,WAREHOUSE_MOTIVE_WAR04,PROGRESSIVE_HIE01,MOVEMENT_DATE,USERNAME,NOTE"+
							") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				pstmt2 = conn.prepareStatement(sqlWAR02);

				pstmt3 = conn.prepareStatement(
				    "update WAR03_ITEMS_AVAILABILITY set "+(itemType.equals(ApplicationConsts.ITEM_GOOD)?"AVAILABLE_QTY=AVAILABLE_QTY+? ":"DAMAGED_QTY=DAMAGED_QTY+? ")+
						"where COMPANY_CODE_SYS01=? and WAREHOUSE_CODE_WAR01=? and PROGRESSIVE_HIE01=? and ITEM_CODE_ITM01=? and "+
						"VARIANT_TYPE_ITM06=? and VARIANT_CODE_ITM11=? and "+
						"VARIANT_TYPE_ITM07=? and VARIANT_CODE_ITM12=? and "+
						"VARIANT_TYPE_ITM08=? and VARIANT_CODE_ITM13=? and "+
						"VARIANT_TYPE_ITM09=? and VARIANT_CODE_ITM14=? and "+
						"VARIANT_TYPE_ITM10=? and VARIANT_CODE_ITM15=? "
				);

        pstmt4 = conn.prepareStatement(
 		        "insert into WAR03_ITEMS_AVAILABILITY(COMPANY_CODE_SYS01,ITEM_CODE_ITM01,PROGRESSIVE_HIE01,WAREHOUSE_CODE_WAR01,"+
						"AVAILABLE_QTY,DAMAGED_QTY,"+
						"VARIANT_TYPE_ITM06,VARIANT_CODE_ITM11,"+
						"VARIANT_TYPE_ITM07,VARIANT_CODE_ITM12,"+
						"VARIANT_TYPE_ITM08,VARIANT_CODE_ITM13,"+
						"VARIANT_TYPE_ITM09,VARIANT_CODE_ITM14,"+
						"VARIANT_TYPE_ITM10,VARIANT_CODE_ITM15 "+
						") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
			  );


				// insert rows in WAR02...
				rset.beforeFirst();
				BigDecimal deltaQty = null;
				long count = 0;
				while(rset.next()) {
					pstmt2.setLong(1,progressive.longValue()+incrementValue.longValue());
					pstmt2.setString(2,rset.getString(1));
					pstmt2.setString(3,warehouseCodeWAR01);
					pstmt2.setString(4,rset.getString(2)); // item code

					pstmt2.setString(5,rset.getString(3));
					pstmt2.setString(6,rset.getString(4));
					pstmt2.setString(7,rset.getString(5));
					pstmt2.setString(8,rset.getString(6));
					pstmt2.setString(9,rset.getString(7));
					pstmt2.setString(10,rset.getString(8));
					pstmt2.setString(11,rset.getString(9));
					pstmt2.setString(12,rset.getString(10));
					pstmt2.setString(13,rset.getString(11));
					pstmt2.setString(14,rset.getString(12));

	        deltaQty = rset.getBigDecimal(13);
	        pstmt2.setBigDecimal(15,deltaQty);
					pstmt2.setString(16,deltaQty.longValue()<0?negWarehouseMotiveWar04:posWarehouseMotiveWar04);
					pstmt2.setBigDecimal(17,rset.getBigDecimal(14)); // progressiveHIE01
					pstmt2.setTimestamp(18,new java.sql.Timestamp(System.currentTimeMillis()));
					pstmt2.setString(19,username);
					pstmt2.setString(20,note);
					pstmt2.addBatch();

					// update WAR03...
					pstmt3.setBigDecimal(1,deltaQty);
					pstmt3.setString(2,companyCodeSys01);
					pstmt3.setString(3,warehouseCodeWAR01);
					pstmt3.setBigDecimal(4,rset.getBigDecimal(14)); // progressiveHIE01
					pstmt3.setString(5,rset.getString(2)); // item code

					pstmt3.setString(6,rset.getString(3));
					pstmt3.setString(7,rset.getString(4));
					pstmt3.setString(8,rset.getString(5));
					pstmt3.setString(9,rset.getString(6));
					pstmt3.setString(10,rset.getString(7));
					pstmt3.setString(11,rset.getString(8));
					pstmt3.setString(12,rset.getString(9));
					pstmt3.setString(13,rset.getString(10));
					pstmt3.setString(14,rset.getString(11));
					pstmt3.setString(15,rset.getString(12));
					rows = pstmt3.executeUpdate();
					if (rows==0) {
						// insert into WAR03...

						pstmt4.setString(1,companyCodeSys01);
						pstmt4.setString(2,rset.getString(2)); // item code
						pstmt4.setBigDecimal(3,rset.getBigDecimal(14)); // progressiveHIE01
						pstmt4.setString(4,warehouseCodeWAR01);
						if (itemType.equals(ApplicationConsts.ITEM_GOOD)) {
							pstmt4.setBigDecimal(5,deltaQty);
							pstmt4.setBigDecimal(6,new BigDecimal(0));
						}
						else if (itemType.equals(ApplicationConsts.ITEM_DAMAGED)) {
							pstmt4.setBigDecimal(5,new BigDecimal(0));
							pstmt4.setBigDecimal(6,deltaQty);
						}

						pstmt4.setString(7,rset.getString(3));
						pstmt4.setString(8,rset.getString(4));
						pstmt4.setString(9,rset.getString(5));
						pstmt4.setString(10,rset.getString(6));
						pstmt4.setString(11,rset.getString(7));
						pstmt4.setString(12,rset.getString(8));
						pstmt4.setString(13,rset.getString(9));
						pstmt4.setString(14,rset.getString(10));
						pstmt4.setString(15,rset.getString(11));
						pstmt4.setString(16,rset.getString(12));

						pstmt4.execute();

					}



					progressive.add(incrementValue);
					count++;
					if (count>1000) {
						count = 0;
						pstmt2.executeBatch();
						conn.commit();
					}
				}

				pstmt2.executeBatch();
				conn.commit();

			  rset.close();
			  pstmt.close();
				pstmt2.close();



				pstmt = conn.prepareStatement(
				  "update WAR07_INVENTORY_ITEMS set STATE=? where "+
					 "COMPANY_CODE_SYS01=? and "+
					 "PROGRESSIVE_WAR06=? and  "+
					 "DELTA_QTY is not null and "+
					 "not DELTA_QTY=0 and "+
					 "STATE=?"
				);
				pstmt.setString(1,ApplicationConsts.CLOSED);
			  pstmt.setString(2,companyCodeSys01);
			  pstmt.setBigDecimal(3,progressiveWAR06);
				pstmt.setString(4,ApplicationConsts.CONFIRMED);
			  pstmt.execute();
				pstmt.close();
			  conn.commit();


				// add serial numbers...
				pstmt = conn.prepareStatement(
						"insert into WAR05_STORED_SERIAL_NUMBERS("+
						"SERIAL_NUMBER,COMPANY_CODE_SYS01,ITEM_CODE_ITM01,PROGRESSIVE_HIE01,"+
						"VARIANT_TYPE_ITM06,VARIANT_CODE_ITM11,"+
						"VARIANT_TYPE_ITM07,VARIANT_CODE_ITM12,"+
						"VARIANT_TYPE_ITM08,VARIANT_CODE_ITM13,"+
						"VARIANT_TYPE_ITM09,VARIANT_CODE_ITM14,"+
						"VARIANT_TYPE_ITM10,VARIANT_CODE_ITM15) "+
						"select "+
						"SERIAL_NUMBER,COMPANY_CODE_SYS01,ITEM_CODE_ITM01,PROGRESSIVE_HIE01,"+
						"VARIANT_TYPE_ITM06,VARIANT_CODE_ITM11,"+
						"VARIANT_TYPE_ITM07,VARIANT_CODE_ITM12,"+
						"VARIANT_TYPE_ITM08,VARIANT_CODE_ITM13,"+
						"VARIANT_TYPE_ITM09,VARIANT_CODE_ITM14,"+
						"VARIANT_TYPE_ITM10,VARIANT_CODE_ITM15 "+
						"from WAR08_INVENTORY_S_N where "+
						"COMPANY_CODE_SYS01=? and "+
						"PROGRESSIVE_WAR06=? and not exists "+
						"(select * from WAR05_STORED_SERIAL_NUMBERS where "+
						" WAR05_STORED_SERIAL_NUMBERS.COMPANY_CODE_SYS01=WAR08_INVENTORY_S_N.COMPANY_CODE_SYS01 and "+
						" WAR05_STORED_SERIAL_NUMBERS.ITEM_CODE_ITM01=WAR08_INVENTORY_S_N.ITEM_CODE_ITM01 and "+
						" WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM06=WAR08_INVENTORY_S_N.VARIANT_TYPE_ITM06 and "+
						" WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM07=WAR08_INVENTORY_S_N.VARIANT_TYPE_ITM07 and "+
						" WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM08=WAR08_INVENTORY_S_N.VARIANT_TYPE_ITM08 and "+
						" WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM09=WAR08_INVENTORY_S_N.VARIANT_TYPE_ITM09 and "+
						" WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM10=WAR08_INVENTORY_S_N.VARIANT_TYPE_ITM10 and "+
						" WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM11=WAR08_INVENTORY_S_N.VARIANT_CODE_ITM11 and "+
						" WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM12=WAR08_INVENTORY_S_N.VARIANT_CODE_ITM12 and "+
						" WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM13=WAR08_INVENTORY_S_N.VARIANT_CODE_ITM13 and "+
						" WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM14=WAR08_INVENTORY_S_N.VARIANT_CODE_ITM14 and "+
						" WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM15=WAR08_INVENTORY_S_N.VARIANT_CODE_ITM15 and "+
 					  " WAR05_STORED_SERIAL_NUMBERS.PROGRESSIVE_HIE01=WAR08_INVENTORY_S_N.PROGRESSIVE_HIE01 "+
						")"
				);
				pstmt.setString(1,companyCodeSys01);
				pstmt.setBigDecimal(2,progressiveWAR06);


			 // remove serial numbers: phisically delete records in WAR07...
			 pstmt = conn.prepareStatement(
				 "delete from WAR08_INVENTORY_S_N where PROGRESSIVE_WAR06=?"
			 );
			 pstmt.setBigDecimal(1,progressiveWAR06);
			 pstmt.execute();

	     // change state to close...
			 sql =
				 "update WAR06_INVENTORIES set STATE=?,END_DATE=? " +
				 "where COMPANY_CODE_SYS01=? and PROGRESSIVE=? ";
			 pstmt = conn.prepareStatement(sql);
			 pstmt.setString(1, ApplicationConsts.CLOSED);
			 pstmt.setDate(2, new java.sql.Date(System.currentTimeMillis()));
			 pstmt.setString(3, companyCodeSys01);
			 pstmt.setBigDecimal(4, progressiveWAR06);
			 pstmt.execute();
			 pstmt.close();

			 sql =
				 "update WAR07_INVENTORY_ITEMS set STATE=? " +
				 "where COMPANY_CODE_SYS01=? and PROGRESSIVE_WAR06=? ";
			 pstmt = conn.prepareStatement(sql);
			 pstmt.setString(1, ApplicationConsts.CLOSED);
			 pstmt.setString(2, companyCodeSys01);
			 pstmt.setBigDecimal(3, progressiveWAR06);
			 pstmt.executeUpdate();
			 pstmt.close();

			 conn.commit();

			 return new VOResponse(new Boolean(true));
		 }
		 catch (Throwable ex) {
			 Logger.error(username,this.getClass().getName(),"executeCommand","Error while closing an inventory",ex);

			 try {
				 if (this.conn==null && conn!=null)
					 // rollback only local connection
					 conn.rollback();
			 }
			 catch (Exception ex3) {
			 }

			 try {
				 // change state to in progress...
					String sql =
						 "update WAR06_INVENTORIES set STATE=? " +
						 "where COMPANY_CODE_SYS01=? and PROGRESSIVE=? ";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, ApplicationConsts.CONFIRMED);
					pstmt.setString(2, companyCodeSys01);
					pstmt.setBigDecimal(3, progressiveWAR06);
					pstmt.executeUpdate();
					pstmt.close();
					conn.commit();
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
				 pstmt3.close();
			 }
			 catch (Exception ex2) {
			 }
			 try {
				 pstmt4.close();
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


	 public VOResponse confirmInventory(
			String companyCodeSys01,
			String warehouseCodeWAR01,
			BigDecimal progressiveWAR06,
			String serverLanguageId,
			String username
	 ) throws Throwable {
		 PreparedStatement pstmt = null;
		 Connection conn = null;
		 try {
			 if (this.conn==null) conn = getConn(); else conn = this.conn;

			 String sql =
				 "update WAR06_INVENTORIES set STATE=? " +
				 "where COMPANY_CODE_SYS01=? and PROGRESSIVE=? and STATE=? ";
			 pstmt = conn.prepareStatement(sql);
			 pstmt.setString(1, ApplicationConsts.CONFIRMED);
			 pstmt.setString(2, companyCodeSys01);
			 pstmt.setBigDecimal(3, progressiveWAR06);
			 pstmt.setString(4, ApplicationConsts.OPENED);
			 int rows = pstmt.executeUpdate();
			 pstmt.close();
			 if (rows==0)
				 throw new Exception("cannot confirm the inventory");

			 sql =
				 "update WAR07_INVENTORY_ITEMS set STATE=? " +
				 "where COMPANY_CODE_SYS01=? and PROGRESSIVE_WAR06=? ";
			 pstmt = conn.prepareStatement(sql);
			 pstmt.setString(1, ApplicationConsts.CONFIRMED);
			 pstmt.setString(2, companyCodeSys01);
			 pstmt.setBigDecimal(3, progressiveWAR06);
			 pstmt.executeUpdate();
			 pstmt.close();

			 return new VOResponse(new Boolean(true));
		 }
		 catch (Throwable ex) {
			 Logger.error(username,this.getClass().getName(),"executeCommand","Error while confirming an inventory",ex);
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



	public VOResponse updateInventoryItem(
		 String companyCodeSys01,
		 String warehouseCodeWAR01,
		 BigDecimal progressiveWAR06,
		 BigDecimal progressiveHIE02,
		 String itemCodeItm01WAR07,
		 BigDecimal realProgressiveHie01WAR07, // warehouse's real location
		 BigDecimal realQtyWAR07, // warehouse's real location TO ADD
		 String variantTypeItm06,
		 String variantCodeItm11,
		 String variantTypeItm07,
		 String variantCodeItm12,
		 String variantTypeItm08,
		 String variantCodeItm13,
		 String variantTypeItm09,
		 String variantCodeItm14,
		 String variantTypeItm10,
		 String variantCodeItm15,
		 ArrayList serialNumbers,
		 String serverLanguageId,
		 String username
	) throws Throwable {
		 PreparedStatement pstmt = null;
		 Connection conn = null;
		 try {
			 if (this.conn==null) conn = getConn(); else conn = this.conn;

			 // check if the item already exists in the specified location:
			 // - if exists, then update qty (by adding qty)
			 // - if not exists, then insert the record in WAR07 (and WAR06 if needed)...
			 // re-calculate delta qty

			pstmt = conn.prepareStatement(
				"select REAL_QTY,QTY from WAR07_INVENTORY_ITEMS where "+
				"COMPANY_CODE_SYS01=? and " +
				"PROGRESSIVE_WAR06=? and " +
				"ITEM_CODE_ITM01=? and " +
				"VARIANT_TYPE_ITM06=? and " +
				"VARIANT_TYPE_ITM07=? and " +
				"VARIANT_TYPE_ITM08=? and " +
				"VARIANT_TYPE_ITM09=? and " +
				"VARIANT_TYPE_ITM10=? and " +
				"VARIANT_CODE_ITM11=? and " +
				"VARIANT_CODE_ITM12=? and " +
				"VARIANT_CODE_ITM13=? and " +
				"VARIANT_CODE_ITM14=? and " +
				"VARIANT_CODE_ITM15=? and " +
				"PROGRESSIVE_HIE01=? "
			);
			pstmt.setString(1, companyCodeSys01);
			pstmt.setBigDecimal(2, progressiveWAR06);
			pstmt.setString(3, itemCodeItm01WAR07);
			pstmt.setString(4, variantTypeItm06);
			pstmt.setString(5, variantTypeItm07);
			pstmt.setString(6, variantTypeItm08);
			pstmt.setString(7, variantTypeItm09);
			pstmt.setString(8, variantTypeItm10);
			pstmt.setString(9, variantCodeItm11);
			pstmt.setString(10, variantCodeItm12);
			pstmt.setString(11, variantCodeItm13);
			pstmt.setString(12, variantCodeItm14);
			pstmt.setString(13, variantCodeItm15);
			pstmt.setBigDecimal(14, realProgressiveHie01WAR07);
			ResultSet rset = pstmt.executeQuery();
			BigDecimal realQty = null;
			BigDecimal qty = null;
			boolean recordFound = false;
			if (rset.next()) {
				recordFound = true;
				realQty = rset.getBigDecimal(1);
				qty = rset.getBigDecimal(2);
			}
			rset.close();
			pstmt.close();

	    if (recordFound) {
				pstmt = conn.prepareStatement(
					"update WAR07_INVENTORY_ITEMS set "+
					"REAL_QTY=?, "+
					"DELTA_QTY=?, "+
					"REAL_PROGRESSIVE_HIE01=? "+
					"where "+
					"COMPANY_CODE_SYS01=? and " +
					"PROGRESSIVE_WAR06=? and " +
					"ITEM_CODE_ITM01=? and " +
					"VARIANT_TYPE_ITM06=? and " +
					"VARIANT_TYPE_ITM07=? and " +
					"VARIANT_TYPE_ITM08=? and " +
					"VARIANT_TYPE_ITM09=? and " +
					"VARIANT_TYPE_ITM10=? and " +
					"VARIANT_CODE_ITM11=? and " +
					"VARIANT_CODE_ITM12=? and " +
					"VARIANT_CODE_ITM13=? and " +
					"VARIANT_CODE_ITM14=? and " +
					"VARIANT_CODE_ITM15=? and " +
					"PROGRESSIVE_HIE01=? "
				);

       	realQty = (realQty==null?new BigDecimal(0):realQty).add(realQtyWAR07);
				pstmt.setBigDecimal(1, realQty);
				pstmt.setBigDecimal(2, realQty.subtract(qty==null?new BigDecimal(0):qty));
				pstmt.setBigDecimal(3, realProgressiveHie01WAR07);
				pstmt.setString(4, companyCodeSys01);
				pstmt.setBigDecimal(5, progressiveWAR06);
				pstmt.setString(6, itemCodeItm01WAR07);
				pstmt.setString(7, variantTypeItm06);
				pstmt.setString(8, variantTypeItm07);
				pstmt.setString(9, variantTypeItm08);
				pstmt.setString(10, variantTypeItm09);
				pstmt.setString(11, variantTypeItm10);
				pstmt.setString(12, variantCodeItm11);
				pstmt.setString(13, variantCodeItm12);
				pstmt.setString(14, variantCodeItm13);
				pstmt.setString(15, variantCodeItm14);
				pstmt.setString(16, variantCodeItm15);
				pstmt.setBigDecimal(17, realProgressiveHie01WAR07);
				pstmt.execute();
				pstmt.close();
			}
			else {
				pstmt = conn.prepareStatement(
					"insert into WAR07_INVENTORY_ITEMS("+
					"COMPANY_CODE_SYS01, "+
					"PROGRESSIVE_WAR06, "+
					"ITEM_CODE_ITM01, "+
					"VARIANT_TYPE_ITM06, "+
					"VARIANT_TYPE_ITM07, "+
					"VARIANT_TYPE_ITM08, "+
					"VARIANT_TYPE_ITM09, "+
					"VARIANT_TYPE_ITM10, "+
					"VARIANT_CODE_ITM11, "+
					"VARIANT_CODE_ITM12, "+
					"VARIANT_CODE_ITM13, "+
					"VARIANT_CODE_ITM14, "+
					"VARIANT_CODE_ITM15, "+
					"PROGRESSIVE_HIE01, "+
					"REAL_PROGRESSIVE_HIE01, "+
					"PROGRESSIVE_HIE02, "+
					"STATE, "+
					"QTY, "+
					"REAL_QTY, "+
					"DELTA_QTY) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
				);
				pstmt.setString(1, companyCodeSys01);
				pstmt.setBigDecimal(2, progressiveWAR06);
				pstmt.setString(3, itemCodeItm01WAR07);
				pstmt.setString(4, variantTypeItm06);
				pstmt.setString(5, variantTypeItm07);
				pstmt.setString(6, variantTypeItm08);
				pstmt.setString(7, variantTypeItm09);
				pstmt.setString(8, variantTypeItm10);
				pstmt.setString(9, variantCodeItm11);
				pstmt.setString(10, variantCodeItm12);
				pstmt.setString(11, variantCodeItm13);
				pstmt.setString(12, variantCodeItm14);
				pstmt.setString(13, variantCodeItm15);
				pstmt.setBigDecimal(14, realProgressiveHie01WAR07);
				pstmt.setBigDecimal(15, realProgressiveHie01WAR07);
				pstmt.setBigDecimal(16, progressiveHIE02);
				pstmt.setString(17, ApplicationConsts.CONFIRMED);
				pstmt.setBigDecimal(18, new BigDecimal(0));
				pstmt.setBigDecimal(19, realQtyWAR07);
				pstmt.setBigDecimal(20, realQtyWAR07);
				pstmt.execute();
				pstmt.close();
			}


	     // insert serial numbers...
			 pstmt = conn.prepareStatement(
				 "insert into WAR08_INVENTORY_S_N("+
				 "COMPANY_CODE_SYS01, "+
				 "PROGRESSIVE_WAR06, "+
				 "ITEM_CODE_ITM01, "+
				 "VARIANT_TYPE_ITM06, "+
				 "VARIANT_TYPE_ITM07, "+
				 "VARIANT_TYPE_ITM08, "+
				 "VARIANT_TYPE_ITM09, "+
				 "VARIANT_TYPE_ITM10, "+
				 "VARIANT_CODE_ITM11, "+
				 "VARIANT_CODE_ITM12, "+
				 "VARIANT_CODE_ITM13, "+
				 "VARIANT_CODE_ITM14, "+
				 "VARIANT_CODE_ITM15, "+
 				 "PROGRESSIVE_HIE01, "+
				 "SERIAL_NUMBER) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
			 );
			 for(int i=0;i<serialNumbers.size();i++) {
				 pstmt.setString(1, companyCodeSys01);
				 pstmt.setBigDecimal(2, progressiveWAR06);
				 pstmt.setString(3, itemCodeItm01WAR07);
				 pstmt.setString(4, variantTypeItm06);
				 pstmt.setString(5, variantTypeItm07);
				 pstmt.setString(6, variantTypeItm08);
				 pstmt.setString(7, variantTypeItm09);
				 pstmt.setString(8, variantTypeItm10);
				 pstmt.setString(9, variantCodeItm11);
				 pstmt.setString(10, variantCodeItm12);
				 pstmt.setString(11, variantCodeItm13);
				 pstmt.setString(12, variantCodeItm14);
				 pstmt.setString(13, variantCodeItm15);
				 pstmt.setBigDecimal(14, realProgressiveHie01WAR07);
				 pstmt.setObject(15, serialNumbers.get(i));
				 pstmt.execute();
			 }
			 pstmt.close();

			 return new VOResponse(Boolean.TRUE);
		 }
		 catch (Throwable ex) {
			 Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating qty for an item in inventory",ex);
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


}

