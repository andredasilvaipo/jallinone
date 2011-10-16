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
 * Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 04139, USA.
 *
 *       The author may be contacted at:
 *           maurocarniel@tin.it</p>
 *
 * @author Mauro Carniel
 * @version 1.0
 */
public class HierarchiesBean  implements Hierarchies {


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




  public HierarchiesBean() {
  }



  /**
   * Business logic to execute.
   */
  public VOResponse getRootLevel(BigDecimal progressiveHIE04,String langId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      // retrieve the whole tree...
      DefaultTreeModel model = null;
      pstmt = conn.prepareStatement(
          "select HIE03_LEVELS.PROGRESSIVE,HIE03_LEVELS.PROGRESSIVE_HIE03,HIE03_LEVELS.LEV,SYS10_TRANSLATIONS.DESCRIPTION "+
          "from HIE03_LEVELS,SYS10_TRANSLATIONS,HIE04_HIERARCHIES "+
          "where HIE04_HIERARCHIES.PROGRESSIVE=? and "+
          "HIE04_HIERARCHIES.PROGRESSIVE_HIE03=HIE03_LEVELS.PROGRESSIVE and "+
          "HIE03_LEVELS.PROGRESSIVE = SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE='"+langId+"'"
      );
      pstmt.setBigDecimal(1,progressiveHIE04);
      ResultSet rset = pstmt.executeQuery();
      HierarchyLevelVO vo = null;
      if(rset.next()) {
        vo = new HierarchyLevelVO();
        vo.setEnabledHIE03("Y");
        vo.setLevelHIE03(rset.getBigDecimal(3));
        vo.setProgressiveHIE03(rset.getBigDecimal(1));
        vo.setProgressiveHie03HIE03(rset.getBigDecimal(2));
        vo.setProgressiveHie04HIE03(progressiveHIE04);
        vo.setDescriptionSYS10(rset.getString(4));
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
  public VOResponse insertLevel(HierarchyLevelVO vo,String serverLanguageId,String username)  throws Throwable{
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      vo.setEnabledHIE03("Y");

      // insert record in SYS10...
      BigDecimal progressiveHIE03 = TranslationUtils.insertTranslations(vo.getDescriptionSYS10(),username,conn);
      vo.setProgressiveHIE03(progressiveHIE03);

      // insert record in HIE03...
      pstmt = conn.prepareStatement(
          "insert into HIE03_LEVELS(PROGRESSIVE,PROGRESSIVE_HIE03,PROGRESSIVE_HIE04,LEV,ENABLED,CREATE_USER,CREATE_DATE) values(?,?,?,?,?,?,?)"
      );
      pstmt.setBigDecimal(1,progressiveHIE03);
      pstmt.setBigDecimal(2,vo.getProgressiveHie03HIE03());
      pstmt.setBigDecimal(3,vo.getProgressiveHie04HIE03());
      pstmt.setBigDecimal(4,vo.getLevelHIE03());
      pstmt.setString(5,vo.getEnabledHIE03());
			pstmt.setString(6,username);
			pstmt.setTimestamp(7,new java.sql.Timestamp(System.currentTimeMillis()));
      pstmt.execute();

      return new VOResponse(vo);
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"insertLevel","Error while inserting new hierarchy level",ex);
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
  public VOResponse updateLevel(HierarchyLevelVO oldVO,HierarchyLevelVO newVO,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;


      // update record in SYS10...
      TranslationUtils.updateTranslation(
          oldVO.getDescriptionSYS10(),
          newVO.getDescriptionSYS10(),
          newVO.getProgressiveHIE03(),
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
  public VOResponse deleteLevel(HierarchyLevelVO vo,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      // retrieve nodes to delete...
      pstmt = conn.prepareStatement(
          "select HIE03_LEVELS.PROGRESSIVE,HIE03_LEVELS.PROGRESSIVE_HIE03,HIE03_LEVELS.LEV from HIE03_LEVELS "+
          "where ENABLED='Y' and PROGRESSIVE_HIE04=? and PROGRESSIVE>=? "+
          "order by LEV,PROGRESSIVE_HIE03,PROGRESSIVE"
      );
      pstmt.setBigDecimal(1,vo.getProgressiveHie04HIE03());
      pstmt.setBigDecimal(2,vo.getProgressiveHIE03());
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
        if (rset.getBigDecimal(1).equals(vo.getProgressiveHIE03())) {
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

      // logically delete (update...) records in HIE03...
      String in = "";
      for(int i=0;i<nodesToDelete.size();i++)
        in += nodesToDelete.get(i)+",";
      in = in.substring(0,in.length()-1);
      pstmt = conn.prepareStatement("update HIE03_LEVELS set ENABLED='N',LAST_UPDATE_USER=?,LAST_UPDATE_DATE=?  where PROGRESSIVE in ("+in+")");
			pstmt.setString(1,username);
			pstmt.setTimestamp(2,new java.sql.Timestamp(System.currentTimeMillis()));
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
		public VOListResponse getLeaves(BigDecimal progressiveHIE04,BigDecimal progressiveHIE03,String langId,String username) throws Throwable {
			PreparedStatement pstmt = null;
			Connection conn = null;
			try {
				if (this.conn==null) conn = getConn(); else conn = this.conn;

				// retrieve the whole tree...
				pstmt = conn.prepareStatement(
						"select HIE03_LEVELS.PROGRESSIVE,HIE03_LEVELS.PROGRESSIVE_HIE03,HIE03_LEVELS.LEV,"+
						"SYS10_TRANSLATIONS.DESCRIPTION,HIE04_HIERARCHIES.PROGRESSIVE_HIE03 "+
						"from HIE03_LEVELS,HIE04_HIERARCHIES,SYS10_TRANSLATIONS where "+
						"HIE03_LEVELS.PROGRESSIVE_HIE04=HIE04_HIERARCHIES.PROGRESSIVE and "+
						"HIE03_LEVELS.PROGRESSIVE = SYS10_TRANSLATIONS.PROGRESSIVE and "+
						"SYS10_TRANSLATIONS.LANGUAGE_CODE='"+langId+"' and HIE03_LEVELS.ENABLED='Y' and "+
						"HIE03_LEVELS.PROGRESSIVE_HIE04=? "+
						"order by HIE03_LEVELS.LEV,HIE03_LEVELS.PROGRESSIVE_HIE03,HIE03_LEVELS.PROGRESSIVE"
				);
				pstmt.setBigDecimal(1,progressiveHIE04);
				ResultSet rset = pstmt.executeQuery();

				Hashtable currentLevelNodes = new Hashtable();
				Hashtable newLevelNodes = new Hashtable();
				int currentLevel = -1;
				DefaultMutableTreeNode rootNode = null;
				DefaultMutableTreeNode currentNode = null;
				DefaultMutableTreeNode parentNode = null;
				HierarchyLevelVO vo = null;
				while(rset.next()) {
					if (currentLevel!=rset.getInt(3)) {
						// next level...
						currentLevel = rset.getInt(3);
						currentLevelNodes = newLevelNodes;
						newLevelNodes = new Hashtable();
					}

					if (currentLevel==0) {
						// prepare a tree model with the root node...
						vo = new HierarchyLevelVO();
						vo.setEnabledHIE03("Y");
						vo.setLevelHIE03(rset.getBigDecimal(3));
						vo.setProgressiveHIE03(rset.getBigDecimal(1));
						vo.setProgressiveHie03HIE03(rset.getBigDecimal(2));
						vo.setProgressiveHie04HIE03(progressiveHIE04);
						vo.setDescriptionSYS10(rset.getString(4));
						vo.setProgressiveHie03HIE04(rset.getBigDecimal(5));
						currentNode = new DefaultMutableTreeNode(vo);
						rootNode = currentNode;
					}
					else {
						vo = new HierarchyLevelVO();
						vo.setEnabledHIE03("Y");
						vo.setLevelHIE03(rset.getBigDecimal(3));
						vo.setProgressiveHIE03(rset.getBigDecimal(1));
						vo.setProgressiveHie03HIE03(rset.getBigDecimal(2));
						vo.setProgressiveHie04HIE03(progressiveHIE04);
						vo.setDescriptionSYS10(rset.getString(4));
						vo.setProgressiveHie03HIE04(rset.getBigDecimal(5));
						currentNode = new DefaultMutableTreeNode(vo);

						parentNode = (DefaultMutableTreeNode)currentLevelNodes.get(new Integer(rset.getInt(2)));
						parentNode.add(currentNode);
					}

					newLevelNodes.put(new Integer(rset.getInt(1)),currentNode);

				}
				rset.close();

				if (progressiveHIE03!=null) {
					// remove all nodes not descendents of the node identified by progressiveHIE03...
					rootNode = getNode(rootNode,progressiveHIE03);
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
				HierarchyLevelVO vo = (HierarchyLevelVO)node.getUserObject();
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


			private DefaultMutableTreeNode getNode(DefaultMutableTreeNode node,BigDecimal progressiveHIE03) {
				HierarchyLevelVO vo = (HierarchyLevelVO)node.getUserObject();
				if (vo!=null && vo.getProgressiveHIE03().equals(progressiveHIE03)) {
					return node;
				}
				else {
					DefaultMutableTreeNode aux = null;
					for(int i=0;i<node.getChildCount();i++) {
						aux = getNode(
							(DefaultMutableTreeNode)node.getChildAt(i),
							progressiveHIE03
						);
						if (aux!=null)
							return aux;
					}
					return null;
				}
			}


}

