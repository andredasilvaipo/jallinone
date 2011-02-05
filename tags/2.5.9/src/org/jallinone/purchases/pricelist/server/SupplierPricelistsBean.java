package org.jallinone.purchases.pricelist.server;

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
import org.jallinone.purchases.pricelist.java.*;
import org.openswing.swing.server.*;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.commons.server.*;

import java.math.*;

import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage supplier item prices.</p>
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
public class SupplierPricelistsBean  implements SupplierPricelists {


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




  public SupplierPricelistsBean() {
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public SupplierPricelistVO getSupplierPricelist() {
	  throw new UnsupportedOperationException();
  }


  /**
   * Business logic to execute.
   */
  public VOListResponse loadSupplierPricelists(GridParams gridParams,String serverLanguageId,String username,ArrayList companiesList,ArrayList customizedFields) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      // retrieve companies list...
      String companies = (String)gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01); // used in lookup grid...
      BigDecimal progressiveREG04 = (BigDecimal)gridParams.getOtherGridParams().get(ApplicationConsts.PROGRESSIVE_REG04);

      if (companies==null) {
        companies = "";
        for(int i=0;i<companiesList.size();i++)
          companies += "'"+companiesList.get(i).toString()+"',";
        companies = companies.substring(0,companies.length()-1);
      }
      else
        companies = "'"+companies+"'";

      String sql =
          "select PUR03_SUPPLIER_PRICELISTS.COMPANY_CODE_SYS01,PUR03_SUPPLIER_PRICELISTS.PRICELIST_CODE,"+
          "PUR03_SUPPLIER_PRICELISTS.PROGRESSIVE_REG04,PUR03_SUPPLIER_PRICELISTS.PROGRESSIVE_SYS10,"+
          "SYS10_TRANSLATIONS.DESCRIPTION,PUR03_SUPPLIER_PRICELISTS.CURRENCY_CODE_REG03,REG04_SUBJECTS.NAME_1 "+
          "from PUR03_SUPPLIER_PRICELISTS,SYS10_TRANSLATIONS,REG04_SUBJECTS "+
          "where PUR03_SUPPLIER_PRICELISTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and PUR03_SUPPLIER_PRICELISTS.COMPANY_CODE_SYS01 in ("+companies+") and "+
          "REG04_SUBJECTS.COMPANY_CODE_SYS01=PUR03_SUPPLIER_PRICELISTS.COMPANY_CODE_SYS01 AND "+
          "REG04_SUBJECTS.PROGRESSIVE=PUR03_SUPPLIER_PRICELISTS.PROGRESSIVE_REG04 ";
      if (progressiveREG04!=null)
        sql += " AND PUR03_SUPPLIER_PRICELISTS.PROGRESSIVE_REG04=? ";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01PUR03","PUR03_SUPPLIER_PRICELISTS.COMPANY_CODE_SYS01");
      attribute2dbField.put("pricelistCodePUR03","PUR03_SUPPLIER_PRICELISTS.PRICELIST_CODE");
      attribute2dbField.put("progressiveReg04PUR03","PUR03_SUPPLIER_PRICELISTS.PROGRESSIVE_REG04");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("progressiveSys10PUR03","PUR03_SUPPLIER_PRICELISTS.PROGRESSIVE_SYS10");
      attribute2dbField.put("currencyCodeReg03PUR03","PUR03_SUPPLIER_PRICELISTS.CURRENCY_CODE_REG03");
      attribute2dbField.put("name_1REG04","REG04_SUBJECTS.NAME_1");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      if (progressiveREG04!=null)
        values.add(progressiveREG04);

