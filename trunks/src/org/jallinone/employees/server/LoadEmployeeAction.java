package org.jallinone.employees.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.employees.java.*;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.commons.server.CustomizeQueryUtil;
import java.math.BigDecimal;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.subjects.java.SubjectPK;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to fetch employee from SCH01/REG04 tables.</p>
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
public class LoadEmployeeAction implements Action {


  public LoadEmployeeAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "loadEmployee";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();

    Connection conn = null;
    PreparedStatement pstmt = null;
    PreparedStatement pstmt2 = null;
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


      String sql =
          "select REG04_SUBJECTS.COMPANY_CODE_SYS01,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,REG04_SUBJECTS.PROGRESSIVE,REG04_SUBJECTS.ADDRESS,REG04_SUBJECTS.CITY,REG04_SUBJECTS.PROVINCE,REG04_SUBJECTS.COUNTRY,REG04_SUBJECTS.TAX_CODE,"+
          "REG04_SUBJECTS.SUBJECT_TYPE,REG04_SUBJECTS.ZIP,REG04_SUBJECTS.SEX,REG04_SUBJECTS.MARITAL_STATUS,REG04_SUBJECTS.NATIONALITY,REG04_SUBJECTS.BIRTHDAY,"+
          "REG04_SUBJECTS.BIRTHPLACE,REG04_SUBJECTS.PHONE_NUMBER,REG04_SUBJECTS.MOBILE_NUMBER,REG04_SUBJECTS.FAX_NUMBER,REG04_SUBJECTS.EMAIL_ADDRESS,"+
          "REG04_SUBJECTS.WEB_SITE,REG04_SUBJECTS.NOTE,"+
          "SCH01_EMPLOYEES.COMPANY_CODE_SYS01,SCH01_EMPLOYEES.PROGRESSIVE_REG04,SCH01_EMPLOYEES.EMPLOYEE_CODE,SYS10_TRANSLATIONS.DESCRIPTION,SCH01_EMPLOYEES.PHONE_NUMBER,SCH01_EMPLOYEES.OFFICE,"+
          "SCH01_EMPLOYEES.TASK_CODE_REG07,SCH01_EMPLOYEES.DIVISION,SCH01_EMPLOYEES.EMAIL_ADDRESS,SCH01_EMPLOYEES.MANAGER_PROGRESSIVE_REG04,"+
          "SCH01_EMPLOYEES.ASSISTANT_PROGRESSIVE_REG04,SCH01_EMPLOYEES.LEV,SCH01_EMPLOYEES.ENABLED,SCH01_EMPLOYEES.ENGAGED_DATE,SCH01_EMPLOYEES.DISMISSAL_DATE,"+
          "SCH01_EMPLOYEES.COMPANY_CODE_SYS01_MAN_SCH01,SCH01_EMPLOYEES.COMPANY_CODE_SYS01_ASS_SCH01,SCH01_EMPLOYEES.SALARY "+
          " from REG04_SUBJECTS,SCH01_EMPLOYEES,REG07_TASKS,SYS10_TRANSLATIONS where "+
          "SCH01_EMPLOYEES.COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 and "+
          "SCH01_EMPLOYEES.PROGRESSIVE_REG04=REG04_SUBJECTS.PROGRESSIVE and "+
          "REG04_SUBJECTS.COMPANY_CODE_SYS01=? and REG04_SUBJECTS.PROGRESSIVE=? and "+
          "SCH01_EMPLOYEES.COMPANY_CODE_SYS01=REG07_TASKS.COMPANY_CODE_SYS01 and "+
          "SCH01_EMPLOYEES.TASK_CODE_REG07=REG07_TASKS.TASK_CODE and "+
          "REG07_TASKS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? ";


      SubjectPK pk = (SubjectPK)inputPar;

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

