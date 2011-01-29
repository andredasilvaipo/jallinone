package org.jallinone.sales.charges.server;

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
import org.jallinone.sales.charges.java.*;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage charges.</p>
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
public class ChargesBean  implements Charges {


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




  public ChargesBean() {
  }

  
  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public ChargeVO getCharge() {
	  throw new UnsupportedOperationException();
  }



  /**
   * Business logic to execute.
   */
  public VOListResponse loadCharges(GridParams gridParams,String serverLanguageId,String username,ArrayList companiesList,ArrayList customizedFields) throws Throwable {
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
          "select SAL06_CHARGES.COMPANY_CODE_SYS01,SAL06_CHARGES.CHARGE_CODE,SAL06_CHARGES.PROGRESSIVE_SYS10,"+
          "SYS10_TRANSLATIONS.DESCRIPTION,SAL06_CHARGES.VALUE,SAL06_CHARGES.PERC,SAL06_CHARGES.VAT_CODE_REG01,"+
          "SAL06_CHARGES.CURRENCY_CODE_REG03,SAL06_CHARGES.ENABLED"+
          " from SAL06_CHARGES,SYS10_TRANSLATIONS where "+
          "SAL06_CHARGES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "SAL06_CHARGES.ENABLED='Y' and "+
          "SAL06_CHARGES.COMPANY_CODE_SYS01 in ("+companies+")";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SAL06","SAL06_CHARGES.COMPANY_CODE_SYS01");
      attribute2dbField.put("chargeCodeSAL06","SAL06_CHARGES.CHARGE_CODE");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("progressiveSys10SAL06","SAL06_CHARGES.PROGRESSIVE_SYS10");
      attribute2dbField.put("valueSAL06","SAL06_CHARGES.VALUE");
      attribute2dbField.put("percSAL06","SAL06_CHARGES.PERC");
      attribute2dbField.put("vatCodeReg01SAL06","SAL06_CHARGES.VAT_CODE_REG01");
      attribute2dbField.put("currencyCodeReg03SAL06","SAL06_CHARGES.CURRENCY_CODE_REG03");
      attribute2dbField.put("enabledSAL06","SAL06_CHARGES.ENABLED");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);

