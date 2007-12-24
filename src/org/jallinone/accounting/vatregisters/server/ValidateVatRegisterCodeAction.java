package org.jallinone.accounting.vatregisters.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.accounting.vatregisters.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.openswing.swing.message.send.java.LookupValidationParams;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.commons.server.CustomizeQueryUtil;
import java.math.BigDecimal;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch vat register from ACC04 table.</p>
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
public class ValidateVatRegisterCodeAction implements Action {


  public ValidateVatRegisterCodeAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "validateVatRegisterCode";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();

    Connection conn = null;
    try {


      LookupValidationParams validationPars = (LookupValidationParams)inputPar;

      // retrieve companies list...
      String companies = "";
      if (validationPars.getLookupValidationParameters().get(ApplicationConsts.COMPANY_CODE_SYS01)!=null) {
        companies = "'"+validationPars.getLookupValidationParameters().get(ApplicationConsts.COMPANY_CODE_SYS01)+"'";
      }
      else {
        ArrayList companiesList = ((JAIOUserSessionParameters)userSessionPars).getCompanyBa().getCompaniesList("ACC04");
        for(int i=0;i<companiesList.size();i++)
          companies += "'"+companiesList.get(i).toString()+"',";
        companies = companies.substring(0,companies.length()-1);
      }

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
          "select ACC04_VAT_REGISTERS.REGISTER_CODE,ACC04_VAT_REGISTERS.PROGRESSIVE_SYS10,SYS10_TRANSLATIONS.DESCRIPTION,ACC04_VAT_REGISTERS.ENABLED, "+
          "SYS10_B.DESCRIPTION,ACC04_VAT_REGISTERS.ACCOUNT_CODE_ACC02,ACC04_VAT_REGISTERS.REGISTER_TYPE,ACC04_VAT_REGISTERS.READ_ONLY "+
          "from ACC04_VAT_REGISTERS,SYS10_TRANSLATIONS,ACC02_ACCOUNTS,SYS10_TRANSLATIONS SYS10_B where "+
          "ACC04_VAT_REGISTERS.COMPANY_CODE_SYS01=ACC02_ACCOUNTS.COMPANY_CODE_SYS01 and "+
          "ACC04_VAT_REGISTERS.ACCOUNT_CODE_ACC02=ACC02_ACCOUNTS.ACCOUNT_CODE and "+
          "ACC02_ACCOUNTS.PROGRESSIVE_SYS10=SYS10_B.PROGRESSIVE and "+
          "SYS10_B.LANGUAGE_CODE=? and "+
          "ACC04_VAT_REGISTERS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "ACC04_VAT_REGISTERS.ENABLED='Y' and "+
          "ACC04_VAT_REGISTERS.REGISTER_CODE='"+validationPars.getCode()+"' and "+
          "ACC04_VAT_REGISTERS.COMPANY_CODE_SYS01 in ("+companies+")";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("registerCodeACC04","ACC04_VAT_REGISTERS.REGISTER_CODE");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("progressiveSys10ACC04","ACC04_VAT_REGISTERS.PROGRESSIVE_SYS10");
      attribute2dbField.put("enabledACC04","ACC04_VAT_REGISTERS.ENABLED");
      attribute2dbField.put("accountDescriptionACC04","SYS10_B.DESCRIPTION");
      attribute2dbField.put("accountCodeAcc02ACC04","ACC04_VAT_REGISTERS.ACCOUNT_CODE_ACC02");
      attribute2dbField.put("registerTypeACC04","ACC04_VAT_REGISTERS.REGISTER_TYPE");
      attribute2dbField.put("readOnlyACC04","ACC04_VAT_REGISTERS.READ_ONLY");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(serverLanguageId);

      GridParams gridParams = new GridParams();

      // read from ACC04 table...
      Response answer = CustomizeQueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          VatRegisterVO.class,
          "Y",
          "N",
          context,
          gridParams,
          true,
          ApplicationConsts.ID_VAT_REGISTERS // window identifier...
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while validating vat register code",ex);
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
