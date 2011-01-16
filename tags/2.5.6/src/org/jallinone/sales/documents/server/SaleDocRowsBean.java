package org.jallinone.sales.documents.server;

import org.openswing.swing.server.*;

import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.GridParams;

import java.sql.*;

import org.openswing.swing.logger.server.*;
import org.jallinone.sales.documents.java.*;
import org.jallinone.system.progressives.server.CompanyProgressiveUtils;
import org.jallinone.system.server.*;
import org.jallinone.variants.java.VariantNameVO;
import org.jallinone.variants.java.VariantsMatrixColumnVO;
import org.jallinone.variants.java.VariantsMatrixRowVO;
import org.jallinone.variants.java.VariantsMatrixUtils;
import org.jallinone.variants.java.VariantsMatrixVO;

import java.math.*;

import org.jallinone.commons.java.ApplicationConsts;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to manage sales order rows.</p>
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
public class SaleDocRowsBean  implements SaleDocRows {


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



  private InsertSaleSerialNumbersBean serialNumBean;

  public void setSerialNumBean(InsertSaleSerialNumbersBean serialNumBean) {
    this.serialNumBean = serialNumBean;
  }


  private InsertSaleItemBean bean;

  public void setBean(InsertSaleItemBean bean) {
	  this.bean = bean;
  }

		private LoadSaleDocRowBean loadSaleDocRowBean;

		public void setLoadSaleDocRowBean(LoadSaleDocRowBean loadSaleDocRowBean) {
			this.loadSaleDocRowBean = loadSaleDocRowBean;
		}




  public SaleDocRowsBean() {
  }


  /**
   * Unsupported method, used to force the generation of a complex type in wsdl file for the return type
   */
  public GridSaleDocRowVO getSaleDoc(SaleDocPK pk) {
	  throw new UnsupportedOperationException();
  }



