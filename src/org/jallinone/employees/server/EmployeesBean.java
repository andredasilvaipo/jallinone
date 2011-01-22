package org.jallinone.employees.server;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.sql.DataSource;

import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.employees.java.DetailEmployeeVO;
import org.jallinone.employees.java.EmployeeCalendarVO;
import org.jallinone.employees.java.GridEmployeeVO;
import org.jallinone.subjects.java.SubjectPK;
import org.jallinone.subjects.server.PeopleBean;
import org.jallinone.system.java.CompanyParametersVO;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.system.server.ParamsBean;
import org.openswing.swing.logger.server.Logger;
import org.openswing.swing.message.receive.java.Response;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.message.receive.java.VOResponse;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.message.send.java.LookupValidationParams;
import org.openswing.swing.server.QueryUtil;
import org.openswing.swing.server.UserSessionParameters;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage employees.</p>
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
public class EmployeesBean  implements Employees {


  private DataSource dataSource; 

  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  /** external connection */
  private Connection conn = null;
  
  /**
   * Set external connection. 
   */
  public void setConn(Connection conn) {
    this.conn = conn;
  }

  /**
   * Create local connection
   */
  public Connection getConn() throws Exception {
    
    Connection c = dataSource.getConnection(); c.setAutoCommit(false); return c;
  }


  private ParamsBean loadPars;

  public void setLoadPars(ParamsBean loadPars) {
    this.loadPars = loadPars;
  }


  private PeopleBean bean;

  public void setBean(PeopleBean bean) {
	  this.bean = bean;
  }
  
  


  private InsertEmployeeCalendarsBean calbean;
  
  public void setCalbean(InsertEmployeeCalendarsBean calbean) {
	  this.calbean = calbean;
  }

  

  public EmployeesBean() {
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public GridEmployeeVO getGridEmployee() {
	  throw new UnsupportedOperationException();
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public EmployeeCalendarVO getEmployeeCalendar() {
	  throw new UnsupportedOperationException();
  }


  /**
   * Business logic to execute.
   */
  public VOResponse loadEmployee(SubjectPK pk,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    PreparedStatement pstmt = null;
    PreparedStatement pstmt2 = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

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
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          DetailEmployeeVO.class,
          "Y",
          "N",
          null,
          true,
          customizedFields
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

      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching employee detail",ex);
      throw new Exception(ex.getMessage());
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
          if (this.conn==null && conn!=null) {
              // close only local connection
              conn.commit();
              conn.close();
          }

      }
      catch (Exception exx) {}
    }

  }



