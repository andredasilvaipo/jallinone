package org.jallinone.purchases.items.server;

import org.openswing.swing.server.*;

import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.message.send.java.LookupValidationParams;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.hierarchies.java.HierarchyLevelVO;
import org.jallinone.items.java.*;
import org.jallinone.registers.measure.server.MeasuresBean;
import org.jallinone.system.server.*;
import java.math.*;

import org.jallinone.purchases.items.java.*;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage supplier items.</p>
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
public class SupplierItemsBean  implements SupplierItems {


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




  private MeasuresBean convBean;

  public void setConvBean(MeasuresBean convBean) {
    this.convBean = convBean;
  }



  public SupplierItemsBean() {
  }



  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
   */
  public SupplierItemVO getSupplierItem(HierarchyLevelVO pk) {
	  throw new UnsupportedOperationException();
  }


  /**
   * Insert new supplier items in PUR02 table. No commit or rollback are executed.
   */
  public final VOListResponse insertSupplierItems(ArrayList list,String langId,String username,ArrayList companiesList ,ArrayList customizedFields) throws Exception {
	Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      String companyCode = companiesList.get(0).toString();
      SupplierItemVO vo = null;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01PUR02","COMPANY_CODE_SYS01");
      attribute2dbField.put("itemCodeItm01PUR02","ITEM_CODE_ITM01");
      attribute2dbField.put("supplierItemCodePUR02","SUPPLIER_ITEM_CODE");
      attribute2dbField.put("progressiveReg04PUR02","PROGRESSIVE_REG04");
      attribute2dbField.put("progressiveHie02PUR02","PROGRESSIVE_HIE02");
      attribute2dbField.put("progressiveHie01PUR02","PROGRESSIVE_HIE01");
      attribute2dbField.put("minPurchaseQtyPUR02","MIN_PURCHASE_QTY");
      attribute2dbField.put("multipleQtyPUR02","MULTIPLE_QTY");
      attribute2dbField.put("umCodeReg02PUR02","UM_CODE_REG02");
      attribute2dbField.put("enabledPUR02","ENABLED");

      Response res = null;
      for(int i=0;i<list.size();i++) {
        vo = (SupplierItemVO)list.get(i);
        vo.setEnabledPUR02("Y");

        if (vo.getCompanyCodeSys01PUR02()==null)
          vo.setCompanyCodeSys01PUR02(companyCode);

        // insert into PUR02...
        res = CustomizeQueryUtil.insertTable(
            conn,
            new UserSessionParameters(username),
            vo,
            "PUR02_SUPPLIER_ITEMS",
            attribute2dbField,
            "Y",
            "N",
            null,
            true,
            customizedFields
        );

        if (res.isError() && list.size()==1) {
          throw new Exception(res.getErrorMessage());
        }

      }
      return new VOListResponse(list,false,list.size());
    }
    catch (Throwable ex) {
    	Logger.error(username,this.getClass().getName(),"insertSupplierItems","Error while inserting new supplier items",ex);
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
  public VOListResponse loadSupplierItems(GridParams pars,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      convBean.setConn(conn); // use same transaction...

      BigDecimal rootProgressiveHIE01 = (BigDecimal)pars.getOtherGridParams().get(ApplicationConsts.ROOT_PROGRESSIVE_HIE01);
      BigDecimal progressiveHIE01 = (BigDecimal)pars.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_HIE01);
      BigDecimal progressiveHIE02 = (BigDecimal)pars.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_HIE02);
      BigDecimal progressiveREG04 = (BigDecimal)pars.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04);
      String companyCodeSYS01 = (String)pars.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01);

      HierarchyLevelVO vo = (HierarchyLevelVO)pars.getOtherGridParams().get(ApplicationConsts.TREE_FILTER);
      if (vo!=null) {
        progressiveHIE01 = vo.getProgressiveHIE01();
        progressiveHIE02 = vo.getProgressiveHie02HIE01();
      }

      String sql =
          "select PUR02_SUPPLIER_ITEMS.COMPANY_CODE_SYS01,PUR02_SUPPLIER_ITEMS.ITEM_CODE_ITM01,PUR02_SUPPLIER_ITEMS.SUPPLIER_ITEM_CODE,PUR02_SUPPLIER_ITEMS.PROGRESSIVE_REG04,"+
          "PUR02_SUPPLIER_ITEMS.PROGRESSIVE_HIE02,PUR02_SUPPLIER_ITEMS.PROGRESSIVE_HIE01,PUR02_SUPPLIER_ITEMS.MIN_PURCHASE_QTY,PUR02_SUPPLIER_ITEMS.MULTIPLE_QTY,"+
          "PUR02_SUPPLIER_ITEMS.UM_CODE_REG02,PUR02_SUPPLIER_ITEMS.ENABLED,SYS10_TRANSLATIONS.DESCRIPTION,REG02_ALIAS1.DECIMALS,"+
          "ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02,REG02_ALIAS2.DECIMALS,ITM01_ITEMS.SERIAL_NUMBER_REQUIRED "+
          " from PUR02_SUPPLIER_ITEMS,SYS10_TRANSLATIONS,ITM01_ITEMS,REG02_MEASURE_UNITS REG02_ALIAS1,REG02_MEASURE_UNITS REG02_ALIAS2 where "+
          "PUR02_SUPPLIER_ITEMS.PROGRESSIVE_HIE02=? and "+
          "PUR02_SUPPLIER_ITEMS.UM_CODE_REG02=REG02_ALIAS1.UM_CODE and "+
          "PUR02_SUPPLIER_ITEMS.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
          "PUR02_SUPPLIER_ITEMS.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and "+
          "ITM01_ITEMS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "PUR02_SUPPLIER_ITEMS.COMPANY_CODE_SYS01 = ? and "+
          "PUR02_SUPPLIER_ITEMS.PROGRESSIVE_REG04=? and "+
          "PUR02_SUPPLIER_ITEMS.ENABLED='Y' and "+
          "ITM01_ITEMS.MIN_SELlING_QTY_UM_CODE_REG02=REG02_ALIAS2.UM_CODE ";
      if (Boolean.TRUE.equals(pars.getOtherGridParams().get(ApplicationConsts.SHOW_ITEMS_WITHOUT_VARIANTS)))
          sql +=
            " and ITM01_ITEMS.USE_VARIANT_1='N' "+
            " and ITM01_ITEMS.USE_VARIANT_2='N' "+
            " and ITM01_ITEMS.USE_VARIANT_3='N' "+
            " and ITM01_ITEMS.USE_VARIANT_4='N' "+
            " and ITM01_ITEMS.USE_VARIANT_5='N' ";
			if (Boolean.TRUE.equals(pars.getOtherGridParams().get(ApplicationConsts.SHOW_ONLY_MOVABLE_ITEMS)))
					sql +=
						" and ITM01_ITEMS.NO_WAREHOUSE_MOV='N' "; // show only items that can be move in warehouse

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
        sql += " and PUR02_SUPPLIER_ITEMS.PROGRESSIVE_HIE01 in ("+nodes+")";
      }

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01PUR02","PUR02_SUPPLIER_ITEMS.COMPANY_CODE_SYS01");
      attribute2dbField.put("itemCodeItm01PUR02","PUR02_SUPPLIER_ITEMS.ITEM_CODE_ITM01");
      attribute2dbField.put("supplierItemCodePUR02","PUR02_SUPPLIER_ITEMS.SUPPLIER_ITEM_CODE");
      attribute2dbField.put("progressiveReg04PUR02","PUR02_SUPPLIER_ITEMS.PROGRESSIVE_REG04");
      attribute2dbField.put("progressiveHie02PUR02","PUR02_SUPPLIER_ITEMS.PROGRESSIVE_HIE02");
      attribute2dbField.put("progressiveHie01PUR02","PUR02_SUPPLIER_ITEMS.PROGRESSIVE_HIE01");
      attribute2dbField.put("minPurchaseQtyPUR02","PUR02_SUPPLIER_ITEMS.MIN_PURCHASE_QTY");
      attribute2dbField.put("multipleQtyPUR02","PUR02_SUPPLIER_ITEMS.MULTIPLE_QTY");
      attribute2dbField.put("umCodeReg02PUR02","PUR02_SUPPLIER_ITEMS.UM_CODE_REG02");
      attribute2dbField.put("enabledPUR02","PUR02_SUPPLIER_ITEMS.ENABLED");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("decimalsREG02","REG02_ALIAS1.DECIMALS");
      attribute2dbField.put("minSellingQtyUmCodeReg02ITM01","ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02");
      attribute2dbField.put("minSellingQtyDecimalsReg02ITM01","REG02_ALIAS2.DECIMALS");
      attribute2dbField.put("serialNumberRequiredITM01","ITM01_ITEMS.SERIAL_NUMBER_REQUIRED");

      ArrayList values = new ArrayList();
      values.add(progressiveHIE02);
      values.add(serverLanguageId);
      values.add(companyCodeSYS01);
      values.add(progressiveREG04);

      // read from PUR02 table...
      Response res = CustomizeQueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          SupplierItemVO.class,
          "Y",
          "N",
          null,
          pars,
          50,
          true,
          customizedFields
      );


      if (!res.isError()) {
        java.util.List rows = ((VOListResponse)res).getRows();
        SupplierItemVO supplierVO = null;
        for(int i=0;i<rows.size();i++) {
          supplierVO = (SupplierItemVO)rows.get(i);
          supplierVO.setValueREG05(convBean.getConversion(
            supplierVO.getMinSellingQtyUmCodeReg02ITM01(),
            supplierVO.getUmCodeReg02PUR02(),
            serverLanguageId,
            username
          ));
        }
      }

      Response answer = res;



      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching supplier items list",ex);
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
        convBean.setConn(null);
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
   * Business logic to execute.
   */
  public VOResponse importAllItemsToSupplier(SupplierItemVO vo,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    PreparedStatement pstmt2 = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      pstmt = conn.prepareStatement(
        "select ITEM_CODE,PROGRESSIVE_HIE01,MIN_SELLING_QTY_UM_CODE_REG02 from ITM01_ITEMS where "+
        "ENABLED='Y' and COMPANY_CODE_SYS01=? and PROGRESSIVE_HIE02=?"
      );
      pstmt2 = conn.prepareStatement(
        "insert into PUR02_SUPPLIER_ITEMS(COMPANY_CODE_SYS01,ITEM_CODE_ITM01,SUPPLIER_ITEM_CODE,PROGRESSIVE_REG04,"+
        "PROGRESSIVE_HIE02,PROGRESSIVE_HIE01,MIN_PURCHASE_QTY,MULTIPLE_QTY,UM_CODE_REG02,ENABLED) values(?,?,?,?,?,?,?,?,?,?)"
      );

      pstmt.setString(1,vo.getCompanyCodeSys01PUR02());
      pstmt.setBigDecimal(2,vo.getProgressiveHie02PUR02());
      ResultSet rset = pstmt.executeQuery();
      while(rset.next()) {
        pstmt2.setString(1,vo.getCompanyCodeSys01PUR02());
        pstmt2.setString(2,rset.getString(1));
        pstmt2.setString(3,rset.getString(1));
        pstmt2.setBigDecimal(4,vo.getProgressiveReg04PUR02());
        pstmt2.setBigDecimal(5,vo.getProgressiveHie02PUR02());
        pstmt2.setBigDecimal(6,rset.getBigDecimal(2));
        pstmt2.setInt(7,1);
        pstmt2.setInt(8,1);
        pstmt2.setString(9,rset.getString(3));
        pstmt2.setString(10,"Y");
        try {
          pstmt2.executeUpdate();
        }
        catch (SQLException ex4) {
        }
      }
      rset.close();

      return new VOResponse(Boolean.TRUE);
    }
    catch (Throwable ex) {
      Logger.error(username, this.getClass().getName(),
                   "executeCommand", "Error while inserting price for all items", ex);
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
        pstmt2.close();
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
  public VOResponse deleteSupplierItems(ArrayList list,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      SupplierItemPK pk = null;

      stmt = conn.createStatement();

      for(int i=0;i<list.size();i++) {
        pk = (SupplierItemPK)list.get(i);

        // logically delete the record in PUR02...
        stmt.execute(
            "update PUR02_SUPPLIER_ITEMS set ENABLED='N' where "+
            "COMPANY_CODE_SYS01='"+pk.getCompanyCodeSys01PUR02()+"' and "+
            "ITEM_CODE_ITM01='"+pk.getItemCodeItm01PUR02()+"' and "+
            "PROGRESSIVE_REG04="+pk.getProgressiveReg04PUR02()
        );
      }


      return  new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing supplier items",ex);
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
  public VOListResponse updateSupplierItems(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      SupplierItemVO oldVO = null;
      SupplierItemVO newVO = null;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01PUR02","COMPANY_CODE_SYS01");
      attribute2dbField.put("itemCodeItm01PUR02","ITEM_CODE_ITM01");
      attribute2dbField.put("supplierItemCodePUR02","SUPPLIER_ITEM_CODE");
      attribute2dbField.put("progressiveReg04PUR02","PROGRESSIVE_REG04");
      attribute2dbField.put("progressiveHie02PUR02","PROGRESSIVE_HIE02");
      attribute2dbField.put("progressiveHie01PUR02","PROGRESSIVE_HIE01");
      attribute2dbField.put("minPurchaseQtyPUR02","MIN_PURCHASE_QTY");
      attribute2dbField.put("multipleQtyPUR02","MULTIPLE_QTY");
      attribute2dbField.put("umCodeReg02PUR02","UM_CODE_REG02");
      attribute2dbField.put("enabledPUR02","ENABLED");

      HashSet pkAttributes = new HashSet();
      pkAttributes.add("companyCodeSys01PUR02");
      pkAttributes.add("itemCodeItm01PUR02");
      pkAttributes.add("progressiveReg04PUR02");

      Response res = null;
      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (SupplierItemVO)oldVOs.get(i);
        newVO = (SupplierItemVO)newVOs.get(i);

        // update PUR02 table...
        res = CustomizeQueryUtil.updateTable(
            conn,
            new UserSessionParameters(username),
            pkAttributes,
            oldVO,
            newVO,
            "PUR02_SUPPLIER_ITEMS",
            attribute2dbField,
            "Y",
            "N",
            null,
            true,
            customizedFields
        );

        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }

      }
      return new VOListResponse(newVOs,false,newVOs.size());
    }
    catch (Throwable ex) {
    	Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing supplier items",ex);
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
  public VOListResponse validateSupplierItemCode(LookupValidationParams pars,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      convBean.setConn(conn); // use same transaction...

      String companyCodeSYS10 = (String)pars.getLookupValidationParameters().get(ApplicationConsts.COMPANY_CODE_SYS01);
      BigDecimal progressiveREG04 = (BigDecimal)pars.getLookupValidationParameters().get(ApplicationConsts.PROGRESSIVE_REG04);

      String sql =
          "select PUR02_SUPPLIER_ITEMS.COMPANY_CODE_SYS01,PUR02_SUPPLIER_ITEMS.ITEM_CODE_ITM01,PUR02_SUPPLIER_ITEMS.SUPPLIER_ITEM_CODE,PUR02_SUPPLIER_ITEMS.PROGRESSIVE_REG04,"+
          "PUR02_SUPPLIER_ITEMS.PROGRESSIVE_HIE02,PUR02_SUPPLIER_ITEMS.PROGRESSIVE_HIE01,PUR02_SUPPLIER_ITEMS.MIN_PURCHASE_QTY,PUR02_SUPPLIER_ITEMS.MULTIPLE_QTY,"+
          "PUR02_SUPPLIER_ITEMS.UM_CODE_REG02,PUR02_SUPPLIER_ITEMS.ENABLED,SYS10_TRANSLATIONS.DESCRIPTION,REG02_ALIAS1.DECIMALS,"+
          "ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02,REG02_ALIAS2.DECIMALS,ITM01_ITEMS.SERIAL_NUMBER_REQUIRED "+
          " from PUR02_SUPPLIER_ITEMS,SYS10_TRANSLATIONS,ITM01_ITEMS,REG02_MEASURE_UNITS REG02_ALIAS1,REG02_MEASURE_UNITS REG02_ALIAS2 where "+
          "PUR02_SUPPLIER_ITEMS.UM_CODE_REG02=REG02_ALIAS1.UM_CODE and "+
          "PUR02_SUPPLIER_ITEMS.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
          "PUR02_SUPPLIER_ITEMS.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and "+
          "ITM01_ITEMS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "PUR02_SUPPLIER_ITEMS.COMPANY_CODE_SYS01 = ? and "+
          "PUR02_SUPPLIER_ITEMS.PROGRESSIVE_REG04=? and "+
          "PUR02_SUPPLIER_ITEMS.ENABLED='Y' and "+
          "PUR02_SUPPLIER_ITEMS.ITEM_CODE_ITM01=? and "+
          "ITM01_ITEMS.MIN_SELlING_QTY_UM_CODE_REG02=REG02_ALIAS2.UM_CODE ";
      if (Boolean.TRUE.equals(pars.getLookupValidationParameters().get(ApplicationConsts.SHOW_ITEMS_WITHOUT_VARIANTS)))
          sql +=
            " and ITM01_ITEMS.USE_VARIANT_1='N' "+
            " and ITM01_ITEMS.USE_VARIANT_2='N' "+
            " and ITM01_ITEMS.USE_VARIANT_3='N' "+
            " and ITM01_ITEMS.USE_VARIANT_4='N' "+
            " and ITM01_ITEMS.USE_VARIANT_5='N' ";
			if (Boolean.TRUE.equals(pars.getLookupValidationParameters().get(ApplicationConsts.SHOW_ONLY_MOVABLE_ITEMS)))
					sql +=
						" and ITM01_ITEMS.NO_WAREHOUSE_MOV='N' "; // show only items that can be move in warehouse


      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01PUR02","PUR02_SUPPLIER_ITEMS.COMPANY_CODE_SYS01");
      attribute2dbField.put("itemCodeItm01PUR02","PUR02_SUPPLIER_ITEMS.ITEM_CODE_ITM01");
      attribute2dbField.put("supplierItemCodePUR02","PUR02_SUPPLIER_ITEMS.SUPPLIER_ITEM_CODE");
      attribute2dbField.put("progressiveReg04PUR02","PUR02_SUPPLIER_ITEMS.PROGRESSIVE_REG04");
      attribute2dbField.put("progressiveHie02PUR02","PUR02_SUPPLIER_ITEMS.PROGRESSIVE_HIE02");
      attribute2dbField.put("progressiveHie01PUR02","PUR02_SUPPLIER_ITEMS.PROGRESSIVE_HIE01");
      attribute2dbField.put("minPurchaseQtyPUR02","PUR02_SUPPLIER_ITEMS.MIN_PURCHASE_QTY");
      attribute2dbField.put("multipleQtyPUR02","PUR02_SUPPLIER_ITEMS.MULTIPLE_QTY");
      attribute2dbField.put("umCodeReg02PUR02","PUR02_SUPPLIER_ITEMS.UM_CODE_REG02");
      attribute2dbField.put("enabledPUR02","PUR02_SUPPLIER_ITEMS.ENABLED");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("decimalsREG02","REG02_ALIAS1.DECIMALS");
      attribute2dbField.put("minSellingQtyUmCodeReg02ITM01","ITM01_ITEMS.MIN_SELLING_QTY_UM_CODE_REG02");
      attribute2dbField.put("minSellingQtyDecimalsReg02ITM01","REG02_ALIAS2.DECIMALS");
      attribute2dbField.put("serialNumberRequiredITM01","ITM01_ITEMS.SERIAL_NUMBER_REQUIRED");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(companyCodeSYS10);
      values.add(progressiveREG04);
      values.add(pars.getCode());

      // read from PUR02 table...
      Response res = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          SupplierItemVO.class,
          "Y",
          "N",
          null,
          new GridParams(),
          true
      );

      if (!res.isError()) {
        java.util.List rows = ((VOListResponse)res).getRows();
        SupplierItemVO supplierVO = null;
        for(int i=0;i<rows.size();i++) {
          supplierVO = (SupplierItemVO)rows.get(i);
          supplierVO.setValueREG05(convBean.getConversion(
            supplierVO.getMinSellingQtyUmCodeReg02ITM01(),
            supplierVO.getUmCodeReg02PUR02(),
            serverLanguageId,
            username
          ));
        }
      }

      Response answer = res;
      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while validating supplier item code",ex);
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
        convBean.setConn(null);
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




}

