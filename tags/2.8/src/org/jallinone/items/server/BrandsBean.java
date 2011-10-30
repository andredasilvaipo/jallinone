package org.jallinone.items.server;

import org.openswing.swing.server.*;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;
import org.jallinone.system.translations.server.CompanyTranslationUtils;

import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.jallinone.variants.java.*;


import javax.sql.DataSource;
import org.jallinone.items.java.BrandVO;
import org.openswing.swing.message.send.java.LookupValidationParams;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage brands.</p>
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
public class BrandsBean implements Brands {


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


  public BrandsBean() {}


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
   */
  public BrandVO getBrand() {
	  throw new UnsupportedOperationException();
  }


  /**
   * Business logic to execute.
   */
  public VOListResponse loadBrands(GridParams gridParams,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      String companyCodeSys01 = (String)gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01);

      String sql =
          "select ITM31_BRANDS.COMPANY_CODE_SYS01,ITM31_BRANDS.BRAND_CODE,ITM31_BRANDS.DESCRIPTION,ITM31_BRANDS.ENABLED "+
          "from ITM31_BRANDS "+
          "where ITM31_BRANDS.COMPANY_CODE_SYS01=? and "+
          "ITM31_BRANDS.ENABLED='Y' ";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01ITM31","ITM31_BRANDS.COMPANY_CODE_SYS01");
      attribute2dbField.put("brandCodeITM31","ITM31_BRANDS.BRAND_CODE");
      attribute2dbField.put("descriptionITM31","ITM31_BRANDS.DESCRIPTION");
      attribute2dbField.put("enabledITM31","ITM31_BRANDS.ENABLED");

      ArrayList values = new ArrayList();
      values.add(companyCodeSys01);

      // read from ITM31 table...
      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          BrandVO.class,
          "Y",
          "N",
          null,
          gridParams,
          true
      );
      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching brands list",ex);
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
  public VOListResponse updateBrands(ArrayList oldVOs,ArrayList newVOs,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      BrandVO oldVO = null;
      BrandVO newVO = null;
      Response res = null;

      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (BrandVO)oldVOs.get(i);
        newVO = (BrandVO)newVOs.get(i);

        HashSet pkAttrs = new HashSet();
        pkAttrs.add("companyCodeSys01ITM31");
        pkAttrs.add("brandCodeITM31");

        HashMap attribute2dbField = new HashMap();
				attribute2dbField.put("companyCodeSys01ITM31","COMPANY_CODE_SYS01");
				attribute2dbField.put("brandCodeITM31","BRAND_CODE");
				attribute2dbField.put("descriptionITM31","DESCRIPTION");
        attribute2dbField.put("enabledITM31","ENABLED");

        res = org.jallinone.commons.server.QueryUtilExtension.updateTable(
            conn,
            new UserSessionParameters(username),
            pkAttrs,
            oldVO,
            newVO,
            "ITM31_BRANDS",
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
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing brands",ex);
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
  public VOListResponse insertBrands(java.util.List list,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      BrandVO vo = null;

      Map attribute2dbField = new HashMap();
			attribute2dbField.put("companyCodeSys01ITM31","COMPANY_CODE_SYS01");
			attribute2dbField.put("brandCodeITM31","BRAND_CODE");
			attribute2dbField.put("descriptionITM31","DESCRIPTION");
			attribute2dbField.put("enabledITM31","ENABLED");

      Response res = null;
      for(int i=0;i<list.size();i++) {
        vo = (BrandVO)list.get(i);
        vo.setEnabledITM31("Y");

        // insert into ITM31...
        res = org.jallinone.commons.server.QueryUtilExtension.insertTable(
            conn,
            new UserSessionParameters(username),
            vo,
            "ITM31_BRANDS",
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
                   "executeCommand", "Error while inserting new brands", ex);
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
  public VOResponse deleteBrands(java.util.ArrayList list,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      BrandVO vo = null;
      for(int i=0;i<list.size();i++) {
        // logically delete the record in ITMxxx...
        vo = (BrandVO)list.get(i);
				pstmt = conn.prepareStatement(
          "update ITM31_BRANDS set ENABLED='N',LAST_UPDATE_USER=?,LAST_UPDATE_DATE=?  "+
          "where COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01ITM31()+"' and BRAND_CODE='"+vo.getBrandCodeITM31()+"'"
        );
				pstmt.setString(1,username);
				pstmt.setTimestamp(2,new java.sql.Timestamp(System.currentTimeMillis()));
				pstmt.execute();
				pstmt.close();
      }

      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing variant types",ex);
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
		public VOListResponse validateBrandCode(LookupValidationParams pars,String username) throws Throwable {
			Connection conn = null;
			try {
				if (this.conn==null) conn = getConn(); else conn = this.conn;

				String companyCodeSys01 = (String)pars.getLookupValidationParameters().get(ApplicationConsts.COMPANY_CODE_SYS01);

				String sql =
						"select ITM31_BRANDS.COMPANY_CODE_SYS01,ITM31_BRANDS.BRAND_CODE,ITM31_BRANDS.DESCRIPTION,ITM31_BRANDS.ENABLED "+
						"from ITM31_BRANDS "+
						"where ITM31_BRANDS.COMPANY_CODE_SYS01=? and "+
						"ITM31_BRANDS.BRAND_CODE=? and "+
						"ITM31_BRANDS.ENABLED='Y' ";

				Map attribute2dbField = new HashMap();
				attribute2dbField.put("companyCodeSys01ITM31","ITM31_BRANDS.COMPANY_CODE_SYS01");
				attribute2dbField.put("brandCodeITM31","ITM31_BRANDS.BRAND_CODE");
				attribute2dbField.put("descriptionITM31","ITM31_BRANDS.DESCRIPTION");
				attribute2dbField.put("enabledITM31","ITM31_BRANDS.ENABLED");

				ArrayList values = new ArrayList();
				values.add(companyCodeSys01);
				values.add(pars.getCode());

				// read from ITM31 table...
				Response answer = QueryUtil.getQuery(
						conn,
						new UserSessionParameters(username),
						sql,
						values,
						attribute2dbField,
						BrandVO.class,
						"Y",
						"N",
						null,
						new GridParams(),
						true
				);
				if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

			}
			catch (Throwable ex) {
				Logger.error(username,this.getClass().getName(),"executeCommand","Error while validating brand code",ex);
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


}

