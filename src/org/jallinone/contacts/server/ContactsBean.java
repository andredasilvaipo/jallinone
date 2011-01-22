package org.jallinone.contacts.server;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.sql.DataSource;

import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.contacts.java.GridContactVO;
import org.jallinone.subjects.java.OrganizationVO;
import org.jallinone.subjects.java.PeopleVO;
import org.jallinone.subjects.java.SubjectPK;
import org.jallinone.subjects.java.SubjectVO;
import org.jallinone.subjects.server.OrganizationBean;
import org.jallinone.subjects.server.PeopleBean;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.openswing.swing.logger.server.Logger;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.message.receive.java.VOResponse;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.server.QueryUtil;
import org.openswing.swing.server.UserSessionParameters;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Bean used to manage contacts and in case of organization, 
 * then manage also linked people contacts.</p>
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
public class ContactsBean  implements Contacts {


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


	private PeopleBean peopleBean;

	private OrganizationBean organizationBean;

	public void setPeopleBean(PeopleBean peopleBean) {
		this.peopleBean = peopleBean;
	}

	public void setOrganizationBean(OrganizationBean organizationBean) {
		this.organizationBean = organizationBean;
	}

	private ContactBean bean;
	
	public void setBean(ContactBean bean) {
		this.bean = bean;
	}
	
	

	public ContactsBean() {
	}


	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
	 */
	public GridContactVO getGridContactVO() {
		throw new UnsupportedOperationException();	  
	}


	/**
	 * Business logic to execute.
	 */
	public VOListResponse loadContacts(GridParams gridParams,String serverLanguageId,String username,ArrayList companiesList) throws Throwable {
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;
			bean.setConn(conn);

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
				"select REG04_SUBJECTS.COMPANY_CODE_SYS01,REG04_SUBJECTS.PROGRESSIVE,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,REG04_SUBJECTS.PROGRESSIVE_REG04,"+
				"REG04_SUBJECTS.PHONE_NUMBER,REG04_SUBJECTS.SUBJECT_TYPE,REG04_SUBJECTS.CITY,REG04_SUBJECTS.PROVINCE,REG04_SUBJECTS.COUNTRY "+
				"from REG04_SUBJECTS where "+
				"REG04_SUBJECTS.COMPANY_CODE_SYS01 in ("+companies+") and REG04_SUBJECTS.ENABLED='Y'";

			Map attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01REG04","REG04_SUBJECTS.COMPANY_CODE_SYS01");
			attribute2dbField.put("progressiveREG04","REG04_SUBJECTS.PROGRESSIVE");
			attribute2dbField.put("name_1REG04","REG04_SUBJECTS.NAME_1");
			attribute2dbField.put("name_2REG04","REG04_SUBJECTS.NAME_2");
			attribute2dbField.put("phoneNumberREG04","REG04_SUBJECTS.PHONE_NUMBER");
			attribute2dbField.put("subjectTypeREG04","REG04_SUBJECTS.SUBJECT_TYPE");
			attribute2dbField.put("cityREG04","REG04_SUBJECTS.CITY");
			attribute2dbField.put("provinceREG04","REG04_SUBJECTS.PROVINCE");
			attribute2dbField.put("countryREG04","REG04_SUBJECTS.COUNTRY");
			attribute2dbField.put("progressiveReg04REG04","REG04_SUBJECTS.PROGRESSIVE_REG04");

			ArrayList values = new ArrayList();

			if (gridParams.getOtherGridParams().get(ApplicationConsts.SUBJECT_PK)!=null) {
				sql += " and REG04_SUBJECTS.COMPANY_CODE_SYS01_REG04=? "+
				" and REG04_SUBJECTS.PROGRESSIVE_REG04=?";
				SubjectPK pk = (SubjectPK)gridParams.getOtherGridParams().get(ApplicationConsts.SUBJECT_PK);
				values.add(pk.getCompanyCodeSys01REG04());
				values.add(pk.getProgressiveREG04());
			}
			else {
				sql += " and REG04_SUBJECTS.SUBJECT_TYPE in (?,?)";
				values.add(ApplicationConsts.SUBJECT_ORGANIZATION_CONTACT);
				values.add(ApplicationConsts.SUBJECT_PEOPLE_CONTACT);
			}

			// read from REG04 table...
			Response res = QueryUtil.getQuery(
					conn,
					new UserSessionParameters(username),
					sql,
					values,
					attribute2dbField,
					GridContactVO.class,
					"Y",
					"N",
					null,
					gridParams,
					50,
					true
			);

			if (res.isError())
				throw new Exception(res.getErrorMessage());

			java.util.List rows = ((VOListResponse)res).getRows();
			GridContactVO vo = null;
			Response contactRes = null;
			SubjectVO subVO = null;
			OrganizationVO orgVO = null;
			
			for(int i=0;i<rows.size();i++) {
				vo = (GridContactVO)rows.get(i);
				if (vo.getProgressiveReg04REG04()!=null) {
					subVO = new SubjectVO(
							vo.getCompanyCodeSys01REG04(),
							vo.getProgressiveReg04REG04(),
							null, // it's not very good...
							null, // it's not very good...
							ApplicationConsts.SUBJECT_ORGANIZATION_CONTACT
					);
					contactRes = bean.loadContact(subVO,serverLanguageId,username);
					if (contactRes.isError())
						throw new Exception(contactRes.getErrorMessage());
					orgVO = (OrganizationVO)((VOResponse)contactRes).getVo();
					vo.setOrganizationName_1REG04(orgVO.getName_1REG04());
				}
			}

			return (VOListResponse)res;
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching contacts list",ex);
			throw new Exception(ex.getMessage());
		}
		finally {

			try {
				bean.setConn(null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
	public VOResponse updateOrganization(OrganizationVO oldVO,OrganizationVO newVO,String t1,String t2,String serverLanguageId,String username) throws Throwable {
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;
			peopleBean.setConn(conn);
			organizationBean.setConn(conn);

			Response res = null;
			// update REG04...
			res = organizationBean.update((OrganizationVO)oldVO,(OrganizationVO)newVO,t2,serverLanguageId,username);
			if (res.isError()) {
				throw new Exception(res.getErrorMessage());
			}
			else
				return (VOResponse)res;
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating an existing contact",ex);
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
			try {
				peopleBean.setConn(null);
				organizationBean.setConn(null);
			} catch (Exception ex) {}
		}
	}



	/**
	 * Business logic to execute.
	 */
	public VOResponse updatePeople(PeopleVO oldVO,PeopleVO newVO,String t1,String t2,String serverLanguageId,String username) throws Throwable {
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;
			peopleBean.setConn(conn);
			organizationBean.setConn(conn);

			Response res = null;
			// update REG04...
			res = peopleBean.update((PeopleVO)oldVO,(PeopleVO)newVO,t1,serverLanguageId,username);
			if (res.isError()) {
				throw new Exception(res.getErrorMessage());
			}
			else
				return (VOResponse)res;
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating an existing contact",ex);
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
			try {
				peopleBean.setConn(null);
				organizationBean.setConn(null);
			} catch (Exception ex) {}
		}
	}




	/**
	 * Business logic to execute.
	 */
	public VOResponse insertOrganization(OrganizationVO vo,String t1,String t2,String serverLanguageId,String username,ArrayList companyCodes) throws Throwable {
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;
			peopleBean.setConn(conn);
			organizationBean.setConn(conn);
			String companyCode = companyCodes.get(0).toString();

			if (vo.getCompanyCodeSys01REG04()==null)
				vo.setCompanyCodeSys01REG04(companyCode);

			// insert into REG04...
			organizationBean.insert(true,vo,t2,serverLanguageId,username);
			return new VOResponse(vo);
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while inserting a new contact",ex);
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
			try {
				peopleBean.setConn(null);
				organizationBean.setConn(null);
			} catch (Exception ex) {}
		}
	}


