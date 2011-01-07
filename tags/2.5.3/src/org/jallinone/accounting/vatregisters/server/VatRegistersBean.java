package org.jallinone.accounting.vatregisters.server;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;

import javax.sql.DataSource;

import org.jallinone.accounting.movements.java.JournalHeaderVO;
import org.jallinone.accounting.movements.java.JournalRowVO;
import org.jallinone.accounting.movements.server.InsertJournalItemBean;
import org.jallinone.accounting.vatregisters.java.VatRegisterVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.system.server.ParamsBean;
import org.jallinone.system.translations.server.TranslationUtils;
import org.openswing.swing.logger.server.Logger;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.message.receive.java.VOResponse;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.message.send.java.LookupValidationParams;
import org.openswing.swing.server.UserSessionParameters;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used manage vat registers.</p>
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
public class VatRegistersBean implements VatRegisters {


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

	private ParamsBean userParamAction;

	public void setUserParamAction(ParamsBean userParamAction) {
		this.userParamAction = userParamAction;
	}



	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
	 */
	public VatRegisterVO getVatRegister() {
		throw new UnsupportedOperationException();
	}
	

	public VatRegistersBean() {
	}



	/**
	 * Business logic to execute.
	 */
	public VOListResponse validateVatRegisterCode(LookupValidationParams validationPars,String serverLanguageId,String username,ArrayList companiesList,ArrayList customizedFields) throws Throwable {
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;

			// retrieve companies list...
			String companies = "";
			if (validationPars.getLookupValidationParameters().get(ApplicationConsts.COMPANY_CODE_SYS01)!=null) {
				companies = "'"+validationPars.getLookupValidationParameters().get(ApplicationConsts.COMPANY_CODE_SYS01)+"'";
			}
			else {
				for(int i=0;i<companiesList.size();i++)
					companies += "'"+companiesList.get(i).toString()+"',";
				companies = companies.substring(0,companies.length()-1);
			}



			String sql =
				"select ACC04_VAT_REGISTERS.REGISTER_CODE,ACC04_VAT_REGISTERS.PROGRESSIVE_SYS10,SYS10_TRANSLATIONS.DESCRIPTION,ACC04_VAT_REGISTERS.ENABLED, "+
				"SYS10_B.DESCRIPTION,ACC04_VAT_REGISTERS.ACCOUNT_CODE_ACC02,ACC04_VAT_REGISTERS.REGISTER_TYPE,ACC04_VAT_REGISTERS.READ_ONLY "+
				"from ACC04_VAT_REGISTERS,SYS10_TRANSLATIONS,ACC02_ACCOUNTS,SYS10_TRANSLATIONS SYS10_B where "+
				"ACC04_VAT_REGISTERS.COMPANY_CODE_SYS01=ACC02_ACCOUNTS.COMPANY_CODE_SYS01 and "+
				"ACC04_VAT_REGISTERS.ACCOUNT_CODE_ACC02=ACC02_ACCOUNTS.ACCOUNT_CODE and "+
				"ACC02_ACCOUNTS.PROGRESSIVE_SYS10=SYS10_B.PROGRESSIVE and "+
				"SYS10_B.LANGUAGE_CODE=? and "+
				"ACC04_VAT_REGISTERS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
				"SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
				"ACC04_VAT_REGISTERS.ENABLED='Y' and "+
				"ACC04_VAT_REGISTERS.REGISTER_CODE='"+validationPars.getCode()+"' and "+
				"ACC04_VAT_REGISTERS.COMPANY_CODE_SYS01 in ("+companies+")";

			Map attribute2dbField = new HashMap();
			attribute2dbField.put("registerCodeACC04","ACC04_VAT_REGISTERS.REGISTER_CODE");
			attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
			attribute2dbField.put("progressiveSys10ACC04","ACC04_VAT_REGISTERS.PROGRESSIVE_SYS10");
			attribute2dbField.put("enabledACC04","ACC04_VAT_REGISTERS.ENABLED");
			attribute2dbField.put("accountDescriptionACC04","SYS10_B.DESCRIPTION");
			attribute2dbField.put("accountCodeAcc02ACC04","ACC04_VAT_REGISTERS.ACCOUNT_CODE_ACC02");
			attribute2dbField.put("registerTypeACC04","ACC04_VAT_REGISTERS.REGISTER_TYPE");
			attribute2dbField.put("readOnlyACC04","ACC04_VAT_REGISTERS.READ_ONLY");

			ArrayList values = new ArrayList();
			values.add(serverLanguageId);
			values.add(serverLanguageId);

			GridParams gridParams = new GridParams();

			// read from ACC04 table...
			Response answer = CustomizeQueryUtil.getQuery(
					conn,
					new UserSessionParameters(username),
					sql,
					values,
					attribute2dbField,
					VatRegisterVO.class,
					"Y",
					"N",
					null,
					gridParams,
					true,
					customizedFields
			);
			if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while validating vat register code",ex);
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
	public VOListResponse loadVatRegisters(GridParams gridParams,String serverLanguageId,String username,ArrayList companiesList,ArrayList customizedFields) throws Throwable {
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;

			// retrieve companies list...
			String companies = "";
			if (gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01)!=null) {
				companies = "'"+gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01)+"'";
			}
			else {
				for(int i=0;i<companiesList.size();i++)
					companies += "'"+companiesList.get(i).toString()+"',";
				companies = companies.substring(0,companies.length()-1);
			}

