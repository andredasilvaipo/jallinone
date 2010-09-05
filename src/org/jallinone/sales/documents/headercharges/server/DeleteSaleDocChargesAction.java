package org.jallinone.sales.documents.headercharges.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.sales.documents.headercharges.java.*;
import org.jallinone.sales.documents.server.UpdateSaleDocRowAction;
import org.jallinone.sales.documents.java.DetailSaleDocVO;
import org.jallinone.sales.documents.java.SaleDocPK;
import org.jallinone.sales.documents.server.UpdateTaxableIncomesBean;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to (phisically) delete existing charges for a sale document.</p>
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
public class DeleteSaleDocChargesAction implements Action {

  private UpdateTaxableIncomesBean totals = new UpdateTaxableIncomesBean();


  public DeleteSaleDocChargesAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "deleteSaleDocCharges";
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

      java.util.List list =  (ArrayList)inputPar;
      SaleDocChargeVO vo = null;

      pstmt = conn.prepareStatement(
          "delete from DOC03_SELLING_CHARGES where "+
          "DOC03_SELLING_CHARGES.COMPANY_CODE_SYS01=? and "+
          "DOC03_SELLING_CHARGES.DOC_TYPE=? and "+
          "DOC03_SELLING_CHARGES.DOC_YEAR=? and "+
          "DOC03_SELLING_CHARGES.DOC_NUMBER=? and "+
          "DOC03_SELLING_CHARGES.CHARGE_CODE_SAL06=?");

      for(int i=0;i<list.size();i++) {
        // phisically delete the record in DOC03...
        vo = (SaleDocChargeVO)list.get(i);
        pstmt.setString(1,vo.getCompanyCodeSys01DOC03());
        pstmt.setString(2,vo.getDocTypeDOC03());
        pstmt.setBigDecimal(3,vo.getDocYearDOC03());
        pstmt.setBigDecimal(4,vo.getDocNumberDOC03());
        pstmt.setString(5,vo.getChargeCodeSal06DOC03());
        pstmt.execute();
      }

      Response res = totals.updateTaxableIncomes(
        conn,
        new SaleDocPK(vo.getCompanyCodeSys01DOC03(),vo.getDocTypeDOC03(),vo.getDocYearDOC03(),vo.getDocNumberDOC03()),
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while deleting existing sale charges",ex);
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