      attribute2dbField.put("companyCodeSys01SCH01","SCH01_EMPLOYEES.COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveReg04SCH01","SCH01_EMPLOYEES.PROGRESSIVE_REG04");
      attribute2dbField.put("employeeCodeSCH01","SCH01_EMPLOYEES.EMPLOYEE_CODE");
      attribute2dbField.put("taskDescriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("phoneNumberSCH01","SCH01_EMPLOYEES.PHONE_NUMBER");
      attribute2dbField.put("officeSCH01","SCH01_EMPLOYEES.OFFICE");
      attribute2dbField.put("taskCodeReg07SCH01","SCH01_EMPLOYEES.TASK_CODE_REG07");
      attribute2dbField.put("divisionSCH01","SCH01_EMPLOYEES.DIVISION");
      attribute2dbField.put("emailAddressSCH01","SCH01_EMPLOYEES.EMAIL_ADDRESS");
      attribute2dbField.put("managerProgressiveReg04SCH01","SCH01_EMPLOYEES.MANAGER_PROGRESSIVE_REG04");
      attribute2dbField.put("assistantProgressiveReg04SCH01","SCH01_EMPLOYEES.ASSISTANT_PROGRESSIVE_REG04");
      attribute2dbField.put("salarySCH01","SCH01_EMPLOYEES.SALARY");
      attribute2dbField.put("levelSCH01","SCH01_EMPLOYEES.LEV");
      attribute2dbField.put("enabledSCH01","SCH01_EMPLOYEES.ENABLED");
      attribute2dbField.put("engagedDateSCH01","SCH01_EMPLOYEES.ENGAGED_DATE");
      attribute2dbField.put("dismissalDateSCH01","SCH01_EMPLOYEES.DISMISSAL_DATE");
      attribute2dbField.put("managerCompanyCodeSys01SCH01","SCH01_EMPLOYEES.COMPANY_CODE_SYS01_MAN_SCH01");
      attribute2dbField.put("assistantCompanyCodeSys01SCH01","SCH01_EMPLOYEES.COMPANY_CODE_SYS01_ASS_SCH01");

      ArrayList values = new ArrayList();
      values.add(pk.getCompanyCodeSys01REG04());
      values.add(pk.getProgressiveREG04());
      values.add(serverLanguageId);

      // read from SCH01 table...
      Response res = CustomizeQueryUtil.getQuery(
          conn,
          userSessionPars,
          sql,
          values,
          attribute2dbField,
          DetailEmployeeVO.class,
          "Y",
          "N",
          context,
          true,
          new BigDecimal(662) // window identifier...
      );

      if (!res.isError()) {
        DetailEmployeeVO vo = (DetailEmployeeVO)((VOResponse)res).getVo();

        // retrieve first+last name of manager...
        if (vo.getManagerProgressiveReg04SCH01()!=null) {
          pstmt = conn.prepareStatement("select NAME_1,NAME_2 from REG04_SUBJECTS where COMPANY_CODE_SYS01=? and PROGRESSIVE=?");
          pstmt.setString(1,vo.getManagerCompanyCodeSys01SCH01());
          pstmt.setBigDecimal(2,vo.getManagerProgressiveReg04SCH01());
          ResultSet rset = pstmt.executeQuery();
          if (rset.next()) {
            vo.setManagerName_1REG04(rset.getString(1));
            vo.setManagerName_2REG04(rset.getString(2));
          }
          rset.close();
        }

        // retrieve first+last name of assistant...
        if (vo.getAssistantProgressiveReg04SCH01()!=null) {
          pstmt2 = conn.prepareStatement("select NAME_1,NAME_2 from REG04_SUBJECTS where COMPANY_CODE_SYS01=? and PROGRESSIVE=?");
          pstmt2.setString(1,vo.getAssistantCompanyCodeSys01SCH01());
          pstmt2.setBigDecimal(2,vo.getAssistantProgressiveReg04SCH01());
          ResultSet rset = pstmt2.executeQuery();
          if (rset.next()) {
            vo.setAssistantName_1REG04(rset.getString(1));
            vo.setAssistantName_2REG04(rset.getString(2));
          }
          rset.close();
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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while fetching employee detail",ex);
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        pstmt.close();
      }
      catch (Exception ex2) {
      }
      try {
        pstmt2.close();
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