			String sql =
				"select ACC04_VAT_REGISTERS.COMPANY_CODE_SYS01,ACC04_VAT_REGISTERS.REGISTER_CODE,"+
				"ACC04_VAT_REGISTERS.PROGRESSIVE_SYS10,SYS10_TRANSLATIONS.DESCRIPTION,ACC04_VAT_REGISTERS.ENABLED, "+
				"SYS10_B.DESCRIPTION,ACC04_VAT_REGISTERS.ACCOUNT_CODE_ACC02,ACC04_VAT_REGISTERS.REGISTER_TYPE,ACC04_VAT_REGISTERS.READ_ONLY "+
				"from ACC04_VAT_REGISTERS,SYS10_TRANSLATIONS,ACC02_ACCOUNTS,SYS10_TRANSLATIONS SYS10_B where "+
				"ACC04_VAT_REGISTERS.COMPANY_CODE_SYS01=ACC02_ACCOUNTS.COMPANY_CODE_SYS01 and "+
				"ACC04_VAT_REGISTERS.ACCOUNT_CODE_ACC02=ACC02_ACCOUNTS.ACCOUNT_CODE and "+
				"ACC02_ACCOUNTS.PROGRESSIVE_SYS10=SYS10_B.PROGRESSIVE and "+
				"SYS10_B.LANGUAGE_CODE=? and "+
				"ACC04_VAT_REGISTERS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
				"SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
				"ACC04_VAT_REGISTERS.ENABLED='Y' and ACC04_VAT_REGISTERS.COMPANY_CODE_SYS01 in ("+companies+")";

			Map attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01ACC04","ACC04_VAT_REGISTERS.COMPANY_CODE_SYS01");
			attribute2dbField.put("registerCodeACC04","ACC04_VAT_REGISTERS.REGISTER_CODE");
			attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
			attribute2dbField.put("progressiveSys10ACC04","ACC04_VAT_REGISTERS.PROGRESSIVE_SYS10");
			attribute2dbField.put("enabledACC04","ACC04_VAT_REGISTERS.ENABLED");
			attribute2dbField.put("accountDescriptionACC04","SYS10_B.DESCRIPTION");
			attribute2dbField.put("accountCodeAcc02ACC04","ACC04_VAT_REGISTERS.ACCOUNT_CODE_ACC02");
			attribute2dbField.put("registerTypeACC04","ACC04_VAT_REGISTERS.REGISTER_TYPE");
			attribute2dbField.put("readOnlyACC04","ACC04_VAT_REGISTERS.READ_ONLY");

			ArrayList values = new ArrayList();
			values.add(serverLanguageId);
			values.add(serverLanguageId);

