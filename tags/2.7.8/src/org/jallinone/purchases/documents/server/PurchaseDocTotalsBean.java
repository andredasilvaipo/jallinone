package org.jallinone.purchases.documents.server;

import javax.sql.DataSource;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.purchases.documents.java.*;
import org.jallinone.system.server.*;
import java.math.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Helper class used to calculate purchase document totals (vat, taxable income, doc. total),
 * based on document rows and header discounts/charges: it does not update the database, it only return an updated v.o.</p>
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
public class PurchaseDocTotalsBean implements PurchaseDocTotals {


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




  public PurchaseDocTotalsBean() {
  }


   /**
    * This method does not create or release a connection; it does not commit or rollback.
    */
  public VOResponse calcDocTotals(DetailPurchaseDocVO vo,String serverLanguageId,String username)  throws Throwable{
   PreparedStatement pstmt = null;
    
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      // retrieve rows totals...
      BigDecimal totalRowsVat = new BigDecimal(0);
      BigDecimal totalRowsTaxableIncome = new BigDecimal(0);
      ArrayList rowsTaxableIncome = new ArrayList();
      ArrayList rowsVatValue = new ArrayList();
      pstmt = conn.prepareStatement(
        "select VAT_VALUE,TAXABLE_INCOME,VALUE_REG01,DEDUCTIBLE_REG01 from DOC07_PURCHASE_ITEMS where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=?"
      );
      pstmt.setString(1,vo.getCompanyCodeSys01DOC06());
      pstmt.setString(2,vo.getDocTypeDOC06());
      pstmt.setBigDecimal(3,vo.getDocYearDOC06());
      pstmt.setBigDecimal(4,vo.getDocNumberDOC06());
      ResultSet rset = pstmt.executeQuery();
      while(rset.next()) {
        totalRowsVat = totalRowsVat.add(rset.getBigDecimal(1));
        totalRowsTaxableIncome = totalRowsTaxableIncome.add(rset.getBigDecimal(2));
        rowsTaxableIncome.add(rset.getBigDecimal(2));
        rowsVatValue.add(new BigDecimal(rset.getDouble(3)*(1d-rset.getDouble(4)/100d))); // % vat
      }
      rset.close();

      BigDecimal totalDocVat = totalRowsVat;
      BigDecimal totalDocTaxableIncome = totalRowsTaxableIncome;

      // substract header discounts...
      double discTaxableIncome = 0;
      double discVatValue = 0;
      double discTotalTaxableIncome = 0;
      double discTotalVatValue = 0;
      if (vo.getDiscountPercDOC06()!=null) {
        for(int i=0;i<rowsTaxableIncome.size();i++) {
          discTaxableIncome = ((BigDecimal)rowsTaxableIncome.get(i)).doubleValue()*vo.getDiscountPercDOC06().doubleValue()/100d;
          discVatValue = discTaxableIncome*((BigDecimal)rowsVatValue.get(i)).doubleValue()/100d;
          discTotalTaxableIncome += discTaxableIncome;
          discTotalVatValue += discVatValue;
          rowsTaxableIncome.set(i,new BigDecimal(((BigDecimal)rowsTaxableIncome.get(i)).doubleValue()-discTaxableIncome));
        }
        totalRowsVat = new BigDecimal(totalRowsVat.doubleValue()-totalRowsVat.doubleValue()*vo.getDiscountPercDOC06().doubleValue()/100d);
        totalRowsTaxableIncome = new BigDecimal(totalRowsTaxableIncome.doubleValue()-totalRowsTaxableIncome.doubleValue()*vo.getDiscountPercDOC06().doubleValue()/100d);
      }

      if (vo.getDiscountValueDOC06()!=null) {
        // reduce rows taxable income proportionally...
        for(int i=0;i<rowsTaxableIncome.size();i++) {
          discTaxableIncome = ((BigDecimal)rowsTaxableIncome.get(i)).doubleValue()/totalRowsTaxableIncome.doubleValue()*vo.getDiscountValueDOC06().doubleValue();
          discVatValue = discTaxableIncome*((BigDecimal)rowsVatValue.get(i)).doubleValue()/100d;
          discTotalTaxableIncome += discTaxableIncome;
          discTotalVatValue += discVatValue;
          rowsTaxableIncome.set(i,new BigDecimal(((BigDecimal)rowsTaxableIncome.get(i)).doubleValue()-discTaxableIncome));
        }
        totalRowsVat = new BigDecimal(totalRowsVat.doubleValue()-discTotalVatValue);
        totalRowsTaxableIncome = new BigDecimal(totalRowsTaxableIncome.doubleValue()-discTotalTaxableIncome);
      }

      totalDocVat = new BigDecimal(totalDocVat.doubleValue()-discTotalVatValue);
      totalDocTaxableIncome = new BigDecimal(totalDocTaxableIncome.doubleValue()-discTotalTaxableIncome);


      double chargeTaxableIncome = 0;
      double chargeVatValue = 0;
      double chargeTotalTaxableIncome = 0;
      double chargeTotalVatValue = 0;
      if (vo.getChargeValueDOC06()!=null)
        // add charge, proportionally to the rows taxable incomes...
        for(int i=0;i<rowsTaxableIncome.size();i++) {
          chargeTaxableIncome = ((BigDecimal)rowsTaxableIncome.get(i)).doubleValue()/totalRowsTaxableIncome.doubleValue()*vo.getChargeValueDOC06().doubleValue();
          chargeVatValue = chargeTaxableIncome*((BigDecimal)rowsVatValue.get(i)).doubleValue()/100d;
          chargeTotalTaxableIncome += chargeTaxableIncome;
          chargeTotalVatValue += chargeVatValue;
        }

      if (vo.getChargePercDOC06()!=null && vo.getChargePercDOC06().intValue()>0) {
        // add charge to the total taxable income and total vat...
        chargeTotalTaxableIncome = totalDocTaxableIncome.doubleValue()*vo.getChargePercDOC06().doubleValue()/100d;
        chargeTotalVatValue = totalDocVat.doubleValue()*vo.getChargePercDOC06().doubleValue()/100d;
      }

      // add charges to the doc taxable income+vat totals...
      totalDocVat = totalDocVat.add(new BigDecimal(chargeTotalVatValue));
      totalDocTaxableIncome = totalDocTaxableIncome.add(new BigDecimal(chargeTotalTaxableIncome));

      // update totals in the value object...
      vo.setTaxableIncomeDOC06(totalDocTaxableIncome.setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP));
      vo.setTotalVatDOC06(totalDocVat.setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP));
      vo.setTotalDOC06( vo.getTaxableIncomeDOC06().add(vo.getTotalVatDOC06()).setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP) );

      return new VOResponse(vo);
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"calcDocTotals","Error on calculating purchase order totals",ex);
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

