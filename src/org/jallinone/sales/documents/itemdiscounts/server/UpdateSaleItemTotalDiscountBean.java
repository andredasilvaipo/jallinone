package org.jallinone.sales.documents.itemdiscounts.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.sales.documents.itemdiscounts.java.*;
import org.openswing.swing.server.QueryUtil;
import java.math.BigDecimal;
import org.jallinone.sales.documents.java.SaleDocRowPK;
import org.jallinone.sales.documents.java.SaleDocPK;
import org.jallinone.sales.documents.server.SaleDocTotalsAction;
import org.jallinone.sales.documents.server.LoadSaleDocAction;
import org.jallinone.sales.documents.java.DetailSaleDocVO;
import org.jallinone.sales.documents.server.LoadSaleDocRowAction;
import org.jallinone.sales.documents.java.DetailSaleDocRowVO;
import org.jallinone.sales.documents.server.SaleItemTotalDiscountAction;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Help class used to update total discount for the specified item row and the document totals.</p>
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
public class UpdateSaleItemTotalDiscountBean {

  private static SaleDocTotalsAction totalBean = new SaleDocTotalsAction();
  private static LoadSaleDocAction docBean = new LoadSaleDocAction();
  private static LoadSaleDocRowAction rowBean = new LoadSaleDocRowAction();
  private static SaleItemTotalDiscountAction itemDiscountBean = new SaleItemTotalDiscountAction();


  /**
   * Recalculate item row totals and document totals.
   * @return Boolean.TRUE if totals are correctly calculated
   */
  public static final Response updateTotals(SaleDocRowPK pk,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    Connection conn = null;
    PreparedStatement pstmt = null;
    try {
      conn = ConnectionManager.getConnection(context);

      // retrieve document header value object...
      SaleDocPK docPK = new SaleDocPK(
          pk.getCompanyCodeSys01DOC02(),
          pk.getDocTypeDOC02(),
          pk.getDocYearDOC02(),
          pk.getDocNumberDOC02()
      );
      Response docResponse = docBean.executeCommand(docPK,userSessionPars,request,response,userSession,context);
      if (docResponse.isError()) {
        conn.rollback();
        return docResponse;
      }
      DetailSaleDocVO vo = (DetailSaleDocVO)((VOResponse)docResponse).getVo();

      // recalculate item row totals...
      Response rowResponse = rowBean.executeCommand(pk,userSessionPars,request,response,userSession,context);
      if (rowResponse.isError()) {
        conn.rollback();
        return rowResponse;
      }
      DetailSaleDocRowVO rowVO = (DetailSaleDocRowVO)((VOResponse)rowResponse).getVo();
      Response itemTotDiscResponse = itemDiscountBean.executeCommand(rowVO,userSessionPars,request,response,userSession,context);
      if (itemTotDiscResponse.isError())
        return itemTotDiscResponse;
      rowVO = (DetailSaleDocRowVO)((VOResponse)itemTotDiscResponse).getVo();

      rowVO.setTotalDiscountDOC02( rowVO.getTotalDiscountDOC02().setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP) );

      // apply total discount to taxable income...
      rowVO.setTaxableIncomeDOC02(rowVO.getQtyDOC02().multiply(rowVO.getValueSal02DOC02()).setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP));
      rowVO.setTaxableIncomeDOC02(rowVO.getTaxableIncomeDOC02().subtract(rowVO.getTotalDiscountDOC02()).setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP));

      // calculate row vat...
      double vatPerc = rowVO.getValueReg01DOC02().doubleValue()*(1d-rowVO.getDeductibleReg01DOC02().doubleValue()/100d)/100;
      rowVO.setVatValueDOC02(rowVO.getTaxableIncomeDOC02().multiply(new BigDecimal(vatPerc)).setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP));

      // calculate row total...
      rowVO.setValueDOC02(rowVO.getTaxableIncomeDOC02().add(rowVO.getVatValueDOC02()));

      pstmt = conn.prepareStatement("update DOC02_SELLING_ITEMS set TAXABLE_INCOME=?,VAT_VALUE=?,VALUE=?,TOTAL_DISCOUNT=? where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? and ITEM_CODE_ITM01=?");
      pstmt.setBigDecimal(1,rowVO.getTaxableIncomeDOC02());
      pstmt.setBigDecimal(2,rowVO.getVatValueDOC02());
      pstmt.setBigDecimal(3,rowVO.getValueDOC02());
      pstmt.setBigDecimal(4,rowVO.getTotalDiscountDOC02());
      pstmt.setString(5,pk.getCompanyCodeSys01DOC02());
      pstmt.setString(6,pk.getDocTypeDOC02());
      pstmt.setBigDecimal(7,pk.getDocYearDOC02());
      pstmt.setBigDecimal(8,pk.getDocNumberDOC02());
      pstmt.setString(9,pk.getItemCodeItm01DOC02());
      pstmt.execute();
      conn.commit();

      // recalculate document totals...
      Response totalResponse = totalBean.executeCommand(vo,userSessionPars,request,response,userSession,context);
      if (totalResponse.isError())
        return totalResponse;

      pstmt = conn.prepareStatement("update DOC01_SELLING set TAXABLE_INCOME=?,TOTAL_VAT=?,TOTAL=? where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=?");
      pstmt.setBigDecimal(1,vo.getTaxableIncomeDOC01());
      pstmt.setBigDecimal(2,vo.getTotalVatDOC01());
      pstmt.setBigDecimal(3,vo.getTotalDOC01());
      pstmt.setString(4,pk.getCompanyCodeSys01DOC02());
      pstmt.setString(5,pk.getDocTypeDOC02());
      pstmt.setBigDecimal(6,pk.getDocYearDOC02());
      pstmt.setBigDecimal(7,pk.getDocNumberDOC02());
      pstmt.execute();

      conn.commit();
      return new VOResponse(Boolean.TRUE);
    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),"org.jallinone.sales.documents.itemdiscounts.server.UpdateSaleItemTotalDiscountBean","updateTotals","Error while updating document and row totals:\n"+ex.getMessage(), ex);
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



}