	/**
	 * Business logic to execute.
	 */
	public VOResponse insertPeople(PeopleVO vo,String t1,String t2,String serverLanguageId,String username,ArrayList companyCodes) throws Throwable {
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;
			peopleBean.setConn(conn);
			organizationBean.setConn(conn);
			String companyCode = companyCodes.get(0).toString();

			if (vo.getCompanyCodeSys01REG04()==null)
				vo.setCompanyCodeSys01REG04(companyCode);

			// insert into REG04...
			peopleBean.insert(true,vo,t1,serverLanguageId,username);
			return new VOResponse(vo);
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while inserting a new contact",ex);
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
			try {
				peopleBean.setConn(null);
				organizationBean.setConn(null);
			} catch (Exception ex) {}
		}
	}





	/**
	 * Business logic to execute.
	 */
	public VOResponse deleteContact(SubjectPK vo,String serverLanguageId,String username) throws Throwable {
		Statement stmt = null;
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;
			stmt = conn.createStatement();

			// logically delete record in REG04...
			stmt.execute("update REG04_SUBJECTS set ENABLED='N' where COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01REG04()+"' and PROGRESSIVE="+vo.getProgressiveREG04());

			stmt.execute("update REG04_SUBJECTS set ENABLED='N' where COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01REG04()+"' and PROGRESSIVE_REG04="+vo.getProgressiveREG04());
			
			return new VOResponse(new Boolean(true));
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting an existing contact",ex);
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

