package org.jallinone.variants.server;

import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.jallinone.commons.java.*;
import org.jallinone.events.server.*;
import org.jallinone.system.server.*;
import org.openswing.swing.logger.server.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.*;
import org.openswing.swing.server.*;
import org.jallinone.variants.java.VariantNameVO;



/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to load variants names defined for the specified company code.</p>
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
public class LoadVariantsNamesAction implements Action {


  public LoadVariantsNamesAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadVariantsNames";
  }

  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();

    Connection conn = null;
    try {
      String companyCodeSys01 = (String)inputPar;

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
      String sql =
          "select ITM21_VARIANTS.COMPANY_CODE_SYS01,ITM21_VARIANTS.TABLE_NAME,ITM21_VARIANTS.PROGRESSIVE_SYS10,"+
          "SYS10_TRANSLATIONS.DESCRIPTION,ITM21_VARIANTS.USE_VARIANT_TYPE "+
          "from ITM21_VARIANTS,SYS10_TRANSLATIONS where "+
          "ITM21_VARIANTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "ITM21_VARIANTS.COMPANY_CODE_SYS01=?";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01ITM21","ITM21_VARIANTS.COMPANY_CODE_SYS01");
      attribute2dbField.put("tableName","ITM21_VARIANTS.TABLE_NAME");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("progressiveSys10ITM21","ITM21_VARIANTS.PROGRESSIVE_SYS10");
      attribute2dbField.put("useVariantTypeITM21","ITM21_VARIANTS.USE_VARIANT_TYPE");


      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(companyCodeSys01);


      // read from ITM21 table...
      Response answer = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          VariantNameVO.class,
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching the list of variants names",ex);
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

