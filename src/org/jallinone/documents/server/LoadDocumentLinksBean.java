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
 * * <p>Description: Bean used to manage document links.</p>
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
public class LoadDocumentLinksBean implements LoadDocumentLinks {



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
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
	 */
	public DocumentLinkVO getDocumentLink(DocumentPK pk) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
	 */
	public DocumentVersionVO getDocumentVersion() {
		throw new UnsupportedOperationException();
	}
	
	
	/**
	 * Business logic to execute.
	 */
	public VOListResponse loadDocumentLinks(GridParams gridParams,String serverLanguageId,String username) throws Throwable {
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;

			String sql =
				"select DOC17_DOCUMENT_LINKS.COMPANY_CODE_SYS01,DOC17_DOCUMENT_LINKS.PROGRESSIVE_DOC14,DOC17_DOCUMENT_LINKS.PROGRESSIVE_HIE01,"+
				"HIE01_LEVELS.PROGRESSIVE_HIE02,SYS10_TRANSLATIONS.DESCRIPTION "+
				" from DOC17_DOCUMENT_LINKS,SYS10_TRANSLATIONS,HIE01_LEVELS where "+
				"DOC17_DOCUMENT_LINKS.PROGRESSIVE_HIE01=HIE01_LEVELS.PROGRESSIVE and "+
				"HIE01_LEVELS.PROGRESSIVE=SYS10_TRANSLATIONS.PROGRESSIVE and "+
				"SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
				"DOC17_DOCUMENT_LINKS.COMPANY_CODE_SYS01=? and "+
				"DOC17_DOCUMENT_LINKS.PROGRESSIVE_DOC14=?";

			Map attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01DOC17","DOC17_DOCUMENT_LINKS.COMPANY_CODE_SYS01");
			attribute2dbField.put("progressiveDoc14DOC17","DOC17_DOCUMENT_LINKS.PROGRESSIVE_DOC14");
			attribute2dbField.put("progressiveHie01DOC17","DOC17_DOCUMENT_LINKS.PROGRESSIVE_HIE01");
			attribute2dbField.put("progressiveHIE02","HIE01_LEVELS.PROGRESSIVE_HIE02");
			attribute2dbField.put("levelDescriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");

			DocumentPK pk = (DocumentPK)gridParams.getOtherGridParams().get(ApplicationConsts.DOCUMENT_PK);

			ArrayList values = new ArrayList();
			values.add(serverLanguageId);
			values.add(pk.getCompanyCodeSys01DOC14());
			values.add(pk.getProgressiveDOC14());

			// read from DOC17 table...
			Response answer = QueryUtil.getQuery(
					conn,
					new UserSessionParameters(username),
					sql,
					values,
					attribute2dbField,
					DocumentLinkVO.class,
					"Y",
					"N",
					null,
					gridParams,
					true
			);
			if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching documents links list",ex);
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
	 * Retrieve the list of DocumentVersionVO objects, based on the specified DocumentPK.
	 * This method does not open or clone a connection.
	 */
	public VOListResponse loadDocumentVersions(DocumentPK pk,GridParams gridParams,String serverLanguageId,String username)  throws Throwable{
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;


			String sql =
				"select DOC15_DOCUMENT_VERSIONS.COMPANY_CODE_SYS01,DOC15_DOCUMENT_VERSIONS.PROGRESSIVE_DOC14,DOC15_DOCUMENT_VERSIONS.VERSION,"+
				"DOC15_DOCUMENT_VERSIONS.CREATE_DATE,DOC15_DOCUMENT_VERSIONS.CREATE_USERNAME from "+
				"DOC15_DOCUMENT_VERSIONS where "+
				"DOC15_DOCUMENT_VERSIONS.COMPANY_CODE_SYS01=? and "+
				"DOC15_DOCUMENT_VERSIONS.PROGRESSIVE_DOC14=?";

			Map attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01DOC15","DOC15_DOCUMENT_VERSIONS.COMPANY_CODE_SYS01");
			attribute2dbField.put("progressiveDoc14DOC15","DOC15_DOCUMENT_VERSIONS.PROGRESSIVE_DOC14");
			attribute2dbField.put("versionDOC15","DOC15_DOCUMENT_VERSIONS.VERSION");
			attribute2dbField.put("createDateDOC15","DOC15_DOCUMENT_VERSIONS.CREATE_DATE");
			attribute2dbField.put("createUsernameDOC15","DOC15_DOCUMENT_VERSIONS.CREATE_USERNAME");

			ArrayList values = new ArrayList();
			values.add(pk.getCompanyCodeSys01DOC14());
			values.add(pk.getProgressiveDOC14());

			// read from DOC17 table...
			Response answer = QueryUtil.getQuery(
					conn,
					new UserSessionParameters(username),
					sql,
					values,
					attribute2dbField,
					DocumentVersionVO.class,
					"Y",
					"N",
					null,
					gridParams,
					true
			);
			if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"getDocumentVersions","Error while fetching document versions list",ex);
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
	 * Retrieve DetailDocumentVO object from the specified DocumentPK value.
	 * This method does not create or release connection.
	 */
	public DetailDocumentVO loadDocument(DocumentPK pk,String serverLanguageId,String username,ArrayList customizedFields)  throws Throwable{
		Statement stmt = null;
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;

			Map attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01DOC14","DOC14_DOCUMENTS.COMPANY_CODE_SYS01");
			attribute2dbField.put("progressiveDOC14","DOC14_DOCUMENTS.PROGRESSIVE");
			attribute2dbField.put("descriptionDOC14","DOC14_DOCUMENTS.DESCRIPTION");
			attribute2dbField.put("filenameDOC14","DOC14_DOCUMENTS.FILENAME");

			HashSet pkAttributes = new HashSet();
			pkAttributes.add("companyCodeSys01DOC14");
			pkAttributes.add("progressiveDOC14");

			String baseSQL =
				"select "+
				"DOC14_DOCUMENTS.COMPANY_CODE_SYS01,DOC14_DOCUMENTS.PROGRESSIVE,DOC14_DOCUMENTS.DESCRIPTION,DOC14_DOCUMENTS.FILENAME "+
				" from "+
				"DOC14_DOCUMENTS "+
				" where "+
				"DOC14_DOCUMENTS.COMPANY_CODE_SYS01=? and "+
				"DOC14_DOCUMENTS.PROGRESSIVE=?";

			ArrayList values = new ArrayList();
			values.add(pk.getCompanyCodeSys01DOC14());
			values.add(pk.getProgressiveDOC14());

			// read from DOC14 table...
			Response res = CustomizeQueryUtil.getQuery(
					conn,
					new UserSessionParameters(username),
					baseSQL,
					values,
					attribute2dbField,
					DetailDocumentVO.class,
					"Y",
					"N",
					null,
					true,
					customizedFields
			);

			Response answer = res;
			if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (DetailDocumentVO)((VOResponse)answer).getVo();
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"loadDocument","Error while fetching an existing document",ex);
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
