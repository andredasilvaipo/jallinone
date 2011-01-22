package org.jallinone.variants.server;

import org.openswing.swing.server.*;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;
import org.jallinone.system.translations.server.TranslationUtils;

import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.jallinone.variants.java.*;
import org.jallinone.variants.java.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage variants.</p>
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
public class VariantsBean  implements Variants {


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

  private HashMap productVariants = new HashMap();
  private HashMap variantTypes = new HashMap();
  private HashMap variantTypeJoins = new HashMap();
  private HashMap variantCodeJoins = new HashMap();


  public VariantsBean() {
    productVariants.put("ITM11_VARIANTS_1","ITM16_PRODUCT_VARIANTS_1");
    productVariants.put("ITM12_VARIANTS_2","ITM17_PRODUCT_VARIANTS_2");
    productVariants.put("ITM13_VARIANTS_3","ITM18_PRODUCT_VARIANTS_3");
    productVariants.put("ITM14_VARIANTS_4","ITM19_PRODUCT_VARIANTS_4");
    productVariants.put("ITM15_VARIANTS_5","ITM20_PRODUCT_VARIANTS_5");

    variantTypes.put("ITM11_VARIANTS_1","ITM06_VARIANT_TYPES_1");
    variantTypes.put("ITM12_VARIANTS_2","ITM07_VARIANT_TYPES_2");
    variantTypes.put("ITM13_VARIANTS_3","ITM08_VARIANT_TYPES_3");
    variantTypes.put("ITM14_VARIANTS_4","ITM09_VARIANT_TYPES_4");
    variantTypes.put("ITM15_VARIANTS_5","ITM10_VARIANT_TYPES_5");

    variantTypeJoins.put("ITM11_VARIANTS_1","VARIANT_TYPE_ITM06");
    variantTypeJoins.put("ITM12_VARIANTS_2","VARIANT_TYPE_ITM07");
    variantTypeJoins.put("ITM13_VARIANTS_3","VARIANT_TYPE_ITM08");
    variantTypeJoins.put("ITM14_VARIANTS_4","VARIANT_TYPE_ITM09");
    variantTypeJoins.put("ITM15_VARIANTS_5","VARIANT_TYPE_ITM10");

    variantCodeJoins.put("ITM11_VARIANTS_1","VARIANT_CODE_ITM11");
    variantCodeJoins.put("ITM12_VARIANTS_2","VARIANT_CODE_ITM12");
    variantCodeJoins.put("ITM13_VARIANTS_3","VARIANT_CODE_ITM13");
    variantCodeJoins.put("ITM14_VARIANTS_4","VARIANT_CODE_ITM14");
    variantCodeJoins.put("ITM15_VARIANTS_5","VARIANT_CODE_ITM15");
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
   */
  public VariantNameVO getVariantName(VariantVO vo) {
	  throw new UnsupportedOperationException();
  }


  /**
   * Business logic to execute.
   */
  public VOListResponse loadVariants(GridParams gridParams,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;


      String companyCodeSys01 = (String)gridParams.getOtherGridParams().get(ApplicationConsts.COMPANY_CODE_SYS01);
      String tableName = (String)gridParams.getOtherGridParams().get(ApplicationConsts.TABLE_NAME);
      String variantType = (String)gridParams.getOtherGridParams().get(ApplicationConsts.VARIANT_TYPE);
      String variantTypeJoin = (String)variantTypeJoins.get(tableName);
      if (variantType==null)
        variantType = ApplicationConsts.JOLLY;

      String sql =
          "select "+tableName+".COMPANY_CODE_SYS01,"+tableName+".VARIANT_CODE,"+tableName+"."+variantTypeJoin+","+
          tableName+".CODE_ORDER,"+tableName+".PROGRESSIVE_SYS10,SYS10_TRANSLATIONS.DESCRIPTION,"+tableName+".ENABLED "+
          "from "+tableName+",SYS10_TRANSLATIONS "+
          "where "+tableName+".COMPANY_CODE_SYS01=? and "+
          tableName+".PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          tableName+".ENABLED='Y' and "+tableName+"."+variantTypeJoin+"=? and not "+tableName+".VARIANT_CODE=?" ;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01",tableName+".COMPANY_CODE_SYS01");
      attribute2dbField.put("codeOrder",tableName+".CODE_ORDER");
      attribute2dbField.put("variantCode",tableName+".VARIANT_CODE");
      attribute2dbField.put("variantType",tableName+"."+variantTypeJoin);
      attribute2dbField.put("progressiveSys10",tableName+".PROGRESSIVE_SYS10");
      attribute2dbField.put("descriptionSys10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("enabled",tableName+".ENABLED");

      ArrayList values = new ArrayList();
      values.add(companyCodeSys01);
      values.add(serverLanguageId);
      values.add(variantType);
      values.add(ApplicationConsts.JOLLY);


      // read from ITMxxx table...
      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          VariantVO.class,
          "Y",
          "N",
          null,
          gridParams,
          true
      );
      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching variants list",ex);
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
  public VOListResponse loadVariantsNames(String companyCodeSys01,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      String sql =
          "select ITM21_VARIANTS.COMPANY_CODE_SYS01,ITM21_VARIANTS.TABLE_NAME,ITM21_VARIANTS.PROGRESSIVE_SYS10,"+
          "SYS10_TRANSLATIONS.DESCRIPTION,ITM21_VARIANTS.USE_VARIANT_TYPE "+
          "from ITM21_VARIANTS,SYS10_TRANSLATIONS where "+
          "ITM21_VARIANTS.PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          "ITM21_VARIANTS.COMPANY_CODE_SYS01=?";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01ITM21","ITM21_VARIANTS.COMPANY_CODE_SYS01");
      attribute2dbField.put("tableName","ITM21_VARIANTS.TABLE_NAME");
      attribute2dbField.put("descriptionSYS10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("progressiveSys10ITM21","ITM21_VARIANTS.PROGRESSIVE_SYS10");
      attribute2dbField.put("useVariantTypeITM21","ITM21_VARIANTS.USE_VARIANT_TYPE");


      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(companyCodeSys01);


      // read from ITM21 table...
      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          VariantNameVO.class,
          "Y",
          "N",
          null,
          new GridParams(),
          true
      );


      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching the list of variants names",ex);
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
  public VOListResponse updateVariants(String tableName,ArrayList oldVOs,ArrayList newVOs,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      String variantTypeJoin = (String)variantTypeJoins.get(tableName);

      VariantVO oldVO = null;
      VariantVO newVO = null;
      Response res = null;

      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (VariantVO)oldVOs.get(i);
        newVO = (VariantVO)newVOs.get(i);

        // update SYS10 table...
        TranslationUtils.updateTranslation(oldVO.getDescriptionSys10(),newVO.getDescriptionSys10(),newVO.getProgressiveSys10(),serverLanguageId,conn);

        HashSet pkAttrs = new HashSet();
        pkAttrs.add("companyCodeSys01");
        pkAttrs.add("variantType");
        pkAttrs.add("variantCode");

        HashMap attribute2dbField = new HashMap();
        attribute2dbField.put("companyCodeSys01","COMPANY_CODE_SYS01");
        attribute2dbField.put("codeOrder","CODE_ORDER");
        attribute2dbField.put("variantCode","VARIANT_CODE");
        attribute2dbField.put("variantType",variantTypeJoin);
        attribute2dbField.put("progressiveSys10","PROGRESSIVE_SYS10");
        attribute2dbField.put("enabled","ENABLED");

        res = new QueryUtil().updateTable(
            conn,
            new UserSessionParameters(username),
            pkAttrs,
            oldVO,
            newVO,
            tableName,
            attribute2dbField,
            "Y",
            "N",
            null,
            true
        );
        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }
      }

