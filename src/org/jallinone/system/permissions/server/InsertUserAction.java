package org.jallinone.system.permissions.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.system.permissions.java.*;
import java.math.BigDecimal;
import org.jallinone.system.progressives.server.ProgressiveUtils;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.system.companies.server.LoadCompaniesAction;
import org.jallinone.system.companies.java.CompanyVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to insert a new user in SYS03 table.</p>
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
public class InsertUserAction implements Action {

  private LoadCompaniesAction loadCompaniesAction = new LoadCompaniesAction();


  public InsertUserAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "insertUser";
  }

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
      UserVO vo = (UserVO) inputPar;
      vo.setUsernameCreateSYS03(userSessionPars.getUsername());
      vo.setCreateDateSYS03(new java.sql.Date(System.currentTimeMillis()));

      // insert record in SYS03...
      pstmt = conn.prepareStatement(
          "insert into SYS03_USERS(USERNAME,PASSWD,PASSWD_EXPIRATION,LANGUAGE_CODE_SYS09,FIRST_NAME,LAST_NAME,"+
          "COMPANY_CODE_SYS01,PROGRESSIVE_REG04,USERNAME_CREATE,CREATE_DATE) values(?,?,?,?,?,?,?,?,?,?)"
      );
      pstmt.setString(1,vo.getUsernameSYS03());
      pstmt.setString(2,vo.getPasswdSYS03());
      pstmt.setDate(3,vo.getPasswdExpirationSYS03());
      pstmt.setString(4,vo.getLanguageCodeSys09SYS03());
      pstmt.setString(5,vo.getFirstNameSYS03());
      pstmt.setString(6,vo.getLastNameSYS03());
      pstmt.setString(7,vo.getCompanyCodeSys01SYS03());
      pstmt.setBigDecimal(8,vo.getProgressiveReg04SYS03());
      pstmt.setString(9,vo.getUsernameCreateSYS03());
      pstmt.setDate(10,vo.getCreateDateSYS03());
      pstmt.execute();
      pstmt.close();

      if (vo.getOldUsernameSYS03()!=null) {
        // duplicate all old roles associations...
        pstmt.close();
        pstmt = conn.prepareStatement(
            "insert into SYS14_USER_ROLES(PROGRESSIVE_SYS04,USERNAME_SYS03) "+
            "select PROGRESSIVE_SYS04,? from SYS14_USER_ROLES where "+
            "USERNAME_SYS03=?"
        );
        pstmt.setString(1,vo.getUsernameSYS03());
        pstmt.setString(2,vo.getOldUsernameSYS03());
        pstmt.execute();
        pstmt.close();
      }

      // insert into SYS19 default values for accounting, for each company code...

      Response res = loadCompaniesAction.executeCommand(null,userSessionPars,request,response,userSession,context);
      if (!res.isError()) {

        pstmt = conn.prepareStatement(
            "insert into SYS19_USER_PARAMS(COMPANY_CODE_SYS01,USERNAME_SYS03,PARAM_CODE,VALUE) "+
            "select COMPANY_CODE_SYS01,?,PARAM_CODE,VALUE from SYS21_COMPANY_PARAMS where COMPANY_CODE_SYS01=?"
        );

        java.util.List list = ((VOListResponse)res).getRows();
        CompanyVO companyVO = null;
        for(int i=0;i<list.size();i++) {
          companyVO = (CompanyVO)list.get(i);
          pstmt.setString(1,vo.getUsernameSYS03());
          pstmt.setString(2,companyVO.getCompanyCodeSYS01());
          pstmt.execute();


//          pstmt.setString(1,companyVO.getCompanyCodeSYS01());
//          pstmt.setString(2,vo.getUsernameSYS03());
//          pstmt.setString(3,ApplicationConsts.CREDITS_ACCOUNT);
//          pstmt.setString(4,ApplicationConsts.CREDITS_ACCOUNT_VALUE);
//          pstmt.execute();
//
//          pstmt.setString(1,companyVO.getCompanyCodeSYS01());
//          pstmt.setString(2,vo.getUsernameSYS03());
//          pstmt.setString(3,ApplicationConsts.ITEMS_ACCOUNT);
//          pstmt.setString(4,ApplicationConsts.ITEMS_ACCOUNT_VALUE);
//          pstmt.execute();
//
//          pstmt.setString(1,companyVO.getCompanyCodeSYS01());
//          pstmt.setString(2,vo.getUsernameSYS03());
//          pstmt.setString(3,ApplicationConsts.ACTIVITIES_ACCOUNT);
//          pstmt.setString(4,ApplicationConsts.ACTIVITIES_ACCOUNT_VALUE);
//          pstmt.execute();
//
//          pstmt.setString(1,companyVO.getCompanyCodeSYS01());
//          pstmt.setString(2,vo.getUsernameSYS03());
//          pstmt.setString(3,ApplicationConsts.CHARGES_ACCOUNT);
//          pstmt.setString(4,ApplicationConsts.CHARGES_ACCOUNT_VALUE);
//          pstmt.execute();
//
//          pstmt.setString(1,companyVO.getCompanyCodeSYS01());
//          pstmt.setString(2,vo.getUsernameSYS03());
//          pstmt.setString(3,ApplicationConsts.DEBITS_ACCOUNT);
//          pstmt.setString(4,ApplicationConsts.DEBITS_ACCOUNT_VALUE);
//          pstmt.execute();
//
//          pstmt.setString(1,companyVO.getCompanyCodeSYS01());
//          pstmt.setString(2,vo.getUsernameSYS03());
//          pstmt.setString(3,ApplicationConsts.COSTS_ACCOUNT);
//          pstmt.setString(4,ApplicationConsts.COSTS_ACCOUNT_VALUE);
//          pstmt.execute();
//
//          pstmt.setString(1,companyVO.getCompanyCodeSYS01());
//          pstmt.setString(2,vo.getUsernameSYS03());
//          pstmt.setString(3,ApplicationConsts.CASE_ACCOUNT);
//          pstmt.setString(4,ApplicationConsts.CASE_ACCOUNT_VALUE);
//          pstmt.execute();
//
//          pstmt.setString(1,companyVO.getCompanyCodeSYS01());
//          pstmt.setString(2,vo.getUsernameSYS03());
//          pstmt.setString(3,ApplicationConsts.BANK_ACCOUNT);
//          pstmt.setString(4,ApplicationConsts.BANK_ACCOUNT_VALUE);
//          pstmt.execute();
//
//          pstmt.setString(1,companyVO.getCompanyCodeSYS01());
//          pstmt.setString(2,vo.getUsernameSYS03());
//          pstmt.setString(3,ApplicationConsts.VAT_ENDORSE_ACCOUNT);
//          pstmt.setString(4,ApplicationConsts.VAT_ENDORSE_ACCOUNT_VALUE);
//          pstmt.execute();
        }
        pstmt.close();
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

      return answer;
    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(), this.getClass().getName(),
                   "executeCommand", "Error while inserting a new role", ex);
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


  /**
   * Replace the specified pattern with the new one.
   * @param b sql script
   * @param oldPattern pattern to replace
   * @param newPattern new pattern
   * @return sql script with substitutions
   */
  private StringBuffer replace(StringBuffer b,String oldPattern,String newPattern) {
    int i = -1;
    while((i=b.indexOf(oldPattern))!=-1) {
      b.replace(i,i+oldPattern.length(),newPattern);
    }
    return b;
  }


}

