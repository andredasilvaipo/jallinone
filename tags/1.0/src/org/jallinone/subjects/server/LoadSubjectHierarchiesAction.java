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
 * <p>Description: Action class used to fetch subject hierarchies from REG08 table.</p>
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
public class LoadSubjectHierarchiesAction implements Action {


  public LoadSubjectHierarchiesAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadSubjectHierarchies";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();

    Connection conn = null;
    try {
      GridParams gridParams = (GridParams)inputPar;
      String subjectTypeREG08 = (String)gridParams.getOtherGridParams().get(ApplicationConsts.SUBJECT_TYPE);

      // retrieve companies list...
      String functionId = (String)gridParams.getOtherGridParams().get(ApplicationConsts.FUCTION_CODE);
      ArrayList companiesList = ((JAIOUserSessionParameters)userSessionPars).getCompanyBa().getCompaniesList(functionId);
      String companies = "";
      for(int i=0;i<companiesList.size();i++)
        companies += "'"+companiesList.get(i).toString()+"',";
      companies = companies.substring(0,companies.length()-1);

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
          "select REG08_SUBJECT_HIERARCHIES.COMPANY_CODE_SYS01,REG08_SUBJECT_HIERARCHIES.SUBJECT_TYPE,REG08_SUBJECT_HIERARCHIES.PROGRESSIVE_SYS10,REG08_SUBJECT_HIERARCHIES.PROGRESSIVE_HIE02,SYS10_TRANSLATIONS.DESCRIPTION from REG08_SUBJECT_HIERARCHIES,SYS10_TRANSLATIONS where "+
          "REG08_SUBJECT_HIERARCHIES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "REG08_SUBJECT_HIERARCHIES.COMPANY_CODE_SYS01 in ("+companies+") and SUBJECT_TYPE='"+subjectTypeREG08+"'";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01REG08","REG08_SUBJECT_HIERARCHIES.COMPANY_CODE_SYS01");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("progressiveHie02REG08","REG08_SUBJECT_HIERARCHIES.PROGRESSIVE_HIE02");
      attribute2dbField.put("progressiveSys10REG08","REG08_SUBJECT_HIERARCHIES.PROGRESSIVE_SYS10");
      attribute2dbField.put("subjectTypeREG08","REG08_SUBJECT_HIERARCHIES.SUBJECT_TYPE");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);


      // read from REG08 table...
      Response answer = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          SubjectHierarchyVO.class,
          "Y",
          "N",
          context,
          gridParams,
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching subject hierarchies list",ex);
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
