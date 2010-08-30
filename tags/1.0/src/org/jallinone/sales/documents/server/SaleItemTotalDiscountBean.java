package org.jallinone.sales.documents.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.sales.documents.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import java.math.BigDecimal;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Helper class used to calculate sale item total discounts, based on DOC04 content and discounts (eventually) defined in DOC02.
 * It does not update the database, it only return the updated value object with the total discount setted.
 * NOTA: total discount decimals are NOT rounded to REG03 settings.</p>
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
public class SaleItemTotalDiscountBean{


  public SaleItemTotalDiscountBean() {
  }


 /**
  * Calculate item total discount.
  * No commit or rollback are executed; no connection is created or released.
  */
  public final Response getSaleItemTotalDiscount(Connection conn,DetailSaleDocRowVO vo,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
   PreparedStatement pstmt = null;
    try {

      // fires the GenericEvent.CONNECTION_CREATED event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "SaleItemTotalDiscountBean.getSaleItemTotalDiscount",
        GenericEvent.CONNECTION_CREATED,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        vo,
        null
      ));
      BigDecimal taxableIncomeDOC02 = vo.getTaxableIncomeDOC02();
      BigDecimal totalDiscountDOC02 = new BigDecimal(0);
      BigDecimal delta = null;

      // apply discount (eventually) defined at row level...
      if (vo.getDiscountValueDOC02()!=null) {
        taxableIncomeDOC02 = taxableIncomeDOC02.subtract(vo.getDiscountValueDOC02());
        totalDiscountDOC02 = totalDiscountDOC02.add(vo.getDiscountValueDOC02());
      }
      else if (vo.getDiscountPercDOC02()!=null) {
        delta = taxableIncomeDOC02.multiply(vo.getDiscountPercDOC02()).divide(new BigDecimal(100),BigDecimal.ROUND_HALF_UP);
        taxableIncomeDOC02 = taxableIncomeDOC02.subtract(delta);
        totalDiscountDOC02 = totalDiscountDOC02.add(delta);
      }

      // retrieve sale item discounts...
      pstmt = conn.prepareStatement(
        "select VALUE,PERC,MIN_QTY,MULTIPLE_QTY from DOC04_SELLING_ITEM_DISCOUNTS where "+
        "COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? and ITEM_CODE_ITM01=? and "+
        "START_DATE<=? and END_DATE>=? order by DISCOUNT_CODE_SAL03 and "+
        "VARIANT_TYPE_ITM06=? and VARIANT_CODE_ITM11=? and "+
        "VARIANT_TYPE_ITM07=? and VARIANT_CODE_ITM12=? and "+
        "VARIANT_TYPE_ITM08=? and VARIANT_CODE_ITM13=? and "+
        "VARIANT_TYPE_ITM09=? and VARIANT_CODE_ITM14=? and "+
        "VARIANT_TYPE_ITM10=? and VARIANT_CODE_ITM15=? "
      );
      pstmt.setString(1,vo.getCompanyCodeSys01DOC02());
      pstmt.setString(2,vo.getDocTypeDOC02());
      pstmt.setBigDecimal(3,vo.getDocYearDOC02());
      pstmt.setBigDecimal(4,vo.getDocNumberDOC02());
      pstmt.setString(5,vo.getItemCodeItm01DOC02());
      pstmt.setDate(6,new java.sql.Date(System.currentTimeMillis()));
      pstmt.setDate(7,new java.sql.Date(System.currentTimeMillis()));

      pstmt.setString(8,vo.getVariantTypeItm06DOC02());
      pstmt.setString(9,vo.getVariantCodeItm11DOC02());
      pstmt.setString(10,vo.getVariantTypeItm07DOC02());
      pstmt.setString(11,vo.getVariantCodeItm12DOC02());
      pstmt.setString(12,vo.getVariantTypeItm08DOC02());
      pstmt.setString(13,vo.getVariantCodeItm13DOC02());
      pstmt.setString(14,vo.getVariantTypeItm09DOC02());
      pstmt.setString(15,vo.getVariantCodeItm14DOC02());
      pstmt.setString(16,vo.getVariantTypeItm10DOC02());
      pstmt.setString(17,vo.getVariantCodeItm15DOC02());

      ResultSet rset = pstmt.executeQuery();
      BigDecimal valueDOC04 = null;
      BigDecimal percDOC04 = null;
      BigDecimal minQtyDOC04 = null;
      boolean multipleQtyDOC04;
      int div;
      double mult,rest;
      while(rset.next()) {
        valueDOC04 = rset.getBigDecimal(1);
        percDOC04 = rset.getBigDecimal(2);
        if (percDOC04!=null && percDOC04.toString().equals("33.33"))
          percDOC04 = new BigDecimal(33.33333);
        else if (percDOC04!=null && percDOC04.toString().equals("66.66"))
          percDOC04 = new BigDecimal(66.66666);
        minQtyDOC04 = rset.getBigDecimal(3);
        multipleQtyDOC04 = rset.getString(4).equals("Y");

        if (minQtyDOC04.doubleValue()>vo.getQtyDOC02().doubleValue())
          continue;

        if (multipleQtyDOC04) {
          // multiple qty enabled: this means that the discount is applied for each "DOC04_SELLING_ITEM_DISCOUNTS.MIN_QTY" items...
          // e.g. qtyDOC02=7, minQty=2.5 => mult=5, rest=2
          div = (int)(vo.getQtyDOC02().doubleValue()/minQtyDOC04.doubleValue());
          mult = ((double)div)*minQtyDOC04.doubleValue();
          rest = vo.getQtyDOC02().doubleValue()-mult;
          if (valueDOC04!=null) {
            delta = new BigDecimal(valueDOC04.doubleValue()*mult/vo.getQtyDOC02().doubleValue());
          }
          else {
            delta = new BigDecimal(
              taxableIncomeDOC02.doubleValue()*percDOC04.doubleValue()/100*mult/vo.getQtyDOC02().doubleValue()
            );
          }
        }
        else {
          // multiple qty disabled: this means that the discount is applied for all "vo.getQtyDOC02()" items...
          if (valueDOC04!=null) {
            delta = valueDOC04;
          }
          else {
            delta = taxableIncomeDOC02.multiply(percDOC04).divide(new BigDecimal(100),BigDecimal.ROUND_HALF_UP);
          }
        }
//        if (valueDOC04!=null) {
//          delta = valueDOC04;
//        }
//        else {
//          delta = taxableIncomeDOC02.multiply(percDOC04).divide(new BigDecimal(100),BigDecimal.ROUND_HALF_UP);
//        }
        taxableIncomeDOC02 = taxableIncomeDOC02.subtract(delta);
        totalDiscountDOC02 = totalDiscountDOC02.add(delta);
      }
      rset.close();

      vo.setTotalDiscountDOC02( totalDiscountDOC02 );

      Response answer = new VOResponse(vo);

      // fires the GenericEvent.BEFORE_COMMIT event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "SaleItemTotalDiscountBean.getSaleItemTotalDiscount",
        GenericEvent.BEFORE_COMMIT,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        vo,
        answer
      ));

      return answer;
    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"getSaleItemTotalDiscount","Error on calculating sale item total discount",ex);
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        pstmt.close();
      }
      catch (Exception ex2) {
      }
    }
  }


}
