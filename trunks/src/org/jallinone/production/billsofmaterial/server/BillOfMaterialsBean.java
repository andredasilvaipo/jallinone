package org.jallinone.production.billsofmaterial.server;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import javax.sql.DataSource;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.items.java.ItemPK;
import org.jallinone.production.billsofmaterial.java.AltComponentVO;
import org.jallinone.production.billsofmaterial.java.ComponentVO;
import org.jallinone.production.billsofmaterial.java.MaterialVO;
import org.jallinone.registers.currency.java.CurrencyVO;
import org.jallinone.registers.currency.server.CurrenciesBean;
import org.jallinone.registers.currency.server.CurrencyConversionUtils;
import org.jallinone.system.progressives.server.CompanyProgressiveUtils;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.openswing.swing.logger.server.Logger;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.message.receive.java.VOResponse;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.server.QueryUtil;
import org.openswing.swing.server.UserSessionParameters;
import org.openswing.swing.tree.java.OpenSwingTreeNode;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage the bill of materials.</p>
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
public class BillOfMaterialsBean implements BillOfMaterials  {



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


	  private CurrenciesBean compCurr;

	  public void setCompCurr(CurrenciesBean compCurr) {
		  this.compCurr = compCurr;
	  }


	  /**
	   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
	   */
	  public ComponentVO getComponent() {
		  throw new UnsupportedOperationException("");
	  }

	  /**
	   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
	   */
	  public AltComponentVO getAltComponent() {
		  throw new UnsupportedOperationException("");
	  }