  /**
   * Business logic to execute.
   */
  public VOResponse updateSaleDocRow(
      HashMap variant1Descriptions,
      HashMap variant2Descriptions,
      HashMap variant3Descriptions,
      HashMap variant4Descriptions,
      HashMap variant5Descriptions,
      DetailSaleDocRowVO oldVO, DetailSaleDocRowVO newVO,
      String serverLanguageId, String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      totals.setConn(conn); // use same transaction...
      serialNumBean.setConn(conn); // use same transaction...
      loadSaleDocRowBean.setConn(conn);

      Map attribute2dbField = new HashMap();
      attribute2dbField.put("companyCodeSys01DOC02","COMPANY_CODE_SYS01");
      attribute2dbField.put("docTypeDOC02","DOC_TYPE");
      attribute2dbField.put("docYearDOC02","DOC_YEAR");
      attribute2dbField.put("docNumberDOC02","DOC_NUMBER");
      attribute2dbField.put("rowNumberDOC02","ROW_NUMBER");
      attribute2dbField.put("itemCodeItm01DOC02","ITEM_CODE_ITM01");
      attribute2dbField.put("vatCodeItm01DOC02","VAT_CODE_ITM01");
      attribute2dbField.put("valueSal02DOC02","VALUE_SAL02");
      attribute2dbField.put("valueDOC02","VALUE");
      attribute2dbField.put("qtyDOC02","QTY");
      attribute2dbField.put("totalDiscountDOC02","TOTAL_DISCOUNT");
      attribute2dbField.put("vatValueDOC02","VAT_VALUE");

      attribute2dbField.put("vatDescriptionDOC02","VAT_DESCRIPTION");
      attribute2dbField.put("startDateSal02DOC02","START_DATE_SAL02");
      attribute2dbField.put("endDateSal02DOC02","END_DATE_SAL02");
      attribute2dbField.put("decimalsReg02DOC02","DECIMALS_REG02");
      attribute2dbField.put("minSellingQtyItm01DOC02","MIN_SELLING_QTY_ITM01");
      attribute2dbField.put("minSellingQtyUmCodeReg02DOC02","MIN_SELLING_QTY_UM_CODE_REG02");
      attribute2dbField.put("valueReg01DOC02","VALUE_REG01");
      attribute2dbField.put("deductibleReg01DOC02","DEDUCTIBLE_REG01");
      attribute2dbField.put("taxableIncomeDOC02","TAXABLE_INCOME");
      attribute2dbField.put("progressiveHie02DOC02","PROGRESSIVE_HIE02");
      attribute2dbField.put("deliveryDateDOC02","DELIVERY_DATE");
      attribute2dbField.put("outQtyDOC02","OUT_QTY");
      attribute2dbField.put("progressiveHie01DOC02","PROGRESSIVE_HIE01");
      attribute2dbField.put("discountValueDOC02","DISCOUNT_VALUE");
      attribute2dbField.put("discountPercDOC02","DISCOUNT_PERC");
      attribute2dbField.put("invoiceQtyDOC02","INVOICE_QTY");

      attribute2dbField.put("variantTypeItm06DOC02","VARIANT_TYPE_ITM06");
      attribute2dbField.put("variantCodeItm11DOC02","VARIANT_CODE_ITM11");
      attribute2dbField.put("variantTypeItm07DOC02","VARIANT_TYPE_ITM07");
      attribute2dbField.put("variantCodeItm12DOC02","VARIANT_CODE_ITM12");
      attribute2dbField.put("variantTypeItm08DOC02","VARIANT_TYPE_ITM08");
      attribute2dbField.put("variantCodeItm13DOC02","VARIANT_CODE_ITM13");
      attribute2dbField.put("variantTypeItm09DOC02","VARIANT_TYPE_ITM09");
      attribute2dbField.put("variantCodeItm14DOC02","VARIANT_CODE_ITM14");
      attribute2dbField.put("variantTypeItm10DOC02","VARIANT_TYPE_ITM10");
      attribute2dbField.put("variantCodeItm15DOC02","VARIANT_CODE_ITM15");

      HashSet pkAttributes = new HashSet();
      pkAttributes.add("companyCodeSys01DOC02");
      pkAttributes.add("docTypeDOC02");
      pkAttributes.add("docYearDOC02");
      pkAttributes.add("docNumberDOC02");
      pkAttributes.add("itemCodeItm01DOC02");
      pkAttributes.add("variantTypeItm06DOC02");
      pkAttributes.add("variantCodeItm11DOC02");
      pkAttributes.add("variantTypeItm07DOC02");
      pkAttributes.add("variantCodeItm12DOC02");
      pkAttributes.add("variantTypeItm08DOC02");
      pkAttributes.add("variantCodeItm13DOC02");
      pkAttributes.add("variantTypeItm09DOC02");
      pkAttributes.add("variantCodeItm14DOC02");
      pkAttributes.add("variantTypeItm10DOC02");
      pkAttributes.add("variantCodeItm15DOC02");

			if ((newVO.getDocTypeDOC02().equals(ApplicationConsts.SALE_CONTRACT_DOC_TYPE) ||
					 newVO.getDocTypeDOC02().equals(ApplicationConsts.SALE_ORDER_DOC_TYPE) ||
					 newVO.getDocTypeDOC02().equals(ApplicationConsts.SALE_DESK_DOC_TYPE) ||
					 newVO.getDocTypeDOC02().equals(ApplicationConsts.SALE_ESTIMATE_DOC_TYPE)) &&
					Boolean.TRUE.equals(newVO.getNoWarehouseMovITM01()))
				newVO.setOutQtyDOC02(newVO.getQtyDOC02());

      // update DOC02 table...
      Response res = QueryUtil.updateTable(
          conn,
          new UserSessionParameters(username),
          pkAttributes,
          oldVO,
          newVO,
          "DOC02_SELLING_ITEMS",
          attribute2dbField,
          "Y",
          "N",
          null,
          true
      );

      if (res.isError()) {
        throw new Exception(res.getErrorMessage());
      }

      SaleDocPK pk = new SaleDocPK(
          newVO.getCompanyCodeSys01DOC02(),
          newVO.getDocTypeDOC02(),
          newVO.getDocYearDOC02(),
          newVO.getDocNumberDOC02()
      );
      Response totalRes = totals.updateTaxableIncomes(
        variant1Descriptions,
        variant2Descriptions,
        variant3Descriptions,
        variant4Descriptions,
        variant5Descriptions,
        pk,
        serverLanguageId,
        username

      );
      if (totalRes.isError()) {
        throw new Exception(totalRes.getErrorMessage());
      }

      // insert serial numbers...
      if (newVO.getSerialNumbers().size()>0) {
        Response serialRes = serialNumBean.reinsertSaleSerialNumbers(newVO,serverLanguageId,username);
        if (serialRes.isError()) {
        	throw new Exception(serialRes.getErrorMessage());
        }
      }

      // reload v.o. after updating taxable incomes...
      res = loadSaleDocRowBean.loadSaleDocRow(
        variant1Descriptions,variant2Descriptions,variant3Descriptions,variant4Descriptions,variant5Descriptions,
        new SaleDocRowPK(
          pk.getCompanyCodeSys01DOC01(),
          pk.getDocTypeDOC01(),
          pk.getDocYearDOC01(),
          pk.getDocNumberDOC01(),
          newVO.getItemCodeItm01DOC02(),
          newVO.getVariantTypeItm06DOC02(),
          newVO.getVariantCodeItm11DOC02(),
          newVO.getVariantTypeItm07DOC02(),
          newVO.getVariantCodeItm12DOC02(),
          newVO.getVariantTypeItm08DOC02(),
          newVO.getVariantCodeItm13DOC02(),
          newVO.getVariantTypeItm09DOC02(),
          newVO.getVariantCodeItm14DOC02(),
          newVO.getVariantTypeItm10DOC02(),
          newVO.getVariantCodeItm15DOC02()
        ),
        serverLanguageId,
        username

      );
      if (res.isError()) {
        throw new Exception(res.getErrorMessage());
      }
      newVO = (DetailSaleDocRowVO)((VOResponse)res).getVo();

      return new VOResponse(newVO);
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while updating an existing sale document row",ex);
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
    	  loadSaleDocRowBean.setConn(null);
    	  totals.setConn(null);
    	  serialNumBean.setConn(null);
      } catch (Exception ex) {}

    }
  }




  /**
   * Business logic to execute.
   */
  public VOResponse insertSaleDocRows(
      HashMap variant1Descriptions,
      HashMap variant2Descriptions,
      HashMap variant3Descriptions,
      HashMap variant4Descriptions,
      HashMap variant5Descriptions,
      DetailSaleDocRowVO voTemplate, VariantsMatrixVO matrixVO,
      Object[][] cells, BigDecimal currencyDecimals, String serverLanguageId,
      String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      bean.setConn(conn); // use same transaction...
      totals.setConn(conn); // use same transaction...
      serialNumBean.setConn(conn); // use same transaction...
      bean.setConn(conn);

      VariantsMatrixColumnVO colVO = null;
      VariantsMatrixRowVO rowVO = null;
      DetailSaleDocRowVO vo = null;
      Response res = null;
      for(int i=0;i<cells.length;i++) {
        rowVO = (VariantsMatrixRowVO)matrixVO.getRowDescriptors()[i];

        if (matrixVO.getColumnDescriptors().length==0) {

          if (cells[i][0]!=null) {
            vo = (DetailSaleDocRowVO)voTemplate.clone();
            try {
								vo.setQtyDOC02((BigDecimal)cells[i][0]);
							} catch (Exception e) {
								continue;
							}
						VariantsMatrixUtils.setVariantTypesAndCodes(vo,"DOC02",matrixVO,rowVO,null);
            vo.setOutQtyDOC02(new BigDecimal(0));

						if ((vo.getDocTypeDOC02().equals(ApplicationConsts.SALE_CONTRACT_DOC_TYPE) ||
								 vo.getDocTypeDOC02().equals(ApplicationConsts.SALE_ORDER_DOC_TYPE) ||
								 vo.getDocTypeDOC02().equals(ApplicationConsts.SALE_DESK_DOC_TYPE) ||
								 vo.getDocTypeDOC02().equals(ApplicationConsts.SALE_ESTIMATE_DOC_TYPE)) &&
								Boolean.TRUE.equals(vo.getNoWarehouseMovITM01()))
							vo.setOutQtyDOC02(vo.getQtyDOC02());

            if (vo.getInvoiceQtyDOC02()==null)
              vo.setInvoiceQtyDOC02(new BigDecimal(0));

            res = bean.insertSaleItem(
							variant1Descriptions,
							variant2Descriptions,
							variant3Descriptions,
							variant4Descriptions,
							variant5Descriptions,
							vo,serverLanguageId,username
				     );
            if (res.isError()) {
              throw new Exception(res.getErrorMessage());
            }

            // insert serial numbers...
            if (vo.getSerialNumbers().size()>0) {
              res = serialNumBean.reinsertSaleSerialNumbers(vo,serverLanguageId,username);
              if (res.isError()) {
                throw new Exception(res.getErrorMessage());
              }
            }
          }

        }
        else
          for(int k=0;k<matrixVO.getColumnDescriptors().length;k++) {

            colVO = (VariantsMatrixColumnVO)matrixVO.getColumnDescriptors()[k];
            if (cells[i][k]!=null) {
              vo = (DetailSaleDocRowVO)voTemplate.clone();
              try {
            	  vo.setQtyDOC02((BigDecimal)cells[i][k]);
              } catch (Exception e) {
            	  continue;
              }
              VariantsMatrixUtils.setVariantTypesAndCodes(vo,"DOC02",matrixVO,rowVO,colVO);
              vo.setOutQtyDOC02(new BigDecimal(0));
							if ((vo.getDocTypeDOC02().equals(ApplicationConsts.SALE_CONTRACT_DOC_TYPE) ||
									 vo.getDocTypeDOC02().equals(ApplicationConsts.SALE_ORDER_DOC_TYPE) ||
									 vo.getDocTypeDOC02().equals(ApplicationConsts.SALE_DESK_DOC_TYPE) ||
									 vo.getDocTypeDOC02().equals(ApplicationConsts.SALE_ESTIMATE_DOC_TYPE)) &&
									Boolean.TRUE.equals(vo.getNoWarehouseMovITM01()))
								vo.setOutQtyDOC02(vo.getQtyDOC02());
              if (vo.getInvoiceQtyDOC02()==null)
                vo.setInvoiceQtyDOC02(new BigDecimal(0));

              res = bean.insertSaleItem(
								variant1Descriptions,
								variant2Descriptions,
								variant3Descriptions,
								variant4Descriptions,
								variant5Descriptions,
				        vo,serverLanguageId,username
				       );
              if (res.isError()) {
                throw new Exception(res.getErrorMessage());
              }

              // insert serial numbers...
              if (vo.getSerialNumbers().size()>0) {
                res = serialNumBean.reinsertSaleSerialNumbers(vo,serverLanguageId,username);
                if (res.isError()) {
                  throw new Exception(res.getErrorMessage());
                }
              }


            } // end if on not null
          } // end inner for
      } // end outer for

      // update doc state...
      SaleDocPK pk = new SaleDocPK(
          voTemplate.getCompanyCodeSys01DOC02(),
          voTemplate.getDocTypeDOC02(),
          voTemplate.getDocYearDOC02(),
          voTemplate.getDocNumberDOC02()
      );
      pstmt = conn.prepareStatement("update DOC01_SELLING set DOC_STATE=? where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? and DOC_STATE=?");
      pstmt.setString(1,ApplicationConsts.HEADER_BLOCKED);
      pstmt.setString(2,pk.getCompanyCodeSys01DOC01());
      pstmt.setString(3,pk.getDocTypeDOC01());
      pstmt.setBigDecimal(4,pk.getDocYearDOC01());
      pstmt.setBigDecimal(5,pk.getDocNumberDOC01());
			pstmt.setString(6,ApplicationConsts.OPENED);
      pstmt.execute();

      // recalculate totals...
      res = totals.updateTaxableIncomes(
        variant1Descriptions,
        variant2Descriptions,
        variant3Descriptions,
        variant4Descriptions,
        variant5Descriptions,
        pk,
        serverLanguageId,
        username

      );
      if (res.isError()) {
        throw new Exception(res.getErrorMessage());
      }

      return new VOResponse(voTemplate);
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while inserting a new sale document item row",ex);
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
        bean.setConn(null);
        totals.setConn(null);
        serialNumBean.setConn(null);
      } catch (Exception ex) {}
    }

  }


  private boolean containsVariant(VariantsMatrixVO vo,String tableName) {
    for(int i=0;i<vo.getManagedVariants().length;i++)
      if (((VariantNameVO)vo.getManagedVariants()[i]).getTableName().equals(tableName))
        return true;
    return false;
  }





  /**
   * Business logic to execute.
   */
  public VOResponse deleteSaleDocRows(
      HashMap variant1Descriptions,
      HashMap variant2Descriptions,
      HashMap variant3Descriptions,
      HashMap variant4Descriptions,
      HashMap variant5Descriptions,
      ArrayList list, String serverLanguageId, String username) throws Throwable {
    PreparedStatement pstmt1 = null;
    PreparedStatement pstmt2 = null;
    PreparedStatement pstmt3 = null;

    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      totals.setConn(conn); // use same transaction...
      SaleDocRowPK rowPK = null;

      pstmt1 = conn.prepareStatement(
          "delete from DOC18_SELLING_SERIAL_NUMBERS where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? and ITEM_CODE_ITM01=? and "+
          "VARIANT_TYPE_ITM06=? and VARIANT_CODE_ITM11=? and "+
          "VARIANT_TYPE_ITM07=? and VARIANT_CODE_ITM12=? and "+
          "VARIANT_TYPE_ITM08=? and VARIANT_CODE_ITM13=? and "+
          "VARIANT_TYPE_ITM09=? and VARIANT_CODE_ITM14=? and "+
          "VARIANT_TYPE_ITM10=? and VARIANT_CODE_ITM15=? "

      );
      pstmt2 = conn.prepareStatement(
          "delete from DOC04_SELLING_ITEM_DISCOUNTS where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? and ITEM_CODE_ITM01=? and "+
          "VARIANT_TYPE_ITM06=? and VARIANT_CODE_ITM11=? and "+
          "VARIANT_TYPE_ITM07=? and VARIANT_CODE_ITM12=? and "+
          "VARIANT_TYPE_ITM08=? and VARIANT_CODE_ITM13=? and "+
          "VARIANT_TYPE_ITM09=? and VARIANT_CODE_ITM14=? and "+
          "VARIANT_TYPE_ITM10=? and VARIANT_CODE_ITM15=? "

      );
      pstmt3 = conn.prepareStatement(
          "delete from DOC02_SELLING_ITEMS where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=? and ITEM_CODE_ITM01=? and "+
          "VARIANT_TYPE_ITM06=? and VARIANT_CODE_ITM11=? and "+
          "VARIANT_TYPE_ITM07=? and VARIANT_CODE_ITM12=? and "+
          "VARIANT_TYPE_ITM08=? and VARIANT_CODE_ITM13=? and "+
          "VARIANT_TYPE_ITM09=? and VARIANT_CODE_ITM14=? and "+
          "VARIANT_TYPE_ITM10=? and VARIANT_CODE_ITM15=? "

      );

      for(int i=0;i<list.size();i++) {
        rowPK = (SaleDocRowPK)list.get(i);

        // phisically delete records in DOC18...
        pstmt1.setString(1,rowPK.getCompanyCodeSys01DOC02());
        pstmt1.setString(2,rowPK.getDocTypeDOC02());
        pstmt1.setBigDecimal(3,rowPK.getDocYearDOC02());
        pstmt1.setBigDecimal(4,rowPK.getDocNumberDOC02());
        pstmt1.setString(5,rowPK.getItemCodeItm01DOC02());

        pstmt1.setString(6,rowPK.getVariantTypeItm06DOC02());
        pstmt1.setString(7,rowPK.getVariantCodeItm11DOC02());
        pstmt1.setString(8,rowPK.getVariantTypeItm07DOC02());
        pstmt1.setString(9,rowPK.getVariantCodeItm12DOC02());
        pstmt1.setString(10,rowPK.getVariantTypeItm08DOC02());
        pstmt1.setString(11,rowPK.getVariantCodeItm13DOC02());
        pstmt1.setString(12,rowPK.getVariantTypeItm09DOC02());
        pstmt1.setString(13,rowPK.getVariantCodeItm14DOC02());
        pstmt1.setString(14,rowPK.getVariantTypeItm10DOC02());
        pstmt1.setString(15,rowPK.getVariantCodeItm15DOC02());

        pstmt1.execute();

        // phisically delete records in DOC04...
        pstmt2.setString(1,rowPK.getCompanyCodeSys01DOC02());
        pstmt2.setString(2,rowPK.getDocTypeDOC02());
        pstmt2.setBigDecimal(3,rowPK.getDocYearDOC02());
        pstmt2.setBigDecimal(4,rowPK.getDocNumberDOC02());
        pstmt2.setString(5,rowPK.getItemCodeItm01DOC02());

        pstmt2.setString(6,rowPK.getVariantTypeItm06DOC02());
        pstmt2.setString(7,rowPK.getVariantCodeItm11DOC02());
        pstmt2.setString(8,rowPK.getVariantTypeItm07DOC02());
        pstmt2.setString(9,rowPK.getVariantCodeItm12DOC02());
        pstmt2.setString(10,rowPK.getVariantTypeItm08DOC02());
        pstmt2.setString(11,rowPK.getVariantCodeItm13DOC02());
        pstmt2.setString(12,rowPK.getVariantTypeItm09DOC02());
        pstmt2.setString(13,rowPK.getVariantCodeItm14DOC02());
        pstmt2.setString(14,rowPK.getVariantTypeItm10DOC02());
        pstmt2.setString(15,rowPK.getVariantCodeItm15DOC02());

        pstmt2.execute();

        // phisically delete the record in DOC02...
        pstmt3.setString(1,rowPK.getCompanyCodeSys01DOC02());
        pstmt3.setString(2,rowPK.getDocTypeDOC02());
        pstmt3.setBigDecimal(3,rowPK.getDocYearDOC02());
        pstmt3.setBigDecimal(4,rowPK.getDocNumberDOC02());
        pstmt3.setString(5,rowPK.getItemCodeItm01DOC02());

        pstmt3.setString(6,rowPK.getVariantTypeItm06DOC02());
        pstmt3.setString(7,rowPK.getVariantCodeItm11DOC02());
        pstmt3.setString(8,rowPK.getVariantTypeItm07DOC02());
        pstmt3.setString(9,rowPK.getVariantCodeItm12DOC02());
        pstmt3.setString(10,rowPK.getVariantTypeItm08DOC02());
        pstmt3.setString(11,rowPK.getVariantCodeItm13DOC02());
        pstmt3.setString(12,rowPK.getVariantTypeItm09DOC02());
        pstmt3.setString(13,rowPK.getVariantCodeItm14DOC02());
        pstmt3.setString(14,rowPK.getVariantTypeItm10DOC02());
        pstmt3.setString(15,rowPK.getVariantCodeItm15DOC02());

        pstmt3.execute();
      }

      // recalculate totals...
      SaleDocPK pk = new SaleDocPK(
          rowPK.getCompanyCodeSys01DOC02(),
          rowPK.getDocTypeDOC02(),
          rowPK.getDocYearDOC02(),
          rowPK.getDocNumberDOC02()
      );
      Response res = totals.updateTaxableIncomes(
        variant1Descriptions,
        variant2Descriptions,
        variant3Descriptions,
        variant4Descriptions,
        variant5Descriptions,
        pk,
        serverLanguageId,
        username

      );
      if (res.isError()) {
        throw new Exception(res.getErrorMessage());
      }

      return new VOResponse(new Boolean(true));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while deleting existing sale order rows",ex);
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
        pstmt1.close();
      }
      catch (Exception ex2) {
      }
      try {
        pstmt2.close();
      }
      catch (Exception ex2) {
      }
      try {
        pstmt3.close();
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