      // read from PUR03 table...
      Response answer = CustomizeQueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          SupplierPricelistVO.class,
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
    	Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching supplier pricelists list",ex);
    	try {
    		if (this.conn==null && conn!=null)
    			// rollback only local connection
    			conn.rollback();
    	}
    	catch (Exception ex3) {
    	}      throw new Exception(ex.getMessage());
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
  public VOListResponse updateSupplierPricelists(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      SupplierPricelistVO oldVO = null;
      SupplierPricelistVO newVO = null;
      Response res = null;

      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (SupplierPricelistVO)oldVOs.get(i);
        newVO = (SupplierPricelistVO)newVOs.get(i);

        // update SYS10 table...
        TranslationUtils.updateTranslation(oldVO.getDescriptionSYS10(),newVO.getDescriptionSYS10(),newVO.getProgressiveSys10PUR03(),serverLanguageId,conn);

        HashSet pkAttrs = new HashSet();
        pkAttrs.add("companyCodeSys01PUR03");
        pkAttrs.add("pricelistCodePUR03");
        pkAttrs.add("progressiveReg04PUR03");

        HashMap attribute2dbField = new HashMap();
        attribute2dbField.put("companyCodeSys01PUR03","COMPANY_CODE_SYS01");
        attribute2dbField.put("pricelistCodePUR03","PRICELIST_CODE");
        attribute2dbField.put("progressiveReg04PUR03","PROGRESSIVE_REG04");
        attribute2dbField.put("progressiveSys10PUR03","PROGRESSIVE_SYS10");
        attribute2dbField.put("currencyCodeReg03PUR03","CURRENCY_CODE_REG03");

        res = new CustomizeQueryUtil().updateTable(
            conn,
            new UserSessionParameters(username),
            pkAttrs,
            oldVO,
            newVO,
            "PUR03_SUPPLIER_PRICELISTS",
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

      return new VOListResponse(newVOs,false,newVOs.size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing supplier pricelists",ex);
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
  public VOListResponse validateSupplierPricelistCode(LookupValidationParams validationPars,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      String sql =
          "select PUR03_SUPPLIER_PRICELISTS.COMPANY_CODE_SYS01,PUR03_SUPPLIER_PRICELISTS.PRICELIST_CODE,"+
          "PUR03_SUPPLIER_PRICELISTS.PROGRESSIVE_SYS10,SYS10_TRANSLATIONS.DESCRIPTION,"+
          "PUR03_SUPPLIER_PRICELISTS.CURRENCY_CODE_REG03,REG04_SUBJECTS.NAME_1 "+
          "from PUR03_SUPPLIER_PRICELISTS,SYS10_TRANSLATIONS,REG04_SUBJECTS "+
          "where "+
          "PUR03_SUPPLIER_PRICELISTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "PUR03_SUPPLIER_PRICELISTS.PRICELIST_CODE=? and PUR03_SUPPLIER_PRICELISTS.COMPANY_CODE_SYS01=? and "+
          "REG04_SUBJECTS.COMPANY_CODE_SYS01=PUR03_SUPPLIER_PRICELISTS.COMPANY_CODE_SYS01 AND "+
          "REG04_SUBJECTS.PROGRESSIVE=PUR03_SUPPLIER_PRICELISTS.PROGRESSIVE_REG04 ";

      if (validationPars.getLookupValidationParameters().get(ApplicationConsts.PROGRESSIVE_REG04)!=null)
        sql += " and PUR03_SUPPLIER_PRICELISTS.PROGRESSIVE_REG04=?";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01PUR03","PUR03_SUPPLIER_PRICELISTS.COMPANY_CODE_SYS01");
      attribute2dbField.put("pricelistCodePUR03","PUR03_SUPPLIER_PRICELISTS.PRICELIST_CODE");
      attribute2dbField.put("progressiveReg04PUR03","PUR03_SUPPLIER_PRICELISTS.PROGRESSIVE_REG04");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("progressiveSys10PUR03","PUR03_SUPPLIER_PRICELISTS.PROGRESSIVE_SYS10");
      attribute2dbField.put("currencyCodeReg03PUR03","PUR03_SUPPLIER_PRICELISTS.CURRENCY_CODE_REG03");
      attribute2dbField.put("name_1REG04","REG04_SUBJECTS.NAME_1");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(validationPars.getCode());
      values.add( validationPars.getLookupValidationParameters().get(ApplicationConsts.COMPANY_CODE_SYS01) );
      if (validationPars.getLookupValidationParameters().get(ApplicationConsts.PROGRESSIVE_REG04)!=null)
        values.add( validationPars.getLookupValidationParameters().get(ApplicationConsts.PROGRESSIVE_REG04) );

      // read from PUR03 table...
      Response answer = CustomizeQueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          SupplierPricelistVO.class,
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
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while validating supplier pricelist code",ex);
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
  public VOResponse changeSupplierPricelist(SupplierPricelistChanges changes,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      if (changes.getStartDate()!=null && changes.getEndDate()!=null) {
        String sql = "update PUR04_SUPPLIER_PRICES set START_DATE=?,END_DATE=? where COMPANY_CODE_SYS01=? and PRICELIST_CODE_PUR03=? and PROGRESSIVE_REG04=?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setDate(1,changes.getStartDate());
        pstmt.setDate(2,changes.getEndDate());
        pstmt.setString(3,changes.getCompanyCodeSys01PUR04());
        pstmt.setString(4,changes.getPricelistCodePur03PUR04());
        pstmt.setBigDecimal(5,changes.getProgressiveReg04PUR04());
        pstmt.execute();
        pstmt.close();
      }

      if (changes.getPercentage()!=null && !changes.isTruncateDecimals()) {
        String sql = "update PUR04_SUPPLIER_PRICES set VALUE=VALUE+VALUE*"+changes.getPercentage().doubleValue()+"/100 where COMPANY_CODE_SYS01=? and PRICELIST_CODE_PUR03=? and PROGRESSIVE_REG04=?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1,changes.getCompanyCodeSys01PUR04());
        pstmt.setString(2,changes.getPricelistCodePur03PUR04());
        pstmt.setBigDecimal(3,changes.getProgressiveReg04PUR04());
        pstmt.execute();
        pstmt.close();
      }

      if (changes.getPercentage()!=null && changes.isTruncateDecimals()) {
        String sql = "select ITEM_CODE_ITM01,VALUE from PUR04_SUPPLIER_PRICES where COMPANY_CODE_SYS01=? and PRICELIST_CODE_PUR03=? ";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1,changes.getCompanyCodeSys01PUR04());
        pstmt.setString(2,changes.getPricelistCodePur03PUR04());
        ResultSet rset = pstmt.executeQuery();
        PreparedStatement pstmt2 = conn.prepareStatement("update PUR04_SUPPLIER_PRICES set VALUE=? where COMPANY_CODE_SYS01=? and PRICELIST_CODE_PUR03=? and ITEM_CODE_ITM01=? and PROGRESSIVE_REG04=?");
        while(rset.next()) {
          pstmt2.setInt(1,(int)(rset.getDouble(2)+rset.getDouble(2)*changes.getPercentage().doubleValue()/100));
          pstmt2.setString(2,changes.getCompanyCodeSys01PUR04());
          pstmt2.setString(3,changes.getPricelistCodePur03PUR04());
          pstmt2.setString(4,rset.getString(1));
          pstmt2.setBigDecimal(5,changes.getProgressiveReg04PUR04());
          pstmt2.execute();
        }
        rset.close();
        pstmt.close();
        pstmt2.close();
      }

      if (changes.getDeltaValue()!=null) {
        String sql = "update PUR04_SUPPLIER_PRICES set VALUE=VALUE+"+changes.getDeltaValue().doubleValue()+" where COMPANY_CODE_SYS01=? and PRICELIST_CODE_PUR03=? and PROGRESSIVE_REG04=?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1,changes.getCompanyCodeSys01PUR04());
        pstmt.setString(2,changes.getPricelistCodePur03PUR04());
        pstmt.setBigDecimal(3,changes.getProgressiveReg04PUR04());
        pstmt.execute();
        pstmt.close();
      }

      return new  VOResponse(Boolean.TRUE);
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing supplier item prices",ex);
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
  public VOResponse deleteSupplierPricelist(ArrayList list,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      stmt = conn.createStatement();

      SupplierPricelistVO vo = null;
      for(int i=0;i<list.size();i++) {
        // phisically delete records from PUR04...
        vo = (SupplierPricelistVO)list.get(i);

        stmt.execute("delete from PUR04_SUPPLIER_PRICES where COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01PUR03()+"' and PRICELIST_CODE_PUR03='"+vo.getPricelistCodePUR03()+"' and PROGRESSIVE_REG04="+vo.getProgressiveReg04PUR03());

        // phisically delete record from SYS10...
        TranslationUtils.deleteTranslations(vo.getProgressiveSys10PUR03(),conn);

        // phisically delete the record in PUR03...
        vo = (SupplierPricelistVO)list.get(i);
        stmt.execute("delete from PUR03_SUPPLIER_PRICELISTS where COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01PUR03()+"' and PRICELIST_CODE='"+vo.getPricelistCodePUR03()+"' and PROGRESSIVE_REG04="+vo.getProgressiveReg04PUR03());
      }

      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting an existing supplier pricelist",ex);
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
  public VOListResponse insertSupplierPricelists(ArrayList list,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      SupplierPricelistVO vo = null;
      BigDecimal progressiveSYS10 = null;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01PUR03","COMPANY_CODE_SYS01");
      attribute2dbField.put("pricelistCodePUR03","PRICELIST_CODE");
      attribute2dbField.put("progressiveReg04PUR03","PROGRESSIVE_REG04");
      attribute2dbField.put("progressiveSys10PUR03","PROGRESSIVE_SYS10");
      attribute2dbField.put("currencyCodeReg03PUR03","CURRENCY_CODE_REG03");
      Response res = null;

      pstmt = conn.prepareStatement(
        "insert into PUR04_SUPPLIER_PRICES(COMPANY_CODE_SYS01,PRICELIST_CODE_PUR03,PROGRESSIVE_REG04,ITEM_CODE_ITM01,VALUE,START_DATE,END_DATE) "+
        "select ?,?,?,ITEM_CODE_ITM01,VALUE,START_DATE,END_DATE from PUR04_SUPPLIER_PRICES where COMPANY_CODE_SYS01=? and PRICELIST_CODE_PUR03=? and PROGRESSIVE_REG04=?"
      );

      for (int i=0;i<list.size();i++) {
        vo = (SupplierPricelistVO)list.get(i);

        // insert record in SYS10...
        progressiveSYS10 = TranslationUtils.insertTranslations(vo.getDescriptionSYS10(),vo.getCompanyCodeSys01PUR03(),conn);
        vo.setProgressiveSys10PUR03(progressiveSYS10);

        // insert into PUR03...
        res = CustomizeQueryUtil.insertTable(
            conn,
            new UserSessionParameters(username),
            vo,
            "PUR03_SUPPLIER_PRICELISTS",
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

        // test if there will have to copy all prices from an existing pricelist...
        if (vo.getOldPricelistCodePur03PUR04()!=null) {
          pstmt.setString(1,vo.getCompanyCodeSys01PUR03());
          pstmt.setString(2,vo.getPricelistCodePUR03());
          pstmt.setBigDecimal(3,vo.getProgressiveReg04PUR03());
          pstmt.setString(4,vo.getCompanyCodeSys01PUR03());
          pstmt.setString(5,vo.getOldPricelistCodePur03PUR04());
          pstmt.setBigDecimal(6,vo.getProgressiveReg04PUR03());
          pstmt.execute();
        }

      }

      return new VOListResponse(list,false,list.size());
    }
    catch (Throwable ex) {
      Logger.error(username, this.getClass().getName(),
    		  "executeCommand", "Error while inserting new supplier pricelists", ex);
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

