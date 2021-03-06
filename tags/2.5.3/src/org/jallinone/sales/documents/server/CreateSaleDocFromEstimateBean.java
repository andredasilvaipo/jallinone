package org.jallinone.sales.documents.server;

import org.openswing.swing.server.*;
import java.io.*;
import java.util.*;

import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.*;
import org.jallinone.system.server.*;
import java.math.*;
import org.jallinone.system.progressives.server.*;
import org.jallinone.system.translations.server.*;
import org.jallinone.commons.server.*;
import org.jallinone.system.java.*;
import org.jallinone.commons.java.*;
import org.openswing.swing.message.send.java.*;
import org.jallinone.sales.documents.java.*;
import org.jallinone.sales.documents.server.*;
import org.jallinone.sales.documents.headercharges.server.*;
import org.jallinone.sales.documents.headerdiscounts.server.*;
import org.jallinone.sales.documents.itemdiscounts.server.*;
import org.jallinone.sales.documents.activities.server.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.*;
import org.openswing.swing.internationalization.server.*;
import org.openswing.swing.internationalization.java.*;
import org.jallinone.sales.customers.server.*;
import org.jallinone.sales.customers.java.*;
import org.jallinone.sales.documents.itemdiscounts.java.*;
import org.jallinone.sales.documents.headercharges.java.*;
import org.jallinone.sales.documents.activities.java.*;
import org.jallinone.sales.documents.headerdiscounts.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to create a sale document (order or contract), based on the specified sale estimate doc.</p>
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
public class CreateSaleDocFromEstimateBean  implements CreateSaleDocFromEstimate {


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



  private LoadSaleDocRowsBean rowsAction;

  public void setRowsAction(LoadSaleDocRowsBean rowsAction) {
    this.rowsAction = rowsAction;
  }

  private SaleDocsBean docAction;

  public void setDocAction(SaleDocsBean docAction) {
    this.docAction = docAction;
  }

  private LoadSaleDocBean loadSaleDocBean;

  public void setLoadSaleDocBean(LoadSaleDocBean loadSaleDocBean) {
	  this.loadSaleDocBean = loadSaleDocBean;
  }

  private CustomersBean custAction;

  public void setCustAction(CustomersBean custAction) {
    this.custAction = custAction;
  }

  private SaleDocChargesBean chargesAction;

  public void setChargesAction(SaleDocChargesBean chargesAction) {
    this.chargesAction = chargesAction;
  }

  private SaleDocActivitiesBean actAction;

  public void setActAction(SaleDocActivitiesBean actAction) {
    this.actAction = actAction;
  }

  private SaleDocDiscountsBean discAction;

  public void setDiscAction(SaleDocDiscountsBean discAction) {
    this.discAction = discAction;
  }

  private SaleDocRowDiscountsBean itemDiscAction;

  public void setItemDiscAction(SaleDocRowDiscountsBean itemDiscAction) {
    this.itemDiscAction = itemDiscAction;
  }


  private UpdateTaxableIncomesBean totals;

  public void setTotals(UpdateTaxableIncomesBean totals) {
    this.totals = totals;
  }

  private InsertSaleItemBean bean;

  public void setBean(InsertSaleItemBean bean) {
	  this.bean = bean;
  }

  private InsertSaleDocRowDiscountBean insBean;

  public void setInsBean(InsertSaleDocRowDiscountBean insBean) {
	  this.insBean = insBean;
  }

  public CreateSaleDocFromEstimateBean() {}



  /**
   * Business logic to execute.
   */
  public DetailSaleDocVO createSaleDocFromEstimate(
      HashMap variant1Descriptions,
      HashMap variant2Descriptions,
      HashMap variant3Descriptions,
      HashMap variant4Descriptions,
      HashMap variant5Descriptions,
      SaleDocPK pk, String t1, String serverLanguageId, String username,
      ArrayList companiesList) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      rowsAction.setConn(conn); // use same transaction...
      docAction.setConn(conn); // use same transaction...
      custAction.setConn(conn); // use same transaction...
      chargesAction.setConn(conn); // use same transaction...
      actAction.setConn(conn); // use same transaction...
      discAction.setConn(conn); // use same transaction...
      itemDiscAction.setConn(conn); // use same transaction...
      totals.setConn(conn); // use same transaction...
      bean.setConn(conn);
      loadSaleDocBean.setConn(conn);
      insBean.setConn(conn);

