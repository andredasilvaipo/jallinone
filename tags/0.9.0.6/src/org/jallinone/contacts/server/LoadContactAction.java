package org.jallinone.contacts.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.contacts.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.commons.server.CustomizeQueryUtil;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.subjects.java.SubjectPK;
import org.jallinone.subjects.java.SubjectVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.subjects.server.*;
import org.jallinone.subjects.java.OrganizationVO;
import org.jallinone.subjects.java.PeopleVO;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch contact from REG04 table.</p>
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
public class LoadContactAction implements Action {


  public LoadContactAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadContact";
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

      SubjectVO subVO = (SubjectVO)inputPar;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01REG04","REG04_SUBJECTS.COMPANY_CODE_SYS01");
      attribute2dbField.put("name_1REG04","REG04_SUBJECTS.NAME_1");
      attribute2dbField.put("name_2REG04","REG04_SUBJECTS.NAME_2");
      attribute2dbField.put("progressiveREG04","REG04_SUBJECTS.PROGRESSIVE");
      attribute2dbField.put("addressREG04","REG04_SUBJECTS.ADDRESS");
      attribute2dbField.put("cityREG04","REG04_SUBJECTS.CITY");
      attribute2dbField.put("provinceREG04","REG04_SUBJECTS.PROVINCE");
      attribute2dbField.put("countryREG04","REG04_SUBJECTS.COUNTRY");
      attribute2dbField.put("taxCodeREG04","REG04_SUBJECTS.TAX_CODE");
      attribute2dbField.put("subjectTypeREG04","REG04_SUBJECTS.SUBJECT_TYPE");
      attribute2dbField.put("zipREG04","REG04_SUBJECTS.ZIP");
      attribute2dbField.put("sexREG04","REG04_SUBJECTS.SEX");
      attribute2dbField.put("maritalStatusREG04","REG04_SUBJECTS.MARITAL_STATUS");
      attribute2dbField.put("nationalityREG04","REG04_SUBJECTS.NATIONALITY");
      attribute2dbField.put("birthdayREG04","REG04_SUBJECTS.BIRTHDAY");
      attribute2dbField.put("birthplaceREG04","REG04_SUBJECTS.BIRTHPLACE");
      attribute2dbField.put("phoneNumberREG04","REG04_SUBJECTS.PHONE_NUMBER");
      attribute2dbField.put("mobileNumberREG04","REG04_SUBJECTS.MOBILE_NUMBER");
      attribute2dbField.put("faxNumberREG04","REG04_SUBJECTS.FAX_NUMBER");
      attribute2dbField.put("emailAddressREG04","REG04_SUBJECTS.EMAIL_ADDRESS");
      attribute2dbField.put("webSiteREG04","REG04_SUBJECTS.WEB_SITE");
      attribute2dbField.put("lawfulSiteREG04","REG04_SUBJECTS.LAWFUL_SITE");
      attribute2dbField.put("noteREG04","REG04_SUBJECTS.NOTE");

      HashSet pkAttributes = new HashSet();
      pkAttributes.add("companyCodeSys01REG04");
      pkAttributes.add("progressiveREG04");

      String sql = null;
      if (subVO.getSubjectTypeREG04().equals(ApplicationConsts.SUBJECT_ORGANIZATION_CONTACT)) {
        sql =
          "select REG04_SUBJECTS.COMPANY_CODE_SYS01,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,REG04_SUBJECTS.PROGRESSIVE,"+
          "REG04_SUBJECTS.ADDRESS,REG04_SUBJECTS.CITY,REG04_SUBJECTS.PROVINCE,REG04_SUBJECTS.COUNTRY,REG04_SUBJECTS.TAX_CODE,"+
          "REG04_SUBJECTS.SUBJECT_TYPE,REG04_SUBJECTS.ZIP,REG04_SUBJECTS.PHONE_NUMBER,REG04_SUBJECTS.FAX_NUMBER,REG04_SUBJECTS.EMAIL_ADDRESS,"+
          "REG04_SUBJECTS.WEB_SITE,REG04_SUBJECTS.LAWFUL_SITE,REG04_SUBJECTS.NOTE "+
          " from REG04_SUBJECTS where "+
          "REG04_SUBJECTS.COMPANY_CODE_SYS01=? and REG04_SUBJECTS.PROGRESSIVE=?  ";
      }
      else {
        sql =
          "select REG04_SUBJECTS.COMPANY_CODE_SYS01,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,REG04_SUBJECTS.PROGRESSIVE,"+
          "REG04_SUBJECTS.ADDRESS,REG04_SUBJECTS.CITY,REG04_SUBJECTS.PROVINCE,REG04_SUBJECTS.COUNTRY,REG04_SUBJECTS.TAX_CODE,"+
          "REG04_SUBJECTS.SUBJECT_TYPE,REG04_SUBJECTS.ZIP,REG04_SUBJECTS.SEX,REG04_SUBJECTS.MARITAL_STATUS,REG04_SUBJECTS.NATIONALITY,REG04_SUBJECTS.BIRTHDAY,"+
          "REG04_SUBJECTS.BIRTHPLACE,REG04_SUBJECTS.PHONE_NUMBER,REG04_SUBJECTS.MOBILE_NUMBER,REG04_SUBJECTS.FAX_NUMBER,REG04_SUBJECTS.EMAIL_ADDRESS,"+
          "REG04_SUBJECTS.WEB_SITE,REG04_SUBJECTS.NOTE "+
          " from REG04_SUBJECTS where "+
          "REG04_SUBJECTS.COMPANY_CODE_SYS01=? and REG04_SUBJECTS.PROGRESSIVE=? ";
      }

      ArrayList values = new ArrayList();
      values.add(subVO.getCompanyCodeSys01REG04());
      values.add(subVO.getProgressiveREG04());

      // read from REG04 table...
      Response answer = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          subVO.getSubjectTypeREG04().equals(ApplicationConsts.SUBJECT_ORGANIZATION_CONTACT)?OrganizationVO.class:PeopleVO.class,
          "Y",
          "N",
          context,
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching contact detail",ex);
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
