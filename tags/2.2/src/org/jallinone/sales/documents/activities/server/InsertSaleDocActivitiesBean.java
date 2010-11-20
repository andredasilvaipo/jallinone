package org.jallinone.sales.documents.activities.server;

import org.openswing.swing.server.*;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;
import org.jallinone.sales.documents.activities.java.*;
import org.jallinone.sales.documents.server.*;
import org.jallinone.sales.documents.java.*;
import org.jallinone.sales.documents.java.*;
import org.jallinone.sales.documents.server.*;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage sale activities for a sale document.</p>
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
public class InsertSaleDocActivitiesBean  implements InsertSaleDocActivities {


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


	private UpdateTaxableIncomesBean totals;

	public void setTotals(UpdateTaxableIncomesBean totals) {
		this.totals = totals;
	}


	private SaleDocActivitiesBean bean;

	public void setBean(SaleDocActivitiesBean bean) {
		this.bean = bean;
	}


	public InsertSaleDocActivitiesBean() {
	}


	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
	 */
	public SaleDocActivityVO getSaleDocActivity() {
		throw new UnsupportedOperationException();
	}
	

	/**
	 * Business logic to execute.
	 */
	public VOListResponse insertSaleDocActivities(
			HashMap variant1Descriptions,
			HashMap variant2Descriptions,
			HashMap variant3Descriptions,
			HashMap variant4Descriptions,
			HashMap variant5Descriptions,
			ArrayList list,String serverLanguageId,String username) throws Throwable {
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;
			totals.setConn(conn); // use same transaction...
			bean.setConn(conn); // use same transaction...
			SaleDocActivityVO vo = null;
			Response res = null;

			for(int i=0;i<list.size();i++) {
				vo = (SaleDocActivityVO)list.get(i);
				res = bean.insertSaleActivity(vo,username);
				if (res.isError()) {
					throw new Exception(res.getErrorMessage());
				}
			}

			pstmt = conn.prepareStatement("update DOC01_SELLING set DOC_STATE=? where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=?");
			pstmt.setString(1,ApplicationConsts.HEADER_BLOCKED);
			pstmt.setString(2,vo.getCompanyCodeSys01DOC13());
			pstmt.setString(3,vo.getDocTypeDOC13());
			pstmt.setBigDecimal(4,vo.getDocYearDOC13());
			pstmt.setBigDecimal(5,vo.getDocNumberDOC13());
			pstmt.execute();

			res = totals.updateTaxableIncomes(
				variant1Descriptions,
				variant2Descriptions,
				variant3Descriptions,
				variant4Descriptions,
				variant5Descriptions,
				new SaleDocPK(vo.getCompanyCodeSys01DOC13(),vo.getDocTypeDOC13(),vo.getDocYearDOC13(),vo.getDocNumberDOC13()),
				serverLanguageId,
				username

			);
			if (res.isError()) {
				throw new Exception(res.getErrorMessage());
			}

			return new VOListResponse(list,false,list.size());
		}
		catch (Throwable ex) {
			Logger.error(username, this.getClass().getName(),
									 "executeCommand", "Error while inserting new sale activities", ex);
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
			try {
				totals.setConn(null);
				bean.setConn(null);
			} catch (Exception ex) {}
		}

	}




}

