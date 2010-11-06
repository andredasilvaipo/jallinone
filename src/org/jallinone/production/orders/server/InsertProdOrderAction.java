package org.jallinone.production.orders.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import java.math.BigDecimal;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.system.progressives.server.ProgressiveUtils;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.system.java.ApplicationParametersVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.message.send.java.LookupValidationParams;
import org.jallinone.production.orders.java.*;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;
import org.jallinone.system.progressives.server.CompanyProgressiveUtils;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to insert a new production order in DOC22 table.</p>
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
public class InsertProdOrderAction implements Action {


  public InsertProdOrderAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "insertProdOrder";
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

      String companyCode = ((JAIOUserSessionParameters)userSessionPars).getCompanyBa().getCompaniesList("DOC22").get(0).toString();

      DetailProdOrderVO vo = (DetailProdOrderVO)inputPar;

      if (vo.getCompanyCodeSys01DOC22()==null)
        vo.setCompanyCodeSys01DOC22(companyCode);

      // generate internal progressive for doc. number...
      vo.setDocNumberDOC22(CompanyProgressiveUtils.getInternalProgressive(vo.getCompanyCodeSys01DOC22(),"DOC22_PRODUCTION_ORDER","DOC_NUMBER",conn));

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC22","COMPANY_CODE_SYS01");
      attribute2dbField.put("docStateDOC22","DOC_STATE");
      attribute2dbField.put("docYearDOC22","DOC_YEAR");
      attribute2dbField.put("docNumberDOC22","DOC_NUMBER");
      attribute2dbField.put("docDateDOC22","DOC_DATE");
      attribute2dbField.put("warehouseCodeWar01DOC22","WAREHOUSE_CODE_WAR01");
      attribute2dbField.put("warehouseCode2War01DOC22","WAREHOUSE_CODE2_WAR01");
      attribute2dbField.put("descriptionWar01DOC22","DESCRIPTION_WAR01");
      attribute2dbField.put("description2War01DOC22","DESCRIPTION2_WAR01");
      attribute2dbField.put("noteDOC22","NOTE");


      // insert into DOC22...
      Response res = CustomizeQueryUtil.insertTable(
          conn,
          userSessionPars,
          vo,
          "DOC22_PRODUCTION_ORDER",
          attribute2dbField,
          "Y",
          "N",
          context,
          true,
          ApplicationConsts.ID_PROD_ORDER // window identifier...
      );

      Response answer = new VOResponse(vo);

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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while inserting a new production order",ex);
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