			// read from ACC04 table...
			Response answer = CustomizeQueryUtil.getQuery(
					conn,
					new UserSessionParameters(username),
					sql,
					values,
					attribute2dbField,
					VatRegisterVO.class,
					"Y",
					"N",
					null,
					gridParams,
					true,
					customizedFields
			);
			if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching vat register list",ex);
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
	public VOListResponse insertVatRegisters(ArrayList list,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;
			VatRegisterVO vo = null;
			BigDecimal progressiveSYS10 = null;

			Map attribute2dbField = new HashMap();
			attribute2dbField.put("registerCodeACC04","REGISTER_CODE");
			attribute2dbField.put("progressiveSys10ACC04","PROGRESSIVE_SYS10");
			attribute2dbField.put("enabledACC04","ENABLED");
			attribute2dbField.put("companyCodeSys01ACC04","COMPANY_CODE_SYS01");
			attribute2dbField.put("accountCodeAcc02ACC04","ACCOUNT_CODE_ACC02");
			attribute2dbField.put("registerTypeACC04","REGISTER_TYPE");
			attribute2dbField.put("readOnlyACC04","READ_ONLY");

			Response res = null;
			for(int i=0;i<list.size();i++) {
				vo = (VatRegisterVO)list.get(i);
				vo.setEnabledACC04("Y");

				// insert record in SYS10...
				progressiveSYS10 = TranslationUtils.insertTranslations(vo.getDescriptionSYS10(),vo.getCompanyCodeSys01ACC04(),conn);
				vo.setProgressiveSys10ACC04(progressiveSYS10);

				// insert into ACC04...
				res = CustomizeQueryUtil.insertTable(
						conn,
						new UserSessionParameters(username),
						vo,
						"ACC04_VAT_REGISTERS",
						attribute2dbField,
						"Y",
						"N",
						null,
						true,
						customizedFields
				);
				if (res.isError()) {
					throw new Exception(res.getErrorMessage());
				}
			}


			return new VOListResponse(list,false,list.size());
		}
		catch (Throwable ex) {
			Logger.error(username, this.getClass().getName(),
					"executeCommand", "Error while inserting new vatRegisters", ex);
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
	public VOListResponse updateVatRegisters(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;
			VatRegisterVO oldVO = null;
			VatRegisterVO newVO = null;
			Response res = null;

			for(int i=0;i<oldVOs.size();i++) {
				oldVO = (VatRegisterVO)oldVOs.get(i);
				newVO = (VatRegisterVO)newVOs.get(i);

				// update SYS10 table...
				TranslationUtils.updateTranslation(oldVO.getDescriptionSYS10(),newVO.getDescriptionSYS10(),newVO.getProgressiveSys10ACC04(),serverLanguageId,conn);

				HashSet pkAttrs = new HashSet();
				pkAttrs.add("companyCodeSys01ACC04");
				pkAttrs.add("vatRegisterCodeACC04");

				HashMap attribute2dbField = new HashMap();
				attribute2dbField.put("registerCodeACC04","REGISTER_CODE");
				attribute2dbField.put("progressiveSys10ACC04","PROGRESSIVE_SYS10");
				attribute2dbField.put("enabledACC04","ENABLED");
				attribute2dbField.put("companyCodeSys01ACC04","COMPANY_CODE_SYS01");
				attribute2dbField.put("accountCodeAcc02ACC04","ACCOUNT_CODE_ACC02");
				attribute2dbField.put("registerTypeACC04","REGISTER_TYPE");
				attribute2dbField.put("readOnlyACC04","READ_ONLY");

				res = new CustomizeQueryUtil().updateTable(
						conn,
						new UserSessionParameters(username),
						pkAttrs,
						oldVO,
						newVO,
						"ACC04_VAT_REGISTERS",
						attribute2dbField,
						"Y",
						"N",
						null,
						true,
						customizedFields
				);
				if (res.isError()) {
					throw new Exception(res.getErrorMessage());
				}
			}

			return new VOListResponse(newVOs,false,newVOs.size());
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing vat register",ex);
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
	public VOResponse deleteVatRegisters(ArrayList list,String serverLanguageId,String username) throws Throwable {
		Statement stmt = null;
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;
			stmt = conn.createStatement();

			VatRegisterVO vo = null;
			for(int i=0;i<list.size();i++) {
				// logically delete the record in ACC04...
				vo = (VatRegisterVO)list.get(i);
				stmt.execute("update ACC04_VAT_REGISTERS set ENABLED='N' where "+
						"COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01ACC04()+"' and REGISTER_CODE='"+vo.getRegisterCodeACC04()+"'");
			}

			return new VOResponse(new Boolean(true));
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing vat register",ex);
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
	  public VOResponse vatEndorse(HashMap map,String t1,String t2,String t3,String serverLanguageId,String username) throws Throwable {
	    PreparedStatement pstmt = null;
	    PreparedStatement pstmt2 = null;
	    
	    Connection conn = null;
	    try {
	      if (this.conn==null) conn = getConn(); else conn = this.conn;
	      bean.setConn(conn); // use same transaction...
	      userParamAction.setConn(conn); // use same transaction...

	      String companyCode = (String)map.get(ApplicationConsts.COMPANY_CODE_SYS01);
	      java.util.Date toDate = (java.util.Date)map.get(ApplicationConsts.DATE_FILTER);

	      // retrieve last record number/account code for each vat register...
	      String sql = "select REGISTER_CODE,LAST_RECORD_NUMBER,LAST_VAT_DATE,ACCOUNT_CODE_ACC02 from ACC04_VAT_REGISTERS where COMPANY_CODE_SYS01=?";
	      pstmt = conn.prepareStatement(sql);
	      pstmt.setString(1,companyCode);
	      ResultSet rset = pstmt.executeQuery();
	      Hashtable lastRecordNumbers = new Hashtable();
	      Hashtable lastVatDates = new Hashtable();
	      Hashtable accountCodes = new Hashtable();
	      String regCode = null;
	      BigDecimal lastRN = null;
	      java.sql.Date lastVD = null;
	      while(rset.next()) {
	        regCode = rset.getString(1);
	        lastRN = rset.getBigDecimal(2);
	        lastVD = rset.getDate(3);
	        if (lastRN==null)
	          lastRN = new BigDecimal(0);
	        lastRecordNumbers.put(regCode,lastRN);
	        if (lastVD==null) {
	          Calendar cal = Calendar.getInstance();
	          cal.set(cal.MONTH,0);
	          cal.set(cal.DAY_OF_MONTH,1);
	          lastVD = new java.sql.Date(cal.getTimeInMillis());
	        }
	        lastVatDates.put(regCode,lastVD);
	        accountCodes.put(regCode,rset.getString(4));
	      }
	      rset.close();
	      pstmt.close();

	      // retrieve vat endorse account code...
	      HashMap params = new HashMap();
	      params.put(ApplicationConsts.COMPANY_CODE_SYS01,companyCode);
	      params.put(ApplicationConsts.PARAM_CODE,ApplicationConsts.VAT_ENDORSE_ACCOUNT);
	      Response res = userParamAction.loadUserParam(params,serverLanguageId,username);
	      if (res.isError()) {
	        throw new Exception(res.getErrorMessage());
	      }
	      String vatEndorseAccountCode = ((VOResponse)res).getVo().toString();

	      SimpleDateFormat sdf = new SimpleDateFormat(t1);

	      // for each vat register: create an accounting item to endorse vat amount from the register to th tresury...
	      Enumeration en = lastRecordNumbers.keys();
	      sql =
	          "select VAT_VALUE,RECORD_NUMBER from ACC07_VAT_ROWS where COMPANY_CODE_SYS01=? and REGISTER_CODE_ACC04=? and "+
	          "RECORD_NUMBER>? and VAT_DATE<=?";
	      pstmt = conn.prepareStatement(sql);
	      BigDecimal amount = null;
	      BigDecimal newLastRN = null;
	      JournalHeaderVO jhVO = null;
	      JournalRowVO jrVO = null;
	      String accountCode = null;

	      sql = "update ACC04_VAT_REGISTERS set LAST_RECORD_NUMBER=?,LAST_VAT_DATE=? where COMPANY_CODE_SYS01=? and REGISTER_CODE=? and (LAST_RECORD_NUMBER=? || LAST_RECORD_NUMBER is null)";
	      pstmt2 = conn.prepareStatement(sql);

	      while(en.hasMoreElements()) {
	        // retrieve total vat amount for the specified vat register...
	        amount = new BigDecimal(0);
	        regCode = en.nextElement().toString();
	        lastRN = (BigDecimal)lastRecordNumbers.get(regCode);
	        lastVD = (java.sql.Date)lastVatDates.get(regCode);
	        accountCode = (String)accountCodes.get(regCode);
	        pstmt.setString(1,companyCode);
	        pstmt.setString(2,regCode);
	        pstmt.setBigDecimal(3,lastRN);
	        pstmt.setDate(4,new java.sql.Date(toDate.getTime()));
	        rset = pstmt.executeQuery();
	        while(rset.next()) {
	          amount = amount.add(rset.getBigDecimal(1));
	          newLastRN = rset.getBigDecimal(2);
	        }
	        rset.close();

	        if (amount.doubleValue()!=0) {
	          lastVD.setTime(lastVD.getTime()+86400*1000);

	          // create the accounting item...
	          jhVO = new JournalHeaderVO();
	          jhVO.setCompanyCodeSys01ACC05(companyCode);
	          jhVO.setAccountingMotiveCodeAcc03ACC05(ApplicationConsts.MOTIVE_VAT_ENDORSING);
	          jhVO.setDescriptionACC05(
	            t2+" "+
	            regCode+" "+
	            t3+" "+sdf.format(lastVD)+" - "+sdf.format(toDate)

	          );
	          jhVO.setItemDateACC05(new java.sql.Date(System.currentTimeMillis()));
	          jhVO.setItemYearACC05(new BigDecimal(Calendar.getInstance().get(Calendar.YEAR)));

	          jrVO = new JournalRowVO();
	          jrVO.setCompanyCodeSys01ACC06(jhVO.getCompanyCodeSys01ACC05());
	          jrVO.setAccountCodeAcc02ACC06(accountCode);
	          jrVO.setAccountCodeACC06(accountCode);
	          jrVO.setAccountCodeTypeACC06(ApplicationConsts.ACCOUNT_TYPE_ACCOUNT);
	          jrVO.setDebitAmountACC06(amount);
	          jrVO.setDescriptionACC06("");
	          jrVO.setItemYearAcc05ACC06(jhVO.getItemYearACC05());
	          jrVO.setProgressiveAcc05ACC06(jhVO.getProgressiveACC05());
	          jhVO.addJournalRow(jrVO);

	          jrVO = new JournalRowVO();
	          jrVO.setCompanyCodeSys01ACC06(jhVO.getCompanyCodeSys01ACC05());
	          jrVO.setAccountCodeAcc02ACC06(vatEndorseAccountCode);
	          jrVO.setAccountCodeACC06(vatEndorseAccountCode);
	          jrVO.setAccountCodeTypeACC06(ApplicationConsts.ACCOUNT_TYPE_ACCOUNT);
	          jrVO.setCreditAmountACC06(amount);
	          jrVO.setDescriptionACC06("");
	          jrVO.setItemYearAcc05ACC06(jhVO.getItemYearACC05());
	          jrVO.setProgressiveAcc05ACC06(jhVO.getProgressiveACC05());
	          jhVO.addJournalRow(jrVO);

	          res = bean.insertJournalItem(jhVO,serverLanguageId,username);
	          if (res.isError()) {
	            throw new Exception(res.getErrorMessage());
	          }

	          // update last record number in the current vat register...
	          pstmt2.setBigDecimal(1,newLastRN);
	          pstmt2.setDate(2,new java.sql.Date(toDate.getTime()));
	          pstmt2.setString(3,companyCode);
	          pstmt2.setString(4,regCode);
	          pstmt2.setBigDecimal(5,lastRN);
	          int upd = pstmt2.executeUpdate();
	          if (upd==0) {
	            return new VOResponse("Updating not performed: the record was previously updated.");
	          }

	        }
	      }

	      Response answer = new VOResponse(Boolean.TRUE);
	      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOResponse)answer;
	    }
	    catch (Throwable ex) {
	      Logger.error(username,this.getClass().getName(),"executeCommand","Error while endorsing vat",ex);
	      try {
	    	  if (this.conn==null && conn!=null)
	    		  // rollback only local connection
	    		  conn.rollback();
	      }
	      catch (Exception ex3) {
	      }
	      throw new Exception(ex.toString());
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

          try {
        	  bean.setConn(null);
	        userParamAction.setConn(null);
	      } catch (Exception ex) {}
	    }

	  }



}

