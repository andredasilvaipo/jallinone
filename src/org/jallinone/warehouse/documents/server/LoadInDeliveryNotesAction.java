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
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.hierarchies.java.HierarchyLevelVO;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch in delivery notes from DOC08 table.</p>
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
public class LoadInDeliveryNotesAction implements Action {


  public LoadInDeliveryNotesAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadInDeliveryNotes";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    PreparedStatement pstmt = null;
    Connection conn = null;
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

      GridParams pars = (GridParams)inputPar;

      // retrieve companies list...
      ArrayList companiesList = ((JAIOUserSessionParameters)userSessionPars).getCompanyBa().getCompaniesList("DOC08_IN");
      String companies = "";
      for(int i=0;i<companiesList.size();i++)
        companies += "'"+companiesList.get(i).toString()+"',";
      companies = companies.substring(0,companies.length()-1);

      String sql =
          "select DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01,DOC08_DELIVERY_NOTES.DOC_TYPE,DOC08_DELIVERY_NOTES.DOC_STATE,"+
          "DOC08_DELIVERY_NOTES.DOC_YEAR,DOC08_DELIVERY_NOTES.DOC_NUMBER,DOC08_DELIVERY_NOTES.DOC_DATE, "+
          "DOC08_DELIVERY_NOTES.WAREHOUSE_CODE_WAR01,DOC08_DELIVERY_NOTES.DOC_REF,WAR01_WAREHOUSES.DESCRIPTION,"+
          "DOC08_DELIVERY_NOTES.PROGRESSIVE_REG04,PUR01_SUPPLIERS.SUPPLIER_CODE,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2, "+
          "DOC08_DELIVERY_NOTES.DOC_SEQUENCE "+
          " from DOC08_DELIVERY_NOTES,WAR01_WAREHOUSES,PUR01_SUPPLIERS,REG04_SUBJECTS where "+
          "DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01=WAR01_WAREHOUSES.COMPANY_CODE_SYS01 and "+
          "DOC08_DELIVERY_NOTES.WAREHOUSE_CODE_WAR01=WAR01_WAREHOUSES.WAREHOUSE_CODE and "+
          "DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01 in ("+companies+") and "+
          "DOC08_DELIVERY_NOTES.ENABLED='Y' and "+
          "DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01=PUR01_SUPPLIERS.COMPANY_CODE_SYS01 and "+
          "DOC08_DELIVERY_NOTES.PROGRESSIVE_REG04=PUR01_SUPPLIERS.PROGRESSIVE_REG04 and "+
          "DOC08_DELIVERY_NOTES.COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 and "+
          "DOC08_DELIVERY_NOTES.PROGRESSIVE_REG04=REG04_SUBJECTS.PROGRESSIVE and "+
          "DOC08_DELIVERY_NOTES.DOC_TYPE=?";

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
      attribute2dbField.put("progressiveReg04DOC08","DOC08_DELIVERY_NOTES.PROGRESSIVE_REG04");
      attribute2dbField.put("supplierCustomerCodeDOC08","PUR01_SUPPLIERS.SUPPLIER_CODE");
      attribute2dbField.put("name_1REG04","REG04_SUBJECTS.NAME_1");
      attribute2dbField.put("name_2REG04","REG04_SUBJECTS.NAME_2");
      attribute2dbField.put("docSequenceDOC08","DOC08_DELIVERY_NOTES.DOC_SEQUENCE");

      ArrayList values = new ArrayList();
      values.add(ApplicationConsts.IN_DELIVERY_NOTE_DOC_TYPE);

      // read from DOC08 table...
      Response answer = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          GridDeliveryNoteVO.class,
          "Y",
          "N",
          context,
          pars,
          50,
          true
      );

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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching in delivery notes list",ex);
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
