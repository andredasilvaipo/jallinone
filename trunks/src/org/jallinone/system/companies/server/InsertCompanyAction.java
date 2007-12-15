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
import org.jallinone.subjects.java.OrganizationVO;
import java.math.BigDecimal;
import org.jallinone.system.progressives.server.ProgressiveUtils;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.subjects.server.OrganizationBean;
import org.jallinone.subjects.java.Subject;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to insert a new company in SYS01/REG04 tables.</p>
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
public class InsertCompanyAction implements Action {


  public InsertCompanyAction() {
  }

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "insertCompany";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    Connection conn = null;
    Statement stmt = null;
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
      stmt = conn.createStatement();

      OrganizationVO vo = (OrganizationVO)inputPar;

      // insert record in SYS01...
      pstmt = conn.prepareStatement(
          "insert into SYS01_COMPANIES(COMPANY_CODE,CURRENCY_CODE_REG03,CREATION_DATE,ENABLED) "+
          "VALUES('"+vo.getCompanyCodeSys01REG04()+"','"+vo.getCurrencyCodeReg03()+"',?,'Y')"
      );
      pstmt.setTimestamp(1,new java.sql.Timestamp(System.currentTimeMillis()));
      pstmt.execute();
      pstmt.close();

      vo.setProgressiveREG04(new BigDecimal(2));
      vo.setSubjectTypeREG04(ApplicationConsts.SUBJECT_MY_COMPANY);
      OrganizationBean bean = new OrganizationBean();
      bean.insert(conn,false,vo,userSessionPars,context);

      // add grants of the new company code to ADMIN user...
      stmt.execute(
          "INSERT INTO SYS02_COMPANIES_ACCESS(COMPANY_CODE_SYS01,PROGRESSIVE_SYS04,FUNCTION_CODE_SYS06,CAN_INS,CAN_UPD,CAN_DEL) "+
          "SELECT '"+vo.getCompanyCodeSys01REG04()+"',2,FUNCTION_CODE_SYS06,'Y','Y','Y' FROM SYS07_ROLE_FUNCTIONS,SYS06_FUNCTIONS WHERE FUNCTION_CODE_SYS06=FUNCTION_CODE AND USE_COMPANY_CODE='Y' and SYS07_ROLE_FUNCTIONS.PROGRESSIVE_SYS04=2"
      );
      pstmt.close();

      // insert company description...
      pstmt = conn.prepareStatement(
        "INSERT INTO REG04_SUBJECTS(COMPANY_CODE_SYS01,PROGRESSIVE,NAME_1,SUBJECT_TYPE,ENABLED) "+
        "VALUES(?,2,?,?,'Y')"
      );
      pstmt.setString(1,vo.getCompanyCodeSys01REG04());
      pstmt.setString(2,vo.getName_1REG04());
      pstmt.setString(3,ApplicationConsts.SUBJECT_MY_COMPANY);
      pstmt.execute();
      pstmt.close();

      // retrieve the first company code defined, that will be used to clone data defined per company...
      pstmt = conn.prepareStatement(
        "SELECT COMPANY_CODE FROM SYS01_COMPANIES WHERE ENABLED='Y' ORDER BY CREATION_DATE ASC"
      );
      ResultSet rset = pstmt.executeQuery();
      rset.next();
      String companyCode = rset.getString(1);
      rset.close();
      pstmt.close();


      // insert company report customizations...
      pstmt = conn.prepareStatement(
        "INSERT INTO SYS15_REPORT_CUSTOMIZATIONS(COMPANY_CODE_SYS01,FUNCTION_CODE_SYS06,REPORT_NAME) "+
        "SELECT ?,FUNCTION_CODE_SYS06,REPORT_NAME FROM SYS15_REPORT_CUSTOMIZATIONS WHERE COMPANY_CODE_SYS01=?"
      );
      pstmt.setString(1,vo.getCompanyCodeSys01REG04());
      pstmt.setString(2,companyCode);
      pstmt.execute();
      pstmt.close();


      // insert company user parameters...
      pstmt = conn.prepareStatement(
        "INSERT INTO SYS19_USER_PARAMS(COMPANY_CODE_SYS01,USERNAME_SYS03,PARAM_CODE,VALUE) "+
        "SELECT ?,USERNAME_SYS03,PARAM_CODE,VALUE FROM SYS19_USER_PARAMS WHERE COMPANY_CODE_SYS01=?"
      );
      pstmt.setString(1,vo.getCompanyCodeSys01REG04());
      pstmt.setString(2,companyCode);
      pstmt.execute();
      pstmt.close();


