package org.jallinone.sales.documents.server;

import org.openswing.swing.server.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.openswing.swing.message.receive.java.*;
import java.sql.*;
import org.openswing.swing.logger.server.Logger;
import org.jallinone.system.server.JAIOUserSessionParameters;
import java.math.BigDecimal;
import org.jallinone.system.progressives.server.ProgressiveUtils;
import org.jallinone.system.translations.server.TranslationUtils;
import org.jallinone.commons.server.CustomizeQueryUtil;
import org.jallinone.system.java.ApplicationParametersVO;
import org.jallinone.commons.java.ApplicationConsts;
import org.openswing.swing.message.send.java.LookupValidationParams;
import org.jallinone.sales.documents.java.*;
import org.jallinone.sales.documents.server.*;
import org.jallinone.sales.documents.headercharges.server.*;
import org.jallinone.sales.documents.headerdiscounts.server.*;
import org.jallinone.sales.documents.itemdiscounts.server.*;
import org.jallinone.sales.documents.activities.server.*;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.internationalization.server.ServerResourcesFactory;
import org.openswing.swing.internationalization.java.Resources;
import org.jallinone.sales.customers.server.ValidateCustomerCodeAction;
import org.jallinone.sales.customers.java.GridCustomerVO;
import org.jallinone.sales.documents.itemdiscounts.java.SaleItemDiscountVO;
import org.jallinone.sales.documents.headercharges.java.SaleDocChargeVO;
import org.jallinone.sales.documents.activities.java.SaleDocActivityVO;
import org.jallinone.sales.documents.headerdiscounts.java.SaleDocDiscountVO;
import org.jallinone.sales.documents.invoices.java.SaleInvoiceFromDelivNotesVO;
import org.jallinone.sales.documents.invoices.java.OutDeliveryNotesVO;
import org.jallinone.sales.documents.activities.server.InsertSaleDocActivityBean;
import org.jallinone.sales.documents.headerdiscounts.server.InsertSaleDocDiscountsAction;
import org.jallinone.sales.documents.itemdiscounts.server.InsertSaleDocRowDiscountsAction;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to create a sale invoice from a set of out delivery notes.
 * Item rows and discounts and charges and activities are added to the invoice only if they are not yet added in previous closed invoices.</p>
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
public class CreateInvoiceFromOutDelivNotesAction implements Action {

  LoadSaleDocRowsAction rowsAction = new LoadSaleDocRowsAction();
  LoadSaleDocBean docAction = new LoadSaleDocBean();
  LoadSaleDocRowAction rowAction = new LoadSaleDocRowAction();
  InsertSaleDocBean insDocBean = new InsertSaleDocBean();
  InsertSaleDocRowBean insRowBean = new InsertSaleDocRowBean();
  ValidateCustomerCodeAction custAction = new ValidateCustomerCodeAction();
  LoadSaleDocChargesAction chargesAction = new LoadSaleDocChargesAction();
  LoadSaleDocActivitiesAction actAction = new LoadSaleDocActivitiesAction();
  LoadSaleDocDiscountsAction discAction = new LoadSaleDocDiscountsAction();
  LoadSaleDocRowDiscountsAction itemDiscAction = new LoadSaleDocRowDiscountsAction();
  InsertSaleDocChargeBean insChargeBean = new InsertSaleDocChargeBean();
  InsertSaleDocActivityBean insActBean = new InsertSaleDocActivityBean();
  InsertSaleDocDiscountBean insDiscBean = new InsertSaleDocDiscountBean();
  InsertSaleDocRowDiscountBean insItemDiscBean = new InsertSaleDocRowDiscountBean();
  UpdateTaxableIncomesBean totals = new UpdateTaxableIncomesBean();


