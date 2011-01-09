package org.jallinone.scheduler.callouts.server;

import org.openswing.swing.server.*;
import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.scheduler.callouts.java.*;
import org.jallinone.system.server.*;
import java.math.*;
import org.jallinone.commons.server.*;
import org.jallinone.system.translations.server.*;
import org.jallinone.commons.java.*;
import org.jallinone.sales.documents.server.*;
import org.jallinone.sales.customers.server.*;
import org.jallinone.sales.customers.server.*;
import org.jallinone.sales.documents.java.*;
import org.openswing.swing.internationalization.server.*;
import org.openswing.swing.internationalization.java.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.scheduler.activities.java.*;
import org.jallinone.sales.documents.activities.server.*;
import org.jallinone.scheduler.activities.server.*;
import org.jallinone.scheduler.activities.server.*;
import org.jallinone.scheduler.activities.java.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.scheduler.activities.java.*;
import org.jallinone.scheduler.activities.java.*;
import org.jallinone.sales.documents.activities.java.*;
import org.jallinone.registers.task.server.*;
import org.jallinone.registers.task.java.*;
import org.jallinone.sales.activities.server.*;
import org.jallinone.system.progressives.server.*;
import org.jallinone.sales.activities.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;
import org.jallinone.system.progressives.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to create a sale invoice from a closed scheduled activity
 * linked to a call-out request.</p>
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
public class CreateInvoiceFromScheduledActivityBean  implements CreateInvoiceFromScheduledActivity {


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



  private SaleDocsBean saleDoc;

  public void setSaleDoc(SaleDocsBean saleDoc) {
    this.saleDoc = saleDoc;
  }

  private InsertSaleItemBean insItemRow;

  public void setInsItemRow(InsertSaleItemBean insItemRow) {
    this.insItemRow = insItemRow;
  }

  private SaleDocActivitiesBean insActRow;

  public void setInsActRow(SaleDocActivitiesBean insActRow) {
    this.insActRow = insActRow;
  }

  private ScheduledItemsBean loadItemsRow;

  public void setLoadItemsRow(ScheduledItemsBean loadItemsRow) {
    this.loadItemsRow = loadItemsRow;
  }

  private ValidatePriceItemCodeBean price;

  public void setPrice(ValidatePriceItemCodeBean price) {
    this.price = price;
  }

  private UpdateTaxableIncomesBean totals;

  public void setTotals(UpdateTaxableIncomesBean totals) {
    this.totals = totals;
  }



  public CreateInvoiceFromScheduledActivityBean() {
  }



