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
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to create a sale document (order or contract), based on the specified sale estimate doc.</p>
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
public class CreateSaleDocFromEstimateAction implements Action {

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


  public CreateSaleDocFromEstimateAction() {}

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "createSaleDocFromEstimate";
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
      SaleDocPK pk = (SaleDocPK)inputPar;

      // retrieve internationalization settings (Resources object)...
      ServerResourcesFactory factory = (ServerResourcesFactory)context.getAttribute(Controller.RESOURCES_FACTORY);
      Resources resources = factory.getResources(userSessionPars.getLanguageId());

      // retrieve document header...
      Response res = docAction.loadSaleDoc(conn,pk,userSessionPars,request,response,userSession,context);
      if (res.isError()) {
        conn.rollback();
        return res;
      }
      DetailSaleDocVO docVO = (DetailSaleDocVO)((VOResponse)res).getVo();



      // retrieve document item rows...
      GridParams gridParams = new GridParams();
      gridParams.getOtherGridParams().put(ApplicationConsts.SALE_DOC_PK,pk);
      res = rowsAction.executeCommand(gridParams,userSessionPars,request,response,userSession,context);
      if (res.isError()) {
        conn.rollback();
        return res;
      }
      java.util.List rows = ((VOListResponse)res).getRows();

      HashMap map = new HashMap();
      map.put(ApplicationConsts.COMPANY_CODE_SYS01,docVO.getCompanyCodeSys01DOC01());
      LookupValidationParams pars = new LookupValidationParams(docVO.getCustomerCodeSAL07(),map);
      res = custAction.executeCommand(pars,userSessionPars,request,response,userSession,context);
      if (res.isError()) {
        conn.rollback();
        return res;
      }
      GridCustomerVO custVO = (GridCustomerVO)((VOListResponse)res).getRows().get(0);

      // create header...
      if (custVO.getSubjectTypeREG04().equals(ApplicationConsts.SUBJECT_ORGANIZATION_CUSTOMER))
        docVO.setDocTypeDOC01(ApplicationConsts.SALE_ORDER_DOC_TYPE);
      else if (custVO.getSubjectTypeREG04().equals(ApplicationConsts.SUBJECT_PEOPLE_CUSTOMER))
        docVO.setDocTypeDOC01(ApplicationConsts.SALE_CONTRACT_DOC_TYPE);
      docVO.setDocRefNumberDOC01(resources.getResource("estimate nr. ")+docVO.getDocSequenceDOC01()+"/"+docVO.getDocYearDOC01());
      docVO.setDocSequenceDOC01(null);
      res = insDocBean.insertSaleDoc(conn,docVO,userSessionPars,request,response,userSession,context);
      if (res.isError()) {
        conn.rollback();
        return res;
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
        res = rowAction.executeCommand(docRowPK,userSessionPars,request,response,userSession,context);
        if (res.isError()) {
          conn.rollback();
          return res;
        }
        rowVO = (DetailSaleDocRowVO)((VOResponse)res).getVo();
        rowVO.setDocTypeDOC02(docVO.getDocTypeDOC01());
        rowVO.setDocNumberDOC02(docVO.getDocNumberDOC01());
        res = insRowBean.insertSaleItem(conn,rowVO,userSessionPars,request,response,userSession,context);
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

      // create charges...
      gridParams = new GridParams();
      gridParams.getOtherGridParams().put(ApplicationConsts.SALE_DOC_PK,pk);
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
        res = insChargeBean.insertSaleDocCharge(conn,chargeVO,userSessionPars,request,response,userSession,context);
        if (res.isError()) {
          conn.rollback();
          return res;
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
        res = insActBean.insertSaleActivity(conn,actVO,userSessionPars,request,response,userSession,context);
        if (res.isError()) {
          conn.rollback();
          return res;
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
      res = totals.updateTaxableIncomes(conn,pk,userSessionPars,request,response,userSession,context);
      if (res.isError()) {
        conn.rollback();
        return res;
      }

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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while creating a sale document from a sale estimate document",ex);
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
