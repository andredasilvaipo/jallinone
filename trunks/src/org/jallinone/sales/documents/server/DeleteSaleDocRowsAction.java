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
 * <p>Description: Action class used to (phisically) delete existing sales order rows.</p>
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
public class DeleteSaleDocRowsAction implements Action {

  private UpdateTaxableIncomesBean totals = new UpdateTaxableIncomesBean();


  public DeleteSaleDocRowsAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "deleteSaleDocRows";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    Connection conn = null;
    PreparedStatement pstmt1 = null;
    PreparedStatement pstmt2 = null;
    PreparedStatement pstmt3 = null;
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

      java.util.List list = (ArrayList)inputPar;
      SaleDocRowPK rowPK = null;

      pstmt1 = conn.prepareStatement(
          "delete from DOC18_SELLING_SERIAL_NUMBERS where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? and ITEM_CODE_ITM01=? and "+
          "VARIANT_TYPE_ITM06=? and VARIANT_CODE_ITM11=? and "+
          "VARIANT_TYPE_ITM07=? and VARIANT_CODE_ITM12=? and "+
          "VARIANT_TYPE_ITM08=? and VARIANT_CODE_ITM13=? and "+
          "VARIANT_TYPE_ITM09=? and VARIANT_CODE_ITM14=? and "+
          "VARIANT_TYPE_ITM10=? and VARIANT_CODE_ITM15=? "

      );
      pstmt2 = conn.prepareStatement(
          "delete from DOC04_SELLING_ITEM_DISCOUNTS where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? and ITEM_CODE_ITM01=? and "+
          "VARIANT_TYPE_ITM06=? and VARIANT_CODE_ITM11=? and "+
          "VARIANT_TYPE_ITM07=? and VARIANT_CODE_ITM12=? and "+
          "VARIANT_TYPE_ITM08=? and VARIANT_CODE_ITM13=? and "+
          "VARIANT_TYPE_ITM09=? and VARIANT_CODE_ITM14=? and "+
          "VARIANT_TYPE_ITM10=? and VARIANT_CODE_ITM15=? "

      );
      pstmt3 = conn.prepareStatement(
          "delete from DOC02_SELLING_ITEMS where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? and ITEM_CODE_ITM01=? and "+
          "VARIANT_TYPE_ITM06=? and VARIANT_CODE_ITM11=? and "+
          "VARIANT_TYPE_ITM07=? and VARIANT_CODE_ITM12=? and "+
          "VARIANT_TYPE_ITM08=? and VARIANT_CODE_ITM13=? and "+
          "VARIANT_TYPE_ITM09=? and VARIANT_CODE_ITM14=? and "+
          "VARIANT_TYPE_ITM10=? and VARIANT_CODE_ITM15=? "

      );

      for(int i=0;i<list.size();i++) {
        rowPK = (SaleDocRowPK)list.get(i);

        // phisically delete records in DOC18...
        pstmt1.setString(1,rowPK.getCompanyCodeSys01DOC02());
        pstmt1.setString(2,rowPK.getDocTypeDOC02());
        pstmt1.setBigDecimal(3,rowPK.getDocYearDOC02());
        pstmt1.setBigDecimal(4,rowPK.getDocNumberDOC02());
        pstmt1.setString(5,rowPK.getItemCodeItm01DOC02());

        pstmt1.setString(6,rowPK.getVariantTypeItm06DOC02());
        pstmt1.setString(7,rowPK.getVariantCodeItm11DOC02());
        pstmt1.setString(8,rowPK.getVariantTypeItm07DOC02());
        pstmt1.setString(9,rowPK.getVariantCodeItm12DOC02());
        pstmt1.setString(10,rowPK.getVariantTypeItm08DOC02());
        pstmt1.setString(11,rowPK.getVariantCodeItm13DOC02());
        pstmt1.setString(12,rowPK.getVariantTypeItm09DOC02());
        pstmt1.setString(13,rowPK.getVariantCodeItm14DOC02());
        pstmt1.setString(14,rowPK.getVariantTypeItm10DOC02());
        pstmt1.setString(15,rowPK.getVariantCodeItm15DOC02());

        pstmt1.execute();

        // phisically delete records in DOC04...
        pstmt2.setString(1,rowPK.getCompanyCodeSys01DOC02());
        pstmt2.setString(2,rowPK.getDocTypeDOC02());
        pstmt2.setBigDecimal(3,rowPK.getDocYearDOC02());
        pstmt2.setBigDecimal(4,rowPK.getDocNumberDOC02());
        pstmt2.setString(5,rowPK.getItemCodeItm01DOC02());

        pstmt1.setString(6,rowPK.getVariantTypeItm06DOC02());
        pstmt1.setString(7,rowPK.getVariantCodeItm11DOC02());
        pstmt1.setString(8,rowPK.getVariantTypeItm07DOC02());
        pstmt1.setString(9,rowPK.getVariantCodeItm12DOC02());
        pstmt1.setString(10,rowPK.getVariantTypeItm08DOC02());
        pstmt1.setString(11,rowPK.getVariantCodeItm13DOC02());
        pstmt1.setString(12,rowPK.getVariantTypeItm09DOC02());
        pstmt1.setString(13,rowPK.getVariantCodeItm14DOC02());
        pstmt1.setString(14,rowPK.getVariantTypeItm10DOC02());
        pstmt1.setString(15,rowPK.getVariantCodeItm15DOC02());

        pstmt2.execute();

        // phisically delete the record in DOC02...
        pstmt3.setString(1,rowPK.getCompanyCodeSys01DOC02());
        pstmt3.setString(2,rowPK.getDocTypeDOC02());
        pstmt3.setBigDecimal(3,rowPK.getDocYearDOC02());
        pstmt3.setBigDecimal(4,rowPK.getDocNumberDOC02());
        pstmt3.setString(5,rowPK.getItemCodeItm01DOC02());

        pstmt1.setString(6,rowPK.getVariantTypeItm06DOC02());
        pstmt1.setString(7,rowPK.getVariantCodeItm11DOC02());
        pstmt1.setString(8,rowPK.getVariantTypeItm07DOC02());
        pstmt1.setString(9,rowPK.getVariantCodeItm12DOC02());
        pstmt1.setString(10,rowPK.getVariantTypeItm08DOC02());
        pstmt1.setString(11,rowPK.getVariantCodeItm13DOC02());
        pstmt1.setString(12,rowPK.getVariantTypeItm09DOC02());
        pstmt1.setString(13,rowPK.getVariantCodeItm14DOC02());
        pstmt1.setString(14,rowPK.getVariantTypeItm10DOC02());
        pstmt1.setString(15,rowPK.getVariantCodeItm15DOC02());

        pstmt3.execute();
      }

      // recalculate totals...
      SaleDocPK pk = new SaleDocPK(
          rowPK.getCompanyCodeSys01DOC02(),
          rowPK.getDocTypeDOC02(),
          rowPK.getDocYearDOC02(),
          rowPK.getDocNumberDOC02()
      );
      Response res = totals.updateTaxableIncomes(
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

      Response answer = new VOResponse(new Boolean(true));


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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while deleting existing sale order rows",ex);
      try {
        conn.rollback();
      }
      catch (Exception ex3) {
      }
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        pstmt1.close();
      }
      catch (Exception ex2) {
      }
      try {
        pstmt2.close();
      }
      catch (Exception ex2) {
      }
      try {
        pstmt3.close();
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
