package org.jallinone.purchases.pricelist.server;

import java.math.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.jallinone.commons.java.*;
import org.jallinone.events.server.*;
import org.jallinone.sales.pricelist.java.*;
import org.jallinone.system.server.*;
import org.jallinone.variants.java.*;
import org.openswing.swing.logger.server.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.server.*;
import org.jallinone.purchases.pricelist.java.SupplierVariantsPriceVO;
import org.jallinone.system.progressives.server.ProgressiveUtils;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to remove existing records ands re-insert one or more supplier prices in PUR05 table.</p>
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
public class UpdateSupplierVariantsPricesAction implements Action {


  public UpdateSupplierVariantsPricesAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "updateSupplierVariantsPrices";
  }

  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,
                                       UserSessionParameters userSessionPars,
                                       HttpServletRequest request,
                                       HttpServletResponse response,
                                       HttpSession userSession,
                                       ServletContext context) {
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


      Object[] objs = (Object[])inputPar;
      VariantsMatrixVO matrixVO = (VariantsMatrixVO)objs[0];
      Object[][] cells = (Object[][])objs[1];
      String priceListCode = (String)objs[2];
      java.sql.Date startDate = (java.sql.Date)objs[3];
      java.sql.Date endDate = (java.sql.Date)objs[4];
      BigDecimal progressiveReg04 = (BigDecimal)objs[5];


      // remove all prices related to the specified item/pricelist...
      pstmt = conn.prepareStatement("delete from PUR05_SUPPLIER_VARIANTS_PRICES where COMPANY_CODE_SYS01=? and PROGRESSIVE_REG04=? and PRICELIST_CODE_PUR03=? and ITEM_CODE_ITM01=?");
      pstmt.setString(1,matrixVO.getItemPK().getCompanyCodeSys01ITM01());
      pstmt.setBigDecimal(2,progressiveReg04);
      pstmt.setString(3,priceListCode);
      pstmt.setString(4,matrixVO.getItemPK().getItemCodeITM01());
      pstmt.execute();

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("progressivePUR05","PROGRESSIVE");
      attribute2dbField.put("companyCodeSys01PUR05","COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveReg04PUR05","PROGRESSIVE_REG04");
      attribute2dbField.put("pricelistCodePur03PUR05","PRICELIST_CODE_PUR03");
      attribute2dbField.put("itemCodeItm01PUR05","ITEM_CODE_ITM01");
      attribute2dbField.put("valuePUR05","VALUE");
      attribute2dbField.put("startDatePUR05","START_DATE");
      attribute2dbField.put("endDatePUR05","END_DATE");

      attribute2dbField.put("variantTypeItm06PUR05","VARIANT_TYPE_ITM06");
      attribute2dbField.put("variantCodeItm11PUR05","VARIANT_CODE_ITM11");
      attribute2dbField.put("variantTypeItm07PUR05","VARIANT_TYPE_ITM07");
      attribute2dbField.put("variantCodeItm12PUR05","VARIANT_CODE_ITM12");
      attribute2dbField.put("variantTypeItm08PUR05","VARIANT_TYPE_ITM08");
      attribute2dbField.put("variantCodeItm13PUR05","VARIANT_CODE_ITM13");
      attribute2dbField.put("variantTypeItm09PUR05","VARIANT_TYPE_ITM09");
      attribute2dbField.put("variantCodeItm14PUR05","VARIANT_CODE_ITM14");
      attribute2dbField.put("variantTypeItm10PUR05","VARIANT_TYPE_ITM10");
      attribute2dbField.put("variantCodeItm15PUR05","VARIANT_CODE_ITM15");


      // insert into PUR05...
      SupplierVariantsPriceVO vo = null;
      Response res = null;
      Object[] row = null;
      VariantsMatrixColumnVO colVO = null;
      VariantsMatrixRowVO rowVO = null;
      for(int i=0;i<cells.length;i++) {
        row = cells[i];
        rowVO = (VariantsMatrixRowVO)matrixVO.getRowDescriptors().get(i);

        if (matrixVO.getColumnDescriptors().size()==0) {

          if (cells[i][0]!=null) {
            vo = new SupplierVariantsPriceVO();
            vo.setProgressivePUR05(ProgressiveUtils.getConsecutiveProgressive("PUR05_SUPPLIER_VARIANTS_PRICES","PROGRESSIVE",conn));
            vo.setCompanyCodeSys01PUR05(matrixVO.getItemPK().getCompanyCodeSys01ITM01());
            vo.setItemCodeItm01PUR05(matrixVO.getItemPK().getItemCodeITM01());
            vo.setProgressiveReg04PUR05(progressiveReg04);
            vo.setPricelistCodePur03PUR05(priceListCode);
            vo.setValuePUR05((BigDecimal)cells[i][0]);
            vo.setStartDatePUR05(startDate);
            vo.setEndDatePUR05(endDate);

            if (!containsVariant(matrixVO,"ITM11_VARIANTS_1")) {
              // e.g. color but not no size...
              vo.setVariantCodeItm11PUR05(ApplicationConsts.JOLLY);
              vo.setVariantTypeItm06PUR05(ApplicationConsts.JOLLY);
            }
            else {
              vo.setVariantCodeItm11PUR05(rowVO.getVariantCodeITM11());
              vo.setVariantTypeItm06PUR05(rowVO.getVariantTypeITM06());
            }
            if (!containsVariant(matrixVO,"ITM12_VARIANTS_2")) {
              vo.setVariantCodeItm12PUR05(ApplicationConsts.JOLLY);
              vo.setVariantTypeItm07PUR05(ApplicationConsts.JOLLY);
            }
            else {
              vo.setVariantCodeItm12PUR05(rowVO.getVariantCodeITM11());
              vo.setVariantTypeItm07PUR05(rowVO.getVariantTypeITM06());
            }
            if (!containsVariant(matrixVO,"ITM13_VARIANTS_3")) {
              vo.setVariantCodeItm13PUR05(ApplicationConsts.JOLLY);
              vo.setVariantTypeItm08PUR05(ApplicationConsts.JOLLY);
            }
            else {
              vo.setVariantCodeItm13PUR05(rowVO.getVariantCodeITM11());
              vo.setVariantTypeItm08PUR05(rowVO.getVariantTypeITM06());
            }
            if (!containsVariant(matrixVO,"ITM14_VARIANTS_4")) {
              vo.setVariantCodeItm14PUR05(ApplicationConsts.JOLLY);
              vo.setVariantTypeItm09PUR05(ApplicationConsts.JOLLY);
            }
            else {
              vo.setVariantCodeItm14PUR05(rowVO.getVariantCodeITM11());
              vo.setVariantTypeItm09PUR05(rowVO.getVariantTypeITM06());
            }
            if (!containsVariant(matrixVO,"ITM15_VARIANTS_5")) {
              vo.setVariantCodeItm15PUR05(ApplicationConsts.JOLLY);
              vo.setVariantTypeItm10PUR05(ApplicationConsts.JOLLY);
            }
            else {
              vo.setVariantCodeItm15PUR05(rowVO.getVariantCodeITM11());
              vo.setVariantTypeItm10PUR05(rowVO.getVariantTypeITM06());
            }

            res = QueryUtil.insertTable(
                conn,
                userSessionPars,
                vo,
                "PUR05_SUPPLIER_VARIANTS_PRICES",
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
          }

        }
        else
          for(int k=0;k<matrixVO.getColumnDescriptors().size();k++) {

            colVO = (VariantsMatrixColumnVO)matrixVO.getColumnDescriptors().get(k);
            if (cells[i][k]!=null) {
              vo = new SupplierVariantsPriceVO();
              vo.setProgressivePUR05(ProgressiveUtils.getConsecutiveProgressive("PUR05_SUPPLIER_VARIANTS_PRICES","PROGRESSIVE",conn));
              vo.setCompanyCodeSys01PUR05(matrixVO.getItemPK().getCompanyCodeSys01ITM01());
              vo.setItemCodeItm01PUR05(matrixVO.getItemPK().getItemCodeITM01());
              vo.setProgressiveReg04PUR05(progressiveReg04);
              vo.setPricelistCodePur03PUR05(priceListCode);
              vo.setValuePUR05((BigDecimal)cells[i][k]);
              vo.setStartDatePUR05(startDate);
              vo.setEndDatePUR05(endDate);

              vo.setVariantCodeItm11PUR05(rowVO.getVariantCodeITM11());
              vo.setVariantTypeItm06PUR05(rowVO.getVariantTypeITM06());

              vo.setVariantCodeItm12PUR05(colVO.getVariantCodeITM12()==null?ApplicationConsts.JOLLY:colVO.getVariantCodeITM12());
              vo.setVariantCodeItm13PUR05(colVO.getVariantCodeITM13()==null?ApplicationConsts.JOLLY:colVO.getVariantCodeITM13());
              vo.setVariantCodeItm14PUR05(colVO.getVariantCodeITM14()==null?ApplicationConsts.JOLLY:colVO.getVariantCodeITM14());
              vo.setVariantCodeItm15PUR05(colVO.getVariantCodeITM15()==null?ApplicationConsts.JOLLY:colVO.getVariantCodeITM15());

              vo.setVariantTypeItm07PUR05(colVO.getVariantTypeITM07()==null?ApplicationConsts.JOLLY:colVO.getVariantTypeITM07());
              vo.setVariantTypeItm08PUR05(colVO.getVariantTypeITM08()==null?ApplicationConsts.JOLLY:colVO.getVariantTypeITM08());
              vo.setVariantTypeItm09PUR05(colVO.getVariantTypeITM09()==null?ApplicationConsts.JOLLY:colVO.getVariantTypeITM09());
              vo.setVariantTypeItm10PUR05(colVO.getVariantTypeITM10()==null?ApplicationConsts.JOLLY:colVO.getVariantTypeITM10());

              res = QueryUtil.insertTable(
                  conn,
                  userSessionPars,
                  vo,
                  "PUR05_SUPPLIER_VARIANTS_PRICES",
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

            } // end if on cell not null
          } // end inner for
      } // end outer for

      Response answer = new VOResponse(Boolean.TRUE);


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
      Logger.error(userSessionPars.getUsername(), this.getClass().getName(),
                   "executeCommand", "Error while inserting price for variants", ex);
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

