package org.jallinone.purchases.documents.server;

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
import org.jallinone.purchases.documents.java.*;
import org.jallinone.purchases.documents.server.*;
import org.openswing.swing.message.receive.java.VOListResponse;
import org.openswing.swing.message.send.java.GridParams;
import org.openswing.swing.internationalization.server.ServerResourcesFactory;
import org.openswing.swing.internationalization.java.Resources;
import org.jallinone.purchases.suppliers.server.ValidateSupplierCodeAction;
import org.jallinone.purchases.suppliers.java.GridSupplierVO;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to create a purchase invoice from the specified purchase order document.</p>
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
public class CreateInvoiceFromPurchaseDocAction implements Action {

  LoadPurchaseDocRowsAction rowsAction = new LoadPurchaseDocRowsAction();
  LoadPurchaseDocBean docAction = new LoadPurchaseDocBean();
  LoadPurchaseDocRowAction rowAction = new LoadPurchaseDocRowAction();
  InsertPurchaseDocAction insDocAction = new InsertPurchaseDocAction();
  InsertPurchaseDocRowAction insRowAction = new InsertPurchaseDocRowAction();
  ValidateSupplierCodeAction custAction = new ValidateSupplierCodeAction();


  public CreateInvoiceFromPurchaseDocAction() {}

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "createInvoiceFromPurchaseDoc";
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
      DetailPurchaseDocVO docVO = (DetailPurchaseDocVO)inputPar;

      // retrieve internationalization settings (Resources object)...
      ServerResourcesFactory factory = (ServerResourcesFactory)context.getAttribute(Controller.RESOURCES_FACTORY);
      Resources resources = factory.getResources(userSessionPars.getLanguageId());

      // insert header...
      docVO.setDocStateDOC06(ApplicationConsts.HEADER_BLOCKED);
      Response res = insDocAction.executeCommand(docVO,userSessionPars,request,response,userSession,context);
      if (res.isError())
        return res;

      PurchaseDocPK refPK = new PurchaseDocPK(
        docVO.getCompanyCodeSys01Doc06DOC06(),
        docVO.getDocTypeDoc06DOC06(),
        docVO.getDocYearDoc06DOC06(),
        docVO.getDocNumberDoc06DOC06()
      );

      // retrieve ref. document item rows...
      GridParams gridParams = new GridParams();
      gridParams.getOtherGridParams().put(ApplicationConsts.PURCHASE_DOC_PK,refPK);
      res = rowsAction.executeCommand(gridParams,userSessionPars,request,response,userSession,context);
      if (res.isError())
        return res;
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
        res = rowAction.executeCommand(docRowPK,userSessionPars,request,response,userSession,context);
        if (res.isError())
          return res;
        rowVO = (DetailPurchaseDocRowVO)((VOResponse)res).getVo();
        rowVO.setDocTypeDOC07(docVO.getDocTypeDOC06());
        rowVO.setDocNumberDOC07(docVO.getDocNumberDOC06());
        if (rowVO.getInvoiceQtyDOC07().doubleValue()<rowVO.getQtyDOC07().doubleValue()) {
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

          res = insRowAction.executeCommand(rowVO, userSessionPars, request, response, userSession, context);
          if (res.isError())
            return res;

        }
      }

      // reload doc header with updated totals...
      PurchaseDocPK pk = new PurchaseDocPK(
        docVO.getCompanyCodeSys01DOC06(),
        docVO.getDocTypeDOC06(),
        docVO.getDocYearDOC06(),
        docVO.getDocNumberDOC06()
      );
      Response answer = docAction.loadPurchaseDoc(conn,pk,userSessionPars,request,response,userSession,context);

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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while creating a purchase invoice from a purchase order document",ex);
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
