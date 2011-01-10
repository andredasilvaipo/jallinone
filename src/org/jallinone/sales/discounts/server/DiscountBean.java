package org.jallinone.sales.discounts.server;

import javax.sql.DataSource;

import java.sql.*;
import org.jallinone.sales.discounts.java.*;
import org.openswing.swing.message.receive.java.*;
import org.jallinone.system.translations.server.*;
import java.util.*;
import org.openswing.swing.server.*;
import org.openswing.swing.server.*;
import javax.servlet.*;
import java.util.*;
import org.openswing.swing.message.send.java.*;
import java.util.*;
import java.math.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Help class used to select/insert/update/delete a sale discount.</p>
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
public class DiscountBean implements Discount {


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
   * @return list of DiscountVO objects
   */
  public Response getDiscountsList(String companyCodeSYS01,ArrayList discountCodes,String langId,GridParams gridParams,String username,Class discountVOClass) throws Exception {
    Connection conn = null;
    try {
	    if (this.conn==null) conn = getConn(); else conn = this.conn;
	    
	    String codes = "";
	    for(int i=0;i<discountCodes.size();i++)
	      codes += "'"+discountCodes.get(i)+"',";
	    if (codes.length()>0)
	      codes = codes.substring(0,codes.length()-1);
	    else
	      codes = "''";
	
	    DiscountVO vo = null;
	    String sql =
	      "select SAL03_DISCOUNTS.COMPANY_CODE_SYS01,SAL03_DISCOUNTS.DISCOUNT_CODE,SAL03_DISCOUNTS.DISCOUNT_TYPE,"+
	      "SAL03_DISCOUNTS.PROGRESSIVE_SYS10,SAL03_DISCOUNTS.CURRENCY_CODE_REG03,SAL03_DISCOUNTS.MIN_VALUE,"+
	      "SAL03_DISCOUNTS.MAX_VALUE,SAL03_DISCOUNTS.MIN_PERC,SAL03_DISCOUNTS.MAX_PERC,SAL03_DISCOUNTS.START_DATE,"+
	      "SAL03_DISCOUNTS.END_DATE,SYS10_TRANSLATIONS.DESCRIPTION,SAL03_DISCOUNTS.MIN_QTY,SAL03_DISCOUNTS.MULTIPLE_QTY "+
	      "from SAL03_DISCOUNTS,SYS10_TRANSLATIONS where "+
	      "SAL03_DISCOUNTS.COMPANY_CODE_SYS01=? and SAL03_DISCOUNTS.DISCOUNT_CODE in ("+codes+") and "+
	      "SAL03_DISCOUNTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
	      "SYS10_TRANSLATIONS.LANGUAGE_CODE=?";
	
	    ArrayList values = new ArrayList();
	    values.add(companyCodeSYS01);
	    values.add(langId);
	
	    HashMap attribute2dbField = new HashMap();
	    attribute2dbField.put("companyCodeSys01SAL03","SAL03_DISCOUNTS.COMPANY_CODE_SYS01");
	    attribute2dbField.put("discountCodeSAL03","SAL03_DISCOUNTS.DISCOUNT_CODE");
	    attribute2dbField.put("discountTypeSAL03","SAL03_DISCOUNTS.DISCOUNT_TYPE");
	    attribute2dbField.put("progressiveSys10SAL03","SAL03_DISCOUNTS.PROGRESSIVE_SYS10");
	    attribute2dbField.put("currencyCodeReg03SAL03","SAL03_DISCOUNTS.CURRENCY_CODE_REG03");
	    attribute2dbField.put("minValueSAL03","SAL03_DISCOUNTS.MIN_VALUE");
	    attribute2dbField.put("maxValueSAL03","SAL03_DISCOUNTS.MAX_VALUE");
	    attribute2dbField.put("minPercSAL03","SAL03_DISCOUNTS.MIN_PERC");
	    attribute2dbField.put("maxPercSAL03","SAL03_DISCOUNTS.MAX_PERC");
	    attribute2dbField.put("startDateSAL03","SAL03_DISCOUNTS.START_DATE");
	    attribute2dbField.put("endDateSAL03","SAL03_DISCOUNTS.END_DATE");
	    attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
	    attribute2dbField.put("minQtySAL03","SAL03_DISCOUNTS.MIN_QTY");
	    attribute2dbField.put("multipleQtySAL03","SAL03_DISCOUNTS.MULTIPLE_QTY");
	
	    return QueryUtil.getQuery(
	        conn,
	        new UserSessionParameters(username),
	        sql,
	        values,
	        attribute2dbField,
	        discountVOClass,
	        "Y",
	        "N",
	        null,
	        gridParams,
	        true
	    );
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
   * Insert record in SAL03 table.
   * Throws an exception if the record already exists.
   */
  public void insertDiscount(DiscountVO vo) throws Exception {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      vo.setProgressiveSys10SAL03( TranslationUtils.insertTranslations(vo.getDescriptionSYS10(),vo.getCompanyCodeSys01SAL03(),conn) );
      if (vo.getMinQtySAL03()==null)
        vo.setMinQtySAL03(new BigDecimal(1));
      if (vo.getMultipleQtySAL03()==null)
        vo.setMultipleQtySAL03(Boolean.FALSE);

      pstmt = conn.prepareStatement(
        "insert into SAL03_DISCOUNTS(COMPANY_CODE_SYS01,DISCOUNT_CODE,DISCOUNT_TYPE,PROGRESSIVE_SYS10,CURRENCY_CODE_REG03,"+
        "MIN_VALUE,MAX_VALUE,MIN_PERC,MAX_PERC,START_DATE,END_DATE,MIN_QTY,MULTIPLE_QTY) values(?,?,?,?,?,?,?,?,?,?,?,?,?)");
      pstmt.setString(1,vo.getCompanyCodeSys01SAL03());
      pstmt.setString(2,vo.getDiscountCodeSAL03());
      pstmt.setString(3,vo.getDiscountTypeSAL03());
      pstmt.setBigDecimal(4,vo.getProgressiveSys10SAL03());
      pstmt.setString(5,vo.getCurrencyCodeReg03SAL03());
      pstmt.setBigDecimal(6,vo.getMinValueSAL03());
      pstmt.setBigDecimal(7,vo.getMaxValueSAL03());
      pstmt.setBigDecimal(8,vo.getMinPercSAL03());
      pstmt.setBigDecimal(9,vo.getMaxPercSAL03());
      pstmt.setDate(10,vo.getStartDateSAL03());
      pstmt.setDate(11,vo.getEndDateSAL03());
      pstmt.setBigDecimal(12,vo.getMinQtySAL03());
      pstmt.setString(13,vo.getMultipleQtySAL03().booleanValue()?"Y":"N");

      pstmt.execute();
    }
    catch (Exception ex) {
        try {
      	  if (this.conn==null && conn!=null)
      		  // rollback only local connection
      		  conn.rollback();
        }
        catch (Exception ex3) {
        }
        throw ex;
    }
    finally {
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
   * Update record in SAL03 table.
   */
  public Response updateDiscount(DiscountVO oldVO,DiscountVO newVO,String langId,String username) throws Exception {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
	  if (this.conn==null) conn = getConn(); else conn = this.conn;
      TranslationUtils.updateTranslation(oldVO.getDescriptionSYS10(),newVO.getDescriptionSYS10(),newVO.getProgressiveSys10SAL03(),langId,conn);

      HashMap attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SAL03","COMPANY_CODE_SYS01");
      attribute2dbField.put("discountCodeSAL03","DISCOUNT_CODE");
      attribute2dbField.put("discountTypeSAL03","DISCOUNT_TYPE");
      attribute2dbField.put("progressiveSys10SAL03","PROGRESSIVE_SYS10");
      attribute2dbField.put("currencyCodeReg03SAL03","CURRENCY_CODE_REG03");
      attribute2dbField.put("minValueSAL03","MIN_VALUE");
      attribute2dbField.put("maxValueSAL03","MAX_VALUE");
      attribute2dbField.put("minPercSAL03","MIN_PERC");
      attribute2dbField.put("maxPercSAL03","MAX_PERC");
      attribute2dbField.put("startDateSAL03","START_DATE");
      attribute2dbField.put("endDateSAL03","END_DATE");
      attribute2dbField.put("minQtySAL03","MIN_QTY");
      attribute2dbField.put("multipleQtySAL03","MULTIPLE_QTY");

      HashSet pkAttrs = new HashSet();
      pkAttrs.add("companyCodeSys01SAL03");
      pkAttrs.add("discountCodeSAL03");

      return QueryUtil.updateTable(
          conn,
          new UserSessionParameters(username),
          pkAttrs,
          oldVO,
          newVO,
          "SAL03_DISCOUNTS",
          attribute2dbField,
          "Y",
          "N",
          null,
          true
      );
    }
    catch (Exception ex) {
        try {
      	  if (this.conn==null && conn!=null)
      		  // rollback only local connection
      		  conn.rollback();
        }
        catch (Exception ex3) {
        }
        throw ex;
    }    
    finally {
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
   * Phisically delete record in SAL03 table.
   */
  public void deleteDiscount(DiscountVO vo) throws Exception {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      TranslationUtils.deleteTranslations(vo.getProgressiveSys10SAL03(),conn);

      pstmt = conn.prepareStatement(
        "delete from SAL03_DISCOUNTS where COMPANY_CODE_SYS01=? and DISCOUNT_CODE=?");
      pstmt.setString(1,vo.getCompanyCodeSys01SAL03());
      pstmt.setString(2,vo.getDiscountCodeSAL03());
      pstmt.execute();
    }
    catch (Exception ex) {
        try {
      	  if (this.conn==null && conn!=null)
      		  // rollback only local connection
      		  conn.rollback();
        }
        catch (Exception ex3) {
        }
        throw ex;
    }    
    finally {
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


}

