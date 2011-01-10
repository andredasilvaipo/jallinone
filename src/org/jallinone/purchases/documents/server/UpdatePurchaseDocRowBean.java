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
import org.jallinone.system.progressives.server.*;
import org.jallinone.system.translations.server.*;
import org.jallinone.commons.server.*;
import org.jallinone.system.java.*;
import org.jallinone.commons.java.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.purchases.documents.java.*;
import org.jallinone.purchases.documents.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to update an existing purchase order row.</p>
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
public class UpdatePurchaseDocRowBean  implements UpdatePurchaseDocRow {


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



  public UpdatePurchaseDocRowBean() {
  }



  /**
   * Business logic to execute.
   */
  public VOResponse updatePurchaseDocRow(DetailPurchaseDocRowVO oldVO,DetailPurchaseDocRowVO newVO,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      totalBean.setConn(conn); // use same transaction...
      docBean.setConn(conn); // use same transaction...

      newVO.setOrderQtyDOC07(newVO.getQtyDOC07());

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC07","COMPANY_CODE_SYS01");
      attribute2dbField.put("docTypeDOC07","DOC_TYPE");
      attribute2dbField.put("docYearDOC07","DOC_YEAR");
      attribute2dbField.put("docNumberDOC07","DOC_NUMBER");
      attribute2dbField.put("rowNumberDOC07","ROW_NUMBER");
      attribute2dbField.put("itemCodeItm01DOC07","ITEM_CODE_ITM01");
      attribute2dbField.put("supplierItemCodePur02DOC07","SUPPLIER_ITEM_CODE_PUR02");
      attribute2dbField.put("vatCodeItm01DOC07","VAT_CODE_ITM01");
      attribute2dbField.put("valuePur04DOC07","VALUE_PUR04");
      attribute2dbField.put("valueDOC07","VALUE");
      attribute2dbField.put("qtyDOC07","QTY");
      attribute2dbField.put("discountValueDOC07","DISCOUNT_VALUE");
      attribute2dbField.put("discountPercDOC07","DISCOUNT_PERC");
      attribute2dbField.put("vatValueDOC07","VAT_VALUE");

      attribute2dbField.put("vatDescriptionDOC07","VAT_DESCRIPTION");
      attribute2dbField.put("startDatePur04DOC07","START_DATE_PUR04");
      attribute2dbField.put("endDatePur04DOC07","END_DATE_PUR04");
      attribute2dbField.put("umCodePur02DOC07","UM_CODE_PUR02");
      attribute2dbField.put("decimalsReg02DOC07","DECIMALS_REG02");
      attribute2dbField.put("minPurchaseQtyPur02DOC07","MIN_PURCHASE_QTY_PUR02");
      attribute2dbField.put("multipleQtyPur02DOC07","MULTIPLE_QTY_PUR02");
      attribute2dbField.put("valueReg01DOC07","VALUE_REG01");
      attribute2dbField.put("deductibleReg01DOC07","DEDUCTIBLE_REG01");
      attribute2dbField.put("taxableIncomeDOC07","TAXABLE_INCOME");
      attribute2dbField.put("progressiveHie02DOC07","PROGRESSIVE_HIE02");
      attribute2dbField.put("deliveryDateDOC07","DELIVERY_DATE");
      attribute2dbField.put("inQtyDOC07","IN_QTY");
      attribute2dbField.put("orderQtyDOC07","ORDER_QTY");

      attribute2dbField.put("variantTypeItm06DOC07","VARIANT_TYPE_ITM06");
      attribute2dbField.put("variantCodeItm11DOC07","VARIANT_CODE_ITM11");
      attribute2dbField.put("variantTypeItm07DOC07","VARIANT_TYPE_ITM07");
      attribute2dbField.put("variantCodeItm12DOC07","VARIANT_CODE_ITM12");
      attribute2dbField.put("variantTypeItm08DOC07","VARIANT_TYPE_ITM08");
      attribute2dbField.put("variantCodeItm13DOC07","VARIANT_CODE_ITM13");
      attribute2dbField.put("variantTypeItm09DOC07","VARIANT_TYPE_ITM09");
      attribute2dbField.put("variantCodeItm14DOC07","VARIANT_CODE_ITM14");
      attribute2dbField.put("variantTypeItm10DOC07","VARIANT_TYPE_ITM10");
      attribute2dbField.put("variantCodeItm15DOC07","VARIANT_CODE_ITM15");

      HashSet pkAttributes = new HashSet();
      pkAttributes.add("companyCodeSys01DOC07");
      pkAttributes.add("docTypeDOC07");
      pkAttributes.add("docYearDOC07");
      pkAttributes.add("docNumberDOC07");
      pkAttributes.add("itemCodeItm01DOC07");
      pkAttributes.add("variantTypeItm06DOC07");
      pkAttributes.add("variantCodeItm11DOC07");
      pkAttributes.add("variantTypeItm07DOC07");
      pkAttributes.add("variantCodeItm12DOC07");
      pkAttributes.add("variantTypeItm08DOC07");
      pkAttributes.add("variantCodeItm13DOC07");
      pkAttributes.add("variantTypeItm09DOC07");
      pkAttributes.add("variantCodeItm14DOC07");
      pkAttributes.add("variantTypeItm10DOC07");
      pkAttributes.add("variantCodeItm15DOC07");

      // update DOC07 table...
      Response res = QueryUtil.updateTable(
          conn,
          new UserSessionParameters(username),
          pkAttributes,
          oldVO,
          newVO,
          "DOC07_PURCHASE_ITEMS",
          attribute2dbField,
          "Y",
          "N",
          null,
          true
      );

      if (res.isError()) {
        throw new Exception(res.getErrorMessage());
      }


      // recalculate totals...
      PurchaseDocPK pk = new PurchaseDocPK(
          newVO.getCompanyCodeSys01DOC07(),
          newVO.getDocTypeDOC07(),
          newVO.getDocYearDOC07(),
          newVO.getDocNumberDOC07()
      );
      DetailPurchaseDocVO vo = docBean.loadPurchaseDoc(pk,serverLanguageId,username,new ArrayList());
      Response totalResponse = totalBean.calcDocTotals(vo,serverLanguageId,username);
      if (totalResponse.isError())
    	  throw new Exception(totalResponse.getErrorMessage());

      pstmt = conn.prepareStatement("update DOC06_PURCHASE set TAXABLE_INCOME=?,TOTAL_VAT=?,TOTAL=? where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=?");
      pstmt.setBigDecimal(1,vo.getTaxableIncomeDOC06());
      pstmt.setBigDecimal(2,vo.getTotalVatDOC06());
      pstmt.setBigDecimal(3,vo.getTotalDOC06());
      pstmt.setString(4,newVO.getCompanyCodeSys01DOC07());
      pstmt.setString(5,newVO.getDocTypeDOC07());
      pstmt.setBigDecimal(6,newVO.getDocYearDOC07());
      pstmt.setBigDecimal(7,newVO.getDocNumberDOC07());
      pstmt.execute();

      Response answer =  res;
      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating an existing purchase order row",ex);
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