	/**
	 * Business logic to execute.
	 */
	public VOListResponse loadComponents(GridParams gridParams,String langId,String username) throws Throwable {
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;

			String sql =
				"select ITM03_COMPONENTS.COMPANY_CODE_SYS01,ITM03_COMPONENTS.ITEM_CODE_ITM01,ITM03_COMPONENTS.PARENT_ITEM_CODE_ITM01,ITM03_COMPONENTS.ENABLED,"+
				"ITM03_COMPONENTS.SEQUENCE,ITM03_COMPONENTS.START_DATE,ITM03_COMPONENTS.END_DATE,"+
				"ITM03_COMPONENTS.VERSION,ITM03_COMPONENTS.REVISION,ITM03_COMPONENTS.QTY,"+
				"SYS10_TRANSLATIONS.DESCRIPTION,ITM01_ITEMS.PROGRESSIVE_HIE02,ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02 "+
				"from ITM01_ITEMS,SYS10_TRANSLATIONS,ITM03_COMPONENTS "+
				"where "+
				"ITM03_COMPONENTS.COMPANY_CODE_SYS01 = ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
				"ITM03_COMPONENTS.ITEM_CODE_ITM01 = ITM01_ITEMS.ITEM_CODE and "+
				"ITM01_ITEMS.PROGRESSIVE_SYS10 = SYS10_TRANSLATIONS.PROGRESSIVE and "+
				"SYS10_TRANSLATIONS.LANGUAGE_CODE=? and ITM03_COMPONENTS.ENABLED='Y' and "+
				"ITM03_COMPONENTS.COMPANY_CODE_SYS01=? and "+
				"ITM03_COMPONENTS.PARENT_ITEM_CODE_ITM01=?";

			Map attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01ITM03","ITM03_COMPONENTS.COMPANY_CODE_SYS01");
			attribute2dbField.put("progressiveHIE02","ITM01_ITEMS.PROGRESSIVE_HIE02");
			attribute2dbField.put("itemCodeItm01ITM03","ITM03_COMPONENTS.ITEM_CODE_ITM01");
			attribute2dbField.put("parentItemCodeItm01ITM03","ITM03_COMPONENTS.PARENT_ITEM_CODE_ITM01");
			attribute2dbField.put("sequenceITM03","ITM03_COMPONENTS.SEQUENCE");
			attribute2dbField.put("startDateITM03","ITM03_COMPONENTS.START_DATE");
			attribute2dbField.put("endDateITM03","ITM03_COMPONENTS.END_DATE");
			attribute2dbField.put("versionITM03","ITM03_COMPONENTS.VERSION");
			attribute2dbField.put("revisionITM03","ITM03_COMPONENTS.REVISION");
			attribute2dbField.put("qtyITM03","ITM03_COMPONENTS.QTY");
			attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
			attribute2dbField.put("enabledITM03","ITM03_COMPONENTS.ENABLED");
			attribute2dbField.put("minSellingQtyUmCodeReg02ITM01","ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02");

			ItemPK pk = (ItemPK)gridParams.getOtherGridParams().get(ApplicationConsts.ITEM_PK);

			if (pk==null) {
				// case: item frame in INSERT mode...
				return new VOListResponse(new ArrayList(),false,0);
			}

			ArrayList values = new ArrayList();
			values.add(langId);
			values.add(pk.getCompanyCodeSys01ITM01());
			values.add(pk.getItemCodeITM01());

			// read from ITM03 table...
			Response answer = QueryUtil.getQuery(
					conn,
					new UserSessionParameters(username),
					sql,
					values,
					attribute2dbField,
					ComponentVO.class,
					"Y",
					"N",
					null,
					gridParams,
					true
			);

			if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

		} catch (Exception ex1) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching an existing components",ex1);
			throw new Exception(ex1.getMessage());
		} finally {
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
	public VOListResponse updateComponents(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username) throws Throwable {
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;

			ComponentVO oldVO = null;
			ComponentVO newVO = null;
			Response res = null;

			HashSet pkAttrs = new HashSet();
			pkAttrs.add("companyCodeSys01ITM03");
			pkAttrs.add("itemCodeItm01ITM03");
			pkAttrs.add("parentItemCodeItm01ITM03");

			HashMap attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01ITM03","COMPANY_CODE_SYS01");
			attribute2dbField.put("itemCodeItm01ITM03","ITEM_CODE_ITM01");
			attribute2dbField.put("parentItemCodeItm01ITM03","PARENT_ITEM_CODE_ITM01");
			attribute2dbField.put("sequenceITM03","SEQUENCE");
			attribute2dbField.put("startDateITM03","START_DATE");
			attribute2dbField.put("endDateITM03","END_DATE");
			attribute2dbField.put("versionITM03","VERSION");
			attribute2dbField.put("revisionITM03","REVISION");
			attribute2dbField.put("qtyITM03","QTY");
			attribute2dbField.put("enabledITM03","ENABLED");

			for(int i=0;i<oldVOs.size();i++) {
				oldVO = (ComponentVO)oldVOs.get(i);
				newVO = (ComponentVO)newVOs.get(i);

				res = new QueryUtil().updateTable(
						conn,
						new UserSessionParameters(username),
						pkAttrs,
						oldVO,
						newVO,
						"ITM03_COMPONENTS",
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

			return new VOListResponse(newVOs,false,newVOs.size());
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing components",ex);
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
	public VOResponse deleteAltComponents(ArrayList list,String serverLanguageId,String username) throws Throwable {
		Statement stmt = null;
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;
			stmt = conn.createStatement();

			AltComponentVO vo = null;
			for(int i=0;i<list.size();i++) {
				// phisically delete records in ITM04...
				vo = (AltComponentVO)list.get(i);
				stmt.execute(
						"delete from ITM04_ALTERNATIVE_ITEMS where "+
						"COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01ITM04()+"' and "+
						"ITEM_CODE_ITM01='"+vo.getItemCodeItm01ITM04()+"' and "+
						"PROGRESSIVE="+vo.getProgressiveITM04()
				);
			}

			return new VOResponse(new Boolean(true));
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing alternative components",ex);
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
	public VOResponse deleteBillOfMaterialsData(String reportId,String serverLanguageId,String username) throws Throwable {
		PreparedStatement pstmt = null;
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;

			pstmt = conn.prepareStatement(
					"delete from TMP01_BILL_OF_MATERIALS where REPORT_ID=?"
			);
			pstmt.setObject(1,reportId);

			return new VOResponse(reportId);
		} catch (Exception ex1) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while removing data from TMP01 table",ex1);
			try {
				if (this.conn==null && conn!=null)
					// rollback only local connection
					conn.rollback();
			}
			catch (Exception ex3) {
			}
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
	public VOResponse deleteComponents(ArrayList list,String serverLanguageId,String username) throws Throwable {
		Statement stmt = null;
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;
			stmt = conn.createStatement();

			ComponentVO vo = null;
			for(int i=0;i<list.size();i++) {
				// phisically delete records in ITM04...
				vo = (ComponentVO)list.get(i);
				stmt.execute(
						"delete from ITM03_COMPONENTS where "+
						"COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01ITM03()+"' and "+
						"ITEM_CODE_ITM01='"+vo.getItemCodeItm01ITM03()+"' and "+
						"PARENT_ITEM_CODE_ITM01='"+vo.getParentItemCodeItm01ITM03()+"'"
				);
			}

			return new VOResponse(new Boolean(true));
		}
		catch (Throwable ex) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing components",ex);
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
	public VOListResponse insertAltComponents(ArrayList list,String serverLanguageId,String username) throws Throwable {
		PreparedStatement pstmt = null;
		ResultSet rset = null;

		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;
			Map attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01ITM04","COMPANY_CODE_SYS01");
			attribute2dbField.put("itemCodeItm01ITM04","ITEM_CODE_ITM01");
			attribute2dbField.put("progressiveITM04","PROGRESSIVE");

			// check if there already exist the specified components in some group (in some PROGRESSIVE...)
			String items = "";
			AltComponentVO vo = null;
			for(int i=0;i<list.size();i++) {
				vo = (AltComponentVO) list.get(i);
				items += "'"+vo.getItemCodeItm01ITM04()+"',";
			}
			if (items.length()>0)
				items = items.substring(0,items.length()-1);
			pstmt = conn.prepareStatement(
					"select PROGRESSIVE,ITEM_CODE_ITM01 from ITM04_ALTERNATIVE_ITEMS where COMPANY_CODE_SYS01=? and ITEM_CODE_ITM01 in ("+items+")"
			);
			pstmt.setString(1,vo.getCompanyCodeSys01ITM04());
			rset = pstmt.executeQuery();
			ArrayList progressives = new ArrayList();
			HashSet itms = new HashSet();
			while(rset.next()) {
				progressives.add(rset.getBigDecimal(1));
				itms.add(rset.getString(2));
			}
			rset.close();
			pstmt.close();

			// if progressives size = 0 then add all components to the current item group
			// if progressives size = 1 then add all components to the fetched group (fetched PROGRESSIVE)
			// if progressives size > 1 then join all groups found and add all components to the unique group just created
			BigDecimal progressiveITM04 = null;
			Response res = null;
			if (progressives.size()==0) {
				vo = (AltComponentVO) list.get(0);
				if (vo.getProgressiveITM04()==null) {

					// maybe there exists ONE record for the current item...
					pstmt = conn.prepareStatement(
							"select PROGRESSIVE from ITM04_ALTERNATIVE_ITEMS where COMPANY_CODE_SYS01=? and ITEM_CODE_ITM01=?"
					);
					pstmt.setString(1,vo.getCompanyCodeSys01ITM04());
					pstmt.setString(2,vo.getCurrentItemCodeItm01ITM04());
					rset = pstmt.executeQuery();
					if (rset.next())
						progressiveITM04 = rset.getBigDecimal(1);
					rset.close();
					pstmt.close();
					if (progressiveITM04==null) {
						// this is the first alternative component defined for this item: create a new progressive...
						progressiveITM04 = CompanyProgressiveUtils.getInternalProgressive(vo.getCompanyCodeSys01ITM04(),"ITM04_ALTERNATIVE_ITEMS", "PROGRESSIVE", conn);
						// insert current item into ITM04...
						AltComponentVO currVO = new AltComponentVO();
						currVO.setCompanyCodeSys01ITM04(vo.getCompanyCodeSys01ITM04());
						currVO.setCurrentItemCodeItm01ITM04(vo.getCurrentItemCodeItm01ITM04());
						currVO.setItemCodeItm01ITM04(vo.getCurrentItemCodeItm01ITM04());
						currVO.setProgressiveITM04(progressiveITM04);
						res = QueryUtil.insertTable(
								conn,
								new UserSessionParameters(username),
								currVO,
								"ITM04_ALTERNATIVE_ITEMS",
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
				}
				else
					progressiveITM04 = vo.getProgressiveITM04();
			}
			else if (progressives.size()==1) {
				// add all components to the fetched group (fetched PROGRESSIVE)
				progressiveITM04 = (BigDecimal)progressives.get(0);
				// insert current item into ITM04...
				AltComponentVO currVO = new AltComponentVO();
				currVO.setCompanyCodeSys01ITM04(vo.getCompanyCodeSys01ITM04());
				currVO.setCurrentItemCodeItm01ITM04(vo.getCurrentItemCodeItm01ITM04());
				currVO.setItemCodeItm01ITM04(vo.getCurrentItemCodeItm01ITM04());
				currVO.setProgressiveITM04(progressiveITM04);
				res = QueryUtil.insertTable(
						conn,
						new UserSessionParameters(username),
						currVO,
						"ITM04_ALTERNATIVE_ITEMS",
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
			else if (progressives.size()>1) {
				// join all groups found and add all components to the unique group just created
				String progrs = "";
				for(int i=0;i<progressives.size();i++)
					progrs += progressives.get(i)+",";
				progrs = progrs.substring(0,progrs.length()-1);
				pstmt = conn.prepareStatement(
						"select ITEM_CODE_ITM01 from ITM04_ALTERNATIVE_ITEMS where COMPANY_CODE_SYS01=? and PROGRESSIVE in ("+progrs+")"
				);
				pstmt.setString(1,vo.getCompanyCodeSys01ITM04());
				rset = pstmt.executeQuery();
				itms = new HashSet();
				while(rset.next())
					if (!rset.getString(1).equals(vo.getCurrentItemCodeItm01ITM04()))
						itms.add(rset.getString(1));
				rset.close();
				pstmt.close();

				// delete all items of all retrieved groups...
				pstmt = conn.prepareStatement(
						"delete from ITM04_ALTERNATIVE_ITEMS where COMPANY_CODE_SYS01=? and PROGRESSIVE in ("+progrs+")"
				);
				pstmt.execute();

				// insert all items in a single new group...
				progressiveITM04 = CompanyProgressiveUtils.getInternalProgressive(vo.getCompanyCodeSys01ITM04(),"ITM04_ALTERNATIVE_ITEMS", "PROGRESSIVE", conn);
				// insert current item into ITM04...
				AltComponentVO currVO = new AltComponentVO();
				currVO.setCompanyCodeSys01ITM04(vo.getCompanyCodeSys01ITM04());
				currVO.setCurrentItemCodeItm01ITM04(vo.getCurrentItemCodeItm01ITM04());
				currVO.setItemCodeItm01ITM04(vo.getCurrentItemCodeItm01ITM04());
				currVO.setProgressiveITM04(progressiveITM04);
				res = QueryUtil.insertTable(
						conn,
						new UserSessionParameters(username),
						currVO,
						"ITM04_ALTERNATIVE_ITEMS",
						attribute2dbField,
						"Y",
						"N",
						null,
						true
				);
				if (res.isError()) {
					throw new Exception(res.getErrorMessage());
				}

				Iterator it = itms.iterator();
				while(it.hasNext()) {
					currVO = new AltComponentVO();
					currVO.setCompanyCodeSys01ITM04(vo.getCompanyCodeSys01ITM04());
					currVO.setCurrentItemCodeItm01ITM04(vo.getCurrentItemCodeItm01ITM04());
					currVO.setItemCodeItm01ITM04(it.next().toString());
					currVO.setProgressiveITM04(progressiveITM04);
					res = QueryUtil.insertTable(
							conn,
							new UserSessionParameters(username),
							currVO,
							"ITM04_ALTERNATIVE_ITEMS",
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

			}


			// add alternative components to the calculated progressive...
			for(int i=0;i<list.size();i++) {
				vo = (AltComponentVO)list.get(i);
				vo.setProgressiveITM04(progressiveITM04);
				if (progressives.size()!=1 || !itms.contains(vo.getItemCodeItm01ITM04())) {
					// insert into ITM04...
					res = QueryUtil.insertTable(
							conn,
							new UserSessionParameters(username),
							vo,
							"ITM04_ALTERNATIVE_ITEMS",
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
			}

			return new VOListResponse(list,false,list.size());
		}
		catch (Throwable ex) {
			Logger.error(username, this.getClass().getName(),
					"executeCommand", "Error while inserting new alternative components", ex);
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
				if (rset!=null)
					rset.close();
			}
			catch (Exception ex4) {
			}
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
	public VOListResponse insertComponents(ArrayList list,String serverLanguageId,String username,String t1) throws Throwable {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;

			Map attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01ITM03","COMPANY_CODE_SYS01");
			attribute2dbField.put("itemCodeItm01ITM03","ITEM_CODE_ITM01");
			attribute2dbField.put("parentItemCodeItm01ITM03","PARENT_ITEM_CODE_ITM01");
			attribute2dbField.put("sequenceITM03","SEQUENCE");
			attribute2dbField.put("startDateITM03","START_DATE");
			attribute2dbField.put("endDateITM03","END_DATE");
			attribute2dbField.put("versionITM03","VERSION");
			attribute2dbField.put("revisionITM03","REVISION");
			attribute2dbField.put("qtyITM03","QTY");
			attribute2dbField.put("enabledITM03","ENABLED");

      pstmt = conn.prepareStatement(
		     "SELECT PARENT_ITEM_CODE_ITM01 FROM ITM03_COMPONENTS WHERE COMPANY_CODE_SYS01=? AND ITEM_CODE_ITM01=? AND ENABLED='Y'"
		  );

			Response res = null;
			ComponentVO vo = null;
			String parentItemCode = null;
			for(int i=0;i<list.size();i++) {
				vo = (ComponentVO)list.get(i);

	      // loops are not allowed: analyze all ancestors of the parent item code: if one of these is the
				// item to insert, then fire an error...
	      parentItemCode = vo.getParentItemCodeItm01ITM03();
				do {
					pstmt.setString(1,vo.getCompanyCodeSys01ITM03());
					pstmt.setString(2,parentItemCode);
					rset = pstmt.executeQuery();
					if (rset.next())
						parentItemCode = rset.getString(1);
					else
						parentItemCode = null;
					rset.close();
					if (parentItemCode!=null && parentItemCode.equals(vo.getItemCodeItm01ITM03()))
						throw new Exception(vo.getItemCodeItm01ITM03()+" "+t1+" "+vo.getParentItemCodeItm01ITM03());
				}
				while(parentItemCode!=null);

				vo.setEnabledITM03("Y");

				// insert into ITM03...
				res = QueryUtil.insertTable(
						conn,
						new UserSessionParameters(username),
						vo,
						"ITM03_COMPONENTS",
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
			Logger.error(username, this.getClass().getName(),
					"executeCommand", "Error while inserting new components", ex);
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
					rset.close();
			}
			catch (Exception exx) {}
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
	public VOListResponse loadAltComponents(GridParams gridParams,String langId,String username) throws Throwable {
		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;

			String sql =
				"select ITM04_ALTERNATIVE_ITEMS.COMPANY_CODE_SYS01,ITM04_ALTERNATIVE_ITEMS.ITEM_CODE_ITM01,"+
				"SYS10_TRANSLATIONS.DESCRIPTION,ITM04_ALTERNATIVE_ITEMS.PROGRESSIVE,ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02 "+
				"from ITM01_ITEMS,SYS10_TRANSLATIONS,ITM04_ALTERNATIVE_ITEMS "+
				"where "+
				"ITM04_ALTERNATIVE_ITEMS.COMPANY_CODE_SYS01 = ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
				"ITM04_ALTERNATIVE_ITEMS.ITEM_CODE_ITM01 = ITM01_ITEMS.ITEM_CODE and "+
				"ITM01_ITEMS.PROGRESSIVE_SYS10 = SYS10_TRANSLATIONS.PROGRESSIVE and "+
				"SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
				"ITM04_ALTERNATIVE_ITEMS.COMPANY_CODE_SYS01=? and "+
				"not ITM04_ALTERNATIVE_ITEMS.ITEM_CODE_ITM01=? and "+
				"ITM04_ALTERNATIVE_ITEMS.PROGRESSIVE in ("+
				"select PROGRESSIVE from ITM04_ALTERNATIVE_ITEMS where COMPANY_CODE_SYS01=? and ITM04_ALTERNATIVE_ITEMS.ITEM_CODE_ITM01=?) ";

			Map attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01ITM04","ITM04_ALTERNATIVE_ITEMS.COMPANY_CODE_SYS01");
			attribute2dbField.put("itemCodeItm01ITM04","ITM04_ALTERNATIVE_ITEMS.ITEM_CODE_ITM01");
			attribute2dbField.put("progressiveITM04","ITM04_ALTERNATIVE_ITEMS.PROGRESSIVE");
			attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
			attribute2dbField.put("minSellingQtyUmCodeReg02ITM01","ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02");

			ItemPK pk = (ItemPK)gridParams.getOtherGridParams().get(ApplicationConsts.ITEM_PK);

			ArrayList values = new ArrayList();
			values.add(langId);
			values.add(pk.getCompanyCodeSys01ITM01());
			values.add(pk.getItemCodeITM01());
			values.add(pk.getCompanyCodeSys01ITM01());
			values.add(pk.getItemCodeITM01());

			// read from ITM04 table...
			Response res = QueryUtil.getQuery(
					conn,
					new UserSessionParameters(username),
					sql,
					values,
					attribute2dbField,
					AltComponentVO.class,
					"Y",
					"N",
					null,
					gridParams,
					true
			);

			if (res.isError())
				throw new Exception(res.getErrorMessage());

			java.util.List rows = ((VOListResponse)res).getRows();
			AltComponentVO vo = null;
			for(int i=0;i<rows.size();i++) {
				vo = (AltComponentVO)rows.get(i);
				vo.setCurrentItemCodeItm01ITM04(pk.getItemCodeITM01());
			}

			Response answer = res;


			if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;
		} catch (Exception ex1) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching an existing alternative components",ex1);
			throw new Exception(ex1.getMessage());
		} finally {
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
	public VOResponse createBillOfMaterialsData(ItemPK pk,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
		PreparedStatement pstmt = null;

		Connection conn = null;
		try {
			if (this.conn==null) conn = getConn(); else conn = this.conn;
			compCurr.setConn(conn); // use same transaction...

			Response res = compCurr.loadCompanyCurrency(pk.getCompanyCodeSys01ITM01(),serverLanguageId,username,customizedFields);
			if (res.isError())
				throw new Exception(res.getErrorMessage());
			CurrencyVO currVO = (CurrencyVO)((VOResponse)res).getVo();
			String pattern = currVO.getCurrencySymbolREG03()+" #"+currVO.getThousandSymbolREG03()+"##0"+currVO.getDecimalSymbolREG03();
			if (currVO.getDecimalsREG03().intValue()>0)
				for(int i=0;i<currVO.getDecimalsREG03().intValue();i++)
					pattern += "0";
			else
				pattern = "#";
			DecimalFormat format = new DecimalFormat(pattern);
			DecimalFormat qtyFormat = new DecimalFormat("###,##");

			res = BillOfMaterialsUtil.getBillOfMaterials(conn,compCurr,pk,serverLanguageId,username,customizedFields);
			if (res.isError())
				throw new Exception(res.getErrorMessage());

			BigDecimal reportId = CompanyProgressiveUtils.getInternalProgressive(pk.getCompanyCodeSys01ITM01(),"TMP01_BILL_OF_MATERIALS","REPORT_ID",conn);
			TreeModel model = (TreeModel)((VOResponse)res).getVo();
			DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();

			pstmt = conn.prepareStatement(
					"insert into TMP01_BILL_OF_MATERIALS(REPORT_ID,PROGRESSIVE,COMPANY_CODE,PAD,ITEM_CODE,DESCRIPTION,LEV,QTY,UM,PRICE,TOTAL_PRICE,COST,TOTAL_COST) values(?,?,?,?,?,?,?,?,?,?,?,?,?)"
			);
			expandNode(0,"",0,null,format,qtyFormat,reportId,pstmt,root);

			return new VOResponse(reportId);
		} catch (Exception ex1) {
			Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching bill of materials and fill in TMP01 table",ex1);
			try {
				if (this.conn==null && conn!=null)
					// rollback only local connection
					conn.rollback();
			}
			catch (Exception ex3) {
			}
			throw new Exception(ex1.getMessage());
		} finally {
			try {
				pstmt.close();
			}
			catch (Exception ex) {
			}
			try {
				compCurr.setConn(null);
			} catch (Exception ex) {}
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
	 * Expand the current node and fill in TMP01 table.
	 */
	private int expandNode(int level,String lastPad,int progressive,DefaultMutableTreeNode parentNode,DecimalFormat format,DecimalFormat qtyFormat,BigDecimal reportId,PreparedStatement pstmt,DefaultMutableTreeNode node) throws Exception {
		MaterialVO vo = (MaterialVO)node.getUserObject();
		String pad = "";

		if (level>0) {
			//      pad = "|";
			//      int spaces = level*2-1;
			//      for(int i=0;i<spaces;i++)
			//        pad += " ";
			//      if (level>1)
			//        pad += "|";

			pad = lastPad+"| ";

			pstmt.setBigDecimal(1,reportId);
			pstmt.setInt(2,progressive);
			pstmt.setString(3,vo.getCompanyCodeSys01ITM03());
			pstmt.setString(4,pad);
			pstmt.setString(5,null);
			pstmt.setString(6,null);
			pstmt.setBigDecimal(7,null);
			pstmt.setString(8,null);
			pstmt.setString(9,null);
			pstmt.setString(10,null);
			pstmt.setString(11,null);
			pstmt.setString(12,null);
			pstmt.setString(13,null);
			pstmt.execute();

			progressive++;
		}

		if (level>0) {
			//      if (level==1)
			//        pad = "+-";
			//      else {
			//        pad = "|";
			//        int spaces = level*2-1;
			//        for(int i=0;i<spaces;i++)
			//          pad += " ";
			//        pad += "+-";
			//      }
			pad = lastPad+"+-";
		}
		else
			pad = "";

		pstmt.setBigDecimal(1,reportId);
		pstmt.setInt(2,progressive);
		pstmt.setString(3,vo.getCompanyCodeSys01ITM03());
		pstmt.setString(4,pad);
		pstmt.setString(5,vo.getItemCodeItm01ITM03());
		pstmt.setString(6,vo.getDescriptionSYS10());
		pstmt.setInt(7,level);
		pstmt.setString(8,qtyFormat.format(vo.getQtyITM03()));
		pstmt.setString(9,vo.getMinSellingQtyUmCodeReg02ITM01());
		pstmt.setString(10,vo.getValuePUR04()==null?null:format.format(vo.getValuePUR04().doubleValue()));
		pstmt.setString(11,vo.getTotalPrices()==null?null:format.format(vo.getTotalPrices().doubleValue()));
		pstmt.setString(12,vo.getValuePRO02()==null?null:format.format(vo.getValuePRO02().doubleValue()));
		pstmt.setString(13,vo.getTotalCosts()==null?null:format.format(vo.getTotalCosts().doubleValue()));
		pstmt.execute();

		progressive++;


		//    pad = "|";
		//    if (level>0) {
		//      int spaces = level*2-1;
		//      for(int i=0;i<spaces;i++)
		//        pad += " ";
		//      if (level>1)
		//        pad += "|";
		//    }
		//
		//    pstmt.setBigDecimal(1,reportId);
		//    pstmt.setInt(2,progressive);
		//    pstmt.setString(3,pad);
		//    pstmt.setString(4,null);
		//    pstmt.setBigDecimal(5,null);
		//    pstmt.setString(6,null);
		//    pstmt.setString(7,null);
		//    pstmt.setString(8,null);
		//    pstmt.setString(9,null);
		//    pstmt.setString(10,null);
		//    pstmt.setString(11,null);
		//    pstmt.execute();
		//
		//    progressive++;

		for(int j=0;j<node.getChildCount();j++) {
			progressive = expandNode(level+1,lastPad+(node.getNextLeaf()!=null?"| ":""),progressive,node,format,qtyFormat,reportId,pstmt,(DefaultMutableTreeNode)node.getChildAt(j));
		}
		return progressive;
	}



}

