package org.jallinone.production.orders.server;

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
import org.jallinone.production.orders.java.*;
import org.openswing.swing.message.send.java.GridParams;
import org.jallinone.production.billsofmaterial.server.LoadBillOfMaterialBean;
import org.jallinone.items.java.ItemPK;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import org.jallinone.production.billsofmaterial.java.MaterialVO;
import org.jallinone.warehouse.availability.server.LoadItemAvailabilitiesAction;
import org.jallinone.warehouse.availability.java.ItemAvailabilityVO;
import org.openswing.swing.internationalization.server.ServerResourcesFactory;
import org.openswing.swing.internationalization.java.Resources;
import org.jallinone.warehouse.movements.server.AddMovementBean;
import org.jallinone.warehouse.movements.java.WarehouseMovementVO;
import org.jallinone.events.server.EventsManager;
import org.jallinone.events.server.GenericEvent;


/**
 * <p>Title: JAllInOne ERP/CRM application</p>
 * <p>Description: Action class used to close a production order, i.e.
 * - load all products into the specified warehouse
 * - change doc state to close
 * </p>
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
public class CloseProdOrderAction implements Action {

  private LoadProdOrderProductsAction rowsAction = new LoadProdOrderProductsAction();
  private AddMovementBean mov = new AddMovementBean();


  public CloseProdOrderAction() {}

  /**
   * @return request name
   */
  public final String getRequestName() {
    return "closeProdOrder";
  }


  /**
   * Business logic to execute.
   */
  public final Response executeCommand(Object inputPar,UserSessionParameters userSessionPars,HttpServletRequest request, HttpServletResponse response,HttpSession userSession,ServletContext context) {
    String serverLanguageId = ((JAIOUserSessionParameters)userSessionPars).getServerLanguageId();
    Connection conn = null;
    PreparedStatement pstmt = null;
    try {
      // retrieve internationalization settings (Resources object)...
      ServerResourcesFactory factory = (ServerResourcesFactory) context.getAttribute(Controller.RESOURCES_FACTORY);
      Resources resources = factory.getResources(userSessionPars.getLanguageId());

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

      DetailProdOrderVO vo = (DetailProdOrderVO)inputPar;


      // update order state...
      pstmt = conn.prepareStatement("update DOC22_PRODUCTION_ORDER set DOC_STATE=? where COMPANY_CODE_SYS01=? and DOC_YEAR=? and DOC_NUMBER=?");
      pstmt.setString(1, ApplicationConsts.CLOSED);
      pstmt.setString(2, vo.getCompanyCodeSys01DOC22());
      pstmt.setBigDecimal(3, vo.getDocYearDOC22());
      pstmt.setBigDecimal(4, vo.getDocNumberDOC22());
      pstmt.execute();
      pstmt.close();
      vo.setDocStateDOC22(ApplicationConsts.CLOSED);


      // retrieve products defined in the production order...
      GridParams gridParams = new GridParams();
      gridParams.getOtherGridParams().put(ApplicationConsts.PROD_ORDER_PK,new ProdOrderPK(vo.getCompanyCodeSys01DOC22(),vo.getDocYearDOC22(),vo.getDocNumberDOC22()));
      Response res = rowsAction.executeCommand(gridParams,userSessionPars,request,response,userSession,context);
      if (res.isError()) {
        conn.rollback();
        return res;
      }
      java.util.List products = ((VOListResponse)res).getRows();


      // add created products to warehouse locations...
      ItemPK pk = null;
      ProdOrderProductVO prodVO = null;
      WarehouseMovementVO movVO = null;
      ArrayList serialNumbers = new ArrayList();
      for(int i=0;i<products.size();i++) {
        prodVO = (ProdOrderProductVO)products.get(i);

        // create a warehouse movement...
        movVO = new WarehouseMovementVO(
          prodVO.getProgressiveHie01DOC23(),
          prodVO.getQtyDOC23(),
          vo.getCompanyCodeSys01DOC22(),
          vo.getWarehouseCode2War01DOC22(),
          prodVO.getItemCodeItm01DOC23(),
          ApplicationConsts.WAREHOUSE_MOTIVE_LOAD_BY_PRODUCTION,
          ApplicationConsts.ITEM_GOOD,
          resources.getResource("load items from production order")+" "+vo.getDocSequenceDOC22()+"/"+vo.getDocYearDOC22(),
          serialNumbers,

         ApplicationConsts.JOLLY,
         ApplicationConsts.JOLLY,
         ApplicationConsts.JOLLY,
         ApplicationConsts.JOLLY,
         ApplicationConsts.JOLLY,
         ApplicationConsts.JOLLY,
         ApplicationConsts.JOLLY,
         ApplicationConsts.JOLLY,
         ApplicationConsts.JOLLY,
         ApplicationConsts.JOLLY

          /*
          prodVO.getVariantCodeItm11DOC23(),
          prodVO.getVariantCodeItm12DOC23(),
          prodVO.getVariantCodeItm13DOC23(),
          prodVO.getVariantCodeItm14DOC23(),
          prodVO.getVariantCodeItm15DOC23(),
          prodVO.getVariantTypeItm06DOC23(),
          prodVO.getVariantTypeItm07DOC23(),
          prodVO.getVariantTypeItm08DOC23(),
          prodVO.getVariantTypeItm09DOC23(),
          prodVO.getVariantTypeItm10DOC23()
          */
        );
        res = mov.addWarehouseMovement(conn,movVO,userSessionPars,request,response,userSession,context);
        if (res.isError()) {
          conn.rollback();
          return res;
        }
      }

      Response answer = new VOResponse(vo);

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
      Logger.error(userSessionPars.getUsername(),this.getClass().getName(),"executeCommand","Error while closing a production order",ex);
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
