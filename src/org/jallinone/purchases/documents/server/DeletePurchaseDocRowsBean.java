package org.jallinone.purchases.documents.server;

import org.openswing.swing.server.*;
import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.purchases.documents.java.*;
import org.jallinone.system.server.*;
import java.math.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to (phisically) delete existing purchases order rows.</p>
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
public class DeletePurchaseDocRowsBean  implements DeletePurchaseDocRows {


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



  private PurchaseDocTotalsBean totalBean;

  public void setTotalBean(PurchaseDocTotalsBean totalBean) {
    this.totalBean = totalBean;
  }

  private LoadPurchaseDocBean docBean;

  public void setDocBean(LoadPurchaseDocBean docBean) {
    this.docBean = docBean;
  }



  public DeletePurchaseDocRowsBean() {
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public PurchaseDocRowPK getPurchaseDocRow() {
	  throw new UnsupportedOperationException();
  }
  

  /**
   * Business logic to execute.
   */
  public VOResponse deletePurchaseDocRows(ArrayList list,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      totalBean.setConn(conn); // use same transaction...
      docBean.setConn(conn); // use same transaction...

      PurchaseDocRowPK rowPK = null;

      pstmt = conn.prepareStatement(
          "delete from DOC07_PURCHASE_ITEMS where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? and ITEM_CODE_ITM01=? and "+
          "VARIANT_TYPE_ITM06=? and VARIANT_CODE_ITM11=? and "+
          "VARIANT_TYPE_ITM07=? and VARIANT_CODE_ITM12=? and "+
          "VARIANT_TYPE_ITM08=? and VARIANT_CODE_ITM13=? and "+
          "VARIANT_TYPE_ITM09=? and VARIANT_CODE_ITM14=? and "+
          "VARIANT_TYPE_ITM10=? and VARIANT_CODE_ITM15=? "
      );

      for(int i=0;i<list.size();i++) {
        rowPK = (PurchaseDocRowPK)list.get(i);

        // phisically delete the record in DOC07...
        pstmt.setString(1,rowPK.getCompanyCodeSys01DOC07());
        pstmt.setString(2,rowPK.getDocTypeDOC07());
        pstmt.setBigDecimal(3,rowPK.getDocYearDOC07());
        pstmt.setBigDecimal(4,rowPK.getDocNumberDOC07());
        pstmt.setString(5,rowPK.getItemCodeItm01DOC07());

        pstmt.setString(6,rowPK.getVariantTypeItm06DOC07());
        pstmt.setString(7,rowPK.getVariantCodeItm11DOC07());
        pstmt.setString(8,rowPK.getVariantTypeItm07DOC07());
        pstmt.setString(9,rowPK.getVariantCodeItm12DOC07());
        pstmt.setString(10,rowPK.getVariantTypeItm08DOC07());
        pstmt.setString(11,rowPK.getVariantCodeItm13DOC07());
        pstmt.setString(12,rowPK.getVariantTypeItm09DOC07());
        pstmt.setString(13,rowPK.getVariantCodeItm14DOC07());
        pstmt.setString(14,rowPK.getVariantTypeItm10DOC07());
        pstmt.setString(15,rowPK.getVariantCodeItm15DOC07());

        pstmt.execute();
      }


      // recalculate totals...
      PurchaseDocPK pk = new PurchaseDocPK(
          rowPK.getCompanyCodeSys01DOC07(),
          rowPK.getDocTypeDOC07(),
          rowPK.getDocYearDOC07(),
          rowPK.getDocNumberDOC07()
      );
      DetailPurchaseDocVO docVO = docBean.loadPurchaseDoc(pk,serverLanguageId,username,new ArrayList());
      Response totalResponse = totalBean.calcDocTotals(docVO,serverLanguageId,username);
      if (totalResponse.isError())
        return new VOResponse(totalResponse.getErrorMessage());

      pstmt = conn.prepareStatement("update DOC06_PURCHASE set TAXABLE_INCOME=?,TOTAL_VAT=?,TOTAL=? where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=?");
      pstmt.setBigDecimal(1,docVO.getTaxableIncomeDOC06());
      pstmt.setBigDecimal(2,docVO.getTotalVatDOC06());
      pstmt.setBigDecimal(3,docVO.getTotalDOC06());
      pstmt.setString(4,rowPK.getCompanyCodeSys01DOC07());
      pstmt.setString(5,rowPK.getDocTypeDOC07());
      pstmt.setBigDecimal(6,rowPK.getDocYearDOC07());
      pstmt.setBigDecimal(7,rowPK.getDocNumberDOC07());
      pstmt.execute();

      return  new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing purchase order rows",ex);
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
      try {
        totalBean.setConn(null);
        docBean.setConn(null);
      } catch (Exception ex) {}
    }

  }



}

