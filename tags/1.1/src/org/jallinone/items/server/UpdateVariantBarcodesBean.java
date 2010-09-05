package org.jallinone.items.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.items.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.commons.server.CustomizeQueryUtil;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;
import org.jallinone.variants.java.*;
import org.openswing.swing.customvo.java.CustomValueObject;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.internationalization.server.ServerResourcesFactory;
import org.openswing.swing.internationalization.java.Resources;
import org.jallinone.items.java.VariantBarcodeVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Bean used to remove all variants barcodes and reinsert variants barcodes for the specified item.</p>
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
public class UpdateVariantBarcodesBean {


  public UpdateVariantBarcodesBean() {
  }


  /**
   * Business logic to execute.
   */
  public final Response updateBarcodes(
      Connection conn,
      VariantsMatrixVO matrixVO,
      Object[][] cells,
      UserSessionParameters userSessionPars,
      HttpServletRequest request,
      HttpServletResponse response,
      HttpSession userSession,
      ServletContext context
  ) {
    PreparedStatement pstmt = null;
    PreparedStatement pstmt2 = null;
    ResultSet rset2 = null;
    try {

      // fires the GenericEvent.CONNECTION_CREATED event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "updateVariantBarcodes",
        GenericEvent.CONNECTION_CREATED,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        new Object[]{matrixVO,cells},
        null
      ));

