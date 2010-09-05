package org.jallinone.purchases.pricelist.server;

import java.math.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.jallinone.commons.java.*;
import org.jallinone.events.server.*;
import org.jallinone.items.java.*;
import org.jallinone.purchases.pricelist.java.*;
import org.jallinone.sales.pricelist.java.*;
import org.jallinone.system.server.*;
import org.jallinone.variants.java.*;
import org.openswing.swing.customvo.java.*;
import org.openswing.swing.logger.server.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.*;
import org.openswing.swing.server.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch supplier prices for item's variants from PUR05 table.</p>
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
public class LoadSupplierVariantsPricesAction implements Action {


  public LoadSupplierVariantsPricesAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadSupplierVariantsPrices";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();

    Connection conn = null;
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

      GridParams params = (GridParams)inputPar;
      VariantsMatrixVO matrixVO = (VariantsMatrixVO)params.getOtherGridParams().get(ApplicationConsts.VARIANTS_MATRIX_VO);
      ItemPK itemPK = matrixVO.getItemPK();
      String priceListCode = (String)params.getOtherGridParams().get(ApplicationConsts.PRICELIST);
      BigDecimal progressiveReg04 = (BigDecimal)params.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04);

      String sql =
            "select PUR05_SUPPLIER_VARIANTS_PRICES.PROGRESSIVE,PUR05_SUPPLIER_VARIANTS_PRICES.COMPANY_CODE_SYS01,"+
            "PUR05_SUPPLIER_VARIANTS_PRICES.PROGRESSIVE_REG04,PUR05_SUPPLIER_VARIANTS_PRICES.PRICELIST_CODE_PUR03,"+
            "PUR05_SUPPLIER_VARIANTS_PRICES.ITEM_CODE_ITM01,PUR05_SUPPLIER_VARIANTS_PRICES.VALUE,"+
            "PUR05_SUPPLIER_VARIANTS_PRICES.START_DATE,PUR05_SUPPLIER_VARIANTS_PRICES.END_DATE,"+
            "PUR05_SUPPLIER_VARIANTS_PRICES.VARIANT_TYPE_ITM06,PUR05_SUPPLIER_VARIANTS_PRICES.VARIANT_CODE_ITM11,"+
            "PUR05_SUPPLIER_VARIANTS_PRICES.VARIANT_TYPE_ITM07,PUR05_SUPPLIER_VARIANTS_PRICES.VARIANT_CODE_ITM12,"+
            "PUR05_SUPPLIER_VARIANTS_PRICES.VARIANT_TYPE_ITM08,PUR05_SUPPLIER_VARIANTS_PRICES.VARIANT_CODE_ITM13,"+
            "PUR05_SUPPLIER_VARIANTS_PRICES.VARIANT_TYPE_ITM09,PUR05_SUPPLIER_VARIANTS_PRICES.VARIANT_CODE_ITM14,"+
            "PUR05_SUPPLIER_VARIANTS_PRICES.VARIANT_TYPE_ITM10,PUR05_SUPPLIER_VARIANTS_PRICES.VARIANT_CODE_ITM15 "+
            " from PUR05_SUPPLIER_VARIANTS_PRICES where "+
            "PUR05_SUPPLIER_VARIANTS_PRICES.COMPANY_CODE_SYS01=? and "+
            "PUR05_SUPPLIER_VARIANTS_PRICES.PROGRESSIVE_REG04=? and "+
            "PUR05_SUPPLIER_VARIANTS_PRICES.PRICELIST_CODE_PUR03=? and "+
            "PUR05_SUPPLIER_VARIANTS_PRICES.ITEM_CODE_ITM01=?";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("progressivePUR05","PUR05_SUPPLIER_VARIANTS_PRICES.PROGRESSIVE");
      attribute2dbField.put("companyCodeSys01PUR05","PUR05_SUPPLIER_VARIANTS_PRICES.COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveReg04PUR05","PUR05_SUPPLIER_VARIANTS_PRICES.PROGRESSIVE_REG04");
      attribute2dbField.put("pricelistCodePur03PUR05","PUR05_SUPPLIER_VARIANTS_PRICES.PRICELIST_CODE_PUR03");
      attribute2dbField.put("itemCodeItm01PUR05","PUR05_SUPPLIER_VARIANTS_PRICES.ITEM_CODE_ITM01");
      attribute2dbField.put("valuePUR05","PUR05_SUPPLIER_VARIANTS_PRICES.VALUE");
      attribute2dbField.put("startDatePUR05","PUR05_SUPPLIER_VARIANTS_PRICES.START_DATE");
      attribute2dbField.put("endDatePUR05","PUR05_SUPPLIER_VARIANTS_PRICES.END_DATE");
      attribute2dbField.put("itemDescriptionSYS10","A.DESCRIPTION");
      attribute2dbField.put("pricelistDescriptionSYS10","B.DESCRIPTION");
      attribute2dbField.put("progressiveHie02ITM01","ITM01_ITEMS.PROGRESSIVE_HIE02");

      attribute2dbField.put("variantTypeItm06PUR05","PUR05_SUPPLIER_VARIANTS_PRICES.VARIANT_TYPE_ITM06");
      attribute2dbField.put("variantCodeItm11PUR05","PUR05_SUPPLIER_VARIANTS_PRICES.VARIANT_CODE_ITM11");
      attribute2dbField.put("variantTypeItm07PUR05","PUR05_SUPPLIER_VARIANTS_PRICES.VARIANT_TYPE_ITM07");
      attribute2dbField.put("variantCodeItm12PUR05","PUR05_SUPPLIER_VARIANTS_PRICES.VARIANT_CODE_ITM12");
      attribute2dbField.put("variantTypeItm08PUR05","PUR05_SUPPLIER_VARIANTS_PRICES.VARIANT_TYPE_ITM08");
      attribute2dbField.put("variantCodeItm13PUR05","PUR05_SUPPLIER_VARIANTS_PRICES.VARIANT_CODE_ITM13");
      attribute2dbField.put("variantTypeItm09PUR05","PUR05_SUPPLIER_VARIANTS_PRICES.VARIANT_TYPE_ITM09");
      attribute2dbField.put("variantCodeItm14PUR05","PUR05_SUPPLIER_VARIANTS_PRICES.VARIANT_CODE_ITM14");
      attribute2dbField.put("variantTypeItm10PUR05","PUR05_SUPPLIER_VARIANTS_PRICES.VARIANT_TYPE_ITM10");
      attribute2dbField.put("variantCodeItm15PUR05","PUR05_SUPPLIER_VARIANTS_PRICES.VARIANT_CODE_ITM15");

      ArrayList values = new ArrayList();
      values.add(itemPK.getCompanyCodeSys01ITM01());
      values.add(progressiveReg04);
      values.add(priceListCode);
      values.add(itemPK.getItemCodeITM01());


      // read ALL from PUR05 table...
      Response res = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          SupplierVariantsPriceVO.class,
          "Y",
          "N",
          context,
          new GridParams(),
          true
      );

      if (res.isError())
        return res;


      java.util.List rows = ((VOListResponse)res).getRows();


      // convert the records list in matrix format...
      ArrayList matrixRows = new ArrayList();
      SupplierVariantsPriceVO vo = null;
      CustomValueObject customVO = null;
      VariantsMatrixRowVO rowVO = null;
      VariantsMatrixColumnVO colVO = null;
      HashMap indexes = new HashMap();
      int cols = matrixVO.getColumnDescriptors().size()==0?1:matrixVO.getColumnDescriptors().size();
      for(int i=0;i<matrixVO.getRowDescriptors().size();i++) {
        rowVO = (VariantsMatrixRowVO)matrixVO.getRowDescriptors().get(i);

        customVO = new CustomValueObject();
        customVO.setAttributeNameS0(rowVO.getRowDescription());
        matrixRows.add(customVO);
        indexes.put(rowVO.getVariantTypeITM06()+" "+rowVO.getVariantCodeITM11(),customVO);
      }
      for(int i=0;i<rows.size();i++) {
        vo = (SupplierVariantsPriceVO)rows.get(i);
        customVO = (CustomValueObject)indexes.get(vo.getVariantTypeItm06PUR05()+" "+vo.getVariantCodeItm11PUR05());
        if (matrixVO.getColumnDescriptors().size()==0) {
          if (customVO==null)
            customVO = (CustomValueObject)indexes.get(vo.getVariantTypeItm07PUR05()+" "+vo.getVariantCodeItm12PUR05());
          if (customVO==null)
            customVO = (CustomValueObject)indexes.get(vo.getVariantTypeItm08PUR05()+" "+vo.getVariantCodeItm13PUR05());
          if (customVO==null)
            customVO = (CustomValueObject)indexes.get(vo.getVariantTypeItm09PUR05()+" "+vo.getVariantCodeItm14PUR05());
          if (customVO==null)
            customVO = (CustomValueObject)indexes.get(vo.getVariantTypeItm10PUR05()+" "+vo.getVariantCodeItm15PUR05());
          customVO.setAttributeNameN0(vo.getValuePUR05());
        }
        else if (customVO!=null) {

          for(int j=0;j<cols;j++) {
            colVO = (VariantsMatrixColumnVO)matrixVO.getColumnDescriptors().get(j);
            if ((colVO.getVariantCodeITM12()==null && vo.getVariantCodeItm12PUR05().equals(ApplicationConsts.JOLLY) || colVO.getVariantCodeITM12().equals(vo.getVariantCodeItm12PUR05())) &&
                (colVO.getVariantCodeITM13()==null && vo.getVariantCodeItm13PUR05().equals(ApplicationConsts.JOLLY) || colVO.getVariantCodeITM13().equals(vo.getVariantCodeItm13PUR05())) &&
                (colVO.getVariantCodeITM14()==null && vo.getVariantCodeItm14PUR05().equals(ApplicationConsts.JOLLY) || colVO.getVariantCodeITM14().equals(vo.getVariantCodeItm14PUR05())) &&
                (colVO.getVariantCodeITM15()==null && vo.getVariantCodeItm15PUR05().equals(ApplicationConsts.JOLLY) || colVO.getVariantCodeITM15().equals(vo.getVariantCodeItm15PUR05())) &&
                (colVO.getVariantTypeITM07()==null && vo.getVariantTypeItm07PUR05().equals(ApplicationConsts.JOLLY) || colVO.getVariantTypeITM07().equals(vo.getVariantTypeItm07PUR05())) &&
                (colVO.getVariantTypeITM08()==null && vo.getVariantTypeItm08PUR05().equals(ApplicationConsts.JOLLY) || colVO.getVariantTypeITM08().equals(vo.getVariantTypeItm08PUR05())) &&
                (colVO.getVariantTypeITM09()==null && vo.getVariantTypeItm09PUR05().equals(ApplicationConsts.JOLLY) || colVO.getVariantTypeITM09().equals(vo.getVariantTypeItm09PUR05())) &&
                (colVO.getVariantTypeITM10()==null && vo.getVariantTypeItm10PUR05().equals(ApplicationConsts.JOLLY) || colVO.getVariantTypeITM10().equals(vo.getVariantTypeItm10PUR05()))) {
              try {
                CustomValueObject.class.getMethod("setAttributeNameN" + j,new Class[] {BigDecimal.class}).invoke(customVO, new Object[] {vo.getValuePUR05()});
              }
              catch (Throwable ex) {
                ex.printStackTrace();
              }
              break;
            }
          }

        } // end else
      } // end for on rows





      return new VOListResponse(matrixRows,false,matrixRows.size());
    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching supplier's prices list",ex);
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        ConnectionManager.releaseConnection(conn, context);
      }
      catch (Exception ex1) {
      }
    }

  }



}
