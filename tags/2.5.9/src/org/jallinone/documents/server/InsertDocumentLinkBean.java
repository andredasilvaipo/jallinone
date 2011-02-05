package org.jallinone.documents.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import javax.sql.DataSource;

import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.documents.java.DetailDocumentVO;
import org.jallinone.documents.java.DocPropertyVO;
import org.jallinone.documents.java.DocumentLinkVO;
import org.jallinone.documents.java.DocumentPK;
import org.jallinone.documents.java.DocumentVersionVO;
import org.jallinone.documents.java.GridDocumentVO;
import org.jallinone.documents.java.LevelPropertyVO;
import org.jallinone.hierarchies.java.HierarchyLevelVO;
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
 * * <p>Description: Bean used to manage links.</p>
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

public class InsertDocumentLinkBean implements InsertDocumentLink {

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


	public InsertDocumentLinkBean() {
	}


	public DocumentVersionVO getDocumentVersion() {
		throw new UnsupportedOperationException();
	}



	/**
	 * Insert new document link in DOC17 table.
	 * This method does not create or release connection and does not commit/rollback connection.
	 */
	public final VOResponse insertDocumentLink(DocumentLinkVO vo,String username) throws Throwable {
		Statement stmt = null;
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;

			Map attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01DOC17","COMPANY_CODE_SYS01");
			attribute2dbField.put("progressiveDoc14DOC17","PROGRESSIVE_DOC14");
			attribute2dbField.put("progressiveHie01DOC17","PROGRESSIVE_HIE01");

			// insert into DOC17...
			Response res = QueryUtil.insertTable(
					conn,
					new UserSessionParameters(username),
					vo,
					"DOC17_DOCUMENT_LINKS",
					attribute2dbField,
					"Y",
					"N",
					null,
					true
			);

			return new VOResponse(Boolean.TRUE);
		}
		catch (Throwable ex) {
			Logger.error(username, this.getClass().getName(),
					"insertDocumentLink", "Error while inserting a new document link", ex);
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




	/**
	 * Insert new document version in DOC15 and save the document in the file system.
	 * This method does not create or release connection and does not commit/rollback connection.
	 */
	public VOResponse insertDocumentVersion(
			DocumentPK pk,
			byte[] document,
			String username,
			String docPath) throws Throwable {
		 PreparedStatement pstmt = null;
		 Connection conn = null;
		 try {
			 if (this.conn==null) conn = getConn(); else conn = this.conn;

			// calculate the next document version...
			DocumentVersionVO vo = new DocumentVersionVO();
			pstmt = conn.prepareStatement("select max(VERSION) from DOC15_DOCUMENT_VERSIONS where COMPANY_CODE_SYS01=? and PROGRESSIVE_DOC14=?");
			pstmt.setString(1,pk.getCompanyCodeSys01DOC14());
			pstmt.setBigDecimal(2,pk.getProgressiveDOC14());
			ResultSet rset = pstmt.executeQuery();
			if (rset.next())
				vo.setVersionDOC15(rset.getBigDecimal(1));
			rset.close();
			if (vo.getVersionDOC15()==null)
				vo.setVersionDOC15(new BigDecimal(0));

			vo.setVersionDOC15(vo.getVersionDOC15().add(new BigDecimal(1)));
			vo.setCompanyCodeSys01DOC15(pk.getCompanyCodeSys01DOC14());
			vo.setProgressiveDoc14DOC15(pk.getProgressiveDOC14());
			vo.setCreateDateDOC15(new java.sql.Timestamp(System.currentTimeMillis()));
			vo.setCreateUsernameDOC15(username);

			Map attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01DOC15","COMPANY_CODE_SYS01");
			attribute2dbField.put("progressiveDoc14DOC15","PROGRESSIVE_DOC14");
			attribute2dbField.put("createDateDOC15","CREATE_DATE");
			attribute2dbField.put("createUsernameDOC15","CREATE_USERNAME");
			attribute2dbField.put("versionDOC15","VERSION");

			// insert into DOC15...
			Response res = QueryUtil.insertTable(
					conn,
					new UserSessionParameters(username),
					vo,
					"DOC15_DOCUMENT_VERSIONS",
					attribute2dbField,
					"Y",
					"N",
					null,
					true
			);

			if (res.isError())
				throw new Exception(res.getErrorMessage());

			// save the document in the file system...
			String appPath = docPath; //(String)((JAIOUserSessionParameters)userSessionPars).getAppParams().get(ApplicationConsts.DOC_PATH);
			appPath = appPath.replace('\\','/');
			if (!appPath.endsWith("/"))
				appPath += "/";
			if (!new File(appPath).isAbsolute()) {
				// relative path (to "WEB-INF/classes/" folder)
				appPath = this.getClass().getResource("/").getPath().replaceAll("%20"," ")+appPath;
			}
			String relativePath = FileUtils.getFilePath(appPath,"DOC14");
			new File(appPath+relativePath).mkdirs();
			FileOutputStream out = new FileOutputStream(appPath+relativePath+"DOC"+vo.getProgressiveDoc14DOC15()+"_"+vo.getVersionDOC15());
			out.write(document);
			out.close();

			Response answer = new VOResponse(vo);



			if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOResponse)answer;
		}
		catch (Throwable ex) {
			Logger.error(username, this.getClass().getName(),
					"insertDocumentVersion", "Error while inserting a new document version", ex);
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