      // insert company user parameters...
      pstmt = conn.prepareStatement(
        "INSERT INTO SYS21_COMPANY_PARAMS(COMPANY_CODE_SYS01,PARAM_CODE,VALUE) "+
        "SELECT DISTINCT ?,PARAM_CODE,VALUE FROM SYS19_USER_PARAMS WHERE COMPANY_CODE_SYS01=?"
      );
      pstmt.setString(1,vo.getCompanyCodeSys01REG04());
      pstmt.setString(2,companyCode);
      pstmt.execute();
      pstmt.close();


      // insert company ledger...
      cloneRecordsAndDescriptions(
          conn,
          "LEDGER_CODE,PROGRESSIVE_SYS10,ENABLED,ACCOUNT_TYPE",
          "ACC01_LEDGER",
          companyCode,
          vo.getCompanyCodeSys01REG04()
      );


      // insert company char of accounts...
      cloneRecordsAndDescriptions(
          conn,
          "ACCOUNT_CODE,LEDGER_CODE_ACC01,PROGRESSIVE_SYS10,ENABLED,ACCOUNT_TYPE,CAN_DEL",
          "ACC02_ACCOUNTS",
          companyCode,
          vo.getCompanyCodeSys01REG04()
      );


      // insert company vat registers...
      cloneRecordsAndDescriptions(
          conn,
          "REGISTER_CODE,PROGRESSIVE_SYS10,REGISTER_TYPE,READ_ONLY,ACCOUNT_CODE_ACC02,ENABLED",
          "ACC04_VAT_REGISTERS",
          companyCode,
          vo.getCompanyCodeSys01REG04()
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

      return answer;
    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while inserting a new company",ex);
      try {
        conn.rollback();
      }
      catch (Exception ex3) {
      }
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        stmt.close();
      }
      catch (Exception ex2) {
      }
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
   * Clone records of "tableName" table having "oldCompanyCode" company code to "newCompanyCode" and
   * clone records in SYS10, too.
   */
  private void cloneRecordsAndDescriptions(Connection conn,String selectFields,String tableName,String oldCompanyCode,String newCompanyCode) throws Exception {
    PreparedStatement pstmt = null;
    PreparedStatement pstmt2 = null;
    PreparedStatement pstmt3 = null;
    try {
      int index = 0;
      int count = 0;
      StringTokenizer st = new StringTokenizer(selectFields,",");
      while(st.hasMoreTokens()) {
        if (st.nextToken().equals("PROGRESSIVE_SYS10"))
          index = count;
        count++;
      }
      String aux = "";
      for(int i=1;i<count;i++)
        aux += "?,";
      aux +="?";

      pstmt = conn.prepareStatement(
        "select "+selectFields+" from "+tableName+" where COMPANY_CODE_SYS01='"+oldCompanyCode+"'"
      );
      pstmt2 = conn.prepareStatement(
        "insert into "+tableName+"(COMPANY_CODE_SYS01,"+selectFields+") values('"+newCompanyCode+"',"+aux+")"
      );
      pstmt3 = conn.prepareStatement(
        "insert into SYS10_TRANSLATIONS(PROGRESSIVE,LANGUAGE_CODE,DESCRIPTION) "+
        "select ?,LANGUAGE_CODE,DESCRIPTION from SYS10_TRANSLATIONS where PROGRESSIVE=?"
      );

      ResultSet rset = pstmt.executeQuery();
      Object oldProgressive = null;
      BigDecimal newProgressive = null;
      while(rset.next()) {
        for(int i=1;i<=count;i++)
          if (i-1!=index)
            pstmt2.setObject(i,rset.getObject(i));
          else {
            oldProgressive = rset.getObject(i);
            newProgressive = ProgressiveUtils.getInternalProgressive("SYS10_TRANSLATIONS","PROGRESSIVE",conn);
            pstmt2.setObject(i,newProgressive);
          }
        pstmt2.execute();

        pstmt3.setBigDecimal(1,newProgressive);
        pstmt3.setObject(2,oldProgressive);
        pstmt3.execute();

      }
      rset.close();

    }
    catch (Exception ex) {
      throw ex;
    }
    finally {
      try {
        pstmt.close();
      }
      catch (Exception ex1) {
      }
      try {
        pstmt2.close();
      }
      catch (Exception ex2) {
      }
      try {
        pstmt3.close();
      }
      catch (Exception ex3) {
      }
    }
  }



}