      String sql =
          "delete from ITM22_VARIANT_BARCODES where "+
          "COMPANY_CODE_SYS01=? AND "+
          "ITEM_CODE_ITM01=? ";
      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1,matrixVO.getItemPK().getCompanyCodeSys01ITM01());
      pstmt.setString(2,matrixVO.getItemPK().getItemCodeITM01());
      pstmt.execute();
      pstmt.close();

      sql =
          "insert into ITM22_VARIANT_BARCODES("+
          "COMPANY_CODE_SYS01,ITEM_CODE_ITM01,"+
          "VARIANT_TYPE_ITM06,VARIANT_TYPE_ITM07,VARIANT_TYPE_ITM08,VARIANT_TYPE_ITM09,VARIANT_TYPE_ITM10,"+
          "VARIANT_CODE_ITM11,VARIANT_CODE_ITM12,VARIANT_CODE_ITM13,VARIANT_CODE_ITM14,VARIANT_CODE_ITM15,"+
          "BAR_CODE) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
      pstmt = conn.prepareStatement(sql);

      pstmt2 = conn.prepareStatement("select * from ITM22_VARIANT_BARCODES where COMPANY_CODE_SYS01=? and BAR_CODE=?");

      VariantsMatrixRowVO rowVO = null;
      VariantsMatrixColumnVO colVO = null;
      Object[] row = null;
      ServerResourcesFactory factory = (ServerResourcesFactory)context.getAttribute(Controller.RESOURCES_FACTORY);
      Resources resources = factory.getResources(userSessionPars.getLanguageId());

      VariantBarcodeVO vo = new VariantBarcodeVO();
      vo.setCompanyCodeSys01ITM22(matrixVO.getItemPK().getCompanyCodeSys01ITM01());
      vo.setItemCodeItm01ITM22(matrixVO.getItemPK().getItemCodeITM01());

      boolean barCodeFound = false;
      for(int i=0;i<cells.length;i++) {
        rowVO = (VariantsMatrixRowVO)matrixVO.getRowDescriptors().get(i);
        row = cells[i];

        if (matrixVO.getColumnDescriptors().size()==0) {

          if (!containsVariant(matrixVO,"ITM11_VARIANTS_1")) {
            // e.g. color but not no size...
            vo.setVariantCodeItm11ITM22(ApplicationConsts.JOLLY);
            vo.setVariantTypeItm06ITM22(ApplicationConsts.JOLLY);
          }
          else {
            vo.setVariantCodeItm11ITM22(rowVO.getVariantCodeITM11());
            vo.setVariantTypeItm06ITM22(rowVO.getVariantTypeITM06());
          }
          if (!containsVariant(matrixVO,"ITM12_VARIANTS_2")) {
            vo.setVariantCodeItm12ITM22(ApplicationConsts.JOLLY);
            vo.setVariantTypeItm07ITM22(ApplicationConsts.JOLLY);
          }
          else {
            vo.setVariantCodeItm12ITM22(rowVO.getVariantCodeITM11());
            vo.setVariantTypeItm07ITM22(rowVO.getVariantTypeITM06());
          }
          if (!containsVariant(matrixVO,"ITM13_VARIANTS_3")) {
            vo.setVariantCodeItm13ITM22(ApplicationConsts.JOLLY);
            vo.setVariantTypeItm08ITM22(ApplicationConsts.JOLLY);
          }
          else {
            vo.setVariantCodeItm13ITM22(rowVO.getVariantCodeITM11());
            vo.setVariantTypeItm08ITM22(rowVO.getVariantTypeITM06());
          }
          if (!containsVariant(matrixVO,"ITM14_VARIANTS_4")) {
            vo.setVariantCodeItm14ITM22(ApplicationConsts.JOLLY);
            vo.setVariantTypeItm09ITM22(ApplicationConsts.JOLLY);
          }
          else {
            vo.setVariantCodeItm14ITM22(rowVO.getVariantCodeITM11());
            vo.setVariantTypeItm09ITM22(rowVO.getVariantTypeITM06());
          }
          if (!containsVariant(matrixVO,"ITM15_VARIANTS_5")) {
            vo.setVariantCodeItm15ITM22(ApplicationConsts.JOLLY);
            vo.setVariantTypeItm10ITM22(ApplicationConsts.JOLLY);
          }
          else {
            vo.setVariantCodeItm15ITM22(rowVO.getVariantCodeITM11());
            vo.setVariantTypeItm10ITM22(rowVO.getVariantTypeITM06());
          }


          if (row[0]==null) {
            // auto-create a barcode...
            new BarCodeGeneratorImpl().calculateBarCode(conn,vo);
            row[0] = vo.getBarCodeITM22();
          }

          // check for barcode uniqueness...
          if (row[0]!=null) {
            pstmt2.setString(1,matrixVO.getItemPK().getCompanyCodeSys01ITM01());
            pstmt2.setString(2,row[0].toString());
            rset2 = pstmt2.executeQuery();
            barCodeFound = false;
            if (rset2.next()) {
              barCodeFound = true;
            }
            rset2.close();
            if (barCodeFound) {
              conn.rollback();
              return new ErrorResponse(resources.getResource("barcode already assigned to another item"));
            }
          }

          pstmt.setString(1,matrixVO.getItemPK().getCompanyCodeSys01ITM01());
          pstmt.setString(2,matrixVO.getItemPK().getItemCodeITM01());

          pstmt.setString(3,vo.getVariantTypeItm06ITM22());
          pstmt.setString(4,vo.getVariantTypeItm07ITM22());
          pstmt.setString(5,vo.getVariantTypeItm08ITM22());
          pstmt.setString(6,vo.getVariantTypeItm09ITM22());
          pstmt.setString(7,vo.getVariantTypeItm10ITM22());

          pstmt.setString(8, vo.getVariantCodeItm11ITM22());
          pstmt.setString(9, vo.getVariantCodeItm12ITM22());
          pstmt.setString(10,vo.getVariantCodeItm13ITM22());
          pstmt.setString(11,vo.getVariantCodeItm14ITM22());
          pstmt.setString(12,vo.getVariantCodeItm15ITM22());

          pstmt.setString(13,(String)row[0]);

          pstmt.execute();
        }
        else {
          for(int j=0;j<row.length;j++) {
            colVO = (VariantsMatrixColumnVO)matrixVO.getColumnDescriptors().get(j);

            if (row[j]==null) {
              // auto-create a barcode...
              vo.setVariantTypeItm06ITM22(rowVO.getVariantTypeITM06()==null?ApplicationConsts.JOLLY:rowVO.getVariantTypeITM06());
              vo.setVariantTypeItm07ITM22(colVO.getVariantTypeITM07()==null?ApplicationConsts.JOLLY:colVO.getVariantTypeITM07());
              vo.setVariantTypeItm08ITM22(colVO.getVariantTypeITM08()==null?ApplicationConsts.JOLLY:colVO.getVariantTypeITM08());
              vo.setVariantTypeItm09ITM22(colVO.getVariantTypeITM09()==null?ApplicationConsts.JOLLY:colVO.getVariantTypeITM09());
              vo.setVariantTypeItm10ITM22(colVO.getVariantTypeITM10()==null?ApplicationConsts.JOLLY:colVO.getVariantTypeITM10());
              vo.setVariantCodeItm11ITM22(rowVO.getVariantCodeITM11()==null?ApplicationConsts.JOLLY:rowVO.getVariantCodeITM11());
              vo.setVariantCodeItm12ITM22(colVO.getVariantCodeITM12()==null?ApplicationConsts.JOLLY:colVO.getVariantCodeITM12());
              vo.setVariantCodeItm13ITM22(colVO.getVariantCodeITM13()==null?ApplicationConsts.JOLLY:colVO.getVariantCodeITM13());
              vo.setVariantCodeItm14ITM22(colVO.getVariantCodeITM14()==null?ApplicationConsts.JOLLY:colVO.getVariantCodeITM14());
              vo.setVariantCodeItm15ITM22(colVO.getVariantCodeITM15()==null?ApplicationConsts.JOLLY:colVO.getVariantCodeITM15());
              new BarCodeGeneratorImpl().calculateBarCode(conn,vo);
              row[j] = vo.getBarCodeITM22();
            }

            if (row[j]!=null) {

              // check for barcode uniqueness...
              if (row[j]!=null) {
                pstmt2.setString(1,matrixVO.getItemPK().getCompanyCodeSys01ITM01());
                pstmt2.setString(2,row[j].toString().toString());
                rset2 = pstmt2.executeQuery();
                barCodeFound = false;
                if (rset2.next()) {
                  barCodeFound = true;
                }
                rset2.close();
                if (barCodeFound) {
                  conn.rollback();
                  return new ErrorResponse(resources.getResource("barcode already assigned to another item"));
                }
              }

              pstmt.setString(1,matrixVO.getItemPK().getCompanyCodeSys01ITM01());
              pstmt.setString(2,matrixVO.getItemPK().getItemCodeITM01());

              pstmt.setString(3,rowVO.getVariantTypeITM06()==null?ApplicationConsts.JOLLY:rowVO.getVariantTypeITM06());
              pstmt.setString(4,colVO.getVariantTypeITM07()==null?ApplicationConsts.JOLLY:colVO.getVariantTypeITM07());
              pstmt.setString(5,colVO.getVariantTypeITM08()==null?ApplicationConsts.JOLLY:colVO.getVariantTypeITM08());
              pstmt.setString(6,colVO.getVariantTypeITM09()==null?ApplicationConsts.JOLLY:colVO.getVariantTypeITM09());
              pstmt.setString(7,colVO.getVariantTypeITM10()==null?ApplicationConsts.JOLLY:colVO.getVariantTypeITM10());

              pstmt.setString(8,rowVO.getVariantCodeITM11()==null?ApplicationConsts.JOLLY:rowVO.getVariantCodeITM11());
              pstmt.setString(9,colVO.getVariantCodeITM12()==null?ApplicationConsts.JOLLY:colVO.getVariantCodeITM12());
              pstmt.setString(10,colVO.getVariantCodeITM13()==null?ApplicationConsts.JOLLY:colVO.getVariantCodeITM13());
              pstmt.setString(11,colVO.getVariantCodeITM14()==null?ApplicationConsts.JOLLY:colVO.getVariantCodeITM14());
              pstmt.setString(12,colVO.getVariantCodeITM15()==null?ApplicationConsts.JOLLY:colVO.getVariantCodeITM15());

              pstmt.setString(13,(String)row[j]);

              pstmt.execute();
            }
          } // end inner for
        }
      } // end outer for

      VOListResponse res = new VOListResponse(new ArrayList(),false,0);
      return res;

    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"updateBarcodes","Error while updating barcodes",ex);
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        rset2.close();
      }
      catch (Exception ex1) {
      }
      try {
        pstmt2.close();
      }
      catch (Exception ex1) {
      }
      try {
        pstmt.close();
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
