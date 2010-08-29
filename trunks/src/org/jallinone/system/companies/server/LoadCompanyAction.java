package org.jallinone.system.companies.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.companies.java.CompanyVO;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.subjects.java.SubjectPK;
import org.jallinone.subjects.java.OrganizationVO;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch a specific company from SYS01 table.</p>
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
public class LoadCompanyAction implements Action {


  public LoadCompanyAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadCompany";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();

    Connection conn = null;
    Statement stmt = null;
    try {
      String companyCode = (String)inputPar;

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

      stmt = conn.createStatement();
      ResultSet rset = stmt.executeQuery(
          "select REG04_SUBJECTS.SUBJECT_TYPE,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,REG04_SUBJECTS.ADDRESS,"+
          "REG04_SUBJECTS.CITY,REG04_SUBJECTS.ZIP,REG04_SUBJECTS.PROVINCE,REG04_SUBJECTS.COUNTRY,REG04_SUBJECTS.TAX_CODE,"+
          "REG04_SUBJECTS.PHONE_NUMBER,REG04_SUBJECTS.FAX_NUMBER,REG04_SUBJECTS.EMAIL_ADDRESS,REG04_SUBJECTS.WEB_SITE,"+
          "REG04_SUBJECTS.LAWFUL_SITE,REG04_SUBJECTS.NOTE,SYS01_COMPANIES.CURRENCY_CODE_REG03,REG04_SUBJECTS.PROGRESSIVE_REG04 "+
          "from REG04_SUBJECTS,SYS01_COMPANIES where "+
          "COMPANY_CODE_SYS01='"+companyCode+"' and "+
          "REG04_SUBJECTS.COMPANY_CODE_SYS01=SYS01_COMPANIES.COMPANY_CODE and "+
          "SYS01_COMPANIES.ENABLED='Y' and "+
          "REG04_SUBJECTS.SUBJECT_TYPE='M'"
      );
      OrganizationVO vo = new OrganizationVO();
      if(rset.next()) {
        vo.setCompanyCodeSys01REG04(companyCode);
        vo.setSubjectTypeREG04(rset.getString(1));
        vo.setName_1REG04(rset.getString(2));
        vo.setName_2REG04(rset.getString(3));
        vo.setAddressREG04(rset.getString(4));

        vo.setCityREG04(rset.getString(5));
        vo.setZipREG04(rset.getString(6));
        vo.setProvinceREG04(rset.getString(7));
        vo.setCountryREG04(rset.getString(8));
        vo.setTaxCodeREG04(rset.getString(9));
        vo.setPhoneNumberREG04(rset.getString(10));
        vo.setFaxNumberREG04(rset.getString(11));
        vo.setEmailAddressREG04(rset.getString(12));
        vo.setWebSiteREG04(rset.getString(13));
        vo.setLawfulSiteREG04(rset.getString(14));
        vo.setNoteREG04(rset.getString(15));
        vo.setCurrencyCodeReg03(rset.getString(16));
        vo.setProgressiveREG04(rset.getBigDecimal(17));
        rset.close();

      }
      else {
        rset.close();
        return new ErrorResponse("Record not found.");
      }
      Response answer = new VOResponse(vo);

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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching company detail",ex);
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
