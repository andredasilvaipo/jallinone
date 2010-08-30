package org.jallinone.subjects.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.subjects.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;



/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch a list of subjects (customers or contacts) from REG04 table,
 * based on:
 * - the specified COMPANY_CODE
 * - the specified SUBJECT_TYPE
 * - the specified name_1 attribute (optionally)
 * - the specified name_2 attribute (optionally)
 * - the specified PROGRESSIVE_REG04 attribute (optionally).
 * </p>
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
public class LoadSubjectPerNameAction implements Action {


  public LoadSubjectPerNameAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadSubjectPerName";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();

    Connection conn = null;
    try {
      GridParams gridParams = (GridParams)inputPar;

      // retrieve companies list...
      String companies = "";
      if (gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01)!=null) {
        companies = "'"+gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01)+"'";
      }
      else {
        ArrayList companiesList = ((JAIOUserSessionParameters)userSessionPars).getCompanyBa().getCompaniesList("REG04_CONTACTS");
        for(int i=0;i<companiesList.size();i++)
          companies += "'"+companiesList.get(i).toString()+"',";
        companies = companies.substring(0,companies.length()-1);
      }

      String subjectTypeREG04 = (String)gridParams.getOtherGridParams().get(ApplicationConsts.SUBJECT_TYPE); // mandatory
      String name_1REG04 = (String)gridParams.getOtherGridParams().get(ApplicationConsts.NAME_1); // optional
      String name_2REG04 = (String)gridParams.getOtherGridParams().get(ApplicationConsts.NAME_2); // optional
      BigDecimal progressiveREG04 = (BigDecimal)gridParams.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04); // optional

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

      String baseSQL = null;
      if (subjectTypeREG04.equals(ApplicationConsts.SUBJECT_ORGANIZATION) ||
          subjectTypeREG04.equals(ApplicationConsts.SUBJECT_ORGANIZATION_CONTACT) ||
          subjectTypeREG04.equals(ApplicationConsts.SUBJECT_ORGANIZATION_CUSTOMER)) {
        baseSQL =
          "select REG04_SUBJECTS.COMPANY_CODE_SYS01,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,REG04_SUBJECTS.PROGRESSIVE,REG04_SUBJECTS.ADDRESS,"+
          "REG04_SUBJECTS.CITY,REG04_SUBJECTS.PROVINCE,REG04_SUBJECTS.COUNTRY,REG04_SUBJECTS.TAX_CODE,REG04_SUBJECTS.SUBJECT_TYPE,REG04_SUBJECTS.ZIP,"+
          "REG04_SUBJECTS.PHONE_NUMBER,REG04_SUBJECTS.FAX_NUMBER,REG04_SUBJECTS.EMAIL_ADDRESS,"+
          "REG04_SUBJECTS.WEB_SITE,REG04_SUBJECTS.LAWFUL_SITE,REG04_SUBJECTS.NOTE "+
          " from REG04_SUBJECTS where "+
          "REG04_SUBJECTS.COMPANY_CODE_SYS01 in ("+companies+") and "+
          "REG04_SUBJECTS.ENABLED='Y' and "+
          "REG04_SUBJECTS.SUBJECT_TYPE in ('"+ApplicationConsts.SUBJECT_ORGANIZATION_CONTACT+"','"+ApplicationConsts.SUBJECT_ORGANIZATION_CUSTOMER+"')";
      }
      else {
        baseSQL =
          "select REG04_SUBJECTS.COMPANY_CODE_SYS01,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,REG04_SUBJECTS.PROGRESSIVE,REG04_SUBJECTS.ADDRESS,"+
          "REG04_SUBJECTS.CITY,REG04_SUBJECTS.PROVINCE,REG04_SUBJECTS.COUNTRY,REG04_SUBJECTS.TAX_CODE,REG04_SUBJECTS.SUBJECT_TYPE,REG04_SUBJECTS.ZIP,"+
          "REG04_SUBJECTS.SEX,REG04_SUBJECTS.MARITAL_STATUS,REG04_SUBJECTS.NATIONALITY,REG04_SUBJECTS.BIRTHDAY,REG04_SUBJECTS.BIRTHPLACE,"+
          "REG04_SUBJECTS.PHONE_NUMBER,REG04_SUBJECTS.MOBILE_NUMBER,REG04_SUBJECTS.FAX_NUMBER,REG04_SUBJECTS.EMAIL_ADDRESS,"+
          "REG04_SUBJECTS.WEB_SITE,REG04_SUBJECTS.NOTE "+
          " from REG04_SUBJECTS where "+
          "REG04_SUBJECTS.COMPANY_CODE_SYS01 in ("+companies+") and "+
          "REG04_SUBJECTS.ENABLED='Y' and "+
          "REG04_SUBJECTS.SUBJECT_TYPE in ('"+ApplicationConsts.SUBJECT_PEOPLE_CONTACT+"','"+ApplicationConsts.SUBJECT_PEOPLE_CUSTOMER+"')";
      }


      ArrayList values = new ArrayList();

      if (name_1REG04!=null && !name_1REG04.equals("")) {
        baseSQL += " and REG04_SUBJECTS.NAME_1=? ";
        values.add(name_1REG04);
      }
      if (name_2REG04!=null && !name_2REG04.equals("")) {
        baseSQL += " and REG04_SUBJECTS.NAME_2=? ";
        values.add(name_2REG04);
      }
      if (progressiveREG04!=null) {
        baseSQL += " and REG04_SUBJECTS.PROGRESSIVE=? ";
        values.add(progressiveREG04);
      }


      // read from REG04 tables...
      Response answer = QueryUtil.getQuery(
          conn,
          userSessionPars,
          baseSQL,
          values,
          attribute2dbField,
            subjectTypeREG04.equals(ApplicationConsts.SUBJECT_ORGANIZATION) ||
            subjectTypeREG04.equals(ApplicationConsts.SUBJECT_ORGANIZATION_CONTACT) ||
            subjectTypeREG04.equals(ApplicationConsts.SUBJECT_ORGANIZATION_CUSTOMER) ?
            OrganizationVO.class:
            PeopleVO.class,
          "Y",
          "N",
          context,
          gridParams,
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching a subject per name",ex);
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
