package org.jallinone.purchases.documents.server;

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
import org.jallinone.purchases.documents.java.*;
import org.jallinone.purchases.documents.server.*;
import org.openswing.swing.message.receive.java.*;
import org.openswing.swing.message.send.java.*;
import org.openswing.swing.internationalization.server.*;
import org.openswing.swing.internationalization.java.*;
import org.jallinone.purchases.suppliers.server.*;
import org.jallinone.purchases.suppliers.java.*;
import org.jallinone.events.server.*;
import org.jallinone.events.server.*;


import javax.sql.DataSource;

/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * * <p>Description: Bean used to create a purchase invoice from the specified purchase order document.</p>
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
public class CreateInvoiceFromPurchaseDocBean  implements CreateInvoiceFromPurchaseDoc {


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



  private LoadPurchaseDocRowsBean rowsAction;

  public void setRowsAction(LoadPurchaseDocRowsBean rowsAction) {
    this.rowsAction = rowsAction;
  }

  private LoadPurchaseDocBean docAction;

  public void setDocAction(LoadPurchaseDocBean docAction) {
    this.docAction = docAction;
  }

  private LoadPurchaseDocRowBean rowAction;

  public void setRowAction(LoadPurchaseDocRowBean rowAction) {
    this.rowAction = rowAction;
  }

  private InsertPurchaseDocBean insDocAction;

  public void setInsDocAction(InsertPurchaseDocBean insDocAction) {
    this.insDocAction = insDocAction;
  }

  private InsertPurchaseDocRowBean insRowAction;

  public void setInsRowAction(InsertPurchaseDocRowBean insRowAction) {
    this.insRowAction = insRowAction;
  }

  private SuppliersBean custAction;

  public void setCustAction(SuppliersBean custAction) {
    this.custAction = custAction;
  }



  public CreateInvoiceFromPurchaseDocBean() {}



