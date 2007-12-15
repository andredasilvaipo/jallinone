package org.jallinone.warehouse.documents.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.warehouse.documents.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import java.math.BigDecimal;
import org.jallinone.system.progressives.server.ProgressiveUtils;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.system.java.ApplicationParametersVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.message.send.java.LookupValidationParams;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to update existing in delivery note rows.</p>
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
public class UpdateInDeliveryNoteRowsAction implements Action {

  private InsertInSerialNumbersBean serialNumBean = new InsertInSerialNumbersBean();


  public UpdateInDeliveryNoteRowsAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "updateInDeliveryNoteRows";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();
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

      ArrayList oldRows = ((ArrayList[])inputPar)[0];
      ArrayList newRows = ((ArrayList[])inputPar)[1];
      GridInDeliveryNoteRowVO oldVO = null;
      GridInDeliveryNoteRowVO newVO = null;

      Response res = null;
      for(int i=0;i<newRows.size();i++) {
        oldVO = (GridInDeliveryNoteRowVO)oldRows.get(i);
        newVO = (GridInDeliveryNoteRowVO)newRows.get(i);

        Map attribute2dbField = new HashMap();
        attribute2dbField.put("companyCodeSys01DOC09","COMPANY_CODE_SYS01");
        attribute2dbField.put("docTypeDOC09","DOC_TYPE");
        attribute2dbField.put("docYearDOC09","DOC_YEAR");
        attribute2dbField.put("docNumberDOC09","DOC_NUMBER");
        attribute2dbField.put("docTypeDoc06DOC09","DOC_TYPE_DOC06");
        attribute2dbField.put("docYearDoc06DOC09","DOC_YEAR_DOC06");
        attribute2dbField.put("docNumberDoc06DOC09","DOC_NUMBER_DOC06");
        attribute2dbField.put("rowNumberDOC09","ROW_NUMBER");
        attribute2dbField.put("itemCodeItm01DOC09","ITEM_CODE_ITM01");
        attribute2dbField.put("qtyDOC09","QTY");
        attribute2dbField.put("supplierQtyDOC09","SUPPLIER_QTY");
        attribute2dbField.put("progressiveHie02DOC09","PROGRESSIVE_HIE02");
        attribute2dbField.put("progressiveHie01DOC09","PROGRESSIVE_HIE01");
        attribute2dbField.put("invoiceQtyDOC09","INVOICE_QTY");

        HashSet pkAttributes = new HashSet();
        pkAttributes.add("companyCodeSys01DOC09");
        pkAttributes.add("docTypeDOC09");
        pkAttributes.add("docYearDOC09");
        pkAttributes.add("docNumberDOC09");
        pkAttributes.add("docTypeDoc06DOC09");
        pkAttributes.add("docYearDoc06DOC09");
        pkAttributes.add("docNumberDoc06DOC09");
        pkAttributes.add("rowNumberDOC09");
        pkAttributes.add("itemCodeItm01DOC09");

        // update DOC09 table...
        res = QueryUtil.updateTable(
            conn,
            userSessionPars,
            pkAttributes,
            oldVO,
            newVO,
            "DOC09_IN_DELIVERY_NOTE_ITEMS",
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

        // insert serial numbers...
        res = serialNumBean.reinsertInSerialNumbers(newVO,conn,userSessionPars,request,response,userSession,context);
        if (res.isError()) {
          conn.rollback();
          return res;
        }

      }

      Response answer = new VOListResponse(newRows,false,newRows.size());

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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while updating an existing purchase order row",ex);
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
