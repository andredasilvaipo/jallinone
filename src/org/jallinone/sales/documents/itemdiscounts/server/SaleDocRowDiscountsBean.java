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
public class SaleDocRowDiscountsBean  implements SaleDocRowDiscounts {


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
  private SaleItemTotalDiscountBean itemDiscountBean;

  private InsertSaleDocRowDiscountsBean bean;

  public void setBean(InsertSaleDocRowDiscountsBean bean) {
	  this.bean = bean;
  }

	private LoadSaleDocRowBean loadSaleDocRowBean;

	public void setLoadSaleDocRowBean(LoadSaleDocRowBean loadSaleDocRowBean) {
		this.loadSaleDocRowBean = loadSaleDocRowBean;
	}



  public void setTotalBean(SaleDocTotalsBean totalBean) {
	  this.totalBean = totalBean;
  }

  public void setDocBean(LoadSaleDocBean docBean) {
	  this.docBean = docBean;
  }


   public void setItemDiscountBean(SaleItemTotalDiscountBean itemDiscountBean) {
	   this.itemDiscountBean = itemDiscountBean;
   }


  public SaleDocRowDiscountsBean() {
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
   */
  public DetailSaleDocRowVO getDetailSaleDocRow() {
	  throw new UnsupportedOperationException();
  }

  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
   */
  public SaleItemDiscountVO getSaleItemDiscount() {
	  throw new UnsupportedOperationException();
  }

  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
   */
  public DiscountVO getDiscount() {
	  throw new UnsupportedOperationException();
  }




  /**
   * Recalculate item row totals and document totals.
   * @return Boolean.TRUE if totals are correctly calculated
   */
  public Response updateTotals(
      HashMap variant1Descriptions,
      HashMap variant2Descriptions,
      HashMap variant3Descriptions,
      HashMap variant4Descriptions,
      HashMap variant5Descriptions,
      SaleDocRowPK pk, String serverLanguageId, String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      totalBean.setConn(conn); // use same transaction...
      docBean.setConn(conn); // use same transaction...
      loadSaleDocRowBean.setConn(conn); // use same transaction...
      itemDiscountBean.setConn(conn);

       // retrieve document header value object...
      SaleDocPK docPK = new SaleDocPK(
          pk.getCompanyCodeSys01DOC02(),
          pk.getDocTypeDOC02(),
          pk.getDocYearDOC02(),
          pk.getDocNumberDOC02()
      );
      DetailSaleDocVO vo = docBean.loadSaleDoc(docPK,serverLanguageId,username,new ArrayList());

      // recalculate item row totals...
      Response rowResponse = loadSaleDocRowBean.loadSaleDocRow(
          variant1Descriptions,
          variant2Descriptions,
          variant3Descriptions,
          variant4Descriptions,
          variant5Descriptions,
          pk, serverLanguageId, username
      );
      if (rowResponse.isError()) {
        conn.rollback();
        throw new Exception(rowResponse.getErrorMessage());
      }
      DetailSaleDocRowVO rowVO = (DetailSaleDocRowVO)((VOResponse)rowResponse).getVo();
      Response itemTotDiscResponse = itemDiscountBean.getSaleItemTotalDiscount(rowVO,serverLanguageId,username);
      if (itemTotDiscResponse.isError())
    	  throw new Exception(itemTotDiscResponse.getErrorMessage());
      rowVO = (DetailSaleDocRowVO)((VOResponse)itemTotDiscResponse).getVo();

      rowVO.setTotalDiscountDOC02( rowVO.getTotalDiscountDOC02().setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP) );

      // apply total discount to taxable income...
      rowVO.setTaxableIncomeDOC02(rowVO.getQtyDOC02().multiply(rowVO.getValueSal02DOC02()).setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP));
      rowVO.setTaxableIncomeDOC02(rowVO.getTaxableIncomeDOC02().subtract(rowVO.getTotalDiscountDOC02()).setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP));

      // calculate row vat...
      double vatPerc = rowVO.getValueReg01DOC02().doubleValue()*(1d-rowVO.getDeductibleReg01DOC02().doubleValue()/100d)/100;
      rowVO.setVatValueDOC02(rowVO.getTaxableIncomeDOC02().multiply(new BigDecimal(vatPerc)).setScale(vo.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP));

      // calculate row total...
      rowVO.setValueDOC02(rowVO.getTaxableIncomeDOC02().add(rowVO.getVatValueDOC02()));

      pstmt = conn.prepareStatement("update DOC02_SELLING_ITEMS set TAXABLE_INCOME=?,VAT_VALUE=?,VALUE=?,TOTAL_DISCOUNT=? where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? and ITEM_CODE_ITM01=?");
      pstmt.setBigDecimal(1,rowVO.getTaxableIncomeDOC02());
      pstmt.setBigDecimal(2,rowVO.getVatValueDOC02());
      pstmt.setBigDecimal(3,rowVO.getValueDOC02());
      pstmt.setBigDecimal(4,rowVO.getTotalDiscountDOC02());
      pstmt.setString(5,pk.getCompanyCodeSys01DOC02());
      pstmt.setString(6,pk.getDocTypeDOC02());
      pstmt.setBigDecimal(7,pk.getDocYearDOC02());
      pstmt.setBigDecimal(8,pk.getDocNumberDOC02());
      pstmt.setString(9,pk.getItemCodeItm01DOC02());
      pstmt.execute();
      conn.commit();

      // recalculate document totals...
      Response totalResponse = totalBean.getSaleDocTotals(vo,serverLanguageId,username);
      if (totalResponse.isError())
    	  throw new Exception(totalResponse.getErrorMessage());

      pstmt = conn.prepareStatement("update DOC01_SELLING set TAXABLE_INCOME=?,TOTAL_VAT=?,TOTAL=? where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=?");
      pstmt.setBigDecimal(1,vo.getTaxableIncomeDOC01());
      pstmt.setBigDecimal(2,vo.getTotalVatDOC01());
      pstmt.setBigDecimal(3,vo.getTotalDOC01());
      pstmt.setString(4,pk.getCompanyCodeSys01DOC02());
      pstmt.setString(5,pk.getDocTypeDOC02());
      pstmt.setBigDecimal(6,pk.getDocYearDOC02());
      pstmt.setBigDecimal(7,pk.getDocNumberDOC02());
      pstmt.execute();

      conn.commit();
      return new VOResponse(Boolean.TRUE);
    }
    catch (Throwable ex) {
      Logger.error(username,"org.jallinone.sales.documents.itemdiscounts.server.UpdateSaleItemTotalDiscountBean","updateTotals","Error while updating document and row totals:\n"+ex.getMessage(), ex);
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
          loadSaleDocRowBean.setConn(null);
          itemDiscountBean.setConn(null);
        } catch (Exception ex) {}
     }

  }




  /**
   * Business logic to execute.
   */
  public VOListResponse validateSaleItemDiscountCode(LookupValidationParams validationPars,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      discountBean.setConn(conn);
      DetailSaleDocRowVO rowVO = (DetailSaleDocRowVO)validationPars.getLookupValidationParameters().get(ApplicationConsts.SALE_DOC_ROW_VO);

      // retrieve item discount codes...
      stmt = conn.createStatement();
      ResultSet rset = stmt.executeQuery(
          "select DISCOUNT_CODE_SAL03 from SAL04_ITEM_DISCOUNTS,SAL03_DISCOUNTS where "+
          "SAL04_ITEM_DISCOUNTS.COMPANY_CODE_SYS01='"+rowVO.getCompanyCodeSys01DOC02()+"' and ITEM_CODE_ITM01='"+rowVO.getItemCodeItm01DOC02()+"' and DISCOUNT_CODE_SAL03='"+validationPars.getCode()+"' and "+
          "SAL04_ITEM_DISCOUNTS.COMPANY_CODE_SYS01=SAL03_DISCOUNTS.COMPANY_CODE_SYS01 and "+
          "SAL04_ITEM_DISCOUNTS.DISCOUNT_CODE_SAL03=SAL03_DISCOUNTS.DISCOUNT_CODE and "+
          "SAL03_DISCOUNTS.CURRENCY_CODE_REG03='"+rowVO.getCurrencyCodeReg03DOC01()+"' and "+
          "SAL03_DISCOUNTS.MIN_QTY<="+rowVO.getQtyDOC02()
      );
      ArrayList discountCodes = new ArrayList();
      while(rset.next()) {
        discountCodes.add( rset.getString(1) );
      }
      rset.close();

      // retrieve item hierarchy discount codes...
      rset = stmt.executeQuery(
          "select DISCOUNT_CODE_SAL03 from SAL05_ITEM_HIERAR_DISCOUNTS,SAL03_DISCOUNTS where "+
          "SAL05_ITEM_HIERAR_DISCOUNTS.COMPANY_CODE_SYS01='"+rowVO.getCompanyCodeSys01DOC02()+"' and PROGRESSIVE_HIE01="+rowVO.getProgressiveHie01ITM01()+" and DISCOUNT_CODE_SAL03='"+validationPars.getCode()+"' and "+
          "SAL05_ITEM_HIERAR_DISCOUNTS.COMPANY_CODE_SYS01=SAL03_DISCOUNTS.COMPANY_CODE_SYS01 and "+
          "SAL05_ITEM_HIERAR_DISCOUNTS.DISCOUNT_CODE_SAL03=SAL03_DISCOUNTS.DISCOUNT_CODE and "+
          "SAL03_DISCOUNTS.CURRENCY_CODE_REG03='"+rowVO.getCurrencyCodeReg03DOC01()+"' and "+
          "SAL03_DISCOUNTS.MIN_QTY<="+rowVO.getQtyDOC02()
      );
      while(rset.next()) {
        discountCodes.add( rset.getString(1) );
      }
      rset.close();

      Response res = discountBean.getDiscountsList(
          rowVO.getCompanyCodeSys01DOC02(),
          discountCodes,
          serverLanguageId,
          new GridParams(),
          username,
          DiscountVO.class
      );

      if (!res.isError()) {
        java.util.List list = ((VOListResponse)res).getRows();
        DiscountVO vo = null;
        int i=0;
        while(i<list.size()) {
          vo = (DiscountVO)list.get(i);
          if (vo.getStartDateSAL03().getTime()>System.currentTimeMillis() ||
              vo.getEndDateSAL03().getTime()<System.currentTimeMillis())
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
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while validating an item discount code",ex);
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
    		discountBean.setConn(null);
    	} catch (Exception ex) {}
    }

  }





  /**
   * Business logic to execute.
   */
  public VOListResponse loadSaleDocRowDiscounts(GridParams gridParams,String serverLanguageId,String username) throws Throwable {
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;

      String sql =
	      "select "+
	      "DOC04_SELLING_ITEM_DISCOUNTS.VARIANT_TYPE_ITM06,"+
	      "DOC04_SELLING_ITEM_DISCOUNTS.VARIANT_CODE_ITM11,"+
	      "DOC04_SELLING_ITEM_DISCOUNTS.VARIANT_TYPE_ITM07,"+
	      "DOC04_SELLING_ITEM_DISCOUNTS.VARIANT_CODE_ITM12,"+
	      "DOC04_SELLING_ITEM_DISCOUNTS.VARIANT_TYPE_ITM08,"+
	      "DOC04_SELLING_ITEM_DISCOUNTS.VARIANT_CODE_ITM13,"+
	      "DOC04_SELLING_ITEM_DISCOUNTS.VARIANT_TYPE_ITM09,"+
	      "DOC04_SELLING_ITEM_DISCOUNTS.VARIANT_CODE_ITM14,"+
	      "DOC04_SELLING_ITEM_DISCOUNTS.VARIANT_TYPE_ITM10,"+
	      "DOC04_SELLING_ITEM_DISCOUNTS.VARIANT_CODE_ITM15,"+
          "DOC04_SELLING_ITEM_DISCOUNTS.COMPANY_CODE_SYS01,DOC04_SELLING_ITEM_DISCOUNTS.DISCOUNT_CODE_SAL03,"+
          "DOC04_SELLING_ITEM_DISCOUNTS.MIN_VALUE,DOC04_SELLING_ITEM_DISCOUNTS.MAX_VALUE,DOC04_SELLING_ITEM_DISCOUNTS.MIN_PERC,"+
          "DOC04_SELLING_ITEM_DISCOUNTS.MAX_PERC,DOC04_SELLING_ITEM_DISCOUNTS.START_DATE,DOC04_SELLING_ITEM_DISCOUNTS.END_DATE,"+
          "DOC04_SELLING_ITEM_DISCOUNTS.DISCOUNT_DESCRIPTION,DOC04_SELLING_ITEM_DISCOUNTS.VALUE,DOC04_SELLING_ITEM_DISCOUNTS.PERC,"+
          "DOC04_SELLING_ITEM_DISCOUNTS.DOC_TYPE,DOC04_SELLING_ITEM_DISCOUNTS.DOC_YEAR,DOC04_SELLING_ITEM_DISCOUNTS.DOC_NUMBER,"+
          "DOC04_SELLING_ITEM_DISCOUNTS.ITEM_CODE_ITM01,DOC04_SELLING_ITEM_DISCOUNTS.MIN_QTY,DOC04_SELLING_ITEM_DISCOUNTS.MULTIPLE_QTY "+
          "from DOC04_SELLING_ITEM_DISCOUNTS where "+
          "DOC04_SELLING_ITEM_DISCOUNTS.COMPANY_CODE_SYS01=? and "+
          "DOC04_SELLING_ITEM_DISCOUNTS.DOC_TYPE=? and "+
          "DOC04_SELLING_ITEM_DISCOUNTS.DOC_YEAR=? and "+
          "DOC04_SELLING_ITEM_DISCOUNTS.DOC_NUMBER=? and "+
          "DOC04_SELLING_ITEM_DISCOUNTS.ITEM_CODE_ITM01=? ";

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC04","DOC04_SELLING_ITEM_DISCOUNTS.COMPANY_CODE_SYS01");
      attribute2dbField.put("discountCodeSal03DOC04","DOC04_SELLING_ITEM_DISCOUNTS.DISCOUNT_CODE_SAL03");
      attribute2dbField.put("minValueDOC04","DOC04_SELLING_ITEM_DISCOUNTS.MIN_VALUE");
      attribute2dbField.put("maxValueDOC04","DOC04_SELLING_ITEM_DISCOUNTS.MAX_VALUE");
      attribute2dbField.put("minPercDOC04","DOC04_SELLING_ITEM_DISCOUNTS.MIN_PERC");
      attribute2dbField.put("maxPercDOC04","DOC04_SELLING_ITEM_DISCOUNTS.MAX_PERC");
      attribute2dbField.put("startDateDOC04","DOC04_SELLING_ITEM_DISCOUNTS.START_DATE");
      attribute2dbField.put("endDateDOC04","DOC04_SELLING_ITEM_DISCOUNTS.END_DATE");
      attribute2dbField.put("discountDescriptionDOC04","DOC04_SELLING_ITEM_DISCOUNTS.DISCOUNT_DESCRIPTION");
      attribute2dbField.put("valueDOC04","DOC04_SELLING_ITEM_DISCOUNTS.VALUE");
      attribute2dbField.put("percDOC04","DOC04_SELLING_ITEM_DISCOUNTS.PERC");
      attribute2dbField.put("docTypeDOC04","DOC04_SELLING_ITEM_DISCOUNTS.DOC_TYPE");
      attribute2dbField.put("docYearDOC04","DOC04_SELLING_ITEM_DISCOUNTS.DOC_YEAR");
      attribute2dbField.put("docNumberDOC04","DOC04_SELLING_ITEM_DISCOUNTS.DOC_NUMBER");
      attribute2dbField.put("itemCodeItm01DOC04","DOC04_SELLING_ITEM_DISCOUNTS.ITEM_CODE_ITM01");
      attribute2dbField.put("minQtyDOC04","DOC04_SELLING_ITEM_DISCOUNTS.MIN_QTY");
      attribute2dbField.put("multipleQtyDOC04","DOC04_SELLING_ITEM_DISCOUNTS.MULTIPLE_QTY");

      attribute2dbField.put("variantTypeItm06DOC04","DOC04_SELLING_ITEM_DISCOUNTS.VARIANT_TYPE_ITM06");
      attribute2dbField.put("variantCodeItm11DOC04","DOC04_SELLING_ITEM_DISCOUNTS.VARIANT_CODE_ITM11");
      attribute2dbField.put("variantTypeItm07DOC04","DOC04_SELLING_ITEM_DISCOUNTS.VARIANT_TYPE_ITM07");
      attribute2dbField.put("variantCodeItm12DOC04","DOC04_SELLING_ITEM_DISCOUNTS.VARIANT_CODE_ITM12");
      attribute2dbField.put("variantTypeItm08DOC04","DOC04_SELLING_ITEM_DISCOUNTS.VARIANT_TYPE_ITM08");
      attribute2dbField.put("variantCodeItm13DOC04","DOC04_SELLING_ITEM_DISCOUNTS.VARIANT_CODE_ITM13");
      attribute2dbField.put("variantTypeItm09DOC04","DOC04_SELLING_ITEM_DISCOUNTS.VARIANT_TYPE_ITM09");
      attribute2dbField.put("variantCodeItm14DOC04","DOC04_SELLING_ITEM_DISCOUNTS.VARIANT_CODE_ITM14");
      attribute2dbField.put("variantTypeItm10DOC04","DOC04_SELLING_ITEM_DISCOUNTS.VARIANT_TYPE_ITM10");
      attribute2dbField.put("variantCodeItm15DOC04","DOC04_SELLING_ITEM_DISCOUNTS.VARIANT_CODE_ITM15");

      ArrayList values = new ArrayList();
      SaleDocRowPK pk = (SaleDocRowPK)gridParams.getOtherGridParams().get(ApplicationConsts.SALE_DOC_ROW_PK);
      values.add( pk.getCompanyCodeSys01DOC02() );
      values.add( pk.getDocTypeDOC02() );
      values.add( pk.getDocYearDOC02() );
      values.add( pk.getDocNumberDOC02() );
      values.add( pk.getItemCodeItm01DOC02() );

      // read from DOC04 table...
      Response answer = QueryUtil.getQuery(
          conn,
          new UserSessionParameters(username),
          sql,
          values,
          attribute2dbField,
          SaleItemDiscountVO.class,
          "Y",
          "N",
          null,
          gridParams,
          true
      );


      if (answer.isError()) throw new Exception(answer.getErrorMessage()); else return (VOListResponse)answer;

    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching item discounts list",ex);
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
  public VOListResponse loadSaleItemDiscounts(GridParams gridParams,String serverLanguageId,String username) throws Throwable {
    Statement stmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      discountBean.setConn(conn);
      DetailSaleDocRowVO rowVO = (DetailSaleDocRowVO)gridParams.getOtherGridParams().get(ApplicationConsts.SALE_DOC_ROW_VO);

      // retrieve item discount codes...
      stmt = conn.createStatement();
      ResultSet rset = stmt.executeQuery(
          "select DISCOUNT_CODE_SAL03 from SAL04_ITEM_DISCOUNTS,SAL03_DISCOUNTS where "+
          "SAL04_ITEM_DISCOUNTS.COMPANY_CODE_SYS01='"+rowVO.getCompanyCodeSys01DOC02()+"' and ITEM_CODE_ITM01='"+rowVO.getItemCodeItm01DOC02()+"' and "+
          "SAL04_ITEM_DISCOUNTS.COMPANY_CODE_SYS01=SAL03_DISCOUNTS.COMPANY_CODE_SYS01 and "+
          "SAL04_ITEM_DISCOUNTS.DISCOUNT_CODE_SAL03=SAL03_DISCOUNTS.DISCOUNT_CODE and "+
          "SAL03_DISCOUNTS.CURRENCY_CODE_REG03='"+rowVO.getCurrencyCodeReg03DOC01()+"' and "+
          "SAL03_DISCOUNTS.MIN_QTY<="+rowVO.getQtyDOC02()

      );
      ArrayList discountCodes = new ArrayList();
      while(rset.next()) {
        discountCodes.add( rset.getString(1) );
      }
      rset.close();

      // retrieve item hierarchy discount codes...
      rset = stmt.executeQuery(
          "select DISCOUNT_CODE_SAL03 from SAL05_ITEM_HIERAR_DISCOUNTS,SAL03_DISCOUNTS where "+
          "SAL05_ITEM_HIERAR_DISCOUNTS.COMPANY_CODE_SYS01='"+rowVO.getCompanyCodeSys01DOC02()+"' and PROGRESSIVE_HIE01="+rowVO.getProgressiveHie01ITM01()+" and "+
          "SAL05_ITEM_HIERAR_DISCOUNTS.COMPANY_CODE_SYS01=SAL03_DISCOUNTS.COMPANY_CODE_SYS01 and "+
          "SAL05_ITEM_HIERAR_DISCOUNTS.DISCOUNT_CODE_SAL03=SAL03_DISCOUNTS.DISCOUNT_CODE and "+
          "SAL03_DISCOUNTS.CURRENCY_CODE_REG03='"+rowVO.getCurrencyCodeReg03DOC01()+"' and "+
          "SAL03_DISCOUNTS.MIN_QTY<="+rowVO.getQtyDOC02()
      );
      while(rset.next()) {
        discountCodes.add( rset.getString(1) );
      }
      rset.close();

      Response res = discountBean.getDiscountsList(
          rowVO.getCompanyCodeSys01DOC02(),
          discountCodes,
          serverLanguageId,
          gridParams,
          username,
          DiscountVO.class
      );

      if (!res.isError()) {
        java.util.List list = ((VOListResponse)res).getRows();
        DiscountVO vo = null;
        int i=0;
        while(i<list.size()) {
          vo = (DiscountVO)list.get(i);
          if (vo.getStartDateSAL03().getTime()>System.currentTimeMillis() ||
              vo.getEndDateSAL03().getTime()<System.currentTimeMillis())
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
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while fetching item discounts list",ex);
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
      try {
          discountBean.setConn(null);
      } catch (Exception ex) {}
    }

  }




  /**
   * Business logic to execute.
   */
  public VOListResponse updateSaleDocRowDiscounts(
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

      SaleItemDiscountVO oldVO = null;
      SaleItemDiscountVO newVO = null;
      Response res = null;

      for(int i=0;i<oldVOs.size();i++) {
        oldVO = (SaleItemDiscountVO)oldVOs.get(i);
        newVO = (SaleItemDiscountVO)newVOs.get(i);

        HashSet pkAttrs = new HashSet();
        pkAttrs.add("companyCodeSys01DOC04");
        pkAttrs.add("docTypeDOC04");
        pkAttrs.add("docYearDOC04");
        pkAttrs.add("docNumberDOC04");
        pkAttrs.add("itemCodeItm01DOC04");
        pkAttrs.add("discountCodeSal03DOC04");
        pkAttrs.add("variantTypeItm06DOC04");
        pkAttrs.add("variantCodeItm11DOC04");
        pkAttrs.add("variantTypeItm07DOC04");
        pkAttrs.add("variantCodeItm12DOC04");
        pkAttrs.add("variantTypeItm08DOC04");
        pkAttrs.add("variantCodeItm13DOC04");
        pkAttrs.add("variantTypeItm09DOC04");
        pkAttrs.add("variantCodeItm14DOC04");
        pkAttrs.add("variantTypeItm10DOC04");
        pkAttrs.add("variantCodeItm15DOC04");

        HashMap attribute2dbField = new HashMap();
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

        res = new QueryUtil().updateTable(
            conn,
            new UserSessionParameters(username),
            pkAttrs,
            oldVO,
            newVO,
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
      }

      res = totals.updateTaxableIncomes(
        variant1Descriptions,
        variant2Descriptions,
        variant3Descriptions,
        variant4Descriptions,
        variant5Descriptions,
        new SaleDocPK(newVO.getCompanyCodeSys01DOC04(),newVO.getDocTypeDOC04(),newVO.getDocYearDOC04(),newVO.getDocNumberDOC04()),
        serverLanguageId,
        username

      );
      if (res.isError()) {
        throw new Exception(res.getErrorMessage());
      }

      return new VOListResponse(newVOs,false,newVOs.size());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating existing item discounts",ex);
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
  public VOResponse deleteSaleDocRowDiscounts(
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

      SaleItemDiscountVO vo = null;

      pstmt = conn.prepareStatement(
          "delete from DOC04_SELLING_ITEM_DISCOUNTS where "+
          "DOC04_SELLING_ITEM_DISCOUNTS.COMPANY_CODE_SYS01=? and "+
          "DOC04_SELLING_ITEM_DISCOUNTS.DOC_TYPE=? and "+
          "DOC04_SELLING_ITEM_DISCOUNTS.DOC_YEAR=? and "+
          "DOC04_SELLING_ITEM_DISCOUNTS.DOC_NUMBER=? and "+
          "DOC04_SELLING_ITEM_DISCOUNTS.ITEM_CODE_ITM01=? and "+
          "DOC04_SELLING_ITEM_DISCOUNTS.DISCOUNT_CODE_SAL03=?");

      for(int i=0;i<list.size();i++) {
        // phisically delete the record in DOC04...
        vo = (SaleItemDiscountVO)list.get(i);
        pstmt.setString(1,vo.getCompanyCodeSys01DOC04());
        pstmt.setString(2,vo.getDocTypeDOC04());
        pstmt.setBigDecimal(3,vo.getDocYearDOC04());
        pstmt.setBigDecimal(4,vo.getDocNumberDOC04());
        pstmt.setString(5,vo.getItemCodeItm01DOC04());
        pstmt.setString(6,vo.getDiscountCodeSal03DOC04());
        pstmt.execute();
      }

      Response res = totals.updateTaxableIncomes(
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

      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing item discounts",ex);
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

