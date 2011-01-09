package org.jallinone.sales.activities.server;

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
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.sales.activities.java.*;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage sale activities.</p>
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
public class SaleActivitiesBean  implements SaleActivities {


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




  public SaleActivitiesBean() {
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public SaleActivityVO getSaleActivity() {
	  throw new UnsupportedOperationException();
  }
  

  /**
   * Business logic to execute.
   */
  public VOListResponse loadSaleActivities(GridParams gridParams,String serverLanguageId,String username,ArrayList companiesList,ArrayList customizedFields) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      // retrieve companies list...
      String companies = (String)gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01); // used in lookup grid...
      if (companies==null) {
        companies = "";
        for(int i=0;i<companiesList.size();i++)
          companies += "'"+companiesList.get(i).toString()+"',";
        companies = companies.substring(0,companies.length()-1);
      }
      else
        companies = "'"+companies+"'";

      String sql =
          "select SAL09_ACTIVITIES.COMPANY_CODE_SYS01,SAL09_ACTIVITIES.ACTIVITY_CODE,SAL09_ACTIVITIES.PROGRESSIVE_SYS10,"+
          "SYS10_SAL09.DESCRIPTION,SAL09_ACTIVITIES.VALUE,SAL09_ACTIVITIES.VAT_CODE_REG01,"+
          "SAL09_ACTIVITIES.CURRENCY_CODE_REG03,SYS10_REG01.DESCRIPTION,REG01_VATS.VALUE,REG01_VATS.DEDUCTIBLE,REG03_CURRENCIES.CURRENCY_SYMBOL"+
          " from SAL09_ACTIVITIES,SYS10_TRANSLATIONS SYS10_SAL09,SYS10_TRANSLATIONS SYS10_REG01,REG01_VATS,REG03_CURRENCIES where "+
          "SAL09_ACTIVITIES.CURRENCY_CODE_REG03=REG03_CURRENCIES.CURRENCY_CODE and "+
          "SAL09_ACTIVITIES.PROGRESSIVE_SYS10=SYS10_SAL09.PROGRESSIVE and "+
          "SYS10_SAL09.LANGUAGE_CODE=? and "+
          "SAL09_ACTIVITIES.COMPANY_CODE_SYS01 in ("+companies+") and "+
          "REG01_VATS.VAT_CODE=SAL09_ACTIVITIES.VAT_CODE_REG01 and "+
          "REG01_VATS.PROGRESSIVE_SYS10=SYS10_REG01.PROGRESSIVE and "+
          "SYS10_REG01.LANGUAGE_CODE=? ";


      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SAL09","SAL09_ACTIVITIES.COMPANY_CODE_SYS01");
      attribute2dbField.put("activityCodeSAL09","SAL09_ACTIVITIES.ACTIVITY_CODE");
      attribute2dbField.put("descriptionSYS10","SYS10_SAL09.DESCRIPTION");
      attribute2dbField.put("progressiveSys10SAL09","SAL09_ACTIVITIES.PROGRESSIVE_SYS10");
      attribute2dbField.put("valueSAL09","SAL09_ACTIVITIES.VALUE");
      attribute2dbField.put("vatCodeReg01SAL09","SAL09_ACTIVITIES.VAT_CODE_REG01");
      attribute2dbField.put("currencyCodeReg03SAL09","SAL09_ACTIVITIES.CURRENCY_CODE_REG03");
      attribute2dbField.put("vatDescriptionSYS10","SYS10_REG01.DESCRIPTION");
      attribute2dbField.put("vatValueREG01","REG01_VATS.VALUE");
      attribute2dbField.put("vatDeductibleREG01","REG01_VATS.DEDUCTIBLE");
      attribute2dbField.put("currencySymbolREG03","REG03_CURRENCIES.CURRENCY_SYMBOL");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(serverLanguageId);

