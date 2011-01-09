package org.jallinone.sales.pricelist.server;

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
import org.jallinone.sales.pricelist.java.*;
import org.openswing.swing.server.*;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.commons.server.*;

import java.math.*;

import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage sale pricelists.</p>
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
public class SalePricelistsBean  implements SalePricelists {


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




  public SalePricelistsBean() {
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public PricelistVO getPricelist() {
	  throw new UnsupportedOperationException();	  
  }


  /**
   * Business logic to execute.
   */
  public VOResponse deletePricelist(ArrayList list,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      stmt = conn.createStatement();

      PricelistVO vo = null;
      for(int i=0;i<list.size();i++) {
        vo = (PricelistVO)list.get(i);

        // phisically delete records from SAL02...
        stmt.execute("delete from SAL02_PRICES where COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01SAL01()+"' and PRICELIST_CODE_SAL01='"+vo.getPricelistCodeSAL01()+"'");

        // phisically delete record from SYS10...
        TranslationUtils.deleteTranslations(vo.getProgressiveSys10SAL01(),conn);

        // phisically delete the record in SAL01...
        vo = (PricelistVO)list.get(i);
        stmt.execute("delete from SAL01_PRICELISTS where COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01SAL01()+"' and PRICELIST_CODE='"+vo.getPricelistCodeSAL01()+"'");
      }

      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting an existing pricelist",ex);
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
  public VOResponse importAllItems(PricelistChanges vo,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    PreparedStatement pstmt2 = null;
    
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      pstmt = conn.prepareStatement(
        "select ITEM_CODE from ITM01_ITEMS where ENABLED='Y' and COMPANY_CODE_SYS01=?"
      );
      pstmt2 = conn.prepareStatement(
        "insert into SAL02_PRICES(COMPANY_CODE_SYS01,PRICELIST_CODE_SAL01,ITEM_CODE_ITM01,VALUE,START_DATE,END_DATE) values(?,?,?,?,?,?)"
      );

      pstmt.setString(1,vo.getCompanyCodeSys01SAL02());
      ResultSet rset = pstmt.executeQuery();
      while(rset.next()) {
        pstmt2.setString(1,vo.getCompanyCodeSys01SAL02());
        pstmt2.setString(2,vo.getPricelistCodeSal01SAL02());
        pstmt2.setString(3,rset.getString(1));
        pstmt2.setBigDecimal(4,vo.getDeltaValue());
        pstmt2.setDate(5,vo.getStartDate());
        pstmt2.setDate(6,vo.getEndDate());
        try {
          pstmt2.executeUpdate();
        }
        catch (SQLException ex4) {
        }
      }
      rset.close();

      return new VOResponse(Boolean.TRUE);
    }
    catch (Throwable ex) {
      Logger.error(username, this.getClass().getName(),
                   "executeCommand", "Error while inserting price for all items", ex);
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
        pstmt2.close();
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
   * Business logic to execute.
   */
  public VOListResponse insertPricelists(ArrayList list,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      PricelistVO vo = null;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SAL01","COMPANY_CODE_SYS01");
      attribute2dbField.put("pricelistCodeSAL01","PRICELIST_CODE");
      attribute2dbField.put("progressiveSys10SAL01","PROGRESSIVE_SYS10");
      attribute2dbField.put("currencyCodeReg03SAL01","CURRENCY_CODE_REG03");

      Response res = null;
      BigDecimal progressiveSYS10 = null;
      pstmt = conn.prepareStatement(
        "insert into SAL02_PRICES(COMPANY_CODE_SYS01,PRICELIST_CODE_SAL01,ITEM_CODE_ITM01,VALUE,START_DATE,END_DATE) "+
        "select ?,?,ITEM_CODE_ITM01,VALUE,START_DATE,END_DATE from SAL02_PRICES where COMPANY_CODE_SYS01=? and PRICELIST_CODE_SAL01=?"
      );
      for(int i=0;i<list.size();i++) {
        vo = (PricelistVO)list.get(i);

        // insert record in SYS10...
        progressiveSYS10 = TranslationUtils.insertTranslations(vo.getDescriptionSYS10(),vo.getCompanyCodeSys01SAL01(),conn);
        vo.setProgressiveSys10SAL01(progressiveSYS10);

        // insert into SAL01...
        res = CustomizeQueryUtil.insertTable(
            conn,
            new UserSessionParameters(username),
            vo,
            "SAL01_PRICELISTS",
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
        if (vo.getOldPricelistCodeSal01SAL02()!=null) {
          pstmt.setString(1,vo.getCompanyCodeSys01SAL01());
          pstmt.setString(2,vo.getPricelistCodeSAL01());
          pstmt.setString(3,vo.getCompanyCodeSys01SAL01());
          pstmt.setString(4,vo.getOldPricelistCodeSal01SAL02());
          pstmt.execute();
        }
      }

      return new VOListResponse(list,false,list.size());
    }
    catch (Throwable ex) {
      Logger.error(username, this.getClass().getName(),
                   "executeCommand", "Error while inserting new pricelists", ex);
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
  public VOListResponse loadPricelists(GridParams gridParams,String serverLanguageId,String username,ArrayList companiesList,ArrayList customizedFields) throws Throwable {
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
      else {
        companies = "'"+companies+"'";
      }

      String sql =
          "select SAL01_PRICELISTS.COMPANY_CODE_SYS01,SAL01_PRICELISTS.PRICELIST_CODE,SAL01_PRICELISTS.PROGRESSIVE_SYS10,SYS10_TRANSLATIONS.DESCRIPTION,SAL01_PRICELISTS.CURRENCY_CODE_REG03 from SAL01_PRICELISTS,SYS10_TRANSLATIONS where "+
          "SAL01_PRICELISTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and SAL01_PRICELISTS.COMPANY_CODE_SYS01 in ("+companies+")";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SAL01","SAL01_PRICELISTS.COMPANY_CODE_SYS01");
      attribute2dbField.put("pricelistCodeSAL01","SAL01_PRICELISTS.PRICELIST_CODE");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("progressiveSys10SAL01","SAL01_PRICELISTS.PROGRESSIVE_SYS10");
      attribute2dbField.put("currencyCodeReg03SAL01","SAL01_PRICELISTS.CURRENCY_CODE_REG03");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);

      // read from SAL01 table...
      Response answer = CustomizeQueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          PricelistVO.class,
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
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching pricelists list",ex);
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
  public VOListResponse updatePricelists(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      PricelistVO oldVO = null;
      PricelistVO newVO = null;
      Response res = null;

      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (PricelistVO)oldVOs.get(i);
        newVO = (PricelistVO)newVOs.get(i);

        // update SYS10 table...
        TranslationUtils.updateTranslation(oldVO.getDescriptionSYS10(),newVO.getDescriptionSYS10(),newVO.getProgressiveSys10SAL01(),serverLanguageId,conn);

        HashSet pkAttrs = new HashSet();
        pkAttrs.add("companyCodeSys01SAL01");
        pkAttrs.add("pricelistCodeSAL01");

        HashMap attribute2dbField = new HashMap();
        attribute2dbField.put("companyCodeSys01SAL01","COMPANY_CODE_SYS01");
        attribute2dbField.put("pricelistCodeSAL01","PRICELIST_CODE");
        attribute2dbField.put("progressiveSys10SAL01","PROGRESSIVE_SYS10");
        attribute2dbField.put("currencyCodeReg03SAL01","CURRENCY_CODE_REG03");

        res = new CustomizeQueryUtil().updateTable(
            conn,
            new UserSessionParameters(username),
            pkAttrs,
            oldVO,
            newVO,
            "SAL01_PRICELISTS",
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
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing pricelists",ex);
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
  public VOListResponse validatePricelistCode(LookupValidationParams validationPars,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      String sql =
          "select SAL01_PRICELISTS.COMPANY_CODE_SYS01,SAL01_PRICELISTS.PRICELIST_CODE,SAL01_PRICELISTS.PROGRESSIVE_SYS10,SYS10_TRANSLATIONS.DESCRIPTION,SAL01_PRICELISTS.CURRENCY_CODE_REG03 from SAL01_PRICELISTS,SYS10_TRANSLATIONS where "+
          "SAL01_PRICELISTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "SAL01_PRICELISTS.PRICELIST_CODE=? and SAL01_PRICELISTS.COMPANY_CODE_SYS01=?";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SAL01","SAL01_PRICELISTS.COMPANY_CODE_SYS01");
      attribute2dbField.put("pricelistCodeSAL01","SAL01_PRICELISTS.PRICELIST_CODE");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("progressiveSys10SAL01","SAL01_PRICELISTS.PROGRESSIVE_SYS10");
      attribute2dbField.put("currencyCodeReg03SAL01","SAL01_PRICELISTS.CURRENCY_CODE_REG03");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(validationPars.getCode());
      values.add( (String)validationPars.getLookupValidationParameters().get(ApplicationConsts.COMPANY_CODE_SYS01) );

      // read from SAL01 table...
      Response answer = CustomizeQueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          PricelistVO.class,
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
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while validating pricelist code",ex);
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
  public VOResponse changePricelist(PricelistChanges changes,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      if (changes.getStartDate()!=null && changes.getEndDate()!=null) {
        String sql = "update SAL02_PRICES set START_DATE=?,END_DATE=? where COMPANY_CODE_SYS01=? and PRICELIST_CODE_SAL01=? ";
        pstmt = conn.prepareStatement(sql);
        pstmt.setDate(1,changes.getStartDate());
        pstmt.setDate(2,changes.getEndDate());
        pstmt.setString(3,changes.getCompanyCodeSys01SAL02());
        pstmt.setString(4,changes.getPricelistCodeSal01SAL02());
        pstmt.execute();
        pstmt.close();
      }

      if (changes.getPercentage()!=null && !changes.isTruncateDecimals()) {
        String sql = "update SAL02_PRICES set VALUE=VALUE+VALUE*"+changes.getPercentage().doubleValue()+"/100 where COMPANY_CODE_SYS01=? and PRICELIST_CODE_SAL01=? ";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1,changes.getCompanyCodeSys01SAL02());
        pstmt.setString(2,changes.getPricelistCodeSal01SAL02());
        pstmt.execute();
        pstmt.close();
      }

      if (changes.getPercentage()!=null && changes.isTruncateDecimals()) {
        String sql = "select ITEM_CODE_ITM01,VALUE from SAL02_PRICES where COMPANY_CODE_SYS01=? and PRICELIST_CODE_SAL01=? ";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1,changes.getCompanyCodeSys01SAL02());
        pstmt.setString(2,changes.getPricelistCodeSal01SAL02());
        ResultSet rset = pstmt.executeQuery();
        PreparedStatement pstmt2 = conn.prepareStatement("update SAL02_PRICES set VALUE=? where COMPANY_CODE_SYS01=? and PRICELIST_CODE_SAL01=? and ITEM_CODE_ITM01=?");
        while(rset.next()) {
          pstmt2.setInt(1,(int)(rset.getDouble(2)+rset.getDouble(2)*changes.getPercentage().doubleValue()/100));
          pstmt2.setString(2,changes.getCompanyCodeSys01SAL02());
          pstmt2.setString(3,changes.getPricelistCodeSal01SAL02());
          pstmt2.setString(4,rset.getString(1));
          pstmt2.execute();
        }
        rset.close();
        pstmt.close();
        pstmt2.close();
      }

      if (changes.getDeltaValue()!=null) {
        String sql = "update SAL02_PRICES set VALUE=VALUE+"+changes.getDeltaValue().doubleValue()+" where COMPANY_CODE_SYS01=? and PRICELIST_CODE_SAL01=? ";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1,changes.getCompanyCodeSys01SAL02());
        pstmt.setString(2,changes.getPricelistCodeSal01SAL02());
        pstmt.execute();
        pstmt.close();
      }

      Response answer =  new VOResponse(Boolean.TRUE);




      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing prices",ex);
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