      // read from SAL06 table...
      Response res = CustomizeQueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          ChargeVO.class,
          "Y",
          "N",
          null,
          gridParams,
          50,
          true,
          customizedFields
      );
     if (res.isError())
       throw new Exception(res.getErrorMessage());

     java.util.List list = ((VOListResponse)res).getRows();
     ChargeVO vo = null;
     sql =
         "select SYS10_TRANSLATIONS.DESCRIPTION,REG01_VATS.VALUE,REG01_VATS.DEDUCTIBLE "+
         "from SYS10_TRANSLATIONS,REG01_VATS where "+
         "REG01_VATS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
         "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
         "REG01_VATS.VAT_CODE=?";
     pstmt = conn.prepareStatement(sql);
     ResultSet rset = null;
     for(int i=0;i<list.size();i++) {
       vo = (ChargeVO)list.get(i);
       if (vo.getVatCodeReg01SAL06()!=null) {
         // retrieve vat data from REG01...
         pstmt.setString(1,serverLanguageId);
         pstmt.setString(2,vo.getVatCodeReg01SAL06());
         rset = pstmt.executeQuery();
         if (rset.next()) {
           vo.setVatDescriptionSYS10(rset.getString(1));
           vo.setVatValueREG01(rset.getBigDecimal(2));
           vo.setVatDeductibleREG01(rset.getBigDecimal(3));
         }
         rset.close();
       }
     }

     Response answer = res;
     if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching charges list",ex);
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
  public VOListResponse updateCharges(ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      ChargeVO oldVO = null;
      ChargeVO newVO = null;
      Response res = null;

      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (ChargeVO)oldVOs.get(i);
        newVO = (ChargeVO)newVOs.get(i);

        // update SYS10 table...
        TranslationUtils.updateTranslation(oldVO.getDescriptionSYS10(),newVO.getDescriptionSYS10(),newVO.getProgressiveSys10SAL06(),serverLanguageId,conn);

        HashSet pkAttrs = new HashSet();
        pkAttrs.add("companyCodeSys01SAL06");
        pkAttrs.add("chargeCodeSAL06");

        HashMap attr2dbFields = new HashMap();
        attr2dbFields.put("companyCodeSys01SAL06","COMPANY_CODE_SYS01");
        attr2dbFields.put("chargeCodeSAL06","CHARGE_CODE");
        attr2dbFields.put("progressiveSys10SAL06","PROGRESSIVE_SYS10");
        attr2dbFields.put("valueSAL06","VALUE");
        attr2dbFields.put("percSAL06","PERC");
        attr2dbFields.put("vatCodeReg01SAL06","VAT_CODE_REG01");
        attr2dbFields.put("currencyCodeReg03SAL06","CURRENCY_CODE_REG03");

        res = new CustomizeQueryUtil().updateTable(
            conn,
            new UserSessionParameters(username),
            pkAttrs,
            oldVO,
            newVO,
            "SAL06_CHARGES",
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
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing charges",ex);
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
  public VOListResponse validateChargeCode(LookupValidationParams validationPars,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;


      String sql =
          "select SAL06_CHARGES.COMPANY_CODE_SYS01,SAL06_CHARGES.CHARGE_CODE,SAL06_CHARGES.PROGRESSIVE_SYS10,"+
          "SYS10_TRANSLATIONS.DESCRIPTION,SAL06_CHARGES.VALUE,SAL06_CHARGES.PERC,SAL06_CHARGES.VAT_CODE_REG01,"+
          "SAL06_CHARGES.CURRENCY_CODE_REG03,SAL06_CHARGES.ENABLED"+
          " from SAL06_CHARGES,SYS10_TRANSLATIONS where "+
          "SAL06_CHARGES.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "SAL06_CHARGES.ENABLED='Y' and "+
          "SAL06_CHARGES.COMPANY_CODE_SYS01='"+validationPars.getLookupValidationParameters().get(ApplicationConsts.COMPANY_CODE_SYS01)+"' and "+
          "SAL06_CHARGES.CHARGE_CODE='"+validationPars.getCode()+"'";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SAL06","SAL06_CHARGES.COMPANY_CODE_SYS01");
      attribute2dbField.put("chargeCodeSAL06","SAL06_CHARGES.CHARGE_CODE");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("progressiveSys10SAL06","SAL06_CHARGES.PROGRESSIVE_SYS10");
      attribute2dbField.put("valueSAL06","SAL06_CHARGES.VALUE");
      attribute2dbField.put("percSAL06","SAL06_CHARGES.PERC");
      attribute2dbField.put("vatCodeReg01SAL06","SAL06_CHARGES.VAT_CODE_REG01");
      attribute2dbField.put("currencyCodeReg03SAL06","SAL06_CHARGES.CURRENCY_CODE_REG03");
      attribute2dbField.put("enabledSAL06","SAL06_CHARGES.ENABLED");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);

      GridParams gridParams = new GridParams();

      // read from SAL06 table...
      Response res = CustomizeQueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          ChargeVO.class,
          "Y",
          "N",
          null,
          gridParams,
          true,
          customizedFields
      );

      if (res.isError())
        throw new Exception(res.getErrorMessage());

      java.util.List list = ((VOListResponse)res).getRows();
      ChargeVO vo = null;
      sql =
          "select SYS10_TRANSLATIONS.DESCRIPTION,REG01_VATS.VALUE,REG01_VATS.DEDUCTIBLE "+
          "from SYS10_TRANSLATIONS,REG01_VATS where "+
          "REG01_VATS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "REG01_VATS.VAT_CODE=?";
      pstmt = conn.prepareStatement(sql);
      ResultSet rset = null;
      for(int i=0;i<list.size();i++) {
        vo = (ChargeVO)list.get(i);
        if (vo.getVatCodeReg01SAL06()!=null) {
          // retrieve vat data from REG01...
          pstmt.setString(1,serverLanguageId);
          pstmt.setString(2,vo.getVatCodeReg01SAL06());
          rset = pstmt.executeQuery();
          if (rset.next()) {
            vo.setVatDescriptionSYS10(rset.getString(1));
            vo.setVatValueREG01(rset.getBigDecimal(2));
            vo.setVatDeductibleREG01(rset.getBigDecimal(3));
          }
          rset.close();
        }
      }

      Response answer = res;



      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while validating charge code",ex);
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
  public VOListResponse insertCharges(ArrayList list,String serverLanguageId,String username,ArrayList customizedFields) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      ChargeVO vo = null;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01SAL06","COMPANY_CODE_SYS01");
      attribute2dbField.put("chargeCodeSAL06","CHARGE_CODE");
      attribute2dbField.put("chargeCodeSAL06","CHARGE_CODE");
      attribute2dbField.put("progressiveSys10SAL06","PROGRESSIVE_SYS10");
      attribute2dbField.put("valueSAL06","VALUE");
      attribute2dbField.put("percSAL06","PERC");
      attribute2dbField.put("vatCodeReg01SAL06","VAT_CODE_REG01");
      attribute2dbField.put("currencyCodeReg03SAL06","CURRENCY_CODE_REG03");
      attribute2dbField.put("enabledSAL06","ENABLED");

      BigDecimal progressiveSYS10 = null;
      Response res = null;
      for(int i=0;i<list.size();i++) {
        vo = (ChargeVO)list.get(i);
        vo.setEnabledSAL06("Y");

        // insert record in SYS10...
        progressiveSYS10 = TranslationUtils.insertTranslations(vo.getDescriptionSYS10(),vo.getCompanyCodeSys01SAL06(),conn);
        vo.setProgressiveSys10SAL06(progressiveSYS10);



        // insert into SAL06...
        res = CustomizeQueryUtil.insertTable(
            conn,
            new UserSessionParameters(username),
            vo,
            "SAL06_CHARGES",
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
                   "executeCommand", "Error while inserting new charges", ex);
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
  public VOResponse deleteCharges(ArrayList list,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      stmt = conn.createStatement();

      ChargeVO vo = null;
      for(int i=0;i<list.size();i++) {
        // logically delete the record in SAL06...
        vo = (ChargeVO)list.get(i);
        stmt.execute("update SAL06_CHARGES set ENABLED='N' where COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01SAL06()+"' and CHARGE_CODE='"+vo.getChargeCodeSAL06()+"'");
      }

      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing charges",ex);
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

