package org.jallinone.sales.activities.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.sales.activities.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.commons.server.CustomizeQueryUtil;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch sale activities from SAL09 table.</p>
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
public class LoadSaleActivitiesAction implements Action {


  public LoadSaleActivitiesAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadSaleActivities";
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

      // retrieve companies list...
      GridParams gridParams = (GridParams)inputPar;
      String companies = (String)gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01); // used in lookup grid...
      if (companies==null) {
        ArrayList companiesList = ((JAIOUserSessionParameters)userSessionPars).getCompanyBa().getCompaniesList("SAL09");
        companies = "";
        for(int i=0;i<companiesList.size();i++)
          companies += "'"+companiesList.get(i).toString()+"',";
        companies = companies.substring(0,companies.length()-1);
      }
      else
        companies = "'"+companies+"'";

      String sql =
          "select SAL09_ACTIVITIES.COMPANY_CODE_SYS01,SAL09_ACTIVITIES.ACTIVITY_CODE,SAL09_ACTIVITIES.PROGRESSIVE_SYS10,"+
          "SYS10_SAL09.DESCRIPTION,SAL09_ACTIVITIES.VALUE,SAL09_ACTIVITIES.VAT_CODE_REG01,"+
          "SAL09_ACTIVITIES.CURRENCY_CODE_REG03,SYS10_REG01.DESCRIPTION,REG01_VATS.VALUE,REG01_VATS.DEDUCTIBLE,REG03_CURRENCIES.CURRENCY_SYMBOL"+
          " from SAL09_ACTIVITIES,SYS10_TRANSLATIONS SYS10_SAL09,SYS10_TRANSLATIONS SYS10_REG01,REG01_VATS,REG03_CURRENCIES where "+
          "SAL09_ACTIVITIES.CURRENCY_CODE_REG03=REG03_CURRENCIES.CURRENCY_CODE and "+
          "SAL09_ACTIVITIES.PROGRESSIVE_SYS10=SYS10_SAL09.PROGRESSIVE and "+
          "SYS10_SAL09.LANGUAGE_CODE=? and "+
          "SAL09_ACTIVITIES.COMPANY_CODE_SYS01 in ("+companies+") and "+
          "REG01_VATS.VAT_CODE=SAL09_ACTIVITIES.VAT_CODE_REG01 and "+
          "REG01_VATS.PROGRESSIVE_SYS10=SYS10_REG01.PROGRESSIVE and "+
          "SYS10_REG01.LANGUAGE_CODE=? ";


      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SAL09","SAL09_ACTIVITIES.COMPANY_CODE_SYS01");
      attribute2dbField.put("activityCodeSAL09","SAL09_ACTIVITIES.ACTIVITY_CODE");
      attribute2dbField.put("descriptionSYS10","SYS10_SAL09.DESCRIPTION");
      attribute2dbField.put("progressiveSys10SAL09","SAL09_ACTIVITIES.PROGRESSIVE_SYS10");
      attribute2dbField.put("valueSAL09","SAL09_ACTIVITIES.VALUE");
      attribute2dbField.put("vatCodeReg01SAL09","SAL09_ACTIVITIES.VAT_CODE_REG01");
      attribute2dbField.put("currencyCodeReg03SAL09","SAL09_ACTIVITIES.CURRENCY_CODE_REG03");
      attribute2dbField.put("vatDescriptionSYS10","SYS10_REG01.DESCRIPTION");
      attribute2dbField.put("vatValueREG01","REG01_VATS.VALUE");
      attribute2dbField.put("vatDeductibleREG01","REG01_VATS.DEDUCTIBLE");
      attribute2dbField.put("currencySymbolREG03","REG03_CURRENCIES.CURRENCY_SYMBOL");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(serverLanguageId);

      if (gridParams.getOtherGridParams().get(ApplicationConsts.CURRENCY_CODE_REG03)!=null) {
        sql += " and SAL09_ACTIVITIES.CURRENCY_CODE_REG03=? ";
        values.add( gridParams.getOtherGridParams().get(ApplicationConsts.CURRENCY_CODE_REG03) );
      }

      // read from SAL09 table...
      Response answer = CustomizeQueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          SaleActivityVO.class,
          "Y",
          "N",
          context,
          gridParams,
          50,
          true,
          ApplicationConsts.ID_SALE_ACTIVITIES // window identifier...
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching sale activities list",ex);
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
