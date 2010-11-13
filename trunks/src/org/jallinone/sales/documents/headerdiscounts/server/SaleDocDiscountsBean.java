package org.jallinone.sales.documents.headerdiscounts.server;

import org.openswing.swing.server.*;

import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.message.send.java.LookupValidationParams;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;
import org.jallinone.sales.discounts.java.DiscountVO;
import org.jallinone.sales.discounts.server.DiscountBean;
import org.jallinone.sales.documents.headerdiscounts.java.*;
import org.jallinone.sales.documents.server.*;
import org.jallinone.sales.documents.java.*;
import org.jallinone.sales.documents.java.*;
import org.jallinone.sales.documents.server.*;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage  customer discounts for a sale document.</p>
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
public class SaleDocDiscountsBean  implements SaleDocDiscounts {


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



  private UpdateTaxableIncomesBean totals;

  public void setTotals(UpdateTaxableIncomesBean totals) {
    this.totals = totals;
  }

  private DiscountBean discountBean;

  public void setDiscountBean(DiscountBean discountBean) {
	  this.discountBean = discountBean;
  }




  private SaleDocTotalsBean totalBean;
  private LoadSaleDocBean docBean;
  private SaleDocRowsBean rowBean;

  public void setTotalBean(SaleDocTotalsBean totalBean) {
	  this.totalBean = totalBean;
  }

  public void setDocBean(LoadSaleDocBean docBean) {
	  this.docBean = docBean;
  }

  public void setRowBean(SaleDocRowsBean rowBean) {
	  this.rowBean = rowBean;
  }


  public SaleDocDiscountsBean() {
  }



  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
   */
  public DiscountVO getDetailSaleDoc(DetailSaleDocVO pk) {
	  throw new UnsupportedOperationException();
  }





