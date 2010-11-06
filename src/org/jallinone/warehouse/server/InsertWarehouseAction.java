package org.jallinone.warehouse.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.warehouse.java.WarehouseVO;
import org.jallinone.system.server.JAIOUserSessionParameters;
import java.math.BigDecimal;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.system.progressives.server.ProgressiveUtils;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;
import org.jallinone.system.progressives.server.CompanyProgressiveUtils;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to insert a new warehouse in WAR01 table.</p>
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
public class InsertWarehouseAction implements Action {


  public InsertWarehouseAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "insertWarehouse";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    Connection conn = null;
    Statement stmt = null;
    try {
      conn = ConnectionManager.getConnection(context);
      String companyCode = ((JAIOUserSessionParameters)userSessionPars).getCompanyBa().getCompaniesList("WAR01").get(0).toString();

      WarehouseVO vo = (WarehouseVO)inputPar;
      vo.setEnabledWAR01("Y");
      if (vo.getCompanyCodeSys01WAR01()==null)
        vo.setCompanyCodeSys01WAR01(companyCode);

      // generate PROGRESSIVE_HIE02 value...
      stmt = conn.createStatement();
      BigDecimal progressiveHIE02 = CompanyProgressiveUtils.getInternalProgressive(vo.getCompanyCodeSys01WAR01(),"HIE02_HIERARCHIES","PROGRESSIVE",conn);
      BigDecimal progressiveHIE01 = TranslationUtils.insertTranslations("",vo.getCompanyCodeSys01WAR01(),conn); // the root has no description as default...
      stmt.execute("INSERT INTO HIE02_HIERARCHIES(PROGRESSIVE,COMPANY_CODE_SYS01,ENABLED) VALUES("+progressiveHIE02+",'"+vo.getCompanyCodeSys01WAR01()+"','Y')");
      stmt.execute("INSERT INTO HIE01_LEVELS(PROGRESSIVE,PROGRESSIVE_HIE02,LEV,ENABLED) VALUES("+progressiveHIE01+","+progressiveHIE02+",0,'Y')");
      stmt.execute("UPDATE HIE02_HIERARCHIES SET PROGRESSIVE_HIE01="+progressiveHIE01+" WHERE PROGRESSIVE="+progressiveHIE02);
      vo.setProgressiveHie02WAR01(progressiveHIE02);

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01WAR01","COMPANY_CODE_SYS01");
      attribute2dbField.put("warehouseCodeWAR01","WAREHOUSE_CODE");
      attribute2dbField.put("descriptionWAR01","DESCRIPTION");
      attribute2dbField.put("progressiveHie02WAR01","PROGRESSIVE_HIE02");
      attribute2dbField.put("addressWAR01","ADDRESS");
      attribute2dbField.put("zipWAR01","ZIP");
      attribute2dbField.put("cityWAR01","CITY");
      attribute2dbField.put("provinceWAR01","PROVINCE");
      attribute2dbField.put("countryWAR01","COUNTRY");
      attribute2dbField.put("progressiveSys04WAR01","PROGRESSIVE_SYS04");
      attribute2dbField.put("enabledWAR01","ENABLED");

      // insert into WAR01...
      Response res = CustomizeQueryUtil.insertTable(
          conn,
          userSessionPars,
          vo,
          "WAR01_WAREHOUSES",
          attribute2dbField,
          "Y",
          "N",
          context,
          true,
          new BigDecimal(462) // window identifier...
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while inserting a new warehouse",ex);
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
