package org.jallinone.items.server;

import org.openswing.swing.server.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.items.java.*;
import org.jallinone.system.server.*;
import org.jallinone.commons.server.*;
import java.math.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.jallinone.variants.java.*;
import org.openswing.swing.customvo.java.*;
import org.jallinone.commons.java.*;
import org.openswing.swing.internationalization.server.*;
import org.openswing.swing.internationalization.java.*;
import org.jallinone.items.java.*;
import org.jallinone.purchases.items.java.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to update min stocks.</p>
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
public class UpdateMinStocksBean  implements UpdateMinStocks {


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




  public UpdateMinStocksBean() {
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public ABCVO getABC() {
	  throw new UnsupportedOperationException();
  }
  

  /**
   * Business logic to execute.
   */
  public VOResponse updateMinStocks(ArrayList vos,String serverLanguageId,String username) throws Throwable {
    PreparedStatement inspstmt = null;
    PreparedStatement updpstmt = null;
    PreparedStatement delpstmt = null;
    
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;


      String updSql =
          "update ITM23_VARIANT_MIN_STOCKS "+
          "set MIN_STOCK=? where COMPANY_CODE_SYS01=? and ITEM_CODE_ITM01=? and "+
          "VARIANT_TYPE_ITM06=? and VARIANT_TYPE_ITM07=? and VARIANT_TYPE_ITM08=? and VARIANT_TYPE_ITM09=? and "+
          "VARIANT_TYPE_ITM10=? and VARIANT_CODE_ITM11=? and VARIANT_CODE_ITM12=? and VARIANT_CODE_ITM13=? and "+
          "VARIANT_CODE_ITM14=? and VARIANT_CODE_ITM15=? ";
      updpstmt = conn.prepareStatement(updSql);

      String insSql =
          "insert into ITM23_VARIANT_MIN_STOCKS("+
          "MIN_STOCK,COMPANY_CODE_SYS01,ITEM_CODE_ITM01,"+
          "VARIANT_TYPE_ITM06,VARIANT_TYPE_ITM07,VARIANT_TYPE_ITM08,VARIANT_TYPE_ITM09,VARIANT_TYPE_ITM10,"+
          "VARIANT_CODE_ITM11,VARIANT_CODE_ITM12,VARIANT_CODE_ITM13,VARIANT_CODE_ITM14,VARIANT_CODE_ITM15"+
          ") values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
      inspstmt = conn.prepareStatement(insSql);

      String delSql =
          "delete from ITM23_VARIANT_MIN_STOCKS "+
          "where COMPANY_CODE_SYS01=? and ITEM_CODE_ITM01=? and "+
          "VARIANT_TYPE_ITM06=? and VARIANT_TYPE_ITM07=? and VARIANT_TYPE_ITM08=? and VARIANT_TYPE_ITM09=? and "+
          "VARIANT_TYPE_ITM10=? and VARIANT_CODE_ITM11=? and VARIANT_CODE_ITM12=? and VARIANT_CODE_ITM13=? and "+
          "VARIANT_CODE_ITM14=? and VARIANT_CODE_ITM15=? ";
      delpstmt = conn.prepareStatement(delSql);

      ABCVO vo = null;
      for(int i=0;i<vos.size();i++) {
        vo = (ABCVO)vos.get(i);
        if (vo.getMinStockITM23()==null || vo.getMinStockITM23().equals(new BigDecimal(0))) {
          // a record must be removeed (if exists...)
          delpstmt.setString(1,vo.getCompanyCodeSys01());
          delpstmt.setString(2,vo.getItemCode());
          delpstmt.setString(3,vo.getVariantTypeITM06());
          delpstmt.setString(4,vo.getVariantTypeITM07());
          delpstmt.setString(5,vo.getVariantTypeITM08());
          delpstmt.setString(6,vo.getVariantTypeITM09());
          delpstmt.setString(7,vo.getVariantTypeITM10());
          delpstmt.setString(8, vo.getVariantCodeITM11());
          delpstmt.setString(9, vo.getVariantCodeITM12());
          delpstmt.setString(10,vo.getVariantCodeITM13());
          delpstmt.setString(11,vo.getVariantCodeITM14());
          delpstmt.setString(12,vo.getVariantCodeITM15());
          delpstmt.executeUpdate();
        }
        else {
          // a record must be updated if exists, or inserted if no still exists...
          updpstmt.setBigDecimal(1,vo.getMinStockITM23());
          updpstmt.setString(2,vo.getCompanyCodeSys01());
          updpstmt.setString(3,vo.getItemCode());
          updpstmt.setString(4,vo.getVariantTypeITM06());
          updpstmt.setString(5,vo.getVariantTypeITM07());
          updpstmt.setString(6,vo.getVariantTypeITM08());
          updpstmt.setString(7,vo.getVariantTypeITM09());
          updpstmt.setString(8,vo.getVariantTypeITM10());
          updpstmt.setString(9, vo.getVariantCodeITM11());
          updpstmt.setString(10,vo.getVariantCodeITM12());
          updpstmt.setString(11,vo.getVariantCodeITM13());
          updpstmt.setString(12,vo.getVariantCodeITM14());
          updpstmt.setString(13,vo.getVariantCodeITM15());
          if (updpstmt.executeUpdate()==0) {
            // an insert must be performed...
            inspstmt.setBigDecimal(1,vo.getMinStockITM23());
            inspstmt.setString(2,vo.getCompanyCodeSys01());
            inspstmt.setString(3,vo.getItemCode());
            inspstmt.setString(4,vo.getVariantTypeITM06());
            inspstmt.setString(5,vo.getVariantTypeITM07());
            inspstmt.setString(6,vo.getVariantTypeITM08());
            inspstmt.setString(7,vo.getVariantTypeITM09());
            inspstmt.setString(8,vo.getVariantTypeITM10());
            inspstmt.setString(9, vo.getVariantCodeITM11());
            inspstmt.setString(10,vo.getVariantCodeITM12());
            inspstmt.setString(11,vo.getVariantCodeITM13());
            inspstmt.setString(12,vo.getVariantCodeITM14());
            inspstmt.setString(13,vo.getVariantCodeITM15());
            inspstmt.execute();
          }
        }
      }

      return new VOResponse(Boolean.TRUE);

    }
    catch (Throwable ex) {
    	try {
    		if (this.conn==null && conn!=null)
    			// rollback only local connection
    			conn.rollback();
    	}
    	catch (Exception ex3) {
    	}
    	Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating min stocks",ex);
    	throw new Exception(ex.getMessage());
    }
    finally {
      try {
        inspstmt.close();
      }
      catch (Exception ex1) {
      }
      try {
        updpstmt.close();
      }
      catch (Exception ex1) {
      }
      try {
        delpstmt.close();
      }
      catch (Exception ex1) {
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