  /**
   * Business logic to execute.
   */
  public VOListResponse loadSaleHeaderDiscounts(GridParams gridParams,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      discountBean.setConn(conn);
      DetailSaleDocVO vo = (DetailSaleDocVO)gridParams.getOtherGridParams().get(ApplicationConsts.SALE_DOC_VO);

      // retrieve customer discount codes...
      stmt = conn.createStatement();
      ResultSet rset = stmt.executeQuery(
          "select DISCOUNT_CODE_SAL03 from SAL08_CUSTOMER_DISCOUNTS,SAL03_DISCOUNTS where "+
          "SAL08_CUSTOMER_DISCOUNTS.COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01DOC01()+"' and PROGRESSIVE_REG04="+vo.getProgressiveReg04DOC01()+" and "+
          "SAL08_CUSTOMER_DISCOUNTS.COMPANY_CODE_SYS01=SAL03_DISCOUNTS.COMPANY_CODE_SYS01 and "+
          "SAL08_CUSTOMER_DISCOUNTS.DISCOUNT_CODE_SAL03=SAL03_DISCOUNTS.DISCOUNT_CODE and "+
          "SAL03_DISCOUNTS.CURRENCY_CODE_REG03='"+vo.getCurrencyCodeReg03DOC01()+"'"
      );
      ArrayList discountCodes = new ArrayList();
      while(rset.next()) {
        discountCodes.add( rset.getString(1) );
      }
      rset.close();

      // retrieve customer hierarchy discount codes...
      rset = stmt.executeQuery(
          "select DISCOUNT_CODE_SAL03 from SAL10_SUBJECT_HIERAR_DISCOUNTS,SAL03_DISCOUNTS where "+
          "SAL10_SUBJECT_HIERAR_DISCOUNTS.COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01DOC01()+"' and PROGRESSIVE_HIE01 in "+
          "(select PROGRESSIVE_HIE01 from REG16_SUBJECTS_LINKS where COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01DOC01()+"' and PROGRESSIVE_REG04="+vo.getProgressiveReg04DOC01()+") and "+
          "SAL10_SUBJECT_HIERAR_DISCOUNTS.COMPANY_CODE_SYS01=SAL03_DISCOUNTS.COMPANY_CODE_SYS01 and "+
          "SAL10_SUBJECT_HIERAR_DISCOUNTS.DISCOUNT_CODE_SAL03=SAL03_DISCOUNTS.DISCOUNT_CODE and "+
          "SAL03_DISCOUNTS.CURRENCY_CODE_REG03='"+vo.getCurrencyCodeReg03DOC01()+"'"

      );
      while(rset.next()) {
        discountCodes.add( rset.getString(1) );
      }
      rset.close();

      Response res = discountBean.getDiscountsList(
          vo.getCompanyCodeSys01DOC01(),
          discountCodes,
          serverLanguageId,
          gridParams,
          username,
          DiscountVO.class
      );

      if (!res.isError()) {
        java.util.List list = ((VOListResponse)res).getRows();
        DiscountVO discVO = null;
        int i=0;
        while(i<list.size()) {
          discVO = (DiscountVO)list.get(i);
          if (discVO.getStartDateSAL03().getTime()>System.currentTimeMillis() ||
              discVO.getEndDateSAL03().getTime()<System.currentTimeMillis())
            list.remove(i);
          else
            i++;
        }
        return new VOListResponse(list,false,list.size());
      }
      else
    	  throw new Exception(res.getErrorMessage());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching customer discounts list",ex);
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
  public VOListResponse updateSaleDocDiscounts(
      HashMap variant1Descriptions,
      HashMap variant2Descriptions,
      HashMap variant3Descriptions,
      HashMap variant4Descriptions,
      HashMap variant5Descriptions,
      ArrayList oldVOs, ArrayList newVOs, String serverLanguageId,
      String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      totals.setConn(conn); // use same transaction...

      SaleDocDiscountVO oldVO = null;
      SaleDocDiscountVO newVO = null;
      Response res = null;

      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (SaleDocDiscountVO)oldVOs.get(i);
        newVO = (SaleDocDiscountVO)newVOs.get(i);

        HashSet pkAttrs = new HashSet();
        pkAttrs.add("companyCodeSys01DOC05");
        pkAttrs.add("docTypeDOC05");
        pkAttrs.add("docYearDOC05");
        pkAttrs.add("docNumberDOC05");
        pkAttrs.add("discountCodeSal03DOC05");

        HashMap attribute2dbField = new HashMap();
        attribute2dbField.put("companyCodeSys01DOC05","COMPANY_CODE_SYS01");
        attribute2dbField.put("discountCodeSal03DOC05","DISCOUNT_CODE_SAL03");
        attribute2dbField.put("minValueDOC05","MIN_VALUE");
        attribute2dbField.put("maxValueDOC05","MAX_VALUE");
        attribute2dbField.put("minPercDOC05","MIN_PERC");
        attribute2dbField.put("maxPercDOC05","MAX_PERC");
        attribute2dbField.put("startDateDOC05","START_DATE");
        attribute2dbField.put("endDateDOC05","END_DATE");
        attribute2dbField.put("discountDescriptionDOC05","DISCOUNT_DESCRIPTION");
        attribute2dbField.put("valueDOC05","VALUE");
        attribute2dbField.put("percDOC05","PERC");
        attribute2dbField.put("docTypeDOC05","DOC_TYPE");
        attribute2dbField.put("docYearDOC05","DOC_YEAR");
        attribute2dbField.put("docNumberDOC05","DOC_NUMBER");


        res = new QueryUtil().updateTable(
            conn,
            new UserSessionParameters(username),
            pkAttrs,
            oldVO,
            newVO,
            "DOC05_SELLING_DISCOUNTS",
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

      res = totals.updateTaxableIncomes(
        variant1Descriptions,
        variant2Descriptions,
        variant3Descriptions,
        variant4Descriptions,
        variant5Descriptions,
        new SaleDocPK(newVO.getCompanyCodeSys01DOC05(),newVO.getDocTypeDOC05(),newVO.getDocYearDOC05(),newVO.getDocNumberDOC05()),
        serverLanguageId,
        username

      );
      if (res.isError()) {
        throw new Exception(res.getErrorMessage());
      }

      return new VOListResponse(newVOs,false,newVOs.size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing customer discounts",ex);
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
    	try {
    		totals.setConn(null);
    	} catch (Exception ex) {}
    }

  }





  /**
   * Business logic to execute.
   */
  public VOListResponse insertSaleDocDiscounts(
      HashMap variant1Descriptions,
      HashMap variant2Descriptions,
      HashMap variant3Descriptions,
      HashMap variant4Descriptions,
      HashMap variant5Descriptions,
      ArrayList list, String serverLanguageId, String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      totals.setConn(conn); // use same transaction...

      SaleDocDiscountVO vo = null;

      Response res = null;
      for(int i=0;i<list.size();i++) {
        vo = (SaleDocDiscountVO)list.get(i);
        // insert into DOC05...
        res = insertSaleDocDiscount(
          vo,
          username
        );
        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }
        list.set(i,((VOResponse)res).getVo());
      }

      pstmt = conn.prepareStatement("update DOC01_SELLING set DOC_STATE=? where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=?");
      pstmt.setString(1,ApplicationConsts.HEADER_BLOCKED);
      pstmt.setString(2,vo.getCompanyCodeSys01DOC05());
      pstmt.setString(3,vo.getDocTypeDOC05());
      pstmt.setBigDecimal(4,vo.getDocYearDOC05());
      pstmt.setBigDecimal(5,vo.getDocNumberDOC05());
      pstmt.execute();

      res = totals.updateTaxableIncomes(
        variant1Descriptions,
        variant2Descriptions,
        variant3Descriptions,
        variant4Descriptions,
        variant5Descriptions,
        new SaleDocPK(vo.getCompanyCodeSys01DOC05(),vo.getDocTypeDOC05(),vo.getDocYearDOC05(),vo.getDocNumberDOC05()),
        serverLanguageId,
        username

      );
      if (res.isError()) {
        throw new Exception(res.getErrorMessage());
      }

      return new VOListResponse(list,false,list.size());
    }
    catch (Throwable ex) {
      Logger.error(username, this.getClass().getName(),
                   "executeCommand", "Error while inserting new customer discounts", ex);
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
      try {
        totals.setConn(null);
      } catch (Exception ex) {}
    }

  }




