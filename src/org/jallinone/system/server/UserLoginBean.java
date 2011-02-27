package org.jallinone.system.server;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

import org.jallinone.system.java.UserLoginVO;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to execute the user authentication: it returns the language identifier associated to the user.</p>
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
public class UserLoginBean implements UserLogin {


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


  public UserLoginBean() {
  }

  
  public UserLoginVO authenticateUser(String username,String password) throws Throwable {
    PreparedStatement pstmt = null;
    Statement stmt = null;
    PreparedStatement pstmt2 = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      pstmt = conn.prepareStatement(
          "select LANGUAGE_CODE_SYS09,PASSWD_EXPIRATION,COMPANY_CODE_SYS01,PROGRESSIVE_REG04,DEF_COMPANY_CODE_SYS01 from SYS03_USERS where "+
          "USERNAME=? and PASSWD=?"
      );
      pstmt.setString(1,username.toUpperCase());
      pstmt.setString(2,password);
      ResultSet rset = pstmt.executeQuery();
      String serverLanguageId = null;
      String companyCodeSys01SYS03 = null;
      BigDecimal progressiveReg04SYS03 = null;
      String defCompanyCodeSys01SYS03 = null;
      if (rset.next()) {

        // verify date expiration...
        java.sql.Date expDate = rset.getDate(2);
        if (expDate!=null && expDate.compareTo(new java.sql.Date(System.currentTimeMillis()))<=0)
          throw new Exception("Account Expired");

        serverLanguageId = rset.getString(1);
        companyCodeSys01SYS03 = rset.getString(3);
        progressiveReg04SYS03 = rset.getBigDecimal(4);
        defCompanyCodeSys01SYS03 = rset.getString(5);

        stmt = conn.createStatement();
        ResultSet rset2 = stmt.executeQuery(
            "select CLIENT_LANGUAGE_CODE from SYS09_LANGUAGES where LANGUAGE_CODE='"+serverLanguageId+"'"
        );
        rset2.next();
        String languageId = rset2.getString(1);
        rset2.close();

        // if progressiveReg04SYS03 is not null, then retrieve employee data...
        String name_1 = null;
        String name_2 = null;
        String empCode = null;
        if (progressiveReg04SYS03!=null) {
          pstmt2 = conn.prepareStatement(
              "select REG04_SUBJECTS.NAME_1,REG04_SUBJECTS.NAME_2,SCH01_EMPLOYEES.EMPLOYEE_CODE from REG04_SUBJECTS,SCH01_EMPLOYEES where "+
              "SCH01_EMPLOYEES.COMPANY_CODE_SYS01=? and "+
              "SCH01_EMPLOYEES.PROGRESSIVE_REG04=? and "+
              "SCH01_EMPLOYEES.COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 and "+
              "SCH01_EMPLOYEES.PROGRESSIVE_REG04=REG04_SUBJECTS.PROGRESSIVE"
          );
          pstmt2.setString(1,companyCodeSys01SYS03);
          pstmt2.setBigDecimal(2,progressiveReg04SYS03);
          rset2 = pstmt2.executeQuery();
          rset2.next();
          name_1 = rset2.getString(1);
          name_2 = rset2.getString(2);
          empCode = rset2.getString(3);
          rset2.close();
          pstmt2.close();
        }


        UserLoginVO userLoginPars = new UserLoginVO();
        userLoginPars.setLanguageId(languageId);
        userLoginPars.setServerLanguageId(serverLanguageId);
        userLoginPars.setProgressiveReg04SYS03(progressiveReg04SYS03);
        userLoginPars.setName_1(name_1);
        userLoginPars.setName_2(name_2);
        userLoginPars.setEmployeeCode(empCode);
        userLoginPars.setCompanyCodeSys01SYS03(companyCodeSys01SYS03);
        userLoginPars.setDefCompanyCodeSys01SYS03(defCompanyCodeSys01SYS03);
        return userLoginPars;
      }
      else
    	  throw new Exception("Account not valid");
    } catch (Exception ex1) {
      ex1.printStackTrace();
      throw new Exception(ex1.getMessage());
    } finally {
      try {
        pstmt.close();
      }
      catch (Exception ex) {
      }
      try {
        stmt.close();
      }
      catch (Exception ex) {
      }
      try {
        pstmt2.close();
      }
      catch (Exception ex) {
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


}

