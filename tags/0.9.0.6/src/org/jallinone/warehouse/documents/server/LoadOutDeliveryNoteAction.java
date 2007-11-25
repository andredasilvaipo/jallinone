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
import org.jallinone.commons.server.CustomizeQueryUtil;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.system.java.ApplicationParametersVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch a specific doc order from DOC08 table.</p>
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
public class LoadOutDeliveryNoteAction implements Action {


  public LoadOutDeliveryNoteAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadOutDeliveryNote";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();
    Connection conn = null;
    Statement stmt = null;
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

      DeliveryNotePK pk = (DeliveryNotePK)inputPar;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC08","DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01");
      attribute2dbField.put("docTypeDOC08","DOC08_DELIVERY_NOTES.DOC_TYPE");
      attribute2dbField.put("docStateDOC08","DOC08_DELIVERY_NOTES.DOC_STATE");
      attribute2dbField.put("docYearDOC08","DOC08_DELIVERY_NOTES.DOC_YEAR");
      attribute2dbField.put("docNumberDOC08","DOC08_DELIVERY_NOTES.DOC_NUMBER");
      attribute2dbField.put("docDateDOC08","DOC08_DELIVERY_NOTES.DOC_DATE");
      attribute2dbField.put("warehouseCodeWar01DOC08","DOC08_DELIVERY_NOTES.WAREHOUSE_CODE_WAR01");
      attribute2dbField.put("docRefDOC08","DOC08_DELIVERY_NOTES.DOC_REF");
      attribute2dbField.put("warehouseDescriptionDOC08","WAR01_WAREHOUSES.DESCRIPTION");

