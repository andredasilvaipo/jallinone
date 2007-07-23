package org.jallinone.employees.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.employees.java.*;
import java.math.BigDecimal;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.subjects.server.PeopleBean;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.internationalization.server.ServerResourcesFactory;
import org.jallinone.system.server.LoadCompanyParamsAction;
import org.jallinone.system.java.CompanyParametersVO;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to insert a new employee in SCH01/REG04 tables.</p>
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
public class InsertEmployeeAction implements Action {

  private InsertEmployeeCalendarsAction insCal = new InsertEmployeeCalendarsAction();
  private LoadCompanyParamsAction loadPars = new LoadCompanyParamsAction();


  public InsertEmployeeAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "insertEmployee";
  }


  private PeopleBean bean = new PeopleBean();



  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,
                                       UserSessionParameters userSessionPars,
                                       HttpServletRequest request,
                                       HttpServletResponse response,
                                       HttpSession userSession,
                                       ServletContext context) {
    Connection conn = null;
    PreparedStatement pstmt = null;
    try {
      // retrieve internationalization settings (Resources object)...
      ServerResourcesFactory factory = (ServerResourcesFactory)context.getAttribute(Controller.RESOURCES_FACTORY);
      String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();

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

      DetailEmployeeVO vo = (DetailEmployeeVO)inputPar;
      vo.setEnabledSCH01("Y");

      String companyCode = ((JAIOUserSessionParameters)userSessionPars).getCompanyBa().getCompaniesList("SCH01").get(0).toString();

      if (vo.getCompanyCodeSys01SCH01()==null)
        vo.setCompanyCodeSys01SCH01(companyCode);
      vo.setCompanyCodeSys01REG04(vo.getCompanyCodeSys01SCH01());
      vo.setSubjectTypeREG04(ApplicationConsts.SUBJECT_EMPLOYEE);

      // check if there exist already an employee with the same code...
      pstmt = conn.prepareStatement(
          "select EMPLOYEE_CODE from SCH01_EMPLOYEES where COMPANY_CODE_SYS01=? and EMPLOYEE_CODE=? and ENABLED='Y'"
      );
      pstmt.setString(1,vo.getCompanyCodeSys01REG04());
      pstmt.setString(2,vo.getEmployeeCodeSCH01());
      ResultSet rset = pstmt.executeQuery();
      if (rset.next()) {
        rset.close();
        return new ErrorResponse(factory.getResources(serverLanguageId).getResource("there is already another employee with the same employee code."));
      }
      rset.close();

      // generate progressiveREG04 and insert into REG04...
      bean.insert(conn,true,vo,userSessionPars,context);
      vo.setProgressiveReg04SCH01(vo.getProgressiveREG04());

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SCH01","COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveReg04SCH01","PROGRESSIVE_REG04");
      attribute2dbField.put("employeeCodeSCH01","EMPLOYEE_CODE");
      attribute2dbField.put("phoneNumberSCH01","PHONE_NUMBER");
      attribute2dbField.put("officeSCH01","OFFICE");
      attribute2dbField.put("taskCodeReg07SCH01","TASK_CODE_REG07");
      attribute2dbField.put("divisionSCH01","DIVISION");
      attribute2dbField.put("emailAddressSCH01","EMAIL_ADDRESS");
      attribute2dbField.put("managerProgressiveReg04SCH01","MANAGER_PROGRESSIVE_REG04");
      attribute2dbField.put("assistantProgressiveReg04SCH01","ASSISTANT_PROGRESSIVE_REG04");
      attribute2dbField.put("salarySCH01","SALARY");
      attribute2dbField.put("levelSCH01","LEVEL");
      attribute2dbField.put("enabledSCH01","ENABLED");
      attribute2dbField.put("engagedDateSCH01","ENGAGED_DATE");
      attribute2dbField.put("dismissalDateSCH01","DISMISSAL_DATE");
      attribute2dbField.put("managerCompanyCodeSys01SCH01","COMPANY_CODE_SYS01_MANAGER_SCH01");
      attribute2dbField.put("assistantCompanyCodeSys01SCH01","COMPANY_CODE_SYS01_ASSISTANT_SCH01");

      // insert into SCH01...
      Response res = CustomizeQueryUtil.insertTable(
          conn,
          userSessionPars,
          vo,
          "SCH01_EMPLOYEES",
          attribute2dbField,
          "Y",
          "N",
          context,
          true,
          new BigDecimal(662) // window identifier...
      );

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

      conn.commit();

      // fires the GenericEvent.AFTER_COMMIT event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        getRequestName(),
        GenericEvent.AFTER_COMMIT,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        inputPar,
        answer
      ));


      // retrieve default company calendar settings...
      Response calRes = loadPars.executeCommand(vo.getCompanyCodeSys01SCH01(),userSessionPars,request,response,userSession,context);
      if (calRes.isError())
        return calRes;
      CompanyParametersVO compCalVO = (CompanyParametersVO)((VOResponse)calRes).getVo();
      Calendar cal = Calendar.getInstance();
      cal.set(cal.DAY_OF_MONTH,1);
      cal.set(cal.MONTH,0);
      cal.set(cal.YEAR,0);
      cal.set(cal.HOUR_OF_DAY,8);
      cal.set(cal.MINUTE,0);
      cal.set(cal.SECOND,0);
      cal.set(cal.MILLISECOND,0);
      Timestamp morningStartHour = compCalVO.getMorningStartHourSCH02();
      if (morningStartHour==null)
        morningStartHour = new java.sql.Timestamp(cal.getTimeInMillis());
      Timestamp morningEndHour = compCalVO.getMorningStartHourSCH02();
      cal.set(cal.HOUR_OF_DAY,12);
      if (morningEndHour==null)
        morningEndHour = new java.sql.Timestamp(cal.getTimeInMillis());
      cal.set(cal.HOUR_OF_DAY,13);
      Timestamp afternoonStartHour = compCalVO.getMorningStartHourSCH02();
      if (afternoonStartHour==null)
        afternoonStartHour= new java.sql.Timestamp(cal.getTimeInMillis());
      cal.set(cal.HOUR_OF_DAY,17);
      Timestamp afternoonEndHour = compCalVO.getMorningStartHourSCH02();
      if (afternoonEndHour==null)
        afternoonEndHour = new java.sql.Timestamp(cal.getTimeInMillis());


      // insert employee calendar, according to the company calendar default settings...
      EmployeeCalendarVO calVO = new EmployeeCalendarVO();
      calVO.setCompanyCodeSys01SCH02(vo.getCompanyCodeSys01SCH01());
      calVO.setProgressiveReg04SCH02(vo.getProgressiveReg04SCH01());
      calVO.setMorningStartHourSCH02(morningStartHour);
      calVO.setMorningEndHourSCH02(morningEndHour);
      calVO.setAfternoonStartHourSCH02(afternoonStartHour);
      calVO.setAfternoonEndHourSCH02(afternoonEndHour);

      calVO.setDayOfWeekSCH02(String.valueOf(Calendar.MONDAY));
      ArrayList list = new ArrayList();
      list.add(calVO);
      calRes = insCal.executeCommand(list,userSessionPars,request,response,userSession,context);
      if (calRes.isError())
        return calRes;

      calVO.setDayOfWeekSCH02(String.valueOf(Calendar.TUESDAY));
      list.clear();
      list.add(calVO);
      calRes = insCal.executeCommand(list,userSessionPars,request,response,userSession,context);
      if (calRes.isError())
        return calRes;

      calVO.setDayOfWeekSCH02(String.valueOf(Calendar.WEDNESDAY));
      list.clear();
      list.add(calVO);
      calRes = insCal.executeCommand(list,userSessionPars,request,response,userSession,context);
      if (calRes.isError())
        return calRes;

      calVO.setDayOfWeekSCH02(String.valueOf(Calendar.THURSDAY));
      list.clear();
      list.add(calVO);
      calRes = insCal.executeCommand(list,userSessionPars,request,response,userSession,context);
      if (calRes.isError())
        return calRes;

      calVO.setDayOfWeekSCH02(String.valueOf(Calendar.FRIDAY));
      list.clear();
      list.add(calVO);
      calRes = insCal.executeCommand(list,userSessionPars,request,response,userSession,context);
      if (calRes.isError())
        return calRes;


      return answer;
    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(), this.getClass().getName(),
                   "executeCommand", "Error while inserting a new employee", ex);
      try {
        conn.rollback();
      }
      catch (Exception ex3) {
      }
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