  /**
   * Business logic to execute.
   */
  public VOResponse createInvoiceFromScheduledActivity(
      HashMap variant1Descriptions,
      HashMap variant2Descriptions,
      HashMap variant3Descriptions,
      HashMap variant4Descriptions,
      HashMap variant5Descriptions,
      InvoiceFromCallOutRequestVO invVO, String t1, String serverLanguageId,
      String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      saleDoc.setConn(conn); // use same transaction...
      insItemRow.setConn(conn); // use same transaction...
      insActRow.setConn(conn); // use same transaction...
      loadItemsRow.setConn(conn); // use same transaction...
      price.setConn(conn); // use same transaction...
      totals.setConn(conn); // use same transaction...

      DetailCallOutRequestVO reqVO = invVO.getCallOutRequest();
      DetailSaleDocVO vo = invVO.getSaleVO();
      ScheduledActivityVO actVO = invVO.getActVO();

      // check if the customer alredy exists, otherwise it will be created...
      if (!invVO.isCustomerAlreadyExists()) {
        // customer does not exist: it will be created...

        // check if customer code is defined: if it's not defined then it will be defined as a progressive...
        if (vo.getCustomerCodeSAL07()==null || vo.getCustomerCodeSAL07().trim().equals("")) {
          vo.setCustomerCodeSAL07( String.valueOf(CompanyProgressiveUtils.getConsecutiveProgressive(vo.getCompanyCodeSys01DOC01(),"SAL07_CUSTOMERS",vo.getCompanyCodeSys01DOC01(),conn).intValue()) );
        }

        pstmt = conn.prepareStatement(
            "insert into SAL07_CUSTOMERS(COMPANY_CODE_SYS01,PROGRESSIVE_REG04,CUSTOMER_CODE,PAYMENT_CODE_REG10,"+
            "PRICELIST_CODE_SAL01,ENABLED,CREDIT_ACCOUNT_CODE_ACC02,ITEMS_ACCOUNT_CODE_ACC02,"+
            "ACTIVITIES_ACCOUNT_CODE_ACC02,CHARGES_ACCOUNT_CODE_ACC02) values(?,?,?,?,?,?,?,?,?,?)"
        );
        pstmt.setString(1,vo.getCompanyCodeSys01DOC01());
        pstmt.setBigDecimal(2,vo.getProgressiveReg04DOC01());
        pstmt.setString(3,vo.getCustomerCodeSAL07());
        pstmt.setString(4,vo.getPaymentCodeReg10DOC01());
        pstmt.setString(5,vo.getPricelistCodeSal01DOC01());
        pstmt.setString(6,"Y");
        pstmt.setString(7,invVO.getCreditAccountCodeAcc02SAL07());
        pstmt.setString(8,invVO.getItemsAccountCodeAcc02SAL07());
        pstmt.setString(9,invVO.getActivitiesAccountCodeAcc02SAL07());
        pstmt.setString(10,invVO.getChargesAccountCodeAcc02SAL07());
        pstmt.execute();
        pstmt.close();
      }

      // insert invoice header...
      Response res = saleDoc.insertSaleDoc(vo,serverLanguageId,username,vo.getCompanyCodeSys01DOC01(),new ArrayList());
      if (res.isError()) {
        throw new Exception(res.getErrorMessage());
      }

      // retrieve scheduled items...
      ScheduledActivityPK pk = new ScheduledActivityPK(
          reqVO.getCompanyCodeSys01SCH03(),
          reqVO.getProgressiveSch06SCH03()
      );
      GridParams gridParams = new GridParams();
      gridParams.getOtherGridParams().put(ApplicationConsts.SCHEDULED_ACTIVITY_PK,pk);
      res = loadItemsRow.loadScheduledItems(gridParams,serverLanguageId,username);
      if (res.isError()) {
        throw new Exception(res.getErrorMessage());
      }



      // create invoice item rows...
      java.util.List rows = ((VOListResponse)res).getRows();
      ScheduledItemVO schItemVO = null;
      DetailSaleDocRowVO saleItemVO = null;
      LookupValidationParams lookupPars = null;
      PriceItemVO priceVO = null;
      java.util.List priceRows = null;
      double vatPerc;
      HashMap map = new HashMap();
      map.put(ApplicationConsts.COMPANY_CODE_SYS01,reqVO.getCompanyCodeSys01SCH03());
      map.put(ApplicationConsts.PRICELIST,vo.getPricelistCodeSal01DOC01());
      for(int i=0;i<rows.size();i++) {
        schItemVO = (ScheduledItemVO)rows.get(i);
        if (schItemVO.getQtySCH15()==null)
          continue;
        saleItemVO = new DetailSaleDocRowVO();
        saleItemVO.setCompanyCodeSys01DOC02(reqVO.getCompanyCodeSys01SCH03());
        saleItemVO.setCurrencyCodeReg03DOC01(vo.getCurrencyCodeReg03DOC01());
        saleItemVO.setDocTypeDOC02(vo.getDocTypeDOC01());
        saleItemVO.setDocYearDOC02(vo.getDocYearDOC01());
        saleItemVO.setItemCodeItm01DOC02(schItemVO.getItemCodeItm01SCH15());
        saleItemVO.setQtyDOC02(schItemVO.getQtySCH15());
        lookupPars = new LookupValidationParams(schItemVO.getItemCodeItm01SCH15(),map);
        res = price.validatePriceItemCode(lookupPars,serverLanguageId,username);
        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }
        priceRows = ((VOListResponse)res).getRows();
        if (priceRows.size()==1)
          priceVO = (PriceItemVO)priceRows.get(0);
        else {
          throw new Exception(t1);
        }
        saleItemVO.setValueSal02DOC02(priceVO.getValueSAL02());
        saleItemVO.setTaxableIncomeDOC02(priceVO.getValueSAL02().multiply(saleItemVO.getQtyDOC02()));
        saleItemVO.setVatCodeItm01DOC02(priceVO.getVatCodeReg01ITM01());
        saleItemVO.setVatDescriptionDOC02(priceVO.getVatDescriptionSYS10());
        saleItemVO.setDeductibleReg01DOC02(priceVO.getDeductibleREG01());
        saleItemVO.setTotalDiscountDOC02(new BigDecimal(0));
        saleItemVO.setValueReg01DOC02(priceVO.getValueREG01());
        saleItemVO.setDocNumberDOC02(vo.getDocNumberDOC01());
        saleItemVO.setProgressiveHie02DOC02(priceVO.getProgressiveHie02ITM01());
        saleItemVO.setProgressiveHie01DOC02(priceVO.getProgressiveHie01ITM01());
        saleItemVO.setStartDateSal02DOC02(priceVO.getStartDateSAL02());
        saleItemVO.setEndDateSal02DOC02(priceVO.getEndDateSAL02());
        saleItemVO.setDeliveryDateDOC02(vo.getDocDateDOC01());
        saleItemVO.setMinSellingQtyUmCodeReg02DOC02(priceVO.getMinSellingQtyUmCodeReg02ITM01());
        saleItemVO.setMinSellingQtyItm01DOC02(priceVO.getMinSellingQtyITM01());
        saleItemVO.setDecimalsReg02DOC02(priceVO.getDecimalsREG02());

        vatPerc = saleItemVO.getValueReg01DOC02().doubleValue()*(1d-saleItemVO.getDeductibleReg01DOC02().doubleValue()/100d);
        saleItemVO.setVatValueDOC02(new BigDecimal(saleItemVO.getTaxableIncomeDOC02().doubleValue()*vatPerc/100));
        saleItemVO.setValueDOC02(saleItemVO.getTaxableIncomeDOC02().add(saleItemVO.getVatValueDOC02()));
        res = insItemRow.insertSaleItem(
					variant1Descriptions,
					variant2Descriptions,
					variant3Descriptions,
					variant4Descriptions,
					variant5Descriptions,
				  saleItemVO,serverLanguageId,username
				);
        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }
      }



      // retrieve scheduled employees and related act. duration and sale activity code and sale activity rate...
      // and create invoice activity row, for each distinct sale activity code...
      SaleDocActivityVO saleactVO = null;
      pstmt = conn.prepareStatement(
        "select SAL09_ACTIVITIES.ACTIVITY_CODE,SAL09_ACTIVITIES.VALUE,sum(SCH07_SCHEDULED_EMPLOYEES.DURATION),"+
        "SAL09_ACTIVITIES.VAT_CODE_REG01,REG01_VATS.VALUE,REG01_VATS.DEDUCTIBLE,ACT_SYS10.DESCRIPTION,VAT_SYS10.DESCRIPTION "+
        "from SAL09_ACTIVITIES,SCH01_EMPLOYEES,REG07_TASKS,SCH07_SCHEDULED_EMPLOYEES,REG01_VATS,SYS10_TRANSLATIONS ACT_SYS10,SYS10_TRANSLATIONS VAT_SYS10 where "+
        "SAL09_ACTIVITIES.COMPANY_CODE_SYS01=REG07_TASKS.COMPANY_CODE_SYS01 and "+
        "SAL09_ACTIVITIES.ACTIVITY_CODE=REG07_TASKS.ACTIVITY_CODE_SAL09 and "+
        "SAL09_ACTIVITIES.PROGRESSIVE_SYS10=ACT_SYS10.PROGRESSIVE and "+
        "ACT_SYS10.LANGUAGE_CODE=? and "+
        "REG01_VATS.PROGRESSIVE_SYS10=VAT_SYS10.PROGRESSIVE and "+
        "VAT_SYS10.LANGUAGE_CODE=? and "+
        "SAL09_ACTIVITIES.VAT_CODE_REG01=REG01_VATS.VAT_CODE and "+
        "REG07_TASKS.COMPANY_CODE_SYS01=SCH01_EMPLOYEES.COMPANY_CODE_SYS01 and "+
        "REG07_TASKS.TASK_CODE=SCH01_EMPLOYEES.TASK_CODE_REG07 and "+
        "SCH01_EMPLOYEES.COMPANY_CODE_SYS01=SCH07_SCHEDULED_EMPLOYEES.COMPANY_CODE_SYS01 and "+
        "SCH01_EMPLOYEES.PROGRESSIVE_REG04=SCH07_SCHEDULED_EMPLOYEES.PROGRESSIVE_REG04 and "+
        "SCH07_SCHEDULED_EMPLOYEES.COMPANY_CODE_SYS01=? and "+
        "SCH07_SCHEDULED_EMPLOYEES.PROGRESSIVE_SCH06=? "+
        "group by SAL09_ACTIVITIES.ACTIVITY_CODE,SAL09_ACTIVITIES.VAT_CODE_REG01,SAL09_ACTIVITIES.VALUE,REG01_VATS.VALUE,REG01_VATS.DEDUCTIBLE "
      );
      pstmt.setString(1,serverLanguageId);
      pstmt.setString(2,serverLanguageId);
      pstmt.setString(3,vo.getCompanyCodeSys01DOC01());
      pstmt.setBigDecimal(4,reqVO.getProgressiveSch06SCH03());
      ResultSet rset = pstmt.executeQuery();
      while(rset.next()) {
        saleactVO = new SaleDocActivityVO();
        saleactVO.setActivityCodeSal09DOC13(rset.getString(1));
        saleactVO.setActivityDescriptionDOC13(rset.getString(7));
        saleactVO.setCompanyCodeSys01DOC13(reqVO.getCompanyCodeSys01SCH03());
        saleactVO.setCurrencyCodeReg03DOC13(vo.getCurrencyCodeReg03DOC01());
        saleactVO.setDocTypeDOC13(vo.getDocTypeDOC01());
        saleactVO.setDocYearDOC13(vo.getDocYearDOC01());
        saleactVO.setDurationDOC13(rset.getBigDecimal(3));
        saleactVO.setValueSal09DOC13(rset.getBigDecimal(2));
        saleactVO.setValueDOC13(saleactVO.getValueSal09DOC13().multiply(saleactVO.getDurationDOC13().divide(new BigDecimal(60),BigDecimal.ROUND_HALF_UP)));
        saleactVO.setVatCodeSal09DOC13(rset.getString(4));
        saleactVO.setVatDescriptionDOC13(rset.getString(8));
        saleactVO.setDocNumberDOC13(vo.getDocNumberDOC01());
        saleactVO.setVatDeductibleDOC13(rset.getBigDecimal(6));
        saleactVO.setProgressiveSch06DOC13(actVO.getProgressiveSCH06());
        vatPerc = rset.getBigDecimal(5).doubleValue()*(1d-saleactVO.getVatDeductibleDOC13().doubleValue()/100d);
        saleactVO.setVatValueDOC13(new BigDecimal(saleactVO.getValueDOC13().doubleValue()*vatPerc/100));
        res = insActRow.insertSaleActivity(saleactVO,username);
        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }
      }
      rset.close();



      SaleDocPK docPK = new SaleDocPK(vo.getCompanyCodeSys01DOC01(),vo.getDocTypeDOC01(),vo.getDocYearDOC01(),vo.getDocNumberDOC01());
      res = totals.updateTaxableIncomes(
        variant1Descriptions,
        variant2Descriptions,
        variant3Descriptions,
        variant4Descriptions,
        variant5Descriptions,
        docPK,
        serverLanguageId,
        username
      );
      if (res.isError()) {
        throw new Exception(res.getErrorMessage());
      }


