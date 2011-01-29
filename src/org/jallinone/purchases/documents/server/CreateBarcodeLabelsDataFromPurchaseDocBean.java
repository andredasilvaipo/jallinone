package org.jallinone.purchases.documents.server;

import java.math.*;
import java.sql.*;
import java.util.*;

import org.jallinone.commons.java.*;
import org.jallinone.events.server.*;
import org.jallinone.purchases.documents.java.*;
import org.jallinone.system.progressives.server.*;
import org.jallinone.system.server.*;
import org.openswing.swing.logger.server.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.*;
import org.openswing.swing.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to prepare barcode labels related to the specified purchase order
 * and fill in a temporary table (TMP02_BARCODES) with that data.</p>
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
public class CreateBarcodeLabelsDataFromPurchaseDocBean  implements CreateBarcodeLabelsDataFromPurchaseDoc {


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



  private LoadPurchaseDocRowsBean action;

  public void setAction(LoadPurchaseDocRowsBean action) {
    this.action = action;
  }



  public CreateBarcodeLabelsDataFromPurchaseDocBean() {
  }




  /**
   * Business logic to execute.
   */
  public VOResponse createBarcodeLabelsDataFromPurchaseDoc(
		HashMap variant1Descriptions,
		HashMap variant2Descriptions,
		HashMap variant3Descriptions,
		HashMap variant4Descriptions,
		HashMap variant5Descriptions,
		HashMap map, String serverLanguageId, String username) throws Throwable {
    PreparedStatement pstmt = null;
    PreparedStatement pstmt2 = null;
    ResultSet rset2 = null;

    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      action.setConn(conn); // use same transaction...


      PurchaseDocPK pk = (PurchaseDocPK)map.get(ApplicationConsts.PURCHASE_DOC_PK);
      GridParams pars = new GridParams();
      pars.getOtherGridParams().put(ApplicationConsts.PURCHASE_DOC_PK,pk);

      Response res = action.loadPurchaseDocRows(variant1Descriptions,variant2Descriptions,variant3Descriptions,variant4Descriptions,variant5Descriptions,pars,serverLanguageId,username);
      if (res.isError())
        throw new Exception(res.getErrorMessage());
      List rows = ((VOListResponse)res).getRows();

      BigDecimal reportId = CompanyProgressiveUtils.getInternalProgressive(pk.getCompanyCodeSys01DOC06(),"TMP02_BARCODES","REPORT_ID",conn);
      pstmt = conn.prepareStatement(
        "insert into TMP02_BARCODES(REPORT_ID,PROGRESSIVE,BAR_CODE,BARCODE_TYPE,DESCRIPTION) values(?,?,?,?,?)"
      );
      pstmt2 = conn.prepareStatement(
        "select BAR_CODE from ITM22_VARIANT_BARCODES where "+
        "COMPANY_CODE_SYS01=? AND "+
        "ITEM_CODE_ITM01=? AND "+
        "VARIANT_TYPE_ITM06=? AND "+
        "VARIANT_CODE_ITM11=? AND "+
        "VARIANT_TYPE_ITM07=? AND "+
        "VARIANT_CODE_ITM12=? AND "+
        "VARIANT_TYPE_ITM08=? AND "+
        "VARIANT_CODE_ITM13=? AND "+
        "VARIANT_TYPE_ITM09=? AND "+
        "VARIANT_CODE_ITM14=? AND "+
        "VARIANT_TYPE_ITM10=? AND "+
        "VARIANT_CODE_ITM15=? "
      );

      GridPurchaseDocRowVO rowVO = null;
      String barcode = null;
      String barcodeType = null;
      long progressive = 0;
      for(int i=0;i<rows.size();i++) {
        rowVO = (GridPurchaseDocRowVO)rows.get(i);
        barcode = rowVO.getBarCodeITM01();
        barcodeType = rowVO.getBarcodeTypeITM01();

        if (barcode==null || barcode.equals("")) {
          // barcode not defined at item level...
          pstmt2.setString(1,rowVO.getCompanyCodeSys01DOC07());
          pstmt2.setString(2,rowVO.getItemCodeItm01DOC07());
          pstmt2.setString(3,rowVO.getVariantTypeItm06DOC07());
          pstmt2.setString(4,rowVO.getVariantCodeItm11DOC07());
          pstmt2.setString(5,rowVO.getVariantTypeItm07DOC07());
          pstmt2.setString(6,rowVO.getVariantCodeItm12DOC07());
          pstmt2.setString(7,rowVO.getVariantTypeItm08DOC07());
          pstmt2.setString(8,rowVO.getVariantCodeItm13DOC07());
          pstmt2.setString(9,rowVO.getVariantTypeItm09DOC07());
          pstmt2.setString(10,rowVO.getVariantCodeItm14DOC07());
          pstmt2.setString(11,rowVO.getVariantTypeItm10DOC07());
          pstmt2.setString(12,rowVO.getVariantCodeItm15DOC07());
          rset2 = pstmt2.executeQuery();
          if (rset2.next()) {
            barcode = rset2.getString(1);
          }
          rset2.close();
        }

        if (barcode!=null && !barcode.equals("")) {
          for(int j=0;j<rowVO.getQtyDOC07().intValue();j++) {
            pstmt.setBigDecimal(1,reportId);
            pstmt.setLong(2,progressive);
            pstmt.setString(3,pad(barcode,barcodeType));
            pstmt.setString(4,barcodeType);
            pstmt.setString(5,rowVO.getDescriptionSYS10());
            pstmt.execute();
            progressive++;
          }
        }
      }


      return new VOResponse(reportId);
    } catch (Exception ex1) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching barcodes and fill in TMP02 table",ex1);
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
        rset2.close();
      }
      catch (Exception ex) {
      }
      try {
        pstmt2.close();
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
      try {
          action.setConn(null);
        } catch (Exception ex) {}
    }
  }


  private String pad(String code,String barcodeType) {
    if (barcodeType.equals(ApplicationConsts.BAR_CODE_EAN13)) {
      int len = code.length();
      for(int i=len;i<13;i++)
        code = "0"+code;
      return code;

    }
    else
      return code;
  }


}

