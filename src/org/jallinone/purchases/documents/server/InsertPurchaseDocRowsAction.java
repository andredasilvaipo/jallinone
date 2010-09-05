package org.jallinone.purchases.documents.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.purchases.documents.java.DetailPurchaseDocRowVO;
import org.jallinone.system.server.JAIOUserSessionParameters;
import java.math.BigDecimal;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.system.progressives.server.ProgressiveUtils;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.system.java.ApplicationParametersVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.message.send.java.LookupValidationParams;
import org.jallinone.purchases.documents.java.PurchaseDocPK;
import org.jallinone.purchases.documents.java.DetailPurchaseDocVO;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;
import org.jallinone.variants.java.VariantsMatrixVO;
import org.openswing.swing.customvo.java.CustomValueObject;
import org.jallinone.variants.java.VariantsMatrixColumnVO;
import org.jallinone.variants.java.VariantsMatrixRowVO;
import org.jallinone.purchases.documents.java.PurchaseUtils;
import org.jallinone.variants.java.VariantNameVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to insert new purchase order rows in DOC07 table.</p>
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
public class InsertPurchaseDocRowsAction implements Action {

  private PurchaseDocTotalsBean totalBean = new PurchaseDocTotalsBean();
  private LoadPurchaseDocBean docBean = new LoadPurchaseDocBean();
  private ProgressiveUtils progBean = new ProgressiveUtils();


  public InsertPurchaseDocRowsAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "insertPurchaseDocRows";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    Connection conn = null;
    PreparedStatement pstmt = null;
    try {
      conn = ConnectionManager.getConnection(context);

      // fires the GenericEvent.CONNECTION_CREATED event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        getRequestName(),
        GenericEvent.CONNECTION_CREATED,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        inputPar,
        null
      ));

      Object[] pars = (Object[])inputPar;
      DetailPurchaseDocRowVO voTemplate = (DetailPurchaseDocRowVO)pars[0];
      VariantsMatrixVO matrixVO = (VariantsMatrixVO)pars[1];
      Object[][] cells = (Object[][])pars[2];
      BigDecimal currencyDecimals = (BigDecimal)pars[3];

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
        rowVO = (VariantsMatrixRowVO)matrixVO.getRowDescriptors().get(i);

        if (matrixVO.getColumnDescriptors().size()==0) {

          if (cells[i][0]!=null) {
            vo = (DetailPurchaseDocRowVO)voTemplate.clone();

            if (!containsVariant(matrixVO,"ITM11_VARIANTS_1")) {
              // e.g. color but not no size...
              vo.setVariantCodeItm11DOC07(ApplicationConsts.JOLLY);
              vo.setVariantTypeItm06DOC07(ApplicationConsts.JOLLY);
            }
            else {
              vo.setVariantCodeItm11DOC07(rowVO.getVariantCodeITM11());
              vo.setVariantTypeItm06DOC07(rowVO.getVariantTypeITM06());
            }
            if (!containsVariant(matrixVO,"ITM12_VARIANTS_2")) {
              vo.setVariantCodeItm12DOC07(ApplicationConsts.JOLLY);
              vo.setVariantTypeItm07DOC07(ApplicationConsts.JOLLY);
            }
            else {
              vo.setVariantCodeItm12DOC07(rowVO.getVariantCodeITM11());
              vo.setVariantTypeItm07DOC07(rowVO.getVariantTypeITM06());
            }
            if (!containsVariant(matrixVO,"ITM13_VARIANTS_3")) {
              vo.setVariantCodeItm13DOC07(ApplicationConsts.JOLLY);
              vo.setVariantTypeItm08DOC07(ApplicationConsts.JOLLY);
            }
            else {
              vo.setVariantCodeItm13DOC07(rowVO.getVariantCodeITM11());
              vo.setVariantTypeItm08DOC07(rowVO.getVariantTypeITM06());
            }
            if (!containsVariant(matrixVO,"ITM14_VARIANTS_4")) {
              vo.setVariantCodeItm14DOC07(ApplicationConsts.JOLLY);
              vo.setVariantTypeItm09DOC07(ApplicationConsts.JOLLY);
            }
            else {
              vo.setVariantCodeItm14DOC07(rowVO.getVariantCodeITM11());
              vo.setVariantTypeItm09DOC07(rowVO.getVariantTypeITM06());
            }
            if (!containsVariant(matrixVO,"ITM15_VARIANTS_5")) {
              vo.setVariantCodeItm15DOC07(ApplicationConsts.JOLLY);
              vo.setVariantTypeItm10DOC07(ApplicationConsts.JOLLY);
            }
            else {
              vo.setVariantCodeItm15DOC07(rowVO.getVariantCodeITM11());
              vo.setVariantTypeItm10DOC07(rowVO.getVariantTypeITM06());
            }

            vo.setQtyDOC07((BigDecimal)cells[i][0]);

            PurchaseUtils.updateTotals(vo,currencyDecimals.intValue());

/*
            vo.setVariantCodeItm12DOC07(ApplicationConsts.JOLLY);
            vo.setVariantCodeItm13DOC07(ApplicationConsts.JOLLY);
            vo.setVariantCodeItm14DOC07(ApplicationConsts.JOLLY);
            vo.setVariantCodeItm15DOC07(ApplicationConsts.JOLLY);

            vo.setVariantTypeItm07DOC07(ApplicationConsts.JOLLY);
            vo.setVariantTypeItm08DOC07(ApplicationConsts.JOLLY);
            vo.setVariantTypeItm09DOC07(ApplicationConsts.JOLLY);
            vo.setVariantTypeItm10DOC07(ApplicationConsts.JOLLY);
*/
            vo.setInQtyDOC07(new BigDecimal(0));
            vo.setOrderQtyDOC07(vo.getQtyDOC07());
            if (vo.getInvoiceQtyDOC07()==null)
              vo.setInvoiceQtyDOC07(new BigDecimal(0));
            vo.setRowNumberDOC07( progBean.getInternalProgressive("DOC07_PURCHASE_ITEMS","ROW_NUMBER",conn) );

            // insert into DOC07...
            res = QueryUtil.insertTable(
                conn,
                userSessionPars,
                vo,
                "DOC07_PURCHASE_ITEMS",
                attribute2dbField,
                "Y",
                "N",
                context,
                true
            );

            if (res.isError()) {
              conn.rollback();
              return res;
            }
          } // end if on not null

        }
        else
          for(int k=0;k<matrixVO.getColumnDescriptors().size();k++) {

            colVO = (VariantsMatrixColumnVO)matrixVO.getColumnDescriptors().get(k);
            if (cells[i][k]!=null) {
              vo = (DetailPurchaseDocRowVO)voTemplate.clone();

              vo.setQtyDOC07((BigDecimal)cells[i][k]);

              PurchaseUtils.updateTotals(vo,currencyDecimals.intValue());

              vo.setVariantCodeItm11DOC07(rowVO.getVariantCodeITM11());
              vo.setVariantTypeItm06DOC07(rowVO.getVariantTypeITM06());

              vo.setVariantCodeItm12DOC07(colVO.getVariantCodeITM12()==null?ApplicationConsts.JOLLY:colVO.getVariantCodeITM12());
              vo.setVariantCodeItm13DOC07(colVO.getVariantCodeITM13()==null?ApplicationConsts.JOLLY:colVO.getVariantCodeITM13());
              vo.setVariantCodeItm14DOC07(colVO.getVariantCodeITM14()==null?ApplicationConsts.JOLLY:colVO.getVariantCodeITM14());
              vo.setVariantCodeItm15DOC07(colVO.getVariantCodeITM15()==null?ApplicationConsts.JOLLY:colVO.getVariantCodeITM15());

              vo.setVariantTypeItm07DOC07(colVO.getVariantTypeITM07()==null?ApplicationConsts.JOLLY:colVO.getVariantTypeITM07());
              vo.setVariantTypeItm08DOC07(colVO.getVariantTypeITM08()==null?ApplicationConsts.JOLLY:colVO.getVariantTypeITM08());
              vo.setVariantTypeItm09DOC07(colVO.getVariantTypeITM09()==null?ApplicationConsts.JOLLY:colVO.getVariantTypeITM09());
              vo.setVariantTypeItm10DOC07(colVO.getVariantTypeITM10()==null?ApplicationConsts.JOLLY:colVO.getVariantTypeITM10());

              vo.setInQtyDOC07(new BigDecimal(0));
              vo.setOrderQtyDOC07(vo.getQtyDOC07());
              if (vo.getInvoiceQtyDOC07()==null)
                vo.setInvoiceQtyDOC07(new BigDecimal(0));
              vo.setRowNumberDOC07( progBean.getInternalProgressive("DOC07_PURCHASE_ITEMS","ROW_NUMBER",conn) );

              // insert into DOC07...
              res = QueryUtil.insertTable(
                  conn,
                  userSessionPars,
                  vo,
                  "DOC07_PURCHASE_ITEMS",
                  attribute2dbField,
                  "Y",
                  "N",
                  context,
                  true
              );

              if (res.isError()) {
                conn.rollback();
                return res;
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
      Response docResponse = docBean.loadPurchaseDoc(conn,pk,userSessionPars,request,response,userSession,context);
      if (docResponse.isError()) {
        conn.rollback();
        return docResponse;
      }
      DetailPurchaseDocVO docVO = (DetailPurchaseDocVO)((VOResponse)docResponse).getVo();
      Response totalResponse = totalBean.calcDocTotals(conn,docVO,userSessionPars,request,response,userSession,context);
      if (totalResponse.isError()) {
        conn.rollback();
        return totalResponse;
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

      Response answer = new VOResponse(vo);

      // fires the GenericEvent.BEFORE_COMMIT event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        getRequestName(),
        GenericEvent.BEFORE_COMMIT,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        inputPar,
        answer
      ));

      conn.commit();

      // fires the GenericEvent.AFTER_COMMIT event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        getRequestName(),
        GenericEvent.AFTER_COMMIT,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        inputPar,
        answer
      ));

      return answer;
    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while inserting new purchase order rows",ex);
      try {
        conn.rollback();
      }
      catch (Exception ex3) {
      }
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        pstmt.close();
      }
      catch (Exception ex2) {
      }
      try {
        ConnectionManager.releaseConnection(conn, context);
      }
      catch (Exception ex1) {
      }
    }
  }


  private boolean containsVariant(VariantsMatrixVO vo,String tableName) {
    for(int i=0;i<vo.getManagedVariants().size();i++)
      if (((VariantNameVO)vo.getManagedVariants().get(i)).getTableName().equals(tableName))
        return true;
    return false;
  }


}
