package org.jallinone.sales.pricelist.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.sales.pricelist.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.commons.server.CustomizeQueryUtil;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.items.java.DetailItemVO;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;
import org.jallinone.variants.java.VariantsMatrixVO;
import org.openswing.swing.customvo.java.CustomValueObject;
import org.jallinone.variants.java.VariantsMatrixRowVO;
import org.jallinone.variants.java.VariantsMatrixColumnVO;
import org.jallinone.items.java.ItemPK;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch prices from SAL11 table.</p>
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
public class LoadVariantsPricesAction implements Action {


  public LoadVariantsPricesAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadVariantsPrices";
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

      String sql =
            "select SAL11_VARIANTS_PRICES.COMPANY_CODE_SYS01,SAL11_VARIANTS_PRICES.PRICELIST_CODE_SAL01,"+
            "SAL11_VARIANTS_PRICES.ITEM_CODE_ITM01,SAL11_VARIANTS_PRICES.VALUE,SAL11_VARIANTS_PRICES.START_DATE,SAL11_VARIANTS_PRICES.END_DATE,"+
            "A.DESCRIPTION,ITM01_ITEMS.PROGRESSIVE_HIE02,"+
            "SAL11_VARIANTS_PRICES.VARIANT_TYPE_ITM06,SAL11_VARIANTS_PRICES.VARIANT_CODE_ITM11,"+
            "SAL11_VARIANTS_PRICES.VARIANT_TYPE_ITM07,SAL11_VARIANTS_PRICES.VARIANT_CODE_ITM12,"+
            "SAL11_VARIANTS_PRICES.VARIANT_TYPE_ITM08,SAL11_VARIANTS_PRICES.VARIANT_CODE_ITM13,"+
            "SAL11_VARIANTS_PRICES.VARIANT_TYPE_ITM09,SAL11_VARIANTS_PRICES.VARIANT_CODE_ITM14,"+
            "SAL11_VARIANTS_PRICES.VARIANT_TYPE_ITM10,SAL11_VARIANTS_PRICES.VARIANT_CODE_ITM15 "+
            " from SAL11_VARIANTS_PRICES,SYS10_TRANSLATIONS A,ITM01_ITEMS where "+
            "SAL11_VARIANTS_PRICES.COMPANY_CODE_SYS01=ITM01_ITEMS.COMPANY_CODE_SYS01 and "+
            "SAL11_VARIANTS_PRICES.ITEM_CODE_ITM01=ITM01_ITEMS.ITEM_CODE and "+
            "ITM01_ITEMS.PROGRESSIVE_SYS10=A.PROGRESSIVE and "+
            "A.LANGUAGE_CODE=? and SAL11_VARIANTS_PRICES.COMPANY_CODE_SYS01=? and ITM01_ITEMS.ENABLED='Y' and "+
            "SAL11_VARIANTS_PRICES.PRICELIST_CODE_SAL01=? and "+
            "SAL11_VARIANTS_PRICES.ITEM_CODE_ITM01=?";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SAL11","SAL11_VARIANTS_PRICES.COMPANY_CODE_SYS01");
      attribute2dbField.put("pricelistCodeSal01SAL11","SAL11_VARIANTS_PRICES.PRICELIST_CODE_SAL01");
      attribute2dbField.put("itemCodeItm01SAL11","SAL11_VARIANTS_PRICES.ITEM_CODE_ITM01");
      attribute2dbField.put("valueSAL11","SAL11_VARIANTS_PRICES.VALUE");
      attribute2dbField.put("startDateSAL11","SAL11_VARIANTS_PRICES.START_DATE");
      attribute2dbField.put("endDateSAL11","SAL11_VARIANTS_PRICES.END_DATE");
      attribute2dbField.put("itemDescriptionSYS10","A.DESCRIPTION");
      attribute2dbField.put("pricelistDescriptionSYS10","B.DESCRIPTION");
      attribute2dbField.put("progressiveHie02ITM01","ITM01_ITEMS.PROGRESSIVE_HIE02");