      attribute2dbField.put("addressDOC08","DOC08_DELIVERY_NOTES.ADDRESS");
      attribute2dbField.put("cityDOC08","DOC08_DELIVERY_NOTES.CITY");
      attribute2dbField.put("provinceDOC08","DOC08_DELIVERY_NOTES.PROVINCE");
      attribute2dbField.put("countryDOC08","DOC08_DELIVERY_NOTES.COUNTRY");
      attribute2dbField.put("zipDOC08","DOC08_DELIVERY_NOTES.ZIP");
      attribute2dbField.put("noteDOC08","DOC08_DELIVERY_NOTES.NOTE");
      attribute2dbField.put("enabledDOC08","DOC08_DELIVERY_NOTES.ENABLED");
      attribute2dbField.put("carrierCodeReg09DOC08","DOC08_DELIVERY_NOTES.CARRIER_CODE_REG09");
      attribute2dbField.put("carrierDescriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("progressiveReg04DOC08","DOC08_DELIVERY_NOTES.PROGRESSIVE_REG04");
      attribute2dbField.put("supplierCustomerCodeDOC08","SAL07_CUSTOMERS.CUSTOMER_CODE");
      attribute2dbField.put("name_1REG04","REG04_SUBJECTS.NAME_1");
      attribute2dbField.put("name_2REG04","REG04_SUBJECTS.NAME_2");
      attribute2dbField.put("progressiveHie02WAR01","WAR01_WAREHOUSES.PROGRESSIVE_HIE02");
      attribute2dbField.put("docSequenceDOC08","DOC08_DELIVERY_NOTES.DOC_SEQUENCE");
      attribute2dbField.put("deliveryDateDOC08","DOC08_DELIVERY_NOTES.DELIVERY_DATE");
      attribute2dbField.put("transportMotiveCodeReg20DOC08","DOC08_DELIVERY_NOTES.TRANSPORT_MOTIVE_CODE_REG20");
      attribute2dbField.put("transportMotiveDescriptionSYS10","SYS10_TRANSLATIONS_B.DESCRIPTION");

      HashSet pkAttributes = new HashSet();
      pkAttributes.add("companyCodeSys01DOC08");
      pkAttributes.add("docTypeDOC08");
      pkAttributes.add("docYearDOC08");
      pkAttributes.add("docNumberDOC08");

      String baseSQL =
          "select DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01,DOC08_DELIVERY_NOTES.DOC_TYPE,DOC08_DELIVERY_NOTES.DOC_STATE,"+
          "DOC08_DELIVERY_NOTES.DOC_YEAR,DOC08_DELIVERY_NOTES.DOC_NUMBER,DOC08_DELIVERY_NOTES.DOC_DATE, "+
          "DOC08_DELIVERY_NOTES.WAREHOUSE_CODE_WAR01,DOC08_DELIVERY_NOTES.DOC_REF,WAR01_WAREHOUSES.DESCRIPTION,"+
          "DOC08_DELIVERY_NOTES.ADDRESS,DOC08_DELIVERY_NOTES.CITY,DOC08_DELIVERY_NOTES.PROVINCE,DOC08_DELIVERY_NOTES.COUNTRY,DOC08_DELIVERY_NOTES.ZIP,"+
          "DOC08_DELIVERY_NOTES.NOTE,DOC08_DELIVERY_NOTES.ENABLED,DOC08_DELIVERY_NOTES.CARRIER_CODE_REG09,SYS10_TRANSLATIONS.DESCRIPTION,"+
          "DOC08_DELIVERY_NOTES.PROGRESSIVE_REG04,SAL07_CUSTOMERS.CUSTOMER_CODE,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,"+
          "WAR01_WAREHOUSES.PROGRESSIVE_HIE02,DOC08_DELIVERY_NOTES.DOC_SEQUENCE, "+
          "DOC08_DELIVERY_NOTES.DELIVERY_DATE,DOC08_DELIVERY_NOTES.TRANSPORT_MOTIVE_CODE_REG20,SYS10_TRANSLATIONS_B.DESCRIPTION "+
          " from DOC08_DELIVERY_NOTES,WAR01_WAREHOUSES,REG09_CARRIERS,SYS10_TRANSLATIONS,SAL07_CUSTOMERS,REG04_SUBJECTS,"+
          "REG20_TRANSPORT_MOTIVES,SYS10_TRANSLATIONS SYS10_TRANSLATIONS_B where "+
          "DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01=WAR01_WAREHOUSES.COMPANY_CODE_SYS01 and "+
          "DOC08_DELIVERY_NOTES.WAREHOUSE_CODE_WAR01=WAR01_WAREHOUSES.WAREHOUSE_CODE and "+
          "DOC08_DELIVERY_NOTES.CARRIER_CODE_REG09=REG09_CARRIERS.CARRIER_CODE and "+
          "REG09_CARRIERS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01=SAL07_CUSTOMERS.COMPANY_CODE_SYS01 and "+
          "DOC08_DELIVERY_NOTES.PROGRESSIVE_REG04=SAL07_CUSTOMERS.PROGRESSIVE_REG04 and "+
          "DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 and "+
          "DOC08_DELIVERY_NOTES.PROGRESSIVE_REG04=REG04_SUBJECTS.PROGRESSIVE and "+
          "DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01=? and "+
          "DOC08_DELIVERY_NOTES.DOC_TYPE=? and "+
          "DOC08_DELIVERY_NOTES.DOC_YEAR=? and "+
          "DOC08_DELIVERY_NOTES.DOC_NUMBER=? and "+
          "DOC08_DELIVERY_NOTES.TRANSPORT_MOTIVE_CODE_REG20=REG20_TRANSPORT_MOTIVES.TRANSPORT_MOTIVE_CODE and "+
          "REG20_TRANSPORT_MOTIVES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS_B.PROGRESSIVE and SYS10_TRANSLATIONS_B.LANGUAGE_CODE=? ";

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(pk.getCompanyCodeSys01DOC08());
      values.add(pk.getDocTypeDOC08());
      values.add(pk.getDocYearDOC08());
      values.add(pk.getDocNumberDOC08());
      values.add(serverLanguageId);

      // read from DOC08 table...
      Response res = QueryUtil.getQuery(
          conn,
          userSessionPars,
          baseSQL,
          values,
          attribute2dbField,
          DetailDeliveryNoteVO.class,
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


      return answer;
    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching an existing out delivery note",ex);
      try {
        conn.rollback();
      }
      catch (Exception ex3) {
      }
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
