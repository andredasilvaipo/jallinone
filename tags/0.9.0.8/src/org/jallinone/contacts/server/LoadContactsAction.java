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
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.subjects.java.SubjectPK;
import org.jallinone.subjects.java.SubjectVO;
import org.jallinone.subjects.java.Subject;
import org.jallinone.subjects.java.OrganizationVO;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch contacts from REG04 table.</p>
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
public class LoadContactsAction implements Action {

  LoadContactAction contactAction = new LoadContactAction();


  public LoadContactsAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadContacts";
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

      String sql =
          "select REG04_SUBJECTS.COMPANY_CODE_SYS01,REG04_SUBJECTS.PROGRESSIVE,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,REG04_SUBJECTS.PROGRESSIVE_REG04,"+
          "REG04_SUBJECTS.PHONE_NUMBER,REG04_SUBJECTS.SUBJECT_TYPE,REG04_SUBJECTS.CITY,REG04_SUBJECTS.PROVINCE,REG04_SUBJECTS.COUNTRY "+
          "from REG04_SUBJECTS where "+
          "REG04_SUBJECTS.COMPANY_CODE_SYS01 in ("+companies+") and REG04_SUBJECTS.ENABLED='Y'";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01REG04","REG04_SUBJECTS.COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveREG04","REG04_SUBJECTS.PROGRESSIVE");
      attribute2dbField.put("name_1REG04","REG04_SUBJECTS.NAME_1");
      attribute2dbField.put("name_2REG04","REG04_SUBJECTS.NAME_2");
      attribute2dbField.put("phoneNumberREG04","REG04_SUBJECTS.PHONE_NUMBER");
      attribute2dbField.put("subjectTypeREG04","REG04_SUBJECTS.SUBJECT_TYPE");
      attribute2dbField.put("cityREG04","REG04_SUBJECTS.CITY");
      attribute2dbField.put("provinceREG04","REG04_SUBJECTS.PROVINCE");
      attribute2dbField.put("countryREG04","REG04_SUBJECTS.COUNTRY");
      attribute2dbField.put("progressiveReg04REG04","REG04_SUBJECTS.PROGRESSIVE_REG04");

      ArrayList values = new ArrayList();

      if (gridParams.getOtherGridParams().get(ApplicationConsts.SUBJECT_PK)!=null) {
        sql += " and REG04_SUBJECTS.COMPANY_CODE_SYS01_REG04=? "+
               " and REG04_SUBJECTS.PROGRESSIVE_REG04=?";
        SubjectPK pk = (SubjectPK)gridParams.getOtherGridParams().get(ApplicationConsts.SUBJECT_PK);
        values.add(pk.getCompanyCodeSys01REG04());
        values.add(pk.getProgressiveREG04());
      }
      else {
        sql += " and REG04_SUBJECTS.SUBJECT_TYPE in (?,?)";
        values.add(ApplicationConsts.SUBJECT_ORGANIZATION_CONTACT);
        values.add(ApplicationConsts.SUBJECT_PEOPLE_CONTACT);
      }

      // read from REG04 table...
      Response res = QueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          GridContactVO.class,
          "Y",
          "N",
          context,
          gridParams,
          50,
          true
      );

      if (res.isError())
        return res;

      ArrayList rows = ((VOListResponse)res).getRows();
      GridContactVO vo = null;
      Response contactRes = null;
      SubjectVO subVO = null;
      OrganizationVO orgVO = null;
      for(int i=0;i<rows.size();i++) {
        vo = (GridContactVO)rows.get(i);
        if (vo.getProgressiveReg04REG04()!=null) {
          subVO = new SubjectVO(
              vo.getCompanyCodeSys01REG04(),
              vo.getProgressiveReg04REG04(),
              null, // it's not very good...
              null, // it's not very good...
              ApplicationConsts.SUBJECT_ORGANIZATION_CONTACT
          );
          contactRes = contactAction.executeCommand(subVO,userSessionPars,request,response,userSession,context);
          if (contactRes.isError())
            return contactRes;
          orgVO = (OrganizationVO)((VOResponse)contactRes).getVo();
          vo.setOrganizationName_1REG04(orgVO.getName_1REG04());
        }
      }

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

      return answer;
    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching contacts list",ex);
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
