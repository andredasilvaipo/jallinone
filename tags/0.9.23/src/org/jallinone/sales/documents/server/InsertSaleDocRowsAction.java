package org.jallinone.sales.documents.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.sales.documents.java.DetailSaleDocRowVO;
import org.jallinone.system.server.JAIOUserSessionParameters;
import java.math.BigDecimal;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.system.progressives.server.ProgressiveUtils;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.system.java.ApplicationParametersVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.message.send.java.LookupValidationParams;
import org.jallinone.sales.documents.java.SaleDocPK;
import org.jallinone.sales.documents.java.DetailSaleDocVO;
import org.jallinone.sales.documents.java.SaleDocRowPK;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;
import org.jallinone.variants.java.VariantsMatrixVO;
import org.jallinone.variants.java.VariantsMatrixColumnVO;
import org.jallinone.variants.java.VariantsMatrixRowVO;
import org.jallinone.variants.java.VariantNameVO;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to insert a new sale document row in DOC02 table.</p>
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
public class InsertSaleDocRowsAction implements Action {

  private InsertSaleDocRowBean bean = new InsertSaleDocRowBean();
  private UpdateTaxableIncomesBean totals = new UpdateTaxableIncomesBean();
  private InsertSaleSerialNumbersBean serialNumBean = new InsertSaleSerialNumbersBean();
  private LoadSaleDocRowBean load = new LoadSaleDocRowBean();


  public InsertSaleDocRowsAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "insertSaleDocRows";
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
      DetailSaleDocRowVO voTemplate = (DetailSaleDocRowVO)pars[0];
      VariantsMatrixVO matrixVO = (VariantsMatrixVO)pars[1];
      Object[][] cells = (Object[][])pars[2];
      BigDecimal currencyDecimals = (BigDecimal)pars[3];


      VariantsMatrixColumnVO colVO = null;
      VariantsMatrixRowVO rowVO = null;
      DetailSaleDocRowVO vo = null;
      Response res = null;
      for(int i=0;i<cells.length;i++) {
        rowVO = (VariantsMatrixRowVO)matrixVO.getRowDescriptors().get(i);

        if (matrixVO.getColumnDescriptors().size()==0) {

          if (cells[i][0]!=null) {
            vo = (DetailSaleDocRowVO)voTemplate.clone();

            vo.setQtyDOC02((BigDecimal)cells[i][0]);

            if (!containsVariant(matrixVO,"ITM11_VARIANTS_1")) {
              // e.g. color but not no size...
              vo.setVariantCodeItm11DOC02(ApplicationConsts.JOLLY);
              vo.setVariantTypeItm06DOC02(ApplicationConsts.JOLLY);
            }
            else {
              vo.setVariantCodeItm11DOC02(rowVO.getVariantCodeITM11());
              vo.setVariantTypeItm06DOC02(rowVO.getVariantTypeITM06());
            }
            if (!containsVariant(matrixVO,"ITM12_VARIANTS_2")) {
              vo.setVariantCodeItm12DOC02(ApplicationConsts.JOLLY);
              vo.setVariantTypeItm07DOC02(ApplicationConsts.JOLLY);
            }
            else {
              vo.setVariantCodeItm12DOC02(rowVO.getVariantCodeITM11());
              vo.setVariantTypeItm07DOC02(rowVO.getVariantTypeITM06());
            }
            if (!containsVariant(matrixVO,"ITM13_VARIANTS_3")) {
              vo.setVariantCodeItm13DOC02(ApplicationConsts.JOLLY);
              vo.setVariantTypeItm08DOC02(ApplicationConsts.JOLLY);
            }
            else {
              vo.setVariantCodeItm13DOC02(rowVO.getVariantCodeITM11());
              vo.setVariantTypeItm08DOC02(rowVO.getVariantTypeITM06());
            }
            if (!containsVariant(matrixVO,"ITM14_VARIANTS_4")) {
              vo.setVariantCodeItm14DOC02(ApplicationConsts.JOLLY);
              vo.setVariantTypeItm09DOC02(ApplicationConsts.JOLLY);
            }
            else {
              vo.setVariantCodeItm14DOC02(rowVO.getVariantCodeITM11());
              vo.setVariantTypeItm09DOC02(rowVO.getVariantTypeITM06());
            }
            if (!containsVariant(matrixVO,"ITM15_VARIANTS_5")) {
              vo.setVariantCodeItm15DOC02(ApplicationConsts.JOLLY);
              vo.setVariantTypeItm10DOC02(ApplicationConsts.JOLLY);
            }
            else {
              vo.setVariantCodeItm15DOC02(rowVO.getVariantCodeITM11());
              vo.setVariantTypeItm10DOC02(rowVO.getVariantTypeITM06());
            }


            //PurchaseUtils.updateTotals(vo,currencyDecimals.intValue());
/*
            vo.setVariantCodeItm11DOC02(rowVO.getVariantCodeITM11());
            vo.setVariantTypeItm06DOC02(rowVO.getVariantTypeITM06());

            vo.setVariantCodeItm12DOC02(ApplicationConsts.JOLLY);
            vo.setVariantCodeItm13DOC02(ApplicationConsts.JOLLY);
            vo.setVariantCodeItm14DOC02(ApplicationConsts.JOLLY);
            vo.setVariantCodeItm15DOC02(ApplicationConsts.JOLLY);

            vo.setVariantTypeItm07DOC02(ApplicationConsts.JOLLY);
            vo.setVariantTypeItm08DOC02(ApplicationConsts.JOLLY);
            vo.setVariantTypeItm09DOC02(ApplicationConsts.JOLLY);
            vo.setVariantTypeItm10DOC02(ApplicationConsts.JOLLY);
*/
            vo.setOutQtyDOC02(new BigDecimal(0));
            if (vo.getInvoiceQtyDOC02()==null)
              vo.setInvoiceQtyDOC02(new BigDecimal(0));


            res = bean.insertSaleItem(conn,vo,userSessionPars,request,response,userSession,context);
            if (res.isError()) {
              conn.rollback();
              return res;
            }

            // insert serial numbers...
            if (vo.getSerialNumbers().size()>0) {
              res = serialNumBean.reinsertSaleSerialNumbers(vo,conn,userSessionPars,request,response,userSession,context);
              if (res.isError()) {
                conn.rollback();
                return res;
              }
            }
          }

        }
        else
          for(int k=0;k<matrixVO.getColumnDescriptors().size();k++) {

            colVO = (VariantsMatrixColumnVO)matrixVO.getColumnDescriptors().get(k);
            if (cells[i][k]!=null) {
              vo = (DetailSaleDocRowVO)voTemplate.clone();

              vo.setQtyDOC02((BigDecimal)cells[i][k]);

              //PurchaseUtils.updateTotals(vo,currencyDecimals.intValue());

              vo.setVariantCodeItm11DOC02(rowVO.getVariantCodeITM11());
              vo.setVariantTypeItm06DOC02(rowVO.getVariantTypeITM06());

              vo.setVariantCodeItm12DOC02(colVO.getVariantCodeITM12()==null?ApplicationConsts.JOLLY:colVO.getVariantCodeITM12());
              vo.setVariantCodeItm13DOC02(colVO.getVariantCodeITM13()==null?ApplicationConsts.JOLLY:colVO.getVariantCodeITM13());
              vo.setVariantCodeItm14DOC02(colVO.getVariantCodeITM14()==null?ApplicationConsts.JOLLY:colVO.getVariantCodeITM14());
              vo.setVariantCodeItm15DOC02(colVO.getVariantCodeITM15()==null?ApplicationConsts.JOLLY:colVO.getVariantCodeITM15());

              vo.setVariantTypeItm07DOC02(colVO.getVariantTypeITM07()==null?ApplicationConsts.JOLLY:colVO.getVariantTypeITM07());
              vo.setVariantTypeItm08DOC02(colVO.getVariantTypeITM08()==null?ApplicationConsts.JOLLY:colVO.getVariantTypeITM08());
              vo.setVariantTypeItm09DOC02(colVO.getVariantTypeITM09()==null?ApplicationConsts.JOLLY:colVO.getVariantTypeITM09());
              vo.setVariantTypeItm10DOC02(colVO.getVariantTypeITM10()==null?ApplicationConsts.JOLLY:colVO.getVariantTypeITM10());

              vo.setOutQtyDOC02(new BigDecimal(0));
              if (vo.getInvoiceQtyDOC02()==null)
                vo.setInvoiceQtyDOC02(new BigDecimal(0));


              res = bean.insertSaleItem(conn,vo,userSessionPars,request,response,userSession,context);
              if (res.isError()) {
                conn.rollback();
                return res;
              }

              // insert serial numbers...
              if (vo.getSerialNumbers().size()>0) {
                res = serialNumBean.reinsertSaleSerialNumbers(vo,conn,userSessionPars,request,response,userSession,context);
                if (res.isError()) {
                  conn.rollback();
                  return res;
                }
              }


            } // end if on not null
          } // end inner for
      } // end outer for