  /**
   * Business logic to execute.
   */
  public VOListResponse loadEmployeeCalendar(GridParams gridParams,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      String sql =
          "select SCH02_EMPLOYEE_CALENDAR.MORNING_START_HOUR,SCH02_EMPLOYEE_CALENDAR.MORNING_END_HOUR,"+
          "SCH02_EMPLOYEE_CALENDAR.AFTERNOON_START_HOUR,SCH02_EMPLOYEE_CALENDAR.AFTERNOON_END_HOUR "+
          "from SCH02_EMPLOYEE_CALENDAR where "+
          "SCH02_EMPLOYEE_CALENDAR.COMPANY_CODE_SYS01=? and "+
          "SCH02_EMPLOYEE_CALENDAR.PROGRESSIVE_REG04=? and "+
          "SCH02_EMPLOYEE_CALENDAR.DAY_OF_WEEK=?";
      pstmt = conn.prepareStatement(sql);

      ArrayList list = new ArrayList();

      pstmt.setObject(1,gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01));
      pstmt.setObject(2,gridParams.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04));
      pstmt.setObject(3,String.valueOf(Calendar.SUNDAY));
      ResultSet rset = pstmt.executeQuery();
      if (rset.next()) {
        EmployeeCalendarVO vo = new EmployeeCalendarVO();
        vo.setCompanyCodeSys01SCH02((String)gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01));
        vo.setProgressiveReg04SCH02((BigDecimal)gridParams.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04));
        vo.setDayOfWeekSCH02(String.valueOf(Calendar.SUNDAY));
        vo.setMorningStartHourSCH02(rset.getTimestamp(1));
        vo.setMorningEndHourSCH02(rset.getTimestamp(2));
        vo.setAfternoonStartHourSCH02(rset.getTimestamp(3));
        vo.setAfternoonEndHourSCH02(rset.getTimestamp(4));
        list.add(vo);
      }
      rset.close();

      pstmt.setObject(1,gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01));
      pstmt.setObject(2,gridParams.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04));
      pstmt.setObject(3,String.valueOf(Calendar.MONDAY));
      rset = pstmt.executeQuery();
      if (rset.next()) {
        EmployeeCalendarVO vo = new EmployeeCalendarVO();
        vo.setCompanyCodeSys01SCH02((String)gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01));
        vo.setProgressiveReg04SCH02((BigDecimal)gridParams.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04));
        vo.setDayOfWeekSCH02(String.valueOf(Calendar.MONDAY));
        vo.setMorningStartHourSCH02(rset.getTimestamp(1));
        vo.setMorningEndHourSCH02(rset.getTimestamp(2));
        vo.setAfternoonStartHourSCH02(rset.getTimestamp(3));
        vo.setAfternoonEndHourSCH02(rset.getTimestamp(4));
        list.add(vo);
      }
      rset.close();

      pstmt.setObject(1,gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01));
      pstmt.setObject(2,gridParams.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04));
      pstmt.setObject(3,String.valueOf(Calendar.TUESDAY));
      rset = pstmt.executeQuery();
      if (rset.next()) {
        EmployeeCalendarVO vo = new EmployeeCalendarVO();
        vo.setCompanyCodeSys01SCH02((String)gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01));
        vo.setProgressiveReg04SCH02((BigDecimal)gridParams.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04));
        vo.setDayOfWeekSCH02(String.valueOf(Calendar.TUESDAY));
        vo.setMorningStartHourSCH02(rset.getTimestamp(1));
        vo.setMorningEndHourSCH02(rset.getTimestamp(2));
        vo.setAfternoonStartHourSCH02(rset.getTimestamp(3));
        vo.setAfternoonEndHourSCH02(rset.getTimestamp(4));
        list.add(vo);
      }
      rset.close();

      pstmt.setObject(1,gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01));
      pstmt.setObject(2,gridParams.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04));
      pstmt.setObject(3,String.valueOf(Calendar.WEDNESDAY));
      rset = pstmt.executeQuery();
      if (rset.next()) {
        EmployeeCalendarVO vo = new EmployeeCalendarVO();
        vo.setCompanyCodeSys01SCH02((String)gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01));
        vo.setProgressiveReg04SCH02((BigDecimal)gridParams.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04));
        vo.setDayOfWeekSCH02(String.valueOf(Calendar.WEDNESDAY));
        vo.setMorningStartHourSCH02(rset.getTimestamp(1));
        vo.setMorningEndHourSCH02(rset.getTimestamp(2));
        vo.setAfternoonStartHourSCH02(rset.getTimestamp(3));
        vo.setAfternoonEndHourSCH02(rset.getTimestamp(4));
        list.add(vo);
      }
      rset.close();

      pstmt.setObject(1,gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01));
      pstmt.setObject(2,gridParams.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04));
      pstmt.setObject(3,String.valueOf(Calendar.THURSDAY));
      rset = pstmt.executeQuery();
      if (rset.next()) {
        EmployeeCalendarVO vo = new EmployeeCalendarVO();
        vo.setCompanyCodeSys01SCH02((String)gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01));
        vo.setProgressiveReg04SCH02((BigDecimal)gridParams.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04));
        vo.setDayOfWeekSCH02(String.valueOf(Calendar.THURSDAY));
        vo.setMorningStartHourSCH02(rset.getTimestamp(1));
        vo.setMorningEndHourSCH02(rset.getTimestamp(2));
        vo.setAfternoonStartHourSCH02(rset.getTimestamp(3));
        vo.setAfternoonEndHourSCH02(rset.getTimestamp(4));
        list.add(vo);
      }
      rset.close();

      pstmt.setObject(1,gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01));
      pstmt.setObject(2,gridParams.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04));
      pstmt.setObject(3,String.valueOf(Calendar.FRIDAY));
      rset = pstmt.executeQuery();
      if (rset.next()) {
        EmployeeCalendarVO vo = new EmployeeCalendarVO();
        vo.setCompanyCodeSys01SCH02((String)gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01));
        vo.setProgressiveReg04SCH02((BigDecimal)gridParams.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04));
        vo.setDayOfWeekSCH02(String.valueOf(Calendar.FRIDAY));
        vo.setMorningStartHourSCH02(rset.getTimestamp(1));
        vo.setMorningEndHourSCH02(rset.getTimestamp(2));
        vo.setAfternoonStartHourSCH02(rset.getTimestamp(3));
        vo.setAfternoonEndHourSCH02(rset.getTimestamp(4));
        list.add(vo);
      }
      rset.close();

      pstmt.setObject(1,gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01));
      pstmt.setObject(2,gridParams.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04));
      pstmt.setObject(3,String.valueOf(Calendar.SATURDAY));
      rset = pstmt.executeQuery();
      if (rset.next()) {
        EmployeeCalendarVO vo = new EmployeeCalendarVO();
        vo.setCompanyCodeSys01SCH02((String)gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01));
        vo.setProgressiveReg04SCH02((BigDecimal)gridParams.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04));
        vo.setDayOfWeekSCH02(String.valueOf(Calendar.SATURDAY));
        vo.setMorningStartHourSCH02(rset.getTimestamp(1));
        vo.setMorningEndHourSCH02(rset.getTimestamp(2));
        vo.setAfternoonStartHourSCH02(rset.getTimestamp(3));
        vo.setAfternoonEndHourSCH02(rset.getTimestamp(4));
        list.add(vo);
      }
      rset.close();

      return new VOListResponse(list,false,list.size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching employee calendar",ex);
      throw new Exception(ex.getMessage());
    }
    finally {
        try {
            pstmt.close();
        }
        catch (Exception exx) {}
        try {
            if (this.conn==null && conn!=null) {
                // close only local connection
                conn.commit();
                conn.close();
            }

        }
        catch (Exception exx) {}
    }


  }




  /**
   * Business logic to execute.
   */
  public VOListResponse loadEmployees(GridParams gridParams,String serverLanguageId,String username,ArrayList companiesList,ArrayList customizedFields) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      String sql =
          "select SCH01_EMPLOYEES.COMPANY_CODE_SYS01,SCH01_EMPLOYEES.PROGRESSIVE_REG04,SCH01_EMPLOYEES.EMPLOYEE_CODE,"+
          "REG07_TASKS.TASK_CODE,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,SYS10_TRANSLATIONS.DESCRIPTION,SCH01_EMPLOYEES.PHONE_NUMBER,SCH01_EMPLOYEES.OFFICE "+
          "from SCH01_EMPLOYEES,SYS10_TRANSLATIONS,REG07_TASKS,REG04_SUBJECTS where "+
          "SCH01_EMPLOYEES.COMPANY_CODE_SYS01=REG07_TASKS.COMPANY_CODE_SYS01 and "+
          "SCH01_EMPLOYEES.TASK_CODE_REG07=REG07_TASKS.TASK_CODE and "+
          "REG07_TASKS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "SCH01_EMPLOYEES.COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 and "+
          "SCH01_EMPLOYEES.PROGRESSIVE_REG04=REG04_SUBJECTS.PROGRESSIVE and "+
          "SCH01_EMPLOYEES.ENABLED='Y'";


      if (gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01)!=null) {
        sql += " and SCH01_EMPLOYEES.COMPANY_CODE_SYS01='"+gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01)+"'";
      }
      else {
        // retrieve companies list...
        String companies = "";
        for(int i=0;i<companiesList.size();i++)
          companies += "'"+companiesList.get(i).toString()+"',";
        companies = companies.substring(0,companies.length()-1);

        sql += " and SCH01_EMPLOYEES.COMPANY_CODE_SYS01 in ("+companies+")";
      }

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SCH01","SCH01_EMPLOYEES.COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveReg04SCH01","SCH01_EMPLOYEES.PROGRESSIVE_REG04");
      attribute2dbField.put("name_1REG04","REG04_SUBJECTS.NAME_1");
      attribute2dbField.put("name_2REG04","REG04_SUBJECTS.NAME_2");
      attribute2dbField.put("phoneNumberSCH01","SCH01_EMPLOYEES.PHONE_NUMBER");
      attribute2dbField.put("officeSCH01","SCH01_EMPLOYEES.OFFICE");
      attribute2dbField.put("employeeCodeSCH01","SCH01_EMPLOYEES.EMPLOYEE_CODE");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("taskCodeReg07SCH01","REG07_TASKS.TASK_CODE");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);

      if (gridParams.getOtherGridParams().get(ApplicationConsts.TASK_CODE_REG07)!=null) {
        sql += " and SCH01_EMPLOYEES.TASK_CODE_REG07='"+gridParams.getOtherGridParams().get(ApplicationConsts.TASK_CODE_REG07)+"'";
      }

      // read from SCH01 table...
      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          GridEmployeeVO.class,
          "Y",
          "N",
          null,
          gridParams,
          50,
          true
      );

      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching employees list",ex);
      throw new Exception(ex.getMessage());
    }
    finally {
        try {
            if (this.conn==null && conn!=null) {
                // close only local connection
                conn.commit();
                conn.close();
            }

        }
        catch (Exception exx) {}
    }


  }



  /**
   * Business logic to execute.
   */
  public VOResponse updateEmployee(DetailEmployeeVO oldVO,DetailEmployeeVO newVO,String t1,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      bean.setConn(conn);
      
      // update REG04 table...
      Response res = bean.update(oldVO,newVO,t1,serverLanguageId,username);
      if (res.isError()) {
        throw new Exception(res.getErrorMessage());
      }

      HashSet pkAttrs = new HashSet();
      pkAttrs.add("companyCodeSys01SCH01");
      pkAttrs.add("progressiveReg04SCH01");

      HashMap attribute2dbField = new HashMap();
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
      attribute2dbField.put("levelSCH01","LEV");
      attribute2dbField.put("enabledSCH01","ENABLED");
      attribute2dbField.put("engagedDateSCH01","ENGAGED_DATE");
      attribute2dbField.put("dismissalDateSCH01","DISMISSAL_DATE");
      attribute2dbField.put("managerCompanyCodeSys01SCH01","COMPANY_CODE_SYS01_MAN_SCH01");
      attribute2dbField.put("assistantCompanyCodeSys01SCH01","COMPANY_CODE_SYS01_ASS_SCH01");


      res = new CustomizeQueryUtil().updateTable(
          conn,
          new UserSessionParameters(username),
          pkAttrs,
          oldVO,
          newVO,
          "SCH01_EMPLOYEES",
          attribute2dbField,
          "Y",
          "N",
          null,
          true,
          customizedFields
      );
      if (res.isError()) {
        throw new Exception(res.getErrorMessage());
      }

      return new VOResponse(newVO);
    }
    catch (Throwable ex) {
    	Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating an existing employee",ex);
    	try {
    		if (this.conn==null && conn!=null)
    			// rollback only local connection
    			conn.rollback();
    	}
    	catch (Exception ex3) {
    	}
    	throw new Exception(ex.getMessage());
    }
    finally {
    	try {
    		bean.setConn(null);
    	} catch (Exception ex) {}
        try {
            if (this.conn==null && conn!=null) {
                // close only local connection
                conn.commit();
                conn.close();
            }

        }
        catch (Exception exx) {}    	
    }

  }



  /**
   * Business logic to execute.
   */
  public VOListResponse updateEmployeeCalendars(ArrayList oldList,ArrayList newList,String serverLanguageId,String username) throws Throwable {
    
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
     EmployeeCalendarVO oldVO = null;
      EmployeeCalendarVO newVO = null;


      HashSet pkAttrs = new HashSet();
      pkAttrs.add("companyCodeSys01SCH02");
      pkAttrs.add("progressiveReg04SCH02");
      pkAttrs.add("dayOfWeekSCH02");

      HashMap attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SCH02","COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveReg04SCH02","PROGRESSIVE_REG04");
      attribute2dbField.put("dayOfWeekSCH02","DAY_OF_WEEK");
      attribute2dbField.put("morningStartHourSCH02","MORNING_START_HOUR");
      attribute2dbField.put("morningEndHourSCH02","MORNING_END_HOUR");
      attribute2dbField.put("afternoonStartHourSCH02","AFTERNOON_START_HOUR");
      attribute2dbField.put("afternoonEndHourSCH02","AFTERNOON_END_HOUR");

      Response res = null;
      for(int i=0;i<newList.size();i++) {
        oldVO = (EmployeeCalendarVO)oldList.get(i);
        newVO = (EmployeeCalendarVO)newList.get(i);

        res = new QueryUtil().updateTable(
            conn,
            new UserSessionParameters(username),
            pkAttrs,
            oldVO,
            newVO,
            "SCH02_EMPLOYEE_CALENDAR",
            attribute2dbField,
            "Y",
            "N",
            null,
            true
        );
        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }

      }

      return new VOListResponse(newList,false,newList.size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating employee calendars",ex);
      try {
    	  if (this.conn==null && conn!=null)
    		  // rollback only local connection
    		  conn.rollback();
      }
      catch (Exception ex3) {
      }
      throw new Exception(ex.getMessage());
    }
    finally {
        try {
            if (this.conn==null && conn!=null) {
                // close only local connection
                conn.commit();
                conn.close();
            }

        }
        catch (Exception exx) {}
    }


  }




  /**
   * Business logic to execute.
   */
  public VOListResponse validateEmployeeCode(LookupValidationParams validationPars,String serverLanguageId,String username,ArrayList companiesList,ArrayList customizedFields) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      String companies = "";
      if (validationPars.getLookupValidationParameters().get(ApplicationConsts.COMPANY_CODE_SYS01)!=null) {
        companies ="'"+validationPars.getLookupValidationParameters().get(ApplicationConsts.COMPANY_CODE_SYS01)+"'";
      }
      else {
        // retrieve companies list...
        for(int i=0;i<companiesList.size();i++)
          companies += "'"+companiesList.get(i).toString()+"',";
        companies = companies.substring(0,companies.length()-1);
      }

      String sql =
          "select SCH01_EMPLOYEES.COMPANY_CODE_SYS01,SCH01_EMPLOYEES.PROGRESSIVE_REG04,SCH01_EMPLOYEES.EMPLOYEE_CODE,"+
          "SCH01_EMPLOYEES.TASK_CODE_REG07,REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,SYS10_TRANSLATIONS.DESCRIPTION,SCH01_EMPLOYEES.PHONE_NUMBER,SCH01_EMPLOYEES.OFFICE "+
          "from SCH01_EMPLOYEES,SYS10_TRANSLATIONS,REG07_TASKS,REG04_SUBJECTS where "+
          "SCH01_EMPLOYEES.COMPANY_CODE_SYS01=REG07_TASKS.COMPANY_CODE_SYS01 and "+
          "SCH01_EMPLOYEES.TASK_CODE_REG07=REG07_TASKS.TASK_CODE and "+
          "REG07_TASKS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "SCH01_EMPLOYEES.COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 and "+
          "SCH01_EMPLOYEES.PROGRESSIVE_REG04=REG04_SUBJECTS.PROGRESSIVE and "+
          "SCH01_EMPLOYEES.ENABLED='Y' and "+
          "SCH01_EMPLOYEES.COMPANY_CODE_SYS01 in ("+companies+") and "+
          "SCH01_EMPLOYEES.EMPLOYEE_CODE='"+validationPars.getCode()+"'";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SCH01","SCH01_EMPLOYEES.COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveReg04SCH01","SCH01_EMPLOYEES.PROGRESSIVE_REG04");
      attribute2dbField.put("name_1REG04","REG04_SUBJECTS.NAME_1");
      attribute2dbField.put("name_2REG04","REG04_SUBJECTS.NAME_2");
      attribute2dbField.put("phoneNumberSCH01","SCH01_EMPLOYEES.PHONE_NUMBER");
      attribute2dbField.put("officeSCH01","SCH01_EMPLOYEES.OFFICE");
      attribute2dbField.put("employeeCodeSCH01","SCH01_EMPLOYEES.EMPLOYEE_CODE");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("taskCodeReg07SCH01","SCH01_EMPLOYEES.TASK_CODE_REG07");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);

      if (validationPars.getLookupValidationParameters().get(ApplicationConsts.TASK_CODE_REG07)!=null) {
        sql += " and SCH01_EMPLOYEES.TASK_CODE_REG07='"+validationPars.getLookupValidationParameters().get(ApplicationConsts.TASK_CODE_REG07)+"'";
      }


      GridParams gridParams = new GridParams();

      // read from SCH01 table...
      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          GridEmployeeVO.class,
          "Y",
          "N",
          null,
          gridParams,
          true
      );

      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while validating employee code",ex);
      try {
    	  if (this.conn==null && conn!=null)
    		  // rollback only local connection
    		  conn.rollback();
      }
      catch (Exception ex3) {
      }

      throw new Exception(ex.getMessage());
    }
    finally {
        try {
            if (this.conn==null && conn!=null) {
                // close only local connection
                conn.commit();
                conn.close();
            }

        }
        catch (Exception exx) {}
    }


  }



  

  /**
   * Business logic to execute.
   */
  public VOResponse insertEmployee(DetailEmployeeVO vo,String t1,String t2,String serverLanguageId,String username,ArrayList companiesList,ArrayList customizedFields) throws Throwable {
    PreparedStatement pstmt = null;
    
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      loadPars.setConn(conn); // use same transaction...
      bean.setConn(conn);
      calbean.setConn(conn);


      vo.setEnabledSCH01("Y");

      String companyCode = companiesList.get(0).toString();

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
        
    	throw new Exception(t1);
      }
      rset.close();

      // generate progressiveREG04 and insert into REG04...
      bean.insert(true,vo,t2,serverLanguageId,username);
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
      attribute2dbField.put("levelSCH01","LEV");
      attribute2dbField.put("enabledSCH01","ENABLED");
      attribute2dbField.put("engagedDateSCH01","ENGAGED_DATE");
      attribute2dbField.put("dismissalDateSCH01","DISMISSAL_DATE");
      attribute2dbField.put("managerCompanyCodeSys01SCH01","COMPANY_CODE_SYS01_MAN_SCH01");
      attribute2dbField.put("assistantCompanyCodeSys01SCH01","COMPANY_CODE_SYS01_ASS_SCH01");

      // insert into SCH01...
      Response res = CustomizeQueryUtil.insertTable(
          conn,
          new UserSessionParameters(username),
          vo,
          "SCH01_EMPLOYEES",
          attribute2dbField,
          "Y",
          "N",
          null,
          true,
          customizedFields
      );

      Response answer = new VOResponse(vo);


      // retrieve default company calendar settings...
      Response calRes = loadPars.loadCompanyParams(vo.getCompanyCodeSys01SCH01(),serverLanguageId,username);
      if (calRes.isError()) {
      	throw new Exception(calRes.getErrorMessage());
      }
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

      
      calRes = calbean.insertEmployeeCalendars(list,serverLanguageId,username);
      if (calRes.isError()) {
        	throw new Exception(calRes.getErrorMessage());
      }
      
      calVO.setDayOfWeekSCH02(String.valueOf(Calendar.TUESDAY));
      list.clear();
      list.add(calVO);
      calRes = calbean.insertEmployeeCalendars(list,serverLanguageId,username);
      if (calRes.isError()) {
    	  throw new Exception(calRes.getErrorMessage());
      }

      calVO.setDayOfWeekSCH02(String.valueOf(Calendar.WEDNESDAY));
      list.clear();
      list.add(calVO);
      calRes = calbean.insertEmployeeCalendars(list,serverLanguageId,username);
      if (calRes.isError()) {
    	  throw new Exception(calRes.getErrorMessage());
      }

      calVO.setDayOfWeekSCH02(String.valueOf(Calendar.THURSDAY));
      list.clear();
      list.add(calVO);
      calRes = calbean.insertEmployeeCalendars(list,serverLanguageId,username);
      if (calRes.isError()) {
    	  throw new Exception(calRes.getErrorMessage());
      }

      calVO.setDayOfWeekSCH02(String.valueOf(Calendar.FRIDAY));
      list.clear();
      list.add(calVO);
      calRes = calbean.insertEmployeeCalendars(list,serverLanguageId,username);
      if (calRes.isError()) {
    	  throw new Exception(calRes.getErrorMessage());
      }

      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username, this.getClass().getName(),
                   "executeCommand", "Error while inserting a new employee", ex);
      try {
    	  if (this.conn==null && conn!=null)
    		  // rollback only local connection
    		  conn.rollback();
      }
      catch (Exception ex3) {
      }
      throw new Exception(ex.getMessage());
    }
    finally {
      try {
        pstmt.close();
      }
      catch (Exception ex2) {
      }
    
      try {
        loadPars.setConn(null);
        bean.setConn(null);
        calbean.setConn(null);
      } catch (Exception ex) {}
      try {
          if (this.conn==null && conn!=null) {
              // close only local connection
              conn.commit();
              conn.close();
          }

      }
      catch (Exception exx) {}      
    }

  }


  

  /**
   * Business logic to execute.
   */
  public VOResponse deleteEmployee(SubjectPK vo,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      stmt = conn.createStatement();

      // logically delete record in SCH01...
      stmt.execute("update SCH01_EMPLOYEES set ENABLED='N' where COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01REG04()+"' and PROGRESSIVE_REG04="+vo.getProgressiveREG04());
      // logically delete record in REG04...
      stmt.execute("update REG04_SUBJECTS set ENABLED='N' where COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01REG04()+"' and PROGRESSIVE="+vo.getProgressiveREG04());

      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting an existing employee",ex);
      try {
    	  if (this.conn==null && conn!=null)
    		  // rollback only local connection
    		  conn.rollback();
      }
      catch (Exception ex3) {
      }
      throw new Exception(ex.getMessage());
    }
    finally {
        try {
            stmt.close();
        }
        catch (Exception exx) {}
        try {
            if (this.conn==null && conn!=null) {
                // close only local connection
                conn.commit();
                conn.close();
            }

        }
        catch (Exception exx) {}
    }


  }



  /**
   * Business logic to execute.
   */
  public VOResponse deleteEmployeeCalendars(ArrayList list,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      stmt = conn.createStatement();

      EmployeeCalendarVO vo = null;
      for(int i=0;i<list.size();i++) {
        vo = (EmployeeCalendarVO)list.get(i);
        // phisically delete record in SCH02...
        stmt.execute(
          "delete from SCH02_EMPLOYEE_CALENDAR where COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01SCH02()+"' and "+
          "PROGRESSIVE_REG04="+vo.getProgressiveReg04SCH02()+" and "+
          "DAY_OF_WEEK='"+vo.getDayOfWeekSCH02()+"'"
        );
      }

      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting a day of week in an employee calendar",ex);
      try {
    	  if (this.conn==null && conn!=null)
    		  // rollback only local connection
    		  conn.rollback();
      }
      catch (Exception ex3) {
      }
      throw new Exception(ex.getMessage());
    }
    finally {
        try {
            stmt.close();
        }
        catch (Exception exx) {}
        try {
            if (this.conn==null && conn!=null) {
                // close only local connection
                conn.commit();
                conn.close();
            }

        }
        catch (Exception exx) {}
    }


  }




}

