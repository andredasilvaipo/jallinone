package org.jallinone.sales.documents.itemdiscounts.server;

import org.openswing.swing.server.*;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.message.send.java.LookupValidationParams;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.registers.vat.java.VatVO;
import org.jallinone.system.server.*;
import org.jallinone.sales.discounts.java.DiscountVO;
import org.jallinone.sales.discounts.server.DiscountBean;
import org.jallinone.sales.documents.itemdiscounts.java.*;
import org.jallinone.sales.documents.server.*;
import org.jallinone.sales.documents.java.*;
import org.jallinone.sales.documents.java.*;
import org.jallinone.sales.documents.java.*;
import org.jallinone.sales.documents.server.*;
import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage item discounts for a sale document row.</p>
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
public class InsertSaleDocRowDiscountsBean implements InsertSaleDocRowDiscounts {


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

	  private InsertSaleDocRowDiscountBean bean;

	  public void setBean(InsertSaleDocRowDiscountBean bean) {
		  this.bean = bean;
	  }


	  /**
	   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
	   */
	  public SaleItemDiscountVO getSaleItemDiscount() {
		  throw new UnsupportedOperationException();
	  }






	 /**
	   * Business logic to execute.
	   */
	  public VOListResponse insertSaleDocRowDiscounts(
             HashMap variant1Descriptions,
             HashMap variant2Descriptions,
             HashMap variant3Descriptions,
             HashMap variant4Descriptions,
             HashMap variant5Descriptions,
             ArrayList list, String serverLanguageId, String username) throws Throwable {
	    Connection conn = null;
	    try {
	      if (this.conn==null) conn = getConn(); else conn = this.conn;
	      totals.setConn(conn); // use same transaction...
	      SaleItemDiscountVO vo = null;
	      Response res = null;

	      for(int i=0;i<list.size();i++) {
	        vo = (SaleItemDiscountVO)list.get(i);
	        res = bean.insertSaleDocRowDiscount(
	          vo,
	          serverLanguageId,
	          username

	        );
	        if (res.isError()) {
	          throw new Exception(res.getErrorMessage());
	        }
	        list.set(i,((VOResponse)res).getVo());
	      }

	      res = totals.updateTaxableIncomes(
               variant1Descriptions,
               variant2Descriptions,
               variant3Descriptions,
               variant4Descriptions,
               variant5Descriptions,
	        new SaleDocPK(vo.getCompanyCodeSys01DOC04(),vo.getDocTypeDOC04(),vo.getDocYearDOC04(),vo.getDocNumberDOC04()),
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
	                   "executeCommand", "Error while inserting new item discounts", ex);
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




}
