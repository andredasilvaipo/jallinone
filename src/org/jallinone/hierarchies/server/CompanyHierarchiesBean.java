package org.jallinone.hierarchies.server;

import org.openswing.swing.server.*;
import org.openswing.swing.tree.java.OpenSwingTreeNode;

import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;

import java.math.*;

import org.jallinone.system.customizations.java.*;
import org.jallinone.system.translations.server.*;
import org.openswing.swing.internationalization.server.*;
import org.openswing.swing.internationalization.java.*;
import org.jallinone.hierarchies.java.*;
import org.jallinone.system.progressives.server.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage hierarchies.</p>
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
public class CompanyHierarchiesBean  implements CompanyHierarchies {


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




  public CompanyHierarchiesBean() {
  }



  /**
   * Business logic to execute.
   */
  public VOResponse getRootLevel(String companyCodeSys01,BigDecimal progressiveHIE02,String langId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      // retrieve the whole tree...
      DefaultTreeModel model = null;
      pstmt = conn.prepareStatement(
          "select HIE01_COMPANY_LEVELS.PROGRESSIVE,HIE01_COMPANY_LEVELS.PROGRESSIVE_HIE01,"+
					"HIE01_COMPANY_LEVELS.LEV,SYS10_COMPANY_TRANSLATIONS.DESCRIPTION,HIE01_COMPANY_LEVELS.COMPANY_CODE_SYS01 "+
          "from HIE01_COMPANY_LEVELS,SYS10_COMPANY_TRANSLATIONS,HIE02_COMPANY_HIERARCHIES "+
					"where "+
					"HIE02_COMPANY_HIERARCHIES.COMPANY_CODE_SYS01=? and "+
          "HIE02_COMPANY_HIERARCHIES.PROGRESSIVE=? and "+
					"HIE02_COMPANY_HIERARCHIES.COMPANY_CODE_SYS01=HIE01_COMPANY_LEVELS.COMPANY_CODE_SYS01 and "+
          "HIE02_COMPANY_HIERARCHIES.PROGRESSIVE_HIE01=HIE01_COMPANY_LEVELS.PROGRESSIVE and "+
					"HIE01_COMPANY_LEVELS.COMPANY_CODE_SYS01=SYS10_COMPANY_TRANSLATIONS.COMPANY_CODE_SYS01 and "+
          "HIE01_COMPANY_LEVELS.PROGRESSIVE=SYS10_COMPANY_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_COMPANY_TRANSLATIONS.LANGUAGE_CODE='"+langId+"'"
      );
			pstmt.setString(1,companyCodeSys01);
      pstmt.setBigDecimal(2,progressiveHIE02);
      ResultSet rset = pstmt.executeQuery();
      CompanyHierarchyLevelVO vo = null;
      if(rset.next()) {
        vo = new CompanyHierarchyLevelVO();
        vo.setEnabledHIE01("Y");
        vo.setLevelHIE01(rset.getBigDecimal(3));
        vo.setProgressiveHIE01(rset.getBigDecimal(1));
        vo.setProgressiveHie01HIE01(rset.getBigDecimal(2));
        vo.setProgressiveHie02HIE01(progressiveHIE02);
        vo.setDescriptionSYS10(rset.getString(4));
				vo.setCompanySys01HIE01(rset.getString(5));
      }
      rset.close();

      Response answer = new VOResponse(vo);

      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOResponse)answer;
    } catch (Exception ex1) {
      ex1.printStackTrace();
      throw new Exception(ex1.getMessage());
    } finally {
      try {
        pstmt.close();
      }
      catch (Exception ex) {
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
  public VOResponse insertLevel(CompanyHierarchyLevelVO vo,String serverLanguageId,String username,String defCompanyCodeSys01SYS03)  throws Throwable{
    PreparedStatement pstmt = null;

    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;


      vo.setEnabledHIE01("Y");

      // insert record in SYS10...
      BigDecimal progressiveHIE01 = CompanyTranslationUtils.insertTranslations(vo.getDescriptionSYS10(),defCompanyCodeSys01SYS03,username,conn);
      vo.setProgressiveHIE01(progressiveHIE01);

      // insert record in HIE01...
      pstmt = conn.prepareStatement(
          "insert into HIE01_COMPANY_LEVELS(PROGRESSIVE,PROGRESSIVE_HIE01,PROGRESSIVE_HIE02,LEV,ENABLED,COMPANY_CODE_SYS01,CREATE_USER,CREATE_DATE) values(?,?,?,?,?,?,?,?)"
      );
      pstmt.setBigDecimal(1,progressiveHIE01);
      pstmt.setBigDecimal(2,vo.getProgressiveHie01HIE01());
      pstmt.setBigDecimal(3,vo.getProgressiveHie02HIE01());
      pstmt.setBigDecimal(4,vo.getLevelHIE01());
      pstmt.setString(5,vo.getEnabledHIE01());
			pstmt.setString(6,vo.getCompanySys01HIE01());
			pstmt.setString(7,username);
			pstmt.setTimestamp(8,new java.sql.Timestamp(System.currentTimeMillis()));
      pstmt.execute();

      return new VOResponse(vo);
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"companyInsertLevel","Error while inserting new hierarchy level",ex);
      try {
          if (this.conn==null && conn!=null) {
            // close only local connection
            conn.commit();
            conn.close();
        }

      }
      catch (Exception exx) {}
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
  public VOResponse updateLevel(CompanyHierarchyLevelVO oldVO,CompanyHierarchyLevelVO newVO,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;


      // update record in SYS10...
      CompanyTranslationUtils.updateTranslation(
		      newVO.getCompanySys01HIE01(),
          oldVO.getDescriptionSYS10(),
          newVO.getDescriptionSYS10(),
          newVO.getProgressiveHIE01(),
          serverLanguageId,
					username,
          conn
      );

      return new VOResponse(newVO);
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating the level description in the specified hierarchy",ex);
      try {
          if (this.conn==null && conn!=null) {
            // close only local connection
            conn.commit();
            conn.close();
        }

      }
      catch (Exception exx) {}
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




  /**
   * Business logic to execute.
   */
  public VOResponse deleteLevel(CompanyHierarchyLevelVO vo,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      // retrieve nodes to delete...
      pstmt = conn.prepareStatement(
          "select HIE01_COMPANY_LEVELS.PROGRESSIVE,HIE01_COMPANY_LEVELS.PROGRESSIVE_HIE01,HIE01_COMPANY_LEVELS.LEV from HIE01_COMPANY_LEVELS "+
          "where COMPANY_CODE_SYS01=? and ENABLED='Y' and PROGRESSIVE_HIE02=? and PROGRESSIVE>=? "+
          "order by LEV,PROGRESSIVE_HIE01,PROGRESSIVE"
      );
			pstmt.setString(1,vo.getCompanySys01HIE01());
      pstmt.setBigDecimal(2,vo.getProgressiveHie02HIE01());
      pstmt.setBigDecimal(3,vo.getProgressiveHIE01());
      ResultSet rset = pstmt.executeQuery();

      HashSet currentLevelNodes = new HashSet();
      HashSet newLevelNodes = new HashSet();
      ArrayList nodesToDelete = new ArrayList();
      int currentLevel = -1;
      while(rset.next()) {
        if (currentLevel!=rset.getInt(3)) {
          // next level...
          currentLevel = rset.getInt(3);
          currentLevelNodes = newLevelNodes;
          newLevelNodes = new HashSet();
        }
        if (rset.getBigDecimal(1).equals(vo.getProgressiveHIE01())) {
          newLevelNodes.add(rset.getBigDecimal(1));
          nodesToDelete.add(rset.getBigDecimal(1));
        }
        else if (currentLevelNodes.contains(rset.getBigDecimal(2))) {
          newLevelNodes.add(rset.getBigDecimal(1));
          nodesToDelete.add(rset.getBigDecimal(1));
        }
      }
      rset.close();
      pstmt.close();

      // logically delete (update...) records in HIE01...
      String in = "";
      for(int i=0;i<nodesToDelete.size();i++)
        in += nodesToDelete.get(i)+",";
      in = in.substring(0,in.length()-1);
      pstmt = conn.prepareStatement(
		     "update HIE01_COMPANY_LEVELS set ENABLED='N',LAST_UPDATE_USER=?,LAST_UPDATE_DATE=?  where COMPANY_CODE_SYS01=? and PROGRESSIVE in ("+in+")"
			);
			pstmt.setString(1,username);
			pstmt.setTimestamp(2,new java.sql.Timestamp(System.currentTimeMillis()));
			pstmt.setString(3,vo.getCompanySys01HIE01());
      pstmt.execute();

      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting the level (and all sub-levels...) in the specified hierarchy",ex);
      try {
          if (this.conn==null && conn!=null) {
            // close only local connection
            conn.commit();
            conn.close();
        }

      }
      catch (Exception exx) {}
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


		/**
		 * Business logic to execute.
		 */
		public VOListResponse getLeaves(String companyCodeSys01,BigDecimal progressiveHIE02,BigDecimal progressiveHIE01,String langId,String username) throws Throwable {
			PreparedStatement pstmt = null;
			Connection conn = null;
			try {
				if (this.conn==null) conn = getConn(); else conn = this.conn;

				// retrieve the whole tree...
				pstmt = conn.prepareStatement(
						"select HIE01_COMPANY_LEVELS.PROGRESSIVE,HIE01_COMPANY_LEVELS.PROGRESSIVE_HIE01,HIE01_COMPANY_LEVELS.LEV,"+
						"SYS10_COMPANY_TRANSLATIONS.DESCRIPTION,HIE02_COMPANY_HIERARCHIES.PROGRESSIVE_HIE01 "+
						"from HIE01_COMPANY_LEVELS,HIE02_COMPANY_HIERARCHIES,SYS10_COMPANY_TRANSLATIONS where "+
						"HIE01_COMPANY_LEVELS.COMPANY_CODE_SYS01=HIE02_COMPANY_HIERARCHIES.COMPANY_CODE_SYS01 and "+
						"HIE01_COMPANY_LEVELS.PROGRESSIVE_HIE02=HIE02_COMPANY_HIERARCHIES.PROGRESSIVE and "+
						"HIE01_COMPANY_LEVELS.COMPANY_CODE_SYS01=SYS10_COMPANY_TRANSLATIONS.COMPANY_CODE_SYS01 and "+
						"HIE01_COMPANY_LEVELS.PROGRESSIVE=SYS10_COMPANY_TRANSLATIONS.PROGRESSIVE and "+
						"SYS10_COMPANY_TRANSLATIONS.LANGUAGE_CODE='"+langId+"' and HIE01_COMPANY_LEVELS.ENABLED='Y' and "+
						"HIE01_COMPANY_LEVELS.COMPANY_CODE_SYS01=? and "+
						"HIE01_COMPANY_LEVELS.PROGRESSIVE_HIE02=? "+
						"order by HIE01_COMPANY_LEVELS.LEV,HIE01_COMPANY_LEVELS.PROGRESSIVE_HIE01,HIE01_COMPANY_LEVELS.PROGRESSIVE"
				);
				pstmt.setString(1,companyCodeSys01);
				pstmt.setBigDecimal(2,progressiveHIE02);
				ResultSet rset = pstmt.executeQuery();

				Hashtable currentLevelNodes = new Hashtable();
				Hashtable newLevelNodes = new Hashtable();
				int currentLevel = -1;
				DefaultMutableTreeNode rootNode = null;
				DefaultMutableTreeNode currentNode = null;
				DefaultMutableTreeNode parentNode = null;
				CompanyHierarchyLevelVO vo = null;
				while(rset.next()) {
					if (currentLevel!=rset.getInt(3)) {
						// next level...
						currentLevel = rset.getInt(3);
						currentLevelNodes = newLevelNodes;
						newLevelNodes = new Hashtable();
					}

					if (currentLevel==0) {
						// prepare a tree model with the root node...
						vo = new CompanyHierarchyLevelVO();
						vo.setCompanySys01HIE01(companyCodeSys01);
						vo.setEnabledHIE01("Y");
						vo.setLevelHIE01(rset.getBigDecimal(3));
						vo.setProgressiveHIE01(rset.getBigDecimal(1));
						vo.setProgressiveHie01HIE01(rset.getBigDecimal(2));
						vo.setProgressiveHie02HIE01(progressiveHIE02);
						vo.setDescriptionSYS10(rset.getString(4));
						vo.setProgressiveHie01HIE02(rset.getBigDecimal(5));
						currentNode = new DefaultMutableTreeNode(vo);
						rootNode = currentNode;
					}
					else {
						vo = new CompanyHierarchyLevelVO();
						vo.setCompanySys01HIE01(companyCodeSys01);
						vo.setEnabledHIE01("Y");
						vo.setLevelHIE01(rset.getBigDecimal(3));
						vo.setProgressiveHIE01(rset.getBigDecimal(1));
						vo.setProgressiveHie01HIE01(rset.getBigDecimal(2));
						vo.setProgressiveHie02HIE01(progressiveHIE02);
						vo.setDescriptionSYS10(rset.getString(4));
						vo.setProgressiveHie01HIE02(rset.getBigDecimal(5));
						currentNode = new DefaultMutableTreeNode(vo);

						parentNode = (DefaultMutableTreeNode)currentLevelNodes.get(new Integer(rset.getInt(2)));
						parentNode.add(currentNode);
					}

					newLevelNodes.put(new Integer(rset.getInt(1)),currentNode);

				}
				rset.close();

				if (progressiveHIE01!=null) {
					// remove all nodes not descendents of the node identified by progressiveHIE01...
					rootNode = getNode(rootNode,progressiveHIE01);
				}

				ArrayList leaves = new ArrayList();
				if (rootNode!=null)
					searchLeaves("", rootNode, leaves);

				return new VOListResponse(leaves,false,leaves.size());
			} catch (Exception ex1) {
				ex1.printStackTrace();
				throw new Exception(ex1.getMessage());
			} finally {
				try {
					pstmt.close();
				}
				catch (Exception ex) {
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


			private void searchLeaves(String prefix, DefaultMutableTreeNode node,ArrayList leaves) {
				CompanyHierarchyLevelVO vo = (CompanyHierarchyLevelVO)node.getUserObject();
				if (node.getChildCount()==0 && vo!=null) {
					vo.setDescriptionSYS10(prefix+vo.getDescriptionSYS10());
					leaves.add(vo);
				}
				else {
					for(int i=0;i<node.getChildCount();i++)
					  searchLeaves(
					    prefix+(vo.getDescriptionSYS10().length()>0?vo.getDescriptionSYS10()+" - ":""),
							(DefaultMutableTreeNode)node.getChildAt(i),
							leaves
						);
				}
			}


			private DefaultMutableTreeNode getNode(DefaultMutableTreeNode node,BigDecimal progressiveHIE01) {
				CompanyHierarchyLevelVO vo = (CompanyHierarchyLevelVO)node.getUserObject();
				if (vo!=null && vo.getProgressiveHIE01().equals(progressiveHIE01)) {
					return node;
				}
				else {
					DefaultMutableTreeNode aux = null;
					for(int i=0;i<node.getChildCount();i++) {
						aux = getNode(
							(DefaultMutableTreeNode)node.getChildAt(i),
							progressiveHIE01
						);
						if (aux!=null)
							return aux;
					}
					return null;
				}
			}


}

