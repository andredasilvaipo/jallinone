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
import org.jallinone.variants.java.VariantsMatrixUtils;


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
            VariantsMatrixUtils.setVariantTypesAndCodes(vo,"DOC02",matrixVO,rowVO,null);
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
              VariantsMatrixUtils.setVariantTypesAndCodes(vo,"DOC02",matrixVO,rowVO,colVO);
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
