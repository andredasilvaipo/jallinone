package org.jallinone.accounting.movements.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.jallinone.accounting.movements.java.JournalHeaderVO;
import org.jallinone.accounting.movements.java.JournalRowVO;
import org.jallinone.system.progressives.server.CompanyProgressiveUtils;
import org.openswing.swing.logger.server.Logger;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOResponse;
import org.openswing.swing.server.QueryUtil;
import org.openswing.swing.server.UserSessionParameters;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to insert journal items.</p>
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
public class InsertJournalItemBean implements InsertJournalItem {


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




	/**
	 * Insert a new accounting item in ACC05/ACC06 tables.
	 * @return JournalHeaderVO object with the progressive attribute filled
	 */
	public VOResponse insertJournalItem(JournalHeaderVO vo,String serverLanguageId,String username) throws Throwable {
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;


			// generate progressive for journal item number...
			vo.setProgressiveACC05(CompanyProgressiveUtils.getConsecutiveProgressive(
					vo.getCompanyCodeSys01ACC05(),
					"ACC05_JOURNAL_HEADER_YEAR="+vo.getItemYearACC05(),
					"PROGRESSIVE",
					conn
			));


			// insert into ACC05...
			Map attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01ACC05","COMPANY_CODE_SYS01");
			attribute2dbField.put("accountingMotiveCodeAcc03ACC05","ACCOUNTING_MOTIVE_CODE_ACC03");
			attribute2dbField.put("progressiveACC05","PROGRESSIVE");
			attribute2dbField.put("itemYearACC05","ITEM_YEAR");
			attribute2dbField.put("itemDateACC05","ITEM_DATE");
			attribute2dbField.put("descriptionACC05","DESCRIPTION");
			Response res = QueryUtil.insertTable(
					conn,
					new UserSessionParameters(username),
					vo,
					"ACC05_JOURNAL_HEADER",
					attribute2dbField,
					"Y",
					"N",
					null,
					true
			);
			if (res.isError())
				throw new Exception(res.getErrorMessage());


			// insert into ACC06...
			attribute2dbField.clear();

			attribute2dbField.put("companyCodeSys01ACC06","COMPANY_CODE_SYS01");
			attribute2dbField.put("progressiveAcc05ACC06","PROGRESSIVE_ACC05");
			attribute2dbField.put("itemYearAcc05ACC06","ITEM_YEAR_ACC05");
			attribute2dbField.put("progressiveACC06","PROGRESSIVE");
			attribute2dbField.put("debitAmountACC06","DEBIT_AMOUNT");
			attribute2dbField.put("creditAmountACC06","CREDIT_AMOUNT");
			attribute2dbField.put("accountCodeTypeACC06","ACCOUNT_CODE_TYPE");
			attribute2dbField.put("accountCodeACC06","ACCOUNT_CODE");
			attribute2dbField.put("accountCodeAcc02ACC06","ACCOUNT_CODE_ACC02");
			attribute2dbField.put("descriptionACC06","DESCRIPTION");
			JournalRowVO rowVO = null;
			for(int i=0;i<vo.getJournalRows().size();i++) {
				rowVO = (JournalRowVO)vo.getJournalRows().get(i);
				rowVO.setProgressiveAcc05ACC06(vo.getProgressiveACC05());
				rowVO.setProgressiveACC06(CompanyProgressiveUtils.getConsecutiveProgressive(
						vo.getCompanyCodeSys01ACC05(),
						"ACC06_JOURNAL_ROWS",
						"PROGRESSIVE",
						conn
				));
				res = QueryUtil.insertTable(
						conn,
						new UserSessionParameters(username),
						rowVO,
						"ACC06_JOURNAL_ROWS",
						attribute2dbField,
						"Y",
						"N",
						null,
						true
				);
				if (res.isError())
					throw new Exception(res.getErrorMessage());
			}

			return new VOResponse(vo);
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"insertItem","Error while inserting a new item in the journal",ex);
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