      return new VOListResponse(newVOs,false,newVOs.size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing variants",ex);
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
  public VOListResponse insertVariants(String tableName,String companyCodeSys01,java.util.List list,String serverLanguageId,String username) throws Throwable {

    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      VariantVO vo = null;
      String variantTypeJoin = (String)variantTypeJoins.get(tableName);

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01","COMPANY_CODE_SYS01");
      attribute2dbField.put("progressiveSys10","PROGRESSIVE_SYS10");
      attribute2dbField.put("enabled","ENABLED");
      attribute2dbField.put("codeOrder","CODE_ORDER");
      attribute2dbField.put("variantCode","VARIANT_CODE");
      attribute2dbField.put("variantType",variantTypeJoin);

      Response res = null;
      BigDecimal progressiveSYS10 = null;
      for(int i=0;i<list.size();i++) {
        vo = (VariantVO)list.get(i);
        vo.setEnabled("Y");
        if (vo.getCompanyCodeSys01()==null)
          vo.setCompanyCodeSys01(companyCodeSys01);
        if (vo.getVariantType()==null)
          vo.setVariantType(ApplicationConsts.JOLLY);

        // insert record in SYS10...
        progressiveSYS10 = TranslationUtils.insertTranslations(vo.getDescriptionSys10(),vo.getCompanyCodeSys01(),conn);
        vo.setProgressiveSys10(progressiveSYS10);

        // insert into ITMxxx...
        res = QueryUtil.insertTable(
            conn,
            new UserSessionParameters(username),
            vo,
            tableName,
            attribute2dbField,
            "Y",
            "N",
            null,
            true
        );
        if (res.isError()) {
        	throw new Exception(res.getErrorMessage());
        }
      }


      return new VOListResponse(list,false,list.size());
    }
    catch (Throwable ex) {
      Logger.error(username, this.getClass().getName(),
                   "executeCommand", "Error while inserting new variants", ex);
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
  public VOResponse deleteVariants(String tableName,ArrayList list,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;

    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      stmt = conn.createStatement();

      String variantTypeJoin = (String)variantTypeJoins.get(tableName);

      VariantVO vo = null;
      for(int i=0;i<list.size();i++) {
        // logically delete the record in ITMxxx...
        vo = (VariantVO)list.get(i);
        stmt.execute(
          "update "+tableName+" set ENABLED='N' "+
          "where COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01()+"' and VARIANT_CODE='"+vo.getVariantCode()+"' and "+variantTypeJoin+"='"+vo.getVariantType()+"'"
        );
      }

      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing variant types",ex);
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


  /**
   * Business logic to execute.
   */
  public VOListResponse loadAllVariants(String tableName,String variantTypeJoin,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      String sql =
          "select "+tableName+".COMPANY_CODE_SYS01,"+tableName+".VARIANT_CODE,"+tableName+"."+variantTypeJoin+","+
          tableName+".CODE_ORDER,"+tableName+".PROGRESSIVE_SYS10,SYS10_TRANSLATIONS.DESCRIPTION,"+tableName+".ENABLED "+
          "from "+tableName+",SYS10_TRANSLATIONS "+
          "where "+
          tableName+".PROGRESSIVE_SYS10=SYS10_TRANSLATIONS.PROGRESSIVE and "+
          "SYS10_TRANSLATIONS.LANGUAGE_CODE=? and "+
          tableName+".ENABLED='Y'  and not "+tableName+".VARIANT_CODE=?" ;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01",tableName+".COMPANY_CODE_SYS01");
      attribute2dbField.put("codeOrder",tableName+".CODE_ORDER");
      attribute2dbField.put("variantCode",tableName+".VARIANT_CODE");
      attribute2dbField.put("variantType",tableName+"."+variantTypeJoin);
      attribute2dbField.put("progressiveSys10",tableName+".PROGRESSIVE_SYS10");
      attribute2dbField.put("descriptionSys10","SYS10_TRANSLATIONS.DESCRIPTION");
      attribute2dbField.put("enabled",tableName+".ENABLED");

      ArrayList values = new ArrayList();
      values.add(serverLanguageId);
      values.add(ApplicationConsts.JOLLY);


      // read from ITMxxx table...
      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          VariantVO.class,
          "Y",
          "N",
          null,
          new GridParams(),
          true
      );
      if (answer.isError())
        throw new Exception(answer.getErrorMessage());

      return (VOListResponse)answer;
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching variants list",ex);
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


}

