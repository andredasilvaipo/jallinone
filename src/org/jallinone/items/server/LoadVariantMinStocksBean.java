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
import org.jallinone.variants.java.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to fetch min stocks defined for item's variants from ITM23 table.</p>
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
public class LoadVariantMinStocksBean  implements LoadVariantMinStocks {


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




  public LoadVariantMinStocksBean() {
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public CustomValueObject getCustomValueObject() {
	  throw new UnsupportedOperationException();  
  }

  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public VariantNameVO getVariantName() {
	  throw new UnsupportedOperationException();  
  }

  /**
   * Business logic to execute.
   */
  public VOListResponse loadVariantMinStocks(VariantsMatrixVO matrixVO,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;




      // indexing cols...
      HashMap cols = new HashMap();
      VariantsMatrixColumnVO colVO = null;
      for(int i=0;i<matrixVO.getColumnDescriptors().length;i++) {
        colVO = (VariantsMatrixColumnVO)matrixVO.getColumnDescriptors()[i];
        cols.put(
          new ColPK(
            colVO.getVariantTypeITM06(),
            colVO.getVariantTypeITM07(),
            colVO.getVariantTypeITM08(),
            colVO.getVariantTypeITM09(),
            colVO.getVariantTypeITM10(),
            colVO.getVariantCodeITM11(),
            colVO.getVariantCodeITM12(),
            colVO.getVariantCodeITM13(),
            colVO.getVariantCodeITM14(),
            colVO.getVariantCodeITM15()
          ),
          new Integer(i+1)
        );
      }

      String sql =
          "select "+
          "VARIANT_TYPE_ITM06,VARIANT_TYPE_ITM07,VARIANT_TYPE_ITM08,VARIANT_TYPE_ITM09,VARIANT_TYPE_ITM10,"+
          "VARIANT_CODE_ITM11,VARIANT_CODE_ITM12,VARIANT_CODE_ITM13,VARIANT_CODE_ITM14,VARIANT_CODE_ITM15,"+
          "MIN_STOCK "+
          "from ITM23_VARIANT_MIN_STOCKS where "+
          "COMPANY_CODE_SYS01=? AND "+
          "ITEM_CODE_ITM01=? AND ";

      VariantNameVO varVO = (VariantNameVO)matrixVO.getManagedVariants()[0];
      if (varVO.getTableName().equals("ITM11_VARIANTS_1")) {
        sql +=
            "VARIANT_TYPE_ITM06=? AND "+
            "VARIANT_CODE_ITM11=? ";
      }
      else if (varVO.getTableName().equals("ITM12_VARIANTS_2")) {
        sql +=
            "VARIANT_TYPE_ITM07=? AND "+
            "VARIANT_CODE_ITM12=? ";
      }
      else if (varVO.getTableName().equals("ITM13_VARIANTS_3")) {
        sql +=
            "VARIANT_TYPE_ITM08=? AND "+
            "VARIANT_CODE_ITM13=? ";
      }
      else if (varVO.getTableName().equals("ITM14_VARIANTS_4")) {
        sql +=
            "VARIANT_TYPE_ITM09=? AND "+
            "VARIANT_CODE_ITM14=? ";
      }
      else if (varVO.getTableName().equals("ITM15_VARIANTS_5")) {
        sql +=
            "VARIANT_TYPE_ITM10=? AND "+
            "VARIANT_CODE_ITM15=? ";
      }

      pstmt = conn.prepareStatement(sql);

      ArrayList rows = new ArrayList();
      CustomValueObject vo = null;
      VariantsMatrixRowVO rowVO = null;
      ColPK colPK = null;
      Integer pos = null;
      for(int i=0;i<matrixVO.getRowDescriptors().length;i++) {
        rowVO = (VariantsMatrixRowVO)matrixVO.getRowDescriptors()[i];

        pstmt.setString(1,matrixVO.getItemPK().getCompanyCodeSys01ITM01());
        pstmt.setString(2,matrixVO.getItemPK().getItemCodeITM01());
        pstmt.setString(3,VariantsMatrixUtils.getVariantType(matrixVO,rowVO));
        pstmt.setString(4,VariantsMatrixUtils.getVariantCode(matrixVO,rowVO));

        vo = new CustomValueObject();
        vo.setAttributeNameS0(rowVO.getRowDescription());
        rows.add(vo);

        rset = pstmt.executeQuery();
        while(rset.next()) {
          colPK = new ColPK(
            varVO.getTableName().equals("ITM11_VARIANTS_1")?ApplicationConsts.JOLLY:rset.getString(1),
            varVO.getTableName().equals("ITM12_VARIANTS_2")?ApplicationConsts.JOLLY:rset.getString(2),
            varVO.getTableName().equals("ITM13_VARIANTS_3")?ApplicationConsts.JOLLY:rset.getString(3),
            varVO.getTableName().equals("ITM14_VARIANTS_4")?ApplicationConsts.JOLLY:rset.getString(4),
            varVO.getTableName().equals("ITM15_VARIANTS_5")?ApplicationConsts.JOLLY:rset.getString(5),
            varVO.getTableName().equals("ITM11_VARIANTS_1")?ApplicationConsts.JOLLY:rset.getString(6),
            varVO.getTableName().equals("ITM12_VARIANTS_2")?ApplicationConsts.JOLLY:rset.getString(7),
            varVO.getTableName().equals("ITM13_VARIANTS_3")?ApplicationConsts.JOLLY:rset.getString(8),
            varVO.getTableName().equals("ITM14_VARIANTS_4")?ApplicationConsts.JOLLY:rset.getString(9),
            varVO.getTableName().equals("ITM15_VARIANTS_5")?ApplicationConsts.JOLLY:rset.getString(10)
          );

          if (matrixVO.getColumnDescriptors().length==0) {
            try {
              CustomValueObject.class.getMethod("setAttributeNameN0",new Class[] {BigDecimal.class}).invoke(vo, new Object[] {rset.getBigDecimal(11)});
            }
            catch (Throwable ex) {
              ex.printStackTrace();
            }
          }
          else {
            pos = (Integer)cols.get(colPK);
            if (pos!=null) {
              try {
                CustomValueObject.class.getMethod("setAttributeNameN" +pos,new Class[] {BigDecimal.class}).invoke(vo, new Object[] {rset.getBigDecimal(11)});
              }
              catch (Throwable ex) {
                ex.printStackTrace();
              }
            }
            else
              Logger.error(username,this.getClass().getName(),"executeCommand","Variants not found: "+colPK,null);
          }

        }
        rset.close();
      }


      return new VOListResponse(rows,false,rows.size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching min stocks",ex);
      throw new Exception(ex.getMessage());
    }
    finally {
      try {
        rset.close();
      }
      catch (Exception ex1) {
      }
      try {
        pstmt.close();
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


  private boolean containsVariant(VariantsMatrixVO vo,String tableName) {
    for(int i=0;i<vo.getManagedVariants().length;i++)
      if (((VariantNameVO)vo.getManagedVariants()[i]).getTableName().equals(tableName))
        return true;
    return false;
  }


  class ColPK {

    private String variantTypeItm06;
    private String variantTypeItm07;
    private String variantTypeItm08;
    private String variantTypeItm09;
    private String variantTypeItm10;

    private String variantCodeItm11;
    private String variantCodeItm12;
    private String variantCodeItm13;
    private String variantCodeItm14;
    private String variantCodeItm15;


    public ColPK(String variantTypeItm06,String variantTypeItm07,String variantTypeItm08,String variantTypeItm09,String variantTypeItm10,
                 String variantCodeItm11,String variantCodeItm12,String variantCodeItm13,String variantCodeItm14,String variantCodeItm15) {
      this.variantTypeItm06 = variantTypeItm06==null?ApplicationConsts.JOLLY:variantTypeItm06;
      this.variantTypeItm07 = variantTypeItm07==null?ApplicationConsts.JOLLY:variantTypeItm07;
      this.variantTypeItm08 = variantTypeItm08==null?ApplicationConsts.JOLLY:variantTypeItm08;
      this.variantTypeItm09 = variantTypeItm09==null?ApplicationConsts.JOLLY:variantTypeItm09;
      this.variantTypeItm10 = variantTypeItm10==null?ApplicationConsts.JOLLY:variantTypeItm10;
      this.variantCodeItm11 = variantCodeItm11==null?ApplicationConsts.JOLLY:variantCodeItm11;
      this.variantCodeItm12 = variantCodeItm12==null?ApplicationConsts.JOLLY:variantCodeItm12;
      this.variantCodeItm13 = variantCodeItm13==null?ApplicationConsts.JOLLY:variantCodeItm13;
      this.variantCodeItm14 = variantCodeItm14==null?ApplicationConsts.JOLLY:variantCodeItm14;
      this.variantCodeItm15 = variantCodeItm15==null?ApplicationConsts.JOLLY:variantCodeItm15;
    }


    public String getVariantCodeItm11() {
      return variantCodeItm11;
    }
    public String getVariantCodeItm12() {
      return variantCodeItm12;
    }
    public String getVariantCodeItm13() {
      return variantCodeItm13;
    }
    public String getVariantCodeItm14() {
      return variantCodeItm14;
    }
    public String getVariantCodeItm15() {
      return variantCodeItm15;
    }
    public String getVariantTypeItm06() {
      return variantTypeItm06;
    }
    public String getVariantTypeItm07() {
      return variantTypeItm07;
    }
    public String getVariantTypeItm08() {
      return variantTypeItm08;
    }
    public String getVariantTypeItm09() {
      return variantTypeItm09;
    }
    public String getVariantTypeItm10() {
      return variantTypeItm10;
    }

    public boolean equals(Object o) {
      if (o==null ||
        !(o instanceof ColPK))
        return false;
      ColPK vo = (ColPK)o;
      return
          (this.variantTypeItm06).equals(vo.variantTypeItm06) &&
          (this.variantTypeItm07).equals(vo.variantTypeItm07) &&
          (this.variantTypeItm08).equals(vo.variantTypeItm08) &&
          (this.variantTypeItm09).equals(vo.variantTypeItm09) &&
          (this.variantTypeItm10).equals(vo.variantTypeItm10) &&
          (this.variantCodeItm12).equals(vo.variantCodeItm12) &&
          (this.variantCodeItm13).equals(vo.variantCodeItm13) &&
          (this.variantCodeItm14).equals(vo.variantCodeItm14) &&
          (this.variantCodeItm15).equals(vo.variantCodeItm15);
    }


    public int hashCode() {
      return
         (variantTypeItm06+
          variantTypeItm07+
          variantTypeItm08+
          variantTypeItm09+
          variantTypeItm10+
          variantCodeItm11+
          variantCodeItm12+
          variantCodeItm13+
          variantCodeItm14+
          variantCodeItm15).hashCode();
    
    }

  } // end inner class


}

