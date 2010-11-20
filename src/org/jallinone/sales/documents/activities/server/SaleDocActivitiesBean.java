package org.jallinone.sales.documents.activities.server;

import org.openswing.swing.server.*;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;
import org.jallinone.sales.documents.activities.java.*;
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
 * * <p>Description: Bean used to manage sale activities for a sale document.</p>
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
public class SaleDocActivitiesBean  implements SaleDocActivities {


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


  public SaleDocActivitiesBean() {
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




  /**
   * Recalculate item row totals and document totals.
   * @return Boolean.TRUE if totals are correctly calculated
   */
  public VOResponse updateTotals(SaleDocPK docPK,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    PreparedStatement pstmt = null;
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
      Logger.error(username,"org.jallinone.sales.documents.activities.server.UpdateSaleDocTotalActivityBean","updateTotals","Error while updating document totals:\n"+ex.getMessage(), ex);
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
  public VOListResponse updateSaleDocActivities(
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

      SaleDocActivityVO oldVO = null;
      SaleDocActivityVO newVO = null;
      Response res = null;

      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (SaleDocActivityVO)oldVOs.get(i);
        newVO = (SaleDocActivityVO)newVOs.get(i);

        HashSet pkAttrs = new HashSet();
        pkAttrs.add("companyCodeSys01DOC13");
        pkAttrs.add("docTypeDOC13");
        pkAttrs.add("docYearDOC13");
        pkAttrs.add("docNumberDOC13");
        pkAttrs.add("activityCodeSal09DOC13");

        HashMap attribute2dbField = new HashMap();
        attribute2dbField.put("companyCodeSys01DOC13","COMPANY_CODE_SYS01");
        attribute2dbField.put("activityCodeSal09DOC13","ACTIVITY_CODE_SAL09");
        attribute2dbField.put("valueSal09DOC13","VALUE_SAL09");
        attribute2dbField.put("activityDescriptionDOC13","ACTIVITY_DESCRIPTION");
        attribute2dbField.put("valueDOC13","VALUE");
        attribute2dbField.put("docTypeDOC13","DOC_TYPE");
        attribute2dbField.put("docYearDOC13","DOC_YEAR");
        attribute2dbField.put("docNumberDOC13","DOC_NUMBER");
        attribute2dbField.put("vatCodeSal09DOC13","VAT_CODE_SAL09");
        attribute2dbField.put("vatDescriptionDOC13","VAT_DESCRIPTION");
//        attribute2dbField.put("vatValueDOC13","VAT_VALUE"); this field is updated from  UpdateTaxableIncomesBean...
        attribute2dbField.put("vatDeductibleDOC13","VAT_DEDUCTIBLE");
        attribute2dbField.put("durationDOC13","DURATION");
        attribute2dbField.put("progressiveSch06DOC13","PROGRESSIVE_SCH06");
        attribute2dbField.put("currencyCodeReg03DOC13","CURRENCY_CODE_REG03");
        attribute2dbField.put("invoicedValueDOC13","INVOICED_VALUE");
        attribute2dbField.put("valueReg01DOC13","VALUE_REG01");
//        attribute2dbField.put("taxableIncomeDOC13","TAXABLE_INCOME"); this field is updated from  UpdateTaxableIncomesBean...


        res = new QueryUtil().updateTable(
            conn,
            new UserSessionParameters(username),
            pkAttrs,
            oldVO,
            newVO,
            "DOC13_SELLING_ACTIVITIES",
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
        new SaleDocPK(newVO.getCompanyCodeSys01DOC13(),newVO.getDocTypeDOC13(),newVO.getDocYearDOC13(),newVO.getDocNumberDOC13()),
        serverLanguageId,
        username
      );
      if (res.isError()) {
        throw new Exception(res.getErrorMessage());
      }

      return new VOListResponse(newVOs,false,newVOs.size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing sale activitys",ex);
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
   * Insert new activity in the sale document.
   * It does not execute any commit or rollback operations.
   */
  public VOResponse insertSaleActivity(
      SaleDocActivityVO vo, String username) throws Exception {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      if (vo.getInvoicedValueDOC13()==null)
          vo.setInvoicedValueDOC13(new BigDecimal(0));
      vo.setTaxableIncomeDOC13(vo.getValueDOC13());
      vo.setVatValueDOC13(new BigDecimal(0));

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC13","COMPANY_CODE_SYS01");
      attribute2dbField.put("activityCodeSal09DOC13","ACTIVITY_CODE_SAL09");
      attribute2dbField.put("valueSal09DOC13","VALUE_SAL09");
      attribute2dbField.put("activityDescriptionDOC13","ACTIVITY_DESCRIPTION");
      attribute2dbField.put("valueDOC13","VALUE");
      attribute2dbField.put("docTypeDOC13","DOC_TYPE");
      attribute2dbField.put("docYearDOC13","DOC_YEAR");
      attribute2dbField.put("docNumberDOC13","DOC_NUMBER");
      attribute2dbField.put("vatCodeSal09DOC13","VAT_CODE_SAL09");
      attribute2dbField.put("vatDescriptionDOC13","VAT_DESCRIPTION");
      attribute2dbField.put("vatValueDOC13","VAT_VALUE");
      attribute2dbField.put("vatDeductibleDOC13","VAT_DEDUCTIBLE");
      attribute2dbField.put("durationDOC13","DURATION");
      attribute2dbField.put("currencyCodeReg03DOC13","CURRENCY_CODE_REG03");
      attribute2dbField.put("progressiveSch06DOC13","PROGRESSIVE_SCH06");
      attribute2dbField.put("invoicedValueDOC13","INVOICED_VALUE");
      attribute2dbField.put("valueReg01DOC13","VALUE_REG01");
      attribute2dbField.put("taxableIncomeDOC13","TAXABLE_INCOME");


      // insert into DOC13...
      Response res = QueryUtil.insertTable(
          conn,
          new UserSessionParameters(username),
          vo,
          "DOC13_SELLING_ACTIVITIES",
          attribute2dbField,
          "Y",
          "N",
          null,
          true
      );

      if (res.isError()) {
        throw new Exception(res.getErrorMessage());
      }

      pstmt = conn.prepareStatement("update DOC01_SELLING set DOC_STATE=? where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=?");
      pstmt.setString(1,ApplicationConsts.HEADER_BLOCKED);
      pstmt.setString(2,vo.getCompanyCodeSys01DOC13());
      pstmt.setString(3,vo.getDocTypeDOC13());
      pstmt.setBigDecimal(4,vo.getDocYearDOC13());
      pstmt.setBigDecimal(5,vo.getDocNumberDOC13());
      pstmt.execute();

      return new VOResponse(vo);
    }
    catch (Throwable ex) {
      Logger.error(username, this.getClass().getName(),
                   "executeCommand", "Error while inserting a new sale activity", ex);
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
  public VOResponse deleteSaleDocActivities(
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

      SaleDocActivityVO vo = null;

      pstmt = conn.prepareStatement(
          "delete from DOC13_SELLING_ACTIVITIES where "+
          "DOC13_SELLING_ACTIVITIES.COMPANY_CODE_SYS01=? and "+
          "DOC13_SELLING_ACTIVITIES.DOC_TYPE=? and "+
          "DOC13_SELLING_ACTIVITIES.DOC_YEAR=? and "+
          "DOC13_SELLING_ACTIVITIES.DOC_NUMBER=? and "+
          "DOC13_SELLING_ACTIVITIES.ACTIVITY_CODE_SAL09=?"
      );

      for(int i=0;i<list.size();i++) {
        // phisically delete the record in DOC13...
        vo = (SaleDocActivityVO)list.get(i);
        pstmt.setString(1,vo.getCompanyCodeSys01DOC13());
        pstmt.setString(2,vo.getDocTypeDOC13());
        pstmt.setBigDecimal(3,vo.getDocYearDOC13());
        pstmt.setBigDecimal(4,vo.getDocNumberDOC13());
        pstmt.setString(5,vo.getActivityCodeSal09DOC13());
        pstmt.execute();
      }

      Response res = totals.updateTaxableIncomes(
        variant1Descriptions,
        variant2Descriptions,
        variant3Descriptions,
        variant4Descriptions,
        variant5Descriptions,
        new SaleDocPK(vo.getCompanyCodeSys01DOC13(),vo.getDocTypeDOC13(),vo.getDocYearDOC13(),vo.getDocNumberDOC13()),
        serverLanguageId,
        username
      );
      if (res.isError()) {
        throw new Exception(res.getErrorMessage());
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
   * Business logic to execute.
   */
  public VOListResponse insertSaleDocActivities(
      HashMap variant1Descriptions,
      HashMap variant2Descriptions,
      HashMap variant3Descriptions,
      HashMap variant4Descriptions,
      HashMap variant5Descriptions,
      ArrayList list,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      totals.setConn(conn); // use same transaction...
      SaleDocActivityVO vo = null;
      Response res = null;

      for(int i=0;i<list.size();i++) {
        vo = (SaleDocActivityVO)list.get(i);
        res = insertSaleActivity(vo,username);
        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }
      }

      pstmt = conn.prepareStatement("update DOC01_SELLING set DOC_STATE=? where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=?");
      pstmt.setString(1,ApplicationConsts.HEADER_BLOCKED);
      pstmt.setString(2,vo.getCompanyCodeSys01DOC13());
      pstmt.setString(3,vo.getDocTypeDOC13());
      pstmt.setBigDecimal(4,vo.getDocYearDOC13());
      pstmt.setBigDecimal(5,vo.getDocNumberDOC13());
      pstmt.execute();

      res = totals.updateTaxableIncomes(
        variant1Descriptions,
        variant2Descriptions,
        variant3Descriptions,
        variant4Descriptions,
        variant5Descriptions,
        new SaleDocPK(vo.getCompanyCodeSys01DOC13(),vo.getDocTypeDOC13(),vo.getDocYearDOC13(),vo.getDocNumberDOC13()),
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




}

