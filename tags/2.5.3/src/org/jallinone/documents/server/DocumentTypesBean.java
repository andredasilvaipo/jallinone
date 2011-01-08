package org.jallinone.documents.server;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.documents.java.DocumentTypeVO;
import org.jallinone.system.progressives.server.CompanyProgressiveUtils;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.system.translations.server.TranslationUtils;
import org.openswing.swing.logger.server.Logger;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.message.receive.java.VOResponse;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.server.QueryUtil;
import org.openswing.swing.server.UserSessionParameters;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage documents types.</p>
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
public class DocumentTypesBean  implements DocumentTypes {


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




	public DocumentTypesBean() {
	}




	/**
	 * Business logic to execute.
	 */
	public VOListResponse loadDocumentTypes(GridParams gridParams,String serverLanguageId,String username,ArrayList companiesList,ArrayList customizedFields) throws Throwable {
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;

			// retrieve companies list...
			String companies = "";
			for(int i=0;i<companiesList.size();i++)
				companies += "'"+companiesList.get(i).toString()+"',";
			companies = companies.substring(0,companies.length()-1);

			String sql =
				"select DOC16_DOC_HIERARCHY.COMPANY_CODE_SYS01,DOC16_DOC_HIERARCHY.PROGRESSIVE_HIE02,DOC16_DOC_HIERARCHY.PROGRESSIVE_SYS10,"+
				"DOC16_DOC_HIERARCHY.ENABLED,SYS10_TRANSLATIONS.DESCRIPTION,HIE02_HIERARCHIES.PROGRESSIVE_HIE01 "+
				"from DOC16_DOC_HIERARCHY,SYS10_TRANSLATIONS,HIE02_HIERARCHIES where "+
				"DOC16_DOC_HIERARCHY.PROGRESSIVE_HIE02=HIE02_HIERARCHIES.PROGRESSIVE and "+
				"HIE02_HIERARCHIES.PROGRESSIVE_HIE01=SYS10_TRANSLATIONS.PROGRESSIVE and "+
				"SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
				"DOC16_DOC_HIERARCHY.COMPANY_CODE_SYS01 in ("+companies+") and "+
				"DOC16_DOC_HIERARCHY.ENABLED='Y'";

			Map attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01DOC16","DOC16_DOC_HIERARCHY.COMPANY_CODE_SYS01");
			attribute2dbField.put("progressiveHie02DOC16","DOC16_DOC_HIERARCHY.PROGRESSIVE_HIE02");
			attribute2dbField.put("enabledDOC16","DOC16_DOC_HIERARCHY.ENABLED");
			attribute2dbField.put("progressiveHie01HIE02","HIE02_HIERARCHIES.PROGRESSIVE_HIE01");
			attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
			attribute2dbField.put("progressiveSys10DOC16","DOC16_DOC_HIERARCHY.PROGRESSIVE_SYS10");

			ArrayList values = new ArrayList();
			values.add(serverLanguageId);


			// read from DOC16 table...
			Response answer = CustomizeQueryUtil.getQuery(
					conn,
					new UserSessionParameters(username),
					sql,
					values,
					attribute2dbField,
					DocumentTypeVO.class,
					"Y",
					"N",
					null,
					gridParams,
					true,
					customizedFields // window identifier...
			);

			if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching documents types list",ex);
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
	public VOListResponse updateDocumentTypes(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {

		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;
			DocumentTypeVO oldVO = null;
			DocumentTypeVO newVO = null;

			for(int i=0;i<oldVOs.size();i++) {
				oldVO = (DocumentTypeVO)oldVOs.get(i);
				newVO = (DocumentTypeVO)newVOs.get(i);

				// update root description...
				TranslationUtils.updateTranslation(
						oldVO.getDescriptionSYS10(),
						newVO.getDescriptionSYS10(),
						newVO.getProgressiveSys10DOC16(),
						serverLanguageId,
						conn
				);
			}

			return new VOListResponse(newVOs,false,newVOs.size());
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing document types",ex);
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
	public VOResponse insertDocumentType(DocumentTypeVO vo,String serverLanguageId,String username,ArrayList companiesList,ArrayList customizedFields) throws Throwable {
		Statement stmt = null;
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;
			vo.setEnabledDOC16("Y");

			String companyCode = companiesList.get(0).toString();
			if (vo.getCompanyCodeSys01DOC16()==null)
				vo.setCompanyCodeSys01DOC16(companyCode);

			// generate PROGRESSIVE_HIE02 value...
			stmt = conn.createStatement();
			BigDecimal progressiveHIE02 = CompanyProgressiveUtils.getInternalProgressive(vo.getCompanyCodeSys01DOC16(),"HIE02_HIERARCHIES","PROGRESSIVE",conn);
			BigDecimal progressiveHIE01 = TranslationUtils.insertTranslations(vo.getDescriptionSYS10(),vo.getCompanyCodeSys01DOC16(),conn);
			stmt.execute("INSERT INTO HIE02_HIERARCHIES(PROGRESSIVE,COMPANY_CODE_SYS01,ENABLED) VALUES("+progressiveHIE02+",'"+vo.getCompanyCodeSys01DOC16()+"','Y')");
			stmt.execute("INSERT INTO HIE01_LEVELS(PROGRESSIVE,PROGRESSIVE_HIE02,LEV,ENABLED) VALUES("+progressiveHIE01+","+progressiveHIE02+",0,'Y')");
			stmt.execute("UPDATE HIE02_HIERARCHIES SET PROGRESSIVE_HIE01="+progressiveHIE01+" WHERE PROGRESSIVE="+progressiveHIE02);
			vo.setProgressiveHie02DOC16(progressiveHIE02);
			vo.setProgressiveSys10DOC16(progressiveHIE01);

			Map attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01DOC16","COMPANY_CODE_SYS01");
			attribute2dbField.put("progressiveHie02DOC16","PROGRESSIVE_HIE02");
			attribute2dbField.put("progressiveSys10DOC16","PROGRESSIVE_SYS10");
			attribute2dbField.put("enabledDOC16","ENABLED");

			// insert into DOC16...
			Response res = CustomizeQueryUtil.insertTable(
					conn,
					new UserSessionParameters(username),
					vo,
					"DOC16_DOC_HIERARCHY",
					attribute2dbField,
					"Y",
					"N",
					null,
					true,
					customizedFields
			);


			return new VOResponse(vo);
		}
		catch (Throwable ex) {
			Logger.error(username, this.getClass().getName(),
					"executeCommand", "Error while inserting a new documents", ex);
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
	public VOResponse deleteDocumentType(ArrayList list,String serverLanguageId,String username) throws Throwable {
		Statement stmt = null;
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;
			stmt = conn.createStatement();

			DocumentTypeVO vo = null;
			for(int i=0;i<list.size();i++) {
				// logically delete the record in DOC16...
				vo = (DocumentTypeVO)list.get(i);
				stmt.execute("update DOC16_DOC_HIERARCHY set ENABLED='N' where COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01DOC16()+"' and PROGRESSIVE_HIE02="+vo.getProgressiveHie02DOC16());
			}

			return new VOResponse(new Boolean(true));
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing document type",ex);
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