  /**
   * Insert a new customer discount for a sale document.
   * No commit or rollback is executed; no connection is created or released.
   */
  public VOResponse insertSaleDocDiscount(SaleDocDiscountVO vo,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC05","COMPANY_CODE_SYS01");
      attribute2dbField.put("discountCodeSal03DOC05","DISCOUNT_CODE_SAL03");
      attribute2dbField.put("minValueDOC05","MIN_VALUE");
      attribute2dbField.put("maxValueDOC05","MAX_VALUE");
      attribute2dbField.put("minPercDOC05","MIN_PERC");
      attribute2dbField.put("maxPercDOC05","MAX_PERC");
      attribute2dbField.put("startDateDOC05","START_DATE");
      attribute2dbField.put("endDateDOC05","END_DATE");
      attribute2dbField.put("discountDescriptionDOC05","DISCOUNT_DESCRIPTION");
      attribute2dbField.put("valueDOC05","VALUE");
      attribute2dbField.put("percDOC05","PERC");
      attribute2dbField.put("docTypeDOC05","DOC_TYPE");
      attribute2dbField.put("docYearDOC05","DOC_YEAR");
      attribute2dbField.put("docNumberDOC05","DOC_NUMBER");

      Response res = null;
      // insert into DOC05...
      res = QueryUtil.insertTable(
          conn,
          new UserSessionParameters(username),
          vo,
          "DOC05_SELLING_DISCOUNTS",
          attribute2dbField,
          "Y",
          "N",
          null,
          true
      );
      if (res.isError()) {
        throw new Exception(res.getErrorMessage());
      }

      return new VOResponse(vo);
    }
    catch (Throwable ex) {
      Logger.error(username, this.getClass().getName(),
                   "insertSaleDocDiscount", "Error while inserting a new customer discount", ex);
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
  public VOResponse deleteSaleDocDiscounts(
      HashMap variant1Descriptions,
      HashMap variant2Descriptions,
      HashMap variant3Descriptions,
      HashMap variant4Descriptions,
      HashMap variant5Descriptions,
      ArrayList list, String serverLanguageId, String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      totals.setConn(conn); // use same transaction...
      SaleDocDiscountVO vo = null;

      pstmt = conn.prepareStatement(
          "delete from DOC05_SELLING_DISCOUNTS where "+
          "DOC05_SELLING_DISCOUNTS.COMPANY_CODE_SYS01=? and "+
          "DOC05_SELLING_DISCOUNTS.DOC_TYPE=? and "+
          "DOC05_SELLING_DISCOUNTS.DOC_YEAR=? and "+
          "DOC05_SELLING_DISCOUNTS.DOC_NUMBER=? and "+
          "DOC05_SELLING_DISCOUNTS.DISCOUNT_CODE_SAL03=?");

      for(int i=0;i<list.size();i++) {
        // phisically delete the record in DOC05...
        vo = (SaleDocDiscountVO)list.get(i);
        pstmt.setString(1,vo.getCompanyCodeSys01DOC05());
        pstmt.setString(2,vo.getDocTypeDOC05());
        pstmt.setBigDecimal(3,vo.getDocYearDOC05());
        pstmt.setBigDecimal(4,vo.getDocNumberDOC05());
        pstmt.setString(5,vo.getDiscountCodeSal03DOC05());
        pstmt.execute();
      }

      Response res = totals.updateTaxableIncomes(
        variant1Descriptions,
        variant2Descriptions,
        variant3Descriptions,
        variant4Descriptions,
        variant5Descriptions,
        new SaleDocPK(vo.getCompanyCodeSys01DOC05(),vo.getDocTypeDOC05(),vo.getDocYearDOC05(),vo.getDocNumberDOC05()),
        serverLanguageId,
        username
      );
      if (res.isError()) {
        throw new Exception(res.getErrorMessage());
      }

      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing customer discounts",ex);
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
      try {
        totals.setConn(null);
      } catch (Exception ex) {}
    }

  }