//      res = load.loadSaleDoc(docPK,serverLanguageId,username);
//      if (res.isError()) {
//        throw new Exception(res.getErrorMessage());
//      }
//      vo = (DetailSaleDocVO)((VOResponse)res).getVo();



      // update invoice state...
      pstmt = conn.prepareStatement("update DOC01_SELLING set DOC_STATE=? where COMPANY_CODE_SYS01=? and DOC_TYPE=? and DOC_YEAR=? and DOC_NUMBER=?");
      pstmt.setString(1,ApplicationConsts.HEADER_BLOCKED);
      pstmt.setString(2,vo.getCompanyCodeSys01DOC01());
      pstmt.setString(3,vo.getDocTypeDOC01());
      pstmt.setBigDecimal(4,vo.getDocYearDOC01());
      pstmt.setBigDecimal(5,vo.getDocNumberDOC01());
      pstmt.execute();
      pstmt.close();



      // update call out doc references and state...
      pstmt = conn.prepareStatement(
        "update SCH03_CALL_OUT_REQUESTS set DOC_TYPE_DOC01=?,DOC_YEAR_DOC01=?,DOC_NUMBER_DOC01=?,CALL_OUT_STATE=? where "+
        "COMPANY_CODE_SYS01=? and REQUEST_YEAR=? and PROGRESSIVE=?"
      );
      reqVO.setCallOutStateSCH03(ApplicationConsts.INVOICED);
      pstmt.setString(1,vo.getDocTypeDOC01());
      pstmt.setBigDecimal(2,vo.getDocYearDOC01());
      pstmt.setBigDecimal(3,vo.getDocNumberDOC01());
      pstmt.setString(4,reqVO.getCallOutStateSCH03());
      pstmt.setString(5,vo.getCompanyCodeSys01DOC01());
      pstmt.setBigDecimal(6,reqVO.getRequestYearSCH03());
      pstmt.setBigDecimal(7,reqVO.getProgressiveSCH03());
      pstmt.execute();
      pstmt.close();



      // update sheduled activities doc state...
      pstmt = conn.prepareStatement(
        "update SCH06_SCHEDULED_ACTIVITIES set ACTIVITY_STATE=? where "+
        "COMPANY_CODE_SYS01=? and PROGRESSIVE=?"
      );
      actVO.setActivityStateSCH06(ApplicationConsts.INVOICED);
      pstmt.setString(1,actVO.getActivityStateSCH06());
      pstmt.setString(2,actVO.getCompanyCodeSys01SCH06());
      pstmt.setBigDecimal(3,actVO.getProgressiveSCH06());
      pstmt.execute();
      pstmt.close();



      reqVO.setDocTypeDoc01SCH03(vo.getDocTypeDOC01());
      reqVO.setDocYearDoc01SCH03(vo.getDocYearDOC01());
      reqVO.setDocNumberDoc01SCH03(vo.getDocNumberDOC01());
      reqVO.setScheduledActivityVO(actVO);

      return new VOResponse(reqVO);
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while creating an invoice from a closed call-out request",ex);
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
        saleDoc.setConn(null);
        insItemRow.setConn(null);
        insActRow.setConn(null);
        loadItemsRow.setConn(null);
        price.setConn(null);
        totals.setConn(null);
      } catch (Exception ex) {}
    }
  }



}

