package org.jallinone.purchases.documents.server;

import java.sql.*;
import java.util.*;

import org.jallinone.events.server.*;
import org.jallinone.purchases.documents.java.*;
import org.jallinone.system.server.*;
import org.openswing.swing.logger.server.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.server.*;
import java.math.*;
import org.jallinone.commons.java.*;
import org.jallinone.registers.currency.java.*;
import org.jallinone.warehouse.java.*;
import org.openswing.swing.internationalization.server.*;
import org.openswing.swing.internationalization.java.*;
import org.jallinone.purchases.documents.java.*;
import org.jallinone.items.java.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to create a set of purchase orders, starting from items specified in the reorder frame.</p>
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
public class CreatePurchaseOrdersBean  implements CreatePurchaseOrders {


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



  private InsertPurchaseDocBean bean;

  public void setBean(InsertPurchaseDocBean bean) {
    this.bean = bean;
  }

  private InsertPurchaseDocRowBean rowbean;

  public void setRowbean(InsertPurchaseDocRowBean rowbean) {
    this.rowbean = rowbean;
  }



  public CreatePurchaseOrdersBean() {
  }



  /**
   * Business logic to execute.
   */
  public VOListResponse createPurchaseOrders(String t1,CurrencyVO currVO,WarehouseVO warehouseVO,ItemTypeVO itemTypeVO,ArrayList vos,String serverLanguageId,String username) throws Throwable {
    PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      bean.setConn(conn); // use same transaction...
      rowbean.setConn(conn); // use same transaction...


      // sort vos per supplier...
      ReorderFromMinStockVO[] list = (ReorderFromMinStockVO[])vos.toArray(new ReorderFromMinStockVO[vos.size()]);
      Arrays.sort(list,new Comparator() {

        public boolean equals(Object obj) {
          if (obj==null || !(obj instanceof ReorderFromMinStockVO))
            return false;
          return this.equals(obj);
        }

        public int compare(Object o1, Object o2) {
          ReorderFromMinStockVO vo1 = (ReorderFromMinStockVO)o1;
          ReorderFromMinStockVO vo2 = (ReorderFromMinStockVO)o2;
          return vo1.getSupplierCode().compareTo(vo1.getSupplierCode());
        }
      });


      // process vos and create an order for each distinct supplier...
      String oldSupplierCode = null;
      String oldPricelistCode = null;
      ArrayList items = new ArrayList();
      ArrayList docNumbers = new ArrayList();
      for(int i=0;i<list.length;i++) {
        if (oldSupplierCode==null) {
          items.add(list[i]);
          oldSupplierCode = list[i].getSupplierCode();
          oldPricelistCode = list[i].getPricelistCodePUR03();
        }
        else if (oldSupplierCode.equals(list[i].getSupplierCode()) &&
                 oldPricelistCode.equals(list[i].getPricelistCodePUR03())) {
          items.add(list[i]);
        }
        else {
         docNumbers.add( createPurchaseOrder(t1,currVO,warehouseVO,itemTypeVO,items,serverLanguageId,username) );
         items.clear();
         items.add(list[i]);
         oldSupplierCode = list[i].getSupplierCode();
         oldPricelistCode = list[i].getPricelistCodePUR03();
        }
      }
      if (items.size()>0)
        docNumbers.add( createPurchaseOrder(t1,currVO,warehouseVO,itemTypeVO,items,serverLanguageId,username) );


     return new VOListResponse(docNumbers,false,docNumbers.size());
    }
    catch (Throwable ex) {
        try {
      	  if (this.conn==null && conn!=null)
      		  // rollback only local connection
      		  conn.rollback();
        }
        catch (Exception ex3) {
        }
        Logger.error(username,this.getClass().getName(),"executeCommand","Error while creating purchase orders",ex);
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
   * Create a purchase order having the specified items.
   * @return DOC_NUMBER of the order just created
   */
  private BigDecimal createPurchaseOrder(String t1,CurrencyVO currVO,WarehouseVO warehouseVO,ItemTypeVO itemTypeVO,ArrayList items,String serverLanguageId,String username) throws Throwable {
    Calendar cal = Calendar.getInstance();

    // create doc header...
    ReorderFromMinStockVO itemVO = (ReorderFromMinStockVO)items.get(0);

    DetailPurchaseDocVO vo = new DetailPurchaseDocVO();
    vo.setCompanyCodeSys01DOC06(itemVO.getCompanyCodeSys01());
    vo.setDocTypeDOC06(ApplicationConsts.PURCHASE_ORDER_DOC_TYPE);
    vo.setDocYearDOC06(new BigDecimal(cal.get(cal.YEAR)));
    vo.setDocDateDOC06(new java.sql.Date(cal.getTimeInMillis()));
    vo.setCurrencyCodeReg03DOC06(currVO.getCurrencyCodeREG03());
    vo.setCurrencySymbolREG03(currVO.getCurrencySymbolREG03());
    vo.setDecimalsREG03(currVO.getDecimalsREG03());
    vo.setDecimalSymbolREG03(currVO.getDecimalSymbolREG03());
    vo.setDescriptionWar01DOC06(warehouseVO.getDescriptionWAR01());
    vo.setName_1REG04(itemVO.getName_1REG04());
    vo.setNoteDOC06(t1);
    vo.setPaymentCodeReg10DOC06(itemVO.getPaymentCodeREG10());
    vo.setPaymentDescriptionDOC06(null);
    vo.setPricelistCodePur03DOC06(itemVO.getPricelistCodePUR03());
    vo.setPricelistDescriptionDOC06(itemVO.getPricelistDescription());
    vo.setProgressiveReg04DOC06(itemVO.getProgressiveREG04());
    vo.setSupplierCodePUR01(itemVO.getSupplierCode());
    vo.setTaxableIncomeDOC06(null);
    vo.setThousandSymbolREG03(currVO.getThousandSymbolREG03());
    vo.setTotalDOC06(null);
    vo.setTotalVatDOC06(null);
    vo.setDocStateDOC06(ApplicationConsts.OPENED);
    vo.setWarehouseCodeWar01DOC06(warehouseVO.getWarehouseCodeWAR01());

    Response res = bean.insertPurchaseDoc(vo,itemVO.getCompanyCodeSys01(),serverLanguageId,username,new ArrayList());
    if (res.isError())
      throw new Exception(res.getErrorMessage());
    vo = (DetailPurchaseDocVO)((VOResponse)res).getVo();

    // create doc rows...
    DetailPurchaseDocRowVO rowVO = null;
    for(int i=0;i<items.size();i++) {
      itemVO = (ReorderFromMinStockVO)items.get(i);

      rowVO = new DetailPurchaseDocRowVO();
      rowVO.setCompanyCodeSys01DOC07(itemVO.getCompanyCodeSys01());
      rowVO.setDocTypeDOC07(ApplicationConsts.PURCHASE_ORDER_DOC_TYPE);
      rowVO.setDocYearDOC07(new BigDecimal(cal.get(cal.YEAR)));
      rowVO.setDocNumberDOC07(vo.getDocNumberDOC06());
      rowVO.setCurrencyCodeReg03DOC06(currVO.getCurrencyCodeREG03());
      rowVO.setDeliveryDateDOC07(new java.sql.Date(cal.getTimeInMillis()));
      rowVO.setDescriptionSYS10(itemVO.getItemDescription());
      rowVO.setItemCodeItm01DOC07(itemVO.getItemCode());
      rowVO.setSupplierItemCodePur02DOC07(itemVO.getSupplierItemCode());
      rowVO.setStartDatePur04DOC07(itemVO.getStartDate());
      rowVO.setEndDatePur04DOC07(itemVO.getEndDate());
      rowVO.setQtyDOC07(itemVO.getQty());
      rowVO.setProgressiveHie02DOC07(itemTypeVO.getProgressiveHie02ITM02());
      rowVO.setVariantTypeItm06DOC07(itemVO.getVariantTypeITM06());
      rowVO.setVariantTypeItm07DOC07(itemVO.getVariantTypeITM07());
      rowVO.setVariantTypeItm08DOC07(itemVO.getVariantTypeITM08());
      rowVO.setVariantTypeItm09DOC07(itemVO.getVariantTypeITM09());
      rowVO.setVariantTypeItm10DOC07(itemVO.getVariantTypeITM10());
      rowVO.setVariantCodeItm11DOC07(itemVO.getVariantCodeITM11());
      rowVO.setVariantCodeItm12DOC07(itemVO.getVariantCodeITM12());
      rowVO.setVariantCodeItm13DOC07(itemVO.getVariantCodeITM13());
      rowVO.setVariantCodeItm14DOC07(itemVO.getVariantCodeITM14());
      rowVO.setVariantCodeItm15DOC07(itemVO.getVariantCodeITM15());
      rowVO.setUmCodePur02DOC07(itemVO.getUmCodeREG02());
      rowVO.setDecimalsReg02DOC07(new BigDecimal(0));
      rowVO.setMinPurchaseQtyPur02DOC07(itemVO.getMinPurchaseQtyPUR02());
      rowVO.setMultipleQtyPur02DOC07(itemVO.getMultipleQtyPUR02());
      rowVO.setValueReg01DOC07(itemVO.getVatValue());
      rowVO.setVatCodeItm01DOC07(itemVO.getVatCode());
      rowVO.setVatDescriptionDOC07(itemVO.getVatDescription());
      rowVO.setDeductibleReg01DOC07(itemVO.getDeductible());
      rowVO.setValuePur04DOC07(itemVO.getUnitPrice());
      rowVO.setTaxableIncomeDOC07(rowVO.getValuePur04DOC07().multiply(itemVO.getQty()));
      rowVO.setVatValueDOC07(
        rowVO.getTaxableIncomeDOC07().multiply(
        rowVO.getValueReg01DOC07()).divide(
        new BigDecimal(100),currVO.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP));
      rowVO.setValueDOC07(rowVO.getTaxableIncomeDOC07().add(rowVO.getVatValueDOC07()));

      res = rowbean.insertPurchaseDocRow(rowVO,serverLanguageId,username);
      if (res.isError())
        throw new Exception(res.getErrorMessage());


    
      try {
        bean.setConn(null);
        rowbean.setConn(null);
      } catch (Exception ex) {}
    }

    return vo.getDocNumberDOC06();
  }



}

