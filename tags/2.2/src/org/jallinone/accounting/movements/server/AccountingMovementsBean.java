package org.jallinone.accounting.movements.server;

import org.openswing.swing.server.*;

import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;

import java.math.*;

import org.openswing.swing.message.send.java.*;
import org.jallinone.commons.java.*;
import org.jallinone.accounting.movements.java.*;
import org.jallinone.accounting.movements.java.*;
import org.jallinone.registers.payments.server.*;
import org.jallinone.registers.payments.server.*;
import org.jallinone.registers.payments.java.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.registers.payments.java.*;
import org.jallinone.system.progressives.server.*;
import org.openswing.swing.internationalization.server.*;
import org.openswing.swing.internationalization.java.*;
import org.jallinone.system.server.*;
import org.jallinone.accounting.movements.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage accounting movements.</p>
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
public class AccountingMovementsBean  implements AccountingMovements {


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

	
	private InsertJournalItemBean bean;
	
	public void setBean(InsertJournalItemBean bean) {
		this.bean = bean;
	}
	
	


	public AccountingMovementsBean() {
	}





	/**
	 * Business logic to execute.
	 */
	public VOResponse openPAccounts(HashMap map,String t1,String serverLanguageId,String username) throws Throwable {
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;
			bean.setConn(conn);

			// fetch data from client request...
			String companyCode = (String)map.get(ApplicationConsts.COMPANY_CODE_SYS01);
			java.sql.Date startDate = new java.sql.Date(((java.util.Date)map.get(ApplicationConsts.DATE_FILTER)).getTime());
			int lastYear = startDate.getYear()+1900-1;
			String openingAccountCode = (String)map.get(ApplicationConsts.OPENING_ACCOUNT);

			pstmt = conn.prepareStatement(
					"select sum(ACC06_JOURNAL_ROWS.DEBIT_AMOUNT),sum(ACC06_JOURNAL_ROWS.CREDIT_AMOUNT),ACC06_JOURNAL_ROWS.ACCOUNT_CODE,ACC06_JOURNAL_ROWS.ACCOUNT_CODE_ACC02,ACC06_JOURNAL_ROWS.ACCOUNT_CODE_TYPE "+
					"from ACC06_JOURNAL_ROWS,ACC05_JOURNAL_HEADER,ACC02_ACCOUNTS,ACC01_LEDGER where "+
					"ACC06_JOURNAL_ROWS.COMPANY_CODE_SYS01=ACC05_JOURNAL_HEADER.COMPANY_CODE_SYS01 and "+
					"ACC06_JOURNAL_ROWS.ITEM_YEAR_ACC05=ACC05_JOURNAL_HEADER.ITEM_YEAR and "+
					"ACC06_JOURNAL_ROWS.PROGRESSIVE_ACC05=ACC05_JOURNAL_HEADER.PROGRESSIVE and "+
					"ACC06_JOURNAL_ROWS.COMPANY_CODE_SYS01=? and "+
					"ACC05_JOURNAL_HEADER.ITEM_YEAR=? and "+
					"ACC06_JOURNAL_ROWS.COMPANY_CODE_SYS01=ACC02_ACCOUNTS.COMPANY_CODE_SYS01 and "+
					"ACC06_JOURNAL_ROWS.ACCOUNT_CODE_ACC02=ACC02_ACCOUNTS.ACCOUNT_CODE and "+
					"ACC02_ACCOUNTS.COMPANY_CODE_SYS01=ACC01_LEDGER.COMPANY_CODE_SYS01 and "+
					"ACC02_ACCOUNTS.LEDGER_CODE_ACC01=ACC01_LEDGER.LEDGER_CODE and "+
					"ACC01_LEDGER.ACCOUNT_TYPE=? and "+
					"(ACC02_ACCOUNTS.ACCOUNT_TYPE=? or ACC06_JOURNAL_ROWS.ACCOUNT_CODE_TYPE=?)and "+
					"NOT ACC06_JOURNAL_ROWS.ACCOUNT_CODE_ACC02=? "+
					"group by ACC06_JOURNAL_ROWS.ACCOUNT_CODE,ACC06_JOURNAL_ROWS.ACCOUNT_CODE_ACC02,ACC06_JOURNAL_ROWS.ACCOUNT_CODE_TYPE"
			);

			// retrieve settlement amounts for each patrimonial account (except for openingAccountCode) that is an active account...
			pstmt.setString(1,companyCode);
			pstmt.setInt(2,lastYear);
			pstmt.setString(3,ApplicationConsts.PATRIMONIAL_ACCOUNT);
			pstmt.setString(4,ApplicationConsts.DEBIT_ACCOUNT);
			pstmt.setString(5,ApplicationConsts.ACCOUNT_TYPE_CUSTOMER);
			pstmt.setString(6,openingAccountCode);
			ResultSet rset = pstmt.executeQuery();

			// prepare the accounting item...
			JournalHeaderVO jhVO = new JournalHeaderVO();
			jhVO.setCompanyCodeSys01ACC05(companyCode);
			jhVO.setDescriptionACC05(t1+lastYear);
			jhVO.setAccountingMotiveCodeAcc03ACC05(ApplicationConsts.MOTIVE_ACCOUNTS_OPENING);
			jhVO.setItemDateACC05(startDate);
			jhVO.setItemYearACC05(new BigDecimal(startDate.getYear()+1900));

			// for each account code: create a row in the accounting item...
			JournalRowVO jrVO = null;
			BigDecimal totalSettlement = new BigDecimal(0);
			BigDecimal settlement1 = null;
			BigDecimal settlement2 = null;
			BigDecimal settlement = null;
			String accountCode = null;
			String accountCodeAcc02 = null;
			String accountCodeType = null;
			
			while(rset.next()) {
				settlement1 = rset.getBigDecimal(1);
				if (settlement1==null)
					settlement1 = new BigDecimal(0);
				settlement2 = rset.getBigDecimal(2);
				if (settlement2==null)
					settlement2 = new BigDecimal(0);
				settlement = settlement1.subtract(settlement2);
				accountCode = rset.getString(3);
				accountCodeAcc02 = rset.getString(4);
				accountCodeType = rset.getString(5);

				jrVO = new JournalRowVO();
				jrVO.setCompanyCodeSys01ACC06(jhVO.getCompanyCodeSys01ACC05());
				jrVO.setAccountCodeAcc02ACC06(accountCodeAcc02);
				jrVO.setAccountCodeACC06(accountCode);
				jrVO.setAccountCodeTypeACC06(accountCodeType);
				jrVO.setDebitAmountACC06(settlement);
				jrVO.setDescriptionACC06("");
				jrVO.setItemYearAcc05ACC06(jhVO.getItemYearACC05());
				jrVO.setProgressiveAcc05ACC06(jhVO.getProgressiveACC05());
				jhVO.addJournalRow(jrVO);

				totalSettlement = totalSettlement.add(settlement);
			}
			rset.close();

			// add new row for the total settlement...
			jrVO = new JournalRowVO();
			jrVO.setCompanyCodeSys01ACC06(jhVO.getCompanyCodeSys01ACC05());
			jrVO.setAccountCodeAcc02ACC06(openingAccountCode);
			jrVO.setAccountCodeACC06(openingAccountCode);
			jrVO.setAccountCodeTypeACC06(ApplicationConsts.ACCOUNT_TYPE_ACCOUNT);
			jrVO.setCreditAmountACC06(totalSettlement);
			jrVO.setDescriptionACC06("");
			jrVO.setItemYearAcc05ACC06(jhVO.getItemYearACC05());
			jrVO.setProgressiveAcc05ACC06(jhVO.getProgressiveACC05());
			jhVO.addJournalRow(jrVO);

			// insert the accounting item...
			VOResponse res = bean.insertJournalItem(jhVO,serverLanguageId,username);
			if (res.isError()) {
				throw new Exception(res.getErrorMessage());
			}


			// retrieve settlement amounts for each patrimonial account (except for openingAccountCode) that is a passive account...
			pstmt.setString(1,companyCode);
			pstmt.setInt(2,lastYear);
			pstmt.setString(3,ApplicationConsts.PATRIMONIAL_ACCOUNT);
			pstmt.setString(4,ApplicationConsts.CREDIT_ACCOUNT);
			pstmt.setString(5,ApplicationConsts.ACCOUNT_TYPE_SUPPLIER);
			pstmt.setString(6,openingAccountCode);
			rset = pstmt.executeQuery();

			// prepare the accounting item...
			jhVO = new JournalHeaderVO();
			jhVO.setCompanyCodeSys01ACC05(companyCode);
			jhVO.setDescriptionACC05(t1+lastYear);
			jhVO.setAccountingMotiveCodeAcc03ACC05(ApplicationConsts.MOTIVE_ACCOUNTS_OPENING);
			jhVO.setItemDateACC05(startDate);
			jhVO.setItemYearACC05(new BigDecimal(startDate.getYear()+1900));

			// for each account code: create a row in the accounting item...
			totalSettlement = new BigDecimal(0);
			while(rset.next()) {
				settlement1 = rset.getBigDecimal(1);
				if (settlement1==null)
					settlement1 = new BigDecimal(0);
				settlement2 = rset.getBigDecimal(2);
				if (settlement2==null)
					settlement2 = new BigDecimal(0);
				settlement = settlement1.subtract(settlement2);
				settlement = settlement.negate();
				accountCode = rset.getString(3);
				accountCodeAcc02 = rset.getString(4);
				accountCodeType = rset.getString(5);

				jrVO = new JournalRowVO();
				jrVO.setCompanyCodeSys01ACC06(jhVO.getCompanyCodeSys01ACC05());
				jrVO.setAccountCodeAcc02ACC06(accountCodeAcc02);
				jrVO.setAccountCodeACC06(accountCode);
				jrVO.setAccountCodeTypeACC06(accountCodeType);
				jrVO.setCreditAmountACC06(settlement);
				jrVO.setDescriptionACC06("");
				jrVO.setItemYearAcc05ACC06(jhVO.getItemYearACC05());
				jrVO.setProgressiveAcc05ACC06(jhVO.getProgressiveACC05());
				jhVO.addJournalRow(jrVO);

				totalSettlement = totalSettlement.add(settlement);
			}
			rset.close();

			// add new row for the total settlement...
			jrVO = new JournalRowVO();
			jrVO.setCompanyCodeSys01ACC06(jhVO.getCompanyCodeSys01ACC05());
			jrVO.setAccountCodeAcc02ACC06(openingAccountCode);
			jrVO.setAccountCodeACC06(openingAccountCode);
			jrVO.setAccountCodeTypeACC06(ApplicationConsts.ACCOUNT_TYPE_ACCOUNT);
			jrVO.setDebitAmountACC06(totalSettlement);
			jrVO.setDescriptionACC06("");
			jrVO.setItemYearAcc05ACC06(jhVO.getItemYearACC05());
			jrVO.setProgressiveAcc05ACC06(jhVO.getProgressiveACC05());
			jhVO.addJournalRow(jrVO);

			// insert the accounting item...
			res = bean.insertJournalItem(jhVO,serverLanguageId,username);
			if (res.isError()) {
				throw new Exception(res.getErrorMessage());
			}

			pstmt.close();
			return res;
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while opening patrimonial accounts",ex);
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
				bean.setConn(null);
			} catch (Exception e) {
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
	public VOResponse endorseEAccounts(HashMap map,String t1,String t2,String t3,String serverLanguageId,String username) throws Throwable {
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;
			bean.setConn(conn);
			
			// fetch data from client request...
			String companyCode = (String)map.get(ApplicationConsts.COMPANY_CODE_SYS01);
			java.sql.Date endDate = new java.sql.Date(((java.util.Date)map.get(ApplicationConsts.DATE_FILTER)).getTime());
			String lossProfitEAccountCode = (String)map.get(ApplicationConsts.LOSSPROFIT_E_ACCOUNT);
			String lossProfitPAccountCode = (String)map.get(ApplicationConsts.LOSSPROFIT_P_ACCOUNT);

			pstmt = conn.prepareStatement(
					"select sum(ACC06_JOURNAL_ROWS.DEBIT_AMOUNT),sum(ACC06_JOURNAL_ROWS.CREDIT_AMOUNT),ACC06_JOURNAL_ROWS.ACCOUNT_CODE_ACC02 "+
					"from ACC06_JOURNAL_ROWS,ACC05_JOURNAL_HEADER,ACC02_ACCOUNTS,ACC01_LEDGER where "+
					"ACC06_JOURNAL_ROWS.COMPANY_CODE_SYS01=ACC05_JOURNAL_HEADER.COMPANY_CODE_SYS01 and "+
					"ACC06_JOURNAL_ROWS.ITEM_YEAR_ACC05=ACC05_JOURNAL_HEADER.ITEM_YEAR and "+
					"ACC06_JOURNAL_ROWS.PROGRESSIVE_ACC05=ACC05_JOURNAL_HEADER.PROGRESSIVE and "+
					"ACC06_JOURNAL_ROWS.COMPANY_CODE_SYS01=? and "+
					"ACC05_JOURNAL_HEADER.ITEM_DATE>=? and ACC05_JOURNAL_HEADER.ITEM_DATE<=? and "+
					"ACC06_JOURNAL_ROWS.COMPANY_CODE_SYS01=ACC02_ACCOUNTS.COMPANY_CODE_SYS01 and "+
					"ACC06_JOURNAL_ROWS.ACCOUNT_CODE_ACC02=ACC02_ACCOUNTS.ACCOUNT_CODE and "+
					"ACC02_ACCOUNTS.COMPANY_CODE_SYS01=ACC01_LEDGER.COMPANY_CODE_SYS01 and "+
					"ACC02_ACCOUNTS.LEDGER_CODE_ACC01=ACC01_LEDGER.LEDGER_CODE and "+
					"ACC01_LEDGER.ACCOUNT_TYPE=? and "+
					"ACC02_ACCOUNTS.ACCOUNT_TYPE=? and "+
					"NOT ACC06_JOURNAL_ROWS.ACCOUNT_CODE_ACC02=? "+
					"group by ACC06_JOURNAL_ROWS.ACCOUNT_CODE_ACC02"
			);
			Calendar cal = Calendar.getInstance();
			cal.set(cal.DAY_OF_MONTH,1);
			cal.set(cal.MONTH,0);
			cal.set(cal.YEAR,endDate.getYear()+1900);
			java.sql.Date startDate = new java.sql.Date(cal.getTimeInMillis());

			// retrieve settlement amounts for each economic account (except for lossProfitEAccountCode) that is a cost...
			pstmt.setString(1,companyCode);
			pstmt.setDate(2,startDate);
			pstmt.setDate(3,endDate);
			pstmt.setString(4,ApplicationConsts.ECONOMIC_ACCOUNT);
			pstmt.setString(5,ApplicationConsts.DEBIT_ACCOUNT);
			pstmt.setString(6,lossProfitEAccountCode);
			ResultSet rset = pstmt.executeQuery();

			// prepare the accounting item...
			JournalHeaderVO jhVO = new JournalHeaderVO();
			jhVO.setCompanyCodeSys01ACC05(companyCode);
			jhVO.setDescriptionACC05(t1);
			jhVO.setAccountingMotiveCodeAcc03ACC05(ApplicationConsts.MOTIVE_LOSS_PROFIT_ENDORSING);
			jhVO.setItemDateACC05(endDate);
			jhVO.setItemYearACC05(new BigDecimal(endDate.getYear()+1900));

			// for each account code: create a row in the accounting item...
			JournalRowVO jrVO = null;
			BigDecimal totalSettlement = new BigDecimal(0);
			BigDecimal total = null;
			BigDecimal settlement1 = null;
			BigDecimal settlement2 = null;
			BigDecimal settlement = null;
			String accountCode = null;
			boolean rowsFound = false;
			
			while(rset.next()) {
				settlement1 = rset.getBigDecimal(1);
				if (settlement1==null)
					settlement1 = new BigDecimal(0);
				settlement2 = rset.getBigDecimal(2);
				if (settlement2==null)
					settlement2 = new BigDecimal(0);
				settlement = settlement1.subtract(settlement2);
				accountCode = rset.getString(3);

				jrVO = new JournalRowVO();
				jrVO.setCompanyCodeSys01ACC06(jhVO.getCompanyCodeSys01ACC05());
				jrVO.setAccountCodeAcc02ACC06(accountCode);
				jrVO.setAccountCodeACC06(accountCode);
				jrVO.setAccountCodeTypeACC06(ApplicationConsts.ACCOUNT_TYPE_ACCOUNT);
				jrVO.setCreditAmountACC06(settlement);
				jrVO.setDescriptionACC06("");
				jrVO.setItemYearAcc05ACC06(jhVO.getItemYearACC05());
				jrVO.setProgressiveAcc05ACC06(jhVO.getProgressiveACC05());
				jhVO.addJournalRow(jrVO);

				totalSettlement = totalSettlement.add(settlement);
				rowsFound = true;
			}
			rset.close();

			// add new row for the total settlement...
			jrVO = new JournalRowVO();
			jrVO.setCompanyCodeSys01ACC06(jhVO.getCompanyCodeSys01ACC05());
			jrVO.setAccountCodeAcc02ACC06(lossProfitEAccountCode);
			jrVO.setAccountCodeACC06(lossProfitEAccountCode);
			jrVO.setAccountCodeTypeACC06(ApplicationConsts.ACCOUNT_TYPE_ACCOUNT);
			jrVO.setDebitAmountACC06(totalSettlement);
			jrVO.setDescriptionACC06("");
			jrVO.setItemYearAcc05ACC06(jhVO.getItemYearACC05());
			jrVO.setProgressiveAcc05ACC06(jhVO.getProgressiveACC05());
			jhVO.addJournalRow(jrVO);
			total = totalSettlement;

			// insert the accounting item...
			VOResponse res = bean.insertJournalItem(jhVO,serverLanguageId,username);
			if (res.isError()) {
				throw new Exception(res.getErrorMessage());
			}


			// retrieve settlement amounts for each economic account (except for lossProfitEAccountCode) that is a proceeds...
			pstmt.setString(1,companyCode);
			pstmt.setDate(2,startDate);
			pstmt.setDate(3,endDate);
			pstmt.setString(4,ApplicationConsts.ECONOMIC_ACCOUNT);
			pstmt.setString(5,ApplicationConsts.CREDIT_ACCOUNT);
			pstmt.setString(6,lossProfitEAccountCode);
			rset = pstmt.executeQuery();

			// prepare the accounting item...
			jhVO = new JournalHeaderVO();
			jhVO.setCompanyCodeSys01ACC05(companyCode);
			jhVO.setDescriptionACC05(t1);
			jhVO.setAccountingMotiveCodeAcc03ACC05(ApplicationConsts.MOTIVE_LOSS_PROFIT_ENDORSING);
			jhVO.setItemDateACC05(endDate);
			jhVO.setItemYearACC05(new BigDecimal(endDate.getYear()+1900));

			// for each account code: create a row in the accounting item...
			totalSettlement = new BigDecimal(0);
			while(rset.next()) {
				settlement1 = rset.getBigDecimal(1);
				if (settlement1==null)
					settlement1 = new BigDecimal(0);
				settlement2 = rset.getBigDecimal(2);
				if (settlement2==null)
					settlement2 = new BigDecimal(0);
				settlement = settlement1.subtract(settlement2);

				settlement = settlement.negate();
				accountCode = rset.getString(3);

				jrVO = new JournalRowVO();
				jrVO.setCompanyCodeSys01ACC06(jhVO.getCompanyCodeSys01ACC05());
				jrVO.setAccountCodeAcc02ACC06(accountCode);
				jrVO.setAccountCodeACC06(accountCode);
				jrVO.setAccountCodeTypeACC06(ApplicationConsts.ACCOUNT_TYPE_ACCOUNT);
				jrVO.setDebitAmountACC06(settlement);
				jrVO.setDescriptionACC06("");
				jrVO.setItemYearAcc05ACC06(jhVO.getItemYearACC05());
				jrVO.setProgressiveAcc05ACC06(jhVO.getProgressiveACC05());
				jhVO.addJournalRow(jrVO);

				totalSettlement = totalSettlement.add(settlement);
				rowsFound = true;
			}
			rset.close();

			// add new row for the total settlement...
			jrVO = new JournalRowVO();
			jrVO.setCompanyCodeSys01ACC06(jhVO.getCompanyCodeSys01ACC05());
			jrVO.setAccountCodeAcc02ACC06(lossProfitEAccountCode);
			jrVO.setAccountCodeACC06(lossProfitEAccountCode);
			jrVO.setAccountCodeTypeACC06(ApplicationConsts.ACCOUNT_TYPE_ACCOUNT);
			jrVO.setCreditAmountACC06(totalSettlement);
			jrVO.setDescriptionACC06("");
			jrVO.setItemYearAcc05ACC06(jhVO.getItemYearACC05());
			jrVO.setProgressiveAcc05ACC06(jhVO.getProgressiveACC05());
			jhVO.addJournalRow(jrVO);
			total = total.subtract(totalSettlement); // loss-profit...

			// insert the accounting item...
			res = bean.insertJournalItem(jhVO,serverLanguageId,username);
			if (res.isError()) {
				throw new Exception(res.getErrorMessage());
			}

			pstmt.close();


			if (!rowsFound) {
				throw new Exception("no loss/profit found in the economic account");
			}


			// prepare the accounting item...
			jhVO = new JournalHeaderVO();
			jhVO.setCompanyCodeSys01ACC05(companyCode);
			if (total.doubleValue()<0) {
				// profit...
				jhVO.setDescriptionACC05(t2);
				jhVO.setAccountingMotiveCodeAcc03ACC05(ApplicationConsts.MOTIVE_PROFIT_ENDORSING);
			}
			else {
				// loss...
				jhVO.setDescriptionACC05(t3);
				jhVO.setAccountingMotiveCodeAcc03ACC05(ApplicationConsts.MOTIVE_LOSS_ENDORSING);
			}
			jhVO.setItemDateACC05(endDate);
			jhVO.setItemYearACC05(new BigDecimal(endDate.getYear()+1900));

			jrVO = new JournalRowVO();
			jrVO.setCompanyCodeSys01ACC06(jhVO.getCompanyCodeSys01ACC05());
			jrVO.setAccountCodeAcc02ACC06(lossProfitEAccountCode);
			jrVO.setAccountCodeACC06(lossProfitEAccountCode);
			jrVO.setAccountCodeTypeACC06(ApplicationConsts.ACCOUNT_TYPE_ACCOUNT);
			if (total.doubleValue()<0) {
				// profit...
				jrVO.setDebitAmountACC06(total.negate());
			}
			else {
				// loss...
				jrVO.setCreditAmountACC06(total);
			}
			jrVO.setDescriptionACC06("");
			jrVO.setItemYearAcc05ACC06(jhVO.getItemYearACC05());
			jrVO.setProgressiveAcc05ACC06(jhVO.getProgressiveACC05());
			jhVO.addJournalRow(jrVO);

			jrVO = new JournalRowVO();
			jrVO.setCompanyCodeSys01ACC06(jhVO.getCompanyCodeSys01ACC05());
			jrVO.setAccountCodeAcc02ACC06(lossProfitPAccountCode);
			jrVO.setAccountCodeACC06(lossProfitPAccountCode);
			jrVO.setAccountCodeTypeACC06(ApplicationConsts.ACCOUNT_TYPE_ACCOUNT);
			if (total.doubleValue()<0) {
				// profit...
				jrVO.setCreditAmountACC06(total.negate());
			}
			else {
				// loss...
				jrVO.setDebitAmountACC06(total);
			}
			jrVO.setDescriptionACC06("");
			jrVO.setItemYearAcc05ACC06(jhVO.getItemYearACC05());
			jrVO.setProgressiveAcc05ACC06(jhVO.getProgressiveACC05());
			jhVO.addJournalRow(jrVO);

			// insert the accounting item...
			res = bean.insertJournalItem(jhVO,serverLanguageId,username);
			if (res.isError()) {
				throw new Exception(res.getErrorMessage());
			}

			return res;
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while endorsing economic accounts",ex);
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
				bean.setConn(null);
			} catch (Exception e) {
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
	public VOResponse closePAccounts(HashMap map,String t1,String serverLanguageId,String username) throws Throwable {
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;
			bean.setConn(conn);
			
			// fetch data from client request...
			String companyCode = (String)map.get(ApplicationConsts.COMPANY_CODE_SYS01);
			java.sql.Date endDate = new java.sql.Date(((java.util.Date)map.get(ApplicationConsts.DATE_FILTER)).getTime());
			String closingAccountCode = (String)map.get(ApplicationConsts.CLOSING_ACCOUNT);

			pstmt = conn.prepareStatement(
					"select sum(ACC06_JOURNAL_ROWS.DEBIT_AMOUNT),sum(ACC06_JOURNAL_ROWS.CREDIT_AMOUNT),ACC06_JOURNAL_ROWS.ACCOUNT_CODE,ACC06_JOURNAL_ROWS.ACCOUNT_CODE_ACC02,ACC06_JOURNAL_ROWS.ACCOUNT_CODE_TYPE "+
					"from ACC06_JOURNAL_ROWS,ACC05_JOURNAL_HEADER,ACC02_ACCOUNTS,ACC01_LEDGER where "+
					"ACC06_JOURNAL_ROWS.COMPANY_CODE_SYS01=ACC05_JOURNAL_HEADER.COMPANY_CODE_SYS01 and "+
					"ACC06_JOURNAL_ROWS.ITEM_YEAR_ACC05=ACC05_JOURNAL_HEADER.ITEM_YEAR and "+
					"ACC06_JOURNAL_ROWS.PROGRESSIVE_ACC05=ACC05_JOURNAL_HEADER.PROGRESSIVE and "+
					"ACC06_JOURNAL_ROWS.COMPANY_CODE_SYS01=? and "+
					"ACC05_JOURNAL_HEADER.ITEM_DATE>=? and ACC05_JOURNAL_HEADER.ITEM_DATE<=? and "+
					"ACC06_JOURNAL_ROWS.COMPANY_CODE_SYS01=ACC02_ACCOUNTS.COMPANY_CODE_SYS01 and "+
					"ACC06_JOURNAL_ROWS.ACCOUNT_CODE_ACC02=ACC02_ACCOUNTS.ACCOUNT_CODE and "+
					"ACC02_ACCOUNTS.COMPANY_CODE_SYS01=ACC01_LEDGER.COMPANY_CODE_SYS01 and "+
					"ACC02_ACCOUNTS.LEDGER_CODE_ACC01=ACC01_LEDGER.LEDGER_CODE and "+
					"ACC01_LEDGER.ACCOUNT_TYPE=? and "+
					"(ACC02_ACCOUNTS.ACCOUNT_TYPE=? or ACC06_JOURNAL_ROWS.ACCOUNT_CODE_TYPE=?)and "+
					"NOT ACC06_JOURNAL_ROWS.ACCOUNT_CODE_ACC02=? "+
					"group by ACC06_JOURNAL_ROWS.ACCOUNT_CODE,ACC06_JOURNAL_ROWS.ACCOUNT_CODE_ACC02,ACC06_JOURNAL_ROWS.ACCOUNT_CODE_TYPE"
			);
			Calendar cal = Calendar.getInstance();
			cal.set(cal.DAY_OF_MONTH,1);
			cal.set(cal.MONTH,0);
			cal.set(cal.YEAR,endDate.getYear()+1900);
			java.sql.Date startDate = new java.sql.Date(cal.getTimeInMillis());

			// retrieve settlement amounts for each patrimonial account (except for closingAccountCode) that is an active account...
			pstmt.setString(1,companyCode);
			pstmt.setDate(2,startDate);
			pstmt.setDate(3,endDate);
			pstmt.setString(4,ApplicationConsts.PATRIMONIAL_ACCOUNT);
			pstmt.setString(5,ApplicationConsts.DEBIT_ACCOUNT);
			pstmt.setString(6,ApplicationConsts.ACCOUNT_TYPE_CUSTOMER);
			pstmt.setString(7,closingAccountCode);
			ResultSet rset = pstmt.executeQuery();

			// prepare the accounting item...
			JournalHeaderVO jhVO = new JournalHeaderVO();
			jhVO.setCompanyCodeSys01ACC05(companyCode);
			jhVO.setDescriptionACC05(t1);
			jhVO.setAccountingMotiveCodeAcc03ACC05(ApplicationConsts.MOTIVE_ACCOUNTS_CLOSING);
			jhVO.setItemDateACC05(endDate);
			jhVO.setItemYearACC05(new BigDecimal(endDate.getYear()+1900));

			// for each account code: create a row in the accounting item...
			JournalRowVO jrVO = null;
			BigDecimal totalSettlement = new BigDecimal(0);
			BigDecimal settlement1 = null;
			BigDecimal settlement2 = null;
			BigDecimal settlement = null;
			String accountCode = null;
			String accountCodeAcc02 = null;
			String accountCodeType = null;
			
			while(rset.next()) {
				settlement1 = rset.getBigDecimal(1);
				if (settlement1==null)
					settlement1 = new BigDecimal(0);
				settlement2 = rset.getBigDecimal(2);
				if (settlement2==null)
					settlement2 = new BigDecimal(0);
				settlement = settlement1.subtract(settlement2);
				accountCode = rset.getString(3);
				accountCodeAcc02 = rset.getString(4);
				accountCodeType = rset.getString(5);

				jrVO = new JournalRowVO();
				jrVO.setCompanyCodeSys01ACC06(jhVO.getCompanyCodeSys01ACC05());
				jrVO.setAccountCodeAcc02ACC06(accountCodeAcc02);
				jrVO.setAccountCodeACC06(accountCode);
				jrVO.setAccountCodeTypeACC06(accountCodeType);
				jrVO.setCreditAmountACC06(settlement);
				jrVO.setDescriptionACC06("");
				jrVO.setItemYearAcc05ACC06(jhVO.getItemYearACC05());
				jrVO.setProgressiveAcc05ACC06(jhVO.getProgressiveACC05());
				jhVO.addJournalRow(jrVO);

				totalSettlement = totalSettlement.add(settlement);
			}
			rset.close();

			// add new row for the total settlement...
			jrVO = new JournalRowVO();
			jrVO.setCompanyCodeSys01ACC06(jhVO.getCompanyCodeSys01ACC05());
			jrVO.setAccountCodeAcc02ACC06(closingAccountCode);
			jrVO.setAccountCodeACC06(closingAccountCode);
			jrVO.setAccountCodeTypeACC06(ApplicationConsts.ACCOUNT_TYPE_ACCOUNT);
			jrVO.setDebitAmountACC06(totalSettlement);
			jrVO.setDescriptionACC06("");
			jrVO.setItemYearAcc05ACC06(jhVO.getItemYearACC05());
			jrVO.setProgressiveAcc05ACC06(jhVO.getProgressiveACC05());
			jhVO.addJournalRow(jrVO);

			// insert the accounting item...
			VOResponse res = bean.insertJournalItem(jhVO,serverLanguageId,username);
			if (res.isError()) {
				throw new Exception(res.getErrorMessage());
			}


			// retrieve settlement amounts for each patrimonial account (except for closingAccountCode) that is a passive account...
			pstmt.setString(1,companyCode);
			pstmt.setDate(2,startDate);
			pstmt.setDate(3,endDate);
			pstmt.setString(4,ApplicationConsts.PATRIMONIAL_ACCOUNT);
			pstmt.setString(5,ApplicationConsts.CREDIT_ACCOUNT);
			pstmt.setString(6,ApplicationConsts.ACCOUNT_TYPE_SUPPLIER);
			pstmt.setString(7,closingAccountCode);
			rset = pstmt.executeQuery();

			// prepare the accounting item...
			jhVO = new JournalHeaderVO();
			jhVO.setCompanyCodeSys01ACC05(companyCode);
			jhVO.setDescriptionACC05(t1);
			jhVO.setAccountingMotiveCodeAcc03ACC05(ApplicationConsts.MOTIVE_ACCOUNTS_CLOSING);
			jhVO.setItemDateACC05(endDate);
			jhVO.setItemYearACC05(new BigDecimal(endDate.getYear()+1900));

			// for each account code: create a row in the accounting item...
			totalSettlement = new BigDecimal(0);
			while(rset.next()) {
				settlement1 = rset.getBigDecimal(1);
				if (settlement1==null)
					settlement1 = new BigDecimal(0);
				settlement2 = rset.getBigDecimal(2);
				if (settlement2==null)
					settlement2 = new BigDecimal(0);
				settlement = settlement1.subtract(settlement2);
				settlement = settlement.negate();
				accountCode = rset.getString(3);
				accountCodeAcc02 = rset.getString(4);
				accountCodeType = rset.getString(5);

				jrVO = new JournalRowVO();
				jrVO.setCompanyCodeSys01ACC06(jhVO.getCompanyCodeSys01ACC05());
				jrVO.setAccountCodeAcc02ACC06(accountCodeAcc02);
				jrVO.setAccountCodeACC06(accountCode);
				jrVO.setAccountCodeTypeACC06(accountCodeType);
				jrVO.setDebitAmountACC06(settlement);
				jrVO.setDescriptionACC06("");
				jrVO.setItemYearAcc05ACC06(jhVO.getItemYearACC05());
				jrVO.setProgressiveAcc05ACC06(jhVO.getProgressiveACC05());
				jhVO.addJournalRow(jrVO);

				totalSettlement = totalSettlement.add(settlement);
			}
			rset.close();

			// add new row for the total settlement...
			jrVO = new JournalRowVO();
			jrVO.setCompanyCodeSys01ACC06(jhVO.getCompanyCodeSys01ACC05());
			jrVO.setAccountCodeAcc02ACC06(closingAccountCode);
			jrVO.setAccountCodeACC06(closingAccountCode);
			jrVO.setAccountCodeTypeACC06(ApplicationConsts.ACCOUNT_TYPE_ACCOUNT);
			jrVO.setCreditAmountACC06(totalSettlement);
			jrVO.setDescriptionACC06("");
			jrVO.setItemYearAcc05ACC06(jhVO.getItemYearACC05());
			jrVO.setProgressiveAcc05ACC06(jhVO.getProgressiveACC05());
			jhVO.addJournalRow(jrVO);

			// insert the accounting item...
			res = bean.insertJournalItem(jhVO,serverLanguageId,username);
			if (res.isError()) {
				throw new Exception(res.getErrorMessage());
			}

			pstmt.close();
			return res;
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while closing patrimonial accounts",ex);
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
				bean.setConn(null);
			} catch (Exception e) {
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
	public VOListResponse insertVatRows(ArrayList vatRows,String serverLanguageId,String username)  throws Throwable{
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;


			// generate progressive for record number...
			VatRowVO rowVO = (VatRowVO)vatRows.get(0);
			BigDecimal recordNumber = CompanyProgressiveUtils.getConsecutiveProgressive(
					rowVO.getCompanyCodeSys01ACC07(),
					"ACC07_VAT_ROWS_RC="+rowVO.getRegisterCodeAcc04ACC07()+"_VAT_YEAR="+rowVO.getVatYearACC07(),
					"RECORD_NUMBER",
					conn
			);

			// insert into ACC07...
			Map attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01ACC07","COMPANY_CODE_SYS01");
			attribute2dbField.put("progressiveACC07","PROGRESSIVE");
			attribute2dbField.put("vatYearACC07","VAT_YEAR");
			attribute2dbField.put("recordNumberACC07","RECORD_NUMBER");
			attribute2dbField.put("registerCodeAcc04ACC07","REGISTER_CODE_ACC04");
			attribute2dbField.put("vatDateACC07","VAT_DATE");
			attribute2dbField.put("vatValueACC07","VAT_VALUE");
			attribute2dbField.put("vatCodeACC07","VAT_CODE");
			attribute2dbField.put("vatDescriptionACC07","VAT_DESCRIPTION");
			attribute2dbField.put("taxableIncomeACC07","TAXABLE_INCOME");
			Response res = null;
			for(int i=0;i<vatRows.size();i++) {
				rowVO = (VatRowVO)vatRows.get(i);
				rowVO.setRecordNumberACC07(recordNumber);
				rowVO.setProgressiveACC07(CompanyProgressiveUtils.getInternalProgressive(
						rowVO.getCompanyCodeSys01ACC07(),
						"ACC07_VAT_ROWS",
						"PROGRESSIVE",
						conn
				));
				res = QueryUtil.insertTable(
						conn,
						new UserSessionParameters(username),
						rowVO,
						"ACC07_VAT_ROWS",
						attribute2dbField,
						"Y",
						"N",
						null,
						true
				);
				if (res.isError())
					throw new Exception(res.getErrorMessage());
			}

			return new VOListResponse(vatRows,false,vatRows.size());
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"insertVatRows","Error while inserting a new vat rows in vat register",ex);
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



}

