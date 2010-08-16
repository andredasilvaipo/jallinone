package org.jallinone.sales.documents.itemdiscounts.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.sales.documents.itemdiscounts.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.sales.documents.java.SaleDocRowPK;
import org.jallinone.sales.discounts.server.LoadItemDiscountsAction;
import org.jallinone.sales.discounts.server.DiscountBean;
import org.jallinone.sales.documents.java.DetailSaleDocRowVO;
import org.jallinone.sales.discounts.java.DiscountVO;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch item discounts that could be applied to a sale document row from SAL03 table.</p>
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
public class LoadSaleItemDiscountsAction implements Action {


  public LoadSaleItemDiscountsAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadSaleItemDiscounts";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();

    Statement stmt = null;
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

      GridParams gridParams = (GridParams)inputPar;
      DetailSaleDocRowVO rowVO = (DetailSaleDocRowVO)gridParams.getOtherGridParams().get(ApplicationConsts.SALE_DOC_ROW_VO);

      // retrieve item discount codes...
      stmt = conn.createStatement();
      ResultSet rset = stmt.executeQuery(
          "select DISCOUNT_CODE_SAL03 from SAL04_ITEM_DISCOUNTS,SAL03_DISCOUNTS where "+
          "SAL04_ITEM_DISCOUNTS.COMPANY_CODE_SYS01='"+rowVO.getCompanyCodeSys01DOC02()+"' and ITEM_CODE_ITM01='"+rowVO.getItemCodeItm01DOC02()+"' and "+
          "SAL04_ITEM_DISCOUNTS.COMPANY_CODE_SYS01=SAL03_DISCOUNTS.COMPANY_CODE_SYS01 and "+
          "SAL04_ITEM_DISCOUNTS.DISCOUNT_CODE_SAL03=SAL03_DISCOUNTS.DISCOUNT_CODE and "+
          "SAL03_DISCOUNTS.CURRENCY_CODE_REG03='"+rowVO.getCurrencyCodeReg03DOC01()+"' and "+
          "SAL03_DISCOUNTS.MIN_QTY<="+rowVO.getQtyDOC02()

      );
      ArrayList discountCodes = new ArrayList();
      while(rset.next()) {
        discountCodes.add( rset.getString(1) );
      }
      rset.close();

      // retrieve item hierarchy discount codes...
      rset = stmt.executeQuery(
          "select DISCOUNT_CODE_SAL03 from SAL05_ITEM_HIERAR_DISCOUNTS,SAL03_DISCOUNTS where "+
          "SAL05_ITEM_HIERAR_DISCOUNTS.COMPANY_CODE_SYS01='"+rowVO.getCompanyCodeSys01DOC02()+"' and PROGRESSIVE_HIE01="+rowVO.getProgressiveHie01ITM01()+" and "+
          "SAL05_ITEM_HIERAR_DISCOUNTS.COMPANY_CODE_SYS01=SAL03_DISCOUNTS.COMPANY_CODE_SYS01 and "+
          "SAL05_ITEM_HIERAR_DISCOUNTS.DISCOUNT_CODE_SAL03=SAL03_DISCOUNTS.DISCOUNT_CODE and "+
          "SAL03_DISCOUNTS.CURRENCY_CODE_REG03='"+rowVO.getCurrencyCodeReg03DOC01()+"' and "+
          "SAL03_DISCOUNTS.MIN_QTY<="+rowVO.getQtyDOC02()
      );
      while(rset.next()) {
        discountCodes.add( rset.getString(1) );
      }
      rset.close();

      Response res = DiscountBean.getDiscountsList(
          conn,
          rowVO.getCompanyCodeSys01DOC02(),
          discountCodes,
          serverLanguageId,
          gridParams,
          userSessionPars,
          context,
          DiscountVO.class
      );

      if (!res.isError()) {
        java.util.List list = ((VOListResponse)res).getRows();
        DiscountVO vo = null;
        int i=0;
        while(i<list.size()) {
          vo = (DiscountVO)list.get(i);
          if (vo.getStartDateSAL03().getTime()>System.currentTimeMillis() ||
              vo.getEndDateSAL03().getTime()<System.currentTimeMillis())
            list.remove(i);
          else
            i++;
        }
        return new VOListResponse(list,false,list.size());
      }

      Response answer = res;

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


      return answer;
    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching item discounts list",ex);
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        stmt.close();
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
