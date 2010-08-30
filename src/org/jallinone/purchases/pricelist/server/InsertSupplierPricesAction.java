package org.jallinone.purchases.pricelist.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.purchases.pricelist.java.*;
import java.math.BigDecimal;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.purchases.items.server.InsertSupplierItemsBean;
import org.jallinone.purchases.items.java.SupplierItemVO;
import org.jallinone.items.server.LoadItemAction;
import org.jallinone.items.java.ItemPK;
import org.jallinone.items.java.DetailItemVO;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to insert one or more supplier item prices in PUR04 table.</p>
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
public class InsertSupplierPricesAction implements Action {

  private InsertSupplierItemsBean insItem = new InsertSupplierItemsBean();
  private LoadItemAction loadItem = new LoadItemAction();


  public InsertSupplierPricesAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "insertSupplierPrices";
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
    ResultSet rset = null;
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

      pstmt = conn.prepareStatement(
        "select ITEM_CODE_ITM01 from PUR02_SUPPLIER_ITEMS where "+
        "COMPANY_CODE_SYS01=? and PROGRESSIVE_REG04=? and ITEM_CODE_ITM01=?"
      );

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01PUR04","COMPANY_CODE_SYS01");
      attribute2dbField.put("pricelistCodePur03PUR04","PRICELIST_CODE_PUR03");
      attribute2dbField.put("progressiveReg04PUR04","PROGRESSIVE_REG04");
      attribute2dbField.put("itemCodeItm01PUR04","ITEM_CODE_ITM01");
      attribute2dbField.put("valuePUR04","VALUE");
      attribute2dbField.put("startDatePUR04","START_DATE");
      attribute2dbField.put("endDatePUR04","END_DATE");

      // insert into PUR04...
      java.util.List list = (ArrayList)inputPar;
      SupplierPriceVO vo = null;
      Response res = null;
      ArrayList items = new ArrayList();
      SupplierItemVO itemVO = new SupplierItemVO();
      items.add(itemVO);
      DetailItemVO detailItemVO = null;
      for(int i=0;i<list.size();i++) {
        vo = (SupplierPriceVO)list.get(i);

        // check if the item is already defined in a supplier-pricelist, otherwise it will be added...
        pstmt.setString(1,vo.getCompanyCodeSys01PUR04());
        pstmt.setBigDecimal(2,vo.getProgressiveReg04PUR04());
        pstmt.setString(3,vo.getItemCodeItm01PUR04());
        rset = pstmt.executeQuery();
        if (!rset.next()) {
          // item not found: it will be added...
          res = loadItem.executeCommand(new ItemPK(vo.getCompanyCodeSys01PUR04(),vo.getItemCodeItm01PUR04()),userSessionPars,request,response,userSession,context);
          if (res.isError()) {
            conn.rollback();
            return res;
          }
          detailItemVO = (DetailItemVO)((VOResponse)res).getVo();
          itemVO.setCompanyCodeSys01PUR02(vo.getCompanyCodeSys01PUR04());
          itemVO.setDecimalsREG02(detailItemVO.getMinSellingQtyDecimalsREG02());
          itemVO.setItemCodeItm01PUR02(vo.getItemCodeItm01PUR04());
          itemVO.setMinPurchaseQtyPUR02(detailItemVO.getMinSellingQtyITM01());
          itemVO.setMultipleQtyPUR02(itemVO.getMinPurchaseQtyPUR02());
          itemVO.setProgressiveHie01PUR02(detailItemVO.getProgressiveHie01ITM01());
          itemVO.setProgressiveHie02PUR02(detailItemVO.getProgressiveHie02ITM01());
          itemVO.setProgressiveReg04PUR02(vo.getProgressiveReg04PUR04());
          itemVO.setSupplierItemCodePUR02(vo.getItemCodeItm01PUR04());
          itemVO.setUmCodeReg02PUR02(detailItemVO.getMinSellingQtyUmCodeReg02ITM01());

          res = insItem.insertSupplierItems(conn,items,userSessionPars,request,response,userSession,context);
          if (res.isError()) {
            conn.rollback();
            return res;
          }
        }
        rset.close();

        res = QueryUtil.insertTable(
            conn,
            userSessionPars,
            vo,
            "PUR04_SUPPLIER_PRICES",
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

      Response answer = new VOListResponse(list,false,list.size());

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
                   "executeCommand", "Error while inserting a new supplier item price", ex);
      try {
        conn.rollback();
      }
      catch (Exception ex3) {
      }
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        if (rset != null) {
          rset.close();
        }
      }
      catch (Exception ex4) {
      }
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

