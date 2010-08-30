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
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;



/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Helper class used to delete and re-insert serial numbers in DOC18 table.</p>
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
public class InsertSaleSerialNumbersBean {

  public InsertSaleSerialNumbersBean() {
  }


  /**
   * Delete and re-insert serial numbers in DOC18 table.
   * It does not comit or rollback the connection.
   */
  public final Response reinsertSaleSerialNumbers(DetailSaleDocRowVO vo,Connection conn,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    PreparedStatement pstmt = null;
    try {

      // fires the GenericEvent.CONNECTION_CREATED event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "InsertSaleSerialNumbersBean.reinsertSaleSerialNumbers",
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
      ArrayList serialNums = vo.getSerialNumbers();

      // delete previous serial numbers from DOC18...
      pstmt = conn.prepareStatement(
       "delete from DOC18_SELLING_SERIAL_NUMBERS where "+
       "COMPANY_CODE_SYS01=? and "+
       "DOC_TYPE=? and "+
       "DOC_YEAR=? and "+
       "DOC_NUMBER=? and "+
       "ITEM_CODE_ITM01=? and "+
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

      pstmt.setString(6,vo.getVariantTypeItm06DOC02());
      pstmt.setString(7,vo.getVariantCodeItm11DOC02());
      pstmt.setString(8,vo.getVariantTypeItm07DOC02());
      pstmt.setString(9,vo.getVariantCodeItm12DOC02());
      pstmt.setString(10,vo.getVariantTypeItm08DOC02());
      pstmt.setString(11,vo.getVariantCodeItm13DOC02());
      pstmt.setString(12,vo.getVariantTypeItm09DOC02());
      pstmt.setString(13,vo.getVariantCodeItm14DOC02());
      pstmt.setString(14,vo.getVariantTypeItm10DOC02());
      pstmt.setString(15,vo.getVariantCodeItm15DOC02());

      pstmt.execute();
      pstmt.close();

      pstmt = conn.prepareStatement(
        "insert into DOC18_SELLING_SERIAL_NUMBERS("+
        "COMPANY_CODE_SYS01,DOC_TYPE,DOC_YEAR,DOC_NUMBER,ITEM_CODE_ITM01,SERIAL_NUMBER,"+
        "VARIANT_TYPE_ITM06,VARIANT_CODE_ITM11,"+
        "VARIANT_TYPE_ITM07,VARIANT_CODE_ITM12,"+
        "VARIANT_TYPE_ITM08,VARIANT_CODE_ITM13,"+
        "VARIANT_TYPE_ITM09,VARIANT_CODE_ITM14,"+
        "VARIANT_TYPE_ITM10,VARIANT_CODE_ITM15"+
        ") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
      );

      for(int i=0;i<serialNums.size();i++) {
         pstmt.setString(1,vo.getCompanyCodeSys01DOC02());
         pstmt.setString(2,vo.getDocTypeDOC02());
         pstmt.setBigDecimal(3,vo.getDocYearDOC02());
         pstmt.setBigDecimal(4,vo.getDocNumberDOC02());
         pstmt.setString(5,vo.getItemCodeItm01DOC02());
         pstmt.setString(6,(String)serialNums.get(i));

         pstmt.setString(7,vo.getVariantTypeItm06DOC02());
         pstmt.setString(8,vo.getVariantCodeItm11DOC02());
         pstmt.setString(9,vo.getVariantTypeItm07DOC02());
         pstmt.setString(10,vo.getVariantCodeItm12DOC02());
         pstmt.setString(11,vo.getVariantTypeItm08DOC02());
         pstmt.setString(12,vo.getVariantCodeItm13DOC02());
         pstmt.setString(13,vo.getVariantTypeItm09DOC02());
         pstmt.setString(14,vo.getVariantCodeItm14DOC02());
         pstmt.setString(15,vo.getVariantTypeItm10DOC02());
         pstmt.setString(16,vo.getVariantCodeItm15DOC02());

         pstmt.execute();
      }

      Response answer = new VOResponse(Boolean.TRUE);

      // fires the GenericEvent.BEFORE_COMMIT event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        "InsertSaleSerialNumbersBean.reinsertSaleSerialNumbers",
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"reinsertSaleSerialNumbers","Error while creating serial numbers in DOC18 table.",ex);
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