  /**
   * Business logic to execute.
   */
  public VOResponse createInvoiceFromPurchaseDoc(
		HashMap variant1Descriptions,
		HashMap variant2Descriptions,
		HashMap variant3Descriptions,
		HashMap variant4Descriptions,
		HashMap variant5Descriptions,
		DetailPurchaseDocVO docVO, String companyCode, String serverLanguageId,
		String username) throws Throwable {
		PreparedStatement pstmt = null;
    Connection conn = null;
    try {
      if (this.conn==null) conn = getConn(); else conn = this.conn;
      rowsAction.setConn(conn); // use same transaction...
      docAction.setConn(conn); // use same transaction...
      rowAction.setConn(conn); // use same transaction...
      insDocAction.setConn(conn); // use same transaction...
      insRowAction.setConn(conn); // use same transaction...
      custAction.setConn(conn); // use same transaction...


      // insert header...
      docVO.setDocStateDOC06(ApplicationConsts.HEADER_BLOCKED);
      Response res = insDocAction.insertPurchaseDoc(docVO,companyCode,serverLanguageId,username,new ArrayList());
      if (res.isError())
        throw new Exception(res.getErrorMessage());

      PurchaseDocPK refPK = new PurchaseDocPK(
        docVO.getCompanyCodeSys01Doc06DOC06(),
        docVO.getDocTypeDoc06DOC06(),
        docVO.getDocYearDoc06DOC06(),
        docVO.getDocNumberDoc06DOC06()
      );

      // retrieve ref. document item rows...
      GridParams gridParams = new GridParams();
      gridParams.getOtherGridParams().put(ApplicationConsts.PURCHASE_DOC_PK,refPK);
      res = rowsAction.loadPurchaseDocRows(variant1Descriptions,variant2Descriptions,variant3Descriptions,variant4Descriptions,variant5Descriptions,gridParams,serverLanguageId,username);
      if (res.isError())
        throw new Exception(res.getErrorMessage());
      java.util.List rows = ((VOListResponse)res).getRows();

      // create rows..
      GridPurchaseDocRowVO gridRowVO = null;
      DetailPurchaseDocRowVO rowVO = null;
      ArrayList discRows = null;
      PurchaseDocRowPK docRowPK = null;
      gridParams = new GridParams();
      for(int i=0;i<rows.size();i++) {
        gridRowVO = (GridPurchaseDocRowVO)rows.get(i);

        // retrieve row detail...
        docRowPK = new PurchaseDocRowPK(
            gridRowVO.getCompanyCodeSys01DOC07(),
            gridRowVO.getDocTypeDOC07(),
            gridRowVO.getDocYearDOC07(),
            gridRowVO.getDocNumberDOC07(),
            gridRowVO.getItemCodeItm01DOC07(),
            gridRowVO.getVariantTypeItm06DOC07(),
            gridRowVO.getVariantCodeItm11DOC07(),
            gridRowVO.getVariantTypeItm07DOC07(),
            gridRowVO.getVariantCodeItm12DOC07(),
            gridRowVO.getVariantTypeItm08DOC07(),
            gridRowVO.getVariantCodeItm13DOC07(),
            gridRowVO.getVariantTypeItm09DOC07(),
            gridRowVO.getVariantCodeItm14DOC07(),
            gridRowVO.getVariantTypeItm10DOC07(),
            gridRowVO.getVariantCodeItm15DOC07()

        );
        rowVO = rowAction.loadPurchaseDocRow(variant1Descriptions,variant2Descriptions,variant3Descriptions,variant4Descriptions,variant5Descriptions,docRowPK,serverLanguageId,username);
        rowVO.setDocTypeDOC07(docVO.getDocTypeDOC06());
        rowVO.setDocNumberDOC07(docVO.getDocNumberDOC06());
        if (rowVO.getInvoiceQtyDOC07().doubleValue()<rowVO.getQtyDOC07().doubleValue() &&
            rowVO.getQtyDOC07().doubleValue()==rowVO.getInQtyDOC07().doubleValue()) {
          rowVO.setQtyDOC07(rowVO.getQtyDOC07().subtract(rowVO.getInvoiceQtyDOC07().setScale(rowVO.getDecimalsReg02DOC07().intValue(),BigDecimal.ROUND_HALF_UP)));
          rowVO.setTaxableIncomeDOC07(rowVO.getQtyDOC07().multiply(rowVO.getValuePur04DOC07()).setScale(docVO.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP));

          // apply percentage discount...
          if (rowVO.getDiscountPercDOC07()!=null) {
            double taxtable = rowVO.getTaxableIncomeDOC07().doubleValue()-rowVO.getTaxableIncomeDOC07().doubleValue()*rowVO.getDiscountPercDOC07().doubleValue()/100d;
            rowVO.setTaxableIncomeDOC07(new BigDecimal(taxtable).setScale(docVO.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP));
          }

          // apply value discount...
          if (rowVO.getDiscountValueDOC07()!=null) {
            rowVO.setTaxableIncomeDOC07(rowVO.getTaxableIncomeDOC07().subtract(rowVO.getDiscountValueDOC07()).setScale(docVO.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP));
          }

          // calculate row vat...
          double vatPerc = rowVO.getValueReg01DOC07().doubleValue()*(1d-rowVO.getDeductibleReg01DOC07().doubleValue()/100d)/100;
          rowVO.setVatValueDOC07(rowVO.getTaxableIncomeDOC07().multiply(new BigDecimal(vatPerc)).setScale(docVO.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP));

          // calculate row total...
          rowVO.setValueDOC07(rowVO.getTaxableIncomeDOC07().add(rowVO.getVatValueDOC07()));

          res = insRowAction.insertPurchaseDocRow(rowVO,serverLanguageId,username);
          if (res.isError())
            throw new Exception(res.getErrorMessage());

        }
      }

      // reload doc header with updated totals...
      PurchaseDocPK pk = new PurchaseDocPK(
        docVO.getCompanyCodeSys01DOC06(),
        docVO.getDocTypeDOC06(),
        docVO.getDocYearDOC06(),
        docVO.getDocNumberDOC06()
      );
      return new VOResponse(docAction.loadPurchaseDoc(pk,serverLanguageId,username,new ArrayList()));
    }
    catch (Throwable ex) {
      Logger.error(username,this.getClass().getName(),"executeCommand","Error while creating a purchase invoice from a purchase order document",ex);
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
        rowAction.setConn(null);
        insDocAction.setConn(null);
        insRowAction.setConn(null);
        custAction.setConn(null);
      } catch (Exception ex) {}
    }
  }



}