      // update doc state...
      SaleDocPK pk = new SaleDocPK(
          voTemplate.getCompanyCodeSys01DOC02(),
          voTemplate.getDocTypeDOC02(),
          voTemplate.getDocYearDOC02(),
          voTemplate.getDocNumberDOC02()
      );
      pstmt = conn.prepareStatement("update DOC01_SELLING set DOC_STATE=? where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=?");
      pstmt.setString(1,ApplicationConsts.HEADER_BLOCKED);
      pstmt.setString(2,pk.getCompanyCodeSys01DOC01());
      pstmt.setString(3,pk.getDocTypeDOC01());
      pstmt.setBigDecimal(4,pk.getDocYearDOC01());
      pstmt.setBigDecimal(5,pk.getDocNumberDOC01());
      pstmt.execute();

      // recalculate totals...
      res = totals.updateTaxableIncomes(
        conn,
        pk,
        userSessionPars,
        request,
        response,
        userSession,
        context
      );
      if (res.isError()) {
        conn.rollback();
        return res;
      }
/*
      // reload v.o. after updating taxable incomes...
      res = load.loadSaleDocRow(
        conn,
        new SaleDocRowPK(
          pk.getCompanyCodeSys01DOC01(),
          pk.getDocTypeDOC01(),
          pk.getDocYearDOC01(),
          pk.getDocNumberDOC01(),
          voTemplate.getItemCodeItm01DOC02(),
          vo.getVariantTypeItm06DOC02(),
          vo.getVariantCodeItm11DOC02(),
          vo.getVariantTypeItm07DOC02(),
          vo.getVariantCodeItm12DOC02(),
          vo.getVariantTypeItm08DOC02(),
          vo.getVariantCodeItm13DOC02(),
          vo.getVariantTypeItm09DOC02(),
          vo.getVariantCodeItm14DOC02(),
          vo.getVariantTypeItm10DOC02(),
          vo.getVariantCodeItm15DOC02()
        ),
        userSessionPars,
        request,
        response,
        userSession,
        context
      );
      if (res.isError()) {
        conn.rollback();
        return res;
      }
      vo = (DetailSaleDocRowVO)((VOResponse)res).getVo();
*/


      Response answer = new VOResponse(voTemplate);


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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while inserting a new sale document item row",ex);
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