  public CreateInvoiceFromOutDelivNotesAction() {}

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "createInvoiceFromOutDelivNotes";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();
    Connection conn = null;
    PreparedStatement pstmt = null;
    try {
      conn = ConnectionManager.getConnection(context);

      // fires the GenericEvent.CONNECTION_CREATED event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        getRequestName(),
        GenericEvent.CONNECTION_CREATED,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        inputPar,
        null
      ));
      SaleInvoiceFromDelivNotesVO invoiceVO = (SaleInvoiceFromDelivNotesVO)inputPar;
      DetailSaleDocVO docVO = invoiceVO.getDocVO();
      ArrayList delivNotes = invoiceVO.getSelectedDeliveryNotes();

      // retrieve internationalization settings (Resources object)...
      ServerResourcesFactory factory = (ServerResourcesFactory)context.getAttribute(Controller.RESOURCES_FACTORY);
      Resources resources = factory.getResources(userSessionPars.getLanguageId());

      // insert header invoice...
      docVO.setDocStateDOC01(ApplicationConsts.HEADER_BLOCKED);
      Response res = insDocBean.insertSaleDoc(conn,docVO,userSessionPars,request,response,userSession,context);
      if (res.isError()) {
        conn.rollback();
        return res;
      }

      SaleDocPK refPK = new SaleDocPK(
        docVO.getCompanyCodeSys01Doc01DOC01(),
        docVO.getDocTypeDoc01DOC01(),
        docVO.getDocYearDoc01DOC01(),
        docVO.getDocNumberDoc01DOC01()
      );

      // retrieve the list of items referred by the selected delivery notes...
      Hashtable selectedItems = new Hashtable(); // collection of pairs <itemcode,qty>
      BigDecimal qty = null;
      pstmt = conn.prepareStatement(
        "select DOC10_OUT_DELIVERY_NOTE_ITEMS.QTY,DOC10_OUT_DELIVERY_NOTE_ITEMS.ITEM_CODE_ITM01 from DOC10_OUT_DELIVERY_NOTE_ITEMS where "+
        "DOC10_OUT_DELIVERY_NOTE_ITEMS.COMPANY_CODE_SYS01=? and "+
        "DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_TYPE=? and "+
        "DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_YEAR=? and "+
        "DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_NUMBER=? and "+
        "DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_TYPE_DOC01=? and "+
        "DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_YEAR_DOC01=? and "+
        "DOC10_OUT_DELIVERY_NOTE_ITEMS.DOC_NUMBER_DOC01=? "
      );
      ResultSet rset = null;
      OutDeliveryNotesVO delivVO = null;
      for(int i=0;i<delivNotes.size();i++) {
        delivVO = (OutDeliveryNotesVO)delivNotes.get(i);
        pstmt.setString(1,delivVO.getCompanyCodeSys01DOC08());
        pstmt.setString(2,delivVO.getDocTypeDOC08());
        pstmt.setBigDecimal(3,delivVO.getDocYearDOC08());
        pstmt.setBigDecimal(4,delivVO.getDocNumberDOC08());
        pstmt.setString(5,refPK.getDocTypeDOC01());
        pstmt.setBigDecimal(6,refPK.getDocYearDOC01());
        pstmt.setBigDecimal(7,refPK.getDocNumberDOC01());
        rset = pstmt.executeQuery();
        while(rset.next()) {
          qty = rset.getBigDecimal(1);
          if (selectedItems.contains(rset.getString(2)))
            qty = qty.add( (BigDecimal)selectedItems.get(rset.getString(2)) );
          selectedItems.put(rset.getString(2),qty);
        }
        rset.close();
      }



      // retrieve ref. document item rows...
      GridParams gridParams = new GridParams();
      gridParams.getOtherGridParams().put(ApplicationConsts.SALE_DOC_PK,refPK);
      res = rowsAction.executeCommand(gridParams,userSessionPars,request,response,userSession,context);
      if (res.isError()) {
        conn.rollback();
        return res;
      }
      ArrayList rows = ((VOListResponse)res).getRows();



      // create invoice items rows..
      GridSaleDocRowVO gridRowVO = null;
      DetailSaleDocRowVO rowVO = null;
      ArrayList discRows = null;
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
            gridRowVO.getItemCodeItm01DOC02()
        );
        res = rowAction.executeCommand(docRowPK,userSessionPars,request,response,userSession,context);
        if (res.isError()) {
          conn.rollback();
          return res;
        }
        rowVO = (DetailSaleDocRowVO)((VOResponse)res).getVo();

        // check if the row to be inserted is in the selected delivery notes...
        if (!selectedItems.containsKey(rowVO.getItemCodeItm01DOC02()))
          continue;

        rowVO.setDocTypeDOC02(docVO.getDocTypeDOC01());
        rowVO.setDocNumberDOC02(docVO.getDocNumberDOC01());

        if (rowVO.getInvoiceQtyDOC02().doubleValue()<rowVO.getQtyDOC02().doubleValue()) {

          // this is the invoice qty to set
          qty = (BigDecimal)selectedItems.get(rowVO.getItemCodeItm01DOC02());

          // check if the invoice qty is less or equals to max invoice qty...
          if (qty.doubleValue()<=rowVO.getQtyDOC02().subtract(rowVO.getInvoiceQtyDOC02()).doubleValue())
            rowVO.setQtyDOC02(qty);
          else
            rowVO.setQtyDOC02(rowVO.getQtyDOC02().subtract(rowVO.getInvoiceQtyDOC02().setScale(rowVO.getDecimalsReg02DOC02().intValue(),BigDecimal.ROUND_HALF_UP)));

          rowVO.setTaxableIncomeDOC02(rowVO.getQtyDOC02().multiply(rowVO.getValueSal02DOC02()).setScale(docVO.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP));
          rowVO.setTotalDiscountDOC02( new BigDecimal(0) );

          // calculate row vat...
          double vatPerc = rowVO.getValueReg01DOC02().doubleValue()*(1d-rowVO.getDeductibleReg01DOC02().doubleValue()/100d)/100;
          rowVO.setVatValueDOC02(rowVO.getTaxableIncomeDOC02().multiply(new BigDecimal(vatPerc)).setScale(docVO.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP));

          // calculate row total...
          rowVO.setValueDOC02(rowVO.getTaxableIncomeDOC02().add(rowVO.getVatValueDOC02()));

          res = insRowBean.insertSaleItem(conn, rowVO, userSessionPars, request, response, userSession, context);
          if (res.isError()) {
            conn.rollback();
            return res;
          }

          // create item discounts...
          gridParams.getOtherGridParams().put(ApplicationConsts.SALE_DOC_ROW_PK,docRowPK);
          res = itemDiscAction.executeCommand(gridParams,userSessionPars,request,response,userSession,context);
          if (res.isError()) {
            conn.rollback();
            return res;
          }
          discRows = ((VOListResponse)res).getRows();
          for(int j=0;j<discRows.size();j++) {
            itemDiscVO = (SaleItemDiscountVO)discRows.get(j);
            itemDiscVO.setDocTypeDOC04(docVO.getDocTypeDOC01());
            itemDiscVO.setDocNumberDOC04(docVO.getDocNumberDOC01());
            res = insItemDiscBean.insertSaleDocRowDiscount(conn,itemDiscVO,userSessionPars,request,response,userSession,context);
            if (res.isError()) {
              conn.rollback();
              return res;
            }
          }

        }
      }



      // create charges...
      gridParams = new GridParams();
      gridParams.getOtherGridParams().put(ApplicationConsts.SALE_DOC_PK,refPK);
      res = chargesAction.executeCommand(gridParams,userSessionPars,request,response,userSession,context);
      SaleDocChargeVO chargeVO = null;
      if (res.isError()) {
        conn.rollback();
        return res;
      }
      rows = ((VOListResponse)res).getRows();
      for(int i=0;i<rows.size();i++) {
        chargeVO = (SaleDocChargeVO)rows.get(i);
        chargeVO.setDocTypeDOC03(docVO.getDocTypeDOC01());
        chargeVO.setDocNumberDOC03(docVO.getDocNumberDOC01());
        if (chargeVO.getValueDOC03()==null ||
            chargeVO.getValueDOC03()!=null && chargeVO.getInvoicedValueDOC03().doubleValue()<chargeVO.getValueDOC03().doubleValue()) {
          if (chargeVO.getValueDOC03()!=null)
            chargeVO.setValueDOC03(chargeVO.getValueDOC03().subtract(chargeVO.getInvoicedValueDOC03()).setScale(docVO.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP));

          res = insChargeBean.insertSaleDocCharge(conn,chargeVO,userSessionPars,request,response,userSession,context);
          if (res.isError()) {
            conn.rollback();
            return res;
          }
        }
      }



      // create activities...
      res = actAction.executeCommand(gridParams,userSessionPars,request,response,userSession,context);
      if (res.isError()) {
        conn.rollback();
        return res;
      }
      SaleDocActivityVO actVO = null;
      rows = ((VOListResponse)res).getRows();
      for(int i=0;i<rows.size();i++) {
        actVO = (SaleDocActivityVO)rows.get(i);
        actVO.setDocTypeDOC13(docVO.getDocTypeDOC01());
        actVO.setDocNumberDOC13(docVO.getDocNumberDOC01());
        if (actVO.getInvoicedValueDOC13().doubleValue()<actVO.getValueDOC13().doubleValue()) {
          actVO.setValueDOC13(actVO.getValueDOC13().subtract(actVO.getInvoicedValueDOC13()).setScale(docVO.getDecimalsREG03().intValue(),BigDecimal.ROUND_HALF_UP));
          res = insActBean.insertSaleActivity(conn,actVO,userSessionPars,request,response,userSession,context);
          if (res.isError()) {
            conn.rollback();
            return res;
          }
        }
      }



      // create header discounts...
      res = discAction.executeCommand(gridParams,userSessionPars,request,response,userSession,context);
      if (res.isError()) {
        conn.rollback();
        return res;
      }
      SaleDocDiscountVO discVO = null;
      rows = ((VOListResponse)res).getRows();
      for(int i=0;i<rows.size();i++) {
        discVO = (SaleDocDiscountVO)rows.get(i);
        discVO.setDocTypeDOC05(docVO.getDocTypeDOC01());
        discVO.setDocNumberDOC05(docVO.getDocNumberDOC01());
        res = insDiscBean.insertSaleDocDiscount(conn,discVO,userSessionPars,request,response,userSession,context);
        if (res.isError()) {
          conn.rollback();
          return res;
        }
      }



      // recalculate all taxable incomes, vats, totals...
      SaleDocPK pk = new SaleDocPK(
        docVO.getCompanyCodeSys01DOC01(),
        docVO.getDocTypeDOC01(),
        docVO.getDocYearDOC01(),
        docVO.getDocNumberDOC01()
      );
      res = totals.updateTaxableIncomes(conn,pk,userSessionPars,request,response,userSession,context);
      if (res.isError()) {
        conn.rollback();
        return res;
      }



      // reload doc header with updated totals...
      Response answer = docAction.loadSaleDoc(conn,pk,userSessionPars,request,response,userSession,context);

      // fires the GenericEvent.BEFORE_COMMIT event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        getRequestName(),
        GenericEvent.BEFORE_COMMIT,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        inputPar,
        answer
      ));

      conn.commit();

      // fires the GenericEvent.AFTER_COMMIT event...
      EventsManager.getInstance().processEvent(new GenericEvent(
        this,
        getRequestName(),
        GenericEvent.AFTER_COMMIT,
        (JAIOUserSessionParameters)userSessionPars,
        request,
        response,
        userSession,
        context,
        conn,
        inputPar,
        answer
      ));

      return answer;
    }
    catch (Throwable ex) {
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while creating a sale invoice from a set of out delivery notes",ex);
      try {
        conn.rollback();
      }
      catch (Exception ex3) {
      }
      return new ErrorResponse(ex.getMessage());
    }
    finally {
      try {
        pstmt.close();
      }
      catch (Exception ex2) {
      }

      try {
        ConnectionManager.releaseConnection(conn, context);
      }
      catch (Exception ex1) {
      }
    }
  }



}
