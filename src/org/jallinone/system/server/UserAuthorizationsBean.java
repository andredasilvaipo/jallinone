package org.jallinone.system.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;

import javax.sql.DataSource;

import org.jallinone.system.customizations.java.WindowCustomizationVO;
import org.jallinone.system.gridmanager.server.JAIODbPermissionsDescriptor;
import org.jallinone.system.java.ButtonCompanyAuthorization;
import org.jallinone.system.java.CustomizedWindows;
import org.openswing.swing.mdi.java.ApplicationFunction;
import org.openswing.swing.permissions.java.ButtonAuthorization;
import org.openswing.swing.table.permissions.java.GridPermissions;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to retrieve function authorizations and buttons authorizations,
 * according to the logged user.</p>
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
public class UserAuthorizationsBean  implements UserAuthorizations {


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



  public UserAuthorizationsBean() {
  }


  /**
   * Business logic to execute.
   */
  public HashMap getUserRoles(String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      stmt = conn.createStatement(); // used for secondary query on SYS02...
      ResultSet rset = null;

      // retrieve user roles...
      HashMap userRoles = new HashMap();
      pstmt = conn.prepareStatement(
          "select SYS04_ROLES.PROGRESSIVE,SYS10_TRANSLATIONS.DESCRIPTION "+
          "from SYS04_ROLES,SYS14_USER_ROLES,SYS10_TRANSLATIONS "+
          "where SYS14_USER_ROLES.USERNAME_SYS03='"+username+"' and "+
          "SYS14_USER_ROLES.PROGRESSIVE_SYS04=SYS04_ROLES.PROGRESSIVE and "+
          "SYS04_ROLES.ENABLED='Y' and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE='"+serverLanguageId+"' and "+
          "SYS10_TRANSLATIONS.PROGRESSIVE=SYS04_ROLES.PROGRESSIVE_SYS10"
      );
      rset = pstmt.executeQuery();
      while(rset.next()) {
        if (rset.getString(2)==null)
        	userRoles.put(rset.getBigDecimal(1),"");
        else
        	userRoles.put(rset.getBigDecimal(1),rset.getString(2));
      }
      rset.close();
      pstmt.close();

      return userRoles;

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
	public String[] getCompanies() throws Throwable {
    PreparedStatement pstmt = null;
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      stmt = conn.createStatement(); // used for secondary query on SYS02...

      ResultSet rset = null;

      // count companies number...
			ArrayList companies = new ArrayList();
      rset = stmt.executeQuery("select COMPANY_CODE from SYS01_COMPANIES where ENABLED='Y'");
      while(rset.next())
        companies.add(rset.getString(1));
      rset.close();

      return (String[])companies.toArray(new String[companies.size()]);
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
  public CustomizedWindows getWindowCustomizations(String langId) throws Throwable {
    PreparedStatement pstmt = null;
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      stmt = conn.createStatement(); // used for secondary query on SYS02...
      ResultSet rset = null;

      // retrieve windows customizations...
      CustomizedWindows cust = new CustomizedWindows();
      rset = stmt.executeQuery(
        "select SYS12_WINDOW_CUSTOMIZATIONS.PROGRESSIVE_SYS13,SYS12_WINDOW_CUSTOMIZATIONS.COLUMN_NAME,SYS12_WINDOW_CUSTOMIZATIONS.COLUMN_TYPE,SYS12_WINDOW_CUSTOMIZATIONS.COLUMN_SIZE,SYS12_WINDOW_CUSTOMIZATIONS.COLUMN_DEC,SYS12_WINDOW_CUSTOMIZATIONS.PROGRESSIVE_SYS10,SYS12_WINDOW_CUSTOMIZATIONS.ATTRIBUTE_NAME,SYS10_TRANSLATIONS.DESCRIPTION,SYS13_WINDOWS.TABLE_NAME from "+
        "SYS12_WINDOW_CUSTOMIZATIONS,SYS10_TRANSLATIONS,SYS13_WINDOWS where "+
        "SYS13_WINDOWS.PROGRESSIVE=SYS12_WINDOW_CUSTOMIZATIONS.PROGRESSIVE_SYS13 and "+
        "SYS10_TRANSLATIONS.LANGUAGE_CODE='"+langId+"' and "+
        "SYS12_WINDOW_CUSTOMIZATIONS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE "+
        "order by SYS12_WINDOW_CUSTOMIZATIONS.PROGRESSIVE_SYS13,SYS12_WINDOW_CUSTOMIZATIONS.PROGRESSIVE_SYS10"
      );
      WindowCustomizationVO vo = null;
      while(rset.next()) {
        vo = new WindowCustomizationVO();
        vo.setProgressiveSys13SYS12(rset.getBigDecimal(1));
        vo.setColumnNameSYS12(rset.getString(2));
        vo.setColumnTypeSYS12(rset.getString(3));
        vo.setColumnSizeSYS12(rset.getBigDecimal(4));
        vo.setColumnDecSYS12(rset.getBigDecimal(5));
        vo.setProgressiveSys10SYS12(rset.getBigDecimal(6));
        vo.setAttributeNameSYS12(rset.getString(7));
        vo.setDescriptionSYS10(rset.getString(8));
        vo.setTableNameSYS13(rset.getString(9));
        cust.addWindowCustomization(vo);
      }
      rset.close();

      return cust;
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
  public HashMap getApplicationPars() throws Throwable {
    PreparedStatement pstmt = null;
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      stmt = conn.createStatement(); // used for secondary query on SYS02...
      ResultSet rset = null;

      // retrieve application parameters from SYS11...
      HashMap applicationPars = new HashMap();
      rset = stmt.executeQuery("select PARAM_CODE,VALUE from SYS11_APPLICATION_PARS");
      while(rset.next()) {
        applicationPars.put(rset.getString(1),rset.getString(2));
      }
      rset.close();

      return applicationPars;

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
  public HashMap getLastGridPermissionsDigests() throws Throwable {
    PreparedStatement pstmt = null;
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      stmt = conn.createStatement(); // used for secondary query on SYS02...
      ResultSet rset = null;


      // retrieve grid digests...
      HashMap lastGridPermissionsDigests = new HashMap();
      rset = stmt.executeQuery(
          "select FUNCTION_CODE_SYS06,DIGEST FROM SYS25_FUNCTIONS_DIGESTS"
      );
      while(rset.next())
        lastGridPermissionsDigests.put(rset.getString(1),rset.getString(2));
      rset.close();

      return lastGridPermissionsDigests;
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
  public ButtonAuthorization[] getButtonsAuthorizations(String serverLanguageId,String username) throws Throwable {
	ArrayList ba = new ArrayList();

    PreparedStatement pstmt = null;
    Statement stmt = null;

    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;


      stmt = conn.createStatement(); // used for secondary query on SYS02...
      ResultSet rset2 = null;

      // retrieve functions...
      pstmt = conn.prepareStatement(
          "select SYS06_FUNCTIONS.FUNCTION_CODE,"+
          "SYS07_ROLE_FUNCTIONS.CAN_INS,SYS07_ROLE_FUNCTIONS.CAN_UPD,SYS07_ROLE_FUNCTIONS.CAN_DEL,"+
          "SYS06_FUNCTIONS.USE_COMPANY_CODE,SYS04_ROLES.PROGRESSIVE "+
          "from SYS06_FUNCTIONS,SYS04_ROLES,SYS07_ROLE_FUNCTIONS,SYS14_USER_ROLES,SYS10_TRANSLATIONS,SYS18_FUNCTION_LINKS "+
          "where SYS14_USER_ROLES.USERNAME_SYS03='"+username+"' and "+
          "SYS18_FUNCTION_LINKS.FUNCTION_CODE_SYS06=SYS06_FUNCTIONS.FUNCTION_CODE and "+
          "SYS14_USER_ROLES.PROGRESSIVE_SYS04=SYS04_ROLES.PROGRESSIVE and "+
          "SYS04_ROLES.PROGRESSIVE=SYS07_ROLE_FUNCTIONS.PROGRESSIVE_SYS04 and "+
          "SYS07_ROLE_FUNCTIONS.FUNCTION_CODE_SYS06=SYS06_FUNCTIONS.FUNCTION_CODE and "+
          "SYS04_ROLES.ENABLED='Y' and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE='"+serverLanguageId+"' and "+
          "SYS10_TRANSLATIONS.PROGRESSIVE=SYS06_FUNCTIONS.PROGRESSIVE_SYS10 order by "+
          "SYS18_FUNCTION_LINKS.PROGRESSIVE_HIE01,SYS18_FUNCTION_LINKS.POS_ORDER"
      );
      ResultSet rset = pstmt.executeQuery();
      while(rset.next()) {
        ba.add(new ButtonAuthorization(
        	rset.getString(1),
            rset.getString(2).equals("Y"),
            rset.getString(3).equals("Y"),
            rset.getString(4).equals("Y")
        ));
      }
      pstmt.close();

      return (ButtonAuthorization[])ba.toArray(new ButtonAuthorization[ba.size()]);

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
  public ButtonCompanyAuthorization[] getButtonCompanyAuthorizations(String serverLanguageId,String username) throws Throwable {
    ArrayList companyBa = new ArrayList();
    PreparedStatement pstmt = null;
    Statement stmt = null;

    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;


      stmt = conn.createStatement(); // used for secondary query on SYS02...
      ResultSet rset2 = null;

      // retrieve functions...
      pstmt = conn.prepareStatement(
          "select SYS06_FUNCTIONS.FUNCTION_CODE,SYS04_ROLES.PROGRESSIVE "+
          "from SYS06_FUNCTIONS,SYS04_ROLES,SYS07_ROLE_FUNCTIONS,SYS14_USER_ROLES,SYS10_TRANSLATIONS,SYS18_FUNCTION_LINKS "+
          "where SYS14_USER_ROLES.USERNAME_SYS03='"+username+"' and "+
          "SYS18_FUNCTION_LINKS.FUNCTION_CODE_SYS06=SYS06_FUNCTIONS.FUNCTION_CODE and "+
          "SYS14_USER_ROLES.PROGRESSIVE_SYS04=SYS04_ROLES.PROGRESSIVE and "+
          "SYS04_ROLES.PROGRESSIVE=SYS07_ROLE_FUNCTIONS.PROGRESSIVE_SYS04 and "+
          "SYS07_ROLE_FUNCTIONS.FUNCTION_CODE_SYS06=SYS06_FUNCTIONS.FUNCTION_CODE and "+
          "SYS04_ROLES.ENABLED='Y' and "+
          "SYS06_FUNCTIONS.USE_COMPANY_CODE='Y' and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE='"+serverLanguageId+"' and "+
          "SYS10_TRANSLATIONS.PROGRESSIVE=SYS06_FUNCTIONS.PROGRESSIVE_SYS10 order by "+
          "SYS18_FUNCTION_LINKS.PROGRESSIVE_HIE01,SYS18_FUNCTION_LINKS.POS_ORDER"
      );
      ResultSet rset = pstmt.executeQuery();
      while(rset.next()) {
    	  // authorizations defined at company level...
    	  rset2 = stmt.executeQuery(
    	    "select SYS02_COMPANIES_ACCESS.COMPANY_CODE_SYS01,SYS02_COMPANIES_ACCESS.CAN_INS,SYS02_COMPANIES_ACCESS.CAN_UPD,SYS02_COMPANIES_ACCESS.CAN_DEL from SYS02_COMPANIES_ACCESS,SYS01_COMPANIES "+
    	    "where SYS02_COMPANIES_ACCESS.PROGRESSIVE_SYS04="+rset.getInt(2)+" and SYS02_COMPANIES_ACCESS.FUNCTION_CODE_SYS06='"+rset.getString(1)+"' and "+
				  "SYS02_COMPANIES_ACCESS.COMPANY_CODE_SYS01=SYS01_COMPANIES.COMPANY_CODE and "+
   				"SYS01_COMPANIES.ENABLED='Y' ");
    	  while(rset2.next()) {
    		  companyBa.add(new ButtonCompanyAuthorization(
    				  rset.getString(1),
    				  rset2.getString(1),
    				  rset2.getString(2).equals("Y"),
    				  rset2.getString(3).equals("Y"),
    				  rset2.getString(4).equals("Y")
    		  ));
    	  }
    	  rset2.close();
      }
      pstmt.close();

      return (ButtonCompanyAuthorization[])companyBa.toArray(new ButtonCompanyAuthorization[companyBa.size()]);

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

