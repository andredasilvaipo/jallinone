package org.jallinone.warehouse.server;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

import javax.sql.DataSource;

import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.system.progressives.server.CompanyProgressiveUtils;
import org.jallinone.system.server.JAIOUserSessionParameters;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.warehouse.java.StoredSerialNumberVO;
import org.jallinone.warehouse.java.WarehousePK;
import org.jallinone.warehouse.java.WarehouseVO;
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
 * * <p>Description: Bean used to manage warehouses.</p>
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
public class WarehousesBean  implements Warehouses {


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




  public WarehousesBean() {
  }




  /**
   * Business logic to execute.
   */
  public VOResponse insertWarehouse(WarehouseVO vo,String serverLanguageId,String username,ArrayList companiesList,ArrayList customizedFields) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      String companyCode = companiesList.get(0).toString();

      vo.setEnabledWAR01("Y");
      if (vo.getCompanyCodeSys01WAR01()==null)
        vo.setCompanyCodeSys01WAR01(companyCode);

      // generate PROGRESSIVE_HIE02 value...
      stmt = conn.createStatement();
      BigDecimal progressiveHIE02 = CompanyProgressiveUtils.getInternalProgressive(vo.getCompanyCodeSys01WAR01(),"HIE02_HIERARCHIES","PROGRESSIVE",conn);
      BigDecimal progressiveHIE01 = TranslationUtils.insertTranslations("",vo.getCompanyCodeSys01WAR01(),conn); // the root has no description as default...
      stmt.execute("INSERT INTO HIE02_HIERARCHIES(PROGRESSIVE,COMPANY_CODE_SYS01,ENABLED) VALUES("+progressiveHIE02+",'"+vo.getCompanyCodeSys01WAR01()+"','Y')");
      stmt.execute("INSERT INTO HIE01_LEVELS(PROGRESSIVE,PROGRESSIVE_HIE02,LEV,ENABLED) VALUES("+progressiveHIE01+","+progressiveHIE02+",0,'Y')");
      stmt.execute("UPDATE HIE02_HIERARCHIES SET PROGRESSIVE_HIE01="+progressiveHIE01+" WHERE PROGRESSIVE="+progressiveHIE02);
      vo.setProgressiveHie02WAR01(progressiveHIE02);

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01WAR01","COMPANY_CODE_SYS01");
      attribute2dbField.put("warehouseCodeWAR01","WAREHOUSE_CODE");
      attribute2dbField.put("descriptionWAR01","DESCRIPTION");
      attribute2dbField.put("progressiveHie02WAR01","PROGRESSIVE_HIE02");
      attribute2dbField.put("addressWAR01","ADDRESS");
      attribute2dbField.put("zipWAR01","ZIP");
      attribute2dbField.put("cityWAR01","CITY");
      attribute2dbField.put("provinceWAR01","PROVINCE");
      attribute2dbField.put("countryWAR01","COUNTRY");
      attribute2dbField.put("progressiveSys04WAR01","PROGRESSIVE_SYS04");
      attribute2dbField.put("enabledWAR01","ENABLED");

      // insert into WAR01...
      Response res = CustomizeQueryUtil.insertTable(
          conn,
          new UserSessionParameters(username),
          vo,
          "WAR01_WAREHOUSES",
          attribute2dbField,
          "Y",
          "N",
          null,
          true,
          customizedFields
      );

