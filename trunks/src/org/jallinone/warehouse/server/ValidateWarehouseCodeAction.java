package org.jallinone.warehouse.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.openswing.swing.message.send.java.LookupValidationParams;
import org.jallinone.warehouse.java.WarehouseVO;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.openswing.swing.server.QueryUtil;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to validate a warehouse code from WAR01 table.</p>
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
public class ValidateWarehouseCodeAction implements Action {


  public ValidateWarehouseCodeAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "validateWarehouseCode";
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

      // retrieve roles list...
      Enumeration en = ((JAIOUserSessionParameters)userSessionPars).getUserRoles().keys();
      String roles = "";
      while(en.hasMoreElements())
        roles += en.nextElement().toString()+",";
      roles = roles.substring(0,roles.length()-1);

      LookupValidationParams pars = (LookupValidationParams)inputPar;

      // retrieve companies list...
      String companies = "";
      if (pars.getLookupValidationParameters().get(ApplicationConsts.COMPANY_CODE_SYS01)!=null) {
        companies = "'"+pars.getLookupValidationParameters().get(ApplicationConsts.COMPANY_CODE_SYS01)+"'";
      }
      else {
        ArrayList companiesList = ((JAIOUserSessionParameters)userSessionPars).getCompanyBa().getCompaniesList("WAR01");
        for(int i=0;i<companiesList.size();i++)
          companies += "'"+companiesList.get(i).toString()+"',";
        companies = companies.substring(0,companies.length()-1);
      }

      String sql =
          "select WAR01_WAREHOUSES.COMPANY_CODE_SYS01,WAR01_WAREHOUSES.WAREHOUSE_CODE,WAR01_WAREHOUSES.DESCRIPTION,"+
          "WAR01_WAREHOUSES.PROGRESSIVE_HIE02,WAR01_WAREHOUSES.ADDRESS,WAR01_WAREHOUSES.ZIP,WAR01_WAREHOUSES.CITY,"+
          "WAR01_WAREHOUSES.PROVINCE,WAR01_WAREHOUSES.COUNTRY,WAR01_WAREHOUSES.PROGRESSIVE_SYS04,REG04_SUBJECTS.NAME_1,"+
          "HIE02_HIERARCHIES.PROGRESSIVE_HIE01 from "+
          "WAR01_WAREHOUSES,REG04_SUBJECTS,HIE02_HIERARCHIES where "+
          "WAR01_WAREHOUSES.COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 and "+
          "REG04_SUBJECTS.SUBJECT_TYPE='M' and "+
          "WAR01_WAREHOUSES.COMPANY_CODE_SYS01 in ("+companies+") and "+
          "WAR01_WAREHOUSES.ENABLED='Y' and "+
          "WAR01_WAREHOUSES.WAREHOUSE_CODE=? and "+
          "WAR01_WAREHOUSES.PROGRESSIVE_HIE02=HIE02_HIERARCHIES.PROGRESSIVE";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01WAR01","WAR01_WAREHOUSES.COMPANY_CODE_SYS01");
      attribute2dbField.put("warehouseCodeWAR01","WAR01_WAREHOUSES.WAREHOUSE_CODE");
      attribute2dbField.put("descriptionWAR01","WAR01_WAREHOUSES.DESCRIPTION");
      attribute2dbField.put("progressiveHie02WAR01","WAR01_WAREHOUSES.PROGRESSIVE_HIE02");
      attribute2dbField.put("progressiveHie01HIE02","HIE02_HIERARCHIES.PROGRESSIVE_HIE01");
      attribute2dbField.put("addressWAR01","WAR01_WAREHOUSES.ADDRESS");
      attribute2dbField.put("zipWAR01","WAR01_WAREHOUSES.ZIP");
      attribute2dbField.put("cityWAR01","WAR01_WAREHOUSES.CITY");
      attribute2dbField.put("provinceWAR01","WAR01_WAREHOUSES.PROVINCE");
      attribute2dbField.put("countryWAR01","WAR01_WAREHOUSES.COUNTRY");
      attribute2dbField.put("progressiveSys04WAR01","WAR01_WAREHOUSES.PROGRESSIVE_SYS04");
      attribute2dbField.put("name_1REG04","REG04_SUBJECTS.NAME_1");

      ArrayList values = new ArrayList();
      values.add(pars.getCode());

      Response answer = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          WarehouseVO.class,
          "Y",
          "N",
          context,
          new GridParams(),
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while validating warehouse code",ex);
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        ConnectionManager.releaseConnection(conn, context);
      }
      catch (Exception ex1) {
      }
    }

  }



}
