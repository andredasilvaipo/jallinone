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
import org.jallinone.registers.vat.server.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.registers.vat.java.*;
import org.jallinone.commons.java.*;
import org.jallinone.accounting.movements.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to calculate taxable incomes of a purchase document, grouped by vat code,
 * based on document item rows and header discounts/charges.
 * It does not update the database, it only return an updated v.o.</p>
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
public class PurchaseDocTaxableIncomesBean implements PurchaseDocTaxableIncomes {


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



  private VatsBean vatBean;

  public void setVatBean(VatsBean vatBean) {
    this.vatBean = vatBean;
  }

  private LoadPurchaseDocBean docAction;

  public void setDocAction(LoadPurchaseDocBean docAction) {
    this.docAction = docAction;
  }


  

  public PurchaseDocTaxableIncomesBean() {
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public TaxableIncomeVO getTaxableIncome() {
	  throw new UnsupportedOperationException();
  }
  
  
  /**
   * Calculate Purchase document totals, grouping rows by vat code.
   * @param conn connection used to calculate totals and rows subtotals
   * @param vo Purchase document header v.o.
   * @return totals re
   */
  public VOListResponse calcTaxableIncomes(DetailPurchaseDocVO vo,String serverLanguageId,String username) throws Throwable {
   PreparedStatement pstmt = null;
    
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      vatBean.setConn(conn); // use same transaction...
      docAction.setConn(conn); // use same transaction...


      Hashtable itemsTaxableIncomes = new Hashtable();
      TaxableIncomeVO tVO = null;

/*
        tVO = (TaxableIncomeVO)itemsTaxableIncomes.get(rset.getString(5));
        if (tVO==null) {
          tVO = new TaxableIncomeVO();
          tVO.setRowType(tVO.ITEM_ROW_TYPE);
          tVO.setVatCode(rset.getString(5));
          tVO.setVatDescription(rset.getString(6));
          tVO.setTaxableIncome(rset.getBigDecimal(2).setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP));
          tVO.setVatValue(vatValue.setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP));
        }
        else {
          tVO.setTaxableIncome(tVO.getTaxableIncome().add(rset.getBigDecimal(2).setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP)));
          tVO.setVatValue(tVO.getVatValue().add(vatValue.setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP)));
        }
        itemsTaxableIncomes.put(rset.getString(5),tVO);
*/


      // retrieve rows totals...
      BigDecimal totalRowsVat = new BigDecimal(0);
      BigDecimal totalRowsTaxableIncome = new BigDecimal(0);
      ArrayList rowsTaxableIncome = new ArrayList();
      ArrayList rowsVatValue = new ArrayList();
      pstmt = conn.prepareStatement(
        "select VAT_VALUE,TAXABLE_INCOME,VALUE_REG01,DEDUCTIBLE_REG01,VAT_CODE_ITM01,VAT_DESCRIPTION from DOC07_PURCHASE_ITEMS where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=?"
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

        tVO = (TaxableIncomeVO)itemsTaxableIncomes.get(rset.getString(5));
        if (tVO==null) {
          tVO = new TaxableIncomeVO();
          tVO.setRowType(tVO.ITEM_ROW_TYPE);
          tVO.setVatCode(rset.getString(5));
          tVO.setVatDescription(rset.getString(6));
          tVO.setTaxableIncome(rset.getBigDecimal(2).setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP));
          tVO.setVatValue(rset.getBigDecimal(1).setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP));
        }
        else {
          tVO.setTaxableIncome(tVO.getTaxableIncome().add(rset.getBigDecimal(2).setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP)));
          tVO.setVatValue(tVO.getVatValue().add(rset.getBigDecimal(1).setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP)));
        }
        itemsTaxableIncomes.put(rset.getString(5),tVO);

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
        Enumeration en = itemsTaxableIncomes.keys();
        while(en.hasMoreElements()) {
          tVO = (TaxableIncomeVO)itemsTaxableIncomes.get(en.nextElement());
          discTaxableIncome = tVO.getTaxableIncome().doubleValue()*vo.getDiscountPercDOC06().doubleValue()/100d;
          discVatValue = discTaxableIncome*tVO.getVatValue().doubleValue()/tVO.getTaxableIncome().doubleValue();
          tVO.setTaxableIncome(tVO.getTaxableIncome().subtract(new BigDecimal(discTaxableIncome).setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP)));
          tVO.setVatValue(tVO.getVatValue().subtract(new BigDecimal(discVatValue).setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP)));
        }

      }

      if (vo.getDiscountValueDOC06()!=null) {
        // reduce rows taxable income proportionally...
        Enumeration en = itemsTaxableIncomes.keys();
        while(en.hasMoreElements()) {
          tVO = (TaxableIncomeVO)itemsTaxableIncomes.get(en.nextElement());
          discTaxableIncome = tVO.getTaxableIncome().doubleValue()/totalRowsTaxableIncome.doubleValue()*vo.getDiscountValueDOC06().doubleValue();
          discVatValue = discTaxableIncome*tVO.getVatValue().doubleValue()/tVO.getTaxableIncome().doubleValue();
          tVO.setTaxableIncome(tVO.getTaxableIncome().subtract(new BigDecimal(discTaxableIncome).setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP)));
          tVO.setVatValue(tVO.getVatValue().subtract(new BigDecimal(discVatValue).setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP)));
        }

      }


      double chargeTaxableIncome = 0;
      double chargeVatValue = 0;
      double chargeTotalTaxableIncome = 0;
      double chargeTotalVatValue = 0;
      if (vo.getChargeValueDOC06()!=null) {
        // add charge, proportionally to the rows taxable incomes...
        Enumeration en = itemsTaxableIncomes.keys();
        while(en.hasMoreElements()) {
          tVO = (TaxableIncomeVO)itemsTaxableIncomes.get(en.nextElement());
          chargeTaxableIncome = tVO.getTaxableIncome().doubleValue()/totalRowsTaxableIncome.doubleValue()*vo.getChargeValueDOC06().doubleValue();
          chargeVatValue = chargeTaxableIncome*tVO.getVatValue().doubleValue()/tVO.getTaxableIncome().doubleValue();
          tVO.setTaxableIncome(tVO.getTaxableIncome().add(new BigDecimal(chargeTaxableIncome).setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP)));
          tVO.setVatValue(tVO.getVatValue().add(new BigDecimal(chargeVatValue).setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP)));
        }
      }

      if (vo.getChargePercDOC06()!=null && vo.getChargePercDOC06().intValue()>0) {
        Enumeration en = itemsTaxableIncomes.keys();
        while(en.hasMoreElements()) {
          tVO = (TaxableIncomeVO)itemsTaxableIncomes.get(en.nextElement());
          chargeTaxableIncome = tVO.getTaxableIncome().doubleValue()*vo.getChargePercDOC06().doubleValue()/100d;
          chargeVatValue = chargeTaxableIncome*tVO.getVatValue().doubleValue()/tVO.getTaxableIncome().doubleValue();
          tVO.setTaxableIncome(tVO.getTaxableIncome().add(new BigDecimal(chargeTaxableIncome).setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP)));
          tVO.setVatValue(tVO.getVatValue().add(new BigDecimal(chargeVatValue).setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP)));
        }
      }

      // return taxable income rows...
      ArrayList taxableIncomeRows = new ArrayList();
      Enumeration en = itemsTaxableIncomes.keys();
      while(en.hasMoreElements()) {
        taxableIncomeRows.add( itemsTaxableIncomes.get(en.nextElement()) );
      }

      return new VOListResponse(taxableIncomeRows,false,taxableIncomeRows.size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"calcTaxableIncomes","Error on calculating ourchase document taxable incomes",ex);
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
        vatBean.setConn(null);
        docAction.setConn(null);
      } catch (Exception ex) {}
    }
  }


}

