package org.jallinone.warehouse.documents.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import java.math.BigDecimal;
import org.jallinone.system.progressives.server.ProgressiveUtils;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.system.java.ApplicationParametersVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.message.send.java.LookupValidationParams;
import org.jallinone.warehouse.documents.java.*;
import org.openswing.swing.internationalization.server.ServerResourcesFactory;
import org.openswing.swing.internationalization.java.Resources;
import org.jallinone.warehouse.documents.java.DetailDeliveryNoteVO;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to close a delivery note and generate the progressive.</p>
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
public class CloseDeliveryNoteAction implements Action {

  private UpdateInQtysPurchaseOrderBean inQtyBean = new UpdateInQtysPurchaseOrderBean();
  private UpdateOutQtysSaleDocBean outQtyBean = new UpdateOutQtysSaleDocBean();


  public CloseDeliveryNoteAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "closeDeliveryNote";
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

      DetailDeliveryNoteVO vo = (DetailDeliveryNoteVO)inputPar;

      // generate progressive for doc. sequence...
      pstmt = conn.prepareStatement(
        "select max(DOC_SEQUENCE) from DOC08_DELIVERY_NOTES where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_SEQUENCE is not null"
      );
      pstmt.setString(1,vo.getCompanyCodeSys01DOC08());
      pstmt.setString(2,vo.getDocTypeDOC08());
      pstmt.setBigDecimal(3,vo.getDocYearDOC08());
      ResultSet rset = pstmt.executeQuery();
      int docSequenceDOC08 = 1;
      if (rset.next())
        docSequenceDOC08 = rset.getInt(1)+1;
      rset.close();


      // update delivery note state and note...
      pstmt = conn.prepareStatement("update DOC08_DELIVERY_NOTES set DOC_STATE=?,DOC_SEQUENCE=? where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? and DOC_STATE=?");
      pstmt.setString(1,ApplicationConsts.CLOSED);
      pstmt.setInt(2,docSequenceDOC08);
      pstmt.setString(3,vo.getCompanyCodeSys01DOC08());
      pstmt.setString(4,vo.getDocTypeDOC08());
      pstmt.setBigDecimal(5,vo.getDocYearDOC08());
      pstmt.setBigDecimal(6,vo.getDocNumberDOC08());
      pstmt.setString(7,ApplicationConsts.HEADER_BLOCKED);
      if (pstmt.executeUpdate()==1) {
        // call in/out quantities updating routine...
        Response res = null;
        DeliveryNotePK pk = new DeliveryNotePK(
            vo.getCompanyCodeSys01DOC08(),
            vo.getDocTypeDOC08(),
            vo.getDocYearDOC08(),
            vo.getDocNumberDOC08()
        );
        if (vo.getDocTypeDOC08().equals(ApplicationConsts.IN_DELIVERY_NOTE_DOC_TYPE))
          res = inQtyBean.updateInQuantities(conn,pk,userSessionPars,request,response,userSession,context);
        else if (vo.getDocTypeDOC08().equals(ApplicationConsts.OUT_DELIVERY_NOTE_DOC_TYPE))
          res = outQtyBean.updateOutQuantities(conn,pk,userSessionPars,request,response,userSession,context);

        if (res.isError()) {
          conn.rollback();
          Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while closing a delivery note:\n"+res.getErrorMessage(),null);
          return res;
        }
        Response answer = new VOResponse(new BigDecimal(docSequenceDOC08));

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
      else {
        conn.rollback();

        // retrieve internationalization settings (Resources object)...
        ServerResourcesFactory factory = (ServerResourcesFactory)context.getAttribute(Controller.RESOURCES_FACTORY);
        Resources res = factory.getResources(userSessionPars.getLanguageId());
        return new ErrorResponse(res.getResource("no delivery note to update"));
      }
    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while closing a delivery note",ex);
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
