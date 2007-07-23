package org.jallinone.warehouse.documents.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.warehouse.documents.java.DetailDeliveryNoteVO;
import org.jallinone.system.server.JAIOUserSessionParameters;
import java.math.BigDecimal;
import org.jallinone.system.progressives.server.ProgressiveUtils;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.system.java.ApplicationParametersVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to insert a new out delivery note in DOC08 table.</p>
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
public class InsertOutDeliveryNoteAction implements Action {


  public InsertOutDeliveryNoteAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "insertOutDeliveryNote";
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
      String companyCode = ((JAIOUserSessionParameters)userSessionPars).getCompanyBa().getCompaniesList("DOC08_OUT").get(0).toString();

      DetailDeliveryNoteVO vo = (DetailDeliveryNoteVO)inputPar;
      vo.setEnabledDOC08("Y");
      vo.setDescriptionDOC08(vo.getWarehouseDescriptionDOC08());

      if (vo.getCompanyCodeSys01DOC08()==null)
        vo.setCompanyCodeSys01DOC08(companyCode);

      // generate the internal progressive for the document...
      vo.setDocNumberDOC08( ProgressiveUtils.getInternalProgressive("DOC08_DELIVERY_NOTES","DOC_NUMBER",conn) );

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC08","COMPANY_CODE_SYS01");
      attribute2dbField.put("docTypeDOC08","DOC_TYPE");
      attribute2dbField.put("docStateDOC08","DOC_STATE");
      attribute2dbField.put("docYearDOC08","DOC_YEAR");
      attribute2dbField.put("docNumberDOC08","DOC_NUMBER");
      attribute2dbField.put("docDateDOC08","DOC_DATE");
      attribute2dbField.put("warehouseCodeWar01DOC08","WAREHOUSE_CODE_WAR01");
      attribute2dbField.put("docRefDOC08","DOC_REF");

      attribute2dbField.put("addressDOC08","ADDRESS");
      attribute2dbField.put("cityDOC08","CITY");
      attribute2dbField.put("provinceDOC08","PROVINCE");
      attribute2dbField.put("countryDOC08","COUNTRY");
      attribute2dbField.put("zipDOC08","ZIP");
      attribute2dbField.put("noteDOC08","NOTE");
      attribute2dbField.put("enabledDOC08","ENABLED");
      attribute2dbField.put("carrierCodeReg09DOC08","CARRIER_CODE_REG09");
      attribute2dbField.put("progressiveReg04DOC08","PROGRESSIVE_REG04");
      attribute2dbField.put("deliveryDateDOC08","DELIVERY_DATE");
      attribute2dbField.put("transportMotiveCodeReg20DOC08","TRANSPORT_MOTIVE_CODE_REG20");

      // insert into DOC08...
      Response res = QueryUtil.insertTable(
          conn,
          userSessionPars,
          vo,
          "DOC08_DELIVERY_NOTES",
          attribute2dbField,
          "Y",
          "N",
          context,
          true
      );

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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while inserting a new out delivery note",ex);
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
