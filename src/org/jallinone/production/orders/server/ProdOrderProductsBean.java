package org.jallinone.production.orders.server;

import org.openswing.swing.server.*;

import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.production.orders.java.*;
import org.jallinone.system.server.*;
import java.math.*;

import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage production order products.</p>
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
public class ProdOrderProductsBean  implements ProdOrderProducts {


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




  public ProdOrderProductsBean() {
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public ProdOrderProductVO getProdOrderProduct(ProdOrderPK pk) {
	  throw new UnsupportedOperationException();
  }


  /**
   * Business logic to execute.
   */
  public VOListResponse insertProdOrderProducts(ArrayList list,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC23","COMPANY_CODE_SYS01");
      attribute2dbField.put("docYearDOC23","DOC_YEAR");
      attribute2dbField.put("docNumberDOC23","DOC_NUMBER");
      attribute2dbField.put("itemCodeItm01DOC23","ITEM_CODE_ITM01");
      attribute2dbField.put("qtyDOC23","QTY");
      attribute2dbField.put("progressiveHie01DOC23","PROGRESSIVE_HIE01");
      attribute2dbField.put("progressiveHie02DOC23","PROGRESSIVE_HIE02");

      Response res = null;
      ProdOrderProductVO vo = null;
      for(int i=0;i<list.size();i++) {
        vo = (ProdOrderProductVO)list.get(i);

        // insert into DOC23...
        res = QueryUtil.insertTable(
            conn,
            new UserSessionParameters(username),
            vo,
            "DOC23_PRODUCTION_PRODUCTS",
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

      // update order state...
      pstmt = conn.prepareStatement("update DOC22_PRODUCTION_ORDER set DOC_STATE=? where COMPANY_CODE_SYS01=? and DOC_YEAR=? and DOC_NUMBER=?");
      pstmt.setString(1,ApplicationConsts.HEADER_BLOCKED);
      pstmt.setString(2,vo.getCompanyCodeSys01DOC23());
      pstmt.setBigDecimal(3,vo.getDocYearDOC23());
      pstmt.setBigDecimal(4,vo.getDocNumberDOC23());
      pstmt.execute();
      pstmt.close();

      return new VOListResponse(list,false,list.size());
    }
    catch (Throwable ex) {
    	Logger.error(username,this.getClass().getName(),"executeCommand","Error while inserting new production order products",ex);
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
  public VOListResponse loadProdOrderComponents(GridParams pars,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      ProdOrderPK pk = (ProdOrderPK)pars.getOtherGridParams().get(ApplicationConsts.PROD_ORDER_PK);

      String sql =
          "select DOC24_PRODUCTION_COMPONENTS.COMPANY_CODE_SYS01,DOC24_PRODUCTION_COMPONENTS.DOC_YEAR,"+
          "DOC24_PRODUCTION_COMPONENTS.DOC_NUMBER,DOC24_PRODUCTION_COMPONENTS.ITEM_CODE_ITM01,SYS10_TRANSLATIONS.DESCRIPTION,"+
          "DOC24_PRODUCTION_COMPONENTS.QTY,DOC24_PRODUCTION_COMPONENTS.PROGRESSIVE_HIE01,B.DESCRIPTION,ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02 "+
          " from DOC24_PRODUCTION_COMPONENTS,ITM01_ITEMS,SYS10_TRANSLATIONS,SYS10_TRANSLATIONS B "+
          " where "+
          "DOC24_PRODUCTION_COMPONENTS.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
          "DOC24_PRODUCTION_COMPONENTS.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and "+
          "ITM01_ITEMS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "DOC24_PRODUCTION_COMPONENTS.COMPANY_CODE_SYS01=? and "+
          "DOC24_PRODUCTION_COMPONENTS.DOC_YEAR=? and "+
          "DOC24_PRODUCTION_COMPONENTS.DOC_NUMBER=? and "+
          "DOC24_PRODUCTION_COMPONENTS.PROGRESSIVE_HIE01=B.PROGRESSIVE and "+
          "B.LANGUAGE_CODE=? ";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC24","DOC24_PRODUCTION_COMPONENTS.COMPANY_CODE_SYS01");
      attribute2dbField.put("docYearDOC24","DOC24_PRODUCTION_COMPONENTS.DOC_YEAR");
      attribute2dbField.put("docNumberDOC24","DOC24_PRODUCTION_COMPONENTS.DOC_NUMBER");
      attribute2dbField.put("itemCodeItm01DOC24","DOC24_PRODUCTION_COMPONENTS.ITEM_CODE_ITM01");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("qtyDOC24","DOC24_PRODUCTION_COMPONENTS.QTY");
      attribute2dbField.put("progressiveHie01DOC24","DOC24_PRODUCTION_COMPONENTS.PROGRESSIVE_HIE01");
      attribute2dbField.put("locationDescriptionSYS10","B.DESCRIPTION");
      attribute2dbField.put("minSellingQtyUmCodeReg02ITM01","ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(pk.getCompanyCodeSys01DOC22());
      values.add(pk.getDocYearDOC22());
      values.add(pk.getDocNumberDOC22());
      values.add(serverLanguageId);

      // read from DOC24 table...
      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          ProdOrderComponentVO.class,
          "Y",
          "N",
          null,
          pars,
          true
      );


      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching production order components",ex);
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
  public VOListResponse loadProdOrderProducts(GridParams pars,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      ProdOrderPK pk = (ProdOrderPK)pars.getOtherGridParams().get(ApplicationConsts.PROD_ORDER_PK);

      String sql =
          "select DOC23_PRODUCTION_PRODUCTS.COMPANY_CODE_SYS01,DOC23_PRODUCTION_PRODUCTS.DOC_YEAR,"+
          "DOC23_PRODUCTION_PRODUCTS.DOC_NUMBER,DOC23_PRODUCTION_PRODUCTS.ITEM_CODE_ITM01,A.DESCRIPTION,"+
          "DOC23_PRODUCTION_PRODUCTS.QTY,DOC23_PRODUCTION_PRODUCTS.PROGRESSIVE_HIE01,B.DESCRIPTION,"+
          "DOC22_PRODUCTION_ORDER.WAREHOUSE_CODE_WAR01,DOC23_PRODUCTION_PRODUCTS.PROGRESSIVE_HIE02 "+
          " from DOC23_PRODUCTION_PRODUCTS,ITM01_ITEMS,SYS10_TRANSLATIONS A,SYS10_TRANSLATIONS B,DOC22_PRODUCTION_ORDER "+
          " where "+
          "DOC23_PRODUCTION_PRODUCTS.COMPANY_CODE_SYS01=DOC22_PRODUCTION_ORDER.COMPANY_CODE_SYS01 and "+
          "DOC23_PRODUCTION_PRODUCTS.DOC_YEAR=DOC22_PRODUCTION_ORDER.DOC_YEAR and "+
          "DOC23_PRODUCTION_PRODUCTS.DOC_NUMBER=DOC22_PRODUCTION_ORDER.DOC_NUMBER and "+
          "DOC23_PRODUCTION_PRODUCTS.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
          "DOC23_PRODUCTION_PRODUCTS.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and "+
          "DOC23_PRODUCTION_PRODUCTS.PROGRESSIVE_HIE01=B.PROGRESSIVE and "+
          "B.LANGUAGE_CODE=? and "+
          "ITM01_ITEMS.PROGRESSIVE_SYS10=A.PROGRESSIVE and "+
          "A.LANGUAGE_CODE=? and "+
          "DOC23_PRODUCTION_PRODUCTS.COMPANY_CODE_SYS01=? and "+
          "DOC23_PRODUCTION_PRODUCTS.DOC_YEAR=? and "+
          "DOC23_PRODUCTION_PRODUCTS.DOC_NUMBER=? ";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC23","DOC23_PRODUCTION_PRODUCTS.COMPANY_CODE_SYS01");
      attribute2dbField.put("docYearDOC23","DOC23_PRODUCTION_PRODUCTS.DOC_YEAR");
      attribute2dbField.put("docNumberDOC23","DOC23_PRODUCTION_PRODUCTS.DOC_NUMBER");
      attribute2dbField.put("itemCodeItm01DOC23","DOC23_PRODUCTION_PRODUCTS.ITEM_CODE_ITM01");
      attribute2dbField.put("descriptionSYS10","A.DESCRIPTION");
      attribute2dbField.put("qtyDOC23","DOC23_PRODUCTION_PRODUCTS.QTY");
      attribute2dbField.put("locationDescriptionSYS10","B.DESCRIPTION");
      attribute2dbField.put("progressiveHie01DOC23","DOC23_PRODUCTION_PRODUCTS.PROGRESSIVE_HIE01");
      attribute2dbField.put("warehouseCodeWar01DOC22","DOC22_PRODUCTION_ORDER.WAREHOUSE_CODE_WAR01");
      attribute2dbField.put("progressiveHie02DOC23","DOC23_PRODUCTION_PRODUCTS.PROGRESSIVE_HIE02");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(serverLanguageId);
      values.add(pk.getCompanyCodeSys01DOC22());
      values.add(pk.getDocYearDOC22());
      values.add(pk.getDocNumberDOC22());

      // read from DOC23 table...
      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          ProdOrderProductVO.class,
          "Y",
          "N",
          null,
          pars,
          true
      );

      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching production order products",ex);
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
  public VOListResponse updateProdOrderProducts(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      ProdOrderProductVO oldVO = null;
      ProdOrderProductVO newVO = null;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC23","COMPANY_CODE_SYS01");
      attribute2dbField.put("docYearDOC23","DOC_YEAR");
      attribute2dbField.put("docNumberDOC23","DOC_NUMBER");
      attribute2dbField.put("itemCodeItm01DOC23","ITEM_CODE_ITM01");
      attribute2dbField.put("qtyDOC23","QTY");
      attribute2dbField.put("progressiveHie01DOC23","PROGRESSIVE_HIE01");
      attribute2dbField.put("progressiveHie02DOC23","PROGRESSIVE_HIE02");

      HashSet pkAttributes = new HashSet();
      pkAttributes.add("companyCodeSys01DOC23");
      pkAttributes.add("docYearDOC23");
      pkAttributes.add("docNumberDOC23");
      pkAttributes.add("itemCodeItm01DOC23");


      Response res = null;
      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (ProdOrderProductVO)oldVOs.get(i);
        newVO = (ProdOrderProductVO)newVOs.get(i);

        // update DOC23 table...
        res = QueryUtil.updateTable(
            conn,
            new UserSessionParameters(username),
            pkAttributes,
            oldVO,
            newVO,
            "DOC23_PRODUCTION_PRODUCTS",
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
    	Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing production order products",ex);
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
  public VOResponse deleteProdOrderProducts(ArrayList list,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      ProdOrderProductVO rowVO = null;

      pstmt = conn.prepareStatement(
          "delete from DOC23_PRODUCTION_PRODUCTS where COMPANY_CODE_SYS01=? and DOC_YEAR=? and DOC_NUMBER=? and ITEM_CODE_ITM01=?"
      );

      for(int i=0;i<list.size();i++) {
        rowVO = (ProdOrderProductVO)list.get(i);

        // phisically delete the record in DOC23...
        pstmt.setString(1,rowVO.getCompanyCodeSys01DOC23());
        pstmt.setBigDecimal(2,rowVO.getDocYearDOC23());
        pstmt.setBigDecimal(3,rowVO.getDocNumberDOC23());
        pstmt.setString(4,rowVO.getItemCodeItm01DOC23());
        pstmt.execute();
      }

      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
    	Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing production order products",ex);
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

