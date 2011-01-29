package org.jallinone.accounting.ledger.server;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.sql.DataSource;

import org.jallinone.accounting.ledger.java.LedgerVO;
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
 * * <p>Description: Bean used to manage the ledger.</p>
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
public class LedgerServicesBean  implements LedgerServices {


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




  public LedgerServicesBean() {
  }



	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
	 */
	public LedgerVO getLedger() {
		throw new UnsupportedOperationException();
	}
	

	/**
	 * Business logic to execute.
	 */
	public VOListResponse loadLedger(GridParams gridParams,String serverLanguageId,String username,ArrayList companiesList,ArrayList customizedFields) throws Throwable {
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
				"select ACC01_LEDGER.COMPANY_CODE_SYS01,ACC01_LEDGER.LEDGER_CODE,ACC01_LEDGER.PROGRESSIVE_SYS10,SYS10_TRANSLATIONS.DESCRIPTION,ACC01_LEDGER.ENABLED,ACC01_LEDGER.ACCOUNT_TYPE from ACC01_LEDGER,SYS10_TRANSLATIONS where "+
				"ACC01_LEDGER.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
				"SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
				"ACC01_LEDGER.ENABLED='Y' and ACC01_LEDGER.COMPANY_CODE_SYS01 in ("+companies+")";

			Map attribute2dbField = new HashMap();
			attribute2dbField.put("ledgerCodeACC01","ACC01_LEDGER.LEDGER_CODE");
			attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
			attribute2dbField.put("progressiveSys10ACC01","ACC01_LEDGER.PROGRESSIVE_SYS10");
			attribute2dbField.put("enabledACC01","ACC01_LEDGER.ENABLED");
			attribute2dbField.put("companyCodeSys01ACC01","ACC01_LEDGER.COMPANY_CODE_SYS01");
			attribute2dbField.put("accountTypeACC01","ACC01_LEDGER.ACCOUNT_TYPE");

			ArrayList values = new ArrayList();
			values.add(serverLanguageId);


			// read from ACC01 table...
			Response answer = CustomizeQueryUtil.getQuery(
					conn,
					new UserSessionParameters(username),
					sql,
					values,
					attribute2dbField,
					LedgerVO.class,
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
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching the ledger",ex);
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
	public VOListResponse validateLedgerCode(LookupValidationParams validationPars,String serverLanguageId,String username,ArrayList companiesList,ArrayList customizedFields) throws Throwable {
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
				"select ACC01_LEDGER.COMPANY_CODE_SYS01,ACC01_LEDGER.LEDGER_CODE,ACC01_LEDGER.PROGRESSIVE_SYS10,SYS10_TRANSLATIONS.DESCRIPTION,ACC01_LEDGER.ENABLED,ACC01_LEDGER.ACCOUNT_TYPE from ACC01_LEDGER,SYS10_TRANSLATIONS where "+
				"ACC01_LEDGER.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
				"SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
				"ACC01_LEDGER.ENABLED='Y' and "+
				"ACC01_LEDGER.LEDGER_CODE='"+validationPars.getCode()+"' and ACC01_LEDGER.COMPANY_CODE_SYS01 in ("+companies+")";

			Map attribute2dbField = new HashMap();
			attribute2dbField.put("ledgerCodeACC01","ACC01_LEDGER.LEDGER_CODE");
			attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
			attribute2dbField.put("progressiveSys10ACC01","ACC01_LEDGER.PROGRESSIVE_SYS10");
			attribute2dbField.put("enabledACC01","ACC01_LEDGER.ENABLED");
			attribute2dbField.put("companyCodeSys01ACC01","ACC01_LEDGER.COMPANY_CODE_SYS01");
			attribute2dbField.put("accountTypeACC01","ACC01_LEDGER.ACCOUNT_TYPE");

			ArrayList values = new ArrayList();
			values.add(serverLanguageId);

			GridParams gridParams = new GridParams();

			// read from ACC01 table...
			Response answer = CustomizeQueryUtil.getQuery(
					conn,
					new UserSessionParameters(username),
					sql,
					values,
					attribute2dbField,
					LedgerVO.class,
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
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while validating ledger code",ex);
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
	public VOListResponse insertLedger(ArrayList list,String serverLanguageId,String username,ArrayList companyCodes,ArrayList customizedFields) throws Throwable {

		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;
			String companyCode = companyCodes.get(0).toString();

			LedgerVO vo = null;

			BigDecimal progressiveSYS10 = null;
			Response res = null;

			Map attribute2dbField = new HashMap();
			attribute2dbField.put("ledgerCodeACC01","LEDGER_CODE");
			attribute2dbField.put("progressiveSys10ACC01","PROGRESSIVE_SYS10");
			attribute2dbField.put("enabledACC01","ENABLED");
			attribute2dbField.put("companyCodeSys01ACC01","COMPANY_CODE_SYS01");
			attribute2dbField.put("accountTypeACC01","ACCOUNT_TYPE");

			for(int i=0;i<list.size();i++) {
				vo = (LedgerVO)list.get(i);
				vo.setEnabledACC01("Y");
				if (vo.getCompanyCodeSys01ACC01() == null)
					vo.setCompanyCodeSys01ACC01(companyCode);

				// insert record in SYS10...
				progressiveSYS10 = TranslationUtils.insertTranslations(vo.getDescriptionSYS10(),vo.getCompanyCodeSys01ACC01(),conn);
				vo.setProgressiveSys10ACC01(progressiveSYS10);

				// insert into ACC01...
				res = CustomizeQueryUtil.insertTable(
						conn,
						new UserSessionParameters(username),
						vo,
						"ACC01_LEDGER",
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
					"executeCommand", "Error while inserting new ledgers", ex);
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
	public VOListResponse updateLedger(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;
		LedgerVO oldVO = null;
			LedgerVO newVO = null;
			Response res = null;

			for(int i=0;i<oldVOs.size();i++) {
				oldVO = (LedgerVO)oldVOs.get(i);
				newVO = (LedgerVO)newVOs.get(i);

				// update SYS10 table...
				TranslationUtils.updateTranslation(oldVO.getDescriptionSYS10(),newVO.getDescriptionSYS10(),newVO.getProgressiveSys10ACC01(),serverLanguageId,conn);

				HashSet pkAttrs = new HashSet();
				pkAttrs.add("companyCodeSys01ACC01");
				pkAttrs.add("ledgerCodeACC01");

				HashMap attribute2dbField = new HashMap();
				attribute2dbField.put("ledgerCodeACC01","LEDGER_CODE");
				attribute2dbField.put("progressiveSys10ACC01","PROGRESSIVE_SYS10");
				attribute2dbField.put("enabledACC01","ENABLED");
				attribute2dbField.put("companyCodeSys01ACC01","COMPANY_CODE_SYS01");
				attribute2dbField.put("accountTypeACC01","ACCOUNT_TYPE");

				res = new CustomizeQueryUtil().updateTable(
						conn,
						new UserSessionParameters(username),
						pkAttrs,
						oldVO,
						newVO,
						"ACC01_LEDGER",
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
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing ledger",ex);
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
	public VOResponse deleteLedger(ArrayList list,String serverLanguageId,String username) throws Throwable {
		Statement stmt = null;
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;
			stmt = conn.createStatement();

			LedgerVO vo = null;
			for(int i=0;i<list.size();i++) {
				// logically delete the record in ACC01...
				vo = (LedgerVO)list.get(i);
				stmt.execute("update ACC01_LEDGER set ENABLED='N' where COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01ACC01()+"' and LEDGER_CODE='"+vo.getLedgerCodeACC01()+"'");
			}

			return new VOResponse(new Boolean(true));
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing ledger",ex);
		      try {
		    	  if (this.conn==null && conn!=null)
		    		  // rollback only local connection
		    		  conn.rollback();
		      }
		      catch (Exception ex3) {
		      }			throw new Exception(ex.getMessage());
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

