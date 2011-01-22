package org.jallinone.sales.documents.itemdiscounts.server;

import java.sql.Connection;

import javax.sql.DataSource;


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
public class InsertSaleDocRowDiscountBean implements InsertSaleDocRowDiscount {


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
	
	  
	  

	  

	  /**
	   * Insert a new item row discount.
	   * No commit or rollback is executed; no connection is created or released.
	   */
	  public VOResponse insertSaleDocRowDiscount(
		      SaleItemDiscountVO vo,String serverLanguageId,String username) throws Throwable {
		Connection conn = null;
	    try {
	      if (this.conn==null) conn = getConn(); else conn = this.conn; 

	      Map attribute2dbField = new HashMap();
	      attribute2dbField.put("companyCodeSys01DOC04","COMPANY_CODE_SYS01");
	      attribute2dbField.put("discountCodeSal03DOC04","DISCOUNT_CODE_SAL03");
	      attribute2dbField.put("minValueDOC04","MIN_VALUE");
	      attribute2dbField.put("maxValueDOC04","MAX_VALUE");
	      attribute2dbField.put("minPercDOC04","MIN_PERC");
	      attribute2dbField.put("maxPercDOC04","MAX_PERC");
	      attribute2dbField.put("startDateDOC04","START_DATE");
	      attribute2dbField.put("endDateDOC04","END_DATE");
	      attribute2dbField.put("discountDescriptionDOC04","DISCOUNT_DESCRIPTION");
	      attribute2dbField.put("valueDOC04","VALUE");
	      attribute2dbField.put("percDOC04","PERC");
	      attribute2dbField.put("docTypeDOC04","DOC_TYPE");
	      attribute2dbField.put("docYearDOC04","DOC_YEAR");
	      attribute2dbField.put("docNumberDOC04","DOC_NUMBER");
	      attribute2dbField.put("itemCodeItm01DOC04","ITEM_CODE_ITM01");
	      attribute2dbField.put("minQtyDOC04","MIN_QTY");
	      attribute2dbField.put("multipleQtyDOC04","MULTIPLE_QTY");

	      attribute2dbField.put("variantTypeItm06DOC04","VARIANT_TYPE_ITM06");
	      attribute2dbField.put("variantCodeItm11DOC04","VARIANT_CODE_ITM11");
	      attribute2dbField.put("variantTypeItm07DOC04","VARIANT_TYPE_ITM07");
	      attribute2dbField.put("variantCodeItm12DOC04","VARIANT_CODE_ITM12");
	      attribute2dbField.put("variantTypeItm08DOC04","VARIANT_TYPE_ITM08");
	      attribute2dbField.put("variantCodeItm13DOC04","VARIANT_CODE_ITM13");
	      attribute2dbField.put("variantTypeItm09DOC04","VARIANT_TYPE_ITM09");
	      attribute2dbField.put("variantCodeItm14DOC04","VARIANT_CODE_ITM14");
	      attribute2dbField.put("variantTypeItm10DOC04","VARIANT_TYPE_ITM10");
	      attribute2dbField.put("variantCodeItm15DOC04","VARIANT_CODE_ITM15");

	      Response res = null;
	      // insert into DOC04...
	      res = QueryUtil.insertTable(
	          conn,
	          new UserSessionParameters(username),
	          vo,
	          "DOC04_SELLING_ITEM_DISCOUNTS",
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
	                   "insertSaleDocRowDiscount", "Error while inserting a new item discount", ex);
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




}