      // retrieve document header...
      DetailSaleDocVO docVO = loadSaleDocBean.loadSaleDoc(pk,serverLanguageId,username,new ArrayList());

      // retrieve document item rows...
      GridParams gridParams = new GridParams();
      gridParams.getOtherGridParams().put(ApplicationConsts.SALE_DOC_PK,pk);
      Response res = rowsAction.loadSaleDocRows(variant1Descriptions,variant2Descriptions,variant3Descriptions,variant4Descriptions,variant5Descriptions,gridParams,serverLanguageId,username);
      if (res.isError()) {
        throw new Exception(res.getErrorMessage());
      }
      java.util.List rows = ((VOListResponse)res).getRows();

      HashMap map = new HashMap();
      map.put(ApplicationConsts.COMPANY_CODE_SYS01,docVO.getCompanyCodeSys01DOC01());
      LookupValidationParams pars = new LookupValidationParams(docVO.getCustomerCodeSAL07(),map);
      res = custAction.validateCustomerCode(pars,serverLanguageId,username,companiesList);
      if (res.isError()) {
        throw new Exception(res.getErrorMessage());
      }
      GridCustomerVO custVO = (GridCustomerVO)((VOListResponse)res).getRows().get(0);

      // create header...
      if (custVO.getSubjectTypeREG04().equals(ApplicationConsts.SUBJECT_ORGANIZATION_CUSTOMER))
        docVO.setDocTypeDOC01(ApplicationConsts.SALE_ORDER_DOC_TYPE);
      else if (custVO.getSubjectTypeREG04().equals(ApplicationConsts.SUBJECT_PEOPLE_CUSTOMER))
        docVO.setDocTypeDOC01(ApplicationConsts.SALE_CONTRACT_DOC_TYPE);
      docVO.setDocRefNumberDOC01(t1+docVO.getDocSequenceDOC01()+"/"+docVO.getDocYearDOC01());
      docVO.setDocSequenceDOC01(null);
      res = docAction.insertSaleDoc(docVO,serverLanguageId,username,docVO.getCompanyCodeSys01DOC01(),new ArrayList());
      if (res.isError()) {
        throw new Exception(res.getErrorMessage());
      }

