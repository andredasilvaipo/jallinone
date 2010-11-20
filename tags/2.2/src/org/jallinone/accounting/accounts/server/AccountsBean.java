package org.jallinone.accounting.accounts.server;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.sql.DataSource;

import org.jallinone.accounting.accounts.java.AccountVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.system.server.JAIOUserSessionParameters;
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
 * * <p>Description: Bean used to manage accounts.</p>
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
public class AccountsBean  implements Accounts {


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




  public AccountsBean() {
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public AccountVO getAccount() {
	  throw new UnsupportedOperationException();
  }


  /**
	 * Business logic to execute.
	 */
	public VOListResponse validateAccountCode(LookupValidationParams validationPars,String serverLanguageId,String username,ArrayList companiesList,ArrayList customizedFields) throws Throwable {
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
				"select ACC02_ACCOUNTS.ACCOUNT_CODE,ACC02_ACCOUNTS.PROGRESSIVE_SYS10,SYS10_TRANSLATIONS.DESCRIPTION,ACC02_ACCOUNTS.ENABLED,"+
				"ACC02_ACCOUNTS.COMPANY_CODE_SYS01,ACC02_ACCOUNTS.ACCOUNT_TYPE,ACC02_ACCOUNTS.LEDGER_CODE_ACC01,SYS10_B.DESCRIPTION,ACC02_ACCOUNTS.CAN_DEL "+
				" from ACC02_ACCOUNTS,SYS10_TRANSLATIONS,SYS10_TRANSLATIONS SYS10_B,ACC01_LEDGER where "+
				"ACC02_ACCOUNTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
				"SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
				"ACC02_ACCOUNTS.COMPANY_CODE_SYS01=ACC01_LEDGER.COMPANY_CODE_SYS01 and "+
				"ACC02_ACCOUNTS.LEDGER_CODE_ACC01=ACC01_LEDGER.LEDGER_CODE and "+
				"ACC01_LEDGER.PROGRESSIVE_SYS10=SYS10_B.PROGRESSIVE and "+
				"SYS10_B.LANGUAGE_CODE=? and "+
				"ACC02_ACCOUNTS.COMPANY_CODE_SYS01 in ("+companies+") and "+
				"ACC02_ACCOUNTS.ACCOUNT_CODE='"+validationPars.getCode()+"'";

			if (validationPars.getLookupValidationParameters().get(ApplicationConsts.DO_NOT_ADD_ENABLED)==null)
				sql += " and ACC02_ACCOUNTS.ENABLED='Y' ";


			if (validationPars.getLookupValidationParameters().get(ApplicationConsts.LEDGER_CODE)!=null)
				sql += " and ACC02_ACCOUNTS.LEDGER_CODE_ACC01='"+validationPars.getLookupValidationParameters().get(ApplicationConsts.LEDGER_CODE)+"' ";

			Map attribute2dbField = new HashMap();
			attribute2dbField.put("accountCodeACC02","ACC02_ACCOUNTS.ACCOUNT_CODE");
			attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
			attribute2dbField.put("progressiveSys10ACC02","ACC02_ACCOUNTS.PROGRESSIVE_SYS10");
			attribute2dbField.put("enabledACC02","ACC02_ACCOUNTS.ENABLED");
			attribute2dbField.put("companyCodeSys01ACC02","ACC02_ACCOUNTS.COMPANY_CODE_SYS01");
			attribute2dbField.put("accountTypeACC02","ACC02_ACCOUNTS.ACCOUNT_TYPE");
			attribute2dbField.put("ledgerCodeAcc01ACC02","ACC02_ACCOUNTS.LEDGER_CODE_ACC01");
			attribute2dbField.put("ledgerDescriptionACC02","SYS10_B.DESCRIPTION");
			attribute2dbField.put("canDelACC02","ACC02_ACCOUNTS.CAN_DEL");

			ArrayList values = new ArrayList();
			values.add(serverLanguageId);
			values.add(serverLanguageId);

			GridParams gridParams = new GridParams();

			// read from ACC02 table...
			Response answer = CustomizeQueryUtil.getQuery(
					conn,
					new UserSessionParameters(username),
					sql,
					values,
					attribute2dbField,
					AccountVO.class,
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
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while validating account code",ex);
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
	public VOListResponse loadAccounts(GridParams gridParams,String serverLanguageId,String username,ArrayList companiesList,ArrayList customizedFields) throws Throwable {
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
				"select ACC02_ACCOUNTS.ACCOUNT_CODE,ACC02_ACCOUNTS.PROGRESSIVE_SYS10,SYS10_TRANSLATIONS.DESCRIPTION,ACC02_ACCOUNTS.ENABLED,"+
				"ACC02_ACCOUNTS.COMPANY_CODE_SYS01,ACC02_ACCOUNTS.ACCOUNT_TYPE,ACC02_ACCOUNTS.LEDGER_CODE_ACC01,SYS10_B.DESCRIPTION,ACC02_ACCOUNTS.CAN_DEL "+
				" from ACC02_ACCOUNTS,SYS10_TRANSLATIONS,SYS10_TRANSLATIONS SYS10_B,ACC01_LEDGER where "+
				"ACC02_ACCOUNTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
				"SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
				"ACC02_ACCOUNTS.COMPANY_CODE_SYS01=ACC01_LEDGER.COMPANY_CODE_SYS01 and "+
				"ACC02_ACCOUNTS.LEDGER_CODE_ACC01=ACC01_LEDGER.LEDGER_CODE and "+
				"ACC01_LEDGER.PROGRESSIVE_SYS10=SYS10_B.PROGRESSIVE and "+
				"SYS10_B.LANGUAGE_CODE=? and "+
				"ACC02_ACCOUNTS.ENABLED='Y' and "+
				"ACC02_ACCOUNTS.COMPANY_CODE_SYS01 in ("+companies+") ";

			if (gridParams.getOtherGridParams().get(ApplicationConsts.LEDGER_CODE)!=null)
				sql += " and ACC02_ACCOUNTS.LEDGER_CODE_ACC01='"+gridParams.getOtherGridParams().get(ApplicationConsts.LEDGER_CODE)+"' ";

			Map attribute2dbField = new HashMap();
			attribute2dbField.put("accountCodeACC02","ACC02_ACCOUNTS.ACCOUNT_CODE");
			attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
			attribute2dbField.put("progressiveSys10ACC02","ACC02_ACCOUNTS.PROGRESSIVE_SYS10");
			attribute2dbField.put("enabledACC02","ACC02_ACCOUNTS.ENABLED");
			attribute2dbField.put("companyCodeSys01ACC02","ACC02_ACCOUNTS.COMPANY_CODE_SYS01");
			attribute2dbField.put("accountTypeACC02","ACC02_ACCOUNTS.ACCOUNT_TYPE");
			attribute2dbField.put("ledgerCodeAcc01ACC02","ACC02_ACCOUNTS.LEDGER_CODE_ACC01");
			attribute2dbField.put("ledgerDescriptionACC02","SYS10_B.DESCRIPTION");
			attribute2dbField.put("canDelACC02","ACC02_ACCOUNTS.CAN_DEL");

			ArrayList values = new ArrayList();
			values.add(serverLanguageId);
			values.add(serverLanguageId);

			// read from ACC02 table...
			Response answer = CustomizeQueryUtil.getQuery(
					conn,
					new UserSessionParameters(username),
					sql,
					values,
					attribute2dbField,
					AccountVO.class,
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
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching accounts list",ex);
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
	public VOListResponse insertAccounts(ArrayList rows,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;

			Response res = null;
			AccountVO vo = null;
			BigDecimal progressiveSYS10 = null;

			Map attribute2dbField = new HashMap();
			attribute2dbField.put("accountCodeACC02","ACCOUNT_CODE");
			attribute2dbField.put("progressiveSys10ACC02","PROGRESSIVE_SYS10");
			attribute2dbField.put("enabledACC02","ENABLED");
			attribute2dbField.put("companyCodeSys01ACC02","COMPANY_CODE_SYS01");
			attribute2dbField.put("accountTypeACC02","ACCOUNT_TYPE");
			attribute2dbField.put("ledgerCodeAcc01ACC02","LEDGER_CODE_ACC01");
			attribute2dbField.put("canDelACC02","CAN_DEL");

			for(int i=0;i<rows.size();i++) {
				vo = (AccountVO)rows.get(i);
				vo.setEnabledACC02("Y");

				if (vo.getCanDelACC02()==null)
					vo.setCanDelACC02(new Boolean(true));

				// insert record in SYS10...
				progressiveSYS10 = TranslationUtils.insertTranslations(vo.getDescriptionSYS10(),vo.getCompanyCodeSys01ACC02(),conn);
				vo.setProgressiveSys10ACC02(progressiveSYS10);

				// insert into ACC02...
				res = CustomizeQueryUtil.insertTable(
						conn,
						new UserSessionParameters(username),
						vo,
						"ACC02_ACCOUNTS",
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


			return new VOListResponse(rows,false,rows.size());
		}
		catch (Throwable ex) {
			Logger.error(username, this.getClass().getName(),
					"executeCommand", "Error while inserting new accounts", ex);
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
	public VOListResponse updateAccounts(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;

			AccountVO oldVO = null;
			AccountVO newVO = null;
			Response res = null;

			for(int i=0;i<oldVOs.size();i++) {
				oldVO = (AccountVO)oldVOs.get(i);
				newVO = (AccountVO)newVOs.get(i);

				// update SYS10 table...
				TranslationUtils.updateTranslation(oldVO.getDescriptionSYS10(),newVO.getDescriptionSYS10(),newVO.getProgressiveSys10ACC02(),serverLanguageId,conn);

				HashSet pkAttrs = new HashSet();
				pkAttrs.add("companyCodeSys01ACC02");
				pkAttrs.add("accountCodeACC02");

				HashMap attribute2dbField = new HashMap();
				attribute2dbField.put("accountCodeACC02","ACCOUNT_CODE");
				attribute2dbField.put("progressiveSys10ACC02","PROGRESSIVE_SYS10");
				attribute2dbField.put("enabledACC02","ENABLED");
				attribute2dbField.put("companyCodeSys01ACC02","COMPANY_CODE_SYS01");
				attribute2dbField.put("accountTypeACC02","ACCOUNT_TYPE");
				attribute2dbField.put("ledgerCodeAcc01ACC02","LEDGER_CODE_ACC01");
				attribute2dbField.put("canDelACC02","CAN_DEL");

				res = new CustomizeQueryUtil().updateTable(
						conn,
						new UserSessionParameters(username),
						pkAttrs,
						oldVO,
						newVO,
						"ACC02_ACCOUNTS",
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
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing accounts",ex);
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
	public VOResponse deleteAccounts(ArrayList list,String serverLanguageId,String username) throws Throwable {
		Statement stmt = null;

		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;
			stmt = conn.createStatement();

			AccountVO vo = null;
			for(int i=0;i<list.size();i++) {
				// logically delete the record in ACC02...
				vo = (AccountVO)list.get(i);
				stmt.execute("update ACC02_ACCOUNTS set ENABLED='N' where "+
						"COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01ACC02()+"' and ACCOUNT_CODE='"+vo.getAccountCodeACC02()+"'");
			}

			return new VOResponse(new Boolean(true));
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing accounts",ex);
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



}

