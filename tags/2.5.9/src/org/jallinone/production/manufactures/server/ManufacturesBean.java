package org.jallinone.production.manufactures.server;

import org.openswing.swing.server.*;

import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.message.send.java.LookupValidationParams;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.production.manufactures.java.*;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage anufactures.</p>
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
public class ManufacturesBean  implements Manufactures {


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




  public ManufacturesBean() {
  }

  
  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public ManufactureVO getManufacture() {
	  throw new UnsupportedOperationException();
  }



  /**
   * Business logic to execute.
   */
  public VOListResponse loadManufactures(GridParams gridParams,String serverLanguageId,String username,ArrayList companiesList,ArrayList customizedFields) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      // retrieve companies list...
      String companies = "";
      for(int i=0;i<companiesList.size();i++)
        companies += "'"+companiesList.get(i).toString()+"',";
      companies = companies.substring(0,companies.length()-1);

      String sql =
          "select PRO01_MANUFACTURES.COMPANY_CODE_SYS01,SYS10_TRANSLATIONS.DESCRIPTION,PRO01_MANUFACTURES.MANUFACTURE_CODE,"+
          "PRO01_MANUFACTURES.PROGRESSIVE_SYS10 "+
          "from PRO01_MANUFACTURES,SYS10_TRANSLATIONS where "+
          "PRO01_MANUFACTURES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "PRO01_MANUFACTURES.COMPANY_CODE_SYS01 in ("+companies+") and "+
          "PRO01_MANUFACTURES.ENABLED='Y'";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01PRO01","PRO01_MANUFACTURES.COMPANY_CODE_SYS01");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("manufactureCodePRO01","PRO01_MANUFACTURES.MANUFACTURE_CODE");
      attribute2dbField.put("progressiveSys10PRO01","PRO01_MANUFACTURES.PROGRESSIVE_SYS10");


      ArrayList values = new ArrayList();
      values.add(serverLanguageId);


      // read from PRO01 table...
      Response answer = CustomizeQueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          ManufactureVO.class,
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
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching manufactures list",ex);
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
  public VOResponse updateManufacture(ManufactureVO oldVO,ManufactureVO newVO,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      HashSet pkAttrs = new HashSet();
      pkAttrs.add("companyCodeSys01PRO01");
      pkAttrs.add("manufactureCodePRO01");

      HashMap attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01PRO01","COMPANY_CODE_SYS01");
      attribute2dbField.put("manufactureCodePRO01","MANUFACTURE_CODE");
      attribute2dbField.put("progressiveSys10PRO01","PROGRESSIVE_SYS10");
      attribute2dbField.put("enabledPRO01","ENABLED");


      Response res = new CustomizeQueryUtil().updateTable(
            conn,
            new UserSessionParameters(username),
            pkAttrs,
            oldVO,
            newVO,
            "PRO01_MANUFACTURES",
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


      Response answer = res;
      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOResponse)answer;
    }
    catch (Throwable ex) {
    	Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating an existing manufacture",ex);
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
  public VOListResponse validateManufactureCode(LookupValidationParams validationPars,String serverLanguageId,String username,ArrayList companiesList,ArrayList customizedFields) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      // retrieve companies list...
      String companies = "";
      if (validationPars.getLookupValidationParameters().get(ApplicationConsts.COMPANY_CODE_SYS01)!=null)
        companies = "'"+(String)validationPars.getLookupValidationParameters().get(ApplicationConsts.COMPANY_CODE_SYS01)+"'";
      else {
        for (int i = 0; i < companiesList.size(); i++)
          companies += "'" + companiesList.get(i).toString() + "',";
        companies = companies.substring(0, companies.length() - 1);
      }

      String sql =
          "select PRO01_MANUFACTURES.COMPANY_CODE_SYS01,SYS10_TRANSLATIONS.DESCRIPTION,PRO01_MANUFACTURES.MANUFACTURE_CODE,"+
          "PRO01_MANUFACTURES.PROGRESSIVE_SYS10 "+
          "from PRO01_MANUFACTURES,SYS10_TRANSLATIONS where "+
          "PRO01_MANUFACTURES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "PRO01_MANUFACTURES.COMPANY_CODE_SYS01 in ("+companies+") and "+
          "PRO01_MANUFACTURES.ENABLED='Y' and "+
          "PRO01_MANUFACTURES.MANUFACTURE_CODE='"+validationPars.getCode()+"'";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01PRO01","PRO01_MANUFACTURES.COMPANY_CODE_SYS01");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("manufactureCodePRO01","PRO01_MANUFACTURES.MANUFACTURE_CODE");
      attribute2dbField.put("progressiveSys10PRO01","PRO01_MANUFACTURES.PROGRESSIVE_SYS10");


      ArrayList values = new ArrayList();
      values.add(serverLanguageId);

      // read from PRO01 table...
      Response answer = CustomizeQueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          ManufactureVO.class,
          "Y",
          "N",
          null,
          new GridParams(),
          true,
          customizedFields
      );


      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while validating manufacture code",ex);
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
  public VOListResponse insertManufacture(ManufactureVO vo,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      vo.setEnabledPRO01("Y");
      vo.setProgressiveSys10PRO01(TranslationUtils.insertTranslations(vo.getDescriptionSYS10(),vo.getCompanyCodeSys01PRO01(),conn));

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01PRO01","COMPANY_CODE_SYS01");
      attribute2dbField.put("manufactureCodePRO01","MANUFACTURE_CODE");
      attribute2dbField.put("progressiveSys10PRO01","PROGRESSIVE_SYS10");
      attribute2dbField.put("enabledPRO01","ENABLED");

      // insert into PRO01...
      Response res = CustomizeQueryUtil.insertTable(
          conn,
          new UserSessionParameters(username),
          vo,
          "PRO01_MANUFACTURES",
          attribute2dbField,
          "Y",
          "N",
          null,
          true,
          customizedFields
      );

      ArrayList rows = new ArrayList();
      rows.add(vo);

      return new VOListResponse(rows,false,rows.size());
    }
    catch (Throwable ex) {
      Logger.error(username, this.getClass().getName(),
    		  "executeCommand", "Error while inserting a new manufacture", ex);
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
  public VOResponse deleteManufactures(ArrayList list,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      stmt = conn.createStatement();

      ManufactureVO vo = null;
      for(int i=0;i<list.size();i++) {
        // logically delete the record in PRO01...
        vo = (ManufactureVO)list.get(i);
        stmt.execute(
          "update PRO01_MANUFACTURES set ENABLED='N' where "+
          "COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01PRO01()+"' and "+
          "MANUFACTURE_CODE='"+vo.getManufactureCodePRO01()+"'");

      }

      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
    	Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing manufactures",ex);
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

