package org.jallinone.system.permissions.server;

import org.openswing.swing.server.*;

import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;
import org.jallinone.system.companies.java.CompanyVO;
import org.jallinone.system.companies.server.CompaniesBean;
import org.jallinone.system.permissions.java.*;
import org.openswing.swing.internationalization.server.*;
import org.openswing.swing.internationalization.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage users.</p>
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
public class UsersBean  implements Users {


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

  private CompaniesBean loadCompaniesAction;

  public void setLoadCompaniesAction(CompaniesBean loadCompaniesAction) {
    this.loadCompaniesAction = loadCompaniesAction;
  }



  public UsersBean() {
  }



  /**
   * Business logic to execute.
   */
  public VOListResponse loadUsers(GridParams pars,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      String sql =
          "select SYS03_USERS.USERNAME,SYS03_USERS.PASSWD,SYS03_USERS.PASSWD_EXPIRATION,SYS03_USERS.LANGUAGE_CODE_SYS09,"+
          "SYS03_USERS.FIRST_NAME,SYS03_USERS.LAST_NAME,SYS03_USERS.COMPANY_CODE_SYS01,SYS03_USERS.PROGRESSIVE_REG04,"+
          "SYS03_USERS.USERNAME_CREATE,SYS03_USERS.CREATE_DATE,SCH01_EMPLOYEES.EMPLOYEE_CODE,SYS03_USERS.DEF_COMPANY_CODE_SYS01 "+
          "from SYS03_USERS LEFT OUTER JOIN "+
          "(select SCH01_EMPLOYEES.COMPANY_CODE_SYS01,SCH01_EMPLOYEES.PROGRESSIVE_REG04,SCH01_EMPLOYEES.EMPLOYEE_CODE "+
          "from SCH01_EMPLOYEES) SCH01_EMPLOYEES ON "+
          "SCH01_EMPLOYEES.COMPANY_CODE_SYS01=SYS03_USERS.COMPANY_CODE_SYS01 and "+
          "SCH01_EMPLOYEES.PROGRESSIVE_REG04=SYS03_USERS.PROGRESSIVE_REG04 ";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SYS03","SYS03_USERS.COMPANY_CODE_SYS01");
      attribute2dbField.put("createDateSYS03","SYS03_USERS.CREATE_DATE");
      attribute2dbField.put("firstNameSYS03","SYS03_USERS.FIRST_NAME");
      attribute2dbField.put("languageCodeSys09SYS03","SYS03_USERS.LANGUAGE_CODE_SYS09");
      attribute2dbField.put("lastNameSYS03","SYS03_USERS.LAST_NAME");
      attribute2dbField.put("passwdExpirationSYS03","SYS03_USERS.PASSWD_EXPIRATION");
      attribute2dbField.put("passwdSYS03","SYS03_USERS.PASSWD");
      attribute2dbField.put("progressiveReg04SYS03","SYS03_USERS.PROGRESSIVE_REG04");
      attribute2dbField.put("usernameCreateSYS03","SYS03_USERS.USERNAME_CREATE");
      attribute2dbField.put("usernameSYS03","SYS03_USERS.USERNAME");
      attribute2dbField.put("employeeCodeSCH01","SCH01_EMPLOYEES.EMPLOYEE_CODE");
      attribute2dbField.put("defCompanyCodeSys01SYS03","SYS03_USERS.DEF_COMPANY_CODE_SYS01");


      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          new ArrayList(),
          attribute2dbField,
          UserVO.class,
          "Y",
          "N",
          null,
          pars,
          50,
          false
      );

      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching users list",ex);
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
  public VOListResponse updateUsers(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      UserVO oldVO = null;
      UserVO newVO = null;
      Response res = null;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SYS03","COMPANY_CODE_SYS01");
      attribute2dbField.put("createDateSYS03","CREATE_DATE");
      attribute2dbField.put("firstNameSYS03","FIRST_NAME");
      attribute2dbField.put("languageCodeSys09SYS03","LANGUAGE_CODE_SYS09");
      attribute2dbField.put("lastNameSYS03","LAST_NAME");
      attribute2dbField.put("passwdExpirationSYS03","PASSWD_EXPIRATION");
      attribute2dbField.put("passwdSYS03","PASSWD");
      attribute2dbField.put("progressiveReg04SYS03","PROGRESSIVE_REG04");
      attribute2dbField.put("usernameCreateSYS03","USERNAME_CREATE");
      attribute2dbField.put("usernameSYS03","USERNAME");
      attribute2dbField.put("defCompanyCodeSys01SYS03","DEF_COMPANY_CODE_SYS01");

      HashSet pkAttrs = new HashSet();
      pkAttrs.add("usernameSYS03");

      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (UserVO)oldVOs.get(i);
        newVO = (UserVO)newVOs.get(i);

        QueryUtil.updateTable(
            conn,
            new UserSessionParameters(username),
            pkAttrs,
            oldVO,
            newVO,
            "SYS03_USERS",
            attribute2dbField,
            "Y",
            "N",
            null,
            false
        );
      }

      return new VOListResponse(newVOs,false,newVOs.size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing users info",ex);
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
  public VOResponse insertUser(UserVO vo,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      loadCompaniesAction.setConn(conn); // use same transaction...

      vo.setUsernameCreateSYS03(username);
      vo.setCreateDateSYS03(new java.sql.Date(System.currentTimeMillis()));

      // insert record in SYS03...
      pstmt = conn.prepareStatement(
          "insert into SYS03_USERS(USERNAME,PASSWD,PASSWD_EXPIRATION,LANGUAGE_CODE_SYS09,FIRST_NAME,LAST_NAME,"+
          "COMPANY_CODE_SYS01,PROGRESSIVE_REG04,USERNAME_CREATE,CREATE_DATE,DEF_COMPANY_CODE_SYS01) values(?,?,?,?,?,?,?,?,?,?,?)"
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
      pstmt.setString(11,vo.getDefCompanyCodeSys01SYS03());
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

      Response res = loadCompaniesAction.loadCompanies(serverLanguageId,username);
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




      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username, this.getClass().getName(),
                   "executeCommand", "Error while inserting a new role", ex);
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
    
      try {
        loadCompaniesAction.setConn(null);
      } catch (Exception ex) {}
    }
    return b;
  }



  /**
   * Business logic to execute.
   */
  public VOResponse deleteUsers(ArrayList users,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      stmt = conn.createStatement();
      UserVO vo = null;

      for(int i=0;i<users.size();i++) {
        vo = (UserVO)users.get(i);

        // phisically remove roles associated to the user (from SYS14 table...)
        stmt.execute("delete from SYS14_USER_ROLES where USERNAME_SYS03='"+vo.getUsernameSYS03()+"'");

        // phisically remove roles associated to the user (from SYS19 table...)
        stmt.execute("delete from SYS19_USER_PARAMS where USERNAME_SYS03='"+vo.getUsernameSYS03()+"'");

        // phisically remove user from SYS03 table...
        stmt.execute("delete from SYS03_USERS where USERNAME='"+vo.getUsernameSYS03()+"'");
      }

      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing users",ex);
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



}

