package org.jallinone.sales.pricelist.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.sales.pricelist.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.commons.server.CustomizeQueryUtil;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch pricelists from SAL01 table.</p>
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
public class LoadPricelistAction implements Action {


  public LoadPricelistAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadPricelists";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();

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

      // retrieve companies list...
      GridParams gridParams = (GridParams)inputPar;
      String companies = (String)gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01); // used in lookup grid...
      if (companies==null) {
        ArrayList companiesList = ((JAIOUserSessionParameters)userSessionPars).getCompanyBa().getCompaniesList("SAL01");
        companies = "";
        for(int i=0;i<companiesList.size();i++)
          companies += "'"+companiesList.get(i).toString()+"',";
        companies = companies.substring(0,companies.length()-1);
      }
      else {
        companies = "'"+companies+"'";
      }

      String sql =
          "select SAL01_PRICELISTS.COMPANY_CODE_SYS01,SAL01_PRICELISTS.PRICELIST_CODE,SAL01_PRICELISTS.PROGRESSIVE_SYS10,SYS10_TRANSLATIONS.DESCRIPTION,SAL01_PRICELISTS.CURRENCY_CODE_REG03 from SAL01_PRICELISTS,SYS10_TRANSLATIONS where "+
          "SAL01_PRICELISTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and SAL01_PRICELISTS.COMPANY_CODE_SYS01 in ("+companies+")";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SAL01","SAL01_PRICELISTS.COMPANY_CODE_SYS01");
      attribute2dbField.put("pricelistCodeSAL01","SAL01_PRICELISTS.PRICELIST_CODE");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("progressiveSys10SAL01","SAL01_PRICELISTS.PROGRESSIVE_SYS10");
      attribute2dbField.put("currencyCodeReg03SAL01","SAL01_PRICELISTS.CURRENCY_CODE_REG03");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);

      // read from SAL01 table...
      Response answer = CustomizeQueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          PricelistVO.class,
          "Y",
          "N",
          context,
          gridParams,
          50,
          true,
          new BigDecimal(302) // window identifier...
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching pricelists list",ex);
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
