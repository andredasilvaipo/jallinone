package org.jallinone.sales.documents.server;

import javax.sql.DataSource;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.sales.documents.java.*;
import org.jallinone.system.server.*;
import java.math.*;
import org.jallinone.registers.vat.server.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.registers.vat.java.*;
import org.jallinone.commons.java.*;
import org.jallinone.accounting.movements.java.*;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to calculate taxable incomes of a sale document, grouped by vat code,
 * based on document rows (items, item discounts, activities) and header discounts/charges.
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
public class SaleDocTaxableIncomesBean implements SaleDocTaxableIncomes {


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

  private LoadSaleDocBean docAction;
  
  public void setDocAction(LoadSaleDocBean docAction) {
	  this.docAction = docAction;
  }


  public SaleDocTaxableIncomesBean() {
  }

  
  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type 
   */
  public TaxableIncomeVO getTaxableIncome(){
	  throw new UnsupportedOperationException();
  }
  

  /**
   * Calculate sale document totals, grouping rows by vat code.
   * @param conn connection used to calculate totals and rows subtotals
   * @param vo sale document header v.o.
   * @return totals re
   */
  public final VOListResponse calcTaxableIncomes(DetailSaleDocVO vo,String serverLanguageId,String username) throws Throwable{
   PreparedStatement pstmt = null;
    
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      vatBean.setConn(conn); // use same transaction...
      docAction.setConn(conn);

      Hashtable itemsTaxableIncomes = new Hashtable();
      Hashtable actTaxableIncomes = new Hashtable();
      Hashtable chargesTaxableIncomes = new Hashtable();

      // check if there exists a vat code defined at customer level...
      BigDecimal customerVatValue = null;
      if (vo.getCustomerVatCodeReg01DOC01()!=null && !vo.getCustomerVatCodeReg01DOC01().equals("")) {
        // retrieve vat value and deductible percentage...
        Response res = vatBean.validateVatCode(
            new LookupValidationParams(vo.getCustomerVatCodeReg01DOC01(),new HashMap()),
            serverLanguageId,
            username,
            new ArrayList()
        );
        if (!res.isError()) {
          VatVO vatVO = (VatVO)((VOListResponse)res).getRows().get(0);
          customerVatValue = new BigDecimal(vatVO.getValueREG01().doubleValue()*(1d-vatVO.getDeductibleREG01().doubleValue()/100d));
        }
      }


      // retrieve item rows totals...
      BigDecimal totalRowsVat = new BigDecimal(0);
      BigDecimal totalRowsTaxableIncome = new BigDecimal(0);
      ArrayList rowsTaxableIncome = new ArrayList();
      ArrayList rowsVatValue = new ArrayList();
      pstmt = conn.prepareStatement(
        "select VAT_VALUE,TAXABLE_INCOME,VALUE_REG01,DEDUCTIBLE_REG01,VAT_CODE_ITM01,VAT_DESCRIPTION from DOC02_SELLING_ITEMS where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=?"
      );
      pstmt.setString(1,vo.getCompanyCodeSys01DOC01());
      pstmt.setString(2,vo.getDocTypeDOC01());
      pstmt.setBigDecimal(3,vo.getDocYearDOC01());
      pstmt.setBigDecimal(4,vo.getDocNumberDOC01());
      ResultSet rset = pstmt.executeQuery();
      TaxableIncomeVO tVO = null;
      BigDecimal vatValue = null;
      while(rset.next()) {
        if (customerVatValue==null) {
          rowsVatValue.add(new BigDecimal(rset.getDouble(3)*(1d-rset.getDouble(4)/100d))); // % vat
          vatValue = rset.getBigDecimal(1);
        }
        else {
          rowsVatValue.add(customerVatValue); // % vat
          vatValue = rset.getBigDecimal(1).multiply(customerVatValue).divide(new BigDecimal(100),BigDecimal.ROUND_HALF_UP);
        }
        totalRowsVat = totalRowsVat.add(vatValue);
        totalRowsTaxableIncome = totalRowsTaxableIncome.add(rset.getBigDecimal(2));
        rowsTaxableIncome.add(rset.getBigDecimal(2));

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
      }
      rset.close();


      // retrieve sale activities rows totals...
      pstmt.close();
      pstmt = conn.prepareStatement(
        "select VALUE,VAT_VALUE,VAT_DEDUCTIBLE,VAT_CODE_SAL09,VAT_DESCRIPTION from DOC13_SELLING_ACTIVITIES where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=?"
      );
      pstmt.setString(1,vo.getCompanyCodeSys01DOC01());
      pstmt.setString(2,vo.getDocTypeDOC01());
      pstmt.setBigDecimal(3,vo.getDocYearDOC01());
      pstmt.setBigDecimal(4,vo.getDocNumberDOC01());
      rset = pstmt.executeQuery();
      while(rset.next()) {
        if (customerVatValue==null) {
          rowsVatValue.add(new BigDecimal(rset.getDouble(2)*(1d - rset.getDouble(3) / 100d))); // % vat
          vatValue =
              rset.getBigDecimal(1).
              multiply( (BigDecimal) rowsVatValue.get(rowsVatValue.size() - 1)).
              divide(new BigDecimal(100), BigDecimal.ROUND_HALF_UP);
        }
        else {
          rowsVatValue.add(customerVatValue); // % vat
          vatValue =
              rset.getBigDecimal(1).
              multiply( customerVatValue ).
              divide(new BigDecimal(100), BigDecimal.ROUND_HALF_UP);

        }
        totalRowsVat = totalRowsVat.add(vatValue);

        totalRowsTaxableIncome = totalRowsTaxableIncome.add(rset.getBigDecimal(1));
        rowsTaxableIncome.add(rset.getBigDecimal(1));


        tVO = (TaxableIncomeVO)actTaxableIncomes.get(rset.getString(4));
        if (tVO==null) {
          tVO = new TaxableIncomeVO();
          tVO.setRowType(tVO.ACTIVITY_ROW_TYPE);
          tVO.setVatCode(rset.getString(4));
          tVO.setVatDescription(rset.getString(5));
          tVO.setTaxableIncome(rset.getBigDecimal(1).setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP));
          tVO.setVatValue(vatValue.setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP));
        }
        else {
          tVO.setTaxableIncome(tVO.getTaxableIncome().add(rset.getBigDecimal(1).setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP)));
          tVO.setVatValue(tVO.getVatValue().add(vatValue.setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP)));
        }
        actTaxableIncomes.put(rset.getString(4),tVO);
      }
      rset.close();

      BigDecimal totalRowsTaxableIncomeUsedForDiscCharges = totalRowsTaxableIncome;


      // before applying header discounts and charges there will be check if this is an invoice and
      // there exists a linked sale document: if this is the case then
      // total rows vat and total rows taxtable income will be calculated from the linked sale doc...
      if ((vo.getDocTypeDOC01().equals(ApplicationConsts.SALE_INVOICE_FROM_SD_DOC_TYPE) ||
           vo.getDocTypeDOC01().equals(ApplicationConsts.SALE_INVOICE_FROM_DN_DOC_TYPE)) &&
          vo.getDocNumberDoc01DOC01()!=null
      ) {
        SaleDocPK pk = new SaleDocPK(
          vo.getCompanyCodeSys01Doc01DOC01(),
          vo.getDocTypeDoc01DOC01(),
          vo.getDocYearDoc01DOC01(),
          vo.getDocNumberDoc01DOC01()
        );
        DetailSaleDocVO saleDocVO = docAction.loadSaleDoc(pk,serverLanguageId,username,new ArrayList());
        totalRowsTaxableIncomeUsedForDiscCharges = saleDocVO.getTaxableIncomeDOC01();
      }


      BigDecimal totalDocTaxableIncome = totalRowsTaxableIncome;
      BigDecimal totalDocVat = totalRowsVat;

      // substract header discounts...
      double discTaxableIncome = 0;
      double discVatValue = 0;

      // apply discount (eventually) defined at row level...
      if (vo.getDiscountValueDOC01()!=null) {
        Enumeration en = itemsTaxableIncomes.keys();
        String vatCode = null;
        while(en.hasMoreElements()) {
          vatCode = en.nextElement().toString();
          tVO = (TaxableIncomeVO)itemsTaxableIncomes.get(vatCode);
          discTaxableIncome = tVO.getTaxableIncome().doubleValue()/totalRowsTaxableIncomeUsedForDiscCharges.doubleValue()*vo.getDiscountValueDOC01().doubleValue();
          discVatValue = discTaxableIncome*tVO.getVatValue().doubleValue()/tVO.getTaxableIncome().doubleValue();
          tVO.setTaxableIncome(tVO.getTaxableIncome().subtract(new BigDecimal(discTaxableIncome).setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP)));
          tVO.setVatValue(tVO.getVatValue().subtract(new BigDecimal(discVatValue).setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP)));
        }
      }
      else if (vo.getDiscountPercDOC01()!=null) {
        Enumeration en = itemsTaxableIncomes.keys();
        String vatCode = null;
        while(en.hasMoreElements()) {
          vatCode = en.nextElement().toString();
          tVO = (TaxableIncomeVO)itemsTaxableIncomes.get(vatCode);
          discTaxableIncome = tVO.getTaxableIncome().doubleValue()*vo.getDiscountPercDOC01().doubleValue()/100d;
          discVatValue = discTaxableIncome*tVO.getVatValue().doubleValue()/tVO.getTaxableIncome().doubleValue();
          tVO.setTaxableIncome(tVO.getTaxableIncome().subtract(new BigDecimal(discTaxableIncome).setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP)));
          tVO.setVatValue(tVO.getVatValue().subtract(new BigDecimal(discVatValue).setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP)));
        }
      }

      // substract header discounts (defined at item row level)...
      pstmt.close();
      pstmt = conn.prepareStatement(
        "select VALUE,PERC from DOC05_SELLING_DISCOUNTS "+
        "where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? and "+
        " START_DATE<=? and END_DATE>=? order by DISCOUNT_CODE_SAL03"
      );
      pstmt.setString(1,vo.getCompanyCodeSys01DOC01());
      pstmt.setString(2,vo.getDocTypeDOC01());
      pstmt.setBigDecimal(3,vo.getDocYearDOC01());
      pstmt.setBigDecimal(4,vo.getDocNumberDOC01());
      pstmt.setDate(5,new java.sql.Date(System.currentTimeMillis()));
      pstmt.setDate(6,new java.sql.Date(System.currentTimeMillis()));
      rset = pstmt.executeQuery();
      BigDecimal perc = null;
      BigDecimal value = null;
      while(rset.next()) {
        value = rset.getBigDecimal(1);
        perc = rset.getBigDecimal(2);
        if (perc!=null) {
          Enumeration en = itemsTaxableIncomes.keys();
          String vatCode = null;
          while(en.hasMoreElements()) {
            vatCode = en.nextElement().toString();
            tVO = (TaxableIncomeVO)itemsTaxableIncomes.get(vatCode);
            discTaxableIncome = tVO.getTaxableIncome().doubleValue()*perc.doubleValue()/100d;
            discVatValue = discTaxableIncome*tVO.getVatValue().doubleValue()/tVO.getTaxableIncome().doubleValue();
            tVO.setTaxableIncome(tVO.getTaxableIncome().subtract(new BigDecimal(discTaxableIncome).setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP)));
            tVO.setVatValue(tVO.getVatValue().subtract(new BigDecimal(discVatValue).setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP)));
          }
          totalRowsVat = new BigDecimal(totalRowsVat.doubleValue()-totalRowsVat.doubleValue()*perc.doubleValue()/100d);
          totalRowsTaxableIncome = new BigDecimal(totalRowsTaxableIncome.doubleValue()-totalRowsTaxableIncome.doubleValue()*perc.doubleValue()/100d);
        }
        else
        if (value!=null) {
          // reduce rows taxable income proportionally...
          Enumeration en = itemsTaxableIncomes.keys();
          String vatCode = null;
          while(en.hasMoreElements()) {
            vatCode = en.nextElement().toString();
            tVO = (TaxableIncomeVO)itemsTaxableIncomes.get(vatCode);
            discTaxableIncome = tVO.getTaxableIncome().doubleValue()/totalRowsTaxableIncome.doubleValue()*value.doubleValue();
            discVatValue = discTaxableIncome*tVO.getVatValue().doubleValue()/tVO.getTaxableIncome().doubleValue();
            tVO.setTaxableIncome(tVO.getTaxableIncome().subtract(new BigDecimal(discTaxableIncome).setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP)));
            tVO.setVatValue(tVO.getVatValue().subtract(new BigDecimal(discVatValue).setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP)));
          }
        }
      }
      rset.close();


      // calculate charges...
      pstmt.close();
      pstmt = conn.prepareStatement(
        "select DOC03_SELLING_CHARGES.VALUE,DOC03_SELLING_CHARGES.PERC,DOC03_SELLING_CHARGES.VAT_VALUE,DOC03_SELLING_CHARGES.VAT_DEDUCTIBLE,DOC03_SELLING_CHARGES.VAT_CODE_SAL06,DOC03_SELLING_CHARGES.VAT_DESCRIPTION "+
        "from DOC03_SELLING_CHARGES where "+
        "DOC03_SELLING_CHARGES.COMPANY_CODE_SYS01=? and DOC03_SELLING_CHARGES.DOC_TYPE=? and "+
        "DOC03_SELLING_CHARGES.DOC_YEAR=? and DOC03_SELLING_CHARGES.DOC_NUMBER=? "+
        "order by DOC03_SELLING_CHARGES.CHARGE_CODE_SAL06"
      );
      pstmt.setString(1,vo.getCompanyCodeSys01DOC01());
      pstmt.setString(2,vo.getDocTypeDOC01());
      pstmt.setBigDecimal(3,vo.getDocYearDOC01());
      pstmt.setBigDecimal(4,vo.getDocNumberDOC01());
      rset = pstmt.executeQuery();
      BigDecimal vatDeductible = null;
      double vat = 0; // % vat
      double chargeTaxableIncome;
      double chargeVatValue;
      while(rset.next()) {
        value = rset.getBigDecimal(1);
        perc = rset.getBigDecimal(2);
        vatValue = rset.getBigDecimal(3);
        vatDeductible = rset.getBigDecimal(4);
        if (vatValue!=null && vatDeductible!=null)
          vat = vatValue.doubleValue()*(100d-vatDeductible.doubleValue())/100d;
        if (customerVatValue!=null)
          vat = customerVatValue.doubleValue();

        // if the charge is a percentage then this percentage is applied to each item row (vat value is not defined and is not used)
        // otherwise (charge is a value) then it's added to total taxable income and its vat value is added to total vat
        if (perc!=null) {
          Enumeration en = itemsTaxableIncomes.keys();
          String vatCode = null;
          while(en.hasMoreElements()) {
            vatCode = en.nextElement().toString();
            tVO = (TaxableIncomeVO)itemsTaxableIncomes.get(vatCode);
            chargeTaxableIncome = tVO.getTaxableIncome().doubleValue()*perc.doubleValue()/100d;
            chargeVatValue = discTaxableIncome*tVO.getVatValue().doubleValue()/tVO.getTaxableIncome().doubleValue();
            tVO.setTaxableIncome(tVO.getTaxableIncome().add(new BigDecimal(chargeTaxableIncome).setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP)));
            tVO.setVatValue(tVO.getVatValue().add(new BigDecimal(chargeVatValue).setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP)));
          }
        }
        else if (value!=null) {
          vatValue = value.multiply(new BigDecimal(vat)).divide(new BigDecimal(100),BigDecimal.ROUND_HALF_UP);
          tVO = (TaxableIncomeVO)chargesTaxableIncomes.get(rset.getString(5));
          if (tVO==null) {
            tVO = new TaxableIncomeVO();
            tVO.setRowType(tVO.CHARGE_ROW_TYPE);
            tVO.setVatCode(rset.getString(5));
            tVO.setVatDescription(rset.getString(6));
            tVO.setTaxableIncome(value.setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP));
            tVO.setVatValue(vatValue.setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP));
          }
          else {
            tVO.setTaxableIncome(tVO.getTaxableIncome().add(value.setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP)));
            tVO.setVatValue(tVO.getVatValue().add(vatValue.setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP)));
          }
          chargesTaxableIncomes.put(rset.getString(5),tVO);
        }

      }
      rset.close();

      // return taxable income rows...
      ArrayList taxableIncomeRows = new ArrayList();
      Enumeration en = itemsTaxableIncomes.keys();
      while(en.hasMoreElements()) {
        taxableIncomeRows.add( itemsTaxableIncomes.get(en.nextElement()) );
      }
      en = actTaxableIncomes.keys();
      while(en.hasMoreElements()) {
        taxableIncomeRows.add( actTaxableIncomes.get(en.nextElement()) );
      }
      en = chargesTaxableIncomes.keys();
      while(en.hasMoreElements()) {
        taxableIncomeRows.add( chargesTaxableIncomes.get(en.nextElement()) );
      }

      return new VOListResponse(taxableIncomeRows,false,taxableIncomeRows.size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"calcTaxableIncomes","Error on calculating sale document taxble incomes",ex);
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