      attribute2dbField.put("variantTypeItm06SAL11","SAL11_VARIANTS_PRICES.VARIANT_TYPE_ITM06");
      attribute2dbField.put("variantCodeItm11SAL11","SAL11_VARIANTS_PRICES.VARIANT_CODE_ITM11");
      attribute2dbField.put("variantTypeItm07SAL11","SAL11_VARIANTS_PRICES.VARIANT_TYPE_ITM07");
      attribute2dbField.put("variantCodeItm12SAL11","SAL11_VARIANTS_PRICES.VARIANT_CODE_ITM12");
      attribute2dbField.put("variantTypeItm08SAL11","SAL11_VARIANTS_PRICES.VARIANT_TYPE_ITM08");
      attribute2dbField.put("variantCodeItm13SAL11","SAL11_VARIANTS_PRICES.VARIANT_CODE_ITM13");
      attribute2dbField.put("variantTypeItm09SAL11","SAL11_VARIANTS_PRICES.VARIANT_TYPE_ITM09");
      attribute2dbField.put("variantCodeItm14SAL11","SAL11_VARIANTS_PRICES.VARIANT_CODE_ITM14");
      attribute2dbField.put("variantTypeItm10SAL11","SAL11_VARIANTS_PRICES.VARIANT_TYPE_ITM10");
      attribute2dbField.put("variantCodeItm15SAL11","SAL11_VARIANTS_PRICES.VARIANT_CODE_ITM15");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(itemPK.getCompanyCodeSys01ITM01());
      values.add(priceListCode);
      values.add(itemPK.getItemCodeITM01());


      // read ALL from SAL11 table...
      Response res = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          VariantsPriceVO.class,
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
      VariantsPriceVO vo = null;
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
        vo = (VariantsPriceVO)rows.get(i);
        customVO = (CustomValueObject)indexes.get(vo.getVariantTypeItm06SAL11()+" "+vo.getVariantCodeItm11SAL11());
        if (matrixVO.getColumnDescriptors().size()==0) {
          if (customVO==null)
            customVO = (CustomValueObject)indexes.get(vo.getVariantTypeItm07SAL11()+" "+vo.getVariantCodeItm12SAL11());
          if (customVO==null)
            customVO = (CustomValueObject)indexes.get(vo.getVariantTypeItm08SAL11()+" "+vo.getVariantCodeItm13SAL11());
          if (customVO==null)
            customVO = (CustomValueObject)indexes.get(vo.getVariantTypeItm09SAL11()+" "+vo.getVariantCodeItm14SAL11());
          if (customVO==null)
            customVO = (CustomValueObject)indexes.get(vo.getVariantTypeItm10SAL11()+" "+vo.getVariantCodeItm15SAL11());
          customVO.setAttributeNameN0(vo.getValueSAL11());
        }
        else if (customVO!=null) {

          for(int j=0;j<cols;j++) {
            colVO = (VariantsMatrixColumnVO)matrixVO.getColumnDescriptors().get(j);
            if ((colVO.getVariantCodeITM12()==null && vo.getVariantCodeItm12SAL11().equals(ApplicationConsts.JOLLY) || colVO.getVariantCodeITM12().equals(vo.getVariantCodeItm12SAL11())) &&
                (colVO.getVariantCodeITM13()==null && vo.getVariantCodeItm13SAL11().equals(ApplicationConsts.JOLLY) || colVO.getVariantCodeITM13().equals(vo.getVariantCodeItm13SAL11())) &&
                (colVO.getVariantCodeITM14()==null && vo.getVariantCodeItm14SAL11().equals(ApplicationConsts.JOLLY) || colVO.getVariantCodeITM14().equals(vo.getVariantCodeItm14SAL11())) &&
                (colVO.getVariantCodeITM15()==null && vo.getVariantCodeItm15SAL11().equals(ApplicationConsts.JOLLY) || colVO.getVariantCodeITM15().equals(vo.getVariantCodeItm15SAL11())) &&
                (colVO.getVariantTypeITM07()==null && vo.getVariantTypeItm07SAL11().equals(ApplicationConsts.JOLLY) || colVO.getVariantTypeITM07().equals(vo.getVariantTypeItm07SAL11())) &&
                (colVO.getVariantTypeITM08()==null && vo.getVariantTypeItm08SAL11().equals(ApplicationConsts.JOLLY) || colVO.getVariantTypeITM08().equals(vo.getVariantTypeItm08SAL11())) &&
                (colVO.getVariantTypeITM09()==null && vo.getVariantTypeItm09SAL11().equals(ApplicationConsts.JOLLY) || colVO.getVariantTypeITM09().equals(vo.getVariantTypeItm09SAL11())) &&
                (colVO.getVariantTypeITM10()==null && vo.getVariantTypeItm10SAL11().equals(ApplicationConsts.JOLLY) || colVO.getVariantTypeITM10().equals(vo.getVariantTypeItm10SAL11()))) {
              try {
                CustomValueObject.class.getMethod("setAttributeNameN" + j,new Class[] {BigDecimal.class}).invoke(customVO, new Object[] {vo.getValueSAL11()});
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching prices list",ex);
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