      Response answer = res;

      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while inserting a new warehouse",ex);
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
  public VOListResponse loadStoredSerialNumbers(GridParams gridPars,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      String companyCodeSys01 = (String)gridPars.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01);
      BigDecimal progressiveHie01 = (BigDecimal)gridPars.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_HIE01);

      ArrayList params = new ArrayList();
      params.add(companyCodeSys01);
      String sql =
          "select WAR05_STORED_SERIAL_NUMBERS.COMPANY_CODE_SYS01,WAR05_STORED_SERIAL_NUMBERS.SERIAL_NUMBER,"+
          "WAR05_STORED_SERIAL_NUMBERS.ITEM_CODE_ITM01,WAR05_STORED_SERIAL_NUMBERS.PROGRESSIVE_HIE01,"+
          "WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM06,WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM11,"+
          "WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM07,WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM12,"+
          "WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM08,WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM13,"+
          "WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM09,WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM14,"+
          "WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM10,WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM15 "+
          "from "+
          "WAR05_STORED_SERIAL_NUMBERS where "+
          "WAR05_STORED_SERIAL_NUMBERS.COMPANY_CODE_SYS01=? ";
      if (progressiveHie01!=null) {
        sql += " and WAR05_STORED_SERIAL_NUMBERS.PROGRESSIVE_HIE01=? ";
        params.add(progressiveHie01);
      }

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01WAR05","WAR05_STORED_SERIAL_NUMBERS.COMPANY_CODE_SYS01");
      attribute2dbField.put("serialNumberWAR05","WAR05_STORED_SERIAL_NUMBERS.SERIAL_NUMBER");
      attribute2dbField.put("itemCodeItm01WAR05","WAR05_STORED_SERIAL_NUMBERS.ITEM_CODE_ITM01");
      attribute2dbField.put("progressiveHie01WAR05","WAR05_STORED_SERIAL_NUMBERS.PROGRESSIVE_HIE01");
      attribute2dbField.put("variantTypeItm06WAR05","WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM06");
      attribute2dbField.put("variantCodeItm11WAR05","WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM11");
      attribute2dbField.put("variantTypeItm07WAR05","WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM07");
      attribute2dbField.put("variantCodeItm12WAR05","WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM12");
      attribute2dbField.put("variantTypeItm08WAR05","WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM08");
      attribute2dbField.put("variantCodeItm13WAR05","WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM13");
      attribute2dbField.put("variantTypeItm09WAR05","WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM09");
      attribute2dbField.put("variantCodeItm14WAR05","WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM14");
      attribute2dbField.put("variantTypeItm10WAR05","WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM10");
      attribute2dbField.put("variantCodeItm15WAR05","WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM15");


      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          params,
          attribute2dbField,
          StoredSerialNumberVO.class,
          "Y",
          "N",
          null,
          gridPars,
          true
      );



      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching serial mumbers list",ex);
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
  public VOResponse loadWarehouse(WarehousePK pk,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {

    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01WAR01","WAR01_WAREHOUSES.COMPANY_CODE_SYS01");
      attribute2dbField.put("warehouseCodeWAR01","WAR01_WAREHOUSES.WAREHOUSE_CODE");
      attribute2dbField.put("descriptionWAR01","WAR01_WAREHOUSES.DESCRIPTION");
      attribute2dbField.put("progressiveHie02WAR01","WAR01_WAREHOUSES.PROGRESSIVE_HIE02");
      attribute2dbField.put("progressiveHie01HIE02","HIE02_HIERARCHIES.PROGRESSIVE_HIE01");
      attribute2dbField.put("addressWAR01","WAR01_WAREHOUSES.ADDRESS");
      attribute2dbField.put("zipWAR01","WAR01_WAREHOUSES.ZIP");
      attribute2dbField.put("cityWAR01","WAR01_WAREHOUSES.CITY");
      attribute2dbField.put("provinceWAR01","WAR01_WAREHOUSES.PROVINCE");
      attribute2dbField.put("countryWAR01","WAR01_WAREHOUSES.COUNTRY");
      attribute2dbField.put("progressiveSys04WAR01","WAR01_WAREHOUSES.PROGRESSIVE_SYS04");
      attribute2dbField.put("name_1REG04","REG04_SUBJECTS.NAME_1");

      HashSet pkAttributes = new HashSet();
      pkAttributes.add("companyCodeSys01WAR01");
      pkAttributes.add("warehouseCodeWAR01");

      String baseSQL =
          "select WAR01_WAREHOUSES.COMPANY_CODE_SYS01,WAR01_WAREHOUSES.WAREHOUSE_CODE,WAR01_WAREHOUSES.DESCRIPTION,"+
          "WAR01_WAREHOUSES.PROGRESSIVE_HIE02,WAR01_WAREHOUSES.ADDRESS,WAR01_WAREHOUSES.ZIP,WAR01_WAREHOUSES.CITY,"+
          "WAR01_WAREHOUSES.PROVINCE,WAR01_WAREHOUSES.COUNTRY,WAR01_WAREHOUSES.PROGRESSIVE_SYS04,REG04_SUBJECTS.NAME_1,"+
          "HIE02_HIERARCHIES.PROGRESSIVE_HIE01 from "+
          "WAR01_WAREHOUSES,REG04_SUBJECTS,HIE02_HIERARCHIES where "+
          "WAR01_WAREHOUSES.COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 and "+
          "REG04_SUBJECTS.SUBJECT_TYPE='M' and "+
          "WAR01_WAREHOUSES.COMPANY_CODE_SYS01=? and "+
          "WAR01_WAREHOUSES.WAREHOUSE_CODE=? and "+
          "WAR01_WAREHOUSES.PROGRESSIVE_HIE02=HIE02_HIERARCHIES.PROGRESSIVE";

      ArrayList values = new ArrayList();
      values.add(pk.getCompanyCodeSys01WAR01());
      values.add(pk.getWarehouseCodeWAR01());

      // read from WAR01 table...
      Response res = CustomizeQueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          baseSQL,
          values,
          attribute2dbField,
          WarehouseVO.class,
          "Y",
          "N",
          null,
          true,
          customizedFields
      );

      Response answer = res;
      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching an existing warehouse",ex);
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
  public VOListResponse loadWarehouses(GridParams gridPars,String serverLanguageId,String username,ArrayList companiesList) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

//      // retrieve roles list...
//      Iterator it = ((JAIOUserSessionParameters)userSessionPars).getUserRoles().keySet().iterator();
//      String roles = "";
//      while(it.hasNext())
//        roles += it.next().toString()+",";
//      roles = roles.substring(0,roles.length()-1);


      // retrieve companies list...
      String companies = "";
      if (gridPars.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01)!=null) {
        companies = "'"+gridPars.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01)+"'";
      }
      else {
        for(int i=0;i<companiesList.size();i++)
          companies += "'"+companiesList.get(i).toString()+"',";
        companies = companies.substring(0,companies.length()-1);
      }

      String sql =
          "select WAR01_WAREHOUSES.COMPANY_CODE_SYS01,WAR01_WAREHOUSES.WAREHOUSE_CODE,WAR01_WAREHOUSES.DESCRIPTION,"+
          "WAR01_WAREHOUSES.PROGRESSIVE_HIE02,WAR01_WAREHOUSES.ADDRESS,WAR01_WAREHOUSES.ZIP,WAR01_WAREHOUSES.CITY,"+
          "WAR01_WAREHOUSES.PROVINCE,WAR01_WAREHOUSES.COUNTRY,WAR01_WAREHOUSES.PROGRESSIVE_SYS04,REG04_SUBJECTS.NAME_1,"+
          "HIE02_HIERARCHIES.PROGRESSIVE_HIE01 from "+
          "WAR01_WAREHOUSES,REG04_SUBJECTS,HIE02_HIERARCHIES where "+
          "WAR01_WAREHOUSES.COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 and "+
          "REG04_SUBJECTS.SUBJECT_TYPE='M' and "+
          "WAR01_WAREHOUSES.COMPANY_CODE_SYS01 in ("+companies+") and "+
          "WAR01_WAREHOUSES.ENABLED='Y' and "+
          "WAR01_WAREHOUSES.PROGRESSIVE_HIE02=HIE02_HIERARCHIES.PROGRESSIVE";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01WAR01","WAR01_WAREHOUSES.COMPANY_CODE_SYS01");
      attribute2dbField.put("warehouseCodeWAR01","WAR01_WAREHOUSES.WAREHOUSE_CODE");
      attribute2dbField.put("descriptionWAR01","WAR01_WAREHOUSES.DESCRIPTION");
      attribute2dbField.put("progressiveHie02WAR01","WAR01_WAREHOUSES.PROGRESSIVE_HIE02");
      attribute2dbField.put("progressiveHie01HIE02","HIE02_HIERARCHIES.PROGRESSIVE_HIE01");
      attribute2dbField.put("addressWAR01","WAR01_WAREHOUSES.ADDRESS");
      attribute2dbField.put("zipWAR01","WAR01_WAREHOUSES.ZIP");
      attribute2dbField.put("cityWAR01","WAR01_WAREHOUSES.CITY");
      attribute2dbField.put("provinceWAR01","WAR01_WAREHOUSES.PROVINCE");
      attribute2dbField.put("countryWAR01","WAR01_WAREHOUSES.COUNTRY");
      attribute2dbField.put("progressiveSys04WAR01","WAR01_WAREHOUSES.PROGRESSIVE_SYS04");
      attribute2dbField.put("name_1REG04","REG04_SUBJECTS.NAME_1");

      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          new ArrayList(),
          attribute2dbField,
          WarehouseVO.class,
          "Y",
          "N",
          null,
          gridPars,
          true
      );
      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching warehouses list",ex);
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
  public VOResponse updateWarehouse(WarehouseVO oldVO,WarehouseVO newVO,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01WAR01","COMPANY_CODE_SYS01");
      attribute2dbField.put("warehouseCodeWAR01","WAREHOUSE_CODE");
      attribute2dbField.put("descriptionWAR01","DESCRIPTION");
      attribute2dbField.put("progressiveHie02WAR01","PROGRESSIVE_HIE02");
      attribute2dbField.put("addressWAR01","ADDRESS");
      attribute2dbField.put("zipWAR01","ZIP");
      attribute2dbField.put("cityWAR01","CITY");
      attribute2dbField.put("provinceWAR01","PROVINCE");
      attribute2dbField.put("countryWAR01","COUNTRY");
      attribute2dbField.put("progressiveSys04WAR01","PROGRESSIVE_SYS04");

      HashSet pkAttributes = new HashSet();
      pkAttributes.add("companyCodeSys01WAR01");
      pkAttributes.add("warehouseCodeWAR01");

      // update WAR01 table...
      Response res = CustomizeQueryUtil.updateTable(
          conn,
          new UserSessionParameters(username),
          pkAttributes,
          oldVO,
          newVO,
          "WAR01_WAREHOUSES",
          attribute2dbField,
          "Y",
          "N",
          null,
          true,
          customizedFields
      );

      Response answer = res;

      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating an existing warehouse",ex);
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
  public VOListResponse validateStoredSerialNumber(LookupValidationParams pars,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      String companyCodeSys01 = (String)pars.getLookupValidationParameters().get(ApplicationConsts.COMPANY_CODE_SYS01);
      BigDecimal progressiveHie01 = (BigDecimal)pars.getLookupValidationParameters().get(ApplicationConsts.PROGRESSIVE_HIE01);

      ArrayList params = new ArrayList();
      params.add(companyCodeSys01);
      String sql =
          "select WAR05_STORED_SERIAL_NUMBERS.COMPANY_CODE_SYS01,WAR05_STORED_SERIAL_NUMBERS.SERIAL_NUMBER,"+
          "WAR05_STORED_SERIAL_NUMBERS.ITEM_CODE_ITM01,WAR05_STORED_SERIAL_NUMBERS.PROGRESSIVE_HIE01,"+
          "WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM06,WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM11,"+
          "WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM07,WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM12,"+
          "WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM08,WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM13,"+
          "WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM09,WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM14,"+
          "WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM10,WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM15 "+
          "from "+
          "WAR05_STORED_SERIAL_NUMBERS where "+
          "WAR05_STORED_SERIAL_NUMBERS.COMPANY_CODE_SYS01=? ";
      if (progressiveHie01!=null) {
        sql += " and WAR05_STORED_SERIAL_NUMBERS.PROGRESSIVE_HIE01=? ";
        params.add(progressiveHie01);
      }
      sql += " and WAR05_STORED_SERIAL_NUMBERS.SERIAL_NUMBER=? ";
      params.add(pars.getCode());

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01WAR05","WAR05_STORED_SERIAL_NUMBERS.COMPANY_CODE_SYS01");
      attribute2dbField.put("serialNumberWAR05","WAR05_STORED_SERIAL_NUMBERS.SERIAL_NUMBER");
      attribute2dbField.put("itemCodeItm01WAR05","WAR05_STORED_SERIAL_NUMBERS.ITEM_CODE_ITM01");
      attribute2dbField.put("progressiveHie01WAR05","WAR05_STORED_SERIAL_NUMBERS.PROGRESSIVE_HIE01");
      attribute2dbField.put("variantTypeItm06WAR05","WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM06");
      attribute2dbField.put("variantCodeItm11WAR05","WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM11");
      attribute2dbField.put("variantTypeItm07WAR05","WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM07");
      attribute2dbField.put("variantCodeItm12WAR05","WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM12");
      attribute2dbField.put("variantTypeItm08WAR05","WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM08");
      attribute2dbField.put("variantCodeItm13WAR05","WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM13");
      attribute2dbField.put("variantTypeItm09WAR05","WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM09");
      attribute2dbField.put("variantCodeItm14WAR05","WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM14");
      attribute2dbField.put("variantTypeItm10WAR05","WAR05_STORED_SERIAL_NUMBERS.VARIANT_TYPE_ITM10");
      attribute2dbField.put("variantCodeItm15WAR05","WAR05_STORED_SERIAL_NUMBERS.VARIANT_CODE_ITM15");


      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          params,
          attribute2dbField,
          StoredSerialNumberVO.class,
          "Y",
          "N",
          null,
          new GridParams(),
          true
      );
      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while validating serial number",ex);
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
  public VOListResponse validateWarehouseCode(LookupValidationParams pars,String serverLanguageId,String username,ArrayList companiesList) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

//      // retrieve roles list...
//      Iterator it = ((JAIOUserSessionParameters)userSessionPars).getUserRoles().keySet().iterator();
//      String roles = "";
//      while(it.hasNext())
//        roles += it.next().toString()+",";
//      roles = roles.substring(0,roles.length()-1);


      // retrieve companies list...
      String companies = "";
      if (pars.getLookupValidationParameters().get(ApplicationConsts.COMPANY_CODE_SYS01)!=null) {
        companies = "'"+pars.getLookupValidationParameters().get(ApplicationConsts.COMPANY_CODE_SYS01)+"'";
      }
      else {
        for(int i=0;i<companiesList.size();i++)
          companies += "'"+companiesList.get(i).toString()+"',";
        companies = companies.substring(0,companies.length()-1);
      }

      String sql =
          "select WAR01_WAREHOUSES.COMPANY_CODE_SYS01,WAR01_WAREHOUSES.WAREHOUSE_CODE,WAR01_WAREHOUSES.DESCRIPTION,"+
          "WAR01_WAREHOUSES.PROGRESSIVE_HIE02,WAR01_WAREHOUSES.ADDRESS,WAR01_WAREHOUSES.ZIP,WAR01_WAREHOUSES.CITY,"+
          "WAR01_WAREHOUSES.PROVINCE,WAR01_WAREHOUSES.COUNTRY,WAR01_WAREHOUSES.PROGRESSIVE_SYS04,REG04_SUBJECTS.NAME_1,"+
          "HIE02_HIERARCHIES.PROGRESSIVE_HIE01 from "+
          "WAR01_WAREHOUSES,REG04_SUBJECTS,HIE02_HIERARCHIES where "+
          "WAR01_WAREHOUSES.COMPANY_CODE_SYS01=REG04_SUBJECTS.COMPANY_CODE_SYS01 and "+
          "REG04_SUBJECTS.SUBJECT_TYPE='M' and "+
          "WAR01_WAREHOUSES.COMPANY_CODE_SYS01 in ("+companies+") and "+
          "WAR01_WAREHOUSES.ENABLED='Y' and "+
          "WAR01_WAREHOUSES.WAREHOUSE_CODE=? and "+
          "WAR01_WAREHOUSES.PROGRESSIVE_HIE02=HIE02_HIERARCHIES.PROGRESSIVE";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01WAR01","WAR01_WAREHOUSES.COMPANY_CODE_SYS01");
      attribute2dbField.put("warehouseCodeWAR01","WAR01_WAREHOUSES.WAREHOUSE_CODE");
      attribute2dbField.put("descriptionWAR01","WAR01_WAREHOUSES.DESCRIPTION");
      attribute2dbField.put("progressiveHie02WAR01","WAR01_WAREHOUSES.PROGRESSIVE_HIE02");
      attribute2dbField.put("progressiveHie01HIE02","HIE02_HIERARCHIES.PROGRESSIVE_HIE01");
      attribute2dbField.put("addressWAR01","WAR01_WAREHOUSES.ADDRESS");
      attribute2dbField.put("zipWAR01","WAR01_WAREHOUSES.ZIP");
      attribute2dbField.put("cityWAR01","WAR01_WAREHOUSES.CITY");
      attribute2dbField.put("provinceWAR01","WAR01_WAREHOUSES.PROVINCE");
      attribute2dbField.put("countryWAR01","WAR01_WAREHOUSES.COUNTRY");
      attribute2dbField.put("progressiveSys04WAR01","WAR01_WAREHOUSES.PROGRESSIVE_SYS04");
      attribute2dbField.put("name_1REG04","REG04_SUBJECTS.NAME_1");

      ArrayList values = new ArrayList();
      values.add(pars.getCode());

      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          WarehouseVO.class,
          "Y",
          "N",
          null,
          new GridParams(),
          true
      );
      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while validating warehouse code",ex);
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
  public VOResponse deleteWarehouse(WarehousePK pk,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      stmt = conn.createStatement();

      // logically delete the record in WAR01...
      stmt.execute(
          "update WAR01_WAREHOUSES set ENABLED='N' where "+
          "COMPANY_CODE_SYS01='"+pk.getCompanyCodeSys01WAR01()+"' and "+
          "WAREHOUSE_CODE='"+pk.getWarehouseCodeWAR01()+"'"
      );

      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting an existing warehouse",ex);
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




}

