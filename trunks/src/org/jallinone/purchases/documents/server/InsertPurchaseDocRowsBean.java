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
import org.jallinone.commons.server.*;
import org.jallinone.system.progressives.server.*;
import org.jallinone.system.translations.server.*;
import org.jallinone.system.java.*;
import org.jallinone.commons.java.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.purchases.documents.java.*;
import org.jallinone.purchases.documents.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.jallinone.variants.java.*;
import org.openswing.swing.customvo.java.*;
import org.jallinone.variants.java.*;
import org.jallinone.variants.java.*;
import org.jallinone.purchases.documents.java.*;
import org.jallinone.variants.java.*;
import org.jallinone.variants.java.*;
import org.jallinone.system.progressives.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to insert new purchase order rows in DOC07 table.</p>
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
public class InsertPurchaseDocRowsBean  implements InsertPurchaseDocRows {


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


  public InsertPurchaseDocRowsBean() {
  }



  /**
   * Business logic to execute.
   */
  public VOResponse insertPurchaseDocRows(DetailPurchaseDocRowVO voTemplate,VariantsMatrixVO matrixVO,Object[][] cells,BigDecimal currencyDecimals,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;

    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      totalBean.setConn(conn); // use same transaction...
      docBean.setConn(conn); // use same transaction...

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
      attribute2dbField.put("invoiceQtyDOC07","INVOICE_QTY");

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

      DetailPurchaseDocRowVO vo = null;
      VariantsMatrixColumnVO colVO = null;
      VariantsMatrixRowVO rowVO = null;
      Response res = null;
      for(int i=0;i<cells.length;i++) {
        rowVO = (VariantsMatrixRowVO)matrixVO.getRowDescriptors()[i];

        if (matrixVO.getColumnDescriptors().length==0) {

          if (cells[i][0]!=null) {
            vo = (DetailPurchaseDocRowVO)voTemplate.clone();
            VariantsMatrixUtils.setVariantTypesAndCodes(vo,"DOC07",matrixVO,rowVO,null);
            try {
								vo.setQtyDOC07((BigDecimal)cells[i][0]);
							} catch (Exception e) {
								continue;
							}

            PurchaseUtils.updateTotals(vo,currencyDecimals.intValue());

            vo.setInQtyDOC07(new BigDecimal(0));

						if (vo.getDocTypeDOC07().equals(ApplicationConsts.PURCHASE_ORDER_DOC_TYPE) &&
								Boolean.TRUE.equals(vo.getNoWarehouseMovITM01()))
							vo.setInQtyDOC07(vo.getQtyDOC07());

            vo.setOrderQtyDOC07(vo.getQtyDOC07());
            if (vo.getInvoiceQtyDOC07()==null)
              vo.setInvoiceQtyDOC07(new BigDecimal(0));
            vo.setRowNumberDOC07( CompanyProgressiveUtils.getInternalProgressive(vo.getCompanyCodeSys01DOC07(),"DOC07_PURCHASE_ITEMS","ROW_NUMBER",conn) );

            // insert into DOC07...
            res = QueryUtil.insertTable(
                conn,
                new UserSessionParameters(username),
                vo,
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
          } // end if on not null

        }
        else
          for(int k=0;k<matrixVO.getColumnDescriptors().length;k++) {

            colVO = (VariantsMatrixColumnVO)matrixVO.getColumnDescriptors()[k];
            if (cells[i][k]!=null) {
            	vo = (DetailPurchaseDocRowVO)voTemplate.clone();
            	try {
            		vo.setQtyDOC07((BigDecimal)cells[i][k]);
            	} catch (Exception e) {
            		continue;
            	}

            	PurchaseUtils.updateTotals(vo,currencyDecimals.intValue());

              VariantsMatrixUtils.setVariantTypesAndCodes(vo,"DOC07",matrixVO,rowVO,colVO);
              vo.setInQtyDOC07(new BigDecimal(0));
              vo.setOrderQtyDOC07(vo.getQtyDOC07());
              if (vo.getInvoiceQtyDOC07()==null)
                vo.setInvoiceQtyDOC07(new BigDecimal(0));
              vo.setRowNumberDOC07( CompanyProgressiveUtils.getInternalProgressive(vo.getCompanyCodeSys01DOC07(),"DOC07_PURCHASE_ITEMS","ROW_NUMBER",conn) );

              // insert into DOC07...
              res = QueryUtil.insertTable(
                  conn,
                  new UserSessionParameters(username),
                  vo,
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
            } // end if on not null

          } // end inner for

      } // end outer for


      // recalculate totals...
      PurchaseDocPK pk = new PurchaseDocPK(
          vo.getCompanyCodeSys01DOC07(),
          vo.getDocTypeDOC07(),
          vo.getDocYearDOC07(),
          vo.getDocNumberDOC07()
      );
      DetailPurchaseDocVO docVO = docBean.loadPurchaseDoc(pk,serverLanguageId,username,new ArrayList());
      Response totalResponse = totalBean.calcDocTotals(docVO,serverLanguageId,username);
      if (totalResponse.isError()) {
    	  throw new Exception(totalResponse.getErrorMessage());
      }

      pstmt = conn.prepareStatement("update DOC06_PURCHASE set TAXABLE_INCOME=?,TOTAL_VAT=?,TOTAL=?,DOC_STATE=? where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=?");
      pstmt.setBigDecimal(1,docVO.getTaxableIncomeDOC06());
      pstmt.setBigDecimal(2,docVO.getTotalVatDOC06());
      pstmt.setBigDecimal(3,docVO.getTotalDOC06());
      pstmt.setString(4,ApplicationConsts.HEADER_BLOCKED);
      pstmt.setString(5,vo.getCompanyCodeSys01DOC07());
      pstmt.setString(6,vo.getDocTypeDOC07());
      pstmt.setBigDecimal(7,vo.getDocYearDOC07());
      pstmt.setBigDecimal(8,vo.getDocNumberDOC07());
      pstmt.execute();

      return new VOResponse(vo);
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while inserting new purchase order rows",ex);
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


  private boolean containsVariant(VariantsMatrixVO vo,String tableName) {
    for(int i=0;i<vo.getManagedVariants().length;i++)
      if (((VariantNameVO)vo.getManagedVariants()[i]).getTableName().equals(tableName))
        return true;
    return false;
  }


}

