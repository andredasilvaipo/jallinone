package org.jallinone.registers.bank.server;

import org.openswing.swing.server.*;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.message.send.java.LookupValidationParams;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;
import org.jallinone.registers.bank.java.*;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage banks.</p>
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
public class BanksBean  implements Banks {


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




  public BanksBean() {
  }

  

  /**
   * Business logic to execute.
   */
  public VOListResponse validateBankCode(LookupValidationParams validationPars,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      String sql =
          "select REG12_BANKS.BANK_CODE,REG12_BANKS.DESCRIPTION,REG12_BANKS.BBAN,REG12_BANKS.IBAN,REG12_BANKS.ADDRESS,REG12_BANKS.CITY,REG12_BANKS.ZIP,REG12_BANKS.PROVINCE,REG12_BANKS.COUNTRY,REG12_BANKS.ENABLED from REG12_BANKS where "+
          "REG12_BANKS.ENABLED='Y' and "+
          "REG12_BANKS.BANK_CODE='"+validationPars.getCode()+"'";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("bankCodeREG12","REG12_BANKS.BANK_CODE");
      attribute2dbField.put("descriptionREG12","REG12_BANKS.DESCRIPTION");
      attribute2dbField.put("bbanREG12","REG12_BANKS.BBAN");
      attribute2dbField.put("ibanREG12","REG12_BANKS.IBAN");
      attribute2dbField.put("addressREG12","REG12_BANKS.ADDRESS");
      attribute2dbField.put("cityREG12","REG12_BANKS.CITY");
      attribute2dbField.put("zipREG12","REG12_BANKS.ZIP");
      attribute2dbField.put("provinceREG12","REG12_BANKS.PROVINCE");
      attribute2dbField.put("countryREG12","REG12_BANKS.COUNTRY");
      attribute2dbField.put("enabledREG12","REG12_BANKS.ENABLED");


      ArrayList values = new ArrayList();

      GridParams gridParams = new GridParams();

      // read from REG12 table...
      Response answer = CustomizeQueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          BankVO.class,
          "Y",
          "N",
          null,
          gridParams,
          true,
          customizedFields
      );


      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while validating bank code",ex);
      throw new Exception(ex.getMessage());
    }
    finally {
    	try {
    		if (this.conn==null && conn!=null) {
                // close only local connection
                conn.commit();
                conn.close();
            }

    	} catch (Exception ex) {}
    }

  }



  /**
   * Business logic to execute.
   */
  public VOListResponse loadBanks(GridParams gridParams,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      String sql =
          "select REG12_BANKS.BANK_CODE,REG12_BANKS.DESCRIPTION,REG12_BANKS.BBAN,REG12_BANKS.IBAN,REG12_BANKS.ADDRESS,REG12_BANKS.CITY,REG12_BANKS.ZIP,REG12_BANKS.PROVINCE,REG12_BANKS.COUNTRY,REG12_BANKS.ENABLED from REG12_BANKS where "+
          "REG12_BANKS.ENABLED='Y'";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("bankCodeREG12","REG12_BANKS.BANK_CODE");
      attribute2dbField.put("descriptionREG12","REG12_BANKS.DESCRIPTION");
      attribute2dbField.put("bbanREG12","REG12_BANKS.BBAN");
      attribute2dbField.put("ibanREG12","REG12_BANKS.IBAN");
      attribute2dbField.put("addressREG12","REG12_BANKS.ADDRESS");
      attribute2dbField.put("cityREG12","REG12_BANKS.CITY");
      attribute2dbField.put("zipREG12","REG12_BANKS.ZIP");
      attribute2dbField.put("provinceREG12","REG12_BANKS.PROVINCE");
      attribute2dbField.put("countryREG12","REG12_BANKS.COUNTRY");
      attribute2dbField.put("enabledREG12","REG12_BANKS.ENABLED");

      ArrayList values = new ArrayList();


      // read from REG12 table...
      Response answer = CustomizeQueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          BankVO.class,
          "Y",
          "N",
          null,
          gridParams,
          50,
          true,
          customizedFields
      );

      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching banks list",ex);
      throw new Exception(ex.getMessage());
    }
    finally {
    	try {
    		if (this.conn==null && conn!=null) {
                // close only local connection
                conn.commit();
                conn.close();
            }

    	} catch (Exception ex) {}
    }

  }



  
  
  /**
   * Business logic to execute.
   */
  public VOResponse insertBank(BankVO vo,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      vo.setEnabledREG12("Y");

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("bankCodeREG12","BANK_CODE");
      attribute2dbField.put("descriptionREG12","DESCRIPTION");
      attribute2dbField.put("bbanREG12","BBAN");
      attribute2dbField.put("ibanREG12","IBAN");
      attribute2dbField.put("addressREG12","ADDRESS");
      attribute2dbField.put("cityREG12","CITY");
      attribute2dbField.put("zipREG12","ZIP");
      attribute2dbField.put("provinceREG12","PROVINCE");
      attribute2dbField.put("countryREG12","COUNTRY");
      attribute2dbField.put("enabledREG12","ENABLED");

      // insert into REG12...
      Response res = CustomizeQueryUtil.insertTable(
          conn,
          new UserSessionParameters(username),
          vo,
          "REG12_BANKS",
          attribute2dbField,
          "Y",
          "N",
          null,
          true,
          customizedFields
      );
      if (res.isError())
    	  throw new Error(res.getErrorMessage());


      return new VOResponse(vo);
    }
    catch (Throwable ex) {
      Logger.error(username, this.getClass().getName(),
                   "executeCommand", "Error while inserting a new bank", ex);
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

    	} catch (Exception ex) {}
    }

  }



  /**
   * Business logic to execute.
   */
  public VOResponse updateBank(BankVO oldVO,BankVO newVO,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      HashSet pkAttrs = new HashSet();
      pkAttrs.add("bankCodeREG12");

      HashMap attribute2dbField = new HashMap();
      attribute2dbField.put("bankCodeREG12","BANK_CODE");
      attribute2dbField.put("descriptionREG12","DESCRIPTION");
      attribute2dbField.put("bbanREG12","BBAN");
      attribute2dbField.put("ibanREG12","IBAN");
      attribute2dbField.put("addressREG12","ADDRESS");
      attribute2dbField.put("cityREG12","CITY");
      attribute2dbField.put("zipREG12","ZIP");
      attribute2dbField.put("provinceREG12","PROVINCE");
      attribute2dbField.put("countryREG12","COUNTRY");
      attribute2dbField.put("enabledREG12","ENABLED");


      Response res = new CustomizeQueryUtil().updateTable(
            conn,
            new UserSessionParameters(username),
            pkAttrs,
            oldVO,
            newVO,
            "REG12_BANKS",
            attribute2dbField,
            "Y",
            "N",
            null,
            true,
            customizedFields
      );

      return new VOResponse(newVO);
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating an existing bank",ex);
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
  public VOResponse deleteBanks(ArrayList list,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;



      stmt = conn.createStatement();

      BankVO vo = null;
      for(int i=0;i<list.size();i++) {
        // logically delete the record in REG12...
        vo = (BankVO)list.get(i);
        stmt.execute("update REG12_BANKS set ENABLED='N' where BANK_CODE='"+vo.getBankCodeREG12()+"'");
      }

      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing banks",ex);
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

