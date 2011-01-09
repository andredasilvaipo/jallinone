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


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to update variants min stocks.</p>
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
public class UpdateVariantMinStocksBean  implements UpdateVariantMinStocks {


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




  public UpdateVariantMinStocksBean() {
  }



  /**
   * Business logic to execute.
   */
  public VOListResponse updateVariantMinStocks(VariantsMatrixVO matrixVO,Object[][] cells,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;

    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;





      String sql =
          "delete from ITM23_VARIANT_MIN_STOCKS where "+
          "COMPANY_CODE_SYS01=? AND "+
          "ITEM_CODE_ITM01=? ";
      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1,matrixVO.getItemPK().getCompanyCodeSys01ITM01());
      pstmt.setString(2,matrixVO.getItemPK().getItemCodeITM01());
      pstmt.execute();
      pstmt.close();

      sql =
          "insert into ITM23_VARIANT_MIN_STOCKS("+
          "COMPANY_CODE_SYS01,ITEM_CODE_ITM01,"+
          "VARIANT_TYPE_ITM06,VARIANT_TYPE_ITM07,VARIANT_TYPE_ITM08,VARIANT_TYPE_ITM09,VARIANT_TYPE_ITM10,"+
          "VARIANT_CODE_ITM11,VARIANT_CODE_ITM12,VARIANT_CODE_ITM13,VARIANT_CODE_ITM14,VARIANT_CODE_ITM15,"+
          "MIN_STOCK) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
      pstmt = conn.prepareStatement(sql);

      VariantsMatrixRowVO rowVO = null;
      VariantsMatrixColumnVO colVO = null;
      Object[] row = null;

      VariantMinStockVO vo = new VariantMinStockVO();
      vo.setCompanyCodeSys01ITM23(matrixVO.getItemPK().getCompanyCodeSys01ITM01());
      vo.setItemCodeItm01ITM23(matrixVO.getItemPK().getItemCodeITM01());
      BigDecimal qty = null;

      for(int i=0;i<cells.length;i++) {
        rowVO = (VariantsMatrixRowVO)matrixVO.getRowDescriptors()[i];
        row = cells[i];

        if (matrixVO.getColumnDescriptors().length==0) {
          // e.g. color but not no size...

          try {
        	qty = (BigDecimal)row[0];
          } catch (Exception e) {
        	qty = null;
          }
          if (qty==null)
        	continue;

          VariantsMatrixUtils.setVariantTypesAndCodes(vo,"ITM23",matrixVO,rowVO,null);

          pstmt.setString(1,matrixVO.getItemPK().getCompanyCodeSys01ITM01());
          pstmt.setString(2,matrixVO.getItemPK().getItemCodeITM01());

          pstmt.setString(3,vo.getVariantTypeItm06ITM23());
          pstmt.setString(4,vo.getVariantTypeItm07ITM23());
          pstmt.setString(5,vo.getVariantTypeItm08ITM23());
          pstmt.setString(6,vo.getVariantTypeItm09ITM23());
          pstmt.setString(7,vo.getVariantTypeItm10ITM23());

          pstmt.setString(8, vo.getVariantCodeItm11ITM23());
          pstmt.setString(9, vo.getVariantCodeItm12ITM23());
          pstmt.setString(10,vo.getVariantCodeItm13ITM23());
          pstmt.setString(11,vo.getVariantCodeItm14ITM23());
          pstmt.setString(12,vo.getVariantCodeItm15ITM23());

          pstmt.setBigDecimal(13,(BigDecimal)row[0]);

          pstmt.execute();

        }
        else {
          // e.g. color and size...
          for(int j=0;j<row.length;j++) {
            colVO = (VariantsMatrixColumnVO)matrixVO.getColumnDescriptors()[j];
            VariantsMatrixUtils.setVariantTypesAndCodes(vo,"ITM23",matrixVO,rowVO,colVO);

            try {
            	qty = (BigDecimal)row[j];
            } catch (Exception e) {
            	qty = null;
            }
            if (qty==null)
              continue;

            pstmt.setString(1,matrixVO.getItemPK().getCompanyCodeSys01ITM01());
            pstmt.setString(2,matrixVO.getItemPK().getItemCodeITM01());

            pstmt.setString(3,vo.getVariantTypeItm06ITM23());
            pstmt.setString(4,vo.getVariantTypeItm07ITM23());
            pstmt.setString(5,vo.getVariantTypeItm08ITM23());
            pstmt.setString(6,vo.getVariantTypeItm09ITM23());
            pstmt.setString(7,vo.getVariantTypeItm10ITM23());

            pstmt.setString(8, vo.getVariantCodeItm11ITM23());
            pstmt.setString(9, vo.getVariantCodeItm12ITM23());
            pstmt.setString(10,vo.getVariantCodeItm13ITM23());
            pstmt.setString(11,vo.getVariantCodeItm14ITM23());
            pstmt.setString(12,vo.getVariantCodeItm15ITM23());

            pstmt.setBigDecimal(13,(BigDecimal)row[j]);

            pstmt.execute();

          } // end inner for
        }
      } // end outer for

      return new VOListResponse(new ArrayList(),false,0);
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

