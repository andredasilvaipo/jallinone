package org.jallinone.sales.documents.headercharges.server;

import org.openswing.swing.server.*;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;
import org.jallinone.sales.documents.headercharges.java.*;
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
 * * <p>Description: Bean used to manage charges for a sale document.</p>
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
public class SaleDocChargesBean  implements SaleDocCharges {


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




  public SaleDocChargesBean() {
  }




  /**
   * Recalculate item row totals and document totals.
   * @return Boolean.TRUE if totals are correctly calculated
   */
  public Response updateTotals(SaleDocPK docPK,String serverLanguageId,String username) throws Throwable {
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
        return totalResponse;

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
      Logger.error(username,"org.jallinone.sales.documents.headercharges.server.UpdateSaleDocTotalChargeBean","updateTotals","Error while updating document totals:\n"+ex.getMessage(), ex);
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
          totalBean.setConn(null);
          docBean.setConn(null);
          rowBean.setConn(null);
      } catch (Exception ex) {}
    }

  }

  /**
   * Business logic to execute.
   */
  public VOListResponse updateSaleDocCharges(
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

      SaleDocChargeVO oldVO = null;
      SaleDocChargeVO newVO = null;
      Response res = null;

      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (SaleDocChargeVO)oldVOs.get(i);
        newVO = (SaleDocChargeVO)newVOs.get(i);
        if (newVO.getValueDOC03()!=null)
          newVO.setInvoicedValueDOC03(new BigDecimal(0));
        else
          newVO.setInvoicedValueDOC03(null);

        HashSet pkAttrs = new HashSet();
        pkAttrs.add("companyCodeSys01DOC03");
        pkAttrs.add("docTypeDOC03");
        pkAttrs.add("docYearDOC03");
        pkAttrs.add("docNumberDOC03");
        pkAttrs.add("chargeCodeSal06DOC03");

        HashMap attribute2dbField = new HashMap();
        attribute2dbField.put("companyCodeSys01DOC03","COMPANY_CODE_SYS01");
        attribute2dbField.put("chargeCodeSal06DOC03","CHARGE_CODE_SAL06");
        attribute2dbField.put("valueSal06DOC03","VALUE_SAL06");
        attribute2dbField.put("percSal06DOC03","PERC_SAL06");
        attribute2dbField.put("chargeDescriptionDOC03","CHARGE_DESCRIPTION");
        attribute2dbField.put("valueDOC03","VALUE");
        attribute2dbField.put("percDOC03","PERC");
        attribute2dbField.put("docTypeDOC03","DOC_TYPE");
        attribute2dbField.put("docYearDOC03","DOC_YEAR");
        attribute2dbField.put("docNumberDOC03","DOC_NUMBER");
        attribute2dbField.put("vatCodeSal06DOC03","VAT_CODE_SAL06");
        attribute2dbField.put("vatDescriptionDOC03","VAT_DESCRIPTION");
//        attribute2dbField.put("vatValueDOC03","VAT_VALUE"); this field is updated from  UpdateTaxableIncomesBean...
        attribute2dbField.put("vatDeductibleDOC03","VAT_DEDUCTIBLE");
        attribute2dbField.put("invoicedValueDOC03","INVOICED_VALUE");
        attribute2dbField.put("valueReg01DOC03","VALUE_REG01");
//        attribute2dbField.put("taxableIncomeDOC03","TAXABLE_INCOME"); this field is updated from  UpdateTaxableIncomesBean...

        res = new QueryUtil().updateTable(
            conn,
            new UserSessionParameters(username),
            pkAttrs,
            oldVO,
            newVO,
            "DOC03_SELLING_CHARGES",
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
        new SaleDocPK(newVO.getCompanyCodeSys01DOC03(),newVO.getDocTypeDOC03(),newVO.getDocYearDOC03(),newVO.getDocNumberDOC03()),
        serverLanguageId,
        username
      );
      if (res.isError()) {
        throw new Exception(res.getErrorMessage());
      }

      return new VOListResponse(newVOs,false,newVOs.size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing sale charges",ex);
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
  public VOListResponse insertSaleDocCharges(
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

      SaleDocChargeVO vo = null;

      Response res = null;
      for(int i=0;i<list.size();i++) {
        vo = (SaleDocChargeVO)list.get(i);
        res = insertSaleDocCharge(
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
      pstmt.setString(2,vo.getCompanyCodeSys01DOC03());
      pstmt.setString(3,vo.getDocTypeDOC03());
      pstmt.setBigDecimal(4,vo.getDocYearDOC03());
      pstmt.setBigDecimal(5,vo.getDocNumberDOC03());
      pstmt.execute();

      res = totals.updateTaxableIncomes(
        variant1Descriptions,
        variant2Descriptions,
        variant3Descriptions,
        variant4Descriptions,
        variant5Descriptions,
        new SaleDocPK(vo.getCompanyCodeSys01DOC03(),vo.getDocTypeDOC03(),vo.getDocYearDOC03(),vo.getDocNumberDOC03()),
        serverLanguageId,
        username

      );
      if (res.isError()) {
        throw new Exception(res.getErrorMessage());
      }

      return  new VOListResponse(list,false,list.size());
    }
    catch (Throwable ex) {
      Logger.error(username, this.getClass().getName(),
                   "executeCommand", "Error while inserting new sale charges", ex);
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
   * Insert a new charge for a sale document.
   * No commit or rollback are executed; no connection is created or released.
   */
  public Response insertSaleDocCharge(SaleDocChargeVO vo,String username) throws Throwable {
	  Connection conn = null;
	  try {
		  if (this.conn==null) conn = getConn(); else conn = this.conn;

		  Map attribute2dbField = new HashMap();
		  attribute2dbField.put("companyCodeSys01DOC03","COMPANY_CODE_SYS01");
		  attribute2dbField.put("chargeCodeSal06DOC03","CHARGE_CODE_SAL06");
		  attribute2dbField.put("valueSal06DOC03","VALUE_SAL06");
		  attribute2dbField.put("percSal06DOC03","PERC_SAL06");
		  attribute2dbField.put("chargeDescriptionDOC03","CHARGE_DESCRIPTION");
		  attribute2dbField.put("valueDOC03","VALUE");
		  attribute2dbField.put("percDOC03","PERC");
		  attribute2dbField.put("docTypeDOC03","DOC_TYPE");
		  attribute2dbField.put("docYearDOC03","DOC_YEAR");
		  attribute2dbField.put("docNumberDOC03","DOC_NUMBER");
		  attribute2dbField.put("vatCodeSal06DOC03","VAT_CODE_SAL06");
		  attribute2dbField.put("vatDescriptionDOC03","VAT_DESCRIPTION");
		  attribute2dbField.put("vatValueDOC03","VAT_VALUE");
		  attribute2dbField.put("vatDeductibleDOC03","VAT_DEDUCTIBLE");
		  attribute2dbField.put("invoicedValueDOC03","INVOICED_VALUE");
		  attribute2dbField.put("valueReg01DOC03","VALUE_REG01");
		  attribute2dbField.put("taxableIncomeDOC03","TAXABLE_INCOME");

		  Response res = null;
		  if (vo.getInvoicedValueDOC03()==null && vo.getValueDOC03()!=null)
			  vo.setInvoicedValueDOC03(new BigDecimal(0));
		  vo.setTaxableIncomeDOC03(vo.getValueDOC03());

		  // insert into DOC03...
		  res = QueryUtil.insertTable(
				  conn,
				  new UserSessionParameters(username),
				  vo,
				  "DOC03_SELLING_CHARGES",
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
				  "insertSaleDocCharge", "Error while inserting a new sale charge", ex);
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
  public VOResponse deleteSaleDocCharges(
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

      SaleDocChargeVO vo = null;

      pstmt = conn.prepareStatement(
          "delete from DOC03_SELLING_CHARGES where "+
          "DOC03_SELLING_CHARGES.COMPANY_CODE_SYS01=? and "+
          "DOC03_SELLING_CHARGES.DOC_TYPE=? and "+
          "DOC03_SELLING_CHARGES.DOC_YEAR=? and "+
          "DOC03_SELLING_CHARGES.DOC_NUMBER=? and "+
          "DOC03_SELLING_CHARGES.CHARGE_CODE_SAL06=?");

      for(int i=0;i<list.size();i++) {
        // phisically delete the record in DOC03...
        vo = (SaleDocChargeVO)list.get(i);
        pstmt.setString(1,vo.getCompanyCodeSys01DOC03());
        pstmt.setString(2,vo.getDocTypeDOC03());
        pstmt.setBigDecimal(3,vo.getDocYearDOC03());
        pstmt.setBigDecimal(4,vo.getDocNumberDOC03());
        pstmt.setString(5,vo.getChargeCodeSal06DOC03());
        pstmt.execute();
      }

      Response res = totals.updateTaxableIncomes(
        variant1Descriptions,
        variant2Descriptions,
        variant3Descriptions,
        variant4Descriptions,
        variant5Descriptions,
        new SaleDocPK(vo.getCompanyCodeSys01DOC03(),vo.getDocTypeDOC03(),vo.getDocYearDOC03(),vo.getDocNumberDOC03()),
        serverLanguageId,
        username
      );
      if (res.isError()) {
        throw new Exception(res.getErrorMessage());
      }

     return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing sale charges",ex);
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