      // create rows..
      GridSaleDocRowVO gridRowVO = null;
      DetailSaleDocRowVO rowVO = null;
      java.util.List discRows = null;
      SaleDocRowPK docRowPK = null;
      SaleItemDiscountVO itemDiscVO = null;
      gridParams = new GridParams();
      for(int i=0;i<rows.size();i++) {
        gridRowVO = (GridSaleDocRowVO)rows.get(i);

        // retrieve row detail...
        docRowPK = new SaleDocRowPK(
            gridRowVO.getCompanyCodeSys01DOC02(),
            gridRowVO.getDocTypeDOC02(),
            gridRowVO.getDocYearDOC02(),
            gridRowVO.getDocNumberDOC02(),
            gridRowVO.getItemCodeItm01DOC02(),
            gridRowVO.getVariantTypeItm06DOC02(),
            gridRowVO.getVariantCodeItm11DOC02(),
            gridRowVO.getVariantTypeItm07DOC02(),
            gridRowVO.getVariantCodeItm12DOC02(),
            gridRowVO.getVariantTypeItm08DOC02(),
            gridRowVO.getVariantCodeItm13DOC02(),
            gridRowVO.getVariantTypeItm09DOC02(),
            gridRowVO.getVariantCodeItm14DOC02(),
            gridRowVO.getVariantTypeItm10DOC02(),
            gridRowVO.getVariantCodeItm15DOC02()

        );
        res = bean.loadSaleDocRow(variant1Descriptions,variant2Descriptions,variant3Descriptions,variant4Descriptions,variant5Descriptions,docRowPK,serverLanguageId,username);
        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }
        rowVO = (DetailSaleDocRowVO)((VOResponse)res).getVo();
        rowVO.setDocTypeDOC02(docVO.getDocTypeDOC01());
        rowVO.setDocNumberDOC02(docVO.getDocNumberDOC01());
        res = bean.insertSaleItem(rowVO,serverLanguageId,username);
        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }


        // create item discounts...
        gridParams.getOtherGridParams().put(ApplicationConsts.SALE_DOC_ROW_PK,docRowPK);
        res = itemDiscAction.loadSaleDocRowDiscounts(gridParams,serverLanguageId,username);
        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }
        discRows = ((VOListResponse)res).getRows();
        for(int j=0;j<discRows.size();j++) {
          itemDiscVO = (SaleItemDiscountVO)discRows.get(j);
          itemDiscVO.setDocTypeDOC04(docVO.getDocTypeDOC01());
          itemDiscVO.setDocNumberDOC04(docVO.getDocNumberDOC01());
          res = insBean.insertSaleDocRowDiscount(itemDiscVO,serverLanguageId,username);
          if (res.isError()) {
            throw new Exception(res.getErrorMessage());
          }
        }
      }

      // create charges...
      gridParams = new GridParams();
      gridParams.getOtherGridParams().put(ApplicationConsts.SALE_DOC_PK,pk);
      res = rowsAction.loadSaleDocCharges(gridParams,serverLanguageId,username);
      SaleDocChargeVO chargeVO = null;
      if (res.isError()) {
        throw new Exception(res.getErrorMessage());
      }
      rows = ((VOListResponse)res).getRows();
      for(int i=0;i<rows.size();i++) {
        chargeVO = (SaleDocChargeVO)rows.get(i);
        chargeVO.setDocTypeDOC03(docVO.getDocTypeDOC01());
        chargeVO.setDocNumberDOC03(docVO.getDocNumberDOC01());
        res = chargesAction.insertSaleDocCharge(chargeVO,username);
        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }
      }

      // create activities...
      res = rowsAction.loadSaleDocActivities(gridParams,serverLanguageId,username);
      if (res.isError()) {
        throw new Exception(res.getErrorMessage());
      }
      SaleDocActivityVO actVO = null;
      rows = ((VOListResponse)res).getRows();
      for(int i=0;i<rows.size();i++) {
        actVO = (SaleDocActivityVO)rows.get(i);
        actVO.setDocTypeDOC13(docVO.getDocTypeDOC01());
        actVO.setDocNumberDOC13(docVO.getDocNumberDOC01());
        res = actAction.insertSaleActivity(actVO,username);
        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }
      }

      // create header discounts...
      res = rowsAction.loadSaleDocDiscounts(gridParams,serverLanguageId,username);
      if (res.isError()) {
        throw new Exception(res.getErrorMessage());
      }
      SaleDocDiscountVO discVO = null;
      rows = ((VOListResponse)res).getRows();
      for(int i=0;i<rows.size();i++) {
        discVO = (SaleDocDiscountVO)rows.get(i);
        discVO.setDocTypeDOC05(docVO.getDocTypeDOC01());
        discVO.setDocNumberDOC05(docVO.getDocNumberDOC01());
        res = discAction.insertSaleDocDiscount(discVO,username);
        if (res.isError()) {
          throw new Exception(res.getErrorMessage());
        }
      }



      // recalculate all taxable incomes, vats, totals...
      res = totals.updateTaxableIncomes(
          variant1Descriptions,
          variant2Descriptions,
          variant3Descriptions,
          variant4Descriptions,
          variant5Descriptions,
          pk, serverLanguageId, username);
      if (res.isError()) {
        throw new Exception(res.getErrorMessage());
      }

      return loadSaleDocBean.loadSaleDoc(pk,serverLanguageId,username,new ArrayList());
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while creating a sale document from a sale estimate document",ex);
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
        rowsAction.setConn(null);
        docAction.setConn(null);
        custAction.setConn(null);
        chargesAction.setConn(null);
        actAction.setConn(null);
        discAction.setConn(null);
        itemDiscAction.setConn(null);
        totals.setConn(null);
        bean.setConn(null);
        loadSaleDocBean.setConn(null);
        insBean.setConn(null);
      } catch (Exception ex) {}
    }
  }



}