      if (gridParams.getOtherGridParams().get(ApplicationConsts.CURRENCY_CODE_REG03)!=null) {
        sql += " and SAL09_ACTIVITIES.CURRENCY_CODE_REG03=? ";
        values.add( gridParams.getOtherGridParams().get(ApplicationConsts.CURRENCY_CODE_REG03) );
      }

      // read from SAL09 table...
      Response answer = CustomizeQueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          SaleActivityVO.class,
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
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching sale activities list",ex);
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
  public VOListResponse updateSaleActivities(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      SaleActivityVO oldVO = null;
      SaleActivityVO newVO = null;
      Response res = null;

      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (SaleActivityVO)oldVOs.get(i);
        newVO = (SaleActivityVO)newVOs.get(i);

        // update SYS10 table...
        TranslationUtils.updateTranslation(oldVO.getDescriptionSYS10(),newVO.getDescriptionSYS10(),newVO.getProgressiveSys10SAL09(),serverLanguageId,conn);

        HashSet pkAttrs = new HashSet();
        pkAttrs.add("companyCodeSys01SAL09");
        pkAttrs.add("activityCodeSAL09");

        HashMap attr2dbFields = new HashMap();
        attr2dbFields.put("companyCodeSys01SAL09","COMPANY_CODE_SYS01");
        attr2dbFields.put("activityCodeSAL09","ACTIVITY_CODE");
        attr2dbFields.put("progressiveSys10SAL09","PROGRESSIVE_SYS10");
        attr2dbFields.put("valueSAL09","VALUE");
        attr2dbFields.put("vatCodeReg01SAL09","VAT_CODE_REG01");
        attr2dbFields.put("currencyCodeReg03SAL09","CURRENCY_CODE_REG03");

        res = new CustomizeQueryUtil().updateTable(
            conn,
            new UserSessionParameters(username),
            pkAttrs,
            oldVO,
            newVO,
            "SAL09_ACTIVITIES",
            attr2dbFields,
            "Y",
            "N",
            null,
            true,
            customizedFields
        );
        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }
      }

      return new VOListResponse(newVOs,false,newVOs.size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing sale activities",ex);
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
  public VOListResponse validateSaleActivityCode(LookupValidationParams validationPars,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      String sql =
          "select SAL09_ACTIVITIES.COMPANY_CODE_SYS01,SAL09_ACTIVITIES.ACTIVITY_CODE,SAL09_ACTIVITIES.PROGRESSIVE_SYS10,"+
          "SYS10_SAL09.DESCRIPTION,SAL09_ACTIVITIES.VALUE,SAL09_ACTIVITIES.VAT_CODE_REG01,"+
          "SAL09_ACTIVITIES.CURRENCY_CODE_REG03,SYS10_REG01.DESCRIPTION,REG01_VATS.VALUE,REG01_VATS.DEDUCTIBLE,REG03_CURRENCIES.CURRENCY_SYMBOL"+
          " from SAL09_ACTIVITIES,SYS10_TRANSLATIONS SYS10_SAL09,SYS10_TRANSLATIONS SYS10_REG01,REG01_VATS,REG03_CURRENCIES where "+
          "SAL09_ACTIVITIES.CURRENCY_CODE_REG03=REG03_CURRENCIES.CURRENCY_CODE and "+
          "SAL09_ACTIVITIES.PROGRESSIVE_SYS10=SYS10_SAL09.PROGRESSIVE and "+
          "SYS10_SAL09.LANGUAGE_CODE=? and "+
          "REG01_VATS.VAT_CODE=SAL09_ACTIVITIES.VAT_CODE_REG01 and "+
          "REG01_VATS.PROGRESSIVE_SYS10=SYS10_REG01.PROGRESSIVE and "+
          "SYS10_REG01.LANGUAGE_CODE=? and "+
          "SAL09_ACTIVITIES.COMPANY_CODE_SYS01='"+validationPars.getLookupValidationParameters().get(ApplicationConsts.COMPANY_CODE_SYS01)+"' and "+
          "SAL09_ACTIVITIES.ACTIVITY_CODE='"+validationPars.getCode()+"'";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SAL09","SAL09_ACTIVITIES.COMPANY_CODE_SYS01");
      attribute2dbField.put("activityCodeSAL09","SAL09_ACTIVITIES.ACTIVITY_CODE");
      attribute2dbField.put("descriptionSYS10","SYS10_SAL09.DESCRIPTION");
      attribute2dbField.put("progressiveSys10SAL09","SAL09_ACTIVITIES.PROGRESSIVE_SYS10");
      attribute2dbField.put("valueSAL09","SAL09_ACTIVITIES.VALUE");
      attribute2dbField.put("vatCodeReg01SAL09","SAL09_ACTIVITIES.VAT_CODE_REG01");
      attribute2dbField.put("currencyCodeReg03SAL09","SAL09_ACTIVITIES.CURRENCY_CODE_REG03");
      attribute2dbField.put("vatDescriptionSYS10","SYS10_REG01.DESCRIPTION");
      attribute2dbField.put("vatValueREG01","REG01_VATS.VALUE");
      attribute2dbField.put("vatDeductibleREG01","REG01_VATS.DEDUCTIBLE");
      attribute2dbField.put("currencySymbolREG03","REG03_CURRENCIES.CURRENCY_SYMBOL");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(serverLanguageId);

      if (validationPars.getLookupValidationParameters().get(ApplicationConsts.CURRENCY_CODE_REG03)!=null) {
        sql += " and SAL09_ACTIVITIES.CURRENCY_CODE_REG03=? ";
        values.add( validationPars.getLookupValidationParameters().get(ApplicationConsts.CURRENCY_CODE_REG03) );
      }

      GridParams gridParams = new GridParams();

      // read from SAL09 table...
      Response answer = CustomizeQueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          SaleActivityVO.class,
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
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while validating sale activity code",ex);
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
  public VOListResponse insertSaleActivities(ArrayList list,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      SaleActivityVO vo = null;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SAL09","COMPANY_CODE_SYS01");
      attribute2dbField.put("activityCodeSAL09","ACTIVITY_CODE");
      attribute2dbField.put("activityCodeSAL09","ACTIVITY_CODE");
      attribute2dbField.put("progressiveSys10SAL09","PROGRESSIVE_SYS10");
      attribute2dbField.put("valueSAL09","VALUE");
      attribute2dbField.put("vatCodeReg01SAL09","VAT_CODE_REG01");
      attribute2dbField.put("currencyCodeReg03SAL09","CURRENCY_CODE_REG03");

      BigDecimal progressiveSYS10 = null;
      Response res = null;
      for(int i=0;i<list.size();i++) {
        vo = (SaleActivityVO)list.get(i);

        // insert record in SYS10...
        progressiveSYS10 = TranslationUtils.insertTranslations(vo.getDescriptionSYS10(),vo.getCompanyCodeSys01SAL09(),conn);
        vo.setProgressiveSys10SAL09(progressiveSYS10);

        // insert into SAL09...
        res = CustomizeQueryUtil.insertTable(
            conn,
            new UserSessionParameters(username),
            vo,
            "SAL09_ACTIVITIES",
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
      }

      return new VOListResponse(list,false,list.size());
    }
    catch (Throwable ex) {
      Logger.error(username, this.getClass().getName(),
                   "executeCommand", "Error while inserting new sale activities", ex);
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
  public VOResponse deleteSaleActivities(ArrayList list,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      stmt = conn.createStatement();

      SaleActivityVO vo = null;
      for(int i=0;i<list.size();i++) {
        // phisically delete the record in SAL09...
        vo = (SaleActivityVO)list.get(i);
        stmt.execute("delete from SAL09_ACTIVITIES where COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01SAL09()+"' and ACTIVITY_CODE='"+vo.getSaleActivityCodeSAL09()+"'");
      }

      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing sale activities",ex);
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