  /**
   * Recalculate item row totals and document totals.
   * @return Boolean.TRUE if totals are correctly calculated
   */
  public VOResponse updateTotals(SaleDocPK docPK,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      totalBean.setConn(conn); // use same transaction...
      docBean.setConn(conn); // use same transaction...
      rowBean.setConn(conn); // use same transaction...

      DetailSaleDocVO vo = docBean.loadSaleDoc(docPK,serverLanguageId,username,new ArrayList());

      // recalculate document totals...
      Response totalResponse = totalBean.getSaleDocTotals(vo,serverLanguageId,username);
      if (totalResponse.isError())
    	  throw new Exception(totalResponse.getErrorMessage());

      pstmt = conn.prepareStatement("update DOC01_SELLING set TAXABLE_INCOME=?,TOTAL_VAT=?,TOTAL=? where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=?");
      pstmt.setBigDecimal(1,vo.getTaxableIncomeDOC01());
      pstmt.setBigDecimal(2,vo.getTotalVatDOC01());
      pstmt.setBigDecimal(3,vo.getTotalDOC01());
      pstmt.setString(4,docPK.getCompanyCodeSys01DOC01());
      pstmt.setString(5,docPK.getDocTypeDOC01());
      pstmt.setBigDecimal(6,docPK.getDocYearDOC01());
      pstmt.setBigDecimal(7,docPK.getDocNumberDOC01());
      pstmt.execute();

      conn.commit();
      return new VOResponse(Boolean.TRUE);
    }
    catch (Throwable ex) {
      Logger.error(username,"org.jallinone.sales.documents.headerdiscounts.server.UpdateSaleDocTotalDiscountBean","updateTotals","Error while updating document totals:\n"+ex.getMessage(), ex);
      try {
    	  if (this.conn==null && conn!=null)
    		  // rollback only local connection
    		  conn.rollback();
      }
      catch (Exception ex3) {
      }

      throw ex;
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
      try {
          totalBean.setConn(null);
          docBean.setConn(null);
          rowBean.setConn(null);
      } catch (Exception ex) {}
    }

  }




  /**
   * Business logic to execute.
   */
  public VOListResponse validateSaleHeaderDiscountCode(LookupValidationParams validationPars,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      DetailSaleDocVO vo = (DetailSaleDocVO)validationPars.getLookupValidationParameters().get(ApplicationConsts.SALE_DOC_VO);

      // retrieve customer discount codes...
      stmt = conn.createStatement();
      ResultSet rset = stmt.executeQuery(
          "select DISCOUNT_CODE_SAL03 from SAL08_CUSTOMER_DISCOUNTS,SAL03_DISCOUNTS where "+
          "SAL08_CUSTOMER_DISCOUNTS.COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01DOC01()+"' and PROGRESSIVE_REG04="+vo.getProgressiveReg04DOC01()+" and "+
          "DISCOUNT_CODE_SAL03='"+validationPars.getCode()+"' and "+
          "SAL08_CUSTOMER_DISCOUNTS.COMPANY_CODE_SYS01=SAL03_DISCOUNTS.COMPANY_CODE_SYS01 and "+
          "SAL08_CUSTOMER_DISCOUNTS.DISCOUNT_CODE_SAL03=SAL03_DISCOUNTS.DISCOUNT_CODE and "+
          "SAL03_DISCOUNTS.CURRENCY_CODE_REG03='"+vo.getCurrencyCodeReg03DOC01()+"'"

      );
      ArrayList discountCodes = new ArrayList();
      while(rset.next()) {
        discountCodes.add( rset.getString(1) );
      }
      rset.close();

      // retrieve customer hierarchy discount codes...
      rset = stmt.executeQuery(
          "select DISCOUNT_CODE_SAL03 from SAL10_SUBJECT_HIERAR_DISCOUNTS,SAL03_DISCOUNTS where "+
          "SAL10_SUBJECT_HIERAR_DISCOUNTS.COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01DOC01()+"' and PROGRESSIVE_HIE01 in "+
          "(select PROGRESSIVE_HIE01 from REG16_SUBJECTS_LINKS where COMPANY_CODE_SYS01='"+vo.getCompanyCodeSys01DOC01()+"' and PROGRESSIVE_REG04="+vo.getProgressiveReg04DOC01()+") and "+
          "DISCOUNT_CODE_SAL03='"+validationPars.getCode()+"' and "+
          "SAL10_SUBJECT_HIERAR_DISCOUNTS.COMPANY_CODE_SYS01=SAL03_DISCOUNTS.COMPANY_CODE_SYS01 and "+
          "SAL10_SUBJECT_HIERAR_DISCOUNTS.DISCOUNT_CODE_SAL03=SAL03_DISCOUNTS.DISCOUNT_CODE and "+
          "SAL03_DISCOUNTS.CURRENCY_CODE_REG03='"+vo.getCurrencyCodeReg03DOC01()+"'"
      );
      while(rset.next()) {
        discountCodes.add( rset.getString(1) );
      }
      rset.close();

      Response res = discountBean.getDiscountsList(
          vo.getCompanyCodeSys01DOC01(),
          discountCodes,
          serverLanguageId,
          new GridParams(),
          username,
          DiscountVO.class
      );

      if (!res.isError()) {
        java.util.List list = ((VOListResponse)res).getRows();
        DiscountVO discVO = null;
        int i=0;
        while(i<list.size()) {
          discVO = (DiscountVO)list.get(i);
          if (discVO.getStartDateSAL03().getTime()>System.currentTimeMillis() ||
              discVO.getEndDateSAL03().getTime()<System.currentTimeMillis())
            list.remove(i);
          else
            i++;
        }
        return new VOListResponse(list,false,list.size());
      }

      Response answer = res;
      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while validating a customer discount code",ex);
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

