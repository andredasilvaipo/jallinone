package org.jallinone.purchases.documents.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.purchases.documents.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import java.math.BigDecimal;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to (phisically) delete existing purchases order rows.</p>
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
public class DeletePurchaseDocRowsAction implements Action {

  private PurchaseDocTotalsAction totalBean = new PurchaseDocTotalsAction();
  private LoadPurchaseDocAction docBean = new LoadPurchaseDocAction();


  public DeletePurchaseDocRowsAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "deletePurchaseDocRows";
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
      ArrayList list = (ArrayList)inputPar;
      PurchaseDocRowPK rowPK = null;

      pstmt = conn.prepareStatement(
          "delete from DOC07_PURCHASE_ITEMS where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? and ITEM_CODE_ITM01=?"
      );

      for(int i=0;i<list.size();i++) {
        rowPK = (PurchaseDocRowPK)list.get(i);

        // phisically delete the record in DOC07...
        pstmt.setString(1,rowPK.getCompanyCodeSys01DOC07());
        pstmt.setString(2,rowPK.getDocTypeDOC07());
        pstmt.setBigDecimal(3,rowPK.getDocYearDOC07());
        pstmt.setBigDecimal(4,rowPK.getDocNumberDOC07());
        pstmt.setString(5,rowPK.getItemCodeItm01DOC07());
        pstmt.execute();
      }

      conn.commit();

      // recalculate totals...
      PurchaseDocPK pk = new PurchaseDocPK(
          rowPK.getCompanyCodeSys01DOC07(),
          rowPK.getDocTypeDOC07(),
          rowPK.getDocYearDOC07(),
          rowPK.getDocNumberDOC07()
      );
      Response docResponse = docBean.executeCommand(pk,userSessionPars,request,response,userSession,context);
      if (docResponse.isError()) {
        conn.rollback();
        return docResponse;
      }
      DetailPurchaseDocVO docVO = (DetailPurchaseDocVO)((VOResponse)docResponse).getVo();
      Response totalResponse = totalBean.executeCommand(docVO,userSessionPars,request,response,userSession,context);
      if (totalResponse.isError())
        return totalResponse;

      pstmt = conn.prepareStatement("update DOC06_PURCHASE set TAXABLE_INCOME=?,TOTAL_VAT=?,TOTAL=? where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=?");
      pstmt.setBigDecimal(1,docVO.getTaxableIncomeDOC06());
      pstmt.setBigDecimal(2,docVO.getTotalVatDOC06());
      pstmt.setBigDecimal(3,docVO.getTotalDOC06());
      pstmt.setString(4,rowPK.getCompanyCodeSys01DOC07());
      pstmt.setString(5,rowPK.getDocTypeDOC07());
      pstmt.setBigDecimal(6,rowPK.getDocYearDOC07());
      pstmt.setBigDecimal(7,rowPK.getDocNumberDOC07());
      pstmt.execute();

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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while deleting existing purchase order rows",ex);
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
