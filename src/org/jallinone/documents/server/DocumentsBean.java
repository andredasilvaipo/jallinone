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
 * * <p>Description: Bean used to manage documents, links, properties and levels.</p>
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
public class DocumentsBean  implements Documents {


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


	private LoadDocumentLinksBean bean;

	public void setBean(LoadDocumentLinksBean bean) {
		this.bean = bean;
	}

	private InsertDocumentLinkBean insertDocumentLinkBean;

  public void setInsertDocumentLinkBean(InsertDocumentLinkBean insertDocumentLinkBean) {
		this.insertDocumentLinkBean = insertDocumentLinkBean;
	}

	public DocumentsBean() {
	}


	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
	 */
	public GridDocumentVO getGridDocument(HierarchyLevelVO pk) {
		throw new UnsupportedOperationException();
	}


	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
	 */
	public DocPropertyVO getDocProperty() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
	 */
	public LevelPropertyVO getLevelProperty() {
		throw new UnsupportedOperationException();
	}




	/**
	 * Business logic to execute.
	 */
	public VOListResponse loadDocuments(GridParams pars,HashMap filters,String serverLanguageId,String username,ArrayList companiesList) throws Throwable {
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;

			BigDecimal rootProgressiveHIE01 = (BigDecimal)pars.getOtherGridParams().get(ApplicationConsts.ROOT_PROGRESSIVE_HIE01);
			BigDecimal progressiveHIE01 = (BigDecimal)pars.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_HIE01);
			BigDecimal progressiveHIE02 = (BigDecimal)pars.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_HIE02);

			HierarchyLevelVO vo = (HierarchyLevelVO)pars.getOtherGridParams().get(ApplicationConsts.TREE_FILTER);
			if (vo!=null) {
				progressiveHIE01 = vo.getProgressiveHIE01();
				progressiveHIE02 = vo.getProgressiveHie02HIE01();
			}

			// retrieve companies list...
			String companies = "";
			for(int i=0;i<companiesList.size();i++)
				companies += "'"+companiesList.get(i).toString()+"',";
			companies = companies.substring(0,companies.length()-1);

			String sql =
				"select DOC14_DOCUMENTS.COMPANY_CODE_SYS01,DOC14_DOCUMENTS.PROGRESSIVE,DOC14_DOCUMENTS.DESCRIPTION,DOC14_DOCUMENTS.FILENAME,"+
				"HIE01_LEVELS.PROGRESSIVE_HIE02,DOC17_DOCUMENT_LINKS.PROGRESSIVE_HIE01"+
				" from DOC14_DOCUMENTS,DOC17_DOCUMENT_LINKS,HIE01_LEVELS where "+
				"DOC14_DOCUMENTS.COMPANY_CODE_SYS01=DOC17_DOCUMENT_LINKS.COMPANY_CODE_SYS01 and "+
				"DOC14_DOCUMENTS.PROGRESSIVE=DOC17_DOCUMENT_LINKS.PROGRESSIVE_DOC14 and "+
				"DOC17_DOCUMENT_LINKS.PROGRESSIVE_HIE01=HIE01_LEVELS.PROGRESSIVE and "+
				"HIE01_LEVELS.PROGRESSIVE_HIE02=? and "+
				"DOC14_DOCUMENTS.COMPANY_CODE_SYS01 in ("+companies+") ";


			if (rootProgressiveHIE01==null || !rootProgressiveHIE01.equals(progressiveHIE01)) {
				// retrieve all subnodes of the specified node...
				pstmt = conn.prepareStatement(
						"select HIE01_LEVELS.PROGRESSIVE,HIE01_LEVELS.PROGRESSIVE_HIE01,HIE01_LEVELS.LEV from HIE01_LEVELS "+
						"where ENABLED='Y' and PROGRESSIVE_HIE02=? and PROGRESSIVE>=? "+
						"order by LEV,PROGRESSIVE_HIE01,PROGRESSIVE"
				);
				pstmt.setBigDecimal(1,progressiveHIE02);
				pstmt.setBigDecimal(2,progressiveHIE01);
				ResultSet rset = pstmt.executeQuery();

				HashSet currentLevelNodes = new HashSet();
				HashSet newLevelNodes = new HashSet();
				String nodes = "";
				int currentLevel = -1;
				while(rset.next()) {
					if (currentLevel!=rset.getInt(3)) {
						// next level...
						currentLevel = rset.getInt(3);
						currentLevelNodes = newLevelNodes;
						newLevelNodes = new HashSet();
					}
					if (rset.getBigDecimal(1).equals(progressiveHIE01)) {
						newLevelNodes.add(rset.getBigDecimal(1));
						nodes += rset.getBigDecimal(1)+",";
					}
					else if (currentLevelNodes.contains(rset.getBigDecimal(2))) {
						newLevelNodes.add(rset.getBigDecimal(1));
						nodes += rset.getBigDecimal(1)+",";
					}
				}
				rset.close();
				pstmt.close();
				if (nodes.length()>0)
					nodes = nodes.substring(0,nodes.length()-1);
				sql += " and DOC17_DOCUMENT_LINKS.PROGRESSIVE_HIE01 in ("+nodes+")";
			}

			ArrayList values = new ArrayList();
			values.add(progressiveHIE02);

			if (filters!=null) {
				if (filters.size()>0) {
					// apply doc. property filters...
					sql +=
						"and DOC14_DOCUMENTS.PROGRESSIVE in (select DOC20_DOC_PROPERTIES.PROGRESSIVE_DOC14 "+
						"from DOC20_DOC_PROPERTIES where ";
					//              "DOC20_DOC_PROPERTIES.COMPANY_CODE_SYS01=DOC14_DOCUMENTS.COMPANY_CODE_SYS01 ";
					Iterator it = filters.keySet().iterator();
					BigDecimal progressive = null;
					Object value = null;
					while(it.hasNext()) {
						progressive = (BigDecimal)it.next();
						value = filters.get(progressive);
						sql += "DOC20_DOC_PROPERTIES.PROGRESSIVE_SYS10="+progressive+" and ";
						if (value instanceof String)
							sql += "DOC20_DOC_PROPERTIES.TEXT_VALUE='"+value+"' or ";
						else if (value instanceof Timestamp) {
							sql += "DOC20_DOC_PROPERTIES.DATE_VALUE=? or ";
							values.add(value);
						} else if (value instanceof Number)
							sql += "DOC20_DOC_PROPERTIES.NUM_VALUE="+value+" or ";
					}
					sql = sql.substring(0,sql.length()-3); // remove the last "or"...
					sql += ")";
				}
			}



			Map attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01DOC14","DOC14_DOCUMENTS.COMPANY_CODE_SYS01");
			attribute2dbField.put("progressiveDOC14","DOC14_DOCUMENTS.PROGRESSIVE");
			attribute2dbField.put("descriptionDOC14","DOC14_DOCUMENTS.DESCRIPTION");
			attribute2dbField.put("filenameDOC14","DOC14_DOCUMENTS.FILENAME");
			attribute2dbField.put("progressiveHie02HIE01","HIE01_LEVELS.PROGRESSIVE_HIE02");
			attribute2dbField.put("progressiveHie01DOC17","DOC17_DOCUMENT_LINKS.PROGRESSIVE_HIE01");

			// read from DOC14 table...
			Response answer = QueryUtil.getQuery(
					conn,
					new UserSessionParameters(username),
					sql,
					values,
					attribute2dbField,
					GridDocumentVO.class,
					"Y",
					"N",
					null,
					pars,
					50,
					true
			);

			if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching documents list",ex);
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





	/**
	 * Business logic to execute.
	 */
	public VOListResponse loadLevelProperties(GridParams pars,String serverLanguageId,String username,ArrayList companiesList) throws Throwable {
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;

			BigDecimal rootProgressiveHIE01 = (BigDecimal)pars.getOtherGridParams().get(ApplicationConsts.ROOT_PROGRESSIVE_HIE01);
			BigDecimal progressiveHIE01 = (BigDecimal)pars.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_HIE01);
			BigDecimal progressiveHIE02 = (BigDecimal)pars.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_HIE02);

			HierarchyLevelVO vo = (HierarchyLevelVO)pars.getOtherGridParams().get(ApplicationConsts.TREE_FILTER);
			if (vo!=null) {
				progressiveHIE01 = vo.getProgressiveHIE01();
				progressiveHIE02 = vo.getProgressiveHie02HIE01();
			}

			// retrieve companies list...
			String companies = "";
			for(int i=0;i<companiesList.size();i++)
				companies += "'"+companiesList.get(i).toString()+"',";
			companies = companies.substring(0,companies.length()-1);

			String sql =
				"select DOC21_LEVEL_PROPERTIES.COMPANY_CODE_SYS01,DOC21_LEVEL_PROPERTIES.PROGRESSIVE_SYS10,SYS10_TRANSLATIONS.DESCRIPTION,"+
				"DOC21_LEVEL_PROPERTIES.PROGRESSIVE_HIE01,DOC21_LEVEL_PROPERTIES.PROGRESSIVE_HIE02,DOC21_LEVEL_PROPERTIES.PROPERTY_TYPE "+
				" from DOC21_LEVEL_PROPERTIES,SYS10_TRANSLATIONS where "+
				"DOC21_LEVEL_PROPERTIES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
				"SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
				"DOC21_LEVEL_PROPERTIES.PROGRESSIVE_HIE02=? and "+
				"DOC21_LEVEL_PROPERTIES.COMPANY_CODE_SYS01 in ("+companies+") ";

			if (pars.getOtherGridParams().get(ApplicationConsts.LOAD_ANCIENTS)!=null &&
					((Boolean)pars.getOtherGridParams().get(ApplicationConsts.LOAD_ANCIENTS)).booleanValue() &&
					progressiveHIE02!=null &&
					progressiveHIE01!=null) {

				// retrieve all subnodes of the specified node...
				pstmt = conn.prepareStatement(
						"select HIE01_LEVELS.PROGRESSIVE,HIE01_LEVELS.PROGRESSIVE_HIE01 from HIE01_LEVELS "+
						"where HIE01_LEVELS.PROGRESSIVE<=? and PROGRESSIVE_HIE02=?"
				);
				pstmt.setBigDecimal(1,progressiveHIE01);
				pstmt.setBigDecimal(2,progressiveHIE02);
				ResultSet rset = pstmt.executeQuery();
				Hashtable parents = new Hashtable();
				BigDecimal parentProgressive = null;
				while(rset.next()) {
					parentProgressive = rset.getBigDecimal(2);
					if (parentProgressive!=null)
						parents.put(rset.getBigDecimal(1),parentProgressive);
				}
				rset.close();
				pstmt.close();

				String nodes = "";
				parentProgressive = progressiveHIE01;
				while(parentProgressive!=null) {
					nodes += parentProgressive+",";
					parentProgressive = (BigDecimal)parents.get(parentProgressive);
				}
				if (nodes.length()>0)
					nodes = nodes.substring(0,nodes.length()-1);
				sql += " and DOC21_LEVEL_PROPERTIES.PROGRESSIVE_HIE01 in ("+nodes+")";

			}
			else if (progressiveHIE01!=null) {
				// retrieve all subnodes of the specified node...
				sql += " and DOC21_LEVEL_PROPERTIES.PROGRESSIVE_HIE01="+progressiveHIE01;
			}


			Map attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01DOC21","DOC21_LEVEL_PROPERTIES.COMPANY_CODE_SYS01");
			attribute2dbField.put("progressiveSys10DOC21","DOC21_LEVEL_PROPERTIES.PROGRESSIVE_SYS10");
			attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
			attribute2dbField.put("progressiveHie01DOC21","DOC21_LEVEL_PROPERTIES.PROGRESSIVE_HIE01");
			attribute2dbField.put("progressiveHie02DOC21","DOC21_LEVEL_PROPERTIES.PROGRESSIVE_HIE02");
			attribute2dbField.put("propertyTypeDOC21","DOC21_LEVEL_PROPERTIES.PROPERTY_TYPE");


			ArrayList values = new ArrayList();
			values.add(serverLanguageId);
			values.add(progressiveHIE02);

			// read from DOC21 table...
			Response answer = QueryUtil.getQuery(
					conn,
					new UserSessionParameters(username),
					sql,
					values,
					attribute2dbField,
					LevelPropertyVO.class,
					"Y",
					"N",
					null,
					pars,
					true
			);

			if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching level properties list",ex);
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




	/**
	 * Business logic to execute.
	 */
	public VOListResponse updateDocProperties(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username) throws Throwable {
		PreparedStatement pstmt = null;

		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;

			Map attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01DOC20","COMPANY_CODE_SYS01");
			attribute2dbField.put("progressiveDoc14DOC20","PROGRESSIVE_DOC14");
			attribute2dbField.put("progressiveSys10DOC20","PROGRESSIVE_SYS10");
			attribute2dbField.put("textValueDOC20","TEXT_VALUE");
			attribute2dbField.put("numValueDOC20","NUM_VALUE");
			attribute2dbField.put("dateValueDOC20","DATE_VALUE");

			HashSet pkAttributes = new HashSet();
			pkAttributes.add("companyCodeSys01DOC20");
			pkAttributes.add("progressiveDoc14DOC20");
			pkAttributes.add("progressiveSys10DOC20");

			Response res = null;
			DocPropertyVO oldVO = null;
			DocPropertyVO newVO = null;

			pstmt = conn.prepareStatement(
					"select PROGRESSIVE_DOC14 from DOC20_DOC_PROPERTIES where "+
					"COMPANY_CODE_SYS01=? and PROGRESSIVE_DOC14=? and PROGRESSIVE_SYS10=?"
			);
			ResultSet rset = null;

			for(int i=0;i<oldVOs.size();i++) {
				oldVO = (DocPropertyVO)oldVOs.get(i);
				newVO = (DocPropertyVO)newVOs.get(i);

				// check if the record already exists: if it does not exist, then insert it...
				pstmt.setString(1,newVO.getCompanyCodeSys01DOC20());
				pstmt.setBigDecimal(2,newVO.getProgressiveDoc14DOC20());
				pstmt.setBigDecimal(3,newVO.getProgressiveSys10DOC20());
				rset = pstmt.executeQuery();
				if(rset.next()) {
					// the record exixts: it will be updated...
					res = QueryUtil.updateTable(
							conn,
							new UserSessionParameters(username),
							pkAttributes,
							oldVO,
							newVO,
							"DOC20_DOC_PROPERTIES",
							attribute2dbField,
							"Y",
							"N",
							null,
							true
					);
					if (res.isError()) {
						throw new Exception(res.getErrorMessage());
					}
				}
				else {
					// the record does not exixt: it will be inserted...
					res = QueryUtil.insertTable(
							conn,
							new UserSessionParameters(username),
							newVO,
							"DOC20_DOC_PROPERTIES",
							attribute2dbField,
							"Y",
							"N",
							null,
							true
					);
					if (res.isError()) {
						throw new Exception(res.getErrorMessage());
					}
				}
				rset.close();


			}

			return new VOListResponse(newVOs,false,newVOs.size());
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating property values for the specified document",ex);
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




	/**
	 * Business logic to execute.
	 */
	public VOResponse updateDocument(DetailDocumentVO oldVO,DetailDocumentVO newVO,String serverLanguageId,String username,ArrayList customizedFields,String docPath) throws Throwable {
		Statement stmt = null;
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;
			insertDocumentLinkBean.setConn(conn);

			Map attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01DOC14","COMPANY_CODE_SYS01");
			attribute2dbField.put("progressiveDOC14","PROGRESSIVE");
			attribute2dbField.put("descriptionDOC14","DESCRIPTION");
			attribute2dbField.put("filenameDOC14","FILENAME");

			HashSet pkAttributes = new HashSet();
			pkAttributes.add("companyCodeSys01DOC14");
			pkAttributes.add("progressiveDOC14");

			// update DOC14 table...
			Response resDOC14 = CustomizeQueryUtil.updateTable(
					conn,
					new UserSessionParameters(username),
					pkAttributes,
					oldVO,
					newVO,
					"DOC14_DOCUMENTS",
					attribute2dbField,
					"Y",
					"N",
					null,
					true,
					customizedFields
			);

			// check if v.o. contains also a new document version...
			if (newVO.getDocument()!=null) {
				// insert the new document version...
				Response res = insertDocumentLinkBean.insertDocumentVersion(
						new DocumentPK(newVO.getCompanyCodeSys01DOC14(),newVO.getProgressiveDOC14()),
						newVO.getDocument(),
						username,
						docPath
				);
				if (res.isError()) {
					throw new Exception(res.getErrorMessage());
				}
			}

			Response answer = resDOC14;
			if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOResponse)answer;
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating an existing document",ex);
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
				insertDocumentLinkBean.setConn(null);
			}
			catch (Exception ex2) {
			}
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
	 * Business logic to execute.
	 */
	public VOListResponse updateLevelProperties(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username) throws Throwable {
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;

			LevelPropertyVO oldVO = null;
			LevelPropertyVO newVO = null;

			for(int i=0;i<oldVOs.size();i++) {
				oldVO = (LevelPropertyVO)oldVOs.get(i);
				newVO = (LevelPropertyVO)newVOs.get(i);

				// update property description...
				TranslationUtils.updateTranslation(
						oldVO.getDescriptionSYS10(),
						newVO.getDescriptionSYS10(),
						newVO.getProgressiveSys10DOC21(),
						serverLanguageId,
						conn
				);
			}

			return new VOListResponse(newVOs,false,newVOs.size());
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing level property descriptions",ex);
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
	public VOResponse insertDocument(DetailDocumentVO vo,String serverLanguageId,String username,ArrayList companiesList,ArrayList customizedFields,String docPath) throws Throwable {
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;
			insertDocumentLinkBean.setConn(conn);

			String companyCode = companiesList.get(0).toString();

			if (vo.getCompanyCodeSys01DOC14()==null)
				vo.setCompanyCodeSys01DOC14(companyCode);

			// generate progressive for document description...
			BigDecimal progressiveDOC14 = CompanyProgressiveUtils.getInternalProgressive(vo.getCompanyCodeSys01DOC14(),"DOC14_DOCUMENTS","PROGRESSIVE",conn);
			vo.setProgressiveDOC14(progressiveDOC14);

			Map attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01DOC14","COMPANY_CODE_SYS01");
			attribute2dbField.put("progressiveDOC14","PROGRESSIVE");
			attribute2dbField.put("descriptionDOC14","DESCRIPTION");
			attribute2dbField.put("filenameDOC14","FILENAME");

			// insert into DOC14...
			Response resDOC14 = CustomizeQueryUtil.insertTable(
					conn,
					new UserSessionParameters(username),
					vo,
					"DOC14_DOCUMENTS",
					attribute2dbField,
					"Y",
					"N",
					null,
					true,
					customizedFields
			);
			if (resDOC14.isError()) {
				throw new Exception(resDOC14.getErrorMessage());
			}

			// create a document link with the specified tree level...
			DocumentLinkVO linkVO = new DocumentLinkVO();
			linkVO.setCompanyCodeSys01DOC17(vo.getCompanyCodeSys01DOC14());
			linkVO.setProgressiveDoc14DOC17(vo.getProgressiveDOC14());
			linkVO.setProgressiveHie01DOC17(vo.getProgressiveHie01DOC17());
			Response res = insertDocumentLinkBean.insertDocumentLink(
					linkVO,
					username
			);
			if (res.isError()) {
				throw new Exception(res.getErrorMessage());
			}

			// insert the new document version...
			res = insertDocumentLinkBean.insertDocumentVersion(
					new DocumentPK(vo.getCompanyCodeSys01DOC14(),vo.getProgressiveDOC14()),
					vo.getDocument(),
					username,
					docPath
			);
			if (res.isError()) {
				throw new Exception(res.getErrorMessage());
			}

			Response answer = resDOC14;

			if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOResponse)answer;
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while inserting a new document",ex);
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
				insertDocumentLinkBean.setConn(null);
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
	 * Business logic to execute.
	 */
	public VOListResponse insertDocumentLinks(ArrayList list,String serverLanguageId,String username) throws Throwable {
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;
			insertDocumentLinkBean.setConn(conn);

			DocumentLinkVO vo = null;
			Response res = null;

			for(int i=0;i<list.size();i++) {
				vo = (DocumentLinkVO)list.get(i);

				// insert into DOC17...
				res = insertDocumentLinkBean.insertDocumentLink(vo,username);
				if (res.isError()) {
					throw new Exception(res.getErrorMessage());
				}
			}

			return new VOListResponse(list,false,list.size());
		}
		catch (Throwable ex) {
			Logger.error(username, this.getClass().getName(),
					"executeCommand", "Error while inserting new document links", ex);
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
				insertDocumentLinkBean.setConn(null);
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
	 * Business logic to execute.
	 */
	public VOListResponse insertLevelProperties(ArrayList list,String serverLanguageId,String username,ArrayList companiesList) throws Throwable {
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;
			String companyCode = companiesList.get(0).toString();
			LevelPropertyVO vo = null;
			BigDecimal progressiveSys10DOC21 = null;

			Map attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01DOC21","COMPANY_CODE_SYS01");
			attribute2dbField.put("progressiveSys10DOC21","PROGRESSIVE_SYS10");
			attribute2dbField.put("progressiveHie01DOC21","PROGRESSIVE_HIE01");
			attribute2dbField.put("progressiveHie02DOC21","PROGRESSIVE_HIE02");
			attribute2dbField.put("propertyTypeDOC21","PROPERTY_TYPE");

			Response res = null;

			for(int i=0;i<list.size();i++) {
				vo = (LevelPropertyVO)list.get(i);
				if (vo.getCompanyCodeSys01DOC21() == null)
					vo.setCompanyCodeSys01DOC21(companyCode);

				// insert record in SYS10 and generate progressive for property description...
				progressiveSys10DOC21 = TranslationUtils.insertTranslations(vo.getDescriptionSYS10(),vo.getCompanyCodeSys01DOC21(),conn);
				vo.setProgressiveSys10DOC21(progressiveSys10DOC21);

				// insert into DOC21...
				res = QueryUtil.insertTable(
						conn,
						new UserSessionParameters(username),
						vo,
						"DOC21_LEVEL_PROPERTIES",
						attribute2dbField,
						"Y",
						"N",
						null,
						true
				);

				if (res.isError()) {
					throw new Exception(res.getErrorMessage());
				}
			}

			return new VOListResponse(list,false,list.size());
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while inserting new level properties",ex);
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
	public VOListResponse loadDocProperties(DocumentPK pk,String serverLanguageId,String username) throws Throwable {
		String sql = null;
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;
			bean.setConn(conn);

			// retrieve document links...
			GridParams pars = new GridParams();
			pars.getOtherGridParams().put(ApplicationConsts.DOCUMENT_PK,pk);
			Response res = bean.loadDocumentLinks(pars,serverLanguageId,username);
			if (res.isError())
				throw new Exception(res.getErrorMessage());

			// for each document link retrieve ancient progressiveHIE01s...
			java.util.List linkVOs = ((VOListResponse)res).getRows();
			DocumentLinkVO linkVO = null;
			pstmt = conn.prepareStatement(
					"select HIE01_LEVELS.PROGRESSIVE,HIE01_LEVELS.PROGRESSIVE_HIE01 from HIE01_LEVELS "+
					"where HIE01_LEVELS.PROGRESSIVE<=? and PROGRESSIVE_HIE02=?"
			);
			Hashtable parents = new Hashtable();
			HashSet progressiveHIE01s = new HashSet();
			BigDecimal progressiveHIE01 = null;
			for(int i=0;i<linkVOs.size();i++) {
				linkVO = (DocumentLinkVO)linkVOs.get(i);
				parents.clear();
				pstmt.setBigDecimal(1,linkVO.getProgressiveHie01DOC17());
				pstmt.setBigDecimal(2,linkVO.getProgressiveHIE02());
				ResultSet rset = pstmt.executeQuery();
				while(rset.next()) {
					progressiveHIE01 = rset.getBigDecimal(2);
					if (progressiveHIE01!=null)
						parents.put(rset.getBigDecimal(1),progressiveHIE01);
				}
				rset.close();

				// fill in the hashset "progressiveHIE01s"...
				progressiveHIE01 = linkVO.getProgressiveHie01DOC17();
				while(progressiveHIE01!=null) {
					progressiveHIE01s.add(progressiveHIE01);
					progressiveHIE01 = (BigDecimal)parents.get(progressiveHIE01);
				}
			}
			pstmt.close();

			sql =
				"select DOC21_LEVEL_PROPERTIES.COMPANY_CODE_SYS01,DOC21_LEVEL_PROPERTIES.PROGRESSIVE_SYS10,SYS10_TRANSLATIONS.DESCRIPTION,"+
				"DOC21_LEVEL_PROPERTIES.PROGRESSIVE_HIE01,DOC21_LEVEL_PROPERTIES.PROGRESSIVE_HIE02,DOC21_LEVEL_PROPERTIES.PROPERTY_TYPE, "+
				"DOC20_DOC_PROPERTIES.PROGRESSIVE_DOC14,DOC20_DOC_PROPERTIES.TEXT_VALUE,DOC20_DOC_PROPERTIES.NUM_VALUE,DOC20_DOC_PROPERTIES.DATE_VALUE "+
				" from SYS10_TRANSLATIONS,DOC21_LEVEL_PROPERTIES LEFT OUTER JOIN "+
				"(select DOC20_DOC_PROPERTIES.COMPANY_CODE_SYS01,DOC20_DOC_PROPERTIES.PROGRESSIVE_DOC14,DOC20_DOC_PROPERTIES.TEXT_VALUE,"+
				"DOC20_DOC_PROPERTIES.NUM_VALUE,DOC20_DOC_PROPERTIES.DATE_VALUE,DOC20_DOC_PROPERTIES.PROGRESSIVE_SYS10 "+
				"from DOC20_DOC_PROPERTIES where DOC20_DOC_PROPERTIES.PROGRESSIVE_DOC14=?) DOC20_DOC_PROPERTIES ON "+
				"DOC20_DOC_PROPERTIES.COMPANY_CODE_SYS01=DOC21_LEVEL_PROPERTIES.COMPANY_CODE_SYS01 and "+
				"DOC20_DOC_PROPERTIES.PROGRESSIVE_SYS10=DOC21_LEVEL_PROPERTIES.PROGRESSIVE_SYS10 "+
				" where "+
				"DOC21_LEVEL_PROPERTIES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
				"SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
				"DOC21_LEVEL_PROPERTIES.COMPANY_CODE_SYS01=? ";

			// append to SQL the filter on progressiveHIE0xs...
			sql += " and DOC21_LEVEL_PROPERTIES.PROGRESSIVE_HIE01 in ";
			Iterator it = progressiveHIE01s.iterator();
			String where = "";
			while(it.hasNext())
				where += it.next()+",";
			if (progressiveHIE01s.size()>0)
				where = where.substring(0,where.length()-1);
			if (where.length()>0)
				sql += "("+where+")";
			else
				sql += "(-1)";

			Map attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01DOC20","DOC21_LEVEL_PROPERTIES.COMPANY_CODE_SYS01");
			attribute2dbField.put("propertyTypeDOC21","DOC21_LEVEL_PROPERTIES.PROPERTY_TYPE");
			attribute2dbField.put("progressiveSys10DOC20","DOC21_LEVEL_PROPERTIES.PROGRESSIVE_SYS10");
			attribute2dbField.put("progressiveHie01DOC21","DOC21_LEVEL_PROPERTIES.PROGRESSIVE_HIE01");
			attribute2dbField.put("progressiveHie02DOC21","DOC21_LEVEL_PROPERTIES.PROGRESSIVE_HIE02");
			attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
			attribute2dbField.put("progressiveDoc14DOC20","DOC20_DOC_PROPERTIES.PROGRESSIVE_DOC14");
			attribute2dbField.put("textValueDOC20","DOC20_DOC_PROPERTIES.TEXT_VALUE");
			attribute2dbField.put("numValueDOC20","DOC20_DOC_PROPERTIES.NUM_VALUE");
			attribute2dbField.put("dateValueDOC20","DOC20_DOC_PROPERTIES.DATE_VALUE");


			ArrayList values = new ArrayList();
			values.add(pk.getProgressiveDOC14());
			values.add(serverLanguageId);
			values.add(pk.getCompanyCodeSys01DOC14());

			// read from DOC20 table...
			res = QueryUtil.getQuery(
					conn,
					new UserSessionParameters(username),
					sql,
					values,
					attribute2dbField,
					DocPropertyVO.class,
					"Y",
					"N",
					null,
					new GridParams(),
					true
			);

			if (!res.isError()) {
				java.util.List rows = ((VOListResponse)res).getRows();
				for(int i=0;i<rows.size();i++)
					((DocPropertyVO)rows.get(i)).setProgressiveDoc14DOC20(pk.getProgressiveDOC14());
				return (VOListResponse)res;
			}
			else
				throw new Exception(res.getErrorMessage());

		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching document properties list",ex);
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
	public VOResponse deleteDocuments(ArrayList list,String serverLanguageId,String username,String docPath) throws Throwable {
		Statement stmt = null;
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;
			bean.setConn(conn);

			DocumentPK pk = null;
			stmt = conn.createStatement();
			Response res = null;
			ArrayList versions = null;
			DocumentVersionVO versionVO = null;
			for(int i=0;i<list.size();i++) {
				pk = (DocumentPK)list.get(i);

				// retrieve all document versions...
				res = bean.loadDocumentVersions(pk,new GridParams(),serverLanguageId,username);
				if (res.isError())
					throw new Exception(res.getErrorMessage());
				versions = new ArrayList(((VOListResponse)res).getRows());

				// for each document version: delete record in DOC15 and delete file from file system...
				res = deleteDocumentVersions(versions,serverLanguageId,username,docPath);
				if (res.isError()) {
					throw new Exception(res.getErrorMessage());
				}

				// phisically delete document links from DOC17...
				stmt.execute("delete from DOC17_DOCUMENT_LINKS where "+
						"COMPANY_CODE_SYS01='"+pk.getCompanyCodeSys01DOC14()+"' and "+
						"PROGRESSIVE_DOC14="+pk.getProgressiveDOC14());

				// phisically delete the record in DOC14...
				stmt.execute(
						"delete from DOC14_DOCUMENTS where "+
						"COMPANY_CODE_SYS01='"+pk.getCompanyCodeSys01DOC14()+"' and "+
						"PROGRESSIVE="+pk.getProgressiveDOC14()
				);

			}

			return new VOResponse(new Boolean(true));
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing documents",ex);
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
				pstmt.close();
			}
			catch (Exception ex2) {
			}
			try {
				bean.setConn(conn);
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
	public Document loadDocumentVersion(DocumentVersionVO vo,String serverLanguageId,String username,String docPath) throws Throwable {
		Statement stmt = null;
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;

			// retrieve document file name...
			DetailDocumentVO docVO = bean.loadDocument(
					new DocumentPK(vo.getCompanyCodeSys01DOC15(),vo.getProgressiveDoc14DOC15()),
					serverLanguageId,username,new ArrayList()
			);


			// read file from file system...
			String appPath = docPath;
			appPath = appPath.replace('\\','/');
			if (!appPath.endsWith("/"))
				appPath += "/";
			if (!new File(appPath).isAbsolute()) {
				// relative path (to "WEB-INF/classes/" folder)
				appPath = this.getClass().getResource("/").getPath().replaceAll("%20"," ")+appPath;
			}
			String relativePath = FileUtils.getFilePath(appPath,"DOC14",vo.getCreateDateDOC15());
			File file = new File(appPath+"DOC"+vo.getProgressiveDoc14DOC15()+"_"+vo.getVersionDOC15()); // retro-compatibility...
			if (!file.exists())
			  file = new File(appPath+relativePath+"DOC"+vo.getProgressiveDoc14DOC15()+"_"+vo.getVersionDOC15());
			FileInputStream fis = new FileInputStream(file);
			byte[] doc = new byte[(int)file.length()];
			fis.read(doc);
			fis.close();

			// store in application session the document...
			String docId = System.currentTimeMillis()+"_"+docVO.getFilenameDOC14().toLowerCase();
			Document document = new Document();
			document.setDoc(doc);
			document.setDocId(docId);
			return document;
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching an existing document version",ex);
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
	 * Phisically delete records from DOC15 and also delete files from file system.
	 * This method does not create or release connection and does not commit or rollback the connection.
	 */
	public VOResponse deleteDocumentVersions(ArrayList documentVersionVOs,String serverLanguageId,String username,String docPath) throws Throwable {
		Statement stmt = null;
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;
			stmt = conn.createStatement();

			DocumentVersionVO vo = null;
			File file = null;
			for(int i=0;i<documentVersionVOs.size();i++) {
				// phisically delete the records in DOC15...
				vo = (DocumentVersionVO)documentVersionVOs.get(i);
				stmt.execute("delete from DOC15_DOCUMENT_VERSIONS where "+
						"COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01DOC15()+"' and "+
						"PROGRESSIVE_DOC14="+vo.getProgressiveDoc14DOC15()+" and "+
						"VERSION="+vo.getVersionDOC15());

				// delete file from file system...
				String appPath = docPath;
				appPath = appPath.replace('\\','/');
				if (!appPath.endsWith("/"))
					appPath += "/";
				if (!new File(appPath).isAbsolute()) {
					// relative path (to "WEB-INF/classes/" folder)
					appPath = this.getClass().getResource("/").getPath().replaceAll("%20"," ")+appPath;
				}
				file = new File(appPath+"DOC"+vo.getProgressiveDoc14DOC15()+"_"+vo.getVersionDOC15());
				file.delete();
			}

			return new VOResponse(new Boolean(true));
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"deleteDocumentVersions","Error while deleting existing document versions",ex);
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
	public VOResponse deleteLevelProperties(ArrayList list,String serverLanguageId,String username) throws Throwable {
		Statement stmt = null;
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;
			stmt = conn.createStatement();

			LevelPropertyVO vo = null;
			for(int i=0;i<list.size();i++) {
				vo = (LevelPropertyVO)list.get(i);

				// phisically delete records in DOC20...
				stmt.execute("delete from DOC20_DOC_PROPERTIES where "+
						"COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01DOC21()+"' and "+
						"PROGRESSIVE_SYS10="+vo.getProgressiveSys10DOC21());

				// phisically delete record in DOC21...
				stmt.execute("delete from DOC21_LEVEL_PROPERTIES where "+
						"COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01DOC21()+"' and "+
						"PROGRESSIVE_SYS10="+vo.getProgressiveSys10DOC21());
			}

			return new VOResponse(new Boolean(true));
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing level properties",ex);
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
	public VOResponse deleteDocumentLinks(ArrayList list,String serverLanguageId,String username) throws Throwable {
		Statement stmt = null;
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;
			stmt = conn.createStatement();

			DocumentLinkVO vo = null;
			for(int i=0;i<list.size();i++) {
				// phisically delete records in DOC17...
				vo = (DocumentLinkVO)list.get(i);
				stmt.execute("delete from DOC17_DOCUMENT_LINKS where "+
						"COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01DOC17()+"' and "+
						"PROGRESSIVE_DOC14="+vo.getProgressiveDoc14DOC17()+" and "+
						"PROGRESSIVE_HIE01="+vo.getProgressiveHie01DOC17());
			}

			return new VOResponse(new Boolean(true));
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing document links",ex);
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

